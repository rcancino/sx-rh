package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.*

@Transactional
class FiniquitoService {

    def calcular(def ejercicio, Finiquito finiquito) {
    	Empleado e = finiquito.empleado
    	Periodo p = Periodo.getPeriodoAnual(finiquito.baja.fecha.toYear())

    	finiquito.with {
    		salario = e.salario.salarioDiario
    		salarioDiarioIntegrado = e.salario.salarioDiarioIntegrado
    		antiguedad = finiquito.baja.fecha - finiquito.empleado.alta + 1
    		diasAguinaldo = 15
    		primaVacacional = 0.25
    		diasDelEjercicio = p.fechaFinal - p.fechaInicial + 1
    		ControlDeVacaciones cv = ControlDeVacaciones.where {empleado == e && ejercicio == ejercicio}.find()
    		vacacionesEjercicio = cv.diasVacaciones
    		vacacionesAplicadas = cv.diasTomados + cv.diasPagados?:0
    		vacacionesAnteriores = cv.diasTrasladados
    		factorLiquidacion =( (vacacionesEjercicio * primaVacacional) + diasAguinaldo ) / diasDelEjercicio 
    		factorLiquidacion = MonedaUtils.round(factorLiquidacion,4) + 1
    		salarioDiarioIntegradoLiq = salario  * factorLiquidacion    		
    		diasTrabajadoEjercicio = finiquito.baja.fecha - p.fechaInicial + 1
    		diasParaAguinaldo = diasTrabajadoEjercicio + 31
    		diasTrabajadoParaVacaciones = (finiquito.baja.fecha - cv.aniversario ) 
    		if(diasTrabajadoParaVacaciones < 0 )
    			diasTrabajadoParaVacaciones = 0 
    		/****/
    		sueldo = salario * diasPorPagar
			comisiones = (salario == 0.0 ? salarioVariable * diasPorPagar : 0.0)
			vacaciones = (salario == 0.0 ? salarioVariable * diasPorPagar : 0.0)
			primaVacacionalExenta = 0.0
			primaVacacionalGravada = 0.0
			incentivo = 0.0
			aguinaldoExento = 0.0
			aguinaldoGravable = 0.0
			indemnizacionExenta = 0.0
			indemnizacionGravada = 0.0
			primaDeAntiguedadExenta = 0.0
			primaDeAntiguedadGravada = 0.0
			primaDominicalExenta = 0.0
			primaDominicalGravada = 0.0
			compensacion = 0.0
			bonoDeProductividad = 0.0
			permisoPorPaternidad = 0.0
			compensacionSAF = 0.0
			subsEmpPagado = 0.0
			subsEmpAplicado = 0.0
			ingresoTotal = 0.0
			imss = 0.0
			isr = 0.0
			pensionAlimenticia = 0.0
			infonavit = 0.0
			fonacot = 0.0
			prestamo = 0.0
			otrasDeducciones = 0.0
			retardos = 0.0
			anticipoDeNomina = 0.0

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