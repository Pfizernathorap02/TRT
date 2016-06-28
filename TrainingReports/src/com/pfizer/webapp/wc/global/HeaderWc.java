package com.pfizer.webapp.wc.global; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class HeaderWc extends WebComponent {
	private HeaderBottomLinks bottomLinks;
	private String pageId = new String();
	private boolean showNav = true;
    private boolean reportSelect = false;
	private WebComponent customHtml = new EmptyPageWc();
    
	public HeaderWc( String pageId) {
		this.pageId = pageId;
        if ( "reportselect".equals(pageId) ) {
            reportSelect = true;
            showNav = false;
        }
		cssFiles.add(AppConst.CSS_LOC + "header.css");
	}
    
    public void setCustomHtml( WebComponent html ) {
        this.customHtml = html;
    }
    public WebComponent getCustomHtml() {
        return this.customHtml;
    }
	public void setPageId( String id ) {
		this.pageId = id;
	}
	
    public boolean isReportSelect() {
        return reportSelect;
    }
    public void setReportSelect( boolean flag) {
        this.reportSelect = flag;
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
		return AppConst.JSP_LOC + "/global/header.jsp";
	}
	public void setupChildren() {} 
} 
