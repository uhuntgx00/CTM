package mil.ebs.ctm.mail

import grails.plugin.springsecurity.annotation.Secured

import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['permitAll'])
class MailTemplateController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 100, 100)
        respond MailTemplate.list(params), model: [mailTemplateInstanceCount: MailTemplate.count()]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN'])
    def show(MailTemplate mailTemplateInstance) {
        String emailTemplate = createEmailTemplate(mailTemplateInstance)

        respond mailTemplateInstance, model: [emailTemplate: emailTemplate]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def create() {
        respond new MailTemplate(params)
    }

    @Transactional
    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def save(MailTemplate mailTemplateInstance) {
        if (mailTemplateInstance == null) {
            notFound()
            return
        }

        if (mailTemplateInstance.hasErrors()) {
            respond mailTemplateInstance.errors, view: 'create'
            return
        }

        mailTemplateInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'mailTemplateInstance.label', default: 'Mail Template'), mailTemplateInstance.id])
                redirect mailTemplateInstance
            }
            '*' { respond mailTemplateInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def edit(MailTemplate mailTemplateInstance) {
        respond mailTemplateInstance
    }

    @Transactional
    @Secured(['ROLE_ADMIN', 'ROLE_FMC_ADMIN', 'ROLE_DEVELOPER'])
    def update(MailTemplate mailTemplateInstance) {
        if (mailTemplateInstance == null) {
            notFound()
            return
        }

        if (mailTemplateInstance.hasErrors()) {
            respond mailTemplateInstance.errors, view: 'edit'
            return
        }

        mailTemplateInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'MailTemplate.label', default: 'Mail Template'), mailTemplateInstance.id])
                redirect mailTemplateInstance
            }
            '*' { respond mailTemplateInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_DEVELOPER'])
    def delete(MailTemplate mailTemplateInstance) {

        if (mailTemplateInstance == null) {
            notFound()
            return
        }

        mailTemplateInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'MailTemplate.label', default: 'Mail Template'), mailTemplateInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'mailTemplateInstance.label', default: 'MailTemplate'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    private static String createEmailTemplate(final MailTemplate pMailTemplate) {
        String result = "<br/>"

        if (!pMailTemplate?.canOverride && pMailTemplate?.forUser) {
            result += "<span style=\"color:#FF0000\"><b>!!! SYSTEM EMAIL !!!</b></span><br/><br/>"
        }

        result += createAddressing(pMailTemplate, "TO")
        result += createAddressing(pMailTemplate, "CC")
        result += createAddressing(pMailTemplate, "BCC")

        result += "<br/><b>Subject:</b> " + replaceFieldContent(pMailTemplate?.subjectHeader) + "<br/>"

        result += "<br/>" + processContent(pMailTemplate) + "<br/>"

        return result
    }

    private static String createAddressing(final MailTemplate pMailTemplate, final String pAddress) {
        String result = "<b>" + pAddress + ":</b>"

        if (pMailTemplate?.forUser?.equalsIgnoreCase(pAddress)) {
            if (pMailTemplate?.canOverride) {
                result += " <span style=\"color:#FF0000\"><b>{***user***}</b></span>"
            } else {
                result += " {user}"
            }
        }
        if (pMailTemplate?.forSupervisor?.equalsIgnoreCase(pAddress)) {
            result += " {supervisor}"
        }
        if (pMailTemplate?.forTdAdmin?.equalsIgnoreCase(pAddress)) {
            result += " {TD Admin}"
        }
        if (pMailTemplate?.forTdFullAdmin?.equalsIgnoreCase(pAddress)) {
            result += " {TD Full Admin}"
        }
        if (pMailTemplate?.forTdPOC?.equalsIgnoreCase(pAddress)) {
            result += " {TD POC}"
        }
        if (pMailTemplate?.forFmcAdmin?.equalsIgnoreCase(pAddress)) {
            result += " {FMC}"
        }
        if (pMailTemplate?.forCao?.equalsIgnoreCase(pAddress)) {
            result += " {CAO}"
        }

        result += "<br/>"

        return result
    }

    /**
     *
     * @param pMailTemplate (MailTemplate) -
     * @return
     */
    private static String processContent(final MailTemplate pMailTemplate) {
        String content = pMailTemplate.templateContent
        while (content.contains("\$[[")) {
            content = replaceBlockContent(content)
            if (content.equalsIgnoreCase("*ERROR*")) {
                break
            }
        }

        while (content.contains("\$[(")) {
            content = replaceFieldContent(content)
            if (content.equalsIgnoreCase("*ERROR*")) {
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
        String replaced = "*ERROR*"

        int start = pContent.indexOf("\$[[")
        int end = pContent.indexOf("]]")
        if (start < end) {
            String blockName = pContent.substring(start + 3, end)

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
     * @return
     */
    private static String replaceFieldContent(final String pContent) {
        String replaced = "*ERROR*"

        int start = pContent.indexOf("\$[(")
        int end = pContent.indexOf(")]")

        if (start < end) {
            String fieldName = pContent.substring(start+3, end)

            String replacement
            switch (fieldName) {
                case 'Today':
                    replacement = new SimpleDateFormat("dd MMM yyyy").format(new Date())
                    break;

                default:
                    replacement = ""
            }

            if (replacement) {
                replaced = pContent.replace("\$[(" + fieldName + ")]", replacement)
            } else {
                replaced = pContent.replace("\$[(" + fieldName + ")]", "{" + fieldName + "}")
            }
        }

        return replaced
    }

}
