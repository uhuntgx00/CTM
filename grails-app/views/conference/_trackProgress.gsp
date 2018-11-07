
<!--[if (gt IE 8)|!(IE)]><!-->

<g:if test="${conferenceInstance?.responsibleOrg}">

    <g:if test="${conferenceInstance?.status?.equalsIgnoreCase('Disapproved')}">
        <div class="track-progress" data-steps="5">
            <center>
            <li <g:if test="${conferenceInstance?.isDone('Open', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">class="current"</g:if>><span>Open</span><i></i></li><!--
         --><li <g:if test="${conferenceInstance?.isDone('External', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("External")}">class="current"</g:if>><span>External</span></li><!--
         --><li <g:if test="${conferenceInstance?.isDone('Closed', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Closed")}">class="current"</g:if>><span>Disapproved</span><i></i></li>
            </center>
        </div>
    </g:if>
    <g:else>
        <div class="track-progress" data-steps="5">
            <center>
            <li <g:if test="${conferenceInstance?.isDone('Open', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">class="current"</g:if>><span>Open</span><i></i></li><!--
         --><li <g:if test="${conferenceInstance?.isDone('External', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("External")}">class="current"</g:if>><span>External</span></li><!--
         --><li <g:if test="${conferenceInstance?.isDone('Approved', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Approved")}">class="current"</g:if>><span>Approve</span></li><!--
         --><li <g:if test="${conferenceInstance?.isDone('Finalizing', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Finalizing")}">class="current"</g:if>><span>Final</span></li><!--
         --><li <g:if test="${conferenceInstance?.isDone('Closed', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Closed")}">class="current"</g:if>><span>Archive</span><i></i></li>
            </center>
        </div>
    </g:else>

</g:if>
<g:else>

    <g:if test="${conferenceInstance?.status?.equalsIgnoreCase('Cancelled')}">
        <div class="track-progress" data-steps="2">
            <center>
            <li <g:if test="${conferenceInstance?.isDone('Open', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">class="current"</g:if>><span>Open</span><i></i></li><!--
         --><li <g:if test="${conferenceInstance?.isDone('Closed', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Closed")}">class="current"</g:if>><span>Cancelled</span><i></i></li>
            </center>
        </div>
    </g:if>
    <g:else>
        <g:if test="${conferenceInstance?.status?.equalsIgnoreCase('Disapproved')}">
            <g:if test="${csb?.constrainedTotal < 20000}">
                <div class="track-progress" data-steps="5">
                    <center>
                    <li <g:if test="${conferenceInstance?.isDone('Open', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">class="current"</g:if>><span>Open</span><i></i></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Create Package', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Create Package")}">class="current"</g:if>><span>Create</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('TD Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("TD Review")}">class="current"</g:if>><span>TD</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('AFRL Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">class="current"</g:if>><span>AFRL</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Closed', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Closed")}">class="current"</g:if>><span>Disapproved</span><i></i></li>
                    </center>
                </div>
            </g:if>
            <g:else>
                <div class="track-progress" data-steps="7">
                    <center>
                    <li <g:if test="${conferenceInstance?.isDone('Open', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">class="current"</g:if>><span>Open</span><i></i></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Create Package', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Create Package")}">class="current"</g:if>><span>Create</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('TD Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("TD Review")}">class="current"</g:if>><span>TD</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('AFRL Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">class="current"</g:if>><span>AFRL</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('AFMC Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFMC Review")}">class="current"</g:if>><span>AFMC</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('SAF Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("SAF Review")}">class="current"</g:if>><span>SAF</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Closed', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Closed")}">class="current"</g:if>><span>Disapproved</span><i></i></li>
                    </center>
                </div>
            </g:else>
        </g:if>
        <g:else>
            <g:if test="${csb?.constrainedTotal < 20000}">
                <div class="track-progress" data-steps="7">
                    <center>
                    <li <g:if test="${conferenceInstance?.isDone('Open', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">class="current"</g:if>><span>Open</span><i></i></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Create Package', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Create Package")}">class="current"</g:if>><span>Create</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('TD Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("TD Review")}">class="current"</g:if>><span>TD</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('AFRL Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">class="current"</g:if>><span>AFRL</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Approved', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Approved")}">class="current"</g:if>><span>&nbsp;&nbsp;&nbsp;Approve</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Finalizing', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Finalizing")}">class="current"</g:if>><span>Final</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Closed', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Closed")}">class="current"</g:if>><span>&nbsp;&nbsp;Archive</span><i></i></li>
                    </center>
                </div>
            </g:if>
            <g:else>
                <div class="track-progress" data-steps="9">
                    <center>
                    <li <g:if test="${conferenceInstance?.isDone('Open', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Open")}">class="current"</g:if>><span>Open</span><i></i></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Create Package', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Create Package")}">class="current"</g:if>><span>Create</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('TD Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("TD Review")}">class="current"</g:if>><span>TD</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('AFRL Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFRL Review")}">class="current"</g:if>><span>AFRL</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('AFMC Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("AFMC Review")}">class="current"</g:if>><span>AFMC</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('SAF Review', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("SAF Review")}">class="current"</g:if>><span>SAF</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Approved', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Approved")}">class="current"</g:if>><span>&nbsp;&nbsp;&nbsp;Approve</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Finalizing', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Finalizing")}">class="current"</g:if>><span>Final</span></li><!--
                 --><li <g:if test="${conferenceInstance?.isDone('Closed', conferenceInstance?.phaseState)}">class="done"</g:if><g:if test="${conferenceInstance?.phaseState?.equalsIgnoreCase("Closed")}">class="current"</g:if>><span>&nbsp;&nbsp;Archive</span><i></i></li>
                    </center>
                </div>
            </g:else>
        </g:else>
    </g:else>

</g:else>

<!--<![endif]-->
