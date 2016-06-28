package com.pfizer.webapp.wc.util; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.wc.global.HeaderWc;
import com.pfizer.webapp.wc.global.StaticJspWc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.wc.WebComponent;
import com.tgix.wc.WebPageComponent;
import javax.servlet.http.HttpServletRequest;

public class PageBuilder {
	public PageBuilder() {}
	
	/**
	 * This method will build a page based on the MainTemplateWpc
	 */
	public MainTemplateWpc buildPage( WebComponent main, String pageTitle, User user, String pageId ) {
		MainTemplateWpc page = new MainTemplateWpc(user, pageId);
		
		page.setPageTitle(pageTitle);	
		
		page.setMain(main);
        
		return page;
	} 
	public MainTemplateWpc buildPageP2l( WebComponent main, String pageTitle, User user, String pageId ) {
		MainTemplateWpc page = new MainTemplateWpc(user, pageId);
    
		page.setPageTitle(pageTitle);			
		page.setMain(main);
        HeaderWc header = (HeaderWc)page.getHeader();
        header.setCustomHtml(new StaticJspWc(AppConst.JSP_LOC + "/components/report/phasereports/staticjsp/headertxt.jsp"));
		return page;
	} 
    
	public MainTemplateWpc buildPagePoa2( WebComponent main, String pageTitle, User user, String pageId ) {
		MainTemplateWpc page = new MainTemplateWpc(user, pageId);
    
		page.setPageTitle(pageTitle);			
		page.setMain(main);
        HeaderWc header = (HeaderWc)page.getHeader();
        header.setCustomHtml(new StaticJspWc(AppConst.JSP_LOC + "/components/report/phasereports/staticjsp/headertxt.jsp"));
		return page;
	} 

	/**
	 * This method will build a page based on the MainTemplateWpc
	 */
	public MainTemplateWpc buildPWRAPage( WebComponent main, String pageTitle, User user, String pageId ) {
		MainTemplateWpc page = new MainTemplateWpc(user, pageId,"PWRA");
		
		page.setPageTitle(pageTitle);	
		
		page.setMain(main);
		return page;
	} 

} 
