package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional
import grails.converters.JSON

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class ConceptoDeNominaController {

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() { 
        redirect action:'percepciones'
        //respond conceptosList
    }

    def percepciones(Integer max){
    	params.max = Math.min(max ?: 150, 500)
        params.sort = 'lastUpdated'
        params.order = 'desc'
		String tipo=params.tipo?:'PERCEPCION'
    	def conceptosList=ConceptoDeNomina.findAllByTipo(tipo,params)
        render view:'index', model:[conceptosList:conceptosList]
    }

    

    def deducciones(Integer max){
		params.max = Math.min(max ?: 150, 500)
        params.sort = 'lastUpdated'
        params.order = 'desc'
		def conceptosList=ConceptoDeNomina.findAllByTipo('DEDUCCION')
        render view:'index', model:[conceptosList:conceptosList]
	}

    def create(){
    	def conceptoInstance=new ConceptoDeNomina(params)
    	[conceptoInstance:conceptoInstance]
    }

    def show(ConceptoDeNomina conceptoInstance){
        respond conceptoInstance
    }
    
    @Transactional
    def save(ConceptoDeNomina conceptoInstance){
    	if(!conceptoInstance){
    		println 'No se puede generar concepto nulo: '+params
    		return
    	}
    	if(conceptoInstance.hasErrors()){
			render view:'create',model:[conceptoInstance:conceptoInstance]
            return
    	}
    	conceptoInstance.save flush:true
    	flash.message = message(code: 'default.created.message', args: [message(code: 'conceptoInstance.label', default: 'ConceptoDeNomina'), conceptoInstance.clave])
		respond view:'show', conceptoInstance

    }

    def edit(ConceptoDeNomina conceptoInstance){
    	//render view:'create',model:[conceptoInstance:conceptoInstance,tipoDeForma:'edit',action:'update',method:'PUT']
        respond conceptoInstance
    }

    @Transactional
    def update(ConceptoDeNomina conceptoInstance){
    	if(!conceptoInstance){
    		notFound()
    		return
    	}
    	if(conceptoInstance.hasErrors()){
			render view:'edit',model:[conceptoInstance:conceptoInstance,tipoDeForma:'edit',action:'update',method:'PUT']
            return
    	}
    	conceptoInstance.save flush:true
    	flash.message = message(code: 'default.updated.message', args: [message(code: 'ConceptoDeNomina.label', default: 'ConceptoDeNomina'), conceptoInstance.clave])
		respond view: 'show', conceptoInstance
        //redirect action:conceptoInstance.tipo=='PERCEPCION'?'percepciones':'deducciones'

    }

    def getConceptos() {
        def term=params.term.trim()+'%'
        def query=ConceptoDeNomina.where{}
        query=query.where{
            clave=~term || tipo=~term || descripcion=~term
        }
        def list=query.list(max:20, sort:"clave")
        list=list.collect{ emp->
            def nombre=emp.toString()
            [id:emp.id, label:nombre, value:nombre]
        }
        def res=list as JSON
        render res
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message'
                	, args: [message(code: 'ConceptoDeNomina.label', default: 'ConceptoDeNomina'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
