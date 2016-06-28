package com.pfizer.hander;

import com.pfizer.db.ForecastReport;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ForecastReportHandler {

	protected static final Log log = LogFactory
			.getLog(ManagementFilterHandler.class);

	StringBuffer completedQuery = new StringBuffer();
	StringBuffer registeredQuery = new StringBuffer();
	StringBuffer notCompletedQuery = new StringBuffer();
	StringBuffer notRegisteredQuery = new StringBuffer();
	StringBuffer forecastReportQuery = new StringBuffer();

	private String trackId;
	private String trackLabel = "";

	String selectClause = "";
	String whereClausePt1 = " WHERE b.guid = a.emp_no AND a.activity_pk = ";
	String whereClausePt2 = " AND UPPER (a.status) = ";
	String whereClausePt3 = " AND add_months(b.hire_date, ";
	String whereClausePt4 = " ) < ";
	String whereClausePt5 = " AND b.role_cd IN ";

	String role_cd = "";
	int duration = 0;
	String endDate = "";
	String completedCourseCodes = "";
	String notCompletedCourseCodes = "";
	String registeredCourseCodes = "";
	String notRegisteredCourseCodes = "";

	public void ForecastFilterQuery(String trackId) {
		this.trackId = trackId;

		/*
		 * List fFilter = fHandler.getFilterCriteria(trackId); List fields =
		 * Track.getFields(trackId); List filter = Track.getFilter(trackId);
		 * List otherFilters = track.getOtherFilters(trackId); List eachFilter =
		 * new ArrayList();
		 */

	}

	public void setTrackLabel() {
		if (this.trackLabel.trim().length() == 0) {
			String sql = " Select * from FORECAST_TRACK WHERE TRACK_ID = '"
					+ this.trackId + "'";
			List result = DBUtil.executeSql(sql, AppConst.APP_DATASOURCE);
			if (result != null && result.size() > 0) {
				HashMap ret = (HashMap) result.get(0);
				if (ret.get("TRACK_LABEL") != null
						&& ret.get("TRACK_LABEL").toString().trim().length() > 0)
					System.out.println("-----------" + ret.get("TRACK_LABEL"));
				this.trackLabel = ret.get("TRACK_LABEL").toString().trim();
			}
		}
	}

	public String getTrackLabel() {
		return this.trackLabel;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
		getFilterCriteria();
		setTrackLabel();
	}

	public void createForecastReportQuery(boolean admin, String emplID) {

		if (admin) {
			this.selectClause = "SELECT "
					+ getDisplayFields()
					+ " FROM mv_usp_activity_status a, mv_field_employee_rbu b, "
					+ "(select first_name || ' ' || last_name as manager_name,emplid,email_address from mv_field_employee_rbu) m,"
					+ "(select e.emplid,RTRIM(xmlagg(xmlelement(c, s.product_desc||',')).extract('//text()'),',') AS products "
					+ "from mv_field_employee_rbu e,mv_da_sales_position_product s "
					+ "where e.SALES_POSITION_ID = s.SALES_POSITION_ID "
					+ "group by e.emplid) p ";
			this.whereClausePt1 = " WHERE b.guid = a.emp_no AND b.reports_to_emplid = m.emplid AND p.emplid = b.emplid AND a.activity_pk = ";
		} else {
			this.selectClause = "SELECT "
					+ getDisplayFields()
					+ " FROM mv_usp_activity_status a, mv_field_employee_rbu b,"
					+ "(select first_name || ' ' || last_name as manager_name,emplid,email_address from mv_field_employee_rbu) m,"
					+ "(select e.emplid,RTRIM(xmlagg(xmlelement(c, s.product_desc||',')).extract('//text()'),',') AS products "
					+ "from mv_field_employee_rbu e,mv_da_sales_position_product s "
					+ "where e.SALES_POSITION_ID = s.SALES_POSITION_ID "
					+ "group by e.emplid) p, "
					+ "(select emplid from mv_field_employee_rbu "
					+ "start with emplid = " + emplID
					+ " connect by prior emplid = reports_to_emplid) r ";
			this.whereClausePt1 = " WHERE b.guid = a.emp_no AND b.reports_to_emplid = m.emplid AND b.emplid = r.emplid AND p.emplid = b.emplid AND a.activity_pk = ";
		}

		boolean hasMoreQueries = false;

		HashMap courseGroups = getCompletedCourseGroups(trackId);
		if (courseGroups != null && courseGroups.size() > 0) {
			forecastReportQuery.append("(");
			forecastReportQuery.append(createCompletedQuery(courseGroups));
			hasMoreQueries = true;
		}

		courseGroups = getNotCompletedCourseGroups(trackId);
		if (courseGroups != null && courseGroups.size() > 0) {
			if (hasMoreQueries)
				forecastReportQuery.append(" INTERSECT ");
			else
				forecastReportQuery.append("(");
			forecastReportQuery.append(createNotCompletedQuery(courseGroups));
			hasMoreQueries = true;
		}

		courseGroups = getRegisteredCourseGroups(trackId);
		if (courseGroups != null && courseGroups.size() > 0) {
			if (hasMoreQueries)
				forecastReportQuery.append(" INTERSECT ");
			else
				forecastReportQuery.append("(");
			forecastReportQuery.append(createRegisteredQuery(courseGroups));
			hasMoreQueries = true;
		}

		courseGroups = getNotRegisteredCourseGroups(trackId);
		if (courseGroups != null && courseGroups.size() > 0) {
			if (hasMoreQueries)
				forecastReportQuery.append(" INTERSECT ");
			else
				forecastReportQuery.append("(");
			forecastReportQuery.append(createNotRegisteredQuery(courseGroups));
		}

		if (hasMoreQueries)
			forecastReportQuery.append(" ) ORDER by \"First Name\" asc");

	}

	protected String createCompletedQuery(HashMap courseGroups) {
		completedQuery.append(" ( ");
		String union = "";
		for (int i = 0; i < courseGroups.size(); i++) {
			// completedQuery.append(union);
			List courseCodes = (List) courseGroups.get("Group" + i);// getCourseCodes(trackId,
			// courseGroups.get(i).toString());
			String intersect = "";
			if (courseCodes.size() > 0)
				completedQuery.append(union);
			for (int j = 0; j < courseCodes.size(); j++) {
				completedQuery.append(intersect);
				intersect = "";
				completedQuery.append("(" + selectClause);
				completedQuery.append(whereClausePt1
						+ courseCodes.get(j).toString());
				completedQuery.append(whereClausePt2 + " 'COMPLETE' ");
				completedQuery.append(whereClausePt3 + getDuration()
						+ whereClausePt4);
				completedQuery.append(" TO_DATE ('" + getEndDate()
						+ "','yyyy/mm/dd') ");
				if (getRoleCode().length() > 0){
                    String arr[]=getRoleCode().split(",");
                    StringBuffer sb=new StringBuffer();
                    for(int k=0;k<arr.length;k++){
                        sb.append(arr[k]+"','");
                    }
                    String role=sb.substring(0,sb.length()-3);
					completedQuery.append(whereClausePt5 + " ('" +role
							+ "') ");
                }
				completedQuery.append(" ) ");
				intersect = " INTERSECT ";
			}
			union = "UNION";
		}
		completedQuery.append(" ) ");
     //   System.out.println("Completed Query in forecast"+completedQuery);
		return completedQuery.toString();
	}

	protected String createNotCompletedQuery(HashMap courseGroups) {
		String union = "";
		notCompletedQuery.append(" ( ");
		for (int i = 0; i < courseGroups.size(); i++) {
			// notCompletedQuery.append(union);
			List courseCodes = (List) courseGroups.get("Group" + i);
			String intersect = "";
			if (courseCodes.size() > 0)
				notCompletedQuery.append(union);
			for (int j = 0; j < courseCodes.size(); j++) {
				notCompletedQuery.append(intersect);
				intersect = "";
				notCompletedQuery.append("(" + selectClause);
				notCompletedQuery.append(whereClausePt1
						+ courseCodes.get(j).toString());
				notCompletedQuery.append(whereClausePt2 + " 'EXEMPT' ");
				notCompletedQuery.append(whereClausePt3 + getDuration()
						+ whereClausePt4);
				notCompletedQuery.append(" TO_DATE ('" + getEndDate()
						+ "','yyyy/mm/dd') ");
				if (getRoleCode().length() > 0){
                    String arr[]=getRoleCode().split(",");
                    StringBuffer sb=new StringBuffer();
                    for(int k=0;k<arr.length;k++){
                        sb.append(arr[k]+"','");
                    }
                    String role=sb.substring(0,sb.length()-3);
					notCompletedQuery.append(whereClausePt5 + " ('"+role
                    + "') ) ");
                }
				intersect = " INTERSECT ";
			}
			union = "UNION";
		}
		notCompletedQuery.append(" ) ");
		return notCompletedQuery.toString();
	}

	protected String createRegisteredQuery(HashMap courseGroups) {
		registeredQuery.append(" ( ");
		String union = "";
		for (int i = 0; i < courseGroups.size(); i++) {
			// registeredQuery.append(union);
			List courseCodes = (List) courseGroups.get("Group" + i);
			String intersect = "";
			if (courseCodes.size() > 0)
				registeredQuery.append(union);
			for (int j = 0; j < courseCodes.size(); j++) {
				registeredQuery.append(intersect);
				intersect = "";
				registeredQuery.append("(" + selectClause);
				registeredQuery.append(whereClausePt1
						+ courseCodes.get(j).toString());
				registeredQuery.append(whereClausePt2 + " 'REGISTERED' ");
				registeredQuery.append(whereClausePt3 + getDuration()
						+ whereClausePt4);
				registeredQuery.append(" TO_DATE ('" + getEndDate()
						+ "','yyyy/mm/dd') ");
				if (getRoleCode().length() > 0){
                    String arr[]=getRoleCode().split(",");
                    StringBuffer sb=new StringBuffer();
                    for(int k=0;k<arr.length;k++){
                        sb.append(arr[k]+"','");
                    }
                    String role=sb.substring(0,sb.length()-3);
					registeredQuery.append(whereClausePt5 + " ('"+role
                    + "') ) ");
                }
				intersect = " INTERSECT ";
                
			}
			union = "UNION";
		}
		registeredQuery.append(" ) ");
		return registeredQuery.toString();
	}

	protected String createNotRegisteredQuery(HashMap courseGroups) {
		notRegisteredQuery.append(" ( ");
		String union = "";
		for (int i = 0; i < courseGroups.size(); i++) {
			// notRegisteredQuery.append(union);
			List courseCodes = (List) courseGroups.get("Group" + i);
			String intersect = "";
			if (courseCodes.size() > 0)
				notRegisteredQuery.append(union);
			for (int j = 0; j < courseCodes.size(); j++) {
				notRegisteredQuery.append(intersect);
				intersect = "";
				notRegisteredQuery.append("(" + selectClause);
				notRegisteredQuery.append(whereClausePt1
						+ courseCodes.get(j).toString());
				notRegisteredQuery.append(whereClausePt2 + " 'ASSIGNED' ");
				notRegisteredQuery.append(whereClausePt3 + getDuration()
						+ whereClausePt4);
				notRegisteredQuery.append(" TO_DATE ('" + getEndDate()
						+ "','yyyy/mm/dd') ");
				if (getRoleCode().length() > 0){
                    String arr[]=getRoleCode().split(",");
                    StringBuffer sb=new StringBuffer();
                    for(int k=0;k<arr.length;k++){
                        sb.append(arr[k]+"','");
                    }
                    String role=sb.substring(0,sb.length()-3);
					notRegisteredQuery.append(whereClausePt5 + " ('"+role
                    + "') ) ");
                }
				intersect = " INTERSECT ";
			}
			union = "UNION";
		}
		notRegisteredQuery.append(" ) ");
		return notRegisteredQuery.toString();
	}

	public List executeSql() {
		System.out.println("Forecasting report query :"
				+ forecastReportQuery.toString());
		if (forecastReportQuery.toString().trim().length() > 0) {
			List result = DBUtil.executeSql(forecastReportQuery.toString(),
					AppConst.APP_DATASOURCE, "ordered");
			return result;
		} else
			return new ArrayList();
	}

	public HashMap getCompletedCourseGroups(String trackId) {
		if (completedCourseCodes.trim().length() == 0)
			return null;
		HashMap courseGroups = new HashMap();
		String groups[] = this.completedCourseCodes.split(",");
		System.out.println("completedcourse length" + groups.length);
		for (int i = 0; i < groups.length; i++) {
			ArrayList ret = new ArrayList();
			if (groups[i].trim().length() > 0 && groups[i].indexOf("AND") > 0) {
				String courseCodes[] = groups[i].split("AND");
				System.out.println("AND split " + courseCodes.length);
				for (int j = 0; j < courseCodes.length; j++) {
					System.out.println("index j " + j);
					if (courseCodes[j].trim().length() > 0)
						ret.add(courseCodes[j].trim());
				}
			} else if (groups[i].trim().length() > 0
					&& groups[i].indexOf("OR") == -1)
				ret.add(groups[i].trim());
			System.out.println("before saving in the hashmap");
			courseGroups.put("Group" + i, ret);
		}
		return courseGroups;
	}

	public int getDuration() {
		return this.duration;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public String getRoleCode() {
		return this.role_cd;
	}

	public HashMap getNotCompletedCourseGroups(String trackId) {
		if (notCompletedCourseCodes.trim().length() == 0)
			return null;
		HashMap courseGroups = new HashMap();
		String groups[] = this.notCompletedCourseCodes.split(",");
		for (int i = 0; i < groups.length; i++) {
			ArrayList ret = new ArrayList();
			if (groups[i].trim().length() > 0 && groups[i].indexOf("AND") > 0) {
				String courseCodes[] = groups[i].split("AND");
				for (int j = 0; j < courseCodes.length; j++) {
					if (courseCodes[j].trim().length() > 0)
						ret.add(courseCodes[j].trim());
				}
			} else if (groups[i].trim().length() > 0
					&& groups[i].indexOf("OR") == -1)
				ret.add(groups[i].trim());
			courseGroups.put("Group" + i, ret);
		}
		return courseGroups;
	}

	public HashMap getRegisteredCourseGroups(String trackId) {
		if (registeredCourseCodes.trim().length() == 0)
			return null;
		HashMap courseGroups = new HashMap();
		String groups[] = this.registeredCourseCodes.split(",");
		for (int i = 0; i < groups.length; i++) {
			ArrayList ret = new ArrayList();
			if (groups[i].trim().length() > 0 && groups[i].indexOf("AND") > 0) {
				String courseCodes[] = groups[i].split("AND");
				for (int j = 0; j < courseCodes.length; j++) {
					if (courseCodes[j].trim().length() > 0)
						ret.add(courseCodes[j].trim());
				}
			} else if (groups[i].trim().length() > 0
					&& groups[i].indexOf("OR") == -1)
				ret.add(groups[i].trim());
			courseGroups.put("Group" + i, ret);
		}
		return courseGroups;
	}

	public HashMap getNotRegisteredCourseGroups(String trackId) {
		if (notRegisteredCourseCodes.trim().length() == 0)
			return null;
		HashMap courseGroups = new HashMap();
		String groups[] = this.notRegisteredCourseCodes.split(",");
		for (int i = 0; i < groups.length; i++) {
			ArrayList ret = new ArrayList();
			if (groups[i].trim().length() > 0 && groups[i].indexOf("AND") > 0) {
				String courseCodes[] = groups[i].split("AND");
				for (int j = 0; j < courseCodes.length; j++) {
					if (courseCodes[j].trim().length() > 0)
						ret.add(courseCodes[j].trim());
				}
			} else if (groups[i].trim().length() > 0
					&& groups[i].indexOf("OR") == -1)
				ret.add(groups[i].trim());
			courseGroups.put("Group" + i, ret);
		}
		return courseGroups;
	}

	public String getDisplayFields() {
		String fields = " b.NT_ID as NTId" + ", b.FIRST_NAME as \"First Name\" "
				+ ", b.LAST_NAME as \"Last Name\" "
				+ ", b.ROLE_CD as \"Role Code\""
				+ ", b.SALES_GROUP as \"Sales Org \""
				+ ", m.manager_name as Manager"
				+ ", b.EMAIL_ADDRESS as \"Email Id\"";

		String sql = " Select * from FORECAST_OPTIONAL_FIELDS WHERE TRACK_ID = '"
				+ this.trackId + "'";
		List result = DBUtil
				.executeSql(sql, AppConst.APP_DATASOURCE, "ordered");
		if (result != null && result.size() > 0) {
			HashMap ret = (HashMap) result.get(0);
			for (Iterator i = ret.keySet().iterator(); i.hasNext();) {
				String key = (String) i.next();
				System.out.println("col" + i + " " + key);
			}
			if (ret.get("GENDER") != null
					&& ret.get("GENDER").toString().equalsIgnoreCase("yes"))
				fields = fields + ", b.SEX as Gender";
			if (ret.get("MANAGER_EMAIL") != null
					&& ret.get("MANAGER_EMAIL").toString().equalsIgnoreCase(
							"yes"))
				fields = fields + ",m.email_address as \"Manager's Email\"";
			if (ret.get("SOURCE") != null
					&& ret.get("SOURCE").toString().equalsIgnoreCase("yes"))
				fields = fields + ", b.SALES_POSITION_TYPE_CD as Source";
			if (ret.get("PROMOTION_DATE") != null
					&& ret.get("PROMOTION_DATE").toString().equalsIgnoreCase(
							"yes"))
				fields = fields + ", b.PROMOTION_DATE as \"Promotion Date\"";
			if (ret.get("HIRE_DATE") != null
					&& ret.get("HIRE_DATE").toString().equalsIgnoreCase("yes"))
				fields = fields + ", b.HIRE_DATE as \"Hire Date\"";
			if (ret.get("EMPLOYEE_ID") != null
					&& ret.get("EMPLOYEE_ID").toString()
							.equalsIgnoreCase("yes"))
				fields = fields + ", b.EMPLID as \"Employee ID\"";
			if (ret.get("GUID") != null
					&& ret.get("GUID").toString().equalsIgnoreCase("yes"))
				fields = fields + ", b.GUID as GUID";
			if (ret.get("GEOGRAPHY_DESCR") != null
					&& ret.get("GEOGRAPHY_DESCR").toString().equalsIgnoreCase(
							"yes"))
				fields = fields + ", b.GEO_DESC as \"Geography Description\"";
			if (ret.get("REGIONAL_OFFICE_STATE") != null
					&& ret.get("REGIONAL_OFFICE_STATE").toString()
							.equalsIgnoreCase("yes"))
				fields = fields
						+ ", b.GEOGRAPHY_ID as \"Regional Office State\" ";
			if (ret.get("PRODUCTS") != null
					&& ret.get("PRODUCTS").toString().equalsIgnoreCase("yes"))
				fields = fields + ", p.products as Products ";

		}

		log.info(sql);

		return fields;
	}

	public void getFilterCriteria() {
		//String sql = " Select * from FORECAST_FILTER_CRITERIA WHERE FORECAST_REPORT_ID = '"
		//		+ this.trackId + "'";
        
        String sql = "select FORECAST_REPORT_ID,FORECAST_REPORT_LABEL, role_cd,to_char(start_date,'yyyy/mm/dd') as start_date, "
                +"to_char(end_date,'yyyy/mm/dd') as end_date, hire_or_promotion_date, "
                +" duration,COMPLETED_COURSES,NOT_COMPLETED_COURSES,REGISTERED_COURSES, NOT_REGISTERED_COURSES from Forecast_filter_criteria where forecast_report_ID = '"+this.trackId+"'";
        
		List result = DBUtil
				.executeSql(sql, AppConst.APP_DATASOURCE, "ordered");
		if (result != null && result.size() > 0) {
			HashMap ret = (HashMap) result.get(0);
			for (Iterator i = ret.keySet().iterator(); i.hasNext();) {
				String key = (String) i.next();
				System.out.println("col" + i + " " + key + " " + ret.get(key));
			}
			if (ret.get("ROLE_CD") != null
					&& ret.get("ROLE_CD").toString().trim().length() > 0)
				this.role_cd = ret.get("ROLE_CD").toString().trim();
			if (ret.get("HIRE_OR_PROMOTION_DATE") != null){
                if (ret.get("HIRE_OR_PROMOTION_DATE").toString().equalsIgnoreCase("Y"))
				whereClausePt3 = " AND add_months(b.hire_date, ";
                else whereClausePt3 = " AND add_months(b.promotion_date, ";
            }
            if (ret.get("DURATION") != null
					&& ret.get("DURATION").toString().trim().length() > 0)
				this.duration = Integer.parseInt(ret.get("DURATION").toString()
						.trim());
			if (ret.get("END_DATE") != null
					&& ret.get("END_DATE").toString().trim().length() > 0)
				this.endDate = ret.get("END_DATE").toString().trim();
			if (ret.get("COMPLETED_COURSES") != null
					&& ret.get("COMPLETED_COURSES").toString().trim().length() > 0)
				this.completedCourseCodes = ret.get("COMPLETED_COURSES")
						.toString().trim();
			if (ret.get("NOT_COMPLETED_COURSES") != null
					&& ret.get("NOT_COMPLETED_COURSES").toString().trim()
							.length() > 0)
				this.notCompletedCourseCodes = ret.get("NOT_COMPLETED_COURSES")
						.toString().trim();
			if (ret.get("REGISTERED_COURSES") != null
					&& ret.get("REGISTERED_COURSES").toString().trim().length() > 0)
				this.registeredCourseCodes = ret.get("REGISTERED_COURSES")
						.toString().trim();
			if (ret.get("NOT_REGISTERED_COURSES") != null
					&& ret.get("NOT_REGISTERED_COURSES").toString().trim()
							.length() > 0)
				this.notRegisteredCourseCodes = ret.get(
						"NOT_REGISTERED_COURSES").toString().trim();
		}
	}

	// Start: Added for Forecast Admin configuration
	public boolean insertTrainingReports(ForecastReport track, String menuId) {

		List result = DBUtil.executeSql(
				"Select TRAINING_REPORT_ID_SEQ.nextval as nextid from dual",
				AppConst.APP_DATASOURCE);
		Map map = (Map) result.get(0);

		String insertSql = "insert into  training_report (training_report_id, training_report_label,training_report_url,parent,active,allow_group, track_id) "
				+ "values (TRAINING_REPORT_ID_SEQ.nextval,?,?,?,0,null,?) ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
		try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);

			statement.setString(1, track.getTrackLabel());
			statement.setString(2,
					"TrainingReports/ForecastReport/begin.do?track="
							+ track.getTrackId());
			statement.setBigDecimal(3, new BigDecimal(menuId));
			statement.setString(4, track.getTrackId());

			log.debug("forecast Final SQL---\n" + insertSql);
			log.info(insertSql);

			int num = statement.executeUpdate();
			if (num > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error(e, e);
		} finally {
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
		return false;
	}

	public void updateTrainingReports(ForecastReport track) {
		String retString = null;
		String insertSql = "update  training_report set "
				+ "   training_report_label=? " + "   where track_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
		try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);

			statement.setString(1, track.getTrackLabel());
			statement.setString(2, track.getTrackId());

			int num = statement.executeUpdate();
			System.out.println("update:" + num);

		} catch (Exception e) {
			log.error(e, e);
		} finally {
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
	}

	public String insertTrack(String name) {

		List result = DBUtil
				.executeSql(
						"Select FORECAST_TRACK_ID_SEQ.nextval as nextforecastid from dual",
						AppConst.APP_DATASOURCE);
		Map map = (Map) result.get(0);

		String retString = null;

		String insertSql = " insert into FORECAST_TRACK "
				+ " (TRACK_ID,TRACK_LABEL,TRACK_TYPE) "
				+ " values(?,?,'Forecast') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
		String nextValue = null;

		try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);

			nextValue = "FORECAST_" + (BigDecimal) map.get("NEXTFORECASTID");
			// System.out.println("nextValue = "+nextValue);

			// statement.setBigDecimal( 1, (BigDecimal)map.get("NEXTFORECASTID")
			// );
			statement.setString(1, nextValue);
			statement.setString(2, name);

			System.out.println(statement);
			log.debug("forecast Final SQL---\n" + insertSql);

			statement.executeUpdate();

			return nextValue;
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

		return "";
	}

	public List getForecastOptionalFields(String trackID) {
		System.out.println("In getForecastOptionalFields=" + trackID);
		String sql = " select * from Forecast_optional_fields where track_id = '"
				+ trackID + "'";
		System.out.println("SQL == " + sql);
		List result = executeSql2(sql);
		return result;

	}

	public List executeSql2(String sql) {

		List result = DBUtil.executeSql(sql, AppConst.APP_DATASOURCE);
		return result;
	}

	public boolean insertForecastOptionalFields(ForecastReport track) {

		String insertSql = "insert into  Forecast_optional_fields (track_id, Gender,Manager_email,Source,Promotion_Date,Hire_Date, Employee_ID,GUID, Geography_Descr,Regional_Office_state, Products) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?) ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */

		try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);

			statement.setString(1, track.getTrackId());
			statement.setString(2, track.getGender() ? "Yes" : "No");
			statement.setString(3, track.getManagerEmail() ? "Yes" : "No");
			statement.setString(4, track.getSource() ? "Yes" : "No");
			statement.setString(5, track.getPromDate() ? "Yes" : "No");
			statement.setString(6, track.getHirDate() ? "Yes" : "No");
			statement.setString(7, track.getEmployeeId() ? "Yes" : "No");
			statement.setString(8, track.getGuId() ? "Yes" : "No");
			statement.setString(9, track.getGeographyDesc() ? "Yes" : "No");
			statement.setString(10, track.getRegionalOfficeState() ? "Yes"
					: "No");
			statement.setString(11, track.getProducts() ? "Yes" : "No");

			log.debug("forecast Final SQL---\n" + insertSql);
			log.info(insertSql);

			int num = statement.executeUpdate();
			if (num > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error(e, e);
			return false;
		} finally {
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
		return true;
	}

	public int updateForecastOptionalFields(ForecastReport track) {
		String retString = null;
		String insertSql = "update  Forecast_optional_fields set "
				+ "   Gender=? " + ",Manager_email=? " + ",Source=? "
				+ ",Promotion_Date=? " + ",Hire_Date=? " + ", Employee_ID=? "
				+ ",GUID=? " + ", Geography_Descr=? "
				+ ",Regional_Office_state=? " + ", Products=? "
				+ "   where track_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
		int num = 0;
		try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();*/
			statement = conn.prepareStatement(insertSql);

			statement.setString(1, track.getGender() ? "Yes" : "No");
			statement.setString(2, track.getManagerEmail() ? "Yes" : "No");
			statement.setString(3, track.getSource() ? "Yes" : "No");
			statement.setString(4, track.getPromDate() ? "Yes" : "No");
			statement.setString(5, track.getHirDate() ? "Yes" : "No");
			statement.setString(6, track.getEmployeeId() ? "Yes" : "No");
			statement.setString(7, track.getGuId() ? "Yes" : "No");
			statement.setString(8, track.getGeographyDesc() ? "Yes" : "No");
			statement.setString(9, track.getRegionalOfficeState() ? "Yes"
					: "No");
			statement.setString(10, track.getProducts() ? "Yes" : "No");
			statement.setString(11, track.getTrackId());

			System.out.println("track.getGender()=" + track.getGender());

			num = statement.executeUpdate();
			System.out.println("update:" + num);

		} catch (Exception e) {
			log.error(e, e);
			return 0;
		} finally {
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
		return num;
	}

	// ////////start--merge for admin config of forecast///////////
	public List getAllRoleCode() {
		List ret = new ArrayList();
		String sql = " select distinct(role_cd) from mv_field_employee_rbu where role_cd is not null ";
		List result = executeSql2(sql);
		log.info(sql);
		return result;
	}

	public List getForecastFilterData(String trackId) {
		List courses = new ArrayList();
		System.out.println("getForecastFilterData=" + trackId);
		String sql = "select FORECAST_REPORT_ID,FORECAST_REPORT_LABEL, role_cd,to_char(start_date,'mm/dd/yyyy') as start_date, "
				+ "to_char(end_date,'mm/dd/yyyy') as end_date, hire_date,promotion_date, "
				+ " duration,COMPLETED_COURSES,NOT_COMPLETED_COURSES,REGISTERED_COURSES, NOT_REGISTERED_COURSES from Forecast_filter_criteria where forecast_report_ID = '"
				+ trackId + "'";
		List result = executeSql2(sql);
		System.out.println("db results from getForecastFilterData = "
				+ result.toString());
		return result;

	}

	public int updateFilterData(HttpSession session) {

		System.out.println("in updateFilterData");
		System.out.println(" role_cd = " + session.getAttribute("ROLE_CD"));// role);
		System.out.println("in startDate ="
				+ session.getAttribute("START_DATE"));
		System.out.println("in endDate=" + session.getAttribute("END_DATE"));
		System.out.println("in hDate=" + session.getAttribute("HIRE_DATE"));
		System.out
				.println("in pDate=" + session.getAttribute("PROMOTION_DATE"));
		System.out.println("in duration=" + session.getAttribute("DURATION"));
		System.out.println("in trackId=" + session.getAttribute("trackID"));
		// System.out.println("in updateFilterData");

		String sql = "update forecast_filter_criteria set " + "role_cd=? "
				+ ",start_date=to_date(? ,'mm/dd/yyyy') "
				+ ",end_date=to_date(? ,'mm/dd/yyyy') " + ",hire_date=? "
				+ ",promotion_date=? " + ",duration=? "
				+ ",COMPLETED_COURSES=? " + ",NOT_COMPLETED_COURSES=? "
				+ ",REGISTERED_COURSES=? " + ",NOT_REGISTERED_COURSES=? "
				+ "where forecast_report_id=? ";
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
		int num = 0;
		try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();*/
			statement = conn.prepareStatement(sql);
			statement.setString(1, (String) session.getAttribute("ROLE_CD"));
			statement.setString(2, (String) session.getAttribute("START_DATE"));
			statement.setString(3, (String) session.getAttribute("END_DATE"));
			statement.setString(4, (String) session.getAttribute("HIRE_DATE"));
			statement.setString(5, (String) session
					.getAttribute("PROMOTION_DATE"));
			// statement.setString(6,session.getAttribute("DURATION"));
			statement.setString(6, "1");
			statement.setString(7, (String) session
					.getAttribute("Completed_id"));
			statement.setString(8, (String) session
					.getAttribute("NotCompleted_id"));
			statement.setString(9, (String) session
					.getAttribute("Registered_id"));
			statement.setString(10, (String) session
					.getAttribute("NotRegistered_id"));
			statement.setString(11, (String) session.getAttribute("trackID"));
			num = statement.executeUpdate();

		} catch (Exception e) {
			log.error(e, e);
			e.printStackTrace();
			return 0;
		} finally {
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
		return num;
	}

	public boolean insertFilterData(String reportLabel, String role,
			String startDate, String endDate, String hDate, String pDate,
			String duration, String trackId, String session) {

		String sql = "insert into forecast_filter_criteria "
				+ "(FORECAST_REPORT_ID,FORECAST_REPORT_LABEL,ROLE_CD,START_DATE,END_DATE,HIRE_DATE,PROMOTION_DATE,DURATION,COMPLETED_COURSES,NOT_COMPLETED_COURSES,REGISTERED_COURSES, NOT_REGISTERED_COURSES)"
				+ "values(?,?,?,to_date(? ,'mm/dd/yyyy'),to_date(? ,'mm/dd/yyyy'),?,?,?,?,?,?,?)";
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		/* Infosys - Weblogic to Jboss migration changes end here */
		System.out.println("tempStatus = " + session);

		try {
			/*Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(AppConst.APP_DATASOURCE);
			conn = ds.getConnection();*/
			statement = conn.prepareStatement(sql);
			statement.setString(1, trackId);
			statement.setString(2, reportLabel);
			statement.setString(3, role);
			statement.setString(4, startDate);
			statement.setString(5, endDate);
			statement.setString(6, hDate);
			statement.setString(7, pDate);
			statement.setString(8, duration);

			// statement.setString(9,(String)session.getAttribute("tempStatus"));
			statement.setString(9, session);
			statement.setString(10, session);
			statement.setString(11, session);
			statement.setString(12, session);
			statement.executeUpdate();

		} catch (Exception e) {
			log.error(e, e);
			e.printStackTrace();
			return false;
		} finally {
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
		System.out.println("insertFilterData updated");
		return true;
	}

	public List getCourseCompleted(String name) {
		String uName = name.toUpperCase();
		String sql = "select distinct activityname,activityfk as activityid from mv_usp_completed "
				+ "where upper(activityname) like '%" + uName + "%'";
		List result = executeSql2(sql);
		log.info(sql);
		return result;
	}

	public List getCourseRegistered(String name) {
		String uName = name.toUpperCase();
		String sql = "select distinct activityname,activity_pk as activityid from mv_usp_registered "
				+ "where upper(activityname) like '%" + uName + "%'";
		List result = executeSql2(sql);
		log.info(sql);
		return result;
	}

	public List getCourseNotCompleted(String name) {
		String uName = name.toUpperCase();
		String sql = "select distinct activityname,activityfk as activityid from mv_usp_pending "
				+ "where upper(activityname) like '%" + uName + "%'";
		List result = executeSql2(sql);
		log.info(sql);
		return result;
	}

	public List getCourseNotRegistered(String name) {
		String uName = name.toUpperCase();
		String sql = "select distinct activityname,activity_pk as activityid from mv_usp_assigned "
				+ "where upper(activityname) like '%" + uName + "%'";
		List result = executeSql2(sql);
		log.info(sql);
		return result;
	}

	// not reg is assigned
	// not comp is pending

	public HashMap getActivityIDAndDesc(String Id) {
		HashMap activityDataMap = new HashMap();
		HashMap idActivityMap = new HashMap();
		if (Id != null) {
			String idStr = Id.replaceAll("AND", ",");
			idStr = idStr.replaceAll(", OR ,", ",");
			System.out.println("idStr=" + idStr);
			String sql = "Select distinct ACTIVITYNAME, ACTIVITY_PK from mv_usp_activity_master where activity_pk in ("
					+ idStr + ")";
			System.out.println("sql=" + sql);
			List result = executeSql2(sql);

			if (result.size() != 0) {
				for (int i = 0; i < result.size(); i++) {
					activityDataMap = (HashMap) result.get(i);
					idActivityMap.put(activityDataMap.get("ACTIVITY_PK")
							.toString(), (String) activityDataMap
							.get("ACTIVITYNAME"));
					// System.out.println("activity map = "+activityDataMap.toString());
					// System.out.println("Activity Name= "+(String)activityDataMap.get("ACTIVITYNAME"));
					// System.out.println("Activity Id= "+activityDataMap.get("ACTIVITY_PK").toString());//2809387

					// String key =
					// activityDataMap.get("ACTIVITY_PK").toString();
					// System.out.println("Activity value = "+idActivityMap.get(key));
					// System.out.println("Activity value 2  = "+activityDataMap.get(activityDataMap.get("ACTIVITY_PK")));
				}
			}

			idActivityMap.put("OR", "OR");
			idActivityMap.put("AND", "AND");
			System.out.println("result of getActivityIDAndDesc= "
					+ result.toString());
		}
		return idActivityMap;
	}

	// End of forecast admin configuration.



}
