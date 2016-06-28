package com.pfizer.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.PWRA.EmployeeDetailFacade;
import com.pfizer.actionForm.PWRAGetEmployeeDetailForm;
import com.pfizer.actionForm.RBUGetEmployeeDetailForm;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.EmpSearchPOA;
import com.pfizer.db.Employee;
import com.pfizer.db.Territory;
import com.pfizer.db.TrAudit;
import com.pfizer.hander.AttendanceHandler;
import com.pfizer.hander.AuditHandler;
import com.pfizer.hander.CourseHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.PassFailHandler;
import com.pfizer.hander.RBUSHandler;
import com.pfizer.hander.SceHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.processor.OverallResult;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.webapp.chart.PieChartBuilder;
import com.pfizer.webapp.report.ReportBuilder;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.RBU.RBUEmployeeDetailsEmailWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderPWRAWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartIndexWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.chart.RightBarPWRAWc;
import com.pfizer.webapp.wc.components.chart.RightBarWc;
import com.pfizer.webapp.wc.components.report.MainExemptReportWc;
import com.pfizer.webapp.wc.components.report.PLCEmployeeDetailWc;
import com.pfizer.webapp.wc.components.report.POAEmployeeDetailWc;
import com.pfizer.webapp.wc.components.report.ReportListWc;
import com.pfizer.webapp.wc.page.DetailReportWpc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.pfizer.webapp.wc.util.PageBuilder;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.wc.WebPageComponent;


public class TrainingAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
    /**
     * @common:control
     */
    private EmployeeDetailFacade employeeDetailFacade;
    private HttpServletRequest request;
	private HttpServletResponse response;
	private TransactionDB trDb= new TransactionDB();

	public static final Log log = LogFactory.getLog( TrainingAction.class );
	
    /**
     * @common:control
     */
	

public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
	}

	

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

 	}

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward begin(){
     */
    public String begin() {
		/**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if (getResponse().isCommitted()) {
			return null;
		}
		return charts();
    }
	

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward charts(){
     */
    public String charts() {	
		/**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if (getResponse().isCommitted()) {
			return null;
		}
		Timer timer = new Timer();
		
		UserSession uSession = buildUserSession();
		
		log.info( "charts() after buildUserSession:" + timer.getFromStart());		
		User user = uSession.getUser();
		log.info( "charts() after uSession.getUser();:" + timer.getFromStart());

		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setAdmin(uSession.getUser().isAdmin());
		PieChartBuilder pBuilder = new PieChartBuilder();

		OverallProcessor overall = uSession.getOverallProcessor();
		log.info( "charts() after uSession.getOverallProcessor():" + timer.getFromStart());
			
		// get chart component list
		ChartListWc chartListWc = getChartList( pBuilder, uFilter, uSession.getUser(), overall );
		
		Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );				
        
        
		// this component puts the header info in the chart page.
						
		ChartHeaderWc chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), overall.getTotalEmployees(), uFilter.getProduct());
		
		// this will prepare the right side bar of the chart page.
		RightBarWc rightBarWc = new RightBarWc( uSession.getUserFilterForm() , uSession.getUser(), AppConst.APP_ROOT + "/overview/charts" );
		rightBarWc.setCurrentProduct( uFilter.getProduct() );
		// this is main WebComponent that contain all the other components that will render the page.
		ChartIndexWc main = new ChartIndexWc( chartHeaderWc, chartListWc, rightBarWc,overall.getTotalEmployees() );
		
		PageBuilder builder = new PageBuilder();
		
		// This will create a page based on the MainTemplateWpc WebPageComponent
		
		MainTemplateWpc page = builder.buildPage( main, "Chart Index",uSession.getUser(), "charts" );

		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
		log.info( "charts() end:" + timer.getFromStart());	
			
		AuditHandler auditor = new AuditHandler();

		if (!uSession.isAdmin()) {
			auditor.insertAuditByFilter(uFilter, uSession.getUser(), "piereport" );
		} else {
			auditor.insertAuditByFilter(uFilter, uSession.getOrignalUser(), "piereport" );
		}
		
		page.setLoginRequired(true);
		uSession.setOverallProcessor(null);
        return new String( "success" );
    }


/**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward PWRAcharts(){
     */
    public String PWRAcharts() {	
		/**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if (getResponse().isCommitted()) {
			return null;
		}
		Timer timer = new Timer();
		
		UserSession uSession = buildUserSession();
		
		log.info( "charts() after buildUserSession:" + timer.getFromStart());		
		User user = uSession.getUser();
		log.info( "charts() after uSession.getUser();:" + timer.getFromStart());

		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setAdmin(uSession.getUser().isAdmin());
		PieChartBuilder pBuilder = new PieChartBuilder();

		OverallProcessor overall = uSession.getOverallProcessor();
		log.info( "charts() after uSession.getOverallProcessor():" + timer.getFromStart());
			
		// get chart component list
		ChartListWc chartListWc = getPWRAChartList( pBuilder, uFilter, uSession.getUser(), overall );
		
		Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );				
        
        
		// this component puts the header info in the chart page.
						
		ChartHeaderPWRAWc chartHeaderWc = new ChartHeaderPWRAWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), overall.getTotalEmployees(), uFilter.getProduct());
		
		// this will prepare the right side bar of the chart page.
		RightBarPWRAWc rightBarWc = new RightBarPWRAWc( uSession.getUserFilterForm() , uSession.getUser(), AppConst.APP_ROOT + "/overview/PWRAcharts" );
		rightBarWc.setCurrentProduct( uFilter.getProduct() );
		// this is main WebComponent that contain all the other components that will render the page.
		ChartIndexWc main = new ChartIndexWc( chartHeaderWc, chartListWc, rightBarWc,overall.getTotalEmployees() );
		
		PageBuilder builder = new PageBuilder();
		
		// This will create a page based on the MainTemplateWpc WebPageComponent
		
		MainTemplateWpc page = builder.buildPWRAPage( main, "Chart Index",uSession.getUser(), "charts" );

		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
		log.info( "charts() end:" + timer.getFromStart());	
			
		AuditHandler auditor = new AuditHandler();

		if (!uSession.isAdmin()) {
			auditor.insertAuditByFilter(uFilter, uSession.getUser(), "piereport" );
		} else {
			auditor.insertAuditByFilter(uFilter, uSession.getOrignalUser(), "piereport" );
		}
		
		page.setLoginRequired(true);
		uSession.setOverallProcessor(null);
        return new String( "success" );
    }



    /**
     * @jpf:action
     * @jpf:forward name="successXls" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward listreport(){
     */
    public String listreport() {
		/**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if (getResponse().isCommitted()) {
			return null;
		}
		ServiceFactory factory = Service.getServiceFactory( trDb );
		EmployeeHandler eHandler = factory.getEmployeeHandler();
		WebPageComponent page;

		// check if coming from email, if so reset search to 
		// clear all filters in session.
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
		if ("true".equals( qStrings.getFromEmail() ) ) {
			UserSession uSession = UserSession.getUserSession(getRequest());
			UserFilter tempFilter = uSession.getUserFilter();
			tempFilter.setAdmin(uSession.getUser().isAdmin());
			tempFilter.setFilterForm( uSession.getNewTerritoryFilterForm() );
		}
						
		UserSession uSession = buildUserSession();
		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setAdmin(uSession.getUser().isAdmin());

		OverallProcessor overall = uSession.getOverallProcessor();
		
		ChartDetailWc chart = getChart( uFilter, overall );
		
		if ( "true".equals( uFilter.getQuseryStrings().getDownloadExcel() ) ) {
			ReportListWc reportList = new ReportListWc( uFilter, overall, uSession.getUser() );
			reportList.setLayout( ReportListWc.LAYOUT_EXCEL );
			page = new BlankTemplateWpc(reportList);
			
			getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );	
			getResponse().addHeader("content-disposition","attachment;filename=trainingreports.xls");
			
			getResponse().setContentType("application/vnd.ms-excel");	
			getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
			getResponse().setHeader("Pragma","public");		
			
			
			AuditHandler auditor = new AuditHandler();
			auditor.insertAuditByFilter(uFilter, uSession.getUser(), "excel_download" );
			uSession.setOverallProcessor(null);		
		    /**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			    	return new Forward("successXls");
			 */
			return new String("successXls");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
			
		} 
			
		page = new ListReportWpc( uSession.getUser(), uFilter, chart,  overall);
		
		uFilter.setClusterCode( uSession.getUser().getCluster() );
		Employee areaManager = eHandler.getAreaManager( uFilter );
		Employee regionManager = eHandler.getRegionManager( uFilter );
		Employee districtManager = eHandler.getDistrictManager( uFilter );
		
		((ListReportWpc)page).setAreaManager( areaManager );
		((ListReportWpc)page).setRegionManager( regionManager );
		((ListReportWpc)page).setDistrictManager( districtManager );
		
		TrAudit audit = new TrAudit( uSession.getUser(), TrAudit.ACTION_REPORT );
		audit.setPageName("listreport");		
		
		if ( Util.isEmpty( uFilter.getQuseryStrings().getSortBy().getField() ) ) {		
			AuditHandler auditor = new AuditHandler();
			if ("true".equals( qStrings.getFromEmail() ) ) {
				if (!uSession.isAdmin()) {
					auditor.insertAuditByFilter(uFilter, uSession.getUser(), "listreportByEmail" );
				} else {
					auditor.insertAuditByFilter(uFilter, uSession.getOrignalUser(), "listreportByEmail" );
				}
			} else {
				if (!uSession.isAdmin()) {
					auditor.insertAuditByFilter(uFilter, uSession.getUser(), "listreport" );
				} else {
					auditor.insertAuditByFilter(uFilter, uSession.getOrignalUser(), "listreport" );
				}
			}
		}
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
		uSession.setOverallProcessor(null);		
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success");
		 */
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

					
    }
    
    private String getExemptDetail(String emplid, String productCode) {
        UserSession uSession = UserSession.getUserSession(getRequest());
        MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),"ExemptDetail");
        
        ServiceFactory factory = Service.getServiceFactory(); 
        EmployeeHandler eHandler = factory.getEmployeeHandler();
		System.out.println("Coming here in the Method");
        MainExemptReportWc main=new MainExemptReportWc(eHandler.getEmployeeById(emplid),productCode,uSession.getUser());
        Employee thisEmployee=eHandler.getEmployeeById(emplid);
        main.setEmployeeImg(eHandler.getEmployeeImage(emplid));
        main.setReportsTo(eHandler.getEmployeeById(thisEmployee.getReportsToEmplid()));
        main.setExemptionReason(eHandler.getExemptionReason(emplid,productCode));
        page.setMain( main); 
        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);
        /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success");
		 */
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

    }
    
    
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward detailreportPOA(){
     */
    public String detailreportPOA() {
		/**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if (getResponse().isCommitted()) {
			return null;
		}
		UserSession uSession = UserSession.getUserSession(getRequest());
        MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),"detailreport","POA");
        String emplid = getRequest().getParameter("emplid");
        
        ServiceFactory factory = Service.getServiceFactory(); 
        EmployeeHandler eHandler = factory.getEmployeeHandler();
        EmpSearchPOA[] empSearchPOA = eHandler.getPOAEmployeesById(emplid);
		System.out.println("Coming here in the Method");
        POAEmployeeDetailWc main=new POAEmployeeDetailWc(eHandler.getEmployeeById(emplid),uSession.getUser(),empSearchPOA);
        Employee thisEmployee=eHandler.getEmployeeById(emplid);
        main.setEmployeeImg(eHandler.getEmployeeImage(emplid));
        main.setReportsTo(eHandler.getEmployeeById(thisEmployee.getReportsToEmplid()));
        //main.setExemptionReason(eHandler.getExemptionReason(emplid,productCode));
        page.setMain( main); 
        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);
        /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success");
		 */
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

        
        
		
    }	

         /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     
    //Method Author: Derick 
    public String detailreportPDFHS() {
		if ( getResponse().isCommitted() ) {
		return null;
	    }
        
        UserSession uSession = UserSession.getUserSession(getRequest());
        MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),"detailreport","PDFHS");
        String emplid = getRequest().getParameter("emplid");
        
        ServiceFactory factory = Service.getServiceFactory(); 
        EmployeeHandler eHandler = factory.getEmployeeHandler();
        EmpSearchPOA[] empSearchPOA = eHandler.getPOAEmployeesById(emplid);
		System.out.println("Coming here in the Method");
        PDFHSEmployeeDetailWc main=new PDFHSEmployeeDetailWc(eHandler.getEmployeeById(emplid),uSession.getUser(),empSearchPOA);
        Employee thisEmployee=eHandler.getEmployeeById(emplid);
        main.setEmployeeImg(eHandler.getEmployeeImage(emplid));
        main.setReportsTo(eHandler.getEmployeeById(thisEmployee.getReportsToEmplid()));
        //main.setExemptionReason(eHandler.getExemptionReason(emplid,productCode));
        page.setMain( main); 
        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);
        return new String("success");     	
    }	*/
    
         /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    //Method Author: Derick 
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward detailreportPDFHS(){
     */
    public String detailreportPDFHS(PWRAGetEmployeeDetailForm form) {
		/**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if (getResponse().isCommitted()) {
			return null;
		}
		UserSession uSession = UserSession.getUserSession(getRequest());
        MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),"detailreport","PDFHS");        
        String emplid = getRequest().getParameter("emplid");
        
        
        //New Code
        PLCEmployeeDetailWc main = new PLCEmployeeDetailWc("PDFHS");        

        form.setEmployeeInfo(employeeDetailFacade.getEmployeeInfo(emplid));        
        form.setProductAssignmentInfo(employeeDetailFacade.getProductAssignment(emplid));
        form.setTrainingMaterialHistoryInfo(employeeDetailFacade.getTrainingMaterialHistory(emplid));
        form.setPdfHomeStudyStatus(employeeDetailFacade.getPdfHomeStudyStatusInfo(emplid));
        form.setOverallHomeStudyStatus(employeeDetailFacade.getOverallHomeStudyStatus(emplid));
        form.setOverallPLCStatus(employeeDetailFacade.getOverallPLCStatus(emplid));
        form.setPlcStatusInfo(employeeDetailFacade.getPLCStatusInfo(emplid));
        form.setTrainingSchedule(employeeDetailFacade.getTrainingScheduleInfo(emplid));
        main.setFormBean(form);
        page.setMain( main); 
        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);                        
      /**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    	return new Forward("success",form);
	 */
	//return new String("success", form);
	return new String("success");
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

           
        
        //End New Code
        
        
        /*ServiceFactory factory = Service.getServiceFactory(); 
        EmployeeHandler eHandler = factory.getEmployeeHandler();
        EmpSearchPOA[] empSearchPOA = eHandler.getPOAEmployeesById(emplid);
		System.out.println("Coming here in the Method");
        PDFHSEmployeeDetailWc main=new PDFHSEmployeeDetailWc(eHandler.getEmployeeById(emplid),uSession.getUser(),empSearchPOA);
        Employee thisEmployee=eHandler.getEmployeeById(emplid);
        main.setEmployeeImg(eHandler.getEmployeeImage(emplid));
        main.setReportsTo(eHandler.getEmployeeById(thisEmployee.getReportsToEmplid()));
        //main.setExemptionReason(eHandler.getExemptionReason(emplid,productCode));
        page.setMain( main); 
        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);
        return new String("success");*/        
		
    }	
	
    /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward revokeExemption(){
     */
    public String revokeExemption() {
		/**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if (getResponse().isCommitted()) {
			return null;
		}
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
		UserSession uSession = UserSession.getUserSession(getRequest());
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		
		if (eHandler.isTrainingExempted(qStrings.getEmplid(),qStrings.getProductCode())) {
			PassFailHandler pfh = Service.getServiceFactory().getPassFailHandler();
			Employee employee = eHandler.getEmployeeById(qStrings.getEmplid());
			pfh.revokeExemption(qStrings.getProductCode(),qStrings.getEmplid(),uSession.getUser());
		}
        return detailreport();
    }
    /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/components/report/GrantExemptPopUp.jsp"
     * @jpf:forward name="successReturn" path="/overview/detailreport.do"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward grantExemption(){
     */
    public String grantExemption() {
		/**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if (getResponse().isCommitted()) {
			return null;
		}
		String fromReason=getRequest().getParameter("fromReason")==null?"false":getRequest().getParameter("fromReason").toString();
        if(fromReason.equalsIgnoreCase("true")){
            String fwd=new String("successReturn");
            UserSession uSession = UserSession.getUserSession(getRequest());
            User thisUser=uSession.getUser();
            //We will get all the Values here and then perform the DB Operation
            //We will store the Uses EMplid in the DB
            String userID=thisUser.getEmplid();
            String emplid=getRequest().getParameter("emplId")==null?"":getRequest().getParameter("emplId").toString();
            String prodCD=getRequest().getParameter("prodCd")==null?"":getRequest().getParameter("prodCd").toString();
            String reason=getRequest().getParameter("exemptReason")==null?"":getRequest().getParameter("exemptReason").toString();
            if(reason.trim().length()>0) reason=Util.makeSafeQLString(reason);
            log.info("The Values in GRANT EXEMPTION ARE EMPLID--"+emplid+"--User ID--"+userID+"--ProdCD--"+prodCD+"--Exemption Reason"+reason);
            CourseHandler ch=new CourseHandler();
            ch.delFromCourseAssign(emplid, prodCD);
            ch.delFromDeletedCourseAssig(emplid,prodCD);
            ch.insertInDelCourseAssign(emplid, prodCD,userID,reason);
            ch.refreshDB();
          /*  fwd.addQueryParam("needTraining","Exempted");
            fwd.addQueryParam("emplid",emplid);
            fwd.addQueryParam("productCode",prodCD);*/
            request.getSession().setAttribute("refresh","true");
            return fwd;
        }
        /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success");
		 */
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

    }
    /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward detailreport(){
     */
    public String detailreport() {
		/**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if (getResponse().isCommitted()) {
			return null;
		}
		ServiceFactory factory = Service.getServiceFactory( trDb ); 
		EmployeeHandler eHandler = factory.getEmployeeHandler();
		// check if coming from search
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
		
		// does a quick check to see which page to load, normal detail or exempted detail page.        
        if (eHandler.isTrainingExempted(qStrings.getEmplid(),qStrings.getProductCode())) {
            return getExemptDetail(qStrings.getEmplid(),qStrings.getProductCode());
        }
        
		if ("true".equals( qStrings.getFromSearch() ) ) {
			UserSession uSession = UserSession.getUserSession(getRequest());
			UserFilter tempFilter = uSession.getUserFilter(); 
			tempFilter.setAdmin(uSession.getUser().isAdmin());
			tempFilter.setFilterForm( uSession.getNewTerritoryFilterForm() );
		}
		
		
		PassFailHandler pHandler = factory.getPassFailHandler();
		AttendanceHandler aHandler = factory.getAttendanceHandler();
		
		SceHandler sHandler = factory.getSceHandler();
		
		UserSession uSession = buildUserSession();
		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setAdmin(uSession.getUser().isAdmin());
		
		String employeeId = uFilter.getQuseryStrings().getEmplid();
		
		OverallProcessor overall = uSession.getOverallProcessor();
		
		//ChartDetailWc chart = getChart( uFilter, overall );
		OverallResult or = (OverallResult)overall.getAllEmployeeMap().get( uFilter.getQuseryStrings().getEmplid() );
        log.info("Checking OR Object"+or);
		DetailReportWpc page = new DetailReportWpc( uSession.getUser(), uFilter, or.getEmployee(), overall );
		if ( !Util.isEmpty( or.getEmployee().getReportsToEmplid() ) ) {
			page.setReportsTo( eHandler.getEmployeeById( or.getEmployee().getReportsToEmplid() ) );
		}	
		
		//Added by Amit for Order History in Detail Page
        page.setTrainingOrder(eHandler.empTrainingMaterialByProd(employeeId,uFilter.getProduct()));		
		page.setPedagogueExam( pHandler.getExamsByEmployeeProduct( employeeId, uFilter.getProduct() ) );
		page.setCourseAttendance( aHandler.getAttendanceByEmplyeeProduct( employeeId, uFilter.getProduct() ) );
		page.setSceFull( sHandler.getSalesCallEvaluationFull( employeeId, uFilter.getProduct() ) );
		page.setExamModules( pHandler.getExamModules() );
		page.setEmployeePhoto( eHandler.getEmployeeImage( employeeId ) );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );		
		AuditHandler auditor = new AuditHandler();
		if (!uSession.isAdmin()) {
			auditor.insertAuditByFilter(uFilter, uSession.getUser(), "detailreport" );
		} else {
			auditor.insertAuditByFilter(uFilter, uSession.getOrignalUser(), "detailreport" );
		}
		page.setLoginRequired(true);
		uSession.setOverallProcessor(null);
		if ("true".equals( qStrings.getFromSearch() ) ) {
			page.setPageId("detailreport");
		}
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success");
		 */
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

    }	
	
	
	private UserSession buildUserSession() {
		ServiceFactory factory = Service.getServiceFactory( trDb ); 
		UserSession uSession = UserSession.getUserSession(getRequest());
						
		TerritoryFilterForm filterForm = uSession.getUserFilterForm();

		FormUtil.loadObject(getRequest(),filterForm);
		
		// process query stings
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
		FormUtil.loadObject(getRequest(),qStrings.getSortBy());
		
		qStrings.setFullQueryString( getRequest().getQueryString() );
		
		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setEmployeeId(qStrings.getEmplid());	
		uFilter.setAdmin( uSession.getUser().isAdmin() );	
		uFilter.setClusterCode(uSession.getUser().getCluster());	
		uFilter.setFilterForm(filterForm);
		uFilter.setQueryStrings(qStrings);
		
		uFilter.setProdcut( qStrings.getProductCode() );
		
		

		OverallProcessor overall = UserSession.getUserSession( getRequest() ).getOverallProcessor();
		
		// check to see if data needs refresh.
		if ( overall == null || !overall.isSameFilter( uFilter ) ) {
			Timer timer = new Timer();
			ReportBuilder rBuilder = new ReportBuilder( Service.getServiceFactory( trDb ) );			
			
			// this object do all the db access and fetch all the data needed
			// to generate each pie chart and report.
			log.info( "\n\nbefore OverallProcessor():" + timer.getFromStart());
			overall = rBuilder.getOverallProcessor( uFilter );
			log.info( "after OverallProcessor():" + timer.getFromStart());
			UserSession.getUserSession( getRequest() ).setOverallProcessor( overall );
			
		}
		
		return UserSession.getUserSession( getRequest() );
	}	

	private ChartDetailWc getChart( UserFilter filter, OverallProcessor processor ) {
		ChartDetailWc chartDetailWc = null;
		PieChart chart;
		PieChartBuilder pBuilder = new PieChartBuilder();
		
		if ( "attend".equals( filter.getQuseryStrings().getType() ) ) {
			chart = pBuilder.getAttendanceChart( filter, getRequest().getSession(), processor );
			chartDetailWc = new ChartDetailWc( chart, "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_ATTENDANCE ) );
		}
		if ( "sce".equals( filter.getQuseryStrings().getType() ) ) {
			chart = pBuilder.getSceChart( filter, getRequest().getSession(), processor );
			chartDetailWc = new ChartDetailWc( chart, "Sce chart",new ChartLegendWc( ChartLegendWc.LAYOUT_SCE ) );
		}
		
		if ( "test".equals( filter.getQuseryStrings().getType() ) ) {
			List charts = pBuilder.getPassFailCharts( filter, getRequest().getSession(), processor );
			
			for ( Iterator it = charts.iterator(); it.hasNext(); ) {
				PieChart tmp = (PieChart)it.next();
				if ( tmp.getLabel().equals( filter.getQuseryStrings().getExam() ) ) {
					ChartLegendWc cl = new ChartLegendWc( ChartLegendWc.LAYOUT_PASS_FAIL );
					cl.setExamName( tmp.getLabel() );
					chartDetailWc = new ChartDetailWc( tmp, "Test chart",cl );
					chartDetailWc.setLayout( ChartDetailWc.LAYOUT_ALT );
					return chartDetailWc;
				}
			}
		}
		 
		if ("overall".equals( filter.getQuseryStrings().getType() ) ) {
			// Overall Chart
			chart = pBuilder.getOverallChart( filter, getRequest().getSession(), processor );
			chartDetailWc = new ChartDetailWc( chart, "Overall Chart",new ChartLegendWc( ChartLegendWc.LAYOUT_OVERALL ) );
		} 
		if ( chartDetailWc != null ) {
			chartDetailWc.setLayout( ChartDetailWc.LAYOUT_ALT );
		}
		return chartDetailWc;
	}
	

	/**
	 * 
	 */
	private ChartListWc getChartList( PieChartBuilder pBuilder, UserFilter filter, User user, OverallProcessor overall ) {
		List ret = new ArrayList();
		PieChart chart;

		ServiceFactory factory = Service.getServiceFactory( trDb ); 
						
		// Add all the chartDeatilWc objects to a list				
		List charts = new ArrayList();
		
		// This WebComponent renders a single instance of a chart.				
		ChartDetailWc chartDetailWc = null;
		
		// attendance chart	
		if ( overall.getAttendanceProcessor() != null ) {
			chart = pBuilder.getAttendanceChart( filter, getRequest().getSession(), overall );
			chartDetailWc = new ChartDetailWc( chart, "Attendance chart" ,new ChartLegendWc( ChartLegendWc.LAYOUT_ATTENDANCE ) );
			charts.add( chartDetailWc );			
		}
		
		// Test pass/fail charts
		List testCharts = pBuilder.getPassFailCharts( filter, getRequest().getSession(), overall );
		
		for (Iterator it = testCharts.iterator(); it.hasNext(); ) {
			PieChart tmp = (PieChart)it.next();
			ChartLegendWc cl = new ChartLegendWc( ChartLegendWc.LAYOUT_PASS_FAIL );
			cl.setExamName( tmp.getLabel() );
			chartDetailWc = new ChartDetailWc( tmp, tmp.getLabel() + " Chart" , cl);
			charts.add(chartDetailWc);
		}
		
		// Sales Call Evaluation chart
		if ( overall.getSceProcessor() != null) {
			chart = pBuilder.getSceChart( filter, getRequest().getSession(), overall );
			chartDetailWc = new ChartDetailWc( chart, "Sales Call Evaluation Chart" ,new ChartLegendWc( ChartLegendWc.LAYOUT_SCE ) );
			charts.add( chartDetailWc );			
		}
				
		// Overall Chart
		chart = pBuilder.getOverallChart( filter, getRequest().getSession(), overall );
		chartDetailWc = new ChartDetailWc( chart, "Overall Training Status Chart" ,new ChartLegendWc( ChartLegendWc.LAYOUT_OVERALL ) );
		charts.add( chartDetailWc );			
		
		// Add all chart detail object to the ChartListWc object, this component
		// renders all the chart detail objects.		
		ChartListWc chartListWc = new ChartListWc(charts);
		
		return chartListWc;
	}
    
	/**
	 * 
	 */
	private ChartListWc getPWRAChartList( PieChartBuilder pBuilder, UserFilter filter, User user, OverallProcessor overall ) {
		List ret = new ArrayList();
		PieChart chart;

		ServiceFactory factory = Service.getServiceFactory( trDb ); 
						
		// Add all the chartDeatilWc objects to a list				
		List charts = new ArrayList();
		
		// This WebComponent renders a single instance of a chart.				
		ChartDetailWc chartDetailWc = null;
		
		// Test pass/fail charts
		List testCharts = pBuilder.getPassFailCharts( filter, getRequest().getSession(), overall );
		
		for (Iterator it = testCharts.iterator(); it.hasNext(); ) {
			PieChart tmp = (PieChart)it.next();
			ChartLegendWc cl = new ChartLegendWc( ChartLegendWc.LAYOUT_PASS_FAIL );
			cl.setExamName( tmp.getLabel() );
			chartDetailWc = new ChartDetailWc( tmp, tmp.getLabel() + " Chart" , cl);
			charts.add(chartDetailWc);
		}
		
		// Sales Call Evaluation chart
		if ( overall.getSceProcessor() != null) {
			chart = pBuilder.getSceChart( filter, getRequest().getSession(), overall );
			chartDetailWc = new ChartDetailWc( chart, "Sales Call Evaluation Chart" ,new ChartLegendWc( ChartLegendWc.LAYOUT_SCE ) );
			charts.add( chartDetailWc );			
		}
				
		// Overall Chart
		chart = pBuilder.getOverallChart( filter, getRequest().getSession(), overall );
		chartDetailWc = new ChartDetailWc( chart, "Overall Training Status Chart" ,new ChartLegendWc( ChartLegendWc.LAYOUT_OVERALL ) );
		charts.add( chartDetailWc );			
		
		// Add all chart detail object to the ChartListWc object, this component
		// renders all the chart detail objects.		
		ChartListWc chartListWc = new ChartListWc(charts);
		
		return chartListWc;
	}
        
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward getEmployeeDetails(){
     */
    public String getEmployeeDetails()
    {
        /**     * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		System.out.println("i'm in getEmployeeDetails");
		RBUGetEmployeeDetailForm form=new RBUGetEmployeeDetailForm();
        if ( getResponse().isCommitted() ) {
            return null;
        }
        
        //callSecurePage();
        String emplid = getRequest().getParameter("emplid");
        String commandChangeTime = getRequest().getParameter("commandchangetime");
        UserSession uSession = UserSession.getUserSession(getRequest());
        User user = uSession.getUser();
               
        RBUSHandler handler = new RBUSHandler();
         /*
        System.out.println("inside controller - created handler  "); 
        //Change Schedule Time
        if(commandChangeTime!=null&&commandChangeTime.equals("updateTraining")){            
            String oldCourseID = getRequest().getParameter("ocourseid");
            String newCourseID = getRequest().getParameter("courseID");        
            System.out.println(" controller oldCourseID " + getRequest().getParameter("ocourseid"));
            System.out.println(" controller newCourseID " + getRequest().getParameter("courseID"));   
            //updateScheduleTime(emplid,oldCourseID,newCourseID);           
            handler.updateTraining(emplid, user.getId(), oldCourseID, newCourseID, getRequest().getParameter("reason"));

        }
        if(commandChangeTime!=null&&commandChangeTime.equals("addTraining")){                 
            handler.addTraining(emplid,user.getId(),getRequest().getParameter("courseID"), getRequest().getParameter("reason"));
        }
        
        if(commandChangeTime!=null&&commandChangeTime.equals("cancelTraining")){
     //       CourseHandler handler = new CourseHandler();
            String sCourseID = getRequest().getParameter("oldclassid");            
            String sReason = getRequest().getParameter("reason");
            System.out.println("commandChangeTime oldclassid = " + getRequest().getParameter("oldclassid"));
            handler.cancelTraining(emplid, user.getId(), sCourseID, sReason );            
   //         handler.insertCancelInDelCourseAssign(emplid, sCourseID, sReason);
     //       handler.delCancelFromCourseAssign(emplid, sCourseID);
        }
        if(commandChangeTime!=null&&commandChangeTime.equals("recoverTraining")){
      //      CourseHandler handler = new CourseHandler();
            String sCourseID = getRequest().getParameter("courseid");            
            String sReason = getRequest().getParameter("reason");
            
      //      handler.insertRecoverInCourseAssign(emplid, sCourseID, sReason);
      //      handler.delRecoverFromDeletedCourseAssign(emplid, sCourseID);            
        }
        
        //Reorder Command
        if(getRequest().getParameter("commandreorder")!=null){
           Enumeration enum = getRequest().getParameterNames();
           Vector reorders = new Vector();                    
           while(enum.hasMoreElements()){
                String name = (String)enum.nextElement();            
                if(name.startsWith("reorder")){                    
                    reorders.addElement(getRequest().getParameter(name));
                }
            }
            if(reorders.size()!=0){
               // reOrderTrainingMaterialHistory(reorders,emplid);
            } 
        }     
                     
*/
       
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_PSCPTEDP,"PSCPTEDP_EMAIL" ); 
        
        RBUEmployeeDetailsEmailWc main = new RBUEmployeeDetailsEmailWc();    
        
        form.setEmployeeInfo(employeeDetailFacade.getEmployeeInfoRBU(emplid));   
        
        form.setProductAssignmentInfo(employeeDetailFacade.getProductAssignmentRBU(emplid));  
        
        form.setRbuStatus(handler.getRBUAllStatus(emplid));  
        
        form.setRbuGuestTrainers(handler.getGuestTrainingList(emplid));
        
                         
       // form.setRbuStatus(employeeDetailFacade.getRBUTrainingStatus(emplid, form.employeeInfo));
        form.setTrainingMaterialHistoryInfo(employeeDetailFacade.getTrainingMaterialHistory(emplid));                        

        form.setTrainingSchedule(employeeDetailFacade.getRBUTrainingScheduleInfo(emplid));
        //form.setCancelTraining(employeeDetailFacade.getRBUCancelTraining(emplid));
            
        main.setFormBean(form);
        page.setMain(main); 
        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);          
                         
      /**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    	return new Forward("success",form);
	 */
	//return new String("success", form);
	return new String("success");
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

       }
    
   

	public void beforeAction() {
		ServiceFactory factory = Service.getServiceFactory(trDb);
		SuperWebPageComponents tpage = new BlankTemplateWpc();
		tpage.setLoginRequired(true);
		IAMUserControl upControl = new IAMUserControl();
		upControl.checkAuth(getRequest(),getResponse(),tpage);
	}
	
	public void afterAction() {
/*
		try {
			UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
			if (uSession == null) {
				System.out.println("uSession is null");
			}
			SuperWebPageComponents page = (SuperWebPageComponents)getRequest().getAttribute(SuperWebPageComponents.ATTRIBUTE_NAME);
			if ( page == null ) {
				page = new BlankTemplateWpc();
				page.setLoginRequired(false);
			}
			IAMUserControl upControl = new IAMUserControl();
			upControl.checkAuth(getRequest(),getResponse(),page);
		} catch (Exception e) {
			log.error(e,e);
		}
*/
	}
    
    
    
}
