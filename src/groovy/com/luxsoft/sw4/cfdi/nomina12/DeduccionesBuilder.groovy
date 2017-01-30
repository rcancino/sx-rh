package com.luxsoft.sw4.cfdi.nomina12

import mx.gob.sat.nomina12.NominaDocument.Nomina
import mx.gob.sat.nomina12.NominaDocument.Nomina.Deducciones
import mx.gob.sat.nomina12.NominaDocument.Nomina.Deducciones.Deduccion

import mx.gob.sat.sitioInternet.cfd.catalogos.nomina.CTipoDeduccion

import com.luxsoft.sw4.rh.NominaPorEmpleado

class DeduccionesBuilder {

	def build(Nomina nomina, NominaPorEmpleado nominaEmpleado){
		
		def deducciones = nominaEmpleado.conceptos.findAll{it.concepto.tipo == 'DEDUCCION'}
		Deducciones ded=nomina.addNewDeducciones()
		ded.totalOtrasDeducciones = getTotalOtras(deducciones)
		def isr = getTotalIsr(deducciones)
		if(isr){
			ded.totalImpuestosRetenidos = isr
		}
		
		deducciones.each{
			Deduccion dd=ded.addNewDeduccion()
			def clave = it.concepto.claveSat.toString().padLeft(3,'0')
			dd.setTipoDeduccion(CTipoDeduccion.Enum.forString(clave))
			dd.setClave(it.concepto.clave)
			dd.setConcepto(it.concepto.descripcion.replace('.',','))
			dd.setImporte(it.total)
		}
		return ded
	}

	def getTotalOtras(def deducciones){
		deducciones.sum 0, {
		    def clave = it.concepto.claveSat.toString().padLeft(3,'0')
		    if(clave != '002' ){  // Distinta a ISR
		       return it.total
		    }
		    return 0
		}
	}

	def getTotalIsr(def deducciones){
		deducciones.sum 0, {
		    def clave = it.concepto.claveSat.toString().padLeft(3,'0')
		    if(clave == '002' ){  // Distinta a ISR
		       return it.total
		    }
		    return 0
		}
	}

}