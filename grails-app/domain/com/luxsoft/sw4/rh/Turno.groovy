package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat

class Turno {

	String descripcion
	
	@BindingFormat('HH:mm')
	Date inicioDeDia

	@BindingFormat('HH:mm')
	Date horaLimiteDeTrabajo
	
	@BindingFormat('HH:mm')
	Date inicioDeTiempoExtra

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
    	inicioDeDia type:'time'
    	horaLimiteDeTrabajo type: 'time'
    	inicioDeTiempoExtra type: 'time'
    
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
