package com.pfizer.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.db.GapAnalysisEntry;
import com.pfizer.db.GapAnalysisUIEntry;
import com.pfizer.db.SalesOrgCodeDesciption;
import com.pfizer.dao.TransactionDB;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.tgix.html.FormUtil;
import com.tgix.wc.WebComponent;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pfizer.utils.Global;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.components.GapAnalysisMainWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;

import java.util.Date;
import java.sql.*;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 * @jpf:controller
 * @jpf:view-properties view-properties:: <!-- This data is auto-generated.
 *                      Hand-editing this section is not recommended. -->
 *                      <view-properties> <pageflow-object
 *                      id="pageflow:/gapAnalysis/GapAnalysisController.jpf"/>
 *                      <pageflow-object id="action:begin.do"> <property
 *                      value="80" name="x"/> <property value="100" name="y"/>
 *                      </pageflow-object> <pageflow-object
 *                      id="action:displayReport.do"> <property value="60"
 *                      name="x"/> <property value="40" name="y"/>
 *                      </pageflow-object> <pageflow-object
 *                      id="page:/WEB-INF/jsp/templates/mainTemplate.jsp">
 *                      <property value="60" name="x"/> <property value="40"
 *                      name="y"/> </pageflow-object> <pageflow-object
 *                      id="page:/WEB-INF/jsp/templates/blankTemplate.jsp">
 *                      <property value="160" name="x"/> <property value="240"
 *                      name="y"/> </pageflow-object> <pageflow-object id=
 *                      "forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:begin.do@"
 *                      > <property value="44,-59,-59,60" name="elbowsX"/>
 *                      <property value="92,92,-4,-4" name="elbowsY"/> <property
 *                      value="West_1" name="fromPort"/> <property
 *                      value="North_1" name="toPort"/> <property
 *                      value="success" name="label"/> </pageflow-object>
 *                      <pageflow-object id=
 *                      "forward:path#successXls#/WEB-INF/jsp/templates/blankTemplate.jsp#@action:begin.do@"
 *                      > <property value="44,44,149,149" name="elbowsX"/>
 *                      <property value="103,166,166,196" name="elbowsY"/>
 *                      <property value="West_2" name="fromPort"/> <property
 *                      value="North_0" name="toPort"/> <property
 *                      value="successXls" name="label"/> </pageflow-object>
 *                      <pageflow-object id=
 *                      "forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:displayReport.do@"
 *                      > <property value="24,-59,-59,49" name="elbowsX"/>
 *                      <property value="32,32,-4,-4" name="elbowsY"/> <property
 *                      value="West_1" name="fromPort"/> <property
 *                      value="North_0" name="toPort"/> <property
 *                      value="success" name="label"/> </pageflow-object>
 *                      <pageflow-object id=
 *                      "forward:path#successXls#/WEB-INF/jsp/templates/blankTemplate.jsp#@action:displayReport.do@"
 *                      > <property value="96,160,160,160" name="elbowsX"/>
 *                      <property value="43,43,119,196" name="elbowsY"/>
 *                      <property value="East_2" name="fromPort"/> <property
 *                      value="North_1" name="toPort"/> <property
 *                      value="successXls" name="label"/> </pageflow-object>
 *                      <pageflow-object id="control:db.TrDB#trDB"> <property
 *                      value="26" name="x"/> <property value="34" name="y"/>
 *                      </pageflow-object> </view-properties> ::
 */

/*
 * Infosys - Weblogic to Jboss Migrations changes start here: Struts supporting
 * changes
 */
public class GapAnalysisControllerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	TransactionDB trDb = new TransactionDB();
	protected static final Log log = LogFactory
			.getLog(GapAnalysisControllerAction.class);

	private static final String TSR_ADMIN = "TSR Admin";

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


	/* Infosys - Weblogic to Jboss Migrations changes end here */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	public String begin() 
	// protected Forward begin()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		return displayReport();
	}

	/**
	 * This method represents the point of entry into the pageflow
	 * 
	 * @throws ParseException
	 * @jpf:action
	 * @jpf:forward name="success"
	 *              path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 * @jpf:forward name="successXls"
	 *              path="/WEB-INF/jsp/templates/blankTemplate.jsp"
	 */
	/* Infosys - Weblogic to Jboss Migrations changes start here */
	public String displayReport() 
	// protected Forward displayReport()
	/* Infosys - Weblogic to Jboss Migrations changes end here */
	{
		/*
		 * commented by Amit if(getResponse().isCommitted()) { return null; }
		 * AppQueryStrings qStrings=new AppQueryStrings();
		 * FormUtil.loadObject(getRequest(),qStrings); UserSession uSession;
		 * User user; uSession=UserSession.getUserSession(getRequest());
		 * user=uSession.getUser();
		 * 
		 * boolean admin=uSession.getUser().isAdmin(); boolean flag=true;
		 * 
		 * GapAnalysisMainWc wc=new GapAnalysisMainWc();
		 * 
		 * GapAnalysisEntry diff[] = null; GapAnalysisEntry crEntries[] = null;
		 * 
		 * Map result=new HashMap(); String[] productDesc=null; String[]
		 * deploymentPackageId=null;
		 * deploymentPackageId=trDB.getAllDeploymentId(); String
		 * prevDeploymentID = "0";
		 * if(getRequest().getParameter("generate")!=null) { //Get the results
		 * //input parameters are: duration, deploymentid. String duration =
		 * getRequest().getParameter("duration");
		 * 
		 * String deplID = getRequest().getParameter("deploymentid");
		 * 
		 * if(deplID.equals("select")){
		 * wc.setErrorMsg("Please select some Deployment Id"); flag=false; }
		 * else { flag=true; if(duration==null||duration.length()==0){
		 * wc.setErrorMsg("Please enter Duration"); flag=false; } int i=0;
		 * for(i=0;i<duration.length();i++){ if(duration.charAt(0)=='0'){
		 * wc.setErrorMsg
		 * ("Please enter some valid number of month(s) for Duration");
		 * flag=false; break; }
		 * if(duration.charAt(i)=='0'||duration.charAt(i)=='1'
		 * ||duration.charAt(i)=='2'||duration.charAt(i)=='3'||
		 * duration.charAt(i
		 * )=='4'||duration.charAt(i)=='5'||duration.charAt(i)=='6'||
		 * duration.charAt
		 * (i)=='7'||duration.charAt(i)=='8'||duration.charAt(i)=='9'){
		 * flag=true; } else{
		 * wc.setErrorMsg("Please enter some valid number of month(s) for Duration"
		 * ); flag=false; break; } } } if
		 * (!deplID.trim().equalsIgnoreCase(deploymentPackageId[0].trim())) {
		 * prevDeploymentID = deplID; }
		 * System.out.println("prevDeploymentID is "+prevDeploymentID);
		 * System.out
		 * .println("deploymentPackageId[0] is "+deploymentPackageId[0]);
		 * System.out.println("flag is "+flag);
		 * 
		 * if(flag){ wc.setErrorMsg(null); System.out.println("before DB="+new
		 * Date()); System.out.println("admin is "+admin); if(admin){
		 * System.out.println(" calling getGapReportData() " );
		 * trDB.getGapReportData();
		 * System.out.println(" returned from getGapReportData() " ); diff =
		 * trDB.getGapAnalysisDiffEntriesForAdmin(deploymentPackageId[0],
		 * prevDeploymentID,duration); crEntries =
		 * trDB.getGapAnalysisCompOrRegForAdmin
		 * (deploymentPackageId[0],prevDeploymentID,duration); } else{ diff =
		 * trDB
		 * .getGapAnalysisDiffEntries(deploymentPackageId[0],prevDeploymentID
		 * ,duration,user.getEmplid()); crEntries =
		 * trDB.getGapAnalysisCompOrReg(
		 * deploymentPackageId[0],prevDeploymentID,duration,user.getEmplid()); }
		 * System.out.println("after DB="+new Date()); result
		 * =mergeGapAnalysisDBResults(diff,crEntries);
		 * System.out.println(result.size()); } }
		 * 
		 * productDesc=trDB.getAllProductNames(); wc.setResult(result);
		 * wc.setdeploymentPackageId(deploymentPackageId);
		 * wc.setProductDesc(productDesc);
		 * 
		 * if ( "true".equals( getRequest().getParameter("downloadExcel") ) ) {
		 * 
		 * wc.setLayout(GapAnalysisMainWc.LAYOUT_XLS); BlankTemplateWpc page =
		 * new BlankTemplateWpc(wc); getRequest().setAttribute(
		 * BlankTemplateWpc.ATTRIBUTE_NAME, page );
		 * getResponse().addHeader("content-disposition"
		 * ,"attachment;filename=GapAnalysisReports.xls");
		 * getResponse().setContentType("application/vnd.ms-excel");
		 * getResponse().setHeader("Cache-Control","max-age=0");
		 * getResponse().setHeader("Pragma","public");
		 * 
		 * return new Forward("successXls"); }
		 * 
		 * wc.setDeploymentId(getRequest().getParameter("deploymentid"));
		 * wc.setDuration(getRequest().getParameter("duration"));
		 * 
		 * MainTemplateWpc page=new MainTemplateWpc(user,"gapAnalysis");
		 * 
		 * page.setMain(wc);
		 * getRequest().setAttribute(WebComponent.ATTRIBUTE_NAME,page);
		 * 
		 * return new Forward("success");
		 */// Commented by Amit END
		try {
			System.out
					.println("Inside displayReport of GapAnalysisControllerAction");

			if (getResponse().isCommitted()) {
				return null;
			}

			AppQueryStrings qStrings = new AppQueryStrings();
			// First thing to do is get UserSession object.
			FormUtil.loadObject(getRequest(), qStrings);
			UserSession uSession;
			User user;
			uSession = UserSession.getUserSession(getRequest());
			user = uSession.getUser();

			boolean admin = uSession.getUser().isAdmin();
			boolean flag = true;

			GapAnalysisMainWc wc = new GapAnalysisMainWc();

			GapAnalysisEntry diff[] = null;
			GapAnalysisEntry crEntries[] = null;

			Map result = new HashMap();
			String[] productDesc = null;
			String[] deploymentPackageId = null;
			// deploymentPackageId=trDb.getAllDeploymentId();

			// String roleCds[]=null;
			String salesOrgCds[] = null;
			deploymentPackageId = trDb.getAllDeploymentIds(); // fetch
																// deployment
																// ids from sand
																// box

			String prevDeploymentID = "0";
			String[] selectedProducts = null;
			String[] selectedRoles = null;

			String[] selectedOrgCds = null;

			if (getRequest().getParameter("generate") != null) {
				// Get the results
				// input parameters are: duration, deployment id.
				String duration = getRequest().getParameter("duration");

				String deplID = getRequest().getParameter("deploymentid");

				System.out.println("duration " + duration);
				System.out.println("deplID " + deplID);

				if (deplID.equals("select")) {
					wc.setErrorMsg("Please select some Deployment Id");
					flag = false;
				} else {
					flag = true;
					if (duration == null || duration.length() == 0) {
						wc.setErrorMsg("Please enter Duration");
						flag = false;
					}
					int i = 0;
					for (i = 0; i < duration.length(); i++) {
						if (duration.charAt(0) == '0' && duration.length() > 1) {
							wc.setErrorMsg("Please enter some valid number of month(s) for Duration");
							flag = false;
							break;
						}
						if (duration.charAt(i) == '0'
								|| duration.charAt(i) == '1'
								|| duration.charAt(i) == '2'
								|| duration.charAt(i) == '3'
								|| duration.charAt(i) == '4'
								|| duration.charAt(i) == '5'
								|| duration.charAt(i) == '6'
								|| duration.charAt(i) == '7'
								|| duration.charAt(i) == '8'
								|| duration.charAt(i) == '9') {
							flag = true;
						} else {
							wc.setErrorMsg("Please enter some valid number of month(s) for Duration");
							flag = false;
							break;
						}
					}
				}
				// System.out.println("flag is "+flag);

				// logic to capture selected prods starts
				selectedProducts = getRequest().getParameterValues(
						"selProducts");

				String prodNames2 = "'";
				System.out.println("selectedProducts are " + selectedProducts);
				if (selectedProducts != null && selectedProducts.length > 0) {

					// System.out.println("selectedProducts  "+selectedProducts.toString());

					prodNames2 = "'";
					for (int i = 0; i < selectedProducts.length; i++) {
						// System.out.println("selectedProducts ["+i+"] "+selectedProducts[i]);
						if (i != selectedProducts.length - 1) {

							prodNames2 = prodNames2.concat(selectedProducts[i])
									.concat("','");
						} else {

							prodNames2 = prodNames2.concat(selectedProducts[i])
									.concat("'");
						}
					}

					// System.out.println("selectedProducts prodNames2  "+prodNames2);
				} else {
					wc.setErrorMsg("Please select at least single product for Gap Report generation!");
					flag = false;
				}
				// logic to capture selected prods ends here

				// logic to capture selected sales org code starts
				selectedOrgCds = getRequest().getParameterValues("selOrgCds");

				String salesOrgCodes = "'";
				// System.out.println("selected Org codess are "+selectedOrgCds);
				if (selectedOrgCds != null && selectedOrgCds.length > 0) {

					// System.out.println("selectedOrgCds  "+selectedOrgCds.toString());

					salesOrgCodes = "'";
					for (int i = 0; i < selectedOrgCds.length; i++) {
						// System.out.println("selectedOrgCds ["+i+"] "+selectedOrgCds[i]);
						if (i != selectedOrgCds.length - 1) {
							salesOrgCodes = salesOrgCodes.concat(
									selectedOrgCds[i]).concat("','");
						} else {
							salesOrgCodes = salesOrgCodes.concat(
									selectedOrgCds[i]).concat("'");
						}
					}

					// System.out.println("selected org codes-salesOrgCodes  "+salesOrgCodes);
				} else {
					wc.setErrorMsg("Please select at least single Sales Organization Code for Gap Report generation!");
					flag = false;
				}
				// logic to capture selected sales org code ends here

				if (!deplID.trim().equalsIgnoreCase(
						deploymentPackageId[0].trim())) {
					prevDeploymentID = deplID;
				}
				// System.out.println("prevDeploymentID is "+prevDeploymentID);
				// System.out.println("deploymentPackageId[0] is "+deploymentPackageId[0]);
				// System.out.println("flag is "+flag);

				if (flag) {
					wc.setErrorMsg(null);
					// System.out.println("before DB="+new Date());
					// System.out.println("admin is "+admin);
					if (admin) {
						System.out.println(" calling generateGapReportData() ");
						// trDb.generateGapReportData(duration,deplID,prodNames2);
						trDb.generateGapReportData(duration, deplID,
								prodNames2, salesOrgCodes);

						System.out
								.println(" returned from generateGapReportData() ");

						// diff =
						// trDb.getGapAnalysisDiffEntriesForAdmin(deploymentPackageId[0],prevDeploymentID,duration);
						// crEntries =
						// trDb.getGapAnalysisCompOrRegForAdmin(deploymentPackageId[0],prevDeploymentID,duration);

						diff = trDb.getGapReportData();

					}
					/*
					 * No need of this else gap report will be fetch by admin
					 * only else{ diff =
					 * trDb.getGapAnalysisDiffEntries(deploymentPackageId
					 * [0],prevDeploymentID,duration,user.getEmplid());
					 * crEntries =
					 * trDb.getGapAnalysisCompOrReg(deploymentPackageId
					 * [0],prevDeploymentID,duration,user.getEmplid()); }
					 */
					System.out.println("after DB=" + new Date());
					// result =mergeGapAnalysisDBResults(diff,crEntries);
					result = populateGapAnalysisResultMap(diff);

					System.out.println("result.size() : " + result.size());
				}
			}

			// productDesc=trDb.getAllProductNames();
			productDesc = trDb.getGapReportProductNames(); // get product name
															// from
															// gap_fflra_codes
			// roleCds=trDb.getRoleCodes(); //get all roles

			SalesOrgCodeDesciption[] objSalesOrgCds = null;
			Map salesOrgCdDescMap = new HashMap();
			// salesOrgCds=trDb.getSalesOrgCodes();
			objSalesOrgCds = trDb.getSalesOrgCodes();
			if (objSalesOrgCds != null && objSalesOrgCds.length > 0) {
				// System.out.println(" objSalesOrgCds is not null & its size= "+objSalesOrgCds.length);
				for (int i = 0; i < objSalesOrgCds.length; i++) {
					salesOrgCdDescMap.put(objSalesOrgCds[i].getSalesOrgCd(),
							objSalesOrgCds[i].getSalesOrgDesc());
				}
			}
			Map objTreeMap = new TreeMap(salesOrgCdDescMap);

			wc.setResult(result);
			wc.setdeploymentPackageId(deploymentPackageId);
			wc.setProductDesc(productDesc);
			wc.setSelectedProdDesc(selectedProducts);

			wc.setSalesOrgCds(salesOrgCds);
			wc.setSalesOrgCdDesc(objTreeMap);
			wc.setSelectedSalesOrgCds(selectedOrgCds);
			// wc.setRoleCds(roleCds);
			// wc.setSelectedRoleCds(selectedRoles);

			if ("true".equals(getRequest().getParameter("downloadExcel"))) {

				wc.setLayout(GapAnalysisMainWc.LAYOUT_XLS);
				BlankTemplateWpc page = new BlankTemplateWpc(wc);
				getRequest()
						.setAttribute(BlankTemplateWpc.ATTRIBUTE_NAME, page);
				getResponse().addHeader("content-disposition",
						"attachment;filename=GapAnalysisReports.xls");
				getResponse().setContentType("application/vnd.ms-excel");
				getResponse().setHeader("Cache-Control", "max-age=0");
				getResponse().setHeader("Pragma", "public");

				/* Infosys - Weblogic to Jboss Migrations changes start here */
				// return new Forward("successXls");
				return ("successXls");
				/* Infosys - Weblogic to Jboss Migrations changes end here */
			}

			wc.setDeploymentId(getRequest().getParameter("deploymentid"));
			wc.setDuration(getRequest().getParameter("duration"));

			MainTemplateWpc page = new MainTemplateWpc(user, "gapAnalysis");

			page.setMain(wc);
			getRequest().setAttribute(WebComponent.ATTRIBUTE_NAME, page);

			/* Infosys - Weblogic to Jboss Migrations changes start here */
			// return new Forward("success");
			return ("success");

		} catch (Exception e) {
			Global.getError(getRequest(), e);
			return new String("failure");
		}
		/* Infosys - Weblogic to Jboss Migrations changes end here */
	}

	/**
	 * Merging the two queries into a Map
	 */

	/*
	 * private Map mergeGapAnalysisDBResults(GapAnalysisEntry[] diff,
	 * GapAnalysisEntry[] crEntries) { Map result = new HashMap(); //map of
	 * GapAnalysisUIEntry Map emplProd=null; GapAnalysisEntry entry=null;
	 * GapAnalysisUIEntry uiEntry = null; //Commented out by Amit to display the
	 * employees with the gap for registration or completion--start
	 * 
	 * int i=0; for(i=0;i<diff.length;i++) {
	 * if(result.get(diff[i].getEmpID())==null) { emplProd=new HashMap();
	 * uiEntry= new GapAnalysisUIEntry(); uiEntry.setEntry(diff[i]);
	 * uiEntry.setEmplProd(emplProd); result.put(diff[i].getEmpID(),uiEntry); }
	 * else {
	 * emplProd=(HashMap)((GapAnalysisUIEntry)result.get(diff[i].getEmpID(
	 * ))).getEmplProd(); } emplProd.put(diff[i].getProductName(),"G"); }
	 * System.out.println("diff length is "+diff.length);
	 * System.out.println("crEntries length is "+crEntries.length); //Commented
	 * out to display the employees with the gap only
	 * 
	 * //Commented out by Amit to display the employees with the gap for
	 * registration or completion--end
	 * 
	 * for(i=0;i<crEntries.length;i++) {
	 * if(result.get(crEntries[i].getEmpID())==null) { emplProd = new HashMap();
	 * uiEntry = new GapAnalysisUIEntry(); uiEntry.setEntry(crEntries[i]);
	 * uiEntry.setEmplProd(emplProd);
	 * result.put(crEntries[i].getEmpID(),uiEntry); } else { emplProd =
	 * (HashMap)
	 * ((GapAnalysisUIEntry)result.get(crEntries[i].getEmpID())).getEmplProd();
	 * } emplProd.put(crEntries[i].getProductName(),crEntries[i].getStatus()); }
	 * 
	 * 
	 * return result; }
	 */
	private Map populateGapAnalysisResultMap(GapAnalysisEntry[] diff) {
		Map result = new HashMap(); // map of GapAnalysisUIEntry
		Map emplProd = null;
		GapAnalysisEntry entry = null;
		GapAnalysisUIEntry uiEntry = null;

		int i = 0;

		/*
		 * for(i=0;i<diff.length;i++) { if(result.get(diff[i].getEmpID())==null)
		 * { emplProd=new HashMap(); uiEntry= new GapAnalysisUIEntry();
		 * uiEntry.setEntry(diff[i]); uiEntry.setEmplProd(emplProd);
		 * result.put(diff[i].getEmpID(),uiEntry); } else {
		 * emplProd=(HashMap)((GapAnalysisUIEntry
		 * )result.get(diff[i].getEmpID())).getEmplProd(); }
		 * 
		 * emplProd.put(diff[i].getProductName(),diff[i].getStatus()); }
		 */

		for (i = 0; i < diff.length; i++) {
			if (result.get(diff[i].getEmpID()) == null) {
				if (!diff[i].getStatus().equalsIgnoreCase("C")) {
					emplProd = new HashMap();
					uiEntry = new GapAnalysisUIEntry();
					uiEntry.setEntry(diff[i]);
					uiEntry.setEmplProd(emplProd);
					result.put(diff[i].getEmpID(), uiEntry);
				}
			} else {
				emplProd = (HashMap) ((GapAnalysisUIEntry) result.get(diff[i]
						.getEmpID())).getEmplProd();
			}

			if (!diff[i].getStatus().equalsIgnoreCase("C")) {
				emplProd.put(diff[i].getProductName(), diff[i].getStatus());
			}

		}
		System.out.println("diff length is " + diff.length);

		return result;
	}

	/* Infosys - Weblogic to Jboss Migrations changes start here */



	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}
	/* Infosys - Weblogic to Jboss Migrations changes end here */
}
