package com.pfizer.webapp.wc.components.report.p2l; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class OverallStatusWc extends WebComponent {
    private String status;
    
	public OverallStatusWc( String status) {
		super();
        this.status = status;
	}
    public String getStatus() {
        return status;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/p2l/overallStatus.jsp";
	}
	public void setupChildren() {} 
} 
