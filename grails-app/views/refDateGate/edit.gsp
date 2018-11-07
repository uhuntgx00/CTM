<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.ref.RefDateGate" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refDateGate.label', default: 'RefDateGate')}" />
		<title>Edit Date Gate</title>
	</head>
	<body>
		<a href="#edit-refDateGate" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-refDateGate" class="content scaffold-edit" role="main">
			<h1>Edit Date Gate</h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:hasErrors bean="${refDateGateInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${refDateGateInstance}" var="error">
                    <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

			<g:form url="[resource:refDateGateInstance, action:'update']" method="PUT" >
				<g:hiddenField name="version" value="${refDateGateInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
