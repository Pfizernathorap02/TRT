
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%-- <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"%> --%>
<%@ page import="com.tgix.printing.EmployeeListBean"%>
<%@ page import="com.tgix.printing.PrintingConstants"%>
<%@ page import="com.tgix.printing.TRMOrderDateBean"%>
<%@ page import="com.tgix.Utils.PrintUtils"%>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<html lang="en">
<head>
        <title>
           Product Training (PSCPT) - Search TRM Order
        </title>
        <LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
        <LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
        <LINK href="/TrainingReports/resources/css/style.css" type="text/css" rel="STYLESHEET">
        
    </head>
    <script language="javascript">
        String.prototype.trim = function() { return this.replace(/^\s+|\s+$/, ''); };
        function populate(){
             document.SearchLOA.selectGroupID.disabled=false;
             resetGroupID();
             //Lets Get the selected value , if value =ALL ,then we will populate the Group ID and Date as ALL
             var selCluster = document.SearchLOA.selectCluster;
             if(selCluster.selectedIndex==0){
             document.SearchLOA.selectGroupID.disabled=true;
             }
             var clusterChoice = selCluster.options[selCluster.selectedIndex].value;
             resetGroupID();
             resetDate();
             if(clusterChoice=='ALL'){
             document.SearchLOA.selectDate.disabled=false;
             document.SearchLOA.selectGroupID[1]=new Option("ALL","ALL",true,true);
             document.SearchLOA.selectDate[1]=new Option("ALL","ALL",true,true);
             }else{
             document.SearchLOA.selectDate[1]=new Option("Select A Date","Select A Date",true,true);
             document.SearchLOA.selectDate.disabled=true;
             populateGroupID_AJAX(clusterChoice);
             //First We Will Populate the GroupID with all the ID for which order has been placed
             }//end of the ELSE Loop
         }//end of the Function Populate
         
         function populateGroupID_AJAX(cluster){
            xmlHttp=GetXmlHttpObject();
            if (xmlHttp==null){
            alert ("Browser does not support HTTP Request");
            return;
            }
             <%--  Infosys code changes starts here
              var url='<%=PageflowTagUtils.getRewrittenFormAction("groupForSelectedCluster_AJAX", pageContext)%>?ClusterType='+encodeURIComponent(cluster); 
          --%>

            var url='/TrainingReports/PrintHome/groupForSelectedCluster_AJAX?ClusterType='+encodeURIComponent(cluster); 
            /*  Infosys code changes starts here */
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
            temp = responseRecieved.split(',');
            var ind=1;
            var val;
             for(var counter=0;counter<temp.length;counter++){
             val=temp[counter];
             document.SearchLOA.selectGroupID[ind]=new Option(val.trim(),val.trim());
             ind++;
             }//end of the FOR LOOP
            }//end of the IF Loop
        } //end of the Function StateChanged
        
        
        function populateDate(){
        var selCluster = document.SearchLOA.selectCluster;
        var clusterChoice = selCluster.options[selCluster.selectedIndex].value;
        var selGroupID= document.SearchLOA.selectGroupID;
        var groupChoice=selGroupID.options[selGroupID.selectedIndex].value;
        //Just to make Sure that the Selected Cluster and Selected Group ID index is not ZERO as they are of no use
        if(selCluster.selectedIndex!=0 && selGroupID.selectedIndex!=0){
        populateDate_AJAX(clusterChoice,groupChoice);
            }//end of the IF Loop
        } //end of the Function populateDate
        
       function resetGroupID(){
         var selGroupID= document.SearchLOA.selectGroupID;
         for(var clearOpt=1;clearOpt<selGroupID.options.length;clearOpt++) 
         selGroupID.options[clearOpt]=null;
      
       }// end of reset group id 
       
       
       function resetDate(){
         var selGroupID= document.SearchLOA.selectDate;
         for(var clearOpt=1;clearOpt<selGroupID.options.length;clearOpt++) 
         selGroupID.options[clearOpt]=null;
     
         
       }// end of reset group id 
       
          
       function populateDate_AJAX(cluster,group){
       //alert("Cluster"+cluster+"--Group"+group);
       xmlHttp=GetXmlHttpObject();
       <%-- Infosys code changes here
       var url="<%=PageflowTagUtils.getRewrittenFormAction("dateForSelectedClusterGroup_AJAX", pageContext)%>?ClusterType="+encodeURIComponent(cluster)+"&GroupType="+encodeURIComponent(group); 
        --%>
       
       var url="/TrainingReports/PrintHome/dateForSelectedClusterGroup_AJAX?ClusterType="+encodeURIComponent(cluster)+"&GroupType="+encodeURIComponent(group); 
       /* Infosys code changes ends here */
       xmlHttp.onreadystatechange=stateChangedDate
       xmlHttp.open("GET",url,true);
       xmlHttp.send(null);     
       }//end of the Function populateDate_AJAX
       
       function stateChangedDate(){ 
            document.SearchLOA.selectDate.disabled=false;
            resetDate();
            var responseRecieved; 
            var temp = new Array();
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete"){
            responseRecieved=xmlHttp.responseText;
            temp = responseRecieved.split(',');
            var ind=1;
            var val;
            document.SearchLOA.selectDate[1]=new Option("ALL","ALL");
                for(var counter=0;counter<temp.length;counter++){
                ind++;
                val=temp[counter];
                document.SearchLOA.selectDate[ind]=new Option(val.trim(),val.trim());
                }//end of the FOR LOOP
            }//end of the IF Loop
        } //end of the Function stateChangedDate
        
        
      
        
        function getEmployeeInfo()
        {
          var searchType=getCheckedRadioValue(document.SearchLOA.searchCriteria);
          //Lets find the Search Type First
          if(searchType.length==0)
          {
           alert('Please Select a Search Criteria');
           return;
          }
        var url;
        
        if(searchType=="trmNum")
        {
        //The Search is based on the Order Num
         var orderNum=document.SearchLOA.trmOrderNum.value.trim();
         if(orderNum.length==0)
         {
           alert('Enter TRM Order No. to be searched.');
           return;
          }
         <%-- Infosys code changes starts here
         url="<%=PageflowTagUtils.getRewrittenFormAction("employeesForSearchTRM", pageContext)%>?orderNumber="+encodeURIComponent(orderNum)+"&SearchType=loaOrderNum";     
          --%>
          url="/TrainingReports/PrintHome/employeesForSearchTRM?orderNumber="+encodeURIComponent(orderNum)+"&SearchType=loaOrderNum";     
          /* Infosys code changes ends here */
        }
        
        window.location.href = url;
        }//end of the Function
       
        // return the value of the radio button that is checked
        // return an empty string if none are checked, or
        // there are no radio buttons
        function getCheckedRadioValue(radioObj) {
            if(!radioObj)
                return "";
            var radioLength = radioObj.length;
            if(radioLength == undefined)
                if(radioObj.checked)
                    return radioObj.value;
                else
                    return "";
            for(var i = 0; i < radioLength; i++) {
                if(radioObj[i].checked) {
                    return radioObj[i].value;
                }
            }
            return "";
        }
 
 
       function gotoPrint(){
       var orderNum = "null";
      //var orderNum=document.SearchLOA.trmOrderNum.value.trim();
       <%-- Infosys code changes starts here
       var url="<%=PageflowTagUtils.getRewrittenFormAction("printSearchTRMOrder", pageContext)%>?orderNumber="+encodeURIComponent(orderNum);     
        --%>
      
      var url="/TrainingReports/PrintHome/printSearchTRMOrder?orderNumber="+encodeURIComponent(orderNum);     
      /* Infosys code changes ends here */
      
      var appURL = "<%=request.getRequestURL()%>";
       var requestURL = '';
      if (appURL.indexOf("localhost") != -1 || appURL.indexOf("amrnwlw058") != -1 || appURL.indexOf("trt-dev.pfizer.com") != -1 || appURL.indexOf("trt-dev") != -1){
           requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_DEV%>'  + url;
      }else if (appURL.indexOf("trt-tst.pfizer.com") != -1 || appURL.indexOf("trt-tst") != -1){
            requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_INT%>'  + url;
      
      }else if (appURL.indexOf("trt-stg.pfizer.com") != -1 || appURL.indexOf("trt-stg") != -1){
            requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_STG%>'  + url;
      
      }else if (appURL.indexOf("trt.pfizer.com") != -1 ){
        requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_PROD%>'  + url;
      } 
      // window.open(url,'rr',"left=240, top=170, width=635,height=256,scrollbars=yes,location=no,status=yes,resizable = yes");
       window.open(requestURL,'rr',"left=240, top=170, width=635,height=256,scrollbars=yes,location=no,status=yes,resizable = yes");
       
       
       }
        
              
    </script>
    
    <body onload="document.SearchLOA.searchCriteria.checked=true;window.focus();">
    
 
 <jsp:include page="HeaderNoHomeLink.jsp" />
 
 <!--  <netui-data:getData resultId="trmGroupMap" value="{pageFlow.trmClusterMap}" />
   --> 
   <%
        /* TreeMap trmGroupMap=(TreeMap)pageContext.getAttribute("trmGroupMap");
         */
        TreeMap trmGroupMap=	(TreeMap)request.getAttribute("trmClusterMap");
      /* Infosys code changes ends here */
        TRMOrderDateBean thisTRMGroupBean=null;
        List allDates=new ArrayList();
        String clusterName;
        String orderNumber="";
        String clusterType="";
        String Group_ID="";
        String Selected_Date="";
        String Selected_Box = "";
        String fileNameExcel="";
        String printMsgToDisp="";
        String fromSearch=request.getAttribute("fromSearch")==null?"false":request.getAttribute("fromSearch").toString();
        String SearchType=request.getAttribute("SearchType")==null?"false":request.getAttribute("SearchType").toString();  
        
        %>
 
    
    
    <div style="MARGIN:15PX">
        <p>
        <form name="SearchLOA">
        <table width="700">
        <tr>
        <td class="HeadingStyle">
        <b>Search TRM Order</b>
        </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
       
        <tr>
            <td valign="top"><input type="radio" name="searchCriteria" value="trmNum"  >Please Enter The TRM Order No:&nbsp;<input type="text" align="bottom" maxlength="10"  value="" name="trmOrderNum" >
            and click <b>SEARCH</b> button</td>
        </tr>
    
        <tr>
        <td>&nbsp;</td>
        </tr>
        <tr>
        <td >
        <input type="button" name="Search" value="Search"  onclick="getEmployeeInfo();">
        </td>
        
        </tr>
        </table>
        </form>
        </p>
        
        <div id="displayEmployees" > 
        
        <!-- Infosys code changes starts here
        <netui-data:getData resultId="employeeResultMap" value="{pageFlow.searchTRMEmployeeMap}" />
        <netui-data:getData resultId="employeeResultMapWithHSL" value="{pageFlow.searchTRMEmployeeMapWithHSL}" />
        -->
        <%
          if(fromSearch.equalsIgnoreCase("true")){
            //Lets find out how the Search was made 
            if(SearchType.equalsIgnoreCase("loaOrderNum")){
                orderNumber=request.getAttribute("searchOrderNum")==null?"":request.getAttribute("searchOrderNum").toString();            
                printMsgToDisp="For TRM Order NO: "+orderNumber;
                fileNameExcel="EmployeeListForTRMOrderNO"+orderNumber;
                %>
                <b>Search Result For TRM Order NO&nbsp;:<%=orderNumber%></b>
                <%
                 }
                 if(SearchType.equalsIgnoreCase("loaSelect")) {
                
                Selected_Date=request.getAttribute("Selected_Date")==null?"":request.getAttribute("Selected_Date").toString();            
                Selected_Box = request.getAttribute("Selected_Box")==null?"":request.getAttribute("Selected_Box").toString();            
                Group_ID=request.getAttribute("Group_ID")==null?"":request.getAttribute("Group_ID").toString();
                clusterType=request.getAttribute("clusterType")==null?"":request.getAttribute("clusterType").toString();                        
                printMsgToDisp="For Cluster :"+clusterType+" Group ID :"+Group_ID+" TRM Order Date :"+Selected_Date;
                fileNameExcel="EmployeeListForCluster"+clusterType+"GroupID"+Group_ID+"TRMOrderDate"+Selected_Date;;
                %><b>
                  Search Result For  Cluster&nbsp;:<%=clusterType%> &nbsp;&nbsp;Group ID&nbsp;:<%=Group_ID%>&nbsp;&nbsp;TRM Order Date&nbsp;:<%=Selected_Date%>&nbsp;&nbsp;Shipment Box&nbsp;:<%=Selected_Box%>
                 </b><%
                }
                session.setAttribute("printMsgToDisp",printMsgToDisp);
                session.setAttribute("fileNameExcel",fileNameExcel.trim());
                
            }
        %>
       
        <br>
        
         <br>
      
      
       <%
       
       /*   Infosys code changes starts here  
         TreeMap employeeReportMap=(TreeMap)pageContext.getAttribute("employeeResultMap");
         TreeMap employeeResultMapWithHSL=(TreeMap)pageContext.getAttribute("employeeResultMapWithHSL");  */
        
         
		 TreeMap employeeReportMap=	(TreeMap)request.getAttribute("searchTRMEmployeeMap");
		 TreeMap employeeResultMapWithHSL=	(TreeMap)request.getAttribute("searchTRMEmployeeMapWithHSL");
		 HttpSession sess = request.getSession(); 
		 sess.setAttribute("employeeReportMap", employeeReportMap);
		 sess.setAttribute("employeeResultMapWithHSL", employeeResultMapWithHSL);
         
	/* 	 Infosys code changes ends here */
         
         EmployeeListBean thisEmployeeListBean;
         boolean msgFlag=false;
         int i=0;
         String backGroundColor="";
         if(employeeReportMap!=null && !employeeReportMap.isEmpty() && fromSearch.equalsIgnoreCase("true")
          || employeeResultMapWithHSL!=null && !employeeResultMapWithHSL.isEmpty() && fromSearch.equalsIgnoreCase("true")){
        %>
        
        
        
        <input type="button" name="Print_Invitation" value="Print Shipment Letter" onclick="gotoPrint();">
        <input type="button" name="Close_Window" value="Close" onclick="window.close();">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a  href="#" onclick="window.open('/TrainingReports/PrintHome/SearchTRMExcel.jsp','searchnewExcel','height=0,width=0');">Export To Excel&nbsp;</a>
        
        <br>
         
        <table width="80%" bordercolor="#9AB9D7" border="1" cellspacing="0"> 
        <tr>
        <td class="ActionsTableHeader" nowrap>SNO.</td>
        <td class="ActionsTableHeader" nowrap>TRM ORDER No.</td>        
        <td class="ActionsTableHeader" nowrap>Products</td>                
        <td class="ActionsTableHeader" nowrap>EmplID</td>
        <td class="ActionsTableHeader" nowrap>First Name</td>
        <td class="ActionsTableHeader" nowrap>Last Name</td>
        <td class="ActionsTableHeader" nowrap>Address1</td>
        <td class="ActionsTableHeader" nowrap>Address2</td>
        <td class="ActionsTableHeader" nowrap>City</td>
        <td class="ActionsTableHeader" nowrap>State</td>
        <td class="ActionsTableHeader" nowrap>Zip</td>
        <td class="ActionsTableHeader" nowrap>Training Material</td>
        </tr>
        <%
        if(employeeReportMap != null){
        	System.out.println("inside employeeReportMap "+employeeReportMap.size());
        for(Iterator iter=employeeReportMap.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            backGroundColor="";
        %>
          <tr bgcolor="<%=backGroundColor%>">
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();

            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterPDF = sOrderNo.indexOf("RBU");
             if(iAfterPDF >= 0)
               sOrderNo = sOrderNo.substring(iAfterPDF + 3);
            }
          
          %>
            <td nowrap ><%=++i%></td>
            <td nowrap><%=PrintUtils.ifNullNBSP(sOrderNo)%>&nbsp;</td>            
          <%
          List tempProductList;                      
          tempProductList=new ArrayList();
          
          String sProducts = thisEmployeeListBean.getProducts();
          StringTokenizer st = new StringTokenizer(sProducts, ",");
          while(st.hasMoreTokens())
          {
            String sToken = st.nextToken();
            tempProductList.add(sToken);
          }
          
          
          
          //tempProductList=thisEmployeeListBean.getAllProducts();
          String commaProduct="";
          String tempProducts="";
          String tempUniqueProducts="";
           for(int j=0;j<tempProductList.size();j++){
            String tempCurrentProduct = "" + tempProductList.get(j);
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) commaProduct=","; if(j>=3 && (j%3)==0) commaProduct="<br>";
            // This eliminate the Duplicate Product code being displayed on the screen
            if (tempProducts.indexOf(tempCurrentProduct) < 0){
                tempProducts=tempProducts+commaProduct+tempProductList.get(j);
            }
           }
           if(tempProducts!=null && !tempProducts.equalsIgnoreCase("")){
           %>
           <td nowrap><%=PrintUtils.ifNullNBSP(tempProducts)%></td>
            <%}else{%>            
            <td nowrap>&nbsp;</td>
            <%}%>
            
            
            
            
            <td nowrap ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%>&nbsp;</td>            
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd2())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipCity())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipState())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipZip())%>&nbsp;</td>
         <%
          List tempMaterialList=new ArrayList();
          String sMaterials = thisEmployeeListBean.getMaterials();
          if(sMaterials !=null)
          {
            st = new StringTokenizer(sMaterials, ",");
            while(st.hasMoreTokens())
            {
              tempMaterialList.add((String)st.nextToken());            
            }
          }
              
          //tempMaterialList=thisEmployeeListBean.getMaterials();
          String comma="";
          String tempMaterials="";
           for(int j=0;j<tempMaterialList.size();j++){
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) comma=","; if(j>=3 && (j%3)==0) comma="<br>";
           tempMaterials=tempMaterials+comma+tempMaterialList.get(j);
           }
           if(tempMaterials!=null && !tempMaterials.equalsIgnoreCase("")){
           %>
           <td nowrap><%=PrintUtils.ifNullNBSP(tempMaterials)%></td>
            <%}%>

           <%if(tempMaterials==null){
           %>
           <td nowrap>&nbsp;</td>
            <%}%>
            
          
         </tr>
        <%}
         }
         // For HSL
          if(employeeResultMapWithHSL != null){
        	  System.out.println("inside employeeReportMap "+employeeResultMapWithHSL.size());
        for(Iterator iter=employeeResultMapWithHSL.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeResultMapWithHSL.get(iter.next());
            backGroundColor="";
        %>
          <tr bgcolor="#ffcc66">
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();

            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterPDF = sOrderNo.indexOf("RBU");
             if(iAfterPDF >= 0)
               sOrderNo = sOrderNo.substring(iAfterPDF + 3);
            }
          
          %>
            <td nowrap ><%=++i%></td>
            <td nowrap><%=PrintUtils.ifNullNBSP(sOrderNo)%>&nbsp;</td>            
          <%
          List tempProductList;                      
          tempProductList=new ArrayList();
          
          String sProducts = thisEmployeeListBean.getProducts();
          StringTokenizer st = new StringTokenizer(sProducts, ",");
          while(st.hasMoreTokens())
          {
            String sToken = st.nextToken();
            tempProductList.add(sToken);
          }
          
          
          
          //tempProductList=thisEmployeeListBean.getAllProducts();
          String commaProduct="";
          String tempProducts="";
          String tempUniqueProducts="";
           for(int j=0;j<tempProductList.size();j++){
            String tempCurrentProduct = "" + tempProductList.get(j);
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) commaProduct=","; if(j>=3 && (j%3)==0) commaProduct="<br>";
            // This eliminate the Duplicate Product code being displayed on the screen
            if (tempProducts.indexOf(tempCurrentProduct) < 0){
                tempProducts=tempProducts+commaProduct+tempProductList.get(j);
            }
           }
           if(tempProducts!=null && !tempProducts.equalsIgnoreCase("")){
           %>
           <td nowrap><%=PrintUtils.ifNullNBSP(tempProducts)%></td>
            <%}else{%>            
            <td nowrap>&nbsp;</td>
            <%}%>
            
            
            
            
            <td nowrap ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%>&nbsp;</td>            
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd2())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipCity())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipState())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipZip())%>&nbsp;</td>
         <%
          List tempMaterialList=new ArrayList();
          String sMaterials = thisEmployeeListBean.getMaterials();
          if(sMaterials !=null)
          {
            st = new StringTokenizer(sMaterials, ",");
            while(st.hasMoreTokens())
            {
              tempMaterialList.add((String)st.nextToken());            
            }
          }
              
          //tempMaterialList=thisEmployeeListBean.getMaterials();
          String comma="";
          String tempMaterials="";
           for(int j=0;j<tempMaterialList.size();j++){
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) comma=","; if(j>=3 && (j%3)==0) comma="<br>";
           tempMaterials=tempMaterials+comma+tempMaterialList.get(j);
           }
           if(tempMaterials!=null && !tempMaterials.equalsIgnoreCase("")){
           %>
           <td nowrap><%=PrintUtils.ifNullNBSP(tempMaterials)%></td>
            <%}%>

           <%if(tempMaterials==null){
           %>
           <td nowrap>&nbsp;</td>
            <%}%>
            
          
         </tr>
        <%}
         }
      //  session.setAttribute("excelEmployeeList",employeeReportMap); //Set the Attribute to be Used in EXCEL Report
       // employeeReportMap=null; //Lets make it eligible for Garbage Collection
        
        %>
        </table>
         <%
         if(msgFlag){
            %>
            <br>

            <%
         }
        %>
        
        <%
        }%>

         
        </div>
    </div>
      
    </body></html>