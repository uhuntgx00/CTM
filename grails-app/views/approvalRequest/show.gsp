
<%@ page import="mil.ebs.ctm.remove.ApprovalRequest" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'approvalRequest.label', default: 'Conference Request')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-approvalRequest" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-approvalRequest" class="content scaffold-show" role="main">
			<h1>
                <g:message code="default.show.label" args="[entityName]" />
                <br/>Conference: <b><g:link controller="conference" action="show" id="${approvalRequestInstance?.conference?.id}">${approvalRequestInstance?.conference?.encodeAsHTML()}</g:link></b>
                <br/>&nbsp;
            </h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:form url="[resource:approvalRequestInstance, action:'approve']" method="PUT">
                <ol class="property-list approvalRequest">
                    <g:if test="${approvalRequestInstance?.status}">
                    <li class="fieldcontain">
                        <span id="status-label" class="property-label"><g:message code="approvalRequest.status.label" default="Status" /></span>
                        <span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${approvalRequestInstance}" field="status"/></span>
                    </li>
                    </g:if>

                    <g:if test="${approvalRequestInstance?.numAttendees}">
                    <li class="fieldcontain">
                        <span id="numAttendees-label" class="property-label"><g:message code="approvalRequest.numAttendees.label" default="Num Attendees" /></span>
                        <span class="property-value" aria-labelledby="numAttendees-label">
                            <g:fieldValue bean="${approvalRequestInstance}" field="numAttendees"/>
                            &nbsp;<span style="color:#777777">(${approvalRequestInstance?.conference?.attendees?.size()})</span>
                        </span>
                    </li>
                    </g:if>

                    <div class="fieldcontain ${hasErrors(bean: approvalRequestInstance, field: 'numAttendees', 'error')} ">
                        <label for="maxAttendees">
                            <g:message code="approvalRequest.maxAttendees.label" default="Max Attendees" />
                        </label>
                        <g:field name="maxAttendees" id="maxAttendees" size="5" value="${approvalRequestInstance.numAttendees}"/>
                        <g:if test="${approvalRequestInstance?.conference?.attendees && approvalRequestInstance?.conference?.estimateTotal() > 0}">
                            <g:if test="${approvalRequestInstance?.numAttendees}">
                                &nbsp;<span style="color:#777777"><i>(Estimate: $${(approvalRequestInstance?.conference?.estimateTotal()/approvalRequestInstance?.conference?.attendees?.size())*approvalRequestInstance?.numAttendees})</i></span>
                            </g:if>
                        </g:if>
                    </div>

                    <li class="fieldcontain">
                        <span id="totalEstimate-label" class="property-label"><g:message code="approvalRequest.totalEstimate.label" default="Total Estimate" /></span>
                        <span class="property-value" aria-labelledby="totalEstimate-label">
                            <g:formatNumber number="${approvalRequestInstance?.conference?.estimateTotal()}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                            <g:if test="${approvalRequestInstance?.conference?.attendees && approvalRequestInstance?.conference?.estimateTotal() > 0}">
                                &nbsp;<span style="color:#777777"><i>(Average: <g:formatNumber number="${approvalRequestInstance?.conference?.estimateTotal()/approvalRequestInstance?.conference?.attendees?.size()}" type="currency" currencyCode="USD" maxFractionDigits="2" />)</i></span>
                            </g:if>
                        </span>
                    </li>

                    <g:if test="${approvalRequestInstance?.status?.equalsIgnoreCase("Pending")}">
                    <g:if test="${approvalRequestInstance?.approveByDate}">
                    <li class="fieldcontain">
                        <span id="approveByDate-label" class="property-label"><g:message code="approvalRequest.approveByDate.label" default="Approve By Date" /></span>
                        <span class="property-value" aria-labelledby="approveByDate-label">
                            <g:if test="${approvalRequestInstance?.approveByDate < new Date()}">
                                <span style="color:#FF0000"><b><g:formatDate date="${approvalRequestInstance?.approveByDate}" type="date" dateStyle="FULL"/></b></span>
                            </g:if>
                            <g:else>
                                <g:formatDate date="${approvalRequestInstance?.approveByDate}" type="date" dateStyle="FULL"/>
                            </g:else>
                        </span>
                    </li>
                    </g:if>
                    </g:if>

                    <g:if test="${approvalRequestInstance?.approvedBy}">
                    <li class="fieldcontain">
                        <span id="approvedBy-label" class="property-label"><g:message code="approvalRequest.approvedBy.label" default="Approved By" /></span>
                        <span class="property-value" aria-labelledby="approvedBy-label"><g:link controller="account" action="show" id="${approvalRequestInstance?.approvedBy?.id}">${approvalRequestInstance?.approvedBy?.encodeAsHTML()}</g:link></span>
                    </li>
                    </g:if>

                    <g:if test="${approvalRequestInstance?.approvalDate}">
                    <li class="fieldcontain">
                        <span id="approvalDate-label" class="property-label"><g:message code="approvalRequest.approvalDate.label" default="Approval Date" /></span>
                        <span class="property-value" aria-labelledby="approvalDate-label"><g:formatDate date="${approvalRequestInstance?.approvalDate}" /></span>
                    </li>
                    </g:if>
                </ol>

				<fieldset class="buttons">
                    <g:if test="${approvalRequestInstance?.status?.equalsIgnoreCase("Pending")}">
                        <g:actionSubmit class="edit" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" />
                        <g:link class="edit" action="approve" resource="${approvalRequestInstance}"><g:message code="default.button.approve.label" default="Approve" /></g:link>
                    </g:if>
                    <g:if test="${approvalRequestInstance?.status?.equalsIgnoreCase("Pending") || approvalRequestInstance?.status?.equalsIgnoreCase("Approved")}">
                        <g:link class="edit" action="reject" resource="${approvalRequestInstance}"><g:message code="default.button.reject.label" default="Reject" /></g:link>
                    </g:if>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
