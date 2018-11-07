<script type="text/javascript">
    jQuery(document).ready(function($){
        $( document ).tooltip();

        updateDatePicker();

        $("#spinner").ajaxComplete (function(event, request, settings){
            updateDatePicker();
        });

        $('#reservedOrgBlock').hide();
        if ($('#reservedTD option:selected').text() == "Other (Non-AFMC)") {
            $('#reservedOrgBlock').show();
        }
        else if ($('#reservedTD option:selected').text() == "Attendee slot not reserved for TD") {
            $('#reservedOrgBlock').show();
        }

        $('#reservedTD').change(function () {
            if ($('#reservedTD option:selected').text() == "Other (Non-AFMC)") {
                $('#reservedOrgBlock').show();
            }
            else if ($('#reservedTD option:selected').text() == "Attendee slot not reserved for TD") {
                $('#reservedOrgBlock').show();
            }
            else {
                $('#reservedOrg').val("");
                $('#reservedOrgBlock').hide();
            }
        });

        $('#accountLinkBlock').hide();
        $('#accountNameBlock').hide();
        $('#internalBlock').hide();
        $('#externalBlock').hide();
        if ($('#accountType option:selected').text() == "Internal") {
            $('#accountLinkBlock').show();
            $('#internalBlock').show();
        }
        else if ($('#accountType option:selected').text() == "External") {
            $('#accountNameBlock').show();
            $('#externalBlock').show();
        }

        $('#accountType').change(function () {
            if ($('#accountType option:selected').text() == "Internal") {
                $('#accountLinkBlock').show();
                $('#accountNameBlock').hide();
                $('#internalBlock').show();
                $('#externalBlock').hide();
                $('#name').val("");

                if ($('#accountLink option:selected').text() == "") {
                    $('#rankGradeBlock').hide();
                }
            }
            else if ($('#accountType option:selected').text() == "External") {
                $('#accountLinkBlock').hide();
                $('#accountNameBlock').show();
                $('#internalBlock').hide();
                $('#externalBlock').show();

                $('#accountLink').val("")
            }
        });

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
            header: true,
            selectedList: 1,
            minWidth: 650
        }).multiselectfilter();

        $('#status').multiselect({
            noneSelectedText: 'Select Status',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 350
        });

        $('#accountType').multiselect({
            noneSelectedText: 'Select Account Type',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 200
        });

        $('#rankGrade').multiselect({
            noneSelectedText: 'Select Rank/Grade',
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
