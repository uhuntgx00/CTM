package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured
import groovy.json.JsonSlurper
import mil.ebs.ctm.ref.RefDateGate
import mil.ebs.ctm.ref.RefPhaseState
import mil.ebs.ctm.upload.FileUpload

import java.text.SimpleDateFormat
import java.util.zip.ZipOutputStream
import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["permitAll"])
class ConferenceController {

    static allowedMethods = [save: "POST", update: "PUT", performMerge: "POST", delete: "DELETE"]

    def fileService
    def conferenceService
    def attendeeService
    def phaseStateService
    def springSecurityService
    def exportService
    def summaryService

//***********************************************************************************************************************************************
// Conference LIST functions
//***********************************************************************************************************************************************

    /**
     *
     * @param pInitialList (List<Conference>) -
     * @return List<Conference>
     */
    private List<Conference> parseList(final List<Conference> pInitialList) {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.offset = params.offset ? params.int('offset') : 0

        return pInitialList.subList(params.int('offset'), Math.min(params.int('offset') + params.int('max'), pInitialList.size()))
    }

    /**
     *
     * @param max
     * @return
     */
    def index(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.offset = params.offset ? params.int('offset') : 0

        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThan(false, new Date() - 1, [offset: params.offset, max: params.max, sort: params.sort, order: params.order])
        int count = Conference.findAllByHideAndEndDateGreaterThan(false, new Date() - 1, [sort: params.sort, order: params.order]).size()

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        RefPhaseState phaseState = RefPhaseState.findByPhaseAction("Attend")
        def account = Account.get(springSecurityService.principal.id)

        respond conferenceList, model: [attendState: phaseState, account: account, conferenceInstanceCount: count, acd: acd, ccd: ccd, listType: 'Active']
    }

    /**
     *
     * @param max
     * @return
     */
    @Secured(['ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN'])
    def costs(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.list(sort: params.sort, order: params.order)

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Constrained Costs']
    }

    /**
     *
     * @param max
     * @return
     */
    @Secured(['ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN'])
    def ucosts(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.list(sort: params.sort, order: params.order)

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Unconstrained Costs']
    }

    /**
     * EXPORTABLE
     *
     * @param max
     * @return
     */
    @Secured(['ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN'])
    def allList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            List<ConferenceExport> exportList = new ArrayList<ConferenceExport>(0)
            for (conferenceInstance in Conference.list(sort: params.sort, order: params.order)) {
                exportList.add(createConferenceExport(conferenceInstance))
            }

            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]
            response.setHeader("Content-disposition", "attachment; filename=all_conferences.${params.extension}")
            exportService.export(params.formatType, response.outputStream, exportList, conferenceExportFields(), conferenceExportLabels(), [:], [:])
            return
        }

        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.offset = params.offset ? params.int('offset') : 0

        List<Conference> conferenceList = Conference.list(offset: params.offset, max: params.max, sort: params.sort, order: params.order)

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond conferenceList, view: 'index', model: [conferenceInstanceCount: Conference.count(), acd: acd, ccd: ccd, listType: 'ALL', listAction: 'allList', exportOption: true]
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    @Secured(['ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN'])
    def pendingList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThanEqualsAndStatus(false, new Date() - 1, 'Pending', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Pending']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def hidden(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThanEquals(true, new Date() - 1, [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Hidden']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def openList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThanEqualsAndStatus(false, new Date() - 1, 'Open', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        RefPhaseState phaseState = RefPhaseState.findByPhaseAction("Attend")
        def account = Account.get(springSecurityService.principal.id)

        respond parseList(conferenceList), view: 'index', model: [attendState: phaseState, account: account, conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Open']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def cancelledList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByStatus('Cancelled', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Cancelled']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def closedList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByPhaseState('Closed', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Closed']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    @Secured(['ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN'])
    def externalList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByPhaseState('External', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'External']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def archivedList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByStatus('Archived', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Archived']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def reviewingList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThanEqualsAndStatus(false, new Date() - 1, 'Reviewing', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Reviewing']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def draftingList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThanEqualsAndStatus(false, new Date() - 1, 'Drafting', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Drafting']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def processingList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThanAndStatus(false, new Date() - 1, 'Processing', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Processing']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def disapprovedList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThanEqualsAndStatus(false, new Date() - 1, 'Disapproved', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Disapproved']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def approvedList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThanEqualsAndStatus(false, new Date() - 1, 'Approved', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Approved']
    }

    /**
     *
     * @param max (Integer) - maximum records
     * @return
     */
    def finalizeList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByPhaseState('Finalizing', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Finalizing']
    }

    /**
     * ROLE_ADMIN only
     *
     * @param max (Integer) - maximum records
     * @return
     */
    @Secured(['ROLE_ADMIN'])
    def errorList(Integer max) {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Conference> conferenceList = Conference.findAllByStatus('*ERROR*', [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Error']
    }

    /**
     *
     * @return List of conferences for Responsible TD
     */
    def responsibleList() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        Organization organizationInstance = Organization.get(params.id).topParent
        List<Conference> conferenceList = Conference.findAllByResponsibleTD(organizationInstance, [sort: params.sort, order: params.order])

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            List<ConferenceExport> exportList = new ArrayList<ConferenceExport>(0)
            for (conferenceInstance in conferenceList) {
                exportList.add(createConferenceExport(conferenceInstance))
            }

            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]
            response.setHeader("Content-disposition", "attachment; filename=${organizationInstance?.officeSymbol}_all_conferences.${params.extension}")
            exportService.export(params.formatType, response.outputStream, exportList, conferenceExportFields(), conferenceExportLabels(), [:], [:])
            return
        }

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Responsible TD', listAction: 'responsibleList', exportOption: true]
    }

    /**
     *
     * @return List of conferences attending by Account
     */
    def conferenceList() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        params.max = 25

        def account = Account.get(params.id)
        def attendeeList = Attendee.findAllByAccountLink(account)

        List<Conference> conferenceList = new ArrayList<>()
        for (attendee in attendeeList) {
            conferenceList.add(attendee.conference)
        }

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond conferenceList, view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Attendee']
    }

    /**
     *
     * @return List of created conferences by Account
     */
    def conferenceCreatedList() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        params.max = 25

        def account = Account.get(params.id)
        def conferenceList = Conference.findAllByCreatedBy(account)

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond conferenceList, view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'Created']
    }

    /**
     *
     * @return List of conferences for a CAO
     */
    def conferenceCaoList() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        def account = Account.get(params.id)
        def conferenceList = Conference.findAllByConferenceAO(account)
        conferenceList.addAll(Conference.findAllByAlternateCAO(account))

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            List<ConferenceExport> exportList = new ArrayList<ConferenceExport>(0)
            for (conferenceInstance in conferenceList) {
                exportList.add(createConferenceExport(conferenceInstance))
            }

            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]
            response.setHeader("Content-disposition", "attachment; filename=${account.lastName}_cao_conferences.${params.extension}")
            exportService.export(params.formatType, response.outputStream, exportList, conferenceExportFields(), conferenceExportLabels(), [:], [:])
            return
        }

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: 'CAO/AAO', listAction: 'conferenceCaoList', exportOption: true]
    }

    /**
     *
     * @return
     */
    def worldList() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        Locale locale = new Locale("en", params.id)
        List<Address> addressList = Address.findAllByCountryAndAddressType(locale.getISO3Country().toLowerCase(), "Venue")

        List<Conference> conferenceList = new ArrayList<>()
        for (address in addressList) {
            if (address?.conference && !address?.conference?.hide && (address?.conference?.endDate > new Date() - 1)) {
                conferenceList.add(address.conference)
            }
        }

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: locale.getISO3Country(), listAction: 'worldList']
    }

    /**
     *
     * @return
     */
    def usaList() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        List<Address> addressList = Address.findAllByStateAndAddressType(params.id.toUpperCase(), "Venue")

        List<Conference> conferenceList = new ArrayList<>()
        for (address in addressList) {
            if (address?.conference && !address?.conference?.hide && (address?.conference?.endDate > new Date() - 1)) {
                conferenceList.add(address.conference)
            }
        }

        AttendanceChartData acd = new AttendanceChartData()
        acd.computeChartData(conferenceList)

        CostChartData ccd = new CostChartData()
        ccd.computeChartData(conferenceList)

        respond parseList(conferenceList), view: 'index', model: [conferenceInstanceCount: conferenceList.size(), acd: acd, ccd: ccd, listType: params.id.toUpperCase(), listAction: 'usaList']
    }

    /**
     *
     * @param pList
     * @param pMax
     * @param pOffset
     * @return
     */
    private static List<Attendee> paginateList(List<Attendee> pList, int pMax, int pOffset) {
        if (pList) {
            pList = pList.subList(pOffset, Math.min(pOffset + pMax, pList.size()))
        }

        return pList
    }

// ***********************************************************************************************************************************************
// Export methods
// ***********************************************************************************************************************************************

    /**
     *
     * @return List -
     */
    private static List conferenceExportFields() {
        return ["conferenceTitle", "phaseState", "status", "startDate", "endDate", "numAttendees", "maxAttendees", "constrainedCount", "venue", "address", "website", "purpose",
                "hostType", "afrlHosted", "coHostEntity", "nonHostType", "responsibleTD", "alternateRespTD", "ResponsibleOrg", "conferenceAO", "AlternateCAO", "afrlSoccer", "afrlSoccerDate", "afmcSoccer",
                "afmcSoccerDate", "safTmt", "safTmtDate", "approvalNotice", "disapproveNotice", "constrainedCost", "unconstrainedCost", "otherCost", "hidden"]
    }

    /**
     *
     * @return Map -
     */
    private static Map conferenceExportLabels() {
        return ["conferenceTitle":"Conference Title", "phaseState":"Phase", "status":"Status", "startDate":"Start Date", "endDate":"End Date",
                "numAttendees":"Num Attendees", "maxAttendees":"Max Attendees", "constrainedCount":"Constrained Count", "venue":"Venue",
                "address":"Address", "website":"Website", "purpose":"Purpose", "hostType":"Host Type", "afrlHosted":"AFRL Hosted", "coHostEntity":"CoHost Entity",
                "nonHostType":"Non-Host Type", "responsibleTD":"Resp TD", "alternateRespTD":"Alt Resp TD", "ResponsibleOrg":"Resp Org", "conferenceAO":"CAO", "AlternateCAO":"Alt CAO", "afrlSoccer":"AFRL Soccer",
                "afrlSoccerDate":"AFRL Soccer Date", "afmcSoccer":"AFMC Soccer", "afmcSoccerDate":"AFMC Soccer Date", "safTmt":"SAF TMT", "safTmtDate":"SAF TMT Date",
                "approvalNotice":"Approval Notice", "disapproveNotice":"Disapprove Notice", "constrainedCost":"Constrained", "unconstrainedCost":"Unconstrained", "otherCost":"Other", "hidden":"Hidden"]
    }

    /**
     * This function exports the CONFERENCE data as an excel file
     */
    def exportConference() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        Conference conferenceInstance = Conference.get(params.conferenceId)

        if (conferenceInstance == null) {
            notFound()
            return
        }

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]
            response.setHeader("Content-disposition", "attachment; filename=${conferenceInstance.toString().replaceAll(" ", "_")}.${params.extension}")

            List<ConferenceExport> exportList = new ArrayList<ConferenceExport>(0)
            exportList.add(createConferenceExport(conferenceInstance))

            exportService.export(params.formatType, response.outputStream, exportList, conferenceExportFields(), conferenceExportLabels(), [:], [:])
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference)
     * @return ConferenceExport
     */
    private static ConferenceExport createConferenceExport(final Conference pConferenceInstance) {
        ConferenceExport exportData = new ConferenceExport()

        exportData.setConferenceTitle(pConferenceInstance.conferenceTitle)
        exportData.setPhaseState(pConferenceInstance.phaseState)
        exportData.setStatus(pConferenceInstance.status)
        exportData.setStartDate(pConferenceInstance.startDate)
        exportData.setEndDate(pConferenceInstance.endDate)
        exportData.setNumAttendees(pConferenceInstance.attendees?.size())
        exportData.setMaxAttendees(pConferenceInstance.numAttendees)
        exportData.setConstrainedCount(pConferenceInstance.constrainedCount())
        exportData.setVenue(pConferenceInstance.venue)
        exportData.setAddress(pConferenceInstance.address?.toString())
        exportData.setWebsite(pConferenceInstance.website)
        exportData.setPurpose(pConferenceInstance?.purpose?.replaceAll("\\<.*?>",""))
        exportData.setHostType(pConferenceInstance.hostType)
        exportData.setAfrlHosted(pConferenceInstance.afrlHosted)
        exportData.setCoHostEntity(pConferenceInstance.coHostEntity)
        exportData.setNonHostType(pConferenceInstance.nonHostType)
        exportData.setResponsibleTD(pConferenceInstance.responsibleTD?.officeSymbol)
        exportData.setAlternateRespTD(pConferenceInstance.alternateRespTD?.officeSymbol)
        exportData.setResponsibleOrg(pConferenceInstance.responsibleOrg)
        exportData.setConferenceAO(pConferenceInstance.conferenceAO?.toString())
        exportData.setAlternateCAO(pConferenceInstance.alternateCAO?.toString())
        exportData.setAfrlSoccer(pConferenceInstance.afrlSoccer)
        exportData.setAfrlSoccerDate(pConferenceInstance.afrlSoccerDate)
        exportData.setAfmcSoccer(pConferenceInstance.afmcSoccer)
        exportData.setAfmcSoccerDate(pConferenceInstance.afmcSoccerDate)
        exportData.setSafTmt(pConferenceInstance.safTmt)
        exportData.setSafTmtDate(pConferenceInstance.safTmtDate)
        exportData.setApprovalNotice(pConferenceInstance.approvalNotice?.replaceAll("\\<.*?>",""))
        exportData.setDisapproveNotice(pConferenceInstance.disapproveNotice?.replaceAll("\\<.*?>",""))
        exportData.setConstrainedCost(pConferenceInstance.constrainedTotal())
        exportData.setUnconstrainedCost(pConferenceInstance.unconstrainedTotal())
        exportData.setOtherCost(pConferenceInstance.otherEstimateTotal())
        exportData.setHidden(pConferenceInstance.hide)

        return exportData
    }

    /**
     *
     * @return List -
     */
    private static List aarExportFields() {
        return ["afHostOrganization", "conferenceName", "startDate", "endDate", "locationCity", "locationState", "locationCountry", "venue", "originalEstimate", "totalCost",
                "conferenceHostingCosts", "registrationCosts", "travelCosts", "numSponsoredAttendees", "totalAttendees", "conferenceJustification", "nonFederalEntity", "spousalTravel",
                "nonFederalEntityCoSponsorship", "noCostContract", "approvalAuthorityTItle", "waiverApprovalMemoAttached", "notes"]
    }

    /**
     *
     * @return Map -
     */
    private static Map aarExportLabels() {
        return ["afHostOrganization":"AF Host Organization", "conferenceName":"Conference Name", "startDate":"Start Date", "endDate":"End Date", "locationCity":"Location-City", "locationState":"Location-State",
                "locationCountry":"Location-Country", "venue":"Venue", "originalEstimate":"Original Estimate of Total Costs (as submitted in the approval request - column added by AFRL)",
                "totalCost":"Total Cost (Should Equal the Sum of Columns K, L, and M)", "conferenceHostingCosts":"Conference Hosting Costs (AV, facilities rental, supplies, contracts, etc.)",
                "registrationCosts":"Registration Fees Collected from AF Sponsored Attendees", "travelCosts":"Attendee Travel Costs (Travel, Lodging, M&IE)",
                "numSponsoredAttendees":"Number of AF Sponsored Attendees", "totalAttendees":"Number of Total Attendees", "conferenceJustification":"Conference Justification",
                "nonFederalEntity":"Non-Federal Entity Conference Planner", "spousalTravel":"Spousal Travel", "nonFederalEntityCoSponsorship":"Co-Sponsorship with a non-Federal Entity",
                "noCostContract":"No-Cost Contract", "approvalAuthorityTItle":"Approval Authority Title", "waiverApprovalMemoAttached":"Attachment - Waiver/Approval Memo", "notes":"Notes"]
    }

    /**
     * This function exports the CONFERENCE data into AAR data as an excel file
     */
    def exportAAR() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        Conference conferenceInstance = Conference.get(params.conferenceId)

        if (conferenceInstance == null) {
            notFound()
            return
        }

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]
            response.setHeader("Content-disposition", "attachment; filename=${conferenceInstance.toString().replaceAll(" ", "_")}.AAR.${params.extension}")

            List<AAR_AF> exportList = new ArrayList<AAR_AF>(0)
            exportList.add(createAarExport(conferenceInstance))

            exportService.export(params.formatType, response.outputStream, exportList, aarExportFields(), aarExportLabels(), [:], [:])
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference)
     * @return ConferenceExport
     */
    private static AAR_AF createAarExport(final Conference pConferenceInstance) {
        AAR_AF exportData = new AAR_AF()

        exportData.setAfHostOrganization(pConferenceInstance?.responsibleTD?.toString())
        exportData.setConferenceName(pConferenceInstance?.conferenceTitle)
        exportData.setStartDate(pConferenceInstance?.startDate)
        exportData.setEndDate(pConferenceInstance?.endDate)
        exportData.setLocationCity(pConferenceInstance?.getAddress()?.city)
        exportData.setLocationState(pConferenceInstance?.getAddress()?.state)
        exportData.setLocationCountry(pConferenceInstance?.getAddress()?.country)
        exportData.setVenue(pConferenceInstance?.venue)
        exportData.setOriginalEstimate(pConferenceInstance?.constrainedTotal())
        exportData.setTotalCost(pConferenceInstance?.constrainedActualTotal())
        exportData.setConferenceHostingCosts(pConferenceInstance?.constrainedActualTotal_other())
        exportData.setRegistrationCosts(pConferenceInstance?.constrainedActualTotal_registration())
        exportData.setTravelCosts(pConferenceInstance?.constrainedActualTotal_travel())
        exportData.setNumSponsoredAttendees(pConferenceInstance?.constrainedCount())
        exportData.setTotalAttendees(pConferenceInstance?.attendees?.size())
        exportData.setConferenceJustification(pConferenceInstance?.purpose?.replaceAll("\\<.*?>",""))
        exportData.setNonFederalEntity("N/A")
        exportData.setSpousalTravel("No")
        exportData.setNonFederalEntityCoSponsorship("No")
        exportData.setNoCostContract("No")
        exportData.setApprovalAuthorityTItle("Deputy Chief Management COfficer")
        exportData.setWaiverApprovalMemoAttached("No")
        exportData.setNotes("XXX")

        return exportData
    }

    /**
     * This function exports the CONFERENCE data into AAR data as an excel file
     */
    def exportAAR_non() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        Conference conferenceInstance = Conference.get(params.conferenceId)

        if (conferenceInstance == null) {
            notFound()
            return
        }

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]
            response.setHeader("Content-disposition", "attachment; filename=${conferenceInstance.toString().replaceAll(" ", "_")}.AAR.${params.extension}")

            List<AAR_NON_AF> exportList = new ArrayList<AAR_NON_AF>(0)
            exportList.add(createAarNonExport(conferenceInstance))

            exportService.export(params.formatType, response.outputStream, exportList, aarExportFields(), aarExportLabels(), [:], [:])
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference)
     * @return ConferenceExport
     */
    private static AAR_NON_AF createAarNonExport(final Conference pConferenceInstance) {
        AAR_NON_AF exportData = new AAR_NON_AF()

        exportData.setAfOrganization(pConferenceInstance?.responsibleOrg)
        exportData.setConferenceName(pConferenceInstance?.conferenceTitle)
        exportData.setStartDate(pConferenceInstance?.startDate)
        exportData.setEndDate(pConferenceInstance?.endDate)
        exportData.setLocationCity(pConferenceInstance?.getAddress()?.city)
        exportData.setLocationState(pConferenceInstance?.getAddress()?.state)
        exportData.setLocationCountry(pConferenceInstance?.getAddress()?.country)
        exportData.setVenue(pConferenceInstance?.venue)
        exportData.setNonDodEntityName(pConferenceInstance?.coHostEntity)
        exportData.setEstimateCost(pConferenceInstance?.constrainedTotal())
        exportData.setTotalCost(pConferenceInstance?.constrainedActualTotal())
        exportData.setNumSponsoredAttendees(pConferenceInstance?.constrainedCount())
        exportData.setConferenceJustification(pConferenceInstance?.purpose?.replaceAll("\\<.*?>",""))
        exportData.setApprovalAuthorityTItle("Deputy Chief Management COfficer")
        exportData.setWaiverApprovalMemoAttached("No")
        exportData.setNotes("XXX")

        return exportData
    }

    /**
     * This function exports the Conference ATTENDEE data as an excel file
     */
    def exportAttendees() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        Conference conferenceInstance = Conference.get(params.conferenceId)

        if (conferenceInstance == null) {
            notFound()
            return
        }

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]
            response.setHeader("Content-disposition", "attachment; filename=${conferenceInstance.toString().replaceAll(" ", "_")}_attendees.${params.extension}")

            ArrayList<AttendeeExport> exportList = exportAttendeeData(conferenceInstance)

            List fields = ["conference", "td", "organization", "status", "account", "name", "rankGrade", "td_org", "attendanceType", "startTravelDate", "endTravelDate",
                    "supervisor", "justification", "est_registrationCost", "est_airfareCost", "est_localTravelCost", "est_lodgingCost", "est_lodgingCostTax", "est_mealsIncidentalCost",
                    "est_otherCost", "est_notes", "est_totalAf", "est_totalOther", "est_totalNon", "act_registrationCost", "act_airfareCost", "act_localTravelCost", "act_lodgingCost",
                    "act_lodgingCostTax", "act_mealsIncidentalCost", "act_otherCost", "act_notes", "act_totalAf", "act_totalOther", "act_totalNon"]

            Map labels = ["conference":"Conference", "td":"Resp TD", "organization":"Resp Org", "status":"Status", "account":"Account", "name":"Name", "rankGrade":"Rank/Grade",
                    "td_org":"TD Organization", "attendanceType":"Attendance Type", "startTravelDate":"Start Travel", "endTravelDate":"End Travel", "supervisor":"Supervisor", "justification":"Justification",
                    "est_registrationCost":"Est Registration", "est_airfareCost":"Est Airfare", "est_localTravelCost":"Est Travel", "est_lodgingCost":"Est Lodging", "est_lodgingCostTax":"Est Lodging Tax",
                    "est_mealsIncidentalCost":"Est Meals", "est_otherCost":"Est Cost", "est_notes":"Est Notes", "est_totalAf":"AF Est Total", "est_totalOther":"Other Est Total", "est_totalNon":"Non Est Total",
                    "act_registrationCost":"Act Registration", "act_airfareCost":"Act Airfare", "act_localTravelCost":"Act Travel", "act_lodgingCost":"Act Lodging", "act_lodgingCostTax":"Act Lodging Tax",
                    "act_mealsIncidentalCost":"Act Meals", "act_otherCost":"Act Cost", "act_notes":"Act Notes", "act_totalAf":"AF Act Total", "act_totalOther":"Other Act Total", "act_totalNon":"Non Act Total"]

            exportService.export(params.formatType, response.outputStream, exportList, fields, labels, [:], [:])
        }
    }

    private ArrayList<AttendeeExport> exportAttendeeData(Conference conferenceInstance) {
        List<AttendeeExport> exportList = new ArrayList<AttendeeExport>(0)
        for (attendee in conferenceInstance.attendees.sort { it?.sequence }) {

            Cost estimate = Cost.findByAttendeeAndCostType(attendee, "Estimate")
            Cost actual = Cost.findByAttendeeAndCostType(attendee, "Actual")

            AttendeeExport export = new AttendeeExport(conference: attendee?.conference, td_org: attendee?.accountLink?.assignedTD?.officeSymbol, td: attendee?.reservedTD?.officeSymbol,
                    organization: attendee?.reservedOrg, status: attendee?.status, sequence: attendee?.sequence,
                    account: attendee?.toString(), attendanceType: attendee?.attendanceType, startTravelDate: attendee?.startTravelDate, endTravelDate: attendee?.endTravelDate,
                    supervisor: attendee?.supervisor, justification: attendee?.justification?.replaceAll("\\<.*?>",""), name: attendee?.name, rankGrade: attendee?.accountLink?.rankGrade
            )

            export.id = attendee.id

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy")
            export.startTravelDateStr = sdf.format(attendee.startTravelDate)
            export.endTravelDateStr = sdf.format(attendee.endTravelDate)

            if (estimate) {
                export.setEst_registrationCost(estimate?.registrationCost)
                export.setEst_airfareCost(estimate?.airfareCost)
                export.setEst_localTravelCost(estimate?.localTravelCost)
                export.setEst_lodgingCost(estimate?.lodgingCost)
                export.setEst_lodgingCostTax(estimate?.lodgingCostTax)
                export.setEst_mealsIncidentalCost(estimate?.mealsIncidentalCost)
                export.setEst_otherCost(estimate?.otherCost)
                export.setEst_notes(estimate?.notes)
            }

            Map<String, Double> ectFunding = attendee.fundingEstimateTotal()
            if (ectFunding.get("US Air Force")) {
                export.setEst_totalAf(ectFunding.get("US Air Force"))
            }
            if (ectFunding.get("Other US Govt")) {
                export.setEst_totalOther(ectFunding.get("Other US Govt"))
            }
            if (ectFunding.get("Non-Federal Entity")) {
                export.setEst_totalNon(ectFunding.get("Non-Federal Entity"))
            }

            if (actual) {
                export.setAct_registrationCost(actual?.registrationCost)
                export.setAct_airfareCost(actual?.airfareCost)
                export.setAct_localTravelCost(actual?.localTravelCost)
                export.setAct_lodgingCost(actual?.lodgingCost)
                export.setAct_lodgingCostTax(actual?.lodgingCostTax)
                export.setAct_mealsIncidentalCost(actual?.mealsIncidentalCost)
                export.setAct_otherCost(actual?.otherCost)
                export.setAct_notes(actual?.notes)
            }

            Map<String, Double> actFunding = attendee.fundingActualTotal()
            if (actFunding.get("US Air Force")) {
                export.setAct_totalAf(actFunding.get("US Air Force"))
            }
            if (actFunding.get("Other US Govt")) {
                export.setAct_totalOther(actFunding.get("Other US Govt"))
            }
            if (actFunding.get("Non-Federal Entity")) {
                export.setAct_totalNon(actFunding.get("Non-Federal Entity"))
            }

            exportList.add(export)
        }

        exportList
    }

    /**
     * This function exports the Conference SUMMARY data as an excel file
     */
    def exportSummary() {
        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        Conference conferenceInstance = Conference.get(params.conferenceId)

        if (conferenceInstance == null) {
            notFound()
            return
        }

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]
            response.setHeader("Content-disposition", "attachment; filename=${conferenceInstance.toString().replaceAll(" ", "_")}_summary.${params.extension}")

            List<Summary> exportList = new ArrayList<>(0)
            for (summary in Summary.findAllByConference(conferenceInstance)) {
                exportList.add(summary)
            }

            List fields = ["conference", "summaryType", "summaryDate", "constrainedTotal", "unconstrainedTotal", "otherCostTotal", "actualTotal", "ctrActualTotal", "constrainedCount",
                    "unconstrainedCount", "otherCostCount", "actualCount", "ctrActualCount", "attendeeTotal", "boothTotal", "chairTotal", "panelTotal", "presenterTotal", "supportTotal",
                    "otherTotal", "attendeeCount", "boothCount", "chairCount", "panelCount", "presenterCount", "supportCount", "otherCount", "ctrTotal", "extTotal", "ctrCount", "extCount",
                    "pendingCount", "waitlistCount"]

            Map labels = ["conference":"Conference", "summaryType":"Type", "summaryDate":"Date", "constrainedTotal":"Constrained \$", "unconstrainedTotal":"Unconstrained \$", "otherCostTotal":"Other \$",
                    "actualTotal":"Actual \$", "ctrActualTotal":"CTR Actual \$", "constrainedCount":"Constrained", "unconstrainedCount":"Unconstrainted", "otherCostCount":"Other", "actualCount":"Actual",
                    "ctrActualCount":"CTR Actual", "attendeeTotal":"Attendee \$", "attendeeCount":"Attendee", "boothTotal":"Booth \$", "boothCount":"Booth", "chairTotal":"Chair \$", "chairCount":"Chair",
                    "panelTotal":"Panel \$", "panelCount":"Panel", "presenterTotal":"Presenter \$", "presenterCount":"Presenter", "supportTotal":"Support \$", "supportCount":"Support", "otherTotal":"Other \$",
                    "otherCount":"Other", "ctrTotal":"CTR \$", "ctrCount":"CTR", "extTotal":"Ext \$", "extCount":"Ext", "pendingCount":"Pending", "waitlistCount":"Waitlist"]

            exportService.export(params.formatType, response.outputStream, exportList, fields, labels, [:], [:])
        }
    }

// ***********************************************************************************************************************************************
// map/calendar methods
//
// ***********************************************************************************************************************************************

    private static void initCountryCodeMapping(final Map<String, Locale> pLocaleMap) {
        String[] countries = Locale.getISOCountries()
        for (String country : countries) {
            Locale locale = new Locale("", country)
            pLocaleMap.put(locale.getISO3Country().toUpperCase(), locale)
        }
    }

    private static String iso3CountryCodeToIso2CountryCode(final Map<String, Locale> pLocaleMap, final String pIso3CountryCode) {
        return pLocaleMap.get(pIso3CountryCode.toUpperCase()).getCountry()
    }

    def vmap() {
        Map<String, Locale> localeMap = new HashMap<>()
        initCountryCodeMapping(localeMap)

        Map<String, Integer> countMap = new HashMap<>()
        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThan(false, new Date() - 1)

        for (Conference conference : conferenceList) {
            if (conference?.address?.country) {
                String country = iso3CountryCodeToIso2CountryCode(localeMap, conference?.address?.country)
                if (countMap.containsKey(country.toLowerCase())) {
                    Integer value = countMap.get(country.toLowerCase())
                    value++
                    countMap.remove(country.toLowerCase())
                    countMap.put(country.toLowerCase(), value)
                } else {
                    countMap.put(country.toLowerCase(), 1)
                }
            }
        }

        def jsonString = countMap as JSON

        respond jsonString, view: 'vmap', model: [jsonData: jsonString]
    }

    def vmap_usa() {
        Map<String, Integer> countMap = new HashMap<>()
        List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThan(false, new Date() - 1)

        for (Conference conference : conferenceList) {
            if (conference?.address?.state && conference?.address?.state?.length() == 2) {
                String state = conference?.address?.state
                if (countMap.containsKey(state.toLowerCase())) {
                    Integer value = countMap.get(state.toLowerCase())
                    value++
                    countMap.remove(state.toLowerCase())
                    countMap.put(state.toLowerCase(), value)
                } else {
                    countMap.put(state.toLowerCase(), 1)
                }
            }
        }

        def jsonString = countMap as JSON

        respond jsonString, view: 'vmap_usa', model: [jsonData: jsonString]
    }

    def viewMy() {

    }

    def view() {

    }

    def viewConstrained() {

    }

    def viewMyData() {
        def account = Account.get(springSecurityService.principal.id)
        List<Attendee> attendeeList = Attendee.findAllByAccountLink(account)

        List<Conference> attendingList = new ArrayList<Conference>(0)
        for (attendee in attendeeList) {
            if (!attendee?.conference?.status?.equalsIgnoreCase("Archived") && !attendee?.conference?.status?.equalsIgnoreCase("Cancelled") && !attendee?.conference?.status?.equalsIgnoreCase("Disapproved")) {
                attendingList.add(attendee?.conference)
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
        Date start = sdf.parse(params.start)
        Date end = sdf.parse(params.end);

        List<ConferenceEventObject> conferenceList = new ArrayList<>()
        for (conference in attendingList) {
            ConferenceEventObject ceo = new ConferenceEventObject(title: conference.conferenceTitle, start: conference.startDate, end: conference.endDate, url: 'show/' + conference.id)
            conferenceList.add(ceo)
        }

        def jsonString = (conferenceList as JSON).toString()

        render jsonString
    }

    def viewData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
        Date start = sdf.parse(params.start)
        Date end = sdf.parse(params.end);

        List<Conference> conferences = Conference.findAllByHideAndStartDateGreaterThanEqualsAndEndDateLessThanEquals(false, start, end)

        List<ConferenceEventObjectConstrained> conferenceList = new ArrayList<>()
        for (conference in conferences) {
            ConferenceEventObjectConstrained ceoc = new ConferenceEventObjectConstrained(title: conference.conferenceTitle, start: conference.startDate, end: conference.endDate, backgroundColor: '#3A87AD', url: 'show/' + conference.id)
            conferenceList.add(ceoc)
        }

        def account = Account.get(springSecurityService.principal.id)
        List<Attendee> attendeeList = Attendee.findAllByAccountLink(account)

        List<Conference> attendingList = new ArrayList<Conference>(0)
        for (attendee in attendeeList) {
            if (!attendee?.conference?.status?.equalsIgnoreCase("Archived") && !attendee?.conference?.status?.equalsIgnoreCase("Cancelled") && !attendee?.conference?.status?.equalsIgnoreCase("Disapproved")) {
                attendingList.add(attendee?.conference)
            }
        }

        for (conference in attendingList) {
            ConferenceEventObjectConstrained ceoc = new ConferenceEventObjectConstrained(title: conference.conferenceTitle, start: conference.startDate, end: conference.endDate, backgroundColor: '#FF00FF', url: 'show/' + conference.id)
            if (conferenceList.contains(ceoc)) {
                conferenceList.remove(ceoc)
            }
            conferenceList.add(ceoc)
        }

        def jsonString = (conferenceList as JSON).toString()

        render jsonString
    }

    def viewDataConstrained() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
        Date start = sdf.parse(params.start)
        Date end = sdf.parse(params.end);

        List<Conference> conferences = Conference.findAllByHideAndStartDateGreaterThanEqualsAndEndDateLessThanEquals(false, start, end)

        List<ConferenceEventObjectConstrained> conferenceList = new ArrayList<>()
        for (conference in conferences) {

            double total = conference.constrainedTotal()
            String bColor = "#E00000"
            if (total < 15000) {
                bColor = "#00FF00"
            } else if (total < 20000) {
                bColor = "#FFFF00"
            }

            ConferenceEventObjectConstrained ceoc = new ConferenceEventObjectConstrained(title: conference.conferenceTitle, start: conference.startDate, end: conference.endDate, backgroundColor: bColor, url: 'show/' + conference.id)
            conferenceList.add(ceoc)
        }

        def jsonString = (conferenceList as JSON).toString()

        render jsonString
    }

    def viewAttendeeData(Conference pConferenceInstance) {
        def jsonString = (exportAttendeeData(pConferenceInstance) as JSON).toString()

        render jsonString
    }

    def viewEventData() {
        def conference = Conference.get(params.cid)
        def account = Account.get(springSecurityService.principal.id)

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
        Date start = sdf.parse(params.start)
        Date end = sdf.parse(params.end);

        List<ConferenceEventObject> conferenceList = new ArrayList<>()

        for (dateEvent in conference.dateEvents) {
            Date eventStart = dateEvent?.eventDate
            Date eventEnd = dateEvent?.eventDate + 2

            ConferenceEventObject ceo = new ConferenceEventObject(title: dateEvent?.dateGate?.code, start: eventStart, end: eventEnd)
            conferenceList.add(ceo)
        }

        Date eventStart = conference?.startDate - 1
        Date eventEnd = conference?.endDate + 2

        ConferenceEventObject ceo = new ConferenceEventObject(title: 'Conference', start: eventStart, end: eventEnd)
        conferenceList.add(ceo)

        def jsonString = (conferenceList as JSON).toString()

        render jsonString
    }

// ***********************************************************************************************************************************************
// standard action methods
//
// ***********************************************************************************************************************************************

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    def show(Conference conferenceInstance) {

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

/*
TODO: Replace the perdiem system with new DoD based perdium instead of GSA

        if (conferenceInstance?.address && (!conferenceInstance?.perdiem || !conferenceInstance?.meals)) {
            def mealrate
            def pdrate

            def zip = conferenceInstance?.address?.zipCode
            def city = conferenceInstance?.address?.city

//            def rest = new RestBuilder()
//            def resp = rest.get("http://explore.data.gov/resource/perdiem.json?fiscalyear=2014&zip=" + zip)

//            if (!resp) {
            def rest = new RestBuilder(proxy: ['wrightpatterson.proxy.us.af.mil': 8080])
            def resp = rest.get("http://explore.data.gov/resource/perdiem.json?fiscalyear=2014&zip=" + zip)
//            }

            if (resp) {
                String result = resp.json
                result.replace('[', '')
                result.replace(']', '')

                def json = new JsonSlurper().parseText(result)

                boolean firstPass = false
                json.each {
                    if (it.city.contains(city)) {
                        firstPass = true
                        mealrate = it.meals
                        pdrate = it.may
                    }
                }
                if (!firstPass) {
                    json.each {
                        if (it.city.contains("Standard")) {
                            mealrate = it.meals
                            pdrate = it.may
                        }
                    }
                }

                if (pdrate) {
                    if (Integer.parseInt(pdrate) > 0) {
                        conferenceInstance.perdiem = Integer.parseInt(pdrate)
                        conferenceInstance.meals = Integer.parseInt(mealrate)
                        conferenceInstance.save flush: true
                    }
                }
            }

            if (zip?.equalsIgnoreCase("75007")) {
                conferenceInstance.perdiem = 123
                conferenceInstance.meals = 71
                conferenceInstance.save flush: true
            }

            if (zip?.equalsIgnoreCase("41018")) {
                conferenceInstance.perdiem = 127
                conferenceInstance.meals = 56
                conferenceInstance.save flush: true
            }

            if (zip?.equalsIgnoreCase("45433")) {
                conferenceInstance.perdiem = 87
                conferenceInstance.meals = 56
                conferenceInstance.save flush: true
            }
        }
*/

        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        params.offset = params.offset ? params.int('offset') : 0

        def list = Attendee.findAllByConference(conferenceInstance, [sort: "sequence", max: params.max, offset: params.offset])

//        def attendeeList = conferenceInstance?.attendees?.sort { it?.sequence }
//        def list = paginateList(attendeeList, params.int('max'), params.int('offset'))

        ConferenceComment displayComment = null
        if (conferenceInstance?.comments) {
            displayComment = conferenceInstance?.comments?.sort { it?.id }?.getAt(conferenceInstance?.comments?.size()-1)
        }

        ConferenceStatusBlock csb = conferenceService.determineConferenceStatusBlock(false, conferenceInstance)

        respond conferenceInstance, model: [attendeeList: list, attendeeListCount: conferenceInstance?.attendees?.size(),
                displayComment: displayComment, csb: csb]
    }

    /**
     *
     * @return
     */
    def create() {
        Conference conference = new Conference(params)
        conference.phaseState = 'Open'

        // TODO: if conference requires approval first? than make the status "Pending"
        conference.status = 'Open'

        conference.displayAfter = new Date() - 1
        conference.createdDate = new Date()

        respond conference
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def save(Conference pConferenceInstance) {
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'create'
            return
        }

        def account = Account.get(springSecurityService.principal.id)

        // update the conference status change information
        pConferenceInstance.statusChangeDate = new Date()
        pConferenceInstance.statusChangedBy = account

        pConferenceInstance.createdBy = account
        pConferenceInstance.createdDate = new Date()
        pConferenceInstance.lastChange = account
        pConferenceInstance.lastChangeDate = new Date()
        pConferenceInstance.accountEdit++

        // ensure that the dates are in PROPER order
        if (pConferenceInstance.startDate > pConferenceInstance.endDate) {
            def temp = pConferenceInstance.endDate
            pConferenceInstance.endDate = pConferenceInstance.startDate
            pConferenceInstance.startDate = temp
        }

        pConferenceInstance.save flush: true

        // create date event associated with the creation of the conference
        new DateEvent(dateGate: RefDateGate.findByCode("DCC"), eventDate: new Date(), recordedBy: account, conference: pConferenceInstance).save(flush: true)

        // TODO: Remove this functionality
//        ApprovalRequest aRequest = new ApprovalRequest(conference: pConferenceInstance, numAttendees: pConferenceInstance?.numAttendees, status: 'Pending', approveByDate: pConferenceInstance?.startDate - 30).save(flush: true)
//        pConferenceInstance.approvalRequest = aRequest
//        pConferenceInstance.save flush: true

        Attendee attendee = attendeeService.create(pConferenceInstance)
        boolean attendeeCreated = false
        if (attendee) {
            // ensure that the attendee is not a CONTRACTOR (without the can attend flag set to TRUE)
            // account must have ROLE_AFRL_USER as a role to attend as well
            // TODO: This needs to be shortened to just look at the CanAttendConferences
            if (attendee?.accountLink?.hasAuthority("ROLE_AFRL_USER")) {
                if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor")) {
                    attendee.save flush: true
                    attendeeCreated = true
                    new DateEvent(dateGate: RefDateGate.findByCode("DAC"), eventDate: new Date(), recordedBy: account, attendee: attendee).save(flush: true)
                } else {
                    if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") && attendee?.accountLink?.canAttendConferences) {
                        attendee.save flush: true
                        attendeeCreated = true
                        new DateEvent(dateGate: RefDateGate.findByCode("DAC"), eventDate: new Date(), recordedBy: account, attendee: attendee).save(flush: true)
                    }
                }
            }
        }

        conferenceService.determineResponsibleTD(pConferenceInstance)

        pConferenceInstance.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary("Current", pConferenceInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'conferenceInstance.label', default: 'Conference'), pConferenceInstance.conferenceTitle])

                RefPhaseState phaseState = RefPhaseState.findByPhaseStateAndPhaseAction("Open", "Venue Address")
                if (attendeeCreated) {
                    redirect controller:"address", params: [conferenceId: pConferenceInstance?.id, refId: phaseState?.id, attendeeId: attendee?.id], action:"createVenueAddressWizard1", method:"GET"
                } else {
                    redirect controller:"address", params: [conferenceId: pConferenceInstance?.id, refId: phaseState?.id], action:"createVenueAddressWizard2", method:"GET"
                }
            }
            '*' { respond pConferenceInstance, [status: CREATED] }
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    def edit(Conference conferenceInstance) {
        respond conferenceInstance
    }

    def ajaxGetAccounts() {
        def accountList = Organization.get(params.id)?.getAssignedAllowed()

        accountList = accountList.sort() { it.username }
        accountList = accountList.collect() {
            new NameIdGSP(id: it.id, name: it.displayName)
        }

        render template: 'accountCaoBlock', model: [accountList: accountList]
    }

    def ajaxGetAccountsAlt() {
        def accountList = Organization.get(params.id)?.getAssignedAllowed()

        accountList = accountList.sort() { it.username }
        accountList = accountList.collect() {
            new NameIdGSP(id: it.id, name: it.displayName)
        }

        render template: 'accountAltBlock', model: [accountList: accountList]
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def update(Conference pConferenceInstance) {
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        conferenceService.checkConferenceData(pConferenceInstance)
        conferenceService.determineResponsibleTD(pConferenceInstance)

        def account = Account.get(springSecurityService.principal.id)
        pConferenceInstance.lastChange = account
        pConferenceInstance.lastChangeDate = new Date()
        pConferenceInstance.accountEdit++

        // ensure that the dates are in PROPER order
        if (pConferenceInstance.startDate > pConferenceInstance.endDate) {
            def temp = pConferenceInstance.endDate
            pConferenceInstance.endDate = pConferenceInstance.startDate
            pConferenceInstance.startDate = temp
        }

        pConferenceInstance.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pConferenceInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.conferenceTitle])
                redirect pConferenceInstance
            }
            '*' { respond pConferenceInstance, [status: OK] }
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def cancel(Conference conferenceInstance) {
        if (conferenceInstance == null) {
            notFound()
            return
        }

        if (conferenceInstance.hasErrors()) {
            respond conferenceInstance.errors, view: 'edit'
            return
        }

        def account = Account.get(springSecurityService.principal.id)

        conferenceInstance?.status = 'Cancelled'
        conferenceInstance?.statusChangeDate = new Date()
        conferenceInstance?.statusChangedBy = account

        conferenceInstance.lastChange = account
        conferenceInstance.lastChangeDate = new Date()

        conferenceInstance?.save flush: true
        //todo: send out notification email for status change

        List<Attendee> attendeeList = Attendee.findAllByConference(conference, [sort: "id"])
        for (attendee in attendeeList) {
            attendee?.status = "Cancelled"
            attendee?.cancelledDate = new Date()
            attendee?.cancelledBy = account
            attendee?.save flush: true
            //todo: send out notification email for status change
        }

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), conferenceInstance.id])
                redirect conferenceInstance
            }
            '*' { respond conferenceInstance, [status: OK] }
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def delete(Conference conferenceInstance) {

        if (conferenceInstance == null) {
            notFound()
            return
        }

        conferenceInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Conference.label', default: 'Conference'), conferenceInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    /**
     *
     */
    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'conferenceInstance.label', default: 'Conference'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

// ***********************************************************************************************************************************************
// Date events methods
//
// ***********************************************************************************************************************************************

    /**
     *
     * @return
     */
    def deleteDateEvent() {
        def conferenceInstance = Conference.get(params.conferenceId)

        if (conferenceInstance == null) {
            notFound()
            return
        }

        def dateEventInstance = DateEvent.findById(params.id)

        if (dateEventInstance == null) {
            notFound()
            return
        }

        if (dateEventInstance.dateGate.canDelete) {
            dateEventInstance.delete(flush: true)
        }

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
                redirect(action: "show", id: conferenceInstance.id)
            }
            '*' { redirect(action: "show", id: conferenceInstance.id) }
        }
    }

// ***********************************************************************************************************************************************
// file methods
//
// ***********************************************************************************************************************************************

    /**
     *
     * @return
     */
    def retrieveFile() {
        try {
            File file = fileService.retrieveFile(params.id)

            response.setContentType("applcation-xdownload")
            response.setHeader("Content-Disposition", "attachment; filename=${file.getName()}")
            response.getOutputStream() << new ByteArrayInputStream(file.readBytes())
        } catch (FileNotFoundException ignore) {
            flash.message = "Associated file not found on Server!"
            redirect (url:'/')
        }
    }

    /**
     *
     * @return
     */
    def retrieveAllFiles() {
        String id = params.conferenceId
        def conferenceInstance = Conference.findById(Long.parseLong(id))

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream()
            ZipOutputStream zipFile = fileService.retrieveAllFiles(baos, id)

            response.setContentType("application/zip")
            response.setHeader("Content-Disposition", "filename=\"${conferenceInstance.toString()}.zip\"")
            response.getOutputStream() << baos.toByteArray()
            response.getOutputStream().flush()
        } catch (FileNotFoundException ignore) {
            flash.message = "Associated file not found on Server!"
            redirect (url:'/')
        }
    }

    /**
     *
     * @return
     */
    def retrieveApprovalMemo() {
        String id = params.conferenceId
        def conferenceInstance = Conference.findById(Long.parseLong(id))

        try {
            File file = fileService.retrieveFile("Approval Memo", conferenceInstance)

            response.setContentType("applcation-xdownload")
            response.setHeader("Content-Disposition", "attachment; filename=${file.getName()}")
            response.getOutputStream() << new ByteArrayInputStream(file.readBytes())
        } catch (FileNotFoundException ignore) {
            flash.message = "Associated file not found on Server!"
            redirect (url:'/')
        }
    }

    /**
     *
     * @return
     */
    def deleteFile() {
        FileUpload fileUpload = FileUpload.get(params.id)

        fileService.deleteFile(params.id)

        def conferenceInstance = Conference.get(params.conferenceId)
        for (dateEvent in DateEvent.findAllByConference(conferenceInstance)) {
            if (fileUpload?.fileType?.equalsIgnoreCase("Package") && dateEvent?.dateGate?.code?.equalsIgnoreCase("DCPU")) {
                dateEvent.delete(flush: true)
            }

            if (fileUpload?.fileType?.equalsIgnoreCase("CRF") && dateEvent?.dateGate?.code?.equalsIgnoreCase("DCPU")) {
                dateEvent.delete(flush: true)
            }

            if (fileUpload?.fileType?.equalsIgnoreCase("TD Doc") && dateEvent?.dateGate?.code?.equalsIgnoreCase("DCPU")) {
                dateEvent.delete(flush: true)
            }

            if (fileUpload?.fileType?.equalsIgnoreCase("Agenda") && dateEvent?.dateGate?.code?.equalsIgnoreCase("DCPU")) {
                dateEvent.delete(flush: true)
            }

            if (fileUpload?.fileType?.equalsIgnoreCase("CAPE") && dateEvent?.dateGate?.code?.equalsIgnoreCase("DCPU")) {
                dateEvent.delete(flush: true)
            }

            if (fileUpload?.fileType?.equalsIgnoreCase("SAFmemo") && dateEvent?.dateGate?.code?.equalsIgnoreCase("DSMU")) {
                dateEvent.delete(flush: true)
            }

            if (fileUpload?.fileType?.equalsIgnoreCase("Approval Memo") && dateEvent?.dateGate?.code?.equalsIgnoreCase("DSMU")) {
                dateEvent.delete(flush: true)
            }

            if (fileUpload?.fileType?.equalsIgnoreCase("AAR") && dateEvent?.dateGate?.code?.equalsIgnoreCase("DAARU")) {
                dateEvent.delete(flush: true)
            }
        }

        redirect(action: "show", id: params.conferenceId)
    }

// ***********************************************************************************************************************************************
// attendee priority functionality
//
// UP | DOWN sequence priority
// ***********************************************************************************************************************************************

    @Transactional
    def downAttendee(Conference pConferenceInstance) {
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        def attendeeInstance = Attendee.get(params.attendeeId)
        if (attendeeInstance) {
            int newSequence = attendeeInstance?.sequence + 1
            if (newSequence <= pConferenceInstance?.attendees?.size()) {
                for (attendee in pConferenceInstance.attendees) {
                    if (attendee.sequence == newSequence) {
                        attendee.sequence = attendee.sequence - 1
                    }

                    if (attendee == attendeeInstance) {
                        attendee.sequence = newSequence
                    }

                    attendee.save flush: true
                }

                attendeeService.determinePriorityList(pConferenceInstance)

                pConferenceInstance.save flush: true
            }
        }

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    @Transactional
    def upAttendee(Conference pConferenceInstance) {
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        def attendeeInstance = Attendee.get(params.attendeeId)
        if (attendeeInstance) {
            int newSequence = attendeeInstance?.sequence - 1
            if (newSequence > 0) {
                for (attendee in pConferenceInstance.attendees) {
                    if (attendee.sequence == newSequence) {
                        attendee.sequence = attendee.sequence + 1
                    }

                    if (attendee == attendeeInstance) {
                        attendee.sequence = newSequence
                    }

                    attendee.save flush: true
                }

                attendeeService.determinePriorityList(pConferenceInstance)

                pConferenceInstance.save flush: true
            }
        }

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

// ***********************************************************************************************************************************************
// action methods invoked by phase processing
//
// actions controlled:
//      Cancel, Forward, External, Approve, Disapprove, SubmitPackage, SubmitAFMC, SubmitSAF,
//      Return, Revise, Archive, Open, Finalize, UploadPackage, UploadSAFmemo
//      Attend, AddTdSlot, AddOpenSlot
// ***********************************************************************************************************************************************

    /**
     * ACTION: Attend
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def attendConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
            return
        }

        // ----------------------------------------
        // create new attendee
        // add attendee to conference
        // ----------------------------------------
        Attendee attendee = attendeeService.create(pConferenceInstance)
        pConferenceInstance.attendees.add(attendee)

        def account = Account.get(springSecurityService.principal.id)
        new DateEvent(dateGate: RefDateGate.findByCode("DAC"), eventDate: new Date(), recordedBy: account, attendee: attendee).save(flush: true)

        conferenceService.determineResponsibleTD(pConferenceInstance)

        pConferenceInstance.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pConferenceInstance)

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
//                flash.message = message(code: 'default.addedAttendee.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
//                redirect(action: "show", id: pConferenceInstance.id)
//                redirect controller:"cost", params: ['attendee.id': attendee.id, costType: "Estimate"], action:"createEstimateCost", method:"GET"
                redirect controller:"attendee", id: attendee.id, action:"conferenceEditWizard", method:"GET"
            }
            '*' {
//                redirect(action: "show", id: pConferenceInstance.id)
//                redirect controller:"cost", params: ['attendee.id': attendee.id, costType: "Estimate"], action:"createEstimateCost", method:"GET"
                redirect controller:"attendee", id: attendee.id, action:"conferenceEditWizard", method:"GET"
            }
        }
    }

    /**
     * ACTION: AddTdSlot
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def addTdSlot(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // ----------------------------------------
        // create new attendee
        // add attendee to conference
        // ----------------------------------------
        Attendee attendee = attendeeService.create(pConferenceInstance, false, true)
        pConferenceInstance.attendees.add(attendee)

        def account = Account.get(springSecurityService.principal.id)
        new DateEvent(dateGate: RefDateGate.findByCode("DAC"), eventDate: new Date(), recordedBy: account, attendee: attendee).save(flush: true)

        conferenceService.determineResponsibleTD(pConferenceInstance)

        pConferenceInstance.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pConferenceInstance)

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.addedAttendee.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: AddOpenSlot
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def addOpenSlot(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // ----------------------------------------
        // create new attendee
        // add attendee to conference
        // ----------------------------------------
        Attendee attendee = attendeeService.create(pConferenceInstance, false, false)
        pConferenceInstance.attendees.add(attendee)

        def account = Account.get(springSecurityService.principal.id)
        new DateEvent(dateGate: RefDateGate.findByCode("DAC"), eventDate: new Date(), recordedBy: account, attendee: attendee).save(flush: true)

        conferenceService.determineResponsibleTD(pConferenceInstance)

        pConferenceInstance.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pConferenceInstance)

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: Cancel
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def cancelConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // -----------------------------------
        // update Attendee information
        // -----------------------------------
        List<Attendee> attendeeList = Attendee.findAllByConference(pConferenceInstance, [sort: "id"])
        for (attendee in attendeeList) {
            attendee?.status = "Cancelled"
            attendee?.cancelledDate = new Date()
            attendee?.cancelledBy = Account.get(1)
            attendee?.save flush: true
        }

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "index")
            }
            '*' { redirect(action: "index") }
        }
    }

    /**
     * ACTION: Forward
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def forwardConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: External
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def externalConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: Approve
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def approveConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary("Before", pConferenceInstance)

        respond pConferenceInstance, view: 'approval'
    }

    @Transactional
    def approve(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        def account = Account.get(springSecurityService.principal.id)
        pConferenceInstance.statusChangedBy = account
        pConferenceInstance.statusChangeDate = new Date()

        pConferenceInstance.save flush: true

        // -----------------------------------
        // update Attendee information
        // -----------------------------------
        for (attendee in pConferenceInstance.attendees) {
            if (attendee?.status?.equalsIgnoreCase("Pending")
                    || (attendee?.status?.equalsIgnoreCase("Wait List") && attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor")))
            {
                attendee?.status = "Approved"
                attendee?.lastChangeDate = new Date()
                attendee?.lastChange = account
                attendee?.approvalRequestDate = new Date()
                attendee?.approvalRequestBy = account

                attendee?.save flush: true
                new DateEvent(dateGate: RefDateGate.findByCode("DAA1"), eventDate: new Date(), recordedBy: account, attendee: attendee).save(flush: true)
            }
        }

        pConferenceInstance.save flush: true

        conferenceService.approvePhaseState(pConferenceInstance)

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
                redirect id: pConferenceInstance.id, action:"show"
            }
            '*' {
                redirect id: pConferenceInstance.id, action:"show"
            }
        }
    }

    /**
     * ACTION: Disapprove
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def disapproveConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        respond pConferenceInstance, view: 'disapprove'
    }

    @Transactional
    def disapprove(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        def account = Account.get(springSecurityService.principal.id)
        pConferenceInstance.statusChangedBy = account
        pConferenceInstance.statusChangeDate = new Date()

        pConferenceInstance.save flush: true

        // -----------------------------------
        // update Attendee information
        // -----------------------------------
        for (attendee in pConferenceInstance.attendees) {
            attendee?.status = "Disapproved"
            attendee?.lastChangeDate = new Date()
            attendee?.lastChange = account
            attendee?.rejectedDate = new Date()
            attendee?.rejectedBy = account

            attendee?.save flush: true
            new DateEvent(dateGate: RefDateGate.findByCode("DAD"), eventDate: new Date(), recordedBy: account, attendee: attendee).save(flush: true)

            //todo: send out notification email for status change
        }

        pConferenceInstance.save flush: true

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
                redirect id: pConferenceInstance.id, action:"show"
            }
            '*' {
                redirect id: pConferenceInstance.id, action:"show"
            }
        }
    }

    /**
     * ACTION: SubmitPackage
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def submitPackage(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary("Baseline", pConferenceInstance)

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: SubmitAFMC
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def submitAFMC(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: SubmitSAF
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def submitSAF(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: Return
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def returnConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: Revise
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def reviseConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: Archive
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def archiveConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary("After", pConferenceInstance)

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: Open
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def openConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: Baseline
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def baselineConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary("Baseline", pConferenceInstance)

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: Finalize
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def finalizeConference(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'index' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: UploadPackage
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def uploadPackage(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     * ACTION: UploadSAFmemo
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def uploadSAFmemo(Conference pConferenceInstance) {
        // does conference exist?
        if (pConferenceInstance == null) {
            notFound()
            return
        }

        // any errors?
        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'edit'
            return
        }

        // ----------------------------------------
        // update conference information
        // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        if (phaseStateService.checkAction(pConferenceInstance, phaseState?.actionCheck)) {
            conferenceService.updateConferenceDataViaPhaseState(pConferenceInstance, phaseState)
        } else {
            // render error message for failed action check
            flash.message = phaseStateService.checkActionMessage(phaseState?.actionCheck)
            redirect(action: "show")
        }

        // return the "user" to the 'show' conference page
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.id])
                redirect(action: "show", id: pConferenceInstance.id)
            }
            '*' { redirect(action: "show", id: pConferenceInstance.id) }
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    def manage(Conference pConferenceInstance) {
        List<ManageAttendee> attendeeList = new ArrayList<>()

        for (attendee in pConferenceInstance?.attendees) {
            String display
            if (attendee?.toString()?.equalsIgnoreCase('TBD')) {
                if (attendee?.rankGrade) {
                    display = "(" + getAttendanceTypeLetter(attendee?.attendanceType) + ") - " + attendee?.status + " - " + (attendee?.name ? attendee?.name : "TBD") + " [" + attendee?.rankGrade?.code + "]"
                } else {
                    display = "(" + getAttendanceTypeLetter(attendee?.attendanceType) + ") - " + attendee?.status + " - " + (attendee?.name ? attendee?.name : "TBD")
                }
            } else {
                display = "(" + getAttendanceTypeLetter(attendee?.attendanceType) + ") - " + attendee?.status + " - " + attendee?.toString()
            }
            attendeeList.add(new ManageAttendee(id: attendee?.id, sequence: attendee?.sequence, display: display))
        }

        respond pConferenceInstance, model: [attendeeList: attendeeList.sort{it.sequence}]
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def manageUpdate(Conference pConferenceInstance) {

        def attendeeList = params.attendees
//        print attendeeList

        if (pConferenceInstance == null) {
            notFound()
            return
        }

        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'manage'
            return
        }

        List<Attendee> newAttendees = new ArrayList<>()

        int seqCount = 1
        for (String id in attendeeList) {
            Attendee attendee = Attendee.get(Integer.parseInt(id))
            if (attendee) {
                attendee?.sequence = seqCount
                newAttendees.add(attendee)
                seqCount++
            }
        }

//        print "new: " + newAttendees

        def account = Account.get(springSecurityService.principal.id)
        pConferenceInstance.lastChange = account
        pConferenceInstance.lastChangeDate = new Date()
        pConferenceInstance.accountEdit++

        pConferenceInstance.save flush: true

        attendeeService.determinePriorityList(pConferenceInstance)

        pConferenceInstance.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pConferenceInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.conferenceTitle])
                redirect pConferenceInstance
            }
            '*' { respond pConferenceInstance, [status: OK] }
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    def manageCosts(Conference pConferenceInstance) {
        List<ManageAttendee> attendeeList = new ArrayList<>()

        for (attendee in pConferenceInstance?.attendees) {
            String display
            if (attendee?.toString()?.equalsIgnoreCase('TBD')) {
                if (attendee?.rankGrade) {
                    display = "(" + getAttendanceTypeLetter(attendee?.attendanceType) + ") - " + attendee?.status + " - " + (attendee?.name ? attendee?.name : "TBD") + " [" + attendee?.rankGrade?.code + "]"
                } else {
                    display = "(" + getAttendanceTypeLetter(attendee?.attendanceType) + ") - " + attendee?.status + " - " + (attendee?.name ? attendee?.name : "TBD")
                }
            } else {
                display = "(" + getAttendanceTypeLetter(attendee?.attendanceType) + ") - " + attendee?.status + " - " + attendee?.toString()
            }
            attendeeList.add(new ManageAttendee(id: attendee?.id, sequence: attendee?.sequence, display: display))
        }

        def jsonString = (exportAttendeeData(pConferenceInstance) as JSON).toString()

//        print "data: " + jsonString

        respond pConferenceInstance, model: [attendeeList: attendeeList.sort{it.sequence}, jsonData: jsonString]
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def manageCostsUpdate(Conference pConferenceInstance) {
        String jsonData = params.data_return

        List<AttendeeExport> attendeeList = new ArrayList<>()
        attendeeList.addAll(new JsonSlurper().parseText(jsonData))

        if (pConferenceInstance == null) {
            notFound()
            return
        }

        if (pConferenceInstance.hasErrors()) {
            respond pConferenceInstance.errors, view: 'manage'
            return
        }

        for (attendeeExport in attendeeList) {
            if (attendeeExport.id) {
                Attendee attendee = Attendee.get(attendeeExport.id)

                attendee.attendanceType = attendeeExport.attendanceType


                def account = Account.get(springSecurityService.principal.id)
                if (account.hasAuthority("ROLE_FMC_ADMIN")) {
                    attendee.status = attendeeExport.status
                }
    //            attendee.startTravelDate = attendeeExport.startTravelDateStr
    //            attendee.endTravelDate = attendeeExport.endTravelDateStr

                attendee.save flush: true

                Cost estimate = Cost.findByAttendeeAndCostType(attendee, "Estimate")
                if (!estimate) {
                    estimate = new Cost(attendee: attendee, costType: "Estimate")
                    attendee.costs.add(estimate)
                }
                estimate.setAirfareCost(attendeeExport.est_airfareCost)
                estimate.setLodgingCost(attendeeExport.est_lodgingCost)
                estimate.setLodgingCostTax(attendeeExport.est_lodgingCostTax)
                estimate.setLocalTravelCost(attendeeExport.est_localTravelCost)
                estimate.setMealsIncidentalCost(attendeeExport.est_mealsIncidentalCost)
                estimate.setOtherCost(attendeeExport.est_otherCost)
                estimate.setRegistrationCost(attendeeExport.est_registrationCost)

                estimate.save flush: true

                Cost actual = Cost.findByAttendeeAndCostType(attendee, "Actual")
                if (!actual) {
                    actual = new Cost(attendee: attendee, costType: "Actual")
                    attendee.costs.add(actual)
                }
                actual.setAirfareCost(attendeeExport.act_airfareCost)
                actual.setLodgingCost(attendeeExport.act_lodgingCost)
                actual.setLodgingCostTax(attendeeExport.act_lodgingCostTax)
                actual.setLocalTravelCost(attendeeExport.act_localTravelCost)
                actual.setMealsIncidentalCost(attendeeExport.act_mealsIncidentalCost)
                actual.setOtherCost(attendeeExport.act_otherCost)
                actual.setRegistrationCost(attendeeExport.act_registrationCost)

                actual.save flush: true

                attendee.save flush: true
            }
        }

        def account = Account.get(springSecurityService.principal.id)
        pConferenceInstance.lastChange = account
        pConferenceInstance.lastChangeDate = new Date()
        pConferenceInstance.accountEdit++

        attendeeService.determinePriorityList(pConferenceInstance)

        pConferenceInstance.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pConferenceInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Conference.label', default: 'Conference'), pConferenceInstance.conferenceTitle])
                redirect pConferenceInstance
            }
            '*' { respond pConferenceInstance, [status: OK] }
        }
    }

    /**
     *
     * @param pType (String) -
     * @return String
     */
    private static String getAttendanceTypeLetter(final String pType) {
        switch (pType) {
            case 'Attendee': return 'A'
            case 'Booth/Display': return 'B'
            case 'Discussion Panel': return 'D'
            case 'Session Chair': return 'C'
            case 'Presenter/Speaker': return 'P'
            case 'Support': return 'S'
            case 'Other': return 'O'
        }

        return ''
    }

    /**
     *
     * @param pDisplay (String) -
     * @param pFill (int) -
     * @return String -
     */
    private static String prefill(final String pDisplay, final int pFill) {
        String result = pDisplay

        for (int i=pDisplay.length(); i<pFill; i++) {
            result += " "
        }

        return result
    }

    /**
     *
     * @return
     */
    def merge() {
        List<String> mergeList = new ArrayList<String>()
        for (String param in params) {
            if (param.contains("=on")) {
                mergeList.add(param)
            }
        }

        List<Conference> conferenceList = new ArrayList<>()
        List<String> confs = new ArrayList<>()
        for (merge in mergeList) {
            String conferenceId = merge.substring(merge.indexOf("_")+1, merge.indexOf("="))
            Conference conference = Conference.get(Integer.parseInt(conferenceId))
            conferenceList.add(conference)
            confs.add(conference.id)
        }

        boolean websiteDiff = false
        boolean venueDiff = false
        boolean primaryHostDiff = false
        boolean primarySponsorDiff = false
        boolean startDateDiff = false
        boolean endDateDiff = false
        boolean hostTypeDiff = false
        boolean addressDiff = false

        String website = ""
        String venue = ""
        String primaryHost = ""
        String primarySponsor = ""
        Date startDate = null
        Date endDate = null
        String hostType = ""
        String address = ""

        for (conference in conferenceList) {
            if (!website) {
                website = conference.website
            } else {
                if (conference.website && !website.equalsIgnoreCase(conference.website)) {
                    websiteDiff = true
                }
            }

            if (!address) {
                address = conference.address.toString()
            } else {
                if (conference.address && !address.equalsIgnoreCase(conference.address.toString())) {
                    addressDiff = true
                }
            }

            if (!venue) {
                venue = conference.venue
            } else {
                if (conference.venue && !venue.equalsIgnoreCase(conference.venue)) {
                    venueDiff = true
                }
            }

            if (!primaryHost) {
                primaryHost = conference.primaryHost
            } else {
                if (conference.primaryHost && !primaryHost.equalsIgnoreCase(conference.primaryHost)) {
                    primaryHostDiff = true
                }
            }

            if (!primarySponsor) {
                primarySponsor = conference.primarySponsor
            } else {
                if (conference.primarySponsor && !primarySponsor.equalsIgnoreCase(conference.primarySponsor)) {
                    primarySponsorDiff = true
                }
            }

            if (!startDate) {
                startDate = conference.startDate
            } else {
                if (startDate != conference.startDate) {
                    startDateDiff = true
                }
            }

            if (!endDate) {
                endDate = conference.endDate
            } else {
                if (endDate != conference.endDate) {
                    endDateDiff = true
                }
            }

            if (!hostType) {
                hostType = conference.hostType
            } else {
                if (conference.hostType && !hostType.equalsIgnoreCase(conference.hostType)) {
                    hostTypeDiff = true
                }
            }
        }

        respond conferenceList, model: [conferences: conferenceList.sort {it.id}, confs: confs, websiteDiff: websiteDiff, venueDiff: venueDiff, primaryHostDiff: primaryHostDiff,
                primarySponsorDiff: primarySponsorDiff, startDateDiff: startDateDiff, endDateDiff: endDateDiff, hostTypeDiff: hostTypeDiff, addressDiff: addressDiff]
    }

    /**
     *
     * @return
     */
    @Transactional
    def performMerge() {
        Conference newConference = new Conference()

        newConference.conferenceTitle = Conference.get(Long.parseLong(params.get("conferenceTitle"))).conferenceTitle + " [MERGED]"
        newConference.hostType = Conference.get(Long.parseLong(params.get("hostType"))).hostType
        newConference.website = Conference.get(Long.parseLong(params.get("website"))).website
        newConference.primaryHost = Conference.get(Long.parseLong(params.get("primaryHost"))).primaryHost
        newConference.startDate = Conference.get(Long.parseLong(params.get("startDate"))).startDate
        newConference.endDate = Conference.get(Long.parseLong(params.get("endDate"))).endDate
        newConference.venue = Conference.get(Long.parseLong(params.get("venue"))).venue
        newConference.purpose = Conference.get(Long.parseLong(params.get("purpose"))).purpose
        newConference.phaseState = Conference.get(Long.parseLong(params.get("phaseState"))).phaseState
        newConference.status = Conference.get(Long.parseLong(params.get("status"))).status

        def account = Account.get(springSecurityService.principal.id)
        newConference.statusChangeDate = new Date()
        newConference.statusChangedBy = account

        newConference.save flush: true

        Conference oldConference = Conference.get(Long.parseLong(params.get("address")))
        Address address = oldConference.address
        oldConference.address = null
        oldConference.save flush: true

        newConference.address = address
        newConference.save flush: true

        List<DateEvent> dates = new ArrayList<>()
        List<FileUpload> files = new ArrayList<>()
        List<ConferenceComment> comments = new ArrayList<>()

        String sList = params.confs
        sList = sList.replace("[","")
        sList = sList.replace("]","")
        sList = sList.replaceAll(" ", "")
        def list3 = sList.split(",")

        List<Attendee> attendeeList = new ArrayList<>()
        List<Attendee> deleteList = new ArrayList<>()

        for (String id in list3) {
            if (id.isLong()) {
                Conference conference = Conference.findById(Long.parseLong(id))
                if (conference) {
                    for (attendee in conference?.attendees) {
                        boolean found = false

                        for (newAttendee in attendeeList) {
                            if (attendee?.accountLink?.id == newAttendee?.accountLink?.id) {
                                found = true
                            }
                        }

                        if (found) {
                            deleteList.add(attendee)
                        } else {
                            attendee.conference = newConference
                            attendee.save flush: true

                            attendeeList.add(attendee)
                        }
                    }

                    for (attendee in deleteList) {
                        attendeeService.deleteAttendee(attendee)
                    }
                    deleteList.clear()

                    conference.attendees.clear()
                    conference.address = null

                    conference.save flush: true

                    // process date events
                    for (dateEvent in DateEvent.findAllByConference(conference)) {
                        if (dates.contains(dateEvent)) {
                            int idx = dates.indexOf(dateEvent)
                            DateEvent currentEvent = dates.get(idx)
                            if (currentEvent.eventDate > dateEvent.eventDate) {
                                dates.remove(currentEvent)
                                dateEvent.eventComment = "MERGED: " + conference.conferenceTitle
                                dates.add(dateEvent)

                                currentEvent.delete flush: true
                            }
                        } else {
                            dateEvent.eventComment = "MERGED: " + conference.conferenceTitle
                            dates.add(dateEvent)
                        }
                    }

                    // process files
                    for (file in FileUpload.findAllByConference(conference)) {
                        if (files.contains(file)) {
                            int idx = files.indexOf(file)
                            FileUpload currentFile = files.get(idx)
                            if (currentFile.fileDate > file.fileDate) {
                                files.remove(currentFile)
                                file.comments = "MERGED: " + conference.conferenceTitle
                                files.add(file)

                                currentFile.delete flush: true
                            }
                        } else {
                            file.comments = "MERGED: " + conference.conferenceTitle
                            dates.add(file)
                        }
                    }

                    // process comments
                    for (comment in conference?.comments) {
                        comment.conference = newConference
                    }

                    conference.comments.clear()
                    conference.save flush: true
                }
            }
        }

        for (dateEvent in dates) {
            dateEvent.conference = newConference
            dateEvent.save flush: true
        }
        new DateEvent(dateGate: RefDateGate.findByCode('DCM'), eventDate: new Date(), recordedBy: account, conference: newConference).save(flush: true)

        for (file in files) {
            file.conference = newConference
            file.save flush: true
        }

        newConference.save flush: true

        for (String id in list3) {
            if (id.isLong()) {
                Conference conference = Conference.findById(Long.parseLong(id))

                conference.attendees.clear()
                conference?.save flush: true

                for (dateEvent in DateEvent.findAllByConference(conference)) {
                    dateEvent.delete flush: true
                }
                for (file in FileUpload.findAllByConference(conference)) {
                    file.delete flush: true
                }

                conference?.delete(flush: true)
            }
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", newConference)

        // return the "user" to the 'show' conference page for the new merged conference
        request.withFormat {
            form {
                redirect(action: "show", id: newConference.id)
            }
            '*' { redirect(action: "show", id: newConference.id) }
        }
    }

    /**
     * This function is called via the conference 'SHOW' page via the 'toggle' action
     *
     * @param conferenceInstance (Conference) -
     * @return
     */
    @Transactional
    def toggleStep(Conference conferenceInstance) {
        if (conferenceInstance == null) {
            notFound()
            return
        }

        def list = conferenceInstance.constraints.step.inList
        def position = list.indexOf(conferenceInstance.step) + 1
        if (position >= list.size()) {
            position = 0
        }

        conferenceInstance.step = list.get(position)
        conferenceInstance.save flush: true

        request.withFormat {
            form {
                redirect controller: 'conference', action: 'show', id: conferenceInstance?.id
            }
            '*' { redirect controller: 'conference', action: 'show', id: conferenceInstance?.id }
        }
    }

// ***********************************************************************************************************************************************
// comment functionality
//
// add, show, delete
// ***********************************************************************************************************************************************

    def showComments(Conference conferenceInstance) {
        if (conferenceInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        [conferenceInstance: conferenceInstance, who: account]
    }

    def addComment(Conference conferenceInstance) {
        if (conferenceInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can perform task
        if (account) {
            String phase = conferenceInstance.phaseState
            if (!conferenceInstance.phaseState.equals(conferenceInstance.status)) {
                phase += " | " + conferenceInstance.status
            }

            new ConferenceComment(conference: conferenceInstance, who: account, when: new Date(), phase: phase, eComment: params.eComment).save(flush: true)
        }

        redirect(action: "showComments", id: params.id)
    }

    @Secured(['ROLE_ADMIN'])
    def deleteComment(Conference conferenceInstance) {
        if (conferenceInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can perform task
        if (account) {
            def conferenceCommentInstance = ConferenceComment.get(params.commentId)
            if (!conferenceCommentInstance) {
                notFound()
                return
            }

            conferenceCommentInstance.delete(flush: true)
        }

        // return the "user" to the 'show' conference page for the new merged conference
        request.withFormat {
            form {
                redirect(action: "showComments", id: conferenceInstance.id)
            }
            '*' { redirect(action: "showComments", id: conferenceInstance.id) }
        }
    }

}
