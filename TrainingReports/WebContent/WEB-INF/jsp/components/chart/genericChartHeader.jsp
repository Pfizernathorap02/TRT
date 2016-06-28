<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartHeaderWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.GenericChartHeaderWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
	GenericChartHeaderWc wc = (GenericChartHeaderWc)request.getAttribute(GenericChartHeaderWc.ATTRIBUTE_NAME);
%>

<table class="no_space_width" width="100%"> 
	<tr>
            <% if(wc.getLeftWc() != null) { %>
        <td>
            <inc:include-wc component="<%=wc.getLeftWc()%>"/>
        </td>
            <% } %>
		<td align="right">
            <% if (!Util.isEmpty(wc.getTeam())) { %>
			<font class="normal_bold">Team:</font> <font class="normal"><%=wc.getTeam()%></font>&nbsp;&nbsp;
            <% } %>
		<%-- <!--	<font class="normal_bold">Area:</font> <font class="normal"><%=//wc.getArea()%></font>&nbsp;&nbsp;
			<font class="normal_bold">Region:</font> <font class="normal"><%=//wc.getRegion()%></font>&nbsp;&nbsp;
			<font class="normal_bold">District:</font> <font class="normal"><%=wc.getDistrict()%></font>
		--> --%>
        
		</td>
            <% if(wc.getRightWc() != null) { %>
        <td>
            <inc:include-wc component="<%=wc.getRightWc()%>"/>
        </td>
            <% 
            } %>
	</tr>
</table>