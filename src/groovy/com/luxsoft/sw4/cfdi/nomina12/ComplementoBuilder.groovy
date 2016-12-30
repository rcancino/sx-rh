package com.luxsoft.sw4.cfdi.nomina12

import java.math.RoundingMode
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject
import org.apache.xmlbeans.XmlOptions
import org.apache.xmlbeans.XmlValidationError
import org.bouncycastle.util.encoders.Base64
import org.apache.xmlbeans.XmlDateTime
import org.apache.xmlbeans.XmlDate
import java.text.SimpleDateFormat

import com.luxsoft.sw4.rh.NominaPorEmpleado


import mx.gob.sat.nomina12.NominaDocument
import mx.gob.sat.nomina12.NominaDocument.Nomina
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoNomina

class ComplementoBuilder {

	

	Nomina build(NominaPorEmpleado nominaEmpleado){

		NominaDocument nominaDocto=NominaDocument.Factory.newInstance()
		Nomina nomina=nominaDocto.addNewNomina()
		nomina.version = '1.2'
		nomina.tipoNomina = CTipoNomina.O
		nomina.setNumDiasPagados(calcularDiasPagados(nominaEmpleado))
		nomina.fechaPago = NominaUtils.toISO8601(nominaEmpleado.nomina.pago)
		nomina.fechaInicialPago = NominaUtils.toISO8601(nominaEmpleado.nomina.periodo.fechaInicial)
		nomina.fechaFinalPago = NominaUtils.toISO8601(nominaEmpleado.nomina.periodo.fechaFinal)
		
		def totalPercepciones = nominaEmpleado.conceptos.sum 0, { 
			if(it.concepto.catalogoSat == 'c_TipoPercepcion') 
				return it.total 
			return 0
		}
		if(totalPercepciones > 0){
			nomina.setTotalPercepciones(totalPercepciones)
		}
		
		def otrosPagos = nominaEmpleado.conceptos.sum 0, { 
			if(it.concepto.catalogoSat == 'c_TipoOtroPago') 
				return it.total 
			return 0
		}

		if (otrosPagos > 0 ){
			nomina.setTotalOtrosPagos(otrosPagos)
		}

		def totalDeducciones = nominaEmpleado.conceptos.sum 0, { 
			if(it.concepto.catalogoSat == 'c_TipoDeduccion') 
				return it.total 
			return 0
		}
		if(totalDeducciones > 0 ){
			nomina.setTotalDeducciones(totalDeducciones)
		}
		return nomina
	}

	private int calcularDiasPagados(NominaPorEmpleado nominaEmpleado){
		def diasTrabajados=0
		def faltas=0
		if(nominaEmpleado.asistencia){
			if(!nominaEmpleado.empleado.controlDeAsistencia){
				diasTrabajados = nominaEmpleado.diasTrabajados + nominaEmpleado.vacaciones - (nominaEmpleado.asistencia.faltasManuales+(nominaEmpleado.asistencia.faltasManuales * 0.167)+ nominaEmpleado.incapacidades)
			}else{
				if(nominaEmpleado.empleado.alta<=nominaEmpleado.asistencia.calendarioDet.inicio){
				  diasTrabajados=nominaEmpleado.diasTrabajados+nominaEmpleado.vacaciones				 
			  	}else{
			  		diasTrabajados=nominaEmpleado.diasTrabajados+nominaEmpleado.vacaciones+nominaEmpleado.asistencia.paternidad 
			  	}
			}		
 		}
		def diasLab=new BigDecimal(diasTrabajados).setScale(3, RoundingMode.HALF_EVEN)
		return diasLab
	}

	
}