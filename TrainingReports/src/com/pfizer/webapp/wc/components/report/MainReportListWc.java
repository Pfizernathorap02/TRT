package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.Employee;
import com.pfizer.db.LaunchMeetingDetails;
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
import com.tgix.rbu.FutureAllignmentBuDataBean;
import com.tgix.rbu.FutureAllignmentRBUDataBean;
import com.tgix.rbu.ProductDataBean;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;

public class MainReportListWc extends WebComponent { 
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
    private String eventType="";
    private ProductDataBean[] product = null;
    private FutureAllignmentBuDataBean[] bu = null;
    private FutureAllignmentRBUDataBean[] rbu = null;
    private LaunchMeetingDetails phase = null;
    private String trackId = "";
    	
	public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] poaEmployee) {
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
		reportList = new POAReportList(filter,poaEmployee,user);//new ReportListWc( filter, processor, user );
         if(poaEmployee.length>0) total=poaEmployee.length;
       
		// Used to create the dynmaic javascript in the html <head> tag.
		dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
		dynamicJavascripts.add( dynamicJs );
		
		String type = filter.getQuseryStrings().getType();
		String section = filter.getQuseryStrings().getSection();
		String params = "?type=" + type + "&section=" + section;
		
		territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritory(), AppConst.APP_ROOT + "/POA/listreport" + params );
		territorySelect.setShowTeam(true);
        territorySelect.setLayout( TerritorySelectWc.LAYOUT_ALT );
		javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
        
    }
    
    
    public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] beanEmployee, String eventType, ProductDataBean[] product, FutureAllignmentBuDataBean[] bu, FutureAllignmentRBUDataBean[] rbu) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
        this.eventType = eventType;
        this.product = product;
        this.bu = bu;
        this.rbu = rbu;
      //  System.out.println("##################### MainReportList WC ##############");
            if ( chart != null) {
                chart.setShowDescription( false );
            }
//                System.out.println("Length in MainReportList"+beanEmployee.length);
     //           System.out.println("Product in MainReportList"+userFilter.getProduct());
            reportList = new RBUReportList(filter,beanEmployee,user, product);//new ReportListWc( filter, processor, user );
             if(beanEmployee.length>0) total=beanEmployee.length;
            String type = filter.getQuseryStrings().getType();
            String section = filter.getQuseryStrings().getSection();
            String params = "?type=" + type + "&section=" + section;
    }    
     public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] beanEmployee, String eventType, FutureAllignmentBuDataBean[] bu, FutureAllignmentRBUDataBean[] rbu) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
        this.eventType = eventType;
        this.bu = bu;
        this.rbu = rbu;
            if ( chart != null) {
                chart.setShowDescription( false );
            }
             if(eventType.equals("TOVIAZLAUNCH")){
            reportList = new ToviazLaunchReportList(filter,beanEmployee,user);//new ReportListWc( filter, processor, user );
            }
            if(eventType.equals("TOVIAZLAUNCHEXEC")){
            reportList = new ToviazLaunchReportListExec(filter,beanEmployee,user);//new ReportListWc( filter, processor, user );
            }
             if(beanEmployee.length>0) total=beanEmployee.length;
            String type = filter.getQuseryStrings().getType();
            String section = filter.getQuseryStrings().getSection();
            String params = "?type=" + type + "&section=" + section;
    }    
    public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] beanEmployee, String eventType, FutureAllignmentBuDataBean[] bu, FutureAllignmentRBUDataBean[] rbu, LaunchMeetingDetails  phase, String trackId) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
        this.eventType = eventType;
        this.bu = bu;
        this.rbu = rbu;
        this.phase = phase;
        this.trackId = trackId;
            if ( chart != null) {
                chart.setShowDescription( false );
            }
            reportList = new LaunchMeetingReportList(filter,beanEmployee,user,trackId);//new ReportListWc( filter, processor, user );
            if(beanEmployee.length>0) total=beanEmployee.length;
            String type = filter.getQuseryStrings().getType();
            String section = filter.getQuseryStrings().getSection();
            String params = "?type=" + type + "&section=" + section;
    }
    
	public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] beanEmployee, String eventType) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
        this.eventType = eventType;
		// turn off description for report page.
        
        if (eventType.equalsIgnoreCase("PDFHS")){
        
            if ( chart != null) {
                chart.setShowDescription( false );
            }
                System.out.println("Length in MainReportList"+beanEmployee.length);
                System.out.println("Product in MainReportList"+userFilter.getProduct());
            reportList = new PDFHSReportList(filter,beanEmployee,user);//new ReportListWc( filter, processor, user );
             if(beanEmployee.length>0) total=beanEmployee.length;
           
            // Used to create the dynmaic javascript in the html <head> tag.
            //dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
            dynamicJs = new AreaRegionDistJsWc(user.getUserTerritoryOld(), userFilterForm );
            dynamicJavascripts.add( dynamicJs );
            
            String type = filter.getQuseryStrings().getType();
            String section = filter.getQuseryStrings().getSection();
            String params = "?type=" + type + "&section=" + section;
            
            territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritoryOld(), AppConst.APP_ROOT + "/PWRA/listreport" + params );
            territorySelect.setShowTeam(true);
            territorySelect.setLayout( TerritorySelectWc.LAYOUT_ALT );
            javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");
        }
        else if (eventType.equalsIgnoreCase("PLC")){        
            if ( chart != null) {
                chart.setShowDescription( false );
            }
            reportList = new PLCReportList(filter,beanEmployee,user);
             if(beanEmployee.length>0) total=beanEmployee.length;
           
            dynamicJs = new AreaRegionDistJsWc( user.getUserTerritoryOld(), userFilterForm );
            dynamicJavascripts.add( dynamicJs );
            
            String type = filter.getQuseryStrings().getType();
            String section = filter.getQuseryStrings().getSection();
            String params = "?type=" + type + "&section=" + section;
            
            territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritoryOld(), AppConst.APP_ROOT + "/PWRA/listReportPLC" + params );
            territorySelect.setShowTeam(true);
            territorySelect.setLayout( TerritorySelectWc.LAYOUT_ALT );
            javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");            
        }else if (eventType.equalsIgnoreCase("SPFPLC")){
            if ( chart != null) {
                chart.setShowDescription( false );
            }
            reportList = new SPFPLCReportList(filter,beanEmployee,user);
             if(beanEmployee.length>0) total=beanEmployee.length;
           
            dynamicJs = new AreaRegionDistJsWc( user.getUserTerritoryOld(), userFilterForm );
            dynamicJavascripts.add( dynamicJs );
            
            String type = filter.getQuseryStrings().getType();
            String section = filter.getQuseryStrings().getSection();
            String params = "?type=" + type + "&section=" + section;
            
           /* Infosys code changes starts here
            * territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritoryOld(), AppConst.APP_ROOT + "/SPF/listReportPLC.do" + params );
            * Infosys code changes ends here
          */  
            territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritoryOld(), AppConst.APP_ROOT + "/SPF/listReportPLC" + params );
            
            territorySelect.setShowTeam(true);            
            territorySelect.setLayout( TerritorySelectWc.LAYOUT_ALT );
            javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");                        
        }
        /*Changing the conditions to hold the old values */
        else if (eventType.equalsIgnoreCase("GNSM")){
            if ( chart != null) {
                chart.setShowDescription( false );
            }            
            reportList = new GNSMReportListWc(filter,beanEmployee,user);
             if(beanEmployee.length>0) total=beanEmployee.length;
           
            //dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
            dynamicJs = new AreaRegionDistJsWc( user.getUserTerritoryOld(), userFilterForm );
            dynamicJavascripts.add( dynamicJs );
            
            String type = filter.getQuseryStrings().getType();
            String section = filter.getQuseryStrings().getSection();
            String params = "?type=" + type + "&section=" + section;
            /*Infosys - Weblogic to Jboss Migrations changes start here*/
            territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritoryOld(), AppConst.APP_ROOT + "/GNSM/listReportGNSM" + params );
            /*Infosys - Weblogic to Jboss Migrations changes end here*/
            territorySelect.setShowTeam(true);
            territorySelect.setLayout( TerritorySelectWc.LAYOUT_special );
            javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");                        
        }else if (eventType.equalsIgnoreCase("MSEPI")){
            if ( chart != null) {
                chart.setShowDescription( false );
            }            
            reportList = new MSEPIReportListWc(filter,beanEmployee,user);
             if(beanEmployee.length>0) total=beanEmployee.length;
           
            //dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
            dynamicJs = new AreaRegionDistJsWc( user.getUserTerritoryOld(), userFilterForm );
            dynamicJavascripts.add( dynamicJs );
            
            String type = filter.getQuseryStrings().getType();
            String section = filter.getQuseryStrings().getSection();
            String params = "?type=" + type + "&section=" + section;
            /*Infosys - Weblogic to Jboss Migrations changes start here*/
            territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritoryOld(), AppConst.APP_ROOT + "/MSEPI/listReportMSEPI" + params );
            /*Infosys - Weblogic to Jboss Migrations changes end here*/
            territorySelect.setShowTeam(true);
            territorySelect.setLayout( TerritorySelectWc.LAYOUT_special );
            javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");                        
        }

        /*Added condition for Vista Rx Spiriva enhancement
         * Author: Meenakshi
         * Date:15-Sep-2008
        */
        else if (eventType.equalsIgnoreCase("VRS")){
            if ( chart != null) {
                chart.setShowDescription( false );
            }            
            reportList = new VRSReportListWc(filter,beanEmployee,user);
             if(beanEmployee.length>0) total=beanEmployee.length;
           
            //dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
            dynamicJs = new AreaRegionDistJsWc( user.getUserTerritoryOld(), userFilterForm );
            dynamicJavascripts.add( dynamicJs );
            
            String type = filter.getQuseryStrings().getType();
            String section = filter.getQuseryStrings().getSection();
            String params = "?type=" + type + "&section=" + section;
            
            territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritoryOld(), AppConst.APP_ROOT + "/VRS/listReportVRS" + params );
            territorySelect.setShowTeam(true);
            territorySelect.setLayout( TerritorySelectWc.LAYOUT_special );
            javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");                        
        }
        /* End of addition*/
        
    }

	public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] beanEmployee, String eventType, String sController, String sJSPPath) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
		// turn off description for report page.
        
        
        if ( chart != null) {
            chart.setShowDescription( false );
        }

        reportList = new ReportList(filter,beanEmployee,user, sJSPPath);
         if(beanEmployee.length>0) total=beanEmployee.length;
       
        // Used to create the dynmaic javascript in the html <head> tag.
        dynamicJs = new AreaRegionDistJsWc( ut, userFilterForm );
        dynamicJavascripts.add( dynamicJs );
        
        String type = filter.getQuseryStrings().getType();
        String section = filter.getQuseryStrings().getSection();
        String params = "?type=" + type + "&section=" + section;
        
        territorySelect = new TerritorySelectWc( userFilterForm, user.getUserTerritory(), AppConst.APP_ROOT + sController + params );
        territorySelect.setShowTeam(true);
        territorySelect.setLayout( TerritorySelectWc.LAYOUT_ALT );
        javascriptFiles.add(AppConst.JAVASCRIPT_LOC + "tgixDynamicSelect.js");        
    }


    
    public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] poaEmployee, boolean excel) {
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
		reportList = new PDFHSReportList(filter,poaEmployee,user,excel);//new ReportListWc( filter, processor, user );
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
    
     public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] poaEmployee, boolean excel, String eventType, ProductDataBean[] product) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
        this.eventType = eventType;
		// turn off description for report page.
        
		if ( chart != null) {
			chart.setShowDescription( false );
		}
        	System.out.println("Length in MainReportList"+poaEmployee.length);
            System.out.println("Product in MainReportList"+userFilter.getProduct());
		reportList = new RBUReportList(filter,poaEmployee,user,excel, product);//new ReportListWc( filter, processor, user );
       if(poaEmployee.length>0) total=poaEmployee.length;
    }
    
    public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, Employee[] poaEmployee, boolean excel, String eventType) {
		this.userFilterForm = filter.getFilterForm() ;
		this.user = user;
		this.ut = user.getUserTerritory();
		this.userFilter = filter;
		this.chartDetail = chart;
        this.eventType = eventType;
		// turn off description for report page.
        
		if ( chart != null) {
			chart.setShowDescription( false );
		}
        	System.out.println("Length in MainReportList"+poaEmployee.length);
            System.out.println("Product in MainReportList"+userFilter.getProduct());
        if(eventType.equals("TOVIAZLAUNCH") ){       
		reportList = new ToviazLaunchReportList(filter,poaEmployee,user,excel);//new ReportListWc( filter, processor, user );
        }
        if(eventType.equals("TOVIAZLAUNCHEXEC") ){   
            reportList = new ToviazLaunchReportListExec(filter,poaEmployee,user,excel);//new ReportListWc( filter, processor, user );
        }  
        if(eventType.equals("LAUNCHMEETING") ){   
            reportList = new LaunchMeetingReportList(filter,poaEmployee,user,excel);//new ReportListWc( filter, processor, user );
        }  
       if(poaEmployee.length>0) total=poaEmployee.length;
    }
    
    
	public MainReportListWc(User user, UserFilter filter, ChartDetailWc chart, OverallProcessor processor) {
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
    public  String getTrackId(){
        return this.trackId;
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
    
    public ProductDataBean[] getProductDataBean(){
        return this.product;
    }
    
    public FutureAllignmentBuDataBean[] getFutureAllignmentBuDataBean(){
        return this.bu;
    }
    
    public FutureAllignmentRBUDataBean[] getFutureAllignmentRBUDataBean(){
        return this.rbu;
    }
	
     public LaunchMeetingDetails getPhase(){
        return this.phase;
    }
	
    public String getJsp() {
        if(!this.eventType.equals("RBUREPORT") && !this.eventType.equals("TOVIAZLAUNCH") && !this.eventType.equals("TOVIAZLAUNCHEXEC") && !this.eventType.equals("LAUNCHMEETING") ){
            return AppConst.JSP_LOC + "/components/report/MainReportList.jsp";
        }
        else if(this.eventType.equals("RBUREPORT")){
            return AppConst.JSP_LOC + "/components/report/RBUMainReportList.jsp";
        }
        else if(this.eventType.equals("LAUNCHMEETING")){
            return AppConst.JSP_LOC + "/components/report/LaunchMeetingMainReportList.jsp";
        }
        else {
            return AppConst.JSP_LOC + "/components/report/ToviazLaunchMainReportList.jsp";
        }
            
	}

    public void setupChildren() {
        System.out.println("######################## Event type " + eventType);
        if(!this.eventType.equals("RBUREPORT") && !this.eventType.equals("TOVIAZLAUNCH") && !this.eventType.equals("TOVIAZLAUNCHEXEC") && !this.eventType.equals("LAUNCHMEETING")){
		children.add(territorySelect);
        }
		if ( chartDetail != null) {
			children.add(chartDetail);
		}
		
		children.add(reportList);
    }
} 
