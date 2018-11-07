
<%@ page import="mil.ebs.ctm.Attendee; mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
		<title>
            Chart&nbsp;(${listType})
        </title>
        <g:javascript library="raphael"/>
        <g:render template="attendanceType"/>
	</head>
	<body>
		<a href="#list-conference" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-conference" class="content scaffold-list" role="main">
			<h1>
                %{--<g:img dir="/images/icons" file="calendar_512.png" height="32" width="32" alt="Conference List" title="Conference List"/>--}%
                Chart&nbsp;(${listType})
            </h1>
			%{--<g:if test="${flash.message}">--}%
				%{--<div class="message" role="status">${flash.message}</div>--}%
			%{--</g:if>--}%

            <br/>
            <div id="raphael"></div>

            %{--<g:if test="${acd?.totalCount > 0}">--}%
                %{--<center>--}%
                %{--<g:pieChart title='Percentage of Attendance by Type (${acd?.totalCount})'--}%
                            %{--size="${[600, 400]}"--}%
                            %{--colors="${['FF0000','FFFF00','00FF00','00FFFF','0000FF','FF00FF','CCCCCC']}"--}%
                            %{--labels="${mil.ebs.ctm.Attendee.constraints.attendanceType.inList}"--}%
                            %{--fill="${'bg,s,e0edf6'}"--}%
                            %{--dataType='simple'--}%
                            %{--data='${acd?.countList}' />--}%
                %{--</center>--}%
            %{--</g:if>--}%
		</div>
	</body>
</html>
