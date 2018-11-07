package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured
import org.hibernate.criterion.CriteriaSpecification

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["ROLE_EOC", "ROLE_ADMIN", "ROLE_FMC_ADMIN", "ROLE_DEVELOPER"])
class AccountActivityController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 25, 100)

        def activity = AccountActivity.list().unique() { it.account }

        respond activity, model: [accountActivityInstanceCount: activity.size()]
    }

    def last20(Integer max) {
        def activity = AccountActivity.findAllByAccount(params.account, [max: 20])

        respond activity, model: [accountActivityInstanceCount: activity.size()]
    }

    @Secured(["ROLE_DOES_NOT_EXIST"])
    def show(AccountActivity accountActivityInstance) {
        respond accountActivityInstance
    }

    @Secured(["ROLE_DOES_NOT_EXIST"])
    def create() {
        respond new AccountActivity(params)
    }

    @Transactional
    @Secured(["ROLE_DOES_NOT_EXIST"])
    def save(AccountActivity accountActivityInstance) {
        if (accountActivityInstance == null) {
            notFound()
            return
        }

        if (accountActivityInstance.hasErrors()) {
            respond accountActivityInstance.errors, view: 'create'
            return
        }

        accountActivityInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'accountActivityInstance.label', default: 'AccountActivity'), accountActivityInstance.id])
                redirect accountActivityInstance
            }
            '*' { respond accountActivityInstance, [status: CREATED] }
        }
    }

    @Secured(["ROLE_DOES_NOT_EXIST"])
    def edit(AccountActivity accountActivityInstance) {
        respond accountActivityInstance
    }

    @Transactional
    @Secured(["ROLE_DOES_NOT_EXIST"])
    def update(AccountActivity accountActivityInstance) {
        if (accountActivityInstance == null) {
            notFound()
            return
        }

        if (accountActivityInstance.hasErrors()) {
            respond accountActivityInstance.errors, view: 'edit'
            return
        }

        accountActivityInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AccountActivity.label', default: 'AccountActivity'), accountActivityInstance.id])
                redirect accountActivityInstance
            }
            '*' { respond accountActivityInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(["ROLE_DOES_NOT_EXIST"])
    def delete(AccountActivity accountActivityInstance) {

        if (accountActivityInstance == null) {
            notFound()
            return
        }

        accountActivityInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'AccountActivity.label', default: 'AccountActivity'), accountActivityInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'accountActivityInstance.label', default: 'AccountActivity'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
