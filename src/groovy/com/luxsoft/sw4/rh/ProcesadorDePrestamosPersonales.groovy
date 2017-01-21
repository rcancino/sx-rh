package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

import com.luxsoft.sw4.rh.tablas.ZonaEconomica

class ProcesadorDePrestamosPersonales {
	
	def conceptoClave='D004'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
			assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"
		}
		
		//Buscando un prestamo vigente
		def prestamo = buscarPrestamo(ne)
		if(prestamo) {

			if(ne.finiquito == false && !ne.nomina.tipo == 'LIQUIDACION') {
				log.info "Aplicando decucccon para prestamo vigente: ${prestamo}"
				
				def percepciones=getPercepciones(ne)
				def deducciones=getRetencionesPrecedentes(ne)
				
				def salarioMinimo=ZonaEconomica.findByClaveAndEjercicio('A',ne.nomina.ejercicio).salario
				
				def diasNetos=ne.diasDelPeriodo-ne.incapacidades-ne.faltas
				def retMaxima=percepciones-deducciones-(salarioMinimo*diasNetos)
				retMaxima*=0.3
				
				def otrasDeducciones=ne.conceptos.find {it.concepto.clave=="D005"}
				if(otrasDeducciones){
					retMaxima-=otrasDeducciones.getTotal()
					if(retMaxima<0)
						retMaxima=0
				}
				if(prestamo.importeFijo>0){
					retMaxima=prestamo.importeFijo
				}
				
				if(retMaxima){
					def saldo=prestamo.saldo
					def importeExcento = retMaxima <= saldo ? retMaxima: saldo
					//Localizar el concepto
					def neDet = new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
					log.info "Deduccion calculada de: ${importeExcento}"
					neDet.importeGravado = 0
					neDet.importeExcento = importeExcento.setScale(2,RoundingMode.HALF_EVEN)
					ne.addToConceptos(neDet)
					ne.actualizar()
				}	
			} else {

				def percepciones = getPercepciones(ne)
				def deducciones = getRetencionesPrecedentesFiniquito(ne)
				def saldo = prestamo.saldo
				def disponible = percepciones - deducciones 
				def otrasDeducciones=ne.conceptos.find {it.concepto.clave=="D005"}
				
				if(otrasDeducciones) {
					disponible -= otrasDeducciones.getTotal()
				}
				if(ne.nomina.tipo == 'LIQUIDACION'){
					def abono = getAbonoPorFiniquito(ne)
					println 'Abono por finiquito localizado: ' + abono
					saldo -= abono
				}

				def importe = disponible < saldo ? disponible : saldo
				if(importe){
					def neDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
					
					neDet.importeGravado=0
					neDet.importeExcento = importe.setScale(2,RoundingMode.HALF_EVEN)
					ne.addToConceptos(neDet)
					ne.actualizar()	
				}
				
			}
			
			
			
		}
		
	}
	
	private Prestamo buscarPrestamo(NominaPorEmpleado ne) {
		def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>0.0 and p.empleado=? order by p.id  asc"
			,[ne.empleado],[max:1])
		return prestamos?prestamos[0]:null
	}
	
	private BigDecimal getPercepciones(NominaPorEmpleado ne){
		def invalidas=['P019','P021','P033']
		def res=0.0
		ne.conceptos.each{
			if(it.concepto.tipo=='PERCEPCION'){
				if(!invalidas.contains(it.concepto.clave) ){
					res+=it.getTotal()
				}
			}
		}
		return res;
	}

	private BigDecimal getDeducciones(){
		
	}
	
	private BigDecimal getRetencionesPrecedentes(NominaPorEmpleado ne) {
		
		def acu=0.0
		ne.conceptos.each{
			if(it.concepto.clave=='D012'){
				acu+=it.getTotal()
			}
		}
		return acu;
	}
	
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def prestamo=buscarPrestamo(ne)
		//def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
		def salarioMinimo=ZonaEconomica.findByClaveAndEjercicio("A",det.nomina.ejercicio).salario
		def retMaxima=( (ne.salarioDiarioBase-salarioMinimo)*(ne.diasDelPeriodo-ne.incapacidades-ne.faltas) )*0.3
		def otrasDeducciones=ne.conceptos.find {it.concepto.clave=="D005"}
		def model=[prestamo:prestamo
			,salarioDiario:ne.salarioDiarioBase
			,salarioMinimo:salarioMinimo
			,diasTrabajados:(ne.diasDelPeriodo-ne.incapacidades-ne.faltas)
			,factor:0.3
			,tope:retMaxima
			,otras:otrasDeducciones?.total
			,total:det.getTotal()
			]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionPrestamoPersonal"
	}
	
	String toString() {
		"Procesador de Prestamo personal "
	}

	private BigDecimal getRetencionesPrecedentesFiniquito(NominaPorEmpleado ne) {
		
		def INVALIDAS = ['D004','D005']
		def importe = ne.conceptos.sum 0.0 ,{
			if(it.concepto.tipo == 'DEDUCCION' && !INVALIDAS.contains(it.concepto.clave) ) {
				return it.getTotal()
			}
			return 0.0
		}
		return importe
	}

	private BigDecimal getAbonoPorFiniquito(NominaPorEmpleado ne) {
		def finiquito = Finiquito.where {neLiquidacion == ne }.find()
		assert finiquito, 'No existe el finiquito para esta liquidacion'
		assert finiquito.neFiniquito, 'No existe la nomina de finiquito para esta liquidacion'
		def abono = finiquito.neFiniquito.conceptos.find {it.concepto.clave == 'D004'}.find() 
		//assert abono , 'No se registro la deduccion por prestamo en el finiquito'
 		return abono ? abono.importeExcento : 0.0
	}

}
