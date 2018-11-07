<%@ page import="mil.ebs.ctm.Attendee; mil.ebs.ctm.ref.RefPhaseState" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <title>CTM Home</title>
    </head>

    <body>
        <a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

        <div id="status_block" role="complementary">
            <sec:ifNotLoggedIn>
                <h1>No VALID Account</h1>
            </sec:ifNotLoggedIn>
            <sec:ifAnyGranted roles="ROLE_AFRL_USER, ROLE_NON_AFRL_USER, ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                <h1>Search Conferences</h1>
                <g:form url="[controller: 'home', action:'search']" id="searchableForm" name="searchableForm" method="GET">
                    <ul>
                        <li><center><g:textField name="q" size="21" value="${params.q}"/></center></li>
                        <li>&nbsp;</li>
                        %{--<li><span style="font-size:10px;padding-top:5px"><i><b>Advanced Search</b></i></span></li>--}%
                        <li>&nbsp;</li>
                        <li>&nbsp;</li>
                        <li>
                            <center><input type="submit" value="Search" /></center>
                        </li>
                    </ul>
                </g:form>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                <br/><hr/><br/>
                <h1>Search<br/>Attendees</h1>
                <g:form url="[controller: 'home', action:'searchAttendee']" id="searchableForm" name="searchableForm" method="GET">
                    <ul>
                        <li><center><g:textField name="qA" size="21" value="${params.qA}"/></center></li>
                        <li>&nbsp;</li>
                        %{--<li><span style="font-size:10px;padding-top:5px"><i><b>Advanced Search</b></i></span></li>--}%
                        <li>&nbsp;</li>
                        <li>&nbsp;</li>
                        <li>
                            <center><input type="submit" value="Search" /></center>
                        </li>
                    </ul>
                </g:form>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_AFRL_USER, ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                <br/><hr/><br/>
                <h1>Conference<br/>Cloud</h1>
                <div id="tagcloud">
                    <center>
                    <g:each in="${termList}" status="i" var="termInstance">
                        <g:link controller="home" action="search" rel="${termInstance?.freq}" params="[q: "${termInstance?.term}"]">${termInstance?.term}</g:link>
                    </g:each>
                    </center>
                    %{--<tc:tagCloud tags="${[green: 5, red: 10, Security: 1, black: 24, Veteran: 17]}" size="${[start: 10, end: 40, unit: 'px']}" color="${[start: '#f00', end: '#00f']}" controller="home" action="search" paramName="q"/>--}%
        </div>
            </sec:ifAnyGranted>
        </div>

        <g:if test="${tdConcurrenceList}">
            <div id="conf_attend" role="main">
                <h1 class="home">Awaiting TD Concurrence</h1>
                <div class="home-confs">
                    <ul class="unstyled">
                        <g:each in="${tdConcurrenceList}" status="i" var="attendeeInstance">
                            <li class="${(i % 2) == 0 ? 'home-info-even' : 'home-info-odd'}">
                                <g:link action="show" controller="attendee" id="${attendeeInstance?.id}">
                                <div class="home-supervisor-approve">
                                    <g:img dir="images/icons" file="blog_512.png" height="72" width="72"/>
                                </div>
                                </g:link>
                                <div class="home-title">
                                    Conference: <g:link action="show" controller="conference" id="${attendeeInstance?.conference?.id}">${attendeeInstance?.conference?.conferenceTitle}</g:link>
                                </div>
                                <div class="home-titlewhere">
                                    <div class="home-title">
                                        <g:link action="show" controller="attendee" id="${attendeeInstance?.id}">${attendeeInstance}</g:link>
                                    </div>
                                    <div class="home-where">
                                        <g:each in="${attendeeInstance.costs}" var="c">
                                            <g:if test="${c.costType.equalsIgnoreCase('Estimate')}">
                                                <g:link controller="cost" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link>
                                                <span style="color:#777777"><i>(<g:formatNumber number="${c?.total()}" type="currency" currencyCode="USD" maxFractionDigits="2" />)</i></span>
                                            </g:if>
                                        </g:each>
                                    </div>
                                    <div class="home-attendees">
                                        &nbsp;
                                    </div>
                                </div>
                                <input id="home-cid" class="home-conf-id" type="hidden" value="366" name="home-cid">
                                <div class="clearb"></div>
                            </li>
                        </g:each>
                    </ul>

                    <g:link class="home-more" action="requestingList" controller="attendee">
                        View more attendees...
                    </g:link>
                </div>
            </div>
        </g:if>

        <g:if test="${supervisorList}">
            <div id="conf_attend" role="main">
                <g:if test="${tdConcurrenceList}">
                    <br/><br/>
                </g:if>
                <h1 class="home">Awaiting Supervisor Approval</h1>
                <div class="home-confs">
                    <ul class="unstyled">
                        <g:each in="${supervisorList}" status="i" var="attendeeInstance">
                            <li class="${(i % 2) == 0 ? 'home-info-even' : 'home-info-odd'}">
                                <g:link action="show" controller="attendee" id="${attendeeInstance?.id}">
                                <div class="home-supervisor-approve">
                                    <g:img dir="images/icons" file="blog_512.png" height="72" width="72"/>
                                </div>
                                </g:link>
                                <div class="home-title">
                                    Conference: <g:link action="show" controller="conference" id="${attendeeInstance?.conference?.id}">${attendeeInstance?.conference?.conferenceTitle}</g:link>
                                </div>
                                <div class="home-titlewhere">
                                    <div class="home-title">
                                        <g:link action="show" controller="attendee" id="${attendeeInstance?.id}">${attendeeInstance}</g:link>
                                    </div>
                                    <div class="home-where">
                                        <g:each in="${attendeeInstance.costs}" var="c">
                                            <g:if test="${c.costType.equalsIgnoreCase('Estimate')}">
                                                <g:link controller="cost" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link>
                                                <span style="color:#777777"><i>(<g:formatNumber number="${c?.total()}" type="currency" currencyCode="USD" maxFractionDigits="2" />)</i></span>
                                            </g:if>
                                        </g:each>
                                    </div>
                                    <div class="home-attendees">
                                        &nbsp;
                                    </div>
                                </div>
                                <input id="home-cid" class="home-conf-id" type="hidden" value="366" name="home-cid">
                                <div class="clearb"></div>
                            </li>
                        </g:each>
                    </ul>

                    <g:link class="home-more" action="supervisorList" controller="attendee">
                        View more attendees...
                    </g:link>
                </div>
            </div>
        </g:if>

        <g:if test="${attendingList}">
            <div id="conf_attend" role="main">
                <g:if test="${tdConcurrenceList || supervisorList}">
                    <br/><br/>
                </g:if>
                <h1 class="home">Conferences Attending</h1>
                <div class="home-confs">
                    <ul class="unstyled">
                        <g:each in="${attendingList}" status="i" var="conferenceInstance">
                            <li class="${(i % 2) == 0 ? 'home-info-even' : 'home-info-odd'}">
                                <g:link action="show" controller="conference" id="${conferenceInstance.id}">
                                <div class="home-date">
                                    <g:if test="${conferenceInstance?.constrainedTotal() < 15000}">
                                        <div class="home-date-top-green">
                                    </g:if>
                                    <g:elseif test="${conferenceInstance?.constrainedTotal() < 20000}">
                                        <div class="home-date-top-yellow">
                                    </g:elseif>
                                    <g:else>
                                        <div class="home-date-top-red">
                                    </g:else>
                                            <div class="home-date-my"><g:formatDate format="MMMM" date="${conferenceInstance?.startDate}" /></div>
                                    </div>
                                    <div class="home-date-bottom">
                                        <div class="home-date-d"><g:formatDate format="dd" date="${conferenceInstance?.startDate}" /></div>
                                        <div class="home-date-my"><g:formatDate format="yyyy" date="${conferenceInstance?.startDate}" /></div>
                                    </div>
                                </div>
                                </g:link>
                                <div class="home-titlewhere">
                                    <div class="home-title">
                                        <g:link action="show" controller="conference" id="${conferenceInstance.id}">${conferenceInstance?.conferenceTitle}</g:link>
                                    </div>
                                    <div class="home-where">
                                        <g:if test="${conferenceInstance?.address}">
                                            <g:if test="${conferenceInstance?.address?.street1}">${conferenceInstance?.address?.street1},&nbsp;</g:if>
                                            ${conferenceInstance?.address?.city}&nbsp;
                                            <g:if test="${conferenceInstance?.address?.state}">${conferenceInstance?.address?.state},&nbsp;</g:if>
                                            <g:if test="${conferenceInstance?.address?.country}"><g:country code="${conferenceInstance?.address?.country}" />&nbsp;&nbsp;</g:if>
                                            ${conferenceInstance?.address?.zipCode}
                                        </g:if>
                                        <g:else>
                                            <span style="color:#777777"><i><b>ADDRESS</b> missing for event!</i></span>
                                        </g:else>
                                    </div>
                                    <div class="home-attendees">
                                        <span style="color:#00009b"><b>${conferenceInstance?.phaseState}<g:if test="${!conferenceInstance?.phaseState?.equalsIgnoreCase(conferenceInstance?.status)}">&nbsp;|&nbsp;${conferenceInstance?.status}</g:if></b></span>
                                        --&nbsp;<b>Attendees:</b>&nbsp;${conferenceInstance?.attendees?.size()}
                                        &nbsp;<span style="color:#777777"><i>(${conferenceInstance?.startDate - new Date()} days until event)</i></span>
                                        <g:each in="${conferenceInstance?.attendees}" var="attendeeInstance">
                                            <g:if test="${attendeeInstance?.accountLink == account}">
                                                <span style="color:#333333; clear: right; float: right; padding: 0; margin: 0; margin-right: 25px">
                                                    <b>${attendeeInstance?.status}</b>&nbsp;&nbsp;&nbsp;
                                                    <span class="buttons">
                                                        <g:link action="show" controller="attendee" id="${attendeeInstance?.id}">Attendee Record</g:link>
                                                    </span>
                                                </span>
                                            </g:if>
                                        </g:each>
                                        %{--<g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Approved") || conferenceInstance?.phaseState?.equalsIgnoreCase("Finalizing")}">--}%
                                            %{--<span style="clear: right; float: right; padding: 0; margin: 0; margin-right: 25px" class="buttons">--}%
                                                %{--<g:link class="padd" controller="attendee" action="registerAttendee" params="[refId: "${registerState?.id}"]">Register</g:link>--}%
                                            %{--</span>--}%
                                        %{--</g:if>--}%
                                    </div>
                                </div>
                                <input id="home-cid" class="home-conf-id" type="hidden" value="366" name="home-cid">
                                <div class="clearb"></div>
                            </li>
                        </g:each>
                    </ul>
                </div>
            </div>
        </g:if>

        <g:if test="${conferenceInstanceList}">
            <div id="conf_next" role="main">
                <g:if test="${tdConcurrenceList || supervisorList || attendingList}">
                    <br/><br/>
                </g:if>
                <h1 class="home">Conferences Coming Up</h1>
                <div class="home-confs">
                    <ul class="unstyled">
                        <g:each in="${conferenceInstanceList}" status="i" var="conferenceInstance">
                            <li class="${(i % 2) == 0 ? 'home-info-even' : 'home-info-odd'}">
                                <g:link action="show" controller="conference" id="${conferenceInstance.id}">
                                <div class="home-date">
                                    <g:if test="${conferenceInstance?.constrainedTotal() < 15000}">
                                        <div class="home-date-top-green">
                                    </g:if>
                                    <g:elseif test="${conferenceInstance?.constrainedTotal() < 20000}">
                                        <div class="home-date-top-yellow">
                                    </g:elseif>
                                    <g:else>
                                        <div class="home-date-top-red">
                                    </g:else>
                                            <div class="home-date-my"><g:formatDate format="MMMM" date="${conferenceInstance?.startDate}" /></div>
                                    </div>
                                    <div class="home-date-bottom">
                                        <div class="home-date-d"><g:formatDate format="dd" date="${conferenceInstance?.startDate}" /></div>
                                        <div class="home-date-my"><g:formatDate format="yyyy" date="${conferenceInstance?.startDate}" /></div>
                                    </div>
                                </div>
                                </g:link>
                                <div class="home-titlewhere">
                                    <div class="home-title">
                                        <g:link action="show" controller="conference" id="${conferenceInstance.id}">${conferenceInstance?.conferenceTitle}</g:link>
                                    </div>
                                    <div class="home-where">
                                        <g:if test="${conferenceInstance?.address}">
                                            <g:if test="${conferenceInstance?.address?.street1}">${conferenceInstance?.address?.street1},&nbsp;</g:if>
                                            ${conferenceInstance?.address?.city}&nbsp;
                                            <g:if test="${conferenceInstance?.address?.state}">${conferenceInstance?.address?.state},&nbsp;</g:if>
                                            <g:if test="${conferenceInstance?.address?.country}"><g:country code="${conferenceInstance?.address?.country}" />&nbsp;&nbsp;</g:if>
                                            ${conferenceInstance?.address?.zipCode}
                                        </g:if>
                                        <g:else>
                                            <span style="color:#777777"><i><b>ADDRESS</b> missing for event!</i></span>
                                        </g:else>
                                    </div>
                                    <div class="home-attendees">
                                        <span style="color:#00009b"><b>${conferenceInstance?.phaseState}<g:if test="${!conferenceInstance?.phaseState?.equalsIgnoreCase(conferenceInstance?.status)}">&nbsp;|&nbsp;${conferenceInstance?.status}</g:if></b></span>
                                        --&nbsp;<b>Attendees:</b>&nbsp;${conferenceInstance?.attendees?.size()}
                                        &nbsp;<span style="color:#777777"><i>(${conferenceInstance?.startDate - new Date()} days until event)</i></span>
                                        %{--<div id="compactAll"></div>--}%
                                        <g:if test="${conferenceInstance?.checkAction('isNotAttending') && conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">
                                            <span style="clear: right; float: right; padding: 0; margin: 0; margin-right: 25px" class="buttons">
                                                <g:link class="padd" action="attendConference" resource="${conferenceInstance}" params="[refId: "${attendState?.id}"]">Attend</g:link>
                                            </span>
                                        </g:if>
                                    </div>
                                </div>
                                <input id="home-cid" class="home-conf-id" type="hidden" value="366" name="home-cid">
                                <div class="clearb"></div>
                            </li>
                        </g:each>
                    </ul>

                    <g:link class="home-more" action="index" controller="conference">
                        View more events...
                    </g:link>
                </div>
            </div>
        </g:if>

        <script type="text/javascript">
            $(document).ready(function(){
                $.fn.tagcloud.defaults = {
                  size: {start: 10, end: 24, unit: "px"},
                  color: {start: '#f00', end: '#00f'}
                };

                $("#tagcloud a").tagcloud();

//                $('#compactAll').countdown({until: new Date() + 4, compact: true, format: 'YOD', description: ''});
            })
        </script>

    </body>
</html>
