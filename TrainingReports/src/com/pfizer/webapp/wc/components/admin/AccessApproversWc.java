package com.pfizer.webapp.wc.components.admin;

import com.pfizer.db.AccessApproversMembers;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class AccessApproversWc extends WebComponent 
{
	private AccessApproversMembers appOwner;
	private AccessApproversMembers businessOwner1;
	private AccessApproversMembers businessOwner2;
	
	private String message;
	
	public AccessApproversWc(AccessApproversMembers appOwner, AccessApproversMembers businessOwner1,AccessApproversMembers businessOwner2) 
	{
		this.appOwner = appOwner;
		this.businessOwner1 = businessOwner1;
		this.businessOwner2 = businessOwner2;
	}

	public String getJsp() 
	{
		return AppConst.JSP_LOC + "/components/admin/accessApprovers.jsp";
	}

	public void setupChildren() {
		
	}

	public AccessApproversMembers getAppOwner() {
		return appOwner;
	}

	public void setAppOwner(AccessApproversMembers appOwner) {
		this.appOwner = appOwner;
	}

	public AccessApproversMembers getBusinessOwner1() {
		return businessOwner1;
	}

	public void setBusinessOwner1(AccessApproversMembers businessOwner1) {
		this.businessOwner1 = businessOwner1;
	}

	public AccessApproversMembers getBusinessOwner2() {
		return businessOwner2;
	}

	public void setBusinessOwner2(AccessApproversMembers businessOwner2) {
		this.businessOwner2 = businessOwner2;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
