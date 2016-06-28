<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.actionForm.PWRATrainingScheduleListForm"%>
<%@ page import="com.pfizer.PWRA.EmployeeDetailFacade.TrainingScheduleList"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.Vector"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%
    PWRATrainingScheduleListForm form = (PWRATrainingScheduleListForm)request.getAttribute("trainingScheduleListForm");
    String mode0 = request.getParameter("m0")==null?"":request.getParameter("m0");
    String mode1 = request.getParameter("m1")==null?"":request.getParameter("m1");
    String mode2 = request.getParameter("m2")==null?"":request.getParameter("m2");
%>
<netui:html>
    <head>
        <title>
            Change Training Date            
        </title>
        <style type="text/css">
            body { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } td { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } th { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .bodystyle { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .small { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 9px; } .medium { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .big { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 16px; } .xbig { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 24px; } .expanded { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; line-height: 16px; letter-spacing: 2px; } .justified { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; text-align: justify; } .footer { font-family: "Times New Roman", Times, serif; font-size: 9px; color: #999999; } .box1 { padding: 3px; border-width: thin; border-style: solid; border-color: #CCCCCC #666666 #666666 #CCCCCC; } .box2 { font-style: italic; word-spacing: 2pt; padding: 3px; border-width: thin; border-style: solid; } .botline { border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #666666; } .pad { padding: 10px; } .mainNavOn { font-weight: bold; color: #FFFFFF; background-color: #FFFF00; } .navonlink { color: #FFFFFF; } .mainNavOff { font-weight: bold; color: #FFFFFF; background-color: #024c98; } .navOffLink { color: #FFFFFF; } .SecondaryNavOff { font-weight: bold; color: #000000; background-color: ffffcc; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .SecondaryNavOn { font-weight: bold; color: #000000; background-color: #FFFF00; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .popTitle { font-weight: bold; color: #000000; background-color: #9FBFDF; } .popSortLink { color: #000000; } .TblBrdr { border: 1px solid #333333; } .TDLeftBrdr { border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-left-style: solid; border-top-color: #333333; border-right-color: #333333; border-bottom-color: #333333; border-left-color: #333333; } .textBox { FONT-WEIGHT: normal; FONT-SIZE: 9pt; COLOR: black; FONT-FAMILY: Arial; } .trainingTable { border-top: 1px solid #333333; border-right: 1px solid #333333; border-bottom: 1px solid #333333; border-left: 1px solid #333333; } .trainingTable1 { border-bottom: 1px solid #333333; } .ActionsTableHeader { border: 1px solid #9FBDDA; font-family: Arial, Helvetica, sans-serif; font-size: 14px; font-weight: bold; background-color: #5C97C7; color: #FFFFFF; } .HeadingStyle{ FONT-WEIGHT: bold; FONT-SIZE: 11pt; COLOR: black; FONT-FAMILY: Arial; } .dataSection{ font-family: Arial; FONT-WEIGHT: NORMAL; FONT-SIZE: 9pt; }        
        </style>        
        
    </head>
    <body onload="window.focus();">    
        <form name="ChangeDate">    		
        <TABLE class="TblBrdr" width="300" cellpadding="5" cellspacing="0">
        <TR bgcolor="#DDDDDD">
        <TH>Product</TH>
        <TH>Date</TH>
        </TR>
        <TR> 
        <TD>
            <netui:label value="{request.trainingScheduleListForm.productName}"></netui:label>
        </TD>
        <TD>
            <select name="updateDate" class="textBox">
            
            <option value="">Select Date</option>
            <%                
                Vector list = form.getTrainingScheduleList(); 
                for(int i=0;i<list.size();i++){
                TrainingScheduleList data = (TrainingScheduleList)list.elementAt(i);
             %>
                <option value="<%=data.getCourseID()%>" ><%=Util.formatDateShort(data.getStartDate())%></option> 
            <%}%>                               
            </select>  
                    
        </TD>
        </TR>    
        <TR>
        <TD width="40%">&nbsp;</TD>
        <TD>
            <input type="button" name="Change" value="Change" onclick="javascript:if(document.ChangeDate.updateDate.selectedIndex!=0){ if(window.confirm('Do you want to continue ?')) window.opener.open('getEmployeeDetail?commandchangetime=t&emplid=<%=form.getEmplid()%>&ocourseID=<%=form.getOldCourseID()%>&m0=<%=mode0%>&m1=<%=mode1%>&m2=<%=mode2%>&courseID='+document.ChangeDate.updateDate.options[document.ChangeDate.updateDate.selectedIndex].value,'_self');window.close();}else{alert('Make A Selection First !');}">
            <input type="button" name="cancel" value="Cancel" onclick="window.close();">   
            
        </TD>
        </TR>        
        </TABLE>
        </form>
    </body>
</netui:html>