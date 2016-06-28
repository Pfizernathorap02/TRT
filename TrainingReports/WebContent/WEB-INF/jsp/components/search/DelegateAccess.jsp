<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.DelegatedEmp"%>
<%@ page import="com.pfizer.db.EmpSearch"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.processor.OverallResult"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.search.DelegateAccessWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SimulateSearchResultListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.search.SwitchUserWc"%>
<%@ page import="com.pfizer.db.DelegateBean"%>
<%@ page import="com.tgix.Utils.LoggerHelper"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	DelegateAccessWc wc = (DelegateAccessWc)request.getAttribute(DelegateAccessWc.ATTRIBUTE_NAME);
    DelegatedEmp[] arr = wc.getList();
    
   
    String source = (String)session.getAttribute("source");
    String fromtxtField = "";
    String fromEmpID = "";
    if (wc.getFromBean() != null) {
        
        fromtxtField = wc.getFromBean().getlname() + wc.getFromBean().getfName();
        LoggerHelper.logSystemDebug(fromtxtField);
        fromEmpID = wc.getFromBean().getempId();
    }    
        
    String totxtField = "";
    String toEmpID = "";
    if (wc.getToBean() != null) {
        totxtField = wc.getToBean().getlname() + wc.getToBean().getfName();               
        toEmpID = wc.getToBean().getempId();
    }    
%>

<html>
<head>
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/header.css"/>
	
		<link rel="stylesheet" type="text/css" href="/TrainingReports/resources/css/trainning.css"/>
        <script type="text/javascript" language="JavaScript" src="/TrainingReports/resources/js/sorttable.js"></script>	

<script type="text/javascript" language="JavaScript">
    
    function checkCheckRad() {
     var radiobtnlist = document.form1.name;
     var checkedflg = false;
    for (var i=0; i < radiobtnlist.length; i++)
    { 
     if (radiobtnlist[i].checked)
       {
      
    
         var delfrid = document.getElementById('delfromid'+i).innerHTML;
         var deltoid = document.getElementById('deltooid'+i).innerHTML;
         checkedflg = true;
         
       }
    }  
        
     if (document.form1.name.checked && !checkedflg )
       {
         var delfrid = document.getElementById('delfromid'+i).innerHTML;
         var deltoid = document.getElementById('deltooid'+i).innerHTML;
         document.form1.delfrhid.value  = delfrid;
         document.form1.deltohid.value = deltoid;
         document.form1.submit();   
       }
      if (!checkedflg) {
       return false;
        } 
        
      else {
      document.form1.delfrhid.value  = delfrid;
      document.form1.deltohid.value = deltoid;
      document.form1.submit();   
     
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
<table width="100%" class="no_space_table">
<br>
<tr>
<td align="center">Select users to provide delegated access:</td>
</tr>
<tr>
<td>
<br>
        <table width="100%" class="no_space_table">
            <tr width="50%">
                <td>Delegated From:</td>
               
                <td><input title="Click on search button to select users" class="text" type="text" name="Fempid" value="<%=fromtxtField%>" size="20" maxlength="20" readonly></td>
                <td>
                <input name="" type="image" src="/TrainingReports/resources/images/btn_search2.gif" onclick="window.location='/TrainingReports/delegateSearch?source=from';"  style="margin-top:20px;">
                </td>
            
                <td>Delegated To:</td>
               
                <td><input title="Click on search button to select users" class="text" type="text" name="Toempid" value="<%=totxtField%>" size="20" maxlength="20" readonly></td>
                 <td> 
                <input name="" type="image" src="/TrainingReports/resources/images/btn_search2.gif" onclick ="window.location='/TrainingReports/delegateSearch?source=to'" style="margin-top:20px;">
                </td>
            </tr>
           
            
            <tr width= "45%">           
                
                <td align ="right" colspan="3">
                
                <form name="form" action ="/TrainingReports/delegateAccess" method = "post" >
                    <input type="hidden" id="feid" name="fempid" value="<%= fromEmpID%>" />
                    <input type="hidden" id="toeid" name="toempid" value="<%= toEmpID%>" />             
                
                    <input name="save" value="Save" type="submit">&nbsp;
                </form>       
                </td>
                <td align = "left" colspan="3">
                <form name="form2" action ="/TrainingReports/delegateAccess" method = "post" >
                    
                    &nbsp;<input name="clear" value="Clear" type="submit">
                </form>                             
                </td>   
            </tr>
        </table>
        
</td>
</tr>
<tr>
<%=wc.getErrorMsg()%></tr>
<tr>
<td align="center">Select an entry to revoke delegated access:</td>
</tr>
</table>      



           
<%
System.out.print("arr length="+arr.length);

 if (wc.getListEmpty()) {%>  
  No Delegated Users
<%} else {
    System.out.println("length" + wc.getList());%>    
<form name="form1" action ="/TrainingReports/delegateAccess" method = "post"  >


<table cellspacing="0" id="tsr_table" width="100%" class="blue_table">
  
    
    <tr>
    
        <th nowrap>From Id</th>
        <th nowrap>From Name</th>
        <th nowrap>To Id</th>
        <th nowrap>To Name</th>
        <th nowrap>Select</th>
        
        
    </tr>
        <%  boolean oddEvenFlag=false;
        
            for (int i=0; i < wc.getList().length; i++) {
              oddEvenFlag = !oddEvenFlag; 
              DelegatedEmp curr = arr[i]; 
               
              String delfr = "delfrom"+ i;
              String delto = "deltoo"+ i;
              String delfrId = "delfromid" + i;
              String deltoId = "deltooid" + i;
              
              %>
        
                <tr class="<%=oddEvenFlag?"even":"odd"%>">
                    <td><div id="<%=delfrId%>"><%=Util.toEmpty(curr.getdelfrId())%></div></td>
                    <td><div id="<%=delfr%>"><%=Util.toEmpty(curr.getdelfr())%></div></td>
                    <td><div id="<%=deltoId%>"><%=Util.toEmpty(curr.getdeltoId())%></div></td>
                    <td><div id="<%=delto%>"><%=Util.toEmpty(curr.getdelto())%></div> </td>
                    <td><input type='radio' name='name' value='<%=i%>'></td>
                </tr>
            <%}%>
       
  
</table> 

<table width =100%>
<tr>
       <td align ="center" colspan="3">
       
         <input type="hidden" id="delfrom" name="delfrhid"/>
         <input type="hidden" id="delto" name="deltohid"/>
         <input type="submit" value="Remove" name="remove" onclick="checkCheckRad()">
    
    
    
</td>
</tr>
</table>              
<%}%>
</form>
<br>
<br>
<br>

</body>
</html>

