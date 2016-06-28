package com.pfizer.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.ForecastReport;
import com.pfizer.db.GAProdCourse;
import com.pfizer.db.GAProduct;
import com.pfizer.db.LaunchMeeting;
import com.pfizer.db.LaunchMeetingDetails;
// Added for TRT major enhancement 3.6 F2(Management Summary Report)
import com.pfizer.db.ManagementSummaryReport;
// end
import com.pfizer.db.MenuList;
import com.pfizer.db.P2lTrack;
import com.pfizer.db.P2lTrackPhase;
import com.pfizer.db.TrainingPathConfigBean;
import com.pfizer.hander.ForecastFilterHandler;
// end
import com.pfizer.hander.LaunchMeetingHandler;
// Added for TRT major enhancement 3.6 F2(Management Summary Report)
//import com.pfizer.hander.ForecastFilterHandler;
import com.pfizer.hander.ManagementFilterHandler;
import com.pfizer.hander.MoveCopyHandler;
import com.pfizer.hander.P2lHandler;
import com.pfizer.hander.TrainingPathHandler;
import com.pfizer.hander.TrainingReportGroupHandler;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.AddSelectedStatusCourseWc;
import com.pfizer.webapp.wc.components.EditArchiveWc;
import com.pfizer.webapp.wc.components.EditManagementFilterCriteriaWc;
import com.pfizer.webapp.wc.components.EditMenuWc;
import com.pfizer.webapp.wc.components.EditReportForecastFilterWc;
import com.pfizer.webapp.wc.components.SearchCourseForecastWc;
import com.pfizer.webapp.wc.components.TrainingPathAdminWc;
import com.pfizer.webapp.wc.components.UnauthorizedWc;
import com.pfizer.webapp.wc.components.searchCourseManagementWc;
import com.pfizer.webapp.wc.components.searchCourseTrainingPathWc;
import com.pfizer.webapp.wc.components.admin.ActivitySearchLaunchMeetingWc;
import com.pfizer.webapp.wc.components.admin.ActivitySearchWc;
import com.pfizer.webapp.wc.components.admin.CopyMoveWc;
import com.pfizer.webapp.wc.components.admin.EditForecastOptionalFieldsWc;
import com.pfizer.webapp.wc.components.admin.EditReportLaunchMeetingWc;
import com.pfizer.webapp.wc.components.admin.EditReportWc;
import com.pfizer.webapp.wc.components.admin.GapAnalysisAdminWc;
import com.pfizer.webapp.wc.components.search.CourseSearchWc;
import com.pfizer.webapp.wc.global.EmptyPageWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
// Added for TRT major enhancement 3.6 F2(Management Summary Report)
// end of addition

public class AdminHomeControllerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private TransactionDB trDb = new TransactionDB();
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

	/**
	 * @common:control
	 */
	// private db.TrDB trDb;
	// Uncomment this declaration to access Global.app.
	//
	// protected global.Global globalApp;
	//

	// For an example of page flow exception handling see the example "catch"
	// and "exception-handler"
	// annotations in {project}/WEB-INF/src/global/Global.app

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	// protected Forward begin()
	public String begin()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {
			return editMenu();
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editNode()
	public String editNode() {
		try {
			/* Infosys - Weblogic to Jboss Migrations changes end here */

			UserSession uSession = buildUserSession();

			BlankTemplateWpc page = new BlankTemplateWpc();
			// main.setRandomString("My string:" +
			// uSession.getUserFilter().getQuseryStrings().getId());
			P2lHandler p2l = new P2lHandler();
			P2lTrackPhase phase = p2l.getTrackPhaseById(uSession
					.getUserFilter().getQuseryStrings().getId());
			List result = new ArrayList();

			ActivitySearchWc main = new ActivitySearchWc();

			if ("root".equals(uSession.getUserFilter().getQuseryStrings()
					.getType())) {
				result = p2l.getActivityTree(phase.getRootActivityId());
				main.setType("root");
			} else {
				result = p2l.getActivityTree(phase.getAlttActivityId());
				main.setType("alt");
			}
			FormUtil.loadObject(getRequest(), main);

			if (!Util.isEmpty(main.getActivityname())
					|| !Util.isEmpty(main.getCode())) {

				List searchresult = new ArrayList();

				if (!Util.isEmpty(main.getCode())) {
					searchresult = p2l.getActivityTreeByCode(main.getCode());
				} else {
					searchresult = p2l.getActivityTreeByName(main
							.getActivityname().toUpperCase());
				}
				main.setSearchResults(searchresult);
			}
			main.setPhase(phase);
			main.setCurrent(result);
			page.setMain(main);

			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editReport()
	public String editReport() {
		try {
			/* Infosys - Weblogic to Jboss Migrations changes end here */

			EmptyPageWc main = new EmptyPageWc();
			main.setRandomString("edit was clicked");
			UserSession uSession = buildUserSession();

			P2lHandler p2l = new P2lHandler();
			TrainingReportGroupHandler grpHandler = new TrainingReportGroupHandler(); // Phase
																						// 2
																						// addition
			P2lTrack track = null;
			UserFilter uFilter = uSession.getUserFilter();
			track = p2l.getTrack(uFilter.getQuseryStrings().getTrack());
			if (track == null) {
				track = grpHandler.getTrack(uFilter.getQuseryStrings()
						.getTrack());
			}
			EditReportWc report = new EditReportWc();
			if ("name".equals(uFilter.getQuseryStrings().getType())) {
				if (track.getTrackId().startsWith("GROUP")) {
					String trackLabel = getRequest().getParameter(
							P2lTrack.FIELD_TRACK_LABEL);
					grpHandler.updateGroup(track.getTrackId(), trackLabel);
				} else {
					System.out.println("dosubmit");
					FormUtil.loadObject(getRequest(), track);
					MenuList menu = uSession.getMenuList();
					menu.setLabel(track.getTrackLabel());
					p2l.updateTrack(track);
					p2l.updateTrainingReports(track);
				}

			}
			if ("addPie".equals(uFilter.getQuseryStrings().getType())) {
				System.out.println("do add");
				P2lTrackPhase phase = new P2lTrackPhase();
				FormUtil.loadObject(getRequest(), phase);

				if (!Util.isEmpty(phase.getPhaseNumber())) {
					List tmp = DBUtil.executeSql(
							"select * from p2l_track_phase where track_id='"
									+ track.getTrackId()
									+ "' and upper(PHASE_NUMBER) = '"
									+ phase.getPhaseNumber().toUpperCase()
									+ "' ", AppConst.APP_DATASOURCE);
					if (tmp != null && tmp.size() > 0
							&& !"EMPTY".equals(phase.getPhaseNumber())) {
						report.setErrorMsg("Pie name has to be unique in this group.");
					} else {
						p2l.insertPie(phase.getPhaseNumber(),
								track.getTrackId());
					}
				} else {
					report.setErrorMsg("Pie name cannot be empty.");
				}
				// p2l.updateTrainingReports(track);
			}
			System.out.println("Submit:"
					+ uFilter.getQuseryStrings().getSubmit());
			if ("phase".equals(uFilter.getQuseryStrings().getType())) {
				P2lTrackPhase phase = new P2lTrackPhase();
				FormUtil.loadObject(getRequest(), phase);
				if ("Save".equals(uFilter.getQuseryStrings().getSubmit())) {
					System.out.println("do save");
					try {
						if (!Util.isEmpty(phase.getSortorder())) {
							Integer.parseInt(phase.getSortorder().trim());
						}
						p2l.updateTrackPhase(phase);
					} catch (Exception e) {
						report.setErrorMsg("<br>Invalid sort:"
								+ phase.getSortorder());
					}

				} else {
					p2l.deleteTrackPhase(phase.getTrackPhaseId());
					System.out.println("do delete");
				}

			}
			if ("root".equals(uFilter.getQuseryStrings().getType())) {
				p2l.updateTrackPhase(uFilter.getQuseryStrings().getId(),
						"ROOT_ACTIVITY_ID", uFilter.getQuseryStrings()
								.getActivitypk());
				System.out.println("update root");
			}
			if ("alt".equals(uFilter.getQuseryStrings().getType())) {
				p2l.updateTrackPhase(uFilter.getQuseryStrings().getId(),
						"ALT_ACTIVITY_ID", uFilter.getQuseryStrings()
								.getActivitypk());
				System.out.println("update alt");
			}
			track = p2l.getTrack(uFilter.getQuseryStrings().getTrack());
			if (track == null) {
				track = grpHandler.getTrack(uFilter.getQuseryStrings()
						.getTrack());
			}

			System.out.println("track ==== --- ---  " + track.getTrackId());
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "_");

			report.setMenu(uSession.getMenuList());
			report.setTrack(track);
			page.setMain(report);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editReportLaunchMeeting()
	public String editReportLaunchMeeting() {
		try {
			/* Infosys - Weblogic to Jboss Migrations changes end here */

			EmptyPageWc main = new EmptyPageWc();
			main.setRandomString("edit was clicked");
			UserSession uSession = buildUserSession();

			LaunchMeetingHandler handler = new LaunchMeetingHandler();
			LaunchMeeting track = new LaunchMeeting();
			UserFilter uFilter = uSession.getUserFilter();
			track = handler.getTrack(uFilter.getQuseryStrings().getTrack());
			// track.setTrackId("TOVIAZ");

			EditReportLaunchMeetingWc report = new EditReportLaunchMeetingWc();
			if ("name".equals(uFilter.getQuseryStrings().getType())) {
				System.out.println("dosubmit");
				FormUtil.loadObject(getRequest(), track);
				MenuList menu = uSession.getMenuList();
				menu.setLabel(track.getTrackLabel());
				handler.updateTrack(track);
				handler.updateTrainingReports(track);
			}
			if ("addPie".equals(uFilter.getQuseryStrings().getType())) {
				System.out.println("do add");
				LaunchMeetingDetails phase = new LaunchMeetingDetails();
				FormUtil.loadObject(getRequest(), phase);

				if (!Util.isEmpty(phase.getPhaseNumber())) {
					List tmp = DBUtil.executeSql(
							"select * from launch_meeting_details where track_id='"
									+ track.getTrackId()
									+ "' and upper(PHASE_NUMBER) = '"
									+ phase.getPhaseNumber().toUpperCase()
									+ "' ", AppConst.APP_DATASOURCE);
					if (tmp != null && tmp.size() > 0
							&& !"EMPTY".equals(phase.getPhaseNumber())) {
						report.setErrorMsg("Pie name has to be unique in this group.");
					} else {
						handler.insertPie(phase.getPhaseNumber(),
								track.getTrackId());
					}
				} else {
					report.setErrorMsg("Pie name cannot be empty.");
				}
				// p2l.updateTrainingReports(track);
			}
			System.out.println("Submit:"
					+ uFilter.getQuseryStrings().getSubmit());
			if ("phase".equals(uFilter.getQuseryStrings().getType())) {
				LaunchMeetingDetails phase = new LaunchMeetingDetails();
				FormUtil.loadObject(getRequest(), phase);
				String process = "";
				if (getRequest().getParameter("process") != null) {
					process = getRequest().getParameter("process");
				}
				System.out.println("Value of button is  >>>>>>>>>>>>>>> "
						+ process);
				// if ("Save".equals(uFilter.getQuseryStrings().getSubmit() )) {
				if ("Save".equals(process)) {
					System.out.println("do save");
					try {
						if (!Util.isEmpty(phase.getSortorder())) {
							Integer.parseInt(phase.getSortorder().trim());
						}
						System.out
								.println("######################## Attendance here  "
										+ phase.getAttendance());
						handler.updateTrackPhase(phase);
					} catch (Exception e) {
						report.setErrorMsg("<br>Invalid sort:"
								+ phase.getSortorder());
					}

				} else if ("Delete".equals(process)) {
					handler.deleteTrackPhase(phase.getTrackPhaseId());
					System.out.println("do delete");
				} else {
					System.out.println("do save");
					try {
						if (!Util.isEmpty(phase.getSortorder())) {
							Integer.parseInt(phase.getSortorder().trim());
						}
						System.out
								.println("######################## Attendance here  "
										+ phase.getAttendance());
						handler.updateTrackPhase(phase);
					} catch (Exception e) {
						report.setErrorMsg("<br>Invalid sort:"
								+ phase.getSortorder());
					}

				}

			}
			if ("root".equals(uFilter.getQuseryStrings().getType())) {
				handler.updateTrackPhase(uFilter.getQuseryStrings().getId(),
						"ROOT_ACTIVITY_ID", uFilter.getQuseryStrings()
								.getActivitypk());
				System.out.println("update root");
			}
			if ("alt".equals(uFilter.getQuseryStrings().getType())) {
				handler.updateTrackPhase(uFilter.getQuseryStrings().getId(),
						"ALT_ACTIVITY_ID", uFilter.getQuseryStrings()
								.getActivitypk());
				System.out.println("update alt");
			}
			if ("alt1".equals(uFilter.getQuseryStrings().getType())) {
				handler.updateTrackPhase(uFilter.getQuseryStrings().getId(),
						"ALT_ACTIVITY_ID1", uFilter.getQuseryStrings()
								.getActivitypk());
				System.out.println("update alt1");
			}
			track = handler.getTrack(uFilter.getQuseryStrings().getTrack());
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					"reportselect");

			report.setMenu(uSession.getMenuList());
			report.setTrack(track);
			page.setMain(report);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editNodeLaunchMeeting()
	public String editNodeLaunchMeeting() {
		try {
			/* Infosys - Weblogic to Jboss Migrations changes end here */

			UserSession uSession = buildUserSession();

			BlankTemplateWpc page = new BlankTemplateWpc();
			// main.setRandomString("My string:" +
			// uSession.getUserFilter().getQuseryStrings().getId());
			LaunchMeetingHandler handler = new LaunchMeetingHandler();
			LaunchMeetingDetails phase = handler.getTrackPhaseById(uSession
					.getUserFilter().getQuseryStrings().getId());
			List result = new ArrayList();

			ActivitySearchLaunchMeetingWc main = new ActivitySearchLaunchMeetingWc();
			System.out.println("Type here is  ################  "
					+ uSession.getUserFilter().getQuseryStrings().getType());
			if ("root".equals(uSession.getUserFilter().getQuseryStrings()
					.getType())) {
				result = handler.getActivityTree(phase.getRootActivityId());
				main.setType("root");
			} else if ("alt".equals(uSession.getUserFilter().getQuseryStrings()
					.getType())) {
				result = handler.getActivityTree(phase.getAlttActivityId());
				main.setType("alt");
			} else {
				result = handler.getActivityTree(phase.getAlttActivityId1());
				main.setType("alt1");
			}
			FormUtil.loadObject(getRequest(), main);

			if (!Util.isEmpty(main.getActivityname())
					|| !Util.isEmpty(main.getCode())) {

				List searchresult = new ArrayList();

				if (!Util.isEmpty(main.getCode())) {
					System.out.println("Result fro here  " + main.getCode());
					searchresult = handler
							.getActivityTreeByCode(main.getCode());
				} else {
					System.out.println("Result fro here  else part  ");
					searchresult = handler.getActivityTreeByName(main
							.getActivityname().toUpperCase());
				}
				main.setSearchResults(searchresult);
			}
			main.setPhase(phase);
			main.setCurrent(result);
			page.setMain(main);

			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editMenu()
	public String editMenu() {
		try {
			/* Infosys - Weblogic to Jboss Migrations changes end here */
			callSecurePage();
			/*
			 * Infosys - Weblogic to Jboss Migrations changes start
			 * here*:Commented below code
			 */
			/*
			 * if ( getResponse().isCommitted() ) { return null; }
			 */
			/* Infosys - Weblogic to Jboss Migrations changes end here */
			UserSession uSession = buildUserSession();
			User user = uSession.getUser();

			if (!user.isSuperAdmin()) {
				return unauthorized();
			}

			// Start: added for TRT Phase 1 enhancement - Forecast filter
			// criteria-
			// removal of session attributes.
			System.out.println("Starting: Removal of session attributes");
			HttpSession session = getRequest().getSession();
			System.out.println("here before session");
			String[] valueNames = session.getValueNames();
			String ValueName = null;
			for (int i = 0; i < valueNames.length; i++) {
				ValueName = valueNames[i];
				System.out.println("ValueName in session is " + ValueName);
			}
			session.removeAttribute("trackID");
			session.removeAttribute("trackName");
			session.removeAttribute("ROLE_CD");
			session.removeAttribute("START_DATE");
			session.removeAttribute("END_DATE");
			session.removeAttribute("DURATION");
			session.removeAttribute("HIRE_OR_PROMOTION_DATE");
			session.removeAttribute("Completed_id");
			session.removeAttribute("NotCompleted_id");
			session.removeAttribute("Registered_id");
			session.removeAttribute("NotRegistered_id");
			session.removeAttribute("Completed");
			session.removeAttribute("NotCompleted");
			session.removeAttribute("Registered");
			session.removeAttribute("NotRegistered");
			System.out.println("Ending: Removal of session attributes");
			// Ends here

			String reportType = "";
			if (getRequest().getParameter("reportType") != null) {
				reportType = getRequest().getParameter("reportType");
				System.out.println("report type is" + reportType);
			}

			String editType = ""; // for

			if (getRequest().getParameter("editType") != null) {
				editType = getRequest().getParameter("editType");
				System.out.println("editType" + editType);
			}

			// Added for TRT major enhancement 3.6 F2(Management Summary Report)
			ManagementFilterHandler mtHandler = new ManagementFilterHandler();
			ForecastFilterHandler fcHandler = new ForecastFilterHandler();
			// end of addition

			TrainingReportGroupHandler grpHandler = new TrainingReportGroupHandler(); // Phase
																						// 2
																						// addition
			P2lHandler p2l = new P2lHandler();
			LaunchMeetingHandler handler = new LaunchMeetingHandler();
			// String menuName = getRequest().getParameter("name");
			String menuID = getRequest().getParameter("id");
			System.out.println(menuID);

			MenuList menuItem = trDb.getMenuItemByID(menuID);
			uSession.setMenuList(menuItem);
			System.out.println("found:" + menuItem.getLabel());
			String command = "";
			EditMenuWc wc = new EditMenuWc(user);

			if (getRequest().getParameter("command") != null) {
				if ("deleteMenu".equals(getRequest().getParameter("command"))) {
					command = "deleteMenu";
				} else if ("archive".equals(getRequest()
						.getParameter("command"))) {
					command = "archive";

				} else if ("copy".equals(getRequest().getParameter("command"))) {
					command = "copy";

				} else if ("move".equals(getRequest().getParameter("command"))) {
					command = "move";

				}

			}
			if (getRequest().getParameter("update") != null) {
				command = (String) getRequest().getParameter("update");
				System.out.println(command);
				System.out.println("check pie name");
			}
			if (getRequest().getParameter("add") != null) {
				command = (String) getRequest().getParameter("add");
				System.out.println("clicked add");
				P2lTrack track = new P2lTrack();
				FormUtil.loadObject(getRequest(), track);

				System.out.println(reportType + " report type");

				List tmp = DBUtil.executeSql(
						"select * from training_report where training_report_label = '"
								+ track.getTrackLabel()
								+ "' and delete_flag ='N'",
						AppConst.APP_DATASOURCE);
				if (tmp != null && tmp.size() > 0) {
					wc.setErrorMsg("Error in report name.  Must be unique.");
				} else {
					if (!reportType.equals("ForecastReport")
							&& !reportType.equals("LaunchMeeting")
							&& (!reportType.equals("ManagementSummary"))
							&& !reportType.equals("GapAnalysisReport")
							&& !reportType.equals("Group")) {
						int trackId = p2l.insertTrack(track.getTrackLabel()); // for
																				// regular
						System.out.println(track.getTrackLabel());
						track.setTrackId(trackId + "");
						p2l.insertTrainingReports(track, menuID);
					} else if (reportType.equals("LaunchMeeting")) {
						LaunchMeeting meeting = new LaunchMeeting();
						FormUtil.loadObject(getRequest(), meeting);
						String trackId = handler.insertTrack(track
								.getTrackLabel());
						System.out.println("Track Id #################"
								+ trackId);
						meeting.setTrackId(trackId + "");
						handler.insertTrainingReports(meeting, menuID);
					}

					// /////////START:MERGING////////////////

					else if (reportType.equals("GapAnalysisReport")) {
						System.out.println("track.getTrackLabel()=="
								+ track.getTrackLabel());
						boolean gapExists = false;
						P2lTrack report = new P2lTrack();
						String trackId = p2l.insertGapTrack(track
								.getTrackLabel());
						System.out.println("Track Id ################# "
								+ trackId);
						report.setTrackId(trackId);
						report.setTrackLabel(track.getTrackLabel());
						boolean bool = p2l.insertTrainingReportsForGap(report,
								menuID);
						System.out.println("bool is" + bool);

					}

					else if (reportType.equals("ForecastReport")) {
						String trackId = fcHandler.insertTrack(track
								.getTrackLabel());
						ForecastReport report = new ForecastReport();
						System.out.println("Track Id ################# "
								+ trackId);
						report.setTrackId(trackId);
						System.out.println("report.getTrackId(trackId)="
								+ report.getTrackId());
						report.setTracklabel(track.getTrackLabel());
						System.out.println("Track label "
								+ report.getTrackLabel());
						fcHandler.insertTrainingReports(report, menuID);

						// fcHandler.forecastSQL();

					} else if (reportType.equals("ManagementSummary")) {

						System.out.println("(track.getTrackLabel()="
								+ track.getTrackLabel());
						String trackId = mtHandler.insertTrack(track
								.getTrackLabel());

						System.out.println(track.getTrackLabel()
								+ " Inside mgt summary");
						ManagementSummaryReport report = new ManagementSummaryReport();
						System.out.println("Track Id ################# "
								+ trackId);
						report.setTrackId(trackId);
						// report.setTrack(track);
						report.setTrackLabel(track.getTrackLabel());

						mtHandler.insertTrainingReports(report, menuID);

					} else if (reportType.equals("Group")) {
						int trackId = p2l.insertTrack(track.getTrackLabel()); // for
																				// regular
						System.out.println("(track.getTrackLabel()="
								+ track.getTrackLabel());
						System.out.println(track.getTrackLabel()
								+ " Inside Training Group");
						// ManagementSummaryReport report = new
						// ManagementSummaryReport();
						System.out.println("Track Id ################# "
								+ trackId);

						System.out.println("GROUP_" + trackId);
						track.setTrackId("GROUP_" + trackId);
						grpHandler.insertTrainingReports(track, menuID);

					}
				}
				// /////////////END:MERGING/////////////////////
			}

			if (command != null && command.equals("Save")) {

				String[] removefromgroup = getRequest().getParameterValues(
						"removefromgroup");
				String[] parent_group_id = getRequest().getParameterValues(
						"parent_group_id");
				String[] child_group_id = getRequest().getParameterValues(
						"child_group_id");

				if (removefromgroup != null) {
					System.out.println("remove from group"
							+ removefromgroup.length);
					for (int i = 0; i < removefromgroup.length; i++) {
						System.out.println("report id" + removefromgroup[i]);
						if (removefromgroup[i].trim().length() > 0)
							trDb.removeFromGroup(menuID, removefromgroup[i]);
					}
				}
				if (parent_group_id != null && child_group_id != null) {
					System.out.println("Parent groups | Child groups"
							+ parent_group_id.length);
					if (parent_group_id.length == child_group_id.length) {
						for (int i = 0; i < parent_group_id.length; i++) {
							System.out.println(parent_group_id[i] + " | "
									+ child_group_id[i]);
							if (parent_group_id[i].trim().length() > 0
									&& child_group_id[i].trim().length() > 0) {
								trDb.addToGroup(parent_group_id[i],
										child_group_id[i]);
							}
						}
					}
				}

				Enumeration enumName = getRequest().getParameterNames();
				String menuId = "";
				String sortNum = "";
				boolean saveFlag = true;
				// check if sort # are valid
				while (enumName.hasMoreElements()) {
					String name = (String) enumName.nextElement();
					if (name.startsWith("sort")) {
						String value = getRequest().getParameter(name);
						try {
							if (!Util.isEmpty(value))
								Integer.parseInt(value.trim());
						} catch (Exception e) {
							wc.setErrorMsg(wc.getErrorMsg()
									+ "<br>Invalid Sort value:" + value);
							saveFlag = false;
						}
					}
				}

				enumName = getRequest().getParameterNames();
				if (saveFlag) {
					int sortOrder = 0;
					String[] sorts = getRequest().getParameterValues("sort");
					if (sorts != null)
						System.out.println("sort elements " + sorts.length);
					while (enumName.hasMoreElements()) {
						String name = (String) enumName.nextElement();

						// if( name.startsWith("sort") ){
						// String value = getRequest().getParameter(name);
						if (sorts != null && sortOrder < sorts.length
								&& sorts[sortOrder] != null
								&& sorts[sortOrder].trim().length() > 0) {
							trDb.updateSortMenuByID(sorts[sortOrder],
									new Integer(sortOrder + 1).toString());
							System.out
									.println("------ --- ---- ---- ----- --- sorting "
											+ sorts[sortOrder]
											+ " "
											+ (sortOrder + 1));
						}
						// trDb.updateSortMenuByID(name.substring(5),value.trim());
						// }
						if (name.startsWith("access_")) {
							trDb.updateAccessMenuByID(name.substring(7),
									getRequest().getParameter(name));
						}
						sortOrder++;
					}
				}
				wc.setErrorMsg("<B>Save Successful.</B>");
			} else if (command != null && command.equals("deleteMenu")) {
				System.out.println("\n\n\nClick delete\n\n\n");
				/*
				 * String trackId = getRequest().getParameter("delIDTrack");
				 * if(!trackId.startsWith("LAUNCH")){
				 * trDb.deleteMenuByID(getRequest().getParameter("delID"));
				 * trDb.
				 * deleteReportByID(getRequest().getParameter("delIDTrack"));
				 * trDb
				 * .deleteTrackPhaseByID(getRequest().getParameter("delIDTrack"
				 * )); } else {
				 * trDb.deleteMenuByID(getRequest().getParameter("delID"));
				 * trDb.
				 * deleteReportByIDForLaunchMeeting(getRequest().getParameter(
				 * "delIDTrack"));
				 * trDb.deleteTrackPhaseByIDForLaunchMeeting(getRequest
				 * ().getParameter("delIDTrack"));
				 * trDb.deleteTrackActivityMappingForLaunchMeeting
				 * (getRequest().getParameter("delIDTrack"));
				 * trDb.deleteLaunchMeetingAttendance
				 * (getRequest().getParameter("delIDTrack")); }
				 */
				String trackId = getRequest().getParameter("delIDTrack").trim();
				if (trackId.startsWith("MANAGEMENT")) {
					System.out.println("true");
				}
				System.out.println(trackId);
				if (!trackId.startsWith("LAUNCH")
						&& !trackId.startsWith("MANAGEMENT")
						&& !trackId.startsWith("FORECAST")) {
					System.out.println("Inside Launch Deletion");
					trDb.deleteMenuByID(getRequest().getParameter("delID"));
					// trDb.deleteReportByID(getRequest().getParameter("delIDTrack"));
					// trDb.deleteTrackPhaseByID(getRequest().getParameter("delIDTrack"));
				} // Added for Management report
				else if (trackId.startsWith("MANAGEMENT")) {
					System.out.println("Inside Management Deletion");
					trDb.deleteMenuByID(getRequest().getParameter("delID"));
					/*
					 * //trDb.deleteReportByIDForManagementReport(getRequest().
					 * getParameter("delIDTrack")); try{ //
					 * trDb.deleteReportFromManagementCodeDesc
					 * (getRequest().getParameter("delIDTrack")); }
					 * catch(Exception e){ System.out.println(e); } //
					 * trDb.deleteReportFromManagementFilterCriteria
					 * (getRequest().getParameter("delIDTrack"));
					 */
				} else if (trackId.startsWith("FORECAST")) {
					System.out.println("Inside Forecast Deletion");
					trDb.deleteMenuByID(getRequest().getParameter("delID"));
					/*
					 * trDb.deleteReportByIDForForecastReport(getRequest().
					 * getParameter ("delIDTrack"));
					 * trDb.deleteReportFromForecastFilterCriteria(getRequest
					 * ().getParameter("delIDTrack"));
					 */
				} else if (trackId.startsWith("LAUNCH")) {
					trDb.deleteMenuByID(getRequest().getParameter("delID"));
					/*
					 * trDb.deleteReportByIDForLaunchMeeting(getRequest().
					 * getParameter ("delIDTrack"));
					 * trDb.deleteTrackPhaseByIDForLaunchMeeting(getRequest
					 * ().getParameter("delIDTrack"));
					 * trDb.deleteTrackActivityMappingForLaunchMeeting
					 * (getRequest().getParameter("delIDTrack"));
					 * trDb.deleteLaunchMeetingAttendance
					 * (getRequest().getParameter("delIDTrack"));
					 */
				}
				wc.setErrorMsg("<B>Delete successful.</B>");
			} else if (command != null && command.equals("archive")) {
				if (getRequest().getParameter("trackID") != null) {
					String trackID = getRequest().getParameter("trackID");
					System.out.println("trackid for archival " + trackID);
					trDb.removeFromGroup(menuID, trackID);
					trDb.updateActiveStatusMenuByID(trackID, "0");
					trDb.updateActiveStatusMenuByParentID(trackID, "0");
				} else {
					System.out.println("Track ID is empty");
				}
				wc.setErrorMsg("Archived Successfully.");
			} else if (command != null && command.equals("copy")) {
				String idChild = getRequest().getParameter("idChild");
				String idParent = getRequest().getParameter("idParent");
				String newTrackId = "";
				if (idChild != null && idChild.trim().length() > 0
						&& idParent != null && idParent.trim().length() > 0) {
					MoveCopyHandler mcHandler = new MoveCopyHandler();
					mcHandler.copyReport(idChild, idParent);
				}

				System.out.println("edit menu copy action");
				wc.setErrorMsg("<B>Copy Successful.</B>");

			} else if (command != null && command.equals("move")) {
				String idChild = getRequest().getParameter("idChild");
				String idParent = getRequest().getParameter("idParent");

				if (idChild != null && idChild.trim().length() > 0
						&& idParent != null && idParent.trim().length() > 0) {
					trDb.addToGroup(idParent, idChild);
					System.out.println(idChild + " moved to " + idParent);
				}
				wc.setErrorMsg("<B>Move Successful.</B>");

			} else if (command != null && command.equals("Add")) {
				System.out.println("\n\n\nClick Add\n\n\n");
			}

			// Render
			MainTemplateWpc page = new MainTemplateWpc(user, "reportselect");
			wc.setMenuName(reportType);
			// System.out.println("reportType====="+reportType);
			wc.setMenuName(menuItem.getLabel());
			wc.setRootID(menuID);
			renderEditMenu(wc, menuID);
			page.setMain(wc);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	private void renderEditMenu(EditMenuWc wc, String menuID) {
		Vector list = new Vector();
		MenuList[] menuList = trDb.getAllMenuByID(menuID);
		for (int i = 0; i < menuList.length; i++) {
			list.addElement(menuList[i]);
		}
		wc.setMenu(list);
	}

	private void callSecurePage() {
		SuperWebPageComponents tpage = new BlankTemplateWpc();
		tpage.setLoginRequired(true);
		IAMUserControl upControl = new IAMUserControl();
		upControl.checkAuth(getRequest(), getResponse(), tpage);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward unauthorized() {
	public String unauthorized() {
		try {
			/* Infosys - Weblogic to Jboss Migrations changes end here */
			if (getResponse().isCommitted()) {
				return null;
			}
			MainTemplateWpc page = new MainTemplateWpc(null, "unauthorized");
			page.setMain(new UnauthorizedWc());
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	private UserSession buildUserSession() {
		UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());
		TerritoryFilterForm filterForm = uSession.getUserFilterForm();

		// process query stings
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(), qStrings);
		FormUtil.loadObject(getRequest(), qStrings.getSortBy());

		// This will give you the full query string if needed
		qStrings.setFullQueryString(getRequest().getQueryString());

		// Setup user filter obejct
		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setEmployeeId(qStrings.getEmplid());
		uFilter.setAdmin(uSession.getUser().isAdmin());
		uFilter.setClusterCode(uSession.getUser().getCluster());
		uFilter.setFilterForm(filterForm);
		uFilter.setQueryStrings(qStrings);
		uFilter.setIsSpecialRoleUser(uSession.getUser().isSpecialRole());
		uFilter.setEmployeeIdForSplRole(uSession.getUser().getEmplIdForSpRole());
		// System.out.println("User=="+uSession.getUser().getEmplid());
		// System.out.println("Emp ID in filter@#$@$="+uSession.getUserFilter().getEmployeeId());
		// System.out.println();

		return uSession;
	}

	// Start: Modified for TRT 3.6 enhancement - F 7.6 -(Gap analysis - product
	// - course mapping)
	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editGapAnalysisReport()
	public String editGapAnalysisReport()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {
			UserSession uSession = buildUserSession();
			GAProduct[] products = null;
			GAProdCourse[] pcMap = null;
			String code = getRequest().getParameter("code");
			String selectedProduct = getRequest().getParameter("product");
			String message = "";

			// Added by Amit-Start
			String completionCheck = getRequest().getParameter("compCheck");
			String registrationCheck = getRequest().getParameter("regCheck");
			// Added by Amit End

			if (null == code)
				message = "Please search for the course code to add it.";

			UserFilter uFilter = uSession.getUserFilter();
			System.out.println("code: " + code + " selectedProduct: "
					+ selectedProduct);
			/* Get details from the database */
			// products = trDb.getGAProducts(); //Commented by Amit
			// pcMap = trDb.getProdCourseMapping(); //Commented by Amit
			// //retrieves
			// the codes as well as the product names

			products = trDb.getGAPproductsFromSandBox(); // added by Amit to
															// fetch
															// product names
															// from
															// sand box
			pcMap = trDb.getProdCourseMappingNew(); // Added by Amit
			// System.out.println("products" +products.length + " pcMap" +
			// pcMap.length);

			/* form submits/processing logic */
			if ("Save".equals(uFilter.getQuseryStrings().getType())) {
				if ((null != code) && (null != selectedProduct)
						&& (code.trim().length() > 0)) {
					if ((completionCheck != null && registrationCheck != null)) {// &&
																					// (!completionCheck.equalsIgnoreCase("N")
																					// &&
																					// !registrationCheck.equalsIgnoreCase("N"))){
																					// //added
																					// by
																					// AMit
						boolean isPresent = false;
						if (null != pcMap) {
							for (int i = 0; i < pcMap.length; i++) {
								if (pcMap[i].getProductCode().equalsIgnoreCase(
										selectedProduct)
										&& pcMap[i].getCourseCode()
												.equalsIgnoreCase(code)
										&& pcMap[i].getCompletion()
												.equalsIgnoreCase(
														completionCheck)
										&& pcMap[i].getRegistration()
												.equalsIgnoreCase(
														registrationCheck)) {
									isPresent = true;
								}
							}
						}
						System.out.println("isPresent: " + isPresent);
						if (!isPresent) {
							// trDb.saveProdCourseMapping(selectedProduct,code);
							// //Commented by Amit
							trDb.removeGapRptProductCourseMapping(
									selectedProduct, code); // Added by Amit
															// //deletes the
															// codes as
															// well as the
															// product names
							trDb.saveGapRptProdCourseMapping(selectedProduct,
									code, completionCheck, registrationCheck); // Added
																				// by
																				// Amit
							message = "Mapping successfully saved.";
						} else
							message = "<font color=red>Mapping already present.<//font>";
					} else {
						message = "<font color=red>Please select value for either Completion check or Registration check.<//font>";
					}
				} else
					message = "<font color=red>Please select a course code.<//font>";
			} else if ("Remove".equals(uFilter.getQuseryStrings().getType())) {
				if ((null != code) && (null != selectedProduct)
						&& (code.trim().length() > 0)) {
					// trDb.removeProductCourseMapping(selectedProduct,code);
					// //Commented by Amit
					trDb.removeGapRptProductCourseMapping(selectedProduct, code); // Added
																					// by
																					// Amit
																					// //deletes
																					// the
																					// codes
																					// as
																					// well
																					// as
																					// the
																					// product
																					// names
					message = "Mapping Successfully removed.";
					code = null;
					selectedProduct = null;
				}
			}

			/* Get details from the database */
			// products = trDb.getGAProducts(); //Commented by Amit
			// pcMap = trDb.getProdCourseMapping(); //Commented by Amit
			// //retrieves
			// the codes as well as the product names

			products = trDb.getGAPproductsFromSandBox(); // added by Amit to
															// fetch
															// product names
															// from
															// sand box
			pcMap = trDb.getProdCourseMappingNew(); // Added by Amit

			/* UI structuring */
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					"reportselect");
			GapAnalysisAdminWc wc = new GapAnalysisAdminWc();
			if (null != code)
				wc.setCourseCode(code);
			if (null != selectedProduct)
				wc.setSelectedProduct(selectedProduct);
			wc.setProducts(products);
			wc.setPcMap(pcMap);
			wc.setMessage(message);

			page.setMain(wc);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// Start: Modified for TRT 3.6 enhancement - F 5 (Training Path
	// configuration)
	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward trainingPath()
	public String trainingPath() {
		try {
			/* Infosys - Weblogic to Jboss Migrations changes end here */
			UserSession uSession = buildUserSession();
			// ManagementFilterHandler mHandler = new ManagementFilterHandler();
			TrainingPathHandler tHandler = new TrainingPathHandler();
			// ManagementSummaryReport track = new ManagementSummaryReport ();
			TrainingPathConfigBean track = new TrainingPathConfigBean();

			List roleCodes = tHandler.getRoleCodes();
			track.setAllRoleCodes(roleCodes);

			List businessUnits = tHandler.getBusinessUnit();
			track.setAllBusinessUnit(businessUnits);

			List salesOrg = tHandler.getSalesOrg();
			track.setAllSalesOrg(salesOrg);

			// For Sales Org
			ArrayList allSalesOrg = new ArrayList();
			Iterator itr = salesOrg.iterator();

			LabelValueBean labelValueBean;
			FormUtil.loadObject(getRequest(), track);

			while (itr.hasNext()) {
				Map mFilter = (Map) itr.next();
				String strL = (String) mFilter.get("SALES_GROUP");
				String strV = (String) mFilter.get("GROUP_CD");

				labelValueBean = new LabelValueBean(strL, strV);
				track.setAllSalesOrgList(labelValueBean);
			}

			// For BU
			ArrayList allBusinessUnit = new ArrayList();
			Iterator itrBu = businessUnits.iterator();

			while (itrBu.hasNext()) {
				Map mFilter = (Map) itrBu.next();
				String strL = (String) mFilter.get("BU");
				String strV = (String) mFilter.get("BU");
				labelValueBean = new LabelValueBean(strL, strV);
				track.setAllBusinessUnitList(labelValueBean);
			}

			// For Role Codes
			ArrayList allRoleCodes = new ArrayList();
			Iterator itrRc = roleCodes.iterator();

			while (itrRc.hasNext()) {
				Map mFilter = (Map) itrRc.next();
				String strL = (String) mFilter.get("ROLE_DESC");
				String strV = (String) mFilter.get("ROLE_CD");

				labelValueBean = new LabelValueBean(strL, strV);
				track.setAllRoleList(labelValueBean);
			}

			/* UI structuring */
			String org = getRequest().getParameter("trsOrg");
			String bu = getRequest().getParameter("trBu");
			String roles = getRequest().getParameter("trRoles");
			String courses = getRequest().getParameter("trCourses");
			String activityNames = getRequest().getParameter("trCourseNames");
			String courseAlias = getRequest().getParameter("trAlias");
			String courseAliasCode = getRequest().getParameter("trAliasCode");

			System.out.println("courseAlias " + courseAlias
					+ "courseAliasCode " + courseAliasCode);

			HttpSession session = getRequest().getSession();
			boolean editFlag = false;
			boolean secondInsert = false;
			String id = getRequest().getParameter("trackId");
			System.out.println("id = " + id);
			int trackId = 0;
			try {
				// cjk for null
				trackId = Integer.parseInt(id);
			} catch (NumberFormatException e) {
				System.out.println("exception==" + e);
			}

			TrainingPathAdminWc wc = new TrainingPathAdminWc();

			if ((getRequest().getParameter("savenewpath") != null)
					&& (getRequest().getParameter("savenewpath").trim()
							.length() != 0)
					&& (getRequest().getParameter("trackId").trim().length() != 0)
					&& (getRequest().getParameter("trackId").equals("null") || getRequest()
							.getParameter("trackId").equals("0"))) {
				List result = tHandler.validateTrainingPaths(org, bu, roles);
				System.out.println("result size==" + result.size() + "result"
						+ result);
				if (result == null || result.size() == 0 || result != null) {
					// System.out.println("first insert.");

					trackId = trDb.getConfigId();
					String[] selectedValues = org.split(",");
					if (selectedValues.length > 0) {

						tHandler.insertTrainingPathConfig(trackId,
								"sales_group", org, "group_cd");
						System.out.println("after sales org insertion");
					}
					// }
					// if(getRequest().getParameter("savenewpath")!=null){

					selectedValues = bu.split(",");
					if (selectedValues.length > 0) {

						tHandler.insertTrainingPathConfig(trackId, "bu", bu,
								"bu");
						System.out.println("after bu insertion");
					}
					// }
					// if(getRequest().getParameter("savenewpath")!=null){

					selectedValues = roles.split(",");
					if (selectedValues.length > 0) {

						tHandler.insertTrainingPathConfig(trackId, "role_desc",
								roles, "role_cd");
						System.out.println("after role insertion");
					}
					// }
					// if(getRequest().getParameter("savenewpath")!=null){
					int course_order = 0;

					selectedValues = courses.split(",");
					if (selectedValues.length > 0) {
						tHandler.insertTrainingPathConfig(trackId,
								"activityfk", courses, "activity_pk");
						System.out.println("after Course insertion");
					}

					selectedValues = courseAlias.split(",");
					String[] selectedAliasCode = courses.split(",");
					if (selectedValues.length > 0
							&& selectedAliasCode.length > 0) {
						tHandler.insertTrainingPathConfig(trackId, "alias",
								courses, courseAlias);
						System.out.println("after CourseAlias insertion");
					}

					wc.setErrorMsg("Training Path Configured successfully.");
				}/*
				 * else{
				 * 
				 * System.out.println("Cannot insert"); String[]
				 * selectedSalesOrg = org.split(","); String[] selectedRoles =
				 * roles.split(","); String[] selectedBu = bu.split(",");
				 * String[] selectedCourses = courses.split(","); String[]
				 * selectedAlias = courseAlias.split(","); String[]
				 * selectedAliasCode = courseAliasCode.split(","); String[]
				 * selectedCourseNames = activityNames.split(",");
				 * 
				 * ArrayList salesList = new ArrayList(); ArrayList buList = new
				 * ArrayList(); ArrayList roleList = new ArrayList(); ArrayList
				 * courseList = new ArrayList(); ArrayList aliasList = new
				 * ArrayList(); ArrayList aliasCodeList = new ArrayList();
				 * ArrayList courseNamesList = new ArrayList();
				 * 
				 * 
				 * for(int i=0;i<selectedSalesOrg.length;i++)
				 * salesList.add(selectedSalesOrg[i]); for(int
				 * i=0;i<selectedBu.length;i++) buList.add(selectedBu[i]);
				 * for(int i=0;i<selectedRoles.length;i++)
				 * roleList.add(selectedRoles[i]); for(int
				 * i=0;i<selectedCourses.length;i++)
				 * courseList.add(selectedCourses[i]); for(int
				 * i=0;i<selectedCourses.length;i++)
				 * courseNamesList.add(selectedCourseNames[i]); for(int
				 * i=0;i<selectedAlias.length;i++)
				 * aliasList.add(selectedAlias[i]); for(int
				 * i=0;i<selectedAliasCode.length;i++)
				 * aliasCodeList.add(selectedAliasCode[i]);
				 * 
				 * if(courseList !=null){ HashMap dataMap=new HashMap(); HashMap
				 * courseCodeMap=new HashMap(); HashMap courseAliasMap=new
				 * HashMap(); if(courseNamesList.size()>0){ for(int i = 0;
				 * i<courseNamesList.size(); i++){
				 * courseCodeMap.put(courseList.get(i
				 * ).toString(),(String)courseNamesList.get(i));
				 * courseAliasMap.put(courseList
				 * .get(i).toString(),(String)aliasList.get(i)); }
				 * wc.setCourseCodeMap(courseCodeMap);
				 * wc.setCourseAliasMap(courseAliasMap); } }
				 * System.out.println("courseList"+courseList);
				 * System.out.println("selectedAlias.length "+
				 * selectedAlias.length); secondInsert = true;
				 * wc.setSalesList(salesList); wc.setRoleList(roleList);
				 * wc.setBuList(buList); wc.setCourseList(courseList);
				 * wc.setCourseAliasList(aliasList);
				 * wc.setCourseAliasCodeList(aliasCodeList);
				 * 
				 * wc.setErrorMsg(
				 * "The Training Path for the selected Role Code, BU, Sales Organization has already been configured."
				 * ); }
				 */

			} else if ((getRequest().getParameter("savenewpath") != null)
					&& (getRequest().getParameter("savenewpath").trim()
							.length() != 0)
					&& (getRequest().getParameter("trackId") != null)
					&& (getRequest().getParameter("trackId").trim().length() != 0)
					&& trackId != '0') {
				System.out.println("Inside update");

				id = getRequest().getParameter("trackId");
				// System.out.println("edit=="+getRequest().getParameter("edit"));

				try {
					trackId = Integer.parseInt(id);
					System.out.println("trackId in update query==" + id);
				} catch (NumberFormatException e) {
					System.out.println("exception==" + e);
				}
				// deleting a configuration

				// tHandler.updateTrainingPathConfig(trackId);
				List result = tHandler.validateEditTrainingPaths(org, bu,
						roles, trackId);
				// System.out.println("reult size=="+result.size() +
				// "result"+result);
				if (result == null || result.size() == 0 || result != null) {
					tHandler.deleteConfiguration(id);
					String[] selectedValues = org.split(",");
					if (selectedValues.length > 0) {

						tHandler.insertTrainingPathConfig(trackId,
								"sales_group", org, "group_cd");
						System.out.println("after sales org insertion");
					}
					// }
					// if(getRequest().getParameter("savenewpath")!=null){

					selectedValues = bu.split(",");
					if (selectedValues.length > 0) {

						tHandler.insertTrainingPathConfig(trackId, "bu", bu,
								"bu");
						System.out.println("after bu insertion");
					}
					// }
					// if(getRequest().getParameter("savenewpath")!=null){

					selectedValues = roles.split(",");
					if (selectedValues.length > 0) {

						tHandler.insertTrainingPathConfig(trackId, "role_desc",
								roles, "role_cd");
						System.out.println("after role insertion");
					}
					// }
					// if(getRequest().getParameter("savenewpath")!=null){
					int course_order = 0;

					selectedValues = courses.split(",");
					if (selectedValues.length > 0) {

						tHandler.insertTrainingPathConfig(trackId,
								"activityfk", courses, "activity_pk");
						System.out.println("after course insertion");
					}

					selectedValues = courseAlias.split(",");
					String[] selectedAliasCode = courses.split(",");
					if (selectedValues.length > 0
							&& selectedAliasCode.length > 0) {

						tHandler.insertTrainingPathConfig(trackId, "alias",
								courses, courseAlias);
						System.out.println("after course alias insertion");
					}
					wc.setTrackId(trackId);
					wc.setErrorMsg("Training Path edited successfully.");
				}/*
				 * else{
				 * 
				 * System.out.println("Cannot insert"); String[]
				 * selectedSalesOrg = org.split(","); String[] selectedRoles =
				 * roles.split(","); String[] selectedBu = bu.split(",");
				 * String[] selectedCourses = courses.split(","); String[]
				 * selectedCourseNames = activityNames.split(","); String[]
				 * selectedAlias = courseAlias.split(","); String[]
				 * selectedAliasCode = courseAliasCode.split(",");
				 * 
				 * ArrayList salesList = new ArrayList(); ArrayList buList = new
				 * ArrayList(); ArrayList roleList = new ArrayList(); ArrayList
				 * courseList = new ArrayList(); ArrayList courseNamesList = new
				 * ArrayList(); ArrayList aliasList = new ArrayList(); ArrayList
				 * aliasCodeList = new ArrayList();
				 * 
				 * for(int i=0;i<selectedSalesOrg.length;i++)
				 * salesList.add(selectedSalesOrg[i]); for(int
				 * i=0;i<selectedBu.length;i++) buList.add(selectedBu[i]);
				 * for(int i=0;i<selectedRoles.length;i++)
				 * roleList.add(selectedRoles[i]); for(int
				 * i=0;i<selectedCourses.length;i++)
				 * courseList.add(selectedCourses[i]); for(int
				 * i=0;i<selectedAlias.length;i++)
				 * aliasList.add(selectedAlias[i]); for(int
				 * i=0;i<selectedAliasCode.length;i++)
				 * aliasCodeList.add(selectedAliasCode[i]);
				 * 
				 * 
				 * if(courseList !=null){ HashMap dataMap=new HashMap(); HashMap
				 * courseCodeMap=new HashMap(); HashMap courseAliasMap=new
				 * HashMap(); if(courseNamesList.size()>0){ for(int i = 0;
				 * i<courseNamesList.size(); i++){
				 * courseCodeMap.put(courseList.get(i
				 * ).toString(),(String)courseNamesList.get(i));
				 * courseAliasMap.put(courseList
				 * .get(i).toString(),(String)aliasList.get(i)); }
				 * wc.setCourseCodeMap(courseCodeMap);
				 * wc.setCourseAliasMap(courseAliasMap); } }
				 * System.out.println("courseList"+courseList); secondInsert =
				 * true; wc.setSalesList(salesList); wc.setRoleList(roleList);
				 * wc.setBuList(buList); wc.setCourseList(courseList);
				 * wc.setCourseAliasList(aliasList);
				 * wc.setCourseAliasCodeList(aliasCodeList);
				 * wc.setTrackId(trackId); wc.setErrorMsg(
				 * "The Training Path for the selected Role Code, BU, Sales Organization has already been configured."
				 * ); }
				 */
			}

			if (getRequest().getParameter("delt") != null
					&& getRequest().getParameter("delt").trim().length() != 0) {
				System.out.println("deleting");
				id = getRequest().getParameter("delt");
				tHandler.deleteConfiguration(id);
				wc.setErrorMsg("Training Path deleted succesfully.");
			}

			Iterator it;
			// TrainingPathAdminWc wc = new TrainingPathAdminWc();

			if (getRequest().getParameter("edit") != null
					&& getRequest().getParameter("edit").trim().length() != 0) {
				System.out.println("editing");

				id = getRequest().getParameter("edit");
				editFlag = true;

				try {
					trackId = Integer.parseInt(id);
					wc.setTrackId(trackId);
					System.out.println("trackId in edit" + wc.getTrackId());
				} catch (NumberFormatException e) {
					System.out.println("exception==" + e);
				}
				List editedEntry = tHandler
						.getSelectedTrainingConfiguraton(trackId);
				it = editedEntry.iterator();

			} else {
				List listOfEntry = tHandler
						.getSelectedTrainingConfiguraton(trackId);
				it = listOfEntry.iterator();
			}

			ArrayList salesList = new ArrayList();
			ArrayList buList = new ArrayList();
			ArrayList roleList = new ArrayList();
			ArrayList courseList = new ArrayList();
			ArrayList courseNames = new ArrayList();
			ArrayList courseAliasList = new ArrayList();
			ArrayList courseAliasNames = new ArrayList();

			while (it.hasNext()) {
				Map mFilter = (Map) it.next();

				String type = (String) mFilter.get("FILTER_TYPE");
				String code = (String) mFilter.get("CODE");
				String desc = (String) mFilter.get("DESCRIPTION");

				if (type.equals("sales_group")) {
					salesList.add(code);
				}
				if (type.equals("bu")) {
					buList.add(code);
				}
				if (type.equals("role_desc")) {
					roleList.add(code);
				}
				if (type.equals("activityfk")) {
					courseList.add(code);
					courseNames.add(desc);
				}
				if (type.equals("alias")) {
					System.out.println("Got alias values. code :" + code
							+ "desc" + desc);
					courseAliasList.add(code);
					courseAliasNames.add(desc);
				}

				if (courseList != null) {
					HashMap dataMap = new HashMap();
					HashMap courseCodeMap = new HashMap();
					if (courseNames.size() > 0) {
						for (int i = 0; i < courseNames.size(); i++) {
							courseCodeMap.put(courseList.get(i).toString(),
									(String) courseNames.get(i));
						}
						wc.setCourseCodeMap(courseCodeMap);
					}
				}

				if (courseAliasList != null && courseAliasList.size() > 0) {
					HashMap dataMap = new HashMap();
					HashMap courseAliasMap = new HashMap();
					if (courseAliasNames.size() > 0) {
						for (int i = 0; i < courseAliasNames.size(); i++) {
							courseAliasMap.put(courseAliasList.get(i)
									.toString(), (String) courseAliasNames
									.get(i));
						}
						wc.setCourseAliasMap(courseAliasMap);
					}
				} else {
					System.out.println("Alias is null ");
					HashMap dataMap = new HashMap();
					HashMap courseCodeMap = new HashMap();
					if (courseNames.size() > 0) {
						for (int i = 0; i < courseNames.size(); i++) {
							courseCodeMap.put(courseList.get(i).toString(),
									(String) courseNames.get(i));
						}
						wc.setCourseAliasMap(courseCodeMap);
					}
				}
			}
			if (!secondInsert) {
				wc.setSalesList(salesList);
				wc.setBuList(buList);
				wc.setRoleList(roleList);
				wc.setCourseList(courseList);
			}

			TrainingPathConfigBean tPathConfig = null;
			List TrainingPathConfigurations = new ArrayList();

			List configList = new ArrayList();

			List result = tHandler.getAllTrainingPaths();

			Iterator iterator = result.iterator();
			String configid = "";
			int countPath = 0;
			while (iterator.hasNext()) {
				Map mFilter = (Map) iterator.next();
				String cfgId = mFilter.get("CONFIG_ID").toString();
				String type = (String) mFilter.get("FILTER_TYPE");
				String desc = (String) mFilter.get("DESCRIPTION");
				String code = (String) mFilter.get("CODE");

				if (!cfgId.equals(configid)) {
					if (tPathConfig != null) {
						countPath++;
						TrainingPathConfigurations.add(tPathConfig);
					} else {
						System.out
								.println("New path" + countPath + " " + cfgId);
					}
					tPathConfig = new TrainingPathConfigBean();
					tPathConfig.setConfigId(cfgId);
					configid = cfgId;
					configList.add(cfgId);
				} else if (cfgId.equals(configid) && !iterator.hasNext()) {
					countPath++;
					TrainingPathConfigurations.add(tPathConfig);
				}
				if (type.equals("sales_group")) {
					tPathConfig.addSalesDesc(code, desc);
				}
				if (type.equals("bu")) {
					tPathConfig.addBuDesc(code, desc);
				}
				if (type.equals("role_desc")) {
					tPathConfig.addRoleDesc(code, desc);
				}
				if (type.equals("activityfk")) {
					tPathConfig.addCourseDesc(code, desc);
				}
				if (type.equals("alias")) {
					tPathConfig.addCourseAlias(code, desc);
				}

			}
			wc.setConfigIdList(configList);
			wc.setTrainingPathConfigurationList(TrainingPathConfigurations);
			System.out.println("TrainingPathConfigurations.size=="
					+ TrainingPathConfigurations.size());

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					"reportselect");
			wc.setTrackId(trackId);
			System.out.println("get Track Id==" + wc.getTrackId());
			wc.setMenu(uSession.getMenuList());
			wc.setTrack(track);
			page.setMain(wc);

			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward searchCourseTrainingPath()
	public String searchCourseTrainingPath()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {
			UserSession uSession = buildUserSession();

			BlankTemplateWpc page = new BlankTemplateWpc();
			P2lHandler p2l = new P2lHandler();

			System.out.println("debug1");
			System.out.println("Track Id in search and select courses.==="
					+ getRequest().getParameter("track"));
			String trackId = getRequest().getParameter("track");

			// searchCourseManagementWc main = new searchCourseManagementWc();
			searchCourseTrainingPathWc main = new searchCourseTrainingPathWc();
			main.setTrackId(trackId);

			FormUtil.loadObject(getRequest(), main);

			if (!Util.isEmpty(main.getActivityname())
					|| !Util.isEmpty(main.getCode())) {

				List searchresult = new ArrayList();

				if (!Util.isEmpty(main.getCode())) {
					searchresult = p2l.getActivityTreeByCode(main.getCode());
				} else {
					searchresult = p2l.getActivityTreeByName(main
							.getActivityname().toUpperCase());
				}
				main.setSearchResults(searchresult);
			}

			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			System.out.println("debug2");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// end
	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward searchAndSelectCourses()
	public String searchAndSelectCourses()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {

			UserSession uSession = buildUserSession();

			BlankTemplateWpc page = new BlankTemplateWpc();
			P2lHandler p2l = new P2lHandler();

			System.out.println("debug1");
			CourseSearchWc main = new CourseSearchWc();

			FormUtil.loadObject(getRequest(), main);

			if (!Util.isEmpty(main.getActivityname())
					|| !Util.isEmpty(main.getCode())) {

				List searchresult = new ArrayList();

				if (!Util.isEmpty(main.getCode())) {
					searchresult = p2l.getActivityTreeByCode(main.getCode());
				} else {
					searchresult = p2l.getActivityTreeByName(main
							.getActivityname().toUpperCase());
				}
				main.setSearchResults(searchresult);
			}

			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			System.out.println("debug2");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// End: Modified for TRT 3.6 enhancement - F 7.6 -(Gap analysis - product -
	// course mapping)

	// Start: modified for TRT major enhancement 3.6 F2 (Management Summary
	// report)
	/**
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editManagementFilterCriteria(){
	public String editManagementFilterCriteria() {
		try {
			/* Infosys - Weblogic to Jboss Migrations changes end here */

			EmptyPageWc main = new EmptyPageWc();
			main.setRandomString("edit was clicked");

			UserSession uSession = (UserSession) getRequest().getSession(true)
					.getAttribute(UserSession.ATTRIBUTE);

			// EditManagementFilterCriteriaWc wc = new
			// EditManagementFilterCriteriaWc();
			ManagementFilterHandler mHandler = new ManagementFilterHandler();
			ManagementSummaryReport track = new ManagementSummaryReport();

			UserFilter uFilter = uSession.getUserFilter();

			String trackId = getRequest().getParameter("trackID");
			// System.out.println("trackId=="+trackId);
			track.setTrackId(trackId);

			ManagementSummaryReport trackLabel = new ManagementSummaryReport();
			trackLabel = mHandler.getTrack(trackId);
			track.setTrackLabel(trackLabel.getTrackLabel());
			System.out.println("TrackLabel ===" + trackLabel.getTrackLabel());

			EditManagementFilterCriteriaWc report = new EditManagementFilterCriteriaWc();

			AppQueryStrings qString = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qString);
			String org = getRequest().getParameter("sOrg");
			String bu = getRequest().getParameter("bu");
			String roles = getRequest().getParameter("roles");
			String courses = getRequest().getParameter("courses");
			String gender = getRequest().getParameter("gender_hidden");
			String TStartdate = getRequest().getParameter("TStartDate");
			String TEnddate = getRequest().getParameter("TEndDate");
			String RStartdate = getRequest().getParameter("RStartDate");
			String REnddate = getRequest().getParameter("REndDate");
			String HStartdate = getRequest().getParameter("HStartDate");
			String HEnddate = getRequest().getParameter("HEndDate");
			String Groupby1 = getRequest().getParameter("Groupby1");
			String Groupby2 = getRequest().getParameter("Groupby2");
			String Groupby3 = getRequest().getParameter("Groupby3");
			String Groupby4 = getRequest().getParameter("Groupby4");
			String Groupby5 = getRequest().getParameter("Groupby5");
			String Groupby6 = getRequest().getParameter("Groupby6");
			System.out.println("group by 6==" + Groupby6);

			boolean firstTime = true;
			List tmp = DBUtil.executeSql(
					"select * from management_filter_criteria  where filter_id = '"
							+ trackId + "'", AppConst.APP_DATASOURCE);
			if (tmp != null && tmp.size() > 0) {
				firstTime = false;
				report.setFirstTime(firstTime);
			}

			System.out.println("Save" + getRequest().getParameter("save"));

			boolean insertFlag = false;
			if (getRequest().getParameter("save") != null) {
				insertFlag = true;
				if (TStartdate.equals("") || TEnddate.equals("")
						|| RStartdate.equals("") || REnddate.equals("")
						|| HEnddate.equals("") || HStartdate.equals("")) {
					insertFlag = false;
					report.setErrorMsg("Please enter the required fields");
				} else {

					Date hStartDate = new Date(HStartdate);
					Date hEndDate = new Date(HEnddate);

					if (hStartDate.compareTo(hEndDate) > 0) {
						insertFlag = false;
						report.setErrorMsg("Hire Start date cannot be greater than hire end date");
					}

					Date tStartDate = new Date(TStartdate);
					Date tEndDate = new Date(TEnddate);

					if (tStartDate.compareTo(tEndDate) > 0) {
						insertFlag = false;
						report.setErrorMsg("Training Completion Start date cannot be greater than Training Completion end date");
					}

					Date rStartDate = new Date(RStartdate);
					Date rEndDate = new Date(REnddate);

					if (rStartDate.compareTo(rEndDate) > 0) {
						insertFlag = false;
						report.setErrorMsg("Training Registration Start date cannot be greater than Training Registration end date");
					}
				}

				if (insertFlag) {
					// track.setTrackLabel(trackId);
					if (org == null || org.equals("null")) {
						org = "";
					}
					track.setSalesOrg(org);
					if (bu == null || bu.equals("null")) {
						bu = "";
					}
					track.setBusinessUnit(bu);
					if (roles == null || roles.equals("null")) {
						roles = "";
					}
					track.setRoleCode(roles);
					if (courses == null || courses.equals("null")) {
						courses = "";
					}
					track.setCourseCode(courses);
					if (gender == null || gender.equals("null")) {
						gender = "";
					}
					track.setGender(gender);
					track.setHireStartDate(HStartdate);
					track.setHireEndDate(HEnddate);
					track.setTrainingCompletionStartDate(TStartdate);
					track.setTrainingCompletionEnddate(TEnddate);
					track.setTrainingRegistrationStartDate(RStartdate);
					track.setTrainingRegistrationEndDate(REnddate);
					if (Groupby1 == null || Groupby1.equals("null")) {
						Groupby1 = "";
					}
					track.setGroupBy1(Groupby1);
					track.setGroupBy2(Groupby2);
					track.setGroupBy3(Groupby3);
					track.setGroupBy4(Groupby4);
					track.setGroupBy5(Groupby5);
					track.setGroupBy6(Groupby6);
				}

				if (firstTime && insertFlag) {

					mHandler.insertManagementFilterCriteria(track);
					System.out
							.println("after insertion into management filter");
				}
				if (insertFlag && !firstTime) {

					mHandler.updateManagementFilterCriteria(track);
					System.out.println("after updation into management filter");
				}

			}

			List roleCodes = mHandler.getRoleCodes();
			// System.out.println("roleCodes"+roleCodes.toString());
			track.setAllRoleCodes(roleCodes);

			List businessUnits = mHandler.getBusinessUnit();
			track.setAllBusinessUnit(businessUnits);

			// List courseCodes = mHandler.getCourseCodes();
			// track.setAllCourseCodes(courseCodes);

			List salesOrg = mHandler.getSalesOrg();
			track.setAllSalesOrg(salesOrg);

			List insertStmts = new ArrayList();

			// For Sales Org
			ArrayList allSalesOrg = new ArrayList();
			Iterator itr = salesOrg.iterator();

			LabelValueBean labelValueBean;
			FormUtil.loadObject(getRequest(), track);

			while (itr.hasNext()) {
				Map mFilter = (Map) itr.next();
				String strL = (String) mFilter.get("SALES_GROUP");
				String strV = (String) mFilter.get("GROUP_CD");

				labelValueBean = new LabelValueBean(strL, strV);
				track.setAllSalesOrgList(labelValueBean);
			}

			if (getRequest().getParameter("save") != null) {
				System.out.println("before updating sales org");
				String[] selectedValues = org.split(",");
				if (selectedValues.length > 0) {

					mHandler.insertManagementCodeAndDescrpn(trackId,
							"sales_group", org, "group_cd");
					System.out.println("after sales org insertion");
				}

			}

			// end

			// For BU
			ArrayList allBusinessUnit = new ArrayList();
			Iterator itrBu = businessUnits.iterator();

			while (itrBu.hasNext()) {
				Map mFilter = (Map) itrBu.next();
				String strL = (String) mFilter.get("BU");
				String strV = (String) mFilter.get("BU");

				labelValueBean = new LabelValueBean(strL, strV);
				track.setAllBusinessUnitList(labelValueBean);
			}

			if (getRequest().getParameter("save") != null) {

				String[] selectedValues = bu.split(",");
				if (selectedValues.length > 0) {

					mHandler.insertManagementCodeAndDescrpn(trackId, "bu", bu,
							"bu");
					System.out.println("after bu insertion");
				}

			}
			// End of BU

			// For Role Codes
			ArrayList allRoleCodes = new ArrayList();
			Iterator itrRc = roleCodes.iterator();

			while (itrRc.hasNext()) {
				Map mFilter = (Map) itrRc.next();
				String strL = (String) mFilter.get("ROLE_DESC");
				String strV = (String) mFilter.get("ROLE_CD");

				labelValueBean = new LabelValueBean(strL, strV);
				track.setAllRoleList(labelValueBean);
			}

			if (getRequest().getParameter("save") != null) {

				String[] selectedValues = roles.split(",");
				if (selectedValues.length > 0) {

					mHandler.insertManagementCodeAndDescrpn(trackId,
							"role_desc", roles, "role_cd");
					System.out.println("after role desc insertion");
				}

			}

			// End for Role codes

			// For Course codes
			ArrayList allCourseCodes = new ArrayList();
			// Iterator itrCc = courseCodes.iterator();

			/*
			 * while(itrCc.hasNext()){ Map mFilter=(Map)itrCc.next(); String
			 * strL = (String)mFilter.get("ACTIVITYNAME"); String strV =
			 * mFilter.get("ACTIVITYFK").toString();
			 * 
			 * 
			 * labelValueBean = new LabelValueBean(strL,strV);
			 * track.setAllCourseList(labelValueBean); }
			 */

			if (getRequest().getParameter("save") != null) {

				String[] selectedValues = courses.split(",");
				if (selectedValues.length > 0) {

					mHandler.insertManagementCodeAndDescrpn(trackId,
							"activityfk", courses, "activity_pk");
					System.out.println("after activityname insertion");
				}

			}

			if (getRequest().getParameter("save") != null) {

				String[] selectedValues = gender.split(",");
				if (selectedValues.length > 0) {

					mHandler.insertManagementCodeAndDescrpn(trackId, "sex",
							gender, "sex");
					System.out.println("after gender insertion");
				}

			}

			// End for course codes.

			if (trackId != null) {
				// System.out.println("hi");
				List listOfEntry = mHandler
						.getSelectedManagementFilter(trackId);

				// System.out.println("listOfEntry="+listOfEntry);
				Iterator it = listOfEntry.iterator();

				ArrayList buList = new ArrayList();

				while (it.hasNext()) {
					Map mFilter = (Map) it.next();
					// System.out.println("trackid"+trackId);
					track.setTrackId(trackId);
					track.setTrackLabel(trackLabel.getTrackLabel());
					String str = (String) mFilter.get("BUSINESS_UNIT");

					track.setBusinessUnit(str);
					track.setSalesOrg((String) mFilter.get("SALES_ORG"));
					track.setRoleCode((String) mFilter.get("ROLE_CODE"));
					track.setCourseCode((String) mFilter.get("COURSE_CODE"));

					if (track.getCourseCode() != null) {
						HashMap dataMap = new HashMap();
						HashMap courseCodeMap = new HashMap();
						List courseNames = mHandler.getSelectedCourseCodeList(
								trackId, track.getCourseCode());
						// System.out.println("courseNames in admin home controller:=="+courseNames);

						if (courseNames.size() > 0) {
							for (int i = 0; i < courseNames.size(); i++) {
								dataMap = (HashMap) courseNames.get(i);
								courseCodeMap.put(dataMap.get("FILTER_CODE")
										.toString(), (String) dataMap
										.get("FILTER_DESCRIPTION"));
							}
							report.setCourseCodeMap(courseCodeMap);
							// System.out.println("Course Code Map===Filter Code and desc"+courseCodeMap);
							// System.out.println("Getting Course code Map==="+report.getCourseCodeMap());
						}
					}

					track.setGender((String) mFilter.get("GENDER"));

					track.setHireStartDate((String) mFilter
							.get("HIRE_START_DATE"));
					track.setHireEndDate((String) mFilter.get("HIRE_END_DATE"));
					track.setTrainingCompletionStartDate((String) mFilter
							.get("TRAINING_COMPLETION_START_DATE"));
					track.setTrainingCompletionEnddate((String) mFilter
							.get("TRAINING_COMPLETION_END_DATE"));
					track.setTrainingRegistrationStartDate((String) mFilter
							.get("TRAINING_REG_START_DATE"));
					track.setTrainingRegistrationEndDate((String) mFilter
							.get("TRAINING_REG_END_DATE"));
					track.setGroupBy1((String) mFilter.get("FIRST_GROUP_BY"));
					track.setGroupBy2((String) mFilter.get("SECOND_GROUP_BY"));
					track.setGroupBy3((String) mFilter.get("THIRD_GROUP_BY"));
					track.setGroupBy4((String) mFilter.get("FOURTH_GROUP_BY"));
					track.setGroupBy5((String) mFilter.get("FIFTH_GROUP_BY"));
					track.setGroupBy6((String) mFilter.get("SIXTH_GROUP_BY"));
				}

				track.setManagementFilter(listOfEntry);
			}

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					"reportselect");

			report.setMenu(uSession.getMenuList());
			report.setTrack(track);
			page.setMain(report);
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
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward searchCourseManagement()
	public String searchCourseManagement()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {

			UserSession uSession = buildUserSession();

			BlankTemplateWpc page = new BlankTemplateWpc();
			P2lHandler p2l = new P2lHandler();

			System.out.println("debug1");
			System.out.println("Track Id in search and select courses.==="
					+ getRequest().getParameter("track"));
			String trackId = getRequest().getParameter("track");

			searchCourseManagementWc main = new searchCourseManagementWc();
			main.setTrackId(trackId);

			FormUtil.loadObject(getRequest(), main);

			if (!Util.isEmpty(main.getActivityname())
					|| !Util.isEmpty(main.getCode())) {

				List searchresult = new ArrayList();

				if (!Util.isEmpty(main.getCode())) {
					searchresult = p2l.getActivityTreeByCode(main.getCode());
				} else {
					searchresult = p2l.getActivityTreeByName(main
							.getActivityname().toUpperCase());
				}
				main.setSearchResults(searchresult);
			}

			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			System.out.println("debug2");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// end of addition
	// /////start===editForcastFilter====adminconfig/////////////
	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editForecastFilterCriteria(){
	public String editForecastFilterCriteria() {
		try {
			/* Infosys - Weblogic to Jboss Migrations changes end here */

			EmptyPageWc main = new EmptyPageWc();
			main.setRandomString("edit was clicked");

			UserSession uSession = buildUserSession();

			ForecastFilterHandler fcHandler = new ForecastFilterHandler();
			ForecastReport track = new ForecastReport();

			String trackId = getRequest().getParameter("trackID");
			System.out.println("trackId" + trackId);

			EditReportForecastFilterWc reportWc = new EditReportForecastFilterWc();

			FormUtil.loadObject(getRequest(), reportWc);
			// track = reportWc.getTrack();
			System.out.println("track from form obj="
					+ getRequest().getParameter("trackID"));

			// get all form values
			track.setTrackId(trackId);
			track.setTracklabel(getRequest().getParameter("trackName"));
			HttpSession session = getRequest().getSession();
			session.setAttribute("trackID", getRequest()
					.getParameter("trackID"));
			session.setAttribute("trackName",
					getRequest().getParameter("trackName"));

			// Check if its first time.
			boolean firstTime = false;
			List listOfEntry = fcHandler.getForecastFilterData(trackId);
			if (listOfEntry.size() == 0) {
				firstTime = true;
				reportWc.setFirstTime(firstTime);
			}

			boolean insertFlag = false;
			System.out.println("save=" + getRequest().getParameter("Save"));
			if ("Save".equals(getRequest().getParameter("Save"))) {
				insertFlag = true;
				// reportWc.setSave(true);
			}
			// reportWcsetSave(true);
			// get all form values
			// track.setTrackId(trackId);
			// track.setTracklabel(getRequest().getParameter("trackName"));
			String reportLabel = getRequest().getParameter("trackName");
			String role = getRequest().getParameter("role_cd");
			String startDate = getRequest().getParameter("startDate");
			String endDate = getRequest().getParameter("endDate");
			String duration = getRequest().getParameter("duration");
			String hpDate = getRequest().getParameter("hpdate");

			System.out.println("startDate=" + startDate);

			System.out.println("empty string" + "".equals(startDate));
			System.out.println("util " + Util.toEmpty(startDate));

			// ////////////start:validations//////////////////////////////////////
			if (((Util.toEmpty(startDate).equals(" ")) && !(Util
					.toEmpty(endDate).equals(" ")))
					|| (!(Util.toEmpty(startDate).equals(" ")) && (Util
							.toEmpty(endDate).equals(" ")))) {
				insertFlag = false;
				reportWc.setErrorMsg("Please enter both From date and To date.");
			}

			if (!(Util.toEmpty(duration).equals(" "))
					&& ((Util.toEmpty(startDate).equals(" ")) || (Util
							.toEmpty(endDate).equals(" ")))) {
				insertFlag = false;
				reportWc.setErrorMsg("Please enter both From date and To date for Duration");
			}

			int sMonth = 0;
			int eMonth = 0;
			int sYear = 0;
			int eYear = 0;

			if (!(Util.toEmpty(startDate).equals(" "))
					&& !(Util.toEmpty(endDate).equals(" "))) {

				Date stDate = new Date(startDate);
				Date eDate = new Date(endDate);
				sMonth = stDate.getMonth();
				sYear = stDate.getYear();
				eMonth = eDate.getMonth();
				eYear = eDate.getYear();

				System.out.println("sMonth=" + sMonth);
				System.out.println("sYear=" + sYear);
				System.out.println("eMonth=" + eMonth);
				System.out.println("eYear=" + eYear);

				if (stDate.compareTo(eDate) > 0) {
					insertFlag = false;
					reportWc.setErrorMsg("Start Date cannot be greater than End Date");
				}
			}

			if (!(Util.toEmpty(duration).equals(" "))
					&& !(Util.toEmpty(startDate).equals(" "))
					&& !(Util.toEmpty(endDate).equals(" "))) {

				try {
					int dur = Integer.parseInt(duration);
					int calculatedMonth = ((eYear - sYear) * 12)
							+ (eMonth - sMonth);
					System.out.println("calculatedMonth=" + calculatedMonth);
					if (dur > calculatedMonth) {
						insertFlag = false;
						reportWc.setErrorMsg("Entered Duration should be between From Date and To Date.");
					}
				} catch (NumberFormatException nfe) {
					insertFlag = false;
					reportWc.setErrorMsg("Invalid Duration");
				}

				for (int i = 0; i < duration.length(); i++) {
					if (!Character.isDigit(duration.charAt(i))) {
						insertFlag = false;
						reportWc.setErrorMsg("Invalid Duration");
						break;
					}
				}
			}

			if ((String) session.getAttribute("SEARCH_ID_LIST") != null) {
				session.removeAttribute("SEARCH_ID_LIST");
			}

			if ((String) session.getAttribute("SEARCH_VALUE_LIST") != null) {
				session.removeAttribute("SEARCH_VALUE_LIST");
			}
			// /////////////end:validations//////////////////////////////////////

			// getting List for combobox from the request
			String id = (String) getRequest().getParameter("idList");
			String value = (String) getRequest().getParameter("valueList");
			String status = getRequest().getParameter("status");
			System.out.println("status==" + status);
			System.out.println("id==" + id);
			System.out.println("value==" + value);
			HashMap idDescMap = new HashMap();

			if (!(Util.toEmpty(status).equals(" "))) {
				if (!(Util.toEmpty(id).equals(" "))
						&& !(Util.toEmpty(value).equals(" "))) {
					String idList[] = id.split(",");
					String valueList[] = value.split(",");
					// session.setAttribute("tempStatus",id);
					System.out.println("selectedList==" + idList[0]);
					reportWc.setIdList(idList);
					reportWc.setValueList(valueList);
					idDescMap = fcHandler.getActivityIDAndDesc(id);

					session.setAttribute(status, idDescMap);
					session.setAttribute(status + "_id", id);

				} else {
					session.setAttribute(status, "");
					session.setAttribute(status + "_id", "");
				}
			}

			boolean insert = false;
			int updateCount = 0;
			if (firstTime && insertFlag) {
				session.setAttribute("ROLE_CD",
						getRequest().getParameter("role_hidden"));
				session.setAttribute("START_DATE",
						getRequest().getParameter("startDate"));
				session.setAttribute("END_DATE",
						getRequest().getParameter("endDate"));
				session.setAttribute("DURATION",
						getRequest().getParameter("duration"));
				session.setAttribute("HIRE_OR_PROMOTION_DATE", getRequest()
						.getParameter("hpdate"));
				insert = fcHandler.insertFilterData(session);
				System.out.println("after insertion into forecast filter");
			}
			if (insertFlag && !firstTime) {
				System.out.println("before update compIdList ="
						+ session.getAttribute("compIdList"));
				System.out.println("before update nonCompIdList = "
						+ session.getAttribute("nonCompIdList"));
				System.out.println("before update Completed_id = "
						+ session.getAttribute("Completed_id"));
				System.out.println("before update NotCompleted_id = "
						+ session.getAttribute("NotCompleted_id"));

				// Added to save 2nd time changes.
				System.out.println("getRequest().getParameter(role_hidden)="
						+ getRequest().getParameter("role_hidden"));
				session.setAttribute("ROLE_CD",
						getRequest().getParameter("role_hidden"));
				session.setAttribute("START_DATE",
						getRequest().getParameter("startDate"));
				session.setAttribute("END_DATE",
						getRequest().getParameter("endDate"));
				session.setAttribute("DURATION",
						getRequest().getParameter("duration"));
				session.setAttribute("HIRE_OR_PROMOTION_DATE", getRequest()
						.getParameter("hpdate"));

				// updateCount=fcHandler.updateFilterData(role,startDate,endDate,"Y","N",duration,trackId);
				updateCount = fcHandler.updateFilterData(session);
				System.out.println("after updation into forecast filter");

			}

			// fetch data for role code from MV_field_employee_rbu table column
			// name
			// role_cd
			List roleCode = fcHandler.getAllRoleCode();
			track.setRoleCode(roleCode);
			// if(getRequest().getParameter("status")==null ||
			// getRequest().getParameter("cancel")!= "true"){
			if (!"true".equals(getRequest().getParameter("cancel"))) {
				System.out.println("status = "
						+ getRequest().getParameter("status"));
				// get data stored (if any) in the table Forecast_filters for
				// all
				// the status for the filter id.
				listOfEntry = fcHandler.getForecastFilterData(trackId);

				track.setListOfEntry(listOfEntry);

				Map filterDataMap = new HashMap();
				if (track.getListOfEntry().size() != 0) {
					filterDataMap = (HashMap) track.getListOfEntry().get(0);
				}
				track.setRoleCd((String) filterDataMap.get("ROLE_CD"));
				track.setStartDate((String) filterDataMap.get("START_DATE"));
				track.setEndDate((String) filterDataMap.get("END_DATE"));
				track.setPromotionDate((String) filterDataMap
						.get("HIRE_OR_PROMOTION_DATE"));
				track.setDuration((String) filterDataMap.get("DURATION"));

				session.setAttribute("ROLE_CD",
						(String) filterDataMap.get("ROLE_CD"));
				session.setAttribute("START_DATE",
						(String) filterDataMap.get("START_DATE"));
				session.setAttribute("END_DATE",
						(String) filterDataMap.get("END_DATE"));
				session.setAttribute("HIRE_OR_PROMOTION_DATE",
						(String) filterDataMap.get("HIRE_OR_PROMOTION_DATE"));
				session.setAttribute("DURATION",
						(String) filterDataMap.get("DURATION"));

				session.setAttribute("Completed_id",
						(String) filterDataMap.get("COMPLETED_COURSES"));
				session.setAttribute("NotCompleted_id",
						(String) filterDataMap.get("NOT_COMPLETED_COURSES"));
				session.setAttribute("Registered_id",
						(String) filterDataMap.get("REGISTERED_COURSES"));
				session.setAttribute("NotRegistered_id",
						(String) filterDataMap.get("NOT_REGISTERED_COURSES"));
				System.out.println("Completed_id"
						+ session.getAttribute("Completed_id"));
				// TODO: remove

				idDescMap = fcHandler.getActivityIDAndDesc((String) session
						.getAttribute("Completed_id"));
				session.setAttribute("Completed", idDescMap);
				idDescMap = fcHandler.getActivityIDAndDesc((String) session
						.getAttribute("NotCompleted_id"));
				session.setAttribute("NotCompleted", idDescMap);
				idDescMap = fcHandler.getActivityIDAndDesc((String) session
						.getAttribute("Registered_id"));
				session.setAttribute("Registered", idDescMap);
				idDescMap = fcHandler.getActivityIDAndDesc((String) session
						.getAttribute("NotRegistered_id"));
				session.setAttribute("NotRegistered", idDescMap);

			}

			reportWc.setTrack(track);

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					"reportselect");

			reportWc.setMenu(uSession.getMenuList());
			reportWc.setTrack(track);
			page.setMain(reportWc);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward addSelectedStatusCourse()
	public String addSelectedStatusCourse()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {
			UserSession uSession = buildUserSession();
			UserFilter uFilter = uSession.getUserFilter();
			HttpSession session = getRequest().getSession();
			AddSelectedStatusCourseWc wc = new AddSelectedStatusCourseWc();

			System.out.println("role cd in selected courses "
					+ (String) getRequest().getParameter("role_cd"));

			System.out.println("trackID in selected courses "
					+ session.getAttribute("trackID"));

			System.out.println("track from form obj="
					+ getRequest().getParameter("trackID"));
			String selectedId = (String) getRequest().getParameter(
					"activitySelectedIdList");
			String selectedValue = (String) getRequest().getParameter(
					"activitySelectedValueList");
			String status = (String) getRequest().getParameter("status");

			System.out.println("selectedId==" + selectedId);
			System.out.println("selectedValue==" + selectedValue);

			System.out.println("selectedId==" + selectedId);
			System.out.println("selectedValue==" + selectedValue);
			if (selectedId != null && selectedValue != null) {
				String selectIdList[] = selectedId.split(",");
				String selectValueList[] = selectedValue.split(",");
				System.out.println("selectedList==" + selectIdList[0]);
				System.out.println("Id List from session "
						+ session.getAttribute("SEARCH_ID_LIST"));
				System.out.println("Value List from session "
						+ session.getAttribute("SEARCH_VALUE_LIST"));
				String appendedId = (String) session
						.getAttribute("SEARCH_ID_LIST");
				if (appendedId == null) {
					appendedId = selectedId;
				} else {
					appendedId = appendedId + "," + selectedId;
				}
				session.setAttribute("SEARCH_ID_LIST", appendedId);
				String appendedValue = "";
				appendedValue = (String) session
						.getAttribute("SEARCH_VALUE_LIST");
				if (appendedValue == null) {
					appendedValue = selectedValue;
				} else {
					appendedValue = appendedValue + "," + selectedValue;
				}

				session.setAttribute("SEARCH_VALUE_LIST", appendedValue);

				System.out.println("Appended Id List from session "
						+ session.getAttribute("SEARCH_ID_LIST"));
				System.out.println("Appended Value List from session "
						+ session.getAttribute("SEARCH_VALUE_LIST"));

				wc.setSelectIdList(appendedId.split(","));
				wc.setSelectValueList(appendedValue.split(","));
			} else {
				session.getAttribute("ROLE_CD");
				System.out.println("role cd in selected courses "
						+ session.getAttribute("ROLE_CD"));
				System.out.println("role cd in getRequest "
						+ getRequest().getParameter("role_hidden"));
				System.out.println("START_DATE in getRequest "
						+ getRequest().getParameter("startDate"));
				System.out.println("endDate in getRequest "
						+ getRequest().getParameter("endDate"));
				System.out.println("hpdate in getRequest "
						+ getRequest().getParameter("hpdate"));
				session.setAttribute("ROLE_CD",
						getRequest().getParameter("role_hidden"));
				session.setAttribute("START_DATE",
						getRequest().getParameter("startDate"));
				session.setAttribute("END_DATE",
						getRequest().getParameter("endDate"));
				session.setAttribute("HIRE_OR_PROMOTION_DATE", getRequest()
						.getParameter("hpdate"));
				session.setAttribute("DURATION",
						getRequest().getParameter("duration"));
				System.out.println("hDate in add sel courses="
						+ session.getAttribute("HIRE_OR_PROMOTION_DATE"));

			}

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					"addSeletedStatusCourse");
			page.setMain(wc);
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
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward searchCourseForecast()
	public String searchCourseForecast()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {

			UserSession uSession = buildUserSession();

			BlankTemplateWpc page = new BlankTemplateWpc();
			P2lHandler p2l = new P2lHandler();
			String trackId = "";
			System.out.println("In searchCourseForecast");

			SearchCourseForecastWc main = new SearchCourseForecastWc();

			FormUtil.loadObject(getRequest(), main);

			if (!Util.isEmpty(main.getActivityname())
					|| !Util.isEmpty(main.getCode())) {

				List searchresult = new ArrayList();

				if (!Util.isEmpty(main.getCode())) {
					searchresult = p2l.getActivityTreeByCode(main.getCode());
				} else {
					searchresult = p2l.getActivityTreeByName(main
							.getActivityname().toUpperCase());
				}
				main.setSearchResults(searchresult);
			}

			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// /////end===editForcastFilter====adminconfig//////////////////////////

	// ////////////start: for optional fields/////////////////////
	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editForecastOptionalFields()
	public String editForecastOptionalFields()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {
			// System.out.println(getRequest().getParameter("trackID"));
			EmptyPageWc main = new EmptyPageWc();
			main.setRandomString("edit was clicked");
			UserSession uSession = buildUserSession();
			System.out.println("in Edif fields");

			FormUtil.loadObject(getRequest(), main);

			System.out.println("enum.nextelement_value == "
					+ getRequest().getParameter("trackID"));

			ForecastFilterHandler fcHandler = new ForecastFilterHandler();
			ForecastReport ftrack = new ForecastReport();
			UserFilter uFilter = uSession.getUserFilter();
			String trackID = getRequest().getParameter("trackID");
			String trackName = getRequest().getParameter("trackName");

			EditForecastOptionalFieldsWc report = new EditForecastOptionalFieldsWc();
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					"reportselect");

			// Check if its first time.
			boolean firstTime = false;
			List listOfEntry = fcHandler.getForecastOptionalFields(trackID);
			if (listOfEntry.size() == 0) {
				firstTime = true;
				report.setFirstTime(firstTime);
			}

			boolean insertFlag = false;
			System.out.println("save=" + getRequest().getParameter("Save"));
			if ("Save".equals(getRequest().getParameter("Save"))) {
				insertFlag = true;
				ftrack.setGender(getRequest().getParameter(
						ForecastReport.FIELD_GENDER));
				ftrack.setManagerEmail(getRequest().getParameter(
						ForecastReport.FIELD_MANAGER_EMAIL));
				ftrack.setHirDate(getRequest().getParameter(
						ForecastReport.FIELD_HIRE_DATE));
				ftrack.setPromDate(getRequest().getParameter(
						ForecastReport.FIELD_PROM_DATE));
				ftrack.setSource(getRequest().getParameter(
						ForecastReport.FIELD_SOURCE));
				ftrack.setGuId(getRequest().getParameter(
						ForecastReport.FIELD_GUID));
				ftrack.setGeographyDesc(getRequest().getParameter(
						ForecastReport.FIELD_GEOGRAPHY_DESC));
				ftrack.setRegionalOfficeState(getRequest().getParameter(
						ForecastReport.FIELD_REGIONAL_OFFICE_STATE));
				ftrack.setEmployeeId(getRequest().getParameter(
						ForecastReport.FIELD_EMPLOYEE_ID));
				ftrack.setProducts(getRequest().getParameter(
						ForecastReport.FIELD_PRODUCTS));
				ftrack.setTrackId(trackID);

				System.out.println("gender on save ====="
						+ getRequest()
								.getParameter(ForecastReport.FIELD_GENDER));

				System.out.println("inside save=" + ftrack.getGender());
			}

			if (firstTime && insertFlag) {
				System.out.println("inside insert");
				// insert into table.
				boolean bool = fcHandler.insertForecastOptionalFields(ftrack);
				if (bool)
					System.out.println("Inserted Successfully");
			}
			System.out.println("ftrack.getGender()=" + ftrack.getGender());
			if (insertFlag && !firstTime) {
				System.out.println("inside update");
				System.out.println(ftrack.getGender());
				// Update the table
				int num = fcHandler.updateForecastOptionalFields(ftrack);
				if (num > 0)
					System.out.println("Updated Successfully");
			}

			List optFields = fcHandler.getForecastOptionalFields(trackID);

			System.out.println("optFields==" + optFields.size());
			// ftrack.setFields(optFields);
			Map currMap = new HashMap();
			if (optFields.size() != 0) {

				currMap = (HashMap) optFields.iterator().next();
				System.out.println("optFields==" + optFields.size());

				ftrack.setGender(currMap.get("GENDER").toString());
				ftrack.setManagerEmail(currMap.get("MANAGER_EMAIL").toString());
				ftrack.setHirDate(currMap.get("HIRE_DATE").toString());
				ftrack.setPromDate(currMap.get("PROMOTION_DATE").toString());
				ftrack.setSource(currMap.get("SOURCE").toString());
				ftrack.setGuId(currMap.get("GUID").toString());
				ftrack.setGeographyDesc(currMap.get("GEOGRAPHY_DESCR")
						.toString());
				ftrack.setRegionalOfficeState(currMap.get(
						"REGIONAL_OFFICE_STATE").toString());
				ftrack.setEmployeeId(currMap.get("EMPLOYEE_ID").toString());
				ftrack.setProducts(currMap.get("PRODUCTS").toString());
			}

			ftrack.setTrackId(trackID);
			ftrack.setTracklabel(trackName);

			System.out.println("List == " + optFields.toString());
			report.setMenu(uSession.getMenuList());
			report.setTrack(ftrack);
			page.setMain(report);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// ////////////end: for optional fields/////////////////////

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward copyMove()
	public String copyMove()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {
			UserSession uSession = buildUserSession();

			BlankTemplateWpc page = new BlankTemplateWpc();
			System.out.println("In copyMove");

			CopyMoveWc main = new CopyMoveWc();

			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	// protected Forward editArchive()
	public String editArchive()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		try {
			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}
			UserSession uSession = buildUserSession();
			User user = uSession.getUser();

			if (!user.isSuperAdmin()) {
				return unauthorized();
			}

			// Start: added for TRT Phase 1 enhancement - Forecast filter
			// criteria-
			// removal of session attributes.
			System.out.println("Starting: Removal of session attributes");
			HttpSession session = getRequest().getSession();
			session.removeAttribute("trackID");
			session.removeAttribute("trackName");
			session.removeAttribute("ROLE_CD");
			session.removeAttribute("START_DATE");
			session.removeAttribute("END_DATE");
			session.removeAttribute("DURATION");
			session.removeAttribute("HIRE_OR_PROMOTION_DATE");
			session.removeAttribute("Completed_id");
			session.removeAttribute("NotCompleted_id");
			session.removeAttribute("Registered_id");
			session.removeAttribute("NotRegistered_id");
			session.removeAttribute("Completed");
			session.removeAttribute("NotCompleted");
			session.removeAttribute("Registered");
			session.removeAttribute("NotRegistered");
			System.out.println("Ending: Removal of session attributes");
			// Ends here

			String reportType = "";
			if (getRequest().getParameter("reportType") != null) {
				reportType = getRequest().getParameter("reportType");
				System.out.println("report type is" + reportType);
			}

			String editType = ""; // for

			if (getRequest().getParameter("editType") != null) {
				editType = getRequest().getParameter("editType");
				System.out.println("editType" + editType);
			}

			// Added for TRT major enhancement 3.6 F2(Management Summary Report)
			ManagementFilterHandler mtHandler = new ManagementFilterHandler();
			ForecastFilterHandler fcHandler = new ForecastFilterHandler();
			// end of addition

			TrainingReportGroupHandler grpHandler = new TrainingReportGroupHandler(); // Phase
																						// 2
																						// addition
			P2lHandler p2l = new P2lHandler();
			LaunchMeetingHandler handler = new LaunchMeetingHandler();
			// String menuName = getRequest().getParameter("name");
			String menuID = getRequest().getParameter("id");
			System.out.println(menuID);

			MenuList menuItem = trDb.getMenuItemByID(menuID);
			uSession.setMenuList(menuItem);
			String command = "";
			EditArchiveWc wc = new EditArchiveWc(user);

			if (getRequest().getParameter("command") != null) {
				if ("deleteMenu".equals(getRequest().getParameter("command"))) {
					command = "deleteMenu";
				} else if ("restore".equals(getRequest()
						.getParameter("command"))) {
					command = "restore";

				}
			}
			if (getRequest().getParameter("update") != null) {
				command = (String) getRequest().getParameter("update");
				System.out.println(command);
				System.out.println("check pie name");
			}

			if (command != null && command.equals("Save")) {

				String[] removefromgroup = getRequest().getParameterValues(
						"removefromgroup");
				String[] parent_group_id = getRequest().getParameterValues(
						"parent_group_id");
				String[] child_group_id = getRequest().getParameterValues(
						"child_group_id");

				if (removefromgroup != null) {
					System.out.println("remove from group"
							+ removefromgroup.length);
					for (int i = 0; i < removefromgroup.length; i++) {
						System.out.println("report id" + removefromgroup[i]);
						if (removefromgroup[i].trim().length() > 0)
							trDb.removeFromGroup(menuID, removefromgroup[i]);
					}
				}
				if (parent_group_id != null && child_group_id != null) {
					System.out.println("Parent groups | Child groups"
							+ parent_group_id.length);
					if (parent_group_id.length == child_group_id.length) {
						for (int i = 0; i < parent_group_id.length; i++) {
							System.out.println(parent_group_id[i] + " | "
									+ child_group_id[i]);
							if (parent_group_id[i].trim().length() > 0
									&& child_group_id[i].trim().length() > 0) {
								trDb.addToGroup(parent_group_id[i],
										child_group_id[i]);
							}
						}
					}
				}

				Enumeration enumName = getRequest().getParameterNames();
				String menuId = "";
				String sortNum = "";
				boolean saveFlag = true;
				// check if sort # are valid
				while (enumName.hasMoreElements()) {
					String name = (String) enumName.nextElement();
					if (name.startsWith("sort")) {
						String value = getRequest().getParameter(name);
						try {
							if (!Util.isEmpty(value))
								Integer.parseInt(value.trim());
						} catch (Exception e) {
							wc.setErrorMsg(wc.getErrorMsg()
									+ "<br>Invalid Sort value:" + value);
							saveFlag = false;
						}
					}
				}

				enumName = getRequest().getParameterNames();
				if (saveFlag) {
					int sortOrder = 0;
					String[] sorts = getRequest().getParameterValues("sort");
					if (sorts != null)
						System.out.println("sort elements " + sorts.length);
					while (enumName.hasMoreElements()) {
						String name = (String) enumName.nextElement();

						// if( name.startsWith("sort") ){
						// String value = getRequest().getParameter(name);
						if (sorts != null && sortOrder < sorts.length
								&& sorts[sortOrder] != null
								&& sorts[sortOrder].trim().length() > 0) {
							trDb.updateSortMenuByID(sorts[sortOrder],
									new Integer(sortOrder + 1).toString());
							System.out
									.println("------ --- ---- ---- ----- --- sorting "
											+ sorts[sortOrder]
											+ " "
											+ (sortOrder + 1));
						}
						// trDb.updateSortMenuByID(name.substring(5),value.trim());
						// }
						if (name.startsWith("access_")) {
							trDb.updateAccessMenuByID(name.substring(7),
									getRequest().getParameter(name));
						}
						sortOrder++;
					}
				}
				wc.setErrorMsg("<B>Save Successful.</B>");
			} else if (command != null && command.equals("deleteMenu")) {
				System.out.println("\n\n\nClick delete\n\n\n");
				String trackId = getRequest().getParameter("delIDTrack").trim();
				if (trackId.startsWith("MANAGEMENT")) {
					System.out.println("true");
				}
				System.out.println(trackId);
				if (!trackId.startsWith("LAUNCH")
						&& !trackId.startsWith("MANAGEMENT")
						&& !trackId.startsWith("FORECAST")) {
					System.out.println("Inside Launch Deletion");
					trDb.deleteMenuByID(getRequest().getParameter("delID"));
				} else if (trackId.startsWith("MANAGEMENT")) {
					System.out.println("Inside Management Deletion");
					trDb.deleteMenuByID(getRequest().getParameter("delID"));
				} else if (trackId.startsWith("FORECAST")) {
					System.out.println("Inside Forecast Deletion");
					trDb.deleteMenuByID(getRequest().getParameter("delID"));
				} else if (trackId.startsWith("LAUNCH")) {
					trDb.deleteMenuByID(getRequest().getParameter("delID"));
				}
				wc.setErrorMsg("<B>Delete Successful.</B>");
			} else if (command != null && command.equals("restore")) {
				if (getRequest().getParameter("trackID") != null) {
					String trackID = getRequest().getParameter("trackID");
					System.out.println("trackid for restore " + trackID);
					trDb.removeFromGroup(menuID, trackID);
					trDb.updateActiveStatusMenuByID(trackID, "1");
					trDb.updateActiveStatusMenuByParentID(trackID, "1");
				} else {
					System.out.println("Track ID is empty");
				}
				wc.setErrorMsg("<B>Restore Successful.</B>");
			}
			// Render
			MainTemplateWpc page = new MainTemplateWpc(user, "reportselect");
			wc.setMenuName(reportType);
			// System.out.println("reportType====="+reportType);
			wc.setMenuName(menuItem.getLabel());
			wc.setRootID(menuID);
			// renderEditMenu(wc,menuID);
			renderArchiveMenu(wc, menuID);
			page.setMain(wc);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	private void renderArchiveMenu(EditArchiveWc wc, String menuID) {
		Vector list = new Vector();
		MenuList[] menuList = trDb.getAllMenuArchiveByID(menuID);
		for (int i = 0; i < menuList.length; i++) {
			list.addElement(menuList[i]);
		}
		wc.setMenu(list);
	}

	/*
	 * Infosys - Weblogic to Jboss Migrations changes start here: Added below
	 * code for Struts 2 compatibility
	 */


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
}