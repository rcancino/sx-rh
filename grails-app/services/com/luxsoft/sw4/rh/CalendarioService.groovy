package com.luxsoft.sw4.rh

import org.apache.commons.lang.exception.ExceptionUtils;

import com.luxsoft.sw4.Periodo

import grails.transaction.Transactional

import java.math.*

@Transactional
class CalendarioService {


	def generarPeriodosCalendario(Calendario calendario){

		if (calendario.tipo=='SEMANA'){
			generarSemanasCalendario(calendario)
		}
		if(calendario.tipo=='QUINCENA'){
			generarQuincenasCalendario(calendario)
		}
	}

	def generarQuincenasCalendario(Calendario calendario){
		def periodos=Periodo.getPeriodosDelYear(calendario.ejercicio)
		def periodoAnual= Periodo.getPeriodoAnual(calendario.ejercicio)
		println 'Periodo:  '+periodoAnual
		def quincenas=[]
		def quincenasAsistencia=[]
		
		
		

		for(Periodo mes:periodos) {
            
			def q1=new Periodo(mes.fechaInicial,mes.fechaInicial+14)
			def q2=new Periodo(mes.fechaInicial+15,mes.fechaFinal)
            
			quincenas<<q1
			quincenas<<q2
            
          def q1Asistencia=new Periodo(q1.fechaInicial-2,q1.fechaFinal-2)
          def q2Asistencia=new Periodo(q2.fechaInicial-2,q2.fechaFinal-2)
           
            if(q1.fechaInicial==periodoAnual.fechaInicial){

                q1Asistencia.fechaInicial=periodoAnual.fechaInicial

            }
             if(q2.fechaFinal==periodoAnual.fechaFinal){
                q2Asistencia.fechaFinal=q2.fechaFinal
            }
            
            quincenasAsistencia<<q1Asistencia
            quincenasAsistencia<<q2Asistencia
            
            
         

		}

		

		def folio=1
		
		for(int i=0;i<quincenas.size();i++){
			def quincena=quincenas[i]
            def fechaDePago=quincenas[i].fechaFinal-2
            def month=Periodo.obtenerMes(quincenas[i].fechaInicial)+1
            def bimestre=(month/2).setScale(0,RoundingMode.UP).intValue()
            
			def calendarioDet=CalendarioDet.findAllByCalendarioAndFolio(calendario,folio)
                if(!calendarioDet){
                   def partida=new CalendarioDet(
                    folio:folio++,
                    inicio:quincenas[i].fechaInicial,
                    fin:quincenas[i].fechaFinal,
                    fechaDePago:fechaDePago,
                   	bimestre:bimestre,
                    mes:month,
                    asistencia:new Periodo(quincenasAsistencia[i].fechaInicial,quincenasAsistencia[i].fechaFinal)       
                    )
          			calendario.addToPeriodos(partida)
            		calendario.save failOnError:true,flush:true
                    println partida.mes+"   "+partida.bimestre
                    
                }else{
                	log.info 'Ya existe un registro para este periodo'
                }
            
            
		}

	}


	def generarSemanasCalendario(Calendario calendario){
    
    def periodo= Periodo.getPeriodoAnual(calendario.ejercicio)
    def folio=1
    def folioAsistencia=1
    def mes=1
    def semanas=[]
    def semanasAsistencia=[]
    def inicioDeSemana
    def inicioAsistencia


     for(Date dia:periodo.fechaInicial..periodo.fechaFinal){

         def semana=new Periodo()  

        if(dia==periodo.fechaInicial && dia.day==0){
             continue
         }

         if(inicioDeSemana==null){
             inicioDeSemana=dia
         }
         if((dia-periodo.fechaInicial)<=4){
             inicioDeSemana=periodo.fechaInicial
         }
         if(dia.day==0){    
              semana=new Periodo(inicioDeSemana,dia)               
             if(semana.dias()>=3){
                semanas<<semana 
             }


             inicioDeSemana=null
         }	


         if( inicioDeSemana && ((periodo.fechaFinal-inicioDeSemana)>=3)  && (periodo.fechaFinal==dia) ){

                 semana=new Periodo(inicioDeSemana,dia)   
                semanas<<semana 

             }
         if( inicioDeSemana &&((periodo.fechaFinal-inicioDeSemana)<=3) && (periodo.fechaFinal==dia) ){
             semanas[semanas.size()-1].fechaFinal=periodo.fechaFinal
         }

     }

    for(int i=0;i<semanas.size();i++){

            def semanaAsist=new Periodo(semanas[i].fechaInicial-4,semanas[i].fechaFinal-4)

         if(semanas[i].fechaInicial==periodo.fechaInicial){
             semanaAsist.fechaInicial=semanas[i].fechaInicial
         }
         if(semanas[i].fechaFinal==periodo.fechaFinal){
            semanaAsist.fechaFinal=semanas[i].fechaFinal
         }

         semanasAsistencia<<semanaAsist
        
        
        
        def mesInicial=Periodo.obtenerMes(semanas[i].fechaInicial)+1
				
		def mesFinal=Periodo.obtenerMes(semanas[i].fechaFinal)+1

		mes=mesInicial

		def bimestre

		def  trimestre

		def cuatrimestre

		def semestre

		if(mesInicial!=mesFinal){
			def periodoMesInicial=Periodo.getPeriodoEnUnMes(mes)
            
			if(periodoMesInicial.fechaFinal.day<=3){
				mes=mesFinal
				}

			}

			bimestre=(mes/2).setScale(0,RoundingMode.UP).intValue()

			trimestre=(mes/3).setScale(0,RoundingMode.UP).intValue()

			cuatrimestre=(mes/4).setScale(0,RoundingMode.UP).intValue()

			semestre=(mes/6).setScale(0,RoundingMode.UP).intValue()
        
        	def fechaDePago=semanas[i].fechaFinal-2
        	folio=i+1
               def calendarioDet=CalendarioDet.findAllByCalendarioAndFolio(calendario,folio)
                if(!calendarioDet){
                   def partida=new CalendarioDet(
                    folio:folio,
                    inicio:semanas[i].fechaInicial,
                    fin:semanas[i].fechaFinal,
                    fechaDePago:fechaDePago,
                    bimestre:bimestre,
                    mes:mes,
                    asistencia:new Periodo(semanasAsistencia[i].fechaInicial,semanasAsistencia[i].fechaFinal)       
                    )
          			calendario.addToPeriodos(partida)
            		calendario.save failOnError:true,flush:true 
                }else{
                	log.info 'Ya existe un registro para este periodo'
                }
				
    }
    

}




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
