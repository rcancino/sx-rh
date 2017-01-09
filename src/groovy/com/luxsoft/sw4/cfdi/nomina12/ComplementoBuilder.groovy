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

import mx.gob.sat.nomina12.NominaDocument
import mx.gob.sat.nomina12.NominaDocument.Nomina
import mx.gob.sat.nomina12.NominaDocument.Nomina.Emisor




import com.luxsoft.sw4.rh.NominaPorEmpleado

class ComplementoBuilder {

	Nomina build(NominaPorEmpleado nominaEmpleado){

		validar(nominaEmpleado)

		NominaDocument nominaDocto=NominaDocument.Factory.newInstance()
		Nomina nomina=nominaDocto.addNewNomina()
		nomina.version = '1.2'
		nomina.tipoNomina = CTipoNomina.O
		nomina.setNumDiasPagados(calcularDiasPagados(nominaEmpleado))
		nomina.fechaPago = NominaUtils.toISO8601(nominaEmpleado.nomina.pago)
		nomina.fechaInicialPago = NominaUtils.toISO8601(nominaEmpleado.nomina.periodo.fechaInicial)
		nomina.fechaFinalPago = NominaUtils.toISO8601(nominaEmpleado.nomina.periodo.fechaFinal)
		registrarEmisor(nomina, nominaEmpleado.nomina.empresa)
		registrarReceptor(nomina, nominaEmpleado)
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

	def registrarEmisor(Nomina nomina, Empresa empresa) {
		Emisor emisor = nomina.addNewEmisor()
		emisor.setRegistroPatronal(empresa.registroPatronal)
	}

	def registrarReceptor(Nomina nomina, NominaPorEmpleado nominaEmpleado){
		Empleado empleado = nominaEmpleado.empleado
		Nomina.Receptor receptor = nomina.addNewReceptor()
        receptor.curp = empleado.curp
    	receptor.numSeguridadSocial = empleado.seguridadSocial.numero.replace('-','')
        receptor.antigüedad = "P${nominaEmpleado.antiguedad}W"
        def tr = empleado.perfil.regimenContratacion.clave.toString().padLeft(2,'0')
        receptor.tipoRegimen = CTipoRegimen.Enum.forString(tr) // TODOS LOS EMPLEADOS DEBEN TENER TIPO REGIMEN
        receptor.numEmpleado = empleado.perfil.numeroDeTrabajador
        receptor.setDepartamento(empleado.perfil.departamento.clave)
        receptor.puesto = empleado.perfil.puesto?.clave
        receptor.setFechaInicioRelLaboral(NominaUtils.toISO8601(empleado.alta))
        receptor.setTipoContrato(CTipoContrato.X_01)  // AJUSTAR EN LA APLICACION
        receptor.setTipoJornada(CTipoJornada.X_01)  /// AJUSTAR LA APLICACION
        receptor.setSindicalizado(Nomina.Receptor.Sindicalizado.NO)  // AJUSTAR EN LA APLICACION
        receptor.setClaveEntFed(CEstado.DIF) 
        
        if(empleado.perfil?.riesgoPuesto?.clave){
        	receptor.setRiesgoPuesto(CRiesgoPuesto.Enum.forInt(empleado.perfil?.riesgoPuesto?.clave))
        }
        
        if(nominaEmpleado.nomina.periodicidad == 'QUINCENAL')
        	receptor.setPeriodicidadPago(CPeriodicidadPago.X_04)
        else
        	receptor.setPeriodicidadPago(CPeriodicidadPago.X_02)
        if(empleado?.salario?.banco?.clave){
			receptor.setBanco(empleado.salario.banco.clave)
			//receptor.setCuentaBancaria('')
        }
		receptor.setSalarioBaseCotApor(nominaEmpleado.salarioDiarioBase)
		receptor.setSalarioDiarioIntegrado(nominaEmpleado.salarioDiarioIntegrado)
	}


	private validar(NominaPorEmpleado ne){
		ne.conceptos.each {
			assert it.concepto.catalogoSat, "No esta registrado el CatalogoSat para el concepto de nomina ${it}"
			assert it.concepto.catalogoSatClave, "No esta registrado la clave SAT  para el concepto de nomina ${it}"
		}
	}

	
}