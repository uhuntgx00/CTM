<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.mail.ContentBlock" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'contentBlock.label', default: 'Content Block')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:render template="tinyMCE"/>
	</head>
	<body>
		<a href="#edit-contentBlock" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-contentBlock" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
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

			<g:form url="[resource:contentBlockInstance, action:'update']" method="PUT" >
				<g:hiddenField name="version" value="${contentBlockInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
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
