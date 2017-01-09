package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.*
import java.math.*
import com.luxsoft.sw4.rh.tablas.ZonaEconomica

@Transactional
class FiniquitoService {

        def inicializarFiniquito(Finiquito finiquitoInstance){
            Periodo p = Periodo.getPeriodoAnual(finiquitoInstance.baja.fecha.toYear())
            def de = p.fechaFinal - p.fechaInicial + 1    
            def dte = finiquitoInstance.baja.fecha - p.fechaInicial + 1 

            finiquitoInstance.with {
            empleado = finiquitoInstance?.baja?.empleado
            alta = finiquitoInstance?.empleado?.alta
            antiguedad = (finiquitoInstance.baja.fecha - finiquitoInstance.empleado.alta) + 1
            diasDelEjercicio = de
            diasTrabajadoEjercicio = dte
            anosTrabajados = (((finiquitoInstance.baja.fecha - finiquitoInstance.empleado.alta) + 1)/365).setScale(0,RoundingMode.UP).intValue()
            salario = finiquitoInstance.empleado.salario.salarioDiario
            salarioVariable = finiquitoInstance.empleado.salario.salarioVariable
            salarioDiarioIntegrado = finiquitoInstance.empleado.salario.salarioDiarioIntegrado            
            }
            return finiquitoInstance
        }

        def registrarVacaciones(Finiquito finiquito){ 
            ControlDeVacaciones cv = ControlDeVacaciones.where {empleado == finiquito.empleado && ejercicio == Periodo.obtenerYear(finiquito.empleado.baja.fecha)}.find()
            ZonaEconomica smg = ZonaEconomica.where {ejercicio == Periodo.obtenerYear(finiquito.empleado.baja.fecha) && clave == 'A'}.find()
            finiquito.with{
                vacacionesEjercicio = cv.diasVacaciones
                vacacionesAplicadas = cv.diasTomados + cv.diasPagados?:0
                vacacionesAnteriores = cv.diasTrasladados
                primaVacacional = 0.25                            
                def primVacExAcu = cv.acumuladoExcento
                diasTrabajadoParaVacaciones = (finiquito.baja.fecha - cv.aniversario ) 
            if(finiquito.diasTrabajadoParaVacaciones < 0 )
                diasTrabajadoParaVacaciones = 0             
            def vacacionesFiniquito = finiquito.vacacionesEjercicio + finiquito.vacacionesAnteriores - finiquito.vacacionesAplicadas  
            def sd = !finiquito.salario ? finiquito.salarioVariable : finiquito.salario
            println("salario diario : "+sd+" Vacaciones Finiquito : "+vacacionesFiniquito)
            vacaciones = ((sd * vacacionesFiniquito) + ((sd * finiquito.vacacionesEjercicio / finiquito.diasDelEjercicio) * finiquito.diasTrabajadoParaVacaciones))
            def pv = finiquito.vacaciones * finiquito.primaVacacional
            def topeEx = smg.salario * 15
            primaVacacionalExenta = (primVacExAcu <= topeEx ? (primVacExAcu + pv < topeEx ? pv : topeEx - primVacExAcu) : 0.0)
            primaVacacionalGravada = pv - finiquito.primaVacacionalExenta
            }
            return finiquito
        }

        def registrarAguinaldoFiniquito(Finiquito finiquitoInstance){
            ZonaEconomica smg = ZonaEconomica.where {ejercicio == Periodo.obtenerYear(finiquitoInstance.empleado.baja.fecha) && clave == 'A'}.find()
            finiquitoInstance.with {          
            diasAguinaldo = 15            
            factorLiquidacion = ( ( (finiquitoInstance.vacacionesEjercicio * finiquitoInstance.primaVacacional) + diasAguinaldo ) / finiquitoInstance.diasDelEjercicio)
            factorLiquidacion = MonedaUtils.round(factorLiquidacion,4) + 1
            salarioDiarioIntegradoLiq = salario  * finiquitoInstance.factorLiquidacion        
            diasParaAguinaldo = finiquitoInstance.diasTrabajadoEjercicio + 31   
            def aguinaldo = salario * (diasAguinaldo / diasDelEjercicio) * diasParaAguinaldo
                println("Res Agndo : "+Aguinaldo)
            def topeEx = smg.salario * 30
            aguinaldoExento = aguinaldo > topeEx ? topeEx : aguinaldo
            aguinaldoGravable = aguinaldo - aguinaldoExento
            }
            return finiquitoInstance
        }

        def registrarDeduccionImss(Finiquito finiquito) {
            def ejercicio = finiquito.baja.fecha.toYear()
            def empleado = finiquito.empleado

            def salarioMinimo = ZonaEconomica.findByClaveAndEjercicio('A',ejercicio).salario
            def sdi = empleado.salario.salarioDiarioIntegrado
            def diasTrabajados = finiquito.diasPorPagar
            def diasDelPeriodo = finiquito.diasPorPagar

            def prima=0.5
            def aporacionAsegurado=0.0
            //EyM sobre 1 SMGDF
        aporacionAsegurado=0
    
        //'EyM sobre dif. entre SBC y 3 SMGDF
        def emd=0.0
        if(sdi<(salarioMinimo * 25)){
            if(sdi<(salarioMinimo*3)){
                emd=0.0
            }else{
                emd=sdi-(salarioMinimo*3)
            }
        }else{
            emd=(salarioMinimo*25)-(salarioMinimo*3)
        }
        emd=(emd*0.40*diasDelPeriodo)/100
        emd=emd.setScale(2,RoundingMode.HALF_EVEN)
        log.debug 'EyM sobre dif. entre SBC y 3 SMGDF: '+emd
        aporacionAsegurado+=emd
    
        'Prestaciones en dinero EyM sobre SBC'
        def val3
        if(sdi<(salarioMinimo*1.0452)){
            val3=(salarioMinimo*1.0452)
        }else if(sdi<(salarioMinimo*25)){
            val3=sdi
        }else{
            val3=(salarioMinimo*25)
        }
        def pd=((val3*0.25)*diasDelPeriodo)/100
        pd=pd.setScale(2,RoundingMode.HALF_EVEN)
        aporacionAsegurado+=pd
        println 'Prestaciones en dinero EyM sobre SBC: '+pd
    
        def gmp=((val3*0.375)*diasDelPeriodo)/100
        gmp=gmp.setScale(2,RoundingMode.HALF_EVEN)
        aporacionAsegurado+=gmp
        println 'Gastos mdicos pensionado sobre SBC: '+gmp
    
        def iv=((val3*0.625)*diasTrabajados)/100
        iv=iv.setScale(2,RoundingMode.HALF_EVEN)
        aporacionAsegurado+=iv
        println 'Invalidez y Vida sobre SBC: '+iv
    
        def sr=0
        aporacionAsegurado+=sr
        println 'Seguro de Retiro: '+sr
    
        def cv=((val3*1.125)*diasTrabajados)/100
        cv=cv.setScale(2,RoundingMode.HALF_EVEN)
        aporacionAsegurado+=cv
        println 'Cesanta edad avanzada y vejez sobre SBC: '+cv
    
        def sg=0
        aporacionAsegurado+=sg
        println 'Seguro de Guarderas sobre SBC: '+sg
    
        def rt=0
        aporacionAsegurado+=rt
        println 'Riesgos de trabajo: '+rt
    
        def inf=0
        aporacionAsegurado+=inf
        println 'Infonavit: '+inf 
    }

 /*		def sd = 0.0
     	def de = 0 
     	def dte = 0

    
    def calcular(def ejercicio, Finiquito finiquito) {
    	Empleado e = finiquito.empleado
    	Periodo p = Periodo.getPeriodoAnual(finiquito.baja.fecha.toYear())
    	ControlDeVacaciones cv = ControlDeVacaciones.where {empleado == e && ejercicio == ejercicio}.find()
     	 sd = e.salario.salarioDiario
     	 de = p.fechaFinal - p.fechaInicial + 1    
     	 dte = finiquito.baja.fecha - p.fechaInicial + 1 	

    	finiquito.salario = sd
    	finiquito.diasDelEjercicio = de
    	finiquito.diasTrabajadoEjercicio = dte

    	finiquito.with {
    		salario = e.salario.salarioDiario
    		salarioDiarioIntegrado = e.salario.salarioDiarioIntegrado
    		antiguedad = finiquito.baja.fecha - finiquito.empleado.alta + 1
    		diasAguinaldo = 15
    		primaVacacional = 0.25
    		diasDelEjercicio = p.fechaFinal - p.fechaInicial + 1
    		

    		factorLiquidacion =( (vacacionesEjercicio * primaVacacional) + diasAguinaldo ) / diasDelEjercicio 
    		factorLiquidacion = MonedaUtils.round(factorLiquidacion,4) + 1
    		salarioDiarioIntegradoLiq = salario  * factorLiquidacion    		
    	//	diasTrabajadoEjercicio = finiquito.baja.fecha - p.fechaInicial + 1
    		diasParaAguinaldo = diasTrabajadoEjercicio + 31

    		
    		
			imss = 0.0
			isr = 0.0
			pensionAlimenticia = 0.0
			infonavit = 0.0
			fonacot = 0.0
			prestamo = 0.0
			otrasDeducciones = 0.0
			retardos = 0.0
			anticipoDeNomina = 0.0

    	}
    //	registrarPercepciones(finiquito,cv)
    	registrarVacaciones(finiquito,cv)

    	return finiquito
    }

    private registrarVacaciones(Finiquito finiquito, def cv){ 
    		finiquito.with{
    			vacacionesEjercicio =cv.di
    		}
    		sd = finiquito.empleado.salario.salarioDiario       	
        	finiquito.vacacionesEjercicio = cv.diasVacaciones
    		finiquito.vacacionesAplicadas = cv.diasTomados + cv.diasPagados?:0
    		finiquito.vacacionesAnteriores = cv.diasTrasladados
    		def primaVacacionalExentaAcu = 0.0
    		primaVacacionalExentaAcu = cv.acumuladoExcento
    		finiquito.diasTrabajadoParaVacaciones = (finiquito.baja.fecha - cv.aniversario ) 
    		if(finiquito.diasTrabajadoParaVacaciones < 0 )
    			finiquito.diasTrabajadoParaVacaciones = 0 
    		def vacacionesFiniquito = 0
    		vacacionesFiniquito = vacacionesEjercicio + vacacionesAnteriores - vacacionesAplicadas
    		sd = 
    		vacaciones = (salario ? 
    			(salario * vacacionesFiniquito) + ((salario * vacacionesEjercicio / diasDelEjercicio) * diasTrabajadoParaVacaciones)	)
				: (salarioVariable * vacacionesFiniquito) + ((salarioVariable * vacacionesEjercicio / diasDelEjercicio) * diasTrabajadoParaVacaciones)				
			primaVacacionalExenta = 0.0
			primaVacacionalGravada = 0.0

			return finiquito
    	}

    private registrarPercepciones(Finiquito finiquito, def cv){
    	finiquito.with{
    		sueldo = salario * diasPorPagar
			comisiones = (salario == 0.0 ? salarioVariable * diasPorPagar : 0.0)
			incentivo = 0.0
			aguinaldoExento = 0.0
			aguinaldoGravable = 0.0
			indemnizacionExenta = 0.0
			indemnizacionGravada = 0.0
			primaDeAntiguedadExenta = 0.0
			primaDeAntiguedadGravada = 0.0
			primaDominicalExenta = 0.0
			primaDominicalGravada = 0.0
			compensacion = 0.0
			bonoDeProductividad = 0.0
			permisoPorPaternidad = 0.0
			compensacionSAF = 0.0
			subsEmpPagado = 0.0
			subsEmpAplicado = 0.0
			ingresoTotal = 0.0
    	}
    }
*/
    /*
    public Integer getDiasDelEjercicio(def baja){
    	Periodo p = Periodo.getPeriodoAnual(baja.fecha.toYear())
    	return p.fechaFinal - p.fechaFinal + 1
		
	}
	*/




}