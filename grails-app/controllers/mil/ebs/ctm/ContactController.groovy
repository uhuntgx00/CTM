package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured

@Secured(["permitAll"])
class ContactController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {}
}

