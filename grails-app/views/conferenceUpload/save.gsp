<!doctype html>
    <html>
        <head>
            <meta name="layout" content="main">
                <g:set var="entityName" value="${message(code: 'evalSplit.label', default: 'Eval Data')}" />
                <title>
                    <g:message code="default.show.label" args="[entityName]" />
                </title>
            </head>

            <body>
                <a href="#list-evalSplit" class="skip" tabindex="-1">
                    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
                </a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/', absolute: 'true')}"><g:message code="default.home.label"/></a></li>
                    </ul>
                </div>
                <div id="show-evalSplit" class="content scaffold-list" role="main">
                    <h1>
                        <g:message code="default.show.label" args="[entityName]" />
                    </h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <div class="dialog">
                        <table>
                            <tbody>
                                <tr class="prop">
                                    <td valign="top" class="name">Document:</td>
                                    <td valign="top" class="value">${upload.fileName}  [as of: <g:formatDate format="dd MMM yyyy" date="${fileAsOfDate}"/>]</td>
                                </tr>
                                <tr class="prop">
                                    <td valign="top" class="name">Data Uploaded:</td>
                                    <td valign="top" class="value">${upload.status}</td>
                                </tr>
                                <tr>
                                    <td valign="top" class="name">Parse Results:</td>
                                    <g:if test="${validDataRows == 0 && errorRows == 0 && blankRows == 0}">
                                        <td>No Records Parsed</td>
                                    </g:if>
                                    <g:else>
                                        <td>
                                            <table border="1">
                                                <tr>
                                                    <th>Total Rows</th>
                                                    <th>Valid Rows</th>
                                                    <th>Rows with Errors</th>
                                                    <th>Blank Rows (Blank Rows are Ignored)</th>
                                                </tr>
                                                <tr>
                                                    <td align="center">${totalRows}</td>
                                                    <td align="center">${validDataRows}</td>
                                                    <td align="center">${errorRows}</td>
                                                    <td align="center">${blankRows}</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </g:else>
                                </tr>
                                <g:if test="${errors}">
                                    <tr class="prop">
                                        <td valign="top" class="name">Errors:</td>
                                        <td valign="top" class="value">
                                            <ul>
                                                <g:each in="${errors}" var='errorMessage'>
                                                    <li>${errorMessage}</li>
                                                </g:each>
                                            </ul>
                                        </td>
                                    </tr>
                                </g:if>
                            </tbody>
                        </table>
                    </div>
                </div>

            </body>
        </html>