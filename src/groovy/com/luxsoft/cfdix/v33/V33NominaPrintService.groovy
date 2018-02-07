package com.luxsoft.cfdix.v33


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

import lx.cfdi.v33.Comprobante
import lx.cfdi.v33.nomina.Nomina
import lx.cfdi.v33.nomina.NominaUtils
import lx.cfdi.v33.CfdiUtils

import com.luxsoft.cfdix.v33.NominaPrintUtils
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.cfdi.Cfdi



class V33NominaPrintService {

	def imprimir(Cfdi cfdi, def params = [:]){
		println 'Imprimiendo CFDI: ' + cfdi.folio
		NominaPorEmpleado ne = NominaPorEmpleado.findByCfdi(cfdi)
		Comprobante comprobante = CfdiUtils.read(cfdi.xml)

		def complemento = comprobante.complemento
		Nomina nomina  = complemento.any.get(0)
		
		def modelData = []
		registrarDeducciones(nomina, modelData)
		registrarPercepciones(nomina, modelData)
		registrarOtrosPagos(nomina, modelData)

		modelData.sort{it.clave}

		def repParams = NominaPrintUtils.getParametros(cfdi, nomina, ne)
		params.FECHA = comprobante.fecha
		params << repParams

		params['RECIBO_NOMINA'] = ne.id as String
		params[PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT]="this.print();"

		def reportDef=new JasperReportDef(
			name: 'NominaDigitalCFDI33'
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

		def repParams = NominaPrintUtils.getParametros(cfdi.comprobante, nomina, ne)
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

	
	def registrarDeducciones(Nomina nomina, def modelData){
		def deducciones = nomina.deducciones.deduccion
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
		def percepciones=nomina.percepciones.percepcion
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
			def otrosPagos=nomina.otrosPagos.otroPago
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