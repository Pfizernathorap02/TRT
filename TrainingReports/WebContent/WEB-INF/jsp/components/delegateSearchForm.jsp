<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.search.EmplSearchForm"%>
<%@ page import="com.pfizer.webapp.wc.components.HomepageWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.DelegateSearchFormWc"%>

<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	DelegateSearchFormWc wc = (DelegateSearchFormWc)request.getAttribute(DelegateSearchFormWc.ATTRIBUTE_NAME);
    EmplSearchForm emplForm = wc.getSearchForm();
%>
<table class="no_space_table">
<tr>
    <td rowspan="3" width="20"></td>
    <td></td>
</tr>
<br><br>
<tr>
    <td>
<table class="no_space_table">
	<form name="searchForm" action="<%=wc.getPostUrl()%>" onsubmit="<%=wc.getOnSubmit()%>" target="<%=wc.getTarget()%>" class="form_basic" method="post">
		<tr>           
			<td align="left" width="150">First Name:</td>
			<td><input class="text" type="text" name="<%=wc.getSearchForm().FIELD_FNAME%>" value="" size="20" maxlength="30" ></td>
		</tr>
        <tr></tr><tr></tr>
		<tr>
			<td align="left" width="150">Last Name:</td>
			<td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_LNAME%>" value="" size="20" maxlength="30"></td>
		</tr>  
        <tr></tr><tr></tr>     
        <tr>
        <td align="left" width="150">
            Emplid:</td>
            <td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_EMPLID%>" value="" size="20" maxlength="30"></td>
        </tr>
        <tr></tr><tr></tr>       
        <tr>
        <td align="left" width="150">
            Email Address:</td>
            <td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_EMAIL%>" value="" size="20" maxlength="30"></td>
        </tr>
        <tr></tr><tr></tr>
        <tr>
        <td align="left" width="150">
            Business Unit:</td>
            <td>
             <select id="<%=wc.getSearchForm().FIELD_BU%>" name="<%=wc.getSearchForm().FIELD_BU%>" onchange="updateTgixSelect(this,dynamicSelect)">
                            <%=HtmlBuilder.getOptionsFromLabelValue(emplForm.getBuList(),emplForm.getBu())%>
            </select>
            </td>
        </tr>
        <tr></tr><tr></tr>
        <tr>
        <td align="left" width="150">
            Sales Organization:</td>
            <td>
               <select id="<%=wc.getSearchForm().FIELD_SALESORG%>" name="<%=wc.getSearchForm().FIELD_SALESORG%>" onchange="updateTgixSelect(this,dynamicSelect)">
                            <%=HtmlBuilder.getOptionsFromLabelValue(emplForm.getSalOrgList(),emplForm.getSalesorg())%>
                </select>
            </td>
        </tr>
        <tr></tr><tr></tr>
        <tr>
        <td align="left">
            Role:</td>
            <td>
               <select id="<%=wc.getSearchForm().FIELD_ROLE%>" name="<%=wc.getSearchForm().FIELD_ROLE%>" onchange="updateTgixSelect(this,dynamicSelect)">
                            <%=HtmlBuilder.getOptionsFromLabelValue(emplForm.getRoleList(),emplForm.getRole())%>
                </select>
            </td>
        </tr>
        <tr></tr><tr></tr>
		<tr>
			<td>
            <input  type="hidden" name="searchSubmit" value="true">
            <input type="hidden" value="<%=wc.getSource()%>" name="source" >
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