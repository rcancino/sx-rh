package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat
import groovy.time.TimeCategory

@EqualsAndHashCode(includes='ejercicio,empleado')
class Finiquito {

	Empleado empleado

	BajaDeEmpleado baja	

	NominaPorEmpleado nominaPorEmpleado

	Date alta

	Integer antiguedad = 0

	BigDecimal salario = 0.0
	
	BigDecimal salarioDiarioIntegrado=0.0

	BigDecimal factorLiquidacion=0.0000
	
	Integer diasParaAguinaldo = 0

	Integer diasAguinaldo = 15

	BigDecimal primaVacacional=0.0

	BigDecimal salarioDiarioIntegradoLiq=0.0

	BigDecimal sueldo=0.0

	Integer diasDelEjercicio = 0

	Integer diasTrabajadoEjercicio=0

	Integer diasTrabajadoParaVacaciones=0

	Integer vacacionesAnteriores=0

	Integer vacacionesEjercicio=0

	Integer vacacionesAplicadas=0

	Integer anosTrabajados = 0

	/***********/

/*
	Integer antiguedad=0

	BigDecimal salario=0.0	

	BigDecimal sueldo=0.0
	
	BigDecimal comisiones=0.0                                                                                                                                                                                                                                                                                                        
	
	BigDecimal vacaciones=0.0
	
	BigDecimal primaVacacionalExenta=0.0
	
	BigDecimal primaVacacionalGravada=0.0
	
	BigDecimal incentivo=0.0
	
	BigDecimal aguinaldoExento=0.0
	
	BigDecimal aguinaldoGravable=0.0
	
	BigDecimal indemnizacionExenta=0.0
	
	BigDecimal indemnizacionGravada=0.0
	
	BigDecimal primaDeAntiguedadExenta=0.0
	
	BigDecimal primaDeAntiguedadGravada=0.0
	
	BigDecimal primaDominicalExenta=0.0
	
	BigDecimal primaDominicalGravada=0.0
	
	BigDecimal compensacion=0.0

	BigDecimal BonoDeProductividad=0.0
	
	BigDecimal permisoPorPaternidad=0.0

	BigDecimal compensacionSAF=0.0
	
	BigDecimal SubsEmpPagado=0.0
	
	BigDecimal SubsEmpAplicado=0.0
	
	BigDecimal ingresoTotal=0.0

	BigDecimal imss=0.0
	
	BigDecimal isr=0.0

	BigDecimal pensionAlimenticia=0.0

	BigDecimal infonavit=0.0

	BigDecimal fonacot=0.0

	BigDecimal prestamo=0.0

	BigDecimal otrasDeducciones=0.0

	BigDecimal retardos=0.0

	BigDecimal anticipoDeNomina=0.0
	
	BigDecimal totalGravado=0.0
	
	BigDecimal totalExento=0.0
	
	BigDecimal total=0.0

	BigDecimal indemnizacionIntereses=0.0

	BigDecimal indemnizacion20DiasPorAnio=0.0

	BigDecimal indemnizacion3MesesDeSueldo=0.0

	BigDecimal indemnizacionPrimaDeAntiguedad=0.0

	BigDecimal isrAcumuladoDelMes=0.0

	BigDecimal subsidioAcumuladoMes=0.0

	/************/
		
	Date dateCreated
	
	Date lastUpdated


    static constraints = {
		empleado unique:true
		baja uniqut:true
		nominaPorEmpleado nullable:true
		diasDelEjercicio nullable:true
		//comentario nullable:true
		anosTrabajados nullable: true
    }

    static mapping = {
		// fechaInicial type:'date'
		// fechaFinal type:'date'
	}

	

    String toString(){
    	return "Finiquito $empleado "
    }
    

	
	

}


