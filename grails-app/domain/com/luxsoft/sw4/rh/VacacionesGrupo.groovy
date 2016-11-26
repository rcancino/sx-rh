package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat
import groovy.transform.ToString

@ToString(excludes='dateCreated,lastUpdated',includeNames=true,includePackage=false)
class VacacionesGrupo {
	
	
	@BindingFormat("dd/MM/yyyy")
	Date fechaInicial = new Date()

	@BindingFormat("dd/MM/yyyy")
	Date fechaFinal = new Date()

	String comentario

	CalendarioDet calendarioDet
	
	Date dateCreated

	Date lastUpdated


    static constraints = {
		comentario maxSize:250
		calendarioDet nullable:true
	}

	static hasMany = [partidas: Vacaciones]

	static mapping = {
		fechaInicial type:'date'
		fechaFinal type:'date'
	}

	
}
