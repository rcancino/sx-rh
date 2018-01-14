package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

//import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.tablas.*
import com.luxsoft.sw4.rh.acu.*

class ProcesadorDeISTPLiquidacion {
	
	def conceptoClave='D002'
	  
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado nominaEmpleado) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}

		def finiquito = Finiquito.where {empleado == nominaEmpleado.empleado }.find()
		assert finiquito, 'No existe entidad de finiquito para '+ nominaEmpleado.empleado
		def nominaPorEmpleadoDet = new NominaPorEmpleadoDet(
			concepto:concepto,
			importeGravado:0.0,
			importeExcento:0.0,
			comentario:'LIQUIDACION'
			)
		nominaPorEmpleadoDet.importeExcento = finiquito.isr
		nominaPorEmpleadoDet.importeGravado = 0.0
		nominaEmpleado.addToConceptos(nominaPorEmpleadoDet)
		//nominaEmpleado.actualizar()
		
	}
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def nominaEmpleado=det.parent
		def model=[:]
		model.importeISTP=det.total
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionISTP"
	}
	
	
}
