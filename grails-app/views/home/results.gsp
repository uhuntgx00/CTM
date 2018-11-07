
<%@ page import="mil.ebs.ctm.Attendee; mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
		<title>
            <g:message code="default.list.label" args="[entityName]" />&nbsp;(${listType})
        </title>
        <g:javascript library="raphael"/>
        <g:render template="raphael"/>
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

            %{--<br/>--}%
            %{--<g:render template="topCharts"/>--}%
            %{--<br/>--}%

            <br/>
            <div id="raphael"></div>

            <g:form name="merge" controller="conference" action="merge" method="POST">

                <table>
                    <thead>
                        <tr>
                            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                <th>&nbsp;</th>
                            </sec:ifAnyGranted>
                            <th>Conference Title</th>
                            <th></th>
                            <th>Location</th>
                            <th>Status</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            <th>Attendees</th>
                            <th>Estimate</th>
                            <th>Actual</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${conferenceInstanceList}" status="i" var="conferenceInstance">
                        <g:if test="${!conferenceInstance?.hide || !(conferenceInstance?.displayAfter > new Date())}">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                    <td><g:checkBox name="cSelect_${conferenceInstance?.id}" id="cSelect_${conferenceInstance?.id}"/></td>
                                </sec:ifAnyGranted>
                                <td class="no_underline">
                                    <g:if test="${!conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">
                                        <g:img dir="images/icons" file="lock_512.png" height="24" width="24"/>
                                    </g:if>
                                    <g:link action="show" controller="conference" id="${conferenceInstance.id}" alt="${conferenceInstance?.status}" title="${conferenceInstance?.status}">${fieldValue(bean: conferenceInstance, field: "conferenceTitle")}</g:link></td>
                                <td>
                                    <g:if test="${conferenceInstance?.checkAction('isNotAttending') && conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">
                                        <span style="clear: right; float: right; padding: 0; margin: 0; margin-right: 25px" class="buttons">
                                            <g:link class="padd" action="attendConference" resource="${conferenceInstance}" params="[refId: "${attendState?.id}"]">Attend</g:link>
                                        </span>
                                    </g:if>
                                    <g:else>
                                        <g:if test="${account}">
                                            <g:each in="${conferenceInstance?.attendees}" var="attendeeInstance">
                                                <g:if test="${attendeeInstance?.accountLink == account}">
                                                    <span style="clear: right; float: right; padding: 0; margin: 0; margin-right: 25px" class="buttons">
                                                        <g:link action="show" controller="attendee" id="${attendeeInstance?.id}">${attendeeInstance?.status}</g:link>
                                                    </span>
                                                </g:if>
                                            </g:each>
                                        </g:if>
                                    </g:else>
                                </td>
                                <td>${conferenceInstance?.address?.city}&nbsp;${conferenceInstance?.address?.state}<g:if test="${conferenceInstance?.address?.country}">,&nbsp;<g:country code="${conferenceInstance?.address?.country}"/></g:if></td>
                                <td>${conferenceInstance?.phaseState}<g:if test="${conferenceInstance?.step && conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">&nbsp;<i>(${conferenceInstance?.step})</i></g:if></td>
                                <td><g:formatDate date="${conferenceInstance?.startDate}" type="Date" dateStyle="Long"/></td>
                                <td><g:formatDate date="${conferenceInstance?.endDate}" type="Date" dateStyle="Long"/></td>
                                <td>${conferenceInstance?.attendees?.size()}&nbsp;<span style="color:#999999">(<g:if test="${conferenceInstance?.numAttendees}">${conferenceInstance?.numAttendees}&nbsp;</g:if><g:else>~</g:else>)</span></td>
                                <td>
                                    <g:if test="${conferenceInstance?.constrainedTotal() < 15000}">
                                        <g:img dir="images" file="green_threshold_warning.png" height="24" width="24" alt="Green Threshold"/>
                                    </g:if>
                                    <g:elseif test="${conferenceInstance?.constrainedTotal()< 20000}">
                                        <g:img dir="images" file="yellow_threshold_warning.png" height="24" width="24" alt="Yellow Threshold"/>
                                    </g:elseif>
                                    <g:else>
                                        <g:img dir="images" file="red_threshold_warning.png" height="24" width="24" alt="Red Threshold"/>
                                    </g:else>
                                    <g:formatNumber number="${conferenceInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                                </td>
                                <td><g:formatNumber number="${conferenceInstance?.actualTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></td>
                            </tr>
                        </g:if>
                    </g:each>
                    </tbody>
                </table>

                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                    <g:if test="${conferenceInstanceCount > 1}">
                        <fieldset class="buttons">
                            <g:submitButton name="Merge" class="edit"/>
                            %{--<g:link controller="conference" class="edit" action="merge" resource="${conferenceInstance}"><g:message code="default.button.merge.label" default="Merge" /></g:link>--}%
                        </fieldset>
                        <br/>
                    </g:if>
                </sec:ifAnyGranted>

            </g:form>

            <g:if test="${confernenceInstanceCount > 25}">
                <div class="pagination">
                    <g:paginate total="${conferenceInstanceCount ?: 0}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
