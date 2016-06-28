<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.UserAccess"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.wc.components.user.UserListWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.Iterator"%>

<%
	UserListWc wc = (UserListWc)request.getAttribute(UserListWc.ATTRIBUTE_NAME);
	System.out.println(wc.getCurrentSelected());
    AppQueryStrings app=new AppQueryStrings();
    String message= app.getMessage();
%>
<script type="text/javascript" language="JavaScript">
	function searchFunction(selected) {
		window.location=selected.options[selected.selectedIndex].value;
	}
	
	function editUser(id) {
		window.location="/TrainingReports/admin/edituser?<%=AppQueryStrings.FIELD_USER_ID%>=" + id;
	}
	function newUser() {
		window.location="/TrainingReports/admin/edituser";
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
    
    function removeUser(id) {
        if (confirm('Are you sure you want to remove this user?')) {
		window.location="/TrainingReports/admin/deleteuser?<%=AppQueryStrings.FIELD_USER_ID%>=" + id;       
        }
    }
	
	
</script>

<br>
<br>
<br>
<br>

<table class="no_space_width">
<tr>
	<td  width="15"></td>

	<td>
		<img style="cursor: hand;" src="/TrainingReports/resources/images/admin/button_add_user.gif" alt="Edit" align="right" onclick="return newUser();"/>
		<form class="form_basic">
			<label>User Status</label>
			<select onchange="searchFunction(this)">
				<option <%="Active".equals(wc.getCurrentSelected())?"selected":""%> value="/TrainingReports/admin/begin?<%=AppQueryStrings.FIELD_USER_ACCESS_STATUS%>=Active">Active</option>
				<option <%="All".equals(wc.getCurrentSelected())?"selected":""%> value="/TrainingReports/admin/begin?<%=AppQueryStrings.FIELD_USER_ACCESS_STATUS%>=All">All</option>
				<option <%="Inactive".equals(wc.getCurrentSelected())?"selected":""%> value="/TrainingReports/admin/begin?<%=AppQueryStrings.FIELD_USER_ACCESS_STATUS%>=Inactive">Inactive</option>
			</select>
		</form>
        <%
                if (message != null && !"".equals(message.trim())) {
                %>
                <%=message%>
                <%
                }
        %>
		<table class="blue_table" id="userList">
			<tr>
				<th>Last Name</th>
				<th>Fist Name</th>
				<th>Emplid</th>
				<th>Email</th>
				<th>NT ID</th>
				<th>NT Domain</th>
				<th>User Type</th>
				<th>Status</th>		
				<th></th>		
			</tr>
			<%	boolean oddEvenFlag = false;
				for (Iterator it = wc.getResults().iterator(); it.hasNext();) {
					UserAccess ua = (UserAccess)it.next();
					oddEvenFlag = !oddEvenFlag; 
			%>
					<tr class="<%=oddEvenFlag?"even":"odd"%>">
						<td><%=ua.getLname()%></td>
						<td><%=ua.getFname()%></td>
						<td><%=ua.getEmplid()%></td>
						<td><%=ua.getEmail()%></td>
						<td><%=ua.getNtId()%></td>
						<td><%=ua.getNtDomain()%></td>
						<td><%=ua.getUserType()%></td>
						<td><%=ua.getStatus()%></td>
						<td> 
							<img style="cursor: hand;" src="/TrainingReports/resources/images/admin/button_edit.gif" alt="Edit" width="40" height="18" onclick="return editUser(<%=ua.getUserId()%>)"/>
                            <img style="cursor: hand;" src="/TrainingReports/resources/images/admin/button_remove.gif" alt="Remove" width="56" height="18" onclick="return removeUser(<%=ua.getUserId()%>)"/>
                        </td>
					</tr>
			<%	} %>
		</table>
	</td>
</tr>
</table>