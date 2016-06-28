package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class ErrorWc extends WebComponent{ 
	private String sessionKey = new String();
	
	public ErrorWc( String sessionKey ) {
		this.sessionKey = sessionKey;
	}
	
	public String getSessionKey() {
		return sessionKey;
	}
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/error.jsp";
	}
	public void setupChildren() {}	

} 
