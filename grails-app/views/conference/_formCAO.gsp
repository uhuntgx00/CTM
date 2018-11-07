<%@ page import="org.springframework.web.util.HtmlUtils; mil.ebs.ctm.Organization; mil.ebs.ctm.Account; mil.ebs.ctm.Conference" %>

<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
    <br/><hr>
    <g:if test="${!conferenceInstance?.phaseState?.equalsIgnoreCase("Open") || conferenceInstance?.responsibleTD}">
        <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'responsibleTD', 'error')} ">
            <label for="responsibleTD">
                <g:message code="conference.responsibleTD.label" default="Responsible TD" />
            </label>
            <g:select id="responsibleTD" name="responsibleTD.id" from="${Organization.findAllByTrueTDAndLevelTDAndParent(true, "1", null).sort {it.officeSymbol} }" optionKey="id" value="${conferenceInstance?.responsibleTD?.id}" class="many-to-one" noSelection="['null': '']"
                  onchange="${remoteFunction(
                      controller: 'conference',
                      action: 'ajaxGetAccounts',
                      params: '\'id=\' + escape(this.value)',
                      update: 'accountCaoBlock'
                  )}"
            />
        </div>
    </g:if>

    <g:if test="${!conferenceInstance?.phaseState?.equalsIgnoreCase("Open") || conferenceInstance?.alternateRespTD}">
        <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'alternateRespTD', 'error')} ">
            <label for="alternateRespTD">
                <g:message code="conference.alternateRespTD.label" default="Alternate Responsible TD" />
            </label>
            <g:select id="alternateRespTD" name="alternateRespTD.id" from="${Organization.findAllByTrueTDAndLevelTDAndParent(true, "1", null).sort {it.officeSymbol} }" optionKey="id" value="${conferenceInstance?.alternateRespTD?.id}" class="many-to-one" noSelection="['null': '']"
                  onchange="${remoteFunction(
                      controller: 'conference',
                      action: 'ajaxGetAccountsAlt',
                      params: '\'id=\' + escape(this.value)',
                      update: 'accountAltBlock'
                  )}"
            />
        </div>
    </g:if>

    <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'responsibleOrg', 'error')} ">
        <label for="responsibleOrg">
            <g:message code="conference.responsibleOrg.label" default="External Responsible Organization" />
        </label>
        <g:textField name="responsibleOrg" value="${conferenceInstance?.responsibleOrg}"/>
        &nbsp;&nbsp;<span style="color:#777777"><i>This should be used for <b>External Organization</b> conferences</i></span>
    </div>

        <div id="accountCaoBlock" class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'conferenceAO', 'error')} ">
            <label for="conferenceAO">
                <g:message code="conference.conferenceAO.label" default="Conference AO" />
            </label>
            <g:select id="conferenceAO" name="conferenceAO.id" from="${conferenceInstance?.responsibleTD?.getAssignedAllowed()?.sort {it.displayName}}" optionKey="id" value="${conferenceInstance?.conferenceAO?.id}" class="many-to-one" noSelection="['null': '']"/>
        </div>

        <div id="accountAltBlock" class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'alternateCAO', 'error')} ">
            <label for="alternateCAO">
                <g:message code="conference.alternateCAO.label" default="Alternate CAO" />
            </label>
            <g:select id="alternateCAO" name="alternateCAO.id" from="${conferenceInstance?.alternateRespTD?.getAssignedAllowed()?.sort {it.displayName}}" optionKey="id" value="${conferenceInstance?.alternateCAO?.id}" class="many-to-one" noSelection="['null': '']"/>
        </div>
</sec:ifAnyGranted>
<sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
    <g:if test="${conferenceInstance?.isCAO()}">
        <br/><hr>
        <g:if test="${!conferenceInstance?.phaseState?.equalsIgnoreCase("Open") || conferenceInstance?.responsibleTD}">
            <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'responsibleTD', 'error')} ">
                <label for="responsibleTD">
                    <g:message code="conference.responsibleTD.label" default="Responsible TD" />
                </label>
                <g:select id="responsibleTD" name="responsibleTD.id" from="${Organization.findAllByTrueTDAndLevelTDAndParent(true, "1", null).sort {it.officeSymbol} }" optionKey="id" value="${conferenceInstance?.responsibleTD?.id}" class="many-to-one" noSelection="['null': '']"
                      onchange="${remoteFunction(
                          controller: 'conference',
                          action: 'ajaxGetAccounts',
                          params: '\'id=\' + escape(this.value)',
                          update: 'accountCaoBlock'
                      )}"
                />
            </div>
        </g:if>

        <g:if test="${!conferenceInstance?.phaseState?.equalsIgnoreCase("Open") || conferenceInstance?.alternateRespTD}">
            <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'alternateRespTD', 'error')} ">
                <label for="alternateRespTD">
                    <g:message code="conference.alternateRespTD.label" default="Alternate Responsible TD" />
                </label>
                <g:select id="alternateRespTD" name="alternateRespTD.id" from="${Organization.findAllByTrueTDAndLevelTDAndParent(true, "1", null).sort {it.officeSymbol} }" optionKey="id" value="${conferenceInstance?.alternateRespTD?.id}" class="many-to-one" noSelection="['null': '']"
                      onchange="${remoteFunction(
                          controller: 'conference',
                          action: 'ajaxGetAccountsAlt',
                          params: '\'id=\' + escape(this.value)',
                          update: 'accountAltBlock'
                      )}"
                />
            </div>
        </g:if>

        <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'responsibleOrg', 'error')} ">
            <label for="responsibleOrg">
                <g:message code="conference.responsibleOrg.label" default="External Responsible Organization" />
            </label>
            <g:textField name="responsibleOrg" value="${conferenceInstance?.responsibleOrg}"/>
            &nbsp;&nbsp;<span style="color:#777777"><i>This should be used for <b>External Organization</b> conferences</i></span>
        </div>

        <g:if test="${conferenceInstance?.responsibleTD}">
            <div id="accountCaoBlock" class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'conferenceAO', 'error')} ">
                <label for="conferenceAO">
                    <g:message code="conference.conferenceAO.label" default="Conference AO" />
                </label>
                <g:select id="conferenceAO" name="conferenceAO.id" from="${conferenceInstance?.responsibleTD?.getAssignedAllowed()?.sort {it.displayName}}" optionKey="id" value="${conferenceInstance?.conferenceAO?.id}" class="many-to-one" noSelection="['null': '']"/>
            </div>
        </g:if>

        <g:if test="${conferenceInstance?.alternateRespTD}">
            <div id="accountAltBlock" class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'alternateCAO', 'error')} ">
                <label for="alternateCAO">
                    <g:message code="conference.alternateCAO.label" default="Alternate CAO" />
                </label>
                <g:select id="alternateCAO" name="alternateCAO.id" from="${conferenceInstance?.responsibleTD?.getAssignedAllowed()?.sort {it.displayName}}" optionKey="id" value="${conferenceInstance?.alternateCAO?.id}" class="many-to-one" noSelection="['null': '']"/>
            </div>
        </g:if>
    </g:if>
</sec:ifNotGranted>
