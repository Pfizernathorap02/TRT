
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%@ page import = "com.pfizer.webapp.wc.templates.MainTemplateWpc" %>
<%@ page import = "com.tgix.wc.WebComponent" %>


<%
	MainTemplateWpc wc = (MainTemplateWpc)request.getAttribute(MainTemplateWpc.ATTRIBUTE_NAME);
	response.setHeader("Cache-Control","private"); //HTTP 1.1
	response.setHeader("Pragma","no-cache");		//HTTP 1.0
	response.setDateHeader ("Expires", 0);			//prevents caching at the proxy server 
%>
<!--
Template:<%=wc.getJsp()%>
mAIN:<%=wc.getMain()%>
Template Class:<%=wc.getClass().getName()%>
Main Jsp:<%=wc.getMain().getJsp()%>
Main Class:<%=wc.getMain().getClass().getName()%>
Userbar:<%=wc.getUserBar()%>

Template:/WEB-INF/jsp/templates/mainTemplate.jsp 
mAIN:com.pfizer.webapp.wc.components.ReportSelectWc@1e6ce45 
Template Class:com.pfizer.webapp.wc.templates.MainTemplateWpc 
Main Jsp:/WEB-INF/jsp/components/reportSelect.jsp 
Main Class:com.pfizer.webapp.wc.components.ReportSelectWc 
Userbar:com.pfizer.webapp.wc.global.UserBarWc@1f037dd 
-->
<html>
	<inc:include-wc component="<%=wc.getHead()%>"/>
	
	<body onload="<%=wc.getOnLoad()%>">
	<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
	<script type="text/javascript" lang="JavaScript" src="<%=AppConst.APP_ROOT%>/resources/js/style.js"></script>
	
	<div id="wrap">
		<table class="no_space_table">
			<tr>
				<td>
					<inc:include-wc component="<%=wc.getHeader()%>"/>
				</td>
			</tr>
			<tr>
				<td>
					<inc:include-wc component="<%=wc.getUserBar()%>"/>
				</td>
			</tr>
			<tr>
				<td> 
					<inc:include-wc component="<%=wc.getMain()%>"/>
                    
				</td>
			<tr>
		</table>

	</div>
	</body>		
</html>