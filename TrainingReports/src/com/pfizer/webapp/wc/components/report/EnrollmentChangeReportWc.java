package com.pfizer.webapp.wc.components.report; 

//import com.pfizer.db.EmpReport;
import com.pfizer.db.EnrollChangeReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;
 
public class EnrollmentChangeReportWc extends WebComponent
{ 
    //private EmpReport[] empReport;    
    //private EmpReport empReportTotal; 
    private EnrollChangeReport[] empReport;       
    
    private String event;
    public EnrollmentChangeReportWc(EnrollChangeReport[] empReport, String event) {
        this.setEmpReport(empReport);
        this.event = event;
        //this.setEmpReportTotal(empReportTotal);
	}   
    
    public void setEmpReport(EnrollChangeReport[] empReport){
        this.empReport = empReport;
    }    
    
    public EnrollChangeReport[] getEmpReport(){
        return this.empReport;
    }

   /* public void setEmpReportTotal(EmpReport empReportTotal){
        this.empReportTotal = empReportTotal;
    }    
    
    public EmpReport getEmpReportTotal(){
        return this.empReportTotal;
    }*/

    
    public String getJsp() {         
        return AppConst.JSP_LOC + "/components/report/EnrollmentChangeReport.jsp";	
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
