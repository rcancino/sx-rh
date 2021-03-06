package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat
//import groovy.time.Duration



class TurnoDet {

	String dia
	@BindingFormat('HH:mm')
	Date entrada1
	@BindingFormat('HH:mm')
	Date salida1
	@BindingFormat('HH:mm')
	Date entrada2
	@BindingFormat('HH:mm')
	Date salida2
	
	BigDecimal horasDeTrabajo
	

	static belongsTo = [turno: Turno]

    static constraints = {
    	//dia inList:['LUNES','MARTES','MIERCOLES','JUEVES','SABADO','DOMINGO','LUNES']
        entrada1 nullable:true
        salida1 nullable:true
        entrada2 nullable:true
        salida2 nullable:true
    }

    static mapping = {
    	entrada1 type:'time'
    	entrada2 type:'time'
    	salida1 type:'time'
    	salida2 type:'time'
    }
	
	static transients = ['horasDeTrabajo']
	
	BigDecimal getHorasDeTrabajo(){
		if(horasDeTrabajo==null){
			def ini=entrada1
			def fin=salida2?:salida1
			def res=fin.getTime()-ini.getTime()
			horasDeTrabajo=(res/(1000*60*60) as BigDecimal)
		}
		return horasDeTrabajo
	}
	
}
