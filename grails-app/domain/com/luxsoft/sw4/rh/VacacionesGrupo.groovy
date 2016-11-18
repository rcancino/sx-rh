package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat

class VacacionesGrupo {
	
	@BindingFormat("dd/MM/yyyy")
	Date solicitud=new Date()
	
	String comentario
	
	boolean pg=false
	
	boolean acreditada=false
	
	Set dias=new HashSet()
	
	int diasPagados=0

	CalendarioDet calendarioDet
	
	boolean cierreAnual

	Date dateCreated

	Date lastUpdated


    static constraints = {
		comentario nullable:true,maxSize:250
		acreditada nullable:true
		calendarioDet nullable:true
		cierreAnual nullable:true
	}

	static hasMany = [dias:Date]

	static mapping = {
		hasMany joinTable: [name: 'vacaciones_grupo_dias',
                           key: 'vacacines_grupo_id',
                           column: 'fecha',
                           type: "date"]
		solicitud type:'date'
	}
}
