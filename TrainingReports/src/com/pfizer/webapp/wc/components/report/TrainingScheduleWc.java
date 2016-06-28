package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.EmpReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;

public class TrainingScheduleWc extends WebComponent
{ 
    private EmpReport[] empReport;    
    private String event;
    public TrainingScheduleWc(EmpReport[] empReport, String event) {
        this.setEmpReport(empReport);
        this.setEvent(event);
	}   
    
    public void setEmpReport(EmpReport[] empReport){
        this.empReport = empReport;
    }    
    
    public EmpReport[] getEmpReport(){
        return this.empReport;
    }
    
    public String getJsp() {         
        if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            return AppConst.JSP_LOC + "/components/report/PDFHSTrainingSchedule.jsp";		
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {            
            return AppConst.JSP_LOC + "/components/report/SPFHSTrainingSchedule.jsp";		
        }else if (AppConst.EVENT_RBU.equalsIgnoreCase(event)) {            
            return AppConst.JSP_LOC + "/components/report/RBUTrainingSchedule.jsp";		
        }
        return AppConst.JSP_LOC + "/components/report/PDFHSTrainingSchedule.jsp";	
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
