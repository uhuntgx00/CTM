package mil.ebs.ctm.remove

import mil.ebs.ctm.Account
import mil.ebs.ctm.Conference

class ApprovalRequest {

    Conference conference
    String status
    Long numAttendees

    Date approveByDate

    Account approvedBy
    Date approvalDate

    static constraints = {
        conference blank: false, nullable: false
        numAttendees blank: true, nullable: true

        status inList: ['Pending', 'Approved', 'Rejected']

        approveByDate blank: true, nullable: true

        approvedBy blank: true, nullable: true
        approvalDate blank: true, nullable: true
    }

    @Override
    public String toString() {
        return conference?.conferenceTitle
    }

}
