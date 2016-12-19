package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.*

@Transactional
class FiniquitoService {
 /*		def sd = 0.0
     	def de = 0 
     	def dte = 0


    def calcular(def ejercicio, Finiquito finiquito) {
    	Empleado e = finiquito.empleado
    	Periodo p = Periodo.getPeriodoAnual(finiquito.baja.fecha.toYear())
    	ControlDeVacaciones cv = ControlDeVacaciones.where {empleado == e && ejercicio == ejercicio}.find()
     	 sd = e.salario.salarioDiario
     	 de = p.fechaFinal - p.fechaInicial + 1    
     	 dte = finiquito.baja.fecha - p.fechaInicial + 1 	

    	finiquito.salario = sd
    	finiquito.diasDelEjercicio = de
    	finiquito.diasTrabajadoEjercicio = dte

    	finiquito.with {
    	//	salario = e.salario.salarioDiario
    		salarioDiarioIntegrado = e.salario.salarioDiarioIntegrado
    		antiguedad = finiquito.baja.fecha - finiquito.empleado.alta + 1
    		diasAguinaldo = 15
    		primaVacacional = 0.25
    	//	diasDelEjercicio = p.fechaFinal - p.fechaInicial + 1
    		

    		factorLiquidacion =( (vacacionesEjercicio * primaVacacional) + diasAguinaldo ) / diasDelEjercicio 
    		factorLiquidacion = MonedaUtils.round(factorLiquidacion,4) + 1
    		salarioDiarioIntegradoLiq = salario  * factorLiquidacion    		
    	//	diasTrabajadoEjercicio = finiquito.baja.fecha - p.fechaInicial + 1
    		diasParaAguinaldo = diasTrabajadoEjercicio + 31

    		
    		
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
    //	registrarPercepciones(finiquito,cv)
    	registrarVacaciones(finiquito,cv)

    	return finiquito
    }

    private registrarVacaciones(Finiquito finiquito, def cv){ 
    		finiquito.with{
    			vacacionesEjercicio =cv.di
    		}
    		sd = finiquito.empleado.salario.salarioDiario       	
        	finiquito.vacacionesEjercicio = cv.diasVacaciones
    		finiquito.vacacionesAplicadas = cv.diasTomados + cv.diasPagados?:0
    		finiquito.vacacionesAnteriores = cv.diasTrasladados
    		def primaVacacionalExentaAcu = 0.0
    		primaVacacionalExentaAcu = cv.acumuladoExcento
    		finiquito.diasTrabajadoParaVacaciones = (finiquito.baja.fecha - cv.aniversario ) 
    		if(finiquito.diasTrabajadoParaVacaciones < 0 )
    			finiquito.diasTrabajadoParaVacaciones = 0 
    		def vacacionesFiniquito = 0
    		vacacionesFiniquito = vacacionesEjercicio + vacacionesAnteriores - vacacionesAplicadas
    		sd = 
    		vacaciones = (salario ? 
    			(salario * vacacionesFiniquito) + ((salario * vacacionesEjercicio / diasDelEjercicio) * diasTrabajadoParaVacaciones)	)
				: (salarioVariable * vacacionesFiniquito) + ((salarioVariable * vacacionesEjercicio / diasDelEjercicio) * diasTrabajadoParaVacaciones)				
			primaVacacionalExenta = 0.0
			primaVacacionalGravada = 0.0

			return finiquito
    	}

    private registrarPercepciones(Finiquito finiquito, def cv){
    	finiquito.with{
    		sueldo = salario * diasPorPagar
			comisiones = (salario == 0.0 ? salarioVariable * diasPorPagar : 0.0)
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
    	}
    }
*/
    /*
    public Integer getDiasDelEjercicio(def baja){
    	Periodo p = Periodo.getPeriodoAnual(baja.fecha.toYear())
    	return p.fechaFinal - p.fechaFinal + 1
		
	}
	*/




}