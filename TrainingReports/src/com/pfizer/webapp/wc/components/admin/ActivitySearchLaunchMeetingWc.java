package com.pfizer.webapp.wc.components.admin;

import com.pfizer.db.Employee;
import com.pfizer.db.LaunchMeetingDetails;
import com.pfizer.db.P2lTrack;
import com.pfizer.db.P2lTrackPhase;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.Utils.Util;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class ActivitySearchLaunchMeetingWc extends WebComponent {
    public static final String FIELD_ACTIVITY_NAME = "activityname";
    public static final String FIELD_CODE = "code";

    private String activityname = "";
    private String code="";
    private String type="";
    private LaunchMeetingDetails phase;
    private List currentNode = new ArrayList();

    private List searchResults = new ArrayList();
    private int trackphaseId;

	public ActivitySearchLaunchMeetingWc() {
	}

    public void setType(String str) {
        this.type=str;
    }
    public String getType() {
        return this.type;
    }
    public void setActivityname(String str) {
        this.activityname = str;
    }
    public String getActivityname() {
        return Util.toEmpty(this.activityname);
    }
    public void setCode(String str) {
        this.code = str;
    }
    public String getCode() {
        return Util.toEmpty(this.code);
    }

    public List getCurrent() {
        return currentNode;
    }
    public void setCurrent( List list ) {
        this.currentNode = list;
    }
    public List getSearchResults() {
        return searchResults;
    }
    public void setSearchResults( List list ) {
        this.searchResults = list;
    }
    public void setPhase( LaunchMeetingDetails phase ) {
        this.phase = phase;
    }
    public LaunchMeetingDetails getPhase() {
        return this.phase;
    }
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/admin/activitySearchLaunchMeeting.jsp";
	}
	public void setupChildren() {}

}
