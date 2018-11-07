<%@ page import="org.springframework.web.util.HtmlUtils; mil.ebs.ctm.Organization; mil.ebs.ctm.Account; mil.ebs.ctm.Conference" %>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'conferenceTitle', 'error')} required">
	<label for="conferenceTitle">
		<g:message code="conference.conferenceTitle.label" default="Conference Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="conferenceTitle" size="75" required="" value="${conferenceInstance?.conferenceTitle}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'phaseState', 'error')}">
	<label for="phaseState">
		<g:message code="conference.phaseState.label" default="Phase State" />
	</label>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <g:select id="phaseState" name="phaseState" from="${conferenceInstance.constraints.phaseState.inList}" value="${conferenceInstance?.phaseState}" valueMessagePrefix="conference.phaseState" />
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_USER, ROLE_AFRL_USER, ROLE_NON_AFRL_USER, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
        <sec:ifNotGranted roles="ROLE_ADMIN">
            ${conferenceInstance?.phaseState}
        </sec:ifNotGranted>
    </sec:ifAnyGranted>
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'status', 'error')}">
	<label for="status">
		<g:message code="conference.status.label" default="Status" />
	</label>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <g:select id="status" name="status" from="${conferenceInstance.constraints.status.inList}" value="${conferenceInstance?.status}" valueMessagePrefix="conference.status" />
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_USER, ROLE_AFRL_USER, ROLE_NON_AFRL_USER, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
        <sec:ifNotGranted roles="ROLE_ADMIN">
            ${conferenceInstance?.status}
        </sec:ifNotGranted>
    </sec:ifAnyGranted>
</div>

<g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">
    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'step', 'error')} ">
        <label for="step">
            <g:message code="conference.step.label" default="Step" />
        </label>
        <sec:ifAnyGranted roles="ROLE_FMC_ADMIN, ROLE_ADMIN">
            <g:select name="step" from="${conferenceInstance.constraints.step.inList}" value="${conferenceInstance?.step}" valueMessagePrefix="conference.step"/>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_FMC_ADMIN, ROLE_ADMIN">
            ${conferenceInstance?.step}
        </sec:ifNotGranted>
    </div>
</g:if>

<br/><hr>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'startDate', 'error')} required">
	<label for="startDate">
		<g:message code="conference.startDate.label" default="Start Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker required="" id="startDate" name="startDate" precision="day" value="${conferenceInstance?.startDate}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'endDate', 'error')} required">
	<label for="endDate">
		<g:message code="conference.endDate.label" default="End Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker required="" id="endDate" name="endDate" precision="day" value="${conferenceInstance?.endDate}"  />
</div>

%{--<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'displayAfter', 'error')}">--}%
	%{--<label for="displayAfter">--}%
		%{--<g:message code="conference.displayAfter.label" default="Display After" />--}%
	%{--</label>--}%
	%{--<g:datePicker name="displayAfter" precision="day" value="${conferenceInstance?.displayAfter}"  />--}%
%{--</div>--}%

<sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'hide', 'error')}">
	<label for="hide">
		<g:message code="conference.hide.label" default="Hide" />
	</label>
	<g:checkBox name="hide" value="${conferenceInstance?.hide}"  />
</div>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'locked', 'error')}">
	<label for="locked">
		<g:message code="conference.locked.label" default="Locked" />
	</label>
	<g:checkBox name="locked" value="${conferenceInstance?.locked}"  />
</div>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
    <g:if test="${!conferenceInstance.phaseState.equalsIgnoreCase("Open")}">
        <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'numAttendees', 'error')}">
        	<label for="numAttendees">
        		<g:message code="conference.numAttendees.label" default="Max # of Attendees" />
        	</label>
        	<g:textField name="numAttendees" size="10" value="${conferenceInstance?.numAttendees}" />
            &nbsp;&nbsp;<span style="color:#777777"><i>Blank is unlimited attendees</i></span>
        </div>
    </g:if>
</sec:ifAnyGranted>
<sec:ifNotGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
    <g:if test="${conferenceInstance?.isCAO()}">
        <g:if test="${!conferenceInstance.phaseState.equalsIgnoreCase("Open")}">
            <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'numAttendees', 'error')}">
                <label for="numAttendees">
                    <g:message code="conference.numAttendees.label" default="Max # of Attendees" />
                </label>
                <g:textField name="numAttendees" size="10" value="${conferenceInstance?.numAttendees}" />
                &nbsp;&nbsp;<span style="color:#777777"><i>Blank is unlimited attendees</i></span>
            </div>
        </g:if>
    </g:if>
</sec:ifNotGranted>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'venue', 'error')} required">
	<label for="venue">
		<g:message code="conference.venue.label" default="Venue" />
        <span class="required-indicator">*</span>
	</label>
	<g:textField name="venue" size="50" required="" value="${conferenceInstance?.venue}"/>
</div>

<g:if test="${conferenceInstance?.address}">
<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'address', 'error')} ">
	<label for="address">
		<g:message code="conference.address.label" default="Address" />
	</label>
    <g:link name="address" controller="address" action="show" id="${conferenceInstance?.address?.id}">${conferenceInstance?.address?.encodeAsHTML()}</g:link>
</div>
</g:if>

<br/><hr>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'website', 'error')} ">
	<label for="website">
		<g:message code="conference.website.label" default="Website" />
	</label>
	<g:textField name="website" size="50" value="${conferenceInstance?.website}"/>
    &nbsp;&nbsp;<span style="color:#777777"><i>Copy/paste full URL (include http:// or https://)</i></span>
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'purpose', 'error')} ">
	<label for="purpose">
		<g:message code="conference.purpose.label" default="Purpose" />
	</label>
	%{--<g:textField name="purpose" size="50" value="${conferenceInstance?.purpose}"/>--}%
    <g:textArea name="purpose" cols="120" rows="20" value="${conferenceInstance?.purpose}"/>
</div>

<br/><hr>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'primaryHost', 'error')} ">
	<label for="primaryHost">
		<g:message code="conference.primaryHost.label" default="Primary Host/Sponsor" />
	</label>
	<g:textField name="primaryHost" size="50" value="${conferenceInstance?.primaryHost}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'hostType', 'error')} required">
	<label for="hostType">
		<g:message code="conference.hostType.label" default="Host Type" />
        <span class="required-indicator">*</span>
	</label>
	<g:select id="hostType" required="" name="hostType" from="${conferenceInstance.constraints.hostType.inList}" value="${conferenceInstance?.hostType}" valueMessagePrefix="conference.hostType" noSelection="['': '']"/>
</div>
<div class="afrlHost">
    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'afrlHosted', 'error')}">
    	<label for="afrlHosted">
    		<g:message code="conference.afrlHosted.label" default="AFRL Hosted" />
    	</label>
    	<g:checkBox id="afrlHosted" name="afrlHosted" value="${conferenceInstance?.afrlHosted}"  />
    </div>
</div>
<div class="nonHost">
    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'nonHostType', 'error')}">
    	<label for="nonHostType">
    		<g:message code="conference.nonHostType.label" default="Non-DoD Host" />
    	</label>
    	<g:select id="nonHostType" name="nonHostType" from="${conferenceInstance.constraints.nonHostType.inList}" value="${conferenceInstance?.nonHostType}" valueMessagePrefix="conference.nonHostType" noSelection="['': '']"/>
    </div>
</div>
<div class="coHost">
    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'coHostEntity', 'error')} ">
    	<label for="coHostEntity">
    		<g:message code="conference.coHostEntity.label" default="Co-Host(s)/Sponsor(s)" />
    	</label>
    	<g:textField id="coHostEntity" name="coHostEntity" size="50" value="${conferenceInstance?.coHostEntity}"/>
    </div>
</div>

