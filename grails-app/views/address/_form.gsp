<%@ page import="mil.ebs.ctm.Address" %>

<div class="fieldcontain ${hasErrors(bean: addressInstance, field: 'street1', 'error')}">
	<label for="street1">
		<g:message code="address.street1.label" default="Street 1" />
	</label>
	<g:textField name="street1" size="50" value="${addressInstance?.street1}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: addressInstance, field: 'street2', 'error')} ">
	<label for="street2">
		<g:message code="address.street2.label" default="Street 2" />
	</label>
	<g:textField name="street2" size="50" value="${addressInstance?.street2}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: addressInstance, field: 'city', 'error')} required">
	<label for="city">
		<g:message code="address.city.label" default="City" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="city" size="50" required="" value="${addressInstance?.city}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: addressInstance, field: 'state', 'error')} ">
	<label for="state">
		<g:message code="address.state.label" default="State/Province/Region" />
	</label>
	<g:textField name="state" value="${addressInstance?.state}"/>
    &nbsp;&nbsp;&nbsp;<span style="font-size:12px;color:#777777"><i><b>For State</b>, use the 2 letter abbreviation</i></span>
</div>

<div class="fieldcontain ${hasErrors(bean: addressInstance, field: 'country', 'error')} required">
	<label for="country">
		<g:message code="address.country.label" default="Country" />
		<span class="required-indicator">*</span>
	</label>
    <g:countrySelect name="country" value="${addressInstance?.country}" default="usa"/>
</div>

<div class="fieldcontain ${hasErrors(bean: addressInstance, field: 'zipCode', 'error')} ">
	<label for="zipCode">
		<g:message code="address.zipCode.label" default="Zip Code" />
	</label>
	<g:textField name="zipCode" size="10" maxlength="10" value="${addressInstance?.zipCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: addressInstance, field: 'phone', 'error')} ">
	<label for="phone">
		<g:message code="address.phone.label" default="Phone" />
	</label>
	<g:textField name="phone" size="15" value="${addressInstance?.phone}"/>
</div>

