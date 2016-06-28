<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.POAReportList"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%-- <%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>  --%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>


<!-- <netui:html> -->

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
         POAReportList wc = (POAReportList)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);
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
	var subjectStr = '&subject=Powers Mid-POA1  Follow-up';	
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
		<%-- <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/POA/listreport.do?downloadExcel=true&type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/POA/listreport.do?type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Back to default sort</a>
	 --%>
	 <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/POA/listreport?downloadExcel=true&type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/POA/listreport?type=<%=qString.getType()%>&section=<%=qString.getSection()%>">Back to default sort</a>
	
	 
	 
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
                <th nowrap><b>Aricpet</b></th>
                <th nowrap><b>Celebrex</b></th>
                <th nowrap><b>Geodon</b></th>
                <th nowrap><b>Lyrica</b></th>
                <th nowrap><b>Rebif</b></th>
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
         <td><%=b%><%=emp.getTeamCode()%><%=bb%></td>
				<td><%=b%><%=(emp.getDistrictDesc()==null)?"":emp.getDistrictDesc()%><%=bb%></td>
                	
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/overview/detailreportPOA?emplid=<%=emp.getEmplId()%>"><%=emp.getLastName()%> </a><%=bb%></td>
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/overview/detailreportPOA?emplid=<%=emp.getEmplId()%>"><%=emp.getFirstName()%></a><%=bb%></td>
				<td><%=b%><%=emp.getRole()%><%=bb%></td>
				<td><%=b%><%=emp.getEmplId()%><%=bb%></td>
                <%if("Overall".equalsIgnoreCase(type)){
                    empHashMap=emp.getProductStatusMap();
                    
                    %>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("ARCP"))%><%=bb%></td>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("CLBR"))%><%=bb%></td>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("GEOD"))%><%=bb%></td>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("LYRC"))%><%=bb%></td>
                    <td bgcolor="#FFF39C"><%=b%><%=Util.toEmptyNA((String)empHashMap.get("REBF"))%><%=bb%></td>
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
<!-- </netui:html> -->
</html>