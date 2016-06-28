<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"%> --%>
<%@ page import="com.pfizer.hander.CourseHandler"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<!-- <netui:html> -->
<html>
    <head>
       <title>
       FFT Training Reports - Reason for Exemption
       </title>
        <STYLE TYPE="text/css">
div#commentForm{  margin: 0px 20px 0px 20px;  display: none;}
</STYLE>
<script>
    var pressFlag = 'no'; 
	function noEntry(){
        reasonIndex = document.getReason.exemptionNotes;
        var selDomainIndex=reasonIndex.selectedIndex;
        if(selDomainIndex==0){
			alert('Please select a reason');
        	return false;
        }
            
       if (pressFlag == 'no') {
           var my = opener.document.getElementById('mybutton');
           pressFlag = 'yes';
           my.innerHTML = '<font id="grantingtext">Granting Exemption. Please wait 30 seconds...</font>';
           opener.blink('grantingtext');
           //Lets Hide the Present DIV  and Display the Other Div:
           var firstDiv = document.getElementById('disp');
           var secondDiv = document.getElementById('process');
           firstDiv.style.display="none";
           secondDiv.style.display="";
           var selectedReason=document.getReason.exemptionNotes.options[document.getReason.exemptionNotes.selectedIndex].value;
           opener.document.exemptAction.exemptReason.value=selectedReason;
           opener.document.exemptAction.submit();
           window.close();
           return false;
       } else {
       		// do nothing
       }
        	return true;
       }
        
        
    function showhide(id){ 
        if (document.getElementById){ 
            obj = document.getElementById(id); 
            if (obj.style.display == "none"){ 
            	obj.style.display = ""; 
            } else { 
            	obj.style.display = "none"; 
            } 
        } 
    } 

       
</script>
    </head>
    <body>
    <%
    String success=request.getParameter("success");
    String emplid=request.getParameter("emplId");
    String prodCd=request.getParameter("prodCd");
    if("success".equalsIgnoreCase(success)){
    session.setAttribute("refresh","true");
    %>
    <script>
    opener.location.href='/TrainingReports/overview/detailreport.do?refresh=true&needTraining=Exempted&search=true&emplid=<%=emplid%>&productCode=<%=prodCd%>';           
    window.close();
    </script>
    <%
    }
    CourseHandler ch=new CourseHandler();
    ArrayList listReason=(ArrayList)ch.getAllExemptionReasons();
    
    String lastName=request.getParameter("lastName");
    String firstName=request.getParameter("firstName");
    %>
       <div style="margin:2px;display:" id="disp">
        <form  action="/TrainingReports/overview/grantExemption" method="get" onsubmit="return noEntry();" name="getReason">
        <table >
            <tr>
             <td>Please select an exemption reason for&nbsp;<%=lastName%>,&nbsp;<%=firstName%>&nbsp;for&nbsp;<%=prodCd%></td>
            </tr>
            <tr>
              <tr>
             <td>&nbsp;</td>
            </tr>
            <tr>
            
            <td>
            <select name="exemptionNotes">
            <option selected value="Select an exemption reason">Select an exemption reason</option>
            <%
            if(listReason.size()>0){
                Iterator iter=listReason.iterator();
                String reason="";
                while(iter.hasNext()){
                reason=iter.next().toString();
                %>
                <option  value="<%=reason%>"><%=reason%></option>
                <%
                }//end of the while loop
            }//end of the IF Loop
            %>
            </select>
            </td>
            </tr>
            <tr>
            <td align="left">
            <input type="submit" value="Submit">
            <input type="button" value="Cancel" onclick="window.close();">
            <input type="hidden"  name="fromReason" value="true">
            <input type="hidden"  name="emplId" value="<%=emplid%>">
            <input type="hidden"  name="prodCd" value="<%=prodCd%>">
            </td>
            </tr>
            </table>
        </form>
        </div>
        
        <div style="margin:5px;display: none;"  id="process">    
        Processing your request...
        </div>
        
    </body>
<!-- </netui:html> -->
</html>
