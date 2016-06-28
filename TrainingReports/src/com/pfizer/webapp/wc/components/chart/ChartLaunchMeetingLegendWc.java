package com.pfizer.webapp.wc.components.chart;



import com.pfizer.db.LaunchMeetingDetails;
import com.pfizer.db.P2lTrackPhase;
import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;

import com.tgix.wc.WebComponent;



public class ChartLaunchMeetingLegendWc extends WebComponent {


	private int layout = 1;
	private String activityId;
    private boolean pendingFlag = false;
    private LaunchMeetingDetails phase;

	public ChartLaunchMeetingLegendWc(LaunchMeetingDetails phase ) {
        this.phase = phase;
	}

   /* public boolean showPending() {
        return phase.getApprovalStatus();
    }
    public boolean showAssigned() {
        return phase.getAssigned();
    }
    public boolean showExempt() {
        return phase.getExempt();
    }*/
    

    public String getActivityId() {
        return this.activityId;
    }
    
    public LaunchMeetingDetails getPhase() {
        return this.phase;
    }
    
    public ChartLaunchMeetingLegendWc() {

	}

    public String getJsp() {

		return AppConst.JSP_LOC + "/components/report/launchMeeting/chartLaunchMeetingLegend.jsp";

	}


	public void setupChildren() {}

}

