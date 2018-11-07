<%@ page import="mil.ebs.ctm.mail.MailTemplate" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'mailTemplate.label', default: 'MailTemplate')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-mailTemplate" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-mailTemplate" class="content scaffold-list" role="main">
			<h1>Mail Template List [${mailTemplateInstanceCount}]</h1>

            <g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
			    <thead>
					<tr>
						<g:sortableColumn property="templateName" title="${message(code: 'mailTemplate.templateName.label', default: 'Template Name')}" />
						<g:sortableColumn property="subjectHeader" title="${message(code: 'mailTemplate.subjectHeader.label', default: 'Subject Header')}" />
						<g:sortableColumn property="templateContent" title="${message(code: 'mailTemplate.templateContent.label', default: 'Template Content')}" />
						<g:sortableColumn property="canOverride" title="${message(code: 'mailTemplate.canOverride.label', default: 'Can Override')}" />
						<g:sortableColumn property="forUser" title="${message(code: 'mailTemplate.forUserList.label', default: 'User')}" />
						<g:sortableColumn property="forSupervisor" title="${message(code: 'mailTemplate.forSupervisorList.label', default: 'Super')}" />
                        <g:sortableColumn property="forTdAdmin" title="${message(code: 'mailTemplate.forTdAdminList.label', default: 'TD')}" />
                        <g:sortableColumn property="forTdFullAdmin" title="${message(code: 'mailTemplate.forTdFullAdminList.label', default: 'Full')}" />
                        <g:sortableColumn property="forTdPOC" title="${message(code: 'mailTemplate.forTdPocList.label', default: 'POC')}" />
                        <g:sortableColumn property="forFmcAdmin" title="${message(code: 'mailTemplate.forFmcAdminList.label', default: 'FMC')}" />
                        <g:sortableColumn property="forCao" title="${message(code: 'mailTemplate.forCaoList.label', default: 'CAO')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${mailTemplateInstanceList}" status="i" var="mailTemplateInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${mailTemplateInstance.id}">${fieldValue(bean: mailTemplateInstance, field: "templateName")}</g:link></td>
						<td>${fieldValue(bean: mailTemplateInstance, field: "subjectHeader")}</td>
						<td>${fieldValue(bean: mailTemplateInstance, field: "templateContent")}</td>
						<td><g:formatBoolean boolean="${mailTemplateInstance.canOverride}" /></td>
						<td>${fieldValue(bean: mailTemplateInstance, field: "forUser")}</td>
						<td>${fieldValue(bean: mailTemplateInstance, field: "forSupervisor")}</td>
                        <td>${fieldValue(bean: mailTemplateInstance, field: "forTdAdmin")}</td>
                        <td>${fieldValue(bean: mailTemplateInstance, field: "forTdFullAdmin")}</td>
                        <td>${fieldValue(bean: mailTemplateInstance, field: "forTdPOC")}</td>
                        <td>${fieldValue(bean: mailTemplateInstance, field: "forFmcAdmin")}</td>
                        <td>${fieldValue(bean: mailTemplateInstance, field: "forCao")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${mailTemplateInstanceCount > 200}">
                <div class="pagination">
                    <g:paginate total="${mailTemplateInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
