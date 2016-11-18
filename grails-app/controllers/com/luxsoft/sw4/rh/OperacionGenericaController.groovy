package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class OperacionGenericaController {
    static scaffold = true

    def index(){
		params.max?:500
		params.sort='lastUpdated'
		params.order='desc'
		def ejercicio = session.ejericio
		log.info('Ejercicio: ' + ejercicio)
		//def query = OperacionGenerica.where{calendarioDet.calendario.ejercicio == ejercicio}
		def query = OperacionGenerica.where {calendarioDet.calendario.ejercicio == 2016 }
		def list = query.list(params)
		log.info('Reg: ' + list.size())
		[operacionGenericaInstanceList:list,operacionGenericaInstanceCount:query.count()]
	}

	def delete(OperacionGenerica operacion){
		if(operacion==null){
			flash.message="No encontro operacion para eliminar"
			redirect action:'index'
			return
		}
		operacion.delete flush:true
		flash.message="Operación eliminada: "+operacion.id
		redirect action:'index'
	}
}
