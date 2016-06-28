<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.actionForm.PWRATrainingScheduleListForm"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.TrainingScheduleList"%>
<%@ page import="com.tgix.Utils.Util"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
        <LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
        <LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">

<%
    PWRATrainingScheduleListForm form = (PWRATrainingScheduleListForm)request.getAttribute("trainingScheduleListForm");
    String mode0 = request.getParameter("m0")==null?"":request.getParameter("m0");
    String mode1 = request.getParameter("m1")==null?"":request.getParameter("m1");
    String mode2 = request.getParameter("m2")==null?"":request.getParameter("m2");
%>
<script>
function submitcancel()
{     
  document.CancelTraining.reason.value = document.CancelTraining.reason.value.replace(/^\s+|\s+$/g, '');
  if(document.CancelTraining.reason.value.length > 0)
  { 
    document.all.CancelTraining.target = "mainWin";
    if(window.confirm('Do you want to continue ?')) 
      document.all.CancelTraining.submit();
    window.close();
  }else{
    alert('Please Enter the Reason');
    document.CancelTraining.reason.value = '';
    limitText(document.CancelTraining.reason, document.CancelTraining.countdown,256);
    document.all.reason.focus();
  }
}

function limitText(limitField, limitCount, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} else {
		limitCount.value = limitNum - limitField.value.length;
	}
}

</script>
<netui:html>
    <head>
        <title>
            Cancel Training
        </title>
        <style type="text/css">
            body { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } td { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } th { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .bodystyle { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .small { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 9px; } .medium { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .big { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 16px; } .xbig { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 24px; } .expanded { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; line-height: 16px; letter-spacing: 2px; } .justified { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; text-align: justify; } .footer { font-family: "Times New Roman", Times, serif; font-size: 9px; color: #999999; } .box1 { padding: 3px; border-width: thin; border-style: solid; border-color: #CCCCCC #666666 #666666 #CCCCCC; } .box2 { font-style: italic; word-spacing: 2pt; padding: 3px; border-width: thin; border-style: solid; } .botline { border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #666666; } .pad { padding: 10px; } .mainNavOn { font-weight: bold; color: #FFFFFF; background-color: #FFFF00; } .navonlink { color: #FFFFFF; } .mainNavOff { font-weight: bold; color: #FFFFFF; background-color: #024c98; } .navOffLink { color: #FFFFFF; } .SecondaryNavOff { font-weight: bold; color: #000000; background-color: ffffcc; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .SecondaryNavOn { font-weight: bold; color: #000000; background-color: #FFFF00; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .popTitle { font-weight: bold; color: #000000; background-color: #9FBFDF; } .popSortLink { color: #000000; } .TblBrdr { border: 1px solid #333333; } .TDLeftBrdr { border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-left-style: solid; border-top-color: #333333; border-right-color: #333333; border-bottom-color: #333333; border-left-color: #333333; } .textBox { FONT-WEIGHT: normal; FONT-SIZE: 9pt; COLOR: black; FONT-FAMILY: Arial; } .trainingTable { border-top: 1px solid #333333; border-right: 1px solid #333333; border-bottom: 1px solid #333333; border-left: 1px solid #333333; } .trainingTable1 { border-bottom: 1px solid #333333; } .ActionsTableHeader { border: 1px solid #9FBDDA; font-family: Arial, Helvetica, sans-serif; font-size: 14px; font-weight: bold; background-color: #5C97C7; color: #FFFFFF; } .HeadingStyle{ FONT-WEIGHT: bold; FONT-SIZE: 11pt; COLOR: black; FONT-FAMILY: Arial; } .dataSection{ font-family: Arial; FONT-WEIGHT: NORMAL; FONT-SIZE: 9pt; }        
        </style>        
        
    </head>
    <body onload="window.focus(); document.CancelTraining.reason.value='';document.all.reason.focus();">    
        <form name="CancelTraining"   method="post"  target="" action="getEmployeeDetail?commandchangetime=cancelTraining&emplid=<%=form.getEmplid()%>&courseid=<%=form.getOldCourseID()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>">
        <TABLE class="TblBrdr" cellpadding="5" cellspacing="0" width="100%"  height="100%" align="center">
        
        <TR>
          <TH width="100%"  valign="middle" align="center">Please Enter Reason for cancellation.</TH>
        </TR>
        
        <TR> 
        <TD align="center">                        
            <textarea id="reason" name="reason" rows="4" cols="25"
                      onKeyDown="limitText(this.form.reason,this.form.countdown,256);" 
                      onKeyUp="limitText(this.form.reason,this.form.countdown,256);"            
            >
            </textarea>
            <br>
            <font size="1">(Maximum characters: 256)<br>
             You have <input readonly type="text" name="countdown" size="3" value="256"> characters left.</font>            
        </TD>
        </TR>    
        
        <TR>
        <TD align="center">
            <input type="button" name="Update" value="Update" onclick="return submitcancel();">                    
            &nbsp;&nbsp;&nbsp;<input type="button" name="cancel" value="Cancel" onclick="window.close();">               
        </TD>
        </TR>        
        </TABLE>
        </form>
    </body>
</netui:html>