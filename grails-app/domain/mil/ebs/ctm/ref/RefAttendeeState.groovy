package mil.ebs.ctm.ref

class RefAttendeeState {

    String attendeeState
    String phaseState

    String attendeeAction
    String nextState
    String actionCommand
    String actionPermission
    String actionNotification
    String dateGateEvent


    static constraints = {
        attendeeState inList: ['Supervisor', 'TD Concurrence', 'Pending', 'Wait List', 'Withdrawn', 'Approved', 'Disapproved', 'Registered', 'Attended', 'Cancelled', 'Requesting', 'Confirmed'], maxSize: 30
        phaseState inList: ['Open', 'Closed', 'External', 'Create Package', 'TD Review', 'Resp TD', 'AFRL Review', 'AFMC Review', 'SAF Review', 'Approved', 'Finalizing'], maxSize: 30

        attendeeAction nullable: false, blank: false
        nextState nullable: false, blank: false
        actionCommand nullable: false, blank: false
        actionPermission nullable: false, blank: false

        actionNotification nullable: true, blank: true
        dateGateEvent nullable: true, blank: true
    }

    static mapping = {
        version false
    }

    String toString() {
        return attendeeState
    }

}
