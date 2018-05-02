package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat
import groovy.time.TimeCategory


@EqualsAndHashCode(includes='ejercicio,empleado, tipo')
class Bono {

	Integer ejercicio

	Empleado empleado

	String tipo

	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial

	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal

	NominaPorEmpleado nominaPorEmpleado

	int diasDelEjercicio
	
	int faltas
	int incapacidades
	BigDecimal faltasIncapacidadesFactor = 0.0

	int retardos
	BigDecimal retardoFactor = 0.0

	Boolean productividad = false
	BigDecimal productividadFactor = 0.0

	BigDecimal salario = 0.0
	
	BigDecimal ptu = 0.0
	BigDecimal bonoInicial = 0.0
	BigDecimal bonoPreliminar = 0.0
	BigDecimal porcentajeBono = 0.0
	BigDecimal bono = 0.0

	BigDecimal ptuGravado=0.0
	BigDecimal incentivo=0.0
	
	
	BigDecimal totalGravable = 0.0

	//Datos para la retencion
	BigDecimal promedioGravable = 0.0 // (Aguinaldo gravado + bono)*30.4
	BigDecimal sueldoMensual = 0.0 // salario*31 en semana y salario*32 en quincena
	BigDecimal proporcionPromedioMensual = 0.0
	BigDecimal isrMensual = 0.0  // datos de tablas
	BigDecimal isrPromedio = 0.0 // dato de tabla
	BigDecimal difIsrMensualPromedio = 0.0 //isrPromedio-isrMensual
	BigDecimal tasa = 0.0 //si difIsrMensualPrmedio<=0?0:(difIsrMensualPromedio/promedioGravable)
	BigDecimal isrPorRetener = 0.0 //tasa*(aguinaldoGravado+bono)
	
	BigDecimal subsidio = 0.0
	BigDecimal isrOSubsidio = 0.0

	BigDecimal resultadoIsrSubsidio = 0.0
	
	BigDecimal tablaNormalIsrSub = 0.0 
	BigDecimal beneficioPerjuicio = 0.0
	
	BigDecimal isrEjerAnt = 0.0
	BigDecimal subTotal = 0.0
	BigDecimal pensionA = 0.0
	BigDecimal otrasDed = 0.0
	BigDecimal prestamo = 0.0
	BigDecimal netoPagado = 0.0

	Boolean manual = false

	//Datos informaticos
	BigDecimal montoBonoTotal = 0.0
	BigDecimal factorParaBono = 0.0
	

	Date dateCreated
	Date lastUpdated

    static constraints = {
		empleado unique:['ejercicio']
		nominaPorEmpleado nullable:true
		tasa scale:4
    }

    static mapping = {
		fechaInicial type:'date'
		fechaFinal type:'date'
	}

	static transients = ['diasDelEjercicio','antiguedad']

    String toString(){
    	return "$empleado - $ejercicio"
    }

    public Integer getDiasDelEjercicio(){
    	if(!diasDelEjercicio){
    		use(TimeCategory){
    			def duration= fechaFinal-fechaInicial+1.day
    			diasDelEjercicio=duration.days
    		}
    	}
    	return diasDelEjercicio
		
	}

	public Integer getAntiguedad(){
    	if(!antiguedad && empleado){
			/*
			if(empleado.baja && (empleado.alta < empleado.baja.fecha)){
				if(fechaFinal > empleado.baja.fecha)
					fecha = empleado.baja.fecha
			}
			*/

			return (fechaFinal - empleado.alta) + 1
    	}
    	return antiguedad
		
	}

	


}
