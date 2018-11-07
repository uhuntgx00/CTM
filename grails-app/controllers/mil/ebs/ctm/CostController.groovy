package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["permitAll"])
class CostController {

    def summaryService


    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     *
     * @param max (Integer)
     * @return
     */
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Cost.list(params), model: [costInstanceCount: Cost.count()]
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    def show(final Cost pCostInstance) {
        CostCalculator calc = calculateCosts(pCostInstance)

        if (calc.updateCost) {
            pCostInstance.save flush: true
        }

        String fundSource1 = FundingSource.findByCostAndFundSource(pCostInstance, 'Other US Govt')?.percentage
        String fundSource2 = FundingSource.findByCostAndFundSource(pCostInstance, 'US Air Force')?.percentage
        String fundSource3 = FundingSource.findByCostAndFundSource(pCostInstance, 'Non-Federal Entity')?.percentage

        if (!fundSource1 && !fundSource2 && !fundSource3) {
            fundSource1 = "0"
            fundSource2 = "100"
            fundSource3 = "0"
        }

        respond pCostInstance, model: [fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3, calc: calc]
    }

    /**
     *
     * @return
     */
    def create() {
        Cost costInstance = new Cost(params)
        CostCalculator calc = calculateCosts(costInstance)

        respond costInstance, model: [calc: calc]
    }

    /**
     *
     * @return
     */
    def createEstimateCost() {
        Attendee attendee = Attendee.get(params.get("attendee.id"))

        Cost costInstance = new Cost(params)
        CostCalculator calc = calculateCosts(costInstance)

        costInstance?.lodgingCost = calc.lodgingPerDiemCost
        costInstance?.mealsIncidentalCost = calc.mealPerDiemCost
        costInstance?.lodgingProvider = attendee?.conference?.venue

//        String fundSource1 = FundingSource.findByCostAndFundSource(costInstance, 'Other US Govt')?.percentage
//        String fundSource2 = FundingSource.findByCostAndFundSource(costInstance, 'US Air Force')?.percentage
//        String fundSource3 = FundingSource.findByCostAndFundSource(costInstance, 'Non-Federal Entity')?.percentage

//        if (!fundSource1 && !fundSource2 && !fundSource3) {
        String fundSource1 = "0"
        String fundSource2 = "100"
        String fundSource3 = "0"
//        }

        respond costInstance, model: [fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3, calc: calc]
    }

    /**
     *
     * @return
     */
    def createActualCost() {
        Cost costInstance = new Cost(params)

        String fundSource1 = "0"
        String fundSource2 = "100"
        String fundSource3 = "0"

        // fill actual with estimated costs allowing attendee to override and change
        for (cost in costInstance?.attendee?.costs) {
            if (cost?.costType?.equalsIgnoreCase("Estimate")) {
                costInstance?.zeroRegistrationReason = cost?.zeroRegistrationReason
                costInstance?.registrationCost = cost?.registrationCost
                costInstance?.airfareCost = cost?.airfareCost
                costInstance?.zeroAirfareReason = cost?.zeroAirfareReason
                costInstance?.airfareProvider = cost?.airfareProvider
                costInstance?.localTravelCost = cost?.localTravelCost
                costInstance?.localTravelProvider = cost?.localTravelProvider
                costInstance?.lodgingCost = cost?.lodgingCost
                costInstance?.lodgingCostTax = cost?.lodgingCostTax
                costInstance?.zeroLodgingReason = cost?.zeroLodgingReason
                costInstance?.lodgingProvider = cost?.lodgingProvider
                costInstance?.lodgingExceedsPerdiem = cost?.lodgingExceedsPerdiem
                costInstance?.mealsIncidentalCost = cost?.mealsIncidentalCost
                costInstance?.zeroMealsReason = cost?.zeroMealsReason
                costInstance?.mealsExceedsPerdiem = cost?.mealsExceedsPerdiem
                costInstance?.otherCost = cost?.otherCost
                costInstance?.otherDescription = cost?.otherDescription

                fundSource1 = FundingSource.findByCostAndFundSource(cost, 'Other US Govt')?.percentage
                fundSource2 = FundingSource.findByCostAndFundSource(cost, 'US Air Force')?.percentage
                fundSource3 = FundingSource.findByCostAndFundSource(cost, 'Non-Federal Entity')?.percentage
            }
        }

        if (!fundSource1 && !fundSource2 && !fundSource3) {
            fundSource1 = "0"
            fundSource2 = "100"
            fundSource3 = "0"
        }

        CostCalculator calc = calculateCosts(costInstance)

        respond costInstance, model: [fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3, calc: calc]
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    @Transactional
    def save(final Cost pCostInstance) {
        if (pCostInstance == null) {
            notFound()
            return
        }

        if (pCostInstance.hasErrors()) {
            CostCalculator calc = calculateCosts(pCostInstance)

//            String fundSource1 = FundingSource.findByCostAndFundSource(pCostInstance, 'Other US Govt')?.percentage
//            String fundSource2 = FundingSource.findByCostAndFundSource(pCostInstance, 'US Air Force')?.percentage
//            String fundSource3 = FundingSource.findByCostAndFundSource(pCostInstance, 'Non-Federal Entity')?.percentage
//
//            if (!fundSource1 && !fundSource2 && !fundSource3) {
                String fundSource1 = "0"
                String fundSource2 = "100"
                String fundSource3 = "0"
//            }

            respond pCostInstance.errors, view: 'create', model: [calc: calc, fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3]
            return
        }

        if (!Cost.findAllByCostTypeAndAttendee(pCostInstance?.costType, pCostInstance?.attendee)) {
            pCostInstance.save flush: true

            saveFunding(pCostInstance, params.fundSource1a, params.fundSource2a, params.fundSource3a)
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pCostInstance?.attendee?.conference)

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'costInstance.label', default: 'Cost'), pCostInstance.id])
                redirect pCostInstance
            }
            '*' { respond pCostInstance, [status: CREATED] }
        }
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    @Transactional
    def saveEstimateCost(final Cost pCostInstance) {
        if (pCostInstance == null) {
            notFound()
            return
        }

        checkForZeroReasonErrors(pCostInstance)

        if (pCostInstance.hasErrors()) {
            CostCalculator calc = calculateCosts(pCostInstance)

//            String fundSource1 = FundingSource.findByCostAndFundSource(pCostInstance, 'Other US Govt')?.percentage
//            String fundSource2 = FundingSource.findByCostAndFundSource(pCostInstance, 'US Air Force')?.percentage
//            String fundSource3 = FundingSource.findByCostAndFundSource(pCostInstance, 'Non-Federal Entity')?.percentage
//
//            if (!fundSource1 && !fundSource2 && !fundSource3) {
                String fundSource1 = "0"
                String fundSource2 = "100"
                String fundSource3 = "0"
//            }

            respond pCostInstance.errors, view: 'createEstimateCost', model: [calc: calc, fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3]
            return
        }

        clearZeroReasons(pCostInstance)

        pCostInstance.costType = "Estimate"

        if (!Cost.findAllByCostTypeAndAttendee("Estimate", pCostInstance.attendee)) {
            pCostInstance.save flush: true

            saveFunding(pCostInstance, params.fundSource1a, params.fundSource2a, params.fundSource3a)

            def attendee = Attendee.get(Integer.parseInt(params.get('attendee.id')))
            attendee.costs.add(pCostInstance)
            attendee.save flush:true
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pCostInstance?.attendee?.conference)

        request.withFormat {
            form {
                redirect controller:"conference", id: pCostInstance?.attendee?.conference?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: pCostInstance?.attendee?.conference?.id, action:"show", method:"GET" }
        }
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    @Transactional
    def saveActualCost(final Cost pCostInstance) {
        if (pCostInstance == null) {
            notFound()
            return
        }

        checkForZeroReasonErrors(pCostInstance)

        if (pCostInstance.hasErrors()) {
            CostCalculator calc = calculateCosts(pCostInstance)

//            String fundSource1 = FundingSource.findByCostAndFundSource(pCostInstance, 'Other US Govt')?.percentage
//            String fundSource2 = FundingSource.findByCostAndFundSource(pCostInstance, 'US Air Force')?.percentage
//            String fundSource3 = FundingSource.findByCostAndFundSource(pCostInstance, 'Non-Federal Entity')?.percentage
//
//            if (!fundSource1 && !fundSource2 && !fundSource3) {
                String fundSource1 = "0"
                String fundSource2 = "100"
                String fundSource3 = "0"
//            }

            respond pCostInstance.errors, view: 'createActualCost', model: [calc: calc, fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3]
            return
        }

        clearZeroReasons(pCostInstance)

        pCostInstance.costType = "Actual"

        if (!Cost.findAllByCostTypeAndAttendee("Actual", pCostInstance.attendee)) {
            pCostInstance.save flush: true

            saveFunding(pCostInstance, params.fundSource1a, params.fundSource2a, params.fundSource3a)

            def attendee = Attendee.get(Integer.parseInt(params.get('attendee.id')))
            attendee.costs.add(pCostInstance)
            attendee.save flush:true
        }

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pCostInstance?.attendee?.conference)

        request.withFormat {
            form {
                redirect controller:"conference", id: pCostInstance?.attendee?.conference?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: pCostInstance?.attendee?.conference?.id, action:"show", method:"GET" }
        }
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    def edit(final Cost pCostInstance) {
        CostCalculator calc = calculateCosts(pCostInstance)
        if (calc?.updateCost) {
            pCostInstance.save flush: true
        }

        String fundSource1 = FundingSource.findByCostAndFundSource(pCostInstance, 'Other US Govt')?.percentage
        String fundSource2 = FundingSource.findByCostAndFundSource(pCostInstance, 'US Air Force')?.percentage
        String fundSource3 = FundingSource.findByCostAndFundSource(pCostInstance, 'Non-Federal Entity')?.percentage

        if (!fundSource1 && !fundSource2 && !fundSource3) {
            fundSource1 = "0"
            fundSource2 = "100"
            fundSource3 = "0"
        }

        respond pCostInstance, model: [fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3, calc: calc]
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    def conferenceEdit(final Cost pCostInstance) {
        CostCalculator calc = calculateCosts(pCostInstance)
        if (calc.updateCost) {
            pCostInstance.save flush: true
        }

        String fundSource1 = FundingSource.findByCostAndFundSource(pCostInstance, 'Other US Govt')?.percentage
        String fundSource2 = FundingSource.findByCostAndFundSource(pCostInstance, 'US Air Force')?.percentage
        String fundSource3 = FundingSource.findByCostAndFundSource(pCostInstance, 'Non-Federal Entity')?.percentage

        if (!fundSource1 && !fundSource2 && !fundSource3) {
            fundSource1 = "0"
            fundSource2 = "100"
            fundSource3 = "0"
        }

        respond pCostInstance, model: [fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3, calc: calc]
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    @Transactional
    def update(final Cost pCostInstance) {
        if (pCostInstance == null) {
            notFound()
            return
        }

        checkForZeroReasonErrors(pCostInstance)

        if (pCostInstance.hasErrors()) {
            CostCalculator calc = calculateCosts(pCostInstance)

            String fundSource1 = FundingSource.findByCostAndFundSource(pCostInstance, 'Other US Govt')?.percentage
            String fundSource2 = FundingSource.findByCostAndFundSource(pCostInstance, 'US Air Force')?.percentage
            String fundSource3 = FundingSource.findByCostAndFundSource(pCostInstance, 'Non-Federal Entity')?.percentage

            if (!fundSource1 && !fundSource2 && !fundSource3) {
                fundSource1 = "0"
                fundSource2 = "100"
                fundSource3 = "0"
            }

            respond pCostInstance.errors, view: 'edit', model: [calc: calc, fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3]
            return
        }

        clearZeroReasons(pCostInstance)

        pCostInstance.save flush: true

        saveFunding(pCostInstance, params.fundSource1a, params.fundSource2a, params.fundSource3a)

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pCostInstance?.attendee?.conference)

        request.withFormat {
            form {
                flash.message = message(code: 'cost.estimateUpdated.message', args: [message(code: 'Cost.label', default: 'Cost'), pCostInstance.id])
                redirect pCostInstance
            }
            '*' { respond pCostInstance, [status: OK] }
        }
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    @Transactional
    def conferenceUpdate(final Cost pCostInstance) {
        if (pCostInstance == null) {
            notFound()
            return
        }

        checkForZeroReasonErrors(pCostInstance)

        if (pCostInstance.hasErrors()) {
            CostCalculator calc = calculateCosts(pCostInstance)

            String fundSource1 = FundingSource.findByCostAndFundSource(pCostInstance, 'Other US Govt')?.percentage
            String fundSource2 = FundingSource.findByCostAndFundSource(pCostInstance, 'US Air Force')?.percentage
            String fundSource3 = FundingSource.findByCostAndFundSource(pCostInstance, 'Non-Federal Entity')?.percentage

            if (!fundSource1 && !fundSource2 && !fundSource3) {
                fundSource1 = "0"
                fundSource2 = "100"
                fundSource3 = "0"
            }

            respond pCostInstance.errors, view: 'conferenceEdit', model: [calc: calc, fundSource1a: fundSource1, fundSource2a: fundSource2, fundSource3a: fundSource3]
            return
        }

        clearZeroReasons(pCostInstance)

        pCostInstance.save flush: true

        saveFunding(pCostInstance, params.fundSource1a, params.fundSource2a, params.fundSource3a)

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pCostInstance?.attendee?.conference)

        request.withFormat {
            form {
                redirect controller:"conference", id: pCostInstance?.attendee?.conference?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: pCostInstance?.attendee?.conference?.id, action:"show", method:"GET" }
        }
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    @Transactional
    def delete(final Cost pCostInstance) {
        if (pCostInstance == null) {
            notFound()
            return
        }

        // remove current funding from attendee slot
        List<FundingSource> fundList = FundingSource.findAllByCost(pCostInstance)
        for (fundingSource in fundList) {
            fundingSource.delete()
        }

        pCostInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Cost.label', default: 'Cost'), pCostInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    @Transactional
    def conferenceDelete(final Cost pCostInstance) {
        if (pCostInstance == null) {
            notFound()
            return
        }

        def conferenceInstance = pCostInstance?.attendee?.conference

        // remove current funding from attendee slot
        List<FundingSource> fundList = FundingSource.findAllByCost(pCostInstance)
        for (fundingSource in fundList) {
            fundingSource.delete()
        }

        pCostInstance.delete flush: true

        request.withFormat {
            form {
                redirect controller:"conference", id: conferenceInstance?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: conferenceInstance?.id, action:"show", method:"GET" }
        }
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    @Transactional
    def deleteCost(final Cost pCostInstance) {

        if (pCostInstance == null) {
            notFound()
            return
        }

        def attendeeId = pCostInstance?.attendee?.id

        Address address = Address.get(pCostInstance?.lodgingAddress?.id)

        pCostInstance.lodgingAddress = null
        pCostInstance.save flush:true

        if (address) {
            address.delete flush:true
        }

        // remove current funding from attendee slot
        List<FundingSource> fundList = FundingSource.findAllByCost(pCostInstance)
        for (fundingSource in fundList) {
            fundingSource.delete()
        }

        pCostInstance.delete flush: true

        // update the "current" summary associated to the conference
        summaryService.maintainSummary(true, "Current", pCostInstance?.attendee?.conference)

        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeId, action:"show", method:"GET"
            }
            '*' { redirect controller:"attendee", id: attendeeId, action:"show", method:"GET" }
        }
    }

    /**
     *
     * @param pCostInstance (Cost) -
     * @return
     */
    @Transactional
    def clearLodging(final Cost pCostInstance) {

        if (pCostInstance == null) {
            notFound()
            return
        }

        Address address = Address.get(pCostInstance?.lodgingAddress?.id)

        pCostInstance.lodgingCost = 0.0
        pCostInstance.lodgingCostTax = 0.0
        pCostInstance.lodgingProvider = ""
        pCostInstance.lodgingAddress = null

        pCostInstance.save flush:true

        if (address) {
            address.delete flush:true
        }

        request.withFormat {
            form {
                redirect controller:"cost", id: pCostInstance?.id, action:"edit", method:"GET"
            }
            '*' { redirect controller:"cost", id: pCostInstance?.id, action:"edit", method:"GET" }
        }
    }

    /**
     *
     */
    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'costInstance.label', default: 'Cost'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    /**
     *
     * @param costInstance (Cost) -
     * @return
     */
    public CostCalculator calculateCosts(final Cost costInstance) {
        CostCalculator calc = new CostCalculator()

        if (costInstance?.attendee?.travelDays() > 0) {
            if (costInstance?.localTravelCost > 0) {
                calc.avgTravelCost = costInstance?.localTravelCost/costInstance?.attendee?.travelDays()
            }

            if (costInstance?.lodgingCost > 0) {
                calc.avgLodgingCost = costInstance?.lodgingCost/costInstance?.attendee?.travelDays()
            }

            if (costInstance?.mealsIncidentalCost > 0) {
                calc.avgMealsCost = costInstance?.mealsIncidentalCost/costInstance?.attendee?.travelDays()
            }
            if (costInstance?.attendee?.conference?.meals) {
                calc.costPerMeal = costInstance?.attendee?.conference?.meals / 3
                if (costInstance?.attendee?.mealsIncluded) {
                    calc.mealsIncludedAdjustment = calc.costPerMeal * costInstance?.attendee?.mealsIncluded
                }

                calc.mealPerDiemCost = costInstance?.attendee?.conference?.meals * costInstance?.attendee?.travelDays()
                boolean hold = costInstance?.mealsExceedsPerdiem
                costInstance?.mealsExceedsPerdiem = costInstance?.mealsIncidentalCost > (calc.mealPerDiemCost - calc.mealsIncludedAdjustment + 0.01)
                if (hold != costInstance?.mealsExceedsPerdiem) {
                    calc.updateCost = true
                }
            }

            if (costInstance?.attendee?.conference?.perdiem) {
                calc.lodgingPerDiemCost = costInstance?.attendee?.conference?.perdiem * costInstance?.attendee?.travelDays()
                boolean hold = costInstance?.lodgingExceedsPerdiem
                costInstance?.lodgingExceedsPerdiem = costInstance?.lodgingCost > (calc.lodgingPerDiemCost + 0.01)
                if (hold != costInstance?.lodgingExceedsPerdiem) {
                    calc.updateCost = true
                }
            }
        }

        return calc
    }

    /**
     *
     * @param pCostInstance (Cost) -
     */
    def checkForZeroReasonErrors(final Cost pCostInstance) {
        if (!pCostInstance?.registrationCost && !pCostInstance?.zeroRegistrationReason) {
            pCostInstance?.errors?.rejectValue('zeroRegistrationReason', 'zero.registrationReason.message', 'A reason is required for a ZERO dollar amount entry for Registration Cost')
        }
        if (!pCostInstance?.airfareCost && !pCostInstance?.zeroAirfareReason) {
            pCostInstance?.errors?.rejectValue('zeroAirfareReason', 'zero.airfareReason.message', 'A reason is required for a ZERO dollar amount entry for Airfare Cost')
        }
        if (!pCostInstance?.lodgingCost && !pCostInstance?.zeroLodgingReason) {
            pCostInstance?.errors?.rejectValue('zeroLodgingReason', 'zero.lodgingReason.message', 'A reason is required for a ZERO dollar amount entry for Lodging Cost')
        }
        if (!pCostInstance?.mealsIncidentalCost && !pCostInstance?.zeroMealsReason) {
            pCostInstance?.errors?.rejectValue('zeroMealsReason', 'zero.mealsReason.message', 'A reason is required for a ZERO dollar amount entry for Meals/Incidental Cost')
        }
    }

    /**
     *
     * @param pCostInstance (Cost) -
     */
    def clearZeroReasons(final Cost pCostInstance) {
        if (pCostInstance?.registrationCost > 0) {
            pCostInstance?.zeroRegistrationReason = null
        }
        if (pCostInstance?.airfareCost > 0) {
            pCostInstance?.zeroAirfareReason = null
        }
        if (pCostInstance?.lodgingCost > 0) {
            pCostInstance?.zeroLodgingReason = null
        }
        if (pCostInstance?.mealsIncidentalCost > 0) {
            pCostInstance?.zeroMealsReason = null
        }
    }

    /**
     * PRIVATE
     * This function saves the funding source for the attendee slot.
     *
     * @param pCostInstance (Cost)
     * @param pFundSource1 (String)
     * @param pFundSource2 (String)
     * @param pFundSource3 (String)
     */
    private void saveFunding(Cost pCostInstance, String pFundSource1, String pFundSource2, String pFundSource3) {
        // remove current funding from attendee slot
        List<FundingSource> fundList = FundingSource.findAllByCost(pCostInstance)
        for (fundingSource in fundList) {
            fundingSource.delete()
        }

//        print "fund1: " + pFundSource1
        if (pFundSource1) {
            if (Integer.parseInt(pFundSource1) > 0) {
                new FundingSource(fundSource: 'Other US Govt', percentage: Integer.parseInt(pFundSource1), cost: pCostInstance).save(flush: true)
            }
        }

//        print "fund2: " + pFundSource2
        if (pFundSource2) {
            if (Integer.parseInt(pFundSource2) > 0) {
                new FundingSource(fundSource: 'US Air Force', percentage: Integer.parseInt(pFundSource2), cost: pCostInstance).save(flush: true)
            }
        }

//        print "fund3: " + pFundSource3
        if (pFundSource3) {
            if (Integer.parseInt(pFundSource3) > 0) {
                new FundingSource(fundSource: 'Non-Federal Entity', percentage: Integer.parseInt(pFundSource3), cost: pCostInstance).save(flush: true)
            }
        }
    }

}
