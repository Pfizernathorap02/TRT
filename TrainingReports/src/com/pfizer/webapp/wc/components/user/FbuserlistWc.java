package com.pfizer.webapp.wc.components.user; 

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

public class FbuserlistWc extends WebComponent { 
	private List results;
	private String currentSelected;
    private String userResult;
		
/*	public GroupListWc(List result, String currentSelected) {
		this.results = result;
		this.currentSelected = currentSelected;
	}
*/	
	public FbuserlistWc(List result) {
		this.results = result;
	}
    	
	public List getResults() {
		return results;
	}	
    
    public FbuserlistWc(String result){
        this.userResult = result;
    }
    public String getResult() {
		return userResult;
	}
	
	public String getCurrentSelected() {
		return this.currentSelected;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/user/editGroup.jsp";
	}

    public void setupChildren() {
    }
} 
