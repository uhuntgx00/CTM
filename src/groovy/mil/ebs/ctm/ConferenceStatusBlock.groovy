package mil.ebs.ctm

class ConferenceStatusBlock {

    int attendeeCount
    Long numAttendees
    int commentCount
    int datesCount
    int fileCount
    int days

    int constrainedCount
    int unconstrainedCount
    int otherCount
    int constrainedActualCount
    int contractorActualCount

    double constrainedTotal
    double unconstrainedTotal
    double otherEstimateTotal
    double constrainedActualTotal
    double contractorActualTotal

    Set<String> tdList = new HashSet<String>()
    Map<String, Long> tdCount = new HashMap<String, Long>()
    Map<String, Double> tdTotal = new HashMap<String, Double>()

}
