package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.Employee;
import com.pfizer.db.PassFail;
import com.pfizer.db.Product;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.ErrorWc;
import com.pfizer.webapp.wc.components.UnauthorizedWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;

public class PDFHSReportListWc extends WebComponent { 
	private TerritoryFilterForm userFilterForm;
	
	private TerritorySelectWc territorySelect;
	private ChartDetailWc chartDetail = null;
	private WebComponent reportList = null;
	
	private AreaRegionDistJsWc dynamicJs;

	private Employee areaManager = null;
	private Employee regionManager = null;
	private Employee districtManager = null;
   

	private UserTerritory ut;
	private User user;
	private UserFilter userFilter;
	private String reportTitle = "";
	private int total;
	
	public PDFHSReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] poaEmployee) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
		// turn off description for report page.
        
		if ( chart != null) {
			chart.setShowDescription( false );
		}
        	System.out.println("Length in MainReportList"+poaEmployee.length);
            System.out.println("Product in MainReportList"+userFilter.getProduct());
		reportList = new PDFHSReportList(filter,poaEmployee,user);//new ReportListWc( filter, processor, user );
         if(poaEmployee.length>0) total=poaEmployee.length;
       
		// Used to create the dynmaic javascript in the html <head> tag.
		dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
		dynamicJavascripts.add( dynamicJs );
		
		String type = filter.getQuseryStrings().getType();
		String section = filter.getQuseryStrings().getSection();
		String params = "?type=" + type + "&section=" + section;
		
		territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritory(), AppConst.APP_ROOT + "/PWRA/listreport" + params );
		territorySelect.setShowTeam(true);
        territorySelect.setLayout( TerritorySelectWc.LAYOUT_ALT );
		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
        
    }
    
    
    public PDFHSReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] poaEmployee, boolean excel) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
		// turn off description for report page.
        
		if ( chart != null) {
			chart.setShowDescription( false );
		}
        	System.out.println("Length in MainReportList"+poaEmployee.length);
            System.out.println("Product in MainReportList"+userFilter.getProduct());
		reportList = new POAReportList(filter,poaEmployee,user,excel);//new ReportListWc( filter, processor, user );
       if(poaEmployee.length>0) total=poaEmployee.length;
       
		// Used to create the dynmaic javascript in the html <head> tag.
		dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
		dynamicJavascripts.add( dynamicJs );
		
		String type = filter.getQuseryStrings().getType();
		String section = filter.getQuseryStrings().getSection();
		String params = "?type=" + type + "&section=" + section;
		
		territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritory(), AppConst.APP_ROOT + "/PWRA/listreport" + params );
		territorySelect.setShowTeam(true);
        territorySelect.setLayout( TerritorySelectWc.LAYOUT_ALT );
		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
        
    }
    
    
	public PDFHSReportListWc(User user, UserFilter filter, ChartDetailWc chart, OverallProcessor processor) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
		this.total = processor.getTotalEmployees();
		
		// turn off description for report page.
		if ( chart != null) {
			chart.setShowDescription( false );
		}	
		reportList = new ReportListWc( filter, processor, user );
		
		if ( "sce".equals( filter.getQuseryStrings().getType() ) ) {
			reportTitle = "Sales Call Evaluation (SCE) : " + filter.getQuseryStrings().getSection();
		}				
		if ( "attend".equals( filter.getQuseryStrings().getType() ) ) {
			reportTitle = "Attendance : " + filter.getQuseryStrings().getSection();
		}				
		if ( "overall".equals( filter.getQuseryStrings().getType() ) ) {
			reportTitle = "Overall : " + filter.getQuseryStrings().getSection();
		}				
		if ( "test".equals( filter.getQuseryStrings().getType() ) ) {
			if (PassFail.CONST_TEST_FAIL.equals(filter.getQuseryStrings().getSection()) ) {
				reportTitle = filter.getQuseryStrings().getExam() + " : &lt; 80%";
			} else if (PassFail.CONST_TEST_PASS.equals(filter.getQuseryStrings().getSection()) ) {
				reportTitle = filter.getQuseryStrings().getExam() + " : &ge; 80%";
			} else {
				reportTitle = filter.getQuseryStrings().getExam() + " : " + filter.getQuseryStrings().getSection();
			}
		}		
		
        
		
		// Used to create the dynmaic javascript in the html <head> tag.
		dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
		dynamicJavascripts.add( dynamicJs );
		
		String type = filter.getQuseryStrings().getType();
		String section = filter.getQuseryStrings().getSection();
		String params = "?type=" + type + "&section=" + section;
		
		if ( "test".equals( type ) ) {
			params = params + "&exam=" + filter.getQuseryStrings().getExam();
		}
		territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritory(), AppConst.APP_ROOT + "/overview/listreport" + params );
		territorySelect.setLayout( TerritorySelectWc.LAYOUT_ALT );
		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
	}
	
	public int getTotal() {
		return total;
	}
	public WebComponent getTerritorySelect() {
		return territorySelect;
	}
	public UserTerritory getUserTerritory() {
		return ut;
	}
	
	public String getReportTitle() {
		return reportTitle;
	}
	
	public WebComponent getChart() {
		return chartDetail;
	}
	public WebComponent getReportList() {
		return reportList;
	}
	public UserFilter getUserFilter() {
		return userFilter;
	}
	public void setAreaManager( Employee emp ) {
		this.areaManager = emp;
	}
	public Employee getAreaManager() {
		return areaManager;
	}
	
	public void setRegionManager( Employee emp ) {
		this.regionManager = emp;
	}
	public Employee getRegionManager() {
		return this.regionManager;
	}
	
	public void setDistrictManager( Employee emp ) {
		this.districtManager = emp;
	}
	public Employee getDistrictManager() {
		return this.districtManager;
	}
	
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/PDFHSReportList.jsp";
	}

    public void setupChildren() {
		children.add(territorySelect);
		if ( chartDetail != null) {
			children.add(chartDetail);
		}
		
		children.add(reportList);
    }
} 
