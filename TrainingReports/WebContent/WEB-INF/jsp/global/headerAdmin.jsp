<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderAdminWc"%>
<%
	HeaderAdminWc wc = (HeaderAdminWc)request.getAttribute(HeaderAdminWc.ATTRIBUTE_NAME);
    String mode1 = request.getParameter("m1");    
    String mode2 = request.getParameter("m2");    
    //wc.setShowNav(false);
%>


<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<div id=header_title><%=(mode1.equalsIgnoreCase("PDF")?"PDF":"SPF")%> Admin Reports</div>
	
	<UL id=header_top_nav>                                     
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>		        
        <%if(mode1.equalsIgnoreCase("PDF")){%>
		<LI><a href="#" onclick="openPDFFAQ()">FAQ</a></LI>
        <%}else{%>
        <LI><a href="#" onclick="openSPFFAQ()">FAQ</a></LI>        
        <%}%>        
		<LI class=last><a href="#" onclick="openContact()" >Contact</a></LI>
	</UL>
	
    	 
	<% if (wc.showNav()) {%>
		<div id=header_bottom_nav>
			<table id=header_nav_table>
				<tr> 
					<%if ("detailreport".equals( wc.getPageId() )) { %>
						<td><a href="javascript:history.go(-1)">1. Report&nbsp;&nbsp;</a></td>				
					<% }  else { %>
						<td class="nolink">1. Report&nbsp;&nbsp;</td>
					<% } %>
					<% if ( "EmpDetailReport".equals( wc.getPageId() ) || "EmpDetailReport".equals( wc.getPageId() ))  { %>
						<td class="selected">2. Employee Detail&nbsp;&nbsp;</td>
					<% } else {%>
						<td class="nolink">2. Employee Detail&nbsp;&nbsp;</td>				
					<% } %>
					<td>&nbsp;</td>
				</tr>
			</table>
		</div>
	<% } %>
	
</div>		
		