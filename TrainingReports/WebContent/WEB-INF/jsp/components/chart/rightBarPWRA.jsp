<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.RightBarPWRAWc"%>
<%@ page import="com.pfizer.webapp.user.TerritoryFilterForm"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>


<%
	RightBarPWRAWc wc = (RightBarPWRAWc)request.getAttribute(RightBarPWRAWc.ATTRIBUTE_NAME);
	UserTerritory ut = wc.getUserTerritory();
	List products = wc.getUser().getProducts();
	TerritoryFilterForm form = wc.getUserFilterForm();
%>

<inc:include-wc component="<%=wc.getTerritorySelect()%>"/>


<label class="basic_label"> Product:</label>
<br>

<table class="basic_table">
	
	<%	for (Iterator it = products.iterator(); it.hasNext(); ) {	%>
	<%		Product currProd = (Product)it.next();					%>	 
        <% if (currProd.getProductCode().equalsIgnoreCase("REBF") || currProd.getProductCode().equalsIgnoreCase("ARCP") || currProd.getProductCode().equalsIgnoreCase("CLBR") || currProd.getProductCode().equalsIgnoreCase("GEOD") || currProd.getProductCode().equalsIgnoreCase("LYRC")){%>
			<tr>
				<td>
					<a href="<%=AppConst.APP_ROOT%>/overview/PWRAcharts?productCode=<%=currProd.getProductCode()%>">
				    <% if (wc.getCurrentProduct().equals( currProd.getProductCode() )) { %> 
						<img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo_over.gif" alt="<%=currProd.getProductDesc()%>" >					
					<% } else { %>
						<img src="<%=AppConst.IMAGE_DIR%>/logos/<%=currProd.getProductCode()%>_logo.gif" alt="<%=currProd.getProductDesc()%>" >
					<% } %> 
					</a>
				</td>
			</tr>
        <%}%>
	<%	} %>
	
</table>
