package com.luxsoft.sw4.cfdi.nomina12

import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.Empresa
import java.text.SimpleDateFormat
import java.math.RoundingMode

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject
import org.apache.xmlbeans.XmlOptions
import org.apache.xmlbeans.XmlValidationError
import org.apache.xmlbeans.XmlDateTime
import org.apache.xmlbeans.XmlDate

import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoNomina
import mx.gob.sat.sitioInternet.cfd.catalogos.CEstado
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoNomina
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoRegimen
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoJornada
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoContrato
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CRiesgoPuesto
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CPeriodicidadPago
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoPercepcion
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CBanco

import mx.gob.sat.nomina12.NominaDocument
import mx.gob.sat.nomina12.NominaDocument.Nomina
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones.Percepcion
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones.SeparacionIndemnizacion

import com.luxsoft.sw4.rh.Finiquito

import com.luxsoft.sw4.rh.NominaPorEmpleado

class SeparacionIndeminzacionBuilder {

	def build(Nomina nomina, NominaPorEmpleado nominaEmpleado){
		if(nominaEmpleado.nomina.tipo == 'LIQUIDACION') {
			Finiquito finiquito = Finiquito.where {neLiquidacion == nominaEmpleado}.find()
			assert finiquito, 'No se ha registrado la enitdad Finiquito para la nomina de empleado:' + nominaEmpleado.id 
			def ingresoGravadoPorIndeminacion = nominaEmpleado.getPercepcionesGravadas()
			//def ultimoSueldoMensualOrdinario = ingresoGravadoPorIndeminacion<= 0.0 ? 0.0: finiquito.sueldo
			def ultimoSueldoMensualOrdinario = nominaEmpleado.empleado.salario.salarioDiario * 30

			def ingresoAcumulable = ingresoGravadoPorIndeminacion > ultimoSueldoMensualOrdinario ? ultimoSueldoMensualOrdinario : ingresoGravadoPorIndeminacion
			def ingresoNoAcumulable = ingresoGravadoPorIndeminacion - ultimoSueldoMensualOrdinario
			if(ingresoNoAcumulable < 0 ) ingresoNoAcumulable = 0.0

			
			
			SeparacionIndemnizacion separacion = nomina.percepciones.addNewSeparacionIndemnizacion()
			separacion.setUltimoSueldoMensOrd(ultimoSueldoMensualOrdinario)
        	separacion.setIngresoAcumulable(ingresoAcumulable)
        	separacion.setIngresoNoAcumulable(ingresoNoAcumulable )
        	separacion.setNumAñosServicio(finiquito.anosTrabajados)
        	separacion.setTotalPagado(nominaEmpleado.total)
        	
		}
	}

}