package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;

public class ReportSelectRBUWc extends WebComponent { 
	private User user;
		
	public ReportSelectRBUWc(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/reportSelectRBU.jsp";
	}
	public void setupChildren() {}
} 
