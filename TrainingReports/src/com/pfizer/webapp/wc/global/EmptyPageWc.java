package com.pfizer.webapp.wc.global; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class EmptyPageWc extends WebComponent {
    private String randomString = "";
    
	public EmptyPageWc() {
		super();
	}
	public EmptyPageWc(String random) {
		super();
        this.randomString = random;
	}
    public void setRandomString( String str ) {
        randomString = str;
    }
    public String getRandomString() {
        return this.randomString;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/global/emptypage.jsp";
	}
	public void setupChildren() {} 
} 
