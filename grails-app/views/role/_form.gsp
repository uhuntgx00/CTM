<%@ page import="mil.ebs.ctm.Role" %>

<div class="fieldcontain ${hasErrors(bean: roleInstance, field: 'authority', 'error')} required">
	<label for="authority">
		<g:message code="role.authority.label" default="Authority" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="authority" size="35" required="" value="${roleInstance?.authority}"/>
</div>

<g:if test="${roleInstance?.managedRoles}">
    <div class="fieldcontain">
        <label for="managedRoles">
            <g:message code="role.managedRoles.label" default="Managed Roles" />
        </label>
        <g:textField name="managedRoles" size="75" required="" value="${roleInstance?.managedRoles}"/>
    </div>
</g:if>

