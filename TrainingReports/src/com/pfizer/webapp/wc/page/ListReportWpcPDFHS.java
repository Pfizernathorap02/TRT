package com.pfizer.webapp.wc.page; 
import com.pfizer.db.Employee;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.webapp.AppQueryStrings; 
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.report.MainReportListWc;
import com.pfizer.webapp.wc.components.report.PDFHSReportListWc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.wc.WebPageComponent;

public class ListReportWpcPDFHS extends MainTemplateWpc { 
	
	public ListReportWpcPDFHS(User user, UserFilter filter, ChartDetailWc chart, OverallProcessor processor) {
		super( user, "listreport" );
		
		main =  new MainReportListWc( user, filter, chart, processor );
	}
    
    /**
     * POA report
     */
	public ListReportWpcPDFHS(User user, UserFilter filter, ChartDetailWc chart, Employee[] poaEmployee) {
		super( user, "listreport","PDFHS");
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee);
	}
	
    
    public ListReportWpcPDFHS(User user, UserFilter filter, ChartDetailWc chart, Employee[] poaEmployee, boolean excel) {
		super( user, "listreport","PDFHS");
         
		main =  new MainReportListWc( user, filter, chart,poaEmployee,excel);
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
