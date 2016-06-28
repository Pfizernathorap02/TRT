<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.RBUTrainingWeek"%>
<%@ page import="com.pfizer.webapp.AppConst"%>


<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
<LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
<%
List weeks = (List) request.getAttribute("weeks");
%>

<!-- <netui:html> -->
<html>
    <head>
        <title>
            Table Assignment Process     
        </title>
        <style type="text/css">
            body { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } td { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } th { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .bodystyle { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .small { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 9px; } .medium { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .big { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 16px; } .xbig { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 24px; } .expanded { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; line-height: 16px; letter-spacing: 2px; } .justified { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; text-align: justify; } .footer { font-family: "Times New Roman", Times, serif; font-size: 9px; color: #999999; } .box1 { padding: 3px; border-width: thin; border-style: solid; border-color: #CCCCCC #666666 #666666 #CCCCCC; } .box2 { font-style: italic; word-spacing: 2pt; padding: 3px; border-width: thin; border-style: solid; } .botline { border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #666666; } .pad { padding: 10px; } .mainNavOn { font-weight: bold; color: #FFFFFF; background-color: #FFFF00; } .navonlink { color: #FFFFFF; } .mainNavOff { font-weight: bold; color: #FFFFFF; background-color: #024c98; } .navOffLink { color: #FFFFFF; } .SecondaryNavOff { font-weight: bold; color: #000000; background-color: ffffcc; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .SecondaryNavOn { font-weight: bold; color: #000000; background-color: #FFFF00; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .popTitle { font-weight: bold; color: #000000; background-color: #9FBFDF; } .popSortLink { color: #000000; } .TblBrdr { border: 1px solid #333333; } .TDLeftBrdr { border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-left-style: solid; border-top-color: #333333; border-right-color: #333333; border-bottom-color: #333333; border-left-color: #333333; } .textBox { FONT-WEIGHT: normal; FONT-SIZE: 9pt; COLOR: black; FONT-FAMILY: Arial; } .trainingTable { border-top: 1px solid #333333; border-right: 1px solid #333333; border-bottom: 1px solid #333333; border-left: 1px solid #333333; } .trainingTable1 { border-bottom: 1px solid #333333; } .ActionsTableHeader { border: 1px solid #9FBDDA; font-family: Arial, Helvetica, sans-serif; font-size: 14px; font-weight: bold; background-color: #5C97C7; color: #FFFFFF; } .HeadingStyle{ FONT-WEIGHT: bold; FONT-SIZE: 11pt; COLOR: black; FONT-FAMILY: Arial; } .dataSection{ font-family: Arial; FONT-WEIGHT: NORMAL; FONT-SIZE: 9pt; }        
        </style>        
        
    </head>
    <script>
    function submitupdate()
{     
  
  if(window.confirm('Do you want to continue ?')) 
      document.weekselection.submit();
    window.close();
  
}


    </script>
    <body onload="window.focus();">    

        
      
       <%-- Infosys code changes starts here
       <FORM name="weekselection" id = "weekselection" action="<%=AppConst.APP_ROOT%>/RBU/RBUClassRoomReport.do" method="post" enctype="multipart/form-data"> --%>
        <FORM name="weekselection" id = "weekselection" action="<%=AppConst.APP_ROOT%>/RBU/RBUClassRoomReport" method="post" enctype="multipart/form-data">
         <%-- Infosys code changes ends here --%>
        <input type="hidden" name="week_id" id="week_id" value="1">
    
        <TABLE class="TblBrdr" cellpadding="5" cellspacing="0" width="100%"  height="100%" align="center">
        
        <TR> 
        <TD align="center" >             
        Choose a week :  <select name="week" onchange="week_id.value = week.options[week.selectedIndex].value;">
         <option value="0">Select Week</option>
        
        <%for(Iterator i = weeks.iterator(); i.hasNext();){
            RBUTrainingWeek week = (RBUTrainingWeek) i.next(); 
        %>             
             <option value="<%=week.getWeek_id()%>"><%=week.getWeek_name()%></option>
            
        <%}%>
        </select>    
        </TD>
        </TR>
        <TR> 
        <TD align="center" >        
        
        <input type="submit" name="submit" value="submit">
        </TD>
        </TR>
         </TBALE>
         </FORM>


    </body>
<!-- </netui:html> -->
</html>