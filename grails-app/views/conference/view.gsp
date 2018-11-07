
<%@ page import="mil.ebs.ctm.Attendee; mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'fullcalendar.css')}" type="text/css">
        <g:javascript library="full_calendar"/>
		<title>
            Conference Calendar
        </title>

        <style>
        	#calendar {
        		max-width: 1200px;
        		margin: 0 auto;
        	}
        </style>
	</head>
	<body>
		<a href="#list-conference" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-conference" class="content scaffold-list" role="main">
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

        <!--[if (gt IE 8)|!(IE)]><!-->
            <br/>
            <br/>
            <div id='calendar'></div>
            <br/>
        <!--<![endif]-->

		</div>

        <script type="text/javascript">
            $(document).ready(function() {

                // page is now ready, initialize the calendar...
                $('#calendar').fullCalendar({
                    header: {
                        left: 'prev,next today',
                        center: 'title',
                        right: 'month,basicWeek'
                    },
                    eventTextColor: '#FFFFFF',
                    editable: false,
                    eventLimit: true,
                    events: 'viewData',
                    fixedWeekCount: false
                })

            });
        </script>

	</body>
</html>
