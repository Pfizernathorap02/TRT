<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.P2lEmployeeStatus"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<!-- Start: Modified for TRT majoe enhancement 3.6 (employee grid user view)  -->
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<!-- end of modification -->
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<!-- Start: Modified for TRT majoe enhancement 3.6 (employee grid user view)  -->
<%@ page import="com.pfizer.webapp.wc.components.EmployeeGridConfigAdminWc"%>
<!-- end of modification -->
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListReportAreaWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainReportListReportAreaWc wc = (MainReportListReportAreaWc)request.getAttribute(MainReportListReportAreaWc.ATTRIBUTE_NAME);
    
%>
<!-- Added for TRT Phase 2 - F6-->
<script type="text/javascript" language="JavaScript">

checkedManager = false;
     function checkAllManagers(){
        var myform= document.getElementById('emailSelectForm');
   
        if(checkedManager == false) {checkedManager = true;} else {checkedManager=false;}
        var len = myform.length;
        for(var i=0;i < len;i++) {
            if(myform.elements[i].type=='checkbox' && myform.elements[i].name.indexOf('managers')>0){
                 myform.elements[i].checked  =  checkedManager; 
            }
        }
    }
       
    
checkedEmployee = false;
    function checkAllemployees(){
       
        var myform = document.getElementById('emailSelectForm');
        
        if(checkedEmployee == false) {checkedEmployee = true;} else {checkedEmployee = false;}
        var len = myform.length;
        for(var i=0;i < len; i++){
            
            if(myform.elements[i].type=='checkbox' && myform.elements[i].name.indexOf('employees')>0){
                myform.elements[i].checked  =  checkedEmployee;
            }
        }
    }     
</script>
<inc:include-wc component="<%=wc.getMassEmail()%>"/>
<form name="emailSelectForm" id="emailSelectForm">

<div>
	<div  style="float:left;">
<!--// Start: Added for TRT Phase 2 - HQ Users-->
        <%int count=0;
        if(wc.getResult()!=null){ 
        for (Iterator it = wc.getResult().iterator(); it.hasNext();) {  
                P2lEmployeeStatus curr = (P2lEmployeeStatus)it.next();
                if(curr.getEmployee().getSource()!=null){
                  // if(curr.getEmployee().getSource().equals("PFE")){
                        count++;
                   
                //}
        }
        }}
// Ended of HQ Users changes.
    %>
        <%= count %> Trainees
        <!-- Infosys - Weblogic to Jboss Migrations changes start here -->
        <%-- <%=  //wc.getResult().size()  %>  --%>
        <!-- Infosys - Weblogic to Jboss Migrations changes end here -->
        <br>
        Click on employee to get detailed report
	</div>
	<div class="top_table_buttons" style="float:right; align:bottom;">	
    <%
    String xlsActivityId = "";
    if(request.getParameter("activitypk") != null && request.getParameter("activitypk").trim().length() != 0){
        xlsActivityId = request.getParameter("activitypk").trim();
    }else {
        xlsActivityId = wc.getActivityPk();
    }
    %>
		<img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;
		<!-- Infosys - Weblogic to Jboss Migrations changes start here -->
		<%-- <a href="listreport.do?downloadExcel=true&section=<%=wc.getSection()%>&activitypk=<%=xlsActivityId%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; --%>
		<a href="listreport?downloadExcel=true&section=<%=wc.getSection()%>&activitypk=<%=xlsActivityId%>">Download to Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<!-- Infosys - Weblogic to Jboss Migrations changes start here -->
	<!--	<img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll()" /> 
		<img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="unCheckAll()" /> -->
        
        All Employees <input type="checkbox" name='checkallemployees' onclick="checkAllemployees();" value=""/>
		All Managers <input type="checkbox" name='checkallManagers' onclick="checkAllManagers();" value=""/>
        <img id="pos_bottom" src="<%=AppConst.IMAGE_DIR%>/training/b_sendemail.gif" onclick="submitEmail('false')" />
	</div>
	<div class="clear"></div>	
</div>

<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">
    <tr>
        
        <th nowrap>Last Name</th>
        <th nowrap>First Name</th>
        <th nowrap>P2L Status</th>
        
        <th nowrap>Status Date</th>
        <th nowrap>Score</th>
        <th nowrap>Role Code</th>
        <th nowrap>Sales Org</th>
        
        <th nowrap>Manager</th>
        <th nowrap>NTID</th>
        <th nowrap>Employee Email ID</th>
        

        
        <!-- Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid) -->
        <%UserSession uSession = wc.getSession();
        String scoreVisible= uSession.getUser().getScoresFlag(); 
     //   System.out.println(gender);
     //   System.out.println(source);
        %>
        <% boolean gender=false, mngrEmail=false, source=false, promoDate=false, hireDate=false, emplID=false, guid=false, geoDesc=false, state = false,salesPID=false, homeState=false;%>
        
        <%    
            if(wc.getSelOptFields()!= null){  
            Iterator itr = wc.getSelOptFields().iterator();  
            while(itr.hasNext())
            {
                String val = (String)itr.next();
                System.out.println("***"+val);
                
                if(val.equals("EMPLID")){
                    emplID=true;
                %>
                <th nowrap>Employee ID</th>
                <% } 
                
               else if(val.equals("Sex")){
                    gender=true;%>
               
                 <th nowrap>Gender</th>
               <% } 
                else if(val.equals("GEO_DESC")){
                    geoDesc=true;
                %>
                <th nowrap>Geography Description</th>
                <% } 
                else if(val.equals("GUID")){
                    guid=true;
                %>
                <th nowrap>GUID</th>
                <% }
                else if(val.equals("HIRE_DATE")){
                    hireDate=true;
                %>
                <th nowrap>Hire Date</th>
                <% }
                else if(val.equals("MEMAILADDRESS")) {
                    mngrEmail=true;
                %>
                <th nowrap>Manager Email</th>
                <% } 
                else if(val.equals("PROMOTION_DATE")){
                    promoDate=true;
                %>
                <th nowrap>Promotion Date</th>
                <% } 
                else if(val.equals("STATE")){
                    state=true;
                %>
               <th nowrap>Regional Office State</th>
               
              <%  }
                else if(val.equals("SALES_POSITION_TYPE_CD")){
                    source=true;
                %>
                <th nowrap>Source</th>
                <% } 
                else if(val.equals("SALES_POSITION_ID")){
                    salesPID=true;
                %>
                <th nowrap>Sales Position ID</th>
                <!--Added by Swati -->
                <% 
                }
                else if(val.equals("HOME_STATE")){
                    homeState = true;
                %>
                <th nowrap>Home State</th>
                <%
                }
            }
        }
        %>
              <!-- End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid) --> 
       
        <th><input type="checkbox" name="email" class="">Email</th>
        
        <th>Email To Managers<input type="checkbox" onclick="checkAllManagers();" value=""/></th>
        
    </tr>
    <%  boolean oddEvenFlag=false;
        for (Iterator it = wc.getResult().iterator(); it.hasNext();) { 
            
            P2lEmployeeStatus curr = (P2lEmployeeStatus)it.next();
// Added for TRT Phase 2 enhancement - Requirement no. HQ Users %>
  <%                      if(curr.getEmployee().getSource()!=null){
                           //if(curr.getEmployee().getSource().equals("PFE")){
                            oddEvenFlag = !oddEvenFlag; 
// End
                    %>
            <tr class="<%=oddEvenFlag?"even":"odd"%>">
            
                <!-- mandatory field NTId not present -->
                
                
                <%if(uSession.getUser().isHQUser()){%>
                    <td><%=Util.toEmpty(curr.getEmployee().getLastName())%></td>
                <%} else {%>
                <!-- Infosys - Weblogic to Jboss Migrations changes start here -->
                    <%-- <td><a href="detailpage.do?activitypk=<%=wc.getActivityPk()%>&emplid=<%=curr.getEmployee().getEmplId()%>"><%=Util.toEmpty(curr.getEmployee().getLastName())%></a></td> --%>
                    <td><a href="detailpage?activitypk=<%=wc.getActivityPk()%>&emplid=<%=curr.getEmployee().getEmplId()%>"><%=Util.toEmpty(curr.getEmployee().getLastName())%></a></td>
                    <!-- Infosys - Weblogic to Jboss Migrations changes end here -->
                <%}%>
                
                <td><%=Util.toEmpty(curr.getEmployee().getFirstName())%></td>
                <td bgcolor="#FFE87C"><%=Util.toEmpty(curr.getStatus() )%></td>
                
                                <%  if(curr.getCompleteDate()!=null){
                    String sqlDateString=curr.getCompleteDate().toString();
                    SimpleDateFormat myformat=new SimpleDateFormat("mm/dd/yyyy");
                    SimpleDateFormat xformat=new SimpleDateFormat("yyyy-mm-dd");
                    String myDate=null;
                    try{ 
                        myDate=myformat.format(xformat.parse(sqlDateString));
                     //   System.out.println("mydate="+myDate); 
                    }
                       catch(Exception e){
                         e.printStackTrace();
                       }
                 %>

                <td><%=myDate.trim()%></td><%} 
                else { 
                    %> 
                        <td></td> 
                    <%}%>
                    
                                  <%
         //     System.out.println("uSession.getUser().getSalesPositionTypeCd()="+uSession.getUser().getSalesPositionTypeCd());
         //     System.out.println("curr.getEmployee().getSource()="+curr.getEmployee().getSource());
        //      System.out.println("equals="+(uSession.getUser().getSalesPositionTypeCd()).equals(curr.getEmployee().getSource()));
               if( uSession.getUser().getScoresVisible().equals("Y") || uSession.getUser().getSalesPositionTypeCd().equals(curr.getEmployee().getSource()))
                {
                    if ( !("").equals(curr.getScore()) ){
                       //System.out.println("curr.getScore()==="+curr.getScore());
                        
                        double score = Double.valueOf(curr.getScore()).doubleValue();
                        //System.out.println("score==="+score);
                        
                        BigDecimal bd = new BigDecimal(score);
                        int decimalPlaces = 1;
                        
                        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                        score = bd.doubleValue();

                        //System.out.println(score); 
                        %>
                            <td><%=score%></td>
                        <%
                    } else{ %>
                    <td></td>
                    
                    <%}%>
               <% } else{ %>
                    <td></td>
              <%}%>
                <td><%=Util.toEmpty(curr.getEmployee().getRole())%></td>
                <td><%=Util.toEmpty(curr.getEmployee().getSalesOrgDesc())%></td>
                <td><%=Util.toEmpty(curr.getEmployee().getManagerFname().concat(" ").concat(curr.getEmployee().getManagerLname()))!=" "?curr.getEmployee().getManagerFname().concat(" ").concat(curr.getEmployee().getManagerLname()):"Vacant"%></td>
                <!--<td><%=Util.toEmpty(curr.getEmployee().getManagerFname().concat(" ").concat(curr.getEmployee().getManagerLname()))%></td>-->
                <td><%=Util.toEmpty(curr.getEmployee().getNtId())%></td>
                <td><%=Util.toEmpty(curr.getEmployee().getEmail())%></td>
              
                

			<!-- Infosys - Weblogic to Jboss Migrations changes start here -->	
               <%-- <!-- <td><%=//(curr.getCompleteDate()) %> </td>  --> --%>
            <!-- Infosys - Weblogic to Jboss Migrations changes end here -->
                
              <!--Added for Major Enhancement 3.6 for CSO impact -->
              <%//if ( scoreVisible.equalsIgnoreCase("Y") ) {%>  
                <!-- <td><%//=Util.toEmpty(curr.getScore())%></td> -->
              <%//}%>  

              <!-- Ends here -->
              
               <!-- Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid) -->
                <%if(emplID){%>
                <td><%=Util.toEmpty(curr.getEmployee().getEmplId())%></td>
                <%}%> 
                <%if(gender){%>
                <td><%=Util.toEmpty(curr.getEmployee().getGender())%></td>
                <%}%>
                <%if(geoDesc){%>
                <td><%=Util.toEmpty(curr.getEmployee().getSalesPostionDesc())%></td>
                <%}%>
                <%if(guid){%>
                <td><%=Util.toEmpty(curr.getEmployee().getGuid())%></td>
                <%}%>
                <%if(hireDate){%> 
                <td><%=Util.formatDateLong(curr.getEmployee().getHireDate())%></td>
                <%}%>
                <%if(mngrEmail){%>  
                <td><%=Util.toEmpty(curr.getEmployee().getManagerEmail())!=" "?curr.getEmployee().getManagerEmail():"Vacant"%></td>
                <!--<td><%=Util.toEmpty(curr.getEmployee().getManagerEmail())%></td>-->
                <%}%>
                <%if(promoDate){%>
                <td><%=Util.formatDateLong(curr.getEmployee().getPromoDate())%></td>
                <%}%>
                <%if(state){%>
                <td><%=Util.toEmpty(curr.getEmployee().getState())%></td>
                <%}%>
                <%if(salesPID){%>
                <td><%=Util.toEmpty(curr.getEmployee().getSalesPositionId())%></td> 
                <%}%>
                <%if(source){%>
                <td><%=Util.toEmpty(curr.getEmployee().getSourceOrg())%></td> 
                <%}%>
                <%if(homeState){ // Added by Swati %>
                <td><%=Util.toEmpty(curr.getEmployee().getHomeState())%></td> 
                <%}%>

                
         	<!-- Infosys - Weblogic to Jboss Migrations changes start here -->       
          	<%-- <td><input name="MailSelectForm_email_<%=//Util.toEmpty(curr.getEmployee().getEmplId())%>" value="<%=//Util.toEmpty(curr.getEmployee().getEmail())%>"  type="checkbox"> --%> 
        	<%-- <!--  <td><input id="MgrEmail" name="MailSelectForm_email_<%=//Util.toEmpty(curr.getEmployee().getEmplId())%>" value="<%=//Util.toEmpty(curr.getEmployee().getManagerEmail())%>"  type="checkbox"> --> --%>
         	<!-- Infosys - Weblogic to Jboss Migrations changes end here -->
          <td><input name="MailSelectForm_email_employees" value="<%=Util.toEmpty(curr.getEmployee().getEmail())%>"  type="checkbox">   
          <td><input name="MailSelectForm_email_managers" value="<%=Util.toEmpty(curr.getEmployee().getManagerEmail())%>"  type="checkbox">  
          </tr>
            <!-- End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid) -->
    
    <% } }//}%>
</table>
</form>
