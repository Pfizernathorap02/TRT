<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.GapAnalysisEntry"%>
<%@ page import="com.pfizer.db.GapAnalysisUIEntry"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.GapAnalysisMainWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<% GapAnalysisMainWc wc = (GapAnalysisMainWc)request.getAttribute(GapAnalysisMainWc.ATTRIBUTE_NAME);
 %>

<script language="javascript">



function selectAll() 
{
var elmList = document.getElementsByName('selProducts')
     for(var i=0;elmList && i<elmList.length;i++) {
           elmList[i].checked = true;
    }
}

function deselectAll() 
{
var elmList = document.getElementsByName('selProducts')
               for(var i=0;elmList && i<elmList.length;i++) {
           elmList[i].checked = false;
    }
}
        
function selectAllOrgCds() 
{
var elmList = document.getElementsByName('selOrgCds')
     for(var i=0;elmList && i<elmList.length;i++) {
           elmList[i].checked = true;
    }
}

function deselectAllOrgCds() 
{
var elmList = document.getElementsByName('selOrgCds')
               for(var i=0;elmList && i<elmList.length;i++) {
           elmList[i].checked = false;
    }
}
            
        

function  validateproducts(){
    var prdSelected = false;
    var elmList = document.getElementsByName('selProducts');

    for(var i=0;elmList && i<elmList.length; i++) 
        {
            if( elmList[i].checked )
            {
                prdSelected = true;
                break;
            }
        }
    if( !prdSelected )
    {
        //alert("Please select at least single product for Gap Report generation!");
        return false;
    }
}

function validateNumber()
{    
	if ((event.keyCode > 32 && event.keyCode < 48) || (event.keyCode > 57 && event.keyCode < 65) || 
		(event.keyCode > 90 && event.keyCode < 97)) 
		event.returnValue = false;
        
	if(event.keyCode < 45 || event.keyCode > 57) 
		event.returnValue = false;

}
function trim()
{
    //alert(document.getElementById('duration').value);
    var duration = document.getElementById('duration').value;
    if(duration.length !=0 && duration.length >1 && duration.charAt(0)=='0')
    {
        duration = duration.substring(1,duration.length);
        document.getElementById('duration').value = duration;
    }    
}
</script>

<%-- <netui:html> --%>
<html>
    <head>
        <title>
            Gap Analysis Report
        </title>
        <link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
        <link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
       
        <script type="text/javascript" language="JavaScript" src="/TrainingReports/resources/js/sorttable.js"></script>
        <script type="text/javascript" language="JavaScript">
        </script>
        
    </head>
    <body>
    
    <br>
    <!-- Infosys - Weblogic to Jboss migration changes start here -->
    <form action="/TrainingReports/gapAnalysis/begin?generate=true">
           <table class="blue_table_without_border" id="form0">
           <!-- <TR><TD><IMG height="25" src="/TrainingReports/resources/images/spacer.gif" /> </TD></TR> -->
	<tr>
        <td>
        	<div class="breadcrumb">
                <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                Gap Analysis Report            
                <!-- Infosys - Weblogic to Jboss migration changes end here -->
        	</div>
        </td>
	</tr>
               <tr>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Deployment Id : Current Quarter 
				<% String[] deploymentPackageId = wc.getdeploymentPackageId();%> 
				<input type="hidden" name="deploymentid" value="<%=deploymentPackageId[0]%>">
				</td>
				<td>&nbsp; &nbsp; Duration :
                <input type="text" name="duration" onkeypress="validateNumber();" onblur="trim();" maxlength="8" <%if(wc.duration!=null){%>value="<%=wc.duration%>"<%}%>>month(s)
                </td>
               <td>&nbsp;&nbsp;<input type="submit" value="Generate" name="generate" 
               onmouseover="this.style.cursor='hand';" onclick="validateproducts();"
              title="Click to generate Gap Analysis Report" ></td></tr>
            
            
            </table >
              <%if(wc.getErrorMsg()!=null){%>
              <br>
              <table class="blue_table_without_border">
              <tr>
              <td> <font color="Red">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><%=wc.getErrorMsg()%></b>
                    </font>
               </td>
              </tr>
              </table>
              <%}%>
              
         
         
         
         <!-- Products div start-->
         <div id="productDiv">
         <%
         Map selectedProdMap=new HashMap();
         if(wc.getProductDesc()!=null && wc.getProductDesc().length>0){
         String[] allProduct=wc.getProductDesc();
         //populate map with user selected product values while generating gap report
         if(wc.getSelectedProdDesc()!=null && wc.getSelectedProdDesc().length>0)
            {
             String[] strUserSelProds=wc.getSelectedProdDesc();
             for(int i=0;i<strUserSelProds.length;i++){
                    selectedProdMap.put(strUserSelProds[i],strUserSelProds[i]);
                }
            }
         
         
         %>
         
         <table border="1" cellspacing="0" cellpadding="0" width="1100" class="blue_table">
        
          <tr width="1100">
            <th>Select Product(s)</th>
          </tr>
          </table>
         
        <table border="0" cellspacing="0" cellpadding="0" width="1100">
        <tr width="1100">
        <td align="right">
            <!--
            <input type="button" align="right" onclick="selectAll()" value="Select All">
            <input type="button" align="right" onclick="deselectAll();validateproducts()" value="Reset All">
            -->
            <A name="Select All" href="#" onClick="selectAll();" style="background-color: white">Select All</A>
            |
            <A name="Reset All" href="#" onClick="deselectAll();" style="background-color: white">Reset All</A>
            &nbsp;&nbsp;&nbsp;&nbsp;
          </td></tr>
          </table>
           
        <table border="1" cellspacing="0" cellpadding="0" width="1100" id="prod_table" class="blue_table">
        
          <%
              boolean oddEvenRow=false;
             int columnCounter=10;
                    for(int i = 0; i < allProduct.length; i++) {
                       
                         if(i%10 ==0 || i==0) {  oddEvenRow = !oddEvenRow; %>
                         
                          <tr class="<%=oddEvenRow?"even":"odd"%>">
                               
                                <%}%>
                                     <TD valign="top" >
                                    <p>
                                    <input type="checkbox" id="selProducts" name="selProducts" <%if(selectedProdMap.containsKey(allProduct[i])){%> checked <%}%> value="<%=allProduct[i]%>">
                                        &nbsp;<%=allProduct[i]%>
                                    </p></td>
                              <% if(i%10 == 9 || i == allProduct.length-1) {%>
                                </TR>
                             <% }
                    }
                             %>
         
         
        
         </table>
         <%}//wc.getProductDesc() if check end
         %>
         </div>
         <!-- Products div end-->
         
         
         <!-- Sales Org Codes div start-->
                  
         <div id="rolestDiv">
         <%
         Map selectedSalesOrgMap=new HashMap();
         //if(wc.getSalesOrgCds()!=null && wc.getSalesOrgCds().length>0){
         //String[] allOrgCds=wc.getSalesOrgCds();
        if(!wc.getSalesOrgCdDesc().isEmpty()){ 
          //populate map with user selected product values while generating gap report
         if(wc.getSelectedSalesOrgCds()!=null && wc.getSelectedSalesOrgCds().length>0)
            {
             String[] strUserSelesOrgs=wc.getSelectedSalesOrgCds();
             for(int i=0;i<strUserSelesOrgs.length;i++){
                    selectedSalesOrgMap.put(strUserSelesOrgs[i],strUserSelesOrgs[i]);
                }
            }
         %>
         
         <table border="1" cellspacing="0" cellpadding="0" width="1100" class="blue_table">
        
          <tr width="1100">
            <th>Select Sales Organization Code(s)</th>
          </tr>
          </table>
         
        <table border="0" cellspacing="0" cellpadding="0" width="1100">
        <tr width="1100">
        <td align="right">
             <A name="Select All" href="#" onClick="selectAllOrgCds();" style="background-color: white">Select All</A>
            |
            <A name="Reset All" href="#" onClick="deselectAllOrgCds();" style="background-color: white">Reset All</A>
            &nbsp;&nbsp;&nbsp;&nbsp;
          </td></tr>
          </table>
           
        <table border="1" cellspacing="0" cellpadding="0" width="1100" id="prod_table" class="blue_table">
        
          <%
              boolean oddEvenRow=false;
             int columnCounter=12;
             int i=0;
             String salesOrgCodefromMap="";
             String salesOrgDescFromMap="";
              Iterator it = wc.getSalesOrgCdDesc().keySet().iterator();
              while(it.hasNext()) { 
                   salesOrgCodefromMap=(String)(it.next());
                    salesOrgDescFromMap=(String)wc.getSalesOrgCdDesc().get(salesOrgCodefromMap);
           
         
                    //for(int i = 0; i < allOrgCds.length; i++) {
                       
                         if(i%12 ==0 || i==0) {  oddEvenRow = !oddEvenRow; %>
                         
                          <tr class="<%=oddEvenRow?"even":"odd"%>">
                               
                                <%}%>
                                     <TD valign="top" >
                                    <p>
                                    <!--input type="checkbox" id="selOrgCds" name="selOrgCds" <%//if(selectedSalesOrgMap.containsKey(allOrgCds[i])){%> checked <%//}%>  value="<%//=allOrgCds[i]%>">
                                        &nbsp;<%//=allOrgCds[i]%>
                                    !-->
                                    <input type="checkbox" id="selOrgCds" name="selOrgCds" <%if(selectedSalesOrgMap.containsKey(salesOrgCodefromMap)){%> checked <%}%>  value="<%=salesOrgCodefromMap%>" title="<%=salesOrgDescFromMap%>" >
                                        &nbsp;<%=salesOrgCodefromMap%>    
                                        
                                    </p></td>
                              <% if(i%12 == 11 || i == wc.getSalesOrgCdDesc().size()-1) {%>
                                </TR>
                             <% }
                    i++;
                    }
                             %>
         
         
        
         </table>
         <%}//wc.getProductDesc() if check end
         %>
         </div>
         
         
         <!-- Roles div ends-->
         
         
        </form>
        
       
        
        <% if (wc.getResult().size()>0) { %>
        <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px"/>&nbsp;
        <% 
            System.out.println("Gap report size = " + wc.getResult().size());
            String duration=wc.getDuration();
            String deploymentId=wc.getDeploymentId();
            
            String[] selProducts=wc.getSelectedProdDesc();
            String[] selOrgCds=wc.getSelectedSalesOrgCds();
            
           
            
            String prodNames2="";
            for(int i=0;i<selProducts.length;i++){
                //System.out.println("selProducts ["+i+"] "+selProducts[i]);
                if(i!=selProducts.length-1){
               prodNames2=prodNames2.concat(selProducts[i]).concat("','");
                }
                else{
                    prodNames2=prodNames2.concat(selProducts[i]);//.concat("'");
                }
            }
             //System.out.println("prodNames2 :: "+prodNames2);
             
        
            
            
           String salesOrgs="";
            for(int i=0;i<selOrgCds.length;i++){
                //System.out.println("selOrgCds ["+i+"] "+selOrgCds[i]);
                if(i!=selOrgCds.length-1){
                    salesOrgs=salesOrgs.concat(selOrgCds[i]).concat("','");
                }
                else{
                    salesOrgs=salesOrgs.concat(selOrgCds[i]);//.concat("'");
                }
            }
             //System.out.println("salesOrgs :: "+salesOrgs);
            
            
            System.out.println("before Complex Query="+new Date()); 
        %>
        <a href="begin.action?downloadExcel=true&generate=true&duration=<%=duration%>&selProducts=<%=prodNames2%>&selOrgCds=<%=salesOrgs%>&deploymentid=<%=deploymentId%>">Download to Excel</a><br>
        <%=wc.getResult().size()%> records found.
        <br>
        <!â€”generate the grid >
        <style type="text/css">
        #table_wrapper {
            position: relative;
            height: 300px;
            overflow-y: scroll;
            
            
        }
        
        thead tr {
            position: relative;
            top: expression(this.offsetParent.scrollTop);
        }
        tbody {
            height: auto;
        }
        
    </style>
       
    
        <div id="table_wrapper">
        <table border="1" cellspacing="0" cellpadding="0" id="tsr_table" class="blue_table">
         <thead>
          <tr>
            <th nowrap>First Name</th>
            <th nowrap>Last Name</th>
            <th nowrap>GUID</th>
            <th nowrap>Role Code</th>
            <th nowrap>Sales Org</th>    
            <th nowrap>Sales Org Code</th> 
            <% 
            
            String[] productDesc=wc.getSelectedProdDesc();
            int[] productTotals = new int[productDesc.length]; 
            int grandTotal = 0;   
            for(int i=0;i<productDesc.length;i++){
            %>
            <th><%=productDesc[i]%></th>
            <% productTotals[i] = 0;
            }	   
            %> 
            <th>Grand Total</th>
          </tr>
         </thead>
         <tbody>
       
          
      <%  boolean oddEvenFlag=false;
                GapAnalysisUIEntry uiEntry = null;
                GapAnalysisEntry empDet = null;
                Iterator it = wc.getResult().keySet().iterator();
                while(it.hasNext()) { 
                    oddEvenFlag = !oddEvenFlag; 
                    uiEntry = (GapAnalysisUIEntry)wc.getResult().get(it.next());
                    empDet = uiEntry.getEntry();
            %>
            

            <tr class="<%=oddEvenFlag?"even":"odd"%>">
                
                <td nowrap><%=Util.toEmpty(empDet.getFirstName())%> </td>
                <td nowrap><%=Util.toEmpty(empDet.getLastName())%></td>
                 <td nowrap><%=Util.toEmpty(empDet.getGuID())%></td>                      
                <td nowrap><%=Util.toEmpty(empDet.getRolecd())%></td>
                <td nowrap><%=Util.toEmpty(empDet.getSalesOrg())%></td>
                <td nowrap><%=Util.toEmpty(empDet.getSalesOrgCd())%></td>
                 
		   
		   <% 
                Map empProdMap = (HashMap)uiEntry.getEmplProd();
                Iterator itr = empProdMap.keySet().iterator();
                String productName=null;
                String status="";
                
                int gapCount=0;
                 for(int i=0;i<productDesc.length;i++){
                    if (null != empProdMap.get(productDesc[i]) && !(empProdMap.get(productDesc[i]).toString().equalsIgnoreCase("null")))
                    {
                       status=empProdMap.get(productDesc[i]).toString();
                        if ( status.equalsIgnoreCase("G") || status.equalsIgnoreCase("C") || status.equalsIgnoreCase("R")) 
                        {
                            gapCount++;
                            grandTotal++;
                            productTotals[i]++;
                        }
                        
                                if(status.equalsIgnoreCase("C")){ 
                                %>
                                <td nowrap bgcolor="Green"><%=(status)%></td>
                                
                                <%}
                                else if(status.equalsIgnoreCase("R")){
                                %>
                                <td nowrap bgcolor="Yellow"><%=(status)%></td>
                                
                                <%}
                                else if(status.equalsIgnoreCase("G")){
                                %>
                                <td nowrap bgcolor="Red"><%=(status)%></td>
                                
                                <%}
                               
                    }else  {
                    %>
                        <td> </td>
                    <% }
                }
                

                     
                
             %>
                <td bgcolor="#9999FF"><%=gapCount%></td>
            </tr>
            


              
    <% } %>
     
        <tr> <td nowrap>Grand Total </td>
            <td colspan="5"></td>
            <% for(int i=0;i<productDesc.length;i++){
            %>
            <td nowrap><%=productTotals[i]%></td>
            <%}%> 
            <td nowrap><%=grandTotal%></td>
            </tr>
       </tbody>
            
    
    </table>
    </div>
    
       
       <table>
       <tr><td>&nbsp;</td></tr>
       <tr><td>&nbsp;</td></tr>
       <tr><td>&nbsp;</td></tr>

       </table>
       
    

        
        <%}else{
         if(wc.getErrorMsg()==null){   %>
        <table class="blue_table_without_border">
              <tr>
              <td><center> <font color="Red">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><h4>No records to Display</h4></b>
                    </font>
                    </center>
               </td>
               
              </tr>
              </table>
        
        <%} }System.out.println("after Complex Query="+new Date());%>


            
            
    </body>
    </html>
<%-- </netui:html> --%>