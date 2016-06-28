<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpSearch"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SimulateSearchResultListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SwitchUserWc"%>
<%@ page import="com.pfizer.webapp.wc.global.UserBarWc"%>
<%@ page import="com.tgix.Utils.LoggerHelper"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	SwitchUserWc wc = (SwitchUserWc)request.getAttribute(SwitchUserWc.ATTRIBUTE_NAME);
	EmpSearch[] arr = wc.getResults();
    //System.out.println("RESULT SET IN SIMULATESEARCHUSERLIST"+arr.length);
    LoggerHelper.logSystemDebug("TRTtest25 ");
    //UserBarWc userWc = (UserBarWc)request.getAttribute(UserBarWc.ATTRIBUTE_NAME);
    System.out.println("Switch User : wc.getUser().getEmplid()=="+wc.getUser().getEmplid());
%>

<html>
<head>
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
	
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
        <script type="text/javascript" lang="JavaScript" src="/TrainingReports/resources/js/sorttable.js"></script>	

<script type="text/javascript" lang="JavaScript">
addEvent(window, "load", sortables_init);


function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.id+' ').indexOf("tsr_table") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}
</script>
</head>
<body>	
<table class="basic_table"> 
<tr>
<td align ="right">
<%String res = UserSession.getUserSession(request).getIsDelegatedUser();
  boolean adm = UserSession.getUserSession(request).getUser().isAdmin();
  boolean hq = UserSession.getUserSession(request).getUser().isHQUser();
%>
<!-- Added for TRT Phase 2 - New landing page -->
         <% if (!adm && !hq) {%>
     <%--   Infosys migrated code weblogic to jboss changes start here
       <a href="/TrainingReports/newHomePage.do?emplid=<%=wc.getUser().getEmplid()%>">My Direct Reports</a>&nbsp;&nbsp;&nbsp;
        <%}%>
        <a href="/TrainingReports/reportselect.do">Training Reports</a>&nbsp;&nbsp;&nbsp;
        <%if(!hq){%>
        <a href="/TrainingReports/allEmployeeSearch.do">Employee Search</a>&nbsp;&nbsp;&nbsp;
        <%}%>
<% 
if (res != null) {%>
 <a href="/TrainingReports/originaluser.do?emplid=<%=UserSession.getUserSession(request).getIsDelegatedUser()%>">Original User</a>   
     --%>
      <a href="/TrainingReports/newHomePage?emplid=<%=wc.getUser().getEmplid()%>">My Direct Reports</a>&nbsp;&nbsp;&nbsp;
        <%}%>
        <a href="/TrainingReports/reportselect">Training Reports</a>&nbsp;&nbsp;&nbsp;
        <%if(!hq){%>
        <a href="/TrainingReports/allEmployeeSearch">Employee Search</a>&nbsp;&nbsp;&nbsp;
        <%}%>
<% 
if (res != null) {%>
 <a href="/TrainingReports/originaluser?emplid=<%=UserSession.getUserSession(request).getIsDelegatedUser()%>">Original User</a>   
    <!-- Infosys migrated code weblogic to jboss changes end here -->
    
    <%}%></td> 
   </tr>
</table>
<br>
<br>
<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">

<form name="emailSelectForm" >
    <tr>
        <th nowrap>Employee Id</th>
        <th nowrap>Last Name</th>
        <th nowrap>First Name</th>
        
        
    </tr>
    <% if ( arr != null ) {  %> 
        <%  boolean oddEvenFlag=false;
            for (int i=0; i < wc.getResults().length; i++) {
              oddEvenFlag = !oddEvenFlag; 
              EmpSearch curr = arr[i];%>
        
                <tr class="<%=oddEvenFlag?"even":"odd"%>">
                    <td>
                    <!--Modified for TRT Phase 2- Requirement F6.
                    <a href="/TrainingReports/reportselect.do?emplid=<%//=curr.getEmplId()%>">
                    -->
                   <%--  Infosys code changes starts here
                    <a href="/TrainingReports/newHomePage.do?emplid=<%=curr.getEmplId()%>"> --%>
                    <a href="/TrainingReports/newHomePage?emplid=<%=curr.getEmplId()%>">
                    <!-- Infosys migrated code weblogic to jboss changes end here -->
                    <%=Util.toEmpty(curr.getEmplId())%></a></td>
                    <td><%=Util.toEmpty(curr.getLastName())%></td>
                    <td><%=Util.toEmpty(curr.getFirstName())%> </td>
              
                   
                   
                </tr>
        
        <% } %>
    <% } %>
</form>
</table>
</body>
</html>

</table>