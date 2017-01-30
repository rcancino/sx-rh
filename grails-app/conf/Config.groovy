import grails.plugin.springsecurity.SpringSecurityUtils
// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }
//grails.config.locations=["file:${userHome}/.grails/${appName}-config.groovy"] 

grails.project.groupId = 'com.luxsoft.impapx' // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

// environments {
//     development {
//         grails.logging.jul.usebridge = true
//     }
//     production {
//         grails.logging.jul.usebridge = false
//         // TODO: grails.serverURL = "http://www.changeme.com"
//     }
// }
//grails.plugin.springsecurity.auth.loginFormUrl='/login/login2'

environments {
    development {
        grails.logging.jul.usebridge = true
        grails.plugin.springsecurity.debug.useFilter = true
        grails.plugin.springsecurity.active = true
        //sw4.rh.asistencia.rawdata="Y://NOMIPLUS//RAWDATA"
        sw4.rh.asistencia.rawdata="/Users/rcancino/dumps/rh/RAWDATA"
    }
    production {
        grails.logging.jul.usebridge = false
        sw4.rh.asistencia.rawdata="/mnt/RAWDATA/NOMIPLUS/RAWDATA"
    }
    test{
        grails.plugin.springsecurity.debug.useFilter = false
        grails.plugin.springsecurity.active = false
    }
    
}


// log4j configuration
log4j.main = {
    
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%-5p  %c{1}: %m%n')
        //console name:'stdout', layout:pattern(conversionPattern: '%-5p [%t] %c{1}: %m%n')

        // rollingFile name:'luxorFileAppender',
        //             maxFileSize:1024,
        //             file:"$logDir/kyo-luxor.log",
        //             maxBackupIndex:7
        //             layout: pattern(conversionPattern: '%-5p [%t] %c{1}: %m%n')
    }
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%-5p [%c{1}] %m%n')
        file name:'file', file:System.properties['user.home']  + '/.grails/lx-imports.log',
             layout: pattern(conversionPattern: '%-5p [%t] %c{1}: %m%n')
        rollingFile name:'importacionLog',
                    maxFileSize:'1MB',
                    file:System.properties['user.home']  + '/.grails/lx-imports.log',
                    maxBackupIndex:7,
                    layout: pattern(conversionPattern: '%-5p %d{DATE} [%c{1}] %m%n')

    }

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
    environments{
        
      development{
        
        info  'grails.app.controllers.com.luxsoft'
        info  'grails.app.controllers.lx.econta'
        info  'grails.app.services.com.luxsoft'
        info  'grails.app.jobs'
        debug 'com.luxsoft.impapx'
        debug 'com.luxsoft.econta.polizas'
        info 'grails.app.services.com.luxsoft.sw4'
        info 'grails.app.services.com.luxsoft.rh'
        debug 'grails.app.services.com.luxsoft.rh.AsistenciaService'
        info 'grails.app.services.com.luxsoft.rh.IncentivoService'
        info 'grails.app.services.com.luxsoft.rh.NominaService'
        info 'grails.app.services.com.luxsoft.sw4.rh.IncentivoService'
        info 'grails.app.services.com.luxsoft.sw4.rh.InfonavitService'
        info 'grails.app.com.luxsoft.sw4.rh.TiempoExtraService'
        info 'grails.app.services.com.luxsoft.rh.AguinaldoService'
        debug 'grails.app.service.com.luxsoft.sw4.rh.PtuService'
        off 'com.luxsoft.sw4.rh.ProcesadorDeSueldo'
        off 'com.luxsoft.sw4.rh.procesadores.ProcesadorDeChecadas'
        off 'com.luxsoft.sw4.rh.procesadores.AjusteMensualISR'
        debug 'com.luxsoft.sw4.rh.ProcesadorSeguroSocial'
        off 'com.luxsoft.sw4.rh.ProcesadorDeISTP'
        off 'com.luxsoft.sw4.rh.ProcesadorDeVacaciones'
        off 'com.luxsoft.sw4.rh.ProcesadorDePrestamosPersonales'
        off 'com.luxsoft.sw4.rh.ProcesadorDeIncentivo'
        off 'com.luxsoft.sw4.rh.ProcesadorDePensionAlimenticia'
        off 'com.luxsoft.sw4.rh.ProcesadorDeOtrasDeducciones'
        info 'com.luxsoft.sw4.rh.procesadores.AjusteIsr'
        info 'com.luxsoft.sw4.rh.finiquito'
        off 'com.luxsoft.sw4.rh.ExportadorDim'
        debug 'com.luxsoft.sw4.rh.procesadores.ProcesadorDeChecadasFaltantes'

      }
        
      produccion{
        error 'grails.app.services'
        error 'grails.app.controllers'
        error 'grails.app.jobs'    
      }
        
    }
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.securityConfigType = "Annotation"
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.luxsoft.sec.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.luxsoft.sec.UserRole'
grails.plugin.springsecurity.authority.className = 'com.luxsoft.sec.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                       ['permitAll'],
	'/index':                  ['permitAll'],
	'/index.gsp':              ['permitAll'],
	'/assets/**':              ['permitAll'],
	'/**/js/**':               ['permitAll'],
	'/**/css/**':              ['permitAll'],
	'/**/images/**':           ['permitAll'],
	'/**/favicon.ico':         ['permitAll'],
  '/login/**':               ['permitAll'],
  '/logout/**':                     ['permitAll'],
  '/contabilidad/**':               ["hasAnyRole('ROLE_ADMIN','CONTABILIDAD')"],
  "/console/**":                    ["hasAnyRole('ROLE_ADMIN')"],
  "/plugins/console*/**":           ["hasAnyRole('ROLE_ADMIN')"],
  '/jasper/**':                     ['RH_USER','ROLE_ADMIN']

]

grails.plugin.springsecurity.roleHierarchy = '''
   RH_MANAGER > RH_USER
   ROLE_ADMIN > RH_MANAGER
'''
grails.plugin.springsecurity.failureHandler.exceptionMappings = [
   'org.springframework.security.authentication.CredentialsExpiredException': '/usuario/passwordExpired'
]
grails.plugin.springsecurity.apf.storeLastUsername=true



proveedorOrigenParaCompras=42
environments{
    
  development{
    luxor{
        empleadosDb{
          url = 'jdbc:mysql://10.10.1.228:3306/rh'
          username = "root"
          password = "sys"
        }
        
    }
    // grails.assets.bundle=true
  }
    
  production{
    luxor{
        empleadosDb{
          url = 'jdbc:mysql://10.10.1.228:3306/rh'
          username = "root"
          password = "sys"
        }
    }
  }
  impapx2{
    proveedorOrigenParaCompras=60
  }
}
beans {
   cacheManager {
      shared = true
  }
}

grails.plugin.databasemigration.updateOnStart = false
grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']


grails.databinding.dateFormats = ['dd/MM/yyyy', 'dd-MM-yyyy HH:mm:ss.S', "dd-MM-yyyy'T'hh:mm:ss'Z'"]


environments {
    test {
        grails.plugin.databasemigration.updateOnStart = false
        //grails.plugin.databasemigration.autoMigrateScripts = ['RunApp','TestApp']
        //grails.plugin.databasemigration.forceAutoMigrate = true
        //grails.plugin.databasemigration.dropOnStart = true
        // if (loadTestData) {
        //     grails.plugin.databasemigration.updateOnStartFileNames = ['testchangelog.groovy', 'testdata.groovy']
        // } else {
        //     grails.plugin.databasemigration.updateOnStartFileNames = ['testchangelog.groovy']
        // }
    }
}

auditLog {
  //verbose = true // verbosely log all changed values to db
  logIds = true  // log db-ids of associated objects.
   // Note: if you change next 2 properties, you must update your database schema!       
  //tablename = 'my_audit' // table name for audit logs.     
  //largeValueColumnTypes = true // use large column db types for oldValue/newValue.
  TRUNCATE_LENGTH = 1000
  cacheDisabled = true
  //logFullClassName = true
  //replacementPatterns = ["local.example.xyz.":""] // replace with empty string.
  actorClosure = { request, session ->

    if (request.applicationContext.springSecurityService.principal instanceof java.lang.String){
      return request.applicationContext.springSecurityService.principal
    }
    //def username = request.applicationContext.springSecurityService.principal?.username
    def username = request.applicationContext.springSecurityService.getCurrentUser()?.nombre
    if (SpringSecurityUtils.isSwitched()){
      username = SpringSecurityUtils.switchedUserOriginalUsername+" AS "+username
    }
    return username
  }
  stampEnabled = true
  //stampAlways = false
  stampCreatedBy = 'createdBy' // fieldname
  stampLastUpdatedBy = 'lastUpdatedBy' // fieldname

}
cxf{
  client{
    consultaService{
      wsdl="https://consultaqr.facturaelectronica.sat.gob.mx/ConsultaCFDIService.svc?singleWsdl"
      namespace="com.luxsoft.cfdi"
      //bindingFile = "grails-app/conf/bindings.xml"
      //client = false //defaults to false
      //allowChunking = true //false
      clientInterface = com.luxsoft.cfdi.IConsultaCFDIService
      serviceEndpointAddress = "https://consultaqr.facturaelectronica.sat.gob.mx/ConsultaCFDIService.svc"
      receiveTimeout = 120000 //2min
      connectionTimeout = 120000 //2min
    }
    
  }
}



