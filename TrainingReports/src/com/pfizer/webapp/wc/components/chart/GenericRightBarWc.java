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
public class GenericRightBarWc extends WebComponent { 
	private TerritoryFilterForm userFilterForm;
	private WebComponent territorySelect;
	private WebComponent bottomWc;
    
	private AreaRegionDistJsWc dynamicJs;
	private UserTerritory ut;
	private User user;
	
	
	private String currentProductCode;
	

	public GenericRightBarWc(TerritorySelectWc selectWc, User user) {
		this.userFilterForm = selectWc.getUserFilterForm();
		this.user = user;
		this.ut = user.getUserTerritory();
		
		// Used to create the dynmaic javascript in the html <head> tag.
		dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
		dynamicJavascripts.add( dynamicJs );
		territorySelect = selectWc;
		
		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
	}

    public void setBottomWc( WebComponent wc) {
        this.bottomWc = wc;
    }
    public WebComponent getBottomWc() {
        return bottomWc;
    }
    public void setTerritorySelect(WebComponent wc) {
        this.territorySelect = wc;
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
		return AppConst.JSP_LOC + "/components/chart/GenericRightBar.jsp";
	}	
	
	
	
	/**
	 * component does not have any childred.
	 */
	public void setupChildren() {}	

} 
