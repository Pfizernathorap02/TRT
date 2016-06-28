<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderSPFHSWc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%
   HeaderSPFHSWc wc = (HeaderSPFHSWc)request.getAttribute(HeaderSPFHSWc.ATTRIBUTE_NAME);
   User user;
 	UserSession uSession;    
    boolean bDisplayPrintingMenu = true;
    AppQueryStrings qStrings = new AppQueryStrings();
    uSession = UserSession.getUserSession(request);
    bDisplayPrintingMenu = uSession.isAdmin() || uSession.isSuperAdmin();      
%>



<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<div id=header_title>Steere Path Forward TSR Phase-I Reports</div>
	
	<UL id=header_top_nav>                                     
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
       <LI><a href="#" onclick="openSPFFAQ()">FAQ</a></LI>                
       <LI class=last><a href="#" onclick="openContact()" >Contact</a></LI>
	</UL>	    	
</div>		
		