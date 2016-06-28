<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.search.NonAtlasEmployeeSearchForm"%>
<%@ page import="com.pfizer.webapp.wc.components.HomepageWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.NASearchFormWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
System.out.println("In NA Search Form");
	NASearchFormWc wc = (NASearchFormWc)request.getAttribute(NASearchFormWc.ATTRIBUTE_NAME);
    NonAtlasEmployeeSearchForm NAForm = wc.getSearchForm();
%>

<table class="no_space_table">
<tr>
    <td rowspan="3" width="20"></td>
    <td></td>
</tr>
<tr>
    <td>
<table class="no_space_table">
	<form name="searchForm" action="<%=wc.getPostUrl()%>" onsubmit="<%=wc.getOnSubmit()%>" target="<%=wc.getTarget()%>" class="form_basic" method="post">
		<tr>           
			<td align="left" width="150">First Name:</td>
			<td><input class="text" type="text" name="<%=wc.getSearchForm().FIELD_FNAME%>" value="" size="20"></td>
		</tr>
        <tr></tr><tr></tr>
		<tr>
			<td align="left" width="150">Last Name:</td>
			<td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_LNAME%>" value="" size="20"></td>
		</tr>  
        <tr></tr><tr></tr>     
      
        <tr></tr><tr></tr>       
     
       
        <tr></tr><tr></tr>
		<tr>
			<td>
            <input  type="hidden" name="fromSearch" value="true">
			</td>
			<td>
				<input name="" type="image" src="/TrainingReports/resources/images/btn_search2.gif" style="margin-top:20px;">
               
			</td>
		</tr>
        
        		
	</form>
</table>
</td>
	</tr>
</table>