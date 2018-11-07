package mil.ebs.ctm

class CostChartData {

    double attendeeCost = 0.0
    double boothCost = 0.0
    double panelCost = 0.0
    double chairCost = 0.0
    double speakerCost = 0.0
    double supportCost = 0.0
    double otherCost = 0.0

    double totalCost = 0.0

//**************************************************************
// Attendee, Booth/Display, Discussion Panel,
// Session Chair, Presenter/Speaker, Support, Other
//**************************************************************
    List<Double> costList = new ArrayList<Double>()


    public void computeChartData(final List<Conference> pConferences) {
        for (conference in pConferences) {
            for (attendee in conference?.attendees) {
                switch (attendee?.attendanceType) {
                    case 'Attendee':
                        if (attendee?.estimateTotal()) {
                            attendeeCost += attendee?.estimateTotal()
                            totalCost += attendee?.estimateTotal()
                        }
                        break

                    case 'Booth/Display':
                        if (attendee?.estimateTotal()) {
                            boothCost += attendee?.estimateTotal()
                            totalCost += attendee?.estimateTotal()
                        }
                        break

                    case 'Discussion Panel':
                        if (attendee?.estimateTotal()) {
                            panelCost += attendee?.estimateTotal()
                            totalCost += attendee?.estimateTotal()
                        }
                        break

                    case 'Session Chair':
                        if (attendee?.estimateTotal()) {
                            chairCost += attendee?.estimateTotal()
                            totalCost += attendee?.estimateTotal()
                        }
                        break

                    case 'Presenter/Speaker':
                        if (attendee?.estimateTotal()) {
                            speakerCost += attendee?.estimateTotal()
                            totalCost += attendee?.estimateTotal()
                        }
                        break

                    case 'Support':
                        if (attendee?.estimateTotal()) {
                            supportCost += attendee?.estimateTotal()
                            totalCost += attendee?.estimateTotal()
                        }
                        break

                    case 'Other':
                        if (attendee?.estimateTotal()) {
                            otherCost += attendee?.estimateTotal()
                            totalCost += attendee?.estimateTotal()
                        }
                        break
                }
            }
        }

        costList.add(attendeeCost)
        costList.add(boothCost)
        costList.add(panelCost)
        costList.add(chairCost)
        costList.add(speakerCost)
        costList.add(supportCost)
        costList.add(otherCost)
    }

}
