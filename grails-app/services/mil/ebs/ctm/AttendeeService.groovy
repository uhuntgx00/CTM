package mil.ebs.ctm

import grails.transaction.Transactional

@Transactional
class AttendeeService {

    def springSecurityService
    def organizationService
    def notificationService
    def conferenceService


    /**
     *
     * @param pConferenceInstance (Conference) -
     */
    @Transactional
    def determinePriorityList(final Conference pConferenceInstance) {
        if (pConferenceInstance?.numAttendees) {

            int count = 0

            for (attendee in pConferenceInstance.attendees.sort{ it.sequence }) {
                if (pConferenceInstance?.phaseState?.equalsIgnoreCase("Open")
                        && (attendee?.status?.equalsIgnoreCase("TD Concurrence") || attendee?.status?.equalsIgnoreCase("Supervisor")))
                {
                    count++
                }

                if (attendee?.status?.equalsIgnoreCase("Pending") || attendee?.status?.equalsIgnoreCase("Wait List")) {
//                    attendee.status = attendee.sequence <= pConferenceInstance?.numAttendees ? "Pending" : "Wait List"
                    if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor")) {
                        attendee.status = ++count <= pConferenceInstance?.numAttendees ? "Pending" : "Wait List"
                        attendee.save flush: true
                    }
                }

                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor")
                        && (attendee?.status?.equalsIgnoreCase("Pending") || attendee?.status?.equalsIgnoreCase("Wait List")))
                {
                    attendee.status = "Pending"
                    attendee.save flush: true
                }
            }
        } else {
            for (attendee in pConferenceInstance.attendees) {
                if (attendee?.status?.equalsIgnoreCase("Pending") || attendee?.status?.equalsIgnoreCase("Wait List")) {
                    attendee.status = "Pending"
                    attendee.save flush: true
                }
            }
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @return Attendee -
     */
    public Attendee create(final Conference pConferenceInstance) {
        return create(pConferenceInstance, true, true)
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     * @param pAccountLink (boolean) -
     * @param pReserveTd (boolean) -
     * @return Attendee -
     */
    public Attendee create(final Conference pConferenceInstance, final boolean pAccountLink, final boolean pReserveTD) {
        Attendee attendee = new Attendee()

        // set the default attendance type to "Attendee"
        attendee.attendanceType = "Attendee"
        attendee.createdDate = new Date()

        if (pConferenceInstance) {
            // set the conference information
            attendee.conference = pConferenceInstance
            attendee.startTravelDate = pConferenceInstance?.startDate - 1
            attendee.endTravelDate = pConferenceInstance?.endDate + 1
            attendee.hoursAttendanceType = pConferenceInstance?.days() * 8

            attendee.status = "Pending"
            if (pConferenceInstance?.numAttendees && pConferenceInstance?.attendees) {
                def available = pConferenceInstance?.numAttendees - pConferenceInstance?.attendees?.size()
                if (available <= 0) {
                    attendee.status = "Wait List"
                }
            }

            // if the size is 0 (zero) than start with 1
            // if the size is > 0 than add 1 to the current size
            attendee.sequence = pConferenceInstance?.attendees?.size() ? pConferenceInstance?.attendees?.size() + 1 : 1
        }

        def account = Account.get(springSecurityService?.principal?.id)
        if (account) {
            // set if pAccountLink is TRUE
            attendee.accountLink = pAccountLink ? account : null
            attendee.supervisor = pAccountLink ? account?.supervisor : null
            attendee.rankGrade = pAccountLink ? account?.rankGrade : null

            // set if pReserveTD is TRUE
            attendee.reservedTD = pReserveTD ? account?.assignedTD?.topParent : null

            // always set who creates the attendee record by the current account
            attendee.createdBy = account

            // default the behaviour of the notify functionality to the current account
            attendee.notifyChanges = account?.notifyChanges
            attendee.notifyConferenceChanges = account?.notifyConferenceChanges

            // check TD attendance rules
            if (account?.assignedTD?.topParent?.attendeeRequestRequired && pReserveTD) {
                if (account.hasAuthority("ROLE_TD_ADMIN") || account.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                    // do nothing - TD Admin creating slot so should be approved already
                } else {
                    attendee.status = "TD Concurrence"
                    notificationService.request('requestTdConcurrence', attendee?.conference, Account.get(springSecurityService.principal.id), attendee?.accountLink)
                }
            }
            if (account?.assignedTD?.topParent?.supervisorApprovalRequired && pAccountLink) {
                if (account?.supervisor) {
                    attendee.status = "Supervisor"
                    notificationService.request('requestSupervisor', attendee?.conference, Account.get(springSecurityService.principal.id), attendee?.accountLink)
                } else {
                    if (account.hasAuthority("ROLE_TD_ADMIN") || account.hasAuthority("ROLE_TD_FULL_ADMIN")) {
                        // do nothing - TD Admin creating slot so should be approved already
                    } else {
                        attendee.status = "TD Concurrence"
                        notificationService.request('requestTdConcurrence', attendee?.conference, Account.get(springSecurityService.principal.id), attendee?.accountLink)
                    }
                }
            }
        }

        return attendee
    }

    public List<Integer> computeAttendeeType(List<Attendee> pAttendeeList) {
        List<Integer> countList = new ArrayList<Integer>()

        int attendeeCount = 0
        int boothCount = 0
        int panelCount = 0
        int chairCount = 0
        int speakerCount = 0
        int supportCount = 0
        int otherCount = 0

        for (attendee in pAttendeeList) {
            switch (attendee?.attendanceType) {
                case 'Attendee':
                    attendeeCount++
                    break

                case 'Booth/Display':
                    boothCount++
                    break

                case 'Discussion Panel':
                    panelCount++
                    break

                case 'Session Chair':
                    chairCount++
                    break

                case 'Presenter/Speaker':
                    speakerCount++
                    break

                case 'Support':
                    supportCount++
                    break

                case 'Other':
                    otherCount++
                    break
            }
        }

        countList.add(attendeeCount)
        countList.add(boothCount)
        countList.add(panelCount)
        countList.add(chairCount)
        countList.add(speakerCount)
        countList.add(supportCount)
        countList.add(otherCount)

        return countList
    }

    /**
     *
     * @param pPermission (String) -
     * @param pAccount (Account) -
     * @param pConference (Conference) -
     * @return boolean - TRUE if check is valid | FALSE if check is invalid
     */
    public boolean checkResponsible(final String pPermission, final Account pAccount, final Conference pConference) {
        boolean check = false

        if (pPermission?.contains("ROLE_RESPONSIBLE")) {
            // ADMIN account needs to match the conference responsible TD (parent)
            if (pAccount?.assignedTD?.topParent?.id == pConference?.responsibleTD?.topParent?.id) {
                check = true
            }
        } else {
            check = true
        }

        return check
    }

//    /**
//     *
//     * NOTE: TD_ADMIN needs to match the conference responsible TD -OR- attendee account link assigned TD
//     *
//     * @param pPermission (String) -
//     * @param pAccount (Account) -
//     * @param pAttendee (Attendee) -
//     * @param pConference (Conference) -
//     * @param pAuthority (String) -
//     * @param pReserved (boolean) -
//     * @return
//     */
//    public boolean checkTdAdmin(final String pPermission, final Account pAccount, final Attendee pAttendee, final Conference pConference) {
//        boolean check = false
//
//        // check if account has the appropriate AUTHORITY
//        if (pPermission?.contains("ROLE_TD_ADMIN")) {
//            def orgList = organizationService.getOrgListById(pAccount?.assignedTD)
//
//            // NOTE: TD_ADMIN needs to match the conference responsible TD (parent) -AND/OR- attendee account link assigned TD (list)
//            if (pAccount?.hasAuthority("ROLE_TD_ADMIN")
//                    && orgList.contains(pAttendee?.accountLink?.assignedTD))
//            {
//                check = checkResponsible(pPermission, pAccount, pConference)
//            }
//        }
//
//        return check
//    }

//    /**
//     *
//     * NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -OR- conference responsible TD (parent) -OR- attendee account link assigned TD (parent)
//     *
//     * @param pPermission (String) -
//     * @param pAccount (Account) -
//     * @param pAttendee (Attendee) -
//     * @param pConference (Conference) -
//     * @param pAuthority (String) -
//     * @param pReserved (boolean) -
//     * @return
//     */
//    public boolean checkTdFullAdmin(final String pPermission, final Account pAccount, final Attendee pAttendee, final Conference pConference) {
//        boolean check = false
//
//        // check if account has the appropriate AUTHORITY
//        if (pPermission?.contains("ROLE_TD_FULL_ADMIN")) {
//            // NOTE: TD_FULL_ADMIN needs to match the attendee reserved TD (parent) -AND/OR- conference responsible TD (parent) -AND/OR- attendee account link assigned TD (parent)
//            if (pAccount?.hasAuthority("ROLE_TD_FULL_ADMIN")) {
//                if (pAccount?.assignedTD?.topParent?.id == pAttendee?.reservedTD?.topParent?.id
//                    || pAccount?.assignedTD?.topParent?.id == pAttendee?.accountLink?.assignedTD?.topParent?.id)
//                {
//                    check = checkResponsible(pPermission, pAccount, pConference)
//                }
//            }
//        }
//
//        return check
//    }

    public Boolean checkPermission(final String pAction, final String pPermission, final Attendee pAttendee) {
        Boolean check = false

        def account = Account.get(springSecurityService?.principal?.id)

        Conference conference = pAttendee?.conference

        switch (pAction) {
            case 'Approve':
                // current account must be logged in
                if (account) {
                    // check for approval against "supervisor"
                    if (pAttendee?.status?.equalsIgnoreCase("Supervisor")) {
                        if (pPermission?.contains("ROLE_USER") || pPermission?.contains("ROLE_AFRL_USER")) {
                            // ROLE_USER | ROLE_AFRL_USER allows a user to WITHDRAW their own record
                            // attendee account link must be enabled
                            if (pAttendee?.supervisor) {
                                // attendee account link MUST match the current account logged in
                                if (pAttendee?.supervisor?.id == account?.id) {
                                    check = true
                                }
                            }
                            if (pAttendee?.accountLink) {
                                // attendee account link MUST match the current account logged in
                                if (pAttendee?.accountLink?.supervisor?.id == account?.id) {
                                    check = true
                                }
                            }
                        }
                    }
                    // check for approval against "td concurrence"
                    else if (pAttendee?.status?.equalsIgnoreCase("TD Concurrence")) {
                        // ROLE_TD_ADMIN | ROLE_TD_FULL_ADMIN have rights to edit based upon phase roles
                        check = conferenceService.checkTdAdmin(pPermission, account, pAttendee, conference) || conferenceService.checkTdFullAdmin(pPermission, account, pAttendee, conference)
                    }
                    else if (pAttendee?.status?.equalsIgnoreCase("Pending") || pAttendee?.status?.equalsIgnoreCase("Withdrawn") || pAttendee?.status?.equalsIgnoreCase("Disapproved")) {
                        // ROLE_TD_ADMIN | ROLE_TD_FULL_ADMIN have rights to edit based upon phase roles
                        check = conferenceService.checkTdAdmin(pPermission, account, pAttendee, conference) || conferenceService.checkTdFullAdmin(pPermission, account, pAttendee, conference)

                        // ROLE_FMC_ADMIN have rights to edit based upon phase roles
                        if (pPermission?.contains("ROLE_FMC_ADMIN")) {
                            if (account?.hasAuthority("ROLE_FMC_ADMIN")) {
                                check = true
                            }
                        }

                        // ROLE_CAO grants the ability to edit within particular phases as defined
                        if (pPermission?.contains("ROLE_CAO")) {
                            // is the account the Conference AO (CAO)?
                            // is the account the Alternate CAO?
                            if (account == conference?.conferenceAO || account == conference?.alternateCAO) {
                                check = true
                            }
                        }
                    }
                }
                break

            case 'Disapprove':
                // current account must be logged in
                if (account) {
                    // check for disapprove against "supervisor"
                    if (pAttendee?.status?.equalsIgnoreCase("Supervisor")) {
                        if (pPermission?.contains("ROLE_USER") || pPermission?.contains("ROLE_AFRL_USER")) {
                            // ROLE_USER | ROLE_AFRL_USER allows a user to WITHDRAW their own record
                            // attendee account link must be enabled
                            if (pAttendee?.supervisor) {
                                // attendee account link MUST match the current account logged in
                                if (pAttendee?.supervisor?.id == account?.id) {
                                    check = true
                                }
                            }
                            if (pAttendee?.accountLink) {
                                // attendee account link MUST match the current account logged in
                                if (pAttendee?.accountLink?.supervisor?.id == account?.id) {
                                    check = true
                                }
                            }
                        }
                    }
                    // check for disapprove against "td concurrence"
                    else if (pAttendee?.status?.equalsIgnoreCase("TD Concurrence")) {
                        // ROLE_TD_ADMIN | ROLE_TD_FULL_ADMIN have rights to edit based upon phase roles
                        check = conferenceService.checkTdAdmin(pPermission, account, pAttendee, conference) || conferenceService.checkTdFullAdmin(pPermission, account, pAttendee, conference)
                    }
                    // check for disapprove against "pending"
                    else if (pAttendee?.status?.equalsIgnoreCase("Pending")) {
                        // ROLE_TD_ADMIN | ROLE_TD_FULL_ADMIN have rights to edit based upon phase roles
                        check = conferenceService.checkTdAdmin(pPermission, account, pAttendee, conference) || conferenceService.checkTdFullAdmin(pPermission, account, pAttendee, conference)

                        // ROLE_FMC_ADMIN have rights to edit based upon phase roles
                        if (pPermission?.contains("ROLE_FMC_ADMIN")) {
                            if (account?.hasAuthority("ROLE_FMC_ADMIN")) {
                                check = true
                            }
                        }

                        // ROLE_CAO grants the ability to edit within particular phases as defined
                        if (pPermission?.contains("ROLE_CAO")) {
                            // is the account the Conference AO (CAO)?
                            // is the account the Alternate CAO?
                            if (account == conference?.conferenceAO || account == conference?.alternateCAO) {
                                check = true
                            }
                        }
                    }
                }
                break

            case 'Withdraw':
                // ROLE_USER | ROLE_AFRL_USER allows a user to WITHDRAW their own record
                // current account must be logged in
                if (account) {
                    // ROLE_TD_ADMIN | ROLE_TD_FULL_ADMIN have rights to edit based upon phase roles
                    check = conferenceService.checkTdAdmin(pPermission, account, pAttendee, conference) || conferenceService.checkTdFullAdmin(pPermission, account, pAttendee, conference)

                    // ROLE_FMC_ADMIN have rights to edit based upon phase roles
                    if (pPermission?.contains("ROLE_FMC_ADMIN")) {
                        if (account?.hasAuthority("ROLE_FMC_ADMIN")) {
                            check = true
                        }
                    }

                    // ROLE_CAO grants the ability to edit within particular phases as defined
                    if (pPermission?.contains("ROLE_CAO")) {
                        // is the account the Conference AO (CAO)?
                        // is the account the Alternate CAO?
                        if (account == conference?.conferenceAO || account == conference?.alternateCAO) {
                            check = true
                        }
                    }

                    // attendee account link must be enabled
                    if (pAttendee?.accountLink) {
                        // attendee account link MUST match the current account logged in
                        if (pAttendee?.accountLink?.id == account?.id) {
                            check = true
                        }
                    }
                }
                break

            case 'Override':
                // current account must be logged in
                if (account) {
                    // ROLE_TD_ADMIN | ROLE_TD_FULL_ADMIN have rights to edit based upon phase roles
                    check = conferenceService.checkTdAdmin(pPermission, account, pAttendee, conference) || conferenceService.checkTdFullAdmin(pPermission, account, pAttendee, conference)

                    // ROLE_FMC_ADMIN have rights to edit based upon phase roles
                    if (pPermission?.contains("ROLE_FMC_ADMIN")) {
                        if (account?.hasAuthority("ROLE_FMC_ADMIN")) {
                            check = true
                        }
                    }

                        // ROLE_CAO grants the ability to OVERRIDE the user
                    if (pPermission?.contains("ROLE_CAO")) {
                        // is the account the Conference AO (CAO)?
                        // is the account the Alternate CAO?
                        if (account == conference?.conferenceAO || account == conference?.alternateCAO) {
                            check = true
                        }
                    }
                }
                break

            case 'Register':
            case 'Confirm Attendance':
            case 'Confirm':
                // current account must be logged in
                if (account) {
                    // ROLE_TD_ADMIN | ROLE_TD_FULL_ADMIN have rights to CONFIRM the user
                    check = conferenceService.checkTdAdmin(pPermission, account, pAttendee, conference) || conferenceService.checkTdFullAdmin(pPermission, account, pAttendee, conference)

                    // ROLE_FMC_ADMIN have rights to CONFIRM the user
                    if (pPermission?.contains("ROLE_FMC_ADMIN")) {
                        if (account?.hasAuthority("ROLE_FMC_ADMIN")) {
                            check = true
                        }
                    }

                    // ROLE_CAO grants the ability to CONFIRM the user
                    if (pPermission?.contains("ROLE_CAO")) {
                        // is the account the Conference AO (CAO)?
                        // is the account the Alternate CAO?
                        if (account == conference?.conferenceAO || account == conference?.alternateCAO) {
                            check = true
                        }
                    }

                    if (pPermission?.contains("ROLE_USER") || pPermission?.contains("ROLE_AFRL_USER")) {
                        // ROLE_USER | ROLE_AFRL_USER allows a user to CONFIRM their own record
                        // attendee account link must be enabled
                        if (pAttendee?.accountLink) {
                            // attendee account link MUST match the current account logged in
                            if (pAttendee?.accountLink?.id == account?.id) {
                                check = true
                            }
                        }
                    }
                }
                break

            case 'Attended':
                // current account must be logged in
                if (account) {
                    // ROLE_TD_ADMIN | ROLE_TD_FULL_ADMIN have rights to WITHDRAW the user
                    check = conferenceService.checkTdAdmin(pPermission, account, pAttendee, conference) || conferenceService.checkTdFullAdmin(pPermission, account, pAttendee, conference)

                    // ROLE_FMC_ADMIN have rights to WITHDRAW the user
                    if (pPermission?.contains("ROLE_FMC_ADMIN")) {
                        if (account?.hasAuthority("ROLE_FMC_ADMIN")) {
                            check = true
                        }
                    }

                    // ROLE_CAO grants the ability to WITHDRAW the user
                    if (pPermission?.contains("ROLE_CAO")) {
                        // is the account the Conference AO (CAO)?
                        // is the account the Alternate CAO?
                        if (account == conference?.conferenceAO || account == conference?.alternateCAO) {
                            check = true
                        }
                    }

                    if (pPermission?.contains("ROLE_USER") || pPermission?.contains("ROLE_AFRL_USER")) {
                        // ROLE_USER | ROLE_AFRL_USER allows a user to WITHDRAW their own record
                        // attendee account link must be enabled
                        if (pAttendee?.accountLink) {
                            // attendee account link MUST match the current account logged in
                            if (pAttendee?.accountLink?.id == account?.id) {
                                check = true
                            }
                        }
                    }
                }
                break

            default:
                break

        }

        return check
    }


    public boolean deleteAttendee(final Attendee pAttendeeInstance) {
        for (cost in pAttendeeInstance?.costs) {
            Address address = Address.get(cost?.lodgingAddress?.id)

            cost.lodgingAddress = null
            cost.save flush:true

            if (address) {
                address.delete flush:true
            }

            pAttendeeInstance?.costs?.remove(cost)

            cost.delete flush: true
        }

        def dateEvents = DateEvent.findAllByAttendee(pAttendeeInstance)
        for (event in dateEvents) {
            event.delete flush: true
        }

        Conference conference = pAttendeeInstance.conference
        int startSequence = pAttendeeInstance?.sequence

        conference.attendees.remove(pAttendeeInstance)
        pAttendeeInstance?.conference = null

        pAttendeeInstance.delete flush: true

        for (attendee in conference.attendees) {
            if (attendee.sequence > startSequence) {
                attendee.sequence = attendee.sequence - 1
            }

            attendee.save flush: true
        }

        conference.save flush: true
    }

}
