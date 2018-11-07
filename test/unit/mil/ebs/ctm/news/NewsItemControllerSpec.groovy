package mil.ebs.ctm.news


import grails.test.mixin.*
import mil.ebs.ctm.news.NewsItem
import mil.ebs.ctm.news.NewsItemController
import spock.lang.*

@TestFor(NewsItemController)
@Mock(NewsItem)
class NewsItemControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.newsItemInstanceList
        model.newsItemInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.newsItemInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        def newsItem = new NewsItem()
        newsItem.validate()
        controller.save(newsItem)

        then: "The create view is rendered again with the correct model"
        model.newsItemInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        newsItem = new NewsItem(params)

        controller.save(newsItem)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/newsItem/show/1'
        controller.flash.message != null
        NewsItem.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def newsItem = new NewsItem(params)
        controller.show(newsItem)

        then: "A model is populated containing the domain instance"
        model.newsItemInstance == newsItem
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def newsItem = new NewsItem(params)
        controller.edit(newsItem)

        then: "A model is populated containing the domain instance"
        model.newsItemInstance == newsItem
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/newsItem/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def newsItem = new NewsItem()
        newsItem.validate()
        controller.update(newsItem)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.newsItemInstance == newsItem

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        newsItem = new NewsItem(params).save(flush: true)
        controller.update(newsItem)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/newsItem/show/$newsItem.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/newsItem/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def newsItem = new NewsItem(params).save(flush: true)

        then: "It exists"
        NewsItem.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(newsItem)

        then: "The instance is deleted"
        NewsItem.count() == 0
        response.redirectedUrl == '/newsItem/index'
        flash.message != null
    }
}
