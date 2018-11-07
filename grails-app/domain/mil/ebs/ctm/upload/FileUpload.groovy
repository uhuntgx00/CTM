package mil.ebs.ctm.upload

import mil.ebs.ctm.Conference

import java.text.SimpleDateFormat

class FileUpload {

    String fileName
    String fileType
    Date fileDate
    String loadedBy
    Integer lineCount
    String status
    Conference conference
    String comments

    static constraints = {
        fileName blank: false
        fileType inlist: ["Package", "CRF", "Agenda", "CAPE", "TD Doc", "Approval Memo", "SAFmemo", "AAR", "ConferenceXLS"]
        fileDate blank: true, nullable: true
        loadedBy blank: false
        lineCount blank: true, nullable: true
        status inList: ["Init", "Errors", "Loaded", "Checked-Out", "Locked"]
        conference blank: true, nullable: true
        comments blank: true, nullable: true
    }

    static mapping = {
        version false
        fileDate type:'java.sql.Date'
    }

    String toString() {
        return "${fileType}: ${fileName} - " + new SimpleDateFormat("dd MMM yyyy").format(getFileDate()) + " (" + getLoadedBy() + ")"
    }

    String toHtmlString() {
        return "<b><i>" + fileType + "</i></b> (" + new SimpleDateFormat("MMMM dd, yyyy").format(getFileDate()) +") - " + fileName + " - " + loadedBy
    }

}
