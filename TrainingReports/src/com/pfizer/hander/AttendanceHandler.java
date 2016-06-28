package com.pfizer.hander; 

import com.pfizer.db.Attendance;
import com.pfizer.db.Employee;
import com.pfizer.db.PassFail;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;

//import db.TrDB;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AttendanceHandler {
	protected static final Log log = LogFactory.getLog( EmployeeHandler.class );
	
	
	public AttendanceHandler() {
	}


	public Attendance[] getAttendanceByEmplyeeProduct( String emplid, String productCd) {
		Attendance[] ret = null;

		String sql =	
				" select distinct " +
					" fe.emplid, " +
					" ca.start_date as startDate," +
					" DECODE(ca.course_desc,null,ep.product_cd,ca.course_desc) as courseDesc, " +
					" DECODE(ca.status,'SCHEDULED',(case when ca.COURSE_ID > 200 then 'Regional Training' else 'Transitional Training' end),'ATTENDED','Attended', decode(fe.EMPL_STATUS,'L','On Leave','P','On Leave','Absent: Training Needed')) as status  " + 
				" from  " +
					" mv_training_required ep, " +
					" v_new_field_employee fe, " +
					" v_course_attendance ca " +
				" where   " +
					" fe.emplid = ep.emplid " +
					" and ep.emplid = ca.emplid(+) " +
					" and ep.product_cd = ca.product_cd(+) " +
					" and ca.status(+) != 'ABSENT' " + 
					" and ep.product_cd  = '" + productCd + "'" +
					" and fe.emplid  = '" + emplid + "'";
					
		return getAttendance( sql );
	}
	
	public Attendance[] getAttendance( UserFilter uFilter ) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Attendance[] ret = null;
		StringBuffer criteria = new StringBuffer();
		
		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER ) {
			criteria.append(" and ep.PRODUCT_CD = '" + uFilter.getProduct() + "' ");
			if (!uFilter.isAdmin())
				criteria.append(" and fe.CLUSTER_CD = '" + uFilter.getClusterCode() + "' ");
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
			criteria.append(" and ep.PRODUCT_CD = '" + uFilter.getProduct() + "' ");
	 		criteria.append(" and fe.area_cd = '" + form.getArea() + "' ");
			if (!uFilter.isAdmin())
				criteria.append(" and fe.CLUSTER_CD = '" + uFilter.getClusterCode() + "' ");
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
			criteria.append(" and ep.PRODUCT_CD = '" + uFilter.getProduct() + "' ");
	 		criteria.append(" and fe.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and fe.REGION_CD = '" + form.getRegion() + "' ");
			if (!uFilter.isAdmin())
				criteria.append(" and fe.CLUSTER_CD = '" + uFilter.getClusterCode() + "' ");
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
			criteria.append(" and ep.PRODUCT_CD = '" + uFilter.getProduct() + "' ");
	 		criteria.append(" and fe.area_cd = '" + form.getArea() + "' ");
	 		criteria.append(" and fe.REGION_CD = '" + form.getRegion() + "' ");
	 		criteria.append(" and fe.district_id = '" + form.getDistrict() + "' ");			
			if (!uFilter.isAdmin())
				criteria.append(" and fe.CLUSTER_CD = '" + uFilter.getClusterCode() + "' ");
		} 
		 if ( !Util.isEmpty( uFilter.getEmployeeId() ) ) {
			criteria.append(" and fe.emplid = '" + uFilter.getEmployeeId() + "' ");
		 }

		String sql =	
				" select distinct " +
					" fe.emplid, " +
					" '' as startDate, " +
					" '' as courseDesc, " +
					" DECODE(ca.status,'SCHEDULED',(case when ca.COURSE_ID > 200 then 'Regional Training' else 'Transitional Training' end),'ATTENDED','Attended', decode(fe.EMPL_STATUS,'L','On Leave','P','On Leave','Absent: Training Needed')) as status  " + 
				" from  " +
					" mv_training_required ep, " +
					" v_new_field_employee fe, " +
					" v_course_attendance ca " +
				" where   " +
					" fe.emplid = ep.emplid " +
					" and ep.emplid = ca.emplid(+) " +
					" and ep.product_cd = ca.product_cd(+) " +
					" and ca.status(+) != 'ABSENT' " + criteria;
		
		return getAttendance( sql );
	}
	
	
	
	private Attendance[] getAttendance( String sql ) {
		Attendance[] ret = null;
		ResultSet rs = null;
		Statement st = null;
		/* Infosys code changes starts here
		 * Connection conn = null;*/
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*Context ctx = new InitialContext();
			Timer timer = new Timer();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			Infosys code changes ends here
			 */ 
			st = conn.createStatement();
			st.setFetchSize(5000);

			ArrayList tempList = new ArrayList();
			
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Attendance curr = new Attendance();
				curr.setEmplid( rs.getString("emplid".toUpperCase()) );
				curr.setStatus( rs.getString("status".toUpperCase()) );
				curr.setCourseDesc( rs.getString("courseDesc".toUpperCase()) );
				java.sql.Date sDate = rs.getDate("startDate".toUpperCase());
				if ( sDate != null ) {
					curr.setStartDate( new Date( sDate.getTime() ));
				}
				tempList.add( curr );
			}
			ret = new Attendance[tempList.size()];
			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (Attendance)tempList.get(j);			
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

		return ret;	
	}
	
} 
