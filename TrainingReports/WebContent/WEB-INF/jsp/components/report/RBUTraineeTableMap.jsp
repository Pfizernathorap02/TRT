<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.actionForm.RbutraineetablemapForm"%>
<%@ page import="com.pfizer.db.RBUClassRoom"%>
<%@ page import="com.pfizer.db.RBUClassTable"%>
<%@ page import="com.pfizer.db.RBUEnrollmentException"%>
<%@ page import="com.pfizer.db.RBUTrainee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page
	import="com.pfizer.webapp.wc.components.report.RBUEnrollmentExceptionWc"%>
<%@ page
	import="com.pfizer.webapp.wc.components.report.RBUTraineeTableMapWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>--%>


<!-- <netui:html>  -->
<html>
<head>
<title>Product Training (PSCPT) - Tabel Map</title>
</head>

<LINK href="/TrainingReports/resources/css/trainning.css"
	type="text/css" rel="STYLESHEET">
<body>
	<div style="margin-left: 10px; margin-right: 10px">
		<%
        RBUTraineeTableMapWc wc = (RBUTraineeTableMapWc)request.getAttribute(RBUTraineeTableMapWc.ATTRIBUTE_NAME);         
        
        List trainees = wc.getEmpReport();  
        List guests = wc.getGuestReport();
        List tables = wc.getAvailableTables();  
        List roomlist = wc.getAvailableRooms();  
        RbutraineetablemapForm form = wc.getRbutraineetablemapForm();
        %>

		<div style="margin-left: 10px; margin-right: 10px">
			<div id="table_inst">
				<%--  <a href="<%=AppConst.APP_ROOT%>/RBU/RBUClassRoomReport.do?week_id=<%=form.getWeek_id()%>">Back to Classroom Grid</a> --%>
				<a
					href="<%=AppConst.APP_ROOT%>/RBU/RBUClassRoomReport?week_id=<%=form.getWeek_id()%>">Back
					to Classroom Grid</a>
			</div>
		</div>
		<br> <br>

		<form name="changeassignment" target="classgrid"
			action="<%=AppConst.APP_ROOT%>/RBU/rbutraineedtableupdate"
			method="post">
			<table cellspacing="0" id="employee_table" width="90%"
				class="employee_table" style="margin-left: 10px; margin-right: 10px">

				<tr>
					<td colspan="5"><%=form.getProduct()%> - <%=form.getDay()%> -
						Room <%=form.getRoom()%> - Table <%=form.getTable_id()%>
					</TH>
				</tr>
				<tr></tr>
				<tr>
					<th nowrap>EMPLID</th>
					<th nowrap>First Name</th>
					<th nowrap>Last Name</th>
					<th nowrap>Future Role</th>
					<th nowrap>Move to Table</th>
				</tr>

				<%
            RBUTrainee exc;
            StringBuffer tts = new StringBuffer("");
            for(Iterator i= trainees.iterator(); i.hasNext();){
                exc = (RBUTrainee) i.next();
                tts.append(exc.getEmplId());
                tts.append(",");
         %>
				<tr>
					<td><%=exc.getEmplId()%></td>
					<td>
						<%-- Infosys code changes starts here
            <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getFirstName())%></a>          
            </td>
            <td>
            <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getLastName())%></a>          
            </td>   --%> <a
						href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getFirstName())%></a>
					</td>
					<td><a
						href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getLastName())%></a>
					</td>
					<%-- Infosys code changes ends here --%>
					<td><%=Util.toEmptyNBSP(exc.getRole())%></td>
					<td> <input type="hidden" name="t<%=exc.getEmplId()%>"  id="t<%=exc.getEmplId()%>" value="<%=form.getTable_id()%>">
						Available Tables: <select name="tables<%=exc.getEmplId()%>"
						onchange="t<%=exc.getEmplId()%>.value = tables<%=exc.getEmplId()%>.options[tables<%=exc.getEmplId()%>.selectedIndex].value;">
							<%for(Iterator iter= tables.iterator(); iter.hasNext();){
                RBUClassTable table = (RBUClassTable) iter.next();
                 String tid = table.getTalbe_id();
                 if(tid.equalsIgnoreCase(form.getTable_id())){
            %>
							<option value="<%=tid%>" selected="true">Table
								<%=tid%> (<%=table.getTraineesCnt()%> +
								<%=table.getGuestCnt()%>)
							</option>
							<%
              }else{                
            %>
							<option value="<%=tid%>">Table
								<%=tid%> (<%=table.getTraineesCnt()%> +
								<%=table.getGuestCnt()%>)
							</option>
							<%}
            }%>
					</select></td>

				</tr>
				<%
         }    
         
         
         %>
				<tr>

				</tr>
				<%
            StringBuffer gts = new StringBuffer("");
            for(Iterator i= guests.iterator(); i.hasNext();){
                exc = (RBUTrainee) i.next();
                gts.append(exc.getEmplId());
                gts.append(",");
         %>
				<tr bgcolor="#ffd699">
					<td><%=exc.getEmplId()%> (GT)</td>
					<td>
						<%--  Infosys code changes starts here
           <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getFirstName())%></a>          
            </td>
            <td>
            <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getLastName())%></a>          
            </td> --%> <a
						href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getFirstName())%></a>
					</td>
					<td><a
						href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getLastName())%></a>
					</td>
					<%--  Infosys code changes ends here --%>




					<td><%=Util.toEmptyNBSP(exc.getRole())%></td>
					<td><input type="hidden" name="g<%=exc.getEmplId()%>"
						id="g<%=exc.getEmplId()%>" value="<%=form.getTable_id()%>">
						Available Tables: <select name="tables<%=exc.getEmplId()%>"
						onchange="g<%=exc.getEmplId()%>.value = tables<%=exc.getEmplId()%>.options[tables<%=exc.getEmplId()%>.selectedIndex].value;">
							<%for(Iterator iter= tables.iterator(); iter.hasNext();){
                RBUClassTable table = (RBUClassTable) iter.next();
                 String tid = table.getTalbe_id();     
                 if(tid.equalsIgnoreCase(form.getTable_id())){
            %>
							<option value="<%=tid%>" selected="true">Table
								<%=tid%> (<%=table.getTraineesCnt()%> +
								<%=table.getGuestCnt()%>)
							</option>
							<%
              }else{                
            %>
							<option value="<%=tid%>">Table
								<%=tid%> (<%=table.getTraineesCnt()%> +
								<%=table.getGuestCnt()%>)
							</option>
							<%}
            }%>
					</select><br> <a
						href="<%=AppConst.APP_ROOT%>/RBU/unassigngt?week_id=<%=form.getWeek_id()%>&class_id=<%=form.getClass_id()%>&table_id=<%=form.getTable_id()%>&gid=<%=exc.getEmplId()%>">
							UnAssign Guest Trainer </a></td>

				</tr>
				<%
         }
         %>

				<tr>
					<td colspan="5" align="right">Move table <%=form.getTable_id()%>
						to Room : <select name="rooms"
						onchange="room_id.value = rooms.options[rooms.selectedIndex].value;">
							<%for(Iterator iter= roomlist.iterator(); iter.hasNext();){
            RBUClassRoom room = (RBUClassRoom) iter.next();
                 String sid = room.getRoom_id();     
                 if(sid.equalsIgnoreCase(form.getRoom_id())){
            %>
							<option value="<%=sid%>" selected="true">Room
								<%=room.getRoom_name()%> (<%=room.getAssignedtalbes()%>)
							</option>
							<%
              }else{                
            %>
							<option value="<%=sid%>">Room
								<%=room.getRoom_name()%> (<%=room.getAssignedtalbes()%>)
							</option>
							<%}
            }%>
					</select>
					</td>
				</tr>
				<input type="hidden" name="emplist" id="emplist"
					value="<%=tts.toString()%>">
				<input type="hidden" name="guestlist" id="guestlist"
					value="<%=gts.toString()%>">
				<input type="hidden" name="class_id" id="class_id"
					value="<%=form.getClass_id()%>">
				<input type="hidden" name="room_id" id="room_id"
					value="<%=form.getRoom_id()%>">
				<input type="hidden" name="table_id" id="table_id"
					value="<%=form.getTable_id()%>">
				<input type="hidden" name="week_id" id="week_id"
					value="<%=form.getWeek_id()%>">
				<tr>
					<td colspan="5"><input type="submit" name="submit"
						value="Submit" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
<!-- </netui:html> -->
</html>