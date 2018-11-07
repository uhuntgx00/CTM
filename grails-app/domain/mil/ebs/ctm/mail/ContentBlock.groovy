package mil.ebs.ctm.mail

class ContentBlock {

    String blockName
    String blockContent

    static constraints = {
        blockName blank: false, nullable: false, unique: true
        blockContent blank: false, nullable: false, maxSize: 4000
    }

    static mapping = {
        version false
    }

}
