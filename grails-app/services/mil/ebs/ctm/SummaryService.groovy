package mil.ebs.ctm

import grails.transaction.Transactional

@Transactional
class SummaryService {

    def conferenceService

    /**
     *
     * @param pSummaryType (String) -
     * @param pConferenceInstance (Conference) -
     * @return
     */
    public ConferenceStatusBlock convertToCSB(final String pSummaryType, final Conference pConferenceInstance) {
        ConferenceStatusBlock csb = new ConferenceStatusBlock()

        if (pConferenceInstance?.attendees) {
            csb.attendeeCount = pConferenceInstance?.attendees?.size()
        } else {
            csb.attendeeCount = 0
        }
        csb.numAttendees = pConferenceInstance?.numAttendees
        if (pConferenceInstance?.comments) {
            csb.commentCount = pConferenceInstance?.comments?.size()
        } else {
            csb.commentCount = 0
        }
        if (pConferenceInstance?.getDateEvents()) {
            csb.datesCount = pConferenceInstance?.getDateEvents()?.size()
        } else {
            csb.datesCount = 0
        }
        if (pConferenceInstance?.getFiles()) {
            csb.fileCount = pConferenceInstance?.getFiles()?.size()
        } else {
            csb.fileCount = 0
        }
        csb.days = pConferenceInstance?.days()

        Summary currentSummary = Summary.findByConferenceAndSummaryType(pConferenceInstance, pSummaryType)
        if (currentSummary) {
            Set<String> tdList = new HashSet<String>()
            Map<String, Long> tdCount = new HashMap<String, Long>()
            Map<String, Double> tdTotal = new HashMap<String, Double>()

            for (orgSummary in currentSummary?.orgSummaries) {
                String key = orgSummary?.org?.officeSymbol
                tdList.add(key)
                tdCount.put(key, orgSummary?.constrainedCount)
                tdTotal.put(key, orgSummary?.constrainedTotal)
            }

            if (currentSummary?.extCount) {
                tdList.add("EXT")
                tdCount.put("EXT", currentSummary?.extCount)
                tdTotal.put("EXT", currentSummary?.extTotal)
            }

            csb.setTdList(tdList)
            csb.setTdCount(tdCount)
            csb.setTdTotal(tdTotal)

            csb.setConstrainedTotal(currentSummary.constrainedTotal)
            csb.setUnconstrainedTotal(currentSummary.unconstrainedTotal)
            csb.setOtherEstimateTotal(currentSummary.otherCostTotal)
            csb.setConstrainedActualTotal(currentSummary.actualTotal)
            csb.setContractorActualTotal(currentSummary.ctrActualTotal)

            csb.constrainedCount = currentSummary.constrainedCount
            csb.unconstrainedCount = currentSummary.unconstrainedCount
            csb.otherCount = currentSummary.otherCostCount
            csb.constrainedActualCount = currentSummary.actualCount
            csb.contractorActualCount = currentSummary.ctrActualCount
        } else {
            conferenceService.computeConferenceStatusBlock(csb, pConferenceInstance)

            // update the "current" summary associated to the conference
            maintainSummary(csb, "Current", pConferenceInstance)
        }

        return csb
    }

    /**
     *
     * @param pCSB (ConferenceStatusBlock) -
     * @param pConferenceInstance (Conference) -
     * @param pSummaryType (String) -
     * @return Summary -
     */
    public Summary convertFromCSB(final ConferenceStatusBlock pCSB, final Conference pConferenceInstance, final String pSummaryType) {
        Summary summary = new Summary()

        summary.conference = pConferenceInstance
        summary.summaryType = pSummaryType
        summary.summaryDate = new Date()

        summary.setConstrainedTotal(pCSB.constrainedTotal)
        summary.setUnconstrainedTotal(pCSB.unconstrainedTotal)
        summary.setOtherCostTotal(pCSB.otherEstimateTotal)
        summary.setActualTotal(pCSB.constrainedActualTotal)
        summary.setCtrActualTotal(pCSB.contractorActualTotal)

        summary.constrainedCount = pCSB.constrainedCount
        summary.unconstrainedCount = pCSB.unconstrainedCount
        summary.otherCostCount = pCSB.otherCount
        summary.actualCount = pCSB.constrainedActualCount
        summary.ctrActualCount = pCSB.contractorActualCount

        for (attendee in pConferenceInstance?.attendees) {
            if (attendee?.status?.equalsIgnoreCase("Pending") ||
//                    attendee?.status?.equalsIgnoreCase("Wait List") ||
                    attendee?.status?.equalsIgnoreCase("Approved") ||
                    attendee?.status?.equalsIgnoreCase("Registered") ||
                    attendee?.status?.equalsIgnoreCase("Confirmed") ||
                    attendee?.status?.equalsIgnoreCase("Attended"))
            {
                double estimateTotal = attendee?.estimateTotal()
                int afFunding = attendee?.afFunding()

                switch (attendee?.attendanceType) {
                    case 'Attendee':
                        if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") && !attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                            summary.attendeeTotal += (estimateTotal * afFunding) / 100
                        }
                        summary.attendeeCount++
                        break

                    case 'Booth/Display':
                        if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") && !attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                            summary.boothTotal += (estimateTotal * afFunding) / 100
                        }
                        summary.boothCount++
                        break

                    case 'Discussion Panel':
                        if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") && !attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                            summary.panelTotal += (estimateTotal * afFunding) / 100
                        }
                        summary.panelCount++
                        break

                    case 'Session Chair':
                        if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") && !attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                            summary.chairTotal += (estimateTotal * afFunding) / 100
                        }
                        summary.chairCount++
                        break

                    case 'Presenter/Speaker':
                        if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") && !attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                            summary.presenterTotal += (estimateTotal * afFunding) / 100
                        }
                        summary.presenterCount++
                        break

                    case 'Support':
                        if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") && !attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                            summary.supportTotal += (estimateTotal * afFunding) / 100
                        }
                        summary.supportCount++
                        break

                    case 'Other':
                        if (!attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") && !attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                            summary.otherTotal += (estimateTotal * afFunding) / 100
                        }
                        summary.otherCount++
                        break
                }

                if (attendee?.accountType?.equalsIgnoreCase("External") && !attendee?.reservedTD) {
                    summary.extTotal += (estimateTotal * afFunding) / 100
                    summary.extCount++
                }

                if (attendee?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendee?.rankGrade?.code?.equalsIgnoreCase("CTR")) {
                    summary.ctrTotal += (estimateTotal * afFunding) / 100
                    summary.ctrCount++
                }
            }

            if (attendee?.status?.equalsIgnoreCase("Pending")) {
                summary.pendingCount++
            }

            if (attendee?.status?.equalsIgnoreCase("Wait List")) {
                summary.waitlistCount++
            }
        }

        return summary
    }

    /**
     *
     * @param pSummary (Summary) -
     * @param pConferenceInstance (Conference) -
     */
    @Transactional
    public void convertTdFromCSB(final Summary pSummary, final ConferenceStatusBlock pCSB) {
        pSummary.orgSummaries = new HashSet<>()

        for (officeSymbol in pCSB?.tdList) {
            Organization org = Organization.findByOfficeSymbol(officeSymbol)

            if (org) {
                OrgSummary orgSummary = new OrgSummary()

                orgSummary.org = org
                orgSummary.setConstrainedTotal(pCSB?.tdTotal?.get(officeSymbol))
                orgSummary.constrainedCount = pCSB?.tdCount?.get(officeSymbol)
                orgSummary.summary = pSummary

                orgSummary.save flush: true

                pSummary.orgSummaries.add(orgSummary)
            }
        }
    }

    /**
     *
     * @param pConferenceInstance (Conference) -
     */
    @Transactional
    public void removeSummary(final String pSummaryType, final Conference pConferenceInstance) {
        Summary currentSummary = Summary.findByConferenceAndSummaryType(pConferenceInstance, pSummaryType)
        if (currentSummary) {
            currentSummary.orgSummaries.clear()
            pConferenceInstance.summaries.remove(currentSummary)
            currentSummary.delete flush: false
        }

        pConferenceInstance.save flush: true
    }

    /**
     *
     * @param pSummaryType (String) -
     * @param pConferenceInstance (Conference) -
     */
    @Transactional
    public void maintainSummary(final String pSummaryType, final Conference pConferenceInstance) {
        maintainSummary(conferenceService.determineConferenceStatusBlock(false, pConferenceInstance), pSummaryType, pConferenceInstance)
    }

    /**
     *
     * @param pForceRecalc (boolean) -
     * @param pSummaryType (String) -
     * @param pConferenceInstance (Conference) -
     */
    @Transactional
    public void maintainSummary(final boolean pForceRecalc, final String pSummaryType, final Conference pConferenceInstance) {
        maintainSummary(conferenceService.determineConferenceStatusBlock(pForceRecalc, pConferenceInstance), pSummaryType, pConferenceInstance)
    }

    /**
     *
     * @param pCSB (ConferenceStatusBlock) -
     * @param pSummaryType (String) -
     * @param pConferenceInstance (Conference) -
     */
    @Transactional
    public void maintainSummary(final ConferenceStatusBlock pCSB, final String pSummaryType, final Conference pConferenceInstance) {
        removeSummary(pSummaryType, pConferenceInstance)

        Summary summary = convertFromCSB(pCSB, pConferenceInstance, pSummaryType)
        summary.save flush: true

        convertTdFromCSB(summary, pCSB)
        summary.save flush: true

        if (!pConferenceInstance?.summaries) {
            pConferenceInstance.summaries = new HashSet<>()
        }
        pConferenceInstance.summaries.add(summary)
        pConferenceInstance.save flush: true
    }

}
