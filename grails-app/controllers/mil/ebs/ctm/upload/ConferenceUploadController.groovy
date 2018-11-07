package mil.ebs.ctm.upload

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import mil.ebs.ctm.parser.ConferenceParser
import mil.ebs.utility.ExcelParserSplit
import mil.ebs.ctm.Account
import org.apache.poi.POIXMLException
import org.apache.poi.openxml4j.exceptions.InvalidFormatException
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.web.multipart.commons.CommonsMultipartFile

@Transactional(readOnly = true)
@Secured(["permitAll"])
//@Secured(['ROLE_DEVELOPER, ROLE_ADMIN'])
class ConferenceUploadController {

    def springSecurityService


    def index = { }

    @Transactional
    def save() {
        try {
            //get uploaded file
            CommonsMultipartFile file = request.getFile("file")
            InputStream fileStream = file.inputStream
            OPCPackage pkg = OPCPackage.open(fileStream)
            Workbook workbook = new XSSFWorkbook(pkg)
            fileStream.close()
            pkg.close()

            // get count of data rows (minus header) - assumed file only has 1 sheet
            int rowCount = workbook.getSheetAt(4).getLastRowNum()

            // set as-of-date of file (determined by selected date)
//            def fileAsOfDate = new Date(request.getParameter("asOfDate_value"))
            def fileAsOfDate = new Date()

            def account = Account.get(springSecurityService.principal.id)

            // parser
            ExcelParserSplit parser = new ConferenceParser()
            parser.parse(workbook, null, account)

            // captures meta data
            FileUpload upload = new FileUpload()
            upload.setFileName(file.getFileItem().name.substring(file.getFileItem().name.lastIndexOf("\\") + 1))
            upload.setFileDate(new Date())
            upload.setLineCount(parser.getValidRowCount())
            upload.setFileType("ConferenceXLS")
            upload.setStatus("Init")

            // add the current user's name here...
            upload.loadedBy = (String) session.getAttribute("SPRING_SECURITY_LAST_USERNAME") != null ? (String) session.getAttribute("SPRING_SECURITY_LAST_USERNAME") : "Localhost"
            upload.save()
            upload.finalize()

            // gets list of valid records
//            List<Conference> conferenceList = parser.getConferences()
//            List<Attendee> attendeeList = parser.getAttendees()

            // get list of encounter parsing errors
            List<String> errorList = parser.getFileErrorList()

            // check if errors were encountered
            if (parser.fileHasErrors()) {
                // data was not loaded
                upload.setStatus("Errors")
                if (parser.getValidRowCount() == 0) {
                    flash.message = "Improper File Format: No valid data found"
                } else {
                    flash.message = "Encountered Errors: No Data has been loaded"
                }
            } else {
//                for (conference in conferenceList) {
//                    if (!item.save()) {
//                        item.errors.each {
//                            println it
//                        }
//                    }
//                }

                // save new records
//                for (attendee in attendeeList) {
//                    if (!item.save()) {
//                        item.errors.each {
//                            println it
//                        }
//                    }
//                }

                // data was loaded
                upload.setStatus("Loaded")
            }

            // save load status
            upload.save()

            [upload: upload, totalRows: rowCount, blankRows: parser.getBlankRowCount(), validDataRows: parser.getValidRowCount(), errorRows: parser.getErrorRowCount(), errors: errorList, fileAsOfDate: fileAsOfDate]
        } catch (IllegalArgumentException ignored) {
            flash.message = "No 'File Valid As Of' date selected"
            redirect action: index
            return
        } catch (FileNotFoundException ignored) {
            flash.message = "Selected File does not exist"
            redirect action: index
            return
        } catch (InvalidFormatException ignored) {
            flash.message = "Selected File is not an Excel File"
            redirect action: index
            return
        } catch (IOException ignored) {
            flash.message = "Invalid File select"
            redirect action: index
            return
        } catch (POIXMLException ignored) {
            flash.message = "Selected File is not an Excel File"
            redirect action: index
            return
        }
    }

}
