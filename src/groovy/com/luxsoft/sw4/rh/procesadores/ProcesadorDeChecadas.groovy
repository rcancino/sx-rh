package com.luxsoft.sw4.rh.procesadores

import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.Asistencia
import com.luxsoft.sw4.rh.AsistenciaDet
import com.luxsoft.sw4.rh.Checado
import com.luxsoft.sw4.rh.DiaFestivo;
import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.TurnoDet
import java.text.DateFormat
import java.text.SimpleDateFormat
import  java.util.Date
import java.sql.Time


class ProcesadorDeChecadas {
	
	def tolerancia=(60*1000*10)
	
	
	
	def registrarChecadas(Asistencia asistencia) {
		def empleado=asistencia.empleado
		def turno=empleado.perfil.turno
		assert turno,"El empleado ${empleado} debe tener un turno asignado"
		
		def turnosMap=turno.toDiasMap()
		
		Calendar calendar = Calendar.getInstance();
		asistencia.partidas.each{it ->
			def sdia=Dias.toNombre(it.fecha)
			TurnoDet turnoDet=turnosMap[sdia]
			assert turnoDet,"Se requiere turno para ${sdia}"
			it.turnoDet=turnoDet
			
			if(turnoDet.entrada1){
				Date ini=turnoDet.entrada1
				Date fin=turnoDet.salida2?:turnoDet.salida1
				calendar.setTime(ini)
				def iniMillis=calendar.getTimeInMillis()
				calendar.setTime(fin)
				def finMillis=calendar.getTimeInMillis()
				def res=finMillis-iniMillis
				it.horasTrabajadas=(res/(1000*60*60) as BigDecimal)
				
			}

			if(!turnoDet.entrada1){
				
				it.horasTrabajadas=9.5 as BigDecimal
				
			}
			
			def lecturas=buscarLecturas(empleado,it.fecha)
			def row = 1
			log.debug " Lecturas detectadas ${lecturas.size()} para empleado: ${empleado} fecha:${it.fecha}"
			lecturas.each{ lec ->
				if(!it.manual)
					resolverChecada(turnoDet,it,lec,row++)
			}
		}
		
	}
	
	def resolverChecada(TurnoDet t,AsistenciaDet ad,Checado chk,int row) {
		
		def time=new Time(chk.hora.time)
		def lectura = chk.hora
		
		def festivo=DiaFestivo.findByFecha(ad.fecha)
		
		if(festivo){
			if(!festivo.parcial){
				ad.tipo='DIA_FESTIVO'
				return
			}
		}
		
		if((t.entrada1==null || t.salida1 == null) && !chk) { // Descanso
		//if((t.entrada1==null || t.salida1 == null)) { // Descanso
			ad.tipo='DESCANSO'
			return 
		}

			//Descanso Laborado 
		
		if((t.entrada1==null || t.salida1==null) && chk) {

			ad.tipo='DESCANSO'

			String salidaT = "13:00";
			String entradaT = "09:00";
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			Date entrada = formatter.parse(entradaT);
			Date salida = formatter.parse(salidaT);
			


			if(lectura<salida && (ad.entrada1==null)) {
				ad.entrada1=time
				return
			}
			if(lectura>=salida) {
				ad.salida1=time
				
				return
			}
			
			if(ad.entrada1){
				Date checado=ad.entrada1
				def dif = (lectura.getTime() - checado.getTime() ) / (1000 * 60)
				//def dif=( ((lectura.getHourOfDay()*60)+lectura.getMinuteOfHour()) - ((checado.getHourOfDay()*60)+checado.getMinuteOfHour()) )
				if(dif > 60 ) {
					ad.salida1=time
					
				}
				
			}
			return
		}


		
		//Dia festivo parcial
		if(festivo && festivo.parcial){
			//def salidaFestivo=Date.fromDateFields(festivo.salida)
			def salidaFestivo=festivo.salida
			if(lectura<salidaFestivo) {
				ad.entrada1=time
				return
			}
			if(lectura >= salidaFestivo) {
				ad.salida1=time
				return
			}
			return
		}
		
		//Medio dia (Sabado eje)
		if(t.entrada2==null || t.salida2==null) {
			if(lectura<t.salida1 && (ad.entrada1==null)) {
				ad.entrada1=time
				return
			}
			if(lectura>=t.salida1) {
				ad.salida1=time
				
				return
			}
			
			if(ad.entrada1){
				Date checado=ad.entrada1
				def dif = (lectura.getTime() - checado.getTime() ) / (1000 * 60)
				//def dif=( ((lectura.getHourOfDay()*60)+lectura.getMinuteOfHour()) - ((checado.getHourOfDay()*60)+checado.getMinuteOfHour()) )
				if(dif > 60 ) {
					ad.salida1=time
					
				}
				
			}
			return
		}
		
		// Jornada Normal	
		if(lectura < t.salida1) {
			ad.entrada1 = time
			
			return
		}
		
		if( lectura >= t.salida2 || (row==4)) {
			ad.salida2=time
			
			return
		}
		
		if(lectura>t.salida1 && lectura<t.entrada2) {
			if(ad.salida1==null) {
				ad.salida1=time
				return
			}
		}
		
		if(ad.salida1!=null) {
			
			if(lectura>t.salida1) {
				ad.entrada2=time
			}
		}
		
	}
	
	
	def buscarLecturas(Empleado e,Date fecha) {
		def lecturas=Checado.findAll(sort:"numeroDeEmpleado"){numeroDeEmpleado==e.perfil.numeroDeTrabajador && fecha==fecha}
		lecturas.sort(){ c->
			c.hora
		}
		
		def valid =[]
		def last=null
		lecturas.each{ reg->
			if(last==null){
				last=reg.hora
				valid.add(reg)
			}
			def dif=reg.hora.time-last.time
			if(dif>tolerancia ){
				
				last=reg.hora
				valid.add(reg)
			}
		}
		return valid
	}

}
