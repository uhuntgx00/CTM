<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.mail.MailTemplate" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'mailTemplate.label', default: 'MailTemplate')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-mailTemplate" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-mailTemplate" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list mailTemplate">
				<g:if test="${mailTemplateInstance?.templateName}">
				<li class="fieldcontain">
					<span id="templateName-label" class="property-label"><g:message code="mailTemplate.templateName.label" default="Template Name" /></span>
					<span class="property-value" aria-labelledby="templateName-label"><g:fieldValue bean="${mailTemplateInstance}" field="templateName"/></span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.subjectHeader}">
				<li class="fieldcontain">
					<span id="subjectHeader-label" class="property-label"><g:message code="mailTemplate.subjectHeader.label" default="Subject Header" /></span>
					<span class="property-value" aria-labelledby="subjectHeader-label"><g:fieldValue bean="${mailTemplateInstance}" field="subjectHeader"/></span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.templateContent}">
				<li class="fieldcontain">
					<span id="templateContent-label" class="property-label"><g:message code="mailTemplate.templateContent.label" default="Template Content" /></span>
					<span class="property-value" aria-labelledby="templateContent-label">
                        ${mailTemplateInstance?.templateContent}
                    </span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.canOverride}">
				<li class="fieldcontain">
					<span id="canOverride-label" class="property-label"><g:message code="mailTemplate.canOverride.label" default="Can Override" /></span>
					<span class="property-value" aria-labelledby="canOverride-label"><g:formatBoolean boolean="${mailTemplateInstance?.canOverride}" /></span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.forUser}">
				<li class="fieldcontain">
					<span id="forUser-label" class="property-label"><g:message code="mailTemplate.forUser.label" default="For User" /></span>
					<span class="property-value" aria-labelledby="forUser-label"><g:fieldValue bean="${mailTemplateInstance}" field="forUser"/></span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.forSupervisor}">
				<li class="fieldcontain">
					<span id="forSupervisor-label" class="property-label"><g:message code="mailTemplate.forSupervisor.label" default="For Supervisor" /></span>
					<span class="property-value" aria-labelledby="forSupervisor-label"><g:fieldValue bean="${mailTemplateInstance}" field="forSupervisor"/></span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.forTdAdmin}">
				<li class="fieldcontain">
					<span id="forTdAdmin-label" class="property-label"><g:message code="mailTemplate.forTdAdmin.label" default="For TD Admin" /></span>
					<span class="property-value" aria-labelledby="forTdAdmin-label"><g:fieldValue bean="${mailTemplateInstance}" field="forTdAdmin"/></span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.forTdFullAdmin}">
				<li class="fieldcontain">
					<span id="forTdFullAdmin-label" class="property-label"><g:message code="mailTemplate.forTdFullAdmin.label" default="For TD Full Admin" /></span>
					<span class="property-value" aria-labelledby="forTdFullAdmin-label"><g:fieldValue bean="${mailTemplateInstance}" field="forTdFullAdmin"/></span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.forTdPOC}">
				<li class="fieldcontain">
					<span id="forTdPOC-label" class="property-label"><g:message code="mailTemplate.forTdPOC.label" default="For TD POC" /></span>
					<span class="property-value" aria-labelledby="forTdPOC-label"><g:fieldValue bean="${mailTemplateInstance}" field="forTdPOC"/></span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.forFmcAdmin}">
				<li class="fieldcontain">
					<span id="forFmcAdmin-label" class="property-label"><g:message code="mailTemplate.forFmcAdmin.label" default="For FMC Admin" /></span>
					<span class="property-value" aria-labelledby="forFmcAdmin-label"><g:fieldValue bean="${mailTemplateInstance}" field="forFmcAdmin"/></span>
				</li>
				</g:if>
			
				<g:if test="${mailTemplateInstance?.forCao}">
				<li class="fieldcontain">
					<span id="forCao-label" class="property-label"><g:message code="mailTemplate.forCao.label" default="For CAO" /></span>
					<span class="property-value" aria-labelledby="forCao-label"><g:fieldValue bean="${mailTemplateInstance}" field="forCao"/></span>
				</li>
				</g:if>

                <br/><hr/><hr/><hr/>

                    ${emailTemplate}

                <br/><hr/><hr/><hr/>
			</ol>

            <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_FMC_ADMIN">
			    <g:form url="[resource:mailTemplateInstance, action:'delete']" method="DELETE">
                    <fieldset class="buttons">
                        <g:link class="edit" action="edit" resource="${mailTemplateInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        <sec:ifAnyGranted roles="ROLE_DEVELOPER">
                            <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                        </sec:ifAnyGranted>
                    </fieldset>
	    		</g:form>
            </sec:ifAnyGranted>
		</div>
	</body>
</html>
