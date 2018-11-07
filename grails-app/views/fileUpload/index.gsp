<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fileUpload.label', default: 'File')}" />
		<title><g:message code="default.upload.label" args="[entityName]" /></title>
	</head>

    <body>
        <a href="#list" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div id="upload" class="content scaffold-list" role="main">
            <h1>
                File Uploads for <b>${conference}</b>
            </h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:form controller="fileUpload" method="post" action="save" enctype="multipart/form-data">
                <g:hiddenField name="conference_id" value="${conference_id}" />
   				<g:hiddenField name="conference" value="${conference}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td colspan="2">&nbsp;</td>
                                <td><center><i>Current Version<br>Count</i></center></td>
                                <td>&nbsp;</td>
                                <td valign="middle"><i><br>Input comments for each file to be uploaded</i></td>
                                <td>&nbsp;</td>
                            </tr>

                            <tr class="prop">
                                <td>&nbsp;</td>
                                <td align="right"><b>After Action Report (XLS)</b></td>
                                <td><center>[ ${aar_v} ]</center></td>
                                <td align="left"><input maxlength="100" size="50" type="file" name="aar_file"/></td>
                                <td align="right"><input maxlength="200" size="50" type="text" name="aar_comment"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr class="prop">
                                <td>&nbsp;</td>
                                <td align="right"><b>SAF Memo (?)</b></td>
                                <td><center>[ ${safMemo_v} ]</center></td>
                                <td align="left"><input maxlength="100" size="50" type="file" name="safMemo_file"/></td>
                                <td align="right"><input maxlength="200" size="50" type="text" name="safMemo_comment"/></td>
                                <td>&nbsp;</td>
                            </tr>

                            <tr><td colspan="6">&nbsp;</td></tr>

                            <tr class="prop">
                                <td colspan="6"><center><g:submitButton name="submit" value="Submit Files to be Uploaded to Conference"/></center></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </g:form>
        </div>
    </body>
</html>