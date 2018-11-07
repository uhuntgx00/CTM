<%@ page import="mil.ebs.ctm.remove.ApprovalRequest" %>



<div class="fieldcontain ${hasErrors(bean: approvalRequestInstance, field: 'conference', 'error')} required">
	<label for="conference">
		<g:message code="approvalRequest.conference.label" default="Conference" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="conference" name="conference.id" from="${mil.ebs.ctm.Conference.list()}" optionKey="id" required="" value="${approvalRequestInstance?.conference?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: approvalRequestInstance, field: 'numAttendees', 'error')} ">
	<label for="numAttendees">
		<g:message code="approvalRequest.numAttendees.label" default="Num Attendees" />
		
	</label>
	<g:field name="numAttendees" type="number" value="${approvalRequestInstance.numAttendees}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: approvalRequestInstance, field: 'status', 'error')} ">
	<label for="status">
		<g:message code="approvalRequest.status.label" default="Status" />
		
	</label>
	<g:select name="status" from="${approvalRequestInstance.constraints.status.inList}" value="${approvalRequestInstance?.status}" valueMessagePrefix="approvalRequest.status" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: approvalRequestInstance, field: 'approveByDate', 'error')} ">
	<label for="approveByDate">
		<g:message code="approvalRequest.approveByDate.label" default="Approve By Date" />
		
	</label>
	<g:datePicker name="approveByDate" precision="day"  value="${approvalRequestInstance?.approveByDate}" default="none" noSelection="['': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: approvalRequestInstance, field: 'approvedBy', 'error')} ">
	<label for="approvedBy">
		<g:message code="approvalRequest.approvedBy.label" default="Approved By" />
		
	</label>
	<g:select id="approvedBy" name="approvedBy.id" from="${mil.ebs.ctm.Account.list()}" optionKey="id" value="${approvalRequestInstance?.approvedBy?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: approvalRequestInstance, field: 'approvalDate', 'error')} ">
	<label for="approvalDate">
		<g:message code="approvalRequest.approvalDate.label" default="Approval Date" />
		
	</label>
	<g:datePicker name="approvalDate" precision="day"  value="${approvalRequestInstance?.approvalDate}" default="none" noSelection="['': '']" />
</div>

