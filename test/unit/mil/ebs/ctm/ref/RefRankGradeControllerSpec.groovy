package mil.ebs.ctm.ref

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(RefRankGradeController)
@Mock(RefRankGrade)
class RefRankGradeControllerSpec
        extends Specification
{

    def setup() {
    }

    def cleanup() {
    }

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {
        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.refRankGradeInstanceList
        model.refRankGradeInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.refRankGradeInstance != null
    }

    void "Test the save action correctly persists an instance"() {
        when: "The save action is executed with an invalid instance"
        def refRankGrade = new RefRankGrade()
        refRankGrade.validate()
        controller.save(refRankGrade)

        then: "The create view is rendered again with the correct model"
        model.refRankGradeInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        refRankGrade = new RefRankGrade(params)

        request.method = 'POST'
        controller.save(refRankGrade)

        then: "A redirect is issued to the show action"
//        view == '/refRankGrade/show/1'
        response.redirectedUrl == '/refRankGrade/show/1'
        controller.flash.message != null
        RefRankGrade.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def refRankGrade = new RefRankGrade(params)
        controller.show(refRankGrade)

        then: "A model is populated containing the domain instance"
        model.refRankGradeInstance == refRankGrade
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def refRankGrade = new RefRankGrade(params)
        controller.edit(refRankGrade)

        then: "A model is populated containing the domain instance"
        model.refRankGradeInstance == refRankGrade
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/refRankGrade/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def refRankGrade = new RefRankGrade()
        refRankGrade.validate()
        controller.update(refRankGrade)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.refRankGradeInstance == refRankGrade

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        refRankGrade = new RefPhaseState(params).save(flush: true)
        controller.update(refRankGrade)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/refRankGrade/show/$refRankGrade.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/refRankGrade/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def refRankGrade = new RefRankGrade(params).save(flush: true)

        then: "It exists"
        RefRankGrade.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(refRankGrade)

        then: "The instance is deleted"
        RefRankGrade.count() == 0
        response.redirectedUrl == '/refRankGrade/index'
        flash.message != null
    }


}
