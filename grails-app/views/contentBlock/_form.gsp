<%@ page import="mil.ebs.ctm.mail.ContentBlock" %>



<div class="fieldcontain ${hasErrors(bean: contentBlockInstance, field: 'blockName', 'error')} required">
	<label for="blockName">
		<g:message code="contentBlock.blockName.label" default="Block Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="blockName" required="" value="${contentBlockInstance?.blockName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: contentBlockInstance, field: 'blockContent', 'error')} required">
	<label for="blockContent">
		<g:message code="contentBlock.blockContent.label" default="Block Content" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="blockContent" cols="40" rows="5" maxlength="4000" required="" value="${contentBlockInstance?.blockContent}"/>
</div>

