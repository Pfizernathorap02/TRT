<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.processor.OverallProcessor"%>

<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartP2lLegendWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.p2l.OverallStatusWc"%>

<%

	OverallStatusWc wc = (OverallStatusWc)request.getAttribute(OverallStatusWc.ATTRIBUTE_NAME);

%>


    <TABLE class="blue_table">
    <TR><TH colspan="1" align="left">Training Information</TH></TR>
    <TR bgcolor="#DDDDDD"><TD>Overall Status</TD></TR>
    
    <TR><TD><span><%=wc.getStatus()%></span></TD></TR>
    
    </TABLE>
