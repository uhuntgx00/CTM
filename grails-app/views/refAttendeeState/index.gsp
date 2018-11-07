
<%@ page import="mil.ebs.ctm.ref.RefAttendeeState" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refAttendeeState.label', default: 'RefAttendeeState')}" />
		<title>Attendee State List</title>
	</head>
	<body>
		<a href="#list-refAttendeeState" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-refAttendeeState" class="content scaffold-list" role="main">
			<h1>Attendee State List [${refAttendeeStateInstanceCount}]</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			    <thead>
					<tr>
						<g:sortableColumn property="attendeeState" title="${message(code: 'refAttendeeState.attendeeState.label', default: 'Attendee State')}" />
						<g:sortableColumn property="phaseState" title="${message(code: 'refAttendeeState.phaseState.label', default: 'Phase State')}" />
						<g:sortableColumn property="attendeeAction" title="${message(code: 'refAttendeeState.attendeeAction.label', default: 'Attendee Action')}" />
						<g:sortableColumn property="nextState" title="${message(code: 'refAttendeeState.nextState.label', default: 'Next State')}" />
						<g:sortableColumn property="actionCommand" title="${message(code: 'refAttendeeState.actionCommand.label', default: 'Action Command')}" />
						<g:sortableColumn property="actionPermission" title="${message(code: 'refAttendeeState.actionPermission.label', default: 'Action Permission')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${refAttendeeStateInstanceList}" status="i" var="refAttendeeStateInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${refAttendeeStateInstance.id}">${fieldValue(bean: refAttendeeStateInstance, field: "attendeeState")}</g:link></td>
						<td>${fieldValue(bean: refAttendeeStateInstance, field: "phaseState")}</td>
						<td>${fieldValue(bean: refAttendeeStateInstance, field: "attendeeAction")}</td>
						<td>${fieldValue(bean: refAttendeeStateInstance, field: "nextState")}</td>
						<td>${fieldValue(bean: refAttendeeStateInstance, field: "actionCommand")}</td>
						<td>${fieldValue(bean: refAttendeeStateInstance, field: "actionPermission")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${refAttendeeStateInstanceCount > 200}">
                <div class="pagination">
                    <g:paginate total="${refAttendeeStateInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
