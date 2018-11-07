
<%@ page import="mil.ebs.ctm.Cost" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'cost.label', default: 'Cost')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-cost" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-cost" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="costType" title="${message(code: 'cost.costType.label', default: 'Cost Type')}" />
					
						<g:sortableColumn property="registrationCost" title="${message(code: 'cost.registrationCost.label', default: 'Registration Cost')}" />
					
						<g:sortableColumn property="airfareCost" title="${message(code: 'cost.airfareCost.label', default: 'Airfare Cost')}" />
					
						<g:sortableColumn property="airfareProvider" title="${message(code: 'cost.airfareProvider.label', default: 'Airfare Provider')}" />
					
						<g:sortableColumn property="localTravelCost" title="${message(code: 'cost.localTravelCost.label', default: 'Local Travel Cost')}" />
					
						<g:sortableColumn property="localTravelProvider" title="${message(code: 'cost.localTravelProvider.label', default: 'Local Travel Provider')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${costInstanceList}" status="i" var="costInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${costInstance.id}">${fieldValue(bean: costInstance, field: "costType")}</g:link></td>
					
						<td>${fieldValue(bean: costInstance, field: "registrationCost")}</td>
					
						<td>${fieldValue(bean: costInstance, field: "airfareCost")}</td>
					
						<td>${fieldValue(bean: costInstance, field: "airfareProvider")}</td>
					
						<td>${fieldValue(bean: costInstance, field: "localTravelCost")}</td>
					
						<td>${fieldValue(bean: costInstance, field: "localTravelProvider")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${costInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
