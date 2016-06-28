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
import com.pfizer.webapp.wc.components.EmployeeGapReportWc;
import com.pfizer.webapp.wc.components.ErrorWc;
import com.pfizer.webapp.wc.components.TrainingPathDisplayWc;
import com.pfizer.webapp.wc.components.UnauthorizedWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.Utils.Util;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;

public class DetailPageWc extends WebComponent { 
    private WebComponent employeeInfo;
    private WebComponent overallWc = null;
    private WebComponent trainingSummary;
    private WebComponent phaseDetail;
    private String pagename;
    private String activityid;
    private String slice;
    private UserSession uSession;
    private P2lTrack track;
    //Added for TRT Phase 2
    private TrainingPathDisplayWc trainingPath= null;
    private EmployeeGapReportWc gapReport = null;
        	
	public DetailPageWc(WebComponent employeeInfo,WebComponent trainingSummary, WebComponent phaseDetail, UserSession uSession) {
        super();
        
        this.employeeInfo = employeeInfo;
        this.trainingSummary = trainingSummary;
        this.phaseDetail = phaseDetail;
        this.uSession = uSession;
        cssFiles.add(AppConst.CSS_LOC + "p2lreporting.css");
    }
    //Start: added for TRT Phase 2 Enhancement. 
    public DetailPageWc(WebComponent employeeInfo,WebComponent trainingSummary, WebComponent phaseDetail, UserSession uSession, TrainingPathDisplayWc trainingPath, EmployeeGapReportWc gapReport) {
        super();
        
        this.employeeInfo = employeeInfo;
        this.trainingSummary = trainingSummary;
        this.phaseDetail = phaseDetail;
        this.uSession = uSession;
        cssFiles.add(AppConst.CSS_LOC + "p2lreporting.css");
        this.gapReport = gapReport;
        this.trainingPath = trainingPath;
    }
    
    public TrainingPathDisplayWc getTrainingPath() {
        return trainingPath;
    }
    public EmployeeGapReportWc getGapReport() {
        return gapReport;
    }
    
    //Ends here
    public UserSession getUserSession() {
        return uSession;
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
    public WebComponent getEmployeeInfo() {
        return employeeInfo;
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
        return AppConst.JSP_LOC + "/components/report/phasereports/DetailPage.jsp";
    }

    public void setupChildren() {
         children.add(employeeInfo);
    }
} 
