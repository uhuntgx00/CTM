<%@page defaultCodec="none" %>
<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:render template="tinyMCE"/>
	</head>
	<body>
		<a href="#edit-conference" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-conference" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${conferenceInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${conferenceInstance}" var="error">
                        <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

			<g:form url="[resource:conferenceInstance, action:'update']" method="PUT" >
				<fieldset class="form">
					<g:render template="form"/>
                    <g:render template="formCAO"/>
                    <g:render template="formAdmin"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>

        <script type="text/javascript">
            $(document).ready(function(){
                $( document ).tooltip();

                updateDatePicker();

                $("#spinner").ajaxComplete (function(event, request, settings){
                    updateDatePicker();
                });

                $('.afrlHost').hide();
                $('.coHost').hide();
                $('.nonHost').hide();

                if ($('#hostType option:selected').text() == "AF Hosted"){
                    $('.afrlHost').show();
                }
                else if ($('#hostType option:selected').text() == "AF Co-Hosted"){
                    $('.afrlHost').show();
                    $('.coHost').show();
                }
                else if ($('#hostType option:selected').text() == "DoD Co-Hosted"){
                    $('.coHost').show();
                }
                else if ($('#hostType option:selected').text() == "Non-DoD Hosted"){
                    $('.nonHost').show();
                    $('.coHost').show();
                }

                $('#hostType').change(function () {
                    if ($('#hostType option:selected').text() == ""){
                        $('#afrlHosted').prop('checked', false);
                        $('#coHostEntity').val("");
                        $('#nonHostType').val("");
                        $('.afrlHost').hide();
                        $('.coHost').hide();
                        $('.nonHost').hide();
                    }
                    if ($('#hostType option:selected').text() == "AF Hosted"){
                        $('#coHostEntity').val("");
                        $('#nonHostType').val("");
                        $('.afrlHost').show();
                        $('.coHost').hide();
                        $('.nonHost').hide();
                    }
                    else if ($('#hostType option:selected').text() == "AF Co-Hosted"){
                        $('#nonHostType').val("");
                        $('.afrlHost').show();
                        $('.coHost').show();
                        $('.nonHost').hide();
                    }
                    else if ($('#hostType option:selected').text() == "DoD Hosted"){
                        $('#afrlHosted').prop('checked', false);
                        $('#nonHostType').val("");
                        $('#coHostEntity').val("");
                        $('.afrlHost').hide();
                        $('.coHost').hide();
                        $('.nonHost').hide();
                    }
                    else if ($('#hostType option:selected').text() == "DoD Co-Hosted"){
                        $('#afrlHosted').prop('checked', false);
                        $('#nonHostType').val("");
                        $('.afrlHost').hide();
                        $('.coHost').show();
                        $('.nonHost').hide();
                    }
                    else if ($('#hostType option:selected').text() == "Non-DoD Hosted"){
                        $('#afrlHosted').prop('checked', false);
                        $('.afrlHost').hide();
                        $('.coHost').show();
                        $('.nonHost').show();
                    }
                });

                $('#accountCaoBlock').show();
                $('#accountAltBlock').show();
                if ($('#responsibleTD option:selected').text() == "") {
                    $('#accountCaoBlock').hide();
                }
                if ($('#alternateRespTD option:selected').text() == "") {
                    $('#accountAltBlock').hide();
                }

                $('#hostType').multiselect({
                    noneSelectedText: 'Select Host Type',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 200
                });

                $('#nonHostType').multiselect({
                    noneSelectedText: 'Select Non-Host Type',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 300
                });

                $('#phaseState').multiselect({
                    noneSelectedText: 'Select Phase State',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 200
                });

                $('#status').multiselect({
                    noneSelectedText: 'Select Status',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 200
                });

                $('#responsibleTD').multiselect({
                    noneSelectedText: 'Select Responsible Technology Directorate',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 650
                });

                $('#alternateRespTD').multiselect({
                    noneSelectedText: 'Select Responsible Technology Directorate',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 650
                });

                $('#conferenceAO').multiselect({
                    noneSelectedText: 'Select CAO',
                    multiple: false,
                    header: true,
                    selectedList: 1,
                    minWidth: 650
                }).multiselectfilter();

                $('#alternateCAO').multiselect({
                    noneSelectedText: 'Select Alternate CAO',
                    multiple: false,
                    header: true,
                    selectedList: 1,
                    minWidth: 650
                }).multiselectfilter();

            })
        </script>

	</body>
</html>
