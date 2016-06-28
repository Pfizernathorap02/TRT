package com.pfizer.webapp.wc.components.report;

//import com.pfizer.db.EmpReport;

import com.pfizer.action.RBUControllerAction.RbuUnAssignedEmployeeForm;
import com.pfizer.db.EnrollChangeReport;
import com.pfizer.db.RBUEnrollChangeReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.printing.TrainingWeeks;
import com.tgix.wc.WebComponent;

import java.util.List;

public class RBUUnassignedEmployeesWc extends WebComponent
{
    //private EmpReport[] empReport;
    //private EmpReport empReportTotal;
  //  private RBUClassRoomReportForm form;
    private RbuUnAssignedEmployeeForm form;
    private List empReport;
     private TrainingWeeks[] week;
   // private List availableTables;
  //  private List guestReport;
   // private List availableRooms;


    private String event;
    public RBUUnassignedEmployeesWc(List empReport, String event, TrainingWeeks[] week) {
        this.setEmpReport(empReport);
//        this.setGuestReport(guests);
        this.event = event;
        this.week = week;
	}

    public void setEmpReport(List empReport){
        this.empReport = empReport;
    }

    public List getEmpReport(){
        return this.empReport;
    }
    
     public TrainingWeeks[] getTrainingWeek(){
        return this.week;
    }


 /*  public void setGuestReport(List guestReport){
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
    }*/
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/RBUUnassignedEmployees.jsp";
	}

    public void setupChildren() {
    }

    public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

    public RbuUnAssignedEmployeeForm getRbuUnAssignedEmployeeForm(){
        return form;
    }

    public void setRbuUnAssignedEmployeeForm(RbuUnAssignedEmployeeForm form){
        this.form = form;
    }

}
