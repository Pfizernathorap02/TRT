<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.GeneralSessionEmployee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.PDFHSReportList"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.HashMap"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"% --%>>

<netui:html>
    <head>
        <title>
            General Session Attendance
        </title>

        <LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
        <LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
        
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
   
    
    <body>
        <jsp:include page="Header.jsp" />    
        <p>
          <%         
         GeneralSessionEmployee[] employees = (GeneralSessionEmployee[])request.getAttribute(GeneralSessionEmployee.ATTRIBUTE_NAME);         
         String sUpdateStatus = (String)request.getAttribute("status");     
          %>
           
           
    <script type="text/javascript" language="JavaScript">

function updateAttendance() 
{
	var myform = document.generalSessionSelectForm;

	var emplids = '';
    var checkedstatus = '';
    var precheckedstatus = '';
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') 
		{
            //alert('emplids:' + emplids + '\n' + 'checkedstatus:' + checkedstatus);
            if (emplids == '') {
                // Don't append a comma
                precheckedstatus = document.getElementById('pre_checked_status_' + myform.elements[i].value).value;
                emplids = myform.elements[i].value;
                if (myform.elements[i].checked) {
                    checkedstatus = 'P';
                }
                else {
                    checkedstatus = ' ';
                }
            }
            else {
                precheckedstatus = precheckedstatus + ',' + document.getElementById('pre_checked_status_' + myform.elements[i].value).value;
                emplids = emplids + ',' + myform.elements[i].value;
                if (myform.elements[i].checked) {
                    checkedstatus = checkedstatus + ',' + 'P';
                }
                else {
                    checkedstatus = checkedstatus + ',' + ' ';
                }
            }
        }
    }
    document.all.generalSessionSelectForm.precheckedstatus.value = precheckedstatus;
    document.all.generalSessionSelectForm.emplids.value = emplids;
    document.all.generalSessionSelectForm.checkedstatus.value = checkedstatus;
    document.all.generalSessionSelectForm.submit();
}
            

function checkAll() {
	var myform = document.generalSessionSelectForm;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = true; 
		} 
	}	
}
function unCheckAll() {
	var myform = document.generalSessionSelectForm;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = false; 
		} 
	}	
}

</script>
           
        </p>
        
        <table class="no_space_width" style="MARGIN:15PX">
	      <tr>
		    <td>
			  <p id="table_inst_title">Please Check/Uncheck the employees who attended General Session Training</p>
		    </td>
	      </tr>
         <%if(sUpdateStatus != null && sUpdateStatus.length()> 0){%>
	      <tr>
		    <td>
			  <p style="color:blue" id="table_inst_title"><%=sUpdateStatus%></p>
		    </td>
	      </tr>
          <%}%>

        </table>
        <div>
	<div class="top_table_buttons" style="float:right;">	
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll()" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="unCheckAll()" />
        <input type="submit" value="Update Attendance" onclick="updateAttendance()"/>		        
	</div>
	<div class="clear"></div>	
    </div>


<form name="generalSessionSelectForm" action="/TrainingReports/PWRA/updateGeneralSession" method="post">

<input type=hidden id="emplids" name="emplids" value="">
<input type=hidden id="checkedstatus" name="checkedstatus" value="">
<input type=hidden id="precheckedstatus" name="precheckedstatus" value="">


<table cellspacing="0" id="employee_table" width="100%" class="employee_table" style="MARGIN:15PX">
  <tr>
			<th nowrap>Team</th>        
        	<th nowrap>District</th>    
			<th nowrap>Last Name</th>		
			<th nowrap>First Name</th>		
			<th nowrap>Role</th>		
			<th nowrap>Emplid</th>
            <th nowrap>Date</th>
            <th nowrap>Attended</th>                
        </tr>
        <%
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String trmColor="";
         //Employee[] employeeBean=wc.getEmployeeBean();
         GeneralSessionEmployee emp;
         HashMap empHashMap=new HashMap();
         for(int i=0;i<employees.length;i++){
         emp=employees[i];
         String trClass = "";
         String b="<b>";
         String bb="</b>";
         String strStartDate = "";
         try {
            strStartDate = format.format(emp.getStartDate());
         }
         catch (Exception e) {
            strStartDate = "";
         }

         trmColor = "white";
         String sChecked;
         if(emp.getAttended()!=null && emp.getAttended().equalsIgnoreCase("P"))
         {
            sChecked = "checked";
         }else{
            sChecked = "";
         }
         
         %>
         <tr <%=trClass%>  bgcolor="<%=trmColor%>"  >
           <td><%=b%><%=emp.getTeamCode()%><%=bb%></td>
          <td><%=b%><%=(emp.getDistrictDesc()==null)?"":emp.getDistrictDesc()%><%=bb%></td>                	
				<td><%=b%><%=emp.getLastName()%> <%=bb%></td>
				<td><%=b%><%=emp.getFirstName()%><%=bb%></td>
				<td><%=b%><%=emp.getRole()%><%=bb%></td>
				<td><%=b%><%=emp.getEmplId()%><%=bb%></td>                			
                <td><%=b%><%=Util.toEmptyNBSP(strStartDate)%><%=bb%></td> 
                <td><input value='<%=emp.getEmplId()%>_<%=emp.getCourseId()%>' type=checkbox <%=sChecked%>> </td> 
                <td><input type=hidden id="pre_checked_status_<%=emp.getEmplId()%>_<%=emp.getCourseId()%>" name="pre_status_<%=emp.getEmplId()%>_<%=emp.getCourseId()%>" value="<%="P".equalsIgnoreCase(emp.getAttended()) ? "P" : " "%>" />                              
                </tr>
                
                <%
         }
                %>
     </table>
    
    
    </form>

    </body>
</netui:html>