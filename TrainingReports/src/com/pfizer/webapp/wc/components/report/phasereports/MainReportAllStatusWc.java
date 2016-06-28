package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.db.P2lTrack;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
import com.pfizer.webapp.user.User;

public class MainReportAllStatusWc extends WebComponent { 
    private WebComponent area1;
    private WebComponent area2;
    private WebComponent area3;
    private String pagename;
    private String activityid;
    private String slice;
    private P2lTrack track;
    private String parentActivityPK;
    
    // Added for TRT major enhancement
    private WebComponent esearch;
     private User user;
    // end of addition
    
    // Added for Major Enhancement 3.6 - F1
    private String errorMsg="";
    public MainReportAllStatusWc(){
        
    }
    	
	public MainReportAllStatusWc(WebComponent area1, WebComponent area2, WebComponent area3) {
        this.area1 = area1;
        this.area2 = area2;
        this.area3 = area3;
    }
    // Added for TRT major enhancement
    public MainReportAllStatusWc(WebComponent area1, WebComponent area2, WebComponent area3,WebComponent esearch,User user) {
        this.area1 = area1;
        this.area2 = area2;
        this.area3 = area3;
        this.esearch = esearch;
        this.user=user;
    }
    // end of addition
    
    
    // Added for Major Enhancement 3.6 - F1
    public User getUser() {
        return user;
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
    
    // Added for TRT major enhancement
    public WebComponent getESearch() {
        return this.esearch;
    }
    // end of addition
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/phasereports/MainReportAllStatus.jsp";
    }
    public void setupChildren() {
		children.add(area1);
        children.add(area2);
        if(area3 != null) {
		children.add(area3);
        }
        // Added for TRT major enhancement
        children.add(esearch);
        // end of addition
    }
     
   
}



