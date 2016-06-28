<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.RBUClassRosterBean"%>
<%@ page import="com.pfizer.db.RBUTrainingWeek"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.report.RBUClassRosterReportWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.PrintUtils"%>
<%@ page import="com.tgix.printing.RBUBoxDataBean"%>
<%@ page import="com.tgix.printing.TRMOrderDateBean"%>
<%@ page import="com.tgix.printing.PrintingConstants"%>
<%@ page import="com.tgix.printing.TrainingWeeks"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collections"%>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>
<%--<netui:html> --%>
    <head>
        <title>
            Product Training (PSCPT) - Class Roster Report
        </title>
    </head>
    <script src="/TrainingReports/resources/js/OpenPopUp.js"></script>
    <script src="/TrainingReports/resources/js/OpenEmployeeList.js"></script>
    
        <script language="javascript">
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
     // alert(user_selected_box);
      if( user_selected_box == 0){
        alert('Please select a Week to continue.');
        return false;
      }
       /*  var url='/TrainingReports/RBU/displayClassRosterReport.do?WeekId='+encodeURIComponent(user_selected_box);
        */
        var url='/TrainingReports/RBU/displayClassRosterReport?WeekId='+encodeURIComponent(user_selected_box);
       // alert(url);
         window.location.href = url;
      
      }
    
    </script>
            <script type="text/javascript" language="JavaScript">
addEvent(window, "load", sortables_init);
function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.id+' ').indexOf("employee_table") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}
function checkAll() {
var myform = document.printForm;
   // var myform = document.forms[0];
	for(var i=0;i<myform.length;i++) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = true; 
		} 
	}	
}
function unCheckAll() {
	var myform = document.printForm;
     //var myform = document.forms[0];
	for(var i=0;i<myform.length;i++) { 
		if(myform.elements[i].type=='checkbox') { 
		myform.elements[i].checked = false; 
		} 
	}	
    
}
function downloadExcel(){
    var myform = document.printForm;
    var week = document.printForm.week.value;
    var emplId = '';
    var counter = 0; 
    for(var i=0;i<myform.length;++i) { 
       if(myform.elements[i].type=='checkbox') { 
            if(myform.elements[i].checked){
                if ( counter == 0 ) {
                    emplId = myform.elements[i].value;
                } 
                else {
                    emplId = emplId + ',' + myform.elements[i].value;
                }
            counter = counter + 1;   
            }
       }
     }
      if(counter >1000){
        alert('Please select number of employees less than 1000');
        return false;
     }
     var url="/TrainingReports/RBU/displayClassRosterReport?downloadExcel=true&orderNumber="+encodeURIComponent(emplId)+"&WeekId="+encodeURIComponent(week);
     window.location.href = url;
}
function printAgendas(){
    var myform = document.printForm;
     var week = document.printForm.week.value;
    var emplId = '';
    var counter = 0; 
    for(var i=0;i<myform.length;++i) { 
       if(myform.elements[i].type=='checkbox') { 
            if(myform.elements[i].checked){
                if ( counter == 0 ) {
                    emplId = myform.elements[i].value;
                } 
                else {
                    emplId = emplId + ',' + myform.elements[i].value;
                }
            counter = counter + 1;   
            }
       }
     }
     if(counter >1000){
        alert('Please select number of employees less than 1000');
        return false;
     }
     //if(emplId.length <= 0){
       // alert('Please select employees to print personalized agendas');
        //return false;
     //}
   <%-- //  var url="<%=PageflowTagUtils.getRewrittenFormAction("printSearchEmployeeAgenda", pageContext)%>?orderNumber="+encodeURIComponent(emplId); --%>
    var url="/TrainingReports/PrintHome/printSearchEmployeeAgenda?orderNumber="+encodeURIComponent(emplId)+"&week="+encodeURIComponent(week);
       var appURL = "<%=request.getRequestURL()%>";
       var requestURL = '';
       if (appURL.indexOf("localhost") != -1 || appURL.indexOf("amrnwlw058") != -1 || appURL.indexOf("trt-dev.pfizer.com") != -1 || appURL.indexOf("trt-dev") != -1){
           requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_DEV%>'  + url;
      }else if (appURL.indexOf("trt-tst.pfizer.com") != -1 || appURL.indexOf("trt-tst") != -1){
            requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_INT%>'  + url;
      
      }else if (appURL.indexOf("trt-stg.pfizer.com") != -1 || appURL.indexOf("trt-stg") != -1){
            requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_STG%>'  + url;
      
      }else if (appURL.indexOf("trt.pfizer.com") != -1 ){
        requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_PROD%>'  + url;
      } 
      // window.open(url,'rr',"left=240, top=170, width=635,height=256,scrollbars=yes,location=no,status=yes,resizable = yes");
       window.open(requestURL,'rr',"left=240, top=170, width=635,height=256,scrollbars=yes,location=no,status=yes,resizable = yes");
}
</script>

    <%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");
%>
    <%--<body> --%>
        <%
                
                RBUClassRosterReportWc wc = (RBUClassRosterReportWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);                 
                TrainingWeeks trainingWeek[];
                trainingWeek = wc.getTrainingWeek();
                String displayResult = wc.getDisplayResult();
                RBUClassRosterBean[] classRosterBean;
                classRosterBean = wc.getEmpReport();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String weekIdFromRequest = ""; 
               if(session.getAttribute("weekId")  != null){
                    weekIdFromRequest = (String)session.getAttribute("weekId");
               }
               String  excelURL = "/RBU/displayClassRosterReport?WeekId="+ weekIdFromRequest;
               String backMenu ="PSCPT Admin Reports";
                /* Infosys code changes starts here
                String backLink ="rbusreportselect.do";   */
                String backLink ="rbusreportselect";  
        %>
        <form name="printForm" class="form_basic">
        <table width="100%">
         <%if (!downloadExcel) {%>
             <h3 style="MARGIN-TOP:20PX"  >
                 Product Training (PSCPT) Class Roster Report
                </h3>  
                    
        <%}%>
         <%if (!downloadExcel) {%>
        <tr>
           <td width="10%">Select a week</td>
            <td align="left"> 
            <select  onchange="getReport(this)" name="week">
           <%
                if(trainingWeek != null){
                for(int j=0;j<trainingWeek.length;j++){    
                 String weekId = trainingWeek[j].getWeek_id();
                Date today = new Date();
                String newDate = dateFormat.format(today);
                
               // Date startDate = dateFormat.parse(trainingWeek[j].getStart_date());
              //  Date endDate = dateFormat.parse(trainingWeek[j].getEnd_date());
              //  if(trainingWeek[j].getStart_date().equals(newDate)){
               // if(today.after(startDate) && today.before(endDate)){
//                  if(trainingWeek[j].getWeek_id().equals("1") || trainingWeek[j].getWeek_id().equals("2")){
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
            </select>
            </td> 
            </tr>
        <%
         }
        if(displayResult.equals("Y") ){
        %>
           <div style="margin-left:10px;margin-right:20px">
         <%
               
                  if(trainingWeek != null){
                 Date today = new Date();
                String newDate = dateFormat.format(today);
                for(int j=0;j<trainingWeek.length;j++){ 
             //       if(trainingWeek[j].getStart_date().equals(newDate)){
                //Date startDate = dateFormat.parse(trainingWeek[j].getStart_date());
                      //  Date endDate = dateFormat.parse(trainingWeek[j].getEnd_date());
                      //  if(trainingWeek[j].getStart_date().equals(newDate)){
                     //   if(today.after(startDate) && today.before(endDate)){
                       if(trainingWeek[j].getWeek_id().equals(weekIdFromRequest)){
                     String weekId = trainingWeek[j].getWeek_id();
                %>
              
              <b>Week <%=weekId%></b> <br>
              <b>Start Date : <%=trainingWeek[j].getStart_date()%></b><br>
              <b>End Date : <%=trainingWeek[j].getEnd_date()%></b>
          </div>
           <%
                }
                }
                  }
               
            %>
             <%if (!downloadExcel) {%>
             
                <div style="margin-left:10px;margin-right:10px">
               <div id="table_inst">
                <%--
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%><%=excelURL%>&downloadExcel=true">Download to Excel</a>
                --%>
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a onclick="downloadExcel()" href="#">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/<%=backLink%>"><%=backMenu%></a>
            </div>            
        </div>
                    
        <%}%>
        
        <div class="top_table_buttons" style="float:right;">
		 <input type="button" class="submit_button"  name="check" onclick="checkAll()" value="Check All"/>
          <input type="button" class="submit_button"  name="unCheck" onclick="unCheckAll()" value="Uncheck All"/>
        <input type="button" class="submit_button"  name="printPersonalizedAgenda" onclick="printAgendas()" value="Print Personalized Agendas"/>
        
		<%--<img src="<%=AppConst.IMAGE_DIR%>/training/b_sendemail.gif" onclick="printAgendas()" />--%>
        
        </div>
        <table cellspacing="0" id="employee_table" width="100%" class="employee_table" style="margin-left:10px;margin-right:10px">
            <tr>
            <th nowrap>Employee ID</th>
        	<th nowrap>First Name</th>
			<th nowrap>Last Name</th>		
			<th nowrap>Product</th>
            <th nowrap>Start Date</th>
            <th nowrap>Role CD</th>
            <th nowrap>Is Trainer ?</th>
            <th nowrap>Room Name</th>
            <th nowrap>Table Number</th>
            <th nowrap>&nbsp;</th>
        </tr>
        <%
            if(classRosterBean != null){
             for(int k=0;k<classRosterBean.length;k++){        
        %>
        <tr>
            <td><%=classRosterBean[k].getEmplId()%></td>
            <td><%=classRosterBean[k].getFirstName()%></td>
            <td><%=classRosterBean[k].getLastName()%></td>
            <td><%=classRosterBean[k].getProduct()%></td>
            <td><%=classRosterBean[k].getStartDate()%></td>
            <td><%=PrintUtils.ifNullNBSP(classRosterBean[k].getRole_Cd())%></td>
            <td><%=classRosterBean[k].getIsTrainer()%></td>
            <td><%=classRosterBean[k].getRoomName()%></td>
            <td><%=classRosterBean[k].getTableNumber()%></td>
            <%if (!downloadExcel) {%>
            <td><input name="<%=classRosterBean[k].getEmplId()%>" value="<%=classRosterBean[k].getEmplId()%>"  type="checkbox">
                </td>
             <%
            }
             %>   
            </tr>
            <%
             }
            }
            %>
            </table>
            <%
               }
            %>
       </table>     
        </form>
   <%-- </body>
</netui:html>--%>