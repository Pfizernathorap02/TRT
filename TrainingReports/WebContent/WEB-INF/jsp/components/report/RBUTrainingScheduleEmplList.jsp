<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.TrainingScheduleEmplListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%
    TrainingScheduleEmplListWc wc = (TrainingScheduleEmplListWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
    ClassFilterForm form = wc.getClassFilterForm();
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");      
    boolean enrollmentPage = ((form.getEnrollmentDate()==null||form.getEnrollmentDate().equals("")))?false:true;
    boolean bytrackPage = ((form.getClasses()==null||form.getClasses().equals("")))?false:true;
    boolean iffrombytrack = request.getParameter("iffrombytrack") != null && request.getParameter("iffrombytrack").equals("true");  
    boolean ifdate = request.getParameter("ifdate") != null && request.getParameter("ifdate").equals("true");    
    boolean ifproduct = request.getParameter("ifproduct") != null && request.getParameter("ifproduct").equals("true");  
    String track = request.getParameter("track");    
%>
<html>
    <head>
        <title>
            Product Training (PSCPT) - Schedule Detailed Report
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
  <div style="margin-left:10px;margin-right:10px">      
        
    <%if (bytrackPage){
            %>
        <h3 style="MARGIN-TOP:15PX">
            Product Training (PSCPT) - Schedule by Track Detailed Report
        </h3>
        <%}else{%>
            <h3 style="MARGIN-TOP:15PX">
            Product Training (PSCPT) - Schedule Detailed Report
             </h3>
        <%}%>
        <%if (track != null){                
        %>
            <b> 
            TRACK: <%=track%> <br>
                      
            <%if (ifdate){%>
            Start Date: <%=Util.toEmptyNBSP(format.format(wc.getEmpReport()[0].getStartDate()))%> <br>   
             End Date: <%=Util.toEmptyNBSP(format.format(wc.getEmpReport()[0].getEndDate()))%><br>
            <%}%>
            </b>
            <br>
            <br>
        <%}%>
        
        <%if (ifproduct){                
        %>
            <b> 
            Product: <%=Util.toEmptyNBSP(wc.getEmpReport()[0].getProductDesc())%> <br>
            <%if (ifdate){%>
            Start Date: <%=Util.toEmptyNBSP(format.format(wc.getEmpReport()[0].getStartDate()))%>  <br>   
            End Date: <%=Util.toEmptyNBSP(format.format(wc.getEmpReport()[0].getEndDate()))%><br>
            <%}%>
            </b>
        <%}%>
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
            <%
          
                if(!downloadExcel){
            %>  
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList?<%=request.getQueryString()%>&downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
            <%
                
                
                if(enrollmentPage){ 
                  
                    %>
                    <a href="<%=AppConst.APP_ROOT%>/PWRA/PDFEnrollmentSummaryReport">Back To Enrollment Summary</a>                    
                <%}else if(bytrackPage){%>
                     <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleByTrack">Back To Training Schedule by Track Summary</a>
                <%}else{%>
                    <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingSchedule">Back To Training Schedule Summary</a>
                <%}}%>        
                
            </div>             
</div> 
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="5"><b>Product Training (PSCPT) - Schedule Detailed Report</b></td>
        </tr>

        <%}%>

     <%if (downloadExcel) {%>
        <tr bgcolor="#1f61a9">
        <%} else{%>
        <tr>
        <%}%>
        <th nowrap>SlNo.</th>
        <%if (!ifproduct){%>            
            <th nowrap>Product</th>
        <%}%>	
        <%if (!ifdate){%>
        	<th nowrap>Start Date</th>
			<th nowrap>End Date</th>
         <%}%>
       
            <th nowrap>EMPLID</th>
            <th nowrap>First Name</th>
            <th nowrap>Last Name</th>
            <th nowrap>Future Role</th>
            <th nowrap>Future BU</th>
            <th nowrap>Future RBU</th>
            <th nowrap>Address</th>
            <th nowrap>Sales Position</th>
            <th nowrap>Future Manager</th>
			<th nowrap>HR Status</th>		
			<th nowrap>Field Status</th>
            <%if(enrollmentPage){%>
                <th nowrap>TRM Shipment Date</th>
                <th nowrap>Email Invitation Sent Date</th>
                <th nowrap>P2L Emrollment Date</th>
            <%}%>
        </tr>
        
        <%
            String strStartDate = "";
            String strEndDate = "";
            String TRMShipmentDate = "";
            String EmailInvitationSentDate = "";
            String P2LEmrollmentDate = "";
            
            EmpReport[] arrEmpReport = wc.getEmpReport();
            EmpReport oEmpReport;
            if (arrEmpReport != null)
            for(int i=0; i<arrEmpReport.length; i++) {
                oEmpReport = arrEmpReport[i];
                try {
                    if(oEmpReport.getStartDate() != null){
                        strStartDate = format.format(oEmpReport.getStartDate());
                    }
                }
                catch (Exception e) {
                    strStartDate = "";
                    e.printStackTrace();
                }                  
                try {
                    strEndDate = format.format(oEmpReport.getEndDate());
                }
                catch (Exception e) {
                    strEndDate = "";
                    e.printStackTrace();
                }                  
                try {
                    if(oEmpReport.getTRMShipmentDate() != null){
                        TRMShipmentDate = format.format(oEmpReport.getTRMShipmentDate());
                    }
                }
                catch (Exception e) {
                    TRMShipmentDate = "";
                    e.printStackTrace();
                }                  
                try {
                    if(oEmpReport.getEmailInvitationDate()!=null){
                        EmailInvitationSentDate = format.format(oEmpReport.getEmailInvitationDate());
                    }
                }
                catch (Exception e) {
                    EmailInvitationSentDate = "";
                    e.printStackTrace();
                }                  
                try {
                    if(oEmpReport.getP2LregistrationDate() != null){
                        P2LEmrollmentDate = format.format(oEmpReport.getP2LregistrationDate());
                    }
                }
                catch (Exception e) {
                    P2LEmrollmentDate = "";
                    e.printStackTrace();
                }                  
                
        %>
        <tr>
            <td><%=i+1%></td>
             <%if (!ifproduct){%>  
            <td><%=Util.toEmptyNBSP(oEmpReport.getProductDesc())%></td>
            <%}%>
             <%if (!ifdate){%>
            <td><%=Util.toEmptyNBSP(strStartDate)%></td>
            <td><%=Util.toEmptyNBSP(strEndDate)%></td>
            <%}%>
            <td><%=downloadExcel? Util.toEmptyNBSP(Util.formatStrExcel(oEmpReport.getEmplId())) : Util.toEmptyNBSP(oEmpReport.getEmplId())%></td>
            <%if (!downloadExcel) {%>
            <td><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(oEmpReport.getFirstName())%></a></td>
            <td><a href="<%=AppConst.APP_ROOT%>/RBU/employeeDetail/getEmployeeDetails?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=RBU"><%=Util.toEmptyNBSP(oEmpReport.getLastName())%></a></td>
            <%} else {%>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFirstName())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getLastName())%></td>
            <%}%>
            <td><%=Util.toEmptyNBSP(oEmpReport.getRole())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFutureBU())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFutureRBU())%></td>            
            <td><%=Util.toEmptyNBSP(oEmpReport.getFullAddress())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getTerritoryId())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getReportsToEmplid())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getEmplStatus())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFieldActive())%></td>            
            </tr>
        <%
            }          
        %>
        </table>
        </div>
    </body>
</html>