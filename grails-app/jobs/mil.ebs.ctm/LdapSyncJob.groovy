package mil.ebs.ctm

class LdapSyncJob {
    def ldapService

    def concurrent = false

    static triggers = {
        def cronTimer = '0 0 0 * * ?'
        cron name: 'ldapTrigger', group: 'ldapSync', cronExpression: cronTimer
    }

    def group = 'ldapSync'

    def execute() {
        System.out.println("--------------------------------------------")
        System.out.println("Starting LDAP Sync - " + new Date())
        System.out.println("--------------------------------------------")
        ldapService.processSync()
    }

}