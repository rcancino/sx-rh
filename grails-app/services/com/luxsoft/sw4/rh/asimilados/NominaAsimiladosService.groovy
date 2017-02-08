package com.luxsoft.sw4.rh.asimilados

import grails.transaction.Transactional

import com.luxsoft.sw4.rh.Nomina
import com.luxsoft.sw4.rh.CalendarioDet
import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.NominaException

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
