package mil.ebs.ctm.mail

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['permitAll'])
class ContentBlockController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 100, 100)
        respond ContentBlock.list(params), model: [contentBlockInstanceCount: ContentBlock.count()]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN'])
    def show(ContentBlock contentBlockInstance) {
        respond contentBlockInstance
    }

    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def create() {
        respond new ContentBlock(params)
    }

    @Transactional
    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def save(ContentBlock contentBlockInstance) {
        if (contentBlockInstance == null) {
            notFound()
            return
        }

        if (contentBlockInstance.hasErrors()) {
            respond contentBlockInstance.errors, view: 'create'
            return
        }

        contentBlockInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'contentBlockInstance.label', default: 'Content Block'), contentBlockInstance.id])
                redirect contentBlockInstance
            }
            '*' { respond contentBlockInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def edit(ContentBlock contentBlockInstance) {
        respond contentBlockInstance
    }

    @Transactional
    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def update(ContentBlock contentBlockInstance) {
        if (contentBlockInstance == null) {
            notFound()
            return
        }

        if (contentBlockInstance.hasErrors()) {
            respond contentBlockInstance.errors, view: 'edit'
            return
        }

        contentBlockInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ContentBlock.label', default: 'Content Block'), contentBlockInstance.id])
                redirect contentBlockInstance
            }
            '*' { respond contentBlockInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER'])
    def delete(ContentBlock contentBlockInstance) {

        if (contentBlockInstance == null) {
            notFound()
            return
        }

        contentBlockInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ContentBlock.label', default: 'Content Block'), contentBlockInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'contentBlockInstance.label', default: 'ContentBlock'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
