package mil.ebs.ctm.ldap;

//import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public class PersonAttributesMapper
//        implements AttributesMapper
{

//    public Object mapFromAttributes(final Attributes pAttrs)
//            throws NamingException
//    {
//
//        LdapPerson person = new LdapPerson();
//
//        if (pAttrs.get("cn") != null) {
//            person.setFullName((String) pAttrs.get("cn").get());
//        }
//        if (pAttrs.get("sn") != null) {
//            person.setLastName((String) pAttrs.get("sn").get());
//        }
//
//        if (pAttrs.get("altsecurityidentities") != null) {
//            String[] cn = ((String) pAttrs.get("altsecurityidentities").get()).split("CN=");
//            if (cn.length > 2) {
//                person.setGuid(cn[2]);
//            }
//        }
//
//        if (person.getGuid() != null) {
//            String[] guid = person.getGuid().split("\\.");
//            if (guid.length > 2) {
//                person.setFirstName(toProperCase(guid[1]));
//            }
//            if (guid.length > 3) {
//                person.setMiddleName(toProperCase(guid[2]));
//            }
//        }
//
//        if (pAttrs.get("mail") != null) {
//            person.setEmailAddress((String) pAttrs.get("mail").get());
//        }
//
//        return person;
//    }
//
//    private String toProperCase(final String pName) {
//        return pName.length() > 1 ? pName.substring(0, 1).toUpperCase() + pName.substring(1).toLowerCase() : pName;
//    }

}

