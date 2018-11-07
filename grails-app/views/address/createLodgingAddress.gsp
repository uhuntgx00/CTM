<%@ page import="org.springframework.validation.FieldError" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'address.label', default: 'Address')}" />
		<title>Create Lodging Address</title>
	</head>
	<body>
		<a href="#create-address" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="create-address" class="content scaffold-create" role="main">
			<h1>Create Lodging Address</h1>
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

            <g:form url="[resource:addressInstance, action:'saveLodgingAddress']" >
                <g:hiddenField name="addressType" value="Lodging"/>
                <g:hiddenField name="cost.id" value="${addressInstance?.cost?.id}"/>
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
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
