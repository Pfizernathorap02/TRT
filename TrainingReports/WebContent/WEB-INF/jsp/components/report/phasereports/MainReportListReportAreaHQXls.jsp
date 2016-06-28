<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.P2lEmployeeStatus"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<!-- Start: Modified for TRT majoe enhancement 3.6 (employee grid user view)  -->
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<!-- end of modification -->
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<!-- Start: Modified for TRT majoe enhancement 3.6 (employee grid user view)  -->
<%@ page import="com.pfizer.webapp.wc.components.EmployeeGridConfigAdminWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListReportAreaHQWc"%>
<!-- end of modification -->
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListReportAreaWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%
	MainReportListReportAreaHQWc wc = (MainReportListReportAreaHQWc)request.getAttribute(MainReportListReportAreaHQWc.ATTRIBUTE_NAME);
    
%>


<form name="emailSelectForm" id="emailSelectForm">

<div>
	<div  style="float:left;">
    <%int count=0;
        if(wc.getResult()!=null){ 
        for (Iterator it = wc.getResult().iterator(); it.hasNext();) {  
                P2lEmployeeStatus curr = (P2lEmployeeStatus)it.next();
                if(curr.getEmployee().getSource()==null){
                   
                        count++;
                   
                }
        }
        }
    %>
    
	</div>
	
</div>

<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">
    <tr>
        
        <th nowrap>Last Name</th>
        <th nowrap>First Name</th>
        <!-- <th nowrap>Role Code</th>
        <th nowrap>Sales Org</th>
        
        <th nowrap>Manager</th> -->
        
        <th nowrap>P2L Status</th>
        <th nowrap>Status Date</th>
        <th nowrap>NTID</th>
        <th nowrap>Employee Email ID</th>
        <th nowrap>Business Unit Descr</th>
       <!-- <th nowrap>Score</th>-->
        
        <!-- Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid) -->
        <%UserSession uSession = wc.getSession();
        String scoreVisible= uSession.getUser().getScoresFlag(); 
    /*  //   System.out.println(gender);
     //   System.out.println(source); */
        %>
        <% boolean deptId=false, mngr=false, source=false, promoDate=false, location=false, emplID=false, guid=false, geoDesc=false, state = false;%>
        
        <%    
            if(wc.getSelOptHQFields()!= null){  
            Iterator itr = wc.getSelOptHQFields().iterator();  
            while(itr.hasNext())
            {
                String val = (String)itr.next();
                System.out.println(val);
                
                if(val.equals("DEPTID")){
                    deptId=true;%>
               
                 <th nowrap>Department ID</th>
               <% } 
               else if(val.equals("EMPLID")){
                    emplID=true;
                %>
                <th nowrap>Employee ID</th>
                <% }
                else if(val.equals("GUID")){
                    guid=true;
                %>
                <th nowrap>GUID</th>
                <% } 
                else if(val.equals("LOCATION")){
                    location=true;
                %>
                <th nowrap>Location</th>
                <% }
                else if(val.equals("SUPERVISORNAME")) {
                    mngr=true;
                %>
                <th nowrap>Manager</th>
                <% }
                
                             
              
            }
        }
        %>
              <!-- End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid) --> 
       
       
        
    </tr>
    <%  boolean oddEvenFlag=false;
        String hquser=null;
        for (Iterator it = wc.getResult().iterator(); it.hasNext();) { 
            oddEvenFlag = !oddEvenFlag; 
            P2lEmployeeStatus curr = (P2lEmployeeStatus)it.next();
            %>    <!--  // System.out.println("curr.getEmployee()++++++++++"+curr.getEmployee().toString()); -->
            
            <%if(curr.getEmployee().getSource()==null){
           %>
            <tr class="<%=oddEvenFlag?"even":"odd"%>">
            
              
               
                
               <%--  <!--<td><a href="detailpage?activitypk=<%=wc.getActivityPk()%>&emplid=<%=curr.getEmployee().getEmplId()%>"><%=Util.toEmpty(curr.getEmployee().getLastName())%></a></td>--> --%>
                <td><%=Util.toEmpty(curr.getEmployee().getLastName())%></td>
                
                <td><%=Util.toEmpty(curr.getEmployee().getFirstName())%></td>
                
                
               <%--  <!--<td><%//=Util.toEmpty(curr.getEmployee().getManagerLname())%></td>--> --%>
               
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

                <td><%=myDate %> </td><%} 
                else { 
                    %> 
                        <td></td> 
                    <%}%>
                      <!-- mandatory field NTId not present -->
                <td><%=Util.toEmpty(curr.getEmployee().getNtId())%></td>
                <td><%=Util.toEmpty(curr.getEmployee().getEmail())%></td>
                <td><%=Util.toEmpty(curr.getEmployee().getBusinessUnit())%></td>
             <%--   <!-- <td><%=//(curr.getCompleteDate()) %> </td>  -->
                 --%>
              <!--Added for Major Enhancement 3.6 for CSO impact -->
            <%--   <%//if ( scoreVisible.equalsIgnoreCase("Y") ) {%>  
                <!-- <td><%//=Util.toEmpty(curr.getScore())%></td> -->
              <%//}%>  
              <%
         //     System.out.println("uSession.getUser().getSalesPositionTypeCd()="+uSession.getUser().getSalesPositionTypeCd());
         //     System.out.println("curr.getEmployee().getSource()="+curr.getEmployee().getSource());
        //      System.out.println("equals="+(uSession.getUser().getSalesPositionTypeCd()).equals(curr.getEmployee().getSource()));
              // if( uSession.getUser().getScoresVisible().equals("Y") || uSession.getUser().getSalesPositionTypeCd().equals(curr.getEmployee().getSource()))
              //  {%>
                   <!-- <td><%=//Util.toEmpty(curr.getScore())%></td>
               <%// } else{ %>
                    <td></td>-->
              <%//}%> --%>
              <!-- Ends here -->
              
               <!-- Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid) -->
               
              <%--  <%//if(gender){%>
                   <!-- <td><%//=Util.toEmpty(curr.getEmployee().getGender())%></td>-->
                  <%//}%>
               <%//if(promoDate){%>
               <!-- <td><%//=Util.formatDateLong(curr.getEmployee().getPromoDate())%></td>-->
                <%//}%> --%>
                <%if(deptId){%>
                <td><%=Util.toEmpty(curr.getEmployee().getGroupCD())%></td> 
                <%}%>
                <%if(emplID){%>
                <td><%=Util.toEmpty(curr.getEmployee().getEmplId())%></td>
                <%}%> 
                <%if(guid){%>
                <td><%=Util.toEmpty(curr.getEmployee().getGuid())%></td>
                <%}%>
                <%if(location){%> 
                <td><%=Util.toEmpty(curr.getEmployee().getGeographyDesc())%></td>
                <%}%>
                 <%if(mngr){
                  hquser=Util.toEmpty(curr.getEmployee().getHQManager()); 
                  if(hquser!=null || hquser!= ""){
                    String strArr[] = hquser.split(",") ; 
                    String hqManager="";                
                  try{
                  String hqManagerLastName="";
                  String hqManagerFirstName="";
                  hqManagerLastName = strArr[0];
                  hqManagerFirstName = strArr[1];                  
                  
                  hqManager= hqManagerFirstName+" "+hqManagerLastName;                             
                  }
                 catch(Exception e){
                         e.printStackTrace();
                       }
                    %>  
                <td><%=hqManager%></td>
                
                <%}
                else{ %>
                 
               <td> </td>
                 <%}}%>
                 
               <%--  <!--<%//if(emplID){%>
                <td><%=//Util.toEmpty(curr.getEmployee().getEmplId())%></td>
                <%//}%>   -->
                
                <%//if(geoDesc){%>
                <!--<td><%//=Util.toEmpty(curr.getEmployee().getSalesPostionDesc())%></td>-->
                <%//}%>
                <%//if(state){%>
                <!--<td><%//=Util.toEmpty(curr.getEmployee().getState())%></td>-->
                <%//}%> --%>
           
          </tr>
            <!-- End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid) -->
    <%}}%>
    
</table>
</form>
