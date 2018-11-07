<%@ page import="mil.ebs.ctm.Conference" %>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'disapproveNotice', 'error')} ">
	<label for="disapproveNotice">
		<g:message code="conference.disapproveNotice.label" default="Disapproval Notice" />
	</label>
    <g:textArea name="disapproveNotice" cols="120" rows="20" value="${conferenceInstance?.disapproveNotice}"/>
</div>


