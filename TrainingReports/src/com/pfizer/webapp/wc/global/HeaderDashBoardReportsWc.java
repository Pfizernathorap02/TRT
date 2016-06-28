package com.pfizer.webapp.wc.global;

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class HeaderDashBoardReportsWc extends WebComponent
{

    private HeaderBottomLinks bottomLinks;
	private String pageId = new String();
	private boolean showNav = true;
    private String headString = "";

	public HeaderDashBoardReportsWc(String pageId) {
		this.pageId = pageId;
		cssFiles.add(AppConst.CSS_LOC + "header.css");
	}
	public void setPageId( String id ) {
		this.pageId = id;
	}

    public String getHeadString() {
        return headString;
    }
    public void setHeadString(String str) {
        this.headString =str;
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
		return AppConst.JSP_LOC + "/global/headerDashBoardReports.jsp";
	}
	public void setupChildren() {}

}
