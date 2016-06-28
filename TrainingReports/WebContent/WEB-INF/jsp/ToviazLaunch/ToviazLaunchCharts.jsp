<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"%>
<%-- <%@ page import="com.bea.wlw.runtime.core.dispatcher.HttpRequest"%> --%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.chart.PieChartBuilder"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.RBU.RBUChartsWc"%>
<%@ page import="com.pfizer.webapp.wc.RBU.RBUPLCChartsWc"%>
<%@ page import="com.pfizer.webapp.wc.ToviazLaunch.ToviazLaunchChartsWc"%>

<%@ page import="com.pfizer.webapp.wc.components.HomepageWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartDetailWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartHeaderWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ChartLegendWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.RBUChartHeaderWc"%>
<%@ page import="com.pfizer.webapp.wc.components.chart.ToviazLaunchChartHeaderWc"%>
<%@ page import="com.tgix.rbu.FutureAllignmentBuDataBean"%>
<%@ page import="com.tgix.rbu.FutureAllignmentRBUDataBean"%>
<%@ page import="com.tgix.rbu.ProductDataBean"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%
	ToviazLaunchChartsWc wc = (ToviazLaunchChartsWc)request.getAttribute(RBUChartsWc.ATTRIBUTE_NAME);
   // RBUChartsWc wc = new RBUChartsWc();
    ToviazLaunchChartHeaderWc chartHeaderWc=(ToviazLaunchChartHeaderWc)wc.getHeader();
     FutureAllignmentBuDataBean thisBuDataBean[];
    //thisBuDataBean = (FutureAllignmentBuDataBean[])request.getAttribute("bu");
    thisBuDataBean = wc.getFutureAllignmentBuDataBean();
     FutureAllignmentRBUDataBean thisRbuDataBean[];
    //thisRbuDataBean = (FutureAllignmentRBUDataBean[])request.getAttribute("rbu");
    thisRbuDataBean = wc.getFutureAllignmentRBUDataBean();
    String overAll = wc.getFirstRequest();
    
    String selectedProduct ="";
     String selectedBU = "";
      String selectedRBU = "";
    if(wc.getSelectedProduct() != null){
     selectedProduct =  wc.getSelectedProduct();
    }
    if(chartHeaderWc.getBu() != null){
        selectedBU = chartHeaderWc.getBu();
    }
    if(chartHeaderWc.getRbu() != null){
        selectedRBU = chartHeaderWc.getRbu();
    }
    String fromRequest = "";
    if(session.getAttribute("fromRequest") != null){
            fromRequest = (String)session.getAttribute("fromRequest");
            session.removeAttribute("fromRequest");
    }
%>
<style type="text/css">
.widthed{
width:100px;
} 
</style>
 <script language="javascript">
 function getReports(from){
 var bu = document.getElementById("bu").value;
 var rbu = document.getElementById("rbu").value;
<%-- //var url = '<%=PageflowTagUtils.getRewrittenFormAction("beginToviazLaunch", pageContext)%>?selectedBU='+encodeURIComponent(bu)+'&selectedRBU='+encodeURIComponent(rbu); --%>
  var url = '';
  if(from ==''){
    url = '/TrainingReports/ToviazLaunch/beginToviazLaunch?selectedBU='+encodeURIComponent(bu)+'&selectedRBU='+encodeURIComponent(rbu);
  }
  else{
    url = '/TrainingReports/ToviazLaunch/beginToviazLaunchExec?selectedBU='+encodeURIComponent(bu)+'&selectedRBU='+encodeURIComponent(rbu);
  }
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
<table class="basic_table">
	<tr>
		<td colspan="3">
			<table class="basic_table">
				<tr>
					<td align="center">	
                   <%--
                    <label>  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;Charts based on </label><%=chartHeaderWc.getNumTrainees()%> 

			<label>Trainees.</label>--%>
                    	
			&nbsp;&nbsp;&nbsp;<label>BU:</label> <%=chartHeaderWc.getBu()%>&nbsp;&nbsp;

			<label>RBU:</label> <%=chartHeaderWc.getRbu()%>&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25"></td>
                    
                    <%
                
                    
                    %>
				</tr>				
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top" width="100%">
			<table class="basic_table">
				<tr>
					<td valign="top" align="center" >
                     <inc:include-wc component="<%=wc.getWebComponent()%>"/>
					</td>
				</tr>
			</table>
		</td>
		<td valign="top" align="right">
			<table class="no_space_width" height="100%" >
				<tr>
					<td class="thin_white" height="10"><img src="<%=AppConst.IMAGE_DIR%>/spacer.gif"></td>
				</tr>			
				<tr>
					<td class="thin_grey" height="550"></td>
				</tr>			
			</table>
		</td>
		<td valign="top">
            <%-- <inc:include-wc component="<%=wc.getTerritoryForm()%>"/> --%>
            <table width="500" class="basic_table">
                <tr>
                <td><label>BU:</label></td>
                    <td>
                    <select name="bu"  >
<%--
                    <%
                      if(selectedBU.equalsIgnoreCase("ALL")){
                    %>
                    <option  selected value="ALL">All</option>     
                    <%
                        }
                        else{
                    %>
                   
                     <option   value="ALL">All</option>   
                      --%>
                     <%
                       // }
                        if(thisBuDataBean != null){
                        for(int i=0;i<thisBuDataBean.length;i++){
                        if( thisBuDataBean[i].getBu().equals("Primary Care")){
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
                        }
                    %>
                    </select>
                   </td>
                </tr>
                <tr>
                 <td><label>RBU:</label></td>
                    <td>
                    <select name="rbu">
                     <%
                      if(selectedRBU.equalsIgnoreCase("ALL")){
                    %>
                    <option  selected value="ALL">All</option>     
                    <%
                        }
                        else{
                    %>
                     <option   value="ALL">All</option>   
                     <%
                        }
                    
                        if(thisRbuDataBean != null){
                        for(int i=0;i<thisRbuDataBean.length;i++){
                       if(selectedRBU.equals(thisRbuDataBean[i].getRbu())){     
                    %>
                        <option selected value="<%=thisRbuDataBean[i].getRbu()%>"><%=thisRbuDataBean[i].getRbu()%></option>     
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
                    <td>
                     <img src="<%=AppConst.IMAGE_DIR%>/btn_getreport.gif" onclick="getReports('<%=fromRequest%>')" />
                </tr>

            </table>
		</td>	
	</tr>
</table>
  </form>
