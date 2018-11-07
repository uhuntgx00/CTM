package mil.ebs.ctm.ref

class RefRankGrade {

    String code
    String grade
    String description
    String employeeType = "Civilian"

    boolean officer = false
    boolean military = false


    static constraints = {
        code blank: false, nullable: false, unique: true
        grade blank: false, nullable: false, maxSize: 5
        description blank: false, nullable: false, maxSize: 100
        employeeType inList: ['Military', 'Civilian', 'Contractor', 'Other'], maxSize: 30, blank: true, nullable: true

        officer()
        military()
    }

    static mapping = {
        version false
    }

    String toString() {
        return "${code} [${description}]"
    }

}
