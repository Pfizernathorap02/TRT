package com.pfizer.action;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.BUnitBean;
import com.pfizer.db.Employee;
import com.pfizer.db.GroupAccessDetail;
import com.pfizer.db.P2lActivityStatus;
import com.pfizer.db.P2lEmployeeStatus;
import com.pfizer.db.P2lTrack;
import com.pfizer.db.P2lTrackPhase;
import com.pfizer.db.RoleBean;
import com.pfizer.db.SalesOrgBean;
import com.pfizer.db.StatusSubSetBean;
import com.pfizer.db.SubActivityBean;
import com.pfizer.db.Territory;
import com.pfizer.db.UserGroups;
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
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.EmployeeGapReportWc;
import com.pfizer.webapp.wc.components.TrainingPathDisplayWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartIndexWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.chart.ChartP2lPhaseLegendWc;
import com.pfizer.webapp.wc.components.chart.RightBarPl2Wc;
import com.pfizer.webapp.wc.components.report.global.EmployeeInfoWc;
import com.pfizer.webapp.wc.components.report.global.MassEmailWc;
import com.pfizer.webapp.wc.components.report.phasereports.CourseCompletionResultWc;
import com.pfizer.webapp.wc.components.report.phasereports.CourseCompletionWc;
import com.pfizer.webapp.wc.components.report.phasereports.CourseSearchForm;
import com.pfizer.webapp.wc.components.report.phasereports.CourseSearchFormWc;
import com.pfizer.webapp.wc.components.report.phasereports.DetailPageWc;
import com.pfizer.webapp.wc.components.report.phasereports.DrillDownAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.EmplSearchResultWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportAllStatusWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListChartAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListFilterSelectAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListReportAreaHQWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListReportAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListWc;
import com.pfizer.webapp.wc.components.report.phasereports.PhaseTrainingDetailWc;
import com.pfizer.webapp.wc.components.report.phasereports.SCEevaluate;
import com.pfizer.webapp.wc.components.report.phasereports.SceCodesWc;
import com.pfizer.webapp.wc.components.report.phasereports.SpecialCodesWc;
import com.pfizer.webapp.wc.components.report.phasereports.TrainingSummaryWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchWc;
import com.pfizer.webapp.wc.components.search.SearchFormWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.pfizer.webapp.wc.util.PageBuilder;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.printing.LoggerHelper;
import com.tgix.wc.WebComponent;

public class PhaseControllerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private TransactionDB trDb = new TransactionDB();
	Map masterMap = new HashMap();

	public HttpSession getSession() {
		return getRequest().getSession();
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

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
		// TODO Auto-generated method stub
		this.request = request;
	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward begin(){
	 */
	public String begin() {
		try {

			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			return charts();
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward charts(){
	 */
	public String charts() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}
			Timer timer = new Timer();
			UserSession uSession = buildUserSession();
			UserFilter uFilter = uSession.getUserFilter();

			// added for RBU
			uFilter.setRefresh(true);
			uSession.setCurrentSlice("");
			UserTerritory ut = uSession.getUser().getUserTerritory();

			// ended for RBU
			// System.out.println("IS MULTIPLE GEOS"+uSession.getUser().isMultipleGeos());
			// getRequest().getSession().setAttribute("Current Chart",null);

			TerritorySelectWc territorySelect = new TerritorySelectWc(
					uSession.getUserFilterForm(), uSession.getUser()
							.getUserTerritory(), "/TrainingReports/phase/begin");
			/*
			 * if(uSession.getUser().isMultipleGeos()==true) {
			 * territorySelect.setShowMultipleGeos(true); }
			 */

			territorySelect.setShowTeam(true);
			territorySelect.setLayout(3);
			Territory terr = Service.getServiceFactory().getTerritoryHandler()
					.getTerritory(uFilter);

			List charts = new ArrayList();

			Collection result = new ArrayList();

			P2lHandler p2l = new P2lHandler();

			P2lTrack track = uSession.getTrack();
			if (track == null
					|| !Util.isEmpty(uFilter.getQuseryStrings().getTrack())) {
				track = p2l.getTrack(uFilter.getQuseryStrings().getTrack());
				uSession.setTrack(track);
			}
			PieChart chart = null;
			P2lTrackPhase phase = null;
			for (Iterator it = track.getCompletePhaseList().iterator(); it
					.hasNext();) {
				phase = (P2lTrackPhase) it.next();
				if (P2lTrackPhase.EMPTY_FLAG.equals(phase.getPhaseNumber())) {
					ChartDetailWc chartDetailWc = new ChartDetailWc();
					chartDetailWc.setLayout(ChartDetailWc.LAYOUT_EMPTY);
					charts.add(chartDetailWc);
				} else {
					// chart =
					// getPhaseChart(phase,label,uFilter,phase.getAlttActivityId(),result,
					// false, ""); //473181
					// added for RBU
					chart = getPhaseChart(ut, phase, uFilter,
							phase.getAlttActivityId(), result, false, "");
					// ended for RBU
					if (chart.getCount() > 0) {
						int layout = ChartLegendWc.LAYOUT_PHASE;
						if (phase.getApprovalStatus()) {
							layout = ChartLegendWc.LAYOUT_PHASE_PENDING;
						}
						ChartDetailWc chartDetailWc = new ChartDetailWc(chart,
								phase.getPhaseNumber(),
								new ChartP2lPhaseLegendWc(phase
										.getRootActivityId(), phase, chart));
						// Added for Major Enhancement 3.6 - F1
						chartDetailWc
								.setP2lPhaseChartURL("listReportAllStatus?activitypk="
										+ phase.getRootActivityId());
						// ends here
						charts.add(chartDetailWc);
					} else {
						ChartDetailWc chartDetailWc = new ChartDetailWc();
						chartDetailWc.setChart(chart);
						chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
						charts.add(chartDetailWc);
					}
				}
			}
			if (track.getDoOverall()) {
				chart = getOverallChart(track, uFilter, "", result, false, "",
						track.getAllNodesDelimit());
				phase = (P2lTrackPhase) track.getPhases().get(0);
				if (chart.getCount() > 0) {
					ChartDetailWc chartDetailWc = new ChartDetailWc(chart,
							"Overall", new ChartP2lPhaseLegendWc("Overall",
									phase, chart));
					chartDetailWc.setChart(chart);
					// Added for Major Enhancement 3.6 - F1
					chartDetailWc
							.setP2lPhaseChartURL("listReportAllStatus?activitypk="
									+ phase.getRootActivityId());
					// ends here
					charts.add(chartDetailWc);
				} else {
					ChartDetailWc chartDetailWc = new ChartDetailWc();
					chartDetailWc.setChart(chart);
					chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
					charts.add(chartDetailWc);
				}
			}
			/* Setting the session */
			getRequest().getSession().setAttribute("P2lCurrentChart", charts);

			ChartListWc chartListWc = new ChartListWc(charts);
			chartListWc.setLayout(ChartListWc.LAYOUT_2COL);
			// this component puts the header info in the chart page.
			// ChartHeaderWc chartHeaderWc = new ChartHeaderWc(
			// terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(),
			// 10);
			// GenericChartHeaderWc headerWc =new
			// GenericChartHeaderWc(uSession.getUser().getBusinessUnit(),uSession.getUser().getSalesPostionDesc(),
			// uSession.getUser().getGeoType(),
			// uSession.getUser().getSalesOrganization());
			ChartHeaderWc chartHeaderWc = new ChartHeaderWc(uSession.getUser()
					.getBusinessUnit(), uSession.getUser()
					.getSalesPostionDesc(), uSession.getUser().getGeoType(), 10);

			chartHeaderWc.setJsp(AppConst.JSP_LOC
					+ "/components/chart/chartHeaderBasic.jsp");
			// P2lBreadCrumbWc crumb = new P2lBreadCrumbWc(track);
			// headerWc.setLeftWc(crumb);

			// Start: Modified for TRT 3.6 enhancement - F 4.1-(additional
			// search fields)
			EmplSearchForm eForm = new EmplSearchForm();
			if (eForm.getBuList().size() <= 0) {
				BUnitBean[] allBu = null;
				allBu = trDb.getAllBusinessUnits();
				LabelValueBean labelValueBean;
				eForm.setBu("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setBuList(labelValueBean);
				for (int i = 0; i < allBu.length; i++) {
					labelValueBean = new LabelValueBean(
							allBu[i].getBunitDesc(), allBu[i].getBunitDesc());
					eForm.setBuList(labelValueBean);
				}
			}
			if (eForm.getRoleList().size() <= 0) {
				RoleBean[] allRoles = null;
				allRoles = trDb.getAllRoleDesc();
				LabelValueBean labelValueBean;
				eForm.setRole("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setRoleList(labelValueBean);
				for (int i = 0; i < allRoles.length; i++) {
					labelValueBean = new LabelValueBean(
							allRoles[i].getRoleDesc(), allRoles[i].getRoleCd());
					eForm.setRoleList(labelValueBean);
				}
			}
			// End: Modified for TRT 3.6 enhancement - F 4.1-(additional search
			// fields)
			SearchFormWc searchFormWc = new SearchFormWc(eForm);
			searchFormWc.setPostUrl("searchemployee");
			searchFormWc.setTarget("myW");
			searchFormWc.setOnSubmit("DoThis12()");
			EmployeeSearchWc esearch = new EmployeeSearchWc(eForm,
					new ArrayList());
			esearch.setSearchForm(searchFormWc);

			// GenericRightBarWc rightBar = new
			// GenericRightBarWc(territorySelect,uSession.getUser());
			// rightBar.setBottomWc(esearch);

			RightBarPl2Wc right = new RightBarPl2Wc(territorySelect,
					uSession.getUser(), esearch);
			chartHeaderWc.setTrack(track);
			ChartIndexWc main = new ChartIndexWc(chartHeaderWc, chartListWc,
					right, 10);

			PageBuilder builder = new PageBuilder();
			MainTemplateWpc page = builder.buildPagePoa2(main, "Chart Index",
					uSession.getUser(), "reportselect");
			getRequest().setAttribute(WebComponent.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	private UserSession buildUserSession() {
		UserSession uSession = UserSession.getUserSession(getRequest());

		TerritoryFilterForm filterForm = uSession.getUserFilterForm();

		/*
		 * if(uSession.getUserFilterForm().getTeamList().size()<=0){ TeamBean[]
		 * allTeam=null;
		 * 
		 * if ( uSession.getUser().isAdmin() || uSession.getUser().isTsrAdmin())
		 * { allTeam=trDb.getAllTEAM(); } else {
		 * allTeam=trDb.getTEAMBYCLUSTER(uSession.getUser().getCluster()); }
		 * LabelValueBean labelValueBean ;
		 * uSession.getUserFilterForm().setTeam("All");
		 * 
		 * FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
		 * labelValueBean=new LabelValueBean("All","All");
		 * uSession.getUserFilterForm().setTeamList(labelValueBean); for(int
		 * i=0;i<allTeam.length;i++){ labelValueBean=new
		 * LabelValueBean(allTeam[i].getTeamDesc(),allTeam[i].getTeamCd());
		 * uSession.getUserFilterForm().setTeamList(labelValueBean); } }
		 */

		/* Modified for displaying the Sales Org drop down */
		if (uSession.getUserFilterForm().getSalesOrgList().size() <= 0) {
			SalesOrgBean[] allSalesOrg = null;

			if (uSession.getUser().isAdmin() || uSession.getUser().isTsrAdmin()) {
				allSalesOrg = trDb.getAllSALESORG();
			} else {
				allSalesOrg = trDb.getSALESORGBYUSER(uSession.getUser()
						.getEmplid());
			}
			LabelValueBean labelValueBean;
			uSession.getUserFilterForm().setSalesOrg("All");

			FormUtil.loadObject(getRequest(), uSession.getUserFilterForm());
			labelValueBean = new LabelValueBean("All", "All");
			uSession.getUserFilterForm().setSalesOrgList(labelValueBean);
			for (int i = 0; i < allSalesOrg.length; i++) {
				labelValueBean = new LabelValueBean(
						allSalesOrg[i].getSalesOrgDesc(),
						allSalesOrg[i].getSalesOrgCd());
				uSession.getUserFilterForm().setSalesOrgList(labelValueBean);
			}
		}

		if (uSession.getUserFilterForm().getFirstList().size() <= 0) {
			ArrayList firstSalesPos = new ArrayList();
			firstSalesPos = uSession.getUser().getUserTerritory()
					.getFirstDropdown();
			LabelValueBean labelValueBean;
			uSession.getUserFilterForm().setLevel1("All");
			FormUtil.loadObject(getRequest(), uSession.getUserFilterForm());
			labelValueBean = new LabelValueBean("All", "All");
			uSession.getUserFilterForm().setFirstList(labelValueBean);
			if (firstSalesPos != null) {
				for (int i = 0; i < firstSalesPos.size(); i++) {
					LabelValueBean tempLabelValueBean;
					tempLabelValueBean = (LabelValueBean) firstSalesPos.get(i);
					labelValueBean = new LabelValueBean(
							(String) tempLabelValueBean.getLabel(),
							(String) tempLabelValueBean.getValue());
					uSession.getUserFilterForm().setFirstList(labelValueBean);
				}
			}

		}
		/*
		 * if(uSession.getUser().isMultipleGeos()==true) {
		 * uSession.getUserFilterForm().clearMultipleGeos(); ArrayList
		 * mulGeos=new ArrayList(); mulGeos=
		 * uSession.getUser().getMultipleGeoList();
		 * System.out.println("/nMULTIPLE GEOS IN PHASE CONTROLLER"
		 * +mulGeos.size());
		 * System.out.println("/nGEOGRAPHY ID IN PHASE CONTROLLER"
		 * +uSession.getUser().getGeographyId()); LabelValueBean labelValueBean
		 * ; uSession.getUserFilterForm().setSelectedGeo(uSession.getUser().
		 * getGeographyId());
		 * FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
		 * //labelValueBean=new
		 * LabelValueBean(uSession.getUser().getGeographyDesc
		 * (),uSession.getUser().getGeographyId());
		 * //uSession.getUserFilterForm().setMultipleGeoList(labelValueBean);
		 * for(int i=0;i<mulGeos.size();i++){ LabelValueBean tempLabelValueBean;
		 * tempLabelValueBean = (LabelValueBean)mulGeos.get(i);
		 * labelValueBean=new
		 * LabelValueBean((String)tempLabelValueBean.getLabel(
		 * ),(String)tempLabelValueBean.getValue());
		 * uSession.getUserFilterForm().setMultipleGeoList(labelValueBean); } }
		 */

		FormUtil.loadObject(getRequest(), filterForm);

		// process query stings
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(), qStrings);
		FormUtil.loadObject(getRequest(), qStrings.getSortBy());

		qStrings.setFullQueryString(getRequest().getQueryString());
		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setEmployeeId(uSession.getUser().getEmplid());
		uFilter.setTsrAdmin(uSession.getUser().isTsrAdmin());
		uFilter.setAdmin(uSession.getUser().isAdmin());
		// uFilter.setClusterCode(uSession.getUser().getCluster());
		uFilter.setFilterForm(filterForm);
		uFilter.setQueryStrings(qStrings);
		// Added for TRT Phase 2 - Requirement F3
		uFilter.setHqUser(uSession.getUser().isHQUser());
		uFilter.setIsSpecialRoleUser(uSession.getUser().isSpecialRole());
		uFilter.setIsSpecialRoleUser(uSession.getUser().isSpecialRole());
		uFilter.setEmployeeIdForSplRole(uSession.getUser().getEmplIdForSpRole());

		return uSession;
	}

	public PieChart getPhaseChart(UserTerritory ut, P2lTrackPhase phase,
			UserFilter uFilter, String altNode, Collection employees,
			boolean detailflag, String emplid) {
		StringBuffer sb = new StringBuffer();
		Timer timer = new Timer();
		P2lHandler p2l = new P2lHandler();
		boolean rmFlag = false;
		if ("POA".equals(phase.getTrackId())) {
			rmFlag = true;
		}

		// Collection master = p2l.getPhaseStatus(phase, uFilter,detailflag,"");
		// added for RBU
		// Infosys Coding Modification Starts
		Collection master;
		if (masterMap != null && masterMap.size() > 0
				&& masterMap.get(phase.getPhaseNumber()) != null) {
			LoggerHelper
					.logSystemDebug("$$$ get master map from Collection object.");
			master = (Collection) masterMap.get(phase.getPhaseNumber());// p2l.getPhaseStatus(ut,
																		// phase,
																		// uFilter,detailflag,"");
		} else {
			master = p2l.getPhaseStatus(ut, phase, uFilter, detailflag, "");
			masterMap.put(phase.getPhaseNumber(), master);
		}
		// Infosys Coding Modification Ends Here
		// Collection sub = p2l.getSubSetPhaseStatus(ut, phase,
		// uFilter,detailflag,"");
		Collection subset = new ArrayList();

		// Added for Major Enhancement 3.6 for CSO impact
		/*
		 * UserSession uSession = buildUserSession();
		 * uSession.getUser().setScoresFlag(""); EmployeeHandler eHandler = new
		 * EmployeeHandler(); Employee emp = eHandler.getEmployeeById(
		 * uFilter.getQuseryStrings().getEmplid() );
		 */
		// Ends here

		// ended for RBU
		int registered = 0;
		int assigned = 0;
		int exempt = 0;
		int complete = 0;
		int onleave = 0;
		int pending = 0;
		int notComplete = 0;
		// added for major enhancement
		int cancelled = 0;
		// ends here
		LoggerHelper.logSystemDebug("inside get phasechart1");
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

			if ("Registered".equals(st.getStatus())
					|| "In Progress".equals(st.getStatus())
					|| "Cancelled".equals(st.getStatus())
					|| "No Show".equals(st.getStatus())
					|| "Pending".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Not Complete");
					notComplete++;
				}
			} else if ("Waived".equals(st.getStatus())
					|| "Complete".equals(st.getStatus())) {// ||
															// "RegisteredC".equals(st.getStatus())){

				/*
				 * if("On-Leave".equals(st.getEmployee().getEmployeeStatus())){
				 * st.setStatus("On-Leave"); st.setStatusToDisplay("On-Leave");
				 * onleave++; }
				 */
				// else{
				st.setStatusToDisplay("Complete");
				complete++;
				// }

			} else if ("Assigned".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Assigned");
					assigned++;
				}

			}

			/*
			 * if ("Registered".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { registered++; } }
			 * else if ("Assigned".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { assigned++; } }
			 * else if ("Waived".equals(st.getStatus())) { // Added for P2L
			 * issues data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ exempt++; } }
			 * //added for major enhancement else if
			 * ("Cancelled".equals(st.getStatus())) { // Added for P2L issues
			 * data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ cancelled++; } }
			 * //ends here else if ("Pending".equals(st.getStatus())) {
			 * pending++; } else if ("RegisteredC".equals(st.getStatus())) {
			 * st.setStatus("Complete"); complete++; } // Added for 'In
			 * Progress' status by Meenakshi else if
			 * ("In Progress".equals(st.getStatus())){ registered++; } else {
			 * complete++; }
			 */
		}

		HashMap result = new HashMap();
		PieChart chart = null;
		List data = new ArrayList();
		Map tMap = new HashMap();
		// Added for Major Enhancement 3.6 - F1
		PieChartBuilder builder = new PieChartBuilder();
		StatusSubSetBean b = getStatusSubsetDetails(uFilter);
		LoggerHelper.logSystemDebug("inside get phasechart Subset");
		/*
		 * data.add(new ChartData( "Complete", complete ) ); //added for major
		 * enhancement data.add(new ChartData( "Cancelled", cancelled ) );
		 * //ends here if ( phase.getExempt() ) { data.add(new ChartData(
		 * "Waived", exempt ) ); tMap.put( "Waived", new Color(255,153,0) ); }
		 * if ( phase.getAssigned()) { data.add(new ChartData( "Assigned",
		 * assigned) ); tMap.put( "Assigned", new Color(153,204,0) ); } if (
		 * phase.getApprovalStatus() ) { tMap.put( "Pending", new
		 * Color(255,51,102) ); data.add(new ChartData( "Pending", pending ) );
		 * } else { data.add(new ChartData( "Registered", registered ) ); }
		 * data.add(new ChartData( "On-Leave", onleave ) );
		 * 
		 * 
		 * 
		 * 
		 * tMap.put( "Complete", AppConst.COLOR_BLUE ); //Added for Major
		 * Enhancement 3.6 - F1 tMap.put( "Cancelled", AppConst.COLOR_CYAN );
		 * //ends here tMap.put( "Registered", new Color(153,51,0) ); tMap.put(
		 * "On-Leave", new Color(253,243,156) );
		 */
		data.add(new ChartData("Complete", complete));
		data.add(new ChartData("Assigned", assigned));
		data.add(new ChartData("Not Complete", notComplete));
		data.add(new ChartData("On-Leave", onleave));
		tMap.put("Complete", AppConst.COLOR_BLUE);
		tMap.put("On-Leave", new Color(253, 243, 156));
		tMap.put("Assigned", new Color(153, 204, 0));
		tMap.put("Not Complete", new Color(153, 51, 0));
		Map colorMap = Collections.unmodifiableMap(tMap);
		// Added for Major Enhancement 3.6 for CSO impact
		/*
		 * if( uSession.getUser().getScoresVisible().equals("Y") ||
		 * uSession.getUser
		 * ().getSalesPositionTypeCd().equals(emp.getSalesPostionTypeCode())) {
		 * uSession.getUser().setScoresFlag("Y"); }
		 * 
		 * System.out.println("Scores Flag"+uSession.getUser().getScoresFlag());
		 */
		// Ends here

		String ak = phase.getRootActivityId();
		String label = phase.getTrack().getTrackLabel() + " : "
				+ phase.getPhaseNumber();
		// chart =
		// builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk="
		// + ak ,getRequest().getSession(), colorMap);
		// Added for Major Enhancement 3.6 - F1
		LoggerHelper.logSystemDebug("chart section now"
				+ uFilter.getQuseryStrings().getSection());
		String sec = uFilter.getQuseryStrings().getSection();

		chart = builder.generate(data, null, label,
				"listReportAllStatus?activitypk=" + phase.getRootActivityId(),
				getRequest().getSession(), colorMap);
		LoggerHelper.logSystemDebug("aftr builder ");
		chart.setCount(complete + onleave + assigned + notComplete);
		chart.setAssigned(assigned);
		chart.setCompleted(complete);
		chart.setOnleave(onleave);
		chart.setNotComplete(notComplete);
		employees.addAll(master);
		// chart.setCount( complete + exempt + assigned + registered + onleave +
		// cancelled);
		/*
		 * Added for Phase 1 by Meenakshi chart.setCompleted(complete);
		 * chart.setExempt(exempt); chart.setAssigned(assigned);
		 * chart.setRegistered(registered); chart.setOnleave(onleave);
		 * chart.setCancelled(cancelled); /* End of addition
		 */
		// ends here
		// Added for Major Enhancement 3.6 - F1
		// }
		return chart;
	}

	public PieChart getOverallChart(P2lTrack track, UserFilter uFilter,
			String altNode, Collection employees, boolean detailflag,
			String emplid, String otherNodes) {
		StringBuffer sb = new StringBuffer();
		Timer timer = new Timer();
		P2lHandler p2l = new P2lHandler();
		boolean rmFlag = false;
		if ("POA".equals(track.getTrackId())) {
			rmFlag = true;
		}
		Collection master = p2l.getOveralStatus(track, uFilter, true);
		int registered = 0;
		int assigned = 0;
		int exempt = 0;
		int complete = 0;
		int onleave = 0;
		int pending = 0;
		int notComplete = 0;
		// Added for Major Enhancement 3.6 - F1

		int cancelled = 0;
		// ends here
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

			if ("Registered".equals(st.getStatus())
					|| "In Progress".equals(st.getStatus())
					|| "Cancelled".equals(st.getStatus())
					|| "No Show".equals(st.getStatus())
					|| "Pending".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Not Complete");
					notComplete++;
				}
			} else if ("Waived".equals(st.getStatus())
					|| "Complete".equals(st.getStatus())) {// ||
															// "RegisteredC".equals(st.getStatus())){

				/*
				 * if("On-Leave".equals(st.getEmployee().getEmployeeStatus())){
				 * st.setStatus("On-Leave"); st.setStatusToDisplay("On-Leave");
				 * onleave++; } else{
				 */
				st.setStatusToDisplay("Complete");
				complete++;
				// }

			} else if ("Assigned".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Assigned");
					assigned++;
				}

			}
			/*
			 * if ("Registered".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { registered++; } }
			 * else if ("Assigned".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { assigned++; } }
			 * //End: for Major Enhancement 3.6 - F1
			 * 
			 * else if ("Cancelled".equals(st.getStatus())) { // Added for P2L
			 * issues data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ cancelled++; } } //
			 * ends here else if ("Waived".equals(st.getStatus())) { // Added
			 * for P2L issues data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ exempt++; } } else
			 * if ("Pending".equals(st.getStatus())) { pending++; } else if
			 * ("RegisteredC".equals(st.getStatus())) {
			 * st.setStatus("Complete"); complete++; } // Added for 'In
			 * Progress' status by Meenakshi else if
			 * ("In Progress".equals(st.getStatus())){ registered++; } else {
			 * complete++; }
			 */
		}

		HashMap result = new HashMap();
		P2lTrackPhase phase = (P2lTrackPhase) track.getPhases().get(0);

		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();

		data.add(new ChartData("Complete", complete));
		data.add(new ChartData("Assigned", assigned));
		data.add(new ChartData("Not Complete", notComplete));
		data.add(new ChartData("On-Leave", onleave));
		tMap.put("Complete", AppConst.COLOR_BLUE);
		tMap.put("On-Leave", new Color(253, 243, 156));
		tMap.put("Assigned", new Color(153, 204, 0));
		tMap.put("Not Complete", new Color(153, 51, 0));
		/*
		 * data.add(new ChartData( "Complete", complete ) ); //Added for Major
		 * Enhancement 3.6 - F1
		 * 
		 * data.add(new ChartData( "Cancelled", cancelled ) ); //ends here if
		 * (phase.getExempt()) { data.add(new ChartData( "Waived", exempt ) );
		 * tMap.put( "Waived", new Color(255,153,0) ); } if
		 * (phase.getAssigned()) { data.add(new ChartData( "Assigned", assigned)
		 * ); tMap.put( "Assigned", new Color(153,204,0) ); } data.add(new
		 * ChartData( "Registered", registered ) ); data.add(new ChartData(
		 * "On-Leave", onleave ) );
		 */

		PieChartBuilder builder = new PieChartBuilder();

		/*
		 * 
		 * tMap.put( "Complete", AppConst.COLOR_BLUE );
		 * 
		 * //Added for Major Enhancement 3.6 - F1
		 * 
		 * tMap.put( "Cancelled", AppConst.COLOR_CYAN ); //ends here tMap.put(
		 * "Registered", new Color(153,51,0) );
		 * 
		 * tMap.put( "On-Leave", new Color(253,243,156) );
		 */
		Map colorMap = Collections.unmodifiableMap(tMap);

		String ak = "";
		String label = "";
		if (!Util.isEmpty(otherNodes)) {
			ak = "Overall";
			label = "Overall";
		}
		// Added for Major Enhancement 3.6 - F1
		chart.setCount(complete + onleave + assigned + notComplete);
		chart.setAssigned(assigned);
		chart.setCompleted(complete);
		chart.setOnleave(onleave);
		chart.setNotComplete(notComplete);
		// chart =
		// builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk="
		// + ak ,getRequest().getSession(), colorMap);
		// chart.setCount( complete + exempt + assigned + registered + onleave +
		// cancelled);
		chart = builder.generate(data, uFilter.getQuseryStrings().getSection(),
				label, "listReportAllStatus?activitypk=" + ak, getRequest()
						.getSession(), colorMap);
		// ends here
		employees.addAll(master);
		return chart;
	}

	StatusSubSetBean getStatusSubsetDetails(UserFilter uFilter) {
		StatusSubSetBean b = new StatusSubSetBean();
		b.setChkStatus(uFilter.getFilterForm().getChkStatus());

		String chkStatus = uFilter.getFilterForm().getChkStatus();
		String statusFromDate = uFilter.getFilterForm().getFromDate();
		String statusToDate = uFilter.getFilterForm().getToDate();

		String str = getRequest().getParameter("complete");

		LoggerHelper.logSystemDebug("inside subsetdetails" + statusFromDate);
		LoggerHelper.logSystemDebug("inside subsetdetails1" + statusToDate);

		if ((statusFromDate == null || statusFromDate.trim().length() == 0)
				&& (statusToDate == null || statusToDate.trim().length() == 0)) {

			b.setStatusSubsetSelected(false);
			LoggerHelper.logSystemDebug("date null");
		} else {
			b.setStatusSubsetSelected(true);

			Date dt = null;
			DateFormat df = new SimpleDateFormat("mm/dd/yyyy");

			if (statusFromDate != null && statusFromDate.trim().length() != 0) {

				// try
				// {
				// dt = df.parse(statusFromDate);
				// LoggerHelper.logSystemDebug("date parse"+ dt);
				b.setStatusFromDate(statusFromDate);

				// } catch (ParseException e)
				// {
				// e.printStackTrace();
				// }
			}

			if (statusToDate != null && statusToDate.trim().length() != 0) {
				// try
				// {
				// dt = df.parse(statusToDate);
				b.setStatusToDate(statusToDate);

				// } catch (ParseException e)
				// {
				// e.printStackTrace();
				// }

			}
		}

		return b;

	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward listReportAllStatus(){
	 */
	public String listReportAllStatus() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}

			UserSession uSession = buildUserSession();
			UserFilter uFilter = uSession.getUserFilter();
			uFilter.setRefresh(true);
			uFilter.getFilterForm().setChkStatus(""); // Added by Swati
			String activityPk = null;
			String currentSlice = null;

			String rootActivityID = "";

			if ((uFilter.getQuseryStrings().getActivitypk().equals("") || uFilter
					.getQuseryStrings().getActivitypk().equals("null"))) {
				System.out
						.println("\n--------------- QUERY STRINGS NULL----------------\n");
				LoggerHelper.logSystemDebug("inside activitycheck");
				activityPk = uSession.getCurrentActivity();
				uFilter.getQuseryStrings().setActivitypk(activityPk);
				System.out
						.println("\n--------------- QUERY STRINGS NULL----------------\n"
								+ activityPk);
				// currentSlice= uSession.getCurrentSlice();
				// System.out.println("\n--------------- QUERY STRINGS NULL----------------\n"+currentSlice);
				// uFilter.getQuseryStrings().setSection(currentSlice);
			} else {
				LoggerHelper.logSystemDebug("inside elseactivity");
				System.out
						.println("--------------- QUERY STRINGS NOT NULL----------------");
				activityPk = uFilter.getQuseryStrings().getActivitypk();
				// currentSlice=uFilter.getQuseryStrings().getSection();
			}

			String section = uFilter.getQuseryStrings().getSection();
			// uFilter.getFilterForm().setChkStatus("All");
			// uFilter.getFilterForm().setFromDate("");
			// uFilter.getFilterForm().setToDate("");

			if (section != null) {
				currentSlice = uFilter.getQuseryStrings().getSection();
				// currentSlice = null;
				uFilter.getQuseryStrings().setSection(null);

				if ("All".equals(currentSlice)) {
					LoggerHelper.logSystemDebug("all section" + currentSlice);
					currentSlice = null;
					uFilter.getQuseryStrings().setSection("All");

				}

			} else {
				LoggerHelper.logSystemDebug("r1 all status");
				if (uFilter.getFilterForm().getChkStatus().equals("")
						|| uFilter.getFilterForm().getChkStatus()
								.equals("null")) {
					LoggerHelper.logSystemDebug("r2");
					currentSlice = uSession.getCurrentSlice();
				} else {
					LoggerHelper.logSystemDebug("r3");
					currentSlice = uFilter.getFilterForm().getChkStatus();
					if ("All".equals(currentSlice))
						currentSlice = "";
				}
				LoggerHelper.logSystemDebug("r4");
				uFilter.getQuseryStrings().setSection(null);
			}

			uSession.setCurrentSlice(currentSlice);
			uSession.setCurrentActivity(activityPk);

			Collection result = new ArrayList();
			PieChart chart;
			String label = "";
			P2lHandler p2l = new P2lHandler();
			P2lTrackPhase trackPhase = null;
			List activityName = null;
			P2lTrack track = uSession.getTrack();
			/* Added for Phase 1 by Meenakshi */
			P2lTrackPhase subTrackPhase = null;

			// //////
			// System.out.println("track.getTrackId()=="+track.getTrackId());
			// System.out.println("activityPk=="+activityPk);

			String parentActivityPk = (String) getRequest().getSession()
					.getAttribute("parentActivityPk");
			System.out.println("parentActivityPk beginning=="
					+ parentActivityPk);
			// /////////////
			activityPk = uFilter.getQuseryStrings().getActivitypk();
			if ("Overall".equals(activityPk)) {
				label = "Overall";
				chart = getOverallChart(track, uFilter, "", result, true,
						uSession.getUser().getId(), track.getAllNodesDelimit());
				trackPhase = (P2lTrackPhase) track.getPhases().get(0);
			} else {
				trackPhase = p2l.getTrackPhase(activityPk, track.getTrackId());

				if (trackPhase != null) {
					label = trackPhase.getPhaseNumber();
					LoggerHelper.logSystemDebug("inside new9 ");
					trackPhase.setTrack(track);
					trackPhase.setTrack(track);
					UserTerritory ut = uSession.getUser().getUserTerritory();
					chart = getAllStatusPhaseChart(ut, trackPhase, uFilter,
							trackPhase.getAlttActivityId(), result, true, null);
					rootActivityID = trackPhase.getRootActivityId();
					LoggerHelper.logSystemDebug("root666" + rootActivityID);

					parentActivityPk = activityPk;
				} else {
					// Sub Activity
					SubActivityBean bean = p2l.getActivityDetails(activityPk);
					label = bean.getActivityName();
					// rootActivityID = bean.getRootActivityID();
					UserTerritory ut = uSession.getUser().getUserTerritory();
					chart = getActivityChart(ut, bean, uFilter, result);
					/* Modified for Phase 1 by Meenakshi */
					subTrackPhase = p2l.getTrackPhase(parentActivityPk,
							track.getTrackId());
					if (subTrackPhase != null) {
						rootActivityID = subTrackPhase.getRootActivityId();
					}
				}
			}
			/* Setting the session required for the getNextLevel() method */
			getRequest().getSession().setAttribute("P2lListReportChart", chart);

			int layout = ChartLegendWc.LAYOUT_PHASE;
			if (trackPhase != null && trackPhase.getApprovalStatus()) {
				layout = ChartLegendWc.LAYOUT_PHASE_PENDING;
			}
			ChartDetailWc chartDetailWc = null;
			List resultList = new ArrayList();
			resultList = p2l.getLinksForActivities(activityPk);

			if (chart.getCount() > 0) {
				// chartDetailWc = new ChartDetailWc( chart, label
				// ,null,"showReportLink",resultList);
				/* Modfified for Phase 1 by Meenakshi */
				if (trackPhase != null) {
					chartDetailWc = new ChartDetailWc(chart, label,
							new ChartP2lPhaseLegendWc(uFilter
									.getQuseryStrings().getActivitypk() + "",
									trackPhase, chart), "showReportLink",
							resultList);
				} else {
					chartDetailWc = new ChartDetailWc(chart, label,
							new ChartP2lPhaseLegendWc(uFilter
									.getQuseryStrings().getActivitypk() + "",
									subTrackPhase, chart), "showReportLink",
							resultList);
				}
				/* End of modification for Phase 1 by Meenakshi */
			} else {
				chartDetailWc = new ChartDetailWc();
				chartDetailWc.setChart(chart);
				chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			}
			uFilter.setLayoutNew("3");
			PageBuilder builder = new PageBuilder();

			// List actresult = p2l.getActivityTree(rootActivityID);

			/*
			 * Iterator itr = actresult.iterator();
			 * LoggerHelper.logSystemDebug("activity drilldown debug "); int
			 * lvl, id; String code, name; while (itr.hasNext()){ Map currMap =
			 * (Map)itr.next(); lvl =
			 * ((BigDecimal)currMap.get("LEVEL")).toBigInteger().intValue();
			 * code = (String)currMap.get("ACTIVITY_CODE"); name =
			 * (String)currMap.get("ACTIVITYNAME"); id =
			 * ((BigDecimal)currMap.get
			 * ("ACTIVITY_PK")).toBigInteger().intValue();
			 * 
			 * LoggerHelper.logSystemDebug(lvl + "," + code + "," + name + "," +
			 * id); }
			 */
			DrillDownAreaWc ddArea = null;
			boolean isTreeViewVisible = checkActivityDrilldownVisibility(uSession
					.getUser());
			if (isTreeViewVisible) {
				List actresult = p2l.getActivityTree(rootActivityID);
				ddArea = new DrillDownAreaWc(actresult);
			}
			// Start: Modified for TRT 3.6 enhancement - F 4.1 (additional
			// search attributes)
			EmplSearchForm eForm = new EmplSearchForm();

			if (eForm.getBuList().size() <= 0) {
				BUnitBean[] allBu = null;
				allBu = trDb.getAllBusinessUnits();
				LabelValueBean labelValueBean;
				eForm.setBu("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setBuList(labelValueBean);
				for (int i = 0; i < allBu.length; i++) {
					labelValueBean = new LabelValueBean(
							allBu[i].getBunitDesc(), allBu[i].getBunitDesc());
					eForm.setBuList(labelValueBean);
				}
			}
			if (eForm.getRoleList().size() <= 0) {
				RoleBean[] allRoles = null;
				allRoles = trDb.getAllRoleDesc();
				LabelValueBean labelValueBean;
				eForm.setRole("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setRoleList(labelValueBean);
				for (int i = 0; i < allRoles.length; i++) {
					labelValueBean = new LabelValueBean(
							allRoles[i].getRoleDesc(), allRoles[i].getRoleCd());
					eForm.setRoleList(labelValueBean);
				}
			}
			SearchFormWc searchFormWc = new SearchFormWc(eForm);
			searchFormWc.setPostUrl("searchemployee");
			searchFormWc.setTarget("myW");
			searchFormWc.setOnSubmit("DoThis12()");
			EmployeeSearchWc esearch = new EmployeeSearchWc(eForm,
					new ArrayList());
			esearch.setSearchForm(searchFormWc);

			// End: Modified for TRT 3.6 enhancement - F 4.1 (additional search
			// attributes)

			String fromListReportAllStatus = "fromlistreportAllStatus";
			LoggerHelper.logSystemDebug("inside listreportallstatus");
			MainReportAllStatusWc main = new MainReportAllStatusWc(
					new MainReportListChartAreaWc(chartDetailWc),
					new MainReportListFilterSelectAreaWc(uSession.getUser(),
							uFilter, trackPhase), ddArea, esearch,
					uSession.getUser());
			main.setPageName(label);
			main.setActivityId(activityPk);
			main.setTrack(track);
			getRequest().getSession().setAttribute("datecheck",
					fromListReportAllStatus);
			System.out.println("parentActivityPk==" + parentActivityPk);
			getRequest().getSession().setAttribute("parentActivityPk",
					parentActivityPk);
			getRequest().getSession().setAttribute("listlevel", null);
			MainTemplateWpc page = builder.buildPageP2l(main, "Report List",
					uSession.getUser(), "reportselect");
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	public PieChart getAllStatusPhaseChart(UserTerritory ut,
			P2lTrackPhase phase, UserFilter uFilter, String altNode,
			Collection employees, boolean detailflag, String emplid) {
		StringBuffer sb = new StringBuffer();
		Timer timer = new Timer();
		P2lHandler p2l = new P2lHandler();
		boolean rmFlag = false;
		if ("POA".equals(phase.getTrackId())) {
			rmFlag = true;
		}
		System.out.println("inside get all status phase chart");
		// Collection master = p2l.getPhaseStatus(phase, uFilter,detailflag,"");
		// added for RBU
		// Added by Swati
		LoggerHelper
				.logSystemDebug("$$$ get master map from Collection object inside getAllStatusPhaseChart.");
		/*
		 * Collection master =
		 * (Collection)masterMap.get(phase.getPhaseNumber());
		 * //p2l.getPhaseStatus(ut, phase, uFilter,detailflag,"");
		 */
		Collection master;
		if (masterMap != null && masterMap.size() > 0
				&& masterMap.get(phase.getPhaseNumber()) != null) {
			master = (Collection) masterMap.get(phase.getPhaseNumber());// p2l.getPhaseStatus(ut,
																		// phase,
																		// uFilter,detailflag,"");
		} else {
			master = p2l.getPhaseStatus(ut, phase, uFilter, detailflag, "");
			masterMap.put(phase.getPhaseNumber(), master);
		}

		System.out.println("master size" + master.size());
		// Collection sub = p2l.getSubSetPhaseStatus(ut, phase,
		// uFilter,detailflag,"");
		Collection subset = new ArrayList();

		// Added for Major Enhancement 3.6 for CSO impact
		/*
		 * UserSession uSession = buildUserSession();
		 * uSession.getUser().setScoresFlag(""); EmployeeHandler eHandler = new
		 * EmployeeHandler(); Employee emp = eHandler.getEmployeeById(
		 * uFilter.getQuseryStrings().getEmplid() );
		 */
		// Ends here

		// ended for RBU
		int registered = 0;
		int assigned = 0;
		int exempt = 0;
		int complete = 0;
		int onleave = 0;
		int pending = 0;
		// added for major enhancement
		int cancelled = 0;
		int notComplete = 0;
		// ends here
		LoggerHelper.logSystemDebug("inside get phasechart1");
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

			if ("Registered".equals(st.getStatus())
					|| "In Progress".equals(st.getStatus())
					|| "Cancelled".equals(st.getStatus())
					|| "No Show".equals(st.getStatus())
					|| "Pending".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Not Complete");
					notComplete++;
				}
			} else if ("Waived".equals(st.getStatus())
					|| "Complete".equals(st.getStatus())) {// ||
															// "RegisteredC".equals(st.getStatus())){

				st.setStatusToDisplay("Complete");
				complete++;
			} else if ("Assigned".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Assigned");
					assigned++;
				}

			}
		}
		/*
		 * if ("Registered".equals(st.getStatus())) { if (
		 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
		 * st.setStatus("On-Leave"); onleave++; } else { registered++; } } else
		 * if ("Assigned".equals(st.getStatus())) { if (
		 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
		 * st.setStatus("On-Leave"); onleave++; } else { assigned++; } } else if
		 * ("Waived".equals(st.getStatus())) { // Added for P2L issues data fix
		 * -- 17Sep if ( "On-Leave".equals(st.getEmployee().getEmployeeStatus())
		 * ) { st.setStatus("On-Leave"); onleave++; } else{ exempt++; } }
		 * //added for major enhancement else if
		 * ("Cancelled".equals(st.getStatus())) { //* Added for P2L issues data
		 * fix -- 17Sep if (
		 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
		 * st.setStatus("On-Leave"); onleave++; } else{ cancelled++; } } //ends
		 * here else if ("Pending".equals(st.getStatus())) { pending++; } else
		 * if ("RegisteredC".equals(st.getStatus())) { st.setStatus("Complete");
		 * complete++; } // Added for 'In Progress' status by Meenakshi else if
		 * ("In Progress".equals(st.getStatus())) { registered++; } // End of
		 * addition else { complete++; }
		 */

		HashMap result = new HashMap();
		PieChart chart = null;
		List data = new ArrayList();
		Map tMap = new HashMap();
		PieChartBuilder builder = new PieChartBuilder();
		StatusSubSetBean b = getStatusSubsetDetails(uFilter);

		if (!b.isStatusSubsetSelected()) {

			LoggerHelper.logSystemDebug("inside get phasechart Subset");
			/*
			 * data.add(new ChartData( "Complete", complete ) ); //added for
			 * major enhancement data.add(new ChartData( "Cancelled", cancelled
			 * ) ); //ends here
			 * 
			 * if ( phase.getExempt() ) { data.add(new ChartData( "Waived",
			 * exempt ) ); tMap.put( "Waived", new Color(255,153,0) ); } if (
			 * phase.getAssigned()) { data.add(new ChartData( "Assigned",
			 * assigned) ); tMap.put( "Assigned", new Color(153,204,0) ); } if (
			 * phase.getApprovalStatus() ) { tMap.put( "Pending", new
			 * Color(255,51,102) ); data.add(new ChartData( "Pending", pending )
			 * ); } else { data.add(new ChartData( "Registered", registered ) );
			 * } data.add(new ChartData( "On-Leave", onleave ) );
			 * 
			 * 
			 * 
			 * 
			 * tMap.put( "Complete", AppConst.COLOR_BLUE ); //added for major
			 * enhancement tMap.put( "Cancelled", AppConst.COLOR_CYAN ); //ends
			 * here tMap.put( "Registered", new Color(153,51,0) ); tMap.put(
			 * "On-Leave", new Color(253,243,156) );
			 */
			data.add(new ChartData("Complete", complete));
			data.add(new ChartData("Assigned", assigned));
			data.add(new ChartData("Not Complete", notComplete));
			data.add(new ChartData("On-Leave", onleave));
			tMap.put("Complete", AppConst.COLOR_BLUE);
			tMap.put("On-Leave", new Color(253, 243, 156));
			tMap.put("Assigned", new Color(153, 204, 0));
			tMap.put("Not Complete", new Color(153, 51, 0));
			Map colorMap = Collections.unmodifiableMap(tMap);
			// Added for Major Enhancement 3.6 for CSO impact
			/*
			 * if( uSession.getUser().getScoresVisible().equals("Y") ||
			 * uSession.
			 * getUser().getSalesPositionTypeCd().equals(emp.getSalesPostionTypeCode
			 * ())) { uSession.getUser().setScoresFlag("Y"); }
			 * 
			 * System.out.println("Scores Flag"+uSession.getUser().getScoresFlag(
			 * ));
			 */
			// Ends here

			String ak = phase.getRootActivityId();
			String label = phase.getTrack().getTrackLabel() + " : "
					+ phase.getPhaseNumber();
			// chart =
			// builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk="
			// + ak ,getRequest().getSession(), colorMap);
			// added for major enhancement
			LoggerHelper.logSystemDebug("chart section now"
					+ uFilter.getQuseryStrings().getSection());
			String sec = uFilter.getFilterForm().getChkStatus();
			if (sec.equals("") || sec.equals("All")) {
				chart = builder.generate(data, null, label, null, getRequest()
						.getSession(), colorMap);
			} else {
				chart = builder.generate(data, sec, label, null, getRequest()
						.getSession(), colorMap);
			}
			LoggerHelper.logSystemDebug("aftr builder ");
			/*
			 * chart.setCount( complete + exempt + assigned + registered +
			 * onleave + cancelled); //ends here //Added for Phase 1 by
			 * Meenakshi chart.setCompleted(complete); chart.setExempt(exempt);
			 * chart.setAssigned(assigned); chart.setRegistered(registered);
			 * chart.setOnleave(onleave); chart.setCancelled(cancelled);
			 */
			/* End of addition */
			chart.setCount(complete + onleave + assigned + notComplete);
			chart.setAssigned(assigned);
			chart.setCompleted(complete);
			chart.setOnleave(onleave);
			chart.setNotComplete(notComplete);
			employees.addAll(master);
		} else {

			int subSetRegistered = 0;
			int subSetAssigned = 0;
			int subSetExempt = 0;
			int subSetComplete = 0;
			int subSetOnleave = 0;
			int subSetPending = 0;
			int subSetCancelled = 0;
			int subSetNotComplete = 0;
			String dtStatusTo = null;
			String dtStatusFrom = null;

			if (b != null) {
				System.out.println("bean is not null");
				dtStatusFrom = b.getStatusFromDate();
				dtStatusTo = b.getStatusToDate();
			}
			LoggerHelper.logSystemDebug(" not inside get phasechart Subset"
					+ b.getStatusFromDate());
			Date statusDate = null;
			Date dateTime = null;
			String stDate = null;
			Date statDate = null;
			Date statusFrDate = null;
			Date statusToDate = null;
			int i;
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

			SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfOutput = new SimpleDateFormat("MM/dd/yyyy");

			boolean isInDateRange;

			for (Iterator it = master.iterator(); it.hasNext();) {
				boolean dateFlag = false;
				P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();
				statusDate = null;
				isInDateRange = false;
				dateTime = st.getCompleteDate();
				if (dateTime != null) {
					String strstatusDate = dateTime.toString();
					String[] s = strstatusDate.split(" ");
					String str = s[0];
					LoggerHelper.logSystemDebug("split date now" + str);
					dateFlag = true;
					// if(dtStatusFrom != null || dtStatusTo != null) {
					if (!Util.toEmpty(dtStatusFrom).equals(" ")
							|| !Util.toEmpty(dtStatusTo).equals(" ")) {
						try {
							statusDate = sdfInput.parse(str);
							stDate = sdfOutput.format(statusDate);
							statDate = sdfOutput.parse(stDate);
							System.out.println("statusFrDate" + statusFrDate);
							System.out.println("dtStatusTo" + dtStatusTo);
							if (!Util.toEmpty(dtStatusFrom).equals(" "))
								statusFrDate = sdfOutput.parse(dtStatusFrom);
							if (!Util.toEmpty(dtStatusTo).equals(" "))
								statusToDate = sdfOutput.parse(dtStatusTo);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						LoggerHelper
								.logSystemDebug("split date parse" + stDate);
						// compare date
						// if(stDate != null) {
						/*
						 * if (dtStatusFrom != null && dtStatusTo != null) {
						 * 
						 * if(statDate.compareTo(statusFrDate) >= 0 &&
						 * statDate.compareTo(statusToDate) <= 0) {
						 * isInDateRange = true;
						 * LoggerHelper.logSystemDebug("date in range1");
						 * 
						 * }
						 */
						System.out.println("dtStatusFrom" + dtStatusFrom);
						System.out.println("dtStatusTo" + dtStatusTo);
						System.out.println("statDate" + statDate);
						/*
						 * if (dtStatusFrom != null || dtStatusTo != null) {
						 * 
						 * if(dtStatusTo != null) {
						 * 
						 * if((statDate.compareTo(statusFrDate) >= 0 &&
						 * statDate.compareTo(statusToDate) <= 0) || (statDate
						 * != null && statDate.compareTo(statusToDate) <= 0)) {
						 * isInDateRange = true;
						 * 
						 * LoggerHelper.logSystemDebug("date falls in range");
						 * 
						 * } } else { if(statDate.compareTo(statusFrDate) >= 0
						 * && statDate.compareTo(new java.util.Date())<=0){
						 * isInDateRange = true; } }
						 * 
						 * }
						 */
						if (!Util.toEmpty(dtStatusFrom).equals(" ")
								|| !Util.toEmpty(dtStatusTo).equals(" ")) {
							if (!Util.toEmpty(dtStatusFrom).equals(" ")
									&& Util.toEmpty(dtStatusTo).equals(" ")) {
								if (statDate.compareTo(statusFrDate) >= 0
										&& statDate
												.compareTo(new java.util.Date()) <= 0) {
									isInDateRange = true;
									System.out.println("inside from date");
								}
							} else if (!Util.toEmpty(dtStatusTo).equals(" ")
									&& Util.toEmpty(dtStatusFrom).equals(" ")) {
								if (statDate.compareTo(statusToDate) <= 0) {
									isInDateRange = true;
									System.out.println("inside to date");
								}
							} else if (!Util.toEmpty(dtStatusFrom).equals(" ")
									&& !Util.toEmpty(dtStatusTo).equals(" ")) {
								if ((statDate.compareTo(statusFrDate) >= 0 && statDate
										.compareTo(statusToDate) <= 0)) {
									isInDateRange = true;
									System.out.println("date falls in range");
								}
							}
						}

						/*
						 * else if (dtStatusFrom != null) {
						 * if(stDate.compareTo(statusFrDate) >= 0) {
						 * isInDateRange = false;
						 * LoggerHelper.logSystemDebug("date in range2"); } }
						 * else if (dtStatusTo != null) {
						 * if(stDate.compareTo(statusToDate) <= 0) {
						 * isInDateRange = false;
						 * LoggerHelper.logSystemDebug("date in range3 now"); }
						 * }
						 */
						// }
					}
				}
				if (isInDateRange && dateFlag) {

					if ("Registered".equals(st.getStatus())
							|| "In Progress".equals(st.getStatus())
							|| "Cancelled".equals(st.getStatus())
							|| "No Show".equals(st.getStatus())
							|| "Pending".equals(st.getStatus())) {

						if ("On-Leave".equals(st.getEmployee()
								.getEmployeeStatus())) {
							st.setStatus("On-Leave");
							st.setStatusToDisplay("On-Leave");
							subSetOnleave++;
						} else {
							st.setStatusToDisplay("Not Complete");
							subSetNotComplete++;
						}
					} else if ("Waived".equals(st.getStatus())
							|| "Complete".equals(st.getStatus())) {// ||
																	// "RegisteredC".equals(st.getStatus())){

						st.setStatusToDisplay("Complete");
						subSetComplete++;
					} else if ("Assigned".equals(st.getStatus())) {

						if ("On-Leave".equals(st.getEmployee()
								.getEmployeeStatus())) {
							st.setStatus("On-Leave");
							st.setStatusToDisplay("On-Leave");
							subSetOnleave++;
						} else {
							st.setStatusToDisplay("Assigned");
							subSetAssigned++;
						}

					} else if (st.getStatus().equalsIgnoreCase("On-Leave")) {
						subSetOnleave++;
					}

					subset.add(st);
				}
			} // end for

			int rest = subset.size();

			if ("Complete".equals(b.getChkStatus())
					|| "All".equals(b.getChkStatus())) {
				data.add(new ChartData("Complete", subSetComplete));
				tMap.put("Complete", AppConst.COLOR_BLUE);
				rest -= subSetComplete;
			}
			if ("Not Complete".equals(b.getChkStatus())
					|| "All".equals(b.getChkStatus())) {
				rest -= subSetNotComplete;
				data.add(new ChartData("Not Complete", subSetNotComplete));
				tMap.put("Not Complete", new Color(153, 51, 0));
			}
			if ("Assigned".equals(b.getChkStatus())
					|| "All".equals(b.getChkStatus())) {
				rest -= subSetAssigned;
				data.add(new ChartData("Assigned", subSetAssigned));
				tMap.put("Assigned", new Color(153, 204, 0));
			}
			if ("On-Leave".equals(b.getChkStatus())
					|| "All".equals(b.getChkStatus())) {
				rest -= subSetOnleave;
				tMap.put("On-Leave", new Color(253, 243, 156));
				data.add(new ChartData("On-Leave", subSetOnleave));
			}
			if ("All".equals(b.getChkStatus())) {
			} else {
				data.add(new ChartData("Others", rest));
				tMap.put("Others", new Color(0, 0, 0));
			}

			Map colorMap = Collections.unmodifiableMap(tMap);

			String ak = phase.getRootActivityId();
			String label = phase.getTrack().getTrackLabel() + " : "
					+ phase.getPhaseNumber();
			String sec = uFilter.getFilterForm().getChkStatus();
			if (sec.equals("") || sec.equals("All")) {
				chart = builder.generate(data, null, label, null, getRequest()
						.getSession(), colorMap);
			} else {
				chart = builder.generate(data, sec, label, null, getRequest()
						.getSession(), colorMap);
			}

			chart.setCount(subSetComplete + subSetAssigned + subSetOnleave
					+ subSetNotComplete);
			chart.setNotComplete(subSetNotComplete);
			chart.setCompleted(subSetComplete);
			chart.setAssigned(subSetAssigned);
			chart.setOnleave(subSetOnleave);
			employees.addAll(subset);
		}
		return chart;
	}

	public PieChart getActivityChart(UserTerritory ut, SubActivityBean bean,
			UserFilter uFilter, Collection employees) {
		StringBuffer sb = new StringBuffer();
		Timer timer = new Timer();
		P2lHandler p2l = new P2lHandler();
		PieChartBuilder builder = new PieChartBuilder();
		System.out.println("inside get activity chartX");
		Collection master = p2l.getActivityStatus(ut, bean, uFilter);
		// Collection sub = p2l.getSubSetActivityStatus(ut, bean, uFilter);
		// Added for Major Enhancement 3.6 for CSO impact
		/*
		 * UserSession uSession = buildUserSession();
		 * uSession.getUser().setScoresFlag(""); EmployeeHandler eHandler = new
		 * EmployeeHandler(); Employee emp = eHandler.getEmployeeById(
		 * uFilter.getQuseryStrings().getEmplid() );
		 */
		// Ends here
		Collection subset = new ArrayList();
		LoggerHelper.logSystemDebug("master666" + master);

		// ended for RBU
		int registered = 0;
		int assigned = 0;
		int exempt = 0;
		int complete = 0;
		int onleave = 0;
		int pending = 0;
		// added for major enhancement
		int cancelled = 0;
		int notComplete = 0;
		// ends here
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

			LoggerHelper.logSystemDebug("hello");

			if ("Registered".equals(st.getStatus())
					|| "In Progress".equals(st.getStatus())
					|| "Cancelled".equals(st.getStatus())
					|| "No Show".equals(st.getStatus())
					|| "Pending".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Not Complete");
					notComplete++;
				}
			} else if ("Waived".equals(st.getStatus())
					|| "Complete".equals(st.getStatus())) {// ||
															// "RegisteredC".equals(st.getStatus())){
				st.setStatusToDisplay("Complete");
				complete++;
			} else if ("Assigned".equals(st.getStatus())) {
				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Assigned");
					assigned++;
				}
			}
		}

		HashMap result = new HashMap();

		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();

		StatusSubSetBean b = getStatusSubsetDetails(uFilter);
		if (!b.isStatusSubsetSelected()) {
			/*
			 * data.add(new ChartData( "Complete", complete ) );
			 * 
			 * tMap.put( "Complete", AppConst.COLOR_BLUE );
			 * 
			 * //added for major enhancement data.add(new ChartData(
			 * "Cancelled", cancelled ) );
			 * 
			 * tMap.put( "Cancelled", AppConst.COLOR_CYAN ); //ends here
			 * 
			 * data.add(new ChartData( "Waived", exempt ) ); tMap.put( "Waived",
			 * new Color(255,153,0) );
			 * 
			 * data.add(new ChartData( "Assigned", assigned) ); tMap.put(
			 * "Assigned", new Color(153,204,0) ); data.add(new ChartData(
			 * "Registered", registered ) ); tMap.put( "Registered", new
			 * Color(153,51,0) );
			 * 
			 * data.add(new ChartData( "On-Leave", onleave ) ); tMap.put(
			 * "On-Leave", new Color(253,243,156) );
			 */

			// Added for Major Enhancement 3.6 for CSO impact
			/*
			 * if( uSession.getUser().getScoresVisible().equals("Y") ||
			 * uSession.
			 * getUser().getSalesPositionTypeCd().equals(emp.getSalesPostionTypeCode
			 * ())) { uSession.getUser().setScoresFlag("Y"); }
			 */

			data.add(new ChartData("Complete", complete));
			data.add(new ChartData("Assigned", assigned));
			data.add(new ChartData("Not Complete", notComplete));
			data.add(new ChartData("On-Leave", onleave));
			tMap.put("Complete", AppConst.COLOR_BLUE);
			tMap.put("On-Leave", new Color(253, 243, 156));
			tMap.put("Assigned", new Color(153, 204, 0));
			tMap.put("Not Complete", new Color(153, 51, 0));
			Map colorMap = Collections.unmodifiableMap(tMap);

			String ak = bean.getRootActivityID();
			String label = bean.getActivityName();
			String sec = uFilter.getFilterForm().getChkStatus();
			if (sec.equals("") || sec.equals("All")) {
				chart = builder.generate(data, null, label, null, getRequest()
						.getSession(), colorMap);
			} else {
				chart = builder.generate(data, sec, label, null, getRequest()
						.getSession(), colorMap);
			}

			chart.setCount(complete + onleave + assigned + notComplete);
			// chart.setCount( complete + exempt + assigned + registered +
			// onleave + cancelled);
			employees.addAll(master);
			chart.setCount(complete + onleave + assigned + notComplete);
			chart.setAssigned(assigned);
			chart.setCompleted(complete);
			chart.setOnleave(onleave);
			chart.setNotComplete(notComplete);

			/*
			 * Added for Phase 1 by Meenakshi chart.setCompleted(complete);
			 * chart.setExempt(exempt); chart.setAssigned(assigned);
			 * chart.setRegistered(registered); chart.setOnleave(onleave);
			 * chart.setCancelled(cancelled); /* End of addition
			 */
			return chart;
		}

		else {

			int subSetRegistered = 0;
			int subSetAssigned = 0;
			int subSetExempt = 0;
			int subSetComplete = 0;
			int subSetOnleave = 0;
			int subSetPending = 0;
			int subSetCancelled = 0;
			int subSetNotComplete = 0;

			String dtStatusFrom = b.getStatusFromDate();
			String dtStatusTo = b.getStatusToDate();
			LoggerHelper.logSystemDebug(" not inside get phasechart Subset"
					+ b.getStatusFromDate());
			Date statusDate = null;
			Date dateTime = null;
			String stDate = null;
			Date statDate = null;
			Date statusFrDate = null;
			Date statusToDate = null;
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

			SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfOutput = new SimpleDateFormat("MM/dd/yyyy");

			boolean isInDateRange;

			for (Iterator it = master.iterator(); it.hasNext();) {
				boolean dateFlag = false;
				P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

				statusDate = null;
				isInDateRange = false;

				dateTime = st.getCompleteDate();
				if (dateTime != null) {
					String strstatusDate = dateTime.toString();
					String[] s = strstatusDate.split(" ");
					String str = s[0];
					LoggerHelper.logSystemDebug("split date now" + str);
					dateFlag = true;
					if (dtStatusFrom != null && dtStatusTo != null) {
						try {
							statusDate = sdfInput.parse(str);
							stDate = sdfOutput.format(statusDate);
							statDate = sdfOutput.parse(stDate);
							statusFrDate = sdfOutput.parse(dtStatusFrom);
							statusToDate = sdfOutput.parse(dtStatusTo);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						LoggerHelper
								.logSystemDebug("split date parse" + stDate);
						// compare date
						// if(statusDate != null) {
						if (dtStatusFrom != null && dtStatusTo != null) {

							if (statDate.compareTo(statusFrDate) >= 0
									&& statDate.compareTo(statusToDate) <= 0) {
								isInDateRange = true;
								LoggerHelper.logSystemDebug("date in range1");

							}
						} /*
						 * else if (dtStatusFrom != null) {
						 * if(stDate.compareTo(dtStatusFrom) >= 0) {
						 * isInDateRange = false;
						 * LoggerHelper.logSystemDebug("date in range2"); } }
						 * else if (dtStatusTo != null) {
						 * if(stDate.compareTo(dtStatusTo) <= 0) { isInDateRange
						 * = false;
						 * LoggerHelper.logSystemDebug("date in range3"); }
						 */
						// }
					}
				}
				if (isInDateRange && dateFlag) {

					if ("Registered".equals(st.getStatus())
							|| "In Progress".equals(st.getStatus())
							|| "Cancelled".equals(st.getStatus())
							|| "No Show".equals(st.getStatus())
							|| "Pending".equals(st.getStatus())) {

						if ("On-Leave".equals(st.getEmployee()
								.getEmployeeStatus())) {
							st.setStatus("On-Leave");
							st.setStatusToDisplay("On-Leave");
							subSetOnleave++;
						} else {
							st.setStatusToDisplay("Not Complete");
							subSetNotComplete++;
						}
					} else if ("Waived".equals(st.getStatus())
							|| "Complete".equals(st.getStatus())) {// ||
																	// "RegisteredC".equals(st.getStatus())){

						st.setStatusToDisplay("Complete");
						subSetComplete++;
					} else if ("Assigned".equals(st.getStatus())) {

						if ("On-Leave".equals(st.getEmployee()
								.getEmployeeStatus())) {
							st.setStatus("On-Leave");
							st.setStatusToDisplay("On-Leave");
							subSetOnleave++;
						} else {
							st.setStatusToDisplay("Assigned");
							subSetAssigned++;
						}
					} else if (st.getStatus().equalsIgnoreCase("On-Leave")) {
						subSetOnleave++;
					}

					subset.add(st);
				}
			} // end for

			int rest = subset.size();

			if ("Complete".equals(b.getChkStatus())
					|| "All".equals(b.getChkStatus())) {
				data.add(new ChartData("Complete", subSetComplete));
				tMap.put("Complete", AppConst.COLOR_BLUE);
				rest -= subSetComplete;
			}
			if ("Not Complete".equals(b.getChkStatus())
					|| "All".equals(b.getChkStatus())) {
				rest -= subSetNotComplete;
				data.add(new ChartData("Not Complete", subSetNotComplete));
				tMap.put("Not Complete", new Color(153, 51, 0));
			}
			if ("Assigned".equals(b.getChkStatus())
					|| "All".equals(b.getChkStatus())) {
				rest -= subSetAssigned;
				data.add(new ChartData("Assigned", subSetAssigned));
				tMap.put("Assigned", new Color(153, 204, 0));
			}
			if ("On-Leave".equals(b.getChkStatus())
					|| "All".equals(b.getChkStatus())) {
				rest -= subSetOnleave;
				tMap.put("On-Leave", new Color(253, 243, 156));
				data.add(new ChartData("On-Leave", subSetOnleave));
			}
			if ("All".equals(b.getChkStatus())) {
			} else {
				data.add(new ChartData("Others", rest));
				tMap.put("Others", new Color(0, 0, 0));
			}

			Map colorMap = Collections.unmodifiableMap(tMap);

			String ak = bean.getRootActivityID();
			String label = bean.getActivityName();
			String sec = uFilter.getFilterForm().getChkStatus();
			if (sec.equals("") || sec.equals("All")) {
				chart = builder.generate(data, null, label, null, getRequest()
						.getSession(), colorMap);
			} else {
				chart = builder.generate(data, sec, label, null, getRequest()
						.getSession(), colorMap);
			}

			chart.setCount(subSetComplete + subSetAssigned + subSetOnleave
					+ subSetNotComplete);
			chart.setNotComplete(subSetNotComplete);
			chart.setCompleted(subSetComplete);
			chart.setAssigned(subSetAssigned);
			chart.setOnleave(subSetOnleave);
			employees.addAll(subset);

		}

		return chart;
	}

	public boolean checkActivityDrilldownVisibility(User usr) {
		boolean allowDisplay = true;
		boolean allowrow = true;
		UserGroups ug = new UserGroups();
		String groupName = trDb.getActivityDrillDownAccessGroup();
		LoggerHelper.logSystemDebug("checktest" + groupName);

		if (groupName != null) {
			/* Adding Feedback user condition for SCE Feedback form enhancement */
			if (!groupName.equalsIgnoreCase("ADMIN")
					|| !groupName.equalsIgnoreCase("TSR_ADMIN")) {
				ug = getGroupDetails(groupName);
			}
		}

		if (!Util.isEmpty(groupName) && ug != null) {
			allowDisplay = false;
			if ("TSR_ADMIN".equals(groupName)) {
				if (usr.isTsrAdmin() || usr.isSuperAdmin()) {
					allowDisplay = true;
				}
			}
			/* Adding condition for Feedback user for SCE Form enhancement */
			else if ("ADMIN".equals(groupName)) {
				if (usr.isAdmin() || usr.isSuperAdmin()) {
					allowDisplay = true;
				}
			} else if ("SUPER_ADMIN".equals(groupName)) {
				if (usr.isSuperAdmin()) {
					allowDisplay = true;
				}
			}
			/* Loop added for user Specific groups */
			else if (usr.isSuperAdmin()) {
				allowDisplay = true;
			}
			/* Condition added for SCE Feedback form enhancement */
			else if (usr.isAdmin() && usr.isFeedbackUser()) {
				if ((Util
						.splitFields(ug.getSelectedBU(), usr.getBusinessUnit())
						&& Util.splitFields(ug.getSelectedSalesorg(),
								usr.getSalesOrganization()) && Util
							.splitFields(ug.getSelectedRole(),
									usr.getRoleDesc()))
						|| (Util.splitFields(ug.getSelectedFBU(), usr.getName()))) {
					allowDisplay = true;
				}
			}
			/* Adding condition for Feedback user for SCE Form enhancement */
			else if (!usr.isAdmin() && !usr.isTsrAdmin() && !usr.isSuperAdmin()
					&& !usr.isFeedbackUser()) {
				if (Util.splitFields(ug.getSelectedBU(), usr.getBusinessUnit())
						&& Util.splitFields(ug.getSelectedSalesorg(),
								usr.getSalesOrganization())
						&& Util.splitFields(ug.getSelectedRole(),
								usr.getRoleDesc())) {
					allowDisplay = true;
				}
			}
		}
		return allowDisplay;
	}

	private UserGroups getGroupDetails(String groupName) {
		UserGroups userGroups = null;
		userGroups = trDb.getUserGroupDetails(groupName);
		if (userGroups != null) {
			// System.out.println("After getting user groups1"+userGroups.getSelectedBU());
			// System.out.println("After getting user groups2"+userGroups.getSelectedSalesorg());
			// System.out.println("After getting user groups3"+userGroups.getSelectedRole());
		}
		return userGroups;
	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward searchemployee(){
	 */
	public String searchemployee() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}
			UserSession uSession = buildUserSession();
			UserFilter uFilter = uSession.getUserFilter();
			P2lTrack track = uSession.getTrack();
			List phases = track.getPhases();
			String nodes = "";
			for (Iterator it = phases.iterator(); it.hasNext();) {
				P2lTrackPhase phase = (P2lTrackPhase) it.next();
				if (Util.isEmpty(nodes)) {
					nodes = phase.getRootActivityId();
				} else {
					nodes = nodes + "," + phase.getRootActivityId();
				}
				if (!Util.isEmpty(phase.getAlttActivityId())) {
					nodes = nodes + "," + phase.getAlttActivityId();
				}
			}
			EmplSearchForm eForm = new EmplSearchForm();
			FormUtil.loadObject(getRequest(), eForm);

			P2lHandler pHandler = new P2lHandler();
			Employee[] result = pHandler.search(nodes, uSession, eForm);

			BlankTemplateWpc page = new BlankTemplateWpc();
			EmplSearchResultWc main = new EmplSearchResultWc(result);
			main.setTrack(track);
			page.setMain(main);
			getRequest().setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward listreport(){
	 */
	public String listreport() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}
			UserSession uSession = buildUserSession();
			UserFilter uFilter = uSession.getUserFilter();
			/* // Added for Major Enhancement 3.6 - F1 */
			String rootActivityID = "";

			/* Added for RBU changes */
			uFilter.setRefresh(true);
			String activityPk = null;
			String currentSlice = null;

			// Added for Major Enhancement 3.6 - F1
			String statusFromDate = uFilter.getFilterForm().getFromDate();
			String statusToDate = uFilter.getFilterForm().getToDate();
			SimpleDateFormat sdfOutput = new SimpleDateFormat("MM/dd/yyyy");
			boolean FrDateGreater = false;
			boolean EmptyToDate = false;
			boolean EmptyFromDate = false;
			Date statusFrDate = null;
			Date statusTDate = null;
			String datecheck = (String) getRequest().getSession().getAttribute(
					"datecheck");

			if (statusFromDate.length() > 0) {
				if (statusToDate.length() < 0 || statusToDate.equals(null)
						|| statusToDate.equals("")) {
					EmptyToDate = true;
					LoggerHelper.logSystemDebug("datecheck" + datecheck);
				}
			}

			if (statusToDate.length() > 0) {
				if (statusFromDate.length() < 0 || statusFromDate.equals(null)
						|| statusFromDate.equals("")) {
					EmptyFromDate = true;
				}
			}

			LoggerHelper.logSystemDebug("listrep");

			if ((uFilter.getQuseryStrings().getActivitypk().equals("") || uFilter
					.getQuseryStrings().getActivitypk().equals("null"))) {
				System.out
						.println("\n--------------- QUERY STRINGS NULL----------------\n");
				LoggerHelper.logSystemDebug("inside activitycheck");
				activityPk = uSession.getCurrentActivity();
				uFilter.getQuseryStrings().setActivitypk(activityPk);
				System.out
						.println("\n--------------- QUERY STRINGS NULL----------------\n"
								+ activityPk);
				// Start: Commented for Major Enhancement 3.6 - F1
				// currentSlice= uSession.getCurrentSlice();
				// System.out.println("\n--------------- QUERY STRINGS NULL----------------\n"+currentSlice);
				// uFilter.getQuseryStrings().setSection(currentSlice);
				// Ends here: for Major Enhancement 3.6 - F1
			} else {
				System.out
						.println("--------------- QUERY STRINGS NOT NULL----------------");
				activityPk = uFilter.getQuseryStrings().getActivitypk();
				// Added for Major Enhancement 3.6 - F1
				// currentSlice=uFilter.getQuseryStrings().getSection();
			}
			// Start: Commented for Major Enhancement 3.6 - F1

			/*
			 * if(uFilter.getQuseryStrings().getSection().equals("")||
			 * uFilter.getQuseryStrings().getSection().equals("null")) {
			 * 
			 * if(uFilter.getFilterForm().getChkStatus().equals("")||uFilter.
			 * getFilterForm().getChkStatus().equals("null")) {
			 * 
			 * currentSlice= uSession.getCurrentSlice(); } else {
			 * 
			 * currentSlice= uFilter.getFilterForm().getChkStatus(); if
			 * ("All".equals(currentSlice)) currentSlice = ""; }
			 * 
			 * uFilter.getQuseryStrings().setSection(currentSlice); } else {
			 * 
			 * currentSlice=null;
			 * uFilter.getQuseryStrings().setSection(currentSlice); if
			 * ("All".equals(currentSlice)) { currentSlice = null;
			 * uFilter.getQuseryStrings().setSection(currentSlice);
			 * 
			 * } }
			 * 
			 * 
			 * uSession.setCurrentSlice(currentSlice);
			 * uSession.setCurrentActivity(activityPk);
			 */
			// End: for Major Enhancement 3.6 - F1

			// Start: Added for Major Enhancement 3.6 - F1
			String section = uFilter.getQuseryStrings().getSection();

			if (section != null) {
				currentSlice = uFilter.getQuseryStrings().getSection();
				LoggerHelper.logSystemDebug("today section" + currentSlice);
				// currentSlice = null;
				uFilter.getQuseryStrings().setSection(
						uFilter.getFilterForm().getChkStatus());
				System.out.println("check section1"
						+ uFilter.getQuseryStrings().getSection());
				if ("All".equals(currentSlice)) {
					currentSlice = null;
					uFilter.getQuseryStrings().setSection(
							uFilter.getFilterForm().getChkStatus());

				}

			} else {
				LoggerHelper.logSystemDebug("r1");
				if (uFilter.getFilterForm().getChkStatus().equals("")
						|| uFilter.getFilterForm().getChkStatus()
								.equals("null")) {
					LoggerHelper.logSystemDebug("r2");
					currentSlice = uSession.getCurrentSlice();
				} else {
					LoggerHelper.logSystemDebug("r3");
					currentSlice = uFilter.getFilterForm().getChkStatus();
					if ("All".equals(currentSlice))
						currentSlice = "";
				}
				LoggerHelper.logSystemDebug("r4");
				uFilter.getQuseryStrings().setSection(
						uFilter.getFilterForm().getChkStatus());
				System.out.println("check section2"
						+ uFilter.getQuseryStrings().getSection());
			}
			// End: for Major Enhancement 3.6 - F1

			uSession.setCurrentSlice(currentSlice);
			uSession.setCurrentActivity(activityPk);

			/* End of addition */

			// uSession.setCurrentSlice( uFilter.getQuseryStrings().getSection()
			// );
			// String activityPk = uFilter.getQuseryStrings().getActivitypk();

			Collection result = new ArrayList();

			P2lHandler p2l = new P2lHandler();

			P2lTrackPhase phase = null;
			P2lTrackPhase trackPhase = null;
			String label = "";
			PieChart chart;
			P2lTrack track = uSession.getTrack();
			/* Added for Phase 1 by Meenakshi */
			P2lTrackPhase subTrackPhase = null;
			// Added for Major Enhancement 3.6 - F1
			String parentActivityPk = (String) getRequest().getSession()
					.getAttribute("parentActivityPk");
			String fromListReport = "fromlistreport";
			// End: for Major Enhancement 3.6 - F1
			if ("Overall".equals(activityPk)) {
				label = "Overall";
				chart = getOverallChart(track, uFilter, "", result, true,
						uSession.getUser().getId(), track.getAllNodesDelimit()); // 441818
				trackPhase = (P2lTrackPhase) track.getPhases().get(0);
				LoggerHelper.logSystemDebug("inside overall");

			} else {
				trackPhase = p2l.getTrackPhase(activityPk, track.getTrackId());
				// If condition added for Major Enhancement 3.6 - F1
				if (trackPhase != null) {
					label = trackPhase.getPhaseNumber();
					trackPhase.setTrack(track);
					// chart =
					// getPhaseChart(trackPhase,uFilter,trackPhase.getAlttActivityId(),result,
					// true, null); //441818
					trackPhase.setTrack(track);
					// added for RBU
					UserTerritory ut = uSession.getUser().getUserTerritory();
					// Added for Major Enhancement 3.6 - F1
					chart = getAllStatusPhaseChart(ut, trackPhase, uFilter,
							trackPhase.getAlttActivityId(), result, true, null); // 441818
					LoggerHelper.logSystemDebug("inside trackphase"
							+ chart.getCount());
					// ended for RBU
					rootActivityID = trackPhase.getRootActivityId();
					parentActivityPk = activityPk;
				} // End: for Major Enhancement 3.6 - F1
					// Added for Major Enhancement 3.6 - F1
				else {
					// Sub Activity
					SubActivityBean bean = p2l.getActivityDetails(activityPk);
					label = bean.getActivityName();
					// rootActivityID = bean.getRootActivityID();

					UserTerritory ut = uSession.getUser().getUserTerritory();
					chart = getActivityChart(ut, bean, uFilter, result);

					subTrackPhase = p2l.getTrackPhase(parentActivityPk,
							track.getTrackId());

					if (subTrackPhase != null) {
						rootActivityID = subTrackPhase.getRootActivityId();
					}
				}
			}
			// End: for Major Enhancement 3.6 - F1

			/* Setting the session required for the getNextLevel() method */
			getRequest().getSession().setAttribute("P2lListReportChart", chart);

			int layout = ChartLegendWc.LAYOUT_PHASE;
			if (trackPhase != null && trackPhase.getApprovalStatus()) {
				layout = ChartLegendWc.LAYOUT_PHASE_PENDING;
			}
			ChartDetailWc chartDetailWc = null;

			// Get the links that would be displayed
			System.out.println("Activity ID in P2L Controller------------"
					+ activityPk);
			List resultList = new ArrayList();
			resultList = p2l.getLinksForActivities(activityPk);
			// Added for Major Enhancement 3.6 - F1

			StatusSubSetBean b = getStatusSubsetDetails(uFilter);

			if (chart.getCount() > 0) {
				// Addedfor Major Enhancement 3.6 - F1

				if (b.isStatusSubsetSelected()) {
					// ChartP2lSubsetLegendWc legendWc = new
					// ChartP2lSubsetLegendWc(
					// uFilter.getQuseryStrings().getActivitypk() + "",
					// trackPhase
					// );
					// legendWc.setChkStatus(uFilter.getFilterForm().getChkStatus());
					/* Modified for Phase 1 by Meenakshi */
					// chartDetailWc = new ChartDetailWc( chart, label
					// ,null,"showReportLink",resultList);
					if (trackPhase != null) {
						chartDetailWc = new ChartDetailWc(chart, label,
								new ChartP2lPhaseLegendWc(uFilter
										.getQuseryStrings().getActivitypk()
										+ "", trackPhase, chart),
								"showReportLink", resultList);
					} else {
						chartDetailWc = new ChartDetailWc(chart, label,
								new ChartP2lPhaseLegendWc(uFilter
										.getQuseryStrings().getActivitypk()
										+ "", subTrackPhase, chart),
								"showReportLink", resultList);
					}
					/* End of modification */
					LoggerHelper.logSystemDebug("StatusSubsetSelected");
				}

				else {
					// End: for Major Enhancement 3.6 - F1

					// chartDetailWc = new ChartDetailWc( chart, label ,new
					// ChartP2lLegendWc(
					// uFilter.getQuseryStrings().getActivitypk()
					// + "", trackPhase ) );
					/* Modified for Product and Summary report links */
					/* Modified for Phase 1 by Meenakshi */
					// chartDetailWc = new ChartDetailWc( chart, label
					// ,null,"showReportLink",resultList);
					if (trackPhase != null) {
						chartDetailWc = new ChartDetailWc(chart, label,
								new ChartP2lPhaseLegendWc(uFilter
										.getQuseryStrings().getActivitypk()
										+ "", trackPhase, chart),
								"showReportLink", resultList);
					} else {
						chartDetailWc = new ChartDetailWc(chart, label,
								new ChartP2lPhaseLegendWc(uFilter
										.getQuseryStrings().getActivitypk()
										+ "", subTrackPhase, chart),
								"showReportLink", resultList);
					}
					// End: for Major Enhancement 3.6 - F1
					// chartDetailWc.setP2lPhaseChartURL("listReportAllStatus.do?activitypk="
					// + phase.getRootActivityId());
					// LoggerHelper.logSystemDebug("SatusSubset Not Sel");
					// ends here
				}

			} else {
				chartDetailWc = new ChartDetailWc();
				chartDetailWc.setChart(chart);
				chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			}

			Collection filteredList = new ArrayList();
			// StatusSubSetBean b = getStatusSubsetDetails(uFilter);

			for (Iterator it = result.iterator(); it.hasNext();) {
				LoggerHelper.logSystemDebug("Result iterator" + result.size());
				P2lEmployeeStatus tmp = (P2lEmployeeStatus) it.next();
				if (uFilter.getFilterForm().getChkStatus()
						.equals(tmp.getStatusToDisplay())
						|| uFilter.getFilterForm().getChkStatus().equals("All")) {
					LoggerHelper.logSystemDebug("inside getSection"
							+ uFilter.getQuseryStrings().getSection());
					filteredList.add(tmp);
				}
				/* Added for 'In Progress' status change by Meenakshi */
				if ((uFilter.getFilterForm().getChkStatus()
						.equals("Registered"))
						&& (tmp.getStatus().equals("In Progress"))) {
					filteredList.add(tmp);
				}
				/* End of addition */
			}

			getRequest().getSession()
					.setAttribute("FilteredList", filteredList);

			/* Setting the session required for the getNextLevel() method */
			// getRequest().getSession().setAttribute("P2lFilteredList",filteredList);
			uFilter.setLayoutNew("3");
			PageBuilder builder = new PageBuilder();
			// Added for Major Enhancement 3.6 - F1
			DrillDownAreaWc ddArea = null;
			boolean isTreeViewVisible = checkActivityDrilldownVisibility(uSession
					.getUser());
			if (isTreeViewVisible) {
				List result1 = p2l.getActivityTree(rootActivityID);
				ddArea = new DrillDownAreaWc(result1);
			}
			// List result1 =
			// p2l.getActivityTree(trackPhase.getRootActivityId());
			// DrillDownAreaWc ddArea = new DrillDownAreaWc(result1);
			// ends here
			// Start: Modified for TRT 3.6 enhancement - F 4.1(user view
			// employee
			// grid)
			// Added for TRT Phase 2 employee grid and HQ user
			// List selectedOptEmpFields = p2l.getSelOptionalEmplFields();*/
			String selectedFields = getRequest().getParameter("newSet");
			List selectedOptEmpFields = new ArrayList();
			if (selectedFields != null) {
				String selectedFieldArray[] = selectedFields.split(",");

				for (int i = 0; i < selectedFieldArray.length; i++) {
					selectedOptEmpFields.add(selectedFieldArray[i]);
				}
			}
			HttpSession session = request.getSession();
			if (("true".equals(uFilter.getQuseryStrings().getDownloadExcel()))
					|| ("true".equals(getRequest().getParameter(
							"downloadExcelHQ")))) {
				System.out.println("session.getAttribute=="
						+ session.getAttribute("selectedOptEmpFields"));
				selectedOptEmpFields = (List) session
						.getAttribute("selectedOptEmpFields");
				// session.setAttribute("selectedOptEmpFields",selectedOptEmpFields);
			} else {
				session.setAttribute("selectedOptEmpFields",
						selectedOptEmpFields);
			}

			String selectedHQFields = getRequest().getParameter("newHQSet");
			List selectedOptHQEmpFields = new ArrayList();
			if (selectedHQFields != null) {
				String selectedHQFieldArray[] = selectedHQFields.split(",");

				for (int i = 0; i < selectedHQFieldArray.length; i++) {
					selectedOptHQEmpFields.add(selectedHQFieldArray[i]);
				}
			}
			if (("true".equals(getRequest().getParameter("downloadExcelHQ")))
					|| ("true".equals(uFilter.getQuseryStrings()
							.getDownloadExcel()))) {
				// System.out.println("session.getAttribute=="+session.getAttribute("selectedOptHQEmpFields"));
				selectedOptHQEmpFields = (List) session
						.getAttribute("selectedOptHQEmpFields");
				// session.setAttribute("selectedOptEmpFields",selectedOptEmpFields);
			} else {
				session.setAttribute("selectedOptHQEmpFields",
						selectedOptHQEmpFields);
			}
			// end employee grid and hq user
			System.out.println("selectedOptEmpFields outside=="
					+ selectedOptEmpFields);
			MainReportListReportAreaWc mainArea = new MainReportListReportAreaWc(
					filteredList, uSession.getUser(), parentActivityPk, uFilter
							.getQuseryStrings().getSection(),
					selectedOptEmpFields);
			// Added for TRT Phase 2 HQ user
			MainReportListReportAreaHQWc mainAreaHQ = new MainReportListReportAreaHQWc(
					filteredList, uSession.getUser(), parentActivityPk, uFilter
							.getQuseryStrings().getSection(),
					selectedOptHQEmpFields);
			// end hq user
			// End: Modified for TRT 3.6 enhancement - F 4.1(user view employee
			// grid)
			MassEmailWc emWc = new MassEmailWc(uSession.getUser());
			emWc.setEmailSubject(track.getTrackLabel() + " Follow-up");
			mainArea.setMassEmailWc(emWc);
			// Added for TRT Phase 2 HQ user
			mainAreaHQ.setMassEmailWc(emWc);
			// end hq user
			System.out.println("UserFilter - Layout" + uFilter.getLayoutNew());
			// Added for TRT Enchancement CSO impact
			mainArea.setSession(uSession);
			// Added for TRT Phase 2 HQ user
			mainAreaHQ.setSession(uSession);
			// end hq user
			System.out.println("User Session in getNextLevel==" + uSession);
			// End of enhancement

			uFilter.setLayoutNew("3");

			// Start: Modified for TRT 3.6 enhancement - F 4.1 (additional
			// search
			// attributes)
			EmplSearchForm eForm = new EmplSearchForm();

			if (eForm.getBuList().size() <= 0) {
				BUnitBean[] allBu = null;
				allBu = trDb.getAllBusinessUnits();
				LabelValueBean labelValueBean;
				eForm.setBu("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setBuList(labelValueBean);
				for (int i = 0; i < allBu.length; i++) {
					labelValueBean = new LabelValueBean(
							allBu[i].getBunitDesc(), allBu[i].getBunitDesc());
					eForm.setBuList(labelValueBean);
				}
			}
			if (eForm.getRoleList().size() <= 0) {
				RoleBean[] allRoles = null;
				allRoles = trDb.getAllRoleDesc();
				LabelValueBean labelValueBean;
				eForm.setRole("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setRoleList(labelValueBean);
				for (int i = 0; i < allRoles.length; i++) {
					labelValueBean = new LabelValueBean(
							allRoles[i].getRoleDesc(), allRoles[i].getRoleCd());
					eForm.setRoleList(labelValueBean);
				}
			}

			SearchFormWc searchFormWc = new SearchFormWc(eForm);
			searchFormWc.setPostUrl("searchemployee");
			searchFormWc.setTarget("myW");
			searchFormWc.setOnSubmit("DoThis12()");
			EmployeeSearchWc esearch = new EmployeeSearchWc(eForm,
					new ArrayList());
			esearch.setSearchForm(searchFormWc);
			/*
			 * MainReportListWc main = new MainReportListWc(new
			 * MainReportListChartAreaWc(chartDetailWc), new
			 * MainReportListFilterSelectAreaWc
			 * (uSession.getUser(),uFilter,trackPhase),
			 * mainArea,uSession.getUser());
			 */

			// Modified for TRT Phase 2 - Requirement no. F3 - HQ user
			MainReportListWc main = new MainReportListWc(
					new MainReportListChartAreaWc(chartDetailWc),
					new MainReportListFilterSelectAreaWc(uSession.getUser(),
							uFilter, trackPhase), mainArea, mainAreaHQ,
					uSession.getUser(), esearch);
			// end
			// End: Modified for TRT 3.6 enhancement - F 4.1 (additional search
			// attributes)

			/*
			 * if (statusToDate.trim().length() > 0 &&
			 * statusFromDate.trim().length() > 0) { try { statusFrDate =
			 * sdfOutput.parse(statusFromDate); statusTDate =
			 * sdfOutput.parse(statusToDate); } catch(ParseException e) {
			 * e.printStackTrace(); }
			 * 
			 * if(statusFrDate.compareTo(statusTDate) > 0) {
			 * main.setErrorMsg("From date cannot be greater than To date"); } }
			 * 
			 * if(EmptyToDate) { main.setErrorMsg("Please enter to date"); }
			 * if(EmptyFromDate) { main.setErrorMsg("Please enter from date"); }
			 */

			main.setPageName(label);
			main.setActivityId(activityPk);
			main.setSlice(uFilter.getFilterForm().getChkStatus());
			main.setTrack(track);
			// Added for Major Enhancement 3.6 - F1
			main.setDrillDownArea(ddArea);
			getRequest().getSession().setAttribute("parentActivityPk",
					parentActivityPk);
			getRequest().getSession().setAttribute("listlevel", fromListReport);
			// ends here
			MainReportListReportAreaWc reportList = null;
			// Start: Modified for TRT 3.6 enhancement - F 4.1
			filteredList = (ArrayList) getRequest().getSession().getAttribute(
					"FilteredList");
			getRequest().getSession().setAttribute("downloadexcelfilter",
					filteredList);
			// End of modifcation

			if ("true".equals(uFilter.getQuseryStrings().getDownloadExcel())) {
				Collection sessionfilteredList = new ArrayList();
				sessionfilteredList = (ArrayList) getRequest().getSession()
						.getAttribute("downloadexcelfilter");
				System.out.println("selectedOptEmpFields=="
						+ selectedOptEmpFields);
				if (sessionfilteredList != null) {
					reportList = new MainReportListReportAreaWc(
							sessionfilteredList, uSession.getUser(),
							parentActivityPk + "", uFilter.getQuseryStrings()
									.getSection(), selectedOptEmpFields);
				} else {
					reportList = new MainReportListReportAreaWc(filteredList,
							uSession.getUser(), parentActivityPk + "", uFilter
									.getQuseryStrings().getSection(),
							selectedOptEmpFields);
				}
				reportList.setLayout(MainReportListReportAreaWc.LAYOUT_XLS);
				// Added for TRT enhancement
				reportList.setSession(uSession);
				// end of addition
				BlankTemplateWpc page = new BlankTemplateWpc(reportList);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getResponse().addHeader("content-disposition",
						"attachment;filename=trainingreports.xls");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=300"); // HTTP
																			// 1.1
				getResponse().setHeader("Pragma", "public");
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes start here
				 * --> return new Forward("successXls");
				 */
				return new String("successXls");
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes ends here
				 * -->
				 */

			}
			// Added for TRT phase 2 hq user
			MainReportListReportAreaHQWc reportListHQ = null;
			if ("true".equals(getRequest().getParameter("downloadExcelHQ"))) {
				Collection sessionfilteredList = new ArrayList();
				sessionfilteredList = (ArrayList) getRequest().getSession()
						.getAttribute("downloadexcelfilter");
				if (sessionfilteredList != null) {
					reportListHQ = new MainReportListReportAreaHQWc(
							sessionfilteredList, uSession.getUser(),
							parentActivityPk + "", uFilter.getQuseryStrings()
									.getSection(), selectedOptHQEmpFields);
				} else {
					reportListHQ = new MainReportListReportAreaHQWc(
							filteredList, uSession.getUser(), parentActivityPk
									+ "", uFilter.getQuseryStrings()
									.getSection(), selectedOptHQEmpFields);
				}
				reportListHQ.setLayout(MainReportListReportAreaHQWc.LAYOUT_XLS);
				// Added for TRT enhancement
				reportListHQ.setSession(uSession);
				// end of addition
				BlankTemplateWpc page = new BlankTemplateWpc(reportListHQ);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getResponse().addHeader("content-disposition",
						"attachment;filename=trainingreports.xls");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=300"); // HTTP
																			// 1.1
				getResponse().setHeader("Pragma", "public");

				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes start here
				 * --> return new Forward("successXls");
				 */
				return new String("successXls");
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes ends here
				 * -->
				 */

			}
			// ////end here//////////
			getRequest().getSession().removeAttribute("downloadexcelfilter");

			MainTemplateWpc page = builder.buildPageP2l(main, "Report List",
					uSession.getUser(), "reportselect");
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward coursecomplete(){
	 */
	public String coursecomplete() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			UserSession uSession = buildUserSession();
			UserFilter uFilter = uSession.getUserFilter();
			EmployeeHandler eh = new EmployeeHandler();
			PageBuilder builder = new PageBuilder();
			CourseSearchForm form = new CourseSearchForm();

			// retrieve form values
			FormUtil.loadObject(getRequest(), form);
			P2lHandler ph = new P2lHandler();
			if (uSession.getAllP2lTracks() == null) {
				uSession.setAllP2lTracks(ph.getAllCompleteTracks());
			}
			form.setTrackPhase(uSession.getAllP2lTracks());

			CourseSearchFormWc formwc = new CourseSearchFormWc(form);

			if ("complete".equals(uSession.getUserFilter().getQuseryStrings()
					.getType())) {
				form = uSession.getCourseSearchForm();
				String[] tempVals = getRequest().getParameterValues(
						"markedpeople");
				if (tempVals != null) {
					for (int i = 0; i < tempVals.length; i++) {
						String[] tmp = tempVals[i].split(":");
						ph.insertComplete(uSession, tmp[0],
								eh.getEmployeeById(tmp[1]));
					}
				}
			}
			if ("uncomplete".equals(uSession.getUserFilter().getQuseryStrings()
					.getType())) {
				form = uSession.getCourseSearchForm();
				// form.setActivity(uFilter.getQuseryStrings().getActivitypk()+"");
				ph.deleteComplete(uSession, uFilter.getQuseryStrings()
						.getActivitypk() + "", eh.getEmployeeById(uFilter
						.getQuseryStrings().getEmplid()));
			}
			if ("true".equals(uFilter.getQuseryStrings().getDownloadExcel())) {
				// form.setActivity(uFilter.getQuseryStrings().getActivitypk()+"");
				form = uSession.getCourseSearchForm();
				List result = ph.getEmployeeActivity(form);
				System.out.println("RES:" + result.size());
				CourseCompletionResultWc resultWc = new CourseCompletionResultWc(
						result, uFilter.getQuseryStrings().getActivitypk() + "");
				resultWc.setExcel(true);
				BlankTemplateWpc page = new BlankTemplateWpc(resultWc);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getResponse().addHeader("content-disposition",
						"attachment;filename=trainingreports.xls");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");

				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes start here
				 * --> return new Forward("successXls");
				 */
				return new String("successXls");
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes ends here
				 * -->
				 */

			}
			uSession.setCourseSearchForm(form);
			List result = ph.getEmployeeActivity(form);
			System.out.println("resultsize:" + result.size());
			CourseCompletionResultWc resultWc = new CourseCompletionResultWc(
					result, form.getActivity());
			CourseCompletionWc main = new CourseCompletionWc(formwc, resultWc);

			MainTemplateWpc page = builder.buildPage(main,
					"Course Completion Page", uSession.getUser(),
					"reportselect");
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward detailpage(){
	 */
	public String detailpage() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}
			UserSession uSession = buildUserSession();
			UserFilter uFilter = uSession.getUserFilter();
			String label = "";

			PageBuilder builder = new PageBuilder();
			EmployeeHandler eHandler = new EmployeeHandler();
			List userGroups = new ArrayList();
			// Additon to obtain flag for reprenstative form
			// String Group[]={"Test","Test123"};
			System.out.println("Is User Admin?" + uSession.getUser().isAdmin());
			userGroups = uSession.getUser().getGroups();
			System.out.println("Inside phase" + userGroups.size());
			String SaveFlag = "NoAccess";
			String SubmitFlag = "NoAccess";
			// if(!uSession.getUser().isAdmin() ||
			// !uSession.getUser().isSuperAdmin())
			if (!uSession.getUser().isSuperAdmin()) {
				List result = eHandler.getAccessDetail(userGroups);
				for (Iterator it = result.iterator(); it.hasNext();) {
					GroupAccessDetail sFlag = (GroupAccessDetail) it.next();
					if (sFlag.getIsSubmit().equalsIgnoreCase("Y")) {
						SubmitFlag = "isSubmit";
					}
					if (sFlag.getIsSave().equalsIgnoreCase("Y")) {
						SaveFlag = "isSave";
					}
				}
			}
			// else if(uSession.getUser().isAdmin() ||
			// uSession.getUser().isSuperAdmin())
			else if (uSession.getUser().isSuperAdmin()) {
				SubmitFlag = "isSubmit";
				SaveFlag = "isSave";
			}
			System.out.println("Submit Flag" + SubmitFlag);
			System.out.println("Save Flag" + SaveFlag);
			String isFlag = "None";
			if (SubmitFlag.equals("isSubmit")) {
				isFlag = SubmitFlag;
			} else if (SaveFlag.equals("isSave")) {
				isFlag = SaveFlag;
			}
			System.out.println("isFlag==" + isFlag);
			getRequest().setAttribute("Accessflag", isFlag);
			/* End of addition */
			// P2lHandler pHandler = new P2lHandler();

			P2lHandler p2l = new P2lHandler();

			// Added for CSO requirements
			uSession.getUser().setScoresFlag("");
			// get Employee object
			Employee emp = eHandler.getEmployeeById(uFilter.getQuseryStrings()
					.getEmplid());

			// build employee info section
			EmployeeInfoWc employeeInfo = new EmployeeInfoWc(emp);
			employeeInfo.setImage(eHandler.getEmployeeImage(emp.getEmplId()));
			employeeInfo.setManager(eHandler.getEmployeeById(emp
					.getReportsToEmplid()));

			TrainingSummaryWc tsummary = new TrainingSummaryWc();

			// get track and phase info
			P2lTrack track = uSession.getTrack();
			String activityPk = uFilter.getQuseryStrings().getActivitypk();
			P2lTrackPhase trackPhase = p2l.getTrackPhase(activityPk + "",
					track.getTrackId());

			// if(trackPhase != null) {
			label = trackPhase.getPhaseNumber();

			// check if complete button was clicked
			if (uFilter.getQuseryStrings().getCactivityid() > 0) {
				p2l.insertComplete(uSession, uFilter.getQuseryStrings()
						.getCactivityid() + "", emp);
				uFilter.getQuseryStrings().setCactivitid("0");
			}
			Map scodes = p2l.getSpecialCodes();
			P2lActivityStatus.setCodes(scodes);

			P2lActivityStatus testresult = p2l.getPhaseDetail(emp.getGuid(),
					trackPhase);
			// }
			/*
			 * else{ SubActivityBean bean = p2l.getActivityDetails(activityPk);
			 * label = bean.getActivityName(); if (
			 * uFilter.getQuseryStrings().getCactivityid() > 0) {
			 * p2l.insertComplete(uSession,
			 * uFilter.getQuseryStrings().getCactivityid()+"" ,emp);
			 * uFilter.getQuseryStrings().setCactivitid("0"); } Map scodes =
			 * p2l.getSpecialCodes(); P2lActivityStatus.setCodes(scodes);
			 * 
			 * //P2lActivityStatus testresult = p2l.getPhaseDetail(
			 * emp.getGuid(), bean); }
			 */
			PhaseTrainingDetailWc pDetail = new PhaseTrainingDetailWc(
					new ArrayList(), emp, track, trackPhase, uSession, isFlag);

			/*
			 * Log: Added by Meenakshi.M.B on 14-May-2010 Added for CSO
			 * requirements
			 */
			// System.out.println("SCORES VISIBILITY IN P2LCONTROLLER"+uSession.getUser().getScoresVisible());
			// System.out.println("Type of logged in User"+uSession.getUser().getSalesPositionTypeCd());
			// System.out.println("Employee Sales Position Type Code"+emp.getSalesPostionTypeCode());
			if (uSession.getUser().getScoresVisible().equals("Y")
					|| uSession.getUser().getSalesPositionTypeCd()
							.equals(emp.getSalesPostionTypeCode())) {
				uSession.getUser().setScoresFlag("Y");
			}

			// Start: Added for TRT Phase 2 Enhancement. Added Gap report and
			// Training path for employees.
			// Get Gap report
			List statusForGapList = new ArrayList();
			String emplyID = emp.getEmplId();
			// statusForGapList=eHandler.getProductStatusForGap("009228");
			statusForGapList = eHandler.getProductStatusForGap(emplyID);
			List statusForNotGapList = new ArrayList();
			// statusForNotGapList=eHandler.getProductStatusForNotGap("009228");
			statusForNotGapList = eHandler.getProductStatusForNotGap(emplyID);
			EmployeeGapReportWc empGapWc = new EmployeeGapReportWc();
			empGapWc.setStatusForGapList(statusForGapList);
			empGapWc.setStatusForNotGapList(statusForNotGapList);
			empGapWc.setUser(emplyID);

			String breadCrumbs = "<a href='begin?track=" + track.getTrackId()
					+ "'>" + track.getTrackLabel() + "</a> / "
					+ "<a href='listreport?section="
					+ uFilter.getFilterForm().getChkStatus() + "&activitypk="
					+ activityPk + "'>" + label;
			System.out.println("in detail page ^^^^^^^^^^==" + breadCrumbs);
			HttpSession session = getSession();
			session.setAttribute("breadCrumbs", breadCrumbs);
			// Get Training path for the employee.
			List empTrainingPath = new ArrayList();
			List empTrainingPathDisplay = new ArrayList();
			empTrainingPath = eHandler.getTrainingPath(emp);
			P2lHandler pHandler = new P2lHandler();
			TrainingPathDisplayWc pathWc = new TrainingPathDisplayWc();
			pathWc.setUser(emplyID);
			Map displayPath = new HashMap();
			int i = 1;

			System.out.println("trainingpath size=" + empTrainingPath.size());
			if (empTrainingPath.size() != 0) {
				Iterator pathIterator = empTrainingPath.iterator();
				// System.out.println(empTrainingPath.iterator());

				while (pathIterator.hasNext()) {
					Map pathCode = (Map) pathIterator.next();

					String activityID = (String) pathCode.get("CODE");
					// System.out.println("activityID="+activityID);
					P2lActivityStatus p2LResult = pHandler
							.getSinglePhaseDetail(emp.getGuid(), activityID);
					// System.out.println("Status = "+p2LResult.getStatus());
					pathCode.put("STATUS", p2LResult.getStatus());
					displayPath = pathCode;
					// System.out.println("displayPath"+displayPath.get("CODE"));
					// System.out.println("displayPath STATUS"+displayPath.get("STATUS"));
					empTrainingPathDisplay.add(displayPath);
					i++;
				}
			} else {
				pathWc.setMessage("No Training Path Configured.");
			}

			// System.out.println("emp.getBusinessUnit"+emp.getBusinessUnit());
			// System.out.println("emp.getRole"+emp.getRole());
			// System.out.println("emp.getSalesPositionId"+emp.getGroupCD());
			// System.out.println("Size = "+empTrainingPathDisplay.size());
			pathWc.setTrainingPath(empTrainingPathDisplay);

			// TRT Phase 2 changes Ends here.

			pDetail.setStatus(testresult);
			DetailPageWc main = new DetailPageWc(employeeInfo, tsummary,
					pDetail, uSession, pathWc, empGapWc);
			main.setTrack(track);
			main.setActivityId(trackPhase.getRootActivityId());
			main.setPageName(label);
			if ("debug".equals(uSession.getUserFilter().getQuseryStrings()
					.getMode())) {
				pDetail.setDebug(true);
			}
			MainTemplateWpc page = builder.buildPageP2l(main, "Report List",
					uSession.getUser(), "reportselect");
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward getNextLevel(){
	 */
	public String getNextLevel() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			UserSession uSession = buildUserSession();
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String salesId = null;
			String salesLevel = null;
			String salesMultiple = null;
			String salesValue = null;
			String salesOrg = null;
			AppQueryStrings qString = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qString);
			salesId = qString.getSales();
			salesLevel = qString.getSaleslevel();
			salesMultiple = qString.getMultiple();
			salesValue = qString.getSalesvalue();
			salesOrg = qString.getSalesorg();

			// System.out.println("***GEO ID ****"+salesId);
			// System.out.println("*** USER TYPE ****"+salesLevel);
			// System.out.println("*** GEO MULTIPLE ****"+salesMultiple);
			System.out.println("salesOrg" + salesOrg);
			uSession.getUserFilterForm().setSalesOrg(salesOrg);
			/* This loop is to handle users having multiple geographies */

			/*
			 * if(salesMultiple!=null && salesMultiple!=null) { HashMap tempMap=
			 * new HashMap(); String tempId=null;
			 * tempMap=uSession.getUser().getMultipleGeoMap(); tempId = (String)
			 * tempMap.get(salesId);
			 * System.out.println("TEMP GEO ID---------------------"+tempId);
			 * UserTerritory terr =
			 * Service.getServiceFactory().getTerritoryHandler()
			 * .getUserGeography(uSession.getUser().getEmplid(),tempGeoId);
			 * uSession.getUser().setUserTerritory(terr);
			 * uSession.getUserFilterForm().setSelectedGeo(geoValue); /*
			 * Resetting the first Geo drop down in case the geo ID of the user
			 * is changed uSession.getUserFilterForm().clearFirstList();
			 * ArrayList firstGeo= new ArrayList(); firstGeo
			 * =uSession.getUser().getUserTerritory().getFirstGeoDropdown();
			 * LabelValueBean labelValueBean ;
			 * uSession.getUserFilterForm().setLevel1("All");
			 * FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
			 * labelValueBean=new LabelValueBean("All","All");
			 * uSession.getUserFilterForm().setFirstList(labelValueBean);
			 * for(int i=0;i<firstGeo.size();i++) { LabelValueBean
			 * tempLabelValueBean; tempLabelValueBean =
			 * (LabelValueBean)firstGeo.get(i); labelValueBean=new
			 * LabelValueBean((String)tempLabelValueBean.getLabel(
			 * ),(String)tempLabelValueBean.getValue());
			 * uSession.getUserFilterForm().setFirstList(labelValueBean); }
			 * uSession.getUserFilterForm().clearSecondList();
			 * uSession.getUserFilterForm().clearThirdList();
			 * uSession.getUserFilterForm().clearFourthList();
			 * uSession.getUserFilterForm().clearFifthList();
			 * uSession.getUserFilterForm().clearSixthList();
			 * uSession.getUserFilterForm().clearSeventhList();
			 * uSession.getUserFilterForm().clearEighthList();
			 * uSession.getUserFilterForm().clearNinthList();
			 * uSession.getUserFilterForm().clearTenthList(); }
			 */

			UserTerritory ut = uSession.getUser().getUserTerritory();

			if (salesId != null && salesLevel != null) {
				if (salesLevel.toString().equals("2")) {
					ArrayList nextLevel = new ArrayList();
					LabelValueBean labelValueBean;
					if (!salesId.equalsIgnoreCase("All")) {
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
						FormUtil.loadObject(getRequest(),
								uSession.getUserFilterForm());
						// System.out.println("Size of next level"+nextLevel.size());
						if (nextLevel.size() > 0) {
							labelValueBean = new LabelValueBean("All", "All");
							uSession.getUserFilterForm().setSecondList(
									labelValueBean);
							for (int i = 0; i < nextLevel.size(); i++) {
								LabelValueBean tempLabelValueBean;
								tempLabelValueBean = (LabelValueBean) nextLevel
										.get(i);
								labelValueBean = new LabelValueBean(
										(String) tempLabelValueBean.getLabel(),
										(String) tempLabelValueBean.getValue());
								uSession.getUserFilterForm().setSecondList(
										labelValueBean);
							}
						}
					} else if (salesId.equalsIgnoreCase("All")) {
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
				} else if (salesLevel.toString().equals("3")) {
					// System.out.println("---- Inside second loop-----------"+salesId);
					ArrayList nextLevel2 = new ArrayList();
					if (!salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel2(salesValue);
						nextLevel2 = ut
								.getDropdownSalesPositionDesc(salesValue);
						uSession.getUserFilterForm().clearThirdList();
						uSession.getUserFilterForm().clearFourthList();
						uSession.getUserFilterForm().clearFifthList();
						uSession.getUserFilterForm().clearSixthList();
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
						LabelValueBean labelValueBean1;
						uSession.getUserFilterForm().setLevel3("All");
						FormUtil.loadObject(getRequest(),
								uSession.getUserFilterForm());
						if (nextLevel2.size() > 0) {
							labelValueBean1 = new LabelValueBean("All", "All");
							uSession.getUserFilterForm().setThirdList(
									labelValueBean1);
							// System.out.println("SIZE OF NEXT LEVEL"+nextLevel2.size());
							for (int i = 0; i < nextLevel2.size(); i++) {
								LabelValueBean tempLabelValueBean;
								tempLabelValueBean = (LabelValueBean) nextLevel2
										.get(i);
								labelValueBean1 = new LabelValueBean(
										(String) tempLabelValueBean.getLabel(),
										(String) tempLabelValueBean.getValue());
								uSession.getUserFilterForm().setThirdList(
										labelValueBean1);
							}
						}
					} else if (salesId.equalsIgnoreCase("All")) {
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
				} else if (salesLevel.toString().equals("4")) {
					// System.out.println("---- Inside third loop-----------"+salesId);
					ArrayList nextLevel3 = new ArrayList();
					if (!salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel3(salesValue);
						nextLevel3 = ut
								.getDropdownSalesPositionDesc(salesValue);
						uSession.getUserFilterForm().clearFourthList();
						uSession.getUserFilterForm().clearFifthList();
						uSession.getUserFilterForm().clearSixthList();
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
						LabelValueBean labelValueBean1;
						uSession.getUserFilterForm().setLevel4("All");
						FormUtil.loadObject(getRequest(),
								uSession.getUserFilterForm());
						if (nextLevel3.size() > 0) {
							labelValueBean1 = new LabelValueBean("All", "All");
							uSession.getUserFilterForm().setFourthList(
									labelValueBean1);
							// System.out.println("SIZE OF NEXT LEVEL"+nextLevel3.size());
							for (int i = 0; i < nextLevel3.size(); i++) {
								LabelValueBean tempLabelValueBean;
								tempLabelValueBean = (LabelValueBean) nextLevel3
										.get(i);
								labelValueBean1 = new LabelValueBean(
										(String) tempLabelValueBean.getLabel(),
										(String) tempLabelValueBean.getValue());
								uSession.getUserFilterForm().setFourthList(
										labelValueBean1);
							}
						}
					} else if (salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel3(salesValue);
						uSession.getUserFilterForm().clearFourthList();
						uSession.getUserFilterForm().clearFifthList();
						uSession.getUserFilterForm().clearSixthList();
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
					}
				} else if (salesLevel.toString().equals("5")) {
					// System.out.println("---- Inside fourth loop-----------"+salesId);
					ArrayList nextLevel4 = new ArrayList();
					if (!salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel4(salesValue);
						nextLevel4 = ut
								.getDropdownSalesPositionDesc(salesValue);
						uSession.getUserFilterForm().clearFifthList();
						uSession.getUserFilterForm().clearSixthList();
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
						LabelValueBean labelValueBean1;
						uSession.getUserFilterForm().setLevel5("All");
						FormUtil.loadObject(getRequest(),
								uSession.getUserFilterForm());
						if (nextLevel4.size() > 0) {
							labelValueBean1 = new LabelValueBean("All", "All");
							uSession.getUserFilterForm().setFifthList(
									labelValueBean1);
							// System.out.println("SIZE OF NEXT LEVEL"+nextLevel4.size());
							for (int i = 0; i < nextLevel4.size(); i++) {
								LabelValueBean tempLabelValueBean;
								tempLabelValueBean = (LabelValueBean) nextLevel4
										.get(i);
								labelValueBean1 = new LabelValueBean(
										(String) tempLabelValueBean.getLabel(),
										(String) tempLabelValueBean.getValue());
								uSession.getUserFilterForm().setFifthList(
										labelValueBean1);
							}
						}
					} else if (salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel4(salesValue);
						uSession.getUserFilterForm().clearFifthList();
						uSession.getUserFilterForm().clearSixthList();
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
					}
				} else if (salesLevel.toString().equals("6")) {
					// System.out.println("---- Inside fifth loop-----------"+salesId);
					ArrayList nextLevel5 = new ArrayList();
					if (!salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel5(salesValue);
						nextLevel5 = ut
								.getDropdownSalesPositionDesc(salesValue);
						uSession.getUserFilterForm().clearSixthList();
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
						LabelValueBean labelValueBean1;
						uSession.getUserFilterForm().setLevel6("All");
						FormUtil.loadObject(getRequest(),
								uSession.getUserFilterForm());
						if (nextLevel5.size() > 0) {
							labelValueBean1 = new LabelValueBean("All", "All");
							uSession.getUserFilterForm().setSixthList(
									labelValueBean1);
							// System.out.println("SIZE OF NEXT LEVEL"+nextLevel5.size());
							for (int i = 0; i < nextLevel5.size(); i++) {
								LabelValueBean tempLabelValueBean;
								tempLabelValueBean = (LabelValueBean) nextLevel5
										.get(i);
								labelValueBean1 = new LabelValueBean(
										(String) tempLabelValueBean.getLabel(),
										(String) tempLabelValueBean.getValue());
								uSession.getUserFilterForm().setSixthList(
										labelValueBean1);
							}
						}
					} else if (salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel5(salesValue);
						uSession.getUserFilterForm().clearSixthList();
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
					}
				} else if (salesLevel.toString().equals("7")) {
					// System.out.println("---- Inside level 7 loop-----------"+salesId);
					ArrayList nextLevel6 = new ArrayList();
					if (!salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel6(salesValue);
						nextLevel6 = ut
								.getDropdownSalesPositionDesc(salesValue);
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
						LabelValueBean labelValueBean1;
						uSession.getUserFilterForm().setLevel7("All");
						FormUtil.loadObject(getRequest(),
								uSession.getUserFilterForm());
						if (nextLevel6.size() > 0) {
							labelValueBean1 = new LabelValueBean("All", "All");
							uSession.getUserFilterForm().setSeventhList(
									labelValueBean1);
							// System.out.println("SIZE OF NEXT LEVEL"+nextLevel6.size());
							for (int i = 0; i < nextLevel6.size(); i++) {
								LabelValueBean tempLabelValueBean;
								tempLabelValueBean = (LabelValueBean) nextLevel6
										.get(i);
								labelValueBean1 = new LabelValueBean(
										(String) tempLabelValueBean.getLabel(),
										(String) tempLabelValueBean.getValue());
								uSession.getUserFilterForm().setSeventhList(
										labelValueBean1);
							}
						}
					} else if (salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel6(salesValue);
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
					}

				} else if (salesLevel.toString().equals("8")) {
					// System.out.println("---- Inside sixth loop-----------"+salesId);
					ArrayList nextLevel7 = new ArrayList();
					if (!salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel7(salesValue);
						nextLevel7 = ut
								.getDropdownSalesPositionDesc(salesValue);
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
						LabelValueBean labelValueBean1;
						uSession.getUserFilterForm().setLevel8("All");
						FormUtil.loadObject(getRequest(),
								uSession.getUserFilterForm());
						if (nextLevel7.size() > 0) {
							labelValueBean1 = new LabelValueBean("All", "All");
							uSession.getUserFilterForm().setEighthList(
									labelValueBean1);
							for (int i = 0; i < nextLevel7.size(); i++) {
								LabelValueBean tempLabelValueBean;
								tempLabelValueBean = (LabelValueBean) nextLevel7
										.get(i);
								labelValueBean1 = new LabelValueBean(
										(String) tempLabelValueBean.getLabel(),
										(String) tempLabelValueBean.getValue());
								uSession.getUserFilterForm().setEighthList(
										labelValueBean1);
							}
						}
					} else if (salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel7(salesValue);
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
					}
				} else if (salesLevel.toString().equals("9")) {

					// System.out.println("---- Inside sixth loop-----------"+salesId);
					ArrayList nextLevel8 = new ArrayList();
					if (!salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel8(salesValue);
						nextLevel8 = ut
								.getDropdownSalesPositionDesc(salesValue);
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
						LabelValueBean labelValueBean1;
						uSession.getUserFilterForm().setLevel9("All");
						FormUtil.loadObject(getRequest(),
								uSession.getUserFilterForm());
						if (nextLevel8.size() > 0) {
							labelValueBean1 = new LabelValueBean("All", "All");
							uSession.getUserFilterForm().setNinthList(
									labelValueBean1);
							for (int i = 0; i < nextLevel8.size(); i++) {
								LabelValueBean tempLabelValueBean;
								tempLabelValueBean = (LabelValueBean) nextLevel8
										.get(i);
								labelValueBean1 = new LabelValueBean(
										(String) tempLabelValueBean.getLabel(),
										(String) tempLabelValueBean.getValue());
								uSession.getUserFilterForm().setNinthList(
										labelValueBean1);

							}
						}
					} else if (salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel8(salesValue);
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
					}
				} else if (salesLevel.toString().equals("10")) {
					// System.out.println("---- Inside sixth loop-----------"+salesId);
					ArrayList nextLevel9 = new ArrayList();
					if (!salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel9(salesValue);
						nextLevel9 = ut
								.getDropdownSalesPositionDesc(salesValue);
						uSession.getUserFilterForm().clearTenthList();
						LabelValueBean labelValueBean1;
						uSession.getUserFilterForm().setLevel10("All");
						FormUtil.loadObject(getRequest(),
								uSession.getUserFilterForm());
						if (nextLevel9.size() > 0) {
							labelValueBean1 = new LabelValueBean("All", "All");
							uSession.getUserFilterForm().setTenthList(
									labelValueBean1);
							for (int i = 0; i < nextLevel9.size(); i++) {
								LabelValueBean tempLabelValueBean;
								tempLabelValueBean = (LabelValueBean) nextLevel9
										.get(i);
								labelValueBean1 = new LabelValueBean(
										(String) tempLabelValueBean.getLabel(),
										(String) tempLabelValueBean.getValue());
								uSession.getUserFilterForm().setTenthList(
										labelValueBean1);
							}
						}
					} else if (salesId.equalsIgnoreCase("All")) {
						filterForm.setLevel9(salesValue);
						uSession.getUserFilterForm().clearTenthList();
					}

				} else if (salesLevel.toString().equals("11")) {

					uSession.getUserFilterForm().setLevel10(salesValue);
				}

			}

			UserFilter uFilter = uSession.getUserFilter();
			uFilter.setRefresh(false);
			System.out.println("TYPE IN PHASECONTROLLER"
					+ uFilter.getFilterForm().getSelectionType());
			/* Code for drop downs--To be uncommented later */
			TerritorySelectWc territorySelect = new TerritorySelectWc(
					uSession.getUserFilterForm(), uSession.getUser()
							.getUserTerritory(), "/TrainingReports/phase/begin");
			territorySelect.setShowTeam(true);
			territorySelect.setShowMultipleGeos(true);
			territorySelect.setLayout(3);
			Territory terr = Service.getServiceFactory().getTerritoryHandler()
					.getTerritory(uFilter);

			System.out.println("Gettttttttting session"
					+ uSession.getCurrentSlice());

			/* Added for Detail report */
			// if(uSession.getCurrentSlice()==null ||
			// uSession.getCurrentSlice()=="")
			// {
			List charts = new ArrayList();
			Collection result = new ArrayList();
			P2lHandler p2l = new P2lHandler();

			P2lTrack track = uSession.getTrack();
			if (track == null
					|| !Util.isEmpty(uFilter.getQuseryStrings().getTrack())) {
				track = p2l.getTrack(uFilter.getQuseryStrings().getTrack());
				uSession.setTrack(track);
			}
			/*
			 * Commenting out the DB calls since we are getting the object in
			 * the session
			 */
			/*
			 * PieChart chart=null;
			 * 
			 * for (Iterator it = track.getCompletePhaseList().iterator();
			 * it.hasNext();) { P2lTrackPhase phase = (P2lTrackPhase)it.next();
			 * if ( P2lTrackPhase.EMPTY_FLAG.equals(phase.getPhaseNumber()) ) {
			 * ChartDetailWc chartDetailWc = new ChartDetailWc();
			 * chartDetailWc.setLayout(ChartDetailWc.LAYOUT_EMPTY);
			 * charts.add(chartDetailWc); } else { String label =
			 * track.getTrackLabel() + " : " + phase.getPhaseNumber(); //chart =
			 * getPhaseChart
			 * (phase,label,uFilter,phase.getAlttActivityId(),result, false,
			 * ""); //473181 //added for RBU chart =
			 * getPhaseChart(ut,phase,label
			 * ,uFilter,phase.getAlttActivityId(),result, false, "");
			 * System.out.println("Before getting session object"); /*
			 * if((PieChart
			 * )getRequest().getSession().getAttribute("Current Chart") !=null)
			 * { chart=
			 * (PieChart)getRequest().getSession().getAttribute("Current Chart"
			 * ); } else{ chart =
			 * getPhaseChart(ut,phase,label,uFilter,phase.getAlttActivityId
			 * (),result, false, ""); }
			 */
			// System.out.println("Before getting session object"+chart);
			// ended for RBU
			/*
			 * if ( chart.getCount() > 0 ) { int layout =
			 * ChartLegendWc.LAYOUT_PHASE; if ( phase.getApprovalStatus()) {
			 * layout = ChartLegendWc.LAYOUT_PHASE_PENDING; } ChartDetailWc
			 * chartDetailWc = new ChartDetailWc( chart, label ,new
			 * ChartP2lLegendWc( phase.getRootActivityId(),phase ) );
			 * charts.add(chartDetailWc); } else { ChartDetailWc chartDetailWc =
			 * new ChartDetailWc(); chartDetailWc.setChart(chart);
			 * chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			 * charts.add(chartDetailWc); } } }
			 */
			/* Getting the charts object from the session */
			charts = (List) getRequest().getSession().getAttribute(
					"P2lCurrentChart");

			ChartListWc chartListWc = new ChartListWc(charts);
			chartListWc.setLayout(ChartListWc.LAYOUT_2COL);
			// ChartHeaderWc chartHeaderWc = new ChartHeaderWc( "","", "", 10);
			ChartHeaderWc chartHeaderWc = new ChartHeaderWc(uSession.getUser()
					.getBusinessUnit(), uSession.getUser()
					.getSalesPostionDesc(), uSession.getUser().getGeoType(), 10);
			chartHeaderWc.setJsp(AppConst.JSP_LOC
					+ "/components/chart/chartHeaderBasic.jsp");

			// Start: Modified for TRT 3.6 enhancement - F 4.1 (additional
			// search
			// attributes)
			EmplSearchForm eForm = new EmplSearchForm();

			if (eForm.getBuList().size() <= 0) {
				BUnitBean[] allBu = null;
				allBu = trDb.getAllBusinessUnits();
				LabelValueBean labelValueBean;
				eForm.setBu("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setBuList(labelValueBean);
				for (int i = 0; i < allBu.length; i++) {
					labelValueBean = new LabelValueBean(
							allBu[i].getBunitDesc(), allBu[i].getBunitDesc());
					eForm.setBuList(labelValueBean);
				}
			}
			if (eForm.getRoleList().size() <= 0) {
				RoleBean[] allRoles = null;
				allRoles = trDb.getAllRoleDesc();
				LabelValueBean labelValueBean;
				eForm.setRole("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setRoleList(labelValueBean);
				for (int i = 0; i < allRoles.length; i++) {
					labelValueBean = new LabelValueBean(
							allRoles[i].getRoleDesc(), allRoles[i].getRoleCd());
					eForm.setRoleList(labelValueBean);
				}
			}

			SearchFormWc searchFormWc = new SearchFormWc(eForm);
			searchFormWc.setPostUrl("searchemployee");
			searchFormWc.setTarget("myW");
			searchFormWc.setOnSubmit("DoThis12()");
			EmployeeSearchWc esearch = new EmployeeSearchWc(eForm,
					new ArrayList());
			esearch.setSearchForm(searchFormWc);

			RightBarPl2Wc right = new RightBarPl2Wc(territorySelect,
					uSession.getUser(), esearch);
			chartHeaderWc.setTrack(track);
			ChartIndexWc main = new ChartIndexWc(chartHeaderWc, chartListWc,
					right, 10);

			PageBuilder builder = new PageBuilder();
			MainTemplateWpc page = builder.buildPageP2l(main, "Chart Index",
					uSession.getUser(), "reportselect");
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			// }

			// uSession.setCurrentSlice("");

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward getNextP2lPhaseLevel(){
	 */
		public String getNextP2lPhaseLevel() {
			try{
		/** * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		UserSession uSession = buildUserSession();
		TerritoryFilterForm filterForm = uSession.getUserFilterForm();
		String salesId = null;
		String salesLevel = null;
		String salesMultiple = null;
		String salesValue = null;
		String salesOrg = null;
		String rootActivityID = "";
		AppQueryStrings qString = new AppQueryStrings();
		FormUtil.loadObject(getRequest(), qString);
		salesId = qString.getSales();
		salesLevel = qString.getSaleslevel();
		salesMultiple = qString.getMultiple();
		salesValue = qString.getSalesvalue();
		salesOrg = qString.getSalesorg();

		LoggerHelper.logSystemDebug("get salesId" + salesId);
		LoggerHelper.logSystemDebug("get salesId" + salesLevel);
		System.out.println("***GEO ID ****" + salesId);
		System.out.println("*** USER TYPE ****" + salesLevel);
		System.out.println("*** GEO MULTIPLE ****" + salesMultiple);

		uSession.getUserFilterForm().setSalesOrg(salesOrg);

		/* This loop is to handle users having multiple geographies */

		/*
		 * if(geoId!=null && geoMultiple!=null) { HashMap tempMap= new
		 * HashMap(); String tempGeoId=null;
		 * tempMap=uSession.getUser().getMultipleGeoMap(); tempGeoId = (String)
		 * tempMap.get(geoId);
		 * System.out.println("TEMP GEO ID---------------------"+tempGeoId);
		 * UserTerritory terr =
		 * Service.getServiceFactory().getTerritoryHandler()
		 * .getUserGeography(uSession.getUser().getEmplid(),tempGeoId);
		 * uSession.getUser().setUserTerritory(terr);
		 * uSession.getUserFilterForm().setSelectedGeo(geoValue); /* Resetting
		 * the first Geo drop down in case the geo ID of the user is changed
		 * uSession.getUserFilterForm().clearFirstList(); ArrayList firstGeo=
		 * new ArrayList(); firstGeo
		 * =uSession.getUser().getUserTerritory().getFirstGeoDropdown();
		 * LabelValueBean labelValueBean ;
		 * uSession.getUserFilterForm().setLevel1("All");
		 * FormUtil.loadObject(getRequest(),uSession.getUserFilterForm());
		 * labelValueBean=new LabelValueBean("All","All");
		 * uSession.getUserFilterForm().setFirstList(labelValueBean); for(int
		 * i=0;i<firstGeo.size();i++) { LabelValueBean tempLabelValueBean;
		 * tempLabelValueBean = (LabelValueBean)firstGeo.get(i);
		 * labelValueBean=new
		 * LabelValueBean((String)tempLabelValueBean.getLabel(
		 * ),(String)tempLabelValueBean.getValue());
		 * uSession.getUserFilterForm().setFirstList(labelValueBean); }
		 * uSession.getUserFilterForm().clearSecondList();
		 * uSession.getUserFilterForm().clearThirdList();
		 * uSession.getUserFilterForm().clearFourthList();
		 * uSession.getUserFilterForm().clearFifthList();
		 * uSession.getUserFilterForm().clearSixthList();
		 * uSession.getUserFilterForm().clearSeventhList();
		 * uSession.getUserFilterForm().clearEighthList();
		 * uSession.getUserFilterForm().clearNinthList();
		 * uSession.getUserFilterForm().clearTenthList(); }
		 */

		UserTerritory ut = uSession.getUser().getUserTerritory();

		if (salesId != null && salesLevel != null) {
			if (salesLevel.toString().equals("2")) {
				ArrayList nextLevel = new ArrayList();
				LabelValueBean labelValueBean;
				if (!salesId.equalsIgnoreCase("All")) {

					LoggerHelper.logSystemDebug("inside saleslevel 2");
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
					FormUtil.loadObject(getRequest(),
							uSession.getUserFilterForm());
					System.out.println("Size of next level" + nextLevel.size());
					if (nextLevel.size() > 0) {
						labelValueBean = new LabelValueBean("All", "All");
						uSession.getUserFilterForm().setSecondList(
								labelValueBean);
						for (int i = 0; i < nextLevel.size(); i++) {
							LabelValueBean tempLabelValueBean;
							tempLabelValueBean = (LabelValueBean) nextLevel
									.get(i);
							labelValueBean = new LabelValueBean(
									(String) tempLabelValueBean.getLabel(),
									(String) tempLabelValueBean.getValue());
							uSession.getUserFilterForm().setSecondList(
									labelValueBean);
						}
					}
				} else if (salesId.equalsIgnoreCase("All")) {
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
			} else if (salesLevel.toString().equals("3")) {
				System.out.println("---- Inside second loop-----------"
						+ salesId);
				ArrayList nextLevel2 = new ArrayList();
				if (!salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel2(salesValue);
					nextLevel2 = ut.getDropdownSalesPositionDesc(salesValue);
					LabelValueBean labelValueBean1;
					uSession.getUserFilterForm().setLevel3("All");
					FormUtil.loadObject(getRequest(),
							uSession.getUserFilterForm());
					if (nextLevel2.size() > 0) {
						labelValueBean1 = new LabelValueBean("All", "All");
						uSession.getUserFilterForm().setThirdList(
								labelValueBean1);
						uSession.getUserFilterForm().clearFourthList();
						uSession.getUserFilterForm().clearFifthList();
						uSession.getUserFilterForm().clearSixthList();
						uSession.getUserFilterForm().clearSeventhList();
						uSession.getUserFilterForm().clearEighthList();
						uSession.getUserFilterForm().clearNinthList();
						uSession.getUserFilterForm().clearTenthList();
						System.out.println("SIZE OF NEXT LEVEL"
								+ nextLevel2.size());
						for (int i = 0; i < nextLevel2.size(); i++) {
							LabelValueBean tempLabelValueBean;
							tempLabelValueBean = (LabelValueBean) nextLevel2
									.get(i);
							labelValueBean1 = new LabelValueBean(
									(String) tempLabelValueBean.getLabel(),
									(String) tempLabelValueBean.getValue());
							uSession.getUserFilterForm().setThirdList(
									labelValueBean1);
						}
					}
				} else if (salesId.equalsIgnoreCase("All")) {
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
			} else if (salesLevel.toString().equals("4")) {
				System.out.println("---- Inside third loop-----------"
						+ salesId);
				ArrayList nextLevel3 = new ArrayList();
				if (!salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel3(salesValue);
					nextLevel3 = ut.getDropdownSalesPositionDesc(salesValue);
					uSession.getUserFilterForm().clearFourthList();
					uSession.getUserFilterForm().clearFifthList();
					uSession.getUserFilterForm().clearSixthList();
					uSession.getUserFilterForm().clearSeventhList();
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
					LabelValueBean labelValueBean1;
					uSession.getUserFilterForm().setLevel4("All");
					FormUtil.loadObject(getRequest(),
							uSession.getUserFilterForm());
					if (nextLevel3.size() > 0) {
						labelValueBean1 = new LabelValueBean("All", "All");
						uSession.getUserFilterForm().setFourthList(
								labelValueBean1);
						System.out.println("SIZE OF NEXT LEVEL"
								+ nextLevel3.size());
						for (int i = 0; i < nextLevel3.size(); i++) {
							LabelValueBean tempLabelValueBean;
							tempLabelValueBean = (LabelValueBean) nextLevel3
									.get(i);
							labelValueBean1 = new LabelValueBean(
									(String) tempLabelValueBean.getLabel(),
									(String) tempLabelValueBean.getValue());
							uSession.getUserFilterForm().setFourthList(
									labelValueBean1);
						}
					}
				} else if (salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel3(salesValue);
					uSession.getUserFilterForm().clearFourthList();
					uSession.getUserFilterForm().clearFifthList();
					uSession.getUserFilterForm().clearSixthList();
					uSession.getUserFilterForm().clearSeventhList();
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
				}
			} else if (salesLevel.toString().equals("5")) {
				System.out.println("---- Inside fourth loop-----------"
						+ salesId);
				ArrayList nextLevel4 = new ArrayList();
				if (!salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel4(salesValue);
					nextLevel4 = ut.getDropdownSalesPositionDesc(salesValue);
					uSession.getUserFilterForm().clearFifthList();
					uSession.getUserFilterForm().clearSixthList();
					uSession.getUserFilterForm().clearSeventhList();
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
					LabelValueBean labelValueBean1;
					uSession.getUserFilterForm().setLevel5("All");
					FormUtil.loadObject(getRequest(),
							uSession.getUserFilterForm());
					if (nextLevel4.size() > 0) {
						labelValueBean1 = new LabelValueBean("All", "All");
						uSession.getUserFilterForm().setFifthList(
								labelValueBean1);
						System.out.println("SIZE OF NEXT LEVEL"
								+ nextLevel4.size());
						for (int i = 0; i < nextLevel4.size(); i++) {
							LabelValueBean tempLabelValueBean;
							tempLabelValueBean = (LabelValueBean) nextLevel4
									.get(i);
							labelValueBean1 = new LabelValueBean(
									(String) tempLabelValueBean.getLabel(),
									(String) tempLabelValueBean.getValue());
							uSession.getUserFilterForm().setFifthList(
									labelValueBean1);
						}
					}
				} else if (salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel4(salesValue);
					uSession.getUserFilterForm().clearFifthList();
					uSession.getUserFilterForm().clearSixthList();
					uSession.getUserFilterForm().clearSeventhList();
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
				}
			} else if (salesLevel.toString().equals("6")) {
				System.out.println("---- Inside fifth loop-----------"
						+ salesId);
				ArrayList nextLevel5 = new ArrayList();
				if (!salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel5(salesValue);
					nextLevel5 = ut.getDropdownSalesPositionDesc(salesValue);
					uSession.getUserFilterForm().clearSixthList();
					uSession.getUserFilterForm().clearSeventhList();
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
					LabelValueBean labelValueBean1;
					uSession.getUserFilterForm().setLevel6("All");
					FormUtil.loadObject(getRequest(),
							uSession.getUserFilterForm());
					if (nextLevel5.size() > 0) {
						labelValueBean1 = new LabelValueBean("All", "All");
						uSession.getUserFilterForm().setSixthList(
								labelValueBean1);
						System.out.println("SIZE OF NEXT LEVEL"
								+ nextLevel5.size());
						for (int i = 0; i < nextLevel5.size(); i++) {
							LabelValueBean tempLabelValueBean;
							tempLabelValueBean = (LabelValueBean) nextLevel5
									.get(i);
							labelValueBean1 = new LabelValueBean(
									(String) tempLabelValueBean.getLabel(),
									(String) tempLabelValueBean.getValue());
							uSession.getUserFilterForm().setSixthList(
									labelValueBean1);
						}
					}
				} else if (salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel5(salesValue);
					uSession.getUserFilterForm().clearSixthList();
					uSession.getUserFilterForm().clearSeventhList();
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
				}
			} else if (salesLevel.toString().equals("7")) {
				System.out.println("---- Inside fifth loop-----------"
						+ salesId);
				ArrayList nextLevel6 = new ArrayList();
				if (!salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel6(salesValue);
					nextLevel6 = ut.getDropdownSalesPositionDesc(salesValue);
					uSession.getUserFilterForm().clearSeventhList();
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
					LabelValueBean labelValueBean1;
					uSession.getUserFilterForm().setLevel7("All");
					FormUtil.loadObject(getRequest(),
							uSession.getUserFilterForm());
					if (nextLevel6.size() > 0) {
						labelValueBean1 = new LabelValueBean("All", "All");
						uSession.getUserFilterForm().setSeventhList(
								labelValueBean1);
						System.out.println("SIZE OF NEXT LEVEL"
								+ nextLevel6.size());
						for (int i = 0; i < nextLevel6.size(); i++) {
							LabelValueBean tempLabelValueBean;
							tempLabelValueBean = (LabelValueBean) nextLevel6
									.get(i);
							labelValueBean1 = new LabelValueBean(
									(String) tempLabelValueBean.getLabel(),
									(String) tempLabelValueBean.getValue());
							uSession.getUserFilterForm().setSeventhList(
									labelValueBean1);
						}
					}
				} else if (salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel6(salesValue);
					uSession.getUserFilterForm().clearSeventhList();
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
				}

			} else if (salesLevel.toString().equals("8")) {
				System.out.println("---- Inside sixth loop-----------"
						+ salesId);
				ArrayList nextLevel7 = new ArrayList();
				if (!salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel7(salesValue);
					nextLevel7 = ut.getDropdownSalesPositionDesc(salesValue);
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
					LabelValueBean labelValueBean1;
					uSession.getUserFilterForm().setLevel8("All");
					FormUtil.loadObject(getRequest(),
							uSession.getUserFilterForm());
					if (nextLevel7.size() > 0) {
						labelValueBean1 = new LabelValueBean("All", "All");
						uSession.getUserFilterForm().setEighthList(
								labelValueBean1);
						for (int i = 0; i < nextLevel7.size(); i++) {
							LabelValueBean tempLabelValueBean;
							tempLabelValueBean = (LabelValueBean) nextLevel7
									.get(i);
							labelValueBean1 = new LabelValueBean(
									(String) tempLabelValueBean.getLabel(),
									(String) tempLabelValueBean.getValue());
							uSession.getUserFilterForm().setEighthList(
									labelValueBean1);
						}
					}
				} else if (salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel7(salesValue);
					uSession.getUserFilterForm().clearEighthList();
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
				}
			} else if (salesLevel.toString().equals("9")) {

				System.out.println("---- Inside sixth loop-----------"
						+ salesId);
				ArrayList nextLevel8 = new ArrayList();
				if (!salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel8(salesValue);
					nextLevel8 = ut.getDropdownSalesPositionDesc(salesValue);
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
					LabelValueBean labelValueBean1;
					uSession.getUserFilterForm().setLevel9("All");
					FormUtil.loadObject(getRequest(),
							uSession.getUserFilterForm());
					if (nextLevel8.size() > 0) {
						labelValueBean1 = new LabelValueBean("All", "All");
						uSession.getUserFilterForm().setNinthList(
								labelValueBean1);
						for (int i = 0; i < nextLevel8.size(); i++) {
							LabelValueBean tempLabelValueBean;
							tempLabelValueBean = (LabelValueBean) nextLevel8
									.get(i);
							labelValueBean1 = new LabelValueBean(
									(String) tempLabelValueBean.getLabel(),
									(String) tempLabelValueBean.getValue());
							uSession.getUserFilterForm().setNinthList(
									labelValueBean1);
						}
					}
				} else if (salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel8(salesValue);
					uSession.getUserFilterForm().clearNinthList();
					uSession.getUserFilterForm().clearTenthList();
				}
			} else if (salesLevel.toString().equals("10")) {
				System.out.println("---- Inside sixth loop-----------"
						+ salesId);
				ArrayList nextLevel9 = new ArrayList();
				if (!salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel9(salesValue);
					nextLevel9 = ut.getDropdownSalesPositionDesc(salesValue);
					uSession.getUserFilterForm().clearTenthList();
					LabelValueBean labelValueBean1;
					uSession.getUserFilterForm().setLevel10("All");
					FormUtil.loadObject(getRequest(),
							uSession.getUserFilterForm());
					if (nextLevel9.size() > 0) {
						labelValueBean1 = new LabelValueBean("All", "All");
						uSession.getUserFilterForm().setTenthList(
								labelValueBean1);
						for (int i = 0; i < nextLevel9.size(); i++) {
							LabelValueBean tempLabelValueBean;
							tempLabelValueBean = (LabelValueBean) nextLevel9
									.get(i);
							labelValueBean1 = new LabelValueBean(
									(String) tempLabelValueBean.getLabel(),
									(String) tempLabelValueBean.getValue());
							uSession.getUserFilterForm().setTenthList(
									labelValueBean1);
						}
					}
				} else if (salesId.equalsIgnoreCase("All")) {
					filterForm.setLevel9(salesValue);
					uSession.getUserFilterForm().clearTenthList();
				}

			} else if (salesLevel.toString().equals("11")) {
				uSession.getUserFilterForm().setLevel10(salesValue);
			}
		}

		UserFilter uFilter = uSession.getUserFilter();

		uFilter.setRefresh(false);
		// LoggerHelper.logSystemDebug("outside levels");
		System.out.println("TYPE IN PHASECONTROLLER"
				+ uFilter.getFilterForm().getSelectionType());
		/* Code for drop downs--To be uncommented later */

		System.out.println("Gettttttttting session"
				+ uSession.getCurrentSlice());
		TerritorySelectWc territorySelect = new TerritorySelectWc(
				uSession.getUserFilterForm(), uSession.getUser()
						.getUserTerritory(), "/TrainingReports/p2l/charts");
		territorySelect.setShowTeam(true);
		territorySelect.setShowMultipleGeos(true);
		territorySelect.setLayout(4);

		TerritoryHandler tHandler = new TerritoryHandler();
		Territory terr = tHandler.getTerritory(uSession.getUserFilter());
		/* Added for Detail report */

		String check = (String) getRequest().getSession().getAttribute(
				"listlevel");

		LoggerHelper.logSystemDebug("check level" + check);

		if (check == "fromlistreport") {

			String activityPk = uSession.getCurrentActivity();
			uFilter.getQuseryStrings().setActivitypk(activityPk);
			uFilter.getQuseryStrings().setSection(uSession.getCurrentSlice());

			Collection result = new ArrayList();

			String currentSlice = null;

			// Start: Added for Major Enhancement 3.6 - F1
			String section = uFilter.getQuseryStrings().getSection();
			if (section != null) {
				currentSlice = uFilter.getQuseryStrings().getSection();
				LoggerHelper.logSystemDebug("today section" + currentSlice);
				// currentSlice = null;
				uFilter.getQuseryStrings().setSection(
						uFilter.getFilterForm().getChkStatus());
				System.out.println("check section1"
						+ uFilter.getQuseryStrings().getSection());
				if ("All".equals(currentSlice)) {
					currentSlice = null;
					uFilter.getQuseryStrings().setSection(
							uFilter.getFilterForm().getChkStatus());

				}

			} else {
				LoggerHelper.logSystemDebug("r1");
				if (uFilter.getFilterForm().getChkStatus().equals("")
						|| uFilter.getFilterForm().getChkStatus()
								.equals("null")) {
					LoggerHelper.logSystemDebug("r2");
					currentSlice = uSession.getCurrentSlice();
				} else {
					LoggerHelper.logSystemDebug("r3");
					currentSlice = uFilter.getFilterForm().getChkStatus();
					if ("All".equals(currentSlice))
						currentSlice = "";
				}
				LoggerHelper.logSystemDebug("r4");
				uFilter.getQuseryStrings().setSection(
						uFilter.getFilterForm().getChkStatus());
				System.out.println("check section2"
						+ uFilter.getQuseryStrings().getSection());
			}
			// End: for Major Enhancement 3.6 - F1
			uSession.setCurrentSlice(currentSlice);
			uSession.setCurrentActivity(activityPk);

			P2lHandler p2l = new P2lHandler();
			// Added for Major Enhancement 3.6 - F1
			P2lTrackPhase phase = null;
			P2lTrackPhase trackPhase = null;
			String label = "";
			PieChart chart;
			P2lTrack track = uSession.getTrack();
			/* Added for Phase 1 by Meenakshi */
			P2lTrackPhase subTrackPhase = null;
			// Added for Major Enhancement 3.6 - F1
			String parentActivityPk = (String) getRequest().getSession()
					.getAttribute("parentActivityPk");
			String fromListReport = "fromlistreport";
			// String activityPk = uFilter.getQuseryStrings().getActivitypk();
			// End: for Major Enhancement 3.6 - F1
			if ("Overall".equals(activityPk)) {
				label = "Overall";
				chart = getOverallChart(track, uFilter, "", result, true,
						uSession.getUser().getId(), track.getAllNodesDelimit()); // 441818
				trackPhase = (P2lTrackPhase) track.getPhases().get(0);
				LoggerHelper.logSystemDebug("inside overall");

			} else {
				trackPhase = p2l.getTrackPhase(activityPk, track.getTrackId());
				// If condition added for Major Enhancement 3.6 - F1
				if (trackPhase != null) {
					label = trackPhase.getPhaseNumber();
					trackPhase.setTrack(track);
					// chart =
					// getPhaseChart(trackPhase,uFilter,trackPhase.getAlttActivityId(),result,
					// true, null); //441818
					trackPhase.setTrack(track);
					// added for RBU
					// UserTerritory ut = uSession.getUser().getUserTerritory();
					// Added for Major Enhancement 3.6 - F1
					chart = getAllStatusPhaseChart(ut, trackPhase, uFilter,
							trackPhase.getAlttActivityId(), result, true, null); // 441818
					LoggerHelper.logSystemDebug("inside trackphase"
							+ chart.getCount());
					// ended for RBU
					rootActivityID = trackPhase.getRootActivityId();
					parentActivityPk = activityPk;
				} // End: for Major Enhancement 3.6 - F1
				else {
					// Sub Activity
					SubActivityBean bean = p2l.getActivityDetails(activityPk);
					label = bean.getActivityName();
					rootActivityID = bean.getRootActivityID();

					// UserTerritory ut = uSession.getUser().getUserTerritory();
					chart = getActivityChart(ut, bean, uFilter, result);
					subTrackPhase = p2l.getTrackPhase(parentActivityPk,
							track.getTrackId());

					if (subTrackPhase != null) {
						rootActivityID = subTrackPhase.getRootActivityId();
					}
				}
			}
			// End: for Major Enhancement 3.6 - F1

			/* Setting the session required for the getNextLevel() method */
			getRequest().getSession().setAttribute("P2lListReportChart", chart);

			int layout = ChartLegendWc.LAYOUT_PHASE;
			if (trackPhase != null && trackPhase.getApprovalStatus()) {
				layout = ChartLegendWc.LAYOUT_PHASE_PENDING;
			}
			ChartDetailWc chartDetailWc = null;

			// Get the links that would be displayed
			System.out.println("Activity ID in P2L Controller------------"
					+ activityPk);
			List resultList = new ArrayList();
			resultList = p2l.getLinksForActivities(activityPk);
			// Added for Major Enhancement 3.6 - F1

			StatusSubSetBean b = getStatusSubsetDetails(uFilter);

			if (chart.getCount() > 0) {
				// Addedfor Major Enhancement 3.6 - F1

				if (b.isStatusSubsetSelected()) {
					// ChartP2lSubsetLegendWc legendWc = new
					// ChartP2lSubsetLegendWc(
					// uFilter.getQuseryStrings().getActivitypk() + "",
					// trackPhase );
					// legendWc.setChkStatus(uFilter.getFilterForm().getChkStatus());		
							/* Modified for Phase 1 by Meenakshi */
					if (trackPhase != null) {
						chartDetailWc = new ChartDetailWc(chart, label,
								new ChartP2lPhaseLegendWc(uFilter
										.getQuseryStrings().getActivitypk()
										+ "", trackPhase, chart),
								"showReportLink", resultList);
					} else {
						chartDetailWc = new ChartDetailWc(chart, label,
								new ChartP2lPhaseLegendWc(uFilter
										.getQuseryStrings().getActivitypk()
										+ "", subTrackPhase, chart),
								"showReportLink", resultList);
					}
					/* End of modification */
					LoggerHelper.logSystemDebug("StatusSubsetSelected");
				}

				else {
					// End: for Major Enhancement 3.6 - F1

					// chartDetailWc = new ChartDetailWc( chart, label ,new
					// ChartP2lLegendWc(
					// uFilter.getQuseryStrings().getActivitypk() + "",
					// trackPhase ) );
					/* Modified for Product and Summary report links */
					/* Modified for Phase 1 by Meenakshi */
					if (trackPhase != null) {
						chartDetailWc = new ChartDetailWc(chart, label,
								new ChartP2lPhaseLegendWc(uFilter
										.getQuseryStrings().getActivitypk()
										+ "", trackPhase, chart),
								"showReportLink", resultList);
					} else {
						chartDetailWc = new ChartDetailWc(chart, label,
								new ChartP2lPhaseLegendWc(uFilter
										.getQuseryStrings().getActivitypk()
										+ "", subTrackPhase, chart),
								"showReportLink", resultList);
					}

					/* End of modification */
					// End: for Major Enhancement 3.6 - F1
					// chartDetailWc.setP2lPhaseChartURL("listReportAllStatus?activitypk="
					// + phase.getRootActivityId());
					// LoggerHelper.logSystemDebug("SatusSubset Not Sel");
					// ends here
				}

			} else {
				chartDetailWc = new ChartDetailWc();
				chartDetailWc.setChart(chart);
				chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			}

			Collection filteredList = new ArrayList();
			// StatusSubSetBean b = getStatusSubsetDetails(uFilter);

			for (Iterator it = result.iterator(); it.hasNext();) {
				LoggerHelper.logSystemDebug("Result iterator" + result.size());
				P2lEmployeeStatus tmp = (P2lEmployeeStatus) it.next();
				if (uFilter.getFilterForm().getChkStatus()
						.equals(tmp.getStatus())
						|| uFilter.getFilterForm().getChkStatus().equals("All")) {
					LoggerHelper.logSystemDebug("inside getSection"
							+ uFilter.getQuseryStrings().getSection());
					filteredList.add(tmp);
				}
			}

			/* Setting the session required for the getNextLevel() method */
			getRequest().getSession().setAttribute("P2lFilteredList",
					filteredList);
			uFilter.setLayoutNew("4");
			PageBuilder builder = new PageBuilder();
			// Added for Major Enhancement 3.6 - F1
			DrillDownAreaWc ddArea = null;
			boolean isTreeViewVisible = checkActivityDrilldownVisibility(uSession
					.getUser());
			if (isTreeViewVisible) {
				List result1 = p2l.getActivityTree(rootActivityID);
				ddArea = new DrillDownAreaWc(result1);
			}
			// Start: Modified for TRT 3.6 enhancement - F 4.1 (additional
			// search attributes)
			EmplSearchForm eForm = new EmplSearchForm();

			if (eForm.getBuList().size() <= 0) {
				BUnitBean[] allBu = null;
				allBu = trDb.getAllBusinessUnits();
				LabelValueBean labelValueBean;
				eForm.setBu("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setBuList(labelValueBean);
				for (int i = 0; i < allBu.length; i++) {
					labelValueBean = new LabelValueBean(
							allBu[i].getBunitDesc(), allBu[i].getBunitDesc());
					eForm.setBuList(labelValueBean);
				}
			}
			if (eForm.getRoleList().size() <= 0) {
				RoleBean[] allRoles = null;
				allRoles = trDb.getAllRoleDesc();
				LabelValueBean labelValueBean;
				eForm.setRole("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setRoleList(labelValueBean);
				for (int i = 0; i < allRoles.length; i++) {
					labelValueBean = new LabelValueBean(
							allRoles[i].getRoleDesc(), allRoles[i].getRoleCd());
					eForm.setRoleList(labelValueBean);
				}
			}

			SearchFormWc searchFormWc = new SearchFormWc(eForm);
			searchFormWc.setPostUrl("searchemployee");
			searchFormWc.setTarget("myW");
			searchFormWc.setOnSubmit("DoThis12()");
			EmployeeSearchWc esearch = new EmployeeSearchWc(eForm,
					new ArrayList());
			esearch.setSearchForm(searchFormWc);

			// DrillDownAreaWc ddArea = new DrillDownAreaWc(result1,
			// isTreeViewVisible);
			// List result1 =
			// p2l.getActivityTree(trackPhase.getRootActivityId());
			// DrillDownAreaWc ddArea = new DrillDownAreaWc(result1);
			// ends here
			getRequest().getSession().setAttribute("listlevel", fromListReport);
			// Start: Modified for TRT 3.6 enhancement - F 4.5

			// ////Added for TRT Phase 2 employee grid/ and hq user//////////
			// List selectedOptEmpFields = p2l.getSelOptionalEmplFields();
			String selectedFields = getRequest().getParameter("newSet");
			List selectedOptEmpFields = new ArrayList();
			if (selectedFields != null) {
				String selectedFieldArray[] = selectedFields.split(",");

				for (int i = 0; i < selectedFieldArray.length; i++) {
					selectedOptEmpFields.add(selectedFieldArray[i]);
				}
			}
			HttpSession session = getSession();
			session.setAttribute("selectedOptEmpFields", selectedOptEmpFields);

			String selectedHQFields = getRequest().getParameter("newHQSet");
			List selectedOptHQEmpFields = new ArrayList();
			if (selectedHQFields != null) {
				String selectedHQFieldArray[] = selectedHQFields.split(",");

				for (int i = 0; i < selectedHQFieldArray.length; i++) {
					selectedOptHQEmpFields.add(selectedHQFieldArray[i]);
				}
			}

			session.setAttribute("selectedOptHQEmpFields",
					selectedOptHQEmpFields);

			// /////end employee grid and hq user//////////
			MainReportListReportAreaWc mainArea = new MainReportListReportAreaWc(
					filteredList, uSession.getUser(), parentActivityPk, uFilter
							.getQuseryStrings().getSection(),
					selectedOptEmpFields);
			// ////added for HQ user////////
			MainReportListReportAreaHQWc mainAreaHQ = new MainReportListReportAreaHQWc(
					filteredList, uSession.getUser(), parentActivityPk, uFilter
							.getQuseryStrings().getSection(),
					selectedOptHQEmpFields);
			// ////end HQ user///////
			// End: Modified for TRT 3.6 enhancement - F 4.5
			MassEmailWc emWc = new MassEmailWc(uSession.getUser());
			emWc.setEmailSubject(track.getTrackLabel() + " Follow-up");
			mainArea.setMassEmailWc(emWc);
			mainAreaHQ.setMassEmailWc(emWc);

			// Added for TRT Enchancement CSO impact
			mainArea.setSession(uSession);
			// ///added for hq user////
			mainAreaHQ.setSession(uSession);
			// ///end hq user//////
			System.out.println("User Session in getNextLevel==" + uSession);
			// End of enhancement

			System.out.println("UserFilter - Layout" + uFilter.getLayoutNew());
			/*
			 * MainReportListWc main = new MainReportListWc(new
			 * MainReportListChartAreaWc(chartDetailWc), new
			 * MainReportListFilterSelectAreaWc
			 * (uSession.getUser(),uFilter,trackPhase),
			 * mainArea,uSession.getUser());
			 */
			// ///edited for HQ user////////
			MainReportListWc main = new MainReportListWc(
					new MainReportListChartAreaWc(chartDetailWc),
					new MainReportListFilterSelectAreaWc(uSession.getUser(),
							uFilter, trackPhase), mainArea, mainAreaHQ,
					uSession.getUser(), esearch);
			// ///end HQ user///////
			main.setPageName(label);
			main.setActivityId(activityPk);
			main.setSlice(uFilter.getFilterForm().getChkStatus());
			main.setTrack(track);
			// Added for Major Enhancement 3.6 - F1
			main.setDrillDownArea(ddArea);
			getRequest().getSession().setAttribute("parentActivityPk",
					parentActivityPk);

			// ends here
			MainReportListReportAreaWc reportList = null;
			if ("true".equals(uFilter.getQuseryStrings().getDownloadExcel())) {
				Collection sessionfilteredList = new ArrayList();
				sessionfilteredList = (ArrayList) getRequest().getSession()
						.getAttribute("downloadexcelfilter");
				if (sessionfilteredList != null) {
					reportList = new MainReportListReportAreaWc(
							sessionfilteredList, uSession.getUser(),
							parentActivityPk + "", uFilter.getQuseryStrings()
									.getSection(), selectedOptEmpFields);
				} else {
					reportList = new MainReportListReportAreaWc(filteredList,
							uSession.getUser(), parentActivityPk + "", uFilter
									.getQuseryStrings().getSection(),
							selectedOptEmpFields);
				}
				reportList.setLayout(MainReportListReportAreaWc.LAYOUT_XLS);
				reportList.setSession(uSession);
				BlankTemplateWpc page = new BlankTemplateWpc(reportList);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getResponse().addHeader("content-disposition",
						"attachment;filename=trainingreports.xls");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");

				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes start here
				 * --> return new Forward("successXls");
				 */
				return new String("successXls");
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes ends here
				 * -->
				 */

			}
			getRequest().getSession().removeAttribute("downloadexcelfilter");

			MainTemplateWpc page = builder.buildPageP2l(main, "Report List",
					uSession.getUser(), "reportselect");
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		}

		else {

			String activityPk = uSession.getCurrentActivity();
			System.out.println("Inside else of getNextLevel---------"
					+ activityPk);
			System.out.println("Inside else of getNextLevel---------"
					+ uSession.getCurrentSlice());
			LoggerHelper.logSystemDebug("inside else of NextLevel");
			uFilter.getQuseryStrings().setActivitypk(activityPk);
			uFilter.getQuseryStrings().setSection(uSession.getCurrentSlice());

			uSession.setCurrentSlice(uFilter.getQuseryStrings().getSection());
			// String activityPk = uFilter.getQuseryStrings().getActivitypk();

			Collection result = new ArrayList();
			uFilter.getFilterForm().setChkStatus("All");
			P2lHandler p2l = new P2lHandler();
			P2lTrackPhase trackPhase = null;
			String label = "";
			PieChart chart;
			/* Modified for Phase 1 by Meenakshi */
			P2lTrackPhase subTrackPhase = null;
			P2lTrack track = uSession.getTrack();
			String parentActivityPk = (String) getRequest().getSession()
					.getAttribute("parentActivityPk");
			if ("Overall".equals(activityPk)) {
				label = "Overall";
				chart = getOverallChart(track, uFilter, "", result, true,
						uSession.getUser().getId(), track.getAllNodesDelimit());
				trackPhase = (P2lTrackPhase) track.getPhases().get(0);
			} else {
				trackPhase = p2l.getTrackPhase(activityPk, track.getTrackId());

				if (trackPhase != null) {
					label = trackPhase.getPhaseNumber();
					LoggerHelper.logSystemDebug("inside new9 ");
					trackPhase.setTrack(track);
					trackPhase.setTrack(track);
					// UserTerritory ut = uSession.getUser().getUserTerritory();
					chart = getAllStatusPhaseChart(ut, trackPhase, uFilter,
							trackPhase.getAlttActivityId(), result, true, null);
					rootActivityID = trackPhase.getRootActivityId();
					LoggerHelper.logSystemDebug("root666" + rootActivityID);
					parentActivityPk = activityPk;
				} else {
					// Sub Activity
					SubActivityBean bean = p2l.getActivityDetails(activityPk);
					label = bean.getActivityName();

					rootActivityID = bean.getRootActivityID();

					// UserTerritory ut = uSession.getUser().getUserTerritory();
					chart = getActivityChart(ut, bean, uFilter, result);

					/* Modified for Phase 1 by Meenakshi */
					subTrackPhase = p2l.getTrackPhase(parentActivityPk,
							track.getTrackId());
					if (subTrackPhase != null) {
						rootActivityID = subTrackPhase.getRootActivityId();
					}
				}

			}

			/* Getting the chart object from the session */
			// chart= (PieChart)getSession().getAttribute("P2lListReportChart");
			int layout = ChartLegendWc.LAYOUT_PHASE;
			if (trackPhase != null && trackPhase.getApprovalStatus()) {
				layout = ChartLegendWc.LAYOUT_PHASE_PENDING;
			}
			ChartDetailWc chartDetailWc = null;
			List resultList = new ArrayList();
			resultList = p2l.getLinksForActivities(activityPk);

			if (chart.getCount() > 0) {
				/* Modified for Phase 1 by Meenakshi */
				// chartDetailWc = new ChartDetailWc( chart, label
				// ,null,"showReportLink",resultList);
				if (trackPhase != null) {
					chartDetailWc = new ChartDetailWc(chart, label,
							new ChartP2lPhaseLegendWc(uFilter
									.getQuseryStrings().getActivitypk() + "",
									trackPhase, chart), "showReportLink",
							resultList);
				} else {
					chartDetailWc = new ChartDetailWc(chart, label,
							new ChartP2lPhaseLegendWc(uFilter
									.getQuseryStrings().getActivitypk() + "",
									subTrackPhase, chart), "showReportLink",
							resultList);
				}
				/* End of modification */
			} else {

				chartDetailWc = new ChartDetailWc();
				chartDetailWc.setChart(chart);
				chartDetailWc.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
			}

			uFilter.setLayoutNew("4");
			PageBuilder builder = new PageBuilder();

			// List actresult = p2l.getActivityTree(rootActivityID);

			/*
			 * Iterator itr = actresult.iterator();
			 * LoggerHelper.logSystemDebug("activity drilldown debug "); int
			 * lvl, id; String code, name; while (itr.hasNext()){ Map currMap =
			 * (Map)itr.next(); lvl =
			 * ((BigDecimal)currMap.get("LEVEL")).toBigInteger().intValue();
			 * code = (String)currMap.get("ACTIVITY_CODE"); name =
			 * (String)currMap.get("ACTIVITYNAME"); id =
			 * ((BigDecimal)currMap.get
			 * ("ACTIVITY_PK")).toBigInteger().intValue();
			 * 
			 * LoggerHelper.logSystemDebug(lvl + "," + code + "," + name + "," +
			 * id); }
			 */
			DrillDownAreaWc ddArea = null;
			boolean isTreeViewVisible = checkActivityDrilldownVisibility(uSession
					.getUser());
			if (isTreeViewVisible) {
				List result1 = p2l.getActivityTree(rootActivityID);
				ddArea = new DrillDownAreaWc(result1);
			}
			// Start: Modified for TRT 3.6 enhancement - F 4.1 (additional
			// search attributes)
			EmplSearchForm eForm = new EmplSearchForm();

			if (eForm.getBuList().size() <= 0) {
				BUnitBean[] allBu = null;
				allBu = trDb.getAllBusinessUnits();
				LabelValueBean labelValueBean;
				eForm.setBu("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setBuList(labelValueBean);
				for (int i = 0; i < allBu.length; i++) {
					labelValueBean = new LabelValueBean(
							allBu[i].getBunitDesc(), allBu[i].getBunitDesc());
					eForm.setBuList(labelValueBean);
				}
			}
			if (eForm.getRoleList().size() <= 0) {
				RoleBean[] allRoles = null;
				allRoles = trDb.getAllRoleDesc();
				LabelValueBean labelValueBean;
				eForm.setRole("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setRoleList(labelValueBean);
				for (int i = 0; i < allRoles.length; i++) {
					labelValueBean = new LabelValueBean(
							allRoles[i].getRoleDesc(), allRoles[i].getRoleCd());
					eForm.setRoleList(labelValueBean);
				}
			}

			SearchFormWc searchFormWc = new SearchFormWc(eForm);
			searchFormWc.setPostUrl("searchemployee");
			searchFormWc.setTarget("myW");
			searchFormWc.setOnSubmit("DoThis12()");
			EmployeeSearchWc esearch = new EmployeeSearchWc(eForm,
					new ArrayList());
			esearch.setSearchForm(searchFormWc);
			String fromListReportAllStatus = "fromlistreportAllStatus";

			LoggerHelper.logSystemDebug("inside listreportallstatus");
			MainReportAllStatusWc main = new MainReportAllStatusWc(
					new MainReportListChartAreaWc(chartDetailWc),
					new MainReportListFilterSelectAreaWc(uSession.getUser(),
							uFilter, trackPhase), ddArea, esearch,
					uSession.getUser());
			getRequest().getSession().setAttribute("datecheck",
					fromListReportAllStatus);
			main.setPageName(label);
			main.setActivityId(activityPk);
			main.setTrack(track);

			MainTemplateWpc page = builder.buildPageP2l(main, "Report List",
					uSession.getUser(), "reportselect");
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		}
			}catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	public PieChart getOverallChart(P2lTrack track, UserFilter uFilter,
			String altNode, Collection employees, boolean detailflag,
			String emplid, String otherNodes, UserTerritory ut) {
		StringBuffer sb = new StringBuffer();
		Timer timer = new Timer();
		P2lHandler p2l = new P2lHandler();
		boolean rmFlag = false;
		if ("POA".equals(track.getTrackId())) {
			rmFlag = true;
		}
		Collection master = p2l.getOveralStatus(track, uFilter, true, ut);
		int registered = 0;
		int assigned = 0;
		int exempt = 0;
		int complete = 0;
		int onleave = 0;
		int pending = 0;
		int notComplete = 0;
		// Added for Major Enhancement 3.6 - F1

		int cancelled = 0;
		// ends here
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

			if ("Registered".equals(st.getStatus())
					|| "In Progress".equals(st.getStatus())
					|| "Cancelled".equals(st.getStatus())
					|| "No Show".equals(st.getStatus())
					|| "Pending".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Not Complete");
					notComplete++;
				}
			} else if ("Waived".equals(st.getStatus())
					|| "Complete".equals(st.getStatus())) {// ||
															// "RegisteredC".equals(st.getStatus())){

				/*
				 * if("On-Leave".equals(st.getEmployee().getEmployeeStatus())){
				 * st.setStatus("On-Leave"); st.setStatusToDisplay("On-Leave");
				 * onleave++; } else{
				 */
				st.setStatusToDisplay("Complete");
				complete++;
				// }

			} else if ("Assigned".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Assigned");
					assigned++;
				}

			}

			/*
			 * if ("Registered".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { registered++; } }
			 * else if ("Assigned".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { assigned++; } }
			 * //Added for Major Enhancement 3.6 - F1
			 * 
			 * else if ("Cancelled".equals(st.getStatus())) { // Added for P2L
			 * issues data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ cancelled++; } } //
			 * ends here //else if ("Exempt".equals(st.getStatus())) { else if
			 * ("Waived".equals(st.getStatus())) { //* Added for P2L issues data
			 * fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ exempt++; } } else
			 * if ("Pending".equals(st.getStatus())) { pending++; } else if
			 * ("RegisteredC".equals(st.getStatus())) {
			 * st.setStatus("Complete"); complete++; } // Added for 'In
			 * Progress' status by Meenakshi else if
			 * ("In Progress".equals(st.getStatus())){ registered++; } //End of
			 * addition else { complete++; }
			 */
		}

		HashMap result = new HashMap();
		P2lTrackPhase phase = (P2lTrackPhase) track.getPhases().get(0);

		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();

		data.add(new ChartData("Complete", complete));
		data.add(new ChartData("Assigned", assigned));
		data.add(new ChartData("Not Complete", notComplete));
		data.add(new ChartData("On-Leave", onleave));
		tMap.put("Complete", AppConst.COLOR_BLUE);
		tMap.put("On-Leave", new Color(253, 243, 156));
		tMap.put("Assigned", new Color(153, 204, 0));
		tMap.put("Not Complete", new Color(153, 51, 0));
		/*
		 * data.add(new ChartData( "Complete", complete ) ); //Added for Major
		 * Enhancement 3.6 - F1
		 * 
		 * data.add(new ChartData( "Cancelled", cancelled ) ); //ends here
		 * 
		 * if (phase.getExempt()) { data.add(new ChartData( "Waived", exempt )
		 * ); tMap.put( "Waived", new Color(255,153,0) ); } if
		 * (phase.getAssigned()) { data.add(new ChartData( "Assigned", assigned)
		 * ); tMap.put( "Assigned", new Color(153,204,0) ); } data.add(new
		 * ChartData( "Registered", registered ) ); data.add(new ChartData(
		 * "On-Leave", onleave ) );
		 */

		PieChartBuilder builder = new PieChartBuilder();

		/*
		 * tMap.put( "Complete", AppConst.COLOR_BLUE ); //Added for Major
		 * Enhancement 3.6 - F1
		 * 
		 * tMap.put( "Cancelled", AppConst.COLOR_CYAN ); //ends here
		 * 
		 * tMap.put( "Registered", new Color(153,51,0) );
		 * 
		 * tMap.put( "On-Leave", new Color(253,243,156) );
		 */
		Map colorMap = Collections.unmodifiableMap(tMap);

		String ak = "";
		String label = "";
		if (!Util.isEmpty(otherNodes)) {
			ak = "Overall";
			label = "Overall";
		}
		// Added for Major Enhancement 3.6 - F1
		// chart =
		// builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk="
		// + ak ,getRequest().getSession(), colorMap);
		chart = builder.generate(data, uFilter.getQuseryStrings().getSection(),
				label, "listReportAllStatus?activitypk=" + ak, getRequest()
						.getSession(), colorMap);
		chart.setCount(complete + onleave + assigned + notComplete);
		chart.setAssigned(assigned);
		chart.setCompleted(complete);
		chart.setOnleave(onleave);
		chart.setNotComplete(notComplete);
		// chart.setCount( complete + exempt + assigned + registered + onleave +
		// cancelled);
		// ends here
		employees.addAll(master);
		return chart;
	}

	public PieChart getPhaseChart(P2lTrackPhase phase, UserFilter uFilter,
			String altNode, Collection employees, boolean detailflag,
			String emplid) {
		StringBuffer sb = new StringBuffer();
		Timer timer = new Timer();
		P2lHandler p2l = new P2lHandler();
		boolean rmFlag = false;
		if ("POA".equals(phase.getTrackId())) {
			rmFlag = true;
		}
		Collection master = p2l.getPhaseStatus(phase, uFilter, detailflag, "");
		int registered = 0;
		int assigned = 0;
		int exempt = 0;
		int complete = 0;
		int onleave = 0;
		int pending = 0;
		int notComplete = 0;
		// Added for Major Enhancement 3.6 - F1
		int cancelled = 0;
		// ends here
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

			if ("Registered".equals(st.getStatus())
					|| "In Progress".equals(st.getStatus())
					|| "Cancelled".equals(st.getStatus())
					|| "No Show".equals(st.getStatus())
					|| "Pending".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Not Complete");
					notComplete++;
				}
			} else if ("Waived".equals(st.getStatus())
					|| "Complete".equals(st.getStatus())) {// ||
															// "RegisteredC".equals(st.getStatus())){

				/*
				 * if("On-Leave".equals(st.getEmployee().getEmployeeStatus())){
				 * st.setStatus("On-Leave"); st.setStatusToDisplay("On-Leave");
				 * onleave++; } else{
				 */
				st.setStatusToDisplay("Complete");
				complete++;
				// }

			} else if ("Assigned".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Assigned");
					assigned++;
				}

			}

			/*
			 * if ("Registered".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { registered++; } }
			 * else if ("Assigned".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { assigned++; } }
			 * else if ("Waived".equals(st.getStatus())) { // Added for P2L
			 * issues data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ exempt++; } }
			 * //added for major enhancement else if
			 * ("Cancelled".equals(st.getStatus())) { // Added for P2L issues
			 * data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ cancelled++; } }
			 * //ends here else if ("Pending".equals(st.getStatus())) {
			 * pending++; } else if ("RegisteredC".equals(st.getStatus())) {
			 * st.setStatus("Complete"); complete++; } // Added for 'In
			 * Progress' status by Meenakshi else if
			 * ("In Progress".equals(st.getStatus())){ registered++; } else {
			 * complete++; }
			 */
		}

		HashMap result = new HashMap();

		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();

		/*
		 * data.add(new ChartData( "Complete", complete ) ); //added for major
		 * enhancement data.add(new ChartData( "Cancelled", cancelled ) );
		 * //ends here if ( phase.getExempt() ) { data.add(new ChartData(
		 * "Waived", exempt ) ); tMap.put( "Waived", new Color(255,153,0) ); }
		 * if ( phase.getAssigned()) { data.add(new ChartData( "Assigned",
		 * assigned) ); tMap.put( "Assigned", new Color(153,204,0) ); } if (
		 * phase.getApprovalStatus() ) { tMap.put( "Pending",
		 * AppConst.COLOR_CYAN ); data.add(new ChartData( "Pending", pending )
		 * ); } else { data.add(new ChartData( "Registered", registered ) ); }
		 * data.add(new ChartData( "On-Leave", onleave ) );
		 */

		PieChartBuilder builder = new PieChartBuilder();

		/*
		 * tMap.put( "Complete", AppConst.COLOR_BLUE );
		 * 
		 * //added for major enhancement tMap.put( "Cancelled",
		 * AppConst.COLOR_CYAN ); //ends here tMap.put( "Registered", new
		 * Color(153,51,0) ); tMap.put( "On-Leave", new Color(253,243,156) );
		 */
		data.add(new ChartData("Complete", complete));
		data.add(new ChartData("Assigned", assigned));
		data.add(new ChartData("Not Complete", notComplete));
		data.add(new ChartData("On-Leave", onleave));
		tMap.put("Complete", AppConst.COLOR_BLUE);
		tMap.put("On-Leave", new Color(253, 243, 156));
		tMap.put("Assigned", new Color(153, 204, 0));
		tMap.put("Not Complete", new Color(153, 51, 0));
		Map colorMap = Collections.unmodifiableMap(tMap);

		String ak = phase.getRootActivityId();
		String label = phase.getTrack().getTrackLabel() + " : "
				+ phase.getPhaseNumber();
		// chart =
		// builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk="
		// + ak ,getRequest().getSession(), colorMap);
		// added for major enhancement
		chart = builder.generate(data, uFilter.getQuseryStrings().getSection(),
				label, "listReportAllStatus?activitypk=" + ak, getRequest()
						.getSession(), colorMap);
		// chart.setCount( complete + exempt + assigned + registered + onleave +
		// cancelled);
		chart.setCount(complete + onleave + assigned + notComplete);
		chart.setAssigned(assigned);
		chart.setCompleted(complete);
		chart.setOnleave(onleave);
		chart.setNotComplete(notComplete);
		// ends here
		employees.addAll(master);
		return chart;
	}

	public PieChart getPhaseChart(P2lTrackPhase phase, String label,
			UserFilter uFilter, String altNode, Collection employees,
			boolean detailflag, String emplid) {
		StringBuffer sb = new StringBuffer();
		Timer timer = new Timer();
		P2lHandler p2l = new P2lHandler();
		Collection master = p2l.getPhaseStatus(phase, uFilter, detailflag);
		int registered = 0;
		int assigned = 0;
		int exempt = 0;
		int complete = 0;
		int onleave = 0;
		int pending = 0;
		int cancelled = 0;
		int notComplete = 0;
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

			if ("Registered".equals(st.getStatus())
					|| "In Progress".equals(st.getStatus())
					|| "Cancelled".equals(st.getStatus())
					|| "No Show".equals(st.getStatus())
					|| "Pending".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Not Complete");
					notComplete++;
				}
			} else if ("Waived".equals(st.getStatus())
					|| "Complete".equals(st.getStatus())) {// ||
															// "RegisteredC".equals(st.getStatus())){

				/*
				 * if("On-Leave".equals(st.getEmployee().getEmployeeStatus())){
				 * st.setStatus("On-Leave"); st.setStatusToDisplay("On-Leave");
				 * onleave++; }
				 */
				// else{
				st.setStatusToDisplay("Complete");
				complete++;
				// }

			} else if ("Assigned".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Assigned");
					assigned++;
				}

			}

			/*
			 * if ("Registered".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { registered++; } }
			 * else if ("Assigned".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { assigned++; } }
			 * else if ("Waived".equals(st.getStatus())) { // Added for P2L
			 * issues data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ exempt++; } } else
			 * if ("Cancelled".equals(st.getStatus())) { // Added for P2L issues
			 * data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ cancelled++; } }
			 * else if ("Pending".equals(st.getStatus())) { pending++; } else if
			 * ("RegisteredC".equals(st.getStatus())) {
			 * st.setStatus("Complete"); //System.out.println("gottcha"); //if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) { //
			 * st.setStatus("On-Leave"); // complete++; //} else { complete++;
			 * //} } // Added for 'In Progress' status by Meenakshi else if
			 * ("In Progress".equals(st.getStatus())){ registered++; } //End of
			 * addition else { complete++; }
			 */
		}

		HashMap result = new HashMap();

		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();
		/*
		 * data.add(new ChartData( "Complete", complete ) ); if (
		 * phase.getExempt() ) { data.add(new ChartData( "Waived", exempt ) );
		 * tMap.put( "Waived", new Color(255,153,0) ); } if (
		 * phase.getAssigned() ) { data.add(new ChartData( "Assigned", assigned)
		 * ); tMap.put( "Assigned", new Color(153,204,0) ); }
		 * 
		 * if ( phase.getApprovalStatus() ) { data.add(new ChartData( "Pending",
		 * pending ) ); tMap.put( "Pending", AppConst.COLOR_CYAN ); } else {
		 * data.add(new ChartData( "Registered", registered ) ); }
		 * 
		 * data.add(new ChartData( "On-Leave", onleave ) );
		 */

		data.add(new ChartData("Complete", complete));
		data.add(new ChartData("Assigned", assigned));
		data.add(new ChartData("Not Complete", notComplete));
		data.add(new ChartData("On-Leave", onleave));
		tMap.put("Complete", AppConst.COLOR_BLUE);
		tMap.put("On-Leave", new Color(253, 243, 156));
		tMap.put("Assigned", new Color(153, 204, 0));
		tMap.put("Not Complete", new Color(153, 51, 0));
		PieChartBuilder builder = new PieChartBuilder();

		/*
		 * tMap.put( "Complete", AppConst.COLOR_BLUE );
		 * 
		 * tMap.put( "Registered", new Color(153,51,0) );
		 * 
		 * tMap.put( "On-Leave", new Color(253,243,156) );
		 */
		Map colorMap = Collections.unmodifiableMap(tMap);

		chart = builder.generate(data, uFilter.getQuseryStrings().getSection(),
				label, "listreport?activitypk=" + phase.getRootActivityId(),
				getRequest().getSession(), colorMap);
		// chart.setCount( complete + exempt + assigned + registered + onleave);
		chart.setCount(complete + onleave + assigned + notComplete);
		chart.setAssigned(assigned);
		chart.setCompleted(complete);
		chart.setOnleave(onleave);
		chart.setNotComplete(notComplete);
		employees.addAll(master);

		return chart;
	}

	public PieChart getPhaseChart(UserTerritory ut, P2lTrackPhase phase,
			String label, UserFilter uFilter, String altNode,
			Collection employees, boolean detailflag, String emplid) {
		StringBuffer sb = new StringBuffer();
		Timer timer = new Timer();
		P2lHandler p2l = new P2lHandler();
		// Collection master = p2l.getPhaseStatus(phase, uFilter,detailflag);
		// added for RBU
		Collection master = p2l.getPhaseStatus(ut, phase, uFilter, detailflag);

		// Added for Major Enhancement 3.6 for CSO impact
		/*
		 * UserSession uSession = buildUserSession();
		 * uSession.getUser().setScoresFlag(""); EmployeeHandler eHandler = new
		 * EmployeeHandler(); Employee emp = eHandler.getEmployeeById(
		 * uFilter.getQuseryStrings().getEmplid() );
		 */
		// Ends here
		Iterator itr = master.iterator();
		ArrayList tempList = new ArrayList();
		while (itr.hasNext()) {
			P2lEmployeeStatus temp1 = (P2lEmployeeStatus) itr.next();
			tempList.add(temp1);
		}
		// ended for RBU
		int registered = 0;
		int assigned = 0;
		int exempt = 0;
		int complete = 0;
		int onleave = 0;
		int pending = 0;
		int cancelled = 0;
		int notComplete = 0;
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

			if ("Registered".equals(st.getStatus())
					|| "In Progress".equals(st.getStatus())
					|| "Cancelled".equals(st.getStatus())
					|| "No Show".equals(st.getStatus())
					|| "Pending".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Not Complete");
					notComplete++;
				}
			} else if ("Waived".equals(st.getStatus())
					|| "Complete".equals(st.getStatus())) {// ||
															// "RegisteredC".equals(st.getStatus())){

				/*
				 * if("On-Leave".equals(st.getEmployee().getEmployeeStatus())){
				 * st.setStatus("On-Leave"); st.setStatusToDisplay("On-Leave");
				 * onleave++; }
				 */
				// else{
				st.setStatusToDisplay("Complete");
				complete++;
				// }

			} else if ("Assigned".equals(st.getStatus())) {

				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					st.setStatusToDisplay("On-Leave");
					onleave++;
				} else {
					st.setStatusToDisplay("Assigned");
					assigned++;
				}

			}

			/*
			 * if ("Registered".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { registered++; } }
			 * else if ("Assigned".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else { assigned++; } }
			 * else if ("Waived".equals(st.getStatus())) { if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ exempt++; } } else
			 * if ("Cancelled".equals(st.getStatus())) { // Added for P2L issues
			 * data fix -- 17Sep if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) {
			 * st.setStatus("On-Leave"); onleave++; } else{ cancelled++; } }
			 * else if ("Pending".equals(st.getStatus())) { pending++; } else if
			 * ("RegisteredC".equals(st.getStatus())) {
			 * st.setStatus("Complete"); //System.out.println("gottcha"); //if (
			 * "On-Leave".equals(st.getEmployee().getEmployeeStatus()) ) { //
			 * st.setStatus("On-Leave"); // complete++; //} else { complete++;
			 * //} } //Added for 'In Progress' status by Meenakshi else if
			 * ("In Progress".equals(st.getStatus())){ registered++; } // End of
			 * addition else { complete++; }
			 */
		}

		HashMap result = new HashMap();

		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();

		data.add(new ChartData("Complete", complete));
		data.add(new ChartData("Assigned", assigned));
		data.add(new ChartData("Not Complete", notComplete));
		data.add(new ChartData("On-Leave", onleave));
		tMap.put("Complete", AppConst.COLOR_BLUE);
		tMap.put("On-Leave", new Color(253, 243, 156));
		tMap.put("Assigned", new Color(153, 204, 0));
		tMap.put("Not Complete", new Color(153, 51, 0));
		/*
		 * data.add(new ChartData( "Complete", complete ) ); if (
		 * phase.getExempt() ) { data.add(new ChartData( "Waived", exempt ) );
		 * tMap.put( "Waived", new Color(255,153,0) ); } if (
		 * phase.getAssigned() ) { data.add(new ChartData( "Assigned", assigned)
		 * ); tMap.put( "Assigned", new Color(153,204,0) ); }
		 * 
		 * if ( phase.getApprovalStatus() ) { data.add(new ChartData( "Pending",
		 * pending ) ); tMap.put( "Pending", AppConst.COLOR_CYAN ); } else {
		 * data.add(new ChartData( "Registered", registered ) ); }
		 * 
		 * data.add(new ChartData( "On-Leave", onleave ) );
		 */

		PieChartBuilder builder = new PieChartBuilder();

		/*
		 * tMap.put( "Complete", AppConst.COLOR_BLUE );
		 * 
		 * tMap.put( "Registered", new Color(153,51,0) );
		 * 
		 * tMap.put( "On-Leave", new Color(253,243,156) );
		 */
		Map colorMap = Collections.unmodifiableMap(tMap);

		// Added for Major Enhancement 3.6 for CSO impact
		/*
		 * if( uSession.getUser().getScoresVisible().equals("Y") ||
		 * uSession.getUser
		 * ().getSalesPositionTypeCd().equals(emp.getSalesPostionTypeCode())) {
		 * uSession.getUser().setScoresFlag("Y"); }
		 * System.out.println("Scores Flag"+uSession.getUser().getScoresFlag());
		 */
		// Ends here
		chart = builder.generate(data, uFilter.getQuseryStrings().getSection(),
				label, "listreport?activitypk=" + phase.getRootActivityId(),
				getRequest().getSession(), colorMap);
		// chart.setCount( complete + exempt + assigned + registered + onleave);
		chart.setCount(complete + onleave + assigned + notComplete);
		chart.setAssigned(assigned);
		chart.setCompleted(complete);
		chart.setOnleave(onleave);
		chart.setNotComplete(notComplete);
		employees.addAll(master);
		return chart;
	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward handleSceCase(){
	 */
	public String handleSceCase() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}
			System.out.println("!!!!! INside handle sce function!!!");
			UserSession uSession = buildUserSession();
			UserFilter uFilter = uSession.getUserFilter();
			EmployeeHandler eHandler = new EmployeeHandler();
			System.out.println("\nUser Filter------"
					+ uFilter.getQuseryStrings().getEmplid());
			Employee emp = eHandler.getEmployeeById(uFilter.getQuseryStrings()
					.getEmplid());
			System.out.println("\n After User Filter------" + emp.getEmplId());

			// String activityPk = uFilter.getQuseryStrings().getActivitypk();
			/*
			 * P2lHandler pHandler = new P2lHandler(); P2lTrack track =
			 * uSession.getTrack(); P2lTrackPhase trackPhase =
			 * pHandler.getTrackPhase(activityPk+"",track.getTrackId());
			 * P2lActivityStatus testresult = pHandler.getPhaseDetail(
			 * emp.getGuid(), trackPhase); Map scoreMap = new HashMap();
			 * testresult.getRegisteredPedExams(scoreMap); List results =
			 * pHandler.getPedScores(scoreMap,emp.getGuid());
			 */

			BlankTemplateWpc page = new BlankTemplateWpc();
			// String aname =
			// pHandler.getActivityNameById(uFilter.getQuseryStrings().getSactivityid()+"");
			SceCodesWc main = new SceCodesWc(uSession, emp.getEmplId());
			page.setMain(main);
			getRequest().setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward handleSpecialCase(){
	 */
	public String handleSpecialCase() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}
			UserSession uSession = buildUserSession();
			UserFilter uFilter = uSession.getUserFilter();
			EmployeeHandler eHandler = new EmployeeHandler();
			System.out.println("\nUser Filter------"
					+ uFilter.getQuseryStrings().getEmplid());
			Employee emp = eHandler.getEmployeeById(uFilter.getQuseryStrings()
					.getEmplid());
			System.out.println("\n After User Filter------" + emp.getEmplId());

			String activityPk = uFilter.getQuseryStrings().getActivitypk();
			P2lHandler pHandler = new P2lHandler();
			P2lTrack track = uSession.getTrack();
			P2lTrackPhase trackPhase = pHandler.getTrackPhase(activityPk + "",
					track.getTrackId());
			P2lActivityStatus testresult = pHandler.getPhaseDetail(
					emp.getGuid(), trackPhase);
			Map scoreMap = new HashMap();
			testresult.getRegisteredPedExams(scoreMap);
			List results = pHandler.getPedScores(scoreMap, emp.getGuid());

			BlankTemplateWpc page = new BlankTemplateWpc();
			String aname = pHandler.getActivityNameById(uFilter
					.getQuseryStrings().getSactivityid() + "");
			SpecialCodesWc main = new SpecialCodesWc(results, uSession,
					emp.getEmplId(), uFilter.getQuseryStrings().getType(),
					uFilter.getQuseryStrings().getSactivityid() + "", aname);
			page.setMain(main);
			getRequest().setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward SCEeval(){
	 */
	public String SCEeval() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}
			AppQueryStrings qString = new AppQueryStrings();
			BlankTemplateWpc page = new BlankTemplateWpc();
			System.out.println("SCeval@@@");
			SCEevaluate main = new SCEevaluate();
			page.setMain(main);
			getRequest().setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

}
