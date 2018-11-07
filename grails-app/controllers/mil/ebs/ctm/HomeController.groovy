package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured
import mil.ebs.ctm.ref.RefPhaseState
import org.compass.core.engine.SearchEngineQueryParseException
import org.springframework.util.ClassUtils

@Secured(['permitAll'])
class HomeController {

    def springSecurityService
    def searchableService


    def index() {
        if (isLoggedIn()) {
            params.max = 5
            params.sort = "startDate"
            params.order = "asc"
//            List<Conference> conferenceList = Conference.findAllByHideAndEndDateGreaterThan(false, new Date() - 1, params)
//            List<Conference> conferenceList = Conference.findAllByStartDateGreaterThanAndPhaseStateNotEqual(new Date() + 149, "Open", params)
            List<Conference> conferenceList = Conference.findAllByHideAndStartDateGreaterThan(false, new Date() + 149, params)

            def account = Account.get(springSecurityService.principal.id)
            List<Attendee> attendeeList = Attendee.findAllByAccountLink(account)

            List<Conference> attendingList = new ArrayList<Conference>(0)
            for (attendee in attendeeList) {
                if (!attendee?.conference?.status?.equalsIgnoreCase("Archived") && !attendee?.conference?.status?.equalsIgnoreCase("Cancelled") && !attendee?.conference?.status?.equalsIgnoreCase("Disapproved"))
//                if (attendee?.conference?.endDate >= new Date() + 30
//                        && (!attendee?.conference?.status?.equalsIgnoreCase("Archived") || !attendee?.conference?.status?.equalsIgnoreCase("Cancelled") || !attendee?.conference?.status?.equalsIgnoreCase("Disapproved")))
                {
                    attendingList.add(attendee?.conference)
                }
            }

            List<Attendee> supervisorList = Attendee.findAllByStatusAndSupervisor("Supervisor", account)
            if (supervisorList) {
                supervisorList = supervisorList.subList(0, Math.min(5, supervisorList.size()))
            }

            List<Attendee> tdConcurrenceList = new ArrayList<Attendee>(0)
            if (account?.hasAuthority("ROLE_TD_ADMIN") || account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                tdConcurrenceList = Attendee.findAllByStatusAndReservedTD("TD Concurrence", account?.assignedTD?.topParent)
                if (tdConcurrenceList) {
                    tdConcurrenceList = tdConcurrenceList.subList(0, Math.min(5, tdConcurrenceList.size()))
                }
            }

            RefPhaseState phaseState = RefPhaseState.findByPhaseAction("Attend")

            respond conferenceList?.sort { it?.startDate }, model: [attendState: phaseState, conferenceInstanceCount: conferenceList?.size(),
                    attendingList: attendingList?.sort { it?.startDate }, supervisorList: supervisorList?.sort { it?.conference?.startDate },
                    tdConcurrenceList: tdConcurrenceList?.sort { it?.conference?.startDate }, account: account,
                    termList: Conference?.termFreqs("conferenceTitle", size: 25)?.sort {it?.term}]
        }
    }

    def stats() {
        searchableService.reindex()
        Conference.reindex()
    }

    def about() {}

    def search = {

        print "SEARCH TERMS: " + params.q

        if (params.q.equals("*")) {
            redirect view: 'index'
            return
        }

        try {
            params.max = 200
            def searchResult = searchableService.search(params.q, params)

            List<Conference> initialList = new ArrayList<>()

            for (result in searchResult?.results) {
                if (ClassUtils.getShortName(result.getClass()).equalsIgnoreCase("Conference")) {
                    Conference conference = Conference.get(result.id)
                    if (!initialList?.contains(conference) && !conference?.isHide()) {
                        initialList.add(conference)
                    }
                }
                if (ClassUtils.getShortName(result.getClass()).equalsIgnoreCase("Address")) {
                    Address address = Address.get(result.id)
                    if (!initialList?.contains(address?.conference) && !address?.conference?.isHide()) {
                        initialList.add(address?.conference)
                    }
                }
            }

            params.max = Math.min(params.max ? params.int('max') : 25, 100)
            params.offset = params.offset ? params.int('offset') : 0
            List<Account> list = initialList.subList(params.int('offset'), Math.min(params.int('offset') + params.int('max'), initialList.size()))

            AttendanceChartData acd = new AttendanceChartData()
            acd.computeChartData(initialList)

            CostChartData ccd = new CostChartData()
            ccd.computeChartData(initialList)

            RefPhaseState phaseState = RefPhaseState.findByPhaseAction("Attend")
            def account = Account.get(springSecurityService.principal.id)

            respond list, view: 'results', model: [attendState: phaseState, account: account, conferenceInstanceCount: initialList?.size(), acd: acd, ccd: ccd, listType: 'Search']
        } catch (SearchEngineQueryParseException ignore) {
            redirect view: 'index'
            return [parseException: true]
        }
    }

    def searchAttendee = {
        try {
            params.max = 200
            def searchResult = searchableService.search(params.qA, params)

            println params.qA
            println searchResult

            def attendeeResult = Attendee.search(params.qA, params)

            println "------"
            println attendeeResult

            List<Attendee> initialList = new ArrayList<>()

            for (result in searchResult?.results) {
                if (ClassUtils.getShortName(result.getClass()).equalsIgnoreCase("Attendee")) {
                    initialList.add(Attendee.get(result.id))
                }
            }

            params.max = Math.min(params.max ? params.int('max') : 25, 100)
            params.offset = params.offset ? params.int('offset') : 0
            List<Account> list = initialList.subList(params.int('offset'), Math.min(params.int('offset') + params.int('max'), initialList.size()))

            respond list, view: 'resultsAttendee', model: [attendeeInstanceCount: initialList.size(), listType: 'Search']
        } catch (SearchEngineQueryParseException ignore) {
            redirect view: 'index'
            return [parseException: true]
        }
    }

}
