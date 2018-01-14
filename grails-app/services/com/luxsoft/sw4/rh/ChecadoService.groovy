package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional

import com.luxsoft.sw4.rh.Checado
import com.luxsoft.sw4.Periodo

@Transactional
class ChecadoService {

	def grailsApplication

    @NotTransactional
    def importarLecturas(Periodo periodo ,String rfc){
		//log.info 'Importando lectoras para el periodo: '+periodo
    	for(date in periodo.fechaInicial..periodo.fechaFinal){
    		importarLecturas(date , rfc)
    	}
    	
    }

    @NotTransactional
    def importarLecturas(Date date,String rfc){

		def sdate=date.format('yyyyMMdd')
		def rawdata=grailsApplication.config.sw4.rh.asistencia.rawdata

		
		File file =new File(rawdata+"/"+sdate+".chk")
		
		log.info " Importando lecturas de checado para : ${date.text()} desde el archivo: $file"
    	
    	assert file.exists(), 'No existe el archivo :' + file
		
		
		int lector
		def fecha
		file.eachLine{line,row ->
		  def fields=line.split(',')
		  
		  if(fields.length==2){
			lector=fields[0].toInteger()
			//println 'Lector: '+lector
		  }else if(fields.length==4){
			fecha=Date.parse('yyyyMMdd',fields[2])
			  //println 'Fecha: '+fecha
		  }else{

		  		if(rfc=='PBA0511077F9'){
		  			
		  			lector=fields[0]
		  			fecha=Date.parse('yyyyMMdd',fields[1])
		  			def hora=Date.parse('HHmmss',fields[2])
		  			def numeroDeEmpleado=fields[4].replaceFirst("0", "")
		  			def r=Checado.findOrSaveWhere(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:numeroDeEmpleado)

		  			}else{
		  				//
						def hora=Date.parse('HHmmss',fields[0])
						//println "registrando evento empleado: ${fields[2]} hora:$hora  Fecha:$fecha"
						def r=Checado.findOrSaveWhere(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
		  			}
				
		  }
		  
		}
    }

    def eliminarLecturas(def periodo){
    	(periodo.fechaInicial..periodo.fechaFinal).each{ dia ->
    		Checado.executeUpdate("delete from Checado where date(fecha) = ? ", [dia])
    	}
    }
}
