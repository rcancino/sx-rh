import com.luxsoft.sx4.sec.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sec.User
import com.luxsoft.sec.UserRole
import com.luxsoft.sec.Role
import grails.util.Environment

class BootStrap {

    def cfdiRetencionesService

    def init = { servletContext ->
    	//Zonas economicas por defecto
		def zonaA=ZonaEconomica.findOrSaveWhere(clave:'A')
		def zonaB=ZonaEconomica.findOrSaveWhere(clave:'B')
		def zonaC=ZonaEconomica.findOrSaveWhere(clave:'C')
		def zonaD=ZonaEconomica.findOrSaveWhere(clave:'D')

		Date.metaClass.inicioDeMes{ ->
			def d1=delegate.clone()
			d1.putAt(Calendar.DATE,1)
			return d1.clearTime()
		
		}
		
		Date.metaClass.finDeMes{ ->
			Calendar c2=delegate.clone().toCalendar()
			c2.putAt(Calendar.DATE,c2.getActualMaximum(Calendar.DATE))
			return c2.getTime().clearTime()
		}
			
		Date.metaClass.text{ ->
			return delegate.format('dd/MM/yyyy')
		}
		
		Date.metaClass.toMonth{ ->
			return delegate.getAt(Calendar.MONTH)+1
		}
		Date.metaClass.toYear{
			return delegate.getAt(Calendar.YEAR)
		}
		Date.metaClass.asPeriodoText{
			return delegate.format('MMMM - yyyy')
		}
	    
	    Date.metaClass.isSameMonth{ fecha ->
	        return  (delegate.getAt(Calendar.ERA)==fecha.getAt(Calendar.ERA)  && 
	        		 delegate.getAt(Calendar.YEAR)==fecha.getAt(Calendar.YEAR)  && 
	        		 delegate.getAt(Calendar.MONTH)==fecha.getAt(Calendar.MONTH)
	        		 )
	        
	    }
        java.security.Security.addProvider(new BouncyCastleProvider())

        /*
        def admin2=User.findByUsername('admin2')
        if(!admin2){
            def userRole=Role.findWhere(authority:'USUARIO')
            def adminRole=Role.findWhere(authority:'ADMIN')
            admin2=new User(username:'admin2'
                ,password:'admin2'
                ,apellidoPaterno:'admin2'
                ,apellidoMaterno:'admin2'
                ,nombres:'admin2'
                ,nombre:' ADMIN2 ADMIN2'
                ,numeroDeEmpleado:'10000')
            .save(flush:true,failOnError:true)
            UserRole.create(admin2,userRole,true)
            UserRole.create(admin2,adminRole,true)
        }
        */
       	if(Environment.current == Environment.DEVELOPMENT) {
       		def admin = User.findByUsername('admin')
        	admin.password = 123
        	admin.save(flush:true,failOnError:true)
       	}
        
    }
    
    def destroy = {
    }
}
