package com.pfizer.webapp.wc.components.report; 

//import com.pfizer.db.EmpReport;
//import P4.P4Controller.P4ClassRoomReportForm;
//import RBU.RBUController.RBUClassRoomReportForm;
//import com.pfizer.trt.ActionForm.P4ClassRoomReportForm;
import com.pfizer.actionForm.P4ClassRoomReportForm;
import com.pfizer.db.EnrollChangeReport;
import com.pfizer.db.RBUEnrollChangeReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;

import java.util.List;
 
public class P4ClassRoomReportWc extends WebComponent
{ 
    //private EmpReport[] empReport;    
    //private EmpReport empReportTotal; 
    private  P4ClassRoomReportForm form;
    private List empReport;       
    
    private String event;
    public P4ClassRoomReportWc(List empReport, String event) {
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
        return AppConst.JSP_LOC + "/components/report/P4ClassRoomReport.jsp";	
	}
    
    public void setupChildren() {                
    }
    
    public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
    
    public P4ClassRoomReportForm getP4ClassRoomReportForm(){
        return form;
    }
    
    public void setP4ClassRoomReportForm(P4ClassRoomReportForm form){
        this.form = form;
    }
} 
