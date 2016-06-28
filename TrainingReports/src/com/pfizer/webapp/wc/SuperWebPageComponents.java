package com.pfizer.webapp.wc; 

import com.tgix.wc.WebPageComponent;

public abstract class SuperWebPageComponents extends WebPageComponent {
	private boolean loginRequired = false;
	
	
	public void setLoginRequired(boolean flag) {
		loginRequired = flag;
	}
	public boolean isLoginRequired() {
		return loginRequired;
	}
} 
