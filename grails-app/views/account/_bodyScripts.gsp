<script type="text/javascript">
    $(document).ready(function(){
        $( document ).tooltip();

        $('#employeeType').multiselect({
            noneSelectedText: 'Select Employee Type',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 200
        });

        $('#assignedTD').multiselect({
            noneSelectedText: 'Select Assigned Organization',
            multiple: false,
            header: true,
            selectedList: 1,
            minWidth: 650
        }).multiselectfilter();

        $('#supervisor').multiselect({
            noneSelectedText: 'Select Supervisor',
            multiple: false,
            header: true,
            selectedList: 1,
            minWidth: 650
        }).multiselectfilter();

        $('#rankGrade').multiselect({
            noneSelectedText: 'Select Rank/Grade',
            multiple: false,
            header: true,
            selectedList: 1,
            minWidth: 650
        }).multiselectfilter();

    })
</script>
