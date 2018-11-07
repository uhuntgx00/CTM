package mil.ebs.ctm.remove


import grails.test.mixin.*
import mil.ebs.ctm.remove.ApprovalRequest
import mil.ebs.ctm.remove.ApprovalRequestController
import spock.lang.*

@TestFor(ApprovalRequestController)
@Mock(ApprovalRequest)
class ApprovalRequestControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.approvalRequestInstanceList
        model.approvalRequestInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.approvalRequestInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        def approvalRequest = new ApprovalRequest()
        approvalRequest.validate()
        controller.save(approvalRequest)

        then: "The create view is rendered again with the correct model"
        model.approvalRequestInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        approvalRequest = new ApprovalRequest(params)

        controller.save(approvalRequest)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/approvalRequest/show/1'
        controller.flash.message != null
        ApprovalRequest.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def approvalRequest = new ApprovalRequest(params)
        controller.show(approvalRequest)

        then: "A model is populated containing the domain instance"
        model.approvalRequestInstance == approvalRequest
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def approvalRequest = new ApprovalRequest(params)
        controller.edit(approvalRequest)

        then: "A model is populated containing the domain instance"
        model.approvalRequestInstance == approvalRequest
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/approvalRequest/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def approvalRequest = new ApprovalRequest()
        approvalRequest.validate()
        controller.update(approvalRequest)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.approvalRequestInstance == approvalRequest

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        approvalRequest = new ApprovalRequest(params).save(flush: true)
        controller.update(approvalRequest)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/approvalRequest/show/$approvalRequest.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/approvalRequest/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def approvalRequest = new ApprovalRequest(params).save(flush: true)

        then: "It exists"
        ApprovalRequest.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(approvalRequest)

        then: "The instance is deleted"
        ApprovalRequest.count() == 0
        response.redirectedUrl == '/approvalRequest/index'
        flash.message != null
    }
}
