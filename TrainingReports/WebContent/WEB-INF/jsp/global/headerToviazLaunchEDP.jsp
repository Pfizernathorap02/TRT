<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderToviazLaunchEDPWc"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderPWRAWc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%
    HeaderToviazLaunchEDPWc wc = (HeaderToviazLaunchEDPWc)request.getAttribute(HeaderPWRAWc.ATTRIBUTE_NAME);
    String sReportType = (String)request.getSession().getAttribute("ReportType");
    if(sReportType == null) sReportType = "";
%>

<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<div id=header_title>Toviaz Launch - Employee Details Page</div>
	
	<UL id=header_top_nav>                                     
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
        <%if(sReportType.equalsIgnoreCase("PDFHS")){%>
		<LI><a href="#" onclick="openPDFFAQ()">FAQ</a></LI>
        <%}else if(sReportType.equalsIgnoreCase("SPF")){%>
        <LI><a href="#" onclick="openSPFFAQ()">FAQ</a></LI>        
        <%}else if(sReportType.equalsIgnoreCase("RBU")){%>  
        <LI><a href="#" onclick="openRBUFAQ()">FAQ</a></LI>
        <%}%>      
        <%--
        <LI><a href="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/RbuSearchController.jpf">PSCPT Search</a></LI>            
        --%>
       <LI class=last><a href="#" onclick="openContact()" >Contact</a></LI>
	</UL>
</div>		
		