package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat


class DiaFestivo {

	Integer ejercicio
	Date fecha
	String descripcion
	boolean parcial=false
	@BindingFormat('HH:mm')
	Date salida

	

	static constraints = {
		ejercicio inList:[2014,2015,2016,2017,2018,2019,2020]
		descripcion blank:false
		salida nullable:true
	}
	
	static mapping = {
		fecha type:'date'
		salida type:'time'
	}
	
}
