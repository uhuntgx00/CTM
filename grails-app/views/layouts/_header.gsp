%{--<div id="grailsLogo" role="banner">--}%
    %{--<a id="home1" class="top_link" href="${createLink(uri: '/', absolute: 'true')}">--}%
        %{--<sec:ifAnyGranted roles="ROLE_LOGO">--}%
            %{--<img src="${resource(dir: 'images', file: 'ebs_eti_logo_dk1.png')}" alt="EBS FM Conference Tracker"/>--}%
        %{--</sec:ifAnyGranted>--}%
        %{--<sec:ifNotGranted roles="ROLE_LOGO">--}%
            %{--<img src="${resource(dir: 'images', file: 'ebs_eti_logo_a.png')}" alt="EBS FM Conference Tracker"/>--}%
        %{--</sec:ifNotGranted>--}%
    %{--</a>--}%
%{--</div>--}%

<div>
    <table id="header">
	    <tr class="header-tr">
		    <td id="logo">
                <span class='logo'>
                    <a tabindex="-1" id="homepage" class="top_link" href="https://ebs.afrl.af.mil/"><g:img dir="images" file="EBSLogo_black.png" height="36" width="142" alt="EBS Homepage" title="EBS Homepage"/></a>
                    <g:img dir="images" file="conference.png" height="36" width="36" alt="CTM Logo" title="CTM Logo"/>
                    <a tabindex="-1" id="home" class="top_link" href=${createLink(uri: '/', absolute: 'true')}><span id="logo_title" title="CTM" alt="CTM">Conference Tracking & Management (CTM)</span></a>
                </span>
            </td>
			<td id="header-right">
				<table class="header-table">
					<tr class="header-tr">
						<td class="header-link">
                            %{--<span class='header-text'><g:link tabindex="-1" controller="contact" absolute="true" title="CTM Contact Page" alt="CTM Contact Page">Contact</g:link> | <a tabindex="-1" href="https://cs2.eis.afmc.af.mil/sites/afrlebs/pages/CTM.aspx" title="AFRL CTM Training" alt="AFRL CTM Training">Training</a> | <a tabindex="-1" href="#">Support</a> | <a tabindex="-1" href="https://cs2.eis.afmc.af.mil/sites/EBSPO/tdapps/CTM1/Lists/FAQ/AllItems.aspx" title="FAQ" alt="FAQ">FAQ</a>&nbsp;&nbsp;&nbsp;</span>--}%
                            <span class='header-text'><g:link tabindex="-1" controller="contact" absolute="true" title="CTM Contact Page" alt="CTM Contact Page">Contact</g:link> | <a tabindex="-1"  href="https://cs2.eis.afmc.af.mil/sites/EBSPO/tdapps/CTM1/Lists/FAQ/AllItems.aspx" target="_blank" title="FAQ" alt="FAQ">FAQ</a>&nbsp;&nbsp;&nbsp;</span>
                        </td>
						<td class="social">
						    <div style="text-align:left;">
                                <div style="height:0px;overflow:hidden"></div>
                                <span class="wsite-social wsite-social-default">
                                    <a tabindex="-1" class='wsite-social-item wsite-social-mail' href='mailto:mail@ebs.com' target='_blank'>
                                        <span class='wsite-social-item-inner'></span>
                                    </a>
                                    <a tabindex="-1" class='wsite-social-item wsite-social-rss' href='http:///feed.rss' target='_blank'>
                                        <span class='wsite-social-item-inner'></span>
                                    </a>
                                </span>
                                <div style="height:0px;overflow:hidden"></div>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>


