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
<%@ page import="java.util.HashMap"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-templa --%>te"%>

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
     
          %>
           
           
    <script type="text/javascript" language="JavaScript">

function updateAttendance() 
{

	var myform = document.generalSessionSelectForm;

	var counter = 0;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') 
		{ 
			if (myform.elements[i].checked) 
			{
				if ( counter == 0 ) 
				{
                    document.all.generalSessionSelectForm.checkedstatus.value = 'P';				
					document.all.generalSessionSelectForm.emplids.value = myform.elements[i].value;
				} else {
					document.all.generalSessionSelectForm.emplids.value = document.all.generalSessionSelectForm.emplids.value + ',' + myform.elements[i].value;
					document.all.generalSessionSelectForm.checkedstatus.value = document.all.generalSessionSelectForm.checkedstatus.value + ',' + 'P';
				}
			} else{
				if ( counter == 0 ) 
				{
                    document.all.generalSessionSelectForm.emplids.value = document.all.generalSessionSelectForm.emplids.value + ',' + myform.elements[i].value;				
                    document.all.generalSessionSelectForm.checkedstatus.value = ' ';				

				} else {
					document.all.generalSessionSelectForm.emplids.value = document.all.generalSessionSelectForm.emplids.value + ',' + myform.elements[i].value;
					document.all.generalSessionSelectForm.checkedstatus.value = document.all.generalSessionSelectForm.checkedstatus.value + ',' + ' ';
				}									  
			}
		    counter = counter + 1;
		} 
	}
    
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
			  <p id="table_inst_title">Plese Check/Uncheck the employees who attended General Session Training</p>
		    </td>
	      </tr>
        </table>
        <div>
	<div class="top_table_buttons" style="float:right;">	
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll()" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="unCheckAll()" />
        <input type="submit" value="Update Attendance" onclick="updateAttendance()"/>		        
	</div>
	<div class="clear"></div>	
    </div>


<form name="generalSessionSelectForm" action="/TrainingReports/PLC/updateGeneralSession">

<input type=hidden id="emplids" name="emplids">
<input type=hidden id="checkedstatus" name="checkedstatus">

<table cellspacing="0" id="employee_table" width="100%" class="employee_table" style="MARGIN:15PX">
  <tr>
			<th nowrap>Team</th>        
        	<th nowrap>District</th>    
			<th nowrap>Last Name</th>		
			<th nowrap>First Name</th>		
			<th nowrap>Role</th>		
			<th nowrap>Emplid</th>
            <th nowrap>Attended</th>                
        </tr>
        <%
        String trmColor="";
         //Employee[] employeeBean=wc.getEmployeeBean();
         GeneralSessionEmployee emp;
         HashMap empHashMap=new HashMap();
         for(int i=0;i<employees.length;i++){
         emp=employees[i];
         String trClass = "";
         String b="<b>";
         String bb="</b>";

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
                <td><input value='<%=emp.getEmplId()%>' type=checkbox <%=sChecked%>> </td>                                
                </tr>
                
                <%
         }
                %>
     </table>
    
    
    </form>

    </body>
</netui:html>