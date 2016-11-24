package com.luxsoft.sw4.rh

import org.joda.time.LocalTime
import org.jadira.usertype.dateandtime.joda.*

class Turno {

	String descripcion
	
	LocalTime inicioDeDia
	LocalTime horaLimiteDeTrabajo
	LocalTime inicioDeTiempoExtra

	List dias

	boolean horaLimiteSiguienteDia

	Date dateCreated
	Date lastUpdated

	

	static hasMany = [dias: TurnoDet]

    static constraints = {
    	descripcion blank:false
    	inicioDeTiempoExtra nullable:true
    }

    static mapping = {
    	dias cascade: "all-delete-orphan"
    	inicioDeDia type: PersistentLocalTime
    	horaLimiteDeTrabajo type: PersistentLocalTime
    	inicioDeTiempoExtra type: PersistentLocalTime
    }
    
	
	Map toDiasMap() {
		return dias.collectEntries{item->
			[(item.dia):item]
		}
		
	}
	
	String toString(){
		return descripcion
	}
}
