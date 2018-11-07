
<%@ page import="mil.ebs.ctm.ref.RefPhase" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'refPhase.label', default: 'RefPhase')}" />
		<title>Phase List</title>
	</head>
	<body>
		<a href="#list-refPhase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-refPhase" class="content scaffold-list" role="main">
			<h1>Phase List [${refPhaseInstanceCount}]</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
			    <thead>
					<tr>
						<g:sortableColumn property="phase" title="${message(code: 'refPhase.phase.label', default: 'Phase')}" />
						<g:sortableColumn property="canUserEstimate" title="${message(code: 'refPhase.canUserEstimateShort.label', default: 'User $e')}" />
						<g:sortableColumn property="canUserActual" title="${message(code: 'refPhase.canUserActualShort.label', default: 'User $a')}" />
						<g:sortableColumn property="canCaoEstimate" title="${message(code: 'refPhase.canCaoEstimateShort.label', default: 'CAO $e')}" />
                        <g:sortableColumn property="canCaoActual" title="${message(code: 'refPhase.canCaoActualShort.label', default: 'CAO $a')}" />
						<g:sortableColumn property="canTdEstimate" title="${message(code: 'refPhase.canTdEstimateShort.label', default: 'TD $e')}" />
                        <g:sortableColumn property="canTdActual" title="${message(code: 'refPhase.canTdActualShort.label', default: 'TD $a')}" />
						<g:sortableColumn property="canFmcEstimate" title="${message(code: 'refPhase.canFmcEstimateShort.label', default: 'FMC $e')}" />
                        <g:sortableColumn property="canFmcActual" title="${message(code: 'refPhase.canFmcActualShort.label', default: 'FMC $e')}" />
					</tr>
				</thead>
				<tbody>
                    <g:each in="${refPhaseInstanceList}" status="i" var="refPhaseInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td><g:link action="show" id="${refPhaseInstance.id}">${fieldValue(bean: refPhaseInstance, field: "phase")}</g:link></td>
                            <td><g:formatBoolean boolean="${refPhaseInstance.canUserEstimate}" /></td>
                            <td><g:formatBoolean boolean="${refPhaseInstance.canUserActual}" /></td>
                            <td><g:formatBoolean boolean="${refPhaseInstance.canCaoEstimate}" /></td>
                            <td><g:formatBoolean boolean="${refPhaseInstance.canCaoActual}" /></td>
                            <td><g:formatBoolean boolean="${refPhaseInstance.canTdEstimate}" /></td>
                            <td><g:formatBoolean boolean="${refPhaseInstance.canTdActual}" /></td>
                            <td><g:formatBoolean boolean="${refPhaseInstance.canFmcEstimate}" /></td>
                            <td><g:formatBoolean boolean="${refPhaseInstance.canFmcActual}" /></td>
                        </tr>
                    </g:each>
				</tbody>
			</table>

            <g:if test="${refPhaseInstanceCount > 20}">
                <div class="pagination">
                    <g:paginate total="${refPhaseInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
