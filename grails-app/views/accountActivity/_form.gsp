<%@ page import="mil.ebs.ctm.AccountActivity" %>



<div class="fieldcontain ${hasErrors(bean: accountActivityInstance, field: 'account', 'error')} required">
	<label for="account">
		<g:message code="accountActivity.account.label" default="Account" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="account" cols="40" rows="5" maxlength="500" required="" value="${accountActivityInstance?.account}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: accountActivityInstance, field: 'url', 'error')} ">
	<label for="url">
		<g:message code="accountActivity.url.label" default="Url" />
		
	</label>
	<g:textArea name="url" cols="40" rows="5" maxlength="1000" value="${accountActivityInstance?.url}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: accountActivityInstance, field: 'activityDate', 'error')} required">
	<label for="activityDate">
		<g:message code="accountActivity.activityDate.label" default="Activity Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="activityDate" precision="day"  value="${accountActivityInstance?.activityDate}"  />
</div>

