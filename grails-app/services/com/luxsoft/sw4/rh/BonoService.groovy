package com.luxsoft.sw4.rh



import org.apache.commons.lang.time.DateUtils

import grails.transaction.Transactional
import grails.transaction.NotTransactional

import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*

import java.math.RoundingMode
import com.luxsoft.sw4.Empresa

@Transactional
class BonoService {

	Empresa empresa

	def generarBonos(Ptu ptu, BigDecimal montoTotal, BigDecimal factorParaBono) {
		
		this.calS = null
		this.calQ = null

		def ptuTotal = ptu.partidas.sum 0.0, { (it.status != 'BAJA' ) ? (it.ptuExcento + it.ptuGravado) : 0.0}
		
		List<Bono> bonos = []

		ptu.partidas.each {
		    if(it.status != 'BAJA') {
		    	def ptuEmpleado = (it.ptuExcento + it.ptuGravado) 
		    	def ptuGravado=it.ptuGravado
				def part = ptuEmpleado * 100 / ptuTotal
				def monto = montoTotal * (part/100)
				log.info "${it.empleado.nombre} Part: ${part} Monto ind: ${monto}"
				Bono bono = generar(it.empleado, 2018)
				if(bono) {
					bono.montoBonoTotal = montoTotal
					bono.ptuGravado=ptuGravado
					bono.factorParaBono = factorParaBono
					bono.ptu = (bono.fechaFinal - it.empleado.alta) >= 365 ? ptuEmpleado : 0.0
					bono.bonoInicial = monto
					calcular(bono)	
					bono.bonoPreliminar = bono.ptu * (bono.porcentajeBono / 100)
					bonos << bono
				}
			}
		}
		/*
		def bpTotal = bonos.sum 0.0, {it.bonoPreliminar }
		bonos.each { b -> 
			def part = b.bonoPreliminar * 100 / bpTotal
			def monto = montoTotal * (part / 100)
			b.bono = monto
			b.factorParaBono = factorParaBono
			b.montoBonoTotal = montoTotal
			
		}
		*/	

	}

	def actualizarImportes(Integer ejercicio) {
		List bonos = Bono.where { ejercicio == ejercicio}.list()
		def bpTotal = bonos.sum 0.0, {it.bonoPreliminar }

		
		bonos.each { b -> 
			
			//b.bonoPreliminar = b.ptu * (b.porcentajeBono / 100)

			def montoTotal = b.montoBonoTotal
			def part = b.bonoPreliminar * 100 / bpTotal
			def monto = montoTotal * (part / 100)
			b.bono = monto
			b.totalGravable = b.bono
			calcularImpuestos(b)
			calcularPago(b)
			b.save flush: true
		}
	}

	def generar(Empleado empleado, Integer ejercicio, String tipo = 'ESPECIAL_PA'){

		def cal = getCalendario(empleado, ejercicio)
		
		if(cal == null )
			return 
		def periodo = cal.asistencia

		// Validacines iniciales
		/*
		if(empleado.alta > periodo.fechaInicial){
			return
		}
		if(empleado.baja) {
			if(empleado.baja.fecha > empleado.alta) {
				return
			}
		}
		*/
		def bono = Bono.find{ejercicio == ejercicio && empleado == empleado && tipo == tipo}
		if(!bono){
			bono = new Bono(empleado:empleado,ejercicio:ejercicio, tipo: tipo)
			bono.fechaInicial=periodo.fechaInicial
			bono.fechaFinal=periodo.fechaFinal
			bono.salario = empleado.salario.salarioDiario
			if(bono.empleado.salario.salarioDiario <= 0.0){
				bono.salario = bono.empleado.salario.salarioVariable
			}
			bono.sueldoMensual = empleado.salario.periodicidad == 'SEMANAL' ? bono.salario * 31 : bono.salario * 32
			bono.incentivo=bono.sueldoMensual*0.1
			bono.save failOnError:true, flush:true
		}
		return bono
	}


    def calcular(Bono bono) {
		
		registrarFaltas(bono)
    	
    	if(!bono.manual){
    		def factorGeneral = bono.faltasIncapacidadesFactor + bono.retardoFactor
    		if(bono.productividad) {
    			factorGeneral = (factorGeneral + bono.productividadFactor) / 3
			} else {
				factorGeneral = factorGeneral  / 2
			}
    		bono.porcentajeBono = factorGeneral
    	}
		// bono.bonoPreliminar = bono.bonoInicial * (bono.porcentajeBono / 100)
		// bono.save flush: true, failOnError:true
    }

	def calS 
	def calQ

	def getCalendario(Empleado empleado, Integer ejercicio){
		if(calS==null){
			calS = CalendarioDet.where{calendario.tipo == 'SEMANA' && calendario.comentario == 'ESPECIAL_PA' && calendario.ejercicio ==  ejercicio}.first()
		}
		if(calQ == null){
			calQ = CalendarioDet.where{calendario.tipo == 'QUINCENA' && calendario.comentario == 'ESPECIAL_PA' && calendario.ejercicio == ejercicio}.first()
		}
		if(empleado.salario.periodicidad == 'SEMANAL')
			return calS
		else if (empleado.salario.periodicidad == 'QUINCENAL')
			return calQ
		else 
			return null
	}
    

    def registrarFaltas(Bono a){
		
		if(a.manual) 
			return
		
		/** FALTAS ****/		
		def rows = AsistenciaDet
			.findAll("from AsistenciaDet a where a.asistencia.empleado=? and date(a.fecha) between ? and ?",
			[a.empleado, a.fechaInicial, a.fechaFinal])
		def faltas = rows.count{(it.tipo == 'FALTA' || it.tipo == 'INCIDENCIA_F') && it.asistencia.empleado.controlDeAsistencia }
		a.faltas = faltas
		def incapacidades = rows.count{it.tipo == 'INCAPACIDAD'}		
		a.incapacidades = incapacidades
		faltas = faltas + incapacidades

		if(faltas <= 3) {
			a.faltasIncapacidadesFactor = 100
		} else if( faltas > 3 && faltas <= 6 ) {
			a.faltasIncapacidadesFactor = 66
		} else if (faltas > 6 && faltas <= 9 ) {
			a.faltasIncapacidadesFactor = 33
		} else if( faltas > 9) {
			a.faltasIncapacidadesFactor = 0.0
		}

		/** RETARDOS ****/
		def retardos = rows.sum { it.retardoMenor + it.retardoMayor + it.retardoComida + it.retardoMenorComida + it.minutosNoLaborados}
		def asistencias = Asistencia
			.findAll("from Asistencia a where a.empleado=? and date(a.calendarioDet.asistencia.fechaInicial)>= ?  and  date(a.calendarioDet.asistencia.fechaFinal) <=?",
			[a.empleado,a.fechaInicial,a.fechaFinal])
		def minutosPorDescontar = asistencias.sum{it.minutosPorDescontar}
		retardos = retardos + minutosPorDescontar
		a.retardos = retardos

		if(retardos <= 200) {
			a.retardoFactor = 100
		} else if( retardos > 200 && retardos <= 400 ) {
			a.retardoFactor = 66
		} else if (retardos > 400 && retardos <= 600 ) {
			a.retardoFactor = 33
		} else if( retardos > 600) {
			a.retardoFactor = 0.0
		}
	}
	
	def prepararBaseMensualParaISR(Bono bono){
		def diasDelEjercicioReales = bono.fechaFinal - bono.fechaInicial+1
		bono.promedioGravable = (bono.bono / diasDelEjercicioReales) * 30.4
		bono.proporcionPromedioMensual = bono.promedioGravable + bono.sueldoMensual + bono.ptuGravado + bono.incentivo
	}
	
	def calcularImpuestos(Bono a){
		prepararBaseMensualParaISR(a)
		a.isrMensual = calcularImpuesto(a.sueldoMensual + a.ptuGravado + a.incentivo)
		a.isrPromedio = calcularImpuesto(a.proporcionPromedioMensual)
		a.difIsrMensualPromedio = a.isrPromedio - a.isrMensual
		a.tasa = a.difIsrMensualPromedio <=0 ? 0.0 : (a.difIsrMensualPromedio/a.promedioGravable).setScale(4,RoundingMode.HALF_EVEN)
		a.isrPorRetener = (a.tasa * a.totalGravable).setScale(2,RoundingMode.HALF_EVEN)
		
		def subsidio=Subsidio.obtenerTabla(30.4).find(){((a.sueldoMensual + a.incentivo + a.ptuGravado)> it.desde && (a.sueldoMensual + a.incentivo + a.ptuGravado) <= it.hasta)}
		a.subsidio = subsidio.subsidio
		a.isrOSubsidio = a.isrMensual - a.subsidio
		a.resultadoIsrSubsidio = a.isrPorRetener + a.isrOSubsidio
		
		//Tabla normal ISR
		//def tablaNormalIsrSub=
		def t1 = a.totalGravable + a.sueldoMensual + a.ptuGravado + a.incentivo
		def t1_isr = calcularImpuesto(t1)
		
		def t1_sub = Subsidio.obtenerTabla(30.4).find(){(t1 > it.desde && t1 <= it.hasta)}.subsidio
		a.tablaNormalIsrSub = t1_isr-t1_sub
		a.beneficioPerjuicio = a.resultadoIsrSubsidio - a.tablaNormalIsrSub
	}
	
	private BigDecimal calcularImpuesto(BigDecimal percepciones){
		def tarifa =TarifaIsr.obtenerTabla(30.4)
		.find(){(percepciones>it.limiteInferior && percepciones<=it.limiteSuperior)}
		def importeGravado=percepciones-tarifa.limiteInferior
		importeGravado*=tarifa.porcentaje
		importeGravado/=100
		importeGravado+=tarifa.cuotaFija
		importeGravado=importeGravado.setScale(2,RoundingMode.HALF_EVEN)
		return importeGravado
	}
	
	def calcularPago(Bono a){
		a.subTotal = a.totalGravable - a.isrPorRetener - a.isrEjerAnt
		def percepcion = a.subTotal
		def pension = buscarPension(a.empleado)
		if(pension) {
			def importeP = 0.0
			if(!pension.neto) {
				importeP = (a.totalGravable + a.aguinaldoExcento) * (pension.porcentaje / 100)
			}else{
				importeP = (a.subTotal) * (pension.porcentaje / 100)
			}
			a.pensionA = importeP
			percepcion -= importeP
		}
		percepcion *= 0.75
		
		def otraDeduccion = buscarOtrasDeducciones(a.empleado)
		
		if(otraDeduccion) {
			if(otraDeduccion.saldo <= percepcion) {
				a.otrasDed = otraDeduccion.saldo
				percepcion -= otraDeduccion.saldo
			}else {
				a.otrasDed = percepcion
			}
		} else {
			a.otrasDed = 0.0
		}
		
		def prestamos=buscarPrestamos(a.empleado)
		
		def saldo = prestamos.sum(0.0, {it.saldo})
		
		if(saldo) {
			if(saldo <= percepcion) {
				a.prestamo = saldo
			}else {
				a.prestamo = percepcion
			}
		}else {
			a.prestamo = 0.0
		}
		a.netoPagado = a.subTotal - a.pensionA - a.otrasDed - a.prestamo
		
	}

	

	def buscarAsistencias(Aguinaldo aguinaldo){
		def c = getCalendario(aguinaldo)
		def p = c.asistencia
		
		def rows = AsistenciaDet
		.findAll("from AsistenciaDet a where a.asistencia.empleado=? and date(a.fecha) between ? and ?",
		[aguinaldo.empleado,p.fechaInicial,p.fechaFinal])
		def res = []
		res.addAll(rows.findAll{it.tipo == 'FALTA' || it.tipo == 'INCIDENCIA_F'}.sort{it.fecha})
		res.addAll(rows.findAll{it.tipo == 'INCIDENCIA' && it.comentario == 'INCIDENCIA PERMISO_P'}.sort({it.fecha}))
		res.addAll(rows.findAll{it.tipo == 'INCAPACIDAD' }.sort({it.fecha}))
		return res
	}
	
	private PensionAlimenticia buscarPension(Empleado e) {
		def pensiones=PensionAlimenticia.findAll("from PensionAlimenticia p where p.empleado=?"
			,[e],[max:1])
		return pensiones?pensiones[0]:null
	}
	
	private Prestamo buscarPrestamo(Empleado e) {
		def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>0 and p.empleado=? order by p.saldo desc"
			,[e],[max:1])
		return prestamos?prestamos[0]:null
	}

	private def buscarPrestamos(Empleado e) {
		def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>0 and p.empleado=? order by p.saldo desc"
			,[e])
		return prestamos
	}
	
	private OtraDeduccion buscarOtrasDeducciones(Empleado e){
		def d=OtraDeduccion.findAll("from OtraDeduccion d where d.saldo>0 and d.empleado=? order by d.saldo desc"
			,[e],[max:1])
		return d?d[0]:null
	}

	Empresa getEmpresa(){
		if(!empresa){
			empresa = Empresa.first()
		}
		return empresa
	}
}
