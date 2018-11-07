<script type="text/javascript">
    jQuery(document).ready(function($){
        $( document ).tooltip();

        $('#country').multiselect({
            noneSelectedText: 'Select Country',
            multiple: false,
            header: false,
            selectedList: 1,
            minWidth: 450
        });

    })
</script>
