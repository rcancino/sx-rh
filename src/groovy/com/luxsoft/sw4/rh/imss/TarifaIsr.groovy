package com.luxsoft.sw4.rh.imss

import java.math.RoundingMode

class TarifaIsr {
	
	BigDecimal limiteInferior
	BigDecimal limiteSuperior
	BigDecimal cuotaFija
	BigDecimal porcentaje
	
	static def valores=[
		
		new TarifaIsr(limiteInferior:0.01,limiteSuperior:578.52,cuotaFija:0,porcentaje:1.92),
		new TarifaIsr(limiteInferior:578.53,limiteSuperior:4910.18,cuotaFija:11.11,porcentaje:6.4),
		new TarifaIsr(limiteInferior:4910.19,limiteSuperior:8629.2,cuotaFija:288.33,porcentaje:10.88),
		new TarifaIsr(limiteInferior:8629.21,limiteSuperior:10031.07,cuotaFija:692.96,porcentaje:16),
		new TarifaIsr(limiteInferior:10031.08,limiteSuperior:12009.94,cuotaFija:917.264,porcentaje:17.92),
		new TarifaIsr(limiteInferior:12009.95,limiteSuperior:24222.31,cuotaFija:1271.871,porcentaje:21.36),
		new TarifaIsr(limiteInferior:24222.32,limiteSuperior:38177.69,cuotaFija:3880.44,porcentaje:23.52),
		new TarifaIsr(limiteInferior:38177.7,limiteSuperior:72887.5,cuotaFija:7162.74,porcentaje:30),
		new TarifaIsr(limiteInferior:72887.51,limiteSuperior:97183.33,cuotaFija:17575.69,porcentaje:32),
		new TarifaIsr(limiteInferior:97183.34,limiteSuperior:291550,cuotaFija:25350.35,porcentaje:34),
		new TarifaIsr(limiteInferior:291550.01,limiteSuperior:250000000.00,cuotaFija:91435.02,porcentaje:35)

	]
	
	static diasMap=[:]
	
	static def obtenerTabla(def diasDelPeriodo){
		def diasMes=30.4
		def res=[] //diasMap[diasDelPeriodo]
		
		valores.each{
			def val=new TarifaIsr(
				limiteInferior:it.limiteInferior>0.01? ((it.limiteInferior/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN):0.01
				,limiteSuperior:((it.limiteSuperior/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				,cuotaFija:((it.cuotaFija/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				,porcentaje:it.porcentaje
				)
			res.add(val)
		}
		return res
	}
	
	static TarifaIsr buscar(def valor){
		return valores.find(){ it ->
			(valor>it.limiteInferior && valor<=it.limiteSuperior)
		}
	}
	
	String toString(){
		return "$limiteInferior $limiteSuperior $cuotaFija $porcentaje"
	}

}
