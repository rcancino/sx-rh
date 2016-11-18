package com.luxsoft.sw4.rh



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.web.json.JSONArray

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Transactional(readOnly = true)
class OperacionGenericaGrupoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 40, 100)
        respond OperacionGenericaGrupo.list(params), model:[operacionGenericaGrupoInstanceCount: OperacionGenericaGrupo.count()]
    }

    def show(OperacionGenericaGrupo operacionGenericaGrupoInstance) {
        respond operacionGenericaGrupoInstance
    }

    def create() {
        respond new OperacionGenericaGrupo(params)
    }

    @Transactional
    def save(OperacionGenericaGrupo operacionGenericaGrupoInstance) {
        if (operacionGenericaGrupoInstance == null) {
            notFound()
            return
        }

        if (operacionGenericaGrupoInstance.hasErrors()) {
            respond operacionGenericaGrupoInstance.errors, view:'create'
            return
        }

        operacionGenericaGrupoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'operacionGenericaGrupo.label', default: 'OperacionGenericaGrupo'), operacionGenericaGrupoInstance.id])
                redirect operacionGenericaGrupoInstance
            }
            '*' { respond operacionGenericaGrupoInstance, [status: CREATED] }
        }
    }

    def edit(OperacionGenericaGrupo operacionGenericaGrupoInstance) {
        respond operacionGenericaGrupoInstance
    }

    @Transactional
    def update(OperacionGenericaGrupo operacionGenericaGrupoInstance) {
        if (operacionGenericaGrupoInstance == null) {
            notFound()
            return
        }

        if (operacionGenericaGrupoInstance.hasErrors()) {
            respond operacionGenericaGrupoInstance.errors, view:'edit'
            return
        }

        operacionGenericaGrupoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'OperacionGenericaGrupo.label', default: 'OperacionGenericaGrupo'), operacionGenericaGrupoInstance.id])
                redirect operacionGenericaGrupoInstance
            }
            '*'{ respond operacionGenericaGrupoInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(OperacionGenericaGrupo operacionGenericaGrupoInstance) {

        if (operacionGenericaGrupoInstance == null) {
            notFound()
            return
        }

        operacionGenericaGrupoInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'OperacionGenericaGrupo.label', default: 'OperacionGenericaGrupo'), operacionGenericaGrupoInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }


    def selectorDeEmpleados(OperacionGenericaGrupo operacionGenericaGrupoInstance) {
        params.sort = 'apellidoPaterno'
        params.order =  'desc'
        def empleados = Empleado.where{activo==true}.list(params)
        [empleados: empleados, operacion: operacionGenericaGrupoInstance]
    }

    @Transactional
    def agregarPartidas(OperacionGenericaGrupo operacion){
        log.info(' Agregando partidas al grupo: ' + operacion)
        def dataToRender=[:]
        JSONArray ids=JSON.parse(params.empleadosIds);
        ids.each {
            def empleado = Empleado.get(it)
            log.info('Generando operacion para empleado: ' + empleado)
            OperacionGenerica op = new OperacionGenerica()
            op.empleado = empleado
            op.tipo = operacion.tipo
            op.concepto = operacion.concepto
            op.importeGravado = operacion.importeGravado
            op.importeExcento = operacion.importeExcento
            op.calendarioDet = operacion.calendarioDet
            op.comentario = operacion.comentario
            operacion.addToPartidas(op)
        }
        operacion = operacion.save failOnError:true, flush:true
        dataToRender.operacionId=operacion.id        
        render dataToRender as JSON
    }
    

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'operacionGenericaGrupo.label', default: 'OperacionGenericaGrupo'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
