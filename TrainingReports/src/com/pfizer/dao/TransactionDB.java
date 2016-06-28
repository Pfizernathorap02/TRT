package com.pfizer.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jfree.util.Log;

import com.pfizer.PWRA.EmployeeDetailFacade.EmployeeInfo;
import com.pfizer.PWRA.EmployeeDetailFacade.PDFHomeStudyStatus;
import com.pfizer.PWRA.EmployeeDetailFacade.PDFProduct;
import com.pfizer.PWRA.EmployeeDetailFacade.PLCExamStatus;
import com.pfizer.PWRA.EmployeeDetailFacade.PLCStatus;
import com.pfizer.PWRA.EmployeeDetailFacade.RBUPostProduct;
import com.pfizer.PWRA.EmployeeDetailFacade.RBUProduct;
import com.pfizer.PWRA.EmployeeDetailFacade.TrainingMaterialHistory;
import com.pfizer.PWRA.EmployeeDetailFacade.TrainingSchedule;
import com.pfizer.PWRA.EmployeeDetailFacade.TrainingScheduleList;
import com.pfizer.db.Attendance;
import com.pfizer.db.BUnitBean;
import com.pfizer.db.Employee;
import com.pfizer.db.GAProdCourse;
import com.pfizer.db.GAProduct;
import com.pfizer.db.GapAnalysisEntry;
import com.pfizer.db.GroupBean;
import com.pfizer.db.MenuList;
import com.pfizer.db.P2LRegistration;
import com.pfizer.db.PassFail;
import com.pfizer.db.PedagogueExam;
import com.pfizer.db.Product;
import com.pfizer.db.RBUClassRosterBean;
import com.pfizer.db.ReportTypeBean;
import com.pfizer.db.RoleBean;
import com.pfizer.db.SCEList;
import com.pfizer.db.SalesOrgBean;
import com.pfizer.db.SalesOrgCodeDesciption;
import com.pfizer.db.Sce;
import com.pfizer.db.TeamBean;
import com.pfizer.db.Territory;
import com.pfizer.db.UserAccess;
import com.pfizer.db.UserGroups;
import com.pfizer.utils.HibernateUtils;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.wc.POA.POAChartBean;
import com.tgix.Utils.Timer;
import com.tgix.printing.EmployeeGridOptFieldsBean;
import com.tgix.printing.TrainingWeeks;
import com.tgix.rbu.FutureAllignmentBuDataBean;
import com.tgix.rbu.FutureAllignmentRBUDataBean;
import com.tgix.rbu.ProductDataBean;

public class TransactionDB implements Serializable {

	public EmployeeInfo getEmployeeInfo(String emplid) {
		EmployeeInfo emp = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT E.EMPLID,E.PROMOTION_DATE ,       E.EFFECTIVE_HIRE_DATE ,E.SEX , E.EMAIL_ADDRESS , E.REPORTS_TO_EMPLID ,     		DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') , E.AREA_CD , E.AREA_DESC ,E.REGION_CD ,       E.REGION_DESC ,  E.DISTRICT_ID , E.DISTRICT_DESC ,  E.TERRITORY_ID ,E.TERRITORY_ROLE_CD ,E.TEAM_CD ,E.CLUSTER_CD ,  E.LAST_NAME , E.MIDDLE_NAME ,E.PREFERRED_NAME "
							+ "FROM V_PDF_SPF_FIELD_EMPLOYEE E WHERE E.EMPLID= :empid");
			query.setParameter("empid", emplid);
			List<Object> lst = query.list();
			List<EmployeeInfo> eInfo = new ArrayList<EmployeeInfo>();
			Iterator it = lst.iterator();
			while (it.hasNext()) {
				emp = new EmployeeInfo();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					emp.setEmplID(field[0].toString());
				if (field[1] != null)
					emp.setPromotionDate((Date) field[1]);
				if (field[2] != null)
					emp.setHireDate((Date) field[2]);
				if (field[3] != null)
					emp.setGender(field[3].toString());
				if (field[4] != null)
					emp.setEmail(field[4].toString());
				if (field[5] != null)
					emp.setReportToEmplID(field[5].toString());
				if (field[6] != null)
					emp.setStatus(field[6].toString());
				if (field[7] != null)
					emp.setAreaCD(field[7].toString());
				if (field[8] != null)
					emp.setAreaDesc(field[8].toString());
				if (field[9] != null)
					emp.setRegionCD(field[9].toString());
				if (field[10] != null)
					emp.setRegionDesc(field[10].toString());
				if (field[11] != null)
					emp.setDistrictID(field[11].toString());
				if (field[12] != null)
					emp.setDistrictDesc(field[12].toString());
				if (field[13] != null)
					emp.setTerritoryID(field[13].toString());
				if (field[14] != null)
					emp.setTerritoryRole(field[14].toString());
				if (field[15] != null)
					emp.setTeamCD(field[15].toString());
				if (field[16] != null)
					emp.setClusterCD(field[16].toString());
				if (field[17] != null)
					emp.setLastName(field[17].toString());
				if (field[18] != null)
					emp.setMiddleName(field[18].toString());
				if (field[19] != null)
					emp.setPreferredName(field[19].toString());
			}
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getEmployeeInfo Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return emp;
	}

	public GroupBean[] getAllUserGroups() {

		GroupBean[] groupBeans = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			// Transaction tx= session.beginTransaction();

			Query query = session
					.createSQLQuery("SELECT DISTINCT GROUP_NAME groupName FROM USER_GROUP WHERE GROUP_NAME IS NOT NULL "
							+ " ORDER BY GROUP_NAME ASC");

			List gbList = query.list();
			System.out.println("After Execution Query ");
			Iterator it = gbList.iterator();
			List<GroupBean> templist = new ArrayList<GroupBean>();
			String gb = "";
			templist.clear();
			while (it.hasNext()) {
				GroupBean groupBean = new GroupBean();
				Object field = it.next();
				if (field != null)
					gb = field.toString();
				groupBean.setGroupName(gb);
				templist.add(groupBean);

			}

			groupBeans = templist.toArray(new GroupBean[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getAllUserGroups Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return groupBeans;
	}

	public MenuList[] renderMainMenu() {

		MenuList[] menuLists = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			// Transaction tx= session.beginTransaction();
			Query query = session
					.createSQLQuery("SELECT SORT_ORDER SORTORDER,LEVEL,TRAINING_REPORT_LABEL LABEL,TRAINING_REPORT_URL URL,ALLOW_GROUP ALLOW,TRAINING_REPORT_ID ID, TRACK_ID trackId,minimize Minimize "
							+ " FROM TRAINING_REPORT START WITH PARENT IS NULL AND ACTIVE=1 and delete_flag ='N' CONNECT BY PRIOR TRAINING_REPORT_ID = PARENT AND active = 1 AND delete_flag = 'N' "
							+ " ORDER SIBLINGS BY SORT_ORDER ");
			List mlList = query.list();
			List<MenuList> templist = new ArrayList<MenuList>();
			templist.clear();
			Iterator it = mlList.iterator();
			while (it.hasNext()) {
				MenuList menuList = new MenuList();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					menuList.setSortorder(field[0].toString());
				if (field[1] != null)
					menuList.setLevel(field[1].toString());
				if (field[2] != null)
					menuList.setLabel(field[2].toString());
				if (field[3] != null)
					menuList.setUrl(field[3].toString());
				if (field[4] != null)
					menuList.setAllow(field[4].toString());
				if (field[5] != null)
					menuList.setId(field[5].toString());
				if (field[6] != null)
					menuList.setTrackId(field[6].toString());
				if (field[7] != null)
					menuList.setMinimize(field[7].toString());
				templist.add(menuList);
			}

			menuLists = templist.toArray(new MenuList[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("renderMainMenu Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return menuLists;
	}

	public UserGroups getUserGroupDetails(String groupName) {

		UserGroups userGroups = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select BUSINESS_UNIT selectedBU, SALES_ORGANIZATION selectedSalesorg, ROLE selectedRole, FEEDBACK_USERS selectedFBU, HQ_USERS selectedHQU from USER_GROUP where GROUP_NAME= :groupName");
			query.setParameter("groupName", groupName);
			List ugList = query.list();
			Iterator it = ugList.iterator();
			while (it.hasNext()) {
				userGroups = new UserGroups();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					userGroups.setSelectedBU(field[0].toString());
				if (field[1] != null)
					userGroups.setSelectedSalesorg(field[1].toString());
				if (field[2] != null)
					userGroups.setSelectedRole(field[2].toString());
				if (field[3] != null)
					userGroups.setSelectedFBU(field[3].toString());
				if (field[4] != null)
					userGroups.setSelectedHQU(field[4].toString());
			}

		} catch (HibernateException e) {
			e.printStackTrace();
			System.out.println("getUserGroupDetails Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return userGroups;
	}

	public BUnitBean[] getAllBusinessUnits() {
		BUnitBean[] bUnitBeans = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT(BU) bunitDesc FROM mv_bu_salesgroup_rbu where BU is not null"
							+ " ORDER BY bunitDesc ASC");
			List BUList = query.list();
			List<BUnitBean> templist = new ArrayList<BUnitBean>();
			templist.clear();
			Iterator it = BUList.iterator();
			while (it.hasNext()) {
				BUnitBean BuList = new BUnitBean();
				String field = (String) it.next();
				BuList.setBunitDesc(field);
				templist.add(BuList);
			}
			bUnitBeans = templist.toArray(new BUnitBean[templist.size()]);
		} catch (HibernateException e) {
			e.printStackTrace();
			System.out.println("getAllBusinessUnits Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return bUnitBeans;
	}

	public SalesOrgBean[] getAllSalesOrg() {

		SalesOrgBean[] salesOrgBeans = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT(SALESGROUP) salesOrgDesc FROM mv_bu_salesgroup_rbu where SALESGROUP is not null "
							+ " ORDER BY salesOrgDesc ASC");
			List salOrg = query.list();
			List<SalesOrgBean> templist = new ArrayList<SalesOrgBean>();
			templist.clear();
			Iterator it = salOrg.iterator();
			while (it.hasNext()) {
				SalesOrgBean saOrgList = new SalesOrgBean();
				Object field = it.next();
				if (field != null)
					saOrgList.setSalesOrgDesc(field.toString());
				templist.add(saOrgList);
			}
			salesOrgBeans = templist.toArray(new SalesOrgBean[templist.size()]);
		} catch (HibernateException e) {
			e.printStackTrace();
			System.out.println("getAllSalesOrg Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return salesOrgBeans;
	}

	public RoleBean[] getAllRoleDesc() {
		RoleBean[] roleBeans = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT distinct role_cd roleCd, role_desc roleDesc from mv_field_employee_rbu where role_cd is not NULL "
							+ " ORDER by role_desc asc");
			List roleLst = query.list();
			List<RoleBean> templist = new ArrayList<RoleBean>();
			templist.clear();
			Iterator it = roleLst.iterator();
			while (it.hasNext()) {
				RoleBean roleList = new RoleBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					roleList.setRoleCd(field[0].toString());
				if (field[1] != null)
					roleList.setRoleDesc(field[1].toString());
				templist.add(roleList);
			}
			roleBeans = templist.toArray(new RoleBean[templist.size()]);
		} catch (HibernateException e) {
			e.printStackTrace();
			System.out.println("getAllRoleDesc Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return roleBeans;
	}

	public void insertGroups(int serial, String groups) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createSQLQuery("INSERT INTO ACTIVITY_DRILLDOWN_CONFIG VALUES({:serial},{:groups})");
			query.setParameter("serial", serial);
			query.setParameter("groups", groups);
			int res = query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			System.out.println("insertGroups Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void setActivityDrillDownAccessGroup(String group) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createSQLQuery("UPDATE ACTIVITY_DRILLDOWN_CONFIG SET GROUP_NAME = {:group}");
			query.setParameter("group", group);
			int res = query.executeUpdate();
			System.out.println("Number of Rows Inserted :" + res);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			System.out
					.println("setActivityDrillDownAccessGroup Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public String getActivityDrillDownAccessGroup() {
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select GROUP_NAME from ACTIVITY_DRILLDOWN_CONFIG");
			String res = (String) query.uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
			System.out
					.println("setActivityDrillDownAccessGroup Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return null;
	}

	public SalesOrgBean[] getAllSALESORG() {
		SalesOrgBean[] salesOrgBeans = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT GROUP_CD salesOrgCd, SALES_GROUP salesOrgDesc FROM MV_FIELD_EMPLOYEE_RBU where SALES_GROUP is not NULL ORDER BY salesOrgDesc ASC");
			List salOrg = query.list();
			List<SalesOrgBean> templist = new ArrayList<SalesOrgBean>();
			templist.clear();
			Iterator it = salOrg.iterator();
			while (it.hasNext()) {
				SalesOrgBean saOrgList = new SalesOrgBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					saOrgList.setSalesOrgCd(field[0].toString());
				if (field[1] != null)
					saOrgList.setSalesOrgDesc(field[1].toString());
				templist.add(saOrgList);
			}
			salesOrgBeans = templist.toArray(new SalesOrgBean[templist.size()]);
		} catch (HibernateException e) {
			e.printStackTrace();
			System.out.println("getAllSALESORG Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return salesOrgBeans;
	}

	public SalesOrgBean[] getSALESORGBYUSER(String emplid) {
		SalesOrgBean[] salesOrgBeans = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select distinct salesOrgCd, salesOrgDesc from (SELECT (select distinct emplid from MV_FIELD_EMPLOYEE_RBU "
							+ " where emplid=x.reports_to_emplid) REPORTS_TO_EMPLID, emplid, GROUP_CD salesOrgCd, sales_group salesOrgDesc "
							+ " FROM MV_FIELD_EMPLOYEE_RBU x CONNECT BY PRIOR emplid=x.reports_to_emplid START WITH reports_to_emplid = :emplid ) where salesOrgCd is not null order by salesOrgDesc asc");
			query.setParameter("emplid", emplid);
			List salOrg = query.list();
			List<SalesOrgBean> templist = new ArrayList<SalesOrgBean>();
			templist.clear();
			Iterator it = salOrg.iterator();
			while (it.hasNext()) {
				SalesOrgBean saOrgList = new SalesOrgBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					saOrgList.setSalesOrgCd(field[0].toString());
				if (field[1] != null)
					saOrgList.setSalesOrgDesc(field[1].toString());
				templist.add(saOrgList);
			}

			salesOrgBeans = templist.toArray(new SalesOrgBean[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSALESORGBYUSER Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return salesOrgBeans;
	}

	public int checkGroup(String groupName) {

		int chk = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select count(*) from user_group where group_name = :groupName");
			query.setParameter("groupName", groupName);
			List<Object> rs = query.list();
			for (Object i : rs) {
				chk = Integer.parseInt(i.toString());
			}

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("checkGroup Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return chk;
	}

	public void deleteAccessToReports(String groupId) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("update training_report set allow_group='' where allow_group=(select group_name from user_group where group_id= :groupId)");
			query.setParameter("groupId", groupId);
			session.beginTransaction();
			int dr = query.executeUpdate();
			System.out.println("Number of Records Deleted" + dr);
			session.getTransaction().commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("deleteAccessToReports Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void deleteGroup(String groupId) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("delete from user_group where group_id = :groupId");
			query.setParameter("groupId", groupId);
			session.beginTransaction();
			int dr = query.executeUpdate();
			System.out.println("Number of Records Deleted" + dr);
			session.getTransaction().commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("deleteAccessToReports Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public String[] getAllDeploymentIds() {
		// TODO Auto-generated method stub
		String[] deptIds = null;
		Session session = HibernateUtils.getHibernateSession();
		String s = "";
		try {
			/*
			 * Old Query For Fetching DeploymentIds.
			 */
			
			/*
			Query query = session
					.createSQLQuery("SELECT DISTINCT to_number(deployment_package_id) "
							+ " FROM tr_v_s4_sales_position_product "
							+ " ORDER BY to_number(deployment_package_id) DESC");
			*/
			
			/*
			 * New Query Added By Ankit on 15 sept.
			 */
			
			Query query = session
					.createSQLQuery("SELECT DISTINCT(DEPLOYMENT_PACKAGE_ID) from DEPLOYMENT_DETAILS order by DEPLOYMENT_PACKAGE_ID DESC");
			
			
			List dids = query.list();
			List<String> sts = new ArrayList<String>();
			Iterator it = dids.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					s = field.toString();
				sts.add(s);
			}
			deptIds = sts.toArray(new String[dids.size()]);

		} catch (HibernateException e) {
			e.printStackTrace();
			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			System.out.println("getAllDeploymentIds Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return deptIds;
	}

	public void generateGapReportData(String duration, String deplID,
			String selectedProducts, String salesOrgCodes) {

		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			System.out.println("quer1" + queryString);

			queryString
					.append("call GENERATE_PRODUCT_GAP_REPORT(:duration,:deplID,:selectedProducts,:salesOrgCodes) ");

			String q1 = queryString.toString();
			System.out.println("quer2 " + queryString);

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(q1);
			q.setParameter("duration", duration);
			q.setParameter("deplID", deplID);
			q.setParameter("selectedProducts", selectedProducts);
			q.setParameter("salesOrgCodes", salesOrgCodes);

			int result = q.executeUpdate();

			System.out.println("Result : " + result);
			ts.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			System.out.println("generateGapReportData Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public GapAnalysisEntry[] getGapReportData() throws ParseException {
		GapAnalysisEntry[] gapAnalysisEntryBean = new GapAnalysisEntry[200];

		Session session = HibernateUtils.getHibernateSession();
		try {

			String SQL = "SELECT a.emplid as empID, a.guid as guID,a.first_name as firstName,a.last_name as lastName,a.email_address as emailAddr,a.role_cd as rolecd, "
					+ " a.SALES_GROUP as salesOrg,a.SALESGROUPCD as salesOrgCd,a.MANAGER_FNAME as mngrFirstName,a.MANAGER_LNAME as mngrLastName, "
					+ " a.MANAGER_EMAIL as mngrEmail,a.product_desc as productName,a.status as status,a.status_date as statusDate "
					+ " from GAP_RPT_FFRA_TEMP a ORDER BY a.emplid,a.product_desc ";

			Query query = session.createSQLQuery(SQL);
			List gapAnalysisEntryList = query.list();
			List<GapAnalysisEntry> templist = new ArrayList<GapAnalysisEntry>();
			Iterator it = gapAnalysisEntryList.iterator();
			while (it.hasNext()) {
				GapAnalysisEntry gapAnalysisEntry = new GapAnalysisEntry();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					gapAnalysisEntry.setEmpID(field[0].toString());
				if (field[1] != null)
					gapAnalysisEntry.setGuID(field[1].toString());
				if (field[2] != null)
					gapAnalysisEntry.setFirstName(field[2].toString());
				if (field[3] != null)
					gapAnalysisEntry.setLastName(field[3].toString());
				if (field[4] != null)
					gapAnalysisEntry.setEmailAddr(field[4].toString());
				if (field[5] != null)
					gapAnalysisEntry.setRolecd(field[5].toString());
				if (field[6] != null)
					gapAnalysisEntry.setSalesOrg(field[6].toString());
				if (field[7] != null)
					gapAnalysisEntry.setSalesOrgCd(field[7].toString());
				if (field[8] != null)
					gapAnalysisEntry.setMngrFirstName(field[8].toString());
				if (field[9] != null)
					gapAnalysisEntry.setMngrLastName(field[9].toString());
				if (field[10] != null)
					gapAnalysisEntry.setMngrEmail(field[10].toString());
				if (field[11] != null)
					gapAnalysisEntry.setProductName(field[11].toString());
				if (field[12] != null)
					gapAnalysisEntry.setStatus(field[12].toString());
				if (field[13] != null)
					gapAnalysisEntry.setStatusDate((Date) (field[13]));

				templist.add(gapAnalysisEntry);
			}

			gapAnalysisEntryBean = templist
					.toArray(new GapAnalysisEntry[templist.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getGapReportData Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return gapAnalysisEntryBean;
	}

	public String[] getGapReportProductNames() {

		String[] productNames = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT product FROM gap_rpt_ffra_codes order by product");
			List<String> pn = query.list();
			productNames = pn.toArray(new String[pn.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("getGapReportProductNames Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return productNames;
	}

	public SalesOrgCodeDesciption[] getSalesOrgCodes() {

		SalesOrgCodeDesciption[] socDesc = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT f.sales_organization_cd AS salesOrgCd, f.sales_organization_desc AS salesOrgDesc "
							+ " FROM tr.mv_field_employee_rbu a,tr.v_bu_salesgroup_rbu f  WHERE a.sales_group(+) = f.sales_organization_desc ORDER BY salesOrgCd ");
			List<Object> sogcod = query.list();
			Iterator it = sogcod.iterator();
			List<SalesOrgCodeDesciption> templist = new ArrayList<SalesOrgCodeDesciption>();
			templist.clear();
			while (it.hasNext()) {
				SalesOrgCodeDesciption salesOrgCodeDesciption = new SalesOrgCodeDesciption();
				Object[] so = (Object[]) it.next();
				if (so[0] != null)
					salesOrgCodeDesciption.setSalesOrgCd(so[0].toString());
				if (so[1] != null)
					salesOrgCodeDesciption.setSalesOrgDesc(so[1].toString());

				templist.add(salesOrgCodeDesciption);

			}

			socDesc = templist.toArray(new SalesOrgCodeDesciption[templist
					.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSalesOrgCodes Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return socDesc;
	}

	public MenuList[] getAllMenuArchiveByID(String menuID) {

		MenuList[] menuLists = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			// Transaction tx= session.beginTransaction();
			Query query = session
					.createSQLQuery("SELECT SORT_ORDER SORTORDER,LEVEL,TRAINING_REPORT_LABEL LABEL,TRAINING_REPORT_URL URL,ALLOW_GROUP ALLOW, ACTIVE,TRAINING_REPORT_ID ID, TRACK_ID trackId "
							+ " FROM TRAINING_REPORT START WITH PARENT = :menuID AND ACTIVE=0 and delete_flag ='N'  CONNECT BY PRIOR TRAINING_REPORT_ID = PARENT  "
							+ " ORDER SIBLINGS BY SORT_ORDER  ");
			query.setParameter("menuID", menuID);
			List<Object[]> mlList = query.list();
			List<MenuList> templist = new ArrayList<MenuList>();
			templist.clear();
			Iterator it = mlList.iterator();
			while (it.hasNext()) {
				MenuList menuList = new MenuList();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					menuList.setSortorder(field[0].toString());
				if (field[1] != null)
					menuList.setLevel(field[1].toString());
				if (field[2] != null)
					menuList.setLabel(field[2].toString());
				if (field[3] != null)
					menuList.setUrl(field[3].toString());
				if (field[4] != null)
					menuList.setAllow(field[4].toString());
				if (field[5] != null)
					menuList.setActive(field[5].toString());
				if (field[6] != null)
					menuList.setId(field[6].toString());
				if (field[7] != null)
					menuList.setTrackId(field[7].toString());

				templist.add(menuList);
			}

			menuLists = templist.toArray(new MenuList[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getAllMenuArchiveByID Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return menuLists;
	}

	public MenuList getMenuItemByID(String menuID) {

		MenuList mList = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			// Transaction tx= session.beginTransaction();
			Query query = session
					.createSQLQuery("SELECT TRAINING_REPORT_LABEL LABEL,TRAINING_REPORT_URL URL,ALLOW_GROUP ALLOW, ACTIVE,TRAINING_REPORT_ID ID,TRACK_ID  trackId "
							+ " FROM TRAINING_REPORT where TRAINING_REPORT_ID = :menuID ");
			query.setParameter("menuID", menuID);
			List mlList = query.list();

			Iterator it = mlList.iterator();
			while (it.hasNext()) {
				MenuList menuList = new MenuList();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					menuList.setLabel(field[0].toString());
				if (field[1] != null)
					menuList.setUrl(field[1].toString());
				if (field[2] != null)
					menuList.setAllow(field[2].toString());
				if (field[3] != null)
					menuList.setActive(field[3].toString());
				if (field[4] != null)
					menuList.setId(field[4].toString());
				if (field[5] != null)
					menuList.setTrackId(field[5].toString());
				mList = menuList;
			}

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getMenuItemByID Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return mList;
	}

	public void removeFromGroup(String menuId, String parentId) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("UPDATE TRAINING_REPORT SET PARENT= :menuId ,SORT_ORDER=null WHERE TRAINING_REPORT_ID = :parentId ");
			query.setParameter("menuId", menuId);
			query.setParameter("parentId", parentId);
			int i = query.executeUpdate();
			System.out.println("Group Deleted" + i);
			tx.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("removeFromGroup Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void addToGroup(String parentId, String childId) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("UPDATE TRAINING_REPORT SET PARENT= :parentId WHERE TRAINING_REPORT_ID = :childId ");
			query.setParameter("parentId", parentId);
			query.setParameter("childId", childId);
			int i = query.executeUpdate();
			System.out.println(i + "Group Added");
			tx.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("addToGroup Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void updateSortMenuByID(String menuID, String order) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("UPDATE TRAINING_REPORT  SET SORT_ORDER = :order  WHERE TRAINING_REPORT_ID = :menuID ");
			query.setParameter("menuID", menuID);
			query.setParameter("order", order);
			int i = query.executeUpdate();
			System.out.println("Updated Sort Menu" + i);
			tx.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("updateSortMenuByID Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void updateAccessMenuByID(String menuID, String access) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("UPDATE TRAINING_REPORT SET allow_group = :access WHERE TRAINING_REPORT_ID = :menuID ");
			query.setParameter("menuID", menuID);
			query.setParameter("access", access);
			int i = query.executeUpdate();
			System.out.println("Updated Access Menu" + i);
			tx.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("updateAccessMenuByID Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void deleteMenuByID(String menuID) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("UPDATE TRAINING_REPORT  SET DELETE_FLAG='Y' WHERE TRAINING_REPORT_ID = :menuID ");
			query.setParameter("menuID", menuID);
			int i = query.executeUpdate();
			System.out.println("Deleted Menu" + i);
			tx.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			System.out.println("deleteMenuByID Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void updateActiveStatusMenuByID(String menuID, String status) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("UPDATE TRAINING_REPORT  SET ACTIVE = :status,SORT_ORDER=null  WHERE TRAINING_REPORT_ID = :menuID ");
			query.setParameter("menuID", menuID);
			query.setParameter("status", status);
			int i = query.executeUpdate();
			System.out.println("Updated Active Status Menu" + i);
			tx.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("updateActiveStatusMenuByID Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void updateActiveStatusMenuByParentID(String menuID, String status) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("UPDATE TRAINING_REPORT SET ACTIVE = :status WHERE PARENT = :menuID ");
			query.setParameter("menuID", menuID);
			query.setParameter("status", status);
			int i = query.executeUpdate();
			System.out.println("Updated Active Status Menu" + i);
			tx.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("updateActiveStatusMenuByID Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public MenuList[] getAllMenuByID(String menuID) {

		MenuList[] menuLists = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			// Transaction tx= session.beginTransaction();
			
/*			Commented this part of query*/
		/*	Query query = session
					.createSQLQuery("SELECT SORT_ORDER SORTORDER,LEVEL,TRAINING_REPORT_LABEL LABEL,TRAINING_REPORT_URL URL,ALLOW_GROUP ALLOW, ACTIVE,TRAINING_REPORT_ID ID, TRACK_ID trackId "
							+ " FROM TRAINING_REPORT START WITH PARENT = :menuID AND ACTIVE=1 and delete_flag ='N'  "
							+ " CONNECT BY PRIOR TRAINING_REPORT_ID = PARENT  ORDER SIBLINGS BY SORT_ORDER  ");*/
			
/*	Code changed by sanjeev 25 August 2015: Modified query for not fetching deleted activity in edit page*/
			
			Query query = session
					.createSQLQuery("SELECT SORT_ORDER SORTORDER,LEVEL,TRAINING_REPORT_LABEL LABEL,TRAINING_REPORT_URL URL,ALLOW_GROUP ALLOW, ACTIVE,TRAINING_REPORT_ID ID, TRACK_ID trackId "
					+ " FROM TRAINING_REPORT where delete_flag ='N' AND ACTIVE=1 "
					+ " START WITH PARENT = :menuID AND ACTIVE=1 and delete_flag ='N'  "	
					+ " CONNECT BY PRIOR TRAINING_REPORT_ID = PARENT  ORDER SIBLINGS BY SORT_ORDER  "); 
			
			
			
			
			query.setParameter("menuID", menuID);
			List<Object[]> mlList = query.list();
			List<MenuList> templist = new ArrayList<MenuList>();
			templist.clear();
			Iterator it = mlList.iterator();
			while (it.hasNext()) {
				MenuList menuList = new MenuList();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					menuList.setSortorder(field[0].toString());
				if (field[1] != null)
					menuList.setLevel(field[1].toString());
				if (field[2] != null)
					menuList.setLabel(field[2].toString());
				if (field[3] != null)
					menuList.setUrl(field[3].toString());
				if (field[4] != null)
					menuList.setAllow(field[4].toString());
				if (field[5] != null)
					menuList.setActive(field[5].toString());
				if (field[6] != null)
					menuList.setId(field[6].toString());
				if (field[7] != null)
					menuList.setTrackId(field[7].toString());

				templist.add(menuList);
			}

			menuLists = templist.toArray(new MenuList[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getAllMenuByID Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return menuLists;
	}

	public FutureAllignmentBuDataBean[] getBu() {

		Session session = HibernateUtils.getHibernateSession();
		FutureAllignmentBuDataBean[] futureAllignmentBuDataBeans = new FutureAllignmentBuDataBean[200];
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT BU as BU from V_RBU_FUTURE_ALIGNMENT where BU is not null order by bu asc");
			List buLst = query.list();
			List<FutureAllignmentBuDataBean> beanList = new ArrayList<FutureAllignmentBuDataBean>();
			Iterator it = buLst.iterator();
			while (it.hasNext()) {
				FutureAllignmentBuDataBean bean = new FutureAllignmentBuDataBean();
				String busUnit = it.next().toString();
				bean.setBu(busUnit);

				beanList.add(bean);
			}
			futureAllignmentBuDataBeans = beanList
					.toArray(new FutureAllignmentBuDataBean[beanList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getBu Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return futureAllignmentBuDataBeans;
	}

	public FutureAllignmentRBUDataBean[] getRbuForRBU(String bu) {

		Session session = HibernateUtils.getHibernateSession();
		FutureAllignmentRBUDataBean[] futureAllignmentRBUDataBeans = new FutureAllignmentRBUDataBean[200];
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT  RBU_DESC as RBU from V_RBU_FUTURE_ALIGNMENT  where bu = :bu and RBU_DESC is not null and RBU not in ('CGC') order by RBU_DESC asc");
			query.setParameter("bu", bu);
			List rbuLst = query.list();
			List<FutureAllignmentRBUDataBean> beanList = new ArrayList<FutureAllignmentRBUDataBean>();
			Iterator it = rbuLst.iterator();
			while (it.hasNext()) {
				FutureAllignmentRBUDataBean bean = new FutureAllignmentRBUDataBean();
				String rbuUnit = it.next().toString();
				bean.setRbu(rbuUnit);

				beanList.add(bean);
			}
			futureAllignmentRBUDataBeans = beanList
					.toArray(new FutureAllignmentRBUDataBean[beanList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getRbuForRBU Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return futureAllignmentRBUDataBeans;
	}

	public TeamBean[] getAllPDFHSTEAM() {

		Session session = HibernateUtils.getHibernateSession();
		TeamBean[] teamsBeans = new TeamBean[200];
		try {
			Query query = session
					.createSQLQuery("select distinct team_cd teamCd, team_desc  teamDesc from PWRA_PRODUCT_ASSIGNMENT");
			List saleLst = query.list();
			List<TeamBean> beanList = new ArrayList<TeamBean>();
			Iterator it = saleLst.iterator();
			while (it.hasNext()) {
				TeamBean bean = new TeamBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					bean.setTeamCd(field[0].toString());
				if (field[1] != null)
					bean.setTeamDesc(field[1].toString());
				beanList.add(bean);
			}
			teamsBeans = beanList.toArray(new TeamBean[beanList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getAllPDFHSTEAM Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return teamsBeans;
	}

	public P2LRegistration[] getP2LRegistrations() {

		Session session = HibernateUtils.getHibernateSession();
		P2LRegistration[] p2lRegistrations = new P2LRegistration[200];
		try {
			Query query = session.createSQLQuery("select "
					+ " EMP_NUMBER as empNumber,  "
					+ " CLASS_CODE as classCode,  "
					+ " START_DATE as startDate, "
					+ " REGISTRATION_DATE as registrationDate, "
					+ " COMPLETION_DATE as completionDate,  "
					+ " LAUNCH_DATE as launchDate,  " + " SCORE as score, "
					+ " PASSED as passed,  "
					+ " CANCELLATION_DATE as cancellationDate, "
					+ " PAYMENT_TERM as paymentTerm, " + " COST as cost,  "
					+ " CURRENCY as currency, " + " TIMEZONE as timezone,  "
					+ " STATUS as status,  " + " NOTES as notes,  "
					+ " SS_ACTIVITY_CODE as ssActivityCode, "
					+ " SS_ACTIVITY_START_DATE as ssActivityStartDate, "
					+ " CREATE_REGISTRATION as createRegistration " + " from "
					+ " v_pwra_p2l_course_reg");

			List<P2LRegistration> p2lLst = query.list();
			List<P2LRegistration> beanList = new ArrayList<P2LRegistration>();
			Iterator it = p2lLst.iterator();
			while (it.hasNext()) {
				P2LRegistration bean = new P2LRegistration();
				Object[] p2lReg = (Object[]) it.next();
				if (p2lReg[0] != null)
					bean.setEmpNumber(Integer.parseInt(p2lReg[0].toString()));
				if (p2lReg[1] != null)
					bean.setClassCode(p2lReg[1].toString());
				if (p2lReg[2] != null)
					bean.setStartDate(p2lReg[2].toString());
				if (p2lReg[3] != null)
					bean.setRegistrationDate(p2lReg[3].toString());
				if (p2lReg[4] != null)
					bean.setCompletionDate(p2lReg[4].toString());
				if (p2lReg[5] != null)
					bean.setLaunchDate(p2lReg[5].toString());
				if (p2lReg[6] != null)
					bean.setScore(Float.parseFloat(p2lReg[6].toString()));
				if (p2lReg[7] != null)
					bean.setPassed(Integer.parseInt(p2lReg[7].toString()));
				if (p2lReg[8] != null)
					bean.setCancellationDate(p2lReg[8].toString());
				if (p2lReg[9] != null)
					bean.setPaymentTerm(p2lReg[9].toString());
				if (p2lReg[10] != null)
					bean.setCost(Float.parseFloat(p2lReg[10].toString()));
				if (p2lReg[11] != null)
					bean.setCurrency(p2lReg[11].toString());
				if (p2lReg[12] != null)
					bean.setTimezone(p2lReg[12].toString());
				if (p2lReg[13] != null)
					bean.setStatus(Integer.parseInt(p2lReg[13].toString()));
				if (p2lReg[14] != null)
					bean.setNotes(p2lReg[14].toString());
				if (p2lReg[15] != null)
					bean.setSsActivityCode(p2lReg[15].toString());
				if (p2lReg[16] != null)
					bean.setSsActivityStartDate(p2lReg[16].toString());
				if (p2lReg[17] != null)
					bean.setCreateRegistration(Integer.parseInt(p2lReg[17]
							.toString()));

				beanList.add(bean);
			}
			p2lRegistrations = beanList.toArray(new P2LRegistration[beanList
					.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getP2LRegistrations Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return p2lRegistrations;
	}



	public TrainingWeeks[] getTrainingWeeks(String query) {

		Session session = HibernateUtils.getHibernateSession();
		TrainingWeeks[] trainingWeeks = null;
		try {
			Query query1 = session.createSQLQuery(query);
			List sList = query1.list();
			List<TrainingWeeks> gList = new ArrayList<TrainingWeeks>();
			Iterator it = sList.iterator();
			
			while (it.hasNext()) {
				TrainingWeeks training = new TrainingWeeks();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					training.setWeek_id(field[0].toString());
				if (field[1] != null)
					training.setStart_date(field[1].toString());
				if (field[2] != null)
					training.setEnd_date(field[2].toString());
				if (field[3] != null)
					training.setWeek_name(field[3].toString());

				gList.add(training);
			}
			trainingWeeks = gList.toArray(new TrainingWeeks[gList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("TrainingWeeks Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return trainingWeeks;
	}

	public TrainingWeeks[] getTrainingWeeksP4(String query) {

		Session session = HibernateUtils.getHibernateSession();
		TrainingWeeks[] trainingWeeks = null;
		try {
			Query query1 = session.createSQLQuery(query);
			List sList = query1.list();
			List<TrainingWeeks> gList = new ArrayList<TrainingWeeks>();
			Iterator it = sList.iterator();
			
			while (it.hasNext()) {
				TrainingWeeks training = new TrainingWeeks();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					training.setWeek_id(field[0].toString());
				if (field[1] != null)
					training.setWeek_name(field[1].toString());

				gList.add(training);
			}
			trainingWeeks = gList.toArray(new TrainingWeeks[gList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("TrainingWeeks Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return trainingWeeks;
	}
	public ProductDataBean[] getProducts() {

		Session session = HibernateUtils.getHibernateSession();
		ProductDataBean[] productDataBeans = null;
		try {
			Query query1 = session
					.createSQLQuery(" SELECT PRODUCT_CD as PRODUCTCD, PRODUCT_DESC as PRODUCTDESC "
							+ "    from RBU_PRODUCTS where TRAININGEXISTS='Y'  and product_cd NOT IN ('ARCP','GEOD','LYRC','TOVZ') "
							+ "   order by product_cd asc");
			List prList = query1.list();
			List<ProductDataBean> pList = new ArrayList<ProductDataBean>();
			Iterator it = prList.iterator();
			while (it.hasNext()) {
				ProductDataBean pBean = new ProductDataBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					pBean.setProductCd(field[0].toString());
				if (field[1] != null)
					pBean.setProductDesc(field[1].toString());
				pList.add(pBean);
			}
			productDataBeans = pList.toArray(new ProductDataBean[pList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getProducts Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return productDataBeans;
	}

	public FutureAllignmentRBUDataBean[] getRbu() {

		Session session = HibernateUtils.getHibernateSession();
		FutureAllignmentRBUDataBean[] futureAllignmentRBUDataBeans = new FutureAllignmentRBUDataBean[200];
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT RBU_DESC as RBU from V_RBU_FUTURE_ALIGNMENT where RBU_DESC is not null and RBU not in ('CGC') order by RBU_DESC asc");
			List rbuLst = query.list();
			List<FutureAllignmentRBUDataBean> beanList = new ArrayList<FutureAllignmentRBUDataBean>();
			Iterator it = rbuLst.iterator();
			while (it.hasNext()) {
				FutureAllignmentRBUDataBean bean = new FutureAllignmentRBUDataBean();
				String rbuUnit = it.next().toString();
				bean.setRbu(rbuUnit);

				beanList.add(bean);
			}
			futureAllignmentRBUDataBeans = beanList
					.toArray(new FutureAllignmentRBUDataBean[beanList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getRbu Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return futureAllignmentRBUDataBeans;
	}

	public RBUClassRosterBean[] getClassRosterReport(String string) {

		Session session = HibernateUtils.getHibernateSession();
		RBUClassRosterBean[] rbuClass = null;
		try {
			Query query1 = session.createSQLQuery(string);
			List sList = query1.list();
			List<RBUClassRosterBean> gList = new ArrayList<RBUClassRosterBean>();
			Iterator it = sList.iterator();
			while (it.hasNext()) {
				RBUClassRosterBean rbu = new RBUClassRosterBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					rbu.setProduct(field[0].toString());
				if (field[1] != null)
					rbu.setStartDate(field[1].toString());
				if (field[2] != null)
					rbu.setEmplId(field[2].toString());
				if (field[3] != null)
					rbu.setFirstName(field[3].toString());
				if (field[4] != null)
					rbu.setLastName(field[4].toString());
				if (field[5] != null)
					rbu.setIsTrainer(field[5].toString());
				if (field[6] != null)
					rbu.setRoomName(field[6].toString());
				if (field[7] != null)
					rbu.setTableNumber(field[7].toString());

				gList.add(rbu);
			}
			rbuClass = gList.toArray(new RBUClassRosterBean[gList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("RBUClassRosterBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return rbuClass;
	}

	public EmployeeInfo getImageURL(String emplid) {

		EmployeeInfo emp = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query1 = session
					.createSQLQuery("SELECT PHOTO_FILE_NAME IMAGEURL FROM EMPLOYEE_PICS WHERE EMPLID= :emplid ");
			query1.setParameter("emplid", emplid);
			List img = query1.list();
			Iterator it = img.iterator();
			while (it.hasNext()) {
				emp = new EmployeeInfo();
				String image = (String) it.next();
				emp.setImageURL(image);
			}
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getImageURL Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return emp;
	}

	public Iterator<RBUProduct> getPreRBUProduct(String emplid) {
		Session session = HibernateUtils.getHibernateSession();
		Iterator<RBUProduct> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT P.PRODUCT_DESC PRODUCT,TEAM_CD PRETEAM "
							+ " FROM V_RBU_PRE_PRODUCTS P"
							+ " WHERE P.EMPLID=:emplid ");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<RBUProduct> ls = new ArrayList<RBUProduct>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				RBUProduct pp = new RBUProduct();
				if (oj[0] != null)
					pp.setProduct(oj[0].toString());
				if (oj[1] != null)
					pp.setPreTeam(oj[1].toString());

				ls.add(pp);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getPreRBUProduct Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<TrainingMaterialHistory> getTrainingMaterialHistory(
			String emplid) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<TrainingMaterialHistory> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT INV_ID INVID,STATUS STATUS,TITLE MATERIALDESC,DATEORDERED ORDERDATE,TRACKING_NUMBER TRACKINGNUMBER, TRM_ORDER_NBR_TXT TRMORDERID "
							+ " FROM V_RBU_TRM_ORDER_STATUS "
							+ " WHERE EMPLID = :emplid "
							+ " ORDER BY DATEORDERED,INV_ID");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<TrainingMaterialHistory> ls = new ArrayList<TrainingMaterialHistory>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				TrainingMaterialHistory ts = new TrainingMaterialHistory();
				if (oj[0] != null)
					ts.setInvID(oj[0].toString());
				if (oj[1] != null)
					ts.setStatus(oj[1].toString());
				if (oj[2] != null)
					ts.setMaterialDesc(oj[2].toString());
				if (oj[3] != null)
					ts.setOrderDate((Date) oj[3]);
				if (oj[4] != null)
					ts.setTrackingNumber(oj[4].toString());
				if (oj[5] != null)
					ts.setTrmOrderID(oj[5].toString());

				ls.add(ts);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("getTrainingMaterialHistory Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public EmployeeInfo getEmployeeInfoRBU(String emplid) {

		EmployeeInfo emp = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			// Transaction tx = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT E.EMPLID EMPLID, "
					+ "   E.PROMOTIONDATE PROMOTIONDATE, "
					+ "   E.HIREDATE HIREDATE, " + "   E.GENDER GENDER, "
					+ "   E.EMAIL EMAIL, "
					+ "   E.REPORTTOEMPLID REPORTTOEMPLID, " + "   E.STATUS , "
					+ "   E.AREACD AREACD, " + "   E.AREADESC AREADESC, "
					+ "   E.REGIONCD REGIONCD, "
					+ "   E.REGIONDESC REGIONDESC, "
					+ "   E.DISTRICTID DISTRICTID, "
					+ "   E.DISTRICTDESC  DISTRICTDESC, "
					+ "   E.TERRITORYID TERRITORYID, "
					+ "   E.TERRITORYROLE TERRITORYROLE, "
					+ "   E.TEAMCD TEAMCD, " + "   M.CLUSTER_DESC CLUSTERCD, "
					+ "   E.LASTNAME LASTNAME, "
					+ "   E.MIDDLENAME MIDDLENAME, "
					+ "   E.PREFERREDNAME PREFERREDNAME, " + "   E.BU, "
					+ "   E.FUTURE_MANAGER "
					+ "   FROM V_RBU_FIELD_EMPLOYEE E, "
					+ "   MV_CLUSTER_CODE_MAP M "
					+ "   WHERE E.EMPLID=:emplid "
					+ "   AND M.CLUSTER_CD(+) = E.CLUSTERCD");
			query.setParameter("emplid", emplid);
			List<Object> lst = query.list();
			List<EmployeeInfo> eInfo = new ArrayList<EmployeeInfo>();
			Iterator it = lst.iterator();
			while (it.hasNext()) {
				emp = new EmployeeInfo();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					emp.setEmplID(field[0].toString());
				if (field[1] != null)
					emp.setPromotionDate((Date) field[1]);
				if (field[2] != null)
					emp.setHireDate((Date) field[2]);
				if (field[3] != null)
					emp.setGender(field[3].toString());
				if (field[4] != null)
					emp.setEmail(field[4].toString());
				if (field[5] != null)
					emp.setReportToEmplID(field[5].toString());
				if (field[6] != null)
					emp.setStatus(field[6].toString());
				if (field[7] != null)
					emp.setAreaCD(field[7].toString());
				if (field[8] != null)
					emp.setAreaDesc(field[8].toString());
				if (field[9] != null)
					emp.setRegionCD(field[9].toString());
				if (field[10] != null)
					emp.setRegionDesc(field[10].toString());
				if (field[11] != null)
					emp.setDistrictID(field[11].toString());
				if (field[12] != null)
					emp.setDistrictDesc(field[12].toString());
				if (field[13] != null)
					emp.setTerritoryID(field[13].toString());
				if (field[14] != null)
					emp.setTerritoryRole(field[14].toString());
				if (field[15] != null)
					emp.setTeamCD(field[15].toString());
				if (field[16] != null)
					emp.setClusterCD(field[16].toString());
				if (field[17] != null)
					emp.setLastName(field[17].toString());
				if (field[18] != null)
					emp.setMiddleName(field[18].toString());
				if (field[19] != null)
					emp.setPreferredName(field[19].toString());
				if (field[20] != null)
					emp.setBu(field[20].toString());
				if (field[21] != null)
					emp.setFuture_manager(field[21].toString());
			}
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getEmployeeInfo Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return emp;
	}

	public Iterator<PDFProduct> getPrePDFProduct(String emplid) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<PDFProduct> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT P.PRODUCT_DESC PRODUCT,pa.TEAM_DESC TEAM FROM V_PRE_PWRA_PRODUCTS P,FFT_PRODUCT_ASSIGNMENT PA "
							+ "  WHERE P.TEAM_CD = PA.TEAM_CD "
							+ "  AND P.TERRITORY_ROLE_CD = PA.ROLE_CD "
							+ "  AND P.PRODUCT_CD = PA.PRODUCT_CD "
							+ "  AND EMPLID= :emplid");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<PDFProduct> ls = new ArrayList<PDFProduct>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				PDFProduct pp = new PDFProduct();
				if (oj[0] != null)
					pp.setProduct(oj[0].toString());
				if (oj[1] != null)
					pp.setTeam(oj[1].toString());

				ls.add(pp);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getPrePDFProduct Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<PDFProduct> getPostPDFProduct(String emplid) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<PDFProduct> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT P.PRODUCT_DESC PRODUCT,RA.TEAM_DESC TEAM FROM V_PWRA_PRODUCTS P, V_REALIGNMENT RA "
							+ " WHERE P.EMPLID = RA.EMPLID  AND P.EMPLID= :emplid ");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<PDFProduct> ls = new ArrayList<PDFProduct>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				PDFProduct pp = new PDFProduct();
				if (oj[0] != null)
					pp.setProduct(oj[0].toString());
				if (oj[1] != null)
					pp.setTeam(oj[1].toString());

				ls.add(pp);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getPostPDFProduct Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<RBUPostProduct> getPostRBUProduct(String emplid) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<RBUPostProduct> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT P.PRODUCT_DESC PRODUCT, BU  FROM V_RBU_FUTURE_PRODUCTS P "
							+ " WHERE P.EMPLID=:emplid   ");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<RBUPostProduct> ls = new ArrayList<RBUPostProduct>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				RBUPostProduct pp = new RBUPostProduct();
				if (oj[0] != null)
					pp.setProduct(oj[0].toString());
				if (oj[0] != null)
					pp.setBU(oj[1].toString());

				ls.add(pp);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getPostRBUProduct Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<PLCStatus> getSPFStatus(String emplid) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<PLCStatus> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT "
							+ " (CASE WHEN V.PRODUCT_CD = 'PLCA' THEN 'General Session' ELSE P.PRODUCT_DESC END) PRODUCT, "
							+ " V.PRODUCT_CD PRODUCTCODE, "
							+ " DECODE(V.OVERALL_STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') STATUS "
							+ " FROM "
							+ " ( "
							+ " SELECT DISTINCT pd.EMPLID, pd.PRODUCT_CD, DECODE(g1.status,'','P',g1.status) AS overall_status "
							+ " FROM v_spf_plc_data pd,(SELECT DISTINCT emplid,product_cd,status FROM v_spf_plc_data WHERE status IN('L','I')) g1 "
							+ " WHERE pd.emplid=g1.emplid(+)  "
							+ " AND pd.product_cd=g1.product_cd(+) "
							+ " ) V,  " + " PRODUCT_CODE_MAP P "
							+ " WHERE P.PRODUCT_CD(+)=V.PRODUCT_CD "
							+ " AND EMPLID = :emplid");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<PLCStatus> ls = new ArrayList<PLCStatus>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				PLCStatus pp = new PLCStatus();
				if (oj[0] != null)
					pp.setProduct(oj[0].toString());
				if (oj[0] != null)
					pp.setProductCode(oj[1].toString());
				if (oj[0] != null)
					pp.setStatus(oj[2].toString());

				ls.add(pp);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSPFStatus Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<PLCExamStatus> getSPFExamStatus(String emplid) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<PLCExamStatus> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT EXAM_TYPE EXAMTYPE,EXAM_NAME EXAMNAME,TEST_SCORE SCORE ,DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') EXAMSTATUS "
							+ "  ,EXAM_TAKEN_DATE COMPLETIONDATE ,PRODUCT_CD PRODUCTCODE FROM V_SPF_PLC_DATA V WHERE V.EMPLID= :emplid");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<PLCExamStatus> ls = new ArrayList<PLCExamStatus>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				PLCExamStatus pp = new PLCExamStatus();
				if (oj[0] != null)
					pp.setExamType(oj[0].toString());
				if (oj[1] != null)
					pp.setExamName(oj[1].toString());
				if (oj[2] != null)
					pp.setScore(oj[2].toString());
				if (oj[3] != null)
					pp.setExamStatus(oj[3].toString());
				if (oj[4] != null)
					pp.setCompletionDate((Date) oj[4]);
				if (oj[5] != null)
					pp.setProductCode(oj[5].toString());

				ls.add(pp);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSPFExamStatus Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<TrainingSchedule> getTrainingSchedule(String emplid) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<TrainingSchedule> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT C.COURSE_ID COURSEID,C.COURSE_DESCRIPTION COURSEDESCRIPTION,C.START_DATE COURSESCHEDULE "
							+ "  FROM COURSE C,COURSE_ASSIGNMENT CA "
							+ " WHERE C.COURSE_ID=CA.COURSE_ID "
							+ "  AND C.COURSE_ID >299 AND CA.TRAINEE_SSN = :emplid  ");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<TrainingSchedule> ls = new ArrayList<TrainingSchedule>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				TrainingSchedule ts = new TrainingSchedule();
				if (oj[0] != null)
					ts.setCourseID(oj[0].toString());
				if (oj[1] != null)
					ts.setCourseDescription(oj[1].toString());
				if (oj[2] != null)
					ts.setCourseSchedule((Date) oj[2]);

				ls.add(ts);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getTrainingSchedule Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<TrainingSchedule> getRBUTrainingSchedule(String emplid) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<TrainingSchedule> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT C.CLASS_ID COURSEID,C.CLASS_NAME COURSEDESCRIPTION, C.START_DATE COURSESCHEDULE "
							+ " FROM RBU_CLASS C,RBU_CLASS_ASSIGNMENT CA "
							+ " WHERE C.CLASS_ID = CA.CLASS_ID "
							+ " AND CA.EMPLID = :emplid");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<TrainingSchedule> ls = new ArrayList<TrainingSchedule>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				TrainingSchedule ts = new TrainingSchedule();
				if (oj[0] != null)
					ts.setCourseID(oj[0].toString());
				if (oj[1] != null)
					ts.setCourseDescription(oj[1].toString());
				if (oj[2] != null)
					ts.setCourseSchedule((Date) oj[2]);

				ls.add(ts);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getRBUTrainingSchedule Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<TrainingScheduleList> getSPFTrainingScheduleListPHR(
			String courseID) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<TrainingScheduleList> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT START_DATE STARTDATE, COURSE_ID COURSEID "
							+ " FROM COURSE "
							+ " WHERE COURSE_ID = :courseID "
							+ " union "
							+ " SELECT START_DATE STARTDATE, COURSE_ID COURSEID "
							+ " FROM COURSE "
							+ " WHERE  "
							+ " PRODUCT_CD = (SELECT C.PRODUCT_CD FROM COURSE C WHERE C.COURSE_ID=:courseID) "
							+ " AND "
							+ " ( "
							+ " (COURSE_ID = 400) or "
							+ " (COURSE_ID>=405 AND COURSE_ID<=408) or "
							+ " (COURSE_ID = 409) or  "
							+ " (COURSE_ID>=414 AND COURSE_ID<=417) " + " ) ");
			query.setParameter("courseID", courseID);
			Iterator its = query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				TrainingScheduleList ts = new TrainingScheduleList();
				if (oj[0] != null)
					ts.setStartDate((Date) oj[0]);
				if (oj[1] != null)
					ts.setCourseID(oj[1].toString());
				ls.add(ts);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("getSPFTrainingScheduleListPHR Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<TrainingScheduleList> getSPFTrainingScheduleList(
			String courseID) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<TrainingScheduleList> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT START_DATE STARTDATE, COURSE_ID COURSEID "
							+ " FROM COURSE "
							+ " WHERE COURSE_ID = :courseID "
							+ " union "
							+ " SELECT START_DATE STARTDATE, COURSE_ID COURSEID "
							+ " FROM COURSE "
							+ " WHERE  "
							+ " PRODUCT_CD = (SELECT C.PRODUCT_CD FROM COURSE C WHERE C.COURSE_ID= :courseID ) "
							+ " AND "
							+ " ( "
							+ " (COURSE_ID>=400 AND COURSE_ID<=404) or "
							+ " (COURSE_ID>=409 AND COURSE_ID<=413) " + " ) ");
			query.setParameter("courseID", courseID);
			Iterator its = query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				TrainingScheduleList ts = new TrainingScheduleList();
				if (oj[0] != null)
					ts.setStartDate((Date) oj[0]);
				if (oj[1] != null)
					ts.setCourseID(oj[1].toString());
				ls.add(ts);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("getSPFTrainingScheduleList Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public int insertAuditLogTrainingScheduleChange(String userId,
			String emplid, String oldCourseID, String newCourseID) {

		Session session = HibernateUtils.getHibernateSession();
		int audit = 0;
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("INSERT INTO TR_WEBSITE_AUDIT (AUDIT_ID,USER_ID,ACTION,FILTER_PIE,FILTER_SLICE,ACTION_DATE,PAGE_NAME) "
							+ "  VALUES ((SELECT MAX(AUDIT_ID)+1 FROM TR_WEBSITE_AUDIT),:user_emplid,'Training Schedule Change',CONCAT(CONCAT(CONCAT('COURSE_ID=',:oldCourseID),'to'),:newCourseID),CONCAT('EMPLID=',:emplid),SYSDATE,'Training Schedule Change')");
			query.setParameter("user_emplid", userId);
			query.setParameter("oldCourseID", oldCourseID);
			query.setParameter("newCourseID", newCourseID);
			query.setParameter("emplid", emplid);
			audit = query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("insertAuditLogTrainingScheduleChange Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return audit;
	}

	public String getTeam(String emplId) {

		Session session = HibernateUtils.getHibernateSession();
		String team = new String();
		try {
			String sect = "";
			Query query = session
					.createSQLQuery("select team_cd from v_realignment where emplid = :emplId");
			query.setParameter("emplId", emplId);
			List lbl = query.list();
			Iterator it = lbl.iterator();
			while (it.hasNext()) {
				if (it.next() != null) {
					Object field = it.next();
					if (field != null)
						sect = field.toString();
				}
				team = sect;
			}

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSection Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return team;
	}

	public Iterator<PDFHomeStudyStatus> getVRSStatus(String emplid) {

		Session session = HibernateUtils.getHibernateSession();
		Iterator<PDFHomeStudyStatus> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT TRT_COURSE_NAME PRODUCTDESC "
							+ "  ,COURSE_CODE PEDAGOGUEEXAM "
							+ "  ,SCORE SCORE "
							+ "  ,DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') STATUS "
							+ "  ,EXAM_TAKEN_DATE COMPLETIONDATE "
							+ "  FROM V_VISTASPIRIVA_DATA     "
							+ "  WHERE EMPLID = :emplid "
							+ "  ORDER BY TRT_COURSE_NAME ");
			query.setParameter("emplid", emplid);
			Iterator its = query.list().iterator();
			List<PDFHomeStudyStatus> ls = new ArrayList<PDFHomeStudyStatus>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				PDFHomeStudyStatus pp = new PDFHomeStudyStatus();
				if (oj[0] != null)
					pp.setProductDesc(oj[0].toString());
				if (oj[1] != null)
					pp.setPedagogueExam(oj[1].toString());
				if (oj[2] != null)
					pp.setScore(oj[2].toString());
				if (oj[3] != null)
					pp.setStatus(oj[3].toString());
				if (oj[4] != null)
					pp.setCompletionDate((Date) oj[4]);
				ls.add(pp);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getVRSStatus Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public String getVRSAttendance(String emplid, String mode) {

		Session session = HibernateUtils.getHibernateSession();
		String present = null;
		try {
			Query query = session
					.createSQLQuery(" SELECT YES_NO FROM SPIRIVA_ATTENDANCE WHERE EMPLID = :emplid AND TYPE = :mode");
			query.setParameter("emplid", emplid);
			query.setParameter("mode", mode);
			present = (String) query.uniqueResult();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getVRSAttendance Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return present;
	}

	public Sce[] getSceByArea(String productCode, String area) {
		Sce[] sc = new Sce[200];
		Session session = HibernateUtils.getHibernateSession();

		try {
			Query query = session
					.createSQLQuery("select distinct fe.emplid as emplid, decode(sce.overall_rating,'DC','Demonstrated Competance','NI','Needs Improvement','UN','Unacceptable','Null' ) as rating "
							+ " from  v_training_required ep, v_new_field_employee fe, sce_fft sce where fe.emplid = ep.emplid "
							+ "	and ep.emplid = sce.emplid(+) and ep.product_cd = sce.product_cd(+) and ep.PRODUCT_CD= :productCode "
							+ "	and fe.area_cd = :area ");
			query.setParameter("productCode", productCode);
			query.setParameter("area", area);
			List sceArea = query.list();
			List<Sce> sceList = new ArrayList<Sce>();
			Iterator it = sceArea.iterator();
			while (it.hasNext()) {
				Object[] scAr = (Object[]) it.next();
				Sce scarea = new Sce();
				if (scAr[0] != null)
					scarea.setEmplid(scAr[0].toString());
				if (scAr[1] != null)
					scarea.setRating(scAr[1].toString());
				sceList.add(scarea);
			}
			sc = sceList.toArray(new Sce[sceList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSceByArea Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return sc;
	}

	public Sce[] getSceByDistrict(String productCode, String area,
			String region, String district) {
		Sce[] sc = new Sce[200];
		Session session = HibernateUtils.getHibernateSession();

		try {
			Query query = session
					.createSQLQuery("select distinct fe.emplid as emplid, decode(sce.overall_rating,'DC','Demonstrated Competance','NI','Needs Improvement','UN','Unacceptable','Null' ) as rating "
							+ " from v_training_required ep, v_new_field_employee fe, sce_fft sce "
							+ "  where fe.emplid = ep.emplid and ep.emplid = sce.emplid(+) and ep.product_cd = sce.product_cd(+) "
							+ "	and ep.PRODUCT_CD= :productCode and fe.region_cd= :region and fe.district_id= :district "
							+ "	and fe.area_cd = :area ");
			query.setParameter("productCode", productCode);
			query.setParameter("region", region);
			query.setParameter("district", district);
			query.setParameter("area", area);
			List sceDist = query.list();
			List<Sce> sceList = new ArrayList<Sce>();
			Iterator it = sceDist.iterator();
			while (it.hasNext()) {
				Object[] scDs = (Object[]) it.next();
				Sce scarea = new Sce();
				if (scDs[0] != null)
					scarea.setEmplid(scDs[0].toString());
				if (scDs[1] != null)
					scarea.setRating(scDs[1].toString());
				sceList.add(scarea);
			}
			sc = sceList.toArray(new Sce[sceList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSceByDistrict Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return sc;
	}

	public Sce[] getSceByRegion(String productCode, String area, String region) {
		Sce[] sc = new Sce[200];
		Session session = HibernateUtils.getHibernateSession();

		try {
			Query query = session
					.createSQLQuery("select distinct fe.emplid as emplid, decode(sce.overall_rating,'DC','Demonstrated Competance','NI','Needs Improvement','UN','Unacceptable','Null' ) as rating "
							+ " from v_training_required ep,v_new_field_employee fe,sce_fft sce "
							+ " where fe.emplid = ep.emplid and ep.emplid = sce.emplid(+) and ep.product_cd = sce.product_cd(+) and ep.PRODUCT_CD= :productCode "
							+ " and fe.region_cd= :region and fe.area_cd = :area ");
			query.setParameter("productCode", productCode);
			query.setParameter("region", region);
			query.setParameter("area", area);
			List sceDist = query.list();
			List<Sce> sceList = new ArrayList<Sce>();
			Iterator it = sceDist.iterator();
			while (it.hasNext()) {
				Object[] scReg = (Object[]) it.next();
				Sce scarea = new Sce();
				if (scReg[0] != null)
					scarea.setEmplid(scReg[0].toString());
				if (scReg[1] != null)
					scarea.setRating(scReg[1].toString());
				sceList.add(scarea);
			}
			sc = sceList.toArray(new Sce[sceList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSceByRegion Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return sc;
	}

	public Sce[] getSceByProduct(String productCode) {
		Sce[] sc = new Sce[200];
		Session session = HibernateUtils.getHibernateSession();

		try {
			Query query = session
					.createSQLQuery(" select distinct fe.emplid as emplid,decode(sce.overall_rating,'DC','Demonstrated Competance','NI','Needs Improvement','UN','Unacceptable','Null' ) as rating "
							+ " from v_training_required ep,v_new_field_employee fe,sce_fft sce "
							+ " where fe.emplid = ep.emplid and ep.emplid = sce.emplid(+) and ep.product_cd = sce.product_cd(+) and ep.PRODUCT_CD= :productCode");
			query.setParameter("productCode", productCode);
			List sceDist = query.list();
			List<Sce> sceList = new ArrayList<Sce>();
			Iterator it = sceDist.iterator();
			while (it.hasNext()) {
				Object[] scReg = (Object[]) it.next();
				Sce scarea = new Sce();
				if (scReg[0] != null)
					scarea.setEmplid(scReg[0].toString());
				if (scReg[1] != null)
					scarea.setRating(scReg[1].toString());
				sceList.add(scarea);
			}
			sc = sceList.toArray(new Sce[sceList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSceByRegion Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return sc;
	}

	public String[] getSection() {
		Session session = HibernateUtils.getHibernateSession();
		String[] section = new String[200];
		try {
			Query query = session
					.createSQLQuery("SELECT TRAINING_REPORT_LABEL FROM TRAINING_REPORT WHERE PARENT IS NULL order by sort_order asc ");
			List lbl = query.list();
			List<String> scts = new ArrayList<String>();
			Iterator it = lbl.iterator();
			while (it.hasNext()) {
				Object[] sect = (Object[]) it.next();
				String sct = new String();
				if (sect[0] != null)
					sct = sect[0].toString();
				scts.add(sct);
			}
			section = scts.toArray(new String[scts.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getSection Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return section;
	}

	public TeamBean[] getTEAMBYCLUSTER(String cluster) {
		Session session = HibernateUtils.getHibernateSession();
		TeamBean[] teamsBeans = new TeamBean[200];
		try {
			Query query = session
					.createSQLQuery(" select distinct new_team_cd teamCd, team_cd  teamDesc from v_new_field_employee ft where ft.cluster_cd = :cluster order by teamdesc");
			query.setParameter("cluster", cluster);
			List saleLst = query.list();
			List<TeamBean> beanList = new ArrayList<TeamBean>();
			Iterator it = saleLst.iterator();
			while (it.hasNext()) {
				TeamBean bean = new TeamBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					bean.setTeamCd(field[0].toString());
				if (field[1] != null)
					bean.setTeamDesc(field[1].toString());
				beanList.add(bean);
			}
			teamsBeans = beanList.toArray(new TeamBean[beanList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getTEAMBYCLUSTER Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return teamsBeans;

	}

	public Employee getUserByEmployeeId(String employeeId) {
		Session session = HibernateUtils.getHibernateSession();
		Employee employee = new Employee();
		try {
			Query query = session.createSQLQuery("select "
					+ "		emplid as emplId, " + "		area_cd as areaCd, "
					+ "		area_desc as areaDesc, " + "		region_cd as regionCd, "
					+ "		region_desc as regionDesc, "
					+ "		district_id as districtId, "
					+ "		district_desc as districtDesc, "
					+ "		promotion_date as promoDate,  "
					+ "		effective_hire_date as hireDate, "
					+ "		sex as gender, " + "		email_address as email,  "
					+ "		reports_to_emplid as reportsToEmplid, "
					+ "		territory_id as territoryId, "
					+ "		territory_role_cd as role, "
					+ "		team_cd as teamCode, "
					+ "		cluster_cd as clusterCode, "
					+ "		last_name as lastName, "
					+ "		first_name as firstName, "
					+ "		middle_name as middleName, "
					+ "		preferred_name as preferredName " + " from  "
					+ "		v_new_field_employee " + " where "
					+ "		emplid = :employeeId");
			query.setParameter("employeeId", employeeId);
			List empLst = query.list();

			Iterator it = empLst.iterator();
			while (it.hasNext()) {
				Employee bean = new Employee();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					bean.setEmplId(field[0].toString());
				if (field[1] != null)
					bean.setAreaCd(field[1].toString());
				if (field[2] != null)
					bean.setAreaDesc(field[2].toString());
				if (field[3] != null)
					bean.setRegionCd(field[3].toString());
				if (field[4] != null)
					bean.setRegionDesc(field[4].toString());
				if (field[5] != null)
					bean.setDistrictId(field[5].toString());
				if (field[6] != null)
					bean.setDistrictDesc(field[6].toString());
				if (field[7] != null)
					bean.setPromoDate((Date) field[7]);
				if (field[8] != null)
					bean.setHireDate((Date) field[8]);
				if (field[9] != null)
					bean.setGender(field[9].toString());
				if (field[10] != null)
					bean.setEmail(field[10].toString());
				if (field[11] != null)
					bean.setReportsToEmplid(field[11].toString());
				if (field[12] != null)
					bean.setTerritoryId(field[12].toString());
				if (field[13] != null)
					bean.setRole(field[13].toString());
				if (field[14] != null)
					bean.setTeamCode(field[14].toString());
				if (field[15] != null)
					bean.setClusterCode(field[15].toString());
				if (field[16] != null)
					bean.setLastName(field[16].toString());
				if (field[17] != null)
					bean.setFirstName(field[17].toString());
				if (field[18] != null)
					bean.setMiddleName(field[18].toString());
				if (field[19] != null)
					bean.setPreferredName(field[19].toString());
				employee = bean;
			}
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getUserByEmployeeId Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return employee;

	}

	public UserAccess getUserAccessByEmplid(String employeeId) {
		Session session = HibernateUtils.getHibernateSession();
		UserAccess userAccess = new UserAccess();
		try {
			Query query = session.createSQLQuery("select "
					+ "		emplid as emplId, " + "		user_type as userType, "
					+ "		nt_id as ntId, " + "		nt_domain as ntDomain, "
					+ "		email as email, " + "		fname as fname, "
					+ "		lname as lname " + " from  " + "		user_access "
					+ " where " + "		emplid = :employeeId");
			query.setParameter("employeeId", employeeId);
			List usAcc = query.list();

			Iterator it = usAcc.iterator();
			while (it.hasNext()) {
				UserAccess bean = new UserAccess();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					bean.setEmplid(field[0].toString());
				if (field[1] != null)
					bean.setUserType(field[1].toString());
				if (field[2] != null)
					bean.setNtId(field[2].toString());
				if (field[3] != null)
					bean.setNtDomain(field[3].toString());
				if (field[4] != null)
					bean.setEmail(field[4].toString());
				if (field[5] != null)
					bean.setFname(field[5].toString());
				if (field[6] != null)
					bean.setLname(field[6].toString());
				userAccess = bean;
			}
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getUserAccessByEmplid Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return userAccess;
	}

	public Iterator<TrainingScheduleList> getTrainingScheduleListOld(
			String courseID) {
		Session session = HibernateUtils.getHibernateSession();
		Iterator<TrainingScheduleList> it = null;
		try {
			Query query = session
					.createSQLQuery("SELECT START_DATE STARTDATE, COURSE_ID COURSEID "
							+ "  FROM COURSE "
							+ "  WHERE COURSE_ID >299 "
							+ "  AND PRODUCT_CD = (SELECT C.PRODUCT_CD FROM COURSE C WHERE C.COURSE_ID=:courseID)   ");
			query.setParameter("courseID", courseID);
			Iterator its = query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				TrainingScheduleList ts = new TrainingScheduleList();
				if (oj[0] != null)
					ts.setStartDate((Date) oj[0]);
				if (oj[1] != null)
					ts.setCourseID(oj[1].toString());
				ls.add(ts);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("getTrainingScheduleListOld Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public Iterator<TrainingScheduleList> getTrainingScheduleListPHROld(
			String courseID) {
		Session session = HibernateUtils.getHibernateSession();
		Iterator<TrainingScheduleList> it = null;
		try {
			Query query = session
					.createSQLQuery(" SELECT START_DATE STARTDATE, COURSE_ID COURSEID "
							+ "  FROM COURSE "
							+ "  WHERE COURSE_ID >299 AND ((COURSE_ID>=315 AND COURSE_ID<=319) OR (COURSE_ID>=309 AND COURSE_ID<=311)) "
							+ "  AND PRODUCT_CD = (SELECT C.PRODUCT_CD FROM COURSE C WHERE C.COURSE_ID=:courseID) ");
			query.setParameter("courseID", courseID);
			Iterator its = query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] oj = (Object[]) its.next();
				TrainingScheduleList ts = new TrainingScheduleList();
				if (oj[0] != null)
					ts.setStartDate((Date) oj[0]);
				if (oj[1] != null)
					ts.setCourseID(oj[1].toString());
				ls.add(ts);
			}
			it = ls.iterator();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("getTrainingScheduleListPHROld Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return it;
	}

	public void deleteUser(String userId) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("delete from user_access where user_id = :userId");
			query.setParameter("userId", userId);
			int r = query.executeUpdate();
			System.out.println("Rows updated deleteUser():" + r);

			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("deleteUser Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public TeamBean[] getAllPOATEAM() {

		Session session = HibernateUtils.getHibernateSession();
		TeamBean[] teamsBeans = new TeamBean[200];
		try {
			Query query = session
					.createSQLQuery("select distinct vp.team_cd teamCd, team_short_desc  teamDesc"
							+ "  from v_powers_midpoa1_data vp , MV_TEAM_CODE_MAP ft where vp.team_cd=ft.team_cd order by teamdesc");
			List saleLst = query.list();
			List<TeamBean> beanList = new ArrayList<TeamBean>();
			Iterator it = saleLst.iterator();
			while (it.hasNext()) {
				TeamBean bean = new TeamBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					bean.setTeamCd(field[0].toString());
				if (field[1] != null)
					bean.setTeamDesc(field[1].toString());
				beanList.add(bean);
			}
			teamsBeans = beanList.toArray(new TeamBean[beanList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getAllPOATEAM Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return teamsBeans;
	}

	public ReportTypeBean[] getAllReportTypes() {

		Session session = HibernateUtils.getHibernateSession();
		ReportTypeBean[] reportBean = null;
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT Report_type reportType FROM PHASE_EVALUATION_MAPPING");
			List sList = query.list();
			List<ReportTypeBean> gList = new ArrayList<ReportTypeBean>();
			Iterator it = sList.iterator();
			while (it.hasNext()) {
				ReportTypeBean course = new ReportTypeBean();
				String field = it.next().toString();
				if (field != null)
					course.setReportType(field);
				gList.add(course);
			}
			reportBean = gList.toArray(new ReportTypeBean[gList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getAllReportTypes Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return reportBean;
	}

	public TeamBean[] getAllSPFTEAM() {
		Session session = HibernateUtils.getHibernateSession();
		TeamBean[] teamsBeans = new TeamBean[200];
		try {
			Query query = session
					.createSQLQuery("select distinct team_cd teamCd,team_desc teamDesc from fft_product_assignment"
							+ " where cluster_cd = 'ST' and role_cd in ('TSR','PHR')");
			List saleLst = query.list();
			List<TeamBean> beanList = new ArrayList<TeamBean>();
			Iterator it = saleLst.iterator();
			while (it.hasNext()) {
				TeamBean bean = new TeamBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					bean.setTeamCd(field[0].toString());
				if (field[1] != null)
					bean.setTeamDesc(field[1].toString());
				beanList.add(bean);
			}
			teamsBeans = beanList.toArray(new TeamBean[beanList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getAllSPFTEAM Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return teamsBeans;
	}

	public int getMaxSort() {

		Session session = HibernateUtils.getHibernateSession();
		Integer maxSor = 0;
		try {
			Query query = session
					.createSQLQuery("select max(sort_order) from training_report where parent is null");
			List list = query.list();
			System.out.println("size getMaxSort()" + list.size());
			maxSor = (list.size() > 0) ? ((BigDecimal) list.get(0)).intValue()
					: null;

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("SalesOrgBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return maxSor;
	}

	public void insertTrainingReports(String sectionName, int active, int sort,
			String delflag, String minflag) {

		Session session = HibernateUtils.getHibernateSession();
		try {

			String updateUserSql = "INSERT INTO TRAINING_REPORT"
					+ " (Training_report_id,training_report_label,active,sort_order,delete_flag,minimize)"
					+ " VALUES(TRAINING_REPORT_ID_SEQ.nextval,:label,:active,:sort,:delflag,:minflag)";

			Transaction tx = session.beginTransaction();
			Query query = session.createSQLQuery(updateUserSql);

			query.setParameter("label", sectionName);
			query.setParameter("active", active);
			query.setParameter("sort", sort);
			query.setParameter("delflag", delflag);
			query.setParameter("minflag", minflag);

			int result = query.executeUpdate();
			System.out.println("Rows affected in insertTrainingReports: "
					+ result);

			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("insertUserAccess Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public GAProduct[] getGAPproductsFromSandBox() {

		Session session = HibernateUtils.getHibernateSession();
		GAProduct[] gaProduct = null;
		try {
			
			/*Code change done by Sanjeev as a remediation of sandbox , replaced v_s4_sales_position_product with  V_SALES_POS_PROD_CURR_F as sandbox view will not be available in future
			 * 22 september 2015
			 * 
			 *  */		
			
			/*Query query = session
					.createSQLQuery("select distinct PRODUCT_DESC as product from v_s4_sales_position_product@sandbox.pfizer.com where PRODUCT_WEIGHT<>0 order by product_desc asc");*/
			
			
			Query query = session
			.createSQLQuery("SELECT DISTINCT PRODUCT_DESC FROM V_SALES_POS_PROD_CURR_F ORDER BY PRODUCT_DESC ASC");
			
			List sList = query.list();
			List<GAProduct> gList = new ArrayList<GAProduct>();
			Iterator it = sList.iterator();
			while (it.hasNext()) {
				GAProduct product = new GAProduct();
				String field = it.next().toString();
				if (field != null)
					product.setProduct(field);
				gList.add(product);
			}
			gaProduct = gList.toArray(new GAProduct[gList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out
					.println("getGAPproductsFromSandBox Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return gaProduct;
	}

	public GAProdCourse[] getProdCourseMappingNew() {

		Session session = HibernateUtils.getHibernateSession();
		GAProdCourse[] gaCourse = null;
		try {
			Query query = session
					.createSQLQuery("Select product as productCode, course_code as courseCode, completion, registration from gap_rpt_ffra_codes"
							+ " where product is not null and course_code is not null order by product");
			List sList = query.list();
			List<GAProdCourse> gList = new ArrayList<GAProdCourse>();
			Iterator it = sList.iterator();
			while (it.hasNext()) {
				GAProdCourse course = new GAProdCourse();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					course.setProductCode(field[0].toString());
				if (field[1] != null)
					course.setCourseCode(field[1].toString());
				if (field[2] != null)
					course.setCompletion(field[2].toString());
				if (field[3] != null)
					course.setRegistration(field[3].toString());
				gList.add(course);
			}
			gaCourse = gList.toArray(new GAProdCourse[gList.size()]);
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getProdCourseMappingNew Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return gaCourse;
	}

	public String getTeamDescription(String team) {

		Session session = HibernateUtils.getHibernateSession();
		List saleLst = new ArrayList();
		try {
			Query query = session
					.createSQLQuery("select team_short_desc  from MV_TEAM_CODE_MAP where upper(team_cd)=:team");
			query.setParameter("team", team);

			saleLst = query.list();

		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("getTeamDescription Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return (saleLst.size() > 0) ? (String) saleLst.get(0) : null;
	}

	public void removeGapRptProductCourseMapping(String selectedProduct,
			String code) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE FROM gap_rpt_ffra_codes WHERE PRODUCT = :product AND COURSE_CODE = :courseCode");
			query.setParameter("product", selectedProduct);
			query.setParameter("courseCode", code);
			int r = query.executeUpdate();
			System.out
					.println("rows updated removeGapRptProductCourseMapping()"
							+ r);
			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("deleteUser Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void saveGapRptProdCourseMapping(String selectedProduct,
			String code, String completionCheck, String registrationCheck) {

		Session session = HibernateUtils.getHibernateSession();
		try {

			String updateUserSql = "INSERT INTO gap_rpt_ffra_codes(PRODUCT,COURSE_CODE,COMPLETION,REGISTRATION,ACTIVE) VALUES (:product,:code,:completion,:registration,'Y')";
			Transaction tx = session.beginTransaction();
			Query query = session.createSQLQuery(updateUserSql);
			System.out.println("Inside saveGapRptProdCourseMapping");
			query.setParameter("product", selectedProduct);
			query.setParameter("code", code);
			query.setParameter("completion", completionCheck);
			query.setParameter("registration", registrationCheck);
			int result = query.executeUpdate();
			System.out.println("Rows affected in saveGapRptProdCourseMapping: "
					+ result);

			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("insertUserAccess Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public int getConfigId() {
		Session session = HibernateUtils.getHibernateSession();
		int configId = 0;

		Query query = session
				.createSQLQuery("SELECT TRAINING_PATH_ID_SEQ.nextval FROM DUAL");
		if (query.list().get(0) != null)
			configId = Integer.parseInt(query.list().get(0).toString());
		return configId;
	}

	public FutureAllignmentBuDataBean[] getBuForLaunchMeeting() {
		Session session = HibernateUtils.getHibernateSession();
		FutureAllignmentBuDataBean[] futureAllignmentBuDataBean = new FutureAllignmentBuDataBean[200];
		Query query = session
				.createSQLQuery("SELECT DISTINCT BU as BU from MV_FIELD_EMPLOYEE_RBU where BU is not null order by bu asc");

		List<FutureAllignmentBuDataBean> list = query.list();
		Iterator it = list.iterator();
		List<FutureAllignmentBuDataBean> tempList = new ArrayList<FutureAllignmentBuDataBean>();

		while (it.hasNext()) {
			FutureAllignmentBuDataBean futureAllignmentBuData = new FutureAllignmentBuDataBean();
			Object field = (Object) it.next();
			if (field != null)
				futureAllignmentBuData.setBu(field.toString());

			tempList.add(futureAllignmentBuData);
		}

		futureAllignmentBuDataBean = tempList
				.toArray(new FutureAllignmentBuDataBean[(tempList.size())]);
		return futureAllignmentBuDataBean;
	}

	public FutureAllignmentRBUDataBean[] getRbuForLaunchMeeting() {
		Session session = HibernateUtils.getHibernateSession();
		FutureAllignmentRBUDataBean[] futureAllignmentRBUDataBean = new FutureAllignmentRBUDataBean[200];
		Query query = session
				.createSQLQuery("SELECT DISTINCT SALES_GROUP as RBU from MV_FIELD_EMPLOYEE_RBU "
						+ "where SALES_GROUP is not null and GROUP_CD <> 'CGC'  order by SALES_GROUP asc");

		List<FutureAllignmentRBUDataBean> list = query.list();
		Iterator it = list.iterator();
		List<FutureAllignmentRBUDataBean> tempList = new ArrayList<FutureAllignmentRBUDataBean>();

		while (it.hasNext()) {
			FutureAllignmentRBUDataBean futureAllignmentRBUData = new FutureAllignmentRBUDataBean();
			Object field = (Object) it.next();
			if (field != null)
				futureAllignmentRBUData.setRbu(field.toString());

			tempList.add(futureAllignmentRBUData);
		}

		futureAllignmentRBUDataBean = tempList
				.toArray(new FutureAllignmentRBUDataBean[(tempList.size())]);
		return futureAllignmentRBUDataBean;
	}

	public FutureAllignmentRBUDataBean[] getRbuForRBUForLaunchMeeting(String bu) {
		Session session = HibernateUtils.getHibernateSession();
		FutureAllignmentRBUDataBean[] futureAllignmentRBUDataBean = new FutureAllignmentRBUDataBean[200];
		Query query = session
				.createSQLQuery("SELECT DISTINCT  SALES_GROUP as RBU from MV_FIELD_EMPLOYEE_RBU  "
						+ "where bu = :bu and SALES_GROUP is not null and GROUP_CD <> 'CGC'   order by SALES_GROUP asc");

		query.setParameter("bu", bu);
		List<FutureAllignmentRBUDataBean> list = query.list();
		Iterator it = list.iterator();
		List<FutureAllignmentRBUDataBean> tempList = new ArrayList<FutureAllignmentRBUDataBean>();

		while (it.hasNext()) {
			FutureAllignmentRBUDataBean futureAllignmentRBUData = new FutureAllignmentRBUDataBean();
			Object field = (Object) it.next();
			if (field != null)
				futureAllignmentRBUData.setRbu(field.toString());

			tempList.add(futureAllignmentRBUData);
		}

		futureAllignmentRBUDataBean = tempList
				.toArray(new FutureAllignmentRBUDataBean[(tempList.size())]);
		return futureAllignmentRBUDataBean;
	}

	public TeamBean[] getAllMSEPITEAM() {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		TeamBean[] teamBean = new TeamBean[200];
		TeamBean team = new TeamBean();

		Query query = session
				.createSQLQuery("select distinct team_cd , team_desc "
						+ "from mv_team_code_map  WHERE team_cd IN ('WC','PWCM','WCP') ");

		List list = query.list();
		Iterator it = list.iterator();
		List<TeamBean> tempList = new ArrayList<TeamBean>();
		while (it.hasNext()) {
			TeamBean bean = new TeamBean();
			Object[] field = (Object[]) it.next();
			if (field[0] != null)
				bean.setTeamCd(field[0].toString());
			if (field[1] != null)
				bean.setTeamDesc(field[1].toString());
			tempList.add(bean);
		}

		teamBean = tempList.toArray(new TeamBean[(tempList.size())]);
		return teamBean;
	}

	public int deleteSCECourse(String exCourseCode) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();

		try {
			Query query = session
					.createSQLQuery("DELETE P2L_SCE_SPECIAL_CODES WHERE COURSE_CODE=:idDel ");

			query.setParameter("idDel", exCourseCode);
			query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("deleteSCECourse Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return 0;

	}

	public int insertSCECourse(String courseCode, String eventID) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createSQLQuery("INSERT INTO P2L_SCE_SPECIAL_CODES(COURSE_CODE,SCE_EVENT_ID) "
							+ "VALUES (:course,:event)");

			query.setParameter("course", courseCode);
			query.setParameter("event", eventID);
			query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("insertSCECourse Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return 0;
	}

	public SCEList[] getListSCE() {
		Session session = HibernateUtils.getHibernateSession();
		SCEList[] sceList = new SCEList[200];
		Query query = session
				.createSQLQuery("SELECT COURSE_CODE CODE,SCE_EVENT_ID EVENTID"
						+ " FROM P2L_SCE_SPECIAL_CODES ");

		List<SCEList> list = query.list();
		Iterator it = list.iterator();
		List<SCEList> tempList = new ArrayList<SCEList>();

		while (it.hasNext()) {
			SCEList sce = new SCEList();
			Object[] field = (Object[]) it.next();
			if (field[0] != null)
				sce.setCode(field[0].toString());
			if (field[1] != null)
				sce.setEventID(field[1].toString());
			tempList.add(sce);
		}

		sceList = tempList.toArray(new SCEList[(tempList.size())]);
		return sceList;
	}

	public EmployeeInfo getReportToInfo(String reportToEmplID) {
		Session session = HibernateUtils.getHibernateSession();
		EmployeeInfo employeeInfo = new EmployeeInfo();
		EmployeeInfo empl = new EmployeeInfo();

		Query query = session
				.createSQLQuery("SELECT EMAIL_ADDRESS REPORTTOEMAIL,LAST_NAME REPORTTOLASTNAME,"
						+ "PREFERRED_NAME REPORTTOPREFERREDNAME "
						+ "FROM V_NEW_FIELD_EMPLOYEE WHERE EMPLID= :emplid");

		query.setParameter("emplid", reportToEmplID);
		List result = query.list();
		List<EmployeeInfo> tempList = new ArrayList<EmployeeInfo>();

		Iterator it = result.iterator();

		while (it.hasNext()) {
			Object[] field = (Object[]) it.next();
			if (field[0] != null)
				empl.setReportToEmail(field[0].toString());
			if (field[1] != null)
				empl.setReportToLastName(field[1].toString());
			if (field[2] != null)
				empl.setReportToPreferredName(field[2].toString());
		}
		employeeInfo = empl;

		return employeeInfo;
	}

	public EmployeeInfo getEmployeeInfoGSM(String emplid) throws ParseException {
		Session session = HibernateUtils.getHibernateSession();
		EmployeeInfo employeeInfo = new EmployeeInfo();
		EmployeeInfo empl = new EmployeeInfo();

		Query query = session
				.createSQLQuery("SELECT E.EMPLID EMPLID, E.PROMOTION_DATE PROMOTIONDATE, "
						+ "E.EFFECTIVE_HIRE_DATE HIREDATE, E.SEX GENDER, E.EMAIL_ADDRESS EMAIL, "
						+ "E.REPORTS_TO_EMPLID REPORTTOEMPLID, "
						+ "DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') STATUS , "
						+ "E.AREA_CD AREACD,E.AREA_DESC AREADESC, E.REGION_CD REGIONCD,E.REGION_DESC REGIONDESC,"
						+ "E.DISTRICT_ID DISTRICTID,E.DISTRICT_DESC DISTRICTDESC, E.TERRITORY_ID TERRITORYID, "
						+ "E.TERRITORY_ROLE_CD TERRITORYROLE,E.TEAM_CD TEAMCD, E.CLUSTER_CD CLUSTERCD, "
						+ "E.LAST_NAME LASTNAME,E.MIDDLE_NAME MIDDLENAME,E.PREFERRED_NAME PREFERREDNAME "
						+ "FROM V_NEW_FIELD_EMPLOYEE E WHERE E.EMPLID=:emplid	");

		query.setParameter("emplid", emplid);
		List result = query.list();
		List<EmployeeInfo> tempList = new ArrayList<EmployeeInfo>();

		Iterator it = result.iterator();

		while (it.hasNext()) {
			Object[] field = (Object[]) it.next();
			if (field[0] != null)
				empl.setEmplID(field[0].toString());
			if (field[1] != null)
				empl.setPromotionDate((Date) field[1]);
			if (field[2] != null)
				empl.setHireDate((Date) field[2]);
			if (field[3] != null)
				empl.setGender(field[3].toString());
			if (field[4] != null)
				empl.setEmail(field[4].toString());
			if (field[5] != null)
				empl.setReportToEmplID(field[5].toString());
			if (field[6] != null)
				empl.setStatus(field[6].toString());
			if (field[7] != null)
				empl.setAreaCD(field[7].toString());
			if (field[8] != null)
				empl.setAreaDesc(field[8].toString());
			if (field[9] != null)
				empl.setRegionCD(field[9].toString());
			if (field[10] != null)
				empl.setRegionDesc(field[10].toString());
			if (field[11] != null)
				empl.setDistrictID(field[11].toString());
			if (field[12] != null)
				empl.setDistrictDesc(field[12].toString());
			if (field[13] != null)
				empl.setTerritoryID(field[13].toString());
			if (field[14] != null)
				empl.setTerritoryRole(field[14].toString());
			if (field[15] != null)
				empl.setTeamCD(field[15].toString());
			if (field[16] != null)
				empl.setClusterCD(field[16].toString());
			if (field[17] != null)
				empl.setLastName(field[17].toString());
			if (field[18] != null)
				empl.setMiddleName(field[18].toString());
			if (field[19] != null)
				empl.setPreferredName(field[19].toString());
		}
		employeeInfo = empl;

		return employeeInfo;
	}

	public int updateVRSAttendance(String emplid, String userId, String result,
			String mode) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		int n = 0;
		try {
			Query query = session
					.createSQLQuery("UPDATE SPIRIVA_ATTENDANCE "
							+ " SET BY_USER=:userId , DATE_TIME=SYSDATE, YES_NO=:result: "
							+ " WHERE TYPE =:mode AND EMPLID=:emplid ");

			query.setParameter("emplid", emplid);
			query.setParameter("userId", userId);
			query.setParameter("result", result);
			query.setParameter("mode", mode);

			n = query.executeUpdate();
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return n;
	}

	public int updateRBUClass(String emplid, String oldCourseID,
			String newCourseID) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		int n = 0;
		try {
			Query query = session
					.createSQLQuery(" UPDATE COURSE_ASSIGNMENT SET COURSE_ID = :newCourseID"
							+ " WHERE COURSE_ID=:oldCourseID AND TRAINEE_SSN=:emplid ");

			query.setParameter("emplid", emplid);
			query.setParameter("oldCourseID", oldCourseID);
			query.setParameter("newCourseID", newCourseID);

			n = query.executeUpdate();
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return n;
	}

	public int updateMSEPIAttendance(String emplid, String userId,
			String result, String mode) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		int n = 0;
		try {
			Query query = session
					.createSQLQuery(" UPDATE MSEPI_NSM_ATTENDANCE "
							+ " SET BY_USER=:userId , DATE_TIME=SYSDATE, YES_NO=:result "
							+ " WHERE TYPE =:mode AND EMPLID=:emplid");

			query.setParameter("emplid", emplid);
			query.setParameter("userId", userId);
			query.setParameter("result", result);
			query.setParameter("mode", mode);

			n = query.executeUpdate();
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return n;
	}

	public int updateGNSMAttendance(String emplid, String userId,
			String result, String mode) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		int n = 0;
		try {
			Query query = session
					.createSQLQuery(" UPDATE GNSM_ATTENDANCE "
							+ " SET BY_USER=:userId , DATE_TIME=SYSDATE, YES_NO=:result "
							+ " WHERE TYPE =:mode AND EMPLID=:emplid");

			query.setParameter("emplid", emplid);
			query.setParameter("userId", userId);
			query.setParameter("result", result);
			query.setParameter("mode", mode);

			n = query.executeUpdate();
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return n;
	}

	public void updateDeletedCourseAssignment(String emplid, String courseid,
			String gender, String role, String status, String reason) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createSQLQuery("insert into deleted_course_assignment (trainee_ssn, course_id, "
							+ " gender, role, status, operation, time_stamp, last_updated_by, delete_reason)"
							+ " values (:emplid, :courseid, :gender, :role, :status, 'Delete', SYSDATE, :reason)");

			query.setParameter("emplid", emplid);
			query.setParameter("courseid", courseid);
			query.setParameter("gender", gender);
			query.setParameter("role", role);
			query.setParameter("status", status);
			query.setParameter("reason", reason);

			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public int updateCourseList(String emplid, String oldCourseID,
			String newCourseID) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		int n = 0;
		try {
			Query query = session
					.createSQLQuery(" UPDATE COURSE_ASSIGNMENT SET COURSE_ID = :newCourseID "
							+ " WHERE COURSE_ID=:oldCourseID AND TRAINEE_SSN=:emplid ");

			query.setParameter("newCourseID", newCourseID);
			query.setParameter("oldCourseID", oldCourseID);
			query.setParameter("emplid", emplid);

			n = query.executeUpdate();
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return n;
	}

	public void setEmployeeGridSelecteOptFields(String selectedFields) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();

		try {
			Query query = session.createSQLQuery(" UPDATE EMPLOYEE_GRID_CONFIG"
					+ " SET SELECTED_FIELDS = :selectedFields ");

			query.setParameter("selectedFields", selectedFields);

			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void saveProdCourseMapping(String product, String code) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createSQLQuery(" INSERT INTO GAP_REPORT_CODES(PRODUCT,COURSE_CODE,ACTIVE) "
							+ " VALUES (:product,:code,'Y') ");

			query.setParameter("product", product);
			query.setParameter("code", code);

			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
	}

	public Employee[] getEmployeeByProductRegion(String product, String area,
			String region) {
		Session session = HibernateUtils.getHibernateSession();
		Employee[] employee = new Employee[200];
		try {
			Query query = session
					.createSQLQuery("select e.emplid as emplId,e.area_cd as areaCd,"
							+ "e.area_desc as areaDesc,	e.region_cd as regionCd,e.region_desc as regionDesc,"
							+ "e.district_id as districtId,e.district_desc as districtDesc,"
							+ "e.territory_id as territoryId,e.territory_role_cd as role,e.team_cd as teamCode,"
							+ "e.cluster_cd as clusterCode,e.last_name as lastName,e.first_name as firstName,"
							+ "e.middle_name as middleName,e.preferred_name as preferredName "
							+ "from v_new_field_employee e, v_training_required p "
							+ "where e.emplid = p.emplid and e.area_cd = :area and e.region_cd = :region "
							+ "and p.product_cd = :product");

			query.setParameter("product", product);
			query.setParameter("area", area);
			query.setParameter("region", region);

			List lst = query.list();
			Iterator it = lst.iterator();
			List<Employee> tempList = new ArrayList<Employee>();

			while (it.hasNext()) {
				Employee empl = new Employee();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					empl.setEmplId(field[0].toString());
				if (field[1] != null)
					empl.setAreaCd(field[1].toString());
				if (field[2] != null)
					empl.setAreaDesc(field[2].toString());
				if (field[3] != null)
					empl.setRegionCd(field[3].toString());
				if (field[4] != null)
					empl.setRegionDesc(field[4].toString());
				if (field[5] != null)
					empl.setDistrictId(field[5].toString());
				if (field[6] != null)
					empl.setDistrictDesc(field[6].toString());
				if (field[7] != null)
					empl.setTerritoryId(field[7].toString());
				if (field[8] != null)
					empl.setRole(field[8].toString());
				if (field[9] != null)
					empl.setTeamCode(field[9].toString());
				if (field[10] != null)
					empl.setClusterCode(field[10].toString());
				if (field[11] != null)
					empl.setLastName(field[11].toString());
				if (field[12] != null)
					empl.setFirstName(field[12].toString());
				if (field[13] != null)
					empl.setMiddleName(field[13].toString());
				if (field[14] != null)
					empl.setPreferredName(field[14].toString());
				tempList.add(empl);
			}
			employee = tempList.toArray(new Employee[tempList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return employee;
	}

	public Employee[] getEmployeeByRole(String role) {
		Session session = HibernateUtils.getHibernateSession();

		Employee[] employee = new Employee[200];
		try {
			Query query = session
					.createSQLQuery("select emplid as emplId,area_cd as areaCd,"
							+ " area_desc as areaDesc,region_cd as regionCd,region_desc as regionDesc,"
							+ " district_id as districtId,district_desc as districtDesc,"
							+ " promotion_date as promoDate, effective_hire_date as hireDate, "
							+ " sex as gender, email_address as email, reports_to_emplid as reportsToEmplid,"
							+ " territory_id as territoryId,territory_role_cd as role,team_cd as teamCode,"
							+ " cluster_cd as clusterCode,last_name as lastName,first_name as firstName,"
							+ " middle_name as middleName,preferred_name as preferredName "
							+ " from v_new_field_employee where cluster_cd in ('Steere','Pratt','Powers')"
							+ " and territory_role_cd = :role");

			query.setParameter("role", role);

			List lst = query.list();
			Iterator it = lst.iterator();
			List<Employee> tempList = new ArrayList<Employee>();

			while (it.hasNext()) {
				Employee empl = new Employee();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					empl.setEmplId(field[0].toString());
				if (field[1] != null)
					empl.setAreaCd(field[1].toString());
				if (field[2] != null)
					empl.setAreaDesc(field[2].toString());
				if (field[3] != null)
					empl.setRegionCd(field[3].toString());
				if (field[4] != null)
					empl.setRegionDesc(field[4].toString());
				if (field[5] != null)
					empl.setDistrictId(field[5].toString());
				if (field[6] != null)
					empl.setDistrictDesc(field[6].toString());
				if (field[7] != null)
					empl.setPromoDate((Date) field[7]);
				if (field[8] != null)
					empl.setHireDate((Date) field[8]);
				if (field[9] != null)
					empl.setGender(field[9].toString());
				if (field[10] != null)
					empl.setEmail(field[10].toString());
				if (field[11] != null)
					empl.setReportsToEmail(field[11].toString());
				if (field[12] != null)
					empl.setTerritoryId(field[12].toString());
				if (field[13] != null)
					empl.setRole(field[13].toString());
				if (field[14] != null)
					empl.setTeamCode(field[14].toString());
				if (field[15] != null)
					empl.setClusterCode(field[15].toString());
				if (field[16] != null)
					empl.setLastName(field[16].toString());
				if (field[17] != null)
					empl.setFirstName(field[17].toString());
				if (field[18] != null)
					empl.setMiddleName(field[18].toString());
				if (field[19] != null)
					empl.setPreferredName(field[19].toString());
				tempList.add(empl);
			}
			employee = tempList.toArray(new Employee[tempList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return employee;
	}

	public EmployeeInfo getEmployeeFutureInfoRBU(String emplid) {
		Session session = HibernateUtils.getHibernateSession();
		EmployeeInfo employeeInfo = new EmployeeInfo();

		Query query = session
				.createSQLQuery("SELECT E.TERRITORY_ID SALESPOSITIONID,"
						+ " E.TERRITORY_DESC SALESPOSITIONDESC,E.TERRITORY_ROLE_CD  FUTUREROLE,"
						+ "E.BU FUTUREBU,E.RBU_DESC FUTURERBU,E.REPORTS_TO_EMPLID as FUTUREREPORTSTOEMPLID "
						+ " FROM V_RBU_FUTURE_ALIGNMENT E WHERE E.EMPLID=:emplid");

		query.setParameter("emplid", emplid);

		List<EmployeeInfo> list = query.list();
		Iterator it = list.iterator();
		EmployeeInfo lst = new EmployeeInfo();
		while (it.hasNext()) {
			Object[] field = (Object[]) it.next();
			if (field[0] != null)
				lst.setSalesPositionId(field[0].toString());
			if (field[1] != null)
				lst.setSalesPositionDesc(field[1].toString());
			if (field[2] != null)
				lst.setFutureRole(field[2].toString());
			if (field[3] != null)
				lst.setFutureBU(field[3].toString());
			if (field[4] != null)
				lst.setFutureRBU(field[4].toString());
			if (field[5] != null)
				lst.setFutureReportsToEmplID(field[5].toString());
		}
		employeeInfo = lst;
		return employeeInfo;
	}

	public EmployeeGridOptFieldsBean[] getEmployeeGridAllOptFields() {
		Session session = HibernateUtils.getHibernateSession();
		EmployeeGridOptFieldsBean[] employee = new EmployeeGridOptFieldsBean[200];
		try {
			Query query = session
					.createSQLQuery("select COL_NAME dbColumnName,FIELD_NAME fieldName "
							+ " FROM EMPLOYEE_GRID_CONFIG_LOOKUP");

			List lst = query.list();
			Iterator it = lst.iterator();
			List<EmployeeGridOptFieldsBean> tempList = new ArrayList<EmployeeGridOptFieldsBean>();

			while (it.hasNext()) {
				EmployeeGridOptFieldsBean empl = new EmployeeGridOptFieldsBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					empl.setDBColumnName(field[0].toString());
				if (field[1] != null)
					empl.setFieldName(field[1].toString());

				tempList.add(empl);
			}
			employee = tempList.toArray(new EmployeeGridOptFieldsBean[tempList
					.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return employee;
	}

	public String getEmployeeGridSelectedOptFields() {
		Session session = HibernateUtils.getHibernateSession();
		String selectedFields = null;

		try {
			Query query = session
					.createSQLQuery("select SELECTED_FIELDS FROM EMPLOYEE_GRID_CONFIG");
			if (query.list().get(0) != null)
				selectedFields = query.list().get(0).toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return selectedFields;
	}

	public POAChartBean[] getPOAOverallChart() {
		Session session = HibernateUtils.getHibernateSession();
		POAChartBean[] employee = new POAChartBean[200];
		try {
			Query query = session
					.createSQLQuery("SELECT 'Complete' coursestatus, COUNT (DISTINCT emplid) total"
							+ " FROM v_powers_midpoa1_data WHERE OVERALL_STATUS='P' "
							+ " UNION "
							+ " SELECT 'InComplete' coursestatus, COUNT (DISTINCT emplid) total "
							+ " FROM v_powers_midpoa1_data WHERE OVERALL_STATUS = 'I' "
							+ " UNION "
							+ " SELECT 'OnLeave' coursestatus, COUNT (DISTINCT emplid) total "
							+ " FROM v_powers_midpoa1_data "
							+ " WHERE OVERALL_STATUS = 'L'");

			List lst = query.list();
			Iterator it = lst.iterator();
			List<POAChartBean> tempList = new ArrayList<POAChartBean>();

			while (it.hasNext()) {
				POAChartBean empl = new POAChartBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					empl.setCourseStatus(field[0].toString());
				if (field[1] != null)
					empl.setTotal(Integer.parseInt(field[1].toString()));

				tempList.add(empl);
			}
			employee = tempList.toArray(new POAChartBean[tempList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return employee;
	}

	public Iterator<PDFProduct> getPostSPFProduct(String emplid) {
		Session session = HibernateUtils.getHibernateSession();
		Iterator<PDFProduct> pdfIterator = null;
		try {
			Query query = session
					.createSQLQuery("SELECT P.PRODUCT_DESC PRODUCT,RA.TEAM_DESC TEAM "
							+ " FROM V_SPF_PRODUCTS P, V_REALIGNMENT RA "
							+ " WHERE P.EMPLID = RA.EMPLID AND P.EMPLID=:emplid ");
			query.setParameter("emplid", emplid);

			List lst = query.list();
			Iterator it = lst.iterator();
			List<PDFProduct> tempList = new ArrayList<PDFProduct>();

			while (it.hasNext()) {
				PDFProduct prod = new PDFProduct();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					prod.setProduct(field[0].toString());
				if (field[1] != null)
					prod.setTeam(field[1].toString());

				tempList.add(prod);
			}
			pdfIterator = tempList.iterator();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return pdfIterator;
	}

	public GAProdCourse[] getProdCourseMapping() {
		Session session = HibernateUtils.getHibernateSession();
		GAProdCourse[] gaProdCourse = new GAProdCourse[200];
		try {
			Query query = session
					.createSQLQuery("Select product as productCode, course_code as courseCode "
							+ " from GAP_REPORT_CODES "
							+ " where product is not null and course_code is not null "
							+ " order by product");

			List lst = query.list();
			Iterator it = lst.iterator();
			List<GAProdCourse> tempList = new ArrayList<GAProdCourse>();

			while (it.hasNext()) {
				GAProdCourse prodCourse = new GAProdCourse();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					prodCourse.setProductCode(field[0].toString());
				if (field[1] != null)
					prodCourse.setCourseCode(field[1].toString());

				tempList.add(prodCourse);
			}
			gaProdCourse = tempList.toArray(new GAProdCourse[tempList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return gaProdCourse;
	}

	public Product[] getProdctByClusterTeam(String clusterCode, String teamCode) {
		Session session = HibernateUtils.getHibernateSession();
		Product[] product = new Product[200];
		try {
			Query query = session
					.createSQLQuery("Select distinct product_cd as productCode,"
							+ "product_desc as productDesc from fft_product_assignment "
							+ " where cluster_desc = :clusterCode and team_desc = :teamCode ");
			query.setParameter("teamCode", teamCode);
			query.setParameter("clusterCode", clusterCode);
			List lst = query.list();
			Iterator it = lst.iterator();
			List<Product> tempList = new ArrayList<Product>();

			while (it.hasNext()) {
				Product prod = new Product();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					prod.setProductCode(field[0].toString());
				if (field[1] != null)
					prod.setProductDesc(field[1].toString());

				tempList.add(prod);
			}
			product = tempList.toArray(new Product[tempList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return product;
	}

	public Iterator<TrainingSchedule> getRBUCancelTrainingSchedule(String emplid) {
		Session session = HibernateUtils.getHibernateSession();

		Iterator<TrainingSchedule> trainingIterator = null;
		try {
			Query query = session
					.createSQLQuery("SELECT C.CLASS_ID COURSEID,C.PRODUCT_CD COURSEDESCRIPTION,"
							+ "C.START_DATE COURSESCHEDULE, CA.UPDATE_REASON AS REASON "
							+ " FROM RBU_CLASS C,RBU_MANUAL_CLASS_ASSIGNMENT CA "
							+ " WHERE C.CLASS_ID=CA.CLASS_ID AND C.CLASS_ID >299 AND CA.UPDATE_FLAG = 'D'"
							+ " AND CA.EMPLID = :emplid");
			query.setParameter("emplid", emplid);

			List lst = query.list();
			Iterator it = lst.iterator();
			List<TrainingSchedule> tempList = new ArrayList<TrainingSchedule>();

			while (it.hasNext()) {
				TrainingSchedule prod = new TrainingSchedule();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					prod.setCourseID(field[0].toString());
				if (field[1] != null)
					prod.setCourseDescription(field[1].toString());
				if (field[2] != null)
					prod.setCourseSchedule((Date) field[2]);
				if (field[3] != null)
					prod.setCancelReason(field[3].toString());

				tempList.add(prod);
			}
			trainingIterator = tempList.iterator();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return trainingIterator;
	}

	public int insertGNSMAttendance(String emplid, String userId,
			String result, String mode) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		int sqlResult = 0;
		try {
			Query query = session
					.createSQLQuery("INSERT INTO GNSM_ATTENDANCE "
							+ " (GNSM_ATTENDANCE_ID,EMPLID,BY_USER,DATE_TIME,YES_NO,TYPE)"
							+ " VALUES ( (SELECT COUNT(*)+1 FROM GNSM_ATTENDANCE), "
							+ " :emplid,:userId,SYSDATE,:result,:mode)");
			query.setParameter("emplid", emplid);
			query.setParameter("userId", userId);
			query.setParameter("result", result);
			query.setParameter("mode", mode);

			sqlResult = query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("insertGNSMAttendance Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return sqlResult;
	}

	public int insertMSEPIAttendance(String emplid, String userId,
			String result, String mode) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		int sqlResult = 0;
		try {
			Query query = session
					.createSQLQuery("INSERT INTO MSEPI_NSM_ATTENDANCE "
							+ " (GNSM_ATTENDANCE_ID,EMPLID,BY_USER,DATE_TIME,YES_NO,TYPE) "
							+ " VALUES ( (SELECT COUNT(*)+1 FROM GNSM_ATTENDANCE), "
							+ " :emplid,:userId,SYSDATE,:result,:mode)");
			query.setParameter("emplid", emplid);
			query.setParameter("userId", userId);
			query.setParameter("result", result);
			query.setParameter("mode", mode);

			sqlResult = query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("insertMSEPIAttendance Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return sqlResult;
	}

	public int insertVRSAttendance(String emplid, String userId, String result,
			String mode) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		int sqlResult = 0;
		try {
			Query query = session
					.createSQLQuery("INSERT INTO SPIRIVA_ATTENDANCE "
							+ " (SPIRIVA_ATTENDANCE_ID,EMPLID,BY_USER,DATE_TIME,YES_NO,TYPE) "
							+ " VALUES ( (SELECT COUNT(*)+1 FROM SPIRIVA_ATTENDANCE), "
							+ " :emplid,:userId,SYSDATE,:result,:mode)");
			query.setParameter("emplid", emplid);
			query.setParameter("userId", userId);
			query.setParameter("result", result);
			query.setParameter("mode", mode);

			sqlResult = query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {

			e.printStackTrace();

			System.out.println("insertVRSAttendance Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return sqlResult;
	}

	public Iterator cancelTrainingSchedule(String emplid, String courseID) {

		Iterator it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("delete course_assignment where trainee_ssn = :emplid and course_id = :courseID");
			query.setParameter("emplid", emplid);
			query.setParameter("courseID", courseID);
			int res = query.executeUpdate();
			Log.info("cancelTrainingSchedule" + res);
			List<Object[]> mlList = query.list();
			it = mlList.iterator();
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("cancelTrainingSchedule() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public void copyTrackPhase(String newTrack, String oldTrack) {

		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("INSERT INTO P2L_TRACK_PHASE (TRACK_ID,TRACK_PHASE_ID,PHASE_NUMBER ,ROOT_ACTIVITY_ID,ALT_ACTIVITY_ID , "
							+ " DO_PREREQUISITE, REPORT_APPROVAL_STATUS,SORT_ORDER,DO_ASSIGNED,DO_EXEMPT) "
							+ " VALUES (:newTrack,(SELECT TRACK_PHASE_ID,PHASE_NUMBER ,ROOT_ACTIVITY_ID,ALT_ACTIVITY_ID ,DO_PREREQUISITE, "
							+ " REPORT_APPROVAL_STATUS,SORT_ORDER,DO_ASSIGNED,DO_EXEMPT FROM P2L_TRACK_PHASE WHERE TRACK_ID = :oldTrack)) ");
			query.setParameter("newTrack", newTrack);
			query.setParameter("oldTrack", oldTrack);
			int res = query.executeUpdate();
			Log.info("copyTrackPhase::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("cancelTrainingSchedule() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public int deleteLaunchMeetingAttendance(String id) {
		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE launch_meeting_attendance WHERE track_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteLaunchMeetingAttendance::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteLaunchMeetingAttendance() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteReportByID(String id) {
		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE p2l_track WHERE track_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteReportByID::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteReportByID() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteReportByIDForForecastReport(String id) {
		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE forecast_track WHERE track_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteReportByIDForForecastReport::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteReportByIDForForecastReport() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteReportByIDForLaunchMeeting(String id) {

		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE launch_meeting WHERE track_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteReportByIDForLaunchMeeting::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteReportByIDForLaunchMeeting() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteReportByIDForManagementReport(String id) {
		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE management_track WHERE track_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteReportByIDForManagementReport::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteReportByIDForManagementReport() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteReportFromForecastFilterCriteria(String id) {
		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE forecast_filter_criteria WHERE FORECAST_REPORT_ID = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteReportFromForecastFilterCriteria::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteReportFromForecastFilterCriteria() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteReportFromManagementCodeDesc(String id) {
		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE management_code_desc WHERE filter_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteReportFromManagementCodeDesc::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteReportFromManagementCodeDesc() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteReportFromManagementFilterCriteria(String id) {
		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE management_filter_criteria WHERE filter_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteReportFromManagementFilterCriteria::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteReportFromManagementFilterCriteria() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteTrackActivityMappingForLaunchMeeting(String id) {
		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE track_activity_mapping WHERE track_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteTrackActivityMappingForLaunchMeeting::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteTrackActivityMappingForLaunchMeeting() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteTrackPhaseByID(String id) {

		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE p2l_track_phase WHERE track_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteTrackPhaseByID::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteTrackPhaseByID() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public int deleteTrackPhaseByIDForLaunchMeeting(String id) {
		int res = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("DELETE launch_meeting_details WHERE track_id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
			Log.info("deleteTrackPhaseByIDForLaunchMeeting::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("deleteTrackPhaseByIDForLaunchMeeting() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return res;
	}

	public void firstInsert(String str) {
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction tx = session.beginTransaction();
			Query query = session
					.createSQLQuery("INSERT INTO EMPLOYEE_GRID_CONFIG(SERIALNO,SELECTED_FIELDS) VALUES (1,:str)");
			query.setParameter("str", str);
			int res = query.executeUpdate();
			Log.info("firstInsert::" + res);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("firstInsert() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public Territory[] getAdminTerritory() {

		Territory[] territoryy = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select area_cd as areaCd, area_desc as areaDesc, region_cd as regionCd, region_desc as regionDesc, district_id as districtId, district_desc as districtDesc, "
							+ " territory_id as territoryId  from v_new_field_employee  where cluster_cd in ('Steere','Pratt','Powers')");
			List tyList = query.list();
			List<Territory> templist = new ArrayList<Territory>();
			templist.clear();
			Iterator it = tyList.iterator();
			while (it.hasNext()) {
				Territory territory = new Territory();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					territory.setAreaCd(field[0].toString());
				if (field[1] != null)
					territory.setAreaDesc(field[1].toString());
				if (field[2] != null)
					territory.setRegionCd(field[2].toString());
				if (field[3] != null)
					territory.setRegionDesc(field[3].toString());
				if (field[4] != null)
					territory.setDistrictId(field[4].toString());
				if (field[5] != null)
					territory.setDistrictDesc(field[5].toString());
				if (field[6] != null)
					territory.setTerritoryId(field[6].toString());

				templist.add(territory);
				Log.info("getAdminTerritory::" + territory);
			}

			territoryy = templist.toArray(new Territory[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getAdminTerritory Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return territoryy;
	}

	public String[] getAllDeploymentId() {

		String[] deptpkIds = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT to_number(deployment_package_id) FROM future_product_alignments ORDER BY to_number(deployment_package_id) DESC");
			List<Integer> dids = query.list();
			deptpkIds = dids.toArray(new String[dids.size()]);
			Log.info("getAllDeploymentId()::" + deptpkIds);

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("getAllDeploymentId() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return deptpkIds;
	}

	public Product[] getAllProdcts() {

		Product[] productt = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select distinct product_cd as productCode, product_desc as productDesc from fft_product_assignment");
			List tyList = query.list();
			List<Product> templist = new ArrayList<Product>();
			templist.clear();
			Iterator it = tyList.iterator();
			while (it.hasNext()) {
				Product product = new Product();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					product.setProductCode(field[0].toString());
				if (field[1] != null)
					product.setProductDesc(field[1].toString());
				templist.add(product);
				Log.info("getAllProdcts()::" + product);
			}
			productt = templist.toArray(new Product[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getAllProdcts() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return productt;
	}

	public String[] getAllProductNames() {
		String[] productNames = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT product FROM products order by product");
			List<String> dids = query.list();
			productNames = dids.toArray(new String[dids.size()]);
			Log.info("getAllProductNames()::" + productNames);

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("getAllProductNames() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return productNames;
	}

	public TeamBean[] getAllRBUTEAM() {

		TeamBean[] teamBeann = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select distinct team_cd teamCd, team_desc  teamDesc from PWRA_PRODUCT_ASSIGNMENT");
			List tyList = query.list();
			List<TeamBean> templist = new ArrayList<TeamBean>();
			templist.clear();
			Iterator it = tyList.iterator();
			while (it.hasNext()) {
				TeamBean teamBean = new TeamBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					teamBean.setTeamCd(field[0].toString());
				if (field[1] != null)
					teamBean.setTeamDesc(field[1].toString());
				templist.add(teamBean);
				Log.info("getAllRBUTEAM()::" + teamBean);
			}
			teamBeann = templist.toArray(new TeamBean[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getAllRBUTEAM() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return teamBeann;
	}

	public RoleBean[] getAllRoles() {
		RoleBean[] roleBeann = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT(ROLE_CD) roleCd FROM MV_FIELD_EMPLOYEE_RBU where ROLE_CD is not null ORDER BY roleCd ASC");
			List queryList = query.list();
			List<RoleBean> templist = new ArrayList<RoleBean>();
			templist.clear();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				RoleBean roleBean = new RoleBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					roleBean.setRoleCd(field[0].toString());
				templist.add(roleBean);
				Log.info("getAllRBUTEAM()::" + roleBean);
			}
			roleBeann = templist.toArray(new RoleBean[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getAllRBUTEAM() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return roleBeann;
	}

	public TeamBean[] getAllTEAM() {
		TeamBean[] teamBeann = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select distinct ft.team_cd teamCd, ft.team_short_desc  teamDesc from MV_TEAM_CODE_MAP ft, "
							+ " v_new_field_employee v where v.TEAM_CD=ft.TEAM_SHORT_DESC and v.CLUSTER_CD in "
							+ " ('Steere','Pratt','Powers','Specialty Marke','Pratt Steere PR','Powers - PR','SM PR') order by teamdesc ");

			List queryList = query.list();
			List<TeamBean> templist = new ArrayList<TeamBean>();
			templist.clear();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				TeamBean teamBean = new TeamBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					teamBean.setTeamCd(field[0].toString());
				if (field[1] != null)
					teamBean.setTeamDesc(field[1].toString());

				templist.add(teamBean);
				Log.info("getAllTEAM()::" + teamBean);
			}
			teamBeann = templist.toArray(new TeamBean[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getAllTEAM() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return teamBeann;
	}

	public GapAnalysisEntry[] getGapAnalysisCompOrReg(String deploymentID,
			String prevDeploymentID, String duration, String emplId) {
		GapAnalysisEntry[] gapAnalysisEntryy = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT a.emplid as empID, a.first_name as firstName, a.last_name as lastName, "
							+ "  a.email_address as emailAddr, a.role_cd as rolecd, a.sales_organization as salesOrg, "
							+ "  a.manager_first_name as mngrFirstName, a.manager_last_name as mngrLastName, a.manager_email as mngrEmail, "
							+ "  b.product as productName, b.status as status  FROM  future_product_alignments a, (select emplid "
							+ "  from mv_field_employee_rbu start with emplid =:emplId connect by prior emplid = reports_to_emplid) r, "
							+ "  (SELECT b.product, a.guid, d.status AS status FROM future_product_alignments a, gap_report_codes b, "
							+ "  mv_usp_activity_master c, mv_usp_compl_and_reg d, gap_report_roles e WHERE a.product_desc = b.product "
							+ "  AND b.course_code = c.code AND b.active = 'Y' AND e.active = 'Y' AND a.role_cd = e.role_cd AND a.deployment_package_id = :deploymentID "
							+ "  AND c.activity_pk = d.activityfk AND add_months(d.status_date,:duration) >= SYSDATE AND a.guid = d.emp_no) b,(SELECT DISTINCT emplid, "
							+ "  product_desc FROM future_product_alignments WHERE deployment_package_id = :deploymentID MINUS  SELECT DISTINCT emplid, product_desc "
							+ "  FROM future_product_alignments WHERE deployment_package_id = :prevDeploymentID) c WHERE a.guid = b.guid AND a.product_desc = b.product "
							+ "  AND a.emplid=r.emplid AND a.emplid = c.emplid AND c.product_desc = b.product ORDER BY a.emplid,b.product");
			query.setParameter("deploymentID", deploymentID);
			query.setParameter("prevDeploymentID", prevDeploymentID);
			query.setParameter("duration", duration);
			query.setParameter("emplId", emplId);

			List queryList = query.list();
			List<GapAnalysisEntry> templist = new ArrayList<GapAnalysisEntry>();
			templist.clear();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				GapAnalysisEntry gapAnalysisEntry = new GapAnalysisEntry();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					gapAnalysisEntry.setEmpID(field[0].toString());
				if (field[1] != null)
					gapAnalysisEntry.setFirstName(field[1].toString());
				if (field[2] != null)
					gapAnalysisEntry.setLastName(field[2].toString());
				if (field[3] != null)
					gapAnalysisEntry.setEmailAddr(field[3].toString());
				if (field[4] != null)
					gapAnalysisEntry.setRolecd(field[4].toString());
				if (field[5] != null)
					gapAnalysisEntry.setSalesOrg(field[5].toString());
				if (field[6] != null)
					gapAnalysisEntry.setMngrFirstName(field[6].toString());
				if (field[7] != null)
					gapAnalysisEntry.setMngrLastName(field[7].toString());
				if (field[8] != null)
					gapAnalysisEntry.setMngrEmail(field[8].toString());
				if (field[9] != null)
					gapAnalysisEntry.setProductName(field[9].toString());
				if (field[10] != null)
					gapAnalysisEntry.setStatus(field[10].toString());

				templist.add(gapAnalysisEntry);
				Log.info("getGapAnalysisCompOrReg()::" + gapAnalysisEntry);
			}
			gapAnalysisEntryy = templist.toArray(new GapAnalysisEntry[templist
					.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getGapAnalysisCompOrReg() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return gapAnalysisEntryy;
	}

	public GapAnalysisEntry[] getGapAnalysisCompOrRegForAdmin(
			String deploymentID, String prevDeploymentID, String duration) {

		GapAnalysisEntry[] gapAnalysisEntryy = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT a.emplid as empID, a.first_name as firstName, a.last_name as lastName, "
							+ " a.email_address as emailAddr, a.role_cd as rolecd, a.sales_organization as salesOrg, "
							+ " a.manager_first_name as mngrFirstName, a.manager_last_name as mngrLastName, a.manager_email as mngrEmail, "
							+ " b.product as productName, b.status as status FROM  future_product_alignments a, (SELECT b.product, a.guid, "
							+ " d.status AS status FROM future_product_alignments a, gap_report_codes b, mv_usp_activity_master c, mv_usp_compl_and_reg d, "
							+ " gap_report_roles e WHERE a.product_desc = b.product  AND b.course_code = c.code AND b.active = 'Y' AND e.active = 'Y' "
							+ " AND a.role_cd = e.role_cd AND a.deployment_package_id =:deploymentID AND c.activity_pk = d.activityfk AND "
							+ " add_months(d.status_date,:duration) >= SYSDATE AND a.guid = d.emp_no) b, (SELECT DISTINCT emplid, product_desc "
							+ " FROM future_product_alignments WHERE deployment_package_id = :deploymentID AND EMPL_STATUS='A' "
							+ " MINUS SELECT DISTINCT emplid, product_desc FROM future_product_alignments WHERE deployment_package_id = :prevDeploymentID "
							+ " AND EMPL_STATUS='A') c WHERE a.guid = b.guid AND a.product_desc = b.product AND a.emplid = c.emplid AND c.product_desc = b.product "
							+ " ORDER BY a.emplid,b.product ");

			query.setParameter("deploymentID", deploymentID);
			query.setParameter("prevDeploymentID", prevDeploymentID);
			query.setParameter("duration", duration);
			List queryList = query.list();
			List<GapAnalysisEntry> templist = new ArrayList<GapAnalysisEntry>();
			templist.clear();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				GapAnalysisEntry gapAnalysisEntry = new GapAnalysisEntry();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					gapAnalysisEntry.setEmpID(field[0].toString());
				if (field[1] != null)
					gapAnalysisEntry.setFirstName(field[1].toString());
				if (field[2] != null)
					gapAnalysisEntry.setLastName(field[2].toString());
				if (field[3] != null)
					gapAnalysisEntry.setEmailAddr(field[3].toString());
				if (field[4] != null)
					gapAnalysisEntry.setRolecd(field[4].toString());
				if (field[5] != null)
					gapAnalysisEntry.setSalesOrg(field[5].toString());
				if (field[6] != null)
					gapAnalysisEntry.setMngrFirstName(field[6].toString());
				if (field[7] != null)
					gapAnalysisEntry.setMngrLastName(field[7].toString());
				if (field[8] != null)
					gapAnalysisEntry.setMngrEmail(field[8].toString());
				if (field[9] != null)
					gapAnalysisEntry.setProductName(field[9].toString());
				if (field[10] != null)
					gapAnalysisEntry.setStatus(field[10].toString());

				templist.add(gapAnalysisEntry);
				Log.info("getGapAnalysisCompOrRegForAdmin()::"
						+ gapAnalysisEntry);
			}
			gapAnalysisEntryy = templist.toArray(new GapAnalysisEntry[templist
					.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getGapAnalysisCompOrRegForAdmin() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return gapAnalysisEntryy;
	}

	public GapAnalysisEntry[] getGapAnalysisDiffEntries(String deploymentID,
			String prevDeploymentID, String duration, String emplId) {
		GapAnalysisEntry[] gapAnalysisEntryy = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery(" SELECT a.emplid as empID, a.first_name as firstName, a.last_name as lastName, "
							+ " a.email_address as emailAddr, a.role_cd as rolecd, a.sales_organization as salesOrg, a.manager_first_name as mngrFirstName, "
							+ " a.manager_last_name as mngrLastName, a.manager_email as mngrEmail, b.product as productName  FROM future_product_alignments a, "
							+ " (select emplid from mv_field_employee_rbu start with emplid = :emplId connect by prior emplid = reports_to_emplid) r, "
							+ " (SELECT b.product, a.guid FROM future_product_alignments a, gap_report_codes b, gap_report_roles c WHERE a.product_desc = b.product "
							+ "  AND c.role_cd = a.role_cd AND c.active = 'Y' AND b.active = 'Y' AND a.deployment_package_id = :deploymentID MINUS SELECT b.product, a.guid "
							+ "  FROM future_product_alignments a, gap_report_codes b, mv_usp_activity_master c, mv_usp_compl_and_reg d, gap_report_roles e WHERE a.product_desc = b.product "
							+ "  AND b.course_code = c.code AND b.active = 'Y' AND e.active = 'Y' AND a.role_cd = e.role_cd AND a.deployment_package_id = :deploymentID AND c.activity_pk = d.activityfk "
							+ "  AND add_months(d.status_date,:duration) >= SYSDATE  AND a.guid = d.emp_no ) b, (SELECT DISTINCT emplid, product_desc FROM future_product_alignments "
							+ "  WHERE deployment_package_id = :deploymentID MINUS SELECT DISTINCT emplid, product_desc FROM future_product_alignments WHERE deployment_package_id = :prevDeploymentID) c "
							+ "  WHERE a.guid = b.guid AND a.product_desc = b.product AND a.emplid =r.emplid AND a.emplid = c.emplid AND c.product_desc = b.product "
							+ "  ORDER BY a.emplid,b.product");

			query.setParameter("deploymentID", deploymentID);
			query.setParameter("prevDeploymentID", prevDeploymentID);
			query.setParameter("duration", duration);
			query.setParameter("emplId", emplId);
			List queryList = query.list();
			List<GapAnalysisEntry> templist = new ArrayList<GapAnalysisEntry>();
			templist.clear();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				GapAnalysisEntry gapAnalysisEntry = new GapAnalysisEntry();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					gapAnalysisEntry.setEmpID(field[0].toString());
				if (field[1] != null)
					gapAnalysisEntry.setFirstName(field[1].toString());
				if (field[2] != null)
					gapAnalysisEntry.setLastName(field[2].toString());
				if (field[3] != null)
					gapAnalysisEntry.setEmailAddr(field[3].toString());
				if (field[4] != null)
					gapAnalysisEntry.setRolecd(field[4].toString());
				if (field[5] != null)
					gapAnalysisEntry.setSalesOrg(field[5].toString());
				if (field[6] != null)
					gapAnalysisEntry.setMngrFirstName(field[6].toString());
				if (field[7] != null)
					gapAnalysisEntry.setMngrLastName(field[7].toString());
				if (field[8] != null)
					gapAnalysisEntry.setMngrEmail(field[8].toString());
				if (field[9] != null)
					gapAnalysisEntry.setProductName(field[9].toString());

				templist.add(gapAnalysisEntry);
				Log.info("getGapAnalysisDiffEntries()::" + gapAnalysisEntry);
			}
			gapAnalysisEntryy = templist.toArray(new GapAnalysisEntry[templist
					.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getGapAnalysisDiffEntries() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return gapAnalysisEntryy;
	}

	public GapAnalysisEntry[] getGapAnalysisDiffEntriesForAdmin(
			String deploymentID, String prevDeploymentID, String duration) {
		GapAnalysisEntry[] gapAnalysisEntryy = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery(" SELECT a.emplid as empID, a.first_name as firstName, a.last_name as lastName, a.email_address as emailAddr, "
							+ " a.role_cd as rolecd, a.sales_organization as salesOrg, a.manager_first_name as mngrFirstName, a.manager_last_name as mngrLastName, "
							+ " a.manager_email as mngrEmail, b.product as productName FROM future_product_alignments a, (SELECT b.product, a.guid FROM future_product_alignments a, "
							+ " gap_report_codes b, gap_report_roles c WHERE a.product_desc = b.product AND c.role_cd = a.role_cd AND c.active = 'Y' AND b.active = 'Y' "
							+ " AND a.deployment_package_id = :deploymentID MINUS SELECT b.product, a.guid FROM future_product_alignments a, gap_report_codes b,mv_usp_activity_master c, "
							+ " mv_usp_compl_and_reg d, gap_report_roles e WHERE a.product_desc = b.product AND b.course_code = c.code AND b.active = 'Y' AND e.active = 'Y' "
							+ " AND a.role_cd = e.role_cd AND a.deployment_package_id = :deploymentID AND c.activity_pk = d.activityfk AND add_months(d.status_date,:duration) >= SYSDATE "
							+ " AND a.guid = d.emp_no ) b, (SELECT DISTINCT emplid, product_desc FROM future_product_alignments WHERE deployment_package_id = :deploymentID AND EMPL_STATUS='A' "
							+ " MINUS SELECT DISTINCT emplid, product_desc FROM future_product_alignments WHERE deployment_package_id = :prevDeploymentID  AND EMPL_STATUS='A') c "
							+ " WHERE a.guid = b.guid AND a.product_desc = b.product AND a.emplid = c.emplid AND c.product_desc = b.product ORDER BY a.emplid,b.product ");

			query.setParameter("deploymentID", deploymentID);
			query.setParameter("prevDeploymentID", prevDeploymentID);
			query.setParameter("duration", duration);
			List queryList = query.list();
			List<GapAnalysisEntry> templist = new ArrayList<GapAnalysisEntry>();
			templist.clear();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				GapAnalysisEntry gapAnalysisEntry = new GapAnalysisEntry();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					gapAnalysisEntry.setEmpID(field[0].toString());
				if (field[1] != null)
					gapAnalysisEntry.setFirstName(field[1].toString());
				if (field[2] != null)
					gapAnalysisEntry.setLastName(field[2].toString());
				if (field[3] != null)
					gapAnalysisEntry.setEmailAddr(field[3].toString());
				if (field[4] != null)
					gapAnalysisEntry.setRolecd(field[4].toString());
				if (field[5] != null)
					gapAnalysisEntry.setSalesOrg(field[5].toString());
				if (field[6] != null)
					gapAnalysisEntry.setMngrFirstName(field[6].toString());
				if (field[7] != null)
					gapAnalysisEntry.setMngrLastName(field[7].toString());
				if (field[8] != null)
					gapAnalysisEntry.setMngrEmail(field[8].toString());
				if (field[9] != null)
					gapAnalysisEntry.setProductName(field[9].toString());

				templist.add(gapAnalysisEntry);
				Log.info("getGapAnalysisDiffEntriesForAdmin()::"
						+ gapAnalysisEntry);
			}
			gapAnalysisEntryy = templist.toArray(new GapAnalysisEntry[templist
					.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getGapAnalysisDiffEntriesForAdmin() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return gapAnalysisEntryy;
	}

	public GAProduct[] getGAProducts() {
		GAProduct[] gaProductt = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select product from products order by product asc");

			List queryList = query.list();
			List<GAProduct> templist = new ArrayList<GAProduct>();
			templist.clear();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				GAProduct gaProduct = new GAProduct();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					gaProduct.setProduct(field[0].toString());
				templist.add(gaProduct);
				Log.info("getGAProducts()::" + gaProduct);
			}
			gaProductt = templist.toArray(new GAProduct[templist.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.info("getGAProducts() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return gaProductt;
	}

	public Iterator<PDFHomeStudyStatus> getGNSMStatus(String emplid) {

		Iterator it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery("SELECT DISTINCT TRT_COURSE_NAME PRODUCTDESC ,COURSE_CODE PEDAGOGUEEXAM "
							+ " ,SCORE SCORE ,DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') STATUS "
							+ " ,EXAM_TAKEN_DATE COMPLETIONDATE FROM V_GNSM_DATA WHERE EMPLID = :emplid ORDER BY TRT_COURSE_NAME");

			query.setParameter("emplid", emplid);

			Iterator its = query.list().iterator();
			List<PDFHomeStudyStatus> ls = new ArrayList<PDFHomeStudyStatus>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				PDFHomeStudyStatus aPDFHomeStudyStatus = new PDFHomeStudyStatus();
				if (object[0] != null)
					aPDFHomeStudyStatus.setProductDesc(object[0].toString());
				if (object[1] != null)
					aPDFHomeStudyStatus.setPedagogueExam(object[1].toString());
				if (object[2] != null)
					aPDFHomeStudyStatus.setScore(object[2].toString());
				if (object[3] != null)
					aPDFHomeStudyStatus.setStatus(object[3].toString());
				if (object[4] != null)
					aPDFHomeStudyStatus.setCompletionDate((Date) object[4]);

				ls.add(aPDFHomeStudyStatus);
			}
			it = ls.iterator();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getGNSMStatus() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public String[] getMinimize() {
		String[] minimizee = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT minimize FROM TRAINING_REPORT WHERE DELETE_FLAG='N' and PARENT IS NULL order by sort_order asc ");
			List<String> minimize = query.list();
			minimizee = minimize.toArray(new String[minimize.size()]);
			Log.info("getMinimize()::" + minimizee);

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("getMinimize() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return minimizee;
	}

	public String getMSEPIAttendance(String emplid, String mode) {

		String yes_no = "";
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT YES_NO FROM MSEPI_NSM_ATTENDANCE  WHERE EMPLID = :emplid AND TYPE = :mode");
			query.setParameter("emplid", emplid);
			query.setParameter("mode", mode);
			query.uniqueResult();
			List queryList = query.list();

			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					yes_no = field.toString();
				Log.info("getMSEPIAttendance()");
			}

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("HibernateException in getMSEPIAttendance", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return yes_no;

	}

	public String getMSEPIPLAttendanceStatus(String emplid) {

		String status = "";
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery(" SELECT STATUS FROM V_MSEPI_NSM_COMPLIANCE_STATUS WHERE EMPLID=:emplid");
			query.setParameter("emplid", emplid);
			query.uniqueResult();

			List queryList = query.list();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					status = field.toString();
				Log.info("getMSEPIPLAttendanceStatus()");
			}

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("HibernateException in getMSEPIPLAttendanceStatus", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return status;

	}

	public String getOverallGNSMStatus(String emplid) {
		String status = "";
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT(DECODE(OVERALL_STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA')) STATUS FROM V_GNSM_DATA_OVERALL WHERE EMPLID = :emplid");
			query.setParameter("emplid", emplid);
			// query.uniqueResult();
			if (query.list().get(0) != null)
				status = query.list().get(0).toString();
			System.out.println("Status in getOverallGNSMStatus " + status);

			/*
			 * List queryList = query.list(); Iterator it =
			 * queryList.iterator(); while (it.hasNext()) { status =
			 * it.next().toString(); Log.info("getOverallGNSMStatus()"); }
			 */

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("HibernateException in getOverallGNSMStatus", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return status;

	}

	public String getOverallHomeStudyStatus(String emplid) {
		String status = "";
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT(DECODE(OVERALL_STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA')) STATUS FROM V_PWRA_HS_DATA_OVERALL WHERE EMPLID = :emplid");
			query.setParameter("emplid", emplid);
			query.uniqueResult();

			List queryList = query.list();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					status = field.toString();
				Log.info("getOverallHomeStudyStatus()");
			}

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("HibernateException in getOverallHomeStudyStatus", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return status;

	}

	public String getOverallMSEPIStatus(String emplid) {

		String status = "";
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT(DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA')) STATUS FROM  V_MSEPI_NSM_DATA  WHERE EMPLID = :emplid");
			query.setParameter("emplid", emplid);
			query.uniqueResult();

			List queryList = query.list();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					status = field.toString();
				Log.info("getOverallMSEPIStatus()");
			}

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("HibernateException in getOverallMSEPIStatus", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return status;

	}

	public String getOverallPLCStatus(String emplid) {

		String status = "";
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT(DECODE(OVERALL_STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA')) STATUS FROM V_PWRA_PLC_DATA_OVERALL WHERE EMPLID =:emplid");
			query.setParameter("emplid", emplid);
			query.uniqueResult();

			List queryList = query.list();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					status = field.toString();
				Log.info("getOverallPLCStatus()");
			}

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("HibernateException in getOverallPLCStatus", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return status;

	}

	public String getOverallSPFStatus(String emplid) {

		String status = "";
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT(DECODE(OVERALL_STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA')) STATUS FROM V_SPF_PLC_DATA_OVERALL WHERE EMPLID =:emplid");
			query.setParameter("emplid", emplid);
			query.uniqueResult();

			List queryList = query.list();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					status = field.toString();
				Log.info("getOverallSPFStatus()");
			}

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("HibernateException in getOverallSPFStatus", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return status;

	}

	public String getOverallVRSStatus(String emplid) {
		String status = "";
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT DISTINCT(DECODE(OVERALL_STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA')) STATUS  FROM V_SPIRIVA_DATA_OVERALL  WHERE EMPLID = :emplid");
			query.setParameter("emplid", emplid);
			query.uniqueResult();

			List queryList = query.list();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					status = field.toString();
				Log.info("getOverallVRSStatus()");
			}

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("HibernateException in getOverallVRSStatus", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return status;

	}

	public Iterator<PDFHomeStudyStatus> getPDFHomeStudyStatus(String emplid) {

		Iterator<PDFHomeStudyStatus> it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery("SELECT DISTINCT PPA.PRODUCT_DESC PRODUCTDESC ,V.EXAM_NAME PEDAGOGUEEXAM ,V.TEST_SCORE SCORE "
							+ "  ,DECODE(V.STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') STATUS ,V.EXAM_TAKEN_DATE COMPLETIONDATE "
							+ "  FROM V_PWRA_HS_DATA V,PWRA_PRODUCT_ASSIGNMENT PPA  WHERE V.PRODUCT_CD= PPA.PRODUCT_CD AND V.EMPLID =:emplid "
							+ "  ORDER BY PPA.PRODUCT_DESC");

			query.setParameter("emplid", emplid);

			Iterator its = query.list().iterator();
			List<PDFHomeStudyStatus> ls = new ArrayList<PDFHomeStudyStatus>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				PDFHomeStudyStatus aPDFHomeStudyStatus = new PDFHomeStudyStatus();
				if (object[0] != null)
					aPDFHomeStudyStatus.setProductDesc(object[0].toString());
				if (object[1] != null)
					aPDFHomeStudyStatus.setPedagogueExam(object[1].toString());
				if (object[2] != null)
					aPDFHomeStudyStatus.setScore(object[2].toString());
				if (object[3] != null)
					aPDFHomeStudyStatus.setStatus(object[3].toString());
				if (object[4] != null)
					aPDFHomeStudyStatus.setCompletionDate((Date) object[4]);

				ls.add(aPDFHomeStudyStatus);
			}
			it = ls.iterator();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPDFHomeStudyStatus() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public Iterator<TrainingScheduleList> getPDFTrainingScheduleList(String courseID) {

		Iterator<TrainingScheduleList> it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery("SELECT START_DATE STARTDATE, COURSE_ID COURSEID FROM COURSE WHERE COURSE_ID = :courseID UNION SELECT START_DATE STARTDATE, COURSE_ID COURSEID "
							+ " FROM COURSE WHERE COURSE_ID BETWEEN 300 AND 399 AND PRODUCT_CD = (SELECT C.PRODUCT_CD FROM COURSE C WHERE C.COURSE_ID=:courseID)  "
							+ " AND ((COURSE_ID = :courseID + 14) OR (COURSE_ID = :courseID - 14))");

			query.setParameter("courseID", courseID);

			Log.info("getPDFTrainingScheduleList");
			
			Iterator its =  query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				TrainingScheduleList trainingScheduleList = new TrainingScheduleList();
				if (object[0] != null)
					trainingScheduleList.setStartDate((Date) object[0]);
				if (object[1] != null)
					trainingScheduleList.setCourseID(object[1].toString());

				ls.add(trainingScheduleList);
			}
			it = ls.iterator();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPDFTrainingScheduleList() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public Iterator<TrainingScheduleList> getPDFTrainingScheduleListOld(
			String courseID) {
		Iterator it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery(" SELECT START_DATE STARTDATE, COURSE_ID COURSEID FROM COURSE  WHERE COURSE_ID = :courseID union "
							+ "   SELECT START_DATE STARTDATE, COURSE_ID COURSEID FROM COURSE WHERE PRODUCT_CD =  "
							+ "  (SELECT C.PRODUCT_CD FROM COURSE C WHERE C.COURSE_ID=:courseID) AND ( (COURSE_ID>=300 AND COURSE_ID<309) or "
							+ "  (COURSE_ID>=314 AND COURSE_ID<=319))");

			query.setParameter("courseID", courseID);

			Iterator its = query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				TrainingScheduleList trainingScheduleList = new TrainingScheduleList();
				if (object[0] != null)
					trainingScheduleList.setStartDate((Date) object[0]);
				if (object[1] != null)
					trainingScheduleList.setCourseID(object[1].toString());

				ls.add(trainingScheduleList);
			}
			it = ls.iterator();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPDFTrainingScheduleListOld() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public Iterator<TrainingScheduleList> getPDFTrainingScheduleListPHR(
			String courseID) {

		Iterator it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery("SELECT START_DATE STARTDATE, COURSE_ID COURSEID FROM COURSE WHERE COURSE_ID = :courseID union  SELECT START_DATE STARTDATE, COURSE_ID COURSEID "
							+ " FROM COURSE WHERE PRODUCT_CD = (SELECT C.PRODUCT_CD FROM COURSE C WHERE C.COURSE_ID=:courseID ) AND ((COURSE_ID = 300) or (COURSE_ID>=309 AND COURSE_ID<=311) or "
							+ " (COURSE_ID = 314) or (COURSE_ID>=323 AND COURSE_ID<=325) ) ");

			query.setParameter("courseID", courseID);
			Iterator its = query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				TrainingScheduleList trainingScheduleList = new TrainingScheduleList();
				if (object[0] != null)
					trainingScheduleList.setStartDate((Date) object[0]);
				if (object[1] != null)
					trainingScheduleList.setCourseID(object[1].toString());

				ls.add(trainingScheduleList);
			}
			it = ls.iterator();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPDFTrainingScheduleListOld() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public PedagogueExam[] getPedqgogueByEmplidProduct(String productCode,
			String emplid) {

		PedagogueExam[] pedagogueExamm = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select fe.EMPLID as emplid, decode(pTest.EXAM_NAME,null,'Not taken', pTest.EXAM_NAME) as examName, "
							+ " score.exam_taken_date as examDate, score.test_score as examScore, score.set_id as setId from ( select distinct pt.EMPLID, pt.EXAM_NAME, pt.SET_ID, pp.PRODUCT_CD "
							+ " from v_pedagogue_test pt, fft_product_pedagogue_map pp  where  pt.SET_ID = pp.SET_ID ) pTest, v_new_field_employee fe, v_training_required ep, "
							+ " (select ps1.* from pedagogue_scores ps1, ( select SET_ID,EMPLID, max(exam_taken_date) as exam_taken_date from pedagogue_scores group by set_id, emplid)  ps2 "
							+ " where ps1.set_id = ps2.set_id and ps1.exam_taken_date = ps2.exam_taken_date and ps1.emplid = ps2.emplid ) score where fe.EMPLID = :emplid "
							+ " and fe.EMPLID = ep.EMPLID and ep.EMPLID = pTest.EMPLID(+) and ep.PRODUCT_CD = pTest.product_cd(+) and score.SET_ID(+) = pTest.SET_ID "
							+ " and pTest.EMPLID = score.EMPLID(+) and ep.PRODUCT_CD = :productCode order by pTest.EXAM_NAME ");

			query.setParameter("productCode", productCode);
			query.setParameter("emplid", emplid);

			List queryList = query.list();
			Iterator it = queryList.iterator();
			List<PedagogueExam> templist = new ArrayList<PedagogueExam>();
			templist.clear();
			while (it.hasNext()) {
				PedagogueExam pedagogueExam = new PedagogueExam();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					pedagogueExam.setEmplid(field[0].toString());
				if (field[1] != null)
					pedagogueExam.setExamName(field[1].toString());
				if (field[2] != null)
					pedagogueExam.setExamDate((Date) field[2]);
				if (field[3] != null)
					pedagogueExam.setExamScore(field[3].toString());
				if (field[4] != null)
					pedagogueExam.setSetId(field[4].toString());
				templist.add(pedagogueExam);
			}

			pedagogueExamm = templist
					.toArray(new PedagogueExam[templist.size()]);

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPedqgogueByEmplidProduct() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return pedagogueExamm;
	}

	public String getPLAttendanceStatus(String emplid) {
		String status = "";
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery(" SELECT STATUS FROM V_GNSM_COMPLIANCE_STATUS WHERE EMPLID=:emplid");
			query.setParameter("emplid", emplid);
			query.uniqueResult();

			List queryList = query.list();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					status = field.toString();
				Log.info("getPLAttendanceStatus()");
			}

		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("HibernateException in getPLAttendanceStatus()", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return status;

	}

	public Iterator<PLCExamStatus> getPLCExamStatus(String emplid) {

		Iterator it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery(" SELECT EXAM_TYPE EXAMTYPE ,EXAM_NAME EXAMNAME ,TEST_SCORE SCORE ,DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') EXAMSTATUS "
							+ " ,EXAM_TAKEN_DATE COMPLETIONDATE ,PRODUCT_CD PRODUCTCODE FROM V_PWRA_PLC_DATA V WHERE V.EMPLID= :emplid");
			query.setParameter("emplid", emplid);

			Iterator its = query.list().iterator();
			List<PLCExamStatus> ls = new ArrayList<PLCExamStatus>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				PLCExamStatus aPLCExamStatus = new PLCExamStatus();
				if (object[0] != null)
					aPLCExamStatus.setExamType(object[0].toString());
				if (object[1] != null)
					aPLCExamStatus.setExamName(object[1].toString());
				if (object[2] != null)
					aPLCExamStatus.setScore(object[2].toString());
				if (object[3] != null)
					aPLCExamStatus.setExamStatus(object[3].toString());
				if (object[4] != null)
					aPLCExamStatus.setCompletionDate((Date) object[4]);
				if (object[5] != null)
					aPLCExamStatus.setProductCode(object[5].toString());

				ls.add(aPLCExamStatus);
			}
			it = ls.iterator();

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPLCExamStatus() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public Iterator<PLCExamStatus> getPLCExamStatusRBU(String emplid) {
		Log.info("getPLCExamStatusRBU()::Start");
		Iterator it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery(" SELECT EXAM_TYPE EXAMTYPE ,EXAM_NAME EXAMNAME ,TEST_SCORE SCORE ,DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') EXAMSTATUS ,EXAM_TAKEN_DATE COMPLETIONDATE"
							+ " ,PRODUCT_CD PRODUCTCODE FROM V_RBU_PLC_DATA V  WHERE V.EMPLID= :emplid order by productcode, examtype asc");
			query.setParameter("emplid", emplid);

			Iterator its = query.list().iterator();
			List<PLCExamStatus> ls = new ArrayList<PLCExamStatus>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				PLCExamStatus aPLCExamStatus = new PLCExamStatus();
				if (object[0] != null)
					aPLCExamStatus.setExamType(object[0].toString());
				if (object[1] != null)
					aPLCExamStatus.setExamName(object[1].toString());
				if (object[2] != null)
					aPLCExamStatus.setScore(object[2].toString());
				if (object[3] != null)
					aPLCExamStatus.setExamStatus(object[3].toString());
				if (object[4] != null)
					aPLCExamStatus.setCompletionDate((Date) object[4]);
				if (object[5] != null)
					aPLCExamStatus.setProductCode(object[5].toString());

				ls.add(aPLCExamStatus);
			}
			it = ls.iterator();
			Log.info("getPLCExamStatusRBU()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPLCExamStatusRBU() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public Iterator<PLCStatus> getPLCStatus(String emplid) {
		Log.info("getPLCStatus()::Start");
		Iterator it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery(" SELECT DISTINCT (CASE WHEN V.PRODUCT_CD = 'PLCA' THEN 'General Session' ELSE P.PRODUCT_DESC END) PRODUCT, "
							+ " V.PRODUCT_CD PRODUCTCODE, DECODE(V.OVERALL_STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') STATUS "
							+ " FROM ( SELECT DISTINCT pd.EMPLID, pd.PRODUCT_CD,DECODE(g1.status,'','P',g1.status) AS overall_status "
							+ " FROM v_pwra_plc_data pd,(SELECT DISTINCT emplid,product_cd,status FROM v_pwra_plc_data WHERE status IN('L','I')) g1 "
							+ " WHERE pd.emplid=g1.emplid(+) AND pd.product_cd=g1.product_cd(+) ) V, PRODUCT_CODE_MAP P WHERE P.PRODUCT_CD(+)=V.PRODUCT_CD "
							+ " AND EMPLID = :emplid");

			query.setParameter("emplid", emplid);

			Iterator its = query.list().iterator();
			List<PLCStatus> ls = new ArrayList<PLCStatus>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				PLCStatus aPLCStatus = new PLCStatus();
				if (object[0] != null)
					aPLCStatus.setProduct(object[0].toString());
				if (object[1] != null)
					aPLCStatus.setStatus(object[1].toString());

				ls.add(aPLCStatus);
			}
			it = ls.iterator();
			Log.info("getPLCStatus()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPLCStatus() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public Iterator<PLCStatus> getPLCStatusRBU(String emplid) {
		Log.info("getPLCStatusRBU()::Start");
		Iterator<PLCStatus> it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery(" SELECT DISTINCT (CASE WHEN v.product_cd = 'PLCA' THEN 'General Session' ELSE p.product_desc END "
							+ " ) product, v.product_cd productcode, DECODE (v.overall_status, 'L', 'On Leave','NC', 'Not Complete','C', 'Complete', "
							+ " 'NA' ) status,c.class_id, c.start_date FROM (SELECT DISTINCT pd.EMPLID, pd.PRODUCT_CD, DECODE (g1.status,'', 'C', "
							+ " g1.status ) AS overall_status FROM v_rbu_plc_data pd,(SELECT DISTINCT emplid, product_cd, status FROM v_rbu_plc_data "
							+ " WHERE status IN ('L', 'NC')) g1 WHERE pd.emplid = g1.emplid(+) AND pd.product_cd = g1.product_cd(+)) v, rbu_products p, "
							+ " rbu_class c, rbu_class_assignment ca WHERE p.product_cd(+) = v.product_cd AND c.product_cd = p.product_cd AND ca.class_id = c.class_id "
							+ " AND ca.emplid = v.emplid AND  v.emplid = :emplid ");

			query.setParameter("emplid", emplid);

			Iterator its = query.list().iterator();
			List<PLCStatus> ls = new ArrayList<PLCStatus>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				PLCStatus aPLCStatus = new PLCStatus();
				if (object[0] != null)
					aPLCStatus.setStatus(object[0].toString());
				if (object[1] != null)
					aPLCStatus.setClass_id(object[1].toString());
				if (object[2] != null)
					aPLCStatus.setClassDate((Date) object[2]);

				ls.add(aPLCStatus);
			}
			it = ls.iterator();
			Log.info("getPLCStatusRBU()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPLCStatusRBU() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public POAChartBean[] getPOAChartsForAll(String productCode) {
		POAChartBean[] aPOAChartBeann = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT 'Complete' coursestatus, COUNT (DISTINCT emplid) total FROM v_powers_midpoa1_data WHERE status = 'P' AND  product_cd = :productCode "
							+ " UNION SELECT 'InComplete' coursestatus, COUNT (DISTINCT emplid) total FROM v_powers_midpoa1_data WHERE status = 'I' AND product_cd = :productCode "
							+ " UNION SELECT 'OnLeave' coursestatus, COUNT (DISTINCT emplid) total FROM v_powers_midpoa1_data WHERE status = 'L' AND product_cd = :productCode");

			query.setParameter("productCode", productCode);

			List queryList = query.list();
			Iterator it = queryList.iterator();
			List<POAChartBean> templist = new ArrayList<POAChartBean>();
			templist.clear();
			while (it.hasNext()) {
				POAChartBean aPOAChartBean = new POAChartBean();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					aPOAChartBean.setCourseStatus(field[0].toString());
				if (field[1] != null)
					aPOAChartBean.setCourseStatus(field[1].toString());
				if (field[2] != null)
					aPOAChartBean.setCourseStatus(field[2].toString());
				templist.add(aPOAChartBean);
			}

			aPOAChartBeann = templist
					.toArray(new POAChartBean[templist.size()]);

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getPedqgogueByEmplidProduct() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return aPOAChartBeann;
	}

	public Iterator<TrainingScheduleList> getRBUTrainingScheduleList(
			String courseID) {

		Log.info("getRBUTrainingScheduleList()::Start");
		Iterator<TrainingScheduleList> it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery(" SELECT START_DATE STARTDATE, CLASS_ID COURSEID, CLASS_NAME courseName FROM RBU_CLASS WHERE  PRODUCT_CD = "
							+ " (SELECT C.PRODUCT_CD FROM RBU_CLASS C WHERE C.CLASS_ID=:courseID  ) ORDER BY STARTDATE ASC ");

			query.setParameter("courseID", courseID);

			Iterator its = query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				TrainingScheduleList trainingScheduleList = new TrainingScheduleList();
				trainingScheduleList.setStartDate((Date) object[0]);
				if (object[0] != null)
					trainingScheduleList.setCourseID(object[1].toString());
				if (object[1] != null)
					trainingScheduleList.setCourseName(object[1].toString());

				ls.add(trainingScheduleList);
			}
			it = ls.iterator();
			Log.info("getRBUTrainingScheduleList()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getRBUTrainingScheduleList() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public Iterator<TrainingScheduleList> getRBUTrainingScheduleListByProduct(
			String productcd) {
		Log.info("getRBUTrainingScheduleListByProduct()::Start");
		Iterator<TrainingScheduleList> it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery(" SELECT START_DATE STARTDATE, CLASS_ID COURSEID, CLASS_NAME courseName FROM RBU_CLASS WHERE "
							+ "  PRODUCT_CD = :productcd  ORDER BY STARTDATE ASC ");

			query.setParameter("productcd", productcd);

			Iterator its = query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				TrainingScheduleList trainingScheduleList = new TrainingScheduleList();
				if (object[0] != null)
					trainingScheduleList.setStartDate((Date) object[0]);
				if (object[1] != null)
					trainingScheduleList.setCourseID(object[1].toString());
				if (object[2] != null)
					trainingScheduleList.setCourseName(object[1].toString());

				ls.add(trainingScheduleList);
			}
			it = ls.iterator();
			Log.info("getRBUTrainingScheduleListByProduct()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getRBUTrainingScheduleListByProduct() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public EmployeeInfo getReportToInfoRBU(String emplid) {
		Log.info("getReportToInfoRBU()::Start");

		EmployeeInfo emmployeeInfo = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("SELECT EMAIL_ADDRESS REPORTTOEMAIL ,LAST_NAME REPORTTOLASTNAME,FIRST_NAME REPORTTOFIRSTNAME "
							+ " FROM V_RBU_Live_Feed WHERE EMPLID=:emplid ");

			query.setParameter("emplid", emplid);
			List ugList = query.list();

			Iterator it = ugList.iterator();
			while (it.hasNext()) {
				emmployeeInfo = new EmployeeInfo();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					emmployeeInfo.setReportToEmail(field[0].toString());
				if (field[1] != null)
					emmployeeInfo.setReportToLastName(field[1].toString());
				if (field[2] != null)
					emmployeeInfo.setReportToFirstName(field[2].toString());
			}
			Log.info("getReportToInfoRBU()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getReportToInfoRBU() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return emmployeeInfo;
	}

	public String[] getRoleCodes() {
		Log.info("getRoleCodes()::START");
		String[] roleCds = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery(" select distinct role_cd as roleCds from gap_rpt_ffra_roles order by role_cd ");

			List<String> queryList = query.list();
			roleCds = queryList.toArray(new String[queryList.size()]);
			Log.info("getRoleCodes()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getRoleCodes() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return roleCds;
	}

	public PassFail[] getTestScoreByRegion(String productCode, String area,
			String region) {
		Log.info("getTestScoreByRegion()::START");

		PassFail[] aPassFail = null;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select fe.EMPLID as emplid, decode(pTest.EXAM_NAME,null,'Not taken', pTest.EXAM_NAME) as examName, "
							+ "  decode (score.SET_PASSED,null,decode(pTest.EXAM_NAME,null,'Not taken','Failed'),'Passed') as status from "
							+ "  V_PEDAGOGUE_EXAM  pTest, v_new_field_employee fe, v_training_required ep, "
							+ " (select distinct  s.SET_PASSED, s.EMPLID, s.SET_ID from pedagogue_scores s where s.SET_PASSED = 1)  score where "
							+ "  fe.EMPLID = ep.EMPLID 	and ep.EMPLID = pTest.EMPLID(+) and ep.PRODUCT_CD = pTest.product_cd(+) and score.SET_ID(+) "
							+ "  = pTest.SET_ID and pTest.EMPLID = score.EMPLID(+) and ep.PRODUCT_CD = :productCode and fe.area_cd = :area "
							+ "  and fe.REGION_CD = :region ");

			query.setParameter("productCode", productCode);
			query.setParameter("area", area);
			query.setParameter("region", region);

			List queryList = query.list();
			Iterator it = queryList.iterator();
			List<PassFail> templist = new ArrayList<PassFail>();
			templist.clear();
			while (it.hasNext()) {
				PassFail passFail = new PassFail();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					passFail.setEmplid(field[0].toString());
				if (field[1] != null)
					passFail.setExamName(field[1].toString());
				if (field[2] != null)
					passFail.setStatus(field[2].toString());

				templist.add(passFail);
			}
			aPassFail = templist.toArray(new PassFail[templist.size()]);
			Log.info("getTestScoreByRegion()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTestScoreByRegion() Hibernatate Exception");

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return aPassFail;
	}

	public Iterator<TrainingScheduleList> getTrainingScheduleListLYRC(
			String courseID, String team) {

		Log.info("getTrainingScheduleListLYRC()::Start");
		Iterator<TrainingScheduleList> it = null;
		Session session = HibernateUtils.getHibernateSession();
		try {

			Query query = session
					.createSQLQuery(" SELECT START_DATE STARTDATE, COURSE_ID COURSEID FROM COURSE WHERE COURSE_ID = :courseID union "
							+ "  SELECT START_DATE STARTDATE, COURSE_ID COURSEID  FROM COURSE  WHERE PRODUCT_CD = (SELECT C.PRODUCT_CD "
							+ "  FROM COURSE C WHERE C.COURSE_ID=:courseID ) AND (COURSE_ID>=315 AND COURSE_ID<=319) union  SELECT START_DATE STARTDATE, "
							+ "  COURSE_ID COURSEID FROM COURSE WHERE PRODUCT_CD = (SELECT C.PRODUCT_CD FROM COURSE C WHERE C.COURSE_ID= :courseID ) "
							+ "  AND (COURSE_ID>=303 AND COURSE_ID<=305) AND upper(COURSE_DESCRIPTION) = :team ");

			query.setParameter("courseID", courseID);
			query.setParameter("team", team);

			Iterator its = query.list().iterator();
			List<TrainingScheduleList> ls = new ArrayList<TrainingScheduleList>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				TrainingScheduleList trainingScheduleList = new TrainingScheduleList();
				if (object[0] != null)
					trainingScheduleList.setStartDate((Date) object[0]);
				if (object[1] != null)
					trainingScheduleList.setCourseID(object[1].toString());
				ls.add(trainingScheduleList);
			}
			it = ls.iterator();

			Log.info("getTrainingScheduleListLYRC()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTrainingScheduleListLYRC() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return it;
	}

	public String getAttendance(String emplid, String mode) {
		Log.info("getAttendance()::START");
		Session session = HibernateUtils.getHibernateSession();
		String result = null;
		try {
			Query query = session
					.createSQLQuery(" SELECT YES_NO FROM GNSM_ATTENDANCE WHERE EMPLID =:emplid AND TYPE =:mode ");

			query.setParameter("emplid", emplid);
			query.setParameter("mode", mode);
			List queryList = query.list();
			Iterator it = queryList.iterator();
			while (it.hasNext()) {
				Object field = it.next();
				if (field != null)
					result = field.toString();
			}
			Log.info("getAttendance()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getAttendance() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return result;
	}

	public Attendance[] getAttendanceByArea(String productCode, String area) {
		Log.info("getAttendanceByArea()::START");
		Session session = HibernateUtils.getHibernateSession();
		Attendance[] attendancee = null;
		try {
			Query query = session
					.createSQLQuery(" select distinctfe.emplid,DECODE(ca.status,'SCHEDULED','Transitional Training','ATTENDED','Attended', "
							+ " decode(fe.EMPL_STATUS,'L','On Leave','Absent: Training Needed')) as status from v_training_required ep, "
							+ " v_new_field_employee fe,v_course_attendance ca where fe.emplid = ep.emplid and ep.emplid = ca.emplid(+) and "
							+ " ep.product_cd = ca.product_cd(+) and ca.status(+) != 'ABSENT' and ep.PRODUCT_CD=:productCode and fe.area_cd =:area ");

			query.setParameter("productCode", productCode);
			query.setParameter("area", area);
			List List = query.list();
			List<Attendance> templist = new ArrayList<Attendance>();
			templist.clear();
			Iterator it = List.iterator();
			while (it.hasNext()) {
				Attendance attendance = new Attendance();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					attendance.setEmplid(field[0].toString());
				if (field[1] != null)
					attendance.setStatus(field[1].toString());
				templist.add(attendance);
			}
			attendancee = templist.toArray(new Attendance[templist.size()]);
			Log.info("getAttendanceByArea()::END");
		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("getAttendanceByArea() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return attendancee;
	}

	public Attendance[] getAttendanceByDistrict(String productCode,
			String area, String region, String district) {
		Log.info("getAttendanceByDistrict()::START");
		Session session = HibernateUtils.getHibernateSession();
		Attendance[] attendancee = null;
		try {
			Query query = session
					.createSQLQuery(" select distinct fe.emplid,DECODE(ca.status,'SCHEDULED','Transitional Training','ATTENDED','Attended', decode(fe.EMPL_STATUS,'L','On Leave','Absent: Training Needed')) as status from v_training_required ep, "
							+ " v_new_field_employee fe,v_course_attendance ca where  fe.emplid = ep.emplid and ep.emplid = ca.emplid(+) and ep.product_cd = ca.product_cd(+)	and ca.status(+) != 'ABSENT' and ep.PRODUCT_CD=:productCode	and  "
							+ " fe.area_cd =:area and fe.district_id =:district and fe.region_cd =:region ");

			query.setParameter("productCode", productCode);
			query.setParameter("area", area);
			query.setParameter("region", region);
			query.setParameter("district", district);

			List aList = query.list();
			List<Attendance> templist = new ArrayList<Attendance>();
			templist.clear();
			Iterator it = aList.iterator();
			while (it.hasNext()) {
				Attendance attendance = new Attendance();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					attendance.setEmplid(field[0].toString());
				if (field[1] != null)
					attendance.setStatus(field[1].toString());
				templist.add(attendance);
			}
			attendancee = templist.toArray(new Attendance[templist.size()]);
			Log.info("getAttendanceByDistrict()::END");
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getAttendanceByDistrict Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return attendancee;
	}

	public Attendance[] getAttendanceByProduct(String productCode) {
		Log.info("getAttendanceByProduct()::START");
		Session session = HibernateUtils.getHibernateSession();
		Attendance[] attendances = null;
		try {
			Query query = session
					.createSQLQuery(" select distinct fe.emplid,DECODE(ca.status,'SCHEDULED','Transitional Training','ATTENDED','Attended', decode(fe.EMPL_STATUS,'L','On Leave','Absent: Training Needed'))  "
							+ " as status  from v_training_required ep,v_new_field_employee fe, v_course_attendance ca where fe.emplid = ep.emplid and ep.emplid = ca.emplid(+) and ep.product_cd = ca.product_cd(+) "
							+ " and ca.status(+) != 'ABSENT' and ep.PRODUCT_CD=:productCode ");
			query.setParameter("productCode", productCode);
			List list = query.list();
			List<Attendance> templist = new ArrayList<Attendance>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Attendance attendance = new Attendance();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					attendance.setEmplid(field[0].toString());
				if (field[1] != null)
					attendance.setStatus(field[1].toString());
				templist.add(attendance);
			}
			attendances = templist.toArray(new Attendance[templist.size()]);
			Log.info("getAttendanceByProduct()::END");
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getAttendanceByProduct() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return attendances;
	}

	public Attendance[] getAttendanceByRegion(String productCode, String area,
			String region) {
		Log.info("getAttendanceByRegion()::START");
		Session session = HibernateUtils.getHibernateSession();
		Attendance[] attendances = null;
		try {
			Query query = session
					.createSQLQuery(" select distinct fe.emplid, DECODE(ca.status,'SCHEDULED','Transitional Training','ATTENDED','Attended',decode(fe.EMPL_STATUS,'L','On  Leave','Absent: Training Needed'))"
							+ " as status from v_training_required ep, v_new_field_employee fe, v_course_attendance ca where fe.emplid = ep.emplid and ep.emplid = ca.emplid(+) and ep.product_cd = ca.product_cd(+) and"
							+ " ca.status(+) != 'ABSENT' and ep.PRODUCT_CD=:productCode and  fe.area_cd =:area and fe.region_cd =:region ");

			query.setParameter("productCode", productCode);
			query.setParameter("area", area);
			query.setParameter("region", region);
			List list = query.list();
			List<Attendance> templist = new ArrayList<Attendance>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Attendance attendance = new Attendance();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					attendance.setEmplid(field[0].toString());
				if (field[1] != null)
					attendance.setStatus(field[1].toString());
				templist.add(attendance);
			}
			attendances = templist.toArray(new Attendance[templist.size()]);
			Log.info("getAttendanceByRegion()::END");
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getAttendanceByRegion() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return attendances;
	}

	public Iterator<TrainingSchedule> getCancelTrainingSchedule(String emplid) {
		Log.info("getCancelTrainingSchedule()::START");
		Session session = HibernateUtils.getHibernateSession();
		Iterator<TrainingSchedule> iterator = null;
		try {
			Query query = session
					.createSQLQuery(" SELECT C.COURSE_ID COURSEID,C.COURSE_DESCRIPTION COURSEDESCRIPTION, "
							+ " C.START_DATE COURSESCHEDULE, CA.DELETE_REASON AS REASON FROM COURSE C, "
							+ " DELETED_COURSE_ASSIGNMENT CA WHERE C.COURSE_ID=CA.COURSE_ID AND C.COURSE_ID >299 AND CA.TRAINEE_SSN =:emplid ");

			query.setParameter("emplid", emplid);

			Iterator its = query.list().iterator();
			List<TrainingSchedule> ls = new ArrayList<TrainingSchedule>();
			while (its.hasNext()) {
				Object[] object = (Object[]) its.next();
				TrainingSchedule trainingSchedule = new TrainingSchedule();
				if (object[0] != null)
					trainingSchedule.setCourseID(object[0].toString());
				if (object[1] != null)
					trainingSchedule.setCourseDescription(object[1].toString());
				if (object[2] != null)
					trainingSchedule.setCourseSchedule((Date) object[2]);
				if (object[3] != null)
					trainingSchedule.setCancelReason(object[3].toString());
				ls.add(trainingSchedule);
			}
			iterator = ls.iterator();
			Log.info("getCancelTrainingSchedule()::END");
		} catch (HibernateException e) {
			Log.error("getCancelTrainingSchedule() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return iterator;
	}

	public int getCountConfigId() {
		Log.info("getCountConfigId()::START");
		int count = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {
			Query query = session
					.createSQLQuery("select count(distinct(config_id)) from training_path_config ");
			count = (Integer) query.uniqueResult();
			Log.info("getCountConfigId()::END");

		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getCountConfigId() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return count;
	}

	public Employee[] getEmployeeByProduct(String product) {
		Log.info("getEmployeeByProduct()::START");
		Session session = HibernateUtils.getHibernateSession();
		Employee[] employees = null;
		try {
			Query query = session
					.createSQLQuery(" selecte.emplid as emplId,"
							+ " e.area_cd as areaCd,e.area_desc as areaDesc, "
							+ " e.region_cd as regionCd,"
							+ " e.region_desc as regionDesc,"
							+ " e.district_id as districtId, "
							+ " e.district_desc as districtDesc,"
							+ " e.territory_id as territoryId,"
							+ " e.territory_role_cd as role, "
							+ "	e.team_cd as teamCode,"
							+ " e.cluster_cd as clusterCode, "
							+ " e.last_name as lastName,	"
							+ " e.first_name as firstName,"
							+ " e.middle_name as middleName,"
							+ " e.preferred_name as preferredName "
							+ " from v_new_field_employee e,"
							+ " v_training_required p where e.emplid = :p.emplid and p.product_cd =:product ");

			query.setParameter("product", product);
			List list = query.list();
			List<Employee> templist = new ArrayList<Employee>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Employee employee = new Employee();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					employee.setEmplId(field[0].toString());
				if (field[1] != null)
					employee.setAreaCd(field[1].toString());
				if (field[2] != null)
					employee.setAreaDesc(field[2].toString());
				if (field[3] != null)
					employee.setRegionCd(field[3].toString());
				if (field[4] != null)
					employee.setRegionDesc(field[4].toString());
				if (field[5] != null)
					employee.setDistrictId(field[5].toString());
				if (field[6] != null)
					employee.setDistrictDesc(field[6].toString());
				if (field[7] != null)
					employee.setTerritoryId(field[7].toString());
				if (field[8] != null)
					employee.setRole(field[8].toString());
				if (field[9] != null)
					employee.setTeamCode(field[9].toString());
				if (field[10] != null)
					employee.setClusterCode(field[10].toString());
				if (field[11] != null)
					employee.setLastName(field[11].toString());
				if (field[12] != null)
					employee.setFirstName(field[12].toString());
				if (field[13] != null)
					employee.setMiddleName(field[13].toString());
				if (field[14] != null)
					employee.setPreferredName(field[14].toString());
				templist.add(employee);
			}
			employees = templist.toArray(new Employee[templist.size()]);
			Log.info("getEmployeeByProduct()::END");
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getEmployeeByProduct() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return employees;
	}

	public Employee[] getEmployeeByProductArea(String product, String area) {
		Log.info("getEmployeeByProductArea()::START");
		Session session = HibernateUtils.getHibernateSession();
		Employee[] employees = null;
		try {
			Query query = session
					.createSQLQuery(" select e.emplid as emplId, "
							+ " e.area_cd as areaCd, "
							+ " e.area_desc as areaDesc, "
							+ " e.region_cd as regionCd, "
							+ " e.region_desc as regionDesc, "
							+ " e.district_id as districtId, "
							+ " e.district_desc as districtDesc, "
							+ " e.territory_id as territoryId, "
							+ "	e.territory_role_cd as role, "
							+ " e.team_cd as teamCode, "
							+ "	e.cluster_cd as clusterCode, "
							+ "	e.last_name as lastName, "
							+ " e.first_name as firstName, "
							+ " e.middle_name as middleName, "
							+ "	e.preferred_name as preferredName from	v_new_field_employee e, "
							+ " v_training_required p where e.emplid = :p.emplid and e.area_cd =:area and p.product_cd =:product ");

			query.setParameter("product", product);
			query.setParameter("area", area);
			List list = query.list();
			List<Employee> templist = new ArrayList<Employee>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Employee employee = new Employee();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					employee.setEmplId(field[0].toString());
				if (field[1] != null)
					employee.setAreaCd(field[1].toString());
				if (field[2] != null)
					employee.setAreaDesc(field[2].toString());
				if (field[3] != null)
					employee.setRegionCd(field[3].toString());
				if (field[4] != null)
					employee.setRegionDesc(field[4].toString());
				if (field[5] != null)
					employee.setDistrictId(field[5].toString());
				if (field[6] != null)
					employee.setDistrictDesc(field[6].toString());
				if (field[7] != null)
					employee.setTerritoryId(field[7].toString());
				if (field[8] != null)
					employee.setRole(field[8].toString());
				if (field[9] != null)
					employee.setTeamCode(field[9].toString());
				if (field[10] != null)
					employee.setClusterCode(field[10].toString());
				if (field[11] != null)
					employee.setLastName(field[11].toString());
				if (field[12] != null)
					employee.setFirstName(field[12].toString());
				if (field[13] != null)
					employee.setMiddleName(field[13].toString());
				if (field[14] != null)
					employee.setPreferredName(field[14].toString());
				templist.add(employee);

				employees = templist.toArray(new Employee[templist.size()]);
				Log.info("getEmployeeByProductArea()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("getEmployeeByProductArea() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return employees;
	}

	public Employee[] getEmployeeByProductDistrict(String product, String area,
			String region, String district) {
		Log.info("getEmployeeByProductDistrict()::START");
		Session session = HibernateUtils.getHibernateSession();
		Employee[] employees = null;
		try {
			Query query = session
					.createSQLQuery(" select e.emplid as emplId, "
							+ " e.area_cd as areaCd, "
							+ " e.area_desc as areaDesc, "
							+ " e.region_cd as regionCd, "
							+ " e.region_desc as regionDesc, "
							+ " e.district_id as districtId, "
							+ " e.district_desc as districtDesc, "
							+ " e.territory_id as territoryId, "
							+ "	e.territory_role_cd as role, "
							+ " e.team_cd as teamCode, "
							+ " e.cluster_cd as clusterCode, "
							+ " e.last_name as lastName, "
							+ " e.first_name as firstName, "
							+ " e.middle_name as middleName, "
							+ " e.preferred_name as preferredName from v_new_field_employee e, "
							+ " fft_product_assignment p where e.cluster_cd =: p.cluster_cd  "
							+ " and e.team_cd =: p.team_cd	and e.area_cd = :areaand  "
							+ " e.region_cd = :region and e.district_id = :districtand p.product_cd = :product ");

			query.setParameter("product", product);
			query.setParameter("area", area);
			query.setParameter("region", region);
			query.setParameter("district", district);
			List list = query.list();
			List<Employee> templist = new ArrayList<Employee>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Employee employee = new Employee();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					employee.setEmplId(field[0].toString());
				if (field[1] != null)
					employee.setAreaCd(field[1].toString());
				if (field[2] != null)
					employee.setAreaDesc(field[2].toString());
				if (field[3] != null)
					employee.setRegionCd(field[3].toString());
				if (field[4] != null)
					employee.setRegionDesc(field[4].toString());
				if (field[5] != null)
					employee.setDistrictId(field[5].toString());
				if (field[6] != null)
					employee.setDistrictDesc(field[6].toString());
				if (field[7] != null)
					employee.setTerritoryId(field[7].toString());
				if (field[8] != null)
					employee.setRole(field[8].toString());
				if (field[9] != null)
					employee.setTeamCode(field[9].toString());
				if (field[10] != null)
					employee.setClusterCode(field[10].toString());
				if (field[11] != null)
					employee.setLastName(field[11].toString());
				if (field[12] != null)
					employee.setFirstName(field[12].toString());
				if (field[13] != null)
					employee.setMiddleName(field[13].toString());
				if (field[14] != null)
					employee.setPreferredName(field[14].toString());
				templist.add(employee);

				employees = templist.toArray(new Employee[templist.size()]);
				Log.info("getEmployeeByProductDistrict()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getEmployeeByProductDistrict() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return employees;
	}

	public Territory getTerritoryArea(String area) {
		Log.info("getTerritoryArea()::START");
		Session session = HibernateUtils.getHibernateSession();
		Territory territory = null;

		try {
			Query query = session.createSQLQuery(" select area_cd as areaCd,"
					+ "	area_desc as areaDesc," + "	'All' as regionCd, "
					+ " 'All' as regionDesc, " + " 'All' as districtId, "
					+ " 'All' as districtDesc, "
					+ " 'All' as territoryId from v_new_field_employee  "
					+ " where area_cd =:area  and rownum=:1  ");

			query.setParameter("area", area);
			List list = query.list();
			List<Territory> templist = new ArrayList<Territory>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				territory = new Territory();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					territory.setAreaCd(field[0].toString());
				if (field[1] != null)
					territory.setAreaDesc(field[1].toString());
				if (field[2] != null)
					territory.setRegionCd(field[2].toString());
				if (field[3] != null)
					territory.setRegionDesc(field[3].toString());
				if (field[4] != null)
					territory.setDistrictId(field[4].toString());
				if (field[5] != null)
					territory.setDistrictDesc(field[5].toString());
				if (field[5] != null)
					territory.setTerritoryId(field[6].toString());
				templist.add(territory);
				Log.info("getTerritoryArea()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTerritoryArea() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return territory;
	}

	public Territory getTerritoryAreaRegion(String area, String region) {
		Log.info("getTerritoryAreaRegion()::START");
		Session session = HibernateUtils.getHibernateSession();
		Territory territory = null;
		try {
			Query query = session
					.createSQLQuery("select distinct area_cd as areaCd,"
							+ " area_desc as areaDesc,"
							+ "	region_cd as regionCd,"
							+ " region_desc as regionDesc,"
							+ " 'All' as districtId,"
							+ " 'All' as districtDesc,"
							+ " 'All' as territoryId from	v_new_field_employee where  area_cd =:area "
							+ "  and region_cd =:region and ROWNUM=:1 ");

			query.setParameter("area", area);
			query.setParameter("region", region);
			List list = query.list();
			List<Territory> templist = new ArrayList<Territory>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				territory = new Territory();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					territory.setAreaCd(field[0].toString());
				if (field[1] != null)
					territory.setAreaDesc(field[1].toString());
				if (field[2] != null)
					territory.setRegionCd(field[2].toString());
				if (field[3] != null)
					territory.setRegionDesc(field[3].toString());
				if (field[4] != null)
					territory.setDistrictId(field[4].toString());
				if (field[5] != null)
					territory.setDistrictDesc(field[5].toString());
				if (field[6] != null)
					territory.setTerritoryId(field[6].toString());

				templist.add(territory);
				Log.info("getTerritoryAreaRegion()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTerritoryAreaRegion() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return territory;
	}

	public Territory getTerritoryAreaRegionDistrict(String area, String region,
			String district) {
		Log.info("getTerritoryAreaRegionDistrict()::START");
		Session session = HibernateUtils.getHibernateSession();
		Territory territory = null;
		try {
			Query query = session
					.createSQLQuery(" select area_cd as areaCd, "
							+ " area_desc as areaDesc "
							+ " region_cd as regionCd, "
							+ " region_desc as regionDesc, "
							+ " district_id as districtId, "
							+ " district_desc as districtDesc, "
							+ " territory_id as territoryId  from v_new_field_employee "
							+ " where  area_cd =:area and region_cd = :region and "
							+ " district_id =:district and ROWNUM =:1 ");

			query.setParameter("area", area);
			query.setParameter("region", region);
			query.setParameter("district", district);
			List ll = query.list();
			List<Territory> templist = new ArrayList<Territory>();
			templist.clear();
			Iterator it = ll.iterator();
			while (it.hasNext()) {
				territory = new Territory();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					territory.setAreaCd(field[0].toString());
				if (field[1] != null)
					territory.setAreaDesc(field[1].toString());
				if (field[2] != null)
					territory.setRegionCd(field[2].toString());
				if (field[3] != null)
					territory.setRegionDesc(field[3].toString());
				if (field[4] != null)
					territory.setDistrictId(field[4].toString());
				if (field[5] != null)
					territory.setDistrictDesc(field[5].toString());
				if (field[6] != null)
					territory.setTerritoryId(field[6].toString());
				templist.add(territory);
				Log.info("getTerritoryAreaRegionDistrict()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.info("getTerritoryAreaRegionDistrict() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return territory;
	}

	public Territory[] getTerritoryByArea(String areaCd, String cluster) {
		Log.info("getTerritoryByArea()::START");
		Session session = HibernateUtils.getHibernateSession();
		Territory[] territories = null;
		try {
			Query query = session
					.createSQLQuery(" select area_cd as areaCd, "
							+ "	area_desc as areaDesc, "
							+ "	region_cd as regionCd, "
							+ " region_desc as regionDesc, "
							+ "	district_id as districtId, "
							+ "	district_desc as districtDesc, "
							+ "	territory_id as territoryId  from "
							+ "	v_new_field_employee where 	cluster_cd =:cluster and area_cd =:areaCd ");

			query.setParameter("areaCd", areaCd);
			query.setParameter("cluster", cluster);
			List list = query.list();
			List<Territory> templist = new ArrayList<Territory>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Territory territory = new Territory();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					territory.setAreaCd(field[0].toString());
				if (field[1] != null)
					territory.setAreaDesc(field[1].toString());
				if (field[2] != null)
					territory.setRegionCd(field[2].toString());
				if (field[3] != null)
					territory.setRegionDesc(field[3].toString());
				if (field[4] != null)
					territory.setDistrictId(field[4].toString());
				if (field[5] != null)
					territory.setDistrictDesc(field[5].toString());
				if (field[6] != null)
					territory.setTerritoryId(field[6].toString());
				templist.add(territory);
				territories = templist.toArray(new Territory[templist.size()]);
				Log.info("getTerritoryByArea()::END");
			}
		} catch (HibernateException e) {

			e.printStackTrace();
			Log.error("getTerritoryByArea() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return territories;
	}

	public Territory[] getTerritoryByAreReg(String area, String region,
			String cluster) {
		Log.info("getTerritoryByAreReg()::START");
		Session session = HibernateUtils.getHibernateSession();
		Territory[] territories = null;
		try {
			Query query = session
					.createSQLQuery(" select area_cd as areaCd, "
							+ " area_desc as areaDesc, "
							+ " region_cd as regionCd, "
							+ " region_desc as regionDesc, "
							+ " district_id as districtId, "
							+ "	district_desc as districtDesc, "
							+ " territory_id as territoryId from v_new_field_employee "
							+ " where region_cd = :region and area_cd =:area and cluster_cd =:cluster ");

			query.setParameter("area", area);
			query.setParameter("region", region);
			query.setParameter("cluster", cluster);
			String res = (String) query.uniqueResult();
			Log.info("getTerritoryByAreReg()" + res);
			List list = query.list();
			List<Territory> templist = new ArrayList<Territory>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Territory territory = new Territory();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					territory.setAreaCd(field[0].toString());
				if (field[1] != null)
					territory.setAreaDesc(field[1].toString());
				if (field[2] != null)
					territory.setRegionCd(field[2].toString());
				if (field[3] != null)
					territory.setRegionDesc(field[3].toString());
				if (field[4] != null)
					territory.setDistrictId(field[4].toString());
				if (field[5] != null)
					territory.setDistrictDesc(field[5].toString());
				if (field[6] != null)
					territory.setTerritoryId(field[6].toString());
				templist.add(territory);
				territories = templist.toArray(new Territory[templist.size()]);
				Log.info("getTerritoryByAreReg()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTerritoryByAreReg() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return territories;
	}

	public Territory[] getTerritoryByEmpId(String emplid) {
		Log.info("getTerritoryByEmpId()::START");
		Session session = HibernateUtils.getHibernateSession();
		Territory[] territories = null;
		try {
			Query query = session
					.createSQLQuery(" select area_cd as areaCd, "
							+ "	area_desc as areaDesc, "
							+ "	region_cd as regionCd, "
							+ "	region_desc as regionDesc, "
							+ " district_id as districtId, "
							+ "	district_desc as districtDesc,"
							+ " territory_id as territoryId from v_new_field_employee  where  emplid =:emplid ");

			query.setParameter("emplid", emplid);
			String res = (String) query.uniqueResult();
			Log.info("getTerritoryByEmpId()" + res);
			List list = query.list();
			List<Territory> templist = new ArrayList<Territory>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Territory territory = new Territory();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					territory.setAreaCd(field[0].toString());
				if (field[1] != null)
					territory.setAreaDesc(field[1].toString());
				if (field[2] != null)
					territory.setRegionCd(field[2].toString());
				if (field[3] != null)
					territory.setRegionDesc(field[3].toString());
				if (field[4] != null)
					territory.setDistrictId(field[4].toString());
				if (field[5] != null)
					territory.setDistrictDesc(field[5].toString());
				if (field[6] != null)
					territory.setTerritoryId(field[6].toString());
				templist.add(territory);
				territories = templist.toArray(new Territory[templist.size()]);
				Log.info("getTerritoryByEmpId()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTerritoryByEmpId() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return territories;
	}

	public Territory[] getTerritoryByNational(String cluster) {
		Log.info("getTerritoryByNational()::START");
		Session session = HibernateUtils.getHibernateSession();
		Territory[] territories = null;
		try {
			Query query = session
					.createSQLQuery(" select area_cd as areaCd,"
							+ " area_desc as areaDesc, "
							+ " region_cd as regionCd, "
							+ "	region_desc as regionDesc, "
							+ " district_id as districtId, "
							+ "	district_desc as districtDesc, "
							+ " territory_id as territoryId from  v_new_field_employee where cluster_cd =:cluster ");

			query.setParameter("cluster", cluster);
			List list3 = query.list();
			List<Territory> templist = new ArrayList<Territory>();
			templist.clear();
			Iterator it = list3.iterator();
			while (it.hasNext()) {
				Territory territory = new Territory();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					territory.setAreaCd(field[0].toString());
				if (field[1] != null)
					territory.setAreaDesc(field[1].toString());
				if (field[2] != null)
					territory.setRegionCd(field[2].toString());
				if (field[3] != null)
					territory.setRegionDesc(field[3].toString());
				if (field[4] != null)
					territory.setDistrictId(field[4].toString());
				if (field[5] != null)
					territory.setDistrictDesc(field[5].toString());
				if (field[6] != null)
					territory.setTerritoryId(field[6].toString());
				templist.add(territory);
				territories = templist.toArray(new Territory[templist.size()]);
				Log.info("getTerritoryByNational()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTerritoryByNational() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return territories;
	}

	public PassFail[] getTestScoreByArea(String productCode, String area) {
		Log.info("getTestScoreByArea()::START");
		Session session = HibernateUtils.getHibernateSession();
		PassFail[] fails = null;
		try {
			Query query = session
					.createSQLQuery(" select distinct fe.EMPLID as emplid, "
							+ " decode(pTest.EXAM_NAME,null,'Not taken', pTest.EXAM_NAME) as examName,"
							+ " decode (score.SET_PASSED,null,decode(pTest.EXAM_NAME,null,'Not taken','Failed'),'Passed') "
							+ " as status from V_PEDAGOGUE_EXAM  pTest, v_new_field_employee fe, v_training_required ep,"
							+ " (select distinct  s.SET_PASSED, s.EMPLID, s.SET_ID from pedagogue_scores s where s.SET_PASSED = :1) "
							+ " score where fe.EMPLID =: ep.EMPLID	and ep.EMPLID =: pTest.EMPLID(+)	and ep.PRODUCT_CD =:pTest.product_cd(+)"
							+ "	and score.SET_ID(+) = :pTest.SET_ID	and pTest.EMPLID = :score.EMPLID(+)	and ep.PRODUCT_CD =:productCode}and fe.AREA_CD =:area ");

			query.setParameter("productCode", productCode);
			query.setParameter("area", area);
			List list = query.list();
			List<PassFail> templist = new ArrayList<PassFail>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				PassFail passFail = new PassFail();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					passFail.setEmplid(field[0].toString());
				if (field[1] != null)
					passFail.setExamName(field[1].toString());
				if (field[2] != null)
					passFail.setStatus(field[2].toString());

				templist.add(passFail);
				Log.info("getTestScoreByArea()" + passFail);
				fails = templist.toArray(new PassFail[templist.size()]);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTestScoreByArea() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return fails;
	}

	public PassFail[] getTestScoreByDistrict(String productCode, String area,
			String region, String district) {
		Log.info("getTestScoreByDistrict()::START");
		Session session = HibernateUtils.getHibernateSession();
		PassFail[] fails = null;
		try {
			Query query = session
					.createSQLQuery(" select  fe.EMPLID as emplid, "
							+ " decode(pTest.EXAM_NAME,null,'Not taken', pTest.EXAM_NAME) as examName,"
							+ "  decode (score.SET_PASSED,null,decode(pTest.EXAM_NAME,null,'Not taken','Failed'),'Passed') "
							+ " as status from	V_PEDAGOGUE_EXAM  pTest, v_new_field_employee fe, v_training_required ep,"
							+ "	(select distinct  s.SET_PASSED, s.EMPLID, s.SET_ID from pedagogue_scores s where s.SET_PASSED = 1)  score "
							+ "	where 	fe.EMPLID = ep.EMPLID	and ep.EMPLID = pTest.EMPLID(+)	and ep.PRODUCT_CD = pTest.product_cd(+) "
							+ "	and score.SET_ID(+) = pTest.SET_ID	and pTest.EMPLID = score.EMPLID(+) 	and ep.PRODUCT_CD =productCode "
							+ "	and fe.area_cd = :area	and fe.district_id =:district and fe.REGION_CD =:region ");

			query.setParameter("productCode", productCode);
			query.setParameter("area", area);
			query.setParameter("district", district);
			List list = query.list();
			List<PassFail> templist = new ArrayList<PassFail>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				PassFail passFail = new PassFail();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					passFail.setEmplid(field[0].toString());
				if (field[1] != null)
					passFail.setExamName(field[2].toString());
				if (field[2] != null)
					passFail.setStatus(field[3].toString());
				templist.add(passFail);
				fails = templist.toArray(new PassFail[templist.size()]);
				Log.info("getTestScoreByDistrict()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTestScoreByDistrict() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return fails;
	}

	public PassFail[] getTestScoreByProduct(String productCode) {
		Log.info("getTestScoreByProduct()::START");
		Session session = HibernateUtils.getHibernateSession();
		PassFail[] fails = null;
		try {
			Query query = session
					.createSQLQuery(" select fe.EMPLID as emplid, "
							+ " decode(pTest.EXAM_NAME,null,'Not taken', pTest.EXAM_NAME) "
							+ " as examName,  decode (score.SET_PASSED,null,decode(pTest.EXAM_NAME,null,'Not taken','Failed'),'Passed') "
							+ " as status from V_PEDAGOGUE_EXAM  pTest,	v_new_field_employee fe, v_training_required ep, "
							+ "	(select distinct  s.SET_PASSED, s.EMPLID, s.SET_ID from pedagogue_scores s where s.SET_PASSED = :1) "
							+ "  score where fe.EMPLID = :ep.EMPLID and ep.EMPLID =: pTest.EMPLID(+) and ep.PRODUCT_CD = pTest.product_cd(+) "
							+ " and score.SET_ID(+) =: pTest.SET_ID	and pTest.EMPLID = score.EMPLID(+) and ep.PRODUCT_CD = :productCode  ");

			query.setParameter("productCode", productCode);
			List list = query.list();
			List<PassFail> templist = new ArrayList<PassFail>();
			templist.clear();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				PassFail passFail = new PassFail();
				Object[] field = (Object[]) it.next();
				if (field[0] != null)
					passFail.setEmplid(field[0].toString());
				if (field[1] != null)
					passFail.setExamName(field[2].toString());
				if (field[2] != null)
					passFail.setStatus(field[3].toString());

				templist.add(passFail);
				fails = templist.toArray(new PassFail[templist.size()]);
				Log.info("getTestScoreByProduct()::END");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("getTestScoreByProduct() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return fails;
	}

	public void removeProductCourseMapping(String product, String courseCode) {
		Log.info("removeProductCourseMapping()::START");
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = session.beginTransaction();
		try {
			tx = session.beginTransaction();
			Query query = session
					.createSQLQuery(" DELETE GAP_REPORT_CODES WHERE PRODUCT = :product AND COURSE_CODE = :courseCode ");

			query.setParameter("product", product);
			query.setParameter("courseCode", courseCode);
			int prod = query.executeUpdate();
			Log.info("Number of records Deleted" + "product" + "coruseCode");
			Log.info("removeProductCourseMapping()::END");
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			Log.error("removeProductCourseMapping() Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

}
