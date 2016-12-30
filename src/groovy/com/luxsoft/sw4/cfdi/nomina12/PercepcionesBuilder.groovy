package com.luxsoft.sw4.cfdi.nomina12

import mx.gob.sat.nomina12.NominaDocument.Nomina
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones.Percepcion
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoPercepcion

import com.luxsoft.sw4.rh.NominaPorEmpleado

class PercepcionesBuilder {

	def build(Nomina nomina, NominaPorEmpleado nominaEmpleado){
		
		// Percepciones
		Percepciones per=nomina.addNewPercepciones()
		def percepciones = nominaEmpleado.conceptos.findAll{it.concepto.tipo == 'PERCEPCION'}
		
		per.totalSueldos = percepciones.sum 0, {
		    def clave = it.concepto.claveSat.toString().padLeft(3,'0')
		    if(NominaUtils.isSueldos(clave) ){
		       return it.importeGravado + it.importeExcento
		    }
		    return 0
		}
		per.totalGravado=nominaEmpleado.percepcionesGravadas
		per.totalExento=nominaEmpleado.percepcionesExcentas
		percepciones.each{
			Percepcion pp=per.addNewPercepcion()
		  	def clave = it.concepto.claveSat.toString().padLeft(3,'0')
		  	pp.setTipoPercepcion(CTipoPercepcion.Enum.forString(clave))
		  	pp.setClave(it.concepto.clave)  // Debe ser la clave que se usa en contabilidad que sea la que se usa en contabilidad
		  	pp.setConcepto(it.concepto.descripcion)
		  	pp.setImporteGravado(it.importeGravado)
		  	pp.setImporteExento(it.importeExcento)
		}
		return per
	}

}