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
import com.luxsoft.sw4.rh.NominaPorEmpleado;

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
		println 'A pagar: ' + nominaEmpleado.nomina.pago
		def empleado = nominaEmpleado.empleado
		//Complemento nomina
		NominaDocument nominaDocto=NominaDocument.Factory.newInstance()
		Nomina nomina=nominaDocto.addNewNomina()
		nomina.with {
			version = '1.2'
			tipoNomina = CTipoNomina.O
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
			setNumDiasPagados(diasLab)
		}
		nomina.fechaPago = toISO8601(nominaEmpleado.nomina.pago)
		nomina.fechaInicialPago = toISO8601(nominaEmpleado.nomina.periodo.fechaInicial)
		nomina.fechaFinalPago = toISO8601(nominaEmpleado.nomina.periodo.fechaFinal)
		
		Emisor emisor = nomina.addNewEmisor()
		emisor.setRegistroPatronal(empresa.registroPatronal)

		Nomina.Receptor receptor = nomina.addNewReceptor()
        receptor.with{
        	curp = empleado.curp
        	numSeguridadSocial = empleado.seguridadSocial.numero
            antigüedad = "P${nominaEmpleado.antiguedad}W"
            tipoRegimen = CTipoRegimen.Enum.forString(empleado.perfil.regimenContratacion.clave)
            numEmpleado = empleado.perfil.numeroDeTrabajador
            setDepartamento(empleado.perfil.departamento.clave)
            puesto = empleado.perfil.puesto?.clave
            
        }
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

		
		// Percepciones
		Percepciones per=nomina.addNewPercepciones()
		per.totalGravado=nominaEmpleado.percepcionesGravadas
		per.totalExento=nominaEmpleado.percepcionesExcentas
		nominaEmpleado.conceptos.each{
			if(it.concepto.tipo=='PERCEPCION') {
			  Percepcion pp=per.addNewPercepcion()
			  //pp.setTipoPercepcion()
			  pp.setTipoPercepcion(CTipoPercepcion.Enum.forString(StringUtils.leftPad(it.concepto.claveSat.toString(), 3, '0')))
			  pp.setClave(it.concepto.clave)
			  pp.setConcepto(it.concepto.descripcion)
			  pp.setImporteGravado(it.importeGravado)
			  pp.setImporteExento(it.importeExcento)
			}
		  }
		
		//Deducciones
		/*
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
		*/
		
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
}