package com.luxsoft.cfdix.v33

import org.apache.commons.logging.LogFactory
import org.bouncycastle.util.encoders.Base64
import java.math.RoundingMode

import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.rh.TiempoExtra
import com.luxsoft.sw4.rh.Finiquito
import com.luxsoft.sw4.rh.CalculoAnual
import com.luxsoft.sw4.MonedaUtils

import lx.cfdi.utils.DateUtils
import lx.cfdi.v33.ObjectFactory
import lx.cfdi.v33.Comprobante

import lx.cfdi.v33.nomina.Nomina
import lx.cfdi.v33.nomina.NominaUtils


// Catalogos
import lx.cfdi.v33.nomina.CTipoNomina
import lx.cfdi.v33.CEstado

class NominaBuilder {

	private static final log = LogFactory.getLog(this)

    private factory = new ObjectFactory();
    private Empresa empresa
    private NominaPorEmpleado ne;
    private Nomina nomina;

    def build(NominaPorEmpleado ne){
        empresa = Empresa.first()
        this.nomina = factory.createNomina()
        this.ne = ne
        nomina.version = '1.2'
        nomina.tipoNomina = CTipoNomina.O
        nomina.tipoNomina = CTipoNomina.O
        if(ne.nomina.tipo == 'PTU'|| ne.nomina.tipo == 'AGUINALDO' || ne.nomina.tipo == 'LIQUIDACION' || ne.nomina.tipo == 'ASIMILADOS'){
            nomina.tipoNomina = CTipoNomina.E
        }
        
        nomina.fechaPago = NominaUtils.toDate(ne.nomina.pago)
        nomina.fechaInicialPago = NominaUtils.toDate(ne.nomina.periodo.fechaInicial)
        nomina.fechaFinalPago = NominaUtils.toDate(ne.nomina.periodo.fechaFinal)
        nomina.numDiasPagados = calcularDiasPagados()
        
        def totalPercepciones = ne.conceptos.sum 0, { 
            if(it.concepto.catalogoSat == 'c_TipoPercepcion') 
                return it.total 
            return 0
        }
        if(totalPercepciones > 0){
            nomina.setTotalPercepciones(totalPercepciones)
        }
        def otrosPagos = ne.conceptos.sum 0, { 
            if(it.concepto.catalogoSat == 'c_TipoOtroPago') 
                return it.total 
            return 0
        }

        if (otrosPagos > 0 ){
            nomina.setTotalOtrosPagos(otrosPagos)
        }

        def totalDeducciones = ne.conceptos.sum 0, { 
            if(it.concepto.catalogoSat == 'c_TipoDeduccion') 
                return it.total 
            return 0
        }
        if(totalDeducciones > 0 ){
            nomina.setTotalDeducciones(totalDeducciones)
        }
        registrarEmisor()
        registrarReceptor()
        buildPercepciones()
        buildSeparacion()
        buildOtrosPagos()
        buildDeducciones()
        return nomina;
    }

    def registrarEmisor() {
        Nomina.Emisor emisor = factory.createNominaEmisor()
        emisor.setRegistroPatronal(empresa.registroPatronal)
        nomina.emisor = emisor
    }

    def registrarReceptor(){
        Empleado empleado = ne.empleado
        Nomina.Receptor receptor = factory.createNominaReceptor()
        receptor.curp = empleado.curp
        receptor.numSeguridadSocial = empleado.seguridadSocial.numero.replace('-','')
        receptor.antigüedad = "P${ne.antiguedad}W"
        if(ne.antiguedad <= 0) {
            def diasLab=new BigDecimal((ne.nomina.periodo.fechaFinal-ne.empleado.alta)+1).setScale(0, RoundingMode.HALF_EVEN)
            receptor.antigüedad = "P${diasLab}D"
        }
        def tr = empleado.perfil.regimenContratacion.clave.toString().padLeft(2,'0')
        receptor.tipoRegimen = tr // TODOS LOS EMPLEADOS DEBEN TENER TIPO REGIMEN
        receptor.numEmpleado = empleado.perfil.numeroDeTrabajador
        receptor.setDepartamento(empleado.perfil.departamento.clave)
        receptor.puesto = empleado.perfil.puesto?.clave
        receptor.setFechaInicioRelLaboral(NominaUtils.toDate(empleado.alta))
        receptor.setTipoContrato('01')  // AJUSTAR EN LA APLICACION
        receptor.setTipoJornada('01')  /// AJUSTAR LA APLICACION
        receptor.setSindicalizado(empleado.perfil.tipo == 'SINDICALIZADO' ? 'Sí' : 'No')  // AJUSTAR EN LA APLICACION
        receptor.setClaveEntFed(CEstado.DIF) 
        
        if(empleado.perfil?.riesgoPuesto?.clave){
            receptor.setRiesgoPuesto(empleado.perfil?.riesgoPuesto?.clave.toString())
        }
        
        if(ne.nomina.periodicidad == 'QUINCENAL')
            receptor.setPeriodicidadPago('04')
        else
            receptor.setPeriodicidadPago('02')
        if(ne.nomina.tipo == 'LIQUIDACION' || ne.nomina.tipo == 'PTU' || ne.nomina.tipo == 'AGUINALDO' ){
            receptor.setPeriodicidadPago('99')
        }
        
        if( ne.nomina.formaDePago == 'TRANSFERENCIA'){

            if(empleado.salario.clabe){

            } else {
                    if(empleado?.salario?.banco?.clave && ne.nomina.formaDePago == 'TRANSFERENCIA'){
                        def bancoClave = empleado?.salario.banco.clave.toString().padLeft(3,'0')
                        receptor.setBanco(bancoClave)
                        }
                receptor.cuentaBancaria = new BigInteger(empleado.salario.numeroDeCuenta)
            }
                
        }
        receptor.setSalarioBaseCotApor(ne.salarioDiarioBase)
        receptor.setSalarioDiarioIntegrado(ne.salarioDiarioIntegrado)
        nomina.receptor = receptor
    }


    private BigDecimal calcularDiasPagados(){
        def diasTrabajados=0
        def faltas=0
        if(ne.asistencia){
            if(!ne.empleado.controlDeAsistencia){
                diasTrabajados = ne.diasTrabajados + ne.vacaciones - (ne.asistencia.faltasManuales+(ne.asistencia.faltasManuales * 0.167)+ ne.incapacidades)
            }else{
                if(ne.empleado.alta<=ne.asistencia.calendarioDet.inicio){
                  diasTrabajados=ne.diasTrabajados+ne.vacaciones                 
                }else{
                    diasTrabajados=ne.diasTrabajados+ne.vacaciones+ne.asistencia.paternidad 
                }
            }       
        }
        def diasLab=new BigDecimal(diasTrabajados).setScale(3, RoundingMode.HALF_EVEN)
        if(diasLab<=0) diasLab = 1
        return diasLab
    }

    def buildPercepciones(){
        // Percepciones
        Nomina.Percepciones per = factory.createNominaPercepciones()
        def percepciones = ne.conceptos.findAll{it.concepto.catalogoSat == 'c_TipoPercepcion'}
        
        per.totalSueldos = percepciones.sum 0.0, {
            def clave = it.concepto.claveSat.toString().padLeft(3,'0')
            if(com.luxsoft.sw4.cfdi.nomina12.NominaUtils.isSueldos(clave) ){
               return it.importeGravado + it.importeExcento
            }
            return 0.0
        }

        per.totalGravado = percepciones.sum 0.0, {
            def clave = it.concepto.claveSat.toString().padLeft(3,'0')
            return it.importeGravado
        }

        per.totalExento = percepciones.sum 0.0, {
            def clave = it.concepto.claveSat.toString().padLeft(3,'0')
            return it.importeExcento
        }

        percepciones.each{
            Nomina.Percepciones.Percepcion pp = factory.createNominaPercepcionesPercepcion()
            def clave = it.concepto.catalogoSatClave
            pp.setTipoPercepcion(clave)
            pp.setClave(it.concepto.clave)  // Debe ser la clave que se usa en contabilidad que sea la que se usa en contabilidad
            pp.setConcepto(it.concepto.descripcion)
            if(ne.nomina.tipo == 'ASIMILADOS' && it.concepto.clave == 'P036'){
                String d = ne.empleado.rfc=="AAIJ4009248G4" ? "${it.concepto.descripcion}" : "${it.concepto.descripcion} ${ne.nomina.calendarioDet.mes.toUpperCase()} ${it.parent.nomina.ejercicio}"
                pp.setConcepto(d)
            }
            pp.setImporteGravado(it.importeGravado)
            pp.setImporteExento(it.importeExcento)

            // Horas extras
            if( it.concepto.clave == 'P022' || it.concepto.clave == 'P023') {
                def dias = ne.asistencia.partidas.count {det -> det.pagarTiempoExtra}
                def te = TiempoExtra.where {asistencia == ne.asistencia}.find()
                // Horas dobles
                def doblesTotal = te.getDoblesExcentos() + te.getDoblesGravados()
                if(doblesTotal > 0) {
                    def minutosDobles = te.partidas.sum 0, { it.minutosDobles }
                    def doblesTotalHoras = Math.floor(minutosDobles /60)
                    
                    
                    Nomina.Percepciones.Percepcion.HorasExtra heDobles = factory.createNominaPercepcionesPercepcionHorasExtra()
                    heDobles.dias = dias
                    heDobles.tipoHoras = '01'
                    heDobles.horasExtra = doblesTotalHoras
                    heDobles.importePagado = doblesTotal    
                }

                // Horas triples
                def triplesTotal = te.getTriplesGravados()
                if( triplesTotal > 0 ) {
                    def minutosTriples = te.partidas.sum 0, { it.minutosTriples }
                    def triplesTotalHoras = Math.floor(minutosTriples /60)
                    
                    Nomina.Percepciones.Percepcion.HorasExtra heTriples = factory.createNominaPercepcionesPercepcionHorasExtra()
                    heTriples.dias = dias
                    heTriples.tipoHoras = '02'
                    heTriples.horasExtra = triplesTotalHoras
                    heTriples.importePagado = triplesTotal
                }
            }
            per.percepcion.add(pp)

        }
        if( ne.nomina.tipo == 'LIQUIDACION' ) {
            per.totalSeparacionIndemnizacion = per.totalGravado + per.totalExento
        }
        nomina.percepciones = per
        this
    }


    def buildSeparacion(){
        if(ne.nomina.tipo == 'LIQUIDACION') {
            Finiquito finiquito = Finiquito.where {neLiquidacion == ne}.find()
            assert finiquito, 'No se ha registrado la enitdad Finiquito para la nomina de empleado:' + ne.id 
            def ingresoGravadoPorIndeminacion = ne.getPercepcionesGravadas()
            def ultimoSueldoMensualOrdinario = ne.empleado.salario.salarioDiario * 30
            def ingresoAcumulable = ingresoGravadoPorIndeminacion > ultimoSueldoMensualOrdinario ? ultimoSueldoMensualOrdinario : ingresoGravadoPorIndeminacion
            def ingresoNoAcumulable = ingresoGravadoPorIndeminacion - ultimoSueldoMensualOrdinario
            if(ingresoNoAcumulable < 0 ) ingresoNoAcumulable = 0.0
            
            Nomina.Percepciones.SeparacionIndemnizacion separacion = factory.createNominaPercepcionesSeparacionIndemnizacion()
            separacion.setUltimoSueldoMensOrd(ultimoSueldoMensualOrdinario)
            separacion.setIngresoAcumulable(ingresoAcumulable)
            separacion.setIngresoNoAcumulable(ingresoNoAcumulable )
            separacion.setNumAñosServicio(finiquito.anosTrabajados)
            separacion.setTotalPagado(ne.total)
            nomina.percepciones.separacionIndemnizacion = separacion
            
        }
        return this
    }

    def buildOtrosPagos(){
        def percepciones = ne.conceptos.findAll{it.concepto.catalogoSat == 'c_TipoOtroPago'}
        if(!percepciones) return null
        Nomina.OtrosPagos otrosPagos = factory.createNominaOtrosPagos()
        
        percepciones.each{
            Nomina.OtrosPagos.OtroPago otroPago = factory.createNominaOtrosPagosOtroPago()
            def clave = it.concepto.catalogoSatClave
            otroPago.setTipoOtroPago(clave)
            otroPago.setClave(it.concepto.clave)  // Debe ser la clave que se usa en contabilidad que sea la que se usa en contabilidad
            otroPago.setConcepto(it.concepto.descripcion.replace('.',','))
            otroPago.setImporte(it.total)
            if(clave == '002'){
                Nomina.OtrosPagos.OtroPago.SubsidioAlEmpleo subsidioAlEmpleo = factory.createNominaOtrosPagosOtroPagoSubsidioAlEmpleo()
                subsidioAlEmpleo.setSubsidioCausado(it.parent.subsidioEmpleoAplicado)
                otroPago.subsidioAlEmpleo = subsidioAlEmpleo
            }
            if(clave == '004') {
                def eje = 2017
                CalculoAnual ca = CalculoAnual.where {ejercicio == eje && empleado == ne.empleado}.find()
                assert ca, 'Debe existir el calculo anual para poder usar el concepto 004'
                Nomina.OtrosPagos.OtroPago.CompensacionSaldosAFavor compensacionSaldosAFavor = factory.createNominaOtrosPagosOtroPagoCompensacionSaldosAFavor()
                compensacionSaldosAFavor.setAño((short)eje)
                compensacionSaldosAFavor.setSaldoAFavor(ca.resultado)
                def remanente = ca.resultado - ca.aplicado - it.total
                if(remanente < 0)
                    remanente = 0
                compensacionSaldosAFavor.setRemanenteSalFav(remanente)
                otroPago.compensacionSaldosAFavor = compensacionSaldosAFavor
            }
            otrosPagos.otroPago.add(otroPago)
        }
        nomina.setOtrosPagos(otrosPagos)
        return this
    }

    def buildDeducciones(){
        
        def deducciones = ne.conceptos.findAll{it.concepto.tipo == 'DEDUCCION'}

        def totalOtrasDeducciones = getTotalOtras(deducciones)
        def isr = getTotalIsr(deducciones)

        if( totalOtrasDeducciones <= 0.0 && isr <= 0.0) 
            return null

        Nomina.Deducciones ded = factory.createNominaDeducciones()
        ded.totalOtrasDeducciones = totalOtrasDeducciones
        
        if(isr){
            ded.totalImpuestosRetenidos = isr
        }
        
        deducciones.each{
            Nomina.Deducciones.Deduccion dd  = factory.createNominaDeduccionesDeduccion()
            def clave = it.concepto.claveSat.toString().padLeft(3,'0')
            dd.setTipoDeduccion(clave)
            dd.setClave(it.concepto.clave)
            dd.setConcepto(it.concepto.descripcion.replace('.',','))
            dd.setImporte(it.total)
            ded.deduccion.add(dd)
        }
        nomina.deducciones = ded
        return this
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
