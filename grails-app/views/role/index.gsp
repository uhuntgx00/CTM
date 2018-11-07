
<%@ page import="mil.ebs.ctm.Role" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-role" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-role" class="content scaffold-list" role="main">

			<h1><g:message code="default.list.label" args="[entityName]" />&nbsp;&nbsp;[${roleInstanceCount}]</h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
			    <thead>
					<tr>
                        <g:sortableColumn property="authority" title="${message(code: 'role.authority.label', default: 'Authority')}" />
                        <g:sortableColumn property="managedRoles" title="${message(code: 'role.managedRoles.label', default: 'Managed Roles')}" />
                    </tr>
				</thead>
				<tbody>
				<g:each in="${roleInstanceList}" status="i" var="roleInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${roleInstance.id}">${fieldValue(bean: roleInstance, field: "authority")}</g:link></td>
                        <td>${fieldValue(bean: roleInstance, field: "managedRoles")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${roleInstanceCount>25}">
                <div class="pagination">
                    <g:paginate total="${roleInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
