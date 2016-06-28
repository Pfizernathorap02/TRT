<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.actionForm.RBUClassRoomReportForm"%>
<%-- <%@ page import="RBU.RBUController.RBUClassRoomReportForm"%> --%>
<%@ page import="com.pfizer.db.RBUClassData"%>
<%@ page import="com.pfizer.db.RBUClassRoom"%>
<%@ page import="com.pfizer.db.RBUClassTable"%>
<%@ page import="com.pfizer.db.RBURoomGridVO"%>
<%@ page import="com.pfizer.db.RBUTrainingWeek"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.RBUClassRoomReportWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

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
            Product Training (PSCPT) - Classroom Grid
        </title>
    </head>


    <body>
        <div style="margin-left:10px;margin-right:10px">
        <%
        RBUClassRoomReportWc wc = (RBUClassRoomReportWc)request.getAttribute(RBUClassRoomReportWc.ATTRIBUTE_NAME);    
        
        List weeks = wc.getRBUClassRoomReportForm().getWeeks();
        String week_id = wc.getRBUClassRoomReportForm().getWeek_id();
        if (request.getParameter("week_id") != null ) week_id = request.getParameter("week_id");
        
        //if(week_id == null) week_id = "1";
         System.out.println("week_id - jsp -  " + week_id);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        List report = wc.getEmpReport();    

        %>
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
            Product Training (PSCPT) - Class Room Report 
        </h3>        
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
                
              <%-- Infosys code changes starts here
              <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUClassRoomReport.do?downloadExcel=true&week_id=<%=week_id%>">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/rbusreportselect.do">PSCPT Admin Reports</a> --%>
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUClassRoomReport?downloadExcel=true&week_id=<%=week_id%>">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/rbusreportselect">PSCPT Admin Reports</a>
                 <%-- Infosys code changes starts here --%>
            </div>            
        </div>
                    
        <%}%>
         <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="6"><b>RBU -  Classroom Grid</b></td>
        </tr>
        <%} else{%>
      <%--   <form name="weekselection" action="<%=AppConst.APP_ROOT%>/RBU/RBUClassRoomReport.do" method="post"> --%>
        <form name="weekselection" action="<%=AppConst.APP_ROOT%>/RBU/RBUClassRoomReport" method="post">
        <input type="hidden" name="week_id" value="<%=week_id%>">
        <tr><td colspan="6">Week <select name="week" onchange="week_id.value = week.options[week.selectedIndex].value;weekselection.submit();">
        <%for(Iterator i = weeks.iterator(); i.hasNext();){
            RBUTrainingWeek week = (RBUTrainingWeek) i.next(); 
            if((week.getWeek_id()).equals(week_id)){
            %>
                <option selected="true"  value="<%=week.getWeek_id()%>"><%=week.getWeek_name()%></option>
            <%
            }else{
             %>
                <option value="<%=week.getWeek_id()%>"><%=week.getWeek_name()%></option>
            <%
            }               
        %>
            
        <%}%>
        </select>&nbsp; &nbsp; <!-- <A HREF ="<%=AppConst.APP_ROOT%>/RBU/rbuUnassignedEmployeesReport?weekId=<%=week_id%>"> Unassigned Tables Report </A>--></td></tr>
        
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
            RBURoomGridVO exc;
            for(Iterator i= report.iterator(); i.hasNext();){
                exc = (RBURoomGridVO) i.next();
                List roomdata = exc.getRoomdata();
         %>
         <tr>
            <td>Room <%=exc.getRoom_name()%></td>
            <td>
            <%             
                for (Iterator iter = roomdata.iterator(); iter.hasNext();){
                    RBUClassRoom r = (RBUClassRoom)  iter.next();
                  
                    if(RBUClassRoom.MONDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                        <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getRbuClasses().iterator(); iClass.hasNext();){                            
                                RBUClassData c = (RBUClassData)iClass.next();
                            %>
                            <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                            <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                   RBUClassTable table = (RBUClassTable) t.next();
                                %>
                                <tr><td >Table <%=table.getTalbe_id()%></td>
                                <td >
                                 <%if(!downloadExcel){%>
                              <%--  Infosys code changes starts here
                               <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                               --%>  
                               
                                 <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                              <%--  Infosys code changes ends here --%>
                               <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                </a>
                                <%}else{
                                %>
                                <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                <%}%>
                                </td></tr> 
                                <%
                                }}%>                          
                 
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
                    RBUClassRoom r = (RBUClassRoom)  iter.next();
                  
                    if(RBUClassRoom.TUESDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                        <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getRbuClasses().iterator(); iClass.hasNext();){                            
                                RBUClassData c = (RBUClassData)iClass.next();
                            %>
                            <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                            <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                   RBUClassTable table = (RBUClassTable) t.next();
                                %>
                                <tr><td >Table <%=table.getTalbe_id()%></td>
                                <td >
                                 <%if(!downloadExcel){%>
                                <%-- Infosys code changes starts here
                                 <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                               
                                --%>
                                <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                               
                                <%--  Infosys code changes ends here --%>
                               
                                <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                </a>
                                <%}else{
                                %>
                                <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                <%}%>
                                </td></tr> 
                                <%
                                }}%>                          
                 
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
                    RBUClassRoom r = (RBUClassRoom)  iter.next();
                  
                    if(RBUClassRoom.WENSEDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                        <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getRbuClasses().iterator(); iClass.hasNext();){                            
                                RBUClassData c = (RBUClassData)iClass.next();
                            %>
                            <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                            <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                   RBUClassTable table = (RBUClassTable) t.next();
                                %>
                                <tr><td >Table <%=table.getTalbe_id()%></td>
                                <td >
                                 <%if(!downloadExcel){%>
                               <%-- Infosys code changes starts here
                                <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                                --%> 
                                 <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                               
                                 <%--  Infosys code changes ends here --%>
                                <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                </a>
                                <%}else{
                                %>
                                <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                <%}%>
                                </td></tr> 
                                <%
                                }}%>                          
                 
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
                    RBUClassRoom r = (RBUClassRoom)  iter.next();
                  
                    if(RBUClassRoom.THURSDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                        <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getRbuClasses().iterator(); iClass.hasNext();){                            
                                RBUClassData c = (RBUClassData)iClass.next();
                            %>
                            <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                            <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                   RBUClassTable table = (RBUClassTable) t.next();
                                %>
                                <tr><td >Table <%=table.getTalbe_id()%></td>
                                <td >
                                 <%if(!downloadExcel){%>
                                <%-- Infosys code changes starts here
                                 <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                                --%>
                                <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                               
                                 <%--  Infosys code changes ends here --%>
                                <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                </a>
                                <%}else{
                                %>
                                <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                <%}%>
                                </td></tr> 
                                <%
                                }}%>                          
                 
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
                    RBUClassRoom r = (RBUClassRoom)  iter.next();
                  
                    if(RBUClassRoom.FRIDAY.equalsIgnoreCase(r.getWeekday())){
                        %>
                         <table border = "1" class="no_space_width_table" STYLE="border-style: none" width="100%">
                            
                            <% for (Iterator iClass = r.getRbuClasses().iterator(); iClass.hasNext();){                            
                                RBUClassData c = (RBUClassData)iClass.next();
                            %>
                            <tr><td colspan="2"><%=c.getProductdesc()%></td></tr>
                            <% for (Iterator t = c.getTables().iterator(); t.hasNext();){
                                   RBUClassTable table = (RBUClassTable) t.next();
                                %>
                                <tr><td >Table <%=table.getTalbe_id()%></td>
                                <td >
                                 <%if(!downloadExcel){%>
                                <%-- Infosys code changes starts here
                                 <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap.do?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                                 --%>
                                <a href="<%=AppConst.APP_ROOT%>/RBU/rbutraineetablemap?day=<%=Util.formatDateShort(r.getDay())%>&product=<%=c.getProductdesc()%>&room=<%=r.getRoom_name()%>&room_id=<%=r.getRoom_id()%>&table_id=<%=table.getTalbe_id()%>&class_id=<%=c.getCourseID()%>&week_id=<%=week_id%>" >
                                
                                
                                <%--  Infosys code changes ends here --%>
                                <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                </a>
                                <%}else{
                                %>
                                <%=table.getTraineesCnt()%> + <%=table.getGuestCnt()%> 
                                <%}%>
                                </td></tr> 
                                <%
                                }}%>                          
                 
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