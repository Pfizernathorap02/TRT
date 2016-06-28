<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.processor.OverallProcessor"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>

<%

	ChartLegendWc wc = (ChartLegendWc)request.getAttribute(ChartLegendWc.ATTRIBUTE_NAME);
    

%>



<table class="no_space_width" style="font-size: .8em; border-width: 1px;" >

	<tr>

		<td bgcolor="#2988C7" width="10px"></td>

		<td><a href="<%=AppConst.APP_ROOT%>/GNSM/listReportGNSM?type=<%=wc.getExamName()%>&section=<%=OverallProcessor.STATUS_COMPLETE%>">&nbsp;&nbsp;<%=OverallProcessor.STATUS_COMPLETE%></a> &nbsp;&nbsp; </td>

	</tr> 

	<tr>

		<td bgcolor="#D66500" width="10px"></td>

		<td><a href="<%=AppConst.APP_ROOT%>/GNSM/listReportGNSM?type=<%=wc.getExamName()%>&section=<%=OverallProcessor.STATUS_INCOMPLETE%>">&nbsp;&nbsp;Registered</a> &nbsp;&nbsp; </td>

	</tr>
	<tr>

		<td bgcolor="#FFF39C" width="10px"></td>

		<td><a href="<%=AppConst.APP_ROOT%>/GNSM/listReportGNSM?type=<%=wc.getExamName()%>&section=<%=OverallProcessor.STATUS_ON_LEAVE%>">&nbsp;&nbsp;<%=OverallProcessor.STATUS_ON_LEAVE%></a> &nbsp;&nbsp; </td>

	</tr>

</table>