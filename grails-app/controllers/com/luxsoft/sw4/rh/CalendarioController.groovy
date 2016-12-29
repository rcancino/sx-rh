package com.luxsoft.sw4.rh

import org.springframework.security.access.annotation.Secured

import com.luxsoft.sw4.Periodo;

import grails.transaction.Transactional
import static org.springframework.http.HttpStatus.*

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Transactional(readOnly = true)
class CalendarioController {
	
	def calendarioService

    def index(Integer max) { 
		params.max = 500
		params.sort='id'
		params.order='desc'
		respond Calendario.list(params), model:[calendarioInstanceCount: Calendario.count()]
	}
	
	def create(){
		[calendarioInstance:new Calendario(ejercicio:session.ejercicio)]
	}
	
	@Transactional
	def save(Calendario calendarioInstance){
		if(calendarioInstance==null){
			notFound()
			return
		}
		if(calendarioInstance.hasErrors()){
			respond calendarioInstance.errors, view:'create'
			return
		}
		calendarioInstance.save flush:true
		flash.message = message(code: 'default.created.message', args: [message(code: 'calendario.label', default: 'Calendario')
			, calendarioInstance.id])
		redirect action:'edit', id: calendarioInstance.id
	}

	def show(Calendario calendarioInstance){
		respond calendarioInstance
	}
	
	def edit(Calendario calendarioInstance){
		[calendarioInstance:calendarioInstance]
	}

	def createPeriodo(Calendario calendarioInstance){
		if(calendarioInstance == null) {
			notFound()
			return
		}
		def folio = calendarioInstance.periodos.size() + 1
		def calendarioDetInstance = new CalendarioDet(calendario: calendarioInstance, folio: folio)
		[calendarioDetInstance: calendarioDetInstance, calendarioInstance: calendarioInstance]
	}
	
	@Transactional
	def agregarPeriodo(Calendario calendarioInstance){
		if(calendarioInstance == null) {
			notFound()
			return
		}
		CalendarioDet det=new CalendarioDet()
		bindData(det, params)
		calendarioInstance.addToPeriodos(det)
		det.validate()
		if(det.hasErrors()){
			render view:'createPeriodo',model:[calendarioDetInstance: det, calendarioInstance: calendarioInstance]
            return
		}
		calendarioInstance.save failOnError:true
		render view:'edit', model:[calendarioInstance:calendarioInstance]
	}
	
	
	@Transactional
	def eliminarPeriodo(Long id){
		CalendarioDet calendarioDet=CalendarioDet.get(id)
		if(calendarioDet==null){
			notFound()
			return
		}
		def calendarioInstance=calendarioDet.calendario
		calendarioInstance.removeFromPeriodos(calendarioDet)
		calendarioInstance.save failOnError:true
		flash.message="Segmento de calendario eliminado"
		params.id=calendarioInstance.id
		redirect action:'edit',params:params
	}
	
	@Transactional
	def delete(Calendario calendarioInstance){
		if(calendarioInstance==null){
			notFound()
			return
		}
		calendarioInstance.delete flush:true
		flash.message="Calendario ${calendarioInstance.id} eliminado "
		redirect action:'index'
	}
	
	@Transactional
	def generarPeriodos(Calendario calendarioInstance){
		if(calendarioInstance==null){
			notFound()
			return
		}
		try{
			//calendarioInstance=calendarioService.generarPeriodos(calendarioInstance)
		}catch(Exception ex){
			println ex
			flash.message="Error al generar periodo: "+ex.message
		}
		
		respond calendarioInstance, view:'show'
		
	}

	@Transactional
	def generarPeriodosCalendario(Calendario calendarioInstance){

		calendarioService.generarPeriodosCalendario(calendarioInstance)

		respond calendarioInstance, view:'show'
	}

	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendario.label', default: 'Calendario'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}
