<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>
<%
	ChartLegendWc wc = (ChartLegendWc)request.getAttribute(ChartLegendWc.ATTRIBUTE_NAME);
%>

<table class="no_space_width" style="font-size: .8em; border-width: 1px;" >
	<tr>
		<td bgcolor="#2988C7" width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=attend&section=Attended">&nbsp;&nbsp;Attended</a> <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif" onMouseOver="stm(Text['Attended'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
	</tr>
	<tr>
		<td bgcolor="#00FFFF" width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=attend&section=Regional%20Training">&nbsp;&nbsp;Regional Training</a> <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['Regional Training'],Style[0])" onMouseOut="htm()" ></td>
	</tr>
	<tr>
		<td bgcolor="#FFF39C" width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=attend&section=On%20Leave">&nbsp;&nbsp;On Leave</a> <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['On Leave'],Style[0])" onMouseOut="htm()" ></td>
	</tr>
</table>