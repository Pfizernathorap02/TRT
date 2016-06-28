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
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
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
           Product Training (PSCPT) - Schedule Summary Report
        </title>
    </head>

    <script type="text/javascript" language="JavaScript">
    self="mainWin";
    </script>
   
    
    <body>
        <div style="margin-left:10px;margin-right:10px">
        <%
        TrainingScheduleWc wc = (TrainingScheduleWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        %>
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
            Product Training (PSCPT) - Schedule Summary Report
        </h3>        
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
               <%--  Infosys code changes starts here
               <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingSchedule.do?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/rbusreportselect.do">PSCPT Admin Reports</a> --%>
          <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingSchedule?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/rbusreportselect">PSCPT Admin Reports</a> 
                <%--  Infosys code changes ends here --%>
            </div>            
        </div>
                    
        <%}%>
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="7"><b>RBU - Training Schedule Summary Report</b></td>
        </tr>
        <tr></tr>
        <%}%>
        <tr>
            <th nowrap>Product</th>
        	<th nowrap>Start Date</th>
			<th nowrap>End Date</th>		
			<th nowrap>Guest Trainers</th>					
            <th nowrap>Representative Count</th>
            <th nowrap>Manager Count</th>
            <th nowrap>Total Trainee Count</th>
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
            <td>
            
            <%  if(oEmpReport.getGuestCount() >0){%>
            <%=oEmpReport.getGuestCount()%> 
           <%if (!downloadExcel) {%>
           <%--  Infosys code changes starts heres
           <a href="RBUGuestTrainerList.do?classid=<%=oEmpReport.getCourseId()%>&product=<%=oEmpReport.getProductDesc()%>&startdate=<%=Util.formatDateShort(oEmpReport.getStartDate())%>"> 
            --%>
            <a href="RBUGuestTrainerList?classid=<%=oEmpReport.getCourseId()%>&product=<%=oEmpReport.getProductDesc()%>&startdate=<%=Util.formatDateShort(oEmpReport.getStartDate())%>"> 
            <%--  Infosys code changes ends heres --%>
            View List  
            </a>
            <%}else{%>
            View List 
            <%}%>
            <%}else{%>
                <%if (!downloadExcel) {%>
           <a href="#" onclick="window.open('uploadGuest?classid=<%=oEmpReport.getCourseId().intValue()%>','_blank','width=400,height=250,scrollbars=yes'); return false;"><div > 
           0 Upload List 
           </a>
            <%}else{%>
                   0 Upload List
            <%}}%>
            
            </td>  
             <td>
                <%if (!downloadExcel) {%>
                <%-- Infosys code changes starts here
                <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList.do?<%=ClassFilterForm.FIELD_ISMANAGER%>=N&<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&<%=ClassFilterForm.FIELD_STARTDATE%>=<%=strStartDate%>&<%=ClassFilterForm.FIELD_ENDDATE%>=<%=strEndDate%>&ifproduct=true&ifdate=true">
               --%>
               <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList?<%=ClassFilterForm.FIELD_ISMANAGER%>=N&<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&<%=ClassFilterForm.FIELD_STARTDATE%>=<%=strStartDate%>&<%=ClassFilterForm.FIELD_ENDDATE%>=<%=strEndDate%>&ifproduct=true&ifdate=true">
                <%-- Infosys code changes ends here --%>
                <%}%>                
                <%=oEmpReport.getCount().intValue() - oEmpReport.getManager_count().intValue()%>
                <%if (!downloadExcel) {%>
                </a>
                <%}%>
            </td> 
            <td>
                <%if (!downloadExcel) {%>
                <%-- Infosys code changes starts here
                <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList.do?<%=ClassFilterForm.FIELD_ISMANAGER%>=Y&<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&<%=ClassFilterForm.FIELD_STARTDATE%>=<%=strStartDate%>&<%=ClassFilterForm.FIELD_ENDDATE%>=<%=strEndDate%>&ifproduct=true&ifdate=true">
                --%>
               <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList?<%=ClassFilterForm.FIELD_ISMANAGER%>=Y&<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&<%=ClassFilterForm.FIELD_STARTDATE%>=<%=strStartDate%>&<%=ClassFilterForm.FIELD_ENDDATE%>=<%=strEndDate%>&ifproduct=true&ifdate=true">
                 <%-- Infosys code changes ends here --%>
                <%}%>                
                <%=oEmpReport.getManager_count()%>
                <%if (!downloadExcel) {%>
                </a>
                <%}%>
            </td>           
            <td>
                <%if (!downloadExcel) {%>
               <%-- Infosys code changes starts here
                 <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList.do?<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&<%=ClassFilterForm.FIELD_STARTDATE%>=<%=strStartDate%>&<%=ClassFilterForm.FIELD_ENDDATE%>=<%=strEndDate%>&ifproduct=true&ifdate=true">
               --%>
                <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList?<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&<%=ClassFilterForm.FIELD_STARTDATE%>=<%=strStartDate%>&<%=ClassFilterForm.FIELD_ENDDATE%>=<%=strEndDate%>&ifproduct=true&ifdate=true">
                <%-- Infosys code changes ends here --%>
                <%}%>                
                <%=oEmpReport.getCount()%>
                <%if (!downloadExcel) {%>
                </a>
                <%}%>
            </td>
        </tr>
        <%if (productRowSpan == 1) {%>
        <tr bgcolor="#ffd699">
            <td colspan="5">&nbsp;</td>
            <td><b><%=oEmpReport.getProductDesc().toUpperCase()%> TOTAL</b></td>
            <td><b>
            <%if (!downloadExcel) {
                if(productTotal>1){%>
            
            <%--  Infosys code changes starts here
            <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList.do?<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&ifproduct=true&ifdate=false">
            <%}else{%>
            <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList.do?<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&ifproduct=true&ifdate=true">
            --%> 
            <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList?<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&ifproduct=true&ifdate=false">
            <%}else{%>
            <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList?<%=ClassFilterForm.FIELD_PRODUCT%>=<%=oEmpReport.getProductCode()%>&ifproduct=true&ifdate=true">
           <%-- Infosys code changes ends here --%>
            <%                
            }
            }%>
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
            <td colspan="7">&nbsp;</td>
        </tr>
        <tr bgcolor="#d6ebad">
            <td><b>Total Product Training(PSCPT) Trainees</b></td>
            <td colspan="5">&nbsp;</td>            
            <td><b>
            <%if (!downloadExcel) {%>
            <%--  Infosys code changes starts here
            <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList.do"> --%>
            <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList">
            <%-- Infosys code changes ends here --%>
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