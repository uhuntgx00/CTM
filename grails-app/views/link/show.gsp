
<%@ page import="mil.ebs.ctm.Link" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'link.label', default: 'Link')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-link" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-link" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list link">
                <g:if test="${linkInstance?.display}">
                <li class="fieldcontain">
                    <span id="display-label" class="property-label"><g:message code="link.display.label" default="Display" /></span>
                    <span class="property-value" aria-labelledby="display-label"><g:fieldValue bean="${linkInstance}" field="display"/></span>
                </li>
                </g:if>

				<g:if test="${linkInstance?.url}">
				<li class="fieldcontain">
					<span id="url-label" class="property-label"><g:message code="link.url.label" default="Url" /></span>
					<span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${linkInstance}" field="url"/></span>
				</li>
				</g:if>
			
				<g:if test="${linkInstance?.tooltip}">
				<li class="fieldcontain">
					<span id="tooltip-label" class="property-label"><g:message code="link.tooltip.label" default="Tooltip" /></span>
					<span class="property-value" aria-labelledby="tooltip-label"><g:fieldValue bean="${linkInstance}" field="tooltip"/></span>
				</li>
				</g:if>
			
				<g:if test="${linkInstance?.linkType}">
				<li class="fieldcontain">
					<span id="linkType-label" class="property-label"><g:message code="link.linkType.label" default="Link Type" /></span>
					<span class="property-value" aria-labelledby="linkType-label"><g:fieldValue bean="${linkInstance}" field="linkType"/></span>
				</li>
				</g:if>
			
				<g:if test="${linkInstance?.linkColumn}">
				<li class="fieldcontain">
					<span id="linkColumn-label" class="property-label"><g:message code="link.linkColumn.label" default="Column" /></span>
					<span class="property-value" aria-labelledby="linkColumn-label"><g:fieldValue bean="${linkInstance}" field="linkColumn"/></span>
				</li>
				</g:if>
			</ol>

			<g:form url="[resource:linkInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${linkInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
