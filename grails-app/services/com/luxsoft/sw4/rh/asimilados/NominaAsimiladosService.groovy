package com.luxsoft.sw4.rh.asimilados

import grails.transaction.Transactional

import com.luxsoft.sw4.rh.Nomina
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.rh.ConceptoDeNomina
import com.luxsoft.sw4.rh.NominaPorEmpleadoDet
import com.luxsoft.sw4.rh.CalendarioDet
import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.NominaException
import com.luxsoft.sw4.rh.ProcesadorDeISTPMensual

@Transactional
class NominaAsimiladosService {

    @Transactional
	def generar(Long calendarioDetId, String formaDePago){

		String tipo = 'ASIMILADOS'

		def calendarioDet=CalendarioDet.get(calendarioDetId)

		def periodicidad = getPeriodicidad(calendarioDet)

		log.info "Generando nomina $tipo $formaDePago $calendarioDet.calendario.tipo $calendarioDet.calendario.comentario $calendarioDet.folio"
		
		
		def periodo=calendarioDet.periodo()
		Empresa empresa=Empresa.first()
		
		def nomina=Nomina.find{calendarioDet==calendarioDet && tipo==tipo && formaDePago==formaDePago}
		if(nomina){
			throw new NominaException(message:'Nomina ya generada calendario: '+calendarioDet)
		}

		nomina=new Nomina(tipo:tipo,
			periodicidad:periodicidad,
			folio:calendarioDet.folio,
			status:"PENDIENTE",
			periodo:periodo,
			calendarioDet:calendarioDet,
			ejercicio:calendarioDet.calendario.ejercicio)
		nomina.pago=calendarioDet.fechaDePago
		nomina.diaDePago=calendarioDet.fechaDePago.format('EEEE')
		nomina.formaDePago=formaDePago
		nomina.empresa = empresa
		nomina.total = 0.0
		nomina.partidas = []
		//generarPartidas nomina
		nomina.save(failOnError:true)
		return nomina
		
	}

	@Transactional
	def agregarNominaPorAsimilado(Nomina nomina, Empleado empleado, BigDecimal importe) {
		NominaPorEmpleado ne=new NominaPorEmpleado(
			empleado:empleado,
			ubicacion:empleado?.perfil?.ubicacion,
			antiguedadEnSemanas:0,
			nomina:nomina,
			vacaciones:0,
			fraccionDescanso:0,
			orden:1
		)
		ne.antiguedadEnSemanas=ne.getAntiguedad()
		actualizarNominaPorAsimilado(ne, importe)

		nomina.addToPartidas(ne)
		nomina.save failOnError: true, flush: true
	}

	@Transactional
	def actualizarNominaPorAsimilado(NominaPorEmpleado ne){
		actualizarNominaPorAsimilado(ne, ne.getPercepciones())
	}

	@Transactional
	def actualizarNominaPorAsimilado(NominaPorEmpleado ne, BigDecimal percepcion){
		ne.conceptos.clear()

		// Percepcion
		ConceptoDeNomina concepto = ConceptoDeNomina.where {clave=='P036'}.find()
		def nominaPorEmpleadoDet=new NominaPorEmpleadoDet(
			concepto:concepto,
			importeGravado: percepcion,
			importeExcento:0.0,
			comentario:'PENDIENTE')
		ne.addToConceptos(nominaPorEmpleadoDet)

		// Deducciones
		new ProcesadorDeISTPMensual().procesar(ne)
		ne.save()
		return ne
	}


	private String getPeriodicidad(CalendarioDet calendarioDet){
		//tipo inList:['SEMANA','QUINCENA','MES','CATORCENA','BIMESTRE','ESPECIAL']
		//periodicidad inList:['SEMANAL','QUINCENAL','MENSUAL','ANUAL','ESPECIAL']
		switch(calendarioDet.calendario.tipo) {
			case 'SEMANA':
				return 'SEMANAL'
			case 'QUINCENA':
				return 'QUINCENAL'
			case 'MES':
				return 'MENSUAL'
			break
			default:
				return 'ESPECIAL'
			break
		}
	}
}
