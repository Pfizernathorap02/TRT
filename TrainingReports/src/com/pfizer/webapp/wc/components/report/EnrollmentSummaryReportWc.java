package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.EmpReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;
 
public class EnrollmentSummaryReportWc extends WebComponent
{ 
    private EmpReport[] empReport;    
    private EmpReport empReportTotal;    
    private String event;
    public EnrollmentSummaryReportWc(EmpReport[] empReport,EmpReport empReportTotal,String event) {
        this.setEmpReport(empReport);
        this.event = event;
        this.setEmpReportTotal(empReportTotal);
	}   
    
    public void setEmpReport(EmpReport[] empReport){
        this.empReport = empReport;
    }    
    
    public EmpReport[] getEmpReport(){
        return this.empReport;
    }

    public void setEmpReportTotal(EmpReport empReportTotal){
        this.empReportTotal = empReportTotal;
    }    
    
    public EmpReport getEmpReportTotal(){
        return this.empReportTotal;
    }

    
    public String getJsp() {         
        return AppConst.JSP_LOC + "/components/report/EnrollmentSummaryReport.jsp";	
	}
    
    public void setupChildren() {                
    }
    
    public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
} 
