<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.processor.OverallProcessor"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartP2lLegendWc"%>

<%

	ChartP2lLegendWc wc = (ChartP2lLegendWc)request.getAttribute(ChartP2lLegendWc.ATTRIBUTE_NAME);

%>



<table class="no_space_width" style="font-size: .8em; border-width: 1px;" >

	<tr>

		<td bgcolor="#2988C7" width="10px"></td>

		<td><a href="listreport?section=Complete&activitypk=<%=wc.getActivityId()%>">&nbsp;&nbsp;Complete</a></td>

	</tr>
    <%--
    <% if (wc.showExempt()) { %>

	<tr>

		<td bgcolor="#ff9900" width="10px"></td>

		<td><a href="listreport.do?section=Exempt&activitypk=<%=wc.getActivityId()%>">&nbsp;&nbsp;Waived</a></td>

	</tr>
    <% } %> --%>

    <% if (wc.showAssigned()) { %>

	<tr>

		<td bgcolor="#99cc00" width="10px"></td>

		<td><a href="listreport?section=Assigned&activitypk=<%=wc.getActivityId()%>">&nbsp;&nbsp;Assigned</a></td>

	</tr>
    <% } %>

<%--
    <% if (wc.showPending()) { %>
	<tr>
		<td bgcolor="#00FFFF" width="10px"></td>
		<td><a href="listreport.do?section=Pending&activitypk=<%=wc.getActivityId()%>">&nbsp;&nbsp;Pending Approval</a>&nbsp;&nbsp;&nbsp;</td>
	</tr> 
    <% } %>--%>
	<tr>

		<td bgcolor="#993300" width="10px"></td>

		<td><a href="listreport?section=Registered&activitypk=<%=wc.getActivityId()%>">&nbsp;&nbsp;Not&nbsp;Complete</a>&nbsp;&nbsp;&nbsp;</td>

	</tr>
	<tr>

		<td bgcolor="#fdf39c" width="10px"></td>

		<td><a href="listreport?section=On-Leave&activitypk=<%=wc.getActivityId()%>">&nbsp;&nbsp;On-Leave</a></td>

	</tr>

</table>