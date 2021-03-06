<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.Cost" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'cost.label', default: 'Cost')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#edit-cost" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-cost" class="content scaffold-edit" role="main">
            <h1>
                Edit <b>${costInstance?.costType}</b> Cost (<b>${costInstance?.attendee?.attendanceType}</b>) - <i>${costInstance?.attendee?.encodeAsHTML()}</i>
                <br/>Conference: <b>${costInstance?.attendee?.conference}</b>
                <br/>&nbsp;
            </h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:hasErrors bean="${costInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${costInstance}" var="error">
                        <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

            <g:form url="[resource:costInstance, action:'conferenceUpdate']" method="PUT" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="conferenceUpdate" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
                        <g:actionSubmit class="delete" action="conferenceDelete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
		</div>

        <g:render template="fundingSourceScripts"/>

	</body>
</html>
