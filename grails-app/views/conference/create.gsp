<%@ page import="org.springframework.validation.FieldError" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
        <g:render template="tinyMCE"/>
	</head>
	<body>
		<div id="create-conference" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
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

            <g:form url="[resource:conferenceInstance, action:'save']" >
                <sec:ifAnyGranted roles="ROLE_USER, ROLE_AFRL_USER, ROLE_NON_AFRL_USER, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN, ROLE_DEVELOPER">
                    <sec:ifNotGranted roles="ROLE_ADMIN">
                        <g:hiddenField name="status" value="${conferenceInstance?.status}"/>
                        <g:hiddenField name="phaseState" value="${conferenceInstance?.phaseState}"/>
                    </sec:ifNotGranted>
                </sec:ifAnyGranted>
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>

        <script type="text/javascript">
//            $.noConflict();
            jQuery(document).ready(function($){
                $( document ).tooltip();

                updateDatePicker();

                $("#spinner").ajaxComplete (function(event, request, settings){
                    updateDatePicker();
                });

                $('.afrlHost').hide();
                $('.coHost').hide();
                $('.nonHost').hide();
                $('.dodCoHost').hide();

                $('#hostType').change(function () {
                    if ($('#hostType option:selected').text() == ""){
                        $('#afrlHosted').prop('checked', false);
                        $('#coHostEntity').val("");
                        $('#nonHostType').val("");
                        $('.afrlHost').hide();
                        $('.coHost').hide();
                        $('.nonHost').hide();
                        $('.dodCoHost').hide();
                    }
                    if ($('#hostType option:selected').text() == "AF Hosted"){
                        $('#coHostEntity').val("");
                        $('#nonHostType').val("");
                        $('.afrlHost').show();
                        $('.coHost').hide();
                        $('.nonHost').hide();
                        $('.dodCoHost').hide();
                    }
                    else if ($('#hostType option:selected').text() == "AF Co-Hosted"){
                        $('#nonHostType').val("");
                        $('.afrlHost').show();
                        $('.coHost').show();
                        $('.nonHost').hide();
                        $('.dodCoHost').hide();
                    }
                    else if ($('#hostType option:selected').text() == "DoD Hosted"){
                        $('#afrlHosted').prop('checked', false);
                        $('#nonHostType').val("");
                        $('#coHostEntity').val("");
                        $('.afrlHost').hide();
                        $('.coHost').hide();
                        $('.nonHost').hide();
                        $('.dodCoHost').hide();
                    }
                    else if ($('#hostType option:selected').text() == "DoD Co-Hosted"){
                        $('#afrlHosted').prop('checked', false);
                        $('#nonHostType').val("");
                        $('.afrlHost').hide();
                        $('.coHost').show();
                        $('.nonHost').hide();
                        $('.dodCoHost').hide();
                    }
                    else if ($('#hostType option:selected').text() == "Non-DoD Hosted"){
                        $('#afrlHosted').prop('checked', false);
                        $('#coHostEntity').val("");
                        $('.afrlHost').hide();
                        $('.coHost').hide();
                        $('.nonHost').show();
                        $('.dodCoHost').show();
                    }
                });

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

                <sec:ifAnyGranted roles="ROLE_ADMIN">
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
                </sec:ifAnyGranted>

                $('#conferenceAO').multiselect({
                    noneSelectedText: 'Select CAO',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 650
                });

                $('#alternateCAO').multiselect({
                    noneSelectedText: 'Select Alternate CAO',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 650
                });

            })
        </script>
	</body>
</html>
