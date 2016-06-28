package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.db.Employee;
import com.pfizer.db.P2lTrack;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
import java.util.List;

public class EmplSearchResultWc extends WebComponent {
    private Employee[] result;
    private P2lTrack track;
    
	public EmplSearchResultWc(Employee[] result) {
		super();
        this.result = result;
	}
    
    public void setTrack( P2lTrack track ) {
        this.track = track;
    }
    public P2lTrack getTrack() {
        return this.track;
    }
    public Employee[] getResult() {
        return result;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/phasereports/EmplSearchResult.jsp";
	}
	public void setupChildren() {} 
} 
