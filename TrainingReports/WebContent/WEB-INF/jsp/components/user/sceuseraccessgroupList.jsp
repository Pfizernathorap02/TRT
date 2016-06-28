<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.UserAccess"%>
<%@ page import="com.pfizer.db.UserGroups"%>
<%@ page import="com.pfizer.db.SceEvaluation"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.wc.components.user.PhaseEvaluationWc"%>
<%@ page import="com.pfizer.db.PhaseEvaluation"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.pfizer.webapp.wc.components.user.UserListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.user.SceFormAccessWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	SceFormAccessWc wc = (SceFormAccessWc)request.getAttribute(SceFormAccessWc.ATTRIBUTE_NAME);
	System.out.println(wc.getCurrentSelected());
    AppQueryStrings app=new AppQueryStrings();
    String message= app.getMessage();
    app.setMessage("");
   // PhaseEvaluationWc wc1 = (PhaseEvaluationWc)request.getAttribute(PhaseEvaluationWc.ATTRIBUTE_NAME);
    PhaseEvaluation temp1 = (PhaseEvaluation)wc.getPhaseresult().iterator().next();
%>
<script type="text/javascript" language="JavaScript">

	   /*Action on Reset button*/    
    function resetForm()
    {
        window.location="/TrainingReports/admin/sceformaccess";
    }
     /*Function to get the List of all Checked ID's of SAVE and SUBMIT*/
    function checkChecked()
    {
    //get all SAVE Fields which are checked
	var checkDataSave = document.getElementsByName('saveField');
	var saveData = '(';	
	var saveFlag = 1;
	for( var x=0; x<checkDataSave.length; x++ ) 
	{	
		if (checkDataSave[x].checked) 
		{	
			if(saveFlag!=1)
			{	
				saveData += ',';
			}
			saveFlag++ ;	
			saveData += '\''+ checkDataSave[x].value + '\'';
		}
	}
	saveData += ')';

    //get all SUBMIT Fields which are checked
	var checkDataSubmit = document.getElementsByName('submitField');
	var submitData = '(';	
	var submitFlag = 1;
	for( var y=0; y<checkDataSubmit.length; y++ ) 
	{	
		if (checkDataSubmit[y].checked) 
		{	
			if(submitFlag!=1)
			{	
				submitData += ',';
			}
			submitFlag++ ;	
			submitData += '\''+ checkDataSubmit[y].value + '\'';
		}
	}
	submitData += ')';
    
  window.location="/TrainingReports/admin/getSceEvalAccess?<%=AppQueryStrings.FIELD_SCESAVE%>=" +saveData+"&<%=AppQueryStrings.FIELD_SCESUBMIT%>="+submitData ;
    
    }
    
     /*Get the Seleclted Report Type and display the Access List*/    
    function getPhaseType(id)
    {
        var temp=id.selectedIndex;
        var selected_text = id.options[temp].text;     
        var selected_value = id.options[temp].value;
        //alert(selected_value);
        if(selected_value=="sceform")
        {
        window.location="/TrainingReports/admin/sceformaccess" ;        
        }
        else
        {
        window.location="/TrainingReports/admin/phaseEvaluation?<%=AppQueryStrings.FIELD_REPORTTYPE%>=" +selected_value;
        }
    }
</script>

<br>
<br>
<br>
<br>

<table class="no_space_width" width = "875">
<tr>
     
  
    <FORM class=form_basic><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Report Type</label>
    <select id="reportType" name="reportType" onchange="getPhaseType(this)">
            <%=HtmlBuilder.getOptionsFromLabelValue(wc.getResulttypeList(),temp1.getReportType())%>
	        <%//System.out.println("Sales Org Filter Form value"+tff.getSalesOrg());%>
            <option value="sceform" selected>Representative Feedback Form</option>
    </select>

			<br>
<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<input type="button" value="Apply Changes" onclick="checkChecked()">
			<label>&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<input type="reset" value="Reset" onclick="resetForm()">
			<br>
			<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
			Please note that usergroups having "Submit" privileges can also save the evaluations.
			<br>
              
            <%
                if (message != null && !"".equals(message.trim())) {
                %>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=message%>
                <%
                }
            %>
	<td  width="15"></td>
   	
      <td>   </FORM>
		<table class="blue_table" id="userList" width="700" align="center">
			<tr>
                 <th>User Group</th>
                <th>Save Privilege</th>
				<th>Submit Privilege</th>    		
			</tr>
            
           <%
            
            boolean oddEvenFlag = false;
				for (Iterator it = wc.getResults().iterator(); it.hasNext();) {
					SceEvaluation sceEval = (SceEvaluation)it.next();
					oddEvenFlag = !oddEvenFlag; 
                    		 
			%>
					<tr class="<%=oddEvenFlag?"even":"odd"%>">
                        <td><%=sceEval.getUserGroup()%></td>
			
            
                       <%
	                            if (sceEval.getSave().equalsIgnoreCase("N") && sceEval.getSubmit().equalsIgnoreCase("N")) {
	                            %>
	    						<td align="center"><input name="saveField" value ="<%=sceEval.getUserGroup()%>" type="checkbox"></td>
	                            <% } else {
	                            %> 
	                            <td align="center"><input name="saveField" value ="<%=sceEval.getUserGroup()%>" type="checkbox" checked ></td>
	                            <% 
	                            }
	                            %>
	                            
	                            <%
	                            if (sceEval.getSubmit().equalsIgnoreCase("N")) {
	                            %>
	    						<td align="center"><input name="submitField" value ="<%=sceEval.getUserGroup()%>" type="checkbox"></td>
	                            <% } else {
	                            %> 
	                            <td align="center"><input name="submitField" value ="<%=sceEval.getUserGroup()%>" type="checkbox" checked ></td>
	                            <% 
	                            }
                        %>
						                            
						</tr>
			<%	} 
            %>
		</table>
           
        
	</td>
    
</tr>

</table>
