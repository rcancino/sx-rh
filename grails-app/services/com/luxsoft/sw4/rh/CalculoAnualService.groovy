package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import net.sf.jasperreports.engine.type.CalculationEnum;

import org.apache.commons.lang.time.DateUtils

import grails.transaction.Transactional
import grails.transaction.NotTransactional

import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*

import java.math.BigDecimal;
import java.math.RoundingMode
import com.luxsoft.sw4.Empresa

@Transactional
class CalculoAnualService {

	
	@NotTransactional
	def calcular(Integer ejercicio){
		//CalculoAnual.executeUpdate("delete from CalculoAnual c where c.ejercicio=?",[ejercicio])
		def ids=NominaPorEmpleado.executeQuery(
			"select distinct(n.empleado.id) from NominaPorEmpleado n where n.nomina.ejercicio=? ",[ejercicio])
		log.info "Generando calculando anual para $ids.size empleados del ejercicio $ejercicio"
		ids.each{ 
			def empleado=Empleado.get(it)
			try {
				calcular(empleado,ejercicio)
			}
			catch(Exception e) {
				log.error "Error calculando calculo de $empleado ($ejercicio)",e
	
			}
			
		}
	}
	
	def calcular(Empleado e,Integer ejercicio){
		CalculoAnual.executeUpdate("delete from CalculoAnual c where c.empleado=? and c.ejercicio=?",[e,ejercicio])
		def calculo=CalculoAnual.findOrCreateWhere(ejercicio: ejercicio,empleado:e)
		def periodo=Periodo.getPeriodoAnual(calculo.ejercicio)
		//calculo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
		//calculo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
		calculo.fechaInicial=periodo.fechaInicial
		calculo.fechaFinal=periodo.fechaFinal
		calculo.empleado=e
		calcular(calculo)
	}

	def calcular(CalculoAnual calculo) {
		log.info 'Actualizando :'+calculo+ 'Empleado.id:'+calculo.empleado.id
		def periodo=Periodo.getPeriodoAnual(calculo.ejercicio)
		def diasDelEjercicioReales=periodo.fechaFinal-periodo.fechaInicial+1
		def empleado =calculo.empleado
		//calculo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
		//calculo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
		calculo.fechaInicial=periodo.fechaInicial
		calculo.fechaFinal=periodo.fechaFinal
		if(empleado.alta>calculo.fechaInicial){
			
			calculo.fechaInicial=empleado.alta
			calculo.fechaFinal=periodo.fechaFinal
			
		}
		calculo.salario=calculo.empleado.salario.salarioDiario
		calculo.resultado=0.0
		actualizarConceptos(calculo)
		validarAplicacion(calculo)
		calculo.save failOnError:true
	}
	
	
	private actualizarConceptos(CalculoAnual anual){
		
		def partidas=NominaPorEmpleadoDet
		.findAll("from NominaPorEmpleadoDet d where d.parent.empleado=?"+
			" and d.concepto.id in(13,41,37,44,36,22,14,40,38,18,19,15,43,23,24,31,42,20,45,34,35,33,2,12,43,49,50 )"+
			" and d.parent.nomina.ejercicio=? "
				,[anual.empleado,anual.ejercicio])
		
		Empresa emp=Empresa.first()
		
		partidas.each{it->
			
			switch(it.concepto.id){
				case 13:
					anual.sueldo+=it.importeGravado
					break
				case 41:
					anual.comisiones+=it.importeGravado
					break
				case 37:
					anual.vacaciones+=it.importeGravado
					break
				case 44:
					anual.vacacionesPagadas+=it.importeGravado
					break
				case 36:
					anual.primaVacacionalExenta+=it.importeExcento
					anual.primaVacacionalGravada+=it.importeGravado
					break
				case 22:
					anual.incentivo+=it.importeGravado
					break
				case 14:
					anual.aguinaldoExento+=it.importeExcento
					anual.aguinaldoGravable+=it.importeGravado
					break
				case 40:
					anual.indemnizacionExenta+=it.importeExcento
					anual.indemnizacionGravada+=it.importeGravado
					break
				case 38:
					anual.primaDeAntiguedadExenta+=it.importeExcento
					anual.primaDeAntiguedadGravada+=it.importeGravado
					break
				case 19:
					anual.compensacion+=it.importeGravado
					break
				case 18:
					anual.compensacion+=it.importeGravado
					break
				/**Parche para utilizar en Kyo**/	
				case (emp.rfc.equals("PAP830101CR3")?15:43):
					
				    	anual.ptuExenta+=it.importeExcento
						anual.ptuGravada+=it.importeGravado
				    
					break
				case 23:
					anual.bonoDeProductividad+=it.importeGravado
					break
				case 24:
					anual.bonoPorDesempeno+=it.importeGravado
					break
				case 31:
					anual.devISPT+=it.importeExcento					
					break
				case 42:
					anual.primaDominicalExenta+=it.importeExcento
					anual.primaDominicalGravada+=it.importeGravado
					break
				case 20:
					anual.gratificacion+=it.importeGravado
					break
				case 45:
				anual.permisoPorPaternidad+=it.importeGravado
					break
				case 34:
					anual.tiempoExtraDobleExento+=it.importeExcento
					anual.tiempoExtraDobleGravado+=it.importeGravado
					break
				case 35:
				anual.tiempoExtraTripleGravado+=it.importeGravado
					break
				case 33:
				anual.SubsEmpPagado+=it.importeExcento
					break
				case  2:
				anual.ISR+=it.importeExcento
					break
				case 12:
				anual.retardos+=it.importeExcento
					break
				case 49:
					print 'Acumulando: '+it+ ' a bono: '+anual.bono
					anual.bono += it.importeGravado
					print 'Res:______'+anual.bono
					break
				case 50:
					anual.bonoAntiguedad += it.importeGravado
					break


			}
			
		}


			def  calendarioQ=Calendario.findByEjercicioAndComentarioAndTipo(anual.ejercicio,'NOMINA','QUINCENA')
			def  calendarioS=Calendario.findByEjercicioAndComentarioAndTipo(anual.ejercicio,'NOMINA','SEMANA')

			def ultimaQ=calendarioQ.periodos.get(calendarioQ.periodos.size()-1)
			def ultimaS=calendarioS.periodos.get(calendarioS.periodos.size()-1)

			def ultimoCalendario=anual.empleado.salario.periodicidad=='QUINCENAL'?ultimaQ:ultimaS

			def compAntSAF=0

			def comASAF=NominaPorEmpleadoDet.findAll("from NominaPorEmpleadoDet det where det.parent.nomina.ejercicio=? and det.parent.nomina.calendarioDet!=? and det.parent.empleado=? and det.concepto.id=?",[anual.ejercicio,ultimoCalendario,anual.empleado,47L]).each{
				 compAntSAF+=it.importeExcento
			}

			def compen=NominaPorEmpleadoDet.find("from NominaPorEmpleadoDet det where det.parent.nomina.calendarioDet=? and det.parent.empleado=? and det.concepto.id=?",[ultimoCalendario,anual.empleado,47L])

			anual.devISPTAnt= compAntSAF?:0 
			anual.compensacionSAF= compen?.importeExcento?:0   

		def subEmpAplic=0.0
		
		def nominasEmpleado=NominaPorEmpleado.findAll("from NominaPorEmpleado n where empleado=? and n.nomina.ejercicio=? "+
			"  ",[anual.empleado,anual.ejercicio]).each {nom->
			subEmpAplic+=nom.subsidioEmpleoAplicado
		}
				
		anual.SubsEmpAplicado=subEmpAplic
		anual.totalGravado=0.0
		def netoGravado=0.0
		anual.with{
			totalGravado=sueldo+comisiones+vacaciones+vacacionesPagadas+primaVacacionalGravada+incentivo+aguinaldoGravable+indemnizacionGravada+primaDeAntiguedadGravada+compensacion+ptuGravada+bonoDeProductividad+bonoPorDesempeno+primaDominicalGravada+gratificacion+permisoPorPaternidad+tiempoExtraDobleGravado+tiempoExtraTripleGravado+bono+bonoAntiguedad
			totalExento=primaVacacionalExenta+aguinaldoExento+indemnizacionExenta+primaDeAntiguedadExenta+ptuExenta+primaDominicalExenta+tiempoExtraDobleExento  
			total=totalGravado+totalExento
			netoGravado=totalGravado-retardos
			ingresoTotal=total-devISPTAnt-retardos

		}
		def diasProyectados=calcularDiasProyectados(anual)
		//def dias=anual.empleado.salario.periodicidad=='QUINCENAL'?16:0
		anual.proyectado=anual.salario*diasProyectados

		anual.impuestoDelEjercicio=calcularImpuesto(netoGravado+anual.proyectado)
		
		def ResISPT177=anual.impuestoDelEjercicio-anual.SubsEmpAplicado
		if(anual.impuestoDelEjercicio<anual.subsEmpAplicado){
			ResISPT177=0.0
		}		
		
		anual.resultado=anual.ISR-anual.devISPT-ResISPT177
	}
	
	/**
	 * @Todo Falta implementar la logica para determinar los dias de proyectado
	 * @param anual
	 * @return
	 */
	private calcularDiasProyectados(CalculoAnual anual){
		return anual.empleado.salario.periodicidad=='QUINCENAL'?0:0
	}
	
	public void validarAplicacion(CalculoAnual anual){
		if(anual.ingresoTotal>=400000.00){
			anual.calculoAnual=false
			return
		}
		if(anual.empleado.perfil.declaracionAnual==true){
			anual.calculoAnual=false
			return
		}
		def periodo=Periodo.getPeriodoAnual(anual.ejercicio)
		def diciembre=Periodo.getPeriodoEnUnMes(11,anual.ejercicio)
		if(anual.empleado.alta>periodo.fechaInicial ){
			anual.calculoAnual=false
			return
		
		}
		//if(anual.empleado.baja && anual.empleado.baja<diciembre.fechaInicial){
		if(anual.empleado.baja && anual.empleado.baja.fecha<=diciembre.fechaFinal){
			anual.calculoAnual=false
			return
		}
	}
	
	private BigDecimal calcularImpuesto(BigDecimal percepciones){
		def tarifa =TarifaAnualIsr.buscar(percepciones)
		println 'Tarifa seleccionada: '+tarifa+ ' Para un ingreso de: '+percepciones
		def importeGravado=percepciones-(tarifa.limiteInferior)
		importeGravado*=tarifa.porcentaje
		importeGravado/=100
		importeGravado+=(tarifa.cuotaFija)
		importeGravado=importeGravado.setScale(2,RoundingMode.HALF_EVEN)
		return importeGravado
	}
	
	public aplicarCalculoAnualConSaldo(NominaPorEmpleado ne,CalculoAnual calculo){
		assert calculo.saldo, "No se puede aplicar calculo anual sin saldo"
		def impuestoDet=ne.conceptos.find{it.concepto.clave=='D002'}
		if(impuestoDet && impuestoDet.total>0){
			def importeExcento=0.0
			def concepto=ConceptoDeNomina.findByClave('P033')
			if(calculo.saldo>impuestoDet.importeExcento){
				importeExcento=impuestoDet.importeExcento
			}else{
				importeExcento=calculo.saldo
			}
			if(importeExcento>0.0){
				def ca=new NominaPorEmpleadoDet(
					concepto:concepto
					,importeGravado:0.0
					,importeExcento:importeExcento
					,comentario:'CALCULO ANUAL')
				ne.addToConceptos(ca)
			}
		}
		
	}


	public aplicar(NominaPorEmpleado ne) {
		
		def ajuste=IsptMensual.findByNominaPorEmpleado(ne)
		if(!ajuste) 
			return
			
			
		def found=ne.conceptos.find{it.concepto.clave=='P033'}
		if(found){
			log.info "Calculo anual ya aplicado"
			return
		}
			
		def calculo=CalculoAnual.findByEmpleadoAndEjercicio(ne.empleado,ne.nomina.ejercicio)
		if(calculo && calculo.calculoAnual){
			log.info 'Aplicando calculo anual: '+calculo+ " Resultado: "+calculo.resultado
			def resultado=calculo.resultado
			def impuestoDet=ne.conceptos.find(){ 
				it.concepto.clave=='D002'
			}


			if(impuestoDet){
				def importeExcento=0.0
				def concepto=ConceptoDeNomina.findByClave('P033')
				if(resultado>impuestoDet.importeExcento){
					importeExcento=impuestoDet.importeExcento
				}else{
					importeExcento=resultado
				}
				if(importeExcento>0.0){
					def ca=new NominaPorEmpleadoDet(
						concepto:concepto
						,importeGravado:0.0
						,importeExcento:importeExcento
						,comentario:'CALCULO ANUAL')
					ne.addToConceptos(ca)
				}
				
			}
		}
		ne.actualizar()

		//ne.save flush:true,failOnError:true
	}


	def actualizarSaldos(NominaPorEmpleado ne){

		def empleado = ne.empleado

		def calculo= CalculoAnual.find("from CalculoAnual c where c.empleado=? and date(c.fechaFinal)=?",[empleado,ne.nomina.periodo.fechaFinal])

		def inicial = 0.0
		def acumulado = 0.0

		if(calculo){
		  	def percepcion = ne.conceptos.find{it.concepto.clave=='P033'}
		  	//log.info 'Found: '+percepcion
		  	if(percepcion)
		  		inicial=percepcion.total
		  	calculo.aplicado=inicial
		  	calculo.save failOnError:true,flush:true
		  
		}else{
		  def ejercicio = ne.nomina.ejercicio-1
		  calculo= CalculoAnual.find("from CalculoAnual c where c.empleado=? and c.ejercicio=?",[empleado,ejercicio])
		  if(calculo){
		  	def fechaInicial = calculo.fechaFinal
		  def fechaFinal = Periodo.getPeriodoAnual(ejercicio+1).fechaFinal-1
		  def per = new Periodo(fechaInicial,fechaFinal)
		  
		  acumulado=NominaPorEmpleadoDet.executeQuery(
						"select sum(d.importeExcento) from NominaPorEmpleadoDet d "+
						" where  d.parent.empleado=? "+
						" and d.concepto.clave=? "+
		  				" and date(d.parent.nomina.periodo.fechaFinal) between ? and ? ",
						[empleado,"P033",per.fechaInicial,per.fechaFinal]).get(0)?:0.0
		  calculo.aplicado = acumulado
		  calculo.save failOnError:true,flush:true
		  log.info "Inicial: ${inicial} Acumulado:${acumulado}"
		  }
		  
		}
		
		
	}


	def buscarAplicado(Empleado empleado,def ejercicio){

		def aplicacionInicial=NominaPorEmpleadoDet.find(
					"from NominaPorEmpleadoDet d "+
					" where  d.parent.empleado=? "+
					" and d.concepto.clave=? "+
					" and d.parent.nomina.ejercicio=? order by d.parent.nomina.folio desc",
					[empleado,"P033",ejercicio])

		def aplicado=NominaPorEmpleadoDet.executeQuery(
					"select sum(d.importeExcento) from NominaPorEmpleadoDet d "+
					" where  d.parent.empleado=? "+
					" and d.concepto.clave=? "+
					" and d.parent.nomina.ejercicio=?",
					[empleado,"P033",ejercicio+1])
		

		def res=aplicado.get(0)?:0.0
		def ainicial=aplicacionInicial?.importeExcento?:0.0
		return res+ainicial
	}

	def buscarDeducciones(Empleado empleado,def ejercicio){
		def deducciones=NominaPorEmpleadoDet.executeQuery(
					"select sum(d.importeExcento) from NominaPorEmpleadoDet d "+
					" where  d.parent.empleado=? "+
					" and d.concepto.clave=? "+
					" and d.parent.nomina.ejercicio=?",
					[empleado,"D015",ejercicio+1])

		return deducciones.get(0)?:0.0
	}
	
	
    
}
