<%@ page import="mil.ebs.ctm.Account" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}"/>
        <title><g:message code="default.list.label" args="[entityName]"/></title>
    </head>

    <body>
        <a href="#list-account" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div id="list-account" class="content scaffold-list" role="main">
            <h1>
                <g:img dir="/images/icons" file="info-user_512.png" height="48" width="48" alt="Account List" title="Account List"/>
                <g:message code="default.list.label" args="[entityName]"/>&nbsp;(${listType})&nbsp;&nbsp;[${accountInstanceCount}]
            </h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <table>
                <thead>
                    <tr>
                        <g:sortableColumn property="username" title="${message(code: 'account.username.label', default: 'Username')}"/>
                        <g:sortableColumn property="employeeType" title="${message(code: 'account.type.label', default: 'Type')}"/>
                        <g:sortableColumn property="assignedTD" title="${message(code: 'account.Organization.label', default: 'Organization')}"/>
                        <g:sortableColumn property="emailAddress" title="${message(code: 'account.emailAddress.label', default: 'E-Mail')}"/>
                        <g:sortableColumn property="enabled" title="${message(code: 'account.enabled.label', default: 'Enabled')}"/>
                        <g:sortableColumn property="accountExpired" title="${message(code: 'account.accountExpired.label', default: 'Expired')}"/>
                        <g:sortableColumn property="accountLocked" title="${message(code: 'account.accountLocked.label', default: 'Locked')}"/>
                    </tr>
                </thead>
                <tbody>
                    <g:form method="post" action="${indexEvent}">
                        <g:hiddenField name="id" value="${conferenceId}" />
                        <tr class="odd">
                            <td><g:textField name="searchAccount" value="${searchAccount}" size="50" />&nbsp;<g:actionSubmitImage value="filter" action="${indexEvent}" src="${resource(dir: 'images/icons', file:'search_512.png')}" align="top" width="18" height="18" title="Search for Account" alt="Search for Account"/></td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                    </g:form>
                    <g:each in="${accountInstanceList}" status="i" var="accountInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td class="no_underline"><g:link action="show" id="${accountInstance.id}">${fieldValue(bean: accountInstance, field: "username")}</g:link></td>
                            <td>${fieldValue(bean: accountInstance, field: "employeeType")}</td>
                            <td class="no_underline"><g:link controller="organization" action="show" id="${accountInstance?.assignedTD?.id}">${accountInstance?.assignedTD?.toParentString()}</g:link></td>
                            <td><a href="mailto:${accountInstance?.emailAddress}"><g:img dir="images/icons" file="Email-Sending-512.png" height="16" width="16" alt="Send Email" title="Send Email"/></a>&nbsp;${fieldValue(bean: accountInstance, field: "emailAddress")}</td>
                            <td>
                                <g:if test="${accountInstance?.enabled}">
                                    <g:formatBoolean boolean="${accountInstance.enabled}"/>
                                </g:if>
                                <g:else>
                                    <span style="color:#FF0000"><b><g:formatBoolean boolean="${accountInstance.enabled}"/></b></span>
                                </g:else>
                            </td>
                            <td>
                                <g:if test="${!accountInstance?.accountExpired}">
                                    <g:formatBoolean boolean="${accountInstance.accountExpired}"/>
                                </g:if>
                                <g:else>
                                    <span style="color:#FF0000"><b><g:formatBoolean boolean="${accountInstance.accountExpired}"/></b></span>
                                </g:else>
                            </td>
                            <td>
                                <g:if test="${!accountInstance?.accountLocked}">
                                    <g:formatBoolean boolean="${accountInstance.accountLocked}"/>
                                </g:if>
                                <g:else>
                                    <span style="color:#FF0000"><b><g:formatBoolean boolean="${accountInstance.accountLocked}"/></b></span>
                                </g:else>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>

            <g:if test="${accountInstanceCount > 25}">
                <div class="pagination">
                    <g:if test="${searchAccount}">
                        <g:paginate total="${accountInstanceCount ?: 0}" params="['searchAccount': "${searchAccount}"]"/>
                    </g:if>
                    <g:else>
                        <g:paginate total="${accountInstanceCount ?: 0}"/>
                    </g:else>
                </div>
            </g:if>
        </div>
    </body>
</html>
