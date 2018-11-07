
<%@ page import="mil.ebs.ctm.ref.RefDateGate" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refDateGate.label', default: 'RefDateGate')}" />
		<title>Date Gate List</title>
	</head>
	<body>
		<a href="#list-refDateGate" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-refDateGate" class="content scaffold-list" role="main">
			<h1>Date Gate List&nbsp;[${refDateGateInstanceCount}]</h1>

            <g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

            <table>
			    <thead>
					<tr>
						<g:sortableColumn property="code" title="${message(code: 'refDateGate.code.label', default: 'Code')}" />
						<g:sortableColumn property="name" title="${message(code: 'refDateGate.name.label', default: 'Name')}" />
						<g:sortableColumn property="canDelete" title="${message(code: 'refDateGate.canDelete.label', default: 'Can Delete')}" />
						<g:sortableColumn property="onDisplay" title="${message(code: 'refDateGate.onDisplay.label', default: 'On Display')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${refDateGateInstanceList}" status="i" var="refDateGateInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${refDateGateInstance.id}">${fieldValue(bean: refDateGateInstance, field: "code")}</g:link></td>
						<td>${fieldValue(bean: refDateGateInstance, field: "name")}</td>
						<td><g:formatBoolean boolean="${refDateGateInstance.canDelete}" /></td>
						<td><g:formatBoolean boolean="${refDateGateInstance.onDisplay}" /></td>
					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${refDateGateInstanceCount > 10}">
                <div class="pagination">
                    <g:paginate total="${refDateGateInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
