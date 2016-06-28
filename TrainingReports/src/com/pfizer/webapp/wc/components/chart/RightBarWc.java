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

public class RightBarWc extends WebComponent { 

	private TerritoryFilterForm userFilterForm;

	private List logos = new ArrayList();

	private TerritorySelectWc territorySelect;

	

	private AreaRegionDistJsWc dynamicJs;

	private UserTerritory ut;

	private User user;

	

	

	private String currentProductCode;

	



	public RightBarWc( TerritoryFilterForm form, User user, String postUrl ) {

		this.userFilterForm = form;

		this.user = user;

		this.ut = user.getUserTerritory();

		

		// Used to create the dynmaic javascript in the html <head> tag.

		dynamicJs = new AreaRegionDistJsWc( ut, form );

		dynamicJavascripts.add( dynamicJs );

		

		territorySelect = new TerritorySelectWc( form, user.getUserTerritory(), postUrl );

		

		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");

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

	

	public void setCurrentProduct(String prodCode) {

		this.currentProductCode = prodCode;

	}

	public String getCurrentProduct() {

		return this.currentProductCode;

	}

	

	public AreaRegionDistJsWc getAreaRegionDistJs() {

		return dynamicJs;

	}

	

    public String getJsp() {

		return AppConst.JSP_LOC + "/components/chart/rightBar.jsp";

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

	public void setupChildren() {}	



} 

