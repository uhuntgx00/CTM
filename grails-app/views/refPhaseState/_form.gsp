<%@ page import="mil.ebs.ctm.mail.MailTemplate; mil.ebs.ctm.ref.RefDateGate; mil.ebs.ctm.Conference; mil.ebs.ctm.ref.RefPhaseState" %>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'phaseState', 'error')} ">
	<label for="phaseState">
		<g:message code="refPhaseState.phaseState.label" default="Phase State" />
	</label>
	<g:select name="phaseState" from="${refPhaseStateInstance.constraints.phaseState.inList}" value="${refPhaseStateInstance?.phaseState}" valueMessagePrefix="refPhaseState.phaseState"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'nextPhaseState', 'error')} ">
	<label for="nextPhaseState">
		<g:message code="refPhaseState.nextPhaseState.label" default="Next Phase State" />
	</label>
	<g:select name="nextPhaseState" from="${refPhaseStateInstance.constraints.nextPhaseState.inList}" value="${refPhaseStateInstance?.nextPhaseState}" valueMessagePrefix="refPhaseState.nextPhaseState"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'phaseAction', 'error')} required">
	<label for="phaseAction">
		<g:message code="refPhaseState.phaseAction.label" default="Phase Action" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="phaseAction" required="" value="${refPhaseStateInstance?.phaseAction}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'buttonClass', 'error')}">
	<label for="buttonClass">
		<g:message code="refPhaseState.buttonClass.label" default="Button Class" />
	</label>
	<g:textField name="buttonClass" value="${refPhaseStateInstance?.buttonClass}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'actionStatus', 'error')} required">
	<label for="actionStatus">
		<g:message code="refPhaseState.actionStatus.label" default="Action Status" />
		<span class="required-indicator">*</span>
	</label>
    <g:select name="actionStatus" from="${Conference.constraints.status.inList}" value="${refPhaseStateInstance?.actionStatus}" valueMessagePrefix="refPhaseState.actionStatus"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'actionController', 'error')}">
	<label for="actionController">
		<g:message code="refPhaseState.actionController.label" default="Action Controller" />
	</label>
	<g:textField name="actionController" value="${refPhaseStateInstance?.actionController}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'actionCommand', 'error')} required">
	<label for="actionCommand">
		<g:message code="refPhaseState.actionCommand.label" default="Action Command" />
		<span class="required-indicator">*</span>
	</label>
	%{--<g:textField name="actionCommand" required="" value="${refPhaseStateInstance?.actionCommand}"/>--}%
    <g:select name="actionCommand" from="${['cancelConference', 'forwardConference', 'externalConference', 'edit', 'approveConference', 'disapproveConference', 'submitPackage',
            'submitAFMC', 'submitSAF', 'returnConference', 'reviseConference', 'archiveConference', 'openConference', 'finalizeConference', 'packageFileUpload', 'safMemoFileUpload',
            'aarFileUpload', 'retrieveAllFiles', 'attendConference', 'addTdSlot', 'addOpenSlot', 'createVenueAddress', 'retrieveApprovalMemo', 'baselineConference']}" value="${refPhaseStateInstance?.actionCommand}" valueMessagePrefix="refPhaseState.actionCommand"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'actionPermission', 'error')} required">
	<label for="actionPermission">
		<g:message code="refPhaseState.actionPermission.label" default="Action Permission" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="actionPermission" size="100" required="" value="${refPhaseStateInstance?.actionPermission}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'actionCheck', 'error')} ">
	<label for="actionCheck">
		<g:message code="refPhaseState.actionCheck.label" default="Action Check" />
	</label>
    <g:select name="actionCheck" from="${refPhaseStateInstance.constraints.actionCheck.inList}" value="${refPhaseStateInstance?.actionCheck}" valueMessagePrefix="refPhaseState.actionCheck"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'actionNotification', 'error')} ">
	<label for="actionNotification">
		<g:message code="refPhaseState.actionNotification.label" default="Action Notification" />
	</label>
	%{--<g:textField name="actionNotification" value="${refPhaseStateInstance?.actionNotification}"/>--}%
    <g:select name="actionNotification" from="${MailTemplate.findAllByTemplateNameIlike('notify%')}" value="${refPhaseStateInstance?.actionNotification}" valueMessagePrefix="refPhaseState.actionNotification" noSelection="['': '']"/>
    %{--<g:select name="actionNotification" from="${['', 'notifyCancellation', 'notifySupervisor', 'notifyCAO', 'notifyRevise', 'notifyApproval', 'notifyDisapprove', 'notifyTD', 'notifyFMC']}" value="${refPhaseStateInstance?.actionNotification}" valueMessagePrefix="refPhaseState.actionNotification"/>--}%
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'dateGateEvent', 'error')} ">
	<label for="dateGateEvent">
		<g:message code="refPhaseState.dateGateEvent.label" default="Date Gate Event" />
	</label>
	%{--<g:textField name="dateGateEvent" value="${refPhaseStateInstance?.dateGateEvent}"/>--}%
    <g:select name="dateGateEvent" optionKey="code" optionValue="name" from="${RefDateGate.list().sort {it.id}}" value="${refPhaseStateInstance?.dateGateEvent}" valueMessagePrefix="refPhaseState.dateGateEvent" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'displayDisabled', 'error')} ">
	<label for="displayDisabled">
		<g:message code="refPhaseState.displayDisabled.label" default="Display Disabled" />
	</label>
	<g:checkBox name="displayDisabled" value="${refPhaseStateInstance?.displayDisabled}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseStateInstance, field: 'checkPermission', 'error')} ">
	<label for="checkPermission">
		<g:message code="refPhaseState.checkPermission.label" default="Check Permission" />
	</label>
	<g:checkBox name="checkPermission" value="${refPhaseStateInstance?.checkPermission}" />
</div>


