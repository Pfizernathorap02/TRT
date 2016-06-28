<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.UserAccess"%>
<%@ page import="com.pfizer.db.UserGroups"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.wc.components.user.UserListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.user.GroupListWc"%>
<%@ page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	GroupListWc wc = (GroupListWc)request.getAttribute(GroupListWc.ATTRIBUTE_NAME);
	System.out.println("Current Selected is"+wc.getCurrentSelected());
    AppQueryStrings app=new AppQueryStrings();
    String message= app.getMessage();
%>
<script type="text/javascript" language="JavaScript">
	function searchFunction(selected) {
		window.location=selected.options[selected.selectedIndex].value;
	}
	
	function editGroup(id) {
		window.location="/TrainingReports/admin/editgroup?<%=AppQueryStrings.FIELD_USER_ID%>=" + id;
	}
	function newGroup() {
		window.location="/TrainingReports/admin/editgroup";
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
    
    function removeGroup(id) {
        if (confirm('Are you sure you want to remove this group?')) {
		window.location="/TrainingReports/admin/deletegroup.action?<%=AppQueryStrings.FIELD_USER_ID%>=" + id;       
        }
    }
	
	
</script>

<br>
<br>
<br>
<br>

<table class="no_space_width" width = "875">
<tr>
	<td  width="15"></td>
   	<img style="cursor: hand;" src="/TrainingReports/resources/images/admin/button_add_group.gif" alt="Edit" align="right" onclick="return newGroup();"/>		      
        <%
                if (message != null && !"".equals(message.trim())) {
                %>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=message%>
                <%
                }
        %>
        <br>
        <br>
      <td>  
		<table class="blue_table" id="userList" width="850" align="right">
			<tr>
                <th>User Group</th>
                <th>Business Unit(s)</th>
				<th>Sales Organization(s)</th>
				<th>Role(s)</th>
                <th>Feedback User(s)</th>
                <th>HQ User(s)</th>       
				<th></th>		
			</tr>
			<%	boolean oddEvenFlag = false;
				for (Iterator it = wc.getResults().iterator(); it.hasNext();) {
					UserGroups ua = (UserGroups)it.next();
					oddEvenFlag = !oddEvenFlag; 
			%>
					<tr class="<%=oddEvenFlag?"even":"odd"%>">
                        <td><%=ua.getGroupName()%></td>
						<td><%=ua.getBusUnit()%></td>
						<td><%=ua.getSalesOrg()%></td>
						<td><%=ua.getRole()%></td>
                        <%
                        if(ua.getSelectedFBU()==null)
                        {
                        %>
                        <td>None</td>
                        <%}
                        else
                        {%>
                        <td><%=ua.getSelectedFBU()%></td>
                        <%}%>
                             <%
                        if(ua.getSelectedHQU()==null)
                        {
                        %>
                        <td>None</td>
                        <%}
                        else
                        {%>
                        <td><%=ua.getSelectedHQU()%></td>
                        <%}%>                 					
						<td> 
							<img style="cursor: hand;" src="/TrainingReports/resources/images/admin/button_edit.gif" alt="Edit" width="40" height="18" onclick="return editGroup(<%=ua.getGroupId()%>)"/>                      
                            <img style="cursor: hand;" src="/TrainingReports/resources/images/admin/button_remove.gif" alt="Remove" width="56" height="18" onclick="return removeGroup(<%=ua.getGroupId()%>)"/>
                        </td>
					</tr>
			<%	} %>
		</table>
	</td>
</tr>
</table>