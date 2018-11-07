package mil.ebs.ctm.mail

class MailTemplate {

    String templateName
    String subjectHeader
    String templateContent
    boolean canOverride = true

    String forUser
    String forSupervisor
    String forTdAdmin
    String forTdFullAdmin
    String forTdPOC
    String forFmcAdmin
    String forCao

    static constraints = {
        templateName blank: false, nullable: false, unique: true
        subjectHeader blank: false, nullable: false
        templateContent blank: false, nullable: false, maxSize: 4000
        canOverride blank: false, nullable: false

        forUser inList: ['', 'TO', 'CC', 'BCC'], maxSize: 10, blank: true, nullable: true
        forSupervisor inList: ['', 'TO', 'CC', 'BCC'], maxSize: 10, blank: true, nullable: true
        forTdAdmin inList: ['', 'TO', 'CC', 'BCC'], maxSize: 10, blank: true, nullable: true
        forTdFullAdmin inList: ['', 'TO', 'CC', 'BCC'], maxSize: 10, blank: true, nullable: true
        forTdPOC inList: ['', 'TO', 'CC', 'BCC'], maxSize: 10, blank: true, nullable: true
        forFmcAdmin inList: ['', 'TO', 'CC', 'BCC'], maxSize: 10, blank: true, nullable: true
        forCao inList: ['', 'TO', 'CC', 'BCC'], maxSize: 10, blank: true, nullable: true
    }

    static mapping = {
        version false
    }

    String toString() {
        return templateName
    }

}
