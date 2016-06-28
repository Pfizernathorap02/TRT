package com.pfizer.webapp.wc.components.shared; 



import com.pfizer.db.Product;

import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;

import com.pfizer.webapp.user.User;

import com.pfizer.webapp.user.TerritoryFilterForm;

import com.pfizer.webapp.user.UserTerritory;

import com.pfizer.webapp.wc.global.FooterWc;

import com.tgix.wc.WebComponent;

import java.util.ArrayList;

import java.util.List;

/**

 * This components renders the right side of the chart index page.

 */

public class TerritorySelectWc extends WebComponent { 

	public static final int LAYOUT_MAIN = 1;

	public static final int LAYOUT_ALT = 2;
	public static final int LAYOUT_p2l = 3;

    /* Added for RBU */
    public static final int LAYOUT_p2lnew = 4;
    public static final int LAYOUT_special =5;
    public static final int LAYOUT_empty =11;
	

	private TerritoryFilterForm userFilterForm;

	private List logos = new ArrayList();

	private String postUrl;

	

	private AreaRegionDistJsWc dynamicJs;

	private UserTerritory ut;

	

	private Product currentProduct;

	private int layout = 1;
    private boolean showTeam = false;
	
    private boolean showMultipleGeos = false;


	public TerritorySelectWc(TerritoryFilterForm form, UserTerritory ut, String postUrl ) {

		this.userFilterForm = form;

		this.ut = ut;

		this.postUrl = postUrl;

		

		// Used to create the dynmaic javascript in the html <head> tag.

		dynamicJs = new AreaRegionDistJsWc( ut, form );

		dynamicJavascripts.add( dynamicJs );

		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");

	}


    public void setShowTeam(boolean flag) {
        this.showTeam = flag;
    }
    public boolean isShowTeam() {
        return showTeam;
    }
    
      
    /*Added for RBU */
     public void setShowMultipleGeos(boolean flag) {
        this.showMultipleGeos = flag;
    }
    public boolean isShowMultipleGeos() {
        return showMultipleGeos;
    }
    
    /* End of addition for RBU */
    
    
	public void setLayout( int layout ) {

		this.layout = layout;

	}

	

	public UserTerritory getUserTerritory() {

		return ut;

	}

	public TerritoryFilterForm getUserFilterForm() {

		return userFilterForm;

	}

	

	public String getPostUrl() {

		return postUrl;

	}

	

	public void setCurrentProduct(Product prod) {

		this.currentProduct = prod;

	}

	

	

    public String getJsp() {

		if ( layout == LAYOUT_ALT ) {

			return AppConst.JSP_LOC + "/components/shared/TerritorySelectAlt.jsp";	

		}
		if ( layout == LAYOUT_p2l ) {

			return AppConst.JSP_LOC + "/components/shared/TerritorySelectAltLayout.jsp";	

		}
        /* Added for RBU */
        if ( layout == LAYOUT_p2lnew ) {

			return AppConst.JSP_LOC + "/components/shared/TerritorySelectAltLayoutP2L.jsp";	

		}
         if ( layout == LAYOUT_special ) {

			return AppConst.JSP_LOC + "/components/shared/TerritorySelectAltLayoutSpecial.jsp";	

		}
        /*End of RBU */
         if ( layout == LAYOUT_empty ) {

			return AppConst.JSP_LOC + "/components/shared/blank.jsp";	

		}

		return AppConst.JSP_LOC + "/components/shared/TerritorySelect.jsp";

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

