
<%@ page import="mil.ebs.ctm.Account" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-account" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-account" class="content scaffold-show" role="main">
            <g:render template="statusBlock"/>

			<h1>
                <g:if test="${accountInstance?.markedForDeletion}">
                    <g:img dir="/images/icons" file="delete-user_512.png" width="48" height="48" alt="Account Locked" title="Account Locked"/>
                </g:if>
                <g:else>
                    <g:img dir="/images/icons" file="user_512.png" width="48" height="48" alt="Account" title="Account"/>
                </g:else>
                <g:message code="default.show.label" args="[entityName]" />
                <g:if test="${accountInstance?.notifyChanges}"><g:img dir="/images/icons" file="Email-Sending-512.png" width="32" height="32" alt="Notify Attendee Changes" title="Notify Attendee Changes"/></g:if>
                <g:if test="${accountInstance?.notifyConferenceChanges}"><g:img dir="/images/icons" file="Email-Sending-2-512.png" width="32" height="32" alt="Notify Conference Changes" title="Notify Conference Changes"/></g:if>
                <g:if test="${accountInstance?.canAttendConferences}"><g:img dir="/images/icons" file="group_512.png" width="36" height="36" alt="Can Attend Conferences" title="Can Attend Conferences"/></g:if><g:else><g:img dir="/images/icons" file="close-group_512.png" width="36" height="36" alt="Can NOT Attend Conferences" title="Can NOT Attend Conferences"/></g:else>
                <g:if test="${accountInstance?.markedForDeletion}"><g:img dir="/images/icons" file="security_lock_128.png" width="32" height="32" alt="Account Locked" title="Account Locked"/></g:if>
                <a style="float:right" href="mailto:${accountInstance?.emailAddress}"><g:img dir="/images/icons" file="mail_512.png" width="64" height="64" alt="Send Mail" title="Send Mail"/></a>
            </h1>

            <g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <ol class="property-list account">
                <div class="account-block-blue">
                    <g:if test="${accountInstance?.employeeType?.equalsIgnoreCase("Civilian")}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-blue">${accountInstance?.employeeType}</div>
                        </div>
                    </g:if>
                    <g:if test="${accountInstance?.employeeType?.equalsIgnoreCase("Military")}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-purple">${accountInstance?.employeeType}</div>
                        </div>
                    </g:if>
                    <g:if test="${accountInstance?.employeeType?.equalsIgnoreCase("Contractor")}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-red">CTR</div>
                        </div>
                    </g:if>

                    <g:if test="${accountInstance?.username}">
                    <li class="fieldcontain">
                        <span id="username-label" class="property-label">
                            <b><g:message code="account.username.label" default="Account (CAC)" /></b>
                        </span>
                        <span class="property-value" aria-labelledby="username-label">
                            <g:fieldValue bean="${accountInstance}" field="username"/>
                            <g:if test="${accountInstance?.accountValidated}">
                                    <g:img dir="/images/icons" file="ok_512.png" width="16" height="16" alt="Account Validated" title="Account Validated"/>
                            </g:if>
                            <g:else>
                                <g:img dir="/images/icons" file="alert_512.png" width="16" height="16" alt="Account not LDAP Verified" title="Account not LDAP Verified"/>
                            </g:else>
                        </span>
                    </li>
                    </g:if>

                    <g:if test="${accountInstance?.displayName}">
                    <li class="fieldcontain">
                        <span id="displayName-label" class="property-label">
                            <b><g:message code="account.displayName.label" default="Display Name" /></b>
                        </span>
                        <span class="property-value" aria-labelledby="displayName-label">
                            <g:if test="${(applicationContext.springSecurityService.getCurrentUser()).toString().equalsIgnoreCase(accountInstance?.displayName)}">
                                <g:link tabindex="-1" class="top_link" controller="Account" action="currentAuth" absolute="true">
                                    <span>${(applicationContext.springSecurityService.getCurrentUser())}</span>
                                </g:link>
                            </g:if>
                            <g:else>
                                <g:fieldValue bean="${accountInstance}" field="displayName"/>
                            </g:else>
                        </span>
                    </li>
                    </g:if>

                    %{--<g:if test="${accountInstance?.title}">--}%
                    %{--<li class="fieldcontain">--}%
                        %{--<span id="title-label" class="property-label">--}%
                            %{--<b><g:message code="account.title.label" default="Title" /></b>--}%
                        %{--</span>--}%
                        %{--<span class="property-value" aria-labelledby="title-label">--}%
                            %{--<g:fieldValue bean="${accountInstance}" field="title"/>--}%
                        %{--</span>--}%
                    %{--</li>--}%
                    %{--</g:if>--}%

                    <g:if test="${accountInstance?.firstName || accountInstance?.lastName}">
                    <li class="fieldcontain">
                        <span id="name-label" class="property-label">
                            <b><g:message code="account.identity.label" default="Identity" /></b>
                        </span>
                        <span class="property-value" aria-labelledby="name-label">
                            <g:fieldValue bean="${accountInstance}" field="lastName"/>,&nbsp;<g:fieldValue bean="${accountInstance}" field="firstName"/>&nbsp;<g:fieldValue bean="${accountInstance}" field="middleInitial"/><g:if test="${accountInstance?.title}">&nbsp;&nbsp;<i><span style="color:#999999">(<g:fieldValue bean="${accountInstance}" field="title"/>)</span></i></g:if>
                        </span>
                    </li>
                    </g:if>

                    <g:if test="${accountInstance?.emailAddress}">
                    <li class="fieldcontain">
                            <span id="emailAddress-label" class="property-label">
                                <b><g:message code="account.emailAddress.label" default="Email" /></b>
                            </span>
                        <span class="property-value" aria-labelledby="emailAddress-label">
                            <g:fieldValue bean="${accountInstance}" field="emailAddress"/>
                            <g:if test="${accountInstance?.emailValidated}">
                                <g:img dir="/images/icons" file="ok_512.png" width="16" height="16" alt="Account Verified" title="Account Verified"/>
                            </g:if>
                            <g:else>
                                <g:img dir="/images/icons" file="alert_512.png" width="16" height="16" alt="Account not LDAP Verified" title="Account not LDAP Verified"/>
                            </g:else>
                        </span>
                    </li>
                    </g:if>

                    <g:if test="${accountInstance?.phoneNumber}">
                    <li class="fieldcontain">
                        <span id="phoneNumber-label" class="property-label">
                            <b><g:message code="account.phoneNumber.label" default="Phone Number" /></b>
                        </span>
                        <span class="property-value" aria-labelledby="phoneNumber-label"><g:fieldValue bean="${accountInstance}" field="phoneNumber"/></span>
                    </li>
                    </g:if>

                    <g:if test="${accountInstance?.rankGrade}">
                    <li class="fieldcontain">
                        <span id="rankGrade-label" class="property-label">
                            <b><g:message code="account.rankGrade.label" default="Rank/Grade" /></b>
                        </span>
                        <span class="property-value" aria-labelledby="rankGrade-label"><g:fieldValue bean="${accountInstance}" field="rankGrade"/></span>
                    </li>
                    </g:if>
                </div>

                    <br/>

                    <g:if test="${accountInstance?.markedForDeletion || accountInstance?.accountExpired || !accountInstance?.enabled || accountInstance?.accountLocked}">
                        <div class="account-block-red emphasize">
                    </g:if>
                    <g:else>
                        <div class="account-block">
                    </g:else>
                    <li class="fieldcontain">
                        <span id="enabled-label" class="property-label"><g:message code="account.enabled.label" default="Enabled" /></span>
                        <span class="property-value" aria-labelledby="enabled-label">
                            <g:if test="${accountInstance?.enabled}">
                                <b><g:formatBoolean boolean="${accountInstance?.enabled}"/></b>
                            </g:if>
                            <g:else>
                                <span style="color:#FF0000"><b><g:formatBoolean boolean="${accountInstance?.enabled}"/></b></span>
                            </g:else>
                        </span>
                    </li>

                    <g:if test="${accountInstance?.accountExpired}">
                    <li class="fieldcontain">
                        <span id="accountExpired-label" class="property-label"><g:message code="account.accountExpired.label" default="Account Expired" /></span>
                        <span class="property-value" aria-labelledby="accountExpired-label"><g:formatBoolean boolean="${accountInstance?.accountExpired}" /></span>
                    </li>
                    </g:if>

                    <g:if test="${accountInstance?.accountLocked}">
                    <li class="fieldcontain">
                        <span id="accountLocked-label" class="property-label">
                            <span style="color:#FF0000"><b><g:message code="account.accountLocked.label" default="Account Locked" /></b></span>
                        </span>
                        <span class="property-value" aria-labelledby="accountLocked-label">
                            <b><g:formatBoolean boolean="${accountInstance?.accountLocked}" /></b>
                        </span>
                    </li>
                    </g:if>

                    <g:if test="${accountInstance?.lastChangeDate}">
                    <li class="fieldcontain">
                        <span id="lastChangeDate-label" class="property-label"><g:message code="account.lastChangeDate.label" default="Last Change" /></span>
                        <span class="property-value" aria-labelledby="lastChangeDate-label">
                            <g:formatDate date="${accountInstance?.lastChangeDate}" type="date" style="MEDIUM"/>
                            <span style="color:#999999"><i>(${accountInstance?.getChangeDays()} days)</i></span>
                            <g:if test="${accountInstance?.lastChange}">
                                &nbsp;-&nbsp;<span style="color:#006dba"><i>${accountInstance?.lastChange}</i></span>
                            </g:if>
                        </span>
                    </li>
                    </g:if>
                </div>

                <br/>

                <div class="account-block-icon">
                    <g:if test="${accountInstance?.assignedTD}">
                        <span id="responsibleTD-icon">
                            <g:img dir="/images/td" height="100px" width="100px" file="${accountInstance?.assignedTD?.getTopParent()?.officeSymbol?.replaceAll("AFRL/","")?.replaceAll(" ","_")?.toLowerCase()}_logo.png" alt="${accountInstance?.assignedTD?.officeSymbol} Logo" title="${accountInstance?.assignedTD?.officeSymbol} Logo"/>
                        </span>
                    </g:if>

                    <g:if test="${accountInstance?.assignedTD}">
                    <li class="fieldcontain">
                        <span id="assignedTD-label" class="property-label"><g:message code="account.assignedTD.label" default="Assigned TD" /></span>
                        <span class="property-value" aria-labelledby="assignedTD-label">
                            %{--<g:fieldValue bean="${accountInstance}" field="assignedTD" />--}%
                            <g:if test="${accountInstance?.assignedTD?.levelTD?.equals("3")}">
                                <g:link controller="Organization" action="show" id="${accountInstance?.assignedTD?.id}" absolute="true">${accountInstance?.assignedTD?.officeSymbol}</g:link>
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${accountInstance?.assignedTD?.parent?.id}" absolute="true">${accountInstance?.assignedTD?.parent?.officeSymbol}</g:link>
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${accountInstance?.assignedTD?.parent?.parent?.id}" absolute="true">${accountInstance?.assignedTD?.parent?.parent}</g:link>
                            </g:if>
                            <g:elseif test="${accountInstance?.assignedTD?.levelTD?.equals("2")}">
                                <g:link controller="Organization" action="show" id="${accountInstance?.assignedTD?.id}" absolute="true">${accountInstance?.assignedTD?.officeSymbol}</g:link>
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${accountInstance?.assignedTD?.parent?.id}" absolute="true">${accountInstance?.assignedTD?.parent}</g:link>
                            </g:elseif>
                            <g:else>
                                <g:link controller="Organization" action="show" id="${accountInstance?.assignedTD?.id}" absolute="true">${accountInstance?.assignedTD}</g:link>
                            </g:else>
                        </span>
                    </li>
                    </g:if>

                    <li class="fieldcontain">
                        <span id="supervisor-label" class="property-label"><g:message code="account.supervisor.label" default="Supervisor" /></span>
                        <span class="property-value" aria-labelledby="supervisor-label">
                            <g:if test="${accountInstance?.supervisor}">
                                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                    <g:link controller="Account" action="show" id="${accountInstance?.supervisor?.id}" absolute="true"><g:fieldValue bean="${accountInstance}" field="supervisor" /></g:link>
                                </sec:ifAnyGranted>
                                <sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                    <g:fieldValue bean="${accountInstance}" field="supervisor" />
                                </sec:ifNotGranted>
                            </g:if>
                            <g:else>
                                <i><span style="font-size:12px; color:#999999"><b>SUPERVISOR</b> not set for account</span></i>
                            </g:else>
                        </span>
                    </li>

                    <g:if test="${accountInstance?.fsOrganization}">
                    <li class="fieldcontain">
                        <span id="fsOrganization-label" class="property-label"><g:message code="account.fsOrganization.label" default="Organization" /></span>
                        <span class="property-value" aria-labelledby="fsOrganization-label"><g:fieldValue bean="${accountInstance}" field="fsOrganization" /></span>
                    </li>
                    </g:if>
                </div>

                <br/>

                <div class="account-block emphasize">
                    %{--<div class="ribbon-wrapper-blue">--}%
                        %{--<div class="ribbon-blue">ROLES</div>--}%
                    %{--</div>--}%
                    <li class="fieldcontain">
                        <span id="roles-label" class="property-label">
                            <g:message code="account.roles.label" default="Roles" />&nbsp;<b>(${accountInstance?.getAuthorities()?.size()})</b>
                        </span>
                        <span class="property-value" aria-labelledby="roles-label">
                            <g:each in="${accountInstance?.getAuthorities()}" var="r">
                                ${r?.authority}<br/>
                            </g:each>
                        </span>
                    </li>
                </div>

                <br/>

                <g:if test="${accountInstance?.getSupervisingList()?.size() > 0}">
                    <div class="account-block">
                        <li class="fieldcontain">
                            <span id="subs-label" class="property-label">
                                <g:message code="account.subs.label" default="Subs" />&nbsp;<b>(${accountInstance?.getSupervisingList()?.size()})</b>
                            </span>
                            <span class="property-value" aria-labelledby="subs-label">
                                <g:each in="${accountInstance?.getSupervisingList()}" var="s">
                                    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                        <g:link controller="Account" action="show" id="${s?.id}" absolute="true">${s}</g:link><br/>
                                    </sec:ifAnyGranted>
                                    <sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                        ${s}
                                    </sec:ifNotGranted>
                                </g:each>
                            </span>
                        </li>
                    </div>
                </g:if>

                %{--<div class="account-block">--}%
                    %{--<li class="fieldcontain">--}%
                        %{--<span id="subs-label" class="property-label">--}%
                            %{--Authentication--}%
                        %{--</span>--}%
                        %{--<span class="property-value" aria-labelledby="subs-label">--}%
                            %{--<g:link tabindex="-1" class="top_link" controller="Account" action="currentAuth" absolute="true">--}%
                                %{--<span>${(applicationContext.springSecurityService.getCurrentUser())}</span>--}%
                            %{--</g:link>--}%
                        %{--</span>--}%
                    %{--</li>--}%
                %{--</div>--}%
			</ol>

            <g:form url="[resource:accountInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
                    <g:if test="${!accountInstance?.markedForDeletion}">
                        <g:link class="edit" action="edit" resource="${accountInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        <sec:ifAnyGranted roles="ROLE_EOC">
                            <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.lock.label', default: 'Lock')}" onclick="return confirm('${message(code: 'default.button.lock.confirm.message', default: 'Are you sure you wish to LOCK this account?')}');" />
                        </sec:ifAnyGranted>
                    </g:if>
                    <g:else>
                        <sec:ifAnyGranted roles="ROLE_EOC">
                            <g:actionSubmit class="delete" action="undelete" value="${message(code: 'default.button.unlock.label', default: 'Unlock')}" onclick="return confirm('${message(code: 'default.button.unlock.confirm.message', default: 'Are you sure you wish to UNLOCK this account?')}');" />
                        </sec:ifAnyGranted>
                    </g:else>

                    <sec:ifAnyGranted roles="ROLE_EOC, ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN">
                        <g:if test="${!accountInstance?.emailValidated && !accountInstance?.markedForDeletion}">
                            <g:actionSubmit class="edit" action="verify" value="${message(code: 'default.button.verify.label', default: 'Verify')}" onclick="return confirm('${message(code: 'default.button.verify.confirm.message', default: 'Are you sure you want to Verify this Account?')}');" />
                        </g:if>
                        <g:if test="${(!accountInstance?.accountValidated && !accountInstance?.emailValidated) && !accountInstance?.markedForDeletion}">
                            <g:actionSubmit class="edit" action="validate" value="${message(code: 'default.button.validate.label', default: 'Validate')}" onclick="return confirm('${message(code: 'default.button.validate.confirm.message', default: 'Are you sure you want to Validate this Account?')}');" />
                        </g:if>
                    </sec:ifAnyGranted>
                    <sec:ifAnyGranted roles="ROLE_EOC, ROLE_ADMIN">
                        <g:if test="${(accountInstance?.accountValidated || accountInstance?.emailValidated) && !accountInstance?.markedForDeletion}">
                            <g:actionSubmit class="delete" action="invalidate" value="${message(code: 'default.button.invalidate.label', default: 'Invalidate')}" onclick="return confirm('${message(code: 'default.button.invalidate.confirm.message', default: 'Are you sure you want to Invalidate this Account?')}');" />
                        </g:if>
                    </sec:ifAnyGranted>

                    <sec:ifAnyGranted roles="ROLE_EOC, ROLE_DEVELOPER">
                        <g:actionSubmit class="edit" action="sync" value="${message(code: 'default.button.sync.label', default: 'Sync')}" onclick="return confirm('${message(code: 'default.button.sync.confirm.message', default: 'Are you sure you want to Sync this Account?')}');" />
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
