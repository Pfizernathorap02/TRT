package com.pfizer.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.BUnitBean;
import com.pfizer.db.DelegateBean;
import com.pfizer.db.DelegatedEmp;
import com.pfizer.db.EmpSearch;
import com.pfizer.db.Employee;
import com.pfizer.db.GroupBean;
import com.pfizer.db.MenuList;
import com.pfizer.db.P2lActivityStatus;
import com.pfizer.db.Product;
import com.pfizer.db.RoleBean;
import com.pfizer.db.SalesOrgBean;
import com.pfizer.db.UserGroups;
import com.pfizer.hander.ArchivedHandler;
import com.pfizer.hander.DelegateHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.P2lHandler;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.email.EmailListener;
import com.pfizer.webapp.report.ReportBuilder;
import com.pfizer.webapp.search.EmplSearchForm;
import com.pfizer.webapp.search.EmployeeSearch;
import com.pfizer.webapp.search.EmployeeSearchPDFHS;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.EmployeeGapReportWc;
import com.pfizer.webapp.wc.components.ErrorFormWc;
import com.pfizer.webapp.wc.components.ErrorWc;
import com.pfizer.webapp.wc.components.PreviewHomePageWc;
import com.pfizer.webapp.wc.components.ProductSelectWc;
import com.pfizer.webapp.wc.components.RBUControlPanelWc;
import com.pfizer.webapp.wc.components.ReportSelectPDFHSWc;
import com.pfizer.webapp.wc.components.ReportSelectRBUWc;
import com.pfizer.webapp.wc.components.ReportSelectSPFHSWc;
import com.pfizer.webapp.wc.components.ReportSelectWc;
import com.pfizer.webapp.wc.components.TrainingPathDisplayWc;
import com.pfizer.webapp.wc.components.UnauthorizedRoleWC;
import com.pfizer.webapp.wc.components.UnauthorizedWc;
import com.pfizer.webapp.wc.components.archivedPageWc;
import com.pfizer.webapp.wc.components.newHomePageWc;
import com.pfizer.webapp.wc.components.admin.ActivityDrillDownAdminWc;
import com.pfizer.webapp.wc.components.report.global.EmployeeInfoWc;
import com.pfizer.webapp.wc.components.search.AllEmployeeSearchFormWc;
import com.pfizer.webapp.wc.components.search.AllEmployeeSearchWc;
import com.pfizer.webapp.wc.components.search.DelegateAccessWc;
import com.pfizer.webapp.wc.components.search.DelegateSearchFormWc;
import com.pfizer.webapp.wc.components.search.DelegateSearchResultListWc;
import com.pfizer.webapp.wc.components.search.DelegateSearchWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchPDFHSWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchPOAWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchWc;
import com.pfizer.webapp.wc.components.search.SimulateSearchFormWc;
import com.pfizer.webapp.wc.components.search.SimulateSearchWc;
import com.pfizer.webapp.wc.components.search.SwitchUserWc;
import com.pfizer.webapp.wc.global.HeaderSEARCHWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.MailUtil;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.printing.LoggerHelper;

@SuppressWarnings("serial")
public class HomeAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	private TransactionDB trDb = new TransactionDB();
	public static final Log log = LogFactory.getLog(HomeAction.class);
	private static final String TSR_ADMIN = "TSR Admin";
	private static final String ADMIN = "ADMIN";
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;

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

	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward begin(){
	 */

	public String begin() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			System.out.print("Step1- Begin");
			UserSession uSession = null;
			User user = null;
			try {
				IAMUserControl upControl = new IAMUserControl();
				System.out.print("Step2- Begin");
				upControl.loginFull(getRequest(), getResponse());
				uSession = (UserSession) getRequest().getSession(true)
						.getAttribute(UserSession.ATTRIBUTE);
				user = uSession.getUser();
				System.out.print("Step3- in home.jpf");
				System.out.print("Step3- in home.jpf Uswer is " + user);
				// commented as part of IAM Implementation
				/*
				 * if (getRequest().getParameter("authid") != null) { if ( user
				 * != null) {
				 * 
				 * String url =
				 * (String)getRequest().getSession(true).getAttribute
				 * ("bounceBack");
				 * 
				 * if (!Util.isEmpty(url) && !"null".equals(url)) { // redirect
				 * of clicked from email //added by Shannon for debugging
				 * System.
				 * out.println("bounce back in home.jpf - url redirect -  " +
				 * url); LoggerHelper.logSystemDebug(
				 * "The url here is ##########################" + url); // Added
				 * for testing the redirect of URL's if (
				 * url.indexOf("email=true") > 0 ){
				 * getRequest().getSession(true).removeAttribute("bounceBack");
				 * getResponse().sendRedirect(url); return null; } if (
				 * url.indexOf("emplid=") > 0) { LoggerHelper.logSystemDebug(
				 * "The url has emplid so remove bounce back ############");
				 * getRequest().getSession(true).removeAttribute("bounceBack");
				 * getResponse().sendRedirect(url); return null; } } } }
				 */
			} catch (Exception e) {
				log.error(e, e);
			}

			if (user != null) {
				if (user.getValidUser()) {
					// Added for TRT Phase 2 major enhancement F1 (Archived
					// page)
					if (user.isAdmin() || user.isHQUser()) {
						System.out
								.println("\nuser is admin so normal home page will be displayed");
						return reportselect();
					} else {
						System.out
								.println("\nuser is not admin so new home page will be displayed");
						return newHomePage();
					}
					// end
				} else {
					return unauthorized();
				}
			} else {
				return login();
			}

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// Added for TRT Phase 2 Major enhancement
	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */

	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward newHomePage(){
	 */
	public String newHomePage() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();
			String empid;

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession = UserSession.getUserSession(getRequest());

			User user = uSession.getUser();
			// UserFilter uFilter = uSession.getUserFilter();
			String emplyID = getRequest().getParameter("emplid");

			EmployeeHandler eHandler = new EmployeeHandler();
			// user = uSession.getUser( user.getEmplid() );
			// empid = qStrings.getEmplid();

			// Remove session attribute of employee details page.
			HttpSession session = getSession();
			session.removeAttribute("breadCrumbs");
			// Added the comment for switch user functionality
			if (!Util.isEmpty(emplyID) /* && uSession.isAdmin() */) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE
				// );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(emplyID);

			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
				emplyID = user.getEmplid();

			}

			// if (uSession.getIsDelegatedUser() == null) {
			uSession.setIsDelegatedUser(null);
			DelegateHandler DHandler = new DelegateHandler();
			// List result = DHandler.getDelegatedFromUserList("008637");
			List result = DHandler.getDelegatedFromUserList(emplyID);

			if (result.size() > 0) {
				LoggerHelper.logSystemDebug("TRTtest21");
				// uSession.setIsDelegatedUser("008637");
				uSession.setIsDelegatedUser(emplyID);
			}
			// }

			// Get employee information.
			// Employee emp = eHandler.getEmployeeById("008637");
			Employee emp = eHandler.getEmployeeById(emplyID);

			// build employee info section
			EmployeeInfoWc employeeInfo = new EmployeeInfoWc(emp);
			employeeInfo.setImage(eHandler.getEmployeeImage(emp.getEmplId()));
			employeeInfo.setManager(eHandler.getEmployeeById(emp
					.getReportsToEmplid()));

			// Get Reporting hierarchy details.
			List reportingEmployeeDetailsList = new ArrayList();
			// reportingEmployeeDetailsList=eHandler.getReportingEmployeeDetails("008637");
			// neha--begin
			if (user.isSpecialRole()) {
				emplyID = user.getEmplIdForSpRole();
			}
			// neha--end
			reportingEmployeeDetailsList = eHandler
					.getReportingEmployeeDetails(emplyID);

			// Get Gap report
			List statusForGapList = new ArrayList();
			// statusForGapList=eHandler.getProductStatusForGap("009228");
			statusForGapList = eHandler.getProductStatusForGap(emplyID);
			List statusForNotGapList = new ArrayList();
			// statusForNotGapList=eHandler.getProductStatusForNotGap("009228");
			statusForNotGapList = eHandler.getProductStatusForNotGap(emplyID);
			EmployeeGapReportWc empGapWc = new EmployeeGapReportWc();
			empGapWc.setStatusForGapList(statusForGapList);
			empGapWc.setStatusForNotGapList(statusForNotGapList);
			empGapWc.setUser(emplyID);

			// Get Training path for the employee.
			List empTrainingPath = new ArrayList();
			List empTrainingPathDisplay = new ArrayList();
			empTrainingPath = eHandler.getTrainingPath(emp);
			P2lHandler pHandler = new P2lHandler();
			TrainingPathDisplayWc pathWc = new TrainingPathDisplayWc();
			pathWc.setUser(emplyID);
			Map displayPath = new HashMap();
			int i = 1;
			// if (Util.ifNull(empTrainingPath) != ""){
			if (empTrainingPath.size() != 0) {
				Iterator pathIterator = empTrainingPath.iterator();

				while (pathIterator.hasNext()) {
					Map pathCode = (Map) pathIterator.next();

					String activityID = (String) pathCode.get("CODE");
					P2lActivityStatus testresult = pHandler
							.getSinglePhaseDetail(emp.getGuid(), activityID);
					pathCode.put("STATUS", testresult.getStatus());
					displayPath = pathCode;
					empTrainingPathDisplay.add(displayPath);
					i++;
				}
			} else {
				pathWc.setMessage("No Training Path Configured.");
			}

			pathWc.setTrainingPath(empTrainingPathDisplay);

			MainTemplateWpc page = new MainTemplateWpc(user, "newHomePage");
			newHomePageWc wc = new newHomePageWc(user, employeeInfo, pathWc,
					empGapWc);
			wc.setReportingEmployeeDetailsList(reportingEmployeeDetailsList);

			page.setMain(wc);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/** Infosys - Weblogic to Jboss migration changes ends here */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// Start: Modified for TRT Phase 2 - Requirement no. F4
	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward allEmployeeSearch(){
	 */
	public String allEmployeeSearch() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}

			UserSession uSession = (UserSession) getRequest().getSession(true)
					.getAttribute(UserSession.ATTRIBUTE);
			User user = null;
			if (uSession != null) {
				user = uSession.getUser();

			}

			if (uSession == null) {
				return unauthorized();
			}
			ServiceFactory factory = Service.getServiceFactory();
			EmployeeHandler eHandler = factory.getEmployeeHandler();

			// Remove session attribute of employee details page.
			HttpSession session = getSession();
			session.removeAttribute("breadCrumbs");

			/* Adding code for RBU changes */
			if (uSession.getIsDelegatedUser() == null) {
				// System.out.println("emp id2"+qStrings.toString());
				DelegateHandler DHandler = new DelegateHandler();
				List result = DHandler.getDelegatedFromUserList(user
						.getEmplid());

				if (result.size() > 0) {
					LoggerHelper.logSystemDebug("TRTtest21");
					uSession.setIsDelegatedUser(user.getEmplid());
				}
			}

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
			if (eForm.getSalOrgList().size() <= 0) {
				SalesOrgBean[] allSalesOrg = null;
				allSalesOrg = trDb.getAllSalesOrg();
				LabelValueBean labelValueBean;
				eForm.setSalesorg("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setSalOrgList(labelValueBean);
				for (int i = 0; i < allSalesOrg.length; i++) {
					labelValueBean = new LabelValueBean(
							allSalesOrg[i].getSalesOrgDesc(),
							allSalesOrg[i].getSalesOrgDesc());
					eForm.setSalOrgList(labelValueBean);
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

			MainTemplateWpc page = new MainTemplateWpc(user, "simulateuser");
			// page.setMain( new HomepageWc( svpList, vpList, rmList, dmList,
			// rcList
			// ) );
			AllEmployeeSearchWc esearch = new AllEmployeeSearchWc(eForm,
					new ArrayList());
			AllEmployeeSearchFormWc searchFormWc = new AllEmployeeSearchFormWc(
					eForm);

			searchFormWc.setUserEmplID(user.getEmplid());
			searchFormWc.setIsEmplAdmin(user.isAdmin());

			esearch.setSearchForm(searchFormWc);
			// page.setMain(new AllEmployeeSearchFormWc(eForm));
			page.setMain(searchFormWc);
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward allEmployeeSearchList(){
	 */
	public String allEmployeeSearchList() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}
			EmplSearchForm form = new EmplSearchForm();
			if (form.getBuList().size() <= 0) {
				BUnitBean[] allBu = null;
				allBu = trDb.getAllBusinessUnits();
				LabelValueBean labelValueBean;
				form.setBu("All");
				FormUtil.loadObject(getRequest(), form);
				labelValueBean = new LabelValueBean("All", "All");
				form.setBuList(labelValueBean);
				for (int i = 0; i < allBu.length; i++) {
					labelValueBean = new LabelValueBean(
							allBu[i].getBunitDesc(), allBu[i].getBunitDesc());
					form.setBuList(labelValueBean);
				}
			}
			if (form.getSalOrgList().size() <= 0) {
				SalesOrgBean[] allSalesOrg = null;
				allSalesOrg = trDb.getAllSalesOrg();
				LabelValueBean labelValueBean;
				form.setSalesorg("All");
				FormUtil.loadObject(getRequest(), form);
				labelValueBean = new LabelValueBean("All", "All");
				form.setSalOrgList(labelValueBean);
				for (int i = 0; i < allSalesOrg.length; i++) {
					labelValueBean = new LabelValueBean(
							allSalesOrg[i].getSalesOrgDesc(),
							allSalesOrg[i].getSalesOrgDesc());
					form.setSalOrgList(labelValueBean);
				}
			}
			if (form.getRoleList().size() <= 0) {
				RoleBean[] allRoles = null;
				allRoles = trDb.getAllRoleDesc();
				LabelValueBean labelValueBean;
				form.setRole("All");
				FormUtil.loadObject(getRequest(), form);
				labelValueBean = new LabelValueBean("All", "All");
				form.setRoleList(labelValueBean);
				for (int i = 0; i < allRoles.length; i++) {
					labelValueBean = new LabelValueBean(
							allRoles[i].getRoleDesc(), allRoles[i].getRoleCd());
					form.setRoleList(labelValueBean);
				}
			}

			FormUtil.loadObject(getRequest(), form);
			UserSession uSession = UserSession.getUserSession(getRequest());
			User user = uSession.getUser();
			EmployeeSearch es = new EmployeeSearch();

			List ret = new ArrayList();

			if (!Util.isEmpty(form.getLname())
					|| !Util.isEmpty(form.getFname())
					|| !Util.isEmpty(form.getEmail())
					|| !Util.isEmpty(form.getEmplid())
					|| !Util.isEmpty(form.getBu())
					|| !Util.isEmpty(form.getSalesorg())
					|| !Util.isEmpty(form.getRole())) {
				ret = es.getAllEmployees(form, uSession);
			}
			MainTemplateWpc page = new MainTemplateWpc(user, "search");
			/*
			 * Working page.setMain( new AllEmployeeSearchWc(form,ret) );
			 * getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);
			 * page.setLoginRequired(true);
			 */

			AllEmployeeSearchWc esearch = new AllEmployeeSearchWc(form, ret);
			AllEmployeeSearchFormWc searchFormWc = new AllEmployeeSearchFormWc(
					form);

			searchFormWc.setUserEmplID(user.getEmplid());
			searchFormWc.setIsEmplAdmin(user.isAdmin());

			esearch.setSearchForm(searchFormWc);
			// page.setMain(new AllEmployeeSearchFormWc(eForm));
			page.setMain(esearch);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/** Infosys - Weblogic to Jboss migration changes ends here */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// End: Modified for TRT Phase 2
	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward simulateuser(){
	 */
	public String simulateuser() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}

			UserSession uSession = (UserSession) getRequest().getSession(true)
					.getAttribute(UserSession.ATTRIBUTE);
			User user = null;
			if (uSession != null) {
				user = uSession.getUser();
			}

			if (uSession == null || !uSession.isAdmin()) {
				return unauthorized();
			}
			ServiceFactory factory = Service.getServiceFactory();
			EmployeeHandler eHandler = factory.getEmployeeHandler();
			/*
			 * Employee[] vpList = eHandler.getEmployeeByRole(
			 * "'VP','DDO','DAO'" ); //System.out.println("VP:" +
			 * vpList.length); Employee[] svpList = eHandler.getEmployeeByRole(
			 * "'SVP', 'DCO',  'NSD' " );
			 * 
			 * Employee[] rmList = eHandler.getEmployeeByRole(
			 * "'RM','ARM', 'ASD', 'SD','SDIR','SDTL','CAD'" ); Employee[]
			 * rcList = eHandler.getEmployeeByRole( "'RC'" ); Employee[] dmList
			 * = eHandler.getEmployeeByRole( "'DM'" );
			 */
			/*
			 * ReadProperties read = new ReadProperties(); Employee[] vpList =
			 * eHandler.getEmployeeByRole(read.getValue("vpList")); Employee[]
			 * svpList = eHandler.getEmployeeByRole(read.getValue("svpList") );
			 * Employee[] rmList =
			 * eHandler.getEmployeeByRole(read.getValue("rmList") ); Employee[]
			 * rcList = eHandler.getEmployeeByRole(read.getValue("rcList"));
			 * Employee[] dmList =
			 * eHandler.getEmployeeByRole(read.getValue("dmList") );
			 */
			/* Adding code for RBU changes */
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
			if (eForm.getSalOrgList().size() <= 0) {
				SalesOrgBean[] allSalesOrg = null;
				allSalesOrg = trDb.getAllSalesOrg();
				LabelValueBean labelValueBean;
				eForm.setSalesorg("All");
				FormUtil.loadObject(getRequest(), eForm);
				labelValueBean = new LabelValueBean("All", "All");
				eForm.setSalOrgList(labelValueBean);
				for (int i = 0; i < allSalesOrg.length; i++) {
					labelValueBean = new LabelValueBean(
							allSalesOrg[i].getSalesOrgDesc(),
							allSalesOrg[i].getSalesOrgDesc());
					eForm.setSalOrgList(labelValueBean);
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

			MainTemplateWpc page = new MainTemplateWpc(user, "simulateuser");
			// page.setMain( new HomepageWc( svpList, vpList, rmList, dmList,
			// rcList
			// ) );
			SimulateSearchWc esearch = new SimulateSearchWc(eForm,
					new ArrayList());
			SimulateSearchFormWc searchFormWc = new SimulateSearchFormWc(eForm);
			esearch.setSearchForm(searchFormWc);
			page.setMain(new SimulateSearchFormWc(eForm));

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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward simulatesearch(){
	 */
	public String simulatesearch() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}
			EmplSearchForm form = new EmplSearchForm();
			if (form.getBuList().size() <= 0) {
				BUnitBean[] allBu = null;
				allBu = trDb.getAllBusinessUnits();
				LabelValueBean labelValueBean;
				form.setBu("All");
				FormUtil.loadObject(getRequest(), form);
				labelValueBean = new LabelValueBean("All", "All");
				form.setBuList(labelValueBean);
				for (int i = 0; i < allBu.length; i++) {
					labelValueBean = new LabelValueBean(
							allBu[i].getBunitDesc(), allBu[i].getBunitDesc());
					form.setBuList(labelValueBean);
				}
			}
			if (form.getSalOrgList().size() <= 0) {
				SalesOrgBean[] allSalesOrg = null;
				allSalesOrg = trDb.getAllSalesOrg();
				LabelValueBean labelValueBean;
				form.setSalesorg("All");
				FormUtil.loadObject(getRequest(), form);
				labelValueBean = new LabelValueBean("All", "All");
				form.setSalOrgList(labelValueBean);
				for (int i = 0; i < allSalesOrg.length; i++) {
					labelValueBean = new LabelValueBean(
							allSalesOrg[i].getSalesOrgDesc(),
							allSalesOrg[i].getSalesOrgDesc());
					form.setSalOrgList(labelValueBean);
				}
			}
			if (form.getRoleList().size() <= 0) {
				RoleBean[] allRoles = null;
				allRoles = trDb.getAllRoleDesc();
				LabelValueBean labelValueBean;
				form.setRole("All");
				FormUtil.loadObject(getRequest(), form);
				labelValueBean = new LabelValueBean("All", "All");
				form.setRoleList(labelValueBean);
				for (int i = 0; i < allRoles.length; i++) {
					labelValueBean = new LabelValueBean(
							allRoles[i].getRoleDesc(), allRoles[i].getRoleCd());
					form.setRoleList(labelValueBean);
				}
			}

			FormUtil.loadObject(getRequest(), form);
			UserSession uSession = UserSession.getUserSession(getRequest());
			User user = uSession.getUser();
			EmployeeSearch es = new EmployeeSearch();

			List ret = new ArrayList();

			if (!Util.isEmpty(form.getLname())
					|| !Util.isEmpty(form.getFname())
					|| !Util.isEmpty(form.getEmail())
					|| !Util.isEmpty(form.getEmplid())
					|| !Util.isEmpty(form.getBu())
					|| !Util.isEmpty(form.getSalesorg())
					|| !Util.isEmpty(form.getRole())) {
				ret = es.getEmployeesForSimulationSearch(form, uSession);
			}
			MainTemplateWpc page = new MainTemplateWpc(user, "search");

			page.setMain(new SimulateSearchWc(form, ret));
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward originaluser(){
	 */
	public String originaluser() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}

			UserSession uSession = (UserSession) getRequest().getSession(true)
					.getAttribute(UserSession.ATTRIBUTE);
			uSession.reset();

			return reportselect();

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward testemail(){
	 */
	public String testemail() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			EmailListener el = new EmailListener();
			el.handleNotification(null, null);

			MainTemplateWpc page = new MainTemplateWpc(null, "login");
			page.setLoginRequired(false);
			getRequest().setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);

			return null;
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
	}

	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward pdfhsreportselect(){
	 */
	public String pdfhsreportselect() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			getSession().setAttribute("ReportType", "PDFHS");

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
					"PDFHSREPORT");

			page.setMain(new ReportSelectPDFHSWc(user));
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward rbusreportselect(){
	 */
	public String rbusreportselect() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			getSession().setAttribute("ReportType", "RBUREPORT");

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

			MainTemplateWpc page = new MainTemplateWpc(user, "RBU", "RBUREPORT");

			page.setMain(new ReportSelectRBUWc(user));
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward spfreportselect(){
	 */
	public String spfreportselect() {
		try {

			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			getSession().setAttribute("ReportType", "SPF");

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

			MainTemplateWpc page = new MainTemplateWpc(user, "SPF", "SPFREPORT");

			page.setMain(new ReportSelectSPFHSWc(user));
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward reportselect(){
	 */
	public String reportselect() {
		try {

			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			String empid;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			getSession().setAttribute("ReportType", "PDFHS");

			// Start: added for TRT Phase 1 enhancement - Forecast filter
			// criteria-
			// removal of session attributes.
			System.out
					.println("Starting: Removal of session attributes in reportSelect.do");
			HttpSession session = getSession();
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
			session.removeAttribute("minimizeList");
			session.removeAttribute("sectionNamesList");
			System.out
					.println("Ending: Removal of session attributes in reportSelect.do");
			// Ends here
			// added for TRT major enhancement 3.6- F6
			// empid = uSession.getUser().getEmplid();
			// log.info("####################################### TRTP1 logger empid:"+empid);
			// System.out.println("####################################### TRTP1 sysout empid:"+empid);
			/*
			 * if (uSession.getIsDelegatedUser() == null) {
			 * LoggerHelper.logSystemDebug("TRTP1 empid:"+empid);
			 * DelegateHandler DHandler = new DelegateHandler(); List result =
			 * DHandler.getDelegatedFromUserList(empid);
			 * 
			 * if (result.size()> 0) { LoggerHelper.logSystemDebug("TRTtest21");
			 * uSession.setIsDelegatedUser(empid); } }
			 */
			// ends here

			// Remove session attribute of employee details page.
			session.removeAttribute("breadCrumbs");

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE
				// );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
				empid = qStrings.getEmplid();
				if (uSession.getIsDelegatedUser() == null) {
					DelegateHandler DHandler = new DelegateHandler();
					List result = DHandler.getDelegatedFromUserList(empid);

					if (result.size() > 0) {
						LoggerHelper.logSystemDebug("TRTtest21");
						uSession.setIsDelegatedUser(empid);
					}
				}
				if (user.getValidUser()) {
					uSession.setUserFilterForm(uSession
							.getNewTerritoryFilterForm());
				} else {
					return unauthorized();
				}
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
				empid = uSession.getUser().getEmplid();
				if (uSession.getIsDelegatedUser() == null) {
					LoggerHelper.logSystemDebug("del empid:" + empid);
					DelegateHandler DHandler = new DelegateHandler();
					List result = DHandler.getDelegatedFromUserList(empid);

					if (result.size() > 0) {
						LoggerHelper.logSystemDebug("TRTtest21");
						uSession.setIsDelegatedUser(empid);
					}
				}
				uSession.setUserFilterForm(uSession.getNewTerritoryFilterForm());
			}

			/*
			 * if ("Pratt".equals(user.getDisplayCluster()) ||
			 * "Specialty Market".equals(user.getDisplayCluster()) ||
			 * "Steere".equals(user.getDisplayCluster())) { return
			 * productselect(); }
			 */

			MainTemplateWpc page = new MainTemplateWpc(user, "reportselect");
			ReportSelectWc wc = new ReportSelectWc(user);
			renderMainMenu(wc, user.getRole());

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

	// Added for TRT Phase 2 Major enhancement Requirement No. 1
	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward archivedPage(){
	 */
	public String archivedPage() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			String empid;
			uSession = UserSession.getUserSession(getRequest());

			User user = uSession.getUser();
			archivedPageWc wc = new archivedPageWc(user);
			ArchivedHandler aHandler = new ArchivedHandler();
			String headerId = (String) getRequest().getParameter("id");

			wc.setHeaderId(headerId);
			wc.setLabelList(aHandler.getLabelList(headerId));
			if (user.isSuperAdmin() || user.isTsrAdmin()) {

				wc.setlabelUrlList(aHandler.getLabelUrlList(headerId));
			} else {

				wc.setlabelUrlList(aHandler.getLabelUrlListUsers(headerId,
						user.getRole()));
			}
			MainTemplateWpc page = new MainTemplateWpc(user, "archievedPage");

			page.setMain(wc);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);

			return new String("index");
			/** Infosys - Weblogic to Jboss migration changes ends here */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// end

	private void renderMainMenu(ReportSelectWc wc, String role) {
		Hashtable menuRender = new Hashtable();
		Vector headers = new Vector();
		String key = null;
		Vector list = null;
		/* Added for RBU changes */
		MenuList menu = new MenuList();
		UserGroups ug = new UserGroups();
		GroupBean[] userGroupBean = null;
		userGroupBean = trDb.getAllUserGroups();
		ArchivedHandler aHandler = new ArchivedHandler();
		// menu.setAccessList(userGroupBean);
		menu.clearAccessList();
		menu.clearSpecialAccessList();
		// System.out.println("After getting userGroupBean");
		LabelValueBean labelValueBean;
		labelValueBean = new LabelValueBean("All", "");
		menu.setAccessList(labelValueBean);
		menu.setSpecialAccessList(labelValueBean);
		labelValueBean = new LabelValueBean("ADMIN", ADMIN);
		menu.setAccessList(labelValueBean);
		menu.setSpecialAccessList(labelValueBean);
		labelValueBean = new LabelValueBean("TSR Admin", TSR_ADMIN);
		menu.setSpecialAccessList(labelValueBean);
		for (int i = 0; i < userGroupBean.length; i++) {
			labelValueBean = new LabelValueBean(
					userGroupBean[i].getGroupName(),
					userGroupBean[i].getGroupName());
			menu.setAccessList(labelValueBean);
			menu.setSpecialAccessList(labelValueBean);
		}
		MenuList[] menuList = trDb.renderMainMenu();

		List result;
		int count;
		List ls = new ArrayList();
		boolean archiveFlag = false;
		for (int i = 0; i < menuList.length; i++) {
			if (menuList[i].getLevel().equalsIgnoreCase("1")) {
				key = menuList[i].getLabel();
				/* Added for RBU */
				String groupName = menuList[i].getAllow();
				// Added for TRT Phase 2 - Requirement no. F1
				String minimize = menuList[i].getMinimize();
				// Added for TRT major enhancement Requirement no.1 (Archived
				// Reports)
				if (role.equals("SUPER ADMIN")) {
					// for super admin
					result = aHandler.getCountArchivedReports(
							menuList[i].getId(), role);
					count = result.size();
				} else {
					// for other users
					result = aHandler.getCountArchivedReportsUsers(
							menuList[i].getId(), role);
					count = result.size();
				}
				// count=result.size();

				if (count > 0) {
					// System.out.println("inside");
					archiveFlag = true;
					menuList[i].setArchiveFlag(archiveFlag);
				}
				// end
				if (groupName != null) {
					/*
					 * Adding Feedback user condition for SCE Feedback form
					 * enhancement
					 */
					if (!groupName.equalsIgnoreCase("ADMIN")
							|| !groupName.equalsIgnoreCase("TSR_ADMIN")) {
						ug = getGroupDetails(groupName);
						if (ug != null) {
							menuList[i].setBusUnit(ug.getSelectedBU());
							menuList[i].setSalesOrg(ug.getSelectedSalesorg());
							menuList[i].setRole(ug.getSelectedRole());
							menuList[i].setFeedbackUsers(ug.getSelectedFBU());
							menuList[i].setHQUsers(ug.getSelectedHQU());
						}
					}
				}
				/* End of addition */
				List sectionDisplay = aHandler.getSectionDisplay(
						menuList[i].getId(), role);
				if (role.equals("SUPER ADMIN")) {
					if (sectionDisplay.size() > 0 || count >= 0) {
						headers.addElement(menuList[i]);
					}
				} else {
					if (sectionDisplay.size() > 0 || count > 0) {
						headers.addElement(menuList[i]);
					}
				}

			} else {
				if (menuRender.get(key) == null) {
					list = new Vector();
					/* Added for RBU */
					String groupName = menuList[i].getAllow();
					if (groupName != null) {
						/*
						 * Adding Feedback user condition for SCE Feedback form
						 * enhancement
						 */
						if (!groupName.equalsIgnoreCase("ADMIN")
								|| !groupName.equalsIgnoreCase("TSR_ADMIN")) {
							ug = getGroupDetails(groupName);
							if (ug != null) {
								menuList[i].setBusUnit(ug.getSelectedBU());
								menuList[i].setSalesOrg(ug
										.getSelectedSalesorg());
								menuList[i].setRole(ug.getSelectedRole());
								menuList[i].setFeedbackUsers(ug
										.getSelectedFBU());
								menuList[i].setHQUsers(ug.getSelectedHQU());
							}
						}
					}
					/* End of addition */

					list.addElement(menuList[i]);

				} else {
					list = (Vector) menuRender.get(key);
					/* Added for RBU */
					String groupName = menuList[i].getAllow();
					if (groupName != null) {
						/*
						 * Adding Feedback user condition for SCE Feedback form
						 * enhancement
						 */
						if (!groupName.equalsIgnoreCase("ADMIN")
								|| !groupName.equalsIgnoreCase("TSR_ADMIN")) {
							ug = getGroupDetails(groupName);
							if (ug != null) {
								menuList[i].setBusUnit(ug.getSelectedBU());
								menuList[i].setSalesOrg(ug
										.getSelectedSalesorg());
								menuList[i].setRole(ug.getSelectedRole());
								menuList[i].setFeedbackUsers(ug
										.getSelectedFBU());
								menuList[i].setHQUsers(ug.getSelectedHQU());
							}
						}
					}
					/* End of addition */
					list.addElement(menuList[i]);

				}
				menuRender.put(key, list);
			}
		}
		// wc.setCount(ls);
		// System.out.println("getCount=="+wc.getCount());
		wc.setMenu(menuRender);
		wc.setMenuHeader(headers);
	}

	// Start : Added for TRT Phase 2 - Requirement no. F1
	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward previewHomePage(){
	 */
	public String previewHomePage() {
		try {

			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			String empid;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			getSession().setAttribute("ReportType", "PDFHS");

			HttpSession session = getSession();

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE
				// );
				getRequest().getSession(true).setAttribute(
						UserSession.ATTRIBUTE, uSession);
				// uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser(qStrings.getEmplid());
				empid = qStrings.getEmplid();
				if (uSession.getIsDelegatedUser() == null) {
					DelegateHandler DHandler = new DelegateHandler();
					List result = DHandler.getDelegatedFromUserList(empid);

					if (result.size() > 0) {
						LoggerHelper.logSystemDebug("TRTtest21");
						uSession.setIsDelegatedUser(empid);
					}
				}
				if (user.getValidUser()) {
					uSession.setUserFilterForm(uSession
							.getNewTerritoryFilterForm());
				} else {
					return unauthorized();
				}
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
				empid = uSession.getUser().getEmplid();
				if (uSession.getIsDelegatedUser() == null) {
					LoggerHelper.logSystemDebug("del empid:" + empid);
					DelegateHandler DHandler = new DelegateHandler();
					List result = DHandler.getDelegatedFromUserList(empid);

					if (result.size() > 0) {
						LoggerHelper.logSystemDebug("TRTtest21");
						uSession.setIsDelegatedUser(empid);
					}
				}
				uSession.setUserFilterForm(uSession.getNewTerritoryFilterForm());
			}

			/*
			 * if ("Pratt".equals(user.getDisplayCluster()) ||
			 * "Specialty Market".equals(user.getDisplayCluster()) ||
			 * "Steere".equals(user.getDisplayCluster())) { return
			 * productselect(); }
			 */
			List sectionNamesList = (List) session
					.getAttribute("sectionNamesList");
			List trackIdList = (List) session.getAttribute("trackIdList");
			List minimizeList = (List) session.getAttribute("minimizeList");
			// System.out.println("get attribute in preview page=="+session.getAttribute("sectionNamesList"));
			MainTemplateWpc page = new MainTemplateWpc(user, "reportselect");

			PreviewHomePageWc wc = new PreviewHomePageWc(user);
			previewRenderMainMenu(wc, user.getRole());
			if (sectionNamesList != null) {
				wc.setSectionNameList(sectionNamesList);
			}
			wc.setTrackIdList(trackIdList);
			wc.setMinimizeList(minimizeList);
			page.setMain(wc);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/** Infosys - Weblogic to Jboss migration changes ends here */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	private void previewRenderMainMenu(PreviewHomePageWc wc, String role) {
		Hashtable menuRender = new Hashtable();
		Vector headers = new Vector();
		String key = null;
		Vector list = null;
		/* Added for RBU changes */
		ArchivedHandler aHandler = new ArchivedHandler();
		MenuList menu = new MenuList();
		UserGroups ug = new UserGroups();
		GroupBean[] userGroupBean = null;
		userGroupBean = trDb.getAllUserGroups();
		// menu.setAccessList(userGroupBean);
		menu.clearAccessList();
		menu.clearSpecialAccessList();
		// System.out.println("After getting userGroupBean");
		LabelValueBean labelValueBean;
		labelValueBean = new LabelValueBean("All", "");
		menu.setAccessList(labelValueBean);
		menu.setSpecialAccessList(labelValueBean);
		labelValueBean = new LabelValueBean("ADMIN", ADMIN);
		menu.setAccessList(labelValueBean);
		menu.setSpecialAccessList(labelValueBean);
		labelValueBean = new LabelValueBean("TSR Admin", TSR_ADMIN);
		menu.setSpecialAccessList(labelValueBean);
		for (int i = 0; i < userGroupBean.length; i++) {
			labelValueBean = new LabelValueBean(
					userGroupBean[i].getGroupName(),
					userGroupBean[i].getGroupName());
			menu.setAccessList(labelValueBean);
			menu.setSpecialAccessList(labelValueBean);
		}
		MenuList[] menuList = trDb.renderMainMenu();

		HttpSession session = getSession();
		String[] sectionNames;
		sectionNames = (String[]) session.getAttribute("sectionNames");

		String[] id;
		id = getRequest().getParameterValues("trackId");

		String[] minimize;
		minimize = (String[]) session.getAttribute("min");

		List result;
		int count;
		List ls = new ArrayList();
		boolean archiveFlag = false;

		for (int i = 0; i < menuList.length; i++) {
			if (menuList[i].getLevel().equalsIgnoreCase("1")) {
				key = menuList[i].getLabel();
				/* Added for RBU */
				String groupName = menuList[i].getAllow();
				String sortorder = menuList[i].getSortorder();
				// System.out.println("sort=="+menuList[i].getSortorder());
				// System.out.println("section name=="+menuList[i].getLabel());
				if (role.equals("SUPER ADMIN")) {
					// for super admin
					result = aHandler.getCountArchivedReports(
							menuList[i].getId(), role);
					count = result.size();
				} else {
					// for other users
					result = aHandler.getCountArchivedReportsUsers(
							menuList[i].getId(), role);
					count = result.size();
				}
				// count=result.size();

				if (count > 0) {
					// System.out.println("inside");
					archiveFlag = true;
					menuList[i].setArchiveFlag(archiveFlag);
				}
				if (groupName != null) {
					/*
					 * Adding Feedback user condition for SCE Feedback form
					 * enhancement
					 */
					if (!groupName.equalsIgnoreCase("ADMIN")
							|| !groupName.equalsIgnoreCase("TSR_ADMIN")) {
						ug = getGroupDetails(groupName);
						if (ug != null) {
							menuList[i].setBusUnit(ug.getSelectedBU());
							menuList[i].setSalesOrg(ug.getSelectedSalesorg());
							menuList[i].setRole(ug.getSelectedRole());
							menuList[i].setFeedbackUsers(ug.getSelectedFBU());
						}
					}
				}
				/* End of addition */
				List sectionDisplay = aHandler.getSectionDisplay(
						menuList[i].getId(), role);
				if (role.equals("SUPER ADMIN")) {
					if (sectionDisplay.size() > 0 || count >= 0) {
						headers.addElement(menuList[i]);
					}
				} else {
					if (sectionDisplay.size() > 0 || count > 0) {
						headers.addElement(menuList[i]);
					}
				}
			} else {
				if (menuRender.get(key) == null) {
					list = new Vector();
					/* Added for RBU */
					String groupName = menuList[i].getAllow();
					if (groupName != null) {
						/*
						 * Adding Feedback user condition for SCE Feedback form
						 * enhancement
						 */
						if (!groupName.equalsIgnoreCase("ADMIN")
								|| !groupName.equalsIgnoreCase("TSR_ADMIN")) {
							ug = getGroupDetails(groupName);
							if (ug != null) {
								menuList[i].setBusUnit(ug.getSelectedBU());
								menuList[i].setSalesOrg(ug
										.getSelectedSalesorg());
								menuList[i].setRole(ug.getSelectedRole());
								menuList[i].setFeedbackUsers(ug
										.getSelectedFBU());
							}
						}
					}
					/* End of addition */
					list.addElement(menuList[i]);
				} else {
					list = (Vector) menuRender.get(key);
					/* Added for RBU */
					String groupName = menuList[i].getAllow();
					if (groupName != null) {
						/*
						 * Adding Feedback user condition for SCE Feedback form
						 * enhancement
						 */
						if (!groupName.equalsIgnoreCase("ADMIN")
								|| !groupName.equalsIgnoreCase("TSR_ADMIN")) {
							ug = getGroupDetails(groupName);
							if (ug != null) {
								menuList[i].setBusUnit(ug.getSelectedBU());
								menuList[i].setSalesOrg(ug
										.getSelectedSalesorg());
								menuList[i].setRole(ug.getSelectedRole());
								menuList[i].setFeedbackUsers(ug
										.getSelectedFBU());
							}
						}
					}
					/* End of addition */
					list.addElement(menuList[i]);
				}
				menuRender.put(key, list);
			}
		}
		wc.setMenu(menuRender);
		wc.setMenuHeader(headers);
	}

	// End

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
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward reportslist(){
	 */
	public String reportslist() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			getSession().setAttribute("ReportType", "FFT");

			if (!Util.isEmpty(qStrings.getEmplid()) && uSession.isAdmin()) {
				getRequest().getSession().invalidate();
				// getRequest().getSession().removeAttribute(
				// UserSession.ATTRIBUTE
				// );
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

			if ("Pratt".equals(user.getDisplayCluster())
					|| "Specialty Market".equals(user.getDisplayCluster())
					|| "Steere".equals(user.getDisplayCluster())) {
				return productselect();
			}

			MainTemplateWpc page = new MainTemplateWpc(user, "reportselect");

			page.setMain(new ReportSelectWc(user));
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward productselect(){
	 */
	public String productselect() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}

			getSession().setAttribute("ReportType", "FFT");

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
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
				uSession.setUserFilterForm(uSession.getNewTerritoryFilterForm());
			} else {
				uSession = UserSession.getUserSession(getRequest());
				user = uSession.getUser();
				uSession.setUserFilterForm(uSession.getNewTerritoryFilterForm());
			}

			MainTemplateWpc page = new MainTemplateWpc(user, "productselect");

			page.setMain(new ProductSelectWc(user));
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);
			return new String("index");
			/** Infosys - Weblogic to Jboss migration changes ends here */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward searchPDFHS(){
	 */
	public String searchPDFHS() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */
			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}
			EmplSearchForm form = new EmplSearchForm();
			FormUtil.loadObject(getRequest(), form);

			UserSession uSession = UserSession.getUserSession(getRequest());
			User user = uSession.getUser();
			boolean bolRefresh = false;
			EmployeeSearchPDFHS es = new EmployeeSearchPDFHS();

			// We will create resultMap for all products for the FirstTime User
			// logs
			// in
			ReportBuilder rb = new ReportBuilder(Service.getServiceFactory());
			List products = user.getProducts();

			String iter_prod = "";
			String sessionKey;

			// Lets check if we need to refresh
			String refresh = (String) getSession().getAttribute("refresh");

			if ("true".equalsIgnoreCase(refresh)) {
				bolRefresh = true;
				log.info("Entering to Refresh");
			}

			for (Iterator iter = products.iterator(); iter.hasNext();) {
				Product thisProd = (Product) iter.next();
				sessionKey = "allProd" + thisProd.getProductCode();

				if (getSession().getAttribute(sessionKey) == null || bolRefresh) {
					OverallProcessor or = rb.getOverallProcessorByProduct(
							uSession, thisProd.getProductCode());
					Map allemps = or.getAllEmployeeMap();
					log.debug("Setting session for " + sessionKey);
					getSession().setAttribute(sessionKey, allemps);

				}
			}

			if (bolRefresh) {
				getSession().removeAttribute("refresh");
				bolRefresh = false;
			}

			List ret = new ArrayList();
			// For PDF and SPF
			String m1 = getRequest().getParameter("m1") == null ? ""
					: getRequest().getParameter("m1");
			if (!Util.isEmpty(form.getLname())
					|| !Util.isEmpty(form.getFname())
					|| !Util.isEmpty(form.getTerrId())
					|| !Util.isEmpty(form.getEmplid())) {
				ret = es.getPDFHSEmployeesByName(form, uSession, m1);
			}
			MainTemplateWpc page = new MainTemplateWpc(user, "detailreport",
					"SEARCH");
			((HeaderSEARCHWc) page.getHeader()).setShowNav(false);
			page.setMain(new EmployeeSearchPDFHSWc(form, ret));
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward searchPOA(){
	 */
	public String searchPOA() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}
			EmplSearchForm form = new EmplSearchForm();
			FormUtil.loadObject(getRequest(), form);

			UserSession uSession = UserSession.getUserSession(getRequest());
			User user = uSession.getUser();
			boolean bolRefresh = false;
			EmployeeSearch es = new EmployeeSearch();

			// We will create resultMap for all products for the FirstTime User
			// logs
			// in
			ReportBuilder rb = new ReportBuilder(Service.getServiceFactory());
			List products = user.getProducts();

			String iter_prod = "";
			String sessionKey;

			// Lets check if we need to refresh
			String refresh = (String) getSession().getAttribute("refresh");

			if ("true".equalsIgnoreCase(refresh)) {
				bolRefresh = true;
				log.info("Entering to Refresh");
			}

			for (Iterator iter = products.iterator(); iter.hasNext();) {
				Product thisProd = (Product) iter.next();
				sessionKey = "allProd" + thisProd.getProductCode();

				if (getSession().getAttribute(sessionKey) == null || bolRefresh) {
					OverallProcessor or = rb.getOverallProcessorByProduct(
							uSession, thisProd.getProductCode());
					Map allemps = or.getAllEmployeeMap();
					log.debug("Setting session for " + sessionKey);
					getSession().setAttribute(sessionKey, allemps);

				}
			}

			if (bolRefresh) {
				getSession().removeAttribute("refresh");
				bolRefresh = false;
			}

			List ret = new ArrayList();

			if (!Util.isEmpty(form.getLname())
					|| !Util.isEmpty(form.getFname())
					|| !Util.isEmpty(form.getTerrId())
					|| !Util.isEmpty(form.getEmplid())) {
				ret = es.getPOAEmployeesByName(form, uSession);
			}
			MainTemplateWpc page = new MainTemplateWpc(user, "detailreport",
					"POA");

			page.setMain(new EmployeeSearchPOAWc(form, ret));

			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
			page.setLoginRequired(true);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/** Infosys - Weblogic to Jboss migration changes ends here */
		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	/**
	 * @jpf:action
	 * 
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward login(){
	 */
	public String login() {
		/** Infosys - Weblogic to Jboss migration changes ends here */
		UserSession uSession = UserSession.getUserSession(getRequest());

		if (uSession.isLoggedIn()) {
			return productselect();
		}

		MainTemplateWpc page = new MainTemplateWpc(null, "login");
		page.setLoginRequired(true);
		getRequest().setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
		return null;
	}

	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward unauthorized(){
	 */
	public String unauthorized() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */
			if (getResponse().isCommitted()) {
				System.out.println("Unauthorized getResponse()");
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

	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward unauthorizedRole(){
	 */
	public String unauthorizedRole() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */
			if (getResponse().isCommitted()) {
				return null;
			}
			UserSession uSession = UserSession.getUserSession(getRequest());
			User user = null;
			if (uSession != null) {
				user = uSession.getUser();
			}
			MainTemplateWpc page = new MainTemplateWpc(user, "UNAUTHORIZED",
					"UNAUTHORIZED");
			page.setMain(new UnauthorizedRoleWC());
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward error(){
	 */
	public String error() {
		
			/** Infosys - Weblogic to Jboss migration changes ends here */
			if (getResponse().isCommitted()) {
				return null;
			}
			String errorSessionKey = getRequest().getParameter("session_key");
			MainTemplateWpc page = new MainTemplateWpc(null, "error");
			page.setMain(new ErrorFormWc(errorSessionKey));
			page.setShowNav(false);
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
		

	}

	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/logErrorConfirm.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward logerror(){
	 */
	public String logerror() {
		
			/** Infosys - Weblogic to Jboss migration changes ends here */
			UserSession uSession = UserSession.getUserSession(getRequest());
			String userId = new String();
			if (uSession != null) {
				User user = uSession.getUser();
				if (user != null) {
					userId = user.getId();
				}
			}
			String errorSessionKey = getRequest().getParameter("session_key");
			String errorRequest = (String) getSession().getAttribute(
					errorSessionKey + ".error.request");
			String userMessage = (String) getRequest().getParameter(
					"description");
			String errorStacktrace = (String) getSession().getAttribute(
					errorSessionKey + ".error.stacktrace");
			//
			// get everything and then email the admin about the error
			//
			StringBuffer message = new StringBuffer(1024);
			message.append("<html><body>");
			if (errorRequest != null) {
				message.append("<STRONG>Error Request</STRONG>: ")
						.append(errorRequest).append("<BR>");
			}

			if (!Util.isEmpty(userId)) {
				message.append("<STRONG>User Id/Employee Id</STRONG>: ")
						.append(userId).append("<p>");
			}
			if (!Util.isEmpty(userMessage)) {
				message.append("<STRONG>User Description</STRONG>: ")
						.append(userMessage).append("<p>");
			}
			if (errorStacktrace != null) {
				message.append("<STRONG>Error Stacktrace</STRONG>: ")
						.append("<BR>").append(errorStacktrace);
			}

			message.append("</html></body>");

			// String fromAddress = EligiblityConstants.TEST_EMAIL_ADDRESS;
			String[] toAddress = new String[1];
			String[] ccAddress = new String[1];
			String[] bccAddress = new String[1];

			try {
				toAddress[0] = "tr_admin@tgix.com";
				ccAddress[0]=" tr_admin@tgix.com";
				bccAddress[0]=" tr_admin@tgix.com";
				// LoggerHelper.logSystemDebug("fromAddress "+fromAddress
				// +" toAddress[0] "+toAddress[0] +
				// "ccAddress[0] "+ccAddress[0]);
				/*MailUtil.sendMessage("tr_admin@tgix.com", toAddress, ccAddress,
						new String[0], "Training Reports Error Notification",
						message.toString(), "text/html", "trMailSession");*/
				MailUtil.sendMessage("tr_admin@tgix.com", toAddress, ccAddress,
						bccAddress, "Training Reports Error Notification",
						message.toString(), "text/html", "trMailSession");
				//
				// if successful, remove the error stuff from the session
				//
				HttpSession session = getSession();
				session.removeAttribute(errorSessionKey + ".error.request");
				session.removeAttribute(errorSessionKey + ".error.message");
				session.removeAttribute(errorSessionKey + ".error.stacktrace");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e, e);
			}
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			 * return new Forward("index");
			 */
			return new String("index");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here -->
			 */
	

	}

	public String errorMain() {
		if (getResponse().isCommitted()) {
			return null;
		}
		String errorSessionKey = getRequest().getParameter("session_key");
		MainTemplateWpc page = new MainTemplateWpc(null, "error");
		/* Pooja */
		/* page.setMain( new ErrorFormWc(errorSessionKey)); */
		page.setMain(new ErrorWc(errorSessionKey));
		page.setShowNav(false);
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);

		return new String("index");

	}

	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward search(){
	 */
	public String search() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */
			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}
			EmplSearchForm form = new EmplSearchForm();
			FormUtil.loadObject(getRequest(), form);

			UserSession uSession = UserSession.getUserSession(getRequest());
			User user = uSession.getUser();
			boolean bolRefresh = false;
			EmployeeSearch es = new EmployeeSearch();

			// We will create resultMap for all products for the FirstTime User
			// logs
			// in
			ReportBuilder rb = new ReportBuilder(Service.getServiceFactory());
			List products = user.getProducts();

			String iter_prod = "";
			String sessionKey;

			// Lets check if we need to refresh
			String refresh = (String) getSession().getAttribute("refresh");

			if ("true".equalsIgnoreCase(refresh)) {
				bolRefresh = true;
				log.info("Entering to Refresh");
			}

			for (Iterator iter = products.iterator(); iter.hasNext();) {
				Product thisProd = (Product) iter.next();
				sessionKey = "allProd" + thisProd.getProductCode();

				if (getSession().getAttribute(sessionKey) == null || bolRefresh) {
					OverallProcessor or = rb.getOverallProcessorByProduct(
							uSession, thisProd.getProductCode());
					Map allemps = or.getAllEmployeeMap();
					log.debug("Setting session for " + sessionKey);
					getSession().setAttribute(sessionKey, allemps);

				}
			}

			if (bolRefresh) {
				getSession().removeAttribute("refresh");
				bolRefresh = false;
			}

			List ret = new ArrayList();

			if (!Util.isEmpty(form.getLname())
					|| !Util.isEmpty(form.getFname())
					|| !Util.isEmpty(form.getTerrId())
					|| !Util.isEmpty(form.getEmplid())) {
				ret = es.getEmployeesByName(form, uSession);
			}
			MainTemplateWpc page = new MainTemplateWpc(user, "search");

			page.setMain(new EmployeeSearchWc(form, ret));
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

	private void callSecurePage() {
		SuperWebPageComponents tpage = new BlankTemplateWpc();
		tpage.setLoginRequired(true);
		IAMUserControl upControl = new IAMUserControl();
		upControl.checkAuth(getRequest(), getResponse(), tpage);
	}

	public void beforeAction() {
		ServiceFactory factory = Service.getServiceFactory(trDb);

	}

	public void afterAction() {

		try {
			UserSession uSession = (UserSession) getRequest().getSession(true)
					.getAttribute(UserSession.ATTRIBUTE);
			if (uSession == null) {
				// System.out.println("uSession is null");
			} else {
				if (uSession.getUser() != null) {
					return;
				}
			}
			SuperWebPageComponents page = (SuperWebPageComponents) getRequest()
					.getAttribute(SuperWebPageComponents.ATTRIBUTE_NAME);
			IAMUserControl upControl = new IAMUserControl();
			upControl.checkAuth(getRequest(), getResponse(), page);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward rbucontrolpanel(){
	 */
	public String rbucontrolpanel() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */
			callSecurePage();

			if (getResponse().isCommitted()) {
				return null;
			}
			// To check whether the user is authorised to do the operation

			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			getSession().setAttribute("ReportType", "PSCPT");

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
			// Check whether the user is super admin. Else display the message
			// not
			// authorized.
			if (user.getRole().equals(User.USER_TYPE_SUPER_ADMIN)) {

				MainTemplateWpc page = new MainTemplateWpc(user, "PSCPT",
						"PSCPT");

				page.setMain(new RBUControlPanelWc(user));
				getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME, page);
				page.setLoginRequired(true);
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes start here
				 * --> return new Forward("index");
				 */
				return new String("index");
				/**
				 * <!-- Infosys - Weblogic to Jboss migration changes ends here
				 * -->
				 */

			} else {
				return unauthorizedRole();
			}

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}

	}

	// added for TRT major enhancement 3.6- F6
	/**
	 * @jpf:action
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward switchUser(){
	 */
	public String switchUser() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */
			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}
			LoggerHelper.logSystemDebug("TRTtest5 ");
			UserSession uSession = (UserSession) getRequest().getSession(true)
					.getAttribute(UserSession.ATTRIBUTE);
			User user = null;
			if (uSession != null) {
				user = uSession.getUser();
			}

			/*
			 * if ( uSession==null || uSession.getIsDelegatedUser() != null) {
			 * return unauthorized(); }
			 */

			LoggerHelper.logSystemDebug("TRTtest6 ");
			DelegateHandler DHandler = new DelegateHandler();
			List result = DHandler.getDelegatedFromUserList(uSession
					.getIsDelegatedUser());

			LoggerHelper.logSystemDebug("TRTtest30 ");
			ServiceFactory factory = Service.getServiceFactory();
			EmployeeHandler eHandler = factory.getEmployeeHandler();

			EmpSearch[] arr = eHandler.getDelegatedFromEmployees(result);
			LoggerHelper.logSystemDebug("TRTtest23 ");

			MainTemplateWpc page = new MainTemplateWpc(user, "simulateuser");
			// NewWc inst = new NewWc();
			// inst.setEmpSearchList(arr);
			// page.setMain(inst);

			SwitchUserWc switchuser = new SwitchUserWc(arr);
			switchuser.setUser(user);

			page.setMain(switchuser);
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward delegateAccess(){
	 */
	public String delegateAccess() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			System.out.println("TRTtest01 ");
			UserSession uSession = (UserSession) getRequest().getSession(true)
					.getAttribute(UserSession.ATTRIBUTE);
			User user = null;
			if (uSession != null) {
				user = uSession.getUser();
			}

			DelegateBean fromBean = null;
			DelegateBean toBean = null;

			String fempid = "";
			String toempid = "";
			boolean setFlag = false;
			boolean setFFlag = false;
			boolean setTFlag = false;
			boolean setRFlag = false;
			boolean setSFlag = false;
			boolean setDFlag = false;
			boolean setDupFlag = false;

			/*
			 * if (getRequest().getParameter("save") != null) { fempid =
			 * getRequest().getParameter("fempid"); toempid =
			 * getRequest().getParameter("toempid"); DelegateHandler Dhandler =
			 * new DelegateHandler(); Dhandler.Insert(fempid,toempid);
			 * getRequest().getSession().setAttribute("fromDelgBean",null);
			 * getRequest().getSession().setAttribute("toDelgBean",null);
			 * 
			 * }
			 */

			if (getRequest().getParameter("save") != null) {
				fempid = getRequest().getParameter("fempid");
				toempid = getRequest().getParameter("toempid");
				LoggerHelper.logSystemDebug("from user now" + fempid);

				if ("".equals(fempid) && toempid != "") {
					setFFlag = true;

				} else if ("".equals(toempid) && fempid != "") {
					setTFlag = true;
				}

				else if ("".equals(fempid) && "".equals(toempid)) {

					setFlag = true;
				} else if (fempid.equals(toempid)) {
					setDupFlag = true;
				}

				else {

					DelegateHandler Dhandler = new DelegateHandler();
					List tmp = DBUtil.executeSql(
							"select * from delegate where delfr = '" + fempid
									+ "'" + "and delto = '" + toempid + "'",
							AppConst.APP_DATASOURCE);
					if (tmp != null && tmp.size() > 0) {
						setDFlag = true;
					} else {
						Dhandler.Insert(fempid, toempid);
						getRequest().getSession().setAttribute("fromDelgBean",
								null);
						getRequest().getSession().setAttribute("toDelgBean",
								null);

					}

				}
			}

			if (getRequest().getParameter("clear") != null) {
				getRequest().getSession().setAttribute("fromDelgBean", null);
				getRequest().getSession().setAttribute("toDelgBean", null);
			}

			if (getRequest().getSession().getAttribute("fromDelgBean") != null) {
				fromBean = (DelegateBean) getRequest().getSession()
						.getAttribute("fromDelgBean");
			} else {
				fromBean = new DelegateBean();
			}
			if (getRequest().getSession().getAttribute("toDelgBean") != null) {
				toBean = (DelegateBean) getRequest().getSession().getAttribute(
						"toDelgBean");
			} else {
				toBean = new DelegateBean();
			}

			if (getRequest().getParameter("source") != null) {
				String source = getRequest().getParameter("source");

				if ("from".equals(source)) {

					/*
					 * if(getRequest().getParameter("OK") != null) {
					 * 
					 * String fName =getRequest().getParameter("fname");
					 * 
					 * if("".equals(fName)){ setSFlag = true; }
					 */
					// else {
					fromBean.setfName(getRequest().getParameter("fname"));
					fromBean.setlName(getRequest().getParameter("lname"));
					fromBean.setempId(getRequest().getParameter("empid"));

					getRequest().getSession().setAttribute("fromDelgBean",
							fromBean);
					// }
					// }

				} else if ("to".equals(source)) {

					/*
					 * if(getRequest().getParameter("OK") != null) {
					 * 
					 * String fName =getRequest().getParameter("fname");
					 * 
					 * if("".equals(fName)){ setSFlag = true; }
					 */
					// else {
					toBean.setfName(getRequest().getParameter("fname"));
					toBean.setlName(getRequest().getParameter("lname"));
					toBean.setempId(getRequest().getParameter("empid"));

					getRequest().getSession()
							.setAttribute("toDelgBean", toBean);
					// }
					// }

				}

			}

			String delfr;
			String delto;
			if (getRequest().getParameter("remove") != null) {
				delfr = getRequest().getParameter("delfrhid");
				delto = getRequest().getParameter("deltohid");

				if ("".equals(delfr)) {

					setRFlag = true;

				}

				LoggerHelper.logSystemDebug("del6" + delfr);
				DelegateHandler Dhandler = new DelegateHandler();
				Dhandler.Delete(delfr, delto);
			}

			DelegateHandler DHandler = new DelegateHandler();
			DelegatedEmp[] arr = DHandler.DelegatedFrList();
			// DelegatedEmp[] arrto = DHandler.DelegatedToList();

			DelegateAccessWc delegateaccess = new DelegateAccessWc(arr);

			for (int i = 0; i < arr.length; i++) {
				if (arr[i] == null) {
					delegateaccess.setListEmpty(true);
				}
			}

			if (setFlag) {
				delegateaccess
						.setErrorMsg("Please select users to delegate from and to");

			}
			if (setFFlag) {
				delegateaccess
						.setErrorMsg("Please select user to delegate from ");

			}
			if (setTFlag) {
				delegateaccess
						.setErrorMsg("Please select user for delegating to");
			}
			if (setRFlag) {
				delegateaccess.setErrorMsg("Please select an entry to remove");
			}

			if (setDupFlag) {
				delegateaccess
						.setErrorMsg("Delegated From and Delegated To should be different users");
			}
			if (setDFlag) {
				delegateaccess.setErrorMsg("Entry already exists.");
			}
			delegateaccess.setFromBean(fromBean);
			delegateaccess.setToBean(toBean);

			MainTemplateWpc page = new MainTemplateWpc(user, "delegateuser");
			// NewWc inst = new NewWc();
			// inst.setEmpSearchList(arr);
			// page.setMain(inst);

			page.setMain(delegateaccess);
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
	 * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward delegateSearch(){
	 */
	public String delegateSearch() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */
			callSecurePage();
			if (getResponse().isCommitted()) {
				return null;
			}

			String source = getRequest().getParameter("source");
			LoggerHelper.logSystemDebug("TRTtest02 ");

			EmplSearchForm form = new EmplSearchForm();
			if (form.getBuList().size() <= 0) {
				BUnitBean[] allBu = null;
				allBu = trDb.getAllBusinessUnits();
				LabelValueBean labelValueBean;
				form.setBu("All");
				FormUtil.loadObject(getRequest(), form);
				labelValueBean = new LabelValueBean("All", "All");
				form.setBuList(labelValueBean);
				for (int i = 0; i < allBu.length; i++) {
					labelValueBean = new LabelValueBean(
							allBu[i].getBunitDesc(), allBu[i].getBunitDesc());
					form.setBuList(labelValueBean);
				}
			}
			if (form.getSalOrgList().size() <= 0) {
				SalesOrgBean[] allSalesOrg = null;
				allSalesOrg = trDb.getAllSalesOrg();
				LabelValueBean labelValueBean;
				form.setSalesorg("All");
				FormUtil.loadObject(getRequest(), form);
				labelValueBean = new LabelValueBean("All", "All");
				form.setSalOrgList(labelValueBean);
				for (int i = 0; i < allSalesOrg.length; i++) {
					labelValueBean = new LabelValueBean(
							allSalesOrg[i].getSalesOrgDesc(),
							allSalesOrg[i].getSalesOrgDesc());
					form.setSalOrgList(labelValueBean);
				}
			}
			if (form.getRoleList().size() <= 0) {
				RoleBean[] allRoles = null;
				allRoles = trDb.getAllRoleDesc();
				LabelValueBean labelValueBean;
				form.setRole("All");
				FormUtil.loadObject(getRequest(), form);
				labelValueBean = new LabelValueBean("All", "All");
				form.setRoleList(labelValueBean);
				for (int i = 0; i < allRoles.length; i++) {
					labelValueBean = new LabelValueBean(
							allRoles[i].getRoleDesc(), allRoles[i].getRoleCd());
					form.setRoleList(labelValueBean);
				}
			}

			FormUtil.loadObject(getRequest(), form);
			UserSession uSession = UserSession.getUserSession(getRequest());
			User user = uSession.getUser();
			EmployeeSearch es = new EmployeeSearch();

			List ret = new ArrayList();

			if (getRequest().getParameter("searchSubmit") != null) {
				if (!Util.isEmpty(form.getLname())
						|| !Util.isEmpty(form.getFname())
						|| !Util.isEmpty(form.getEmail())
						|| !Util.isEmpty(form.getEmplid())
						|| !Util.isEmpty(form.getBu())
						|| !Util.isEmpty(form.getSalesorg())
						|| !Util.isEmpty(form.getRole())) {
					ret = es.getEmployeesForSimulationSearch(form, uSession);
				}
			}
			MainTemplateWpc page = new MainTemplateWpc(user, "search");

			DelegateSearchResultListWc resultWc = new DelegateSearchResultListWc(
					ret);

			resultWc.setSource(source);

			DelegateSearchFormWc searchFormWc = new DelegateSearchFormWc(form);

			searchFormWc.setSource(source);

			page.setMain(new DelegateSearchWc(searchFormWc, resultWc));
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

	// ends here

	// added for TRT major enhancement 3.6- F1
	/**
	 * @jpf:action
	 * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/**
	 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	 * protected Forward activityDrilldownConfig(){
	 */
	public String activityDrilldownConfig() {
		try {
			/** Infosys - Weblogic to Jboss migration changes ends here */

			LoggerHelper.logSystemDebug("inside activity0 ");

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qString = new AppQueryStrings();
			FormUtil.loadObject(getRequest(), qString);
			LoggerHelper.logSystemDebug("inside activity ");
			UserSession uSession = (UserSession) getRequest().getSession(true)
					.getAttribute(UserSession.ATTRIBUTE);

			/*-----process the role change */

			if ("newGroup".equals(qString.getType())) {
				String group = qString.getInputADDGroup();
				LoggerHelper.logSystemDebug("newtoday" + group);
				try {

					trDb.insertGroups(1, group);
				}

				catch (Exception e) {

					trDb.setActivityDrillDownAccessGroup(group);
				}

			}

			/* UI */

			String currGroup = trDb.getActivityDrillDownAccessGroup();
			ActivityDrillDownAdminWc wc = new ActivityDrillDownAdminWc();

			wc.setCurrGroup(currGroup);

			wc.setGroups(MenuList.getAccessList());

			MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(),
					"Drilldown Activity Report Config");

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
