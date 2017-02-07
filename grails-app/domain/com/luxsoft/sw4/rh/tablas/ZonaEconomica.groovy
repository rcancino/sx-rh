package com.luxsoft.sw4.rh.tablas



class ZonaEconomica {	

	String clave
	BigDecimal salario = 0
	Integer ejercicio
	BigDecimal uma = 0
	
	String toString(){
		return "$clave: $salario"
	}

    static constraints = {
    	ejercicio inList:[2014,2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,2025,2026,2027]
    }
	
}
