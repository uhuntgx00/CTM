<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.news.NewsItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'newsItem.label', default: 'NewsItem')}" />
		<title>Latest News</title>
	</head>
	<body>
		<a href="#list-newsItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-newsItem" class="content scaffold-list" role="main">
			<h1>Latest News</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:each in="${newsItemInstanceList}" var="newsItem">
                <article class="item">
                    <header>
                        <h3>
                            <g:link controller="newsItem" action="show" id="${newsItem?.id}">
                                ${newsItem?.title?.encodeAsHTML()}
                            </g:link>
                        </h3>
                        <p class="author">
                            Submitted by <a href="#">${newsItem?.author}</a>,
                            published on <g:formatDate date="${newsItem?.dateCreated}" dateStyle="Full" type="date"/>
                        </p>
                    </header>
                    <div class="articleBody">
                        ${newsItem?.body}
                    </div>
                </article>
            </g:each>

		</div>
	</body>
</html>
