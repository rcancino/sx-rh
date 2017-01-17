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

		def concepto = ConceptoDeNomina.findByClave('D002')

		def percepciones = finiquito.partidas.sum 0.0, { it.tipo == 'PERCEPCION' ? it.importeGravado: 0.0 }

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
		
		/*
		def calDet = nominaEmpleado.nomina.calendarioDet

		def dias = calDet.calendario.periodos.sum 0,{
			if(it.mes==calDet.mes){
				(it.fin-it.inicio)+1
			}else 0
		}
		*/
		
		def dias = 30

		def tarifa = TarifaIsr.obtenerTabla(ejercicio, 'MENSUAL', diasTrabajados, dias).find(){
			(percepciones > it.limiteInferior && percepciones <= it.limiteSuperior)
		}
		assert tarifa,"No encontro TarifaIsr para los parametros: Dias: ${diasTrabajados} Perc:${percepciones} Empleado: ${nominaEmpleado.empleado}"

		def subsidio = SubsidioEmpleo.obtenerTabla(diasTrabajados,ejercicio,dias)
			.find(){(percepciones > it.desde && percepciones <= it.hasta)}
		assert subsidio,'No existe registro en tabla de subsidio para el empleo'
		
		log.info 'Subsidio localizado: ' + subsidio

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
		det.tipo = 'DEDUCCION'
		det.concepto = concepto
		det.importeGravado = 0.0
		det.importeExcento = sub.abs()
		finiquito.addToPartidas(det)

        return finiquito
	}
}