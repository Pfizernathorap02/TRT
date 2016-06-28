package com.pfizer.webapp.wc.components.shared; 



import com.pfizer.db.P2lTrackPhase;
import com.pfizer.db.Product;

import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;
//import com.pfizer.webapp.user.ReportFilterForm;

import com.pfizer.webapp.user.User;

import com.pfizer.webapp.user.TerritoryFilterForm;

import com.pfizer.webapp.user.UserTerritory;

import com.pfizer.webapp.wc.global.FooterWc;
import com.tgix.printing.LoggerHelper;

import com.tgix.wc.WebComponent;

import java.util.ArrayList;

import java.util.List;

/**

 * This components renders the right side of the chart index page.

 */

public class ReportFilterSelectWc extends WebComponent { 

	public static final int LAYOUT_MAIN = 1;

	public static final int LAYOUT_ALT = 2;
	public static final int LAYOUT_p2l = 3;

    /* Added for RBU */
    public static final int LAYOUT_p2lnew = 4;
    public static final int LAYOUT_special =5;
    public static final int LAYOUT_empty =11;
	

	private TerritoryFilterForm filterForm;

	private List logos = new ArrayList();

	private String postUrl;
    ///////Added for TRT Phase 2 employee grid - HQ Users///////
    private User user;
    public void setUser(User user){
        this.user=user;
    }
    public User getUser(){
        return this.user;
    }
    private String selectedOptFields;
    public void setSelectedOptFields(String r) {
    	this.selectedOptFields = r;
    }
    public String getSelectedOptFields() {
    	return this.selectedOptFields;
    }
    /////end employee grid///////
	

	private AreaRegionDistJsWc dynamicJs;

	private UserTerritory ut;
    private P2lTrackPhase phase = null;

	

	private Product currentProduct;

	private int layout = 1;
    private boolean showTeam = false;
	
    private boolean showMultipleGeos = false;


	public ReportFilterSelectWc(TerritoryFilterForm form, UserTerritory ut, String postUrl, P2lTrackPhase phase  ) {

		this.filterForm = form;

		this.ut = ut;

		this.postUrl = postUrl;
        
        this.phase = phase;
        LoggerHelper.logSystemDebug("inside reportfilterselectwc");

		

		// Used to create the dynmaic javascript in the html <head> tag.

		dynamicJs = new AreaRegionDistJsWc(ut, form );

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
    
    public P2lTrackPhase getPhase() {

		return this.phase;

	}
     public String checkOption(String disp, String sel) {
				if(disp.equals(sel))
				  return "checked ";
				return "";
    } 
    
    public boolean showPending() {
        return phase.getApprovalStatus();
    }
    public boolean showAssigned() {
        return phase.getAssigned();
    }
    public boolean showExempt() {
       
        return phase.getExempt();
    }
    
	public TerritoryFilterForm getTerritoryFilterForm() {

		return filterForm;

	}

	

	public String getPostUrl() {

		return postUrl;

	}

	

	public void setCurrentProduct(Product prod) {

		this.currentProduct = prod;

	}

	
  
	

    public String getJsp() {
	if ( layout == LAYOUT_p2l ) {

		return AppConst.JSP_LOC + "/components/shared/ReportFilterSelectAltLayout.jsp";	

	}
        /* Added for RBU */
        if ( layout == LAYOUT_p2lnew ) {

		return AppConst.JSP_LOC + "/components/shared/ReportFilterSelectAltLayoutP2L.jsp";	
	}
       if ( layout == LAYOUT_empty ) {

		return AppConst.JSP_LOC + "/components/shared/blank.jsp";	

	}
	return AppConst.JSP_LOC + "/components/shared/ReportFilterSelect.jsp";
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


