<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.EmployeeSearchRBUWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.EmployeeSearchPDFHSWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchResultListPOAWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	EmployeeSearchRBUWc wc = (EmployeeSearchRBUWc)request.getAttribute(EmployeeSearchRBUWc.ATTRIBUTE_NAME);    
%>

<table class="basic_table">
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="15"></td>
	</tr>
	<tr>
		<td align="center"><inc:include-wc component="<%=wc.getSearchFormRBUWc()%>"/></td>
	</tr>
	<tr>
		<td align="center"><inc:include-wc component="<%=wc.getResultWc()%>"/></td>
	</tr>
</table>