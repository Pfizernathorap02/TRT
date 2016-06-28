<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.global.EmployeeInfoWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.global.MassEmailWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MassEmailWc wc = (MassEmailWc)request.getAttribute(MassEmailWc.ATTRIBUTE_NAME);
%>


    <script type="text/javascript" language="JavaScript">
var emailWindow;

function submitEmail(isHq) {
    var answer = true;
    var doneflag = false;
	var myform;
    if(isHq=='false'){
        myform = document.emailSelectForm;
     }
     else{
        myform = document.emailSelectFormHQ;
     }
	var emails = ''; 
	var counter = 0;
	var currentUserEmail = '<%=wc.getUser().getEmail()%>';
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
	var subjectStr = '&subject=<%=wc.getEmailSubject()%>';	
	var sendToStr = '&subject=';
	if (counter == 1) {
		sendToStr = emailToStr + emails +  subjectStr;
	} else {
		sendToStr = emailToStr + '?bcc=' + emails + subjectStr;
	}
    if (answer) {
    	window.location = sendToStr;
    } 
}


function checkAll() {
	var myform = document.emailSelectForm;
    //alert(myform.length);
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = true; 
		} 
	}	
}
function unCheckAll() {
	var myform = document.emailSelectForm;
	for(var i=0;i<myform.length;++i) { 
		if(myform.elements[i].type=='checkbox') { 
			myform.elements[i].checked = false; 
		} 
	}	
}

</script>
