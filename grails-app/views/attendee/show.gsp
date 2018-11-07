<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.Attendee" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'attendee.label', default: 'Attendee')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-attendee" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-attendee" class="content scaffold-show" role="main">
            <g:render template="statusBlock"/>

            <div class="attendeeInfo">
                <h1><span><g:img dir="images/icons" file="user_512.png" height="48" width="48" alt="Attendee"/></span>&nbsp;Show Attendee</h1>
            </div>

			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <ol class="property-list attendee">
                <div class="account-block-blue">
                    <g:if test="${attendeeInstance?.accountLink?.employeeType?.equalsIgnoreCase("Civilian")}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-blue">${attendeeInstance?.accountLink?.employeeType}</div>
                        </div>
                    </g:if>
                    <g:if test="${attendeeInstance?.accountLink?.employeeType?.equalsIgnoreCase("Military")}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-purple">${attendeeInstance?.accountLink?.employeeType}</div>
                        </div>
                    </g:if>
                    <g:if test="${attendeeInstance?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || attendeeInstance?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-red">CTR</div>
                        </div>
                    </g:if>

                    <li class="fieldcontain">
                        <span id="attendanceType-label" class="property-label"><g:message code="attendee.attendanceType.label" default="Attendance Type" /></span>
                        <span class="property-value" aria-labelledby="attendanceType-label"><b>${attendeeInstance?.attendanceType}</b></span>
                    </li>

                    <li class="fieldcontain">
                        <span id="accountLink-label" class="property-label"><g:message code="attendee.accountLink.label" default="Account" /></span>
                        <span class="property-value" aria-labelledby="accountLink-label">
                            <g:link controller="account" action="show" id="${attendeeInstance?.accountLink?.id}">${attendeeInstance?.accountLink?.encodeAsHTML()}</g:link>
                        </span>
                    </li>

                    <g:if test="${attendeeInstance?.name}">
                    <li class="fieldcontain">
                        <span id="nameImport-label" class="property-label"><g:message code="attendee.nameImport.label" default="Name (Import)" /></span>
                        <span class="property-value" aria-labelledby="nameImport-label">
                            ${attendeeInstance?.name}
                        </span>
                    </li>
                    </g:if>

                    <g:if test="${attendeeInstance?.accountLink?.rank && attendeeInstance?.accountLink?.employeeType?.equalsIgnoreCase('Military')}">
                    <li class="fieldcontain">
                        <span id="rank-label" class="property-label"><g:message code="attendee.rank.label" default="Rank" /></span>
                        <span class="property-value" aria-labelledby="rank-label"><g:fieldValue bean="${attendeeInstance?.accountLink}" field="rank"/></span>
                    </li>
                    </g:if>

                    <g:if test="${attendeeInstance?.accountLink?.grade && attendeeInstance?.accountLink?.employeeType?.equalsIgnoreCase('Civilian')}">
                    <li class="fieldcontain">
                        <span id="grade-label" class="property-label"><g:message code="attendee.grade.label" default="Grade" /></span>
                        <span class="property-value" aria-labelledby="grade-label"><g:fieldValue bean="${attendeeInstance?.accountLink}" field="grade"/></span>
                    </li>
                    </g:if>

                    <g:if test="${attendeeInstance?.supervisor}">
                    <li class="fieldcontain">
                        <span id="supervisor-label" class="property-label"><g:message code="attendee.supervisor.label" default="Supervisor" /></span>
                        <span class="property-value" aria-labelledby="supervisor-label"><g:link controller="account" action="show" id="${attendeeInstance?.supervisor?.id}">${attendeeInstance?.supervisor?.encodeAsHTML()}</g:link></span>
                    </li>
                    </g:if>

                    <g:if test="${attendeeInstance?.lastChangeDate}">
                    <li class="fieldcontain">
                        <span id="lastChangeDate-label" class="property-label"><g:message code="attendeeInstance.lastChangeDate.label" default="Last Change" /></span>
                        <span class="property-value" aria-labelledby="lastChangeDate-label">
                            <g:formatDate date="${attendeeInstance?.lastChangeDate}" type="date" style="MEDIUM"/>
                            <span style="color:#999999"><i>(${attendeeInstance?.getChangeDays()} days)</i></span>
                            <g:if test="${attendeeInstance?.lastChange}">
                                &nbsp;-&nbsp;<span style="color:#006dba"><i>${attendeeInstance?.lastChange}</i></span>
                            </g:if>
                        </span>
                    </li>
                    </g:if>
                </div>

                <br/>
                <div class="account-block">
                    <li class="fieldcontain">
                        <span id="dates-label" class="property-label"><g:message code="conference.travelDates.label" default="Travel Dates" /></span>
                        <span class="property-value" aria-labelledby="dates-label">
                            <g:formatDate date="${attendeeInstance?.startTravelDate}" type="date" dateStyle="MEDIUM"/>
                            &nbsp;<span style="color:#777777">to</span>&nbsp;
                            <g:formatDate date="${attendeeInstance?.endTravelDate}" type="date" dateStyle="MEDIUM"/>
                            &nbsp;<span style="color:#777777"><i>${attendeeInstance?.travelDays()} day(s)</i></span>
                        </span>
                    </li>

                    <g:if test="${attendeeInstance?.mealsIncluded}">
                    <li class="fieldcontain">
                        <span id="mealsIncluded-label" class="property-label"><g:message code="attendee.mealsIncluded.label" default="Meals Included" /></span>
                        <span class="property-value" aria-labelledby="mealsIncluded-label"><g:fieldValue bean="${attendeeInstance}" field="mealsIncluded"/></span>
                    </li>
                    </g:if>
                </div>
			
				<g:if test="${attendeeInstance?.justification}">
                <br/>
                <div class="account-block">
                    <li class="fieldcontain">
                        <span id="justification-label" class="property-label"><g:message code="attendee.justification.label" default="Justification" /></span>
                        <span class="property-value" aria-labelledby="justification-label">${attendeeInstance.justification}</span>
                    </li>
                </div>
				</g:if>

                %{--<br/>--}%
                %{--<div class="account-block">--}%
                    %{--<li class="fieldcontain">--}%
                        %{--<span id="fundSource-label" class="property-label">--}%
                            %{--Funding Source(s)--}%
                        %{--</span>--}%
                        %{--<g:each in="${FundSource.findAllByAttendee(attendeeInstance)}" var="f">--}%
                            %{--<span class="property-value" aria-labelledby="fundSource-label">--}%
                                %{--<b>${f.percentage}%</b>&nbsp;&nbsp;${f.fundSource}--}%
                            %{--</span>--}%
                        %{--</g:each>--}%
                    %{--</li>--}%
                %{--</div>--}%

                %{--<g:if test="${!attendeeInstance?.status?.equalsIgnoreCase("Supervisor") && !attendeeInstance?.status?.equalsIgnoreCase("Requesting")}">--}%
                    <br/>
                    <div class="account-block emphasize">
                        <g:if test="${attendeeInstance?.costs}">
                            <li class="fieldcontain">
                                <span id="costs-label" class="property-label">
                                    <g:message code="attendee.costs.label" default="Costs" />
                                </span>
                                <g:each in="${attendeeInstance.costs}" var="c">
                                    <span class="property-value" aria-labelledby="costs-label">
                                        <g:if test="${c?.mealsExceedsPerdiem || c?.lodgingExceedsPerdiem }">
                                            <g:img dir="images" file="Icon_Warning_Red.png" height="16" width="16" alt="Warning"/>
                                        </g:if>
                                        <g:if test="${attendeeInstance?.conference?.canEstimate(attendeeInstance?.id)}">
                                            <g:link controller="cost" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link>
                                        </g:if>
                                        <g:else>
                                            ${c?.encodeAsHTML()}
                                        </g:else>
                                        <span style="color:#777777"><i>(<g:formatNumber number="${c?.total()}" type="currency" currencyCode="USD" maxFractionDigits="2" />)</i></span>
                                        &nbsp;&nbsp;
                                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
                                            <g:link controller="cost" action="deleteCost" id="${c?.id}" params="['attendee.id': attendeeInstance?.id]" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                                                <g:img dir="images" file="remove_16.png" alt="Remove Cost"/>
                                            </g:link>
                                        </sec:ifAnyGranted>
                                    </span>
                                </g:each>
                                <g:if test="${attendeeInstance?.status?.equalsIgnoreCase("Attended")}">
                                    <g:if test="${!attendeeInstance?.hasActual() && attendeeInstance?.conference?.canActual(attendeeInstance?.id)}">
                                        <span class="property-value" aria-labelledby="costs-label">
                                            <br/><g:link controller="cost" action="createActualCost" params="['attendee.id': attendeeInstance?.id, 'costType': 'Actual']">${message(code: 'default.add.label', args: [message(code: 'actualCost.label', default: 'Actual Costs')])}</g:link>
                                        </span>
                                    </g:if>
                                </g:if>
                            </li>
                        </g:if>
                        <g:else>
                            <li class="fieldcontain">
                                <span id="estimateCostsMissing-label" class="property-label"><g:message code="attendee.costs.label" default="Costs" /></span>
                                <span class="property-value" aria-labelledby="estimateCostsMissing-label">
                                    <g:if test="${attendeeInstance?.conference?.canEstimate(attendeeInstance?.id)}">
                                        <g:link controller="cost" action="createEstimateCost" params="['attendee.id': attendeeInstance?.id, 'costType': 'Estimate']">${message(code: 'default.add.label', args: [message(code: 'estimateCost.label', default: 'Estimate Costs')])}</g:link>
                                    </g:if>
                                </span>
                            </li>
                        </g:else>
                    </div>
                %{--</g:if>--}%

                <br/>
                <div class="account-block">
                    <li class="fieldcontain">
                        <span id="files-label" class="property-label">
                            Date Events <g:img dir="/images/icons" file="Book_5-512.png" height="16" width="16" alt="Date events pertaining to the conference" title="Date events pertaining to the conference"/>
                        </span>
                        <g:each in="${attendeeInstance.getDateEvents()}" var="d">
                            <span class="property-value" aria-labelledby="files-label">
                                <g:if test="${d?.dateGate?.onDisplay}">
                                    <b><i>${d?.dateGate?.toString()}</i></b>&nbsp; (<g:formatDate date="${d?.eventDate}" type="date" dateStyle="Full"/>)&nbsp;-&nbsp;${d?.shortRecordedBy()}
                                    <g:if test="${d?.dateGate?.canDelete}">
                                        <g:link action="deleteDateEvent" id="${d?.id}" absolute="true" params="[conferenceId: "${conferenceInstance.id}"]" onclick="return confirm('${message(code: 'default.button.deletedateevent.confirm.message', default: 'Are you sure you want to delete this Date Event?')}');"><g:img dir="/images" file="reject_16n.png" alt="Delete date event" title="Delete date event"/></g:link>
                                    </g:if>
                                </g:if>
                            </span>
                        </g:each>
                    </li>
                </div>
			</ol>

			<g:form url="[resource:attendeeInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
                    <g:if test="${attendeeInstance?.conference?.canAttendee("edit", attendeeInstance?.id)}">
					    <g:link class="edit" action="edit" resource="${attendeeInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    </g:if>

                    <g:each in="${attendeeInstance?.availableActions()}" var="a">
                        %{--${a?.actionPermission}--}%
                        <sec:ifAnyGranted roles="${a?.actionPermission}">
                            <g:if test="${attendeeInstance?.checkPermission(a?.attendeeAction, a?.actionPermission)}">
                                <g:link class="paction" action="${a?.actionCommand}" resource="${attendeeInstance}" params="[refId: "${a.id}"]">${a?.attendeeAction}</g:link>
                            </g:if>
                        </sec:ifAnyGranted>
                        <sec:ifNotGranted roles="${a?.actionPermission}">
                            <g:if test="${a?.actionPermission?.contains("ROLE_CAO") && attendeeInstance?.conference?.isCAO()}">
                                <g:if test="${attendeeInstance?.checkPermission(a?.attendeeAction, a?.actionPermission)}">
                                    <g:link class="paction" action="${a?.actionCommand}" resource="${attendeeInstance}" params="[refId: "${a.id}"]">${a?.attendeeAction}</g:link>
                                </g:if>
                            </g:if>
                        </sec:ifNotGranted>
                    </g:each>
				</fieldset>
			</g:form>
		</div>

        <script type="text/javascript">
            $(document).ready(function(){
                $( document ).tooltip();
            })
        </script>

	</body>
</html>
