<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.TrainingScheduleWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%!
    private int getProductRowSpan(EmpReport[] arrEmpReport, int indx) {
        int productRowSpan = 1;
        String curProductCd = arrEmpReport[indx].getProductCode();
        if (arrEmpReport != null && arrEmpReport.length > indx+1) {
            for (int i=indx+1; i<arrEmpReport.length; i++) {
                if (arrEmpReport[i].getProductCode() != null && arrEmpReport[i].getProductCode().equals(curProductCd)) {
                    productRowSpan++;
                }
                else {
                    break;
                }
            }
        }        
        return productRowSpan;
    }
    
    private int getDateRowSpan(EmpReport[] arrEmpReport, int indx) {
        int dateRowSpan = 1;
        String curProductCd = arrEmpReport[indx].getProductCode();
        Date curStartDate = arrEmpReport[indx].getStartDate();
        Date curEndDate = arrEmpReport[indx].getEndDate();
        if (arrEmpReport != null && arrEmpReport.length > indx+1) {
            for (int i=indx+1; i<arrEmpReport.length; i++) {
                if ( (arrEmpReport[i].getProductCode() != null && arrEmpReport[i].getProductCode().equals(curProductCd))
                && ((arrEmpReport[i].getStartDate() != null && arrEmpReport[i].getStartDate().equals(curStartDate)) || (arrEmpReport[i].getStartDate() == null && curStartDate == null))
                && ((arrEmpReport[i].getEndDate() != null && arrEmpReport[i].getEndDate().equals(curEndDate)) || (arrEmpReport[i].getEndDate() == null && curEndDate == null))) 
                {
                    dateRowSpan++;
                }
                else {
                    break;
                }
            }
        }
        return dateRowSpan;
    }
%>

<%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");  
%>
<html>
    <head>
        <title>
            PDF - Training Schedule Summary Report
        </title>
    </head>

    <script type="text/javascript" language="JavaScript">
    </script>
   
    
    <body>
        <div style="margin-left:10px;margin-right:10px">
        <%
        TrainingScheduleWc wc = (TrainingScheduleWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        %>
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
            PDF - Training Schedule Summary Report
        </h3>        
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/PWRA/PDFHSReportTrainingSchedule?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/pdfhsreportselect">PDF Reports Home</a>
            </div>            
        </div>
                    
        <%}%>
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="5"><b>PDF - Training Schedule Summary Report</b></td>
        </tr>
        <tr></tr>
        <%}%>
        <tr>
            <th nowrap>Product</th>
        	<th nowrap>Start Date</th>
			<th nowrap>End Date</th>		
			<th nowrap>Team</th>		
			<th nowrap>Trainee Count By Team</th>
        </tr>
        <%
            int productRowSpan = 0;
            int productRowSpan1 = 0;
            int dateRowSpan = 0;
            int dateRowSpan1 = 0;
            
            int productTotal = 0;
            int grandTotal = 0;
            
            
            String strStartDate = "";
            String strEndDate = "";
            String trmColor="";
            
            EmpReport[] arrEmpReport = wc.getEmpReport();
            EmpReport oEmpReport;
            
            HashMap empHashMap=new HashMap();
            if (arrEmpReport != null) {
            for(int i=0; i<arrEmpReport.length; i++) {
                oEmpReport = arrEmpReport[i];               
                try {
                    strStartDate = format.format(oEmpReport.getStartDate());
                }
                catch (Exception e) {
                    strStartDate = "";
                }                  
                try {
                    strEndDate = format.format(oEmpReport.getEndDate());
                }
                catch (Exception e) {
                    strEndDate = "";
                }  
                
                String trClass = "";
                String b="<b>";
                String bb="</b>";
                
                //grandTotal += oEmpReport.getCount().intValue();
                if (i==0) {
                    grandTotal = oEmpReport.getTotalCount().intValue();
                }
                
                if (productRowSpan == 0) {
                    productRowSpan = getProductRowSpan(arrEmpReport, i);
                    productRowSpan1 = productRowSpan;                    
                    productTotal = 0;
                }
                productTotal += oEmpReport.getCount().intValue();
                
                if (dateRowSpan == 0) {
                    dateRowSpan = getDateRowSpan(arrEmpReport, i);
                    dateRowSpan1 = dateRowSpan;
                }
        %>
        <tr <%=trClass%> bgcolor="<%=trmColor%>">
            <%if (productRowSpan1 == productRowSpan) {%>
            <td rowspan="<%=productRowSpan%>" valign="top"><%=Util.toEmptyNBSP(oEmpReport.getProductDesc())%></td>
            <%}%>
            <%if (dateRowSpan1 == dateRowSpan) {%>
            <td rowspan="<%=dateRowSpan%>" valign="top"><%=Util.toEmptyNBSP(strStartDate)%></td>            
            <td rowspan="<%=dateRowSpan%>" valign="top"><%=Util.toEmptyNBSP(strEndDate)%></td>  
            <%}%>      
            <td><%=Util.toEmptyNA(oEmpReport.getTeamDesc())%></td>
            <td>
                <%if (!downloadExcel) {%>
                <a href="<%=AppConst.APP_ROOT%>/PWRA/PDFHSReportTrainingScheduleEmplList?<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&<%=ClassFilterForm.FIELD_STARTDATE%>=<%=strStartDate%>&<%=ClassFilterForm.FIELD_ENDDATE%>=<%=strEndDate%>&<%=ClassFilterForm.FIELD_TEAMCD%>=<%=oEmpReport.getTeamCode()%>">
                <%}%>                
                <%=oEmpReport.getCount()%>
                <%if (!downloadExcel) {%>
                </a>
                <%}%>
            </td>                    
        </tr>
        <%if (productRowSpan == 1) {%>
        <tr bgcolor="#ffd699">
            <td colspan="3">&nbsp;</td>
            <td><b><%=oEmpReport.getProductDesc().toUpperCase()%> TOTAL</b></td>
            <td><b>
            <%if (!downloadExcel) {%>
            <a href="<%=AppConst.APP_ROOT%>/PWRA/PDFHSReportTrainingScheduleEmplList?<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>">
            <%}%>
            <%=productTotal%>
            <%if (!downloadExcel) {%></a><%}%>
            </b></td>
        </tr>
        <%}%>
        <%
            productRowSpan--;
            dateRowSpan--;
            }
        %>
        <tr>
            <td colspan="5">&nbsp;</td>
        </tr>
        <tr bgcolor="#d6ebad">
            <td><b>Total PDF Trainees</b></td>
            <td colspan="3">&nbsp;</td>            
            <td><b>
            <%if (!downloadExcel) {%>
            <a href="<%=AppConst.APP_ROOT%>/PWRA/PDFHSReportTrainingScheduleEmplList">
            <%}%>
            <%=grandTotal%>            
            <%if (!downloadExcel) {%></a><%}%>
            </b></td>
        </tr>
        <%        
            }
        %>
        </table>
        </div>
    </body>
</html>