package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat
import groovy.time.TimeCategory

@EqualsAndHashCode(includes='ejercicio,empleado')
class Finiquito {

	Empleado empleado

	BajaDeEmpleado baja

	NominaPorEmpleado nominaPorEmpleado

	Integer antiguedad=0

	BigDecimal salario = 0.0
	
	BigDecimal sueldo=0.0
	
	BigDecimal totalGravado=0.0
	
	BigDecimal totalExento=0.0
	
	BigDecimal total=0.0

	String comentario
		
	Date dateCreated
	
	Date lastUpdated


    static constraints = {
		empleado unique:true
		baja uniqut:true
		nominaPorEmpleado nullable:true
		comentario nullable:true
    }

    static mapping = {
		// fechaInicial type:'date'
		// fechaFinal type:'date'
	}

	static transients = ['antiguedad']

    String toString(){
    	return "Finiquito $empleado "
    }
    

	public Integer getAntiguedad(){
    	if(!antiguedad && empleado){
			
			def fecha=fechaFinal
			if(empleado.baja && (empleado.alta<empleado.baja.fecha)){
				if(fechaFinal>empleado.baja.fecha)
					fecha=empleado.baja.fecha
			}
			return (fecha-empleado.alta)+1
    	}
    	return antiguedad
	}
	

}


