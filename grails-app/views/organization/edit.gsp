<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.Organization" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'organization.label', default: 'TechnicalDirective')}" />
		<title>Edit Organization</title>
        <script>
            tinymce.init({
                selector: "textarea",
                theme: "modern",
                width: 700,
                height: 200,
                plugins: [
                     "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",
                     "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
                     "save table contextmenu directionality emoticons template paste textcolor"
                ],
                content_css: "css/content.css",
                toolbar: "insertfile undo redo | styleselect | bold italic forecolor backcolor fontselect fontsizeselect | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image ",
                style_formats: [
                    {title: 'Bold text', inline: 'b'},
                    {title: 'Red text', inline: 'span', styles: {color: '#ff0000'}},
                    {title: 'Red header', block: 'h1', styles: {color: '#ff0000'}},
                    {title: 'Example 1', inline: 'span', classes: 'example1'},
                    {title: 'Example 2', inline: 'span', classes: 'example2'},
                    {title: 'Table styles'},
                    {title: 'Table row 1', selector: 'tr', classes: 'tablerow1'}
                ]
            });
        </script>
	</head>
	<body>
		<a href="#edit-technicalDirective" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-technicalDirective" class="content scaffold-edit" role="main">
			<h1>
                <g:img dir="/images/icons" file="edit-group_512.png" width="48" height="48" alt="Group" title="Group"/>
                Edit Organization<g:if test="${organizationInstance?.trueTD}">&nbsp;<span style="color:#999999">(TD)</span></g:if>
            </h1>

			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${organizationInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${organizationInstance}" var="error">
                        <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

			<g:form url="[resource:organizationInstance, action:'update']" method="PUT" >
				<g:hiddenField name="version" value="${organizationInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>

        <script type="text/javascript">
            $(document).ready(function(){
                $( document ).tooltip();

                $('#tempAccount').multiselect({
                    noneSelectedText: 'Select Point-of-Contact',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 650
                });

                $('#director').multiselect({
                    noneSelectedText: 'Select Director',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 650
                });

            })
        </script>

	</body>
</html>
