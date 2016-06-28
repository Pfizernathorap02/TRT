<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.pfizer.actionForm.AddGuestForm"%> --%>
<%@ page import="com.pfizer.action.RBUControllerAction.AddGuestForm" %>
<%-- <%@ page import="RBU.RBUController.AddGuestForm"%> --%>
<%@ page import="com.pfizer.webapp.AppConst"%>

<%@ page import="com.tgix.Utils.Util"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
<LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">

<%

  //  String action = request.getParameter("action")==null?"add":request.getParameter("action");
  //  String classid = request.getParameter("classid")==null?"":request.getParameter("classid");

    AddGuestForm form = (AddGuestForm)request.getAttribute("addGuestForm");
%>


<html>
    <head>
        <title>
            Add Guest         
        </title>
        <style type="text/css">
            body { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } td { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } th { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .bodystyle { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .small { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 9px; } .medium { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .big { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 16px; } .xbig { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 24px; } .expanded { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; line-height: 16px; letter-spacing: 2px; } .justified { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; text-align: justify; } .footer { font-family: "Times New Roman", Times, serif; font-size: 9px; color: #999999; } .box1 { padding: 3px; border-width: thin; border-style: solid; border-color: #CCCCCC #666666 #666666 #CCCCCC; } .box2 { font-style: italic; word-spacing: 2pt; padding: 3px; border-width: thin; border-style: solid; } .botline { border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #666666; } .pad { padding: 10px; } .mainNavOn { font-weight: bold; color: #FFFFFF; background-color: #FFFF00; } .navonlink { color: #FFFFFF; } .mainNavOff { font-weight: bold; color: #FFFFFF; background-color: #024c98; } .navOffLink { color: #FFFFFF; } .SecondaryNavOff { font-weight: bold; color: #000000; background-color: ffffcc; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .SecondaryNavOn { font-weight: bold; color: #000000; background-color: #FFFF00; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .popTitle { font-weight: bold; color: #000000; background-color: #9FBFDF; } .popSortLink { color: #000000; } .TblBrdr { border: 1px solid #333333; } .TDLeftBrdr { border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-left-style: solid; border-top-color: #333333; border-right-color: #333333; border-bottom-color: #333333; border-left-color: #333333; } .textBox { FONT-WEIGHT: normal; FONT-SIZE: 9pt; COLOR: black; FONT-FAMILY: Arial; } .trainingTable { border-top: 1px solid #333333; border-right: 1px solid #333333; border-bottom: 1px solid #333333; border-left: 1px solid #333333; } .trainingTable1 { border-bottom: 1px solid #333333; } .ActionsTableHeader { border: 1px solid #9FBDDA; font-family: Arial, Helvetica, sans-serif; font-size: 14px; font-weight: bold; background-color: #5C97C7; color: #FFFFFF; } .HeadingStyle{ FONT-WEIGHT: bold; FONT-SIZE: 11pt; COLOR: black; FONT-FAMILY: Arial; } .dataSection{ font-family: Arial; FONT-WEIGHT: NORMAL; FONT-SIZE: 9pt; }        
        </style>        
        
    </head>
    <script>
    function submitupdate()
{     
  if(document.guest.firstname.value.length<=0){
    alert('First Name Required !');
    return;
  }
  
   if(document.guest.lastname.value.length<=0){
    alert('Last Name Required !');
    return;
  }
  
  if(document.guest.email.value.length<=0){
    alert('Email Required !');
    return;
  }
  document.guest.target = "mainWin";
  if(window.confirm('Do you want to continue ?')) 
      document.guest.submit();
  window.close();
}

    </script>
    <body onload="window.focus();">    
        <!-- Infosys Code changes starts here
        <form name="guest" method="post"  target="" action="RBUGuestTrainerList.do">   -->
          <!-- Infosys Code changes ends  here -->
       <form name="guest" method="post"  target="" action="RBUGuestTrainerList">  
        <input type="hidden" name="classid" value="<%=form.getClassid()%>">
        
        <TABLE class="TblBrdr" cellpadding="5" cellspacing="0" width="100%"  height="100%" align="center">
        
        <TR> 
        <TD align="left" >       
        <table>       
        <%if(form.getFirstname() == null){%>               
         <input type="hidden" name="command" value="add">
        <tr> <td> Employee ID:</td><td><input type="text" name="emplid"></td></tr>
        
        <tr> <td>First name:</td><td><input type="text" name="firstname"></td><td>

        <tr> <td>Last name:</td><td><input type="text" name="lastname"></td><td>

        <tr> <td>Email Address:</td><td> <input type="text" name="email"></td><td>
  
       <tr> <td>NT Domain: </td><td><input type="text" name="nt_domain" ></td><td>
     
        <tr> <td>NT ID:</td><td> <input type="text" name="nt_id"></td><td>
        </table>
        </TD>
        </TR> 
        <TR>
        <TD>
            <input type="button" name="Add" value="Add" onclick="return submitupdate();">
            <input type="button" name="Cancel" value="Cancel" onclick="window.close();">               
        </TD>
        </TR>      
        <%}else {
              //  String emplid = request.getParameter("emplid")==null?"":request.getParameter("emplid");
            //    String firstname = request.getParameter("firstname")==null?"":request.getParameter("firstname");
            //    String lastname = request.getParameter("lastname")==null?"":request.getParameter("lastname");
            //    String email = request.getParameter("email")==null?"":request.getParameter("email");
        %> 
         <input type="hidden" name="command" value="edit">
         <table>
        <tr> <td>Employee ID:</td><td>   <input type="text" name="emplid" value="<%=form.getEmplid()%>" disabled='true'></td><td>
            
        <tr> <td>First name: </td><td>  <input type="text" name="firstname" value="<%=form.getFirstname()%>"></td><td>
            
        <tr> <td>Last name:  </td><td>   <input type="text" name="lastname" value="<%=form.getLastname()%>" ></td><td>
        
        <tr> <td>Email Address: </td><td><input type="text" name="email" value="<%=form.getEmail()%>"></td><td>
         
        <tr> <td>NT Domain: </td><td><input type="text" name="nt_domain" value="<%=form.getNt_domain()%>"></td><td>
         
        <tr> <td>NT ID: </td><td><input type="text" name="nt_id" value="<%=form.getNt_id()%>"></td><td>
        </table>
        </TD>
        </TR> 
        <TR>
        <TD>
            <input type="button" name="Change" value="Change" onclick="return submitupdate();">
            <input type="button" name="cancel" value="Cancel" onclick="window.close();">               
        </TD>
        </TR>      
        <%}%>

         
        </TABLE>
        </form>
    </body>
</html>