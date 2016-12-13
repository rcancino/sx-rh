package com.luxsoft.sw4.rh



import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(includes='ejercicio,tipo,comentario',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='ejercicio,tipo')
class Calendario {

	Integer ejercicio
	
	String tipo
	
	String comentario
	
	List periodos

	Date dateCreated
	Date lastUpdated

    static constraints = {
		ejercicio inList:[2014,2015,2016,2017,2018,2019,2020]
		tipo inList:['SEMANA','QUINCENA','MES','CATORCENA','BIMESTRE','ESPECIAL']
    	comentario nullable:true,maxSize:200
    }
    
	static hasMany=[periodos:CalendarioDet]
	
	static mapping = {
		periodos cascade: "all-delete-orphan"  
	}

}
