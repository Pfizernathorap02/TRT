<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.tgix.printing.PrintingConstants"%>
 <%
   String appURL = request.getRequestURL().toString();
   String url = "/TrainingReports";
   String requestURL = "";
               /*  if (appURL.indexOf("localhost:8619") != -1 || appURL.indexOf("amrnwlw058:8619") != -1){
                */  
                if (appURL.indexOf("localhost:8080") != -1 || appURL.indexOf("amrnwlw058:8619") != -1){
                	requestURL = PrintingConstants.APPLICATION_PATH_JSP_DEV  + url;
              }else if (appURL.indexOf("wlsdev1.pfizer.com") != -1){
                requestURL = "http://trt-tst.pfizer.com"  + url;
              
              }else if (appURL.indexOf("wlsstg5.pfizer.com") != -1){
                requestURL = "http://trt-stg.pfizer.com"  + url;
              
              }else if (appURL.indexOf("wlsprd4.pfizer.com") != -1){
              requestURL = "http://trt.pfizer.com"  + url;
              }
     /*  Infosys code changes starts here
     String requestURL_Home = requestURL + "/reportselect"; */
     String requestURL_Home = requestURL + "/reportselect";
      
     /*   String requestURL_Search =requestURL + "/RBU/rbuSearch/RbuSearchController.jpf";    */   
       String requestURL_Search =requestURL + "/RBU/rbuSearch/begin";   
%>     

<script language="Javascript">
function openContactBox() {
    window.open('<%=requestURL%>/resources/html/contacts.html','faq_window','status=yes,scrollbars=yes,height=400,width=750,resizable=no');                    
    return false;
} 

</script>
<div id=top_header>
	<div id=header_logo></div>
    <div id=header_title >Product Training (PSCPT) - Email</div>
    <UL id=header_top_nav>  
                               
        <LI><a href="<%=AppConst.APP_ROOT%>/reportselect">Home</a></LI>
        <LI><a href="<%=AppConst.APP_ROOT%>/RBU/rbuSearch/begin">PSCPT Search</a></LI>            
       <LI class=last><a href="#" onclick="openContactBox()" >Contact</a></LI>
	</UL>
    	
   </div> 
    