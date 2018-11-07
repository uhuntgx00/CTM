package mil.ebs.ctm

class Role {

	String authority
    String managedRoles

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true, nullable: false
        managedRoles blank: true, nullable: true
	}

    Set<Account> getAccounts() {
   		AccountRole.findAllByRole(this).collect { it.account } as Set
   	}

    @Override
    public String toString() {
        return authority;
    }

    String rolesManaged() {
        if (managedRoles) {
            return managedRoles
        } else {
            return ""
        }
    }

}
