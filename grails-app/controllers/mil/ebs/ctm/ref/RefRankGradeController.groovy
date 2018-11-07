package mil.ebs.ctm.ref

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["ROLE_DEVELOPER", "ROLE_ADMIN", "ROLE_FMC_ADMIN"])
class RefRankGradeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     *
     * @param max (Integer) -
     * @return
     */
    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        respond RefRankGrade.list(params), model: [refRankGradeInstanceCount: RefRankGrade.count()]
    }

    /**
     *
     * @param refRankGradeInstance (RefRankGrade) -
     * @return
     */
    def show(RefRankGrade refRankGradeInstance) {
        respond refRankGradeInstance
    }

    /**
     *
     * @return
     */
    def create() {
        respond new RefRankGrade(params)
    }

    /**
     *
     * @param refRankGradeInstance (RefRankGrade) -
     * @return
     */
    @Transactional
    def save(RefRankGrade refRankGradeInstance) {
        if (refRankGradeInstance == null) {
            notFound()
            return
        }

        if (refRankGradeInstance.hasErrors()) {
            respond refRankGradeInstance.errors, view: 'create'
            return
        }

        refRankGradeInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'refRankGradeInstance.label', default: 'RefRankGrade'), refRankGradeInstance.id])
                redirect refRankGradeInstance
            }
            '*' {
                respond refRankGradeInstance, [status: CREATED]
            }
        }
    }

    /**
     *
     * @param refRankGradeInstance (RefRankGrade) -
     * @return
     */
    def edit(RefRankGrade refRankGradeInstance) {
        respond refRankGradeInstance
    }

    /**
     *
     * @param refRankGradeInstance (RefRankGrade) -
     * @return
     */
    @Transactional
    def update(RefRankGrade refRankGradeInstance) {
        if (refRankGradeInstance == null) {
            notFound()
            return
        }

        if (refRankGradeInstance.hasErrors()) {
            respond refRankGradeInstance.errors, view: 'edit'
            return
        }

        refRankGradeInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'RefRankGrade.label', default: 'RefRankGrade'), refRankGradeInstance.id])
                redirect refRankGradeInstance
            }
            '*' { respond refRankGradeInstance, [status: OK] }
        }
    }

    /**
     *
     * @param refRankGradeInstance (RefRankGrade) -
     * @return
     */
    @Transactional
    def delete(RefRankGrade refRankGradeInstance) {
        if (refRankGradeInstance == null) {
            notFound()
            return
        }

        refRankGradeInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'RefRankGrade.label', default: 'RefRankGrade'), refRankGradeInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    /**
     *
     */
    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'refRankGradeInstance.label', default: 'RefRankGrade'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
