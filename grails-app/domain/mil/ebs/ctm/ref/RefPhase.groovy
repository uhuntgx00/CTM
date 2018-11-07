package mil.ebs.ctm.ref

class RefPhase {

    String phase
    String roles

    boolean canUserEstimate = false
    boolean canUserActual = false

    boolean canCaoEstimate = false
    boolean canCaoActual = false

    boolean canTdEstimate = false
    boolean canTdActual = false

    boolean canFmcEstimate = false
    boolean canFmcActual = false

    boolean canAddAttendee = false
    boolean canEditAttendee = false
    boolean canDeleteAttendee = false
    boolean canManageAttendee = false


    static constraints = {
        phase inList: ['Open', 'Closed', 'External', 'Create Package', 'TD Review', 'Resp TD', 'AFRL Review', 'AFMC Review', 'SAF Review', 'Approved', 'Finalizing', '*ERROR*']
        roles nullable: true, blank: true

        canUserEstimate nullable: false, blank: false
        canUserActual nullable: false, blank: false

        canCaoEstimate nullable: false, blank: false
        canCaoActual nullable: false, blank: false

        canTdEstimate nullable: false, blank: false
        canTdActual nullable: false, blank: false

        canFmcEstimate nullable: false, blank: false
        canFmcActual nullable: false, blank: false

        canAddAttendee nullable: false, blank: false
        canEditAttendee nullable: false, blank: false
        canDeleteAttendee nullable: false, blank: false
        canManageAttendee nullable: false, blank: false
    }

    static mapping = {
        version false
    }

    String toString() {
        return phase
    }

}
