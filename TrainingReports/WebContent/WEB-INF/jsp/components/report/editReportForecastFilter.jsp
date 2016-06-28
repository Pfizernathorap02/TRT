<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%@ page import="com.pfizer.db.ForecastReport"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.EditReportForecastFilterWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="com.tgix.html.LabelValueBean"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>


<style type="text/css">@import url(/TrainingReports/resources/js/jscalendar-1.0/calendar-blue.css);</style>
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/calendar.js"></script>        
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/lang/calendar-en.js"></script>
<script type="text/javascript" src="/TrainingReports/resources/js/jscalendar-1.0/calendar-setup.js"></script>        
<script type="text/javascript" src="/TrainingReports/resources/js/jquery.js"></script>       
<script type="text/javascript">
    function goToAddCourses(stat){
    if(stat=='c')
        document.form1.action='/TrainingReports/adminHome/addSelectedStatusCourse?status=Completed&trackID=<%=session.getAttribute("trackID")%>&trackName=<%=session.getAttribute("trackName")%>';
    else if(stat=='nc')
        document.form1.action='/TrainingReports/adminHome/addSelectedStatusCourse?status=NotCompleted';
     else if(stat=='r')
        document.form1.action='/TrainingReports/adminHome/addSelectedStatusCourse?status=Registered'; 
    else if(stat=='nr')
        document.form1.action='/TrainingReports/adminHome/addSelectedStatusCourse?status=NotRegistered'; 
        
    }
    
    function validation(){
        var start=document.form1.startDate.value;
        var end=document.form1.endDate.value;
        var duration=document.form1.duration.value;
        //alert('in validation');
        if((start=="" && end!="") || (start!="" && end=="")){
            document.getElementById("msg").innerHTML='Please enter both Start Date and End Date';
            document.getElementById("msgSelect").focus();
            return false;
        }
        if(duration!="" && (start=="" || end=="")){
            document.getElementById("msg").innerHTML='Please enter both Start Date and End Date for duration';
            document.getElementById("msgSelect").focus();
            return false;
        }
        if(start!="" && end!=""){
            var sDate=new Date(start);
            var eDate=new Date(end);
            sMonth=sDate.getMonth();
            sYear=sDate.getFullYear();
            eMonth=eDate.getMonth();
            eYear=eDate.getFullYear();
            if(sDate>eDate){
                document.getElementById("msg").innerHTML='Start Date can not be greater than End date';
                document.getElementById("msgSelect").focus();
                return false;
            }
        }
        if(duration == ""){
             document.getElementById("msg").innerHTML='Please enter duration';
             document.getElementById("msgSelect").focus();
            return false;
        }
        if(duration!="" && start!="" && end!=""){
            validString='0123456789';
            for(var i=0;i<duration.length;i++){
                if(!(duration.charAt(i)=='0' || duration.charAt(i)=='1'|| duration.charAt(i)=='2' ||duration.charAt(i)=='3'||duration.charAt(i)=='4' ||duration.charAt(i)=='5'||duration.charAt(i)=='6' ||duration.charAt(i)=='7'||duration.charAt(i)=='8' || duration.charAt(i)=='9')){
                    document.getElementById("msg").innerHTML='Invalid Duration';
                    document.getElementById("msgSelect").focus();
                    return false; 
                }
                
                
            }
        
            var calculatedMonth=((eYear-sYear)*12)+(eMonth-sMonth);
            if(duration>calculatedMonth){
                document.getElementById("msg").innerHTML='Entered Duration should be between Start Date and End Date.';
                document.getElementById("msgSelect").focus();
                return false; 
            }
        }
     
        
    return true;
    
    }
    
   
    
    function displayvals() {
  //  alert('inside displayvals');
        var a = [];
        var b = [];
        var i;
        var selectedRoleValue = null;
        var selectedRoleIndex = null;
        
                $('#role_cd :selected').each(function(i, selected) {
                    
                    b[i] = $(selected).text();
                    a[i] = $(selected).val();
                    //alert(a[i]);
                    if (selectedRoleValue == null) {
                        selectedRoleValue = b[i];
                        selectedRoleIndex = a[i];
                    } else {
                        selectedRoleValue = selectedRoleValue + "," + b[i];
                        selectedRoleIndex = selectedRoleIndex + "," + a[i];
                    }
        
            } );
    if(selectedRoleIndex==null)selectedRoleIndex='';
    document.getElementById('role_hidden').value = selectedRoleIndex;
   // alert(selectedRoleIndex);
    
    }
    
    function clearDates(sid){
    var hStartDate=document.getElementById(sid);
    document.getElementById(sid).value="";
   }
</script>



<%            
	EditReportForecastFilterWc wc = (EditReportForecastFilterWc)request.getAttribute(EditReportWc.ATTRIBUTE_NAME);
    ForecastReport track = wc.getTrack();

    List roleCodes = track.getRoleCode();
    String status=(String)request.getParameter("status");
%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
    <link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>

</head>
<body>


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
                <a href="/TrainingReports/adminHome/editMenu?name=Reports&id=<%=wc.getMenu().getId()%>"> <%=wc.getMenu().getLabel()%> </a> /
                <%=session.getAttribute("trackName")%>
        	</div>
        </td>
	</tr>
    </table>
    <table><tr>
            <td ><Label class="basic_label">Report Name : </Label></td>
            <td > <Label ><%=session.getAttribute("trackName")%> </Label></td>
            <%//}%>
        </tr></table>
    <table class="blue_table_without_border" align="center">
    <tr>
        <td><a id="msgSelect" href=""></a><span id="msg" style="color:red;"></span></td>
    </tr>
   
    </table>
    <form name="form1" id="form1" action="/TrainingReports/adminHome/editForecastFilterCriteria?trackID=<%=track.getTrackId()%>&trackName=<%=track.getTrackLabel()%>" method="post">
        <input type=hidden name="<%=ForecastReport.FIELD_TRACK_ID%>" value="<%=ForecastReport.FIELD_TRACK_ID%>">
        <table class="blue_table" width="100%">
        <tr>
            <td><%=wc.getErrorMsg()%></td>
        </tr>
        
        </table>
        <table class="blue_table" width="100%">
        
        <tr><TH align="left" colspan="2">Forecast Filter Criteria</TH>
        </tr>
        <tr>
            <td>Roles</td>
            <td><select name="role_cd" id="role_cd" size="5" multiple>
            <% Map roleCodeMap=new HashMap();
                String role=null;
                LabelValueBean labelValueBean;
                List list=new ArrayList();
                List selectedList=new ArrayList();
                if(session.getAttribute("ROLE_CD")!=null){
                role=session.getAttribute("ROLE_CD").toString();
                }
                
                if(role!=null){
                    String arr[]=role.split(",");
                    for(int i=0;i<arr.length;i++){
                        selectedList.add(arr[i]);
                    }
                }
                for(Iterator it=roleCodes.iterator();it.hasNext();){ 
                roleCodeMap=(HashMap)it.next();
                String strL = (String)roleCodeMap.get("ROLE_DESC");
                String strV = (String)roleCodeMap.get("ROLE_CD");
                labelValueBean = new LabelValueBean(strL,strV);
                list.add(labelValueBean);
            } %>
            
            <%=HtmlBuilder.getMultipleOptionalFromLabelValue(list,selectedList)%>
            </select>
            <input type="hidden" id="role_hidden" name="role_hidden">
            </td>
        </tr>
        <tr>
            <td>Hire or Promotion Date</td>
            <td>Hire Date :
            <%System.out.println("hiredate===="+session.getAttribute("HIRE_OR_PROMOTION_DATE"));%>
            <input type="radio" name="hpdate" value="N" 
            <%if(session.getAttribute("HIRE_OR_PROMOTION_DATE")!=null){if(session.getAttribute("HIRE_OR_PROMOTION_DATE").equals("N")){%>checked<%}}%> >
           &nbsp;&nbsp;&nbsp;&nbsp;
           Promotion Date :
            <input type="radio" name="hpdate" value="Y" 
            <%if(session.getAttribute("HIRE_OR_PROMOTION_DATE")!=null){
                if(session.getAttribute("HIRE_OR_PROMOTION_DATE").equals("Y")){%>checked<%}}%> >
            </td>
        </tr>
        
        <tr>
            <td>Start Date <font color="red">*</font></td>
            <td>
            <input type="text" name="startDate" id="startDate" value="<%=session.getAttribute("START_DATE")==null?"":session.getAttribute("START_DATE")%>"  readonly>
            
            <input type=image src="/TrainingReports/resources/images/calendar.jpg" id="startDate_Id"  border="0" height="17" width="20" alt="Select Start Date" value="Change Date" name="changeDate" >
                <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "startDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "startDate_Id"                         
                                });
                               
                            </script>

<img src="/TrainingReports/resources/images/cancel.gif" alt="Clear Start Date" onmouseover="this.style.cursor='hand'" height="17" width="20" onclick="clearDates('startDate');">
            </td>
        </tr>
        <tr>
            <td>End Date <font color="red">*</font></td>
            <td>
            <input type="text" name="endDate" id="endDate" value="<%=session.getAttribute("END_DATE")==null?"":session.getAttribute("END_DATE")%>"  readonly>
    
            <input type=image src="/TrainingReports/resources/images/calendar.jpg" id="endDate_Id"  border="0" height="17" width="20" alt="Select End Date" value="Change Date" name="changeDate" >
                <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "endDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "endDate_Id"                         
                                });
                               
                            </script>
<img src="/TrainingReports/resources/images/cancel.gif" alt="Clear End Date" onmouseover="this.style.cursor='hand'" height="17" width="20" onclick="clearDates('endDate');">
            </td>
        </tr>
        <tr>
            <td>Duration <font color="red">*</font></td>
            <td>
           
            <input type="text" name="duration" onclick="this.select();" id="duration" maxlength="8" value="<%=session.getAttribute("DURATION")==null?"":session.getAttribute("DURATION")%>">&nbsp;(Minimum duration in months since Hire/Promotion as on the End Date selected above.)
         
            
            </td>
        </tr>
    
        
    </table>
    <br>
    <br>
    <table class="blue_table">
    
    </table>
    <br>
    <table class="blue_table" width="100%">
    <tr><TH align="left" colspan="3">Training Details Selection:</TH></tr>
    <tr>
        <td>Completed</td>
        <td>
         <div style="width:500px;height:100px;overflow:auto;">
   <%  int count = track.getNumOptionalValFromMap(session,"Completed");
            if (count==0){
                 %>
        <select size="5" name="compList" disabled style="width:500px">
        <%
            } else {
            if (count < 5) count=5;
            %>
        <select size="<%=count%>" disabled name="compList" >
        <%}%>
        <%=track.getOptionalValFromMap(session,"Completed")%>
        </select>
        </div>
        </td>
        <td><input type="submit" name="completed" value="Add Completed Courses" onclick="javascript:displayvals();goToAddCourses('c');"></td>
        </tr>
        
        <tr>
        <td>Not Completed</td>
        <td>
        <div style="width:500px;height:100px;overflow:auto;">
      <%  count = track.getNumOptionalValFromMap(session,"NotCompleted");
            if (count==0){
                 %>
        <select size="5" name="nonCompList" disabled style="width:500px">
        <%
            } else {
            if (count < 5) count=5;
            %>
        <select size="<%=count%>" name="nonCompList" multiple disabled >
        <%}%>
        <%=track.getOptionalValFromMap(session,"NotCompleted")%>
        </select>
        </div>
        </td>
        <td><input type="submit" name="notCompleted" value="Add Courses Not Completed" onclick="javascript:displayvals();goToAddCourses('nc');"></td>
        </tr>
        
        <tr>
        <td>Registered</td>
        <td>
        <div style="width:500px;height:100px;overflow:auto;">
       <%  count = track.getNumOptionalValFromMap(session,"Registered");
            if (count==0){
                 %>
        <select size="5" name="regList" disabled style="width:500px">
        <%
            } else {
            if (count < 5) count=5;
            %>
        <select size="<%=count%>" name="regList" disabled>
        <%}%>
        <%=track.getOptionalValFromMap(session,"Registered")%>
        </select>
        </div>
        </td>
        <td><input type="submit" name="registered" value="Add Registered Courses" onclick="javascript:displayvals();goToAddCourses('r');"></td>
        </tr>
        
        <tr>
        <td>Not Registered</td>
        <td>
        <div style="width:500px;height:100px;overflow:auto;">
        <%  count = track.getNumOptionalValFromMap(session,"NotRegistered");
            if (count==0){
                 %>
        <select size="5" name="nonRegList" disabled style="width:500px">
        <%
            } else {
            if (count < 5) count=5;
            %>
                <select size="<%=count%>" name="nonRegList" disabled >
        <%}%>
        <%=track.getOptionalValFromMap(session,"NotRegistered")%>
        </select>
        </div>
        </td>
        <td><input type="submit" name="notRegistered" value="Add Courses Not Registered" onclick="goToAddCourses('nr');"></td>
        </tr>
    </table>
     <table align="center">
        <tr>
            <td>
            <input type="submit" value="Save" name="Save" onclick="javascript:displayvals();return validation();">
            </td>
            <td>
            <input type="button" value="Cancel" onclick="window.location='/TrainingReports/adminHome/editMenu?name=Reports&id=<%=wc.getMenu().getId()%>'">
            </td>
        </tr>
        </table>
    </form>
    </td>
</tr>
</table>

</body>
</html>