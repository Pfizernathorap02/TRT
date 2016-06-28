<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.P4ClassData"%>
<%@ page import="com.pfizer.db.P4ClassRoom"%>
<%@ page import="com.pfizer.db.P4ClassTable"%>
<%@ page import="com.pfizer.db.P4RoomGridVO"%>
<%@ page import="com.pfizer.db.P4TrainingWeek"%>
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
<%@ page import="com.pfizer.webapp.wc.components.report.P4ClassRoomReportWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>
 --%>
<%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");  
%>
<!-- <netui:html> -->
<html>
<script>
    self.name = 'classgrid';
    function openLink(url){
      window.open(url,'rr',"left=240, top=170,width=1000,height=700,scrollbars=yes,location=no,status=yes,resizable = yes");
      } 
      
      
</script>

<LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">

    <head>
        <title>
            P4 Training  - Classroom Grid
        </title>
    </head>


    <body>
        <div style="margin-left:10px;margin-right:10px">
        <%
        P4ClassRoomReportWc wc = (P4ClassRoomReportWc)request.getAttribute(P4ClassRoomReportWc.ATTRIBUTE_NAME);    
        
        List weeks = wc.getP4ClassRoomReportForm().getWeeks();
        String week_id = wc.getP4ClassRoomReportForm().getWeek_id();
        String wave_id = wc.getP4ClassRoomReportForm().getWave_id();
         System.out.println("week_id - jsp1 -  " + week_id);
         System.out.println("week_id - jsp1 request.getParameter-  " + request.getParameter("week_id"));
        if (request.getParameter("week_id") != null && !"null".equals(request.getParameter("week_id"))) week_id = request.getParameter("week_id");
        if (request.getParameter("wave_id") != null && !"null".equals(request.getParameter("wave_id"))) wave_id = request.getParameter("wave_id");
        
        //if(week_id == null) week_id = "1";
         System.out.println("week_id - jsp2 -  " + week_id);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        List report = wc.getEmpReport();    

        %>
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
            P4 Training - Classroom Grid Report 
        </h3>        
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
               <%-- Infosys code changes starts here
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/P4/P4ClassRoomReport.do?downloadExcel=true&week_id=<%=week_id%>">Download to Excel</a>
                &nbsp;&nbsp;| <a href="<%=AppConst.APP_ROOT%>/reportselect.do">TRT Home</a> &nbsp;&nbsp;
             --%>
              <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/P4/P4ClassRoomReport?downloadExcel=true&week_id=<%=week_id%>">Download to Excel</a>
                &nbsp;&nbsp;| <a href="<%=AppConst.APP_ROOT%>/reportselect">TRT Home</a> &nbsp;&nbsp;
            <%-- Infosys code changes ends here --%>
             </div>            
        </div>
                    
        <%}%>
         <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="6"><b>P4 -  Classroom Grid</b></td>
        </tr>
        <%} else{ %>
       <%--  Infosys code changes starts here
       <form name="weekselection" action="<%=AppConst.APP_ROOT%>/P4/P4ClassRoomReport.do" method="post">
 --%>
  <form name="weekselection" action="<%=AppConst.APP_ROOT%>/P4/P4ClassRoomReport" method="post">
 <%--Infosys code changes ends here --%>
        <tr><td colspan="6">Wave/Week &nbsp;&nbsp;<select name="waveweek" onchange="waveweek.options[waveweek.selectedIndex].value;weekselection.submit();">
            <option value="0-0%>">Select Wave/Week</option>
        <%for(Iterator i = weeks.iterator(); i.hasNext();){
            P4TrainingWeek week = (P4TrainingWeek) i.next(); 
            if((week.getWeek_id()).equals(week_id) && (week.getWave_id()).equals(wave_id)){
            %>
                <option selected="true"  value="<%=week.getWave_id()%>-<%=week.getWeek_id()%>"><%=week.getWeek_name()%></option>
            <%
            }else{
             %>
                <option value="<%=week.getWave_id()%>-<%=week.getWeek_id()%>"><%=week.getWeek_name()%></option>
            <%
            }               
        %>
            
        <%}%>
        </select>&nbsp; &nbsp; <!-- <A HREF ="<%=AppConst.APP_ROOT%>/P4/rbuUnassignedEmployeesReport?weekId=<%=week_id%>"> Unassigned Tables Report </A>--></td></tr>
        
        </form>    
        <%}%>
        <tr>
            <th nowrap>Rooms </th>
            <th nowrap>Monday</th>
            <th nowrap>Tuesday</th>
            <th nowrap>Wednesday</th>
        	<th nowrap>Thursday</th>
            <th nowrap>Friday</th>
        </tr>

        <%
            P4RoomGridVO exc;
            for(Iterator i= report.iterator(); i.hasNext();){
                exc = (P4RoomGridVO) i.next();
                List roomdata = exc.getRoomdata();
         %>
         <tr>
            <td>Room <%=exc.getRoom_name()%></td>
            <td>
            <%             
                for (Iterator iter = roomdata.iterator(); iter.hasNext();){
                    P4ClassRoom r = (P4ClassRoom)  iter.next();
                  
                    if(P4ClassRoom.MONDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                        <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getP4Classes().iterator(); iClass.hasNext();){                            
                                boolean tableNullFlag = false;
                                int nullTraineecnt = 0;
                                int nullGuestrcnt = 0;
                                P4ClassData c = (P4ClassData)iClass.next();
                            %>
                                <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                                <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                        P4ClassTable table = (P4ClassTable) t.next();
                                        if ("".equals(table.getTalbe_id())) {
                                            nullTraineecnt = nullTraineecnt + table.getTraineesCnt();
                                            nullGuestrcnt = nullGuestrcnt + table.getGuestCnt();
                                            tableNullFlag = true;
                                        } else {
                                       
                                            %>
                                            <tr><td >Table <%=table.getTalbe_id()%></td>
                                            <td >
                                             <%if(!downloadExcel){%>
                                            <%-- Infosys code changes starts here
                                            <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                            --%>
                                           <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                           
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                        }
                                    } 
                                    if (tableNullFlag) {
                                            %>
                                            <tr>
                                            <td colspan="2" align="center">
                                             <%if(!downloadExcel){%>
                                            <%-- <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                            --%>
                                           <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                           
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                    }
                                }%>                          
                 
                        </table>
                        <%
                       break;
                    }
                   
                }
            %>

            </td>
            <td>
           <%             
                for (Iterator iter = roomdata.iterator(); iter.hasNext();){
                    P4ClassRoom r = (P4ClassRoom)  iter.next();
                  
                    if(P4ClassRoom.TUESDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                        <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getP4Classes().iterator(); iClass.hasNext();){                            
                                boolean tableNullFlag = false;
                                int nullTraineecnt = 0;
                                int nullGuestrcnt = 0;
                                P4ClassData c = (P4ClassData)iClass.next();
                            %>
                                <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                                <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                        P4ClassTable table = (P4ClassTable) t.next();
                                        if ("".equals(table.getTalbe_id())) {
                                            nullTraineecnt = nullTraineecnt + table.getTraineesCnt();
                                            nullGuestrcnt = nullGuestrcnt + table.getGuestCnt();
                                            tableNullFlag = true;
                                        } else {
                                       
                                            %>
                                            <tr><td >Table <%=table.getTalbe_id()%></td>
                                            <td >
                                             <%if(!downloadExcel){%>
                                           <%--  <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                             --%>
                                             <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                            
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                        }
                                    } 
                                    if (tableNullFlag) {
                                            %>
                                            <tr>
                                            <td colspan="2" align="center">
                                             <%if(!downloadExcel){%>
                                          <%--   <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                            --%> 
                                            
                                              <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                           
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                    }
                                }%>                          
                 
                        </table>
                        <%
                       break;
                    }
                   
                }
            %>

            </td>
            <td>
           <%             
                for (Iterator iter = roomdata.iterator(); iter.hasNext();){
                    P4ClassRoom r = (P4ClassRoom)  iter.next();
                  
                    if(P4ClassRoom.WENSEDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                        <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getP4Classes().iterator(); iClass.hasNext();){                            
                                boolean tableNullFlag = false;
                                int nullTraineecnt = 0;
                                int nullGuestrcnt = 0;
                                P4ClassData c = (P4ClassData)iClass.next();
                            %>
                                <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                                <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                        P4ClassTable table = (P4ClassTable) t.next();
                                        if ("".equals(table.getTalbe_id())) {
                                            nullTraineecnt = nullTraineecnt + table.getTraineesCnt();
                                            nullGuestrcnt = nullGuestrcnt + table.getGuestCnt();
                                            tableNullFlag = true;
                                        } else {
                                       
                                            %>
                                            <tr><td >Table <%=table.getTalbe_id()%></td>
                                            <td >
                                             <%if(!downloadExcel){%>
                                            <%-- <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                            --%> 
                                            <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                           
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                        }
                                    } 
                                    if (tableNullFlag) {
                                            %>
                                            <tr>
                                            <td colspan="2" align="center">
                                             <%if(!downloadExcel){%>
                                             <%-- <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                              --%>
                                              <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                             
                                             
                                           
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                    }
                                }%>                          
                 
                        </table>
                        <%
                       break;
                    }
                   
                }
            %>
            </td>
            <td>
           <%             
                for (Iterator iter = roomdata.iterator(); iter.hasNext();){
                    P4ClassRoom r = (P4ClassRoom)  iter.next();
                  
                    if(P4ClassRoom.THURSDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                        <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getP4Classes().iterator(); iClass.hasNext();){                            
                                boolean tableNullFlag = false;
                                int nullTraineecnt = 0;
                                int nullGuestrcnt = 0;
                                P4ClassData c = (P4ClassData)iClass.next();
                            %>
                                <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                                <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                        P4ClassTable table = (P4ClassTable) t.next();
                                        if ("".equals(table.getTalbe_id())) {
                                            nullTraineecnt = nullTraineecnt + table.getTraineesCnt();
                                            nullGuestrcnt = nullGuestrcnt + table.getGuestCnt();
                                            tableNullFlag = true;
                                        } else {
                                       
                                            %>
                                            <tr><td >Table <%=table.getTalbe_id()%></td>
                                            <td >
                                             <%if(!downloadExcel){%>
                                            <%-- <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                           --%>
                                           <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                          
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                        }
                                    } 
                                    if (tableNullFlag) {
                                            %>
                                            <tr>
                                            <td colspan="2" align="center">
                                             <%if(!downloadExcel){%>
                                            <%-- <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                            --%>
                                            <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                           
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                    }
                                }%>                          
                 
                        </table>
                        <%
                       break;
                    }
                   
                }
            %>
            </td>
            <td>
           <%             
                for (Iterator iter = roomdata.iterator(); iter.hasNext();){
                    P4ClassRoom r = (P4ClassRoom)  iter.next();
                  
                    if(P4ClassRoom.FRIDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                        <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getP4Classes().iterator(); iClass.hasNext();){                            
                                boolean tableNullFlag = false;
                                int nullTraineecnt = 0;
                                int nullGuestrcnt = 0;
                                P4ClassData c = (P4ClassData)iClass.next();
                            %>
                                <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                                <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                        P4ClassTable table = (P4ClassTable) t.next();
                                        if ("".equals(table.getTalbe_id())) {
                                            nullTraineecnt = nullTraineecnt + table.getTraineesCnt();
                                            nullGuestrcnt = nullGuestrcnt + table.getGuestCnt();
                                            tableNullFlag = true;
                                        } else {
                                       
                                            %>
                                            <tr><td >Table <%=table.getTalbe_id()%></td>
                                            <td >
                                             <%if(!downloadExcel){%>
                                            <%-- <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                             --%>
                                             <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                            
                                            
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                        }
                                    } 
                                    if (tableNullFlag) {
                                            %>
                                            <tr>
                                            <td colspan="2" align="center">
                                             <%if(!downloadExcel){%>
                                           <%--  <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                           --%>
                                            <a href="<%=AppConst.APP_ROOT%>/P4/p4traineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>&wave_id=<%=wave_id%>" >
                                          
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            </a>
                                            <%}else{
                                            %>
                                            <%=nullTraineecnt%> + <%=nullGuestrcnt%> 
                                            <%}%>
                                            </td></tr> 
                                            <%
                                    }
                                }%>                          
                 
                        </table>
                        <%
                       break;
                    }
                   
                }
            %>
            </td>
        </tr>
         <%
         }
         %>

        </table>
        </div>
    </body>
<!-- </netui:html> -->
</html>