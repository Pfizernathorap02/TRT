package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.Attendance;
import com.pfizer.db.Employee;
import com.pfizer.db.PassFail;
import com.pfizer.db.PedagogueExam;
import com.pfizer.db.Product;
import com.pfizer.db.SceFull;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.processor.OverallResult;
import com.pfizer.processor.PassFailProcessor;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.Utils.Util;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginReportWc extends WebComponent { 
	private List results;
	private String header;
	private boolean showTitle = true;
	private String excelUrl = "";
	
	public LoginReportWc(List result, String header) {
		this.results = result;
		this.header = header;
	}
	
	public void setShowTitle(boolean flag) {
		this.showTitle = flag;
	}
	public boolean isShowTitle() {
		return showTitle;
	}
	
	public void setExcelLink( String url ) {
		excelUrl = url;
	}
	public String getExcelLink() {
		return excelUrl;
	}
	
	public String getHeader() {
		return header;
	}
	public List getResults() {
		return results;
	}	
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/LoginReport.jsp";
	}

    public void setupChildren() {
    }
} 
