<!doctype html>
    <html>
        <head>
            <meta name="layout" content="main">
            <g:set var="entityName" value="${message(code: 'conferenceFile.label', default: 'Conference File')}" />
            <title>
                <g:message code="default.show.label" args="[entityName]" />
            </title>
            <meta http-equiv="refresh" content="3; url=/CTM/conference/show/${conference_id}">
        </head>

        <body>
            <a href="#list-hqc" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
            </a>
            <div id="show-hqc" class="content scaffold-list" role="main">
                <h1>
                    <g:message code="default.show.label" args="[entityName]" /> information for <b>${conference}</b>
                </h1>
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td colspan="2">&nbsp;</td>
                            </tr>

                            <g:each in="${uploadedFiles}" var="f">
                                <g:if test="${f}">
                                    <tr class="prop">
                                        <td valign="top" class="name">Document Uploaded:</td>
                                        <td valign="top" class="value">${f?.fileName} | ${f?.fileType} | ${f?.fileDate} | ${f?.loadedBy} | ${f?.comments}</td>
                                    </tr>
                                </g:if>
                            </g:each>

                        </tbody>
                    </table>
                </div>
                <center><g:link class="show" controller="conference" action="show" id="${conference_id}" absolute="true">Return to <i>${conference}</i></g:link></center>
            </div>

        </body>
    </html>