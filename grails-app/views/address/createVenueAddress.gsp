<%@ page import="org.springframework.validation.FieldError" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'address.label', default: 'Address')}" />
		<title>Create Venue Address</title>
	</head>
	<body>
		<a href="#create-address" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-address" class="content scaffold-create" role="main">
			<h1>Create Venue Address</h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${addressInstance}">
            <ul class="errors" role="alert">
                <g:eachError bean="${addressInstance}" var="error">
                    <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
			</g:hasErrors>

			<g:form url="[resource:addressInstance, action:'saveVenueAddress']" >
                <g:hiddenField name="addressType" value="Venue"/>
                <g:hiddenField name="conference.id" value="${addressInstance?.conference?.id}"/>
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>

        <g:render template="bodyScripts"/>

	</body>
</html>
