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

public class PhaseTrainingDetailWc extends WebComponent { 
    private List result;
    private Employee employee;
    private P2lTrack track;
    private P2lTrackPhase trackPhase;
    private P2lActivityStatus status;
    private User user;
    private UserSession uSession;
    private String accessFlag;
    private boolean isDebug = false;
    	
	public PhaseTrainingDetailWc(List result, Employee emp, P2lTrack track, P2lTrackPhase phase,UserSession uSession,String Flag) {
        super();
        this.result = result;
        this.employee = emp;
        this.track = track;
        this.uSession = uSession;
        this.user = uSession.getUser();
        this.trackPhase = phase;
        this.accessFlag = Flag;
    }
    public P2lTrackPhase getPhase() {
        return trackPhase;
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
    public void setStatus( P2lActivityStatus status ) {
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
    
    public String getAccessFlag() {
        return accessFlag;
    }
    public Employee getEmployee() {
        return employee;
    }
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/phasereports/PhaseTrainingDetail.jsp";
    }

    public void setupChildren() {
    }
} 
