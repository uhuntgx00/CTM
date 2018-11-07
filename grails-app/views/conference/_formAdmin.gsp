<g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review") || conferenceInstance?.phaseState?.equalsIgnoreCase("AFMC Review") || conferenceInstance?.phaseState?.equalsIgnoreCase("SAF Review") || conferenceInstance?.phaseState?.equalsIgnoreCase("Approved") || conferenceInstance?.phaseState?.equalsIgnoreCase("Finalizing")}">
    <br/><hr>
</g:if>

<g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review") || conferenceInstance?.phaseState?.equalsIgnoreCase("Approved") || conferenceInstance?.phaseState?.equalsIgnoreCase("Finalizing")}">
    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'afrlSoccer', 'error')} ">
        <label for="afrlSoccer">
            <g:message code="conference.afrlSoccer.label" default="AFRL SOCCER#" />
        </label>
        <g:textField name="afrlSoccer" value="${conferenceInstance?.afrlSoccer}"/>
    </div>

    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'afrlSoccerDate', 'error')} ">
        <label for="afrlSoccerDate">
            <g:message code="conference.afrlSoccerDate.label" default="AFRL SOCCER Date" />
        </label>
        <g:datePicker name="afrlSoccerDate" precision="day" value="${conferenceInstance?.afrlSoccerDate}" default="none" noSelection="['': '']" />
    </div>
</g:if>

<g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFMC Review") || conferenceInstance?.phaseState?.equalsIgnoreCase("SAF Review") || conferenceInstance?.phaseState?.equalsIgnoreCase("Approved") || conferenceInstance?.phaseState?.equalsIgnoreCase("Finalizing")}">
    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'afmcSoccer', 'error')} ">
        <label for="afmcSoccer">
            <g:message code="conference.afmcSoccer.label" default="AFMC SOCCER#" />
        </label>
        <g:textField name="afmcSoccer" value="${conferenceInstance?.afmcSoccer}"/>
    </div>

    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'afmcSoccerDate', 'error')} ">
        <label for="afmcSoccerDate">
            <g:message code="conference.afmcSoccerDate.label" default="AFMC SOCCER Date" />
        </label>
        <g:datePicker name="afmcSoccerDate" precision="day" value="${conferenceInstance?.afmcSoccerDate}" default="none" noSelection="['': '']" />
    </div>
</g:if>

<g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("SAF Review") || conferenceInstance?.phaseState?.equalsIgnoreCase("Approved") || conferenceInstance?.phaseState?.equalsIgnoreCase("Finalizing")}">
    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'safTmt', 'error')} ">
        <label for="safTmt">
            <g:message code="conference.safTmt.label" default="SAF TMT#" />
        </label>
        <g:textField name="safTmt" value="${conferenceInstance?.safTmt}"/>
    </div>

    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'safTmtDate', 'error')} ">
        <label for="safTmtDate">
            <g:message code="conference.safTmtDate.label" default="SAF TMT Date" />
        </label>
        <g:datePicker name="safTmtDate" precision="day" value="${conferenceInstance?.safTmtDate}" default="none" noSelection="['': '']" />
    </div>
</g:if>

%{--<br/><hr>--}%

%{--<div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'primarySponsor', 'error')} ">--}%
	%{--<label for="primarySponsor">--}%
		%{--<g:message code="conference.primarySponsor.label" default="Primary Sponsor" />--}%
	%{--</label>--}%
	%{--<g:textField name="primarySponsor" size="50" value="${conferenceInstance?.primarySponsor}"/>--}%
%{--</div>--}%


