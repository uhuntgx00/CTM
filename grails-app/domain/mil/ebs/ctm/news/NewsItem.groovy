package mil.ebs.ctm.news

import mil.ebs.ctm.Account

class NewsItem {

    String title
    Account author
    String body
    Boolean locked = false
    Date dateCreated
    Date lastUpdated
    String status


    static hasMany = [versions: Version]
    static transients = ["latestVersion"]

    static constraints = {
        title blank: false, nullable: false
        body blank: true, nullable: true
        locked blank: true, nullable: true
        author blank: false, nullable: false
        status inList: ['Pending', 'Approved', 'Rejected']

        dateCreated blank: true, nullable: true
        lastUpdated blank: true, nullable: true
    }

    static mapping = {
        cache true
        body type:"text"
        cache 'nonstrict-read-write'
        title index: "title_idx"
    }

    def getLatestVersion() {
        Version.withCriteria(uniqueResult: true) {
            eq("current", this)
            order "number", "desc"
            maxResults 1
        }
    }

    Version createVersion() {
        def verObject = new Version(number:version, current:this)
        verObject.title = title
        verObject.body = body
        return verObject
    }

    static searchable = {
        only = ['body', 'title']
        title boost: 2.0
    }

    static namedQueries = {
        approved {
            eq "status", 'Approved'
        }
        pending {
            or {
                eq 'status', 'Pending'
                eq 'status', 'Rejected'
            }

        }
        all {
            order "dateCreated", "desc"
        }
        allApproved {
            approved()
            all()
        }

    }

}
