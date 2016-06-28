<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.VarianceReportBean"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.VarianceReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.util.HashMap" %>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>--%>

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
            SPF Variance Report 
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
           
           
    
        <h3 style="MARGIN-TOP:15PX">
            SPF - Variance Report
        </h3>

<%if(bDownloadExcel == false){%>
<div style="margin-left:10px;margin-right:10px">
      <div id="table_inst">
        <%-- <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/SPF/SPFHSReportVariance.do?downloadExcel=true">Download to Excel</a>        
       --%>
       <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/SPF/SPFHSReportVariance?downloadExcel=true">Download to Excel</a>        
      
        &nbsp;&nbsp;|&nbsp;&nbsp;
      <%--   <a href="<%=AppConst.APP_ROOT%>/spfreportselect.do">SPF Reports Home</a> --%>
        <a href="<%=AppConst.APP_ROOT%>/spfreportselect">SPF Reports Home</a>
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
        <th nowrap>Pre SPF Cluster</th>                        
        <th nowrap>Pre SPF Team</th>            
        <th nowrap>Pre SPF Role</th>            
        <th nowrap>Pre SPF Product</th>
        <th nowrap>Post SPF Cluster</th>                        
        <th nowrap>Post SPF Team</th>            
        <th nowrap>Post SPF Role</th>            
        <th nowrap>Post SPF Prod</th>		
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
         <td nowrap><%=integerSrno.toString().trim()%></td>
         <td><%=bDownloadExcel? Util.toEmptyNBSP(Util.formatStrExcel(emp.getEmplID())) : Util.toEmptyNBSP(emp.getEmplID())%></td>
         <%if (!bDownloadExcel) {%>
         <%-- Infosys code changes start here
         <td nowrap><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=emp.getEmplID()%>&m0=report&m1=SPF"><%=emp.getFirstName()%></a></td>
         <td nowrap><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail.do?emplid=<%=emp.getEmplID()%>&m0=report&m1=SPF"><%=emp.getLastName()%></a></td>
          --%>
         <td nowrap><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=emp.getEmplID()%>&m0=report&m1=SPF"><%=emp.getFirstName()%></a></td>
         <td nowrap><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=emp.getEmplID()%>&m0=report&m1=SPF"><%=emp.getLastName()%></a></td>
          <%-- Infosys code changes ends here --%> 
         
         <%} else {%>
            <td><%=Util.toEmptyNBSP(emp.getFirstName())%></td>
            <td><%=Util.toEmptyNBSP(emp.getLastName())%></td>
         <%}%>
         <td nowrap><%=emp.getPreCluster()%></td>
         <td nowrap><%=emp.getPreTeam()%></td>
         <td nowrap><%=emp.getPreRole()%></td>
         <td nowrap><%=emp.getPreProduct()%></td>
         <td nowrap><%=emp.getPostCluster()%></td>
         <td nowrap><%=emp.getPostTeam()%></td>
         <td nowrap><%=emp.getPostRole()%></td>
         <td nowrap><%=emp.getPostProduct()%></td>
         <td nowrap><%=emp.getRequiredProducts()%></td>
       </tr>
    <%
         }
    %>
</table>
    </div>
    </body>
<!-- </netui:html> -->
</html>