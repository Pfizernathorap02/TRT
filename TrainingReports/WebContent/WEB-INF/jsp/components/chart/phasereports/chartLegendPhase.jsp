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

		<td><a href="<%=AppConst.APP_ROOT%>/phase/listreport?section=Complete&activitypk=<%=wc.getKey()%>">&nbsp;&nbsp;Complete</a></td>

	</tr>

	<tr>

		<td bgcolor="#ff9900" width="10px"></td>

		<td><a href="<%=AppConst.APP_ROOT%>/phase/listreport?section=Exempt&activitypk=<%=wc.getKey()%>">&nbsp;&nbsp;Exempt</a></td>

	</tr>
	<tr>

		<td bgcolor="#99cc00" width="10px"></td>

		<td><a href="<%=AppConst.APP_ROOT%>/phase/listreport?section=Assigned&activitypk=<%=wc.getKey()%>">&nbsp;&nbsp;Assigned</a></td>

	</tr>
	<tr>

		<td bgcolor="#993300" width="10px"></td>

		<td><a href="<%=AppConst.APP_ROOT%>/phase/listreport?section=Registered&activitypk=<%=wc.getKey()%>">&nbsp;&nbsp;Registered</a>&nbsp;&nbsp;&nbsp;</td>

	</tr>
	<tr>

		<td bgcolor="#fdf39c" width="10px"></td>

		<td><a href="<%=AppConst.APP_ROOT%>/phase/listreport?section=On-Leave&activitypk=<%=wc.getKey()%>">&nbsp;&nbsp;On-Leave</a></td>

	</tr>

</table>