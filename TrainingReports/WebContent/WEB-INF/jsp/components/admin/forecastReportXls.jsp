<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.wc.components.ForecastReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
	ForecastReportWc wc = (ForecastReportWc) request
			.getAttribute(EditReportWc.ATTRIBUTE_NAME);
	List resultList = wc.getReportList();
	ArrayList columnNames = new ArrayList();
	HashMap record = new HashMap();
	int columns = 0;
	if (resultList != null && resultList.size() > 0)
		record = (HashMap) resultList.get(0);
	if (record != null) {
		columns = record.size();
		for (Iterator i = record.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			columnNames.add(key);
		}
	}
%>
<table class="basic_table">

	
	<tr>
		<td colspan="4">
		<table class="blue_table" width="100%">
			<tr>
				<%
					for (int j = 0; j < columnNames.size(); j++) {
				%>
				<th><%=columnNames.get(j)%></th>
				<%
					}
				%>
			</tr>
			<%
				boolean oddEvenFlag = false;
				for (int i = 0; i < resultList.size(); i++) {
					oddEvenFlag = !oddEvenFlag;
					record = (HashMap) resultList.get(i);
			%>
			<tr class="<%=oddEvenFlag?"even":"odd"%>">
				<%
					for (int j = 0; j < columns; j++) {
				%>
				<td><%=record.get(columnNames.get(j))%></td>
				<%
					}
				%>
			</tr>
			<%
				}
			%>
		</table>
		</td>
	</tr>
</table>