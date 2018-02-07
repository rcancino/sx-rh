import grails.util.Environment
import org.springframework.web.servlet.i18n.FixedLocaleResolver

import com.luxsoft.sw4.cfdi.CfdiCadenaBuilder
import com.luxsoft.sw4.cfdi.CfdiSellador
import com.luxsoft.sw4.cfdi.CfdiTimbrador
import com.luxsoft.sw4.rh.ConceptoDeNominaRuleResolver
import com.luxsoft.sw4.rh.ProcesadorDeISTP
import com.luxsoft.sw4.rh.ProcesadorDeInfonavit
import com.luxsoft.sw4.rh.ProcesadorDeFonacot
import com.luxsoft.sw4.rh.ProcesadorDeNomina
import com.luxsoft.sw4.rh.ProcesadorDePensionAlimenticia;
import com.luxsoft.sw4.rh.ProcesadorDePrestamosPersonales
import com.luxsoft.sw4.rh.ProcesadorDePrimaVacacional
import com.luxsoft.sw4.rh.ProcesadorDeSueldo
import com.luxsoft.sw4.rh.ProcesadorDeVacaciones
import com.luxsoft.sw4.rh.ProcesadorRetardoPermiso;
import com.luxsoft.sw4.rh.ProcesadorSeguroSocial
import com.luxsoft.sw4.rh.ProcesadorDeIncentivo
import com.luxsoft.sw4.rh.ProcesadorDePaternidad
import com.luxsoft.sw4.rh.ProcesadorDeOtrasDeducciones
import com.luxsoft.sw4.rh.ProcesadorDePagoDeComisiones
import com.luxsoft.sw4.rh.ProcesadorDeAjusteISPT
import com.luxsoft.sw4.rh.ProcesadorDeTiempoExtra
import com.luxsoft.sw4.rh.procesadores.*
import com.luxsoft.sw4.rh.ProcesadorDePercepcionGenerica
import com.luxsoft.sw4.rh.ProcesadorDeDeduccionGenerica
import com.luxsoft.sw4.rh.RevisionAguinaldoExport
import com.luxsoft.sw4.rh.ProcesadorSeguroSocialFiniquito
import com.luxsoft.sw4.rh.ProcesadorDeISTPLiquidacion

import com.luxsoft.sw4.rh.ExportadorDim

import com.luxsoft.sw4.cfdi.nomina12.*

import com.luxsoft.cfdix.v33.*

// Place your Spring DSL code here
beans = {
	
	localeResolver(FixedLocaleResolver,Locale.US){
		defaultLocale=new Locale("es","mx")
		Locale.setDefault(defaultLocale)
	}

	switch(Environment.current){
			case Environment.PRODUCTION:
				cfdiTimbrador(CfdiTimbrador){
					timbradoDePrueba = false
				}
				break
			case Environment.DEVELOPMENT:
				cfdiTimbrador(CfdiTimbrador){
					timbradoDePrueba=true
				}
				break
			case Environment.TEST:
				cfdiTimbrador(CfdiTimbrador){
				timbradoDePrueba=true
			}
	}
		
	cfdiCadenaBuilder(CfdiCadenaBuilder){
		//xsltFileName="web-app/sat/cadenaoriginal_3_2.xslt"
	}

	cfdiSellador(CfdiSellador){
		cadenaBuilder=ref("cfdiCadenaBuilder")
	}
		
	procesadorDeNomina(ProcesadorDeNomina){
		reglas=[
			ref('procesadorDeSueldo'),
			ref('procesadorDePagoDeComisiones'),
			ref('procesadorDePercepcionGenerica'),
			ref('procesadorDePaternidad'),
			ref('procesadorDeVacaciones'),
			ref('procesadorDePrimaVacacional'),
			ref('procesadorDeIncentivo'),
			ref('procesadorDeTiempoExtra'),
			ref('procesadorSeguroSocial'),
			ref('procesadorDeDeduccionGenerica'),
			ref('procesadorRetardoPermiso'),
			ref('procesadorDeISTP'),
			ref('procesadorDeAjusteISPT'),
			ref('procesadorDeInfonavit'),
			ref('procesadorDeFonacot'),
			ref('prcoesadorDePensionAlimenticia'),
			ref('procesadorDeOtrasDeducciones'),
			ref('procesadorDePrestamosPersonales')
			
			]
	}
		
	conceptoDeNominaRuleResolver(ConceptoDeNominaRuleResolver){
		ruleMap=['P001':ref('procesadorDeSueldo'),
				 'D002':ref('procesadorDeISTP'),
				 'P021':ref('procesadorDeISTP'),
				 'P010':ref('procesadorDeIncentivo'),
				 'P025':ref('procesadorDeVacaciones'),
				 'P024':ref('procesadorDePrimaVacacional'),
				 'D004':ref('procesadorDePrestamosPersonales'),
				 'D012':ref('procesadorRetardoPermiso'),
				 'P032':ref('procesadorDePaternidad'),
				 'D006':ref('procesadorDeInfonavit'),
				 'D014':ref('procesadorDeFonacot'),
				 'D005':ref('procesadorDeOtrasDeducciones'),
				 'P029':ref('procesadorDePagoDeComisiones'),
				 'D001':ref('procesadorSeguroSocial')
				 ]
	}
		
	procesadorDeSueldo(ProcesadorDeSueldo){}

	procesadorDeISTP(ProcesadorDeISTP){}
	procesadorSeguroSocial(ProcesadorSeguroSocial){}
	procesadorSeguroSocialFiniquito(ProcesadorSeguroSocialFiniquito){}
	procesadorDeIncentivo(ProcesadorDeIncentivo){}
	procesadorDeVacaciones(ProcesadorDeVacaciones){}
	procesadorDePrimaVacacional(ProcesadorDePrimaVacacional){}
	procesadorDeInfonavit(ProcesadorDeInfonavit){}
	procesadorDeFonacot(ProcesadorDeFonacot){}
	procesadorDePrestamosPersonales(ProcesadorDePrestamosPersonales){}
	procesadorRetardoPermiso(ProcesadorRetardoPermiso)
	procesadorDePaternidad(ProcesadorDePaternidad)
	procesadorDeChecadas(ProcesadorDeChecadas){}
	prcoesadorDePensionAlimenticia(ProcesadorDePensionAlimenticia){}
	procesadorDeOtrasDeducciones(ProcesadorDeOtrasDeducciones){}
	procesadorDePagoDeComisiones(ProcesadorDePagoDeComisiones){}
	procesadorDeAjusteISPT(ProcesadorDeAjusteISPT){}
	procesadorDeTiempoExtra(ProcesadorDeTiempoExtra){}
	ajusteIsr(AjusteIsr){}

	procesadorDePercepcionGenerica(ProcesadorDePercepcionGenerica){}
	procesadorDeDeduccionGenerica(ProcesadorDeDeduccionGenerica){}
	procesadorDeISTPLiquidacion(ProcesadorDeISTPLiquidacion){}
	
	exportadorDim(ExportadorDim){}
	procesadorDeChecadasFaltantes(ProcesadorDeChecadasFaltantes){}
	revisionAguinaldoExport(RevisionAguinaldoExport){
		dataSource: ref('dataSource')
	}

	/** Complemento de nomina 1.2 para CFDI */

	cfdiBuilder(CfdiBuilder){}

	nominaPrintService(NominaPrintService){}
	

	procesadorDeNominaFiniquito(ProcesadorDeNomina){
		reglas=[
			ref('procesadorSeguroSocialFiniquito'),
			ref('procesadorDeISTP'),
			ref('procesadorDeAjusteISPT'),
			ref('prcoesadorDePensionAlimenticia'),
			ref('procesadorDeOtrasDeducciones'),
			ref('procesadorDePrestamosPersonales')
			]
	}

	procesadorDeNominaLiquidacion(ProcesadorDeNomina){
		reglas=[
			//ref('procesadorDeISTP'),
			//ref('procesadorDeAjusteISPT'),
			ref('procesadorDeISTPLiquidacion'),
			ref('prcoesadorDePensionAlimenticia'),
			ref('procesadorDeOtrasDeducciones'),
			ref('procesadorDePrestamosPersonales')
			]
	}

	procesadorDeNominaEspecial(ProcesadorDeNomina){
		reglas=[
			ref('procesadorDeISTP'),
			ref('procesadorDeAjusteISPT'),
			ref('prcoesadorDePensionAlimenticia'),
			ref('procesadorDeOtrasDeducciones'),
			ref('procesadorDePrestamosPersonales')
			]
	}

	// CFDI 3.3

	cfdiCadenaBuilder33(CfdiCadenaBuilder33){}

	cfdiSellador33(CfdiSellador33){
		cfdiCadenaBuilder33 = ref('cfdiCadenaBuilder33')
	}	

	v33NominaPrintService(V33NominaPrintService){}

	
}
