package com.pfizer.webapp.wc.components.chart; 

import com.pfizer.db.P2lTrackPhase;
import com.pfizer.webapp.AppConst;

 public class ChartP2lSubsetLegendWc extends ChartP2lLegendWc {
	
    private String chkStatus = "";
    
public ChartP2lSubsetLegendWc(String activityId, P2lTrackPhase phase ) {
        super(activityId,phase);
    }
    
public void setChkStatus(String chkStatus ) {
        this.chkStatus = chkStatus;
    }
    
public String getChkStatus() {
        return chkStatus;
    }

    public String getJsp() {
                
		return AppConst.JSP_LOC + "/components/report/p2l/chartP2lSubsetLegend.jsp";

	}


} 
