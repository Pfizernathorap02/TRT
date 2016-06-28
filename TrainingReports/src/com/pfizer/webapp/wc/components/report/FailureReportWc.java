package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.SceFull;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class FailureReportWc extends WebComponent
{ 
    private SceFull[] failureList;
    private WebComponent massEmail;
    private String emailSubject = "Phased Training Follow-up";
	
	public FailureReportWc(SceFull[] failureList) {
		this.failureList = failureList;
	}
	
	public SceFull[] getFailureList() {
		return failureList;
	}
    
    public void setMassEmailWc( WebComponent email ) {
        this.massEmail = email;
    }
    public WebComponent getMassEmail() {
        return this.massEmail;
    }
    public String getEmailSubject() {
        return emailSubject;
    }
    public void setEmailSubject( String str ){
        this.emailSubject = str;
    }
    	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/FailureReport.jsp";
	}

    public void setupChildren() {
    }
} 
