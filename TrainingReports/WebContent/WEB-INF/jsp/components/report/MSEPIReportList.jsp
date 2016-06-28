<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.GNSMReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MSEPIReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.util.*"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<!-- <netui:html>  -->
<html>
    <head>
        <title>
            Web Application Page
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
   
    
    <body> 
        <p>
           <%
        MSEPIReportListWc wc = (MSEPIReportListWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
        User user=wc.getUser();        
        AppQueryStrings qString = new AppQueryStrings();
        FormUtil.loadObject(request,qString);
        String type=qString.getType();     
           %>
           
           
    <script type="text/javascript" language="JavaScript">
var emailWindow;

function submitEmail() {
    var answer = true;
    var doneflag = false;
	var myform = document.emailSelectForm;
	var emails = ''; 
	var counter = 0;
	var currentUserEmail = '<%=wc.getUser().getEmail()%>';
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			if (myform.elements[i].checked) {
				if ( counter == 0 ) {
					emails = myform.elements[i].value;
				} else if ( doneflag ) {
                    myform.elements[i].checked = false;
                } else {
                    var tmpstr = emails + '; ' + myform.elements[i].value; 
                    if ( tmpstr.length > 1950 ) {
                        var answer = confirm('Too many email recipients selected. The first ' + (counter-1) + ' recipients can be emailed now, the others will be de-selected.  Would you like to continue?');
                        if (answer) {
                            myform.elements[i].checked = false;
                            doneflag = true;
                        } else {
                            i = myform.length;
                        }
                    } else {
    					emails = emails + '; ' + myform.elements[i].value;
                    }
				}
				counter = counter + 1;
			} 
		} 
	}
	
	var emailToStr = 'mailto:';	
	var ccString = '';
	var subjectStr = '&subject=MS/Epi National Sales Meeting Follow-up';	
	var sendToStr = '&subject=';
	if (counter == 1) {
		sendToStr = emailToStr + emails +  subjectStr;
	} else {
		sendToStr = emailToStr + '?bcc=' + emails + subjectStr;
	}
    if (answer) {
    	window.location = sendToStr;
    } 
}

function checkAll() {
	var myform = document.emailSelectForm;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = true; 
		} 
	}	
}
function unCheckAll() {
	var myform = document.emailSelectForm;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = false; 
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
		<td>&nbsp;</td>
	</tr>
</table>
        <div>
	<div id="table_inst">
	     <!-- Infosys - Weblogic to Jboss Migrations changes start here -->
		<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/MSEPI/listReportMSEPI?downloadExcel=true&type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/MSEPI/listReportMSEPI?type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Back to default sort</a>
		<!-- Infosys - Weblogic to Jboss Migrations changes end here -->
	</div>
	<div class="top_table_buttons" style="float:right;">	
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll()" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="unCheckAll()" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_sendemail.gif" onclick="submitEmail()" />
	</div>
	<div class="clear"></div>	
</div> 


<form name="emailSelectForm" >
<table cellspacing="0" id="employee_table" width="100%" class="employee_table">
  <tr>
		

			<th nowrap>Team</th>

        
        	<th nowrap>District</th>
		
        
			<th nowrap>Last Name</th>
		
			<th nowrap>First Name</th>
		
			<th nowrap>Role</th>
		
			<th nowrap>Emplid</th>
			<%if("Overall".equalsIgnoreCase(type)){%>
                <th nowrap><b>Geodon Exam 1</b></th>
                <th nowrap><b>Geodon Exam 2</b></th>
                <th nowrap><b>Survey</b></th>
                <th nowrap><b>Video Link</b></th>
                <th nowrap><b>Attendance</b></th>
            <%}%>
            <th nowrap><b><%=qString.getType()%></b></th>
			
            <th>Email</th>
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
          /*if (!doFlag) { 	
                		
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
         */
         %>
         <tr <%=trClass%>  bgcolor="<%=trmColor%>">
         <td><%=(emp.getTeamCode()==null)?"":emp.getTeamCode()%></td>
				<td><%=(emp.getDistrictDesc()==null)?"":emp.getDistrictDesc()%></td>
                	
				<td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=emp.getEmplId()%>&m1=MSEPI"><%=emp.getLastName()%> </a></td>
				<td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=emp.getEmplId()%>&m1=MSEPI"><%=emp.getFirstName()%></a></td>
				<td><%=emp.getRole()%><%=bb%></td>
				<td><%=emp.getEmplId()%><%=bb%></td>
                <%if("Overall".equalsIgnoreCase(type)){
                    empHashMap=emp.getProductStatusMap();
                    
                    %>
                    <td bgcolor="#FFF39C"><%=Util.toEmptyNA((String)empHashMap.get("GEODE1"))%></td>
                    <td bgcolor="#FFF39C"><%=Util.toEmptyNA((String)empHashMap.get("GEODE2"))%></td>
                    <%--<td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("RVTO"))%><%=bb%></td>--%>
                    <td bgcolor="#FFF39C"><%=Util.toEmptyNA((String)empHashMap.get("GEODSY"))%></td>
                    <td bgcolor="#FFF39C"><%=Util.toEmptyNA((String)empHashMap.get("GEODVL"))%></td>
                    <td bgcolor="#FFF39C"><%=Util.toEmptyNA((String)empHashMap.get("GEODAT"))%></td>
                    <td ><%=qString.getSection()%></td>
                
                <%}else{
                    %>
                    <td bgcolor="#FFE87C"><%=emp.getCourseStatus()%><%=bb%></td>
                    <%
                }%>
                
			
                <td><input name="MailSelectForm_email_<%=emp.getEmplId()%>" value="<%=emp.getEmail()%>"  type="checkbox">
                </td>
                </tr>
                <%
         }
                %>
                </table>
        </form>

    </body>
<!-- </netui:html> -->
</html>