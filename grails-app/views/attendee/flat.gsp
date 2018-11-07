<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.Attendee; mil.ebs.ctm.ref.RefAttendeeState" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'attendee.label', default: 'Attendee')}" />
		<title><g:message code="default.list.label" args="[entityName]" />&nbsp;(${listType})</title>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'flat.css')}" type="text/css">
	</head>
	<body>
		<a href="#list-attendee" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-attendee" class="content scaffold-list" role="main">
            <h1>
                 <g:img dir="images/icons" file="user_512.png" height="48" width="48" alt="Attendee List" title="Attendee List"/>
                 <g:message code="default.list.label" args="[entityName]" />&nbsp;(${listType})&nbsp;&nbsp;[${attendeeInstanceCount}]
                 <g:if test="${exportOption}">
                     <sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                         <g:if test="${searchAttendee}">
                             <g:if test="${params.id}">
                                 <g:link action="${indexEvent}" params="[id: "${params.id}", formatType: "excel", extension: "xls", searchAttendee: "${searchAttendee}"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Attendee(s) Export to Excel" title="Attendee(s) Export to Excel"/></g:link>
                             </g:if>
                             <g:else>
                                 <g:link action="${indexEvent}" params="[formatType: "excel", extension: "xls", searchAttendee: "${searchAttendee}"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Attendee(s) Export to Excel" title="Attendee(s) Export to Excel"/></g:link>
                             </g:else>
                         </g:if>
                         <g:else>
                             <g:if test="${params.id}">
                                 <g:link action="${indexEvent}" params="[id: "${params.id}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Attendee(s) Export to Excel" title="Attendee(s) Export to Excel"/></g:link>
                             </g:if>
                             <g:else>
                                 <g:link action="${indexEvent}" params="[formatType: "excel", extension: "xls"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Attendee(s) Export to Excel" title="Attendee(s) Export to Excel"/></g:link>
                             </g:else>
                         </g:else>
                     </sec:ifAnyGranted>
                 </g:if>
            </h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

            <br/>
            <g:each in="${attendeeInstanceList}" status="i" var="attendeeInstance">
                <div class="requestingAttendee">
                    <div class="requestingInfoBlock">
                        <div class="requestingTypeIcon">
                            <g:if test="${attendeeInstance?.attendanceType?.equalsIgnoreCase('Attendee')}">
                                <g:img style="vertical-align: middle" dir="/images" file="Letter-A-orange-icon.png" width="78" height="78" alt="Attendee" title="Attendee"/>
                            </g:if>
                            <g:if test="${attendeeInstance?.attendanceType?.equalsIgnoreCase('Booth/Display')}">
                                <g:img style="vertical-align: middle" dir="/images" file="Letter-B-blue-icon.png" width="78" height="78" alt="Booth/Display" title="Booth/Display"/>
                            </g:if>
                            <g:if test="${attendeeInstance?.attendanceType?.equalsIgnoreCase('Discussion Panel')}">
                                <g:img style="vertical-align: middle" dir="/images" file="Letter-D-grey-icon.png" width="78" height="78" alt="Discussion Panel" title="Discussion Panel"/>
                            </g:if>
                            <g:if test="${attendeeInstance?.attendanceType?.equalsIgnoreCase('Session Chair')}">
                                <g:img style="vertical-align: middle" dir="/images" file="Letter-C-lg-icon.png" width="78" height="78" alt="Session Chair" title="Session Chair"/>
                            </g:if>
                            <g:if test="${attendeeInstance?.attendanceType?.equalsIgnoreCase('Presenter/Speaker')}">
                                <g:img style="vertical-align: middle" dir="/images" file="Letter-P-dg-icon.png" width="78" height="78" alt="Presenter/Speaker" title="Presenter/Speaker"/>
                            </g:if>
                            <g:if test="${attendeeInstance?.attendanceType?.equalsIgnoreCase('Support')}">
                                <g:img style="vertical-align: middle" dir="/images" file="Letter-S-red-icon.png" width="78" height="78" alt="Support" title="Support"/>
                            </g:if>
                            <g:if test="${attendeeInstance?.attendanceType?.equalsIgnoreCase('Other')}">
                                <g:img style="vertical-align: middle" dir="/images" file="Letter-O-violet-icon.png" width="78" height="78" alt="Other" title="Other"/>
                            </g:if>
                        </div>
                    </div>
                    <div class="requestingWhoBlock">
                        <g:link action="show" id="${attendeeInstance.id}">${attendeeInstance}</g:link>
                        <br/>
                    </div>
                    <div class="requestingAttendeeConference">
                        <g:link action="show" controller="conference" id="${attendeeInstance?.conference?.id}">${fieldValue(bean: attendeeInstance, field: "conference")}</g:link>
                        <g:if test="${attendeeInstance?.accountLink}">
                            (<g:link action="show" controller="organization" id="${attendeeInstance?.accountLink?.assignedTD?.id}">${attendeeInstance?.accountLink?.assignedTD?.officeSymbol}</g:link>)
                        </g:if>
                        <g:else>
                            (<g:link action="show" controller="organization" id="${attendeeInstance?.reservedTD?.id}">&nbsp;(${attendeeInstance?.reservedTD?.officeSymbol}</g:link>)
                        </g:else>
                        <b>
                        <g:if test="${attendeeInstance?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendeeInstance?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                            <span style="color:#b00000"><g:formatNumber number="${attendeeInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></span>
                        </g:if>
                        <g:else>
                            <span style="color:#777777"><g:formatNumber number="${attendeeInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></span>
                        </g:else>
                        </b><i>(est)</i>
                    </div>
                    <div class="requestingAttendeeSupervisor">
                        <g:if test="${attendeeInstance?.supervisor}">
                            <g:link action="show" controller="account" id="${attendeeInstance?.supervisor?.id}">${fieldValue(bean: attendeeInstance, field: "supervisor")}</g:link>
                        </g:if>
                        <g:else>
                            <b><i>Supervisor not assigned</i></b>
                        </g:else>
                    </div>
                    <div class="requestingAttendeeAction">
                        <g:if test="${RefAttendeeState.findByAttendeeStateAndPhaseStateAndAttendeeAction('TD Concurrence', attendeeInstance?.conference?.phaseState, 'Approve')}">
                            <g:link class="pok" action="tdApproveAttendee" resource="${attendeeInstance}" params="[refId: "${RefAttendeeState.findByAttendeeStateAndPhaseStateAndAttendeeAction('TD Concurrence', attendeeInstance?.conference?.phaseState, 'Approve')?.id}"]">Approve</g:link>
                        </g:if>
                        <g:else>
                            <g:link class="pexclaim" action="requestingListFlat"><i>Invalid</i></g:link>
                        </g:else>
                        <g:if test="${RefAttendeeState.findByAttendeeStateAndPhaseStateAndAttendeeAction('TD Concurrence', attendeeInstance?.conference?.phaseState, 'Disapprove')}">
                            <g:link class="pcancel" action="disapproveAttendee" resource="${attendeeInstance}" params="[refId: "${RefAttendeeState.findByAttendeeStateAndPhaseStateAndAttendeeAction('TD Concurrence', attendeeInstance?.conference?.phaseState, 'Disapprove')?.id}"]">Disapprove</g:link>
                        </g:if>
                        <g:else>
                            <g:link class="pexclaim" action="requestingListFlat"><i>Invalid</i></g:link>
                        </g:else>
                    </div>
                    <div class="requestingJustification">
                        <g:if test="${attendeeInstance?.justification}">
                            <span alt="${attendeeInstance?.justification?.replaceAll("\\<.*?>","")}" title="${attendeeInstance?.justification?.replaceAll("\\<.*?>","")}">${attendeeInstance?.justification?.substring(0, Math.min(120, attendeeInstance?.justification ? attendeeInstance?.justification?.length() : 0))}</span>
                        </g:if>
                    </div>
                </div>
            </g:each>

            <g:if test="${attendeeInstanceCount > 25}">
                <div class="pagination">
                    <g:if test="${searchAttendee}">
                        <g:if test="${params.acctId}">
                            <g:paginate total="${attendeeInstanceCount ?: 0}" id="${params.id}" params="['acctId': params.acctId, 'searchAttendee': "${searchAttendee}"]"/>
                        </g:if>
                        <g:else>
                            <g:paginate total="${attendeeInstanceCount ?: 0}" id="${params.id}" params="['searchAttendee': "${searchAttendee}"]"/>
                        </g:else>
                    </g:if>
                    <g:else>
                        <g:if test="${params.acctId}">
                            <g:paginate total="${attendeeInstanceCount ?: 0}" id="${params.id}" params="['acctId': params.acctId]"/>
                        </g:if>
                        <g:else>
                            <g:paginate total="${attendeeInstanceCount ?: 0}" id="${params.id}"/>
                        </g:else>
                    </g:else>
                </div>
            </g:if>
		</div>
	</body>
</html>
