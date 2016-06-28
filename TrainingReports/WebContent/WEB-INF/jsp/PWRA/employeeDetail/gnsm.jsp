<%@ page import="com.pfizer.actionForm.PWRAGetEmployeeDetailForm"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.PDFHomeStudyStatus"%>
<%@ page import="com.pfizer.webapp.wc.components.report.PLCEmployeeDetailWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%
    //PLCEmployeeDetailWc wc = ;
PWRAGetEmployeeDetailForm form = ((PLCEmployeeDetailWc)request.getAttribute(PLCEmployeeDetailWc.ATTRIBUTE_NAME)).getFormBean();    
%>
     
<%        
     String attendance = form.getAttendacne();    
     String attendanceY = (attendance==null)?"":((attendance.equalsIgnoreCase("N")?"":"checked"));    
     String attendanceN = (attendance==null)?"":((attendance.equalsIgnoreCase("Y")?"":"checked"));    
     
     String mcAttendance = form.getMcAttendacne();            
     String mcAttendanceY = (mcAttendance==null)?"":((mcAttendance.equalsIgnoreCase("N")?"":"checked"));    
     //String mcAttendanceN = (mcAttendance==null)?"":((mcAttendance.equalsIgnoreCase("Y")?"":"checked"));    
        
     String attendanceDisable = "";    
     String mcDisabled = "DISABLED";    
     if(attendance!=null&&((attendance.equalsIgnoreCase("N")||(attendance.equalsIgnoreCase("Y"))))) {
        attendanceDisable = "DISABLED";        
        if(attendance.equalsIgnoreCase("N")&&(mcAttendance==null)){
            mcDisabled = "";    
        }
     }                   
     
     
     
    %>         
    
    <TABLE class="blue_table" width="760px">    
    <TR><TH colspan="5" align="left">Geodon National Sales Meeting Status</TH></TR>
    <TR bgcolor="#DDDDDD">
    <TD><B>Course Name</B></TD>
    <TD><B>Course Code</B></TD>
    <TD><B>Score</B></TD>
    <TD><B>Status</B></TD>
    <TD><B>Completion Date</B></TD>
    </TR>    

    <%        
        for(int i=0;i<form.getGnsmStatusInfo().size();i++){
        PDFHomeStudyStatus data = (PDFHomeStudyStatus)form.getGnsmStatusInfo().elementAt(i);
    %>
    <TR CLASS="<%=(i%2==0)?"even":"odd"%>">
    <TD><%=data.getProductDesc()%></TD>
    <TD><%=(data.getPedagogueExam()==null)?"Pedagogue":data.getPedagogueExam()%></TD>
    <TD><%=(data.getScore()==null)?"":data.getScore()%></TD>
    <TD><%=data.getStatus()%></TD>
    <TD><%=Util.formatDateShort(data.getCompletionDate())%></TD>
    </TR>
        <%}%>    
    </TABLE>
    <BR><BR>
    
<script language="javascript">
function submitCommandAttendance(){
    if(document.gnsmForm.attendance[0].checked||document.gnsmForm.attendance[1].checked){
        return window.confirm('Do you want to continue ?');
    }else{
        alert('Make A Selection First !');
        return false;
    } 
}
function submitCommandManagerCertificaion(){    
    if(document.gnsmForm.managerCertification.checked){                
        return window.confirm('Do you want to continue ?');
    }else{
        alert('Make A Selection First !');
        return false;
    }
}
</script>     
    
    <%--RENDER ATTENDANCE STATUS --%>    
    <FORM action="" method="post" name="gnsmForm">
    <TABLE class="blue_table" width="500px">    
    <TR><TH colspan="3" align="left">Attendance</TH></TR>
    <TR>
    <TD><B>Rep Attended Geodon NSM</B></TD>
    <TD><B>
    <input type="radio" name="attendance" value="Y" <%=attendanceY%> <%=attendanceDisable%>>
    Yes &nbsp;&nbsp;&nbsp;
    <input type="radio" name="attendance" value="N" <%=attendanceN%> <%=attendanceDisable%>>No    
    </B></TD>
    <TD>
    <input type="submit" value="Submit" name="commandattendance" <%=attendanceDisable%> onclick="return submitCommandAttendance()">
    </TD> 
    </TR>    
    <TR>
    <TD><B>My rep didn't attend Geodon NSM, and I've provided training</B></TD>
    <TD><B>
    <input type="radio" name="managerCertification" value="Y" <%=mcAttendanceY%> <%=mcDisabled%>>Yes &nbsp;&nbsp;&nbsp;    
    </B></TD>
    <TD>
    <input type="submit" value="Submit" name="commandmanagercertificaion" <%=mcDisabled%> onclick="return submitCommandManagerCertificaion()">
    </TD>
    </TR>    
    <TR>
    <TD><B>Compliance PPT Completion Status:</B></TD>
    <TD colspan="2">    
    <% if (Util.isEmpty(form.getPlAttendacne())){%>    
        N/A
    <%} else {%>
        <%=form.getPlAttendacne()%>
    <%}%>        
    </TD>    
    </TR>        
    </TABLE> 
    </FORM>            
