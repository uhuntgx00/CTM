
<%@ page import="mil.ebs.ctm.Organization" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'organization.label', default: 'TechnicalDirective')}" />
		<title>Organization List</title>
	</head>
	<body>
		<a href="#list-technicalDirective" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-technicalDirective" class="content scaffold-list" role="main">
			<h1>
                <g:img dir="/images/icons" file="info-group_512.png" height="48" width="48" alt="Technical Directive List" title="Technical Directive List"/>
                Organization List (${typeList})&nbsp;&nbsp;[${organizationInstanceCount}]
            </h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
    			<thead>
					<tr>
                        <g:sortableColumn property="officeSymbol" title="${message(code: 'organization.officeSymbol.label', default: 'Office Symbol')}" />
						<g:sortableColumn property="name" title="${message(code: 'organization.name.label', default: 'Name')}" />
                        <th>Accounts</th>
                        <g:sortableColumn property="trueTD" title="${message(code: 'organization.TD.label', default: 'TD')}" />
						<g:sortableColumn property="director" title="${message(code: 'organization.director.label', default: 'Director')}" />
					</tr>
				</thead>
				<tbody>
                    <g:form method="post" action="${indexEvent}">
                        <g:hiddenField name="id" value="${conferenceId}" />
                        <tr class="odd">
                            <td><g:textField name="searchOfficeSymbol" value="${searchOfficeSymbol}" size="30" />&nbsp;<g:actionSubmitImage value="filter" action="${indexEvent}" src="${resource(dir: 'images/icons', file:'search_512.png')}" align="top" width="18" height="18" title="Search for Office Symbol" alt="Search for Office Symbol"/></td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                    </g:form>
                    <g:each in="${organizationInstanceList}" status="i" var="organizationInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td class="no_underline"><g:link action="show" id="${organizationInstance?.id}">${fieldValue(bean: organizationInstance, field: "officeSymbol")}</g:link></td>
                            <td>
                                <g:if test="${organizationInstance?.getParentName()}">
                                    ${organizationInstance?.getParentName()}&nbsp;<span style="color:#999999"><i>(${fieldValue(bean: organizationInstance, field: "name")})</i></span>
                                </g:if>
                                <g:else>
                                    ${fieldValue(bean: organizationInstance, field: "name")}
                                </g:else>
                            </td>
                            <td class="no_underline"><g:link controller="Account" action="listAssigned" params="[id: "${organizationInstance?.id}"]" absolute="true">${organizationInstance?.getAssigned()?.size()}</g:link></td>
                            <td>
                                <g:if test="${organizationInstance?.trueTD}">
                                    True
                                </g:if>
                                <g:else>
                                    <span style="color:#FF0000"><b>FALSE</b></span>
                                </g:else>
                            </td>
                            <td>${fieldValue(bean: organizationInstance, field: "director")}</td>
                        </tr>
                    </g:each>
				</tbody>
			</table>

            <g:if test="${organizationInstanceCount > 25}">
                <div class="pagination">
       				<g:paginate total="${organizationInstanceCount ?: 0}"/>
       			</div>
            </g:if>
		</div>
	</body>
</html>
