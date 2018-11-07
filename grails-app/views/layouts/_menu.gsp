<%@ page import="org.springframework.security.core.context.SecurityContextHolder; mil.ebs.ctm.Account" %>

<div>
    <span class="preload1"></span>
    <span class="preload2"></span>

    <ul id="ctmnav">
        <li class="top">
            <a tabindex="-1" id="home" class="top_link" href="${createLink(uri: '/', absolute: 'true')}"><span><g:message code="default.home.label"/></span></a>
        </li>

        <sec:ifLoggedIn>
        <li class="top">
            <a tabindex="-1" href="#nogo3" id="accountMenu" class="top_link"><span class="down">Accounts</span></a>
            <ul class="sub">
                <sec:ifAnyGranted roles="ROLE_EOC">
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="create" action="create" absolute="true">Create Account</g:link></li>
                    %{--<li class="controller"><g:link tabindex="-1" controller="Account" class="create" action="createLdap" absolute="true">Create Account (LDAP)</g:link></li>--}%
                    <li>&nbsp;-------------</li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_EOC, ROLE_FMC_ADMIN">
                    <li class="controller"><g:link tabindex="-1" controller="AccountActivity" absolute="true">Account Activity</g:link></li>
                    <li>&nbsp;-------------</li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_EOC, ROLE_FMC_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN">
                    <li class="controller"><g:link tabindex="-1" controller="Account" absolute="true">Account List (ALL)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listBase" absolute="true">Account List (Civilian/Military)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listCivilian" absolute="true">Account List (Civilian)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listMilitary" absolute="true">Account List (Military)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listContractor" absolute="true">Account List (Contractor)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listOther" absolute="true">Account List (Other)</g:link></li>
                    <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_EOC">
                        <li>&nbsp;-------------</li>
                    </sec:ifAnyGranted>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_EOC, ROLE_ADMIN, ROLE_DEVELOPER">
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listUnassigned" absolute="true">Account List (Unassigned)</g:link></li>
                    <li>&nbsp;-------------</li>
                </sec:ifAnyGranted>
                <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listSupervising" absolute="true">Account List (Supervising)</g:link></li>
                <sec:ifAnyGranted roles="ROLE_EOC">
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="account" absolute="true">Account List (Login)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listDisabled" absolute="true">Account List (Disabled)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listExpired" absolute="true">Account List (Expired)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listLocked" absolute="true">Account List (Locked)</g:link></li>
                    <li>&nbsp;-------------</li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listEOC" absolute="true">Account List (EOC)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listADMIN" absolute="true">Account List (ADMIN)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listDEV" absolute="true">Account List (DEVELOPER)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="list" action="listEXPIRE" absolute="true">Account List (EXPIRE)</g:link></li>
                    <li>&nbsp;-------------</li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_EOC, ROLE_ADMIN, ROLE_DEVELOPER">
                    <li class="controller"><g:link tabindex="-1" controller="Account" class="create" action="processSync" absolute="true">LDAP Sync (Manual)</g:link></li>
                </sec:ifAnyGranted>
            </ul>
        </li>
        </sec:ifLoggedIn>

        <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_EOC, ROLE_FMC_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN">
        <li class="top">
            <a tabindex="-1" href="#nogo1" id="adminMenu" class="top_link"><span class="down">Admin</span></a>
            <ul class="sub">
                <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_FMC_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN">
                    <li><a tabindex="-1" href="#nogo1a" class="fly">Mail System</a>
                        <ul>
                            <li class="controller"><g:link tabindex="-1" controller="MailTemplate" absolute="true">Mail Template List</g:link></li>
                            <li class="controller"><g:link tabindex="-1" controller="ContentBlock" absolute="true">Content Block List</g:link></li>
                            <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_FMC_ADMIN">
                                <li class="controller"><g:link tabindex="-1" controller="MailTemplate" action="create" absolute="true">Create Mail Template</g:link></li>
                                <li class="controller"><g:link tabindex="-1" controller="ContentBlock" action="create" absolute="true">Create Content Block</g:link></li>
                            </sec:ifAnyGranted>
                        </ul>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_EOC">
                    <li><a tabindex="-1" href="#nogo2" class="fly">Roles</a>
                        <ul>
                            <li class="controller"><g:link tabindex="-1" controller="Role" absolute="true">Role List</g:link></li>
                        </ul>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_ADMIN, ROLE_FMC_ADMIN">
                    <li><a tabindex="-1" href="#nogo4b" class="fly">Link Management</a>
                        <ul>
                            <li class="controller"><g:link tabindex="-1" controller="Link" absolute="true">Link List</g:link></li>
                            <li class="controller"><g:link tabindex="-1" controller="Link" class="create" action="create" absolute="true">Create Link</g:link></li>
                        </ul>
                    </li>
                    <li><a tabindex="-1" href="#nogo5" class="fly">Reference</a>
                        <ul>
                            <li class="controller"><g:link tabindex="-1" controller="RefAttendeeState" absolute="true">Attendee State List</g:link></li>
                            <li class="controller"><g:link tabindex="-1" controller="RefPhase" absolute="true">Phase List</g:link></li>
                            <li class="controller"><g:link tabindex="-1" controller="RefPhaseState" absolute="true">Phase State List</g:link></li>
                            <li class="controller"><g:link tabindex="-1" controller="RefDateGate" absolute="true">Date Gate List</g:link></li>
                            <li class="controller"><g:link tabindex="-1" controller="RefRankGrade" absolute="true">Rank/Grade List</g:link></li>
                        </ul>
                    </li>
                </sec:ifAnyGranted>
            </ul>
        </li>
        </sec:ifAnyGranted>	  

        <sec:ifAnyGranted roles="ROLE_DEVELOPER">
        <li class="top">
            <a tabindex="-1" href="#nogo1d" id="devMenu" class="top_link"><span class="down">Developer</span></a>
            <ul class="sub">
                <li><a tabindex="-1" href="#nogo2d" class="fly">Roles</a>
                    <ul>
                        <li class="controller"><g:link tabindex="-1" controller="Role" absolute="true">Role List</g:link></li>
                        <li class="controller"><g:link tabindex="-1" controller="Role" class="create" action="create" absolute="true">Create Role</g:link></li>
                    </ul>
                </li>
                <li><a tabindex="-1" href="#nogo5d" class="fly">Reference</a>
                    <ul>
                        <li class="controller"><g:link tabindex="-1" controller="RefAttendeeState" class="create" action="create" absolute="true">Create Attendee State</g:link></li>
                        <li class="controller"><g:link tabindex="-1" controller="RefPhaseState" class="create" action="create" absolute="true">Create Phase State</g:link></li>
                        <li class="controller"><g:link tabindex="-1" controller="RefDateGate" class="create" action="create">Create Date Gate</g:link></li>
                        <li class="controller"><g:link tabindex="-1" controller="RefRankGrade" class="create" action="create">Create Rank/Grade</g:link></li>
                    </ul>
                </li>
                <li><a tabindex="-1" href="#nogo6d" class="fly">Access</a>
                    <ul>
                        <li class="controller"><g:link tabindex="-1" controller="Login" absolute="true">Login</g:link></li>
                        <li class="controller"><g:link tabindex="-1" controller="Logout" absolute="true">Logout</g:link></li>
                    </ul>
                </li>
            </ul>
        </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
        <li class="top">
            <a tabindex="-1" href="#nogo4b" id="orgMenu" class="top_link"><span class="down">Orgs</span></a>
            <ul class="sub">
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_DEVELOPER">
                    <li class="controller"><g:link tabindex="-1" controller="Organization" class="create" action="create">Create Organization</g:link></li>
                    <li>&nbsp;-------------</li>
                </sec:ifAnyGranted>
                <li class="controller"><g:link tabindex="-1" controller="Organization" absolute="true">Organization List (ALL)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="Organization" action="tdList" absolute="true">Organization List (TD)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="Organization" action="nonAfrlList" absolute="true">Organization List (Non-AFRL)</g:link></li>
            </ul>
        </li>
        </sec:ifAnyGranted>

        <li class="top">
            <a tabindex="-1" href="#nogo7" id="attendeeMenu" class="top_link"><span class="down">Attendee</span></a>
            <ul class="sub">
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                    <li class="controller"><g:link tabindex="-1" controller="attendee" absolute="true">Attendee List (ALL)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="attendee" action="waitList" absolute="true">Attendee List (Wait List)</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                    <li class="controller"><g:link tabindex="-1" controller="attendee" action="registeredList" absolute="true">Attendee List (Registered)</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                    <li class="controller"><g:link tabindex="-1" controller="attendee" action="confirmedList" absolute="true">Attendee List (Confirmed)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="attendee" action="attendedList" absolute="true">Attendee List (Attended)</g:link></li>
                    <li>&nbsp;-------------</li>
                </sec:ifAnyGranted>
                <li class="controller"><g:link tabindex="-1" controller="attendee" action="supervisorList" absolute="true">Attendee List (Supervisor)</g:link></li>
                <sec:ifAnyGranted roles="ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
                    <li class="controller"><g:link tabindex="-1" controller="attendee" action="supervisorListTD" absolute="true">Attendee List (TD Supervisor)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="attendee" action="requestingList" absolute="true">Attendee List (TD Concurrence)</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_FMC_ADMIN">
                    <li>&nbsp;-------------</li>
                    <li class="controller"><g:link tabindex="-1" controller="attendee" action="supervisorListAll" absolute="true">ALL List (Supervisor)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="attendee" action="requestingListAll" absolute="true">ALL List (TD Concurrence)</g:link></li>
                </sec:ifAnyGranted>
            </ul>
        </li>

        <sec:ifAnyGranted roles="ROLE_USER, ROLE_AFRL_USER, ROLE_NON_AFRL_USER, ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
        <li class="top">
            <a tabindex="-1" href="#nogo8" id="conferenceMenu" class="top_link"><span class="down">Conference</span></a>
            <ul class="sub">
                %{--<li class="controller"><g:link tabindex="-1" controller="conference" action="advancedSearch" absolute="true">Advanced Search</g:link></li>--}%
                <sec:ifAnyGranted roles="ROLE_AFRL_USER, ROLE_FMC_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_ADMIN">
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="create" absolute="true">Add Conference</g:link></li>
                </sec:ifAnyGranted>
                <li>&nbsp;-------------</li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="viewMy" absolute="true">View My Conferences</g:link></li>
                <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_AFRL_USER, ROLE_NON_AFRL_USER, ROLE_FMC_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_ADMIN">
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="view" absolute="true">View Conferences</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_FMC_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN">
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="viewConstrained" absolute="true">View Conferences (C)</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_DEVELOPER, ROLE_AFRL_USER, ROLE_NON_AFRL_USER, ROLE_FMC_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_ADMIN">
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="vmap" absolute="true">Map Conferences (World)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="vmap_usa" absolute="true">Map Conferences (USA)</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
                    <li>&nbsp;-------------</li>
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="allList" absolute="true">Conference List (ALL)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="externalList" absolute="true">Conference List (External)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="pendingList" absolute="true">Conference List (Pending)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="costs" absolute="true">Conference List (C Costs)</g:link></li>
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="ucosts" absolute="true">Conference List (U Costs)</g:link></li>
                </sec:ifAnyGranted>
                <li>&nbsp;-------------</li>
                <li class="controller"><g:link tabindex="-1" controller="conference" absolute="true">Conference List (Active)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="openList" absolute="true">Conference List (Open)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="draftingList" absolute="true">Conference List (Drafting)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="reviewingList" absolute="true">Conference List (Reviewing)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="processingList" absolute="true">Conference List (Processing)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="approvedList" absolute="true">Conference List (Approved)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="finalizeList" absolute="true">Conference List (Finalizing)</g:link></li>
                <li>&nbsp;-------------</li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="cancelledList" absolute="true">Conference List (Cancelled)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="disapprovedList" absolute="true">Conference List (Disapproved)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="archivedList" absolute="true">Conference List (Archived)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="conference" action="closedList" absolute="true">Conference List (Closed)</g:link></li>
                <sec:ifAnyGranted roles="ROLE_FMC_ADMIN">
                    <li>&nbsp;-------------</li>
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="hidden" absolute="true">Conference List (Hidden)</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_ADMIN">
                    <li>&nbsp;-------------</li>
                    <li class="controller"><g:link tabindex="-1" controller="conference" action="errorList" absolute="true">Conference List (Error)</g:link></li>
                </sec:ifAnyGranted>
            </ul>
        </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_NEWS">
        <li class="top">
            <a tabindex="-1" href="#nogo9" id="newsMenu" class="top_link"><span class="down">News</span></a>
            <ul class="sub">
                <li class="controller"><g:link tabindex="-1" controller="NewsItem" action="index" absolute="true">Latest News</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="NewsItem" action="create" absolute="true">Add News</g:link></li>
            </ul>
        </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_REPORTS">
        <li class="top">
            <a tabindex="-1" tabindex="-1" href="#nogo10" id="reportMenu" class="top_link"><span class="down">Reports</span></a>
            <ul class="sub">
                <li class="controller"><g:link tabindex="-1" controller="report" action="reports" absolute="true">Reports</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="report" action="conferences" absolute="true">Conferences (PPTX)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="report" action="attendanceType" absolute="true">Attendance Type (PPTX)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="report" action="attendees" absolute="true">Attendee Report (PPTX)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="report" action="conferenceCost" absolute="true">Cost Report (PPTX)</g:link></li>
                <li>&nbsp;-------------</li>
                <li class="controller"><g:link tabindex="-1" controller="report" action="attendanceChart" absolute="true">Attendance Type (ALL)</g:link></li>
                <li class="controller"><g:link tabindex="-1" controller="report" action="costChart" absolute="true">Cost Type (ALL)</g:link></li>
            </ul>
        </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_UPLOAD">
            <li class="top"><a tabindex="-1" href="#nogo11" id="upload" class="top_link"><span class="down">Upload</span></a>
                <ul class="sub">
                    <li class="controller"><g:link tabindex="-1" controller="ConferenceUpload" absolute="true">Conference Data</g:link></li>
                </ul>
            </li>
        </sec:ifAnyGranted>

        <sec:ifLoggedIn>
        <li class="top" style="clear: right; float: right; text-align: right; margin-right:20px">

            %{--${Account.get(springSecurityService?.principal?.id)}--}%
            %{----}%
            <g:link tabindex="-1" class="top_link" controller="Account" action="show" absolute="true" id="${SecurityContextHolder?.context?.authentication?.principal?.id}">
                <span>${(applicationContext.springSecurityService.getCurrentUser())}</span>
            </g:link>
            %{----}%
            %{--<li class="top">--}%
                %{--<a tabindex="-1" id="home" class="top_link" href="${createLink(uri: '/', absolute: 'true')}"><span><g:message code="default.home.label"/></span></a>--}%
            %{--</li>--}%

            %{--${SecurityContextHolder?.context?.authentication}--}%

            %{--<g:link tabindex="-1" class="top_link" controller="Account" action="currentAuth" absolute="true">--}%
                %{--<span>${(applicationContext.springSecurityService.getCurrentUser())}</span>--}%
            %{--</g:link>--}%
        </li>
        </sec:ifLoggedIn>
    </ul>
</div>
