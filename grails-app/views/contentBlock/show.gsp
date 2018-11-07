<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.mail.ContentBlock" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'contentBlock.label', default: 'ContentBlock')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-contentBlock" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-contentBlock" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list contentBlock">
				<g:if test="${contentBlockInstance?.blockName}">
				<li class="fieldcontain">
					<span id="blockName-label" class="property-label"><g:message code="contentBlock.blockName.label" default="Block Name" /></span>
					<span class="property-value" aria-labelledby="blockName-label"><g:fieldValue bean="${contentBlockInstance}" field="blockName"/></span>
				</li>
				</g:if>
			
				<g:if test="${contentBlockInstance?.blockContent}">
				<li class="fieldcontain">
					<span id="blockContent-label" class="property-label"><g:message code="contentBlock.blockContent.label" default="Block Content" /></span>
					<span class="property-value" aria-labelledby="blockContent-label">
                        ${contentBlockInstance?.blockContent}
                    </span>
				</li>
				</g:if>
			</ol>

            <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_FMC_ADMIN">
			    <g:form url="[resource:contentBlockInstance, action:'delete']" method="DELETE">
                    <fieldset class="buttons">
                        <g:link class="edit" action="edit" resource="${contentBlockInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        <sec:ifAnyGranted roles="ROLE_DEVELOPER">
                            <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                        </sec:ifAnyGranted>
                    </fieldset>
    			</g:form>
            </sec:ifAnyGranted>
		</div>
	</body>
</html>
