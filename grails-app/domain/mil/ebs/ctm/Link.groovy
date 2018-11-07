package mil.ebs.ctm

class Link {

    String url
    String display
    String tooltip
    String linkType
    String linkColumn

    static constraints = {
        url blank: true, nullable: true
        display blank: false, unique: true, nullable: false
        tooltip blank: false, nullable: false
        linkType inList: ['Link', 'Email', 'Doc']
        linkColumn inList: ['Policy', 'Support', 'External'], blank: false, nullable: false
    }

    static mapping = {
        version false
    }

    /**
     *
     * @return String -
     */
    @Override
    public String toString() {
        return url
    }

}
