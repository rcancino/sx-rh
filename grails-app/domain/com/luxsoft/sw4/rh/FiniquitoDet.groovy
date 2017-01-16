package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='concepto')
class FiniquitoDet {
	
	String tipo

	ConceptoDeNomina concepto
	
	BigDecimal importeGravado=0.00

	BigDecimal importeExcento=0.00
	
	//CalendarioDet calendarioDet
	
	//NominaPorEmpleadoDet nominaPorEmpleadoDet
	
	//String comentario
	
	
	static belongsTo = [finiquito: Finiquito]	

    static constraints = {
		tipo inList:['PERCEPCION','DEDUCCION']
		//nominaPorEmpleadoDet nullable:true
		//comentario nullable:true,maxSize:250
    }
}

