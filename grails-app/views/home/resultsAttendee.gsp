
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
                            <td class="no_underline"><g:link controller="attendee" action="show" id="${attendeeInstance.id}">${attendeeInstance}</g:link></td>
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
                            <td><g:formatNumber number="${attendeeInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></td>
                            <td><g:formatNumber number="${attendeeInstance?.actualTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></td>
                        </tr>
                    </g:each>
				</tbody>
			</table>

            <g:if test="${attendeeInstanceCount > 25}">
                <div class="pagination">
                    <g:paginate total="${attendeeInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
