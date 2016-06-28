package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.EmpReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.report.ClassFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;

public class TrainingScheduleEmplListWc extends WebComponent
{     
    private EmpReport[] empReport;
    private String event;
    private ClassFilterForm classFilterForm;
    
    public TrainingScheduleEmplListWc(ClassFilterForm classFilterForm, EmpReport[] empReport, String event) {
        this.classFilterForm = classFilterForm;
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
            return AppConst.JSP_LOC + "/components/report/PDFHSTrainingSheduleEmplList.jsp";		
        }
        if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
            return AppConst.JSP_LOC + "/components/report/RBUTrainingScheduleEmplList.jsp";		
        }
        if (AppConst.EVENT_RBU.equalsIgnoreCase(event)) {
            return AppConst.JSP_LOC + "/components/report/RBUTrainingScheduleEmplList.jsp";		
        }
        return AppConst.JSP_LOC + "/components/report/PDFHSTrainingSheduleEmplList.jsp";        
	}
    
    public void setupChildren() {                
    }
    
    public void setEvent( String event) {
		this.event = event;
	}
    
    public String getEvent() {
		return this.event;
	}
    
    public ClassFilterForm getClassFilterForm() {
		return classFilterForm;
	}
} 
