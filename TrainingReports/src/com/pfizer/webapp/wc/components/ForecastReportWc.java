package com.pfizer.webapp.wc.components;

import com.pfizer.db.MenuList;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
import java.util.List;

public class ForecastReportWc extends WebComponent {
	private String track;
    private String trackLabel;
	private MenuList menuItem;
	private String errorMsg = "";
	private List reportList;
    public int layout;
    public static final int LAYOUT_XLS=1;

	public ForecastReportWc() {
	}

	public void setErrorMsg(String msg) {
		this.errorMsg = msg;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public void setMenu(MenuList menuItem) {
		this.menuItem = menuItem;
	}

	public MenuList getMenu() {
		return this.menuItem;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getTrack() {
		return this.track;
	}
    
    public void setTrackLabel(String trackLabel) {
		this.trackLabel = trackLabel;
	}

	public String getTrackLabel() {
		return this.trackLabel;
	}

	public String getJsp() {
        if(layout==LAYOUT_XLS)
       {
        return AppConst.JSP_LOC+"/components/admin/forecastReportXls.jsp";
       }
       else{
		return AppConst.JSP_LOC + "/components/admin/forecastReport.jsp";
       }
	}

	public void setupChildren() {
	}

	public void setReportList(List reportList) {
		this.reportList = reportList;
	}

	public List getReportList(){
	return this.reportList;
}
    public void setLayout(int layout)
    {
        this.layout=layout;
    }
    public int getLayout()
    {
        return this.layout;
    }
}
