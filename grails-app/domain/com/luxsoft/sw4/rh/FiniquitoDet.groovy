package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='concepto')
class FiniquitoDet {
	
	String tipo

	ConceptoDeNomina concepto
	
	BigDecimal importeGravado=0.00

	BigDecimal importeExcento=0.00

	Boolean manual = false

	Boolean liquidacion = false
	
	
	static belongsTo = [finiquito: Finiquito]	

    static constraints = {
		tipo inList:['PERCEPCION','DEDUCCION']
    }
}

