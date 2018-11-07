
<%@ page import="mil.ebs.ctm.mail.ContentBlock" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'contentBlock.label', default: 'ContentBlock')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-contentBlock" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-contentBlock" class="content scaffold-list" role="main">
			<h1>Content Block List [${contentBlockInstanceCount}]</h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
    			<thead>
					<tr>
						<g:sortableColumn property="blockName" title="${message(code: 'contentBlock.blockName.label', default: 'Block Name')}" />
						<g:sortableColumn property="blockContent" title="${message(code: 'contentBlock.blockContent.label', default: 'Block Content')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${contentBlockInstanceList}" status="i" var="contentBlockInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${contentBlockInstance.id}">${fieldValue(bean: contentBlockInstance, field: "blockName")}</g:link></td>
						<td>${fieldValue(bean: contentBlockInstance, field: "blockContent")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${contentBlockInstanceCount > 200}">
                <div class="pagination">
                    <g:paginate total="${contentBlockInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
