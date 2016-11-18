package com.luxsoft.sw4.rh

class OperacionGenericaGrupo {

	
	String tipo

	ConceptoDeNomina concepto
	
	BigDecimal importeGravado=0.00

	BigDecimal importeExcento=0.00
	
	CalendarioDet calendarioDet
	
	String comentario
	
	Date dateCreated
	Date lastUpdated

	List partidas

	static hasMany = [partidas: OperacionGenerica]

    static constraints = {
		tipo inList:['PERCEPCION','DEDUCCION']
		comentario nullable:true,maxSize:250
    }

    
}
