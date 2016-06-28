<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%
	HeaderWc wc = (HeaderWc)request.getAttribute(HeaderWc.ATTRIBUTE_NAME);
    String pageID=wc.getPageId();
%>

<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
        <div id=header_title>Training Reports</div>
        <inc:include-wc component="<%=wc.getCustomHtml()%>"/>
</div>		
		