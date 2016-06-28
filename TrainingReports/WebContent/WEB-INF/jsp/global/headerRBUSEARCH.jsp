<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.pfizer.webapp.wc.global.HeaderRBUSearchWc"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderSEARCHWc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%
	HeaderRBUSearchWc wc = (HeaderRBUSearchWc)request.getAttribute(HeaderSEARCHWc.ATTRIBUTE_NAME);
    //wc.setShowNav(false);
%>


<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<div id=header_title>Product Training (PSCPT) Search</div>
	
	<UL id=header_top_nav>                                     
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
		<%-- <LI ><a href="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/RbuSearchController.jpf">PSCPT Search</a></LI> --%>                
		<LI ><a href="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/begin">PSCPT Search</a></LI>
		<LI class=last><a href="#" onclick="openContact()" >Contact</a></LI>
	</UL>
	
    	 
	
</div>		
		