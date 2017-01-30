package com.luxsoft.sw4.rh.finiquito

import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.Periodo

import com.luxsoft.sw4.rh.*

import java.math.RoundingMode

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.tablas.*
import com.luxsoft.sw4.rh.acu.*


class ImpuestoBuilder {

	private static final log=LogFactory.getLog(this)

	def build(Finiquito finiquito){

		// Quitar las existentes.....
		def delete = finiquito.partidas.findAll {it.concepto.clave == 'D001' || it.concepto.clave == 'P021'}
		delete.each {
			finiquito.removeFromPartidas(it)
		}

		def concepto = ConceptoDeNomina.findByClave('D002')

		def percepciones = finiquito.partidas.sum 0.0, {
			if(it.tipo == 'PERCEPCION'){
				return it.importeGravado
			}
			return 0.0
		}

		def retardoPermiso = 0 //nominaEmpleado.conceptos.find{it.concepto.clave=='D012'}

		if(retardoPermiso){
			percepciones -= retardoPermiso.importeExcento
		}

		if(percepciones<=0)
			return
			
		def diasTrabajados = finiquito.diasPorPagar
		
		if(diasTrabajados <= 0)
			return
		
		def ejercicio = finiquito.baja.fecha.toYear()

		def tarifa = TarifaIsr.buscar(ejercicio, 'MENSUAL', percepciones)

		assert tarifa,"No encontro TarifaIsr para los parametros: Dias: ${diasTrabajados} Perc:${percepciones} Empleado: ${nominaEmpleado.empleado}"

		def subsidio = SubsidioEmpleo.buscar(ejercicio, percepciones)
		assert subsidio,'No existe registro en tabla de subsidio para el empleo'
		
		

		def importeGravado = percepciones - tarifa.limiteInferior
		importeGravado *= tarifa.porcentaje
		importeGravado /= 100
		importeGravado += tarifa.cuotaFija
		importeGravado = importeGravado.setScale(2,RoundingMode.HALF_EVEN)
		
		def sub = importeGravado - subsidio.subsidio

		FiniquitoDet det = new FiniquitoDet()
		if(sub < 0){
			concepto=ConceptoDeNomina.findByClave('P021')
		}
		det.tipo = concepto.tipo
		det.concepto = concepto
		det.importeGravado = 0.0
		det.importeExcento = sub.abs()
		finiquito.addToPartidas(det)
		log.info (" Impuest calculado: ${sub.abs()} Percepciones: ${percepciones} TarifaIsr: ${tarifa.limiteInferior} Subsidio: ${subsidio.subsidio}")
        return finiquito
	}
}