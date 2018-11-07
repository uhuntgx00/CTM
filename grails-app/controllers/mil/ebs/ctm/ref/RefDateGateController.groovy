package mil.ebs.ctm.ref

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["ROLE_DEVELOPER", "ROLE_ADMIN", "ROLE_FMC_ADMIN"])
class RefDateGateController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond RefDateGate.list(params), model:[refDateGateInstanceCount: RefDateGate.count()]
    }

    def show(RefDateGate refDateGateInstance) {
        respond refDateGateInstance
    }

    @Secured(['ROLE_DEVELOPER'])
    def create() {
        respond new RefDateGate(params)
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER'])
    def save(RefDateGate refDateGateInstance) {
        if (refDateGateInstance == null) {
            notFound()
            return
        }

        if (refDateGateInstance.hasErrors()) {
            respond refDateGateInstance.errors, view:'create'
            return
        }

        refDateGateInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'refDateGateInstance.label', default: 'RefDateGate'), refDateGateInstance.id])
                redirect refDateGateInstance
            }
            '*' { respond refDateGateInstance, [status: CREATED] }
        }
    }

    def edit(RefDateGate refDateGateInstance) {
        respond refDateGateInstance
    }

    @Transactional
    def update(RefDateGate refDateGateInstance) {
        if (refDateGateInstance == null) {
            notFound()
            return
        }

        if (refDateGateInstance.hasErrors()) {
            respond refDateGateInstance.errors, view:'edit'
            return
        }

        refDateGateInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'RefDateGate.label', default: 'RefDateGate'), refDateGateInstance.id])
                redirect refDateGateInstance
            }
            '*'{ respond refDateGateInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER'])
    def delete(RefDateGate refDateGateInstance) {

        if (refDateGateInstance == null) {
            notFound()
            return
        }

        refDateGateInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'RefDateGate.label', default: 'RefDateGate'), refDateGateInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'refDateGateInstance.label', default: 'RefDateGate'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
