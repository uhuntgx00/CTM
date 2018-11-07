<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fileUpload.label', default: 'File')}" />
		<title>Conference Package File Upload</title>
	</head>

    <body>
        <a href="#list" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div id="upload" class="content scaffold-list" role="main">
            <h1>
                Conference Package File Upload for <b>${conference}</b>
            </h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:form controller="fileUpload" method="post" action="savePackage" enctype="multipart/form-data">
                <g:hiddenField name="conference_id" value="${conference_id}" />
   				<g:hiddenField name="conference" value="${conference}" />
                <g:hiddenField name="refId" value="${refId}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td colspan="2">&nbsp;</td>
                                <td>&nbsp;</td>
                                <td valign="middle"><i><br>Input comments for the file to be uploaded</i></td>
                                <td>&nbsp;</td>
                            </tr>

                            <tr class="prop">
                                <td>&nbsp;</td>
                                <td align="right"><b>Conference Request Form (CRF)</b></td>
                                <td align="left"><input maxlength="100" size="50" type="file" name="crf_file"/></td>
                                <td align="right"><input maxlength="200" size="50" type="text" name="crf_comment"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr class="prop">
                                <td>&nbsp;</td>
                                <td align="right"><b>Agenda</b></td>
                                <td align="left"><input maxlength="100" size="50" type="file" name="agenda_file"/></td>
                                <td align="right"><input maxlength="200" size="50" type="text" name="agenda_comment"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr class="prop">
                                <td>&nbsp;</td>
                                <td align="right"><b>CAPE</b></td>
                                <td align="left"><input maxlength="100" size="50" type="file" name="cape_file"/></td>
                                <td align="right"><input maxlength="200" size="50" type="text" name="cape_comment"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr class="prop">
                                <td>&nbsp;</td>
                                <td align="right"><b>TD Document</b></td>
                                <td align="left"><input maxlength="100" size="50" type="file" name="td_file"/></td>
                                <td align="right"><input maxlength="200" size="50" type="text" name="td_comment"/></td>
                                <td>&nbsp;</td>
                            </tr>

                            <tr><td colspan="6">&nbsp;</td></tr>

                            <tr class="prop">
                                <td colspan="6"><center><g:submitButton name="submit" value="Submit File to be Uploaded to Conference"/></center></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </g:form>
        </div>
    </body>
</html>