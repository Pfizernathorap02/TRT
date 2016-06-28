<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.search.EmplSearchForm"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.HomepageWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.AllEmployeeSearchFormWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SimulateSearchFormWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
	AllEmployeeSearchFormWc wc = (AllEmployeeSearchFormWc)request.getAttribute(AllEmployeeSearchFormWc.ATTRIBUTE_NAME);
    EmplSearchForm emplForm = wc.getSearchForm();
%>

<head>
<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
	
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
 <script type="text/javascript" language="JavaScript" src="/TrainingReports/resources/js/sorttable.js"></script>	

</head>
<table class="no_space_table">

<tr>
    <td><br><br></td>
    <td align="right">
            <% 
            System.out.println("emplForm = "+wc.getUserEmplID());
            System.out.println("emplForm = "+wc.getUserEmplID());
            System.out.println("is admin from wc = "+wc.getIsEmplAdmin());
            if (!wc.getIsEmplAdmin()) {%>
           <%--  Infosys migrated code weblogic to jboss changes start here
           <a href="/TrainingReports/newHomePage.do?emplid=<%=wc.getUserEmplID()%>">My Direct Reports</a>&nbsp;&nbsp;&nbsp;
            --%>
            <a href="/TrainingReports/newHomePage?emplid=<%=wc.getUserEmplID()%>">My Direct Reports</a>&nbsp;&nbsp;&nbsp;
           <!-- Infosys migrated code weblogic to jboss changes end here -->
        
            <!-- Infosys migrated code weblogic to jboss changes start here
            <a href="/TrainingReports/reportselect.do">Other Reports</a>&nbsp;&nbsp;&nbsp;-->
            <%}%>
            <!-- <a href="/TrainingReports/reportselect.do">Training Reports</a>&nbsp;&nbsp;&nbsp; -->
            <a href="/TrainingReports/reportselect">Training Reports</a>&nbsp;&nbsp;&nbsp;
            <!-- Infosys migrated code weblogic to jboss changes end here -->
            <% String res = UserSession.getUserSession(request).getIsDelegatedUser();
            boolean sprAdmin = wc.getIsEmplAdmin();
            if (res != null) {
             if(!sprAdmin){%>
             <!-- Infosys migrated code weblogic to jboss changes start here
             <a href="/TrainingReports/switchUser.do">Switch User</a> -->
             <a href="/TrainingReports/switchUser">Switch User</a>
             <!-- Infosys migrated code weblogic to jboss changes end here -->
            <%}}%>&nbsp;&nbsp;&nbsp;    
    </td>
</tr>
<tr>
     <td width="2%">&nbsp;</td>
    <td colspan="3" align="left">
        <div class="breadcrumb">
        <%if (!wc.getIsEmplAdmin()) {%>
        <%-- Infosys migrated code weblogic to jboss changes start here
        <a href="/TrainingReports/p2l/employeeSearchDetailPage.do?emplid=<%=wc.getUserEmplID()%>" style="margin-left:0px;">My Profile</a>/
        <%} else {%>
        <a href="/TrainingReports/reportselect.do" style="margin-left:0px;">Home</a>/
        <%}%> --%>
        <a href="/TrainingReports/p2l/employeeSearchDetailPage?emplid=<%=wc.getUserEmplID()%>" style="margin-left:0px;">My Profile</a>/
        <%} else {%>
        <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a>/
          <!-- Infosys migrated code weblogic to jboss changes end here -->
        <%}%>
            Employee Search 
        </div>
    </td>
</tr>
<tr>
    <td width="2%">&nbsp;</td>
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
        <tr>
        <td align="left" width="150">
            Emplid:</td>
            <td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_EMPLID%>" value="" size="20"></td>
        </tr>
        <tr></tr><tr></tr>       
        <tr>
        <td align="left" width="150">
            Email Address:</td>
            <td><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_EMAIL%>" value="" size="20"></td>
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