<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.DetailPageWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.SpecialCodesWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.PhaseTrainingDetailWc"%>

<%-- <netui:html> --%>
<html>
   <%
	System.out.println("Inside JSp");
    //SpecialCodesWc wc = (SpecialCodesWc)request.getAttribute(SpecialCodesWc.ATTRIBUTE_NAME);
   // PhaseTrainingDetailWc wc1 = (PhaseTrainingDetailWc)request.getAttribute("SCEEVAL");
    
   // User user = wc1.getUser();
    //System.out.println("User obj"+user);
    String url = request.getRequestURL().toString();
    System.out.println("URL"+url);
    String str = url;
    String[] words = str.split ("&");
    for (int i=0; i < words.length; i++)
    {
            System.out.println (words[i]);
    }
    String scUrl = "http://sce.pfizer.com/SCEWeb/evaluation/evaluateSCE";
    
    
    if ( url != null ) {
		url = url.toLowerCase();
	}
	if ( url != null 
		&& (url.indexOf("trt-stg.pfizer.com") > 0 || url.indexOf("tgix-dev.pfizer.com") > 0 )) {
		scUrl = "http://sce-stg.pfizer.com/SCEWeb/evaluation/evaluateSCE";	
	} 
    if ( url != null 
		&& (url.indexOf("trt-tst.pfizer.com") > 0 )) {
		scUrl = "http://sce-tst.pfizer.com/SCEWeb/evaluation/evaluateSCE";	
	}
    if ( url != null 
		&& (url.indexOf("localhost") > 0 )) {
		scUrl = "http://localhost:7001/SCEWeb/evaluation/evaluateSCE";	
	}
    else if ( url != null && (url.indexOf("trt-dev.pfizer.com") > 0 || url.indexOf("amrndhl275") > 0)) {
        //sceUrl = "http://scedev.pfizer.com/SCEWeb/evaluation/redirectToSce.do?linkName="+linkName+"&ntId="+ntId+"&emplId="+emplId+"&productCode="+productCode+"&eventId="+eventId;
        //env = "http://scedev.pfizer.com/SCEWeb/evaluation/";
        scUrl = "http://sce-dev.pfizer.com/SCEWeb/";
        System.out.println("JSP:::DEV env = "+scUrl);
    }
    String EmplId = request.getParameter("emplId");
    String EvalId=request.getParameter("evalId");
    String ActivityId=request.getParameter("activityId");
    String EvalNm=request.getParameter("evalNm");
    String EmpNm=request.getParameter("empNm");
    String Flag=request.getParameter("flag");
    String isSAdmin= request.getParameter("superAdmin");
    String trackId= request.getParameter("trackId");
    
    System.out.println("Emp ID"+EmplId);
    System.out.println("Emp Name"+EmpNm);
    System.out.println("EmplName"+EvalNm);
    System.out.println("SCE Url"+scUrl);
    System.out.println("isSAdmin in SCEEvaluationForm.jsp"+isSAdmin);
    %>
    <body onload="document.evaluateFormNew.submit();">
         <form name='evaluateFormNew' id='evaluateFormNew' method='post' action='<%=scUrl%>' > 
                    
                    <input type='hidden' name='emplId' value='<%=EmplId%>' id='emplId'/> 
                    <input type='hidden' name='productCode' value='<%=ActivityId%>' id='productCode'/> 
                    <input type='hidden' name='evaluatorEmplId' value='<%=EvalId%>' id='evaluatorEmplId'/>
                    <input type='hidden' name='evaluatorEmplName' value='<%=EvalNm%>' id='evaluatorEmplName'/>
                    <input type='hidden' name='EmplName' value='<%=EmpNm%>' id='EmplName'/>
                    <input type='hidden' name='flag' value='<%=Flag%>' id='flag'/>
                    <input type='hidden' name='superAdmin' value='<%=isSAdmin%>' id='superAdmin'/>
                    <input type='hidden' name='trackId' value='<%=trackId%>' id='trackId'/>
                    
                    <% System.out.println("Before A HREF");
                    /*System.out.println("USER"+user.getName());
                    System.out.println("SCEURL"+scUrl);
                    System.out.println("ACTIVITY ID"+wc1.getEmployee().getEmplId());
                    System.out.println("EMP ID"+wc1.getEmployee().getProductCode());
                    System.out.println("EMP NAME"+Fname);*/
                    %>
                     
                 </form>
    </body>
<%-- </netui:html>
 --%>
 </html>
