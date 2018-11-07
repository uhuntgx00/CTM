<%@ page import="org.springframework.validation.FieldError; mil.ebs.ctm.Address" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'address.label', default: 'Address')}" />
		<title>Edit Venue Address</title>
	</head>
	<body>
		<a href="#edit-address" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-address" class="content scaffold-edit" role="main">
			<h1>Edit Venue Address</h1>
			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${addressInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${addressInstance}" var="error">
                    <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

			<g:form url="[resource:addressInstance, action:'update']" method="PUT" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="conferenceUpdate" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>

        <script type="text/javascript">
            $(document).ready(function(){
                $( document ).tooltip();

                $('#country').multiselect({
                    noneSelectedText: 'Select Country',
                    multiple: false,
                    header: false,
                    selectedList: 1,
                    minWidth: 450
                });

            })
        </script>

	</body>
</html>
