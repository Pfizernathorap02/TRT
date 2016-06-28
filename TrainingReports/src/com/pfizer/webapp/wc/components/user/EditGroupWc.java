package com.pfizer.webapp.wc.components.user; 

import com.pfizer.db.Attendance;
import com.pfizer.db.Employee;
import com.pfizer.db.PassFail;
import com.pfizer.db.PedagogueExam;
import com.pfizer.db.Product;
import com.pfizer.db.SceFull;
import com.pfizer.db.UserAccess;
import com.pfizer.db.UserGroups;
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
//added for RBU
import java.util.HashMap;
import java.util.LinkedHashMap;
//ended for RBU

public class EditGroupWc extends WebComponent { 
	private UserGroups userGroups;
    private HashMap buSalesDataMap = new LinkedHashMap();
			
	public EditGroupWc(UserGroups ug, HashMap buSalesDataMap) {
		this.userGroups = ug;
        this.buSalesDataMap = buSalesDataMap;
	}
	
	public UserGroups getUserAccess() {
		return userGroups;
	}	
	
    public HashMap getBuSalesDataMap() {
        return this.buSalesDataMap;
    }
    
    public void setBuSalesDataMap(HashMap buSalesDataMap) {
        this.buSalesDataMap = buSalesDataMap;
    }
    
    
    //test method 
  /*  public void setBuSalesDataMap() {
        HashMap samHashMap = null;
        samHashMap = new HashMap();
        ArrayList samArrayList1 = null;
        samArrayList1 = new ArrayList();
        ArrayList samArrayList2 = null;
        samArrayList2 = new ArrayList();
        samArrayList1.add("SA1");
        samArrayList1.add("SA2");
        samArrayList1.add("SA3");
        
        samArrayList2.add("SA8");
        samArrayList2.add("SA9");
        samHashMap.put("BU1", samArrayList1);
        samHashMap.put("BU2", samArrayList2);
        samHashMap.put("BU3", samArrayList1);
        this.buSalesDataMap = samHashMap;
    } */
    	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/user/editGroup.jsp";
	}

    public void setupChildren() {
    }
} 
