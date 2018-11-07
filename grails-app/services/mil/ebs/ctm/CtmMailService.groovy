package mil.ebs.ctm

import grails.transaction.Transactional
import grails.util.Holders
import mil.ebs.ctm.mail.ContentBlock
import mil.ebs.ctm.mail.MailTemplate

import java.text.SimpleDateFormat

@Transactional
class CtmMailService {

    def mailService

// ****************************************************************************************************************************************
// message blocks
// ****************************************************************************************************************************************

    /**
     *
     * @param pRequest (boolean) -
     * @param pConference (Conference) -
     * @param pAction (String) -
     * @return
     */
    private static def subjectBlock(final boolean pRequest, final Conference pConference, final String pAction) {
        return "AFRL CTM: " + (pRequest ? "Request - " : "") + pConference.conferenceTitle + " [" + pAction + "]"
    }

    /**
     * This [TAG] is the general heading on all EMAILS outbound from the system.
     *
     * @return
     */
    private static def heading() {
        return "Please view message in <b>HTML</b>." +
                singleGap() + "***Do not reply to this email. It is an automated email from the <b>Conference Tracking & Management (CTM)</b> System.***" +
                doubleGap() + "Greetings!" + doubleGap()
    }

    /**
     *
     * @param pDays (int) -
     * @return
     */
    private static def expireBlock(final int pDays) {
        return "AFRL CTM: Account Expiration Notice (" + pDays + " days)"
    }

    private static def line() {
        return "<hr/>"
    }

    /**
     * This [TAG} produces a single line
     *
     * @return
     */
    private static def singleGap() {
        return "<br/>"
    }

    /**
     * This [TAG] produces a double line
     *
     * @return
     */
    private static def doubleGap() {
        return "<br/><br/>"
    }

    /**
     * This [TAG] adds a reference to get further information from system.
     *
     * @return String -
     */
    private static String endMessage() {
        return singleGap() + "Please access the <b>CTM</b> system for further information pertaining to this conference." + singleGap()
    }

    /**
     * This [TAG] adds a reference to the HELP system.
     *
     * @return String -
     */
    private static def helpLink() {
        String helpUrl = "https://cs2.eis.afmc.af.mil/sites/afrlebs/pages/CTM.aspx"
        return  doubleGap() + "For the latest forms, additional instructions, and help on the <b>CTM</b> system, click <a href='" + helpUrl + "'>" + " HERE </a>"
    }

//***************************************************************************
// Conference INFO block
//***************************************************************************

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def conferenceInfoBlock(final Conference pConference) {
        return doubleGap() + line() + "<b>Conference:</b> " + pConference.conferenceTitle +
                singleGap() + "<b>Conference Dates:</b> " + new SimpleDateFormat("dd MMM yyyy").format(pConference.startDate) + " <b>to</b> " + new SimpleDateFormat("dd MMM yyyy").format(pConference.endDate) + " <i>(" + pConference.days() + " days)</i>" +
                singleGap() + "<b>Days Until Event:</b> " + (pConference.startDate - new Date()) +
                doubleGap() + "<b>Venue:</b> " + pConference.venue +
                singleGap() + "<b>Total Attendance Count:</b> " + pConference.attendees?.size() + line()
    }

//***************************************************************************
// Specific mail content blocks
//***************************************************************************

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def createBlock(final Conference pConference, final String pAccountName) {
        return "A conference has been created: " + pConference.conferenceTitle + "." +
                singleGap() + "This request has been made by: " + pAccountName + "." +
                doubleGap() + "Please access the <b>CTM</b> system and enable the requested conference."
    }

    /**
     *
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def expireBlock(final String pAccountName, final int pDays) {
        return "This notification email is to inform you that your account (" + pAccountName + ") within the CTM system will be expiring in " + pDays + " days." +
                doubleGap() + "Please access the <b>CTM</b> system to prevent your account from expiring."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def cancelBlock(final Conference pConference, final String pAccountName) {
        return "The conference <b>[" + pConference.conferenceTitle + "]</b> has been cancelled." +
                singleGap() + "This cancellation has been made by: " + pAccountName + "."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def changeCaoBlock(final Conference pConference, final String pAccountName) {
        return "The conference <b>[" + pConference.conferenceTitle + "]</b> CAO has been changed as of " + new SimpleDateFormat("dd MMM yyyy").format(new Date()) + "." +
                singleGap() + "The new CAO for the conference is <b>" + pConference.conferenceAO + "</b>." +
                singleGap() + "This change has been made by: " + pAccountName + "."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def notifyCaoBlock(final Conference pConference) {
        return "The conference <b>[" + pConference.conferenceTitle + "]</b> has been LOCKED and is now in the DRAFTING phase." +
                singleGap() + "The assigned CAO for the conference as of " + new SimpleDateFormat("dd MMM yyyy").format(new Date()) + " is <b>" + pConference.conferenceAO + "</b>."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def notifyReviseBlock(final Conference pConference) {
        return "The conference <b>[" + pConference.conferenceTitle + "]</b> has requested REVISION by AFRL FMC and is now in the REVISING phase." +
                singleGap() + "The assigned CAO for the conference as of " + new SimpleDateFormat("dd MMM yyyy").format(new Date()) + " is <b>" + pConference.conferenceAO + "</b>."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def notifyApprovedBlock(final Conference pConference) {
        return "The conference <b>[" + pConference.conferenceTitle + "]</b> has been APPROVED and is now in the APPROVED phase." +
                singleGap() + "Attendees may now REGISTER for the conference."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def notifyFinalizingdBlock(final Conference pConference) {
        return "The conference <b>[" + pConference.conferenceTitle + "]</b> has been APPROVED and is now in the FINALIZING phase." +
                singleGap() + "Attendees may now indicate they have ATTENDED the conference and record ACTUAL costs."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def notifyDisapprovedBlock(final Conference pConference) {
        return "The conference <b>[" + pConference.conferenceTitle + "]</b> has been DISAPPROVED and is now in the CLOSED phase."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def notifyFmcBlock(final Conference pConference) {
        return "The conference <b>[" + pConference.conferenceTitle + "]</b> has been submitted to be approved/disapproved and is now in the PROCESSING phase." +
                singleGap() + "The assigned CAO for the conference as of " + new SimpleDateFormat("dd MMM yyyy").format(new Date()) + " is <b>" + pConference.conferenceAO + "</b>."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def notifyTDBlock(final Conference pConference) {
        return "The conference <b>[" + pConference.conferenceTitle + "]</b> has been submitted for review by the Responsible TD and is now in the REVIEWING phase." +
                singleGap() + "The assigned CAO for the conference as of " + new SimpleDateFormat("dd MMM yyyy").format(new Date()) + " is <b>" + pConference.conferenceAO + "</b>."
    }

    /**
     *
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccountName (String) - display name for account performing the action
     * @return
     */
    private static def notifySupervisorBlock(final Account pAccount, final Conference pConference) {
        return pAccount.toString() + " has selected attendance for conference <b>[" + pConference.conferenceTitle + "]</b> that is being tracked in the CTM application." +
                singleGap() + "The assigned CAO for the conference as of " + new SimpleDateFormat("dd MMM yyyy").format(new Date()) + " is <b>" + pConference.conferenceAO + "</b>."
    }

// ****************************************************************************************************************************************
// private message call
// ****************************************************************************************************************************************

    /**
     * -PRIVATE-
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccountEmail (String) - email address of the account performing the action
     * @param pSubject (String) - subject of the email
     * @param pContent (String) - content of the email
     * @return
     */
    private def sendMail(final List<String> pToAddress, final String pAccountEmail, final String pSubject, final String pContent) {
        if (pToAddress && pAccountEmail) {
            mailService.sendMail {
                to pToAddress.toArray()
                cc pAccountEmail
                subject pSubject
                html pContent
            }
        }
    }

    /**
     * -PRIVATE-
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email 'TO'
     * @param pCcAddress (List<String>) - list of email addresses to send the email 'CC'
     * @param pBccAddress (List<String>) - list of email addresses to send the email 'BCC'
     * @param pSubject (String) - subject of the email
     * @param pContent (String) - content of the email
     * @return
     */
    private def sendMail(final List<String> pToAddress, final List<String> pCcAddress, final List<String> pBccAddress, final String pSubject, final String pContent) {
        if (pToAddress && pCcAddress && pBccAddress) {
            mailService.sendMail {
                to pToAddress.toArray()
                cc pCcAddress.toArray()
                bcc pBccAddress.toArray()
                subject pSubject
                html pContent
            }
            return
        }

        if (pToAddress && pCcAddress) {
            mailService.sendMail {
                to pToAddress.toArray()
                cc pCcAddress.toArray()
                subject pSubject
                html pContent
            }
            return
        }

        if (pToAddress && pBccAddress) {
            mailService.sendMail {
                to pToAddress.toArray()
                bcc pBccAddress.toArray()
                subject pSubject
                html pContent
            }
            return
        }

        if (pCcAddress && pBccAddress) {
            mailService.sendMail {
                cc pCcAddress.toArray()
                bcc pBccAddress.toArray()
                subject pSubject
                html pContent
            }
            return
        }

        if (pToAddress) {
            mailService.sendMail {
                to pToAddress.toArray()
                subject pSubject
                html pContent
            }
            return
        }

        if (pCcAddress) {
            mailService.sendMail {
                cc pCcAddress.toArray()
                subject pSubject
                html pContent
            }
            return
        }

        if (pBccAddress) {
            mailService.sendMail {
                bcc pBccAddress.toArray()
                subject pSubject
                html pContent
            }
        }
    }

// ****************************************************************************************************************************************
// private message/template functions
// ****************************************************************************************************************************************

    /**
     *
     * @param pMailTemplate (MailTemplate) -
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccount (Account) - account performing the action
     * @return
     */
    private static String processContent(final MailTemplate pMailTemplate, final Conference pConference, final Account pAccount, final Account pAttendee) {
        String content = pMailTemplate.templateContent

        while (content.contains("\$[[")) {
            content = replaceBlockContent(content)
            if (content.equalsIgnoreCase("*BLOCK ERROR*")) {
                break
            }
        }

        while (content.contains("\$[(")) {
            content = replaceFieldContent(content, pConference, pAccount, pAttendee)
            if (content.equalsIgnoreCase("*FIELD ERROR*")) {
                break
            }
        }

        return content
    }

    /**
     *
     * @param pContent (String) -
     * @return
     */
    private static String replaceBlockContent(final String pContent) {
        String replaced = "*BLOCK ERROR*"

        // locate the block replacement link
        int start = pContent.indexOf("\$[[")
        int end = pContent.indexOf("]]")

        // make sure start is before end
        if (start < end) {
            String blockName = pContent.substring(start+3, end)

            // determine the replaced value for the content block
            ContentBlock contentBlock = ContentBlock.findByBlockName(blockName)

            // if the replacement value is acceptable replace - otherwise re-wrap with {}
            if (contentBlock) {
                replaced = pContent.replace("\$[[" + blockName + "]]", contentBlock?.blockContent)
            } else {
                replaced = pContent.replace("\$[[" + blockName + "]]", "{" + blockName + "}")
            }
        }

        return replaced
    }

    /**
     *
     * @param pContent (String) -
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccount (Account) - account performing the action
     * @param pAttendee (Account) - attendee email pertains to
     * @return
     */
    private static String replaceFieldContent(final String pContent, final Conference pConference, final Account pAccount, final Account pAttendee) {
        String replaced = "*FIELD ERROR*"

        // locate the field replacement link
        int start = pContent.indexOf("\$[(")
        int end = pContent.indexOf(")]")

        if (start < end) {
            String fieldName = pContent.substring(start+3, end)

            // determine the replacement value
            String replacement
            switch (fieldName) {
                case 'Today':
                    replacement = new SimpleDateFormat("dd MMM yyyy").format(new Date())
                    break

                case 'ConferenceTitle':
                    replacement = pConference?.conferenceTitle
                    break

                case 'ConferenceLink':
                    def serverURL = Holders.config.grails.configURL
                    replacement = "<a href=\"" + serverURL + "conference/show/" + pConference?.id + "\">" + pConference?.conferenceTitle + "</a>"
                    break

                case 'HomeLink':
                    def serverURL = Holders.config.grails.configURL
                    replacement = "<a href=\"" + serverURL + "\">CTM</a>"
                    break

                case 'HomeLinkFull':
                    def serverURL = Holders.config.grails.configURL
                    replacement = "<a href=\"" + serverURL + "\">Conference Tracking & Management (CTM)</a>"
                    break

                case 'Venue':
                    replacement = pConference?.venue
                    break

                case 'NumAttendees':
                    replacement = pConference?.attendees?.size()
                    break

                case 'DaysUntil':
                    replacement = pConference?.startDate - new Date()
                    break

                case 'StartDate':
                    replacement = new SimpleDateFormat("dd MMM yyyy").format(pConference?.startDate)
                    break

                case 'EndDate':
                    replacement = new SimpleDateFormat("dd MMM yyyy").format(pConference?.endDate)
                    break

                case 'EventDays':
                    replacement = pConference?.days()
                    break

                case 'CAO':
                    String primaryCAO = pConference?.conferenceAO
                    String alternateCAO = pConference?.alternateCAO

                    if (pConference?.alternateCAO) {
                        replacement = "<b>" + primaryCAO + "</b> <i>(primary)</i> | <b>" + alternateCAO + "</b> <i>(alternate)</i>"
                    } else {
                        replacement = "<b>" + primaryCAO + "</b>"
                    }
                    break

                case 'AccountName':
                    replacement = pAccount?.toString()
                    break

                case 'Attendee':
                    replacement = pAttendee?.toString()
                    break

                case 'ApprovalMemo':
                    replacement = pConference?.approvalNotice
                    break

                case 'DisapproveMemo':
                    replacement = pConference?.disapproveNotice
                    break

                case 'ExpireAccount':
                    replacement = pAccount?.toString()
                    break

                case 'ExpireDays':
                    replacement = "?"
                    break

                default:
                    replacement = ""
            }

            // if the replacement value is acceptable replace - otherwise re-wrap with {}
            if (replacement) {
                replaced = pContent.replace("\$[(" + fieldName + ")]", replacement)
            } else {
                replaced = pContent.replace("\$[(" + fieldName + ")]", "{" + fieldName + "}")
            }
        }

        // return the replaced content
        return replaced
    }

// ****************************************************************************************************************************************
// public message calls
// ****************************************************************************************************************************************

    /**
     *
     * @param pMailTemplate (MailTemplate) -
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAccount (Account) - account performing the action
     * @param pAttendee (Account) - attendee account action being performed upon
     * @param pToAddress (List<String>) - list of email addresses to send the email 'to'
     * @param pCcAddress (List<String>) - list of email addresses to send the email 'cc'
     * @param pBccAddress (List<String>) - list of email addresses to send the email 'bcc'
     */
    def sendNotification(final MailTemplate pMailTemplate, final Conference pConference, final Account pAccount, final Account pAttendee,
                         final List<String> pToAddress, final List<String> pCcAddress, final List<String> pBccAddress) {
        // if the mail template is not valid exit
        if (!pMailTemplate) {
            return
        }

        def subjectText = replaceFieldContent(pMailTemplate?.subjectHeader, pConference, pAccount, pAttendee)
        String content = processContent(pMailTemplate, pConference, pAccount, pAttendee)

        sendMail(pToAddress, pCcAddress, pBccAddress, subjectText, content)
    }


    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @param pAction (String) - action that is being performed
     * @return
     */
    def sendRequest(final List<String> pToAddress, final Account pAccount, final Conference pConference, final String pAction) {
        def subjectText = subjectBlock(true, pConference, pAction)
        String content = heading() + createBlock(pConference, pAccount.toString()) + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendCancellationNotice(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        def subjectText = subjectBlock(false, pConference, "Cancellation")
        String content = heading() + cancelBlock(pConference, pAccount.toString()) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendChangeOfCAO(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        println pToAddress

        def subjectText = subjectBlock(false, pConference, "Change of CAO")
        String content = heading() + changeCaoBlock(pConference, pAccount.toString()) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendNotifyCAO(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        println pToAddress

        def subjectText = subjectBlock(false, pConference, "CAO Notification")
        String content = heading() + notifyCaoBlock(pConference) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendNotifyApproval(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        println pToAddress

        def subjectText = subjectBlock(false, pConference, "Approval")
        String content = heading() + notifyApprovedBlock(pConference) + conferenceInfoBlock(pConference) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendNotifyFinalize(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        println pToAddress

        def subjectText = subjectBlock(false, pConference, "Finalize")
        String content = heading() + notifyFinalizingdBlock(pConference) + conferenceInfoBlock(pConference) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendNotifyDisapproved(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        println pToAddress

        def subjectText = subjectBlock(false, pConference, "Disapproved")
        String content = heading() + notifyDisapprovedBlock(pConference) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendNotifyTD(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        println pToAddress

        def subjectText = subjectBlock(false, pConference, "TD Notification")
        String content = heading() + notifyTDBlock(pConference) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendNotifyFMC(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        println pToAddress

        def subjectText = subjectBlock(false, pConference, "FMC Notification")
        String content = heading() + notifyFmcBlock(pConference) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendNotifyRevise(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        println pToAddress

        def subjectText = subjectBlock(false, pConference, "Revision Request")
        String content = heading() + notifyReviseBlock(pConference) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pConference (Conference) - conference email is pertaining to
     * @return
     */
    def sendNotifySupervisor(final List<String> pToAddress, final Account pAccount, final Conference pConference) {
        println pToAddress

        def subjectText = subjectBlock(false, pConference, "Supervisor Notification")
        String content = heading() + notifySupervisorBlock(pAccount, pConference) + endMessage() + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

    /**
     *
     * @param pToAddress (List<String>) - list of email addresses to send the email to
     * @param pAccount (Account) - account performing the action
     * @param pDays (int) - number of days before expiration occurs
     * @return
     */
    def sendExpireNotification(final List<String> pToAddress, final Account pAccount, final int pDays) {
        def subjectText = expireBlock(pDays)
        String content = heading() + expireBlock(pAccount.toString(), pDays) + helpLink()

        sendMail(pToAddress, pAccount.emailAddress, subjectText, content)
    }

}
