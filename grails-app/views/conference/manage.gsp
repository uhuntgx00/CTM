<%@page defaultCodec="none" %>
<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
        <g:javascript library="listbox_reorder"/>
		<title><g:message code="default.manage.label" args="[entityName]" /> Attendees</title>
	</head>
	<body>
		<a href="#edit-conference" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div id="edit-conference" class="content scaffold-edit" role="main">
			<h1><g:message code="default.manage.label" args="[entityName]" /> Attendees</h1>

			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${conferenceInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${conferenceInstance}" var="error">
                        <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

			<g:form url="[resource:conferenceInstance, action:'manageUpdate']" method="PUT" onsubmit="return selectAll('attendees');">
				<fieldset class="form">
					<g:render template="manage"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="manageUpdate" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>

        <script type="text/javascript">
            $(document).ready(function(){
                $( document ).tooltip();
            })
        </script>

	</body>
</html>
