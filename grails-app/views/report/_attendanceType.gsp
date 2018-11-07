<%@ page import="mil.ebs.ctm.Attendee" %>
<script>
    <g:if test="${acd?.totalCount > 0}">
        window.onload = function () {
            var r = Raphael("raphael"),
                pie1 = r.piechart(600, 160, 120, ${acd?.countList}, {
                    colors: ['#FF0000','#FFFF00','#00FF00','#00FFFF','#0000FF','#FF00FF','#CCCCCC'],
                    legend: [<g:each in="${Attendee.constraints.attendanceType.inList}" var="t">'## ${t.toString()} (%%.%)',</g:each>],
                    legendpos: "east"
                });

            r.text(700, 10, 'Percentage of Attendance by Type (${acd?.totalCount})').attr({ font: "20px sans-serif" });

            pie1.hover(function () {
                this.sector.stop();
                this.sector.scale(1.1, 1.1, this.cx, this.cy);

                if (this.label) {
                    this.label[0].stop();
                    this.label[0].attr({ r: 7.5 });
                    this.label[1].attr({ "font-weight": 800 });
                }
            }, function () {
                this.sector.animate({ transform: 's1 1 ' + this.cx + ' ' + this.cy }, 500, "bounce");

                if (this.label) {
                    this.label[0].animate({ r: 5 }, 500, "bounce");
                    this.label[1].attr({ "font-weight": 400 });
                }
            });
        };
    </g:if>
</script>