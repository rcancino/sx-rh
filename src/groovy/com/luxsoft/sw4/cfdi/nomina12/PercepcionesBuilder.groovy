package com.luxsoft.sw4.cfdi.nomina12

import mx.gob.sat.nomina12.NominaDocument.Nomina
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones.Percepcion
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones.Percepcion.HorasExtra
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoPercepcion
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoHoras

// import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones.Percepcion.HorasExtra.CTipoHoras

import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.rh.TiempoExtra

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
		  		String d = nominaEmpleado.empleado.rfc=="AAIJ4009248G4" ? "${it.concepto.descripcion}" : "${it.concepto.descripcion} ${nominaEmpleado.nomina.calendarioDet.mes.toUpperCase()} ${it.parent.nomina.ejercicio}"
		  		pp.setConcepto(d)
		  	}
		  	pp.setImporteGravado(it.importeGravado)
		  	pp.setImporteExento(it.importeExcento)

		  	// Horas extras
		  	if( it.concepto.clave == 'P022' || it.concepto.clave == 'P023') {
		  		def dias = nominaEmpleado.asistencia.partidas.count {det -> det.pagarTiempoExtra}
		  			
		  		
		  		def te = TiempoExtra.where {asistencia == nominaEmpleado.asistencia}.find()
		  		
		  		// Horas dobles
		  		def doblesTotal = te.getDoblesExcentos() + te.getDoblesGravados()
		  		if(doblesTotal > 0) {
		  			def minutosDobles = te.partidas.sum 0, { it.minutosDobles }
		  			def doblesTotalHoras = Math.floor(minutosDobles /60)
		  			
		  			HorasExtra heDobles = pp.addNewHorasExtra()
		  			heDobles.dias = dias
		  			heDobles.tipoHoras = CTipoHoras.Enum.forString('01')
		  			heDobles.horasExtra = doblesTotalHoras
		  			heDobles.importePagado = doblesTotal	
		  		}

		  		// Horas triples
		  		def triplesTotal = te.getTriplesGravados()
		  		if( triplesTotal > 0 ) {
		  			def minutosTriples = te.partidas.sum 0, { it.minutosTriples }
		  			def triplesTotalHoras = Math.floor(minutosTriples /60)
		  			
		  			HorasExtra heTriples = pp.addNewHorasExtra()
		  			heTriples.dias = dias
		  			heTriples.tipoHoras = CTipoHoras.Enum.forString('02')
		  			heTriples.horasExtra = triplesTotalHoras
		  			heTriples.importePagado = triplesTotal
		  		}
			}

		}
		if( nominaEmpleado.nomina.tipo == 'LIQUIDACION' ) {
			per.totalSeparacionIndemnizacion = per.totalGravado + per.totalExento
		}

		

		return per
	}


}