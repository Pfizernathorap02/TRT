package com.pfizer.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.ClassRosterBean;
import com.pfizer.db.EmpReport;
import com.pfizer.db.Employee;
import com.pfizer.db.EnrollChangeReport;
import com.pfizer.db.GeneralSessionEmployee;
import com.pfizer.db.P2LRegistration;
import com.pfizer.db.TeamBean;
import com.pfizer.db.Territory;
import com.pfizer.db.VarianceReportBean;
import com.pfizer.hander.AdminReportHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.PDFHSHandler;
import com.pfizer.hander.PLCHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.chart.PieChart;
import com.pfizer.webapp.chart.PieChartBuilder;
import com.pfizer.webapp.report.ClassFilterForm;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.PDFHS.PDFHSChartBean;
import com.pfizer.webapp.wc.PDFHS.PDFHSChartsWc;
import com.pfizer.webapp.wc.PLC.PlcChartsWc;
import com.pfizer.webapp.wc.POA.POAChartBean;
import com.pfizer.webapp.wc.components.ProductSelectPWRAWc;
import com.pfizer.webapp.wc.components.admintoolsSelectPDFHSWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.chart.ChartHeaderWc;
import com.pfizer.webapp.wc.components.chart.ChartLegendWc;
import com.pfizer.webapp.wc.components.chart.ChartListWc;
import com.pfizer.webapp.wc.components.report.ClassRosterWc;
import com.pfizer.webapp.wc.components.report.EnrollmentChangeReportWc;
import com.pfizer.webapp.wc.components.report.EnrollmentSummaryReportWc;
import com.pfizer.webapp.wc.components.report.GeneralSessionWc;
import com.pfizer.webapp.wc.components.report.PersonalAgendaWc;
import com.pfizer.webapp.wc.components.report.TrainingScheduleEmplListWc;
import com.pfizer.webapp.wc.components.report.TrainingScheduleWc;
import com.pfizer.webapp.wc.components.report.VarianceReportListWc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.page.ListReportWpcPDFHS;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.printing.PrintingConstants;
import com.tgix.printing.VelocityConvertor;
import com.tgix.wc.WebPageComponent;

public class PWRAControllerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	public Employee[] excelPoaEmployee;
	public Employee[] excelPDFHSEmployee;
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

	/**
	 * @common:control
	 */

	// Uncomment this declaration to access Global.app.
	//
	// public global.Global globalApp;
	//

	// For an example of page flow exception handling see the example "catch"
	// and "exception-handler"
	// annotations in {project}/WEB-INF/src/global/Global.app

	/**
	 * getFilteredChart This method represents the point of entry into the
	 * pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	public String begin() {
		try {
			return beginPDFHS();
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
	public String beginPDFHS() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "PowersDFHStudy");
			boolean fromFilter = false;

			PieChart pieChart;

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"PDFHS");

			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			// System.out.println("filterForm District####"+filterForm.getTeamList().size());
			ServiceFactory factory = Service.getServiceFactory(trDb);
			PDFHSHandler pdfhsHandler = factory.getPDFHSHandler();
			UserFilter uFilter = uSession.getUserFilter();
			if (filterForm.getTeamList().size() <= 0) {
				TeamBean[] allTeam = null;
				allTeam = trDb.getAllPDFHSTEAM();
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
				// We need to get the team description
				teamDesc = trDb.getTeamDescription(filterForm.getTeam());

			}
			// Total Candidates Participating:
			int totalParticipants = pdfhsHandler.getOverAllTotalCount(
					"Overall", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			// System.out.println("THE TOTAL PARTICIPANT HERE IS"+totalParticipants);
			Territory terr = Service.getServiceFactory(trDb)
					.getTerritoryHandler().getTerritory(uFilter);
			// this component puts the header info in the chart page.

			ChartHeaderWc chartHeaderWc = new ChartHeaderWc(terr.getAreaDesc(),
					terr.getRegionDesc(), terr.getDistrictDesc(),
					totalParticipants, uFilter.getProduct(), teamDesc);

			PDFHSChartsWc chartpage = new PDFHSChartsWc(filterForm,
					uSession.getUser(), chartHeaderWc);

			List charts = new ArrayList();

			PieChartBuilder pBuilder = new PieChartBuilder();

			qStrings.setSection("");
			PDFHSChartBean[] thisPDFHSChartBean;
			ChartDetailWc chartdetail = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);

			// Get The Count For the Aricept
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("ARCP", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("ARCP");
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Aricept",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Aricept"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Celebrex
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("CLBR", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("CLBR");
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Celebrex",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Celebrex"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the
			// Geodon//thisPDFHSChartBean=trDb.getPDFHSChartsForAll("GEOD");
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("GEOD", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			chartdetail = new ChartDetailWc(pBuilder.getPDFHSChart(
					uSession.getUserFilter(), getRequest().getSession(),
					thisPDFHSChartBean, "Geodon", fromFilter),
					"Attendance chart", new ChartLegendWc(
							ChartLegendWc.LAYOUT_PDFHS, "Geodon"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Lyrica
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("LYRC", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("LYRC");
			chartdetail = new ChartDetailWc(pBuilder.getPDFHSChart(
					uSession.getUserFilter(), getRequest().getSession(),
					thisPDFHSChartBean, "Lyrica", fromFilter),
					"Attendance chart", new ChartLegendWc(
							ChartLegendWc.LAYOUT_PDFHS, "Lyrica"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Rebif
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("REBF", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("REBF");
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Rebif",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Rebif"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Overall
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSOverallChart("Overall",
							filterForm.getArea(), filterForm.getRegion(),
							filterForm.getDistrict(), filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSOverallChart();
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Overall",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Overall"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);

			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 *  protected Forward beginPHR(){
	 */

	public String beginPHR() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "PowersDFHStudy");
			boolean fromFilter = false;

			PieChart pieChart;

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"PHR");

			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			// System.out.println("filterForm Area"+filterForm.getArea());
			// System.out.println("filterForm Region"+filterForm.getRegion());
			// System.out.println("filterForm District"+filterForm.getDistrict());
			// System.out.println("filterForm District"+filterForm.getTeam());
			// System.out.println("filterForm District"+filterForm.getTeamList().size());
			ServiceFactory factory = Service.getServiceFactory(trDb);
			PDFHSHandler pdfhsHandler = factory.getPDFHSHandler();
			UserFilter uFilter = uSession.getUserFilter();
			if (filterForm.getTeamList().size() <= 0) {
				TeamBean[] allTeam = null;
				allTeam = trDb.getAllPDFHSTEAM();
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
				// We need to get the team description
				teamDesc = trDb.getTeamDescription(filterForm.getTeam());

			}
			// Total Candidates Participating:
			int totalParticipants = pdfhsHandler.getOverAllTotalCount(
					"Overall", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			// System.out.println("THE TOTAL PARTICIPANT HERE IS"+totalParticipants);
			Territory terr = Service.getServiceFactory(trDb)
					.getTerritoryHandler().getTerritory(uFilter);
			// this component puts the header info in the chart page.

			ChartHeaderWc chartHeaderWc = new ChartHeaderWc(terr.getAreaDesc(),
					terr.getRegionDesc(), terr.getDistrictDesc(),
					totalParticipants, uFilter.getProduct(), teamDesc);

			PDFHSChartsWc chartpage = new PDFHSChartsWc(filterForm,
					uSession.getUser(), chartHeaderWc);

			List charts = new ArrayList();

			PieChartBuilder pBuilder = new PieChartBuilder();

			qStrings.setSection("");
			PDFHSChartBean[] thisPDFHSChartBean;
			ChartDetailWc chartdetail = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);

			// Get The Count For the Aricept
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("ARCP", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("ARCP");
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Aricept",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Aricept"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Celebrex
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("CLBR", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("CLBR");
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Celebrex",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Celebrex"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the
			// Geodon//thisPDFHSChartBean=trDb.getPDFHSChartsForAll("GEOD");
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("GEOD", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			chartdetail = new ChartDetailWc(pBuilder.getPDFHSChart(
					uSession.getUserFilter(), getRequest().getSession(),
					thisPDFHSChartBean, "Geodon", fromFilter),
					"Attendance chart", new ChartLegendWc(
							ChartLegendWc.LAYOUT_PDFHS, "Geodon"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Lyrica
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("LYRC", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("LYRC");
			chartdetail = new ChartDetailWc(pBuilder.getPDFHSChart(
					uSession.getUserFilter(), getRequest().getSession(),
					thisPDFHSChartBean, "Lyrica", fromFilter),
					"Attendance chart", new ChartLegendWc(
							ChartLegendWc.LAYOUT_PDFHS, "Lyrica"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Rebif
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("REBF", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("REBF");
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Rebif",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Rebif"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Overall
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSOverallChart("Overall",
							filterForm.getArea(), filterForm.getRegion(),
							filterForm.getDistrict(), filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSOverallChart();
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Overall",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Overall"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);

			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * By  protected Forward getFilteredChart(){
	 */

	public String getFilteredChart() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			TerritoryFilterForm filterForm = uSession
					.getNewTerritoryFilterForm();
			FormUtil.loadObject(getRequest(), filterForm);
			ServiceFactory factory = Service.getServiceFactory(trDb);
			PDFHSHandler pdfhsHandler = factory.getPDFHSHandler();
			List charts = new ArrayList();
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"PDFHS");
			filterForm = uSession.getUserFilterForm();
			FormUtil.loadObject(getRequest(), filterForm);

			UserFilter uFilter = uSession.getUserFilter();

			// New Code Under Test
			String teamDesc = "All";
			// System.out.println("filterForm Area"+filterForm.getArea());
			// System.out.println("filterForm Region"+filterForm.getRegion());
			// System.out.println("filterForm District"+filterForm.getDistrict());
			// System.out.println("filterForm District"+filterForm.getTeam());
			// System.out.println("filterForm District"+filterForm.getTeamList().size());
			if (filterForm.getTeamList().size() <= 0) {
				TeamBean[] allTeam = null;
				allTeam = trDb.getAllPDFHSTEAM();
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
				// We need to get the team description
				teamDesc = trDb.getTeamDescription(filterForm.getTeam());
			}

			// End New Code Under Test

			// Total Candidates Participating:
			int totalParticipants = pdfhsHandler.getOverAllTotalCount(
					"Overall", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			// Get Team Short Description for Header

			// Removed
			// String teamDesc="";

			if (filterForm.getTeam().equalsIgnoreCase("All"))
				teamDesc = "All";
			else
				teamDesc = trDb.getTeamDescription(filterForm.getTeam());
			Territory terr = Service.getServiceFactory(trDb)
					.getTerritoryHandler().getTerritory(uFilter);
			// this component puts the header info in the chart page.

			ChartHeaderWc chartHeaderWc = new ChartHeaderWc(terr.getAreaDesc(),
					terr.getRegionDesc(), terr.getDistrictDesc(),
					totalParticipants, uFilter.getProduct(), teamDesc);

			PDFHSChartsWc chartpage = new PDFHSChartsWc(filterForm,
					uSession.getUser(), chartHeaderWc);
			PieChartBuilder pBuilder = new PieChartBuilder();
			ChartDetailWc chartdetail = null;
			PDFHSChartBean[] thisPDFHSChartBean;
			PieChart pieChart = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			boolean fromFilter = false;
			// Get The Count For the Aricept
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("ARCP", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());

			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Aricept",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Aricept"));

			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Celebrex
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("CLBR", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Celebrex",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Celebrex"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);
			// Get The Count For the Geodon
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("GEOD", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Geodon",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Geodon"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Lyrica
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("LYRC", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Lyrica",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Lyrica"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Rebif
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("REBF", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Rebif",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Rebif"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the OverAll
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSOverallChart("Overall",
							filterForm.getArea(), filterForm.getRegion(),
							filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Overall",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Overall"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward beginPDFRA(){
	 */

	public String beginPDFRA() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "PowersDFHStudy");
			boolean fromFilter = false;

			PieChart pieChart;

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"PDFRA");

			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			// System.out.println("filterForm Area"+filterForm.getArea());
			// System.out.println("filterForm Region"+filterForm.getRegion());
			// System.out.println("filterForm District"+filterForm.getDistrict());
			// System.out.println("filterForm District"+filterForm.getTeam());
			// System.out.println("filterForm District"+filterForm.getTeamList().size());
			ServiceFactory factory = Service.getServiceFactory(trDb);
			PDFHSHandler pdfhsHandler = factory.getPDFHSHandler();
			UserFilter uFilter = uSession.getUserFilter();
			if (filterForm.getTeamList().size() <= 0) {
				TeamBean[] allTeam = null;
				allTeam = trDb.getAllPDFHSTEAM();
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
				// We need to get the team description
				teamDesc = trDb.getTeamDescription(filterForm.getTeam());

			}
			// Total Candidates Participating:
			int totalParticipants = pdfhsHandler.getOverAllTotalCount(
					"Overall", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			// System.out.println("THE TOTAL PARTICIPANT HERE IS"+totalParticipants);
			Territory terr = Service.getServiceFactory(trDb)
					.getTerritoryHandler().getTerritory(uFilter);
			// this component puts the header info in the chart page.

			ChartHeaderWc chartHeaderWc = new ChartHeaderWc(terr.getAreaDesc(),
					terr.getRegionDesc(), terr.getDistrictDesc(),
					totalParticipants, uFilter.getProduct(), teamDesc);

			PDFHSChartsWc chartpage = new PDFHSChartsWc(filterForm,
					uSession.getUser(), chartHeaderWc);

			List charts = new ArrayList();

			PieChartBuilder pBuilder = new PieChartBuilder();

			qStrings.setSection("");
			PDFHSChartBean[] thisPDFHSChartBean;
			ChartDetailWc chartdetail = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);

			// Get The Count For the Aricept
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("ARCP", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("ARCP");
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Aricept",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Aricept"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Celebrex
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("CLBR", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("CLBR");
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Celebrex",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Celebrex"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the
			// Geodon//thisPDFHSChartBean=trDb.getPDFHSChartsForAll("GEOD");
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("GEOD", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			chartdetail = new ChartDetailWc(pBuilder.getPDFHSChart(
					uSession.getUserFilter(), getRequest().getSession(),
					thisPDFHSChartBean, "Geodon", fromFilter),
					"Attendance chart", new ChartLegendWc(
							ChartLegendWc.LAYOUT_PDFHS, "Geodon"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Lyrica
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("LYRC", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("LYRC");
			chartdetail = new ChartDetailWc(pBuilder.getPDFHSChart(
					uSession.getUserFilter(), getRequest().getSession(),
					thisPDFHSChartBean, "Lyrica", fromFilter),
					"Attendance chart", new ChartLegendWc(
							ChartLegendWc.LAYOUT_PDFHS, "Lyrica"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Rebif
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSChart("REBF", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSChartsForAll("REBF");
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Rebif",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Rebif"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Overall
			thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
					.getFilteredPDFHSOverallChart("Overall",
							filterForm.getArea(), filterForm.getRegion(),
							filterForm.getDistrict(), filterForm.getTeam());
			// thisPDFHSChartBean=trDb.getPDFHSOverallChart();
			pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
					getRequest().getSession(), thisPDFHSChartBean, "Overall",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Overall"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);

			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");

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
	 * By  protected Forward productselect(){
	 */

	public String productselect() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			request.getSession().setAttribute("ReportType", "PowersDFHStudy");

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
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
				uSession.setUserFilterForm(uSession.getNewTerritoryFilterForm());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
				uSession.setUserFilterForm(uSession.getNewTerritoryFilterForm());
			}

			MainTemplateWpc page = new MainTemplateWpc(user, "productselect",
					"PWRA");
			// MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(),
			// "",true );

			page.setMain(new ProductSelectPWRAWc(user));
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);
			return new String("index");
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/components/report/POAReportListXlS.jsp"
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward listreport(){
	 */

	public String listreport() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */

			if (getResponse().isCommitted()) {
				return null;
			}

			Employee[] pdfhsEmployee = null;

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
			// System.out.println("TEAM IN JPF is"+filterForm.getTeam());
			UserFilter uFilter = uSession.getUserFilter();
			uFilter.setQueryStrings(qStrings);
			uFilter.setAdmin(uSession.getUser().isAdmin());
			// System.out.println("Product is "+qStrings.getType());

			PieChart pieChart = null;

			OverallProcessor overall = uSession.getOverallProcessor();

			PieChartBuilder pBuilder = new PieChartBuilder();
			PDFHSChartBean[] thisPDFHSChartBean;
			boolean fromFilter = false;
			PDFHSHandler pdfhsHandler = factory.getPDFHSHandler();
			ChartDetailWc chart = null;
			// Get The Count For the Geodon
			if ("Geodon".equalsIgnoreCase(qStrings.getType())) {
				thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
						.getFilteredPDFHSChart("GEOD", filterForm.getArea(),
								filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("GEOD");
				pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPDFHSChartBean,
						"Geodon", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Geodon"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);

			}
			if ("Aricept".equalsIgnoreCase(qStrings.getType())) {
				thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
						.getFilteredPDFHSChart("ARCP", filterForm.getArea(),
								filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("ARCP");
				pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPDFHSChartBean,
						"Aricept", fromFilter);
				chart = new ChartDetailWc(
						pieChart,
						"Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Aricept"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("Celebrex".equalsIgnoreCase(qStrings.getType())) {
				thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
						.getFilteredPDFHSChart("CLBR", filterForm.getArea(),
								filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("CLBR");
				pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPDFHSChartBean,
						"Celebrex", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS,
								"Celebrex"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("Rebif".equalsIgnoreCase(qStrings.getType())) {
				thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
						.getFilteredPDFHSChart("REBF", filterForm.getArea(),
								filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("REBF");
				pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPDFHSChartBean, "Rebif",
						fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Rebif"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("Lyrica".equalsIgnoreCase(qStrings.getType())) {
				thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
						.getFilteredPDFHSChart("LYRC", filterForm.getArea(),
								filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("LYRC");
				pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPDFHSChartBean,
						"Lyrica", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Lyrica"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}
			if ("Overall".equalsIgnoreCase(qStrings.getType())) {
				thisPDFHSChartBean = (PDFHSChartBean[]) pdfhsHandler
						.getFilteredPDFHSOverallChart("Overall",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("Overall");
				pieChart = pBuilder.getPDFHSChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPDFHSChartBean,
						"Overall", fromFilter);
				chart = new ChartDetailWc(
						pieChart,
						"Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PDFHS, "Overall"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}

			if ("true".equals(uFilter.getQuseryStrings().getDownloadExcel())) {

				if ("Overall".equalsIgnoreCase(qStrings.getType())) {
					excelPDFHSEmployee = eHandler.getPDFHSOverAllEmployees(
							uFilter, qStrings.getType(), qStrings.getSection());
				} else {
					excelPDFHSEmployee = eHandler.getPDFHSEmployees(uFilter,
							qStrings.getType(), qStrings.getSection());
				}

				ListReportWpcPDFHS reportList = new ListReportWpcPDFHS(
						uSession.getUser(), uFilter, chart, excelPDFHSEmployee);

				page = new ListReportWpcPDFHS(uSession.getUser(), uFilter,
						chart, excelPDFHSEmployee, true);

				request.getSession()
						.setAttribute("xlsBean", excelPDFHSEmployee);
				request.getSession()
						.setAttribute("xlsType", qStrings.getType());
				request.getSession().setAttribute("section",
						qStrings.getSection());
				return new String("successXls");
			}
			// Get All the Filter and Param we need to run the query to get
			// Employee List

			if ("Overall".equalsIgnoreCase(qStrings.getType())) {
				pdfhsEmployee = eHandler.getPDFHSOverAllEmployees(uFilter,
						qStrings.getType(), qStrings.getSection());
				// System.out.println("After the OVERALL PDFHS BEAN LENGTH IS "+pdfhsEmployee.length);
			} else {
				pdfhsEmployee = eHandler.getPDFHSEmployees(uFilter,
						qStrings.getType(), qStrings.getSection());
			}

			// pdfhsEmployee
			// =eHandler.getPDFHSEmployees(uFilter,qStrings.getType(),qStrings.getSection());
			// System.out.println("pdfhsEmployee length in JPF is "+pdfhsEmployee.length);
			if (pieChart.getCount() == 0)
				chart = null;
			page = new ListReportWpc(uSession.getUser(), uFilter, chart,
					pdfhsEmployee, "PDFHS");
			uFilter.setClusterCode(uSession.getUser().getCluster());
			Employee areaManager = eHandler.getAreaManager(uFilter);
			Employee regionManager = eHandler.getRegionManager(uFilter);
			Employee districtManager = eHandler.getDistrictManager(uFilter);
			((ListReportWpc) page).setAreaManager(areaManager);
			((ListReportWpc) page).setRegionManager(regionManager);
			((ListReportWpc) page).setDistrictManager(districtManager);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			uSession.setOverallProcessor(null);
			return new String("success");

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
	 * By  protected Forward PDFEnrollmentSummaryReport(){
	 */

	public String PDFEnrollmentSummaryReport() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
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
			EmpReport[] empReport = handler
					.getEnrollmentSummaryReport(AppConst.EVENT_PDF);
			EmpReport empReportTotal = handler
					.getEnrollmentSummaryReportTotal(AppConst.EVENT_PDF);
			EnrollmentSummaryReportWc main = new EnrollmentSummaryReportWc(
					empReport, empReportTotal, AppConst.EVENT_PDF);

			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"PDF - Training Schedule Summary.xls\"");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");

				return new String("successXls");
			}
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_PDF, "PDFHSREPORT");
			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * By  protected Forward PDFEnrollmentChangeReport(){
	 */

	public String PDFEnrollmentChangeReport() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
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
			EnrollChangeReport[] empReport = handler
					.getEnrollmentChangeReport(AppConst.EVENT_PDF);
			EmpReport empReportTotal = handler
					.getEnrollmentSummaryReportTotal(AppConst.EVENT_PDF);
			EnrollmentChangeReportWc main = new EnrollmentChangeReportWc(
					empReport, AppConst.EVENT_PDF);

			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"PDF - Enrollment Change Report.xls\"");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");

				return new String("successXls");
			}
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_PDF, "PDFHSREPORT");
			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * By  protected Forward PDFHSReportTrainingSchedule(){
	 */

	public String PDFHSReportTrainingSchedule() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
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
			EmpReport[] empReport = handler
					.getTrainingSchedule(AppConst.EVENT_PDF);
			TrainingScheduleWc main = new TrainingScheduleWc(empReport,
					AppConst.EVENT_PDF);
			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"PDF - Training Schedule Summary.xls\"");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");

				return new String("successXls");
			}
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_PDF, "PDFHSREPORT");
			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * By  protected Forward PDFHSReportTrainingScheduleEmplList(){
	 */

	public String PDFHSReportTrainingScheduleEmplList() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
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

			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			String downloadExcel = getRequest().getParameter("downloadExcel");

			ClassFilterForm form = new ClassFilterForm();
			form.setProduct(getRequest().getParameter(form.FIELD_PRODUCT));
			form.setStartDate(getRequest().getParameter(form.FIELD_STARTDATE));
			form.setEndDate(getRequest().getParameter(form.FIELD_ENDDATE));
			form.setTeamCd(getRequest().getParameter(form.FIELD_TEAMCD));
			form.setEnrollmentDate(getRequest().getParameter(
					form.FIELD_ENROLLMENTDATE));

			AdminReportHandler handler = new AdminReportHandler();
			EmpReport[] empReport = null;
			if (form.getEnrollmentDate() == null
					|| form.getEnrollmentDate().equals("")) {
				empReport = handler.getTrainingScheduleEmplList(
						AppConst.EVENT_PDF, form);
			} else {
				empReport = handler.getEnrollmentEmplList(AppConst.EVENT_PDF,
						form);
			}

			TrainingScheduleEmplListWc main = new TrainingScheduleEmplListWc(
					form, empReport, AppConst.EVENT_PDF);

			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"PDF - Training Schedule Detail.xls\"");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");

				return new String("successXls");
			}

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_PDF, "PDFHSREPORT");
			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * By  protected Forward PDFHSReportVariance(){
	 */

	public String PDFHSReportVariance() {
		try {
			/**
			 * } } <!-- Infosys - Weblogic to Jboss migration changes ends here
			 * -->
			 */
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			AppQueryStrings qStrings = new AppQueryStrings();
			User user;
			AdminReportHandler handler = new AdminReportHandler();
			VarianceReportBean[] varianceReportBean = handler
					.getVarianceReport(AppConst.EVENT_PDF);

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

			UserFilter filter = uSession.getUserFilter();
			VarianceReportListWc varianceReport = new VarianceReportListWc(
					filter, varianceReportBean, user, AppConst.EVENT_PDF);

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
						"attachment; filename=\"PDF - Variance Report.xls\"");
				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				return new String("successXls");
			} else {
				MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
						AppConst.EVENT_PDF, "PDFHSREPORT");
				page.setMain(varianceReport);
				getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
				return new String("success");
			}
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/components/report/PLCReportListXlS.jsp"
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward listReportPLC(){
	 */

	public String listReportPLC() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
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
			// System.out.println("TEAM IN JPF is"+filterForm.getTeam());
			UserFilter uFilter = uSession.getUserFilter();
			uFilter.setQueryStrings(qStrings);
			uFilter.setAdmin(uSession.getUser().isAdmin());
			// System.out.println("Product is "+qStrings.getType());

			PieChart pieChart = null;

			OverallProcessor overall = uSession.getOverallProcessor();

			PieChartBuilder pBuilder = new PieChartBuilder();
			POAChartBean[] thisPOAChartBean;
			boolean fromFilter = false;
			PLCHandler plcHandler = factory.getPLCHandler();
			ChartDetailWc chart = null;
			// Action for Excel Request
			if ("true".equals(uFilter.getQuseryStrings().getDownloadExcel())) {
				if ("Overall".equalsIgnoreCase(qStrings.getType())) {
					excelPoaEmployee = eHandler.getPLCOverAllEmployees(uFilter,
							qStrings.getType(), qStrings.getSection());
				} else {
					excelPoaEmployee = eHandler.getPLCEmployees(uFilter,
							qStrings.getType(), qStrings.getSection());
				}
				request.getSession().setAttribute("xlsBean", excelPoaEmployee);
				request.getSession()
						.setAttribute("xlsType", qStrings.getType());
				request.getSession().setAttribute("section",
						qStrings.getSection());

				return new String("successXls");
			}

			// Action for Detail Page
			// Render Chart
			if ("Geodon".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) plcHandler
						.getFilteredChartWithExamType("GEOD",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("GEOD");
				pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean, "Geodon",
						fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Geodon"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if ("Aricept".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) plcHandler
						.getFilteredChartWithExamType("ARCP",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("ARCP");
				pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean, "Aricept",
						fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Aricept"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if ("Celebrex".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) plcHandler
						.getFilteredChartWithExamType("CLBR",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("CLBR");
				pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean,
						"Celebrex", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Celebrex"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if ("Rebif".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) plcHandler
						.getFilteredChartWithExamType("REBF",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("REBF");
				pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean, "Rebif",
						fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Rebif"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if ("Lyrica".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) plcHandler
						.getFilteredChartWithExamType("LYRC",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("LYRC");
				pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean, "Lyrica",
						fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Lyrica"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if ("General Session".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) plcHandler
						.getFilteredChart("PLCA", filterForm.getArea(),
								filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("PLCA");
				pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean,
						"General Session", fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PLC,
								"General Session"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			} else if ("Overall".equalsIgnoreCase(qStrings.getType())) {
				thisPOAChartBean = (POAChartBean[]) plcHandler
						.getFilteredPLCOverallChart("Overall",
								filterForm.getArea(), filterForm.getRegion(),
								filterForm.getDistrict(), filterForm.getTeam());
				uSession.getUserFilter().setProdcut("Overall");
				pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
						getRequest().getSession(), thisPOAChartBean, "Overall",
						fromFilter);
				chart = new ChartDetailWc(pieChart, "Attendance chart",
						new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Overall"));
				chart.setLayout(ChartDetailWc.LAYOUT_ALT);
			}

			if ("Overall".equalsIgnoreCase(qStrings.getType())) {
				employees = eHandler.getPLCOverAllEmployees(uFilter,
						qStrings.getType(), qStrings.getSection());
			} else {
				employees = eHandler.getPLCEmployees(uFilter,
						qStrings.getType(), qStrings.getSection());
			}

			// poaEmployee
			// =eHandler.getPOAEmployees(uFilter,qStrings.getType(),qStrings.getSection());
			// System.out.println("poaEmployee length in JPF is "+poaEmployee.length);
			if (pieChart.getCount() == 0)
				chart = null;
			page = new ListReportWpc(uSession.getUser(), uFilter, chart,
					employees, "PLC");
			uFilter.setClusterCode(uSession.getUser().getCluster());
			Employee areaManager = eHandler.getAreaManager(uFilter);
			Employee regionManager = eHandler.getRegionManager(uFilter);
			Employee districtManager = eHandler.getDistrictManager(uFilter);
			((ListReportWpc) page).setAreaManager(areaManager);
			((ListReportWpc) page).setRegionManager(regionManager);
			((ListReportWpc) page).setDistrictManager(districtManager);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			uSession.setOverallProcessor(null);
			return new String("success");

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
	 * By  protected Forward getFilteredChartPLC(){
	 */

	public String getFilteredChartPLC() {
		try {

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			TerritoryFilterForm filterForm = uSession
					.getNewTerritoryFilterForm();
			FormUtil.loadObject(getRequest(), filterForm);
			ServiceFactory factory = Service.getServiceFactory(trDb);
			// POAHandler poaHandler = factory.getPOAHandler();
			PLCHandler plcHandler = factory.getPLCHandler();
			List charts = new ArrayList();
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"PLC");
			filterForm = uSession.getUserFilterForm();
			FormUtil.loadObject(getRequest(), filterForm);

			UserFilter uFilter = uSession.getUserFilter();
			// Total Candidates Participating:
			int totalParticipants = plcHandler.getOverAllTotalCount("Overall",
					filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
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
					totalParticipants, uFilter.getProduct(), teamDesc);

			PlcChartsWc chartpage = new PlcChartsWc(filterForm,
					uSession.getUser(), chartHeaderWc);
			PieChartBuilder pBuilder = new PieChartBuilder();
			ChartDetailWc chartdetail = null;
			POAChartBean[] chartBean;
			PieChart pieChart = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);
			boolean fromFilter = false;

			// Get The Count For the Aricept
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("ARCP", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder
					.getPlcChart(uSession.getUserFilter(), getRequest()
							.getSession(), chartBean, "Aricept", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Aricept"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Celebrex
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("CLBR", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Celebrex",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Celebrex"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the
			// Geodon//thisPOAChartBean=trDb.getPOAChartsForAll("GEOD");
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("GEOD", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Geodon", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Geodon"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Lyrica
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("LYRC", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Lyrica", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Lyrica"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Rebif
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("REBF", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Rebif", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Rebif"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the PLCA
			chartBean = (POAChartBean[]) plcHandler.getFilteredChart("PLCA",
					filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "General Session",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC,
							"General Session"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Overall
			chartBean = (POAChartBean[]) plcHandler.getFilteredPLCOverallChart(
					"Overall", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			// thisPOAChartBean=trDb.getPOAOverallChart();
			pieChart = pBuilder
					.getPlcChart(uSession.getUserFilter(), getRequest()
							.getSession(), chartBean, "Overall", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Overall"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			// Changed from Calling charts() for performance.
			return new String("success");
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="/WEB-INF/JSP/PDFHS/GeneralSession.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward generalSessionAttendance(){
	 */

	public String generalSessionAttendance() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			AdminReportHandler handler = new AdminReportHandler();
			GeneralSessionEmployee[] employees = handler
					.getGeneralSessionEmployees(AppConst.EVENT_PDF);
			getRequest().setAttribute(GeneralSessionEmployee.ATTRIBUTE_NAME,
					employees);
			return new String("success");
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	private HashMap createHashMapForEmployees(GeneralSessionEmployee[] employees) {
		HashMap retHash = new HashMap();
		for (int i = 0; i < employees.length; i++) {
			GeneralSessionEmployee curr = employees[i];
			retHash.put(curr.getEmplId(),
					curr.getAttended() != null ? curr.getAttended() : " ");
		}
		return retHash;
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="/WEB-INF/JSP/PDFHS/GeneralSession.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward updateGeneralSession(){
	 */

	public String updateGeneralSession() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			UserSession uSession;
			String sUser = null;

			uSession = UserSession.getUserSession(getRequest());
			if (uSession != null) {
				User user = uSession.getUser();
				if (user != null)
					sUser = user.getEmplid();
			}

			if (sUser == null)
				sUser = "-2";

			AdminReportHandler handler = new AdminReportHandler();

			String sEmplIDs = (String) getRequest().getParameter("emplids");
			String sCheckedStatusList = (String) getRequest().getParameter(
					"checkedstatus");
			String sPreCheckedStatusList = (String) getRequest().getParameter(
					"precheckedstatus");

			// This is list of EmpID's new checked list on General Session Entry
			// Screen
			HashMap hashBeforeEntry = Util.hashMapFromcommaDelimitedStrings(
					sEmplIDs, sPreCheckedStatusList);
			HashMap hashAfterEntry = Util.hashMapFromcommaDelimitedStrings(
					sEmplIDs, sCheckedStatusList);
			HashMap hashEntryDiff = Util.hashMapDiff(hashBeforeEntry,
					hashAfterEntry);

			handler.batchUpdateAttendance(AppConst.EVENT_PDF, hashEntryDiff,
					sUser);

			GeneralSessionEmployee[] employees = handler
					.getGeneralSessionEmployees(AppConst.EVENT_PDF);
			getRequest().setAttribute(GeneralSessionEmployee.ATTRIBUTE_NAME,
					employees);

			if (hashEntryDiff != null && hashEntryDiff.size() > 0)
				getRequest().setAttribute(
						"status",
						"Attendance status updated for " + hashEntryDiff.size()
								+ " employees");

			return new String("success");
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
	 * By  protected Forward PDFHSReportPersonalAgenda(){
	 */

	public String PDFHSReportPersonalAgenda() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
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

			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

			String downloadExcel = getRequest().getParameter("downloadExcel");

			AdminReportHandler handler = new AdminReportHandler();
			EmpReport[] empReport = handler
					.getPersonalizedAgendaReport(AppConst.EVENT_PDF);
			PersonalAgendaWc main = new PersonalAgendaWc(empReport,
					AppConst.EVENT_PDF);
			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"PDF - Personalized Agenda.xls\"");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");

				return new String("successXls");
			}
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_PDF, "PDFHSREPORT");
			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward beginPLC(){
	 */

	public String beginPLC() {
		try {

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "PowersPLC");
			boolean fromFilter = false;

			PieChart pieChart;

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "",
					"PLC");

			TerritoryFilterForm filterForm = uSession.getUserFilterForm();
			String teamDesc = "All";
			ServiceFactory factory = Service.getServiceFactory(trDb);
			PLCHandler plcHandler = factory.getPLCHandler();
			UserFilter uFilter = uSession.getUserFilter();
			if (filterForm.getTeamList().size() <= 0) {
				TeamBean[] allTeam = null;
				// Load data into Listbox;
				allTeam = trDb.getAllPDFHSTEAM();
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
				// We need to get the team description
				teamDesc = trDb.getTeamDescription(filterForm.getTeam());
			}
			// Total Candidates Participating:
			int totalParticipants = plcHandler.getOverAllTotalCount("Overall",
					filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			Territory terr = Service.getServiceFactory(trDb)
					.getTerritoryHandler().getTerritory(uFilter);

			// this component puts the header info in the chart page.
			ChartHeaderWc chartHeaderWc = new ChartHeaderWc(terr.getAreaDesc(),
					terr.getRegionDesc(), terr.getDistrictDesc(),
					totalParticipants, uFilter.getProduct(), teamDesc);

			PlcChartsWc chartpage = new PlcChartsWc(filterForm,
					uSession.getUser(), chartHeaderWc);

			List charts = new ArrayList();

			PieChartBuilder pBuilder = new PieChartBuilder();

			qStrings.setSection("");
			POAChartBean[] chartBean;
			// POAChartBean[] thisPOAChartBean;
			ChartDetailWc chartdetail = null;
			uSession.getUserFilter().getQuseryStrings().setSection(null);

			// Get The Count For the Aricept
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("ARCP", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder
					.getPlcChart(uSession.getUserFilter(), getRequest()
							.getSession(), chartBean, "Aricept", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Aricept"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Celebrex
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("CLBR", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Celebrex",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Celebrex"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the
			// Geodon//thisPOAChartBean=trDb.getPOAChartsForAll("GEOD");
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("GEOD", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Geodon", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Geodon"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Lyrica
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("LYRC", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Lyrica", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Lyrica"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Rebif
			chartBean = (POAChartBean[]) plcHandler
					.getFilteredChartWithExamType("REBF", filterForm.getArea(),
							filterForm.getRegion(), filterForm.getDistrict(),
							filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "Rebif", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Rebif"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the PLCA
			chartBean = (POAChartBean[]) plcHandler.getFilteredChart("PLCA",
					filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			pieChart = pBuilder.getPlcChart(uSession.getUserFilter(),
					getRequest().getSession(), chartBean, "General Session",
					fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC,
							"General Session"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			// Get The Count For the Overall
			chartBean = (POAChartBean[]) plcHandler.getFilteredPLCOverallChart(
					"Overall", filterForm.getArea(), filterForm.getRegion(),
					filterForm.getDistrict(), filterForm.getTeam());
			// thisPOAChartBean=trDb.getPOAOverallChart();
			pieChart = pBuilder
					.getPlcChart(uSession.getUserFilter(), getRequest()
							.getSession(), chartBean, "Overall", fromFilter);
			chartdetail = new ChartDetailWc(pieChart, "Attendance chart",
					new ChartLegendWc(ChartLegendWc.LAYOUT_PLC, "Overall"));
			if (pieChart.getCount() > 0)
				charts.add(chartdetail);

			ChartListWc chartListWc = new ChartListWc(charts);
			// Add the Overall Trainees;
			chartListWc.setTrainees(pieChart.getCount());
			chartpage.setWebComponent(chartListWc);
			page.setMain(chartpage);

			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * By  protected Forward PDFHSReportClassRoster(){
	 */

	public String PDFHSReportClassRoster() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
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
					AppConst.EVENT_PDF);
			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse().addHeader("content-disposition",
						"attachment;filename=\"PDF - Class Roster.xls\"");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");

				return new String("successXls");
			}
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_PDF, "PDFHSREPORT");
			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
	 * By  protected Forward PDFHSReportGeneralSessionAttendance(){
	 */

	public String PDFHSReportGeneralSessionAttendance() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
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
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			ClassFilterForm form = new ClassFilterForm();

			if (!"true".equalsIgnoreCase(downloadExcel)) {
				FormUtil.loadObject(getRequest(), form);
			} else {
				form.setClassroom(getRequest().getParameter(
						form.FIELD_CLASSROOM));
				form.setTrainingDate(getRequest().getParameter(
						form.FIELD_TRAININGDATE));
			}

			if (form.getTrainingDate() == null
					|| "".equals(form.getTrainingDate())) {
				form.setTrainingDate("All");
			}
			if (form.getClassRoom() == null || "".equals(form.getClassRoom())) {
				form.setClassroom("All");
			}

			// HardCoded For General Session
			form.setProduct("PLCA");

			AdminReportHandler handler = new AdminReportHandler();
			EmpReport[] empReport = handler.getAttendanceReport(
					AppConst.EVENT_PDF, form);

			ClassRosterBean[] classData = handler.getClassData(
					AppConst.EVENT_PDF, "PLCA");
			GeneralSessionWc main = new GeneralSessionWc(form, classData,
					empReport, AppConst.EVENT_PDF);
			if ("true".equalsIgnoreCase(downloadExcel)) {
				WebPageComponent page;
				page = new BlankTemplateWpc(main);

				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getRequest().setAttribute("exceldownload", "true");

				getResponse()
						.addHeader("content-disposition",
								"attachment;filename=\"PDF - General Session Attendance.xls\"");

				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0"); // HTTP
																		// 1.1
				getResponse().setHeader("Pragma", "public");

				return new String("successXls");
			}
			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					AppConst.EVENT_PDF, "PDFHSREPORT");
			page.setMain(main);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			return new String("success");
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
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			request.getSession().setAttribute("ReportType", "PDFHS");

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

			MainTemplateWpc page = new MainTemplateWpc(user, "PDF",
					"PDFHSTOOLS");

			page.setMain(new admintoolsSelectPDFHSWc(user));
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);
			return new String("index");
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="/WEB-INF/JSP/PDFHS/GenerateP2L.jsp"
	 * @jpf:forward name="download"
	 *              path="/WEB-INF/JSP/PDFHS/GenerateP2LDownload.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here --> Added
	 * By  protected Forward generateP2L(){
	 */

	public String generateP2L() {
		try {
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
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
					return new String("success");
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
			return new String("success");
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

}
