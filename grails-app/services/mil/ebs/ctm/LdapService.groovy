package mil.ebs.ctm

import mil.ebs.ctm.ldap.LdapPerson
import mil.ebs.ctm.ldap.LdapPersonMapper
import mil.ebs.utility.StringUtility

class LdapService {

    def performSearch(String pLastName, String pEmailAddress) {
        LdapPersonMapper lpm = new LdapPersonMapper()
        List<LdapPerson> ldapList = lpm.getAllPersons(pLastName, pEmailAddress)
    }

    def processSync() {
        List<LdapPerson> personList = LdapPerson.findAll( directory: "person", filter:"(&(userPrincipalName=*)(!(netbiosDomain=extranet))(!(employeeType=T))(!(employeeType=U)))")
//        List<LdapPerson> personList = LdapPerson.findAll( directory: "person", filter:"(userPrincipalName=*)")
        List<Account> accountList = Account.getAll()

        if (personList.empty) {
            // do nothing
        } else {
            List<Account> guidList = new ArrayList<>(0)

            // add all users from LDAP into guidList
            for (person in personList) {
//                print "LdapPerson --> " + person
                guidList.add(createAccount(person))
            }

            // remove all accounts currently in the system from the guidList
            for (account in accountList) {
                guidList.remove(account)
            }
            println "Possible accounts to add: " + guidList.size()

            // add all remaining users in guidList into the current system
            int added = 0;
            for (newAccount in guidList) {
                added += ProcessLdapAdd(newAccount)
            }

            println "Accounts added: " + added
        }
    }

    private static Account createAccount(final LdapPerson pPerson) {
        Account account = new Account(username: pPerson.guid, displayName: pPerson.fullName, middleInitial: pPerson.initials, lastName: pPerson.lastName, firstName: pPerson.givenName, phoneNumber: pPerson.telephoneNumber, createdDate: new Date(), lastLoginDate: new Date(), emailAddress: pPerson.emailAddress, emailValidated: true, accountValidated: true, enabled: true, title: pPerson.title)

        if (pPerson?.employeeType?.equalsIgnoreCase("C")) {
            account.setEmployeeType("Civilian")
            account.setCanAttendConferences(true)
        } else if (pPerson?.employeeType?.equalsIgnoreCase("E")) {
            account.setEmployeeType("Contractor")
            account.setCanAttendConferences(false)
        } else if (pPerson?.employeeType?.equalsIgnoreCase("A")) {
            account.setEmployeeType("Military")
            account.setCanAttendConferences(true)
        } else {
            account.setEmployeeType("Other")
            account.setCanAttendConferences(true)
        }

//*************************************************************************************************************************
// AFMC/AFOSR
//*************************************************************************************************************************
        // does the LDAP fullName contain "AFOSR/"
        if (pPerson.fullName.contains("AFOSR/")) {
            String lastPart = pPerson.fullName.substring(pPerson.fullName.indexOf("AFOSR/"))

            // remove double AFRL/ from lastPart
            if (lastPart.contains("AFOSR/AFOSR/")) {
                lastPart = pPerson.fullName.substring(pPerson.fullName.indexOf("AFOSR/")+6)
            }

            // shorten lastPart to just 10 characters
            if (lastPart.length() > 10) {
                lastPart = lastPart.substring(0, 10)
            }

            // does the last part exist in the Org list
            Organization firstPass = Organization.findByOfficeSymbol(lastPart)
            if (firstPass) {
                account.assignedTD = firstPass
            } else {
                String strippedOrg = lastPart.substring(lastPart.indexOf("AFOSR/")+6)
                String twoLetter = strippedOrg.length() > 2 ? strippedOrg.substring(0, 2) : strippedOrg
                account.assignedTD = Organization.findByOfficeSymbol("AFOSR/"+twoLetter)
                account.fsOrganization = lastPart
            }
        } else {

        //*************************************************************************************************************************
        // AFMC/AFRL
        //*************************************************************************************************************************
            // does the LDAP fullName contain "AFRL/"
            if (pPerson.fullName.contains("AFRL/")) {
                String lastPart = pPerson.fullName.substring(pPerson.fullName.indexOf("AFRL/"))

                // remove double AFRL/ from lastPart
                if (lastPart.contains("AFRL/AFRL/")) {
                    lastPart = pPerson.fullName.substring(pPerson.fullName.indexOf("AFRL/")+5)
                }

                // shorten lastPart to just 10 characters
                if (lastPart.length() > 10) {
                    lastPart = lastPart.substring(0, 10)
                }

                // does the last part exist in the Org list
                Organization firstPass = Organization.findByOfficeSymbol(lastPart)
                if (firstPass) {
                    account.assignedTD = firstPass
                } else {
                    String strippedOrg = lastPart.substring(lastPart.indexOf("AFRL/")+5)
                    String twoLetter = strippedOrg.length() > 2 ? strippedOrg.substring(0, 2) : strippedOrg
                    account.assignedTD = Organization.findByOfficeSymbol("AFRL/"+twoLetter)
                    account.fsOrganization = lastPart
                }
            }

        }

//*************************************************************************************************************************
// AFMC/USAFSAM
//*************************************************************************************************************************
        if (pPerson.fullName.contains("USAFSAM")) {
            String lastPart = pPerson.fullName.substring(pPerson.fullName.indexOf("USAFSAM"))

            // does the last part exist in the Org list
            Organization firstPass = Organization.findByOfficeSymbol(lastPart)
            if (firstPass) {
                account.assignedTD = firstPass
            } else {
                String strippedOrg = lastPart.substring(lastPart.indexOf("USAFSAM/")+8)
                String twoLetter = strippedOrg.length() > 2 ? strippedOrg.substring(0, 2) : strippedOrg
                account.assignedTD = Organization.findByOfficeSymbol("711 HPW/"+twoLetter)
                account.fsOrganization = lastPart
            }
        }

//*************************************************************************************************************************
// AFMC/711 HPW
//*************************************************************************************************************************
        if (pPerson.fullName.contains("711 HPW")) {
            String lastPart = pPerson.fullName.substring(pPerson.fullName.indexOf("711 HPW"))

            // does the last part exist in the Org list
            Organization firstPass = Organization.findByOfficeSymbol(lastPart)
            if (firstPass) {
                account.assignedTD = firstPass
            } else {
                String strippedOrg = lastPart.substring(lastPart.indexOf("711 HPW/")+8)
                String twoLetter = strippedOrg.length() > 2 ? strippedOrg.substring(0, 2) : strippedOrg
                account.assignedTD = Organization.findByOfficeSymbol("711 HPW/"+twoLetter)
                account.fsOrganization = lastPart
            }
        }

        return account
    }

    private static int ProcessLdapAdd(final Account pAccount) {
        try {
            pAccount.save flush: true

            AccountRole.create pAccount, Role.findByAuthority("ROLE_USER"), true
            if (pAccount?.assignedTD?.trueTD) {
                AccountRole.create pAccount, Role.findByAuthority("ROLE_AFRL_USER"), true
            } else {
                AccountRole.create pAccount, Role.findByAuthority("ROLE_NON_AFRL_USER"), true
            }

            println "Account added --> " + pAccount
            return 1
        } catch (Exception ignore) {
            // do nothing
        }

        return 0
    }


    public Account processLdapParse(final Account pAccount, final String pPal2, final String pOfficeSymbol) {
        try {
            List<LdapPerson> personList = LdapPerson.findAll(directory: "person", filter: "(&(sn=${pAccount.lastName})(givenName=${pAccount.firstName})(!(netbiosDomain=extranet))(!(employeeType=T))(!(employeeType=U)))")
            if (personList.empty) {
                pAccount.setEmailValidated(false)
            } else {
                if (personList.size() == 1) {
                    // select the 1 record and populate required data...
                    LdapPerson person = personList.get(0)

                    // does the person object have a VALID guid, if so set the username (CAC) for the EbsEtiUser account
                    if (person.guid) {
                        pAccount.setUsername(person.guid)
                    }

                    // does the person object have a VALID emailAddress, if so set the email address for the EbsEtiUser account and validate TRUE
                    if (person.emailAddress) {
                        pAccount.setEmailAddress(person.emailAddress)
                        pAccount.setEmailValidated(true)
                    } else {
                        pAccount.setEmailValidated(false)
                    }
                } else {
                    if (StringUtility.containsValue(pPal2)) {
                        def tdOrg = "AFRL/" + pOfficeSymbol
                        for (person in personList) {
                            if (person.fullName.contains(tdOrg) || (tdOrg.contains("711") && person.fullName.contains("USAFSAM"))) {
                                // does the person object have a VALID guid, if so set the username (CAC) for the EbsEtiUser account
                                if (person.guid) {
                                    pAccount.setUsername(person.guid)
                                }

                                // does the person object have a VALID emailAddress, if so set the email address for the EbsEtiUser account and validate TRUE
                                if (person.emailAddress) {
                                    pAccount.setEmailAddress(person.emailAddress)
                                    pAccount.setEmailValidated(true)
                                } else {
                                    pAccount.setEmailValidated(false)
                                }

                                // break from the loop if user information has been updated...
                                break
                            }
                        }
                    } else {
                        pAccount.setEmailValidated(false)
                    }
                }
            }
        } catch (Exception ignore) {
            pAccount.setEmailValidated(false)
        }

        return pAccount
    }


    public Account processLdapLookup(final Account pAccount) {
        //noinspection GroovyAssignabilityCheck
        try {
            List<LdapPerson> personList = LdapPerson.findAll( directory: "person", filter:"(&(sn=${pAccount.lastName})(givenName=${pAccount.firstName}))")

            if (personList.empty) {
                pAccount.setEmailValidated(false)
            } else {
                if (personList.size() == 1) {
                    // select the 1 record and populate required data...
                    LdapPerson person = (LdapPerson) personList.get(0)

                    // does the person object have a VALID guid, if so set the username (CAC) for the EbsEtiUser account
                    if (person.guid) {
                        pAccount.setUsername(person.guid)
                    }

                    // does the person object have a VALID emailAddress, if so set the email address for the EbsEtiUser account and validate TRUE
                    if (person.emailAddress) {
                        pAccount.setEmailAddress(person.emailAddress)
                        pAccount.setEmailValidated(true)
                    } else {
                        pAccount.setEmailValidated(false)
                    }
                } else {
                    def tdOrg = "AFRL"

                    for (person in personList) {
                        if (person.fullName.contains(tdOrg) || (tdOrg.contains("711") && person.fullName.contains("USAFSAM"))) {
                            // does the person object have a VALID guid, if so set the username (CAC) for the EbsEtiUser account
                            if (person.guid) {
                                pAccount.setUsername(person.guid)
                            }

                            // does the person object have a VALID emailAddress, if so set the email address for the EbsEtiUser account and validate TRUE
                            if (person.emailAddress) {
                                pAccount.setEmailAddress(person.emailAddress)
                                pAccount.setEmailValidated(true)
                            } else {
                                pAccount.setEmailValidated(false)
                            }

                            // break from the loop if user information has been updated...
                            break
                        }
                    }
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace()
            pAccount.setEmailValidated(false)
        }

        return pAccount
    }

    public Account processLdapQuery(final Account pAccount) {
        //noinspection GroovyAssignabilityCheck
        List<LdapPerson> personList = LdapPerson.findAll( directory: "person", filter:"(&(sn=${pAccount.lastName})(givenName=${pAccount.firstName}))")

        for (Iterator list = personList.iterator(); list.hasNext();) {
            LdapPerson person =  (LdapPerson) list.next();
            if (person.guid == null) {
                if (list) {
                    list.remove();
                }
            }
            if (!(pAccount.emailAddress?.equalsIgnoreCase(person.emailAddress))) {
                if (list) {
                    list.remove();
                }
            }
        }

        if (personList.empty) {
            pAccount.setEmailValidated(false)
        } else {
            if (personList.size() == 1) {
                // select the 1 record and populate required data...
                LdapPerson person = (LdapPerson) personList.get(0)

                // does the person object have a VALID guid, if so set the username (CAC) for the EbsEtiUser account
                if (person.guid) {
                    pAccount.setUsername(person.guid)
                }

                // does the person object have a VALID emailAddress, if so set the email address for the EbsEtiUser account and validate TRUE
                if (person.emailAddress) {
                    pAccount.setEmailAddress(person.emailAddress)
                    pAccount.setEmailValidated(true)
                } else {
                    pAccount.setEmailValidated(false)
                }

                return pAccount
            }
        }

        return null
    }


}
