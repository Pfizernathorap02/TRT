package com.pfizer.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.pfizer.db.AccessApproversMembers;
import com.pfizer.db.UserAccess;
import com.pfizer.hander.AccessApproverHandler;
import com.pfizer.hander.UserHandler;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.components.admin.AccessApproversWc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;

public class AccessApproversManager extends ActionSupport implements ServletRequestAware, ServletResponseAware 
{
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
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

	public String getAccessApprovers()
	{
		try{
			
			AccessApproversMembers appOwner = new AccessApproversMembers();
			AccessApproversMembers businessOwner1 = new AccessApproversMembers();
			AccessApproversMembers businessOwner2 = new AccessApproversMembers();
			
			UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
			
			AccessApproverHandler handler = new AccessApproverHandler();
			
			List<AccessApproversMembers> approvers=handler.getAccessApprovers();
			
			for (AccessApproversMembers approver : approvers) 
			{
				if(approver.getApproverType().equalsIgnoreCase(AccessApproversMembers.APP_OWNER))
					appOwner=approver;
				else if(approver.getApproverType().equalsIgnoreCase(AccessApproversMembers.BUSINESS_OWNER_1))
					businessOwner1=approver;
				else if(approver.getApproverType().equalsIgnoreCase(AccessApproversMembers.BUSINESS_OWNER_2))
					businessOwner2= approver;
			}
			
			AccessApproversWc main = new AccessApproversWc(appOwner,businessOwner1,businessOwner2);
			
			if(getRequest().getAttribute("message") != null)
			{
				main.setMessage(getRequest().getAttribute("message").toString());
			}
			
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "Access Approver Admin");
			page.setMain( main );
			getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
	        /**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			    	return new Forward("success");
			 */
			return new String("success");
	    	}
	    	catch (Exception e) {
	    		Global.getError(getRequest(),e);
	    		return new String("failure");
	    		}
	}

	public String updateApprovers()
	{
		try {

			String approverToUpdate = getRequest().getParameter("approverToUpdate");

			String valueToUpdate = getRequest().getParameter(approverToUpdate);

			AccessApproversMembers updateMember = new AccessApproversMembers();

			updateMember.setApproverType(approverToUpdate);
			updateMember.setEmailId(valueToUpdate);

			AccessApproverHandler handler = new AccessApproverHandler();

			handler.updateApprovers(updateMember);
			
			getRequest().setAttribute("message", "Approver successfully updated.");

			return getAccessApprovers();
		} 
		catch (Exception e) 
		{
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) 
	{
		this.response=response;
		
	}

	@Override
	public void setServletRequest(HttpServletRequest request) 
	{
		this.request=request;
		
	}

}
