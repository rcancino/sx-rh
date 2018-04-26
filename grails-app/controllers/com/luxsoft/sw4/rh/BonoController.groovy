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

    /*def save(EmpleadoPorEjercicioCommand command){
        log.info 'Generando aguinaldo para '+command
        def aguinaldo=Aguinaldo.find{ejercicio==command.ejercicio && empleado==command.empleado}
        if(aguinaldo){
            flash.message="Agunaldo para $command.empleado para el ejercicio $command.ejercicio ya existe"
            render view:'create',model:[aguinaldoInstance:new EmpleadoPorEjercicioCommand(ejercicio:session.ejercicio)]
            return
        }
        aguinaldo=aguinaldoService.generar(command.empleado,command.ejercicio)
        flash:'Nuevo registro de aguinaldo generado '+aguinaldo.id
        redirect action:'index'
    }*/

    def edit(Aguinaldo a){
        [aguinaldoInstance:a]
    }

    def update(){
        
        def a = Aguinaldo.get(params.id)
        // [aguinaldoInstance:a]
        def bono = params['porcentajeBono'] as BigDecimal
        
        println 'Bono:' +params.porcentajeBono



        bindData(a,params,[exclude: ['id', 'version']])
        
        bono = bono/100
        a.porcentajeBono = bono

        //log.info 'Actualizando aguinaldo: '+params+ 'Bono: '+bono+ 'Manual: '+bono
        
        log.info 'Actualizando aguinaldo: '+a
        a=aguinaldoService.calcular(a)
        flash.message = "Aguinaldo ${a.id} actualizado"
        redirect action:'show',params:[id:a.id]
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
        bonoService.actualizarImportes(ptu.ejercicio)
        flash: 'Bonos generado exsitosamente '
        // println "Generando bono ${ptu.ejercicio} PTU: ${ptu.id} Monto: ${monto} % ${porcentaje}"
        redirect action:'index'
    }

    def actualizar(){
    	def ejercicio=session.ejercicio
    	aguinaldoService.calcular(ejercicio)
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