<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.LaunchMeetingReportList"%>
<%@ page import="com.pfizer.webapp.wc.components.report.RBUReportList"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ToviazLaunchReportList"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.util.*"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%--<netui:html>--%>
    <head>
        <title>
            Launch Meeting Training Schedule
        </title>
    </head>
<script src="/TrainingReports/resources/js/sorttable.js"></script>


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
</script>
   
    
    <!--<body>-->
        <p>
           <%
         LaunchMeetingReportList wc = (LaunchMeetingReportList)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
         User user=wc.getUser();
        AppQueryStrings qString = new AppQueryStrings();
        FormUtil.loadObject(request,qString);
        String type ="";
        String section = "";
        if(session.getAttribute("selectedSection") != null){
            section = (String)session.getAttribute("selectedSection");
        }
        if(session.getAttribute("type") != null){
            type = (String)session.getAttribute("type");
        }
        List exams = new ArrayList();
        if(session.getAttribute("exams") != null){
            exams = (List)session.getAttribute("exams");
        }
        String exam = "";
        if(session.getAttribute("exam") != null){
            exam = (String)session.getAttribute("exam");
        }
        
           %>
    <script type="text/javascript" language="JavaScript">
var emailWindow;

function submitEmail() {
    var myform = document.forms[0];
    //alert(myform);
	var emails = ''; 
	var counter = 0;
	var currentUserEmail = '<%=wc.getUser().getEmail()%>'; 
    var answer = true;
    var doneflag = false;
    var oneSelected = 0;
    
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
        if(myform.elements[i].name != 'MailSelectForm_email' || myform.elements[i].name != 'MailSelectForm_email_managers'){
			if (myform.elements[i].checked) {
            oneSelected = 1;
				if ( counter == 0 ) {
                if(myform.elements[i].value != ''){
					emails = myform.elements[i].value;
                    }
				} else if ( doneflag ) {
                    myform.elements[i].checked = false;
                } else {
                        var tmpstr = '';
                        if(myform.elements[i].value != ''){
                            var tmpstr = emails + '; ' + myform.elements[i].value;
                        }
                    if ( tmpstr.length > 1950 ) {
                        var answer = confirm('Too many email recipients selected. The first ' + (counter-1) + ' recipients can be emailed now, the others will be de-selected.  Would you like to continue?');
                        if (answer) {
                            myform.elements[i].checked = false;
                            doneflag = true;
                        } else {
                            i = myform.length;
                        }
                    } else {
                            if(myform.elements[i].value != ''){
                                emails = emails + '; ' + myform.elements[i].value;
                            }
                    }
				}
				counter = counter + 1;
			} 
		}
        } 
	}
    if(oneSelected == 0){
        alert('Please select atleast one employee to send email.');
        return false;
    }
    if(emails == ''){
        alert('The selected employee(s) do not have a direct \'Reports To\'');
        return false;
    }
	var emailToStr = 'mailto:';	
	var ccString = '';
	var subjectStr = '&subject=Launch Meeting Follow-up';	
	var sendToStr = '?subject=';
	if (counter == 1) {
		sendToStr = emailToStr + emails +  subjectStr;
	} else {
		sendToStr = emailToStr  + '?bcc=' +  emails + subjectStr;
	}

    if (answer) {
    	window.location = sendToStr;
    } 
}

function checkAll() {
    //var myform = document.emailSelectForm;
    var myform = document.forms[0];
    //alert(myform);
	for(var i=0;i<myform.length;i++) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = true; 
		} 
	}	
}
function unCheckAll() {
	//var myform = document.emailSelectForm;
     var myform = document.forms[0];
	for(var i=0;i<myform.length;i++) { 
		if(myform.elements[i].type=='checkbox') { 
		myform.elements[i].checked = false; 
		} 
	}	
    
}

 checked = false;
      function checkedAllReps () {
       var myform = document.forms[0];
        if (checked == false){checked = true}else{checked = false}
       var myform = document.forms[0]; 
    for(var i=0;i<myform.length;i++) {
    if(myform.elements[i].type=='checkbox' && myform.elements[i].name == 'MailSelectForm_email') {  
       myform.elements[i].checked=checked;
      }
	}
     }

checkedManager = false;
      function checkedAllManagers () {
       var myform = document.forms[0];
        if (checkedManager == false){checkedManager = true}else{checkedManager = false}
       var myform = document.forms[0]; 
    for(var i=0;i<myform.length;i++) {
    if(myform.elements[i].type=='checkbox' && myform.elements[i].name == 'MailSelectForm_email_managers') {  
       myform.elements[i].checked=checkedManager;
      }
	}
     }


</script>
           
        </p>
        
        <table class="no_space_width">
	<tr>
		<td>
			<p id="table_inst_title">Click on employee name to get detailed report</p>
		</td>
		<td>&nbsp;</td>
	</tr>
</table>
        <div>
	<div id="table_inst">
	<!-- Infosys - Weblogic to Jboss Migrations changes start here -->
		<%-- <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/LaunchMeeting/listreport.do?downloadExcel=true&type=<%=type%>&excelSection=<%=section%>&trackId=<%=wc.getTrackId()%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/LaunchMeeting/listreport.do?type=<%=type%>&section=<%=section%>&trackId=<%=wc.getTrackId()%>">Back to default sort</a> --%>
		<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/LaunchMeeting/listreport?downloadExcel=true&type=<%=type%>&excelSection=<%=section%>&trackId=<%=wc.getTrackId()%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/LaunchMeeting/listreport?type=<%=type%>&section=<%=section%>&trackId=<%=wc.getTrackId()%>">Back to default sort</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/LaunchMeeting/begin?">Launch Meeting Reports</a>
		<!-- Infosys - Weblogic to Jboss Migrations changes end here -->
                
	</div>
	<div class="top_table_buttons" style="float:right;">	
		<%--
        <img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll()" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="unCheckAll()" />
        --%>
        All Reps: <input type='checkbox' name='checkall' onclick='checkedAllReps();' value="">
        All Managers: <input type='checkbox' name='checkallManagers' onclick='checkedAllManagers();' value="">
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_sendemail.gif" onclick="submitEmail()" align="bottom" />

	</div>
	<div class="clear"></div>	
</div>


<form name="emailSelectForm">
<table cellspacing="0" id="employee_table" width="100%" class="employee_table">
  <tr>
		

			<th nowrap>Future BU</th>
            <th nowrap>Future RBU</th>
			<th nowrap>Last Name</th>
			<th nowrap>First Name</th>
			<th nowrap>Emplid</th>
			<%
           if(!exam.equals("")){
            %>
            
            <th nowrap><b><%=exam%></b></th>
            <%
            }
            %>
            <th>Email To Reps</th>
            <th>Email To Managers</th>
        </tr>
        <%
        String trmColor="";
         Employee[] employeeBean=wc.getEmployeeBean();
         Employee emp;
         HashMap empHashMap=new HashMap();
         for(int i=0;i<employeeBean.length;i++){
         emp=employeeBean[i];
         String trClass = "";
         String b="<b>";
         String bb="</b>";
         
         boolean doFlag = false;  // flag to determine of a row should be shown
          if (!doFlag) { 	
			
				if ( "VP".equals( emp.getRole() ) ) {
					trClass = "class='active_row avp_row'";
                   trmColor="#d6ebad";
                   
                   
                    
				}else
				if ( "RM".equals( emp.getRole() ) || "ARM".equals( emp.getRole() ) ) {
					trClass = "class='active_row rm_row'";
                    trmColor="#ffd699";
				}else
				if ( "DM".equals( emp.getRole() ) ) {
					trClass = "class='active_row dm_row'";
                     trmColor="#c2d6eb";
				}
                else{
                    trmColor="";
                    b="";
                    bb="";
                }
                
         }
         
         %>
         <tr <%=trClass%>  bgcolor="<%=trmColor%>"  >
         <td><%=b%><%=Util.toEmptyNBSP(emp.getFutureBU())%><%=bb%></td>
         <td><%=b%><%=Util.toEmptyNBSP(emp.getFutureRBU())%><%=bb%></td>
         		<!-- Infosys - Weblogic to Jboss Migrations changes start here -->
				<%-- <td><%=b%><a href="<%=AppConst.APP_ROOT%>/LaunchMeeting/detailpage.do?&emplid=<%=emp.getEmplId()%>&phaseNumber=<%=type%>&section=<%=section%>&track=<%=wc.getTrackId()%>"><%=emp.getLastName()%> </a><%=bb%></td> --%>
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/LaunchMeeting/detailpage?&emplid=<%=emp.getEmplId()%>&phaseNumber=<%=type%>&section=<%=section%>&track=<%=wc.getTrackId()%>"><%=emp.getLastName()%> </a><%=bb%></td>
				<%-- <td><%=b%><a href="<%=AppConst.APP_ROOT%>/LaunchMeeting/detailpage.do?&emplid=<%=emp.getEmplId()%>&phaseNumber=<%=type%>&section=<%=section%>&track=<%=wc.getTrackId()%>"><%=emp.getFirstName()%></a><%=bb%></td> --%>
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/LaunchMeeting/detailpage?&emplid=<%=emp.getEmplId()%>&phaseNumber=<%=type%>&section=<%=section%>&track=<%=wc.getTrackId()%>"><%=emp.getFirstName()%></a><%=bb%></td>
				<!-- Infosys - Weblogic to Jboss Migrations changes end here -->
                
                <td><%=b%><%=emp.getEmplId()%><%=bb%></td>
               <%--
                <%if(!"Attendance".equalsIgnoreCase(type)){
                    List examScores = new ArrayList();
                    examScores = emp.getAvailableExams();
                    empHashMap=emp.getProductStatusMap();
                    if(exams != null)
                    {
                           Iterator iter = exams.iterator();
                            while(iter.hasNext()){
                            String exam   = (String)iter.next();
                            Iterator examIter = examScores.iterator();
                            boolean found = false;                            
                            while(examIter.hasNext()){
                                HashMap map = (HashMap)examIter.next();
                                if(map.containsKey(exam.trim())){
                                    found = true;
                                String score = Util.toEmptyBlank((String)map.get(exam));
                                String font ="black";
                                if(score != null && !exam.equals("Attendance") && !score.equals("")){
                                    if(Integer.parseInt(score) < 80){
                                        font = "red";
                                    }
                                    else{
                                        font = "black";
                                    }
                                }
                    %>
                    <td bgcolor="#FFF39C"><font color="<%=font%>"><%=b%><%=Util.toEmptyNotComplete((String)map.get(exam))%><%=bb%></font></td>
                    <%
                                }
                            }
                            if(!found){
                     %>           
                        <td bgcolor="#FFF39C">N/A</td>
                       <%         
                            }    
                        }
                }
               
                }    
                %>
                
                <%
                    if(!"Attendance".equalsIgnoreCase(type)){
                    String ped1 = emp.getPed1();
                    String font1 ="black";
                    if(ped1 != null && !ped1.equals("Not Complete") && !ped1.equals("On Leave")){
                       if(Integer.parseInt(ped1) < 80){
                           font1 = "red";
                        }
                        else{
                            font1 = "black";
                        }
                        
                    }
                    
                    String ped2 = emp.getPed1();
                    String font2 ="black";
                    if(ped2 != null && !ped2.equals("Not Complete") && !ped2.equals("On Leave")){
                       if(Integer.parseInt(ped2) < 80){
                           font2 = "red";
                        }
                        else{
                            font2 = "black";
                        }
                        
                    } 
                     
                %>
                <%
                   if("Overall".equalsIgnoreCase(type) || "Ped1".equalsIgnoreCase(type)){
                %>
                <td bgcolor="#FFF39C"><font color="<%=font1%>"><%=b%><%=emp.getPed1()%><%=bb%></font></td>
                <%
                   }
                    if("Overall".equalsIgnoreCase(type) || "Ped2".equalsIgnoreCase(type)){
                %>
                <td bgcolor="#FFF39C"><font color="<%=font2%>"><%=b%><%=emp.getPed2()%><%=bb%></font></td>
                <%
                    }
                    }
                   if("Overall".equalsIgnoreCase(type)){
                %>
                    <td bgcolor="#FFF39C"><%=b%><%=emp.getAttendance()%><%=bb%></td> 
                <%
                   }
                %>
              <%-- <td><input name="MailSelectForm_email_<%=emp.getEmplId()%>" value="<%=emp.getEmail()%>"  type="checkbox">
                </td>
                --%>
                <%
                    if(!exam.equals("")){
                        
                %>
                 <td><%=b%><%=Util.toEmptyNBSP(emp.getPed1())%><%=bb%></td>
                <%
                    }
                %>
                <td><input name="MailSelectForm_email" value="<%=emp.getEmail()%>"  type="checkbox">
                </td>
                <%--
                 <td><input name="MailSelectForm_email_managers_<%=emp.getReportsToEmplid()%>" value="<%=emp.getReportsToEmail()%>"  type="checkbox">
                </td>
                --%>
                 <td><input name="MailSelectForm_email_managers" value="<%=emp.getReportsToEmail()%>"  type="checkbox">
                </td>
                </tr>
                <%
         }
                %>
                </table>
     </form>
<%--
    </body>
</netui:html>
--%>