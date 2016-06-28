<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.bea.wlw.runtime.core.dispatcher.HttpRequest"%> --%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.chart.PieChartBuilder"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.PLC.PlcChartsWc"%>
<%@ page import="com.pfizer.webapp.wc.RBU.RBUPLCChartsWc"%>
<%@ page import="com.pfizer.webapp.wc.components.HomepageWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartDetailWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartHeaderWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>
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
	//PlcChartsWc wc = (PlcChartsWc)request.getAttribute(PlcChartsWc.ATTRIBUTE_NAME);
    RBUPLCChartsWc wc = (RBUPLCChartsWc)request.getAttribute(PlcChartsWc.ATTRIBUTE_NAME);
    ChartHeaderWc chartHeaderWc=(ChartHeaderWc)wc.getHeader();    
%>

<table class="basic_table">
	<tr>
		<td colspan="3">
			<table class="basic_table">
				<tr>
                
                
                
					<td align="center">	
                 
                    
                    <font class="normal_bold">  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;Charts based on</font> <%=chartHeaderWc.getNumTrainees()%> 

			<font class="normal_bold">Trainees.</font>
            <font class="normal_bold">Product:</font> <font class="normal"><%=chartHeaderWc.getProductCode()%></font>
							&nbsp;&nbsp;&nbsp;<font class="normal_bold">Area:</font> <font class="normal"><%=chartHeaderWc.getArea()%></font>&nbsp;&nbsp;

			<font class="normal_bold">Region:</font> <font class="normal"><%=chartHeaderWc.getRegion()%></font>&nbsp;&nbsp;

			<font class="normal_bold">District:</font> <font class="normal"><%=chartHeaderWc.getDistrict()%></font>&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25"></td>
                    
                    <%
                
                    
                    %>
				</tr>				
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top" width="100%">
			<table class="basic_table">
				<tr>
					<td valign="top" align="center" >
                     <inc:include-wc component="<%=wc.getWebComponent()%>"/>
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
             <inc:include-wc component="<%=wc.getTerritoryForm()%>"/>
		</td>	
	</tr>
</table>