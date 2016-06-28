<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.action.RBUControllerAction.RbuUnAssignedEmployeeForm"%>
<%@ page import="com.pfizer.actionForm.RbutraineetablemapForm"%>
<%@ page import="com.pfizer.db.RBUClassRoom"%>
<%@ page import="com.pfizer.db.RBUClassTable"%>
<%@ page import="com.pfizer.db.RBUEnrollmentException"%>
<%@ page import="com.pfizer.db.RBUTrainee"%>
<%@ page import="com.pfizer.db.RBUUnassignedEmployees"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.RBUEnrollmentExceptionWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.RBUUnassignedEmployeesWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="com.tgix.printing.TrainingWeeks"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<!-- <netui:html> -->
<html>
    <head>
        <title>
            Product Training (PSCPT) - Unassigned Employees
        </title>
    </head>
  <script lang="javascript">
  
   function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
        }
        function ltrim(stringToTrim) {
	return stringToTrim.replace(/^\s+/,"");
        }
        function rtrim(stringToTrim) {
	return stringToTrim.replace(/\s+$/,"");
        }
        
      function getReport(selectObj){
      var the_form = window.document.forms[0];
      var idx = selectObj.selectedIndex;
       var user_selected_box=0;
       // var user_selected_box =the_form.week.value;
      user_selected_box =  selectObj.options[idx].value; 
        var url='/TrainingReports/RBU/rbuUnassignedEmployeesReport?weekId='+encodeURIComponent(user_selected_box);
         window.location.href = url;
      }
      
      function validate(){
          var output = 0;
          var collection = document.getElementsByTagName("select")
          for(var x = 0;x<collection.length;x++){
             var name  =  collection[x].name;
             var result = collection[x].value;
             if(name.indexOf("tables") != -1 && result != -1){
                output = 100;
             }
        }
        if(output != 100){
            alert('Please select table assignment for atleast one employee.');
            return false;
        }
        if(output == 100) {
            document.changeassignment.submit();
        }
      }
    </script>
<LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
 <%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");
%>
    <body>
        <div style="margin-left:10px;margin-right:10px">
        
        <%
        RBUUnassignedEmployeesWc wc = (RBUUnassignedEmployeesWc)request.getAttribute(RBUUnassignedEmployeesWc.ATTRIBUTE_NAME);         
        TrainingWeeks trainingWeek[];
        trainingWeek = wc.getTrainingWeek();
        List trainees = wc.getEmpReport();  
        RbuUnAssignedEmployeeForm form = wc.getRbuUnAssignedEmployeeForm();
        String weekIdFromRequest = ""; 
       if(session.getAttribute("weekId")  != null){
            weekIdFromRequest = (String)session.getAttribute("weekId");
       }
       String  excelURL = "/RBU/rbuUnassignedEmployeesReport?weekId="+ weekIdFromRequest;
        %>
        <br>
        
        <br>
        <form name="changeassignment"   action="<%=AppConst.APP_ROOT%>/RBU/rbuAssignEmployeesToTables" method="post">
        <table width="100%">
         <h3 style="MARGIN-TOP:20PX"  >
                 Product Training (PSCPT) - Unassigned Tables Report
         </h3>  
         <div style="margin-left:10px;margin-right:20px">
         <%
                  if(trainingWeek != null){
                for(int j=0;j<trainingWeek.length;j++){ 
                    if(trainingWeek[j].getWeek_id().equals(weekIdFromRequest)){
                     String weekId = trainingWeek[j].getWeek_id();
                %>
              <b>Week <%=weekId%></b> <br>
              <b>Start Date : <%=trainingWeek[j].getStart_date()%></b><br>
              <b>End Date   : <%=trainingWeek[j].getEnd_date()%></b><br><br>
              <A href ="<%=AppConst.APP_ROOT%>/RBU/RBUClassRoomReport?week_id=<%=weekIdFromRequest%>"> Classroom Grid </A><br>
          </div>
           <%
                }
                }
                  }
               
            %>
          <%if (!downloadExcel) {%>   
        <tr>
           <td width="10%">Select a week</td>
            <td align="left" > 
            <select  onchange="getReport(this)" name="week">
           <%
                if(trainingWeek != null){
                for(int j=0;j<trainingWeek.length;j++){    
                 String weekId = trainingWeek[j].getWeek_id();
                  if(weekIdFromRequest.equals(trainingWeek[j].getWeek_id())){  
            %>
                    <option  selected value="<%=trainingWeek[j].getWeek_id()%>"><%=trainingWeek[j].getWeek_name()%></option>
                    <%
                          }
                          else{
                    %>        
                      <option  value="<%=trainingWeek[j].getWeek_id()%>"><%=trainingWeek[j].getWeek_name()%></option>
                     
                    <%        
                          }
                    %>
           <%
                }
                }
           %>
            </select>&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
           &nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; <input type="submit" name ="result" value="Submit" onClick="return validate()" />
            </td> 
          <!--
            <td width="30%" align="right"><input type="submit" name ="result" value="Submit" onClick="return validate()" /></td>-->
            </tr>
        
       
        
                <div style="margin-left:10px;margin-right:10px">
               <div id="table_inst">
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%><%=excelURL%>&downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;
            </div>            
        </div>
                    
        <%}%>
        <table cellspacing="0" id="employee_table" width="80%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <tr></tr>
        <tr>
            <th nowrap>EMPLID</th>
            <th nowrap>Product</th>
            <th nowrap>First Name</th>
            <th nowrap>Last Name</th>
            <th nowrap>Future Role</th>
            <%if (!downloadExcel) {%>
        	<th nowrap>Assign to Table</th>
            <%
            }
            %>
        </tr>

        <%
            String bgColor = "";
            RBUUnassignedEmployees exc;
            StringBuffer tts = new StringBuffer("");
            for(Iterator i= trainees.iterator(); i.hasNext();){
                exc = (RBUUnassignedEmployees) i.next();
                tts.append(exc.getEmplId()+"_"+ exc.getClassId()+"_"+ exc.getIsTrainer());
                tts.append(",");
                if(exc.getIsTrainer() != null && exc.getIsTrainer().equals("Y")){
                    bgColor = "ffd699";
                }
         %>
         <tr bgcolor="<%=bgColor%>">
            <%
                if(exc.getIsTrainer() != null && exc.getIsTrainer().equals("Y")){
            %>
            <td><%=exc.getEmplId()%> (GT)</td>
            <%
                }
                else{
            %>
            <td><%=exc.getEmplId()%></td>
            <%  
                }
            %>
            <td><%=exc.getProductDesc()%></td>                        
             <%if (!downloadExcel) {%>
            <td>
            <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getFirstName())%></a>          
            </td>
            <td>
            <a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=exc.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(exc.getLastName())%></a>          
            </td>            
            <%}else{%>
            <td><%=Util.toEmptyNBSP(exc.getFirstName())%>
            </td>
            <td><%=Util.toEmptyNBSP(exc.getLastName())%>
            </td> 
            <%
            }
            %>
            <td><%=Util.toEmptyNBSP(exc.getFutureRole())%></td>
           <%if (!downloadExcel) {%>
            <td>
            
           <input type="hidden" name="t<%=exc.getEmplId()%><%=exc.getClassId()%>"  id="t<%=exc.getEmplId()%><%=exc.getClassId()%>" value="-1">
             
             Available Tables: <select name="tables<%=exc.getEmplId()%><%=exc.getClassId()%>" onchange="t<%=exc.getEmplId()%><%=exc.getClassId()%>.value = tables<%=exc.getEmplId()%><%=exc.getClassId()%>.options[tables<%=exc.getEmplId()%><%=exc.getClassId()%>.selectedIndex].value;">
             <%--
             Available Tables: <select name="tables<%=exc.getEmplId()%>" onchange="test('<%=exc.getEmplId()%>')"> 
             --%>
            <option value = "-1" selected="true">Select</option> 
            <%for(Iterator iter= exc.getTablesForClassId().iterator(); iter.hasNext();){
                RBUClassTable table = (RBUClassTable) iter.next();
                 String tid = table.getTalbe_id();
            %>
               
            
                <option value = "<%=tid%>">Table <%=tid%> (<%=table.getTraineesCnt()%>  + <%=table.getGuestCnt()%>)</option>    
            <%
            }%>
            </select>
            </td>
            <%
           }
            %>
            
        </tr>
         <%
         }    
         %>
        <tr>
        </tr>
         <input type="hidden" name="emplist" id="emplist" value="<%=tts.toString()%>">
         <input type="hidden" name="weekId" id="weekId" value="<%=form.getWeek_id()%>">
          <%if (!downloadExcel) {%>
        <tr><td>&nbsp;</td>
        <td colspan="5" align="right"><input type="submit" name ="result" value="Submit" onClick="return validate()" /></td></tr>
        <%}%>
        
       <%-- <tr><td colspan="5"><input type="submit" name ="submit" value="Submit" /></td></tr>
       --%>
        </table>
         </table>
        </form>
        </div>
    </body>
<!-- </netui:html> -->
</html>