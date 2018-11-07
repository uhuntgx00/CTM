package mil.ebs.ctm.ref

class RefPhaseState {

    String phaseState
    String nextPhaseState
    String phaseAction

    String dateGateEvent
    String actionStatus
    String actionController
    String actionCommand
    String actionPermission
    String actionCheck
    String actionNotification
    String buttonClass
    boolean displayDisabled = false
    boolean checkPermission = true


    static constraints = {
        phaseState inList: ['Open', 'Closed', 'External', 'Create Package', 'TD Review', 'Resp TD', 'AFRL Review', 'AFMC Review', 'SAF Review', 'Approved', 'Finalizing', '*ERROR*'], maxSize: 30
        nextPhaseState inList: ['Open', 'Closed', 'External', 'Create Package', 'TD Review', 'Resp TD', 'AFRL Review', 'AFMC Review', 'SAF Review', 'Approved', 'Finalizing', '*ERROR*'], maxSize: 30

        phaseAction nullable: false, blank: false
        actionStatus nullable: false, blank: false
        actionCommand nullable: false, blank: false
        actionController nullable: true, blank: true
        actionPermission nullable: false, blank: false

        actionCheck inList: ['', 'dateCheck', 'isNotAttending', 'hasCAO', 'hasExtOrg', 'hasEstimates', 'hasActuals', 'hasSoccer', 'hasAuthority', 'hasSAFmemo', 'hasPackage', 'hasVenueAddress'], nullable: true, blank: true, maxSize: 30
        actionNotification nullable: true, blank: true
        dateGateEvent nullable: true, blank: true

        buttonClass nullable: true, blank: true
        displayDisabled nullable: false, blank: false
        checkPermission nullable: false, blank: false
    }

    static mapping = {
        version false
    }

    String toString() {
        return phaseState
    }

}
