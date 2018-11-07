<%@ page import="mil.ebs.ctm.ref.RefRankGrade" %>

<div class="fieldcontain ${hasErrors(bean: refRankGradeInstance, field: 'code', 'error')} required">
	<label for="code">
		<g:message code="refRankGrade.code.label" default="Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="code" size="10" required="" value="${refRankGradeInstance?.code}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refRankGradeInstance, field: 'grade', 'error')} required">
	<label for="grade">
		<g:message code="refRankGrade.grade.label" default="Grade" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="grade" size="5" maxlength="5" required="" value="${refRankGradeInstance?.grade}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refRankGradeInstance, field: 'description', 'error')} required">
	<label for="description">
		<g:message code="refRankGrade.description.label" default="Description" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="description" size="50" maxlength="100" required="" value="${refRankGradeInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: refRankGradeInstance, field: 'officer', 'error')} ">
	<label for="officer">
		<g:message code="refRankGrade.officer.label" default="Officer" />
	</label>
	<g:checkBox name="officer" value="${refRankGradeInstance?.officer}" />
</div>

<div class="fieldcontain ${hasErrors(bean: refRankGradeInstance, field: 'military', 'error')} ">
	<label for="military">
		<g:message code="refRankGrade.military.label" default="Military" />
	</label>
	<g:checkBox name="military" value="${refRankGradeInstance?.military}" />
</div>

