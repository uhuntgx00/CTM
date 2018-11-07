
<%@ page import="mil.ebs.ctm.ref.RefPhase" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refPhase.label', default: 'RefPhase')}" />
		<title>Show Phase ${refPhaseInstance?.phase}</title>
	</head>
	<body>
		<a href="#show-refPhase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-refPhase" class="content scaffold-show" role="main">
			<h1>Show Phase <b>${refPhaseInstance?.phase}</b></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>

   			<ol class="property-list refPhase">
				<g:if test="${refPhaseInstance?.roles}">
				<li class="fieldcontain">
					<span id="roles-label" class="property-label"><g:message code="refPhase.roles.label" default="Roles" /></span>
					<span class="property-value" aria-labelledby="roles-label"><g:fieldValue bean="${refPhaseInstance}" field="roles"/></span>
				</li>
				</g:if>

                <br/><hr/>

				<li class="fieldcontain">
					<span id="canUserEstimate-label" class="property-label"><g:message code="refPhase.canUserEstimate.label" default="Can User Estimate" /></span>
					<span class="property-value" aria-labelledby="canUserEstimate-label"><g:formatBoolean boolean="${refPhaseInstance?.canUserEstimate}" /></span>
				</li>

				<li class="fieldcontain">
					<span id="canUserActual-label" class="property-label"><g:message code="refPhase.canUserActual.label" default="Can User Actual" /></span>
					<span class="property-value" aria-labelledby="canUserActual-label"><g:formatBoolean boolean="${refPhaseInstance?.canUserActual}" /></span>
				</li>

                <br/><hr/>

				<li class="fieldcontain">
					<span id="canCaoEstimate-label" class="property-label"><g:message code="refPhase.canCaoEstimate.label" default="Can CAO Estimate" /></span>
					<span class="property-value" aria-labelledby="canCaoEstimate-label"><g:formatBoolean boolean="${refPhaseInstance?.canCaoEstimate}" /></span>
				</li>

                <li class="fieldcontain">
                    <span id="canCaoActual-label" class="property-label"><g:message code="refPhase.canCaoActual.label" default="Can CAO Actual" /></span>
                    <span class="property-value" aria-labelledby="canCaoActual-label"><g:formatBoolean boolean="${refPhaseInstance?.canCaoActual}" /></span>
                </li>

                <br/><hr/>

				<li class="fieldcontain">
					<span id="canTdEstimate-label" class="property-label"><g:message code="refPhase.canTdEstimate.label" default="Can TD Estimate" /></span>
					<span class="property-value" aria-labelledby="canTdEstimate-label"><g:formatBoolean boolean="${refPhaseInstance?.canTdEstimate}" /></span>
				</li>

                <li class="fieldcontain">
                    <span id="canTdActual-label" class="property-label"><g:message code="refPhase.canTdActual.label" default="Can TD Actual" /></span>
                    <span class="property-value" aria-labelledby="canTdActual-label"><g:formatBoolean boolean="${refPhaseInstance?.canTdActual}" /></span>
                </li>

                <br/><hr/>

				<li class="fieldcontain">
					<span id="canFmcEstimate-label" class="property-label"><g:message code="refPhase.canFmcEstimate.label" default="Can FMC Estimate" /></span>
					<span class="property-value" aria-labelledby="canFmcEstimate-label"><g:formatBoolean boolean="${refPhaseInstance?.canFmcEstimate}" /></span>
				</li>

				<li class="fieldcontain">
					<span id="canFmcActual-label" class="property-label"><g:message code="refPhase.canFmcActual.label" default="Can FMC Actual" /></span>
					<span class="property-value" aria-labelledby="canFmcActual-label"><g:formatBoolean boolean="${refPhaseInstance?.canFmcActual}" /></span>
				</li>

                <br/><hr/>

				<li class="fieldcontain">
					<span id="canAddAttendee-label" class="property-label"><g:message code="refPhase.canAddAttendee.label" default="Can Add Attendee" /></span>
					<span class="property-value" aria-labelledby="canAddAttendee-label"><g:formatBoolean boolean="${refPhaseInstance?.canAddAttendee}" /></span>
				</li>

				<li class="fieldcontain">
					<span id="canEditAttendee-label" class="property-label"><g:message code="refPhase.canEditAttendee.label" default="Can Edit Attendee" /></span>
					<span class="property-value" aria-labelledby="canEditAttendee-label"><g:formatBoolean boolean="${refPhaseInstance?.canEditAttendee}" /></span>
				</li>

				<li class="fieldcontain">
					<span id="canDeleteAttendee-label" class="property-label"><g:message code="refPhase.canDeleteAttendee.label" default="Can Delete Attendee" /></span>
					<span class="property-value" aria-labelledby="canDeleteAttendee-label"><g:formatBoolean boolean="${refPhaseInstance?.canDeleteAttendee}" /></span>
				</li>

				<li class="fieldcontain">
					<span id="canManageAttendee-label" class="property-label"><g:message code="refPhase.canManageAttendee.label" default="Can Manage Attendee" /></span>
					<span class="property-value" aria-labelledby="canManageAttendee-label"><g:formatBoolean boolean="${refPhaseInstance?.canManageAttendee}" /></span>
				</li>
			</ol>

			<g:form url="[resource:refPhaseInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${refPhaseInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <sec:ifAnyGranted roles="ROLE_CREATOR">
                        <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>

		</div>
	</body>
</html>
