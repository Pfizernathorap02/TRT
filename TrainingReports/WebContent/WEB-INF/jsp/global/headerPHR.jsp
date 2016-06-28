<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderPHRWc"%>
<%@ page import="com.pfizer.webapp.wc.global.HeaderPLCWc"%>
<%@ page import = "com.pfizer.webapp.wc.global.HeaderWc" %>
<%
	HeaderPHRWc wc = (HeaderPHRWc)request.getAttribute(HeaderPHRWc.ATTRIBUTE_NAME);
    
%>



<div id=top_header>
	<a href="<%=AppConst.APP_ROOT%>/reportselect" onmouseover="this.style.cursor='hand'"><div id=header_logo></div></a>
	<div id=header_title>POWERS Driving Force PHR Report</div>
	
	<UL id=header_top_nav>                                     
        <%--Infosys code changes starts here 
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect.do">Home</a></LI>
		<LI ><a href="<%=AppConst.APP_ROOT%>/searchPDFHS.do">Search</a></LI> --%>
		<LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
		<LI ><a href="<%=AppConst.APP_ROOT%>/searchPDFHS">Search</a></LI>
		<%--Infosys code changes ends here --%>
        <LI><a href="#" onclick="openSessionAttendance()" >General Session Attendance</a></LI>
        <LI><a href="#" onclick="openPDFFAQ()">FAQ</a></LI>                
		<LI class=last><a href="#" onclick="openContact()" >Contact</a></LI>
	</UL>
	
    	
	<% if (wc.showNav()) {%>
		<div id=header_bottom_nav>
			<table id=header_nav_table>
				<tr>
					<% if ( "productselect".equals( wc.getPageId() ) )  { %>
						<td class="selected">1. Product Summary&nbsp;&nbsp;</td>				
					<% }  else { %>
						<td><a href="<%=AppConst.APP_ROOT%>/PWRA/getFilteredChartPLC">1. Product Summary</a>&nbsp;&nbsp;</td>				
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
		