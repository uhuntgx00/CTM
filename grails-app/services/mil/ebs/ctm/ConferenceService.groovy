package mil.ebs.ctm

import grails.transaction.Transactional
import mil.ebs.ctm.ref.RefDateGate
import mil.ebs.ctm.ref.RefPhase
import mil.ebs.ctm.ref.RefPhaseState

@Transactional
class ConferenceService {

//    static transactional = false

    def ctmMailService
    def notificationService
    def springSecurityService
    def attendeeService
    def organizationService
    def summaryService


    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return boolean - TRUE is CAO/Alternate CAO | FALSE is not CAO/Alternate CAO
     */
    public boolean isCAO(final Conference pConferenceInstance) {
        boolean check = false
        Account account = Account.get(springSecurityService?.principal?.id)

        if (account) {
            // is the account the Conference AO (CAO)?
            // is the account the Alternate CAO?
            if (account == pConferenceInstance?.conferenceAO || account == pConferenceInstance?.alternateCAO) {
                check = true
            }
        }

        return check
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return
     */
    def determineResponsibleTD(final Conference pConferenceInstance) {
        // exit the function if the Conference is not in the OPEN phaseState
        if (!pConferenceInstance?.phaseState?.equalsIgnoreCase("Open")) {
            return
        }

        // exit the function if the CAO/Alternate CAO has been selected
        if (pConferenceInstance?.conferenceAO || pConferenceInstance?.alternateCAO) {
            return
        }

        // create count list
        Map<String, Integer> count = new HashMap<>()

        // build list of counts
        for (organization in Organization.findAllByTrueTDAndLevelTD(true, "1").sort {it.id}) {
            count.put(organization?.officeSymbol, 0)
        }
        count.put("EXT", 0)
        count.put("OPEN", 0)

        print count

        // count attendee information
        for (attendee in pConferenceInstance?.attendees) {
            // SCR 8420 - Do not count Withdrawn or Disapproved when determining Responsible TD
            if (!attendee?.status?.equalsIgnoreCase("Withdrawn") && !attendee?.status?.equalsIgnoreCase("Disapproved")) {
                if (attendee?.reservedTD) {
                    if (attendee?.reservedTD?.trueTD) {
                        Integer value = count.get(attendee?.reservedTD?.topParent?.officeSymbol)
                        if (null != value && value >= 0) {
                            value++
                            count.remove(attendee?.reservedTD?.topParent?.officeSymbol)
                            count.put(attendee?.reservedTD?.topParent?.officeSymbol, value)
                        }
                    } else {
                        Integer value = count.get("EXT")
                        if (null != value && value >= 0) {
                            value++
                            count.remove("EXT")
                            count.put("EXT", value)
                        }
                    }
                } else if (attendee?.reservedOrg) {
                    Integer value = count.get("EXT")
                    if (null != value && value >= 0) {
                        value++
                        count.remove("EXT")
                        count.put("EXT", value)
                    }
                } else {
                    Integer value = count.get("OPEN")
                    if (null != value && value >= 0) {
                        value++
                        count.remove("OPEN")
                        count.put("OPEN", value)
                    }
                }
            }
        }

        // determine largest count
        Integer highest = 0
        String what = ""
        boolean tie = false
        for (key in count.keySet()) {
            if (count.get(key) > highest) {
                if (key.equalsIgnoreCase("OPEN")) {

                } else if (key.equalsIgnoreCase("EXT")) {

                } else {
                    highest = count.get(key)
                    what = key
                }
            }
        }

        if (what.equalsIgnoreCase("OPEN")) {
            // do nothing
        } else if (what.equalsIgnoreCase("EXT")) {
            // do nothing
//            pConferenceInstance.responsibleTD = null
//            pConferenceInstance.responsibleOrg = "EXT"
        } else {
            pConferenceInstance.responsibleTD = Organization.findByOfficeSymbol(what)
            if (!pConferenceInstance?.alternateRespTD) {
                pConferenceInstance.alternateRespTD = Organization.findByOfficeSymbol(what)
            }
            pConferenceInstance.responsibleOrg = ""
        }

    }

// ****************************************************************************************************************************************
// phase attributes for attendee options
// ****************************************************************************************************************************************

    private static final int CR_TRUE = -1
    private static final int CR_CHECK_FALSE = 0
    private static final int CR_CHECK_TRUE = 1

    /**
     *
     * @param pPermission (String) -
     * @param pAccount (Account) -
     * @param pConference (Conference) -
     * @return int - -1 (TRUE) no permission required | 0 (FALSE) permission check failed | 1 (TRUE) permission check success
     */
    public int checkResponsible(final String pPermission, final Account pAccount, final Conference pConference) {
        int check = CR_CHECK_FALSE

        if (pPermission?.contains("ROLE_RESPONSIBLE")) {
            // ADMIN account needs to match the conference responsible TD (parent)
            if (pAccount?.assignedTD?.topParent?.id == pConference?.responsibleTD?.topParent?.id) {
                check = CR_CHECK_TRUE
            }
        } else {
            check = CR_TRUE
        }

        return check
    }

    /**
     *
     * NOTE: TD_ADMIN needs to match the conference responsible TD -OR- attendee account link assigned TD
     *
     * @param pPermission (String) -
     * @param pAccount (Account) -
     * @param pAttendee (Attendee) -
     * @param pConference (Conference) -
     * @return
     */
    public boolean checkTdAdmin(final String pPermission, final Account pAccount, final Attendee pAttendee, final Conference pConference) {
        boolean check = false

        // does the PERMISSION list have the appropriate role required
        if (pPermission?.contains("ROLE_TD_ADMIN")) {
            // check if account has the appropriate AUTHORITY
            if (pAccount?.hasAuthority("ROLE_TD_ADMIN")) {
                int respCheck = checkResponsible(pPermission, pAccount, pConference)
                // check for valid ATTENDEE record
                if (pAttendee) {
                    // check to see if ATTENDEE is an internal account type
                    if (pAttendee?.accountType?.equalsIgnoreCase("Internal")) {
                        // check to see if ATTENDEE has an account link
                        if (pAttendee?.accountLink) {
                            // check to ensure that the account link is linked to an AFRL TD
                            if (pAttendee?.accountLink?.assignedTD?.trueTD) {
                                def orgList = organizationService.getOrgListById(pAccount?.assignedTD)

                                boolean check1 = (respCheck == CR_CHECK_TRUE)
                                boolean check2 = (orgList.contains(pAttendee?.accountLink?.assignedTD?.id) && (respCheck == CR_TRUE))

                                // NOTE: TD_ADMIN needs to match the conference responsible TD (parent) -AND/OR- attendee account link assigned TD (list)
                                check = ( check1 || check2 )
                            // if NON-AFRL TD account linked than check for responsible (if required)
                            } else {
                                check = (respCheck != CR_CHECK_FALSE)
                            }
                        // ATTENDEE does not have an account (OPEN or Reserved TD Account)
                        } else {
                            check = (respCheck != CR_CHECK_FALSE)
                        }
                    // ATTENDEE is not an internal account type - perform responsible TD check (EXTERNAL account)
                    } else {
                        check = (respCheck != CR_CHECK_FALSE)
                    }
                // ATTENDEE not valid
                } else {
                    check = (respCheck != CR_CHECK_FALSE)
                }
            }
        }

        return check
    }

    /**
     *
     * NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -OR- conference responsible TD (parent) -OR- attendee account link assigned TD (parent)
     *
     * @param pPermission (String) -
     * @param pAccount (Account) -
     * @param pAttendee (Attendee) -
     * @param pConference (Conference) -
     * @return
     */
    public boolean checkTdFullAdmin(final String pPermission, final Account pAccount, final Attendee pAttendee, final Conference pConference) {
        boolean check = false

        // does the PERMISSION list have the appropriate role required
        if (pPermission?.contains("ROLE_TD_FULL_ADMIN")) {
            // check if account has the appropriate AUTHORITY
            if (pAccount?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                int respCheck = checkResponsible(pPermission, pAccount, pConference)
                if (pAttendee) {
                    // check to see if ATTENDEE is an internal account type
                    if (pAttendee?.accountType?.equalsIgnoreCase("Internal")) {
                        // check to see if ATTENDEE has an account link (linked account)
                        if (pAttendee?.accountLink) {
                            // check to ensure that the account link is linked to an AFRL TD
                            if (pAttendee?.accountLink?.assignedTD?.trueTD) {
                                boolean check1 = (respCheck == CR_CHECK_TRUE)
                                boolean check2 = ((pAccount?.assignedTD?.topParent?.id == pAttendee?.accountLink?.assignedTD?.topParent?.id) && (respCheck == CR_TRUE))

                                // NOTE: TD_FULL_ADMIN needs to match the conference responsible TD (parent) -AND/OR- attendee account link assigned TD (parent)
                                check = ( check1 || check2 )
                            // if NON-AFRL TD account linked than check for responsible (if required)
                            } else {
                                check = (respCheck != CR_CHECK_FALSE)
                            }
                        // check to see if ATTENDEE has an reserved link (Reserved INTERNAL Account: TBD)
                        } else if (pAttendee?.reservedTD) {
                            // check to ensure that the account link is linked to an AFRL TD
                            if (pAttendee?.reservedTD?.trueTD) {
                                boolean check1 = (respCheck == CR_CHECK_TRUE)
                                boolean check2 = ((pAccount?.assignedTD?.topParent?.id == pAttendee?.reservedTD?.topParent?.id) && (respCheck == CR_TRUE))

                                // NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -AND/OR- conference responsible TD (parent)
                                check = ( check1 || check2 )
                            // if NON-AFRL TD account linked than check for responsible (if required)
                            } else {
                                check = (respCheck != CR_CHECK_FALSE)
                            }
                        // ATTENDEE does not have an account (OPEN INTERNAL account)
                        } else {
                            check = (respCheck != CR_CHECK_FALSE)
                        }
                    // ATTENDEE is not an internal account type - perform responsible TD check (EXTERNAL account)
                    } else {
                        // check to see if ATTENDEE has an reserved link (Reserved EXTERNAL Account)
                        if (pAttendee?.reservedTD) {
                            // check to ensure that the account link is linked to an AFRL TD
                            if (pAttendee?.reservedTD?.trueTD) {
                                boolean check1 = (respCheck == CR_CHECK_TRUE)
                                boolean check2 = ((pAccount?.assignedTD?.topParent?.id == pAttendee?.reservedTD?.topParent?.id) && (respCheck == CR_TRUE))

                                // NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -AND/OR- conference responsible TD (parent)
                                check = ( check1 || check2 )
                            // if NON-AFRL TD account linked than check for responsible (if required)
                            } else {
                                check = (respCheck != CR_CHECK_FALSE)
                            }
                        // ATTENDEE does not have an account (Open EXTERNAL account)
                        } else {
                            check = (respCheck != CR_CHECK_FALSE)
                        }
                    }
                // ATTENDEE not valid
                } else {
                    check = (respCheck != CR_CHECK_FALSE)
                }
            }
        }

        return check
    }

    /**
     *
     * @param pAction (String) - action to be attempted at the given phase ['delete', 'manage', 'add', 'edit']
     * @param pAttendeeId (Long) -
     * @param pConferenceInstance (Conference) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    public boolean canAttendee(final String pAction, final Long pAttendeeId, final Conference pConferenceInstance) {
        RefPhase phase = RefPhase.findByPhase(pConferenceInstance.phaseState)
        Attendee attendee = Attendee.get(pAttendeeId)
        Account account = Account.get(springSecurityService?.principal?.id)

        // ROLE_ADMIN has the right to perform any action on the data at any point
        if (account.hasAuthority("ROLE_ADMIN")) {
            return true
        }

        boolean check = false

        switch (pAction) {
            case 'delete':
                if (phase?.canDeleteAttendee) {
                    check = performCheck(phase, account, attendee, pConferenceInstance)
                }
                break

            case 'manage':
                if (phase?.canManageAttendee) {
                    check = performCheck(phase, account, attendee, pConferenceInstance)
                }
                break

            case 'add':
                if (phase?.canAddAttendee) {
                    check = performCheck(phase, account, attendee, pConferenceInstance)
                }
                break

            case 'view':
                check = performViewCheck(phase, account, attendee, pConferenceInstance)
                break

            case 'edit':
            default:
                if (phase?.canEditAttendee) {
                    check = performCheck(phase, account, attendee, pConferenceInstance)
                }
                break
        }

        return check
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    public boolean checkPermission(final Conference pConferenceInstance, final String pActionCommand) {
        RefPhaseState phaseState = RefPhaseState.findByPhaseStateAndActionCommand(pConferenceInstance.phaseState, pActionCommand)
        Account account = Account.get(springSecurityService?.principal?.id)

        // ROLE_ADMIN has the right to perform any action on the data at any point
        if (account.hasAuthority("ROLE_ADMIN")) {
            return true
        }

        boolean check = false

        // ROLE_TD_ADMIN have rights to edit based upon phase roles
        // NOTE: TD_ADMIN needs to match the attendee reserved TD -OR- conference responsible TD -OR- attendee account link assigned TD
        check = check || checkTdAdmin(phaseState?.actionPermission, account, null, pConferenceInstance)

        // ROLE_TD_FULL_ADMIN have rights to edit based upon phase roles
        // NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -OR- conference responsible TD (parent) -OR- attendee account link assigned TD (parent)
        check = check || checkTdFullAdmin(phaseState?.actionPermission, account, null, pConferenceInstance)

        // ROLE_FMC_ADMIN have rights to edit based upon phase roles
        if (phaseState?.actionPermission?.contains("ROLE_FMC_ADMIN")) {
            if (account?.hasAuthority("ROLE_FMC_ADMIN")) {
                check = true
            }
        }

        // ROLE_CAO grants the ability to edit within particular phases as defined
        if (phaseState?.actionPermission?.contains("ROLE_CAO")) {
            // is the account the Conference AO (CAO)?
            // is the account the Alternate CAO?
            if (account == pConferenceInstance?.conferenceAO
                || account == pConferenceInstance?.alternateCAO)
            {
                check = true
            }
        }

        // ROLE_USER | ROLE_AFRL_USER allows a user to edit their own record
        // or a record that has been defined as TBD (null) account link
        // NOTE: a user may only edit records that are still reserved to their TD
        if (phaseState?.actionPermission?.contains("ROLE_USER") || phaseState?.actionPermission?.contains("ROLE_AFRL_USER") || phaseState?.actionPermission?.contains("ROLE_NON_AFRL_USER")) {
            if (account?.hasAuthority("ROLE_USER") || account?.hasAuthority("ROLE_AFRL_USER") || account?.hasAuthority("ROLE_NON_AFRL_USER")) {
                // if the phaseAction is "attend" always return true
                if (phaseState?.checkPermission) {
                    if (pConferenceInstance?.createdBy?.id == account?.id) {
                        check = (checkResponsible(phaseState?.actionPermission, account, pConferenceInstance) != CR_CHECK_FALSE)
                    }
                } else {
                    check = true;
                }
            }
        }

        return check
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    public boolean canManage(final Conference pConferenceInstance) {
        RefPhase phase = RefPhase.findByPhase(pConferenceInstance.phaseState)
        Account account = Account.get(springSecurityService?.principal?.id)

        // ROLE_ADMIN has the right to perform any action on the data at any point
        if (account.hasAuthority("ROLE_ADMIN")) {
            return true
        }

        boolean check = false

        if (phase?.canManageAttendee) {
            check = performManageCheck(phase, account, pConferenceInstance)
        }

        return check
    }

    /**
     *
     * @param pPhase (RefPhase) -
     * @param pAttendee (Attendee) -
     * @param pConferenceInstance (Conference) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    private boolean performManageCheck(final RefPhase pPhase, final Account pAccount, final Conference pConferenceInstance) {
        boolean check = false

        // ROLE_TD_ADMIN have rights to edit based upon phase roles
        // NOTE: TD_ADMIN needs to match the conference responsible TD (parent)
        check = check || checkTdAdmin(pPhase?.roles, pAccount, null, pConferenceInstance)

        // ROLE_TD_FULL_ADMIN have rights to edit based upon phase roles
        // NOTE: TD_FULL_ADMIN needs to match the conference responsible TD (parent)
        check = check || checkTdFullAdmin(pPhase?.roles, pAccount, null, pConferenceInstance)

        // ROLE_FMC_ADMIN have rights to edit based upon phase roles
        if (pPhase?.roles?.contains("ROLE_FMC_ADMIN")) {
            if (pAccount?.hasAuthority("ROLE_FMC_ADMIN")) {
                check = true
            }
        }

        // ROLE_CAO grants the ability to edit within particular phases as defined
        if (pPhase?.roles?.contains("ROLE_CAO")) {
            // is the account the Conference AO (CAO)?
            // is the account the Alternate CAO?
            if (pAccount == pConferenceInstance?.conferenceAO || pAccount == pConferenceInstance?.alternateCAO) {
                check = true
            }
        }

        return check
    }

    /**
     *
     * @param pPhase (RefPhase) -
     * @param pAccount (Account) -
     * @param pAttendee (Attendee) -
     * @param pConferenceInstance (Conference) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    private boolean performCheck(final RefPhase pPhase, final Account pAccount, final Attendee pAttendee, final Conference pConferenceInstance) {
        boolean check = false

        // ROLE_TD_ADMIN have rights to edit based upon phase roles
        // NOTE: TD_ADMIN needs to match the attendee reserved TD -OR- conference responsible TD -OR- attendee account link assigned TD
        check = check || checkTdAdmin(pPhase?.roles, pAccount, pAttendee, pConferenceInstance)

        // ROLE_TD_FULL_ADMIN have rights to edit based upon phase roles
        // NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -OR- conference responsible TD (parent) -OR- attendee account link assigned TD (parent)
        check = check || checkTdFullAdmin(pPhase?.roles, pAccount, pAttendee, pConferenceInstance)

        // ROLE_FMC_ADMIN have rights to edit based upon phase roles
        if (pPhase?.roles?.contains("ROLE_FMC_ADMIN")) {
            if (pAccount?.hasAuthority("ROLE_FMC_ADMIN")) {
                check = true
            }
        }

        // ROLE_CAO grants the ability to edit within particular phases as defined
        if (pPhase?.roles?.contains("ROLE_CAO")) {
            // is the account the Conference AO (CAO)?
            // is the account the Alternate CAO?
            if (pAccount == pConferenceInstance?.conferenceAO || pAccount == pConferenceInstance?.alternateCAO) {
                check = true
            }
        }

        // ROLE_USER | ROLE_AFRL_USER allows a user to edit their own record
        // or a record that has been defined as TBD (null) account link
        // NOTE: a user may only edit records that are still reserved to their TD
        if (pPhase?.roles?.contains("ROLE_USER") || pPhase?.roles?.contains("ROLE_AFRL_USER") || pPhase?.roles?.contains("ROLE_NON_AFRL_USER")) {
            if (pAccount?.hasAuthority("ROLE_USER") || pAccount?.hasAuthority("ROLE_AFRL_USER") || pAccount?.hasAuthority("ROLE_NON_FAFRL_USER")) {
                // attendee account link is null
                if (!pAttendee?.accountLink) {
                    if (pAttendee?.createdBy?.id == pAccount?.id
                        && (!pAttendee?.reservedTD
                            || pAccount?.assignedTD?.topParent?.id == pAttendee?.reservedTD?.topParent?.id))
                    {
                        check = true
                    }
                    // if attendee slot is "open" than anyone can perform check
                    else if (!pAttendee?.reservedTD && !pAttendee?.name) {
                        check = true
                    }

                    // if attendee slot is "reserved TD" than anyone can view
                    if (pAttendee?.reservedTD?.topParent?.id == pAccount?.assignedTD?.topParent?.id) {
                        check = true
                    }
                } else {
                    // attendee account link MUST match the current account logged in
                    if (pAttendee?.accountLink?.id == pAccount?.id) {
                        check = true
                    }
                }
            }
        }

        return check
    }

    /**
     *
     * @param pPhase (RefPhase) -
     * @param pAccount (Account) -
     * @param pAttendee (Attendee) -
     * @param pConferenceInstance (Conference) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    private boolean performViewCheck(final RefPhase pPhase,final Account pAccount, final Attendee pAttendee, final Conference pConferenceInstance) {
        boolean check = false

        // ROLE_TD_ADMIN have rights to view
        // NOTE: TD_ADMIN needs to match the attendee reserved TD -OR- conference responsible TD -OR- attendee account link assigned TD
        check = check || checkTdAdmin(pPhase?.roles, pAccount, pAttendee, pConferenceInstance)

        // ROLE_TD_FULL_ADMIN have rights to view
        // NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -OR- conference responsible TD (parent) -OR- attendee account link assigned TD (parent)
        check = check || checkTdFullAdmin(pPhase?.roles, pAccount, pAttendee, pConferenceInstance)

        // ROLE_FMC_ADMIN have rights to view
        if (pAccount?.hasAuthority("ROLE_FMC_ADMIN")) {
            check = true
        }

        // ROLE_CAO grants the ability to view
        // is the account the Conference AO (CAO)?
        // is the account the Alternate CAO?
        if (pAccount == pConferenceInstance?.conferenceAO || pAccount == pConferenceInstance?.alternateCAO) {
            check = true
        }

        // ROLE_USER | ROLE_AFRL_USER allows a user to edit their own record
        // or a record that has been defined as TBD (null) account link
        // NOTE: a user may only edit records that are still reserved to their TD
        if (pAccount?.hasAuthority("ROLE_USER") || pAccount?.hasAuthority("ROLE_AFRL_USER")) {
            // attendee account link is null
            if (!pAttendee?.accountLink) {
//                if (pAttendee?.createdBy?.id == pAccount?.id && (!pAttendee?.reservedTD || pAccount?.assignedTD?.id == pAttendee?.reservedTD?.id)) {
//                    check = true
//                }
                // if attendee slot is "open" than anyone can view
                if (!pAttendee?.reservedTD && !pAttendee?.name) {
                    check = true
                }

                // if attendee slot is "reserved TD" than anyone can view
                if (pAttendee?.reservedTD?.topParent?.id == pAccount?.assignedTD?.topParent?.id) {
                    check = true
                }
            } else {
                // attendee account link MUST match the current account logged in
                if (pAttendee?.accountLink?.id == pAccount?.id) {
                    check = true
                }
            }
        }

        return check
    }

    /**
     * This function calculates who can add/edit 'ESTIMATE' values.
     *
     * @param pAttendeeId (Long) -
     * @param pConferenceInstance (Conference) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    public boolean canEstimate(final Long pAttendeeId, final Conference pConferenceInstance) {
        RefPhase phase = RefPhase.findByPhase(pConferenceInstance.phaseState)
        Attendee attendee = Attendee.get(pAttendeeId)
        Account account = Account.get(springSecurityService.principal.id)

        boolean check = false

        // ROLE_ADMIN has the right to estimate the data at any point
        if (account?.hasAuthority("ROLE_ADMIN")) {
            check = true
        }

        // ROLE_USER | ROLE_AFRL_USER allows a user to edit their own record
        // or a record that has been defined as TBD (null) account link
        // NOTE: a user may only edit records that are still reserved to their TD
        if (account?.hasAuthority("ROLE_USER") || account?.hasAuthority("ROLE_AFRL_USER")) {
            if (phase?.canUserEstimate) {
                // attendee account link is null
                if (!attendee?.accountLink) {
                    if (attendee?.createdBy?.id == account?.id && (!attendee?.reservedTD || account?.assignedTD?.topParent?.id == attendee?.reservedTD?.topParent?.id)) {
                        check = true
                    }
                    // if attendee slot is "open" than anyone can perform check
                    else if (!attendee?.reservedTD && !attendee?.name) {
                        check = true
                    }

                    // if attendee slot is "reserved TD" than anyone can view
                    if (attendee?.reservedTD?.topParent?.id == account?.assignedTD?.topParent?.id) {
                        check = true
                    }
                } else {
                    // attendee account link MUST match the current account logged in
                    if (attendee?.accountLink?.id == account?.id) {
                        check = true
                    }
                }
            }
        }

        // ROLE_CAO grants the ability to perform estimate entries within defined phases
        if (phase?.canCaoEstimate) {
            // is the account the Conference AO (CAO)?
            // is the account the Alternate CAO?
            if (account?.id == pConferenceInstance?.conferenceAO?.id || account?.id == pConferenceInstance?.alternateCAO?.id) {
                check = true
            }
        }

        // ROLE_TD_ADMIN has rights to perform estimate entries within defined phases
        // NOTE: TD_ADMIN needs to match the attendee reserved TD -OR- conference responsible TD -OR- attendee account link assigned TD
        if (phase?.canTdEstimate) {
            check = check || checkTdAdmin(phase?.roles, account, attendee, pConferenceInstance)
        }

        // ROLE_TD_FULL_ADMIN has rights to perform estimate entries within defined phases
        // NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -OR- conference responsible TD (parent) -OR- attendee account link assigned TD (parent)
        if (phase?.canTdEstimate) {
            check = check || checkTdFullAdmin(phase?.roles, account, attendee, pConferenceInstance)
        }

        // ROLE_FMC_ADMIN has rights to perform estimate entries within defined phases
        if (phase?.canFmcEstimate) {
            if (account?.hasAuthority("ROLE_FMC_ADMIN")) {
                check = true
            }
        }

        return check
    }

    /**
     * This function calculates who can add/edit 'ACTUAL' values.
     *
     * @param pAttendeeId (Long) -
     * @param pConferenceInstance (Conference) -
     * @return boolean - TRUE if action is possible at given phase | FALSE if action is not possible
     */
    public boolean canActual(final Long pAttendeeId, final Conference pConferenceInstance) {
        RefPhase phase = RefPhase.findByPhase(pConferenceInstance.phaseState)
        Attendee attendee = Attendee.get(pAttendeeId)
        Account account = Account.get(springSecurityService.principal.id)

        boolean check = false

        // ROLE_ADMIN has the right to actual the data at any point
        if (account?.hasAuthority("ROLE_ADMIN")) {
            check = true
        }

        // ROLE_USER | ROLE_AFRL_USER allows a user to edit their own record
        // or a record that has been defined as TBD (null) account link
        // NOTE: a user may only edit records that are still reserved to their TD
        if (account?.hasAuthority("ROLE_USER") || account?.hasAuthority("ROLE_AFRL_USER")) {
            if (phase?.canUserActual) {
                // attendee account link is null
                if (!attendee?.accountLink) {
                    if (attendee?.createdBy?.id == account?.id && (!attendee?.reservedTD || account?.assignedTD?.id == attendee?.reservedTD?.id)) {
                        check = true
                    }
                } else {
                    // attendee account link MUST match the current account logged in
                    if (attendee?.accountLink?.id == account?.id) {
                        check = true
                    }
                }
            }
        }

        // ROLE_CAO grants the ability to perform actual entries within defined phases
        if (phase?.canCaoActual) {
            // is the account the Conference AO (CAO)?
            // is the account the Alternate CAO?
            if (account?.id == pConferenceInstance?.conferenceAO?.id || account?.id == pConferenceInstance?.alternateCAO?.id) {
                check = true
            }
        }

        // ROLE_TD_ADMIN has rights to perform actual entries within defined phases
        // NOTE: TD_ADMIN needs to match the attendee reserved TD -OR- conference responsible TD -OR- attendee account link assigned TD
        if (phase?.canTdActual) {
            check = check || checkTdAdmin(phase?.roles, account, attendee, pConferenceInstance)
        }

        // ROLE_TD_FULL_ADMIN has rights to perform actual entries within defined phases
        // NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -OR- conference responsible TD (parent) -OR- attendee account link assigned TD (parent)
        if (phase?.canTdActual) {
            check = check || checkTdFullAdmin(phase?.roles, account, attendee, pConferenceInstance)
        }

        // ROLE_FMC_ADMIN has rights to perform actual entries within defined phases
        if (phase?.canFmcActual) {
            if (account.hasAuthority("ROLE_FMC_ADMIN")) {
                check = true
            }
        }

        return check
    }

// ****************************************************************************************************************************************
// main update function for phase state changes
// ****************************************************************************************************************************************

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @param pPhaseState (RefPhaseState) -
     */
    def void updateConferenceDataViaPhaseState(final Conference pConferenceInstance, final RefPhaseState pPhaseState) {
        def account = Account.get(springSecurityService.principal.id)

        pConferenceInstance?.status = pPhaseState?.actionStatus ? pPhaseState?.actionStatus : "*ERROR*"
        pConferenceInstance?.phaseState = pPhaseState?.nextPhaseState ? pPhaseState?.nextPhaseState : "*ERROR*"
        pConferenceInstance?.statusChangeDate = new Date()
        pConferenceInstance?.statusChangedBy = account

        // special condition set the STEP for the AFRL Review phase...
        if (pConferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")) {
            pConferenceInstance?.step = "AFRL/FMC"
        }

        // save the updated/changed conference information
        pConferenceInstance?.save flush: true

        // does the phase state have a valid date gate event to process?
        if (pPhaseState?.dateGateEvent) {
            new DateEvent(dateGate: RefDateGate.findByCode(pPhaseState.dateGateEvent), eventDate: new Date(), recordedBy: account, conference: pConferenceInstance).save(flush: true)
        }

        if (!pPhaseState?.phaseAction?.equalsIgnoreCase("Approve")) {
            // does the phase state have a notification event to process?
            if (pPhaseState?.actionNotification) {
                notificationService.notify(pPhaseState?.actionNotification, pConferenceInstance, account)
            }
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     */
    def void approvePhaseState(final Conference pConferenceInstance) {
        notificationService.notify("notifyApproval", pConferenceInstance, Account.get(springSecurityService.principal.id))
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     */
    def void checkConferenceData(final Conference pConferenceInstance) {
        if (pConferenceInstance.isDirty('conferenceAO') || pConferenceInstance.isDirty('alternateCAO')) {
            notificationService.change('changeCAO', pConferenceInstance, Account.get(springSecurityService.principal.id))
        }

        if (pConferenceInstance.isDirty('numAttendees')) {
            attendeeService.determinePriorityList(pConferenceInstance)
        }

    }

    /**
     *
     * @param pForceRecalc (boolean) -
     * @param pConferenceInstance (Conference) -
     * @return ConferenceStatusBlock -
     */
    public ConferenceStatusBlock determineConferenceStatusBlock(final boolean pForceRecalc, final Conference pConferenceInstance) {
        if (pForceRecalc) {
            ConferenceStatusBlock csb = new ConferenceStatusBlock()

            if (pConferenceInstance?.attendees) {
                csb.attendeeCount = pConferenceInstance?.attendees?.size()
            } else {
                csb.attendeeCount = 0
            }

            csb.numAttendees = pConferenceInstance?.numAttendees

            if (pConferenceInstance?.comments) {
                csb.commentCount = pConferenceInstance?.comments?.size()
            } else {
                csb.commentCount = 0
            }

            if (pConferenceInstance?.getDateEvents()) {
                csb.datesCount = pConferenceInstance?.getDateEvents()?.size()
            } else {
                csb.datesCount = 0
            }

            if (pConferenceInstance?.getFiles()) {
                csb.fileCount = pConferenceInstance?.getFiles()?.size()
            } else {
                csb.fileCount = 0
            }

            csb.days = pConferenceInstance?.days()

            computeConferenceStatusBlock(csb, pConferenceInstance)

            return csb
        } else {
            return summaryService.convertToCSB("Current", pConferenceInstance)
        }
    }

    /**
     *
     * @param pCsb (ConferenceStatusBlock) -
     * @param pConference (Conference) -
     */
    public static void computeConferenceStatusBlock(final ConferenceStatusBlock pCsb, final Conference pConference) {
        Set<String> tdList = new HashSet<String>()
        Map<String, Long> tdCount = new HashMap<String, Long>()
        Map<String, Double> tdTotal = new HashMap<String, Double>()

        double constrainedTotal = 0.0
        double unconstrainedTotal = 0.0
        double otherEstimateTotal = 0.0
        double constrainedActualTotal = 0.0
        double contractorActualTotal = 0.0

        int constrainedCount = 0
        int unconstrainedCount = 0
        int otherCount = 0
        int constrainedActualCount = 0
        int contractorActualCount = 0

        for (attendee in pConference?.attendees) {
            double estimateTotal = attendee?.estimateTotal()
            Double actualTotal = attendee?.actualTotal()
            int afFunding = attendee?.afFunding()
            int otherFunding = attendee?.otherFunding()

            // the total estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Wait List") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                unconstrainedTotal += (estimateTotal * afFunding) / 100
                unconstrainedCount ++

                if (otherFunding) {
                    otherEstimateTotal += (estimateTotal * otherFunding) / 100
                    otherCount ++
                }
            }

            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // do not include CONTRACTOR amounts in CONSTRAINED total
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    constrainedTotal += 0
                } else {
                    constrainedTotal += (estimateTotal * afFunding) / 100
                }
                constrainedCount ++
            }

//            // the total VALID estimate cost does not include
//            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor'
//            if (attendee?.status?.equalsIgnoreCase("Pending") ||
//                    attendee?.status?.equalsIgnoreCase("Wait List") ||
//                    attendee?.status?.equalsIgnoreCase("Approved") ||
//                    attendee?.status?.equalsIgnoreCase("Registered") ||
//                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
//                    attendee?.status?.equalsIgnoreCase("Attended"))
//            {
//                if (otherFunding) {
//                    otherEstimateTotal += (estimateTotal * otherFunding) / 100
//                    otherCount ++
//                }
//            }

            // the total VALID estimate cost does not include
            // attendee that have registered/confirmed or attended
            if (attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // remove contractor $ from constrained total
                if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    constrainedActualTotal += actualTotal
                    constrainedActualCount++
                }

                // count only contractor total
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    contractorActualTotal += actualTotal
                    contractorActualCount++
                }
            }

//            // the total VALID estimate cost does not include
//            // attendee that have registered/confirmed or attended
//            if (attendee?.status?.equalsIgnoreCase("Registered") ||
//                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
//                    attendee?.status?.equalsIgnoreCase("Attended"))
//            {
//                // count only contractor total
//                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase('Contractor') || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
//                    contractorActualTotal += actualTotal
//                    contractorActualCount++
//                }
//            }

            // the total VALID estimate cost does not include
            // attendee that have 'Withdrawn', 'Disapproved', 'Requesting', 'Supervisor', 'Wait List'
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                // do not include CONTRACTOR amounts in CONSTRAINED total
                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    // do nothing
                } else {
                    Double attendeeValue = (estimateTotal * afFunding) / 100

                    String key = "EXT"
                    if (attendee?.accountLink) {
                        if (attendee?.accountLink?.assignedTD?.getTopParent()?.officeSymbol) {
                            key = attendee?.accountLink?.assignedTD?.getTopParent()?.officeSymbol
                        } else {
                            key = "UNK"
                        }
                    } else if (attendee?.reservedTD) {
                        if (attendee?.reservedTD?.getTopParent()?.officeSymbol) {
                            key = attendee?.reservedTD?.getTopParent()?.officeSymbol
                        } else {
                            key = "UNK"
                        }
                    }

                    tdList.add(key)

                    if (tdCount?.containsKey(key)) {
                        Long value = tdCount?.get(key)
                        tdCount?.remove(key)
                        tdCount?.put(key, value + 1)
                    } else {
                        tdCount?.put(key, 1)
                    }

                    if (tdTotal?.containsKey(key)) {
                        Double value = tdTotal?.get(key)
                        tdTotal?.remove(key)
                        tdTotal?.put(key, value + attendeeValue)
                    } else {
                        tdTotal?.put(key, attendeeValue)
                    }
                }
            }

        }

        pCsb.setConstrainedTotal(constrainedTotal)
        pCsb.setUnconstrainedTotal(unconstrainedTotal)
        pCsb.setOtherEstimateTotal(otherEstimateTotal)
        pCsb.setConstrainedActualTotal(constrainedActualTotal)
        pCsb.setContractorActualTotal(contractorActualTotal)

        pCsb.setConstrainedCount(constrainedCount)
        pCsb.setUnconstrainedCount(unconstrainedCount)
        pCsb.setOtherCount(otherCount)
        pCsb.setConstrainedActualCount(constrainedActualCount)
        pCsb.setContractorActualCount(contractorActualCount)

        pCsb.tdList.addAll(tdList)
        pCsb.tdCount.putAll(tdCount)
        pCsb.tdTotal.putAll(tdTotal)
    }


    public void checkApprovedToFinalize() {
        Date checkDate = new Date()

        List<Conference> conferenceList = Conference.findAllByPhaseState('Approved')

        for (conference in conferenceList) {
            if ((conference.endDate + 2) <= checkDate) {
                conference?.phaseState = "Finalizing"
                conference?.statusChangeDate = new Date()
                conference?.save(flush: true)

                notificationService.notify("notifyFinalize", conference, null)
            }
        }
    }

}
