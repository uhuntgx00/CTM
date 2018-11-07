
<%@ page import="mil.ebs.ctm.Attendee" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'attendee.label', default: 'Attendee')}" />
		<title>Attendee Confirmed Attendance List</title>
	</head>
	<body>
		<a href="#list-attendee" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-attendee" class="content scaffold-list" role="main">
			<h1>Attendee Confirmed Attendance List&nbsp;[${attendeeInstanceCount}]</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
			<thead>
					<tr>
                        <g:sortableColumn property="accountLink" title="Account"/>
						<g:sortableColumn property="attendanceType" title="${message(code: 'attendee.attendanceType.label', default: 'Attendance Type')}" />
						<g:sortableColumn property="conference" title="${message(code: 'attendee.conference.label', default: 'Conference')}" />
                        <g:sortableColumn property="supervisor" title="Supervisor"/>
                        <th><g:message code="attendee.estimateCost.label" default="Estimate Cost" /></th>
                        <th><g:message code="attendee.actualCost.label" default="Actual Cost" /></th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${attendeeInstanceList}" status="i" var="attendeeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:link action="show" id="${attendeeInstance.id}">${attendeeInstance}</g:link>
                        </td>
						<td>${fieldValue(bean: attendeeInstance, field: "attendanceType")}</td>
						<td>${fieldValue(bean: attendeeInstance, field: "conference")}</td>
						<td>
                            <g:if test="${attendeeInstance?.supervisor}">
                                ${fieldValue(bean: attendeeInstance, field: "supervisor")}
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
