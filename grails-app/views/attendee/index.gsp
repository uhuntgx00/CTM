
<%@ page import="mil.ebs.ctm.Attendee" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'attendee.label', default: 'Attendee')}" />
		<title><g:message code="default.list.label" args="[entityName]" />&nbsp;(${listType})</title>
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

			<table>
    			<thead>
					<tr>
                        <g:sortableColumn property="accountLink" title="Attendee"/>
						<th>Organization (TD)</th>
                        <g:sortableColumn property="status" title="Status" />
						<g:sortableColumn property="attendanceType" title="${message(code: 'attendee.attendanceType.label', default: 'Attendance Type')}" />
						<g:sortableColumn property="conference" title="${message(code: 'attendee.conference.label', default: 'Conference')}" />
                        <g:sortableColumn property="supervisor" title="Supervisor"/>
                        <th><g:message code="attendee.estimateCost.label" default="Estimate Cost" /></th>
                        <th><g:message code="attendee.actualCost.label" default="Actual Cost" /></th>
					</tr>
				</thead>
				<tbody>
                    <g:form method="post" action="${indexEvent}">
                        <g:hiddenField name="id" value="${organizationId}" />
                        <g:hiddenField name="acctId" value="${accountId}" />
                        <tr class="odd">
                            <td><g:textField name="searchAttendee" value="${searchAttendee}" size="20" />&nbsp;<g:actionSubmitImage value="filter" action="${indexEvent}" src="${resource(dir: 'images/icons', file:'search_512.png')}" align="top" width="18" height="18" title="Search for Attendee" alt="Search for Attendee"/></td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                    </g:form>
                    <g:each in="${attendeeInstanceList}" status="i" var="attendeeInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td class="no_underline"><g:link action="show" id="${attendeeInstance.id}">${attendeeInstance}</g:link></td>
                            <td>
                                <g:if test="${attendeeInstance?.accountLink}">
                                    <g:link action="show" controller="organization" id="${attendeeInstance?.accountLink?.assignedTD?.id}">${attendeeInstance?.accountLink?.assignedTD}</g:link>
                                </g:if>
                                <g:else>
                                    <g:link action="show" controller="organization" id="${attendeeInstance?.reservedTD?.id}">${attendeeInstance?.reservedTD}</g:link>
                                </g:else>
                            </td>
                            <td>${fieldValue(bean: attendeeInstance, field: "status")}</td>
                            <td>${fieldValue(bean: attendeeInstance, field: "attendanceType")}</td>
                            <td class="no_underline"><g:link action="show" controller="conference" id="${attendeeInstance?.conference?.id}">${fieldValue(bean: attendeeInstance, field: "conference")}</g:link></td>
                            <td>
                                <g:if test="${attendeeInstance?.supervisor}">
                                    <g:link action="show" controller="account" id="${attendeeInstance?.supervisor?.id}">${fieldValue(bean: attendeeInstance, field: "supervisor")}</g:link>
                                </g:if>
                                <g:else>
                                    <b><i>TBD</i></b>
                                </g:else>
                            </td>
                            <td class="no_underline">
                                <g:if test="${attendeeInstance?.hasEstimate() && attendeeInstance?.conference?.canEstimate(attendeeInstance?.id)}">
                                    <g:if test="${attendeeInstance?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendeeInstance?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                                        <span style="color:#990000" alt="Estimate Total" title="Estimate Total"><g:link class="contractorCost" controller="cost" action="conferenceEdit" params="['id': attendeeInstance?.getCostId('Estimate')]"><g:formatNumber number="${attendeeInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></g:link></span>
                                    </g:if>
                                    <g:else>
                                        <span style="color:#777777" alt="Estimate Total" title="Estimate Total"><g:link class="cost" controller="cost" action="conferenceEdit" params="['id': attendeeInstance?.getCostId('Estimate')]"><g:formatNumber number="${attendeeInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></g:link></span>
                                    </g:else>
                                </g:if>
                                <g:else>
                                    <g:if test="${attendeeInstance?.conference?.canEstimate(attendeeInstance?.id)}">
                                        <g:if test="${attendeeInstance?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendeeInstance?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                                            <g:link controller="cost" action="createEstimateCost" params="['attendee.id': attendeeInstance?.id, 'costType': 'Estimate']"><span style="color:#990000">$0.00</span></g:link>
                                        </g:if>
                                        <g:else>
                                            <g:link controller="cost" action="createEstimateCost" params="['attendee.id': attendeeInstance?.id, 'costType': 'Estimate']"><span style="color:#003296">$0.00</span></g:link>
                                        </g:else>
                                    </g:if>
                                    <g:else>
                                        <g:if test="${attendeeInstance?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendeeInstance?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                                            <span style="color:#990000" alt="Estimate Total" title="Estimate Total"><g:formatNumber number="${attendeeInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></span>
                                        </g:if>
                                        <g:else>
                                            <span style="color:#777777" alt="Estimate Total" title="Estimate Total"><g:formatNumber number="${attendeeInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></span>
                                        </g:else>
                                    </g:else>
                                </g:else>
                            </td>
                            <td><g:formatNumber number="${attendeeInstance?.actualTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></td>
                        </tr>
                    </g:each>
				</tbody>
			</table>

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
