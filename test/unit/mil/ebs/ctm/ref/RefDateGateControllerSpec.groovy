package mil.ebs.ctm.ref



import grails.test.mixin.*
import spock.lang.*

@TestFor(RefDateGateController)
@Mock(RefDateGate)
class RefDateGateControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.refDateGateInstanceList
            model.refDateGateInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.refDateGateInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            def refDateGate = new RefDateGate()
            refDateGate.validate()
            controller.save(refDateGate)

        then:"The create view is rendered again with the correct model"
            model.refDateGateInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            refDateGate = new RefDateGate(params)

            controller.save(refDateGate)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/refDateGate/show/1'
            controller.flash.message != null
            RefDateGate.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def refDateGate = new RefDateGate(params)
            controller.show(refDateGate)

        then:"A model is populated containing the domain instance"
            model.refDateGateInstance == refDateGate
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def refDateGate = new RefDateGate(params)
            controller.edit(refDateGate)

        then:"A model is populated containing the domain instance"
            model.refDateGateInstance == refDateGate
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/refDateGate/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def refDateGate = new RefDateGate()
            refDateGate.validate()
            controller.update(refDateGate)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.refDateGateInstance == refDateGate

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            refDateGate = new RefDateGate(params).save(flush: true)
            controller.update(refDateGate)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/refDateGate/show/$refDateGate.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/refDateGate/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def refDateGate = new RefDateGate(params).save(flush: true)

        then:"It exists"
            RefDateGate.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(refDateGate)

        then:"The instance is deleted"
            RefDateGate.count() == 0
            response.redirectedUrl == '/refDateGate/index'
            flash.message != null
    }
}
