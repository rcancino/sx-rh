package com.luxsoft.sw4.rh.tablas

import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class ZonaEconomicaController {

      static scaffold = true

    def index(){
    	params.max = 200
		def list=ZonaEconomica.findAll("from ZonaEconomica t where t.ejercicio=? order by t.ejercicio desc,t.desde asc",[session.ejercicio],params)
    	[zonaEconomicaInstanceList:list,zonaEconomicaInstanceCount:ZonaEconomica.count()]
    }
}
