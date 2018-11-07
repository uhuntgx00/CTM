<%@ page import="mil.ebs.ctm.ref.RefRankGrade; mil.ebs.ctm.Organization; mil.ebs.ctm.Account" %>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'username', 'error')} required">
	<label for="username">
		<g:message code="account.username.label" default="Account (CAC)" />
        <sec:ifAnyGranted roles="ROLE_EOC">
            <span class="required-indicator">*</span>
        </sec:ifAnyGranted>
	</label>
    <g:if test="${accountInstance?.accountValidated}">
        ${accountInstance?.username}
        <g:img dir="/images/icons" file="ok_512.png" width="16" height="16" alt="Account Verified" title="Account Verified"/>
    </g:if>
    <g:else>
        <sec:ifAnyGranted roles="ROLE_EOC">
            <g:textField name="username" size="50" required="" value="${accountInstance?.username}"/>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_EOC">
            ${accountInstance?.username}
        </sec:ifNotGranted>
        <g:img dir="/images/icons" file="alert_512.png" width="16" height="16" alt="Account not LDAP Verified" title="Account not LDAP Verified"/>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'displayName', 'error')}">
	<label for="displayName">
        <g:img dir="/images/icons" file="Sticky-512.png" width="16" height="16" alt="LDAP Data Reference" title="LDAP Data Reference"/>
        <g:message code="account.displayName.label" default="Display Name (LDAP)" />
	</label>
    <sec:ifAnyGranted roles="ROLE_EOC, ROLE_DEVELOPER">
        <g:textField name="displayName" size="50" required="" value="${accountInstance?.displayName}"/>
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="ROLE_EOC, ROLE_DEVELOPER">
        <g:if test="${accountInstance?.displayName}">
            <span id="displayName">${accountInstance?.displayName}</span>
        </g:if>
        <g:else>
            <i><span style="font-size:12px; color:#999999"><b>LDAP</b> sync data missing</span></i>
        </g:else>
    </sec:ifNotGranted>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'firstName', 'error')}">
	<label for="firstName">
		<g:message code="account.firstName.label" default="First Name" />
	</label>
	<g:textField name="firstName" size="50" value="${accountInstance?.firstName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'lastName', 'error')}">
	<label for="lastName">
		<g:message code="account.lastName.label" default="Last Name" />
	</label>
	<g:textField name="lastName" size="50" value="${accountInstance?.lastName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'middleInitial', 'error')}">
	<label for="middleInitial">
		<g:message code="account.middleInitial.label" default="Middle Initial" />
	</label>
	<g:textField name="middleInitial" size="5" value="${accountInstance?.middleInitial}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'title', 'error')}">
	<label for="title">
        <g:img dir="/images/icons" file="Sticky-512.png" width="16" height="16" alt="LDAP Data Reference" title="LDAP Data Reference"/>
		<g:message code="account.title.label" default="Title" />
	</label>
    <sec:ifAnyGranted roles="ROLE_EOC, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN">
    	<g:textField name="title" size="50" value="${accountInstance?.title}"/>
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="ROLE_EOC, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN">
        <g:if test="${accountInstance?.title}">
            ${accountInstance?.title}
        </g:if>
        <g:else>
            <i><span style="font-size:12px; color:#999999"><b>TITLE</b> blank for account</span></i>
        </g:else>
    </sec:ifNotGranted>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'emailAddress', 'error')} required">
	<label for="emailAddress">
        <g:img dir="/images/icons" file="Sticky-512.png" width="16" height="16" alt="LDAP Data Reference" title="LDAP Data Reference"/>
		<g:message code="account.emailAddress.label" default="E-Mail" />
        <sec:ifAnyGranted roles="ROLE_EOC">
            <span class="required-indicator">*</span>
        </sec:ifAnyGranted>
	</label>
    <g:if test="${accountInstance?.emailValidated && accountInstance?.accountValidated}">
        ${accountInstance?.emailAddress}
    </g:if>
    <g:else>
        <sec:ifAnyGranted roles="ROLE_EOC">
            <g:textField name="emailAddress" size="50" required="" value="${accountInstance?.emailAddress}"/>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_EOC">
            ${accountInstance?.emailAddress}
        </sec:ifNotGranted>
    </g:else>
    <g:if test="${accountInstance?.emailValidated}">
        <g:img dir="/images/icons" file="ok_512.png" width="16" height="16" alt="Account Verified" title="Account Verified"/>
    </g:if>
    <g:else>
        <g:img dir="/images/icons" file="alert_512.png" width="16" height="16" alt="Account not LDAP Verified" title="Account not LDAP Verified"/>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'phoneNumber', 'error')}">
	<label for="phoneNumber">
        <g:img dir="/images/icons" file="Sticky-512.png" width="16" height="16" alt="LDAP Data Reference" title="LDAP Data Reference"/>
		<g:message code="account.phoneNumber.label" default="Phone Number" />
	</label>
    <sec:ifAnyGranted roles="ROLE_EOC, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN">
    	<g:textField name="phoneNumber" size="14" maxlength="14" value="${accountInstance?.phoneNumber}" alt="Phone Number: (XXX) XXX-XXXX" title="Phone Number: (XXX) XXX-XXXX"/>
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="ROLE_EOC, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN">
        <g:if test="${accountInstance?.phoneNumber}">
            ${accountInstance?.phoneNumber}
        </g:if>
        <g:else>
            <i><span style="font-size:12px; color:#999999"><b>PHONE NUMBER</b> blank for account</span></i>
        </g:else>
    </sec:ifNotGranted>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'employeeType', 'error')} required">
	<label for="employeeType">
		<g:message code="account.employeeType.label" default="Employee Type" />
        <sec:ifAnyGranted roles="ROLE_EOC">
            <span class="required-indicator">*</span>
        </sec:ifAnyGranted>
	</label>
    <sec:ifAnyGranted roles="ROLE_EOC">
        <g:select required="" name="employeeType" from="${accountInstance.constraints.employeeType.inList}" value="${accountInstance?.employeeType}" valueMessagePrefix="account.employeeType"/>
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="ROLE_EOC">
        ${accountInstance?.employeeType}
    </sec:ifNotGranted>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'rankGrade', 'error')}">
	<label for="rankGrade">
		<g:message code="account.rankGrade.label" default="Rank/Grade" />
	</label>
    <g:select id="rankGrade" name="rankGrade.id" from="${RefRankGrade.findAllByEmployeeType(accountInstance?.employeeType)}" optionKey="id" value="${accountInstance?.rankGrade?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_EOC">
    <div class="fieldcontain">
        <label></label>
        <g:img dir="/images/icons" file="Sticky-512.png" width="12" height="12" alt="LDAP Data Reference" title="LDAP Data Reference"/>
        <i><span style="font-size:12px; color:#999999">Attributes that will be <b>SYNC</b> updated from <b>LDAP</b></span></i>
    </div>
</sec:ifAnyGranted>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'enabled', 'error')} ">
	<label for="enabled">
		<b><g:message code="account.enabled.label" default="Enabled" /></b>
	</label>
    <sec:ifAnyGranted roles="ROLE_EOC">
        <g:checkBox name="enabled" value="${accountInstance?.enabled}" />
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="ROLE_EOC">
        <g:formatBoolean boolean="${accountInstance?.enabled}"/>
    </sec:ifNotGranted>
    %{--<g:formatDate date="${accountInstance?.lastLoginDate}" type="date" style="MEDIUM"/>--}%
    <span style="color:#999999"><i><g:formatDate date="${accountInstance?.lastLoginDate}" type="date" style="MEDIUM"/>&nbsp;(${accountInstance?.getLoginDays()} days)</i></span>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'accountExpired', 'error')} ">
	<label for="accountExpired">
		<g:message code="account.accountExpired.label" default="Account Expired" />
	</label>
    <sec:ifAnyGranted roles="ROLE_EOC, ROLE_EXPIRE">
	    <g:checkBox name="accountExpired" value="${accountInstance?.accountExpired}" />
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="ROLE_EOC, ROLE_EXPIRE">
        <g:formatBoolean boolean="${accountInstance?.accountExpired}"/>
    </sec:ifNotGranted>
    %{--<g:formatDate id="lastExpirationDate" date="${accountInstance?.lastExpirationDate}" type="date" style="MEDIUM"/>--}%
    <span style="color:#999999"><i><g:formatDate id="lastExpirationDate" date="${accountInstance?.lastExpirationDate}" type="date" style="MEDIUM"/>&nbsp;(${accountInstance?.getExpireDays()} days)</i></span>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'accountLocked', 'error')} ">
	<label for="accountLocked">
		<g:message code="account.accountLocked.label" default="Account Locked" />
	</label>
    <sec:ifAnyGranted roles="ROLE_EOC">
	    <g:checkBox name="accountLocked" value="${accountInstance?.accountLocked}" />
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="ROLE_EOC">
        <g:formatBoolean boolean="${accountInstance?.accountLocked}"/>
    </sec:ifNotGranted>
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'lastChangeDate', 'error')} ">
    <label for="lastChangeDate">
        <g:message code="account.lastChangeDate.label" default="Last Change" />
    </label>
    <g:formatDate id="lastChangeDate" date="${accountInstance?.lastChangeDate}" type="date" style="MEDIUM"/>
    <span style="color:#999999"><i>(${accountInstance?.getChangeDays()} days)</i></span>
    <g:if test="${accountInstance?.lastChange}">
        &nbsp;-&nbsp;<span style="color:#006dba"><i>${accountInstance?.lastChange}</i></span>
    </g:if>
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'assignedTD', 'error')} ">
	<label for="assignedTD">
		<g:message code="account.assignedTD.label" default="Assigned TD" />
	</label>
    <sec:ifAnyGranted roles="ROLE_EOC">
    	<g:select id="assignedTD" name="assignedTD.id" from="${Organization.list().sort{it.officeSymbol}}" optionKey="id" value="${accountInstance?.assignedTD?.id}" class="many-to-one" noSelection="['null': '']"/>
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="ROLE_EOC">
        ${accountInstance?.assignedTD}
    </sec:ifNotGranted>
</div>

<g:if test="${accountInstance?.assignedTD}">
    <div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'supervisor', 'error')} ">
        <label for="supervisor">
            <g:message code="account.supervisor.label" default="Supervisor" />
        </label>
        <sec:ifAnyGranted roles="ROLE_EOC, ROLE_AFRL_USER, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
            <g:select id="supervisor" name="supervisor.id" from="${accountInstance?.assignedTD?.getTopParent()?.getAssigned()?.sort {it.displayName}}" optionKey="id" value="${accountInstance?.supervisor?.id}" class="many-to-one" noSelection="['null': '']"/>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_EOC, ROLE_AFRL_USER, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
            <g:if test="${accountInstance?.supervisor}">
                ${accountInstance?.supervisor}
            </g:if>
            <g:else>
                <i><span style="font-size:12px; color:#999999"><b>SUPERVISOR</b> not set for account</span></i>
            </g:else>
        </sec:ifNotGranted>
    </div>
</g:if>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'fsOrganization', 'error')}">
	<label for="fsOrganization">
		<g:message code="account.fsOrganization.label" default="Organization" />
	</label>
	<g:textField name="fsOrganization" size="50" value="${accountInstance?.fsOrganization}"/>
</div>

<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'notifyChanges', 'error')}">
	<label for="notifyChanges">
		<g:message code="account.notifyChanges.label" default="Notify Changes" />
	</label>
	<g:checkBox name="notifyChanges" value="${accountInstance?.notifyChanges}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'notifyConferenceChanges', 'error')}">
	<label for="notifyConferenceChanges">
		<g:message code="account.notifyConferenceChanges.label" default="Notify Conference Changes" />
	</label>
	<g:checkBox name="notifyConferenceChanges" value="${accountInstance?.notifyConferenceChanges}"  />
</div>

<sec:ifAnyGranted roles="ROLE_EOC, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_ADMIN, ROLE_FMC_ADMIN">
<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'canAttendConferences', 'error')}">
	<label for="canAttendConferences">
		<g:message code="account.canAttendConferences.label" default="Can Attend Conferences" />
	</label>
	<g:checkBox name="canAttendConferences" value="${accountInstance?.canAttendConferences}"  />
</div>
</sec:ifAnyGranted>
<sec:ifNotGranted roles="ROLE_EOC, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_ADMIN, ROLE_FMC_ADMIN">
<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'canAttendConferences', 'error')}">
	<label for="canAttendConferences">
		<g:message code="account.canAttendConferences.label" default="Can Attend Conferences" />
	</label>
    <g:formatBoolean boolean="${accountInstance?.canAttendConferences}"/>
</div>
</sec:ifNotGranted>



