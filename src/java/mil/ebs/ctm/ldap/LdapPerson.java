package mil.ebs.ctm.ldap;

//import gldapo.schema.annotation.GldapoNamingAttribute;
//import gldapo.schema.annotation.GldapoSynonymFor;
import mil.ebs.utility.StringUtility;

@SuppressWarnings("UnusedDeclaration")
public class LdapPerson {

// -------------------------------------------
// Member Variables
// -------------------------------------------

//    @GldapoNamingAttribute
//    private String uid;
//
//    private String guid;
//    private String firstName;
//    private String middleName;
//    private String initials;
//
//    @GldapoSynonymFor("sn")
//    private String lastName;
//
//    private String givenName;
//
//    @GldapoSynonymFor("cn")
//    private String fullName;
//
//    private String description;
//    private String title;
//
//    @GldapoSynonymFor("employeetype")
//    private String employeeType;
//
//    @GldapoSynonymFor("telephonenumber")
//    private String telephoneNumber;
//
//    @GldapoSynonymFor("mail")
//    private String emailAddress;
//
//    @GldapoSynonymFor("altsecurityidentities")
//    private String altSecurityIdentities;
//
//// -------------------------------------------
//// Constructors
//// -------------------------------------------
//
//    /**
//     * CONSTRUCTOR
//     *
//     * @param pGuid (Integer) -
//     * @param pEmailAddress (String) -
//     * @param pFullName (String) -
//     * @param pLastName (String) -
//     * @since 1.0.0
//     */
//    public LdapPerson(final String pGuid, final String pEmailAddress, final String pFullName, final String pLastName) {
//        setEmailAddress(pEmailAddress);
//        setGuid(pGuid);
//        setFullName(pFullName);
//        setLastName(pLastName);
//    }
//
//    public LdapPerson() {
//
//    }
//
//// -------------------------------------------
//// Member Functions
//// -------------------------------------------
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public String toString() {
//        final StringBuilder sb = new StringBuilder();
//
//        sb.append("Person");
//        sb.append("{guid=").append(guid);
//        sb.append(", lastName='").append(lastName).append('\'');
//        sb.append(", givenName='").append(givenName).append('\'');
//        sb.append(", middleName='").append(middleName).append('\'');
//        sb.append(", initials='").append(initials).append('\'');
//        sb.append(", fullName='").append(fullName).append('\'');
//        sb.append(", description='").append(description).append('\'');
//        sb.append(", emailAddress='").append(emailAddress).append('\'');
//        sb.append(", telephoneNumber='").append(telephoneNumber).append('\'');
//        sb.append(", employeeType='").append(employeeType).append('\'');
//        sb.append(", title='").append(title).append('\'');
//        sb.append('}');
//
//        return sb.toString();
//    }
//
//    public void mapToFullNameField(String value) {
//        fullName = value.toUpperCase();
//    }
//
//    public void mapToLastNameField(String value) {
//        lastName = value.toUpperCase();
//    }
//
//    public void mapToEmailAddressField(String value) {
//        emailAddress = value.toUpperCase();
//    }
//
//    public void mapToAltSecurityIdentitiesField(String value) {
//        altSecurityIdentities = value.toUpperCase();
//    }
//
//// -------------------------------------------
//// Accessors
//// -------------------------------------------
//
//    /**
//     * Function returns the user's GUID.
//     *
//     * @return String - user's GUID
//     * @since 1.0.0
//     */
//    public String getGuid() {
//        if (!StringUtility.containsValue(guid) && StringUtility.containsValue(altSecurityIdentities)) {
//            String[] cn = altSecurityIdentities.split("CN=");
//            if (cn.length > 2) {
//                guid = cn[2];
//            }
//        }
//
//        return guid;
//    }
//
//    /**
//     * Function returns the user's email address.
//     *
//     * @return String - user's email address
//     * @since 1.0.0
//     */
//    public String getEmailAddress() {
//        return emailAddress;
//    }
//
//    /**
//     * Function returns the user's first name.
//     * For user's full name use the getName() function.
//     *
//     * @return String - user's first name
//     * @since 1.0.0
//     */
//    public String getFullName() {
//        return fullName;
//    }
//
//    public String getAltSecurityIdentities() {
//        return altSecurityIdentities;
//    }
//
//    public String getGivenName() {
//        return givenName;
//    }
//
//    public String getUid() {
//        return uid;
//    }
//
//    public String getTelephoneNumber() { return telephoneNumber; }
//
//    public String getInitials() { return initials; }
//
//    public String getEmployeeType() { return employeeType; }
//
//    public String getTitle() { return title; }
//
//    /**
//     * Function returns the user's last name.
//     * For user's full name use the getName() function.
//     *
//     * @return String - user's last name
//     * @since 1.0.0
//     */
//    public String getLastName() {
//        return lastName;
//    }
//
//    /**
//     * Getter for property 'description'.
//     *
//     * @return Value for property 'description'.
//     */
//    public String getDescription() {
//        return description;
//    }
//
//    /**
//     * Getter for property 'middleName'.
//     *
//     * @return Value for property 'middleName'.
//     */
//    public String getMiddleName() {
//        return middleName;
//    }
//
//    /**
//     * Getter for property 'firstName'.
//     *
//     * @return Value for property 'firstName'.
//     */
//    public String getFirstName() {
//        return firstName;
//    }
//
//// -------------------------------------------
//// Mutators
//// -------------------------------------------
//
//    /**
//     * Function updates the user's GUID with the passed in value.
//     *
//     * @param pGuid (Integer) - guid
//     * @since 1.0.0
//     */
//    public void setGuid(final String pGuid) {
//        guid = pGuid;
//    }
//
//    /**
//     * Function updates the user's email address with the passed in value.
//     *
//     * @param pEmailAddress (String) - email address
//     * @since 1.0.0
//     */
//    public void setEmailAddress(final String pEmailAddress) {
//        emailAddress = pEmailAddress;
//    }
//
//    /**
//     * Function updates the user's first name with the passed in value.
//     *
//     * @param pFullName (String) - full name
//     * @since 1.0.0
//     */
//    public void setFullName(final String pFullName) {
//        fullName = pFullName;
//    }
//
//    /**
//     * Function updates the user's last name with the passed in value.
//     *
//     * @param pLastName (String) - last name
//     * @since 1.0.0
//     */
//    public void setLastName(final String pLastName) {
//        lastName = pLastName;
//    }
//
//    /**
//     * Setter for property 'description'.
//     *
//     * @param pDescription Value to set for property 'description'.
//     */
//    public void setDescription(final String pDescription) {
//        description = pDescription;
//    }
//
//    /**
//     * Setter for property 'firstName'.
//     *
//     * @param pFirstName Value to set for property 'firstName'.
//     */
//    public void setFirstName(final String pFirstName) {
//        firstName = pFirstName;
//    }
//
//    /**
//     * Setter for property 'middleName'.
//     *
//     * @param pMiddleName Value to set for property 'middleName'.
//     */
//    public void setMiddleName(final String pMiddleName) {
//        middleName = pMiddleName;
//    }
//
//    public void setAltSecurityIdentities(final String pAltSecurityIdentities) {
//        altSecurityIdentities = pAltSecurityIdentities;
//    }
//
//    public void setGivenName(final String pGivenName) {
//        givenName = pGivenName;
//    }
//
//    public void setUid(final String pUid) {
//        uid = pUid;
//    }
//
//    public void setTelephoneNumber(final String pTelephoneNumber) { telephoneNumber = pTelephoneNumber; }
//
//    public void setInitials(final String pInitials) { initials = pInitials; }
//
//    public void setEmployeeType(final String pEmployeeType) { employeeType = pEmployeeType; }
//
//    public void setTitle(final String pTitle) { title = pTitle; }
}
