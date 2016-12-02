package com.pfizer.action;

import java.util.Calendar;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.pfizer.dao.TransactionDB;
import com.pfizer.db.AccessRequest;
/*import weblogic.management.timer.Timer;*/
//////////////////////////////////////////
//import com.tgix.Utils.MailUtil;
/* Infosys migrated code weblogic to jboss changes start here
 * import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.runtime.ServerRuntimeMBean;
Infosys migrated code weblogic to jboss changes end here
*/
import com.tgix.Utils.MailUtil;
import com.pfizer.hander.*;

public class RequestAccess implements
ServletRequestAware, ServletResponseAware
{
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	
	private final static String TRTTST = "trt-tst.pfizer.com/";
	private final static String TRTSTG = "trt-stg.pfizer.com/";
	private final static String TRTPROD = "trt.pfizer.com/";
	private final static String TRTLOCL = "localhost:8080/";

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	public void sendRequestEmail(AccessRequest request) throws AddressException, MessagingException
	{
		 String requestURL= getRequest().getRequestURL().toString();
		
		 String mailURL= "";
		 
		 if(requestURL != null && requestURL.toString().contains(TRTPROD))
			 mailURL=TRTPROD;
		 else if(requestURL != null && requestURL.toString().contains(TRTSTG))
			 mailURL=TRTSTG;
		 else if(requestURL != null && requestURL.toString().contains(TRTTST))
			 mailURL=TRTTST;
		 else if(requestURL != null && requestURL.toString().contains(TRTLOCL))
			 mailURL=TRTLOCL;
		
		 AccessApproverHandler handler = new AccessApproverHandler();
		 
		 String emailTo[] = handler.getAccessApproversEmails();
	   
	     String sSubject = "Access request in TRT system.";
	     
	     String emailBody =
	    		 "<font style='font-family: sans-serif;color: #2f00b2;'>Dear Approver,"
	     		 +"<br>"+"Please take the required action on the below access request:"
	    		
	     		 +"<br><br>"+"Name:"+request.getLastName()+" "+request.getFirstName()+""

			     +"<br>Pfizer Employee:"+" "+request.getPfizerEmployee()
			     
			     +"<br>Email:"+" "+request.getEamilID();
		
			     if(request.getPfizerEmployee().equalsIgnoreCase("Yes"))
			     {
			    	 emailBody += "<br>"+"Emplid:"+" "+request.getEmployeeId()
			    				
						     +"<br>"+"NTID:"+" "+request.getNtid()
					
						     +"<br>"+"NT Domain:"+" "+request.getNtidDomain();			 
			     }
			     
			     
			     emailBody += "<br>Comments:"+" "+request.getComments() 
			    		 +"</font><br><br>";
			     
			     
		
	     		emailBody +="<table style='width:1000%;text-align: center;font-size: 25;'>"
				+"<tr>"
				+"<td style='width:12%;background-color: #90c140;border: #3D9E69 3px solid;'>"
				+ "<a href='http://"+mailURL+"TrainingReports/admin/edituser?isRequestAccess=true&lastName="+ request.getLastName() +"&firstName="+request.getFirstName()+"&email="+request.getEamilID()+"&emplId="+request.getEmployeeId()+"&ntid="+request.getNtid()+"&ntdomain="+request.getNtidDomain()+"&userid="+request.getId()+"' "
				+ "style='text-decoration:none;color:whitesmoke'><div style='margin-top:12%'>Approve<br></div></a></td>"
				+"<td style='width:5%'></td>"
				+"<td style='width:12%;background-color: #fc5720;border: #fc1616 3px solid;'>"
				+ "<a href='http://"+mailURL+"TrainingReports/requestAccess.jsp?&email="+request.getEamilID()+"&userid="+request.getId()+"&Result=accessRequestRejected' "
				+ "style='text-decoration:none;color:whitesmoke'><div style=style='margin-top:12%'>Reject<br></div></a></td>"
				+"<td style='width:71%'></td>"
				+"</tr>"
				+"</table>"
		
			     +"<font style='font-family: sans-serif;color: #2f00b2;'><br><br><br><br>"+"Thank You"
		
			     +"<br>"+"Training Reports Team.</font>";

	  
	     
	     MailUtil.sendMessage("traininglogistics@pfizer.com", emailTo, new String[0],new String[0], sSubject, emailBody, "text/html", "java:jboss/TRTMailSession");
	   
	}

	public String rejectRequest()
	{
		String toEmail = request.getParameter("email");
		
		AccessApproverHandler handler = new AccessApproverHandler();
		
		String ccEmial[] = handler.getAccessApproversEmails();
		
		AccessRequestHandler accHandle = new AccessRequestHandler();
		
		AccessRequest rejectedRequest = new AccessRequest();
		
		rejectedRequest.setId(Integer.parseInt(request.getParameter("userid").toString()));
		
		rejectedRequest.setRequestStatus( AccessRequest.REJECTED);
		
		rejectedRequest.setApprovers_comments(request.getParameter("rejectionComments").toString());
		
		
		
		accHandle.updateAccessRequests(rejectedRequest);
		
		sendRejectedAccessMail(toEmail, ccEmial);
		
		request.setAttribute("Result","accessRequestCompleted");    
	     
		return new String("success");
	}
	
	public String accessRequest()
	{
		AccessRequest newRequest = new  AccessRequest();
		newRequest.setEamilID(getRequest().getParameter("email").toString().toLowerCase());
		newRequest.setEmployeeId(getRequest().getParameter("emplId"));
		newRequest.setFirstName(getRequest().getParameter("fristName"));
		newRequest.setLastName(getRequest().getParameter("lastName"));
		newRequest.setNtid(getRequest().getParameter("ntid").toString().toLowerCase());
		newRequest.setNtidDomain(getRequest().getParameter("ntidDomain"));
		newRequest.setPfizerEmployee(getRequest().getParameter("pfizerEmployee"));
		newRequest.setComments(getRequest().getParameter("comments"));
		newRequest.setDate_submitted(new Date(Calendar.getInstance().getTimeInMillis()));
		newRequest.setRequestStatus(AccessRequest.SUBMITTED);
		
		AccessRequestHandler dbHandler = new AccessRequestHandler();
		
		newRequest.setId(dbHandler.isAccessRequestPending(newRequest));
		
		if(newRequest.getId() != null)
		{
			request.setAttribute("Result","accessRequestExists");
			request.setAttribute("reminderMailId",newRequest.getId().toString()); 
			return new String("success");
		}
		
		try 
		{
			String userid=dbHandler.saveAccessRequests(newRequest);
			newRequest.setId(Integer.parseInt(userid));
			sendRequestEmail(newRequest);
		} 
		catch (AddressException e) 
		{
			System.out.println("Email sending error >>>>>>>>>>>>"+ e.getMessage());
			return new String("error");
		} 
		catch (MessagingException e) 
		{
			System.out.println("Email sending error >>>>>>>>>>>>"+ e.getMessage());
			return new String("error");
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Some Error Occured"+ e.getMessage());
			return new String("error");
		}
		
		  request.setAttribute("Result","accessRequestSubmitted");    
		     
		  return new String("success");
	}
	
	public void sendRejectedAccessMail(String to, String[] cc)
	{
		String sSubject = "Access request in TRT system.";
		 String emailBody =
	    		 "Dear User,"
	     		 +"<br>"+"Your access request to TRT system has been rejected by approvers."
	    		
	     		 +"<br><br>Kindly take the required action."
		
			     +"<br><br><br><br>"+"Thanks and Regards,"
		
			     +"<br>"+"Training Reports Team.";

	  
	     try{
	     MailUtil.sendMessage("traininglogistics@pfizer.com",new String[]{to},cc,new String[0], sSubject, emailBody, "text/html", "java:jboss/TRTMailSession");
	     }
	     catch(Exception e)
	     {
	         System.out.println("Email sending error >>>>>>>>>>>>"+ e.getMessage());
	     }
	}
	
	public String reminderAccessMail()
	{
		AccessRequest newRequest = new  AccessRequest();
	
		AccessRequestHandler dbHandler = new AccessRequestHandler();
		
		newRequest=dbHandler.getRequest(Integer.parseInt(getRequest().getParameter("reminderMailId").toString()));
		
		try 
		{
			sendRequestReminderEmail(newRequest);
		} 
		catch (AddressException e) 
		{
			System.out.println("Email sending error >>>>>>>>>>>>"+ e.getMessage());
			return new String("error");
		} 
		catch (MessagingException e) 
		{
			System.out.println("Email sending error >>>>>>>>>>>>"+ e.getMessage());
			return new String("error");
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Some Error Occured"+ e.getMessage());
			return new String("error");
		}
		
		  request.setAttribute("Result","accessRequestSubmitted");    
		     
		  return new String("success");
	}
	
	
	public void sendRequestReminderEmail(AccessRequest request) throws AddressException, MessagingException
	{
		 String requestURL= getRequest().getRequestURL().toString();
		
		 String mailURL= "";
		 
		 if(requestURL != null && requestURL.toString().contains(TRTPROD))
			 mailURL=TRTPROD;
		 else if(requestURL != null && requestURL.toString().contains(TRTSTG))
			 mailURL=TRTSTG;
		 else if(requestURL != null && requestURL.toString().contains(TRTTST))
			 mailURL=TRTTST;
		 else if(requestURL != null && requestURL.toString().contains(TRTLOCL))
			 mailURL=TRTLOCL;
		
		 AccessApproverHandler handler = new AccessApproverHandler();
		 
		 String emailTo[] = handler.getAccessApproversEmails();
	   
	     String sSubject = "Access request in TRT system.";
	     
	     String emailBody =
	    		 "<font style='font-family: sans-serif;color: #2f00b2;'>Dear Approver,"
	     		 +"<br>"+"Access request with below details is still pending from your end, <br>Kindly take the required action,:"
	    		
	     		 +"<br><br>"+"Name:"+request.getLastName()+" "+request.getFirstName()+""

			     +"<br>Pfizer Employee:"+" "+request.getPfizerEmployee()
			     
			     +"<br>Email:"+" "+request.getEamilID();
		
			     if(request.getPfizerEmployee().equalsIgnoreCase("Yes"))
			     {
			    	 emailBody += "<br>"+"Emplid:"+" "+request.getEmployeeId()
			    				
						     +"<br>"+"NTID:"+" "+request.getNtid()
					
						     +"<br>"+"NT Domain:"+" "+request.getNtidDomain();			 
			     }
			     
			     
			     emailBody += "<br>Comments:"+" "+request.getComments() 
			    		 +"</font><br><br>";
			     
			     
		
	     		emailBody +="<table style='width:1000%;text-align: center;font-size: 25;'>"
				+"<tr>"
				+"<td style='width:12%;background-color: #90c140;border: #3D9E69 3px solid;'>"
				+ "<a href='http://"+mailURL+"TrainingReports/admin/edituser?isRequestAccess=true&lastName="+ request.getLastName() +"&firstName="+request.getFirstName()+"&email="+request.getEamilID()+"&emplId="+request.getEmployeeId()+"&ntid="+request.getNtid()+"&ntdomain="+request.getNtidDomain()+"&userid="+request.getId()+"' "
				+ "style='text-decoration:none;color:whitesmoke'><div style='margin-top:12%'>Approve<br></div></a></td>"
				+"<td style='width:5%'></td>"
				+"<td style='width:12%;background-color: #fc5720;border: #fc1616 3px solid;'>"
				+ "<a href='http://"+mailURL+"TrainingReports/requestAccess.jsp?&email="+request.getEamilID()+"&userid="+request.getId()+"&Result=accessRequestRejected' "
				+ "style='text-decoration:none;color:whitesmoke'><div style=style='margin-top:12%'>Reject<br></div></a></td>"
				+"<td style='width:71%'></td>"
				+"</tr>"
				+"</table>"
		
			     +"<font style='font-family: sans-serif;color: #2f00b2;'><br><br><br><br>"+"Thank You"
		
			     +"<br>"+"Training Reports Team.</font>";

	  
	     
	     MailUtil.sendMessage("traininglogistics@pfizer.com", emailTo, new String[0],new String[0], sSubject, emailBody, "text/html", "java:jboss/TRTMailSession");
	   
	}
	
	
	
	
	@Override
	public void setServletResponse(HttpServletResponse response) 
	{
		this.response=response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) 
	{ 
		
		this.request = request;
	}
}
