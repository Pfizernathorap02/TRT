<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MainReportListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.rbu.FutureAllignmentBuDataBean"%>
<%@ page import="com.tgix.rbu.FutureAllignmentRBUDataBean"%>
<%@ page import="com.tgix.rbu.ProductDataBean"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainReportListWc wc = (MainReportListWc)request.getAttribute(MainReportListWc.ATTRIBUTE_NAME);
    String comingFrom =session.getAttribute("ReportType")==null?"":(String)session.getAttribute("ReportType");
    String mailSub="Toviaz Launch Follow-up";
   
     FutureAllignmentBuDataBean thisBuDataBean[];
    thisBuDataBean = wc.getFutureAllignmentBuDataBean();
     FutureAllignmentRBUDataBean thisRbuDataBean[];
    thisRbuDataBean = wc.getFutureAllignmentRBUDataBean();    
     String fromRequest = "";
    if(session.getAttribute("fromRequest") != null){
            fromRequest = (String)session.getAttribute("fromRequest");
            session.removeAttribute("fromRequest");
    }    
    
%>
 <script language="javascript">
 function getReports(from){
 var bu = document.getElementById("bu").value;
 var rbu = document.getElementById("rbu").value;
 /* Infosys -  migration changes */
<%-- // var url = '<%=PageflowTagUtils.getRewrittenFormAction("getFilteredChart", pageContext)%>?selectedProduct='+encodeURIComponent(product)+'&selectedProductDesc='+encodeURIComponent(productValue)+'&selectedBU='+encodeURIComponent(bu)+'&selectedRBU='+encodeURIComponent(rbu); --%>
var url = '';
	/*Infosys - Weblogic to Jboss Migrations changes start here */
     url = '/TrainingReports/LaunchMeeting/listreport?selectedBU='+encodeURIComponent(bu)+'&selectedRBU='+encodeURIComponent(rbu)+'&fromRequest=reRun&type=<%=wc.getPhase().getPhaseNumber()%>&trackId=<%=wc.getTrackId()%>';
     /* Infosys - Weblogic to Jboss Migrations changes end here */
 //document.all.printForm.action = url;
 //document.all.printForm.submit();
  window.location.href = url;
 }
  /* Infosys - Weblogic to Jboss Migrations changes start here */
  /* var temp = '/TrainingReports/LaunchMeeting/getRBU.do?bu=' */
	 var temp = '/TrainingReports/LaunchMeeting/getRBU?bu='
		 function getRBUValues(){
		  var bu =  document.getElementById("bu").value;
           xmlHttp=GetXmlHttpObject();
           if (xmlHttp==null){
           alert ("Browser does not support HTTP Request");
           return;
           }
          // var url=temp+encodeURIComponent(bu);
           var url=temp + encodeURIComponent(bu);  
           xmlHttp.onreadystatechange=stateChanged
           xmlHttp.open("GET",url,true);
           xmlHttp.send(null);
		  }
         
         function GetXmlHttpObject(){ 
            var objXMLHttp=null;
            if (window.XMLHttpRequest){
            objXMLHttp=new XMLHttpRequest();
            }else if (window.ActiveXObject){
            objXMLHttp=new ActiveXObject("Microsoft.XMLHTTP")
            }
            return objXMLHttp;
          } //end of GetXmlHttpObject()
         
         
         function stateChanged(){ 
            var responseRecieved; 
            var temp = new Array();
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete"){
            responseRecieved=xmlHttp.responseText;
            temp = responseRecieved.split('|');
            var ind=1;
            var val;
            var select = document.getElementById("rbu");
            if(select)
            {
            select.options.length = 0;
             var nullOption = document.createElement("OPTION");
             nullOption.value = "ALL";
            nullOption.text = "All";
            select.options.add(nullOption);
             for(var counter=0;counter<temp.length;counter++){
                var oOption = document.createElement("OPTION");
                oOption.text = temp[counter];
                oOption.value = temp[counter];            
                select.options.add(oOption);
             }//end of the FOR LOOP
             }
            }//end of the IF Loop
        }
 </script>
 <form name="printForm" class="form_basic">
<table class="basic_table" >
	
    <%
        String selectedProduct = "";
        String selectedBU = "";
        String selectedRBU = "";
        if(session.getAttribute("selectedProduct")!= null){
            selectedProduct = (String)session.getAttribute("selectedProduct");
        }
        if(session.getAttribute("selectedBU")!= null){
            selectedBU = (String)session.getAttribute("selectedBU");
        }
        if(session.getAttribute("selectedRBU")!= null){
            selectedRBU = (String)session.getAttribute("selectedRBU");
        }
    %>
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td align="left">
			<table width="100" class="basic_table">
                <tr>
                 <td width="15%"><label>BU:</label></td>
                    <td>
                    <select name="bu" onchange="getRBUValues()" >

                    <%
                      if(selectedBU.equalsIgnoreCase("ALL")){
                    %>
                    <option  selected value="ALL">All</option>     
                    <%
                        }
                        else{
                    %>
                   
                     <option   value="ALL">All</option>   

                     <%
                        }
                        if(thisBuDataBean != null){
                        for(int i=0;i<thisBuDataBean.length;i++){
                        if(selectedBU.equals(thisBuDataBean[i].getBu()  )){                            
                    %>
                        <option selected value="<%=thisBuDataBean[i].getBu()%>"><%=thisBuDataBean[i].getBu()%></option>     
                    <%
                        }
                        else{
                     %>
                      <option  value="<%=thisBuDataBean[i].getBu()%>"><%=thisBuDataBean[i].getBu()%></option>     
                     <%
                        }  
                        }
                        }
                    %>
                    </select>
                   </td>
                </tr>
                <tr>
                 <td width="15%"><label>RBU:</label></td>
                    <td>
                    <select name="rbu" >
                     <option value="ALL">All</option>
                    <%
                        if(thisRbuDataBean != null){
                        for(int i=0;i<thisRbuDataBean.length;i++){
                        if(selectedRBU.equals(thisRbuDataBean[i].getRbu())){
                            
                    %>
                        <option  selected value="<%=thisRbuDataBean[i].getRbu()%>"><%=thisRbuDataBean[i].getRbu()%></option>     
                    <%
                        }
                        else{
                    %>        
                            
                          <option value="<%=thisRbuDataBean[i].getRbu()%>"><%=thisRbuDataBean[i].getRbu()%></option>       
                            
                     <%  
                        }     
                        }
                        }
                    %>
                    </select>
                   </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td width="10%">
                    <img src="<%=AppConst.IMAGE_DIR%>/btn_getreport.gif" onclick="getReports('<%=fromRequest%>')" />
                </tr>
                </table>
		</td>
        
		<td align="right">
			<% if (wc.getChart() != null) { %>
				<inc:include-wc component="<%=wc.getChart()%>"/>
			<% } %>
		</td>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
	</tr>
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td valign="top" colspan="2">
			<% if (wc.getReportList() != null) { %>
				<inc:include-wc component="<%=wc.getReportList()%>"/>
			<% } %>
			
			<% if (wc.getTotal() == 0) {%>
				<p>There are no trainees that meet this criteria</p>
			<% } %>
		</td>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
	</tr>
</table>
</form>