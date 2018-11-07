<script type="text/javascript">
    $(document).ready(function(){
        $( document ).tooltip();

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

                updateTotal()
            }
        });

        $( "#fundSource1" ).val( "Other US Govt: " + $("#fundSource1a").val() + "%");
        $( "#fundSource2" ).val( "US Air Force: " + $("#fundSource2a").val() + "%");
        $( "#fundSource3" ).val( "Non-Federal Entity: " + $("#fundSource3a").val() + "%");

        updateTotal()

        $("#fundSourceSlider").slider({values: [Number($("#fundSource1a").val()), 100-Number($("#fundSource3a").val())]});
    })

    function updateTotal() {
        var x = 0;

        var cost1 = Number($("#registrationCost").val());
        var cost2 = Number($("#airfareCost").val());
        var cost3 = Number($("#localTravelCost").val());
        var cost4 = Number($("#lodgingCost").val());
        var cost5 = Number($("#lodgingCostTax").val());
        var cost6 = Number($("#mealsIncidentalCost").val());
        var cost7 = Number($("#otherCost").val());

        x = cost1 + cost2 + cost3 + cost4 + cost5 + cost6 + cost7;

        $( "#total").val(x.toFixed(2));

        $( "#fundSource1num" ).val( Number((Number($("#fundSource1a").val()) * x / 100).toFixed(2)) );
        $( "#fundSource2num" ).val( Number((Number($("#fundSource2a").val()) * x / 100).toFixed(2)) );
        $( "#fundSource3num" ).val( Number((Number($("#fundSource3a").val()) * x / 100).toFixed(2)) );

        $( "#fundSource1numa" ).val( Number($("#fundSource1a").val()) * x / 100);
        $( "#fundSource2numa" ).val( Number($("#fundSource2a").val()) * x / 100);
        $( "#fundSource3numa" ).val( Number($("#fundSource3a").val()) * x / 100);
    }
</script>
