
<%@ page import="mil.ebs.ctm.Role" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-role" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-role" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list role">
				<g:if test="${roleInstance?.authority}">
                    <li class="fieldcontain">
                        <span id="authority-label" class="property-label"><g:message code="role.authority.label" default="Authority" /></span>
                        <span class="property-value" aria-labelledby="authority-label"><g:fieldValue bean="${roleInstance}" field="authority"/></span>
                    </li>
				</g:if>
                <g:if test="${roleInstance?.managedRoles}">
                    <li class="fieldcontain">
                        <span id="managedRoles-label" class="property-label"><g:message code="role.managedRoles.label" default="Managed Roles" /></span>
                        <span class="property-value" aria-labelledby="managedRoles-label"><g:fieldValue bean="${roleInstance}" field="managedRoles"/></span>
                    </li>
                </g:if>
			</ol>

			<g:form url="[resource:roleInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${roleInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <sec:ifAnyGranted roles="ROLE_DEVELOPER">
                        <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
