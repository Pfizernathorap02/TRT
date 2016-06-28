<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpSearch"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.AllEmployeeSearchResultWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SimulateSearchResultListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>

<%
	AllEmployeeSearchResultWc wc = (AllEmployeeSearchResultWc)request.getAttribute(AllEmployeeSearchResultWc.ATTRIBUTE_NAME);
	List ret = wc.getResults();
    System.out.println("RESULT SET IN SIMULATESEARCHUSERLIST"+ret.size());
%>

<html>
<head>
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
	
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
        <script type="text/javascript" language="JavaScript" src="/TrainingReports/resources/js/sorttable.js"></script>	

<script type="text/javascript" language="JavaScript">
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
<table class="blue_table_without_border" width="100%">
<% if ( wc.getResults().size() != 0) {  %> 
<tr><td align="left"><%=wc.getResults().size()%> records found.</td></tr>

<tr><td>
<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">
<form name="emailSelectForm" >

    <tr>
        <th nowrap>Sales Position ID</th>
        <th nowrap>Last Name</th>
        <th nowrap>First Name</th>
        <th nowrap>Employee Id</th>    
        <th nowrap>Role</th>
        <th nowrap>Business Unit</th>
        <th nowrap>Sales Organization</th>
        
    </tr>
    
        <%  boolean oddEvenFlag=false;
            for (int i=0; i < wc.getResults().size(); i++) {
              oddEvenFlag = !oddEvenFlag; 
              EmpSearch curr = (EmpSearch)wc.getResults().get(i); %>
        
                <tr class="<%=oddEvenFlag?"even":"odd"%>">
                    <td><%=Util.toEmpty(curr.getSalesPosId())%></td>
                    <td>
                    <%-- Infosys migrated code weblogic to jboss changes start here
                    <a href="/TrainingReports/p2l/employeeSearchDetailPage.do?emplid=<%=curr.getEmplId()%>">
                     --%>
                    
                    <a href="/TrainingReports/p2l/employeeSearchDetailPage?emplid=<%=curr.getEmplId()%>">
                     <!-- Infosys migrated code weblogic to jboss changes end here -->
                    <%=Util.toEmpty(curr.getLastName())%></a></td>
                    <td><%=Util.toEmpty(curr.getFirstName())%> </td>
                    <td><%=Util.toEmpty(curr.getEmplId())%> </td>
                    <td><%=Util.toEmpty(curr.getRoleCd())%></td>
                    <td><%=Util.toEmpty(curr.getBusUnit())%></td>
                    <td><%=Util.toEmpty(curr.getSalesOrg())%></td>
                </tr>
        
        <% } %>
    
</form>
</table>
<% } else {%>
        <tr><td align="left">No Records found.</td></tr>
    <%}%>
</td></tr>
</table>

</body>
</html>

