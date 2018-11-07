<%@ page import="mil.ebs.ctm.mail.MailTemplate" %>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'templateName', 'error')} required">
	<label for="templateName">
		<g:message code="mailTemplate.templateName.label" default="Template Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="templateName" required="" value="${mailTemplateInstance?.templateName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'subjectHeader', 'error')} required">
	<label for="subjectHeader">
		<g:message code="mailTemplate.subjectHeader.label" default="Subject Header" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="subjectHeader" size="75" required="" value="${mailTemplateInstance?.subjectHeader}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'templateContent', 'error')} required">
	<label for="templateContent">
		<g:message code="mailTemplate.templateContent.label" default="Template Content" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="templateContent" cols="40" rows="5" maxlength="4000" required="" value="${mailTemplateInstance?.templateContent}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'canOverride', 'error')} ">
	<label for="canOverride">
		<g:message code="mailTemplate.canOverride.label" default="Can Override" />
	</label>
	<g:checkBox name="canOverride" value="${mailTemplateInstance?.canOverride}" />
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'forUser', 'error')} ">
	<label for="forUser">
		<g:message code="mailTemplate.forUser.label" default="For User" />
	</label>
	<g:select name="forUser" from="${mailTemplateInstance.constraints.forUser.inList}" value="${mailTemplateInstance?.forUser}" valueMessagePrefix="mailTemplate.forUser" />
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'forSupervisor', 'error')} ">
	<label for="forSupervisor">
		<g:message code="mailTemplate.forSupervisor.label" default="For Supervisor" />
	</label>
	<g:select name="forSupervisor" from="${mailTemplateInstance.constraints.forSupervisor.inList}" value="${mailTemplateInstance?.forSupervisor}" valueMessagePrefix="mailTemplate.forSupervisor" />
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'forTdAdmin', 'error')} ">
	<label for="forTdAdmin">
		<g:message code="mailTemplate.forTdAdmin.label" default="For TD Admin" />
	</label>
	<g:select name="forTdAdmin" from="${mailTemplateInstance.constraints.forTdAdmin.inList}" value="${mailTemplateInstance?.forTdAdmin}" valueMessagePrefix="mailTemplate.forTdAdmin" />
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'forTdFullAdmin', 'error')} ">
	<label for="forTdFullAdmin">
		<g:message code="mailTemplate.forTdFullAdmin.label" default="For TD (Full) Admin" />
	</label>
	<g:select name="forTdFullAdmin" from="${mailTemplateInstance.constraints.forTdFullAdmin.inList}" value="${mailTemplateInstance?.forTdFullAdmin}" valueMessagePrefix="mailTemplate.forTdFullAdmin" />
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'forTdPOC', 'error')} ">
	<label for="forTdPOC">
		<g:message code="mailTemplate.forTdPOC.label" default="For TD POC" />
	</label>
	<g:select name="forTdPOC" from="${mailTemplateInstance.constraints.forTdPOC.inList}" value="${mailTemplateInstance?.forTdPOC}" valueMessagePrefix="mailTemplate.forTdPOC" />
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'forFmcAdmin', 'error')} ">
	<label for="forFmcAdmin">
		<g:message code="mailTemplate.forFmcAdmin.label" default="For FMC Admin" />
	</label>
	<g:select name="forFmcAdmin" from="${mailTemplateInstance.constraints.forFmcAdmin.inList}" value="${mailTemplateInstance?.forFmcAdmin}" valueMessagePrefix="mailTemplate.forFmcAdmin" />
</div>

<div class="fieldcontain ${hasErrors(bean: mailTemplateInstance, field: 'forCao', 'error')} ">
	<label for="forCao">
		<g:message code="mailTemplate.forCao.label" default="For CAO" />
	</label>
	<g:select name="forCao" from="${mailTemplateInstance.constraints.forCao.inList}" value="${mailTemplateInstance?.forCao}" valueMessagePrefix="mailTemplate.forCao" />
</div>

