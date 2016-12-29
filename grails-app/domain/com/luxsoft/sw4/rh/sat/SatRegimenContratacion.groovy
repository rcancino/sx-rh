package com.luxsoft.sw4.rh.sat

class SatRegimenContratacion implements Serializable{
    String clave
    String descripcion

    static constraints = {
        clave blank:false,unique:true,maxSize:3
        descripcion blank:false,maxSize:300
    }
	
	String toString() {
		return "$descripcion ($clave) "
	}
}
