<%@ page import="mil.ebs.ctm.Attendee" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>About CTM</title>
        <style type="text/css" media="screen">
            #page-body {
                text-align: center;
                margin: 1em 1em 1em 1em;
            }

            h1 {
                margin-top: 1em;
                margin-bottom: 0.3em;
                font-size: 2em;
            }

            h2 {
                margin-top: 1em;
                margin-bottom: 0.3em;
                font-size: 1em;
            }

            p {
                line-height: 1.5;
                margin: 0.25em 0;
            }

            #controller-list ul {
                list-style-position: inside;
            }

            #controller-list li {
                line-height: 1.3;
                list-style-position: inside;
                margin: 0.25em 0;
            }

            @media screen and (max-width: 480px) {
                #page-body {
                    margin: 0 1em 1em;
                }

                #page-body h1 {
                    margin-top: 0;
                }
            }
        </style>
	</head>
	<body>
		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="page-body" role="main">
			<h1><span style="font-size: 1.5em">About</span><br/>Conference Tracking & Management (CTM)</h1>
            <h2>This software subject to the license agreement set forth with AFRL.</h2>
			<div id="controller-list" role="navigation">
                <br/>
                <br/>
                <p><span style="font-size: 16pt"><strong>Product Champion(s):</strong> Cheryl Collazo <em>(Government)</em>, Patrick Jackson <em>(Government</em></span></p>

                <br/>
                <p><span style="font-size: 14pt"><strong>Project Management:</strong> Bret Stoneking <em>(Government)</em></span></p>

                <br/>
                <p><span style="font-size: 14pt"><strong>Engineering:</strong> Guy Hunter <em>(Jacobs)</em></span></p>

                <br/>
                <p><span style="font-size: 14pt"><strong>DBA:</strong> Thomas Hassig <em>(Jacobs)</em></span></p>

                <br/>
                <p><span style="font-size: 14pt"><strong>Requirements Analyst:</strong> Patricia Snavely <em>(Jacobs)</em></span></p>

                <br/>
                <p><span style="font-size: 14pt"><strong>Testing:</strong> Jenna Rahrig <em>(Jacobs)</em></span></p>

                <br/>
                <p><span style="font-size: 12pt"><strong>TRAINING:</strong> John Corwin <em>(Peerless Technologies)</em>, Linda Thomas <em>(Peerless Technologies)</em></span></p>

                <br/>
                <p><span style="font-size: 12pt"><strong>AFRL EBS Operations Support:</strong> James Yannekis <em>(RCF)</em>, Peter Magee <em>(RCF)</em></span></p>
			</div>
		</div>

	</body>
</html>
