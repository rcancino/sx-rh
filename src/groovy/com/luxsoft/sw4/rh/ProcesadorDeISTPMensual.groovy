package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.tablas.*
import com.luxsoft.sw4.rh.acu.*

class ProcesadorDeISTPMensual {
	
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		def ejercicio=ne.nomina.ejercicio
		def concepto = ConceptoDeNomina.findByClave('D002')
		def importeGravado = 0.0
		
		def honorarios = ne.getPercepciones()
		if(ne.empleado.contratado){

			def impuestoPorHonorarios = calcularImpuesto(honorarios, ejercicio)
		
			def salarioMensual = calcularSalarioMensual(ne)
			def impuestoMensual = calcularImpuesto(salarioMensual, ejercicio)

			//def impuestoAcumulado = impuestoMensual + impuestoPorHonorarios

			def ingresoTotal = salarioMensual + honorarios
			def impuestoTotal = calcularImpuesto(ingresoTotal, ejercicio)

			importeGravado = impuestoTotal - impuestoMensual

		} else {
			importeGravado = calcularImpuestoAsalariado(honorarios, ejercicio)
		}
		

		def nominaPorEmpleadoDet = new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
		ne.addToConceptos(nominaPorEmpleadoDet)
		nominaPorEmpleadoDet.importeExcento = 0.0
		nominaPorEmpleadoDet.importeGravado = importeGravado.setScale(2,RoundingMode.HALF_EVEN)
		ne.actualizar()
		
	}

	def calcularSalarioMensual(NominaPorEmpleado ne){
		def sm = 0.0
		if( ne.empleado.contratado){
			def sd = ne.empleado.salario.salarioDiario
			def dias = ne.nomina.getDiasPagados()
			sm = dias * sd
		} 
		return sm
	}

	def calcularImpuesto(BigDecimal percepciones, Integer ejercicio){
		if(percepciones<=0.0)
			return 0.0
		
		def tarifa = TarifaIsr.buscar(ejercicio, 'MENSUAL', percepciones)
		assert tarifa,"No encontro TarifaIsr para los parametros: Perc:${percepciones} Ejercicio: ${ejercicio}"
		
		def importeGravado = percepciones - tarifa.limiteInferior
		importeGravado *= tarifa.porcentaje
		importeGravado /= 100
		importeGravado += tarifa.cuotaFija
		importeGravado = importeGravado.setScale(2,RoundingMode.HALF_EVEN)
		return importeGravado

	}

	def calcularImpuestoAsalariado(BigDecimal percepciones, Integer ejercicio){
		if(percepciones<=0.0)
			return 0.0
		
		def tarifa = TarifaIsr.where {ejercicio == ejercicio}.list([sort:'porcentaje', order: 'desc']).get(0)
		
		def importeGravado = percepciones * tarifa.porcentaje
		importeGravado /= 100
		importeGravado = importeGravado.setScale(2,RoundingMode.HALF_EVEN)
		return importeGravado

	}
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def nominaEmpleado=det.parent
		def ejercicio = nominaEmpleado.nomina.ejercicio
	   	
		def concepto = ConceptoDeNomina.findByClave('D002')
		def model=[:]
		model.nominaEmpleado = nominaEmpleado
		model.ejercicio = ejercicio
		model.honorarios = nominaEmpleado.getPercepciones()
		model.impuestoPorHonorarios = calcularImpuesto(model.honorarios, ejercicio)
		
		model.salarioMensual = calcularSalarioMensual(nominaEmpleado)
		model.impuestoMensual = calcularImpuesto(model.salarioMensual, ejercicio)

		//def impuestoAcumulado = impuestoMensual + impuestoPorHonorarios

		model.ingresoTotal = model.salarioMensual + model.honorarios
		model.impuestoTotal = calcularImpuesto(model.ingresoTotal, ejercicio)
		model.impuestoAjustado = model.impuestoTotal - model.impuestoMensual
		model.impuestoAjustado = model.impuestoAjustado.setScale(2,RoundingMode.HALF_EVEN)
		return model

	}
	
	def getTemplate() {
		return "/nominaAsimilados/info/deduccionISTP"
	}
	
	
}
