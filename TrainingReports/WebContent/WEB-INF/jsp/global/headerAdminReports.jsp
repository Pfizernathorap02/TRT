<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderAdminReportsWc"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderPWRAWc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%
	HeaderAdminReportsWc wc = (HeaderAdminReportsWc)request.getAttribute(HeaderPWRAWc.ATTRIBUTE_NAME);
    String sReportType = (String)request.getSession().getAttribute("ReportType");
    if(sReportType == null) sReportType = "";
    String pageId = wc.getPageId();
  //  System.out.println("PageId################" + pageId);
%>

<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<% 
        if(pageId.equals("dashreport")){
    %>
    <div id=header_title>Product Training (PSCPT) Reports<br><div style="font-size:.7em; color:red;"><%=wc.getHeadString()%></div></div>
    <%
        }
        else{
    %>
     <div id=header_title>Product Training (PSCPT) Admin Reports</div>
    <%
        }
    %>
       
        <UL id=header_top_nav>                                     
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
        <%if(sReportType.equalsIgnoreCase("PDFHS")){%>
		<LI><a href="#" onclick="openPDFFAQ()">FAQ</a></LI>
        <%}else if(sReportType.equalsIgnoreCase("SPF")){%>
        <LI><a href="#" onclick="openSPFFAQ()">FAQ</a></LI>        
        <%}else if(sReportType.equalsIgnoreCase("RBU")){%>  
        <LI><a href="#" onclick="openRBUFAQ()">FAQ</a></LI>
        <%}%>      
        <%-- <LI><a href="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/RbuSearchController.jpf">PSCPT Search</a></LI> --%>
        <LI><a href="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/begin">PSCPT Search</a></LI>
       <LI class=last><a href="#" onclick="openContact()" >Contact</a></LI>
	</UL>
</div>		
		
