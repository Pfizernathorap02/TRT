package com.pfizer.webapp.wc.components.report; 

//import com.pfizer.db.EmpReport;
//import RBU.RBUController.RBUClassRoomReportForm;
import com.pfizer.action.RBUControllerAction.RBUClassRoomReportForm;
import com.pfizer.db.EnrollChangeReport;
import com.pfizer.db.RBUEnrollChangeReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;

import java.util.List;
 
public class RBUClassRoomReportWc extends WebComponent
{ 
    //private EmpReport[] empReport;    
    //private EmpReport empReportTotal; 
    private RBUClassRoomReportForm form;
    private List empReport;       
    
    private String event;
    public RBUClassRoomReportWc(List empReport, String event) {
        this.setEmpReport(empReport);
        this.event = event;
        //this.setEmpReportTotal(empReportTotal);
	}   
    
    public void setEmpReport(List empReport){
        this.empReport = empReport;
    }    
    
    public List getEmpReport(){
        return this.empReport;
    }

   /* public void setEmpReportTotal(EmpReport empReportTotal){
        this.empReportTotal = empReportTotal;
    }    
    
    public EmpReport getEmpReportTotal(){
        return this.empReportTotal;
    }*/

    
    public String getJsp() {         
        return AppConst.JSP_LOC + "/components/report/RBUClassRoomReport.jsp";	
	}
    
    public void setupChildren() {                
    }
    
    public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
    
    public RBUClassRoomReportForm getRBUClassRoomReportForm(){
        return form;
    }
    
    public void setRBUClassRoomReportForm(RBUClassRoomReportForm form){
        this.form = form;
    }
} 
