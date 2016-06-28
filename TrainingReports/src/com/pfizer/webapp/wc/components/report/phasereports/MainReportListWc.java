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
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.ErrorWc;
import com.pfizer.webapp.wc.components.UnauthorizedWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;

public class MainReportListWc extends WebComponent { 
    private WebComponent area1;
    private WebComponent area2;
    private WebComponent area3;
    ////added for HQ user///
    private WebComponent area4;
    ////end HQ user/////
    // Start: Modified for TRT 3.6 enhancement - F 4.1 ( addition of search criteria)
    private WebComponent esearch;
    private WebComponent rightBar;
    // End: Modified for TRT 3.6 enhancement - F 4.1 ( addition of search criteria)
    private String pagename;
    private String activityid;
    private String slice;
    private P2lTrack track;
    //added for TRT major Enhancement 3.6-F1
    private WebComponent drillDownArea;	
     private User user;
    private String errorMsg="";
	public MainReportListWc(WebComponent area1, WebComponent area2, WebComponent area3) {
        this.area1 = area1;
        this.area2 = area2;
        this.area3 = area3;
    }
// Added for TRT Phase 2 - HQ Users
	public MainReportListWc(WebComponent area1, WebComponent area2, WebComponent area3,WebComponent area4) {
//	public MainReportListWc(WebComponent area1, WebComponent area2, WebComponent area3) {
        this.area1 = area1;
        this.area2 = area2;
        this.area3 = area3;
        ////added for HQ user///
        this.area4 = area4;
        ////end HQ user/////
    }
    // Start: Modified for TRT 3.6 enhancement - F 4.1 ( addition of search criteria)
	public MainReportListWc(WebComponent area1, WebComponent area2, WebComponent area3,User user) {
        this.area1 = area1;
        this.area2 = area2;
        this.area3 = area3;
        //this.esearch = esearch;
        this.user=user;
	javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
	}
    // End: Modified for TRT 3.6 enhancement - F 4.1 ( addition of search criteria)
// Added for TRT Phase 2 - HQ Users
	public MainReportListWc(WebComponent area1, WebComponent area2, WebComponent area3,WebComponent area4,User user, WebComponent esearch) {
        this.area1 = area1;
        this.area2 = area2;
        this.area3 = area3;
        ////end HQ user/////
        this.area4 = area4;
        ////end HQ user/////
        this.esearch = esearch;
        this.user=user;
	javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
	}
     //added for TRT major Enhancement 3.6-F1
    public User getUser() {
        return user;
    }
    public WebComponent getDrillDownArea() {
          return this.drillDownArea;
    }
       
    public void setDrillDownArea(WebComponent wc) {
    this.drillDownArea = wc;
    }
     public void setErrorMsg( String msg ) {
        this.errorMsg = msg;
    }
    public String getErrorMsg() {
        return this.errorMsg;
    }
   
    //ends here
    public void setTrack( P2lTrack track ) {
        this.track = track;
    }
    public P2lTrack getTrack() {
        return track;
    }
    public void setActivityId( String id ) {
        this.activityid=id;
    }
    public String getActivityId() {
        return this.activityid;
    }
    
    public String getSlice() {
        return slice;
    }
    public void setSlice(String slice) {
        this.slice=slice;
    }
    public String getPageName() {
        return pagename;
    }
    public void setPageName(String name) {
        this.pagename = name;
    }
	public WebComponent getArea1() {
        return this.area1;
    }
	public WebComponent getArea2() {
        return this.area2;
    }
	public WebComponent getArea3() {
        return this.area3;
    }
// Added for TRT Phase 2 - Requirement no. F3
    public WebComponent getArea4() {
        return this.area4;
    }
// End
   // Start: Modified for TRT 3.6 enhancement - F 4.1 ( addition of search criteria)
   public WebComponent getEsearch(){
        return this.esearch;
    } 
    
   // End: Modified for TRT 3.6 enhancement - F 4.1( addition of search criteria)
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/phasereports/MainReportList.jsp";
    }
    public void setupChildren() {
		children.add(area1);
        children.add(area2);
		children.add(area3);
         //added for TRT major Enhancement 3.6-F1
         if(drillDownArea != null) {
        children.add(drillDownArea);
         }
   // Start: Modified for TRT 3.6 enhancement - F 4.1 ( addition of search criteria)
        children.add(esearch);
   // End: Modified for TRT 3.6 enhancement - F 4.1( addition of search criteria)

    }
} 
