package com.luxsoft.sw4.cfdi.nomina12

import org.apache.xmlbeans.XmlObject

import net.sf.jasperreports.engine.JRExporterParameter
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.JRPdfExporterParameter
import net.sf.jasperreports.export.PdfExporterConfiguration


import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.springframework.core.io.Resource


import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import mx.gob.sat.nomina12.NominaDocument
import mx.gob.sat.nomina12.NominaDocument.Nomina

import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.cfdi.Cfdi

import com.luxsoft.sw4.cfdi.CfdiPrintUtils;
import com.luxsoft.sw4.cfdi.ComplementoNomina;


class NominaPrintService {

	def imprimir(Cfdi cfdi, def params = [:]){
		//NominaPorEmpleado ne = NominaPorEmpleado.get(cfdi.folio)

		NominaPorEmpleado ne = NominaPorEmpleado.findByCfdi(cfdi)

		if(ne.nomina.ejercicio<2017){
				
			def pdfStream=imprimirCfdi(cfdi)
				return pdfStream
		
		}

		Comprobante comprobante=cfdi.comprobante
		Nomina nomina = getComplemento(cfdi)
		def modelData = []
		registrarDeducciones(nomina, modelData)
		registrarPercepciones(nomina, modelData)
		registrarOtrosPagos(nomina, modelData)

		modelData.sort{
			it.clave
		}

		def repParams = ParamsUtils.getParametros(cfdi.comprobante, nomina, ne)
		params.FECHA = comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")
		params << repParams

		params['RECIBO_NOMINA']=ne.id as String
		params[PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT]="this.print();"

		def reportDef=new JasperReportDef(
			name: 'NominaDigitalCFDI'
			,fileFormat: JasperExportFormat.PDF_FORMAT
			,reportData: modelData,
			,parameters: params
			)
		Resource resource = reportDef.getReport()
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reportDef.reportData)
		JasperPrint print= JasperFillManager.fillReport(JasperCompileManager.compileReport(resource.inputStream)
			, reportDef.parameters
			, jrBeanCollectionDataSource)
		ByteArrayOutputStream  pdfStream = new ByteArrayOutputStream();
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, pdfStream); // your output goes here
		exporter.exportReport();

		return pdfStream
	}



def imprimirCfdi(Cfdi cfdi) {
		
		
		if(cfdi==null){
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cfdiInstance.label', default: 'Cfdi'), params.id])
			redirect action: "show", params:[id:id]
		}
		NominaPorEmpleado nominaPorEmpleado=NominaPorEmpleado.findByCfdi(cfdi)
		Comprobante comprobante=cfdi.comprobante
		ComplementoNomina complemento=new ComplementoNomina(comprobante)
		
		
		mx.gob.sat.nomina.NominaDocument.Nomina nomina=complemento.nomina
		
		def deducciones=nomina?.deducciones?.deduccionArray
		def modelData=deducciones.collect { cc ->
			def res=[
				'GRUPO':cc.tipoDeduccion,
				'CLAVE':cc.clave,
				'DESCRIPCION':cc.concepto,
				'IMPORTE_GRAVADO':cc.importeGravado,
				'IMPORTE_EXENTO':cc.importeExento,
				'CONCEPTO':'D'
			 ]
			return res
		}
		def percepciones=nomina.percepciones.percepcionArray
		percepciones.each{ cc->
			def res=[
				'GRUPO':cc.tipoPercepcion,
				'CLAVE':cc.clave,
				'DESCRIPCION':cc.concepto,
				'IMPORTE_GRAVADO':cc.importeGravado,
				'IMPORTE_EXENTO':cc.importeExento,
				'CONCEPTO':'P'
			 ]
			modelData<<res
		}
		
		modelData.sort{
			it.clave
		}
		 def params = [:]
		def repParams=CfdiPrintUtils.resolverParametros(comprobante,complemento.nomina,nominaPorEmpleado)
		params<<repParams
		params.FECHA=comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")
		
		params['RECIBO_NOMINA']=nominaPorEmpleado.id
		params[PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT]="this.print();"
		
		def reportDef=new JasperReportDef(
			name: 'NominaDigitalCFDI'
			,fileFormat: JasperExportFormat.PDF_FORMAT
			,reportData: modelData,
			,parameters: params
			)
		//println( "Reporte Def generado: ${nominaPorEmpleado.empleado}")
		
		Resource resource = reportDef.getReport()
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reportDef.reportData)
		JasperPrint print= JasperFillManager.fillReport(JasperCompileManager.compileReport(resource.inputStream)
			, reportDef.parameters
			, jrBeanCollectionDataSource)
		ByteArrayOutputStream  pdfStream = new ByteArrayOutputStream();
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, pdfStream); // your output goes here
		exporter.exportReport();

		return pdfStream
	}



	def generarReportDef(NominaPorEmpleado ne, def params = [:]){
		Cfdi cfdi = ne.cfdi
		Comprobante comprobante=cfdi.comprobante
		Nomina nomina = getComplemento(cfdi)
		def modelData = []
		registrarDeducciones(nomina, modelData)
		registrarPercepciones(nomina, modelData)
		registrarOtrosPagos(nomina, modelData)

		modelData.sort{
			it.clave
		}

		def repParams = ParamsUtils.getParametros(cfdi.comprobante, nomina, ne)
		//params.FECHA = '10-10-2017' //comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")
		params << repParams

		params['RECIBO_NOMINA']=ne.id as String
		params[PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT]="this.print();"

		def reportDef=new JasperReportDef(
			name: 'NominaDigitalCFDI'
			,fileFormat: JasperExportFormat.PDF_FORMAT
			,reportData: modelData,
			,parameters: params
			)
		log.info( "Reporte Def generado: ${ne.empleado}")
		return reportDef
	}


	Nomina getComplemento(Cfdi cfdi){
		def complemento=cfdi.comprobante.getComplemento()
		if(complemento){
	        String queryExpression ="declare namespace nomina12='http://www.sat.gob.mx/nomina12';" +
						"\$this/nomina12:Nomina"
	        XmlObject[] res=complemento.selectPath(queryExpression);
	        if(res.length>0){
				XmlObject n1=res[0];
				def nominaDocument = NominaDocument.Factory.parse(n1.domNode)
				return nominaDocument.getNomina()
	            
			}
		} 
		return null
	}

	def registrarDeducciones(Nomina nomina, def modelData){
		def deducciones = nomina?.deducciones?.deduccionArray
		deducciones.each { cc ->
			def res=[
				'GRUPO':cc.tipoDeduccion.toString(),
				'CLAVE':cc.clave,
				'DESCRIPCION':cc.concepto,
				'IMPORTE_GRAVADO': 0.0,
				'IMPORTE_EXENTO': cc.importe,
				'CONCEPTO':'D'
			 ]
			modelData << res
		}
	}

	def registrarPercepciones(Nomina nomina, def modelData){
		def percepciones=nomina.percepciones.percepcionArray
		percepciones.each{ cc->
			def res = [
				'GRUPO':cc.tipoPercepcion.toString(),
				'CLAVE':cc.clave,
				'DESCRIPCION':cc.concepto,
				'IMPORTE_GRAVADO':cc.importeGravado,
				'IMPORTE_EXENTO':cc.importeExento,
				'CONCEPTO':'P'
			 ]
			modelData<<res
		}
	}

	def registrarOtrosPagos(Nomina nomina, def modelData){
		if( nomina.otrosPagos) {
			def otrosPagos=nomina.otrosPagos.otroPagoArray
			otrosPagos.each{ cc->
				def res=[
					'GRUPO':cc.tipoOtroPago.toString(),
					'CLAVE':cc.clave,
					'DESCRIPCION':cc.concepto,
					'IMPORTE_GRAVADO':0.0,
					'IMPORTE_EXENTO':cc.importe,
					'CONCEPTO':'P'
				 ]
				modelData<<res
			}
		}
	}
}