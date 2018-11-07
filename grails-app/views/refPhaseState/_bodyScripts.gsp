<script type="text/javascript">
    $(document).ready(function(){
        $( document ).tooltip();

        $('#phaseState').multiselect({
            noneSelectedText: 'Select Phase State',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 300
        });

        $('#nextPhaseState').multiselect({
            noneSelectedText: 'Select Next Phase State',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 300
        });

        $('#actionStatus').multiselect({
            noneSelectedText: 'Select Action Status',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 300
        });

        $('#actionCommand').multiselect({
            noneSelectedText: 'Select Action Command',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 300
        });

        $('#actionCheck').multiselect({
            noneSelectedText: 'Select Action Check',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 300
        });

        $('#actionNotification').multiselect({
            noneSelectedText: 'Select Action Notification',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 300
        });

        $('#dateGateEvent').multiselect({
            noneSelectedText: 'Select Date Gate Event',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 450
        });

    })
</script>
