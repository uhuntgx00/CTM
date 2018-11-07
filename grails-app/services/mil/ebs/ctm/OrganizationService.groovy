package mil.ebs.ctm

import grails.transaction.Transactional

@Transactional
class OrganizationService {

    /**
     *
     * @param pOrganization (Organization)
     * @return
     */
    public static List<Account> getAssigned(final Organization pOrganization) {
        List<Account> result = new ArrayList<Account>()

        for (org in getOrgList(pOrganization)) {
            result.addAll(Account.findAllByAssignedTD(org))
        }

//        def alist1 = Account.findAllByAssignedTD(pOrganization)
//        result.addAll(alist1)
//
//        if (pOrganization.levelTD?.equalsIgnoreCase("1") || pOrganization.levelTD?.equalsIgnoreCase("2") || pOrganization.levelTD?.equalsIgnoreCase("3")) {
//            def temp2 = Organization.findAllByParent(pOrganization)
//
//            for (td2 in temp2) {
//                def alist2 = Account.findAllByAssignedTD(td2)
//                result.addAll(alist2)
//
//                if (td2?.levelTD?.equalsIgnoreCase("2") || td2?.levelTD?.equalsIgnoreCase("3")) {
//                    def temp3 = Organization.findAllByParent(td2)
//
//                    for (td3 in temp3) {
//                        def alist3 = Account.findAllByAssignedTD(td3)
//                        result.addAll(alist3)
//
//                        if (td3?.levelTD?.equalsIgnoreCase("3")) {
//                            def temp4 = Organization.findAllByParent(td3)
//
//                            for (td4 in temp4) {
//                                def alist4 = Account.findAllByAssignedTD(td4)
//                                result.addAll(alist4)
//                            }
//                        }
//                    }
//                }
//            }
//        }

        return result
    }

    /**
     *
     * @param pOrganization (Organization)
     * @return
     */
    public static List<Account> getAssignedAllowed(final Organization pOrganization) {
        List<Account> result = new ArrayList<Account>()

        for (org in getOrgList(pOrganization)) {
            result.addAll(Account.findAllByAssignedTDAndCanAttendConferences(org, true))
        }

        return result
    }

    /**
     *
     * @param pOrganization (Organization)
     * @return
     */
    public static List<Organization> getOrgList(final Organization pOrganization) {
        List<Organization> result = new ArrayList<>()

        result.add(pOrganization)

        if (pOrganization.levelTD?.equalsIgnoreCase("1") || pOrganization.levelTD?.equalsIgnoreCase("2") || pOrganization.levelTD?.equalsIgnoreCase("3")) {
            def level2 = Organization.findAllByParent(pOrganization)
            for (orgLevel2 in level2) {
                result.add(orgLevel2)

                if (orgLevel2.levelTD?.equalsIgnoreCase("1") || orgLevel2.levelTD?.equalsIgnoreCase("2") || orgLevel2.levelTD?.equalsIgnoreCase("3")) {
                    def level3 = Organization.findAllByParent(orgLevel2)
                    for (orgLevel3 in level3) {
                        result.add(orgLevel3)

                        if (orgLevel3?.levelTD?.equalsIgnoreCase("2") || orgLevel3?.levelTD?.equalsIgnoreCase("3")) {
                            def level4 = Organization.findAllByParent(orgLevel3)
                            for (orgLevel4 in level4) {
                                result.add(orgLevel4)

                                if (orgLevel4?.levelTD?.equalsIgnoreCase("3")) {
                                    def level5 = Organization.findAllByParent(orgLevel4)
                                    for (orgLevel5 in level5) {
                                        result.add(orgLevel5)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result
    }

    /**
     *
     * @param pOrganization (Organization)
     * @return
     */
    public static List<Long> getOrgListById(final Organization pOrganization) {
        List<Long> result = new ArrayList<>()

        result.add(pOrganization?.id)

        if (pOrganization.levelTD?.equalsIgnoreCase("1") || pOrganization.levelTD?.equalsIgnoreCase("2") || pOrganization.levelTD?.equalsIgnoreCase("3")) {
            def level2 = Organization.findAllByParent(pOrganization)
            for (orgLevel2 in level2) {
                result.add(orgLevel2?.id)

                if (orgLevel2.levelTD?.equalsIgnoreCase("1") || orgLevel2.levelTD?.equalsIgnoreCase("2") || orgLevel2.levelTD?.equalsIgnoreCase("3")) {
                    def level3 = Organization.findAllByParent(orgLevel2)
                    for (orgLevel3 in level3) {
                        result.add(orgLevel3?.id)

                        if (orgLevel3?.levelTD?.equalsIgnoreCase("2") || orgLevel3?.levelTD?.equalsIgnoreCase("3")) {
                            def level4 = Organization.findAllByParent(orgLevel3)
                            for (orgLevel4 in level4) {
                                result.add(orgLevel4?.id)

                                if (orgLevel4?.levelTD?.equalsIgnoreCase("3")) {
                                    def level5 = Organization.findAllByParent(orgLevel4)
                                    for (orgLevel5 in level5) {
                                        result.add(orgLevel5?.id)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result
    }

    /**
     *
     * @param pOrganization (Organization)
     * @param pSearch (String)
     * @return
     */
    public static List<Account> getAssignedFiltered(final Organization pOrganization, final String pSearch) {
        List<Account> result = new ArrayList<Account>()

        def tempLast = Account.findAllByAssignedTDAndLastNameIlike(pOrganization, pSearch)
        result.addAll(tempLast)
        def tempFirst = Account.findAllByAssignedTDAndFirstNameIlike(pOrganization, pSearch)
        result.addAll(tempFirst)

        if (pOrganization.levelTD?.equalsIgnoreCase("1") || pOrganization.levelTD?.equalsIgnoreCase("2") || pOrganization.levelTD?.equalsIgnoreCase("3")) {
            def temp2 = Organization.findAllByParent(pOrganization)
            for (td2 in temp2) {
                def alist2Last = Account.findAllByAssignedTDAndLastNameIlike(td2, pSearch)
                result.addAll(alist2Last)
                def alist2First = Account.findAllByAssignedTDAndFirstNameIlike(td2, pSearch)
                result.addAll(alist2First)

                if (td2?.levelTD?.equalsIgnoreCase("2") || td2?.levelTD?.equalsIgnoreCase("3")) {
                    def temp3 = Organization.findAllByParent(td2)
                    for (td3 in temp3) {
                        def alist3Last = Account.findAllByAssignedTDAndLastNameIlike(td3, pSearch)
                        result.addAll(alist3Last)
                        def alist3First = Account.findAllByAssignedTDAndFirstNameIlike(td3, pSearch)
                        result.addAll(alist3First)

                        if (td3?.levelTD?.equalsIgnoreCase("3")) {
                            def temp4 = Organization.findAllByParent(td3)
                            for (td4 in temp4) {
                                def alist4Last = Account.findAllByAssignedTDAndLastNameIlike(td4, pSearch)
                                result.addAll(alist4Last)
                                def alist4First = Account.findAllByAssignedTDAndFirstNameIlike(td4, pSearch)
                                result.addAll(alist4First)
                            }
                        }
                    }
                }
            }
        }

        return result
    }

}
