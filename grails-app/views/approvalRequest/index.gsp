
<%@ page import="mil.ebs.ctm.remove.ApprovalRequest" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'approvalRequest.label', default: 'ApprovalRequest')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-approvalRequest" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
			</ul>
		</div>
		<div id="list-approvalRequest" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
			<thead>
					<tr>
						<th><g:message code="approvalRequest.conference.label" default="Conference" /></th>
						<g:sortableColumn property="numAttendees" title="${message(code: 'approvalRequest.numAttendees.label', default: '# Attendees')}" />
						<g:sortableColumn property="status" title="${message(code: 'approvalRequest.status.label', default: 'Status')}" />
                        <th><g:message code="approvalRequest.estimateCost.label" default="Estimate Cost" /></th>
                        <g:sortableColumn property="approveByDate" title="${message(code: 'approvalRequest.approveByDate.label', default: 'Approve By Date')}" />
                        <g:sortableColumn property="approvedBy" title="${message(code: 'approvalRequest.approvedBy.label', default: 'Approved By')}" />
                        <g:sortableColumn property="approvalDate" title="${message(code: 'approvalRequest.approvalDate.label', default: 'Approval Date')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${approvalRequestInstanceList}" status="i" var="approvalRequestInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${approvalRequestInstance.id}">${fieldValue(bean: approvalRequestInstance, field: "conference")}</g:link></td>
						<td>
                            ${fieldValue(bean: approvalRequestInstance, field: "numAttendees")}
                            &nbsp;<span style="color:#777777">(${approvalRequestInstance?.conference?.attendees?.size()})</span>
                        </td>
						<td>${fieldValue(bean: approvalRequestInstance, field: "status")}</td>
                        <td>$${approvalRequestInstance?.conference?.estimateTotal()}</td>
						<td>
                            <g:if test="${approvalRequestInstance?.status?.equalsIgnoreCase("Pending")}">
                                <g:if test="${approvalRequestInstance?.approveByDate < new Date()}">
                                    <span style="color:#FF0000"><b><g:formatDate date="${approvalRequestInstance?.approveByDate}" type="date" dateStyle="FULL"/></b></span>
                                </g:if>
                                <g:else>
                                    <g:formatDate date="${approvalRequestInstance?.approveByDate}" type="date" dateStyle="FULL"/>
                                </g:else>
                            </g:if>
                        </td>
                        <td>${approvalRequestInstance?.approvedBy}</td>
                        <td><g:formatDate date="${approvalRequestInstance?.approvalDate}" type="date" dateStyle="FULL"/></td>
					</tr>
				</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${approvalRequestInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
