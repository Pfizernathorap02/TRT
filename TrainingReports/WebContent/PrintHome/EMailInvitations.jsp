<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"%>
 --%>
<%@ page import="com.tgix.printing.RBUBoxDataBean"%>
<%@ page import="com.tgix.printing.TRMOrderDateBean"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.tgix.printing.PrintingConstants;" %>
<%-- <%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<!-- <netui:html> --> 
<html>
    <head>
        <title>
            Product Training (PSCPT) - Email Invitation
        </title>
    <LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
    <LINK href="/TrainingReports/resources/css/style.css" type="text/css" rel="STYLESHEET">
    <LINK href="/TrainingReports/resources/css/header.css" type="text/css" rel="STYLESHEET">           
    </head>
    <script src="/TrainingReports/resources/js/OpenPopUp.js"></script>
    <script src="/TrainingReports/resources/js/OpenEmployeeList.js"></script>    
    <script language="javascript">
      
      function sendEMail()
      {	
        /* //var the_form = window.document.forms[0];   */     
        var the_form = window.document.forms[0];
      var user_selected_date = document.getElementById("TRMorderDate").value;
      if(user_selected_date == '-1') {
        alert('Please select a TRM Order Date or select ALL option for all the dates to continue.');
        return false;
      }
      var user_selected_box=0;
        var rads =the_form.RBUBox;
        if(rads != null){
            for(var i=0;i<rads.length;i++){
                if(the_form.RBUBox[i].checked == true){
                    user_selected_box= the_form.RBUBox[i].value;
                    }
            }
        }
       
      if( user_selected_box == 0){
        alert('Please select a Box to continue.');
        return false;
      }    
        var r=window.confirm("Please confirm to send email.");
        if (r)
        {        
        	<%-- <!--  var url='<%=PageflowTagUtils.getRewrittenFormAction("sendEmailInvitation", pageContext)%>?Selected_Date='+encodeURIComponent(user_selected_date)+'&Selected_Box='+encodeURIComponent(user_selected_box); --> --%>
         
           var url='/TrainingReports/PrintHome/sendEmailInvitation?Selected_Date='+encodeURIComponent(user_selected_date)+'&Selected_Box='+encodeURIComponent(user_selected_box);
         
           var appURL = "<%=request.getRequestURL()%>";
                var requestURL = '';
                if (appURL.indexOf("localhost:8619") != -1 || appURL.indexOf("amrnwlw058:8619") != -1 || appURL.indexOf("trt-dev.pfizer.com") != -1 || appURL.indexOf("trt-dev") != -1){
                   requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_DEV%>'  + url;
              }else if (appURL.indexOf("trt-tst.pfizer.com") != -1 || appURL.indexOf("trt-tst") != -1){
                requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_INT%>'  + url;
              
              }else if (appURL.indexOf("trt-stg.pfizer.com") != -1 || appURL.indexOf("trt-stg") != -1){
                requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_STG%>'  + url;
              
              }else if (appURL.indexOf("trt.pfizer.com") != -1 ){
              requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_PROD%>'  + url;
              }else if (appURL.indexOf("wlsdev1.pfizer.com") != -1){
                requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_INT%>'  + url;
              
              }else if (appURL.indexOf("wlsstg5.pfizer.com") != -1){
                requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_STG%>'  + url;
              
              }else if (appURL.indexOf("wlsprd4.pfizer.com") != -1){
              requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_PROD%>'  + url;
              }
          
          
          window.location.href = requestURL;
        }        
    }      

      
      function openPrintProgress(url){
      window.open(url,'rr',"left=240, top=170, width=635,height=256,scrollbars=yes,location=no,status=yes,resizable = yes");
      }
    
    </script>
    
    <body onload="window.focus();">
        <!-- Infosys code changes starts here
        <netui-data:getData resultId="trmOrderDate" value="{pageFlow.trmOrderDateBean}" />
        <netui-data:getData resultId="rbuBoxData" value="{pageFlow.rbuBoxDataBean}" /> -->
        <jsp:include page="EMailHeader.jsp"/>        
        
        <% //Begin Java
         String sCommand  = (String)request.getAttribute("Command");
         if(sCommand!=null && sCommand.equalsIgnoreCase("PromptForSending") && request.getAttribute("Count") != null)
          {
             String sCount  = (String)request.getAttribute("Count");
             %><p align="left"><b>Email Invitations Sent: </b><%=sCount%> <br><br>          
         <%}%>
          
            <form name="printForm">
             <%
            TRMOrderDateBean thisTRMOrderDateBean[];
            // thisTRMOrderDateBean = (TRMOrderDateBean[])pageContext.getAttribute("trmOrderDate");
            thisTRMOrderDateBean = (TRMOrderDateBean[])request.getAttribute("trmOrderDateBean");
            List allDates=new ArrayList();
            RBUBoxDataBean thisRBUBoxDataBean[];
            // thisRBUBoxDataBean = (RBUBoxDataBean[])pageContext.getAttribute("rbuBoxData");
            thisRBUBoxDataBean = (RBUBoxDataBean[])request.getAttribute("rbuBoxDataBean");
            String firstTimeLoad ="";
            if(request.getAttribute("firstTimeLoad") != null){
                firstTimeLoad = (String)request.getAttribute("firstTimeLoad");
            }
            String chosenDate = "";
            if(request.getAttribute("Chose_Date") != null){
                chosenDate = (String)request.getAttribute("Chose_Date");
            }
            %>
        <table>
            <tr>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;
                </td>             
                
                <td>
                Please select the TRM order date for printing the invitations:
                <table width="600" cellspacing="0">
                <tr>   
                
                <td>
                <!-- TRM Order Date:       <select name="TRMorderDate" onchange="getBoxes()">-->
                TRM Order Date:       <select name="TRMorderDate">
                        <option value="-1">Select Date</option>
                        <%
                            if(chosenDate.equalsIgnoreCase("ALL")){
                        %>
                         <option selected value="All">All</option>
                        <%
                        }
                            else{
                        %> 
                            <option  value="All">All</option>
                        <%
                            }
                            if(thisTRMOrderDateBean != null){
                         for(int j=0;j<thisTRMOrderDateBean.length;j++){
                            if(chosenDate.equals(thisTRMOrderDateBean[j].getOrderDate())){
                            %>
                            <option selected value="<%=thisTRMOrderDateBean[j].getOrderDate()%>"><%=thisTRMOrderDateBean[j].getOrderDate()%></option>
                           <%
                            }
                            else{
                          %>
                           <option value="<%=thisTRMOrderDateBean[j].getOrderDate()%>"><%=thisTRMOrderDateBean[j].getOrderDate()%></option>     
                            <%
                            }
                         }
                        }
                        %>
                        </select>
                        <%
                %>
                </td>
                </tr>
                </table>
                <table  width=600 bordercolor="#9AB9D7" border="1"> 
                    <tr>
                        <td width="20%"> Product(s)</td>
                        <td width="15%"> Box(es) </td>
                        <td width="10%"> Select </td>
                    </tr>
                <%
                   
                    if(thisRBUBoxDataBean != null){
                         for(int j=0;j<thisRBUBoxDataBean.length;j++){
                %>
                    <tr>
                        <td> <%=thisRBUBoxDataBean[j].getProductName()%> </td>
                        <td> <%=thisRBUBoxDataBean[j].getBoxCombo()%> </td>
                        <input type="hidden" name="product" value="<%=thisRBUBoxDataBean[j].getProductName()%>"/>
                        <input type="hidden" name="box" value="<%=thisRBUBoxDataBean[j].getBoxCombo()%>"/>
                       <td><INPUT TYPE=RADIO NAME="RBUBox" VALUE="<%=thisRBUBoxDataBean[j].getBoxId()%>"></td>
                    </tr> 
                 <%
                         }
                    }
                 %>
                    <tr>
                        <td> </td>
                        <td>All Boxes </td>
                        <input type="hidden" name="box" value="All Boxes"/>
                       <td><INPUT TYPE=RADIO NAME="RBUBox" VALUE="100"></td>
                    </tr>                    
                     <input type="hidden" value="" name="RBUBox" />
                </table>
            
            
            
            
            
            <table>
                <tr>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;
                </tr>            
                <tr>
                <td width="100">&nbsp;&nbsp;&nbsp;&nbsp;            
                  <td>
                  <p align="center">
                  Please click 'Send email' to send email invitations. 
                 <br>
                    <input type="button"  name="showList" value="Send email" onclick="sendEMail()">
                    </p>
                 </td>
               </tr>
            </table>
        </form>
    </body>
<!-- </netui:html> -->
</html>