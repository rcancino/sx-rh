
import grails.plugin.springsecurity.SpringSecurityUtils



navigation={
	user{
		
		catalogos(){
			
		}
		operaciones(){
			compras(controller:'compra',action:'index')
			embarques(controller:'embarque',action:'index')
			pedimentos(controller:'pedimento',action:'index')
			cuentasDeGastos(controller:'cuentaDeGastos',action:'index')
			cuentasPorPagar(controller:'cuentaPorPagar',action:'index'){
				importaciones()
				gastosDeImportacion()
				otrosGastos()
				requisiciones()
				pagos()
				notasDeCredito()
				detallesDeFactura()
				cuentaDeGastos()
			}
			distribucion(controller:'distribucion',action:'index')
			ventas(controller:'venta',action:'index')
			cfdis(controller:'cfdi',action:'index')
			notasDeCargo(controller:'notaDeCargo',action:'index')
			cuentasPorCobrar(controller:'cuentaPorCobrar',action:'index')
			anticipo(controller:'anticipo',action:'index')
		}
		
		contabilidad(){
			cuentasContables(controller:'cuentaContable',action:'index',titleText:'Cuentas')
			saldos(controller:'saldoPorCuentaContable',action:'index')
			foliosCFDI(controller:'cfdiFolio',action:'index')
			polizas(controller:'poliza',action:'index')
			balanza(controller:'poliza',action:'index')
			reportes(controller:'contabilidad',action:'index')
			cierreAnual(controller:'poliza',action:'cierreAnual')
			contabilidadElectronica(controller:'contabilidadElectronica',action:'index')
			diot(controller:'diot',action:'index',titleText:'DIOT')
		}
		
		tesoreria(){
			tipoDeCambio(controller:'tipoDeCambio',actio:'index')
			bancos(controller:'banco',action:'index')
			cuentas(controller:'cuentaBancaria',action:'index')
			proveedores(controller:'proveedor',action:'index')
			cuentaPorPagar(controller:'cuentaPorPagar',action:'index',titleText:'C x P')
			movimientos(controller:'movimientoDeCuenta',action:'index')
		}
		procesos(){
					empleados()
					//salarioDiarioIntegrado()
					calculoSdi(controller:'calculoSdi',action:'index',titleText:'Calculo bimestral SDI')
					acumulados(controller:'acumuladoPorConcepto',action:'index')
					modificacionSalarial(controller:'modificacionSalarial',action:'index')
					aguinaldo(controller:'aguinaldo',action:'index')
					ptu(controller:'ptu',action:'index',titleText:'PTU')
					calculoAnual(controller:'calculoAnual',action:'index')
					exportador(controller:'exportador',action:'index',titleText:'Layouts')
		}
		configuracion(){
			reglasDeEjecuccion(controller:'brNomina',action:'index',titleText:'Reglas de proceso'){
				porNomina()
				porConcepto()

			}
			concepto(controller:'',action:'index')
			empresa(controller:'empresa', action:'edit')
			
		}
		reportes(controller:'reporte'){
			nomina(controller:'reporte'){
				impuestoSobreNominas(controller:'reporte',titleText:'Impuesto sobre nominas')
				tiempoExtra()
				detallePorConcepto()
				acumuladoDeNominasPorConcepto()
				calificacionDeIncentivos()
				conceptoDeNominaAcumulado()
			}
			salarios(controller:'reporte'){
				historicoDeSalarios(controller:'reporte',titleText:'Historico de salarios')
				incrementosIndividuales()
				
			}
			contratacion(controller:'contratacion'){
				contrato()
				solicitud(titleText:'Solicitud de empleo')
				recepcionDeDocumentos()
				induccion()
				entregaDeDocumentos()
				constanciaDeNoEmbarazo()
				constanciaDeNoFamiliares()
				solicitudDeTarjetaDeNomina()
				/*nuevos*/
				referenciasLaborales()
				actualizacionExpedientesPersonales()
				cambioPuesto()
				dc3(titleText:'DC3')
				entrevistaSalida()
				evaluacionNvoIngreso()
				solicitudPrestamos()
				solicitudVacaciones()
			}
			asistencia(controller:'reporte'){
				bitacoraDeChecado()
				vacacionesEjercicio()
				vacacionesEmpleado()
				incapacidades()
				incapacidadesEmpleado()
				faltasIncapacidades()
				faltasIncapacidadesPeriodo()
				minutosPorPagar()
				ausentismoPorDia()
				programacionDeVacaciones()
				
			}
			
			prestamos(controller:'reporte',titleText:'Prestamos'){
				historicoDePrestamos()
				
			}
					
		}
		exportadores(controller:'exportador'){
			nominaBanamex()
			imss(titleText:'IMSS')
			altasImss()
			bajasImss()
			modificacionesImss()
			modificacionIndividualImss()
			sua(titleText:'SUA')
			trabajadoresSua()
			reporteDeTrabajadores()
			bajasSua()
			reporteDeBajas()
			modificacionBimestralSua()
			reporteDeModificacionBimestral()
			modificacionIndividualSua()
			reporteDeModificacionIndividual()
			ausentismoSua()
			reporteDeAusentismo()
			incapacidadesSua()
			reporteDeIncapacidades(titleText:'Rep. Incapacidades Expedidas Mes')
			incapacidadesSuaDet()
			reporteDiasIncapacidad(titleText:'Rep. Dias Incapacidades Mes')
			infonavitSua()
			reporteDeInfonavit()
			dim(titleText:'DIM Sueldos')
			dimPagosPorSeparacion (titleText:'DIM Pagos Por Separacion')	
			rfc(titleText:'RFC')
			reporteDeRFC()
			
			
		}
		
	}
	
}