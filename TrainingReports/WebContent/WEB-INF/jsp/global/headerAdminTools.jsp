<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderAdminToolsWc"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderPWRAWc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%
	HeaderAdminToolsWc wc = (HeaderAdminToolsWc)request.getAttribute(HeaderAdminToolsWc.ATTRIBUTE_NAME);
    String sReportType = (String)request.getSession().getAttribute("ReportType");
%>

<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<div id=header_title><%=wc.getPageId()%> Admin Tool</div>
	
	<UL id=header_top_nav>                                     
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
        <%if(sReportType.equalsIgnoreCase("PDFHS")){%>
		<LI><a href="#" onclick="openPDFFAQ()">FAQ</a></LI>
        <%}else if(sReportType.equalsIgnoreCase("SPF")){%>
        <LI><a href="#" onclick="openSPFFAQ()">FAQ</a></LI>        
        <%}%>                        
       <LI class=last><a href="#" onclick="openContact()" >Contact</a></LI>
	</UL>
</div>		
		