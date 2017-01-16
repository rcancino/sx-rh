package com.luxsoft.sw4.rh.finiquito

import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.ConceptoDeNomina

import com.luxsoft.sw4.rh.*

class IndeminzacionBuilder {

	def build(Finiquito finiquito){
		ZonaEconomica smg = ZonaEconomica.where {ejercicio == Periodo.obtenerYear(finiquito.empleado.baja.fecha) && clave == 'A'}.find()
		
		finiquito.with{
        
            def montoIntereses = 10000.0  //**** Agrgar al bean  **//

            def tasaInteres = 10.0 //**** Agrgar al bean  **//

            def dias20PorAnio = 20

            def dias3MesesSueldo = 90

            def tope2Veces = smg.salario * 2

            def diasAnioPorAntiguedad = 12

            indemnizacionIntereses = montoIntereses * tasaInteres / 100.0



            def sdiLiq = sdiOpcion ? salarioDiarioIntegradoLiq : salarioDiarioIntegrado

            indemnizacion20DiasPorAnio = sdiLiq * dias20PorAnio * anosTrabajados

            indemnizacion3MesesDeSueldo = sdiLiq * dias3MesesSueldo

            indemnizacionPrimaDeAntiguedad = tope2Veces * diasAnioPorAntiguedad * anosTrabajados

            def totalIndemnizacion = indemnizacion20DiasPorAnio + indemnizacion3MesesDeSueldo + indemnizacionPrimaDeAntiguedad       

            indemnizacionExenta = totalIndemnizacion > (smg.salario * anosTrabajados * 90) ?  (smg.salario * anosTrabajados * 90) : totalIndemnizacion
    
            indemnizacionGravada = totalIndemnizacion - indemnizacionExenta 
    
            primaDeAntiguedadExenta = indemnizacionPrimaDeAntiguedad
    
            //primaDeAntiguedadGravada=0.0
            
        }

        return finiquito
	}
}