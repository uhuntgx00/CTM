
<%@ page import="mil.ebs.ctm.ref.RefDateGate; mil.ebs.ctm.ref.RefPhaseState" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refPhaseState.label', default: 'RefPhaseState')}" />
		<title>Show Phase State</title>
	</head>
	<body>
		<a href="#show-refPhaseState" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-refPhaseState" class="content scaffold-show" role="main">
			<h1>Show Phase State</h1>

            <g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list refPhaseState">
				<g:if test="${refPhaseStateInstance?.phaseState}">
				<li class="fieldcontain">
					<span id="phaseState-label" class="property-label"><g:message code="refPhaseState.phaseState.label" default="Phase State" /></span>
					<span class="property-value" aria-labelledby="phaseState-label"><g:fieldValue bean="${refPhaseStateInstance}" field="phaseState"/></span>
				</li>
				</g:if>

                <g:if test="${refPhaseStateInstance?.phaseState}">
                <li class="fieldcontain">
                    <span id="nextPhaseState-label" class="property-label"><g:message code="refPhaseState.nextPhaseState.label" default="Next Phase State" /></span>
                    <span class="property-value" aria-labelledby="nextPhaseState-label"><g:fieldValue bean="${refPhaseStateInstance}" field="nextPhaseState"/></span>
                </li>
                </g:if>

				<g:if test="${refPhaseStateInstance?.phaseAction}">
				<li class="fieldcontain">
					<span id="phaseAction-label" class="property-label"><g:message code="refPhaseState.phaseAction.label" default="Phase Action" /></span>
					<span class="property-value" aria-labelledby="phaseAction-label"><g:fieldValue bean="${refPhaseStateInstance}" field="phaseAction"/></span>
				</li>
				</g:if>
			
                <g:if test="${refPhaseStateInstance?.buttonClass}">
                <li class="fieldcontain">
                    <span id="buttonClass-label" class="property-label"><g:message code="refPhaseState.buttonClass.label" default="Button Class" /></span>
                    <span class="property-value" aria-labelledby="buttonClass-label"><g:fieldValue bean="${refPhaseStateInstance}" field="buttonClass"/></span>
                </li>
                </g:if>

				<g:if test="${refPhaseStateInstance?.actionStatus}">
				<li class="fieldcontain">
					<span id="actionStatus-label" class="property-label"><g:message code="refPhaseState.actionStatus.label" default="Action Status" /></span>
					<span class="property-value" aria-labelledby="actionStatus-label"><g:fieldValue bean="${refPhaseStateInstance}" field="actionStatus"/></span>
				</li>
				</g:if>
			
                <g:if test="${refPhaseStateInstance?.actionController}">
                <li class="fieldcontain">
                    <span id="actionController-label" class="property-label"><g:message code="refPhaseState.actionController.label" default="Action Controller" /></span>
                    <span class="property-value" aria-labelledby="actionController-label"><g:fieldValue bean="${refPhaseStateInstance}" field="actionController"/></span>
                </li>
                </g:if>

				<g:if test="${refPhaseStateInstance?.actionCommand}">
				<li class="fieldcontain">
					<span id="actionCommand-label" class="property-label"><g:message code="refPhaseState.actionCommand.label" default="Action Command" /></span>
					<span class="property-value" aria-labelledby="actionCommand-label"><g:fieldValue bean="${refPhaseStateInstance}" field="actionCommand"/></span>
				</li>
				</g:if>
			
				<g:if test="${refPhaseStateInstance?.actionPermission}">
				<li class="fieldcontain">
					<span id="actionPermission-label" class="property-label"><g:message code="refPhaseState.actionPermission.label" default="Action Permission" /></span>
					<span class="property-value" aria-labelledby="actionPermission-label"><g:fieldValue bean="${refPhaseStateInstance}" field="actionPermission"/></span>
				</li>
				</g:if>
			
				<g:if test="${refPhaseStateInstance?.actionCheck}">
				<li class="fieldcontain">
					<span id="actionCheck-label" class="property-label"><g:message code="refPhaseState.actionCheck.label" default="Action Check" /></span>
					<span class="property-value" aria-labelledby="actionCheck-label"><g:fieldValue bean="${refPhaseStateInstance}" field="actionCheck"/></span>
				</li>
				</g:if>
			
				<g:if test="${refPhaseStateInstance?.actionNotification}">
				<li class="fieldcontain">
					<span id="actionNotification-label" class="property-label"><g:message code="refPhaseState.actionNotification.label" default="Action Notification" /></span>
					<span class="property-value" aria-labelledby="actionNotification-label"><g:fieldValue bean="${refPhaseStateInstance}" field="actionNotification"/></span>
				</li>
				</g:if>
			
				<g:if test="${refPhaseStateInstance?.dateGateEvent}">
				<li class="fieldcontain">
					<span id="dateGateEvent-label" class="property-label"><g:message code="refPhaseState.dateGateEvent.label" default="Date Gate Event" /></span>
					<span class="property-value" aria-labelledby="dateGateEvent-label">
                        <g:fieldValue bean="${refPhaseStateInstance}" field="dateGateEvent"/>
                        <i>(${mil.ebs.ctm.ref.RefDateGate.findByCode(refPhaseStateInstance?.dateGateEvent)})</i>
                    </span>
				</li>
				</g:if>

                <li class="fieldcontain">
                    <span id="displayDisabled-label" class="property-label"><g:message code="refPhaseState.displayDisabled.label" default="Display Disabled" /></span>
                    <span class="property-value" aria-labelledby="displayDisabled-label"><g:formatBoolean boolean="${refPhaseStateInstance?.displayDisabled}" /></span>
                </li>

                <li class="fieldcontain">
                    <span id="checkPermission-label" class="property-label"><g:message code="refPhaseState.checkPermission.label" default="Check Permission" /></span>
                    <span class="property-value" aria-labelledby="checkPermission-label"><g:formatBoolean boolean="${refPhaseStateInstance?.checkPermission}" /></span>
                </li>
			</ol>

			<g:form url="[resource:refPhaseStateInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${refPhaseStateInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <sec:ifAnyGranted roles="ROLE_DEVELOPER">
                        <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
