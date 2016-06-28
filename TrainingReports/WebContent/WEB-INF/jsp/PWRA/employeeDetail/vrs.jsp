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
     String attendanceY = (attendance==null)?"":((attendance.equalsIgnoreCase("Y")?"":"checked"));    
    // String attendanceN = (attendance==null)?"":((attendance.equalsIgnoreCase("Y")?"":"checked"));    
     System.out.println("AttendanceY initial"+attendanceY);
     
     String attendanceDisable = "";    
     System.out.println("Attendance initial"+attendance);

     if(attendance!=null&&((attendance.equalsIgnoreCase("Y")))) {
        attendanceDisable = "DISABLED";        
 
     }
     System.out.println("Attendance after"+attendance);                   
     
%>         
    
    <TABLE class="blue_table" width="760px">    
    <TR><TH colspan="5" align="left">Vista RX Spiriva Status</TH></TR>
    <TR bgcolor="#DDDDDD">
    <TD><B>Course Name</B></TD>
    <TD><B>Course Code</B></TD>
    <TD><B>Score</B></TD>
    <TD><B>Status</B></TD>
    <TD><B>Completion Date</B></TD>
    </TR>    

    <%        
        for(int i=0;i<form.getVrsStatusInfo().size();i++){
        PDFHomeStudyStatus data = (PDFHomeStudyStatus)form.getVrsStatusInfo().elementAt(i);      
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
    if(document.vrsForm.attendance.checked){        
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
    <FORM action="" method="post" name="vrsForm">
    <TABLE class="blue_table" width="500px">    
    <TR><TH colspan="3" align="left">Attendance</TH></TR>
    <TR>
    <TD><B>Rep attended Vista RX training</B></TD>
    <TD><B>
    <input type="radio" name="attendance" value="Y" <%=attendanceY%> <%=attendanceDisable%>>
    Yes &nbsp;&nbsp;&nbsp;     
    </B></TD>
    <TD>
    <input type="submit" value="Submit" name="vrsattendance" <%=attendanceDisable%> onclick="return submitCommandAttendance()">
    </TD> 
    </TR>    
    <TR>     
    </TR>        
    </TABLE> 
    </FORM>            
