package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.*

@Transactional
class FiniquitoService {

    def calcular(def ejercicio, Finiquito finiquito) {
    	Empleado e = finiquito.empleado
    	Periodo p = Periodo.getPeriodoAnual(finiquito.baja.fecha.toYear())
    	//Date altaAct = 
    	finiquito.with {
    		salario = e.salario.salarioDiario
    		salarioDiarioIntegrado = e.salario.salarioDiarioIntegrado
    		antiguedad = finiquito.baja.fecha - finiquito.empleado.alta + 1
    		primaVacacional = 0.25
    		diasDelEjercicio = p.fechaFinal - p.fechaInicial + 1
    		ControlDeVacaciones cv = ControlDeVacaciones.where {empleado == e && ejercicio == ejercicio}.find()
    		vacacionesEjercicio = cv.diasVacaciones
    		vacacionesAplicadas = cv.diasTomados + cv.diasPagados?:0
    		vacacionesAnteriores = cv.diasTrasladados
    		factorLiquidacion =( (vacacionesEjercicio * primaVacacional) + diasParaAguinaldo ) / diasDelEjercicio 
    		factorLiquidacion = MonedaUtils.round(factorLiquidacion,4) + 1
    		salarioDiarioIntegradoLiq = salario  * factorLiquidacion
    		sueldo = 30 * salario
    		diasTrabajadoEjercicio = finiquito.baja.fecha - p.fechaInicial + 1

    		diasTrabajadoParaVacaciones = (finiquito.baja.fecha - cv.aniversario ) 
    		if(diasTrabajadoParaVacaciones < 0 )
    			diasTrabajadoParaVacaciones = 0 

    	}
    	return finiquito
    }

    /*
    public Integer getDiasDelEjercicio(def baja){
    	Periodo p = Periodo.getPeriodoAnual(baja.fecha.toYear())
    	return p.fechaFinal - p.fechaFinal + 1
		
	}
	*/




}