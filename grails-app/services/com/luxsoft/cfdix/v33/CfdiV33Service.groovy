package com.luxsoft.cfdix.v33

import grails.transaction.Transactional
import java.text.SimpleDateFormat


import lx.cfdi.v33.Comprobante
import lx.cfdi.v33.CfdiUtils
import lx.cfdi.v33.nomina.NominaUtils
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.cfdi.Cfdi

@Transactional
public class CfdiV33Service {


	def cfdiSellador33

	final static SimpleDateFormat CFDI_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

	def generar(NominaPorEmpleado ne){
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
		cfdi.versionCfdi = 3.3
		// cfdi.xml = CfdiUtils.serialize(comprobante).getBytes()
		cfdi.xml = NominaUtils.toXmlByteArray(comprobante)
		cfdi.setXmlName("${cfdi.receptorRfc}_${cfdi.serie}_${cfdi.folio}.xml")
		cfdi.save failOnError:true, flush:true
		ne.cfdi = cfdi
		ne.save()
		return cfdi
	}
	

	def toXml(Comprobante comprobante){
		return CfdiUtils.serialize(comprobante)
	}

}