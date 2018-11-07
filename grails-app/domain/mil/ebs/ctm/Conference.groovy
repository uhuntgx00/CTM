package mil.ebs.ctm

import mil.ebs.ctm.ref.RefDateGate
import mil.ebs.ctm.ref.RefPhaseState
import mil.ebs.ctm.remove.ApprovalRequest
import mil.ebs.ctm.upload.FileUpload

class Conference {

    transient phaseStateService
    transient conferenceService

    String primaryHost
    String conferenceTitle

    String phaseState
    String status
    Date statusChangeDate
    Account statusChangedBy
    String step
    boolean missionCritical = false
    boolean annualConference = false

    Account createdBy
    Date createdDate
    Date lastChangeDate
    Account lastChange
    int accountEdit = 0

    Date displayAfter
    Date startDate
    Date endDate
    boolean hide = false
    boolean locked = false
    Long numAttendees

    String venue
    Address address
    Integer perdiem
    Integer meals

    String website
    String purpose
    String hostType
    boolean afrlHosted = false
    String coHostEntity
    String nonHostType
    Organization responsibleTD
    Organization alternateRespTD
    String responsibleOrg

    Account conferenceAO
    Account alternateCAO

    String afrlSoccer
    Date afrlSoccerDate
    String afmcSoccer
    Date afmcSoccerDate
    String safTmt
    Date safTmtDate

    String primarySponsor
    ApprovalRequest approvalRequest

    String approvalNotice
    String disapproveNotice

    static transients = ['phaseStateService', 'conferenceService']

    static hasMany = [attendees: Attendee, comments: ConferenceComment, summaries: Summary]

    static searchable = {
        except = ["approvalRequest", "conferenceAO", "alternateCAO", "startDate", "endDate", "hide", "locked",
                  "numAttendees", "perdiem", "meals", "afrlHosted", "responsibleTD", "alternateRespTD", "responsibleOrg",
                  "afrlSoccerDate", "afmcSoccerDate", "safTmtDate", "displayAfter", "accountEdit",
                  "createdBy", "createdDate", "lastChangeDate", "lastChange", "missionCritical",
                  "annualConference", "step", "status", "phaseState", "statusChangeDate", "statusChangedBy"]
//        startDate format: "MMMM"
        conferenceTitle boost: 5.0
        purpose boost: 3.0
        venue boost: 2.0
        spellCheck "include"
    }

    static constraints = {
        conferenceTitle blank: false, nullable: false, unique: true
        primaryHost blank: true, nullable: true

        phaseState inList: ['Open', 'Closed', 'External', 'Create Package', 'TD Review', 'Resp TD', 'AFRL Review', 'AFMC Review', 'SAF Review', 'Approved', 'Finalizing', '*ERROR*'], maxSize: 30
        status inList: ['Pending', 'Open', 'Cancelled', 'Drafting', 'Reviewing', 'Revising', 'Processing', 'Approved', 'Disapproved', 'Archived', '*ERROR*'], maxSize: 30
        statusChangeDate blank: true, nullable: true
        statusChangedBy blank: true, nullable: true

        createdBy blank: true, nullable: true
        createdDate blank: true, nullable: true
        lastChangeDate blank: true, nullable: true
        lastChange blank: true, nullable: true
        accountEdit blank: false, nullable: false
        step inList: ['AFRL/FMC', 'AFRL/FM', 'AFRL/Workflow', 'AFRL/JA', 'AFRL/CV', 'AFRL/CA', 'AFRL/CC'], blank: true, nullable: true, maxSize: 30
        missionCritical blank: false, nullable: false
        annualConference blank: false, nullable: false

        displayAfter blank: true, nullable: true
        startDate blank: false, nullable: false
        endDate blank: false, nullable: false
        hide blank: false, nullable: false
        locked blank: false, nullable: false
        numAttendees blank: true, nullable: true

        venue blank: false, nullable: false
        address blank: true, nullable: true
        perdiem blank: true, nullable: true
        meals blank: true, nullable: true

        website blank: true, nullable: true
        purpose blank: true, nullable: true, maxSize: 4000

        hostType inList: ['AF Hosted', 'AF Co-Hosted', 'DoD Hosted', 'DoD Co-Hosted', 'Non-DoD Hosted'], maxSize: 50
        afrlHosted blank: false, nullable: false
        coHostEntity blank: true, nullable: true, maxSize: 250
        nonHostType inList: ['Other US Govt Hosted', 'Foreign Govt Hosted', 'Professional Society', 'Academia', 'Other'], blank: true, nullable: true, maxSize: 50
        responsibleTD blank: true, nullable: true
        alternateRespTD blank: true, nullable: true
        responsibleOrg blank: true, nullable: true

        conferenceAO blank: true, nullable: true
        alternateCAO blank: true, nullable: true

        afrlSoccer blank: true, nullable: true
        afrlSoccerDate blank: true, nullable: true
        afmcSoccer blank: true, nullable: true
        afmcSoccerDate blank: true, nullable: true
        safTmt blank: true, nullable: true
        safTmtDate blank: true, nullable: true

        primarySponsor blank: true, nullable: true
        approvalRequest blank: true, nullable: true

        approvalNotice blank: true, nullable: true, maxSize: 3000
        disapproveNotice blank: true, nullable: true, maxSize: 3000
    }

    static mapping = {
        version false
        attendees sort: 'id'

        statusChangeDate type:'java.sql.Date'
        createdDate type:'java.sql.Date'
        lastChangeDate type:'java.sql.Date'
        displayAfter type:'java.sql.Date'

        startDate type:'java.sql.Date'
        endDate type:'java.sql.Date'

        afrlSoccerDate type:'java.sql.Date'
        afmcSoccerDate type:'java.sql.Date'
        safTmtDate type:'java.sql.Date'

        attendees lazy: true
        comments lazy: true
    }

    @Override
    /**
     *
      * @return String -
     */
    public String toString() {
        return conferenceTitle
    }

// --------------------------------------------
// functions
// --------------------------------------------

    boolean isDone(final String pPhase, final String pCurrentPhase) {
        switch (pCurrentPhase) {
            case "Open":
                return false

            case "External":
                return pPhase.equalsIgnoreCase("Open")

            case "Create Package":
                return pPhase.equalsIgnoreCase("Open")

            case "TD Review":
                return pPhase.equalsIgnoreCase("Open") || pPhase.equalsIgnoreCase("Create Package")

            case "AFRL Review":
                return pPhase.equalsIgnoreCase("Open") || pPhase.equalsIgnoreCase("Create Package") ||
                        pPhase.equalsIgnoreCase("TD Review")

            case "AFMC Review":
                return pPhase.equalsIgnoreCase("Open") || pPhase.equalsIgnoreCase("Create Package") ||
                        pPhase.equalsIgnoreCase("TD Review") || pPhase.equalsIgnoreCase("AFRL Review")

            case "SAF Review":
                return pPhase.equalsIgnoreCase("Open") || pPhase.equalsIgnoreCase("Create Package") ||
                        pPhase.equalsIgnoreCase("TD Review") || pPhase.equalsIgnoreCase("AFRL Review") ||
                        pPhase.equalsIgnoreCase("AFMC Review")

            case "Approved":
                return pPhase.equalsIgnoreCase("Open") || pPhase.equalsIgnoreCase("Create Package") || pPhase.equalsIgnoreCase("External") ||
                        pPhase.equalsIgnoreCase("TD Review") || pPhase.equalsIgnoreCase("AFRL Review") ||
                        pPhase.equalsIgnoreCase("AFMC Review") || pPhase.equalsIgnoreCase("SAF Review")

            case "Finalizing":
                return pPhase.equalsIgnoreCase("Open") || pPhase.equalsIgnoreCase("Create Package") || pPhase.equalsIgnoreCase("External") ||
                        pPhase.equalsIgnoreCase("TD Review") || pPhase.equalsIgnoreCase("AFRL Review") ||
                        pPhase.equalsIgnoreCase("AFMC Review") || pPhase.equalsIgnoreCase("SAF Review") ||
                        pPhase.equalsIgnoreCase("Approved")

            case "Closed":
                return pPhase.equalsIgnoreCase("Open") || pPhase.equalsIgnoreCase("Create Package") || pPhase.equalsIgnoreCase("External") ||
                        pPhase.equalsIgnoreCase("TD Review") || pPhase.equalsIgnoreCase("AFRL Review") ||
                        pPhase.equalsIgnoreCase("AFMC Review") || pPhase.equalsIgnoreCase("SAF Review") ||
                        pPhase.equalsIgnoreCase("Approved") || pPhase.equalsIgnoreCase("Finalizing")
        }
    }

    /**
     *
     * @return boolean - TRUE is CAO/Alternate CAO | FALSE is not CAO/Alternate CAO
     */
    boolean isCAO() {
        return conferenceService?.isCAO(this)
    }

    /**
     *
     * @return int - number of days the conference is
     */
    public int days() {
        if (startDate) {
            return endDate - startDate + 1
        } else {
            return 0
        }
    }

    /**
     *
     * @return double - the total amount of $ (estimate) that has been entered against the conference
     */
    double estimateTotal() {
        double total = 0.0

        for (attendee in attendees) {
            // the total estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Wait List") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                total += attendee?.estimateTotal()
            }
        }

        return total
    }

    /**
     *
     * @return double - the total amount of $ (estimate) that has been entered against the conference
     */
    double unconstrainedTotal() {
        double total = 0.0

        for (attendee in attendees) {
            // the total estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Wait List") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                total += (attendee?.estimateTotal() * attendee?.afFunding()) / 100
            }
        }

        return total
    }

    /**
     *
     * @return Map<String, Double> - the total amount of $ (estimate) that has been broken out by funding type
     */
    Map<String, Double> unconstrainedTotalByFunding() {
        Map<String, Double> result = new HashMap<String, Double>()

        for (attendee in attendees) {
            // the total estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Wait List") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                Map<String, Double> temp = attendee?.fundingEstimateTotal()
                for (item in temp) {
                    if (result?.containsKey(item.key)) {
                        Double value = result?.get(item.key)
                        result?.remove(item.key)
                        result?.put(item.key, value + item.value)
                    } else {
                        result.put(item.key, item.value)
                    }
                }
            }
        }

        return result
    }

    /**
     *
     * @return double - the total amount of $ (estimate) that has been entered against the conference
     */
    double validEstimateTotal() {
        double total = 0.0

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                total += attendee?.estimateTotal()
            }
        }

        return total
    }

    /**
     *
     * @return double - the total amount of $ (estimate) that has been entered against the conference
     */
    double constrainedTotal() {
        double total = 0.0

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // do not include CONTRACTOR amounts in CONSTRAINED total
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    total += 0
                } else {
                    total += (attendee?.estimateTotal() * attendee?.afFunding()) / 100
                }
            }
        }

        return total
    }

    /**
     *
     * @return int - the total number of attendees that are part of the constrained value
     */
    int constrainedCount() {
        int total = 0

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                total++
            }
        }

        return total
    }

    /**
     *
     * @return Map<String, Double> - the total amount of $ (constrained) that has been broken out by funding type
     */
    Map<String, Double> constrainedTotalByFunding() {
        Map<String, Double> result = new HashMap<String, Double>()

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // do not include CONTRACTOR amounts in CONSTRAINED total
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    // do nothing
                } else {
                    Map<String, Double> temp = attendee?.fundingEstimateTotal()
                    for (item in temp) {
                        if (result?.containsKey(item.key)) {
                            Double value = result?.get(item.key)
                            result?.remove(item.key)
                            result?.put(item.key, value + item.value)
                        } else {
                            result?.put(item.key, item.value)
                        }
                    }
                }
            }
        }

        return result
    }

    /**
     *
     * @return Map<String, Double> - the total amount of $ (constrained) that has been broken out by TD (only using AF funding)
     */
    Map<String, Double> constrainedTotalByTD() {
        Map<String, Double> result = new HashMap<String, Double>()

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // do not include CONTRACTOR amounts in CONSTRAINED total
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    // do nothing
                } else {
                    Double attendeeValue = (attendee?.estimateTotal() * attendee?.afFunding()) / 100

                    String key = "EXT"
                    if (attendee?.accountLink) {
                        if (attendee?.accountLink?.assignedTD?.topParent?.officeSymbol) {
                            key = attendee?.accountLink?.assignedTD?.topParent?.officeSymbol
                        } else {
                            key = "UNK"
                        }
                    } else if (attendee?.reservedTD) {
                        if (attendee?.reservedTD?.topParent?.officeSymbol) {
                            key = attendee?.reservedTD?.topParent?.officeSymbol
                        } else {
                            key = "UNK"
                        }
                    }

                    if (result?.containsKey(key)) {
                        Double value = result?.get(key)
                        result?.remove(key)
                        result?.put(key, value + attendeeValue)
                    } else {
                        result?.put(key, attendeeValue)
                    }
                }
            }
        }

        return result
    }

    /**
     *
     * @return Map<String, Double> - the total amount of $ (constrained) that has been broken out by TD (only using AF funding)
     */
    Map<String, Long> countTotalByTD() {
        Map<String, Long> result = new HashMap<String, Long>()

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // do not include CONTRACTOR amounts in CONSTRAINED total
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    // do nothing
                } else {
                    String key = "EXT"
                    if (attendee?.accountLink) {
                        if (attendee?.accountLink?.assignedTD?.topParent?.officeSymbol) {
                            key = attendee?.accountLink?.assignedTD?.topParent?.officeSymbol
                        } else {
                            key = "UNK"
                        }
                    } else if (attendee?.reservedTD) {
                        if (attendee?.reservedTD?.topParent?.officeSymbol) {
                            key = attendee?.reservedTD?.topParent?.officeSymbol
                        } else {
                            key = "UNK"
                        }
                    }

                    if (result?.containsKey(key)) {
                        Long value = result?.get(key)
                        result?.remove(key)
                        result?.put(key, value + 1)
                    } else {
                        result?.put(key, 1)
                    }
                }
            }
        }

        return result
    }

    /**
     *
     * @return Map<String, Double> - the total amount of $ (constrained) that has been broken out by TD (only using AF funding)
     */
    Set<String> conferenceTdList() {
        Set<String> result = new HashSet<String>()

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // do not include CONTRACTOR amounts in CONSTRAINED total
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    // do nothing
                } else {
                    if (attendee?.accountLink) {
                        if (attendee?.accountLink?.assignedTD?.topParent?.officeSymbol) {
                            result.add(attendee?.accountLink?.assignedTD?.topParent?.officeSymbol)
                        } else {
                            result?.add("UNK")
                        }
                    } else if (attendee?.reservedTD) {
                        if (attendee?.reservedTD?.topParent?.officeSymbol) {
                            result?.add(attendee?.reservedTD?.topParent?.officeSymbol)
                        } else {
                            result?.add("UNK")
                        }
                    } else {
                        result?.add("EXT")
                    }
                }
            }
        }

        return result
    }

    /**
     *
     * @return double - the total amount of $ (estimate) that has been entered against the conference
     */
    double otherEstimateTotal() {
        double total = 0.0

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Wait List") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                total += (attendee?.estimateTotal() * attendee?.otherFunding()) / 100
            }
        }

        return total
    }

    /**
     *
     * @return double - the total amount of $ (actual) that has been entered against the conference
     */
    double actualTotal() {
        double total = 0.0

        for (attendee in attendees) {
            // the total actual cost only includes
            // attendee that have registered/confirmed or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                total += attendee?.actualTotal()
            }
        }

        return total
    }

    /**
     *
     * @return double - the total amount of $ (actual) that has been entered against the conference
     */
    double constrainedActualTotal() {
        double total = 0.0

        for (attendee in attendees) {
            // the total actual cost only includes
            // attendee that have registered/confirmed or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // remove contractor $ from constrained amount
                if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    total += attendee?.actualTotal()
                }
            }
        }

        return total
    }

    /**
     *
     * @return double - the registration amount of $ (actual) that has been entered against the conference
     */
    double constrainedActualTotal_registration() {
        double total = 0.0

        for (attendee in attendees) {
            // the total actual cost only includes
            // attendee that have registered/confirmed or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // remove contractor $ from constrained amount
                if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    total += attendee?.actualTotal_registration()
                }
            }
        }

        return total
    }

    /**
     *
     * @return double - the travel amount of $ (actual) that has been entered against the conference
     */
    double constrainedActualTotal_travel() {
        double total = 0.0

        for (attendee in attendees) {
            // the total actual cost only includes
            // attendee that have registered/confirmed or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // remove contractor $ from constrained amount
                if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    total += attendee?.actualTotal_travel()
                }
            }
        }

        return total
    }

    /**
     *
     * @return double - the other amount of $ (actual) that has been entered against the conference
     */
    double constrainedActualTotal_other() {
        double total = 0.0

        for (attendee in attendees) {
            // the total actual cost only includes
            // attendee that have registered/confirmed or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // remove contractor $ from constrained amount
                if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    total += attendee?.actualTotal_other()
                }
            }
        }

        return total
    }

    /**
     *
     * @return double - the total amount of $ (actual) that has been entered against the conference
     */
    double contractorActualTotal() {
        double total = 0.0

        for (attendee in attendees) {
            // the total actual cost only includes
            // attendee that have registered/confirmed or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // count only contractor $
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    total += attendee?.actualTotal()
                }
            }
        }

        return total
    }

    /**
     *
     * @return int - the total number of attendees that are part of the constrained value
     */
    int constrainedActualCount() {
        int total = 0

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have registered/confirmed or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // remove contractor $ from constrained total
                if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    total++
                }
            }
        }

        return total
    }

    /**
     *
     * @return int - the total number of attendees that are part of the constrained value
     */
    int contractorActualCount() {
        int total = 0

        for (attendee in attendees) {
            // the total VALID estimate cost does not include
            // attendee that have registered/confirmed or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // count only contractor total
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    total++
                }
            }
        }

        return total
    }

    /**
     *
     * @return Map<String, Double> - the total amount of $ (estimate) that has been broken out by funding type
     */
    Map<String, Double> actualTotalByFunding() {
        Map<String, Double> result = new HashMap<String, Double>()

        for (attendee in attendees) {
            // the total actual cost only includes
            // attendee that have registered or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                Map<String, Double> temp = attendee.fundingActualTotal()
                for (item in temp) {
                    if (result?.containsKey(item.key)) {
                        Double value = result?.get(item.key)
                        result?.remove(item.key)
                        result?.put(item.key, value + item.value)
                    } else {
                        result?.put(item.key, item.value)
                    }
                }
            }
        }

        return result
    }

// --------------------------------------------
// phase actions
// --------------------------------------------

    /**
     *
     * @return Set<RefPhaseState> - possible actions available for the given phase
     */
    Set<RefPhaseState> availableActions() {
        RefPhaseState?.findAllByPhaseState(phaseState)?.collect { it } as Set
    }

    /**
     *
     * @param pActionCheck (String) - check action to verify before action can be performed
     * @return boolean - TRUE if action has checked positive | FALSE if action has failed the check
     */
    public boolean checkAction(final String pActionCheck) {
        return phaseStateService?.checkAction(this, pActionCheck)
    }

    /**
     *
     * @param pActionCommand (String) - check action to verify before action can be performed
     * @return boolean - TRUE if action has checked positive | FALSE if action has failed the check
     */
    public boolean checkPermission(final String pActionCommand) {
        return conferenceService?.checkPermission(this, pActionCommand)
    }

    /**
     *
     * @param pActionCheck (String) - check action to verify before action can be performed
     * @return String - action display message returned
     */
    public String checkActionMessage(final String pActionCheck) {
        return phaseStateService?.checkActionMessage(pActionCheck)
    }

    /**
     *
     * @param pAction (String) - action to be attempted at the given phase ['delete', 'manage', 'add', 'edit']
     * @param pAttendeeId (Long) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    public boolean canAttendee(final String pAction, final Long pAttendeeId) {
        return conferenceService?.canAttendee(pAction, pAttendeeId, this)
    }

    /**
     *
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    public boolean canManage() {
        return conferenceService?.canManage(this)
    }

    /**
     *
     * @param pAction (String) - action to be attempted at the given phase ['user', 'cao', 'td', 'fmc']
     * @param pAttendeeId (Long) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    public boolean canEstimate(final Long pAttendeeId) {
        return conferenceService?.canEstimate(pAttendeeId, this)
    }

    /**
     *
     * @param pAction (String) - action to be attempted at the given phase ['user', 'cao', 'td', 'fmc']
     * @param pAttendeeId (Long) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    public boolean canActual(final Long pAttendeeId) {
        return conferenceService.canActual(pAttendeeId, this)
    }

// --------------------------------------------
// files attached
// --------------------------------------------

    /**
     *
     * @return Set<FileUploadVw) - files attached to the conference
     */
    Set<FileUpload> getFiles() {
        FileUpload?.findAllByConference(this)?.collect { it } as Set
    }

// --------------------------------------------
// date events attached
// --------------------------------------------

    /**
     *
     * @return Set<DateEvent) - date events attached to the conference
     */
    List<DateEvent> getDateEvents() {
        DateEvent?.findAllByConference(this)?.collect { it }
    }

    /**
     *
     * @param pDateEventCode (String) -
     * @return
     */
    public boolean hasDate(final String pDateEventCode) {
        for (dateEvent in DateEvent.findAllByConference(this)) {
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

        List<DateEvent> dateEvent1 = DateEvent.findAllByConferenceAndDateGate(this, RefDateGate.findByCode(pDateEventCode1))
        List<DateEvent> dateEvent2 = DateEvent.findAllByConferenceAndDateGate(this, RefDateGate.findByCode(pDateEventCode2))

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

    public int getChangeDays() {
        return diffInDays(lastChangeDate, new Date())
    }

    public int getStatusDays() {
        return diffInDays(statusChangeDate, new Date())
    }

}
