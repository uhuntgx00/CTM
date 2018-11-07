<br/><hr/>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'roles', 'error')} ">
    <label for="accountRole-label"><g:message code="account.role.label" default="Roles" /></label>
    <span class="property-value" aria-labelledby="accountRole-label">
        <g:each in="${accountInstance.getManagedAuthorities()}" var="a">
            <g:if test="${a?.readOnly}">
                <g:hiddenField name="${a?.roleName}" value="on"/>
                <g:checkBox name="visual_dummy_${a.roleName}" value="${a?.selected}" disabled="true"/> ${a.roleName}
            </g:if>
            <g:else>
                <g:checkBox name="${a.roleName}" value="${a?.selected}"/> ${a.roleName}
            </g:else>
            <br/>
        </g:each>
    </span>
</div>

