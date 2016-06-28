package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;

public class UnauthorizedWc extends WebComponent { 
		
	public UnauthorizedWc() {
	}
		
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/Unauthorized.jsp";
	}
	
	public void setupChildren() {}
} 
