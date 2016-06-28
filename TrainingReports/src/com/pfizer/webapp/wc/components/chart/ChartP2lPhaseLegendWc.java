package com.pfizer.webapp.wc.components.chart; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.db.P2lTrackPhase;

public class ChartP2lPhaseLegendWc extends ChartP2lLegendWc
{     
    
    /*Modified for Phase 1 by Meenakshi */
    public ChartP2lPhaseLegendWc(String activityId, P2lTrackPhase phase,PieChart chart)
    { 
        super(activityId,phase,chart);
    }
	
    public String getJsp() {
                
		return AppConst.JSP_LOC + "/components/report/p2l/chartP2lPhaseLegend.jsp";

	}	

} 
