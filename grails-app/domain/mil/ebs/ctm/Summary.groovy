package mil.ebs.ctm

import mil.ebs.ctm.Conference
import mil.ebs.ctm.OrgSummary

class Summary {

    String summaryType = 'Current'
    Date summaryDate

    Double constrainedTotal = 0.0
    Double unconstrainedTotal = 0.0
    Double otherCostTotal = 0.0
    Double actualTotal = 0.0
    Double ctrActualTotal = 0.0

    int constrainedCount = 0
    int unconstrainedCount = 0
    int otherCostCount = 0
    int actualCount = 0
    int ctrActualCount = 0

    Double attendeeTotal = 0.0
    Double boothTotal = 0.0
    Double chairTotal = 0.0
    Double panelTotal = 0.0
    Double presenterTotal = 0.0
    Double supportTotal = 0.0
    Double otherTotal = 0.0

    int attendeeCount = 0
    int boothCount = 0
    int chairCount = 0
    int panelCount = 0
    int presenterCount = 0
    int supportCount = 0
    int otherCount = 0

    Double ctrTotal = 0.0
    Double extTotal = 0.0

    int ctrCount = 0
    int extCount = 0
    int pendingCount = 0
    int waitlistCount = 0


    static belongsTo = [conference: Conference]
    static hasMany = [orgSummaries: OrgSummary]

    static constraints = {
        summaryType inList: ['Current', 'Baseline', 'Before', 'After', 'Actual'], blank: false, nullable: false, maxSize: 30
        summaryDate blank: true, nullable: true

        constrainedTotal blank: false, nullable: false, min: 0.0D
        unconstrainedTotal blank: false, nullable: false, min: 0.0D
        otherCostTotal blank: false, nullable: false, min: 0.0D
        actualTotal blank: false, nullable: false, min: 0.0D
        ctrActualTotal blank: false, nullable: false, min: 0.0D

        constrainedCount blank: false, nullable: false, min: 0
        unconstrainedCount blank: false, nullable: false, min: 0
        otherCostCount blank: false, nullable: false, min: 0
        actualCount blank: false, nullable: false, min: 0
        ctrActualCount blank: false, nullable: false, min: 0

        attendeeTotal blank: false, nullable: false, min: 0.0D
        boothTotal blank: false, nullable: false, min: 0.0D
        chairTotal blank: false, nullable: false, min: 0.0D
        panelTotal blank: false, nullable: false, min: 0.0D
        presenterTotal blank: false, nullable: false, min: 0.0D
        supportTotal blank: false, nullable: false, min: 0.0D
        otherTotal blank: false, nullable: false, min: 0.0D

        attendeeCount blank: false, nullable: false, min: 0
        boothCount blank: false, nullable: false, min: 0
        chairCount blank: false, nullable: false, min: 0
        panelCount blank: false, nullable: false, min: 0
        presenterCount blank: false, nullable: false, min: 0
        supportCount blank: false, nullable: false, min: 0
        otherCount blank: false, nullable: false, min: 0

        ctrTotal blank: false, nullable: false, min: 0.0D
        extTotal blank: false, nullable: false, min: 0.0D

        ctrCount blank: false, nullable: false, min: 0
        extCount blank: false, nullable: false, min: 0
        pendingCount blank: false, nullable: false, min: 0
        waitlistCount blank: false, nullable: false, min: 0
    }

    static mapping = {
        version false
        summaryDate type:'java.sql.Date'
        orgSummaries cascade: 'all-delete-orphan'
    }
}
