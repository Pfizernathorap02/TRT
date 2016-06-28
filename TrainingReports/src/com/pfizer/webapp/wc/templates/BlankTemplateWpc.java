package com.pfizer.webapp.wc.templates; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.global.FooterWc;
import com.pfizer.webapp.wc.global.HeaderWc;
import com.pfizer.webapp.wc.global.UserBarWc;
import com.tgix.wc.WebComponent;
import com.tgix.wc.WebPageComponent;

public class BlankTemplateWpc extends SuperWebPageComponents { 
	
	protected WebComponent main;
	public BlankTemplateWpc() {
		super();
	}
	public BlankTemplateWpc( WebComponent main ) {		
		super();
		this.main = main;
		
		cssFiles.add(AppConst.CSS_LOC + "trainning.css");
	}
	public void setupChildren() {
		children.add( main );
	}
	public WebComponent getMain() {
		return main;
	}
	public void setMain( WebComponent main ) {
		this.main = main;
	}
	
    public String getJsp()  {
		return AppConst.JSP_LOC + "/templates/blankTemplate.jsp";
	}
	
	
} 
