<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.PassFail"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>
<%@ page import="com.tgix.Utils.CharacterTools"%>
<%
	ChartLegendWc wc = (ChartLegendWc)request.getAttribute(ChartLegendWc.ATTRIBUTE_NAME);
%>

<table class="no_space_width" style="font-size: .8em; border-width: 1px;" >
	<tr>
		<td bgcolor="#2988C7" width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=test&section=<%=PassFail.CONST_TEST_PASS%>&exam=<%=CharacterTools.escapeHtml(wc.getExamName())%>">&nbsp;&nbsp;&ge; 80%</a></td>
	</tr>
	<tr>
		<td bgcolor="#4AA208" width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=test&section=<%=PassFail.CONST_TEST_FAIL%>&exam=<%=CharacterTools.escapeHtml(wc.getExamName())%>">&nbsp;&nbsp;&lt; 80%</a> </td>
	</tr>
	<tr>
		<td  bgcolor="#D66500" width="10px"></td>
		<td><a href="<%=AppConst.APP_ROOT%>/overview/listreport?type=test&section=<%=PassFail.CONST_TEST_NOT_TAKEN%>&exam=<%=CharacterTools.escapeHtml(wc.getExamName())%>">&nbsp;&nbsp;<%=PassFail.CONST_TEST_NOT_TAKEN%></a> </td>
	</tr>
</table>