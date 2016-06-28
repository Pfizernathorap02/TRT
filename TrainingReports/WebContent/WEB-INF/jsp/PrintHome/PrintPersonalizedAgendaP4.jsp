<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"%> --%>
<%@ page import="com.pfizer.db.RBUTrainingWeek"%>
<%@ page import="com.tgix.printing.RBUBoxDataBean"%>
<%@ page import="com.tgix.printing.TRMOrderDateBean"%>
<%@ page import="com.tgix.printing.PrintingConstants"%>
<%@ page import="com.tgix.printing.TrainingWeeks"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.io.File" %>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>--%>
<!-- <netui:html>  -->
<html>
    <head>
        <title>
            P4 Training - Personalized Agenda Printing
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
      
      function getSelectedInfo(){
      var the_form = window.document.forms[0];
       var user_selected_box=0;
        var user_selected_box =the_form.week.value;
      if( user_selected_box == 0){
        alert('Please select a Date to continue.');
        return false;
      }
        var r=window.confirm("Personalized Agenda Letters will be printed \nDo you want to continue?");
            if (r){
               <%--  Infosys Code chabges starts here
               var url='<%=PageflowTagUtils.getRewrittenFormAction("createPersonlizedAgendaLettersP4", pageContext)%>?WeekId='+encodeURIComponent(user_selected_box);
              --%>
              
              var url='/TrainingReports/PrintHome/createPersonlizedAgendaLettersP4?WeekId='+encodeURIComponent(user_selected_box);
              
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
              }
                openPrintProgress(requestURL);
                return true;
                   }
                return false;
      
      }
      function openPrintProgress(url){
      window.open(url,'rr',"left=240, top=170, width=635,height=256,scrollbars=yes,location=no,status=yes,resizable = yes");
      }
    
    </script>
    
    <body onload="window.focus();">
        <jsp:include page="HeaderAgendaP4.jsp" />
        
        <%
                TrainingWeeks trainingWeek[];
                trainingWeek = (TrainingWeeks[])request.getAttribute("classWeeks");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                session.setAttribute("trainingWeek", trainingWeek);
                
        %>
        <form name="printForm">
        <table>
            <tr>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;
                </td>             
                
                <td>
                    Please select a wave
               </td>
           </tr>
           <tr>
           <td>&nbsp;</td>
            <td> 
            <select name="week">
                    <option  value="wave1">Wave 1</option>
                    <option  value="wave2">Wave 2</option>
                    <option  value="wave3">Wave 3</option>
            </select>
            </td> 
            </tr>
           <tr>
           <tr>
           <td>&nbsp;</td>
           <td>
            <input type="button"  name="printPersonalizedAgenda" onclick="getSelectedInfo()" value="Print Personalized Agendas">
            <input type="button"  name="trmOrder" value="Search Employee" onclick="getEmployeeList('/TrainingReports/PrintHome/SearchEmployeeForAgendaP4.jsp')">
            </td>
            </tr>
       </table>     
        </form>
    </body>
<!-- </netui:html> -->
</html>