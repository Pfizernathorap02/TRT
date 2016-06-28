<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.RBUSearchBean"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.search.EmplSearchForm"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SearchFormRBUWc"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.VarianceReportBean"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.RBUResultsListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.VarianceReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.util.HashMap" %>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>

<netui:html> --%>

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
            Product Training (PSCPT) Search Results 
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
    <%
		EmplSearchForm form = new EmplSearchForm();
		FormUtil.loadObject(request,form);
    %>
<br>    
<br>

<div style="margin-left:10px;margin-right:10px">
<table class="no_space_width">
	<%-- <form name="searchForm" action="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/searchemployeerbu.do" class="form_basic" method="get">
	 --%>
	<form name="searchForm" action="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/searchemployeerbu" class="form_basic" method="get">
		
		
		<tr>
			<td><label>First Name:</label></td>
			<td><input class="text" type="text" name="<%=EmplSearchForm.FIELD_FNAME%>" value="<%=form.getFname()%>" size="20"></td>
		</tr>
		<tr>
			<td><label>Last Name:</label></td>
			<td><input  class="text" type="text" name="<%=EmplSearchForm.FIELD_LNAME%>" value="<%=form.getLname()%>" size="20"></td>
		</tr>
        <tr>
        <td>
        <i>or</i>
        </td>
        </tr>
        <tr> 
        <td>
            <label>Emplid:</label></td>
            <td><input  class="text" type="text" name="<%=EmplSearchForm.FIELD_EMPLID%>" value="<%=form.getEmplid()%>" size="20"></td>
        </tr>
         <tr>
        <td>
        <i>or</i>
        </td>
        </tr>
        
        <tr>
        <td>
            <label>Sales Position:&nbsp; </label></td>
            <td><input  class="text" type="text" name="<%=EmplSearchForm.FIELD_TERRITORYID%>" value="<%=form.getTerrId()%>" size="20"></td>
        </tr>
		<tr>
			<td>
            <input  type="hidden" name="fromSearch" value="true">
			</td>
			<td>
				<input type="submit" value="Search">	
			</td>
		</tr>		
	</form>
</table>
</div>
    <div style="margin-left:10px;margin-right:10px">
        <p>
        
        
         <%
         RBUResultsListWc wc = (RBUResultsListWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
         User user=wc.getUser();
        
         AppQueryStrings qString = new AppQueryStrings();
         FormUtil.loadObject(request,qString);
         String type=qString.getType();     
         %>
           
        <h3 style="MARGIN:15PX">
            Product Training (PSCPT) - Search Results Report
        </h3>

<%if(bDownloadExcel == false){%>
<div style="margin-left:10px;margin-right:10px">
	  <div id="table_inst">
		<%-- Infosys code changes starts here
		 <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/VarianceReport.do?downloadExcel=true">Download to Excel</a>        
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <a href="<%=AppConst.APP_ROOT%>/rbusreportselect.do">PSCPT Admin Reports</a> --%>
       <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/VarianceReport?downloadExcel=true">Download to Excel</a>        
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <a href="<%=AppConst.APP_ROOT%>/rbusreportselect">PSCPT Admin Reports</a>
	 <%-- Infosys code changes starts here --%>
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
        <th nowrap>Employee Status</th>            
        <th nowrap>Active</th>      
        <th nowrap>Future Role</th>            
        <th nowrap>Sales Position</th>
        <th nowrap>Current Products</th>
        <th nowrap>Future Products</th>		
        <th nowrap>Required Products</th>			
    </tr>
        <%
         RBUSearchBean[] employeeBean=wc.getEmployeeBean();
         RBUSearchBean emp;
         HashMap empHashMap=new HashMap();
         for(int i=0;i<employeeBean.length;i++){
         emp=employeeBean[i];         
         //System.out.println("first name " + emp.getFirstName());
         boolean doFlag = false;  // flag to determine of a row should be shown
         Integer integerSrno = new Integer(i+1);         
         %>
         <tr>
         <td nowrap><%=integerSrno.toString()%></td>
        <td nowrap><%=bDownloadExcel? Util.toEmptyNBSP(Util.formatStrExcel(emp.getEmplID())) : Util.toEmptyNBSP(emp.getEmplID())%></td>
         <%if (!bDownloadExcel) {%>
           <%--  Infosys code changes starts here
            <td nowrap><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=emp.getEmplID()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(emp.getFirstName())%></a></td>
            <td nowrap><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails.do?emplid=<%=emp.getEmplID()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(emp.getLastName())%></a></td>
         --%>
          <td nowrap><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=emp.getEmplID()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(emp.getFirstName())%></a></td>
          <td nowrap><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=emp.getEmplID()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(emp.getLastName())%></a></td>
         <%--  Infosys code changes ends here --%>
        
        
        
        <%} else {%>
            
            <td nowrap><%=Util.toEmptyNBSP(emp.getFirstName())%></td>
            <td nowrap><%=Util.toEmptyNBSP(emp.getLastName())%></td>
         <%}%>
         <td nowrap><%=Util.toEmptyNBSP(emp.getEmployee_Status())%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getActive())%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getFuture_Role())%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getSales_Position())%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getCurrent_products())%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getPostProduct())%></td>
         <td nowrap><%=Util.toEmptyNBSP(emp.getRequiredProducts())%></td>
       </tr>
    <%
         }
    %>
</table>
    </div>
    </body>
<%-- </netui:html> --%>
</html>