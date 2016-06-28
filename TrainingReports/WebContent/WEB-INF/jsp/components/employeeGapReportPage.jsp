<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.wc.components.EmployeeGapReportWc"%>
<%@ page import="java.util.*"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%            
	EmployeeGapReportWc wc = (EmployeeGapReportWc)request.getAttribute(EmployeeGapReportWc.ATTRIBUTE_NAME);
    String userID = wc.getUser();
%>

<%-- <netui:html> --%>
<html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        <!--<table class="blue_table" width="100%">
        <tr><td width="45%" align="left" valign="top">-->
            <%
                List statusGapList=wc.getStatusForGapList();
                List statusNotGapList=wc.getStatusForNotGapList();
                if(statusGapList!=null || statusNotGapList!=null){
                    List productList=new ArrayList();
                    List statusList=new ArrayList();
                    List activityList=new ArrayList();
                    if(statusNotGapList!=null){
                        for(int i=0;i<statusNotGapList.size();i++){ 
                            Map map1=new HashMap(); 
                            map1=(HashMap)statusNotGapList.get(i);
                            productList.add(map1.get("PRODUCTNAME"));
                            statusList.add(map1.get("STATUS"));
                            activityList.add(map1.get("ACTIVITYID"));
                            System.out.println(map1.get("ACTIVITYID"));
                        }
                    }
                    if(statusGapList!=null){
                        for(int i=0;i<statusGapList.size();i++){ 
                            Map map2=new HashMap(); 
                            map2=(HashMap)statusGapList.get(i);
                            productList.add(map2.get("PRODUCTNAME"));
                            statusList.add("Gap");
                            activityList.add("null");
                        }
                    }
                   
            %>
            
            <table class="blue_table" width="100%">
            <tr><th align="left">Gap Reports for Products Trainings</th></tr>
            <tr><td>
            <%if(productList.size()>0){%>
            <span style="color:red;">
            "Gap" indicates that the product has been assigned to you, but you have not completed or registered for training on this product. 
            <br>If you have not been compensated for promoting this product in the past 12 months and will be selling it, you must register and
            complete the product training ASAP.
            </span>
            <br>
            
            
            <%for(int j=0;j<productList.size();j=j+5){%>
            <table class="blue_table">
            
                <tr><th>Products</th>
                <%
                    for(int i=j;i<productList.size();i++){ 
                %>
                <td><%=productList.get(i)%></td>
                <%if(i==(j+4) && i!=0){break;}}%>
               
                
                </tr>
                <tr><th>Status</th>
                <%
                    for(int i=j;i<statusList.size();i++){
                         
                        String status="";
                        if(statusList.get(i)!=null){
                        status=statusList.get(i).toString();
                        if(status.equalsIgnoreCase("C"))
                            status="Completed";
                            else if(status.equalsIgnoreCase("R"))
                            status="Registered";
                        }
                        
                        String activityID ="";
                        if(activityList.get(i)!=null){
                            activityID = activityList.get(i).toString();
                        }
                if(!status.equalsIgnoreCase("Gap")){
                %>
                <td>
                <%-- Infosys migrated code weblogic to jboss changes start here
                 <a href="/TrainingReports/p2l/employeeSearchDetailPage.do?activitypk=<%=activityID%>&emplid=<%=userID%>">
                --%>
               <a href="/TrainingReports/p2l/employeeSearchDetailPage?activitypk=<%=activityID%>&emplid=<%=userID%>">
               <!-- Infosys migrated code weblogic to jboss changes end here -->
                <%=status%></a></td>
                
                <%}else{%>
                <td><span style="color:red;"><%=status%></span></td>
                <%}if(i==(j+4) && i!=0){break;}}%>
                </tr>
            
            </table>
            <%}}else{%>
            No Products Affiliation found.
            <%}%>
            </td></tr>
            </table>
        <%}%>
       <!-- </td></tr>
        </table>-->
    </body>
    </html>
<%-- </netui:html> --%>