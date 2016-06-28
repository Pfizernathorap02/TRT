<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartDetailWc"%>

<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%

	ChartDetailWc wc = (ChartDetailWc)request.getAttribute(ChartDetailWc.ATTRIBUTE_NAME);

%>

<%=wc.getChart().getImageMap()%>



<table class="no_space_width" >

	<tr>

		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="0"></td>

		<td align="center">

			<img src="<%= wc.getChart().getGraphURL()%>"  border=0 usemap="#<%= wc.getChart().getFilename()%>">

		</td>

		<td colspan="3" align="center"><inc:include-wc component="<%=wc.getLegend()%>"/></td>

		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="0"></td>

	</tr>

	<tr>

		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="0"></td>

		<td align="center" >

			<% if ( wc.isShowDescription() ) { %>

				<font class="small"><%=wc.getDescription()%></font>

			<% } %>

		</td>

		<td colspan="2"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="0"></td>

	</tr>

</table>

