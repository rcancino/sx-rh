package com.luxsoft.sw4.rh.finiquito

import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.Periodo

import com.luxsoft.sw4.rh.*

class PercepcionBuilder {

	def build(Finiquito finiquito){
		
		ZonaEconomica smg = ZonaEconomica.where {ejercicio == Periodo.obtenerYear(finiquito.empleado.baja.fecha) && clave == 'A'}.find()
		
		finiquito.with{

    	  	sueldo = salario * diasPorPagar

    	  	def diasPrimaDominical = 0
    	  	  
			primaDominicalExenta = salario * diasPrimaDominical > smg.salario * diasPrimaDominical ? smg.salario * diasPrimaDominical : salario * diasPrimaDominical
						  
	  		primaDominicalGravada = (salario * diasPrimaDominical) - primaDominicalExenta

			  compensacionSAF = 0.0
			
			  subsEmpPagado = 0.0
			
			  subsEmpAplicado = 0.0
          
        }

        return finiquito
	}
}