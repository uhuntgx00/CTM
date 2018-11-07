<%@ page import="org.springframework.validation.FieldError" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refRankGrade.label', default: 'RefRankGrade')}" />
		<title>Create Rank/Grade</title>
	</head>
	<body>
		<a href="#create-refRankGrade" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-refRankGrade" class="content scaffold-create" role="main">
            <div class="developer_only">
                *** Developer ONLY Functionality ***
            </div>
			<h1>Create Rank/Grade</h1>

            <g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:hasErrors bean="${refRankGradeInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${refRankGradeInstance}" var="error">
                    <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

			<g:form url="[resource:refRankGradeInstance, action:'save']" >
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
