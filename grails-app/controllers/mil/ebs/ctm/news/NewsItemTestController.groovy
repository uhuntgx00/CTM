package mil.ebs.ctm.news

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import mil.ebs.ctm.Account

@Transactional(readOnly = true)
@Secured(["permitAll"])
class NewsItemTestController {

    def index() {
        render view:'list', model:[newsItems: NewsItem.approved.list(max:10, sort:'dateCreated', order:'desc')]
    }

    def create() {
        if(request.method == "GET") {
            def template = flash.newsItem ?: new NewsItem( body: WikiPage.findByTitle('Default Create Wiki Template')?.body )
            return [newsItem: template]
        }
        else {
            def newsItem = new NewsItem( params['title', 'body', 'status'] )
            newsItem.author = request.account
            if(!newsItem.validate()) {
                flash.newsItem = newsItem
                redirect action:'create'
            }
            else {
                if(request.account?.roles?.any { it.name == org.grails.auth.Role.ADMINISTRATOR }) {
                    newsItem.status = ApprovalStatus.APPROVED
                }
                else {

                    flash.message = "Your news post has been submitted to the administrators for approval"
                }

                newsItem.save(flush:true)
                request.account.addToPermissions("news:edit:$newsItem.id")
                request.account.save()
                redirect action:"show", id:newsItem.id
            }

        }
    }

    def show(Long id) {
        def newsItem = NewsItem.get(id)
        if(newsItem != null) {
            if(newsItem.status == ApprovalStatus.APPROVED || (request.account != null && newsItem.author == request.account)) {
                return [newsItem: newsItem]
            }
            else {
                render status: 404
            }

        }
        else {
            render status:404
        }
    }

    def legacyShow(String author, String title) {
        def a = Account.findByLogin(author)
        def newsItem = NewsItem.where { author == a && title == title }.get()

        if (newsItem) {
            redirect action: "show", id: newsItem.id, permanent: true
        }
        else {
            response.sendError 404
        }
    }

    def edit( Long id) {
        if(request.method == 'GET') {
            def newsItem = show(id)

            if(newsItem) {
                render view:"edit", model: newsItem
            }
        }
        else {
            def n = NewsItem.get(id)

            n.properties = params['title', 'body', 'status']
            if(n.save()) {
                redirect action:'show', id:id
            }
            else {
                render view:"edit", model: newsItem
            }

        }

    }
}