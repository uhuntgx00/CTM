package mil.ebs.ctm

class AccountActivity {

    String account
    String url
    Date activityDate

    static constraints = {
        account blank: false, nullable: false, maxSize: 500
        url blank: true, nullable: true, maxSize: 1000
        activityDate blanke: false, nullable: false
    }

    static mapping = {
        version false
        sort "activityDate"
        order "desc"
   	}

}
