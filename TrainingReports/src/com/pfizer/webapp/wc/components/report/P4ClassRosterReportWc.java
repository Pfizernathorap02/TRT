package com.pfizer.webapp.wc.components.report;

//import com.pfizer.db.EmpReport;
import com.pfizer.db.EnrollChangeReport;
import com.pfizer.db.RBUClassRosterBean;
import com.pfizer.db.RBUEnrollChangeReport;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.printing.TrainingWeeks;
import com.tgix.wc.WebComponent;

public class P4ClassRosterReportWc extends WebComponent
{
    //private EmpReport[] empReport;
    //private EmpReport empReportTotal;
    private RBUClassRosterBean[] empReport;
    private TrainingWeeks[] week;
    private String displayResult;

    private String event;
    public P4ClassRosterReportWc(RBUClassRosterBean[] empReport, String event, TrainingWeeks[] week, String displayResult) {
        this.setEmpReport(empReport);
        this.event = event;
        this.week = week;
        this.displayResult = displayResult;
	}

    public void setEmpReport(RBUClassRosterBean[] empReport){
        this.empReport = empReport;
    }

    public RBUClassRosterBean[] getEmpReport(){
        return this.empReport;
    }
    
    public TrainingWeeks[] getTrainingWeek(){
        return this.week;
    }

    public String getDisplayResult(){
        return this.displayResult;
    }
  
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/P4ClassRosterReport.jsp";
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
