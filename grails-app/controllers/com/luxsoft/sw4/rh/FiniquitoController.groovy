package com.luxsoft.sw4.rh



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class FiniquitoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def finiquitoService

    def index(Integer max) {
        params.max = Math.min(max ?: 40, 100)
        params.sort = 'dateCreated'
        params.order = 'desc'
        respond Finiquito.list(params), model:[finiquitoInstanceCount: Finiquito.count()]
    }

    def show(Finiquito finiquitoInstance) {
        respond finiquitoInstance
    }

    private getBajas(){
        def bajas = BajaDeEmpleado
            .findAll('from BajaDeEmpleado b where date(b.fecha) > ? and b not in(select f.baja from Finiquito f)',
                [Date.parse('dd/MM/yyyy','01/08/2016')])
        return bajas 
    }

    def create() {
        
        [finiquitoInstance: new Finiquito(params), bajas:getBajas()]
    }



    @Transactional
    def save(Finiquito finiquitoInstance) {
        if (finiquitoInstance == null) {
            notFound()
            return
        }
        finiquitoInstance = finiquitoService.save(finiquitoInstance)
        flash.message = "Finiquito ${finiquitoInstance.id} generado"
        redirect finiquitoInstance
    }

    def edit(Finiquito finiquitoInstance) {
        respond finiquitoInstance
    }

    @Transactional
    def update(Finiquito finiquitoInstance) {

        println "Actualizando el Finiquito:  -"+finiquitoInstance

        println finiquitoInstance.comisiones

        if (finiquitoInstance == null) {
            notFound()
            return
        }
        if (finiquitoInstance.hasErrors()) {
            respond finiquitoInstance.errors, view:'edit'
            return
        }

        finiquitoInstance = finiquitoService.save(finiquitoInstance)
        flash.message = "Finiquito ${finiquitoInstance.id} actualizado"
        redirect finiquitoInstance
    }

    @Transactional
    def delete(Finiquito finiquitoInstance) {

        if (finiquitoInstance == null) {
            notFound()
            return
        }

        finiquitoService.eliminar(finiquitoInstance)
        flash.message = " Finiquito eliminado ${finiquitoInstance.id}"
        redirect action: 'index'
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'finiquito.label', default: 'Finiquito'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
