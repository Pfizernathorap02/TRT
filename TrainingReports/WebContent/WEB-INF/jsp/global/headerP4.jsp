<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderP4Wc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%
	HeaderP4Wc wc = (HeaderP4Wc)request.getAttribute(HeaderP4Wc.ATTRIBUTE_NAME);
    String pageID=wc.getPageId();
%>

<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
        
        <%=wc.getCustomHeader()%>
        
</div>		
		