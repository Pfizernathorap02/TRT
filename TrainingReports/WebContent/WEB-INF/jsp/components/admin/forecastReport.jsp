<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.ForecastReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.Date"%>

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
<TR><TD><IMG height="25" src="/TrainingReports/resources/images/spacer.gif" /> </TD></TR>
	<tr>
        <td>
        	<div class="breadcrumb">
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                <%=wc.getTrackLabel()%>
                
        	</div>
        </td>
	</tr>
	<tr>
        <%if (resultList.size() > 0) { %>
        
		<td>
        <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px"/>&nbsp;
        
        <a href="getReport.do?downloadExcel=true&track=<%=wc.getTrack()%>">Download to Excel</a>
        <br>
        
        <%=resultList.size()%> records found.
        </td>
        <%} else {%>
        <td>No Data Found.</td>
        <%}%>
		<td></td>
		<td colspan="<%=columns-3%>">&nbsp;</td>
	</tr>
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
                        if (columnNames.get(j).toString().indexOf("Date") > 0)
                        {%><td><%=Util.formatDateLong((Date)record.get(columnNames.get(j)))%></td>
                        <%
                        }else{
				%>
				<td><%=record.get(columnNames.get(j))%></td>
				<%}
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