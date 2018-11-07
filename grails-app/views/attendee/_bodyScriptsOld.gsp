<script type="text/javascript">
    $(document).ready(function(){
        $( document ).tooltip();

        updateDatePicker();

        $("#spinner").ajaxComplete (function(event, request, settings){
            updateDatePicker();
        });

        $('#fundSourceSlider').slider({
            range: true,
            min: 0,
            max: 100,
            values: [0, 100],
            slide: function( event, ui ) {
                $( "#fundSource1" ).val( "Other US Govt: " + ui.values[ 0 ] + "%");
                $( "#fundSource2" ).val( "US Air Force: " + (ui.values[ 1 ] - ui.values[ 0 ]) + "%");
                $( "#fundSource3" ).val( "Non-Federal Entity: " + (100 - ui.values[ 1 ]) + "%");
                $( "#fundSource1a" ).val(ui.values[0]);
                $( "#fundSource2a" ).val(ui.values[1] - ui.values[0]);
                $( "#fundSource3a" ).val(100 - ui.values[1]);
            }
        });

        $( "#fundSource1" ).val( "Other US Govt: " + $("#fundSource1a").val() + "%");
        $( "#fundSource2" ).val( "US Air Force: " + $("#fundSource2a").val() + "%");
        $( "#fundSource3" ).val( "Non-Federal Entity: " + $("#fundSource3a").val() + "%");

        $("#mealsIncluded").spinner();
        $("#hoursAttendanceType").spinner();

        $('#reservedTD').multiselect({
            noneSelectedText: 'Select Technology Directorate',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 650
        });

        $('#accountLink').multiselect({
            noneSelectedText: 'Select Account',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 650
        });

        $('#attendanceType').multiselect({
            noneSelectedText: 'Select Attendance Type',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 200
        });
    })
</script>
