<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Attendance"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.PassFail"%>
<%@ page import="com.pfizer.db.Sce"%>
<%@ page import="com.pfizer.processor.AttendanceProcessor"%>
<%@ page import="com.pfizer.processor.OverallProcessor"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.processor.PassFailProcessor"%>
<%@ page import="com.pfizer.processor.SceProcessor"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
	ReportListWc wc = (ReportListWc)request.getAttribute(ReportListWc.ATTRIBUTE_NAME);
	List reportNames = wc.getReportNames();
	PassFailProcessor pfp = wc.getProcessor().getPassFailProcessor();
	AttendanceProcessor aProcessor = wc.getProcessor().getAttendanceProcessor();
	SceProcessor sProcessor = wc.getProcessor().getSceProcessor();
	
	AppQueryStrings qString = wc.getQueryStrings();
	String reverseString = "";
	if ( !"reverse".equals( qString.getSortBy().getDirection() ) ) {
		reverseString = "&sb_direction=reverse";
	}	
	List allEmployees = wc.getProcessor().getAllEmployeeList( qString.getSortBy() );
	
%>

<table cellspacing="0" id="employee_table" width="100%">
	<tr>
		<th>District</th>
		<th class="sort_down">Last Name</th>
		<th class="sort_up">First Name</th>
		<th>Role</th>
		<th>Emplid</th>
		<th>Team</th>
		<%-- 
			This is where all the test names get generated, most products have 2 exams
			but a couple has 4.
		--%>
		<% for ( Iterator it = reportNames.iterator(); it.hasNext(); ) { %>
			<th><%=it.next()%></th>	
		<% } %>
		
		<th>Attendance</th>
		<th>SCE</th>
		<th>Overall</th>
	</tr>
	
	<%-- 
		This is where it loops through every employee to generate the list.
	--%>
	<% for ( Iterator it = allEmployees.iterator(); it.hasNext(); ) { 
		OverallResult oResult = (OverallResult)it.next();
		Employee emp = oResult.getEmployee();
		
		boolean doFlag = false;  // flag to determine of a row should be shown.
		
		// these set of if conditions determine if the row should be shown.
		// The list is not filtered before hand so every report loops through
		// every employee and here is where they get filtered.
		if ( "attend".equals( qString.getType() ) ) {
			doFlag = aProcessor.checkStatusByEmployeeId( qString.getSection(), emp.getEmplId() );
		}	
		if ( "sce".equals( qString.getType() ) ) {
			doFlag = sProcessor.checkStatusByEmployeeId( qString.getSection(), emp.getEmplId() );
		}	
		if ( "test".equals( qString.getType() ) ) {
			doFlag = pfp.checkStatusByEmployeeId( qString.getExam(), qString.getSection(), emp.getEmplId() );
		}	
		if ( "overall".equals( qString.getType() ) ) {
			if (OverallProcessor.STATUS_COMPLETE.equals( qString.getSection() ) ) {
				if ( oResult.isPassed() ) {
					doFlag = true;
				}
			}
			if (OverallProcessor.STATUS_INCOMPLETE.equals( qString.getSection() ) ) {
				if ( !oResult.isPassed() ) {
					doFlag = true;
				}
			}
		}	
    %>	
		<% if (doFlag) { %>		
			<%-- 
				This is where the row color logic is happening.
			--%>		
			<% String trClass = "";
				if ( "VP".equals( emp.getRole() ) ) {
					trClass = "class='active_row avp_row'";
				}
				if ( "RM".equals( emp.getRole() ) || "ARM".equals( emp.getRole() ) ) {
					trClass = "class='active_row rm_row'";
				}
				if ( "DM".equals( emp.getRole() ) ) {
					trClass = "class='active_row dm_row'";
				}
			%>
			<tr <%=trClass%> >
				<td><%=(emp.getDistrictDesc()==null)?"":emp.getDistrictDesc()%></td>
				<td><%=emp.getLastName()%></td>
				<td><%=emp.getPreferredName()%></td>
				<td><%=emp.getRole()%></td>
				<td><%=emp.getEmplId()%></td>
				<td><%=emp.getTeamCode()%></td>
				<% for ( Iterator itb = reportNames.iterator(); itb.hasNext(); ) { 
					String testName = (String)itb.next();
					String cssName = "";
					if ( "test".equals( qString.getType() ) ) {
						if ( testName.equals( qString.getExam() ) ) {
							cssName = "class='highlight_col'";
						}
					}
				%>				
					<td <%= PassFail.CONST_TEST_PASS.equals( pfp.getPassedByTestEmployeeId( testName, emp.getEmplId() ) ) ? "" : " style='color : ff0000;' " %> <%=cssName%> ><%=pfp.getTestScoreEmployeeId( testName, emp.getEmplId() )%></td>	
				<% } %>
				
				<td <%= AttendanceProcessor.STATUS_ATTENED.equals( aProcessor.getAttendanceStatusByEmployeeId( emp.getEmplId() ) ) ? "" : " style='color : ff0000;' " %> <%="attend".equals( qString.getType() )?"class='highlight_col'":""%> ><%=aProcessor.getAttendanceStatusByEmployeeId( emp.getEmplId() )%></td>
				<td <%= "DC".equals( sProcessor.getSceStatusByEmployeeId( emp.getEmplId() ) ) ? "" : " style='color : ff0000;' " %> <%="sce".equals( qString.getType() )?"class='highlight_col'":""%> ><%=sProcessor.getSceStatusByEmployeeId( emp.getEmplId() )%></td>
				<td <%= oResult.isPassed() ? "" : " style='color : ff0000;' " %> <%="overall".equals( qString.getType() )?"class='highlight_col'":""%> ><%= oResult.isPassed() ? "Complete" : "Not Complete" %></td>
	
			</tr>
		<% } %>
	<% } %>
</table>
