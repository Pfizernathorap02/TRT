<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.wc.components.report.EmailDetailReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.EmailReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.LoginReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MainSiteReportWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ProductSliceReportWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	EmailDetailReportWc wc = (EmailDetailReportWc)request.getAttribute(EmailDetailReportWc.ATTRIBUTE_NAME);
%>

<inc:include-wc component="<%=wc.getNoLoginResults()%>"/>

<inc:include-wc component="<%=wc.getLoginResults()%>"/>
