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
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%            
	ListSCEWc wc = (ListSCEWc)request.getAttribute(ListSCEWc.ATTRIBUTE_NAME);
    Vector renderSCEList = wc.getSCEList();     
%>
<script type="text/javascript">
<!--
   function confirmDelete(idDel) {    
      var msg = "Do you want to continue ?";
      document.SCEList.delID.value = idDel;
      document.SCEList.command.value = "deleteSCE";
      if ( confirm(msg) ) {        
        document.SCEList.submit();
      }
    }
   function confirmSubmit() {    
      if(document.SCEList.courseCode.value.length==0||document.SCEList.eventID.value.length==0){
        alert("Course Code or SCE Event ID can not be blank");
        return false;
      }      
      return true;
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
                SCE Admin Tool
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

<TABLE class="basic_table"><TR><TD><IMG src="/TrainingReports/resources/images/spacer.gif" width="15" height="1"></TD><TD>
&nbsp;

<FORM action="" method="post" name="SCEList">
            <TABLE class="blue_table" width="90%">
            <TR><TH align="left">
                SCE                          
            </TH></TR>    
            
            <TR><TD><TABLE width="90%">
            <tr>
                <td style="border:0px">Course Code</td>
                <td style="border:0px">SCE Event ID</td>
                <td style="border:0px">Action</td>
            </tr>
            <%                                            
            for(int i=0;i<renderSCEList.size();i++){            
                SCEList list = (SCEList)renderSCEList.elementAt(i);                                                        
            %>
                <TR>
                <TD align="left" valign="top" style="border:0px">                                      
                <%=list.getCode()%>
                </TD>
                <TD align="left" valign="top" style="border:0px">                                      
                <%=list.getEventID()%>
                </TD>
                <TD align="left" valign="top" style="border:0px">                                      
                <a href="updateSCE?exCourseCode=<%=list.getCode()%>&exEventID=<%=list.getEventID()%>">
                [Edit] 
                </a>
                &nbsp;&nbsp;&nbsp;&nbsp; 
                <a href="javascript:confirmDelete('<%=list.getCode()%>')">
                [Delete]
                </a>
                </TD>
                </TR>
            <%}%>
            
            <TR>
                <TD style="border:0px" colspan="3">
                    Course Code: 
                    <input type="text" name="courseCode">
                    SCE Event ID:
                    <input type="text" name="eventID">                    
                    <input type="submit" name="addCommand" value="Add SCE" onclick="return confirmSubmit()">
                </TD>
            </TR>

            </TABLE>
             
        </TD></TR></TABLE>
         <input type="hidden" name="command" value="">                         
         <input type="hidden" name="delID" value="">                         
</FORM>
</TD></TR>
</TABLE>

</TD></TR></TABLE>


    