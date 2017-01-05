package com.luxsoft.sw4.rh



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.web.json.JSONArray


@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class VacacionesGrupoController {

    

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 40, 100)
        def list = VacacionesGrupo.list(params)
        log.info('List: ' + list.size())
        respond list, model:[vacacionesGrupoInstanceCount: VacacionesGrupo.count()]
    }

    def show(VacacionesGrupo vacacionesGrupoInstance) {
        respond vacacionesGrupoInstance
    }

    def create() {
        respond new VacacionesGrupo(params)
    }

    @Transactional
    def save(VacacionesGrupo vacacionesGrupoInstance) {
        if (vacacionesGrupoInstance == null) {
            notFound()
            return
        }

        if (vacacionesGrupoInstance.hasErrors()) {
            respond vacacionesGrupoInstance.errors, view:'create'
            return
        }

        vacacionesGrupoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'vacacionesGrupo.label', default: 'VacacionesGrupo'), vacacionesGrupoInstance.id])
                redirect action:'edit', id:vacacionesGrupoInstance.id
            }
            '*' { respond vacacionesGrupoInstance, [status: CREATED] }
        }
    }

    def edit(VacacionesGrupo vacacionesGrupoInstance) {
        respond vacacionesGrupoInstance
    }

    @Transactional
    def update(VacacionesGrupo vacacionesGrupoInstance) {
        if (vacacionesGrupoInstance == null) {
            notFound()
            return
        }

        if (vacacionesGrupoInstance.hasErrors()) {
            respond vacacionesGrupoInstance.errors, view:'edit'
            return
        }

        vacacionesGrupoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'VacacionesGrupo.label', default: 'VacacionesGrupo'), vacacionesGrupoInstance.id])
                redirect vacacionesGrupoInstance
            }
            '*'{ respond vacacionesGrupoInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(VacacionesGrupo vacacionesGrupoInstance) {

        if (vacacionesGrupoInstance == null) {
            notFound()
            return
        }

        vacacionesGrupoInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'VacacionesGrupo.label', default: 'VacacionesGrupo'), vacacionesGrupoInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    def selectorDeEmpleados(VacacionesGrupo grupo) {
        params.sort = 'apellidoPaterno'
        params.order =  'desc'
        def empleados = Empleado.where{activo==true && status == 'ALTA'}.list(params)
        def existentes = grupo.partidas.collect {it.empleado.id}

        empleados = empleados.findAll {
            !existentes.contains(it.id)
        }
        [empleados: empleados, grupo: grupo]
    }

    @Transactional
    def agregarPartidas(VacacionesGrupo grupo){
        log.info(' Agregando partidas al grupo: ' + grupo)
        def periodo = (grupo.fechaInicial..grupo.fechaFinal)
        def dataToRender=[:]
        JSONArray ids=JSON.parse(params.empleadosIds);
        ids.each {
            def empleado = Empleado.get(it)
            log.info('Agrgando vacaciones en grupo para : ' + empleado)
            def control=ControlDeVacaciones.findByEmpleadoAndEjercicio(empleado,session.ejercicio)
            assert control, 'No exsiste instancia de control de vacaciones para : ' +empleado + ' ejercicio: ' + session.ejercicio
            Vacaciones vacaciones = new Vacaciones(
                empleado: empleado,
                solicitud: grupo.fechaInicial,
                comentario: grupo.comentario,
                cierreAnual: false,
                control: control
            )
            vacaciones.calendarioDet = grupo.calendarioDet
            periodo.each { fecha ->
                vacaciones.dias.add(fecha)
               
   
            }
           
            grupo.addToPartidas(vacaciones)
            
        }
        grupo = grupo.save failOnError:true, flush:true
        actualizarControlGrupo(grupo)
        dataToRender.operacionId=grupo.id        
        render dataToRender as JSON
    }

   

    @Transactional
    def eleiminarVacaciones(VacacionesGrupo grupo){
        Vacaciones v = Vacaciones.get(params.vacacionesId)
        if(v == null){
            notFound()
            redirect action:'edit', model:[vacacionesGrupoInstance:grupo]
            return
        }

        log.info('Eliminando vacaciones del grupo ' + grupo)
        def res = grupo.removeFromPartidas(v)
        
        log.info('Eliminado:  ' + res )
        grupo = grupo.save failOnError:true, flush:true
        actualizarControlGrupo(grupo)
        redirect action:'edit', id:grupo.id
    }

    @Transactional
    def actualizarControlGrupo(VacacionesGrupo grupo){

        grupo.partidas.each{
             actualizarControl(it.control)
        }
    }

    @Transactional
   def actualizarControl(ControlDeVacaciones control){

        println "Actualizando el control de vacaciones para"+control+" de :  " +control.empleado
        def vacaciones=Vacaciones.findAllByControl(control)
            control.diasTomados=vacaciones.sum 0,{it.dias.size()}      
            control.save failOnError:true, flush:true
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'vacacionesGrupo.label', default: 'VacacionesGrupo'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
