package com.pfizer.webapp.wc.components.report; 

//import com.pfizer.db.EmpReport;
import com.pfizer.db.EnrollChangeReport;
import com.pfizer.db.RBUEnrollChangeReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;
 
public class RBUEnrollmentChangeReportWc extends WebComponent
{ 
    //private EmpReport[] empReport;    
    //private EmpReport empReportTotal; 
    private RBUEnrollChangeReport[] empReport;       
    
    private String event;
    public RBUEnrollmentChangeReportWc(RBUEnrollChangeReport[] empReport, String event) {
        this.setEmpReport(empReport);
        this.event = event;
        //this.setEmpReportTotal(empReportTotal);
	}   
    
    public void setEmpReport(RBUEnrollChangeReport[] empReport){
        this.empReport = empReport;
    }    
    
    public RBUEnrollChangeReport[] getEmpReport(){
        return this.empReport;
    }

   /* public void setEmpReportTotal(EmpReport empReportTotal){
        this.empReportTotal = empReportTotal;
    }    
    
    public EmpReport getEmpReportTotal(){
        return this.empReportTotal;
    }*/

    
    public String getJsp() {         
        return AppConst.JSP_LOC + "/components/report/RBUEnrollmentChangeReport.jsp";	
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
