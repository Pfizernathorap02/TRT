<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.report.LoginReportWc"%>
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
	ProductSliceReportWc wc = (ProductSliceReportWc)request.getAttribute(ProductSliceReportWc.ATTRIBUTE_NAME);
%>
<br>
<% if (wc.isShowTitle()) { %>
	<h2>Detail Report</h2>
	<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=wc.getExcelLink()%>">Download to Excel</a>
<% } %>
<table class="blue_table">
		<tr>
			<th>Pie</th>	
			<th>Section</th>	
			<th>Aricept</th>	
			<th>Caduet</th>	
			<th>Chantix</th>	
			<th>Celebrex</th>	
			<th>Detrol LA</th>	
			<th>Exubera</th>	
			<th>Geodon</th>	
			<th>Lipitor</th>	
			<th>Lyrica</th>	
			<th>Rebif</th>	
			<th>Revatio</th>	
			<th>Spiriva</th>	
			<th>Viagra</th>	
			<th>Zyrtec</th>	
			<th>Total</th>	
		</tr>

<%	boolean oddEvenFlag = true;
	for (Iterator it = wc.getResults().iterator(); it.hasNext(); ) {
		HashMap map = (HashMap)it.next();
		oddEvenFlag = !oddEvenFlag;
%>
		<tr class="<%=oddEvenFlag?"even":"odd"%>">
			<td><%=map.get("filter_pie".toUpperCase())%></td>	
			<td><%=Util.toEmpty((String)map.get("filter_slice".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("arcp".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("cadt".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("chtx".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("clbr".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("detr".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("exub".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("geod".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("lptr".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("lyrc".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("rebf".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("rvto".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("sprv".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("viag".toUpperCase()))%></td>	
			<td><%=Util.toEmpty((String)map.get("zyrt".toUpperCase()))%></td>	
			<td><B><%=Util.toEmpty((String)map.get("total".toUpperCase()))%></b></td>	
		</tr>
<%	} %>
</table>