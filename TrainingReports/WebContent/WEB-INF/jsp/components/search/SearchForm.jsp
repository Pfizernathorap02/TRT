<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
    <!-- Start: Modified for TRT 3.6 enhancement - F 4.1-(additional search fields) -->
<%@ page import="com.pfizer.webapp.search.EmplSearchForm"%>
<%@ page import="com.pfizer.webapp.wc.components.HomepageWc"%>
    <!-- End: Modified for TRT 3.6 enhancement - F 4.1-(additional search fields) -->
<%@ page import="com.pfizer.webapp.wc.components.search.SearchFormWc"%>
    <!-- Start: Modified for TRT 3.6 enhancement - F 4.1-(additional search fields) -->
<%@ page import="com.pfizer.webapp.wc.components.search.SimulateSearchFormWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
    <!-- End: Modified for TRT 3.6 enhancement - F 4.1-(additional search fields) -->
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	SearchFormWc wc = (SearchFormWc)request.getAttribute(SearchFormWc.ATTRIBUTE_NAME);
    
    // Start: Modified for TRT 3.6 enhancement - F 4.1-(additional search fields)
    EmplSearchForm emplForm = wc.getSearchForm();
    
    // End: Modified for TRT 3.6 enhancement - F 4.1-(additional search fields)
%>

<script type="text/javascript" language="javascript"> 

var dynamicSelect;
dynamicSelect = new tgixSelect('<%=wc.getSearchForm().FIELD_ROLE%>');

<!-- 
var myW;
function DoThis12() { 
if (myW != null)
  {
    
    if (!myW.closed) {
      myW.focus();
    } 
    
  }

window.name = "main";
myW = window.open("","myW","height=400,width=500,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes") 
} 
--> 
</script>

<!--<table class="no_space_width" chartscontrol_area>-->
<table class="no_space_width" width="100%">
	<form name="searchForm" action="<%=wc.getPostUrl()%>" onsubmit="<%=wc.getOnSubmit()%>" target="<%=wc.getTarget()%>" class="form_basic" method="post">
        <TABLE style="FONT-SIZE: 12px">
            <tr style="height:3px"><td></td></tr>
        <tr>
			<td valign="bottom" colspan="2"><h5 align="center">Employee Search</h5></td>
            </tr>
        <tr>
			<td valign="bottom" nowrap>First Name:</td>
			<td valign="top"><input class="text" type="text" name="<%=wc.getSearchForm().FIELD_FNAME%>" value="" size="20"></td>
		</tr>
		<tr>
			<td valign="bottom" nowrap>Last Name:</td>
			<td valign="top"><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_LNAME%>" value="" size="20"></td>
		</tr>
        <tr>
        <td valign="bottom">
            Emplid:</td>
            <td valign="top"><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_EMPLID%>" value="" size="20"></td>
        </tr>
        <tr>
        <td valign="bottom" nowrap>
            Sales Position ID:&nbsp;</td>
            <td valign="top"><input  class="text" type="text" name="<%=wc.getSearchForm().FIELD_SALESPOSID%>" value="" size="20"></td>
        </tr>
       
       <!-- Start: Modified for TRT 3.6 enhancement - F4.1 (Adding new search attributes) -->
        <tr >
        <td align="left" valign="baseline">
           Role:</td>
            <td valign="top">
               <select style="height:12; width:165; max-height:12" id="<%=wc.getSearchForm().FIELD_ROLE%>" name="<%=wc.getSearchForm().FIELD_ROLE%>" onchange="updateTgixSelect(this,dynamicSelect)">
                           <%=HtmlBuilder.getOptionsFromLabelValue(emplForm.getRoleList(),emplForm.getRole())%>
                </select>
            </td>
        </tr>
        
        <tr>
        
        <td align="left" valign="baseline" nowrap>
            Business Unit:</td>
            <td valign="top">
             <select style="height:12; width:165; max-height:12" id="<%=wc.getSearchForm().FIELD_BU%>" name="<%=wc.getSearchForm().FIELD_BU%>" onchange="updateTgixSelect(this,dynamicSelect)">
                            <%=HtmlBuilder.getOptionsFromLabelValue(emplForm.getBuList(),emplForm.getBu())%>
            </select>
            </td>
        </tr>
        
        <!-- End: Modified for TRT 3.6 enhancement - F4.1 (Adding new search attributes) -->
        
        
        <tr>
			<td>
            <input  type="hidden" name="fromSearch" value="true">
			</td>
			<td>
				<input name="" type="image" src="/TrainingReports/resources/images/btn_search2.gif" style="margin-top:20px;">
			</td>
		</tr>	
        </TABLE>        	
	</form>
</table>