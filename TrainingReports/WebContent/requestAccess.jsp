<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Language" content="en-us" />
<title>Training Reports</title>
<meta name="ROBOTS" content="ALL" />
<meta http-equiv="imagetoolbar" content="no" />
<meta name="MSSmartTagsPreventParsing" content="true" />
<meta name="Keywords" content="_KEYWORDS_" />
<meta name="Description" content="_DESCRIPTION_" />
<link href="<%=request.getContextPath()%>/resources/css/header.css"
	rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/resources/css/trainning.css"
	rel="stylesheet" type="text/css" media="all" />
<!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
        
<%
	String showDiv="requestDiv";

	System.out.println(request.getAttribute("Result"));
	
	System.out.println(request.getParameter("Result"));

	if (request.getAttribute("Result") != null && request.getAttribute("Result").toString().equalsIgnoreCase("accessRequestSubmitted"))
		showDiv = "closeReqdiv";
	else if (request.getAttribute("Result") != null && request.getAttribute("Result").toString() .equalsIgnoreCase("accessRequestCompleted"))
		showDiv = "closeActiondiv";
	else if (request.getAttribute("Result") != null && request.getAttribute("Result").toString() .equalsIgnoreCase("accessRequestExists"))
		showDiv = "requestExistsDiv";
	else if (request.getAttribute("Result") != null && request.getAttribute("Result").toString() .equalsIgnoreCase("accessRequestNotSubmitted"))
		showDiv = "actionTakenDiv";
	else if (request.getParameter("Result") != null && request.getParameter("Result").toString() .equalsIgnoreCase("accessRequestRejected"))
		showDiv = "requestRejectDiv";
	
	
%>
        
        

<script type="text/javascript" language="JavaScript">
	function showHideNtidDiv(flag) {
		if (flag) {
			document.getElementById('ntidDiv').style.display = 'block';
			document.getElementById('pfizerEmployee').value = 'Yes';
		} else {
			document.getElementById('ntidDiv').style.display = 'none';
			document.getElementById('pfizerEmployee').value = 'No';
		}
	}
	
	function parseAction()
	{
		var condition = '<%=showDiv%>';
		
		document.getElementById('sumitSuccess').style.display = 'none';
		document.getElementById('approveSuccess').style.display = 'none';
		document.getElementById('requestForm').style.display = 'none';
		document.getElementById('requestExists').style.display = 'none';
		document.getElementById('rejectRequest').style.display = 'none';
		document.getElementById('actionTaken').style.display = 'none';
		
		
		if (condition=='requestDiv') 
		{
			document.getElementById('requestForm').style.display = 'block';	
		} else if (condition=='closeReqdiv') 
		{
			document.getElementById('sumitSuccess').style.display = 'block';	
		}		
		else if (condition=='closeActiondiv') 
		{
			document.getElementById('approveSuccess').style.display = 'block';
		}
		else if (condition=='requestExistsDiv') 
		{
			document.getElementById('requestExists').style.display = 'block';
		}
		else if (condition=='requestRejectDiv')
		{
			document.getElementById('rejectRequest').style.display = 'block';
		}
		else if (condition=='actionTakenDiv')
		{
			document.getElementById('actionTaken').style.display = 'block';
		}
		
	}
	
	function selfClose()
	{
		self.close();
	}
	
	
	function emailAddressValidate(str) 
	{ 
	var at="@"
	var dot="."
	var lat=str.indexOf(at)
	var lstr=str.length
	var ldot=str.indexOf(dot)
	if (str.indexOf(at)==-1)
	{
	return false;
	}
	if (str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==(lstr-1))
	{
	return false;
	}
	if (str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.lastIndexOf (dot)==(lstr-1))
	{
	return false;
	}
	if (str.indexOf(at,(lat+1))!=-1)
	{
	return false;
	}
	if (str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot)
	{
	return false;
	}
	if (str.indexOf(dot,(lat+2))==-1)
	{
	return false;
	}
	if (str.indexOf(" ")!=-1)
	{
	return false;
	}
	return true; 
	}

	
	
	function validateForm()
	{
		var sumitForm = true;
		
		if(document.getElementById('lastName').value == null || document.getElementById('lastName').value=='')
			{
				alert('Last Name is a mandatory feild.');
				sumitForm = false;
				return;
			}
			
		else if(document.getElementById('fristName').value == null || document.getElementById('fristName').value=='')
			{
				alert('First Name is a mandatory feild.');
				sumitForm = false;
				return;
			}
		else if(document.getElementById('email').value == null || document.getElementById('email').value=='')
		{
			alert('Email is a mandatory feild.');
			sumitForm = false;
			return;
		}
		
		if(document.getElementById('pfizerEmployee').value=='Yes')
		{
				
				
			 if(document.getElementById('ntid').value == null || document.getElementById('ntid').value=='')
				{
					alert('NTID is a mandatory feild.');
					sumitForm = false;
					return;
				}
			else if(document.getElementById('ntidDomain').value == null || document.getElementById('ntidDomain').value=='')
				{
					alert('NTID Domain is a mandatory feild.');
					sumitForm = false;
					return;
				}
		
			
		}
		
		if(sumitForm)
			 var email = document.getElementById('email').value;
			if(emailAddressValidate(email))
				{
				document.getElementById('requestAccessForm').submit();
				}
			else
				{
				alert('Please enter a valid Email ID.');
				}
			
	}
	
	
	function submitRejection()
	{
		if(document.getElementById('rejectionComments').value == null || document.getElementById('rejectionComments').value=='')
		{
			alert('Rejection comments is a mandatory feild.');
			return;
		}
		document.getElementById('rejectionForm').submit();
	}
	
	function sendReminderMail(userID) 
	{
		document.getElementById('reminderMailForm').submit();
	}
	
	
</script>



</head>
<body id="p-contact" onload="parseAction()">
	<div id="wrap">
		<div id="top_header">
			<div id=header_logo></div>
			<div id=header_title>Training Reports</div>
			<!-- end #top_header -->
		</div>

		<table class="no_space_table"
			background="/TrainingReports/resources/images/h3_background.gif"
			style="background-repeat: repeat-y;" bgcolor="#eff7fc">
			<TR>
				<TD width="100%" valign="middle">
					<!--<h3>Not Authorized</h3> -->
				</TD>
			</TR>
		</TABLE>
		<!-- <div id="top_head" style="margin-left: 7%">
				<h1>Pfizer</h1>
				<h2>Training Reports Application</h2>

			</div> -->
		<div id="main_content" style="margin-left: 5%;">
			<div id="requestForm">
				<h3>Please fill in the below form to help us better serve you!</h3>
				<br>
				<table class="no_space_table">
					<tr>
						<td rowspan="3" width="20"></td>
						<td></td>
					</tr>
					<tr>
						<td><br> <br> <br></td>
					</tr>
					<tr>
						<td>
							<form class="form_basic" action="accessRequest" method="post" id="requestAccessForm" name="requestAccessForm">
								<div>
									<table>
										<tr>
											<td colspan="3"><h5>*All fields are mandatory!</h5></td>
										</tr>
										
										
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
										<tr>
											<td colspan="3"> <input
												type="hidden" name="pfizerEmployee" id="pfizerEmployee"
												value="Yes"></td>
										</tr>
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
										
										
										<tr>
											<td><label>Last Name</label></td>
											<td width="43"></td>
											<td><input type="text" class="text" size="30" value=""
												name="lastName" id="lastName"></td>
										</tr>
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
										<tr>
											<td><label>First Name</label></td>
											<td></td>
											<td><input type="text" class="text" size="30" value=""
												name="fristName" id="fristName"></td>
										</tr>
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
										<tr>
											<td><label>Email</label></td>
											<td></td>
											<td><input type="text" class="text" size="30" value=""
												name="email" id="email"></td>
										</tr>
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
									</table>
								</div>
								<div id="ntidDiv">
									<table>
										<!-- <tr>
											<td><label>Employee Id</label></td>
											<td width="33px"></td>
											<td><input type="text" class="text" size="30" value=""
												name="emplId" id="emplId"></td>
										</tr> -->
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
										<tr>
											<td><label>NTID</label></td>
											<td width="32"></td>
											<td><input type="text" class="text" size="30" value=""
												name="ntid" id="ntid">
											</td>
										</tr>
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
										<tr>
											<td><label>NTID Domain</label></td>
											<td></td>
											<td>
											<select name="ntidDomain" id="ntidDomain">
												<option value="AMER">AMER</option>
												<option value="APAC">APAC</option>
												<option value="MER">MER</option>
												<option value="EMEA">EMEA</option>
											</select>
											
												
											</td>
										</tr>
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
									</table>
								</div>
								<table>
									<tr>
											<td><label>Comments</label></td>
											<td width="46"></td>
											<td><textarea rows="5" cols="22" name="comments" id="comments"></textarea>
												</td>
									</tr>
									<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
										<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
									<tr>
										<td colspan="3"><input type="button" value=" Submit " onclick="validateForm()"></td>
									</tr>
									<tr>
											<td colspan="3"><img
												src="/TrainingReports/resources/images/spacer.gif"
												height="5"></td>
										</tr>
								</table>
							</form>
						</td>
					</tr>
				</table>
			</div>
			<div id="sumitSuccess"><h3>You request has been routed for approval.You will be notified timely by Email about the status of your request.<br><a onclick="selfClose()" style="cursor:pointer">Close.</a></h3></div>
			<div id="approveSuccess"><h3>Your response has been recorded.<br><a onclick="selfClose()" style="cursor:pointer">Close.</a></h3></div>
			<div id="requestExists">
				<h3>You already have a pending request. Please wait till approvers take required action on your previous request!
					<br>
					You can send a reminder mail by <a onclick="sendReminderMail()" style="cursor:pointer">clicking here! </a>
					<br><a onclick="selfClose()">Close.</a>
					<form action="reminderAccessMail" id="reminderMailForm" name="reminderMailForm">
						<input id="reminderMailId" name="reminderMailId" value='<%=request.getAttribute("reminderMailId")%>' type="hidden">
					</form>
					
				</h3>
				</div>
			<div id="rejectRequest">
			<h3>Please Provide Rejection comments below:</h3>
			<form class="form_basic" action="rejectRequest" method="post" id="rejectionForm" name="rejectionForm">
			<%if(request.getParameter("email") != null && request.getParameter("userid") !=null) {%>
			<input type="hidden" name="email" id="email" value="<%=request.getParameter("email")%>">
			<input type="hidden" name="userid" id="userid" value="<%=request.getParameter("userid").toString()%>">
			<%} %>
				<table>
					<tr>
						<td><label>Rejection comments</label></td>
						<td width="46"></td>
						<td><textarea rows="5" cols="22" name="rejectionComments"
								id="rejectionComments"></textarea></td>
					</tr>
					<tr>
						<td colspan="3"><img
							src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
					</tr>
					<tr>
						<td colspan="3"><img
							src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
					</tr>
					<tr>
						<td colspan="3"><input type="button" value=" Submit "
							onclick="submitRejection()"></td>
					</tr>
					<tr>
						<td colspan="3"><img
							src="/TrainingReports/resources/images/spacer.gif" height="5"></td>
					</tr>
				</table>
				</form>
			</div>
			
			<!--added   -->
			
			<div id="actionTaken">
			
				<h4> Action is already taken for this request</h4>
			
			</div>
			<!--  -->
			
		</div>
	</div>
</body>
</html>