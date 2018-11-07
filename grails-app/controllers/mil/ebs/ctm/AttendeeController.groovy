package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured
import mil.ebs.ctm.ref.RefAttendeeState
import mil.ebs.ctm.ref.RefDateGate
import mil.ebs.ctm.ref.RefPhaseState
import mil.ebs.ctm.ref.RefRankGrade

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["permitAll"])
class AttendeeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def conferenceService
    def springSecurityService
    def exportService
    def notificationService
    def summaryService
    def attendeeService

//**************************************************************************
// Attendee LIST functions
//**************************************************************************

    /**
     *
     * @param pInitialList (List<Conference>) -
     * @return List<Conference>
     */
    private List<Attendee> parseList(final List<Attendee> pInitialList) {
        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.offset = params.offset ? params.int('offset') : 0

        int start = params.int('offset')
        int end = Math.min(params.int('offset') + params.int('max'), pInitialList.size())

        if (start > end) {
            start = 0
            params.offset = 0
        }

        return pInitialList.subList(start, end)
    }

    /**
     *
     * @param pInitialList (List<Conference>) -
     * @return List<Conference>
     */
    private List<Attendee> parseList(final List<Attendee> pInitialList, int pMin) {
        params.max = Math.min(params.max ? params.int('max') : pMin, 100)
        params.offset = params.offset ? params.int('offset') : 0

        int start = params.int('offset')
        int end = Math.min(params.int('offset') + params.int('max'), pInitialList.size())

        if (start > end) {
            start = 0
            params.offset = 0
        }

        return pInitialList.subList(start, end)
    }


    /**
     *
     * @param pInitialList (List<Attendee>) -
     * @param pStatus (String) -
     * @param pAccount (Account) -
     * @param pConference (Conference) -
     * @return int - count of records
     */
    private int processSearch(final List<Attendee> pInitialList, final String pStatus, final Account pAccount, final Conference pConference) {
        return processSearch(pInitialList, pStatus, pAccount, pConference, null)
    }

    /**
     *
     * @param pInitialList (List<Attendee>) -
     * @param pStatus (String) -
     * @param pAccount (Account) -
     * @param pConference (Conference) -
     * @param pReservedTD (Organization) -
     * @return int - count of records
     */
    private int processSearch(final List<Attendee> pInitialList, final String pStatus, final Account pAccount, final Conference pConference, final Organization pReservedTD) {
        int result

        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.offset = params.offset ? params.int('offset') : 0

        if (params.searchAttendee) {
            String searchTerm = "%" + params.searchAttendee + "%"
            for (account in Account.findAllByFirstNameIlikeOrLastNameIlikeOrUsernameIlike(searchTerm, searchTerm, searchTerm, [sort: params.sort, order: params.order])) {
                for (attendee in Attendee.findAllByAccountLink(account)) {
                    if (pStatus) {
                        if (attendee?.status?.equalsIgnoreCase(pStatus)) {
                            pInitialList.add(attendee)
                        }
                    } else {
                        pInitialList.add(attendee)
                    }
                }
            }
            result = pInitialList.size()

//            if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
//                // do nothing
//            } else {
//                List<Attendee> newList = parseList(pInitialList)
//                pInitialList.clear()
//                for (attendee in newList) {
//                    pInitialList.add(attendee)
//                }
//            }
        } else {
            if (pStatus) {
                if (pStatus.equalsIgnoreCase("TD Concurrence") && pAccount) {
                    // do not restrict excel output to a single page - get all data...
                    if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
                        pInitialList.addAll(Attendee.findAllByStatusAndReservedTD(pStatus, pAccount?.assignedTD?.topParent, [sort: params.sort, order: params.order]))
                    } else {
                        pInitialList.addAll(Attendee.findAllByStatusAndReservedTD(pStatus, pAccount?.assignedTD?.topParent, [offset: params.offset, max: params.max, sort: params.sort, order: params.order]))
                    }

                    result = Attendee.findAllByStatusAndReservedTD(pStatus, pAccount?.assignedTD?.topParent, [sort: params.sort, order: params.order]).size()
                } else if (pStatus.equalsIgnoreCase("Supervisor") && pAccount) {
                    // do not restrict excel output to a single page - get all data...
                    if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
                        pInitialList.addAll(Attendee.findAllByStatusAndSupervisor(pStatus, pAccount, [sort: params.sort, order: params.order]))
                    } else {
                        pInitialList.addAll(Attendee.findAllByStatusAndSupervisor(pStatus, pAccount, [offset: params.offset, max: params.max, sort: params.sort, order: params.order]))
                    }

                    result = Attendee.findAllByStatusAndSupervisor(pStatus, pAccount, [sort: params.sort, order: params.order]).size()
                } else if (pStatus.equalsIgnoreCase("Supervisor") && pReservedTD) {
                    // do not restrict excel output to a single page - get all data...
                    if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
                        pInitialList.addAll(Attendee.findAllByStatusAndReservedTD(pStatus, pReservedTD?.topParent, [sort: params.sort, order: params.order]))
                    } else {
                        pInitialList.addAll(Attendee.findAllByStatusAndReservedTD(pStatus, pReservedTD?.topParent, [offset: params.offset, max: params.max, sort: params.sort, order: params.order]))
                    }

                    result = Attendee.findAllByStatusAndReservedTD(pStatus, pReservedTD?.topParent, [sort: params.sort, order: params.order]).size()
                } else {
                    // do not restrict excel output to a single page - get all data...
                    if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
                        pInitialList.addAll(Attendee.findAllByStatus(pStatus, [sort: params.sort, order: params.order]))
                    } else {
                        pInitialList.addAll(Attendee.findAllByStatus(pStatus, [offset: params.offset, max: params.max, sort: params.sort, order: params.order]))
                    }

                    result = Attendee.findAllByStatus(pStatus, [sort: params.sort, order: params.order]).size()
                }
            } else if (pAccount) {
                // do not restrict excel output to a single page - get all data...
                if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
                    pInitialList.addAll(Attendee.findAllByAccountLink(pAccount, [sort: params.sort, order: params.order]))
                } else {
                    pInitialList.addAll(Attendee.findAllByAccountLink(pAccount, [offset: params.offset, max: params.max, sort: params.sort, order: params.order]))
                }

                result = Attendee.findAllByAccountLink(pAccount, [sort: params.sort, order: params.order]).size()
            } else if (pConference) {
                // do not restrict excel output to a single page - get all data...
                if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
                    pInitialList.addAll(Attendee.findAllByConference(pConference, [sort: params.sort, order: params.order]))
                } else {
                    pInitialList.addAll(Attendee.findAllByConference(pConference, [offset: params.offset, max: params.max, sort: params.sort, order: params.order]))
                }

                result = Attendee.findAllByConference(pConference, [sort: params.sort, order: params.order]).size()
            } else {
                // do not restrict excel output to a single page - get all data...
                if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
                    pInitialList.addAll(Attendee.list(sort: params.sort, order: params.order))
                } else {
                    pInitialList.addAll(Attendee.list(offset: params.offset, max: params.max, sort: params.sort, order: params.order))
                }

                result = Attendee.count()
            }
        }

        return result
    }

    /**
     * EXPORTABLE
     *
     * @param max (Integer) -
     * @return
     */
    def index(Integer max) {
        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "", null, null)

//        if (params.searchAttendee) {
//            String searchTerm = "%" + params.searchAttendee + "%"
//            def aList = Account.findAllByFirstNameIlikeOrLastNameIlikeOrUsernameIlike(searchTerm, searchTerm, searchTerm, [sort: params.sort, order: params.order])
//            for (account in aList) {
//                for (attendee in Attendee.findAllByAccountLink(account)) {
//                    initialList.add(attendee)
//                }
//            }
//            count = initialList.size()
//
//            if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
//                // do nothing
//            } else {
//                initialList = parseList(initialList)
//            }
//        } else {
//            // do not restrict excel output to a single page - get all data...
//            if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
//                initialList = Attendee.list(sort: params.sort, order: params.order)
//            } else {
//                initialList = Attendee.list(offset: params.offset, max: params.max, sort: params.sort, order: params.order)
//            }
//            count = Attendee.count()
//        }

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            List<AttendeeExport> exportList = new ArrayList<AttendeeExport>(0)
            for (attendeeInstance in initialList) {
                exportList.add(createAttendeeExport(attendeeInstance))
            }

            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]

            if (params.searchAttendee) {
                response.setHeader("Content-disposition", "attachment; filename=${params.searchAttendee}_attendees.${params.extension}")
            } else {
                response.setHeader("Content-disposition", "attachment; filename=all_attendees.${params.extension}")
            }

            exportService.export(params.formatType, response.outputStream, exportList, attendeeExportFields(), attendeeExportLabels(), [:], [:])
            return
        } else {
            if (params.searchAttendee) {
                initialList = parseList(initialList)
            }
        }

        respond initialList, model: [attendeeInstanceCount: count, listType: 'ALL', indexEvent: 'index', searchAttendee: params.searchAttendee, exportOption: true]
    }

    /**
     *
     * @param max
     * @return
     */
    def waitList(Integer max) {
//        List<Attendee> initialList = Attendee.findAllByStatus("Wait List", [sort: params.sort, order: params.order])
//        if (params.searchAttendee) {
//            initialList = initialList.findAll { it?.accountLink?.firstName?.toUpperCase()?.contains(params.searchAttendee.toUpperCase()) || it?.accountLink?.lastName?.contains(params.searchAttendee.toUpperCase()) }
//        }
//
//        respond parseList(initialList), view: 'index', model: [attendeeInstanceCount: initialList.size(), listType: 'Wait List', indexEvent: 'waitList', searchAttendee: params.searchAttendee]

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "Wait List", null, null)

        if (params.searchAttendee) {
            initialList = parseList(initialList)
        }

        respond initialList, view: 'index', model: [attendeeInstanceCount: count, listType: 'Wait List', indexEvent: 'waitList', searchAttendee: params.searchAttendee]
    }

    /**
     *
     * @param max
     * @return
     */
    def confirmedList(Integer max) {
//        List<Attendee> initialList = Attendee.findAllByStatus("Confirmed", [sort: params.sort, order: params.order])
//        if (params.searchAttendee) {
//            initialList = initialList.findAll { it?.accountLink?.firstName?.toUpperCase()?.contains(params.searchAttendee.toUpperCase()) || it?.accountLink?.lastName?.contains(params.searchAttendee.toUpperCase()) }
//        }

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "Confirmed", null, null)

        if (params.searchAttendee) {
            initialList = parseList(initialList)
        }

        respond parseList(initialList), view: 'index', model: [attendeeInstanceCount: count, listType: 'Confirmed', indexEvent: 'confirmedList', searchAttendee: params.searchAttendee]
    }

    /**
     *
     * @param max
     * @return
     */
    def registeredList(Integer max) {
//        List<Attendee> initialList = Attendee.findAllByStatus("Registered", [sort: params.sort, order: params.order])
//        if (params.searchAttendee) {
//            initialList = initialList.findAll { it?.accountLink?.firstName?.toUpperCase()?.contains(params.searchAttendee.toUpperCase()) || it?.accountLink?.lastName?.contains(params.searchAttendee.toUpperCase()) }
//        }

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "Registered", null, null)

        if (params.searchAttendee) {
            initialList = parseList(initialList)
        }

        respond parseList(initialList), view: 'index', model: [attendeeInstanceCount: count, listType: 'Registered', indexEvent: 'registeredList', searchAttendee: params.searchAttendee]
    }

    /**
     *
     * @param max
     * @return
     */
    def attendedList(Integer max) {
//        List<Attendee> initialList = Attendee.findAllByStatus("Attended", [sort: params.sort, order: params.order])
//        if (params.searchAttendee) {
//            initialList = initialList.findAll { it?.accountLink?.firstName?.toUpperCase()?.contains(params.searchAttendee.toUpperCase()) || it?.accountLink?.lastName?.contains(params.searchAttendee.toUpperCase()) }
//        }

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "Attended", null, null)

        if (params.searchAttendee) {
            initialList = parseList(initialList)
        }

        respond parseList(initialList), view: 'index', model: [attendeeInstanceCount: count, listType: 'Attended', indexEvent: 'attendedList', searchAttendee: params.searchAttendee]
    }

    /**
     *
     * @param max
     * @return
     */
    def supervisorList(Integer max) {
        def account = Account.get(springSecurityService?.principal?.id)

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "Supervisor", account, null)

        if (params.searchAttendee) {
            initialList = parseList(initialList, 100)
        }

        respond parseList(initialList, 100), view: 'index', model: [attendeeInstanceCount: count, listType: 'Supervisor', indexEvent: 'supervisorList', searchAttendee: params.searchAttendee]
    }

    /**
     *
     * @param max
     * @return
     */
    @Secured(["ROLE_FMC_ADMIN"])
    def supervisorListAll(Integer max) {
        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "Supervisor", null, null)

        if (params.searchAttendee) {
            initialList = parseList(initialList, 100)
        }

        respond parseList(initialList, 100), view: 'index', model: [attendeeInstanceCount: count, listType: 'Supervisor ALL', indexEvent: 'supervisorListAll', searchAttendee: params.searchAttendee]
    }

    /**
     *
     * @param max
     * @return
     */
    @Secured(["ROLE_FMC_ADMIN", "ROLE_TD_ADMIN", "ROLE_TD_FULL_ADMIN"])
    def supervisorListTD(Integer max) {
        def account = Account.get(springSecurityService?.principal?.id)

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "Supervisor", null, null, account?.assignedTD)

        if (params.searchAttendee) {
            initialList = parseList(initialList, 100)
        }

        respond parseList(initialList, 100), view: 'index', model: [attendeeInstanceCount: count, listType: 'Supervisor TD', indexEvent: 'supervisorListTD', searchAttendee: params.searchAttendee]
    }

    /**
     *
     * @param max
     * @return
     */
    def requestingList(Integer max) {
        def account = Account.get(springSecurityService?.principal?.id)

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "TD Concurrence", account, null)

        if (params.searchAttendee) {
            initialList = parseList(initialList, 100)
        }

        respond parseList(initialList, 100), view: 'index', model: [attendeeInstanceCount: count, listType: 'TD Concurrence', indexEvent: 'requestingList', searchAttendee: params.searchAttendee]
    }

    /**
     *
     * @param max
     * @return
     */
    @Secured(["ROLE_FMC_ADMIN"])
    def requestingListAll(Integer max) {
        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "TD Concurrence", null, null)

        if (params.searchAttendee) {
            initialList = parseList(initialList, 100)
        }

        respond parseList(initialList, 100), view: 'index', model: [attendeeInstanceCount: count, listType: 'TD Concurrence ALL', indexEvent: 'requestingListAll', searchAttendee: params.searchAttendee]
    }

    /**
     *
     * @param max
     * @return
     */
    def requestingListFlat(Integer max) {
        def account = Account.get(springSecurityService?.principal?.id)

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "TD Concurrence", account, null)

        if (params.searchAttendee) {
            initialList = parseList(initialList, 100)
        }

//        RefAttendeeState ras = RefAttendeeState.findByAttendeeStateAndPhaseStateAndAttendeeAction()

        respond parseList(initialList, 100), view: 'flat', model: [attendeeInstanceCount: count, listType: 'TD Concurrence', indexEvent: 'requestingList', searchAttendee: params.searchAttendee]
    }

// ***********************************************************************************************************************************************
// External INDEX called methods
// ***********************************************************************************************************************************************

    /**
     * EXPORTABLE
     *
     * @return
     */
    def attendeeList() {
        def td = Organization.get(params.id)
        def initialList = td.getAttendees() as List
        if (params.searchAttendee) {
            initialList = initialList.findAll { it?.accountLink?.firstName?.toUpperCase()?.contains(params.searchAttendee.toUpperCase()) || it?.accountLink?.lastName?.contains(params.searchAttendee.toUpperCase()) }
        }

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            List<AttendeeExport> exportList = new ArrayList<AttendeeExport>(0)
            for (attendeeInstance in initialList) {
                exportList.add(createAttendeeExport(attendeeInstance))
            }

            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]

            if (params.searchAttendee) {
                response.setHeader("Content-disposition", "attachment; filename=${td?.officeSymbol}_${params.searchAttendee}_attendees.${params.extension}")
            } else {
                response.setHeader("Content-disposition", "attachment; filename=${td?.officeSymbol}_all_attendees.${params.extension}")
            }

            exportService.export(params.formatType, response.outputStream, exportList, attendeeExportFields(), attendeeExportLabels(), [:], [:])
            return
        }

        respond parseList(initialList), view: 'index', model: [attendeeInstanceCount: initialList.size(), listType: 'Org', organizationId: params.id, indexEvent: 'attendeeList', searchAttendee: params.searchAttendee, exportOption: true]
    }

    /**
     *
     * @return
     */
    def accountAttendeeList() {
//        def account = Account.get(params.acctId)
//
//        def initialList = Attendee.findAllByAccountLink(account, [sort: params.sort, order: params.order]) as List
//        if (params.searchAttendee) {
//            initialList = initialList.findAll { it?.accountLink?.firstName?.toUpperCase()?.contains(params.searchAttendee.toUpperCase()) || it?.accountLink?.lastName?.contains(params.searchAttendee.toUpperCase()) }
//        }

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "", Account.get(params.acctId), null)

        if (params.searchAttendee) {
            initialList = parseList(initialList)
        }

        respond parseList(initialList), view: 'index', model: [attendeeInstanceCount: count, listType: 'Account', accountId: params.acctId, indexEvent: 'accountAttendeeList', searchAttendee: params.searchAttendee]
    }

    /**
     * EXPORTABLE
     *
     * @return
     */
    def conferenceAttendeeList() {
//        def conference = Conference.get(params.id)
//
//        def initialList = Attendee.findAllByConference(conference, [sort: params.sort, order: params.order]) as List
//        if (params.searchAttendee) {
//            initialList = initialList.findAll { it?.accountLink?.firstName?.toUpperCase()?.contains(params.searchAttendee.toUpperCase()) || it?.accountLink?.lastName?.contains(params.searchAttendee.toUpperCase()) }
//        }

        List<Attendee> initialList = new ArrayList<>()
        int count = processSearch(initialList, "", null, Conference.get(params.id))

        if (params?.formatType && params.formatType.equalsIgnoreCase("excel")) {
            List<AttendeeExport> exportList = new ArrayList<AttendeeExport>(0)
            for (attendeeInstance in initialList) {
                exportList.add(createAttendeeExport(attendeeInstance))
            }

            response.contentType = grailsApplication.config.grails.mime.types[params.formatType]

            if (params.searchAttendee) {
                response.setHeader("Content-disposition", "attachment; filename=${Conference.get(params.id).toString().replaceAll(" ", "_")}_${params.searchAttendee}_attendees.${params.extension}")
            } else {
                response.setHeader("Content-disposition", "attachment; filename=${Conference.get(params.id).toString().replaceAll(" ", "_")}_all_attendees.${params.extension}")
            }

            exportService.export(params.formatType, response.outputStream, exportList, attendeeExportFields(), attendeeExportLabels(), [:], [:])
            return
        } else {
            if (params.searchAttendee) {
                initialList = parseList(initialList)
            }
        }

        respond parseList(initialList), view: 'index', model: [attendeeInstanceCount: count, listType: 'Account', accountId: params.acctId, indexEvent: 'conferenceAttendeeList', searchAttendee: params.searchAttendee, exportOption: true]
    }

// ***********************************************************************************************************************************************
// Export methods
// ***********************************************************************************************************************************************

    /**
     *
     * @return List -
     */
    private static List attendeeExportFields() {
        return ["conference", "td", "organization", "status", "account", "name", "rankGrade", "td_org", "attendanceType", "startTravelDate", "endTravelDate",
                "supervisor", "justification", "est_registrationCost", "est_airfareCost", "est_localTravelCost", "est_lodgingCost", "est_lodgingCostTax", "est_mealsIncidentalCost",
                "est_otherCost", "est_notes", "est_totalAf", "est_totalOther", "est_totalNon", "act_registrationCost", "act_airfareCost", "act_localTravelCost", "act_lodgingCost",
                "act_lodgingCostTax", "act_mealsIncidentalCost", "act_otherCost", "act_notes", "act_totalAf", "act_totalOther", "act_totalNon"]
    }

    /**
     *
     * @return Map -
     */
    private static Map attendeeExportLabels() {
        return ["conference":"Conference", "td":"TD", "organization":"Ext Org", "status":"Status", "account":"Account", "name":"Name", "rankGrade":"Rank/Grade",
                "td_org":"Organization", "attendanceType":"Attendance Type", "startTravelDate":"Start Travel", "endTravelDate":"End Travel", "supervisor":"Supervisor", "justification":"Justification",
                "est_registrationCost":"Est Registration", "est_airfareCost":"Est Airfare", "est_localTravelCost":"Est Travel", "est_lodgingCost":"Est Lodging", "est_lodgingCostTax":"Est Lodging Tax",
                "est_mealsIncidentalCost":"Est Meals", "est_otherCost":"Est Cost", "est_notes":"Est Notes", "est_totalAf":"AF Est Total", "est_totalOther":"Other Est Total", "est_totalNon":"Non Est Total",
                "act_registrationCost":"Act Registration", "act_airfareCost":"Act Airfare", "act_localTravelCost":"Act Travel", "act_lodgingCost":"Act Lodging", "act_lodgingCostTax":"Act Lodging Tax",
                "act_mealsIncidentalCost":"Act Meals", "act_otherCost":"Act Cost", "act_notes":"Act Notes", "act_totalAf":"AF Act Total", "act_totalOther":"Other Act Total", "act_totalNon":"Non Act Total"]
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

            List<AttendeeExport> exportList = new ArrayList<AttendeeExport>(0)
            for (attendee in conferenceInstance.attendees.sort { it?.sequence }) {
                exportList.add(createAttendeeExport(attendee))
            }

            exportService.export(params.formatType, response.outputStream, exportList, attendeeExportFields(), attendeeExportLabels(), [:], [:])
        }
    }

    /**
     *
     * @param pAttendee (Attendee) -
     * @return AttendeeExport -
     */
    private static AttendeeExport createAttendeeExport(final Attendee pAttendee) {
        AttendeeExport export = new AttendeeExport(conference:pAttendee?.conference, td_org: pAttendee?.accountLink?.assignedTD?.officeSymbol, td:pAttendee?.reservedTD?.officeSymbol,
                organization:pAttendee?.reservedOrg, status:pAttendee?.status, sequence:pAttendee?.sequence,
                account:pAttendee?.accountLink, attendanceType:pAttendee?.attendanceType, startTravelDate:pAttendee?.startTravelDate, endTravelDate:pAttendee?.endTravelDate,
                supervisor:pAttendee?.supervisor, justification:pAttendee?.justification?.replaceAll("\\<.*?>",""), name:pAttendee?.name, rankGrade:pAttendee?.accountLink?.rankGrade
        )

        Cost estimate = Cost.findByAttendeeAndCostType(pAttendee, "Estimate")
        Cost actual = Cost.findByAttendeeAndCostType(pAttendee, "Actual")

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

        Map<String, Double> ectFunding = pAttendee.fundingEstimateTotal()
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

        Map<String, Double> actFunding = pAttendee.fundingActualTotal()
        if (actFunding.get("US Air Force")) {
            export.setAct_totalAf(actFunding.get("US Air Force"))
        }
        if (actFunding.get("Other US Govt")) {
            export.setAct_totalOther(actFunding.get("Other US Govt"))
        }
        if (actFunding.get("Non-Federal Entity")) {
            export.setAct_totalNon(actFunding.get("Non-Federal Entity"))
        }

        return export
    }

// ***********************************************************************************************************************************************
// standard action methods
//
// ***********************************************************************************************************************************************

    /**
     * Default "NOT FOUND" redirect
     */
    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'attendeeInstance.label', default: 'Attendee'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    /**
     *
     * @param attendeeInstance (Attendee)
     * @return
     */
    def show(Attendee attendeeInstance) {
        respond attendeeInstance
    }

    /**
     * This function "should" not normally be called - since an attendee is created via the conference controller.
     *
     * @return
     */
    def create() {
        respond new Attendee(params)
    }

    /**
     * This is CALLED from the SHOW Conference page.
     *
     * @return
     */
    def conferenceCreate() {
        Attendee attendee = new Attendee(params)

        Conference conferenceInstance = Conference.get(params.get('conferenceId'))
        attendee.conference = conferenceInstance

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can perform a create
        if (account) {
            attendee.accountLink = account
            attendee.supervisor = account?.supervisor
            attendee.rankGrade = account?.rankGrade
            attendee.reservedTD = account?.assignedTD?.topParent
            attendee.createdBy = account
            attendee.notifyChanges = account?.notifyChanges
            attendee.notifyConferenceChanges = account?.notifyConferenceChanges
        }

        attendee.createdDate = new Date()
        attendee.startTravelDate = attendee?.conference?.startDate
        attendee.endTravelDate = attendee?.conference?.endDate
        attendee.attendanceType = "Attendee"
        attendee.hoursAttendanceType = attendee?.conference?.days() * 8
        attendee.sequence = attendee?.conference?.numAttendees ? attendee?.conference?.numAttendees + 1 : 0

        attendee.status = "Pending"
        if (attendee?.conference?.numAttendees) {
            if (attendee.sequence > attendee?.conference?.numAttendees) {
                attendee.status = "Wait List"
            }
        }

        respond attendee
    }

    /**
     *
     * @param attendeeInstance (Attendee)
     * @return
     */
    @Transactional
    def save(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'create'
            return
        }

        if (!params.rankGrade?.id?.equalsIgnoreCase("null")) {
            if (attendeeInstance?.accountLink) {
                if (params.rankGrade?.id) {
                    attendeeInstance?.accountLink?.rankGrade = RefRankGrade.get(params.rankGrade?.id)
                }
                attendeeInstance?.accountLink?.save(flush: true)
            }
        }

        attendeeInstance.save flush: true

//        saveFunding(attendeeInstance, params.fundSource1a, params.fundSource2a, params.fundSource3a)

    // ----------------------------------------
    // update conference information
    // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        conferenceService.updateConferenceDataViaPhaseState(attendeeInstance?.conference, phaseState)

        def account = Account.get(springSecurityService.principal.id)
        new DateEvent(dateGate: RefDateGate.findByCode("DAC"), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'attendeeInstance.label', default: 'Attendee'), attendeeInstance.id])
                respond attendeeInstance
            }
            '*' { respond attendeeInstance, [status: CREATED] }
        }
    }

    /**
     *
     * @param attendeeInstance (Attendee)
     * @return
     */
    @Transactional
    def conferenceSave(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'create'
            return
        }

        if (!params.rankGrade?.id?.equalsIgnoreCase("null")) {
            if (attendeeInstance?.accountLink) {
                if (params.rankGrade?.id) {
                    attendeeInstance?.accountLink?.rankGrade = RefRankGrade.get(params.rankGrade?.id)
                }
                attendeeInstance?.accountLink?.save(flush: true)
            }
        }

        attendeeInstance.save flush: true

//        saveFunding(attendeeInstance, params.fundSource1a, params.fundSource2a, params.fundSource3a)

    // ----------------------------------------
    // update conference information
    // ----------------------------------------
        RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))

        // check the available action to "revalidate" if it is possible
        // NOTE: this is to prevent a user from modifying the URL and attempting this action regardless of the page options
        conferenceService.updateConferenceDataViaPhaseState(attendeeInstance?.conference, phaseState)

        def account = Account.get(springSecurityService.principal.id)
        new DateEvent(dateGate: RefDateGate.findByCode("DAC"), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)

        request.withFormat {
            form {
                redirect controller: 'conference', action: 'show', id: attendeeInstance?.conference?.id
            }
            '*' { redirect controller: 'conference', action: 'show', id: attendeeInstance?.conference?.id }
        }
    }

    @Transactional
    def duplicateAttendee(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'create'
            return
        }

        def conferenceId = attendeeInstance?.conference?.id

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        Attendee newAttendee = new Attendee()

        newAttendee?.accountLink = null
        newAttendee?.supervisor = null
        newAttendee?.rankGrade = null
        newAttendee?.reservedTD = attendeeInstance?.reservedTD
        newAttendee?.startTravelDate = attendeeInstance?.startTravelDate
        newAttendee?.endTravelDate = attendeeInstance?.endTravelDate
        newAttendee?.attendanceType = attendeeInstance?.attendanceType
        newAttendee?.hoursAttendanceType = attendeeInstance?.hoursAttendanceType
        newAttendee?.mealsIncluded = attendeeInstance?.mealsIncluded
        newAttendee?.createdDate = new Date()

        // should be logged in person
        newAttendee?.createdBy = account

        newAttendee?.notifyChanges = true
        newAttendee?.notifyConferenceChanges = true

        newAttendee?.justification = attendeeInstance?.justification

        newAttendee?.status = "Pending"
        if (attendeeInstance?.conference?.numAttendees) {
            def available = attendeeInstance?.conference?.numAttendees - attendeeInstance?.conference?.attendees?.size()
            if (available <= 0) {
                newAttendee?.status = "Wait List"
            }
        }

        newAttendee?.sequence = attendeeInstance?.conference?.attendees?.size() + 1
        newAttendee?.conference = attendeeInstance?.conference

        newAttendee.save flush: true

        attendeeInstance?.conference?.attendees?.add(newAttendee)
        attendeeInstance?.conference?.save flush: true

        conferenceService.determineResponsibleTD(attendeeInstance?.conference)

        // save costs associated with original attendee
        for (cost in attendeeInstance?.costs) {
            Cost newCost = new Cost()
            newCost?.costType = cost?.costType
            newCost?.zeroRegistrationReason = cost?.zeroRegistrationReason
            newCost?.registrationCost = cost?.registrationCost
            newCost?.airfareCost = cost?.airfareCost
            newCost?.zeroAirfareReason = cost?.zeroAirfareReason
            newCost?.airfareProvider = cost?.airfareProvider
            newCost?.localTravelCost = cost?.localTravelCost
            newCost?.localTravelProvider = cost?.localTravelProvider
            newCost?.lodgingCost = cost?.lodgingCost
            newCost?.lodgingCostTax = cost?.lodgingCostTax
            newCost?.zeroLodgingReason = cost?.zeroLodgingReason
            newCost?.lodgingProvider = cost?.lodgingProvider
            newCost?.lodgingExceedsPerdiem = cost?.lodgingExceedsPerdiem
            newCost?.mealsIncidentalCost = cost?.mealsIncidentalCost
            newCost?.zeroMealsReason = cost?.zeroMealsReason
            newCost?.mealsExceedsPerdiem = cost?.mealsExceedsPerdiem
            newCost?.otherCost = cost?.otherCost
            newCost?.otherDescription = cost?.otherDescription
            newCost?.attendee = newAttendee

            newCost.save flush: true

            String fundSource1 = FundingSource.findByCostAndFundSource(cost, 'Other US Govt')?.percentage
            String fundSource2 = FundingSource.findByCostAndFundSource(cost, 'US Air Force')?.percentage
            String fundSource3 = FundingSource.findByCostAndFundSource(cost, 'Non-Federal Entity')?.percentage
            saveFunding(newCost, fundSource1, fundSource2, fundSource3)
        }

        new DateEvent(dateGate: RefDateGate.findByCode("DAC"), eventDate: new Date(), recordedBy: account, attendee: newAttendee).save(flush: true)

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"conference", id: conferenceId, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: conferenceId, action:"show", method:"GET" }
        }
    }

    /**
     *
     * @param attendeeInstance (Attendee)
     * @return
     */
    def edit(Attendee attendeeInstance) {

        Date start = attendeeInstance?.conference?.startDate
        if (!attendeeInstance?.startTravelDate) {
            attendeeInstance?.startTravelDate = start
        }

        Date end = attendeeInstance?.conference?.endDate
        if (!attendeeInstance?.endTravelDate) {
            attendeeInstance?.endTravelDate = end
        }

//        String fundSource1 = FundSource.findByAttendeeAndFundSource(attendeeInstance, 'Other US Govt')?.percentage
//        String fundSource2 = FundSource.findByAttendeeAndFundSource(attendeeInstance, 'US Air Force')?.percentage
//        String fundSource3 = FundSource.findByAttendeeAndFundSource(attendeeInstance, 'Non-Federal Entity')?.percentage
//
//        if (!fundSource1 && !fundSource2 && !fundSource3) {
//            fundSource1 = "0"
//            fundSource2 = "100"
//            fundSource3 = "0"
//        }

        Set<Account> accountList = attendeeInstance?.reservedTD?.getAssignedAllowed()
//        respond attendeeInstance, model: [fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3, accountList: accountList?.sort {it.displayName}]
        respond attendeeInstance, model: [accountList: accountList?.sort {it.displayName}]
    }

    /**
     *
     * @param attendeeInstance (Attendee)
     * @return
     */
    @Transactional
    def update(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'edit'
            return
        }

        if (!params.rankGrade?.id?.equalsIgnoreCase("null")) {
            if (attendeeInstance?.accountLink) {
                if (params.rankGrade?.id) {
                    attendeeInstance?.accountLink?.rankGrade = RefRankGrade.get(params.rankGrade?.id)
                }
                attendeeInstance?.accountLink?.save(flush: true)
            }
        }

        if (attendeeInstance?.accountLink) {
            attendeeInstance?.supervisor = attendeeInstance?.accountLink?.supervisor
            attendeeInstance?.rankGrade = attendeeInstance?.accountLink?.rankGrade
        } else {
            attendeeInstance?.supervisor = null
            attendeeInstance?.rankGrade = null
        }

        attendeeInstance.save flush: true

//        saveFunding(attendeeInstance, params.fundSource1a, params.fundSource2a, params.fundSource3a)

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Attendee.label', default: 'Attendee'), attendeeInstance.id])
                redirect attendeeInstance
            }
            '*' { respond attendeeInstance, [status: OK] }
        }
    }


//***********************************************************************************************************************************************
// OLD functions
//***********************************************************************************************************************************************

    @Transactional
    def approve(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'edit'
            return
        }

        if (attendeeInstance?.status?.equalsIgnoreCase("Pending")
                || attendeeInstance?.status?.equalsIgnoreCase("Wait List")
                || attendeeInstance?.status?.equalsIgnoreCase("Disapproved"))
        {
            attendeeInstance.status = "Approved"
            attendeeInstance.approvalRequestDate = new Date()
            attendeeInstance.approvalRequestBy = Account.get(springSecurityService.principal.id)
            attendeeInstance.save flush: true
            //todo: send out notification email for status change
        }

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    @Transactional
    def reject(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'edit'
            return
        }

        if (attendeeInstance?.status?.equalsIgnoreCase("Pending")
                || attendeeInstance?.status?.equalsIgnoreCase("Wait List")
                || attendeeInstance?.status?.equalsIgnoreCase("Approved")
                || attendeeInstance?.status?.equalsIgnoreCase("Confirmed")
                || attendeeInstance?.status?.equalsIgnoreCase("Registered"))
        {
            attendeeInstance.status = "Disapproved"
            attendeeInstance.rejectedDate = new Date()
            attendeeInstance.rejectedBy = Account.get(springSecurityService.principal.id)
            attendeeInstance.save flush: true
            //todo: send out notification email for status change
        }

        Conference conference = attendeeInstance.conference
        conferenceService.determineResponsibleTD(conference)
        conference.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    @Transactional
    def register(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'edit'
            return
        }

        if (attendeeInstance?.status?.equalsIgnoreCase("Approved")) {
            attendeeInstance.status = "Confirmed"
            attendeeInstance.registeredDate = new Date()
            attendeeInstance.registeredBy = Account.get(springSecurityService.principal.id)
            attendeeInstance.save flush: true
            //todo: send out notification email for status change
        }

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    @Transactional
    def withdrawn(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'edit'
            return
        }

        if (attendeeInstance?.status?.equalsIgnoreCase("Approved") || attendeeInstance?.status?.equalsIgnoreCase("Pending") || attendeeInstance?.status?.equalsIgnoreCase("Wait List")) {
            attendeeInstance.status = "Withdrawn"
            attendeeInstance.withdrawnDate = new Date()
            attendeeInstance.withdrawnBy = Account.get(springSecurityService.principal.id)
            attendeeInstance.save flush: true
            //todo: send out notification email for status change
        }

        Conference conference = attendeeInstance.conference
        conferenceService.determineResponsibleTD(conference)
        conference.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    @Transactional
    def attended(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'edit'
            return
        }

        if (attendeeInstance?.status?.equalsIgnoreCase("Confirmed")) {
            attendeeInstance.status = "Attended"
            attendeeInstance.save flush: true
            //todo: send out notification email for status change
        }

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    @Transactional
    def delete(Attendee attendeeInstance) {

        if (attendeeInstance == null) {
            notFound()
            return
        }

        attendeeInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Attendee.label', default: 'Attendee'), attendeeInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    @Transactional
    def deleteAttendee(Attendee attendeeInstance) {

        if (attendeeInstance == null) {
            notFound()
            return
        }

        def conferenceId = attendeeInstance?.conference?.id

        for (cost in attendeeInstance?.costs) {
            Address address = Address.get(cost?.lodgingAddress?.id)

            cost.lodgingAddress = null
            cost.save flush:true

            if (address) {
                address.delete flush:true
            }

            attendeeInstance?.costs?.remove(cost)

            cost.delete flush: true
        }

        def dateEvents = DateEvent.findAllByAttendee(attendeeInstance)
        for (event in dateEvents) {
            event.delete flush: true
        }
        
        Conference conference = attendeeInstance.conference
        int startSequence = attendeeInstance?.sequence

        //todo: send out notification email for deletion from conference

        attendeeInstance.delete flush: true

        for (attendee in conference.attendees) {
            if (attendee.sequence > startSequence) {
                attendee.sequence = attendee.sequence - 1
            }

            attendee.save flush: true
        }

        conferenceService.determineResponsibleTD(conference)
        conference.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"conference", id: conferenceId, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: conferenceId, action:"show", method:"GET" }
        }
    }

//----------------------------------------
// Conference 'SHOW' page calls
//----------------------------------------

    def ajaxGetAccounts() {
        def accountList = Organization.get(params.id)?.getAssignedAllowed()

        accountList = accountList.sort() { it.username }
        accountList = accountList.collect() {
            new NameIdGSP(id: it.id, name: it.displayName)
        }

        render template: 'accountLinkBlock', model: [accountList: accountList]
    }

    def ajaxGetInternalAccount() {
        def account = Account.get(params.id)

        render template: 'internalBlock', model: [rankGrade: account?.rankGrade, supervisor: account?.supervisor]
    }

    /**
     * This function is called from the conference 'SHOW' page
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    def conferenceEdit(Attendee attendeeInstance) {

        Date start = attendeeInstance?.conference?.startDate
        if (!attendeeInstance?.startTravelDate) {
            attendeeInstance?.startTravelDate = start
        }

        Date end = attendeeInstance?.conference?.endDate
        if (!attendeeInstance?.endTravelDate) {
            attendeeInstance?.endTravelDate = end
        }

        if (attendeeInstance?.accountLink) {
            attendeeInstance?.rankGrade = attendeeInstance?.accountLink?.rankGrade
        }

//        String fundSource1 = FundSource.findByAttendeeAndFundSource(attendeeInstance, 'Other US Govt')?.percentage
//        String fundSource2 = FundSource.findByAttendeeAndFundSource(attendeeInstance, 'US Air Force')?.percentage
//        String fundSource3 = FundSource.findByAttendeeAndFundSource(attendeeInstance, 'Non-Federal Entity')?.percentage
//
//        if (!fundSource1 && !fundSource2 && !fundSource3) {
//            fundSource1 = "0"
//            fundSource2 = "100"
//            fundSource3 = "0"
//        }

        Set<Account> accountList = attendeeInstance?.reservedTD?.getAssignedAllowed()
//        respond attendeeInstance, model: [fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3, accountList: accountList?.sort {it.displayName}]
        respond attendeeInstance, model: [accountList: accountList?.sort {it.displayName}]
    }

    /**
     * This function is called from the conference 'SHOW' page
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    def conferenceEditWizard(Attendee attendeeInstance) {

        Date start = attendeeInstance?.conference?.startDate
        if (!attendeeInstance?.startTravelDate) {
            attendeeInstance?.startTravelDate = start
        }

        Date end = attendeeInstance?.conference?.endDate
        if (!attendeeInstance?.endTravelDate) {
            attendeeInstance?.endTravelDate = end
        }

//        String fundSource1 = FundSource.findByAttendeeAndFundSource(attendeeInstance, 'Other US Govt')?.percentage
//        String fundSource2 = FundSource.findByAttendeeAndFundSource(attendeeInstance, 'US Air Force')?.percentage
//        String fundSource3 = FundSource.findByAttendeeAndFundSource(attendeeInstance, 'Non-Federal Entity')?.percentage
//
//        if (!fundSource1 && !fundSource2 && !fundSource3) {
//            fundSource1 = "0"
//            fundSource2 = "100"
//            fundSource3 = "0"
//        }

        Set<Account> accountList = attendeeInstance?.reservedTD?.getAssignedAllowed()
//        respond attendeeInstance, model: [fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3, accountList: accountList?.sort {it.displayName}]
        respond attendeeInstance, model: [accountList: accountList?.sort {it.displayName}]
    }

    /**
     * This function is called via the conference 'SHOW' page via the 'editAttendee' action
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def conferenceUpdate(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'conferenceEdit'
            return
        }

        if (!params.rankGrade?.id?.equalsIgnoreCase("null")) {
            if (attendeeInstance?.accountLink) {
                if (params.rankGrade?.id) {
                    attendeeInstance?.accountLink?.rankGrade = RefRankGrade.get(params.rankGrade?.id)
                }
                attendeeInstance?.accountLink?.save(flush: true)
            }
        }

        if (attendeeInstance?.accountLink) {
            attendeeInstance?.supervisor = attendeeInstance?.accountLink?.supervisor
            attendeeInstance?.rankGrade = attendeeInstance?.accountLink?.rankGrade
            attendeeInstance?.reservedTD = attendeeInstance?.accountLink?.assignedTD?.topParent
        } else {
            attendeeInstance?.supervisor = null
        }

        // did the accountLink change?
        if (attendeeInstance.isDirty('accountLink')) {
            def account = Account.get(springSecurityService?.principal?.id)

            // check TD attendance rules
            if (attendeeInstance?.accountLink?.assignedTD?.topParent?.attendeeRequestRequired) {
                if (account.hasAuthority("ROLE_TD_ADMIN") || account.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                    // do nothing - TD Admin creating slot so should be approved already
                } else {
                    attendeeInstance.status = "TD Concurrence"
                    notificationService.request('requestTdConcurrence', attendeeInstance?.conference, Account.get(springSecurityService.principal.id), attendeeInstance?.accountLink)
                }
            }
            if (attendeeInstance?.accountLink?.assignedTD?.topParent?.supervisorApprovalRequired) {
                if (attendeeInstance?.accountLink?.supervisor) {
                    attendeeInstance.status = "Supervisor"
                    notificationService.request('requestSupervisor', attendeeInstance?.conference, Account.get(springSecurityService.principal.id), attendeeInstance?.accountLink)
                } else {
                    if (account.hasAuthority("ROLE_TD_ADMIN") || account.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                        // do nothing - TD Admin creating slot so should be approved already
                    } else {
                        attendeeInstance.status = "TD Concurrence"
                        notificationService.request('requestTdConcurrence', attendeeInstance?.conference, Account.get(springSecurityService.principal.id), attendeeInstance?.accountLink)
                    }
                }
            }
        }

        attendeeInstance.save flush: true

        conferenceService.determineResponsibleTD(attendeeInstance?.conference)
        attendeeInstance?.conference?.save flush: true

        attendeeService.determinePriorityList(attendeeInstance?.conference)

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller: 'conference', action: 'show', id: attendeeInstance?.conference?.id
            }
            '*' { redirect controller: 'conference', action: 'show', id: attendeeInstance?.conference?.id }
        }
    }

    /**
     * This function is called via the conference 'SHOW' page via the 'editAttendee' action
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def conferenceUpdateWizard(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        if (attendeeInstance.hasErrors()) {
            respond attendeeInstance.errors, view: 'conferenceEditWizard'
            return
        }

        if (attendeeInstance?.accountLink) {
            attendeeInstance?.supervisor = attendeeInstance?.accountLink?.supervisor
            attendeeInstance?.rankGrade = attendeeInstance?.accountLink?.rankGrade
            attendeeInstance?.reservedTD = attendeeInstance?.accountLink?.assignedTD?.topParent
        } else {
            attendeeInstance?.supervisor = null
            attendeeInstance?.rankGrade = null
        }

        // did the accountLink change?
        if (attendeeInstance.isDirty('accountLink')) {
            def account = Account.get(springSecurityService?.principal?.id)

            // check TD attendance rules
            if (attendeeInstance?.accountLink?.assignedTD?.topParent?.attendeeRequestRequired) {
                if (account.hasAuthority("ROLE_TD_ADMIN") || account.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                    // do nothing - TD Admin creating slot so should be approved already
                } else {
                    attendeeInstance.status = "TD Concurrence"
                    notificationService.request('requestTdConcurrence', attendeeInstance?.conference, Account.get(springSecurityService.principal.id), attendeeInstance?.accountLink)
                }
            }
            if (attendeeInstance?.accountLink?.assignedTD?.topParent?.supervisorApprovalRequired) {
                if (attendeeInstance?.accountLink?.supervisor) {
                    attendeeInstance.status = "Supervisor"
                    notificationService.request('requestSupervisor', attendeeInstance?.conference, Account.get(springSecurityService.principal.id), attendeeInstance?.accountLink)
                } else {
                    if (account.hasAuthority("ROLE_TD_ADMIN") || account.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                        // do nothing - TD Admin creating slot so should be approved already
                    } else {
                        attendeeInstance.status = "TD Concurrence"
                        notificationService.request('requestTdConcurrence', attendeeInstance?.conference, Account.get(springSecurityService.principal.id), attendeeInstance?.accountLink)
                    }
                }
            }

        }

        attendeeInstance.save flush: true

//        saveFunding(attendeeInstance, params.fundSource1a, params.fundSource2a, params.fundSource3a)

        conferenceService.determineResponsibleTD(attendeeInstance?.conference)
        attendeeInstance?.conference?.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

//        request.withFormat {
//            form {
//                redirect controller: 'conference', action: 'show', id: attendeeInstance?.conference?.id
//            }
//            '*' { redirect controller: 'conference', action: 'show', id: attendeeInstance?.conference?.id }
//        }

        String attendeeId = attendeeInstance.id
        request.withFormat {
            form {
                redirect controller:"cost", params: ['attendee.id': attendeeId, costType: "Estimate"], action:"createEstimateCost", method:"GET"
            }
            '*' {
                redirect controller:"cost", params: ['attendee.id': attendeeId, costType: "Estimate"], action:"createEstimateCost", method:"GET"
            }
        }

    }

    /**
     * This function is called via the conference 'SHOW' page via the 'toggle' action
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def toggle(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            def list = attendeeInstance.constraints.attendanceType.inList
            def position = list.indexOf(attendeeInstance.attendanceType) + 1
            if (position >= list.size()) {
                position = 0
            }

            attendeeInstance.attendanceType = list.get(position)
            attendeeInstance.save flush: true

            // update the "current" summary associated to the conference
            summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)
        }

        request.withFormat {
            form {
                redirect controller: 'conference', action: 'show', id: attendeeInstance?.conference?.id
            }
            '*' { redirect controller: 'conference', action: 'show', id: attendeeInstance?.conference?.id }
        }
    }

// ***********************************************************************************************************************************************
// action methods invoked by phase/status processing
//
// actions controlled:
//      supervisorApproveAttendee, supervisorDisapproveAttendee, tdApproveAttendee, tdDisapproveAttendee,
//      withdrawAttendee, overrideAttendee, fmcApproveAttendee, fmcDisapproveAttendee,
//      registerAttendee, attendedAttendee
// ***********************************************************************************************************************************************

    /**
     * This function is called via the attendee 'SHOW' page. Sets the selected attendee slot to "pending/wait list/requesting".
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def supervisorApproveAttendee(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            attendeeInstance.status = "Pending"
            if (attendeeInstance?.conference?.numAttendees) {
                def available = attendeeInstance?.conference?.numAttendees - attendeeInstance?.conference?.attendees?.size()
                if (available <= 0) {
                    attendeeInstance.status = "Wait List"
                }
            }

            if (attendeeInstance?.reservedTD?.attendeeRequestRequired) {
                attendeeInstance.status = "TD Concurrence"
            }

            if (attendeeInstance?.accountLink?.assignedTD?.attendeeRequestRequired) {
                attendeeInstance.status = "TD Concurrence"
            }

            attendeeInstance.supervisorApprovalBy = account
            attendeeInstance.supervisorApprovalDate = new Date()
            attendeeInstance.lastChange = account
            attendeeInstance.lastChangeDate = new Date()

            attendeeInstance.save flush: true

            RefAttendeeState attendeeState = RefAttendeeState.get(params.get("refId"))
            if (attendeeState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(attendeeState?.dateGateEvent), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)
            }
        }

        conferenceService.determineResponsibleTD(attendeeInstance?.conference)
        attendeeInstance?.conference?.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    /**
     * This function is called via the attendee 'SHOW' page. Sets the selected attendee slot to "pending/wait list".
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def tdApproveAttendee(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            RefAttendeeState attendeeState = RefAttendeeState.get(params.get("refId"))
            attendeeInstance.status = attendeeState?.nextState
            if (attendeeInstance?.status?.equalsIgnoreCase("Pending")) {
                if (attendeeInstance?.conference?.numAttendees) {
                    def available = attendeeInstance?.conference?.numAttendees - attendeeInstance?.conference?.attendees?.size()
                    if (available <= 0) {
                        attendeeInstance.status = "Wait List"
                    }
                }
            }

            attendeeInstance.tdApprovalBy = account
            attendeeInstance.tdApprovalDate = new Date()
            attendeeInstance.lastChange = account
            attendeeInstance.lastChangeDate = new Date()

            attendeeInstance.save flush: true

            if (attendeeState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(attendeeState?.dateGateEvent), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)
            }
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    /**
     * This function is called via the attendee 'SHOW' page. Sets the selected attendee slot to "pending/wait list".
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def fmcApproveAttendee(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            RefAttendeeState attendeeState = RefAttendeeState.get(params.get("refId"))
            attendeeInstance.status = attendeeState?.nextState
            attendeeInstance.tdApprovalBy = account
            attendeeInstance.tdApprovalDate = new Date()
            attendeeInstance.lastChange = account
            attendeeInstance.lastChangeDate = new Date()

            attendeeInstance.save flush: true

            if (attendeeState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(attendeeState?.dateGateEvent), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)
            }
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    /**
     * This function is called via the attendee 'SHOW' page. Sets the selected attendee slot to "disapproved".
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def disapproveAttendee(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            RefAttendeeState attendeeState = RefAttendeeState.get(params.get("refId"))
            attendeeInstance.status = attendeeState?.nextState

            attendeeInstance.lastChange = account
            attendeeInstance.lastChangeDate = new Date()

            attendeeInstance.save flush: true

            if (attendeeState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(attendeeState?.dateGateEvent), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)
            }
        }

        Conference conference = attendeeInstance.conference
        conferenceService.determineResponsibleTD(conference)
        conference.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    /**
     * This function is called via the attendee 'SHOW' page. Sets the selected attendee slot to "withdrawn".
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def withdrawAttendee(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            RefAttendeeState attendeeState = RefAttendeeState.get(params.get("refId"))
            attendeeInstance.status = attendeeState?.nextState

            attendeeInstance.lastChange = account
            attendeeInstance.lastChangeDate = new Date()

            attendeeInstance.save flush: true

            if (attendeeState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(attendeeState?.dateGateEvent), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)
            }
        }

        Conference conference = attendeeInstance.conference
        conferenceService.determineResponsibleTD(conference)
        conference.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    /**
     * This function is called via the attendee 'SHOW' page. Sets the selected attendee slot to "?".
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def overrideAttendee(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            RefAttendeeState attendeeState = RefAttendeeState.get(params.get("refId"))
            attendeeInstance.status = attendeeState?.nextState

            attendeeInstance.lastChange = account
            attendeeInstance.lastChangeDate = new Date()

            attendeeInstance.save flush: true

            if (attendeeState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(attendeeState?.dateGateEvent), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)
            }
        }

        Conference conference = attendeeInstance.conference
        conferenceService.determineResponsibleTD(conference)
        conference.save flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    /**
     * This function is called via the attendee 'SHOW' page. Sets the selected attendee slot to "register".
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def registerAttendee(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            RefAttendeeState attendeeState = RefAttendeeState.get(params.get("refId"))
            attendeeInstance.status = attendeeState?.nextState

            attendeeInstance.lastChange = account
            attendeeInstance.lastChangeDate = new Date()

            attendeeInstance.save flush: true

            if (attendeeState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(attendeeState?.dateGateEvent), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)
            }
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    /**
     * This function is called via the attendee 'SHOW' page. Sets the selected attendee slot to "attended".
     *
     * @param attendeeInstance (Attendee) -
     * @return
     */
    @Transactional
    def attendedAttendee(Attendee attendeeInstance) {
        if (attendeeInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            RefAttendeeState attendeeState = RefAttendeeState.get(params.get("refId"))
            attendeeInstance.status = attendeeState?.nextState

            attendeeInstance.lastChange = account
            attendeeInstance.lastChangeDate = new Date()

            attendeeInstance.save flush: true

            if (attendeeState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(attendeeState?.dateGateEvent), eventDate: new Date(), recordedBy: account, attendee: attendeeInstance).save(flush: true)
            }
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", attendeeInstance?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeInstance?.id, action:"show", method:"GET" }
        }
    }

    /**
     * PRIVATE
     * This function saves the funding source for the attendee slot.
     *
     * @param pCost (Cost)
     * @param pFundSource1 (String)
     * @param pFundSource2 (String)
     * @param pFundSource3 (String)
     */
    private void saveFunding(Cost pCost, String pFundSource1, String pFundSource2, String pFundSource3) {
        // remove current funding from attendee slot
        List<FundingSource> fundList = FundingSource.findAllByCost(pCost)
        for (fundingSource in fundList) {
            fundingSource.delete()
        }

        if (pFundSource1) {
            if (Integer.parseInt(pFundSource1) > 0) {
                new FundingSource(fundSource: 'Other US Govt', percentage: Integer.parseInt(pFundSource1), cost: pCost).save(flush: true)
            }
        }
        if (pFundSource2) {
            if (Integer.parseInt(pFundSource2) > 0) {
                new FundingSource(fundSource: 'US Air Force', percentage: Integer.parseInt(pFundSource2), cost: pCost).save(flush: true)
            }
        }
        if (pFundSource3) {
            if (Integer.parseInt(pFundSource3) > 0) {
                new FundingSource(fundSource: 'Non-Federal Entity', percentage: Integer.parseInt(pFundSource3), cost: pCost).save(flush: true)
            }
        }
    }

}
