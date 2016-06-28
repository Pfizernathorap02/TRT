
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.EmployeeGridConfigAdminWc"%>
<%@ page import="com.tgix.printing.EmployeeGridOptFieldsBean"%>

<!-- Added file for TRT 3.6 Enhancement F4.4 - admin configuration of employee grid -->
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
    EmployeeGridConfigAdminWc wc = (EmployeeGridConfigAdminWc) request.getAttribute(EmployeeGridConfigAdminWc.ATTRIBUTE_NAME); 
%>

<script type="text/javascript">

function concatSelectedFields()
{

var form = document.selOptionalFieldsForm;
var strResult = " ";
        
    for(i=0; i<form.chkBox.length; i++)
    {
      if(form.chkBox[i].checked){
         if(strResult == " "){
            strResult=form.chkBox[i].value;}
         else{
            
            strResult = strResult + "," + form.chkBox[i].value ;
          
            }
       }  
    }
  
    form.newSet.value = strResult;
    
    form.submit();
}

</script>

<table class="no_space_table">
<tr>
    <td rowspan="3" width="20"></td>
    <td></td>
</tr>
<tr><td>
    <table class="blue_table_without_border">
        <tr><td><b>Instructions</b></td></tr>
        <tr><td>1. Please select the optional fields in the table to configure the Employee Grid.</td></tr>
        <tr><td>2. Click on 'Save' to save the configuration.</td></tr> 
    </table> 
</td></tr>

<tr><td>

    <table class="no_space_table">
        
        <form name="selOptionalFieldsForm" action="<%=wc.getPostUrl()%>" class="form_basic" method="post" target="<%=wc.getTarget()%>" onsubmit="<%=wc.getOnSubmit()%>">
        
        <tr> 
        <td align="center"><BR><BR><BR><BR>
        
        <table class="blue_table" id="userList" width="350" align="center">
        	<tr>
                <th>Optional Fields</th>
                <th>Save Fields</th>
			</tr>
       
        <% boolean oddEvenFlag = false;
         for(int i=0;i<wc.getOptFields().size();i++){
            
            EmployeeGridOptFieldsBean inst = (EmployeeGridOptFieldsBean) wc.getOptFields().get(i); 
            oddEvenFlag = !oddEvenFlag;
        
        %>
          <tr class="<%=oddEvenFlag?"even":"odd"%>">
          
          <td align="center"><%=inst.getFieldName()%><BR></td>
          
          <td align="center"><input type="checkbox" name="chkBox" value="<%=inst.getDBColumnName()%>"
             <% if(wc.getSelectedOptFields() != null){
                    if(inst.inList(wc.getSelectedOptFields())){%>
                    checked=true  <% System.out.println(wc.getSelectedOptFields());} 
             }%>
         /></td>
          
       </tr>
        
        
         <% System.out.println(inst.getFieldName());
         System.out.println("+++++++");
          }%>
         </table>
         
        <BR><BR>
        <input type="button" value="Save" onclick="javascript:concatSelectedFields();">
        <BR><BR>
         
        </td>
    </tr>
        <tr>
			<td rowspan=2>
				<input type="hidden" name="type" value="newInput"/> 
                <input type="hidden" name="newSet" value=""/> 
  
			</td>
		</tr>

        </form> 
        
    </table>

</table>
