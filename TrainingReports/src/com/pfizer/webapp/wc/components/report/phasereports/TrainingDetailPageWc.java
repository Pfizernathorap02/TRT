package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.db.Employee;
import com.pfizer.db.P2lTrack;
import com.pfizer.db.PassFail;
import com.pfizer.db.Product;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.ErrorWc;
import com.pfizer.webapp.wc.components.UnauthorizedWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.Utils.Util;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.List;

public class TrainingDetailPageWc extends WebComponent { 
    private WebComponent employeeInfo;
    private WebComponent overallWc = null;
    private TrainingDetailWc trainingSummary;
   private WebComponent phaseDetail;
   private WebComponent trainingPath;
   private WebComponent gapReport;
    private String pagename;
    private String activityid;
    private String slice;
    private UserSession uSession;
    private P2lTrack track;
     private List statusForGapList;
    private List statusForNotGapList;
    private String empID = null;
    private String actName=null;
    private String empName=null;
        	
	public TrainingDetailPageWc(UserSession uSession,WebComponent employeeInfo,WebComponent trainingPath, WebComponent gapReport) {
        super();
        
        this.employeeInfo = employeeInfo;
        this.gapReport = gapReport;
       this.trainingPath = trainingPath;
        this.uSession = uSession;
        cssFiles.add(AppConst.CSS_LOC + "p2lreporting.css");
    }
    public TrainingDetailPageWc(UserSession uSession,WebComponent employeeInfo,WebComponent trainingPath, WebComponent gapReport, TrainingDetailWc trainingSummary) {
        super();
        
        this.employeeInfo = employeeInfo;
        this.gapReport = gapReport;
       this.trainingPath = trainingPath;
        this.uSession = uSession;
       // this.trainingSummary = trainingSummary;
       this.phaseDetail = trainingSummary;
        cssFiles.add(AppConst.CSS_LOC + "p2lreporting.css");
    }
    public UserSession getUserSession() {
        return uSession;
    }
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
    
    public void setTrack( P2lTrack track) {
        this.track = track;
    }
    public P2lTrack getTrack() {
        return this.track;
    }
    public String getSlice() {
        if (!Util.isEmpty(uSession.getCurrentSlice())) {
            return uSession.getCurrentSlice();
        } else {
            return "Assigned";
        }
    }
    public void setActivityId( String id ) {
        this.activityid=id;
    }
    public String getActivityId() {
        return this.activityid;
    }

    public String getPageName() {
        return pagename;
    }
    public void setPageName(String name) {
        this.pagename = name;
    }
    
    public WebComponent getTrainingSummary() {
        return trainingSummary;
    }
    public WebComponent getTrainingPath() {
        return trainingPath;
    }
    public WebComponent getEmployeeInfo() {
        return employeeInfo;
    }
    public WebComponent getGapReport() {
        return gapReport;
    }
   public WebComponent getPhaseDetail() {
        return phaseDetail;
   }
    public WebComponent getOverallStatus() {
        return overallWc;
    }
    public void setOverallStatus( WebComponent wc) {
        this.overallWc = wc;
    }
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/phasereports/TrainingDetailPage.jsp";
    }

    public void setupChildren() {
         children.add(employeeInfo);
    }
    public String getSearchEmpl() {
        return empID;
    }
    public void setSearchEmpl(String empID) {
        this.empID = empID;
    }
    public String getActivityName() {
        return actName;
    }
    public void setActivityName(String actName) {
        this.actName = actName;
    }
    
    public String getSearchedEmplName() {
        return empName;
    }
    public void setSearchedEmplName(String empName) {
        this.empName = empName;
    }
} 

