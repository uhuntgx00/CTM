package mil.ebs.ctm

import grails.transaction.Transactional

@Transactional
class PhaseStateService {

    def springSecurityService


    /**
     *
     * @param pConference (Conference) -
     * @param pActionCheck (String) -
     * @return (boolean) - TRUE if phase action check is valid | FALSE if phase action check is invalid
     */
    public checkAction(final Conference pConference, final String pActionCheck) {
        // if the phaseAction is '' or empty or null return TRUE
        if (!pActionCheck) {
            return true
        }

        switch (pActionCheck) {
            case 'dateCheck':
                def account = Account.get(springSecurityService.principal.id)

                boolean check = true

                // date check on conference
                if ((pConference.startDate - new Date()) < 150) {
                    check = false
                }

                // ROLE_FMC_ADMIN and ROLE_TD_ADMIN/ROLE_TD_FULL_ADMIN allow override of date check against conference attendance
                if (account?.hasAuthority("ROLE_FMC_ADMIN") || account?.hasAuthority("ROLE_TD_ADMIN") || account?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                    check = true
                }

                return check
                break

            case 'isNotAttending':
                def account = Account.get(springSecurityService.principal.id)

                boolean check = true
                for (attendee in pConference?.attendees) {
                    if (attendee?.accountLink?.id == account?.id) {
                        check = false
                    }
                }

                // Accounts checked for eligible attendance to conferences
                // Users must have the ROLE_AFRL_USER to attend a conference
//                if (!account?.canAttendConferences || !account?.hasAuthority("ROLE_AFRL_USER")) {
                if (!account?.canAttendConferences) {
                    check = false
                }

                // date check on conference
//                if ((pConference.startDate - new Date()) < 150) {
//                    check = false
//                }

                return check
                break

            case 'hasCAO':
                return pConference?.conferenceAO || pConference?.alternateCAO
                break

            case 'hasExtOrg':
                return pConference?.responsibleOrg
                break

            case 'hasEstimates':
                break

            case 'hasActuals':
                int attendeeCount = 0
                int actualCount = 0

                for (attendee in pConference?.attendees) {
                    if (attendee?.status?.equalsIgnoreCase("Confirmed") || attendee?.status?.equalsIgnoreCase("Attended")) {
                        attendeeCount++
                    }

                    if (attendee?.hasActual()) {
                        actualCount++
                    }
                }

                return (attendeeCount <= actualCount)
                break

            case 'hasSoccer':
                return pConference?.afrlSoccer
                break

            case 'hasAuthority':
                return pConference?.constrainedTotal() < 20000.01 && pConference?.afrlSoccer
                break

            case 'hasSAFmemo':
                return pConference?.hasDate('DSMU')
                break

            case 'hasPackage':
                return pConference?.hasDate('DCPU')
                break

            case 'hasVenueAddress':
                return !pConference?.address
                break;

            default:
                return true
        }
    }

    /**
     *
     * @param pActionCheck (String) -
     * @return (String) - message to be displayed if action check failed
     */
    public String checkActionMessage(final String pActionCheck) {
        if (!pActionCheck) {
            return ""
        }

        switch (pActionCheck) {
            case 'dateCheck':
                return "Conference is within the 5 month minimum attendance window."
                break;

            case 'isNotAttending':
                return "Attendee is already attending conference."
                break;

            case 'hasCAO':
                return "Conference MUST have a CAO or Alternate AO selected before being routed to 'Responsible TD'."
                break

            case 'hasExtOrg':
                return "Conference MUST have a Responsible Organization defined before being routed to 'External'."
                break

            case 'hasEstimates':
                break

            case 'hasActuals':
                break

            case 'hasSoccer':
                return 'Conference MUST have SOCCER information entered.'
                break

            case 'hasAuthority':
                return 'Conference must be less than $20k and have valid SOCCER information entered.'
                break

            case 'hasSAFmemo':
                return "Conference MUST have a Approval Memo attached before it can be 'Approved'."
                break

            case 'hasPackage':
                return "Conference MUST have a 'Package' uploaded before being forwarded to AFRL FMC for review."
                break

            case 'hasVenueAddress':
                return "Conference may only have ONE venue address stored."
                break;

            default:
                return ""
        }
    }

}



