<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.TrainingScheduleByTrack"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.TrainingScheduleByTrackWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.TrainingScheduleWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collections"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%!
    private int getTrackRowSpan(List report, int trackid) {
        int trackRowSpan = 0;
        List tmpreport = new ArrayList(report);
        for(Iterator i = tmpreport.iterator(); i.hasNext();){
            TrainingScheduleByTrack trackdata = (TrainingScheduleByTrack) i.next();
            if(trackid == trackdata.getTrack_id()){                
                trackRowSpan = trackRowSpan + trackdata.getRolegroupcount().size()+1;
            }
            
        }     
        if(trackRowSpan == 0)trackRowSpan=1;
        return trackRowSpan;
    }    

%>

<%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");  
%>
<html>
    <head>
        <title>
            Product Training (PSCPT) - Schedule By Track Report 
        </title>
    </head>

    <body>
        <div style="margin-left:10px;margin-right:10px">
        <%
        TrainingScheduleByTrackWc wc = (TrainingScheduleByTrackWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        List report = wc.getReport();    
        
        
        %>
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
            Product Training (PSCPT) - Schedule By Track Report
        </h3>        
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
               <%--  Infosys code changes starts here
               <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleByTrack.do?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/rbusreportselect.do">PSCPT Admin Reports</a> --%>
                 <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleByTrack?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/rbusreportselect">PSCPT Admin Reports</a>
                 <%--  Infosys code changes ends here --%>
            </div>            
        </div>
                    
        <%}%>
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="5"><b>Product Training (PSCPT) - Schedule By Track Report</b></td>
        </tr>
        <tr></tr>
        <%}%>
        <tr>
            <th nowrap>Track</th>
            <th nowrap>Products</th>
        	<th nowrap>Start Date</th>
			<th nowrap>End Date</th>
            <th nowrap>Role Group</th>		
			<th nowrap>Trainee Count by Role Group</th>
        </tr>
        <tr>
        <%
            int tmptrack=0;
            for(Iterator i= report.iterator(); i.hasNext();){
               TrainingScheduleByTrack trackdata = (TrainingScheduleByTrack) i.next();
               int productrows = trackdata.getRolegroupcount().size()+1;
               //int trackrows =productrows;
               int trackrows =getTrackRowSpan(report, trackdata.getTrack_id());
               
               
               if(trackdata.getTrack_id() != tmptrack){%>
                   <td rowspan="<%=trackrows%>"><%=trackdata.getTrack_desc()%></td>
               <%
                    tmptrack = trackdata.getTrack_id();
               }             

        %>
            <td rowspan="<%=productrows%>"><%=trackdata.getProductDesc()%> </td>
            <%
                if(trackdata.getStart_date() != null){
            %>
            <td rowspan="<%=productrows%>"><%=format.format(trackdata.getStart_date())%></td>
            <%
                }
                else{
            %>
             <td rowspan="<%=productrows%>"></td>
             <%
                }
             %>
            <%
                if(trackdata.getEnd_date() != null){
            %>
            <td rowspan="<%=productrows%>"><%=format.format(trackdata.getEnd_date())%></td>
            <%
                }
                else{
            %>
             <td rowspan="<%=productrows%>"></td>
             <%
                }
             %>
            

            <% 
            int row = 0;
            for(Iterator iter = trackdata.getRolegroupcount().entrySet().iterator(); iter.hasNext();){
                Map.Entry pairs = (Map.Entry)iter.next();    
                if(row > 0){
                    out.print("<tr>");
                }
                   
            %>
            
            <td><%=(String)pairs.getKey()%></td>           
            <td>
            <%if (!downloadExcel) {%>
            <%-- Infosys code changes starts here
            <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList.do?<%=ClassFilterForm.FIELD_ROLEGRROUP%>=<%=(String)pairs.getKey()%>&<%=ClassFilterForm.FIELD_CLASS%>=<%=trackdata.getClass_id()%>&<%=ClassFilterForm.FIELD_TRACK%>=<%=trackdata.getTrack_id()%>&track=<%=trackdata.getTrack_desc()%>&commandfrom=bytrack&ifdate=true">
            --%>
           <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList?<%=ClassFilterForm.FIELD_ROLEGRROUP%>=<%=(String)pairs.getKey()%>&<%=ClassFilterForm.FIELD_CLASS%>=<%=trackdata.getClass_id()%>&<%=ClassFilterForm.FIELD_TRACK%>=<%=trackdata.getTrack_id()%>&track=<%=trackdata.getTrack_desc()%>&commandfrom=bytrack&ifdate=true">
           <%-- Infosys code changes ends here --%>
            <%=((Integer)pairs.getValue()).intValue()%></a>
            <%}else{%>
            <%=((Integer)pairs.getValue()).intValue()%>
            <%}%>
            <%
                
                if(row > 0){
                    out.print("</tr>");
                }
                row++;
            }
            if(productrows ==1){
            %>      
            <td bgcolor="#ffd699">TOTAL</td><td bgcolor="#ffd699"> <%=trackdata.getTraineecount()%></td></tr>
            <%
            }else{
            %>
            <TR><td bgcolor="#ffd699">TOTAL</td><td bgcolor="#ffd699">        
            <b>
            <%if (!downloadExcel && trackdata.getTraineecount()>0) {%>
            <%-- Infosys code changes starts here
            <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList.do?<%=ClassFilterForm.FIELD_CLASS%>=<%=trackdata.getClass_id()%>&<%=ClassFilterForm.FIELD_TRACK%>=<%=trackdata.getTrack_id()%>&track=<%=trackdata.getTrack_desc()%>&commandfrom=bytrack&ifdate=true">
            --%>
           <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingScheduleEmplList?<%=ClassFilterForm.FIELD_CLASS%>=<%=trackdata.getClass_id()%>&<%=ClassFilterForm.FIELD_TRACK%>=<%=trackdata.getTrack_id()%>&track=<%=trackdata.getTrack_desc()%>&commandfrom=bytrack&ifdate=true">
           <%-- Infosys code changes ends here --%>
            <%}%>
            <%=trackdata.getTraineecount()%>        
            <%if (!downloadExcel) {%></a><%}%>
            </b>
         
         </td></TR>
         
            
            <%
            }
            %>


        <%
            }
        %>
        

        </table>
        </div>
    </body>
</html>