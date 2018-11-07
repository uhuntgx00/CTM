<%@ page import="mil.ebs.ctm.news.NewsItem" %>

<div class="fieldcontain ${hasErrors(bean: newsItemInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="newsItem.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" required="" value="${newsItemInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: newsItemInstance, field: 'body', 'error')} ">
	<label for="body">
		<g:message code="newsItem.body.label" default="Body" />
	</label>
	<g:textArea name="body" value="${newsItemInstance?.body}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: newsItemInstance, field: 'locked', 'error')} ">
	<label for="locked">
		<g:message code="newsItem.locked.label" default="Locked" />
	</label>
	<g:checkBox name="locked" value="${newsItemInstance?.locked}" />
</div>

<div class="fieldcontain ${hasErrors(bean: newsItemInstance, field: 'author', 'error')} required">
	<label for="author">
		<g:message code="newsItem.author.label" default="Author" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="author" name="author.id" from="${mil.ebs.ctm.Account.list()}" optionKey="id" required="" value="${newsItemInstance?.author?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: newsItemInstance, field: 'status', 'error')} ">
	<label for="status">
		<g:message code="newsItem.status.label" default="Status" />
	</label>
	<g:select name="status" from="${newsItemInstance.constraints.status.inList}" value="${newsItemInstance?.status}" valueMessagePrefix="newsItem.status" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: newsItemInstance, field: 'versions', 'error')} ">
	<label for="versions">
		<g:message code="newsItem.versions.label" default="Versions" />
	</label>
    <ul class="one-to-many">
        <g:each in="${newsItemInstance?.versions?}" var="v">
            <li><g:link controller="version" action="show" id="${v.id}">${v?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="version" action="create" params="['newsItem.id': newsItemInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'version.label', default: 'Version')])}</g:link>
        </li>
    </ul>
</div>

