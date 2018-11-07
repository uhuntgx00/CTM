<%@ page import="org.springframework.validation.FieldError" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'attendee.label', default: 'Attendee')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
        <g:render template="tinyMCE"/>
	</head>
	<body>
		<a href="#create-attendee" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-attendee" class="content scaffold-create" role="main">
			<h1>
                Create Attendee (<b>${attendeeInstance?.status}</b>)
                <br/>Conference: <b>${attendeeInstance?.conference}</b>
                <br/>&nbsp;
            </h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${attendeeInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${attendeeInstance}" var="error">
			    	<li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>

			<g:form url="[resource:attendeeInstance, action:'conferenceSave']" >
                <g:hiddenField name="conference.id" value="${attendeeInstance?.conference?.id}"/>
                <g:hiddenField name="status" value="${attendeeInstance?.status}"/>
                <g:hiddenField name="refId" value="${params.get("refId")}"/>
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>

        <script type="text/javascript">
            $(document).ready(function(){
                updateDatePicker();

                $("#spinner").ajaxComplete (function(event, request, settings){
                    updateDatePicker();
                });
            })
        </script>

	</body>
</html>
