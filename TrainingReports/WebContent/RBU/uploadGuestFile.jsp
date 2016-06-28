<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.actionForm.AddGuestForm"%>
<%@ page import="com.pfizer.actionForm.UploadGuestForm"%>
<%@ page import="com.pfizer.hander.RBUSHandler"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>

<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
<LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">

<%    
    String command = request.getParameter("command");
    String classid = request.getParameter("classid");
    String returncode = "Succeed";
    
   
    UserSession  uSession = UserSession.getUserSession(request);
    User user = uSession.getUser();


    if(command != null && command.equals("guestupload")){
            if (ServletFileUpload.isMultipartContent(request)){
            try{
            
            ServletFileUpload fu = new ServletFileUpload(new DiskFileItemFactory());
            
            List fileItems = fu.parseRequest(request);
            Iterator itr = fileItems.iterator();
            FileItem toprocessed = null;
    
            while(itr.hasNext()) {
              FileItem fi = (FileItem)itr.next();
    
              //Check if not form field so as to only handle the file inputs
              //else condition handles the submit button input
              if(!fi.isFormField()) {
                System.out.println("\nNAME: "+fi.getName());
                System.out.println("SIZE: "+fi.getSize());
                toprocessed = fi;
              }
              else {
             
                System.out.println("file processing - " + fi.getFieldName());
              }
              System.out.println("classid - " + classid);
              RBUSHandler rbuhandler = new RBUSHandler();
              returncode = rbuhandler.processGuestFile(classid, user.getId(), toprocessed);
            }
            }catch(Exception e){
                e.printStackTrace();
            }
            }else{
                System.out.println("wrong http request format");
            }
        
        
        }
%>


<netui:html>
    <head>
        <title>
            Upload Guest  File      
        </title>
        <style type="text/css">
            body { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } td { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } th { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .bodystyle { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .small { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 9px; } .medium { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; } .big { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 16px; } .xbig { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 24px; } .expanded { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; font-size: 12px; line-height: 16px; letter-spacing: 2px; } .justified { font-family: Tahoma, Geneva, Arial, Helvetica, sans-serif; text-align: justify; } .footer { font-family: "Times New Roman", Times, serif; font-size: 9px; color: #999999; } .box1 { padding: 3px; border-width: thin; border-style: solid; border-color: #CCCCCC #666666 #666666 #CCCCCC; } .box2 { font-style: italic; word-spacing: 2pt; padding: 3px; border-width: thin; border-style: solid; } .botline { border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #666666; } .pad { padding: 10px; } .mainNavOn { font-weight: bold; color: #FFFFFF; background-color: #FFFF00; } .navonlink { color: #FFFFFF; } .mainNavOff { font-weight: bold; color: #FFFFFF; background-color: #024c98; } .navOffLink { color: #FFFFFF; } .SecondaryNavOff { font-weight: bold; color: #000000; background-color: ffffcc; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .SecondaryNavOn { font-weight: bold; color: #000000; background-color: #FFFF00; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #333333; } .popTitle { font-weight: bold; color: #000000; background-color: #9FBFDF; } .popSortLink { color: #000000; } .TblBrdr { border: 1px solid #333333; } .TDLeftBrdr { border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-left-style: solid; border-top-color: #333333; border-right-color: #333333; border-bottom-color: #333333; border-left-color: #333333; } .textBox { FONT-WEIGHT: normal; FONT-SIZE: 9pt; COLOR: black; FONT-FAMILY: Arial; } .trainingTable { border-top: 1px solid #333333; border-right: 1px solid #333333; border-bottom: 1px solid #333333; border-left: 1px solid #333333; } .trainingTable1 { border-bottom: 1px solid #333333; } .ActionsTableHeader { border: 1px solid #9FBDDA; font-family: Arial, Helvetica, sans-serif; font-size: 14px; font-weight: bold; background-color: #5C97C7; color: #FFFFFF; } .HeadingStyle{ FONT-WEIGHT: bold; FONT-SIZE: 11pt; COLOR: black; FONT-FAMILY: Arial; } .dataSection{ font-family: Arial; FONT-WEIGHT: NORMAL; FONT-SIZE: 9pt; }        
        </style>        
        
    </head>
    <script>
    function submitupdate()
{     
  
  if(window.confirm('Do you want to continue ?')) 
      document.filesForm.submit();
  
}


    </script>
    <body onload="window.focus();">    
    <% if(command != null && command.equals("guestupload")){%>

       <TABLE class="TblBrdr" cellpadding="5" cellspacing="0" width="100%"  height="100%" align="center">
        
        <TR> 
        <TD align="center" >             
               <%=returncode%><br>
                <form name = "closew" action="">
                <input type="button" name="Close" value="Close" onclick="window.close()">      
                </form>
        </TD>
        </TR>
         </TBALE>
         
      <%}else{
        UploadGuestForm form = (UploadGuestForm)request.getAttribute("uploadGuestForm");   
        System.out.println("getting form");
        %>     
        
      
       <FORM name="filesForm" action="uploadGuestFile.jsp?command=guestupload&classid=<%=form.getClassid()%>" method="post" enctype="multipart/form-data">
             
    
        <TABLE class="TblBrdr" cellpadding="5" cellspacing="0" width="100%"  height="100%" align="center">
        
        <TR> 
        <TD align="center" >             
        Guest File in CSV :<input type="file" name="file1" accept="text/html"/><br/>            
        </TD>
        </TR>
        <TR> 
        <TD align="center" >  
        *Required Columns: FirstName, LastName, EMPLID, Email, NT_Domain, NT_ID<br>
        If you have an excel spreadsheet, please save it as a csv file before uploading it. <br>   
       <input type="button" name="Upload" value="Upload" onclick="return submitupdate();">
        
    
        </TD>
        </TR>
         </TBALE>
         </FORM>
      <%}%>

    </body>
</netui:html>