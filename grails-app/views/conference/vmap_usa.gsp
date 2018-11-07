<%@page defaultCodec="none" %>
<%@ page import="mil.ebs.ctm.Attendee; mil.ebs.ctm.Conference" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'conference.label', default: 'Conference')}" />
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'jqvmap.css')}" type="text/css">
        <g:javascript library="vmap"/>
		<title>
            Conference Map
        </title>

	</head>
	<body>
		<a href="#list-conference" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-conference" class="content scaffold-list" role="main">
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

            <br/>
            <br/>
            <center>
                <div id="vmap" style="width: 1200px; height: 800px;"></div>
            </center>
            <br/>
		</div>

        <script type="text/javascript">

            var data = ${jsonData}

            $(document).ready(function() {

                jQuery('#vmap').vectorMap({
                    map: 'usa_en',
                    backgroundColor: null,
                    color: '#ffffff',
                    hoverOpacity: 0.7,
                    selectedColor: '#666666',
                    enableZoom: true,
                    showTooltip: true,
                    values: data,
                    scaleColors: ['#C8EEFF', '#006491'],
                    normalizeFunction: 'polynomial',
                    onLabelShow: function(element, label, code) {
                        label.text(label.text() + ' (' + parseInt(data[code]) + ')')
                    },
                    onRegionClick: function(element, code, region) {
                        window.location.href = '${createLink(controller: 'conference', action: 'usaList')}/' + code
                    }
                });

            });
        </script>

	</body>
</html>
