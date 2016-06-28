package com.pfizer.action;

/*import com.bea.wlw.netui.pageflow.Forward;
import com.bea.wlw.netui.pageflow.PageFlowController;*/
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.db.Employee;
import com.pfizer.db.TeamBean;
import com.pfizer.db.Territory;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.MSEPIHandler;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.webapp.chart.PieChartBuilder;
import com.pfizer.webapp.search.EmplSearchForm;
import com.pfizer.webapp.search.EmployeeSearchPDFHS;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.MSEPI.MsepiChartsWc;
import com.pfizer.webapp.wc.POA.POAChartBean;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchGNSMWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchMSEPIWc;
import com.pfizer.webapp.wc.global.HeaderMSEPISEARCHWc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.wc.WebPageComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.pfizer.dao.TransactionDB;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/MSEPI/MSEPIController.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:beginMSEPI.do">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:listReportMSEPI.do">
 *   <property value="120" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:getFilteredChartMSEPI.do">
 *   <property value="140" name="x"/>
 *   <property value="120" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:searchMSEPI.do">
 *   <property value="230" name="x"/>
 *   <property value="210" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:index.jsp">
 *   <property value="240" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/WEB-INF/jsp/templates/mainTemplate.jsp">
 *   <property value="180" name="x"/>
 *   <property value="160" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/WEB-INF/jsp/components/report/PLCReportListXlS.jsp">
 *   <property value="200" name="x"/>
 *   <property value="180" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#index.jsp#@action:begin.do@">
 *   <property value="116,160,160,204" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:beginMSEPI.do@">
 *   <property value="60,60,102,144" name="elbowsX"/>
 *   <property value="84,152,152,152" name="elbowsY"/>
 *   <property value="South_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#successXls#/WEB-INF/jsp/components/report/PLCReportListXlS.jsp#@action:listReportMSEPI.do@">
 *   <property value="84,84,142,200" name="elbowsX"/>
 *   <property value="92,136,136,136" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="successXls" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:listReportMSEPI.do@">
 *   <property value="84,84,180,180" name="elbowsX"/>
 *   <property value="103,64,64,116" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:getFilteredChartMSEPI.do@">
 *   <property value="104,104,169,169" name="elbowsX"/>
 *   <property value="112,84,84,116" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#index#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:searchMSEPI.do@">
 *   <property value="194,61,61,191" name="elbowsX"/>
 *   <property value="202,202,116,116" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_2" name="toPort"/>
 *   <property value="index" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="control:db.TrDB#trDb">
 *   <property value="26" name="x"/>
 *   <property value="34" name="y"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 */
public class MSEPIControllerAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{
	/*Infosys - Weblogic to Jboss Migrations changes start here: Added the below code to define variables used*/
	private static final long serialVersionUID = 1L;
	TransactionDB trDb= new TransactionDB();
	Map masterMap = new HashMap();
	 
	protected static final Log log = LogFactory.getLog( P2lControllerAction.class );
	
	private static final String TSR_ADMIN  = "TSR Admin";   
	
	private HttpServletRequest request;
	private HttpServletResponse response;	
	
	private HttpSession session;
	
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
		return session;
	}
	
	public void setSession(HttpSession session) {
		this.session = session;
	}
	/*Infosys - Weblogic to Jboss Migrations changes end here*/
	
	
	public Employee[] excelPoaEmployee;
    /**
     * @common:control
     */
    //private db.TrDB trDb;

    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
	
	 /*Infosys - Weblogic to Jboss Migrations changes start here*/
	 //protected Forward begin() {
	 public String begin() {
	      //return new Forward("success");
	      return ("success");
	      /*Infosys - Weblogic to Jboss Migrations changes end here*/
	 }
	
	 /**getFilteredChart
	     * This method represents the point of entry into the pageflow
	     * @jpf:action
	     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	     */
	 	/*Infosys - Weblogic to Jboss Migrations changes start here*/
	    //protected Forward beginMSEPI()
	    public String beginMSEPI()
	    /*Infosys - Weblogic to Jboss Migrations changes end here*/
	    {   
	          
	        callSecurePage();
	        
	        if ( getResponse().isCommitted() ) {
				return null;
			}
	        	        		
	        AppQueryStrings qStrings = new AppQueryStrings();
			try {
				FormUtil.loadObject(getRequest(),qStrings);
				UserSession uSession;
				uSession = UserSession.getUserSession(getRequest());
				/*Infosys - Weblogic to Jboss Migrations changes start here*/
				getRequest().getSession().setAttribute("ReportType","MSEPI");
				/*Infosys - Weblogic to Jboss Migrations changes end here*/
				 boolean fromFilter=false; 
				 
				PieChart pieChart;
   
				MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","MSEPI" );
				
				TerritoryFilterForm filterForm = uSession.getUserFilterForm();
				String teamDesc="All";
				ServiceFactory factory = Service.getServiceFactory( trDb );
				MSEPIHandler msepiHandler = factory.getMSEPIHandler();         
				UserFilter uFilter = uSession.getUserFilter();
				if(filterForm.getTeamList().size()<=0){
				    TeamBean[] allTeam=null;
				    //Load data into Listbox;
				    allTeam=trDb.getAllMSEPITEAM();
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

				int totalParticipants=msepiHandler.getOverAllTotalCount("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
				Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
				
				// this component puts the header info in the chart page.
				ChartHeaderWc chartHeaderWc=null;
				if(terr!=null)
				{		
				chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc," "," "," ");        
				}       
				
				MsepiChartsWc chartpage = new MsepiChartsWc( filterForm, uSession.getUser() ,chartHeaderWc) ;
				
				List charts = new ArrayList();
				
				PieChartBuilder pBuilder = new PieChartBuilder();
				
				qStrings.setSection("");        
				POAChartBean[] chartBean;  
				ChartDetailWc chartdetail=null;
				uSession.getUserFilter().getQuseryStrings().setSection(null);
				        
				//Get The Count                 
				chartBean=(POAChartBean[])msepiHandler.getFilteredChart("MSEPIATT",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
				pieChart=pBuilder.getMSEPIChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Attendance",fromFilter);
				chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_MSEPI,"Attendance" ) );
				if(pieChart.getCount()>0){  
				    charts.add(chartdetail);
				}else{
				    chartdetail = new ChartDetailWc();
				    chartdetail.setChart(pieChart);
				    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				    charts.add(chartdetail);            
				}
				
				ChartListWc chartListWc = new ChartListWc(charts);
				//Add the Overall Trainees;
				chartListWc.setTrainees(pieChart.getCount());
				chartpage.setWebComponent( chartListWc );
				page.setMain( chartpage );	
				
				getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
				
				/*Infosys - Weblogic to Jboss Migrations changes start here*/
				//return new Forward("success");
				return ("success");
			} catch (Exception e) {
				Global.getError(getRequest(),e);
				return new String("failure");
			}
	        /*Infosys - Weblogic to Jboss Migrations changes end here*/        
	       
	    }

	    /**
	     * @jpf:action
	     * @jpf:forward name="successXls" path="/WEB-INF/jsp/components/report/PLCReportListXlS.jsp"
	     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	     */
	    /*Infosys - Weblogic to Jboss Migrations changes start here*/
	    //protected Forward listReportMSEPI(){
	    public String listReportMSEPI(){
	    /*Infosys - Weblogic to Jboss Migrations changes end here*/
	    	if ( getResponse().isCommitted() ) {
				return null;
			}
	    	
	        
	        Employee[] employees=null;

			try {
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
				UserFilter uFilter = uSession.getUserFilter();
				uFilter.setQueryStrings(qStrings);
				uFilter.setAdmin(uSession.getUser().isAdmin());
				
				PieChart pieChart=null;

				
				PieChartBuilder pBuilder = new PieChartBuilder();
				POAChartBean[] thisPOAChartBean;
				boolean fromFilter=false;
				MSEPIHandler msepiHandler = factory.getMSEPIHandler(); 
				 ChartDetailWc chart=null;
				//Action for Excel Request
				if ("true".equals( uFilter.getQuseryStrings().getDownloadExcel())) {                      
				    excelPoaEmployee =eHandler.getMSEPIEmployees(uFilter,qStrings.getType(),qStrings.getSection());
				    /*Infosys - Weblogic to Jboss migration changes start here*/
				    getRequest().getSession().setAttribute("xlsBean",excelPoaEmployee);
				    getRequest().getSession().setAttribute("xlsType",qStrings.getType());
				    getRequest().getSession().setAttribute("section",qStrings.getSection());
				    
				    //return new Forward("successXls");
				    return ("successXls");
				    /*Infosys - Weblogic to Jboss Migrations changes end here*/		
				} 

				//Render Chart
				if("Attendance".equalsIgnoreCase(qStrings.getType())){ 
				    thisPOAChartBean=(POAChartBean[])msepiHandler.getFilteredChart("MSEPIATT",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
				    pieChart=pBuilder.getMSEPIChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Attendance",fromFilter);
				    chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_MSEPI,"Attendance" ) );
				    chart.setLayout(ChartDetailWc.LAYOUT_ALT);        
				}                					             
				    employees =eHandler.getMSEPIEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
     
     // poaEmployee =eHandler.getPOAEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
      //System.out.println("poaEmployee length in JPF is "+poaEmployee.length);
      if(pieChart.getCount()==0) chart=null;
				      
   page = new ListReportWpc( uSession.getUser(), uFilter, chart,employees,"MSEPI");		
   uFilter.setClusterCode( uSession.getUser().getCluster() );
				Employee areaManager = eHandler.getAreaManager( uFilter );
				Employee regionManager = eHandler.getRegionManager( uFilter );
				Employee districtManager = eHandler.getDistrictManager( uFilter );		
				//((ListReportWpc)page).setAreaManager( areaManager );
				//((ListReportWpc)page).setRegionManager( regionManager );
				//((ListReportWpc)page).setDistrictManager( districtManager );
				getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
				/*Infosys - Weblogic to Jboss Migrations changes start here*/
				//return new Forward("success");
				return ("success");
			} catch (Exception e) {
				Global.getError(getRequest(),e);
				return new String("failure");
			}
	        /*Infosys - Weblogic to Jboss Migrations changes end here*/
						
	    }    
	    
	    /**
	     *  @jpf:action
	     *  @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	     */
	    /*Infosys - Weblogic to Jboss Migrations changes start here*/
	    //protected Forward getFilteredChartMSEPI(){
	    public String getFilteredChartMSEPI(){
	    /*Infosys - Weblogic to Jboss Migrations changes end here*/
	          
	        UserSession uSession;
			try {
				uSession = UserSession.getUserSession(getRequest());
				TerritoryFilterForm filterForm = uSession.getNewTerritoryFilterForm();
				FormUtil.loadObject(getRequest(),filterForm);    
				ServiceFactory factory = Service.getServiceFactory( trDb );		
				MSEPIHandler msepiHandler = factory.getMSEPIHandler();        
				List charts = new ArrayList();
				AppQueryStrings qStrings = new AppQueryStrings();
				FormUtil.loadObject(getRequest(),qStrings);
				MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","MSEPI" );
				filterForm = uSession.getUserFilterForm();
				FormUtil.loadObject(getRequest(),filterForm);
				
				
				UserFilter uFilter = uSession.getUserFilter();
				   //Total Candidates Participating:
				int totalParticipants=msepiHandler.getOverAllTotalCount("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
				//Get Team Short Description for Header
				String teamDesc="";
				if(filterForm.getTeam().equalsIgnoreCase("All"))teamDesc="All";else teamDesc=trDb.getTeamDescription(filterForm.getTeam());
				Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
				// this component puts the header info in the chart page.
				
				ChartHeaderWc chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc," "," "," ");        
				
				MsepiChartsWc chartpage = new MsepiChartsWc( filterForm, uSession.getUser(),chartHeaderWc) ;
				PieChartBuilder pBuilder = new PieChartBuilder();
				ChartDetailWc chartdetail=null;
				POAChartBean[] chartBean;
				PieChart pieChart=null;
				uSession.getUserFilter().getQuseryStrings().setSection(null);
				boolean fromFilter=false;

				//Get The Count
				chartBean=(POAChartBean[])msepiHandler.getFilteredChart("MSEPIATT",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
				pieChart=pBuilder.getMSEPIChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Attendance",fromFilter);
				chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_MSEPI,"Attendance" ) );
				if(pieChart.getCount()>0) {         
				    charts.add(chartdetail);
				}else{
				    chartdetail = new ChartDetailWc();
				    chartdetail.setChart(pieChart);
				    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				    charts.add(chartdetail);            
				} 
				                
				ChartListWc chartListWc = new ChartListWc(charts);
				//Add the Overall Trainees;
				chartListWc.setTrainees(pieChart.getCount());
				chartpage.setWebComponent( chartListWc );
				page.setMain( chartpage );	
				getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
				
				//Changed from Calling charts() for performance.
				/*Infosys - Weblogic to Jboss Migrations changes start here*/
				//return new Forward("success");
				return ("success");
			} catch (Exception e) {
				Global.getError(getRequest(),e);
				return new String("failure");
			}
	        /*Infosys - Weblogic to Jboss Migrations changes end here*/
	    }

	    

		private void callSecurePage() {
			SuperWebPageComponents tpage = new BlankTemplateWpc();
			tpage.setLoginRequired(true);
			IAMUserControl upControl = new IAMUserControl();
			upControl.checkAuth(getRequest(),getResponse(),tpage);
		}	

	     /**
	     * @jpf:action
	     * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	     */
		/*Infosys - Weblogic to Jboss Migrations changes start here*/
	    //protected Forward searchMSEPI(){
	    public String searchMSEPI(){
	    /*Infosys - Weblogic to Jboss Migrations changes end here*/
	    	callSecurePage();
	    	
	    	if ( getResponse().isCommitted() ) {
				return null;
			} 
	    	
			EmplSearchForm form = new EmplSearchForm();
			FormUtil.loadObject(getRequest(),form);
			
			try {
				UserSession uSession = UserSession.getUserSession(getRequest()); 
				User user = uSession.getUser();	
				boolean bolRefresh=false;        
				
				//Test
				EmployeeSearchPDFHS es = new EmployeeSearchPDFHS();
				List ret = new ArrayList();
				if ( !Util.isEmpty( form.getLname() ) || !Util.isEmpty( form.getFname() ) || !Util.isEmpty(form.getTerrId()) || !Util.isEmpty(form.getEmplid())){
					//ret = es.getPDFHSEmployeesByName( form, uSession, m1 );            
				    ret = es.getMSEPIEmployees( form, uSession);                        
				}                
				MainTemplateWpc page = new MainTemplateWpc( user, "detailreport","MSEPISEARCH");        
				((HeaderMSEPISEARCHWc)page.getHeader()).setShowNav(false);
				page.setMain( new EmployeeSearchMSEPIWc(form, ret ) );
				getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
				page.setLoginRequired(true);
				
				/*Infosys - Weblogic to Jboss Migrations changes start here*/
				//return new Forward("index");
				return ("index");
			} catch (Exception e) {
				Global.getError(getRequest(),e);
				return new String("failure");
			}
	        /*Infosys - Weblogic to Jboss Migrations changes end here*/
	    }
	    
    /*Infosys - Weblogic to Jboss Migrations changes start here*/


	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}
	/*Infosys - Weblogic to Jboss Migrations changes end here*/

}
