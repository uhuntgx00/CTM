<%@ page import="mil.ebs.ctm.Organization" %>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="organization.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" size="50" required="" value="${organizationInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'officeSymbol', 'error')} required">
	<label for="officeSymbol">
		<g:message code="organization.officeSymbol.label" default="Office Symbol" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="officeSymbol" size="10" required="" value="${organizationInstance?.officeSymbol}"/>
</div>

