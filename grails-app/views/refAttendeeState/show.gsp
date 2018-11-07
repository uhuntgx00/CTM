
<%@ page import="mil.ebs.ctm.ref.RefDateGate; mil.ebs.ctm.ref.RefAttendeeState" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refAttendeeState.label', default: 'RefAttendeeState')}" />
		<title>Show Attendee State</title>
	</head>
	<body>
		<a href="#show-refAttendeeState" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-refAttendeeState" class="content scaffold-show" role="main">
			<h1>Show Attendee State</h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list refAttendeeState">
				<g:if test="${refAttendeeStateInstance?.attendeeState}">
				<li class="fieldcontain">
					<span id="attendeeState-label" class="property-label"><g:message code="refAttendeeState.attendeeState.label" default="Attendee State" /></span>
					<span class="property-value" aria-labelledby="attendeeState-label"><g:fieldValue bean="${refAttendeeStateInstance}" field="attendeeState"/></span>
				</li>
				</g:if>
			
				<g:if test="${refAttendeeStateInstance?.phaseState}">
				<li class="fieldcontain">
					<span id="phaseState-label" class="property-label"><g:message code="refAttendeeState.phaseState.label" default="Phase State" /></span>
					<span class="property-value" aria-labelledby="phaseState-label"><g:fieldValue bean="${refAttendeeStateInstance}" field="phaseState"/></span>
				</li>
				</g:if>
			
				<g:if test="${refAttendeeStateInstance?.attendeeAction}">
				<li class="fieldcontain">
					<span id="attendeeAction-label" class="property-label"><g:message code="refAttendeeState.attendeeAction.label" default="Attendee Action" /></span>
					<span class="property-value" aria-labelledby="attendeeAction-label"><g:fieldValue bean="${refAttendeeStateInstance}" field="attendeeAction"/></span>
				</li>
				</g:if>
			
				<g:if test="${refAttendeeStateInstance?.nextState}">
				<li class="fieldcontain">
					<span id="nextState-label" class="property-label"><g:message code="refAttendeeState.nextState.label" default="Next State" /></span>
					<span class="property-value" aria-labelledby="nextState-label"><g:fieldValue bean="${refAttendeeStateInstance}" field="nextState"/></span>
				</li>
				</g:if>
			
				<g:if test="${refAttendeeStateInstance?.actionCommand}">
				<li class="fieldcontain">
					<span id="actionCommand-label" class="property-label"><g:message code="refAttendeeState.actionCommand.label" default="Action Command" /></span>
					<span class="property-value" aria-labelledby="actionCommand-label"><g:fieldValue bean="${refAttendeeStateInstance}" field="actionCommand"/></span>
				</li>
				</g:if>
			
				<g:if test="${refAttendeeStateInstance?.actionPermission}">
				<li class="fieldcontain">
					<span id="actionPermission-label" class="property-label"><g:message code="refAttendeeState.actionPermission.label" default="Action Permission" /></span>
					<span class="property-value" aria-labelledby="actionPermission-label"><g:fieldValue bean="${refAttendeeStateInstance}" field="actionPermission"/></span>
				</li>
				</g:if>
			
				<g:if test="${refAttendeeStateInstance?.actionNotification}">
				<li class="fieldcontain">
					<span id="actionNotification-label" class="property-label"><g:message code="refAttendeeState.actionNotification.label" default="Action Notification" /></span>
					<span class="property-value" aria-labelledby="actionNotification-label"><g:fieldValue bean="${refAttendeeStateInstance}" field="actionNotification"/></span>
				</li>
				</g:if>
			
				<g:if test="${refAttendeeStateInstance?.dateGateEvent}">
				<li class="fieldcontain">
					<span id="dateGateEvent-label" class="property-label"><g:message code="refAttendeeState.dateGateEvent.label" default="Date Gate Event" /></span>
					<span class="property-value" aria-labelledby="dateGateEvent-label">
                        <g:fieldValue bean="${refAttendeeStateInstance}" field="dateGateEvent"/>
                        <i>(${mil.ebs.ctm.ref.RefDateGate.findByCode(refAttendeeStateInstance?.dateGateEvent)})</i>
                    </span>
				</li>
				</g:if>
			</ol>

			<g:form url="[resource:refAttendeeStateInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${refAttendeeStateInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <sec:ifAnyGranted roles="ROLE_DEVELOPER">
                        <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
