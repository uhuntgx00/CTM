package mil.ebs.ctm

class ConferenceComment {

    String eComment
    Account who
    Date when
    String phase

    static belongsTo = [conference: Conference]

    static constraints = {
        eComment blank: true, nullable: true, maxSize: 2000
        who blank: false, nullable: false
        when blank: true, nullable: true
        phase blank: true, nullable: true
    }

    static mapping = {
        version false

        when type:'java.sql.Date'
    }

}
