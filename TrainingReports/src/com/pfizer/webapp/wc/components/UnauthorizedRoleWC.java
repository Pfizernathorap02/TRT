package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;

public class UnauthorizedRoleWC extends WebComponent { 
		
	public UnauthorizedRoleWC() {
	}
		
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/UnauthorizedRole.jsp";
	}
	
	public void setupChildren() {}
} 
