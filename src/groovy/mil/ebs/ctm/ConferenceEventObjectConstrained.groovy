package mil.ebs.ctm

class ConferenceEventObjectConstrained {

    String title
    String start
    String end
    String backgroundColor
    String url

    boolean equals(final o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }

        ConferenceEventObjectConstrained that = (ConferenceEventObjectConstrained) o

        if (title != that.title) {
            return false
        }

        return true
    }

    int hashCode() {
        return (title != null ? title.hashCode() : 0)
    }
}
