<div id="conference_status_block" role="complementary">
    <h1>Information</h1>
    <ul>
        <li>Date Created: <span><b><g:formatDate date="${conferenceInstance?.createdDate}" type="date" style="MEDIUM"/></b></span></li>
        <li>Changed: <span><b><g:formatDate date="${conferenceInstance?.lastChangeDate}" type="date" style="MEDIUM"/></b></span></li>
        <li>Status Change: <span><b><g:formatDate date="${conferenceInstance?.statusChangeDate}" type="date" style="MEDIUM"/></b></span></li>
        <li>Edits: <span><b>${conferenceInstance?.accountEdit}</b></span></li>
        <li>&nbsp;</li>
        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_DEVELOPER, ROLE_FMC_ADMIN">
            <g:if test="${csb?.numAttendees}">
                <li><g:link controller="Attendee" action="conferenceAttendeeList" id="${conferenceInstance?.id}" absolute="true">Attendee(s)</g:link>: <span><b>${csb?.attendeeCount} (${csb?.numAttendees})</b></span></li>
            </g:if>
            <g:else>
                <li><g:link controller="Attendee" action="conferenceAttendeeList" id="${conferenceInstance?.id}" absolute="true">Attendee(s)</g:link>: <span><b>${csb?.attendeeCount} (~)</b></span></li>
            </g:else>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_DEVELOPER, ROLE_FMC_ADMIN">
            <g:if test="${csb?.numAttendees}">
                <li>Attendee(s): <span><b>${csb?.attendeeCount} (${csb?.numAttendees})</b></span></li>
            </g:if>
            <g:else>
                <li>Attendee(s): <span><b>${csb?.attendeeCount} (~)</b></span></li>
            </g:else>
        </sec:ifNotGranted>
        <li>&nbsp;</li>
        <li>Comments: <span><b>${csb?.commentCount}</b></span></li>
        <li>Date Events: <span><b>${csb?.datesCount}</b></span></li>
        <li>File Uploads: <span><b>${csb?.fileCount}</b></span></li>
        <li>&nbsp;</li>
        <li>Total Days: <span><b>${csb?.days}</b></span></li>
        <li>&nbsp;</li>
        <li>Constrained&nbsp;<b>(${csb?.constrainedCount})</b>: <span><b><g:formatNumber number="${csb?.constrainedTotal}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>Unconstrained: <span><b><g:formatNumber number="${csb?.unconstrainedTotal}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>Other: <span><b><g:formatNumber number="${csb?.otherEstimateTotal}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>&nbsp;</li>
        <li>Actuals&nbsp;<b>(${csb?.constrainedActualCount})</b>: <span><b><g:formatNumber number="${csb?.constrainedActualTotal}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>CTR Actuals&nbsp;<b>(${csb?.contractorActualCount})</b>: <span><b><g:formatNumber number="${csb?.contractorActualTotal}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>&nbsp;</li>
        <g:if test="${csb?.tdList}">
            <li style="color:#000000"><b><center>TD CONSTRAINED</center></b></li>
            <g:each in="${csb?.tdList?.sort {it}}" var="td">
                %{--<g:if test="${td}">--}%
                    <li>${td}&nbsp;<b>(${csb?.tdCount?.get(td)})</b>: <span><b><g:formatNumber number="${csb?.tdTotal?.get(td)}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
                %{--</g:if>--}%
            </g:each>
            <li>&nbsp;</li>
        </g:if>
        <li>&nbsp;</li>
    </ul>

    <g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("External")}">
        <h2 class="yellow_threshold">${conferenceInstance?.phaseState}</h2>
    </g:if>
    <g:elseif test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Closed") || conferenceInstance?.phaseState?.equalsIgnoreCase("*ERROR*")}">
        <h2>${conferenceInstance?.phaseState}</h2>
    </g:elseif>
    <g:elseif test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Approved") || conferenceInstance?.phaseState?.equalsIgnoreCase("Finalizing")}">
        <h2 class="green_threshold">${conferenceInstance?.phaseState}</h2>
    </g:elseif>
    <g:else>
        <h2 class="blue_status">${conferenceInstance?.phaseState}</h2>
    </g:else>

    <g:if test="${conferenceInstance?.step && conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">
        <sec:ifAnyGranted roles="ROLE_FMC_ADMIN">
            <h2 class="yellow_threshold"><g:link action="toggleStep" id="${conferenceInstance?.id}">${conferenceInstance?.step}</g:link></h2>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_FMC_ADMIN">
            <h2 class="yellow_threshold">${conferenceInstance?.step}</h2>
        </sec:ifNotGranted>
    </g:if>

    <g:if test="${!conferenceInstance?.status?.equalsIgnoreCase(conferenceInstance?.phaseState)}">
        <g:if test="${conferenceInstance?.status?.equalsIgnoreCase("Archived")}">
            <h2 class="yellow_threshold">${conferenceInstance?.status}</h2>
        </g:if>
        <g:elseif test="${conferenceInstance?.status?.equalsIgnoreCase("Cancelled") || conferenceInstance?.status?.equalsIgnoreCase("Disapproved") || conferenceInstance?.status?.equalsIgnoreCase("*ERROR*")}">
            <h2>${conferenceInstance?.status}</h2>
        </g:elseif>
        <g:elseif test="${conferenceInstance?.status?.equalsIgnoreCase("Approved")}">
            <h2 class="green_threshold">${conferenceInstance?.status}</h2>
        </g:elseif>
        <g:else>
            <h2 class="blue_status">${conferenceInstance?.status}</h2>
        </g:else>
    </g:if>

    <br/>
    <g:if test="${csb?.constrainedTotal < 15000}">
        <center><g:img dir="images" file="green_threshold_warning.png" height="150" width="150" alt="GREEN THRESHOLD" title="GREEN THRESHOLD"/></center>
    </g:if>
    <g:elseif test="${csb?.constrainedTotal < 20000}">
        <center><g:img dir="images" file="yellow_threshold_warning.png" height="150" width="150" alt="YELLOW THRESHOLD" title="YELLOW THRESHOLD"/></center>
    </g:elseif>
    <g:else>
        <center><g:img dir="images" file="red_threshold_warning.png" height="150" width="150" alt="RED THRESHOLD" title="RED THRESHOLD"/></center>
    </g:else>
</div>
