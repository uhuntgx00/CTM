<%@ page import="org.springframework.validation.FieldError" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'organization.label', default: 'TechnicalDirective')}" />
		<title>Add Organizational Section</title>
	</head>
	<body>
		<a href="#create-technicalDirective" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-technicalDirective" class="content scaffold-create" role="main">
			<h1>Add TD Section <b>[${organizationInstance?.parent?.officeSymbol}]</b></h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:hasErrors bean="${organizationInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${organizationInstance}" var="error">
                        <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

            <g:form url="[resource:organizationInstance, action:'save']" >
                <g:hiddenField name="parentID" value="${organizationInstance?.parent?.id}"/>
                <g:hiddenField name="trueTD" value="${organizationInstance?.trueTD}"/>
                <g:hiddenField name="levelTD" value="${organizationInstance?.levelTD}"/>
                <g:hiddenField name="parentSymbol" value="${organizationInstance?.parent?.officeSymbol}"/>
				<fieldset class="form">
					<g:render template="formChild"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
