package com.luxsoft.sw4.rh.imss

import java.math.RoundingMode

class TarifaAnualIsr {
	
	BigDecimal limiteInferior
	BigDecimal limiteSuperior
	BigDecimal cuotaFija
	BigDecimal porcentaje
	
	static def valores=[
		
			new TarifaAnualIsr(limiteInferior:0.01,limiteSuperior:6942.20,cuotaFija:0,porcentaje:1.92),
			new TarifaAnualIsr(limiteInferior:6942.21,limiteSuperior:58922.16,cuotaFija:133.28,porcentaje:6.4),
			new TarifaAnualIsr(limiteInferior:58922.17,limiteSuperior:103550.44,cuotaFija:3460.01,porcentaje:10.88),
			new TarifaAnualIsr(limiteInferior:103550.45,limiteSuperior:120372.83,cuotaFija:8315.57,porcentaje:16),
			new TarifaAnualIsr(limiteInferior:120372.84,limiteSuperior:144119.23,cuotaFija:11007.14,porcentaje:17.92),
			new TarifaAnualIsr(limiteInferior:144119.24,limiteSuperior:290667.75,cuotaFija:15262.49,porcentaje:21.36),
			new TarifaAnualIsr(limiteInferior:290667.76,limiteSuperior:458132.29,cuotaFija:46565.26,porcentaje:23.52),
			new TarifaAnualIsr(limiteInferior:458132.30,limiteSuperior:874650.00,cuotaFija:85952.92,porcentaje:30),
			new TarifaAnualIsr(limiteInferior:874650.01,limiteSuperior:1166200.00,cuotaFija:210908.23,porcentaje:32),
			new TarifaAnualIsr(limiteInferior:1166200.01,limiteSuperior:3498600.00,cuotaFija:304204.21,porcentaje:34),
			new TarifaAnualIsr(limiteInferior:33498600.01,limiteSuperior:99999999.99,cuotaFija:1097220.21,porcentaje:35)


	]
	
	static diasMap=[:]
	
	
	
	static TarifaAnualIsr buscar(def valor){
		return valores.find(){ it ->
			(valor>it.limiteInferior && valor<=it.limiteSuperior)
		}
	}
	
	String toString(){
		return "$limiteInferior $limiteSuperior $cuotaFija $porcentaje"
	}

}
