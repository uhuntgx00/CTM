<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.news.NewsItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'newsItem.label', default: 'NewsItem')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-newsItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-newsItem" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list newsItem">
				<g:if test="${newsItemInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="newsItem.title.label" default="Title" /></span>
    				<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${newsItemInstance}" field="title"/></span>
				</li>
				</g:if>
			
				<g:if test="${newsItemInstance?.body}">
				<li class="fieldcontain">
					<span id="body-label" class="property-label"><g:message code="newsItem.body.label" default="Body" /></span>
					<span class="property-value" aria-labelledby="body-label">${newsItemInstance?.body}</span>
				</li>
				</g:if>
			
				<g:if test="${newsItemInstance?.locked}">
				<li class="fieldcontain">
					<span id="locked-label" class="property-label"><g:message code="newsItem.locked.label" default="Locked" /></span>
					<span class="property-value" aria-labelledby="locked-label"><g:formatBoolean boolean="${newsItemInstance?.locked}" /></span>
				</li>
				</g:if>
			
				<g:if test="${newsItemInstance?.author}">
				<li class="fieldcontain">
					<span id="author-label" class="property-label"><g:message code="newsItem.author.label" default="Author" /></span>
					<span class="property-value" aria-labelledby="author-label"><g:link controller="account" action="show" id="${newsItemInstance?.author?.id}">${newsItemInstance?.author?.encodeAsHTML()}</g:link></span>
				</li>
				</g:if>
			
				<g:if test="${newsItemInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="newsItem.status.label" default="Status" /></span>
					<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${newsItemInstance}" field="status"/></span>
				</li>
				</g:if>
			
				<g:if test="${newsItemInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="newsItem.dateCreated.label" default="Date Created" /></span>
					<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${newsItemInstance?.dateCreated}" /></span>
				</li>
				</g:if>
			
				<g:if test="${newsItemInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="newsItem.lastUpdated.label" default="Last Updated" /></span>
					<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${newsItemInstance?.lastUpdated}" /></span>
				</li>
				</g:if>
			
				<g:if test="${newsItemInstance?.versions}">
				<li class="fieldcontain">
					<span id="versions-label" class="property-label"><g:message code="newsItem.versions.label" default="Versions" /></span>
					<g:each in="${newsItemInstance.versions}" var="v">
						<span class="property-value" aria-labelledby="versions-label"><g:link controller="version" action="show" id="${v.id}">${v?.encodeAsHTML()}</g:link></span>
					</g:each>
				</li>
				</g:if>
			</ol>

			<g:form url="[resource:newsItemInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${newsItemInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
