
<%@ page import="mil.ebs.ctm.Address" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'address.label', default: 'Address')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-address" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-address" class="content scaffold-show" role="main">
			<h1>
                Show <b>${addressInstance?.addressType}</b> Address
                <br/>Conference: <g:link controller="conference" action="show" id="${addressInstance?.conference?.id}"><b>${addressInstance?.conference}</b></g:link>
                <br/>&nbsp;
            </h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list address">
				<g:if test="${addressInstance?.street1}">
				<li class="fieldcontain">
					<span id="street1-label" class="property-label"><g:message code="address.street1.label" default="Street (1)" /></span>
					<span class="property-value" aria-labelledby="street1-label"><g:fieldValue bean="${addressInstance}" field="street1"/></span>
				</li>
				</g:if>
			
				<g:if test="${addressInstance?.street2}">
				<li class="fieldcontain">
					<span id="street2-label" class="property-label"><g:message code="address.street2.label" default="Street (2)" /></span>
					<span class="property-value" aria-labelledby="street2-label"><g:fieldValue bean="${addressInstance}" field="street2"/></span>
				</li>
				</g:if>
			
				<g:if test="${addressInstance?.city}">
				<li class="fieldcontain">
					<span id="city-label" class="property-label"><g:message code="address.city.label" default="City" /></span>
    				<span class="property-value" aria-labelledby="city-label"><g:fieldValue bean="${addressInstance}" field="city"/></span>
				</li>
				</g:if>
			
				<g:if test="${addressInstance?.state}">
				<li class="fieldcontain">
					<span id="state-label" class="property-label"><g:message code="address.state.label" default="State/Province/Region" /></span>
					<span class="property-value" aria-labelledby="state-label"><g:fieldValue bean="${addressInstance}" field="state"/></span>
				</li>
				</g:if>
			
				<g:if test="${addressInstance?.country}">
				<li class="fieldcontain">
					<span id="country-label" class="property-label"><g:message code="address.country.label" default="Country" /></span>
					<span class="property-value" aria-labelledby="country-label">
                        <g:country code="${addressInstance.country}"/>
                    </span>
				</li>
				</g:if>
			
				<g:if test="${addressInstance?.zipCode}">
				<li class="fieldcontain">
					<span id="zipCode-label" class="property-label"><g:message code="address.zipCode.label" default="Zip Code" /></span>
					<span class="property-value" aria-labelledby="zipCode-label"><g:fieldValue bean="${addressInstance}" field="zipCode"/></span>
				</li>
				</g:if>
			
				<g:if test="${addressInstance?.phone}">
				<li class="fieldcontain">
					<span id="phone-label" class="property-label"><g:message code="address.phone.label" default="Phone" /></span>
					<span class="property-value" aria-labelledby="phone-label"><g:fieldValue bean="${addressInstance}" field="phone"/></span>
				</li>
				</g:if>
			</ol>

			<g:form url="[resource:addressInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${addressInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
