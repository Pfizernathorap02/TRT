package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.db.Employee;
import com.pfizer.db.P2lActivityStatus;
import com.pfizer.db.P2lTrack;
import com.pfizer.db.P2lTrackPhase;
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
import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.List;

public class TrainingDetailWc extends WebComponent { 
    private List result;
    private String str= null;
    private Employee employee;
    private P2lTrack track;
    private String activityname;
   // private P2lTrackPhase trackPhase;
    private P2lActivityStatus status;
    private User user;
    private UserSession uSession;
    private String accessFlag;
    private boolean isDebug = false;
    public static final int LAYOUT_XLS=1;
    public int layout;	
    
	public TrainingDetailWc(List result, Employee emp,UserSession uSession,String activityname, String Flag) {
        super();
        this.result = result;
        this.employee = emp;
        this.track = track;
        this.uSession = uSession;
        this.user = uSession.getUser();
       // this.trackPhase = phase;
        this.activityname = activityname;
        this.accessFlag = Flag;
    }
   public TrainingDetailWc(List result, Employee emp,UserSession uSession,String activityname) {
        super();
        this.result = result;
        this.employee = emp;
        this.track = track;
        this.uSession = uSession;
        this.user = uSession.getUser();
       // this.trackPhase = phase;
        this.activityname = activityname;
    }
   
    
   /* public P2lTrackPhase getPhase() {
        return trackPhase;
    }*/
     public void setPhaseTabs( String str ) {
        this.str = str;
    }
    public String getPhaseTabs(){
        return str;
    }
    public String getActivityName() {
        return this.activityname;
    }
    public UserSession getUserSesion() {
        return uSession;
    }
    public User getUser() {
        return this.user;
    }
    public void setDebug( boolean flag ) {
        this.isDebug = flag;
    }
    public boolean isDebug() {
        return isDebug;
    }
    public P2lTrack getTrack() {
        return track;
    }
    public void setStatus(P2lActivityStatus status) {
        this.status = status;
    }
    public P2lActivityStatus getStatus() {
        return status;
    }
    public String getActivityPk() {
        return status.getActivityId()+"";
    }
    public List getResult() {
        return result;
    }
     public void setLayout(int layout)
    {
        this.layout=layout;
    }
    public int getLayout()
    {
        return this.layout;
    }
    
    public String getAccessFlag() {
        return accessFlag;
    }
    public Employee getEmployee() {
        return employee;
    }
    public String getJsp() {
         if(layout==LAYOUT_XLS)
       {
        return AppConst.JSP_LOC + "/components/report/phasereports/TrainingDetailXls.jsp";
       }
       else {
        return AppConst.JSP_LOC + "/components/report/phasereports/TrainingDetail.jsp";
       }
    }

    public void setupChildren() {
    }
} 
