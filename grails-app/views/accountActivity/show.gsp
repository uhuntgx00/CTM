
<%@ page import="mil.ebs.ctm.AccountActivity" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'accountActivity.label', default: 'AccountActivity')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-accountActivity" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-accountActivity" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list accountActivity">
			
				<g:if test="${accountActivityInstance?.account}">
				<li class="fieldcontain">
					<span id="account-label" class="property-label"><g:message code="accountActivity.account.label" default="Account" /></span>
					
						<span class="property-value" aria-labelledby="account-label"><g:fieldValue bean="${accountActivityInstance}" field="account"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${accountActivityInstance?.url}">
				<li class="fieldcontain">
					<span id="url-label" class="property-label"><g:message code="accountActivity.url.label" default="Url" /></span>
					
						<span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${accountActivityInstance}" field="url"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${accountActivityInstance?.activityDate}">
				<li class="fieldcontain">
					<span id="activityDate-label" class="property-label"><g:message code="accountActivity.activityDate.label" default="Activity Date" /></span>
					
						<span class="property-value" aria-labelledby="activityDate-label"><g:formatDate date="${accountActivityInstance?.activityDate}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:accountActivityInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${accountActivityInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
