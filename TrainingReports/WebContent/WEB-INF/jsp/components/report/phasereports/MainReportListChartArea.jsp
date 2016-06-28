<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListChartAreaWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainReportListChartAreaWc wc = (MainReportListChartAreaWc)request.getAttribute(MainReportListChartAreaWc.ATTRIBUTE_NAME);
	
    
%>
<inc:include-wc component="<%=wc.getChart()%>"/>