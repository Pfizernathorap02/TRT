<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"%> --%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.MainReportListWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.rbu.FutureAllignmentBuDataBean"%>
<%@ page import="com.tgix.rbu.FutureAllignmentRBUDataBean"%>
<%@ page import="com.tgix.rbu.ProductDataBean"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainReportListWc wc = (MainReportListWc)request.getAttribute(MainReportListWc.ATTRIBUTE_NAME);
    String comingFrom =session.getAttribute("ReportType")==null?"":(String)session.getAttribute("ReportType");
    String reportto =session.getAttribute("emplid")==null?null:(String)session.getAttribute("emplid");
    String mailSub="Product Training (PSCPT) Follow-up";
    /*if("PowersPOA".equalsIgnoreCase(comingFrom)) 
      mailSub ="Powers Mid-POA1  Follow-up";

    if("PowersDFHStudy".equalsIgnoreCase(comingFrom)) 
      mailSub="Powers Driving Force Home Study Follow-up";

    if("PowersPLC".equalsIgnoreCase(comingFrom))    
      mailSub="POWERS Driving Force PLC Report"; */ 
      ProductDataBean thisProductDataBean[];
    thisProductDataBean = wc.getProductDataBean();
     FutureAllignmentBuDataBean thisBuDataBean[];
    thisBuDataBean = wc.getFutureAllignmentBuDataBean();
     FutureAllignmentRBUDataBean thisRbuDataBean[];
    thisRbuDataBean = wc.getFutureAllignmentRBUDataBean();        
    
%>
 <script language="javascript">
 function getReports(){
 var product = document.getElementById("product").value;
 var w = document.printForm.product.selectedIndex;
 var productValue = document.printForm.product.options[w].text;
 var bu = document.getElementById("bu").value;
 var rbu = document.getElementById("rbu").value;
 var url = '/TrainingReports/RBU/listreport?selectedProduct='+encodeURIComponent(product)+'&selectedProductDesc='+encodeURIComponent(productValue)+'&selectedBU='+encodeURIComponent(bu)+'&selectedRBU='+encodeURIComponent(rbu)+'&fromRequest=reRun';
<%-- // var url = '<%=PageflowTagUtils.getRewrittenFormAction("getFilteredChart", pageContext)%>?selectedProduct='+encodeURIComponent(product)+'&selectedProductDesc='+encodeURIComponent(productValue)+'&selectedBU='+encodeURIComponent(bu)+'&selectedRBU='+encodeURIComponent(rbu); --%>

 //document.all.printForm.action = url;
 //document.all.printForm.submit();
  window.location.href = url;
 }
 
  var temp = '/TrainingReports/RBU/getRBU?bu='
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
            var objXMLHttp=null
            if (window.XMLHttpRequest){
            objXMLHttp=new XMLHttpRequest()
            }else if (window.ActiveXObject){
            objXMLHttp=new ActiveXObject("Microsoft.XMLHTTP")
            }
            return objXMLHttp
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
    <%if (reportto == null) {%>
	<tr>
		<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="15"></td>
		<td align="left">
			<table width="100" class="basic_table">
            <tr>
                 <td width="10%"><label>Product:</label></td>       
                    <td>
                    <select name="product" >
                    <option value="Overall">Overall</option>     
                    <option value="ALL">All Products</option>     
                    <%
                        if(thisProductDataBean != null){
                        for(int i=0;i<thisProductDataBean.length;i++){
                        if(selectedProduct.equals(thisProductDataBean[i].getProductCd())){ 
                    %>
                        <option selected value="<%=thisProductDataBean[i].getProductCd()%>"><%=thisProductDataBean[i].getProductDesc()%></option>     
                    <%
                        }
                        else{
                    %>        
                            <option  value="<%=thisProductDataBean[i].getProductCd()%>"><%=thisProductDataBean[i].getProductDesc()%></option>     
                     <%       
                        }
                        }
                        
                        }
                    %>
                    </select>
                   </td>
                </tr>
                <tr>
                 <td width="10%"><label>BU:</label></td>
                    <td>
                    <select name="bu" onchange="getRBUValues()">
                     <option value="ALL">All</option>   
                    <%
                        if(thisBuDataBean != null){
                        for(int i=0;i<thisBuDataBean.length;i++){
                         if(selectedBU.equals(thisBuDataBean[i].getBu())){   
                    %>
                        <option selected value="<%=thisBuDataBean[i].getBu()%>"><%=thisBuDataBean[i].getBu()%></option>     
                    <%
                         }
                         else{
                     %>       
                            
                         <option value="<%=thisBuDataBean[i].getBu()%>"><%=thisBuDataBean[i].getBu()%></option>        
                     <%       
                         }
                        }
                        }
                    %>
                    </select>
                   </td>
                </tr>
                <tr>
                 <td width="10%"><label>RBU:</label></td>
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
                    <img src="<%=AppConst.IMAGE_DIR%>/btn_getreport.gif" onclick="getReports()" />
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
    <%}%>
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
