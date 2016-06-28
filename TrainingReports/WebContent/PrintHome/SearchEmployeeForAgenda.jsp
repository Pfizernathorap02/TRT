<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%-- <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"%> --%>
<%@ page import="com.tgix.printing.EmployeeListBean"%>
<%@ page import="com.tgix.printing.PrintingConstants"%>
<%@ page import="com.tgix.printing.TRMOrderDateBean"%>
<%@ page import="com.tgix.Utils.PrintUtils"%>
<%@ page import="com.tgix.printing.TrainingWeeks"%>
<%@ page import="java.util.*" %>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>--%>
<!-- <netui:html>  -->
<html>

    <head>
        <title>
           Product Training (PSCPT) - Search Employee for Personalized Agenda
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
           <%--  var url='<%=PageflowTagUtils.getRewrittenFormAction("groupForSelectedCluster_AJAX", pageContext)%>?ClusterType='+encodeURIComponent(cluster); 
            --%>
            var url='/TrainingReports/PrintHome/groupForSelectedCluster_AJAX?ClusterType='+encodeURIComponent(cluster);
            
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
   <%--     var url="<%=PageflowTagUtils.getRewrittenFormAction("dateForSelectedClusterGroup_AJAX", pageContext)%>?ClusterType="+encodeURIComponent(cluster)+"&GroupType="+encodeURIComponent(group); 
      --%>
       var url="/TrainingReports/PrintHome/dateForSelectedClusterGroup_AJAX?ClusterType="+encodeURIComponent(cluster)+"&GroupType="+encodeURIComponent(group); 
       
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
        var url;
        var lastName = '';
        var orderNum = '';
        var firstName= '';
        //The Search is based on the Order Num
          orderNum=document.SearchLOA.employeeNum.value.trim();
//         if(orderNum.length==0)
  //       {
    //       alert('Enter Employee No. to be searched.');
      //     return;
        //  }
          // Get the last name
           lastName = document.SearchLOA.lastName.value;
           firstName = document.SearchLOA.firstName.value;
          var week = document.SearchLOA.week.value;
          if(week.length==0){
            alert('Please select a week to search employees');
            return false;
          }
          <%-- url="<%=PageflowTagUtils.getRewrittenFormAction("employeesForSearchAgenda", pageContext)%>?orderNumber="+encodeURIComponent(orderNum)+"&SearchType=loaEmployee&lastName="+encodeURIComponent(lastName)+"&firstName="+encodeURIComponent(firstName)+"&week="+encodeURIComponent(week);     
           --%>
          url="/TrainingReports/PrintHome/employeesForSearchAgenda?orderNumber="+encodeURIComponent(orderNum)+"&SearchType=loaEmployee&lastName="+encodeURIComponent(lastName)+"&firstName="+encodeURIComponent(firstName)+"&week="+encodeURIComponent(week);    
          
          window.location.href = url;
        }
       function gotoPrint(){
       var orderNum = "<%=request.getAttribute("searchEmployeeNum")%>";
       var lastName = "<%=request.getAttribute("lastName")%>";
       var firstName = "<%=request.getAttribute("firstName")%>";
       var week = "<%=request.getAttribute("week")%>";
       
      <%-- var url="<%=PageflowTagUtils.getRewrittenFormAction("printSearchEmployeeAgenda", pageContext)%>?orderNumber="+encodeURIComponent(orderNum)+"&SearchType=loaEmployee&lastName="+encodeURIComponent(lastName)+"&firstName="+encodeURIComponent(firstName)+"&week="+encodeURIComponent(week);
       --%>
      var url="/TrainingReports/PrintHome/printSearchEmployeeAgenda?orderNumber="+encodeURIComponent(orderNum)+"&SearchType=loaEmployee&lastName="+encodeURIComponent(lastName)+"&firstName="+encodeURIComponent(firstName)+"&week="+encodeURIComponent(week);

      
      var appURL = "<%=request.getRequestURL()%>";
       var requestURL = '';
       if (appURL.indexOf("localhost") != -1 || appURL.indexOf("amrnwlw058") != -1 || appURL.indexOf("trt-dev.pfizer.com") != -1 || appURL.indexOf("trt-dev") != -1){
           requestURL = '<%=PrintingConstants.APPLICATION_PATH_JSP_DEV%>'  + url;
      }else if (appURL.indexOf("tr-tst.pfizer.com") != -1){
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
    
    <body>
    <jsp:include page="HeaderNoHomeLinkForAgenda.jsp" />
    <netui-data:getData resultId="trmGroupMap" value="{pageFlow.trmClusterMap}" />
    <%
        TrainingWeeks trainingWeek[] = null;;
        if(session.getAttribute("trainingWeek")!= null){
        trainingWeek = (TrainingWeeks[])session.getAttribute("trainingWeek");
        }
        TreeMap trmGroupMap=(TreeMap)pageContext.getAttribute("trmGroupMap");
        String employeeNo="";
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
        <b>Search Employee</b>
        </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
			<td><label>First Name:</label></td>
			<td><input class="text" type="text" name="firstName" value="" size="20"></td>
		</tr>
		<tr>
			<td><label>Last Name:</label></td>
			<td><input  class="text" type="text" name="lastName" value="" size="20"></td>
		</tr>
        <tr>
        <td>
        <i>or</i>
        </td>
        </tr>
        <tr> 
        <td>
            <label>Emplid:</label></td>
            <td><input  class="text" type="text" name="employeeNum" value="" size="20"></td>
        </tr>
         <tr>
        <td>
        <i>and</i>
        </td>
        </tr>
        <tr>
           <td>&nbsp;</td>
            <td> 
            <select name="week">
           <%
                if(trainingWeek != null){
                for(int j=0;j<trainingWeek.length;j++){    
                 String weekId = trainingWeek[j].getWeek_id();
           %>
                    <%
                        Date today = new Date();
                        //String newDate = dateFormat.format(today);
                        
                       // Date startDate = dateFormat.parse(trainingWeek[j].getStart_date());
                      //  Date endDate = dateFormat.parse(trainingWeek[j].getEnd_date());
                      //  if(trainingWeek[j].getStart_date().equals(newDate)){
                       // if(today.after(startDate) && today.before(endDate)){
                    %>
                    <option  value="<%=trainingWeek[j].getWeek_id()%>"><%=trainingWeek[j].getWeek_name()%></option>
                    
           <%
                }
                }
           %>
            </select>
            </td> 
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
        <netui-data:getData resultId="employeeResultMap" value="{pageFlow.searchmployeeForAgendaMap}" />
        <%
          if(fromSearch.equalsIgnoreCase("true")){
            //Lets find out how the Search was made 
            if(SearchType.equalsIgnoreCase("loaEmployee")){
                employeeNo=request.getAttribute("searchEmployeeNum")==null?"":request.getAttribute("searchEmployeeNum").toString();            
                printMsgToDisp="For Employee Id: "+employeeNo;
                %>
               <%--
                <b>Search Result For Employee Id:&nbsp;<%=employeeNo%></b> --%>
                <b>Search Results</b>
                <%
            }
            session.setAttribute("printMsgToDisp",printMsgToDisp);
                
            }
        %>
        <br>
         <br>
       <%
         TreeMap employeeReportMap=(TreeMap)pageContext.getAttribute("employeeResultMap");
         EmployeeListBean thisEmployeeListBean;
         boolean msgFlag=false;
         int i=0;
         String backGroundColor="";
         if(employeeReportMap!=null && !employeeReportMap.isEmpty() && fromSearch.equalsIgnoreCase("true")
         ){
        %>
        <input type="button" name="Print_Invitation" value="Print Personalized Agenda" onclick="gotoPrint();">
        <input type="button" name="Close_Window" value="Close" onclick="window.close();">
        <br>
         
        <table width="80%" bordercolor="#9AB9D7" border="1" cellspacing="0"> 
        <tr>
        <td class="ActionsTableHeader" nowrap>SNO.</td>
        <td class="ActionsTableHeader" nowrap>EmplID</td>
        <td class="ActionsTableHeader" nowrap>First Name</td>
        <td class="ActionsTableHeader" nowrap>Last Name</td>
        </tr>
        <%
        if(employeeReportMap != null){
        for(Iterator iter=employeeReportMap.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            backGroundColor="";
        %>
          <tr bgcolor="<%=backGroundColor%>">
          
            <td nowrap ><%=++i%></td>
            
            <td nowrap ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%>&nbsp;</td>            
         </tr>
        <%}
         }
        %>
        </table>
        <%
        }%>
        </div>
    </div>
      
    </body>
<!-- </netui:html> -->
</html>

