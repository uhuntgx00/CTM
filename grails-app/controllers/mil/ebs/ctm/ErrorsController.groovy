package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class ErrorsController {

    def error403 = {
        render view: '/error'
    }

    def error500 = {
        render view: '/error'
    }

    def errorSecurity = {
        render view: '/security'
    }

}