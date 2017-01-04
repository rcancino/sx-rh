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



        
        finiquitoInstance = finiquitoService.inicializarFiniquito(finiquitoInstance)
        finiquitoInstance = finiquitoService.registrarVacaciones(finiquitoInstance)
        finiquitoInstance = finiquitoService.registrarAguinaldoFiniquito(finiquitoInstance)


        finiquitoInstance.validate()
        if (finiquitoInstance.hasErrors()) {
            respond finiquitoInstance.errors, view:'create', model:[bajas:getBajas()]
            return
        }
        
        finiquitoInstance.save flush:true
        //finiquitoInstance = finiquitoService.calcular(session.ejercicio,finiquitoService)
        //finiquitoInstance = finiquitoService.inicializarFiniquito(finiquitoInstance)

        request.withFormat {
            form multipartForm {
                flash.message = "Finiquito para $finiquitoInstance.empleado generado exitosamente"
                redirect finiquitoInstance
            }
            '*' { respond finiquitoInstance, [status: CREATED] }
        }
    }

    def edit(Finiquito finiquitoInstance) {
        respond finiquitoInstance
    }

    @Transactional
    def update(Finiquito finiquitoInstance) {
        if (finiquitoInstance == null) {
            notFound()
            return
        }

        if (finiquitoInstance.hasErrors()) {
            respond finiquitoInstance.errors, view:'edit'
            return
        }

        finiquitoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Finiquito.label', default: 'Finiquito'), finiquitoInstance.id])
                redirect finiquitoInstance
            }
            '*'{ respond finiquitoInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Finiquito finiquitoInstance) {

        if (finiquitoInstance == null) {
            notFound()
            return
        }

        finiquitoInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Finiquito.label', default: 'Finiquito'), finiquitoInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
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
