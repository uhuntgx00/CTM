package mil.ebs.ctm.parser

import mil.ebs.ctm.FundingSource
import mil.ebs.ctm.ref.RefDateGate;
import mil.ebs.utility.ExcelParserSplit
import mil.ebs.ctm.Account
import mil.ebs.ctm.Address;
import mil.ebs.ctm.Attendee;
import mil.ebs.ctm.Conference
import mil.ebs.ctm.Cost
import mil.ebs.ctm.DateEvent
import mil.ebs.ctm.Organization;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ConferenceParser
        extends ExcelParserSplit<Conference, Attendee>
{

    public void parse(final Workbook pWorkbook, final Date pFileAsOfDate, final Account pAccount) {

        Sheet sheet = pWorkbook.getSheetAt(4)

        boolean checkColumns14 = checkColumns(sheet.getRow(14), "", "NAME", "TD", "ORGANIZATION", "GRADE/RANK", "EMAIL ADDRESS", "PHONE NUMBER",
                "ATTENDANCE TYPE", "JUSTIFICATION FOR ATTENDANCE", "SUPERVISOR")

        Conference conferenceRecord = new Conference()
        String location;

        if (checkColumns14) {
            int sequence = 1;

            for (Row row : sheet) {

                // identifies row in spreadsheet
                int rowNum = row.getRowNum() + 1 // adds 1 due to row count starts at zero

                if (rowNum==2) {
                    Cell a = row.getCell(0)
                    conferenceRecord.setConferenceTitle(parseValue(rowNum, "Title", a, true))
                }
                if (rowNum==3) {
                    Cell a = row.getCell(0)
                    conferenceRecord.setStartDate(parseDate(rowNum, "StartDate", a, "dd-MMM-yyyy", false))
                    print "s:" + conferenceRecord.startDate
                }
                if (rowNum==4) {
                    Cell a = row.getCell(0)
                    conferenceRecord.setEndDate(parseDate(rowNum, "EndDate", a, "dd-MMM-yyyy", false))
                    print "e:" + conferenceRecord.endDate
                }
                if (rowNum==5) {
                    Cell a = row.getCell(0)
                    conferenceRecord.setVenue(parseValue(rowNum, "Venue", a, true))
                }
                if (rowNum==6) {
                    Cell a = row.getCell(0)
                    location = parseValue(rowNum, "Location", a, true)
                }
                if (rowNum==7) {
                    Cell a = row.getCell(0)
                    conferenceRecord.setWebsite(parseValue(rowNum, "WebSite", a, false))
                }
                if (rowNum==8) {
                    Cell a = row.getCell(0)
                    conferenceRecord.setPurpose(parseValue(rowNum, "Purpose", a, false))
                }
                if (rowNum==9) {
                    Cell a = row.getCell(0)

                    conferenceRecord.setHostType("Non-DoD Hosted")
                    conferenceRecord.setNonHostType("Other")

                    String hostType = parseValue(rowNum, "HostType", a, true)

                    if (hostType.equalsIgnoreCase("AFRL-Hosted")) {
                        conferenceRecord.setHostType("AF Hosted")
                        conferenceRecord.setAfrlHosted(true)
                    }
                    if (hostType.equalsIgnoreCase("AF Hosted")) {
                        conferenceRecord.setHostType("AF Hosted")
                    }
                    if (hostType.equalsIgnoreCase("DoD Hosted")) {
                        conferenceRecord.setHostType("DoD Hosted")
                    }
                    if (hostType.equalsIgnoreCase("Non-Gov't Hosted")) {
                        conferenceRecord.setHostType("Non-DoD Hosted")
                        conferenceRecord.setNonHostType("Other")
                    }
                    if (hostType.equalsIgnoreCase("Other Gov't Hosted")) {
                        conferenceRecord.setHostType("Non-DoD Hosted")
                        conferenceRecord.setNonHostType("Other US Govt Hosted")
                    }
                }

                if (rowNum==10) {
                    conferenceRecord.phaseState = "*ERROR*"
                    conferenceRecord.status = "*ERROR*"

                    conferenceRecord.createdDate = new Date()
                    conferenceRecord.createdBy = pAccount
                    conferenceRecord.lastChangeDate = new Date()
                    conferenceRecord.lastChange = pAccount
                    conferenceRecord.statusChangeDate = new Date()
                    conferenceRecord.statusChangedBy = pAccount

                    conferenceRecord.save()

//                    print conferenceRecord.errors

                    if (conferenceRecord.hasErrors()) {
                        return
                    }

                    // create date event associated with the importation of the conference
                    new DateEvent(dateGate: RefDateGate.findByCode("DCI"), eventDate: new Date(), recordedBy: pAccount, conference: conferenceRecord).save(flush: true)

                    if (location) {
                        print location

                        String city = ""
                        String state = ""

                        if (location?.contains(',')) {
                            city = location?.split(',')[0]
                            state = location?.split(',')[1]
                        }
                        new Address(addressType: 'Venue', country: 'usa', city: city, state: state, conference: conferenceRecord).save(flush: true)
                    }

                }

                if (rowNum > 16) {
                    Cell a = row.getCell(0)        // -Empty-
                    Cell b = row.getCell(1)        // Name
                    Cell c = row.getCell(2)        // TD
                    Cell d = row.getCell(3)        // Organization
                    Cell e = row.getCell(4)        // Grade/Rank
                    Cell f = row.getCell(5)        // Email Address
                    Cell g = row.getCell(6)        // Phone Number
                    Cell h = row.getCell(7)        // Attendance Type
                    Cell i = row.getCell(8)        // Justification for Attendance
                    Cell j = row.getCell(9)        // Supervisor

                    Cell k = row.getCell(10)       // Estimated Registration Fee
                    Cell l = row.getCell(11)       // Estimated Airfare
                    Cell m = row.getCell(12)       // Estimated Local Travel
                    Cell n = row.getCell(13)       // Estimated Lodging
                    Cell o = row.getCell(14)       // Estimated Meals
                    Cell p = row.getCell(15)       // Estimated Other
                    Cell q = row.getCell(16)       // Estimated Total
                    Cell r = row.getCell(17)       // Estimated Notes

                    Cell s = row.getCell(18)       // Actual Registration Fee
                    Cell t = row.getCell(19)       // Actual Airfare
                    Cell u = row.getCell(20)       // Actual Local Travel
                    Cell v = row.getCell(21)       // Actual Lodging
                    Cell w = row.getCell(22)       // Actual Meals
                    Cell x = row.getCell(23)       // Actual Other
                    Cell y = row.getCell(24)       // Actual Total
                    Cell z = row.getCell(25)       // Actual Notes

                    String name = parseValue(rowNum, "Name", b, false)
                    String email = parseValue(rowNum, "Email", f, false)

                    if (name) {
                        Attendee attendeeRecord = new Attendee()

                        attendeeRecord.setSequence(sequence++)
                        attendeeRecord.setName(name)

                        String[] partName = name.split(',')
                        if (partName.length > 1) {
                            String firstName = partName[1].trim()
                            String lastName = partName[0].trim()

                            print "Name: " + name
                            print "First: " + firstName
                            print "Last: " + lastName

                            Account accountLookup = Account.findByLastNameIlikeAndFirstNameIlike(lastName, firstName)

                            if (!accountLookup && email) {
                                accountLookup = Account.findByEmailAddress(email)
                            }

                            print "Account: " + accountLookup

                            String supervisor = parseValue(rowNum, "Supervisor", j, false)
                            String[] partSuper = supervisor.split(',')
                            if (partSuper.length > 1) {
                                String firstSuper = partSuper[1].trim()
                                String lastSuper = partSuper[0].trim()

                                print "Supervisor: " + supervisor
                                print "First: " + firstSuper
                                print "Last: " + lastSuper

                                Account superLookup = Account.findByLastNameIlikeAndFirstNameIlike(lastSuper, firstSuper)
                                print "SuperAccount: " + superLookup

                                if (superLookup) {
                                    if (!accountLookup?.supervisor) {
                                        accountLookup?.setSupervisor(superLookup)
                                        accountLookup?.save()
                                    }
                                }
                            }

                            attendeeRecord.setAccountLink(accountLookup)
                        }

                        attendeeRecord.setReservedTD(Organization.findByOfficeSymbol("AFRL/"+parseValue(rowNum, "TD", c, false)))

                        attendeeRecord.setAttendanceType(parseValue(rowNum, "Attendance Type", h, false))
                        if (!attendeeRecord.attendanceType) {
                            attendeeRecord.setAttendanceType("Attendee")
                        }

                        attendeeRecord.setJustification(parseValue(rowNum, "Justification", i, false))
                        attendeeRecord.setStartTravelDate(conferenceRecord.getStartDate() - 1)
                        attendeeRecord.setEndTravelDate(conferenceRecord.getEndDate() + 1)
                        attendeeRecord.setConference(conferenceRecord)
                        attendeeRecord.setStatus("Pending")
                        attendeeRecord.createdDate = new Date()
                        attendeeRecord.createdBy = pAccount
                        attendeeRecord.lastChangeDate = new Date()
                        attendeeRecord.lastChange = pAccount

                        attendeeRecord.save()

                        Cost estimate = new Cost()
                        estimate.costType = "Estimate"
                        estimate.setRegistrationCost(parseDouble(rowNum, "Registration", k, false))
                        estimate.setAirfareCost(parseDouble(rowNum, "Airfare", l, false))
                        estimate.setLocalTravelCost(parseDouble(rowNum, "Local", m, false))
                        estimate.setLodgingCost(parseDouble(rowNum, "Lodging", n, false))
                        estimate.setMealsIncidentalCost(parseDouble(rowNum, "Meals", o, false))
                        estimate.setOtherCost(parseDouble(rowNum, "Other", p, false))
                        estimate.setNotes(parseValue(rowNum, "Notes", r, false))
                        estimate.setAttendee(attendeeRecord)
                        estimate.save flush:true

                        new FundingSource(fundSource: 'US Air Force', percentage: 100, cost: estimate).save(flush: true)

                        Cost actual = new Cost()
                        actual.costType = "Actual"
                        actual.setRegistrationCost(parseDouble(rowNum, "Registration", s, false))
                        actual.setAirfareCost(parseDouble(rowNum, "Airfare", t, false))
                        actual.setLocalTravelCost(parseDouble(rowNum, "Local", u, false))
                        actual.setLodgingCost(parseDouble(rowNum, "Lodging", v, false))
                        actual.setMealsIncidentalCost(parseDouble(rowNum, "Meals", w, false))
                        actual.setOtherCost(parseDouble(rowNum, "Other", x, false))
                        actual.setNotes(parseValue(rowNum, "Notes", z, false))
                        actual.setAttendee(attendeeRecord)
                        actual.save flush:true

                        new FundingSource(fundSource: 'US Air Force', percentage: 100, cost: actual).save(flush: true)
                    }
                }

                // skip first row (header) and checks if any data is present
                if (rowNum != 1) {
                    // only include records with all values present
                    if (rowHasErrors(rowNum)) {
                        // counts rows with errors (rows can have multiple errors)
                        incErrorRows()
                    } else {
                        // adds only valid rows to list
//                        addList1(conferenceRecord)
//                        addList2(new Attendee())

                        // counts rows containing valid data
                        incValidRows()
                    }
                } else {
                    if (rowNum != 1) {
                        incBlankRows()
                    }
                }
            }
        }
    }






}