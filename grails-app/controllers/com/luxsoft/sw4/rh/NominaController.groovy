package com.luxsoft.sw4.rh


import com.luxsoft.sw4.Empresa;
import com.luxsoft.sw4.Periodo

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import grails.validation.Validateable
import groovy.transform.ToString
import grails.plugin.springsecurity.annotation.Secured

import org.grails.databinding.BindingFormat

import com.luxsoft.sw4.rh.acu.IsptMensual
import org.apache.commons.lang.exception.ExceptionUtils

//@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class NominaController {

	def nominaService

	// def cfdiService

	def cfdiV33Service
	def cfdiTimbradoService
    //static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	def importarNominaService
	
	def ajusteIsr

	def nominaPorEmpleadoService
	
	def calculoAnualService
    
    def index(Integer max) {
        params.max = 1000  //Math.min(max ?: 60, 100)
		params.periodicidad=params.periodicidad?:'QUINCENAL'
		params.sort=params.sort?:'lastUpdated'
		params.order='desc'
		def tipo=params.periodicidad=='SEMANAL'?'SEMANA':'QUINCENA'
		def ejercicio=session.ejercicio
		def periodos=CalendarioDet
			.findAll('from CalendarioDet d where d.calendario.tipo=? and d.calendario.ejercicio=?',[tipo,ejercicio])
		
		
		
		def query=Nomina.where{periodicidad==params.periodicidad && ejercicio==session.ejercicio}
		[nominaInstanceList:query.list(params)
			,nominaInstanceListTotal:query.count(params)
			,periodicidad:params.periodicidad,periodos:periodos
			,calendarioActual:tipo=='SEMANAL'?session.calendarioSemana:session.calendarioQuincena]
    }

    def show(Long id) {
		def nominaInstance=Nomina.get(id)
		Map partidasMap=nominaInstance.partidas.groupBy([{it.ubicacion.clave}])
		[nominaInstance:nominaInstance,partidasMap:partidasMap]
		        
    }

    def create(){
    	//respond new Nomina()
    }	
	

	def generar(Long calendarioDet){
		def tipo=params.tipo
		def periodicidad=params.periodicidad
		def formaDePago=params.formaDePago
		
		def nominaInstance=nominaService.generar(calendarioDet,tipo,formaDePago,periodicidad)
		
		nominaInstance.partidas.each{
			nominaService.actualizarCalculoAnual(nominaInstance.ejercicio,it.empleado)
			nominaService.actualizarVacaciones(nominaInstance.ejercicio,it.empleado)
		}

		redirect action:'actualizarPartidas',params:[id:nominaInstance.id]
		
	}
	
	def actualizarPartidas(Long id) {
		//def nomina=nominaService.actualizarPartidas(Nomina.get(id))

		def nomina =Nomina.get(id)

		if(nomina.tipo == 'AGUINALDO' ){
			nominaService.actualizarAguinaldo(nomina)
			flash.message="Nomina de Aguinaldo Folio: ${nomina.id} Tipo: $nomina.tipo actualizada"
			redirect action:'show',params:[id:id]
			return
		}

		if(nomina.tipo=='PTU'){
			nominaService.actualizarPtu(nomina)
			flash.message="Nomina de PTU actualizada"
			redirect action:'show',params:[id:id]
			return
		}
		if(nomina.tipo == 'LIQUIDACION') {
			nominaService.registrarLiquidacion(nomina)
			flash.message="Nomina de LIQUIDACION actualizada"
			redirect action:'show',params:[id:id]
			return
		}
		if(nomina.tipo == 'ESPECIAL'){
			nominaService.generarPartidasEspecial(nomina)
			nomina.partidas.each { ne ->
				nominaPorEmpleadoService.actualizarNominaEspecial(ne)
			}
			flash.message="Nomina Especial actualizada"
			redirect action:'show',params:[id:id]
			return
		}
		for(it in nomina.partidas){
			if(it.finiquito){
				log.info "Finiquito detectado para ${it.empleado}"
				continue
			}
			def ajuste = IsptMensual.find("from IsptMensual i where i.nominaPorEmpleado.id=?",[it.id])
			if(ajuste) {
				log.info "Ajuste menusal detectado para ${it.empleado}"
				continue
			}
			
			def ne = nominaPorEmpleadoService.actualizarNominaPorEmpleado(it.id)
			nominaPorEmpleadoService.depurarNominaPorEmpleado(ne.id)
				
			log.debug "Nomina actualizara para: ${ne.empleado}"
		}
		nominaService.depurar(id)
		flash.message="Actualizacion exitosa"
		redirect action:'show',params:[id:id]
	}

	def depurar(Long id){
		nominaService.depurar(id)
		redirect action:'show',params:[id:id]	
	}

    def delete(Nomina nominaInstance){
    	if(nominaInstance==null){
    		notFound()
    		return
    	}
    	nominaService.eliminarNomina(nominaInstance.id)
    	flash.message="Nomina ${nominaInstance.id} eliminada"
    	redirect action:'index',params:[periodicidad:nominaInstance.periodicidad]
    }
	
	

    private List findEmpleadosDisponibles(Long nominaId,String periodicidad){
        def res=Empleado.findAll(
            "from Empleado e where e.status ='ALTA' and e.salario.periodicidad=? and e.id not in(select ne.empleado.id from NominaPorEmpleado ne where ne.nomina.id=?) "
            ,[periodicidad,nominaId])
    }

    @Transactional
    def generarCfdis(Nomina nomina) {
    	nomina.partidas.each { ne ->
    		if(!ne.cfdi){
    			try {
    				cfdiV33Service.generar(ne)
    				nominaService.actualizarSaldos(ne)
    			}
    			catch(Exception e) {
    				String msg="Error generando CFDI para nomina de ${ne.empleado} id: $ne.id  " + ExceptionUtils.getMessage(e)
    				flash.mesage= msg
    				throw new RuntimeException(msg)
    				return
    			}
    		}
    	}
    	//nomina.status='CERRADA'
    	nomina = nominaService.save(nomina)
		//nomina = nomina.save flush:true
		//nominaService.actualizarSaldos(nomina)
		flash.message = "Comprobantes fiscales generados exitosamente "
		redirect action:'show',params:[id:nomina.id]
    }
	
	@NotTransactional
	def timbrar(Nomina nominaInstance) {
		def errores = []
		def pendientes = nominaInstance.partidas.findAll{ it?.cfdi?.uuid == null}

		pendientes.each{ ne ->
			try {
				// cfdiV33Service.timbrar(ne)
				cfdiTimbradoService.timbrar(ne.cfdi)
			}
			catch(Exception e) {
				def ccc = ExceptionUtils.getRootCouseMessage(e)
				println " Error timbrando ${ne.empleado} ${cc}"
				errores << " Error timbrando ${ne.empleado} ${cc}"
			}
		}

		if(errores) {
			flash.message = errores.join(',')
		} else {
			flash.message=" Nomina de ${nominaInstance.id} timbrara exitosamente"
		}
		redirect action:'show',params:[id:nominaInstance.id]
	}
	
	def actualizarSaldos(Nomina nominaInstance){
		
		nominaService.actualizarSaldos(nominaInstance)
		flash.message="Saldos actualizados"
		redirect action:'show',params:[id:nominaInstance.id]
	}
	
	def importar(ImportacionCmd cmd) {
		if(request.method=='GET') {
			render view:'importar',model:[importacionCmd:new ImportacionCmd()]
		}else {
			//Parsing parameters
			log.info 'Generando importacion para: '+cmd
			if(cmd.hasErrors()) {
				flash.message="Errores en parametros de importacion"
				render view:'importar',model:[importacionCmd:cmd]
			}else {
				
				//def file=request.getFile('archivo')
				//render 'Procesando importcion con archivo: '+file.getContentType()
				
				def periodo=new Periodo(cmd.fechaInicial,cmd.fechaFinal)
				if(cmd.tipo=='QUINCENAL') {
					def file=new ByteArrayInputStream(cmd.archivo)
					importarNominaService.importarQuincena(file,cmd.folio,periodo,cmd.fechaDePago,cmd.diaDePago,cmd.ejercicio)
					params.periodicidad='QUINCENAL'
					redirect action:'index',params:params
				}else if(cmd.tipo=='SEMANAL') {
					def file=new ByteArrayInputStream(cmd.archivo)
					importarNominaService.importarSemana(file,cmd.folio,periodo,cmd.fechaDePago,cmd.diaDePago,cmd.ejercicio)
					params.periodicidad='SEMANAL'
					redirect action:'index',params:params
				}
	
				
			}
			
		}
		
	}

    protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
					, args: [message(code: 'nomina.label', default: 'Nómina'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
	
	//@Transactional
	def ajusteMensualIsr(Long id){
		
		Nomina nomina=Nomina.get(id)
		nomina.partidas.each{ne->
			
			println "Calculando ajuste mensual para :"+ ne.empleado.nombre
			def found=IsptMensual.findByNominaPorEmpleado(ne)
			if(!found){
				println "Ajustando nomina"
				ajusteIsr.ajusteMensual(ne)
				nominaPorEmpleadoService.actualizarNominaPorEmpleado(ne.id)
			}else{
			  println "Nomina ya ajustada"
			}

		}
		redirect action:'show',params:[id:nomina.id]
	}
	
	@Transactional
	def eliminarAjusteMensualIsr(Long id){
		
		Nomina nomina=Nomina.get(id)
		nomina.partidas.each{ne ->
			if(ne.cfdi==null){
				def found=IsptMensual.findByNominaPorEmpleado(ne)
				if(found){
					log.info 'Eliminando ajuste mensual '+ne
					found.delete flush:true
					nominaPorEmpleadoService.actualizarNominaPorEmpleado(ne.id)
					
				}
			}
		}
		//nominaService.actualizarPartidas(Nomina.get(id))
		flash.message="Ajuste mensual ISTP eliminado"
		redirect action:'show',params:[id:nomina.id]
	}

	def aplicarCalculoAnual(Long id){
		
		Nomina nomina=Nomina.get(id)
		nomina.partidas.each{
			calculoAnualService.aplicar(it)
		}
		//nominaService.actualizarPartidas(Nomina.get(id))
		redirect action:'show',params:[id:nomina.id]
	}
	
	@Transactional
	def aplicarCalculoAnualConSaldo(Long id){
		Nomina nomina=Nomina.get(id)
		def ejercicio=nomina.ejercicio-1
		nomina.partidas.each{ne->
			
			def calculo=CalculoAnual.findByEjercicioAndEmpleado(ejercicio,ne.empleado)
			if(calculo && calculo.saldo && calculo.calculoAnual){
				calculoAnualService.aplicarCalculoAnualConSaldo(ne,calculo)
				
			}
		}
		flash.message="Aplicacion de saldos para el calculo anual "+ejercicio+ " generado"
		redirect action:'show',params:[id:nomina.id]
	}


	// def getNominasAsJSON() {
	// 	def term=params.term.trim()+'%'
	// 	def query=Nomina.where{
	// 		calendario.tipo=~term || folio.toString()=~term 
	// 	}
	// 	def list=query.list(max:100, sort:"folio",order:'desc')
	// 	//println query.count()
	// 	println list.size()
	// 	list=list.collect{ calDet->
	// 		def nombre="$calDet.calendario.tipo $calDet.folio  $calDet.calendario.ejercicio"
	// 		[id:calDet.id
	// 			,label:nombre
	// 			,value:nombre
	// 		]
	// 	}
	// 	def res=list as JSON
		
	// 	render res
	// }
    
}

@ToString(includeNames=true,includePackage=false)
class ImportacionCmd{
	
	byte[] archivo
	
	Integer folio
	
	Integer ejercicio
	
	String tipo
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaDePago
	
	String diaDePago
	
	static constraints = {
		tipo inList:['SEMANAL','QUINCENAL']
		diaDePago inList:['LUNES','MARTES','MIERCOLES','JUEVES','VIERNES']
		archivo nullable:false
		folio minSize:1
		ejercicio inList:[2015,2016,2017,2018,2019,2020]
	} 
}

@Validateable
class ReporteDeNominaCommand{
	
	String tipo
	Integer ejercicio
	Integer folio
	String periodicidad

	static constraints = {
		ejercicio inList:2014..2018
		tipo inList:['GENERAL','ESPECIAL','AGUINALDO','PTU']
		folio range:1..200
		periodicidad inList:['SEMANAL','QUINCENAL']
		
	}
	
}

