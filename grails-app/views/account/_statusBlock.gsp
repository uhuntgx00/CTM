<div id="status_block" role="complementary">
    <h1>Information</h1>
    <ul>
        <li>Date Created: <span><b><g:formatDate date="${accountInstance?.createdDate}" type="date" style="MEDIUM"/></b></span></li>
        <li>Last Login: <span><b><g:formatDate date="${accountInstance?.lastLoginDate}" type="date" style="MEDIUM"/></b></span></li>
        <li>Last Expired: <span><b><g:formatDate date="${accountInstance?.lastExpirationDate}" type="date" style="MEDIUM"/></b></span></li>
        <li>&nbsp;</li>
        <li>Login Count: <span><b>${accountInstance?.loginCount}</b></span></li>
        <li>Edit Count: <span><b>${accountInstance?.accountEdit}</b></span></li>
        <li>&nbsp;</li>
        <li><g:link controller="Conference" action="conferenceCreatedList" params="[id: "${accountInstance?.id}"]" absolute="true">Conferences Created</g:link>: <span><b>${accountInstance?.getConferencesCreated()?.size()}</b></span></li>
        <li><g:link controller="Conference" action="conferenceList" params="[id: "${accountInstance?.id}"]" absolute="true">Conference</g:link> | <g:link controller="Attendee" action="accountAttendeeList" params="[acctId: "${accountInstance?.id}"]" absolute="true">Attendee</g:link>: <span><b>${accountInstance?.getConferencesAttendee()?.size()}</b></span></li>
        <li><g:link controller="Conference" action="conferenceCaoList" params="[id: "${accountInstance?.id}"]" absolute="true">Conference CAO</g:link>: <span><b>${accountInstance?.getConferencesCaoList()?.size()}</b></span></li>
        <li>&nbsp;</li>
        <li>Date Events: <span><b>${accountInstance?.getDatesList()?.size()}</b></span></li>
        <li>File Uploads: <span><b>${accountInstance?.getFileList()?.size()}</b></span></li>
        <li>Changes: <span><b>${accountInstance?.getChanges()}</b></span></li>
        <li>&nbsp;</li>
        <li>TD POC: <span><b>${accountInstance?.getTdPocList()?.size()}</b></span></li>
        <li>Supervisor: <span><b>${accountInstance?.getSupervisingList()?.size()}</b></span></li>
        <li>&nbsp;</li>
        <li>Roles: <span><b>${accountInstance?.getAuthorities()?.size()}</b></span></li>
        <li>&nbsp;</li>
        <li>Total Estimates: <span><b><g:formatNumber number="${accountInstance?.getEstimates()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>Total Actuals: <span><b><g:formatNumber number="${accountInstance?.getActuals()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>&nbsp;</li>
        <li>&nbsp;</li>
    </ul>
    <g:if test="${accountInstance?.hasAuthority("ROLE_EOC")}">
        <h2>EOC</h2>
    </g:if>
    <g:if test="${accountInstance?.hasAuthority("ROLE_ADMIN")}">
        <h2>ADMIN</h2>
    </g:if>
    <g:if test="${accountInstance?.hasAuthority("ROLE_TD_ADMIN") || accountInstance?.hasAuthority("ROLE_TD_FULL_ADMIN")}">
        <h2 class="yellow_threshold">TD ADMIN</h2>
    </g:if>
    <g:if test="${accountInstance?.hasAuthority("ROLE_FMC_ADMIN")}">
        <h2 class="green_threshold">FMC ADMIN</h2>
    </g:if>
    <g:if test="${accountInstance?.hasAuthority("ROLE_DEVELOPER")}">
        <h2 class="blue_status">DEVELOPER</h2>
    </g:if>
</div>
