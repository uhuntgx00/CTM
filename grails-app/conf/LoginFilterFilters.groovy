import mil.ebs.ctm.Account
import mil.ebs.ctm.AccountActivity

class LoginFilterFilters {

    def springSecurityService

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                if (springSecurityService.isLoggedIn()) {
                    // get the user who is logged on currently
                    def account = Account.get(springSecurityService.principal?.id)

                    // only a VALID user account logged can be updated
                    if (account) {
                        Date date = new Date()

                        // increment the login count if the day does not match
                        // TODO: Yes, there is a distinct possibility that if a user does not login since the previous login day it will not increment
                        if (account?.lastLoginDate?.day != date?.day) {
                            account.loginCount++
                        }

                        account.setLastLoginDate(date)
                        account.setAccountExpired(false)
                        account.save(flush: true)

                        AccountActivity accountActivity = new AccountActivity(account: account.getUsername(), url: request.forwardURI, activityDate: new Date())
                        accountActivity.save flush: true
                    }
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
