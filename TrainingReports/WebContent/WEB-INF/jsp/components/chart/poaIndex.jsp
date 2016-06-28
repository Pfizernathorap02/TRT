<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartIndexWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	ChartIndexWc wc = (ChartIndexWc)request.getAttribute(ChartIndexWc.ATTRIBUTE_NAME);
%>

<table class="basic_table">
	<tr>
		<td colspan="3">
			<table class="basic_table">
				<tr>
					<td align="center">		
						<inc:include-wc component="<%=wc.getHeader()%>"/>
					</td>
				</tr>
				<tr>
					<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25"></td>
				</tr>				
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top" width="100%">
			<table class="basic_table">
				<tr>
					<td valign="top" align="center" >
						<% if ( wc.getNumTrainess() > 0 ) { %>
							<inc:include-wc component="<%=wc.getChartList()%>"/>
						<% } else { %>
							<p>There are no trainees that meet this criteria</p>
						<% } %>
					</td>
				</tr>
			</table>
		</td>
		<td valign="top" align="right">
			<table class="no_space_width" height="100%" >
				<tr>
					<td class="thin_white" height="10"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif"></td>
				</tr>			
				<tr>
					<td class="thin_grey" height="550"></td>
				</tr>			
			</table>
		</td>
		<td valign="top">
			<inc:include-wc component="<%=wc.getRightBar()%>"/>
		</td>	
	</tr>
</table>