
<%@ page import="mil.ebs.ctm.ref.RefDateGate" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refDateGate.label', default: 'RefDateGate')}" />
		<title>Show Date Gate</title>
	</head>
	<body>
		<a href="#show-refDateGate" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-refDateGate" class="content scaffold-show" role="main">
			<h1>Show Date Gate</h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list refDateGate">
				<g:if test="${refDateGateInstance?.code}">
				<li class="fieldcontain">
					<span id="code-label" class="property-label"><g:message code="refDateGate.code.label" default="Code" /></span>
					<span class="property-value" aria-labelledby="code-label"><g:fieldValue bean="${refDateGateInstance}" field="code"/></span>
				</li>
				</g:if>
			
				<g:if test="${refDateGateInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="refDateGate.name.label" default="Name" /></span>
					<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${refDateGateInstance}" field="name"/></span>
				</li>
				</g:if>
			
				<g:if test="${refDateGateInstance?.canDelete}">
				<li class="fieldcontain">
					<span id="canDelete-label" class="property-label"><g:message code="refDateGate.canDelete.label" default="Can Delete" /></span>
					<span class="property-value" aria-labelledby="canDelete-label"><g:formatBoolean boolean="${refDateGateInstance?.canDelete}" /></span>
				</li>
				</g:if>
			
				<g:if test="${refDateGateInstance?.onDisplay}">
				<li class="fieldcontain">
					<span id="onDisplay-label" class="property-label"><g:message code="refDateGate.onDisplay.label" default="On Display" /></span>
					<span class="property-value" aria-labelledby="onDisplay-label"><g:formatBoolean boolean="${refDateGateInstance?.onDisplay}" /></span>
				</li>
				</g:if>
			</ol>

			<g:form url="[resource:refDateGateInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${refDateGateInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <sec:ifAnyGranted roles="ROLE_DEVELOPER">
                        <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
