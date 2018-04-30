package com.luxsoft.sw4.rh

import com.luxsoft.sw4.*
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import grails.validation.Validateable

@Secured(['ROLE_ADMIN'])
class BonoController {

    static scaffold = true

    static allowedMethods = [save: "POST", update: "PUT", delete: "GET"]

    def bonoService

    def jasperService

    def index(){
    	def ejercicio = session.ejercicio
    	def list = Bono.findAll("from Bono a where a.ejercicio=?", [ejercicio] )
        list=list.sort{a,b ->
            a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
        }
    	[bonoInstanceList:list]
    }

    
    def edit(Bono bonoInstance){
        [bonoInstance:bonoInstance]
    }

    def update(Bono bonoInstance){
        // def a = Bono.get(params.id)
        // bindData(a,params,[exclude: ['id', 'version']])
        // flash.message = "Bono de ${a.empleado.nombre} actualizado"
        // redirect action:'show',params:[id:a.id]
         if (bonoInstance == null) {
            notFound()
            return
        }

        if (bonoInstance.hasErrors()) {
            respond bonoInstance.errors, view:'edit'
            return
        }

        bonoInstance=bonoInstance.save failOnError: true, flush: true
        flash.message = "Bono de ${bonoInstance.empleado.nombre} actualizado"
        redirect action:'index'
    }

    def show(Aguinaldo a){
        def asistencias = aguinaldoService.buscarAsistencias(a)
        [aguinaldoInstance:a,asistencias:asistencias]
    }

    def create(){
        
        Integer ejercicio = session.ejercicio - 1 
        Ptu ptu = Ptu.where{ejercicio == ejercicio }.find()
        Ptu ptuAnterior = Ptu.where {ejercicio == (ejercicio - 1) }.find()

        BonoAnualCommand command = new BonoAnualCommand()
        command.ejercicio = ptu.ejercicio
        command.ptuAnterior = ptuAnterior.monto
        command.ptuActual = ptu.monto
        command.ptu = ptu
        
        [bonoAnualInstance: command]
    }

    def save() {
        Ptu ptu = Ptu.get(params.ptu)
        BigDecimal monto = params.double('monto') as BigDecimal
        BigDecimal porcentaje = params.double('porcentaje') as BigDecimal
        bonoService.generarBonos(ptu, monto, porcentaje)

        flash: 'Bonos generado exsitosamente '
        // println "Generando bono ${ptu.ejercicio} PTU: ${ptu.id} Monto: ${monto} % ${porcentaje}"
        redirect action:'index'
    }

    def actualizar(){
        Integer ejercicio = session.ejercicio 
    	bonoService.actualizarImportes(ejercicio)
    	redirect action:'index'
    }



    def reporteBonoAnual(){
        params['EJERCICIO'] = session.ejercicio
        params.reportName = 'BonoAnual'
        ByteArrayOutputStream  pdfStream = runReport(params)
        render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:params.reportName)
    }

    private runReport(Map repParams){
        log.info 'Ejecutando reporte  '+repParams
        def nombre=repParams.reportName
        def reportDef=new JasperReportDef(
            name:nombre
            ,fileFormat:JasperExportFormat.PDF_FORMAT
            ,parameters:repParams
            )
        ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
        return pdfStream
        
    }

    def eliminar(){
        def ejercicio = session.ejercicio
        def res = Aguinaldo.executeUpdate("delete from Aguinaldo a where a.ejercicio = ? ",[ejercicio])
        flash.message = "${res} Registros de aguinaldo del ejercicio ${ejercicio} eliminados"
        redirect action:'index'
    }


}


@Validateable
class BonoAnualCommand{

    Integer id = 0
    
    Integer ejercicio
    BigDecimal ptuAnterior
    BigDecimal porcentaje = 0.0
    BigDecimal incremento = 0.0
    BigDecimal ptuAnteriorActualizada = 0.0
    BigDecimal ptuActual = 0.0
    BigDecimal monto = 0.0
    Ptu ptu
    
    static constraints={
        ejercicio inList:2015..2025
    }
}