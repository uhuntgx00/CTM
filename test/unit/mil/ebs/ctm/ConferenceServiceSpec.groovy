package mil.ebs.ctm

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ConferenceService)
@Mock([Account, Conference])
class ConferenceServiceSpec
        extends Specification
{
    def conferenceService


    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
    }

    void "Test the check responsible returns the correct result"() {

        when:"The check responsible action is performed with the correct role"
//            ConferenceService conferenceService = new ConferenceService()

            String permission = ""
            String permissionRole = "ROLE_RESPONSIBLE"
            Account testAccount = new Account()
            Conference conference = new Conference()

            boolean check = conferenceService.checkResponsible(permission, testAccount, conference)

        then:"The model is correct"

    }

    void "test check responsible"() {
    }

}



//public boolean checkResponsible(final String pPermission, final Account pAccount, final Conference pConference) {
//    boolean check = false
//
//    if (pPermission?.contains("ROLE_RESPONSIBLE")) {
//        // ADMIN account needs to match the conference responsible TD (parent)
//        if (pAccount?.assignedTD?.topParent?.id == pConference?.responsibleTD?.topParent?.id) {
//            check = true
//        }
//    } else {
//        check = true
//    }
//
//    return check
//}

