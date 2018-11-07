<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.Attendee; mil.ebs.ctm.Organization" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'organization.label', default: 'TechnicalDirective')}" />
		<title>Show Organization</title>
        <g:javascript library="raphael"/>
        <r:require modules="animate"/>
        <g:render template="raphael"/>
	</head>
	<body>
		<a href="#show-technicalDirective" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-technicalDirective" class="content scaffold-show" role="main">
            <g:render template="statusBlock"/>

			<h1>
                <g:img dir="/images/icons" file="group_512.png" width="48" height="48" alt="Group" title="Group"/>
                Show Organization
                <g:if test="${organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
                    <g:if test="${organizationInstance?.supervisorApprovalRequired && organizationInstance?.attendeeRequestRequired}">
                        <g:img dir="/images/icons" file="Shield_1b-512.png" width="32" height="32" alt="Supervisor Approval AND TD Concurrence Required" title="Supervisor Approval AND TD Concurrence Required"/>
                    </g:if>
                    <g:else>
                        <g:if test="${organizationInstance?.supervisorApprovalRequired}"><g:img dir="/images/icons" file="Shield_1a-512.png" width="32" height="32" alt="Supervisor Approval Required" title="Supervisor Approval Required"/></g:if>
                        <g:if test="${organizationInstance?.attendeeRequestRequired}"><g:img dir="/images/icons" file="Shield_1-512.png" width="32" height="32" alt="Attendee TD Concurrence" title="Attendee TD Concurrence"/></g:if>
                    </g:else>
                    %{--<g:if test="${organizationInstance?.allowCreateConference}"><g:img dir="/images/icons" file="Shield_2-512.png" width="32" height="32" alt="Allow Create Conference" title="Allow Create Conference"/></g:if>--}%
                    <g:if test="${organizationInstance?.allowAttendeeNotification}"><g:img dir="/images/icons" file="Email-Sending-512.png" width="32" height="32" alt="Allow Attendee Notification" title="Allow Attendee Notification"/></g:if>
                </g:if>
            </h1>

			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list technicalDirective">
                %{--<g:if test="$organizationInstance?.officeSymbol}">--}%
                    %{--<g:img dir="/images/td" file="${organizationInstance?.officeSymbol}_logo.jpg"/>--}%
                %{--</g:if>--}%

                <div class="account-block-blue">
                    <g:if test="${organizationInstance?.trueTD}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-red">TD</div>
                        </div>
                    </g:if>
                    <g:if test="${organizationInstance?.levelTD?.equalsIgnoreCase("2")}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-red">Division</div>
                        </div>
                    </g:if>
                    <g:if test="${organizationInstance?.levelTD?.equalsIgnoreCase("3")}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-red">Branch</div>
                        </div>
                    </g:if>
                    <g:if test="${organizationInstance?.levelTD?.equalsIgnoreCase("4")}">
                        <div class="ribbon-wrapper">
                            <div class="ribbon-red">Section</div>
                        </div>
                    </g:if>

                    <g:if test="${organizationInstance?.name}">
                    <li class="fieldcontain">
                        <span id="name-label" class="property-label"><g:message code="organization.name.label" default="Name" /></span>
                        <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${organizationInstance}" field="name"/></span>
                    </li>
                    </g:if>

                    <g:if test="${organizationInstance?.officeSymbol}">
                    <li class="fieldcontain">
                        <span id="officeSymbol-label" class="property-label"><g:message code="organization.officeSymbol.label" default="Office Symbol" /></span>
                        <span class="property-value" aria-labelledby="officeSymbol-label">
                            <g:if test="${organizationInstance?.levelTD?.equals("4")}">
                                ${organizationInstance?.officeSymbol}
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.id}" absolute="true">${organizationInstance?.parent?.officeSymbol}</g:link>
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.parent?.id}" absolute="true">${organizationInstance?.parent?.parent?.officeSymbol}</g:link>
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.parent?.parent?.id}" absolute="true">${organizationInstance?.parent?.parent?.parent?.officeSymbol}</g:link>
                                <g:if test="${organizationInstance?.parent?.parent?.parent?.levelTD?.equals("1") && organizationInstance?.parent?.parent?.parent?.parent}">
                                    &nbsp;&nbsp;>>&nbsp;&nbsp;
                                    <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.parent?.parent?.parent?.id}" absolute="true">${organizationInstance?.parent?.parent?.parent?.parent?.officeSymbol}</g:link>
                                </g:if>
                            </g:if>
                            <g:elseif test="${organizationInstance?.levelTD?.equals("3")}">
                                ${organizationInstance?.officeSymbol}
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.id}" absolute="true">${organizationInstance?.parent?.officeSymbol}</g:link>
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.parent?.id}" absolute="true">${organizationInstance?.parent?.parent?.officeSymbol}</g:link>
                                <g:if test="${organizationInstance?.parent?.parent?.levelTD?.equals("1") && organizationInstance?.parent?.parent?.parent}">
                                    &nbsp;&nbsp;>>&nbsp;&nbsp;
                                    <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.parent?.parent?.id}" absolute="true">${organizationInstance?.parent?.parent?.parent?.officeSymbol}</g:link>
                                </g:if>
                            </g:elseif>
                            <g:elseif test="${organizationInstance?.levelTD?.equals("2")}">
                                ${organizationInstance?.officeSymbol}
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.id}" absolute="true">${organizationInstance?.parent?.officeSymbol}</g:link>
                                <g:if test="${organizationInstance?.parent?.levelTD?.equals("1") && organizationInstance?.parent?.parent}">
                                    &nbsp;&nbsp;>>&nbsp;&nbsp;
                                    <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.parent?.id}" absolute="true">${organizationInstance?.parent?.parent?.officeSymbol}</g:link>
                                </g:if>
                            </g:elseif>
                            <g:elseif test="${organizationInstance?.levelTD?.equals("1") && organizationInstance?.parent}">
                                ${organizationInstance?.officeSymbol}
                                &nbsp;&nbsp;>>&nbsp;&nbsp;
                                <g:link controller="Organization" action="show" id="${organizationInstance?.parent?.id}" absolute="true">${organizationInstance?.parent?.officeSymbol}</g:link>
                            </g:elseif>
                            <g:else>
                                ${organizationInstance?.officeSymbol}
                            </g:else>
                        </span>
                    </li>
                    </g:if>

                    <g:if test="${organizationInstance?.director}">
                    <li class="fieldcontain">
                        <span id="director-label" class="property-label"><g:message code="organization.director.label" default="Director" /></span>
                        <span class="property-value" aria-labelledby="director-label">
                            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                <g:link controller="Account" action="show" id="${organizationInstance?.director?.id}" absolute="true">${organizationInstance?.director}</g:link>
                            </sec:ifAnyGranted>
                            <sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                ${organizationInstance?.director}
                            </sec:ifNotGranted>
                        </span>
                    </li>
                    </g:if>

                    <g:if test="${organizationInstance?.lastChangeDate}">
                    <li class="fieldcontain">
                        <span id="lastChangeDate-label" class="property-label"><g:message code="organization.lastChangeDate.label" default="Last Change" /></span>
                        <span class="property-value" aria-labelledby="lastChangeDate-label">
                            <g:formatDate date="${organizationInstance?.lastChangeDate}" type="date" style="MEDIUM"/>
                            <span style="color:#999999"><i>(${organizationInstance?.getChangeDays()} days)</i></span>
                            <g:if test="${organizationInstance?.lastChange}">
                                &nbsp;-&nbsp;<span style="color:#006dba"><i>${organizationInstance?.lastChange}</i></span>
                            </g:if>
                        </span>
                    </li>
                    </g:if>
                </div>

                <g:if test="${organizationInstance?.missionStatement}">
                    <br/>
                    <div class="account-block">
                        <li class="fieldcontain">
                            <span id="missionStatement-label" class="property-label"><g:message code="organization.missionStatement.label" default="Mission Statement" /></span>
                            <span class="property-value" aria-labelledby="missionStatement-label">
                                ${organizationInstance?.missionStatement}
                            </span>
                        </li>
                    </div>
                </g:if>

                <g:if test="${organizationInstance?.trueTD && organizationInstance?.getChildren(0)?.size() > 0}">
                    <br/>
                    <div class="account-block">
                        <li class="fieldcontain">
                            <span id="childrenA-label" class="property-label">
                                <g:if test="${organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
                                    <g:message code="organization.directorates.label" default="Directorates" />&nbsp;<b>(${organizationInstance?.getChildren(0)?.size()})</b>
                                </g:if>
                            </span>
                            <span class="property-value" aria-labelledby="childrenA-label">
                                <g:each in="${organizationInstance?.getChildren(0)}" var="c">
                                    <g:link controller="Organization" action="show" id="${c.id}">${c?.officeSymbol}</g:link>&nbsp;<i>(${c?.name})</i><br/>
                                </g:each>
                            </span>
                        </li>
                    </div>
                </g:if>

                <g:if test="${organizationInstance?.trueTD && organizationInstance?.getChildren(1)?.size() > 0}">
                    <br/>
                    <div class="account-block">
                        <li class="fieldcontain">
                            <span id="children-label" class="property-label">
                                <g:if test="${organizationInstance?.levelTD?.equalsIgnoreCase("2")}">
                                    <g:message code="organization.branches.label" default="Branches" />&nbsp;<b>(${organizationInstance?.getChildren(1)?.size()})</b>
                                </g:if>
                                <g:elseif test="${organizationInstance?.levelTD?.equalsIgnoreCase("3")}">
                                    <g:message code="organization.sections.label" default="Sections" />&nbsp;<b>(${organizationInstance?.getChildren(1)?.size()})</b>
                                </g:elseif>
                                <g:else>
                                    <g:message code="organization.divisions.label" default="Divisions" />&nbsp;<b>(${organizationInstance?.getChildren(1)?.size()})</b>
                                </g:else>
                            </span>
                            <span class="property-value" aria-labelledby="children-label">
                                <g:each in="${organizationInstance?.getChildren(1)}" var="c">
                                    <g:link controller="Organization" action="show" id="${c.id}">${c?.officeSymbol}</g:link>&nbsp;<i>(${c?.name})</i><br/>
                                </g:each>
                            </span>
                        </li>
                    </div>
                </g:if>

				<g:if test="${organizationInstance?.contacts}">
                    <br/>
                    <div class="account-block">
                        <li class="fieldcontain">
                            <span id="contacts-label" class="property-label">
                                <g:message code="organization.contacts.label" default="Contacts" />&nbsp;<b>(${organizationInstance?.contacts?.size()})</b>
                            </span>
                            <g:each in="${organizationInstance?.contacts}" var="c">
                                <span class="property-value" aria-labelledby="contacts-label">
                                    <g:link controller="accountContact" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link>
                                    <a style="vertical-align: middle" href="mailto:${c?.accountLink?.emailAddress}"><g:img style="vertical-align: middle" dir="/images/icons" file="mail_512.png" width="24" height="24" alt="Send Mail" title="Send Mail"/></a>
                                    <g:if test="${c?.primaryPOC}">&nbsp;<g:img dir="images/icons" file="ok_512.png" height="16" width="16" alt="Primary POC" title="Primary POC"/></g:if>
                                </span>
                            </g:each>
                        </li>
                    </div>
                </g:if>

                <g:if test="${assignedList}">
                    <br/>
                    <div class="account-block">
                        <h1><center><a href="javascript:animatedcollapse.show('assigned_fold')"><g:img class="toggle" dir="/images/icons" file="download_512.png" alt="Show Assigned" title="Show Assigned" height="32" width="32"/></a><span class="toggle">&nbsp;Assigned&nbsp;<b>(${assignedListCount})&nbsp;</b></span><a href="javascript:animatedcollapse.hide('assigned_fold')"><g:img dir="/images/icons" file="upload_512.png" alt="Hide Assigned" title="Hide Assigned" height="32" width="32"/></a></center></h1>
                        <div id="assigned_fold" style="display:block">
                            <li class="fieldcontain">
                                <span id="assigned-label" class="property-label">
                                    %{--<g:message code="organization.assigned.label" default="Assigned" />&nbsp;<b>(${organizationInstance?.getAssigned()?.size()})</b>--}%
                                </span>
                                <g:each in="${assignedList}" var="c">
                                    <span class="property-value" aria-labelledby="assigned-label">
                                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                            <g:link controller="Account" action="show" id="${c.id}" absolute="true">${c?.encodeAsHTML()}</g:link>
                                        </sec:ifAnyGranted>
                                        <sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                            ${c?.encodeAsHTML()}
                                        </sec:ifNotGranted>
                                        <b>[${c?.assignedTD?.officeSymbol}]</b>
                                    </span>
                                </g:each>
                            </li>
                            <br/>
                            <g:if test="${assignedListCount > 25}">
                                <div class="pagination">
                                    <g:paginate total="${assignedListCount ?: 0}" max="25" offset="${session.assignedPagination?.offset}" params="${[paginate:'assigned']}" id="${organizationInstance?.id}" action="show"/>
                                </div>
                            </g:if>
                        </div>
                    </div>
                </g:if>

                <g:if test="${totalAssignedList && (assignedListCount != totalAssignedListCount)}">
                    <br/>
                    <div class="account-block">
                        <h1><center><a href="javascript:animatedcollapse.show('total_assigned_fold')"><g:img class="toggle" dir="/images/icons" file="download_512.png" alt="Show Total Assigned" title="Show Total Assigned" height="32" width="32"/></a><span class="toggle">&nbsp;Total Assigned&nbsp;<b>(${totalAssignedListCount})&nbsp;</b></span><a href="javascript:animatedcollapse.hide('total_assigned_fold')"><g:img dir="/images/icons" file="upload_512.png" alt="Hide Total Assigned" title="Hide Total Assigned" height="32" width="32"/></a></center></h1>
                        <div id="total_assigned_fold" style="display:block">
                            <li class="fieldcontain">
                                <span id="assigned-label" class="property-label">
                                    %{--<g:message code="organization.assigned.label" default="Assigned" />&nbsp;<b>(${organizationInstance?.getAssigned()?.size()})</b>--}%
                                </span>
                                <g:each in="${totalAssignedList}" var="c">
                                    <span class="property-value" aria-labelledby="assigned-label">
                                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                            <g:link controller="Account" action="show" id="${c.id}" absolute="true">${c?.encodeAsHTML()}</g:link>
                                        </sec:ifAnyGranted>
                                        <sec:ifNotGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                                            ${c?.encodeAsHTML()}
                                        </sec:ifNotGranted>
                                        <b>[${c?.assignedTD?.officeSymbol}]</b>
                                    </span>
                                </g:each>
                            </li>
                            <br/>
                            <g:if test="${totalAssignedListCount > 25}">
                                <div class="pagination">
                                    <g:paginate total="${totalAssignedListCount ?: 0}" max="25" offset="${session.totalAssignedPagination?.offset}" params="${[paginate:'totalAssigned']}" id="${organizationInstance?.id}" action="show"/>
                                </div>
                            </g:if>
                        </div>
                    </div>
                </g:if>

			</ol>

			<g:form url="[resource:organizationInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
                    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                        <g:if test="${organizationInstance?.canEdit()}">
                            <g:link class="edit" action="edit" resource="${organizationInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        </g:if>
                    </sec:ifAnyGranted>
                    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                        <g:if test="${organizationInstance?.trueTD && organizationInstance?.levelTD?.equalsIgnoreCase("1")}">
                            <g:link class="save" action="addDivision" absolute="true" resource="${organizationInstance}" params="[parentId: "${organizationInstance?.id}"]">Add Division</g:link>
                        </g:if>
                        <g:if test="${organizationInstance?.trueTD && organizationInstance?.levelTD?.equalsIgnoreCase("2")}">
                            <g:link class="save" action="addBranch" absolute="true" resource="${organizationInstance}" params="[parentId: "${organizationInstance?.id}"]">Add Branch</g:link>
                        </g:if>
                        <g:if test="${organizationInstance?.trueTD && organizationInstance?.levelTD?.equalsIgnoreCase("3")}">
                            <g:link class="save" action="addSection" absolute="true" resource="${organizationInstance}" params="[parentId: "${organizationInstance?.id}"]">Add Section</g:link>
                        </g:if>
                    </sec:ifAnyGranted>
                    <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN">
                        <g:if test="${!organizationInstance?.trueTD || organizationInstance?.levelTD?.equalsIgnoreCase("4") ||
                                (organizationInstance?.levelTD?.equalsIgnoreCase("3") && !organizationInstance?.getChildren(1)?.size()) ||
                                (organizationInstance?.levelTD?.equalsIgnoreCase("2") && !organizationInstance?.getChildren(1)?.size() && !organizationInstance?.getChildren(2)?.size()) ||
                                (organizationInstance?.levelTD?.equalsIgnoreCase("1") && !organizationInstance?.getChildren(1)?.size() && !organizationInstance?.getChildren(2)?.size() && !organizationInstance?.getChildren(3)?.size())}">
                            <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                        </g:if>
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
		</div>

        <script type="text/javascript">
            $(document).ready(function(){
                animatedcollapse.addDiv('assigned_fold');
                animatedcollapse.addDiv('total_assigned_fold');
                animatedcollapse.ontoggle=function($, divobj, state) {};

                animatedcollapse.init()
            });
        </script>
	</body>
</html>


