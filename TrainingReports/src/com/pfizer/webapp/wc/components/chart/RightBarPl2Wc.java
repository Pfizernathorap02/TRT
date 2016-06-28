package com.pfizer.webapp.wc.components.chart; 

import com.pfizer.db.Product;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;

import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.List;
/**
 * This components renders the right side of the chart index page.
 */
public class RightBarPl2Wc extends WebComponent { 
	private TerritoryFilterForm userFilterForm;
	private List logos = new ArrayList();
	private TerritorySelectWc territorySelect;
	
	private AreaRegionDistJsWc dynamicJs;
	private UserTerritory ut;
	private User user;
	
    private WebComponent searchWc;
	
	private String currentProductCode;
	

	public RightBarPl2Wc(TerritorySelectWc territorySelect, User user, WebComponent search) {
		this.user = user;
		
		this.territorySelect = territorySelect;
		this.searchWc = search;
		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
	}
    //Added for TRT Phase 2 - Requirement no. F3 - HQ users
    public RightBarPl2Wc( User user, WebComponent search) {
		this.user = user;
		this.searchWc = search;
		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
	}
    //ends here

    public WebComponent getSearch() {
        return searchWc;
    }
	public WebComponent getTerritorySelect() {
		return territorySelect;
	}
	
	public UserTerritory getUserTerritory() {
		return ut;
	}
	public TerritoryFilterForm getUserFilterForm() {
		return userFilterForm;
	}
	
	public User getUser() {
		return user;
	}
	
	
	public AreaRegionDistJsWc getAreaRegionDistJs() {
		return dynamicJs;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/chart/rightBarPl2.jsp";
	}	
	
	
	/**
	 * List of logos to display
	 */
	public void setLogos(List logos) {
		this.logos = logos;
	}
	
	/**
	 * component does not have any childred.
	 */
	public void setupChildren() {
        children.add(territorySelect);
    }	

} 
