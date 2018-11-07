package mil.ebs.ctm.upload

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import mil.ebs.ctm.Account
import mil.ebs.ctm.Conference
import mil.ebs.ctm.DateEvent
import mil.ebs.ctm.ref.RefDateGate
import mil.ebs.ctm.ref.RefPhaseState
import org.springframework.web.multipart.MultipartFile

@Transactional(readOnly = true)
@Secured(["permitAll"])
class FileUploadController {

    def fileService
    def springSecurityService
    def notificationService

    def index = {
        def conferenceInstance = Conference.get(params.id)
        [conference_id: params.id, conference: conferenceInstance]
    }

    def packageFileUpload = {
        def conferenceInstance = Conference.get(params.conferenceId)
        [conference_id: params.conferenceId, conference: conferenceInstance, refId: params.refId]
    }

    def aarFileUpload = {
        def conferenceInstance = Conference.get(params.conferenceId)
        [conference_id: params.conferenceId, conference: conferenceInstance, refId: params.refId]
    }

    def safMemoFileUpload = {
        def conferenceInstance = Conference.get(params.conferenceId)
        [conference_id: params.conferenceId, conference: conferenceInstance, refId: params.refId]
    }

    def save = {
        try {
            Conference conferenceInstance = Conference.get(params.conference_id)

            FileUpload aarUpload = processFileUpload("AAR", request.getFile("aar_file"), conferenceInstance, params.aar_comment)
            FileUpload safMemoUpload = processFileUpload("Approval Memo", request.getFile("safMemo_file"), conferenceInstance, params.safMemo_comment)
            FileUpload packageUpload = processFileUpload("Package", request.getFile("package_file"), conferenceInstance, params.safMemo_comment)

            List<FileUpload> files = new ArrayList<FileUpload>()
            files.add(aarUpload)
            files.add(safMemoUpload)
            files.add(packageUpload)

            [uploadedFiles: files, conference_id: params.conference_id, conference: params.conference]
        } catch (FileNotFoundException ignored) {
            flash.message = "Selected File does not exist"
            redirect action: index
            return
        } catch (IOException ignored) {
            flash.message = "Invalid File select"
            redirect action: index
            return
        }
    }

    def saveAAR = {
        try {
            Conference conferenceInstance = Conference.get(params.conference_id)

            def account = Account.get(springSecurityService.principal.id)

            FileUpload aarUpload = processFileUpload("AAR", request.getFile("aar_file"), conferenceInstance, params.aar_comment, account)

            List<FileUpload> files = new ArrayList<FileUpload>()
            files.add(aarUpload)

            // does the phase state have a valid date gate event to process?
            RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))
            if (phaseState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(phaseState.dateGateEvent), eventDate: new Date(), recordedBy: account, conference: conferenceInstance).save(flush: true)
            }

            if (phaseState?.actionNotification) {
                notificationService.notify(phaseState?.actionNotification, conferenceInstance, account)
            }

            [uploadedFiles: files, conference_id: params.conference_id, conference: params.conference]
        } catch (FileNotFoundException ignored) {
            flash.message = "Selected File does not exist"
            redirect action: index
            return
        } catch (IOException ignored) {
            flash.message = "Invalid File select"
            redirect action: index
            return
        }
    }

    def saveMemo = {
        try {
            Conference conferenceInstance = Conference.get(params.conference_id)

            def account = Account.get(springSecurityService.principal.id)

            FileUpload safMemoUpload = processFileUpload("Approval Memo", request.getFile("safMemo_file"), conferenceInstance, params.safMemo_comment, account)

            List<FileUpload> files = new ArrayList<FileUpload>()
            files.add(safMemoUpload)

            // does the phase state have a valid date gate event to process?
            RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))
            if (phaseState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(phaseState.dateGateEvent), eventDate: new Date(), recordedBy: account, conference: conferenceInstance).save(flush: true)
            }

            if (phaseState?.actionNotification) {
                notificationService.notify(phaseState?.actionNotification, conferenceInstance, account)
            }

            [uploadedFiles: files, conference_id: params.conference_id, conference: params.conference]
        } catch (FileNotFoundException ignored) {
            flash.message = "Selected File does not exist"
            redirect action: index
            return
        } catch (IOException ignored) {
            flash.message = "Invalid File select"
            redirect action: index
            return
        }
    }

    def savePackage = {
        try {
            Conference conferenceInstance = Conference.get(params.conference_id)

            def account = Account.get(springSecurityService.principal.id)

            FileUpload crfUpload = processFileUpload("CRF", request.getFile("crf_file"), conferenceInstance, params.crf_comment, account)
            FileUpload agendaUpload = processFileUpload("Agenda", request.getFile("agenda_file"), conferenceInstance, params.agenda_comment, account)
            FileUpload capeUpload = processFileUpload("CAPE", request.getFile("cape_file"), conferenceInstance, params.cape_comment, account)
            FileUpload tdDocUpload = processFileUpload("TD Doc", request.getFile("td_file"), conferenceInstance, params.td_comment, account)

            List<FileUpload> files = new ArrayList<FileUpload>()
            files.add(crfUpload)
            files.add(agendaUpload)
            files.add(capeUpload)
            files.add(tdDocUpload)

            // does the phase state have a valid date gate event to process?
            RefPhaseState phaseState = RefPhaseState.get(params.get("refId"))
            if (phaseState?.dateGateEvent) {
                new DateEvent(dateGate: RefDateGate.findByCode(phaseState.dateGateEvent), eventDate: new Date(), recordedBy: account, conference: conferenceInstance).save(flush: true)
            }

            if (phaseState?.actionNotification) {
                notificationService.notify(phaseState?.actionNotification, conferenceInstance, account)
            }

            [uploadedFiles: files, conference_id: params.conference_id, conference: params.conference]
        } catch (FileNotFoundException ignored) {
            flash.message = "Selected File does not exist"
            redirect action: index
            return
        } catch (IOException ignored) {
            flash.message = "Invalid File select"
            redirect action: index
            return
        }
    }

    private FileUpload processFileUpload(final String pFileType, MultipartFile pFileUpload, final Conference pConferenceInstance, final String pComments, final Account pAccount) {
        FileUpload upload = null

        if (pFileUpload != null && pFileUpload?.size > 0) {
            upload = fileService.uploadFile(pFileUpload, pFileType, pConferenceInstance, pComments, pAccount)
        }

        return upload
    }

}
