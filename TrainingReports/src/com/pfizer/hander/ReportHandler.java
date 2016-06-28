package com.pfizer.hander; 

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.utils.JdbcConnectionUtil;

public class ReportHandler {
	protected static final Log log = LogFactory.getLog( ReportHandler.class );
	
	
	public ReportHandler() {
	}


	public List getReportOne() {
		String sql =	"select " +
						"	myaudit.user_id, " +
						"	decode(fe.first_name,null,ua.fname,fe.first_name) as first_name, " +
						"	decode(fe.last_name,null,ua.lname,fe.last_name) as last_name, " +
						"	fe.EMAIL_ADDRESS, " +
						"	decode(fe.cluster_cd,'Specialty Marke','Specialty Market',fe.cluster_cd ) as cluster_cd, " +
						" fe.TEAM_CD, fe.AREA_DESC, fe.REGION_DESC, fe.DISTRICT_DESC, FE.TERRITORY_ROLE_CD,  " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action in ('login')) as logins, " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action='piereport') as pie_report, " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action in ('listreport','listreportByEmail')) as list_report, " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action='detailreport') as detail_report, " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action in ('listreport','listreportByEmail','piereport','detailreport')) as total_hits " +

						"from " +
						"	user_access ua, " +
						"	v_new_field_employee fe, " +
						"	(select " + 
						"		distinct user_id " +
						"	from " + 
						"		tr_website_audit wa where wa.action in ('login','piereport','listreport','listreportByEmail','detailreport')" +
						"		and wa.USER_ID not in ( select user_id from user_access where type = 'SUPER ADMIN'))  myaudit  " + 
						"where " +
						"	myaudit.USER_ID = fe.EMPLID(+)" +
						"	and myaudit.user_id = ua.user_id(+) " +
						" order by fe.last_name, fe.first_name" ;
	
		log.info(sql);
		return getReport( sql );
	}

	public List getReportTwo() {

		String sql =	"select decode(filter_pie,'sce','SCE','test','Exam','attend','Attendance','Overall') as filter_pie ,filter_slice, " +
							"	sum( case when filter_product = 'ARCP' then 1 else 0 end) arcp, " +
							"	sum( case when filter_product = 'CADT' then 1 else 0 end) cadt, " +
							"	sum( case when filter_product = 'CHTX'  then 1 else 0 end) chtx, " +
							"	sum( case when filter_product = 'CLBR'  then 1 else 0 end) clbr, " +
							"	sum( case when filter_product = 'DETR'  then 1 else 0 end) detr, " +
							"	sum( case when filter_product = 'EXUB'  then 1 else 0 end) exub, " +
							"	sum( case when filter_product = 'GEOD'  then 1 else 0 end) geod, " +
							"	sum( case when filter_product = 'LPTR'  then 1 else 0 end) lptr, " +
							"	sum( case when filter_product = 'LYRC'  then 1 else 0 end) lyrc, " +
							"	sum( case when filter_product = 'REBF'  then 1 else 0 end) rebf, " +
							"	sum( case when filter_product = 'RVTO'  then 1 else 0 end) rvto, " +
							"	sum( case when filter_product = 'SPRV'  then 1 else 0 end) sprv, " +
							"	sum( case when filter_product = 'VIAG'  then 1 else 0 end) viag, " +
							"	sum( case when filter_product = 'ZYRT'  then 1 else 0 end) zyrt, " +
							"	sum( case when filter_product is not null  then 1 else 0 end) total " +
						 "from " +
							"	tr_website_audit " +
						 "where " +
							"	action in ('listreport','listreportByEmail') " +
							"	and USER_ID not in ( select user_id from user_access where type = 'SUPER ADMIN')  " + 
						 "group by filter_pie,filter_slice";
		
		return getReport( sql );
	}
	
	public List getClusterReport( String criteria ) {
		String sql =	"SELECT   FE.CLUSTER_CD,"+
						"          COUNT(FE.EMPLID) AS NUM_EMAILED,"+
						"          SUM(CASE "+
						"                WHEN EL.HAS_LOGGED_ON = 'Y' THEN 1"+
						"                ELSE 0"+
						"              END) AS NUM_LOGGED_ON,"+
						"          TO_CHAR((SUM(CASE "+
						"                         WHEN EL.HAS_LOGGED_ON = 'Y' THEN 1"+
						"                         ELSE 0"+
						"                       END) / COUNT(FE.EMPLID)) * 100,'999d99') AS PERCENT"+
						" FROM     V_NEW_FIELD_EMPLOYEE FE,"+
						"          V_EMAIL_LOGIN EL"+
						" WHERE    EL.EMAILED_USER_ID = FE.EMPLID"+
						"		   AND FE.TERRITORY_ROLE_CD in (" + criteria + ")" +
						" GROUP BY FE.CLUSTER_CD";
						log.info(sql);
		return getReport( sql );				
	}
	
	public List getEmailDetailReport(String cluster, String role, String type) {
		
		String criteria = " and fe.cluster_cd = '" + cluster + "' ";
		criteria = criteria + " and FE.TERRITORY_ROLE_CD in (" + role + ") ";
		criteria = criteria + " and HAS_LOGGED_ON = '" + type + "' ";
		
		String sql =	"select " +
						"	el.EMAILED_USER_ID as user_id, " +
						"	fe.first_name, " +
						"	fe.last_name, " +
						"	fe.EMAIL_ADDRESS, " +
						"	decode(fe.cluster_cd,'Specialty Marke','Specialty Market',fe.cluster_cd ) as cluster_cd, " +
						" fe.TEAM_CD, fe.AREA_DESC, fe.REGION_DESC, fe.DISTRICT_DESC, FE.TERRITORY_ROLE_CD,  " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action in ('login')) as logins, " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action='piereport') as pie_report, " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action in ('listreport','listreportByEmail')) as list_report, " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action='detailreport') as detail_report, " +
						"	(select count (user_id) from tr_website_audit tmp where tmp.user_id = myaudit.user_id and tmp.action in ('listreport','listreportByEmail','piereport','detailreport')) as total_hits " +
						"from " +
						"	V_EMAIL_LOGIN EL, " +
						"	v_new_field_employee fe, " +
						"	(select " + 
						"		distinct user_id " +
						"	from " + 
						"		tr_website_audit wa where wa.action in ('login','piereport','listreport','listreportByEmail','detailreport')" +
						"		and wa.USER_ID not in ( select user_id from user_access where type = 'SUPER ADMIN'))  myaudit  " + 
						"where " +
						"	el.EMAILED_USER_ID = myaudit.user_id(+) " +
						"	and el.EMAILED_USER_ID = fe.EMPLID " + criteria +
						" order by fe.last_name, fe.first_name" ;
	
		log.info(sql);
		return getReport( sql );
	}
	
	
	public List getTsrReport() {
		String sql = "SELECT   E.emplid,"+
						 "			e.last_name,"+
						 "			e.first_name,"+
						 "			decode(e.cluster_cd,'Specialty Marke','Specialty Market',e.cluster_cd ) as cluster_cd, " +
						 "			e.team_cd,"+
						 "			to_char(e.date_exempted,'Mm-DD-YYYY') as date_exempted,"+
						 "          E.PRODUCT_CD,"+
						 "          PROD.PRODUCT_DESC"+
						 " FROM     V_TSR_EXEMPTIONS E,"+
						 "          (SELECT DISTINCT PRODUCT_CD,"+
						 "                           PRODUCT_DESC"+
						 "           FROM   FFT_PRODUCT_ASSIGNMENT) PROD"+
						 " WHERE    E.PRODUCT_CD = PROD.PRODUCT_CD"+
						 " ORDER BY E.date_exempted DESC, " +
						 "			E.LAST_NAME,"+
						 "          E.FIRST_NAME,"+
						 "          E.EMPLID";
		return getReport( sql );
	}
	
	
/*	Infosys code changes starts here
 * private List getReport( String sql ) {
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		ArrayList ret = new ArrayList();
		try {
			Context ctx = new InitialContext();

			Timer timer = new Timer();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
			st = conn.createStatement();
			st.setFetchSize(5000);
			
			
			
			
			rs = st.executeQuery(sql);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int ncols = rsmd.getColumnCount();
			
			while (rs.next()) {
				HashMap record = new HashMap();	

			    for (int i=1; i<=ncols; i++) {
					String key = rsmd.getColumnName(i);
					String val;
				    val = rs.getString(i);
					record.put(key,val);
				}
						
				ret.add( record );
			}

		} catch (Exception e) {
			log.error(e,e);
		} finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}
		log.info("ret size:" + ret.size());
		return ret;	
	}
*/
	
	
	private List getReport( String sql ) {
		ArrayList ret = new ArrayList();
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		ResultSet rs = null;
		Statement stmt=null;
		
		try{
			System.out.println(sql+"  @@@@@@");
			stmt = conn.createStatement();
			stmt.setFetchSize(5000);
			rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int ncols = rsmd.getColumnCount();

            System.out.println(ncols+"Columnss");
            while (rs.next()) {
            	HashMap record = new HashMap();	

			    for (int i=1; i<=ncols; i++) {
					String key = rsmd.getColumnName(i);
					String val;
				    val = rs.getString(i);
					record.put(key,val);
				}
						
				ret.add( record );
			}
		}
            catch (Exception e) {
    			log.error(e,e);
    		} finally {
    			if ( rs != null) {
    				try {
    					rs.close();
    				} catch ( Exception e2) {
    					log.error(e2,e2);
    				}
    			}
    			if ( stmt != null) {
    				try {
    					stmt.close();
    				} catch ( Exception e2) {
    					log.error(e2,e2);
    				}
    			}
    			if ( conn != null) {
    				try {
    					conn.close();
    				} catch ( Exception e2) {
    					log.error(e2,e2);
    				}
    			}
    		}
    		log.info("ret size:" + ret.size());
    		return ret;
	}
}

// 	Infosys code changes ends here