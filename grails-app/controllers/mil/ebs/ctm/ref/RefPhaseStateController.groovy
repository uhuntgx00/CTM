package mil.ebs.ctm.ref

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["ROLE_DEVELOPER", "ROLE_ADMIN", "ROLE_FMC_ADMIN"])
class RefPhaseStateController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 200, 200)
        respond RefPhaseState.list(params), model: [refPhaseStateInstanceCount: RefPhaseState.count()]
    }

    def show(RefPhaseState refPhaseStateInstance) {
        respond refPhaseStateInstance
    }

    @Secured(['ROLE_DEVELOPER'])
    def create() {
        respond new RefPhaseState(params)
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER'])
    def save(RefPhaseState refPhaseStateInstance) {
        if (refPhaseStateInstance == null) {
            notFound()
            return
        }

        if (refPhaseStateInstance.hasErrors()) {
            respond refPhaseStateInstance.errors, view: 'create'
            return
        }

        refPhaseStateInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'refPhaseStateInstance.label', default: 'RefPhaseState'), refPhaseStateInstance.id])
                redirect refPhaseStateInstance
            }
            '*' { respond refPhaseStateInstance, [status: CREATED] }
        }
    }

    def edit(RefPhaseState refPhaseStateInstance) {
        respond refPhaseStateInstance
    }

    @Transactional
    def update(RefPhaseState refPhaseStateInstance) {
        if (refPhaseStateInstance == null) {
            notFound()
            return
        }

        if (refPhaseStateInstance.hasErrors()) {
            respond refPhaseStateInstance.errors, view: 'edit'
            return
        }

        refPhaseStateInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'RefPhaseState.label', default: 'RefPhaseState'), refPhaseStateInstance.id])
                redirect refPhaseStateInstance
            }
            '*' { respond refPhaseStateInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER'])
    def delete(RefPhaseState refPhaseStateInstance) {

        if (refPhaseStateInstance == null) {
            notFound()
            return
        }

        refPhaseStateInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'RefPhaseState.label', default: 'RefPhaseState'), refPhaseStateInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'refPhaseStateInstance.label', default: 'RefPhaseState'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
