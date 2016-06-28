<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.newHomePageWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.global.EmployeeInfoWc"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%            
	System.out.println(" ****** New Home Page.jsp Begins ****** ");
    newHomePageWc wc = (newHomePageWc)request.getAttribute(newHomePageWc.ATTRIBUTE_NAME);
    
%>


<html>
    <head>
    <script type="text/javascript" language="javascript">
var myW;
var myWin;
function DoThis12() { 
if (myW != null)
  {
    
    if (!myW.closed) {
      myW.focus();
    } 
    
  }

window.name = "main";
myW = window.open("","myW","height=400,width=500,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes") 
myW.location='http://p2l.pfizer.com/SumTotal/app/management/LMS_LearnerReports.aspx?UserMode=0&Mode=1';
}  


function DoThis13() { 
if (myWin != null)
  {
    
    if (!myWin.closed) {
      myWin.focus();
    } 
    
  }

window.name = "main";
myWin = window.open("","myW","height=400,width=500,scrollbars=yes,resizable=yes,statusbar=no,menubar=no,toolbar=no,dependent=yes") 
myWin.location='http://p2l.pfizer.com/SumTotal/app/management/LMS_LearnerHome.aspx?UserMode=1&PersistMode=1';
}  

</script> 
        <script>
        function displayHierarchy(emplid)
        {
            x= document.getElementsByTagName("tr");
            elements = 0;
           for (i=0;i<x.length;i++){
                if (x[i].id == emplid) 
                {
                    document.getElementsByTagName("tr")[i].style.display = 'block';
                    elements++;
                }
              }
            document.getElementById("plus_" + emplid).style.display = 'none';
            document.getElementById("minus_" + emplid).style.display = 'inline';
        }
        
        function hideHierarchy(emplid)
        {
            x= document.getElementsByTagName("tr");
            elements = 0;
           for (i=0;i<x.length;i++){
                 if (x[i].id.indexOf(emplid) > -1) 
                {
                    document.getElementsByTagName("tr")[i].style.display = 'none';
                    elements++;
                }
             }
              y = document.getElementsByTagName("img");
              for (i=0;i<y.length;i++){
                 if (y[i].id.indexOf("plus_" + emplid) > -1) 
                {
                    document.getElementsByTagName("img")[i].style.display = 'inline';
                } 
                if (y[i].id.indexOf("minus_" + emplid) > -1) 
                {
                    document.getElementsByTagName("img")[i].style.display = 'none';
                }
                
             }
        }
        </script>
    </head>
    <body>
        <table class="blue_table_without_border" width="100%" align="center">
        <tr>
            <td colspan="5">
                <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="5">
            </td>
            
        </tr>
        <tr>
        <td colspan="5" align="right">
       <!-- Infosys Migrated code changes starts here 
       <a href="/TrainingReports/reportselect.do">Training Reports</a>&nbsp;&nbsp;&nbsp;
            <a href="/TrainingReports/allEmployeeSearch.do">Employee Search</a>&nbsp;&nbsp;&nbsp;
    --> 
            <a href="/TrainingReports/reportselect">Training Reports</a>&nbsp;&nbsp;&nbsp;
            <a href="/TrainingReports/allEmployeeSearch">Employee Search</a>&nbsp;&nbsp;&nbsp;
            <!-- Migrated code changes ends here  -->
            <% String res = UserSession.getUserSession(request).getIsDelegatedUser();
                boolean sprAdmin = wc.getUser().isSuperAdmin();
                String user1= null;
                user1=wc.getUser().getEmplid();
                System.out.println("#########********"+ user1);
            if (res != null) {
                if(!sprAdmin){%>
             <a href="/TrainingReports/switchUser">Switch User</a>
            <%}}%>&nbsp;&nbsp;&nbsp;
        </td>
        </tr>
        
        <tr><td>
        <br>
        <a><h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Welcome to Training Reporting Tool</h3></a>
        <table class="blue_table_without_border" width="95%" align="center" >
        <tr>
        <td width="45%" align="left" valign="top" >
        <table  class="blue_table_without_border" width="95%" align="center" ><tr>
        <td width="45%" valign="top">
        <inc:include-wc component="<%=wc.getEmployeeInfo()%>"/>
        </td>
        </tr>
        <tr><td>
         <a  onmouseover="this.style.cursor='hand';" onclick="DoThis12();">Click Here</a>&nbsp;to go your Training Transcripts from P2L 
        <br>
        <a onmouseover="this.style.cursor='hand';" onclick="DoThis13();">Click Here</a>&nbsp;to go to your Direct Reports Training Transcripts from P2L 
        </td></tr>
        </table>
        </td>
        <td width="45%" align="left" valign="top">
            <table class="blue_table_without_border" width="100%">
            
            <%-- <tr><td width="40%" valign="top">
            
            
            <inc:include-wc component="<%=wc.getTrainingPath()%>"/></td></tr> --%>
            
            
            <!--<table class="blue_table_without_border" width="100%">-->
            <tr><td width="40%" valign="top"><inc:include-wc component="<%=wc.getEmployeeGapReport()%>"/></td></tr>
            </table>
            
       
        </td>
        <td width="2%"></td>
        </tr>
        </table></td></tr>
        
        <tr>
        <td>
        <table class="blue_table_without_border">
        <!--<tr>
            <td width="2%"></td>
            <td colspan="3">
                 <a  onmouseover="this.style.cursor='hand';" onclick="DoThis12();">Click Here</a>&nbsp;to go your Training Transcripts from P2L 
                  
            </td>
        </tr>
        <tr>
            <td width="2%"></td>
            <td colspan="2">
            <a onmouseover="this.style.cursor='hand';" onclick="DoThis13();">Click Here</a>&nbsp;to go to your Direct Reportees Training Transcripts from P2L 
            </td>
            
            
            <td>
                <img src="<%-- <%=AppConst.IMAGE_DIR%> --%>/spacer.gif" height="5">
            </td>
        </tr>-->
        <% 
            List list=wc.getReportingEmployeeDetailsList();
            if(list.size() != 0){
        %>
        <tr>
         <td width="2%"></td>
         <td>Click on employee to get detailed report.</td></tr>
        <tr>
         <td width="2%"></td>
         <td>Click on +/- to drill down employee reporting.</td></tr>
        </table></td></tr>
        <tr>
         
         <td>

         <table class="blue_table" align="center">
        
        
        <tr>
        <th nowrap>Last Name</th>
        <th nowrap>First Name</th>
        <th nowrap>Employee Id</th>
        <th nowrap>Role</th>
        <th nowrap>Sales Position Id</th>
        <th nowrap>Sales Position Desc</th>
        <th nowrap>Email Id</th>
        <th nowrap>Sales Org</th>
        <th nowrap>NT ID</th>
        </tr>
        <%
                int currentLevel = 1;
                int nextLevel = 1;
                List hierarchy= new ArrayList();
               // Map hierarchyMap = new HashMap();
                for(int i=0;i<list.size();i++){
                    String currentLabel = "";
                Map detailsMap=new HashMap();
                Map detailsMapNextElement=new HashMap();
                detailsMap=(HashMap)list.get(i);
                currentLevel = new Integer(detailsMap.get("LEVEL").toString()).intValue();
               
               //* System.out.println(" Current Level is "+currentLevel);
                //System.out.println(" Employee Id      "+detailsMap.get("EMPLOYEEID").toString());
                //System.out.println(" Employee Id      "+detailsMap.get("SALESPOSITIONID").toString());
               // System.out.println(" we got it ");
                //hierarchyMap.put(new Integer(currentLevel -1),detailsMap.get("EMPLOYEEID").toString());
                //hierarchy.add(currentLevel-1,detailsMap.get("EMPLOYEEID").toString());
               /* Code added for Vacant sales position issue*/
                hierarchy.add(currentLevel-1,detailsMap.get("SALESPOSITIONID").toString());
              
                if (i<(list.size()-1))
                {
                    
                    detailsMapNextElement=(HashMap)list.get(i+1);
                    nextLevel = new Integer(detailsMapNextElement.get("LEVEL").toString()).intValue();
                }else
                {
                    nextLevel = 1;
                }
                /* Code added for the Vacant Sales Position issue*/
                /*  This Code is added to prevent null records to be displayed */ 
                 if(detailsMap.get("EMPLOYEEID") == null && (nextLevel <= currentLevel || detailsMapNextElement.get("EMPLOYEEID") == null ))
                {continue;}
               
               if (currentLevel > 1){
for (int levelIndex = 0;levelIndex < (currentLevel-1);levelIndex ++)
{
    currentLabel = currentLabel + "_" + hierarchy.get(levelIndex);
   // currentLabel = currentLabel + "_" + hierarchyMap.get(new Integer(levelIndex));
}
//System.out.println("** Current Label **"+currentLabel);
%>
<tr id="<%=currentLabel%>" style="display:none">
<%                
}else{
%><tr><%}%>
    <td width="16%">
    <% for(int space=1;space < currentLevel ;space++){%>&nbsp;&nbsp;<%}
        if (currentLevel > 1){
            
            /*Code added to remove null elements in the hierarchy(Vacant Sales Position Issue).
            */
            if (nextLevel == currentLevel && detailsMapNextElement.get("EMPLOYEEID") != null)
            {%><img style="display:inline;" src="<%=AppConst.IMAGE_DIR%>/linemiddlenode.gif"><%
            }else 
            {%><img style="display:inline;" src="<%=AppConst.IMAGE_DIR%>/linelastnode.gif"><%
            }
        }
    if (nextLevel > currentLevel && detailsMapNextElement.get("EMPLOYEEID") != null){
        currentLabel = currentLabel + "_" + hierarchy.get(currentLevel-1);
       // currentLabel = currentLabel + "_" + hierarchyMap.get(new Integer(currentLevel-1));
       %>
<img id="plus_<%=currentLabel%>"  style="display:inline;" src="<%=AppConst.IMAGE_DIR%>/treeview/plus.gif" onclick="displayHierarchy('<%=currentLabel%>');">
<img id="minus_<%=currentLabel%>" style="display:none;" src="<%=AppConst.IMAGE_DIR%>/treeview/minus.gif" onclick="hideHierarchy('<%=currentLabel%>');">
 <%}%>
       
        <% 
        
        
        /*  To display records with role_cd,sales_position_id_desc,sales_group(Vacant Sales Position issue)*/
        if(detailsMap.get("EMPLOYEEID") != null){%>
     <%-- Infosys code Changes start here  
     <a href="/TrainingReports/p2l/employeeSearchDetailPage.do?emplid=<%=detailsMap.get("EMPLOYEEID")%>"><%=detailsMap.get("LASTNAME")%></a></td>
        --%>
        <a href="/TrainingReports/p2l/employeeSearchDetailPage?emplid=<%=detailsMap.get("EMPLOYEEID")%>"><%=detailsMap.get("LASTNAME")%></a></td>
        <!-- Infosys code changes end here -->
        <td width="10%"><%=detailsMap.get("FIRSTNAME")%></td>
        <td width="9%"><%=detailsMap.get("EMPLOYEEID")%></td>
        <td width="7%"><%=detailsMap.get("ROLE")%></td>
        <td width="12%"><%=detailsMap.get("SALESPOSITIONID")%></td>
        <td width="16%"><%=detailsMap.get("SALESPOSITIONDESC")%></td>
        <% if (detailsMap.get("EMAILID")!= null){%>
        <td width="14%"><a href="mailto:<%=detailsMap.get("EMAILID")%>"><%=detailsMap.get("EMAILID")%></a></td>
        <%}else{%>
        <td width="14%"></td>
        <%}%>
        <td width="8%"><%=detailsMap.get("SALESORG")%></td>
        <td width="8%"><%=detailsMap.get("NTID")%></td>
        
        <%}  else{%>            
        Vacant </td> 
        <td width="10%"></td>
        <td width="9%"></td>
        <td width="7%"><%=detailsMap.get("ROLE")%></td>
        <td width="12%"><%=detailsMap.get("SALESPOSITIONID")%></td>
        <td width="16%"><%=detailsMap.get("SALESPOSITIONDESC")%></td>
        <td width="14%"></td>
        <td width="8%"><%=detailsMap.get("SALESORG")%></td>
        <td width="8%"></td>         
        <%}%>        
        </tr>  
        
        
       <%
        }%>
        <tr><td colspan="9" height="0"></td></tr>
        
        
        <%
        } else {%>
        <tr><td>No reporting employees found.</td></tr>
        <%}%>
        </table></td></tr>
        </table>
    </body>
</html>