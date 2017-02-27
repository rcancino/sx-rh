package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.Periodo

@Transactional
class ControlDeVacacionesService {

    def actualizarAcumulados(NominaPorEmpleado ne) {
		
    }
	
	def actualizarAcumulados(Empleado empleado,Integer ejercicio){
		def control=ControlDeVacaciones.findByEmpleadoAndEjercicio(empleado,ejercicio)
		if(control){
			def hql="from NominaPorEmpleadoDet n where n.parent.empleado=? "
				+" and n.concepto.clave=? and n.parent.nomina.ejercicio=?"
			def impores=NominaPorEmpleadoDet.executeQuery(hql,[empleado,'P024',ejercicio])
		}
	}

	def save(Integer ejercicio,Empleado empleado){
		def control=ControlDeVacaciones.findByEmpleadoAndEjercicio(empleado,ejercicio)
		assert !control,'Ya esta registrado el control de vacaciones: '+control
		def fecha=new Date()
		def ca=new ControlDeVacaciones(
			empleado:empleado,
			ejercicio:ejercicio,
			acumuladoExcento:0,
			acumuladoGravado:0,
			antiguedadDias:fecha-empleado.alta,
			antiguedadYears:0,
			diasVacaciones:0,
			diasTomados:0)


		Calendar c = Calendar.getInstance()
		c.setTime(empleado.alta)
				c.set(Calendar.YEAR,ejercicio)
		if(Periodo.obtenerYear(empleado.alta)==ejercicio){
				c.set(Calendar.YEAR,ejercicio+1)
		}
		ca.aniversario=c.getTime()
		
		ca.save failOnError:true

		return ca
		
		
	}
}
