package mil.ebs.ctm

class Cost {

    String costType
    String zeroRegistrationReason
    double registrationCost

    double airfareCost
    String zeroAirfareReason
    String airfareProvider

    double localTravelCost
    String localTravelProvider

    double lodgingCost
    double lodgingCostTax
    String zeroLodgingReason
    String lodgingProvider
    Address lodgingAddress
    boolean lodgingExceedsPerdiem = false

    double mealsIncidentalCost
    String zeroMealsReason
    boolean mealsExceedsPerdiem = false

    double otherCost
    String otherDescription

    String notes


    static belongsTo = [attendee: Attendee]
    static hasMany = [fundSources: FundingSource]

    static searchable = true

    static constraints = {
        costType inList: ['Estimate', 'Actual']

        registrationCost blank: false, nullable: false, min: 0.0D
        zeroRegistrationReason blank: true, nullable: true

        airfareCost blank: false, nullable: false, min: 0.0D
        zeroAirfareReason blank: true, nullable: true
        airfareProvider blank: true, nullable: true

        localTravelCost blank: true, nullable: true, min: 0.0D
        localTravelProvider blank: true, nullable: true

        lodgingCost blank: false, nullable: false, min: 0.0D
        lodgingCostTax blank: true, nullable: true, min: 0.0D
        zeroLodgingReason blank: true, nullable: true
        lodgingProvider blank: true, nullable: true
        lodgingAddress blank: true, nullable: true
        lodgingExceedsPerdiem blank: false, nullable: false

        mealsIncidentalCost blank: false, nullable: false, min: 0.0D
        zeroMealsReason blank: true, nullable: true
        mealsExceedsPerdiem blank: false, nullable: false

        otherCost blank: true, nullable: true, min: 0.0D
        otherDescription blank: true, nullable: true

        notes blank: true, nullable: true, maxSize: 2000
    }

    static mapping = {
        version false
    }

    String toString() {
        return "${costType}"
    }

// --------------------------------------------
// cost functions
// --------------------------------------------

    /**
     *
     * @return double - total costs entered into each cost appropriate field
     */
    double total() {
        return registrationCost + airfareCost + localTravelCost + lodgingCost + lodgingCostTax + mealsIncidentalCost + otherCost
    }

    /**
     * Funding Sources:
     *      'US Air Force'
     *      'Other US Govt'
     *      'Non-Federal Entity'
     *
     * @return Map<String, Double> - total by funding
     */
    Map<String, Double> totalByFunding() {
        Map<String, Double> result = new HashMap<String, Double>()

        if (fundSources) {
            for (fundingSource in fundSources) {
                result.put(fundingSource.fundSource, ((total() * fundingSource.percentage)/100))
            }
        } else {
            // ASSUME 100% 'US Air Force'
            result.put('US Air Force', total())
        }

        return result
    }



}
