package com.pfizer.webapp.wc.global;

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class HeaderToviazLaunchEDPWc extends WebComponent
{

    private HeaderBottomLinks bottomLinks;
	private String pageId = new String();
	private boolean showNav = true;

	public HeaderToviazLaunchEDPWc(String pageId) {
		this.pageId = pageId;
		cssFiles.add(AppConst.CSS_LOC + "header.css");
	}
	public void setPageId( String id ) {
		this.pageId = id;
	}

	public boolean showNav() {
		return showNav;
	}
	public void setShowNav( boolean flag ) {
		this.showNav= flag;
	}
	public String getPageId() {
		return pageId;
	}
    public String getJsp() {
		return AppConst.JSP_LOC + "/global/headerToviazLaunchEDP.jsp";
	}
	public void setupChildren() {}

}
