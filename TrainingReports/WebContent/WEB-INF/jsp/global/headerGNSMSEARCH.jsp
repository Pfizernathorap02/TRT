<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderGNSMSEARCHWc"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderSEARCHWc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%
	HeaderGNSMSEARCHWc wc = (HeaderGNSMSEARCHWc)request.getAttribute(HeaderSEARCHWc.ATTRIBUTE_NAME);
    //wc.setShowNav(false);
%>


<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<div id=header_title>Geodon National Sales Meeting Search</div>
	
	<UL id=header_top_nav>                                     
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
		<LI ><a href="<%=AppConst.APP_ROOT%>/GNSM/searchGNSM">Search</a></LI>                
		<LI class=last><a href="#" onclick="openContact()" >Contact</a></LI>
	</UL>
	
    	 
	<% if (wc.showNav()) {%>
		<div id=header_bottom_nav>
			<table id=header_nav_table>
				<tr> 
					<% if ( "productselect".equals( wc.getPageId() ) )  { %>
						<td class="selected">1. Product Summary&nbsp;&nbsp;</td>				
					<% }  else { %> 
                        <td><a href="<%=AppConst.APP_ROOT%>/GNSM/getFilteredChartGNSM">1. Product Summary</a> &nbsp;&nbsp; </td>				                                                
					<% } %>
					<% if ( "listreport".equals( wc.getPageId() ) )  { %>
						<td class="selected">2. Report Detail&nbsp;&nbsp;</td>
					<% }  else if ("detailreport".equals( wc.getPageId() )) { %>
						<td><a href="javascript:history.go(-1)">2. Search Page&nbsp;&nbsp;</a></td>				
					<% }  else { %>
						<td class="nolink">2. Report Detail&nbsp;&nbsp;</td>
					<% } %>
					<% if ( "EmpDetailReport".equals( wc.getPageId() ) || "EmpDetailReport".equals( wc.getPageId() ))  { %>
						<td class="selected">3. Employee Detail&nbsp;&nbsp;</td>
					<% } else {%>
						<td class="nolink">3. Employee Detail&nbsp;&nbsp;</td>				
					<% } %>
					<td>&nbsp;</td>
				</tr>
			</table>
		</div>
	<% } %>
	
</div>		
		