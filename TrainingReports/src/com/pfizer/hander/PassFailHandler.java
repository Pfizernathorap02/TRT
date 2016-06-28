package com.pfizer.hander; 

import com.pfizer.db.Employee;
import com.pfizer.db.ExamModule;
import com.pfizer.db.PassFail;
import com.pfizer.db.PedagogueExam;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.Utils.Database;
import com.tgix.Utils.Timer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PassFailHandler {
	protected static final Log log = LogFactory.getLog( PassFailHandler.class );
	
	public PassFailHandler() {
	}


	public PassFail[] getPassFail( UserFilter uFilter ) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		PassFail[] ret = null;
		ArrayList test = new ArrayList();
		StringBuffer criteria = new StringBuffer();
		String[] params = new String[0];
		if ( form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER ) {
			criteria.append(" and ep.PRODUCT_CD = ? ");
			if (uFilter.isAdmin()) {
				params = new String[1];
			} else {
				criteria.append(" and fe.cluster_cd = ? ");
				params = new String[2];
				params[1] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER ) {
			criteria.append(" and ep.PRODUCT_CD = ? ");
	 		criteria.append(" and fe.area_cd = ? ");
			if (uFilter.isAdmin()) {
				params = new String[2];
			} else {
				criteria.append(" and fe.cluster_cd = ? ");
				params = new String[3];
				params[2] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
			params[1] = form.getArea();
		} else if ( form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER ) {
			criteria.append(" and ep.PRODUCT_CD = ? ");
	 		criteria.append(" and fe.area_cd = ? ");
	 		criteria.append(" and fe.REGION_CD = ? ");
			if (uFilter.isAdmin()) {
				params = new String[3];
			} else {
				criteria.append(" and fe.cluster_cd = ? ");
				params = new String[4];
				params[3] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
			params[1] = form.getArea();
			params[2] = form.getRegion();
		} else if  ( form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER ) {
			criteria.append(" and ep.PRODUCT_CD = ? ");
	 		criteria.append(" and fe.area_cd = ? ");
	 		criteria.append(" and fe.REGION_CD = ? ");
	 		criteria.append(" and fe.district_id = ? ");
			if (uFilter.isAdmin()) {
				params = new String[4];
			} else {
				criteria.append(" and fe.cluster_cd = ? ");
				params = new String[5];
				params[4] = uFilter.getClusterCode();
			}
			params[0] = uFilter.getProduct();
			params[1] = form.getArea();
			params[2] = form.getRegion();			
			params[3] = form.getDistrict();			
		} 
		
		return getPassFail( criteria.toString(),params );
	}
	
	public PedagogueExam[] getExamsByEmployeeProduct( String emplid, String productCode ) {
		PedagogueExam[] exams = null;
		StringBuffer criteria = new StringBuffer();
		
		
		criteria.append(" and ep.PRODUCT_CD = '" + productCode + "' ");
		criteria.append(" and fe.EMPLID = '" + emplid + "' ");
		
		exams = getPedagogueExam(criteria.toString());

		//trDb.getPedqgogueByEmplidProduct( productCode, emplid );
			
		return exams;
	}
	
	public Map getExamModules() {
		Map ret = new HashMap();
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
			String sql =	"Select " +
								" exam_name as examName," +
								" modules as modules," +
								" link as link " +
							 "from pedagogue_exam_modules m ";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ExamModule curr = new ExamModule();
				curr.setExamName( rs.getString("examName".toUpperCase()) );
				curr.setModules( rs.getString("modules".toUpperCase()) );
				curr.setLink( ( rs.getString("link".toUpperCase()) )  ) ;
				ret.put(curr.getExamName(), curr );
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
	
	private PassFail[] getPassFail( String criteria, String[] params ) {
		PassFail[] ret = null;

		ResultSet rs = null;
		PreparedStatement st = null;
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
			
			String sql =	
					" select  distinct " +
						" fe.EMPLID as EMPLID,   " +
						" decode (score.TEST_SCORE ,null,0,score.TEST_SCORE) as score,   " +
						" decode(pTest.EXAM_NAME,null,'Not taken', pTest.EXAM_NAME) as examName,  " +
						" decode (score.TEST_SCORE,null,'" + PassFail.CONST_TEST_NOT_TAKEN + "',greatest(score.TEST_SCORE,80),'" + PassFail.CONST_TEST_PASS + "','" + PassFail.CONST_TEST_FAIL + "') as status " +
					"from  " +
						" V_PEDAGOGUE_EXAM  pTest,  " +
						" v_new_field_employee fe,  " +
						" mv_training_required ep,  " +
						" mv_pedagogue_scores score  " +
					" where   " +
						" fe.EMPLID = ep.EMPLID  " +
						" and ep.EMPLID = pTest.EMPLID(+) " +
						" and ep.PRODUCT_CD = pTest.product_cd(+) " +
						" and score.SET_ID(+) = pTest.SET_ID 		 " +			
						" and score.EMPLID(+) = pTest.EMPLID " + criteria;

			st = conn.prepareStatement(sql);				
			st.setFetchSize(5000);
			
			for ( int i=1; i <= params.length; i++) {
				st.setString(i,params[i-1]);
			}
			
			ArrayList tempList = new ArrayList();
			
			rs = st.executeQuery();
			while (rs.next()) {
				PassFail curr = new PassFail();
				curr.setEmplid( rs.getString("EMPLID") );
				curr.setExamName( ( rs.getString("EXAMNAME") ) );
				curr.setStatus( ( rs.getString("STATUS") ) );
				curr.setScore( ( rs.getInt("SCORE") ) );
				tempList.add( curr );
			}
			ret = new PassFail[tempList.size()];
			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (PassFail)tempList.get(j);			
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

	private PedagogueExam[] getPedagogueExam( String criteria ) {
		PedagogueExam[] ret = null;

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
			
			String sql =	
					" select  distinct " +
						" fe.EMPLID as EMPLID,   " +
						" decode (score.TEST_SCORE ,null,0,score.TEST_SCORE) as score,   " +
						" decode(pTest.EXAM_NAME,null,'Not taken', pTest.EXAM_NAME) as examName,  " +
						" decode (score.TEST_SCORE,null,'" + PassFail.CONST_TEST_NOT_TAKEN + "',greatest(score.TEST_SCORE,80),'" + PassFail.CONST_TEST_PASS + "','" + PassFail.CONST_TEST_FAIL + "') as status, " +
						" score.exam_taken_date as examDate, " +
						" score.test_score as examScore, " +
						" score.set_id as setId, " +
                        " to_char(fpth.ISSUED_ON,'MM/DD/YYYY') issuedOn "+ //Added by Amit for Exam Date
					"from  " +
						" v_pedagogue_exam_detailPage  pTest,  " +
						" v_new_field_employee fe,  " +
						" mv_training_required ep,  " +
						" mv_pedagogue_scores score, " +
                        " fft_pedagogue_test_history fpth	 "+ //Added by Amit for Exam Date
					" where   " +
						" fe.EMPLID = ep.EMPLID  " +
						" and ep.EMPLID = pTest.EMPLID(+) " +
						" and ep.PRODUCT_CD = pTest.product_cd(+) " +
						" and score.SET_ID(+) = pTest.SET_ID 		 " +			
						" and score.EMPLID(+) = pTest.EMPLID " +
						" AND fpth.SET_ID(+)=ptest.SET_ID "  +  //Added by Amit for Exam Date
                        " AND pTest.EMPLID=fpth.EMPLID(+)"      //Added by Amit for Exam Date              
                        + criteria;
            
			log.info("SQL For getPedagogueExam in PassFailHandler:"+sql);				
			
			ArrayList tempList = new ArrayList();
			
			rs = st.executeQuery(sql);
			while (rs.next()) {
				PedagogueExam curr = new PedagogueExam();
				curr.setEmplid( rs.getString("EMPLID") );
                curr.setExamIssueDate(rs.getString("issuedOn"));
				curr.setExamName( ( rs.getString("EXAMNAME") ) );
				curr.setExamDate( rs.getDate( "examDate".toUpperCase() ) );
				curr.setExamScore( rs.getString("examScore".toUpperCase()) );
				curr.setSetId( rs.getString("setId".toUpperCase()) );
               
				tempList.add( curr );
			}
			ret = new PedagogueExam[tempList.size()];
			for ( int j=0; j < tempList.size(); j++ ) {
				ret[j] = (PedagogueExam)tempList.get(j);			
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
	
	public void revokeExemption(String productCode, String emplid, User user) {
		getRegionCourseId(productCode, emplid,user.getEmplid());
		//log.info("courseId:" + courseId);
	}
	
	
	private int getRegionCourseId(String productCode, String employeeId, String loggedId) {

		ResultSet rs = null;
		PreparedStatement st = null;
		/* Infosys code changes starts here
		 * Connection conn = null;*/
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		
		try {
			/*Context ctx = new InitialContext();

			Timer timer = new Timer();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
			Infosys code changes ends here */
			
			String sql =	"INSERT INTO COURSE_ASSIGNMENT CA"+
							"            (TRAINEE_SSN,"+
							"             COURSE_ID,"+
							"             GENDER,"+
							"             ROLE,"+
							"             USER_ID)"+
							" SELECT FE.NATIONAL_ID,"+
							"        (SELECT C.COURSE_ID"+
							"         FROM   COURSE C"+
							"         WHERE  COURSE_ID > 200"+
							"                AND C.PRODUCT_CD = ?) AS COURSE_ID,"+
							"        FE.SEX,"+
							"        FE.TERRITORY_ROLE_CD,"+
							"        '-2'"+
							" FROM   V_NEW_FIELD_EMPLOYEE FE"+
							" WHERE  FE.EMPLID = ?";

							            
			st = conn.prepareStatement(sql);			
			st.setString(1,productCode);
			st.setString(2,employeeId);
			int num = st.executeUpdate();
			log.info("Updated this many rows on insert:" + num);
		
			sql = "DELETE FROM DELETED_COURSE_ASSIGNMENT DCA"+
					 " WHERE       DCA.OPERATION = 'Delete'"+
					 "             AND DCA.STATUS = 'Approved'"+
					 "             AND DCA.COURSE_ID IN (SELECT COURSE_ID"+
					 "                                   FROM   COURSE"+
					 "                                   WHERE  PRODUCT_CD =? )"+
					 "             AND DCA.TRAINEE_SSN = ( select FE.NATIONAL_ID from v_new_field_employee fe where fe.emplid = ?)";
			st = conn.prepareStatement(sql);
			
			st.setString(1,productCode);
			st.setString(2,employeeId);
			num = st.executeUpdate();
			log.info("Updated this many rows on delete:" + num);
					
			sql = "INSERT INTO DELETED_COURSE_ASSIGNMENT"+
					 "            (TRAINEE_SSN,"+
					 "             COURSE_ID,"+
					 "             GENDER,"+
					 "             ROLE,"+
					 "             USER_ID,"+
					 "             STATUS,"+
					 "             OPERATION,"+
					 "             LAST_UPDATED_BY,"+
					 "             TIME_STAMP,"+
					 "             DELETE_REASON)"+
					 " SELECT MFE.NATIONAL_ID,"+
					 "        C.COURSE_ID,"+
					 "        MFE.SEX,"+
					 "        MFE.TERRITORY_ROLE_CD,"+
					 "        '-2' USER_ID,"+
					 "        'Approved' STATUS,"+
					 "        'Insert' OPERATION,"+
					 "        '"+loggedId+"' LAST_UPDATED_BY,"+
					 "        SYSDATE TIME_STAMP,"+
					 "        '' DELETE_REASON"+
					 " FROM   v_new_field_employee MFE,"+
					 "        COURSE C"+
					 " WHERE  MFE.EMPLID = ?"+
					 "        AND UPPER(C.PRODUCT_CD) = ? "+
					 "        AND C.COURSE_ID > 200";
		
			st = conn.prepareStatement(sql);
			st.setString(2,productCode);
			st.setString(1,employeeId);
			num = st.executeUpdate();
			log.info("Updated this many rows on insert:" + num);
					
			CallableStatement proc = conn.prepareCall("{ call Refresh_mv_training_required()}");		
			proc.execute();
		} catch (Exception e) {
			log.error(e,e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				log.error(e2,e2);
			}
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
		return 0;	

	}

} 
