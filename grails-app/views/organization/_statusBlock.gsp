<div id="status_block" role="complementary">
    <g:if test="$organizationInstance?.officeSymbol}">
        <center><g:img dir="/images/td" height="100px" width="100px" file="${organizationInstance?.getTopParent()?.officeSymbol?.replaceAll("AFRL/","")?.replaceAll(" ","_")?.toLowerCase()}_logo.png" alt="${organizationInstance?.officeSymbol} Logo" title="${organizationInstance?.officeSymbol} Logo"/></center>
    </g:if>
    <h1>Information</h1>
    <ul>
        <li>Date Created: <span><b><g:formatDate date="${organizationInstance?.createdDate}" type="date" style="MEDIUM"/></b></span></li>
        <li>Edit Count: <span><b>${organizationInstance?.accountEdit}</b></span></li>
        <li>&nbsp;</li>
        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
            <li><g:link controller="Account" action="listAssigned" params="[id: "${organizationInstance?.id}"]" absolute="true">Total Assigned</g:link>: <span><b>${organizationInstance?.getAssigned()?.size()}</b></span></li>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
            <li>Total Assigned: <span><b>${organizationInstance?.getAssigned()?.size()}</b></span></li>
        </sec:ifNotGranted>
        <g:if test="${organizationInstance?.trueTD && organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
            <li>Total Contacts: <span><b>${organizationInstance?.contacts?.size()}</b></span></li>
        </g:if>
        <g:if test="${organizationInstance?.trueTD}">
            <g:if test="${organizationInstance?.levelTD?.equalsIgnoreCase("4")}">

            </g:if>
            <g:elseif test="${organizationInstance?.levelTD?.equalsIgnoreCase("3")}">
                <li>&nbsp;</li>
                <li>Total Sections: <span><b>${organizationInstance?.getChildren(1)?.size()}</b></span></li>
            </g:elseif>
            <g:elseif test="${organizationInstance?.levelTD?.equalsIgnoreCase("2")}">
                <li>&nbsp;</li>
                <li>Total Branches: <span><b>${organizationInstance?.getChildren(1)?.size()}</b></span></li>
                <li>Total Sections: <span><b>${organizationInstance?.getChildren(2)?.size()}</b></span></li>
            </g:elseif>
            <g:elseif test="${organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
                <li>&nbsp;</li>
                <g:if test="${organizationInstance?.getChildren(0)?.size()}"><li>Total Directorates: <span><b>${organizationInstance?.getChildren(0)?.size()}</b></span></li></g:if>
                <li>Total Divisions: <span><b>${organizationInstance?.getChildren(1)?.size()}</b></span></li>
                <li>Total Branches: <span><b>${organizationInstance?.getChildren(2)?.size()}</b></span></li>
                <li>Total Sections: <span><b>${organizationInstance?.getChildren(3)?.size()}</b></span></li>
            </g:elseif>
        </g:if>
        <li>&nbsp;</li>
        <g:if test="${organizationInstance?.trueTD && organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
            <li><g:link controller="Conference" action="responsibleList" params="[id: "${organizationInstance?.id}"]" absolute="true">Responsible</g:link>: <span><b>${organizationInstance?.getResponsible()?.size()}</b></span></li>
        </g:if>
        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
            <li><g:link controller="Attendee" action="attendeeList" params="[id: "${organizationInstance?.id}"]" absolute="true">Attendee(s)</g:link>: <span><b>${organizationInstance?.getAttendees()?.size()}</b></span></li>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
            <li>Attendee(s): <span><b>${organizationInstance?.getAttendees()?.size()}</b></span></li>
        </sec:ifNotGranted>
        <li>&nbsp;</li>
        <li>Total Estimates: <span><b><g:formatNumber number="${organizationInstance?.getEstimates()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>Total Actuals: <span><b><g:formatNumber number="${organizationInstance?.getActuals()}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b></span></li>
        <li>&nbsp;</li>
        <li>
            <div id="raphael"></div>
            %{--<g:if test="${totalCount > 0}">--}%
            %{--<g:pieChart title='Attendance Type'--}%
                        %{--colors="${['FF0000','FFFF00','00FF00','00FFFF','0000FF','FF00FF','CCCCCC']}"--}%
                        %{--fill="${'bg,s,FFFFFF'}"--}%
                        %{--dataType='simple'--}%
                        %{--size="${[190,190]}"--}%
                        %{--data='${countList}' />--}%
            %{--</g:if>--}%
        </li>
        <li>&nbsp;</li>
        <g:if test="${organizationInstance?.trueTD}">
            <h2>Technology Directorate</h2>
        </g:if>
    </ul>
</div>
