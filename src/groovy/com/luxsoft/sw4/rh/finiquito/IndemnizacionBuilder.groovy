package com.luxsoft.sw4.rh.finiquito

import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.ConceptoDeNomina

import com.luxsoft.sw4.rh.*
import com.luxsoft.sw4.rh.imss.TarifaAnualIsr
import java.math.RoundingMode

class IndeminzacionBuilder {

	def build(Finiquito finiquito){

        def ejercicio = Periodo.obtenerYear(finiquito.empleado.baja.fecha)
        def totalGravado =  NominaPorEmpleado
            .executeQuery(
                "select sum(ne.totalGravado) from NominaPorEmpleado ne where ne.empleado=? and ne.nomina.ejercicio = ? and " +
                " ne.nomina.tipo!='LIQUIDACION'"
                ,[finiquito.empleado,ejercicio])[0]

		ZonaEconomica smg = ZonaEconomica.where {ejercicio == Periodo.obtenerYear(finiquito.empleado.baja.fecha) && clave == 'A'}.find()
		
		finiquito.with{
        
            def montoIntereses = 0.0  //**** Agrgar al bean  **//

            def tasaInteres = 0.0 //**** Agrgar al bean  **//

            //def dias20PorAnio = 20

            def dias20PorAnio = 0

            def dias3MesesSueldo = 90
            //def dias3MesesSueldo = 45
             

            def tope2Veces = smg.salario * 2

            //def diasAnioPorAntiguedad = 12
        
                def diasAnioPorAntiguedad = 0

            indemnizacionIntereses = montoIntereses * tasaInteres / 100.0



            def sdiLiq = sdiOpcion ? salarioDiarioIntegradoLiq : salarioDiarioIntegrado

            indemnizacion20DiasPorAnio = sdiLiq * dias20PorAnio * anosTrabajados

            indemnizacion3MesesDeSueldo = sdiLiq * dias3MesesSueldo

            indemnizacionPrimaDeAntiguedad = tope2Veces * diasAnioPorAntiguedad * anosTrabajados

            def totalIndemnizacion = indemnizacion20DiasPorAnio + indemnizacion3MesesDeSueldo + indemnizacionPrimaDeAntiguedad       

            indemnizacionExenta = totalIndemnizacion > (smg.uma * anosTrabajados * 90) ?  (smg.uma * anosTrabajados * 90)  - indemnizacionPrimaDeAntiguedad : totalIndemnizacion - indemnizacionPrimaDeAntiguedad
    
            indemnizacionGravada = totalIndemnizacion - indemnizacionExenta - indemnizacionPrimaDeAntiguedad
    
            primaDeAntiguedadExenta = indemnizacionPrimaDeAntiguedad 

            //Se utilizo esta columna para Acumulado Total Gravado y se mando a la variable ingresoGravadoAcumAnual
            isrAcumuladoDelMes = totalGravado? totalGravado : 0

            //println "isrAcumuladoDelMes: "+isrAcumuladoDelMes

            def ingresoGravadoAcumAnual = isrAcumuladoDelMes

            def sueldoMensual = empleado.salario.salarioDiario * 30

            def ingresoAcumulable1 = 0

            if(ingresoGravadoAcumAnual){
                 ingresoAcumulable1 = ingresoGravadoAcumAnual + sueldoMensual
              }else{
                 ingresoAcumulable1 = sueldoMensual
              }

             // println "ingresoAcumulable1: "+ingresoAcumulable1

            def isrSobreIngresoAcumulable = calcularImpuesto(ingresoAcumulable1)
            //println "isrSobreIngresoAcumulable: "+isrSobreIngresoAcumulable
            isr = isrSobreIngresoAcumulable            

            if(totalIndemnizacion>sueldoMensual){
                def cociente = isrSobreIngresoAcumulable / ingresoAcumulable1
             //   println "cociente: "+cociente
                def tasaEfectiva = cociente * 100
              //  println "tasaEfectiva: "+tasaEfectiva
                def exedenteIngresoExento = totalIndemnizacion - primaDeAntiguedadExenta - indemnizacionExenta            
                
             //   println "exedenteIngresoExento: "+exedenteIngresoExento+"  ---- totalIndemnizacion: "+totalIndemnizacion+"  --- sueldoMensual: "+sueldoMensual

                def ingresoAcumulable2 = exedenteIngresoExento - sueldoMensual

              //  println "ingresoAcumulable2: "+ingresoAcumulable2

                def isrIngresoNoAcumulable = ingresoAcumulable2 * tasaEfectiva / 100

             //   println("isrIngresoNoAcumulable : "+isrIngresoNoAcumulable)

                isr = isrSobreIngresoAcumulable + isrIngresoNoAcumulable

             //   println("isr2 : "+isr)

            }

            if(indemnizacionGravada==0){
                isr = 0.0
            }

            
    
            //primaDeAntiguedadGravada=0.0
            
        }



        return finiquito
	}

    private BigDecimal calcularImpuesto(BigDecimal percepciones){
        def tarifa =TarifaAnualIsr.buscar(percepciones)
      //  println "Calculando el impuesto..."
     //   println 'Tarifa seleccionada: '+tarifa+ ' Para un ingreso de: '+percepciones
        def importeGravado=percepciones-(tarifa.limiteInferior)
      //  println "importeGravado: "+importeGravado +"--- limiteInferior: "+tarifa.limiteInferior
        importeGravado*=tarifa.porcentaje
     //   println "importeGravado: "+importeGravado +"--- tarifa-porcentaje: "+tarifa.porcentaje
        importeGravado/=100
      //  println "importeGravado: "+importeGravado 
        importeGravado+=(tarifa.cuotaFija)
      //  println "importeGravado: "+importeGravado +"--- cuotaFija: "+tarifa.cuotaFija
        importeGravado=importeGravado.setScale(2,RoundingMode.HALF_EVEN)
     //   println "importeGravado redondeado: "+importeGravado 

        return importeGravado
    }
}