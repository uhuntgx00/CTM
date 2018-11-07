<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.Account" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#edit-account" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-account" class="content scaffold-edit" role="main">
			<h1>
                <g:img dir="/images/icons" file="edit-user_512.png" width="48" height="48" alt="Account" title="Account"/>
                <g:message code="default.edit.label" args="[entityName]" />
            </h1>

            <g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:hasErrors bean="${accountInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${accountInstance}" var="error">
                        <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

			<g:form url="[resource:accountInstance, action:'update']" method="PUT" >
				<fieldset class="form">
					<g:render template="form"/>
                    <g:render template="roles"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>

        <g:render template="bodyScripts"/>

	</body>
</html>
