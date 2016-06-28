<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpSearch"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.search.DelegateSearchResultListWc"%>

<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.printing.LoggerHelper"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
	DelegateSearchResultListWc wc = (DelegateSearchResultListWc)request.getAttribute(DelegateSearchResultListWc.ATTRIBUTE_NAME);
	List ret = wc.getResults();
    System.out.println("RESULT SET IN SIMULATESEARCHUSERLIST"+ret.size());
%>

<html>
<head>
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
	
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
        <script type="text/javascript" language="JavaScript" src="/TrainingReports/resources/js/sorttable.js"></script>	

 <script type="text/javascript" language="JavaScript">
    function checkCheckRadio() {
        var radiobtnlist = document.form.id;
        var checkedflg = false;
                
        for (var i=0; i < radiobtnlist.length; i++)
        {
          if (radiobtnlist[i].checked)
          {      
              var empid = document.getElementById('empId'+ i).innerHTML;
              var fname = document.getElementById('lName'+ i).innerHTML;
              var lname = document.getElementById('fName'+ i).innerHTML;  
              checkedflg = true;
          }
        }
        if (document.form.id.checked && !checkedflg )
       {
        
         var empid = document.getElementById('empId'+ i).innerHTML;
         var fname = document.getElementById('lName'+ i).innerHTML;
         var lname = document.getElementById('fName'+ i).innerHTML;  
         document.form.fname.value  = fname;
         document.form.lname.value = lname;
         document.form.empid.value = empid;
         document.form.submit();   
       }
        
        if (!checkedflg) {
            return false;
        } else {    
            document.form.fname.value  = fname;
            document.form.lname.value = lname;
            document.form.empid.value = empid;
            document.form.submit();
           
        }       
    }
   

    </script>
    
 <script type="text/javascript" language="JavaScript">
addEvent(window, "load", sortables_init);


function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.id+' ').indexOf("tsr_table") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}
</script>
    
</head>
<body>	
<table width="100%">
<form name="form" action ="/TrainingReports/delegateAccess" method="post" >
<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">
    <% if ( ret != null && ret.size() > 0 ) {  %> 
    <tr>
        <th nowrap>Sales Position ID</th>
        <th nowrap>Last Name</th>
        <th nowrap>First Name</th>
        <th nowrap>Employee Id</th>    
        <th nowrap>Role</th>
        <th nowrap>Business Unit</th>
        <th nowrap>Sales Organization</th>
        <th nowrap>Choose</th>
        
    </tr>
    
        <%  boolean oddEvenFlag=false;
            for (int i=0; i < wc.getResults().size(); i++) {
              oddEvenFlag = !oddEvenFlag; 
              EmpSearch curr = (EmpSearch)wc.getResults().get(i); 
              String lNameID = "lName"+ i;
              String fNameID = "fName"+ i;
              String emplId = "empId"+ i;
              LoggerHelper.logSystemDebug("name1"+ lNameID);
              %>
        
                <tr class="<%=oddEvenFlag?"even":"odd"%>">
                    <td id="salespos">
                    <%=Util.toEmpty(curr.getSalesPosId())%></td>
                    <td><div id="<%=lNameID%>"><%=Util.toEmpty(curr.getLastName())%></div></td>
                    <td><div id="<%=fNameID%>"><%=Util.toEmpty(curr.getFirstName())%></div></td>
                    <td><div id="<%=emplId%>"><%=Util.toEmpty(curr.getEmplId())%></div></td>
                    <td><%=Util.toEmpty(curr.getRoleCd())%></td>
                    <td ><%=Util.toEmpty(curr.getBusUnit())%></td>
                    <td ><%=Util.toEmpty(curr.getSalesOrg())%></td>
                    <td><input type='radio' name='id' value='<%=i%>'></td>
                </tr>
        
        <% } %>
</table>   
    
    <tr>
        <td align ="center" colspan="8">
          <input type="hidden" id="firstname" name="fname"/>
          <input type="hidden" id="lastname" name="lname"/>
          <input type="hidden" id="employeeid" name="empid"/>
    
   
          <input type="hidden" value="<%=wc.getSource()%>" name="source" >
          <input type="button" value="OK" name="OK" onclick="checkCheckRadio()">
        </td>
    </tr>    
    <% } %>
</form>
</td>
</tr>
</table>
</body>
</html>

