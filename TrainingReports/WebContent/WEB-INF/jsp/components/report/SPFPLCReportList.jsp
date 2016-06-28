<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.PLCReportList"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.SPFPLCReportList"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.util.HashMap" %>
<%-- Infosys code changes starts here<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%-- <netui:html> Infosys code changes ends here --%> 
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
         SPFPLCReportList wc = (SPFPLCReportList)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
         User user=wc.getUser();
        
        AppQueryStrings qString = new AppQueryStrings();
        FormUtil.loadObject(request,qString);
      String type=qString.getType();
     
           %>
           
           
    <script type="text/javascript" language="JavaScript">
var emailWindow;

function submitEmail() {
	var myform = document.emailSelectForm;
	var emails = ''; 
	var counter = 0;
	var currentUserEmail = '<%=wc.getUser().getEmail()%>';
    var answer = true;
    var doneflag = false;
    
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
	var subjectStr = '&subject=Steere Path Forward PLC Report Follow-up';	
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
		<td>
			
			<table class="no_space_width" style="font-size: .8em;" >
			 <tr>
				<td>&nbsp;</td>
				<td bgcolor="#ffd699" width="10px"></td>
				<td>&nbsp;Regional Manager</td>
				<td>&nbsp;</td>
				<td bgcolor="#c2d6eb" width="10px"></td>
				<td>&nbsp;District Manager</td>
			 </tr>
			</table>
		</td>
	</tr>
</table>
        <div>
	<div id="table_inst">
	<%-- Infosys code changes starts here
		<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/SPF/listReportPLC.do?downloadExcel=true&type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/SPF/listReportPLC.do?type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Back to default sort</a>
	 --%>
	 <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/SPF/listReportPLC?downloadExcel=true&type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/SPF/listReportPLC?type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Back to default sort</a>
	<%-- Infosys code changes ends here --%>
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
                <th nowrap><b>Detrol LA</b></th>
                <th nowrap><b>Chantix</b></th>
                <%--<th nowrap><b>Revatio</b></th>--%>
                <th nowrap><b>Spiriva</b></th>
                <th nowrap><b>Viagra</b></th>
                <th nowrap><b>General Session</b></th>
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
         <td><%=b%><%=(emp.getTeamCode()==null)?"":emp.getTeamCode()%><%=bb%></td>
				<td><%=b%><%=(emp.getDistrictDesc()==null)?"":emp.getDistrictDesc()%><%=bb%></td>
               <%--  Infosys code changes ends here	
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=emp.getEmplId()%>&m1=SPF&m2=PLC"><%=emp.getLastName()%> </a><%=bb%></td>
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=emp.getEmplId()%>&m1=SPF&m2=PLC"><%=emp.getFirstName()%></a><%=bb%></td>
				 --%>
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=emp.getEmplId()%>&m1=SPF&m2=PLC"><%=emp.getLastName()%> </a><%=bb%></td>
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=emp.getEmplId()%>&m1=SPF&m2=PLC"><%=emp.getFirstName()%></a><%=bb%></td>
				 <%--  Infosys code changes ends here	--%>
				
				<td><%=b%><%=emp.getRole()%><%=bb%></td>
				<td><%=b%><%=emp.getEmplId()%><%=bb%></td>
                <%if("Overall".equalsIgnoreCase(type)){
                    empHashMap=emp.getProductStatusMap();
                    
                    %>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("DETR"))%><%=bb%></td>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("CHTX"))%><%=bb%></td>
                    <%--<td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("RVTO"))%><%=bb%></td>--%>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("SPRV"))%><%=bb%></td>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("VIAG"))%><%=bb%></td>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("PLCA"))%><%=bb%></td>
                    <td ><%=b%><%=qString.getSection()%><%=bb%></td>
                
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
</html>