
    <label for="accountLink">
        <g:message code="attendee.userLink.label" default="Account Link" />
    </label>
    <g:select id="accountLink" name="accountLink.id" from="${accountList}" optionValue="name" optionKey="id" class="many-to-one" noSelection="['': '']"
          onchange="${remoteFunction(
              controller: 'attendee',
              action: 'ajaxGetInternalAccount',
              params: '\'id=\' + escape(this.value)',
              update: 'internalRankGradeBlock'
          )}"
    />

    <script type="text/javascript">
        $('#accountLink').multiselect({
            noneSelectedText: 'Select Account',
            multiple: false,
            header: true,
            selectedList: 1,
            minWidth: 650
        }).multiselectfilter();

        if ($('#accountType option:selected').text() == "Internal") {
            $('#accountLinkBlock').show();
            $('#internalBlock').show();
            $('#rankGradeBlock').hide();
        }
    </script>
