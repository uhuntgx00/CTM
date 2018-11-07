
<%@ page import="mil.ebs.ctm.ref.RefRankGrade" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refRankGrade.label', default: 'RefRankGrade')}" />
		<title>Show Rank/Grade</title>
	</head>
	<body>
		<a href="#show-refRankGrade" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-refRankGrade" class="content scaffold-show" role="main">
			<h1>Show Rank/Grade</h1>
			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list refRankGrade">
				<g:if test="${refRankGradeInstance?.code}">
				<li class="fieldcontain">
					<span id="code-label" class="property-label"><g:message code="refRankGrade.code.label" default="Code" /></span>
					<span class="property-value" aria-labelledby="code-label"><g:fieldValue bean="${refRankGradeInstance}" field="code"/></span>
				</li>
				</g:if>
			
				<g:if test="${refRankGradeInstance?.grade}">
				<li class="fieldcontain">
					<span id="grade-label" class="property-label"><g:message code="refRankGrade.grade.label" default="Grade" /></span>
					<span class="property-value" aria-labelledby="grade-label"><g:fieldValue bean="${refRankGradeInstance}" field="grade"/></span>
				</li>
				</g:if>
			
				<g:if test="${refRankGradeInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="refRankGrade.description.label" default="Description" /></span>
					<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${refRankGradeInstance}" field="description"/></span>
				</li>
				</g:if>
			
				<g:if test="${refRankGradeInstance?.officer}">
				<li class="fieldcontain">
					<span id="officer-label" class="property-label"><g:message code="refRankGrade.officer.label" default="Officer" /></span>
					<span class="property-value" aria-labelledby="officer-label"><g:formatBoolean boolean="${refRankGradeInstance?.officer}" /></span>
				</li>
				</g:if>
			
				<g:if test="${refRankGradeInstance?.military}">
				<li class="fieldcontain">
					<span id="military-label" class="property-label"><g:message code="refRankGrade.military.label" default="Military" /></span>
					<span class="property-value" aria-labelledby="military-label"><g:formatBoolean boolean="${refRankGradeInstance?.military}" /></span>
				</li>
				</g:if>
			</ol>

			<g:form url="[resource:refRankGradeInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${refRankGradeInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <sec:ifAnyGranted roles="ROLE_DEVELOPER">
                        <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
