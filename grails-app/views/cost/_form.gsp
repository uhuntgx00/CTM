<%@ page import="mil.ebs.ctm.Address; mil.ebs.ctm.Attendee; mil.ebs.ctm.Cost" %>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'registrationCost', 'error')} required">
	<label for="registrationCost">
		<g:message code="cost.registrationCost.label" default="Registration Cost" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField tabindex="1" name="registrationCost" value="${costInstance?.registrationCost?.encodeAsHTML()}" required="" onchange="updateTotal()"/>
</div>

<g:if test="${!costInstance?.registrationCost}">
<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'zeroRegistrationReason', 'error')}">
	<label for="zeroRegistrationReason">
		<span style="<g:if test="${hasErrors(bean: costInstance, field: 'zeroRegistrationReason', 'error').equalsIgnoreCase('Error')}">color:#FF0000</g:if><g:else>color:#000000</g:else>"><b><g:message code="cost.zeroRegistrationReason.label" default="Reason Zero (0.00) Entry" /></b></span>
	</label>
	<g:textField tabindex="-1" name="zeroRegistrationReason" size="50" value="${fieldValue(bean: costInstance, field: 'zeroRegistrationReason')}"/>
</div>
</g:if>

<br/><hr>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'airfareCost', 'error')} required">
	<label for="airfareCost">
		<g:message code="cost.airfareCost.label" default="Airfare Cost" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField tabindex="2" name="airfareCost" value="${costInstance?.airfareCost?.encodeAsHTML()}" required="" onchange="updateTotal()"/>
    &nbsp;&nbsp;&nbsp;<span style="font-size:12px;color:#777777"><i>Include baggage fees; <b>From</b> <a tabindex="-1" class="costLink" href="http://cpsearch.fas.gsa.gov/cpsearch/search.do?method=enter" target="_blank">http://cpsearch.fas.gsa.gov/cpsearch/search.do?method=enter</a></i></span>
</div>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'airfareProvider', 'error')} ">
	<label for="airfareProvider">
		<g:message code="cost.airfareProvider.label" default="Airfare Provider" />
	</label>
	<g:textField tabindex="-1" name="airfareProvider" size="50" value="${costInstance?.airfareProvider}"/>
</div>

<g:if test="${!costInstance?.airfareCost}">
<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'zeroAirfareReason', 'error')}">
	<label for="zeroAirfareReason">
        <span style="<g:if test="${hasErrors(bean: costInstance, field: 'zeroAirfareReason', 'error').equalsIgnoreCase('Error')}">color:#FF0000</g:if><g:else>color:#000000</g:else>"><b><g:message code="cost.zeroReason.label" default="Reason Zero (0.00) Entry" /></b></span>
	</label>
	<g:textField tabindex="-1" name="zeroAirfareReason" size="50" value="${fieldValue(bean: costInstance, field: 'zeroAirfareReason')}"/>
</div>
</g:if>

<br/><hr>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'localTravelCost', 'error')}">
	<label for="localTravelCost">
		<g:message code="cost.localTravelCost.label" default="Local Travel Cost" />
	</label>
	<g:textField tabindex="3" name="localTravelCost" value="${costInstance?.localTravelCost?.encodeAsHTML()}" onchange="updateTotal()"/>
    &nbsp;&nbsp;&nbsp;<span style="font-size:12px;color:#777777"><i><b>I.e.</b> rental car, taxi, parking, etc</i></span>
</div>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'localTravelProvider', 'error')} ">
	<label for="localTravelProvider">
		<g:message code="cost.localTravelProvider.label" default="Local Travel Provider" />
	</label>
	<g:textField tabindex="-1" name="localTravelProvider" size="50" value="${costInstance?.localTravelProvider}"/>
</div>

<br/><hr>

<g:if test="${costInstance?.attendee?.conference?.perdiem}">
    <div class="fieldcontain">
        <span id="perdiem-label" class="property-label"><i><g:message code="cost.lodgingPerdiem.label" default="Lodging PerDiem" /></i></span>
        <span class="property-value" aria-labelledby="perdiem-label">
            <b><g:formatNumber number="${costInstance?.attendee?.conference?.perdiem}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b>/night
            &nbsp;&nbsp;<span style="color:#777777"><i>(<g:formatNumber number="${calc?.lodgingPerDiemCost}" type="currency" currencyCode="USD" maxFractionDigits="2" /> total)</i></span>
        </span>
    </div>
</g:if>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'lodgingCost', 'error')} required">
	<label for="lodgingCost">
		<g:message code="cost.lodgingCost.label" default="Lodging Cost" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField tabindex="4" name="lodgingCost" value="${costInstance?.lodgingCost?.encodeAsHTML()}" required="" onchange="updateTotal()"/>
    &nbsp;&nbsp;&nbsp;<span style="font-size:12px;color:#777777"><i><b>From</b> <a tabindex="-1" class="costLink" href="http://www.defensetravel.dod.mil/site/perdiemCalc.cfm" target="_blank">http://www.defensetravel.dod.mil/site/perdiemCalc.cfm</a></i></span>
</div>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'lodgingCostTax', 'error')}">
	<label for="lodgingCostTax">
		<g:message code="cost.lodgingCostTax.label" default="Lodging Cost Tax" />
	</label>
	<g:textField tabindex="5" name="lodgingCostTax" value="${costInstance?.lodgingCostTax?.encodeAsHTML()}" onchange="updateTotal()"/>
</div>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'lodgingProvider', 'error')} ">
	<label for="lodgingProvider">
		<g:message code="cost.lodgingProvider.label" default="Lodging Provider" />
	</label>
	<g:textField tabindex="-1" name="lodgingProvider" size="50" value="${costInstance?.lodgingProvider}"/>
</div>

<g:if test="${!costInstance?.lodgingCost}">
<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'zeroLodgingReason', 'error')}">
	<label for="zeroLodgingReason">
        <span style="<g:if test="${hasErrors(bean: costInstance, field: 'zeroLodgingReason', 'error').equalsIgnoreCase('Error')}">color:#FF0000</g:if><g:else>color:#000000</g:else>"><b><g:message code="cost.zeroReason.label" default="Reason Zero (0.00) Entry" /></b></span>
	</label>
	<g:textField tabindex="-1" name="zeroLodgingReason" size="50" value="${fieldValue(bean: costInstance, field: 'zeroLodgingReason')}"/>
</div>
</g:if>

<g:if test="${costInstance?.lodgingAddress}">
<div class="fieldcontain">
	<label for="lodgingAddress">
		<g:message code="cost.lodgingAddress.label" default="Lodging Address" />
	</label>
    <span style="color:#777777"><i><g:fieldValue name="lodgingAddress" bean="${costInstance}" field="lodgingAddress"/></i></span>
</div>

<div class="fieldcontain">
	<label for="lodgingClear">
		<g:message code="cost.lodgingClear.label" default="" />
	</label>
    <g:link name="lodgingClear" controller="cost" action="clearLodging" id="${costInstance?.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
        <g:img dir="images" file="remove_16.png" alt="Clear Lodging"/>
    </g:link>
</div>
</g:if>

<br/><hr>

<g:if test="${costInstance?.attendee?.conference?.meals}">
    <div class="fieldcontain">
        <span id="meals-label" class="property-label"><i><g:message code="cost.mealsPerdiem.label" default="Meals/Incidental PerDiem" /></i></span>
        <span class="property-value" aria-labelledby="meals-label">
            <b><g:formatNumber number="${costInstance?.attendee?.conference?.meals}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b>/day
            &nbsp;&nbsp;
            <g:if test="${calc?.mealsIncludedAdjustment}"><strike></g:if>
                <span style="color:#777777"><i>(<g:formatNumber number="${calc?.mealPerDiemCost}" type="currency" currencyCode="USD" maxFractionDigits="2" /> total)</i></span>
            <g:if test="${calc?.mealsIncludedAdjustment}"></strike></g:if>
            <g:if test="${calc?.mealsIncludedAdjustment}">
                &nbsp;&nbsp;<span style="color:#FF0000"><i>(<g:formatNumber number="${calc?.mealPerDiemCost - calc?.mealsIncludedAdjustment}" type="currency" currencyCode="USD" maxFractionDigits="2" /> Adjusted)</i></span>
            </g:if>
        </span>
    </div>

    <g:if test="${costInstance?.attendee?.mealsIncluded}">
    <div class="fieldcontain">
        <span id="mealsIncluded-label" class="property-label"><i><g:message code="cost.mealsIncluded.label" default="Meals Included Adjustment" /></i></span>
        <span class="property-value" aria-labelledby="mealsIncluded-label">
            ${costInstance?.attendee?.mealsIncluded} meals @ <g:formatNumber number="${calc?.costPerMeal}" type="currency" currencyCode="USD" maxFractionDigits="2" />
            <span style="color:#FF0000"><i>(<g:formatNumber number="${calc?.mealsIncludedAdjustment}" type="currency" currencyCode="USD" maxFractionDigits="2" /> total)</i></span>
        </span>
    </div>
    </g:if>
</g:if>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'mealsIncidentalCost', 'error')} required">
	<label for="mealsIncidentalCost">
		<g:message code="cost.mealsIncidentalCost.label" default="Meals Incidental Cost" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField tabindex="6" name="mealsIncidentalCost" value="${costInstance?.mealsIncidentalCost?.encodeAsHTML()}" required="" onchange="updateTotal()"/>
    &nbsp;&nbsp;&nbsp;<span style="font-size:12px;color:#777777"><i>Meals and incidental expenses; <b>From</b> <a tabindex="-1" class="costLink" href="http://www.defensetravel.dod.mil/site/perdiemCalc.cfm" target="_blank">http://www.defensetravel.dod.mil/site/perdiemCalc.cfm</a></i></span>
</div>

<g:if test="${!costInstance?.mealsIncidentalCost}">
<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'zeroMealsReason', 'error')}">
	<label for="zeroMealsReason">
        <span style="<g:if test="${hasErrors(bean: costInstance, field: 'zeroMealsReason', 'error').equalsIgnoreCase('Error')}">color:#FF0000</g:if><g:else>color:#000000</g:else>"><b><g:message code="cost.zeroReason.label" default="Reason Zero (0.00) Entry" /></b></span>
	</label>
	<g:textField tabindex="-1" name="zeroMealsReason" size="50" value="${fieldValue(bean: costInstance, field: 'zeroMealsReason')}"/>
</div>
</g:if>

<br/><hr>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'otherCost', 'error')}">
	<label for="otherCost">
		<g:message code="cost.otherCost.label" default="Other Cost" />
	</label>
	<g:textField tabindex="7" name="otherCost" value="${costInstance?.otherCost?.encodeAsHTML()}" onchange="updateTotal()"/>
</div>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'otherDescription', 'error')} ">
	<label for="otherDescription">
		<g:message code="cost.otherDescription.label" default="Other Cost Description" />
	</label>
	<g:textField tabindex="-1" name="otherDescription" size="50" value="${costInstance?.otherDescription}"/>
    &nbsp;&nbsp;&nbsp;<span style="font-size:12px;color:#777777"><i><b>Example:</b> Booth Fee (225), Parking Pass (25)</i></span>
</div>

<br/><hr>

<div class="fieldcontain ${hasErrors(bean: costInstance, field: 'notes', 'error')} ">
	<label for="otherDescription">
		<g:message code="cost.notes.label" default="Notes" />
	</label>
	<g:textField tabindex="-1" name="notes" size="50" value="${costInstance?.notes}"/>
</div>

<br/><hr/>

<div class="fieldcontain">
    <label for="total">
   		<b>Total</b>
   	</label>
    <span>
        <input type="text" size="20" id="total" readonly tabindex="-1">
    </span>
</div>

<div class="fieldcontain">
	<label for="funding">
		<g:message code="attendee.funding.label" default="Funding" />
	</label>
    <span id="funding">
        %{--<g:hiddenField id="fundSource1numa" name="fundSource1numa" />--}%
        %{--<g:hiddenField id="fundSource2numa" name="fundSource2numa" />--}%
        %{--<g:hiddenField id="fundSource3numa" name="fundSource3numa" />--}%
        &nbsp;&nbsp;&nbsp;&nbsp;<input type="text" size="32" id="fundSource1num" readonly style="text-align: center; border:0; color:#1f93f6; background-color: rgba(1,1,1,.02); font-weight: bold;">
        <input type="text" size="32" id="fundSource2num" readonly style="text-align: center; border:0; color:#1f93f6; background-color: rgba(1,1,1,.02); font-weight: bold;">
        <input type="text" size="32" id="fundSource3num" readonly style="text-align: center; border:0; color:#1f93f6; background-color: rgba(1,1,1,.02); font-weight: bold;">
    </span>
</div>

<div class="fieldcontain">
	<label for="fundSources">
		<g:message code="attendee.fundSources.label" default="Funding Source" />
	</label>
    <span id="fundSources">
        &nbsp;&nbsp;&nbsp;&nbsp;<input type="text" size="32" id="fundSource1" readonly style="text-align: center; border:0; color:#1f93f6; background-color: rgba(1,1,1,.02); font-weight: bold;">
        <input type="text" size="32" id="fundSource2" readonly style="text-align: center; border:0; color:#1f93f6; background-color: rgba(1,1,1,.02); font-weight: bold;">
        <input type="text" size="32" id="fundSource3" readonly style="text-align: center; border:0; color:#1f93f6; background-color: rgba(1,1,1,.02); font-weight: bold;">
        <g:hiddenField id="fundSource1a" name="fundSource1a" value="${fundSource1a}" />
        <g:hiddenField id="fundSource2a" name="fundSource2a" value="${fundSource2a}" />
        <g:hiddenField id="fundSource3a" name="fundSource3a" value="${fundSource3a}" />
        <div id="fundSourceSliderDiv">
            <div id="fundSourceSlider"></div>
        </div>
        <br/>
        <span class="fundSource" style="color:#777777"><i>The <b>FUND SOURCE</b> selector above has two slider controls one on each end of the slider.</i></span>
        <span class="fundSource" style="color:#777777"><i>The left slider control handles the <b>Other US Govt</b> funding, while the right slider control handles the <b>Non-Federal Entity</b> funding.</i></span>
        <span class="fundSource" style="color:#777777"><i>The <b>US Air Force</b> funding is computed by taking the remainder from the total of the two slide controls.</i></span>
        <span class="fundSource" style="color:#777777"><i>The slide control ensure that a total funding source of 100% is maintained.</i></span>
    </span>
</div>

