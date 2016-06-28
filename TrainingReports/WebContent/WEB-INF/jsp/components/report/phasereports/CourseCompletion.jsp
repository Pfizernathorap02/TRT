<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.CourseCompletionWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	CourseCompletionWc wc = (CourseCompletionWc)request.getAttribute(CourseCompletionWc.ATTRIBUTE_NAME);
    
%>
<div class="page_content_wrapper">
	<h1>Course Completion Page</h1>
	
<p>
Completions can be reverted by clicking on undo link next to a trainee. Please note that undo option is available only until the completions data is passed to the P2L system - which is scheduled for 4:30pm everyday. When a completion can no longer be reverted it will display "Locked" in place of the undo link.
</p>    
<inc:include-wc component="<%=wc.getSearchForm()%>"/>

<br>
<inc:include-wc component="<%=wc.getResult()%>"/>

</div>