<%@ page import="org.springframework.validation.FieldError" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refPhaseState.label', default: 'RefPhaseState')}" />
		<title>Create Phase State</title>
	</head>
	<body>
		<a href="#create-refPhaseState" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="developer_only">
            *** Developer ONLY Functionality ***
        </div>
		<div id="create-refPhaseState" class="content scaffold-create" role="main">
			<h1>Create Phase State</h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:hasErrors bean="${refPhaseStateInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${refPhaseStateInstance}" var="error">
                    <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

            <g:form url="[resource:refPhaseStateInstance, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>

                <div class="developer_only">
                    *** Developer ONLY Functionality ***
                </div>

				<fieldset class="buttons">
                    <sec:ifAnyGranted roles="ROLE_DEVELOPER">
                        <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
