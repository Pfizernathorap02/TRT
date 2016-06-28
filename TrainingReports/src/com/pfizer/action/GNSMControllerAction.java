package com.pfizer.action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.db.Employee;
import com.pfizer.db.TeamBean;
import com.pfizer.db.Territory;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.GNSMHandler;
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
import com.pfizer.webapp.wc.GNSM.GnsmChartsWc;
import com.pfizer.webapp.wc.POA.POAChartBean;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchGNSMWc;
import com.pfizer.webapp.wc.global.HeaderGNSMSEARCHWc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.wc.WebPageComponent;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 * @param <Forward>
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/GNSM/GNSMControllerController.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:beginGNSM.do">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:listReportGNSM.do">
 *   <property value="150" name="x"/>
 *   <property value="130" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:getFilteredChartGNSM.do">
 *   <property value="210" name="x"/>
 *   <property value="190" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:searchGNSM.do">
 *   <property value="240" name="x"/>
 *   <property value="220" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/WEB-INF/jsp/templates/mainTemplate.jsp">
 *   <property value="200" name="x"/>
 *   <property value="180" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/WEB-INF/jsp/components/report/PLCReportListXlsgnsm.jsp">
 *   <property value="180" name="x"/>
 *   <property value="160" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:begin.do@">
 *   <property value="44,1,1,109" name="elbowsX"/>
 *   <property value="92,92,56,56" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:beginGNSM.do@">
 *   <property value="24,24,120,120" name="elbowsX"/>
 *   <property value="32,4,4,56" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#successXls#/WEB-INF/jsp/components/report/PLCReportListXlsgnsm.jsp#@action:listReportGNSM.do@">
 *   <property value="114,27,27,180" name="elbowsX"/>
 *   <property value="122,122,116,116" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="successXls" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:listReportGNSM.do@">
 *   <property value="114,111,111,109" name="elbowsX"/>
 *   <property value="111,111,56,56" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:getFilteredChartGNSM.do@">
 *   <property value="174,120,120,120" name="elbowsX"/>
 *   <property value="182,182,163,144" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="South_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#index#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:searchGNSM.do@">
 *   <property value="204,131,131,131" name="elbowsX"/>
 *   <property value="212,212,178,144" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="South_2" name="toPort"/>
 *   <property value="index" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="control:db.TrDB#trDb">
 *   <property value="26" name="x"/>
 *   <property value="34" name="y"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 */
////change the migration code here JPF to Struts
@SuppressWarnings({ "serial", "rawtypes" })
public class GNSMControllerAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{
   
	protected static final Log log = LogFactory.getLog( GNSMControllerAction.class );
    public Employee[] excelPoaEmployee;
    //private db.TrDB trDb;
    private static final long serialVersionUID = 1L;
	TransactionDB trDb= new TransactionDB();
    
    ////change the migration code here add HttpServletRequest,HttpServletResponce
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
	//change the migration code here Forward replace to String
    public String begin()
    {
        return beginGNSM();        
    }
    @Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
		
	}
	
    //change the migration code(replace of String to Forward)
    public String beginGNSM()
    {   
          
        callSecurePage();
        
        if ( getResponse().isCommitted() ) {
			return null;
		}
        
        		
        try {
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(),qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			getRequest().getSession().setAttribute("ReportType","GNSM");
			 boolean fromFilter=false; 
			 
			PieChart pieChart;
   
			MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","GNSM" );
			
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc="All";
			ServiceFactory factory = Service.getServiceFactory( trDb );
			GNSMHandler gnsmHandler = factory.getGNSMHandler();         
			UserFilter uFilter = uSession.getUserFilter();
			if(filterForm.getTeamList().size()<=0){
			    TeamBean[] allTeam=null;
			    //Load data into Listbox;
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
			//Total Candidates Participating:

			int totalParticipants=gnsmHandler.getOverAllTotalCount("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
			Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
			
			// this component puts the header info in the chart page.
			ChartHeaderWc chartHeaderWc=null;
			if(terr!=null){		
			chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc," "," "," ");        
			}       
			
			GnsmChartsWc chartpage = new GnsmChartsWc( filterForm, uSession.getUser() ,chartHeaderWc) ;
			
			List charts = new ArrayList();
			
			PieChartBuilder pBuilder = new PieChartBuilder();
			
			qStrings.setSection("");        
			POAChartBean[] chartBean;  
			ChartDetailWc chartdetail=null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			        
			//Get The Count For the Activity 1                
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODE1",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Geodon Exam 1",fromFilter);
			chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Geodon Exam 1" ) );
			if(pieChart.getCount()>0){  
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			}
			                
			//Get The Count For the Activity 2        
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODE2",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Geodon Exam 2",fromFilter);
			chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Geodon Exam 2" ) );
			if(pieChart.getCount()>0){ 
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			}
			                   
			//Get The Count For the Activity 3
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODSY",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Geodon Learning System Survey",fromFilter);
			chartdetail = new ChartDetailWc(pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Geodon Learning System Survey" ) );
			if(pieChart.getCount()>0){
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			}
			                
			//Get The Count For the Activity 4
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODVL",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Video Link",fromFilter);
			chartdetail = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Video Link" ) );
			if(pieChart.getCount()>0){
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			} 
			                        
			//Get The Count For the Activity 5
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChartAttendance("GEODAT",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Attendance",fromFilter);
			chartdetail = new ChartDetailWc(pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Attendance" ) );
			if(pieChart.getCount()>0){ 
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			}
			
			//Get The Count For the Overall 
			chartBean=(POAChartBean[])gnsmHandler.getFilteredOverallChart("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Overall",fromFilter);
			chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Overall" ) );
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
			
			//return new Forward("success"); 
			return ("success");
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		} 
        
       
    }


    /**
     * @jpf:action
     * @jpf:forward name="successXls" path="/WEB-INF/jsp/components/report/PLCReportListXlsgnsm.jsp"
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    
     //TRT Infosys - Weblogic to Jboss migration  code starts here 
    public String listReportGNSM() {
			
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
			GNSMHandler gnsmHandler = factory.getGNSMHandler(); 
			 ChartDetailWc chart=null;
			//Action for Excel Request
			if ("true".equals( uFilter.getQuseryStrings().getDownloadExcel())) {                      
			    if("Overall".equalsIgnoreCase(qStrings.getType())){                
			        excelPoaEmployee =eHandler.getGNSMOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
			   }else if("Attendance".equalsIgnoreCase(qStrings.getType())){
			        excelPoaEmployee =eHandler.getGNSMEmployeesAttendance(uFilter,qStrings.getType(),qStrings.getSection());       
			    }else{ 
			        excelPoaEmployee =eHandler.getGNSMEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
			    }
			    getRequest().getSession().setAttribute("xlsBean",excelPoaEmployee);
			    getRequest().getSession().setAttribute("xlsType",qStrings.getType());
			    getRequest().getSession().setAttribute("section",qStrings.getSection());  
			    //change the migration code(replace of String to Forward)
			    //return new Forward("successXls");	
			    return ("successXls");
			}
			 //TRT Infosys - Weblogic to Jboss migration  code end here 
			//Render Chart
			if("Geodon Exam 1".equalsIgnoreCase(qStrings.getType())){ 
			    thisPOAChartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODE1",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			    //uSession.getUserFilter().setProdcut("GEODE1");
			    pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Geodon Exam 1",fromFilter);
			    chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Geodon Exam 1" ) );
			    chart.setLayout(ChartDetailWc.LAYOUT_ALT);        
			}else if("Geodon Exam 2".equalsIgnoreCase(qStrings.getType())){
			    thisPOAChartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODE2",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			    //uSession.getUserFilter().setProdcut("GEODE2");
			    pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Geodon Exam 2",fromFilter);        
			    chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Geodon Exam 2" ) );
			    chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}else if("Geodon Learning System Survey".equalsIgnoreCase(qStrings.getType())){
			    thisPOAChartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODSY",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			    //uSession.getUserFilter().setProdcut("GEODSY");
			    pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Geodon Learning System Survey",fromFilter);
			    chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Geodon Learning System Survey" ) );
			    chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}else if("Video Link".equalsIgnoreCase(qStrings.getType())){
			    thisPOAChartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODVL",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			    //uSession.getUserFilter().setProdcut("GEODVL");
			    pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Video Link",fromFilter);
			    chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM ,"Video Link") );
			    chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}else if("Attendance".equalsIgnoreCase(qStrings.getType())){
			    thisPOAChartBean=(POAChartBean[])gnsmHandler.getFilteredChartAttendance("GEODAT",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			    //uSession.getUserFilter().setProdcut("GEODAT");
			    pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Attendance",fromFilter);
			    chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Attendance" ) );
			    chart.setLayout(ChartDetailWc.LAYOUT_ALT);         
			}else if("Overall".equalsIgnoreCase(qStrings.getType())){
			    thisPOAChartBean=(POAChartBean[])gnsmHandler.getFilteredOverallChart("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			    //uSession.getUserFilter().setProdcut("Overall");
			    pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),thisPOAChartBean,"Overall",fromFilter);
			    chart = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Overall" ) );
			    chart.setLayout(ChartDetailWc.LAYOUT_ALT);        
			}
			        					
			     
      if("Overall".equalsIgnoreCase(qStrings.getType())){
			    employees =eHandler.getGNSMOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
      }else if("Attendance".equalsIgnoreCase(qStrings.getType())){
			    employees =eHandler.getGNSMEmployeesAttendance(uFilter,qStrings.getType(),qStrings.getSection());       
      }else{
			    employees =eHandler.getGNSMEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
      }
     
     // poaEmployee =eHandler.getPOAEmployees(uFilter,qStrings.getType(),qStrings.getSection());       
      //System.out.println("poaEmployee length in JPF is "+poaEmployee.length);
      if(pieChart.getCount()==0) chart=null;
			      
			page = new ListReportWpc( uSession.getUser(), uFilter, chart,employees,"GNSM");		
			uFilter.setClusterCode( uSession.getUser().getCluster() );
			Employee areaManager = eHandler.getAreaManager( uFilter );
			Employee regionManager = eHandler.getRegionManager( uFilter );
			Employee districtManager = eHandler.getDistrictManager( uFilter );		
			//((ListReportWpc)page).setAreaManager( areaManager );
			//((ListReportWpc)page).setRegionManager( regionManager );
			//((ListReportWpc)page).setDistrictManager( districtManager );
			getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
			//change the migration code(replace of String to Forward)
			//return new Forward("success");
			return ("success");
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		}			
    }    

    //TRT Infosys - Weblogic to Jboss migration  code starts here 
    private ServletRequest getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     *  @jpf:action
     *  @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    @SuppressWarnings("unchecked")
	public String getFilteredChartGNSM(){
        
        try {
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			TerritoryFilterForm filterForm = uSession.getNewTerritoryFilterForm();
			FormUtil.loadObject(getRequest(),filterForm);    
			ServiceFactory factory = Service.getServiceFactory( trDb );		
			GNSMHandler gnsmHandler = factory.getGNSMHandler();        
			List charts = new ArrayList();
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(),qStrings);
			MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "","GNSM" );
			filterForm = uSession.getUserFilterForm();
			FormUtil.loadObject(getRequest(),filterForm);
			
			
			UserFilter uFilter = uSession.getUserFilter();
			   //Total Candidates Participating:
			int totalParticipants=gnsmHandler.getOverAllTotalCount("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			//Get Team Short Description for Header
			String teamDesc="";
			if(filterForm.getTeam().equalsIgnoreCase("All"))teamDesc="All";else teamDesc=(trDb).getTeamDescription(filterForm.getTeam());
			Territory terr = Service.getServiceFactory(trDb).getTerritoryHandler().getTerritory( uFilter );	
			// this component puts the header info in the chart page.
			
			ChartHeaderWc chartHeaderWc = new ChartHeaderWc( terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(), totalParticipants, uFilter.getProduct(),teamDesc," "," "," ");        
			        
			
			GnsmChartsWc chartpage = new GnsmChartsWc( filterForm, uSession.getUser(),chartHeaderWc) ;
			PieChartBuilder pBuilder = new PieChartBuilder();
			ChartDetailWc chartdetail=null;
			POAChartBean[] chartBean;
			PieChart pieChart=null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			boolean fromFilter=false;

			//Get The Count For the 1
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODE1",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Geodon Exam 1",fromFilter);
			chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Geodon Exam 1" ) );
			if(pieChart.getCount()>0) {         
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			}
			        
			//Get The Count For the 2
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODE2",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Geodon Exam 2",fromFilter);
			chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Geodon Exam 2" ) );
			if(pieChart.getCount()>0){ 
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			}
			                   
			//Get The Count For the 3
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODSY",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Geodon Learning System Survey",fromFilter);
			chartdetail = new ChartDetailWc(pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Geodon Learning System Survey" ) );
			if(pieChart.getCount()>0){ 
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			}
			                
			//Get The Count For the 4
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChart("GEODVL",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());        
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Video Link",fromFilter);
			chartdetail = new ChartDetailWc(pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Video Link" ) );
			if(pieChart.getCount()>0){            
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			}
			
			                
			//Get The Count For the 5
			chartBean=(POAChartBean[])gnsmHandler.getFilteredChartAttendance("GEODAT",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Attendance",fromFilter);
			chartdetail = new ChartDetailWc(pieChart ,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Attendance" ) );
			if(pieChart.getCount()>0){ 
			    charts.add(chartdetail);
			}else{
			    chartdetail = new ChartDetailWc();
			    chartdetail.setChart(pieChart);
			    chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			    charts.add(chartdetail);            
			}
			        
			//Get The Count For the Overall 
			chartBean=(POAChartBean[])gnsmHandler.getFilteredOverallChart("Overall",filterForm.getArea(),filterForm.getRegion(),filterForm.getDistrict(),filterForm.getTeam());
			//thisPOAChartBean=trDb.getPOAOverallChart();
			pieChart=pBuilder.getGNSMChart(uSession.getUserFilter(),getRequest().getSession(),chartBean,"Overall",fromFilter);
			chartdetail = new ChartDetailWc( pieChart,  "Attendance chart", new ChartLegendWc( ChartLegendWc.LAYOUT_GNSM,"Overall" ) );
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
   
			//Changed from Calling charts() for performance.
			//change the migration code(replace of String to Forward)
			//return new Forward("success");
			return ("success");
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		}
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
	//TRT Infosys - Weblogic to Jboss migration  code starts here 
	//change the migration code(replace of String to Forward)
    public String searchGNSM(){
		callSecurePage();
		if ( getResponse().isCommitted() ) {
			return null;
		} 
		EmplSearchForm form = new EmplSearchForm();
		FormUtil.loadObject(getRequest(),form);
		
		UserSession uSession = UserSession.getUserSession(getRequest()); 
		User user = uSession.getUser();	
		boolean bolRefresh=false;        
        
        //Test
		EmployeeSearchPDFHS es = new EmployeeSearchPDFHS();
	 //TRT Infosys - Weblogic to Jboss migration  code end here 
        /*
        //We will create resultMap for all products for the FirstTime User logs in
        ReportBuilder rb = new ReportBuilder(Service.getServiceFactory());
        List products = user.getProducts();
        
        String iter_prod="";
        String sessionKey;
        
         //Lets check if we need to refresh 
            String refresh=(String)getSession().getAttribute("refresh");
            
            if("true".equalsIgnoreCase(refresh)){
                bolRefresh=true;
                log.info("Entering to Refresh");
            }
			
        
        for(Iterator iter=products.iterator();iter.hasNext();){
			Product thisProd=(Product)iter.next();
			sessionKey="allProd"+thisProd.getProductCode();
            
           
			if(getSession().getAttribute(sessionKey)==null || bolRefresh){
				OverallProcessor or = rb.getOverallProcessorByProduct(uSession,thisProd.getProductCode());    
				Map allemps = or.getAllEmployeeMap();
				log.debug("Setting session for "+sessionKey);
				getSession().setAttribute(sessionKey,allemps);
               
			}
        }
        
         if(bolRefresh){
                    getSession().removeAttribute("refresh");
                    bolRefresh=false;
                }
                    
            
		
		List ret = new ArrayList();
		// For PDF and SPF
        String m1 = getRequest().getParameter("m1")==null?"":getRequest().getParameter("m1");
        */
        List ret = new ArrayList();
		try {
			if ( !Util.isEmpty( form.getLname() ) || !Util.isEmpty( form.getFname() ) || !Util.isEmpty(form.getTerrId()) || !Util.isEmpty(form.getEmplid())){
				//ret = es.getPDFHSEmployeesByName( form, uSession, m1 );            
			    ret = es.getGNSMEmployees( form, uSession);                        
			}                
			MainTemplateWpc page = new MainTemplateWpc( user, "detailreport","GNSMSEARCH");        
			((HeaderGNSMSEARCHWc)page.getHeader()).setShowNav(false);
			page.setMain( new EmployeeSearchGNSMWc(form, ret ) );
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
			page.setLoginRequired(true);
			//change the migration code(replace of String to Forward)
			//return new Forward("index");
			return ("success");
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		}
    }
    
}
