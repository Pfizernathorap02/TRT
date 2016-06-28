<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.P2lTrack"%>
<%@ page import="com.pfizer.db.P2lTrackPhase"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.EmplSearchResultWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.TrainingSummaryWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	EmplSearchResultWc wc = (EmplSearchResultWc)request.getAttribute(EmplSearchResultWc.ATTRIBUTE_NAME);
    P2lTrack track = wc.getTrack();
    P2lTrackPhase phase = (P2lTrackPhase)track.getPhases().get(0);
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

<table class="blue_table_without_border">
<tr><td>
<% if ( wc.getResult() != null && wc.getResult().length > 0) {  %>
<%=wc.getResult().length%> records found.</td>
<td><%} else {%>
No records found.</td>
<%}%>
</tr>   
</table>

<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">
<form name="emailSelectForm" >
    <% if ( wc.getResult() != null && wc.getResult().length > 0) {  %>
    <tr>
        <th nowrap>Last Name</th>
        <th nowrap>First Name</th>
        <th nowrap>Employee Id</th>
        <th nowrap>Role</th>
        <th nowrap>Sales Org</th>
    </tr> <%}%>
    <% if ( wc.getResult() != null ) {  %> 
        <%  boolean oddEvenFlag=false;
            for (int i=0; i < wc.getResult().length; i++) {
              oddEvenFlag = !oddEvenFlag; 
              Employee curr = wc.getResult()[i]; %>
        
                <tr class="<%=oddEvenFlag?"even":"odd"%>">
                <td><a href="detailpage?activitypk=<%=phase.getRootActivityId()%>&emplid=<%=curr.getEmplId()%>" target="main" onclick="window.close();"> <%=Util.toEmpty(curr.getLastName())%></a></td>
                    <td><%=Util.toEmpty(curr.getFirstName())%> </td>
                    <td><%=Util.toEmpty(curr.getEmplId())%> </td>
                    <td><%=Util.toEmpty(curr.getRole())%></td>
                      <td><%=Util.toEmpty(curr.getSalesOrgDesc() )%></td>
                </tr>
        
        <% } %>
    <% } %>
</form>
</table>
</body>
</html>