package mil.ebs.ctm

import mil.ebs.ctm.ref.RefRankGrade
import mil.ebs.ctm.upload.FileUpload

@SuppressWarnings("GroovyUnusedDeclaration")
class Account {

	transient springSecurityService

// --------------------------------------------
// Spring Security CORE Data Elements
// --------------------------------------------

    String username                     // CAC - this is the cac id is stored

    boolean enabled                     // account is either active (in use) or inactive (not validate)

   	boolean accountExpired              // account has not been used for over 35 days
   	boolean accountLocked               // account has been temporarily locked for maintenance

    String password = "password"        // this is not used (required by the Spring Security Core engine)
                                        // by default this will be set to '*password*'
   	boolean passwordExpired             // this is not used (required by the Spring Security Core engine)

// --------------------------------------------
// Additional Collection Data
// --------------------------------------------

    String employeeType = "Civilian"
    RefRankGrade rankGrade
    String rank
    String grade
    String title

    String emailAddress
    String phoneNumber
    String firstName
    String lastName
    String middleInitial
    String displayName

    String fsOrganization               // freeStyle Organization field
    Organization assignedTD
    Account supervisor

    boolean emailValidated = false
    boolean accountValidated = false
    boolean markedForDeletion = false

    Date lastLoginDate
    Date lastExpirationDate
    int loginCount = 0

    Date createdDate
    Date lastChangeDate
    Account lastChange
    int accountEdit = 0

    Boolean notifyChanges = true
    Boolean notifyConferenceChanges = true
    Boolean canAttendConferences = true

// --------------------------------------------
// Attributes
// --------------------------------------------

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true, nullable: false
        firstName blank: true, nullable: true
        lastName blank: true, nullable: true
        middleInitial blank: true, nullable: true
		password blank: false, nullable: false
        title blank: true, nullable: true
        displayName blank: true, nullable: true

        emailAddress email: true, blank: false, unique: true, nullable: false
        emailValidated()
        accountValidated()

        fsOrganization blank: true, nullable: true
        assignedTD blank: true, nullable: true
        phoneNumber blank: true, nullable: true

        employeeType inList: ['Military', 'Civilian', 'Contractor', 'Other'], maxSize: 30
        rankGrade blank: true, nullable: true
        rank blank: true, nullable: true
        grade blank: true, nullable: true
        supervisor blank: true, nullable: true

        lastExpirationDate blank: true, nullable: true
        lastLoginDate blank: true, nullable: true
        loginCount blank: false, nullable: false

        createdDate blank: true, nullable: true
        lastChangeDate blank: true, nullable: true
        lastChange blank: true, nullable: true
        accountEdit blank: false, nullable: false

        notifyChanges blank: false, nullable: false
        notifyConferenceChanges blank: false, nullable: false
        canAttendConferences blank: false, nullable: false
	}

	static mapping = {
        version false

		password column: '`password`'
        sort "username"

        lastLoginDate type:'java.sql.Date'
        lastExpirationDate type:'java.sql.Date'
        createdDate type:'java.sql.Date'
        lastChangeDate type:'java.sql.Date'
	}

    @Override
    public String toString() {
        if (displayName) {
            return displayName
        } else {
            if (middleInitial) {
                return lastName + ", " + firstName + " " + middleInitial + " (" + emailAddress + ")"
            } else {
                return lastName + ", " + firstName + " (" + emailAddress + ")"
            }
        }
    }

// --------------------------------------------
// Accessors
// --------------------------------------------

	Set<Role> getAuthorities() {
		AccountRole.findAllByAccount(this).collect { it.role } as Set
	}

    public String fullName() {
        if (displayName) {
            return displayName
        } else {
            if (middleInitial) {
                return lastName + ", " + firstName + " " + middleInitial + " (" + username + ") [" + emailAddress + "]"
            } else {
                return lastName + ", " + firstName + " (" + username + ") [" + emailAddress + "]"
            }
        }
    }

    public String shortName() {
        if (middleInitial) {
            return lastName + ", " + firstName + " " + middleInitial
        } else {
            return lastName + ", " + firstName
        }
    }

    boolean hasAuthority(String pRole) {
        def list = AccountRole.findAllByAccount(this)
        return list.any { it?.role?.authority?.equalsIgnoreCase(pRole) }
    }

    Set<Account> getSupervisingList() {
        Account.findAllBySupervisor(this) as Set
    }

    Set<Conference> getConferencesCreated() {
        Conference.findAllByCreatedBy(this) as Set
    }

    Set<Attendee> getConferencesAttendee() {
        Attendee.findAllByAccountLink(this) as Set
    }

    Double getEstimates() {
        def list = Attendee.findAllByAccountLink(this)

        Double estimate = 0.0
        for (attendee in list) {
            if (attendee?.estimateTotal()) {
                estimate += attendee?.estimateTotal()
            }
        }

        return estimate
    }

    Double getActuals() {
        def list = Attendee.findAllByAccountLink(this)

        Double actual = 0.0
        for (attendee in list) {
            if (attendee?.actualTotal()) {
                actual += attendee?.actualTotal()
            }
        }

        return actual
    }

    Set<Conference> getConferencesCaoList() {
        Conference.findAllByConferenceAOOrAlternateCAO(this, this) as Set
    }

    Set<Conference> getTdPocList() {
        AccountContact.findAllByAccountLink(this) as Set
    }

    Set<DateEvent> getDatesList() {
        DateEvent.findAllByRecordedBy(this) as Set
    }

    Set<FileUpload> getFileList() {
        FileUpload.findAllByLoadedBy(this) as Set
    }
    
    public int getChanges() {
        int count = 0

        count += Account.findAllByLastChange(this).size()
        count += Conference.findAllByStatusChangedBy(this).size()
        
        return count
    }

// --------------------------------------------
// Security Related
// --------------------------------------------

    def beforeInsert() {
   		encodePassword()
   	}

   	def beforeUpdate() {
   		if (isDirty('password')) {
   			encodePassword()
   		}
   	}

   	protected void encodePassword() {
   		password = springSecurityService.encodePassword(password)
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

    public int getLoginDays() {
        return diffInDays(getLastLoginDate(), new Date())
    }

    public int getExpireDays() {
        return diffInDays(getLastExpirationDate(), new Date())
    }

    public int getChangeDays() {
        return diffInDays(getLastChangeDate(), new Date())
    }

// --------------------------------------------
// Role Related
// --------------------------------------------

    def List<RoleSelector> getManagedAuthorities() {
        def currentUser = Account.get(springSecurityService.principal?.id)

        Set<Role> currentUserRoles = AccountRole.findAllByAccount(currentUser).collect { it.role } as Set
        Set<Role> roles = AccountRole.findAllByAccount(this).collect { it.role } as Set

        List<Role> current = new ArrayList<Role>()
        List<Role> managedRoles = new ArrayList<Role>()
        List<RoleSelector> result = new ArrayList<RoleSelector>()

        // locate all managed roles for the current user logged in
        for (role in currentUserRoles) {
            def roleList = role.rolesManaged().split(',').collect {it as String}
            for (managedRole in roleList) {
                if (managedRole) {
                    Role roleManaged = Role.findByAuthority(managedRole.trim())
                    if (!managedRoles.contains(roleManaged)) {
                        managedRoles.add(roleManaged)
                    }
                }
            }
        }

        // add all additional roles that have been enabled into the list of current
        for (role in roles) {
            if (!current.contains(role)) {
                current.add(role)
            }
        }

        if (managedRoles.isEmpty()) {
            for (role in current) {
                result.add(new RoleSelector(roleName: role.authority, selected: true, radioButton: false, readOnly: true))
            }
        } else {
            for (role in managedRoles) {
                if (current.contains(role)) {
                    result.add(new RoleSelector(roleName: role.authority, selected: true, radioButton: false, readOnly: false))
                    current.remove(role)
                } else {
                    result.add(new RoleSelector(roleName: role.authority, selected: false, radioButton: false, readOnly: false))
                }
            }
            for (role in current) {
                result.add(new RoleSelector(roleName: role.authority, selected: true, radioButton: false, readOnly: true))
            }
        }

        return result
    }

}
