package com.luxsoft.sw4.cfdi.nomina12


import org.apache.commons.lang.StringUtils

import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Emisor
import mx.gob.sat.cfd.x3.TUbicacion

import mx.gob.sat.nomina12.NominaDocument
import mx.gob.sat.nomina12.NominaDocument.Nomina

import com.luxsoft.sw4.cfdi.QRCodeUtils
import com.luxsoft.sw4.cfdi.TimbreFiscal
import com.luxsoft.sw4.cfdi.ImporteALetra
import com.luxsoft.sw4.rh.NominaPorEmpleado



class ParamsUtils {

	static getParametros(Comprobante comprobante,Nomina nomina,NominaPorEmpleado nominaPorEmpleado){
		
		def parametros=[:]
		Emisor emisor=comprobante.getEmisor();
		parametros.put("EMISOR_NOMBRE", 	emisor.getNombre())
		parametros.put("EMISOR_RFC", 		emisor.getRfc())
		parametros.put("EMISOR_DIRECCION", comprobante.lugarExpedicion)
		parametros.put("EXPEDIDO_DIRECCION", comprobante.lugarExpedicion)
		parametros.put("REGIMEN",comprobante.getEmisor().getRegimenFiscalArray(0).regimen)
		parametros.put("METODO_DE_PAGO", 		comprobante.getMetodoDePago())
		
		// Datos tomados del Comprobante fiscal digital XML
		parametros.put("NOMBRE", 			comprobante.getReceptor().getNombre()) //Recibir como Parametro
		parametros.put("RFC", 				comprobante.getReceptor().getRfc())
		parametros['NUMERO_EMPLEADO'] = nomina.receptor.numEmpleado
		parametros['NUMERO_IMSS'] = nomina.receptor.numSeguridadSocial
		parametros['CURP'] = nomina.receptor.curp
		parametros['REGIMEN_TRABAJADOR'] = nomina.receptor.tipoRegimen.toString()
		parametros['EMISOR_RFC']=comprobante.emisor.rfc
		parametros['PERIOCIDAD_PAGO'] = nomina.receptor.periodicidadPago.toString()
		parametros.put("SERIE", 			comprobante.serie)
		parametros.put("FOLIO", 			comprobante.folio)
		parametros.put("NUM_CERTIFICADO", 	comprobante.getNoCertificado())
		parametros.put("SELLO_DIGITAL", 	comprobante.getSello())
		parametros.put("IMP_CON_LETRA", 	ImporteALetra.aLetra(comprobante.getTotal()))
		if(nominaPorEmpleado.nomina.tipo!='ASIMILADOS'){
			parametros['REGISTRO_PATRONAL'] = nomina.emisor.registroPatronal
			parametros['RIESGO_PUESTO'] = ''+nomina.receptor.riesgoPuesto
			parametros['TIPO_JORNADA'] = nomina.receptor.tipoJornada.toString()
			parametros['ANTIGUEDAD'] = '' + nomina.receptor.antigüedad	
			parametros['ASIMILADOS']='NO'
			parametros['BANCO'] = nominaPorEmpleado.nomina.formaDePago == 'TRANSFERENCIA'? nomina.receptor.banco?.toString() : ''
			parametros['CLABE']=nomina.receptor.cuentaBancaria?.toString()
		}else{
			parametros['REGISTRO_PATRONAL'] = ''
			parametros['RIESGO_PUESTO'] = ''
			parametros['TIPO_JORNADA'] = ''
			parametros['ANTIGUEDAD'] = ''
			parametros['ASIMILADOS']='SI'
			parametros['BANCO'] = ''
			parametros['CLABE']=''
		}
		
		parametros.put("NFISCAL", 			comprobante.getSerie()+": "+nominaPorEmpleado.nomina.folio+" - "+comprobante.getFolio())
		
		
		
		parametros['FECHA_INGRESO_LABORAL'] = nomina.receptor.fechaInicioRelLaboral?.format("yyyy-MM-dd")

		parametros['TIPO_CONTRATO'] = nomina.receptor.tipoContrato.toString()
		
		parametros['FECHA_INICIAL']=nomina.fechaInicialPago?.format("yyyy-MM-dd")
		parametros['FECHA_FINAL']=nomina.fechaFinalPago?.format("yyyy-MM-dd")
		
		
		
		parametros['TOTAL']=comprobante.getTotal() //as String
		parametros['COMENTARIO_NOM']='Nómina'
		def img=QRCodeUtils.generarQR(comprobante)
		parametros.put("QR_CODE",img)
		TimbreFiscal timbre=new TimbreFiscal(comprobante)
		parametros.put("FECHA_TIMBRADO", timbre.FechaTimbrado)
		parametros.put("FOLIO_FISCAL", timbre.UUID)
		parametros.put("SELLO_DIGITAL_SAT", timbre.selloSAT)
		parametros.put("CERTIFICADO_SAT", timbre.noCertificadoSAT)
		parametros.put("CADENA_ORIGINAL_SAT", timbre.cadenaOriginal())
		
		parametros['SALARIO_DIARIO_BASE']=nomina.receptor.salarioBaseCotApor as String
		parametros['SALARIO_DIARIO_INTEGRADO']=nomina.receptor.salarioDiarioIntegrado as String
		
		parametros['TIPO_NOMINA']=nomina.tipoNomina.toString()
		parametros['SUCURSAL']=nominaPorEmpleado.empleado.perfil.ubicacion.clave
		parametros['PUESTO']=nomina.receptor.puesto
		parametros['DEPARTAMENTO']=nomina.receptor.departamento
		parametros['FECHA']= comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")

		if(nominaPorEmpleado.asistencia){
			def diasTrabajados=0
			def faltas=0
			if(nominaPorEmpleado){
				if(nominaPorEmpleado.asistencia){
					if(!nominaPorEmpleado.empleado.controlDeAsistencia){
				   		diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones+nominaPorEmpleado.asistencia.paternidad
				   		faltas=	(nominaPorEmpleado.asistencia.faltasManuales+(nominaPorEmpleado.asistencia.faltasManuales*0.167)+nominaPorEmpleado.incapacidades)
					}else{
				  		if(nominaPorEmpleado.empleado.alta<=nominaPorEmpleado.asistencia.calendarioDet.inicio){
					   		diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones+nominaPorEmpleado.asistencia.paternidad
				  			faltas=nominaPorEmpleado.diasDelPeriodo-nominaPorEmpleado.diasTrabajados-nominaPorEmpleado.vacaciones-nominaPorEmpleado.asistencia.paternidad
				  		}else{
					  		diasTrabajados=nominaPorEmpleado.diasTrabajados //-(nominaPorEmpleado.asistencia.faltasManuales+nominaPorEmpleado.incapacidades)
					  		faltas=nominaPorEmpleado.faltas+nominaPorEmpleado.fraccionDescanso+nominaPorEmpleado.incapacidades  //(
				  		}
					}
				}			
			}
			parametros['FALTAS']=com.luxsoft.sw4.MonedaUtils.round(faltas,2) as String
			parametros['DIAS_TRABAJADOS']=com.luxsoft.sw4.MonedaUtils.round(diasTrabajados,2) as String
		}	
		parametros['SUB_EMPLEO_APLIC']=nominaPorEmpleado.subsidioEmpleoAplicado

		def horasExtras = nominaPorEmpleado.conceptos.find {it.concepto.clave == 'P022'|| it.concepto.clave=='P023'}
		if(horasExtras){
			def diasDobles = 0;
			def diasTriples = 0;
			def horasDobles = 0;
			def horasTriples = 0;
			def importeDobles = 0
			def importeTriples = 0
			nomina.getPercepciones().getPercepcionArray().each {
				it.getHorasExtraArray().each { pp->
					if(pp.tipoHoras.toString() == '01') {
						diasDobles=pp.getDias()
						horasDobles= pp.getHorasExtra()	
						importeDobles= pp.importePagado
					}
					if( pp.tipoHoras.toString() =='02'){
						diasTriples=pp.getDias()	
						horasTriples= pp.getHorasExtra()	
						importeTriples = pp.importePagado
					}


				}
			}
		
			parametros.put("DIAS_HORAS_EXTRA_DOBLE", diasDobles )
			parametros['TIPO_HORAS_DOBLE'] = '01'
			parametros['HORAS_EXTRA_DOBLE'] = horasDobles
			parametros['IMPORTE_HORAS_EXTRA_DOBLE'] = importeDobles

			if(importeTriples > 0 ) {	
				parametros['DIAS_HORAS_EXTRA_TRIPLE'] = diasDobles 
				parametros['TIPO_HORAS_TRIPLE'] ='02'
				parametros['HORAS_EXTRA_TRIPLE']= horasTriples
				parametros['IMPORTE_HORAS_EXTRA_TRIPLE'] = importeTriples
			}

			
			
			
		}
			
		
		
		return parametros
		}
	}
