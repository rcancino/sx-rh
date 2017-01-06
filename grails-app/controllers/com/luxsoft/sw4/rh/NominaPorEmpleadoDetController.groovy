package com.luxsoft.sw4.rh



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class NominaPorEmpleadoDetController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    // def index(Integer max) {
    //     params.max = Math.min(max ?: 40, 100)
    //     respond NominaPorEmpleadoDet.list(params), model:[nominaPorEmpleadoDetInstanceCount: NominaPorEmpleadoDet.count()]
    // }

    def show(NominaPorEmpleadoDet nominaPorEmpleadoDetInstance) {
        respond nominaPorEmpleadoDetInstance
    }

    def create(NominaPorEmpleado nominaPorEmpleado) {
        respond new NominaPorEmpleadoDet(parent: nominaPorEmpleado)
    }

    @Transactional
    def save(NominaPorEmpleadoDet nominaPorEmpleadoDetInstance) {
        if (nominaPorEmpleadoDetInstance == null) {
            notFound()
            return
        }

        if (nominaPorEmpleadoDetInstance.hasErrors()) {
            respond nominaPorEmpleadoDetInstance.errors, view:'create'
            return
        }

        //nominaPorEmpleadoDetInstance.save flush:true
        def ne = nominaPorEmpleadoDetInstance.parent
        def det = nominaPorEmpleadoDetInstance

        if(det?.concepto?.importeExcento){
            det.importeExcento=det.importeGravado
            det.importeGravado=0.0
        }
        if(det.comentario==null){
            det.comentario=" "
        }
        ne.addToConceptos(det)
        ne.actualizar()
        ne.save(failOnError:true)

        flash.message = "Percepcion/Deduccion manual generada "
        redirect controller:'nominaPorEmpleado', action: 'edit', id: ne.id
       
    }

    def edit(NominaPorEmpleadoDet nominaPorEmpleadoDetInstance) {
        respond nominaPorEmpleadoDetInstance
    }

    @Transactional
    def update(NominaPorEmpleadoDet nominaPorEmpleadoDetInstance) {
        if (nominaPorEmpleadoDetInstance == null) {
            notFound()
            return
        }

        if (nominaPorEmpleadoDetInstance.hasErrors()) {
            respond nominaPorEmpleadoDetInstance.errors, view:'edit'
            return
        }

        nominaPorEmpleadoDetInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'NominaPorEmpleadoDet.label', default: 'NominaPorEmpleadoDet'), nominaPorEmpleadoDetInstance.id])
                redirect nominaPorEmpleadoDetInstance
            }
            '*'{ respond nominaPorEmpleadoDetInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(NominaPorEmpleadoDet nominaPorEmpleadoDetInstance) {

        if (nominaPorEmpleadoDetInstance == null) {
            notFound()
            return
        }

        nominaPorEmpleadoDetInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'NominaPorEmpleadoDet.label', default: 'NominaPorEmpleadoDet'), nominaPorEmpleadoDetInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'nominaPorEmpleadoDet.label', default: 'NominaPorEmpleadoDet'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
