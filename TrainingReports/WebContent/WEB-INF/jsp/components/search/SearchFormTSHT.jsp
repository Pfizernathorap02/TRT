<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchFormTSHTWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	SearchFormTSHTWc wc = (SearchFormTSHTWc)request.getAttribute(SearchFormTSHTWc.ATTRIBUTE_NAME);
%>

<table class="no_space_width">
	<form name="searchForm" action="<%=AppConst.APP_ROOT%>/TSHTReports/searchTSHT" class="form_basic" method="get">
		<tr>
        <td>
            <label>Emplid:</label></td>
            <td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_EMPLID%>" value="" size="20"></td>


			<td><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;First Name:</label></td>
			<td><input class="text" type="text" name="<%=wc.getSearchForm().FIELD_FNAME%>" value="" size="20"></td>

			<td><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Last Name:</label></td>
			<td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_LNAME%>" value="" size="20"></td>

        <td>
            <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Course Name:&nbsp; </label></td>
            <td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_COURSE_NAME%>" value="" size="40"></td>
			<td>
            &nbsp;&nbsp;<input  type="hidden" name="fromSearch" value="true">
			</td>
			<td>
				<input type="submit" value="Search">	
			</td>
		</tr>		
	</form>
</table>