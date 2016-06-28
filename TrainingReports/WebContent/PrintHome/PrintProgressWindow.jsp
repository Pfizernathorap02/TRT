<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.tgix.printing.LoggerHelper"%>
<%@ page import="com.tgix.printing.PrintingConstants"%>
<%@ page import="com.tgix.printing.VelocityConvertor"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Iterator" %>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> 

<netui-data:getData resultId="invitationMap" value="{pageFlow.invitationMap}" />
<netui-data:getData resultId="invitationMapWithHSL" value="{pageFlow.invitationMapWithHSL}" />
<netui-data:getData resultId="invitationMapSpecial" value="{pageFlow.invitationMapSpecial}" />

--%>

<!-- <netui:html> -->
<html>
<!-- MeadCo Security Manager -->
<!--
<object viewastext style="display:none"
 classid="clsid:5445be81-b796-11d2-b931-002018654e2e"
 codebase="/TrainingReports/PrintHome/smsx.cab#Version=6,3,435,39">-->
 <object viewastext style="display:none"
 classid="clsid:5445be81-b796-11d2-b931-002018654e2e"
 codebase="/TrainingReports/PrintHome/smsx.cab#Version=6,4,438,19">
 <param name="GUID" value="{ED309F84-CC55-40D5-AC49-009DC8025CF9}">
 <param name="Path" value="/TrainingReports/PrintHome/sxlic.mlf">
 <param name="Revision" value="4">
 <param name="PerUser" value="true">
</object>

<!-- MeadCo ScriptX -->
<object viewastext id="factory" style="display:none"
 classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814">
</object>

    <head>
        <title>
        
            Product Training (PSCPT) - Print Progress
        </title>
        <LINK href="/EFTApplicationWeb/jsp/eft.css" type="text/css" rel="STYLESHEET">
        <LINK href="/EFTApplicationWeb/jsp/style.css" type="text/css" rel="STYLESHEET">
    </head>
    <script language="javascript">
         
        
        factory.printing.onbeforeunload ="Cancel Printing";         
            
        window.onunload = abort;
            
        function abort(){
            var j = factory.printing.printerControl(factory.printing.DefaultPrinter()).Jobs;
            var e = new Enumerator(j);
           // alert("There are " + j.Count + " jobs in the queue to be purged");
            oControl = factory.printing.printerControl(factory.printing.currentPrinter);
          //  oControl.Purge();
            //   alert("All Jobs Cleared");
          }//end of the Function abort
            
        function displayJobCount(){
           var j = factory.printing.printerControl(factory.printing.DefaultPrinter()).Jobs;
          // var nStatus=factory.printing.printerControl(factory.printing.DefaultPrinter()).status;
           alert("There are " + j.Count + " jobs in the queue.");
        }
        
        
        
        
        
        
        function CheckSpooling() {
          if ( !factory.printing.IsSpooling() ) {
          document.getElementById("print_status").style.display = 'none';
          document.getElementById("print_jobs").style.display = 'none';
          document.getElementById("print_finished").style.display = '';
          
          }
        setTimeout("CheckSpooling()", 3000);
            }
      
        
        function printWithNoPrompt(url,orderNumber){
            var originalHeader, originalFooter;
            factory.printing.SetMarginMeasure(2);
            factory.printing.bottomMargin="1.0"; 
            factory.printing.topMargin ="1.7";
            factory.printing.leftMargin = "1.25";
            factory.printing.rightMargin = "1.25";
            originalHeader = factory.printing.header;
            originalFooter = factory.printing.footer;			
            var oControl = factory.printing.printerControl(factory.printing.currentPrinter);
            //document.getElementById("PrintDoc").innerHTML=numJobs ;
            //alert(oControl.status);
            
            // Changes made for RBU
            factory.printing.header ="&b&bOrder#"+orderNumber;
            factory.printing.footer = "";
            factory.printing.portrait=true;  
          //factory.printing.OwnQueue();
            factory.printing.PrintHTML(url,false);
          // var j = factory.printing.printerControl(factory.printing.DefaultPrinter()).Jobs;
          
          //By using factory.printing.WaitForSpoolingComplete();-- factory.printing.onbeforeunload will not work ,when we close the process will continue sending  the jobs will be sent to the Spooler.
          // factory.printing.WaitForSpoolingComplete();
          
          //  document.getElementById("dsdsf").innerHTML=j.Count  ;
            //reset the header and footer
			factory.printing.header = originalHeader;
			factory.printing.footer = originalFooter;
            CheckSpooling();

         }
         
        
         
    </script>
    <body onload="window.focus();">
    
    <%
    response.setHeader("Cache-Control","no-store"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
    %>
    
    <div style="MARGIN:15PX" id="main_body" >
        <p>
            <table width="575" bordercolor="#9AB9D7" border="0" cellspacing="0">
        <tr>
        <td colspan="4" class="HeadingStyle">Printing Status&nbsp;&nbsp;</td>
        
        </tr>
        <tr>
        <td colspan="4">&nbsp;</td>
        </tr>
        <tr>
             <%
            String ALL_PROD=request.getAttribute("ALL_PROD")==null?"":request.getAttribute("ALL_PROD").toString();
            String Selected_Date=request.getAttribute("Selected_Date")==null?"":request.getAttribute("Selected_Date").toString();                      
        %>
            <td>Products:&nbsp;<b><%=ALL_PROD%>&nbsp;&nbsp;</b></td>
            <td>TRM Order Date:&nbsp;<b><%=Selected_Date%>&nbsp;&nbsp;</b></td>
          
        </tr>
        </table>
        <div id="print_status" style="">
         <br>
            <br>
            <b>Preparing for Printing Invitations.....<b>&nbsp;&nbsp;&nbsp;&nbsp;
            <b>&nbsp;&nbsp;&nbsp;<b>&nbsp;&nbsp;&nbsp;&nbsp;
            <br>
            <br>
            
        </div>
        
        <%
        //Now we will start Printing for the Employees in Invitation Map
      /*   Infosys code changes starts here
        TreeMap invitationMap=(TreeMap)pageContext.getAttribute("invitationMap");
        TreeMap invitationMapWithHSL=(TreeMap)pageContext.getAttribute("invitationMapWithHSL");
        TreeMap invitationMapSpecial=(TreeMap)pageContext.getAttribute("invitationMapSpecial"); */
        HttpSession sess=request.getSession(false);
        TreeMap invitationMap=	(TreeMap)request.getAttribute("invitationMap");
        TreeMap invitationMapWithHSL=(TreeMap)request.getAttribute("invitationMapWithHSL");
        TreeMap invitationMapSpecial=(TreeMap)request.getAttribute("invitationMapSpecial");
        		
       // Infosys code changes ends here 
        String path="";
        String orderNumber="";
        int  count=0;
        String content="";
        File labelFile;
        URL tempURL;
        String serverName = request.getRequestURL().toString();
        if(invitationMap != null){
        for(Iterator iter=invitationMap.keySet().iterator();iter.hasNext();){
           orderNumber= iter.next().toString();
           /*if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_staging)){
               path="http://trstg.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
               }
               // For testing 
               else if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_prod)){
               
               //path="http://trt.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
               path="http://trint.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
               //path="http://trstg.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
                }
               else{*/
                String sTemplateFolder = VelocityConvertor.getTemplateFolder(PrintingConstants.env_type);
                path = sTemplateFolder + File.separator + "CreatedLabels" + File.separator + PrintingConstants.INVITATION_OUTPUT_FILE + orderNumber.trim()+".html";                
                // Apply this only if the its on pfizer server
                /*if(serverName != null && serverName.indexOf("pfizer.com")  != -1){
                //path = "http://" + serverName + path;
                    path = "http://" + serverName + path;
                }*/
                 //if(serverName != null && serverName.indexOf("trint.pfizer.com") != -1){
                   if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){ 
                    path = PrintingConstants.APPLICATION_PATH_JSP_INT  + path;
                }
                else if(serverName != null && serverName.indexOf("wlsstg5.pfizer.com") != -1){
                //else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
                    path = PrintingConstants.APPLICATION_PATH_JSP_STG  + path;
                }
                //else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
                 else if(serverName != null && serverName.indexOf("wlsprd4.pfizer.com") != -1){   
                    path = PrintingConstants.APPLICATION_PATH_JSP_PROD  + path;
                } 
                else if (serverName.indexOf("localhost") != -1 || serverName.indexOf("amrnwlw058") != -1){ 
                    labelFile=new File(path);
                    tempURL=labelFile.toURL();
                    path=tempURL.toString();
                }
              // }
           count++;
          
          
           LoggerHelper.logSystemDebug("The Path here is" +path);
           %>
            <script>
            <%
              String sOrderNo = orderNumber;
              if(sOrderNo != null && sOrderNo.length() > 0)
              {                            
               sOrderNo = sOrderNo.trim();
               int iAfterPDF = sOrderNo.indexOf("RBU");
               if(iAfterPDF >= 0)
               orderNumber = sOrderNo.substring(iAfterPDF + 3);
              }            
            %>
             printWithNoPrompt('<%=path%>','<%=orderNumber%>');
             </script>
           <%
           
           
           }//end of the for loop
           }
           // Process for records with HSL
            if(invitationMapWithHSL != null){
            for(Iterator iter=invitationMapWithHSL.keySet().iterator();iter.hasNext();){
                orderNumber= iter.next().toString();
           /*if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_staging)){
               path="http://trstg.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
               }
               // For testing 
               else if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_prod)){
               
               //path="http://trt.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
               path="http://trint.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
               //path="http://trstg.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
                }
               else{*/
                String sTemplateFolder = VelocityConvertor.getTemplateFolder(PrintingConstants.env_type);
                path = sTemplateFolder + File.separator + "CreatedLabels" + File.separator + PrintingConstants.INVITATION_OUTPUT_FILE + orderNumber.trim()+".html";                
                // Apply this only if the its on pfizer server
                  if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){ 
                    path = PrintingConstants.APPLICATION_PATH_JSP_INT  + path;
                }
                else if(serverName != null && serverName.indexOf("wlsstg5.pfizer.com") != -1){
                //else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
                    path = PrintingConstants.APPLICATION_PATH_JSP_STG  + path;
                }
                //else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
                 else if(serverName != null && serverName.indexOf("wlsprd4.pfizer.com") != -1){   
                    path = PrintingConstants.APPLICATION_PATH_JSP_PROD  + path;
                } 
                else if (serverName.indexOf("localhost") != -1 || serverName.indexOf("amrnwlw058") != -1){ 
                    labelFile=new File(path);
                    tempURL=labelFile.toURL();
                    path=tempURL.toString();
                }
              // }
           count++;
          
          
           LoggerHelper.logSystemDebug("The Path here is" +path);
           %>
            <script>
            <%
              String sOrderNo = orderNumber;
              if(sOrderNo != null && sOrderNo.length() > 0)
              {                            
               sOrderNo = sOrderNo.trim();
               int iAfterPDF = sOrderNo.indexOf("RBU");
               if(iAfterPDF >= 0)
               orderNumber = sOrderNo.substring(iAfterPDF + 3);
              }            
            %>
             printWithNoPrompt('<%=path%>','<%=orderNumber%>');
             </script>
           <%
           
           
           }//end of the for loop
           }
           // Process for special case
            if(invitationMapSpecial != null){
        for(Iterator iter=invitationMapSpecial.keySet().iterator();iter.hasNext();){
           orderNumber= iter.next().toString();
           /*if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_staging)){
               path="http://trstg.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
               }
               // For testing 
               else if(PrintingConstants.env_type.equalsIgnoreCase(PrintingConstants.env_prod)){
               
               //path="http://trt.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
               path="http://trint.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
               //path="http://trstg.pfizer.com/TrainingReports/PrintHome/Templates/CreatedLabels//INV"+orderNumber.trim()+".html";
                }
               else{*/
                String sTemplateFolder = VelocityConvertor.getTemplateFolder(PrintingConstants.env_type);
                path = sTemplateFolder + File.separator + "CreatedLabels" + File.separator + PrintingConstants.INVITATION_OUTPUT_FILE + orderNumber.trim()+".html";                
                // Apply this only if the its on pfizer server
                /*if(serverName != null && serverName.indexOf("pfizer.com")  != -1){
                //path = "http://" + serverName + path;
                    path = "http://" + serverName + path;
                }*/
                 //if(serverName != null && serverName.indexOf("trint.pfizer.com") != -1){
                   if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){ 
                    path = PrintingConstants.APPLICATION_PATH_JSP_INT  + path;
                }
                else if(serverName != null && serverName.indexOf("wlsstg5.pfizer.com") != -1){
                //else if(serverName != null && serverName.indexOf("trstg.pfizer.com") != -1){
                    path = PrintingConstants.APPLICATION_PATH_JSP_STG  + path;
                }
                //else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
                 else if(serverName != null && serverName.indexOf("wlsprd4.pfizer.com") != -1){   
                    path = PrintingConstants.APPLICATION_PATH_JSP_PROD  + path;
                } 
                else if (serverName.indexOf("localhost") != -1 || serverName.indexOf("amrnwlw058") != -1){ 
                    labelFile=new File(path);
                    tempURL=labelFile.toURL();
                    path=tempURL.toString();
                }
              // }
           count++;
          
          
           LoggerHelper.logSystemDebug("The Path here is" +path);
           %>
            <script>
            <%
              String sOrderNo = orderNumber;
              if(sOrderNo != null && sOrderNo.length() > 0)
              {                            
               sOrderNo = sOrderNo.trim();
               int iAfterPDF = sOrderNo.indexOf("RBU");
               if(iAfterPDF >= 0)
               orderNumber = sOrderNo.substring(iAfterPDF + 3);
              }            
            %>
             printWithNoPrompt('<%=path%>','<%=orderNumber%>');
             </script>
           <%
           
           
           }//end of the for loop
           }
           
           %>
        <div id="print_jobs" style="">
        <script language="javascript">
        //Initiatly Status Will be Displayed , but once we are done printing we will hide the Top Most Div and Display Completion Message
        document.getElementById("print_status").style.display = 'none';
        
        </script>
        <br>
            <br>
            <b>Printing <b>&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button"    value="Show Printing Status"  onclick="displayJobCount();" name="printStatus">
            <input type="button"    value="Cancel Printing"  onclick="window.close();" name="printFinish">
            <br>
            <br>
        </div>
        
        <div id="print_finished"  style="display:none">
        <br>
            <br>
            <b>Printing Completed<b>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"    value="Close Window"  onclick="window.close();" name="printFinish">
            <b>&nbsp;&nbsp;&nbsp;<b>&nbsp;&nbsp;&nbsp;&nbsp;
            <br>
            <br>
            
        </div>
       
    </div>
    </body>
    </html>
<!-- </netui:html> -->