<%@ page import="org.springframework.validation.FieldError" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refAttendeeState.label', default: 'RefAttendeeState')}" />
		<title>Create Attendee State</title>
	</head>
	<body>
		<a href="#create-refAttendeeState" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="developer_only">
            *** Developer ONLY Functionality ***
        </div>
		<div id="create-refAttendeeState" class="content scaffold-create" role="main">
			<h1>Create Attendee State</h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${refAttendeeStateInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${refAttendeeStateInstance}" var="error">
                    <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

			<g:form url="[resource:refAttendeeStateInstance, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
                <div class="developer_only">
                    *** Developer ONLY Functionality ***
                </div>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
