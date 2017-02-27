package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.*
import java.math.*
import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.rh.finiquito.*
import com.luxsoft.sw4.rh.ConceptoDeNomina

@Transactional
class FiniquitoService {

    def actualizarImpuestos(Finiquito finiquito){
        // Actualizar impuesto
        //new ImpuestoBuilder().build(finiquito)
    }

    def save(Finiquito finiquito){

        
        def automaticas = finiquito.partidas.findAll {!it.manual }
        automaticas.each { item ->
            finiquito.removeFromPartidas(item)
        }

        inicializarFiniquito finiquito
        registrarVacaciones finiquito
        registrarAguinaldoFiniquito finiquito
        //registrarDeduccionImss finiquito
        
        // Aplicamos reglas para la Indeminzacion
        new IndeminzacionBuilder().build(finiquito)

        new PercepcionBuilder().build(finiquito)

        finiquito.totalExento = finiquito.primaVacacionalExenta + finiquito.aguinaldoExento + finiquito.indemnizacionExenta + finiquito.primaDeAntiguedadExenta + finiquito.primaDominicalExenta + finiquito.compensacionSAF + finiquito.subsEmpPagado


        finiquito.totalGravado = finiquito.sueldo + finiquito.comisiones + finiquito.vacaciones + finiquito.primaVacacionalGravada + finiquito.incentivo + finiquito.aguinaldoGravable + finiquito.indemnizacionGravada + finiquito.primaDeAntiguedadGravada + finiquito.primaDominicalGravada + finiquito.compensacion + finiquito.bonoDeProductividad + finiquito.permisoPorPaternidad


        finiquito.percepcionTotal = finiquito.totalExento + finiquito.totalGravado

        
        registrarDeduccionImss finiquito

        
        finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:finiquito.sueldo, importeExcento:0, concepto: ConceptoDeNomina.get(13));
        finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:finiquito.vacaciones, importeExcento:0, concepto: ConceptoDeNomina.get(37));        

        finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:finiquito.primaVacacionalGravada, importeExcento:finiquito.primaVacacionalExenta, concepto: ConceptoDeNomina.get(36));
        finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:finiquito.aguinaldoGravable, importeExcento:finiquito.aguinaldoExento, concepto: ConceptoDeNomina.get(14));
        finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:finiquito.indemnizacionGravada, importeExcento:finiquito.indemnizacionExenta, concepto: ConceptoDeNomina.get(40), liquidacion: true);
        
        finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:finiquito.primaDeAntiguedadGravada, importeExcento:finiquito.primaDeAntiguedadExenta, concepto: ConceptoDeNomina.get(38), liquidacion: true);
        finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:finiquito.primaDominicalGravada, importeExcento:finiquito.primaDominicalExenta, concepto: ConceptoDeNomina.get(42));

        //finiquito.addToPartidas(tipo: 'DEDUCCION', importeGravado:0, importeExcento:finiquito.imss, concepto: ConceptoDeNomina.get(1));
        //finiquito.addToPartidas(tipo: 'DEDUCCION', importeGravado:0, importeExcento:finiquito.isr, concepto: ConceptoDeNomina.get(2));
        //finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:0, importeExcento:finiquito.subsEmpPagado, concepto: ConceptoDeNomina.get(33));
        //finiquito.addToPartidas(tipo: 'PERCEPCION', importeGravado:0, importeExcento:finiquito.compensacionSAF, concepto: ConceptoDeNomina.get(47));        

        if (finiquito.pensionAlimenticia)        
        finiquito.addToPartidas(tipo: 'DEDUCCION', importeGravado:0, importeExcento:finiquito.pensionAlimenticia, concepto: ConceptoDeNomina.get(7));
        
        if (finiquito.prestamo)        
        finiquito.addToPartidas(tipo: 'DEDUCCION', importeGravado:0, importeExcento:finiquito.prestamo, concepto: ConceptoDeNomina.get(4));
        
        if (finiquito.otrasDeducciones)        
        finiquito.addToPartidas(tipo: 'DEDUCCION', importeGravado:0, importeExcento:finiquito.otrasDeducciones, concepto: ConceptoDeNomina.get(5));

        
        actualizarImpuestos(finiquito)
        finiquito.save failOnError:true
        
        return finiquito
    }

    def inicializarFiniquito(Finiquito finiquito){
        Periodo p = Periodo.getPeriodoAnual(new Date().toYear())
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
                def aniversarioAnterior = cv.aniversario - 366 + 1
                diasTrabajadoParaVacaciones = 
                (finiquito.baja.fecha - cv.aniversario ) < 0 ? finiquito.baja.fecha - aniversarioAnterior + 1 : finiquito.baja.fecha - cv.aniversario + 1

                if(finiquito.diasTrabajadoParaVacaciones.abs() < 0 )
                    diasTrabajadoParaVacaciones = 0             
                def vacacionesFiniquito = finiquito.vacacionesEjercicio + finiquito.vacacionesAnteriores - finiquito.vacacionesAplicadas  
                def sd = !finiquito.salario ? finiquito.salarioVariable : finiquito.salario
            
                if(vacacionesAnteriores < 0 ){
                    def porcentajeDiasVac = diasTrabajadoParaVacaciones * 100 / diasDelEjercicio
                    def diasPonderados = vacacionesEjercicio * porcentajeDiasVac / 100
                    vacacionesFiniquito = vacacionesAnteriores.abs() <  diasPonderados ? ( diasPonderados - vacacionesAnteriores.abs()) : 0
                    vacaciones = sd * vacacionesFiniquito

                } else {
                    vacaciones = (  ((sd * vacacionesFiniquito / diasDelEjercicio) * diasTrabajadoParaVacaciones))
                }
                
                def pv = vacaciones * primaVacacional
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
                def diasAguinaldoDiciembre =  31
                if( (empleado.alta.isSameMonth(baja.fecha)) && (empleado.alta.toMonth() == 12) ) {
                    println('Mes: '+ empleado.alta.toMonth())
                    diasAguinaldoDiciembre =  baja.fecha - empleado.alta + 1
                    println('diasAguinaldoDiciembre: '+ diasAguinaldoDiciembre)
                    diasParaAguinaldo = diasAguinaldoDiciembre
                } else {
                    diasParaAguinaldo = diasTrabajadoEjercicio + diasAguinaldoDiciembre
                }
                
                println('diasParaAguinaldo: '+ diasParaAguinaldo)
                

                def aguinaldo = salario * (diasAguinaldo / diasDelEjercicio) * diasParaAguinaldo
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
        
        finiquito.imss = aporacionAsegurado

        return finiquito
    }



    def eliminar(Finiquito finiquito){
        finiquito.delete flush:true
    }
 
}