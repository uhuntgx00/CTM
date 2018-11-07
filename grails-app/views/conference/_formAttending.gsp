
<br/><hr>

<div class="fieldcontain">
	<label for="attending">
		<g:message code="conference.attending.label" default="Attending" />
	</label>
	
    <ul class="one-to-many">
        <g:checkBox name="attending"/>
        %{--<g:each in="${conferenceInstance?.attendees?}" var="a">--}%
            %{--<li><g:link controller="attendee" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>--}%
        %{--</g:each>--}%
        %{--<li class="add">--}%
            %{--<g:link name="attendees" controller="attendee" action="create" params="['conference.id': conferenceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'attendee.label', default: 'Attendee')])}</g:link>--}%
        %{--</li>--}%
    </ul>
</div>

