import com.luxsoft.sx4.sec.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import com.luxsoft.sw4.rh.tablas.ZonaEconomica

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
    }
    
    def destroy = {
    }
}
