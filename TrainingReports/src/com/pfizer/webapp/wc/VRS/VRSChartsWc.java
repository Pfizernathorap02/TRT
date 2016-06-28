package com.pfizer.webapp.wc.VRS; 
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.wc.WebComponent;

public class VRSChartsWc extends WebComponent { 
    WebComponent chart = null;
    WebComponent territorySelect;
     WebComponent headerWc;
     
     
    
    public VRSChartsWc(TerritoryFilterForm form, User user, WebComponent headerWc) {
        super();
        this.headerWc = headerWc;
        AreaRegionDistJsWc dynamicJs = new AreaRegionDistJsWc( user.getUserTerritoryOld(), form );

		dynamicJavascripts.add( dynamicJs );
        
        territorySelect = new TerritorySelectWc( form, user.getUserTerritoryOld(), "/TrainingReports/VRS/getFilteredChartVRS" );
        
        ((TerritorySelectWc)territorySelect).setShowTeam(true);
        ((TerritorySelectWc)territorySelect).setLayout(TerritorySelectWc.LAYOUT_special);        
        javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
                                
    }
    public String getJsp()
    {
        return "/WEB-INF/jsp/VRS/VrsCharts.jsp";
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
