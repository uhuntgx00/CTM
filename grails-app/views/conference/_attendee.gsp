<%@ page import="mil.ebs.ctm.Conference" %>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'attendees', 'error')} ">
	<label for="attendees">
		<g:message code="conference.attendees.label" default="Attendees" />
	</label>
	
    <ul class="one-to-many">
        <g:each in="${conferenceInstance?.attendees?}" var="a">
            <li><g:link controller="attendee" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="attendee" action="create" params="['conference.id': conferenceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'attendee.label', default: 'Attendee')])}</g:link>
        </li>
    </ul>
</div>

