package com.pfizer.webapp.wc.page; 
import com.pfizer.db.Employee;
import com.pfizer.db.LaunchMeetingDetails;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.wc.components.report.MainReportListWc;
 
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.report.MainReportListWc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.rbu.FutureAllignmentBuDataBean;
import com.tgix.rbu.FutureAllignmentRBUDataBean;
import com.tgix.rbu.ProductDataBean;
import com.tgix.wc.WebPageComponent;

public class ListReportWpc extends MainTemplateWpc { 
	
	public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart, OverallProcessor processor ) {
		super( user, "listreport" );
		
		main =  new MainReportListWc( user, filter, chart, processor );
	}
    
    /**
     * POA report
     */
	public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee ) {
		super( user, "listreport","POA");
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee);
	}
	
    /**
     * New constructor for displaying the drop downs for RBU Pie chart details
     */
    public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee,String eventType, ProductDataBean[] product,  FutureAllignmentBuDataBean[] bu, FutureAllignmentRBUDataBean[] rbu) {
		
     
          super( user, "dashreport",eventType);
       
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee, eventType, product, bu, rbu);
	}

 /**
     * New constructor for displaying the drop downs for Toviaz Launch chart details
     */
    public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee,String eventType,  FutureAllignmentBuDataBean[] bu, FutureAllignmentRBUDataBean[] rbu) {
		
     
          super( user, "toviazlaunch",eventType);
       
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee, eventType, bu, rbu);
	}



/**
     * New constructor for displaying the drop downs for Toviaz Launch chart details
     */
    public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee,String eventType,  FutureAllignmentBuDataBean[] bu, FutureAllignmentRBUDataBean[] rbu, LaunchMeetingDetails phase, String trackId) {
		
     
          super( user, "launchMeeting",eventType);
       
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee, eventType, bu, rbu, phase,trackId);
	}

    
    
    /**
     * POA report
     */
	public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee,String eventType ) {
		
     
          super( user, "listreport",eventType);
       
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee, eventType);
	}

	public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee,String eventType, String sController, String sJSPPath) {
		
     
          super( user, "listreport",eventType);
       
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee, eventType, sController, sJSPPath);
	}

	public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee,String eventType, String sController, String sJSPPath, boolean bExcel) {
		
     
          super( user, "listreport",eventType);
       
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee, eventType, sController, sJSPPath);
	}
    
    public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee,boolean excel) {
		super( user, "listreport","POA");
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee,excel);
	}
    
    public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee,boolean excel, String eventType, ProductDataBean[] product) {
		super( user, "listreport",eventType);
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee,excel, eventType, product);
	}
    
    public ListReportWpc ( User user, UserFilter filter, ChartDetailWc chart,Employee[] poaEmployee,boolean excel, String eventType) {
		super( user, "toviazlaunch",eventType);
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee,excel, eventType);
	}
	
    
	public void setAreaManager( Employee emp ) {
		((MainReportListWc)main).setAreaManager( emp );
	}
	public void setRegionManager( Employee emp ) {
		((MainReportListWc)main).setRegionManager( emp );
	}
	public void setDistrictManager( Employee emp ) {
		((MainReportListWc)main).setDistrictManager( emp );
	}
    
    
} 
