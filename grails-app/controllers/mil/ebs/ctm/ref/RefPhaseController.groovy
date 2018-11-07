package mil.ebs.ctm.ref

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["ROLE_DEVELOPER", "ROLE_ADMIN", "ROLE_FMC_ADMIN"])
class RefPhaseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        respond RefPhase.list(params), model: [refPhaseInstanceCount: RefPhase.count()]
    }

    def show(RefPhase refPhaseInstance) {
        respond refPhaseInstance
    }

    @Secured(['ROLE_DEVELOPER'])
    def create() {
        respond new RefPhase(params)
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER'])
    def save(RefPhase refPhaseInstance) {
        if (refPhaseInstance == null) {
            notFound()
            return
        }

        if (refPhaseInstance.hasErrors()) {
            respond refPhaseInstance.errors, view: 'create'
            return
        }

        refPhaseInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'refPhaseInstance.label', default: 'RefPhase'), refPhaseInstance.id])
                redirect refPhaseInstance
            }
            '*' { respond refPhaseInstance, [status: CREATED] }
        }
    }

    def edit(RefPhase refPhaseInstance) {
        respond refPhaseInstance
    }

    @Transactional
    def update(RefPhase refPhaseInstance) {
        if (refPhaseInstance == null) {
            notFound()
            return
        }

        if (refPhaseInstance.hasErrors()) {
            respond refPhaseInstance.errors, view: 'edit'
            return
        }

        refPhaseInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'RefPhase.label', default: 'RefPhase'), refPhaseInstance.id])
                redirect refPhaseInstance
            }
            '*' { respond refPhaseInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_CREATOR'])
    def delete(RefPhase refPhaseInstance) {

        if (refPhaseInstance == null) {
            notFound()
            return
        }

        refPhaseInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'RefPhase.label', default: 'RefPhase'), refPhaseInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'refPhaseInstance.label', default: 'RefPhase'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
