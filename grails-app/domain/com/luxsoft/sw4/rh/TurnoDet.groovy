package com.luxsoft.sw4.rh


//import groovy.time.Duration



class TurnoDet {

	String dia

	Date entrada1
	Date salida1
	
	Date entrada2
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
    	
    }
	
	static transients = ['horasDeTrabajo']
	
	BigDecimal getHorasDeTrabajo(){
		if(horasDeTrabajo==null){
			def ini=entrada1
			def fin=salida2?:salida1
			def res=fin.getLocalMillis()-ini.getLocalMillis()
			horasDeTrabajo=(res/(1000*60*60) as BigDecimal)
		}
		return horasDeTrabajo
	}
	
}
