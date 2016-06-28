package com.pfizer.webapp.wc.components.report.p2l; 

import com.pfizer.db.P2lTrack;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class P2lBreadCrumbWc extends WebComponent {
    private P2lTrack track;
	public P2lBreadCrumbWc(P2lTrack track) {
		super();
        this.track = track;
	}
    public P2lTrack getTrack() {
        return track;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/p2l/P2lBreadCrumb.jsp";
	}
	public void setupChildren() {} 
} 
