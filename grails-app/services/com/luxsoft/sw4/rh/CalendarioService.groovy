package com.luxsoft.sw4.rh

import org.apache.commons.lang.exception.ExceptionUtils;

import com.luxsoft.sw4.Periodo

import grails.transaction.Transactional

@Transactional
class CalendarioService {

    def generarPeriodos(Calendario calendario) {
		assert !calendario.periodos,"El calendario ya esta generado debe eliminar los periodos para volver a generarlo"
		try {
			if(calendario.tipo=='SEMANA'){
				return generarPeriodosSemanales(calendario)
			}else if(calendario.tipo=='QUINCENA'){
				return generarCalendarioQuincenal(calendario)
			}
			
		}
		catch(Exception exception) {
			def ex = ExceptionUtils.getRootCause(exception)
			log.error(ex)
			String msg = ExceptionUtils.getMessage(exception)
			throw new CalendarioException(calendario,ex,msg)
		}
		
    }
	
	private Calendario generarPeriodosSemanales(Calendario c){
		
		def periodos=Periodo.getPeriodosDelYear(c.ejercicio)
		println 'Generando semanas para el periodo: ' + periodos.size()
		def folio=1
		def mes=1
		def semanas=[]
		for(Periodo periodo:periodos){
		  
		  def inicioDeSemana
		  mes++
		  for(Date dia:periodo.fechaInicial..periodo.fechaFinal){
			
			if(inicioDeSemana==null){
			  inicioDeSemana=dia
			}
			
			if(dia.day==0){
			  def semana=new Periodo(inicioDeSemana,dia)
			  semanas<<semana
			  inicioDeSemana=null
			}
			if(dia==periodo.fechaFinal && inicioDeSemana){
			  def semana=new Periodo(inicioDeSemana,dia)
			  semanas<<semana
			  inicioDeSemana=null
			}
			
		  }
		}
	  
	  for(int i=0;i<semanas.size();i++){
		println "Semana: $i  "+ semanas[i]
		def per=semanas[i]
		def partida=new CalendarioDet(
			folio:folio++,
			inicio:per.fechaInicial,
			fin:per.fechaFinal,
			asistencia:new Periodo(per.fechaInicial-7,per.fechaFinal-7)
			)
		c.addToPeriodos(partida)
	  }
	  c.save failOnError:true
	  return c
	}
	
	private Calendario generarCalendarioQuincenal(Calendario c){
		def periodos=Periodo.getPeriodosDelYear(c.ejercicio)
		
		def quincenas=[]
		for(Periodo mes:periodos) {
			def q1=new Periodo(mes.fechaInicial,mes.fechaInicial+14)
			def q2=new Periodo(mes.fechaInicial+15,mes.fechaFinal)
			quincenas<<q1
			quincenas<<q2
		}
		def folio=1
		for(int i=0;i<quincenas.size();i++){
			def per=quincenas[i]
			def partida=new CalendarioDet(
				folio:folio++,
				inicio:per.fechaInicial,
				fin:per.fechaFinal,
				asistencia:new Periodo(per.fechaInicial-7,per.fechaFinal-7)
				)
			c.addToPeriodos(partida)
		}
		c.save failOnError:true
		return c
	}
	
}

class CalendarioException extends RuntimeException{
	
	Calendario calendario
	
	CalendarioException(Calendario cal, Exception ex, String message){
		super(message,ex)
		this.calendario = cal
		
	}
}
