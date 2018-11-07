<%@ page import="mil.ebs.ctm.ref.RefPhase" %>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'phase', 'error')} ">
	<label for="phase">
		<g:message code="refPhase.phase.label" default="Phase" />
	</label>
	<g:select name="phase" from="${refPhaseInstance.constraints.phase.inList}" value="${refPhaseInstance?.phase}" valueMessagePrefix="refPhaseInstance.phase"/>
</div>


<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'roles', 'error')} ">
	<label for="roles">
		<g:message code="refPhase.roles.label" default="Roles" />
	</label>
	<g:textField size="75" name="roles" value="${refPhaseInstance?.roles}" />
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canUserEstimate', 'error')} ">
	<label for="canUserEstimate">
		<g:message code="refPhase.canUserEstimate.label" default="Can User Estimate" />
	</label>
	<g:checkBox name="canUserEstimate" value="${refPhaseInstance?.canUserEstimate}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canUserActual', 'error')} ">
	<label for="canUserActual">
		<g:message code="refPhase.canUserActual.label" default="Can User Actual" />
	</label>
	<g:checkBox name="canUserActual" value="${refPhaseInstance?.canUserActual}" />
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canCaoEstimate', 'error')} ">
	<label for="canCaoEstimate">
		<g:message code="refPhase.canCaoEstimate.label" default="Can CAO Estimate" />
	</label>
	<g:checkBox name="canCaoEstimate" value="${refPhaseInstance?.canCaoEstimate}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canCaoActual', 'error')} ">
	<label for="canCaoActual">
		<g:message code="refPhase.canCaoActual.label" default="Can CAO Actual" />
	</label>
	<g:checkBox name="canCaoActual" value="${refPhaseInstance?.canCaoActual}" />
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canTdEstimate', 'error')} ">
	<label for="canTdEstimate">
		<g:message code="refPhase.canTdEstimate.label" default="Can TD Estimate" />
	</label>
	<g:checkBox name="canTdEstimate" value="${refPhaseInstance?.canTdEstimate}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canTdActual', 'error')} ">
	<label for="canTdActual">
		<g:message code="refPhase.canTdActual.label" default="Can TD Actual" />
	</label>
	<g:checkBox name="canTdActual" value="${refPhaseInstance?.canTdActual}" />
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canFmcEstimate', 'error')} ">
	<label for="canFmcEstimate">
		<g:message code="refPhase.canFmcEstimate.label" default="Can FMC Estimate" />
	</label>
	<g:checkBox name="canFmcEstimate" value="${refPhaseInstance?.canFmcEstimate}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canFmcActual', 'error')} ">
	<label for="canFmcActual">
		<g:message code="refPhase.canFmcActual.label" default="Can FMC Actual" />
	</label>
	<g:checkBox name="canFmcActual" value="${refPhaseInstance?.canFmcActual}" />
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canAddAttendee', 'error')} ">
	<label for="canAddAttendee">
		<g:message code="refPhase.canAddAttendee.label" default="Can Add Attendee" />
	</label>
	<g:checkBox name="canAddAttendee" value="${refPhaseInstance?.canAddAttendee}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canEditAttendee', 'error')} ">
	<label for="canEditAttendee">
		<g:message code="refPhase.canEditAttendee.label" default="Can Edit Attendee" />
	</label>
	<g:checkBox name="canEditAttendee" value="${refPhaseInstance?.canEditAttendee}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canDeleteAttendee', 'error')} ">
	<label for="canDeleteAttendee">
		<g:message code="refPhase.canDeleteAttendee.label" default="Can Delete Attendee" />
	</label>
	<g:checkBox name="canDeleteAttendee" value="${refPhaseInstance?.canDeleteAttendee}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refPhaseInstance, field: 'canManageAttendee', 'error')} ">
	<label for="canManageAttendee">
		<g:message code="refPhase.canManageAttendee.label" default="Can Manage Attendee" />
	</label>
	<g:checkBox name="canManageAttendee" value="${refPhaseInstance?.canManageAttendee}" />
</div>

