<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchFormPDFHSWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	SearchFormPDFHSWc wc = (SearchFormPDFHSWc)request.getAttribute(SearchFormPDFHSWc.ATTRIBUTE_NAME);
    String mode1 = request.getParameter("m1");  
    String mode2 = request.getParameter("m2");  
%>

<table class="no_space_width">
	<form name="searchForm" action="<%=AppConst.APP_ROOT%>/searchPDFHS" class="form_basic" method="get">
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
            <input  type="hidden" name="m1" value="<%=mode1%>">            
            <input  type="hidden" name="m2" value="<%=mode2%>">            
			</td>
			<td>
				<input type="submit" value="Search">	
			</td>
		</tr>		
	</form>
</table>