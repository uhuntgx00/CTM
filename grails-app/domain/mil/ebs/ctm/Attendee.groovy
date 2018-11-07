package mil.ebs.ctm

import mil.ebs.ctm.ref.RefAttendeeState
import mil.ebs.ctm.ref.RefDateGate
import mil.ebs.ctm.ref.RefRankGrade
import mil.ebs.ctm.remove.FundSource

class Attendee {

    transient attendeeService

    Account accountLink
    Account supervisor
    int sequence
    String status
    String name

    Organization reservedTD
    String reservedOrg
    String accountType = "Internal"

    Date startTravelDate
    Date endTravelDate

    int mealsIncluded
    String attendanceType
    int hoursAttendanceType
    String justification

    Account createdBy
    Date createdDate
    Date lastChangeDate
    Account lastChange

    Date supervisorApprovalDate
    Account supervisorApprovalBy
    Date tdApprovalDate
    Account tdApprovalBy

    String extEmailAddress
    RefRankGrade rankGrade

// ---------------------------------------------------

    Account approvalRequestBy
    Date approvalRequestDate
    Account registeredBy
    Date registeredDate
    Account withdrawnBy
    Date withdrawnDate
    Account rejectedBy
    Date rejectedDate
    Account cancelledBy
    Date cancelledDate

// ---------------------------------------------------

    Boolean notifyChanges = true
    Boolean notifyConferenceChanges = true

    static belongsTo = [conference: Conference]
    static hasMany = [costs: Cost, fundSources: FundSource]

    static searchable = true

    static constraints = {
        accountLink blank: true, nullable: true
        supervisor blank: true, nullable: true
        name blank: true, nullable: true

        sequence blank: false, nullable: false
        status inList: ['Supervisor', 'TD Concurrence', 'Pending', 'Wait List', 'Withdrawn', 'Approved', 'Disapproved', 'Registered', 'Attended', 'Cancelled', 'Requesting', 'Confirmed'], maxSize: 30

        reservedTD blank: true, nullable: true
        reservedOrg blank: true, nullable: true
        accountType inList: ['Internal', 'External'], maxSize: 30

        startTravelDate blank: true, nullable: true
        endTravelDate blank: true, nullable: true

        createdBy blank: true, nullable: true
        createdDate blank: true, nullable: true
        lastChangeDate blank: true, nullable: true
        lastChange blank: true, nullable: true

        supervisorApprovalDate blank: true, nullable: true
        supervisorApprovalBy blank: true, nullable: true
        tdApprovalDate blank: true, nullable: true
        tdApprovalBy blank: true, nullable: true

        approvalRequestBy blank: true, nullable: true
        approvalRequestDate blank: true, nullable: true
        registeredBy blank: true, nullable: true
        registeredDate blank: true, nullable: true
        withdrawnBy blank: true, nullable: true
        withdrawnDate blank: true, nullable: true
        rejectedBy blank: true, nullable: true
        rejectedDate blank: true, nullable: true
        cancelledBy blank: true, nullable: true
        cancelledDate blank: true, nullable: true

        mealsIncluded blank: true, nullable: true
        attendanceType inList: ['Attendee', 'Booth/Display', 'Discussion Panel', 'Session Chair', 'Presenter/Speaker', 'Support', 'Other'], maxSize: 30
        hoursAttendanceType blank: true, nullable: true
        justification blank: true, nullable: true, maxSize: 4000

        fundSources blank: true, nullable: true

        notifyChanges blank: false, nullable: false
        notifyConferenceChanges blank: false, nullable: false

        rankGrade blank: true, nullable: true
        extEmailAddress email: true, blank: true, nullable: true
    }

    static mapping = {
        version false
        costs sort: 'id'

        startTravelDate type:'java.sql.Date'
        endTravelDate type:'java.sql.Date'

        createdDate type:'java.sql.Date'
        lastChangeDate type:'java.sql.Date'

        supervisorApprovalDate type:'java.sql.Date'
        tdApprovalDate type:'java.sql.Date'

        approvalRequestDate type:'java.sql.Date'
        registeredDate type:'java.sql.Date'
        withdrawnDate type:'java.sql.Date'
        rejectedDate type:'java.sql.Date'
        cancelledDate type:'java.sql.Date'
    }

    String toString() {
        String result = "TBD"

        if (accountType?.equalsIgnoreCase("Internal")) {
            if (accountLink) {
                result = accountLink.toString()
            } else if (reservedTD && name) {
                result = "INT: " + name
            }
        } else if (accountType?.equalsIgnoreCase("External")) {
            if (name) {
                result = "EXT: " + name
            } else {
                result = "EXT"
            }
        }

        return result
    }

// --------------------------------------------
// cost functions
// --------------------------------------------

    Long getCostId(final String pCostType) {
        Cost cost = Cost.findByCostTypeAndAttendee(pCostType, this)
        return cost.id
    }

    Integer afFunding() {
        int result = 0

        for (cost in Cost.findAllByCostTypeAndAttendee("Estimate", this)) {
            if (cost?.fundSources) {
                for (fundingSource in cost?.fundSources) {
                    if (fundingSource?.fundSource?.equalsIgnoreCase("US Air Force")) {
                        result = fundingSource?.percentage
                    }
                }
            } else {
                // ASSUME 100% 'US Air Force'
                result = 100
            }
        }

        return result
    }

    Integer otherFunding() {
        int result = 0

        for (cost in Cost.findAllByCostTypeAndAttendee("Estimate", this)) {
            if (cost?.fundSources) {
                for (fundingSource in cost?.fundSources) {
                    if (!fundingSource?.fundSource?.equalsIgnoreCase("US Air Force")) {
                        result = fundingSource?.percentage
                    }
                }
            } else {
                // ASSUME 100% 'US Air Force'
                result = 0
            }
        }

        return result
    }

    boolean hasEstimate() {
        return Cost.findAllByCostTypeAndAttendee("Estimate", this).size() > 0
    }

    boolean hasActual() {
        return Cost.findAllByCostTypeAndAttendee("Actual", this).size() > 0
    }

    Double estimateTotal() {
        double total = 0.0

        if (status.equalsIgnoreCase("Disapproved") || status.equalsIgnoreCase("Withdrawn") || status.equalsIgnoreCase("Cancelled")) {
            return total
        }

        for (cost in Cost.findAllByCostTypeAndAttendee("Estimate", this)) {
            total += cost.total()
        }

        return total
    }

    Map<String, Double> fundingEstimateTotal() {
        Map<String, Double> result = new HashMap<String, Double>()

        for (cost in Cost.findAllByCostTypeAndAttendee("Estimate", this)) {
            result = cost.totalByFunding()
        }

        return result
    }

    Double actualTotal() {
        double total = 0.0

        if (!status.equalsIgnoreCase("Registered") && !status.equalsIgnoreCase("Confirmed") && !status.equalsIgnoreCase("Attended")) {
            return total
        }

        for (cost in Cost.findAllByCostTypeAndAttendee("Actual", this)) {
            total += cost.registrationCost
            total += cost.airfareCost
            total += cost.localTravelCost
            total += cost.lodgingCost
            total += cost.lodgingCostTax
            total += cost.mealsIncidentalCost
            total += cost.otherCost
        }

        return total
    }

    Double actualTotal_registration() {
        double total = 0.0

        if (!status.equalsIgnoreCase("Registered") && !status.equalsIgnoreCase("Confirmed") && !status.equalsIgnoreCase("Attended")) {
            return total
        }

        for (cost in Cost.findAllByCostTypeAndAttendee("Actual", this)) {
            total += cost.registrationCost
        }

        return total
    }

    Double actualTotal_travel() {
        double total = 0.0

        if (!status.equalsIgnoreCase("Registered") && !status.equalsIgnoreCase("Confirmed") && !status.equalsIgnoreCase("Attended")) {
            return total
        }

        for (cost in Cost.findAllByCostTypeAndAttendee("Actual", this)) {
            total += cost.airfareCost
            total += cost.localTravelCost
            total += cost.lodgingCost
            total += cost.lodgingCostTax
            total += cost.mealsIncidentalCost
        }

        return total
    }

    Double actualTotal_other() {
        double total = 0.0

        if (!status.equalsIgnoreCase("Registered") && !status.equalsIgnoreCase("Confirmed") && !status.equalsIgnoreCase("Attended")) {
            return total
        }

        for (cost in Cost.findAllByCostTypeAndAttendee("Actual", this)) {
            total += cost.otherCost
        }

        return total
    }

    Map<String, Double> fundingActualTotal() {
        Map<String, Double> result = new HashMap<String, Double>()

        for (cost in Cost.findAllByCostTypeAndAttendee("Actual", this)) {
            result = cost.totalByFunding()
        }

        return result
    }

// --------------------------------------------
// date events attached
// --------------------------------------------

    /**
     *
     * @return Set<DateEvent) - date events attached to the conference
     */
    Set<DateEvent> getDateEvents() {
        DateEvent.findAllByAttendee(this).collect { it } as Set
    }

    /**
     *
     * @param pDateEventCode (String) -
     * @return
     */
    public boolean hasDate(final String pDateEventCode) {
        for (dateEvent in DateEvent.findAllByAttendee(this)) {
            if (dateEvent?.dateGate?.code?.equalsIgnoreCase(pDateEventCode)) {
                return true
            }
        }

        return false
    }

    /**
     *
     * @param pDateEventCode1 (String) -
     * @param pDateEventCode2 (String) -
     * @return int - number of days difference between event codes
     */
    public int daysDiff(final String pDateEventCode1, final String pDateEventCode2) {
        int result = 0

        List<DateEvent> dateEvent1 = DateEvent.findAllByAttendeeAndDateGate(this, RefDateGate.findByCode(pDateEventCode1))
        List<DateEvent> dateEvent2 = DateEvent.findAllByAttendeeAndDateGate(this, RefDateGate.findByCode(pDateEventCode2))

        if (!dateEvent1?.empty && !dateEvent2?.empty) {
            result = Math.abs(dateEvent1?.get(0)?.eventDate - dateEvent2?.get(0)?.eventDate)
        }

        return result
    }

// --------------------------------------------
// Date Related
// --------------------------------------------

    private static int diffInDays(final Date pDate1, final Date pDate2) {
        if (pDate1 == null) {
            return 0
        } else {
            return pDate2 - pDate1
        }
    }

    public int travelDays() {
        if (startTravelDate) {
            return endTravelDate - startTravelDate + 1
        } else {
            return 0
        }
    }

    public int getChangeDays() {
        return diffInDays(getLastChangeDate(), new Date())
    }

// --------------------------------------------
// state actions
// --------------------------------------------

    /**
     *
     * @return Set<RefAttendeeState> - possible actions available for the given conference phase and attendee state (status)
     */
    Set<RefAttendeeState> availableActions() {
        RefAttendeeState.findAllByAttendeeStateAndPhaseState(status, conference?.phaseState).collect { it } as Set
    }

    Boolean checkPermission(final String pAction, final String pPermission) {
        return attendeeService.checkPermission(pAction, pPermission, this)
    }

}
