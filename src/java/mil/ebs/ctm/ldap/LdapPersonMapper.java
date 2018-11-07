package mil.ebs.ctm.ldap;

import mil.ebs.utility.StringUtility;
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.ldap.core.support.LdapContextSource;
//import org.springframework.ldap.filter.*;

import java.util.List;

public class LdapPersonMapper {

//    private LdapTemplate ldapTemplate;
//
//    public LdapPersonMapper() {
//
//        LdapContextSource lcs = new LdapContextSource();
//        lcs.setUrl("ldap://ldap.build.devebs.afrl.af.mil:389");
//        lcs.setBase("ou=people,dc=sso,dc=afrl,dc=af,dc=mil");
//        lcs.setUserDn("uid=g2mserviceaccount,dc=sso,dc=afrl,dc=af,dc=mil");
//        lcs.setPassword("1w@nt_2s33P30pl3!");
//
//        ldapTemplate = new LdapTemplate(lcs);
//    }
//
//    /**
//     * Getter for property 'allPersons'.
//     *
//     * @return Value for property 'allPersons'.
//     */
//    @SuppressWarnings({"unchecked"})
//    public List<LdapPerson> getAllPersons(final String pLastName, final String pEmailAddress) {
//        AndFilter andFilter = new AndFilter();
//        andFilter.and(new EqualsFilter("objectclass", "ebsPerson"));
//
//        OrFilter orFilter = new OrFilter();
//        orFilter.or(new EqualsFilter("nsAccountLock", "true"));
//        orFilter.or(new EqualsFilter("obUserAccountControl", "DEACTIVATED"));
//
//        andFilter.and(new NotFilter(orFilter));
//        andFilter.and(new WhitespaceWildcardsFilter("altsecurityidentities", ""));
//
//        if (StringUtility.containsValue(pLastName)) {
//            andFilter.and(new WhitespaceWildcardsFilter("sn", pLastName));
//        }
//        if (StringUtility.containsValue(pEmailAddress)) {
//            andFilter.and(new WhitespaceWildcardsFilter("mail", pEmailAddress));
//        }
//
//        return ldapTemplate.search("", andFilter.encode(), new PersonAttributesMapper());
//    }
//
//    public LdapPerson findPerson(final String pDn) {
//        return (LdapPerson) ldapTemplate.lookup(pDn, new PersonAttributesMapper());
//    }
//
//    public LdapTemplate getLdapTemplate() {
//        return ldapTemplate;
//    }
//
//    public void setLdapTemplate(final LdapTemplate pLdapTemplate) {
//        ldapTemplate = pLdapTemplate;
//    }

}
