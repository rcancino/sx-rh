package com.luxsoft.sw4.rh.finiquito

import com.luxsoft.sw4.rh.*

class IndeminzacionBuilder {

	def build(Finiquito finiquito){
		
		finiquito.with{
        
            def montoIntereses = 10000.0  //**** Agrgar al bean  **//

            def tasaInteres = 10.0 //**** Agrgar al bean  **//

            indemnizacionIntereses = montoIntereses * tasaInteres / 100.0



            def sdiLiq = sdiOpcion ? salarioDiarioIntegradoLiq : salarioDiarioIntegrado

            indemnizacion20DiasPorAnio = sdiLiq * 20 * anosTrabajados

            indemnizacion3MesesDeSueldo = sdiLiq * 90

            indemnizacionPrimaDeAntiguedad=0.0

            indemnizacionExenta=0.0
    
            indemnizacionGravada=0.0
    
            primaDeAntiguedadExenta=0.0
    
            //primaDeAntiguedadGravada=0.0
        }

        return finiquito
	}
}