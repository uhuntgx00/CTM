package mil.ebs.ctm

class SecurityCronJob {
    def accountService

    static triggers = {
        cron name: 'securityTrigger', cronExpression: "0 0 0 * * ?"
    }

    def group = "Security"

    def execute() {
        accountService.checkForExpiredAccounts()
    }

}