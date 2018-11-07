package mil.ebs.ctm

class Organization {

    transient springSecurityService
    transient organizationService

    String name
    String officeSymbol

    Account director
    String missionStatement

    Boolean supervisorApprovalRequired = false
    int supervisorPeriod = 10
    Boolean attendeeRequestRequired = false
    Boolean allowCreateConference = true
    Boolean allowAttendeeNotification = true

    Boolean trueTD = false
    String levelTD
    Organization parent

    Date createdDate
    Date lastChangeDate
    Account lastChange
    int accountEdit = 0

    static hasMany = [contacts: AccountContact]

    static constraints = {
        name blank: false, nullable: false
        officeSymbol blank: false, nullable: false, unique: true

        director blank: true, nullable: true
        contacts blank: true, nullable: true

        missionStatement blank: true, nullable: true

        supervisorApprovalRequired blank: false, nullable: false
        supervisorPeriod blank: false, nullable: false
        attendeeRequestRequired blank: false, nullable: false
        allowCreateConference blank: false, nullable: false
        allowAttendeeNotification blank: false, nullable: false

        trueTD blank: false, nullable: false
        levelTD inlist: ['', '1', '2', '3', '4'], blank: true, nullable: true, maxSize: 25
        parent blank: true, nullable: true

        createdDate blank: true, nullable: true
        lastChangeDate blank: true, nullable: true
        lastChange blank: true, nullable: true
        accountEdit blank: false, nullable: false
    }

    static mapping = {
        version false

        createdDate type:'java.sql.Date'
        lastChangeDate type:'java.sql.Date'
    }

    @Override
    public String toString() {
        return "${name} (${officeSymbol})";
    }

    /**
     *
     * @return
     */
    public String getParentName() {
        if (levelTD?.equalsIgnoreCase("4")) {
            return parent?.parent?.parent?.name
        } else if (levelTD?.equalsIgnoreCase("3")) {
            return parent?.parent?.name
        } else if (levelTD?.equalsIgnoreCase("2")) {
            return parent?.name
        } else if (levelTD?.equalsIgnoreCase("1")) {
            return null
        }
    }

    public String toParentString() {
        return getParentName() ? "${getParentName()} (${officeSymbol})" : toString()
    }

// --------------------------------------------
// functions
// --------------------------------------------

    /**
     *
     * @return
     */
    Set<Attendee> getAttendees() {
        List<Attendee> result = new ArrayList<Attendee>()

        def temp = Attendee.findAllByReservedTD(this)
        result.addAll(temp)

        if (levelTD?.equalsIgnoreCase("1") || levelTD?.equalsIgnoreCase("2") || levelTD?.equalsIgnoreCase("3")) {
            def temp2 = Organization.findAllByParent(this)
            for (td2 in temp2) {
                def alist2 = Attendee.findAllByReservedTD(td2)
                result.addAll(alist2)

                if (td2?.levelTD?.equalsIgnoreCase("2") || td2?.levelTD?.equalsIgnoreCase("3")) {
                    def temp3 = Organization.findAllByParent(td2)
                    for (td3 in temp3) {
                        def alist3 = Attendee.findAllByReservedTD(td3)
                        result.addAll(alist3)

                        if (td3?.levelTD?.equalsIgnoreCase("3")) {
                            def temp4 = Organization.findAllByParent(td3)
                            for (td4 in temp4) {
                                def alist4 = Attendee.findAllByReservedTD(td4)
                                result.addAll(alist4)
                            }
                        }
                    }
                }
            }
        }

        return result as Set
    }

    /**
     *
     * @return
     */
    Double getEstimates() {
        def list = getAttendees() as List

        Double estimate = 0.0
        for (attendee in list) {
            if (attendee?.estimateTotal()) {
                estimate += attendee?.estimateTotal()
            }
        }

        return estimate
    }

    /**
     *
     * @return
     */
    Double getActuals() {
        def list = getAttendees() as List

        Double actual = 0.0
        for (attendee in list) {
            if (attendee?.actualTotal()) {
                actual += attendee?.actualTotal()
            }
        }

        return actual
    }

    /**
     * Get assigned accounts to the organization...
     *
     * @return
     */
    Set<Account> getAssigned() {
        return organizationService.getAssigned(this) as Set
    }

    /**
     * Get assigned ALLOWED accounts to the organization...
     * These accounts are those that are allowed to ATTEND a conference
     *
     * @return
     */
    Set<Account> getAssignedAllowed() {
        return organizationService.getAssignedAllowed(this) as Set
    }

    /**
     *
     * @return
     */
    Organization getTopParent() {
        if (levelTD?.equalsIgnoreCase("1") && parent && trueTD) {
            return this.parent
        }else if (levelTD?.equalsIgnoreCase("1") || !trueTD) {
            return this
        } else if (levelTD?.equalsIgnoreCase("2")) {
            Organization org = this.parent
            if (org?.levelTD?.equalsIgnoreCase("1") && org.parent && org.trueTD) {
                org = org.parent
            }
            return org
        } else if (levelTD?.equalsIgnoreCase("3")) {
            Organization org = this.parent.parent
            if (org?.levelTD?.equalsIgnoreCase("1") && org.parent && org.trueTD) {
                org = org.parent
            }
            return org
        } else if (levelTD?.equalsIgnoreCase("4")) {
            Organization org = this.parent.parent.parent
            if (org?.levelTD?.equalsIgnoreCase("1") && org.parent && org.trueTD) {
                org = org.parent
            }
            return org
        }

        return null
    }

    /**
     *
     * @return
     */
    Set<Conference> getResponsible() {
        Conference.findAllByResponsibleTD(this) as Set
    }

    /**
     *
     * @param pLevel (int) -
     * @return
     */
    Set<Organization> getChildren(final int pLevel) {
        List<Organization> result = new ArrayList<Organization>()

        if (trueTD) {
            def temp = Organization.findAllByParent(this)
            if (pLevel == 0 && this.levelTD.equalsIgnoreCase("1")) {
                for (td1 in temp) {
                    if (td1.levelTD.equalsIgnoreCase("1")) {
                        result.add(td1)
                    }
                }
            }

            if (pLevel == 1) {
                for (td1 in temp) {
                    if (!td1.levelTD.equalsIgnoreCase("1")) {
                        result.add(td1)
                    }
                }
            }

            if (pLevel >= 2) {
                for (td2 in temp) {
                    def temp2 = Organization.findAllByParent(td2)
                    if (pLevel == 2) {
                        result.addAll(temp2)
                    }

                    if (pLevel >= 3) {
                        for (td3 in temp2) {
                            def temp3 = Organization.findAllByParent(td3)
                            result.addAll(temp3)
                        }
                    }
                }
            }
        }

        result as Set
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
        return diffInDays(getLastChangeDate(), new Date())
    }

// --------------------------------------------
// Data/Access Related
// --------------------------------------------

    /**
     *
     * @return Boolean - TRUE if account can edit | FALSE if account can not edit
     */
    public Boolean canEdit() {
        Boolean check = false
        def account = Account.get(springSecurityService?.principal?.id)

        if (account?.hasAuthority("ROLE_TD_ADMIN") && (organizationService.getOrgListById(account?.assignedTD).contains(this?.id))) {
            check = true
        }

        if (account?.hasAuthority("ROLE_TD_FULL_ADMIN") && (account?.assignedTD?.topParent?.id == this?.topParent?.id )) {
            check = true
        }

        if (account?.hasAuthority("ROLE_FMC_ADMIN")) {
            check = true
        }

        if (account?.hasAuthority("ROLE_ADMIN")) {
            check = true
        }

        if (account?.hasAuthority("ROLE_DEVELOPER")) {
            check = true
        }

        return check
    }

}
