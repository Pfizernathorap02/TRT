package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.EmpReport;
import com.pfizer.db.TrainingScheduleByTrack;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;
import java.util.List;

public class RBUEnrollmentExceptionWc extends WebComponent
{ 
    private List report;    
    private String event;
    public RBUEnrollmentExceptionWc(List report, String event) {
        this.setReport(report);
        this.setEvent(event);
	}   
    
    public void setReport(List report){
        this.report = report;
    }    
    
    public List getReport(){
        return this.report;
    }
    
    public String getJsp() {         
           
     return AppConst.JSP_LOC + "/components/report/RBUEnrollmentException.jsp";		
    }

    public void setupChildren() {                
    }
    
	public void setEvent( String event) {
		this.event = event;
	}
    
    public String getEvent() {
		return this.event;
	}
} 
