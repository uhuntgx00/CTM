package mil.ebs.ctm.ref

class RefDateGate {

    String code
    String name

    boolean canDelete = false
    boolean onDisplay = true
    boolean requireComment = false

    String dateGateType


// --------------------------------------------
// Attributes
// --------------------------------------------

    static constraints = {
        code blank: false, nullable: false, unique: true
        name blank: false, nullable: false

        canDelete blank: false, nullable: false
        onDisplay blank: false, nullable: false
        requireComment blank: false, nullable: false

        dateGateType inList: ['Attendee', 'Conference', 'NewsItem', 'File'], maxSize: 30
    }

    static mapping = {
        version false
    }

    String toString() {
        return name
    }

}
