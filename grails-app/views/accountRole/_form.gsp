<%@ page import="mil.ebs.ctm.AccountRole" %>

<div class="fieldcontain ${hasErrors(bean: accountRoleInstance, field: 'role', 'error')} required">
	<label for="role">
		<g:message code="accountRole.role.label" default="Role" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="role" name="role.id" from="${mil.ebs.ctm.Role.list()}" optionKey="id" required="" value="${accountRoleInstance?.role?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: accountRoleInstance, field: 'account', 'error')} required">
	<label for="account">
		<g:message code="accountRole.account.label" default="Account" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="account" name="account.id" from="${mil.ebs.ctm.Account.list()}" optionKey="id" required="" value="${accountRoleInstance?.account?.id}" class="many-to-one"/>
</div>

