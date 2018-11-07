
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
                <g:img dir="/images/icons" file="calendar_512.png" height="48" width="48" alt="Conference List" title="Conference List"/>
                <g:message code="default.list.label" args="[entityName]" />&nbsp;(${listType})&nbsp;[${conferenceInstanceCount}]
                <g:if test="${exportOption}">
                    <sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                        <g:link action="${listAction}" params="[id: "${params.id}", conferenceId: "${params.conferenceId}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Conference(s) Export to Excel" title="Conference(s) Export to Excel"/></g:link>
                    </sec:ifAnyGranted>
                </g:if>
            </h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

            %{--<br/>--}%
            %{--<g:render template="topCharts"/>--}%
            %{--<br/>--}%

        <!--[if (gt IE 8)|!(IE)]><!-->
            <br/>
            <div id="raphael"></div>
        <!--<![endif]-->

            <g:form name="merge" controller="conference" action="merge" method="POST">
                <table>
                    <thead>
                        <tr>
                            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                <th>&nbsp;</th>
                            </sec:ifAnyGranted>
                            <g:sortableColumn property="conferenceTitle" title="${message(code: 'conference.conferenceTitle.label', default: 'Conference Title')}" />
                            <g:if test="${attendState}">
                                <th></th>
                            </g:if>
                            <th>Location</th>
                            <g:sortableColumn property="phaseState" title="Phase" />
                            <g:sortableColumn property="startDate" title="${message(code: 'conference.startDate.label', default: 'Start Date')}" />
                            <g:sortableColumn property="endDate" title="${message(code: 'conference.endDate.label', default: 'End Date')}" />
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
                                    <g:link action="show" id="${conferenceInstance.id}" alt="${conferenceInstance?.status}" title="${conferenceInstance?.status}">${fieldValue(bean: conferenceInstance, field: "conferenceTitle")}</g:link></td>
                                <g:if test="${attendState}">
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
                                </g:if>
                                <td>${conferenceInstance?.address?.city}&nbsp;${conferenceInstance?.address?.state}<g:if test="${conferenceInstance?.address?.country}">,&nbsp;<g:country code="${conferenceInstance?.address?.country}"/></g:if></td>
                                <td>${conferenceInstance?.phaseState}<g:if test="${conferenceInstance?.step && conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">&nbsp;<i>(${conferenceInstance?.step})</i></g:if></td>
                                <td><g:formatDate date="${conferenceInstance.startDate}" type="Date" dateStyle="Long"/></td>
                                <td><g:formatDate date="${conferenceInstance.endDate}" type="Date" dateStyle="Long"/></td>
                                <td>${conferenceInstance?.attendees?.size()}&nbsp;<span style="color:#999999">(<g:if test="${conferenceInstance?.numAttendees}">${conferenceInstance?.numAttendees}</g:if><g:else>~</g:else>)</span></td>
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
                            %{--<g:link class="edit" action="merge" resource="${conferenceInstance}"><g:message code="default.button.merge.label" default="Merge" /></g:link>--}%
                        </fieldset>
                        <br/>
                    </g:if>
                </sec:ifAnyGranted>

            </g:form>

            <g:if test="${conferenceInstanceCount > 25}">
                <div class="pagination">
                    <g:paginate total="${conferenceInstanceCount ?: 0}" id="${params.id}" />
                </div>
            </g:if>
		</div>
	</body>
</html>
