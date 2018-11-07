
    <div id="accountCaoBlock"">
        <label for="conferenceAO">
            <g:message code="conference.conferenceAO.label" default="Conference AO" />
        </label>
        <g:select id="conferenceAO" name="conferenceAO.id" from="${accountList}" optionValue="name" optionKey="id" class="many-to-one" noSelection="['null': '']"/>
    </div>

    <script type="text/javascript">
        $('#conferenceAO').multiselect({
            noneSelectedText: 'Select CAO',
            multiple: false,
            header: true,
            selectedList: 1,
            minWidth: 650
        }).multiselectfilter();

        $('#accountCaoBlock').show();
    </script>

