
<%@ page import="mil.ebs.ctm.Attendee; mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
		<title>
            <g:message code="default.list.label" args="[entityName]" />&nbsp;(${listType})
        </title>
	</head>
	<body>
		<a href="#list-conference" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-conference" class="content scaffold-list" role="main">
			<h1>
                <g:img dir="/images/icons" file="calendar_512.png" height="32" width="32" alt="Conference List" title="Conference List"/>
                <g:message code="default.list.label" args="[entityName]" />&nbsp;(${listType})&nbsp;&nbsp;[${conferenceInstanceCount}]
            </h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:render template="topCharts"/>

            <table>
			    <thead>
					<tr>
                        <th>&nbsp;</th>
						<g:sortableColumn property="conferenceTitle" title="${message(code: 'conference.conferenceTitle.label', default: 'Conference Title')}" />
                        <th>Attendees</th>
                        <th>&nbsp;</th>
                        <th>Unconstrained</th>
                        <th>US Air Force</th>
                        <th>Other USG</th>
                        <th>Non-Federal</th>
                        <th>Total</th>
                        <th>&nbsp;</th>
                        <th>Actual</th>
                        <th>US Air Force</th>
                        <th>Other USG</th>
                        <th>Non-Federal</th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${conferenceInstanceList}" status="i" var="conferenceInstance">
                    <g:if test="${!conferenceInstance?.hide || !(conferenceInstance?.displayAfter > new Date())}">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td><g:checkBox name="conferenceSelect" id="cSelect_${conferenceInstance?.id}"/></td>
                            <td class="no_underline">
                                <g:if test="${!conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">
                                    <g:img dir="images/icons" file="lock_512.png" height="24" width="24"/>
                                </g:if>
                                <g:link action="show" id="${conferenceInstance.id}">${fieldValue(bean: conferenceInstance, field: "conferenceTitle")}</g:link></td>
                            <td>${conferenceInstance?.attendees?.size()}&nbsp;<span style="color:#999999">(<g:if test="${conferenceInstance?.numAttendees}">${conferenceInstance?.numAttendees}</g:if><g:else>~</g:else>)</span></td>
                            <td>|</td>
                            <td style="color:#000077"><b><g:formatNumber number="${conferenceInstance?.unconstrainedTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></td>
                            <td style="color:#000077">
                                <g:if test="${conferenceInstance?.unconstrainedTotalByFunding()?.get("US Air Force")}">
                                    <g:formatNumber number="${conferenceInstance?.unconstrainedTotalByFunding()?.get("US Air Force")}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                                </g:if>
                                <g:else>
                                    $0.00
                                </g:else>
                            </td>
                            <td style="color:#000077">
                                <g:if test="${conferenceInstance?.unconstrainedTotalByFunding()?.get("Other US Govt")}">
                                    <g:formatNumber number="${conferenceInstance?.unconstrainedTotalByFunding()?.get("Other US Govt")}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                                </g:if>
                                <g:else>
                                    $0.00
                                </g:else>
                            </td>
                            <td style="color:#000077">
                                <g:if test="${conferenceInstance?.unconstrainedTotalByFunding()?.get("Non-Federal Entity")}">
                                    <g:formatNumber number="${conferenceInstance?.unconstrainedTotalByFunding()?.get("Non-Federal Entity")}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                                </g:if>
                                <g:else>
                                    $0.00
                                </g:else>
                            </td>
                            <td><b><g:formatNumber number="${conferenceInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></td>
                            <td>|</td>
                            <td style="color:#007700"><b><g:formatNumber number="${conferenceInstance?.actualTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></td>
                            <td style="color:#007700">
                                <g:if test="${conferenceInstance?.actualTotalByFunding()?.get("US Air Force")}">
                                    <g:formatNumber number="${conferenceInstance?.actualTotalByFunding()?.get("US Air Force")}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                                </g:if>
                                <g:else>
                                    $0.00
                                </g:else>
                            </td>
                            <td style="color:#007700">
                                <g:if test="${conferenceInstance?.actualTotalByFunding()?.get("Other US Govt")}">
                                    <g:formatNumber number="${conferenceInstance?.actualTotalByFunding()?.get("Other US Govt")}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                                </g:if>
                                <g:else>
                                    $0.00
                                </g:else>
                            </td>
                            <td style="color:#007700">
                                <g:if test="${conferenceInstance?.actualTotalByFunding()?.get("Non-Federal Entity")}">
                                    <g:formatNumber number="${conferenceInstance?.actualTotalByFunding()?.get("Non-Federal Entity")}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                                </g:if>
                                <g:else>
                                    $0.00
                                </g:else>
                            </td>
                        </tr>
                    </g:if>
				</g:each>
				</tbody>
			</table>

            %{--<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">--}%
                %{--<g:if test="${conferenceInstanceCount > 1}">--}%
                    %{--<g:form url="[resource:conferenceInstance, action:'merge']" method="GET">--}%
                        %{--<fieldset class="buttons">--}%
                            %{--<g:link class="edit" action="merge" resource="${conferenceInstance}"><g:message code="default.button.merge.label" default="Merge" /></g:link>--}%
                        %{--</fieldset>--}%
                    %{--</g:form>--}%
                    %{--<br/>--}%
                %{--</g:if>--}%
            %{--</sec:ifAnyGranted>--}%

            <g:if test="${conferenceInstanceCount > 25}">
                <div class="pagination">
                    <g:paginate total="${conferenceInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
