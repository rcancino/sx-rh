package com.luxsoft.sw4.rh

import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.export.PdfExporterConfiguration;

import org.apache.commons.io.FileUtils;
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat;
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.springframework.core.io.Resource









import com.luxsoft.sw4.Mes;
import com.luxsoft.sw4.cfdi.Cfdi;
import com.luxsoft.sw4.cfdi.CfdiPrintUtils;
import com.luxsoft.sw4.cfdi.ComplementoNomina;
import com.luxsoft.sw4.Empresa

import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class ReciboDeNominaController {
	
	def jasperService

	def nominaPrintService

    def index(long nominaId) {
		
		params.periodicidad=params.periodicidad?:'QUINCENAL'
		
		def query=Nomina.where{periodicidad==params.periodicidad && ejercicio==session.ejercicio}
		[nominaInstanceList:query.list(params)
			,nominaInstanceListTotal:query.count(params)
			,periodicidad:params.periodicidad
		]
		
	}
	
	def semanal(long nominaId) {
		//println 'Lista de recibos para : '+params
		def nominasPorMes=[:]
		Mes.getMeses().each{
			nominasPorMes[it.nombre]=Nomina.findAll(
			"from Nomina n where n.periodicidad='SEMANAL' and month(n.periodo.fechaInicial)=? and n.ejercicio=? order by n.folio"
			,[it.clave+1,session.ejercicio])
		}
		
		def nominasList=[]
		
		[
			nominasPorMesInstanceMap:nominasPorMes  //Para el scrollpane
			,nominaInstance:Nomina.get(nominaId) //La nomina seleccionada
			,mesInstance:params.mesInstance //Para seleccionar el scrollpan activo
			,periodicidad:params.periodicidad
		]
		
	}
	
	def impresionDirecta(Cfdi cfdi) {
		
		ByteArrayOutputStream  pdfStream = nominaPrintService.imprimir(cfdi, params)
		def fileName="cfdi_${cfdi.folio}_${cfdi.receptor}.pdf"
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:fileName)
	}

	def imprimirCfdi() {
		//println 'Imprimiendo CFDI: '+params.id
		def cfdi=Cfdi.findById(params.id)
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
		
		def repParams = CfdiPrintUtils.resolverParametros(comprobante,complemento.nomina,nominaPorEmpleado)
		params << repParams
		params.FECHA = comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")
		params['RECIBO_NOMINA']=nominaPorEmpleado.id
		params[PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT]="this.print();"
		chain(controller:'jasper',action:'index',model:[data:modelData],params:params)
		
	}
	
	def imprimirCfdis(Nomina n){
		def reportes = []
		n.partidas.sort{it.orden}.each{ nominaPorEmpleado->

			if(nominaPorEmpleado.cfdi){
				reportes << nominaPrintService.generarReportDef(nominaPorEmpleado)
			}
		}

		ByteArrayOutputStream  pdfStream = jasperService.generateReport(reportes)
		def fileName="nomina_${n.ejercicio}_${n.periodicidad}_${n.folio}.pdf"
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:fileName)
	}
	
	def imprimirRecibo(NominaPorEmpleado ne){
		log.info 'Imprimiendo recibo de nomina para: '+ne
		def empleado=ne.empleado
		def n=ne.nomina
		def repParams=[:]
		if(ne.nomina.tipo == 'ASIMILADOS'){
			repParams["NOMBRE"]=empleado.nombre
			repParams["RFC"]=empleado.rfc			
			repParams['CURP']=empleado.curp		
			repParams['NFISCAL']=ne.id as String		
			repParams['EMISOR_NOMBRE']=Empresa.first().nombre
			repParams['IMP_CON_LETRA']=com.luxsoft.sw4.cfdi.ImporteALetra.aLetra(ne.getTotal())
			repParams['TOTAL']=ne.total //as String
		} else{
			repParams["NOMBRE"]=empleado.nombre
			repParams["RFC"]=empleado.rfc
			repParams['NUMERO_EMPLEADO']=empleado.perfil.numeroDeTrabajador
			repParams['NUMERO_IMSS']=empleado.seguridadSocial.numero
			repParams['CURP']=empleado.curp
			repParams['SUCURSAL']=empleado.perfil.ubicacion.clave
			repParams['PUESTO']=empleado.perfil.puesto.clave
			repParams['DEPARTAMENTO']=empleado.perfil.departamento.clave
			repParams['SALARIO_DIARIO_BASE']=ne.salarioDiarioBase as String
			repParams['SALARIO_DIARIO_INTEGRADO']=ne.salarioDiarioIntegrado as String
			repParams['SUB_EMPLEO_APLIC']=ne.subsidioEmpleoAplicado //as String
			repParams['FECHA_INGRESO_LABORAL']=empleado.alta.format("yyyy-MM-dd")
			repParams['NFISCAL']=ne.id as String
			repParams['FECHA_INICIAL']=n.periodo.fechaInicial?.format("yyyy-MM-dd")
			repParams['FECHA_FINAL']=n.periodo.fechaFinal?.format("yyyy-MM-dd")
			repParams['EMISOR_NOMBRE']=Empresa.first().nombre
			repParams['PERIOCIDAD_PAGO']=n.periodicidad
			repParams['IMP_CON_LETRA']=com.luxsoft.sw4.cfdi.ImporteALetra.aLetra(ne.getTotal())
			repParams['TOTAL']=ne.total //as String

		}
		
		
		def diasTrabajados=0
		def faltas=0
		def nominaPorEmpleado=ne
		if(nominaPorEmpleado){
			if(nominaPorEmpleado.asistencia){
			  	if(!nominaPorEmpleado.empleado.controlDeAsistencia){
			  			diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones+nominaPorEmpleado.asistencia.paternidad
			  			//diasTrabajados= nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones-(nominaPorEmpleado.asistencia.faltasManuales+(nominaPorEmpleado.asistencia.faltasManuales*0.167)+ nominaPorEmpleado.incapacidades)
			   			faltas=	(nominaPorEmpleado.asistencia.faltasManuales+(nominaPorEmpleado.asistencia.faltasManuales*0.167)+nominaPorEmpleado.incapacidades)
				}else{
			  		if(nominaPorEmpleado.empleado.alta<=nominaPorEmpleado.asistencia.calendarioDet.inicio){
				  		//diasTrabajados=nominaPorEmpleado.diasDelPeriodo-(nominaPorEmpleado.faltas+ nominaPorEmpleado.fraccionDescanso + nominaPorEmpleado.incapacidades)
				  		//faltas=(nominaPorEmpleado.faltas+ nominaPorEmpleado.fraccionDescanso + nominaPorEmpleado.incapacidades)
				  		diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones+nominaPorEmpleado.asistencia.paternidad
				  		faltas=nominaPorEmpleado.diasDelPeriodo-nominaPorEmpleado.diasTrabajados-nominaPorEmpleado.vacaciones-nominaPorEmpleado.asistencia.paternidad
					}else{
					  diasTrabajados=nominaPorEmpleado.diasTrabajados //-(nominaPorEmpleado.asistencia.faltasManuales+nominaPorEmpleado.incapacidades)
				 	  faltas= nominaPorEmpleado.faltas+nominaPorEmpleado.fraccionDescanso+nominaPorEmpleado.incapacidades //(nominaPorEmpleado.asistencia.faltasManuales+nominaPorEmpleado.incapacidades)
			   	    }
			    }
			}		
		}
		repParams['FALTAS']=com.luxsoft.sw4.MonedaUtils.round(faltas,2) as String
		repParams['DIAS_TRABAJADOS']=com.luxsoft.sw4.MonedaUtils.round(diasTrabajados,2) as String
		
		def deducciones=ne.conceptos.findAll{it.concepto.tipo=='DEDUCCION'}
		def percepciones=ne.conceptos.findAll{it.concepto.tipo=='PERCEPCION'}
		
		def modelData=deducciones.collect { cc ->
			def res=[ 
				'GRUPO':cc.concepto.tipo,
				'CLAVE':cc.concepto.clave,
				'DESCRIPCION':cc.concepto.descripcion,
				'IMPORTE_GRAVADO':cc.importeGravado,
				'IMPORTE_EXENTO':cc.importeExcento,
				'CONCEPTO':'D'
			 ]
			return res
		}
		
		percepciones.each{ cc->
			def res=[
				'GRUPO':cc.concepto.tipo,
				'CLAVE':cc.concepto.clave,
				'DESCRIPCION':cc.concepto.descripcion,
				'IMPORTE_GRAVADO':cc.importeGravado,
				'IMPORTE_EXENTO':cc.importeExcento,
				'CONCEPTO':'P'
			 ]
			modelData<<res
		}
		modelData.sort{
			it.clave
		}
		
		if (!percepciones){
			def res=[
				'GRUPO':"",
				'CLAVE':"",
				'DESCRIPCION':"",
				'IMPORTE_GRAVADO':0.0,
				'IMPORTE_EXENTO':0.0,
				'CONCEPTO':'P'
			 ]
			modelData<<res
		}
		
		def reportDef=new JasperReportDef(
			name:'Recibo'
			,fileFormat:JasperExportFormat.PDF_FORMAT
			,reportData:modelData,
			,parameters:repParams
			)
		ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
		def fileName="nomina_${n.ejercicio}_${n.periodicidad}_${n.folio}_${ne.empleado.clave}.pdf"
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:fileName)
	}
	
	def showXml(Long id){
		//println 'Mostrando XML: '+id
		def cfdi=Cfdi.findById(id)
		if(cfdi==null){
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cfdiInstance.label', default: 'Cfdi'), params.id])
			redirect action: "index", method: "GET"
		}
		render view:'cfdiXml',model:[cfdi:cfdi,xml:cfdi.getComprobanteDocument().xmlText()]
	}
}

