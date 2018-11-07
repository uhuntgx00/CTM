package mil.ebs.ctm.parser;

import mil.ebs.utility.ExcelParserSplit;
import mil.ebs.ctm.Account;
import mil.ebs.ctm.Attendee;
import mil.ebs.ctm.Conference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Date;

public class ConferenceParserOld
        extends ExcelParserSplit<Conference, Attendee>
{

    public void parse(final Workbook pWorkbook, final Date pFileAsOfDate, final Account pAccount) {

        Sheet sheet = pWorkbook.getSheetAt(4);

        boolean checkColumns14 = checkColumns(sheet.getRow(14), "", "NAME", "TD", "ORGANIZATION", "GRADE/RANK", "EMAIL ADDRESS", "PHONE NUMBER",
                "ATTENDANCE TYPE", "JUSTIFICATION FOR ATTENDANCE", "SUPERVISOR");

        Conference conferenceRecord = new Conference();

        if (checkColumns14) {
            for (Row row : sheet) {

                // identifies row in spreadsheet
                int rowNum = row.getRowNum() + 1; // adds 1 due to row count starts at zero

                if (rowNum==2) {
                    Cell a = row.getCell(0);
                    conferenceRecord.setConferenceTitle(parseValue(rowNum, "Title", a, true));
                }
                if (rowNum==3) {
                    Cell a = row.getCell(0);
                    conferenceRecord.setStartDate(parseDate(rowNum, "StartDate", a, "mm/dd/yyyy", false));
                }
                if (rowNum==4) {
                    Cell a = row.getCell(0);
                    conferenceRecord.setEndDate(parseDate(rowNum, "EndDate", a, "mm/dd/yyyy", false));
                }
                if (rowNum==5) {
                    Cell a = row.getCell(0);
                    conferenceRecord.setVenue(parseValue(rowNum, "Venue", a, true));
                }
                if (rowNum==7) {
                    Cell a = row.getCell(0);
                    conferenceRecord.setWebsite(parseValue(rowNum, "WebSite", a, false));
                }
                if (rowNum==9) {
                    Cell a = row.getCell(0);
                    conferenceRecord.setHostType(parseValue(rowNum, "HostType", a, true));
                }

                Attendee attendeeRecord = new Attendee();

                if (rowNum > 14) {
                    Cell a = row.getCell(0);        // -Empty-
                    Cell b = row.getCell(1);        // Name
                    Cell c = row.getCell(2);        // TD
                    Cell d = row.getCell(3);        // Organization
                    Cell e = row.getCell(4);        // Grade/Rank
                    Cell f = row.getCell(5);        // Email Address
                    Cell g = row.getCell(6);        // Phone Number
                    Cell h = row.getCell(7);        // Attendance Type
                    Cell i = row.getCell(8);        // Justification for Attendance
                    Cell j = row.getCell(9);        // Supervisor

                    Cell k = row.getCell(10);       // Estimated Registration Fee
                    Cell l = row.getCell(11);       // Estimated Airfare
                    Cell m = row.getCell(12);       // Estimated Local Travel
                    Cell n = row.getCell(13);       // Estimated Lodging
                    Cell o = row.getCell(14);       // Estimated Meals
                    Cell p = row.getCell(15);       // Estimated Other
                    Cell q = row.getCell(16);       // Estimated Total
                    Cell r = row.getCell(17);       // Estimated Notes

                    Cell s = row.getCell(18);       // Actual Registration Fee
                    Cell t = row.getCell(19);       // Actual Airfare
                    Cell u = row.getCell(20);       // Actual Local Travel
                    Cell v = row.getCell(21);       // Actual Lodging
                    Cell w = row.getCell(22);       // Actual Meals
                    Cell x = row.getCell(23);       // Actual Other
                    Cell y = row.getCell(24);       // Actual Total
                    Cell z = row.getCell(25);       // Actual Notes

                }

                // skip first row (header) and checks if any data is present
                if (rowNum != 1 && isPopulatedRow(27, row)) {

//                    Ratee rateeRecord = new Ratee();
//
//                    rateeRecord.setMilPdsLastName(parseValue(rowNum, "LAST NAME MAX", c, true));
//                    rateeRecord.setMilPdsFirstName(parseValue(rowNum, "FIRST NAME MAX", d, true));
//                    rateeRecord.setMilPdsMiddleName(parseValue(rowNum, "MIDDLE NAMES MAX", e, false));
//                    rateeRecord.setMilPdsSuffixName(parseValue(rowNum, "SUFFIX MAX", f, false));
//                    rateeRecord.setMilPdsPrefixName("");
//                    rateeRecord.setMilPdsGrade((RefRankGrade) eService.findRankGradeByGrade(parseValue(rowNum, "RANK", a, true)));
//                    rateeRecord.setGrade((RefRankGrade) eService.findRankGradeByGrade(parseValue(rowNum, "RANK", a, true)));
//                    rateeRecord.setSsn_PII(parseValue(rowNum, "SSAN", b, true));
//                    rateeRecord.setMilPdsOfficeSymbol((Organization) eService.findOrganizationByOfficeSymbol(parseValue(rowNum, "OFFICE SYMBOL", g, false)));
//                    rateeRecord.setMilPdsAssignedPAS((Pascode) eService.findPascodeByPASCode(parseValue(rowNum, "PAS", h, true)));
//                    rateeRecord.setMilPdsDafsc(parseValue(rowNum, "DAFSC", k, true));
//                    rateeRecord.setMilPdsDutyTitle(parseValue(rowNum, "CURRENT DUTY TITLE", l, false));
//                    rateeRecord.setFileType((RefFileType) eService.findFileTypeByAbbr(parseValue(rowNum, "FILE TYPE", j, false)));
//                    rateeRecord.setMilPdsFileType((RefFileType) eService.findFileTypeByAbbr(parseValue(rowNum, "FILE TYPE", j, false)));
//                    rateeRecord.setMemberStatus((RefMemberStatus) eService.findMemberStatusByDescription("MilPDS Loaded"));
//                    rateeRecord.setMilPdsAsOfDate(pFileAsOfDate);
//                    rateeRecord.setMilPdsUpdateFlag(true);
//                    rateeRecord.setLastUpdateDate(new Date());

//                    Eval evalRecord = new Eval();
//
//                    evalRecord.setRatee(rateeRecord);
//                    evalRecord.setMilPdsStartDate(parseDate(rowNum, "EVALUATION START DATE", o, "dd-MMM-yy", false));
//                    evalRecord.setMilPdsProjectedCloseDate(parseDate(rowNum, "EVALUATION CLOSE DATE", p, "dd-MMM-yy", false));
//                    evalRecord.setMilPdsProjectedReason((RefReasonCode) eService.findReasonCodeByCode(parseValue(rowNum, "EVALUATION REASON", r, false)));
//                    evalRecord.setProjectedReason((RefReasonCode) eService.findReasonCodeByCode(parseValue(rowNum, "EVALUATION REASON", r, false)));
//
//                    String supervisor = parseValue(rowNum, "SUPV_NAME", s, false).trim().toUpperCase();
//                    supervisor = supervisor.replace("MR ", "");
//                    String svLastName = parseValue(rowNum, "SUPERVISOR LAST NAME", t, false).trim().toUpperCase();
//                    String svFirstName = parseValue(rowNum, "SUPERVISOR FIRST NAME", u, false).trim().toUpperCase();

//                    evalRecord.setStatus("Projected");
//                    evalRecord.setStatusChange(new Date());
//                    evalRecord.setMilPdsAsOfDate(new Date());
//                    evalRecord.setLastUpdateDate(new Date());
//                    evalRecord.setMilPdsUpdateFlag(true);

                    // only include records with all values present
                    if (rowHasErrors(rowNum)) {
                        // counts rows with errors (rows can have multiple errors)
                        incErrorRows();
                    } else {
                        // adds only valid rows to list
                        addList1(conferenceRecord);
                        addList2(attendeeRecord);

                        // counts rows containing valid data
                        incValidRows();
                    }
                } else {
                    if (rowNum != 1) {
                        incBlankRows();
                    }
                }
            }
        }
    }






}