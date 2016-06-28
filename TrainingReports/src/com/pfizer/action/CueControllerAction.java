package com.pfizer.action;
/*import com.bea.wlw.netui.pageflow.Forward;
import com.bea.wlw.netui.pageflow.PageFlowController;*/
import com.pfizer.db.Employee;
import com.pfizer.db.P2lActivityStatus;
import com.pfizer.db.P2lEmployeeStatus;
import com.pfizer.db.P2lTrack;
import com.pfizer.db.P2lTrackPhase;
import com.pfizer.db.SalesOrgBean;
import com.pfizer.db.TeamBean;
import com.pfizer.db.Territory;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.P2lHandler;
import com.pfizer.hander.TerritoryHandler;
import com.pfizer.service.Service;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.chart.ChartData;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.webapp.chart.PieChartBuilder;
import com.pfizer.webapp.search.EmplSearchForm;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartIndexWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.chart.ChartP2lLegendWc;
import com.pfizer.webapp.wc.components.chart.GenericChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.GenericRightBarWc;
import com.pfizer.webapp.wc.components.chart.RightBarPl2Wc;
import com.pfizer.webapp.wc.components.report.global.EmployeeInfoWc;
import com.pfizer.webapp.wc.components.report.global.MassEmailWc;
import com.pfizer.webapp.wc.components.report.p2l.OverallStatusWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListWc;
import com.pfizer.webapp.wc.components.report.p2l.P2lBreadCrumbWc;
import com.pfizer.webapp.wc.components.report.CueSceReportWc;
import com.pfizer.webapp.wc.components.report.phasereports.DetailPageWc;
import com.pfizer.webapp.wc.components.report.phasereports.EmplSearchResultWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListChartAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListReportAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListSelectAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.PhaseTrainingDetailWc;
import com.pfizer.webapp.wc.components.report.phasereports.TrainingSummaryWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchWc;
import com.pfizer.webapp.wc.components.search.SearchFormWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.pfizer.webapp.wc.global.EmptyPageWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.pfizer.webapp.wc.util.PageBuilder;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.wc.WebComponent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/CUE/CueController.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:charts.do">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:listreport.do">
 *   <property value="80" name="x"/>
 *   <property value="60" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:detailpage.do">
 *   <property value="120" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:searchemployee.do">
 *   <property value="200" name="x"/>
 *   <property value="180" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:getNextLevel.do">
 *   <property value="160" name="x"/>
 *   <property value="140" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/WEB-INF/jsp/templates/mainTemplate.jsp">
 *   <property value="140" name="x"/>
 *   <property value="120" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/WEB-INF/jsp/templates/blankTemplate.jsp">
 *   <property value="220" name="x"/>
 *   <property value="200" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:begin.do@">
 *   <property value="44,21,21,129" name="elbowsX"/>
 *   <property value="92,92,76,76" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:charts.do@">
 *   <property value="24,24,82,140" name="elbowsX"/>
 *   <property value="32,76,76,76" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#successXls#/WEB-INF/jsp/templates/blankTemplate.jsp#@action:listreport.do@">
 *   <property value="44,44,170,170" name="elbowsX"/>
 *   <property value="63,101,101,106" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="successXls" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:listreport.do@">
 *   <property value="44,44,129,129" name="elbowsX"/>
 *   <property value="52,24,24,76" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:detailpage.do@">
 *   <property value="84,21,21,129" name="elbowsX"/>
 *   <property value="92,92,76,76" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/blankTemplate.jsp#@action:searchemployee.do@">
 *   <property value="164,161,161,159" name="elbowsX"/>
 *   <property value="172,172,106,106" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:getNextLevel.do@">
 *   <property value="124,21,21,129" name="elbowsX"/>
 *   <property value="132,132,76,76" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="control:db.TrDB#trDb">
 *   <property value="26" name="x"/>
 *   <property value="34" name="y"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 */

public class CueControllerAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{
	/*Infosys - Weblogic to Jboss Migrations changes start here: Added the below code to define variables used*/
	/**
     * @common:control
     */
    /*private db.TrDB trDb;*/
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
	
	// Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
	/*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward begin(){
    public String begin(){
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
        return charts();
    }
    
    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward charts(){
    public String charts(){
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
    	if ( getResponse().isCommitted() ) {
            return null;
	    }
    	
        System.out.println("Inside charts.do of P2lController");
        // First thing to do is get UserSession object.
        UserSession uSession = buildUserSession();
        UserFilter uFilter = uSession.getUserFilter();
        //added for RBU
        uFilter.setRefresh(true);  
        uSession.setCurrentSlice("");
        try {
			UserTerritory ut = uSession.getUser().getUserTerritory(); 
			System.out.println("IS MULTIPLE GEOS"+uSession.getUser().isMultipleGeos());         
			//ended for RBU
			
			// This builds the area/region/district drop downs.
			TerritorySelectWc territorySelect = new TerritorySelectWc( uSession.getUserFilterForm(), uSession.getUser().getUserTerritory(), "/TrainingReports/p2l/charts"  );
			 //added for RBU
     /*  if(uSession.getUser().isMultipleGeos()==true)
			{
			    territorySelect.setShowMultipleGeos(true);
			} */
			//ended for RBU
			
			// option to show team as a filter item.
			territorySelect.setShowTeam(true);
			
			// this layout is the one with the orange button
			territorySelect.setLayout(11);
			
			// This gets the user filtered territory object
			// used to render the currently selected options.
			TerritoryHandler tHandler = new TerritoryHandler();
			Territory terr = tHandler.getTerritory( uSession.getUserFilter() );				
			
			P2lHandler p2l = new P2lHandler();
			P2lTrack track = uSession.getTrack();

			PieChart chart=null;
			List charts = new ArrayList();
			Collection result = new ArrayList(); 
			P2lTrackPhase phase=null;
			// This loops through the P2lTrack and P2lTrackPhase Obejcts and gets each pie chart.
			for (Iterator it = track.getCompletePhaseList().iterator(); it.hasNext();) {
			    phase = (P2lTrackPhase)it.next();
			    System.out.println("track:" + phase.getTrack().getTrackType());
			    if ( P2lTrackPhase.EMPTY_FLAG.equals(phase.getPhaseNumber()) ) {
			        ChartDetailWc chartDetailWc = new ChartDetailWc();
			        chartDetailWc.setLayout(ChartDetailWc.LAYOUT_EMPTY);
			        charts.add(chartDetailWc);
			    } else {
			         //chart = getPhaseChart(phase, uFilter, phase.getAlttActivityId(), result, false, ""); 
			        //added for RBU
			        chart = getPhaseChart(ut,phase,uFilter,phase.getAlttActivityId(),result, false, ""); 
			        //ended for RBU
			        if ( chart.getCount() > 0 ) {
			            ChartDetailWc chartDetailWc = new ChartDetailWc( chart, phase.getPhaseNumber() ,new ChartP2lLegendWc( phase.getRootActivityId(),  phase) );
			            charts.add(chartDetailWc);            
			        }   else {
			            ChartDetailWc chartDetailWc = new ChartDetailWc();
			            chartDetailWc.setChart(chart);
			            chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			            charts.add(chartDetailWc);
			        }
			    }             
			}
			if ( track.getDoOverall() ) {
			    chart = getOverallChart(track, uFilter, "", result, false, "",track.getAllNodesDelimit());
			    phase = (P2lTrackPhase)track.getPhases().get(0);
			    if ( chart.getCount() > 0 ) {
			        ChartDetailWc chartDetailWc = new ChartDetailWc(chart,"Overall",new ChartP2lLegendWc( "Overall",  phase )  );
			        chartDetailWc.setChart(chart);
			        charts.add(chartDetailWc);
			    }   else {
			            ChartDetailWc chartDetailWc = new ChartDetailWc();
			            chartDetailWc.setChart(chart);
			            chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			            charts.add(chartDetailWc);
			    }
			}
			  
			/* Setting the session */
			getRequest().getSession().setAttribute("P2lCurrentChart",charts);       
			             
			ChartListWc chartListWc = new ChartListWc(charts);
			chartListWc.setLayout(ChartListWc.LAYOUT_2COL);
			        
			EmptyPageWc empty = new EmptyPageWc();
			/*GenericChartHeaderWc headerWc = new GenericChartHeaderWc(terr.getAreaDesc(),
			                                                            terr.getRegionDesc(),
			                                                            terr.getRegionDesc(), 
			                                                            uSession.getUserFilterForm().getTeamDesc());*/
			
			GenericChartHeaderWc headerWc =new GenericChartHeaderWc("","", "","");
			//headerWc.setRightWc(empty);
    /*   P2lBreadCrumbWc crumb = new P2lBreadCrumbWc(track);
			headerWc.setLeftWc(crumb);
			
			
			EmplSearchForm eForm = new EmplSearchForm();
			SearchFormWc searchFormWc = new SearchFormWc(eForm);
			searchFormWc.setPostUrl("searchemployee.do");
			searchFormWc.setTarget("myW");
			searchFormWc.setOnSubmit("DoThis12()");
			EmployeeSearchWc esearch = new EmployeeSearchWc(eForm,new ArrayList() );
			esearch.setSearchForm(searchFormWc);
			*/

			GenericRightBarWc rightBar = new GenericRightBarWc(territorySelect,uSession.getUser());
			
     //  rightBar.setBottomWc(esearch);
			
			ChartIndexWc main = new ChartIndexWc( headerWc, chartListWc, rightBar);
			        
			PageBuilder builder = new PageBuilder();
			MainTemplateWpc page = builder.buildPagePoa2( main, "Chart Index",uSession.getUser(), "reportselect" );
			getRequest().setAttribute( WebComponent.ATTRIBUTE_NAME, page );        
			/*Infosys - Weblogic to Jboss Migrations changes start here*/
			//return new Forward("success");
			return ("success");
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		}
        /*Infosys - Weblogic to Jboss Migrations changes end here*/
    }   
     
    /**330
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="successXls" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward listreport(){
    public String listreport(){
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
    	if ( getResponse().isCommitted() ) {
            return null;
	    }
    	
        UserSession uSession = buildUserSession();
        try {
			UserFilter uFilter = uSession.getUserFilter();
			
			 /* Added for RBU changes */   
			uFilter.setRefresh(true);  
			String activityPk=null;
			String currentSlice=null;
			
			if((uFilter.getQuseryStrings().getSection().equals("null") || uFilter.getQuseryStrings().getSection().equals("")) || 
			    (uFilter.getQuseryStrings().getActivitypk().equals("")|| uFilter.getQuseryStrings().getActivitypk().equals("null")))
			{
			     System.out.println("\n--------------- QUERY STRINGS NULL----------------\n");
			     activityPk = uSession.getCurrentActivity();
			     uFilter.getQuseryStrings().setActivitypk(activityPk);
			     System.out.println("\n--------------- QUERY STRINGS NULL----------------\n"+activityPk);
			     currentSlice= uSession.getCurrentSlice();
			     System.out.println("\n--------------- QUERY STRINGS NULL----------------\n"+currentSlice);
			     uFilter.getQuseryStrings().setSection(currentSlice);
			}
			
			else
			{
			     System.out.println("--------------- QUERY STRINGS NOT NULL----------------");           
			    activityPk = uFilter.getQuseryStrings().getActivitypk();
			    currentSlice=uFilter.getQuseryStrings().getSection();
			}
			uSession.setCurrentSlice(currentSlice);
			uSession.setCurrentActivity(activityPk);
			/*End of addition */
			
     //  uSession.setCurrentSlice( uFilter.getQuseryStrings().getSection() );
      // String activityPk = uFilter.getQuseryStrings().getActivitypk();
			
			Collection result = new ArrayList();

			P2lHandler p2l = new P2lHandler();  
			P2lTrackPhase trackPhase = null;
			String label = "";
			PieChart chart;
			P2lTrack track = uSession.getTrack();
			String reportLabel="";
			
			if ("Overall".equals(activityPk)) {
			    label = "Overall";
			    chart = getOverallChart(track,uFilter,"",result, true, uSession.getUser().getId(),track.getAllNodesDelimit()); //441818     
			    trackPhase = (P2lTrackPhase)track.getPhases().get(0);       
			} else {
			    trackPhase = p2l.getTrackPhase(activityPk,track.getTrackId());
			    label = trackPhase.getPhaseNumber();
			    trackPhase.setTrack(track);
			    //chart = getPhaseChart(trackPhase,uFilter,trackPhase.getAlttActivityId(),result, true, null); //441818
			    trackPhase.setTrack(track);
			    //added for RBU
			    UserTerritory ut = uSession.getUser().getUserTerritory();
			    chart = getPhaseChart(ut,trackPhase,uFilter,trackPhase.getAlttActivityId(),result, true, null); //441818
			    //ended for RBU
			}
			                
			 /*Setting the session required for the getNextLevel() method */
			getRequest().getSession().setAttribute("P2lListReportChart",chart);    
			
			int layout = ChartLegendWc.LAYOUT_PHASE;
			if ( trackPhase != null && trackPhase.getApprovalStatus()) {
			    layout = ChartLegendWc.LAYOUT_PHASE_PENDING;
			}        
			ChartDetailWc chartDetailWc=null;
			
			if ( chart.getCount() > 0 ) {
			    chartDetailWc = new ChartDetailWc( chart, label ,new ChartP2lLegendWc( uFilter.getQuseryStrings().getActivitypk() + "", trackPhase ) );        
			} else {
			    chartDetailWc = new ChartDetailWc();
			    chartDetailWc.setChart(chart);
			    chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);            
			}    
			
			Collection filteredList = new ArrayList();
			for ( Iterator it = result.iterator(); it.hasNext(); ){
			    P2lEmployeeStatus tmp = (P2lEmployeeStatus)it.next();
			    if ( uFilter.getQuseryStrings().getSection().equals(tmp.getStatus()) ) {
			        filteredList.add(tmp);
			    }
			}
			
			   /*Setting the session required for the getNextLevel() method */
			getRequest().getSession().setAttribute("P2lFilteredList",filteredList);
			uFilter.setLayoutNew("4");
			PageBuilder builder = new PageBuilder();
      /* MainReportListReportAreaWc mainArea = new MainReportListReportAreaWc(filteredList,uSession.getUser(), activityPk,uFilter.getQuseryStrings().getSection());
			MassEmailWc emWc = new MassEmailWc(uSession.getUser());
			emWc.setEmailSubject(track.getTrackLabel() + " Follow-up");
			mainArea.setMassEmailWc(emWc);
			System.out.println("UserFilter - Layout"+uFilter.getLayoutNew());
			MainReportListWc main = new MainReportListWc(new MainReportListChartAreaWc(chartDetailWc),
			                                                new MainReportListSelectAreaWc(uSession.getUser(),uFilter),
			                                                mainArea);
      
			main.setPageName(label);
			main.setActivityId(activityPk);
			main.setSlice(uFilter.getQuseryStrings().getSection());
			main.setTrack(track);
			 */        
			reportLabel= uSession.getTrack().getTrackLabel()+":"+label;
			System.out.println("Report Label"+reportLabel);
			        
			ArrayList finalList=new ArrayList(filteredList);
			CueSceReportWc main = new CueSceReportWc();
			main.setReportList(finalList);
			main.setActivityPk(activityPk);
			main.setSection(uFilter.getQuseryStrings().getSection()); 
			main.setReportLabel(reportLabel);
      // MainReportListReportAreaWc reportList=null;
			CueSceReportWc report=null;
			if ( "true".equals( uFilter.getQuseryStrings().getDownloadExcel() ) ) {                                 
			    main.setExcelDownload(true);                      
			    main.setLayout(CueSceReportWc.LAYOUT_XLS);
				//BlankTemplateWpc page = new BlankTemplateWpc(report);
			    BlankTemplateWpc page = new BlankTemplateWpc(main);
				
				getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );	
				getResponse().addHeader("content-disposition","attachment;filename=trainingreports.xls");
				
				getResponse().setContentType("application/vnd.ms-excel");	
				getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
				getResponse().setHeader("Pragma","public");		
				
				/*Infosys - Weblogic to Jboss Migrations changes start here*/
			    //return new Forward("successXls");
				/*Infosys - Weblogic to Jboss Migrations changes start here*/
			    //return new Forward("successXls");
			    return ("successXls");
			    /*Infosys - Weblogic to Jboss Migrations changes end here*/
			    /*Infosys - Weblogic to Jboss Migrations changes end here*/			
			}
			  getRequest().getSession().removeAttribute("p2ldownloadexcelfilter");    
			          
			MainTemplateWpc page = builder.buildPageP2l( main, "Report List",uSession.getUser(), "reportselect" );
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
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward detailpage(){
    public String detailpage(){
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
    	if ( getResponse().isCommitted() ) {
            return null;
	    }
		
        UserSession uSession = buildUserSession();
        try {
			UserFilter uFilter = uSession.getUserFilter();        
			
			/* Added for bug fix */
			UserTerritory ut = uSession.getUser().getUserTerritory();
			/*End of addition */
			
			PageBuilder builder = new PageBuilder();   
			EmployeeHandler eHandler = new EmployeeHandler();
			P2lHandler pHandler = new P2lHandler();
			P2lTrack track = uSession.getTrack();
			P2lHandler p2l = new P2lHandler();
			
			//track=p2l.getCueTrack();
			
			// get Employee object
			Employee emp = eHandler.getEmployeeById( uFilter.getQuseryStrings().getEmplid() );
			
			//build employee info section
			EmployeeInfoWc employeeInfo = new EmployeeInfoWc(emp);
			employeeInfo.setImage( eHandler.getEmployeeImage( emp.getEmplId() ));
			employeeInfo.setManager( eHandler.getEmployeeById( emp.getReportsToEmplid() ) );
			employeeInfo.setEmailSubject(track + " Follow-up");
			
			TrainingSummaryWc tsummary = new TrainingSummaryWc();
			
			// get track and phase info
			String activityPk = uFilter.getQuseryStrings().getActivitypk();
			P2lTrackPhase trackPhase = null;
			String label = "";
			
			OverallStatusWc overallWc = null; 
			if ("Overall".equals(activityPk)) {
			    label = "Overall";
			    trackPhase = (P2lTrackPhase)track.getPhases().get(0);
			} else {
			    
			    trackPhase = p2l.getTrackPhase(activityPk+"",track.getTrackId());
			    label = trackPhase.getPhaseNumber();
			}
			Collection result = new ArrayList();
			//getOverallChart(track,uFilter,"",result, true, uSession.getUser().getId(),track.getAllNodesDelimit()); 
			getOverallChart(track,uFilter,"",result, true, uSession.getUser().getId(),track.getAllNodesDelimit(),ut); 
			for ( Iterator it = result.iterator(); it.hasNext(); ) {
			    P2lEmployeeStatus tm = (P2lEmployeeStatus)it.next();
			    if (emp.getEmplId().equals(tm.getEmployee().getEmplId())) {
			        overallWc = new OverallStatusWc(tm.getStatus());
			    }
			}
			// check if complete button was clicked
			if (  uFilter.getQuseryStrings().getCactivityid() > 0) {
			    p2l.insertComplete(uSession, uFilter.getQuseryStrings().getCactivityid()+"" ,emp);    
			    uFilter.getQuseryStrings().setCactivitid("0");    
			}
			    
			P2lActivityStatus testresult = pHandler.getPhaseDetail( emp.getGuid(), trackPhase);
			    
			PhaseTrainingDetailWc pDetail = new PhaseTrainingDetailWc(new ArrayList(),emp,track,trackPhase,uSession,"None");  
			   
			pDetail.setStatus(testresult);
			DetailPageWc main = new DetailPageWc(employeeInfo, tsummary, pDetail,uSession );
			main.setTrack(track);
			main.setActivityId(activityPk);
			main.setPageName(label);
			
			if ( track.getDoOverall() ) {
			    main.setOverallStatus(overallWc);
			}    
			if ("debug".equals(uSession.getUserFilter().getQuseryStrings().getMode())) {
			    pDetail.setDebug(true);
			}
			MainTemplateWpc page = builder.buildPageP2l( main, "Report List",uSession.getUser(), "reportselect" );
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
 
    
    public PieChart getOverallChart(P2lTrack track, UserFilter uFilter, String altNode, Collection employees, boolean detailflag, String emplid, String otherNodes ) {
        StringBuffer sb = new StringBuffer();
        Timer timer = new Timer();
        P2lHandler p2l = new P2lHandler();
        boolean rmFlag = false;
        if ( "POA".equals(track.getTrackId())) {
            rmFlag = true;
        } 
        Collection master = p2l.getOveralStatus(track,uFilter,true);
        int registered = 0;
        int assigned = 0;
        int exempt = 0;
        int complete = 0;
        int onleave = 0;
        int pending = 0;
        for (Iterator it = master.iterator(); it.hasNext();) {
            P2lEmployeeStatus st = (P2lEmployeeStatus)it.next();
            
            if ("Registered".equals(st.getStatus())) {
                if ( "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
                    st.setStatus("On-Leave");
                    onleave++;
                } else {
                    registered++;
                }
            } else if ("Assigned".equals(st.getStatus())) {
                if ( "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
                    st.setStatus("On-Leave");
                    onleave++;
                } else {
                    assigned++;
                }            
            } else if ("Exempt".equals(st.getStatus())) {
                exempt++;            
            } else if ("Pending".equals(st.getStatus())) {
                pending++;            
            } else if ("RegisteredC".equals(st.getStatus())) {
                st.setStatus("Complete");
                complete++;
            } else {
                complete++;
            }
        }

        HashMap result = new HashMap();
        P2lTrackPhase phase = (P2lTrackPhase)track.getPhases().get(0);
               
		PieChart chart = null;

		List data = new ArrayList();
		 Map tMap = new HashMap();
		data.add(new ChartData( "Complete", complete ) );
        
        if (phase.getExempt()) {
            data.add(new ChartData( "Exempt", exempt ) );
            tMap.put( "Exempt", new Color(255,153,0) );
        }
        if (phase.getAssigned()) {
    		data.add(new ChartData( "Assigned", assigned) );
            tMap.put( "Assigned", new Color(153,204,0) );
        }
        data.add(new ChartData( "Registered", registered ) );
		data.add(new ChartData( "On-Leave", onleave ) );
		
        PieChartBuilder builder = new PieChartBuilder();
        
        
       
		tMap.put( "Complete", AppConst.COLOR_BLUE );
		
        tMap.put( "Registered", new Color(153,51,0) );
		
		tMap.put( "On-Leave", new Color(253,243,156) );
		Map colorMap = Collections.unmodifiableMap(tMap);
        
        String ak = "";
        String label = "";
        if (!Util.isEmpty(otherNodes)) {
            ak = "Overall";
            label = "Overall";
        }
        /*Infosys - Weblogic to Jboss Migrations changes start here*/
        /*chart = builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk=" + ak ,getRequest().getSession(), colorMap);*/
        chart = builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport?activitypk=" + ak ,getRequest().getSession(), colorMap);
        /*Infosys - Weblogic to Jboss Migrations changes end here*/
        chart.setCount( complete + exempt + assigned + registered + onleave);
		employees.addAll(master);			
		return chart;		
	}
    
      public PieChart getOverallChart(P2lTrack track, UserFilter uFilter, String altNode, Collection employees, boolean detailflag, String emplid, String otherNodes, UserTerritory ut ) {
        StringBuffer sb = new StringBuffer();
        Timer timer = new Timer();
        P2lHandler p2l = new P2lHandler();
        boolean rmFlag = false;
        if ( "POA".equals(track.getTrackId())) {
            rmFlag = true;
        } 
        Collection master = p2l.getOveralStatus(track,uFilter,true,ut);
        int registered = 0;
        int assigned = 0;
        int exempt = 0;
        int complete = 0;
        int onleave = 0;
        int pending = 0;
        for (Iterator it = master.iterator(); it.hasNext();) {
            P2lEmployeeStatus st = (P2lEmployeeStatus)it.next();
            
            if ("Registered".equals(st.getStatus())) {
                if ( "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
                    st.setStatus("On-Leave");
                    onleave++;
                } else {
                    registered++;
                }
            } else if ("Assigned".equals(st.getStatus())) {
                if ( "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
                    st.setStatus("On-Leave");
                    onleave++;
                } else {
                    assigned++;
                }            
            } else if ("Exempt".equals(st.getStatus())) {
                exempt++;            
            } else if ("Pending".equals(st.getStatus())) {
                pending++;            
            } else if ("RegisteredC".equals(st.getStatus())) {
                st.setStatus("Complete");
                complete++;
            } else {
                complete++;
            }
        }

        HashMap result = new HashMap();
        P2lTrackPhase phase = (P2lTrackPhase)track.getPhases().get(0);
               
		PieChart chart = null;

		List data = new ArrayList();
		 Map tMap = new HashMap();
		data.add(new ChartData( "Complete", complete ) );
        
        if (phase.getExempt()) {
            data.add(new ChartData( "Exempt", exempt ) );
            tMap.put( "Exempt", new Color(255,153,0) );
        }
        if (phase.getAssigned()) {
    		data.add(new ChartData( "Assigned", assigned) );
            tMap.put( "Assigned", new Color(153,204,0) );
        }
        data.add(new ChartData( "Registered", registered ) );
		data.add(new ChartData( "On-Leave", onleave ) );
		
        PieChartBuilder builder = new PieChartBuilder();
        
        
       
		tMap.put( "Complete", AppConst.COLOR_BLUE );
		
        tMap.put( "Registered", new Color(153,51,0) );
		
		tMap.put( "On-Leave", new Color(253,243,156) );
		Map colorMap = Collections.unmodifiableMap(tMap);
        
        String ak = "";
        String label = "";
        if (!Util.isEmpty(otherNodes)) {
            ak = "Overall";
            label = "Overall";
        }
        /*Infosys - Weblogic to Jboss Migrations changes start here*/
        /*chart = builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk=" + ak ,getRequest().getSession(), colorMap);*/
        chart = builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport?activitypk=" + ak ,getRequest().getSession(), colorMap);
        /*Infosys - Weblogic to Jboss Migrations changes end here*/
        chart.setCount( complete + exempt + assigned + registered + onleave);
		employees.addAll(master);			
		return chart;		
	}
    
    
    
    public PieChart getPhaseChart(P2lTrackPhase phase, UserFilter uFilter, String altNode, Collection employees, boolean detailflag, String emplid) {
        StringBuffer sb = new StringBuffer();
        Timer timer = new Timer();
        P2lHandler p2l = new P2lHandler();
        boolean rmFlag = false;
        if ( "POA".equals(phase.getTrackId())) {
            rmFlag = true;
        } 
        Collection master = p2l.getPhaseStatus(phase, uFilter,detailflag,"");
        int registered = 0;
        int assigned = 0;
        int exempt = 0;
        int complete = 0;
        int onleave = 0;
        int pending = 0;
        for (Iterator it = master.iterator(); it.hasNext();) {
            P2lEmployeeStatus st = (P2lEmployeeStatus)it.next();
            
            
            if ("Registered".equals(st.getStatus())) {
                if ( "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
                    st.setStatus("On-Leave");
                    onleave++;
                } else {
                    registered++;
                }
            } else if ("Assigned".equals(st.getStatus())) {
                if ( "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
                    st.setStatus("On-Leave");
                    onleave++;
                } else {
                    assigned++;
                }            
            } else if ("Exempt".equals(st.getStatus())) {
                exempt++;            
            } else if ("Pending".equals(st.getStatus())) {
                pending++;            
            } else if ("RegisteredC".equals(st.getStatus())) {
            	st.setStatus("Complete");
                complete++;
            } else {
                complete++;
            }
        }

        HashMap result = new HashMap();
        
               
		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();
        
		data.add(new ChartData( "Complete", complete ) );
		
        if ( phase.getExempt() ) {
            data.add(new ChartData( "Exempt", exempt ) );
            tMap.put( "Exempt", new Color(255,153,0) );
        }
        if ( phase.getAssigned()) {
        	data.add(new ChartData( "Assigned", assigned) );
            tMap.put( "Assigned", new Color(153,204,0) );
        }
        if ( phase.getApprovalStatus() ) {
            tMap.put( "Pending", AppConst.COLOR_CYAN );   
    		data.add(new ChartData( "Pending", pending ) );            
        } else {
    		data.add(new ChartData( "Registered", registered ) );
        }
		data.add(new ChartData( "On-Leave", onleave ) );
		
        PieChartBuilder builder = new PieChartBuilder();
        
        
		tMap.put( "Complete", AppConst.COLOR_BLUE );
		
        tMap.put( "Registered", new Color(153,51,0) );
		tMap.put( "On-Leave", new Color(253,243,156) );
		Map colorMap = Collections.unmodifiableMap(tMap);
        
        String ak = phase.getRootActivityId();
        String label = phase.getTrack().getTrackLabel() + " : " +  phase.getPhaseNumber();
        /*Infosys - Weblogic to Jboss Migrations changes start here*/
        /*chart = builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk=" + ak ,getRequest().getSession(), colorMap);*/
        chart = builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport?activitypk=" + ak ,getRequest().getSession(), colorMap);
        /*Infosys - Weblogic to Jboss Migrations changes end here*/
        chart.setCount( complete + exempt + assigned + registered + onleave);
		employees.addAll(master);				
		return chart;		
	}

    /**
      * RBU: Function Overloading to add the parameter geoHashMap for Report Generation
      */

    public PieChart getPhaseChart(UserTerritory ut, P2lTrackPhase phase, UserFilter uFilter, String altNode, Collection employees, boolean detailflag, String emplid) {
        StringBuffer sb = new StringBuffer();
        Timer timer = new Timer();
        P2lHandler p2l = new P2lHandler();
        boolean rmFlag = false;
        if ( "POA".equals(phase.getTrackId())) {
            rmFlag = true;
        } 
        //Collection master = p2l.getPhaseStatus(phase, uFilter,detailflag,"");
        //added for RBU
        System.out.println("Before calling getCuePhaseStatus");
        Collection master = p2l.getCuePhaseStatus(ut, phase, uFilter,detailflag,"");
        //ended for RBU
        int registered = 0;
        int assigned = 0;
        int exempt = 0;
        int complete = 0;
        int onleave = 0;
        int pending = 0;
        for (Iterator it = master.iterator(); it.hasNext();) {
            P2lEmployeeStatus st = (P2lEmployeeStatus)it.next();
            
            
            if ("Registered".equals(st.getStatus())) {
                if ( "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
                    st.setStatus("On-Leave");
                    onleave++;
                } else {
                    registered++;
                }
            } else if ("Assigned".equals(st.getStatus())) {
                if ( "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
                    st.setStatus("On-Leave");
                    onleave++;
                } else {
                    assigned++;
                }            
            } else if ("Exempt".equals(st.getStatus())) {
                exempt++;            
            } else if ("Pending".equals(st.getStatus())) {
                pending++;            
            } else if ("RegisteredC".equals(st.getStatus())) {
            	st.setStatus("Complete");
                complete++;
            } else {
                complete++;
            }
        }

        HashMap result = new HashMap();              
		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();
        
		data.add(new ChartData( "Complete", complete ) );
		
        if ( phase.getExempt() ) {
            data.add(new ChartData( "Exempt", exempt ) );
            tMap.put( "Exempt", new Color(255,153,0) );
        }
        if ( phase.getAssigned()) {
        	data.add(new ChartData( "Assigned", assigned) );
            tMap.put( "Assigned", new Color(153,204,0) );
        }
        if ( phase.getApprovalStatus() ) {
            tMap.put( "Pending", AppConst.COLOR_CYAN );   
    		data.add(new ChartData( "Pending", pending ) );            
        } else {
    		data.add(new ChartData( "Registered", registered ) );
        }
		data.add(new ChartData( "On-Leave", onleave ) );
		
        PieChartBuilder builder = new PieChartBuilder();
        
        
		tMap.put( "Complete", AppConst.COLOR_BLUE );
		
        tMap.put( "Registered", new Color(153,51,0) );
		tMap.put( "On-Leave", new Color(253,243,156) );
		Map colorMap = Collections.unmodifiableMap(tMap);
        
        String ak = phase.getRootActivityId();
        String label = phase.getTrack().getTrackLabel() + " : " +  phase.getPhaseNumber();
        /*Infosys - Weblogic to Jboss Migrations changes start here*/
        /*chart = builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk=" + ak ,getRequest().getSession(), colorMap);*/
        chart = builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport?activitypk=" + ak ,getRequest().getSession(), colorMap);
        /*Infosys - Weblogic to Jboss Migrations changes end here*/
        chart.setCount( complete + exempt + assigned + registered + onleave);
		employees.addAll(master);				
		return chart;		
	}



    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
     */
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward searchemployee(){
    public String searchemployee(){
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
    	if ( getResponse().isCommitted() ) {
            return null;
	    }        
        UserSession uSession = buildUserSession();
        UserFilter uFilter = uSession.getUserFilter();        
        P2lTrack track = uSession.getTrack();
        List phases = track.getPhases();
        String nodes = "";
        for (Iterator it = phases.iterator(); it.hasNext(); ) {
            P2lTrackPhase phase = (P2lTrackPhase)it.next();
            if ( Util.isEmpty( nodes ) ) {
                nodes = phase.getRootActivityId();
            } else {
                nodes = nodes + "," + phase.getRootActivityId();
            }
            if ( !Util.isEmpty( phase.getAlttActivityId() )) {
                nodes = nodes + "," + phase.getAlttActivityId();
            }
        }
        EmplSearchForm eForm = new EmplSearchForm();
        FormUtil.loadObject(getRequest(),eForm);
        System.out.println("" + eForm.getFname());
        
        P2lHandler pHandler = new P2lHandler();
        Employee[] result = pHandler.search(nodes,uSession,eForm);
        
        BlankTemplateWpc page = new BlankTemplateWpc();
        EmplSearchResultWc main = new EmplSearchResultWc(result);
        main.setTrack(track);
        page.setMain(main);
        getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );   
        /*Infosys - Weblogic to Jboss Migrations changes start here*/
        //return new Forward("success");
        return ("success");
        /*Infosys - Weblogic to Jboss Migrations changes end here*/
    }        


    /**
     * This setups the session object for this controller.  Most of this can be reused but some stuff is unique to this controller.
     */
    private UserSession buildUserSession() {
		UserSession uSession = UserSession.getUserSession(getRequest());
						
        //
        //
        //  THIS PART CAN BE COPIED FOR OTHER CONTROLLERS THAT USE THE TERRITORY DROP DOWNS.
        //
        //
                        
		TerritoryFilterForm filterForm = uSession.getUserFilterForm();

        // This grabs info for team drop downs
     /*   if(uSession.getUserFilterForm().getTeamList().size()<=0){
            TeamBean[] allTeam=null;
            if ( uSession.getUser().isAdmin() || uSession.getUser().isTsrAdmin() ) {
                allTeam=trDb.getAllTEAM();
            } else {
                allTeam=trDb.getTEAMBYCLUSTER(uSession.getUser().getCluster());
            }        
            LabelValueBean labelValueBean ;
            uSession.getUserFilterForm().setTeam("All");
            
            FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
            labelValueBean=new LabelValueBean("All","All");
            uSession.getUserFilterForm().setTeamList(labelValueBean);
            for(int i=0;i<allTeam.length;i++){
              labelValueBean=new LabelValueBean(allTeam[i].getTeamDesc(),allTeam[i].getTeamCd());
              uSession.getUserFilterForm().setTeamList(labelValueBean);
            }
        }
       */ 
           /* Modified for displaying the Sales Org drop down */
        if(uSession.getUserFilterForm().getSalesOrgList().size()<=0){
            SalesOrgBean[] allSalesOrg=null;
            
         if ( uSession.getUser().isAdmin() || uSession.getUser().isTsrAdmin()) {
                allSalesOrg=trDb.getAllSALESORG();
            } else {
                allSalesOrg=trDb.getSALESORGBYUSER(uSession.getUser().getEmplid());
            }       
            LabelValueBean labelValueBean ;
            uSession.getUserFilterForm().setSalesOrg("All");
            
            FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
            labelValueBean=new LabelValueBean("All","All");
            uSession.getUserFilterForm().setSalesOrgList(labelValueBean);
            for(int i=0;i<allSalesOrg.length;i++){
              labelValueBean=new LabelValueBean(allSalesOrg[i].getSalesOrgDesc(),allSalesOrg[i].getSalesOrgCd());
              uSession.getUserFilterForm().setSalesOrgList(labelValueBean);
            }
        }
        
         if(uSession.getUserFilterForm().getFirstList().size()<=0)
        {              
            ArrayList firstGeo= new ArrayList();
            firstGeo =uSession.getUser().getUserTerritory().getFirstDropdown();
            LabelValueBean labelValueBean ;
            uSession.getUserFilterForm().setLevel1("All");
            FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
            labelValueBean=new LabelValueBean("All","All");
            uSession.getUserFilterForm().setFirstList(labelValueBean);
            if(firstGeo!=null){
            for(int i=0;i<firstGeo.size();i++){     
              LabelValueBean tempLabelValueBean;   
              tempLabelValueBean = (LabelValueBean)firstGeo.get(i);    
              labelValueBean=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());
              uSession.getUserFilterForm().setFirstList(labelValueBean);
            }
            }
        
        }
        
    /*    if(uSession.getUser().isMultipleGeos()==true)
        {
            uSession.getUserFilterForm().clearMultipleGeos();
            ArrayList mulGeos=new ArrayList();
            mulGeos= uSession.getUser().getMultipleGeos();
            System.out.println("/nMULTIPLE GEOS IN PHASE CONTROLLER"+mulGeos.size());
            System.out.println("/nGEOGRAPHY ID IN PHASE CONTROLLER"+uSession.getUser().getGeographyId());
            LabelValueBean labelValueBean ;
            uSession.getUserFilterForm().setSelectedGeo(uSession.getUser().getGeographyId());
            FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
           // labelValueBean=new LabelValueBean(uSession.getUser().getGeographyId(),uSession.getUser().getGeographyId());
          //  uSession.getUserFilterForm().setMultipleGeoList(labelValueBean);
            for(int i=0;i<mulGeos.size();i++){     
              LabelValueBean tempLabelValueBean;   
              tempLabelValueBean = (LabelValueBean)mulGeos.get(i);    
              labelValueBean=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());
              uSession.getUserFilterForm().setMultipleGeoList(labelValueBean);
            }
        } */
        
        // This populates the territory form object
		FormUtil.loadObject(getRequest(),filterForm);
		
		// process query stings
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
		FormUtil.loadObject(getRequest(),qStrings.getSortBy());
		
        // This will give you the full query string if needed
		qStrings.setFullQueryString( getRequest().getQueryString() );
		
        // Setup user filter obejct
		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setEmployeeId(uSession.getUser().getEmplid());
		uFilter.setAdmin( uSession.getUser().isAdmin() );	
		uFilter.setTsrAdmin( uSession.getUser().isTsrAdmin() );	
		//uFilter.setClusterCode(uSession.getUser().getCluster());	
		uFilter.setFilterForm(filterForm);
		uFilter.setQueryStrings(qStrings);
        uFilter.setIsSpecialRoleUser(uSession.getUser().isSpecialRole());
        uFilter.setEmployeeIdForSplRole(uSession.getUser().getEmplIdForSpRole());
        //
        //  END OF STANDARD SESSION SETUP.
        //
        
        

        //
        //
        //  DO NOT COPY AND PASTE FROM HERE DOWN, THIS IS UNIQUE FOR THIS CONTROLLER
        //
        //
        
        // Get track info and store it in session.
        P2lHandler p2l = new P2lHandler();
        P2lTrack track = uSession.getTrack();
        if ( track == null || !Util.isEmpty(uFilter.getQuseryStrings().getTrack()) ) {
            track = p2l.getTrack(uFilter.getQuseryStrings().getTrack());
            uSession.setTrack(track);
        }
        
        return uSession;
    }    
    
      /**
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward getNextLevel()
    public String getNextLevel()
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
    {  
        UserSession uSession = buildUserSession();
        TerritoryFilterForm filterForm = uSession.getUserFilterForm();        
        String salesId=null; 
        String salesLevel=null;  
        String salesMultiple=null;    
        String salesValue=null; 
        String salesOrg=null; 
        AppQueryStrings qString = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qString); 	       
        salesId = qString.getSales();
        salesLevel=qString.getSaleslevel();
        salesMultiple=qString.getMultiple();
        salesValue = qString.getSalesvalue();
        salesOrg= qString.getSalesorg();
        
        System.out.println("***GEO ID ****"+salesId); 
        System.out.println("*** USER TYPE ****"+salesLevel); 
        System.out.println("*** GEO MULTIPLE ****"+salesMultiple); 
            
            uSession.getUserFilterForm().setSalesOrg(salesOrg);
        
        /* This loop is to handle users having multiple geographies */
        
     /*   if(geoId!=null && geoMultiple!=null)
        {
                HashMap tempMap= new HashMap();
                String tempGeoId=null;
                tempMap=uSession.getUser().getMultipleGeoMap();
                tempGeoId = (String) tempMap.get(geoId);
                System.out.println("TEMP GEO ID---------------------"+tempGeoId);
                UserTerritory terr = Service.getServiceFactory().getTerritoryHandler().getUserGeography(uSession.getUser().getEmplid(),tempGeoId);				
                uSession.getUser().setUserTerritory(terr);
                uSession.getUserFilterForm().setSelectedGeo(geoValue);
              /* Resetting the first Geo drop down in case the geo ID of the user is changed              
                uSession.getUserFilterForm().clearFirstList();
                ArrayList firstGeo= new ArrayList();
                firstGeo =uSession.getUser().getUserTerritory().getFirstGeoDropdown();
                LabelValueBean labelValueBean ;
                uSession.getUserFilterForm().setLevel1("All");
                FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
                labelValueBean=new LabelValueBean("All","All");
                uSession.getUserFilterForm().setFirstList(labelValueBean);
                for(int i=0;i<firstGeo.size();i++)
                {    
                  LabelValueBean tempLabelValueBean;
                  tempLabelValueBean = (LabelValueBean)firstGeo.get(i);     
                  labelValueBean=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue()); 
                  uSession.getUserFilterForm().setFirstList(labelValueBean);              
                }
                uSession.getUserFilterForm().clearSecondList();
                uSession.getUserFilterForm().clearThirdList();
                uSession.getUserFilterForm().clearFourthList();
                uSession.getUserFilterForm().clearFifthList();
                uSession.getUserFilterForm().clearSixthList();
                uSession.getUserFilterForm().clearSeventhList();
                uSession.getUserFilterForm().clearEighthList();
                uSession.getUserFilterForm().clearNinthList();
                uSession.getUserFilterForm().clearTenthList();
        } */
        
        UserTerritory ut = uSession.getUser().getUserTerritory();    
        
        if(salesId!=null && salesLevel!=null)
        {                
            if(salesLevel.toString().equals("2"))        
            { 
                ArrayList nextLevel=new ArrayList();          
                LabelValueBean labelValueBean ;
                if (!salesId.equalsIgnoreCase("All"))
                {                                         
                    filterForm.setLevel1(salesValue);
                    nextLevel = ut.getDropdownSalesPositionDesc(salesValue);
                    uSession.getUserFilterForm().clearSecondList();
                    uSession.getUserFilterForm().clearThirdList();
                    uSession.getUserFilterForm().clearFourthList();
                    uSession.getUserFilterForm().clearFifthList();
                    uSession.getUserFilterForm().clearSixthList();
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList(); 
                    uSession.getUserFilterForm().setLevel2("All");                                    
                    FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());  
                    System.out.println("Size of next level"+nextLevel.size());
                    if(nextLevel.size()>0)
                    {
                       labelValueBean=new LabelValueBean("All","All");
                       uSession.getUserFilterForm().setSecondList(labelValueBean);                
                       for(int i=0;i<nextLevel.size();i++)
                        {     
                          LabelValueBean tempLabelValueBean;
                          tempLabelValueBean = (LabelValueBean)nextLevel.get(i);     
                          labelValueBean=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());            
                          uSession.getUserFilterForm().setSecondList(labelValueBean);
                         }
                    }                 
                 }
                else if(salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel1(salesValue);
                    uSession.getUserFilterForm().clearSecondList();
                    uSession.getUserFilterForm().clearThirdList();
                    uSession.getUserFilterForm().clearFourthList();
                    uSession.getUserFilterForm().clearFifthList();
                    uSession.getUserFilterForm().clearSixthList();
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList();            
                }  
            }          
            else if(salesLevel.toString().equals("3"))
            {
                System.out.println("---- Inside second loop-----------"+salesId);                       
                ArrayList nextLevel2=new ArrayList();
                if (!salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel2(salesValue);
                    nextLevel2 = ut.getDropdownSalesPositionDesc(salesValue);
                    LabelValueBean labelValueBean1 ;
                    uSession.getUserFilterForm().setLevel3("All");
                    FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
                    if(nextLevel2.size()>0)
                    {
                        labelValueBean1=new LabelValueBean("All","All");
                        uSession.getUserFilterForm().setThirdList(labelValueBean1);
                        uSession.getUserFilterForm().clearFourthList();
                        uSession.getUserFilterForm().clearFifthList();
                        uSession.getUserFilterForm().clearSixthList();
                        uSession.getUserFilterForm().clearSeventhList();
                        uSession.getUserFilterForm().clearEighthList();
                        uSession.getUserFilterForm().clearNinthList();
                        uSession.getUserFilterForm().clearTenthList(); 
                        System.out.println("SIZE OF NEXT LEVEL"+nextLevel2.size());
                        for(int i=0;i<nextLevel2.size();i++)
                        { 
                          LabelValueBean tempLabelValueBean;
                          tempLabelValueBean = (LabelValueBean)nextLevel2.get(i);     
                          labelValueBean1=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());
                          uSession.getUserFilterForm().setThirdList(labelValueBean1);
                         }
                    }        
                 }
                  else if(salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel2(salesValue);
                    uSession.getUserFilterForm().clearThirdList();
                    uSession.getUserFilterForm().clearFourthList();
                    uSession.getUserFilterForm().clearFifthList();
                    uSession.getUserFilterForm().clearSixthList();
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList();
                } 
            }
            else if(salesLevel.toString().equals("4"))
            {
                System.out.println("---- Inside third loop-----------"+salesId);                       
                ArrayList nextLevel3=new ArrayList();
                if (!salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel3(salesValue);
                    nextLevel3 = ut.getDropdownSalesPositionDesc(salesValue);     
                    uSession.getUserFilterForm().clearFourthList();
                    uSession.getUserFilterForm().clearFifthList();
                    uSession.getUserFilterForm().clearSixthList();
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList(); 
                    LabelValueBean labelValueBean1 ;
                    uSession.getUserFilterForm().setLevel4("All");
                    FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
                    if(nextLevel3.size()>0)
                    {
                        labelValueBean1=new LabelValueBean("All","All");
                        uSession.getUserFilterForm().setFourthList(labelValueBean1);
                        System.out.println("SIZE OF NEXT LEVEL"+nextLevel3.size());
                        for(int i=0;i<nextLevel3.size();i++)
                        { 
                          LabelValueBean tempLabelValueBean;
                          tempLabelValueBean = (LabelValueBean)nextLevel3.get(i);                   
                          labelValueBean1=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());                           
                          uSession.getUserFilterForm().setFourthList(labelValueBean1);
                         }
                    } 
                }
                else if(salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel3(salesValue);
                    uSession.getUserFilterForm().clearFourthList();
                    uSession.getUserFilterForm().clearFifthList();
                    uSession.getUserFilterForm().clearSixthList();
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList();
                }        
             }
            else if(salesLevel.toString().equals("5"))
            {
                System.out.println("---- Inside fourth loop-----------"+salesId);                       
                ArrayList nextLevel4=new ArrayList();
                 if (!salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel4(salesValue);
                    nextLevel4 = ut.getDropdownSalesPositionDesc(salesValue);   
                    uSession.getUserFilterForm().clearFifthList();
                    uSession.getUserFilterForm().clearSixthList();
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList(); 
                    LabelValueBean labelValueBean1 ;
                    uSession.getUserFilterForm().setLevel5("All");
                    FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
                     if(nextLevel4.size()>0)
                    {
                        labelValueBean1=new LabelValueBean("All","All");
                        uSession.getUserFilterForm().setFifthList(labelValueBean1);
                        System.out.println("SIZE OF NEXT LEVEL"+nextLevel4.size());
                        for(int i=0;i<nextLevel4.size();i++)
                        { 
                          LabelValueBean tempLabelValueBean;
                          tempLabelValueBean = (LabelValueBean)nextLevel4.get(i);                   
                          labelValueBean1=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());            
                          uSession.getUserFilterForm().setFifthList(labelValueBean1);
                         }
                    }
                }
                else if(salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel4(salesValue);
                    uSession.getUserFilterForm().clearFifthList();
                    uSession.getUserFilterForm().clearSixthList();
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList();
                }         
             }
              else if(salesLevel.toString().equals("6"))
            {
                System.out.println("---- Inside fifth loop-----------"+salesId);                       
                ArrayList nextLevel5=new ArrayList();
                if (!salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel5(salesValue);
                    nextLevel5 =ut.getDropdownSalesPositionDesc(salesValue);    
                    uSession.getUserFilterForm().clearSixthList();
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList(); 
                    LabelValueBean labelValueBean1 ;
                    uSession.getUserFilterForm().setLevel6("All");
                    FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
                     if(nextLevel5.size()>0)
                    {
                        labelValueBean1=new LabelValueBean("All","All");
                        uSession.getUserFilterForm().setSixthList(labelValueBean1);
                        System.out.println("SIZE OF NEXT LEVEL"+nextLevel5.size());
                        for(int i=0;i<nextLevel5.size();i++)
                        { 
                          LabelValueBean tempLabelValueBean;
                          tempLabelValueBean = (LabelValueBean)nextLevel5.get(i);                   
                          labelValueBean1=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());            
                          uSession.getUserFilterForm().setSixthList(labelValueBean1);
                         }
                    }
                }
                 else if(salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel5(salesValue);
                    uSession.getUserFilterForm().clearSixthList();
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList();
                }        
             }
              else if(salesLevel.toString().equals("7"))
            {
                System.out.println("---- Inside fifth loop-----------"+salesId);                       
                ArrayList nextLevel6=new ArrayList();
                if (!salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel6(salesValue);
                    nextLevel6 = ut.getDropdownSalesPositionDesc(salesValue);     
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList();
                    LabelValueBean labelValueBean1 ;
                    uSession.getUserFilterForm().setLevel7("All");
                    FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
                    if(nextLevel6.size()>0)
                    {
                        labelValueBean1=new LabelValueBean("All","All");
                        uSession.getUserFilterForm().setSeventhList(labelValueBean1);
                        System.out.println("SIZE OF NEXT LEVEL"+nextLevel6.size());
                        for(int i=0;i<nextLevel6.size();i++)
                        { 
                          LabelValueBean tempLabelValueBean;
                          tempLabelValueBean = (LabelValueBean)nextLevel6.get(i);                   
                          labelValueBean1=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());            
                          uSession.getUserFilterForm().setSeventhList(labelValueBean1);
                         }
                    }
                }
                else if(salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel6(salesValue);
                    uSession.getUserFilterForm().clearSeventhList();
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList();
                }  
                        
             }
             else if(salesLevel.toString().equals("8"))
            {
                System.out.println("---- Inside sixth loop-----------"+salesId);                       
                ArrayList nextLevel7=new ArrayList();
                 if (!salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel7(salesValue);
                    nextLevel7 = ut.getDropdownSalesPositionDesc(salesValue);   
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList(); 
                    LabelValueBean labelValueBean1 ;
                    uSession.getUserFilterForm().setLevel8("All");
                    FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
                    if(nextLevel7.size()>0)
                    {
                        labelValueBean1=new LabelValueBean("All","All");
                        uSession.getUserFilterForm().setEighthList(labelValueBean1);
                        for(int i=0;i<nextLevel7.size();i++)
                        {          
                          LabelValueBean tempLabelValueBean;
                          tempLabelValueBean = (LabelValueBean)nextLevel7.get(i);                   
                          labelValueBean1=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());            
                          uSession.getUserFilterForm().setEighthList(labelValueBean1);
                         }
                    }
                }
                else if(salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel7(salesValue);
                    uSession.getUserFilterForm().clearEighthList();
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList();
                }         
             }
              else if(salesLevel.toString().equals("9"))
            {
                
                System.out.println("---- Inside sixth loop-----------"+salesId);                       
                ArrayList nextLevel8=new ArrayList();
                 if (!salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel8(salesValue);
                    nextLevel8 = ut.getDropdownSalesPositionDesc(salesValue);  
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList(); 
                    LabelValueBean labelValueBean1 ;
                    uSession.getUserFilterForm().setLevel9("All");
                    FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
                    if(nextLevel8.size()>0)
                    {
                        labelValueBean1=new LabelValueBean("All","All");
                        uSession.getUserFilterForm().setNinthList(labelValueBean1);
                        for(int i=0;i<nextLevel8.size();i++)
                        {          
                          LabelValueBean tempLabelValueBean;
                          tempLabelValueBean = (LabelValueBean)nextLevel8.get(i);                   
                          labelValueBean1=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());            
                          uSession.getUserFilterForm().setNinthList(labelValueBean1);                          
                         }
                    }
                }
                else if(salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel8(salesValue);
                    uSession.getUserFilterForm().clearNinthList();
                    uSession.getUserFilterForm().clearTenthList();
                }        
             }
                else if(salesLevel.toString().equals("10"))
            {
                System.out.println("---- Inside sixth loop-----------"+salesId);                       
                ArrayList nextLevel9=new ArrayList();
                if (!salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel9(salesValue);
                    nextLevel9 =ut.getDropdownSalesPositionDesc(salesValue);     
                    uSession.getUserFilterForm().clearTenthList();
                    LabelValueBean labelValueBean1 ;
                    uSession.getUserFilterForm().setLevel10("All");
                    FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
                    if(nextLevel9.size()>0)
                    {
                        labelValueBean1=new LabelValueBean("All","All");
                        uSession.getUserFilterForm().setTenthList(labelValueBean1);
                        for(int i=0;i<nextLevel9.size();i++)
                        {          
                          LabelValueBean tempLabelValueBean;
                          tempLabelValueBean = (LabelValueBean)nextLevel9.get(i);                   
                          labelValueBean1=new LabelValueBean((String)tempLabelValueBean.getLabel(),(String)tempLabelValueBean.getValue());            
                          uSession.getUserFilterForm().setTenthList(labelValueBean1);
                         }
                    }
                }
                else if(salesId.equalsIgnoreCase("All"))
                {
                    filterForm.setLevel9(salesValue);
                    uSession.getUserFilterForm().clearTenthList();
                }
                        
             }
            else if(salesLevel.toString().equals("11"))
                {              
                    uSession.getUserFilterForm().setLevel10(salesValue);
                }            
        }
                
        UserFilter uFilter = uSession.getUserFilter(); 
        uFilter.setRefresh(false);       
        System.out.println("TYPE IN PHASECONTROLLER"+uFilter.getFilterForm().getSelectionType());
        /* Code for drop downs--To be uncommented later */
        TerritorySelectWc territorySelect = new TerritorySelectWc( uSession.getUserFilterForm(), uSession.getUser().getUserTerritory(), "/TrainingReports/p2l/charts"  );
		territorySelect.setShowTeam(true);
        territorySelect.setShowMultipleGeos(true);
        territorySelect.setLayout(4);
        
        TerritoryHandler tHandler = new TerritoryHandler();
        Territory terr = tHandler.getTerritory( uSession.getUserFilter() );	
     
         System.out.println("Gettttttttting session"+uSession.getCurrentSlice());
        
        /* Added for Detail report */
        if(uSession.getCurrentSlice()==null || uSession.getCurrentSlice()=="")
        {
             P2lHandler p2l = new P2lHandler();
            P2lTrack track = uSession.getTrack();

            PieChart chart=null;
            List charts = new ArrayList();
            Collection result = new ArrayList(); 
            P2lTrackPhase phase=null;
            // This loops through the P2lTrack and P2lTrackPhase Obejcts and gets each pie chart.
           /* for (Iterator it = track.getCompletePhaseList().iterator(); it.hasNext();) {
                phase = (P2lTrackPhase)it.next();
                System.out.println("track:" + phase.getTrack().getTrackType());
                if ( P2lTrackPhase.EMPTY_FLAG.equals(phase.getPhaseNumber()) ) {
                    ChartDetailWc chartDetailWc = new ChartDetailWc();
                    chartDetailWc.setLayout(ChartDetailWc.LAYOUT_EMPTY);
                    charts.add(chartDetailWc);
                } else {
                     //chart = getPhaseChart(phase, uFilter, phase.getAlttActivityId(), result, false, ""); 
                    //added for RBU
                    chart = getPhaseChart(ut,phase,uFilter,phase.getAlttActivityId(),result, false, ""); 
                    //ended for RBU
                    if ( chart.getCount() > 0 ) {
                        ChartDetailWc chartDetailWc = new ChartDetailWc( chart, phase.getPhaseNumber() ,new ChartP2lLegendWc( phase.getRootActivityId(),  phase) );
                        charts.add(chartDetailWc);            
                    }   else {
                        ChartDetailWc chartDetailWc = new ChartDetailWc();
                        chartDetailWc.setChart(chart);
                        chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
                        charts.add(chartDetailWc);
                    }
                }             
            } */
              /*Getting the charts object from the session */
            charts= (List)getRequest().getSession().getAttribute("P2lCurrentChart");
          
            if ( track.getDoOverall() ) {
                chart = getOverallChart(track, uFilter, "", result, false, "",track.getAllNodesDelimit());
                phase = (P2lTrackPhase)track.getPhases().get(0);
                if ( chart.getCount() > 0 ) {
                    ChartDetailWc chartDetailWc = new ChartDetailWc(chart,"Overall",new ChartP2lLegendWc( "Overall",  phase )  );
                    chartDetailWc.setChart(chart);
                    charts.add(chartDetailWc);
                }   else {
                        ChartDetailWc chartDetailWc = new ChartDetailWc();
                        chartDetailWc.setChart(chart);
                        chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
                        charts.add(chartDetailWc);
                }
            }
                         
            ChartListWc chartListWc = new ChartListWc(charts);
            chartListWc.setLayout(ChartListWc.LAYOUT_2COL);
                    
            EmptyPageWc empty = new EmptyPageWc();
          /*  GenericChartHeaderWc headerWc = new GenericChartHeaderWc(terr.getAreaDesc(),
                                                                        terr.getRegionDesc(),
                                                                        terr.getRegionDesc(), 
                                                                        uSession.getUserFilterForm().getTeamDesc());
           */
            GenericChartHeaderWc headerWc =new GenericChartHeaderWc(uSession.getUser().getBusinessUnit(),uSession.getUser().getSalesPostionDesc(), uSession.getUser().getGeoType(), uSession.getUser().getSalesOrganization()); 
            //headerWc.setRightWc(empty);
            P2lBreadCrumbWc crumb = new P2lBreadCrumbWc(track);
            headerWc.setLeftWc(crumb);
            
            
            EmplSearchForm eForm = new EmplSearchForm();
            SearchFormWc searchFormWc = new SearchFormWc(eForm);
            searchFormWc.setPostUrl("searchemployee");
            searchFormWc.setTarget("myW");
            searchFormWc.setOnSubmit("DoThis12()");
            EmployeeSearchWc esearch = new EmployeeSearchWc(eForm,new ArrayList() );
            esearch.setSearchForm(searchFormWc);
    
    
            GenericRightBarWc rightBar = new GenericRightBarWc(territorySelect,uSession.getUser());
            rightBar.setBottomWc(esearch);
            
            ChartIndexWc main = new ChartIndexWc( headerWc, chartListWc, rightBar );
                    
            PageBuilder builder = new PageBuilder();
            MainTemplateWpc page = builder.buildPagePoa2( main, "Chart Index",uSession.getUser(), "reportselect" );
            getRequest().setAttribute( WebComponent.ATTRIBUTE_NAME, page );     
        }
        else
        {
                String activityPk = uSession.getCurrentActivity();
                System.out.println("Inside else of getNextLevel---------"+activityPk);  
                System.out.println("Inside else of getNextLevel---------"+uSession.getCurrentSlice());  
                
                uFilter.getQuseryStrings().setActivitypk(activityPk);
                uFilter.getQuseryStrings().setSection(uSession.getCurrentSlice());
                    
                uSession.setCurrentSlice( uFilter.getQuseryStrings().getSection() );
              //  String activityPk = uFilter.getQuseryStrings().getActivitypk();
                
                Collection result = new ArrayList();
        
                P2lHandler p2l = new P2lHandler();  
                P2lTrackPhase trackPhase = null;
                String label = "";
                PieChart chart;
                P2lTrack track = uSession.getTrack();
                if ("Overall".equals(activityPk)) {
                    label = "Overall";
                    chart = getOverallChart(track,uFilter,"",result, true, uSession.getUser().getId(),track.getAllNodesDelimit()); //441818     
                    trackPhase = (P2lTrackPhase)track.getPhases().get(0);       
                } else {
                    trackPhase = p2l.getTrackPhase(activityPk,track.getTrackId());
                    label = trackPhase.getPhaseNumber();
                    trackPhase.setTrack(track);
                    //chart = getPhaseChart(trackPhase,uFilter,trackPhase.getAlttActivityId(),result, true, null); //441818
                    trackPhase.setTrack(track);
                    //added for RBU                
                    //chart = getPhaseChart(ut,trackPhase,uFilter,trackPhase.getAlttActivityId(),result, true, null); //441818
                    //ended for RBU
                }
                /*Getting the chart object from the session  */
                chart= (PieChart)getSession().getAttribute("P2lListReportChart");
            
        
                int layout = ChartLegendWc.LAYOUT_PHASE;
                if ( trackPhase != null && trackPhase.getApprovalStatus()) {
                    layout = ChartLegendWc.LAYOUT_PHASE_PENDING;
                }        
                ChartDetailWc chartDetailWc=null;
                
                if ( chart.getCount() > 0 ) {
                    chartDetailWc = new ChartDetailWc( chart, label ,new ChartP2lLegendWc( uFilter.getQuseryStrings().getActivitypk() + "", trackPhase ) );        
                } else {
                    chartDetailWc = new ChartDetailWc();
                    chartDetailWc.setChart(chart);
                    chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);            
                }    
                
                Collection filteredList = new ArrayList();
              /*  for ( Iterator it = result.iterator(); it.hasNext(); ){
                    P2lEmployeeStatus tmp = (P2lEmployeeStatus)it.next();
                    if ( uFilter.getQuseryStrings().getSection().equals(tmp.getStatus()) ) {
                        filteredList.add(tmp);
                    }
                } */
                /*Getting the Filtered list object from the session  */
                filteredList= (ArrayList)getRequest().getSession().getAttribute("P2lFilteredList");
                
                PageBuilder builder = new PageBuilder();
                MainReportListReportAreaWc mainArea = new MainReportListReportAreaWc(filteredList,uSession.getUser(), activityPk,uFilter.getQuseryStrings().getSection());
                MassEmailWc emWc = new MassEmailWc(uSession.getUser());
                emWc.setEmailSubject(track.getTrackLabel() + " Follow-up");
                mainArea.setMassEmailWc(emWc);
                MainReportListWc main = new MainReportListWc(new MainReportListChartAreaWc(chartDetailWc),
                                                                new MainReportListSelectAreaWc(uSession.getUser(),uFilter),
                                                                mainArea);
                main.setPageName(label);
                main.setActivityId(activityPk);
                main.setSlice(uFilter.getQuseryStrings().getSection());
                main.setTrack(track);
                getRequest().getSession().setAttribute("p2ldownloadexcelfilter",filteredList);
                
                if ( "true".equals( uFilter.getQuseryStrings().getDownloadExcel() ) ) {
                    MainReportListReportAreaWc reportList = new MainReportListReportAreaWc(filteredList,uSession.getUser(), activityPk + "",uFilter.getQuseryStrings().getSection());
                    reportList.setLayout(MainReportListReportAreaWc.LAYOUT_XLS);
                    BlankTemplateWpc page = new BlankTemplateWpc(reportList);
                    
                    getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );	
                    getResponse().addHeader("content-disposition","attachment;filename=trainingreports.xls");
                    
                    getResponse().setContentType("application/vnd.ms-excel");	
                    getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
                    getResponse().setHeader("Pragma","public");		
                    
                    /*Infosys - Weblogic to Jboss Migrations changes start here*/
        	        //return new Forward("successXls");
        	        return ("successXls");
        	        /*Infosys - Weblogic to Jboss Migrations changes end here*/			
                }
                        
                MainTemplateWpc page = builder.buildPageP2l( main, "Report List",uSession.getUser(), "reportselect" );
                getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );                      
            
        }
        
        /*Infosys - Weblogic to Jboss Migrations changes start here*/
        //return new Forward("success");
        return ("success");
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
