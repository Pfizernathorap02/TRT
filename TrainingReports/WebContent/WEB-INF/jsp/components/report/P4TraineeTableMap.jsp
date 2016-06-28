<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.actionForm.P4ClassRoomReportForm"%>
<%@ page import="com.pfizer.actionForm.P4traineetablemapForm"%>
<%@ page import="com.pfizer.actionForm.RbutraineetablemapForm"%>
<%@ page import="com.pfizer.db.P4ClassRoom"%>
<%@ page import="com.pfizer.db.P4ClassTable"%>
<%@ page import="com.pfizer.db.P4Trainee"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collections"%>

<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.P4TraineeTableMapWc"%>


<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>

<%@ page import="java.text.SimpleDateFormat"%>

<head>
        <title>
            Product Training (PSCPT) - Talbe Map
        </title>
    </head>

<LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
    <body>
        <div style="margin-left:10px;margin-right:10px">
        <%
        P4TraineeTableMapWc wc = (P4TraineeTableMapWc)request.getAttribute(P4TraineeTableMapWc.ATTRIBUTE_NAME);         
        
        List trainees = wc.getEmpReport();  
        List guests = wc.getGuestReport();
        List tables = wc.getAvailableTables();  
        List roomlist = wc.getAvailableRooms();  
        P4traineetablemapForm form = wc.getP4traineetablemapForm();
        %>
      
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
                <%-- Infosys code changes starts here
                <a href="<%=AppConst.APP_ROOT%>/P4/P4ClassRoomReport.do?week_id=<%=form.getWeek_id()%>&wave_id=<%=form.getWave_id()%>">Back to Classroom Grid</a>
          --%>
           <a href="<%=AppConst.APP_ROOT%>/P4/P4ClassRoomReport?week_id=<%=form.getWeek_id()%>&wave_id=<%=form.getWave_id()%>">Back to Classroom Grid</a>
          <%-- Infosys code changes ends here --%>
         
            </div>            
        </div>
        <br>
        <br>
        
       <%--  <form name="changeassignment" target = "classgrid" action="<%=AppConst.APP_ROOT%>/P4/p4traineedtableupdate.do" method="post">
       
        --%>
         <form name="changeassignment" target = "classgrid" action="<%=AppConst.APP_ROOT%>/P4/p4traineedtableupdate" method="post">
       
       
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">

        <tr><td colspan="5"><%=form.getProduct()%> - <%=form.getDay()%> - Room <%=form.getRoom()%> - Table <%=form.getTable_id()%></TH> </tr>
        <tr></tr>
        <tr>
            <th nowrap>EMPLID</th>
            <th nowrap>First Name</th>
            <th nowrap>Last Name</th>
            <%if (!Util.isEmpty(form.getTable_id())) {%>
        	<th nowrap>Move to Table</th>
            <%}%>
        </tr>

        <%
            P4Trainee exc;
            StringBuffer tts = new StringBuffer("");
            for(Iterator i= trainees.iterator(); i.hasNext();){
                exc = (P4Trainee) i.next();
                tts.append(exc.getEmplId());
                tts.append(",");
         %>
         <tr>
            <td><%=exc.getEmplId()%></td>            
            <td>
            <%=Util.toEmptyNBSP(exc.getFirstName())%>       
            </td>
            <td>
            <%=Util.toEmptyNBSP(exc.getLastName())%>       
            </td>   
            <%if (!Util.isEmpty(form.getTable_id())) {%>         
            <td>
            <input type="hidden" name="t<%=exc.getEmplId()%>"  id="t<%=exc.getEmplId()%>" value="<%=form.getTable_id()%>">
             Available Tables: <select name="tables<%=exc.getEmplId()%>" onchange="t<%=exc.getEmplId()%>.value = tables<%=exc.getEmplId()%>.options[tables<%=exc.getEmplId()%>.selectedIndex].value;"> 
            <%for(Iterator iter= tables.iterator(); iter.hasNext();){
                P4ClassTable table = (P4ClassTable) iter.next();
                 String tid = table.getTalbe_id();
                 if(tid.equalsIgnoreCase(form.getTable_id())){
            %>
               <option value = "<%=tid%>" selected="true">Table <%=tid%> (<%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%>)</option> 
            <%
              }else{                
            %>
                <option value = "<%=tid%>">Table <%=tid%> (<%=table.getTraineesCnt()%>  + <%=table.getGuestCnt()%>)</option>    
            <%}
            }%>
            </select>
            </td>
            <%}%>
        </tr>
         <%
         }    
         
         
         %>
        <tr>

        </tr>
        <%
            StringBuffer gts = new StringBuffer("");
            for(Iterator i= guests.iterator(); i.hasNext();){
                exc = (P4Trainee) i.next();
                gts.append(exc.getEmplId());
                gts.append(",");
         %>
         <tr bgcolor="#ffd699">
            <td><%=exc.getEmplId()%> (GT)</td>
             <td>
          <%--  Infosys Code chnages starts here 
            <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getFirstName())%></a>          
            </td>
            <td>
            <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getLastName())%></a>          
          --%>
           <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getFirstName())%></a>          
            </td>
            <td>
            <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getLastName())%></a>          
         <%--Infosys code changes ends here --%>
            </td>
            <td><%=Util.toEmptyNBSP(exc.getRole())%></td>
            <td>
            <input type="hidden" name="g<%=exc.getEmplId()%>" id="g<%=exc.getEmplId()%>" value="<%=form.getTable_id()%>">
             Available Tables: <select name="tables<%=exc.getEmplId()%>" onchange="g<%=exc.getEmplId()%>.value = tables<%=exc.getEmplId()%>.options[tables<%=exc.getEmplId()%>.selectedIndex].value;"> 
            <%for(Iterator iter= tables.iterator(); iter.hasNext();){
                P4ClassTable table = (P4ClassTable) iter.next();
                 String tid = table.getTalbe_id();     
                 if(tid.equalsIgnoreCase(form.getTable_id())){
            %>
               <option value = "<%=tid%>" selected="true">Table <%=tid%> (<%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%>)</option> 
            <%
              }else{                
            %>
                <option value = "<%=tid%>">Table <%=tid%> (<%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%>)</option>    
            <%}
            }%>
            </select><br>
             <a href= "<%=AppConst.APP_ROOT%>/RBU/unassigngt?week_id=<%=form.getWeek_id()%>&class_id=<%=form.getClass_id()%>&table_id=<%=form.getTable_id()%>&gid=<%=exc.getEmplId()%>" > UnAssign Guest Trainer </a>
            </td>

        </tr>        
       <%
         }
         %>
         
         <tr>
          <%if (!Util.isEmpty(form.getTable_id())) {%>   
         <td colspan="5" align="right">
         Move table <%=form.getTable_id()%> to Room : <select name="rooms" onchange="room_id.value = rooms.options[rooms.selectedIndex].value;"> 
          <%for(Iterator iter= roomlist.iterator(); iter.hasNext();){
            P4ClassRoom room = (P4ClassRoom) iter.next();
                 String sid = room.getRoom_id();     
                 if(sid.equalsIgnoreCase(form.getRoom_id())){
            %>
               <option value = "<%=sid%>" selected="true">Room <%=room.getRoom_name()%> (<%=room.getAssignedtalbes()%>)</option> 
            <%
              }else{                
            %>
                <option value = "<%=sid%>">Room <%=room.getRoom_name()%> (<%=room.getAssignedtalbes()%>)</option>    
            <%}
            }%>
            </select>
         </td>
         <%}%>
         </tr>
         <input type="hidden" name="emplist" id="emplist" value="<%=tts.toString()%>">
         <input type="hidden" name="guestlist" id="guestlist" value="<%=gts.toString()%>">
         <input type="hidden" name="class_id" id="class_id" value="<%=form.getClass_id()%>">
         <input type="hidden" name="room_id" id="room_id" value="<%=form.getRoom_id()%>">
         <input type="hidden" name="table_id" id="table_id" value="<%=form.getTable_id()%>">
         <input type="hidden" name="week_id" id="week_id" value="<%=form.getWeek_id()%>">
         <input type="hidden" name="wave_id" id="wave_id" value="<%=form.getWave_id()%>">
        <%if (!Util.isEmpty(form.getTable_id())) {%>   

         <tr><td colspan="4"><input type="submit" name ="submit" value="Submit" /></td></tr>
         <%}%>
        </table>
        </form>
        </div>
    </body>
<%-- Infosys code changes starts here    --%>


</html>