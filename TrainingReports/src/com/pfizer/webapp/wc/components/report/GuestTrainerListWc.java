package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.EmpReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.report.ClassFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.List;

public class GuestTrainerListWc extends WebComponent
{     
    private List empReport = new ArrayList();
    private String event;

    
    public GuestTrainerListWc( List empReport, String event) {

        this.setEmpReport(empReport);
        this.setEvent(event);
	}   
    
    public void setEmpReport(List empReport){
        this.empReport = empReport;
    }    
    
    public List getEmpReport(){
        return this.empReport;
    }
    
    public String getJsp() { 
        return AppConst.JSP_LOC + "/components/report/RBUGuestTrainerList.jsp";        
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
