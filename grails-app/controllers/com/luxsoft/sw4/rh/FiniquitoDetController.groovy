package com.luxsoft.sw4.rh



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

//@Secured(["hasRole('ADMIN')"])
@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class FiniquitoDetController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "GET"]

    def finiquitoService

    def index(Integer max) {
        params.max = Math.min(max ?: 40, 100)
        respond FiniquitoDet.list(params), model:[finiquitoDetInstanceCount: FiniquitoDet.count()]
    }

    def show(FiniquitoDet finiquitoDetInstance) {
        respond finiquitoDetInstance
    }

    def create(Finiquito finiquito) {
        respond new FiniquitoDet(params), model:[finiquito: finiquito]
    }

    @Transactional
    def save(FiniquitoDet finiquitoDetInstance) {
        if (finiquitoDetInstance == null) {
            notFound()
            return
        }

        def finiquito = finiquitoDetInstance.finiquito

        if (finiquitoDetInstance.hasErrors()) {
            respond finiquitoDetInstance.errors, view:'create', model:[finiquito:finiquitoDetInstance.finiquito]
            return
        }
        finiquitoDetInstance.manual = true
        finiquito.addToPartidas(finiquitoDetInstance)
        finiquito = finiquitoService.save(finiquito)
        flash.message = "Partida registrada"
        redirect controller: 'finiquito', action:'show', id: finiquito.id
        
    }

    @Transactional
    def delete(FiniquitoDet finiquitoDetInstance) {

        if (finiquitoDetInstance == null) {
            notFound()
            return
        }
        def finiquito = finiquitoDetInstance.finiquito
        finiquito.removeFromPartidas(finiquitoDetInstance)
        finiquito = finiquitoService.save(finiquito)

        flash.message = "Partida eliminada ${finiquitoDetInstance.id}"
        redirect controller: 'finiquito', action:'show', id: finiquito.id
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'finiquitoDet.label', default: 'FiniquitoDet'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
