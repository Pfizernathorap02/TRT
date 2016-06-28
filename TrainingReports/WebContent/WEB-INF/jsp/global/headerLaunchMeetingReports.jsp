<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderLaunchMeetingReportsWc"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderToviazLaunchReportsWc"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderPWRAWc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%
	HeaderLaunchMeetingReportsWc wc = (HeaderLaunchMeetingReportsWc)request.getAttribute(HeaderPWRAWc.ATTRIBUTE_NAME);
    String sReportType = (String)request.getSession().getAttribute("ReportType");
    if(sReportType == null) sReportType = "";
%>

<div id=top_header>
	<!-- Infosys -  migration changes start here -->
	<%-- <a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a> --%>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<!-- Infosys -  migration changes end here -->
	<div id=header_title>Launch Meeting Reports</div>
	
	<UL id=header_top_nav>                      
		<!-- Infosys -  migration changes start here -->               
        <%-- <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI> --%>
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
        <!-- Infosys -  migration changes end here -->
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
		