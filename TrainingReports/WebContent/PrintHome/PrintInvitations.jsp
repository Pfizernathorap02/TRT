<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"%> --%>
<%@ page import="com.tgix.printing.RBUBoxDataBean"%>
<%@ page import="com.tgix.printing.TRMOrderDateBean"%>
<%@ page import="com.tgix.printing.PrintingConstants"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>
<%-- Infosys migrated code weblogic to jboss changes start here
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>
 --%>
 <!-- <netui:html> -->
<html>
<!-- Infosys migrated code weblogic to jboss changes end here -->

    <head>
        <title>
            Product Training (PSCPT) - Invitation Printing
        </title>
        <LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
        <LINK href="/TrainingReports/resources/css/style.css" type="text/css" rel="STYLESHEET">
        <LINK href="/TrainingReports/resources/css/header.css" type="text/css" rel="STYLESHEET">           
    </head>
    <script src="/TrainingReports/resources/js/OpenPopUp.js"></script>
    <script src="/TrainingReports/resources/js/OpenEmployeeList.js"></script>
    
        <script language="javascript">
      function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
        }
        function ltrim(stringToTrim) {
	return stringToTrim.replace(/^\s+/,"");
        }
        function rtrim(stringToTrim) {
	return stringToTrim.replace(/\s+$/,"");
        }
  
        
        
      function getClusterInfo(){
        // Changes made by Jeevan for RBU Box
      var user_selected_box=0;
        var rads =the_form.RBUBox;
        for(var i=0;i<rads.length;i++){
            if(the_form.RBUBox[i].checked == true){
                user_selected_box= the_form.RBUBox[i].value;
                }
        }
      
     //  if(user_selected_date == '-1' &&  user_selected_box == 0)  {
       //     alert('Please select TRM Order Date and Box to continue');
         //   return false;
     //  }
     // if(user_selected_date == '-1') {
     //   alert('Please select a TRM Order Date or select ALL option for all the dates to continue');
      //  return false;
     // }
      if( user_selected_box == 0){
        alert('Please select a Box to continue.');
        return false;
      }
      
      var user_selected_date = '';
        var collection = document.getElementsByTagName("select")
        for(var x = 0;x<collection.length;x++){
            var name  = collection[x].name;
            if(name == trim(user_selected_box)){
                user_selected_date = collection[x].value;
            }
        }
        if(user_selected_date == '' && user_selected_box != 100){
            alert('There are no orders for this box.');
            return false;
        }
      var idField = document.getElementById("cluster_sel");  
      if(idField.selectedIndex==0)return false;
      <%-- Infosys migrated code weblogic to jboss changes start here
      var url='<%=PageflowTagUtils.getRewrittenFormAction("clusterInfo", pageContext)%>?ClusterType='+encodeURIComponent(idField.value); 
       --%>
      var url='/TrainingReports/PrintHome/clusterInfo?ClusterType='+encodeURIComponent(idField.value); 
      <!-- Infosys migrated code weblogic to jboss changes end here -->
      window.location.href = url;
      }
      
      function getSelectedInfo(sourceF){
      var the_form = window.document.forms[0];
      //var user_selected_date = document.getElementById("TRMorderDate").value;
     // var user_selected_date = 'ALL';
      // Changes made by Jeevan for RBU Box
     // if(user_selected_date == '-1') {
       // alert('Please select a TRM Order Date or select ALL option for all the dates to continue.');
       // return false;
     // }
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
    var user_selected_date = '';
    var collection = document.getElementsByTagName("select")
    for(var x = 0;x<collection.length;x++){
        var name  = collection[x].name;
        if(name == trim(user_selected_box)){
            user_selected_date = collection[x].value;
        }
    }
    if(user_selected_date == '' && user_selected_box != 100){
        alert('There are no orders for this box.');
        return false;
    }
      if(sourceF=='EmployeeList'){
    	  
      <%-- Infosys migrated code weblogic to jboss changes start here
       var url='<%=PageflowTagUtils.getRewrittenFormAction("employeesUnderSelection", pageContext)%>?Selected_Date='+encodeURIComponent(user_selected_date)+'&Selected_Box='+encodeURIComponent(user_selected_box);
       --%>
       var url='/TrainingReports/PrintHome/employeesUnderSelection?Selected_Date='+encodeURIComponent(user_selected_date)+'&Selected_Box='+encodeURIComponent(user_selected_box);
       <!-- Infosys migrated code weblogic to jboss changes end here -->
      getEmployeeList(url);
      }else{
            var r=window.confirm("Shipment Letter will be printed for:\nTRM Order Date: "+user_selected_date+"\nDo you want to continue?");
            if (r){
                <%-- var url='<%=PageflowTagUtils.getRewrittenFormAction("printInvitationForSelectedGroup", pageContext)%>?Group_ID='+encodeURIComponent(user_input_group)+'&Selected_Date='+encodeURIComponent(user_selected_date)+'&Selected_Box='+encodeURIComponent(user_selected_box);--%>
                <%-- Infosys migrated code weblogic to jboss changes start here
                var url='<%=PageflowTagUtils.getRewrittenFormAction("printInvitationForSelectedGroup",pageContext)%>?Selected_Date='+encodeURIComponent(user_selected_date)+'&Selected_Box='+encodeURIComponent(user_selected_box);
                --%>
                
                var url='/TrainingReports/PrintHome/printInvitationForSelectedGroup?Selected_Date='+encodeURIComponent(user_selected_date)+'&Selected_Box='+encodeURIComponent(user_selected_box);
                <!-- Infosys migrated code weblogic to jboss changes end here -->
                var appURL = "<%=request.getRequestURL()%>";
                var requestURL = '';
                if (appURL.indexOf("trt-dev.pfizer.com") != -1 || appURL.indexOf("trt-dev") != -1 || appURL.indexOf("amrnwlw058") != -1 || appURL.indexOf("localhost") != -1){
                   requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_DEV%>'  + url;
              }else if (appURL.indexOf("trt-tst.pfizer.com") != -1){
                requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_INT%>'  + url;
              
              }else if (appURL.indexOf("trt-stg.pfizer.com") != -1 || appURL.indexOf("trt-stg") != -1){
                requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_STG%>'  + url;
              
              }else if (appURL.indexOf("trt.pfizer.com") != -1 ){
              requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_PROD%>'  + url;
              }
                //openPrintProgress(url);
               
                openPrintProgress(requestURL);
                return true;
                   }
                return false;
            }
      
      }
      function getBoxes(){
       // alert('Date Selected' + document.getElementById("TRMorderDate").value);
        <%-- Infosys migrated code weblogic to jboss changes start here
        var url='<%=PageflowTagUtils.getRewrittenFormAction("getBoxesForTRMDates", pageContext)%>?Selected_Date='+encodeURIComponent(document.getElementById("TRMorderDate").value);
        --%>
        
        var url='/TrainingReports/PrintHome/getBoxesForTRMDates?Selected_Date='+encodeURIComponent(document.getElementById("TRMorderDate").value);
        <!-- Infosys migrated code weblogic to jboss changes end here -->
        // alert(url);
        //var selectedDate = document.getElementById("TRMorderDate").value);
        document.all.printForm.action = url;
        document.all.printForm.submit();
        
      }
      function openPrintProgress(url){
      window.open(url,'rr',"left=240, top=170, width=635,height=256,scrollbars=yes,location=no,status=yes,resizable = yes");
      }
    
    </script>
    
    <body onload="window.focus();">
       <!-- Infosys code changes starts here
        <netui-data:getData resultId="trmOrderDate" value="{pageFlow.trmOrderDateBean}" />
        <netui-data:getData resultId="rbuBoxData" value="{pageFlow.rbuBoxDataBean}" /> -->
        <jsp:include page="Header.jsp" />
        <%-- </jsp:include> --%>
        <%
        // Changes made for RBU Shipment
        System.out.println("Entering printInvitations.jsp >>>>>>>" +  new Date());
        TRMOrderDateBean thisTRMOrderDateBean[];
       /*  thisTRMOrderDateBean = (TRMOrderDateBean[])pageContext.getAttribute("trmOrderDate"); */
        thisTRMOrderDateBean = (TRMOrderDateBean[])request.getAttribute("trmOrderDateBean");
        System.out.println("The TRMOrder Date Bean is "+thisTRMOrderDateBean);
        List allDates=new ArrayList();
        RBUBoxDataBean thisRBUBoxDataBean[];
        /* thisRBUBoxDataBean = (RBUBoxDataBean[])pageContext.getAttribute("rbuBoxData"); */
        thisRBUBoxDataBean = (RBUBoxDataBean[])request.getAttribute("rbuBoxDataBean");
        System.out.println("The RBU Data Bean is "+thisRBUBoxDataBean);
        List boxes = new ArrayList();
        if(request.getAttribute("boxes") != null){
            boxes = (List)request.getAttribute("boxes");
        }
        String firstTimeLoad ="";
        if(request.getAttribute("firstTimeLoad") != null){
            firstTimeLoad = (String)request.getAttribute("firstTimeLoad");
        }
        String chosenDate = "";
        if(request.getAttribute("Chose_Date") != null){
            chosenDate = (String)request.getAttribute("Chose_Date");
        }
        %>
        
        <form name="printForm">
        <table>
            <tr>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;
                </td>             
                
                <td>
                Please select the TRM order date and Box for printing the invitations:
                <table width="600" cellspacing="0">
                <tr>   
                
               <%-- <td>
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
                --%>
                </tr>
                </table>
                <table  width=600 bordercolor="#9AB9D7" border="1"> 
                    <tr>
                        <td width="20%"> Product(s)</td>
                        <td width="15%"> Box(es) </td>
                        <td width="10%"> Select </td>
                        <td width="10%"> Date </td>
                    </tr>
                <%
                   
                    if(boxes != null){
                         Iterator iter = boxes.iterator();
                         while(iter.hasNext())
                         {
                         RBUBoxDataBean dataBean = (RBUBoxDataBean)iter.next(); 
                         String[] dates = dataBean.getDates();
                        // String[] dates = null;  
                         
                %>
                    
                    <tr>
                        <td> <%=dataBean.getProductName()%> </td>
                        <td> <%=dataBean.getBoxCombo()%> </td>
                       <%--
                        <input type="hidden" name="product" value="<%=dataBean.getProductName()%>"/>
                        <input type="hidden" name="box" value="<%=dataBean.getBoxCombo()%>"/>
                        --%>
                       <td><INPUT TYPE=RADIO NAME="RBUBox" VALUE=" <%=dataBean.getBoxId()%>"></td>
                       <td>
                       <%
                         if(dates != null && dates.length > 0){   
                            
                        %>
                        <select name ="<%=dataBean.getBoxId()%>" id="<%=dataBean.getBoxId()%>" >
                           <option  selected value="All">All</option>
                           <%
                            for(int j=0;j<dates.length;j++){
                           %>
                            <option  value="<%=dates[j]%>"><%=dates[j]%></option>  
                            <%
                                 }
                            %>
                            </select>       
                            <%
                               
                                }
                                
                             %>  
                        </td>
                    </tr> 
                    
                 <%
                        
                         }
                  %>       
                         <tr>
                         
                        <td> </td>
                        <td>HS/L Toviaz 02/04/09</td>
                        <input type="hidden" name="box" value="HS/L Toviaz 02/04/09"/>
                       <td><INPUT TYPE=RADIO NAME="RBUBox" VALUE="100"></td>
                       <td>
                            <select name ="100" id="100" >
                                <option  selected value="All">All</option>
                             </select>   
                            
                       </td> 
                    </tr>
                     <input type="hidden" value="" name="RBUBox" /> 
                 <%   
                
                
                    }
                 %>           
                </table>
                 <br>
                    <input type="button"  name="showList" value="Show List Of Employees" onclick="getSelectedInfo('EmployeeList')">
                    <input type="button"  name="printInvitations" onclick="getSelectedInfo('PrintInv')" value="Print Shipment Letters">
                   <!--  <input type="button"  name="trmOrder" value="Search TRM Order" onclick="getEmployeeList('SearchTRMOrder.jsp')">
                    --> 
                     <input type="button"  name="trmOrder" value="Search TRM Order" onclick="getEmployeeList('/TrainingReports/PrintHome/SearchTRMOrder.jsp')">
                   
               </td>
           </tr>
       </table>     
        </form>

        
    </body>
<!-- </netui:html> -->
</html>