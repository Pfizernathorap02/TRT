<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MainSiteReportWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.HashMap" %>

<%
	MainSiteReportWc wc = (MainSiteReportWc)request.getAttribute(MainSiteReportWc.ATTRIBUTE_NAME);
%>


<table class="basic_table">
	<tr>
		<td rowspan="2"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td colspan="2">	
			<table class="basic_table">
				<tr>
					<!-- <td><a href="/TrainingReports/sitereport/runreports.do?report=access">Access Report</a></td>
				
				 -->
				 <td><a href="/TrainingReports/sitereport/runreports?report=access">Access Report</a></td>
				
				
				
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<inc:include-wc component="<%=wc.getReportWc()%>"/>
		</td>
		<td rowspan="8"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
	</tr>
</table>