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

//@Transactional
class CfdiService {
	
	def cfdiSellador
	
	def cfdiTimbrador

	def cfdiBuilder 

    
	
	Cfdi generarComprobante(def nominaEmpleadoId) {
		
		def nominaEmpleado=NominaPorEmpleado.get(nominaEmpleadoId)
		assert nominaEmpleado,'No existe la nomina empleado: ' + nominaEmpleadoId
		assert nominaEmpleado.cfdi==null,'Ya esta timbrada la nomina para el empleado: '+nominaEmpleado

		ComprobanteDocument document = cfdiBuilder.build(nominaEmpleado)
		Comprobante comprobante = document.getComprobante()
		
		Folio folio=Folio.findOrSaveWhere(empresa:empresa,serie:'NOMINA_CFDI')
		comprobante.folio=folio.next().toString()
		//comprobante.setFecha(CFDIUtils.toXmlDate(fecha).getCalendarValue())
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

		def cfdi = new Cfdi(comprobante)
		cfdi.xml = os.toByteArray()
		cfdi.setXmlName("$cfdi.receptorRfc-$cfdi.serie-$cfdi.folio"+".xml")
		
		
		//cfdi=cfdiTimbrador.timbrar(cfdi,"PAP830101CR3", "yqjvqfofb")
		//nominaEmpleado.cfdi=cfdi
		//println cfdi.xmlName
		return cfdi
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
