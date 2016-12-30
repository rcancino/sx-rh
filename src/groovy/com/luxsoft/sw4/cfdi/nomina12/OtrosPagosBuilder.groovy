package com.luxsoft.sw4.cfdi.nomina12

import mx.gob.sat.nomina12.NominaDocument.Nomina

import com.luxsoft.sw4.rh.NominaPorEmpleado

class OtrosPagosBuilder {

	def build(Nomina nomina, NominaPorEmpleado nominaEmpleado){
		
		// Percepcion por subsidio en incapacidad....
		def percepcionIncapacidad = nominaEmpleado.conceptos.find{it.concepto.claveSat.toString().padLeft(3,'0') == '014'}
		
		
		
	}

}