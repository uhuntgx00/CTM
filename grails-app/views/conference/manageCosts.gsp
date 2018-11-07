<%@ page defaultCodec="none" %>
<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
        <title><g:message code="default.manage.label" args="[entityName]" /> Attendees</title>
    </head>

    <body>
        <a href="#edit-conference" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

        <div id="edit-conference" class="content scaffold-edit" role="main">
            <h1><g:message code="default.manage.label" args="[entityName]" /> Attendees</h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${conferenceInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${conferenceInstance}" var="error">
                        <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}" /></li>
                    </g:eachError>
                </ul>
            </g:hasErrors>

            <g:form url="[resource: conferenceInstance, action: 'manageCostsUpdate']" method="PUT">
                <g:hiddenField id="data_return" name="data_return"/>
                <fieldset class="form">
                    <br/>
                    <br/>

                    <center><div id="contentHOT" style="width: 1300px; height: 350px; overflow: auto"></div></center>

                    <br/>
                    <br/>
                </fieldset>
                <fieldset class="buttons">
                    <g:actionSubmit class="save" action="manageCostsUpdate" value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="submitform()"/>
                </fieldset>
            </g:form>
        </div>

        <script type="text/javascript">
            var data = ${jsonData}

            function submitform() {
                $('#data_return').val(JSON.stringify(data));
                return true;
            }

            $(document).ready(function () {
                $(document).tooltip();

                var container = document.getElementById("contentHOT");

                var yellowRenderer = function (instance, td, row, col, prop, value, cellProperties) {
                    Handsontable.renderers.TextRenderer.apply(this, arguments);
                    td.style.backgroundColor = 'yellow';
                };

                var hot1 = new Handsontable(container, {
//                    data: JSON.parse(JSON.stringify(data)),
                    data: data,
//                    data: [],
                    dataSchema: {
                        id: null,
                        account: null,
                        attendanceType: 'Attendee',
                        rankGrade: null,
                        status: 'Pending',
                        td: null,
                        td_org: null,
                        supervisor: null,
                        startTravelDateStr: '1-1-2014',
                        endTravelDateStr: '1-1-2014',
                        est_airfareCost: 0.0,
                        est_lodgingCost: 0.0,
                        est_lodgingCostTax: 0.0,
                        est_localTravelCost: 0.0,
                        est_mealsIncidentalCost: 0.0,
                        est_otherCost: 0.0,
                        est_registrationCost: 0.0,
                        act_airfareCost: 0.0,
                        act_lodgingCost: 0.0,
                        act_lodgingCostTax: 0.0,
                        act_localTravelCost: 0.0,
                        act_mealsIncidentalCost: 0.0,
                        act_otherCost: 0.0,
                        act_registrationCost: 0.0
                    },
                    startRows: 8,
                    startCols: 6,
                    fixedColumnsLeft: 2,
                    rowHeaders: false,
                    colHeaders: ["ID", "Attendee", "Type", "Rank/Grade", "Status", "TD", "TD Org", "Supervisor", "Start Travel", "End Travel", "EST Airfare", "EST Lodging", "EST Lodging Tax", "EST Local", "EST Meals", "EST Other", "EST Registration", "ACT Airfare", "ACT Lodging", "ACT Lodging Tax", "ACT Local", "ACT Meals", "ACT Other", "ACT Registration"],
                    minSpareRows: 0,
                    columns: [
                        {data: "id", type: "numeric", renderer: yellowRenderer, readOnly: true},
                        {data: "account", type: "text", readOnly: true},
                        {
                            data: "attendanceType",
                            type: "autocomplete",
                            source: ["Attendee", "Booth/Display", "Discussion Panel", "Session Chair", "Presenter/Speaker", "Support", "Other"],
                            strict: true
                        },
                        {data: "rankGrade", type: "text", readOnly: true},

                    <sec:ifAnyGranted roles="ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                        {
                            data: "status",
                            type: "autocomplete",
                            source: ["Supervisor", "TD Concurrence", "Pending", "Wait List", "Withdrawn", "Approved", "Disapproved", "Registered", "Attended", "Cancelled", "Requesting", "Confirmed"],
                            strict: true
                        },
                    </sec:ifAnyGranted>
                    <sec:ifNotGranted roles="ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                        {
                            data: "status",
                            type: "autocomplete",
                            source: ["Supervisor", "TD Concurrence", "Pending", "Wait List", "Withdrawn", "Approved", "Disapproved", "Registered", "Attended", "Cancelled", "Requesting", "Confirmed"],
                            strict: true,
                            readOnly: true
                        },
                    </sec:ifNotGranted>

                        {data: "td", type: "text", readOnly: true},
                        {data: "td_org", type: "text", readOnly: true},
                        {data: "supervisor", type: "text", readOnly: true},
                        {
                            data: "startTravelDateStr",
                            type: "date",
                            dateFormat: "mm-dd-yy"
                        },
                        {
                            data: "endTravelDateStr",
                            type: "date",
                            dateFormat: "mm-dd-yy"
                        },
                        {
                            data: "est_airfareCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "est_lodgingCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "est_lodgingCostTax",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "est_localTravelCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "est_mealsIncidentalCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "est_otherCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "est_registrationCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "act_airfareCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "act_lodgingCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "act_lodgingCostTax",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "act_localTravelCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "act_mealsIncidentalCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "act_otherCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        },
                        {
                            data: "act_registrationCost",
                            type: "numeric",
                            format: "0,0.00 $"
                        }
                    ],
                    afterChange: function( change, source ) {
                        if (source === 'loadData') {
                            return; // don't save this change
                        }
                    }
                });
            })
        </script>

    </body>
</html>
