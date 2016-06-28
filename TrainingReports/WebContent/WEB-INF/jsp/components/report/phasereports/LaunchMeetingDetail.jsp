

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.LaunchMeetingDetails"%>
<%@ page import="com.pfizer.db.P2lActivityStatus"%>
<%@ page import="com.pfizer.db.P2lTrackPhase"%>
<%@ page import="com.pfizer.db.SceFull"%>
<%@ page import="com.pfizer.hander.LaunchMeetingTrainingStatus"%>
<%@ page import="com.pfizer.hander.SceHandler"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.PhaseTrainingDetailLaunchMeetingWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.PhaseTrainingDetailWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.TrainingSummaryWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%
	PhaseTrainingDetailLaunchMeetingWc wc = (PhaseTrainingDetailLaunchMeetingWc)request.getAttribute(PhaseTrainingDetailWc.ATTRIBUTE_NAME);

	String sceUrl = "http://sce.pfizer.com/SCEWeb/evaluation/evaluateTR";
	
	String url = request.getRequestURL().toString();
	if ( url != null ) {
		url = url.toLowerCase();
	}
	if ( url != null 
		&& (url.indexOf("localhost") > 0 || url.indexOf("trt-stg.pfizer.com") > 0  || url.indexOf("tgix-dev.pfizer.com") > 0 )) {
		sceUrl = "http://sce-stg.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}   
    if ( url != null 
		&& (url.indexOf("trt-tst.pfizer.com") > 0 )) {
		
        sceUrl = "http://sce-tst.pfizer.com/SCEWeb/evaluation/evaluateTR";
        //shannon for testing 
        //sceUrl = "http://localhost:7001/SCEWeb/evaluation/evaluateTR";		
	}
    if ( url != null 
		&& (url.indexOf("trt-dev.pfizer.com") > 0)) {
		sceUrl = "http://sce-dev.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
%>

<script type="text/javascript" language="JavaScript">
    function update() {
        window.location.reload(); /*please use this only when there are no 'form submits' on the page, to avoid resubmit*/
    }
    function OpenSpecialCase() { 
        window.name = "specialcase";
        var myWin = window.open("","myWin","height=800,width=1024,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes") 
    }
    
    function submitCommandAttendance(){
    if(document.toviazForm.attendance[0].checked||document.toviazForm.attendance[1].checked){
        return window.confirm('Do you want to continue ?');
    }else{
        alert('Please make a selection first!');
        return false;
    } 
    }
  function submitCommandManagerCertificaion(){    
    if(document.toviazForm.managerCertification.checked){                
        return window.confirm('Do you want to continue ?');
    }else{
        alert('Please make a selection first!');
        return false;
    }
  } 
  
  function enable(){
    //document.forms[0].managerCertification.disabled=false;
    //document.forms[0].commandmanagercertificaion.disabled=false;
  }
  
   function disable(){
    document.toviazForm.managerCertification.disabled=true;
    document.toviazForm.commandmanagercertificaion.disabled=true;
  } 
</script>
<FORM action="" method="post" name="toviazForm">
  <%--Training Information --%>
     <table class="blue_table_without_border" >
    <tr valign="top">
    <td>
     <TABLE class="blue_table" width="400px">
    <TR>
    <TH colspan="2" align="left">Training Information</TH>
    </TR>
    <TR>
    <TD align="center">&nbsp;</TD>
     <TD align="center"><b>Overall Status</b></TD>
   </TR>
   <tr>
        <td><B>Launch Meeting</B></td>
        <td><%=wc.getOverallStatus()%></td>
   </tr>
 
    </TABLE>
     </td>
    <td>&nbsp;</td>
    </tr>
    </table>
    
    <%--Exam Status end--%>
     <%--Exam status --%>
     <table class="blue_table_without_border" >
    <tr valign="top">
    <td>
     <TABLE class="blue_table" width="600px">
    <TR>
    <TH colspan="3" align="left">Training Status</TH>
    </TR>
    <TR>
    <TD align="center"><B>Course Name</B></TD>
     <TD align="center"><b>Course Code</b></TD>
    <TD align="center"><b>Status</b></TD>
   </TR>
   <%
        List examStatus = new ArrayList();
        examStatus = wc.getPedExams();
        Iterator iter = examStatus.iterator();
        while(iter.hasNext()){
        LaunchMeetingTrainingStatus exam = (LaunchMeetingTrainingStatus)iter.next();    
   %>
   <TR> 
   <TD><B><%=Util.toEmptyNBSP(exam.getCourseName())%></B></TD>
   <TD><%=Util.toEmptyNBSP(exam.getCourseCode())%></TD>
   <TD><%=Util.toEmptyNBSP(exam.getStatus())%></TD>
   </TR>
   <%
        }
   %>
    </TABLE>
     </td>
    <td>&nbsp;</td>
    </tr>
    </table>
    <%--Exam Status end--%>
    <%
        if(wc.showAttendance()){
        
    %>
      <%--RENDER ATTENDANCE STATUS --%>    
    <TABLE class="blue_table" width="600px">    
    <TR><TH colspan="3" align="left">Attendance</TH></TR>
    <TR>
    <TD width="60%"><B>My Direct Report Attended Launch Meeting</B></TD>
    <%
        String disableAll = "";
        String attendance = "";
        String managerCertification = "";
        String isRegistered = "";
        if(wc.getAttendanceCodeStatus().equals("Completed") || (wc.getManagerTrainingCodeStatus().equals("Completed") && !wc.getComplinacePresentationCodeStatus().equals(""))){
            disableAll = "Y";
        }
        if(wc.getAttendanceCodeStatus().equals("Completed")){
            attendance = "Y";
        }
         if(wc.getManagerTrainingCodeStatus().equals("Completed")){
            managerCertification = "Y";
        }
         if(wc.getComplinacePresentationCodeStatus().equals("Not Complete") || wc.getComplinacePresentationCodeStatus().equals("Complete")){
            isRegistered = "Y";
            attendance = "N";
        }
         if(wc.getAttendanceCodeStatus().equals("") && wc.getManagerTrainingCodeStatus().equals("") && wc.getComplinacePresentationCodeStatus().equals("")){
            disableAll = "A";
        }
        String disableAttendance = "";
        String attendanceY = "";
        String attendanceN = "";
        String postLaunchY = "";
        String disablePostLaunch = "";
        System.out.println("################### " + attendance + "Manager certification " + managerCertification + "isRegistered" + isRegistered);
        System.out.println("No clicked " + request.getAttribute("noClicked"));
        if(disableAll.equals("Y")){
            disableAttendance = "DISABLED";
            disablePostLaunch = "DISABLED";
        }
        if(disableAll.equals("A")){
            disablePostLaunch = "DISABLED";
        }
        if(attendance.equals("Y")){
            disableAttendance = "DISABLED";
             disablePostLaunch = "DISABLED";
            attendanceY = "checked";
        }
         if(attendance.equals("N")){
            attendanceN = "checked";
            disablePostLaunch = "";
            disableAttendance = "DISABLED";
        }
        if(attendance.equals("N") && managerCertification.equals("Y")){
            disablePostLaunch = "DISABLED";
            attendanceN = "checked";
            postLaunchY = "checked";
        }
        /*if(isRegistered.equals("Y")){
            System.out.println("############ 6");
            disablePostLaunch = "DISABLED";
        }*/
        if(request.getAttribute("noClicked") != null){
            System.out.println("############ 7");
            disablePostLaunch = "";
        }
     //   System.out.println("After all " + attendance + "Post launch " + disablePostLaunch) ;
     %>
   <TD><B>
    <input type="radio" name="attendance" value="Y" <%=attendanceY%> <%=disableAttendance%> onclick="disable()" > Yes &nbsp;&nbsp;&nbsp;
    <input type="radio" name="attendance" value="N" <%=attendanceN%> <%=disableAttendance%> onclick="enable()">No    
    </B></TD>
    <TD>
    
    <input type="submit" value="Submit" name="commandattendance"  <%=disableAttendance%> onclick="return submitCommandAttendance()">
   
   <%--
    <input type="submit" value="Submit" name="commandattendance"  <%=disableAttendance%>>
     --%>
    </TD> 
    </TR>    
    <TR>
    <TD><B>My Direct Report Did Not Attend Launch Meeting and I provided training </B></TD>
    <TD><B>
    <input type="radio" name="managerCertification" value="Y" <%=postLaunchY%> <%=disablePostLaunch%>>Yes &nbsp;&nbsp;&nbsp;  
    </B></TD>
    <TD>
   
    <input type="submit" value="Submit" name="commandmanagercertificaion" <%=disablePostLaunch%> onclick="return submitCommandManagerCertificaion()">
    
     <%--
    <input type="submit" value="Submit" name="commandmanagercertificaion" <%=disablePostLaunch%>>
    --%>
    </TD>
    </TR>    
    <TR>
    <TD><B>Compliance Presentation Completion Status</B></TD>
    <TD colspan="2"> 
    <%
        if(wc.getAttendanceCodeStatus().equals("Completed")){
    %>
        N/A
    <%
        }
        else{
    %>
    <%=wc.getComplinacePresentationCodeStatus()%>   
    <%
        }
    %>
    </TD>    
    </TR>        
    </TABLE> 
    <%
        }
    %>
    </FORM>
                      
                     





 
