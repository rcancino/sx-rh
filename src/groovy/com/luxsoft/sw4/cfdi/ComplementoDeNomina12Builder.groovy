package com.luxsoft.sw4.cfdi

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

import com.luxsoft.sw4.cfdi.CFDIUtils
import com.luxsoft.sw4.cfdi.Cfdi
import com.luxsoft.sw4.cfdi.CfdiException
import com.luxsoft.sw4.cfdi.Folio
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.Empresa

import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Complemento

import mx.gob.sat.nomina12.NominaDocument
import mx.gob.sat.nomina12.NominaDocument.Nomina
import mx.gob.sat.nomina12.NominaDocument.Nomina.Emisor


import mx.gob.sat.nomina12.NominaDocument.Nomina.Deducciones
import mx.gob.sat.nomina12.NominaDocument.Nomina.Deducciones.Deduccion
import mx.gob.sat.nomina12.NominaDocument.Nomina.Incapacidades
import mx.gob.sat.nomina12.NominaDocument.Nomina.Incapacidades.Incapacidad
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones.Percepcion

import mx.gob.sat.sitioInternet.cfd.tipoDatos.tdCFDI.TFecha

import mx.gob.sat.sitioInternet.cfd.catalogos.CEstado
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoNomina
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoRegimen
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoJornada
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoContrato
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CRiesgoPuesto
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CPeriodicidadPago
import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoPercepcion

class ComplementoDeNomina12Builder {

	def generarComplemento(def empresa, def nominaEmpleado, def comprobante){
		

		Nomina nomina = buildNomina(nominaEmpleado)
		
		registrarEmisor nomina, empresa

		registrarReceptor nomina, nominaEmpleado
		
		registrarPercepciones(nomina, nominaEmpleado)

		//registrarDeducciones(nomina, nominaEmpleado)
		
        registrarComplemento(comprobante, nomina)
	}

	def buildNomina(NominaPorEmpleado nominaEmpleado){
		NominaDocument nominaDocto=NominaDocument.Factory.newInstance()
		Nomina nomina=nominaDocto.addNewNomina()
		nomina.version = '1.2'
		nomina.tipoNomina = CTipoNomina.O
		nomina.setNumDiasPagados(calcularDiasPagados(nominaEmpleado))
		nomina.fechaPago = toISO8601(nominaEmpleado.nomina.pago)
		nomina.fechaInicialPago = toISO8601(nominaEmpleado.nomina.periodo.fechaInicial)
		nomina.fechaFinalPago = toISO8601(nominaEmpleado.nomina.periodo.fechaFinal)
		return nomina
	}

	int calcularDiasPagados(NominaPorEmpleado nominaEmpleado){
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
        receptor.tipoRegimen = CTipoRegimen.Enum.forString(empleado.perfil.regimenContratacion.clave)
        receptor.numEmpleado = empleado.perfil.numeroDeTrabajador
        receptor.setDepartamento(empleado.perfil.departamento.clave)
        receptor.puesto = empleado.perfil.puesto?.clave
        receptor.setFechaInicioRelLaboral(toISO8601(empleado.alta))
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

	def registrarPercepciones(Nomina nomina, NominaPorEmpleado nominaEmpleado){
		// Percepciones
		Percepciones per=nomina.addNewPercepciones()
		def percepciones = nominaEmpleado.conceptos.findAll{it.concepto.tipo == 'PERCEPCION'}
		
		per.totalSueldos = percepciones.sum 0, {
		    def clave = it.concepto.claveSat.toString().padLeft(3,'0')
		    if(!OTRAS_PERCEPCIONES.contains(clave) ){
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
		  	pp.setClave(it.concepto.clave)
		  	pp.setConcepto(it.concepto.descripcion)
		  	pp.setImporteGravado(it.importeGravado)
		  	pp.setImporteExento(it.importeExcento)
		}
	}

	def registrarDeducciones(Nomina nomina, NominaPorEmpleado nominaEmpleado){
		//Deducciones
		
		if(nominaEmpleado.deducciones){
			Deducciones ded=nomina.addNewDeducciones()
			ded.totalGravado=nominaEmpleado.deduccionesGravadas
			ded.totalExento=nominaEmpleado.totalExcento
			
			nominaEmpleado.conceptos.each{
			  if(it.concepto.tipo=='DEDUCCION') {
				Deduccion dd=ded.addNewDeduccion()
				dd.setTipoDeduccion(StringUtils.leftPad(it.concepto.claveSat.toString(), 3, '0'))
				dd.setClave(it.concepto.clave)
				dd.setConcepto(it.concepto.descripcion)
				dd.setImporteGravado(it.importeGravado)
				dd.setImporteExento(it.importeExcento)
			  }
			}
		}
	}

	def registrarComplemento(Comprobante comprobante, Nomina nomina){
		Complemento complemento=comprobante.addNewComplemento()
        def cursor=complemento.newCursor()
        cursor.toEndToken()
        def cn=nomina.newCursor()
        cn.moveXml(cursor)
	}
	

	private Calendar toISO8601(Date fecha) {
		Calendar c=Calendar.getInstance();
		c.setTime(fecha)
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd")
		XmlDate xmlDate = XmlDate.Factory.newInstance()
		xmlDate.setStringValue(df.format(c.getTime()))
		return xmlDate.getCalendarValue()
	}

	/**
	 *  Claves de percepciones no acumulables a sueldos y salarios
	 */
	static OTRAS_PERCEPCIONES = [
		'022', // Prima por antiguedad
    	'023', // Pagos por separacion
		'025', // Indemnizaciones
		'039', // Jubilaciones, pensiones o haberes de retiro en una exhibicion
		'044'  // Jubilaciones, pensiones o haberes de retiro en parcialidades
	]
}