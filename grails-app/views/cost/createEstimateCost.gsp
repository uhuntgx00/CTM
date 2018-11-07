<%@ page import="org.springframework.validation.FieldError" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'cost.label', default: 'Cost')}" />
		<title>Create ESTIMATE Cost</title>
	</head>
	<body>
		<a href="#create-cost" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-cost" class="content scaffold-create" role="main">
			<h1>
                Create&nbsp;<b>ESTIMATE</b>&nbsp;Cost&nbsp;-&nbsp;<i>${costInstance?.attendee?.encodeAsHTML()}</i>
                <br/>Conference:&nbsp;<b>${costInstance?.attendee?.conference}</b>&nbsp;<i>-&nbsp;${costInstance?.attendee?.conference?.days()} day(s)</i>
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

			<g:form url="[resource:costInstance, action:'saveEstimateCost']" >
                <g:hiddenField name="costType" value="${costInstance?.costType}"/>
                <g:hiddenField name="attendee.id" value="${costInstance?.attendee?.id}"/>
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>

        <g:render template="fundingSourceScripts"/>

	</body>
</html>
