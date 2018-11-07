package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["permitAll"])
class OrganizationController {

    static allowedMethods = [save: "POST", update: "PUT", addAccount: "PUT", delete: "DELETE"]

    def springSecurityService
    def attendeeService

    def index(Integer max) {
        params.max = Math.min(max ?: 25, 100)

        List<Organization> initialList = Organization.list()
        if (params.searchOfficeSymbol) {
            initialList = initialList.findAll { it.officeSymbol.toUpperCase().contains(params.searchOfficeSymbol.toUpperCase()) || it.officeSymbol.contains(params.searchOfficeSymbol.toUpperCase()) }
        }

        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.offset = params.offset ? params.int('offset') : 0
        List<Organization> list = initialList.subList(params.int('offset'), Math.min(params.int('offset') + params.int('max'), initialList.size()))

        respond list, model: [organizationInstanceCount: initialList.size(), typeList: 'ALL', indexEvent: 'index', searchOfficeSymbol: params.searchOfficeSymbol]
    }

    def tdList(Integer max) {
        params.max = Math.min(max ?: 25, 100)

        def initialList = Organization.findAllByLevelTDAndParent("1", null)
        if (params.searchOfficeSymbol) {
            initialList = initialList.findAll { it.officeSymbol.toUpperCase().contains(params.searchOfficeSymbol.toUpperCase()) || it.officeSymbol.contains(params.searchOfficeSymbol.toUpperCase()) }
        }

        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.offset = params.offset ? params.int('offset') : 0
        List<Organization> list = initialList.subList(params.int('offset'), Math.min(params.int('offset') + params.int('max'), initialList.size()))

        respond list, view: 'index', model: [organizationInstanceCount: initialList.size(), typeList: 'TD', indexEvent: 'tdList', searchOfficeSymbol: params.searchOfficeSymbol]
    }

    def nonAfrlList(Integer max) {
        params.max = Math.min(max ?: 25, 100)

        def initialList = Organization.findAllByTrueTD(false)
        if (params.searchOfficeSymbol) {
            initialList = initialList.findAll { it.officeSymbol.toUpperCase().contains(params.searchOfficeSymbol.toUpperCase()) || it.officeSymbol.contains(params.searchOfficeSymbol.toUpperCase()) }
        }

        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.offset = params.offset ? params.int('offset') : 0
        List<Organization> list = initialList.subList(params.int('offset'), Math.min(params.int('offset') + params.int('max'), initialList.size()))

        respond list, view: 'index', model: [organizationInstanceCount: initialList.size(), typeList: 'Non-AFRL', indexEvent: 'nonAfrlList', searchOfficeSymbol: params.searchOfficeSymbol]
    }


    def show(Organization organizationInstance) {
        List<Attendee> attendeeList = organizationInstance.getAttendees() as List

        List<Integer> countList = new ArrayList<Integer>()

        int attendeeCount = 0
        int boothCount = 0
        int panelCount = 0
        int chairCount = 0
        int speakerCount = 0
        int supportCount = 0
        int otherCount = 0

        for (attendee in attendeeList) {
            switch (attendee?.attendanceType) {
                case 'Attendee':
                    attendeeCount++
                    break

                case 'Booth/Display':
                    boothCount++
                    break

                case 'Discussion Panel':
                    panelCount++
                    break

                case 'Session Chair':
                    chairCount++
                    break

                case 'Presenter/Speaker':
                    speakerCount++
                    break

                case 'Support':
                    supportCount++
                    break

                case 'Other':
                    otherCount++
                    break
            }
        }

        countList.add(attendeeCount)
        countList.add(boothCount)
        countList.add(panelCount)
        countList.add(chairCount)
        countList.add(speakerCount)
        countList.add(supportCount)
        countList.add(otherCount)

        def data = [['Attendee', attendeeCount], ['Booth/Display', boothCount], ['Discussion Panel', panelCount],
                ['Session Chair', chairCount], ['Presenter/Speaker', speakerCount], ['Support', supportCount], ['Other', otherCount]]
        def columns = [['string', 'Attendance Type'], ['number', 'Number of Attendees']]

        def assignedList = Account.findAllByAssignedTD(organizationInstance).sort { it.displayName }
        def totalAssignedList = organizationInstance?.getAssigned()?.sort { it.displayName }

        if (params.paginate == 'assigned') {
            def assignedPagination = [max: params.max, offset: params.offset]
            session.assignedPagination = assignedPagination
        } else if (params.paginate == 'totalAssigned') {
            def totalAssignedPagination = [max: params.max, offset: params.offset]
            session.totalAssignedPagination = totalAssignedPagination
        }

//        params.max = Math.min(params.max ? params.int('max') : 25, 100)
//        params.offset = params.offset ? params.int('offset') : 0

        def list = paginateList(assignedList, session.assignedPagination ? Integer.parseInt(session.assignedPagination.max) : 25, session.assignedPagination ? Integer.parseInt(session.assignedPagination.offset) : 0)
//        def list = assignedList(session.assignedPagination ?: [max: 25, offset: 0])
//        def list = paginateList(assignedList, params.int('max'), params.int('offset'))

//        params.maxTotal = Math.min(params.maxTotal ? params.int('maxTotal') : 25, 100)
//        params.offsetTotal = params.offsetTotal ? params.int('offsetTotal') : 0

        def totalList = paginateList(totalAssignedList, session.totalAssignedPagination ? Integer.parseInt(session.totalAssignedPagination.max) : 25, session.totalAssignedPagination ? Integer.parseInt(session.totalAssignedPagination.offset) : 0)
//        def totalList = totalAssignedList(session.totalAssignedPagination ?: [max:25, offset: 0])
//        def totalList = paginateList(totalAssignedList, params.int('maxTotal'), params.int('offsetTotal'))

        params.offset = null
        params.max = null

        respond organizationInstance, model: [countList: countList, totalCount: attendeeList.size(), columns: columns, data: data,
                assignedList: list, assignedListCount: assignedList.size(), totalAssignedList: totalList, totalAssignedListCount: totalAssignedList.size()]
    }

    private static List paginateList(List pList, int pMax, int pOffset) {
        if (pOffset > Math.min(pOffset + pMax, pList.size())) {
            pOffset = 0
        }

        if (pList) {
            pList = pList.subList(pOffset, Math.min(pOffset + pMax, pList.size()))
        }

        return pList
    }

//********************************************************************************
// Organization SUB-ORG functions
//********************************************************************************

    @Secured(["ROLE_ADMIN", "ROLE_FMC_ADMIN", "ROLE_DEVELOPER"])
    def addDivision() {
        Organization td = new Organization(params)

        if (params.parentId) {
            Organization parent = Organization.get(params.parentId)

            td.trueTD = true
            td.levelTD = "2"
            td.parent = parent
            td.name = parent.name
        }

        [organizationInstance: td]
    }

    @Secured(["ROLE_ADMIN", "ROLE_FMC_ADMIN", "ROLE_DEVELOPER"])
    def addBranch() {
        Organization td = new Organization(params)

        if (params.parentId) {
            Organization parent = Organization.get(params.parentId)

            td.trueTD = true
            td.levelTD = "3"
            td.parent = parent
            td.name = parent.name
        }

        [organizationInstance: td]
    }

    @Secured(["ROLE_ADMIN", "ROLE_FMC_ADMIN", "ROLE_DEVELOPER"])
    def addSection() {
        Organization td = new Organization(params)

        if (params.parentId) {
            Organization parent = Organization.get(params.parentId)

            td.trueTD = true
            td.levelTD = "4"
            td.parent = parent
            td.name = parent.name
        }

        [organizationInstance: td]
    }

//********************************************************************************
// Organization BASIC functions
//********************************************************************************

    /**
     * Default "NOT FOUND" redirect
     */
    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'organizationInstance.label', default: 'Organization'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    /**
     * ROLE_DEVELOPER or ROLE_ADMIN
     *
     * @return
     */
    @Secured(["ROLE_DEVELOPER", "ROLE_ADMIN"])
    def create() {
        respond new Organization(params)
    }

    /**
     * ROLE_DEVELOPER or ROLE_ADMIN
     *
     * @param organizationInstance (Organization)
     * @return
     */
    @Transactional
    @Secured(["ROLE_DEVELOPER", "ROLE_ADMIN"])
    def save(Organization organizationInstance) {
        if (organizationInstance == null) {
            notFound()
            return
        }

        if (organizationInstance.hasErrors()) {
            respond organizationInstance.errors, view: 'create'
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can create an account
        if (account) {
            if (params.parentID) {
                organizationInstance.parent = Organization.get(params.parentID)
            }

            organizationInstance.levelTD = "1"
            organizationInstance.createdDate = new Date()
            organizationInstance.lastChange = account
            organizationInstance.lastChangeDate = new Date()
            organizationInstance.accountEdit++

            organizationInstance.save flush: true
        }

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'Organization.label', default: 'Organization'), organizationInstance.name])
                redirect organizationInstance
            }
            '*' { respond organizationInstance, [status: CREATED] }
        }
    }

    /**
     *
     * @param organizationInstance (Organization)
     * @return
     */
    @Secured(["ROLE_ADMIN", "ROLE_TD_FULL_ADMIN", "ROLE_TD_ADMIN", "ROLE_FMC_ADMIN", "ROLE_DEVELOPER"])
    def edit(Organization organizationInstance) {
        respond organizationInstance
    }

    /**
     *
     * @param organizationInstance (Organization)
     * @return
     */
    @Transactional
    @Secured(["ROLE_ADMIN", "ROLE_TD_FULL_ADMIN", "ROLE_TD_ADMIN", "ROLE_FMC_ADMIN", "ROLE_DEVELOPER"])
    def update(Organization organizationInstance) {
        if (organizationInstance == null) {
            notFound()
            return
        }

        if (organizationInstance.hasErrors()) {
            respond organizationInstance.errors, view: 'edit'
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can create an account
        if (account) {
            organizationInstance.lastChange = account
            organizationInstance.lastChangeDate = new Date()
            organizationInstance.accountEdit++

            organizationInstance.save flush: true
        }

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Organization.label', default: 'Organization'), organizationInstance.name])
                redirect organizationInstance
            }
            '*' { respond organizationInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(["ROLE_DEVELOPER", "ROLE_ADMIN"])
    def delete(Organization organizationInstance) {

        if (organizationInstance == null) {
            notFound()
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        organizationInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Organization.label', default: 'Organization'), organizationInstance.name])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    /**
     *
     * @param pOrganizationInstance (Organization)
     * @return
     */
    @Transactional
    @Secured(["ROLE_TD_ADMIN", "ROLE_TD_FULL_ADMIN", "ROLE_ADMIN", "ROLE_FMC_ADMIN", "ROLE_DEVELOPER"])
    def addContact(Organization pOrganizationInstance) {
        if (pOrganizationInstance == null) {
            notFound()
            return
        }

        if (pOrganizationInstance.hasErrors()) {
            respond pOrganizationInstance.errors, view: 'edit'
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def accountU = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can create an account
        if (accountU) {
            Account account = Account.get(params.get("tempAccount.id"))
            if (params.get("_action_addContact").equals("Primary")) {
                resetContacts(pOrganizationInstance)
                pOrganizationInstance.contacts.add(new AccountContact(accountLink: account, tdLink: pOrganizationInstance, primaryPOC: true))
            } else {
                pOrganizationInstance.contacts.add(new AccountContact(accountLink: account, tdLink: pOrganizationInstance, primaryPOC: false))
            }

            pOrganizationInstance.lastChange = accountU
            pOrganizationInstance.lastChangeDate = new Date()
            pOrganizationInstance.accountEdit++

            pOrganizationInstance.save flush: true
        }

        request.withFormat {
            form {
                redirect controller:"Organization", id: pOrganizationInstance.id, action:"edit"
            }
            '*' { redirect controller:"Organization", id: pOrganizationInstance.id, action:"edit" }
        }
    }

    /**
     *
     * @param pOrganizationInstance (Organization)
     * @return
     */
    @Transactional
    @Secured(["ROLE_TD_ADMIN", "ROLE_TD_FULL_ADMIN", "ROLE_ADMIN", "ROLE_FMC_ADMIN", "ROLE_DEVELOPER"])
    def deleteContact(Organization pOrganizationInstance) {
        if (pOrganizationInstance == null) {
            notFound()
            return
        }

        if (pOrganizationInstance.hasErrors()) {
            respond pOrganizationInstance.errors, view: 'edit'
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def accountU = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can create an account
        if (accountU) {
            AccountContact accountContact = AccountContact.get(params.get("contactId"))

            pOrganizationInstance.contacts.remove(accountContact)

            // if this is a PRIMARY poc being deleted select new primary poc if possible
            if (accountContact.primaryPOC) {
                resetContacts(pOrganizationInstance)
                if (!pOrganizationInstance.contacts.isEmpty()) {
                    AccountContact tempContact = pOrganizationInstance.contacts.getAt(0)
                    tempContact.primaryPOC = true
                }
            }

            accountContact.delete(flush: true)

            pOrganizationInstance.lastChange = accountU
            pOrganizationInstance.lastChangeDate = new Date()
            pOrganizationInstance.accountEdit++

            pOrganizationInstance.save flush: true
        }

        request.withFormat {
            form {
                redirect controller:"organization", id: pOrganizationInstance.id, action:"edit"
            }
            '*' { redirect controller:"organization", id: pOrganizationInstance.id, action:"edit" }
        }
    }

    /**
     *
     * @param pOrganizationInstance (Organization)
     * @return
     */
    @Transactional
    @Secured(["ROLE_TD_ADMIN", "ROLE_TD_FULL_ADMIN", "ROLE_ADMIN", "ROLE_FMC_ADMIN", "ROLE_DEVELOPER"])
    def primaryContact(Organization pOrganizationInstance) {
        if (pOrganizationInstance == null) {
            notFound()
            return
        }

        if (pOrganizationInstance.hasErrors()) {
            respond pOrganizationInstance.errors, view: 'edit'
            return
        }

        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def accountU = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can create an account
        if (accountU) {
            resetContacts(pOrganizationInstance)

            AccountContact accountContact = AccountContact.get(params.get("contactId"))
            accountContact.primaryPOC = true

            accountContact.save flush: true

            pOrganizationInstance.lastChange = accountU
            pOrganizationInstance.lastChangeDate = new Date()
            pOrganizationInstance.accountEdit++

            pOrganizationInstance.save flush: true
        }

        request.withFormat {
            form {
                redirect controller:"organization", id: pOrganizationInstance.id, action:"edit"
            }
            '*' { redirect controller:"organization", id: pOrganizationInstance.id, action:"edit" }
        }
    }

    /**
     *
     * @param pOrganizationInstance (Organization)
     */
    private static resetContacts(final Organization pOrganizationInstance) {
        for (accountContact in pOrganizationInstance.contacts) {
            accountContact.primaryPOC = false
            accountContact.save flush: true
        }
    }

}
