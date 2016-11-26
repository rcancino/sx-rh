package com.luxsoft.sw4.bi

import static org.springframework.http.HttpStatus.*
import org.springframework.security.access.annotation.Secured



@Secured(["hasAnyRole('ROLE_ADMIN')"])
class EmpleadoBiController {
    
    def index(){
    	redirect action:'edit'
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'empresa.label', default: 'Empresa'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
    
}

