<%@ page import="mil.ebs.ctm.Attendee" %>
<center>
    <g:if test="${acd?.totalCount > 0}">
        <g:pieChart title='Percentage of Attendance by Type (${acd?.totalCount})'
                    colors="${['FF0000','FFFF00','00FF00','00FFFF','0000FF','FF00FF','CCCCCC']}"
                    labels="${Attendee.constraints.attendanceType.inList}"
                    fill="${'bg,s,e0edf6'}"
                    dataType='simple'
                    data='${acd?.countList}' />
    </g:if>

    <g:if test="${ccd?.totalCost > 0}">
        <g:pieChart title='Total Cost by Type (\$${(long) ccd?.totalCost})'
                    colors="${['FF0000','FFFF00','00FF00','00FFFF','0000FF','FF00FF','CCCCCC']}"
                    labels="${Attendee.constraints.attendanceType.inList}"
                    fill="${'bg,s,e0edf6'}"
                    dataType='simple'
                    data='${ccd?.costList}' />
    </g:if>
</center>
