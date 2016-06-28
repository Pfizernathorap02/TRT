package com.pfizer.webapp.wc.SPF; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.wc.WebComponent;

public class SPFChartsWc extends WebComponent { 
    WebComponent chart = null;
    WebComponent territorySelect;
     WebComponent headerWc;
     
     
    
    public SPFChartsWc(TerritoryFilterForm form, User user, WebComponent headerWc) {
        super();
        this.headerWc = headerWc;
        AreaRegionDistJsWc dynamicJs = new AreaRegionDistJsWc( user.getUserTerritory(), form );

		dynamicJavascripts.add( dynamicJs );
        
/*  Infosys code changes starts here   
		territorySelect = new TerritorySelectWc( form, user.getUserTerritory(), "/TrainingReports/SPF/getFilteredChart.do" );
    Infosys code changes ends here
      */ 
        territorySelect = new TerritorySelectWc( form, user.getUserTerritory(), "/TrainingReports/SPF/getFilteredChart" );
        
        ((TerritorySelectWc)territorySelect).setShowTeam(true);
        
        
        javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
        
        
        
        
    }
    public String getJsp()
    {
        return "/WEB-INF/jsp/SPF/SPFCharts.jsp";
    }

    public WebComponent getTerritoryForm() {
        return territorySelect;
    }
    
    public WebComponent getWebComponent() {
        return chart;
    }
    
    public void setWebComponent(WebComponent wc) {
        this.chart = wc;
    }
    
    public WebComponent getHeader() {
		return headerWc;
	}
    
    public void setupChildren()    {
        children.add( headerWc );
    }
    
	    
} 
