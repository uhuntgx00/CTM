package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['permitAll'])
class AccountController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def springSecurityService
    def ldapService
    def organizationService
    def sessionRegistry
//    def userCache

//************************************************************************************
// Account Session functions
//************************************************************************************

    def liveAccounts() {
        print sessionRegistry.getAllPrincipals()

    }

    def sessionCount() {
        def count = 0

        sessionRegistry.getAllPrincipals().each {
            count += sessionRegistry.getAllSessions(it, false).size()
        }

        render count
    }

//************************************************************************************
// Account LIST functions
//************************************************************************************

    /**
      *
      * @param pInitialList (List<Conference>) -
      * @return List<Conference>
      */
     private List<Account> parseList(final List<Account> pInitialList) {
         params.max = Math.min(params.max ? params.int('max') : 25, 100)
         params.offset = params.offset ? params.int('offset') : 0

         return pInitialList.subList(params.int('offset'), Math.min(params.int('offset') + params.int('max'), pInitialList.size()))
     }

    /**
     *
     * @param pInitialList (List<Account>) -
     * @return int - count of records
     */
    private int processSearch(final List<Account> pInitialList) {
        int result = 0

        params.max = Math.min(params.max ? params.int('max') : 25, 100)
        params.offset = params.offset ? params.int('offset') : 0

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (!account) {
            return
        }

        if (params.searchAccount) {
            String searchTerm = "%" + params.searchAccount + "%"

            // set the org list
            def orgs = null
            if (account?.hasAuthority("ROLE_TD_ADMIN")) {
                orgs = organizationService.getOrgListById(account?.assignedTD)
            } else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                orgs = organizationService.getOrgListById(account?.assignedTD?.topParent)
            }

            for (acct in Account.findAllByFirstNameIlikeOrLastNameIlikeOrUsernameIlike(searchTerm, searchTerm, searchTerm, [sort: params.sort, order: params.order])) {
                if (account?.hasAuthority("ROLE_TD_ADMIN") || account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                    if (orgs?.contains(acct?.assignedTD?.id)) {
                        pInitialList.add(acct)
                    }
                } else {
                    pInitialList.add(acct)
                }
            }

            result = pInitialList.size()
        } else {
            if (account?.hasAuthority("ROLE_TD_ADMIN")) {
                pInitialList.addAll(organizationService.getAssigned(account?.assignedTD))

                result = pInitialList.size()
            } else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                pInitialList.addAll(organizationService.getAssigned(account?.assignedTD?.topParent))

                result = pInitialList.size()
            } else {
                pInitialList.addAll(Account.list(offset: params.offset, max: params.max, sort: params.sort, order: params.order))

                result = Account.count()
            }
        }

        return result
    }

    /**
     *
     * @return List of ALL accounts
     */
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def index(Integer max) {
        // create an empty list
        List<Account> initialList = new ArrayList<>()

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        int count = processSearch(initialList)
        if (params.searchAccount) {
            initialList = parseList(initialList)
        }

//        // get the user who is logged on currently
//        def account = Account.get(springSecurityService?.principal?.id)
//
//        // only a VALID user account logged in can view the list
//        if (account) {
//            if (account?.hasAuthority("ROLE_TD_ADMIN")) {
//                initialList = organizationService.getAssigned(account?.assignedTD)
//            } else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
//                initialList = organizationService.getAssigned(account?.assignedTD?.topParent)
//            } else {
//                initialList = Account.list(sort: params.sort, order: params.order)
//            }
//
//            if (params.searchAccount) {
//                initialList = initialList.findAll {
//                    it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
//                }
//            }
//        }

        respond initialList, model: [accountInstanceCount: count, listType: 'ALL', indexEvent: 'index', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of CIVILIAN/MILITARY accounts
     */
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def listBase() {
        // create an empty list
        List<Account> initialList = new ArrayList<>()

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            // file the list with an appropriate selection
            if (account?.hasAuthority("ROLE_TD_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("CIVILIAN") || it?.employeeType?.toUpperCase()?.contains("MILITARY") }
            } else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD?.topParent)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("CIVILIAN") || it?.employeeType?.toUpperCase()?.contains("MILITARY") }
            } else {
                initialList = Account.findAllByEmployeeTypeOrEmployeeType("Civilian", "Military", [sort: params.sort, order: params.order])
            }

            if (params.searchAccount) {
                initialList = initialList.findAll {
                    it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
                }
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Civilian/Military', indexEvent: 'listBase', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of CIVILIAN accounts
     */
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def listCivilian() {
        // create an empty list
        List<Account> initialList = new ArrayList<>()

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            // file the list with an appropriate selection
            if (account?.hasAuthority("ROLE_TD_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("CIVILIAN") }
            } else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD?.topParent)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("CIVILIAN") }
            } else {
                initialList = Account.findAllByEmployeeType("Civilian", [sort: params.sort, order: params.order])
            }

            if (params.searchAccount) {
                initialList = initialList.findAll {
                    it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
                }
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Civilian', indexEvent: 'listCivilian', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of CONTRACTOR accounts
     */
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def listContractor() {
        // create an empty list
        List<Account> initialList = new ArrayList<>()

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            // file the list with an appropriate selection
            if (account?.hasAuthority("ROLE_TD_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("CONTRACTOR") }
            } else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD?.topParent)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("CONTRACTOR") }
            } else {
                initialList = Account.findAllByEmployeeType("Contractor", [sort: params.sort, order: params.order])
            }

            if (params.searchAccount) {
                initialList = initialList.findAll {
                    it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
                }
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Contractor', indexEvent: 'listContractor', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of MILITARY accounts
     */
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def listMilitary() {
        // create an empty list
        List<Account> initialList = new ArrayList<>()

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            // file the list with an appropriate selection
            if (account?.hasAuthority("ROLE_TD_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("MILITARY") }
            } else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD?.topParent)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("MILITARY") }
            } else {
                initialList = Account.findAllByEmployeeType("Military", [sort: params.sort, order: params.order])
            }

            if (params.searchAccount) {
                initialList = initialList.findAll {
                    it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
                }
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Military', indexEvent: 'listMilitary', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of OTHER accounts
     */
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def listOther() {
        // create an empty list
        List<Account> initialList = new ArrayList<>()

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            // file the list with an appropriate selection
            if (account?.hasAuthority("ROLE_TD_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("OTHER") }
            } else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                initialList = organizationService.getAssigned(account?.assignedTD?.topParent)
                initialList = initialList.findAll { it?.employeeType?.toUpperCase()?.contains("OTHER") }
            } else {
                initialList = Account.findAllByEmployeeType("Other", [sort: params.sort, order: params.order])
            }

            if (params.searchAccount) {
                initialList = initialList.findAll {
                    it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
                }
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Other', indexEvent: 'listOther', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of UNASSIGNED accounts
     */
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def listUnassigned() {
        List<Account> initialList = Account.findAllByAssignedTD(null, [sort: params.sort, order: params.order])

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) ||
                it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) ||
                it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) ||
                it?.displayName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'indexExt', model: [accountInstanceCount: initialList?.size(), listType: 'Unassigned', indexEvent: 'listUnassigned', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of UNASSIGNED accounts
     */
    @Transactional
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def assignOrg() {
        List<Account> initialList = Account.findAllByAssignedTD(null, [sort: params.sort, order: params.order])

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) ||
                it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) ||
                it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) ||
                it?.displayName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        Organization org = Organization.findByOfficeSymbol(params.searchAccount)
        if (org) {
            for (account in initialList) {
                account.setAssignedTD(org)
                account.save flush:true
            }
        }

        respond parseList(initialList), view: 'indexExt', model: [accountInstanceCount: initialList?.size(), listType: 'Unassigned', indexEvent: 'listUnassigned', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of ASSIGNED accounts
     */
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def listAssigned() {
        def td = Organization.get(params.id)
        List<Account> initialList = td.getAssigned() as List

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Assigned ' + td.officeSymbol, conferenceId: params.id, indexEvent: 'listAssigned', searchAccount: params.searchAccount]
    }

//************************************************************************************
// Account ROLE_EOC functions
//************************************************************************************

    @Secured(['ROLE_EOC'])
    def account(Integer max) {
        // create an empty list
        List<Account> initialList = new ArrayList<>()

        // if not logged-in render NotFound?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        // only a VALID user account logged in can view the list
        if (account) {
            // file the list with an appropriate selection
            initialList = Account.list(sort: params.sort, order: params.order)

            if (params.searchAccount) {
                initialList = initialList.findAll {
                    it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
                }
            }
        }

        respond parseList(initialList), model: [accountInstanceCount: initialList?.size(), listType: 'Login', indexEvent: 'account', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of LOCKED accounts
     */
    @Secured(['ROLE_EOC'])
    def listLocked() {
        List<Account> initialList = Account.findAllByAccountLocked(true, [sort: params.sort, order: params.order])

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Locked', indexEvent: 'listLocked', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of EXPIRED accounts
     */
    @Secured(['ROLE_EOC'])
    def listExpired() {
        List<Account> initialList = Account.findAllByAccountExpired(true, [sort: params.sort, order: params.order])

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Expired', indexEvent: 'listExpired', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of accounts being supervised by Account logged in
     */
    def listSupervising() {
        // get the user who is logged on currently
        def account = Account.get(springSecurityService?.principal?.id)

        List<Account> initialList = Account.findAllBySupervisor(account, [sort: params.sort, order: params.order])

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Supervising', indexEvent: 'listSupervising', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of DISABLED accounts
     */
    @Secured(['ROLE_EOC'])
    def listDisabled() {
        List<Account> initialList = Account.findAllByEnabled(false, [sort: params.sort, order: params.order])

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'Disabled', indexEvent: 'listDisabled', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of ROLE_EOC accounts
     */
    @Secured(['ROLE_EOC'])
    def listEOC() {
        List<AccountRole> roleList = AccountRole.findAllByRole(Role.findByAuthority("ROLE_EOC"))

        List<Account> initialList = new ArrayList<>()
        for (accountRole in roleList) {
            initialList.add(accountRole.account)
        }

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'EOC', indexEvent: 'listEOC', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of ROLE_DEVELOPER accounts
     */
    @Secured(['ROLE_EOC'])
    def listDEV() {
        List<AccountRole> roleList = AccountRole.findAllByRole(Role.findByAuthority("ROLE_DEVELOPER"))

        List<Account> initialList = new ArrayList<>()
        for (accountRole in roleList) {
            initialList.add(accountRole.account)
        }

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'DEVELOPER', indexEvent: 'listDEV', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of ROLE_ADMIN accounts
     */
    @Secured(['ROLE_EOC'])
    def listADMIN() {
        List<AccountRole> roleList = AccountRole.findAllByRole(Role.findByAuthority("ROLE_ADMIN"))

        List<Account> initialList = new ArrayList<>()
        for (accountRole in roleList) {
            initialList.add(accountRole.account)
        }
        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'ADMIN', indexEvent: 'ListADMIN', searchAccount: params.searchAccount]
    }

    /**
     *
     * @return List of ROLE_EXPIRE accounts
     */
    @Secured(['ROLE_EOC'])
    def listEXPIRE() {
        List<AccountRole> roleList = AccountRole.findAllByRole(Role.findByAuthority("ROLE_EXPIRE"))

        List<Account> initialList = new ArrayList<>()
        for (accountRole in roleList) {
            initialList.add(accountRole.account)
        }

        if (params.searchAccount) {
            initialList = initialList.findAll {
                it?.firstName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.lastName?.toUpperCase()?.contains(params.searchAccount?.toUpperCase()) || it?.username?.toUpperCase()?.contains(params.searchAccount?.toUpperCase())
            }
        }

        respond parseList(initialList), view: 'index', model: [accountInstanceCount: initialList?.size(), listType: 'EXPIRE', indexEvent: 'ListEXPIRE', searchAccount: params.searchAccount]
    }

//************************************************************************************
// Account BASIC Functions
//************************************************************************************

    /**
     * Current authentication information (displayed on the name select menu item)
     */
    def currentAuth = {
        // if not logged-in render NotFount?
        if (!springSecurityService.isLoggedIn()) {
            notFound()
            return
        }

        [auth: SecurityContextHolder?.context?.authentication, accountInstance: Account.get(springSecurityService?.principal?.id)]
   	}

    /**
     * Default "NOT FOUND" redirect
     */
    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'accountInstance.label', default: 'Account'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    /**
     * This function allows an account to be viewed. Additional role-based permissions will restrict actions on the view page.
     *
     * NOTE: ROLE_EOC, ROLE_ADMIN, ROLE_DEVELOPER, ROLE_FMC_ADMIN allows unrestricted "view" of ALL accounts
     * NOTE: ROLE_TD_ADMIN/ROLE_TD_FULL_ADMIN allows only "view" of those accounts within the SAME TD as the ADMIN
     * NOTE: ROLE_USER allows viewing only their account
     *
     * @param accountInstance (Account) -
     * @return
     */
    def show(Account accountInstance) {
        if (accountInstance == null) {
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

        // only a VALID user account logged in can create an account
        if (account) {
            // if the account has these authorities ALLOW access to ALL account "view"
            if (account?.hasAuthority("ROLE_EOC") || account?.hasAuthority("ROLE_ADMIN") || account?.hasAuthority("ROLE_FMC_ADMIN") || account?.hasAuthority("ROLE_DEVELOPER")) {
                respond accountInstance
                return
            }

            // if the account has ROLE_TD_ADMIN ensure that only those within that ADMIN organization or below can be "viewed"
            else if (account?.hasAuthority("ROLE_TD_ADMIN")) {
                if (organizationService.getOrgListById(account?.assignedTD).contains(accountInstance?.assignedTD?.id)) {
                    respond accountInstance
                    return
                }
            }

            // if the account has ROLE_TD_FULL_ADMIN ensure that only those within that ADMIN organization (parent) can be "viewed"
            else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                if (account?.assignedTD?.topParent?.id == accountInstance?.assignedTD?.topParent?.id) {
                    respond accountInstance
                    return
                }
            }

            // if the account has ROLE_USER (or ROLE_AFRL_USER/ROLE_NON_AFRL_USER) then ensure that they can ONLY "view" their account
            else if (account?.hasAuthority("ROLE_USER")) {
                if (accountInstance?.username?.equalsIgnoreCase(account?.username)) {
                    respond accountInstance
                    return
                }
            }
        }

        notFound()
    }

    /**
     * ROLE_EOC is the only role that is capable of "creating" an account manually within the application.
     *
     * @return
     */
    @Secured(['ROLE_EOC'])
    def create() {
        respond new Account(params)
    }

    /**
     * ROLE_EOC is the only role that is capable of "creating" an account manually within the application.
     *
     * @param accountInstance (Account)
     * @return
     */
    @Transactional
    @Secured(['ROLE_EOC'])
    def save(Account accountInstance) {
        if (accountInstance == null) {
            notFound()
            return
        }

        // if there are any ERRORS redirect back to the screen for correction
        if (accountInstance.hasErrors()) {
            respond accountInstance.errors, view: 'create'
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
            accountInstance.lastChange = account
            accountInstance.lastChangeDate = new Date()
            accountInstance.accountEdit++
            accountInstance.save flush: true

            // automatically enable ROLE_USER as default role added to an account
            def accountRole = Role.findByAuthority("ROLE_USER")
            AccountRole.create accountInstance, accountRole, true
        }

        request.withFormat {
            form {
                flash.message = message(code: 'default.accountCreated.message', args: [accountInstance.username])
                redirect accountInstance
            }
            '*' { respond accountInstance, [status: CREATED] }
        }
    }

    /**
     * This function allows an account to be edited. Additional role-based permissions will restrict actions on the edit page.
     *
     * NOTE: ROLE_EOC, ROLE_DEVELOPER allows unrestricted "edit" of ALL accounts
     * NOTE: ROLE_TD_ADMIN/ROLE_TD_FULL_ADMIN allows only "edit" of those accounts within the SAME TD as the ADMIN
     * NOTE: ROLE_USER allows editing only their account
     *
     * @param accountInstance (Account)
     * @return
     */
    @Secured(['ROLE_EOC', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_USER', 'ROLE_DEVELOPER'])
    def edit(Account accountInstance) {
        if (accountInstance == null) {
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

        // only a VALID user account logged in can create an account
        if (account) {
            // if the account has these authorities ALLOW access to ALL account "edit"
            if (account?.hasAuthority("ROLE_EOC") || account?.hasAuthority("ROLE_FMC_ADMIN") || account?.hasAuthority("ROLE_DEVELOPER")) {
                respond accountInstance
                return
            }

            // if the account has ROLE_TD_ADMIN ensure that only those within that ADMIN organization can be "edited"
            else if (account?.hasAuthority("ROLE_TD_ADMIN")) {
                if (organizationService.getOrgListById(account?.assignedTD).contains(accountInstance?.assignedTD?.id)) {
                    respond accountInstance
                    return
                }
            }

            // if the account has ROLE_TD_FULL_ADMIN ensure that only those within that ADMIN organization (parent) can be "edited"
            else if (account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                if (account?.assignedTD?.topParent?.id == accountInstance?.assignedTD?.topParent?.id) {
                    respond accountInstance
                    return
                }
            }

            // if the account has ROLE_USER (or ROLE_AFRL_USER/ROLE_NON_AFRL_USER) then ensure that they can ONLY "edit" their account
            else if (account?.hasAuthority("ROLE_USER")) {
                if (accountInstance?.username?.equalsIgnoreCase(account?.username)) {
                    respond accountInstance
                    return
                }
            }
        }

        notFound()
    }

    /**
     *
     * @param accountInstance (Account)
     * @return
     */
    @Transactional
    @Secured(['ROLE_EOC', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_USER', 'ROLE_DEVELOPER'])
    def update(Account accountInstance) {
        if (accountInstance == null) {
            notFound()
            return
        }

        // if there are any ERRORS redirect back to the screen for correction
        if (accountInstance.hasErrors()) {
            respond accountInstance.errors, view: 'edit'
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
            accountInstance.lastChange = account
            accountInstance.lastChangeDate = new Date()
            accountInstance.accountEdit++

            if (accountInstance.isDirty('accountExpired')) {
                accountInstance.lastLoginDate = new Date()
            }

            accountInstance.save flush: true

            for (accountRole in AccountRole.findAllByAccount(accountInstance)) {
                accountRole.delete()
            }

       		addRoles accountInstance
        }

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Account.label', default: 'Account'), accountInstance.username])
                redirect accountInstance
            }
            '*' { respond accountInstance, [status: OK] }
        }
    }

    /**
     * PRIVATE
     *
     * This function creates/adds the roles from the browser results (params) to the given account.
     *
     * @param pAccount (Account) - current account to add roles to
     */
    private void addRoles(Account pAccount) {
        for (String key in params.keySet()) {
            if (key.contains('ROLE_') && 'on' == params.get(key)) {
                AccountRole.create pAccount, Role.findByAuthority(key), true
            }
        }
   	}

    /**
     * ROLE_EOC has the capability to perform this action.
     *
     * The "delete" function only marks the record and locks it. Accounts can not typically be deleted from the system.
     * If the account was actually possible to delete, the LDAP sync at the end-of-day would typically automatically
     * reinsert the account with login permissions enabled.
     *
     * @param accountInstance (Account) -
     * @return
     */
    @Transactional
    @Secured(['ROLE_EOC'])
    def delete(Account accountInstance) {
        if (accountInstance == null) {
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

        // only a VALID user account logged in can modify the account
        if (account) {
            accountInstance.markedForDeletion = true
            accountInstance.accountLocked = true
            accountInstance.lastChange = account
            accountInstance.lastChangeDate = new Date()
            accountInstance.accountEdit++
            accountInstance.save flush: true
        }

        request.withFormat {
            form {
                redirect accountInstance
            }
            '*' { render status: OK }
        }
    }

    /**
     * ROLE_EOC has the capability to perform this action.
     *
     * @param accountInstance (Account) -
     * @return
     */
    @Transactional
    @Secured(['ROLE_EOC'])
    def undelete(Account accountInstance) {
        if (accountInstance == null) {
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

        // only a VALID user account logged in can modify the account
        if (account) {
            accountInstance.markedForDeletion = false
            accountInstance.accountLocked = false
            accountInstance.lastChange = account
            accountInstance.lastChangeDate = new Date()
            accountInstance.accountEdit++
            accountInstance.save flush: true
        }

        request.withFormat {
            form {
                redirect accountInstance
            }
            '*' { render status: OK }
        }
    }

    /**
     * ROLE_EOC, ROLE_ADMIN or ROLE_TD_ADMIN may verify accounts against LDAP.
     *
     * @param accountInstance (Account) -
     * @return
     */
    @Transactional
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN'])
    def verify(Account accountInstance) {
        if (accountInstance == null) {
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

        // validate that the ROLE_TD_ADMIN and account are within the same TD structure
        boolean valid = false
        if (account?.hasAuthority('ROLE_TD_ADMIN')) {
            valid = organizationService.getOrgListById(account?.assignedTD).contains(accountInstance?.assignedTD?.id)
        }

        // validate that the ROLE_TD_FULL_ADMIN and account are within the same TD structure (parent)
        if (account?.hasAuthority('ROLE_TD_FULL_ADMIN')) {
            valid = accountInstance?.assignedTD?.topParent?.id == account?.assignedTD?.topParent?.id
        }

        // only a VALID user account logged in can modify the account
        if (account && valid) {
            accountInstance = ldapService.processLdapLookup(accountInstance)

            accountInstance.lastChange = account
            accountInstance.lastChangeDate = new Date()
            accountInstance.accountEdit++
            accountInstance.save flush: true
        }

        request.withFormat {
            form {
                redirect accountInstance
            }
            '*' { render status: OK }
        }
    }

    /**
     * ROLE_EOC, ROLE_ADMIN or ROLE_TD_ADMIN/ROLE_TD_FULL_ADMIN may validate accounts.
     *
     * @param accountInstance (Account) -
     * @return
     */
    @Transactional
    @Secured(['ROLE_EOC', 'ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN'])
    def validate(Account accountInstance) {
        if (accountInstance == null) {
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

        // validate that the ROLE_TD_ADMIN and account are within the same TD structure
        boolean valid = false
        if (account?.hasAuthority('ROLE_TD_ADMIN')) {
            valid = organizationService.getOrgListById(account?.assignedTD).contains(accountInstance?.assignedTD?.id)
        }

        // validate that the ROLE_TD_FULL_ADMIN and account are within the same TD structure (parent)
        if (account?.hasAuthority('ROLE_TD_FULL_ADMIN')) {
            valid = accountInstance?.assignedTD?.topParent?.id == account?.assignedTD?.topParent?.id
        }

        // only a VALID user account logged in can modify the account
        if (account && valid) {
            accountInstance.accountValidated = true
            accountInstance.lastChange = account
            accountInstance.lastChangeDate = new Date()
            accountInstance.accountEdit++
            accountInstance.save flush: true
        }

        request.withFormat {
            form {
                redirect accountInstance
            }
            '*' { render status: OK }
        }
    }

    /**
     * ROLE_EOC or ROLE_ADMIN may invalidate accounts.
     * NOTE: ROLE_TD_ADMIN/ROLE_TD_FULL_ADMIN may not invalidate any account even those under their control.
     *
     * @param accountInstance (Account) -
     * @return
     */
    @Transactional
    @Secured(['ROLE_EOC', 'ROLE_ADMIN'])
    def invalidate(Account accountInstance) {
        if (accountInstance == null) {
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

        // validate that the ROLE_TD_ADMIN and account are within the same TD structure
        boolean valid = false
        if (account?.hasAuthority('ROLE_TD_ADMIN')) {
            valid = organizationService.getOrgListById(account?.assignedTD).contains(accountInstance?.assignedTD?.id)
        }

        // validate that the ROLE_TD_FULL_ADMIN and account are within the same TD structure (parent)
        if (account?.hasAuthority('ROLE_TD_FULL_ADMIN')) {
            valid = accountInstance?.assignedTD?.topParent?.id == account?.assignedTD?.topParent?.id
        }

        if (account?.hasAuthority('ROLE_EOC') || account?.hasAuthority('ROLE_ADMIN')) {
            valid = true
        }

        // only a VALID user account logged in can modify the account
        if (account && valid) {
            accountInstance.accountValidated = false
            accountInstance.emailValidated = false
            accountInstance.lastChange = account
            accountInstance.lastChangeDate = new Date()
            accountInstance.accountEdit++
            accountInstance.save flush: true
        }

        request.withFormat {
            form {
                redirect accountInstance
            }
            '*' { render status: OK }
        }
    }

    /**
     * This is the MANUAL LDAP sync that can be performed by either ROLE_EOC or ROLE_DEVELOPER
     *
     * @return
     */
    @Transactional
    @Secured(['ROLE_EOC', 'ROLE_DEVELOPER'])
    def processSync() {
        ldapService.processSync()
        respond Account.list(params), view: 'index', model: [accountInstanceCount: Account.count(), listType: 'ALL']
    }

}
