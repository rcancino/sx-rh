package com.luxsoft.sw4.rh

import org.apache.commons.lang.exception.ExceptionUtils

class ProcesadorDeNomina {
	
	def reglas=[]
	
	def procesar(Nomina nomina) {
		nomina.partidas.each{
			procesar(it)
		}
		return nomina
	}
	
	def procesar(NominaPorEmpleado ne) {
		
		reglas.each{ p ->

			println p.class
			try {
				if(ne.asistencia){
					p.procesar(ne)
				}
			}catch(Exception ex) {
				ex.printStackTrace()
				String msg="Error en regla: ${p} Causa: "+ExceptionUtils.getRootCauseMessage(ex)
				throw new ProcesadorDeNominaException(message:msg,nominaPorEmpleado:ne)
			}
			
		}
		//depurar(ne)
		return ne
	}
	/*
	def depurar(NominaPorEmpleado ne){
		try{
			println 'Depurando nomina por empleado : '+ne.id
		}catch(Exception ex){
			ex.printStackTrace()
			
		}
		
	}
	*/
	

}

class ProcesadorDeNominaException extends RuntimeException{
	
	String message
	NominaPorEmpleado nominaPorEmpleado
	
}
