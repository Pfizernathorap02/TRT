<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.P2lTrack"%>
<%@ page import="com.pfizer.db.P2lTrackPhase"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.EditMenuWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%            
	EditReportWc wc = (EditReportWc)request.getAttribute(EditReportWc.ATTRIBUTE_NAME);
    P2lTrack track = wc.getTrack();
    List phases = new ArrayList();
    System.out.println("track.getTrackId() " + track.getTrackId());
    if (!(track.getTrackId().startsWith("GROUP"))){
        phases = track.getCompletePhaseList();
    }
%>

<script type="text/javascript" language="javascript"> 
<!-- 
function openP2lWindow(id, type) { 

window.name = "main";
var myW = window.open("","myW","height=500,width=600,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes");
myW.location =  "/TrainingReports/adminHome/editNode?type=" + type + "&id=" + id;
} 
--> 
</script>



<table class="basic_table">
<tr>
    <td rowspan="2">&nbsp;&nbsp;</td>
    <td>&nbsp;</td>
    <td rowspan="2">&nbsp;&nbsp;</td>
</tr>
<tr>
    <td>

<table class="no_space_width"  height="0%"> 
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td colspan="=2">
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25">
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td>
        	<div class="breadcrumb">
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                <a href="/TrainingReports/adminHome/editMenu?id=<%=wc.getMenu().getId()%>"> <%=wc.getMenu().getLabel()%> </a> /
                <%=track.getTrackLabel()%>
        	</div>
        </td>
	</tr>
</table>

<form method="post" class="form_basic" action="/TrainingReports/adminHome/editReport?type=name&track=<%=track.getTrackId()%>">
    <input type="hidden" name="<%=P2lTrack.FIELD_DO_SUBMIT%>" value="yes">
    <% if ((track.getTrackId().startsWith("GROUP"))){%>
    Group Name<%}else{%>Report Name<%}%>&nbsp;&nbsp;<input size="50" type="text" class="text" name="<%=P2lTrack.FIELD_TRACK_LABEL%>" value="<%=track.getTrackLabel()%>">
<% if (!(track.getTrackId().startsWith("GROUP"))){%>
&nbsp;&nbsp;Add to Course Completion:&nbsp;    
<select style="width:50px;"  name="<%=P2lTrack.FIELD_DO_COMPLETE%>">
<%=HtmlBuilder.getOptionsFromLabelValue(P2lTrack.yesNolist,track.getDoComplete()?"Y":"N")%>
</select>
    <%}%>
    <input type="submit" value="Save">
</form>
<% if (!(track.getTrackId().startsWith("GROUP"))){%>
<table class="blue_table" width="100%">
<tr>
<th>&nbsp;</th>

<th>Name</th>
<th>Assigned</th>
<th>Approval</th>
<th>Waived</th>
<th>Sort</th>
<th colspan="1">Activity Name / Course Code</th>
<th>&nbsp;</th>
<th colspan="1">Alt Activity Name / Course Code</th>
<th>&nbsp;</th>
</tr>
<% for (Iterator it = phases.iterator(); it.hasNext();) { 
    P2lTrackPhase phase = (P2lTrackPhase)it.next();
    System.out.println("Status:" + (phase.getApprovalStatus()?"Yes":"No"));
%>
<tr>
<form class="form_basic"  method="post" name="pie_<%=phase.getTrackPhaseId()%>" action="/TrainingReports/adminHome/editReport?type=phase&track=<%=track.getTrackId()%>">
<td>
    <input type="submit" name="submit" value="Save">
    <input type="submit" name="submit" value="Delete">
</td>
<td>
<input type="hidden" name="<%=P2lTrack.FIELD_DO_PHASE_SUBMIT%>" value="yes">
<input type="hidden" name="<%=P2lTrackPhase.FIELD_TRACK_PHASE_ID%>" value="<%=phase.getTrackPhaseId()%>"/>
<input class="text" type="text" name="<%=P2lTrackPhase.FIELD_PHASE_NUMBER%>" value="<%=phase.getPhaseNumber()%>"/>
</td>
<td>
<select  style="width:50px;" name="<%=P2lTrackPhase.FIELD_ASSIGNED_STATUS%>">
<%=HtmlBuilder.getOptionsFromLabelValue(P2lTrackPhase.yesNoList,phase.getAssigned()?"Yes":"No")%>
</select>
</td>

<td>
<select style="width:50px;"  name="<%=P2lTrackPhase.FIELD_APPROVAL_STATUS%>">
<%=HtmlBuilder.getOptionsFromLabelValue(P2lTrackPhase.yesNoList,phase.getApprovalStatus()?"Yes":"No")%>
</select>
</td>
<td>
<select style="width:50px;"  name="<%=P2lTrackPhase.FIELD_EXEMPT_STATUS%>">
<%=HtmlBuilder.getOptionsFromLabelValue(P2lTrackPhase.yesNoList,phase.getExempt()?"Yes":"No")%>
</select>
</td>
<td>
<input class="text" type="text" size="10" name="<%=P2lTrackPhase.FIELD_SORT_ORDER%>" value="<%=phase.getSortorder()%>"/>
</td>



<td>
<%=Util.toEmpty(phase.getActivityname())%>
<br>
<%=Util.toEmpty(phase.getCoursecode())%>
</td>
<td width="20">
<input type="button" value="Modify" onclick="openP2lWindow('<%=phase.getTrackPhaseId()%>','root'); return false;">
</td>
<td >
<%=Util.toEmpty(phase.getActivitynamealt())%>
<br>
<%=Util.toEmpty(phase.getCoursecodealt())%>
</td>

<td width="10">
    <input type="button" value="Modify" onclick="openP2lWindow('<%=phase.getTrackPhaseId()%>','alt'); return false;">
</td>




</form>
</tr>




<tr>
    <td colspan="10" >&nbsp;</td>
</tr>
<% } %>

<tr>

<form class="form_basic"  method="post" name="addpie" action="/TrainingReports/adminHome/editReport?type=addPie&track=<%=track.getTrackId()%>">
<td>
<input type="hidden" name="<%=P2lTrack.FIELD_DO_PHASE_SUBMIT%>" value="yes">
<input class="text" type="text" name="<%=P2lTrackPhase.FIELD_PHASE_NUMBER%>" value=""/>
</td>
<td>
    <input type="submit" value="Add Pie">  &nbsp; Note: Name the pie "EMPTY" if you want a place holder.
</td>
</form>
</tr>

</table>
<%}%>
    <%=wc.getErrorMsg()%>
</td>
</tr>
</table>

