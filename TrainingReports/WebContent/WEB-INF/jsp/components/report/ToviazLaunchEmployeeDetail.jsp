<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.EmployeeInfo"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.PLCExamStatus"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.PLCStatus"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.ProductAssignmentInfoRBU"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.TrainingMaterialHistory"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.TrainingSchedule"%>
<%@ page import="com.pfizer.actionForm.RBUGetEmployeeDetailForm"%>
<%@ page import="com.pfizer.actionForm.RBUGetEmployeeDetailFormToviazLaunch"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.db.RBUAllStatus"%>
<%@ page import="com.pfizer.db.RBUGuestTrainersClassData"%>
<%@ page import="com.pfizer.db.RBUPedagogueExam"%>
<%@ page import="com.pfizer.db.ToviazLaunchExamStatus"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.RBU.RBUEmployeeDetailsWc"%>
<%@ page import="com.pfizer.webapp.wc.RBU.ToviazLaunchEmployeeDetailsWc"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="com.tgix.Utils.Util"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%

    //try to get form bean, beacause it is not visible.
    String mode0 = request.getParameter("m0")==null?"":request.getParameter("m0");
    String mode1 = request.getParameter("m1")==null?"":request.getParameter("m1");
    String mode2 = request.getParameter("m2")==null?"":request.getParameter("m2");
    ToviazLaunchEmployeeDetailsWc wc = (ToviazLaunchEmployeeDetailsWc)request.getAttribute(RBUEmployeeDetailsWc.ATTRIBUTE_NAME);
    request.setAttribute("getEmployeeDetailForm",wc.getFormBean());
    RBUGetEmployeeDetailFormToviazLaunch getEmployeeDetailForm = wc.getFormBean();
    EmployeeInfo info = getEmployeeDetailForm.getEmployeeInfo();
    if(info == null){
        info = new EmployeeInfo();
    }
    
    // Prepare Render Title
    String titleGeneral = "";
    //String titleSpecific = ""; 
    String eventId = "";

    titleGeneral = "PSCPT";
    eventId = AppConst.EVENTID_RBU;
    
    titleGeneral = titleGeneral+" Detail";
    UserSession uSession = UserSession.getUserSession(request); 
    //todo - check on this - Shannon
    String sceUrl = "http://sce.pfizer.com/SCEWeb/evaluation/evaluateTR";
	
	String url = request.getRequestURL().toString();
	if ( url != null ) {
		url = url.toLowerCase();
	}
	if ( url != null 
		&& (url.indexOf("localhost") > 0 || url.indexOf("trt-stg.pfizer.com") > 0 )) {
		sceUrl = "http://sce-stg.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}    
    if ( url != null 
		&& (url.indexOf("trt-tst.pfizer.com") > 0 )) {
		sceUrl = "http://sce-tst.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
    if ( url != null 
		&& (url.indexOf("trt-dev.pfizer.com") > 0)) {
		sceUrl = "http://sce-dev.pfizer.com/SCEWeb/evaluation/evaluateTR";	
	}
    
%>
<script language="javascript">
    self.name = 'mainWin';
	function openPopup(){
    
		window.open("","_popup","toolbars=0,location=0,width=500,height=500");
		return true;
	}
    function pw() {return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth}; 
    function mouseX(evt) {return evt.clientX ? evt.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft) : evt.pageX;} 
    function mouseY(evt) {return evt.clientY ? evt.clientY + (document.documentElement.scrollTop || document.body.scrollTop) : evt.pageY} 
    function popUp(evt,oi, content) 
    {
    if (document.getElementById) {
    var wp = pw(); dm = document.getElementById(oi); 
    dm.innerHTML = content;
    ds = dm.style; st = ds.visibility; 
    if (dm.offsetWidth) ew = dm.offsetWidth; 
    else if (dm.clip.width) ew = dm.clip.width; 
    if (st == "visible" || st == "show") { 
    ds.visibility = "hidden"; 
    } else {tv = mouseY(evt) + 20; lv = mouseX(evt) - (ew/4); 
    if (lv < 2) lv = 2; else if (lv + ew > wp) lv -= ew/2; 
    lv += 'px';tv += 'px';  
    ds.left = lv; ds.top = tv; ds.visibility = "visible";}}}
    
    
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
    //document.toviazForm.managerCertification.disabled=false;
    //document.toviazForm.commandmanagercertificaion.disabled=false;
  }
  
   function disable(){
    document.toviazForm.managerCertification.disabled=true;
    document.toviazForm.commandmanagercertificaion.disabled=true;
  }
   
    
</script>
<style type="text/css">
                    .tip {font:10px/12px
                    Arial,Helvetica,sans-serif; border:solid 1px
                    #666666; width:150px;height:50px; padding:1px;
                    position:absolute; z-index:100;
                    visibility:hidden; color:#333333; top:20px;
                    left:90px; background-color:#ffffcc;
                    layer-background-color:#ffffcc;}
</style>

<div id="t1" class="tip">This is a Javascript Tooltip</div>
<body>
<FORM action="" method="post" name="toviazForm">
<input type="hidden" name="emplid" value="<%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>">
<TABLE class="basic_table"><TR><TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD><TD>
    <P id="table_title" style="font-size:1.2em; font-weight:bold;">
    <%if(info != null && info.getLastName() !=null){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getLastName()%>,
    <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName()%>
    
    <%
    }
        String emplStatus = null;
        
        if(info !=null) emplStatus = info.getStatus();
        if(emplStatus != null && emplStatus.equalsIgnoreCase("On-Leave") ){ 
            out.println("<font color='red'><b>"+getEmployeeDetailForm.getEmployeeInfo().getStatus()+"</b></font>");
        }
    
    %>

    </P>
    
    <TABLE class="blue_table" width="800px">
    <TR>
    <TH colspan="6" align="left">Employee Information</TH>
    </TR>
    <TR>
    <TD ROWSPAN="8" VALIGN="left">    
    <%if(info.getImageURL() !=null){%>
        <!-- <img src="{request.getEmployeeDetailForm.employeeInfo.imageURL}"/> -->    
        <img src="<%=getEmployeeDetailForm.getEmployeeInfo().getImageURL()%>"/>
    <%}%>
    </TD>
    <TD>Employee Status</TD>
    <TD>
        <%
        if(emplStatus != null){
            if(emplStatus.equalsIgnoreCase("On-Leave")||emplStatus.equalsIgnoreCase("Terminated") ){ 
                out.println("<font color='red'><b>"+getEmployeeDetailForm.getEmployeeInfo().getStatus()+"</b></font>");
            }else{
                out.println(emplStatus);
            }
        }
    %>
    </TD>
    <TD ROWSPAN="8" VALIGN="left" bgcolor="#DDDDDD"></TD>
    <TD>Role</TD>
   <TD><%=Util.toEmptyNBSP(info.getFutureRole())%></TD>
    </TR>
    <TR>
    <TD>Employee ID</TD>
    <TD>
    <%if(info != null && info.getEmplID() !=null){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getEmplID()%>
    <%}%>
    </TD>
    <TD>Sales Position Id</TD>
   <TD><%=Util.toEmptyNBSP(info.getSalesPositionId())%></TD>
    </TR>
    <TR>
    <TD>Full Name</TD>
    <TD>    
    <%if(info != null && info.getLastName() !=null ){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getLastName()%>,
    <%if( info.getPreferredName() !=null ){%>
    <%=getEmployeeDetailForm.getEmployeeInfo().getPreferredName()%>
    <%}%>
    <%}%>
    </TD>
    <TD>Sales Position Description</TD>
    <TD><%=Util.toEmptyNBSP(info.getSalesPositionDesc())%></TD>
    </TR>
    <tr>
    <TD>Gender</TD>
    <TD>
    <%if(info != null && info.getGender() !=null ){%>
     <%=getEmployeeDetailForm.getEmployeeInfo().getGender()%>
     <%}%>
    </TD>
    <TD>BU</TD>
     <TD><%=Util.toEmptyNBSP(info.getFutureBU())%></TD>
    </TR>  
    <TR>  
    <TD>Hire Date</TD>
    <TD>
    <%if(info != null && info.getHireDate() !=null ){%>
    <%=Util.formatDateShort(info.getHireDate())%>
    <%}%>
    </TD>
    <TD>RBU</TD>
    <TD><%=Util.toEmptyNBSP(info.getFutureRBU())%></TD>
    </TR>
    <TR>  
    <TD>Email</TD>
    <TD>
    <%if(info != null && info.getEmail() !=null ){%>
     <a href="mailto:<%=info.getEmail()%>?subject=<%=titleGeneral%>">
      <%=getEmployeeDetailForm.getEmployeeInfo().getEmail()%>      
      </a>  
     <%}%>
    </TD>
    <TD>Reports to</TD>
    <TD>
    <%if(info != null){
    if(info.getFutureReportToEmail() != null && info.getFutureReportToEmail().length() > 0){
        %>
     <a href="mailto:<%=info.getFutureReportToEmail()%>?subject=<%=titleGeneral%>">
     <%=Util.toEmptyNBSP(info.getFutureReportToLastName())%>, <%=Util.toEmptyNBSP(info.getFutureReportToFirstName())%>
     </a>
    <%}
    }%>
    </TD>
    </TR>
        <TD>Promotion Date</TD>
	    <TD>
	    <%if(info != null && info.getPromotionDate() !=null ){%>
	    <%=Util.formatDateShort(info.getPromotionDate())%>
	    <%}%>
    </TD>
    <TR>
    
    </TR>
    </TABLE>
    <BR><BR>
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
        <td> Toviaz Launch Meeting</td>
        <td><%=getEmployeeDetailForm.getOverallStatus()%></td>
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
    <TH colspan="3" align="left">Toviaz Launch Training Status</TH>
    </TR>
    <TR>
    <TD align="center"><B>Course Name</B></TD>
     <TD align="center"><b>Course Code</b></TD>
    <TD align="center"><b>Status</b></TD>
   </TR>
   <%
        List examStatus = new ArrayList();
        examStatus = getEmployeeDetailForm.getExamStatus();
        Iterator iter = examStatus.iterator();
        while(iter.hasNext()){
        ToviazLaunchExamStatus exam = (ToviazLaunchExamStatus)iter.next();    
   %>
   <TR> 
   <TD><%=Util.toEmptyNBSP(exam.getExamName())%></TD>
   <TD><%=Util.toEmptyNBSP(exam.getExamCode())%></TD>
   <TD><%=Util.toEmptyNBSP(exam.getExamStatus())%></TD>
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
    
   
     <%--RENDER ATTENDANCE STATUS --%>    
    <TABLE class="blue_table" width="600px">    
    <TR><TH colspan="3" align="left">Attendance</TH></TR>
    <TR>
    <TD width="60%"><B>My Direct Report Attended Toviaz Launch Meeting</B></TD>
    <%
        String disableAll = "";
        String attendance = "";
        String managerCertification = "";
        String isRegistered = "";
        if(getEmployeeDetailForm.getDisableAll() != null){
            disableAll = getEmployeeDetailForm.getDisableAll();
        }
        if(getEmployeeDetailForm.getAttendance() != null){
            attendance = getEmployeeDetailForm.getAttendance();
        }
         if(getEmployeeDetailForm.getManagerCertification() != null){
            managerCertification = getEmployeeDetailForm.getManagerCertification();
        }
         if(getEmployeeDetailForm.getRegistered() != null){
            isRegistered = getEmployeeDetailForm.getRegistered();
        }
        String disableAttendance = "";
        String attendanceY = "";
        String attendanceN = "";
        String postLaunchY = "";
        String disablePostLaunch = "";
        //System.out.println("################### " + attendance + "Manager certification " + managerCertification + "isRegistered" + isRegistered);
        if(disableAll.equals("Y")){
            disableAttendance = "DISABLED";
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
        if(isRegistered.equals("Y")){
            disablePostLaunch = "DISABLED";
        }
        if(request.getAttribute("noClicked") != null){
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
    <TD><B>My Direct Report Did Not Attend Toviaz Launch Meeting and I provided training </B></TD>
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
    <TD colspan="2"> <%=getEmployeeDetailForm.getComplianceStatus()%>   
    </TD>    
    </TR>        
    </TABLE> 
    </TABLE>    
    <BR><BR>
    </TD></TR></TABLE> 
    </FORM>
    </body>
  