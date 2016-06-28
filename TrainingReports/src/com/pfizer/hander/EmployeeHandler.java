package com.pfizer.hander;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.db.EmpSearch;
import com.pfizer.db.EmpSearchGNSM;
import com.pfizer.db.EmpSearchPDFHS;
import com.pfizer.db.EmpSearchPOA;
import com.pfizer.db.EmpSearchTSHT;
import com.pfizer.db.Employee;
import com.pfizer.db.GroupAccessDetail;
import com.pfizer.db.NAUserSearch;
import com.pfizer.db.Product;
import com.pfizer.db.TrainingOrder;
import com.pfizer.pgrd.csl.security.CSLSystemException;
import com.pfizer.pgrd.csl.security.PersonCriteria;
import com.pfizer.pgrd.csl.security.PersonTO;
import com.pfizer.pgrd.csl.security.Security;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.utils.ReadProperties;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.pfizer.webapp.search.EmplSearchTSHTForm;
import com.pfizer.webapp.search.NonAtlasEmployeeSearchForm;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.tgix.Utils.LoggerHelper;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;
// Added for Major Enhancement 3.6 - F1

// End: Added for Major Enhancement 3.6 - F1

public class EmployeeHandler {
	protected static final Log log = LogFactory.getLog(EmployeeHandler.class);
	ReadProperties read = new ReadProperties();
	private String productEmployeSql = " select 	 "
			+ " e.emplid as emplId, "
			+ " e.area_cd as areaCd, "
			+ " e.promotion_date as promoDate, "
			+ " e.effective_hire_date as hireDate, "
			+ " e.sex as gender, "
			+ " e.email_address as email, "
			+ " e.reports_to_emplid as reportsToEmplid,"
			+ " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') AS FIELDACTIVE ,"
			+ " e.area_desc as areaDesc, " + " e.region_cd as regionCd, "
			+ " e.region_desc as regionDesc, "
			+ " e.district_id as districtId, "
			+ " e.district_desc as districtDesc, "
			+ " e.territory_id as territoryId, "
			+ " e.territory_role_cd as role, " + " e.team_cd as teamCode, "
			+ " e.cluster_cd as clusterCode, " + " e.last_name as lastName, "
			+ " e.first_name as firstName, " + " e.middle_name as middleName, "
			+ " e.preferred_name as preferredName " + " from "
			+ " v_new_field_employee e, " + " mv_training_required p "
			+ " where  " + " p.emplid = e.emplid ";

	private String searchEmployeSql = " select  "
			+ " e.emplid as emplId, "
			+ " p.product_cd as productCd, "
			+ " e.last_name as lastName, "
			+ " e.first_name as firstName, "
			+ " e.middle_name as middleName, "
			+ " e.preferred_name as preferredName , "
			+ " TRAINING_REQUIRED as trainingNeed ,"
			+ " e.TERRITORY_ROLE_CD as roleCD, "
			+ " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ,"
			+ " to_char(sub1.issued_on,'MM/DD/YYYY') exam_issued_on, "
			+ " TO_CHAR(SUB2.MATERIALDATEORDERED,'MM/DD/YYYY') MATERIALDATEORDERED ,"
			+ " e.TERRITORY_ID as territoryId  "
			+ " from "
			+ " v_new_field_employee e, "
			+ " mv_fft_products_training p , "
			+ "	(	select distinct product_cd,emplid,trunc(issued_on) issued_on  "
			+ "		from "
			+ "			FFT_PRODUCT_PEDAGOGUE_MAP pedmap,"
			+ "			fft_pedagogue_test_history fpt "
			+ "		where "
			+ "			pedmap.set_id=fpt.SET_ID ) sub1, "
			+ "	(	SELECT DISTINCT MAX(TRUNC(DATEORDERED)) MATERIALDATEORDERED,FCR.PRODUCT_CD ,PERSON_ID AS EMPLID "
			+ "		FROM " + "			FFT_MATERIAL_ORDER_HISTORY FMO,"
			+ "			FFT_CLUSTER_ROLE_PROD_TM FCR  " + "		WHERE "
			+ "			FMO.INV_ID=FCR.TRAINING_MATERIAL_ID "
			+ "			AND PRODUCT_CD IS NOT NULL  " + "		GROUP BY "
			+ "			PRODUCT_CD ,PERSON_ID ) SUB2 " + " where  "
			+ " p.emplid = e.emplid " + " and p.emplid=sub1.emplid(+) "
			+ " AND p.EMPLID=SUB2.EMPLID(+) "
			+ " AND SUB2.PRODUCT_CD(+) = P.PRODUCT_CD"
			+ " and sub1.product_cd(+)=p.product_cd ";

	private String employeSql = read.getValue("employeSql");

	private String oldemployeSql = " select distinct	 "
			+ " e.emplid as emplId, "
			+ " e.guid as guid, "
			+ " e.promotion_date as promoDate, "
			+ " e.effective_hire_date as hireDate, "
			+ " e.sex as gender, "
			+ " e.geo_type_desc as geoType, "
			+ " e.email_address as email, "
			+ " e.reports_to_emplid as reportsToEmplid,"
			+ " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') AS FIELDACTIVE ,"
			+ " e.area_cd as areaCd, " + " e.area_desc as areaDesc, "
			+ " e.region_cd as regionCd, " + " e.region_desc as regionDesc, "
			+ " e.district_id as districtId, "
			+ " e.district_desc as districtDesc, "
			+ " e.territory_id as territoryId, "
			+ " e.territory_role_cd as role, " + " e.team_cd as teamCode, "
			+ " e.cluster_cd as clusterCode, " + " e.last_name as lastName, "
			+ " e.first_name as firstName, " + " e.middle_name as middleName, "
			+ " e.preferred_name as preferredName " + " from "
			+ " v_new_field_employee e " + " where   ";
	/*
	 * private String employeSql = " select distinct	 " +
	 * " e.emplid as emplId, " + " e.guid as guid, " +
	 * " e.promotion_date as promoDate, " +
	 * " e.effective_hire_date as hireDate, " + " e.sex as gender, " +
	 * " e.geo_type_desc as geoType, " + " e.email_address as email, " +
	 * " e.reports_to_emplid as reportsToEmplid," +
	 * " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') AS FIELDACTIVE ,"
	 * + " e.area_cd as areaCd, " + " e.area_desc as areaDesc, " +
	 * " e.region_cd as regionCd, " + " e.region_desc as regionDesc, " +
	 * " e.district_id as districtId, " + " e.district_desc as districtDesc, " +
	 * " e.territory_id as territoryId, " + " e.territory_role_cd as role, " +
	 * " e.team_cd as teamCode, " + " e.cluster_cd as clusterCode, " +
	 * " e.last_name as lastName, " + " e.first_name as firstName, " +
	 * " e.middle_name as middleName, " + " e.preferred_name as preferredName "
	 * + " from " + " v_new_field_employee e " + " where   ";
	 */
	private String basicemployeSql = " select distinct	 "
			+ " e.emplid as emplId, "
			+ " e.guid as guid, "
			+ " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') AS FIELDACTIVE "
			+ " from " + " v_new_field_employee e " + " where   ";

	private String searchPOAEmployeeSql = " select "
			+ " TO_CHAR(e.DATE_CREATED,'MM/DD/YY') as completedDate, "
			+ " DECODE(e.STATUS,'P','Completed','I','Not Completed','L','On Leave') as examStatus, "
			+ " DECODE(e.STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatus, "
			+ " e.emplid as emplId, "
			+ " e.product_cd as productCd,"
			+ " e.last_name as lastName, "
			+ " e.first_name as firstName,"
			+ " e.middle_name as middleName,"
			+ " e.preferred_name as preferredName , "
			+ " e.TERRITORY_ROLE_CD as roleCD,"
			+ " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ,"
			+ " e.TERRITORY_ID as territoryId"
			+ " from v_powers_midpoa1_data e ";

	/*
	 * private String searchPDFHSEmployeeSql = " select distinct" +
	 * " TO_CHAR(e.EXAM_TAKEN_DATE,'MM/DD/YY') as completedDate, " +
	 * " TO_CHAR(eplc.EXAM_TAKEN_DATE,'MM/DD/YY') as completedDatePLC, " +
	 * " DECODE(e.STATUS,'P','Completed','I','Not Completed','L','On Leave') as examStatus, "
	 * +
	 * " DECODE(eplc.STATUS,'P','Completed','I','Not Completed','L','On Leave') as plcexamStatus, "
	 * +
	 * " DECODE(e.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatus, "
	 * +
	 * " DECODE(eplc.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatusPLC, "
	 * + " e.emplid as emplId, " + " e.product_cd as productCd," +
	 * " e.last_name as lastName, " + " e.first_name as firstName,"+
	 * //" e.middle_name as middleName,"+
	 * //" e.preferred_name as preferredName , "+
	 * //" e.TERRITORY_ROLE_CD as roleCD," +
	 * " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ,"
	 * + " e.TERRITORY_ID as territoryId"+
	 * " from v_pwra_hs_data_overall e inner join v_pwra_plc_data_overall eplc on "
	 * + " e.emplid = eplc.emplid and " + " e.product_cd = eplc.product_cd and "
	 * + " e.last_name = eplc.last_name and " +
	 * " e.first_name = eplc.first_name and " +
	 * " e.territory_id = eplc.territory_id ";
	 */

	private String searchPDFHSEmployeeSql = "select distinct "
			+ " decode(hs.status,'I','', hs.exam_taken_date) as completedDate, "
			+ " decode(e.status,'I','', e.exam_taken_date) as completedDatePLC, "
			+ " DECODE(hs.STATUS,'P','Completed','I','Not Completed','L','On Leave') as examStatus, "
			+ " DECODE(e.STATUS,'P','Completed','I','Not Completed','L','On Leave') as plcexamStatus, "
			+ " DECODE(hs.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatus, "
			+ " DECODE(e.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatusPLC, "
			+ " e.emplid as emplId, "
			+ " (CASE WHEN e.product_cd = 'PLCA' THEN 'General Session' ELSE e.product_cd END) AS productCd, "
			+ " e.last_name as lastName, "
			+ " e.first_name as firstName,"
			+ " e.ROLE_CD as roleCD,"
			+ " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ,"
			+ " e.TERRITORY_ID as territoryId"
			+ " from (select sub1.emplid,sub1.product_cd,sub1.status,OAL.overall_status, "
			+ " OAL.last_name, OAL.first_name, OAL.role_cd, OAL.empl_status, OAL.territory_id, OAL.area_cd, OAL.region_cd, "
			+ " OAL.district_id, OAL.cluster_cd, OAL.exam_taken_date from "
			+ " (select comp_list.*,'P' as status from (select distinct emplid,product_cd from v_pwra_plc_data_overall "
			+ " minus "
			+ " select distinct emplid,product_cd from v_pwra_plc_data_overall where status = 'I' or status = 'L')comp_list "
			+ " union "
			+ " select incomp_list.*, 'I' as status from (select distinct emplid,product_cd from v_pwra_plc_data_overall where status = 'I' or status = 'L')incomp_list)sub1,"
			+ " (select emplid,product_cd,overall_status,last_name, first_name, role_cd, empl_status, territory_id, area_cd, region_cd, district_id, cluster_cd,max(exam_taken_date) as exam_taken_date "
			+ " from v_pwra_plc_data_overall group by emplid,product_cd,overall_status, last_name, first_name, role_cd, empl_status, territory_id, area_cd, region_cd, district_id, cluster_cd)OAL "
			+ " where sub1.emplid = OAL.emplid AND sub1.product_cd = OAL.product_cd)e, v_pwra_hs_data_overall hs "
			+ " where e.emplid = hs.emplid(+) and "
			+ " e.product_cd = hs.product_cd(+) ";

	private String searchSPFHSEmployeeSql = "select distinct "
			+ " null as completedDate, "
			+ " decode(sub1.status,'I','', e.exam_taken_date) as completedDatePLC, "
			+ " null as examStatus, "
			+ " DECODE(sub1.STATUS,'P','Completed','I','Not Completed','L','On Leave') as plcexamStatus, "
			+ " null as overallExamStatus, "
			+ " DECODE(e.overall_STATUS,'P','Completed','I','Not Completed','L','On Leave') as overallExamStatusPLC, "
			+ " e.emplid as emplId, "
			+ " (CASE WHEN e.product_cd = 'PLCA' THEN 'General Session' ELSE e.product_cd END) AS productCd, "
			+ " e.last_name as lastName, "
			+ " e.first_name as firstName,"
			+ " e.ROLE_CD as roleCD,"
			+ " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE ,"
			+ " e.TERRITORY_ID as territoryId"
			+ " FROM "
			+ " (SELECT comp_list.*,'P' AS status FROM "
			+ " (SELECT DISTINCT emplid,product_cd FROM v_spf_plc_data_overall "
			+ " MINUS "
			+ " SELECT DISTINCT emplid,product_cd FROM v_spf_plc_data_overall WHERE status = 'I' or status = 'L')comp_list "
			+ " UNION "
			+ " SELECT incomp_list.*, 'I' AS status FROM (SELECT DISTINCT emplid,product_cd FROM v_spf_plc_data_overall WHERE status = 'I' or status = 'L') incomp_list) "
			+ " sub1, "
			+ " (SELECT emplid,product_cd,overall_status, last_name, first_name, role_cd, empl_status, territory_id, area_cd, region_cd, district_id, cluster_cd, MAX(exam_taken_date) AS exam_taken_date "
			+ " FROM v_spf_plc_data_overall GROUP BY emplid,product_cd,overall_status, last_name, first_name, role_cd, empl_status, territory_id, area_cd, region_cd, district_id, cluster_cd) e  "
			+ " WHERE sub1.emplid = e.emplid AND "
			+ " sub1.product_cd = e.product_cd ";

	private String employeeOrderByProdSql = " SELECT DISTINCT TO_CHAR(MAX(TRUNC (dateordered)),'MM/DD/YYYY') materialdateordered, "
			+ " substr(max(source_order_id),4) as orderid "
			+ " FROM fft_material_order_history fmo, "
			+ " fft_cluster_role_prod_tm fcr "
			+ " WHERE fmo.inv_id = fcr.training_material_id "
			+ " AND product_cd =? "
			+ " AND person_id=? "
			+ " GROUP BY product_cd, person_id ";

	private String searchSimulateUserSql = " select distinct "
			+ " e.emplid as emplId, "
			+ " e.last_name as lastName, "
			+ " e.first_name as firstName, "
			+ " e.middle_name as middleName, "
			+ " e.preferred_name as preferredName , "
			+
			/* Adding columns for RBU changes */
			" e.sales_position_id as sales_position_id , "
			+ " e.email_address as email , "
			+ " e.bu as bu , "
			+ " e.sales_group as salesOrg , "
			+
			/* End of addition */
			" e.role_cd as roleCD, "
			+ " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE "
			+ " from " +
			/* Modified for CSO Enhancement */
			// " mv_field_employee_rbu e,mv_field_employee_rbu emgr where (e.emplid = emgr.reports_to_emplid or e.role_cd in (select role_cd from special_roles))"
			// ;
			// Modified for TRT Phase 2 - Requirement F3 HQ users
			// " mv_field_employee_rbu e,mv_field_employee_relation r where (e.emplid = r.related_emplid or e.role_cd in (select role_cd from special_roles))"
			// ;
			" mv_field_employee_rbu e,mv_field_employee_relation r where (e.emplid = r.related_emplid or e.role_cd in (select role_cd from special_roles) OR e.sales_position_type_cd='HSM')";

	// Start: Start for TRT Phase 2 - Requirement No. F4
	private String searchAllEmployees = " select distinct "
			+ " e.emplid as emplId, "
			+ " e.last_name as lastName, "
			+ " e.first_name as firstName, "
			+ " e.middle_name as middleName, "
			+ " e.preferred_name as preferredName , "
			+
			/* Adding columns for RBU changes */
			" e.sales_position_id as sales_position_id , "
			+ " e.email_address as email , "
			+ " e.bu as bu , "
			+ " e.sales_group as salesOrg , "
			+
			/* End of addition */
			" e.role_cd as roleCD, "
			+ " DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave') AS FIELDACTIVE "
			+ " from " +
			/* Modified for CSO Enhancement */
			// " mv_field_employee_rbu e,mv_field_employee_rbu emgr where (e.emplid = emgr.reports_to_emplid or e.role_cd in (select role_cd from special_roles))"
			// ;
			" mv_field_employee_rbu e where (e.sex in('M','F'))";

	public EmployeeHandler() {
	}

	public Employee getEmployeeById(String id) {
		System.out.print("Inside the getEmployeeById method");
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[1];
		criteria.append(" e.emplid = ? "); // criteria.append(" e.emplid = ? ");

		String sql = employeSql + criteria.toString();
		System.out.print(sql);
		params[0] = id;

		Employee[] tmp = getEmployees(sql, params);

		if (tmp != null && tmp.length >= 1) {
			employee = tmp[0];
		} else {
			return null;
		}

		return employee;
	}

	/*
	 * Log: Added by Meenakshi.M.B on 14-May-2010 Method added to get the P2L
	 * scores visibility
	 */
	public String getScoresVisiblity(String type) {
		String visibility = "";
		ReadProperties props = new ReadProperties();
		int flag = 0;
		ResultSet rs = null;
		PreparedStatement statement = null;
		// Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			String visibilitySql = props.getValue("visibilityQuery");
			System.out.println("P2L Scores--" + visibilitySql);
			statement = conn.prepareStatement(visibilitySql);
			statement.setString(1, type);
			rs = statement.executeQuery();
			int i = 0;
			System.out.println("Result Set--Fetch Size");
			while (rs.next()) {
				visibility = rs.getString("P2L_SCORES_VISIBLE_FOR_ALL");
			}
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		System.out.println("Returning visibility-----" + visibility);
		return visibility;
	}

	/* Adding new method for retaining values for Special Events */
	public Employee getOldEmployeeById(String id) {
		System.out.print("Inside the getOldEmployeeById method");
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[1];
		criteria.append(" e.emplid = ? ");

		String sql = oldemployeSql + criteria.toString();
		System.out.print(sql);
		params[0] = id;

		Employee[] tmp = getEmployees(sql, params);

		if (tmp != null && tmp.length >= 1) {
			employee = tmp[0];
		} else {
			return null;
		}

		return employee;
	}

	public String getExemptionReason(String emplid, String product) {

		String retString = null;

		ResultSet rs = null;
		Statement st = null;
		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);

			String var1 = "SELECT DISTINCT EXEMPTION_REASON"
					+ " FROM   MV_FFT_PRODUCTS_TRAINING"
					+ " WHERE  TRAINING_REQUIRED = 'Exempted'"
					+ "        AND UPPER(PRODUCT_CD) = UPPER('" + product
					+ "')" + "        AND EMPLID = '" + emplid + "'";

			log.info("The Exemption Query is**" + var1);

			rs = st.executeQuery(var1);

			if (rs.next()) {
				retString = rs.getString("EXEMPTION_REASON");
				log.info("The Exemption reason is**" + retString);
			}
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}

		return retString;

	}

	public EmpSearchTSHT[] getTSHTEmployees(EmplSearchTSHTForm eform,
			UserSession uSession) {

		EmpSearchTSHT[] ret = null;
		String sql = "SELECT DISTINCT EMPLID,LAST_NAME,FIRST_NAME ";
		sql = sql + " FROM V_TS_LEGACY_DATA e  ";

		StringBuffer criteria = new StringBuffer();

		TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

		boolean firstWhere = true;

		// if(!Util.isEmpty(eform.getFname())||!Util.isEmpty(
		// eform.getLname())){
		if (!Util.isEmpty(eform.getEmplid())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.emplid='" + eform.getEmplid().trim() + "'");
		}
		if (!Util.isEmpty(eform.getFname())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" (upper(e.first_name) like '"
					+ eform.getFname().toUpperCase().trim() + "%'  ) ");
		}
		if (!Util.isEmpty(eform.getLname())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" upper(e.last_name) like '"
					+ eform.getLname().toUpperCase().trim() + "%' ");
		}
		if (!Util.isEmpty(eform.getCourseName())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" upper(e.course_name) like '"
					+ eform.getCourseName().toUpperCase().trim() + "%' ");
		}
		// }else if(!Util.isEmpty(eform.getEmplid())){
		// }

		criteria.append(" order by e.last_name,e.first_name ");

		sql = sql + criteria;

		log.info("Search SQL FOR THE EMPLOYEE---" + sql);
		System.out.println("Search SQL FOR THE EMPLOYEE:" + sql);

		ResultSet rs = null;
		Statement st = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();

		try {

			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * 
			 * conn = ds.getConnection();
			 */
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			// log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearchTSHT e = new EmpSearchTSHT();
				// e.setTeamCode(rs.getString("TEAM_DESC"));
				// e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
				e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
				e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
				// e.setRole(rs.getString("ROLE_CD"));
				e.setEmplId(rs.getString("EMPLID"));
				// e.setActivityName(rs.getString("TRT_COURSE_NAME"));
				tempList.add(e);
			}

			ret = new EmpSearchTSHT[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (EmpSearchTSHT) tempList.get(j);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	public EmpSearchTSHT[] getTSHTEmployeeDetail(String emplId,
			UserSession uSession) {

		EmpSearchTSHT[] ret = null;
		String sql = "SELECT EMPLID,LAST_NAME,FIRST_NAME,HIRE_DATE,COURSE_CODE,COURSE_NAME, ";
		sql = sql
				+ "COMPLETION_DATE,SCORE,NOTES,EMPL_STATUS,FIELD_ACTIVE,TERRITORY_ROLE_CD, ";
		sql = sql + "COURSE_STATUS,CREDITS ";
		sql = sql + " FROM V_TS_LEGACY_DATA where emplid='" + emplId
				+ "' ORDER BY COURSE_CODE";

		TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

		log.info("Search SQL FOR THE EMPLOYEE---" + sql);

		ResultSet rs = null;
		Statement st = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();

		try {

			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * 
			 * conn = ds.getConnection();
			 */
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			// log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearchTSHT e = new EmpSearchTSHT();
				// e.setTeamCode(rs.getString("TEAM_DESC"));
				// e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
				e.setEmplId(rs.getString("EMPLID"));
				e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
				e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
				e.setHireDate(rs.getDate("HIRE_DATE"));
				e.setCourceCode(rs.getString("COURSE_CODE".toUpperCase()));
				e.setCourceName(rs.getString("COURSE_NAME".toUpperCase()));
				e.setCompletionDate(rs.getDate("COMPLETION_DATE"));

				e.setEmployeeStatus(rs.getString("EMPL_STATUS".toUpperCase()));
				e.setFieldActive(rs.getString("FIELD_ACTIVE".toUpperCase()));
				e.setScores(rs.getString("SCORE".toUpperCase()));
				e.setNotes(rs.getString("NOTES".toUpperCase()));
				e.setHrStatus(rs.getString("EMPL_STATUS".toUpperCase()));

				e.setRole(rs.getString("TERRITORY_ROLE_CD"));
				e.setCourseStatus(rs.getString("COURSE_STATUS"));
				e.setCredits(rs.getString("CREDITS"));

				tempList.add(e);
			}

			ret = new EmpSearchTSHT[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (EmpSearchTSHT) tempList.get(j);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	public String getEmployeeImage(String id) {
		String retString = null;

		ResultSet rs = null;
		Statement st = null;
		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);

			String sql = " select " + "  photo_file_name " + " from  "
					+ " employee_pics " + " where  " + " emplid = '" + id + "'";

			rs = st.executeQuery(sql);

			if (rs.next()) {
				retString = rs.getString("photo_file_name".toUpperCase());
			}
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}

		return retString;
	}

	public Employee getAreaManager(UserFilter uFilter) {
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();

		if ("All".equals(uFilter.getFilterForm().getArea())) {
			return null;
		}
		String[] params = new String[1];

		criteria.append(" e.territory_role_cd = 'VP' ");
		criteria.append(" and e.area_cd = ? ");
		if (!uFilter.isAdmin()) {
			params = new String[2];
			criteria.append(" and e.cluster_cd = ? ");
			params[1] = uFilter.getClusterCode();
		}

		params[0] = uFilter.getFilterForm().getArea();

		String sql = employeSql + criteria.toString();

		Employee[] tmp = getEmployees(sql, params);

		if (tmp != null && tmp.length >= 1) {
			employee = tmp[0];
		} else {
			return null;
		}

		return employee;
	}

	public Employee getRegionManager(UserFilter uFilter) {
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();

		if ("All".equals(uFilter.getFilterForm().getArea())) {
			return null;
		}
		if ("All".equals(uFilter.getFilterForm().getRegion())) {
			return null;
		}

		criteria.append("  e.territory_role_cd = 'RM' ");
		criteria.append(" and e.region_cd = ? ");
		criteria.append(" and e.area_cd = ? ");

		String[] params = new String[2];
		if (!uFilter.isAdmin()) {
			params = new String[3];
			criteria.append(" and e.cluster_cd = ? ");
			params[2] = uFilter.getClusterCode();
		}

		params[0] = uFilter.getFilterForm().getRegion();
		params[1] = uFilter.getFilterForm().getArea();

		String sql = employeSql + criteria.toString();

		Employee[] tmp = getEmployees(sql, params);

		if (tmp != null && tmp.length >= 1) {
			employee = tmp[0];
		} else {
			return null;
		}

		return employee;
	}

	public Employee getARManager(UserFilter uFilter) {
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();

		if ("All".equals(uFilter.getFilterForm().getArea())) {
			return null;
		}
		if ("All".equals(uFilter.getFilterForm().getRegion())) {
			return null;
		}

		String[] params = new String[3];
		criteria.append(" e.cluster_cd = ? ");
		params[0] = uFilter.getClusterCode();
		criteria.append(" and  e.territory_role_cd = 'ARM' ");
		criteria.append(" and e.region_cd = ? ");
		params[1] = uFilter.getFilterForm().getRegion();
		criteria.append(" and e.area_cd = ? ");
		params[2] = uFilter.getFilterForm().getArea();

		String sql = employeSql + criteria.toString();

		Employee[] tmp = getEmployees(sql, params);

		if (tmp != null && tmp.length >= 1) {
			employee = tmp[0];
		} else {
			return null;
		}

		return employee;
	}

	public Employee getDistrictManager(UserFilter uFilter) {
		Employee employee = null;
		StringBuffer criteria = new StringBuffer();

		if ("All".equals(uFilter.getFilterForm().getArea())) {
			return null;
		}
		if ("All".equals(uFilter.getFilterForm().getRegion())) {
			return null;
		}
		if ("All".equals(uFilter.getFilterForm().getDistrict())) {
			return null;
		}

		criteria.append("  e.territory_role_cd = 'DM' ");
		criteria.append(" and e.area_cd = ? ");
		criteria.append(" and e.region_cd = ? ");
		criteria.append(" and e.district_id = ? ");

		String[] params = new String[3];
		if (!uFilter.isAdmin()) {
			criteria.append(" and e.cluster_cd = ? ");
			params = new String[4];
			params[3] = uFilter.getClusterCode();
		}

		params[0] = uFilter.getFilterForm().getArea();
		params[1] = uFilter.getFilterForm().getRegion();
		params[2] = uFilter.getFilterForm().getDistrict();

		String sql = employeSql + criteria.toString();

		Employee[] tmp = getEmployees(sql, params);

		if (tmp != null && tmp.length >= 1) {
			employee = tmp[0];
		} else {
			return null;
		}

		return employee;
	}

	public TrainingOrder empTrainingMaterialByProd(String emplid,
			String productFilter) {
		TrainingOrder thisTrainingOrder = new TrainingOrder();
		ResultSet rs = null;
		PreparedStatement st = null;
		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.prepareStatement(employeeOrderByProdSql);
			st.setString(2, emplid.trim());
			st.setString(1, productFilter.trim().toUpperCase());
			log.info("SQL FOR EMPTRAININGMATRIAL" + employeeOrderByProdSql);
			rs = st.executeQuery();
			String trmID;
			String shipmentDate;
			while (rs.next()) {
				trmID = rs.getString("orderid") == null ? "" : rs
						.getString("orderid");
				shipmentDate = rs.getString("materialdateordered") == null ? ""
						: rs.getString("materialdateordered");
				thisTrainingOrder.setTrmId(trmID);
				thisTrainingOrder.setOrderDate(shipmentDate);
			}
		} catch (Exception e) {
			System.out.println("Error in empTrainingMaterialByProd" + e);
			e.printStackTrace();
			log.error(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return thisTrainingOrder;
	}// end of the Method

	public Employee[] getEmployees(UserFilter uFilter) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[0];
		if (form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER) {
			criteria.append(" and p.product_cd = ? ");
			if (uFilter.isAdmin()) {
				params = new String[1];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[2];
				params[1] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER) {
			criteria.append(" and p.product_cd = ? ");
			criteria.append(" and e.area_cd = ? ");
			if (uFilter.isAdmin()) {
				params = new String[2];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[3];
				params[2] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
			params[1] = form.getArea();
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER) {
			criteria.append(" and p.product_cd = ? ");
			criteria.append(" and e.area_cd = ? ");
			criteria.append(" and e.region_cd = ? ");
			if (uFilter.isAdmin()) {
				params = new String[3];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[4];
				params[3] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
			params[1] = form.getArea();
			params[2] = form.getRegion();
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER) {
			criteria.append(" and p.product_cd = ? ");
			criteria.append(" and e.area_cd = ? ");
			criteria.append(" and e.region_cd = ? ");
			criteria.append(" and e.district_id = ? ");
			if (uFilter.isAdmin()) {
				params = new String[4];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[5];
				params[4] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
			params[1] = form.getArea();
			params[2] = form.getRegion();
			params[3] = form.getDistrict();
		}

		String sql = productEmployeSql + criteria.toString();

		// log.info(sql);
		return getEmployees(sql, params);
	}

	/**
	 * Searches users by name, will filter against employees that are managed by
	 * the user.
	 */
	public EmpSearchPOA[] getPOAEmployeesByName(EmplSearchForm eform,
			UserSession uSession) {
		StringBuffer criteria = new StringBuffer();
		List productCodes = new ArrayList();
		for (Iterator it = uSession.getUser().getProducts().iterator(); it
				.hasNext();) {
			Product prod = (Product) it.next();
			productCodes.add(prod.getProductCode());
		}

		String productStr = Util.delimit(productCodes, "','");
		productStr = "MIDPOA1"; // Hotcoded for MID POA Module
		TerritoryFilterForm form = uSession.getUserFilterForm();

		criteria.append(" where e.product_cd in ('" + productStr + "') ");

		if (!Util.isEmpty(eform.getFname())) {
			criteria.append(" and (upper(e.first_name) like '"
					+ eform.getFname().toUpperCase().trim()
					+ "%' or  upper(e.preferred_name) like '"
					+ eform.getFname().toUpperCase().trim() + "%' )");
		}
		if (!Util.isEmpty(eform.getLname())) {
			criteria.append(" and upper(e.last_name) like '"
					+ eform.getLname().toUpperCase().trim() + "%'");
		}
		if (!Util.isEmpty(eform.getEmplid())) {
			criteria.append("and e.emplid='" + eform.getEmplid().trim() + "'");
		}
		if (!Util.isEmpty(eform.getTerrId())) {
			criteria.append("and e.territory_id='" + eform.getTerrId().trim()
					+ "'");
		}

		if (form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER) {
			criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER) {
			criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER) {
			criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			criteria.append(" and e.district_id = '" + form.getDistrict()
					+ "' ");
		}
		if (!uSession.getUser().isAdmin()) {
			criteria.append(" and e.cluster_cd='"
					+ uSession.getUser().getCluster() + "'");
		}
		criteria.append(" order by e.emplid ");
		String sql = searchPOAEmployeeSql + criteria.toString();

		System.out.println("@@@@ SQL" + sql);

		log.info("Search SQL FOR THE EMPLOYEE---" + sql);
		return getPOAEmployeesSearch(sql);
	}

	/**
	 * Searches users by name, will filter against employees that are managed by
	 * the user.
	 */
	public EmpSearchPDFHS[] getPDFHSEmployeesByName(EmplSearchForm eform,
			UserSession uSession, String event) {
		StringBuffer criteria = new StringBuffer();
		List productCodes = new ArrayList();
		boolean hasProducts = false;
		for (Iterator it = uSession.getUser().getProducts().iterator(); it
				.hasNext();) {
			hasProducts = true;
			Product prod = (Product) it.next();
			productCodes.add(prod.getProductCode());
		}

		String productStr = Util.delimit(productCodes, "','");
		TerritoryFilterForm form = uSession.getUserFilterForm();

		if (hasProducts) {
			productStr = productStr + "','PLCA";
		}
		criteria.append(" and e.product_cd in ('" + productStr + "') ");

		if (!Util.isEmpty(eform.getFname())) {
			criteria.append(" and (upper(e.first_name) like '"
					+ eform.getFname().toUpperCase().trim() + "%'  ) ");
		}
		if (!Util.isEmpty(eform.getLname())) {
			criteria.append(" and upper(e.last_name) like '"
					+ eform.getLname().toUpperCase().trim() + "%' ");
		}
		if (!Util.isEmpty(eform.getEmplid())) {
			criteria.append("and e.emplid='" + eform.getEmplid().trim() + "'");
		}
		if (!Util.isEmpty(eform.getTerrId())) {
			criteria.append("and e.territory_id='" + eform.getTerrId().trim()
					+ "'");
		}

		if (form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER) {
			criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER) {
			criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER) {
			criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			criteria.append(" and e.district_id = '" + form.getDistrict()
					+ "' ");
		}
		if (!uSession.getUser().isAdmin()) {
			criteria.append(" and e.cluster_cd='"
					+ uSession.getUser().getCluster() + "'");
		}

		/*
		 * criteria.append(" and eplc.product_cd in ('" + productStr + "')");
		 * 
		 * if ( !Util.isEmpty( eform.getFname() ) ) { criteria.append(
		 * " and (upper(eplc.first_name) like '" +
		 * eform.getFname().toUpperCase().trim() + "%'  ) " ); } if (
		 * !Util.isEmpty( eform.getLname() ) ) { criteria.append(
		 * " and upper(eplc.last_name) like '" +
		 * eform.getLname().toUpperCase().trim() + "%' " ); }
		 * if(!Util.isEmpty(eform.getEmplid())){
		 * criteria.append("and eplc.emplid='"+eform.getEmplid().trim()+"'"); }
		 * if(!Util.isEmpty(eform.getTerrId())){
		 * criteria.append("and eplc.territory_id='"
		 * +eform.getTerrId().trim()+"'"); }
		 * 
		 * if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER
		 * ) { criteria.append(" and eplc.area_cd = '" + form.getArea() + "' ");
		 * } else if ( form.getSelectionType() ==
		 * TerritoryFilterForm.TYPE_REGION_FILTER ) {
		 * criteria.append(" and eplc.area_cd = '" + form.getArea() + "' ");
		 * criteria.append(" and eplc.region_cd = '" + form.getRegion() + "' ");
		 * } else if ( form.getSelectionType() ==
		 * TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
		 * criteria.append(" and eplc.area_cd = '" + form.getArea() + "' ");
		 * criteria.append(" and eplc.region_cd = '" + form.getRegion() + "' ");
		 * criteria.append(" and eplc.district_id = '" + form.getDistrict() +
		 * "' "); } if ( !uSession.getUser().isAdmin() ) {
		 * criteria.append(" and eplc.cluster_cd='" +
		 * uSession.getUser().getCluster() + "'"); }
		 */

		criteria.append(" order by e.emplid ");
		String sql = "";
		if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
			sql = searchPDFHSEmployeeSql + criteria.toString();
		} else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {
			sql = searchSPFHSEmployeeSql + criteria.toString();
		}

		log.info("Search SQL FOR THE EMPLOYEE---" + sql);
		// System.out.println("Search SQL FOR THE EMPLOYEE---"+ sql );

		return getPDFHSEmployeesSearch(sql);
	}

	public EmpSearchGNSM[] getGNSMEmployees(EmplSearchForm eform,
			UserSession uSession) {

		EmpSearchGNSM[] ret = null;
		String sql = "SELECT EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD ";
		sql = sql
				+ " ,TRT_COURSE_NAME,DECODE(STATUS,'I','Registered','P','Complete','L','On-Leave') STATUS ";
		sql = sql
				+ " ,DECODE(OVERALL_STATUS,'I','Registered','P','Complete','L','On-Leave')  OVERALL_STATUS ";
		sql = sql + " FROM V_GNSM_DATA_OVERALL e ";

		StringBuffer criteria = new StringBuffer();

		TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

		boolean firstWhere = true;

		if (form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER) {
			criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
			firstWhere = false;
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER) {
			criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			firstWhere = false;
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER) {
			criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			criteria.append(" and e.district_id = '" + form.getDistrict()
					+ "' ");
			firstWhere = false;
		}

		if (!Util.isEmpty(eform.getFname()) || !Util.isEmpty(eform.getLname())) {
			if (!Util.isEmpty(eform.getFname())) {
				if (firstWhere) {
					criteria.append(" where ");
					firstWhere = false;
				} else {
					criteria.append(" and ");
				}
				criteria.append(" (upper(e.first_name) like '"
						+ eform.getFname().toUpperCase().trim() + "%'  ) ");
			}
			if (!Util.isEmpty(eform.getLname())) {
				if (firstWhere) {
					criteria.append(" where ");
					firstWhere = false;
				} else {
					criteria.append(" and ");
				}
				criteria.append(" upper(e.last_name) like '"
						+ eform.getLname().toUpperCase().trim() + "%' ");
			}
		} else if (!Util.isEmpty(eform.getEmplid())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.emplid='" + eform.getEmplid().trim() + "'");
		} else if (!Util.isEmpty(eform.getTerrId())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.territory_id='" + eform.getTerrId().trim()
					+ "'");
		}

		if (!uSession.getUser().isAdmin()) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.cluster_cd='" + uSession.getUser().getCluster()
					+ "'");
		}
		criteria.append(" order by e.emplid,TRT_COURSE_NAME ");

		sql = sql + criteria;

		log.info("Search SQL FOR THE EMPLOYEE---" + sql);

		ResultSet rs = null;
		Statement st = null;
		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			// log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearchGNSM e = new EmpSearchGNSM();
				e.setTeamCode(rs.getString("TEAM_DESC"));
				e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
				e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
				e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
				e.setRole(rs.getString("ROLE_CD"));
				e.setEmplId(rs.getString("EMPLID"));
				e.setActivityName(rs.getString("TRT_COURSE_NAME"));
				e.setActivityStatus(rs.getString("STATUS"));
				e.setOverallStatus(rs.getString("OVERALL_STATUS"));
				tempList.add(e);
			}

			ret = new EmpSearchGNSM[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (EmpSearchGNSM) tempList.get(j);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	public EmpSearchGNSM[] getMSEPIEmployees(EmplSearchForm eform,
			UserSession uSession) {

		EmpSearchGNSM[] ret = null;
		String sql = "SELECT EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD ";
		sql = sql
				+ " ,TRT_COURSE_NAME,DECODE(STATUS,'I','Registered','P','Complete','L','On-Leave') STATUS ";
		sql = sql + " FROM V_MSEPI_NSM_DATA e ";

		StringBuffer criteria = new StringBuffer();

		TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

		boolean firstWhere = true;

		if (form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER) {
			criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
			firstWhere = false;
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER) {
			criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			firstWhere = false;
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER) {
			criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			criteria.append(" and e.district_id = '" + form.getDistrict()
					+ "' ");
			firstWhere = false;
		}

		if (!Util.isEmpty(eform.getFname()) || !Util.isEmpty(eform.getLname())) {
			if (!Util.isEmpty(eform.getFname())) {
				if (firstWhere) {
					criteria.append(" where ");
					firstWhere = false;
				} else {
					criteria.append(" and ");
				}
				criteria.append(" (upper(e.first_name) like '"
						+ eform.getFname().toUpperCase().trim() + "%'  ) ");
			}
			if (!Util.isEmpty(eform.getLname())) {
				if (firstWhere) {
					criteria.append(" where ");
					firstWhere = false;
				} else {
					criteria.append(" and ");
				}
				criteria.append(" upper(e.last_name) like '"
						+ eform.getLname().toUpperCase().trim() + "%' ");
			}
		} else if (!Util.isEmpty(eform.getEmplid())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.emplid='" + eform.getEmplid().trim() + "'");
		} else if (!Util.isEmpty(eform.getTerrId())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.territory_id='" + eform.getTerrId().trim()
					+ "'");
		}

		if (!uSession.getUser().isAdmin()) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.cluster_cd='" + uSession.getUser().getCluster()
					+ "'");
		}
		criteria.append(" order by e.emplid,TRT_COURSE_NAME ");

		sql = sql + criteria;

		log.info("Search SQL FOR THE EMPLOYEE---" + sql);

		ResultSet rs = null;
		Statement st = null;
		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			// log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearchGNSM e = new EmpSearchGNSM();
				e.setTeamCode(rs.getString("TEAM_DESC"));
				// e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
				e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
				e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
				e.setRole(rs.getString("ROLE_CD"));
				e.setEmplId(rs.getString("EMPLID"));
				e.setActivityName(rs.getString("TRT_COURSE_NAME"));
				e.setActivityStatus(rs.getString("STATUS"));
				tempList.add(e);
			}

			ret = new EmpSearchGNSM[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (EmpSearchGNSM) tempList.get(j);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	public Employee[] getPOAEmployees(UserFilter uFilter, String productCD,
			String section) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";

		if (section.equalsIgnoreCase("Not Complete")) {
			thisSection = "I";
			courseStatus = "Not Complete";
		} else if (section.equalsIgnoreCase("On Leave")) {
			thisSection = "L";
			courseStatus = "On Leave";
		} else if (section.equalsIgnoreCase("Complete")) {
			thisSection = "P";
			courseStatus = "Completed";
		}

		String sqlQuery = " SELECT VP.DISTRICT_DESC DISTRICTDESC,"
				+ " FTM.TEAM_SHORT_DESC  TEAMCODE," + " VP.LAST_NAME LASTNAME,"
				+ " VP.FIRST_NAME FIRSTNAME," + " VP.TERRITORY_ROLE_CD ROLE,"
				+ " VP.EMAIL_ADDRESS as email, " + " VP.EMPLID EMPLID," + " '"
				+ courseStatus + "' coursestatus"
				+ " FROM   V_POWERS_MIDPOA1_DATA VP ,MV_TEAM_CODE_MAP ftm "
				+ " WHERE  VP.PRODUCT_CD = 'MIDPOA1'" + " AND STATUS='"
				+ thisSection + "' " + " AND FTM.TEAM_CD=VP.TEAM_CD ";

		// ConditionQuery
		String condQuery = "";

		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + " AND AREA_CD='" + form.getArea() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + " AND VP.TEAM_CD='" + form.getTeam() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + " AND REGION_CD='" + form.getRegion()
					+ "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + " AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}

		String orderQuery = " ORDER BY LAST_NAME,FIRST_NAME ";

		sqlQuery = sqlQuery + condQuery + orderQuery;

		// System.out.println("Query Here is in EmployeeHandler for POA--"+sqlQuery);

		return getPOAEmployees(sqlQuery, params);

	}

	public Employee[] getPDFHSEmployees(UserFilter uFilter, String productCD,
			String section) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (productCD.equalsIgnoreCase("Geodon"))
			productCD = "GEOD";
		if (productCD.equalsIgnoreCase("Aricept"))
			productCD = "ARCP";
		if (productCD.equalsIgnoreCase("Lyrica"))
			productCD = "LYRC";
		if (productCD.equalsIgnoreCase("Celebrex"))
			productCD = "CLBR";
		if (productCD.equalsIgnoreCase("Rebif"))
			productCD = "REBF";
		if (section.equalsIgnoreCase("Not Complete")) {
			thisSection = "I";
			courseStatus = "Not Complete";
		} else if (section.equalsIgnoreCase("On Leave")) {
			thisSection = "L";
			courseStatus = "On Leave";
		} else if (section.equalsIgnoreCase("Complete")) {
			thisSection = "P";
			courseStatus = "Completed";
		}

		String sqlQuery = " SELECT VP.DISTRICT_DESC DISTRICTDESC,"
				+ " VP.TEAM_DESC  TEAMCODE," + " VP.LAST_NAME LASTNAME,"
				+ " VP.FIRST_NAME FIRSTNAME," + " VP.ROLE_CD ROLE,"
				+ " VP.EMAIL_ADDRESS as email, " + " VP.EMPLID EMPLID," + " '"
				+ courseStatus + "' coursestatus"
				+ " FROM   V_PWRA_HS_DATA VP  " + " WHERE  VP.PRODUCT_CD = '"
				+ productCD + "'" + " AND STATUS='" + thisSection + "' ";

		// ConditionQuery
		String condQuery = "";

		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + " AND AREA_CD='" + form.getArea() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + " AND VP.TEAM_CD='" + form.getTeam() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + " AND REGION_CD='" + form.getRegion()
					+ "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + " AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}

		String orderQuery = " ORDER BY LAST_NAME,FIRST_NAME ";

		sqlQuery = sqlQuery + condQuery + orderQuery;

		// System.out.println("Query Here is in EmployeeHandler for POA--"+sqlQuery);

		return getPDFHSEmployees(sqlQuery, params);
	}

	// This method is used for General Session Entry.
	/*
	 * public GeneralSessionEmployee[] getGeneralSessionEmployees(){
	 * GeneralSessionEmployee[] ret = null; String thisSection=""; String[]
	 * params = new String[0];
	 * 
	 * String sqlQuery= " SELECT VP.DISTRICT_DESC as DISTRICTDESC,"+
	 * " VP.TEAM_DESC  as TEAMCODE,"+ " VP.LAST_NAME as LASTNAME,"+
	 * " VP.FIRST_NAME as FIRSTNAME,"+ " VP.ROLE_CD as ROLE,"+
	 * " VP.EMPLID as EMPLID"+ " FROM   V_PWRA_PLC_DATA VP  " +
	 * " WHERE  VP.EXAM_TYPE = 'ATD'";
	 * 
	 * 
	 * 
	 * 
	 * 
	 * ResultSet rs = null; //PreparedStatement st = null; Connection conn =
	 * null; Statement st = null;
	 * 
	 * try {
	 * 
	 * 
	 * Timer timer = new Timer();
	 * 
	 * Context ctx = new InitialContext(); DataSource ds =
	 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
	 * 
	 * conn = ds.getConnection();
	 * 
	 * st = conn.createStatement();
	 * 
	 * List tempList = new ArrayList();
	 * 
	 * rs = st.executeQuery(sqlQuery);
	 * 
	 * while (rs.next()) { GeneralSessionEmployee curr = new
	 * GeneralSessionEmployee(); curr.setDistrictDesc(
	 * rs.getString("districtDesc".toUpperCase()) ); curr.setTeamCode(
	 * rs.getString("teamCode".toUpperCase()) ); curr.setLastName(
	 * rs.getString("lastName".toUpperCase()) ); curr.setFirstName(
	 * rs.getString("firstName".toUpperCase()) ); curr.setRole(
	 * rs.getString("role".toUpperCase()) ); curr.setEmplId(
	 * rs.getString("EMPLID") ); tempList.add( curr ); }
	 * 
	 * ret = new GeneralSessionEmployee[tempList.size()];
	 * 
	 * for ( int j=0; j < tempList.size(); j++ ) { ret[j] =
	 * (GeneralSessionEmployee)tempList.get(j); }
	 * 
	 * } catch (Exception e) { log.error(e,e); } finally { if ( rs != null) {
	 * try { rs.close(); } catch ( Exception e2) { log.error(e2,e2); } } if ( st
	 * != null) { try { st.close(); } catch ( Exception e2) { log.error(e2,e2);
	 * } } if ( conn != null) { try { conn.close(); } catch ( Exception e2) {
	 * log.error(e2,e2); } } } return ret; }
	 */

	public Employee[] getPOAOverAllEmployees(UserFilter uFilter,
			String productCD, String section) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (section.equalsIgnoreCase("Not Complete")) {
			thisSection = "I";
			courseStatus = "Not Complete";
		} else if (section.equalsIgnoreCase("On Leave")) {
			thisSection = "L";
			courseStatus = "On Leave";
		} else if (section.equalsIgnoreCase("Complete")) {
			thisSection = "P";
			courseStatus = "Completed";
		}

		String sqlQuery = " SELECT VP.DISTRICT_DESC DISTRICTDESC,"
				+ " FTM.TEAM_SHORT_DESC TEAMCODE,"
				+ " VP.LAST_NAME LASTNAME,"
				+ " VP.FIRST_NAME FIRSTNAME,"
				+ " VP.TERRITORY_ROLE_CD ROLE,"
				+ " VP.EMPLID EMPLID, "
				+ " VP.EMAIL_ADDRESS as email, "
				+ " VP. PRODUCT_CD, DECODE(VP.STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status  "
				+ " FROM   v_powers_midpoa1_data VP ,MV_TEAM_CODE_MAP ftm "
				+ " WHERE  OVERALL_STATUS='" + thisSection + "' "
				+ " AND PRODUCT_CD NOT IN ('NA') "
				+ " AND FTM.TEAM_CD=VP.TEAM_CD ";

		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + " AND AREA_CD='" + form.getArea() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + " AND VP.TEAM_CD='" + form.getTeam() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + " AND REGION_CD='" + form.getRegion()
					+ "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + " AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}

		String orderQuery = " order by LASTNAME,FIRSTNAME ";

		sqlQuery = sqlQuery + condQuery + orderQuery;

		System.out.println("Query Here is in EmployeeHandler for POA OverAll--"
				+ sqlQuery);
		return getPOAOverAllEmployees(sqlQuery, params);

	}

	public Employee[] getPDFHSOverAllEmployees(UserFilter uFilter,
			String productCD, String section) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (section.equalsIgnoreCase("Not Complete")) {
			thisSection = "I";
			courseStatus = "Not Complete";
		} else if (section.equalsIgnoreCase("On Leave")) {
			thisSection = "L";
			courseStatus = "On Leave";
		} else if (section.equalsIgnoreCase("Complete")) {
			thisSection = "P";
			courseStatus = "Completed";
		}

		String sqlQuery = " SELECT VP.DISTRICT_DESC DISTRICTDESC,"
				+ " VP.TEAM_DESC TEAMCODE,"
				+ " VP.LAST_NAME LASTNAME,"
				+ " VP.FIRST_NAME FIRSTNAME,"
				+ " VP.ROLE_CD ROLE,"
				+ " VP.EMPLID EMPLID, "
				+ " VP.EMAIL_ADDRESS as email, "
				+ " VP. PRODUCT_CD, DECODE(VP.STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status  "
				+ " FROM   V_PWRA_HS_DATA_OVERALL VP "
				+ " WHERE  OVERALL_STATUS='" + thisSection + "' "
				+ " AND PRODUCT_CD NOT IN ('NA') ";

		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + " AND AREA_CD='" + form.getArea() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + " AND VP.TEAM_CD='" + form.getTeam() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + " AND REGION_CD='" + form.getRegion()
					+ "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + " AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}

		String orderQuery = " order by LASTNAME,FIRSTNAME ";

		sqlQuery = sqlQuery + condQuery + orderQuery;

		// System.out.println("Query Here is in EmployeeHandler for PDFHS OverAll--"+sqlQuery);
		return getPDFHSOverAllEmployees(sqlQuery, params);

	}

	public Employee[] getSPFOverAllEmployees(UserFilter uFilter,
			String productCD, String section) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (section.equalsIgnoreCase("Not Complete")) {
			thisSection = "I";
			courseStatus = "Not Complete";
		} else if (section.equalsIgnoreCase("On Leave")) {
			thisSection = "L";
			courseStatus = "On Leave";
		} else if (section.equalsIgnoreCase("Complete")) {
			thisSection = "P";
			courseStatus = "Completed";
		}

		String sqlQuery = " SELECT VP.DISTRICT_DESC DISTRICTDESC,"
				+ " VP.TEAM_DESC TEAMCODE,"
				+ " VP.LAST_NAME LASTNAME,"
				+ " VP.FIRST_NAME FIRSTNAME,"
				+ " VP.ROLE_CD ROLE,"
				+ " VP.EMPLID EMPLID, "
				+ " VP.EMAIL_ADDRESS as email, "
				+ " VP. PRODUCT_CD, DECODE(VP.STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status  "
				+ " FROM   V_SPF_HS_DATA_OVERALL VP "
				+ " WHERE  OVERALL_STATUS='" + thisSection + "' "
				+ " AND PRODUCT_CD NOT IN ('NA') ";

		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + " AND AREA_CD='" + form.getArea() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + " AND VP.TEAM_CD='" + form.getTeam() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + " AND REGION_CD='" + form.getRegion()
					+ "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + " AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}

		String orderQuery = " order by LASTNAME,FIRSTNAME ";

		sqlQuery = sqlQuery + condQuery + orderQuery;

		// System.out.println("Query Here is in EmployeeHandler for PDFHS OverAll--"+sqlQuery);
		return getPDFHSOverAllEmployees(sqlQuery, params);

	}

	// This function is made more generic so that it can be reused.
	public Employee[] getSPFEmployees(UserFilter uFilter, String productCD,
			String section) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (productCD.equalsIgnoreCase("Geodon"))
			productCD = "GEOD";
		if (productCD.equalsIgnoreCase("Aricept"))
			productCD = "ARCP";
		if (productCD.equalsIgnoreCase("Lyrica"))
			productCD = "LYRC";
		if (productCD.equalsIgnoreCase("Celebrex"))
			productCD = "CLBR";
		if (productCD.equalsIgnoreCase("Rebif"))
			productCD = "REBF";
		if (section.equalsIgnoreCase("Not Complete")) {
			thisSection = "I";
			courseStatus = "Not Complete";
		} else if (section.equalsIgnoreCase("On Leave")) {
			thisSection = "L";
			courseStatus = "On Leave";
		} else if (section.equalsIgnoreCase("Complete")) {
			thisSection = "P";
			courseStatus = "Completed";
		}

		String sqlQuery = " SELECT VP.DISTRICT_DESC DISTRICTDESC,"
				+ " VP.TEAM_DESC  TEAMCODE," + " VP.LAST_NAME LASTNAME,"
				+ " VP.FIRST_NAME FIRSTNAME," + " VP.ROLE_CD ROLE,"
				+ " VP.EMAIL_ADDRESS as email, " + " VP.EMPLID EMPLID," + " '"
				+ courseStatus + "' coursestatus"
				+ " FROM   V_SPF_HS_DATA VP  " + " WHERE  VP.PRODUCT_CD = '"
				+ productCD + "'" + " AND STATUS='" + thisSection + "' ";

		// ConditionQuery
		String condQuery = "";

		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + " AND AREA_CD='" + form.getArea() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + " AND VP.TEAM_CD='" + form.getTeam() + "' ";
		}

		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + " AND REGION_CD='" + form.getRegion()
					+ "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + " AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}

		String orderQuery = " ORDER BY LAST_NAME,FIRST_NAME ";

		sqlQuery = sqlQuery + condQuery + orderQuery;

		// System.out.println("Query Here is in EmployeeHandler for POA--"+sqlQuery);

		return getPDFHSEmployees(sqlQuery, params);
	}

	/**
	 * Searches users by name, will filter against employees that are managed by
	 * the user.
	 */
	public EmpSearch[] getEmployeesByName(EmplSearchForm eform,
			UserSession uSession) {
		StringBuffer criteria = new StringBuffer();
		List productCodes = new ArrayList();
		for (Iterator it = uSession.getUser().getProducts().iterator(); it
				.hasNext();) {
			Product prod = (Product) it.next();
			productCodes.add(prod.getProductCode());
		}

		String productStr = Util.delimit(productCodes, "','");
		TerritoryFilterForm form = uSession.getUserFilterForm();

		criteria.append(" and p.product_cd in ('" + productStr + "') ");

		if (!Util.isEmpty(eform.getFname())) {
			criteria.append(" and (upper(e.first_name) like '"
					+ eform.getFname().toUpperCase().trim() + "%')");
		}
		if (!Util.isEmpty(eform.getLname())) {
			criteria.append(" and upper(e.last_name) like '"
					+ eform.getLname().toUpperCase().trim() + "%'");
		}
		if (!Util.isEmpty(eform.getEmplid())) {
			criteria.append("and e.emplid='" + eform.getEmplid().trim() + "'");
		}
		/*
		 * if(!Util.isEmpty(eform.getTerrId())){
		 * criteria.append("and e.territory_id='"+eform.getTerrId().trim()+"'");
		 * }
		 */
		/* Adding extra conditions for RBU */
		if (!Util.isEmpty(eform.getEmail())) {
			criteria.append("and e.email_address like '"
					+ eform.getEmail().trim() + "%'");
		}
		if (!Util.isEmpty(eform.getBu())) {
			criteria.append("and e.bu ='" + eform.getBu().trim() + "'");
		}
		if (!Util.isEmpty(eform.getSalesorg())) {
			criteria.append("and e.sales_group='" + eform.getSalesorg().trim()
					+ "'");
		}
		if (!Util.isEmpty(eform.getRole())) {
			criteria.append("and e.role_cd='" + eform.getRole().trim() + "'");
		}
		/*
		 * if(!Util.isEmpty(eform.getSalesPosId())){
		 * criteria.append("and e.sales_position_id='"
		 * +eform.getSalesPosId().trim()+"'"); }
		 */

		/*
		 * if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER
		 * ) { criteria.append(" and e.area_cd = '" + form.getArea() + "' "); }
		 * else if ( form.getSelectionType() ==
		 * TerritoryFilterForm.TYPE_REGION_FILTER ) {
		 * criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
		 * criteria.append(" and e.region_cd = '" + form.getRegion() + "' "); }
		 * else if ( form.getSelectionType() ==
		 * TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
		 * criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
		 * criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
		 * criteria.append(" and e.district_id = '" + form.getDistrict() +
		 * "' "); }
		 * 
		 * if ( !uSession.getUser().isAdmin() ) {
		 * criteria.append(" and e.cluster_cd='" +
		 * uSession.getUser().getCluster() + "'"); }
		 */
		criteria.append(" order by e.emplid ");
		String sql = searchEmployeSql + criteria.toString();
		System.out.println("Search SQL FOR THE EMPLOYEE---" + sql);
		return getEmployeesSearch(sql);
	}

	/**
	 * Searches users by name, will filter against employees that are managed by
	 * the user.
	 */
	public EmpSearch[] getEmployeesForSimulation(EmplSearchForm eform,
			UserSession uSession) {
		StringBuffer criteria = new StringBuffer();

		if (!Util.isEmpty(eform.getFname())) {
			criteria.append(" and (upper(e.first_name) like '"
					+ eform.getFname().toUpperCase().trim() + "%')");
		}
		if (!Util.isEmpty(eform.getLname())) {
			criteria.append(" and upper(e.last_name) like '"
					+ eform.getLname().toUpperCase().trim() + "%'");
		}
		if (!Util.isEmpty(eform.getEmplid())) {
			criteria.append(" and e.emplid='" + eform.getEmplid().trim() + "'");
		}
		/*
		 * if(!Util.isEmpty(eform.getSalesPosId())){
		 * criteria.append(" and e.sales_position_id='"
		 * +eform.getSalesPosId().trim()+"'"); }
		 */
		/* Adding extra conditions for RBU */
		if (!Util.isEmpty(eform.getEmail())) {
			criteria.append(" and e.email_address like '"
					+ eform.getEmail().trim() + "%'");
		}
		if (!Util.isEmpty(eform.getBu()) && !eform.getBu().equals("All")) {
			criteria.append(" and e.bu ='" + eform.getBu().trim() + "'");
		}
		if (!Util.isEmpty(eform.getSalesorg())
				&& !eform.getSalesorg().equals("All")) {
			criteria.append(" and e.sales_group='" + eform.getSalesorg().trim()
					+ "'");
		}
		if (!Util.isEmpty(eform.getRole()) && !eform.getRole().equals("All")) {
			criteria.append(" and e.role_cd='" + eform.getRole().trim() + "'");
		}

		criteria.append(" order by e.emplid ");
		String sql = searchSimulateUserSql + criteria.toString();

		System.out.println("Search SQL FOR THE EMPLOYEE---" + sql);
		return getEmployeesSearch(sql);
	}

	// Start: Start for TRT Phase 2
	public EmpSearch[] getAllEmployees(EmplSearchForm eform,
			UserSession uSession) {
		StringBuffer criteria = new StringBuffer();
		String empId = uSession.getUser().getEmplid();
		boolean isAdmin = uSession.getUser().isAdmin();
		System.out
				.println("getAllEmployees in EmployeeHandler------------------"
						+ empId);

		if (!Util.isEmpty(eform.getFname())) {
			criteria.append(" and (upper(e.first_name) like '"
					+ eform.getFname().toUpperCase().trim() + "%')");
		}
		if (!Util.isEmpty(eform.getLname())) {
			criteria.append(" and upper(e.last_name) like '"
					+ eform.getLname().toUpperCase().trim() + "%'");
		}
		if (!Util.isEmpty(eform.getEmplid())) {
			criteria.append(" and e.emplid='" + eform.getEmplid().trim() + "'");
		}
		if (!Util.isEmpty(eform.getEmail())) {
			criteria.append(" and e.email_address like '"
					+ eform.getEmail().trim() + "%'");
		}
		if (!Util.isEmpty(eform.getBu()) && !eform.getBu().equals("All")) {
			criteria.append(" and e.bu ='" + eform.getBu().trim() + "'");
		}
		if (!Util.isEmpty(eform.getSalesorg())
				&& !eform.getSalesorg().equals("All")) {
			criteria.append(" and e.sales_group='" + eform.getSalesorg().trim()
					+ "'");
		}
		if (!Util.isEmpty(eform.getRole()) && !eform.getRole().equals("All")) {
			criteria.append(" and e.role_cd='" + eform.getRole().trim() + "'");
		}

		if (isAdmin) {
			criteria.append(" order by e.emplid ");
		} else {
			criteria.append(" connect by prior emplid=reports_to_emplid start with reports_to_emplid="
					+ empId + " order by e.emplid ");
		}
		String sql = searchAllEmployees + criteria.toString();

		System.out.println("Search SQL FOR THE EMPLOYEE---" + sql);
		return getEmployeesSearch(sql);
	}

	// End for TRT Phase 2

	public Employee[] getEmployeeByRole(String role) {

		String sql = " select  "
				+ " emplid as emplId, "
				+ " geo_type_desc as geoType, "
				+ " promotion_date as promoDate, "
				+ " effective_hire_date as hireDate, "
				+ " sex as gender, "
				+ " email_address as email, "
				+ " reports_to_emplid as reportsToEmplid,"
				+ " DECODE(empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') AS FIELDACTIVE ,"
				+ " area_cd as areaCd, "
				+ " area_desc as areaDesc, "
				+ " region_cd as regionCd, "
				+ " region_desc as regionDesc, "
				+ " district_id as districtId, "
				+ " district_desc as districtDesc, "
				+ " territory_id as territoryId, "
				+ " territory_role_cd as role, "
				+ " team_cd as teamCode, "
				+ " cluster_cd as clusterCode, "
				+ " last_name as lastName, "
				+ " first_name as firstName, "
				+ " middle_name as middleName, "
				+ " preferred_name as preferredName "
				+ " from  "
				+ " v_new_field_employee "
				+ " where  "
				+ "  cluster_cd in ('Cust Bus Unit','Steere','Pratt','Powers','Specialty Markets','Pratt Steere PR','Powers - PR','SM PR','ONC Bus Unit')   ";

		String[] params = new String[0];
		sql = sql + " and territory_role_cd in (" + role
				+ ") order by last_name,first_name  ";

		return getEmployees(sql, params);
	}

	/**
	 * Takes a sql string and converst the the result to Employee objects
	 */

	private Employee[] getEmployees(String sql, String params[]) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;

		List tempList;
		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			tempList = new ArrayList();

			st = conn.prepareCall(sql);
			st.setFetchSize(1000);

			for (int i = 1; i <= params.length; i++) {
				st.setString(i, params[i - 1]);
			}
			// System.out.println("before run:" + timer.getFromLast());
			rs = st.executeQuery();

			// System.out.println("query run:" + timer.getFromLast());
			ret = convertRecords(rs);
			// System.out.println("query convert:" + timer.getFromLast());
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	private EmpSearchPOA[] getPOAEmployeesSearch(String sql) {
		EmpSearchPOA[] ret = null;

		ResultSet rs = null;
		Statement st = null;
		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			// log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearchPOA curr = new EmpSearchPOA();
				curr.setEmplId(rs.getString("EMPLID"));
				curr.setLastName(rs.getString("lastName".toUpperCase()));
				curr.setFirstName(rs.getString("firstName".toUpperCase()));
				curr.setMiddleName(rs.getString("middleName".toUpperCase()));
				curr.setPreferredName(rs.getString("preferredName"
						.toUpperCase()));
				curr.setProductCd(rs.getString("productCd".toUpperCase()));
				curr.setFieldActive(rs.getString("fieldActive"));
				curr.setTerritoryId(rs.getString("territoryId"));
				curr.setRoleCd(rs.getString("roleCD"));
				curr.setCompletedDate(rs.getString("completedDate"));
				curr.setExamStatus(rs.getString("examStatus"));
				curr.setOverallExamStatus(rs.getString("overallExamStatus"));
				tempList.add(curr);
				// System.out.println("@@@@ Data output:" +
				// rs.getString("EMPLID"));
			}

			ret = new EmpSearchPOA[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (EmpSearchPOA) tempList.get(j);
			}

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	private EmpSearchPDFHS[] getPDFHSEmployeesSearch(String sql) {
		EmpSearchPDFHS[] ret = null;

		ResultSet rs = null;
		Statement st = null;
		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			// log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearchPDFHS curr = new EmpSearchPDFHS();
				curr.setEmplId(rs.getString("EMPLID"));
				curr.setLastName(rs.getString("lastName".toUpperCase()));
				curr.setFirstName(rs.getString("firstName".toUpperCase()));
				curr.setProductCd(rs.getString("productCd".toUpperCase()));
				curr.setFieldActive(rs.getString("fieldActive"));
				curr.setTerritoryId(rs.getString("territoryId"));
				curr.setRoleCd(rs.getString("roleCD"));
				curr.setCompletedDate(rs.getString("completedDate"));
				curr.setExamStatus(rs.getString("examStatus"));
				curr.setOverallExamStatus(rs.getString("overallExamStatus"));
				curr.setcompletedDatePLC(rs.getString("completedDatePLC"));
				curr.setoverallExamStatusPLC(rs
						.getString("overallExamStatusPLC"));
				curr.setPLCStatus(rs.getString("plcexamStatus"));
				tempList.add(curr);
				// System.out.println("@@@@ Data output:" +
				// rs.getString("EMPLID"));
			}

			ret = new EmpSearchPDFHS[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (EmpSearchPDFHS) tempList.get(j);
			}

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	/**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getPOAEmployees(String sql, String params[]) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		// Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {

			Timer timer = new Timer();

			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * 
			 * conn = ds.getConnection();
			 */

			List tempList = new ArrayList();

			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for (int i = 1; i <= params.length; i++) {
				st.setString(i, params[i - 1]);
			}
			rs = st.executeQuery();

			while (rs.next()) {
				Employee curr = new Employee();
				curr.setDistrictDesc(rs.getString("districtDesc".toUpperCase()));
				curr.setTeamCode(rs.getString("teamCode".toUpperCase()));
				curr.setLastName(rs.getString("lastName".toUpperCase()));
				curr.setFirstName(rs.getString("firstName".toUpperCase()));
				curr.setRole(rs.getString("role".toUpperCase()));
				curr.setEmplId(rs.getString("EMPLID"));
				curr.setEmail(rs.getString("EMAIL"));
				curr.setCourseStatus(rs.getString("coursestatus"));
				/**
				 * curr.setAreaCd( rs.getString("areaCd".toUpperCase()) );
				 * curr.setAreaDesc( rs.getString("areaDesc".toUpperCase()) );
				 * curr.setRegionCd( rs.getString("regionCd".toUpperCase()) );
				 * curr.setRegionDesc( rs.getString("regionDesc".toUpperCase())
				 * ); curr.setDistrictId(
				 * rs.getString("districtId".toUpperCase()) );
				 * 
				 * curr.setTerritoryId(
				 * rs.getString("territoryId".toUpperCase()) );
				 * 
				 * 
				 * curr.setClusterCode(
				 * rs.getString("clusterCode".toUpperCase()) );
				 * curr.setMiddleName( rs.getString("middleName".toUpperCase())
				 * ); curr.setPreferredName(
				 * rs.getString("preferredName".toUpperCase()) );
				 * 
				 * curr.setGender( rs.getString("gender".toUpperCase()) );
				 * curr.setEmail( rs.getString("email".toUpperCase()) );
				 * 
				 * curr.setReportsToEmplid(
				 * rs.getString("reportsToEmplid".toUpperCase()) );
				 * 
				 * 
				 * curr.setEmployeeStatus(rs.getString("fieldactive")); Date
				 * hireDate = rs.getDate("hireDate".toUpperCase()); if (
				 * hireDate != null ) { curr.setHireDate( new Date(
				 * hireDate.getTime() ) ); } Date promoDate =
				 * rs.getDate("promoDate".toUpperCase()); if ( promoDate != null
				 * ) { curr.setPromoDate( new Date( promoDate.getTime() ) ); }
				 **/

				tempList.add(curr);
			}

			ret = new Employee[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (Employee) tempList.get(j);
			}

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	/**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getPDFHSEmployees(String sql, String params[]) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		// Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {

			Timer timer = new Timer();

			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * 
			 * conn = ds.getConnection();
			 */

			List tempList = new ArrayList();

			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for (int i = 1; i <= params.length; i++) {
				st.setString(i, params[i - 1]);
			}
			rs = st.executeQuery();

			while (rs.next()) {
				Employee curr = new Employee();
				curr.setDistrictDesc(rs.getString("districtDesc".toUpperCase()));
				curr.setTeamCode(rs.getString("teamCode".toUpperCase()));
				curr.setLastName(rs.getString("lastName".toUpperCase()));
				curr.setFirstName(rs.getString("firstName".toUpperCase()));
				curr.setRole(rs.getString("role".toUpperCase()));
				curr.setEmplId(rs.getString("EMPLID"));
				curr.setEmail(rs.getString("EMAIL"));
				curr.setCourseStatus(rs.getString("coursestatus"));
				/**
				 * curr.setAreaCd( rs.getString("areaCd".toUpperCase()) );
				 * curr.setAreaDesc( rs.getString("areaDesc".toUpperCase()) );
				 * curr.setRegionCd( rs.getString("regionCd".toUpperCase()) );
				 * curr.setRegionDesc( rs.getString("regionDesc".toUpperCase())
				 * ); curr.setDistrictId(
				 * rs.getString("districtId".toUpperCase()) );
				 * 
				 * curr.setTerritoryId(
				 * rs.getString("territoryId".toUpperCase()) );
				 * 
				 * 
				 * curr.setClusterCode(
				 * rs.getString("clusterCode".toUpperCase()) );
				 * curr.setMiddleName( rs.getString("middleName".toUpperCase())
				 * ); curr.setPreferredName(
				 * rs.getString("preferredName".toUpperCase()) );
				 * 
				 * curr.setGender( rs.getString("gender".toUpperCase()) );
				 * curr.setEmail( rs.getString("email".toUpperCase()) );
				 * 
				 * curr.setReportsToEmplid(
				 * rs.getString("reportsToEmplid".toUpperCase()) );
				 * 
				 * 
				 * curr.setEmployeeStatus(rs.getString("fieldactive")); Date
				 * hireDate = rs.getDate("hireDate".toUpperCase()); if (
				 * hireDate != null ) { curr.setHireDate( new Date(
				 * hireDate.getTime() ) ); } Date promoDate =
				 * rs.getDate("promoDate".toUpperCase()); if ( promoDate != null
				 * ) { curr.setPromoDate( new Date( promoDate.getTime() ) ); }
				 **/

				tempList.add(curr);
			}

			ret = new Employee[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (Employee) tempList.get(j);
			}

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	/**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getPOAOverAllEmployees(String sql, String params[]) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		HashMap empHashMap = new LinkedHashMap();

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			List tempList = new ArrayList();
			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for (int i = 1; i <= params.length; i++) {
				st.setString(i, params[i - 1]);
			}
			rs = st.executeQuery();
			String emplid = "";
			Employee curr = null;
			while (rs.next()) {
				// Build a hashmap with Emplid as key and EmpBean as object
				emplid = rs.getString("EMPLID");
				if (empHashMap.containsKey(emplid)) {
					curr = (Employee) empHashMap.get(emplid);
				} else {
					curr = new Employee();
				}
				curr.setDistrictDesc(rs.getString("districtDesc".toUpperCase()));
				curr.setTeamCode(rs.getString("teamCode".toUpperCase()));
				curr.setLastName(rs.getString("lastName".toUpperCase()));
				curr.setFirstName(rs.getString("firstName".toUpperCase()));
				curr.setRole(rs.getString("role".toUpperCase()));
				curr.setEmail(rs.getString("EMAIL"));
				curr.setEmplId(emplid);
				curr.addToproductStatusMap(rs.getString("PRODUCT_CD")
						.toUpperCase(), rs.getString("Status"));
				empHashMap.put(emplid, curr);
				/**
				 * curr.setAreaCd( rs.getString("areaCd".toUpperCase()) );
				 * curr.setAreaDesc( rs.getString("areaDesc".toUpperCase()) );
				 * curr.setRegionCd( rs.getString("regionCd".toUpperCase()) );
				 * curr.setRegionDesc( rs.getString("regionDesc".toUpperCase())
				 * ); curr.setDistrictId(
				 * rs.getString("districtId".toUpperCase()) );
				 * 
				 * curr.setTerritoryId(
				 * rs.getString("territoryId".toUpperCase()) );
				 * 
				 * 
				 * curr.setClusterCode(
				 * rs.getString("clusterCode".toUpperCase()) );
				 * curr.setMiddleName( rs.getString("middleName".toUpperCase())
				 * ); curr.setPreferredName(
				 * rs.getString("preferredName".toUpperCase()) );
				 * 
				 * curr.setGender( rs.getString("gender".toUpperCase()) );
				 * curr.setEmail( rs.getString("email".toUpperCase()) );
				 * 
				 * curr.setReportsToEmplid(
				 * rs.getString("reportsToEmplid".toUpperCase()) );
				 * 
				 * 
				 * curr.setEmployeeStatus(rs.getString("fieldactive")); Date
				 * hireDate = rs.getDate("hireDate".toUpperCase()); if (
				 * hireDate != null ) { curr.setHireDate( new Date(
				 * hireDate.getTime() ) ); } Date promoDate =
				 * rs.getDate("promoDate".toUpperCase()); if ( promoDate != null
				 * ) { curr.setPromoDate( new Date( promoDate.getTime() ) ); }
				 **/

			}
			String emplidTemp = "";
			// System.out.println("HashMap Size here is "+empHashMap.size());
			ret = new Employee[empHashMap.size()];
			int c = 0;
			for (Iterator iter = empHashMap.keySet().iterator(); iter.hasNext();) {
				emplidTemp = iter.next().toString();
				ret[c++] = (Employee) empHashMap.get(emplidTemp);
			}

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	/**
	 * Takes a sql string and converst the the result to Employee objects
	 */
	private Employee[] getPDFHSOverAllEmployees(String sql, String params[]) {
		Employee[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
		HashMap empHashMap = new LinkedHashMap();

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			List tempList = new ArrayList();
			st = conn.prepareCall(sql);
			st.setFetchSize(5000);

			for (int i = 1; i <= params.length; i++) {
				st.setString(i, params[i - 1]);
			}
			rs = st.executeQuery();
			String emplid = "";
			Employee curr = null;
			while (rs.next()) {
				// Build a hashmap with Emplid as key and EmpBean as object
				emplid = rs.getString("EMPLID");
				if (empHashMap.containsKey(emplid)) {
					curr = (Employee) empHashMap.get(emplid);
				} else {
					curr = new Employee();
				}
				curr.setDistrictDesc(rs.getString("districtDesc".toUpperCase()));
				curr.setTeamCode(rs.getString("teamCode".toUpperCase()));
				curr.setLastName(rs.getString("lastName".toUpperCase()));
				curr.setFirstName(rs.getString("firstName".toUpperCase()));
				curr.setRole(rs.getString("role".toUpperCase()));
				curr.setEmail(rs.getString("EMAIL"));
				curr.setEmplId(emplid);
				curr.addToproductStatusMap(rs.getString("PRODUCT_CD")
						.toUpperCase(), rs.getString("Status"));
				empHashMap.put(emplid, curr);
				/**
				 * curr.setAreaCd( rs.getString("areaCd".toUpperCase()) );
				 * curr.setAreaDesc( rs.getString("areaDesc".toUpperCase()) );
				 * curr.setRegionCd( rs.getString("regionCd".toUpperCase()) );
				 * curr.setRegionDesc( rs.getString("regionDesc".toUpperCase())
				 * ); curr.setDistrictId(
				 * rs.getString("districtId".toUpperCase()) );
				 * 
				 * curr.setTerritoryId(
				 * rs.getString("territoryId".toUpperCase()) );
				 * 
				 * 
				 * curr.setClusterCode(
				 * rs.getString("clusterCode".toUpperCase()) );
				 * curr.setMiddleName( rs.getString("middleName".toUpperCase())
				 * ); curr.setPreferredName(
				 * rs.getString("preferredName".toUpperCase()) );
				 * 
				 * curr.setGender( rs.getString("gender".toUpperCase()) );
				 * curr.setEmail( rs.getString("email".toUpperCase()) );
				 * 
				 * curr.setReportsToEmplid(
				 * rs.getString("reportsToEmplid".toUpperCase()) );
				 * 
				 * 
				 * curr.setEmployeeStatus(rs.getString("fieldactive")); Date
				 * hireDate = rs.getDate("hireDate".toUpperCase()); if (
				 * hireDate != null ) { curr.setHireDate( new Date(
				 * hireDate.getTime() ) ); } Date promoDate =
				 * rs.getDate("promoDate".toUpperCase()); if ( promoDate != null
				 * ) { curr.setPromoDate( new Date( promoDate.getTime() ) ); }
				 **/

			}
			String emplidTemp = "";
			// System.out.println("HashMap Size here is "+empHashMap.size());
			ret = new Employee[empHashMap.size()];
			int c = 0;
			for (Iterator iter = empHashMap.keySet().iterator(); iter.hasNext();) {
				emplidTemp = iter.next().toString();
				ret[c++] = (Employee) empHashMap.get(emplidTemp);
			}

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	/**
	 * Searches users by name, will filter against employees that are managed by
	 * the user.
	 */
	public EmpSearchPOA[] getPOAEmployeesById(String emplid) {
		StringBuffer criteria = new StringBuffer();

		criteria.append(" where product_cd <> 'NA' and emplid = '" + emplid
				+ "' ");

		String sql = searchPOAEmployeeSql + criteria.toString();

		System.out.println("@@@@ SQL" + sql);

		log.info("Search SQL FOR THE EMPLOYEE DETAIL PAGE---" + sql);
		return getPOAEmployeesSearch(sql);
	}

	/**
	 * Searches users by name, will filter against employees that are managed by
	 * the user.
	 */
	public EmpSearchPDFHS[] getPDFHSEmployeesById(String emplid) {
		StringBuffer criteria = new StringBuffer();

		criteria.append(" where product_cd <> 'NA' and emplid = '" + emplid
				+ "' ");

		String sql = searchPDFHSEmployeeSql + criteria.toString();

		System.out.println("@@@@ SQL" + sql);

		log.info("Search SQL FOR THE EMPLOYEE DETAIL PAGE---" + sql);
		return getPDFHSEmployeesSearch(sql);
	}

	private EmpSearch[] getEmployeesSearch(String sql) {
		EmpSearch[] ret = null;

		ResultSet rs = null;
		Statement st = null;
		// Connection conn = null;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();

		try {

			Timer timer = new Timer();

			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * 
			 * conn = ds.getConnection();
			 */
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			// log.info( sql );
			rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int ncols = rsmd.getColumnCount();

			HashMap fields = new HashMap();

			for (int i = 1; i <= ncols; i++) {
				String key = rsmd.getColumnName(i);
				fields.put(key, "yes");
			}

			while (rs.next()) {
				EmpSearch curr = new EmpSearch();
				curr.setEmplId(rs.getString("EMPLID"));
				curr.setLastName(rs.getString("lastName".toUpperCase()));
				curr.setFirstName(rs.getString("firstName".toUpperCase()));
				curr.setMiddleName(rs.getString("middleName".toUpperCase()));
				curr.setPreferredName(rs.getString("preferredName"
						.toUpperCase()));
				curr.setFieldActive(rs.getString("fieldActive"));
				curr.setRoleCd(rs.getString("roleCD"));
				if (fields.containsKey("productCd".toUpperCase())) {
					curr.setProductCd(rs.getString("productCd".toUpperCase()));
				}

				if (fields.containsKey("exam_issued_on".toUpperCase())) {
					curr.setExam_issued_on(rs.getString("exam_issued_on"));
				}
				if (fields.containsKey("MATERIALDATEORDERED".toUpperCase())) {
					curr.setMaterialOrderDate(rs
							.getString("MATERIALDATEORDERED"));
				}
				if (fields.containsKey("trainingNeed".toUpperCase())) {
					curr.setTrainingNeed(rs.getString("trainingNeed"));
				}
				// Added for RBU changes
				if (fields.containsKey("sales_position_id".toUpperCase())) {
					curr.setSalesPosId(rs.getString("sales_position_id"
							.toUpperCase()));
				}
				if (fields.containsKey("bu".toUpperCase())) {
					curr.setBusUnit(rs.getString("bu".toUpperCase()));
				}
				if (fields.containsKey("salesOrg".toUpperCase())) {
					curr.setSalesOrg(rs.getString("salesOrg".toUpperCase()));
				}
				if (fields.containsKey("email".toUpperCase())) {
					curr.setEmail(rs.getString("email".toUpperCase()));
				}
				tempList.add(curr);
			}

			ret = new EmpSearch[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (EmpSearch) tempList.get(j);
			}

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	// Start: Added for Major Enhancement 3.6 - F1

	public EmpSearch[] getDelegatedFromEmployees(List eList) {

		LoggerHelper.logSystemDebug("TRTtest8 ");

		EmpSearch[] ret = null;

		ResultSet rs = null;
		Statement st = null;

		List tempList = new ArrayList();
		StringBuffer strJoined = new StringBuffer();
		Iterator itr = eList.iterator();
		while (itr.hasNext()) {
			strJoined.append("'");
			strJoined.append(itr.next());
			strJoined.append("'");
			if (itr.hasNext())
				strJoined.append(",");
		}
		LoggerHelper.logSystemDebug("TRTtest9 ");
		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);

			String sql = "select first_name,last_name,emplid "
					+ "from mv_field_employee_rbu where emplid IN " + "("
					+ strJoined + ")";
			LoggerHelper.logSystemDebug("TRTtest10 ");
			LoggerHelper.logSystemDebug(sql);
			rs = st.executeQuery(sql);

			while (rs.next()) {

				EmpSearch curr = new EmpSearch();
				LoggerHelper.logSystemDebug("TRTtest29 ");
				curr.setEmplId(rs.getString("emplid"));
				LoggerHelper.logSystemDebug("TRTtest31 ");

				curr.setLastName(rs.getString("last_name".toUpperCase()));
				curr.setFirstName(rs.getString("first_name".toUpperCase()));
				LoggerHelper.logSystemDebug("TRTtest35 ");
				tempList.add(curr);

			}

			ret = (EmpSearch[]) tempList.toArray(new EmpSearch[1]);
			LoggerHelper.logSystemDebug("TRTtest34 ");

		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		// LoggerHelper.logSystemDebug("TRTtest27 "+ ret.length );
		return ret;
	}

	// ends here

	public boolean isTrainingExempted(String emplid, String productCd) {

		ResultSet rs = null;
		PreparedStatement st = null;

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */

			String sql = " select " + "  training_required " + " from  "
					+ " mv_fft_products_training " + " where  "
					+ " emplid = ? and product_cd = ? ";
			st = conn.prepareStatement(sql);
			st.setFetchSize(5000);
			st.setString(1, emplid);
			st.setString(2, productCd);

			rs = st.executeQuery();

			if (rs.next()) {
				String tmp = rs.getString("training_required".toUpperCase());
				if ("Exempted".equals(tmp)) {
					return true;
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}

		return false;

	}

	public Employee[] getPLCEmployees(UserFilter uFilter, String productCD,
			String section) {
		return getPLCEmployees(uFilter, productCD, section, "V_PWRA_PLC_DATA");
	}

	public Employee[] getPLCEmployeesSPF(UserFilter uFilter, String productCD,
			String section) {
		return getPLCEmployees(uFilter, productCD, section, "V_SPF_PLC_DATA");
	}

	private Employee[] getPLCEmployees(UserFilter uFilter, String productCD,
			String section, String tableName) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (productCD.equalsIgnoreCase("Geodon"))
			productCD = "GEOD";
		if (productCD.equalsIgnoreCase("Aricept"))
			productCD = "ARCP";
		if (productCD.equalsIgnoreCase("Lyrica"))
			productCD = "LYRC";
		if (productCD.equalsIgnoreCase("Celebrex"))
			productCD = "CLBR";
		if (productCD.equalsIgnoreCase("Rebif"))
			productCD = "REBF";
		if (productCD.equalsIgnoreCase("General Session"))
			productCD = "PLCA";
		if (productCD.equalsIgnoreCase("Detrol LA"))
			productCD = "DETR";
		if (productCD.equalsIgnoreCase("Chantix"))
			productCD = "CHTX";
		if (productCD.equalsIgnoreCase("Revatio"))
			productCD = "RVTO";
		if (productCD.equalsIgnoreCase("Spiriva"))
			productCD = "SPRV";
		if (productCD.equalsIgnoreCase("Viagra"))
			productCD = "VIAG";

		String sqlQuery = "";
		String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID, ";
		String groupBy = " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + "AND AREA_CD='" + form.getArea() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + "AND TEAM_CD='" + form.getTeam() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + "AND REGION_CD='" + form.getRegion() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + "AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}
		String orderByQuery = " ORDER BY LAST_NAME,FIRST_NAME ";

		if (productCD.equalsIgnoreCase("PLCA")) {
			if (section.equalsIgnoreCase("Not Complete")) {
				sqlQuery = "SELECT " + data + " 'Not Complete' coursestatus "
						+ "FROM " + tableName
						+ " WHERE STATUS = 'I' AND PRODUCT_CD='PLCA'"
						+ condQuery + orderByQuery;
			} else if (section.equalsIgnoreCase("On Leave")) {
				sqlQuery = "SELECT " + data + " 'On Leave' coursestatus "
						+ "FROM " + tableName
						+ " WHERE STATUS = 'L' AND PRODUCT_CD='PLCA'"
						+ condQuery + orderByQuery;
			} else if (section.equalsIgnoreCase("Complete")) {
				sqlQuery = "SELECT " + data + " 'Complete' coursestatus "
						+ "FROM " + tableName
						+ " WHERE STATUS = 'P' AND PRODUCT_CD='PLCA'"
						+ condQuery + orderByQuery;
			}
		} else {
			if (section.equalsIgnoreCase("Not Complete")) {
				// thisSection="I";
				// courseStatus="Not Complete";
				String sqlInCompleteQueryFirst = "SELECT  DISTRICTDESC, TEAMCODE, LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID,  'Not Complete' coursestatus FROM ";
				String sqlInCompleteQueryI = " SELECT " + data
						+ " 'Not Complete' coursestatus " + " FROM  "
						+ tableName + " WHERE STATUS='I' AND PRODUCT_CD='"
						+ productCD + "' ";
				String sqlInCompleteQueryMINUS = " MINUS ";
				String sqlInCompleteQueryL = " SELECT " + data
						+ " 'Not Complete' coursestatus " + " FROM "
						+ tableName + " WHERE STATUS='L'AND PRODUCT_CD='"
						+ productCD + "' ";
				sqlInCompleteQueryI = sqlInCompleteQueryI + condQuery + groupBy;
				sqlInCompleteQueryL = sqlInCompleteQueryL + condQuery + groupBy;
				String sqlInCompleteQuery = sqlInCompleteQueryFirst + "("
						+ sqlInCompleteQueryI + sqlInCompleteQueryMINUS
						+ sqlInCompleteQueryL + ")"
						+ " ORDER BY LASTNAME, FIRSTNAME";
				sqlQuery = sqlInCompleteQuery;

			} else if (section.equalsIgnoreCase("On Leave")) {
				// thisSection="L";
				// courseStatus="On Leave";
				String sqlQueryOnLeave = "SELECT " + data
						+ " 'On Leave' coursestatus " + " FROM " + tableName
						+ " WHERE STATUS='L'  AND PRODUCT_CD='" + productCD
						+ "' ";
				sqlQueryOnLeave = sqlQueryOnLeave + condQuery;
				sqlQuery = sqlQueryOnLeave + groupBy + orderByQuery;

			} else if (section.equalsIgnoreCase("Complete")) {
				// thisSection="P";
				// courseStatus="Completed";
				String sqlCompleteQueryMain = "SELECT  DISTRICTDESC, TEAMCODE, LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID, 'Complete' coursestatus "
						+ " FROM ";
				String sqlCompleteQueryPED = " SELECT "
						+ data
						+ " 'Complete' coursestatus "
						+ " FROM "
						+ tableName
						+ " WHERE STATUS = 'P' AND EXAM_TYPE='PED' AND PRODUCT_CD='"
						+ productCD + "' ";
				String sqlCompleteQuerySCE = " SELECT "
						+ data
						+ " 'Complete' coursestatus "
						+ " FROM "
						+ tableName
						+ " WHERE STATUS = 'P' AND EXAM_TYPE='SCE' AND PRODUCT_CD='"
						+ productCD + "' ";
				String sqlCompleteQueryINTERSECT = " INTERSECT ";
				sqlCompleteQueryPED = sqlCompleteQueryPED + condQuery + groupBy;
				sqlCompleteQuerySCE = sqlCompleteQuerySCE + condQuery + groupBy;
				String sqlCompleteQuery = sqlCompleteQueryMain + "("
						+ sqlCompleteQueryPED + sqlCompleteQueryINTERSECT
						+ sqlCompleteQuerySCE + ")";
				sqlQuery = sqlCompleteQuery + " ORDER BY LASTNAME, FIRSTNAME";
			}
		}
		return getPOAEmployees(sqlQuery, params);
	}

	public Employee[] getPLCOverAllEmployees(UserFilter uFilter,
			String productCD, String section) {
		return getPLCOverAllEmployees(uFilter, productCD, section,
				"V_PWRA_PLC_DATA_OVERALL");
	}

	public Employee[] getPLCOverAllEmployeesSPF(UserFilter uFilter,
			String productCD, String section) {
		return getPLCOverAllEmployees(uFilter, productCD, section,
				"V_SPF_PLC_DATA_OVERALL");
	}

	private Employee[] getPLCOverAllEmployees(UserFilter uFilter,
			String productCD, String section, String tableName) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (section.equalsIgnoreCase("Not Complete")) {
			thisSection = "I";
			// courseStatus="Not Complete";
		} else if (section.equalsIgnoreCase("On Leave")) {
			thisSection = "L";
			// courseStatus="On Leave";
		} else if (section.equalsIgnoreCase("Complete")) {
			thisSection = "P";
			// courseStatus="Completed";
		}
		String sql = " SELECT DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID,PRODUCT_CD, DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status "
				+ " FROM "
				+ tableName
				+ " WHERE OVERALL_STATUS='"
				+ thisSection + "' " + " AND PRODUCT_CD NOT IN ('NA') ";
		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + " AND AREA_CD='" + form.getArea() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + " AND TEAM_CD='" + form.getTeam() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + " AND REGION_CD='" + form.getRegion()
					+ "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + " AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}
		String orderQuery = " order by LASTNAME,FIRSTNAME ";
		sql = sql + condQuery + orderQuery;

		return getPOAOverAllEmployees(sql, params);
	}

	public Employee[] getGNSMOverAllEmployees(UserFilter uFilter,
			String productCD, String section) {
		String tableName = "V_GNSM_DATA_OVERALL";
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (section.equalsIgnoreCase("Not Complete")) {
			thisSection = "I";
		} else if (section.equalsIgnoreCase("On Leave")) {
			thisSection = "L";
		} else if (section.equalsIgnoreCase("Complete")) {
			thisSection = "P";
		}
		String sql = " SELECT DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID,TRT_COURSE_CODE PRODUCT_CD, DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status  "
				+ " FROM "
				+ tableName
				+ " WHERE OVERALL_STATUS='"
				+ thisSection + "' ";

		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + " AND AREA_CD='" + form.getArea() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + " AND TEAM_CD='" + form.getTeam() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + " AND REGION_CD='" + form.getRegion()
					+ "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + " AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}
		String orderQuery = " order by LASTNAME,FIRSTNAME ";
		sql = sql + condQuery + orderQuery;

		return getPOAOverAllEmployees(sql, params);
	}

	public Employee[] getGNSMEmployees(UserFilter uFilter, String productCD,
			String section) {
		String tableName = "V_GNSM_DATA";
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (productCD.equalsIgnoreCase("Geodon Exam 1"))
			productCD = "GEODE1";
		if (productCD.equalsIgnoreCase("Geodon Exam 2"))
			productCD = "GEODE2";
		if (productCD.equalsIgnoreCase("Geodon Learning System Survey"))
			productCD = "GEODSY";
		if (productCD.equalsIgnoreCase("Video Link"))
			productCD = "GEODVL";

		String sqlQuery = "";
		String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
		// String groupBy =
		// " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + "AND AREA_CD='" + form.getArea() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + "AND TEAM_CD='" + form.getTeam() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + "AND REGION_CD='" + form.getRegion() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + "AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}
		String orderByQuery = " ORDER BY LAST_NAME,FIRST_NAME ";

		if (section.equalsIgnoreCase("Not Complete")) {
			sqlQuery = "SELECT " + data
					+ " , 'Not Complete' coursestatus  FROM " + tableName
					+ " WHERE STATUS = 'I' AND TRT_COURSE_CODE = '" + productCD
					+ "' " + condQuery;
		} else if (section.equalsIgnoreCase("On Leave")) {
			sqlQuery = "SELECT " + data + ", 'On Leave' coursestatus FROM "
					+ tableName + " WHERE STATUS = 'L' AND TRT_COURSE_CODE = '"
					+ productCD + "' " + condQuery;
		} else if (section.equalsIgnoreCase("Complete")) {
			sqlQuery = "SELECT " + data + ", 'Complete' coursestatus FROM "
					+ tableName + " WHERE STATUS = 'P' AND TRT_COURSE_CODE = '"
					+ productCD + "' " + condQuery;
		}
		sqlQuery = sqlQuery + orderByQuery;
		return getPOAEmployees(sqlQuery, params);
	}

	/*
	 * Method added for Vista Rx Spiriva enhancement Author: Meenakshi Data:
	 * 12-Sep-2008
	 */
	public Employee[] getVRSOverAllEmployees(UserFilter uFilter,
			String productCD, String section) {
		String tableName = "V_SPIRIVA_DATA_OVERALL";
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (section.equalsIgnoreCase("Not Complete")) {
			thisSection = "I";
		} else if (section.equalsIgnoreCase("On Leave")) {
			thisSection = "L";
		} else if (section.equalsIgnoreCase("Complete")) {
			thisSection = "P";
		}
		String sql = " SELECT DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID,TRT_COURSE_CODE PRODUCT_CD, DECODE(STATUS,'L','On Leave','I','Not Complete', 'P','Complete','NA') status  "
				+ " FROM "
				+ tableName
				+ " WHERE OVERALL_STATUS='"
				+ thisSection + "' ";

		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + " AND AREA_CD='" + form.getArea() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + " AND TEAM_CD='" + form.getTeam() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + " AND REGION_CD='" + form.getRegion()
					+ "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + " AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}
		String orderQuery = " order by LASTNAME,FIRSTNAME ";
		sql = sql + condQuery + orderQuery;

		return getPOAOverAllEmployees(sql, params);
	}

	public Employee[] getVRSEmployees(UserFilter uFilter, String productCD,
			String section) {
		String tableName = "V_VISTASPIRIVA_DATA";
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		if (productCD.equalsIgnoreCase("Spiriva Exam 1"))
			productCD = "VISTASPRV1";
		if (productCD.equalsIgnoreCase("Spiriva Exam 2"))
			productCD = "VISTASPRV2";

		String sqlQuery = "";
		String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
		// String groupBy =
		// " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + "AND AREA_CD='" + form.getArea() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + "AND TEAM_CD='" + form.getTeam() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + "AND REGION_CD='" + form.getRegion() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + "AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}
		String orderByQuery = " ORDER BY LAST_NAME,FIRST_NAME ";

		if (section.equalsIgnoreCase("Not Complete")) {
			sqlQuery = "SELECT " + data
					+ " , 'Not Complete' coursestatus  FROM " + tableName
					+ " WHERE STATUS = 'I' AND TRT_COURSE_CODE = '" + productCD
					+ "' " + condQuery;
		} else if (section.equalsIgnoreCase("On Leave")) {
			sqlQuery = "SELECT " + data + ", 'On Leave' coursestatus FROM "
					+ tableName + " WHERE STATUS = 'L' AND TRT_COURSE_CODE = '"
					+ productCD + "' " + condQuery;
		} else if (section.equalsIgnoreCase("Complete")) {
			sqlQuery = "SELECT " + data + ", 'Complete' coursestatus FROM "
					+ tableName + " WHERE STATUS = 'P' AND TRT_COURSE_CODE = '"
					+ productCD + "' " + condQuery;
		}
		sqlQuery = sqlQuery + orderByQuery;
		return getPOAEmployees(sqlQuery, params);
	}

	public EmpSearchGNSM[] getVRSEmployees(EmplSearchForm eform,
			UserSession uSession) {

		EmpSearchGNSM[] ret = null;
		String sql = "SELECT EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD ";
		sql = sql
				+ " ,TRT_COURSE_NAME,DECODE(STATUS,'I','Registered','P','Complete','L','On-Leave') STATUS ";
		sql = sql
				+ " ,DECODE(OVERALL_STATUS,'I','Registered','P','Complete','L','On-Leave')  OVERALL_STATUS ";
		sql = sql + " FROM V_SPIRIVA_DATA_OVERALL e ";

		StringBuffer criteria = new StringBuffer();

		TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();

		boolean firstWhere = true;

		if (form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER) {
			criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
			firstWhere = false;
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER) {
			criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			firstWhere = false;
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER) {
			criteria.append(" where e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			criteria.append(" and e.district_id = '" + form.getDistrict()
					+ "' ");
			firstWhere = false;
		}

		if (!Util.isEmpty(eform.getFname()) || !Util.isEmpty(eform.getLname())) {
			if (!Util.isEmpty(eform.getFname())) {
				if (firstWhere) {
					criteria.append(" where ");
					firstWhere = false;
				} else {
					criteria.append(" and ");
				}
				criteria.append(" (upper(e.first_name) like '"
						+ eform.getFname().toUpperCase().trim() + "%'  ) ");
			}
			if (!Util.isEmpty(eform.getLname())) {
				if (firstWhere) {
					criteria.append(" where ");
					firstWhere = false;
				} else {
					criteria.append(" and ");
				}
				criteria.append(" upper(e.last_name) like '"
						+ eform.getLname().toUpperCase().trim() + "%' ");
			}
		} else if (!Util.isEmpty(eform.getEmplid())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.emplid='" + eform.getEmplid().trim() + "'");
		} else if (!Util.isEmpty(eform.getTerrId())) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.territory_id='" + eform.getTerrId().trim()
					+ "'");
		}

		if (!uSession.getUser().isAdmin()) {
			if (firstWhere) {
				criteria.append(" where ");
				firstWhere = false;
			} else {
				criteria.append(" and ");
			}
			criteria.append(" e.cluster_cd='" + uSession.getUser().getCluster()
					+ "'");
		}
		criteria.append(" order by e.emplid,TRT_COURSE_NAME ");

		sql = sql + criteria;

		log.info("Search SQL FOR THE EMPLOYEE---" + sql);

		ResultSet rs = null;
		Statement st = null;

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(5000);

			List tempList = new ArrayList();

			// log.info( sql );
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpSearchGNSM e = new EmpSearchGNSM();
				e.setTeamCode(rs.getString("TEAM_DESC"));
				e.setDistrictDesc(rs.getString("DISTRICT_DESC"));
				e.setLastName(rs.getString("LAST_NAME".toUpperCase()));
				e.setFirstName(rs.getString("FIRST_NAME".toUpperCase()));
				e.setRole(rs.getString("ROLE_CD"));
				e.setEmplId(rs.getString("EMPLID"));
				e.setActivityName(rs.getString("TRT_COURSE_NAME"));
				e.setActivityStatus(rs.getString("STATUS"));
				e.setOverallStatus(rs.getString("OVERALL_STATUS"));
				tempList.add(e);
			}

			ret = new EmpSearchGNSM[tempList.size()];

			for (int j = 0; j < tempList.size(); j++) {
				ret[j] = (EmpSearchGNSM) tempList.get(j);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}
		return ret;
	}

	/* End of addition */

	public Employee[] getMSEPIEmployees(UserFilter uFilter, String productCD,
			String section) {
		String tableName = "V_MSEPI_NSM_DATA";
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		productCD = "MSEPIATT";

		String sqlQuery = "";
		String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + "AND AREA_CD='" + form.getArea() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + "AND TEAM_CD='" + form.getTeam() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + "AND REGION_CD='" + form.getRegion() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + "AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}
		String orderByQuery = " ORDER BY LAST_NAME,FIRST_NAME ";

		if (section.equalsIgnoreCase("Not Complete")) {
			sqlQuery = "SELECT " + data
					+ " , 'Not Complete' coursestatus  FROM " + tableName
					+ " WHERE STATUS = 'I' AND TRT_COURSE_CODE = '" + productCD
					+ "' " + condQuery;
		} else if (section.equalsIgnoreCase("On Leave")) {
			sqlQuery = "SELECT " + data + ", 'On Leave' coursestatus FROM "
					+ tableName + " WHERE STATUS = 'L' AND TRT_COURSE_CODE = '"
					+ productCD + "' " + condQuery;
		} else if (section.equalsIgnoreCase("Complete")) {
			sqlQuery = "SELECT " + data + ", 'Complete' coursestatus FROM "
					+ tableName + " WHERE STATUS = 'P' AND TRT_COURSE_CODE = '"
					+ productCD + "' " + condQuery;
		}
		sqlQuery = sqlQuery + orderByQuery;
		return getPOAEmployees(sqlQuery, params);
	}

	public Employee[] getGNSMEmployeesAttendance(UserFilter uFilter,
			String productCD, String section) {
		String tableName = "";
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		String sqlQuery = "";
		String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
		// String groupBy =
		// " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + "AND AREA_CD='" + form.getArea() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + "AND TEAM_CD='" + form.getTeam() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + "AND REGION_CD='" + form.getRegion() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + "AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}
		String orderByQuery = " ORDER BY LASTNAME,FIRSTNAME ";

		if (section.equalsIgnoreCase("Not Complete")) {
			// tableName = " V_GNSM_ATTENDANCE_DATA ";
			// sqlQuery = "SELECT "+data
			// +" , 'Not Complete' coursestatus  FROM "+ tableName +
			// " WHERE STATUS = 'I' " +condQuery;
			tableName = " ( SELECT " + data
					+ " FROM V_GNSM_ATTENDANCE_DATA WHERE STATUS = 'I' "
					+ condQuery;
			tableName = tableName + " MINUS (  SELECT " + data
					+ " FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'P' "
					+ condQuery;
			tableName = tableName + " UNION  SELECT " + data
					+ " FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'L' "
					+ condQuery + " ) ) ";
			sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID , 'Not Complete' coursestatus FROM "
					+ tableName;
		} else if (section.equalsIgnoreCase("On Leave")) {
			tableName = "  ( SELECT " + data
					+ " FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'L' "
					+ condQuery;
			tableName = tableName + " MINUS  SELECT " + data
					+ " FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'P' "
					+ condQuery + ") ";
			sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID, 'On Leave' coursestatus FROM "
					+ tableName;
		} else if (section.equalsIgnoreCase("Complete")) {
			tableName = " V_GNSM_ATTENDANCE_DATA ";
			sqlQuery = "SELECT " + data + " , 'Complete' coursestatus  FROM "
					+ tableName + " WHERE STATUS = 'P' " + condQuery;
			/*
			 * tableName =
			 * " ( SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE STATUS = 'P' "
			 * ; tableName = tableName+" MINUS (  SELECT "+data+
			 * " FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'I' "; tableName =
			 * tableName+" UNION  SELECT "+data+
			 * " FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'L' ) ) "; sqlQuery
			 * =
			 * "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID , 'Complete' coursestatus FROM "
			 * +tableName + condQuery;
			 */
		}
		sqlQuery = sqlQuery + orderByQuery;
		return getPOAEmployees(sqlQuery, params);
	}

	/*
	 * Added for the Vista Rx Spiriva enhancement Auhor: Meenakshi
	 * Date:13-Sep-2008
	 */

	public Employee[] getVRSEmployeesAttendance(UserFilter uFilter,
			String productCD, String section) {
		String tableName = "";
		TerritoryFilterForm form = uFilter.getFilterForm();
		Employee[] ret = null;
		String thisSection = "";
		String[] params = new String[0];
		String courseStatus = "";
		String sqlQuery = "";
		String data = " DISTRICT_DESC DISTRICTDESC, TEAM_DESC TEAMCODE, LAST_NAME LASTNAME, FIRST_NAME FIRSTNAME, ROLE_CD ROLE, EMAIL_ADDRESS EMAIL, EMPLID EMPLID ";
		// String groupBy =
		// " GROUP BY EMPLID,TEAM_DESC,DISTRICT_DESC,LAST_NAME,FIRST_NAME,ROLE_CD,EMAIL_ADDRESS ";
		// ConditionQuery
		String condQuery = "";
		if (!"ALL".equalsIgnoreCase(form.getArea())) {
			condQuery = condQuery + "AND AREA_CD='" + form.getArea() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			condQuery = condQuery + "AND TEAM_CD='" + form.getTeam() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getRegion())) {
			condQuery = condQuery + "AND REGION_CD='" + form.getRegion() + "' ";
		}
		if (!"ALL".equalsIgnoreCase(form.getDistrict())) {
			condQuery = condQuery + "AND DISTRICT_ID='" + form.getDistrict()
					+ "' ";
		}
		String orderByQuery = " ORDER BY LASTNAME,FIRSTNAME ";

		if (section.equalsIgnoreCase("Not Complete")) {
			// tableName = " V_GNSM_ATTENDANCE_DATA ";
			// sqlQuery = "SELECT "+data
			// +" , 'Not Complete' coursestatus  FROM "+ tableName +
			// " WHERE STATUS = 'I' " +condQuery;
			tableName = " ( SELECT " + data
					+ " FROM V_SPIRIVA_ATTENDANCE_DATA WHERE STATUS = 'I' "
					+ condQuery;
			tableName = tableName + " MINUS (  SELECT " + data
					+ " FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'P' "
					+ condQuery;
			tableName = tableName + " UNION  SELECT " + data
					+ " FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'L' "
					+ condQuery + " ) ) ";
			sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID , 'Not Complete' coursestatus FROM "
					+ tableName;
		} else if (section.equalsIgnoreCase("On Leave")) {
			tableName = "  ( SELECT " + data
					+ " FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'L' "
					+ condQuery;
			tableName = tableName + " MINUS  SELECT " + data
					+ " FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'P' "
					+ condQuery + ") ";
			sqlQuery = "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID, 'On Leave' coursestatus FROM "
					+ tableName;
		} else if (section.equalsIgnoreCase("Complete")) {
			tableName = " V_SPIRIVA_ATTENDANCE_DATA ";
			sqlQuery = "SELECT " + data + " , 'Complete' coursestatus  FROM "
					+ tableName + " WHERE STATUS = 'P' " + condQuery;
			/*
			 * tableName =
			 * " ( SELECT "+data+" FROM V_GNSM_ATTENDANCE_DATA WHERE STATUS = 'P' "
			 * ; tableName = tableName+" MINUS (  SELECT "+data+
			 * " FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'I' "; tableName =
			 * tableName+" UNION  SELECT "+data+
			 * " FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'L' ) ) "; sqlQuery
			 * =
			 * "SELECT DISTRICTDESC, TEAMCODE,  LASTNAME, FIRSTNAME, ROLE, EMAIL, EMPLID , 'Complete' coursestatus FROM "
			 * +tableName + condQuery;
			 */
		}
		sqlQuery = sqlQuery + orderByQuery;
		return getPOAEmployees(sqlQuery, params);
	}

	/* End of addition */

	/* Adding set conditions for Sales Org details and Sales Position ID for RBU */
	public static Employee[] convertRecords(ResultSet rs) throws Exception {
		Employee[] ret = null;
		List tempList = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();

		int ncols = rsmd.getColumnCount();

		HashMap fields = new HashMap();

		for (int i = 1; i <= ncols; i++) {
			String key = rsmd.getColumnName(i);
			fields.put(key, "yes");
		}
		while (rs.next()) {
			Employee curr = new Employee();
			curr.setEmplId(rs.getString("EMPLID"));

			if (fields.containsKey("GUID"))
				curr.setGuid((rs.getString("guid")));

			if (fields.containsKey("GEOTYPE"))
				curr.setGeoType((rs.getString("GEOTYPE")));

			if (fields.containsKey("fieldactive".toUpperCase()))
				curr.setEmployeeStatus(rs.getString("fieldactive"));

			if (fields.containsKey("areaCd".toUpperCase()))
				curr.setAreaCd(rs.getString("areaCd".toUpperCase()));

			if (fields.containsKey("areaDesc".toUpperCase()))
				curr.setAreaDesc(rs.getString("areaDesc".toUpperCase()));

			if (fields.containsKey("regionCd".toUpperCase()))
				curr.setRegionCd(rs.getString("regionCd".toUpperCase()));

			if (fields.containsKey("regionDesc".toUpperCase()))
				curr.setRegionDesc(rs.getString("regionDesc".toUpperCase()));

			if (fields.containsKey("districtId".toUpperCase()))
				curr.setDistrictId(rs.getString("districtId".toUpperCase()));

			if (fields.containsKey("districtDesc".toUpperCase()))
				curr.setDistrictDesc(rs.getString("districtDesc".toUpperCase()));
			if (fields.containsKey("territoryId".toUpperCase()))
				curr.setTerritoryId(rs.getString("territoryId".toUpperCase()));
			if (fields.containsKey("role".toUpperCase()))
				curr.setRole(rs.getString("role".toUpperCase()));
			if (fields.containsKey("teamCode".toUpperCase()))
				curr.setTeamCode(rs.getString("teamCode".toUpperCase()));
			if (fields.containsKey("clusterCode".toUpperCase()))
				curr.setClusterCode(rs.getString("clusterCode".toUpperCase()));

			/* Added for RBU */
			if (fields.containsKey("geography_id".toUpperCase())) {
				curr.setGeographyId((rs.getString("geography_id")));
				System.out.print(rs.getString("geography_id"));
			}
			if (fields.containsKey("roledesc".toUpperCase()))
				curr.setRoleDesc((rs.getString("roledesc")));
			if (fields.containsKey("bu".toUpperCase()))
				curr.setBusinessUnit((rs.getString("bu")));
			if (fields.containsKey("geoDesc".toUpperCase()))
				curr.setGeographyDesc((rs.getString("geoDesc")));
			if (fields.containsKey("geoType".toUpperCase()))
				curr.setGeographyType((rs.getString("geoType")));
			if (fields.containsKey("salesPositionId".toUpperCase()))
				curr.setSalesPositionId(rs.getString("salesPositionId"
						.toUpperCase()));
			if (fields.containsKey("salesPositionDesc".toUpperCase()))
				curr.setSalesPostionDesc(rs.getString("salesPositionDesc"
						.toUpperCase()));
			if (fields.containsKey("reportsToSalesPosition".toUpperCase()))
				curr.setReportsToSalesPostion(rs
						.getString("reportsToSalesPosition".toUpperCase()));
			if (fields.containsKey("salesOrgDesc".toUpperCase()))
				curr.setSalesOrgDesc(rs.getString("salesOrgDesc".toUpperCase()));
			if (fields.containsKey("parentGeographyId".toUpperCase()))
				curr.setParentGeographyId(rs.getString("parentGeographyId"
						.toUpperCase()));
			if (fields.containsKey("reportsToEmplid".toUpperCase()))
				curr.setReportsToEmplid(rs.getString("reportsToEmplid"
						.toUpperCase()));
			/* End of addition */
			if (fields.containsKey("lastName".toUpperCase()))
				curr.setLastName(rs.getString("lastName".toUpperCase()));
			if (fields.containsKey("firstName".toUpperCase()))
				curr.setFirstName(rs.getString("firstName".toUpperCase()));
			if (fields.containsKey("middleName".toUpperCase()))
				curr.setMiddleName(rs.getString("middleName".toUpperCase()));
			if (fields.containsKey("preferredName".toUpperCase()))
				curr.setPreferredName(rs.getString("preferredName"
						.toUpperCase()));
			if (fields.containsKey("gender".toUpperCase()))
				curr.setGender(rs.getString("gender".toUpperCase()));
			if (fields.containsKey("email".toUpperCase()))
				curr.setEmail(rs.getString("email".toUpperCase()));
			if (fields.containsKey("reportsToEmplid".toUpperCase()))
				curr.setReportsToEmplid(rs.getString("reportsToEmplid"
						.toUpperCase()));
			if (fields.containsKey("hireDate".toUpperCase())) {
				Date hireDate = rs.getDate("hireDate".toUpperCase());
				if (hireDate != null) {
					curr.setHireDate(new Date(hireDate.getTime()));
				}
			}
			/* Code added for CSO Enhancement */
			if (fields.containsKey("salesPositionTypeCode".toUpperCase())) {
				curr.setSalesPositionTypeCode(rs
						.getString("salesPositionTypeCode".toUpperCase()));
			}
			/* End of addition */
			if (fields.containsKey("hireDate".toUpperCase())) {
				Date promoDate = rs.getDate("promoDate".toUpperCase());
				if (promoDate != null)
					curr.setPromoDate(new Date(promoDate.getTime()));
			}
			// Start : Added for phase 2 - Requirement no. 6,5
			if (fields.containsKey("groupCD".toUpperCase())) {
				curr.setGroupCD(rs.getString("groupCD".toUpperCase()));
			}
			tempList.add(curr);
		}
		ret = new Employee[tempList.size()];
		for (int j = 0; j < tempList.size(); j++) {
			ret[j] = (Employee) tempList.get(j);
		}
		return ret;
	}

	public Employee[] getEmployees(TerritoryFilterForm form, UserFilter uFilter, String additionalCriteriaSql, boolean isdetail ) {
		Employee[] ret = null;
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[0];
        criteria.append( " e.emplid = e.emplid ");
       /* if(!"ALL".equalsIgnoreCase(form.getTeam())){
            criteria.append(" AND e.new_team_cd='"+form.getTeam()+"' ");
        } */
         /* Added for RBU changes */
        if(!"ALL".equalsIgnoreCase(form.getSalesOrg())){
            criteria.append(" AND e.GROUP_CD='"+form.getSalesOrg()+"' ");
        }
        /*End of addition */
		/*if ( form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER ) {
			/*if (uFilter.isAdmin()) {
				params = new String[0];
			} else {
				criteria.append(" and e.cluster_cd = ? ");
				params = new String[1];
				params[1] = uFilter.getClusterCode();
			}
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
	 		criteria.append(" and e.area_cd = ? ");
			//if (uFilter.isAdmin()) {
				params = new String[1];
			//} else {
			//	criteria.append(" and e.cluster_cd = ? ");
			//	params = new String[2];
			//	params[1] = uFilter.getClusterCode();
			//}
			params[0] = form.getArea();
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
	 		criteria.append(" and e.area_cd = ? ");
	 		criteria.append(" and e.region_cd = ? ");
			//if (uFilter.isAdmin()) {
				params = new String[2];
			//} else {
			//	criteria.append(" and e.cluster_cd = ? ");
			//	params = new String[3];
			//	params[2] = uFilter.getClusterCode();
			//}
			params[0] = form.getArea();
			params[1] = form.getRegion();
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
	 		criteria.append(" and e.area_cd = ? ");
	 		criteria.append(" and e.region_cd = ? ");
	 		criteria.append(" and e.district_id = ? ");
			//if (uFilter.isAdmin()) {
				params = new String[3];
			//} else {
			//	criteria.append(" and e.cluster_cd = ? ");
			//	params = new String[4];
			//	params[3] = uFilter.getClusterCode();
			//}
			params[0] = form.getArea();
			params[1] = form.getRegion();
			params[2] = form.getDistrict();
		}
        */
	/*	if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_ALL_GEO_FILTER ) {
		} else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL1_FILTER ) {
	 		criteria.append(" and e.GEO_DESC in (?) ");
				params = new String[1];
                params[0] = form.getGeoLevel1();
		} else if ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL2_FILTER ) {
	 		criteria.append(" and e.GEO_DESC in (?,?) ");
			params[0] = form.getGeoLevel1();
			params[1] = form.getGeoLevel2();
		} else if  ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL3_FILTER) {
            criteria.append(" and e.GEO_DESC in (?,?,?) ");
            params = new String[3];
			params[0] = form.getGeoLevel1();
			params[1] = form.getGeoLevel2();
			params[2] = form.getGeoLevel3();
		}
        else if  ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL4_FILTER) {
            criteria.append(" and e.GEO_DESC in (?,?,?,?) ");
            params = new String[4];
			params[0] = form.getGeoLevel1();
			params[1] = form.getGeoLevel2();
			params[2] = form.getGeoLevel3();
            params[3] = form.getGeoLevel4();
		}
        else if  ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL5_FILTER) {
            criteria.append(" and e.GEO_DESC in (?,?,?,?,?) ");
            params = new String[5];
			params[0] = form.getGeoLevel1();
			params[1] = form.getGeoLevel2();
			params[2] = form.getGeoLevel3();
            params[3] = form.getGeoLevel4();
            params[4] = form.getGeoLevel5();
		}
          else if  ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL6_FILTER) {
            criteria.append(" and e.GEO_DESC in (?,?,?,?,?,?) ");
            params = new String[6];
			params[0] = form.getGeoLevel1();
			params[1] = form.getGeoLevel2();
			params[2] = form.getGeoLevel3();
            params[3] = form.getGeoLevel4();
            params[4] = form.getGeoLevel5();
            params[5] = form.getGeoLevel6();
		}
          else if  ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL7_FILTER) {
            criteria.append(" and e.GEO_DESC in (?,?,?,?,?,?,?) ");
            params = new String[7];
			params[0] = form.getGeoLevel1();
			params[1] = form.getGeoLevel2();
			params[2] = form.getGeoLevel3();
            params[3] = form.getGeoLevel4();
            params[4] = form.getGeoLevel5();
            params[5] = form.getGeoLevel6();
            params[6] = form.getGeoLevel7();
		}
          else if  ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL8_FILTER) {
            criteria.append(" and e.GEO_DESC in (?,?,?,?,?,?,?,?) ");
            params = new String[8];
			params[0] = form.getGeoLevel1();
			params[1] = form.getGeoLevel2();
			params[2] = form.getGeoLevel3();
            params[3] = form.getGeoLevel4();
            params[4] = form.getGeoLevel5();
            params[5] = form.getGeoLevel6();
            params[6] = form.getGeoLevel7();
            params[7] = form.getGeoLevel8();
		}
         else if  ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL9_FILTER) {
            criteria.append(" and e.GEO_DESC in (?,?,?,?,?,?,?,?,?) ");
            params = new String[9];
			params[0] = form.getGeoLevel1();
			params[1] = form.getGeoLevel2();
			params[2] = form.getGeoLevel3();
            params[3] = form.getGeoLevel4();
            params[4] = form.getGeoLevel5();
            params[5] = form.getGeoLevel6();
            params[6] = form.getGeoLevel7();
            params[7] = form.getGeoLevel8();
            params[8] = form.getGeoLevel9();
		}
         else if  ( form.getNewSelectionType() == TerritoryFilterForm.TYPE_GEOLEVEL10_FILTER) {
            criteria.append(" and e.GEO_DESC in (?,?,?,?,?,?,?,?,?,?) ");
            params = new String[10];
			params[0] = form.getGeoLevel1();
			params[1] = form.getGeoLevel2();
			params[2] = form.getGeoLevel3();
            params[3] = form.getGeoLevel4();
            params[4] = form.getGeoLevel5();
            params[5] = form.getGeoLevel6();
            params[6] = form.getGeoLevel7();
            params[7] = form.getGeoLevel8();
            params[8] = form.getGeoLevel9();
            params[9] = form.getGeoLevel10();
		} */

		String sql = "";
        if ( isdetail ) {
            sql = employeSql + criteria.toString() + additionalCriteriaSql;
        } else {
            sql = basicemployeSql + criteria.toString() + additionalCriteriaSql;
        }
		System.out.println("FINAL SEARCH SQL"+sql);
		//log.info(sql);
		return getEmployees ( sql,params ) ;
	}
    

	public NAUserSearch[] getNAEmployees(NonAtlasEmployeeSearchForm eform,
			UserSession uSession) {
		System.out.println("!!!!NAUserSearch@@@@@@");
		StringBuffer criteria = new StringBuffer();
		System.out.println("HERE IT COMES---");
		String FName = eform.getFname();
		String LName = eform.getLname();
		System.out.println("FirstName: " + FName);
		System.out.println("LastName: " + LName);
		return getNAEmployeesSearch(FName, LName);
	}

	private NAUserSearch[] getNAEmployeesSearch(String strFirstName,
			String strLastName) {
		NAUserSearch[] res = null;

		List tempList = new ArrayList();
		System.out.println("inside PXED search");
		try {

			System.out.println("inside search pxed users");
			PersonCriteria criteria = new PersonCriteria(null, strFirstName,
					strLastName, null, null);
			List people = Security.search(criteria);// Search providing a list
													// holding PersonTo objects
			String strNtDomain = "";
			String strUserName = "";
			if (people != null) {
				Iterator iterator = people.iterator();
				System.out.println("SIZE " + people.size());

				while (iterator.hasNext()) { // setting values in GALUser dto.
					System.out.println("********************************");
					PersonTO person = (PersonTO) iterator.next();

					// naUser[count].FIRST_NAME = person.getFirstName();
					// naUser[count].LAST_NAME = person.getLastName();

					System.out.println("PERSON TO VALUES \n"
							+ person.getCommonName() + " WORKFORCEID \n"
							+ person.getDepartment() + " DEPT NAME \n"
							+ person.getDescription() + " DESC \n"
							+ person.getEmployeeStatus() + " EMP STATUS \n"
							+ person.getEmployeeType() + " EMP TYPE \n"
							+ person.getFirstName() + " FIRST NAME \n"
							+ person.getGuid() + " GUID \n"
							+ person.getLastName() + " LAST NAME \n"
							+ person.getLocation() + " LOCATION \n"
							+ person.getMiddleInitial() + " INITIAL \n"
							+ person.getNtDomain() + " NT DOMAIN \n"
							+ person.getPhone() + " PHONE \n"
							+ person.getSmtpAddress() + " SMTP \n"
							+ person.getTitle() + " TITLE \n"
							+ person.getUserName() + " USER NAME\n");

					// TODO: Confirm that Domain and NT ID both are coming here
					if (person.getNtDomain() == null) {
						strNtDomain = "-";
					} else {
						strNtDomain = person.getNtDomain();
					}
					if (person.getUserName() == null) {
						strUserName = "-";
					} else {
						strUserName = person.getUserName();
					}
					// String NT_ACCOUNT = strNtDomain+"\\"+strUserName;
					NAUserSearch curr = new NAUserSearch();
					curr.setFirstName(person.getFirstName());
					curr.setLastName(person.getLastName());
					curr.setNtacct(person.getUserName());
					curr.setEmailid(person.getSmtpAddress());
					tempList.add(curr);
				}
				res = new NAUserSearch[tempList.size()];

				for (int j = 0; j < tempList.size(); j++) {
					res[j] = (NAUserSearch) tempList.get(j);
				}
			} else {
				// System.out.println("THE SEARCH CRITERIA DID NOT FETCH ANY RECORD");
				return null;
			}

		} catch (CSLSystemException p) {
			System.out.println("EXCEPTION WHILE QUERYING");
			p.printStackTrace();
			return null;
		} catch (Exception p) {
			System.out.println("EXCEPTION DUE TO SERVICE ACCOUNT");
			p.printStackTrace();
			return null;
		}

		return res;
	}

	public List getAccessDetail(List groups) {
		System.out.print("Inside the getAccessDetail method");
		String grpSql = "select SAVE,SUBMIT from sce_evaluation_access where upper(usergroup)IN (";
		List GrpAcc = new ArrayList();
		StringBuffer where = new StringBuffer();
		for (int i = 0; i < groups.size(); i++) {
			String userGroup = (String) groups.get(i);
			if (i > 0) {
				where.append(",Upper('" + userGroup + "')");
			} else {
				where.append("Upper('" + userGroup + "')");
			}
		}
		String sql = grpSql + where.toString() + ")";
		GrpAcc = getGroupDetail(sql);
		System.out.println("LETS GET IT TILL THIS" + sql);
		return GrpAcc;
	}

	private List getGroupDetail(String sql) {
		List result = new ArrayList();
		ResultSet rs = null;
		Statement st = null;

		// Connection conn = null;
		/** Infosys - Weblogic to Jboss migration changes starts here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/** Infosys - Weblogic to Jboss migration changes ends here */
		try {

			Timer timer = new Timer();
			/** Infosys - Weblogic to Jboss migration changes starts here */
			/*
			 * Context ctx = new InitialContext(); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
			 * ds.getConnection();
			 */
			/** Infosys - Weblogic to Jboss migration changes ends here */
			st = conn.createStatement();
			st.setFetchSize(20);

			List tempList = new ArrayList();

			// log.info( sql );
			System.out.println("before execution");
			rs = st.executeQuery(sql);
			System.out.println("after execution");
			st.setFetchSize(20);
			while (rs.next()) {
				GroupAccessDetail se = new GroupAccessDetail();
				se.setIsSave(rs.getString("SAVE".toUpperCase()));
				se.setIsSubmit(rs.getString("SUBMIT".toUpperCase()));
				result.add(se);
			}
			System.out.println("After Query");
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					log.error(e2, e2);
				}
			}
		}

		return result;
	}

	// Start: Added for TRT Phase 2 - Requirement no. 6
	public List executeSql(String sql) {
		List result = DBUtil.executeSql(sql, AppConst.APP_DATASOURCE);
		return result;
	}

	public List getReportingEmployeeDetails(String empId) {
		List ret1 = new ArrayList();
		List ret2 = new ArrayList();
		String sql = "";
		String employeeSalesPositionType = "";
		String salesPositionId = "";
		String sql1 = "select SALES_POSITION_TYPE_CD,SALES_POSITION_ID  from MV_FIELD_EMPLOYEE_RBU WHERE EMPLID='"
				+ empId + "' ";
		ret1 = executeSql(sql1);
		Map detailsMap1 = new HashMap();
		detailsMap1 = (HashMap) ret1.get(0);
		employeeSalesPositionType = (String) detailsMap1
				.get("SALES_POSITION_TYPE_CD");
		salesPositionId = (String) detailsMap1.get("SALES_POSITION_ID");
		System.out.println(ret1);
		System.out.println("** Sales Position Id   " + salesPositionId);

		String sql2 = "select distinct SALES_POSITION_TYPE_CD from ROLE_CONFIG";
		ret2 = executeSql(sql2);
		String currentSalesPositionTypeCD = "";
		for (int i = 0; i < ret2.size(); i++) {
			Map detailsMap = new HashMap();
			detailsMap = (HashMap) ret2.get(i);
			currentSalesPositionTypeCD = (String) detailsMap
					.get("SALES_POSITION_TYPE_CD");
		}
		if (currentSalesPositionTypeCD
				.equalsIgnoreCase(employeeSalesPositionType)) {
			sql = "select level , m.last_name as lastname,m.first_name as firstname,m.emplid as employeeid,m.role_cd as role,m.sales_position_id as salespositionid,m.sales_position_id_desc as salespositiondesc,m.email_address as emailid,m.sales_group as salesorg,m.nt_id as ntid from  mv_field_employee_rbu m Join tr.mv_field_employee_relation r ON r.emplid = m.emplid where r.related_emplid ="
					+ empId
					+ " connect by prior r.emplid=r.related_emplid start with related_emplid="
					+ empId
					+ " and reports_to_emplid is not null order by lastname ";
		} else {
			// sql =
			// "select level,last_name as lastname,first_name as firstname,emplid as employeeid,role_cd as role,sales_position_id as salespositionid,sales_position_id_desc as salespositiondesc,email_address as emailid,sales_group as salesorg,nt_id as ntid from mv_field_employee_rbu connect by prior emplid=reports_to_emplid start with reports_to_emplid="+empId+" order siblings by lastname";
			sql = " select level,last_name as lastname,first_name as firstname,emplid as employeeid,role_cd as role,sales_position_id as salespositionid,sales_position_id_desc as salespositiondesc,email_address as emailid,sales_group as salesorg,nt_id as ntid from MV_FIELD_EMPLOYEE_RELATION connect by prior sales_position_id=related_sales_position_id start with related_sales_position_id = ("
					+ salesPositionId + ")" + "order siblings by lastname";
		}

		// String sql =
		// "select level,last_name as lastname,first_name as firstname,emplid as employeeid,role_cd as role,sales_position_id as salespositionid,sales_position_id_desc as salespositiondesc,email_address as emailid,sales_group as salesorg,nt_id as ntid from mv_field_employee_rbu connect by prior emplid=reports_to_emplid start with reports_to_emplid="+empId+" order siblings by lastname";
		List result = executeSql(sql);
		log.info(sql);
		System.out.println(sql);
		return result;
	}
/*
	public List getProductStatusForNotGap(String empId) {
		List ret = new ArrayList();
		String sql = "SELECT distinct a.emplid as empID, b.product as productName,b.status as status, b.activityID AS activityID "
				+ "FROM  future_product_alignments a, "
				+ "(SELECT b.product, a.guid, d.status AS status, d.activityfk AS activityID "
				+ "FROM future_product_alignments a,gap_report_codes b,mv_usp_activity_master c,mv_usp_compl_and_reg d,gap_report_roles e "
				+ "WHERE a.product_desc = b.product AND b.course_code = c.code AND b.active = 'Y' AND e.active = 'Y' "
				+ "AND a.role_cd = e.role_cd AND c.activity_pk = d.activityfk AND a.guid = d.emp_no) b, "
				+ "(select max(to_number(deployment_package_id)) as deployment_package_id from future_product_alignments) c "
				+ " WHERE a.guid = b.guid AND a.product_desc = b.product AND a.emplid='"
				+ empId
				+ "' "
				+ "AND a.deployment_package_id = c.deployment_package_id "
				+ " ORDER BY a.emplid,b.product ";
		List result = executeSql(sql);
		log.info(sql);
		System.out.println(sql);
		return result;
	}

	public List getProductStatusForGap(String empId) {
		List ret = new ArrayList();
		String sql = "SELECT distinct a.emplid as empID,  b.product as productName "
				+ "FROM future_product_alignments a, "
				+ "(SELECT b.product, a.guid "
				+ "FROM future_product_alignments a,gap_report_codes b,gap_report_roles c "
				+ "WHERE a.product_desc = b.product AND c.role_cd = a.role_cd AND c.active = 'Y'AND b.active = 'Y'  AND a.emplid='"
				+ empId
				+ "' "
				+ "MINUS "
				+ "SELECT b.product, a.guid "
				+ "FROM future_product_alignments a,gap_report_codes b,mv_usp_activity_master c,mv_usp_compl_and_reg d,gap_report_roles e "
				+ "WHERE a.product_desc = b.product AND b.course_code = c.code AND b.active = 'Y' AND e.active = 'Y' "
				+ "AND a.role_cd = e.role_cd AND c.activity_pk = d.activityfk AND a.guid = d.emp_no  AND a.emplid='"
				+ empId
				+ "' "
				+ ") b, "
				+ "(select max(to_number(deployment_package_id)) as deployment_package_id from future_product_alignments) c "
				+ "WHERE a.guid = b.guid AND a.product_desc = b.product  AND a.emplid='"
				+ empId
				+ "' "
				+ "AND a.deployment_package_id = c.deployment_package_id "
				+ "ORDER BY a.emplid,b.product ";
		List result = executeSql(sql);
		log.info(sql);
		System.out.println(sql);
		return result;
	}
*/
	
	public List getProductStatusForNotGap(String empId) {
		List ret = new ArrayList();
		String sql = "SELECT distinct a.emplid as empID, b.product as productName,b.status as status, b.activityID AS activityID "
				+ "FROM  future_product_alignments a, "
				+ "(SELECT b.product, a.guid, d.status AS status, d.activityfk AS activityID "
				+ "FROM future_product_alignments a,gap_rpt_ffra_codes b,mv_usp_activity_master c,mv_usp_compl_and_reg d "
				+ "WHERE a.product_desc = b.product AND b.course_code = c.code AND b.active = 'Y' AND b.COMPLETION='Y' "
				+ "AND c.activity_pk = d.activityfk AND a.guid = d.emp_no) b, "
				+ "(select max(to_number(deployment_package_id)) as deployment_package_id from future_product_alignments) c "
				+ " WHERE a.guid = b.guid AND a.product_desc = b.product AND a.emplid='"
				+ empId
				+ "' "
				+ "AND a.deployment_package_id = c.deployment_package_id "
				+ " ORDER BY a.emplid,b.product ";
		List result = executeSql(sql);
		log.info(sql);
		System.out.println(sql);
		return result;
	}

	public List getProductStatusForGap(String empId) {
		List ret = new ArrayList();
		String sql = "SELECT distinct a.emplid as empID,  b.product as productName "
				+ "FROM future_product_alignments a, "
				+ "(SELECT b.product, a.guid "
				+ "FROM future_product_alignments a,gap_rpt_ffra_codes b "
				+ "WHERE a.product_desc = b.product AND b.active = 'Y'  AND a.emplid='"
				+ empId
				+ "' "
				+ "MINUS "
				+ "SELECT b.product, a.guid "
				+ "FROM future_product_alignments a,gap_rpt_ffra_codes b,mv_usp_activity_master c,mv_usp_compl_and_reg d "
				+ "WHERE a.product_desc = b.product AND b.course_code = c.code AND b.active = 'Y' "
				+ "AND c.activity_pk = d.activityfk AND a.guid = d.emp_no  AND a.emplid='"
				+ empId
				+ "' "
				+ ") b, "
				+ "(select max(to_number(deployment_package_id)) as deployment_package_id from future_product_alignments) c "
				+ "WHERE a.guid = b.guid AND a.product_desc = b.product  AND a.emplid='"
				+ empId
				+ "' "
				+ "AND a.deployment_package_id = c.deployment_package_id "
				+ "ORDER BY a.emplid,b.product ";
		List result = executeSql(sql);
		log.info(sql);
		System.out.println(sql);
		return result;
	}
	
	
	
	
	
	
	
	public List getTrainingPath(Employee emp) {
		// List pathDetails = new ArrayList();
		String sql = "select c_code.code, c_code .description, c_code. config_id from training_path_config c_code,"
				+ "(select config_id from Training_Path_config where filter_type = 'bu' and code = '"
				+ emp.getBusinessUnit()
				+ "') b_unit, "
				+ "(select config_id from Training_Path_config where filter_type = 'sales_group' and code= '"
				+ emp.getGroupCD()
				+ "') so, "
				+ "(select config_id from Training_Path_config where filter_type = 'role_desc' and code='"
				+ emp.getRole()
				+ "') roleCode "
				+ "where c_code.filter_type = 'alias' "
				+ "And c_code.config_id = b_unit.config_id "
				+ "And c_code.config_id  = so.config_id "
				+ "And c_code.config_id  = roleCode.config_id";
		List pathDetails = executeSql(sql);
		log.info(sql);
		System.out.println(sql);
		if(pathDetails!=null)
		System.out.println(pathDetails.toString());
		String detailpath = null;
		if(pathDetails!=null)
		detailpath = pathDetails.toString();
		// pathDetails.toString()
		return pathDetails;
	}
}
