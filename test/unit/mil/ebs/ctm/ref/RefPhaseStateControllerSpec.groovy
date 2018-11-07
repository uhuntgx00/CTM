package mil.ebs.ctm.ref

import grails.test.mixin.*
import spock.lang.*

@TestFor(RefPhaseStateController)
@Mock(RefPhaseState)
class RefPhaseStateControllerSpec
        extends Specification
{

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.refPhaseStateInstanceList
        model.refPhaseStateInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.refPhaseStateInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        def refPhaseState = new RefPhaseState()
        refPhaseState.validate()
        controller.save(refPhaseState)

        then: "The create view is rendered again with the correct model"
        model.refPhaseStateInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        refPhaseState = new RefPhaseState(params)

        controller.save(refPhaseState)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/refPhaseState/show/1'
        controller.flash.message != null
        RefPhaseState.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def refPhaseState = new RefPhaseState(params)
        controller.show(refPhaseState)

        then: "A model is populated containing the domain instance"
        model.refPhaseStateInstance == refPhaseState
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def refPhaseState = new RefPhaseState(params)
        controller.edit(refPhaseState)

        then: "A model is populated containing the domain instance"
        model.refPhaseStateInstance == refPhaseState
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/refPhaseState/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def refPhaseState = new RefPhaseState()
        refPhaseState.validate()
        controller.update(refPhaseState)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.refPhaseStateInstance == refPhaseState

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        refPhaseState = new RefPhaseState(params).save(flush: true)
        controller.update(refPhaseState)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/refPhaseState/show/$refPhaseState.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/refPhaseState/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def refPhaseState = new RefPhaseState(params).save(flush: true)

        then: "It exists"
        RefPhaseState.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(refPhaseState)

        then: "The instance is deleted"
        RefPhaseState.count() == 0
        response.redirectedUrl == '/refPhaseState/index'
        flash.message != null
    }
}
