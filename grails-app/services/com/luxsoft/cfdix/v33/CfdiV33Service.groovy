package com.luxsoft.cfdix.v33

import grails.transaction.Transactional
import java.text.SimpleDateFormat


import lx.cfdi.v33.Comprobante
import lx.cfdi.v33.CfdiUtils
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.cfdi.Cfdi

@Transactional
public class CfdiV33Service {

	def springSecurityService

	final static SimpleDateFormat CFDI_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

	def generar(NominaPorEmpleado ne){
		
		assert !ne.cfdi , "NominaPorEmpleado ${ne.id} ya tiene  gnerado un CFDI"

		CfdiBuilder33  builder = new CfdiBuilder33()
		CfdiSellador33 sellador = new CfdiSellador33()
		Comprobante comprobante = builder.build(ne)
		comprobante = sellador.sellar(comprobante, ne.empresa)

		def cfdi = new Cfdi()
		// cfdi.tipo="INGRESO"
		// cfdi.referencia="FACTURA"
		cfdi.serie = comprobante.serie
		cfdi.folio = comprobante.folio
		cfdi.fecha = CFDI_DATE_FORMAT.parse(comprobante.fecha)
		cfdi.emisor = comprobante.emisor.nombre
		//cfdi.emisorRfc = comprobante.emisor.rfc
		cfdi.receptor = comprobante.receptor.nombre
		cfdi.receptorRfc = comprobante.receptor.rfc
		cfdi.total = comprobante.total
		cfdi.versionCfdi = comprobante.version

		cfdi.xml = CfdiUtils.toXmlByteArray(comprobante)
		// cfdi.setXmlName("$cfdi.receptorRfc-${'CFDIV33'}-$cfdi.serie-$cfdi.folio"+".xml")
		cfdi.setXmlName("$cfdi.receptorRfc-$cfdi.serie-$cfdi.folio"+".xml")
		
		// def user=springSecurityService.getCurrentUser().username
		// ne.modificadoPor=user
		// cfdi.modificadoPor=user
		// cfdi.creadoPor=user
		cfdi.save(failOnError:true)
		ne.cfdi=cfdi
		return cfdi

	}
	

	def toXml(Comprobante comprobante){
		return CfdiUtils.serialize(comprobante)
	}

}