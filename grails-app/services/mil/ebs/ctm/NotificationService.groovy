package mil.ebs.ctm

import grails.transaction.Transactional
import mil.ebs.ctm.mail.MailTemplate

@Transactional
class NotificationService {

    def ctmMailService

    /**
     *
     * @param pConference (Conference) -
     * @param pCanOverride (boolean) -
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildAttendeeEmailList(final Conference pConference, final boolean pCanOverride) {
        List<String> emailList = new ArrayList<String>(0)

        for (attendee in Attendee.findAllByConference(pConference, [sort: "id"])) {
            if (attendee?.status?.equalsIgnoreCase("Pending")
                    || attendee?.status?.equalsIgnoreCase("Approved")
                    || attendee?.status?.equalsIgnoreCase("Registered")
                    || attendee?.status?.equalsIgnoreCase("Confirmed")
                    || attendee?.status?.equalsIgnoreCase("Attended")
                    || attendee?.status?.equalsIgnoreCase("Wait List"))
            {
                if (pCanOverride) {
                    if (attendee?.notifyConferenceChanges && attendee?.accountLink?.assignedTD?.allowAttendeeNotification) {
                        // send to the account email address
                        if (attendee?.accountLink?.emailAddress) {
                            emailList.add(attendee?.accountLink?.emailAddress)
                        }

                        // send to external email address configured
                        if (attendee?.extEmailAddress) {
                            emailList.add(attendee?.extEmailAddress)
                        }
                    }
                } else {
                    // send to the account email address
                    if (attendee?.accountLink?.emailAddress) {
                        emailList.add(attendee?.accountLink?.emailAddress)
                    }

                    // send to external email address configured
                    if (attendee?.extEmailAddress) {
                        emailList.add(attendee?.extEmailAddress)
                    }
                }
            }
        }

        return emailList
    }

    /**
     *
     * @param pConference (Conference) -
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildSupervisorEmailList(final Conference pConference) {
        List<String> emailList = new ArrayList<String>(0)

        for (attendee in Attendee.findAllByConference(pConference, [sort: "id"])) {
            if ((attendee?.status?.equalsIgnoreCase("Pending")
                    || attendee?.status?.equalsIgnoreCase("Approved")
                    || attendee?.status?.equalsIgnoreCase("Registered")
                    || attendee?.status?.equalsIgnoreCase("Confirmed")
                    || attendee?.status?.equalsIgnoreCase("Attended")
                    || attendee?.status?.equalsIgnoreCase("Wait List")))
            {
                // send to the account email address
                if (attendee?.supervisor?.emailAddress) {
                    emailList.add(attendee?.supervisor?.emailAddress)
                }
            }
        }

        return emailList
    }

    /**
     *
     * @param pConference (Conference) -
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildSupervisorRequestEmailList(final Conference pConference) {
        return buildSupervisorEmailList(pConference, null)
    }

    /**
     *
     * @param pConference (Conference) -
     * @param pAttendee (Account) -
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildSupervisorRequestEmailList(final Conference pConference, final Account pAttendee) {
        List<String> emailList = new ArrayList<String>(0)

        if (pAttendee) {
//            if (pAttendee?.status?.equalsIgnoreCase("Supervisor")) {
                if (pAttendee?.supervisor?.emailAddress) {
                    emailList.add(pAttendee?.supervisor?.emailAddress)
                }
//            }
        } else {
            for (attendee in Attendee.findAllByConference(pConference, [sort: "id"])) {
                if (attendee?.status?.equalsIgnoreCase("Supervisor")) {
                    if (attendee?.supervisor?.emailAddress) {
                        emailList.add(attendee?.supervisor?.emailAddress)
                    }
                }
            }
        }

        return emailList
    }

    /**
     *
     * @param pConference (Conference) -
     * @param pPrimaryOnly (boolean) - TRUE include only primary CAO | FALSE include primary and alternate CAO
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildCaoEmailList(final Conference pConference, final boolean pPrimaryOnly) {
        List<String> emailList = new ArrayList<String>(0)

        if (pConference?.conferenceAO) {
            // send to the account email address
            if (pConference?.conferenceAO?.emailAddress) {
                emailList.add(pConference?.conferenceAO?.emailAddress)
            }

            if (!pPrimaryOnly && pConference?.alternateCAO) {
                // send to the account email address
                if (pConference?.alternateCAO?.emailAddress) {
                    emailList.add(pConference?.alternateCAO?.emailAddress)
                }
            }
        }

        return emailList
    }

    /**
     *
     * @param pConference (Conference) -
     * @param pPrimaryOnly (boolean) - TRUE include only primary contact | FALSE include primary and all alternate contacts
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildTdEmailList(final Conference pConference, final boolean pPrimaryOnly) {
        List<String> emailList = new ArrayList<String>(0)

        if (pConference?.responsibleTD) {
            emailList = buildTdEmailList(pConference?.responsibleTD, pPrimaryOnly)
        }

        return emailList
    }

    /**
     *
     * @param pTechnicalDirective (TechnicalDirective) -
     * @param pPrimaryOnly (boolean) - TRUE include only primary contact | FALSE include primary and all alternate contacts
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildTdEmailList(final Organization pTechnicalDirective, final boolean pPrimaryOnly) {
        List<String> emailList = new ArrayList<String>(0)

        for (accountContact in pTechnicalDirective?.contacts) {
            if (pPrimaryOnly) {
                if (accountContact?.primaryPOC) {
                    // send to the account email address
                    if (accountContact?.accountLink?.emailAddress) {
                        emailList.add(accountContact?.accountLink?.emailAddress)
                    }
                }
            } else {
                // send to the account email address
                if (accountContact?.accountLink?.emailAddress) {
                    emailList.add(accountContact?.accountLink?.emailAddress)
                }
            }
        }

        return emailList
    }

    /**
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildFmcEmailList() {
        List<String> emailList = new ArrayList<String>(0)

        for (accountRole in AccountRole.findAllByRole(Role.findByAuthority("ROLE_FMC_ADMIN"))) {
            if (accountRole?.account?.emailAddress) {
                emailList.add(accountRole?.account?.emailAddress)
            }
        }

        return emailList
    }

    /**
     *
     * @param pConference (Conference) -
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildTdAdminEmailList(final Conference pConference) {
        List<String> emailList = new ArrayList<String>(0)

        for (accountRole in AccountRole.findAllByRole(Role.findByAuthority("ROLE_TD_ADMIN"))) {
            if (accountRole?.account?.assignedTD?.topParent?.id == pConference?.responsibleTD?.topParent?.id) {
                // send to the account email address
                if (accountRole?.account?.emailAddress) {
                    emailList.add(accountRole?.account?.emailAddress)
                }
            }
        }

        return emailList
    }

    /**
     *
     * @param pConference (Conference) -
     *
     * @return List<String> - list of emails to be included for the email being generated
     */
    public static List<String> buildTdFullAdminEmailList(final Conference pConference) {
        List<String> emailList = new ArrayList<String>(0)

        for (accountRole in AccountRole.findAllByRole(Role.findByAuthority("ROLE_TD_FULL_ADMIN"))) {
            if (accountRole?.account?.assignedTD?.topParent?.id == pConference?.responsibleTD?.topParent?.id) {
                // send to the account email address
                if (accountRole?.account?.emailAddress) {
                    emailList.add(accountRole?.account?.emailAddress)
                }
            }
        }

        return emailList
    }

    /**
     *
     * @param pConference (Conference) -
     * @param pIncludeFMC (boolean) - TRUE include ROLE_FMC_ADMIN accounts in email | FALSE do not include accounts
     * @param pIncludeTDadmin (boolean) - TRUE include ROLE_TD_ADMIN/ROLE_TD_FULL_ADMIN accounts (within the responsible TD) in email | FALSE do not include accounts
     * @param pIncludeTDpoc (boolean) - TRUE include TD POC (primary) accounts (within the responsible TD) in email | FALSE do not include accounts
     * @param pIncludeCAO (boolean) - TRUE include CAO (primary) accounts in email | FALSE do not include accounts
     * @param pIncludeSupervisor (boolean) - TRUE include attendee supervisor accounts in email | FALSE do not include accounts
     * @param pIncludeAttendee (boolean) - TRUE include attendees in email | FALSE do not include accounts
     *
     * @return List<String> - list of emails to be included on the TO section of the email
     */
    public static List<String> buildNotificationList(final Conference pConference,
            final boolean pIncludeFMC, final boolean pIncludeTDadmin, final boolean pIncludeTDpoc, final boolean pIncludeCAO, final boolean pIncludeSupervisor, final boolean pIncludeAttendee) {
        List<String> emailList = new ArrayList<String>(0)

        if (pIncludeAttendee) {
            List<String> temp = buildAttendeeEmailList(pConference, false)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pIncludeSupervisor) {
            List<String> temp = buildSupervisorEmailList(pConference)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pIncludeCAO) {
            List<String> temp = buildCaoEmailList(pConference, true)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pIncludeTDpoc) {
            List<String> temp = buildTdEmailList(pConference, true)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pIncludeTDadmin) {
            List<String> temp = buildTdAdminEmailList(pConference)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pIncludeFMC) {
            List<String> temp = buildFmcEmailList()
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        return emailList
    }

    /**
     *
     * @param pAddressType (String) -
     * @param pMailTemplate (MailTemplate) -
     * @param pConference (Conference) -
     *
     * @return List<String> - list of emails to be included on the TO section of the email
     */
    public static List<String> buildNotificationList(final String pAddressType, final MailTemplate pMailTemplate, final Conference pConference) {
        return buildNotificationList(pAddressType, pMailTemplate, pConference, true, null)
    }

    /**
     *
     * @param pAddressType (String) -
     * @param pMailTemplate (MailTemplate) -
     * @param pConference (Conference) -
     * @param pIncludeUsers (boolean) -
     *
     * @return List<String> - list of emails to be included on the TO section of the email
     */
    public static List<String> buildNotificationList(final String pAddressType, final MailTemplate pMailTemplate, final Conference pConference, final boolean pIncludeUsers) {
        return buildNotificationList(pAddressType, pMailTemplate, pConference, pIncludeUsers, null)
    }

    /**
     *
     * @param pAddressType (String) -
     * @param pMailTemplate (MailTemplate) -
     * @param pConference (Conference) -
     * @param pIncludeUsers (boolean) -
     *
     * @return List<String> - list of emails to be included on the TO section of the email
     */
    public static List<String> buildNotificationList(final String pAddressType, final MailTemplate pMailTemplate, final Conference pConference, final boolean pIncludeUsers, final Account pAttendee) {
        List<String> emailList = new ArrayList<String>(0)

        if (pIncludeUsers) {
            if (pMailTemplate?.forUser?.equalsIgnoreCase(pAddressType)) {
                List<String> temp = buildAttendeeEmailList(pConference, pMailTemplate?.canOverride)
                if (!temp.isEmpty()) {
                    emailList.addAll(temp)
                }
            }
        }

        if (pMailTemplate?.forSupervisor?.equalsIgnoreCase(pAddressType)) {
            List<String> temp = buildSupervisorRequestEmailList(pConference, pAttendee)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pMailTemplate?.forTdAdmin?.equalsIgnoreCase(pAddressType)) {
            List<String> temp = buildTdAdminEmailList(pConference)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pMailTemplate?.forTdFullAdmin?.equalsIgnoreCase(pAddressType)) {
            List<String> temp = buildTdFullAdminEmailList(pConference)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pMailTemplate?.forTdPOC?.equalsIgnoreCase(pAddressType)) {
            List<String> temp = buildTdEmailList(pConference, true)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pMailTemplate?.forFmcAdmin?.equalsIgnoreCase(pAddressType)) {
            List<String> temp = buildFmcEmailList()
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        if (pMailTemplate?.forCao?.equalsIgnoreCase(pAddressType)) {
            List<String> temp = buildCaoEmailList(pConference, true)
            if (!temp.isEmpty()) {
                emailList.addAll(temp)
            }
        }

        return emailList
    }

    /**
     * Typical email list for most notifications in CTM - FMC (false) | TDadmin (false) | TDpoc (true) | CAO (true) | Supervisor (false) | Attendee (true)
     *
     * @param pConference (Conference) -
     *
     * @return List<String> - list of emails to be included on the TO section of the email
     */
    public static List<String> buildNotificationList(final Conference pConference) {
        return buildNotificationList(pConference, false, false, true, true, false, true)
    }

    /**
     *
     * @param pCtmMailService (CtmMailService) -
     * @param pNotify (String) -
     * @param pConferenceInstance (Conference) -
     * @param pAccount (Account) -
     * @return
     */
    @SuppressWarnings("GroovyUnusedDeclaration")
    def notifyOld(final CtmMailService pCtmMailService, final String pNotify, final Conference pConferenceInstance, final Account pAccount) {
        switch (pNotify) {
            case 'notifyCancellation':
                pCtmMailService.sendCancellationNotice(buildNotificationList(pConferenceInstance), pAccount, pConferenceInstance)
                break

            case 'notifySupervisor':
                List<String> toAddress = new ArrayList<String>()
                if (pAccount.supervisor && pAccount.supervisor?.emailAddress) {
                    toAddress.add(pAccount.supervisor?.emailAddress)
                    pCtmMailService.sendNotifySupervisor(toAddress, pAccount, pConferenceInstance)
                }
                break

            case 'notifyCAO':
                pCtmMailService.sendNotifyCAO(buildNotificationList(pConferenceInstance), pAccount, pConferenceInstance)
                break

            case 'notifyRevise':
                pCtmMailService.sendNotifyRevise(buildNotificationList(pConferenceInstance, true, false, true, true, false, false), pAccount, pConferenceInstance)
                break

            case 'notifyApproval':
                pCtmMailService.sendNotifyApproval(buildNotificationList(pConferenceInstance), pAccount, pConferenceInstance)
                break

            case 'notifyDisapprove':
                pCtmMailService.sendNotifyDisapproved(buildNotificationList(pConferenceInstance), pAccount, pConferenceInstance)
                break

            case 'notifyTD':
                pCtmMailService.sendNotifyTD(buildNotificationList(pConferenceInstance, false, true, true, false, false, false), pAccount, pConferenceInstance)
                break

            case 'notifyFMC':
                pCtmMailService.sendNotifyFMC(buildNotificationList(pConferenceInstance, true, false, false, false, false, false), pAccount, pConferenceInstance)
                break

//            default:
                // do nothing
        }

    }

    /**
     *
     * @param pNotify (String) -
     * @param pConferenceInstance (Conference) -
     * @param pAccount (Account) -
     * @param pAttendee (Account) -
     * @return
     */
    def notify(final String pNotify, final Conference pConferenceInstance, final Account pAccount) {
        // if the pNotify is not valid exit
        if (!pNotify && !pNotify?.contains('notify')) {
            return
        }

        // locate the mailTemplate for the notification that will take place
        MailTemplate mailTemplate = MailTemplate.findByTemplateName(pNotify)
        if (!mailTemplate) {
            return
        }

        List<String> toAddress = buildNotificationList("TO", mailTemplate, pConferenceInstance)
        List<String> ccAddress = buildNotificationList("CC", mailTemplate, pConferenceInstance)
        List<String> bccAddress = buildNotificationList("BCC", mailTemplate, pConferenceInstance)

        ctmMailService.sendNotification(mailTemplate, pConferenceInstance, pAccount, null, toAddress, ccAddress, bccAddress)
    }

    /**
     *
     * @param pChange (String) -
     * @param pConferenceInstance (Conference) -
     * @param pAccount (Account) -
     * @return
     */
    def change(final String pChange, final Conference pConferenceInstance, final Account pAccount) {
        // if the pNotify is not valid exit
        if (!pChange && !pChange?.contains('change')) {
            return
        }

        // locate the mailTemplate for the notification that will take place
        MailTemplate mailTemplate = MailTemplate.findByTemplateName(pChange)
        if (!mailTemplate) {
            return
        }

        List<String> toAddress = buildNotificationList("TO", mailTemplate, pConferenceInstance)
        List<String> ccAddress = buildNotificationList("CC", mailTemplate, pConferenceInstance)
        List<String> bccAddress = buildNotificationList("BCC", mailTemplate, pConferenceInstance)

        ctmMailService.sendNotification(mailTemplate, pConferenceInstance, pAccount, null, toAddress, ccAddress, bccAddress)
    }

    /**
     *
     * @param pRequest (String) -
     * @param pConferenceInstance (Conference) -
     * @param pAccount (Account) -
     * @param pAttendee (Account) -
     * @return
     */
    def request(final String pRequest, final Conference pConferenceInstance, final Account pAccount, final Account pAttendee) {
        // if the pRequest is not valid exit
        if (!pRequest && !pRequest?.contains('request')) {
            return
        }

        // locate the mailTemplate for the notification that will take place
        MailTemplate mailTemplate = MailTemplate.findByTemplateName(pRequest)
        if (!mailTemplate) {
            return
        }

        // do not include any user's the lists
        List<String> toAddress = buildNotificationList("TO", mailTemplate, pConferenceInstance, false, pAttendee)
        List<String> ccAddress = buildNotificationList("CC", mailTemplate, pConferenceInstance, false, pAttendee)
        List<String> bccAddress = buildNotificationList("BCC", mailTemplate, pConferenceInstance, false, pAttendee)

        if (pAttendee?.assignedTD?.allowAttendeeNotification) {
            switch (mailTemplate?.forUser) {
                case 'TO':
                    toAddress.add(pAttendee?.emailAddress)
                    break

                case 'CC':
                    ccAddress.add(pAttendee?.emailAddress)
                    break

                case 'BCC':
                    bccAddress.add(pAttendee?.emailAddress)
                    break
            }
        }

        ctmMailService.sendNotification(mailTemplate, pConferenceInstance, pAccount, pAttendee, toAddress, ccAddress, bccAddress)
    }




}
