<%@page import="com.pfizer.db.AccessRequest"%>
<%@page import="com.pfizer.webapp.wc.components.user.PendingApprovalsListWc"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.UserAccess"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.wc.components.user.UserListWc"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.Iterator"%>

<%
PendingApprovalsListWc wc = (PendingApprovalsListWc)request.getAttribute(UserListWc.ATTRIBUTE_NAME);
	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++"+wc.getCurrentSelected());
	String message="";
	if(wc.getResults().isEmpty() || wc.getResults().size()==0||wc.getResults()==null)
	{
		 message= "No Pending Approvals";
	}
	else
	{
		 message="";
	}
    
  
    
%>
<script type="text/javascript" language="JavaScript">
	

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
    

</script>

<br>
<br>
<br>
<br>

<table class="no_space_width">
<tr>
	<td  width="15"></td>

	<td>
		
		<div>
		Request User Status : <b>Submitted</b>
		</div>
		
		<br><br>	
		
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
				<th>Email</th>
				<th>NT ID</th>
				<th>NT Domain</th>
				<th>Request Status</th>		
						
			</tr>
			<%	boolean oddEvenFlag = false;
				for (Iterator it = wc.getResults().iterator(); it.hasNext();) {
					AccessRequest _request = (AccessRequest)it.next();
					oddEvenFlag = !oddEvenFlag; 
			%>
					<tr class="<%=oddEvenFlag?"even":"odd"%>">
						<td><%=_request.getLastName()%></td>
						<td><%=_request.getFirstName()%></td>
						<td><%=_request.getEamilID()%></td>
						<td><%=_request.getNtid()%></td>
						<td><%=_request.getNtidDomain()%></td>
						<td><%=_request.getRequestStatus()%></td>
						
					</tr>
			<%	} %>
		</table>
	</td>
</tr>
</table>