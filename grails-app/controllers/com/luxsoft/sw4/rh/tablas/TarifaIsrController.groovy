package com.luxsoft.sw4.rh.tablas


import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class TarifaIsrController {
    static scaffold = true


    def index(){
    	params.max = 20
		def list=TarifaIsr.findAll("from TarifaIsr t where t.ejercicio=? order by t.ejercicio desc,t.limiteInferior asc",[session.ejercicio],params)
    	[tarifaIsrInstanceList:list,tarifaIsrInstanceCount:TarifaIsr.count()]
    }

    def delete(TarifaIsr tarifa){
    	println 'Eliminando tarifa: '+tarifa
    	tarifa.delete flush:true
    	flash.message="Tarifa eliminada "+tarifa.id
    	redirect action:'index'
    }

    def trasladarTabla(){
        def ejercicio=session.ejercicio
        def ejercicioAnt=ejercicio-1
         def found=TarifaIsr.findAllByEjercicio(ejercicio)
        def tablaIsr=TarifaIsr.findAllByEjercicio(ejercicioAnt).each{
           
            if(!found){
                println "Tarifa: "+it
                TarifaIsr  tarifa=new TarifaIsr(
                    ejercicio:ejercicio,
                    tipo:"MENSUAL",
                    limiteInferior:it.limiteInferior,
                    limiteSuperior:it.limiteSuperior,
                    cuotaFija:it.cuotaFija,
                    porcentaje:it.porcentaje
                )
                
                tarifa.save failOnError:true, flush:true
            }else{
                println "Tabla ya trasladada"
            }
        }

        redirect action:'index'
    }
}
