package com.pfizer.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.Employee;
import com.pfizer.hander.RBUEmployeeHandler;
import com.pfizer.hander.RBUPieChartHandler;
import com.pfizer.hander.RBUSHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.webapp.chart.PieChartBuilder;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.RBU.RBUChartBean;
import com.pfizer.webapp.wc.ToviazLaunch.ToviazLaunchChartsWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.chart.ToviazLaunchChartHeaderWc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.html.FormUtil;
import com.tgix.rbu.FutureAllignmentBuDataBean;
import com.tgix.rbu.FutureAllignmentRBUDataBean;
import com.tgix.wc.WebPageComponent;

public class ToviazLaunchControllerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private TransactionDB trDb = new TransactionDB();
	public Employee[] excelRBUEmployee;
	public FutureAllignmentBuDataBean[] buDataBean;
	public FutureAllignmentRBUDataBean[] rbuDataBean;
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
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
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
			return beginToviazLaunch();
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
	 * getFilteredChart This method represents the point of entry into the
	 * pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward beginToviazLaunch(){
	 */
	public String beginToviazLaunch() {
		try {

			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}

			String action = "";
			if (this.getRequest().getParameter("firstTime") != null) {
				action = this.getRequest().getParameter("firstTime");
			}
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "RBUChart");
			boolean fromFilter = false;
			PieChart pieChart = null;
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"TOVIAZLAUNCH");
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			ServiceFactory factory = Service.getServiceFactory(trDb);
			RBUPieChartHandler rbuHandler = factory.getRBUPieChartHandler();
			UserFilter uFilter = uSession.getUserFilter();

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

			// Get the BU list
			buDataBean = trDb.getBu();
			// Get the rbu list
			rbuDataBean = trDb.getRbuForRBU("Primary Care");
			String selectedProduct = "";
			String selectedBU = "";
			String selectedRBU = "";
			if (action.equals("true")) {
				this.getRequest().setAttribute("Attendance", "Attendance");
				selectedBU = "Primary Care";
				selectedRBU = "ALL";
			}
			if (this.getRequest().getParameter("selectedBU") != null) {
				selectedBU = this.getRequest().getParameter("selectedBU");

			}
			if (this.getRequest().getParameter("selectedRBU") != null) {
				selectedRBU = this.getRequest().getParameter("selectedRBU");

			}
			List charts = new ArrayList();
			PieChartBuilder pBuilder = new PieChartBuilder();
			qStrings.setSection("");
			RBUChartBean[] thisRBUChartBean;
			ChartDetailWc chartdetail = null;
			// Get the PED exams for HSLTOVZ and OABTOVZ
			List exams = handler.getToviazExams("");
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			if (action.equals("true")) {
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchAttendanceChart("Attendance",
								"", "", "", emplid);
				pieChart = pBuilder.getToviazLaunchChart(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Attendance", fromFilter);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH,
								"Attendance"));
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);

				Iterator iter = exams.iterator();
				while (iter.hasNext()) {
					String activityName = (String) iter.next();
					System.out.println("Activity Name ############### "
							+ activityName);
					String displayName = "";
					if (activityName.equals("Ped1")) {
						displayName = "Pedagogue Exam 1";
					}
					if (activityName.equals("Ped2")) {
						displayName = "Pedagogue Exam 2";
					}
					// Get the pies for PED1,PED2 and over all also to dispaly
					// first
					// time.
					thisRBUChartBean = (RBUChartBean[]) rbuHandler
							.getFilteredToviazLaunchSpecificChart(activityName,
									"", "", "", emplid);
					pieChart = pBuilder.getToviazLaunchChart(
							uSession.getUserFilter(),
							getRequest().getSession(), thisRBUChartBean,
							displayName, fromFilter);
					chartdetail = new ChartDetailWc(pieChart,
							"Attendance chart", new ChartLegendWc(
									ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH,
									activityName));
					if (pieChart.getCount() > 0)
						charts.add(chartdetail);
				}
				// Get for over all
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchOverAllChart("", "", emplid);
				pieChart = pBuilder.getToviazLaunchChart(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Overall", fromFilter);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH,
								"Overall"));
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);

			} else {

				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchAttendanceChart("Attendance",
								"", selectedBU, selectedRBU, emplid);
				pieChart = pBuilder.getToviazLaunchChart(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Attendance", fromFilter);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH,
								"Attendance"));
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);
				Iterator iter = exams.iterator();
				while (iter.hasNext()) {
					String activityName = (String) iter.next();
					System.out.println("Activity Name ############### "
							+ activityName);
					String displayName = "";
					if (activityName.equals("Ped1")) {
						displayName = "Pedagogue Exam 1";
					}
					if (activityName.equals("Ped2")) {
						displayName = "Pedagogue Exam 2";
					}
					// Get the pies for PED1,PED2 and over all also to dispaly
					// first
					// time.
					thisRBUChartBean = (RBUChartBean[]) rbuHandler
							.getFilteredToviazLaunchSpecificChart(activityName,
									"", selectedBU, selectedRBU, emplid);
					pieChart = pBuilder.getToviazLaunchChart(
							uSession.getUserFilter(),
							getRequest().getSession(), thisRBUChartBean,
							displayName, fromFilter);
					chartdetail = new ChartDetailWc(pieChart,
							"Attendance chart", new ChartLegendWc(
									ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH,
									activityName));
					if (pieChart.getCount() > 0)
						charts.add(chartdetail);
				}
				// Get for over all
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchOverAllChart(selectedBU,
								selectedRBU, emplid);
				pieChart = pBuilder.getToviazLaunchChart(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Overall", fromFilter);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH,
								"Overall"));
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);

			}
			// this component puts the header info in the chart page.
			ToviazLaunchChartHeaderWc chartHeaderWc = new ToviazLaunchChartHeaderWc(
					selectedBU, selectedRBU, pieChart.getCount());
			// ChartHeaderWc chartHeaderWc = new ChartHeaderWc(
			// terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(),
			// totalParticipants, uFilter.getProduct(),teamDesc);

			ToviazLaunchChartsWc chartpage = new ToviazLaunchChartsWc(
					uSession.getUser(), chartHeaderWc);
			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setFutureAllignmentBuDataBean(buDataBean);
			chartpage.setFutureAllignmentRBUDataBean(rbuDataBean);
			chartpage.setFirstRequest("Attendance");
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);
			this.request.getSession().setAttribute("selectedBU", selectedBU);
			this.request.getSession().setAttribute("selectedRBU", selectedRBU);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			this.request.getSession().setAttribute("fromRequest", "");
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

	private String getExamName(String examType) {
		try {
			RBUSHandler handler = new RBUSHandler();
			return handler.getExamName(examType);
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/components/report/ToviazLaunchReportListXls.jsp"
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
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

			Employee[] rbuEmployee = null;

			ServiceFactory factory = Service.getServiceFactory(trDb);
			RBUEmployeeHandler eHandler = factory.getRBUEmployeeHandler();
			WebPageComponent page;
			String selectedBU = "ALL";
			String selectedRBU = "ALL";
			String selectedSection = "";
			String fromRequest = "";
			RBUSHandler handler = new RBUSHandler();
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
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

			// Get the BU list
			buDataBean = trDb.getBu();
			if (selectedBU.equals("ALL")) {
				// Get the rbu list
				rbuDataBean = trDb.getRbuForRBU("Primary Care");
			} else {
				rbuDataBean = trDb.getRbuForRBU(selectedBU);
			}
			// check if coming from email, if so reset search to
			// clear all filters in session.
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			// UserSession uSession;
			// uSession = UserSession.getUserSession(getRequest());
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
			String type = "";
			String downloadExcel = "";
			String excelType = "";
			String excelSection = "";
			if (fromRequest.equals("reRun")) {
				if (this.getRequest().getParameter("selectedBU") != null) {
					selectedBU = this.getRequest().getParameter("selectedBU");
				}
				if (this.getRequest().getParameter("selectedRBU") != null) {
					selectedRBU = this.getRequest().getParameter("selectedRBU");
				}
				if (this.request.getSession().getAttribute("selectedSection") != null) {
					selectedSection = (String) this.request.getSession()
							.getAttribute("selectedSection");
					this.request.getSession()
							.removeAttribute("selectedSection");
				}
				if (this.request.getSession().getAttribute("type") != null) {
					type = (String) this.request.getSession().getAttribute(
							"type");
					this.request.getSession().removeAttribute("type");
				}
				qStrings = new AppQueryStrings();
				qStrings.setSection(selectedSection);
				uFilter.setQueryStrings(qStrings);
				// String sectionNow =
				// Util.toEmpty(uFilter.getQuseryStrings().getSection());
				// System.out.println("#################### section" +
				// sectionNow);

			} else {
				selectedSection = qStrings.getSection();
				type = qStrings.getType();
			}
			if (selectedSection == null) {
				selectedSection = "";
			}
			if (type == null) {
				type = "";
			}
			System.out.println("Section ######## " + selectedSection + "Type "
					+ type);
			if (this.getRequest().getParameter("downloadExcel") != null) {
				downloadExcel = this.getRequest().getParameter("downloadExcel");
			}
			if (this.getRequest().getParameter("excelType") != null) {
				excelType = this.getRequest().getParameter("excelType");
			}
			if (this.getRequest().getParameter("excelSection") != null) {
				excelSection = this.getRequest().getParameter("excelSection");
			}
			if (type.equals("Attendance")) {
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchAttendanceChart("Attendance",
								"", selectedBU, selectedRBU, emplid);
				uSession.getUserFilter().setProdcut("Overall");
				pieChart = pBuilder.getToviazLaunchChart(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Attendance", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH,
								"Attendance"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if (type.equals("Overall")) {
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchOverAllChart(selectedBU,
								selectedRBU, emplid);
				uSession.getUserFilter().setProdcut("Overall");
				pieChart = pBuilder.getToviazLaunchChart(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Overall", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH,
								"Overall"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else {
				String displayName = type;
				if (type.equals("Pedagogue Exam 1")) {
					type = "Ped1";
				}
				if (type.equals("Pedagogue Exam 2")) {
					type = "Ped2";
				}
				if (type.equals("Ped1")) {
					displayName = "Pedagogue Exam 1";
				}
				if (type.equals("Ped2")) {
					displayName = "Pedagogue Exam 2";
				}

				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchSpecificChart(type, "",
								selectedBU, selectedRBU, emplid);
				pieChart = pBuilder.getToviazLaunchChart(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, displayName, fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH,
								type));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			List exams = new ArrayList();
			if ("true".equals(downloadExcel)) {
				// excelRBUEmployee
				// =eHandler.getToviazLaunchAttendanceEmployees(uFilter,excelSection,excelSection,
				// selectedBU, selectedRBU);
				if (excelType.equalsIgnoreCase("Attendance")) {
					excelRBUEmployee = eHandler
							.getToviazLaunchAttendanceEmployees(uFilter, type,
									selectedSection, selectedBU, selectedRBU,
									emplid);
				} else if (excelType.equalsIgnoreCase("Overall")) {
					excelRBUEmployee = eHandler
							.getToviazLaunchOverallEmployees(excelSection,
									selectedBU, selectedRBU, emplid);
					exams = handler.getToviazExamsForEDP("");
				} else {
					// String displayName = type;
					if (excelType.equals("Pedagogue Exam 1")) {
						excelType = "Ped1";
					}
					if (excelType.equals("Pedagogue Exam 2")) {
						excelType = "Ped2";
					}
					excelRBUEmployee = eHandler
							.getToviazLaunchSpecificEmployees(excelType,
									excelSection, selectedBU, selectedRBU,
									emplid);
					exams = handler.getToviazExamsForEDP(excelType);
				}
				ListReportWpc reportList = new ListReportWpc(
						uSession.getUser(), uFilter, chart, excelRBUEmployee,
						"TOVIAZLAUNCH", buDataBean, rbuDataBean);
				page = new ListReportWpc(uSession.getUser(), uFilter, chart,
						excelRBUEmployee, true, "TOVIAZLAUNCH");
				request.getSession().setAttribute("xlsBean", excelRBUEmployee);
				request.getSession().setAttribute("xlsType", excelType);
				request.getSession().setAttribute("section", excelSection);
				request.getSession().setAttribute("exams", exams);
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

			if (type.equalsIgnoreCase("Attendance")) {
				rbuEmployee = eHandler.getToviazLaunchAttendanceEmployees(
						uFilter, type, selectedSection, selectedBU,
						selectedRBU, emplid);
			} else if (type.equalsIgnoreCase("Overall")) {
				rbuEmployee = eHandler.getToviazLaunchOverallEmployees(
						selectedSection, selectedBU, selectedRBU, emplid);
				exams = handler.getToviazExamsForEDP("");
			} else {
				rbuEmployee = eHandler.getToviazLaunchSpecificEmployees(type,
						selectedSection, selectedBU, selectedRBU, emplid);
				exams = handler.getToviazExamsForEDP(type);
			}
			if (pieChart.getCount() == 0)
				chart = null;
			page = new ListReportWpc(uSession.getUser(), uFilter, chart,
					rbuEmployee, "TOVIAZLAUNCH", buDataBean, rbuDataBean);
			uFilter.setClusterCode(uSession.getUser().getCluster());
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			getRequest().setAttribute("type", qStrings.getType());
			uSession.setOverallProcessor(null);
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
			request.getSession().setAttribute("selectedBU", selectedBU);
			request.getSession().setAttribute("selectedRBU", selectedRBU);
			request.getSession().setAttribute("selectedSection",
					selectedSection);
			request.getSession().setAttribute("exams", exams);
			request.getSession().setAttribute("type", type);
			this.request.getSession().setAttribute("fromRequest", "");
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
	 * getFilteredChart This method represents the point of entry into the
	 * pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward beginToviazLaunchExec(){
	 */
	public String beginToviazLaunchExec() {
		try {

			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}

			String action = "";
			if (this.getRequest().getParameter("firstTime") != null) {
				action = this.getRequest().getParameter("firstTime");
			}
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "RBUChart");
			boolean fromFilter = false;
			PieChart pieChart = null;
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"TOVIAZLAUNCH");
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			ServiceFactory factory = Service.getServiceFactory(trDb);
			RBUPieChartHandler rbuHandler = factory.getRBUPieChartHandler();
			UserFilter uFilter = uSession.getUserFilter();

			RBUSHandler handler = new RBUSHandler();
			User user = uSession.getUser();
			String emplid = "";
			emplid = handler.getNTIdExistance(user.getEmplid());
			if (emplid.equals("NF")) {
				emplid = "";
			}
			// Get the BU list
			buDataBean = trDb.getBu();
			// Get the rbu list
			rbuDataBean = trDb.getRbuForRBU("Primary Care");
			String selectedProduct = "";
			String selectedBU = "";
			String selectedRBU = "";
			if (action.equals("true")) {
				this.getRequest().setAttribute("Attendance", "Attendance");
				selectedBU = "Primary Care";
				selectedRBU = "ALL";
			}
			if (this.getRequest().getParameter("selectedBU") != null) {
				selectedBU = this.getRequest().getParameter("selectedBU");

			}
			if (this.getRequest().getParameter("selectedRBU") != null) {
				selectedRBU = this.getRequest().getParameter("selectedRBU");

			}
			List charts = new ArrayList();
			PieChartBuilder pBuilder = new PieChartBuilder();
			qStrings.setSection("");
			RBUChartBean[] thisRBUChartBean;
			ChartDetailWc chartdetail = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			if (action.equals("true")) {
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchAttendanceChartExecs(
								"Attendance", "", "", "", emplid);
				pieChart = pBuilder.getToviazLaunchChartExec(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Attendance", fromFilter);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(
								ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH_EXEC,
								"Attendance"));
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);
			} else {

				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchAttendanceChartExecs(
								"Attendance", "", selectedBU, selectedRBU,
								emplid);
				pieChart = pBuilder.getToviazLaunchChartExec(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Attendance", fromFilter);
				chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(
								ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH_EXEC,
								"Attendance"));
				if (pieChart.getCount() > 0)
					charts.add(chartdetail);
			}
			// this component puts the header info in the chart page.
			ToviazLaunchChartHeaderWc chartHeaderWc = new ToviazLaunchChartHeaderWc(
					selectedBU, selectedRBU, pieChart.getCount());
			// ChartHeaderWc chartHeaderWc = new ChartHeaderWc(
			// terr.getAreaDesc(),terr.getRegionDesc(), terr.getDistrictDesc(),
			// totalParticipants, uFilter.getProduct(),teamDesc);

			ToviazLaunchChartsWc chartpage = new ToviazLaunchChartsWc(
					uSession.getUser(), chartHeaderWc);
			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setFutureAllignmentBuDataBean(buDataBean);
			chartpage.setFutureAllignmentRBUDataBean(rbuDataBean);
			chartpage.setFirstRequest("Attendance");
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);
			this.request.getSession().setAttribute("selectedBU", selectedBU);
			this.request.getSession().setAttribute("selectedRBU", selectedRBU);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			this.request.getSession().setAttribute("fromRequest", "exec");
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
	 *              path="/WEB-INF/jsp/components/report/ToviazLaunchReportListXls.jsp"
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward listreportExec(){
	 */
	public String listreportExec() {
		try {

			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}

			Employee[] rbuEmployee = null;

			ServiceFactory factory = Service.getServiceFactory(trDb);
			RBUEmployeeHandler eHandler = factory.getRBUEmployeeHandler();
			WebPageComponent page;
			String selectedBU = "ALL";
			String selectedRBU = "ALL";
			String selectedSection = "";
			String fromRequest = "";
			RBUSHandler handler = new RBUSHandler();
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			User user = uSession.getUser();
			String emplid = "";
			System.out.println("################# User emplid "
					+ user.getEmplid());

			emplid = handler.getNTIdExistance(user.getEmplid());
			if (emplid.equals("NF")) {
				System.out.println("################ User not found");
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
			// Get the BU list
			buDataBean = trDb.getBu();
			if (selectedBU.equals("ALL")) {
				// Get the rbu list
				rbuDataBean = trDb.getRbuForRBU("Primary Care");
			} else {
				rbuDataBean = trDb.getRbuForRBU(selectedBU);
			}
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			FormUtil.loadObject(getRequest(), filterForm);
			UserFilter uFilter = uSession.getUserFilter();
			uFilter.setQueryStrings(qStrings);
			uFilter.setAdmin(uSession.getUser().isAdmin());
			PieChart pieChart = null;
			// OverallProcessor overall = uSession.getOverallProcessor();

			PieChartBuilder pBuilder = new PieChartBuilder();
			RBUChartBean[] thisRBUChartBean;
			boolean fromFilter = false;
			RBUPieChartHandler rbuHandler = factory.getRBUPieChartHandler();
			ChartDetailWc chart = null;
			String type = "";
			String downloadExcel = "";
			String excelType = "";
			String excelSection = "";
			if (this.request.getSession().getAttribute("forRequest") != null) {
				this.request.getSession().removeAttribute("forRequest");
			}
			if (fromRequest.equals("reRun")) {
				if (this.getRequest().getParameter("selectedBU") != null) {
					selectedBU = this.getRequest().getParameter("selectedBU");
				}
				if (this.getRequest().getParameter("selectedRBU") != null) {
					selectedRBU = this.getRequest().getParameter("selectedRBU");
				}
				if (this.request.getSession().getAttribute("selectedSection") != null) {
					selectedSection = (String) this.request.getSession()
							.getAttribute("selectedSection");
					this.request.getSession()
							.removeAttribute("selectedSection");
				}
				if (this.request.getSession().getAttribute("type") != null) {
					type = (String) this.request.getSession().getAttribute(
							"type");
					this.request.getSession().removeAttribute("type");
				}
				qStrings = new AppQueryStrings();
				qStrings.setSection(selectedSection);
				uFilter.setQueryStrings(qStrings);
			} else {
				selectedSection = qStrings.getSection();
				type = qStrings.getType();
			}
			if (selectedSection == null) {
				selectedSection = "";
			}
			if (type == null) {
				type = "";
			}
			System.out.println("Section ######## " + selectedSection + "Type "
					+ type);
			if (this.getRequest().getParameter("downloadExcel") != null) {
				downloadExcel = this.getRequest().getParameter("downloadExcel");
			}
			if (this.getRequest().getParameter("excelType") != null) {
				excelType = this.getRequest().getParameter("excelType");
			}
			if (this.getRequest().getParameter("excelSection") != null) {
				excelSection = this.getRequest().getParameter("excelSection");
			}
			if (type.equals("Attendance")) {
				thisRBUChartBean = (RBUChartBean[]) rbuHandler
						.getFilteredToviazLaunchAttendanceChartExecs(
								"Attendance", "", selectedBU, selectedRBU,
								emplid);
				uSession.getUserFilter().setProdcut("Overall");
				pieChart = pBuilder.getToviazLaunchChartExec(
						uSession.getUserFilter(), getRequest().getSession(),
						thisRBUChartBean, "Attendance", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(
								ChartLegendWc.LAYOUT_TOVIAZ_LAUNCH_EXEC,
								"Attendance"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("true".equals(downloadExcel)) {
				// excelRBUEmployee
				// =eHandler.getToviazLaunchAttendanceEmployees(uFilter,excelSection,excelSection,
				// selectedBU, selectedRBU);
				if (excelType.equalsIgnoreCase("Attendance")) {
					excelRBUEmployee = eHandler
							.getToviazLaunchAttendanceEmployeesForExec(uFilter,
									type, selectedSection, selectedBU,
									selectedRBU, emplid);
				}
				ListReportWpc reportList = new ListReportWpc(
						uSession.getUser(), uFilter, chart, excelRBUEmployee,
						"TOVIAZLAUNCHEXEC", buDataBean, rbuDataBean);
				page = new ListReportWpc(uSession.getUser(), uFilter, chart,
						excelRBUEmployee, true, "TOVIAZLAUNCHEXEC");
				request.getSession().setAttribute("xlsBean", excelRBUEmployee);
				request.getSession().setAttribute("xlsType", excelType);
				request.getSession().setAttribute("section", excelSection);
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
			if (type.equalsIgnoreCase("Attendance")) {
				rbuEmployee = eHandler
						.getToviazLaunchAttendanceEmployeesForExec(uFilter,
								type, selectedSection, selectedBU, selectedRBU,
								emplid);
			}
			if (pieChart.getCount() == 0)
				chart = null;
			page = new ListReportWpc(uSession.getUser(), uFilter, chart,
					rbuEmployee, "TOVIAZLAUNCHEXEC", buDataBean, rbuDataBean);
			uFilter.setClusterCode(uSession.getUser().getCluster());
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			getRequest().setAttribute("type", qStrings.getType());
			uSession.setOverallProcessor(null);
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
			request.getSession().setAttribute("selectedBU", selectedBU);
			request.getSession().setAttribute("selectedRBU", selectedRBU);
			request.getSession().setAttribute("selectedSection",
					selectedSection);
			request.getSession().setAttribute("type", type);
			this.request.getSession().setAttribute("fromRequest", "exec");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			// request.getSession().setAttribute("forRequest", "execs");
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
