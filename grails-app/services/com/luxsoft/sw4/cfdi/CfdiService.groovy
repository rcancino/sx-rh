package com.luxsoft.sw4.cfdi

import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject
import org.apache.xmlbeans.XmlOptions
import org.apache.xmlbeans.XmlValidationError
import org.bouncycastle.util.encoders.Base64

import com.luxsoft.sw4.cfdi.CFDIUtils
import com.luxsoft.sw4.cfdi.Cfdi
import com.luxsoft.sw4.cfdi.CfdiException
import com.luxsoft.sw4.cfdi.Folio
import com.luxsoft.sw4.rh.NominaPorEmpleado;

import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Complemento
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Conceptos
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Conceptos.Concepto
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Emisor
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Impuestos;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Impuestos.Retenciones;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Impuestos.Retenciones.Retencion;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Impuestos.Retenciones.Retencion.Impuesto;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Receptor
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.TipoDeComprobante
import mx.gob.sat.nomina.NominaDocument
import mx.gob.sat.nomina.NominaDocument.Nomina
import mx.gob.sat.nomina.NominaDocument.Nomina.Deducciones
import mx.gob.sat.nomina.NominaDocument.Nomina.Deducciones.Deduccion
import mx.gob.sat.nomina.NominaDocument.Nomina.Incapacidades
import mx.gob.sat.nomina.NominaDocument.Nomina.Incapacidades.Incapacidad
import mx.gob.sat.nomina.NominaDocument.Nomina.Percepciones
import mx.gob.sat.nomina.NominaDocument.Nomina.Percepciones.Percepcion
import grails.transaction.Transactional

import com.edicom.ediwinws.cfdi.client.CfdiClient
import org.bouncycastle.util.encoders.Base64
import com.edicom.ediwinws.service.cfdi.CancelaResponse

@Transactional
class CfdiService {
	
	def cfdiSellador
	
	def cfdiTimbrador

    
	
	Cfdi generarComprobante(def nominaEmpleadoId) {
		
		def fecha=new Date()
		//def fecha=Date.parse('dd/MM/yyy hh:mm:ss','30/06/2014 19:'+new Date().format('mm:ss'))
		def nominaEmpleado=NominaPorEmpleado.get(nominaEmpleadoId)
		assert nominaEmpleado,'No existe la nomina empleado: '+nominaEmpleadoId
		assert nominaEmpleado.cfdi==null,'Ya esta timbrada la nomina para el empleado: '+nominaEmpleado
		
		def empresa=nominaEmpleado.nomina.empresa
		def empleado=nominaEmpleado.empleado
		Folio folio=Folio.findOrSaveWhere(empresa:empresa,serie:'NOMINA_CFDI')
		ComprobanteDocument document=ComprobanteDocument.Factory.newInstance()
		Comprobante comprobante=document.addNewComprobante()
		CFDIUtils.depurar(document)
		
        comprobante.serie='NOMINA_CFDI'
		comprobante.folio=folio.next().toString()
		comprobante.setVersion("3.2")
		comprobante.setFecha(CFDIUtils.toXmlDate(fecha).getCalendarValue())
		comprobante.setFormaDePago("PAGO EN UNA SOLA EXHIBICION")
		comprobante.setMetodoDePago(nominaEmpleado.nomina.formaDePago.equals('CHEQUE')?'02':'03')
		comprobante.setMoneda(Currency.getInstance(new Locale("es","mx")).currencyCode)
		comprobante.setTipoCambio("1.0")
		
		comprobante.setTipoDeComprobante(TipoDeComprobante.EGRESO)
		comprobante.setLugarExpedicion(empresa.direccion.pais)
		comprobante.setNoCertificado(empresa.numeroDeCertificado)
		byte[] encodedCert=Base64.encode(empresa.getCertificado().getEncoded())
		comprobante.setCertificado(new String(encodedCert))
		
		Emisor emisor=CFDIUtils.registrarEmisor(comprobante, empresa)
		Receptor receptor=CFDIUtils.registrarReceptor(comprobante, nominaEmpleado.empleado)
		
        //Conceptos
		Conceptos conceptos=comprobante.addNewConceptos()
		Concepto c=conceptos.addNewConcepto();
		c.setCantidad(1);
		c.setUnidad("Servicio");
		c.setNoIdentificacion('CARGO');
		c.setDescripcion('Pago de Nomina');
		c.setValorUnitario(nominaEmpleado.percepciones);
		c.setImporte(nominaEmpleado.percepciones);
		
		generarComplemento(empresa, nominaEmpleado, comprobante) 
		
		// Importes
		comprobante.setSubTotal(nominaEmpleado.percepciones)
		comprobante.setDescuento(nominaEmpleado.conceptos.sum {
			def vv=0.0
			if(it.concepto.tipo=='DEDUCCION' && it.concepto.claveSat!=2) {
			  vv+=it.importeGravado+it.importeExcento
			}
			return vv
		})
		comprobante.setMotivoDescuento("Deducciones nómina")
		
        // Total
		def retenciones=nominaEmpleado.conceptos.sum {
			def vv=0.0
			if(it.concepto.tipo=='DEDUCCION' && it.concepto.claveSat==2) {
			  vv+=it.importeGravado+it.importeExcento
			}
			return vv
		}
		if(retenciones==null) 
			retenciones=0.0
		if(comprobante.descuento==null)
			comprobante.descuento=0.0
		if(comprobante.subTotal==null)
			comprobante.subTotal=0.0
		comprobante.setTotal(comprobante.subTotal-comprobante.descuento-retenciones)
		
		// Impuestos
		Impuestos impuestos=comprobante.addNewImpuestos()
		impuestos.setTotalImpuestosRetenidos(retenciones)
		
		Retenciones retNod=impuestos.addNewRetenciones()
		Retencion ret=retNod.addNewRetencion()
		ret.importe=retenciones
		ret.impuesto=Impuesto.ISR
		
		comprobante.sello=cfdiSellador.sellar(empresa.privateKey,document)
		
		XmlOptions options = new XmlOptions()
    	options.setCharacterEncoding("UTF-8")
    	options.put( XmlOptions.SAVE_INNER )
    	options.put( XmlOptions.SAVE_PRETTY_PRINT )
    	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES )
    	options.put( XmlOptions.SAVE_USE_DEFAULT_NAMESPACE )
    	options.put(XmlOptions.SAVE_NAMESPACES_FIRST)
    	ByteArrayOutputStream os=new ByteArrayOutputStream()
    	document.save(os, options)
		validarDocumento(document)

		def cfdi=new Cfdi(comprobante)
		cfdi.xml=os.toByteArray()
		cfdi.setXmlName("$cfdi.receptorRfc-$cfdi.serie-$cfdi.folio"+".xml")
		
		
		//cfdi=cfdiTimbrador.timbrar(cfdi,"PAP830101CR3", "yqjvqfofb")
		//nominaEmpleado.cfdi=cfdi
		//println cfdi.xmlName
		return cfdi
        //return cfdi
		
	}

	def generarComplemento(def empresa, def nominaEmpleado, def comprobante){

		ComplementoDeNomina12Builder builder = new ComplementoDeNomina12Builder()
        builder.generarComplemento(empresa, nominaEmpleado, comprobante)
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
		final XmlOptions options=new XmlOptions();
		final List errors=new ArrayList();
		options.setErrorListener(errors);
		node.validate(options);
		return errors;
		
	}


	@Transactional
	def CancelacionDeCfdi cancelar(Cfdi cfdi,String comentario){

		CancelacionDeCfdi cancel=new CancelacionDeCfdi()
		cancel.cfdi=cfdi
		

		def empresa=Empresa.first()
		//byte[] pfxData=empresa.certificadoDigitalPfx
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
		println 'Message: '+ new String(msg)
		//cancel.message=Base64.decode(msg)
		String aka=res.getAck()
		//println 'Aka:'+aka

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
