package com.luxsoft.sw4.rh

import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import grails.events.Listener

@Transactional
class NominaPorEmpleadoService {
	
	def static CONCEPTOS_BASICOS=['P001','D001','D002']
	
	def procesadorDeNomina

	def procesadorDeNominaFiniquito

	def procesadorDeNominaLiquidacion

	def procesadorDeNominaEspecial
	
	//def prestamoService
	
	@Transactional
	def eliminar(Long id){
		NominaPorEmpleado ne=NominaPorEmpleado.get(id)
		log.info 'Eliminar nomina por empleado: '+ne		
		//prestamoService.beforeDelete(ne)
		def nomina=ne.nomina
		if(nomina.tipo=='AGUINALDO'){
			def aguinaldo=Aguinaldo.findByNominaPorEmpleado(ne)
			aguinaldo.nominaPorEmpleado=null
			//aguinaldo.save flush:true
		}
		if( nomina.tipo == 'LIQUIDACION') {
			def finiquito = Finiquito.where {neFiniquito == ne || neLiquidacion == ne }.find()
			if(finiquito) {
				finiquito.neFiniquito = null
				finiquito.neLiquidacion = null
			}
		}
		nomina.removeFromPartidas(ne)
		nomina.save flush:true		
		return nomina
	}
	
	@Transactional
	def eliminarConcepto(Long id){
		
		NominaPorEmpleadoDet det=NominaPorEmpleadoDet.get(id)
		log.debug 'Eliminando concepto: '+det
		//prestamoService.beforeDelete(det)
		//prestamoService
		def ne=det.parent
		def target=ne.conceptos.find(){it.id==id}
		log.debug 'Found: '+target
		ne.removeFromConceptos(target)
		ne.save()
		return ne
	}
	
	
	@Transactional
	def actualizarNominaPorEmpleado(NominaPorEmpleado ne) {
		
		return procesadorDeNomina.procesar(ne)
	}
	
	@Transactional
	def actualizarNominaPorEmpleado(Long id) {

		NominaPorEmpleado ne=NominaPorEmpleado.get(id)

		
		log.info 'Actualizando nomina '+ne.empleado
		ne.conceptos.clear()
		ne.save flush:true
		
		NominaPorEmpleado res= procesadorDeNomina.procesar(ne)
		depurarNominaPorEmpleado(res)
		return res

	}
	@Transactional
	def actualizarFirmaRecibo(Long id,firma){
	
		NominaPorEmpleado ne=NominaPorEmpleado.get(id)

		if(firma){
			
			ne.reciboFirmado=firma
			
		}else{
			ne.reciboFirmado=false
		}
		ne.save flush:true
		return ne
	}
	
	@Transactional
	def depurarNominaPorEmpleado(Long id){
		NominaPorEmpleado ne=NominaPorEmpleado.get(id)
		return depurarNominaPorEmpleado(ne)
	}
	
	@Transactional
	def depurarNominaPorEmpleado(NominaPorEmpleado ne){
		
		def togo=[]
		ne.conceptos.each{
			if(it.getTotal()<=0.0)
				togo.add(it)
		}
		log.debug "Depurando ${togo.size()} conceptos de la nomina por empleado $ne.id"
		
		togo.each{det->
			ne.removeFromConceptos(det)
		}
		return ne
	}

	@Transactional
	def actualizarFiniquito(NominaPorEmpleado ne) {
		ne.conceptos.clear()

		def INVALIDAS = ['P028','P026']
		def finiquito = Finiquito.where {neFiniquito == ne}.find()
		assert finiquito, "No se ha asignado el  finiquito para ${ne.empleado}"
		
		ne.diasTrabajados = finiquito.diasPorPagar
		ne.diasDelPeriodo=ne.nomina.getDiasPagados()

		def percepciones = finiquito.partidas.findAll {
			it.concepto.tipo == 'PERCEPCION' &&  !INVALIDAS.contains(it.concepto.clave)
		}.sort {it.manual}

		percepciones.each { det ->
			log.info( "Agregando:  ${det.tipo} ${det.concepto}" )
				def d2 = new NominaPorEmpleadoDet(concepto:det.concepto
						,importeGravado:det.importeGravado
						,importeExcento:det.importeExcento
						,comentario:'PENDIENTE')
				if(d2.total>0)
					ne.addToConceptos(d2)
		}

		procesadorDeNominaFiniquito.reglas.each {
			it.procesar(ne)
		}

		finiquito.partidas.findAll {it.concepto.tipo == 'DEDUCCION' && !it.liquidacion}.each{ det->
			def d2 = new NominaPorEmpleadoDet(concepto:det.concepto
					,importeGravado:det.importeGravado
					,importeExcento:det.importeExcento
					,comentario:'PENDIENTE')
			if(d2.total>0)
				ne.addToConceptos(d2)
		}

		
		ne.save failOnError:true, flush:true
		return ne
	}

	@Transactional
	def asignarFiniquito(NominaPorEmpleado ne) {
		def finiquito = Finiquito.where {
			empleado == ne.empleado && neFiniquito == null
		}.find()

		println "----------  "+finiquito.empleado+"-----"+finiquito.id

		assert finiquito, "No se ha registrado el finiquito para ${ne.empleado}"
		
		
		ne.finiquito = true
		finiquito.neFiniquito = ne
		
		finiquito.save flush: true
		ne.save flush: true
		return ne
	}

	@Transactional
	def actualizarLiquidacion(NominaPorEmpleado ne){
		ne.conceptos.clear()
		def finiquito = Finiquito.where {neLiquidacion == ne}.find()
		assert finiquito, "No se ha asignado el  finiquito para ${ne.empleado}"
		
		def percepciones = finiquito.partidas.findAll {
			it.concepto.tipo == 'PERCEPCION' &&  it.liquidacion
		}

		def deducciones = finiquito.partidas.findAll {
			it.concepto.tipo == 'DEDUCCION' && it.liquidacion
		}
		if (deducciones)
			percepciones << deducciones

		ne.diasTrabajados = finiquito.diasPorPagar
		ne.diasDelPeriodo = finiquito.diasPorPagar
		ne.salarioDiarioBase = finiquito.salario
		ne.salarioDiarioIntegrado = finiquito.salarioDiarioIntegrado
		ne.antiguedadEnSemanas = finiquito.antiguedad

		percepciones.each { det ->
			log.info( "Agregando:  ${det.tipo} ${det.concepto}" )
				def d2 = new NominaPorEmpleadoDet(concepto:det.concepto
						,importeGravado:det.importeGravado
						,importeExcento:det.importeExcento
						,comentario:'PENDIENTE')
				if(d2.total>0)
					ne.addToConceptos(d2)
		}
		procesadorDeNominaLiquidacion.reglas.each {
			it.procesar(ne)
		}
		ne.save failOnError:true, flush:true
		return ne
	}

	@Transactional
	def actualizarNominaEspecial(NominaPorEmpleado ne){
		
		ne.conceptos.clear()

		def genericas = OperacionGenerica.where {empleado == ne.empleado && calendarioDet == ne.nomina.calendarioDet}.list()
		
		def percepciones = genericas.findAll {
			it.concepto.tipo == 'PERCEPCION' 
		}

		def deducciones = genericas.findAll {
			it.concepto.tipo == 'DEDUCCION' 
		}

		if (deducciones)
			percepciones << deducciones

		ne.diasTrabajados = 1
		ne.diasDelPeriodo = 1
		ne.salarioDiarioBase = ne.empleado.salario.salarioDiario
		ne.salarioDiarioIntegrado = ne.empleado.salario.salarioDiarioIntegrado
		
		percepciones.each { det ->
			//log.info( "Agregando:  ${det.tipo} ${det.concepto}" )
				def d2 = new NominaPorEmpleadoDet(concepto:det.concepto
						,importeGravado:det.importeGravado
						,importeExcento:det.importeExcento
						,comentario:'PENDIENTE')
				if(d2.total>0)
					ne.addToConceptos(d2)
		}
		procesadorDeNominaEspecial.reglas.each {
			def pg=ne.getPercepcionesGravadas()
			//log.info("Aplicando procesadores a nomina especial de ${ne.empleado}  ${it} percepciones: ${pg}")
			it.procesar(ne)
			ne.diasTrabajados = 0 // Ajustes 
			ne.diasDelPeriodo = 0
		}
		ne.save failOnError:true, flush:true
		return ne
	}
	
	/*
	@Listener(namespace='gorm')
	def afterUpdate(NominaPorEmpleado ne){
		def togo=[]
		ne.conceptos.each{
			if(it.total<=0.0){
				togo.add(it)
			}
		}
		println 'Eliminar: '+togo.size()
		togo.each{
			//println 'Eliminando: '+it+' Idx:'+ne.conceptos.indexOf(it)
			
		}
	}
	*/
	
	
		
}
