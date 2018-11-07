package mil.ebs.ctm

class AccountContact {

    Account accountLink
    Boolean primaryPOC = false

    static belongsTo = [tdLink: Organization]

    static constraints = {
        accountLink blank: false, nullable: false
        tdLink blank: false, nullable: false
        primaryPOC blank: false, nullable: false
    }

    static mapping = {
        version false

        sort: 'id'
    }

    @Override
    public String toString() {
        return accountLink?.lastName + ", " + accountLink?.firstName + " (" + accountLink?.emailAddress + ")";
    }

}
