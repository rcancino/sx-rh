package com.luxsoft.sw4.rh



class Turno {

	String descripcion
	
	Date inicioDeDia
	Date horaLimiteDeTrabajo
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
