<%@ page import="mil.ebs.ctm.Link" %>
<div id="footer" role="contentinfo">
    <div class='footer'>
        <div class='footer-wrap'>
            <table class='footer-table'>
                <tbody class='footer-tbody'>
                    <tr class='footer-tr'>
                        <td class='footer-col30'>
                            <h2>Policies &amp; Guidance</h2>
                            <div class="paragraph">
                                <g:each in="${Link.findAllByLinkColumn("Policy")}" var="l">
                                    <g:if test="${l.linkType.equalsIgnoreCase("Link")}">
                                        <span><a tabindex="-1" href="${l.url}" target="_blank">${l.display}</a></span><br/>
                                    </g:if>
                                    <g:elseif test="${l.linkType.equalsIgnoreCase("Doc")}">
                                        <span><a tabindex="-1" href="${l.url}"><g:img dir="images/icons" file="Page-1-512.png" height="16" width="16" alt="${l.tooltip}" title="${l.tooltip}"/>&nbsp;${l.display}</a></span><br/>
                                    </g:elseif>
                                    <g:elseif test="${l.linkType.equalsIgnoreCase("Email")}">
                                        <span><a tabindex="-1" href="mailto:${l.url}"><g:img dir="images/icons" file="Email-Sending-512.png" height="16" width="16" alt="${l.tooltip}" title="${l.tooltip}"/>&nbsp;${l.display}<a></span><br/>
                                    </g:elseif>
                                </g:each>
                            </div>
                        </td>
                        <td class='footer-col30'>
                            <h2>External Websites</h2>
                            <div class="paragraph">
                                <g:each in="${Link.findAllByLinkColumn("External")}" var="l">
                                    <g:if test="${l.linkType.equalsIgnoreCase("Link")}">
                                        <span><a tabindex="-1" href="${l.url}" target="_blank">${l.display}</a></span><br/>
                                    </g:if>
                                    <g:elseif test="${l.linkType.equalsIgnoreCase("Doc")}">
                                        <span><a tabindex="-1" href="${l.url}"><g:img dir="images/icons" file="Page-1-512.png" height="16" width="16" alt="${l.tooltip}" title="${l.tooltip}"/>&nbsp;${l.display}</a></span><br/>
                                    </g:elseif>
                                    <g:elseif test="${l.linkType.equalsIgnoreCase("Email")}">
                                        <span><a tabindex="-1" href="mailto:${l.url}"><g:img dir="images/icons" file="Email-Sending-512.png" height="16" width="16" alt="${l.tooltip}" title="${l.tooltip}"/>&nbsp;${l.display}<a></span><br/>
                                    </g:elseif>
                                </g:each>
                            </div>
                        </td>
                        <td class='footer-col30'>
                            <h2>Support</h2>
                            <div class="paragraph" >
                                <g:each in="${Link.findAllByLinkColumn("Support")}" var="l">
                                    <g:if test="${l.linkType.equalsIgnoreCase("Link")}">
                                        <span><a tabindex="-1" href="${l.url}" target="_blank">${l.display}</a></span><br/>
                                    </g:if>
                                    <g:elseif test="${l.linkType.equalsIgnoreCase("Doc")}">
                                        <span><a tabindex="-1" href="${l.url}"><g:img dir="images/icons" file="Page-1-512.png" height="16" width="16" alt="${l.tooltip}" title="${l.tooltip}"/>&nbsp;${l.display}</a></span><br/>
                                    </g:elseif>
                                    <g:elseif test="${l.linkType.equalsIgnoreCase("Email")}">
                                        <span><a tabindex="-1" href="mailto:${l.url}"><g:img dir="images/icons" file="Email-Sending-512.png" height="16" width="16" alt="${l.tooltip}" title="${l.tooltip}"/>&nbsp;${l.display}<a></span><br/>
                                    </g:elseif>
                                </g:each>
                            </div>
                        </td>
                        <td class='footer-col20'>
                            <g:img dir="images" file="conference.png" height="128" width="128" alt="CTM Logo" title="CTM Logo"/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="footer_text">
            <g:img class="footer_image" dir="images" file="af_logo.png" height="84" width="84" alt="Air Force Logo" title="Air Force Logo"/><br/>This site is intended for the use of AFRL personnel, government Strategic Partners, or registered AFRL controlled website users only. Do not reproduce or distribute the content of this site to a wider audience without coordination with the information owner and your unit public affairs office. See <a tabindex="-1" href="https://ebs.afrl.af.mil/EBSLogin/government_warning.asp" target="new">details</a>. You are accessing a U.S. Government (USG) Information System (IS) that is provided for USG - authorized use only. This Air Force website and page is compliant with <a tabindex="-1" href="http://www.section508.gov/" target="new">Section 508 standards</a>.
        </div>

        <div class="footer_application">
            <br/>Application Version: CTM (${grailsApplication.metadata['app.buildProfile']} - ${grailsApplication.metadata['app.buildDate']} - ${grailsApplication.metadata['app.version']}.${grailsApplication.metadata['app.buildNumber']})
        </div>
    </div>
</div>
