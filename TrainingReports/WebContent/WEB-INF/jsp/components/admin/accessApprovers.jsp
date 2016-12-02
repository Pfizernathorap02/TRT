<%@page import="com.pfizer.db.AccessApproversMembers"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.UserAccess"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.*"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc"%>
<%@ page import="java.util.Iterator"%>

<%
	AccessApproversWc wc = (AccessApproversWc) request
			.getAttribute(AccessApproversWc.ATTRIBUTE_NAME);
	AccessApproversMembers appOwner = wc.getAppOwner();
	AccessApproversMembers businessOwner1 = wc.getBusinessOwner1();
	AccessApproversMembers businessOwner2 = wc.getBusinessOwner2();
	
	
	String message = new String();
	
	if( wc.getMessage()!= null)
	{
		message = wc.getMessage().toString();
	}
	
%>
<script type="text/javascript" language="JavaScript">
	function updateApprover(approverType) 
	{
		document.getElementById("approverToUpdate").value = approverType;
		document.forms[0].submit();
	}
	
</script>
<br>
<br>
<br>
<br>
<table class="no_space_width">
	<tr>
		<td>
			<form action="updateApprovers" method="post">
				<input type="hidden" value="" name="approverToUpdate" id="approverToUpdate">
				<%
					if(message != null){
				%>
					<label style="color: green; font-size: 15px;margin-left: 10%"><%=message %></label>
				<%} %>
				<table style="margin: 10%; width: 100%;" class="accessApproversTable"> 
					<tr>
						<td>Application owner :</td>
						<td><input type="text" class="text" size="30" value='<%=appOwner.getEmailId()%>' name='<%=appOwner.getApproverType()%>'id='<%=appOwner.getApproverType()%>' /></td>
						<td></td>
						<td><input type="submit" value="Update" onclick="updateApprover('<%=appOwner.getApproverType()%>')"/></td>
					</tr>
					<tr>
						<td colspan="4"><imgsrc="/TrainingReports/resources/images/spacer.gif"height="5"></td>
					</tr>
					<tr>
						<td>Primary business owner:</td>
						<td><input type="text" class="text" size="30" value='<%=businessOwner1.getEmailId()%>' name='<%=businessOwner1.getApproverType()%>'id='<%=businessOwner1.getApproverType()%>' /></td>
						<td></td>
						<td><input type="submit" value="Update" onclick="updateApprover('<%=businessOwner1.getApproverType()%>')"/></td>
					</tr>
					<tr>
						<td colspan="4"><imgsrc="/TrainingReports/resources/images/spacer.gif"height="5"></td>
					</tr>
					<tr>
						<td>Secondary business owner:</td>
						<td><input type="text" class="text" size="30" value='<%=businessOwner2.getEmailId()%>' name='<%=businessOwner2.getApproverType()%>'id='<%=businessOwner2.getApproverType()%>' /></td>
						<td></td>
						<td><input type="submit" value="Update" onclick="updateApprover('<%=businessOwner2.getApproverType()%>')"/></td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
</table>