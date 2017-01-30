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


@Transactional
class CfdiService {
	
	def cfdiSellador
	
	def cfdiTimbrador

	def cfdiBuilder 

	Cfdi generarCfdi(NominaPorEmpleado ne) {

		assert !ne.cfdi , "NominaPorEmpleado ${ne.id} ya tiene  gnerado un CFDI"

		ComprobanteDocument document = generarXml(ne)
		Comprobante comprobante = document.getComprobante()

		def cfdi = new Cfdi(comprobante)
		salvarXml(cfdi, document)

		cfdi.save failOnError:true, flush:true
		ne.cfdi = cfdi
		ne.save()
		return cfdi
	}

	Cfdi regenerarCfdi(NominaPorEmpleado ne){
		if(ne.cfdi == null) return generarCfdi(ne)
		assert !ne.cfdi.uuid, "NominaPorEmpleado ${ne.id} ya timbrada: ${ne.cfdi.uuid}"
		ne.cfdi.delete flush:true
		return generarCfdi(ne);
	}

	ComprobanteDocument generarXml(NominaPorEmpleado ne){
		ComprobanteDocument document = cfdiBuilder.build(ne)
		Comprobante comprobante = document.getComprobante()
		comprobante.folio = ne.id.toString()
		comprobante.serie = 'NOMINA12'
		comprobante.sello=cfdiSellador.sellar(ne.nomina.empresa.privateKey,document)
		return document
	}

	Cfdi salvarXml(Cfdi cfdi, ComprobanteDocument document){

		ByteArrayOutputStream os=new ByteArrayOutputStream()
    	document.save(os, NominaUtils.getXmlOptions())
    	cfdi.xml = os.toByteArray()
		cfdi.setXmlName("$cfdi.receptorRfc-$cfdi.serie-$cfdi.folio"+".xml")
		return cfdi;
	}

	Cfdi timbrar(NominaPorEmpleado ne) {
		assert ne.cfdi , "No se ha generado archivo XML para timbrar la nomina ${ne.id} de ${ne.empleado} "
		assert ne.cfdi.uuid == null , "La nomina ${ne.id} de ${ne.empleado} ya esta timbrada UUID: ${ne.cfdi.uuid}"
		def cfdi = cfdiTimbrador.timbrar(ne.cfdi,"PAP830101CR3", "yqjvqfofb")
		return cfdi
	}
	
	void validarDocumento(ComprobanteDocument document) {
		List<XmlValidationError> errores=findErrors(document);
		if(errores.size()>0){
			StringBuffer buff=new StringBuffer();
			for(XmlValidationError e:errores){
				buff.append(e.getMessage()+"\n");
			}
			throw new CfdiException(message:"Datos para generar el comprobante fiscal (CFD) incorrectos "+buff.toString());
		}
	}
	
	List findErrors(final XmlObject node){
		println 'Validando....' + node
		final XmlOptions options=new XmlOptions();
		final List errors=new ArrayList();
		options.setErrorListener(errors);
		node.validate(options);
		return errors
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
