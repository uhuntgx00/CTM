<%@ page import="mil.ebs.ctm.Conference" %>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'conferenceTitle', 'error')} required">
	<label for="conferenceTitle">
		<g:message code="conference.conferenceTitle.label" default="Conference Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="conferenceTitle" required="" value="${conferenceInstance?.conferenceTitle}" maxlength="250" size="50"/>
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'venue', 'error')} ">
	<label for="venue">
		<g:message code="conference.venue.label" default="Venue" />
	</label>
	<g:textField name="venue" value="${conferenceInstance?.venue}" maxlength="250" size="50"/>
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'startDate', 'error')} required">
	<label for="startDate">
		<g:message code="conference.startDate.label" default="Start Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="startDate" precision="day"  value="${conferenceInstance?.startDate}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'endDate', 'error')} required">
	<label for="endDate">
		<g:message code="conference.endDate.label" default="End Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="endDate" precision="day"  value="${conferenceInstance?.endDate}"  />
</div>


