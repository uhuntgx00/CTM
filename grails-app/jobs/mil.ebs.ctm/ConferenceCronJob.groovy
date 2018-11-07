package mil.ebs.ctm

class ConferenceCronJob {

    def conferenceService

    static triggers = {
        cron name: 'conferenceTrigger', cronExpression: "0 0 0 * * ?"
    }

    def group = "Conference"

    def execute() {
        conferenceService.checkApprovedToFinalize()
    }

}
