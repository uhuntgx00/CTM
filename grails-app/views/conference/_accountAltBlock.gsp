
    <div id="accountAltBlock">
        <div class="fieldcontain ${hasErrors(bean: conferenceInstance, field: 'alternateCAO', 'error')} ">
            <label for="alternateCAO">
                <g:message code="conference.alternateCAO.label" default="Alternate CAO" />
            </label>
            <g:select id="alternateCAO" name="alternateCAO.id" from="${accountList}" optionValue="name" optionKey="id" class="many-to-one" noSelection="['null': '']"/>
        </div>
    </div>

    <script type="text/javascript">
        $('#alternateCAO').multiselect({
            noneSelectedText: 'Select Alternate CAO',
            multiple: false,
            header: true,
            selectedList: 1,
            minWidth: 650
        }).multiselectfilter();

        $('#accountAltBlock').show();
    </script>

