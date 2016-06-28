<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.pfizer.actionForm.RBUTrainingScheduleListForm"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.TrainingScheduleList"%>

<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
        <LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
        <LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
<%
RBUTrainingScheduleListForm form = (RBUTrainingScheduleListForm)request.getAttribute("trainingScheduleListForm");
    String mode0 = request.getParameter("m0")==null?"":request.getParameter("m0");
    String mode1 = request.getParameter("m1")==null?"":request.getParameter("m1");
    String mode2 = request.getParameter("m2")==null?"":request.getParameter("m2");
    
%>
<script>
function submitupdate()
{     

  if(document.ChangeDate.reason.value.length <= 0){
    alert('Please Enter the Reason');
    document.all.reason.focus();
    return;
  }
  if(document.ChangeDate.updateDate.selectedIndex==0){
    alert('Make A Selection First !');
    return;
  }
  
  document.ChangeDate.reason.value = document.ChangeDate.reason.value.replace(/^\s+|\s+$/g, '');
  document.ChangeDate.courseID.value = document.ChangeDate.updateDate.options[document.ChangeDate.updateDate.selectedIndex].value;
  document.all.ChangeDate.target = "mainWin";
  if(window.confirm('Do you want to continue ?')) 
      document.all.ChangeDate.submit();
  window.close();
}

function limitText(limitField, limitCount, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} else {
		limitCount.value = limitNum - limitField.value.length;
	}
}
</script>
<!-- <netui:html> -->
<html>
    <head>
        <title>
            Add Training             
        </title>
        <style type="text/css">
            body { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } td { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } th { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .bodystyle { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .small { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 9px; } .medium { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .big { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 16px; } .xbig { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 24px; } .expanded { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; line-height: 16px; letter-spacing: 2px; } .justified { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; text-align: justify; } .footer { font-family: "Times New Roman", Times, serif; font-size: 9px; color: #999999; } .box1 { padding: 3px; border-width: thin; border-style: solid; border-color: #CCCCCC #666666 #666666 #CCCCCC; } .box2 { font-style: italic; word-spacing: 2pt; padding: 3px; border-width: thin; border-style: solid; } .botline { border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #666666; } .pad { padding: 10px; } .mainNavOn { font-weight: bold; color: #FFFFFF; background-color: #FFFF00; } .navonlink { color: #FFFFFF; } .mainNavOff { font-weight: bold; color: #FFFFFF; background-color: #024c98; } .navOffLink { color: #FFFFFF; } .SecondaryNavOff { font-weight: bold; color: #000000; background-color: ffffcc; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .SecondaryNavOn { font-weight: bold; color: #000000; background-color: #FFFF00; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .popTitle { font-weight: bold; color: #000000; background-color: #9FBFDF; } .popSortLink { color: #000000; } .TblBrdr { border: 1px solid #333333; } .TDLeftBrdr { border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-left-style: solid; border-top-color: #333333; border-right-color: #333333; border-bottom-color: #333333; border-left-color: #333333; } .textBox { FONT-WEIGHT: normal; FONT-SIZE: 9pt; COLOR: black; FONT-FAMILY: Arial; } .trainingTable { border-top: 1px solid #333333; border-right: 1px solid #333333; border-bottom: 1px solid #333333; border-left: 1px solid #333333; } .trainingTable1 { border-bottom: 1px solid #333333; } .ActionsTableHeader { border: 1px solid #9FBDDA; font-family: Arial, Helvetica, sans-serif; font-size: 14px; font-weight: bold; background-color: #5C97C7; color: #FFFFFF; } .HeadingStyle{ FONT-WEIGHT: bold; FONT-SIZE: 11pt; COLOR: black; FONT-FAMILY: Arial; } .dataSection{ font-family: Arial; FONT-WEIGHT: NORMAL; FONT-SIZE: 9pt; }        
        </style>        
        
    </head>
    <body onload="window.focus();">    
        <form name="ChangeDate" method="post"  target="" action="getEmployeeDetails">  
        <input type="hidden" name="commandchangetime" value="addTraining">
        <input type="hidden" name="emplid" value="<%=form.getEmplid()%>">
        <input type="hidden" name="courseID" value="<%=form.getOldCourseID()%>">  		
        <TABLE class="TblBrdr" cellpadding="5" cellspacing="0" width="100%"  height="100%" align="center">
        <TR bgcolor="#DDDDDD">
        <TH>Product</TH>
        <TH>Date</TH>
        </TR>
        <TR> 
        <TD>
            <%=form.getProductName()%>
        </TD>
        <TD>
            <select name="updateDate" class="textBox">
            
            <option value="">Select Date</option>
            <%                
                Vector list = form.getTrainingScheduleList(); 
                for(int i=0;i<list.size();i++){
                	TrainingScheduleList data = (TrainingScheduleList)list.elementAt(i);
             %>
                <option value="<%=data.getCourseID()%>" ><%=Util.formatDateShort(data.getStartDate())%>- <%=data.getCourseName()%></option> 
            <%}%>                               
            </select>  
                    
        </TD>
        </TR>    
        <TR>
          <TH width="100%"  valign="middle" align="center" colspan="2">Please Enter Reason for update</TH>
        </TR>
        
        <TR> 
        <TD align="center" colspan="2">                        
            <textarea id="reason" name="reason" rows="4" cols="25"
                      onKeyDown="limitText(this.form.reason,this.form.countdown,256);" 
                      onKeyUp="limitText(this.form.reason,this.form.countdown,256);"            
            ></textarea>
            <br>
            <font size="1">(Maximum characters: 256)<br>
             You have <input readonly type="text" name="countdown" size="3" value="256"> characters left.</font>            
        </TD>
        </TR> 
        <TR>
        <TD colspan="2">
            <input type="button" name="Change" value="Change" onclick="return submitupdate();">
            <input type="button" name="cancel" value="Cancel" onclick="window.close();">   
            
        </TD>
        </TR>        
        </TABLE>
        </form>
    </body>
<!-- </netui:html> -->
</html>