package mil.ebs.ctm

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["permitAll"])
class AddressController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "GET"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Address.list(params), model:[addressInstanceCount: Address.count()]
    }

    def show(Address addressInstance) {
        respond addressInstance
    }

//***************************************************************************
// Address standard create functions
//***************************************************************************

    def create() {
        respond new Address(params)
    }

    @Transactional
    def save(Address addressInstance) {
        if (addressInstance == null) {
            notFound()
            return
        }

        if (addressInstance.hasErrors()) {
            respond addressInstance.errors, view:'create'
            return
        }

        addressInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'addressInstance.label', default: 'Address'), addressInstance.id])
                redirect addressInstance
            }
            '*' { respond addressInstance, [status: CREATED] }
        }
    }

//***************************************************************************
// Venue address functions
//***************************************************************************

    private Address initAddress(String pConferenceId) {
        Address address = new Address(params)

        Conference conferenceInstance = Conference.get(pConferenceId)
        address.conference = conferenceInstance
        address.addressType = "Venue"

        return address
    }

    /**
     * This function is enabled/executed via the button action from the conference page.
     *
     * @return
     */
    def createVenueAddress() {
        respond initAddress(params.get('conferenceId'))
    }

    /**
     * This function is enabled/executed by a redirect call from the ConferenceController via the create conference action.
     * Wizard1 --> attendee has been created
     *
     * @return
     */
    def createVenueAddressWizard1() {
        respond initAddress(params.get('conferenceId')), model:[attendee: Attendee.get(params.get('attendeeId'))]
    }

    /**
     * This function is enabled/executed by a redirect call from the ConferenceController via the create conference action.
     * Wizard2 --> attendee has NOT been created
     *
     * @return
     */
    def createVenueAddressWizard2() {
        respond initAddress(params.get('conferenceId'))
    }

    /**
     *
     * @param addressInstance (Address) -
     * @return
     */
    @Transactional
    def saveVenueAddress(Address addressInstance) {
        if (addressInstance == null) {
            notFound()
            return
        }

        if (addressInstance.hasErrors()) {
            respond addressInstance.errors, view:'createVenueAddress'
            return
        }

        addressInstance.save flush:true

        def conference = Conference.get(Integer.parseInt(params.get('conference.id')))
        conference.address = addressInstance
        conference.save flush:true

        request.withFormat {
            form {
                redirect controller:"conference", id: conference?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: conference?.id, action:"show", method:"GET"}
        }
    }

    /**
     *
     * Wizard1 --> attendee has been created
     *
     * @param addressInstance (Address) -
     * @return
     */
    @Transactional
    def saveVenueAddressWizard1(Address addressInstance) {
        if (addressInstance == null) {
            notFound()
            return
        }

        if (addressInstance.hasErrors()) {
            respond addressInstance.errors, view:'createVenueAddressWizard1'
            return
        }

        addressInstance.save flush:true

        def conference = Conference.get(Integer.parseInt(params.get('conference.id')))
        conference.address = addressInstance
        conference.save flush:true

        String attendeeId = params.get('attendee.id')
        request.withFormat {
            form {
                redirect controller:"attendee", id: attendeeId, action:"conferenceEditWizard", method:"GET"
            }
            '*' {
                redirect controller:"attendee", id: attendeeId, action:"conferenceEditWizard", method:"GET"
            }
        }
    }

    /**
     *
     * Wizard2 --> attendee has NOT been created
     *
     * @param addressInstance (Address) -
     * @return
     */
    @Transactional
    def saveVenueAddressWizard2(Address addressInstance) {
        if (addressInstance == null) {
            notFound()
            return
        }

        if (addressInstance.hasErrors()) {
            respond addressInstance.errors, view:'createVenueAddressWizard2'
            return
        }

        addressInstance.save flush:true

        def conference = Conference.get(Integer.parseInt(params.get('conference.id')))
        conference.address = addressInstance
        conference.save flush:true

        request.withFormat {
            form {
                redirect controller:"conference", id: conference?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: conference?.id, action:"show", method:"GET"}
        }
    }

//***************************************************************************
// Lodging address functions
//***************************************************************************

    def createLodgingAddress() {
        respond new Address(params)
    }

    @Transactional
    def saveLodgingAddress(Address addressInstance) {
        if (addressInstance == null) {
            notFound()
            return
        }

        if (addressInstance.hasErrors()) {
            respond addressInstance.errors, view:'createLodgingAddress'
            return
        }

        addressInstance.save flush:true

        def cost = Cost.get(Integer.parseInt(params.get('cost.id')))
        cost.lodgingAddress = addressInstance
        cost.save flush:true

        request.withFormat {
            form {
                redirect controller:"cost", id: cost?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"cost", id: cost?.id, action:"show", method:"GET"}
        }
    }

//***************************************************************************
// Address standard functions
//***************************************************************************

    def edit(Address addressInstance) {
        respond addressInstance
    }

    def conferenceEdit(Address addressInstance) {
        respond addressInstance
    }

    @Transactional
    def update(Address addressInstance) {
        if (addressInstance == null) {
            notFound()
            return
        }

        if (addressInstance.hasErrors()) {
            respond addressInstance.errors, view:'edit'
            return
        }

        addressInstance.save flush:true

        request.withFormat {
            form {
                redirect addressInstance
            }
            '*'{ respond addressInstance, [status: OK] }
        }
    }

    @Transactional
    def conferenceUpdate(Address addressInstance) {
        if (addressInstance == null) {
            notFound()
            return
        }

        if (addressInstance.hasErrors()) {
            respond addressInstance.errors, view:'conferenceEdit'
            return
        }

        addressInstance.save flush:true

        request.withFormat {
            form {
                redirect controller:"conference", id: addressInstance?.conference?.id, action:"show", method:"GET"
            }
            '*' { redirect controller:"conference", id: addressInstance?.conference?.id, action:"show", method:"GET" }
        }
    }

    @Transactional
    def delete(Address addressInstance) {

        if (addressInstance == null) {
            notFound()
            return
        }

        addressInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Address.label', default: 'Address'), addressInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    @Transactional
    @Secured(["ROLE_ADMIN", "ROLE_FMC_ADMIN"])
    def deleteVenueAddress(Address addressInstance) {

        if (addressInstance == null) {
            notFound()
            return
        }

        def conference = Conference.get(Integer.parseInt(params.get('conference.id')))
        conference.address = null
        conference.perdiem = null
        conference.meals = null
        conference.save flush:true

        addressInstance.delete flush:true

        request.withFormat {
            form {
                redirect controller:"conference", id: conference?.id, action:"show", method:"GET"
            }
            '*'{ redirect controller:"conference", id: conference?.id, action:"show", method:"GET" }
        }
    }

    @Transactional
    def deleteLodgingAddress(Address addressInstance) {

        if (addressInstance == null) {
            notFound()
            return
        }

        def cost = Cost.get(Integer.parseInt(params.get('cost.id')))
        cost.lodgingAddress = null
        cost.save flush:true

        addressInstance.delete flush:true

        request.withFormat {
            form {
                redirect controller:"cost", id: cost?.id, action:"show", method:"GET"
            }
            '*'{ redirect controller:"cost", id: cost?.id, action:"show", method:"GET" }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'addressInstance.label', default: 'Address'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
