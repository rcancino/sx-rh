package com.luxsoft.sw4.cfdi


import com.luxsoft.sw4.cfdi.CFDIUtils
import com.luxsoft.sw4.cfdi.Folio
import com.luxsoft.sw4.rh.NominaPorEmpleado

import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Conceptos
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Conceptos.Concepto
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Emisor
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Receptor
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Impuestos
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.TipoDeComprobante



class CfdiBuilder {

	private nextFolio(def empresa){
		Folio folio=Folio.findOrSaveWhere(empresa:empresa,serie:'NOMINA_CFDI')
		return folio.next().toString()
	}
    
	
	ComprobanteDocument build(def nominaEmpleado) {
		
		def empresa = nominaEmpleado.nomina.empresa
		def empleado = nominaEmpleado.empleado
		def fecha = new Date()

		ComprobanteDocument document=ComprobanteDocument.Factory.newInstance()
		Comprobante comprobante=document.addNewComprobante()
		comprobante.version = '3.2'
		comprobante.serie = 'NOMINA_CFDI'
		comprobante.folio = nextFolio(empresa)
		
		comprobante.setFecha(CFDIUtils.toXmlDate(fecha).getCalendarValue())
		comprobante.setFormaDePago("PAGO EN UNA SOLA EXHIBICION")
		comprobante.setMetodoDePago('NA')
		comprobante.setMoneda(Currency.getInstance(new Locale("es","mx")).currencyCode)
		comprobante.setTipoCambio("1.0")
		comprobante.setTipoDeComprobante(TipoDeComprobante.EGRESO)
		comprobante.setLugarExpedicion(empresa.direccion.codigoPostal)
		
		
		
		Emisor emisor=comprobante.addNewEmisor()
		emisor.setRfc(empresa.rfc)
		emisor.setNombre(empresa.nombre)
		emisor.setRegimen('601') // PENDIENTE

		Receptor receptor=comprobante.addNewReceptor()
		receptor.setNombre(empleado.toString())
		receptor.setRfc(empleado.rfc)
		
        //Conceptos
		Conceptos conceptos=comprobante.addNewConceptos()
		Concepto c=conceptos.addNewConcepto();
		c.setCantidad(1);
		c.setUnidad("ACT");
		c.setDescripcion('Pago de Nomina');
		/*c.setValorUnitario(complementoNomina.totalPercepciones + complementoNomina.totalOtrosPagos);
		c.setImporte(complementoNomina.totalPercepciones + complementoNomina.totalOtrosPagos);
		
		
		comprobante.setSubTotal(complementoNomina.totalPercepciones + complementoNomina.totalOtrosPagos)
		comprobante.setDescuento(complementoNomina.totalDeducciones)
        comprobante.setTotal(complementoNomina.totalPercepciones + complementoNomina.totalOtrosPagos - complementoNomina.totalDeducciones)
		*/
		// Impuestos
		comprobante.addNewImpuestos()
		CFDIUtils.depurar(document)

		return document
	}

	
}
