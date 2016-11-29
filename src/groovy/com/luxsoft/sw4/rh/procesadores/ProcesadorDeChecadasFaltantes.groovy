package com.luxsoft.sw4.rh.procesadores

import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.Asistencia
import com.luxsoft.sw4.rh.AsistenciaDet
import com.luxsoft.sw4.rh.Checado
import com.luxsoft.sw4.rh.DiaFestivo;
import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.TurnoDet

import java.sql.Time



class ProcesadorDeChecadasFaltantes {
	
	
	def procesar(Asistencia asistencia) {
		log.info 'Procesando checadas faltantes para: '+asistencia.empleado+"  Periodo: "+asistencia.periodo
		asistencia.partidas.findAll{it.tipo=='ASISTENCIA'}.each{ it -> //Iteracion dia por dia
		//asistencia.partidas.each{ it -> //Iteracion dia por dia

			def turnoDet=it.turnoDet
			def tipo = it.tipo
			def diaFestivo=DiaFestivo.findByFecha(it.fecha)

			def checadasRequeridas = 4
			def maximosPermitidas = 2
			def checadas = 0
			

			if(diaFestivo && diaFestivo.parcial) {

				checadasRequeridas = 2
				maximosPermitidas = 1
				if(it.entrada1) checadas ++
				if(it.salida1) checadas ++
					
			} else if(turnoDet.salida2){  // Entre semana

				if(it.entrada1) checadas ++
				if(it.salida1) checadas ++
				if(it.entrada2) checadas ++
				if(it.salida2) checadas ++
			} else { // Sabado
				checadasRequeridas = 2
				maximosPermitidas = 1
				if(it.entrada1) checadas ++
				if(it.salida1) checadas ++
			}

			int faltantes = checadasRequeridas - checadas
			
			if(faltantes == 0){
				log.info("${it.fecha.text()}  ASISTENCIA Checadas faltantes:  $faltantes ")
				
				it.tipo = tipo
				//it.comentario = ''

			} else if(faltantes > maximosPermitidas){
				log.info("${it.fecha.text()}  FALTA Checadas faltantes:  $faltantes   Maximos permitidas: $maximosPermitidas")
				it.comentario="FALTA POR ${faltantes} CHECADAS FALTANTES "
				it.tipo='FALTA'
			} else {
				//log.info " Procesando minutos no laborados por ${faltantes} checadas faltantes"
				actualizarMinutosNolaborados(it,faltantes)
			}
			it.save failOnError:true
		}
		asistencia.minutosNoLaborados=asistencia.partidas.sum 0,{it.minutosNoLaborados}
		return asistencia
	}

	def actualizarMinutosNolaborados(AsistenciaDet det, int faltantes ) {
		def turno=det.turnoDet
		
		if(faltantes == 2){
		
			det.minutosNoLaborados += 120
			
		}
		if(faltantes == 1) {
			
			det.minutosNoLaborados += 60
		}
		
	}

	
}



