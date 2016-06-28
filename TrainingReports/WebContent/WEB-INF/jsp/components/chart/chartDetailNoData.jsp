<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartDetailWc"%>

<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%

	ChartDetailWc wc = (ChartDetailWc)request.getAttribute(ChartDetailWc.ATTRIBUTE_NAME);

%>
<table class="no_space_width" align="center" >
<tr valign="middle" >
    <td colspan="=2"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="50"></td>
</tr>
<tr valign="middle" >
<td align="center">
 <%=wc.getChart().getLabel()%>
 <br>
 No Data Found.
</td>
</tr>
</table>

