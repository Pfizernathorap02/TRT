package com.pfizer.webapp.wc.components.chart; 



import com.pfizer.db.P2lTrackPhase;
import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;

import com.tgix.wc.WebComponent;



public class ChartP2lLegendWc extends WebComponent { 


	private int layout = 1;
	private String activityId;
    private boolean pendingFlag = false;
    private P2lTrackPhase phase;
    /* Added for Phase 1 by Meenakshi */
    private PieChart chart;
    
	public ChartP2lLegendWc(String activityId, P2lTrackPhase phase ) {
        this.activityId = activityId;
        this.phase = phase;
	}
    
    public ChartP2lLegendWc(String activityId, P2lTrackPhase phase, PieChart chart ) {
        this.activityId = activityId;
        this.phase = phase;
        this.chart = chart;
	}
    
    public boolean showPending() {
        return phase.getApprovalStatus();
    }
    public boolean showAssigned() {
        return phase.getAssigned();
    }
    public boolean showExempt() {
        return phase.getExempt();
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    public ChartP2lLegendWc() {

	}
    /* Added for Phase 1 by Meenakshi */
     
    public PieChart getChart()
    {
        return this.chart;
    }

    public String getJsp() {
                
		return AppConst.JSP_LOC + "/components/report/p2l/chartP2lLegend.jsp";

	}


	public void setupChildren() {}	

} 

