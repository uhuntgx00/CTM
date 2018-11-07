package mil.ebs.ctm

class FundingSource {

    String fundSource
    Integer percentage = 100

    static belongsTo = [cost: Cost]

    static constraints = {
        fundSource inList: ['US Air Force', 'Other US Govt', 'Non-Federal Entity']
        percentage nullable: false, blank: false, min: 0, max: 100
    }

    static mapping = {
        version false
    }

    String toString() {
        return fundSource
    }

}
