
<li class="fieldcontain">
	%{--<span id="attendees-label" class="property-label">--}%
        %{--<g:message code="conference.attendees.label" default="Attendees" />--}%
        %{--<span style="color:#777777">&nbsp;(${conferenceInstance?.attendees?.size()})</span>--}%
    %{--</span>--}%
    %{--<span class="property-value" aria-labelledby="attendees-label">--}%
        <table>
            %{--<g:each in="${conferenceInstance.attendees.sort { it.sequence }}" status="i" var="a">--}%
            <g:each in="${attendeeList}" status="i" var="a">
                <g:if test="${conferenceInstance.canAttendee("view", a?.id)}">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                        <td width="15%">
                            <g:if test="${a?.attendanceType?.equalsIgnoreCase('Attendee')}">
                                <g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">
                                    <g:link controller="attendee" action="toggle" id="${a?.id}"><g:img style="vertical-align: middle" dir="/images" file="Letter-A-orange-icon.png" width="24" height="24" alt="Attendee" title="Attendee"/></g:link>
                                </g:if>
                                <g:else>
                                    <g:img style="vertical-align: middle" dir="/images" file="Letter-A-orange-icon.png" width="24" height="24" alt="Attendee" title="Attendee"/>
                                </g:else>
                            </g:if>
                            <g:if test="${a?.attendanceType?.equalsIgnoreCase('Booth/Display')}">
                                <g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">
                                    <g:link controller="attendee" action="toggle" id="${a?.id}"><g:img style="vertical-align: middle" dir="/images" file="Letter-B-blue-icon.png" width="24" height="24" alt="Booth/Display" title="Booth/Display"/></g:link>
                                </g:if>
                                <g:else>
                                    <g:img style="vertical-align: middle" dir="/images" file="Letter-B-blue-icon.png" width="24" height="24" alt="Booth/Display" title="Booth/Display"/>
                                </g:else>
                            </g:if>
                            <g:if test="${a?.attendanceType?.equalsIgnoreCase('Discussion Panel')}">
                                <g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">
                                    <g:link controller="attendee" action="toggle" id="${a?.id}"><g:img style="vertical-align: middle" dir="/images" file="Letter-D-grey-icon.png" width="24" height="24" alt="Discussion Panel" title="Discussion Panel"/></g:link>
                                </g:if>
                                <g:else>
                                    <g:img style="vertical-align: middle" dir="/images" file="Letter-D-grey-icon.png" width="24" height="24" alt="Discussion Panel" title="Discussion Panel"/>
                                </g:else>
                            </g:if>
                            <g:if test="${a?.attendanceType?.equalsIgnoreCase('Session Chair')}">
                                <g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">
                                    <g:link controller="attendee" action="toggle" id="${a?.id}"><g:img style="vertical-align: middle" dir="/images" file="Letter-C-lg-icon.png" width="24" height="24" alt="Session Chair" title="Session Chair"/></g:link>
                                </g:if>
                                <g:else>
                                    <g:img style="vertical-align: middle" dir="/images" file="Letter-C-lg-icon.png" width="24" height="24" alt="Session Chair" title="Session Chair"/>
                                </g:else>
                            </g:if>
                            <g:if test="${a?.attendanceType?.equalsIgnoreCase('Presenter/Speaker')}">
                                <g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">
                                    <g:link controller="attendee" action="toggle" id="${a?.id}"><g:img style="vertical-align: middle" dir="/images" file="Letter-P-dg-icon.png" width="24" height="24" alt="Presenter/Speaker" title="Presenter/Speaker"/></g:link>
                                </g:if>
                                <g:else>
                                    <g:img style="vertical-align: middle" dir="/images" file="Letter-P-dg-icon.png" width="24" height="24" alt="Presenter/Speaker" title="Presenter/Speaker"/>
                                </g:else>
                            </g:if>
                            <g:if test="${a?.attendanceType?.equalsIgnoreCase('Support')}">
                                <g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">
                                    <g:link controller="attendee" action="toggle" id="${a?.id}"><g:img style="vertical-align: middle" dir="/images" file="Letter-S-red-icon.png" width="24" height="24" alt="Support" title="Support"/></g:link>
                                </g:if>
                                <g:else>
                                    <g:img style="vertical-align: middle" dir="/images" file="Letter-S-red-icon.png" width="24" height="24" alt="Support" title="Support"/>
                                </g:else>
                            </g:if>
                            <g:if test="${a?.attendanceType?.equalsIgnoreCase('Other')}">
                                <g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">
                                    <g:link controller="attendee" action="toggle" id="${a?.id}"><g:img style="vertical-align: middle" dir="/images" file="Letter-O-violet-icon.png" width="24" height="24" alt="Other" title="Other"/></g:link>
                                </g:if>
                                <g:else>
                                    <g:img style="vertical-align: middle" dir="/images" file="Letter-O-violet-icon.png" width="24" height="24" alt="Other" title="Other"/>
                                </g:else>
                            </g:if>

                            <g:if test="${a?.reservedTD}"><b>${a?.reservedTD?.officeSymbol}</b></g:if>
                            <g:elseif test="${a?.reservedOrg}"><span alt="${a?.reservedOrg}" title="${a?.reservedOrg}"><b>EXT</b></span></g:elseif>

                            %{--<span style="color:#777777"><i>${a?.attendanceType}</i></span>--}%
                            %{--<g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">--}%
                                %{--<g:link controller="attendee" action="toggle" id="${a?.id}"><g:img dir="images/icons" file="idea-user_512.png" height="16" width="16" alt="Toggle Attendance Type" title="Toggle Attendance Type"/></g:link>--}%
                            %{--</g:if>--}%
                        </td>
                        <td width="30%" class="no_underline">
                            <g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">
                                <g:if test="${a?.encodeAsHTML()?.equalsIgnoreCase("TBD") && a?.reservedOrg}">
                                    <g:if test="${a?.name}">
                                        <g:link controller="attendee" action="conferenceEdit" id="${a?.id}">${"EXT: "+a?.name}&nbsp;[${a?.rankGrade?.code}]</g:link>
                                    </g:if>
                                    <g:else>
                                        <g:link controller="attendee" action="conferenceEdit" id="${a?.id}">${a?.encodeAsHTML()}</g:link>
                                    </g:else>
                                </g:if>
                                <g:else>
                                    <g:link controller="attendee" action="conferenceEdit" id="${a?.id}">${a?.encodeAsHTML()}</g:link>
                                    <g:if test="${a?.accountLink?.emailAddress}">
                                        <a style="vertical-align: middle" href="mailto:${a?.accountLink?.emailAddress}"><g:img style="vertical-align: middle" dir="/images/icons" file="mail_512.png" width="24" height="24" alt="Send Mail to ${a?.accountLink?.emailAddress}" title="Send Mail to ${a?.accountLink?.emailAddress}"/></a>
                                    </g:if>
                                </g:else>
                            </g:if>
                            <g:else>
                                <g:if test="${a?.toString()?.equalsIgnoreCase('TBD')}">
                                    <g:if test="${a?.name}">
                                        ${"EXT: "+a?.name}&nbsp;[${a?.rankGrade?.code}]
                                    </g:if>
                                </g:if>
                                <g:else>
                                    ${a?.encodeAsHTML()}
                                </g:else>
                                <g:if test="${a?.accountLink?.emailAddress}">
                                    <a style="vertical-align: middle" href="mailto:${a?.accountLink?.emailAddress}"><g:img style="vertical-align: middle" dir="/images/icons" file="mail_512.png" width="24" height="24" alt="Send Mail to ${a?.accountLink?.emailAddress}" title="Send Mail to ${a?.accountLink?.emailAddress}"/></a>
                                </g:if>
                            </g:else>
                            <g:if test="${a?.extEmailAddress}">
                                <a style="vertical-align: middle" href="mailto:${a?.extEmailAddress}"><g:img style="vertical-align: middle" dir="/images/icons" file="ext_mail_512.png" width="24" height="24" alt="Send Mail to ${a?.extEmailAddress}" title="Send Mail to ${a?.extEmailAddress}"/></a>
                            </g:if>
                        </td>
                        <td width="10%">
                            <g:if test="${a?.status?.equalsIgnoreCase("Disapproved")}">
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="shutdown_box_red.png" width="24" height="24" alt="Disapproved" title="Disapproved"/>--}%
                                <span style="color:#FF0000"><b>${a?.status}</b></span>
                            </g:if>
                            %{--<g:elseif test="${a?.status?.equalsIgnoreCase("Supervisor")}">--}%
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="shield_yellow.png" width="24" height="24" alt="Supervisor" title="Supervisor"/>--}%
                            %{--</g:elseif>--}%
                            %{--<g:elseif test="${a?.status?.equalsIgnoreCase("TD Concurrence")}">--}%
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="shield_red.png" width="24" height="24" alt="TD Concurrence" title="TD Concurrence"/>--}%
                            %{--</g:elseif>--}%
                            %{--<g:elseif test="${a?.status?.equalsIgnoreCase("Pending")}">--}%
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="shield_green.png" width="24" height="24" alt="Pending" title="Pending"/>--}%
                            %{--</g:elseif>--}%
                            %{--<g:elseif test="${a?.status?.equalsIgnoreCase("Wait List")}">--}%
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="pause1.png" width="24" height="24" alt="Wait List" title="Wait List"/>--}%
                            %{--</g:elseif>--}%
                            %{--<g:elseif test="${a?.status?.equalsIgnoreCase("Approved")}">--}%
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="checked_shield_green.png" width="24" height="24" alt="Approved" title="Approved"/>--}%
                            %{--</g:elseif>--}%
                            <g:elseif test="${a?.status?.equalsIgnoreCase("Attended")}">
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="checked_shield_green.png" width="24" height="24" alt="Approved" title="Approved"/>--}%
                                <span style="color:#0000FF"><b>${a?.status}</b></span>
                            </g:elseif>
                            <g:elseif test="${a?.status?.equalsIgnoreCase("Registered")}">
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="checked_shield_green.png" width="24" height="24" alt="Approved" title="Approved"/>--}%
                                <span style="color:#009999"><b>${a?.status}</b></span>
                            </g:elseif>
                            <g:elseif test="${a?.status?.equalsIgnoreCase("Confirmed")}">
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="checked_shield_green.png" width="24" height="24" alt="Approved" title="Approved"/>--}%
                                <span style="color:#009999"><b>${a?.status}</b></span>
                            </g:elseif>
                            <g:elseif test="${a?.status?.equalsIgnoreCase("Withdrawn")}">
                                %{--<g:img style="vertical-align: middle" dir="/images/status" file="box_red.png" width="24" height="24" alt="Withdrawn" title="Withdrawn"/>--}%
                                <span style="color:#000000"><b>${a?.status}</b></span>
                            </g:elseif>
                            <g:else>
                                <g:if test="${a?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || a?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                                    <span style="color:#990000"><b>${a?.status}</b></span>
                                </g:if>
                                <g:else>
                                    <span style="color:#777777"><b>${a?.status}</b></span>
                                </g:else>
                            </g:else>
                        </td>
                        <td width="10%" class="no_underline">
                            <g:if test="${!a?.status?.equalsIgnoreCase("Withdrawn") && !a?.status?.equalsIgnoreCase("Disapproved")}">
                                <g:if test="${a?.hasEstimate() && conferenceInstance.canEstimate(a?.id)}">
                                    <g:if test="${a?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || a?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                                        <span style="color:#990000" alt="Estimate Total" title="Estimate Total"><i><g:link class="contractorCost" controller="cost" action="conferenceEdit" params="['id': a?.getCostId('Estimate')]"><g:formatNumber number="${a?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /><b>est</b></g:link></i></span>
                                    </g:if>
                                    <g:else>
                                        <span style="color:#777777" alt="Estimate Total" title="Estimate Total"><i><g:link class="cost" controller="cost" action="conferenceEdit" params="['id': a?.getCostId('Estimate')]"><g:formatNumber number="${a?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /><b>est</b></g:link></i></span>
                                    </g:else>
                                </g:if>
                                <g:else>
                                    %{--<g:if test="${!a?.status?.equalsIgnoreCase("Supervisor") && !a?.status?.equalsIgnoreCase("Requesting") && conferenceInstance.canEstimate(a?.id)}">--}%
                                    <g:if test="${conferenceInstance.canEstimate(a?.id)}">
                                        %{--<g:link controller="cost" action="createEstimateCost" params="['attendee.id': a?.id, 'costType': 'Estimate']"><span style="color:#0000FF"><g:img dir="images/icons" file="Money-1-512.png" height="16" width="16" alt="Add Estimate Costs" title="Add Estimate Costs"/>$0.00est</span></g:link>--}%
                                        <g:if test="${a?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || a?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                                            <g:link controller="cost" action="createEstimateCost" params="['attendee.id': a?.id, 'costType': 'Estimate']"><span style="color:#990000">$0.00est</span></g:link>
                                        </g:if>
                                        <g:else>
                                            <g:link controller="cost" action="createEstimateCost" params="['attendee.id': a?.id, 'costType': 'Estimate']"><span style="color:#003296">$0.00est</span></g:link>
                                        </g:else>
                                    </g:if>
                                    <g:else>
                                        <g:if test="${a?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || a?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                                            <g:if test="${a?.hasEstimate()}">
                                                <span style="color:#990000" alt="Estimate Total" title="Estimate Total"><i><g:formatNumber number="${a?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /><b>est</b></i></span>
                                            </g:if>
                                            <g:else>
                                                <span style="color:#990000" alt="Estimate Total" title="Estimate Total"><i>$0.00<b>est</b></i></span>
                                            </g:else>
                                        </g:if>
                                        <g:else>
                                            <g:if test="${a?.hasEstimate()}">
                                                <span style="color:#777777" alt="Estimate Total" title="Estimate Total"><i><g:formatNumber number="${a?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /><b>est</b></i></span>
                                            </g:if>
                                            <g:else>
                                                <span style="color:#777777" alt="Estimate Total" title="Estimate Total"><i>$0.00<b>est</b></i></span>
                                            </g:else>
                                        </g:else>
                                    </g:else>
                                </g:else>
                            </g:if>
                        </td>
                        <td width="15%">
                            <g:if test="${!a?.status?.equalsIgnoreCase("Withdrawn") && !a?.status?.equalsIgnoreCase("Disapproved")}">
                                <g:if test="${a?.hasActual() && conferenceInstance.canActual(a?.id)}">
                                    <span style="color:#333333" alt="Actual Total" title="Actual Total"><i><g:link class="cost" controller="cost" action="conferenceEdit" params="['id': a?.getCostId('Actual')]"><g:formatNumber number="${a?.actualTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /><b>act</b></g:link></i></span>
                                </g:if>
                                <g:elseif test="${a?.status?.equalsIgnoreCase("Attended")}">
                                    <g:if test="${conferenceInstance.canActual(a?.id)}">
                                        %{--<g:link controller="cost" action="createActualCost" params="['attendee.id': a?.id, 'costType': 'Actual']"><span style="color:#0000FF"><g:img dir="images/icons" file="Money-1-512.png" height="16" width="16" alt="Add Actual Costs" title="Add Actual Costs"/></span></g:link>--}%
                                        <g:link controller="cost" action="createActualCost" params="['attendee.id': a?.id, 'costType': 'Actual']"><span style="color:#0000FF">$0.00act</span></g:link>
                                    </g:if>
                                    <g:else>
                                        <g:if test="${a?.accountLink?.employeeType?.equalsIgnoreCase("Contractor") || a?.rankGrade?.code?.equalsIgnoreCase("CTR")}">
                                            <span style="color:#FF0000" alt="Actual Total" title="Actual Total"><i><g:formatNumber number="${a?.actualTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /><b>act</b></i></span>
                                        </g:if>
                                        <g:else>
                                            <span style="color:#333333" alt="Actual Total" title="Actual Total"><i><g:formatNumber number="${a?.actualTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /><b>act</b></i></span>
                                        </g:else>
                                    </g:else>
                                </g:elseif>
                            </g:if>
                        </td>
                        <td width="10%">
                            <g:if test="${!a?.status?.equalsIgnoreCase("Withdrawn") && !a?.status?.equalsIgnoreCase("Disapproved")}">
                                <g:if test="${a?.hasEstimate() && a?.hasActual() && a?.actualTotal() && a?.estimateTotal()}">
                                    <span style="color:#AAAAAA"><i>Diff: <g:formatNumber number="${a?.actualTotal() - a?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></i></span>
                                </g:if>
                            </g:if>
                        </td>
                        <td width="25%">
                            <g:if test="${conferenceInstance.canAttendee("delete", a?.id)}">
                                <g:if test="${a?.status?.equalsIgnoreCase("Supervisor") || a?.status?.equalsIgnoreCase("TD Concurrence") || a?.status?.equalsIgnoreCase("Pending") || a?.status?.equalsIgnoreCase("Wait List") && conferenceInstance?.canAttendee("delete", a?.id)}">
                                    <g:link controller="attendee" action="deleteAttendee" id="${a?.id}" params="['conference.id': conferenceInstance?.id]" onclick="return confirm('Are you sure you wish to remove attendee [ ${a?.encodeAsHTML()} ]?');"><g:img dir="images" file="reject_16n.png" alt="Remove Attendee" title="Remove Attendee"/></g:link>
                                </g:if>
                            </g:if>
                            <g:else>
                                <g:img dir="images" file="reject_blank_16n.png" height="16" width="16" alt="" title=""/>
                            </g:else>

                            <g:if test="${conferenceInstance.canAttendee("edit", a?.id)}">
                                <g:if test="${a?.status?.equalsIgnoreCase("Pending") || a?.status?.equalsIgnoreCase("Wait List") || a?.status?.equalsIgnoreCase("Approved") && conferenceInstance?.canAttendee("add", a?.id)}">
                                    <g:link controller="attendee" action="duplicateAttendee" id="${a?.id}" params="['conference.id': conferenceInstance?.id]"><g:img dir="images/icons" file="reload_512.png" height="16" width="16" alt="Duplicate Attendee" title="Duplicate Attendee"/></g:link>
                                </g:if>
                            </g:if>
                            <g:else>
                                <g:img dir="images" file="reject_blank_16n.png" height="16" width="16" alt="" title=""/>
                            </g:else>

                            %{--<sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_CAO">--}%
                                %{--<g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("External") || conferenceInstance?.phaseState?.equalsIgnoreCase("Create Package") || conferenceInstance?.phaseState?.equalsIgnoreCase("TD Review") || conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">--}%
                                    %{--<g:if test="${a?.sequence > 1}">--}%
                                        %{--<g:link action="upAttendee" resource="${conferenceInstance}" params="[attendeeId: "${a?.id}"]"><g:img dir="images/icons" file="Up-512.png" height="16" width="16" alt="Move Attendee up priority list" title="Move Attendee up priority list"/></g:link>--}%
                                    %{--</g:if>--}%
                                    %{--<g:else>--}%
                                        %{--<g:img dir="images/icons" file="Up-blank-512.png" height="16" width="16" alt="" title=""/>--}%
                                    %{--</g:else>--}%
                                    %{--<g:if test="${a?.sequence != conferenceInstance?.attendees?.size()}">--}%
                                        %{--<g:link action="downAttendee" resource="${conferenceInstance}" params="[attendeeId: "${a?.id}"]"><g:img dir="images/icons" file="Down-512.png" height="16" width="16" alt="Move Attendee down priority list" title="Move Attendee down priority list"/></g:link>--}%
                                    %{--</g:if>--}%
                                    %{--<g:else>--}%
                                        %{--<g:img dir="images/icons" file="Down-blank-512.png" height="16" width="16" alt="" title=""/>--}%
                                    %{--</g:else>--}%
                                %{--</g:if>--}%
                            %{--</sec:ifAnyGranted>--}%
                            %{--<sec:ifNotGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_CAO">--}%
                                %{--<g:if test="${conferenceInstance?.isCAO()}">--}%
                                    %{--<g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("External") || conferenceInstance?.phaseState?.equalsIgnoreCase("Create Package") || conferenceInstance?.phaseState?.equalsIgnoreCase("TD Review") || conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">--}%
                                        %{--<g:if test="${a?.sequence > 1}">--}%
                                            %{--<g:link action="upAttendee" resource="${conferenceInstance}" params="[attendeeId: "${a?.id}"]"><g:img dir="images/icons" file="Up-512.png" height="16" width="16" alt="Move Attendee up priority list" title="Move Attendee up priority list"/></g:link>--}%
                                        %{--</g:if>--}%
                                        %{--<g:else>--}%
                                            %{--<g:img dir="images/icons" file="Up-blank-512.png" height="16" width="16" alt="" title=""/>--}%
                                        %{--</g:else>--}%
                                        %{--<g:if test="${a?.sequence != conferenceInstance?.attendees?.size()}">--}%
                                            %{--<g:link action="downAttendee" resource="${conferenceInstance}" params="[attendeeId: "${a?.id}"]"><g:img dir="images/icons" file="Down-512.png" height="16" width="16" alt="Move Attendee down priority list" title="Move Attendee down priority list"/></g:link>--}%
                                        %{--</g:if>--}%
                                        %{--<g:else>--}%
                                            %{--<g:img dir="images/icons" file="Down-blank-512.png" height="16" width="16" alt="" title=""/>--}%
                                        %{--</g:else>--}%
                                    %{--</g:if>--}%
                                %{--</g:if>--}%
                            %{--</sec:ifNotGranted>--}%

                            <sec:ifAnyGranted roles="ROLE_SEQUENCE">
                                ${a?.sequence}
                            </sec:ifAnyGranted>
                        </td>
                    </tr>
                </g:if>
                %{--<g:else>--}%
                    %{--<tr>--}%
                        %{--<td><center>No attendee information available.</center></td>--}%
                    %{--</tr>--}%
                %{--</g:else>--}%
            </g:each>
        </table>
    %{--</span>--}%

    %{--<g:if test="${attendeeListCount > 10}">--}%
        <div class="pagination">
            <g:paginate total="${attendeeListCount ?: 0}" action="show" id="${conferenceInstance?.id}"/>
            <g:if test="${conferenceInstance.canManage()}">
                <g:if test="${conferenceInstance?.attendees?.size()>1}">
                    <g:link class="edit" action="manage" resource="${conferenceInstance}">Manage</g:link>
                </g:if>
                <!--[if (gt IE 8)|!(IE)]><!-->
                <g:if test="${conferenceInstance?.attendees?.size()>1}">
                    <g:link class="edit" action="manageCosts" resource="${conferenceInstance}">Manage Costs</g:link>
                </g:if>
                <!--<![endif]-->
            </g:if>
        </div>
    %{--</g:if>--}%

    %{--<export:formats formats="['csv', 'excel']" params="[id: "${conferenceInstance?.id}"]"/>--}%
    %{--<export:formats formats="['csv', 'excel']" params="[conferenceId: "${conferenceInstance?.id}"]"/>--}%

    %{--<export:formats controller="Conference" action="export" formats="['csv', 'excel']" params="[conferenceId: "${conferenceInstance?.id}"]"/>--}%
    <sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
        <br/>
        <div><center>
            <g:link controller="Conference" action="exportAttendees" params="[conferenceId: "${conferenceInstance?.id}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Attendee Export to Excel" title="Attendee Export to Excel"/></g:link>
            <g:link controller="Conference" action="exportSummary" params="[conferenceId: "${conferenceInstance?.id}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="excel.png" height="48" width="48" alt="Baseline Export" title="Baseline Export"/></g:link>
        </center></div>
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
        <g:if test="${conferenceInstance?.isCAO()}">
            <br/>
            <div><center>
                <g:link controller="Conference" action="exportAttendees" params="[conferenceId: "${conferenceInstance?.id}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="xls.png" height="48" width="48" alt="Attendee Export to Excel" title="Attendee Export to Excel"/></g:link>
                <g:link controller="Conference" action="exportSummary" params="[conferenceId: "${conferenceInstance?.id}", formatType: "excel", extension: "xls"]"><g:img dir="images" file="excel.png" height="48" width="48" alt="Baseline Export" title="Baseline Export"/></g:link>
            </center></div>
        </g:if>
    </sec:ifNotGranted>
</li>
