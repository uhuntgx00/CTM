
<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'conferenceTitle', 'error')}">
    <label for="conference">
        <b>Conference:</b>
    </label>
    ${conferenceInstance?.conferenceTitle}
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'numAttendees', 'error')}">
    <label for="numAttendees">
        <b>CAP Limit:</b>
    </label>
    <g:if test="${conferenceInstance?.numAttendees}">
        ${conferenceInstance?.numAttendees}
    </g:if>
    <g:else>
        ~
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'attendees', 'error')} ">
    <label for="attendees">
        <b>Attendee(s):</b> <i>(${attendeeList?.size()})</i>
    </label>

    <ul class="one-to-many">
        <g:if test="${attendeeList}">
            <g:select id="attendees" name="attendees" multiple="true" size="${attendeeList.size()}" from="${attendeeList}" optionKey="id" value="${attendeeList.toString()}" class="many-to-one"/>
            <br/>
            <br/>
            <center>
                <g:submitButton name="Up" value="Up" onclick="move_listbox_item_up('attendees');return false;"/>
                <g:submitButton name="Down" value="Down" onclick="move_listbox_item_down('attendees');return false;"/>
            </center>
            <br/>
        </g:if>
    </ul>
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'conferenceTitle', 'error')}">
    <label for="legend">
        <b><span style="color:#0000FF">Legend:</span></b>
    </label>
    <g:img style="vertical-align: middle" dir="/images" file="Letter-A-orange-icon.png" width="24" height="24" alt="(A)ttendee" title="(A)ttendee"/>ttendee -
    <g:img style="vertical-align: middle" dir="/images" file="Letter-B-blue-icon.png" width="24" height="24" alt="(B)ooth/Display" title="(B)ooth/Display"/>ooth/Display -
    Session <g:img style="vertical-align: middle" dir="/images" file="Letter-C-lg-icon.png" width="24" height="24" alt="Session (C)hair" title="Session (C)hair"/>hair -
    <g:img style="vertical-align: middle" dir="/images" file="Letter-D-grey-icon.png" width="24" height="24" alt="(D)iscussion Panel" title="(D)iscussion Panel"/>iscussion Panel -
    <g:img style="vertical-align: middle" dir="/images" file="Letter-P-dg-icon.png" width="24" height="24" alt="(P)resenter/Speaker" title="(P)resenter/Speaker"/>resenter/Speaker -
    <g:img style="vertical-align: middle" dir="/images" file="Letter-S-red-icon.png" width="24" height="24" alt="(S)upport" title="(S)upport"/>upport -
    <g:img style="vertical-align: middle" dir="/images" file="Letter-O-violet-icon.png" width="24" height="24" alt="(O)ther" title="(O)ther"/>ther
</div>
