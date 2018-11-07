<%@ page import="mil.ebs.ctm.Conference" %>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'approvalNotice', 'error')} ">
	<label for="approvalNotice">
		<g:message code="conference.approvalNotice.label" default="Approval Notice" />
	</label>
    <g:textArea name="approvalNotice" cols="120" rows="20" value="${conferenceInstance?.approvalNotice}"/>
</div>


