package com.luxsoft.sw4.rh.asimilados

import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON

import com.luxsoft.sw4.rh.Nomina
import com.luxsoft.sw4.rh.CalendarioDet

@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class NominaAsimiladosController {

	def nominaAsimiladosService

    def index(Integer max) {
        params.max = 1000  //Math.min(max ?: 60, 100)
		params.sort = params.sort?:'lastUpdated'
		params.order = 'desc'
		def query = Nomina.where{ tipo == 'ASIMILADOS'}
		[nominaInstanceList:query.list(params) ,nominaInstanceListTotal:query.count(params)]
    }

	@Transactional
	def generar(Long calendarioDet){
		
		def nominaInstance = nominaAsimiladosService.generar(calendarioDet,params.formaDePago)
		
		flash.message = "Nómina para asimilados generada ${nominaInstance.id}"
		redirect action: 'index'
		
	}

	def show(Nomina nominaInstance) {
		respond nominaInstance
    }

	def getCalendariosDisponibles() {

		def term = params.term.trim() + '%'
		def list = CalendarioDet.where{ calendario.comentario == 'ASIMILADOS' }.list([sort:'id',order:'desc', max:1])
		
		list=list.collect{ calDet->
			def nombre = "$calDet.calendario.tipo $calDet.folio  ($calDet.calendario.ejercicio $calDet.calendario.comentario )"
			[id:calDet.id
				,label: nombre
				,value: nombre
			]
		}
		def res = list as JSON
		render res
	}

	def agregarNominaPorEmpleado(Nomina nomina){
		responde nomina
	}
}
