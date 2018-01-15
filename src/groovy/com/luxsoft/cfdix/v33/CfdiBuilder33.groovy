package com.luxsoft.cfdix.v33

import org.apache.commons.logging.LogFactory
import org.bouncycastle.util.encoders.Base64

import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.MonedaUtils

import lx.cfdi.utils.DateUtils
import lx.cfdi.v33.ObjectFactory
import lx.cfdi.v33.Comprobante

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

    def build(NominaPorEmpleado ne, String serie = 'FACTURA'){
        buildComprobante(ne, serie)
        .buildEmisor()
        .buildReceptor()
        .buildFormaDePago()
        .buildConceptos()
        .buildImpuestos()
        .buildTotales()
        .buildCertificado()
        return comprobante
    }
    

	def buildComprobante(NominaPorEmpleado ne){
		log.info("Generando CFDI 3.3 para ne: ${ne.id}")

		this.comprobante = factory.createComprobante()
        this.ne = ne;
        this.empresa = ne.empresa
        comprobante.version = "3.3"
        comprobante.tipoDeComprobante = CTipoDeComprobante.I
        comprobante.serie = 'NOMINA_CFDI'
        comprobante.folio = ne.folio
        comprobante.setFecha(DateUtils.getCfdiDate(ne.fecha))
        comprobante.moneda = CMoneda.MXN
        comprobante.lugarExpedicion = empresa.direccion.codigoPostal
        return this
	}

    def buildEmisor(){
        /**** Emisor ****/
        Comprobante.Emisor emisor = factory.createComprobanteEmisor()
        emisor.rfc = empresa.rfc
        emisor.nombre = empresa.nombre
        emisor.regimenFiscal = empresa.regimenClaveSat ?:'601' 
        comprobante.emisor = emisor
        return this
    }

    def buildReceptor(){
        /** Receptor ***/
        Comprobante.Receptor receptor = factory.createComprobanteReceptor()
        receptor.rfc = ne.cliente.rfc
        receptor.nombre = ne.cliente.nombre
        receptor.usoCFDI = CUsoCFDI.G_03 // Adquisicion de mercancías
        switch(ne.usoCfdi) {
            case 'G01':
                receptor.usoCFDI = CUsoCFDI.G_01
                break
            case 'G02':
                receptor.usoCFDI = CUsoCFDI.G_02
                break
            case 'G03':
                receptor.usoCFDI = CUsoCFDI.G_03
                break
            case 'P01':
                receptor.usoCFDI = CUsoCFDI.P_01
                break
            case 'I01':
                receptor.usoCFDI = CUsoCFDI.I_01
                break
          default:
              receptor.usoCFDI = CUsoCFDI.P_01
          break
        }
        comprobante.receptor = receptor
        return this
    }

    def buildFormaDePago(){
        switch (ne.formaDePago) {
            case 'EFECTIVO':
              comprobante.formaPago = '01'
              break
            case 'CHEQUE':
              comprobante.formaPago = '02'
              break
            case 'TRANSFERENCIA':
              comprobante.formaPago = '03'
              break
            case 'TARJETA_CREDITO':
              comprobante.formaPago = '04'
              break
            case 'TARJETA_DEBITO':
              comprobante.formaPago = '28'
              break
            default:
              comprobante.formaPago = '99'
        }
        switch(ne.metodoDePago) {
            case 'PPD':
                comprobante.metodoPago = CMetodoPago.PPD
                comprobante.condicionesDePago = 'Credito'
                break
            case 'PUE':
                comprobante.metodoPago = CMetodoPago.PUE
                break
        }
        
        return this
    }

    def buildConceptos(){
        /** Conceptos ***/
        Comprobante.Conceptos conceptos = factory.createComprobanteConceptos()
        ne.partidas.each{ det -> 
            Comprobante.Conceptos.Concepto concepto = factory.createComprobanteConceptosConcepto()
            concepto.with { 
                String desc = (det.comentario?:'')+' '+ ne.comentario?:''
                claveProdServ = det.producto.claveProdServ ?: "80131500" // Tomarlo del producto
                noIdentificacion = det.producto.clave
                cantidad = det.cantidad
                claveUnidad = det.producto.claveUnidadSat ?: 'EA'
                unidad = det.producto.unidadSat ?: 'Pieza'
                descripcion = desc
                valorUnitario = det.precio
                importe = det.importeNeto
                // Impuestos del concepto
                concepto.impuestos = factory.createComprobanteConceptosConceptoImpuestos()
                concepto.impuestos.traslados = factory.createComprobanteConceptosConceptoImpuestosTraslados()
                Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado traslado1 
                traslado1 = factory.createComprobanteConceptosConceptoImpuestosTrasladosTraslado()
                traslado1.base =  det.importeNeto
                traslado1.impuesto = '002'

                if( det.producto.impuesto == 0.0) {
                    traslado1.tipoFactor = CTipoFactor.EXENTO
                } else {
                    traslado1.tipoFactor = CTipoFactor.TASA
                    traslado1.tasaOCuota = '0.160000'
                    traslado1.importe = MonedaUtils.round(det.impuesto)
                }
                
                concepto.impuestos.traslados.traslado.add(traslado1)
                conceptos.concepto.add(concepto)

                def renta=Renta.findByneDet(det)
                if(renta){
                    Comprobante.Conceptos.Concepto.CuentaPredial cp = factory.createComprobanteConceptosConceptoCuentaPredial()
                    cp.setNumero(renta.arrendamiento.inmueble.cuentaPredial)
                    concepto.cuentaPredial = cp
                }
                comprobante.conceptos = conceptos

            }
        }
        return this
    }

    def buildImpuestos(){
        /** Impuestos **/
        Comprobante.Impuestos impuestos = factory.createComprobanteImpuestos()
        impuestos.setTotalImpuestosTrasladados(ne.impuesto)

        
        
        if(ne.impuesto > 0 ) {
            Comprobante.Impuestos.Traslados traslados = factory.createComprobanteImpuestosTraslados()
            Comprobante.Impuestos.Traslados.Traslado traslado = factory.createComprobanteImpuestosTrasladosTraslado()
            traslado.impuesto = '002'
            traslado.tipoFactor = CTipoFactor.TASA
            traslado.tasaOCuota = '0.160000'
            traslado.importe = ne.impuesto
            traslados.traslado.add(traslado)
            impuestos.traslados = traslados
        }
        
        comprobante.setImpuestos(impuestos)
        return this
    }

    def buildTotales(){
        comprobante.subTotal = ne.subTotal
        comprobante.total = ne.total
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
