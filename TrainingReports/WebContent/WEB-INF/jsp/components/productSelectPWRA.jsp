<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectPWRAWc"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
	ProductSelectPWRAWc wc = (ProductSelectPWRAWc)request.getAttribute(ProductSelectPWRAWc.ATTRIBUTE_NAME);
	List products = wc.getUser().getProducts();
%>

<table class="no_space_width"> 
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td>
			<br>
			<b>Welcome <%=wc.getUser().getName()%>, </b>
			
			<br><br>
			
			Powers Realignment.  
			
			<br><br>
			
			<b>Please choose a product:</b>
			<table  width="50">
                        <tr>
                
							<%	for (Iterator it = products.iterator(); it.hasNext(); ) {	%>
                                    <%		Product currProd = (Product)it.next();					%>	 
                                
                                    <% if (currProd.getProductCode().equalsIgnoreCase("ARCP")){%>
                                        <td><a href="<%=AppConst.APP_ROOT%>/overview/PWRAcharts?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
                                    <%}%>
                                    <% if (currProd.getProductCode().equalsIgnoreCase("CLBR")){%>
                                        <td><a href="<%=AppConst.APP_ROOT%>/overview/PWRAcharts?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
                                    <%}%>
                                    <% if (currProd.getProductCode().equalsIgnoreCase("GEOD")){%>
                                        <td><a href="<%=AppConst.APP_ROOT%>/overview/PWRAcharts?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
                                    <%}%>
                                    <% if (currProd.getProductCode().equalsIgnoreCase("LYRC")){%>
                                        <td><a href="<%=AppConst.APP_ROOT%>/overview/PWRAcharts?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
                                    <%}%>
                                    <% if (currProd.getProductCode().equalsIgnoreCase("REBF")){%>
                                        <td><a href="<%=AppConst.APP_ROOT%>/overview/PWRAcharts?productCode=<%=currProd.getProductCode()%>"><img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" > </a></td>
                                    <%}%>
							<%	} %>				
                      </tr>      
			</table>
		</td>
	</tr>
</table>