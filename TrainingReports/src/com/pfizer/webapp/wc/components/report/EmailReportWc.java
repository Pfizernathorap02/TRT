package com.pfizer.webapp.wc.components.report; 

import java.util.List;

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class EmailReportWc extends WebComponent { 
	private List dmResults;
	private List rmResults;
	private List armResults;
	
	public EmailReportWc(List dm, List rm, List arm) {
		this.dmResults = dm;
		this.rmResults = rm;
		this.armResults = arm;
	}
	
	public List getDMResults() {
		return dmResults;
	}	
	public List getRMResults() {
		return rmResults;
	}	
	public List getARMResults() {
		return armResults;
	}	
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/EmailReport.jsp";
	}

    public void setupChildren() {
    }
} 
