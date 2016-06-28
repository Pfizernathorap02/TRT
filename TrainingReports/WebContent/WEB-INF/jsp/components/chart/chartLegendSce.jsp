<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Sce"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>
<%
	ChartLegendWc wc = (ChartLegendWc)request.getAttribute(ChartLegendWc.ATTRIBUTE_NAME);
%>

<table class="no_space_width" style="font-size: .8em; border-width: 1px;" >
	<tr>
		<td bgcolor="#2988C7" width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=sce&section=<%=Sce.STATUS_DC%>">&nbsp;&nbsp;<%=Sce.STATUS_DC%></a> <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"   onMouseOver="stm(Text['Demonstrated Competence'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
	</tr>
	<tr>
		<td  bgcolor="#FFF39C"  width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=sce&section=<%=Sce.STATUS_NI%>">&nbsp;&nbsp;<%=Sce.STATUS_NI%></a> <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['Needs Improvement'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
	</tr>
	<tr>
		<td bgcolor="#4AA208" width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=sce&section=<%=Sce.STATUS_UN%>">&nbsp;&nbsp;<%=Sce.STATUS_UN%></a> <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['Unacceptable'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
	</tr>
	<tr>
		<td  bgcolor="#D66500" width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=sce&section=<%=Sce.STATUS_NOT_COMPLETE%>">&nbsp;&nbsp;<%=Sce.STATUS_NOT_COMPLETE%></a> <img src="<%=AppConst.APP_ROOT%>/resources/images/i_10x10.gif"  onMouseOver="stm(Text['Not Complete sce'],Style[0])" onMouseOut="htm()" style="font-style: italic;" ></td>
	</tr>
</table>