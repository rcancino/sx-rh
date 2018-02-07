package com.luxsoft.sw4.cfdi

import java.math.RoundingMode
import grails.transaction.Transactional

import org.apache.xmlbeans.XmlCursor
import org.apache.xmlbeans.XmlObject
import org.apache.xmlbeans.XmlOptions
import org.apache.xmlbeans.XmlValidationError

import com.luxsoft.sw4.cfdi.CFDIUtils
import com.luxsoft.sw4.cfdi.Cfdi
import com.luxsoft.sw4.cfdi.CfdiException
import com.luxsoft.sw4.cfdi.Folio
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.cfdi.nomina12.NominaUtils

import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante

import com.edicom.ediwinws.cfdi.client.CfdiClient
import com.edicom.ediwinws.service.cfdi.CancelaResponse

import org.bouncycastle.util.encoders.Base64

import lx.cfdi.v33.Comprobante
import lx.cfdi.v33.CfdiUtils
import com.luxsoft.cfdix.v33.CfdiBuilder33 


@Transactional
class CfdiService {
	
	def cfdiSellador

	def cfdiSellador33

	def  cfdiTimbradoService


	Cfdi regenerarCfdi(NominaPorEmpleado ne){
		if(ne.cfdi == null) 
			return generarCfdi(ne)
		assert !ne.cfdi.uuid, "NominaPorEmpleado ${ne.id} ya timbrada: ${ne.cfdi.uuid}"
		ne.cfdi.delete flush:true
		return generarCfdi(ne);
	}


	Cfdi generarCfdi(NominaPorEmpleado ne) {
		
		assert !ne.cfdi , "NominaPorEmpleado ${ne.id} ya tiene  gnerado un CFDI"

		CfdiBuilder33 builder = new CfdiBuilder33()
		def comprobante = builder.build(ne)
		cfdiSellador33.sellar(comprobante)
		def cfdi = new Cfdi()
		cfdi.serie=comprobante.serie
		cfdi.folio=comprobante.folio
		cfdi.fecha = Date.parse("yyyy-MM-dd'T'HH:mm:ss", comprobante.fecha)
		cfdi.emisor=comprobante.emisor.nombre
		cfdi.receptor=comprobante.receptor.nombre
		cfdi.receptorRfc=comprobante.receptor.rfc
		cfdi.total=comprobante.total
		cfdi.xml = CfdiUtils.serialize(comprobante).getBytes()
		cfdi.setXmlName("$cfdi.receptorRfc_${cfdi.serie}_${cfdi.folio}.xml")
		cfdi.save failOnError:true, flush:true
		ne.cfdi = cfdi
		ne.save()
		return cfdi
	}
	

	Cfdi timbrar(NominaPorEmpleado ne) {
		assert ne.cfdi , "No se ha generado archivo XML para timbrar la nomina ${ne.id} de ${ne.empleado} "
		assert ne.cfdi.uuid == null , "La nomina ${ne.id} de ${ne.empleado} ya esta timbrada UUID: ${ne.cfdi.uuid}"
		def cfdi = cfdiTimbradoService.timbrar(ne.cfdi)
		return cfdi
		
	}
	

	@Transactional
	def CancelacionDeCfdi cancelar(Cfdi cfdi,String comentario){

		CancelacionDeCfdi cancel=new CancelacionDeCfdi()
		cancel.cfdi=cfdi

		def empresa=Empresa.first()
		
		byte[] pfxData=empresa.certificadoDigitalPfx //grailsApplication.mainContext.getResource("/WEB-INF/sat/gasoc.pfx").file.readBytes()
		String[] uuids=[cfdi.uuid]
		def client=new CfdiClient()
		CancelaResponse res=client.cancelCfdi(
				empresa.usuarioPac
				, empresa.passwordPac
				, empresa.getRfc()
				, uuids
				, pfxData
				, empresa.passwordPfx);
		String msg=res.getText()
		log.info('Message: ' + new String(msg))
		//cancel.message=Base64.decode(msg)
		String aka=res.getAck()
		cancel.aka=Base64.decode(aka.getBytes())
		cancel.save failOnError:true

		def nominaEmpleado=NominaEmpleado.findByCfdi(cfdi)
		if(nominaEmpleado){
			cancel.comentario="Nomina por empleado origen: "+nominaEmpleado.id
			nominaEmpleado.cfdi=null
			nominaEmpleado.save()
		}
		return cancel

	}
}
