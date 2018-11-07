package mil.ebs.ctm.remove

import mil.ebs.ctm.Attendee

class FundSource {

    String fundSource
    Integer percentage = 100

    static belongsTo = [attendee: Attendee]

    static constraints = {
        fundSource inList: ['US Air Force', 'Other US Govt', 'Non-Federal Entity']
        percentage nullable: false, blank: false, min: 0, max: 100
    }

    static mapping = {
        version false
    }

    String toString() {
        return fundSource
    }

}
