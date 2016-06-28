<%@page import="java.util.*" %>
<%@page import="java.net.*" %>
<%@page import="java.io.*" %>
<%@page import="java.sql.*" %>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>

<%
    response.setHeader("Pragma", "public");
    response.setHeader("Cache-Control", "max-age=30");
%>



<%
    /**
    * This page was added by Mahua Ghosh 5th Dec 2011 to redirect requests from TRT to SCE
    */

try{
    String redirectUrl="";
    String env="";
    //Get all the request paramters from the TRT links "Evaluate/Re-evaluate/View Evaluations"
    String linkName = request.getParameter("linkName");
    String ntId = request.getParameter("employeeNtId");
    String emplId = request.getParameter("emplid");
    String productCode = request.getParameter("productCode");
    String product = request.getParameter("product");
    System.out.println("JSP:::productCode = "+productCode);
    //Hard-coded for testing
    // productCode = "2919504";
    String eventId = request.getParameter("eventId");
    String loginUserNtId = request.getParameter("loginUserNtId");
        
    /*For DEBUGGING*/
    System.out.println("JSP:::linkName = "+linkName);
    System.out.println("JSP:::ntId = "+ntId);
    System.out.println("JSP:::emplId = "+emplId);
    System.out.println("JSP:::productCode = "+productCode);
    System.out.println("JSP:::eventId = "+eventId);
    System.out.println("JSP:::loginUserNtId = "+loginUserNtId);
    System.out.println("JSP:::product = "+product);    
        
        
        /*Environment identification*/        
        String url = request.getRequestURL().toString();
        System.out.println("JSP:::url from request object= "+url);
        
        if (url != null ) {
            url = url.toLowerCase();
            System.out.println("JSP:::Lowercase url = "+url);
        }
        
        
        if(url !=null && (url.indexOf("localhost") > 0)){
            //sceUrl = "http://localhost:7001/SCEWeb/evaluation/redirectToSce.do?linkName="+linkName+"&ntId="+ntId+"&emplId="+emplId+"&productCode="+productCode+"&eventId="+eventId;	
            env = "http://localhost:7001/SCEWeb/evaluation/";	
            System.out.println("JSP:::Localhost env = "+env);
        }
        else if ( url != null && (url.indexOf("trt-dev.pfizer.com") > 0 || url.indexOf("amrndhl275") > 0)) {
            //sceUrl = "http://scedev.pfizer.com/SCEWeb/evaluation/redirectToSce.do?linkName="+linkName+"&ntId="+ntId+"&emplId="+emplId+"&productCode="+productCode+"&eventId="+eventId;
            //env = "http://scedev.pfizer.com/SCEWeb/evaluation/";
            env = "http://sce-dev.pfizer.com/SCEWeb/";
            System.out.println("JSP:::DEV env = "+env);
        }
        else if ( url != null && (url.indexOf("trt-tst.pfizer.com") > 0 )) {
            //sceUrl = "http://sceint.pfizer.com/SCEWeb/evaluation/redirectToSce.do?linkName="+linkName+"&ntId="+ntId+"&emplId="+emplId+"&productCode="+productCode+"&eventId="+eventId;
            env = "http://sce-tst.pfizer.com/SCEWeb/evaluation/";
            System.out.println("JSP:::INT env = "+env);		
        }
        else if (url != null && ( url.indexOf("trt-stg.pfizer.com") > 0  || url.indexOf("tgix-dev.pfizer.com") > 0 )) {
            //sceUrl = "http://scestg.pfizer.com/SCEWeb/evaluation/redirectToSce.do?linkName="+linkName+"&ntId="+ntId+"&emplId="+emplId+"&productCode="+productCode+"&eventId="+eventId;
            env = "http://sce-stg.pfizer.com/SCEWeb/evaluation/";	
            System.out.println("JSP:::STG env = "+env);
        }
        else {//Production
            //sceUrl = "http://sce.pfizer.com/SCEWeb/evaluation/redirectToSce.do?linkName="+linkName+"&ntId="+ntId+"&emplId="+emplId+"&productCode="+productCode+"&eventId="+eventId;
            env = "http://sce.pfizer.com/SCEWeb/evaluation/";
            System.out.println("JSP:::PRD env = "+env);
        }
        
        /*End Environment identification*/
        
        //Redirect to SCE based on the link that was clicked
        //If 'Evaluate' or 'Re-Evaluate' link was clicked, redirect to method that checks for Legal Consent
        //Else If 'View Evaluations' link was clicked, redirect to method that checks for evaluation history
        if(linkName != null && (linkName.equalsIgnoreCase("evaluate") || linkName.equalsIgnoreCase("reEvaluate"))){
          redirectUrl=env+"goToLegalCheckTr?linkName="+linkName+"&ntId="+ntId+"&emplId="+emplId+"&productCode="+productCode+"&eventId="+eventId+"&loginUserNtId="+loginUserNtId+"&product="+product;
          System.out.println("JSP:::Final redirectUrl for Evaluate/Re-Evaluate= "+redirectUrl); 
          
        }else if(linkName != null && linkName.equalsIgnoreCase("viewEvaluations")){
          redirectUrl=env+"goToHistoryCheckTr?linkName="+linkName+"&ntId="+ntId+"&emplId="+emplId+"&productCode="+productCode+"&eventId="+eventId+"&loginUserNtId="+loginUserNtId+"&product="+product; 
          System.out.println("JSP:::Final redirectUrl for View Evaluations= "+redirectUrl); 
        }
        System.out.println("redirectUrl before calling response.sendRedirect = **"+redirectUrl+"**"); 
        response.sendRedirect(redirectUrl); 
     
}
catch(Exception ex) 
{
   System.out.print("Error....");
   ex.printStackTrace();
   
}        
    
%>

