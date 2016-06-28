<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.VarianceReportBean"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.VarianceReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<!-- <netui:html> -->
<html>
<%boolean bDownloadExcel = false;
  String sExcelDownload = (String)request.getParameter("downloadExcel");
  if(sExcelDownload != null && sExcelDownload.equalsIgnoreCase("true"))
  {
    bDownloadExcel = true;
  }
%>
   
    <head>
        <title>
            Product Training (PSCPT) - Variance Report 
        </title>        
    </head>

<script src="/TrainingReports/resources/js/sorttable.js"></script>
<LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">

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
    <div style="margin-left:10px;margin-right:10px">
        <p>
        
        
         <%
         VarianceReportListWc wc = (VarianceReportListWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
         User user=wc.getUser();
        
         AppQueryStrings qString = new AppQueryStrings();
         FormUtil.loadObject(request,qString);
         String type=qString.getType();     
         %>
           
        <h3 style="MARGIN:15PX">
            Product Training (PSCPT) - Variance Report
        </h3>
        

<%if(bDownloadExcel == false){%>
<div style="margin-left:10px;margin-right:10px">
	  <div id="table_inst">
		<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/VarianceReport?downloadExcel=true">Download to Excel</a>        
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <a href="<%=AppConst.APP_ROOT%>/rbusreportselect">PSCPT Admin Reports</a>
	  </div>	  
</div>
<%}%>

<table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
<%if(bDownloadExcel == true){%>
  <tr bgcolor="#1f61a9">
<%}else{%>
  <tr>  
<%}%>    
        <th nowrap>Sl.No</th>        
        <th nowrap>Emp ID</th>        
        <th nowrap>First Name</th>		        
        <th nowrap>Last Name</th>				      
        <th nowrap>Current Role</th>            
        <th nowrap>Current Products</th> 
        <th nowrap>Future BU</th>     
        <th nowrap>Future RBU</th>
        <th nowrap>Future Role</th>            
        <th nowrap>Future Products</th>        
        <th nowrap>Credits</th>		
        <th nowrap>Required Products</th>			
    </tr>
        <%
         VarianceReportBean[] employeeBean=wc.getEmployeeBean();
         VarianceReportBean emp;
         HashMap empHashMap=new HashMap();
         for(int i=0;i<employeeBean.length;i++){
         emp=employeeBean[i];         
         boolean doFlag = false;  // flag to determine of a row should be shown
         Integer integerSrno = new Integer(i+1);         
         %>
         <tr>
         <td nowrap><%=integerSrno.toString()%></td>
        <td nowrap><%=bDownloadExcel? Util.toEmptyNBSP(Util.formatStrExcel(emp.getEmplID())) : Util.toEmptyNBSP(emp.getEmplID())%></td>
         <%if (!bDownloadExcel) {%>
         <td><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=emp.getEmplID()%>&m0=report&m1=RBU"><%=emp.getFirstName()%></a></td>
         <td><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=emp.getEmplID()%>&m0=report&m1=RBU"><%=emp.getLastName()%></a></td>
         <%} else {%>
            <td nowrap><%=Util.toEmptyNBSP(emp.getFirstName())%></td>
            <td nowrap><%=Util.toEmptyNBSP(emp.getLastName())%></td>
         <%}%>
         <td nowrap><%=emp.getPreRole()%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getPreProduct())%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getFutureBU())%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getFutureRBU())%></td>
         <td nowrap><%=emp.getPostRole()%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getPostProduct())%></td>         
         <td nowrap><%=Util.toEmptyNBSP(emp.getCredits())%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getRequiredProducts())%></td>
       </tr>
    <%
         }
    %>
</table>
    </div>
    </body>
<!-- </netui:html> -->
</html>