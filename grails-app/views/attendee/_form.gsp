<%@ page import="mil.ebs.ctm.ref.RefRankGrade; mil.ebs.ctm.Organization; mil.ebs.ctm.Conference; mil.ebs.ctm.Account; mil.ebs.ctm.Attendee" %>

<sec:ifAnyGranted roles="ROLE_ADMIN">
<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'status', 'error')}">
	<label for="status">
		<g:message code="attendee.status.label" default="Status" />
	</label>
    <g:select name="status" from="${Attendee.constraints.status.inList}" value="${attendeeInstance?.status}" />
</div>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_SEQUENCE">
<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'sequence', 'error')}">
	<label for="sequence">
		<g:message code="attendee.sequence.label" default="Sequence" />
	</label>
    <g:textField name="sequence" size="10" value="${attendeeInstance?.sequence}"/>
</div>
</sec:ifAnyGranted>

<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'reservedTD', 'error')} ">
	<label for="reservedTD">
		<g:message code="attendee.reservedTd.label" default="Reserved Org" />
	</label>
	<g:select
        id="reservedTD" name="reservedTD.id" from="${Organization.findAllByLevelTDAndParent("1", null).sort {it.officeSymbol} }" optionKey="id" value="${attendeeInstance?.reservedTD?.id}" class="many-to-one" noSelection="['': 'Attendee slot not reserved for TD']"
        onchange="${remoteFunction(
            controller: 'attendee',
            action: 'ajaxGetAccounts',
            params: '\'id=\' + escape(this.value)',
            update: 'accountLinkBlock'
        )}"
    />
</div>

<div id="reservedOrgBlock" class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'reservedOrg', 'error')} ">
	<label for="reservedOrg">
		<g:message code="attendee.reservedOrg.label" default="Non-AFMC Organization" />
	</label>
	<g:textField name="reservedOrg" size="50" value="${attendeeInstance?.reservedOrg}"/>
</div>

%{--<sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_ADMIN, ROLE_DEVELOPER">--}%
<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'accountType', 'error')}">
	<label for="accountType">
		<g:message code="attendee.accountType.label" default="Account Type" />
	</label>
    <g:select name="accountType" from="${Attendee.constraints.accountType.inList}" value="${attendeeInstance?.accountType}" />
</div>
%{--</sec:ifAnyGranted>--}%

<div id="accountLinkBlock" class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'userLink', 'error')}">
	<label for="accountLink">
		<g:message code="attendee.userLink.label" default="Account Link" />
	</label>
	<g:select id="accountLink" name="accountLink.id" from="${accountList}" optionKey="id" value="${attendeeInstance?.accountLink?.id}" class="many-to-one" noSelection="['': '']"
          onchange="${remoteFunction(
              controller: 'attendee',
              action: 'ajaxGetInternalAccount',
              params: '\'id=\' + escape(this.value)',
              update: 'internalBlock'
          )}"
    />
</div>

<div id="internalBlock">
    <g:if test="${attendeeInstance?.supervisor}">
        <div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'supervisor', 'error')} ">
            <label for="currentSupervisor">
                <g:message code="attendee.currentSupervisor.label" default="Current Supervisor" />
            </label>
            <g:link controller="account" action="show" id="${attendeeInstance?.supervisor?.id}">${attendeeInstance?.supervisor?.encodeAsHTML()}</g:link>
        </div>
    </g:if>

    <g:if test="${attendeeInstance?.supervisor?.id != attendeeInstance?.accountLink?.supervisor?.id}">
        <div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'supervisor', 'error')} ">
            <label for="newSupervisor">
                <g:message code="attendee.newSupervisor.label" default="New Supervisor" />
            </label>
            <g:link controller="account" action="show" id="${attendeeInstance?.accountLink?.supervisor?.id}">${attendeeInstance?.accountLink?.supervisor?.encodeAsHTML()}</g:link>
        </div>
    </g:if>

    <g:if test="${attendeeInstance?.rankGrade}">
        <div id="rankGradeBlock" class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'rankGrade', 'error')} ">
            <label for="rankGrade">
                <g:message code="attendee.rankGrade.label" default="Rank/Grade" />
            </label>
            ${attendeeInstance?.rankGrade}
        </div>
    </g:if>
</div>

<div id="accountNameBlock" class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'name', 'error')}">
    <label for="name">
        <g:message code="attendee.name.label" default="Name" />
    </label>
    <g:textField name="name" size="50" value="${attendeeInstance?.name}"/>
    &nbsp;&nbsp;<span style="color:#777777"><i>This field is not linked to any <b>ACCOUNT</b></i></span>
</div>

<div id="externalBlock">
    <div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'rankGrade', 'error')}">
        <label for="rankGrade">
            <g:message code="attendee.rankGrade.label" default="Rank/Grade" />
        </label>
        <g:select id="rankGrade" name="rankGrade.id" from="${RefRankGrade.list()}" optionKey="id" value="${attendeeInstance?.rankGrade?.id}" class="many-to-one" noSelection="['null': '']"/>
    </div>
</div>

<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'extEmailAddress', 'error')}">
	<label for="extEmailAddress">
		<g:message code="attendee.extEmailAddress.label" default="External Email" />
	</label>
    <g:textField name="extEmailAddress" size="50" value="${attendeeInstance?.extEmailAddress}"/>
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'startTravelDate', 'error')} ">
	<label for="startTravelDate">
		<g:message code="attendee.startTravelDate.label" default="Start Travel Date" />
	</label>
	<g:datePicker name="startTravelDate" precision="day"  value="${attendeeInstance?.startTravelDate}" default="none" noSelection="['': '']" />
    &nbsp;&nbsp;<span style="color:#777777"><i><b>Conference Starts:</b>&nbsp;<g:formatDate date="${attendeeInstance?.conference?.startDate}" type="date" dateStyle="Full"/></i></span>
</div>

<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'endTravelDate', 'error')} ">
	<label for="endTravelDate">
		<g:message code="attendee.endTravelDate.label" default="End Travel Date" />
	</label>
	<g:datePicker name="endTravelDate" precision="day"  value="${attendeeInstance?.endTravelDate}" default="none" noSelection="['': '']" />
    &nbsp;&nbsp;<span style="color:#777777"><i><b>Conference Ends:</b>&nbsp;<g:formatDate date="${attendeeInstance?.conference?.endDate}" type="date" dateStyle="Full"/></i></span>
</div>

%{--<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'mealsIncluded', 'error')} required">--}%
	%{--<label for="mealsIncluded">--}%
		%{--<g:message code="attendee.mealsIncluded.label" default="Meals Included" />--}%
		%{--<span class="required-indicator">*</span>--}%
	%{--</label>--}%
	%{--<g:field id="mealsIncluded" name="mealsIncluded" size="5" value="${attendeeInstance.mealsIncluded}" required=""/>--}%
%{--</div>--}%

<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'attendanceType', 'error')} required">
	<label for="attendanceType">
		<g:message code="attendee.attendanceType.label" default="Attendance Type" />
        <span class="required-indicator">*</span>
	</label>
	<g:select id="attendanceType" name="attendanceType" from="${attendeeInstance.constraints.attendanceType.inList}" value="${attendeeInstance?.attendanceType}" valueMessagePrefix="attendee.attendanceType" required=""/>
</div>

%{--<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'hoursAttendanceType', 'error')} required">--}%
	%{--<label for="hoursAttendanceType">--}%
		%{--<g:message code="attendee.hoursAttendanceType.label" default="Hours Attendance Type" />--}%
		%{--<span class="required-indicator">*</span>--}%
	%{--</label>--}%
	%{--<g:textField id="hoursAttendanceType" name="hoursAttendanceType" size="5" value="${attendeeInstance.hoursAttendanceType}" required=""/>--}%
%{--</div>--}%

<div class="fieldcontain ${hasErrors(bean: attendeeInstance, field: 'justification', 'error')} ">
	<label for="justification">
		<g:message code="attendee.justification.label" default="Justification" />
	</label>
    <g:textArea name="justification" cols="120" rows="20" value="${attendeeInstance?.justification}"/>
</div>

%{--<br/><hr/>--}%

%{--<div class="fieldcontain">--}%
	%{--<label for="fundSources">--}%
		%{--<g:message code="attendee.fundSources.label" default="Fund Source" />--}%
	%{--</label>--}%
    %{--<span>--}%
        %{--<input type="text" size="32" id="fundSource1" readonly style="text-align: center; border:0; color:#1f93f6; background-color: rgba(1,1,1,.02); font-weight: bold;">--}%
        %{--<input type="text" size="32" id="fundSource2" readonly style="text-align: center; border:0; color:#1f93f6; background-color: rgba(1,1,1,.02); font-weight: bold;">--}%
        %{--<input type="text" size="32" id="fundSource3" readonly style="text-align: center; border:0; color:#1f93f6; background-color: rgba(1,1,1,.02); font-weight: bold;">--}%
        %{--<g:hiddenField id="fundSource1a" name="fundSource1a" value="${fundSource1a}" />--}%
        %{--<g:hiddenField id="fundSource2a" name="fundSource2a" value="${fundSource2a}" />--}%
        %{--<g:hiddenField id="fundSource3a" name="fundSource3a" value="${fundSource3a}" />--}%
        %{--<div id="fundSourceSliderDiv">--}%
            %{--<div id="fundSourceSlider"></div>--}%
        %{--</div>--}%
        %{--<br/>--}%
        %{--<span class="fundSource" style="color:#777777"><i>The <b>FUND SOURCE</b> selector above has two slider controls one on each end of the slider.</i></span>--}%
        %{--<span class="fundSource" style="color:#777777"><i>The left slider control handles the <b>Other US Govt</b> funding, while the right slider control handles the <b>Non-Federal Entity</b> funding.</i></span>--}%
        %{--<span class="fundSource" style="color:#777777"><i>The <b>US Air Force</b> funding is computed by taking the remainder from the total of the two slide controls.</i></span>--}%
        %{--<span class="fundSource" style="color:#777777"><i>The slide control ensure that a total funding source of 100% is maintained.</i></span>--}%
    %{--</span>--}%
%{--</div>--}%

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'notifyChanges', 'error')}">
	<label for="notifyChanges">
		<g:message code="conference.notifyChanges.label" default="Notify Changes" />
	</label>
	<g:checkBox name="notifyChanges" value="${attendeeInstance?.notifyChanges}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'notifyConferenceChanges', 'error')}">
	<label for="notifyConferenceChanges">
		<g:message code="conference.notifyConferenceChanges.label" default="Notify Conference Changes" />
	</label>
	<g:checkBox name="notifyConferenceChanges" value="${attendeeInstance?.notifyConferenceChanges}"  />
</div>


