package com.pfizer.webapp.wc.components.report.launchMeeting;

import com.pfizer.db.LaunchMeeting;
import com.pfizer.db.P2lTrack;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class LaunchMeetingBreadCrumbWc extends WebComponent {
    private LaunchMeeting track;
	public LaunchMeetingBreadCrumbWc(LaunchMeeting track) {
		super();
        this.track = track;
	}
    public LaunchMeeting getTrack() {
        return track;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/launchMeeting/launchMeetingBreadCrumb.jsp";
	}
	public void setupChildren() {}
}
