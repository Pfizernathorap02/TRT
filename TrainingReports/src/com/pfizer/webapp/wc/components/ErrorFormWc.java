package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class ErrorFormWc extends WebComponent{ 
	private String sessionKey;
	
	public ErrorFormWc( String key) {
		this.sessionKey = key;
	}
	public String getSessionKey() {
		return sessionKey;
	}
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/errorForm.jsp";
	}
	public void setupChildren() {}	

} 
