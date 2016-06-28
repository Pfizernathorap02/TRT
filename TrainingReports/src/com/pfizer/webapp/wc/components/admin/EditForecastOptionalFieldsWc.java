package com.pfizer.webapp.wc.components.admin; 

import com.pfizer.db.ForecastReport;
import com.pfizer.db.MenuList;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class EditForecastOptionalFieldsWc extends WebComponent 
{  
    private String errorMsg="";
    private MenuList menuItem;
    private ForecastReport track;
       private boolean firstTime;
    
    public EditForecastOptionalFieldsWc() {
	}
    public void setErrorMsg( String msg ) {
        this.errorMsg = msg;
    }
    public String getErrorMsg() {
        return this.errorMsg;
    }
    public void setMenu( MenuList menuItem ) {
        this.menuItem = menuItem;
    }
    public MenuList getMenu() {
        return this.menuItem;
    }
    public void setTrack( ForecastReport track ) {
        this.track = track;
    }
    public ForecastReport getTrack() {
        return this.track;
    }
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/admin/editForecastReportOptionalFields.jsp";
	}

    public void setupChildren()
    {
        
    }
    public void setFirstTime( boolean firstTime ) {
        this.firstTime = firstTime;
    }
    public boolean getFirstTime() {
        return this.firstTime;
    }
} 
