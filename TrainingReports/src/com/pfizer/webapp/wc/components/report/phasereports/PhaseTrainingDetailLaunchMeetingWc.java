package com.pfizer.webapp.wc.components.report.phasereports;

import com.pfizer.db.Employee;
import com.pfizer.db.LaunchMeeting;
import com.pfizer.db.LaunchMeetingDetails;
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

public class PhaseTrainingDetailLaunchMeetingWc extends WebComponent {
    private Employee employee;
    private LaunchMeeting track;
    private LaunchMeetingDetails trackPhase;
    private P2lActivityStatus status;
    private User user;
    private UserSession uSession;
    private boolean isDebug = false;
    private List pedExams;
    private String attendanceCodeStatus;
    private String managerTrainingCodeStatus;
    private String complinacePresentationCodeStatus;
    private boolean showAttendance = false;
    private String overallStatus;

	public PhaseTrainingDetailLaunchMeetingWc(Employee emp, LaunchMeeting track, LaunchMeetingDetails phase,UserSession uSession, List pedExams, String attendance, String managerTraining,String compliancePresentation, boolean showAttendance, String overallStatus) {
        super();
        this.employee = emp;
        this.track = track;
        this.uSession = uSession;
        this.user = uSession.getUser();
        this.trackPhase = phase;
        this.pedExams = pedExams;
        this.attendanceCodeStatus = attendance;
        this.managerTrainingCodeStatus = managerTraining;
        this.complinacePresentationCodeStatus = compliancePresentation;
        this.showAttendance = showAttendance;
        this.overallStatus = overallStatus;
    }
    public LaunchMeetingDetails getPhase() {
        return trackPhase;
    }
    
   public String getAttendanceCodeStatus(){
        return this.attendanceCodeStatus;
   }
   
   public String getManagerTrainingCodeStatus(){
        return this.managerTrainingCodeStatus;
   }
   
   public String getOverallStatus(){
        return this.overallStatus;
    }
   
   public String getComplinacePresentationCodeStatus(){
        return this.complinacePresentationCodeStatus;
   }
   
    public List getPedExams(){
        return this.pedExams;
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
    public boolean showAttendance(){
        return this.showAttendance;
    }
    public LaunchMeeting getTrack() {
        return track;
    }
    public void setStatus( P2lActivityStatus status ) {
        this.status = status;
    }
    public P2lActivityStatus getStatus() {
        return status;
    }
    public Employee getEmployee() {
        return employee;
    }
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/phasereports/LaunchMeetingDetail.jsp";
    }

    public void setupChildren() {
    }
}
