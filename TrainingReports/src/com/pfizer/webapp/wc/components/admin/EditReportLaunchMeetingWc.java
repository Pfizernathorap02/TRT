package com.pfizer.webapp.wc.components.admin;

import com.pfizer.db.Employee;
import com.pfizer.db.MenuList;
import com.pfizer.db.LaunchMeeting;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

public class EditReportLaunchMeetingWc extends WebComponent {
    private LaunchMeeting track;
    private MenuList menuItem;
    private String errorMsg="";

	public EditReportLaunchMeetingWc() {
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


    public void setTrack( LaunchMeeting track ) {
        this.track = track;
    }
    public LaunchMeeting getTrack() {
        return this.track;
    }
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/admin/editReportLaunchMeeting.jsp";
	}
	public void setupChildren() {}

}
