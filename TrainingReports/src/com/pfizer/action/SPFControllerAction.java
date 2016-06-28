package com.pfizer.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.db.ClassRosterBean;
import com.pfizer.db.EmpReport;
import com.pfizer.db.Employee;
import com.pfizer.db.EnrollChangeReport;
import com.pfizer.db.GeneralSessionEmployee;
import com.pfizer.db.TeamBean;
import com.pfizer.db.Territory;
import com.pfizer.db.VarianceReportBean;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.AdminReportHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.PLCHandler;
import com.pfizer.hander.SPFHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.webapp.chart.PieChartBuilder;
import com.pfizer.webapp.report.ClassFilterForm;
import com.pfizer.webapp.report.ReportBuilder;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.POA.POAChartBean;
import com.pfizer.webapp.wc.SPF.PlcChartsWc;
import com.pfizer.webapp.wc.SPF.SPFChartsWc;
import com.pfizer.webapp.wc.components.admintoolsSelectSPFHSWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.global.ChartBean;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.page.ListReportWpcPDFHS;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.wc.WebPageComponent;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.report.ClassRosterWc;
import com.pfizer.webapp.wc.components.report.EnrollmentChangeReportWc;
import com.pfizer.webapp.wc.components.report.EnrollmentSummaryReportWc;
import com.pfizer.webapp.wc.components.report.GeneralSessionWc;
import com.pfizer.webapp.wc.components.report.PersonalAgendaWc;
import com.pfizer.webapp.wc.components.report.TrainingScheduleEmplListWc;
import com.pfizer.webapp.wc.components.report.TrainingScheduleWc;
import com.pfizer.webapp.wc.components.report.VarianceReportListWc;

public class SPFControllerAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{

	
	TransactionDB trDb= new TransactionDB();
	protected static final Log log = LogFactory.getLog(AdminAction.class );
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	  private Employee[] excelPDFHSEmployee;
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
	public HttpSession getSession() {
		return getRequest().getSession();
	}
	
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	
	private void callSecurePage() {
		SuperWebPageComponents tpage = new BlankTemplateWpc();
		tpage.setLoginRequired(true);
		IAMUserControl upControl = new IAMUserControl();
		upControl.checkAuth(getRequest(),getResponse(),tpage);
	}	

    
    public String begin()
    {
    	try{
        return new String("success");
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}
    }

    
    public String beginHS()
    {
    	try{
        callSecurePage();
        
        if ( getResponse().isCommitted() ) {
			return null;
		}
            
		
        AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
		UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());
        getSession().setAttribute("ReportType","PowersDFHStudy");
         boolean fromFilter=false;
         
        PieChart pieChart;
    
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","SPF" );
        
        TerritoryFilterForm filterForm = uSession.getUserFilterForm();        
                
        String teamDesc="All";
        //System.out.println("filterForm Area"+filterForm.getArea());
        //System.out.println("filterForm Region"+filterForm.getRegion());
        //System.out.println("filterForm District"+filterForm.getDistrict());
        //System.out.println("filterForm District"+filterForm.getTeam());
        //System.out.println("filterForm District"+filterForm.getTeamList().size());
        ServiceFactory factory = Service.getServiceFactory( trDb );
        SPFHandler spfHandler = factory.getSPFHandler();
        UserFilter uFilter = uSession.getUserFilter();
            if(filterForm.getTeamList().size()<=0){
            TeamBean[] allTeam=null;
            allTeam=trDb.getAllSPFTEAM();
            LabelValueBean labelValueBean ;
            filterForm.setTeam("All");
            
            FormUtil.loadObject(getRequest(),filterForm);
            labelValueBean=new LabelValueBean("All","All");
            filterForm.setTeamList(labelValueBean);
            for(int i=0;i<allTeam.length;i++){
              labelValueBean=new LabelValueBean(allTeam[i].getTeamDesc(),allTeam[i].getTeamCd());
              filterForm.setTeamList(labelValueBean);
            }
        
        }else{
            //We need to get the  team description 
            teamDesc=trDb.getTeamDescription(filterForm.getTeam());
            
        }
        //Total Candidates Participating:
        int totalParticipants=spfHandler.getOverAllTotalCount("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        //System.out.println("THE TOTAL PARTICIPANT HERE IS"+totalParticipants);
        Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
        // this component puts the header info in the chart page.
		
		ChartHeaderWc chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc);
        
        
		SPFChartsWc chartpage = new SPFChartsWc( filterForm, uSession.getUser() ,chartHeaderWc) ;
        
        
        
        PieChartBuilder pBuilder = new PieChartBuilder();
        
        qStrings.setSection("");
        
        ChartDetailWc chartdetail=null;
        uSession.getUserFilter().getQuseryStrings().setSection(null);
        
        List charts = getAllCharts();
        
//getUserFilterForm
        
        ChartBean[] thisChartBean = (ChartBean[])spfHandler.getFilteredOverallChart("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("Overall");
//        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Overall",fromFilter, "/SPF/listreport.do");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Overall",fromFilter, "/SPF/listreport");

        ChartListWc chartListWc = new ChartListWc(charts);
        //Add the Overall Trainees;
        chartListWc.setTrainees(pieChart.getCount());
        chartpage.setWebComponent( chartListWc );
		page.setMain( chartpage );	
        
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
        return new String("success"); 
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }

   
    public String listreport() {

		try{
		if ( getResponse().isCommitted() ) {
			return null;
		}
        
        Employee[] spfEmployee=null;

		ServiceFactory factory = Service.getServiceFactory( trDb );
		EmployeeHandler eHandler = factory.getEmployeeHandler();
		WebPageComponent page;

		// check if coming from email, if so reset search to 
		// clear all pieChartfilters in session.
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
        UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());
        TerritoryFilterForm filterForm = uSession.getUserFilterForm();
        FormUtil.loadObject(getRequest(),filterForm);    
        if(filterForm.getTeam().trim().equalsIgnoreCase(""))	filterForm.setTeam("All")	;
        //System.out.println("TEAM IN JPF is"+filterForm.getTeam());
		UserFilter uFilter = uSession.getUserFilter();
        uFilter.setQueryStrings(qStrings);
		uFilter.setAdmin(uSession.getUser().isAdmin());
        //System.out.println("Product is "+qStrings.getType());
        
        PieChart pieChart=null;

		OverallProcessor overall = uSession.getOverallProcessor();
        
        PieChartBuilder pBuilder = new PieChartBuilder();
        ChartBean[] thisChartBean;
        boolean fromFilter=false;
    	SPFHandler spfHandler = factory.getSPFHandler(); 
         ChartDetailWc chart=null;
        //Get The Count For the Geodon
        chart = getChart();
	
		
		if ( "true".equals( uFilter.getQuseryStrings().getDownloadExcel() ) ) {
           
           
               if("Overall".equalsIgnoreCase(qStrings.getType())){
                 excelPDFHSEmployee =eHandler.getSPFOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
                }else{
                excelPDFHSEmployee =eHandler.getSPFEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
                }
            
        
                
	    
			ListReportWpc reportList =  new ListReportWpc( uSession.getUser(), uFilter, chart,excelPDFHSEmployee);	
            

			
			page = new ListReportWpcPDFHS( uSession.getUser(), uFilter, chart,excelPDFHSEmployee,true);		
            
            getSession().setAttribute("xlsBean",excelPDFHSEmployee);
            getSession().setAttribute("xlsType",qStrings.getType());
            getSession().setAttribute("section",qStrings.getSection());
		    return new String("successXls");			
		} 
       //Get All the Filter and Param we need to run the query to get Employee List 
      
       if("Overall".equalsIgnoreCase(qStrings.getType())){
        spfEmployee =eHandler.getSPFOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
        //System.out.println("After the OVERALL PDFHS BEAN LENGTH IS "+spfEmployee.length);
       }else{
        spfEmployee =eHandler.getSPFEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
       }
      
      // spfEmployee =eHandler.getPDFHSEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
       //System.out.println("spfEmployee length in JPF is "+spfEmployee.length);
       //if(pieChart.getCount()==0) chart=null;

	/*   page = new ListReportWpc( uSession.getUser(), uFilter, chart, spfEmployee,"SPF", "/SPF/listreport.do", "/components/report/SPFHSReportList.jsp");		
	 */ 
       
       page = new ListReportWpc( uSession.getUser(), uFilter, chart, spfEmployee,"SPF", "/SPF/listreport", "/components/report/SPFHSReportList.jsp");		
  	 
        uFilter.setClusterCode( uSession.getUser().getCluster() );
		Employee areaManager = eHandler.getAreaManager( uFilter );
		Employee regionManager = eHandler.getRegionManager( uFilter );
		Employee districtManager = eHandler.getDistrictManager( uFilter );		
		((ListReportWpc)page).setAreaManager( areaManager );
		((ListReportWpc)page).setRegionManager( regionManager );
		((ListReportWpc)page).setDistrictManager( districtManager );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
		uSession.setOverallProcessor(null);	
		return new String("success");
		}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }
    
    ChartDetailWc getChart()
    {
        PieChartBuilder pBuilder = new PieChartBuilder();
        ChartBean[] thisChartBean;
        PieChart pieChart=null;        
		ServiceFactory factory = Service.getServiceFactory( trDb );
		
        EmployeeHandler eHandler = factory.getEmployeeHandler();
		
        WebPageComponent page;
        ChartDetailWc chart=null;
        
		// check if coming from email, if so reset search to 
		// clear all pieChartfilters in session.
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
        boolean fromFilter=false;
        
        SPFHandler spfHandler = factory.getSPFHandler(); 
        UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());        
        TerritoryFilterForm filterForm = uSession.getNewTerritoryFilterForm();
        List charts = new ArrayList();
        if("Detrol LA".equalsIgnoreCase(qStrings.getType())){
        thisChartBean=(ChartBean[])spfHandler.getFilteredChart("DETR",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("DETR");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Detrol LA",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Detrol LA" ) );
        chart.setLayout(ChartDetailWc.LAYOUT_ALT);
        
        }if("Chantix".equalsIgnoreCase(qStrings.getType())){
            thisChartBean=(ChartBean[])spfHandler.getFilteredChart("CHTX",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("CHTX");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Chantix",fromFilter, "/SPF/listreport");        
        chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Chantix" ) );
        chart.setLayout(ChartDetailWc.LAYOUT_ALT);
        }if("Revatio".equalsIgnoreCase(qStrings.getType())){
            thisChartBean=(ChartBean[])spfHandler.getFilteredChart("RVTO",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("RVTO");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Revatio",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Revatio" ) );
        chart.setLayout(ChartDetailWc.LAYOUT_ALT);
        }if("Spiriva".equalsIgnoreCase(qStrings.getType())){
            thisChartBean=(ChartBean[])spfHandler.getFilteredChart("SPRV",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("SPRV");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Spiriva",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT ,"Spiriva") );
        chart.setLayout(ChartDetailWc.LAYOUT_ALT);
        }if("Viagra".equalsIgnoreCase(qStrings.getType())){
         thisChartBean=(ChartBean[])spfHandler.getFilteredChart("VIAG",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("VIAG");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Viagra",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Viagra" ) );
        chart.setLayout(ChartDetailWc.LAYOUT_ALT);        
        }if("Overall".equalsIgnoreCase(qStrings.getType())){
        thisChartBean=(ChartBean[])spfHandler.getFilteredOverallChart("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("Overall");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Overall",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Overall" ) );
        chart.setLayout(ChartDetailWc.LAYOUT_ALT);        
        }                        
        return chart;        
    }
    
    List getAllCharts()
    {
        PieChartBuilder pBuilder = new PieChartBuilder();
        ChartBean[] thisChartBean;
        PieChart pieChart=null;        
		ServiceFactory factory = Service.getServiceFactory( trDb );
		
        EmployeeHandler eHandler = factory.getEmployeeHandler();
		
        WebPageComponent page;
        ChartDetailWc chart=null;
        
		// check if coming from email, if so reset search to 
		// clear all pieChartfilters in session.
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
        boolean fromFilter=false;
        
        SPFHandler spfHandler = factory.getSPFHandler(); 
        UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());        
        TerritoryFilterForm filterForm = uSession.getUserFilterForm();
        
        List charts = new ArrayList();
        
        thisChartBean=(ChartBean[])spfHandler.getFilteredChart("DETR",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Detrol LA",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Detrol LA", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Detrol LA" ) );
        if(pieChart.getCount()>0) charts.add(chart);
        
        
        thisChartBean=(ChartBean[])spfHandler.getFilteredChart("CHTX",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("CHTX");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Chantix",fromFilter, "/SPF/listreport");        
        chart = new ChartDetailWc(pieChart ,  "Chantix", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Chantix" ) );
        if(pieChart.getCount()>0) charts.add(chart);
        
            thisChartBean=(ChartBean[])spfHandler.getFilteredChart("RVTO",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("RVTO");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Revatio",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Revatio", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Revatio" ) );
        if(pieChart.getCount()>0) charts.add(chart);

            thisChartBean=(ChartBean[])spfHandler.getFilteredChart("SPRV",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("SPRV");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Spiriva",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Spiriva", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT ,"Spiriva") );
        if(pieChart.getCount()>0) charts.add(chart);

         thisChartBean=(ChartBean[])spfHandler.getFilteredChart("VIAG",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("VIAG");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Viagra",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Viagra", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Viagra" ) );
        if(pieChart.getCount()>0) charts.add(chart);

        thisChartBean=(ChartBean[])spfHandler.getFilteredOverallChart("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        uSession.getUserFilter().setProdcut("Overall");
        pieChart=pBuilder.getChart(uSession.getUserFilter(),getRequest().getSession(),thisChartBean,"Overall",fromFilter, "/SPF/listreport");
        chart = new ChartDetailWc(pieChart ,  "Overall", new ChartLegendWc( ChartLegendWc.LAYOUT_DEFAULT,"Overall" ) );
        if(pieChart.getCount()>0) charts.add(chart);

        return charts;        
    }
    

      
    public String SPFHSReportTrainingSchedule() {
    	try{
		UserSession uSession;        
		uSession = UserSession.getUserSession(getRequest());        
        AppQueryStrings qStrings = new AppQueryStrings();        
		User user;
		
        uSession = UserSession.getUserSession(getRequest());
		
		if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
			getRequest().getSession().invalidate();		
			//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
			getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
			//uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser( qStrings.getEmplid() );			
		} else {
			uSession = UserSession.getUserSession(getRequest()); 
			user = uSession.getUser();				
		}

        if ( getResponse().isCommitted() ) {
            return null;
        }
        
        String downloadExcel = getRequest().getParameter("downloadExcel");
        
        AdminReportHandler handler = new AdminReportHandler();          
        EmpReport[] empReport = handler.getTrainingSchedule(AppConst.EVENT_SPF);        
        TrainingScheduleWc main = new TrainingScheduleWc(empReport, AppConst.EVENT_SPF);                      
        if ("true".equalsIgnoreCase(downloadExcel)) {			
			WebPageComponent page;
            page = new BlankTemplateWpc(main);
            
            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
            getRequest().setAttribute("exceldownload","true"); 
            
            
            
            getResponse().addHeader("content-disposition","attachment;filename=\"SPF - Training Schedule Summary.xls\"");
			
			getResponse().setContentType("application/vnd.ms-excel");	
			getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
			getResponse().setHeader("Pragma","public");		
            
            
            
            return new String("successXls");
        }
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_SPF,"SPFREPORT" );  
        page.setMain(main);
        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
        return new String("success");
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }
    

    public String SPFHSReportTrainingScheduleEmplList() {
    	try{
		UserSession uSession;        
		uSession = UserSession.getUserSession(getRequest());        
        AppQueryStrings qStrings = new AppQueryStrings();        
		User user;
		
        uSession = UserSession.getUserSession(getRequest());
		
		if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
			getRequest().getSession().invalidate();		
			//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
			getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
			//uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser( qStrings.getEmplid() );			
		} else {
			uSession = UserSession.getUserSession(getRequest()); 
			user = uSession.getUser();				
		}
        
        if ( getResponse().isCommitted() ) {
            return null;
        }
        
        

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");             
        
        String productCd = getRequest().getParameter("productCd");
        String strStartDate = getRequest().getParameter("startDate");
        String strEndDate = getRequest().getParameter("endDate");
        String teamCd = getRequest().getParameter("teamCd");
        String downloadExcel = getRequest().getParameter("downloadExcel");        
        
        ClassFilterForm form = new ClassFilterForm();
        form.setProduct(getRequest().getParameter(form.FIELD_PRODUCT));
        form.setStartDate(getRequest().getParameter(form.FIELD_STARTDATE));
        form.setEndDate(getRequest().getParameter(form.FIELD_ENDDATE));
        form.setTeamCd(getRequest().getParameter(form.FIELD_TEAMCD));
        form.setEnrollmentDate(getRequest().getParameter(form.FIELD_ENROLLMENTDATE));
                
        AdminReportHandler handler = new AdminReportHandler();          
        EmpReport[] empReport = null; 
        if(form.getEnrollmentDate()==null||form.getEnrollmentDate().equals("")){
            empReport = handler.getTrainingScheduleEmplList(AppConst.EVENT_SPF, form);    
        }else{
            empReport = handler.getEnrollmentEmplList(AppConst.EVENT_SPF,form);
        }                
        TrainingScheduleEmplListWc main = new TrainingScheduleEmplListWc(form, empReport, AppConst.EVENT_SPF);                                                  
        
        if ("true".equalsIgnoreCase(downloadExcel)) {			
            WebPageComponent page;
            page = new BlankTemplateWpc(main);
            
            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
            getRequest().setAttribute("exceldownload","true"); 
            
            getResponse().addHeader("content-disposition","attachment;filename=\"SPF - Training Schedule Detail.xls\"");
			
			getResponse().setContentType("application/vnd.ms-excel");	
			getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
			getResponse().setHeader("Pragma","public");		
			
            return new String("successXls");
        }
        
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_SPF,"SPFREPORT" );                
        page.setMain(main);
        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		        
        return new String("success");
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }
    
    
    public String SPFHSReportVariance()
    {
    	try{
		UserSession uSession;        
		uSession = UserSession.getUserSession(getRequest());        
        AppQueryStrings qStrings = new AppQueryStrings();        
		User user;
        AdminReportHandler handler = new AdminReportHandler();          
        VarianceReportBean[] varianceReportBean = handler.getVarianceReport(AppConst.EVENT_SPF);
        
        uSession = UserSession.getUserSession(getRequest());
		
		if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
			getRequest().getSession().invalidate();		
			//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
			getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
			//uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser( qStrings.getEmplid() );
		} else {
			uSession = UserSession.getUserSession(getRequest()); 
			user = uSession.getUser();	
		}


		if ( getResponse().isCommitted() ) {
			return null;
		}
       
       UserFilter filter = uSession.getUserFilter();
       VarianceReportListWc varianceReport = new VarianceReportListWc(filter, varianceReportBean, user, AppConst.EVENT_SPF);
       
       String sDownloadToExcel = getRequest().getParameter("downloadExcel");        
        
        if (sDownloadToExcel != null && sDownloadToExcel.equalsIgnoreCase("true")) {            
            WebPageComponent page;             
            page = new BlankTemplateWpc(varianceReport);
            getResponse().setContentType("application/vnd.ms-excel;charset=ISO-8859-2"); 
            getRequest().setAttribute("exceldownload","true"); 
            getResponse().setHeader ("Content-Disposition","attachment; filename=\"SPF - Variance Report.xls\"");             
            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
            return new String("successXls");
        }
        else {
            MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_SPF,"SPFREPORT" );                
            page.setMain(varianceReport);	            
            getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		            
            return new String("success");           
        }       
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }

    
   
    public String getFilteredChart(){
        try{
        UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());
        TerritoryFilterForm filterForm = uSession.getNewTerritoryFilterForm();
        FormUtil.loadObject(getRequest(),filterForm);    
        ServiceFactory factory = Service.getServiceFactory( trDb );
		SPFHandler spfHandler = factory.getSPFHandler();
        List charts = new ArrayList();
        AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","SPF" );
        filterForm = uSession.getUserFilterForm();
		FormUtil.loadObject(getRequest(),filterForm);
		
        
        UserFilter uFilter = uSession.getUserFilter();
        
        //New Code Under Test
        String teamDesc="All";
        //System.out.println("filterForm Area"+filterForm.getArea());
        //System.out.println("filterForm Region"+filterForm.getRegion());
        //System.out.println("filterForm District"+filterForm.getDistrict());
        //System.out.println("filterForm District"+filterForm.getTeam());
        //System.out.println("filterForm District"+filterForm.getTeamList().size());                        
            if(filterForm.getTeamList().size()<=0){
            TeamBean[] allTeam=null;
            allTeam=trDb.getAllPDFHSTEAM();
            LabelValueBean labelValueBean ;
            filterForm.setTeam("All");
            
            FormUtil.loadObject(getRequest(),filterForm);
            labelValueBean=new LabelValueBean("All","All");
            filterForm.setTeamList(labelValueBean);
            for(int i=0;i<allTeam.length;i++){
              labelValueBean=new LabelValueBean(allTeam[i].getTeamDesc(),allTeam[i].getTeamCd());
              filterForm.setTeamList(labelValueBean);
            }        
        }else{
            //We need to get the  team description 
            teamDesc=trDb.getTeamDescription(filterForm.getTeam());            
        }        
        
        //End New Code Under Test
        
           //Total Candidates Participating:
        int totalParticipants=spfHandler.getOverAllTotalCount("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        //Get Team Short Description for Header
        
        //Removed 
        //String teamDesc="";
        
        if(filterForm.getTeam().equalsIgnoreCase("All"))teamDesc="All";else teamDesc=trDb.getTeamDescription(filterForm.getTeam());
        Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
        // this component puts the header info in the chart page.
		
		ChartHeaderWc chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc);
        
        
        
        SPFChartsWc chartpage = new SPFChartsWc( filterForm, uSession.getUser(),chartHeaderWc ) ;
        PieChartBuilder pBuilder = new PieChartBuilder();
        ChartDetailWc chartdetail=null;
        ChartBean[] thiChartBean;
        PieChart pieChart=null;
        uSession.getUserFilter().getQuseryStrings().setSection(null);
        boolean fromFilter=false;
        //Get The Count For the Aricept
        
        charts = getAllCharts();
        
        
        
        
        ChartListWc chartListWc = new ChartListWc(charts);
        //Add the Overall Trainees;
        chartListWc.setTrainees(pieChart.getCount());
        chartpage.setWebComponent( chartListWc );
		page.setMain( chartpage );	
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
        return new String("success");
        }
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }
 
   
    public String beginPLC()
    {   
        try{
        if ( getResponse().isCommitted() ) {
			return null;
		}        
        		
        AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
		UserSession uSession = UserSession.getUserSession(getRequest());
        //**************************
        getSession().setAttribute("ReportType","SPFPLC");
         boolean fromFilter=false;
         
        PieChart pieChart;        
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","SPFPLC" );
        
        TerritoryFilterForm filterForm = uSession.getUserFilterForm();
        String teamDesc="All";
        ServiceFactory factory = Service.getServiceFactory( trDb );
        PLCHandler plcHandler = factory.getPLCHandler();        
        UserFilter uFilter = uSession.getUserFilter();
        if(filterForm.getTeamList().size()<=0){
            TeamBean[] allTeam=null;
            //Load data into Listbox;
            allTeam=trDb.getAllSPFTEAM();
            LabelValueBean labelValueBean ;
            filterForm.setTeam("All");
             
            FormUtil.loadObject(getRequest(),filterForm);
            labelValueBean=new LabelValueBean("All","All");
            filterForm.setTeamList(labelValueBean);
            for(int i=0;i<allTeam.length;i++){
              labelValueBean=new LabelValueBean(allTeam[i].getTeamDesc(),allTeam[i].getTeamCd());
              filterForm.setTeamList(labelValueBean);
            }         
        }else{
            //We need to get the  team description 
            teamDesc=trDb.getTeamDescription(filterForm.getTeam());            
        }
        //Total Candidates Participating:
        int totalParticipants=plcHandler.getOverAllTotalCountSPF("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
        Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
        
        // this component puts the header info in the chart page.		
		ChartHeaderWc chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc);        
    
		PlcChartsWc chartpage = new PlcChartsWc( filterForm, uSession.getUser() ,chartHeaderWc) ;
        
        List charts = new ArrayList();
        
        PieChartBuilder pBuilder = new PieChartBuilder();
        
        qStrings.setSection("");        
        POAChartBean[] chartBean;  
        //POAChartBean[] thisPOAChartBean;
        ChartDetailWc chartdetail=null;
        uSession.getUserFilter().getQuseryStrings().setSection(null);
                
        //Get The Count For the Detrol               
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("DETR",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Detrol LA",fromFilter);
        chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Detrol LA" ) );
        if(pieChart.getCount()>0)  charts.add(chartdetail);
                        
        //Get The Count For the Chantix       
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("CHTX",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Chantix",fromFilter);
        chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Chantix" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
                           
        //Get The Count For the Revatio
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("RVTO",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Revatio",fromFilter);
        chartdetail = new ChartDetailWc(pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Revatio" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
                         
        //Get The Count For the Spiriva
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("SPRV",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Spiriva",fromFilter);
        chartdetail = new ChartDetailWc(pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Spiriva" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
                                
        //Get The Count For the Viagra
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("VIAG",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Viagra",fromFilter);
        chartdetail = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Viagra" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);

        //Get The Count For the General Session 
        chartBean=(POAChartBean[])plcHandler.getFilteredChartSPF("PLCA",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"General Session",fromFilter);
        chartdetail = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"General Session" ) );        
        if(pieChart.getCount()>0) charts.add(chartdetail);
        
        //Get The Count For the Overall 
        chartBean=(POAChartBean[])plcHandler.getFilteredPLCOverallChartSPF("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Overall",fromFilter);
        chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Overall" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
         
        ChartListWc chartListWc = new ChartListWc(charts);
        //Add the Overall Trainees;
        chartListWc.setTrainees(pieChart.getCount());
        chartpage.setWebComponent( chartListWc );
		page.setMain( chartpage );	
        
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
        return new String("success");  
        }
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    } 
    
    public String listReportPLC() {
		try{
		if ( getResponse().isCommitted() ) {
			return null;
		}
        
        Employee[] employees=null;
        Employee[] excelPoaEmployee=null;

		ServiceFactory factory = Service.getServiceFactory( trDb );
		EmployeeHandler eHandler = factory.getEmployeeHandler();
		WebPageComponent page;

		// check if coming from email, if so reset search to 
		// clear all filters in session.
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
        UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());
        TerritoryFilterForm filterForm = uSession.getUserFilterForm();
        FormUtil.loadObject(getRequest(),filterForm);    
        if(filterForm.getTeam().trim().equalsIgnoreCase(""))	filterForm.setTeam("All");
        //System.out.println("TEAM IN JPF is"+filterForm.getTeam());
		UserFilter uFilter = uSession.getUserFilter();
        uFilter.setQueryStrings(qStrings);
		uFilter.setAdmin(uSession.getUser().isAdmin());
        //System.out.println("Product is "+qStrings.getType());
        
        PieChart pieChart=null;

		OverallProcessor overall = uSession.getOverallProcessor();
        
        PieChartBuilder pBuilder = new PieChartBuilder();
        POAChartBean[] thisPOAChartBean;
        boolean fromFilter=false;
    	PLCHandler plcHandler = factory.getPLCHandler(); 
         ChartDetailWc chart=null;
        //Action for Excel Request
		if ("true".equals( uFilter.getQuseryStrings().getDownloadExcel())) {                      
            if("Overall".equalsIgnoreCase(qStrings.getType())){ 
                excelPoaEmployee =eHandler.getPLCOverAllEmployeesSPF(uFilter,qStrings.getType(),qStrings.getSection());       
            }else{
                excelPoaEmployee =eHandler.getPLCEmployeesSPF(uFilter,qStrings.getType(),qStrings.getSection());       
            }
            getSession().setAttribute("xlsBean",excelPoaEmployee);
            getSession().setAttribute("xlsType",qStrings.getType());
            getSession().setAttribute("section",qStrings.getSection());
		    return new String("successXls");			
		} 

        //Action for Detail Page
        //Render Chart
        if("Detrol LA".equalsIgnoreCase(qStrings.getType())){ 
            thisPOAChartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("DETR",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
            uSession.getUserFilter().setProdcut("DETR");
            pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Detrol LA",fromFilter);
            chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc(ChartLegendWc.LAYOUT_SPFPLC,"Detrol LA"));
            chart.setLayout(ChartDetailWc.LAYOUT_ALT);        
        }else if("Chantix".equalsIgnoreCase(qStrings.getType())){
            thisPOAChartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("CHTX",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
            uSession.getUserFilter().setProdcut("CHTX");
            pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Chantix",fromFilter);        
            chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Chantix" ) );
            chart.setLayout(ChartDetailWc.LAYOUT_ALT);
        }else if("Revatio".equalsIgnoreCase(qStrings.getType())){
            thisPOAChartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("RVTO",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
            uSession.getUserFilter().setProdcut("RVTO");
            pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Revatio",fromFilter);
            chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Revatio" ) );
            chart.setLayout(ChartDetailWc.LAYOUT_ALT);
        }else if("Spiriva".equalsIgnoreCase(qStrings.getType())){
            thisPOAChartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("SPRV",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
            uSession.getUserFilter().setProdcut("SPRV");
            pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Spiriva",fromFilter);
            chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC ,"Spiriva") );
            chart.setLayout(ChartDetailWc.LAYOUT_ALT);
        }else if("Viagra".equalsIgnoreCase(qStrings.getType())){
            thisPOAChartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("VIAG",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
            uSession.getUserFilter().setProdcut("VIAG");
            pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Viagra",fromFilter);
            chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Viagra" ) );
            chart.setLayout(ChartDetailWc.LAYOUT_ALT);            
        }else if("General Session".equalsIgnoreCase(qStrings.getType())){
            thisPOAChartBean=(POAChartBean[])plcHandler.getFilteredChartSPF("PLCA",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
            uSession.getUserFilter().setProdcut("PLCA");
            pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"General Session",fromFilter);
            chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"General Session" ) );
            chart.setLayout(ChartDetailWc.LAYOUT_ALT);            
        }else if("Overall".equalsIgnoreCase(qStrings.getType())){
            thisPOAChartBean=(POAChartBean[])plcHandler.getFilteredPLCOverallChartSPF("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
            uSession.getUserFilter().setProdcut("Overall");
            pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Overall",fromFilter);
            chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Overall" ) );
            chart.setLayout(ChartDetailWc.LAYOUT_ALT);        
        }                					             
       if("Overall".equalsIgnoreCase(qStrings.getType())){
            employees =eHandler.getPLCOverAllEmployeesSPF(uFilter,qStrings.getType(),qStrings.getSection());       
       }else{
            employees =eHandler.getPLCEmployeesSPF(uFilter,qStrings.getType(),qStrings.getSection());       
       }
       if(pieChart.getCount()==0) chart=null;
	   page = new ListReportWpc( uSession.getUser(), uFilter, chart,employees,"SPFPLC");		
	   uFilter.setClusterCode( uSession.getUser().getCluster() );
		Employee areaManager = eHandler.getAreaManager( uFilter );
		Employee regionManager = eHandler.getRegionManager( uFilter );
		Employee districtManager = eHandler.getDistrictManager( uFilter );		
		((ListReportWpc)page).setAreaManager( areaManager );
		((ListReportWpc)page).setRegionManager( regionManager );
		((ListReportWpc)page).setDistrictManager( districtManager );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
		uSession.setOverallProcessor(null);		
		return new String("success");	
		}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }    
    
  
    public String getFilteredChartPLC(){
         try{
        UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());
        TerritoryFilterForm filterForm = uSession.getNewTerritoryFilterForm();
        FormUtil.loadObject(getRequest(),filterForm);    
        ServiceFactory factory = Service.getServiceFactory( trDb );
		//POAHandler poaHandler = factory.getPOAHandler();        
        PLCHandler plcHandler = factory.getPLCHandler();        
        List charts = new ArrayList();
        AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","SPFPLC" );
        filterForm = uSession.getUserFilterForm(); 
		FormUtil.loadObject(getRequest(),filterForm);
		 
        
        UserFilter uFilter = uSession.getUserFilter();
           //Total Candidates Participating:
        int totalParticipants=plcHandler.getOverAllTotalCountSPF("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        //Get Team Short Description for Header
        String teamDesc="";
        if(filterForm.getTeam().equalsIgnoreCase("All"))teamDesc="All";else teamDesc=trDb.getTeamDescription(filterForm.getTeam());
        Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
        // this component puts the header info in the chart page.
		
		ChartHeaderWc chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc);
                        
        PlcChartsWc chartpage = new PlcChartsWc( filterForm, uSession.getUser(),chartHeaderWc ) ;
        PieChartBuilder pBuilder = new PieChartBuilder();
        ChartDetailWc chartdetail=null;
        POAChartBean[] chartBean;
        PieChart pieChart=null;
        uSession.getUserFilter().getQuseryStrings().setSection(null);
        boolean fromFilter=false;

        //Get The Count For the Aricept
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("DETR",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Detrol LA",fromFilter);
        chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Detrol LA" ) );
        if(pieChart.getCount()>0)  charts.add(chartdetail);
                
        //Get The Count For the Celebrex
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("CHTX",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Chantix",fromFilter);
        chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Chantix" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
                           
        //Get The Count For the Geodon//thisPOAChartBean=trDb.getPOAChartsForAll("GEOD");
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("RVTO",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Revatio",fromFilter);
        chartdetail = new ChartDetailWc(pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Revatio" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
                        
        //Get The Count For the Lyrica
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("SPRV",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Spiriva",fromFilter);
        chartdetail = new ChartDetailWc(pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Spiriva" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
        
                        
        //Get The Count For the Rebif
        chartBean=(POAChartBean[])plcHandler.getFilteredChartWithExamTypeSPF("VIAG",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Viagra",fromFilter);
        chartdetail = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Viagra" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
        
        //Get The Count For the PLCA
        chartBean=(POAChartBean[])plcHandler.getFilteredChartSPF("PLCA",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"General Session",fromFilter);
        chartdetail = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"General Session" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
        
        //Get The Count For the Overall 
        chartBean=(POAChartBean[])plcHandler.getFilteredPLCOverallChartSPF("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
        //thisPOAChartBean=trDb.getPOAOverallChart();
        pieChart=pBuilder.getPlcChartSPF(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Overall",fromFilter);
        chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_SPFPLC,"Overall" ) );
        if(pieChart.getCount()>0) charts.add(chartdetail);
        
        ChartListWc chartListWc = new ChartListWc(charts);
        //Add the Overall Trainees;
        chartListWc.setTrainees(pieChart.getCount());
        chartpage.setWebComponent( chartListWc );
		page.setMain( chartpage );	
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
        return charts();
         }
     	catch (Exception e) {
     	Global.getError(getRequest(),e);
     	return new String("failure");
     	}

    }

   
    public String charts()
    {
        try{
        if ( getResponse().isCommitted() ) {
			return null;
		}
        UserSession uSession = buildUserSession();
        UserFilter uFilter = uSession.getUserFilter();
		uFilter.setAdmin(uSession.getUser().isAdmin());
		PieChartBuilder pBuilder = new PieChartBuilder();

		OverallProcessor overall = uSession.getOverallProcessor();
        return new String("success");
        }
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

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
			
			overall = rBuilder.getOverallProcessor( uFilter );
			
			UserSession.getUserSession( getRequest() ).setOverallProcessor( overall );
			
		}
		
		return UserSession.getUserSession( getRequest() );
	}	
 
    public String SPFHSReportPersonalAgenda() {
    	try{
        UserSession uSession;        
		uSession = UserSession.getUserSession(getRequest());        
        AppQueryStrings qStrings = new AppQueryStrings();        
		User user;
		
        uSession = UserSession.getUserSession(getRequest());
		
		if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
			getRequest().getSession().invalidate();		
			//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
			getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
			//uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser( qStrings.getEmplid() );			
		} else {
			uSession = UserSession.getUserSession(getRequest()); 
			user = uSession.getUser();				
		}
        
        if ( getResponse().isCommitted() ) {
            return null;
        }
        
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");             
        
        String downloadExcel = getRequest().getParameter("downloadExcel");
        
        AdminReportHandler handler = new AdminReportHandler();          
        EmpReport[] empReport = handler.getPersonalizedAgendaReport(AppConst.EVENT_SPF);
        PersonalAgendaWc main = new PersonalAgendaWc(empReport, AppConst.EVENT_SPF);                              
        if ("true".equalsIgnoreCase(downloadExcel)) {
			WebPageComponent page;
            page = new BlankTemplateWpc(main);
            
            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
            getRequest().setAttribute("exceldownload","true"); 
            
            getResponse().addHeader("content-disposition","attachment;filename=\"SPF - Personalized Agenda.xls\"");
			
			getResponse().setContentType("application/vnd.ms-excel");	
			getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
			getResponse().setHeader("Pragma","public");		
			
            return new String("successXls");
        }        
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_SPF,"SPFREPORT" );                
        page.setMain(main);
        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		        
        return new String("success");
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }	
    
   
    public String SPFHSReportClassRoster() {   
    	try{
        UserSession uSession;        
		uSession = UserSession.getUserSession(getRequest());        
        AppQueryStrings qStrings = new AppQueryStrings();        
		User user;
		
        uSession = UserSession.getUserSession(getRequest());
		
		if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
			getRequest().getSession().invalidate();		
			//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
			getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
			//uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser( qStrings.getEmplid() );			
		} else {
			uSession = UserSession.getUserSession(getRequest()); 
			user = uSession.getUser();				
		}
        
        if ( getResponse().isCommitted() ) {
            return null;
        }
        
        String downloadExcel = getRequest().getParameter("downloadExcel");
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");             
        ClassFilterForm form = new ClassFilterForm();

        if (!"true".equalsIgnoreCase(downloadExcel)) {
            FormUtil.loadObject(getRequest(),form);		
        }
        else {
            form.setClassroom(getRequest().getParameter(form.FIELD_CLASSROOM));
            form.setTrainingDate(getRequest().getParameter(form.FIELD_TRAININGDATE));
            form.setProduct(getRequest().getParameter(form.FIELD_PRODUCT));
        }
        

        if (form.getTrainingDate() == null || "".equals(form.getTrainingDate())) {
            form.setTrainingDate("All");            
        }
        if (form.getClassRoom() == null || "".equals(form.getClassRoom())) {
            form.setClassroom("All");
        }
        if (form.getProduct() == null || "".equals(form.getProduct())) {
            form.setProduct("All");
        }
        
        AdminReportHandler handler = new AdminReportHandler();          
        EmpReport[] empReport = handler.getClassRosterReport(AppConst.EVENT_SPF, form);
        
        ClassRosterBean[] classData =  handler.getClassData(AppConst.EVENT_SPF, null);
        ClassRosterWc main = new ClassRosterWc(form, classData, empReport, AppConst.EVENT_SPF);                              
        if ("true".equalsIgnoreCase(downloadExcel)) {
			WebPageComponent page;
            page = new BlankTemplateWpc(main);
            
            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
            getRequest().setAttribute("exceldownload","true"); 
            
            getResponse().addHeader("content-disposition","attachment;filename=\"SPF - Class Roster.xls\"");
			
			getResponse().setContentType("application/vnd.ms-excel");	
			getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
			getResponse().setHeader("Pragma","public");		
			
            return new String("successXls");
        }        
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_SPF,"SPFREPORT" );                
        page.setMain(main);
        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		        
        return new String("success");
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }
    
    
    
    public String SPFHSReportGeneralSessionAttendance() { 
    	try{
        UserSession uSession;        
		uSession = UserSession.getUserSession(getRequest());        
        AppQueryStrings qStrings = new AppQueryStrings();        
		User user;
		
        uSession = UserSession.getUserSession(getRequest());
		
		if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
			getRequest().getSession().invalidate();		
			//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
			getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
			//uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser( qStrings.getEmplid() );			
		} else {
			uSession = UserSession.getUserSession(getRequest()); 
			user = uSession.getUser();				
		}
        
        if ( getResponse().isCommitted() ) {
            return null;
        }
        
        String downloadExcel = getRequest().getParameter("downloadExcel");
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");             
        ClassFilterForm form = new ClassFilterForm();

        if (!"true".equalsIgnoreCase(downloadExcel)) {
            FormUtil.loadObject(getRequest(),form);		
        }
        else {
            form.setClassroom(getRequest().getParameter(form.FIELD_CLASSROOM));
            form.setTrainingDate(getRequest().getParameter(form.FIELD_TRAININGDATE));            
        }
        

        if (form.getTrainingDate() == null || "".equals(form.getTrainingDate())) {
            form.setTrainingDate("All");            
        }
        if (form.getClassRoom() == null || "".equals(form.getClassRoom())) {
            form.setClassroom("All");
        }
        
        // HardCoded For General Session
        form.setProduct("PLCA");
        
        AdminReportHandler handler = new AdminReportHandler();          
        EmpReport[] empReport = handler.getAttendanceReport(AppConst.EVENT_SPF, form);
        
        ClassRosterBean[] classData =  handler.getClassData(AppConst.EVENT_SPF, "PLCA");
        GeneralSessionWc main = new GeneralSessionWc(form, classData, empReport, AppConst.EVENT_SPF);                              
        if ("true".equalsIgnoreCase(downloadExcel)) {
			WebPageComponent page;
            page = new BlankTemplateWpc(main);
            
            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
            getRequest().setAttribute("exceldownload","true"); 
            
            getResponse().addHeader("content-disposition","attachment;filename=\"SPF - General Session Attendance.xls\"");
			
			getResponse().setContentType("application/vnd.ms-excel");	
			getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
			getResponse().setHeader("Pragma","public");		
			
            return new String("successXls");
        }        
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_SPF,"SPFREPORT" );                
        page.setMain(main);
        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		        
        return new String("success");
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }
    
      
    public String generalSessionAttendance()
    {   try{
        AdminReportHandler handler = new AdminReportHandler();   
        GeneralSessionEmployee[] employees = handler.getGeneralSessionEmployees(AppConst.EVENT_SPF);
        getRequest().setAttribute(GeneralSessionEmployee.ATTRIBUTE_NAME, employees);
        return new String("success");
        }
		catch (Exception e) {
		Global.getError(getRequest(),e);
		return new String("failure");
	}

    }
    
   
    public String updateGeneralSession() throws NamingException
    {
    	try{
		UserSession uSession;
        String sUser = null;
		
        uSession = UserSession.getUserSession(getRequest());
        if(uSession != null)
        {
            User user = uSession.getUser(); 
            if(user != null)
              sUser = user.getEmplid();
        }              
        
        if(sUser == null)
            sUser = "-2";
        
        AdminReportHandler handler = new AdminReportHandler();          
                
        String sEmplIDs = (String)getRequest().getParameter("emplids");
        String sCheckedStatusList = (String)getRequest().getParameter("checkedstatus");
        String sPreCheckedStatusList = (String)getRequest().getParameter("precheckedstatus");
        
        //This is list of EmpID's new checked list on General Session Entry Screen
        HashMap hashBeforeEntry = Util.hashMapFromcommaDelimitedStrings(sEmplIDs, sPreCheckedStatusList); 
        HashMap hashAfterEntry = Util.hashMapFromcommaDelimitedStrings(sEmplIDs, sCheckedStatusList);                
        HashMap hashEntryDiff = Util.hashMapDiff(hashBeforeEntry, hashAfterEntry);

        handler.batchUpdateAttendance(AppConst.EVENT_SPF, hashEntryDiff, sUser);
        
        GeneralSessionEmployee[] employees = handler.getGeneralSessionEmployees(AppConst.EVENT_SPF);
        getRequest().setAttribute(GeneralSessionEmployee.ATTRIBUTE_NAME, employees);
        
        if(hashEntryDiff !=null && hashEntryDiff.size() > 0)
          getRequest().setAttribute("status", "Attendance status updated for " + hashEntryDiff.size() + " employee(s)");
        
        return new String("success");
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }   
   
    public String adminToolsSelect(){
		try{
		callSecurePage();
		
		if ( getResponse().isCommitted() ) {
			return null;
		}
		
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
		UserSession uSession;
		User user;
		uSession = UserSession.getUserSession(getRequest());        
        getSession().setAttribute("ReportType","SPF");        
		
		if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
			getRequest().getSession().invalidate();		
			//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
			getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
			//uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser( qStrings.getEmplid() );			
		} else {
			uSession = UserSession.getUserSession(getRequest()); 
			user = uSession.getUser();				
		}
        		
        MainTemplateWpc page = new MainTemplateWpc( user, AppConst.EVENT_SPF,"SPFTOOLS" );                
        
		
		page.setMain( new admintoolsSelectSPFHSWc( user ) );	
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
		page.setLoginRequired(true);
        return new String("index");
		}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }



    public String SPFEnrollmentSummaryReport() { 
    	try{
		UserSession uSession;        
		uSession = UserSession.getUserSession(getRequest());        
        AppQueryStrings qStrings = new AppQueryStrings();        
		User user;
		
        uSession = UserSession.getUserSession(getRequest());
		
		if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
			getRequest().getSession().invalidate();		
			//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
			getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
			//uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser( qStrings.getEmplid() );			
		} else {
			uSession = UserSession.getUserSession(getRequest()); 
			user = uSession.getUser();				
		}

        if ( getResponse().isCommitted() ) {
            return null; 
        }
        
        String downloadExcel = getRequest().getParameter("downloadExcel");
        
        AdminReportHandler handler = new AdminReportHandler();          
        EmpReport[] empReport = handler.getEnrollmentSummaryReport(AppConst.EVENT_SPF); 
        EmpReport empReportTotal = handler.getEnrollmentSummaryReportTotal(AppConst.EVENT_SPF);        
        EnrollmentSummaryReportWc main = new EnrollmentSummaryReportWc(empReport,empReportTotal,AppConst.EVENT_SPF);                      
        
        if ("true".equalsIgnoreCase(downloadExcel)) {			
			WebPageComponent page;
            page = new BlankTemplateWpc(main);
            
            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
            getRequest().setAttribute("exceldownload","true"); 
                                    
            getResponse().addHeader("content-disposition","attachment;filename=\"PDF - Training Schedule Summary.xls\"");
			
			getResponse().setContentType("application/vnd.ms-excel");	
			getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
			getResponse().setHeader("Pragma","public");		
            
             
            
            return new String("successXls");
        }
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_SPF,"SPFREPORT" );  
        page.setMain(main);
        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
        return new String("success");    
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }

       /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     * @jpf:forward name="successXls" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
     */
    public String SPFEnrollmentChangeReport() {
    	try{
        //Winny
		UserSession uSession;        
		uSession = UserSession.getUserSession(getRequest());        
        AppQueryStrings qStrings = new AppQueryStrings();        
		User user;
		
        uSession = UserSession.getUserSession(getRequest());
		
		if ( !Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin())  {			
			getRequest().getSession().invalidate();		
			//getRequest().getSession().removeAttribute( UserSession.ATTRIBUTE );
			getRequest().getSession(true).setAttribute( UserSession.ATTRIBUTE, uSession);	
			//uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser( qStrings.getEmplid() );			
		} else {
			uSession = UserSession.getUserSession(getRequest()); 
			user = uSession.getUser();				
		}

        if ( getResponse().isCommitted() ) {
            return null;
        }
        
        String downloadExcel = getRequest().getParameter("downloadExcel");
        
        AdminReportHandler handler = new AdminReportHandler();          
        EnrollChangeReport[] empReport = handler.getEnrollmentChangeReport(AppConst.EVENT_SPF);
        EmpReport empReportTotal = handler.getEnrollmentSummaryReportTotal(AppConst.EVENT_SPF);        
        EnrollmentChangeReportWc main = new EnrollmentChangeReportWc(empReport, AppConst.EVENT_SPF);                      
                
        if ("true".equalsIgnoreCase(downloadExcel)) {			
			WebPageComponent page;
            page = new BlankTemplateWpc(main);
            
            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
            getRequest().setAttribute("exceldownload","true"); 
                                    
            getResponse().addHeader("content-disposition","attachment;filename=\"PDF - Enrollment Change Report.xls\"");
			
			getResponse().setContentType("application/vnd.ms-excel");	
			getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
			getResponse().setHeader("Pragma","public");		
            
            
            
            return new String("successXls");
        }
        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_PDF,"SPFREPORT" );  
        page.setMain(main);
        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
        return new String("success");        
    	}
    	catch (Exception e) {
    	Global.getError(getRequest(),e);
    	return new String("failure");
    	}

    }

	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response=response;
	}
        
}
