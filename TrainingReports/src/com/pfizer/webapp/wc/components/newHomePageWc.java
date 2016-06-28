package com.pfizer.webapp.wc.components; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.wc.components.report.global.EmployeeInfoWc;
import com.tgix.wc.WebComponent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class newHomePageWc extends WebComponent { 
	private User user;
    private EmployeeInfoWc employeeInfo;
    private TrainingPathDisplayWc trainingPath;
    private EmployeeGapReportWc emplGapReport;
    private List reportingEmployeeDetailsList;
    private List statusForGapList;
    private List statusForNotGapList;
    
    public void setStatusForGapList(List statusForGapList){
        this.statusForGapList=statusForGapList;   
    }
    public List getStatusForGapList(){
        return this.statusForGapList;
    }
    public void setStatusForNotGapList(List statusForNotGapList){
        this.statusForNotGapList=statusForNotGapList;   
    }
    public List getStatusForNotGapList(){
        return this.statusForNotGapList;
    }
    public void setReportingEmployeeDetailsList(List reportingEmployeeDetailsList){
        this.reportingEmployeeDetailsList=reportingEmployeeDetailsList;
    }
    
    public List getReportingEmployeeDetailsList(){
        return this.reportingEmployeeDetailsList;   
    }	
    
	public newHomePageWc(User user,EmployeeInfoWc employeeInfo, TrainingPathDisplayWc trainingPath, EmployeeGapReportWc emplGapReport) {
		this.user = user;
        this.employeeInfo=employeeInfo;
        this.trainingPath = trainingPath;
        this.emplGapReport = emplGapReport;
	}
    
    public EmployeeInfoWc getEmployeeInfo(){
        return this.employeeInfo;   
    }
    
     public TrainingPathDisplayWc getTrainingPath(){
        return this.trainingPath;   
    }
	public EmployeeGapReportWc getEmployeeGapReport(){
        return this.emplGapReport;   
    }
	public User getUser() {
		return user;
	}
	
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/newHomePage.jsp";
	}  
	public void setupChildren() {}
    
 		
    /* End of RBU changes */			
    		
    
} 
