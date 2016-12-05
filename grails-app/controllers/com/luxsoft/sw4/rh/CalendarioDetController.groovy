package com.luxsoft.sw4.rh

import org.springframework.security.access.annotation.Secured
import grails.transaction.Transactional
import grails.converters.JSON

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Transactional(readOnly = true)
class CalendarioDetController {
    static scaffold = true

    def show(CalendarioDet calendarioDetInstance){
    	redirect controller:'calendario',action:'edit',params:[id:calendarioDetInstance.calendario.id]
    }

    def getCalendariosAsJSON() {
		def term=params.term.trim()+'%'
		def list=CalendarioDet
			.findAll("from CalendarioDet c where c.calendario.ejercicio=? and c.calendario.tipo like ? order by c.folio desc "
				,[session.ejercicio,term.toUpperCase()])
		list=list.collect{ calDet->
			def nombre="$calDet.calendario.tipo $calDet.folio  $calDet.calendario.ejercicio (${calDet.inicio.text()} - ${calDet.fin.text()}) ${calDet.calendario.comentario}"
			[id:calDet.id
				,label:nombre
				,value:nombre
			]
		}
		def res=list as JSON
		render res
	}

	def getCalendariosDisponibles() {

		def term=params.term.trim()+'%'

		def year = session.ejercicio

		def nominaSemana = Nomina.where {periodicidad == 'SEMANAL' }.list([sort:'id',order:'desc', max:1])
		def semana = nominaSemana.calendarioDet
		
		def nominaQuincena = Nomina.where {periodicidad == 'QUINCENAL'}.find([sort: 'id', order: 'desc', max:1])
		def quincena = session.calendarioQuincena

		def list = CalendarioDet.where{ calendario.ejercicio == year && calendario.tipo == 'SEMANA' }.list()
		def l2 = CalendarioDet.where{ calendario.ejercicio == year && calendario.tipo == 'QUINCENA' }.list()
		list.addAll(l2)
		
		list=list.collect{ calDet->
			def nombre="$calDet.calendario.tipo $calDet.folio  $calDet.calendario.ejercicio"
			[id:calDet.id
				,label:nombre
				,value:nombre
			]
		}
		def res=list as JSON
		
		render res
	}
}
