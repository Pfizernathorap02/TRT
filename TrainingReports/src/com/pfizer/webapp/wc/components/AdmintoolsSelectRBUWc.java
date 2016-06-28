package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;

public class AdmintoolsSelectRBUWc extends WebComponent { 
	private User user;
		
	public AdmintoolsSelectRBUWc(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/admintoolsSelectRBU.jsp";
	}
	public void setupChildren() {}
} 
