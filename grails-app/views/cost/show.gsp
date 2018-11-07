
<%@ page import="mil.ebs.ctm.Cost" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'cost.label', default: 'Cost')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-cost" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-cost" class="content scaffold-show" role="main">
			<h1>
                Show <b>${costInstance?.costType}</b> Cost (<b>${costInstance?.attendee?.attendanceType}</b>) - <g:link controller="attendee" action="show" id="${costInstance?.attendee?.id}"><i>${costInstance?.attendee?.encodeAsHTML()}</i></g:link>
                <br/>Conference: <g:link controller="conference" action="show" id="${costInstance?.attendee?.conference?.id}"><b>${costInstance?.attendee?.conference}</b></g:link>
                <g:if test="${costInstance?.attendee?.conference?.locked}">(Locked)</g:if><g:else>(Open)</g:else>
                <br/>&nbsp;
            </h1>
			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>

            <ol class="property-list cost">
                <li class="fieldcontain">
                    <span id="registrationCost-label" class="property-label">
                        <g:if test="${!costInstance?.registrationCost}">
                            <g:img dir="images" file="exclamation.png" height="16" width="16" alt="Error"/>
                        </g:if>
                        <g:message code="cost.registrationCost.label" default="Registration Cost" />
                    </span>
                    <span class="property-value" aria-labelledby="registrationCost-label">
                        <g:formatNumber number="${costInstance?.registrationCost}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                    </span>
                </li>

                <g:if test="${costInstance?.zeroRegistrationReason}">
                <li class="fieldcontain">
                    <span id="zeroRegistrationReason-label" class="property-label"><g:message code="cost.zeroRegistrationReason.label" default="Reason Zero (0.00) Entry" /></span>
                    <span class="property-value" aria-labelledby="zeroRegistrationReason-label">
                        <g:fieldValue bean="${costInstance}" field="zeroRegistrationReason"/>
                    </span>
                </li>
                </g:if>

                <br/><hr>

                <li class="fieldcontain">
                    <span id="airfareCost-label" class="property-label">
                        <g:if test="${!costInstance?.airfareCost}">
                            <g:img dir="images" file="exclamation.png" height="16" width="16" alt="Error"/>
                        </g:if>
                        <g:message code="cost.airfareCost.label" default="Airfare Cost" />
                    </span>
                    <span class="property-value" aria-labelledby="airfareCost-label">
                        <g:formatNumber number="${costInstance?.airfareCost}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                        <g:if test="${costInstance?.airfareProvider}">&nbsp;<span style="color:#777777">(${costInstance?.airfareProvider})</span></g:if>
                    </span>
                </li>

                <g:if test="${costInstance?.zeroAirfareReason}">
                <li class="fieldcontain">
                    <span id="zeroAirfareReason-label" class="property-label"><g:message code="cost.zeroReason.label" default="Reason Zero (0.00) Entry" /></span>
                    <span class="property-value" aria-labelledby="zeroAirfareReason-label">
                        <g:fieldValue bean="${costInstance}" field="zeroAirfareReason"/>
                    </span>
                </li>
                </g:if>

                <br/><hr>

                <li class="fieldcontain">
                    <span id="localTravelCost-label" class="property-label">
                        <g:message code="cost.localTravelCost.label" default="Local Travel Cost" />
                    </span>
                    <span class="property-value" aria-labelledby="localTravelCost-label">
                        <g:formatNumber number="${costInstance?.localTravelCost}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                        <g:if test="${costInstance?.localTravelProvider}">&nbsp;<span style="color:#777777">(${costInstance?.localTravelProvider})</span></g:if>
                        <g:if test="${avgTravelCost}">&nbsp;&nbsp;<span style="color:#0000FF"><i>(<g:formatNumber number="${calc?.avgTravelCost}" type="currency" currencyCode="USD" maxFractionDigits="2" /> per day)</i></span></g:if>
                    </span>
                </li>

                <br/><hr>

                <li class="fieldcontain">
                    <span id="lodgingCost-label" class="property-label">
                        <g:if test="${costInstance?.mealsIncidentalCost > calc?.mealPerDiemCost && calc?.mealPerDiemCost != 0}">
                            <g:img dir="images" file="Icon_Warning_Red.png" height="16" width="16" alt="Warning"/>
                        </g:if>
                        <g:if test="${!costInstance?.lodgingCost}">
                            <g:img dir="images" file="exclamation.png" height="16" width="16" alt="Error"/>
                        </g:if>
                        <g:message code="cost.lodgingCost.label" default="Lodging Cost" />
                    </span>
                    <span class="property-value" aria-labelledby="lodgingCost-label">
                        <g:if test="${(costInstance?.lodgingCost > calc?.lodgingPerDiemCost + 0.01) && calc?.lodgingPerDiemCost != 0}">
                            <span style="color:#FF0000"><b><g:formatNumber number="${costInstance?.lodgingCost}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b> | Exceeds <b>PerDiem</b> rate</span>
                        </g:if>
                        <g:else>
                            <g:formatNumber number="${costInstance?.lodgingCost}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                        </g:else>
                        <g:if test="${costInstance?.lodgingProvider}">&nbsp;<span style="color:#777777">(${costInstance?.lodgingProvider})</span></g:if>
                        <g:if test="${calc?.avgLodgingCost}">&nbsp;&nbsp;<span style="color:#0000FF"><i>(<g:formatNumber number="${calc?.avgLodgingCost}" type="currency" currencyCode="USD" maxFractionDigits="2" />/night)</i></span></g:if>
                    </span>
                </li>

                <g:if test="${costInstance?.lodgingCostTax}">
                    <li class="fieldcontain">
                        <span id="lodgingCostTax-label" class="property-label">
                            <g:message code="cost.lodgingCostTax.label" default="Lodging Cost Tax" />
                        </span>
                        <span class="property-value" aria-labelledby="lodgingCostTax-label">
                            <g:formatNumber number="${costInstance?.lodgingCostTax}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                        </span>
                    </li>
                </g:if>

                <g:if test="${costInstance?.attendee?.conference?.perdiem}">
                    <li class="fieldcontain">
                        <span id="perdiem-label" class="property-label"><i><g:message code="cost.lodgingPerdiem.label" default="Lodging PerDiem" /></i></span>
                        <span class="property-value" aria-labelledby="perdiem-label">
                            <b><g:formatNumber number="${costInstance?.attendee?.conference?.perdiem}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b>/night
                            &nbsp;&nbsp;<span style="color:#777777"><i>(<g:formatNumber number="${calc?.lodgingPerDiemCost}" type="currency" currencyCode="USD" maxFractionDigits="2" /> total)</i></span>
                        </span>
                    </li>
                </g:if>

                <g:if test="${costInstance?.lodgingAddress}">
                    <li class="fieldcontain">
                        <span id="lodgingAddress-label" class="property-label"><g:message code="cost.lodgingAddress.label" default="Lodging Address" /></span>
                        <span class="property-value" aria-labelledby="lodgingAddress-label">
                            <g:link controller="address" action="show" id="${costInstance?.lodgingAddress?.id}">
                                ${costInstance?.lodgingAddress?.street1},
                        &nbsp;${costInstance?.lodgingAddress?.city}
                                <g:if test="${costInstance?.lodgingAddress?.state}">&nbsp;${costInstance?.lodgingAddress?.state}</g:if>,
                        &nbsp;<g:country code="${costInstance?.lodgingAddress?.country}"/>
                                &nbsp;&nbsp;${costInstance?.lodgingAddress?.zipCode}
                            </g:link>
                            &nbsp;&nbsp;
                            <g:link controller="address" action="deleteLodgingAddress" id="${costInstance?.lodgingAddress?.id}" params="['cost.id': costInstance?.id]" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                                <g:img dir="images" file="remove_16.png" alt="Remove Address"/>
                            </g:link>
                        </span>
                    </li>
                </g:if>
                <g:else>
                    <li class="fieldcontain">
                        <span id="lodgingAddressMissing-label" class="property-label"><g:message code="cost.lodgingAddress.label" default="Lodging Address" /></span>
                        <span class="property-value" aria-labelledby="lodgingAddressMissing-label">
                            <g:img dir="images/icons" file="add-address_512.png" height="16" width="16" alt="Add Address"/>
                            <g:link controller="address" action="createLodgingAddress" params="['cost.id': costInstance?.id, 'addressType': 'Lodging']">${message(code: 'default.add.label', args: [message(code: 'address.label', default: 'Address')])}</g:link>
                        </span>
                    </li>
                </g:else>

                <g:if test="${costInstance?.zeroLodgingReason}">
                <li class="fieldcontain">
                    <span id="zeroLodgingReason-label" class="property-label"><g:message code="cost.zeroReason.label" default="Reason Zero (0.00) Entry" /></span>
                    <span class="property-value" aria-labelledby="zeroLodgingReason-label">
                        <g:fieldValue bean="${costInstance}" field="zeroLodgingReason"/>
                    </span>
                </li>
                </g:if>

                <br/><hr>

                <li class="fieldcontain">
                    <span id="mealsIncidentalCost-label" class="property-label">
                        <g:if test="${(costInstance?.mealsIncidentalCost > (calc?.mealPerDiemCost - calc?.mealsIncludedAdjustment + 0.01)) && calc?.mealPerDiemCost != 0}">
                            <g:img dir="images" file="Icon_Warning_Red.png" height="16" width="16" alt="Warning"/>
                        </g:if>
                        <g:if test="${!costInstance?.mealsIncidentalCost}">
                            <g:img dir="images" file="exclamation.png" height="16" width="16" alt="Error"/>
                        </g:if>
                        <g:message code="cost.mealsIncidentalCost.label" default="Meals/Incidental Cost" />
                    </span>
                    <span class="property-value" aria-labelledby="mealsIncidentalCost-label">
                        <g:if test="${(costInstance?.mealsIncidentalCost > (calc?.mealPerDiemCost - calc?.mealsIncludedAdjustment + 0.01)) && calc?.mealPerDiemCost != 0}">
                            <span style="color:#FF0000"><b><g:formatNumber number="${costInstance?.mealsIncidentalCost}" type="currency" currencyCode="USD" maxFractionDigits="2" /></b> | Exceeds <b>PerDiem</b> rate</span>
                        </g:if>
                        <g:else>
                            <g:formatNumber number="${costInstance?.mealsIncidentalCost}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                        </g:else>
                        <g:if test="${calc?.avgMealsCost}">&nbsp;&nbsp;<span style="color:#0000FF"><i>(<g:formatNumber number="${calc?.avgMealsCost}" type="currency" currencyCode="USD" maxFractionDigits="2" />/day)</i></span></g:if>
                    </span>
                </li>

                <g:if test="${costInstance?.attendee?.conference?.meals}">
                    <li class="fieldcontain">
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
                    </li>

                    <g:if test="${costInstance?.attendee?.mealsIncluded}">
                        <li class="fieldcontain">
                            <span id="mealsIncluded-label" class="property-label"><i><g:message code="cost.mealsIncluded.label" default="Meals Included Adjustment" /></i></span>
                            <span class="property-value" aria-labelledby="mealsIncluded-label">
                                ${costInstance?.attendee?.mealsIncluded} meals @ <g:formatNumber number="${calc?.costPerMeal}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                                <span style="color:#FF0000"><i>(<g:formatNumber number="${calc?.mealsIncludedAdjustment}" type="currency" currencyCode="USD" maxFractionDigits="2" /> total)</i></span>
                            </span>
                        </li>
                    </g:if>
                </g:if>

                <g:if test="${costInstance?.zeroMealsReason}">
                <li class="fieldcontain">
                    <span id="zeroMealsReason-label" class="property-label"><g:message code="cost.zeroReason.label" default="Reason Zero (0.00) Entry" /></span>
                    <span class="property-value" aria-labelledby="zeroMealsReason-label">
                        <g:fieldValue bean="${costInstance}" field="zeroMealsReason"/>
                    </span>
                </li>
                </g:if>

                <br/><hr>

                <li class="fieldcontain">
                    <span id="otherCost-label" class="property-label"><g:message code="cost.otherCost.label" default="Other Cost" /></span>
                    <span class="property-value" aria-labelledby="otherCost-label">
                        <g:formatNumber number="${costInstance?.otherCost}" type="currency" currencyCode="USD" maxFractionDigits="2" />
                        <g:if test="${costInstance?.otherDescription}">&nbsp;<span style="color:#777777">(${costInstance?.otherDescription})</span></g:if>
                    </span>
                </li>

                <br/><hr>

                <li class="fieldcontain">
                    <span id="notes-label" class="property-label"><g:message code="cost.notes.label" default="Notes" /></span>
                    <span class="property-value" aria-labelledby="notes-label">
                        ${costInstance?.notes}
                    </span>
                </li>
            </ol>

            <g:form url="[resource:costInstance, action:'delete']" method="DELETE">
                <fieldset class="buttons">
                    <g:link class="edit" action="edit" resource="${costInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                </fieldset>
            </g:form>
		</div>
	</body>
</html>
