<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
		<title>Merge Conferences</title>
	</head>
	<body>
		<a href="#show-conference" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-conference" class="content scaffold-show" role="main">
			<h1>Merge Conferences</h1>
			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>

            <div class="merge_text">
                The selected conferences have been arranged below to reflect their <u>current</u> values. Please select the appropriate value which will be the <u>final</u> value for the <b>MERGED</b> conference.
                <br/><br/><span style="color:#FF0000"><b><i>NOTE:</i></b></span> Values that differ and that are highlighted <b>strongly</b> indicate a potential probability the selected conferences are <b><u>NOT</u></b> an indication they are the same.
                <br/>Please ensure that selected conferences are indeed the same before completing the process - once the <b>MERGE</b> operation has been selected there is no ability to unmerge them.
            </div>

            <br/><center><hr width="90%"/></center>

            <g:form url="[action:'performMerge']" >

                <ol class="property-list conference">
                    <li class="fieldcontain">
                        <span id="conferenceTitle-label" class="property-label"><b><g:message code="conference.conferenceTitle.label" default="Conference Title" /></b></span>
                        <span class="property-value" aria-labelledby="conferenceTitle-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="conferenceTitle" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.conferenceTitle}<br/>
                            </g:each>
                        </span>
                    </li>

                    <li class="fieldcontain">
                        <span id="phaseState-label" class="property-label"><b><g:message code="conference.phaseState.label" default="Phase State" /></b></span>
                        <span class="property-value" aria-labelledby="phaseState-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="phaseState" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.phaseState}<br/>
                            </g:each>
                        </span>
                    </li>

                    <li class="fieldcontain">
                        <span id="status-label" class="property-label"><b><g:message code="conference.status.label" default="Status" /></b></span>
                        <span class="property-value" aria-labelledby="status-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="status" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.status}<br/>
                            </g:each>
                        </span>
                    </li>

                    <li class="fieldcontain">
                        <g:if test="${startDateDiff}"><div class="merge_mismatch"></g:if>
                        <span id="startDate-label" class="property-label">
                            <g:if test="${startDateDiff}">
                                <span style="color:#FF0000"><b><g:message code="conference.startDate.label" default="Start Date" /></b></span>
                            </g:if>
                            <g:else>
                                <b><g:message code="conference.startDate.label" default="Start Date" /></b>
                            </g:else>
                        </span>
                        <span class="property-value" aria-labelledby="startDate-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="startDate" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;<g:formatDate date="${c?.startDate}" type="date" dateStyle="FULL"/><br/>
                            </g:each>
                        </span>
                        <g:if test="${startDateDiff}"></div></g:if>
                    </li>

                    <li class="fieldcontain">
                        <g:if test="${endDateDiff}"><div class="merge_mismatch"></g:if>
                        <span id="endDate-label" class="property-label">
                            <g:if test="${endDateDiff}">
                                <span style="color:#FF0000"><b><g:message code="conference.endDate.label" default="End Date" /></b></span>
                            </g:if>
                            <g:else>
                                <b><g:message code="conference.endDate.label" default="End Date" /></b>
                            </g:else>
                        </span>
                        <span class="property-value" aria-labelledby="endDate-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="endDate" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;<g:formatDate date="${c?.endDate}" type="date" dateStyle="FULL"/><br/>
                            </g:each>
                        </span>
                        <g:if test="${endDateDiff}"></div></g:if>
                    </li>

                    <li class="fieldcontain">
                        <g:if test="${primaryHostDiff}"><div class="merge_mismatch"></g:if>
                        <span id="primaryHost-label" class="property-label">
                            <g:if test="${primaryHostDiff}">
                                <span style="color:#FF0000"><b><g:message code="conference.primaryHost.label" default="Primary Host" /></b></span>
                            </g:if>
                            <g:else>
                                <b><g:message code="conference.primaryHost.label" default="Primary Host" /></b>
                            </g:else>
                        </span>
                        <span class="property-value" aria-labelledby="primaryHost-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="primaryHost" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.primaryHost}<br/>
                            </g:each>
                        </span>
                        <g:if test="${primaryHostDiff}"></div></g:if>
                    </li>

                    <li class="fieldcontain">
                        <g:if test="${venueDiff}"><div class="merge_mismatch"></g:if>
                        <span id="venue-label" class="property-label">
                            <g:if test="${venueDiff}">
                                <span style="color:#FF0000"><b><g:message code="conference.venue.label" default="Venue" /></b></span>
                            </g:if>
                            <g:else>
                                <b><g:message code="conference.venue.label" default="Venue" /></b>
                            </g:else>
                        </span>
                        <span class="property-value" aria-labelledby="venue-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="venue" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.venue}<br/>
                            </g:each>
                        </span>
                        <g:if test="${venueDiff}"></div></g:if>
                    </li>

                    <li class="fieldcontain">
                        <g:if test="${addressDiff}"><div class="merge_mismatch"></g:if>
                        <span id="address-label" class="property-label">
                            <g:if test="${addressDiff}">
                                <span style="color:#FF0000"><b><g:message code="conference.venueAddress.label" default="Venue Address" /></b></span>
                            </g:if>
                            <g:else>
                                <b><g:message code="conference.venueAddress.label" default="Venue Address" /></b>
                            </g:else>
                        </span>
                        <span class="property-value" aria-labelledby="address-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:if test="${c?.address}">
                                    <g:radio name="address" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.address?.street1},&nbsp;${c?.address?.city}<g:if test="${c?.address?.state}">&nbsp;${c?.address?.state}</g:if>,&nbsp;<g:country code="${c?.address?.country}"/>&nbsp;&nbsp;${c?.address?.zipCode}<br/>
                                </g:if>
                            </g:each>
                        </span>
                        <g:if test="${addressDiff}"></div></g:if>
                    </li>

                    <li class="fieldcontain">
                        <g:if test="${websiteDiff}"><div class="merge_mismatch"></g:if>
                        <span id="website-label" class="property-label">
                            <g:if test="${websiteDiff}">
                                <span style="color:#FF0000"><b><g:message code="conference.website.label" default="Website" /></b></span>
                            </g:if>
                            <g:else>
                                <b><g:message code="conference.website.label" default="Website" /></b>
                            </g:else>
                        </span>
                        <span class="property-value" aria-labelledby="website-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="website" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.website}&nbsp;<br/>
                            </g:each>
                        </span>
                        <g:if test="${websiteDiff}"></div></g:if>
                    </li>

                    <li class="fieldcontain">
                        <span id="purpose-label" class="property-label"><b><g:message code="conference.purpose.label" default="Purpose" /></b></span>
                        <span class="property-value" aria-labelledby="purpose-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="purpose" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.purpose}<br/>
                            </g:each>
                        </span>
                    </li>

                    <li class="fieldcontain">
                        <g:if test="${hostTypeDiff}"><div class="merge_mismatch"></g:if>
                        <span id="hostType-label" class="property-label">
                            <g:if test="${hostTypeDiff}">
                                <span style="color:#FF0000"><b><g:message code="conference.hostType.label" default="Host Type" /></b></span>
                            </g:if>
                            <g:else>
                                <b><g:message code="conference.hostType.label" default="Host Type" /></b>
                            </g:else>
                        </span>
                        <span class="property-value" aria-labelledby="hostType-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="hostType" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.hostType}<br/>
                            </g:each>
                        </span>
                        <g:if test="${hostTypeDiff}"></div></g:if>
                    </li>

                    <li class="fieldcontain">
                        <g:if test="${primarySponsorDiff}"><div class="merge_mismatch"></g:if>
                        <span id="primarySponsor-label" class="property-label">
                            <g:if test="${primarySponsorDiff}">
                                <span style="color:#FF0000"><b><g:message code="conference.primarySponsor.label" default="Primary Sponsor" /></b></span>
                            </g:if>
                            <g:else>
                                <b><g:message code="conference.primarySponsor.label" default="Primary Sponsor" /></b>
                            </g:else>
                        </span>
                        <span class="property-value" aria-labelledby="primarySponsor-label">
                            <g:each in="${conferences}" status="i" var="c">
                                <g:radio name="primarySponsor" value="${c?.id}" checked="${i==0}"/>&nbsp;<span style="color:#0014BF"><b>${i+1})</b></span>&nbsp;${c?.primarySponsor}<br/>
                            </g:each>
                        </span>
                        <g:if test="${primarySponsorDiff}"></div></g:if>
                    </li>
                </ol>

                <g:hiddenField name="confs" value="${confs}"/>
				<fieldset class="buttons">
                    <g:actionSubmit class="save" action="performMerge" value="${message(code: 'default.button.merge.label', default: 'Merge')}" onclick="return confirm('${message(code: 'default.button.merge.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
