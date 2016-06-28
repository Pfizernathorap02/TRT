package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.db.P2lTrack;
import com.pfizer.webapp.AppConst;
import com.tgix.printing.LoggerHelper;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.List;

public class DrillDownAreaWc extends WebComponent { 
    
    private List activities = new ArrayList();
   // boolean isTreeViewVisible = false;
	
    public DrillDownAreaWc(List result){
	
    this.activities = result;
    //this.isTreeViewVisible = isTreeViewVisible;
	 LoggerHelper.logSystemDebug("wc666" );
    }
    public List getActivities() {
        return activities;
    }
    /*public boolean getisTreeViewVisible() {
        return isTreeViewVisible;
    }*/
    
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/phasereports/DrillDownArea.jsp";
    }
    public void setupChildren() {
	//children.add(area1);
    }
   
}
