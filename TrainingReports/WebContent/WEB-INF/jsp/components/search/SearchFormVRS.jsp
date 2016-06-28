<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchFormPDFHSWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchFormVRSWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>

<%
	SearchFormVRSWc wc = (SearchFormVRSWc)request.getAttribute(SearchFormVRSWc.ATTRIBUTE_NAME);
%>

<table class="no_space_width">
	<form name="searchForm" action="<%=AppConst.APP_ROOT%>/VRS/searchVRS" class="form_basic" method="get">
		<tr>
			<td><label>First Name:</label></td>
			<td><input class="text" type="text" name="<%=wc.getSearchForm().FIELD_FNAME%>" value="" size="20"></td>
		</tr>
		<tr>
			<td><label>Last Name:</label></td>
			<td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_LNAME%>" value="" size="20"></td>
		</tr>
        <tr>
        <td>
        <i>or</i>
        </td>
        </tr>
        <tr> 
        <td>
            <label>Emplid:</label></td>
            <td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_EMPLID%>" value="" size="20"></td>
        </tr>
         <tr>
        <td>
        <i>or</i>
        </td>
        </tr>
        
        <tr>
        <td>
            <label>Sales Position ID:&nbsp; </label></td>
            <td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_SALESPOSID%>" value="" size="20"></td>
        </tr>
		<tr>
			<td>
            <input  type="hidden" name="fromSearch" value="true">
			</td>
			<td>
				<input type="submit" value="Search">	
			</td>
		</tr>		
	</form>
</table>