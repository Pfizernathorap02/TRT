package com.pfizer.action;

/*import com.bea.wlw.netui.pageflow.Forward;
 import com.bea.wlw.netui.pageflow.PageFlowController;*/

import com.pfizer.db.Employee;
import com.pfizer.db.LaunchMeeting;
import com.pfizer.db.LaunchMeetingDetails;
import com.pfizer.db.P2lActivityStatus;
import com.pfizer.db.P2lEmployeeStatus;
import com.pfizer.db.SalesOrgBean;
import com.pfizer.db.Territory;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.LaunchMeetingHandler;
import com.pfizer.hander.LaunchMeetingTrainingStatus;
import com.pfizer.hander.RBUPieChartHandler;
import com.pfizer.hander.RBUSHandler;
import com.pfizer.hander.TerritoryHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
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
import com.pfizer.webapp.wc.LaunchMeeting.LaunchMeetingChartsWc;
import com.pfizer.webapp.wc.RBU.RBUChartBean;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartIndexWc;
import com.pfizer.webapp.wc.components.chart.ChartLaunchMeetingLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.chart.ChartP2lLegendWc;
import com.pfizer.webapp.wc.components.chart.GenericChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.GenericRightBarWc;
import com.pfizer.webapp.wc.components.chart.LaunchMeetingChartHeaderWc;
import com.pfizer.webapp.wc.components.report.global.EmployeeInfoWc;
import com.pfizer.webapp.wc.components.report.global.MassEmailWc;
import com.pfizer.webapp.wc.components.report.launchMeeting.LaunchMeetingBreadCrumbWc;
import com.pfizer.webapp.wc.components.report.p2l.OverallStatusWc;
import com.pfizer.webapp.wc.components.report.p2l.P2lBreadCrumbWc;
import com.pfizer.webapp.wc.components.report.phasereports.DetailPageLaunchMeetingWc;
import com.pfizer.webapp.wc.components.report.phasereports.DetailPageWc;
import com.pfizer.webapp.wc.components.report.phasereports.EmplSearchResultWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListChartAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListLaunchMeetingWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListReportAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListSelectAreaWc;
import com.pfizer.webapp.wc.components.report.phasereports.MainReportListWc;
import com.pfizer.webapp.wc.components.report.phasereports.PhaseTrainingDetailLaunchMeetingWc;
import com.pfizer.webapp.wc.components.report.phasereports.PhaseTrainingDetailWc;
import com.pfizer.webapp.wc.components.report.phasereports.TrainingSummaryWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchWc;
import com.pfizer.webapp.wc.components.search.SearchFormWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.pfizer.webapp.wc.global.EmptyPageWc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.pfizer.webapp.wc.util.PageBuilder;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.rbu.FutureAllignmentBuDataBean;
import com.tgix.rbu.FutureAllignmentRBUDataBean;
import com.tgix.wc.WebComponent;
import com.tgix.wc.WebPageComponent;

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
 * @jpf:view-properties view-properties:: <!-- This data is auto-generated.
 *                      Hand-editing this section is not recommended. -->
 *                      <view-properties> <pageflow-object
 *                      id="pageflow:/LaunchMeeting/LaunchMeetingController.jpf"
 *                      /> <pageflow-object id="action:begin.do"> <property
 *                      value="80" name="x"/> <property value="100" name="y"/>
 *                      </pageflow-object> <pageflow-object
 *                      id="action:charts.do"> <property value="190" name="x"/>
 *                      <property value="170" name="y"/> </pageflow-object>
 *                      <pageflow-object id="action:detailpage.do"> <property
 *                      value="140" name="x"/> <property value="120" name="y"/>
 *                      </pageflow-object> <pageflow-object
 *                      id="action:getRBU.do"> <property value="200" name="x"/>
 *                      <property value="180" name="y"/> </pageflow-object>
 *                      <pageflow-object id="action:listreport.do"> <property
 *                      value="160" name="x"/> <property value="140" name="y"/>
 *                      </pageflow-object> <pageflow-object
 *                      id="page:/WEB-INF/jsp/templates/mainTemplate.jsp">
 *                      <property value="80" name="x"/> <property value="60"
 *                      name="y"/> </pageflow-object> <pageflow-object
 *                      id="page:/WEB-INF/jsp/components/report/RBUReportListXls.jsp"
 *                      > <property value="270" name="x"/> <property value="250"
 *                      name="y"/> </pageflow-object> <pageflow-object id=
 *                      "forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:begin.do@"
 *                      > <property value="44,-39,-39,69" name="elbowsX"/>
 *                      <property value="92,92,16,16" name="elbowsY"/> <property
 *                      value="West_1" name="fromPort"/> <property
 *                      value="North_0" name="toPort"/> <property
 *                      value="success" name="label"/> </pageflow-object>
 *                      <pageflow-object id=
 *                      "forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:charts.do@"
 *                      > <property value="154,91,91,91" name="elbowsX"/>
 *                      <property value="162,162,133,104" name="elbowsY"/>
 *                      <property value="West_1" name="fromPort"/> <property
 *                      value="South_2" name="toPort"/> <property
 *                      value="success" name="label"/> </pageflow-object>
 *                      <pageflow-object id=
 *                      "forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:detailpage.do@"
 *                      > <property value="104,-39,-39,91" name="elbowsX"/>
 *                      <property value="112,112,16,16" name="elbowsY"/>
 *                      <property value="West_1" name="fromPort"/> <property
 *                      value="North_2" name="toPort"/> <property
 *                      value="success" name="label"/> </pageflow-object>
 *                      <pageflow-object id=
 *                      "forward:path#successXls#/WEB-INF/jsp/components/report/RBUReportListXls.jsp#@action:listreport.do@"
 *                      > <property value="196,270,270,270" name="elbowsX"/>
 *                      <property value="132,132,169,206" name="elbowsY"/>
 *                      <property value="East_1" name="fromPort"/> <property
 *                      value="North_1" name="toPort"/> <property
 *                      value="successXls" name="label"/> </pageflow-object>
 *                      <pageflow-object id=
 *                      "forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:listreport.do@"
 *                      > <property value="124,80,80,80" name="elbowsX"/>
 *                      <property value="121,121,112,104" name="elbowsY"/>
 *                      <property value="West_0" name="fromPort"/> <property
 *                      value="South_1" name="toPort"/> <property
 *                      value="success" name="label"/> </pageflow-object>
 *                      <pageflow-object id="control:db.TrDB#trDb"> <property
 *                      value="26" name="x"/> <property value="34" name="y"/>
 *                      </pageflow-object> <pageflow-object
 *                      id="page:/WEB-INF/jsp/templates/blankTemplate.jsp">
 *                      <property value="120" name="x"/> <property value="100"
 *                      name="y"/> </pageflow-object> </view-properties> ::
 */

public class LaunchMeetingControllerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/*
	 * Infosys - Weblogic to Jboss Migrations changes start here: Added the below code to
	 * define variables used
	 */
	/**
	 * @common:control
	 */
	/* private db.TrDB trDb; */
	private static final long serialVersionUID = 1L;
	TransactionDB trDb = new TransactionDB();
	Map masterMap = new HashMap();

	protected static final Log log = LogFactory
			.getLog(P2lControllerAction.class);

	private static final String TSR_ADMIN = "TSR Admin";

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

	/* Infosys - Weblogic to Jboss Migrations changes end here */

	public FutureAllignmentBuDataBean[] buDataBean;
	public FutureAllignmentRBUDataBean[] rbuDataBean;
	public Employee[] excelRBUEmployee;

	// Uncomment this declaration to access Global.app.
	//
	// protected global.Global globalApp;
	//

	// For an example of page flow exception handling see the example "catch"
	// and "exception-handler"
	// annotations in {project}/WEB-INF/src/global/Global.app

	/**
	 * This method represents the point of entry into the pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward begin()
	public String begin() {
		return charts();
	}

	/**
	 * getFilteredChart This method represents the point of entry into the
	 * pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward charts()
	public String charts()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{

		// callSecurePage();
		
		if ( getResponse().isCommitted() ) { return null; }
		 
		
		String action = "";
		try {
			if (this.getRequest().getParameter("firstTime") != null) {
				action = this.getRequest().getParameter("firstTime");
			}
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			String trackId = "";
			if (getRequest().getParameter("track") != null) {
				trackId = getRequest().getParameter("track");
			}
			// Get track info and store it in session.
			LaunchMeetingHandler p2l = new LaunchMeetingHandler();
			LaunchMeeting track = uSession.getLaunchMeetingTrack();
			if (track == null || !Util.isEmpty(trackId)) {
				track = p2l.getTrack(trackId);
				uSession.setLaunchMeetingTrack(track);
			}

			/* Infosys - Weblogic to Jboss Migrations changes start here */
			getRequest().getSession().setAttribute("ReportType", "RBUChart");
			/* Infosys - Weblogic to Jboss Migrations changes end here */
			boolean fromFilter = false;
			PieChart pieChart = null;
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "","LAUNCHMEETING");
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			ServiceFactory factory = Service.getServiceFactory(trDb);
			LaunchMeetingHandler rbuHandler = new LaunchMeetingHandler();
			UserFilter uFilter = uSession.getUserFilter();

			// RBUSHandler handler = new RBUSHandler();
			User user = uSession.getUser();
			String emplid = "";
			// System.out.println("################# User emplid " +
			// user.getEmplid());
			// Get the BU list
			buDataBean = trDb.getBuForLaunchMeeting();
			// Get the rbu list
			rbuDataBean = trDb.getRbuForLaunchMeeting();
			String selectedProduct = "";
			String selectedBU = "";
			String selectedRBU = "";
			if (action.equals("true")) {
				// this.getRequest().setAttribute("Attendance", "Attendance");
				selectedBU = "ALL";
				selectedRBU = "ALL";
			}
			if (this.getRequest().getParameter("selectedBU") != null) {
				selectedBU = this.getRequest().getParameter("selectedBU");

			}
			if (this.getRequest().getParameter("selectedRBU") != null) {
				selectedRBU = this.getRequest().getParameter("selectedRBU");

			}
			if (selectedBU.equals("")) {
				selectedBU = "ALL";
			}
			if (selectedRBU.equals("")) {
				selectedRBU = "ALL";
			}

			String emplidAccess = "";
			System.out.println("################# User emplid " + user.getEmplid());
			emplidAccess = rbuHandler.getNTIdExistance(user.getEmplid());
			if (emplidAccess.equals("NF")) {
				// System.out.println("################ User not found");
				emplidAccess = "";
			}

			System.out.println("Selected BU ########## " + selectedBU+ "selectedRBU " + selectedRBU);
			List charts = new ArrayList();
			PieChartBuilder pBuilder = new PieChartBuilder();
			qStrings.setSection("");
			RBUChartBean[] thisRBUChartBean;
			ChartDetailWc chartDetailWc = null;
			LaunchMeetingDetails phase = null;
			LaunchMeetingChartHeaderWc chartHeaderWc;
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			if (track.getCompletePhaseList().size() > 0) {
				for (Iterator it = track.getCompletePhaseList().iterator(); it.hasNext();) {
					phase = (LaunchMeetingDetails) it.next();
					System.out.println("track:>>>>>>>>>>>>"+ phase.getTrack().getTrackType());
					System.out.println("Pahse Number >>>>>>>>>>> "+ phase.getPhaseNumber()+ "Activity id ################ "+ phase.getAlttActivityId());			
					if (LaunchMeetingDetails.EMPTY_FLAG.equals(phase.getPhaseNumber())) {
						chartDetailWc = new ChartDetailWc();
						chartDetailWc.setLayout(ChartDetailWc.LAYOUT_EMPTY);
						charts.add(chartDetailWc);
					} else {
						if (!phase.getAttendance() && !phase.getOverall()) {
							// For PED exams
							thisRBUChartBean = (RBUChartBean[]) rbuHandler.getLaunchMeetingExamChart(user, phase,uFilter, selectedBU, selectedRBU,emplidAccess);
						pieChart = pBuilder.getLaunchMeetingChart(uSession.getUserFilter(), getRequest().getSession(),thisRBUChartBean, phase.getPhaseNumber(),fromFilter, phase.getTrack().getTrackId());
						chartDetailWc = new ChartDetailWc(pieChart,"Attendance chart",new ChartLaunchMeetingLegendWc(phase));
							if (pieChart.getCount() > 0) charts.add(chartDetailWc);
						}
						if (phase.getAttendance()) {
							// For Attendance
							thisRBUChartBean = (RBUChartBean[]) rbuHandler
									.getLaunchMeetingAttendanceChart(user, phase,
											uFilter, selectedBU, selectedRBU,
											track.getTrackId(), emplidAccess);
							pieChart = pBuilder.getLaunchMeetingChart(uSession
									.getUserFilter(), getRequest().getSession(),
									thisRBUChartBean, phase.getPhaseNumber(),
									fromFilter, phase.getTrack().getTrackId());
							chartDetailWc = new ChartDetailWc(pieChart,
									"Attendance chart",
									new ChartLaunchMeetingLegendWc(phase));
							if (pieChart.getCount() > 0)
								charts.add(chartDetailWc);
						}
						if (phase.getOverall()) {
							// For PED exams
							thisRBUChartBean = (RBUChartBean[]) rbuHandler
									.getLaunchMeetingOverallChart(user, phase,
											uFilter, selectedBU, selectedRBU,
											track.getTrackId(), emplidAccess);
							pieChart = pBuilder.getLaunchMeetingChart(uSession
									.getUserFilter(), getRequest().getSession(),
									thisRBUChartBean, phase.getPhaseNumber(),
									fromFilter, phase.getTrack().getTrackId());
							chartDetailWc = new ChartDetailWc(pieChart,
									"Attendance chart",
									new ChartLaunchMeetingLegendWc(phase));
							if (pieChart.getCount() > 0)
								charts.add(chartDetailWc);
						}
					}

				}
				chartHeaderWc = new LaunchMeetingChartHeaderWc(selectedBU,
						selectedRBU, pieChart.getCount());
			} else {
				chartDetailWc = new ChartDetailWc();
				chartDetailWc.setLayout(ChartDetailWc.LAYOUT_EMPTY);
				charts.add(chartDetailWc);
				chartHeaderWc = new LaunchMeetingChartHeaderWc(selectedBU,
						selectedRBU, 0);
			}
			// this component puts the header info in the chart page.

			// ChartHeaderWc chartHeaderWc = new ChartHeaderWc(
			// terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(),
			// totalParticipants, uFilter.getProduct(),teamDesc);

			LaunchMeetingChartsWc chartpage = new LaunchMeetingChartsWc(
					uSession.getUser(), chartHeaderWc);
			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			// chartListWc.setTrainees(pieChart.getCount());
			chartpage.setFutureAllignmentBuDataBean(buDataBean);
			chartpage.setFutureAllignmentRBUDataBean(rbuDataBean);
			chartpage.setFirstRequest("Attendance");
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);
			/* Infosys - Weblogic to Jboss Migrations changes start here */
			this.getRequest().getSession().setAttribute("selectedBU", selectedBU);
			this.getRequest().getSession().setAttribute("selectedRBU", selectedRBU);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			this.getRequest().getSession().setAttribute("fromRequest", "");
			// return new Forward("success");
			return ("success");
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		}
		/* Infosys - Weblogic to Jboss Migrations changes end here */

	}

	public PieChart getPhaseChart(UserTerritory ut, LaunchMeetingDetails phase,
			UserFilter uFilter, String altNode, Collection employees,
			boolean detailflag, String emplid, String selectedBU,
			String selectedRBU) {
		StringBuffer sb = new StringBuffer();
		// Timer timer = new Timer();
		LaunchMeetingHandler p2l = new LaunchMeetingHandler();
		boolean rmFlag = false;
		if ("POA".equals(phase.getTrackId())) {
			rmFlag = true;
		}
		int registered = 0;
		int assigned = 0;
		int exempt = 0;
		int complete = 0;
		int onleave = 0;
		int pending = 0;
		Collection master = null;
		if (!phase.getOverall() && !phase.getAttendance()) {
			System.out.println("its not overall ####################");
			master = p2l.getPhaseStatus(ut, phase, uFilter, detailflag, "",
					selectedBU, selectedRBU);
		}

		if (phase.getOverall()) {
			System.out.println("its  overall ####################");
			master = p2l.getPhaseStatusOverallForLaunchMeeting(ut, phase,
					uFilter, detailflag, "");
		}

		if (phase.getAttendance()) {
			System.out.println("its  overall ####################");
			master = p2l.getPhaseStatusForAttendance(ut, phase, uFilter,
					detailflag, "");
		}
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();
			if ("Registered".equals(st.getStatus())) {
				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					onleave++;
				} else {
					registered++;
				}
			} else if ("Assigned".equals(st.getStatus())) {
				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
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
		/*
		 * if(phase.getAttendance()){
		 * 
		 * System.out.println("its  attendance ####################"); HashMap
		 * count = p2l.getAttendanceCount(ut,phase, uFilter,detailflag,"");
		 * for(Iterator iter=count.keySet().iterator();iter.hasNext();){ String
		 * status = iter.next().toString(); if(status.equals("Complete")){
		 * complete = ((Integer)count.get(status)).intValue(); }
		 * if(status.equals("InComplete")){ registered =
		 * ((Integer)count.get(status)).intValue(); }
		 * if(status.equals("OnLeave")){ onleave =
		 * ((Integer)count.get(status)).intValue(); } } }
		 */
		HashMap result = new HashMap();
		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();

		data.add(new ChartData("Complete", complete));

		/*
		 * if ( phase.getExempt() ) { data.add(new ChartData( "Exempt", exempt )
		 * ); tMap.put( "Exempt", new Color(255,153,0) ); } if (
		 * phase.getAssigned()) { data.add(new ChartData( "Assigned", assigned)
		 * ); tMap.put( "Assigned", new Color(153,204,0) ); } if (
		 * phase.getApprovalStatus() ) { tMap.put( "Pending",
		 * AppConst.COLOR_CYAN ); data.add(new ChartData( "Pending", pending )
		 * ); }
		 */
		// else {
		data.add(new ChartData("Registered", registered));
		// }
		data.add(new ChartData("On-Leave", onleave));

		PieChartBuilder builder = new PieChartBuilder();

		tMap.put("Complete", AppConst.COLOR_BLUE);

		tMap.put("Registered", new Color(153, 51, 0));
		tMap.put("On-Leave", new Color(253, 243, 156));
		Map colorMap = Collections.unmodifiableMap(tMap);

		String ak = phase.getRootActivityId();
		String label = phase.getTrack().getTrackLabel() + " : "
				+ phase.getPhaseNumber();
		/* Infosys - Weblogic to Jboss Migrations changes start here */
		// chart =
		// builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk="
		// + ak + "&phaseNumber=" +phase.getPhaseNumber()+"&track="+
		// phase.getTrackId(),getRequest().getSession(), colorMap);
		chart = builder.generate(
				data,
				uFilter.getQuseryStrings().getSection(),
				label,
				"listreport?activitypk=" + ak + "&phaseNumber="
						+ phase.getPhaseNumber() + "&track="
						+ phase.getTrackId(), getRequest().getSession(),
				colorMap);

		/* Infosys - Weblogic to Jboss Migrations changes end here */
		chart.setCount(complete + exempt + assigned + registered + onleave);
		employees.addAll(master);
		return chart;
	}

	public PieChart getOverallChart(LaunchMeeting track, UserFilter uFilter,
			String altNode, Collection employees, boolean detailflag,
			String emplid, String otherNodes) {
		StringBuffer sb = new StringBuffer();
		// Timer timer = new Timer();
		LaunchMeetingHandler p2l = new LaunchMeetingHandler();
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
		for (Iterator it = master.iterator(); it.hasNext();) {
			P2lEmployeeStatus st = (P2lEmployeeStatus) it.next();

			if ("Registered".equals(st.getStatus())) {
				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
					st.setStatus("On-Leave");
					onleave++;
				} else {
					registered++;
				}
			} else if ("Assigned".equals(st.getStatus())) {
				if ("On-Leave".equals(st.getEmployee().getEmployeeStatus())) {
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
		LaunchMeetingDetails phase = (LaunchMeetingDetails) track.getPhases()
				.get(0);

		PieChart chart = null;

		List data = new ArrayList();
		Map tMap = new HashMap();
		data.add(new ChartData("Complete", complete));

		/*
		 * if (phase.getExempt()) { data.add(new ChartData( "Exempt", exempt )
		 * ); tMap.put( "Exempt", new Color(255,153,0) ); } if
		 * (phase.getAssigned()) { data.add(new ChartData( "Assigned", assigned)
		 * ); tMap.put( "Assigned", new Color(153,204,0) ); }
		 */
		data.add(new ChartData("Registered", registered));
		data.add(new ChartData("On-Leave", onleave));

		PieChartBuilder builder = new PieChartBuilder();

		tMap.put("Complete", AppConst.COLOR_BLUE);

		tMap.put("Registered", new Color(153, 51, 0));

		tMap.put("On-Leave", new Color(253, 243, 156));
		Map colorMap = Collections.unmodifiableMap(tMap);

		String ak = "";
		String label = "";
		if (!Util.isEmpty(otherNodes)) {
			ak = "Overall";
			label = "Overall";
		}
		/* Infosys - Weblogic to Jboss Migrations changes start here */
		// chart =
		// builder.generate(data,uFilter.getQuseryStrings().getSection(),label,"listreport.do?activitypk="
		// + ak ,getRequest().getSession(), colorMap);
		chart = builder.generate(data, uFilter.getQuseryStrings().getSection(),
				label, "listreport?activitypk=" + ak,
				getRequest().getSession(), colorMap);
		/* Infosys - Weblogic to Jboss Migrations changes end here */
		chart.setCount(complete + exempt + assigned + registered + onleave);
		employees.addAll(master);
		return chart;
	}

	/**
	 * This setups the session object for this controller. Most of this can be
	 * reused but some stuff is unique to this controller.
	 */
	private UserSession buildUserSession() {
		UserSession uSession = UserSession.getUserSession(getRequest());
		TerritoryFilterForm filterForm = uSession.getUserFilterForm();
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
			ArrayList firstGeo = new ArrayList();
			firstGeo = uSession.getUser().getUserTerritory().getFirstDropdown();
			LabelValueBean labelValueBean;
			uSession.getUserFilterForm().setLevel1("All");
			FormUtil.loadObject(getRequest(), uSession.getUserFilterForm());
			labelValueBean = new LabelValueBean("All", "All");
			uSession.getUserFilterForm().setFirstList(labelValueBean);
			if (firstGeo != null) {
				for (int i = 0; i < firstGeo.size(); i++) {
					LabelValueBean tempLabelValueBean;
					tempLabelValueBean = (LabelValueBean) firstGeo.get(i);
					labelValueBean = new LabelValueBean(
							(String) tempLabelValueBean.getLabel(),
							(String) tempLabelValueBean.getValue());
					uSession.getUserFilterForm().setFirstList(labelValueBean);
				}
			}

		}
		// This populates the territory form object
		FormUtil.loadObject(getRequest(), filterForm);

		// process query stings
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(), qStrings);
		FormUtil.loadObject(getRequest(), qStrings.getSortBy());

		// This will give you the full query string if needed
		qStrings.setFullQueryString(getRequest().getQueryString());

		// Setup user filter obejct
		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setEmployeeId(uSession.getUser().getEmplid());
		uFilter.setAdmin(uSession.getUser().isAdmin());
		uFilter.setTsrAdmin(uSession.getUser().isTsrAdmin());
		// uFilter.setClusterCode(uSession.getUser().getCluster());
		uFilter.setFilterForm(filterForm);
		uFilter.setQueryStrings(qStrings);
		uFilter.setIsSpecialRoleUser(uSession.getUser().isSpecialRole());
		uFilter.setEmployeeIdForSplRole(uSession.getUser().getEmplIdForSpRole());
		// Get track info and store it in session.
		LaunchMeetingHandler p2l = new LaunchMeetingHandler();
		LaunchMeeting track = uSession.getLaunchMeetingTrack();
		if (track == null
				|| !Util.isEmpty(uFilter.getQuseryStrings().getTrack())) {
			track = p2l.getTrack(uFilter.getQuseryStrings().getTrack());
			uSession.setLaunchMeetingTrack(track);
		}

		return uSession;
	}

	/**
	 * This method represents the point of entry into the pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward detailpage(){
	public String detailpage() {
		
		 if ( getResponse().isCommitted() ) { return null; }
		 
				UserSession uSession = buildUserSession();
		UserFilter uFilter = uSession.getUserFilter();
		User user = uSession.getUser();
		String actionBy = user.getEmplid();
		PageBuilder builder = new PageBuilder();
		EmployeeHandler eHandler = new EmployeeHandler();
		LaunchMeetingHandler pHandler = new LaunchMeetingHandler();
		LaunchMeeting track = uSession.getLaunchMeetingTrack();
		LaunchMeetingHandler p2l = new LaunchMeetingHandler();

		// get Employee object
		try {
			Employee emp = eHandler.getEmployeeById(uFilter.getQuseryStrings()
					.getEmplid());

			// build employee info section
			EmployeeInfoWc employeeInfo = new EmployeeInfoWc(emp);
			employeeInfo.setImage(eHandler.getEmployeeImage(emp.getEmplId()));
			employeeInfo.setManager(eHandler.getEmployeeById(emp
					.getReportsToEmplid()));
			employeeInfo.setEmailSubject(track + " Follow-up");
			String trackId = uSession.getLaunchMeetingTrack().getTrackId();
			TrainingSummaryWc tsummary = new TrainingSummaryWc();
			LaunchMeetingHandler handler = new LaunchMeetingHandler();
			if (getRequest().getParameter("commandattendance") != null) {
				String attendance = getRequest().getParameter("attendance");
				System.out.println("############################# attendance "
						+ attendance);
				if (attendance.equals("Y")) {
					// Update completion code for attendance
					handler.updateToviazLaunchAttComplete(uFilter
							.getQuseryStrings().getEmplid(), actionBy, trackId);

				}
				if (attendance.equals("N")) {
					// Register for Breeze compliance
					handler.updateRegistrationForBreezeCompliance(uFilter
							.getQuseryStrings().getEmplid(), actionBy, trackId);
					getRequest().setAttribute("noClicked", "noClicked");
				}
			}

			if (getRequest().getParameter("commandmanagercertificaion") != null) {
				String certification = getRequest().getParameter(
						"managerCertification");
				System.out.println("#############################  certification "
						+ certification);
				if (certification.equals("Y")) {
					// Update completion code for Post LAunch conference
					handler.updateToviazLaunchManagerCertification(uFilter
							.getQuseryStrings().getEmplid(), actionBy, trackId);

				}
			}
			// get track and phase info
			String activityPk = uFilter.getQuseryStrings().getActivitypk();
			LaunchMeetingDetails trackPhase = null;
			String label = "";
			String phaseNumber = "";
			String trackIdSelected = "";
			String section = "";
			if (getRequest().getParameter("phaseNumber") != null) {
				phaseNumber = getRequest().getParameter("phaseNumber");
			}
			if (getRequest().getParameter("section") != null) {
				section = getRequest().getParameter("section");
			}
			if (getRequest().getParameter("track") != null) {
				trackId = getRequest().getParameter("track");
			}
			OverallStatusWc overallWc = null;
			if ("Overall".equals(activityPk)) {
				label = "Overall";
				trackPhase = (LaunchMeetingDetails) track.getPhases().get(0);
			} else {

				trackPhase = p2l.getTrackPhaseForList(phaseNumber + "",
						track.getTrackId());
				label = trackPhase.getPhaseNumber();
			}
			Collection result = new ArrayList();
			getOverallChart(track, uFilter, "", result, true, uSession.getUser()
					.getId(), track.getAllNodesDelimit());
			for (Iterator it = result.iterator(); it.hasNext();) {
				P2lEmployeeStatus tm = (P2lEmployeeStatus) it.next();
				if (emp.getEmplId().equals(tm.getEmployee().getEmplId())) {
					overallWc = new OverallStatusWc(tm.getStatus());
				}
			}
			// check if complete button was clicked
			if (uFilter.getQuseryStrings().getCactivityid() > 0) {
				p2l.insertComplete(uSession, uFilter.getQuseryStrings()
						.getCactivityid() + "", emp);
				uFilter.getQuseryStrings().setCactivitid("0");
			}

			// Get the list of ped exams
			List pedExams = pHandler.getPedExamsList(emp.getGuid(),
					trackPhase.getTrackId());

			List testresult = pHandler.getAttendanceList(emp.getGuid(),
					trackPhase.getTrackId());
			String attendanceCode = "";
			String managerCode = "";
			String compliancePresentationCode = "";
			Iterator it = testresult.iterator();
			while (it.hasNext()) {
				LaunchMeetingTrainingStatus status = (LaunchMeetingTrainingStatus) it
						.next();
				if (status.getCourseName().equals("Attendance_Code")) {
					attendanceCode = status.getStatus();
				}
				if (status.getCourseName().equals("Manager_Training_Code")) {
					managerCode = status.getStatus();
				}
				if (status.getCourseName().equals("Compliance_Presentation_Code")) {
					compliancePresentationCode = status.getStatus();
				}
			}
			// Check whether the employee has completed the Compliance presentation
			String complianceStatus = pHandler.getComplianceStatus(emp.getGuid(),
					trackPhase);
			if (!complianceStatus.equals("")) {
				compliancePresentationCode = complianceStatus;
			}
			boolean showAttendance = false;
			// Get the overall Status
			String overallStatus = "";
			overallStatus = pHandler.getOverallStatus(trackId, emp.getEmplId());

			// Check whether there is a Attendance pie define for this track. IF its
			// there then only
			// show the attendance section.
			showAttendance = pHandler.isAttendanceDefined(trackPhase.getTrackId());
			System.out.println("Value of boolean here is ######### "
					+ showAttendance);
			System.out.println("Getting various values " + attendanceCode
					+ "Compliance #### " + compliancePresentationCode + "Manager "
					+ managerCode);
			PhaseTrainingDetailLaunchMeetingWc pDetail = new PhaseTrainingDetailLaunchMeetingWc(
					emp, track, trackPhase, uSession, pedExams, attendanceCode,
					managerCode, compliancePresentationCode, showAttendance,
					overallStatus);
			// pDetail.setStatus(testresult);
			DetailPageLaunchMeetingWc main = new DetailPageLaunchMeetingWc(
					employeeInfo, tsummary, pDetail, uSession, phaseNumber,
					section, trackIdSelected);
			main.setTrack(track);
			main.setActivityId(activityPk);
			main.setPageName(label);

			/*
			 * if ( track.getDoOverall() ) { main.setOverallStatus(overallWc); }
			 */
			if ("debug".equals(uSession.getUserFilter().getQuseryStrings()
					.getMode())) {
				pDetail.setDebug(true);
			}
			MainTemplateWpc page = builder.buildPageP2l(main, "Report List",
					uSession.getUser(), "reportselect");
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/* Infosys - Weblogic to Jboss Migrations changes start here */
			// return new Forward("success");
			return ("success");
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		}
		/* Infosys - Weblogic to Jboss Migrations changes end here */
	}

	/**
	 * @jpf:action
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward getRBU(){
	public String getRBU() {
		/* Infosys - Weblogic to Jboss Migrations changes end here */
		HttpServletRequest req = this.getRequest();
		String bu = "Primary Care";
		
		System.out.println("in getRBU");
		try {
			if (req.getParameter("bu") != null) {
				req.getParameter("bu");
			} else {
				bu = "Primary Care";
			}
			// System.out.println("From request ###################" +
			// req.getParameter("bu"));
			FutureAllignmentRBUDataBean[] rbuBean;
			// String[] rbuBean;
			// String query =
			// "SELECT DISTINCT  RBU_DESC as RBU from V_RBU_FUTURE_ALIGNMENT where bu='"
			// + bu.trim() + "' order by RBU_DESC asc";
			// System.out.println("Query here is ###############" + query);
			if (req.getParameter("bu").equals("ALL")) {
				System.out.println("in if");
				rbuBean = trDb.getRbuForLaunchMeeting();
			} else {
				System.out.println("in else");
				rbuBean = trDb.getRbuForRBUForLaunchMeeting(req.getParameter("bu"));
			}
			StringBuffer result = new StringBuffer();
			if (rbuBean != null) {
				for (int i = 0; i < rbuBean.length; i++) {
					// String rbu = rbuBean[1].getRbu();
					String rbu = rbuBean[i].getRbu();
					result.append(rbu);
					if (i < rbuBean.length) {
						result.append("|");
					}
				}
			}
			String output = "";
			System.out.println("the result is$$$$ "+result.length());
			/*if (result.toString() != null) {*/
			if (result.length() > 0) {
				output = result.toString().substring(0,
						result.toString().length() - 1);
			}
			// System.out.println("##############" + output);
			try {
				getResponse().setContentType("text/xml");
				getResponse().setHeader("Cache-Control", "no-cache");
				getResponse().getWriter().write(output);
			} catch (Exception ex) {
			}

			return null;
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/components/report/RBUReportListXls.jsp"
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward listreport(){
	public String listreport() {
		/* Infosys - Weblogic to Jboss Migrations changes end here */
		System.out
				.println("Inside list report ########################################");
		
		
		if ( getResponse().isCommitted() ) { return null; }
		
		Employee[] rbuEmployee = null;

		ServiceFactory factory = Service.getServiceFactory(trDb);
		LaunchMeetingHandler eHandler = new LaunchMeetingHandler();
		WebPageComponent page;
		String selectedBU = "ALL";
		String selectedRBU = "ALL";
		String selectedSection = "";
		String fromRequest = "";
		UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());
		RBUSHandler handler = new RBUSHandler();
		User user = uSession.getUser();
		String emplid = "";
		// System.out.println("################# User emplid " +
		// user.getEmplid());
		try {
			LaunchMeeting track = uSession.getLaunchMeetingTrack();
			LaunchMeetingDetails phase = null;
			String emplidAccess = "";
			System.out.println("################# User emplid " + user.getEmplid());
			emplidAccess = eHandler.getNTIdExistance(user.getEmplid());
			if (emplidAccess.equals("NF")) {
				// System.out.println("################ User not found");
				emplidAccess = "";
			}
			String trackId = "";
			String phaseNumber = "";
			if (getRequest().getParameter("trackId") != null) {
				trackId = getRequest().getParameter("trackId");
			}
			if (getRequest().getParameter("type") != null) {
				phaseNumber = getRequest().getParameter("type");
			}
			if (!trackId.equals("") && !phaseNumber.equals("")) {
				phase = eHandler.getTrackPhaseForList(phaseNumber, trackId);
			}

			emplid = handler.getNTIdExistance(user.getEmplid());
			if (emplid.equals("NF")) {
				// System.out.println("################ User not found");
				emplid = "";
			}
			if (this.getRequest().getParameter("fromRequest") != null) {
				fromRequest = this.getRequest().getParameter("fromRequest");
			}
			/* Weblogivc to Jboss migration changes start here */
			if (this.getRequest().getSession().getAttribute("selectedBU") != null) {
				selectedBU = (String) this.getRequest().getSession()
						.getAttribute("selectedBU");
				this.getRequest().getSession().removeAttribute("selectedBU");
			}
			if (this.getRequest().getSession().getAttribute("selectedRBU") != null) {
				selectedRBU = (String) this.getRequest().getSession()
						.getAttribute("selectedRBU");
				this.getRequest().getSession().removeAttribute("selectedRBU");
				/* Weblogivc to Jboss migration changes end here */
			}

			// Get the BU list
			buDataBean = trDb.getBuForLaunchMeeting();
			if (selectedBU.equals("ALL")) {
				// Get the rbu list
				rbuDataBean = trDb.getRbuForLaunchMeeting();
			} else {
				rbuDataBean = trDb.getRbuForRBUForLaunchMeeting(selectedBU);
			}
			String section = "";
			if (getRequest().getParameter("section") != null) {
				section = getRequest().getParameter("section");
			}
			// check if coming from email, if so reset search to
			// clear all filters in session.
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			// UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			FormUtil.loadObject(getRequest(), filterForm);
			// if(filterForm.getTeam().trim().equalsIgnoreCase(""))
			// filterForm.setTeam("All") ;
			// System.out.println("TEAM IN JPF is"+filterForm.getTeam());
			UserFilter uFilter = uSession.getUserFilter();
			uFilter.setQueryStrings(qStrings);
			uFilter.setAdmin(uSession.getUser().isAdmin());
			// System.out.println("Product is "+qStrings.getType());

			// System.out.println("Value of selectedBU #########   " + selectedBU +
			// "selectedRBU####   " + selectedRBU);
			PieChart pieChart = null;

			OverallProcessor overall = uSession.getOverallProcessor();

			PieChartBuilder pBuilder = new PieChartBuilder();
			RBUChartBean[] thisRBUChartBean;
			boolean fromFilter = false;
			RBUPieChartHandler rbuHandler = factory.getRBUPieChartHandler();
			ChartDetailWc chartDetailWc = null;
			String productCD = "";
			String type = "";
			String downloadExcel = "";
			// String excelType = "";
			String excelSection = "";
			String prodDesc = "";
			/* Weblogivc to Jboss migration changes start here */
			if (this.getRequest().getSession().getAttribute("exam") != null) {
				this.getRequest().getSession().removeAttribute("exam");
			}
			/* Weblogivc to Jboss migration changes end here */
			if (fromRequest.equals("reRun")) {

				if (this.getRequest().getParameter("selectedProductDesc") != null) {
					prodDesc = this.getRequest()
							.getParameter("selectedProductDesc");
				}
				productCD = prodDesc;
				
				if (this.getRequest().getSession().getAttribute("selectedSection") != null) {
					selectedSection = (String) this.getRequest().getSession().getAttribute(
							"selectedSection");
					this.getRequest().getSession().removeAttribute("selectedSection");
				}
				if (this.getRequest().getParameter("selectedProduct") != null) {
					String selectedProd = this.getRequest().getParameter(
							"selectedProduct");
					type = selectedProd;
				}
				if (this.getRequest().getParameter("selectedBU") != null) {
					selectedBU = this.getRequest().getParameter("selectedBU");
				}
				if (this.getRequest().getParameter("selectedRBU") != null) {
					selectedRBU = this.getRequest().getParameter("selectedRBU");
				}
				if (this.getRequest().getParameter("type") != null) {
					type = this.getRequest().getParameter("type");
				}
				qStrings = new AppQueryStrings();
				qStrings.setSection(selectedSection);
				uFilter.setQueryStrings(qStrings);
			} else {
				selectedSection = qStrings.getSection();
				type = qStrings.getType();
				productCD = type;
			}
			if (selectedSection == null) {
				selectedSection = "";
			}
			if (type == null) {
				type = "";
			}
			if (productCD == null) {
				productCD = "";
			}

			if (this.getRequest().getParameter("downloadExcel") != null) {
				downloadExcel = this.getRequest().getParameter("downloadExcel");
			}
			/*
			 * if(this.getRequest().getParameter("excelType")!= null){ excelType =
			 * this.getRequest().getParameter("excelType"); }
			 */
			if (this.getRequest().getParameter("excelSection") != null) {
				excelSection = this.getRequest().getParameter("excelSection");
			}
			String displayType = productCD;
			if (!phase.getAttendance() && !phase.getOverall()) {
				// For PED exams
				thisRBUChartBean = (RBUChartBean[]) eHandler
						.getLaunchMeetingExamChart(user, phase, uFilter,
								selectedBU, selectedRBU, emplidAccess);
				System.out.println("Section ##################### "
						+ uFilter.getQuseryStrings().getSection());
				System.out.println("Bean " + thisRBUChartBean.length);
				System.out.println("Pahse nUmber " + phase.getPhaseNumber());
				System.out.println("TrackId " + trackId);
				pieChart = new PieChartBuilder().getLaunchMeetingChart(uFilter,
						getRequest().getSession(), thisRBUChartBean,
						phase.getPhaseNumber(), fromFilter, trackId);
				chartDetailWc = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLaunchMeetingLegendWc(phase));
				chartDetailWc.setLayout(ChartDetailWc.LAYOUT_ALT);

			}
			if (phase.getAttendance()) {
				System.out.println("In list report ############### for attendance");
				// For PED exams
				thisRBUChartBean = (RBUChartBean[]) eHandler
						.getLaunchMeetingAttendanceChart(user, phase, uFilter,
								selectedBU, selectedRBU, trackId, emplidAccess);
				pieChart = pBuilder.getLaunchMeetingChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean,
						phase.getPhaseNumber(), fromFilter, trackId);
				chartDetailWc = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLaunchMeetingLegendWc(phase));
				chartDetailWc.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if (phase.getOverall()) {
				// For PED exams
				thisRBUChartBean = (RBUChartBean[]) eHandler
						.getLaunchMeetingOverallChart(user, phase, uFilter,
								selectedBU, selectedRBU, trackId, emplidAccess);
				pieChart = pBuilder.getLaunchMeetingChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean,
						phase.getPhaseNumber(), fromFilter, trackId);
				chartDetailWc = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLaunchMeetingLegendWc(phase));
				chartDetailWc.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("true".equals(downloadExcel)) {
				System.out.println("");

				if (!phase.getAttendance() && !phase.getOverall()) {
					// excelRBUEmployee
					// =eHandler.getRBUOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
					// selectedBU, selectedRBU);
					excelRBUEmployee = eHandler.getLaunchMeetingExamEmployees(user,
							phase, uFilter, selectedBU, selectedRBU, excelSection,
							emplidAccess);
					//Infosys - Weblogic to Jboss Migration changes
					getRequest().getSession().setAttribute("exam", phase.getPhaseNumber());
				}
				if (phase.getAttendance()) {
					// excelRBUEmployee
					// =eHandler.getRBUEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
					// selectedBU, selectedRBU);
					// excelRBUEmployee
					// =eHandler.getRBUEmployees(uFilter,excelType,excelSection,
					// selectedBU, selectedRBU,"", emplid);
					excelRBUEmployee = eHandler
							.getLaunchMeetingAttendanceEmployees(user, phase,
									uFilter, selectedBU, selectedRBU, excelSection,
									trackId, emplidAccess);
				}
				if (phase.getOverall()) {
					// excelRBUEmployee
					// =eHandler.getRBUEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
					// selectedBU, selectedRBU);
					// excelRBUEmployee
					// =eHandler.getRBUEmployees(uFilter,excelType,excelSection,
					// selectedBU, selectedRBU,"", emplid);
					excelRBUEmployee = eHandler.getLaunchMeetingOverallEmployees(
							user, phase, uFilter, selectedBU, selectedRBU,
							excelSection, trackId, emplidAccess);
				}
				ListReportWpc reportList = new ListReportWpc(uSession.getUser(),
						uFilter, chartDetailWc, excelRBUEmployee, "LAUNCHMEETING",
						buDataBean, rbuDataBean);
				page = new ListReportWpc(uSession.getUser(), uFilter,
						chartDetailWc, excelRBUEmployee, true, "LAUNCHMEETING");
				// page = new ListReportWpc( uSession.getUser(), uFilter,
				// chart,excelRBUEmployee,"RBUREPORT", productDataBean,
				// buDataBean,rbuDataBean);
				/*Infosys - Weblogic to Jboss migration changes start here*/
				getRequest().getSession().setAttribute("xlsBean", excelRBUEmployee);
				getRequest().getSession().setAttribute("xlsType", type);
				getRequest().getSession().setAttribute("section", excelSection);
				/*Infosys - Weblogic to Jboss migration changes end here*/
				/* Infosys - Weblogic to Jboss Migrations changes start here */
				// return new Forward("successXls");
				return ("successXls");
				/* Infosys - Weblogic to Jboss Migrations changes end here */
			}

			if (!phase.getAttendance() && !phase.getOverall()) {
				// System.out.println("Getting the employee list over all ##################################");
				// rbuEmployee
				// =eHandler.getRBUOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
				// selectedBU, selectedRBU);
				System.out
						.println("Getting the employees for list #######################");
				rbuEmployee = eHandler.getLaunchMeetingExamEmployees(user, phase,
						uFilter, selectedBU, selectedRBU, uFilter
								.getQuseryStrings().getSection(), emplidAccess);
				//Infosys - Weblogic to Jboss migration changes
				getRequest().getSession().setAttribute("exam", phase.getPhaseNumber());
			}

			if (phase.getAttendance()) {
				// rbuEmployee
				// =eHandler.getRBUEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
				// selectedBU, selectedRBU);
				// rbuEmployee
				// =eHandler.getRBUEmployees(uFilter,type,selectedSection,
				// selectedBU, selectedRBU,"", emplid);
				rbuEmployee = eHandler.getLaunchMeetingAttendanceEmployees(user,
						phase, uFilter, selectedBU, selectedRBU, uFilter
								.getQuseryStrings().getSection(), trackId,
						emplidAccess);
			}
			if (phase.getOverall()) {
				// rbuEmployee
				// =eHandler.getRBUEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
				// selectedBU, selectedRBU);
				rbuEmployee = eHandler.getLaunchMeetingOverallEmployees(user,
						phase, uFilter, selectedBU, selectedRBU, uFilter
								.getQuseryStrings().getSection(), trackId,
						emplidAccess);
				// rbuEmployee
				// =eHandler.getRBUEmployees(uFilter,type,selectedSection,
				// selectedBU, selectedRBU,"", emplid);
			}
			// System.out.println("After Getting the employee list ##################################");
			if (pieChart.getCount() == 0)
				chartDetailWc = null;
			page = new ListReportWpc(uSession.getUser(), uFilter, chartDetailWc,
					rbuEmployee, "LAUNCHMEETING", buDataBean, rbuDataBean, phase,
					trackId);
			uFilter.setClusterCode(uSession.getUser().getCluster());
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			getRequest().setAttribute("type", qStrings.getType());
			uSession.setOverallProcessor(null);
			/*Infosys - Weblogic to Jboss migration changes start here*/
			if (getRequest().getSession().getAttribute("selectedBU") != null) {
				getRequest().getSession().removeAttribute("selectedBU");
			}
			if (getRequest().getSession().getAttribute("selectedRBU") != null) {
				getRequest().getSession().removeAttribute("selectedRBU");
			}
			if (getRequest().getSession().getAttribute("selectedSection") != null) {
				getRequest().getSession().removeAttribute("selectedSection");
			}
			if (getRequest().getSession().getAttribute("type") != null) {
				getRequest().getSession().removeAttribute("type");
			}
			getRequest().getSession().setAttribute("selectedBU", selectedBU);
			getRequest().getSession().setAttribute("selectedRBU", selectedRBU);
			getRequest().getSession().setAttribute("selectedSection", selectedSection);
			getRequest().getSession().setAttribute("type", type);
			// return new Forward("success");
			return ("success");
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		}
		/* Infosys - Weblogic to Jboss Migrations changes end here */
	}



	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

}
