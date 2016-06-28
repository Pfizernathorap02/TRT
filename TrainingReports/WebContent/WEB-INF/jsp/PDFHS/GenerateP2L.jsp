<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.P2LRegistration"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.printing.PrintingConstants"%>
<%@ page import="com.tgix.printing.VelocityConvertor"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%
    String message = (String)request.getAttribute("message");    
%>

<!-- <netui:html> -->
<html>
    <head>
        <title>
            Generate P2L Registration File
        </title>
        <LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
        <LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
        <script language="JavaScript">
        function generateP2L() {
            document.getElementById('message').style.visibility = "hidden";
            //document.all.generateForm.action.value = "generate";
            document.all.generateForm.action.value = "generateANDdownload";
            //setInterval(waitNotice, 1000);
            document.all.generateForm.submit();
        }
        function downloadLatest() {
            document.getElementById('message').style.visibility = "hidden";
            document.all.generateForm.action.value = "download";            
            document.all.generateForm.submit();
        }
        function waitNotice()
        {
            document.getElementById('wait').style.visibility == 'hidden'? document.getElementById('wait').style.visibility = 'visible':document.getElementById('wait').style.visibility = 'hidden';
        } 
        </script>
    </head>
    <body>
        <div id=top_header>
        <div id=header_logo></div>
        <div id=header_title >PDF - Generate P2L Registration</div>
        </div> 
        <p>
            <center>
                <span id="wait" style="visibility: hidden; color: #ff0000; font-weight: bold">Generating... Please wait !</span>                
            </center>
            <center>
                <span id="message" style="color: #1f61a9; font-weight: bold"><%=message%></span>                
            </center>
        </p>
        <form action="/TrainingReports/PWRA/generateP2L" name="generateForm">
            
            <table cellspacing="0" width="90%" style="MARGIN:15PX">
            <tr>
            <td>
                <input type="button" value="Generate P2L Registration File"  onclick="generateP2L();">
                <input type="hidden" name="action" id="action" value="">
                <%--&nbsp;or&nbsp;
                <input type="button" value="Download Latest"  onclick="downloadLatest();">--%>                
            </td>
            </tr>
            </table>
            
        </form>
    </body>
<!-- </netui:html> -->
</html>
<%    
    request.removeAttribute("message");
%>