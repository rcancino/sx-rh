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
		def percepciones = nominaEmpleado.conceptos.findAll{it.concepto.catalogoSat == 'c_TipoPercepcion'}
		
		per.totalSueldos = percepciones.sum 0.0, {
		    def clave = it.concepto.claveSat.toString().padLeft(3,'0')
		    if(NominaUtils.isSueldos(clave) ){
		       return it.importeGravado + it.importeExcento
		    }
		    return 0.0
		    //return it.importeGravado + it.importeExcento
		}

		per.totalGravado = percepciones.sum 0.0, {
			def clave = it.concepto.claveSat.toString().padLeft(3,'0')
			/*if(NominaUtils.isSueldos(clave) ){
				return it.importeGravado
			}
			return 0.0*/
			return it.importeGravado
		}

		per.totalExento = percepciones.sum 0.0, {
			def clave = it.concepto.claveSat.toString().padLeft(3,'0')
			/*if(NominaUtils.isSueldos(clave) ){
				return it.importeExcento
			}
			return 0.0*/
			return it.importeExcento
			
		}
		percepciones.each{
			Percepcion pp=per.addNewPercepcion()
		  	//def clave = it.concepto.claveSat.toString().padLeft(3,'0')
		  	
		  	def clave = it.concepto.catalogoSatClave
		  	
		  	pp.setTipoPercepcion(CTipoPercepcion.Enum.forString(clave))
		  	pp.setClave(it.concepto.clave)  // Debe ser la clave que se usa en contabilidad que sea la que se usa en contabilidad
		  	pp.setConcepto(it.concepto.descripcion)
		  	if(nominaEmpleado.nomina.tipo == 'ASIMILADOS' && it.concepto.clave == 'P036'){
		  		String d = "${it.concepto.descripcion} ${nominaEmpleado.nomina.calendarioDet.mes.toUpperCase()} ${it.parent.nomina.ejercicio}"
		  		pp.setConcepto(d)
		  	}
		  	pp.setImporteGravado(it.importeGravado)
		  	pp.setImporteExento(it.importeExcento)
		}
		if( nominaEmpleado.nomina.tipo == 'LIQUIDACION' ) {
			per.totalSeparacionIndemnizacion = per.totalGravado + per.totalExento
		}
		return per
	}


}