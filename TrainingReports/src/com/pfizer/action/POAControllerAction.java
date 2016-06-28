package com.pfizer.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.db.Employee;
import com.pfizer.db.TeamBean;
import com.pfizer.db.Territory;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.POAHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
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
import com.pfizer.webapp.wc.POA.POAChartBean;
import com.pfizer.webapp.wc.POA.PoaChartsWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Timer;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.wc.WebPageComponent;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc; 


public class POAControllerAction  extends ActionSupport implements ServletRequestAware,ServletResponseAware{

	
	TransactionDB trDb= new TransactionDB();
	protected static final Log log = LogFactory.getLog(AdminAction.class );
	
	private HttpServletRequest request;
	private HttpServletResponse response;
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
		return request.getSession();
	}

	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	
	 public String begin()
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
	        getSession().setAttribute("ReportType","PowersPOA");
	         boolean fromFilter=false;
	         
	                PieChart pieChart;
	    
	        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","POA" );
	        
	        TerritoryFilterForm filterForm = uSession.getUserFilterForm();
	        String teamDesc="All";
	        System.out.println("filterForm Area"+filterForm.getArea());
	        System.out.println("filterForm Region"+filterForm.getRegion());
	        System.out.println("filterForm District"+filterForm.getDistrict());
	        System.out.println("filterForm District"+filterForm.getTeam());
	        System.out.println("filterForm District"+filterForm.getTeamList().size());
	        ServiceFactory factory = Service.getServiceFactory( trDb );
	        POAHandler poaHandler = factory.getPOAHandler();        
	        UserFilter uFilter = uSession.getUserFilter();
	            if(filterForm.getTeamList().size()<=0){
	            TeamBean[] allTeam=null;
	            allTeam=trDb.getAllPOATEAM();
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
	        int totalParticipants=poaHandler.getOverAllTotalCount("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
	        System.out.println("THE TOTAL PARTICIPANT HERE IS"+totalParticipants);
	        Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
	        // this component puts the header info in the chart page.
			
			ChartHeaderWc chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc);
	        
	        
	        
	        
			PoaChartsWc chartpage = new PoaChartsWc(filterForm, uSession.getUser(), chartHeaderWc);
	        
	        List charts = new ArrayList();
	        
	        PieChartBuilder pBuilder = new PieChartBuilder();
	        
	        qStrings.setSection("");
	        POAChartBean[] thisPOAChartBean;
	        ChartDetailWc chartdetail=null;
	        uSession.getUserFilter().getQuseryStrings().setSection(null);
	        
	        //Get The Count For the Aricept
	        
	        
	        //Get The Count For the Celebrex
	        thisPOAChartBean=(POAChartBean[])poaHandler.getFilteredPOAChart("MIDPOA1",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
	       // thisPOAChartBean=trDb.getPOAChartsForAll("CLBR");
	       pieChart=pBuilder.getPoaChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"POWERS Mid-POA1 Breeze",fromFilter);
	        chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_POA,"POWERS Mid-POA1 Breeze" ) );
	        
	        
	        if(pieChart.getCount()>0) charts.add(chartdetail);
	       
	        
	        ChartListWc chartListWc = new ChartListWc(charts);
	        //Add the Overall Trainees;
	        chartListWc.setTrainees(pieChart.getCount());
	        chartpage.setWebComponent(chartListWc);
	        page.setMain( chartpage );	
	        
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
	        return charts();
		    }
		    catch (Exception e) {
		    	Global.getError(getRequest(),e);
		    	return new String("failure");
		    	}

	    }
	    
	 
		private ChartListWc poaChartList( PieChartBuilder pBuilder, UserFilter filter, User user ) {
			List ret = new ArrayList();
			PieChart chart;

			ServiceFactory factory = Service.getServiceFactory( trDb ); 
							
			// Add all the chartDeatilWc objects to a list				
			List charts = new ArrayList();
			
			// This WebComponent renders a single instance of a chart.				
			ChartDetailWc chartDetailWc = null;
			
			// attendance chart	
			/**
				chart = pBuilder.getPoaChart(filter, getRequest().getSession() );
				chartDetailWc = new ChartDetailWc( chart, "Attendance chart" ,new ChartLegendWc( ChartLegendWc.LAYOUT_ATTENDANCE ) );
				charts.add( chartDetailWc );			
			
	    
				chart = pBuilder.getPoaChart( filter, getRequest().getSession());
				chartDetailWc = new ChartDetailWc( chart, "Sales Call Evaluation Chart" ,new ChartLegendWc( ChartLegendWc.LAYOUT_SCE ) );
				charts.add( chartDetailWc );			
			   **/         
			
			
			// Add all chart detail object to the ChartListWc object, this component
			// renders all the chart detail objects.		
			ChartListWc chartListWc = new ChartListWc(charts);
			return chartListWc;
		}
	    
	    
		private void callSecurePage() {
			SuperWebPageComponents tpage = new BlankTemplateWpc();
			tpage.setLoginRequired(true);
			IAMUserControl upControl = new IAMUserControl();
			upControl.checkAuth(getRequest(),getResponse(),tpage);
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

			//OverallProcessor overall = uSession.getOverallProcessor();
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
	    
	    

	   
	    public String getFilteredChart(){
	        try{
	        UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
	        getSession().setAttribute("ReportType","PowersPOA");
	        TerritoryFilterForm filterForm = uSession.getNewTerritoryFilterForm();
	        FormUtil.loadObject(getRequest(),filterForm);    
	        ServiceFactory factory = Service.getServiceFactory( trDb );
			POAHandler poaHandler = factory.getPOAHandler();        
	        List charts = new ArrayList();
	        AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(),qStrings);
	        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","POA" );
	        filterForm = uSession.getUserFilterForm();
			FormUtil.loadObject(getRequest(),filterForm);
			
	        
	        UserFilter uFilter = uSession.getUserFilter();
	           //Total Candidates Participating:
	        int totalParticipants=poaHandler.getOverAllTotalCount("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
	        //Get Team Short Description for Header
	        String teamDesc="";
	        if(filterForm.getTeam().equalsIgnoreCase("All"))teamDesc="All";else teamDesc=trDb.getTeamDescription(filterForm.getTeam());
	        Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
	        // this component puts the header info in the chart page.
			
			ChartHeaderWc chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc);
	        
	        
	        
	        PoaChartsWc chartpage = new PoaChartsWc( filterForm, uSession.getUser(),chartHeaderWc ) ;
	        PieChartBuilder pBuilder = new PieChartBuilder();
	        ChartDetailWc chartdetail=null;
	        POAChartBean[] thisPOAChartBean;
	        PieChart pieChart=null;
	      uSession.getUserFilter().getQuseryStrings().setSection(null);
	       boolean fromFilter=false;
	        
	        //Get The Count For the Celebrex
	        thisPOAChartBean=(POAChartBean[])poaHandler.getFilteredPOAChart("MIDPOA1",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
	        pieChart=pBuilder.getPoaChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"POWERS Mid-POA1 Breeze",fromFilter);
	        chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_POA,"POWERS Mid-POA1 Breeze" ) );
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
	    

	    public String listreport() {

			try{
			if ( getResponse().isCommitted() ) {
				return null;
			}
	        
	        	Employee[] poaEmployee=null;
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
	        getSession().setAttribute("ReportType","PowersPOA");
	        TerritoryFilterForm filterForm = uSession.getUserFilterForm();
	        FormUtil.loadObject(getRequest(),filterForm);    
	        if(filterForm.getTeam().trim().equalsIgnoreCase(""))	filterForm.setTeam("All")	;
	        //System.out.println("TEAM IN JPF is"+filterForm.getTeam());
			UserFilter uFilter = uSession.getUserFilter();
	        uFilter.setQueryStrings(qStrings);
			uFilter.setAdmin(uSession.getUser().isAdmin());
	        //System.out.println("Product is "+qStrings.getType());
	        
	        PieChart pieChart=null;

			//OverallProcessor overall = uSession.getOverallProcessor();
	        
	        PieChartBuilder pBuilder = new PieChartBuilder();
	        POAChartBean[] thisPOAChartBean;
	        boolean fromFilter=true;
	    	POAHandler poaHandler = factory.getPOAHandler(); 
	         ChartDetailWc chart=null;
	        //Get The Count For the Geodon
	        if("POWERS Mid-POA1 Breeze".equalsIgnoreCase(qStrings.getType())){
	            thisPOAChartBean=(POAChartBean[])poaHandler.getFilteredPOAChart("MIDPOA1",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
	            uSession.getUserFilter().setProdcut("POWERS Mid-POA1 Breeze");
	            pieChart=pBuilder.getPoaChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"POWERS Mid-POA1 Breeze",fromFilter);
	            chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_POA,"POWERS Mid-POA1 Breeze" ) );
	            chart.setLayout(ChartDetailWc.LAYOUT_ALT);
	        }		
		
			
			if ( "true".equals( uFilter.getQuseryStrings().getDownloadExcel() ) ) {
	           
	           
	               if("Overall".equalsIgnoreCase(qStrings.getType())){
	                 excelPoaEmployee =eHandler.getPOAOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
	                }else{
	                excelPoaEmployee =eHandler.getPOAEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
	                }
	            
	        
	                
		    
				ListReportWpc reportList =  new ListReportWpc( uSession.getUser(), uFilter, chart,excelPoaEmployee);	
				
				page = new ListReportWpc( uSession.getUser(), uFilter, chart,excelPoaEmployee,true);		
	            
	            getSession().setAttribute("xlsBean",excelPoaEmployee);
	            getSession().setAttribute("xlsType",qStrings.getType());
	            getSession().setAttribute("section",qStrings.getSection());
			    return new String("successXls");			
			} 
	       //Get All the Filter and Param we need to run the query to get Employee List 
	      
	       /*if("Overall".equalsIgnoreCase(qStrings.getType())){
	        poaEmployee =eHandler.getPOAOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
	       }else{
	        poaEmployee =eHandler.getPOAEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
	       }*/


	        poaEmployee =eHandler.getPOAEmployees(uFilter,"POWERS Mid-POA1 Breeze",qStrings.getSection());       

	      
	      // poaEmployee =eHandler.getPOAEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
	       //System.out.println("poaEmployee length in JPF is "+poaEmployee.length);
	       if(pieChart.getCount()==0) chart=null;
		   page = new ListReportWpc( uSession.getUser(), uFilter, chart,poaEmployee);		
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

		@Override
		public void setServletResponse(HttpServletResponse response) {
			// TODO Auto-generated method stub
			this.response=response;
			
		}
} 
