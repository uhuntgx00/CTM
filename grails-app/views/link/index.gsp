
<%@ page import="mil.ebs.ctm.Link" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'link.label', default: 'Link')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-link" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-link" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" />&nbsp;[${linkInstanceCount}]</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
                        <g:sortableColumn property="display" title="${message(code: 'link.display.label', default: 'Display')}" />
						<g:sortableColumn property="url" title="${message(code: 'link.url.label', default: 'Url')}" />
						<g:sortableColumn property="tooltip" title="${message(code: 'link.tooltip.label', default: 'Tooltip')}" />
						<g:sortableColumn property="linkType" title="${message(code: 'link.linkType.label', default: 'Link Type')}" />
						<g:sortableColumn property="linkColumn" title="${message(code: 'link.linkColumn.label', default: 'Column')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${linkInstanceList}" status="i" var="linkInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                        <td><g:link action="show" id="${linkInstance.id}">${fieldValue(bean: linkInstance, field: "display")}</g:link></td>
						<td>${fieldValue(bean: linkInstance, field: "url")}</td>
						<td>${fieldValue(bean: linkInstance, field: "tooltip")}</td>
						<td>${fieldValue(bean: linkInstance, field: "linkType")}</td>
						<td>${fieldValue(bean: linkInstance, field: "linkColumn")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${linkInstanceCount>20}">
                <div class="pagination">
                    <g:paginate total="${linkInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
