
<%@ page import="mil.ebs.ctm.ref.RefRankGrade" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refRankGrade.label', default: 'RefRankGrade')}" />
		<title>Rank/Grade List</title>
	</head>
	<body>
		<a href="#list-refRankGrade" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-refRankGrade" class="content scaffold-list" role="main">
			<h1>Rank/Grade List&nbsp;[${refRankGradeInstanceCount}]</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			    <thead>
					<tr>
						<g:sortableColumn property="code" title="${message(code: 'refRankGrade.code.label', default: 'Code')}" />
						<g:sortableColumn property="grade" title="${message(code: 'refRankGrade.grade.label', default: 'Grade')}" />
                        <g:sortableColumn property="grade" title="${message(code: 'refRankGrade.employeeType.label', default: 'Type')}" />
						<g:sortableColumn property="description" title="${message(code: 'refRankGrade.description.label', default: 'Description')}" />
						<g:sortableColumn property="officer" title="${message(code: 'refRankGrade.officer.label', default: 'Officer')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${refRankGradeInstanceList}" status="i" var="refRankGradeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${refRankGradeInstance.id}">${fieldValue(bean: refRankGradeInstance, field: "code")}</g:link></td>
						<td>${fieldValue(bean: refRankGradeInstance, field: "grade")}</td>
                        <td>${fieldValue(bean: refRankGradeInstance, field: "employeeType")}</td>
						<td>${fieldValue(bean: refRankGradeInstance, field: "description")}</td>
						<td><g:formatBoolean boolean="${refRankGradeInstance.officer}" /></td>
					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${refRankGradeInstanceCount > 20}">
                <div class="pagination">
                    <g:paginate total="${refRankGradeInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
