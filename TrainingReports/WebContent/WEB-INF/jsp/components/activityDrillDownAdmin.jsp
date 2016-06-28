<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.ActivityDrillDownAdminWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="com.tgix.printing.LoggerHelper"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
ActivityDrillDownAdminWc wc = (ActivityDrillDownAdminWc) request.getAttribute(ActivityDrillDownAdminWc.ATTRIBUTE_NAME);
%>



<br>
<table class ="blue_table_without_border" align="center">
<tr>
<td align="center">
Please select the group to provide drill down visibility
</td>
</tr>
</table>
<form name="selForm" action= "/TrainingReports/activityDrilldownConfig"   class="form_basic" method="post">
<table class ="blue_table_without_border" align="center" width="100">


   

	
		<tr>
                   
			<td>Groups&nbsp;:&nbsp;&nbsp; </td>
			<td>
            
                    <select name="inputADDGroup" >
                    <%=HtmlBuilder.getOptionsFromLabelValue(wc.getGroups(),wc.getCurrGroup())%>
                    </select>                    
</td>
		</tr>
        
		


</table>
<br>

<table class ="blue_table_without_border" align="center">

<tr>
			<td align="center">
				<input type="hidden" name="type" value="newGroup"/> 
                <input name="Access" value="Provide Access" type="submit"/>               
			</td>
		</tr>
</table>        
</form>

