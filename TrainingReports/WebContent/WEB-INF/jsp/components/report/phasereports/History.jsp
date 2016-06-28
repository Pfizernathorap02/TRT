<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"% --%>>
<%@ page import="com.pfizer.webapp.sce.SCE"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<script language="javascript">
function view(sceId) {
alert("NORMAL");
        window.document.getElementById(getNetuiTagName("selSceId")).value = sceId;
      <%--   window.document.getElementById(getNetuiTagName("SearchForAttendeeForm")).action="<%=PageflowTagUtils.getRewrittenFormAction("viewEvaluationFromHistory", pageContext)%>"; --%>                
        
        var url=window.document.getElementById(getNetuiTagName("SearchForAttendeeForm")).action
        url=url+'?sceId'+'='+sceId 
        alert(url);
        //window.document.forms[0].submit();
        window.open(url,'view_window','status=yes,scrollbars=yes,height=380,width=500,resizable=yes,menubar=no')         
    }
    
function viewPDF(sceId){
alert("PDF");
window.document.getElementById(getNetuiTagName("selSceId")).value = sceId;
        <%-- window.document.getElementById(getNetuiTagName("SearchForAttendeeForm")).action="<%=PageflowTagUtils.getRewrittenFormAction("viewPDFEvaluationFromHistory", pageContext)%>"; --%>                
        
        var url=window.document.getElementById(getNetuiTagName("SearchForAttendeeForm")).action
        url=url+'?sceId'+'='+sceId 
        alert(url);
        window.document.forms[0].submit();
}
    
</script>

<%
    //Event[] events = (Event[])pageContext.getAttribute("events");
 //   HashMap eventMap = new HashMap();
   // if (events != null) {
    //    for (int i=0; i<events.length; i++) {
     //       eventMap.put(events[i].getId(), events[i]);
    //    }
   // }  
              
    SCE[] sces = (SCE[])session.getAttribute("sces1");  
    if(sces!=null){
        //System.out.println("Product"+sces[0].getProduct());
    }  
   // Attendee attendee = null;
    DateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");    
   // Event curEvent = null;        
    DecimalFormat scoreFormatter = new DecimalFormat("###0.##");
    boolean phasedTraining = false; 
%>
<%-- <netui:html> --%>
<html>
    <head>
        <title>Evaluation History</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="Content-Language" content="en-us" />        
        <meta name="ROBOTS" content="ALL" />
        <meta http-equiv="imagetoolbar" content="no" />
        <meta name="MSSmartTagsPreventParsing" content="true" />
        <meta name="Keywords" content="_KEYWORDS_" />
        <meta name="Description" content="_DESCRIPTION_" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]--> 
    </head>
    <body >
     <div id="wrap">
     <DIV id=top_head>
                <H1>Pfizer</H1>
                <H2>Sales Call Evaluation</H2>            
               
            </DIV>
        
            <H3>Evaluation History</H3>
            <netui:form action="viewSce" tagId="SearchForAttendeeForm">            
    <p><B></B></p>
        <table cellspacing="0" border="1">
                    <tr>
                    <th>Date and Time (EST)</th>
                        <th>Score</th>
                        <th>Action</th>                        
        </tr>
         <%
         SCE objSCE = null;
         if (sces != null) {
                        for (int i=0; i<sces.length; i++) {
                            objSCE = sces[i];
                            System.out.println("Value of I**"+i);
                           // isSubmitted = (objSCE.getId() != null && SCEConstants.ST_SUBMITTED.equalsIgnoreCase(objSCE.getStatus()));
                          //  curEvent = (Event)eventMap.get(objSCE.getEventId());
                           // phasedTraining = (objSCE.getEventId() != null && objSCE.getEventId().intValue() >= 5 && objSCE.getEventId().intValue() <= 7);                           
                       
         %>
        <tr>
          <td><%=objSCE.getSubmittedDate().toString()%></td>
          <td><%=objSCE.getOverallRating()%></td>
          <td><a href="#">View</td>
        </tr>
           <%}
               }
              %>              
          </table>
          <input type="button" value="close" onclick="window.close()">
    </DIV> 
    </netui:form>         
    </body>
<%-- </netui:html> --%>
</html>