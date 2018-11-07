package mil.ebs.ctm.remove

import grails.plugin.springsecurity.annotation.Secured
import mil.ebs.ctm.Account
import mil.ebs.ctm.Attendee
import mil.ebs.ctm.Conference

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["permitAll"])
class ApprovalRequestController {

    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", approve: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ApprovalRequest.list(params), model: [approvalRequestInstanceCount: ApprovalRequest.count()]
    }

    def show(ApprovalRequest approvalRequestInstance) {
        respond approvalRequestInstance
    }

    def create() {
        respond new ApprovalRequest(params)
    }

    @Transactional
    def save(ApprovalRequest approvalRequestInstance) {
        if (approvalRequestInstance == null) {
            notFound()
            return
        }

        if (approvalRequestInstance.hasErrors()) {
            respond approvalRequestInstance.errors, view: 'create'
            return
        }

        approvalRequestInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'approvalRequestInstance.label', default: 'Approval Request'), approvalRequestInstance.id])
                redirect approvalRequestInstance
            }
            '*' { respond approvalRequestInstance, [status: CREATED] }
        }
    }

    def edit(ApprovalRequest approvalRequestInstance) {
        respond approvalRequestInstance
    }

    @Transactional
    def update(ApprovalRequest approvalRequestInstance) {
        if (approvalRequestInstance == null) {
            notFound()
            return
        }

        if (approvalRequestInstance.hasErrors()) {
            respond approvalRequestInstance.errors, view: 'edit'
            return
        }

        approvalRequestInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ApprovalRequest.label', default: 'ApprovalRequest'), approvalRequestInstance.id])
                redirect approvalRequestInstance
            }
            '*' { respond approvalRequestInstance, [status: OK] }
        }
    }

    @Transactional
    def approve(ApprovalRequest approvalRequestInstance) {
        if (approvalRequestInstance == null) {
            notFound()
            return
        }

        if (approvalRequestInstance.hasErrors()) {
            respond approvalRequestInstance.errors, view: 'edit'
            return
        }

        approvalRequestInstance.status = "Approved"
        approvalRequestInstance.approvalDate = new Date()
        approvalRequestInstance.approvedBy = Account.get(1)
        approvalRequestInstance.save flush: true

        Conference conference = Conference.get(approvalRequestInstance?.conference?.id)
        conference?.status = "Approved"
        conference?.statusChangeDate = new Date()
        conference?.statusChangedBy = Account.get(1)
        conference?.save flush: true
        //todo: send out notification email for status change

        def count
        if (conference?.numAttendees) {
            count  = conference.numAttendees
        }

        if (params.get("maxAttendees")) {
            count = Long.parseLong(params.get("maxAttendees"))
            conference?.numAttendees = count
            conference?.save flush: true
        }

        if (conference?.numAttendees && !params.get("maxAttendees")) {
            count = null
            conference?.numAttendees = null
            conference?.save flush: true
        }

        List<Attendee> attendeeList = Attendee.findAllByConference(conference, [sort: "id"])

        if (!count) {
            count = attendeeList.size()
        }

        for (attendee in attendeeList) {
            if (count > 0) {
                attendee.status = "Approved"
                attendee.approvalRequestDate = new Date()
                attendee.approvalRequestBy = Account.get(1)
                attendee.save flush: true
                //todo: send out notification email for status change
                count--
            } else {
                attendee.status = "Wait List"
                attendee.save flush: true
                //todo: send out notification email for status change
            }
        }

        request.withFormat {
            form {
                redirect controller:"conference", id: conference?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: conference?.id, action:"show", method:"GET" }
        }
    }

    @Transactional
    def reject(ApprovalRequest approvalRequestInstance) {
        if (approvalRequestInstance == null) {
            notFound()
            return
        }

        if (approvalRequestInstance.hasErrors()) {
            respond approvalRequestInstance.errors, view: 'edit'
            return
        }

        approvalRequestInstance.status = "Rejected"
        approvalRequestInstance.save flush: true

        Conference conference = Conference.get(approvalRequestInstance?.conference?.id)
        conference?.status = "Disapproved"
        conference?.statusChangeDate = new Date()
        conference?.statusChangedBy = Account.get(1)
        conference?.save flush: true
        //todo: send out notification email for status change

        for (attendee in conference?.attendees) {
            attendee.status = "Disapproved"
            attendee.rejectedDate = new Date()
            attendee.rejectedBy = Account.get(1)
            attendee.save flush: true
            //todo: send out notification email for status change
        }

        request.withFormat {
            form {
                redirect controller:"conference", id: conference?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: conference?.id, action:"show", method:"GET" }
        }
    }

    @Transactional
    def delete(ApprovalRequest approvalRequestInstance) {

        if (approvalRequestInstance == null) {
            notFound()
            return
        }

        approvalRequestInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ApprovalRequest.label', default: 'ApprovalRequest'), approvalRequestInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'approvalRequestInstance.label', default: 'ApprovalRequest'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
