<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.ref.RefRankGrade" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refRankGrade.label', default: 'RefRankGrade')}" />
		<title>Edit Rank/Grade</title>
	</head>
	<body>
		<a href="#edit-refRankGrade" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-refRankGrade" class="content scaffold-edit" role="main">
			<h1>Edit Rank/Grade</h1>
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

            <g:form url="[resource:refRankGradeInstance, action:'update']" method="PUT" >
				<g:hiddenField name="version" value="${refRankGradeInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
