<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
	ProductSelectWc wc = (ProductSelectWc)request.getAttribute(ProductSelectWc.ATTRIBUTE_NAME);
	List products = wc.getUser().getProducts();
%>

<table class="no_space_width"> 
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td>
			<br>
			<b>Welcome <%=wc.getUser().getName()%>, </b>
			
			<br><br>
			
			This web-based reporting tool has been designed to provide access to your team's FFT Product Training results.  Results include:  Pedagogue Exams, Attendance, Sales Call Evaluation (SCE), and overall training score.  For colleagues not completing each requirement, coaching recommendations and tools are provided within this website.  
			
			<br><br>
			
			<b>Please choose a product:</b>
			<table  width="50">
				
							<%	for (Iterator it = products.iterator(); it.hasNext(); ) {	%>
							<%		Product currProd = (Product)it.next();					%>	 
						<tr>
							<td><a href="<%=AppConst.APP_ROOT%>/overview/begin?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
							<% if (it.hasNext()) {
								currProd = (Product)it.next();	
							%>
								<td><a href="<%=AppConst.APP_ROOT%>/overview/begin?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
							<% } %>
							<% if (it.hasNext()) {
								currProd = (Product)it.next();	
							%>
								<td><a href="<%=AppConst.APP_ROOT%>/overview/begin?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
							<% } %>
							<% if (it.hasNext()) {
								currProd = (Product)it.next();	
							%>
								<td><a href="<%=AppConst.APP_ROOT%>/overview/begin?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
							<% } %>
							<% if (it.hasNext()) {
								currProd = (Product)it.next();	
							%>
								<td><a href="<%=AppConst.APP_ROOT%>/overview/begin?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
							<% } %>
			
						</tr>
			
							<%	} %>				
			</table>
		</td>
	</tr>
</table>