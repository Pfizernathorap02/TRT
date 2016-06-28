package com.pfizer.webapp.wc.components.report.phasereports;

import com.pfizer.db.Employee;
import com.pfizer.db.LaunchMeeting;
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

public class MainReportListLaunchMeetingWc extends WebComponent {
    private WebComponent area1;
    private WebComponent area2;
    private WebComponent area3;
    private String pagename;
    private String activityid;
    private String slice;
    private LaunchMeeting track;

	public MainReportListLaunchMeetingWc(WebComponent area1, WebComponent area2, WebComponent area3) {
        this.area1 = area1;
        this.area2 = area2;
        this.area3 = area3;
    }
    public void setTrack( LaunchMeeting track ) {
        this.track = track;
    }
    public LaunchMeeting getTrack() {
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
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/phasereports/MainReportListLaunchMeeting.jsp";
    }
    public void setupChildren() {
		children.add(area1);
        children.add(area2);
		children.add(area3);
    }
}
