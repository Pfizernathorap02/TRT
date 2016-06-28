<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.RBUGuestClassData"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.wc.components.report.GuestTrainerListWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.TrainingScheduleWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%-- <%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"% --%>


<%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");  
    UserSession  uSession = UserSession.getUserSession(request);
    User user = uSession.getUser();
%>
<!-- <netui:html> -->
<html>
    <head>
        <title>
            Product Training (PSCPT) - Guest Trainer List
        </title>
    </head>

    <script type="text/javascript" language="JavaScript">
    self.name = 'mainWin';
    function checkAll(checkname) {
        checkname.checked = true;
        for (i = 0; i < checkname.length; i++)
        checkname[i].checked = true;
    }
    
    function uncheckAll(checkname) {
        checkname.checked = false;
        for (i = 0; i < checkname.length; i++)
        checkname[i].checked = false;
    }

var emailWindow;

function submitEmail() {
	var myform = document.guestform;
	var emails = ''; 
	var counter = 0;
	var currentUserEmail = '<%=user.getEmail()%>';
    var answer = true;
    var doneflag = false;
    
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			if (myform.elements[i].checked) {
				if ( counter == 0 ) {
					emails = myform.elements[i].value;
				} else if ( doneflag ) {
                    myform.elements[i].checked = false;
                } else {
                    var tmpstr = emails + '; ' + myform.elements[i].value; 
                    if ( tmpstr.length > 1950 ) {
                        var answer = confirm('Too many email recipients selected. The first ' + (counter-1) + ' recipients can be emailed now, the others will be de-selected.  Would you like to continue?');
                        if (answer) {
                            myform.elements[i].checked = false;
                            doneflag = true;
                        } else {
                            i = myform.length;
                        }
                    } else {
    					emails = emails + '; ' + myform.elements[i].value;
                    }
				}
				counter = counter + 1;
			} 
		} 
	}
	
	var emailToStr = 'mailto:';	
	var ccString = '';
	var subjectStr = '&subject=RBU Report Follow-up';	
	var sendToStr = '?subject=';
	if (counter == 1) {
		sendToStr = emailToStr + emails +  subjectStr;
	} else {
		sendToStr = emailToStr  + '?bcc=' +  emails + subjectStr;
	}

    if (answer) {
    	window.location = sendToStr;
    } 
}
    </script>
   
    
    <body>
        <div style="margin-left:0px;margin-right:10px">
        <%
        GuestTrainerListWc  wc = (GuestTrainerListWc )request.getAttribute(ListReportWpc.ATTRIBUTE_NAME); 
        List gts = wc.getEmpReport(); 
    
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        %>
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
            <%=request.getParameter("product")%>         
        </h3>      
        <b>Start Date : <%=request.getParameter("startdate")%></b><br><br>
          
        <div style="margin-left:-10px;margin-right:0px">
            <div id="table_inst">
               <%--  Infosys code changes starts here
               <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingSchedule.do?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingSchedule.do">RBU - Training Schedule Summary Report</a>

                <div class="top_table_buttons" style="float:right;">	
                   <img src="<%=AppConst.IMAGE_DIR%>/training/b_add.gif" ONCLICK="window.open('addGuest.do?classid=<%=request.getParameter("classid")%>','_blank','width=400,height=250,scrollbars=yes');" >  
                  <img src="<%=AppConst.IMAGE_DIR%>/training/b_remove.gif" ONCLICK="document.guestform.submit()" >
                    <img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll(document.guestform.selectedguests);" />
                    <img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="uncheckAll(document.guestform.selectedguests);" />
                     <img src="<%=AppConst.IMAGE_DIR%>/training/b_sendemail.gif" onclick="submitEmail()" />
              --%> 
               <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingSchedule?downloadExcel=true">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/RBU/RBUTrainingSchedule">RBU - Training Schedule Summary Report</a>

                <div class="top_table_buttons" style="float:right;">	
                   <img src="<%=AppConst.IMAGE_DIR%>/training/b_add.gif" ONCLICK="window.open('addGuest?classid=<%=request.getParameter("classid")%>','_blank','width=400,height=250,scrollbars=yes');" >  
                  <img src="<%=AppConst.IMAGE_DIR%>/training/b_remove.gif" ONCLICK="document.guestform.submit()" >
                    <img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll(document.guestform.selectedguests);" />
                    <img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="uncheckAll(document.guestform.selectedguests);" />
                     <img src="<%=AppConst.IMAGE_DIR%>/training/b_sendemail.gif" onclick="submitEmail()" />
             <%--  Infosys code changes ends here --%>
              
                </div>

            </div>          
              
        </div>
                    
        <%}%>
       <%-- Infosys code changes starts here
        <form name="guestform" method="post" action="RBUGuestTrainerList.do?command=remove&classid=<%=request.getParameter("classid")%>&product=<%=request.getParameter("product")%>&startdate=<%=request.getParameter("startdate")%>">
         --%>
         <form name="guestform" method="post" action="RBUGuestTrainerList?command=remove&classid=<%=request.getParameter("classid")%>&product=<%=request.getParameter("product")%>&startdate=<%=request.getParameter("startdate")%>">
        <%--  Infosys code changes ends here --%>
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="7"><b>RBU - Guest Trainer List</b></td>
        </tr>
        <tr></tr>
        <%}%>
        <tr>
            <th nowrap>First Name</th>
        	<th nowrap>Last Name</th>
			<th nowrap>EMPLID</th>		
			<th nowrap>Email ID</th>	
            <th nowrap>NT Domain</th>	
            <th nowrap>NT ID</th>		
			<th nowrap>Select</th>
        </tr>
        
        <%for(Iterator i = gts.iterator();i.hasNext();){
            RBUGuestClassData data = (RBUGuestClassData)i.next();
        %>
        <tr>
             <%--  Infosys code changes starts here
             <td><div ONCLICK="window.open('addGuest.do?classid=<%=request.getParameter("classid")%>&firstname=<%=data.getFirstname()%>&emplid=<%=Util.toEmptyNBSP(data.getEmplid())%>&lastname=<%=data.getLastname()%>&email=<%=data.getEmail()%>&nt_domain=<%=Util.toEmptyNBSP(data.getNt_domain())%>&nt_id=<%=Util.toEmptyNBSP(data.getNt_id())%>','_blank','width=400,height=250,scrollbars=yes');"><a href="#"><%=data.getFirstname()%></a></div></td>
             --%> 
             <td><div ONCLICK="window.open('addGuest?classid=<%=request.getParameter("classid")%>&firstname=<%=data.getFirstname()%>&emplid=<%=Util.toEmptyNBSP(data.getEmplid())%>&lastname=<%=data.getLastname()%>&email=<%=data.getEmail()%>&nt_domain=<%=Util.toEmptyNBSP(data.getNt_domain())%>&nt_id=<%=Util.toEmptyNBSP(data.getNt_id())%>','_blank','width=400,height=250,scrollbars=yes');"><a href="#"><%=data.getFirstname()%></a></div></td>
            
             <td><%=data.getLastname()%></td>
             <td><%=Util.toEmptyNBSP(data.getEmplid())%></td>
             <td><%=Util.toEmptyNBSP(data.getEmail())%></td>
             <td><%=Util.toEmptyNBSP(data.getNt_domain())%></td>
             <td><%=Util.toEmptyNBSP(data.getNt_id())%></td>
             <td><input type='checkbox' name="selectedguests" id = "selectedguests" value="<%=data.getEmail()%>"></td>
       </tr>      
        <%
        }
        %>
        
        </table>
              <div style="margin-left:-100px;margin-right:10px">
            <div id="table_inst">
                <div class="top_table_buttons" style="float:right;">
                    <%--  Infosys code changes starts here
                    <img src="<%=AppConst.IMAGE_DIR%>/training/b_add.gif" ONCLICK="window.open('addGuest.do?classid=<%=request.getParameter("classid")%>','_blank','width=400,height=250,scrollbars=yes');" >  
                     --%>
                    <img src="<%=AppConst.IMAGE_DIR%>/training/b_add.gif" ONCLICK="window.open('addGuest?classid=<%=request.getParameter("classid")%>','_blank','width=400,height=250,scrollbars=yes');" >  
                 
                    <img src="<%=AppConst.IMAGE_DIR%>/training/b_remove.gif" ONCLICK="document.guestform.submit()" >
                    <img src="<%=AppConst.IMAGE_DIR%>/training/b_checkall.gif"onclick="checkAll(document.guestform.selectedguests);" />
                    <img src="<%=AppConst.IMAGE_DIR%>/training/b_uncheckall.gif" onclick="uncheckAll(document.guestform.selectedguests);" />
                     <img src="<%=AppConst.IMAGE_DIR%>/training/b_sendemail.gif" onclick="submitEmail()" />
                </div>
            </div>          
              
            </div>  
        </div>
    </body>
</netui:html>