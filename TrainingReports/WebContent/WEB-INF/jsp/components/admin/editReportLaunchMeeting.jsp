<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.LaunchMeeting"%>
<%@ page import="com.pfizer.db.LaunchMeetingDetails"%>
<%@ page import="com.pfizer.db.LaunchMeeting"%>
<%@ page import="com.pfizer.db.LaunchMeetingDetails"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.EditMenuWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportLaunchMeetingWc"%>
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
	EditReportLaunchMeetingWc wc = (EditReportLaunchMeetingWc)request.getAttribute(EditReportWc.ATTRIBUTE_NAME);
    LaunchMeeting track = wc.getTrack();
    List phases = track.getCompletePhaseList();
%>

<script type="text/javascript" language="javascript"> 
<!-- 
function openP2lWindow(id, type) { 

window.name = "main";
var myW = window.open("","myW","height=500,width=600,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes");
myW.location =  "/TrainingReports/adminHome/editNodeLaunchMeeting?type=" + type + "&id=" + id;
} 
--> 

</script>

<script type="text/javascript" language="javascript"> 
  function validate(attendance,rootActivity,courseCodeAlt, courseCodeAlt1,overall){
  //alert(courseCodeAlt + '>>> ' + courseCodeAlt1 + 'rootActivity ' + rootActivity);    
    if(attendance == 'true' && overall=='true'){
        alert('Please select either Attendance or Overall');
        return false;        
    }
    if(attendance == 'true'){
    // Check if Activity 1 is selected. then Alt1 and Alt should not be present
        if(rootActivity == 'null' || courseCodeAlt == 'null' || courseCodeAlt1 == 'null'){
                alert('Please select all the three activity codes for Attendance type report.');
                return false;
        }
    }
  }      
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
    <input type="hidden" name="<%=LaunchMeeting.FIELD_DO_SUBMIT%>" value="yes">
    Report Name&nbsp;&nbsp;<input size="50" type="text" class="text" name="<%=LaunchMeeting.FIELD_TRACK_LABEL%>" value="<%=track.getTrackLabel()%>">
<%--
&nbsp;&nbsp;Add to Course Completion:&nbsp;    
<select style="width:50px;"  name="<%=LaunchMeeting.FIELD_DO_COMPLETE%>">
<%=HtmlBuilder.getOptionsFromLabelValue(LaunchMeeting.yesNolist,track.getDoComplete()?"Y":"N")%>
</select>
    
    <input type="submit" value="Save">
   --%> 
</form>

<table class="blue_table" width="100%">
<tr>
<th>&nbsp;</th>

<th>Name</th>
<th>Attendance</th>
<th>Overall</th>
<%--
<th>Assigned</th>
<th>Approval</th>
<th>Exempt</th>
--%>
<th>Sort</th>
<th colspan="1">Activity Name / Course Code</th>
<th>&nbsp;</th>
<th colspan="1">Alt Activity Name / Course Code</th>
<th>&nbsp;</th>
</tr>
<% for (Iterator it = phases.iterator(); it.hasNext();) { 
    LaunchMeetingDetails phase = (LaunchMeetingDetails)it.next();
  //  System.out.println("Status:" + (phase.getApprovalStatus()?"Yes":"No"));
%>
<tr>
<form class="form_basic"  method="post" name="pie_<%=phase.getTrackPhaseId()%>" action="/TrainingReports/adminHome/editReportLaunchMeeting?type=phase&track=<%=track.getTrackId()%>">
<td>
    <input type="submit"  id="process" name="process" value="Save" onclick="return validate('<%=phase.getAttendance()%>', '<%=phase.getRootActivityId()%>','<%=phase.getCoursecodealt()%>','<%=phase.getCoursecodealt1()%>','<%=phase.getOverall()%>')">
    <input type="submit" id="process" name="process" value="Delete">
</td>
<td>
<input type="hidden" name="<%=LaunchMeeting.FIELD_DO_PHASE_SUBMIT%>" value="yes">
<input type="hidden" name="<%=LaunchMeetingDetails.FIELD_TRACK_PHASE_ID%>" value="<%=phase.getTrackPhaseId()%>"/>
<input class="text" type="text" name="<%=LaunchMeetingDetails.FIELD_PHASE_NUMBER%>" value="<%=phase.getPhaseNumber()%>"/>
</td>
<td>
<select  style="width:50px;" name="<%=LaunchMeetingDetails.FIELD_ATTENDANCE_STATUS%>" onchange="document.pie_<%=phase.getTrackPhaseId()%>.process.value='Save';document.pie_<%=phase.getTrackPhaseId()%>.submit();">
<%=HtmlBuilder.getOptionsFromLabelValue(LaunchMeetingDetails.yesNoList,phase.getAttendance()?"Yes":"No")%>
</select>
</td>

<td>
<select  style="width:50px;" name="<%=LaunchMeetingDetails.FIELD_OVERALL_STATUS%>">
<%=HtmlBuilder.getOptionsFromLabelValue(LaunchMeetingDetails.yesNoList,phase.getOverall()?"Yes":"No")%>
</select>
</td>
<%--
<td>
<select  style="width:50px;" name="<%=LaunchMeetingDetails.FIELD_ASSIGNED_STATUS%>">
<%=HtmlBuilder.getOptionsFromLabelValue(LaunchMeetingDetails.yesNoList,phase.getAssigned()?"Yes":"No")%>
</select>
</td>

<td>
<select style="width:50px;"  name="<%=LaunchMeetingDetails.FIELD_APPROVAL_STATUS%>">
<%=HtmlBuilder.getOptionsFromLabelValue(LaunchMeetingDetails.yesNoList,phase.getApprovalStatus()?"Yes":"No")%>
</select>
</td>
<td>
<select style="width:50px;"  name="<%=LaunchMeetingDetails.FIELD_EXEMPT_STATUS%>">
<%=HtmlBuilder.getOptionsFromLabelValue(LaunchMeetingDetails.yesNoList,phase.getExempt()?"Yes":"No")%>
</select>
</td>
--%>
<td>
<input class="text" type="text" size="10" name="<%=LaunchMeetingDetails.FIELD_SORT_ORDER%>" value="<%=phase.getSortorder()%>"/>
</td>



<td>
<%=Util.toEmpty(phase.getActivityname())%>
<br>
<%=Util.toEmpty(phase.getCoursecode())%>
</td>
<td width="20">
<%
    if(!phase.getOverall()){
%>
<input type="button" value="Modify" onclick="openP2lWindow('<%=phase.getTrackPhaseId()%>','root'); return false;">
<%
    }
%>
<%
     if(phase.getAttendance()){
%>
    <b>Attendance Code</b>
<%
     }
%>
</td>
<td >
<%=Util.toEmpty(phase.getActivitynamealt())%>
<br>
<%=Util.toEmpty(phase.getCoursecodealt())%>
<%
    if(phase.getAttendance()){
%>
<br><br>
    <%=Util.toEmpty(phase.getActivitynamealt1())%>
     <br>
    <%=Util.toEmpty(phase.getCoursecodealt1())%>
<%
    }
%>
</td>

<td width="10">
<%
    if(!phase.getOverall()){
%>
    <input type="button" value="Modify" onclick="openP2lWindow('<%=phase.getTrackPhaseId()%>','alt'); return false;">
    <%
    }
    %>
<%
    if(phase.getAttendance()){
%>
<b>Manager Training Code</b>
<%
    }
%>
<%
    if(phase.getAttendance()){
%>
<br><br>
<input type="button" value="Modify" onclick="openP2lWindow('<%=phase.getTrackPhaseId()%>','alt1'); return false;">
    <b>Compliance Presentation Code</b>
 <%
    }
 %>   
</td>
</form>
</tr>


<% } %>

<tr>

<form class="form_basic"  method="post" name="addpie" action="/TrainingReports/adminHome/editReportLaunchMeeting?type=addPie&track=<%=track.getTrackId()%>">
<td>
<input type="hidden" name="<%=LaunchMeeting.FIELD_DO_PHASE_SUBMIT%>" value="yes">
<input class="text" type="text" name="<%=LaunchMeetingDetails.FIELD_PHASE_NUMBER%>" value=""/>
</td>
<td colspan="5">
    <input type="submit" value="Add Pie">  &nbsp; Note: Name the pie "EMPTY" if you want a place holder.
</td>
</form>
</tr>

</table>

    <%=wc.getErrorMsg()%>
</td>
</tr>
</table>

