<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.PhaseEvaluation"%>
<%@ page import="com.pfizer.db.UserAccess"%>
<%@ page import="com.pfizer.db.UserGroups"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.wc.components.user.PhaseEvaluationWc"%>
<%@ page import="com.pfizer.webapp.wc.components.user.UserListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.user.GroupListWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	PhaseEvaluationWc wc = (PhaseEvaluationWc)request.getAttribute(PhaseEvaluationWc.ATTRIBUTE_NAME);
    PhaseEvaluation temp = (PhaseEvaluation)wc.getResults().iterator().next();
    System.out.println("Report Type :"+temp.getReportType());
    AppQueryStrings app=new AppQueryStrings();
    String message= app.getMessage();
    app.setMessage("");
%>
<script type="text/javascript" language="JavaScript">
	function searchFunction(selected) {
		window.location=selected.options[selected.selectedIndex].value;
	}
	
	addEvent(window, "load", sortables_init);

	function sortables_init() {
		// Find all tables with class sortable and make them sortable
		if (!document.getElementsByTagName) return;
		tbls = document.getElementsByTagName("table");
		for (ti=0;ti<tbls.length;ti++) {
			thisTbl = tbls[ti];
			if (((' '+thisTbl.id+' ').indexOf("userList") != -1) && (thisTbl.id)) {
				//initTable(thisTbl.id);
				ts_makeSortable(thisTbl);
			}
		}
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
    
    var tempObject = document.getElementById('reportType');
    var temp=tempObject.selectedIndex;
    var selected_text = tempObject.options[temp].text;     
    var selected_value = tempObject.options[temp].value;
    window.location="/TrainingReports/admin/getPhaseReport?<%=AppQueryStrings.FIELD_SAVEACCESS%>=" +saveData+"&<%=AppQueryStrings.FIELD_SUBMITACCESS%>="+submitData+"&<%=AppQueryStrings.FIELD_REPORTTYPE%>=" +selected_value;
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
    
    /*Action on Reset button*/    
    function resetForm()
    {
        var tempObject = document.getElementById('reportType');
        var temp=tempObject.selectedIndex;
        var selected_text = tempObject.options[temp].text;     
        var selected_value = tempObject.options[temp].value;
        window.location="/TrainingReports/admin/phaseEvaluation?<%=AppQueryStrings.FIELD_REPORTTYPE%>=" +selected_value;
    }
    
</script>

<br>
<br>

<table class="no_space_width" width = "850">
<tr>
     
    <FORM class=form_basic><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Report Type</label>
    <select id="reportType" name="reportType"  onchange="getPhaseType(this)">
            <%=HtmlBuilder.getOptionsFromLabelValue(wc.getReportTypeList(),temp.getReportType())%>
	        <%//System.out.println("Sales Org Filter Form value"+tff.getSalesOrg());%>
            <option value="sceform">Representative Feedback Form</option>
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
			Please note that users having "Submit" privileges can also save the evaluations.
			<br>
            </FORM>   
            <%
                if (message != null && !"".equals(message.trim())) {
                %>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=message%>
                <%
                }
            %>
		<table class="blue_table" id="userList" width="700" align="center">
			<tr>
                <th>Role</th>
                <th>Role Description</th>
				<th>Save Privileges</th>
				<th>Submit Privileges</th>     		
			</tr>
			<%	boolean oddEvenFlag = false;
				for (Iterator it = wc.getResults().iterator(); it.hasNext();) {
					PhaseEvaluation phaseEval = (PhaseEvaluation)it.next();
					oddEvenFlag = !oddEvenFlag; 
			%>
					<tr class="<%=oddEvenFlag?"even":"odd"%>">
                        <td><%=phaseEval.getRoleCd()%></td>
						<td><%=phaseEval.getRoleDesc()%></td>
                        <%
                        if (phaseEval.getSave().equalsIgnoreCase("N") && phaseEval.getSubmit().equalsIgnoreCase("N")) {
                        %>
						<td align="center"><input name="saveField" value ="<%=phaseEval.getRoleCd()%>" type="checkbox"></td>
                        <% } else {
                        %> 
                        <td align="center"><input name="saveField" value ="<%=phaseEval.getRoleCd()%>" type="checkbox" checked ></td>
                        <% 
                        }
                        %>
                        
                        <%
                        if (phaseEval.getSubmit().equalsIgnoreCase("N")) {
                        %>
						<td align="center"><input name="submitField" value ="<%=phaseEval.getRoleCd()%>" type="checkbox"></td>
                        <% } else {
                        %> 
                        <td align="center"><input name="submitField" value ="<%=phaseEval.getRoleCd()%>" type="checkbox" checked ></td>
                        <% 
                        }
                        %>              					             					
					</tr>
			<%	} %>
		</table>
</tr>
</table>