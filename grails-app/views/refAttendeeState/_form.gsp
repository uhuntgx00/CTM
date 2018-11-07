<%@ page import="mil.ebs.ctm.ref.RefAttendeeState" %>

<div class="fieldcontain ${hasErrors(bean: refAttendeeStateInstance, field: 'attendeeState', 'error')} ">
	<label for="attendeeState">
		<g:message code="refAttendeeState.attendeeState.label" default="Attendee State" />
	</label>
	<g:select name="attendeeState" from="${refAttendeeStateInstance.constraints.attendeeState.inList}" value="${refAttendeeStateInstance?.attendeeState}" valueMessagePrefix="refAttendeeState.attendeeState" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refAttendeeStateInstance, field: 'phaseState', 'error')} ">
	<label for="phaseState">
		<g:message code="refAttendeeState.phaseState.label" default="Phase State" />
	</label>
	<g:select name="phaseState" from="${refAttendeeStateInstance.constraints.phaseState.inList}" value="${refAttendeeStateInstance?.phaseState}" valueMessagePrefix="refAttendeeState.phaseState" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refAttendeeStateInstance, field: 'attendeeAction', 'error')} required">
	<label for="attendeeAction">
		<g:message code="refAttendeeState.attendeeAction.label" default="Attendee Action" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="attendeeAction" required="" value="${refAttendeeStateInstance?.attendeeAction}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refAttendeeStateInstance, field: 'nextState', 'error')} required">
	<label for="nextState">
		<g:message code="refAttendeeState.nextState.label" default="Next State" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nextState" required="" value="${refAttendeeStateInstance?.nextState}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refAttendeeStateInstance, field: 'actionCommand', 'error')} required">
	<label for="actionCommand">
		<g:message code="refAttendeeState.actionCommand.label" default="Action Command" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="actionCommand" required="" value="${refAttendeeStateInstance?.actionCommand}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refAttendeeStateInstance, field: 'actionPermission', 'error')} required">
	<label for="actionPermission">
		<g:message code="refAttendeeState.actionPermission.label" default="Action Permission" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="actionPermission" size="75" required="" value="${refAttendeeStateInstance?.actionPermission}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refAttendeeStateInstance, field: 'actionNotification', 'error')} ">
	<label for="actionNotification">
		<g:message code="refAttendeeState.actionNotification.label" default="Action Notification" />
	</label>
	<g:textField name="actionNotification" value="${refAttendeeStateInstance?.actionNotification}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refAttendeeStateInstance, field: 'dateGateEvent', 'error')} ">
	<label for="dateGateEvent">
		<g:message code="refAttendeeState.dateGateEvent.label" default="Date Gate Event" />
	</label>
	<g:textField name="dateGateEvent" value="${refAttendeeStateInstance?.dateGateEvent}"/>
</div>

