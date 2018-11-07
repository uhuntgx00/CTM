package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["permitAll"])
class LinkController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        respond Link.list(params), model: [linkInstanceCount: Link.count()]
    }

    def show(Link linkInstance) {
        respond linkInstance
    }

    @Secured(['ROLE_DEVELOPER', 'ROLE_ADMIN', 'ROLE_FMC_ADMIN'])
    def create() {
        respond new Link(params)
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER', 'ROLE_ADMIN', 'ROLE_FMC_ADMIN'])
    def save(Link linkInstance) {
        if (linkInstance == null) {
            notFound()
            return
        }

        if (linkInstance.hasErrors()) {
            respond linkInstance.errors, view: 'create'
            return
        }

        linkInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'linkInstance.label', default: 'Link'), linkInstance.id])
                redirect linkInstance
            }
            '*' { respond linkInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_DEVELOPER', 'ROLE_ADMIN', 'ROLE_FMC_ADMIN'])
    def edit(Link linkInstance) {
        respond linkInstance
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER', 'ROLE_ADMIN', 'ROLE_FMC_ADMIN'])
    def update(Link linkInstance) {
        if (linkInstance == null) {
            notFound()
            return
        }

        if (linkInstance.hasErrors()) {
            respond linkInstance.errors, view: 'edit'
            return
        }

        linkInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Link.label', default: 'Link'), linkInstance.id])
                redirect linkInstance
            }
            '*' { respond linkInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER', 'ROLE_ADMIN', 'ROLE_FMC_ADMIN'])
    def delete(Link linkInstance) {

        if (linkInstance == null) {
            notFound()
            return
        }

        linkInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Link.label', default: 'Link'), linkInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'linkInstance.label', default: 'Link'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
