<%@ page import="mil.ebs.ctm.Account; mil.ebs.ctm.Organization" %>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="organization.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" size="50" required="" value="${organizationInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'officeSymbol', 'error')} required">
	<label for="officeSymbol">
		<g:message code="organization.officeSymbol.label" default="Office Symbol" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="officeSymbol" size="10" required="" value="${organizationInstance?.officeSymbol}"/>
</div>

<g:if test="${organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
    <div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'director', 'error')} ">
    	<label for="director">
    		<g:message code="organization.director.label" default="Director" />
    	</label>
    	<g:select id="director" name="director.id" from="${Account.list()}" optionKey="id" value="${organizationInstance?.director?.id}" class="many-to-one" noSelection="['null': '']"/>
    </div>
</g:if>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'trueTD', 'error')}">
    <label for="trueTD">
        <g:message code="organization.trueTD.label" default="Technical Directive (TD)" />
    </label>
    <g:checkBox name="trueTD" value="${organizationInstance?.trueTD}"  />
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'supervisorApprovalRequired', 'error')}">
    <label for="supervisorApprovalRequired">
        <g:message code="organization.supervisorApprovalRequired.label" default="Supervisor Approval Required" />
    </label>
    <g:checkBox name="supervisorApprovalRequired" value="${organizationInstance?.supervisorApprovalRequired}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'attendeeRequestRequired', 'error')}">
    <label for="attendeeRequestRequired">
        <g:message code="organization.attendeeRequestRequired.label" default="Attendee Request Required" />
    </label>
    <g:checkBox name="attendeeRequestRequired" value="${organizationInstance?.attendeeRequestRequired}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'allowCreateConference', 'error')}">
    <label for="allowCreateConference">
        <g:message code="organization.allowCreateConference.label" default="Allow Create Conference" />
    </label>
    <g:checkBox name="allowCreateConference" value="${organizationInstance?.allowCreateConference}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'allowAttendeeNotification', 'error')}">
    <label for="allowAttendeeNotification">
        <g:message code="organization.allowAttendeeNotification.label" default="Allow Attendee Notification" />
    </label>
    <g:checkBox name="allowAttendeeNotification" value="${organizationInstance?.allowAttendeeNotification}"  />
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'missionStatement', 'error')}">
	<label for="missionStatement">
		<g:message code="organization.missionStatement.label" default="Mission Statement" />
	</label>
    <g:textArea name="missionStatement" cols="120" rows="20" value="${organizationInstance?.missionStatement}"/>
</div>
