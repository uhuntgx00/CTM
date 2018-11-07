package mil.ebs.ctm

import grails.transaction.Transactional

@Transactional
class AccountService {

//    def springSecurityService
    def ctmMailService


//    public AccountService(SpringSecurityService pService) {
//        springSecurityService = pService
//    }

    /**
     * This function checks each account and determines if the last login is greater than 35 days.
     * If the date check is true - set the expiration date and expired option for the account.
     */
    public void checkForExpiredAccounts() {
        List<Account> accountList = Account.findAll()

        for (account in accountList) {
            if (account?.loginDays > 35 && !account?.accountExpired) {
                account?.setLastExpirationDate(new Date())
                account?.setAccountExpired(true)

                account.save flush: true
//            } else if (account?.loginDays > 30 && !account?.accountExpired) {
//                List<String> emailList = new ArrayList<String>(0)
//                emailList.add(account?.emailAddress)
//
//                ctmMailService.sendExpireNotification(emailList, account, 35-account?.loginDays)
            }
        }
    }

}
