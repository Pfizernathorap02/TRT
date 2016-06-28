package com.pfizer.webapp.wc.components.report; 

//import com.pfizer.db.EmpReport;
import com.pfizer.actionForm.P4traineetablemapForm;
//import RBU.RBUController.RBUClassRoomReportForm;
//import RBU.RBUController.RbutraineetablemapForm;

import com.pfizer.db.EnrollChangeReport;
import com.pfizer.db.P4RoomGridVO;
import com.pfizer.db.RBUEnrollChangeReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;

import java.util.List;
 
public class P4TraineeTableMapWc extends WebComponent
{ 
    //private EmpReport[] empReport;    
    //private EmpReport empReportTotal; 
  //  private RBUClassRoomReportForm form;
    private 
    P4traineetablemapForm form;
    private List empReport;    
    private List availableTables;   
    private List guestReport;
    private List availableRooms;
    
    
    private String event;
    public P4TraineeTableMapWc(List empReport, List guests, String event) {
        this.setEmpReport(empReport);
        this.setGuestReport(guests);
        this.event = event;
	}   
    
    public void setEmpReport(List empReport){
        this.empReport = empReport;
    }    
    
    public List getEmpReport(){
        return this.empReport;
    }
     
    public void setGuestReport(List guestReport){
        this.guestReport = guestReport;
    }    
    
    public List getGuestReport(){
        return this.guestReport;
    }    
    public void setAvailableTables(List availableTables){
        this.availableTables = availableTables;
    }    
    
    public List getAvailableTables(){
        return this.availableTables;
    }

    public void setAvailableRooms(List availableRooms){
        this.availableRooms = availableRooms;
    }    
    
    public List getAvailableRooms(){
        return this.availableRooms;
    }
    public String getJsp() {         
        return AppConst.JSP_LOC + "/components/report/P4TraineeTableMap.jsp";	
	}
    
    public void setupChildren() {                
    }
    
    public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
    
    public P4traineetablemapForm getP4traineetablemapForm(){
        return form;
    }
    
    public void setP4traineetablemapForm(P4traineetablemapForm form){
        this.form = form;
    }
    
} 
