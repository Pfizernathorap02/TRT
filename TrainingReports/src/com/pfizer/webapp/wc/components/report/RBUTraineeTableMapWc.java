package com.pfizer.webapp.wc.components.report; 

//import com.pfizer.db.EmpReport;

import java.util.List;

import com.pfizer.actionForm.RbutraineetablemapForm;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
 
public class RBUTraineeTableMapWc extends WebComponent
{ 
    //private EmpReport[] empReport;    
    //private EmpReport empReportTotal; 
  //  private RBUClassRoomReportForm form;
    private RbutraineetablemapForm form;
    private List empReport;    
    private List availableTables;   
    private List guestReport;
    private List availableRooms;
    
    
    private String event;
    public RBUTraineeTableMapWc(List empReport, List guests, String event) {
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
        return AppConst.JSP_LOC + "/components/report/RBUTraineeTableMap.jsp";	
	}
    
    public void setupChildren() {                
    }
    
    public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
    
    public RbutraineetablemapForm getRbutraineetablemapForm(){
        return form;
    }
    
    public void setRbutraineetablemapForm(RbutraineetablemapForm form){
        this.form = form;
    }
    
} 
