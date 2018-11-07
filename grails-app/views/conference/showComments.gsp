
<%@page defaultCodec="none" %>
<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
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
	<body class="soria">
		<a href="#show-comments" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                  <li><g:link class="show" action="show" id="${conferenceInstance?.id}" absolute="true">Return to <i>${conferenceInstance}</i></g:link></li>
            </ul>
        </div>

		<div id="show-comments" class="content scaffold-show" role="main">
			<h1 class="comments">Comments for ${conferenceInstance}</h1>

			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <div class="comments">
                <ul class="unstyled">
                    <g:if test="${!conferenceInstance?.comments}">
                        <li class="comments-even">
                            <span style="color:#999999;text-align:center;"><i>No Comments<br/></i></span>
                        </li>
                    </g:if>

                    <g:if test="${conferenceInstance?.comments}">
                        <g:each in="${conferenceInstance.comments.sort { it.id }}" status="i" var="c">
                            <li class="${(i % 2) == 0 ? 'comments-even' : 'comments-odd'}">
                                <span style="color:#999999;font-size:12px"><i><g:formatDate date="${c?.when}" type="date" style="FULL"/><g:if test="${c?.phase}">&nbsp;(<b>${c?.phase}</b>)</g:if></i></span>
                                <sec:ifAnyGranted roles="ROLE_ADMIN">
                                    <g:link action="deleteComment" resource="${conferenceInstance}" absolute="true" params="[commentId: "${c?.id}"]" onclick="return confirm('${message(code: 'default.button.deleteConferenceComment.confirm.message', default: 'Are you sure you want to delete this Conference Comment?')}');"><g:img dir="/images" file="reject_16n.png" alt="Delete comment!" title="Delete comment!"/></g:link>
                                </sec:ifAnyGranted>
                                <br/>
                                ${c?.eComment}
                                <br/>
                                <span style="color:#999999;font-size:12px"><i>${c?.who}</i></span>
                            </li>
                        </g:each>
                    </g:if>
                    <hr/>
                </ul>
            </div>

            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_TD_ADMIN, ROLE_TD_FULL_ADMIN, ROLE_FMC_ADMIN">
                <g:form controller="conference" action="addComment">
                    <div class="fieldcontain ${hasErrors(bean: conferenceCommentInstance, field: 'who', 'error')} ">
                        <label for="who">
                            <g:message code="conferenceComment.who.label" default="Who" />
                        </label>
                        <span style="color:#999999" id="who"><i>${who}</i></span>
                    </div>

                    <div class="fieldcontain ${hasErrors(bean: conferenceCommentInstance, field: 'when', 'error')} ">
                        <label for="when">
                            <g:message code="conferenceComment.when.label" default="When" />
                        </label>
                        <span style="color:#999999" id="when"><i><g:formatDate date="${new Date()}" type="date" style="FULL"/></i></span>
                    </div>

                    <div class="fieldcontain ${hasErrors(bean: conferenceCommentInstance, field: 'eComment', 'error')} ">
                        <label for="eComment">
                            <g:message code="conferenceComment.eComment.label" default="Comment" />
                        </label>
                        <g:textArea name="eComment" wrap="hard" cols="100" rows="5" maxlength="2000" value="${evalCommentInstance?.eComment}"/>
                    </div>

                    <br/>

                    <fieldset class="buttons">
                        <g:hiddenField name="id" value="${conferenceInstance?.id}" />
                        %{--<sec:ifAnyGranted roles="ROLE_ADMIN">--}%
                            <g:submitButton name="addComment" class="save" value="${message(code: 'default.button.add.label', default: 'Add Comment')}" />
                        %{--</sec:ifAnyGranted>--}%
                    </fieldset>
                </g:form>
            </sec:ifAnyGranted>
		</div>

	</body>
</html>
