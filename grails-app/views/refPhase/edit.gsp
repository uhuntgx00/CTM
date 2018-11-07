<%@ page import="mil.ebs.ctm.ref.RefPhase" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refPhase.label', default: 'RefPhase')}" />
		<title>Edit Phase ${refPhaseInstance?.phase}</title>
	</head>
	<body>
		<a href="#edit-refPhase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-refPhase" class="content scaffold-edit" role="main">
			<h1>Edit Phase <b>${refPhaseInstance?.phase}</b></h1>
			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${refPhaseInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${refPhaseInstance}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

			<g:form url="[resource:refPhaseInstance, action:'update']" method="PUT" >
				<g:hiddenField name="version" value="${refPhaseInstance?.version}" />
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
