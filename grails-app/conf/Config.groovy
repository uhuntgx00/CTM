import mil.ebs.ctm.ldap.LdapPerson

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

def ENV_NAME = "ctm-config"

if(!grails.config.locations || !(grails.config.locations instanceof List)) {
    grails.config.locations = []
}

println "--------------------------------------------------------"
if(System.getenv(ENV_NAME)) {
    println "Including configuration file specified in environment: " + System.getenv(ENV_NAME);
    grails.config.locations << "file:" + System.getenv(ENV_NAME)
} else if(System.getProperty(ENV_NAME)) {
    println "Including configuration file specified on command line: " + System.getProperty(ENV_NAME);
    grails.config.locations << "file:" + System.getProperty(ENV_NAME)
} else {
    println "No external configuration file defined."
}
println "--------------------------------------------------------"

grails.configURL = "http://localhost:8890/CTM/"

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

grails.doc.title = "Conference Tracking & Management (CTM)"
grails.doc.authors = "Guy Hunter"
grails.doc.images = new File("src/docs/images")

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    excel:         'application/vnd.ms-excel',
    form:          'application/x-www-form-urlencoded',
    hal:           ['application/hal+json','application/hal+xml'],
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    ods:           'application/vnd.oasis.opendocument.spreadsheet',
    pdf:           'application/pdf',
    rss:           'application/rss+xml',
    rtf:           'application/rtf',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

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
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
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

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

//    appenders {
//        rollingFile name:"myAppender", maxFileSize:4096, fileName:"/ctm/ctm.log", layout: pattern(conversionPattern: '%d{dd MMM yyyy HH:mm:ss} %-5p %c{2} %x - %m%n')
//    }

    root {
        error 'stdout', 'myAppender'
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
}


// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'mil.ebs.ctm.Account'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'mil.ebs.ctm.AccountRole'
grails.plugin.springsecurity.authority.className = 'mil.ebs.ctm.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['permitAll'],
	'/index':                         ['permitAll'],
	'/index.gsp':                     ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/favicon.ico':                ['permitAll'],
	'/searchable':                    ['permitAll'],
    '/searchable/':                   ['permitAll'],
    '/searchable/**':                 ['permitAll']
]

grails.plugin.springsecurity.useX509 = true

jasypt {
    encryptorRegisteredName = "gormEncryptor"
//    algorithm = "PBEWITHSHA256AND256BITAES-CBC-BC"
//    providerName = "BC"
//    password = "S^p#rS#cr#t^ltr@P@ssw0rd"
//    keyObtentionIterations = 1000
}

//ldap {
//    directories {
//        person {
//            url = "ldap://134.131.28.148:389"
//            base = "ou=people,dc=sso,dc=afrl,dc=af,dc=mil"
//            userDn = "uid=claritysyncuser,dc=sso,dc=afrl,dc=af,dc=mil"
//            userName = "uid=claritysyncuser,dc=sso,dc=afrl,dc=af,dc=mil"
//            password = "C1r!TyS1nk1ngY00s3r"
//            searchControls {
//                countLimit = 25000
////                countLimit = 25000
//                timeLimit = 60000
//                searchScope = "subtree"
//            }
//        }
//    }
//
//    schemas = [
//        LdapPerson
//    ]
//}

