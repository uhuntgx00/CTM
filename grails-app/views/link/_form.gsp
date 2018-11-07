<%@ page import="mil.ebs.ctm.Link" %>

<div class="fieldcontain ${hasErrors(bean: linkInstance, field: 'display', 'error')} required">
	<label for="display">
		<g:message code="link.display.label" default="Display" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="display" size="50" required="" value="${linkInstance?.display}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: linkInstance, field: 'url', 'error')} ">
	<label for="url">
		<g:message code="link.url.label" default="URL" />
	</label>
	<g:textField name="url" size="50" value="${linkInstance?.url}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: linkInstance, field: 'tooltip', 'error')} required">
	<label for="tooltip">
		<g:message code="link.tooltip.label" default="Tooltip" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="tooltip" size="50" required="" value="${linkInstance?.tooltip}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: linkInstance, field: 'linkType', 'error')} ">
	<label for="linkType">
		<g:message code="link.linkType.label" default="Link Type" />
	</label>
	<g:select name="linkType" from="${linkInstance.constraints.linkType.inList}" value="${linkInstance?.linkType}" valueMessagePrefix="link.linkType"/>
</div>

<div class="fieldcontain ${hasErrors(bean: linkInstance, field: 'linkColumn', 'error')} ">
	<label for="linkColumn">
		<g:message code="link.linkColumn.label" default="Column" />
	</label>
	<g:select name="linkColumn" from="${linkInstance.constraints.linkColumn.inList}" value="${linkInstance?.linkColumn}" valueMessagePrefix="link.linkColumn"/>
</div>

