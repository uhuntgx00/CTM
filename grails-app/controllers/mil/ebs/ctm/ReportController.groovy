package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured
//import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
//import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import static org.springframework.http.HttpStatus.NOT_FOUND

@Secured(["ROLE_REPORTS"])
class ReportController {

//    def jasperService
//    def springSecurityService
//
//    def index() { }
//
//    def conferences() {
//        def reportDef = new JasperReportDef(name: 'conferences', fileFormat: JasperExportFormat.PPTX_FORMAT)
//
//        response.setContentType("applcation-xdownload")
//        response.setHeader("Content-Disposition", "attachment; filename=Conferences.pptx")
//        response.getOutputStream() << new ByteArrayInputStream(jasperService.generateReport(reportDef).toByteArray())
//    }
//
//    def conferenceCost() {
//        def reportDef = new JasperReportDef(name: 'conferenceCost', fileFormat: JasperExportFormat.PPTX_FORMAT)
//
//        response.setContentType("applcation-xdownload")
//        response.setHeader("Content-Disposition", "attachment; filename=ConferenceCosts.pptx")
//        response.getOutputStream() << new ByteArrayInputStream(jasperService.generateReport(reportDef).toByteArray())
//    }
//
//    def attendanceType() {
//        def reportDef = new JasperReportDef(name: 'attendanceType', fileFormat: JasperExportFormat.PPTX_FORMAT)
//
//        response.setContentType("applcation-xdownload")
//        response.setHeader("Content-Disposition", "attachment; filename=AttendanceType.pptx")
//        response.getOutputStream() << new ByteArrayInputStream(jasperService.generateReport(reportDef).toByteArray())
//    }
//
//    def attendees() {
//        def reportDef = new JasperReportDef(name: 'attendees', fileFormat: JasperExportFormat.PPTX_FORMAT)
//
//        response.setContentType("applcation-xdownload")
//        response.setHeader("Content-Disposition", "attachment; filename=Attendees.pptx")
//        response.getOutputStream() << new ByteArrayInputStream(jasperService.generateReport(reportDef).toByteArray())
//    }
//
//    /**
//     *
//     * @param max
//     * @return
//     */
//    @Secured(['ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN'])
//    def attendanceChart(Integer max) {
//        // if not logged-in render NotFound?
//        if (!springSecurityService.isLoggedIn()) {
//            notFound()
//            return
//        }
//
//        params.max = Math.min(max ?: 25, 100)
//        List<Conference> conferenceList = Conference.list()
//
//        AttendanceChartData acd = new AttendanceChartData()
//        acd.computeChartData(conferenceList)
//
//        respond conferenceList, view: 'chartA', model: [conferenceInstanceCount: Conference.count(), acd: acd, listType: 'Attendance Type']
//    }
//
//    /**
//     *
//     * @param max
//     * @return
//     */
//    @Secured(['ROLE_ADMIN', 'ROLE_TD_ADMIN', 'ROLE_TD_FULL_ADMIN', 'ROLE_FMC_ADMIN'])
//    def costChart(Integer max) {
//        // if not logged-in render NotFound?
//        if (!springSecurityService.isLoggedIn()) {
//            notFound()
//            return
//        }
//
//        params.max = Math.min(max ?: 25, 100)
//        List<Conference> conferenceList = Conference.list()
//
//        CostChartData ccd = new CostChartData()
//        ccd.computeChartData(conferenceList)
//
//        respond conferenceList, view: 'chartB', model: [conferenceInstanceCount: Conference.count(), ccd: ccd, listType: 'Cost Type']
//    }
//
//
//    /**
//     *
//     */
//    protected void notFound() {
//        request.withFormat {
//            form {
//                flash.message = message(code: 'default.not.found.message', args: [message(code: 'conferenceInstance.label', default: 'Conference'), params.id])
//                redirect action: "index", method: "GET"
//            }
//            '*' { render status: NOT_FOUND }
//        }
//    }


}
