<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.TrainingDetailPageWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%-- <%@ taglib uri="/WEB-INF/netui-tags-html.tld" prefix="netui"%> --%>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%
	TrainingDetailPageWc wc = (TrainingDetailPageWc)request.getAttribute(TrainingDetailPageWc.ATTRIBUTE_NAME);
    System.out.println("Testing");
%>

<script type="text/javascript" language="javascript">
var myW;
var myWin;
function DoThis12() { 
if (myW != null)
  {
    
    if (!myW.closed) {
      myW.focus();
    } 
    
  }

window.name = "main";
myW = window.open("","myW","height=400,width=500,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes"); 
myW.location='http://p2l.pfizer.com/SumTotal/app/management/LMS_LearnerReports.aspx?UserMode=0&Mode=1';
}  


function DoThis13() { 
if (myWin != null)
  {
    
    if (!myWin.closed) {
      myWin.focus();
    } 
    
  }

window.name = "main";
myWin = window.open("","myW","height=400,width=500,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes"); 
myWin.location='http://p2l.pfizer.com/SumTotal/app/management/LMS_LearnerHome.aspx?UserMode=1&PersistMode=1';
}  

</script> 
<html>   
<body>
<table class="basic_table"  width="100%">
<tr>
    <td colspan=4 width="2%">
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
    </td>
    
</tr>
<tr>
<td align="right" colspan=4>
        <%String empID = UserSession.getUserSession(request).getUser().getEmplid();
        boolean isAdmin = UserSession.getUserSession(request).getUser().isAdmin();
        if (!isAdmin){%>
        <a href="/TrainingReports/newHomePage?emplid=<%=empID%>">My Direct Reports</a>&nbsp;&nbsp;&nbsp;
        <%}%>
        <a href="/TrainingReports/reportselect">Training Reports</a>&nbsp;&nbsp;&nbsp;
        <a href="/TrainingReports/allEmployeeSearch">Employee Search</a>&nbsp;&nbsp;&nbsp;
        <% String res = UserSession.getUserSession(request).getIsDelegatedUser();
        if (res != null) {%>
         <a href="/TrainingReports/switchUser">Switch User</a>
        <%}%>&nbsp;&nbsp;&nbsp;</td>
</tr>
<tr>
    <td width="2%">&nbsp;</td>
    <td colspan="3">
        <div class="breadcrumb">
            <%if(session.getAttribute("breadCrumbs") != null){
               String brCrumbs = (String)session.getAttribute("breadCrumbs");%>
                 <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                <a> <%= brCrumbs %> </a> /
                Employee Details 
                <br>
            <%} else {%>
                <%if (!isAdmin){%>
                <a href="/TrainingReports/p2l/employeeSearchDetailPage?emplid=<%=empID%>" >My Profile</a> / 
                <%} else {%>
                <a href="/TrainingReports/reportselect" >Home</a>
                
                <%}%>
                <%if (wc.getActivityId() == null || !wc.getSearchEmpl().equals(empID)) {%>
                <%=wc.getSearchedEmplName()%>'s Training Details
                <%} else {%>
                Training Details for <%=wc.getActivityName()%>
                <%}%>
            <%}%>
        </div>
    </td>
</tr>
<tr>
    <td width="2%">&nbsp;</td>
    <td width="45%" align="left" valign="top">
        <table class="blue_table_without_border">
        <tr><td> <inc:include-wc component="<%=wc.getEmployeeInfo()%>"/></td></tr>
        <tr> <td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         <a  onmouseover="this.style.cursor='hand';" onclick="DoThis12();">Click Here</a>&nbsp;to go your Training Transcripts from P2L 
          
        </td>
        </tr>
        <tr>
         <td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a onmouseover="this.style.cursor='hand';" onclick="DoThis13();">Click Here</a>&nbsp;to go to your Direct Reports Training Transcripts from P2L 
        </td>
        </tr>
        </table>
        
    </td>
   
    <td width="45%" align="right" valign="top">
        
            <%-- <table class="blue_table_without_border" >
            <!--<tr><th>&nbsp;</th></tr>
            <tr><th>&nbsp;</th></tr>-->
            <tr><td width="40%" valign="top"><inc:include-wc component="<%=wc.getTrainingPath()%>"/></td></tr>
            </table> --%>
            
            <table class="blue_table_without_border" >
            <tr><td width="40%" valign="top"><inc:include-wc component="<%=wc.getGapReport()%>"/></td></tr>
            </table>
        </td>
       <td width="2%">&nbsp;</td>     
        
        
       
    
</tr>
<tr>
    <td colspan="4" >
        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
    </td>

</tr>

<%-- <tr>
    
    <td colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         <a  onmouseover="this.style.cursor='hand';" onclick="DoThis12();">Click Here</a>&nbsp;to go your Training Transcripts from P2L 
          
    </td>
</tr>
<tr>
    <td colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <a onmouseover="this.style.cursor='hand';" onclick="DoThis13();">Click Here</a>&nbsp;to go to your Direct Reports Training Transcripts from P2L 
    </td>
    
</tr>
 --%>
<tr>
<td width="2%">&nbsp;</td>

<td colspan="4">
    <% System.out.println("in TrainingDetailPage.jsp. Phase Detail.*******= "+wc.getPhaseDetail()); 
      if (wc.getPhaseDetail() != null) { %> 
    <inc:include-wc component="<%=wc.getPhaseDetail()%>"/>
     <% } %> 
</td>

</tr>
</table>
</body>
</html>