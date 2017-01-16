package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.*
import java.math.*
import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.rh.finiquito.*
import com.luxsoft.sw4.rh.ConceptoDeNomina

@Transactional
class FiniquitoService {

    def save(Finiquito finiquito){
        
        inicializarFiniquito finiquito
        registrarVacaciones finiquito
        registrarAguinaldoFiniquito finiquito
        registrarDeduccionImss finiquito
        
        // Aplicamos reglas para la Indeminzacion
        new IndeminzacionBuilder().build(finiquito)

        new PercepcionBuilder().build(finiquito)

        finiquito.totalExento = finiquito.primaVacacionalExenta + finiquito.aguinaldoExento + finiquito.indemnizacionExenta + finiquito.primaDeAntiguedadExenta + finiquito.primaDominicalExenta + finiquito.compensacionSAF + finiquito.subsEmpPagado


        finiquito.totalGravado = finiquito.sueldo + finiquito.comisiones + finiquito.vacaciones + finiquito.primaVacacionalGravada + finiquito.incentivo + finiquito.aguinaldoGravable + finiquito.indemnizacionGravada + finiquito.primaDeAntiguedadGravada + finiquito.primaDominicalGravada + finiquito.compensacion + finiquito.bonoDeProductividad + finiquito.permisoPorPaternidad


        finiquito.percepcionTotal = finiquito.totalExento + finiquito.totalGravado

        
        registrarDeduccionImss finiquito

        finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:finiquito.aguinaldoGravable , importeExcento: finiquito.aguinaldoExento, concepto: ConceptoDeNomina.get(14))
        
        finiquito.save failOnError:true
        
        return finiquito
    }

    def inicializarFiniquito(Finiquito finiquito){
        Periodo p = Periodo.getPeriodoAnual(finiquito.baja.fecha.toYear())
        def de = p.fechaFinal - p.fechaInicial + 1    
        def dte = finiquito.baja.fecha - p.fechaInicial + 1 

        finiquito.with {
            empleado = finiquito?.baja?.empleado
            alta = finiquito?.empleado?.alta
            antiguedad = (finiquito.baja.fecha - finiquito.empleado.alta) + 1
            diasDelEjercicio = de
            diasTrabajadoEjercicio = dte
            anosTrabajados = (((finiquito.baja.fecha - finiquito.empleado.alta) + 1)/365).setScale(0,RoundingMode.UP).intValue()
            salario = finiquito.empleado.salario.salarioDiario
            salarioVariable = finiquito.empleado.salario.salarioVariable
            salarioDiarioIntegrado = finiquito.empleado.salario.salarioDiarioIntegrado            
        }
        return finiquito
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

    def registrarAguinaldoFiniquito(Finiquito finiquito){
            ZonaEconomica smg = ZonaEconomica.where {ejercicio == Periodo.obtenerYear(finiquito.empleado.baja.fecha) && clave == 'A'}.find()
            finiquito.with {          
            diasAguinaldo = 15            
            factorLiquidacion = ( ( (finiquito.vacacionesEjercicio * finiquito.primaVacacional) + diasAguinaldo ) / finiquito.diasDelEjercicio)
            factorLiquidacion = MonedaUtils.round(factorLiquidacion,4) + 1
            salarioDiarioIntegradoLiq = salario  * finiquito.factorLiquidacion        
            diasParaAguinaldo = finiquito.diasTrabajadoEjercicio + 31   
            def aguinaldo = salario * (diasAguinaldo / diasDelEjercicio) * diasParaAguinaldo
                println("Res Agndo : "+Aguinaldo)
            def topeEx = smg.salario * 30
            aguinaldoExento = aguinaldo > topeEx ? topeEx : aguinaldo
            aguinaldoGravable = aguinaldo - aguinaldoExento
            }
            return finiquito
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
        
        def gmp=((val3*0.375)*diasDelPeriodo)/100
        gmp=gmp.setScale(2,RoundingMode.HALF_EVEN)
        aporacionAsegurado+=gmp
        
        def iv=((val3*0.625)*diasTrabajados)/100
        iv=iv.setScale(2,RoundingMode.HALF_EVEN)
        aporacionAsegurado+=iv
        
        def sr=0
        aporacionAsegurado+=sr
        
        def cv=((val3*1.125)*diasTrabajados)/100
        cv=cv.setScale(2,RoundingMode.HALF_EVEN)
        aporacionAsegurado+=cv
        
        def sg=0
        aporacionAsegurado+=sg
        
        def rt=0
        aporacionAsegurado+=rt
        
        def inf=0
        aporacionAsegurado+=inf

        return finiquito
    }



    def eliminar(Finiquito finiquito){
        finiquito.delete flush:true
    }
 
}