import grails.util.Environment
import org.springframework.web.servlet.i18n.FixedLocaleResolver

// Place your Spring DSL code here
beans = {
	
	
	
	localeResolver(FixedLocaleResolver,Locale.US){
		defaultLocale=new Locale("es","mx")
		Locale.setDefault(defaultLocale)
	}
	
	
	
}
