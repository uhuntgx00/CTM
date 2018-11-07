package mil.ebs.ctm

class Address {

    String addressType

    String street1
    String street2
    String city
    String state
    String country
    String zipCode
    String phone

    static belongsTo = [cost: Cost, conference: Conference]

    static searchable = true

    static constraints = {
        addressType inList: ['Venue', 'Lodging']

        street1 blank: true, nullable: true
        street2 blank: true, nullable: true
        city blank: false, nullable: false
        state blank: true, nullable: true
        country blank: false, nullable: false
        zipCode blank: true, nullable: true
        phone blank: true, nullable: true

        cost blank: true, nullable: true
        conference blank: true, nullable: true
    }

    static mapping = {
        version false
    }

    String toString() {
        if (street1) {
            return street1 + ", " + city + " " + state + ", " + country + "  " + zipCode
        } else {
            return city + " " + state + ", " + country + "  " + zipCode
        }
    }

    String toShort() {
        return city + " " + state + ", " + country
    }

}
