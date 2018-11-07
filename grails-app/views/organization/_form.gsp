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
	<g:textField name="officeSymbol" size="20" required="" value="${organizationInstance?.officeSymbol}"/>
</div>

<g:if test="${organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
    <div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'director', 'error')} ">
    	<label for="director">
    		<g:message code="organization.director.label" default="Director" />
    	</label>
    	<g:select id="director" name="director.id" from="${organizationInstance.getAssigned()}" optionKey="id" value="${organizationInstance?.director?.id}" class="many-to-one" noSelection="['null': '']"/>
    </div>
</g:if>

%{--<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'directorName', 'error')} ">--}%
	%{--<label for="directorName">--}%
		%{--<g:message code="organization.directorName.label" default="Director Name" />--}%
	%{--</label>--}%
	%{--<g:textField name="directorName" size="50" value="${organizationInstance?.directorName}"/>--}%
%{--</div>--}%

%{--<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'directorEmail', 'error')} ">--}%
	%{--<label for="directorEmail">--}%
		%{--<g:message code="organization.directorEmail.label" default="Director Email" />--}%
	%{--</label>--}%
	%{--<g:field type="email" size="50" name="directorEmail" value="${organizationInstance?.directorEmail}"/>--}%
%{--</div>--}%

<g:if test="${!organizationInstance?.trueTD || organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
    <br/><hr/>

    <div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'supervisorApprovalRequired', 'error')}">
        <label for="supervisorApprovalRequired">
            <g:message code="organization.supervisorApprovalRequired.label" default="Supervisor Approval Required" />
        </label>
        <g:checkBox name="supervisorApprovalRequired" value="${organizationInstance?.supervisorApprovalRequired}"  />
    </div>

    <div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'attendeeRequestRequired', 'error')}">
        <label for="attendeeRequestRequired">
            <g:message code="organization.attendeeRequestRequired.label" default="TD Concurrence Required" />
        </label>
        <g:checkBox name="attendeeRequestRequired" value="${organizationInstance?.attendeeRequestRequired}"  />
    </div>

    %{--<div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'allowCreateConference', 'error')}">--}%
        %{--<label for="allowCreateConference">--}%
            %{--<g:message code="organization.allowCreateConference.label" default="Allow Create Conference" />--}%
        %{--</label>--}%
        %{--<g:checkBox name="allowCreateConference" value="${organizationInstance?.allowCreateConference}"  />--}%
    %{--</div>--}%

    <div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'allowAttendeeNotification', 'error')}">
        <label for="allowAttendeeNotification">
            <g:message code="organization.allowAttendeeNotification.label" default="Allow Attendee Notification" />
        </label>
        <g:checkBox name="allowAttendeeNotification" value="${organizationInstance?.allowAttendeeNotification}"  />
    </div>
</g:if>

<g:if test="${!organizationInstance?.trueTD || organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
    <br/><hr/>

    <div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'contacts', 'error')} ">
        <label for="contacts">
            <g:message code="organization.contacts.label" default="Contacts" />
        </label>

        <ul class="one-to-many">
            <g:each in="${organizationInstance?.contacts?}" var="c">
                <li>
                    <g:link controller="accountContact" action="show" id="${c?.id}">${c?.encodeAsHTML()}</g:link>
                    <g:if test="${c?.primaryPOC}">&nbsp;<g:img dir="images/icons" file="ok_512.png" height="16" width="16" alt="Primary POC" title="Primary POC"/></g:if>
                    <g:link controller="Organization" action="deleteContact" id="${organizationInstance.id}" params="[contactId: "${c?.id}"]"><g:img dir="images" file="reject_16n.png" alt="Delete contact" title="Delete contact"/></g:link>
                    &nbsp;<g:link controller="Organization" action="primaryContact" id="${organizationInstance.id}" params="[contactId: "${c?.id}"]"><g:if test="${!c?.primaryPOC}"><g:img dir="images/icons" file="send-user_512.png" height="16" width="16" alt="Make primary" title="Make primary"/></g:if></g:link>
                </li>
            </g:each>
            <br/>
            <li class="add">
                <g:select id="tempAccount" name="tempAccount.id" from="${organizationInstance.getAssigned()}" optionKey="id" class="many-to-one" noSelection="['': '']"/>
                &nbsp;&nbsp;<g:actionSubmit class="save" action="addContact" resource="${organizationInstance}" value="Primary" />
                &nbsp;&nbsp;<g:actionSubmit class="save" action="addContact" resource="${organizationInstance}" value="Alternate"/>
            </li>
        </ul>
    </div>
</g:if>

<g:if test="${!organizationInstance?.trueTD || organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
    <br/><hr/>

    <div class="fieldcontain ${hasErrors(bean: organizationInstance, field: 'missionStatement', 'error')}">
        <label for="missionStatement">
            <g:message code="organization.missionStatement.label" default="Mission Statement" />
        </label>
        <g:textArea name="missionStatement" cols="120" rows="20" value="${organizationInstance?.missionStatement}"/>
    </div>
</g:if>

