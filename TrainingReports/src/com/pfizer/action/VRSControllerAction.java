package com.pfizer.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.Employee;
import com.pfizer.db.TeamBean;
import com.pfizer.db.Territory;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.VRSHandler;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.webapp.chart.PieChartBuilder;
import com.pfizer.webapp.search.EmplSearchForm;
import com.pfizer.webapp.search.EmployeeSearchPDFHS;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.POA.POAChartBean;
import com.pfizer.webapp.wc.VRS.VRSChartsWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchVRSWc;
import com.pfizer.webapp.wc.global.HeaderVRSSEARCHWc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.wc.WebPageComponent;

public class VRSControllerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	public static final Log log = LogFactory.getLog(VRSControllerAction.class);
	private HttpServletRequest request;
	private HttpServletResponse response;
	private TransactionDB trDb = new TransactionDB();

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

	public Employee[] excelPoaEmployee;

	/**
	 * @common:control
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
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward begin(){
	 */
	public String begin() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			return beginVRS();
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
	 * protected Forward beginVRS(){
	 */
	public String beginVRS() {
		try {

			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "VRS");
			boolean fromFilter = false;

			PieChart pieChart;

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"VRS");

			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			ServiceFactory factory = Service.getServiceFactory(trDb);
			VRSHandler vrsHandler = factory.getVRSHandler();
			System.out.println("\nAfter getting the VRS Handler");
			UserFilter uFilter = uSession.getUserFilter();
			System.out.println("\nAfter getting the User filter");
			if (filterForm.getTeamList().size() <= 0) {
				System.out.println("\nInside if of Team List");
				TeamBean[] allTeam = null;
				// Load data into Listbox;
				allTeam = trDb.getAllPDFHSTEAM();
				System.out.println("\nALL TEAM" + allTeam.length);

				LabelValueBean labelValueBean;
				filterForm.setTeam("All");

				FormUtil.loadObject(getRequest(), filterForm);
				labelValueBean = new LabelValueBean("All", "All");
				filterForm.setTeamList(labelValueBean);
				for (int i = 0; i < allTeam.length; i++) {
					labelValueBean = new LabelValueBean(
							allTeam[i].getTeamDesc(), allTeam[i].getTeamCd());
					filterForm.setTeamList(labelValueBean);
				}
			} else {
				System.out.println("Inside else of Team Descr");
				// We need to get the team description
				teamDesc = trDb.getTeamDescription(filterForm.getTeam());
			}
			// Total Candidates Participating:

			int totalParticipants = vrsHandler.getOverAllTotalCount("Overall",
					filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			System.out.println("\nTotal Participants" + totalParticipants);
			Territory terr = Service.getServiceFactory(trDb)
					.getTerritoryHandler().getTerritory(uFilter);

			// this component puts the header info in the chart page.
			ChartHeaderWc chartHeaderWc = null;
			if (terr != null) {
				chartHeaderWc = new ChartHeaderWc(terr.getAreaDesc(),
						terr.getRegionDesc(), terr.getDistrictDesc(),
						totalParticipants, uFilter.getProduct(), teamDesc, " ",
						" ", " ");
				System.out.println("\nAfter ChartHeaderWc" + chartHeaderWc);
			}
			VRSChartsWc chartpage = new VRSChartsWc(filterForm,
					uSession.getUser(), chartHeaderWc);
			System.out.println("\nAfter VRSChartsWc" + chartpage);

			List charts = new ArrayList();

			PieChartBuilder pBuilder = new PieChartBuilder();

			qStrings.setSection("");
			POAChartBean[] chartBean;
			ChartDetailWc chartdetail = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			System.out.println("\nGetting the filtered chart");
			// Get The Count For the Activity 1. This is for Spiriva Exam 1 in
			// the mapping table.
			chartBean = (POAChartBean[]) vrsHandler.getFilteredChart(
					"VISTASPRV1", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Spiriva Exam 1",
					fromFilter);
			System.out.println("\nAfter getting pie chart");
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_VRS,
							"Spiriva Exam 1"));
			System.out.println("\nAfter getting Chart Details");
			if (pieChart.getCount() > 0) {
				charts.add(chartdetail);
			} else {
				chartdetail = new ChartDetailWc();
				chartdetail.setChart(pieChart);
				chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				charts.add(chartdetail);
			}
			// Get The Count For the Activity 2. This is for Spiriva Exam 2 in
			// the mapping table.
			chartBean = (POAChartBean[]) vrsHandler.getFilteredChart(
					"VISTASPRV2", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Spiriva Exam 2",
					fromFilter);
			System.out.println("\nAfter getting PIE Chart for EXAM 2");
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_VRS,
							"Spiriva Exam 2"));
			if (pieChart.getCount() > 0) {
				charts.add(chartdetail);
			} else {
				chartdetail = new ChartDetailWc();
				chartdetail.setChart(pieChart);
				chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				charts.add(chartdetail);
			}
			// Get The Count For the Activity 3. This is for Vista Spiriva
			// Attendance
			chartBean = (POAChartBean[]) vrsHandler.getFilteredChartAttendance(
					"VISSPRVATT", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Attendance",
					fromFilter);
			System.out.println("\nAfter getting PIE Chart for ATTENDANCE");
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_VRS, "Attendance"));
			if (pieChart.getCount() > 0) {
				charts.add(chartdetail);
			} else {
				chartdetail = new ChartDetailWc();
				chartdetail.setChart(pieChart);
				chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				charts.add(chartdetail);
			}
			// Get The Count For the Overall
			chartBean = (POAChartBean[]) vrsHandler
					.getFilteredOverallChartForVRS("Overall",
							filterForm.getArea(), filterForm.getRegion(),
							filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder
					.getVRSChart(uSession.getUserFilter(), getRequest()
							.getSession(), chartBean, "Overall", fromFilter);
			System.out.println("\nAfter getting PIE Chart for OVERALL");
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_VRS, "Overall"));
			if (pieChart.getCount() > 0) {
				charts.add(chartdetail);
			} else {
				chartdetail = new ChartDetailWc();
				chartdetail.setChart(pieChart);
				chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				charts.add(chartdetail);
			}

			ChartListWc chartListWc = new ChartListWc(charts);
			chartListWc.setLayout(ChartListWc.LAYOUT_2COL);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
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
	 * @jpf:action
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/components/report/PLCReportListXlsvrs.jsp"
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward listReportVRS(){
	 */
	public String listReportVRS() {
		try {

			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			if (getResponse().isCommitted()) {
				return null;
			}
			Employee[] employees = null;

			ServiceFactory factory = Service.getServiceFactory(trDb);
			EmployeeHandler eHandler = factory.getEmployeeHandler();
			WebPageComponent page;

			// check if coming from email, if so reset search to
			// clear all filters in session.
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			FormUtil.loadObject(getRequest(), filterForm);
			if (filterForm.getTeam().trim().equalsIgnoreCase(""))
				filterForm.setTeam("All");
			UserFilter uFilter = uSession.getUserFilter();
			uFilter.setQueryStrings(qStrings);
			uFilter.setAdmin(uSession.getUser().isAdmin());

			PieChart pieChart = null;

			PieChartBuilder pBuilder = new PieChartBuilder();
			POAChartBean[] thisPOAChartBean;
			boolean fromFilter = false;
			VRSHandler vrsHandler = factory.getVRSHandler();
			ChartDetailWc chart = null;
			// Action for Excel Request
			if ("true".equals(uFilter.getQuseryStrings().getDownloadExcel())) {
				if ("Overall".equalsIgnoreCase(qStrings.getType())) {
					excelPoaEmployee = eHandler.getVRSOverAllEmployees(uFilter,
							qStrings.getType(), qStrings.getSection());
				} else if ("Attendance".equalsIgnoreCase(qStrings.getType())) {
					excelPoaEmployee = eHandler.getVRSEmployeesAttendance(
							uFilter, qStrings.getType(), qStrings.getSection());
				} else {
					excelPoaEmployee = eHandler.getVRSEmployees(uFilter,
							qStrings.getType(), qStrings.getSection());
				}
				request.getSession().setAttribute("xlsBean", excelPoaEmployee);
				request.getSession()
						.setAttribute("xlsType", qStrings.getType());
				request.getSession().setAttribute("section",
						qStrings.getSection());
				return new String("successXls");
			}
			System.out.println("Before rendering the Charts");
			// Render Chart
			if ("Spiriva Exam 1".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) vrsHandler
						.getFilteredChart("VISTASPRV1", filterForm.getArea(),
								filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				// uSession.getUserFilter().setProdcut("GEODE1");
				pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean,
						"Spiriva Exam 1", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_VRS,
								"Spiriva Exam 1"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if ("Spiriva Exam 2".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) vrsHandler
						.getFilteredChart("VISTASPRV2", filterForm.getArea(),
								filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				// uSession.getUserFilter().setProdcut("GEODE2");
				pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean,
						"Spiriva Exam 2", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_VRS,
								"Spiriva Exam 2"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if ("Attendance".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) vrsHandler
						.getFilteredChartAttendance("VISSPRVATT",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				// uSession.getUserFilter().setProdcut("GEODAT");
				pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean,
						"Attendance", fromFilter);
				System.out.println("Pie Chart Builder--- Before");
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_VRS,
								"Attendance"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if ("Overall".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) vrsHandler
						.getFilteredOverallChart("Overall",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				// uSession.getUserFilter().setProdcut("Overall");
				pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean, "Overall",
						fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_VRS, "Overall"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("Overall".equalsIgnoreCase(qStrings.getType())) {
				employees = eHandler.getVRSOverAllEmployees(uFilter,
						qStrings.getType(), qStrings.getSection());
			} else if ("Attendance".equalsIgnoreCase(qStrings.getType())) {
				System.out.println("INSIDE ELSE IF OF ATTENDANCE");
				employees = eHandler.getVRSEmployeesAttendance(uFilter,
						qStrings.getType(), qStrings.getSection());
				System.out.println("Employees" + employees);
			} else {
				employees = eHandler.getVRSEmployees(uFilter,
						qStrings.getType(), qStrings.getSection());
			}

			// poaEmployee
			// =eHandler.getPOAEmployees(uFilter,qStrings.getType(),qStrings.getSection());
			// System.out.println("poaEmployee length in JPF is "+poaEmployee.length);
			if (pieChart.getCount() == 0)
				chart = null;

			System.out.println("Employees before list" + employees);
			System.out.println("\nFilter before list" + uFilter);
			System.out.println("Chart before list" + chart);
			page = new ListReportWpc(uSession.getUser(), uFilter, chart,
					employees, "VRS");
			uFilter.setClusterCode(uSession.getUser().getCluster());
			Employee areaManager = eHandler.getAreaManager(uFilter);
			Employee regionManager = eHandler.getRegionManager(uFilter);
			Employee districtManager = eHandler.getDistrictManager(uFilter);
			// ((ListReportWpc)page).setAreaManager( areaManager );
			// ((ListReportWpc)page).setRegionManager( regionManager );
			// ((ListReportWpc)page).setDistrictManager( districtManager );
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
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward getFilteredChartVRS(){
	 */
	public String getFilteredChartVRS() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			TerritoryFilterForm filterForm = uSession
					.getNewTerritoryFilterForm();
			FormUtil.loadObject(getRequest(), filterForm);
			ServiceFactory factory = Service.getServiceFactory(trDb);
			VRSHandler vrsHandler = factory.getVRSHandler();
			List charts = new ArrayList();
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"VRS");
			filterForm = uSession.getUserFilterForm();
			FormUtil.loadObject(getRequest(), filterForm);
			System.out.println("\nInside getFilteredChartVRS");

			UserFilter uFilter = uSession.getUserFilter();
			// Total Candidates Participating:
			int totalParticipants = vrsHandler.getOverAllTotalCount("Overall",
					filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			System.out.println("\nTotal Participants" + totalParticipants);
			// Get Team Short Description for Header
			String teamDesc = "";
			if (filterForm.getTeam().equalsIgnoreCase("All"))
				teamDesc = "All";
			else
				teamDesc = trDb.getTeamDescription(filterForm.getTeam());
			Territory terr = Service.getServiceFactory(trDb)
					.getTerritoryHandler().getTerritory(uFilter);
			// this component puts the header info in the chart page.

			ChartHeaderWc chartHeaderWc = new ChartHeaderWc(terr.getAreaDesc(),
					terr.getRegionDesc(), terr.getDistrictDesc(),
					totalParticipants, uFilter.getProduct(), teamDesc, " ",
					" ", " ");

			VRSChartsWc chartpage = new VRSChartsWc(filterForm,
					uSession.getUser(), chartHeaderWc);
			PieChartBuilder pBuilder = new PieChartBuilder();
			ChartDetailWc chartdetail = null;
			POAChartBean[] chartBean;
			PieChart pieChart = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			boolean fromFilter = false;

			// Get The Count For the Exam 1
			chartBean = (POAChartBean[]) vrsHandler.getFilteredChart(
					"VISTASPRV1", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Spiriva Exam 1",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_VRS,
							"Spiriva Exam 1"));
			if (pieChart.getCount() > 0) {
				charts.add(chartdetail);
			} else {
				chartdetail = new ChartDetailWc();
				chartdetail.setChart(pieChart);
				chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				charts.add(chartdetail);
			}

			// Get The Count For the Exam 2
			chartBean = (POAChartBean[]) vrsHandler.getFilteredChart(
					"VISTASPRV2", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Spiriva Exam 2",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_VRS,
							"Spiriva Exam 2"));
			if (pieChart.getCount() > 0) {
				charts.add(chartdetail);
			} else {
				chartdetail = new ChartDetailWc();
				chartdetail.setChart(pieChart);
				chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				charts.add(chartdetail);
			}

			// Get The Count For the Attendance
			chartBean = (POAChartBean[]) vrsHandler.getFilteredChartAttendance(
					"VISSPRVATT", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder.getVRSChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Attendance",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_VRS, "Attendance"));
			if (pieChart.getCount() > 0) {
				charts.add(chartdetail);
			} else {
				chartdetail = new ChartDetailWc();
				chartdetail.setChart(pieChart);
				chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				charts.add(chartdetail);
			}

			// Get The Count For the Overall
			chartBean = (POAChartBean[]) vrsHandler.getFilteredOverallChart(
					"Overall", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			// thisPOAChartBean=trDb.getPOAOverallChart();
			pieChart = pBuilder
					.getVRSChart(uSession.getUserFilter(), getRequest()
							.getSession(), chartBean, "Overall", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_VRS, "Overall"));
			if (pieChart.getCount() > 0) {
				charts.add(chartdetail);
			} else {
				chartdetail = new ChartDetailWc();
				chartdetail.setChart(pieChart);
				chartdetail.setLayout(ChartDetailWc.LAYOUT_NO_DATA);
				charts.add(chartdetail);
			}

			ChartListWc chartListWc = new ChartListWc(charts);
			chartListWc.setLayout(ChartListWc.LAYOUT_2COL);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("success");
			 */
			// Changed from Calling charts() for performance.
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
	 * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward searchVRS(){
	 */
	public String searchVRS() {
		try {
			/**
			 * * <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}
			EmplSearchForm form = new EmplSearchForm();
			FormUtil.loadObject(getRequest(), form);

			UserSession uSession = UserSession.getUserSession(getRequest());
			User user = uSession.getUser();
			boolean bolRefresh = false;

			// Test
			EmployeeSearchPDFHS es = new EmployeeSearchPDFHS();
			/*
			 * //We will create resultMap for all products for the FirstTime
			 * User logs in ReportBuilder rb = new
			 * ReportBuilder(Service.getServiceFactory()); List products =
			 * user.getProducts();
			 * 
			 * String iter_prod=""; String sessionKey;
			 * 
			 * //Lets check if we need to refresh String
			 * refresh=(String)getSession().getAttribute("refresh");
			 * 
			 * if("true".equalsIgnoreCase(refresh)){ bolRefresh=true;
			 * log.info("Entering to Refresh"); }
			 * 
			 * 
			 * for(Iterator iter=products.iterator();iter.hasNext();){ Product
			 * thisProd=(Product)iter.next();
			 * sessionKey="allProd"+thisProd.getProductCode();
			 * 
			 * 
			 * if(getSession().getAttribute(sessionKey)==null || bolRefresh){
			 * OverallProcessor or =
			 * rb.getOverallProcessorByProduct(uSession,thisProd
			 * .getProductCode()); Map allemps = or.getAllEmployeeMap();
			 * log.debug("Setting session for "+sessionKey);
			 * getSession().setAttribute(sessionKey,allemps);
			 * 
			 * } }
			 * 
			 * if(bolRefresh){ getSession().removeAttribute("refresh");
			 * bolRefresh=false; }
			 * 
			 * 
			 * 
			 * List ret = new ArrayList(); // For PDF and SPF String m1 =
			 * getRequest(
			 * ).getParameter("m1")==null?"":getRequest().getParameter("m1");
			 */
			List ret = new ArrayList();
			if (!Util.isEmpty(form.getLname())
					|| !Util.isEmpty(form.getFname())
					|| !Util.isEmpty(form.getTerrId())
					|| !Util.isEmpty(form.getEmplid())) {
				// ret = es.getPDFHSEmployeesByName( form, uSession, m1 );
				ret = es.getVRSEmployees(form, uSession);
			}
			MainTemplateWpc page = new MainTemplateWpc(user, "detailreport",
					"VRSSEARCH");
			((HeaderVRSSEARCHWc) page.getHeader()).setShowNav(false);
			page.setMain(new EmployeeSearchVRSWc(form, ret));
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

}
