<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.search.EmplSearchForm"%>

<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>


<table class="no_space_width">
	<%-- Infosys code changes starts here
	 <form name="searchForm" action="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/searchemployeerbu.do" class="form_basic" method="get">
	 --%>
	 <form name="searchForm" action="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/searchemployeerbu" class="form_basic" method="get">
	<%-- Infosys code changes ends here --%>
	 	<tr>
			<td><label>First Name:</label></td>
			<td><input class="text" type="text" name="<%=EmplSearchForm.FIELD_FNAME%>" value="" size="20"></td>
		</tr>
		<tr>
			<td><label>Last Name:</label></td>
			<td><input  class="text" type="text" name="<%=EmplSearchForm.FIELD_LNAME%>" value="" size="20"></td>
		</tr>
        <tr>
        <td>
        <i>or</i>
        </td>
        </tr>
        <tr> 
        <td>
            <label>Emplid:</label></td>
            <td><input  class="text" type="text" name="<%=EmplSearchForm.FIELD_EMPLID%>" value="" size="20"></td>
        </tr>
         <tr>
        <td>
        <i>or</i>
        </td>
        </tr>
        
        <tr>
        <td>
            <label>Sales Position:&nbsp; </label></td>
            <td><input  class="text" type="text" name="<%=EmplSearchForm.FIELD_TERRITORYID%>" value="" size="20"></td>
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