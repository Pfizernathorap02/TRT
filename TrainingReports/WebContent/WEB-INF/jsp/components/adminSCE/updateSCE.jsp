<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.MenuList"%>
<%@ page import="com.pfizer.db.P2lTrack"%>
<%@ page import="com.pfizer.db.P2lTrackPhase"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.db.SCEList"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.EditMenuWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ListSCEWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ProductSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.ReportSelectWc"%>
<%@ page import="com.pfizer.webapp.wc.components.UpdateSCEWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%            
	UpdateSCEWc wc = (UpdateSCEWc)request.getAttribute(UpdateSCEWc.ATTRIBUTE_NAME);    
%>
<script type="text/javascript">
<!--
   function confirmSubmit() {    
      if(document.SCEList.courseCode.value.length==0||document.SCEList.eventID.value.length==0){
        alert("Course Code or SCE Event ID can not be blank");
        return false;
      }      
      return confirm('Do you want to continue ?');
    }
//-->
</script>

<table class="no_space_width" width="90%" height="0%"> 
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td colspan="=2">
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25">
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
        </td>
        <td>
        	<div class="breadcrumb">
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                <a href="/TrainingReports/adminSCE/begin">SCE Admin Tool</a> / 
                Update SCE
        	</div>
        </td>
	</tr>
</table>


<TABLE class="basic_table"> 
<TR>
<TD rowspan="2"><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD>
<TD align="left">
</TD>
</TR>
<TR><TD>

<TABLE class="basic_table"><TR><TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15" height="1"></TD>
<TD>
&nbsp;

<FORM action="" method="post" name="SCEList">
<TABLE width="80%">
<TR><TD>Course Code:</TD>
<TD><input type="text" name="courseCode" value="<%=wc.getCourseCode()%>"></TD></TR>
<TR><TD>SCE Event ID:</TD>
<TD><input type="text" name="eventID" value="<%=wc.getEventID()%>"></TD></TR>
<TR><TD colspan="2" align="left"> 
    <input type="submit" name="command" value="Update" onclick="return confirmSubmit()" >    
    <input type="reset" name="reset">
</TD></TR>
</TABLE>

<input type="hidden" name="exCourseCode" value="<%=wc.getCourseCode()%>">                         
</FORM>
</TD></TR>
</TABLE>

</TD></TR></TABLE>


    