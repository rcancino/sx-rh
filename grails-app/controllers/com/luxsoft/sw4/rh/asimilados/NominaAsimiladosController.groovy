package com.luxsoft.sw4.rh.asimilados

import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON

import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.Nomina
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.rh.NominaPorEmpleadoDet
import com.luxsoft.sw4.rh.CalendarioDet
import com.luxsoft.sw4.rh.ProcesadorDeISTPMensual
import com.luxsoft.sw4.cfdi.Cfdi


@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class NominaAsimiladosController {

	def nominaAsimiladosService

	def cfdiService

    def index(Integer max) {
        params.max = 1000  //Math.min(max ?: 60, 100)
		params.sort = params.sort?:'lastUpdated'
		params.order = 'desc'
		def query = Nomina.where{ tipo == 'ASIMILADOS'}
		[nominaInstanceList:query.list(params) ,nominaInstanceListTotal:query.count(params)]
    }

	@Transactional
	def generar(Long calendarioDet){
		
		def nominaInstance = nominaAsimiladosService.generar(calendarioDet,params.formaDePago)
		
		flash.message = "Nómina para asimilados generada ${nominaInstance.id}"
		redirect action: 'index'
		
	}

	def show(Nomina nominaInstance) {
		respond nominaInstance
    }

	def getCalendariosDisponibles() {

		def term = params.term.trim() + '%'
		def list = CalendarioDet.where{ calendario.comentario == 'ASIMILADOS' }.list([sort:'id',order:'desc', max:1])
		
		list=list.collect{ calDet->
			def nombre = "$calDet.calendario.tipo $calDet.folio  ($calDet.calendario.ejercicio $calDet.calendario.comentario )"
			[id:calDet.id
				,label: nombre
				,value: nombre
			]
		}
		def res = list as JSON
		render res
	}

	def createNominaPorEmpleado(Nomina nomina){
		def asimilados = Empleado.where {asimilado == true}.list()
		[command: new AltaDeAsimiladoCommand(), nomina:nomina,asimilados:asimilados]
	}

	@Transactional
	def saveNominaDeAsimilado(AltaDeAsimiladoCommand command){
        if (command.hasErrors()) {
            render view:'createNominaPorEmpleado', model:[command: command, nomina: command.nomina, asimilados: Empleado.where {asimilado == true}.list()]
            return
        }
        nominaAsimiladosService.agregarNominaPorAsimilado(command.nomina, command.asimilado, command.importe) 

        //asistenciaImssDetInstance.save flush:true

        flash.message = "Nomina de asimilado generada "
        redirect action: 'show', id: command.nomina.id
	}

	@Transactional
	def eliminarNominaPorAsimilado(NominaPorEmpleado ne) {
		if(ne == null){
			flash.message = "Nomina por asimilado no puede ser nula"
			redirect action:'index'
			return
		}
		def nomina = ne.nomina
		nomina.removeFromPartidas(ne)
		nomina.save flush:true
		flash.nessage="Nomina por asimilado ${ne.id} eliminada"
		render view: 'show', model:[nominaInstance: nomina]
	}

	@Transactional
	def actualizarNominaPorAsimilado(NominaPorEmpleado ne) {
		ne = nominaAsimiladosService.actualizarNominaPorAsimilado(ne)
		flash.message = "Nomina por asimilado actualizada"
		redirect action: 'nominaPorAsimiladoEdit', id: ne.id
	}

	def nominaPorAsimiladoEdit(NominaPorEmpleado ne){
		[nominaPorEmpleadoInstance: ne]
	}

	def informacionDeConcepto(Long id) {
		def  neDet = NominaPorEmpleadoDet.get(id)
		def ruleModel = new ProcesadorDeISTPMensual()

		if(ruleModel) {
			render template:ruleModel.getTemplate(),model:ruleModel.getModel(neDet)
		}else {
			render {div("PENDIENTE DE IMPLEMENTAR")}
		}
	}

	@Transactional
	def generarCfdi(NominaPorEmpleado ne) {
		def cfdi = cfdiService.generarCfdi(ne)
		flash.message = "Cfdi ${cfdi.id} generado para nomina por empleado: ${ne.id} "
		redirect action:'nominaPorAsimiladoEdit', id: ne.id
	}
	
	@Transactional
	def timbrar(NominaPorEmpleado ne){
		cfdiService.timbrar(ne)
		flash.message=" Nomina de empleado ${ne.empleado} timbrara exitosamente"
		redirect action:'nominaPorAsimiladoEdit', id: ne.id
	}

	def descargarXml(Cfdi cfdi){
		response.setContentType("application/octet-stream")
		response.setHeader("Content-disposition", "filename=${cfdi.serie}_${cfdi.folio}_${cfdi.receptor}")
		response.outputStream << cfdi.xml
		return
	}

	def mostrarXml(Cfdi cfdi){
	
		render(text: cfdi.comprobanteDocument.xmlText(), contentType: "text/xml", encoding: "UTF-8")
	}
}

import groovy.transform.ToString
//import groovy.transform.EqualsAndHashCode

@ToString(includeNames=true,includePackage=false)
//@EqualsAndHashCode(includes='ejercicio,tipo')
class AltaDeAsimiladoCommand {

	Nomina nomina
	Empleado asimilado
	BigDecimal importe 
	String comentario


	static constraints = {
    	comentario nullable: true
    }
}
