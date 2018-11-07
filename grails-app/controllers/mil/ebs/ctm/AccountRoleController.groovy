package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured
import mil.ebs.ctm.AccountRole

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_EOC', 'ROLE_DEVELOPER'])
class AccountRoleController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond AccountRole.list(params), model: [accountRoleInstanceCount: AccountRole.count()]
    }

    def show(AccountRole accountRoleInstance) {
        respond accountRoleInstance
    }

    def create() {
        respond new AccountRole(params)
    }

    @Transactional
    def save(AccountRole accountRoleInstance) {
        if (accountRoleInstance == null) {
            notFound()
            return
        }

        if (accountRoleInstance.hasErrors()) {
            respond accountRoleInstance.errors, view: 'create'
            return
        }

        accountRoleInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'accountRoleInstance.label', default: 'AccountRole'), accountRoleInstance.id])
                redirect accountRoleInstance
            }
            '*' { respond accountRoleInstance, [status: CREATED] }
        }
    }

    def edit(AccountRole accountRoleInstance) {
        respond accountRoleInstance
    }

    @Transactional
    def update(AccountRole accountRoleInstance) {
        if (accountRoleInstance == null) {
            notFound()
            return
        }

        if (accountRoleInstance.hasErrors()) {
            respond accountRoleInstance.errors, view: 'edit'
            return
        }

        accountRoleInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AccountRole.label', default: 'AccountRole'), accountRoleInstance.id])
                redirect accountRoleInstance
            }
            '*' { respond accountRoleInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(AccountRole accountRoleInstance) {

        if (accountRoleInstance == null) {
            notFound()
            return
        }

        accountRoleInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'AccountRole.label', default: 'AccountRole'), accountRoleInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'accountRoleInstance.label', default: 'AccountRole'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
