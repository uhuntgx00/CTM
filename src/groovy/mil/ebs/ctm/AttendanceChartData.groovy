package mil.ebs.ctm

class AttendanceChartData {

    int attendeeCount = 0
    int boothCount = 0
    int panelCount = 0
    int chairCount = 0
    int speakerCount = 0
    int supportCount = 0
    int otherCount = 0

    int totalCount = 0

//**************************************************************
// Attendee, Booth/Display, Discussion Panel,
// Session Chair, Presenter/Speaker, Support, Other
//**************************************************************
    List<Integer> countList = new ArrayList<Integer>()


    public void computeChartData(final List<Conference> pConferences) {
        for (conference in pConferences) {
            for (attendee in conference?.attendees) {
                totalCount++
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
        }

        countList.add(attendeeCount)
        countList.add(boothCount)
        countList.add(panelCount)
        countList.add(chairCount)
        countList.add(speakerCount)
        countList.add(supportCount)
        countList.add(otherCount)
    }

}
