
<%@ page import="mil.ebs.ctm.Account; mil.ebs.ctm.AccountActivity" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'accountActivity.label', default: 'AccountActivity')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-accountActivity" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-accountActivity" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
    			<thead>
					<tr>
                        <th>&nbsp;</th>
						<th>Account</th>
						<th>URL</th>
						<th>Activity Date</th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${accountActivityInstanceList}" status="i" var="accountActivityInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                        <td><g:link controller="account" action="show" id="${Account.findByUsername(accountActivityInstance.account).id}"><g:img dir="images/icons" file="search-user_512.png" height="24" width="24" alt="Show Account Information" title="Show Account Information"/></g:link></td>
						<td><g:link action="last20" params="[account: "${accountActivityInstance.account}"]">${fieldValue(bean: accountActivityInstance, field: "account")}</g:link></td>
						<td>${fieldValue(bean: accountActivityInstance, field: "url")}</td>
						<td><g:formatDate date="${accountActivityInstance.activityDate}" type="datetime" style="FULL" timestyle="SHORT"/></td>
					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${accountActivityInstanceCount > 25}">
                <div class="pagination">
                    <g:paginate total="${accountActivityInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
