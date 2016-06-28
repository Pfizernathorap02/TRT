<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.report.LoginReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MainSiteReportWc"%>
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
	LoginReportWc wc = (LoginReportWc)request.getAttribute(LoginReportWc.ATTRIBUTE_NAME);
%>
<br>

<% if (wc.isShowTitle()) { %>
	<h2><%=wc.getHeader()%></h2>
	<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=wc.getExcelLink()%>">Download to Excel</a>
<% } %>
<table class="blue_table">
		<tr>
			<th>Employee Id</th>	
			<th>First Name</th>	
			<th>Last Name</th>	
			<th># Login</th>	
			<th># Report</th>	
			<th># Report Details</th>	
			<th># Employee Detail</th>	
			<th># Total</th>	
			<th>Email</th>	
			<th>Cluster</th>	
			<th>Team</th>	
			<th>Role</th>	
			<th>Area</th>	
			<th>Region</th>	
			<th>District</th>	
		</tr>

<%	boolean oddEvenFlag = true;
	HashMap map = null;
	int loginTotal = 0;
	int pieTotal = 0;
	int listTotal = 0;
	int detailTotal = 0;
	for (Iterator it = wc.getResults().iterator(); it.hasNext(); ) {
		map = (HashMap)it.next();
		oddEvenFlag = !oddEvenFlag;
		loginTotal = loginTotal + Util.parseInt(map.get("logins".toUpperCase()));
		pieTotal = pieTotal + Util.parseInt(map.get("pie_report".toUpperCase()));
		listTotal = listTotal + Util.parseInt(map.get("list_report".toUpperCase()));
		detailTotal = detailTotal + Util.parseInt(map.get("detail_report".toUpperCase()));
%>
		<tr class="<%=oddEvenFlag?"even":"odd"%>">
			<td><%=map.get("user_id".toUpperCase())%></td>	
			<td><%=Util.toEmpty((String)map.get("FIRST_NAME".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("LAST_NAME".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("logins".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("pie_report".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("list_report".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("detail_report".toUpperCase()))%></td>	
			<td><b><%=Util.toEmpty((String)map.get("total_hits".toUpperCase()))%></b></td>	
			<td><a href="mailto:<%=Util.toEmpty((String)map.get("EMAIL_ADDRESS".toUpperCase()))%>?subject=FFT Training Follow-up"><%=Util.toEmpty((String)map.get("EMAIL_ADDRESS".toUpperCase()))%></a></td>
			<td><%=Util.toEmpty((String)map.get("cluster_cd".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("TEAM_CD".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("TERRITORY_ROLE_CD".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("AREA_DESC".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("REGION_DESC".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("DISTRICT_DESC".toUpperCase()))%></td>	
		</tr>
<%	} %>
<% if (map != null) {%>
		<tr class="<%=oddEvenFlag?"even":"odd"%>">
			<th> </th>
			<th> </th>
			<th ><b>Totals</b></th>	
			<th align="left"><b><%=loginTotal%></b></th>	
			<th align="left"><b><%=pieTotal%></b></th>	
			<th align="left"><b><%=listTotal%></b></th>	
			<th align="left"><b><%=detailTotal%></b></th>	
			<th align="left"><b><%=pieTotal+listTotal+detailTotal%></b></th>	
			<th> </th>
			<th> </th>
			<th> </th>
			<th> </th>
			<th> </th>
			<th> </th>
			<th> </th>
		</tr>
<% } %>
</table>