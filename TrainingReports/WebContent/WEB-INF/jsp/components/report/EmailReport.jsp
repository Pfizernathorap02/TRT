<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.wc.components.report.EmailReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MainSiteReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ProductSliceReportWc"%>
<%@ page import="com.tgix.Utils.Util"%>
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
	EmailReportWc wc = (EmailReportWc)request.getAttribute(EmailReportWc.ATTRIBUTE_NAME);
	System.out.println(wc.getARMResults().size());
%>
<br>

<h2>Email Effectiveness Report</h2>
<table class="blue_table">
		<tr>
			<th>Cluster</th>	
			<th>Total # Emails Sent to DM's</th>	
			<th>Total # DM's Visited Site</th>	
			<th>% DM's Visited Site</th>	
		</tr>

<%	boolean oddEvenFlag = true;
	for (Iterator it = wc.getDMResults().iterator(); it.hasNext(); ) {
		HashMap map = (HashMap)it.next();
		oddEvenFlag = !oddEvenFlag;
%>
		<tr class="<%=oddEvenFlag?"even":"odd"%>">
			<td><a href="/TrainingReports/sitereport/runreports?report=emaildetailDM&type=<%=Util.toEmpty((String)map.get("cluster_cd".toUpperCase()))%>"><%="Specialty Marke".equals(Util.toEmpty((String)map.get("cluster_cd".toUpperCase())))?"Specialty Market":Util.toEmpty((String)map.get("cluster_cd".toUpperCase()))%></a></td>	
			<td><%=Util.toEmpty((String)map.get("num_emailed".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("num_logged_on".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("percent".toUpperCase()))%></td>	
		</tr>
<%	} %>
</table>

<br>

<table class="blue_table">
		<tr>
			<th>Cluster</th>	
			<th>Total # Emails Sent to RM's</th>	
			<th>Total # RM's Visited Site</th>	
			<th>% RM's Visited Site</th>	
		</tr>

<%	oddEvenFlag = true;
	for (Iterator it = wc.getRMResults().iterator(); it.hasNext(); ) {
		HashMap map = (HashMap)it.next();
		oddEvenFlag = !oddEvenFlag;
%>
		<tr class="<%=oddEvenFlag?"even":"odd"%>">
			<td><a href="/TrainingReports/sitereport/runreports?report=emaildetailRM&type=<%=Util.toEmpty((String)map.get("cluster_cd".toUpperCase()))%>"><%="Specialty Marke".equals(Util.toEmpty((String)map.get("cluster_cd".toUpperCase())))?"Specialty Market":Util.toEmpty((String)map.get("cluster_cd".toUpperCase()))%></a></td>
			<td><%=Util.toEmpty((String)map.get("num_emailed".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("num_logged_on".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("percent".toUpperCase()))%></td>	
		</tr>
<%	} %>
</table>

<br>

<table class="blue_table">
		<tr>
			<th>Cluster</th>	
			<th>Total # Emails Sent to ARM's</th>	
			<th>Total # ARM's Visited Site</th>	
			<th>% ARM's Visited Site</th>	
		</tr>

<%	oddEvenFlag = true;
	for (Iterator it = wc.getARMResults().iterator(); it.hasNext(); ) {
		HashMap map = (HashMap)it.next();
		oddEvenFlag = !oddEvenFlag;
%>
		<tr class="<%=oddEvenFlag?"even":"odd"%>">
			<td><a href="/TrainingReports/sitereport/runreports?report=emaildetailARM&type=<%=Util.toEmpty((String)map.get("cluster_cd".toUpperCase()))%>"><%="Specialty Marke".equals(Util.toEmpty((String)map.get("cluster_cd".toUpperCase())))?"Specialty Market":Util.toEmpty((String)map.get("cluster_cd".toUpperCase()))%></a></td>	
			<td><%=Util.toEmpty((String)map.get("num_emailed".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("num_logged_on".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("percent".toUpperCase()))%></td>	
		</tr>
<%	} %>
</table>
