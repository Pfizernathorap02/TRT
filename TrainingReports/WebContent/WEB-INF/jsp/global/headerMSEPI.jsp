<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderGNSMWc"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderMSEPIWc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%
	HeaderMSEPIWc wc = (HeaderMSEPIWc)request.getAttribute(HeaderMSEPIWc.ATTRIBUTE_NAME);    
%>

<div id=top_header>
	<!-- Infosys - Weblogic to Jboss Migrations changes start here -->
	<%-- <a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a> --%>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<!-- Infosys - Weblogic to Jboss Migrations changes end here -->
    <div id=header_title>MS/Epi National Sales Meeting</div>
	
	<UL id=header_top_nav>
	<!-- Infosys - Weblogic to Jboss Migrations changes start here -->                                     
        <%-- <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI> --%>
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
        <LI ><a href="<%=AppConst.APP_ROOT%>/MSEPI/searchMSEPI">Search</a></LI>
		<!-- Infosys - Weblogic to Jboss Migrations changes end here -->                        
		<LI class=last><a href="#" onclick="openContact()" >Contact</a></LI>
	</UL>
	 
    	
	<% if (wc.showNav()) {%>
		<div id=header_bottom_nav>
			<table id=header_nav_table>
				<tr>
					<% if ( "productselect".equals( wc.getPageId() ) )  { %>
						<td class="selected">1. Product Summary&nbsp;&nbsp;</td>				
					<% }  else { %>
					<!-- Infosys - Weblogic to Jboss Migrations changes start here -->
						<td><a href="<%=AppConst.APP_ROOT%>/MSEPI/getFilteredChartMSEPI">1. Product Summary</a>&nbsp;&nbsp;</td>
						<!-- Infosys - Weblogic to Jboss Migrations changes end here -->				
					<% } %>
					<% if ( "listreport".equals( wc.getPageId() ) )  { %>
						<td class="selected">2. Report Detail&nbsp;&nbsp;</td>
					<% }  else if ("detailreport".equals( wc.getPageId() )) { %>
						<td><a href="javascript:history.go(-1)">2. Report Detail&nbsp;&nbsp;</a></td>				
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
		