<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'fullcalendar.css')}" type="text/css">
        <g:javascript library="full_calendar"/>
        <r:require module="export"/>
	</head>
	<body>
		<a href="#show-conference" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-conference" class="content scaffold-show" role="main">

            <div class="right_panel">
                <g:render template="statusBlock"/>
                <!--[if (gt IE 8)|!(IE)]><!-->
                    <g:render template="calendarBlock"/>
                <!--<![endif]-->
            </div>

			<h1>
                <g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">
                    <g:img dir="images/icons" file="unlock_512.png" height="48" width="48"/>
                </g:if>
                <g:else>
                    <g:img dir="images/icons" file="lock_512.png" height="48" width="48"/>
                </g:else>
                Show Conference
                <sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                    <g:link controller="Conference" action="exportConference" params="[conferenceId: "${conferenceInstance?.id}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Conference Export to Excel" title="Conference Export to Excel"/></g:link>
                    <g:link controller="Conference" action="exportAAR" params="[conferenceId: "${conferenceInstance?.id}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="AF AAR Export to Excel" title="AF AAR Export to Excel"/></g:link>
                    <g:link controller="Conference" action="exportAAR_non" params="[conferenceId: "${conferenceInstance?.id}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Non-AF AAR Export to Excel" title="Non-AF AAR Export to Excel"/></g:link>
                </sec:ifAnyGranted>
                <sec:ifNotGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                    <g:if test="${conferenceInstance?.isCAO()}">
                        <g:link controller="Conference" action="exportConference" params="[conferenceId: "${conferenceInstance?.id}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Conference Export to Excel" title="Conference Export to Excel"/></g:link>
                    </g:if>
                </sec:ifNotGranted>
            </h1>

			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list conference">

                <g:render template="trackProgress"/>

                <div class="account-block-blue">
                    <div class="ribbon-wrapper">
                        <div class="ribbon-blue">
                            <g:if test="${conferenceInstance?.afrlHosted}">AFRL</g:if>
                            <g:else>
                                <g:if test="${conferenceInstance?.hostType?.equalsIgnoreCase("AF Hosted")}">AIR FORCE</g:if>
                                <g:if test="${conferenceInstance?.hostType?.equalsIgnoreCase("AF Co-Hosted")}">AF CO</g:if>
                            </g:else>
                            <g:if test="${conferenceInstance?.hostType?.equalsIgnoreCase("DoD Hosted")}">DoD</g:if>
                            <g:if test="${conferenceInstance?.hostType?.equalsIgnoreCase("Non-DoD Hosted")}">
                                <g:if test="${conferenceInstance?.nonHostType?.equalsIgnoreCase("Other US Govt Hosted")}">US Govt</g:if>
                                <g:if test="${conferenceInstance?.nonHostType?.equalsIgnoreCase("Foreign Govt Hosted")}">Foreign</g:if>
                                <g:if test="${conferenceInstance?.nonHostType?.equalsIgnoreCase("Professional Society")}">Pro</g:if>
                                <g:if test="${conferenceInstance?.nonHostType?.equalsIgnoreCase("Academia")}">Academia</g:if>
                                <g:if test="${conferenceInstance?.nonHostType?.equalsIgnoreCase("Other")}">Other</g:if>
                            </g:if>
                        </div>
                    </div>

                    <li class="fieldcontain">
                        <span id="conferenceTitle-label" class="property-label">
                            <b><g:message code="conference.conferenceTitle.label" default="Conference Title" /></b>
                        </span>
                        <span class="property-value" aria-labelledby="conferenceTitle-label">
                            <b><g:fieldValue bean="${conferenceInstance}" field="conferenceTitle"/></b>
                        </span>
                    </li>

                    <g:if test="${conferenceInstance?.primaryHost}">
                    <li class="fieldcontain">
                        <span id="primaryHost-label" class="property-label"><g:message code="conference.primaryHost.label" default="Primary Host" /></span>
                        <span class="property-value" aria-labelledby="primaryHost-label"><g:fieldValue bean="${conferenceInstance}" field="primaryHost"/></span>
                    </li>
                    </g:if>
			
                    <li class="fieldcontain">
                        <span id="startDate-label" class="property-label"><g:message code="conference.dates.label" default="Dates" /></span>
                        <span class="property-value" aria-labelledby="startDate-label">
                            <g:formatDate date="${conferenceInstance?.startDate}" type="date" dateStyle="MEDIUM"/>
                            &nbsp;<span style="color:#777777">to</span>&nbsp;
                            <g:formatDate date="${conferenceInstance?.endDate}" type="date" dateStyle="MEDIUM"/>
                            &nbsp;<span style="color:#777777"><i>${conferenceInstance?.days()} day(s)</i></span>
                        </span>
                    </li>

                    %{--<g:if test="${conferenceInstance?.displayAfter}">--}%
                    %{--<li class="fieldcontain">--}%
                        %{--<span id="displayAfter-label" class="property-label"><g:message code="conference.displayAfter.label" default="Display After" /></span>--}%
                        %{--<span class="property-value" aria-labelledby="displayAfter-label">--}%
                            %{--<g:formatDate date="${conferenceInstance?.displayAfter}" type="date" dateStyle="Full"/>--}%
                        %{--</span>--}%
                    %{--</li>--}%
                    %{--</g:if>--}%

                    <g:if test="${conferenceInstance?.venue}">
                    <li class="fieldcontain">
                        <span id="venue-label" class="property-label"><g:message code="conference.venue.label" default="Venue" /></span>
                        <span class="property-value" aria-labelledby="venue-label">
                            <g:fieldValue bean="${conferenceInstance}" field="venue"/>

                            <g:if test="${conferenceInstance?.address}">
                                <g:form id="addressForm" url="[resource:conferenceInstance?.address, action:'delete']" method="DELETE">
                                    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
                                        <g:link controller="address" action="conferenceEdit" id="${conferenceInstance?.address?.id}">
                                            <g:if test="${conferenceInstance?.address?.street1}">${conferenceInstance?.address?.street1},&nbsp;</g:if>
                                            ${conferenceInstance?.address?.city}&nbsp;
                                            <g:if test="${conferenceInstance?.address?.state}">${conferenceInstance?.address?.state},&nbsp;</g:if>
                                            <g:if test="${conferenceInstance?.address?.country}">
                                                <g:country code="${conferenceInstance?.address?.country}"/>&nbsp;&nbsp;
                                            </g:if>
                                            ${conferenceInstance?.address?.zipCode}
                                        </g:link>
                                    </sec:ifAnyGranted>
                                    <sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
                                        <g:if test="${conferenceInstance?.address?.street1}">${conferenceInstance?.address?.street1},&nbsp;</g:if>
                                        ${conferenceInstance?.address?.city}&nbsp;
                                        <g:if test="${conferenceInstance?.address?.state}">${conferenceInstance?.address?.state},&nbsp;</g:if>
                                        <g:if test="${conferenceInstance?.address?.country}">
                                            <g:country code="${conferenceInstance?.address?.country}"/>&nbsp;&nbsp;
                                        </g:if>
                                        ${conferenceInstance?.address?.zipCode}
                                    </sec:ifNotGranted>
                                    &nbsp;
                                    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_FMC_ADMIN">
                                        <g:link controller="address" action="deleteVenueAddress" id="${conferenceInstance?.address?.id}" params="['conference.id': conferenceInstance?.id]" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                                            <g:img dir="images" file="reject_16n.png" alt="Remove Address" title="Remove Address"/>
                                        </g:link>
                                    </sec:ifAnyGranted>
                                </g:form>
                            </g:if>
                            <g:else>
                                <br/><i><span style="font-size:12px; color:#999999">- To add a <b>VENUE ADDRESS</b> select option below <b>(if available)</b></span></i>
                            </g:else>
                        </span>
                    </li>
                    </g:if>
			
                    <g:if test="${conferenceInstance?.perdiem}">
                        <li class="fieldcontain">
                            <span id="perdiem-label" class="property-label"><g:message code="conference.perdiem.label" default="Per Diem" /></span>
                            <span class="property-value" aria-labelledby="perdiem-label">
                                <b>$${conferenceInstance?.perdiem}</b>/night <span style="color:#777777">(Lodging)</span> | <b>$${conferenceInstance?.meals}</b>/day <span style="color:#777777">(Meals)</span>
                            </span>
                        </li>
                    </g:if>

                    <g:if test="${conferenceInstance?.lastChangeDate}">
                    <li class="fieldcontain">
                        <span id="lastChangeDate-label" class="property-label"><g:message code="conference.lastChangeDate.label" default="Last Change" /></span>
                        <span class="property-value" aria-labelledby="lastChangeDate-label">
                            <g:formatDate date="${conferenceInstance?.lastChangeDate}" type="date" style="MEDIUM"/>
                            <span style="color:#999999"><i>(${conferenceInstance?.getChangeDays()} days)</i></span>
                                <g:if test="${conferenceInstance?.lastChange}">
                                    &nbsp;-&nbsp;<span style="color:#006dba"><i>${conferenceInstance?.lastChange}</i></span>
                                </g:if>
                        </span>
                    </li>
                    </g:if>
                </div>

                %{--<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">--}%
                <br/>
                <div class="account-block">
                    <li class="fieldcontain">
                        <span id="comments-label" class="property-label">
                            <g:img dir="images/icons" file="chat_512.png" height="16" width="16"/>
                            <g:message code="conference.comments.label" default="Comments" />&nbsp;<span style="color:#999999"><b>(${conferenceInstance?.comments?.size()})</b></span>
                        </span>
                        <span class="property-value" aria-labelledby="comments-label">
                            <g:if test="${displayComment}">
                                <span style="color:#999999;font-size:12px"><i><g:formatDate date="${displayComment?.when}" type="date" style="FULL"/><g:if test="${displayComment?.phase}">&nbsp;(<b>${displayComment?.phase}</b>)</g:if></i></span><br/>
                                ${displayComment?.eComment}<br/>
                                <span style="color:#999999;font-size:12px"><i>${displayComment?.who}</i></span>
                            </g:if>
                            <g:else>
                                <i><span style="font-size:12px; color:#999999">There are no current comments for this conference.</span></i>
                            </g:else>
                            <br/><br/>
                            <i><span style="color:#666666"><b>To add/view comments</b>&nbsp;<g:link action="showComments" resource="${conferenceInstance}" ><g:img dir="images/icons" file="more_512.png" height="32" width="32" title="Add/View Comments" alt="Add/View Comments"/></g:link></span></i>
                        </span>
                    </li>
                </div>
                %{--</sec:ifAnyGranted>--}%

                <br/>
                <div class="account-block">
                    <li class="fieldcontain">
                        <span id="website-label" class="property-label"><g:message code="conference.website.label" default="Website" /></span>
                        <span class="property-value" aria-labelledby="website-label">
                            <g:if test="${conferenceInstance?.website}">
                                <a href="${conferenceInstance?.website}" target="_open"><g:fieldValue bean="${conferenceInstance}" field="website"/></a>
                            </g:if>
                            <g:else>
                                <i><span style="font-size:12px; color:#999999"><b>WEBSITE</b> not set for conference</span></i>
                            </g:else>
                        </span>
                    </li>

                    <li class="fieldcontain">
                        <span id="purpose-label" class="property-label"><g:message code="conference.purpose.label" default="Purpose" /></span>
                        <span class="property-value" aria-labelledby="purpose-label">
                            <g:if test="${conferenceInstance?.purpose}">
                                ${conferenceInstance?.purpose}
                            </g:if>
                            <g:else>
                                <i><span style="font-size:12px; color:#999999"><b>PURPOSE</b> not defined for conference</span></i>
                            </g:else>
                        </span>
                    </li>
                </div>

                <g:if test="${conferenceInstance?.approvalNotice}">
                    <br/>
                    <div class="account-block">
                        <li class="fieldcontain">
                            <span id="approvalNotice-label" class="property-label"><g:message code="conference.approvalNotice.label" default="approvalNotice" /></span>
                            <span class="property-value" aria-labelledby="approvalNotice-label">
                                ${conferenceInstance?.approvalNotice}
                            </span>
                        </li>
                    </div>
                </g:if>

				%{--<g:if test="${conferenceInstance?.hostType}">--}%
				%{--<li class="fieldcontain">--}%
					%{--<span id="hostType-label" class="property-label"><g:message code="conference.hostType.label" default="Host Type" /></span>--}%
					%{--<span class="property-value" aria-labelledby="hostType-label"><g:fieldValue bean="${conferenceInstance}" field="hostType"/></span>--}%
				%{--</li>--}%
				%{--</g:if>--}%
			
                <g:if test="${conferenceInstance?.responsibleTD || conferenceInstance?.responsibleOrg || conferenceInstance?.conferenceAO || conferenceInstance?.alternateCAO}">
                    <br/>
                    <div class="account-block-icon">
                        <g:if test="${conferenceInstance?.responsibleTD}">
                            <span id="responsibleTD-icon">
                                <g:img dir="/images/td" height="100px" width="100px" file="${conferenceInstance?.responsibleTD?.getTopParent()?.officeSymbol?.replaceAll("AFRL/","")?.replaceAll(" ","_")?.toLowerCase()}_logo.png" alt="${conferenceInstance?.responsibleTD?.officeSymbol} Logo" title="${conferenceInstance?.responsibleTD?.officeSymbol} Logo"/>
                            </span>
                        </g:if>

                        <g:if test="${conferenceInstance?.responsibleTD}">
                        <li class="fieldcontain">
                            <span id="responsibleTD-label" class="property-label"><g:message code="conference.responsibleTD.label" default="Responsible TD" /></span>
                            <span class="property-value" aria-labelledby="responsibleTD-label"><g:link controller="organization" action="show" id="${conferenceInstance?.responsibleTD?.id}">${conferenceInstance?.responsibleTD?.encodeAsHTML()}</g:link></span>
                        </li>
                        </g:if>

                        <g:if test="${conferenceInstance?.responsibleOrg}">
                        <li class="fieldcontain">
                            <span id="responsibleOrg-label" class="property-label">EXT Responsible<br/>Organization</span>
                            <span class="property-value" aria-labelledby="responsibleOrg-label"><g:fieldValue bean="${conferenceInstance}" field="responsibleOrg"/></span>
                        </li>
                        </g:if>

                        <g:if test="${conferenceInstance?.conferenceAO}">
                        <li class="fieldcontain">
                            <span id="conferenceAO-label" class="property-label">
                                <g:message style="vertical-align: middle" code="conference.conferenceAO.label" default="Conference AO" />
                            </span>
                            <span class="property-value" aria-labelledby="conferenceAO-label">
                                <g:link style="vertical-align: middle" controller="account" action="show" id="${conferenceInstance?.conferenceAO?.id}">${conferenceInstance?.conferenceAO?.encodeAsHTML()}</g:link>
                                <a style="vertical-align: middle" href="mailto:${conferenceInstance?.conferenceAO?.emailAddress}"><g:img style="vertical-align: middle" dir="/images/icons" file="mail_512.png" width="24" height="24" alt="Send Mail" title="Send Mail"/></a>
                            </span>
                        </li>
                        </g:if>
                        <g:else>
                            <li class="fieldcontain">
                                <span id="conferenceAO-label" class="property-label">
                                    <g:message style="vertical-align: middle" code="conference.conferenceAO.label" default="Conference AO" />
                                </span>
                                <span class="property-value" aria-labelledby="conferenceAO-label">
                                    <i><span style="color:#999999">There is no current assigned CAO.</span></i>
                                </span>
                            </li>
                        </g:else>

                        <g:if test="${conferenceInstance?.alternateCAO}">
                        <li class="fieldcontain">
                            <span id="alternateCAO-label" class="property-label">
                                <g:message style="vertical-align: middle" code="conference.alternateCAO.label" default="Alternate CAO" />
                            </span>
                            <span class="property-value" aria-labelledby="alternateCAO-label">
                                <g:link style="vertical-align: middle" controller="account" action="show" id="${conferenceInstance?.alternateCAO?.id}">${conferenceInstance?.alternateCAO?.encodeAsHTML()}</g:link>
                                <a style="vertical-align: middle" href="mailto:${conferenceInstance?.alternateCAO?.emailAddress}"><g:img style="vertical-align: middle" dir="/images/icons" file="mail_512.png" width="24" height="24" alt="Send Mail" title="Send Mail"/></a>
                            </span>
                        </li>
                        </g:if>
                    </div>
                </g:if>

                <g:if test="${conferenceInstance?.afrlSoccer || conferenceInstance?.afmcSoccer || conferenceInstance?.safTmt || conferenceInstance?.primarySponsor}">
                    <br/>
                    <div class="account-block">
                        <g:if test="${conferenceInstance?.afrlSoccer}">
                        <li class="fieldcontain">
                            <span id="afrlSoccer-label" class="property-label"><g:message code="conference.afrlSoccer.label" default="AFRL SOCCER#" /></span>
                            <span class="property-value" aria-labelledby="afrlSoccer-label">
                                <g:fieldValue bean="${conferenceInstance}" field="afrlSoccer"/>
                                &nbsp;<span style="color:#777777"><b>(<g:formatDate date="${conferenceInstance?.afrlSoccerDate}" type="date" dateStyle="MEDIUM"/>)</b></span>
                            </span>
                        </li>
                        </g:if>

                        <g:if test="${conferenceInstance?.afmcSoccer}">
                        <li class="fieldcontain">
                            <span id="afmcSoccer-label" class="property-label"><g:message code="conference.afmcSoccer.label" default="AFMC SOCCER#" /></span>
                            <span class="property-value" aria-labelledby="afmcSoccer-label">
                                <g:fieldValue bean="${conferenceInstance}" field="afmcSoccer"/>
                                &nbsp;<span style="color:#777777"><b>(<g:formatDate date="${conferenceInstance?.afmcSoccerDate}" type="date" dateStyle="MEDIUM"/>)</b></span>
                            </span>
                        </li>
                        </g:if>

                        <g:if test="${conferenceInstance?.safTmt}">
                        <li class="fieldcontain">
                            <span id="safTmt-label" class="property-label"><g:message code="conference.safTmt.label" default="SAF TMT#" /></span>
                            <span class="property-value" aria-labelledby="safTmt-label">
                                <g:fieldValue bean="${conferenceInstance}" field="safTmt"/>
                                &nbsp;<span style="color:#777777"><b>(<g:formatDate date="${conferenceInstance?.safTmtDate}" type="date" dateStyle="MEDIUM"/>)</b></span>
                            </span>
                        </li>
                        </g:if>

                        <g:if test="${conferenceInstance?.primarySponsor}">
                        <li class="fieldcontain">
                            <span id="primarySponsor-label" class="property-label"><g:message code="conference.primarySponsor.label" default="Primary Sponsor" /></span>
                            <span class="property-value" aria-labelledby="primarySponsor-label"><g:fieldValue bean="${conferenceInstance}" field="primarySponsor"/></span>
                        </li>
                        </g:if>
                    </div>
                </g:if>

                <br/>
                <div class="account-block emphasize norightpadding">
                    <g:if test="${conferenceInstance?.attendees}">
                        <g:render template="attendeeBlock"/>
                    </g:if>
                    <g:else>
                        <li class="fieldcontain">
                            <span id="attendeesMissing-label" class="property-label"><g:message code="conference.attendees.label" default="Attendees" /></span>
                            <span class="property-value" aria-labelledby="attendeesMissing-label">
                                <i><span style="font-size:12px; color:#999999">No <b>ATTENDEES</b> assigned to conference</span></i>
                            </span>
                        </li>
                    </g:else>
                </div>

                <g:if test="${conferenceInstance.getFiles().size()>0}">
                    <br/>
                    <div class="account-block">
                        <li class="fieldcontain">
                            <span id="files-label" class="property-label">
                                Attached Files <g:img dir="/images/icons" file="Page-3-512.png" height="16" width="16" alt="Save file to desktop, edit, and re-upload." title="Save file to desktop, edit, and re-upload."/>
                            </span>
                            <g:each in="${conferenceInstance.getFiles()}" var="f">
                                <span class="property-value" aria-labelledby="files-label">
                                    <span alt="${f.comments}" title="${f.comments}">${f.toHtmlString()}</span>
                                    <sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                        <g:link action="retrieveFile" id="${f.id}" absolute="true" params="[conferenceId: "${conferenceInstance.id}"]"><g:img dir="/images/icons" file="Arrow_42d-512.png" height="14" width="14" alt="Retrieve File" title="Retrieve File"/></g:link>
                                    </sec:ifAnyGranted>
                                    <sec:ifNotGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                        <g:if test="${conferenceInstance?.isCAO()}">
                                            <g:link action="retrieveFile" id="${f.id}" absolute="true" params="[conferenceId: "${conferenceInstance.id}"]"><g:img dir="/images/icons" file="Arrow_42d-512.png" height="14" width="14" alt="Retrieve File" title="Retrieve File"/></g:link>
                                        </g:if>
                                    </sec:ifNotGranted>
                                    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_DEVELOPER">
                                        <g:link action="deleteFile" id="${f.id}" absolute="true" params="[conferenceId: "${conferenceInstance.id}"]" onclick="return confirm('${message(code: 'default.button.deletefile.confirm.message', default: 'Are you sure you want to delete this File?')}');"><g:img dir="/images" file="reject_16n.png" alt="Delete file" title="Delete file"/></g:link>
                                    </sec:ifAnyGranted>
                                </span>
                            </g:each>
                        </li>
                    </div>
                </g:if>

                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                    <br/>
                    <div class="account-block">
                        <li class="fieldcontain">
                            <span id="files-label" class="property-label">
                                Date Events <g:img dir="/images/icons" file="Book_5-512.png" height="16" width="16" alt="Date events pertaining to the conference" title="Date events pertaining to the conference"/>
                            </span>
                            <g:each in="${conferenceInstance.getDateEvents().sort {it.id}}" var="d">
                                <span class="property-value" aria-labelledby="files-label">
                                    <g:if test="${d?.dateGate?.onDisplay}">
                                        <b><i>${d?.dateGate?.toString()}</i></b>&nbsp; (<g:formatDate date="${d?.eventDate}" type="date" dateStyle="Full"/>)&nbsp;-&nbsp;${d?.shortRecordedBy()}
                                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_DEVELOPER">
                                            <g:if test="${d?.dateGate?.canDelete}">
                                                <g:link action="deleteDateEvent" id="${d?.id}" absolute="true" params="[conferenceId: "${conferenceInstance.id}"]" onclick="return confirm('${message(code: 'default.button.deletedateevent.confirm.message', default: 'Are you sure you want to delete this Date Event?')}');"><g:img dir="/images" file="reject_16n.png" alt="Delete date event" title="Delete date event"/></g:link>
                                            </g:if>
                                        </sec:ifAnyGranted>
                                    </g:if>
                                </span>
                            </g:each>
                        </li>
                    </div>
                </sec:ifAnyGranted>
			</ol>

            <g:form id="showForm">
                <g:hiddenField id="ctmJprompt" name="ctmJprompt" value="${ctmJprompt}"/>
				<fieldset class="buttons">
                    <g:each in="${conferenceInstance?.availableActions()}" var="a">
                        <sec:ifAnyGranted roles="${a?.actionPermission}">
                            <g:if test="${conferenceInstance?.checkPermission(a?.actionCommand)}">
                                <g:if test="${conferenceInstance?.checkAction(a?.actionCheck)}">
                                    <g:if test="${a?.actionController}">
                                        <g:link elementId="${a?.actionCommand}" controller="${a?.actionController}" class="${a?.buttonClass}" action="${a?.actionCommand}" params="[conferenceId: "${conferenceInstance.id}", refId: "${a.id}"]">${a?.phaseAction}</g:link>
                                    </g:if>
                                    <g:else>
                                        <g:link elementId="${a?.actionCommand}" class="${a?.buttonClass}" action="${a?.actionCommand}" resource="${conferenceInstance}" params="[refId: "${a.id}"]">${a?.phaseAction}</g:link>
                                    </g:else>
                                </g:if>
                                <g:else>
                                    <span class="disabled_buttons">
                                    <g:if test="${a.displayDisabled}">
                                        <a href="#nogo" alt="${conferenceInstance?.checkActionMessage(a?.actionCheck)}" title="${conferenceInstance?.checkActionMessage(a?.actionCheck)}" class="${a?.buttonClass}">${a?.phaseAction}</a>
                                    </g:if>
                                    </span>
                                </g:else>
                            </g:if>
                        </sec:ifAnyGranted>
                        <sec:ifNotGranted roles="${a?.actionPermission}">
                            <g:if test="${a?.actionPermission?.contains("ROLE_CAO") && conferenceInstance?.isCAO()}">
                                <g:if test="${conferenceInstance?.checkPermission(a?.actionCommand)}">
                                    <g:if test="${conferenceInstance?.checkAction(a?.actionCheck)}">
                                        <g:if test="${a?.actionController}">
                                            <g:link elementId="${a?.actionCommand}" controller="${a?.actionController}" class="${a?.buttonClass}" action="${a?.actionCommand}" params="[conferenceId: "${conferenceInstance.id}", refId: "${a.id}"]">${a?.phaseAction}</g:link>
                                        </g:if>
                                        <g:else>
                                            <g:link elementId="${a?.actionCommand}" class="${a?.buttonClass}" action="${a?.actionCommand}" resource="${conferenceInstance}" params="[refId: "${a.id}"]">${a?.phaseAction}</g:link>
                                        </g:else>
                                    </g:if>
                                    <g:else>
                                        <span class="disabled_buttons">
                                        <g:if test="${a?.displayDisabled}">
                                            <a href="#nogo" alt="${conferenceInstance?.checkActionMessage(a?.actionCheck)}" title="${conferenceInstance?.checkActionMessage(a.actionCheck)}" class="${a?.buttonClass}">${a?.phaseAction}</a>
                                        </g:if>
                                        </span>
                                    </g:else>
                                </g:if>
                            </g:if>
                        </sec:ifNotGranted>
                    </g:each>
				</fieldset>
			</g:form>
		</div>

        <script type="text/javascript">
            $(document).ready(function(){
                $( document ).tooltip();

                // page is now ready, initialize the calendar...
                $('#conference_event_calendar').fullCalendar({
                    dayNames: ['S', 'M', 'T', 'W', 'T', 'F', 'S'],
                    dayNamesShort: ['S', 'M', 'T', 'W', 'T', 'F', 'S'],
                    header: {
                        left: 'prev',
                        center: 'title',
                        right: 'next'
                    },
                    titleFormat: 'MMM YYYY',
                    height: "auto",
                    aspectRatio: 1.0,
                    editable: false,
                    eventLimit: true,
                    events: {
                        url: '../viewEventData',
                        data: {
                            cid: '${conferenceInstance?.id}'
                        }
                    },
                    fixedWeekCount: false
                })
            });
        </script>

	</body>
</html>
