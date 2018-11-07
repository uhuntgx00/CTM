<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="Conference Data" />
		<title>Upload Conference Data</title>
	</head>

    <body>
        %{--<calendar:resources theme="aqua"/>--}%
        <a href="#list-conferenceData" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="upload-conferenceData" class="content scaffold-list" role="main">
            <h1>Upload Conference Data</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:form controller="conferenceUpload" method="post" action="save" enctype="multipart/form-data">
                <div class="dialog">
                    <table>
                        <tbody>
                            %{--<tr class="prop">--}%
                                %{--<td><label for="asOfDate">File Valid as of:</label><calendar:datePicker name="asOfDate"/></td>--}%
                            %{--</tr>--}%
                            <tr class="prop">
                                <td valign="top" class="name"><input maxlength="100" size="50" type="file" name="file"/></td>
                            </tr>
                            <tr class="prop">
                                <td><g:submitButton name="submit" value="Submit ${entityName} EXCEL File (*.xlsx) to be Processed" /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </g:form>
        </div>

    </body>
</html>