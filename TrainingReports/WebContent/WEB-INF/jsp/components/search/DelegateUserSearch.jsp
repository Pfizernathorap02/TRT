<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.DelegateSearchResultListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.DelegateSearchWc"%>

<%@ page import="com.pfizer.webapp.wc.components.search.SearchResultListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	DelegateSearchWc wc = (DelegateSearchWc)request.getAttribute(DelegateSearchWc.ATTRIBUTE_NAME);
    DelegateSearchResultListWc dwc =(DelegateSearchResultListWc) wc.getResultWc();
%>

<table class="basic_table">
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="15"></td>
	</tr>
	<tr>
		<td align="center"><inc:include-wc component="<%=wc.getSearchFormWc()%>"/></td>
	</tr>
    <%if (dwc.getResults() != null) {%>
	<tr>
    
		<td align="center"><inc:include-wc component="<%=wc.getResultWc()%>"/></td>
	</tr>
    <%}%>
</table>