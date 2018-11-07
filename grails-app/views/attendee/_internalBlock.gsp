
    <g:if test="${supervisor}">
        <div class="fieldcontain">
            <label for="supervisor">
                <g:message code="attendee.supervisor.label" default="Supervisor" />
            </label>
            <g:link controller="account" action="show" id="${supervisor?.id}">${supervisor?.encodeAsHTML()}</g:link>
        </div>
    </g:if>

    <g:if test="${rankGrade}">
        <div class="fieldcontain">
            <label for="rankGrade">
                <g:message code="attendee.rankGrade.label" default="Rank/Grade" />
            </label>
            ${rankGrade}
        </div>
    </g:if>

    <script type="text/javascript">
        $('#accountLink').multiselect({
            noneSelectedText: 'Select Account',
            multiple: false,
            header: true,
            selectedList: 1,
            minWidth: 650
        }).multiselectfilter();

        if ($('#accountType option:selected').text() == "Internal") {
            $('#rankGradeBlock').show();
        }
    </script>
