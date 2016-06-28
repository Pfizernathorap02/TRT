package com.pfizer.webapp.wc.components.shared; 



import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.user.TerritoryFilterForm;

import com.pfizer.webapp.user.UserTerritory;

import com.tgix.wc.DynamicJavascriptWc;

import java.util.List;



public class AreaRegionDistJsWc extends DynamicJavascriptWc { 

	private UserTerritory ut;

	private TerritoryFilterForm tff;

	

	public AreaRegionDistJsWc (UserTerritory ut, TerritoryFilterForm tff ) {

		this.ut = ut;

		this.tff = tff;

	}

	

	public TerritoryFilterForm getFilterForm() {

		return tff;

	}

	

	public UserTerritory getUserTerritory() {

		return ut;

	}

	

    public String getJsp() {

		return AppConst.JSP_LOC + "/components/shared/areaRegionDistJs.jsp";

	}



    public void setupChildren() {

    }

} 

