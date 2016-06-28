package com.pfizer.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.actionForm.RbutraineetablemapForm;
import com.pfizer.actionForm.UploadGuestForm;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.ClassRosterBean;
import com.pfizer.db.EmpReport;
import com.pfizer.db.Employee;
import com.pfizer.db.P2LRegistration;
import com.pfizer.db.RBUClassRosterBean;
import com.pfizer.db.RBUEnrollChangeReport;
import com.pfizer.db.RBUGuestClassData;
import com.pfizer.db.RBUTrainingWeek;
import com.pfizer.db.RBUUnassignedEmployees;
import com.pfizer.db.Territory;
import com.pfizer.db.UserAccess;
import com.pfizer.db.VarianceReportBean;
import com.pfizer.hander.AdminReportHandler;
import com.pfizer.hander.PLCHandler;
import com.pfizer.hander.RBUEmployeeHandler;
import com.pfizer.hander.RBUGEMSTravelFeedHandler;
import com.pfizer.hander.RBUPieChartHandler;
import com.pfizer.hander.RBUSHandler;
import com.pfizer.hander.RBUTravelFeedHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.webapp.chart.PieChartBuilder;
import com.pfizer.webapp.email.RBUEmailListener;
import com.pfizer.webapp.report.ClassFilterForm;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.RBU.RBUChartBean;
import com.pfizer.webapp.wc.RBU.RBUChartsWc;
import com.pfizer.webapp.wc.RBU.RBUPLCChartsWc;
import com.pfizer.webapp.wc.components.AdmintoolsSelectRBUWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.chart.RBUChartHeaderWc;
import com.pfizer.webapp.wc.components.report.ClassRosterWc;
import com.pfizer.webapp.wc.components.report.GuestTrainerListWc;
import com.pfizer.webapp.wc.components.report.RBUClassRoomReportWc;
import com.pfizer.webapp.wc.components.report.RBUClassRosterReportWc;
import com.pfizer.webapp.wc.components.report.RBUEnrollmentChangeReportWc;
import com.pfizer.webapp.wc.components.report.RBUEnrollmentExceptionWc;
import com.pfizer.webapp.wc.components.report.RBUGCConflictReportWc;
import com.pfizer.webapp.wc.components.report.RBUTraineeTableMapWc;
import com.pfizer.webapp.wc.components.report.RBUUnassignedEmployeesWc;
import com.pfizer.webapp.wc.components.report.TrainingScheduleByTrackWc;
import com.pfizer.webapp.wc.components.report.TrainingScheduleEmplListWc;
import com.pfizer.webapp.wc.components.report.TrainingScheduleWc;
import com.pfizer.webapp.wc.components.report.VarianceReportListWc;
import com.pfizer.webapp.wc.global.HeaderAdminReportsWc;
import com.pfizer.webapp.wc.global.HeaderDashBoardReportsWc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.printing.PrintHandlers;
import com.tgix.printing.PrintingConstants;
import com.tgix.printing.TrainingWeeks;
import com.tgix.printing.VelocityConvertor;
import com.tgix.rbu.FutureAllignmentBuDataBean;
import com.tgix.rbu.FutureAllignmentRBUDataBean;
import com.tgix.rbu.ProductDataBean;
import com.tgix.rbu.RBUHandlers;
import com.tgix.wc.WebPageComponent;

public class RBUControllerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * @common:control
	 */

	public Employee[] excelRBUEmployee;
	public ProductDataBean[] productDataBean;
	public FutureAllignmentBuDataBean[] buDataBean;
	public FutureAllignmentRBUDataBean[] rbuDataBean;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private TransactionDB trDb = new TransactionDB();
	private AddGuestForm addGuestForm = new AddGuestForm();
	private RBUClassRoomReportForm rbuClassRoomReportForm = new RBUClassRoomReportForm();
	private RbutraineetablemapForm rbutraineetablemapForm = new RbutraineetablemapForm();
	private UploadGuestForm uploadGuestForm = new UploadGuestForm();
	private RbuUnAssignedEmployeeForm rbuUnAssignedEmployeeForm = new RbuUnAssignedEmployeeForm();

	public RbuUnAssignedEmployeeForm getRbuUnAssignedEmployeeForm() {
		return rbuUnAssignedEmployeeForm;
	}

	public void setRbuUnAssignedEmployeeForm(
			RbuUnAssignedEmployeeForm rbuUnAssignedEmployeeForm) {
		this.rbuUnAssignedEmployeeForm = rbuUnAssignedEmployeeForm;
	}

	public AddGuestForm getAddGuestForm() {
		return addGuestForm;
	}

	public void setAddGuestForm(AddGuestForm addGuestForm) {
		this.addGuestForm = addGuestForm;
	}

	public RBUClassRoomReportForm getRbuClassRoomReportForm() {
		return rbuClassRoomReportForm;
	}

	public void setRbuClassRoomReportForm(
			RBUClassRoomReportForm rbuClassRoomReportForm) {
		this.rbuClassRoomReportForm = rbuClassRoomReportForm;
	}

	public RbutraineetablemapForm getRbutraineetablemapForm() {
		return rbutraineetablemapForm;
	}

	public void setRbutraineetablemapForm(
			RbutraineetablemapForm rbutraineetablemapForm) {
		this.rbutraineetablemapForm = rbutraineetablemapForm;
	}

	public UploadGuestForm getUploadGuestForm() {
		return uploadGuestForm;
	}

	public void setUploadGuestForm(UploadGuestForm uploadGuestForm) {
		this.uploadGuestForm = uploadGuestForm;
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
		this.request = request;

	}

	// Uncomment this declaration to access Global.app.
	//
	// public global.Global globalApp;
	//

	// For an example of page flow exception handling see the example "catch"
	// and "exception-handler"
	// annotations in {project}/WEB-INF/src/global/Global.app

	/**
	 * This method represents the point of entry into the pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success" path="RBUTrainingSchedule.do"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward begin(){
	 */

	public String begin() {
		try {
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

	// HS Chart report - required parameters: product, area, region, district
	// default as overall - one pie chart for all employees and all products
	/**
	 * @jpf:action
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward PLCReport(){
	 */

	public String PLCReport() {
		try {
			UserSession uSession;

			// Shannon - Mock up user session for dev testing

			uSession = new UserSession();
			UserFilter uFilter = new UserFilter();
			uFilter.setAdmin(true);
			uSession.setUserFilter(uFilter);

			TerritoryFilterForm filterForm = new TerritoryFilterForm();

			User testUser = new User();

			UserAccess ua = new UserAccess();
			ua.setUserType("SUPER ADMIN");
			ua.setEmplid("admin");
			testUser.setValidUser(true);
			testUser.setUserAcess(ua);
			uSession.setUser(testUser);

			getRequest().getSession().setAttribute(UserSession.ATTRIBUTE,
					uSession);
			getRequest().getSession().setAttribute("ReportType", "RBUPLC");// todo
																			// where
																			// this
																			// is
																			// used

			// uSession = UserSession.getUserSession(getRequest());
			// TerritoryFilterForm filterForm =
			// uSession.getNewTerritoryFilterForm();
			filterForm.setProduct("Overall");
			FormUtil.loadObject(getRequest(), filterForm);
			uFilter.setFilterForm(filterForm);

			// End of Mock Up

			// TODO ADD PRODUCTs to filter form, right now only area region and
			// district, team won't be used by RBU

			FormUtil.loadObject(getRequest(), filterForm);
			ServiceFactory factory = Service.getServiceFactory(trDb);

			PLCHandler plcHandler = factory.getPLCHandler();

			RBUSHandler rbusHandler = factory.getRBUSHander();

			List charts = new ArrayList();
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"PLC");

			// TODO - will changed overall to be dynamic from user query
			boolean overall = true;
			if (overall) {
				int totalParticipants = rbusHandler.getPLCOverAllTotalCount(
						"Overall", filterForm.getArea(),
						filterForm.getRegion(), filterForm.getDistrict());
				Territory terr = factory.getTerritoryHandler().getTerritory(
						uFilter);

				ChartHeaderWc chartHeaderWc = new ChartHeaderWc("", "", "",
						totalParticipants, uFilter.getProduct());

				RBUPLCChartsWc chartpage = new RBUPLCChartsWc(filterForm,
						uSession.getUser(), chartHeaderWc);
				PieChartBuilder pBuilder = new PieChartBuilder();

				ChartDetailWc chartdetail = null;
				RBUChartBean[] thisRBUChartBean;

				uSession.getUserFilter().getQuseryStrings().setSection(null);
				// getPlcChartRBU( UserFilter uFilter, HttpSession
				// session,POAChartBean[] thisPOAChartBean,String
				// productCode,boolean fromFilter)
				// Get The Count For the OverAll
				thisRBUChartBean = (RBUChartBean[]) rbusHandler
						.getFilteredRBUSOverallChart("Overall",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict());
				PieChart pieChart = pBuilder.getPlcChartRBU(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Overall", false);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_OVERALL,
								"Overall"));
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);

				ChartListWc chartListWc = new ChartListWc(charts);
				// Add the Overall Trainees;
				chartListWc.setTrainees(pieChart.getCount());
				chartpage.setWebComponent(chartListWc);
				page.setMain(chartpage);

				getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			}

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
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward generalSessionAttendence(){
	 */

	public String generalSessionAttendence() {
		try {
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward VarianceReport(){
	 */

	public String VarianceReport() {
		try {
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}

			UserFilter filter = uSession.getUserFilter();

			AdminReportHandler handler = new AdminReportHandler();
			VarianceReportBean[] varianceReportBean = handler
					.getRBUVarianceReport();
			System.out.println("variance size " + varianceReportBean.length);

			VarianceReportListWc varianceReport = new VarianceReportListWc(
					filter, varianceReportBean, user, AppConst.EVENT_RBU);

			String sDownloadToExcel = getRequest()
					.getParameter("downloadExcel");

			if (sDownloadToExcel != null
					&& sDownloadToExcel.equalsIgnoreCase("true")) {
				WebPageComponent page;
				page = new BlankTemplateWpc(varianceReport);
				getResponse().setContentType(
						"application/vnd.ms-excel;charset=ISO-8859-2");
				getRequest().setAttribute("exceldownload", "true");
				getResponse().setHeader("Content-Disposition",
						"attachment; filename=\"RBU - Variance Report.xls\"");
				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes start here
				 * --> return new Forward("successXls");
				 */
				return new String("successXls");
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes ends here
				 * -->
				 */

			} else {
				MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
						AppConst.EVENT_RBU, "RBUREPORT");
				page.setMain(varianceReport);
				getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes start here
				 * --> return new Forward("success");
				 */
				return new String("success");
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes ends here
				 * -->
				 */

			}
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward RBUTrainingSchedule(){
	 */

	public String RBUTrainingSchedule() {
		try {

			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}

			String downloadExcel = getRequest().getParameter("downloadExcel");

			AdminReportHandler handler = new AdminReportHandler();
			EmpReport[] empReport = handler.getRBUTrainingSchedule();
			TrainingScheduleWc main = new TrainingScheduleWc(empReport,
					AppConst.EVENT_RBU);
			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"RBU - Training Schedule Summary.xls\"");

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
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward RBUTrainingScheduleEmplList(){
	 */

	public String RBUTrainingScheduleEmplList() {
		try {
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}

			String commandfrom = getRequest().getParameter("commandfrom");

			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			String downloadExcel = getRequest().getParameter("downloadExcel");

			ClassFilterForm form = new ClassFilterForm();
			form.setProduct(getRequest().getParameter(form.FIELD_PRODUCT));
			form.setStartDate(getRequest().getParameter(form.FIELD_STARTDATE));
			form.setEndDate(getRequest().getParameter(form.FIELD_ENDDATE));

			form.setEnrollmentDate(getRequest().getParameter(
					form.FIELD_ENROLLMENTDATE));

			AdminReportHandler handler = new AdminReportHandler();
			EmpReport[] empReport = null;

			// ADD FRO RBU - Shannon
			if (commandfrom != null && commandfrom.equals("bytrack")) {
				form.setClasses(getRequest().getParameter(
						ClassFilterForm.FIELD_CLASS));
				form.setRolegroup(getRequest().getParameter(
						ClassFilterForm.FIELD_ROLEGRROUP));
				form.setTrack(getRequest().getParameter(
						ClassFilterForm.FIELD_TRACK));
				empReport = handler.getRBUTrainingScheduleEmplListRBU(form);

			} else {
				form.setIfmanager(getRequest().getParameter(
						ClassFilterForm.FIELD_ISMANAGER));
				empReport = handler.getRBUTrainingScheduleEmplList(form);

			}

			TrainingScheduleEmplListWc main = new TrainingScheduleEmplListWc(
					form, empReport, AppConst.EVENT_RBU);

			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"RBU - Training Schedule Detail.xls\"");

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

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
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
	 * @jpf:forward name="success" path="adminToolsSelect.do"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward beginRBU(){
	 */

	public String beginRBU() {
		try {
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

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

	private void callSecurePage() {
		SuperWebPageComponents tpage = new BlankTemplateWpc();
		tpage.setLoginRequired(true);
		IAMUserControl upControl = new IAMUserControl();
		upControl.checkAuth(getRequest(), getResponse(), tpage);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward RBUTrainingScheduleByTrack(){
	 */

	public String RBUTrainingScheduleByTrack() {
		try {

			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}
			String downloadExcel = getRequest().getParameter("downloadExcel");

			AdminReportHandler handler = new AdminReportHandler();
			// EmpReport[] empReport = handler.getRBUTrainingSchedule();
			List report = handler.getRBUTrainingScheduleByTrack();
			TrainingScheduleByTrackWc main = new TrainingScheduleByTrackWc(
					report, AppConst.EVENT_RBU);
			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"RBU - Training Schedule Summary.xls\"");

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
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
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
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward adminToolsSelect(){
	 */

	public String adminToolsSelect() {
		try {

			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "RBUREPORTS");

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			MainTemplateWpc page = new MainTemplateWpc(user, "RBU", "RBUREPORT");

			page.setMain(new AdmintoolsSelectRBUWc(user));
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

	/**
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward RBUEnrollmentExceptionReport(){
	 */

	public String RBUEnrollmentExceptionReport() {
		try {
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}
			String downloadExcel = getRequest().getParameter("downloadExcel");

			AdminReportHandler handler = new AdminReportHandler();

			List report = handler.getRBUEnrollmentExcepitons();
			RBUEnrollmentExceptionWc main = new RBUEnrollmentExceptionWc(
					report, AppConst.EVENT_RBU);
			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"RBU - Training Schedule Summary.xls\"");

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
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward RBUReportClassRoster(){
	 */

	public String RBUReportClassRoster() {
		try {
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}

			String downloadExcel = getRequest().getParameter("downloadExcel");
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			ClassFilterForm form = new ClassFilterForm();

			if (!"true".equalsIgnoreCase(downloadExcel)) {
				FormUtil.loadObject(getRequest(), form);
			} else {
				form.setClassroom(getRequest().getParameter(
						form.FIELD_CLASSROOM));
				form.setTrainingDate(getRequest().getParameter(
						form.FIELD_TRAININGDATE));
				form.setProduct(getRequest().getParameter(form.FIELD_PRODUCT));
			}

			if (form.getTrainingDate() == null
					|| "".equals(form.getTrainingDate())) {
				form.setTrainingDate("All");
			}
			if (form.getClassRoom() == null || "".equals(form.getClassRoom())) {
				form.setClassroom("All");
			}
			if (form.getProduct() == null || "".equals(form.getProduct())) {
				form.setProduct("All");
			}

			AdminReportHandler handler = new AdminReportHandler();
			EmpReport[] empReport = handler.getClassRosterReport(
					AppConst.EVENT_PDF, form);

			ClassRosterBean[] classData = handler.getClassData(
					AppConst.EVENT_PDF, null);
			ClassRosterWc main = new ClassRosterWc(form, classData, empReport,
					AppConst.EVENT_RBU);
			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse().addHeader("content-disposition",
						"attachment;filename=\"RBU - Class Roster.xls\"");

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
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
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
	 * @jpf:forward path="ShipmentOrderConfirmation.jsp" name="success"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward placeShipmentOrders(){
	 */

	public String placeShipmentOrders() {
		try {
			// Changes made for RBU Shipment
			PrintHandlers.callTRMOrderProcess();
			// RBUHandlers rbu = new RBUHandlers();
			// rbu.handleNotification();

			HttpServletRequest req = this.getRequest();
			req.setAttribute("ShipmentCommand", "OrderPlaced");
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward RBUGuestTrainerList(){
	 */

	public String RBUGuestTrainerList() {
		try {
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}

			String command = getRequest().getParameter("command");

			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			String downloadExcel = getRequest().getParameter("downloadExcel");

			RBUSHandler rbuhandler = new RBUSHandler();

			System.out.println("command - " + command);

			if (command != null && command.equals("add")) {
				RBUGuestClassData data = new RBUGuestClassData();
				data.setEmplid(getRequest().getParameter("emplid"));
				data.setClassid(getRequest().getParameter("classid"));
				data.setFirstname(getRequest().getParameter("firstname"));
				data.setLastname(getRequest().getParameter("lastname"));
				data.setEmail(getRequest().getParameter("email"));
				data.setNt_domain(getRequest().getParameter("nt_domain"));
				data.setNt_id(getRequest().getParameter("nt_id"));
				data.setEnrolledby(user.getId());
				rbuhandler.addGuest(data);
			} else if (command != null && command.equals("edit")) {
				RBUGuestClassData data = new RBUGuestClassData();
				data.setClassid(getRequest().getParameter("classid"));
				data.setFirstname(getRequest().getParameter("firstname"));
				data.setLastname(getRequest().getParameter("lastname"));
				data.setEmail(getRequest().getParameter("email"));
				data.setNt_domain(getRequest().getParameter("nt_domain"));
				data.setNt_id(getRequest().getParameter("nt_id"));
				data.setEnrolledby(user.getId());
				rbuhandler.updateGuest(data);
			}
			if (command != null && command.equals("remove")) {
				String[] selectedGuests = getRequest().getParameterValues(
						"selectedguests");
				rbuhandler.deleteGuests(selectedGuests, getRequest()
						.getParameter("classid"));
				System.out.println(getRequest().getQueryString());
			}

			List gts = rbuhandler.getGTbyClassId(getRequest().getParameter(
					"classid"));

			GuestTrainerListWc main = new GuestTrainerListWc(gts,
					AppConst.EVENT_RBU);

			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"RBU - Training Schedule Detail.xls\"");

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

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/components/report/updateGuest.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward addGuest(AddGuestForm form){
	 */

	public String addGuest() {
		try {
			AddGuestForm form = getAddGuestForm();
			form.setEmplid(getRequest().getParameter("emplid"));
			form.setClassid(getRequest().getParameter("classid"));
			form.setFirstname(getRequest().getParameter("firstname"));
			form.setLastname(getRequest().getParameter("lastname"));
			form.setEmail(getRequest().getParameter("email"));
			form.setNt_domain(getRequest().getParameter("nt_domain"));
			form.setNt_id(getRequest().getParameter("nt_id"));

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success",form);
			 */
			// return new String("success", form);
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
	 * @jpf:forward name="success" path="uploadGuestFile.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward uploadGuest(UploadGuestForm form){
	 */

	public String uploadGuest() {
		try {

			UploadGuestForm form = getUploadGuestForm();
			form.setClassid(getRequest().getParameter("classid"));
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
	 * @jpf:forward name="success" path="/WEB-INF/jsp/PDFHS/GenerateP2L.jsp"
	 * @jpf:forward name="download"
	 *              path="/WEB-INF/jsp/PDFHS/GenerateP2LDownload.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward generateP2L(){
	 */

	public String generateP2L() {
		try {
			String action = getRequest().getParameter("action");
			String message = "";
			String line = "";
			SimpleDateFormat formatFileName = new SimpleDateFormat("MM-dd-yyyy");
			/* Stores On Server */
			if ("generate".equalsIgnoreCase(action)) {

				P2LRegistration[] regs = trDb.getP2LRegistrations();

				try {

					File p2lFile;

					String p2lFolder = getP2LFolder(PrintingConstants.env_type);
					// String p2lFolder =
					// getP2LFolder(PrintingConstants.env_local);
					p2lFile = new File(p2lFolder + File.separator
							+ "TRT_P2L_Registration_"
							+ formatFileName.format(new Date()) + ".txt");

					BufferedWriter bf1 = new BufferedWriter(new FileWriter(
							p2lFile));

					for (int i = 0; i < regs.length; i++) {
						line = getLine(regs[i]);

						bf1.write(line.toString());
						bf1.write("\n");
					}
					bf1.flush();
					bf1.close();
				} catch (IOException e) {
					message = "An exception occurred while generating the file";
					e.printStackTrace();
				}
				message = "File has been generated";
			} else if ("download".equalsIgnoreCase(action)) {
				String filePath = getLatestFile();

				if (filePath == null || filePath.equals("")) {
					message = "File not found";
					/**
					 * <!-- Infosys - Weblogic to Jboss migration changes start
					 * here --> return new Forward("success");
					 */
					return new String("success");
					/**
					 * <!-- Infosys - Weblogic to Jboss migration changes ends
					 * here -->
					 */

				}
				getResponse().addHeader("content-disposition",
						"attachment;filename=\"test.txt\"");
				getResponse().setContentType("application/octet-stream");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");
				try {
					BufferedReader in = new BufferedReader(new FileReader(
							filePath));
					String str;
					while ((str = in.readLine()) != null) {
						getResponse().getWriter().println(str);
					}
					in.close();
				} catch (IOException e) {
				}

				return new String("download");
			} else if ("generateANDdownload".equalsIgnoreCase(action)) {
				String fileName = "TRT_P2L_Registration_"
						+ formatFileName.format(new Date()) + ".txt";
				P2LRegistration[] regs = trDb.getP2LRegistrations();
				getResponse().addHeader("content-disposition",
						"attachment;filename=\"" + fileName + "\"");
				getResponse().setContentType("application/octet-stream");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");
				try {
					for (int i = 0; i < regs.length; i++) {
						line = getLine(regs[i]);
						if (i < regs.length - 1) {
							getResponse().getWriter().println(line);
						} else {
							getResponse().getWriter().print(line);
						}

					}

				} catch (IOException e) {
				}
				return new String("download");
			}
			getRequest().setAttribute("message", message);
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

	private String getP2LFolder(String sEnv) {
		String sDeploymentRoot = VelocityConvertor.getDeploymentRoot(sEnv);
		String sTemplateRoot = sDeploymentRoot + File.separator
				+ "TrainingReports" + File.separator + "P2LFiles"
				+ File.separator + "CreatedFiles";
		// System.out.println("sTemplateRoot:" + sTemplateRoot);
		return sTemplateRoot;
	}

	private String getLatestFile() {
		SimpleDateFormat formatFileName = new SimpleDateFormat("MM-dd-yyyy");
		String filePath = "";
		// String p2lFolder = getP2LFolder(PrintingConstants.env_local);
		String p2lFolder = getP2LFolder(PrintingConstants.env_staging);
		File dir = new File(p2lFolder);

		// It is also possible to filter the list of returned files.
		// This example does not return any files that start with `.'.
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("TRT_P2L_Registration_");
			}
		};

		String[] children = dir.list(filter);
		Date maxDate = null;
		Date createdDate = null;
		String filename = null;
		String dateStr = null;
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				filename = children[i];
				dateStr = (filename.replaceFirst("TRT_P2L_Registration_", ""))
						.replaceFirst(".txt", "");
				try {
					createdDate = formatFileName.parse(dateStr);
				} catch (Exception e) {
					createdDate = null;
				}
				if (maxDate == null) {
					maxDate = createdDate;
				} else if (maxDate.compareTo(createdDate) < 0) {
					maxDate = createdDate;
				}
			}
		}

		if (maxDate != null) {
			filePath = p2lFolder + File.separator + "TRT_P2L_Registration_"
					+ formatFileName.format(maxDate) + ".txt";
		}
		return filePath;
	}

	private String getLine(P2LRegistration reg) {
		String _Delimiter = "|";
		StringBuffer line = new StringBuffer("");

		line.append(Util.ifNull(reg.getEmpNumber(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getClassCode(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getStartDate(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getRegistrationDate(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getCompletionDate(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getLaunchDate(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getScore(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getPassed(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getCancellationDate(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getPaymentTerm(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getCost(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getCurrency(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getTimezone(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getStatus(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getNotes(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getSsActivityCode(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getSsActivityStartDate(), "") + _Delimiter);
		line.append(Util.ifNull(reg.getCreateRegistration(), "") + _Delimiter);

		return line.toString();
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="P2LRegistrationConfirmation.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward createP2LRegistrations(){
	 */

	public String createP2LRegistrations() {
		try {

			// Added for RBU Shipment by Jeevan
			RBUHandlers.callP2LRegistrationProcess();
			HttpServletRequest req = this.getRequest();
			req.setAttribute("P2LRegistration", "P2LRegistration");
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
	 * @jpf:forward name="success" path="/WEB-INF/jsp/RBU/travelfeed.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward rbutravelfeed(){
	 */

	public String rbutravelfeed() {
		try {
			RBUTravelFeedHandler handler = new RBUTravelFeedHandler();
			String requestURL = getRequest().getRequestURL().toString();
			getRequest().setAttribute("returncode",
					handler.generateCSV(requestURL));
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
	 * @jpf:forward name="success" path="/WEB-INF/jsp/RBU/travelfeed.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward rbugemstravelfeed(){
	 */

	public String rbugemstravelfeed() {
		try {
			RBUGEMSTravelFeedHandler handler = new RBUGEMSTravelFeedHandler();
			String requestURL = getRequest().getRequestURL().toString();
			getRequest().setAttribute("returncode",
					handler.generateCSV(requestURL));
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
	 * @jpf:forward path="SandboxRefreshConfirmation.jsp" name="success"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward rbuSandboxRefreshProcess(){
	 */

	public String rbuSandboxRefreshProcess() {
		try {
			// Changes made for RBU Shipment
			RBUHandlers rbu = new RBUHandlers();
			rbu.rbuSandboxRefresh();

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
	 * @jpf:forward name="success" path="EmailConfirmation.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward testEmail(){
	 */

	public String testEmail() {
		try {
			RBUHandlers.testEmail();
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
	 * @jpf:forward name="success" path="EmailConfirmation.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward emailReminder(){
	 */

	public String emailReminder() {
		try {
			RBUEmailListener email = new RBUEmailListener();
			email.runEmails();
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward RBUGCConflictsReport(){
	 */

	public String RBUGCConflictsReport() {
		try {

			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}

			String downloadExcel = getRequest().getParameter("downloadExcel");

			RBUSHandler handler = new RBUSHandler();
			List empReport = handler.getAllGTConflicts();
			//
			// EmpReport empReportTotal =
			// handler.getEnrollmentSummaryReportTotal(AppConst.EVENT_PDF);
			RBUGCConflictReportWc main = new RBUGCConflictReportWc(empReport,
					AppConst.EVENTID_RBU);

			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"RBU - Enrollment Change Report.xls\"");

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
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward RBUClassRoomReport(RBUClassRoomReportForm
	 * form){
	 */

	public String RBUClassRoomReport() {
		try {

			RBUClassRoomReportForm form = getRbuClassRoomReportForm();
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE
				// );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}

			String downloadExcel = getRequest().getParameter("downloadExcel");

			RBUSHandler handler = new RBUSHandler();

			String week_id = getRequest().getParameter("week_id");
			if (week_id == null) {
				RBUTrainingWeek week = handler.getCurrentWeek();
				if (week != null) {
					week_id = week.getWeek_id();
				} else {
					week_id = "1";
				}
			}

			List rooms = new ArrayList();
			List weeks = handler.getClassWeeks();

			if (week_id != null) {
				rooms = handler.getClassRooms(week_id);
			}
			form.setWeeks(weeks);
			form.setWeek_id(week_id);

			RBUClassRoomReportWc main = new RBUClassRoomReportWc(rooms,
					AppConst.EVENTID_RBU);
			main.setRBUClassRoomReportForm(form);

			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse().addHeader("content-disposition",
						"attachment;filename=\"RBU Class Room Report.xls\"");

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

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");

			page.setMain(main);
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward rbutraineetablemap(RbutraineetablemapForm
	 * form){
	 */

	public String rbutraineetablemap() {
		try {
			RbutraineetablemapForm form = getRbutraineetablemapForm();
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE
				// );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}

			String day = getRequest().getParameter("day");
			String product = getRequest().getParameter("product");
			String room = getRequest().getParameter("room");
			String room_id = getRequest().getParameter("room_id");
			String table_id = getRequest().getParameter("table_id");
			String class_id = getRequest().getParameter("class_id");
			String week_id = getRequest().getParameter("week_id");

			form.setDay(day);
			form.setProduct(product);
			form.setRoom(room);
			form.setTable_id(table_id);
			form.setClass_id(class_id);
			form.setWeek_id(week_id);
			form.setRoom_id(room_id);

			RBUSHandler handler = new RBUSHandler();
			List ts = handler.getEmpListByTalbe(class_id, table_id);
			List guest = handler.getGuestListByTable(class_id, table_id);

			RBUTraineeTableMapWc main = new RBUTraineeTableMapWc(ts, guest,
					AppConst.EVENTID_RBU);
			main.setAvailableTables(handler.getTables(class_id));
			main.setAvailableRooms(handler.getRooms(class_id));
			main.setRbutraineetablemapForm(form);

			// main.setRBUClassRoomReportForm(form);

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			// BlankTemplateWpc page = new BlankTemplateWpc(main);
			page.setMain(main);
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward
	 * rbuUnassignedEmployeesReport(RbuUnAssignedEmployeeForm form){
	 */

	public String rbuUnassignedEmployeesReport() {
		try {
			RbuUnAssignedEmployeeForm form = getRbuUnAssignedEmployeeForm();
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			uSession = UserSession.getUserSession(getRequest());
			String firstTime = "";
			String weekId = "";
			if (getRequest().getParameter("firstTime") != null) {
				firstTime = getRequest().getParameter("firstTime");
			}
			String downloadExcel = getRequest().getParameter("downloadExcel") == null ? ""
					: getRequest().getParameter("downloadExcel");
			if (firstTime.equals("true")) {
				TrainingWeeks[] trainingWeekAray;
				String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
				trainingWeekAray = trDb.getTrainingWeeks(sql);
				// Get the current date
				Calendar calendar = Calendar.getInstance();
				Date now = new Date();
				if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					// Saturday
					now = addDaysToDate(now, 2);
				}
				if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					// Sunday
					now = addDaysToDate(now, 1);
				}
				try {
					if (trainingWeekAray != null) {
						for (int j = 0; j < trainingWeekAray.length; j++) {
							Date startDate = dateFormat
									.parse(trainingWeekAray[j].getStart_date());
							Date endDate = dateFormat.parse(trainingWeekAray[j]
									.getEnd_date());
							// System.out.println("Start Date >>>> " +
							// dateFormat.format(startDate));
							// System.out.println("End Date >>>> " +
							// dateFormat.format(endDate));
							if (now.after(startDate) && now.before(endDate)) {
								weekId = trainingWeekAray[j].getWeek_id();
							} else if (dateFormat.format(now).equals(
									dateFormat.format(startDate))) {
								weekId = trainingWeekAray[j].getWeek_id();
							} else if (dateFormat.format(now).equals(
									dateFormat.format(endDate))) {
								weekId = trainingWeekAray[j].getWeek_id();
							}
						}
					}
				} catch (Exception ex) {
				}
			} else {
				if (getRequest().getParameter("weekId") != null) {
					weekId = getRequest().getParameter("weekId");
				} else if (request.getSession().getAttribute("weekId") != null) {
					weekId = (String) request.getSession().getAttribute(
							"weekId");
					// getSession().removeAttribute("weekId");
				}
			}
			// System.out.println("Week id here is ######################## " +
			// weekId);
			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}
			form.setWeek_id(weekId);
			RBUSHandler handler = new RBUSHandler();
			TrainingWeeks[] trainingWeekArayDropDown;
			String query = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS  where week_id in (select distinct week_id from v_rbu_class_roster_report) order by week_id asc";
			trainingWeekArayDropDown = trDb.getTrainingWeeks(query);
			List ts = handler.getUnassignedEmployees(weekId);
			RBUUnassignedEmployeesWc main = new RBUUnassignedEmployeesWc(ts,
					AppConst.EVENTID_RBU, trainingWeekArayDropDown);
			main.setRbuUnAssignedEmployeeForm(form);
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page1;
				page1 = new BlankTemplateWpc(main);
				getRequest().setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME,
						page1);
				// getRequest().setAttribute("exceldownload","true");
				getResponse()
						.setHeader("content-disposition",
								"attachment;filename=\"RBU-UnassignedTablesReport.xls\"");
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
			request.getSession().setAttribute("weekId", weekId);
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
	 * @jpf:forward name="success" path="RBUClassRoomReport.do"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward rbutraineedtableupdate(){
	 */

	public String rbutraineedtableupdate() {
		try {
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}
			String class_id = getRequest().getParameter("class_id");
			String room_id = getRequest().getParameter("room_id");
			String table_id = getRequest().getParameter("table_id");

			String tts = getRequest().getParameter("emplist");
			String gts = getRequest().getParameter("guestlist");

			String[] glist;
			String[] tlist;

			Map gmap = new HashMap();
			Map tmap = new HashMap();

			if (tts != null) {
				tlist = tts.split(",");
				for (int i = 0; i < tlist.length; i++) {
					tmap.put(tlist[i], getRequest()
							.getParameter("t" + tlist[i]));
				}
			}
			if (gts != null) {
				glist = gts.split(",");

				for (int i = 0; i < glist.length; i++) {
					gmap.put(glist[i], getRequest()
							.getParameter("g" + glist[i]));
				}
			}
			// to do - empty user

			RBUSHandler handler = new RBUSHandler();
			handler.updateTable(gmap, tmap, class_id, user.getId(), room_id,
					table_id);

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
	 * @jpf:forward name="success" path="rbuUnassignedEmployeesReport.do"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward rbuAssignEmployeesToTables(){
	 */

	public String rbuAssignEmployeesToTables() {
		try {
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}
			String week_id = getRequest().getParameter("weekId");
			if (request.getSession().getAttribute("weekId") != null) {
				request.getSession().removeAttribute("weekId");
			}
			request.getSession().setAttribute("weekId", week_id);
			String tts = getRequest().getParameter("emplist");
			String gts = getRequest().getParameter("guestlist");
			String[] tlist;
			List employeesToUpdate = new ArrayList();
			List guestTrainersToUpdate = new ArrayList();
			if (tts != null) {
				tlist = tts.split(",");
				for (int i = 0; i < tlist.length; i++) {
					RBUUnassignedEmployees employees = new RBUUnassignedEmployees();
					StringTokenizer token = new StringTokenizer(tlist[i], "_");
					while (token.hasMoreTokens()) {
						String emplId = token.nextToken();
						String classId = token.nextToken();
						String isTrainer = token.nextToken();
						// System.out.println("emplid ##############" + emplId +
						// "ClassId =  " + classId + "Is trainer " + isTrainer);
						employees.setEmplId(emplId);
						employees.setClassId(classId);
						employees.setIsTrainer(isTrainer);
					}
					String selectedTable = getRequest().getParameter(
							"t" + employees.getEmplId()
									+ employees.getClassId());
					// System.out.println("Table is selected ################ "
					// +
					// selectedTable);
					employees.setTableId(selectedTable);
					if (!selectedTable.equals("-1")) {
						if (employees.getIsTrainer().equals("N")) {
							employeesToUpdate.add(employees);
						} else {
							guestTrainersToUpdate.add(employees);
						}
					}
				}
			}
			RBUSHandler handler = new RBUSHandler();
			handler.assignEmployeesToTables(employeesToUpdate,
					guestTrainersToUpdate, user.getId());
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
	 * @jpf:forward name="success" path="RBUClassRoomReport.do"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward unassigngt(){
	 */

	public String unassigngt() {
		try {
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}
			String class_id = getRequest().getParameter("class_id");
			String table_id = getRequest().getParameter("table_id");

			String gid = getRequest().getParameter("gid");

			// to do - empty user

			RBUSHandler handler = new RBUSHandler();
			// handler.updateTable(gmap, tmap, class_id, user.getId(), room_id,
			// table_id);
			handler.deleteGTByTable(class_id, table_id, gid);

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
	 * This method adds days to a given date
	 */
	private Date addDaysToDate(Date date, int daysToAdd) {
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DAY_OF_MONTH, daysToAdd);
		return now.getTime();
	}

	/**
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 * @jpf:action
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward displayClassRosterReport(){
	 */

	public String displayClassRosterReport() {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			HttpServletRequest req = this.getRequest();
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;
			uSession = UserSession.getUserSession(getRequest());
			String firstTime = "";
			if (req.getParameter("firstTime") != null) {
				firstTime = (String) req.getParameter("firstTime");
			}
			// Training week array to be displayed in the drop down.
			TrainingWeeks[] trainingWeekArayDropDown;
			String query = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS  where week_id in (select distinct week_id from v_rbu_class_roster_report) order by week_id asc";
			trainingWeekArayDropDown = trDb.getTrainingWeeks(query);
			if (firstTime.equals("true")) {
				TrainingWeeks[] trainingWeekAray;
				String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
				trainingWeekAray = trDb.getTrainingWeeks(sql);
				String WeekId = "";
				// Get the current date
				Calendar calendar = Calendar.getInstance();
				Date now = new Date();
				if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					// Saturday
					now = addDaysToDate(now, 2);
				}
				if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					// Sunday
					now = addDaysToDate(now, 1);
				}

				// System.out.println("Date current ############### " +
				// dateFormat.format(now));
				try {
					if (trainingWeekAray != null) {
						for (int j = 0; j < trainingWeekAray.length; j++) {

							Date startDate = dateFormat
									.parse(trainingWeekAray[j].getStart_date());
							Date endDate = dateFormat.parse(trainingWeekAray[j]
									.getEnd_date());
							// System.out.println("Start Date >>>> " +
							// dateFormat.format(startDate));
							// System.out.println("End Date >>>> " +
							// dateFormat.format(endDate));
							if (now.after(startDate) && now.before(endDate)) {
								WeekId = trainingWeekAray[j].getWeek_id();
							} else if (dateFormat.format(now).equals(
									dateFormat.format(startDate))) {
								WeekId = trainingWeekAray[j].getWeek_id();
							} else if (dateFormat.format(now).equals(
									dateFormat.format(endDate))) {
								WeekId = trainingWeekAray[j].getWeek_id();
							}
						}
					}
				} catch (Exception ex) {
				}
				// System.out.println("Week Id on load ############### " +
				// WeekId);
				RBUClassRosterBean[] rbuClassRosterBeanArray;
				StringBuffer sbr = new StringBuffer();
				sbr.append(" Select product_desc AS Product, to_char(START_DATE, 'MM/DD/YYYY') as startDate, emplid, first_name as firstName, last_name as lastName, territory_role_cd AS Role_CD, is_trainer as isTrainer,");
				sbr.append(" room_name as roomName, table_id as tableNumber FROM V_RBU_class_roster_report");
				sbr.append(" WHERE week_id='" + WeekId
						+ "'   ORDER BY start_date, product_desc,  table_id");
				System.out.println("Query ############### " + sbr.toString());
				rbuClassRosterBeanArray = trDb.getClassRosterReport(sbr
						.toString());
				String displayResult = "Y";
				// RBUClassRosterReportWc main = new
				// RBUClassRosterReportWc(rbuClassRosterBeanArray,
				// AppConst.EVENT_RBU, trainingWeekAray, displayResult);
				RBUClassRosterReportWc main = new RBUClassRosterReportWc(
						rbuClassRosterBeanArray, AppConst.EVENT_RBU,
						trainingWeekArayDropDown, displayResult);
				/*
				 * if ("true".equalsIgnoreCase(downloadExcel)) {
				 * WebPageComponent page; page = new BlankTemplateWpc(main);
				 * getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME,
				 * page ); // getRequest().setAttribute("exceldownload","true");
				 * getResponse().setHeader("content-disposition",
				 * "attachment;filename=\"RBU-ClassRosterReport.xls\"");
				 * getResponse().setContentType("application/vnd.ms-excel");
				 * getResponse().setHeader("Cache-Control","max-age=0"); //HTTP
				 * 1.1 getResponse().setHeader("Pragma","public"); return new
				 * String("successXls"); }
				 */
				MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
						AppConst.EVENT_RBU, "RBUREPORT");
				page.setMain(main);
				getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
				if (request.getSession().getAttribute("weekId") != null) {
					request.getSession().removeAttribute("weekId");
				}
				request.getSession().setAttribute("weekId", WeekId);
			}
			/*
			 * Date today = new Date(); SimpleDateFormat dateFormat1 = new
			 * SimpleDateFormat("MM/dd/yyyy"); try{ for(int
			 * j=0;j<trainingWeekAray.length;j++){ //
			 * if(trainingWeek[j].getStart_date().equals(newDate)){ Date
			 * beginDate =
			 * dateFormat1.parse(trainingWeekAray[j].getStart_date()); Date
			 * finishDate =
			 * dateFormat1.parse(trainingWeekAray[j].getEnd_date());
			 * if(today.after(beginDate) && today.before(finishDate)){ WeekId =
			 * trainingWeekAray[j].getWeek_id(); } }
			 * 
			 * } catch(Exception ex){ }
			 */
			else {
				TrainingWeeks[] trainingWeekAray;
				String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
				trainingWeekAray = trDb.getTrainingWeeks(sql);
				String WeekId = req.getParameter("WeekId") == null ? "" : req
						.getParameter("WeekId");
				System.out.println("Week id from request ##################"
						+ WeekId);
				// WeekId = "1";
				String downloadExcel = req.getParameter("downloadExcel") == null ? ""
						: req.getParameter("downloadExcel");
				RBUClassRosterBean[] rbuClassRosterBeanArray;
				StringBuffer sbr = new StringBuffer();
				RBUClassRosterReportWc main = null;
				String displayResult = "Y";
				if (!"true".equalsIgnoreCase(downloadExcel)) {
					sbr.append(" Select product_desc AS Product, to_char(START_DATE, 'MM/DD/YYYY') as startDate, emplid, first_name as firstName, last_name as lastName, territory_role_cd AS Role_CD, is_trainer as isTrainer,");
					sbr.append(" room_name as roomName, table_id as tableNumber FROM V_RBU_class_roster_report");
					sbr.append(" WHERE week_id='"
							+ WeekId
							+ "'   ORDER BY start_date, product_desc,  table_id");
					rbuClassRosterBeanArray = trDb.getClassRosterReport(sbr
							.toString());
					// main = new
					// RBUClassRosterReportWc(rbuClassRosterBeanArray,
					// AppConst.EVENT_RBU, trainingWeekAray, displayResult);
					main = new RBUClassRosterReportWc(rbuClassRosterBeanArray,
							AppConst.EVENT_RBU, trainingWeekArayDropDown,
							displayResult);
				}
				if ("true".equalsIgnoreCase(downloadExcel)) {
					String employeeNumber = "";
					String output = "";
					if (this.getRequest().getParameter("orderNumber") != null) {
						employeeNumber = this.getRequest().getParameter(
								"orderNumber");
					}
					if (employeeNumber != null && !employeeNumber.equals("")
							&& employeeNumber.length() > 0) {
						StringTokenizer token = new StringTokenizer(
								employeeNumber, ",");

						while (token.hasMoreTokens()) {
							output = output + "'" + token.nextToken() + "'";
							output = output + ",";
						}
						output = output.substring(0, output.length() - 1);
					}
					sbr.append(" Select product_desc AS Product, to_char(START_DATE, 'MM/DD/YYYY') as startDate, emplid, first_name as firstName, last_name as lastName, territory_role_cd AS Role_CD, is_trainer as isTrainer,");
					sbr.append(" room_name as roomName, table_id as tableNumber FROM V_RBU_class_roster_report");
					sbr.append(" WHERE week_id='" + WeekId + "'");
					if (output != null && !output.equals("")
							&& output.length() > 0) {
						sbr.append(" and emplid in (" + output.trim() + ")");
					}
					sbr.append(" ORDER BY start_date, product_desc,  table_id");
					System.out.println("Query in download excel ######## "
							+ sbr.toString());
					rbuClassRosterBeanArray = trDb.getClassRosterReport(sbr
							.toString());
					main = new RBUClassRosterReportWc(rbuClassRosterBeanArray,
							AppConst.EVENT_RBU, trainingWeekAray, displayResult);
					WebPageComponent page;
					page = new BlankTemplateWpc(main);
					getRequest().setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME,
							page);
					// getRequest().setAttribute("exceldownload","true");
					getResponse()
							.setHeader("content-disposition",
									"attachment;filename=\"RBU-ClassRosterReport.xls\"");
					getResponse().setContentType("application/vnd.ms-excel");
					getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																			// 1.1
					getResponse().setHeader("Pragma", "public");
					/**
					 * <!-- Infosys - Weblogic to Jboss migration changes start
					 * here --> return new Forward("success");
					 */
					return new String("successXls");
					/**
					 * <!-- Infosys - Weblogic to Jboss migration changes ends
					 * here -->
					 */

				}
				MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
						AppConst.EVENT_RBU, "RBUREPORT");
				page.setMain(main);
				getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
				if (request.getSession().getAttribute("weekId") != null) {
					request.getSession().removeAttribute("weekId");
				}
				request.getSession().setAttribute("weekId", WeekId);
			}
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 * @jpf:action
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward getClassRosterReport(){
	 */

	public String getClassRosterReport() {
		try {
			HttpServletRequest req = this.getRequest();
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;
			uSession = UserSession.getUserSession(getRequest());
			String sql = "SELECT WEEK_ID as WEEK_ID, to_char(START_DATE, 'MM/DD/YYYY') as START_DATE, to_char(END_DATE, 'MM/DD/YYYY')  as END_DATE, WEEK_NAME as WEEK_NAME from RBU_TRAINING_WEEKS order by week_id asc";
			TrainingWeeks[] trainingWeekAray;
			trainingWeekAray = trDb.getTrainingWeeks(sql);
			String displayResult = "N";
			RBUClassRosterReportWc main = new RBUClassRosterReportWc(null,
					AppConst.EVENT_RBU, trainingWeekAray, displayResult);
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			this.getRequest().setAttribute("classWeeks", trainingWeekAray);
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
	 * ActionSupport get and set methods may be overwritten by the Form Bean
	 * editor.
	 */
	public static class AddGuestForm extends ActionSupport {
		private String nt_id;

		private String nt_domain;

		private String classid;

		private String enrolledby;

		private String email;

		private String lastname;

		private String firstname;

		private String emplid;

		public void setEmplid(String emplid) {
			this.emplid = emplid;
		}

		public String getEmplid() {
			return this.emplid;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getFirstname() {
			return this.firstname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public String getLastname() {
			return this.lastname;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getEmail() {
			return this.email;
		}

		public void setEnrolledby(String enrolledby) {
			this.enrolledby = enrolledby;
		}

		public String getEnrolledby() {
			return this.enrolledby;
		}

		public void setClassid(String classid) {
			this.classid = classid;
		}

		public String getClassid() {
			return this.classid;
		}

		public void setNt_domain(String nt_domain) {
			this.nt_domain = nt_domain;
		}

		public String getNt_domain() {
			return this.nt_domain;
		}

		public void setNt_id(String nt_id) {
			this.nt_id = nt_id;
		}

		public String getNt_id() {
			return this.nt_id;
		}
	}

	/**
	 * ActionSupport get and set methods may be overwritten by the Form Bean
	 * editor.
	 */
	

	/**
	 * getFilteredChart This method represents the point of entry into the
	 * pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward beginRBUChart(){
	 */

	public String beginRBUChart() {
		try {
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			String action = "";
			if (this.getRequest().getParameter("firstTime") != null) {
				action = this.getRequest().getParameter("firstTime");
			}
			RBUSHandler handler = new RBUSHandler();
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "RBUChart");
			boolean fromFilter = false;
			PieChart pieChart = null;
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"DASHBOARDREPORT");
			((HeaderDashBoardReportsWc) page.getHeader()).setHeadString(handler
					.getHeaderString());
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			ServiceFactory factory = Service.getServiceFactory(trDb);
			RBUPieChartHandler rbuHandler = factory.getRBUPieChartHandler();
			UserFilter uFilter = uSession.getUserFilter();
			// Get the product list
			productDataBean = trDb.getProducts();
			// Get the BU list
			buDataBean = trDb.getBu();
			// Get the rbu list
			rbuDataBean = trDb.getRbu();
			String selectedProduct = "";
			String selectedBU = "";
			String selectedRBU = "";
			if (action.equals("true")) {
				this.getRequest().setAttribute("OVERALL", "OVERALL");
				selectedProduct = "OVERALL";
				selectedBU = "ALL";
				selectedRBU = "ALL";
			}

			// Changes for data visibility
			// Check the existance of NT ID

			User user = uSession.getUser();
			String emplid = "";
			// System.out.println("################# User emplid " +
			// user.getEmplid());

			emplid = handler.getNTIdExistance(user.getEmplid());
			if (emplid.equals("NF")) {
				// System.out.println("################ User not found");
				emplid = "";
			}
			// Total Candidates Participating:
			int totalParticipants = rbuHandler.getOverAllTotalCount("Overall",
					"", "");
			// System.out.println("THE TOTAL PARTICIPANT HERE IS"+totalParticipants);
			Territory terr = Service.getServiceFactory(trDb)
					.getTerritoryHandler().getTerritory(uFilter);

			List charts = new ArrayList();

			PieChartBuilder pBuilder = new PieChartBuilder();

			qStrings.setSection("");
			RBUChartBean[] thisRBUChartBean;
			ChartDetailWc chartdetail = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);

			if (action.equals("true")) {
				// Get The Count For the Overall
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredRBUOverallChart("Overall", "", "", "",
								emplid);
				pieChart = pBuilder.getRBUSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean, "Overall",
						fromFilter);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_RBU, "Overall"));
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);
			}
			// this component puts the header info in the chart page.
			RBUChartHeaderWc chartHeaderWc = new RBUChartHeaderWc(
					selectedProduct, selectedBU, selectedRBU,
					pieChart.getCount());
			// ChartHeaderWc chartHeaderWc = new ChartHeaderWc(
			// terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(),
			// totalParticipants, uFilter.getProduct(),teamDesc);

			RBUChartsWc chartpage = new RBUChartsWc(uSession.getUser(),
					chartHeaderWc);
			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setProductDataBean(productDataBean);
			chartpage.setFutureAllignmentBuDataBean(buDataBean);
			chartpage.setFutureAllignmentRBUDataBean(rbuDataBean);
			chartpage.setFirstRequest("OVERALL");
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);

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
	 * This method represents the point of entry into the pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward getFilteredChart(){
	 */

	public String getFilteredChart() {
		try {
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			String action = this.getRequest().getParameter("firstTime");
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "RBUChart");
			boolean fromFilter = false;
			String selectedProduct = "";
			String selectedProductDesc = "";
			String selectedBU = "";
			String selectedRBU = "";
			System.out.println("selectedProduct >> " + selectedProduct
					+ "selectedProductDesc >> " + selectedProductDesc
					+ "selectedBU" + selectedBU + "selectedRBU " + selectedRBU);
			if (this.getRequest().getParameter("selectedProduct") != null) {
				selectedProduct = this.getRequest().getParameter(
						"selectedProduct");

			}
			if (this.getRequest().getParameter("selectedProductDesc") != null) {
				selectedProductDesc = this.getRequest().getParameter(
						"selectedProductDesc");

			}
			if (this.getRequest().getParameter("selectedBU") != null) {
				selectedBU = this.getRequest().getParameter("selectedBU");

			}
			if (this.getRequest().getParameter("selectedRBU") != null) {
				selectedRBU = this.getRequest().getParameter("selectedRBU");

			}

			System.out.println("selectedProduct >> " + selectedProduct
					+ "selectedProductDesc >> " + selectedProductDesc
					+ "selectedBU" + selectedBU + "selectedRBU " + selectedRBU);
			PieChart pieChart = null;
			RBUSHandler handler = new RBUSHandler();
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"DASHBOARDREPORT");
			// ((HeaderDashBoardReportsWc)page.getHeader()).setHeadString(handler.getHeaderString());
			// ProductFilterForm filterForm = uSession.getProductFilterForm();
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			ServiceFactory factory = Service.getServiceFactory(trDb);
			RBUPieChartHandler rbuHandler = factory.getRBUPieChartHandler();
			UserFilter uFilter = uSession.getUserFilter();
			// Get the product List
			productDataBean = trDb.getProducts();
			// Get the bu list
			buDataBean = trDb.getBu();
			// Get the rbu list
			// IF the user has selected some bu then get the filtered rbu
			if (selectedBU.equals("ALL")) {
				rbuDataBean = trDb.getRbu();
			} else {
				rbuDataBean = trDb.getRbuForRBU(selectedBU);
			}
			// Total Candidates Participating:
			// int
			// totalParticipants=rbuHandler.getOverAllTotalCount(selectedProduct,selectedBU,selectedRBU);
			// this component puts the header info in the chart page.

			User user = uSession.getUser();
			String emplid = "";
			// System.out.println("################# User emplid " +
			// user.getEmplid());

			emplid = handler.getNTIdExistance(user.getEmplid());
			if (emplid.equals("NF")) {
				// System.out.println("################ User not found");
				emplid = "";
			}

			List charts = new ArrayList();

			PieChartBuilder pBuilder = new PieChartBuilder();

			qStrings.setSection("");
			RBUChartBean[] thisRBUChartBean;
			ChartDetailWc chartdetail = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			int totalCount = 0;
			if (selectedProduct.equalsIgnoreCase("ALL")) {
				// System.out.println("In selected product for all ##################################");
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredRBUOverallChart("Overall", "", selectedBU,
								selectedRBU, emplid);
				pieChart = pBuilder.getRBUSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean, "Overall",
						fromFilter);
				totalCount = pieChart.getCount();
				pieChart = null;
				// Get all the products
				for (int i = 0; i < productDataBean.length; i++) {
					ProductDataBean dataBean = productDataBean[i];
					String productCd = dataBean.getProductCd();
					String productDesc = dataBean.getProductDesc();
					// System.out.println("############################ ProductCD "
					// + productCd);
					thisRBUChartBean = (RBUChartBean[]) rbuHandler
							.getFilteredRBUChart(productCd, selectedBU,
									selectedRBU, emplid);
					pieChart = pBuilder.getRBUSChart(uSession.getUserFilter(),
							getRequest().getSession(), thisRBUChartBean,
							productDesc, fromFilter);
					chartdetail = new ChartDetailWc(pieChart,
							"Attendance chart", new ChartLegendWc(
									ChartLegendWc.LAYOUT_RBU,
									selectedProductDesc));
					if (pieChart.getCount() > 0)
						charts.add(chartdetail);
				}

			} else if (selectedProduct.equalsIgnoreCase("OVERALL")) {
				System.out
						.println("In selected product for OVERALL ##################################");
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredRBUOverallChart("Overall", "", selectedBU,
								selectedRBU, emplid);
				pieChart = pBuilder.getRBUSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean, "Overall",
						fromFilter);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_RBU, "Overall"));
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);
				totalCount = pieChart.getCount();
			} else {
				// Get the links that would be displayed
				List result = new ArrayList();
				result = handler.getLinksForProducts(selectedProduct);
				System.out
						.println("In selected product for else  ##################################");
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredRBUChart(selectedProduct, selectedBU,
								selectedRBU, emplid);
				pieChart = pBuilder.getRBUSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean,
						selectedProductDesc, fromFilter);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_RBU,
								selectedProductDesc), "showLink", result);
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);
				totalCount = pieChart.getCount();
			}
			ChartListWc chartListWc = new ChartListWc(charts);
			RBUChartHeaderWc chartHeaderWc = new RBUChartHeaderWc(
					selectedProductDesc, selectedBU, selectedRBU,
					pieChart.getCount());
			// ChartHeaderWc chartHeaderWc = new ChartHeaderWc(
			// terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(),
			// totalParticipants, uFilter.getProduct(),teamDesc);

			RBUChartsWc chartpage = new RBUChartsWc(uSession.getUser(),
					chartHeaderWc);
			// Add the Overall Trainees;
			// System.out.println("Getting the count in filtered chart ############## "
			// + pieChart.getCount());
			chartListWc.setTrainees(totalCount);
			chartpage.setProductDataBean(productDataBean);
			chartpage.setFutureAllignmentBuDataBean(buDataBean);
			chartpage.setFutureAllignmentRBUDataBean(rbuDataBean);
			// chartpage.setFirstRequest("OVERALL");
			chartpage.setSelectedProduct(selectedProduct);
			chartpage.setSelectedBu(selectedBU);
			chartpage.setSelectedRbu(selectedRBU);
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);

			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			// Set the selections in session
			this.request.getSession().setAttribute("selectedBU", selectedBU);
			this.request.getSession().setAttribute("selectedRBU", selectedRBU);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			/*
			 * getRequest().setAttribute("products", productDataBean);
			 * getRequest().setAttribute("bu", buDataBean);
			 * getRequest().setAttribute("rbu", rbuDataBean);
			 * getRequest().setAttribute("OVERALL", "OVERALL");
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
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward getRBU(){
	 */

	public String getRBU() {
		try {
			HttpServletRequest req = this.getRequest();
			String bu = "Primary Care";
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
				rbuBean = trDb.getRbu();
			} else {
				rbuBean = trDb.getRbuForRBU(req.getParameter("bu"));
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
			if (result.toString() != null) {
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
			Global.getError(getRequest(), e);
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
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward listreportwithreportto(){
	 */

	public String listreportwithreportto() {
		try {

			if (getResponse().isCommitted()) {
				return null;
			}

			Employee[] rbuEmployee = null;

			ServiceFactory factory = Service.getServiceFactory(trDb);
			RBUEmployeeHandler eHandler = factory.getRBUEmployeeHandler();
			WebPageComponent page;
			String selectedBU = "ALL";
			String selectedRBU = "ALL";
			String selectedSection = "Not Complete";
			String reportto = this.getRequest().getParameter("emplid");
			String fromRequest = "";
			if (this.getRequest().getParameter("fromRequest") != null) {
				fromRequest = this.getRequest().getParameter("fromRequest");
			}
			if (this.request.getSession().getAttribute("selectedBU") != null) {
				selectedBU = (String) this.request.getSession().getAttribute(
						"selectedBU");
				this.request.getSession().removeAttribute("selectedBU");
			}
			if (this.request.getSession().getAttribute("selectedRBU") != null) {
				selectedRBU = (String) this.request.getSession().getAttribute(
						"selectedRBU");
				this.request.getSession().removeAttribute("selectedRBU");
			}

			// Get the product list
			productDataBean = trDb.getProducts();
			// Get the BU list
			buDataBean = trDb.getBu();
			if (selectedBU.equals("ALL")) {
				// Get the rbu list
				rbuDataBean = trDb.getRbu();
			} else {
				rbuDataBean = trDb.getRbuForRBU(selectedBU);
			}
			// check if coming from email, if so reset search to
			// clear all filters in session.
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
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

			// System.out.println("Value of selectedBU #########   " +
			// selectedBU +
			// "selectedRBU####   " + selectedRBU);
			PieChart pieChart = null;

			OverallProcessor overall = uSession.getOverallProcessor();

			PieChartBuilder pBuilder = new PieChartBuilder();
			RBUChartBean[] thisRBUChartBean;
			boolean fromFilter = false;
			RBUPieChartHandler rbuHandler = factory.getRBUPieChartHandler();
			ChartDetailWc chart = null;
			String productCD = "";
			String type = "";
			String downloadExcel = "";
			String excelType = "";
			String excelSection = "";
			String prodDesc = "";

			RBUSHandler handler = new RBUSHandler();
			User user = uSession.getUser();
			String emplid = "";
			// System.out.println("################# User emplid " +
			// user.getEmplid());

			emplid = handler.getNTIdExistance(user.getEmplid());
			if (emplid.equals("NF")) {
				// System.out.println("################ User not found");
				emplid = "";
			}
			if (fromRequest.equals("reRun")) {

				if (this.getRequest().getParameter("selectedProductDesc") != null) {
					prodDesc = this.getRequest().getParameter(
							"selectedProductDesc");
				}
				productCD = prodDesc;

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
			if (this.getRequest().getParameter("excelType") != null) {
				excelType = this.getRequest().getParameter("excelType");
			}
			if (this.getRequest().getParameter("excelSection") != null) {
				excelSection = this.getRequest().getParameter("excelSection");
			}
			String displayType = productCD;
			System.out.println("### From Request " + fromRequest + "Type "
					+ type + "Section " + selectedSection + "ProductCD "
					+ productCD + "selectedBU" + selectedBU + "selectedRBU"
					+ selectedRBU);
			if (productCD.equalsIgnoreCase("Aricept PC"))
				productCD = "ARCPPC";
			if (productCD.equalsIgnoreCase("Aricept SM"))
				productCD = "ARCPSM";
			if (productCD.equalsIgnoreCase("Caduet"))
				productCD = "CADT";
			if (productCD.equalsIgnoreCase("Chantix"))
				productCD = "CHTX";
			if (productCD.equalsIgnoreCase("Celebrex"))
				productCD = "CLBR";
			if (productCD.equalsIgnoreCase("Eraxis"))
				productCD = "ERXS";
			if (productCD.equalsIgnoreCase("Geodon PC"))
				productCD = "GEODPC";
			if (productCD.equalsIgnoreCase("Geodon SM"))
				productCD = "GEODSM";
			if (productCD.equalsIgnoreCase("HS/L Toviaz"))
				productCD = "HSLTOVZ";
			if (productCD.equalsIgnoreCase("Lipitor"))
				productCD = "LPTR";
			if (productCD.equalsIgnoreCase("Lyrica PC"))
				productCD = "LYRCPC";
			if (productCD.equalsIgnoreCase("Lyrica SM"))
				productCD = "LYRCSM";
			if (productCD.equalsIgnoreCase("OAB Toviaz"))
				productCD = "OABTOVZ";
			if (productCD.equalsIgnoreCase("Selzentry"))
				productCD = "SLZN";
			if (productCD.equalsIgnoreCase("Spiriva"))
				productCD = "SPRV";
			if (productCD.equalsIgnoreCase("Vfend"))
				productCD = "VFEN";
			if (productCD.equalsIgnoreCase("Viagra"))
				productCD = "VIAG";
			if (productCD.equalsIgnoreCase("Zyvox"))
				productCD = "ZYVX";

			if (!type.equalsIgnoreCase("Overall")) {
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredRBUChart(productCD, selectedBU,
								selectedRBU, emplid);
				uSession.getUserFilter().setProdcut(type);
				pieChart = pBuilder.getRBUSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean, type,
						fromFilter);
				chart = new ChartDetailWc(
						pieChart,
						"Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_RBU, displayType));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("Overall".equalsIgnoreCase(type)) {
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredRBUOverallChart("Overall", "", selectedBU,
								selectedRBU, emplid);
				uSession.getUserFilter().setProdcut("Overall");
				pieChart = pBuilder.getRBUSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean, "Overall",
						fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_RBU, "Overall"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("true".equals(downloadExcel)) {
				if ("Overall".equalsIgnoreCase(excelType)) {
					// excelRBUEmployee
					// =eHandler.getRBUOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
					// selectedBU, selectedRBU);
					excelRBUEmployee = eHandler.getRBUOverAllEmployees(uFilter,
							reportto, excelSection, excelSection, selectedBU,
							selectedRBU);
				} else {
					// excelRBUEmployee
					// =eHandler.getRBUEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
					// selectedBU, selectedRBU);
					excelRBUEmployee = eHandler.getRBUEmployees(uFilter,
							reportto, excelType, excelSection, selectedBU,
							selectedRBU);
				}
				ListReportWpc reportList = new ListReportWpc(
						uSession.getUser(), uFilter, chart, excelRBUEmployee,
						"RBUREPORT", productDataBean, buDataBean, rbuDataBean);
				page = new ListReportWpc(uSession.getUser(), uFilter, chart,
						excelRBUEmployee, true, "RBUREPORT", productDataBean);
				// page = new ListReportWpc( uSession.getUser(), uFilter,
				// chart,excelRBUEmployee,"RBUREPORT", productDataBean,
				// buDataBean,rbuDataBean);
				request.getSession().setAttribute("xlsBean", excelRBUEmployee);
				request.getSession().setAttribute("xlsType", excelType);
				request.getSession().setAttribute("section", excelSection);
				request.getSession().setAttribute("product", productDataBean);
				System.out.println("excelType in download excel #############"
						+ excelType);
				if (excelType.equalsIgnoreCase("Aricept PC"))
					excelType = "ARCPPC";
				if (excelType.equalsIgnoreCase("Aricept SM"))
					excelType = "ARCPSM";
				if (excelType.equalsIgnoreCase("Caduet"))
					excelType = "CADT";
				if (excelType.equalsIgnoreCase("Chantix"))
					excelType = "CHTX";
				if (excelType.equalsIgnoreCase("Celebrex"))
					excelType = "CLBR";
				if (excelType.equalsIgnoreCase("Eraxis"))
					excelType = "ERXS";
				if (excelType.equalsIgnoreCase("Geodon PC"))
					excelType = "GEODPC";
				if (excelType.equalsIgnoreCase("Geodon SM"))
					excelType = "GEODSM";
				if (excelType.equalsIgnoreCase("HS/L Toviaz"))
					excelType = "HSLTOVZ";
				if (excelType.equalsIgnoreCase("Lipitor"))
					excelType = "LPTR";
				if (excelType.equalsIgnoreCase("Lyrica PC"))
					excelType = "LYRCPC";
				if (excelType.equalsIgnoreCase("Lyrica SM"))
					excelType = "LYRCSM";
				if (excelType.equalsIgnoreCase("OAB Toviaz"))
					excelType = "OABTOVZ";
				if (excelType.equalsIgnoreCase("Selzentry"))
					excelType = "SLZN";
				if (excelType.equalsIgnoreCase("Spiriva"))
					excelType = "SPRV";
				if (excelType.equalsIgnoreCase("Vfend"))
					excelType = "VFEN";
				if (excelType.equalsIgnoreCase("Viagra"))
					excelType = "VIAG";
				if (excelType.equalsIgnoreCase("Zyvox"))
					excelType = "ZYVX";
				List examList = new ArrayList();
				if (!"Overall".equalsIgnoreCase(excelType)) {
					// Get the list of examps for this product
					examList = eHandler.getExamList(excelType);
					request.getSession().setAttribute("examList", examList);
				}
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
			if ("Overall".equalsIgnoreCase(type)) {
				// System.out.println("Getting the employee list over all ##################################");
				// rbuEmployee
				// =eHandler.getRBUOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
				// selectedBU, selectedRBU);
				rbuEmployee = eHandler.getRBUOverAllEmployees(uFilter,
						reportto, type, selectedSection, selectedBU,
						selectedRBU);
			} else {
				// rbuEmployee
				// =eHandler.getRBUEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
				// selectedBU, selectedRBU);
				rbuEmployee = eHandler.getRBUEmployees(uFilter, reportto, type,
						selectedSection, selectedBU, selectedRBU);
			}
			// System.out.println("After Getting the employee list ##################################");
			List examList = new ArrayList();
			if (!"Overall".equalsIgnoreCase(type)) {
				// Get the list of examps for this product
				examList = eHandler.getExamList(productCD);
				request.getSession().setAttribute("examList", examList);
			}
			if (pieChart.getCount() == 0)
				chart = null;
			page = new ListReportWpc(uSession.getUser(), uFilter, chart,
					rbuEmployee, "RBUREPORT", productDataBean, buDataBean,
					rbuDataBean);
			uFilter.setClusterCode(uSession.getUser().getCluster());
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			getRequest().setAttribute("type", qStrings.getType());
			uSession.setOverallProcessor(null);
			if (request.getSession().getAttribute("selectedProduct") != null) {
				request.getSession().removeAttribute("selectedProduct");
			}
			if (request.getSession().getAttribute("selectedBU") != null) {
				request.getSession().removeAttribute("selectedBU");
			}
			if (request.getSession().getAttribute("selectedRBU") != null) {
				request.getSession().removeAttribute("selectedRBU");
			}
			if (request.getSession().getAttribute("selectedSection") != null) {
				request.getSession().removeAttribute("selectedSection");
			}
			if (request.getSession().getAttribute("type") != null) {
				request.getSession().removeAttribute("type");
			}
			request.getSession().setAttribute("selectedProduct", productCD);
			request.getSession().setAttribute("selectedBU", selectedBU);
			request.getSession().setAttribute("selectedRBU", selectedRBU);
			request.getSession().setAttribute("selectedSection",
					selectedSection);
			request.getSession().setAttribute("type", type);
			request.getSession().setAttribute("emplid", reportto);

			request.getSession().setAttribute("special", "t");
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
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/components/report/RBUReportListXls.jsp"
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward listreport(){
	 */

	public String listreport() {
		try {

			System.out
					.println("Inside list report ##joe#####################################");
			if (getResponse().isCommitted()) {
				return null;
			}

			Employee[] rbuEmployee = null;

			ServiceFactory factory = Service.getServiceFactory(trDb);
			RBUEmployeeHandler eHandler = factory.getRBUEmployeeHandler();
			ListReportWpc page;
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

			emplid = handler.getNTIdExistance(user.getEmplid());
			if (emplid.equals("NF")) {
				// System.out.println("################ User not found");
				emplid = "";
			}
			if (this.getRequest().getParameter("fromRequest") != null) {
				fromRequest = this.getRequest().getParameter("fromRequest");
			}
			if (this.request.getSession().getAttribute("selectedBU") != null) {
				selectedBU = (String) this.request.getSession().getAttribute(
						"selectedBU");
				this.request.getSession().removeAttribute("selectedBU");
			}
			if (this.request.getSession().getAttribute("selectedRBU") != null) {
				selectedRBU = (String) this.request.getSession().getAttribute(
						"selectedRBU");
				this.request.getSession().removeAttribute("selectedRBU");
			}

			// Get the product list
			productDataBean = trDb.getProducts();
			// Get the BU list
			buDataBean = trDb.getBu();
			if (selectedBU.equals("ALL")) {
				// Get the rbu list
				rbuDataBean = trDb.getRbu();
			} else {
				rbuDataBean = trDb.getRbuForRBU(selectedBU);
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

			// System.out.println("Value of selectedBU #########   " +
			// selectedBU +
			// "selectedRBU####   " + selectedRBU);
			PieChart pieChart = null;

			OverallProcessor overall = uSession.getOverallProcessor();

			PieChartBuilder pBuilder = new PieChartBuilder();
			RBUChartBean[] thisRBUChartBean;
			boolean fromFilter = false;
			RBUPieChartHandler rbuHandler = factory.getRBUPieChartHandler();
			ChartDetailWc chart = null;
			String productCD = "";
			String type = "";
			String downloadExcel = "";
			String excelType = "";
			String excelSection = "";
			String prodDesc = "";
			if (fromRequest.equals("reRun")) {

				if (this.getRequest().getParameter("selectedProductDesc") != null) {
					prodDesc = this.getRequest().getParameter(
							"selectedProductDesc");
				}
				productCD = prodDesc;
				if (this.request.getSession().getAttribute("selectedSection") != null) {
					selectedSection = (String) this.request.getSession()
							.getAttribute("selectedSection");
					this.request.getSession()
							.removeAttribute("selectedSection");
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
			if (this.getRequest().getParameter("excelType") != null) {
				excelType = this.getRequest().getParameter("excelType");
			}
			if (this.getRequest().getParameter("excelSection") != null) {
				excelSection = this.getRequest().getParameter("excelSection");
			}
			String displayType = productCD;
			System.out.println("### From Request " + fromRequest + "Type "
					+ type + "Section " + selectedSection + "ProductCD "
					+ productCD + "selectedBU" + selectedBU + "selectedRBU"
					+ selectedRBU);
			if (productCD.equalsIgnoreCase("Aricept PC"))
				productCD = "ARCPPC";
			if (productCD.equalsIgnoreCase("Aricept SM"))
				productCD = "ARCPSM";
			if (productCD.equalsIgnoreCase("Caduet"))
				productCD = "CADT";
			if (productCD.equalsIgnoreCase("Chantix"))
				productCD = "CHTX";
			if (productCD.equalsIgnoreCase("Celebrex"))
				productCD = "CLBR";
			if (productCD.equalsIgnoreCase("Eraxis"))
				productCD = "ERXS";
			if (productCD.equalsIgnoreCase("Geodon PC"))
				productCD = "GEODPC";
			if (productCD.equalsIgnoreCase("Geodon SM"))
				productCD = "GEODSM";
			if (productCD.equalsIgnoreCase("HS/L Toviaz"))
				productCD = "HSLTOVZ";
			if (productCD.equalsIgnoreCase("Lipitor"))
				productCD = "LPTR";
			if (productCD.equalsIgnoreCase("Lyrica PC"))
				productCD = "LYRCPC";
			if (productCD.equalsIgnoreCase("Lyrica SM"))
				productCD = "LYRCSM";
			if (productCD.equalsIgnoreCase("OAB Toviaz"))
				productCD = "OABTOVZ";
			if (productCD.equalsIgnoreCase("Selzentry"))
				productCD = "SLZN";
			if (productCD.equalsIgnoreCase("Spiriva"))
				productCD = "SPRV";
			if (productCD.equalsIgnoreCase("Vfend"))
				productCD = "VFEN";
			if (productCD.equalsIgnoreCase("Viagra"))
				productCD = "VIAG";
			if (productCD.equalsIgnoreCase("Zyvox"))
				productCD = "ZYVX";

			if (!type.equalsIgnoreCase("Overall")) {
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredRBUChart(productCD, selectedBU,
								selectedRBU, emplid);
				uSession.getUserFilter().setProdcut(type);
				pieChart = pBuilder.getRBUSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean,
						displayType, fromFilter);
				chart = new ChartDetailWc(
						pieChart,
						"Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_RBU, displayType));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("Overall".equalsIgnoreCase(type)) {
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredRBUOverallChart("Overall", "", selectedBU,
								selectedRBU, emplid);
				uSession.getUserFilter().setProdcut("Overall");
				pieChart = pBuilder.getRBUSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisRBUChartBean, "Overall",
						fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_RBU, "Overall"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("true".equals(downloadExcel)) {
				if ("Overall".equalsIgnoreCase(excelType)) {
					// excelRBUEmployee
					// =eHandler.getRBUOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
					// selectedBU, selectedRBU);
					excelRBUEmployee = eHandler.getRBUOverAllEmployees(uFilter,
							excelSection, excelSection, selectedBU,
							selectedRBU, "", emplid);
				} else {
					// excelRBUEmployee
					// =eHandler.getRBUEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
					// selectedBU, selectedRBU);
					excelRBUEmployee = eHandler.getRBUEmployees(uFilter,
							excelType, excelSection, selectedBU, selectedRBU,
							"", emplid);
				}
				ListReportWpc reportList = new ListReportWpc(
						uSession.getUser(), uFilter, chart, excelRBUEmployee,
						"RBUREPORT", productDataBean, buDataBean, rbuDataBean);
				page = new ListReportWpc(uSession.getUser(), uFilter, chart,
						excelRBUEmployee, true, "RBUREPORT", productDataBean);

				// ((HeaderDashBoardReportsWc)page.getHead()).setHeadString(handler.getHeaderString());
				// page = new ListReportWpc( uSession.getUser(), uFilter,
				// chart,excelRBUEmployee,"RBUREPORT", productDataBean,
				// buDataBean,rbuDataBean);
				request.getSession().setAttribute("xlsBean", excelRBUEmployee);
				request.getSession().setAttribute("xlsType", excelType);
				request.getSession().setAttribute("section", excelSection);
				request.getSession().setAttribute("product", productDataBean);
				System.out.println("excelType in download excel #############"
						+ excelType);
				if (excelType.equalsIgnoreCase("Aricept PC"))
					excelType = "ARCPPC";
				if (excelType.equalsIgnoreCase("Aricept SM"))
					excelType = "ARCPSM";
				if (excelType.equalsIgnoreCase("Caduet"))
					excelType = "CADT";
				if (excelType.equalsIgnoreCase("Chantix"))
					excelType = "CHTX";
				if (excelType.equalsIgnoreCase("Celebrex"))
					excelType = "CLBR";
				if (excelType.equalsIgnoreCase("Eraxis"))
					excelType = "ERXS";
				if (excelType.equalsIgnoreCase("Geodon PC"))
					excelType = "GEODPC";
				if (excelType.equalsIgnoreCase("Geodon SM"))
					excelType = "GEODSM";
				if (excelType.equalsIgnoreCase("HS/L Toviaz"))
					excelType = "HSLTOVZ";
				if (excelType.equalsIgnoreCase("Lipitor"))
					excelType = "LPTR";
				if (excelType.equalsIgnoreCase("Lyrica PC"))
					excelType = "LYRCPC";
				if (excelType.equalsIgnoreCase("Lyrica SM"))
					excelType = "LYRCSM";
				if (excelType.equalsIgnoreCase("OAB Toviaz"))
					excelType = "OABTOVZ";
				if (excelType.equalsIgnoreCase("Selzentry"))
					excelType = "SLZN";
				if (excelType.equalsIgnoreCase("Spiriva"))
					excelType = "SPRV";
				if (excelType.equalsIgnoreCase("Vfend"))
					excelType = "VFEN";
				if (excelType.equalsIgnoreCase("Viagra"))
					excelType = "VIAG";
				if (excelType.equalsIgnoreCase("Zyvox"))
					excelType = "ZYVX";
				List examList = new ArrayList();
				if (!"Overall".equalsIgnoreCase(excelType)) {
					// Get the list of examps for this product
					examList = eHandler.getExamList(excelType);
					request.getSession().setAttribute("examList", examList);
				}
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
			if ("Overall".equalsIgnoreCase(type)) {
				// System.out.println("Getting the employee list over all ##################################");
				// rbuEmployee
				// =eHandler.getRBUOverAllEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
				// selectedBU, selectedRBU);
				rbuEmployee = eHandler.getRBUOverAllEmployees(uFilter, type,
						selectedSection, selectedBU, selectedRBU, "", emplid);
			} else {
				// rbuEmployee
				// =eHandler.getRBUEmployees(uFilter,qStrings.getType(),qStrings.getSection(),
				// selectedBU, selectedRBU);
				rbuEmployee = eHandler.getRBUEmployees(uFilter, type,
						selectedSection, selectedBU, selectedRBU, "", emplid);
			}
			// System.out.println("After Getting the employee list ##################################");
			List examList = new ArrayList();
			if (!"Overall".equalsIgnoreCase(type)) {
				// Get the list of examps for this product
				examList = eHandler.getExamList(productCD);
				request.getSession().setAttribute("examList", examList);
			}
			if (pieChart.getCount() == 0)
				chart = null;
			page = new ListReportWpc(uSession.getUser(), uFilter, chart,
					rbuEmployee, "RBUREPORT", productDataBean, buDataBean,
					rbuDataBean);
			((HeaderAdminReportsWc) page.getHeader()).setHeadString(handler
					.getHeaderString());

			uFilter.setClusterCode(uSession.getUser().getCluster());
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			getRequest().setAttribute("type", qStrings.getType());
			uSession.setOverallProcessor(null);
			if (request.getSession().getAttribute("selectedProduct") != null) {
				request.getSession().removeAttribute("selectedProduct");
			}
			if (request.getSession().getAttribute("selectedBU") != null) {
				request.getSession().removeAttribute("selectedBU");
			}
			if (request.getSession().getAttribute("selectedRBU") != null) {
				request.getSession().removeAttribute("selectedRBU");
			}
			if (request.getSession().getAttribute("selectedSection") != null) {
				request.getSession().removeAttribute("selectedSection");
			}
			if (request.getSession().getAttribute("type") != null) {
				request.getSession().removeAttribute("type");
			}
			request.getSession().setAttribute("selectedProduct", productCD);
			request.getSession().setAttribute("selectedBU", selectedBU);
			request.getSession().setAttribute("selectedRBU", selectedRBU);
			request.getSession().setAttribute("selectedSection",
					selectedSection);
			request.getSession().setAttribute("type", type);
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
	 * @jpf:forward path="P2LEmailConfirmation.jsp" name="success"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward mailP2LRegistrationFile(){
	 */

	public String mailP2LRegistrationFile() {
		try {
			// Changes made for RBU Shipment
			HttpServletRequest req = this.getRequest();
			RBUHandlers rbu = new RBUHandlers();
			rbu.handleNotification(req.getRequestURL().toString());

			req.setAttribute("mail", "MailSent");
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
	 * @jpf:forward path="EnrollmentConfirmation.jsp" name="success"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward rbuEnrollmentProcess(){
	 */

	public String rbuEnrollmentProcess() {
		try {
			// Changes made for RBU Shipment
			RBUHandlers rbu = new RBUHandlers();
			rbu.rbuEnrollment();
			HttpServletRequest req = this.getRequest();
			req.setAttribute("enrollment", "Enrolled");
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
	 * @jpf:forward path="guestaccessConfirmation.jsp" name="success"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward rbuSCEGuestProcess(){
	 */

	public String rbuSCEGuestProcess() {
		try {
			// Changes made for RBU Shipment
			RBUHandlers rbu = new RBUHandlers();
			rbu.rbuSCEGT();

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
	 * @jpf:forward path="tableassignment.jsp" name="success"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward getavailableweeks(){
	 */

	public String getavailableweeks() {
		try {
			// Changes made for RBU Shipment
			RBUSHandler handler = new RBUSHandler();
			List weeks = handler.getClassWeeks();
			HttpServletRequest req = this.getRequest();
			req.setAttribute("weeks", weeks);
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
	 * @jpf:forward path="TableAssignmentConfirmation.jsp" name="success"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward rbuTableAssignmentProcess(){
	 */

	public String rbuTableAssignmentProcess() {
		try {
			// Changes made for RBU Shipment
			RBUHandlers rbu = new RBUHandlers();
			rbu.rbuEnrollment();
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			// HttpServletRequest req = this.getRequest();
			// req.setAttribute("enrollment", "Enrolled");
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward RBUEnrollmentChangeReport(){
	 */

	public String RBUEnrollmentChangeReport() {
		try {
			// Winny
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;

			uSession = UserSession.getUserSession(getRequest());

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE
				// );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
			}

			if (getResponse().isCommitted()) {
				return null;
			}

			String downloadExcel = getRequest().getParameter("downloadExcel");

			AdminReportHandler handler = new AdminReportHandler();
			RBUEnrollChangeReport[] empReport = handler
					.getRBUEnrollmentChangeReport();
			EmpReport empReportTotal = handler
					.getEnrollmentSummaryReportTotal(AppConst.EVENT_RBU);
			RBUEnrollmentChangeReportWc main = new RBUEnrollmentChangeReportWc(
					empReport, AppConst.EVENT_RBU);

			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"RBU - Enrollment Change Report.xls\"");

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
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_RBU, "RBUREPORT");
			page.setMain(main);
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
	 * ActionSupport get and set methods may be overwritten by the Form Bean
	 * editor.
	 */
	public static class RBUClassRoomReportForm extends ActionSupport {
		private List weeks = new ArrayList();

		private String week_id = "0";

		public void setWeek_id(String week_id) {
			this.week_id = week_id;
		}

		public String getWeek_id() {
			return this.week_id;
		}

		public void setWeeks(List weeks) {
			this.weeks = weeks;
		}

		public List getWeeks() {
			// For data binding to be able to post data back, complex types and
			// arrays must be initialized to be non-null.
			if (this.weeks == null) {
				this.weeks = new ArrayList();
			}

			return this.weeks;
		}
	}

	/**
	 * ActionSupport get and set methods may be overwritten by the Form Bean
	 * editor.
	 */
	/*public static class RbutraineetablemapForm extends ActionSupport {
		private String room_id;

		private String week_id;

		private String class_id;

		private String table_id;

		private String room;

		private String product;

		private String day;

		public void setDay(String day) {
			this.day = day;
		}

		public String getDay() {
			// For data binding to be able to post data back, complex types and
			// arrays must be initialized to be non-null. This type doesn't have
			// a default constructor, so Workshop cannot initialize it for you.

			// TODO: Initialize day if it is null.
			// if(this.day == null)
			// {
			// this.day = new Date(?);
			// }

			return this.day;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public String getProduct() {
			return this.product;
		}

		public void setRoom(String room) {
			this.room = room;
		}

		public String getRoom() {
			return this.room;
		}

		public void setTable_id(String table_id) {
			this.table_id = table_id;
		}

		public String getTable_id() {
			return this.table_id;
		}

		public void setClass_id(String class_id) {
			this.class_id = class_id;
		}

		public String getClass_id() {
			return this.class_id;
		}

		public void setWeek_id(String week_id) {
			this.week_id = week_id;
		}

		public String getWeek_id() {
			return this.week_id;
		}

		public void setRoom_id(String room_id) {
			this.room_id = room_id;
		}

		public String getRoom_id() {
			return this.room_id;
		}
	}*/

	/**
	 * ActionSupport get and set methods may be overwritten by the Form Bean
	 * editor.
	 */
	public static class RbuUnAssignedEmployeeForm extends ActionSupport {

		private String week_id;

		public void setWeek_id(String week_id) {
			this.week_id = week_id;
		}

		public String getWeek_id() {
			return this.week_id;
		}
	}
}
