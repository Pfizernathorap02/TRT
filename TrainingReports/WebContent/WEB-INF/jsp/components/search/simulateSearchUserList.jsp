<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpSearch"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SimulateSearchResultListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.List"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	SimulateSearchResultListWc wc = (SimulateSearchResultListWc)request.getAttribute(SimulateSearchResultListWc.ATTRIBUTE_NAME);
	List ret = wc.getResults();
    System.out.println("RESULT SET IN SIMULATESEARCHUSERLIST"+ret.size());
%>

<html>
<head>
		<link rel="stylesheet" type="text/css" href="/TrainingReports/WebContent/resources/css/header.css"/>
	
		<link rel="stylesheet" type="text/css" href="/TrainingReports/WebContent/resources/css/trainning.css"/>
        <script type="text/javascript" lang="JavaScript" src="/TrainingReports/WebContent/resources/js/sorttable.js"></script>	

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
    <% if ( ret != null ) {  %> 
        <%  boolean oddEvenFlag=false;
            for (int i=0; i < wc.getResults().size(); i++) {
              oddEvenFlag = !oddEvenFlag; 
              EmpSearch curr = (EmpSearch)wc.getResults().get(i); %>
        
                <tr class="<%=oddEvenFlag?"even":"odd"%>">
                    <td>
                    <!-- modified for TRT Phase 2 - Requirement F6. -->
                   <%--  <a href="/TrainingReports/reportselect.do?emplid=<%=curr.getEmplId()%>"> --%>
                   <%-- Infosys code changes starts here
                   <a href="/TrainingReports/newHomePage.do?emplid=<%=curr.getEmplId()%>"> --%>
                    <a href="/TrainingReports/newHomePage?emplid=<%=curr.getEmplId()%>">
                    <!-- Infosys migrated code weblogic to jboss changes end here -->
                    <%=Util.toEmpty(curr.getSalesPosId())%></a></td>
                    <td><%=Util.toEmpty(curr.getLastName())%></td>
                    <td><%=Util.toEmpty(curr.getFirstName())%> </td>
                    <td><%=Util.toEmpty(curr.getEmplId())%> </td>
                    <td><%=Util.toEmpty(curr.getRoleCd())%></td>
                    <td><%=Util.toEmpty(curr.getBusUnit())%></td>
                    <td><%=Util.toEmpty(curr.getSalesOrg())%></td>
                </tr>
        
        <% } %>
    <% } %>
</form>
</table>
</body>
</html>

</table>