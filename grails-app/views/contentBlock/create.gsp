<%@ page import="org.springframework.validation.FieldError" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'contentBlock.label', default: 'Content Block')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
        <g:render template="tinyMCE"/>
	</head>
	<body>
		<a href="#create-contentBlock" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-contentBlock" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:hasErrors bean="${contentBlockInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${contentBlockInstance}" var="error">
                        <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

            <g:form url="[resource:contentBlockInstance, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
                <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_FMC_ADMIN">
                    <fieldset class="buttons">
                        <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                    </fieldset>
                </sec:ifAnyGranted>
			</g:form>
		</div>

        <script type="text/javascript">
            $(document).ready(function(){
                $( document ).tooltip();
            })
        </script>

	</body>
</html>
