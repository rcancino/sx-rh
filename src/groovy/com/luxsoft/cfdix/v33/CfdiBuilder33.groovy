package com.luxsoft.cfdix.v33

import org.apache.commons.logging.LogFactory
import org.bouncycastle.util.encoders.Base64

import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.MonedaUtils

import lx.cfdi.utils.DateUtils
import lx.cfdi.v33.ObjectFactory
import lx.cfdi.v33.Comprobante
import lx.cfdi.v33.nomina.Nomina

// Catalogos
import lx.cfdi.v33.CUsoCFDI
import lx.cfdi.v33.CMetodoPago
import lx.cfdi.v33.CTipoDeComprobante
import lx.cfdi.v33.CMoneda
import lx.cfdi.v33.CTipoFactor

/**
 * TODO: Parametrizar el regimenFiscal de
 */
class CfdiBuilder33 {

	private static final log=LogFactory.getLog(this)

    private factory = new ObjectFactory();
	private Comprobante comprobante;
    private Empresa empresa
    private NominaPorEmpleado ne;
    private Nomina nomina

    def build(NominaPorEmpleado ne){
        this.nomina = new NominaBuilder().build(ne)
        buildComprobante(ne)
        .buildEmisor()
        .buildReceptor()
        .buildFormaDePago()
        .buildConceptos()
        .buildTotales()
        .buildCertificado()
        
        Comprobante.Complemento complemento = factory.createComprobanteComplemento()
        complemento.any.add(nomina)
        comprobante.complemento = complemento

        return comprobante
    }
    

	def buildComprobante(NominaPorEmpleado ne){
		log.info("Generando CFDI 3.3 para ne: ${ne.id}")

		this.comprobante = factory.createComprobante()
        this.ne = ne;
        this.empresa = Empresa.first()
        comprobante.version = "3.3"
        comprobante.tipoDeComprobante = CTipoDeComprobante.N
        comprobante.serie = 'NOMINA12'
        comprobante.folio = ne.id
        comprobante.setFecha(DateUtils.getCfdiDate(new Date()))
        comprobante.moneda = CMoneda.MXN
        comprobante.lugarExpedicion = empresa.direccion.codigoPostal
        return this
	}

    def buildEmisor(){
        /**** Emisor ****/
        Comprobante.Emisor emisor = factory.createComprobanteEmisor()
        emisor.rfc = empresa.rfc
        emisor.nombre = empresa.nombre
        emisor.regimenFiscal = '601' 
        comprobante.emisor = emisor
        return this
    }

    def buildReceptor(){
        /** Receptor ***/
        Comprobante.Receptor receptor = factory.createComprobanteReceptor()
        receptor.rfc = ne.empleado.rfc
        receptor.nombre = ne.empleado.nombre
        receptor.usoCFDI = CUsoCFDI.P_01
        comprobante.receptor = receptor
        return this
    }

    def buildFormaDePago(){
        comprobante.formaPago = '99'
        comprobante.metodoPago = CMetodoPago.PUE
        return this
    }

    def buildConceptos(){
        /** Conceptos ***/
        Comprobante.Conceptos conceptos = factory.createComprobanteConceptos()
        Comprobante.Conceptos.Concepto concepto = factory.createComprobanteConceptosConcepto()
        concepto.claveProdServ = "84111505"
        concepto.cantidad = 1
        concepto.claveUnidad = 'ACT'
        concepto.descripcion = 'Pago de nómina'
        def totalPercepciones = nomina.totalPercepciones?:0.0
        def totalOtrosPagos = nomina.totalOtrosPagos?:0.0
        concepto.setValorUnitario(totalPercepciones + totalOtrosPagos);
        concepto.setImporte(totalPercepciones + totalOtrosPagos);
        concepto.setDescuento(nomina.totalDeducciones?: 0.0)
        conceptos.concepto.add(concepto)
        comprobante.conceptos = conceptos
        return this
    }
    

    def buildTotales() {
        def totalPercepciones = nomina.totalPercepciones?:0.0
        def totalOtrosPagos = nomina.totalOtrosPagos?:0.0
        def totalDeducciones = nomina.totalDeducciones?:0.0
        comprobante.setSubTotal(totalPercepciones + totalOtrosPagos)
        comprobante.setDescuento(totalDeducciones)
        comprobante.setTotal(totalPercepciones + totalOtrosPagos - totalDeducciones)
        return this
    }

    def buildCertificado(){
        comprobante.setNoCertificado(empresa.numeroDeCertificado)
        byte[] encodedCert=Base64.encode(empresa.getCertificado().getEncoded())
        comprobante.setCertificado(new String(encodedCert))
        return this

    }

    Comprobante getComprobante(){
        return this.comprobante
    }

    
	

}
