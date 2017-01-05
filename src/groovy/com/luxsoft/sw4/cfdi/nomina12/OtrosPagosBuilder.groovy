package com.luxsoft.sw4.cfdi.nomina12

import mx.gob.sat.nomina12.NominaDocument.Nomina
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones
import mx.gob.sat.nomina12.NominaDocument.Nomina.Percepciones.Percepcion

import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoOtroPago

import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.rh.CalculoAnual

class OtrosPagosBuilder {

	def build(Nomina nomina, NominaPorEmpleado nominaEmpleado){
		
		def percepciones = nominaEmpleado.conceptos.findAll{it.concepto.catalogoSat == 'c_TipoOtroPago'}
		if(!percepciones) return null
		Nomina.OtrosPagos otrosPagos = nomina.addNewOtrosPagos()
		percepciones.each{
			Nomina.OtrosPagos.OtroPago otroPago = otrosPagos.addNewOtroPago()
		  	def clave = it.concepto.catalogoSatClave
		  	otroPago.setTipoOtroPago(CTipoOtroPago.Enum.forString(clave))
		  	otroPago.setClave(it.concepto.clave)  // Debe ser la clave que se usa en contabilidad que sea la que se usa en contabilidad
		  	otroPago.setConcepto(it.concepto.descripcion)
		  	otroPago.setImporte(it.total)
		  	if(clave == '002'){
		  		Nomina.OtrosPagos.OtroPago.SubsidioAlEmpleo subsidioAlEmpleo = otroPago.addNewSubsidioAlEmpleo()
        		subsidioAlEmpleo.setSubsidioCausado(it.total)
		  	}
		  	if(clave == '004') {
		  		def eje = 2016
		  		CalculoAnual ca = CalculoAnual.where {ejercicio == eje && empleado == nominaEmpleado.empleado}.find()
		  		assert ca, 'Debe existir el calculo anual para poder usar el concepto 004'
		  		Nomina.OtrosPagos.OtroPago.CompensacionSaldosAFavor compensacionSaldosAFavor  = otroPago.addNewCompensacionSaldosAFavor()
		  		compensacionSaldosAFavor.setAño((short)eje)
        		compensacionSaldosAFavor.setSaldoAFavor(ca.resultado)
        		def remanente = ca.resultado - ca.aplicado - it.total
        		//assert remanente >=0 , 'El remanente no es correcto'
        		if(remanente < 0)
        			remanente = 0
        		compensacionSaldosAFavor.setRemanenteSalFav(remanente)
		  	}
		}
		return otrosPagos
	}

}