<%@ page import="org.springframework.validation.FieldError" %>
<!doctype html>
    <html>
        <head>
            <meta name="layout" content="main">
                <g:set var="entityName" value="${message(code: 'Contact', default: 'Contact')}" />
                <title>Contact Information</title>
        </head>
        <body>
            <a href="#create-contact" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
            </a>
            <div id="create-contact" class="content scaffold-create" role="main">
                <h1>Contact Information</h1>
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${contactInstance}">
                    <ul class="errors" role="alert">
                        <g:eachError bean="${contactInstance}" var="error">
                            <li <g:if test="${error in FieldError}">data-field-id="${error.field}"</g:if>>
                                <g:message error="${error}"/>
                            </li>
                        </g:eachError>
                    </ul>
                </g:hasErrors>
                <br/>

                <p style="padding-left: 30px">For system issues with the CTM application or server:<br/> Please contact <a href="mailto:afrl.ebs.ops.support@us.af.mil"><g:img dir="images/icons" file="Email-Sending-512.png" height="16" width="16" alt="Send EMail to AFRL EBS Operations Support" title="Send EMail to AFRL EBS Operations Support"/>&nbsp;AFRL EBS Operations Support</a> or call DSN: <b>986-3921</b> / Commercial: <b>(937) 656-3921</b>.</p>
                <br/>
                <p style="padding-left: 30px">For issues with conference business policies, processes, attendance, and approval:<br/> Please contact <a href="mailto:afrl.hq.conf.poc@us.af.mil"><g:img dir="images/icons" file="Email-Sending-512.png" height="16" width="16" alt="Send EMail to AFRL CTM Support" title="Send EMail to AFRL CTM Support"/>&nbsp;AFRL/FMC POC</a> or call AFRL/FMC - DSN: <b>986-6271</b> / Commercial: <b>(937) 656-6271</b>.</p>
                <br/>
                <p style="padding-left: 30px">For training questions and/or to access the CTM training materials:<br/> Please contact <a href="mailto:afrl.ebs.training@us.af.mil"><g:img dir="images/icons" file="Email-Sending-512.png" height="16" width="16" alt="Send EMail to EBS Training Team" title="Send EMail to EBS Training Team"/>&nbsp;EBS Training Team</a> and/or access the <a href="https://cs2.eis.afmc.af.mil/sites/afrlebs/pages/CTM.aspx">CTM Training Site</a>.</p>

                <br/>
                <br/>
            </div>
        </body>
    </html>
