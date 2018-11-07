<div id="status_block" role="complementary">
    <h1>Information</h1>
    <ul>
        <li>Date Created: <span><b><g:formatDate date="${attendeeInstance?.createdDate}" type="date" style="MEDIUM"/></b></span></li>
        <li>&nbsp;</li>
        %{--<li>Hours: <span><b>${attendeeInstance?.hoursAttendanceType}</b></span></li>--}%
        %{--<li>Funding Source:</li>--}%
        <li>&nbsp;</li>
        <li>Estimate: <span><b><g:formatNumber number="${attendeeInstance?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>Actual: <span><b><g:if test="${attendeeInstance?.actualTotal()}"><g:formatNumber number="${attendeeInstance?.actualTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></g:if><g:else>$0.00</g:else></b></span></li>
        <li>&nbsp;</li>
        <li>&nbsp;</li>

        <h2 class="green_threshold">${attendeeInstance?.status}</h2>

        <g:if test="${attendeeInstance?.conference?.constrainedTotal()< 15000}">
            <h2 class="green_threshold"><g:link controller="conference" action="show" id="${attendeeInstance?.conference?.id}">${attendeeInstance?.conference?.encodeAsHTML()}</g:link></h2>
        </g:if>
        <g:elseif test="${attendeeInstance?.conference?.constrainedTotal()< 20000}">
            <h2 class="yellow_threshold"><g:link controller="conference" action="show" id="${attendeeInstance?.conference?.id}">${attendeeInstance?.conference?.encodeAsHTML()}</g:link></h2>
        </g:elseif>
        <g:else>
            <h2><g:link controller="conference" action="show" id="${attendeeInstance?.conference?.id}">${attendeeInstance?.conference?.encodeAsHTML()}</g:link></h2>
        </g:else>
    </ul>
</div>

