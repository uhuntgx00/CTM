package mil.ebs.ctm

import mil.ebs.ctm.ref.RefDateGate

import java.text.SimpleDateFormat

class DateEvent {

    RefDateGate dateGate
    Date eventDate
    String eventComment
    Account recordedBy

// --------------------------------------------
// Attributes
// --------------------------------------------

    static belongsTo = [conference: Conference, attendee: Attendee]

    static constraints = {
        dateGate blank: false, nullable: false
        eventDate blank: false, nullable: false
        eventComment blank: true, nullable: true
        recordedBy blank: false, nullable: false

        conference blank: true, nullable: true
        attendee blank: true, nullable: true
    }

    static mapping = {
        version false
        sort: 'id'

        eventDate type: 'java.sql.Date'
        dateGate lazy: false
    }

    @Override
    /**
     *
      * @return String -
     */
    String toString() {
        return dateGate.toString() + " (" + new SimpleDateFormat("MMMM dd, yyyy").format(getEventDate()) +") - " + recordedBy
    }

    /**
     * Equality is based upon whether the dateGate|attendee|conference is the same.
     *
     * @param pObject (Object)
     * @return TRUE (if objects are equal) | FALSE (if objects are not)
     */
    boolean equals(final o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }

        DateEvent dateEvent = (DateEvent) o

        if (attendee != dateEvent.attendee) {
            return false
        }
        if (conference != dateEvent.conference) {
            return false
        }
        if (dateGate != dateEvent.dateGate) {
            return false
        }

        return true
    }

    /**
     *
     * @return
     */
    int hashCode() {
        int result

        result = (dateGate != null ? dateGate.hashCode() : 0)
        result = 31 * result + (attendee != null ? attendee.hashCode() : 0)
        result = 31 * result + (conference != null ? conference.hashCode() : 0)

        return result
    }

// --------------------------------------------
// functions
// --------------------------------------------

    /**
     *
     * @return String -
     */
    String shortRecordedBy() {
        return recordedBy.shortName()
    }

    /**
     *
     * @return String -
     */
    String toHtmlString() {
        return "<b><i>" + dateGate.toString() + "</i></b> (" + new SimpleDateFormat("MMMM dd, yyyy").format(getEventDate()) +") - " + shortRecordedBy()
    }

}
