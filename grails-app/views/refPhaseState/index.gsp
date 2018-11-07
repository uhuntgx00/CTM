
<%@ page import="mil.ebs.ctm.ref.RefPhaseState" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refPhaseState.label', default: 'RefPhaseState')}" />
		<title>Phase State List</title>
	</head>
	<body>
		<a href="#list-refPhaseState" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-refPhaseState" class="content scaffold-list" role="main">
			<h1>Phase State List [${refPhaseStateInstanceCount}]</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
						<g:sortableColumn property="phaseState" title="${message(code: 'refPhaseState.phaseState.label', default: 'Phase State')}" />
						<g:sortableColumn property="phaseAction" title="${message(code: 'refPhaseState.phaseAction.label', default: 'Phase Action')}" />
    					<g:sortableColumn property="actionStatus" title="${message(code: 'refPhaseState.actionStatus.label', default: 'Action Status')}" />
						<g:sortableColumn property="actionCommand" title="${message(code: 'refPhaseState.actionCommand.label', default: 'Action Command')}" />
						<g:sortableColumn property="actionPermission" title="${message(code: 'refPhaseState.actionPermission.label', default: 'Action Permission')}" />
						<g:sortableColumn property="actionCheck" title="${message(code: 'refPhaseState.actionCheck.label', default: 'Action Check')}" />
                        <g:sortableColumn property="actionNotification" title="${message(code: 'refPhaseState.actionNotification.label', default: 'Action Notification')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${refPhaseStateInstanceList}" status="i" var="refPhaseStateInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${refPhaseStateInstance.id}">${fieldValue(bean: refPhaseStateInstance, field: "phaseState")}</g:link></td>
						<td>${fieldValue(bean: refPhaseStateInstance, field: "phaseAction")}</td>
						<td>${fieldValue(bean: refPhaseStateInstance, field: "actionStatus")}</td>
						<td>${fieldValue(bean: refPhaseStateInstance, field: "actionCommand")}</td>
						<td>${fieldValue(bean: refPhaseStateInstance, field: "actionPermission")}</td>
						<td>${fieldValue(bean: refPhaseStateInstance, field: "actionCheck")}</td>
                        <td>${fieldValue(bean: refPhaseStateInstance, field: "actionNotification")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${refPhaseStateInstanceCount > 200}">
                <div class="pagination">
                    <g:paginate total="${refPhaseStateInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
