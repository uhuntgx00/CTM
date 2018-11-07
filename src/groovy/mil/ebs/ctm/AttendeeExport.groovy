package mil.ebs.ctm

class AttendeeExport {

    Long id

    String conference
    String td
    String organization
    String td_org
    int sequence
    String status
    String account
    String name
    String attendanceType
    Date startTravelDate
    Date endTravelDate
    String startTravelDateStr
    String endTravelDateStr
    String supervisor
    String justification
    String rankGrade

    // estimate costs
    double est_registrationCost = 0.0
    double est_airfareCost = 0.0
    double est_localTravelCost = 0.0
    double est_lodgingCost = 0.0
    double est_lodgingCostTax = 0.0
    double est_mealsIncidentalCost = 0.0
    double est_otherCost = 0.0
    String est_notes

    double est_totalAf = 0.0
    double est_totalOther = 0.0
    double est_totalNon = 0.0

    // actual costs
    double act_registrationCost = 0.0
    double act_airfareCost = 0.0
    double act_localTravelCost = 0.0
    double act_lodgingCost = 0.0
    double act_lodgingCostTax = 0.0
    double act_mealsIncidentalCost = 0.0
    double act_otherCost = 0.0
    String act_notes

    double act_totalAf = 0.0
    double act_totalOther = 0.0
    double act_totalNon = 0.0

}
