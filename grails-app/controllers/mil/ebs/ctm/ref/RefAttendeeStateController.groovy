package mil.ebs.ctm.ref

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["ROLE_DEVELOPER", "ROLE_ADMIN", "ROLE_FMC_ADMIN"])
class RefAttendeeStateController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 200, 200)
        respond RefAttendeeState.list(params), model: [refAttendeeStateInstanceCount: RefAttendeeState.count()]
    }

    def show(RefAttendeeState refAttendeeStateInstance) {
        respond refAttendeeStateInstance
    }

    def create() {
        respond new RefAttendeeState(params)
    }

    @Transactional
    def save(RefAttendeeState refAttendeeStateInstance) {
        if (refAttendeeStateInstance == null) {
            notFound()
            return
        }

        if (refAttendeeStateInstance.hasErrors()) {
            respond refAttendeeStateInstance.errors, view: 'create'
            return
        }

        refAttendeeStateInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'refAttendeeStateInstance.label', default: 'RefAttendeeState'), refAttendeeStateInstance.id])
                redirect refAttendeeStateInstance
            }
            '*' { respond refAttendeeStateInstance, [status: CREATED] }
        }
    }

    def edit(RefAttendeeState refAttendeeStateInstance) {
        respond refAttendeeStateInstance
    }

    @Transactional
    def update(RefAttendeeState refAttendeeStateInstance) {
        if (refAttendeeStateInstance == null) {
            notFound()
            return
        }

        if (refAttendeeStateInstance.hasErrors()) {
            respond refAttendeeStateInstance.errors, view: 'edit'
            return
        }

        refAttendeeStateInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'RefAttendeeState.label', default: 'RefAttendeeState'), refAttendeeStateInstance.id])
                redirect refAttendeeStateInstance
            }
            '*' { respond refAttendeeStateInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(RefAttendeeState refAttendeeStateInstance) {

        if (refAttendeeStateInstance == null) {
            notFound()
            return
        }

        refAttendeeStateInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'RefAttendeeState.label', default: 'RefAttendeeState'), refAttendeeStateInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'refAttendeeStateInstance.label', default: 'RefAttendeeState'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
