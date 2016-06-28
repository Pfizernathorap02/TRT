<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.RBUReportList"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="com.tgix.rbu.ProductDataBean"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%--
 <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>
 --%>
<%--<netui:html>--%>
    <head>
        <title>
            PSCPT Training Schedule
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
         RBUReportList wc = (RBUReportList)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
         User user=wc.getUser();
         ProductDataBean thisProductDataBean[];
        thisProductDataBean = wc.getProductDataBean();
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
             String reportto =session.getAttribute("emplid")==null?null:(String)session.getAttribute("emplid");
   
     // String type=qString.getType();
     // String section = qString.getSection();
      List examList = new ArrayList();
      if(!type.equalsIgnoreCase("OverAll")){
            if(session.getAttribute("examList") != null){
                examList = (List)session.getAttribute("examList");
                session.removeAttribute("examList");
            }
      }
      
           %>
    <script type="text/javascript" language="JavaScript">
var emailWindow;

function submitEmail() {
    var myform = document.forms[1];
	var emails = ''; 
	var counter = 0;
	var currentUserEmail = '<%=wc.getUser().getEmail()%>'; 
    var answer = true;
    var doneflag = false;
    var oneSelected = 0;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			if (myform.elements[i].checked) {
            oneSelected = 1;
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
	 if(oneSelected == 0){
        alert('Please select atleast one employee to send email.');
        return false;
    }
	var emailToStr = 'mailto:';	
	var ccString = '';
	var subjectStr = '&subject=Product Training (PSCPT) Follow-up';	
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

function checkAll(theElement) {
    var myform = document.forms[1];
	for(var i=0;i<myform.length;i++) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = true; 
		} 
	}	
}
function unCheckAll() {
	//var myform = document.emailSelectForm;
     var myform = document.forms[1];
	for(var i=0;i<myform.length;i++) { 
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
	</tr>
</table>
        <div>
                <%if(reportto ==null){%>
	<div id="table_inst">
		<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/listreport?downloadExcel=true&excelType=<%=type%>&excelSection=<%=section%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/RBU/listreport?type=<%=type%>&section=<%=section%>">Back to default sort</a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a  href="<%=AppConst.APP_ROOT%>/RBU/beginRBUChart?firstTime=true">PSCPT Reports</a>        
	</div>
    <%}%>
	<div class="top_table_buttons" style="float:right;">	
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll()" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="unCheckAll(this)" />
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_sendemail.gif" onclick="submitEmail(this)" />
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
			<%if("Overall".equalsIgnoreCase(type)){
                        if(thisProductDataBean != null){
                        for(int i=0;i<thisProductDataBean.length;i++){
                %>
                    <th  nowrap><b><%=thisProductDataBean[i].getProductDesc()%></b></th>            
            <%
                }
                }
            }
           if(!"Overall".equalsIgnoreCase(type)){
                Iterator iter = examList.iterator();
                while(iter.hasNext()){
            %>
            
            <th nowrap><b><%=(String)iter.next()%></b></th>
			<%
                }
           %>
            <th>OverAll</th>
            <%
            }
            %>
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
         <td><%=b%><%=Util.toEmptyNBSP(emp.getFutureBU())%><%=bb%></td>
         <td><%=b%><%=Util.toEmptyNBSP(emp.getFutureRBU())%><%=bb%></td>
				<%-- Infosys code Changes starts here
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=emp.getEmplId()%>&m0=report&m1=RBU"><%=emp.getLastName()%> </a><%=bb%></td>
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=emp.getEmplId()%>&m0=report&m1=RBU"><%=emp.getFirstName()%></a><%=bb%></td>
				 --%>
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=emp.getEmplId()%>&m0=report&m1=RBU"><%=emp.getLastName()%> </a><%=bb%></td>
				<td><%=b%><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=emp.getEmplId()%>&m0=report&m1=RBU"><%=emp.getFirstName()%></a><%=bb%></td>
				<%-- Infosys code Changes ends here --%>
				
				<td><%=b%><%=emp.getEmplId()%><%=bb%></td>
                <%if("Overall".equalsIgnoreCase(type)){
                    empHashMap=emp.getProductStatusMap();
                    
                     if(thisProductDataBean != null){
                        for(int k=0;k<thisProductDataBean.length;k++){
                    %>
                    <td bgcolor="#FFF39C"><%=b%> <%=Util.toEmptyNA((String)empHashMap.get(thisProductDataBean[k].getProductCd()))%><%=bb%></td>
                <%}
                     }
                }else{
                    
                    List examSocres = emp.getAvailableExams();
                    empHashMap=emp.getProductStatusMap();
                    if(examList != null)
                    {
                         Iterator iter = examList.iterator();
                        while(iter.hasNext()){
                            String exam = (String)iter.next();
                           // System.out.println("Exam >>>>>>>>>> " + exam);
                            Iterator examIter = examSocres.iterator();
                            boolean found = false;                            
                            boolean recordPresent = false;                          
                            while(examIter.hasNext()){
                                HashMap map = (HashMap)examIter.next();
                                if(map.size() > 0){
                                    recordPresent = true;
                                }
                                if(map.containsKey(exam)){
                                    found = true;
                                String score = Util.toEmptyBlank((String)map.get(exam));
                                String font ="black";
                                if(score != null && !exam.equals("SCE") && !score.equals("")){
                                    if(Integer.parseInt(score) < 80){
                                        font = "red";
                                    }
                                    else{
                                        font = "black";
                                    }
                                }
                       if(section != null && section.equals("On Leave")){         
                    %>
                    <td bgcolor="#FFF39C"><font color="<%=font%>"><%=b%><%=Util.toEmptyL((String)map.get(exam))%><%=bb%></font></td>
                    <%
                       }
                       else{
                        
                     %>
                     <td bgcolor="#FFF39C"><font color="<%=font%>"><%=b%><%=Util.toEmptyNC((String)map.get(exam))%><%=bb%></font></td>
                     
                     <%
                       }   
                                }
                            }
                       if(!found && !exam.equals("SCE")){
                       if(section != null && section.equals("On Leave")){              
                     %>           
                                <td bgcolor="#FFF39C"><font color="black"><%=b%>L<%=bb%></font></td>
                       <%  
                           }       
                          else{
                        %>    
                                  <td bgcolor="#FFF39C"><font color="black"><%=b%>NC<%=bb%></font></td>
                          
                        <%
                          }     
                            }    
                            if(!found && exam.equals("SCE")){
                               if(recordPresent){              
                             %>           
                                <td bgcolor="#FFF39C"><font color="black"><%=b%>N/A<%=bb%></font></td>
                               <%  
                                   }       
                                  else{
                                    if(section.equals("On Leave")){
                                %>    
                                  <td bgcolor="#FFF39C"><font color="black"><%=b%>L<%=bb%></font></td>
                                  
                                <%
                                    }
                                    else{
                                %>
                                <td bgcolor="#FFF39C"><font color="black"><%=b%>NC<%=bb%></font></td>
                                <%        
                                        
                                    }
                                  }
                            } 
                            
                               
                        }
                }
                %>
                
                 <td bgcolor="#FFE87C"><%=section%><%=bb%></td>
               <%  
                }    
                %> 
               <td><input name="MailSelectForm_email_<%=emp.getEmplId()%>" value="<%=emp.getEmail()%>"  type="checkbox">
              
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