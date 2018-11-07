<%@ page import="mil.ebs.ctm.ref.RefDateGate" %>

<div class="fieldcontain ${hasErrors(bean: refDateGateInstance, field: 'code', 'error')} required">
	<label for="code">
		<g:message code="refDateGate.code.label" default="Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="code" size="10" required="" value="${refDateGateInstance?.code}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refDateGateInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="refDateGate.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" size="75" required="" value="${refDateGateInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refDateGateInstance, field: 'name', 'error')} required">
	<label for="dateGateType">
		<g:message code="refDateGate.dateGateType.label" default="Date Gate Type" />
		<span class="required-indicator">*</span>
	</label>
    <g:select name="dateGateType" from="${refDateGateInstance.constraints.dateGateType.inList}" value="${refDateGateInstance?.dateGateType}" valueMessagePrefix="refDateGateInstance.dateGateType"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refDateGateInstance, field: 'canDelete', 'error')} ">
	<label for="canDelete">
		<g:message code="refDateGate.canDelete.label" default="Can Delete" />
	</label>
	<g:checkBox name="canDelete" value="${refDateGateInstance?.canDelete}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refDateGateInstance, field: 'onDisplay', 'error')} ">
	<label for="onDisplay">
		<g:message code="refDateGate.onDisplay.label" default="On Display" />
	</label>
	<g:checkBox name="onDisplay" value="${refDateGateInstance?.onDisplay}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refDateGateInstance, field: 'requireComment', 'error')} ">
	<label for="requireComment">
		<g:message code="refDateGate.requireComment.label" default="Require Comment" />
	</label>
	<g:checkBox name="requireComment" value="${refDateGateInstance?.requireComment}" />
</div>

