package com.pfizer.hander;

//import db.TrDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.db.Sce;
import com.pfizer.db.SceFull;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.Utils.Util;

public class SceHandler {
	protected static final Log log = LogFactory.getLog(SceHandler.class);

	public SceHandler() {
	}

	public Sce[] getSalesCallEvaluation(UserFilter uFilter) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		Sce[] ret = null;
		StringBuffer criteria = new StringBuffer();
		if (form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER) {
			criteria.append(" and ep.PRODUCT_CD = '" + uFilter.getProduct()
					+ "' ");
			if (!uFilter.isAdmin())
				criteria.append(" and fe.cluster_cd = '"
						+ uFilter.getClusterCode() + "' ");
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER) {
			criteria.append(" and ep.PRODUCT_CD = '" + uFilter.getProduct()
					+ "' ");
			criteria.append(" and fe.area_cd = '" + form.getArea() + "' ");
			if (!uFilter.isAdmin())
				criteria.append(" and fe.cluster_cd = '"
						+ uFilter.getClusterCode() + "' ");
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER) {
			criteria.append(" and ep.PRODUCT_CD = '" + uFilter.getProduct()
					+ "' ");
			criteria.append(" and fe.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and fe.REGION_CD = '" + form.getRegion() + "' ");
			if (!uFilter.isAdmin())
				criteria.append(" and fe.cluster_cd = '"
						+ uFilter.getClusterCode() + "' ");
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER) {
			criteria.append(" and ep.PRODUCT_CD = '" + uFilter.getProduct()
					+ "' ");
			criteria.append(" and fe.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and fe.REGION_CD = '" + form.getRegion() + "' ");
			criteria.append(" and fe.district_id = '" + form.getDistrict()
					+ "' ");
			if (!uFilter.isAdmin())
				criteria.append(" and fe.cluster_cd = '"
						+ uFilter.getClusterCode() + "' ");
		}
		if (!Util.isEmpty(uFilter.getEmployeeId())) {
			criteria.append(" and fe.emplid = '" + uFilter.getEmployeeId()
					+ "' ");
		}

		return getSalesCallEvaluation(criteria.toString());
	}

	private Sce[] getSalesCallEvaluation(String criteria) {
		Sce[] ret = null;
		ResultSet rs = null;
		Statement st = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext(); Timer timer = new Timer();
			 * DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * conn = ds.getConnection(); Infosys code changes ends here
			 */

			st = conn.createStatement();
			st.setFetchSize(5000);

			String sql = " select distinct "
					+ " fe.emplid as emplid, "
					+
					// " decode(sce.overall_rating,'DC','" + Sce.STATUS_DC +
					// "','NI','" + Sce.STATUS_NI + "','UN','" + Sce.STATUS_UN +
					// "','" + Sce.STATUS_NOT_COMPLETE + "' ) as rating " +
					" (select distinct score_value from sales_call.scoring_system where score_legend = sce.overall_rating) as rating "
					+ // shinoy added on Dec 2nd 2011
					" from  " + " mv_training_required ep, "
					+ " mv_field_employee_RBU fe, "
					+ " (select * from sce_fft where event_id = "
					+ AppConst.EVENTID_FFT + ") sce " + " where  "
					+ " fe.emplid = ep.emplid "
					+ " and ep.emplid = sce.emplid(+) "
					+ " and ep.product_cd = sce.product_cd(+) " + criteria;

			ArrayList test = new ArrayList();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Sce curr = new Sce();

				curr.setEmplid(rs.getString("emplid".toUpperCase()));

				curr.setRating(rs.getString("rating".toUpperCase()));

				test.add(curr);

			}
			ret = new Sce[test.size()];
			for (int j = 0; j < test.size(); j++) {
				ret[j] = (Sce) test.get(j);

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

	public SceFull[] getSalesCallEvaluationFull(String emplid, String productCd) {
		SceFull[] ret = null;
		ResultSet rs = null;
		Statement st = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext(); Timer timer = new Timer();
			 * DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * conn = ds.getConnection(); Infosys code changes ends here
			 */

			st = conn.createStatement();
			st.setFetchSize(5000);

			String sql = " select distinct "
					+ " fe.emplid as emplid, "
					+ " nvl(fe_eval.first_name,fe_eval1.FIRST_NAME) as evalFName, "
					+ " nvl(fe_eval.last_name,fe_eval1.last_name) as evalLName, "
					+ " sce.overall_comments as ocomment, "
					+ " sce.submitted_date as evalDate, "
					+
					// " decode(sce.overall_rating,'DC','" + Sce.STATUS_DC +
					// "','NI','" + Sce.STATUS_NI + "','UN','" + Sce.STATUS_UN +
					// "','" + Sce.STATUS_NOT_COMPLETE + "' ) as rating " +
					" (select distinct score_value from sales_call.scoring_system where score_legend = sce.overall_rating) as rating, "
					+ // shinoy added on Dec 2nd 2011
					" from  "
					+ " mv_training_required ep, "
					+ " v_new_field_employee fe, "
					+ " v_new_field_employee fe_eval, "
					+ " (select * from sce_fft where event_id = "
					+ AppConst.EVENTID_FFT
					+ ") sce ,"
					+
					// Added by Amit to get Evaluator Name who are not
					// v_new_field_employee but are there in SCE_USER
					"  sce_users fe_eval1  " + " where  "
					+ " fe.emplid = ep.emplid "
					+ " and ep.emplid = sce.emplid(+) "
					+ " and sce.submitted_by_emplid = fe_eval.emplid(+) "
					+ " and sce.submitted_by_emplid = fe_eval1.emplid(+) "
					+ " and ep.product_cd = sce.product_cd(+) "
					+ " and fe.emplid = '" + emplid + "' "
					+ " and ep.product_cd = '" + productCd + "' ";

			ArrayList test = new ArrayList();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				SceFull curr = new SceFull();
				curr.setEmplid(rs.getString("emplid".toUpperCase()));
				curr.setRating(rs.getString("rating".toUpperCase()));
				curr.setEvalFName(rs.getString("evalFName".toUpperCase()));
				curr.setEvalLName(rs.getString("evalLName".toUpperCase()));
				java.sql.Date sDate = rs.getDate("evalDate".toUpperCase());
				if (sDate != null) {
					curr.setEvalDate(new Date(sDate.getTime()));
				}
				curr.setComment(rs.getString("ocomment".toUpperCase()));
				test.add(curr);
			}
			ret = new SceFull[test.size()];
			for (int j = 0; j < test.size(); j++) {
				ret[j] = (SceFull) test.get(j);
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

	/*
	 * public SceFull[] getSalesCallEvaluationFull2( String emplid, String
	 * productCd, String eventId ) { SceFull[] ret = null; ResultSet rs = null;
	 * Statement st = null; Connection conn = null; try { Context ctx = new
	 * InitialContext();
	 * 
	 * Timer timer = new Timer();
	 * 
	 * DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
	 * ds.getConnection(); st = conn.createStatement(); st.setFetchSize(1000);
	 * //productCd = "ARCP"; String sql = " select distinct " +
	 * " fe.emplid as emplid, " +
	 * " nvl(fe_eval.first_name,fe_eval1.FIRST_NAME) as evalFName, " +
	 * " nvl(fe_eval.last_name,fe_eval1.last_name) as evalLName, " +
	 * " sce.event_id as eventid, " + " sce.sce_product_cd as productcd, " +
	 * " sce.overall_comments as ocomment, " +
	 * " sce.submitted_date as evalDate, " + " decode(sce.overall_rating,'DC','"
	 * + Sce.STATUS_DC + "','NI','" + Sce.STATUS_NI + "','UN','" + Sce.STATUS_UN
	 * + "','" + Sce.STATUS_NOT_COMPLETE + "' ) as rating, " +
	 * " decode(sce.status,'DRAFT','Draft','SUBMITTED','Submitted',sce.status) as status "
	 * + " from  " + " v_new_field_employee fe, " +
	 * " v_new_field_employee fe_eval, " +
	 * " (select * from V_P2L_SCE_RESULTS ) sce ," + "  sce_users fe_eval1  "+
	 * " where  " + " fe.emplid = sce.emplid " +
	 * " and sce.submitted_by_emplid = fe_eval.emplid(+) " +
	 * " and sce.submitted_by_emplid = fe_eval1.emplid(+) "+
	 * " and fe.emplid = '" + emplid + "' " + " and sce.product_cd = '" +
	 * productCd + "' ";
	 * 
	 * log.info(sql); ArrayList test = new ArrayList(); rs =
	 * st.executeQuery(sql); while (rs.next()) { SceFull curr = new SceFull();
	 * curr.setProductCd( rs.getString("productcd".toUpperCase()) );
	 * curr.setEmplid( rs.getString("emplid".toUpperCase()) ); curr.setRating(
	 * rs.getString("rating".toUpperCase()) ); curr.setEvalFName(
	 * rs.getString("evalFName".toUpperCase()) ); curr.setEvalLName(
	 * rs.getString("evalLName".toUpperCase()) );
	 * curr.setEventId(rs.getString("eventid".toUpperCase())); java.sql.Date
	 * sDate = rs.getDate("evalDate".toUpperCase()); if ( rs.getString("STATUS")
	 * != null ) { curr.setStatus(rs.getString("STATUS")); } if ( sDate != null)
	 * { curr.setEvalDate( new Date( sDate.getTime() ) ); } curr.setComment(
	 * rs.getString("ocomment".toUpperCase()) ); test.add( curr ); } ret = new
	 * SceFull[test.size()]; for ( int j=0; j < test.size(); j++ ) { ret[j] =
	 * (SceFull)test.get(j); }
	 * 
	 * } catch (Exception e) { log.error(e,e); } finally { if ( rs != null) {
	 * try { rs.close(); } catch ( Exception e2) { log.error(e2,e2); } } if ( st
	 * != null) { try { st.close(); } catch ( Exception e2) { log.error(e2,e2);
	 * } } if ( conn != null) { try { conn.close(); } catch ( Exception e2) {
	 * log.error(e2,e2); } } } return ret; }
	 */

	public SceFull[] getSalesCallEvaluationFull2(String emplid,
			String productCd, String eventId) {
		SceFull[] ret = null;
		ResultSet rs = null;
		Statement st = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext(); Timer timer = new Timer();
			 * DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * conn = ds.getConnection(); Infosys code changes ends here
			 */

			st = conn.createStatement();
			st.setFetchSize(1000);
			// productCd = "ARCP";
			String sql = " select distinct "
					+ " fe.emplid as emplid, "
					+ " nvl(fe_eval.first_name,fe_eval1.FIRST_NAME) as evalFName, "
					+ " nvl(fe_eval.last_name,fe_eval1.last_name) as evalLName, "
					+ " sce.event_id as eventid, "
					+ " sce.sce_product_cd as productcd, "
					+ " sce.overall_comments as ocomment, "
					+ " sce.submitted_date as evalDate, "
					+
					// " decode(sce.overall_rating,'DC','" + Sce.STATUS_DC +
					// "','NI','" + Sce.STATUS_NI + "','UN','" + Sce.STATUS_UN +
					// "','" + Sce.STATUS_NOT_COMPLETE + "' ) as rating, " +
					" (select distinct score_value from sales_call.scoring_system where score_legend = sce.overall_rating) as rating, "
					+ // shinoy added on Dec 2nd 2011
					" decode(sce.status,'DRAFT','Draft','SUBMITTED','Submitted',sce.status) as status "
					+ " from  " + " mv_field_employee_RBU fe, "
					+ " mv_field_employee_RBU fe_eval, "
					+ " (select * from V_P2L_SCE_RESULTS ) sce ,"
					+ "  sce_users fe_eval1  " + " where  "
					+ " fe.emplid = sce.emplid "
					+ " and sce.submitted_by_emplid = fe_eval.emplid(+) "
					+ " and sce.submitted_by_emplid = fe_eval1.emplid(+) "
					+ " and fe.emplid = '" + emplid + "' "
					+ " and sce.product_cd = '" + productCd
					+ "' order by evaldate desc";
			// System.out.println("************sql*********************"+sql);
			log.info(sql);
			ArrayList test = new ArrayList();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				SceFull curr = new SceFull();
				curr.setProductCd(rs.getString("productcd".toUpperCase()));
				curr.setEmplid(rs.getString("emplid".toUpperCase()));
				// System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%rs.getString('rating'.toUpperCase():"+rs.getString("rating".toUpperCase())+"///");
				// curr.setRating( getRatingFullForm(conn,
				// rs.getString("rating".toUpperCase()))); /////shinoy added
				curr.setRating(rs.getString("rating".toUpperCase()));
				// System.out.println("Ends %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+curr.getRating()+"///");
				curr.setEvalFName(rs.getString("evalFName".toUpperCase()));
				curr.setEvalLName(rs.getString("evalLName".toUpperCase()));
				curr.setEventId(rs.getString("eventid".toUpperCase()));
				java.sql.Date sDate = rs.getDate("evalDate".toUpperCase());
				if (rs.getString("STATUS") != null) {
					curr.setStatus(rs.getString("STATUS"));
				}
				if (sDate != null) {
					curr.setEvalDate(new Date(sDate.getTime()));
				}
				curr.setComment(rs.getString("ocomment".toUpperCase()));
				test.add(curr);
				// System.out.println("********ADDED***************************");
			}
			ret = new SceFull[test.size()];
			System.out.println("***ret after creation:" + ret);
			System.out.println("***********test.size():" + test.size());
			for (int j = 0; j < test.size(); j++) {
				ret[j] = (SceFull) test.get(j);
				System.out.println("**********ret[" + j + "]" + ret[j]);
				if (ret[j] != null) {
					SceFull curr = ret[j];
					// System.out.println("***********curr.getRating()"+curr.getRating());
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
		// System.out.println("ret:"+ret);
		return ret;
	}

	private String getRatingFullForm(Connection conn, String ratingShortForm) {
		// System.out.println("getRatingFullForm starts....:");
		// System.out.println("ratingShortForm:"+ratingShortForm+"///");
		String ratingInFullForm = ratingShortForm;
		ResultSet rs = null;
		PreparedStatement st = null;
		try {

			String sql = " select distinct score_value from sales_call.scoring_system where score_legend = '"
					+ ratingShortForm.trim() + "'";
			// System.out.println("sql:"+sql);
			st = conn.prepareCall(sql);
			rs = st.executeQuery();
			System.out.println("rs:" + rs);
			while (rs.next()) {
				System.out.println("rs.getString'score_value':"
						+ rs.getString("score_value"));
				ratingInFullForm = rs.getString("score_value");
			}

		} catch (Exception e) {
			// System.out.println("Exception Message:"+e.getMessage());
			// System.out.println("printStackTrace starts....:");
			e.printStackTrace();
			// System.out.println("printStackTrace ends....:");
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
		// System.out.println("ratingInFullForm:"+ratingInFullForm);
		// System.out.println("getRatingFullForm ends....:");
		return ratingInFullForm;
	}

	public SceFull[] getSCEFailureReport() {
		SceFull[] ret = null;
		ResultSet rs = null;
		Statement st = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext(); Timer timer = new Timer();
			 * DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * conn = ds.getConnection(); Infosys code changes ends here
			 */

			st = conn.createStatement();
			st.setFetchSize(1000);
			// productCd = "ARCP";

			String sql = " select distinct "
					+ " fe.last_name as evalLName, "
					+ " fe.first_name as evalFName, "
					+ " fe.emplid as emplid, "
					+ " fe.role_cd as role, "
					+ " fe.bu as clusterCode, "
					+ " fe.sales_group as teamCode, "
					+ " fe.email_address as email, "
					+ " sce.event_id as eventid, "
					+ " sce.product_cd as productcd, "
					+ " sce.product_name as productName, "
					+ " sce.submitted_date as evalDate, "
					+ " sce.overall_score as score "
					+ " from  "
					+ " mv_field_employee_rbu fe, "
					+ " (select allSCE.event_id, allSCE.emplid, allSCE.product_cd, allSCE.product_name, allSCE.status, allSCE.overall_score, allSCE.submitted_date "
					+ " from v_p2l_sce_results allSCE, "
					+ " (select event_id, emplid, product_cd, max(submitted_date) as submittedDate from v_p2l_sce_results "
					+ " where event_id in (5,6,7) and status = 'SUBMITTED' "
					+ " group by event_id, emplid, product_cd) latestSCE "
					+ " where " + " allSCE.event_id = latestSCE.event_id and "
					+ " allSCE.emplid = latestSCE.emplid and "
					+ " allSCE.product_cd = latestSCE.product_cd and "
					+ " allSCE.submitted_date = latestSCE.submittedDate and "
					+ " allSCE.status = 'SUBMITTED') sce " + " where  "
					+ " fe.emplid = sce.emplid and "
					+ " sce.event_id in (5,6,7) and "
					+ " sce.overall_score < 75 "
					+ " order by product_name, fe.emplid ";

			log.info(sql);
			// System.out.println("sql:" + sql);
			ArrayList test = new ArrayList();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				SceFull curr = new SceFull();
				curr.setEvalLName(rs.getString("evalLName".toUpperCase()));
				curr.setEvalFName(rs.getString("evalFName".toUpperCase()));
				curr.setEmplid(rs.getString("emplid".toUpperCase()));
				curr.setRole(rs.getString("role".toUpperCase()));
				curr.setClusterCode(rs.getString("clusterCode".toUpperCase()));
				curr.setTeamCode(rs.getString("teamCode".toUpperCase()));
				curr.setEmail(rs.getString("email".toUpperCase()));
				curr.setEventId(rs.getString("eventid".toUpperCase()));
				curr.setProductCd(rs.getString("productcd".toUpperCase()));
				curr.setProductName(rs.getString("productName".toUpperCase()));
				java.sql.Date sDate = rs.getDate("evalDate".toUpperCase());

				if (sDate != null) {
					curr.setEvalDate(new Date(sDate.getTime()));
				}
				curr.setScore(new Double(rs.getDouble("score".toUpperCase())));
				test.add(curr);
			}
			ret = new SceFull[test.size()];
			for (int j = 0; j < test.size(); j++) {
				ret[j] = (SceFull) test.get(j);
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

	public int getFeedbackTrackMapping(String trackid) {
		ResultSet rs = null;
		Statement st = null;
		int count = 0;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext(); Timer timer = new Timer();
			 * DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * conn = ds.getConnection(); Infosys code changes ends here
			 */

			st = conn.createStatement();

			String sql = " select count(*) from feedback_track_mapping"
					+ " where  " + "track_id = '" + trackid + "' ";

			log.info(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
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
		return count;

	}

	public int getFeedbackActivityMapping(String activityId) {
		ResultSet rs = null;
		Statement st = null;
		int count = 0;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext(); Timer timer = new Timer();
			 * DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * conn = ds.getConnection(); Infosys code changes ends here
			 */

			st = conn.createStatement();

			String sql = "select  count(*) from p2l_track_phase b, "
					+ " (SELECT CONNECT_BY_ROOT activity_pk as activity, level "
					+ " FROM mv_usp_activity_hierarchy WHERE activity_pk = '"
					+ activityId
					+ "'"
					+ " CONNECT BY PRIOR activity_pk = prntactfk) a "
					+ " where (a.activity = b.ROOT_ACTIVITY_ID or a.activity = b.ALT_ACTIVITY_ID) and b.TRACK_ID in "
					+ " (select track_id from feedback_track_mapping)";
			// System.out.println("getFeedbackActivityMapping sql = "+sql);
			log.info(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
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
		return count;

	}

	public static boolean isLMSMapped(String emplid, String productCd,
			String eventId, String scoreValue) {
		System.out.println("isLMSMapped starts....:");
		boolean lmsMapped = false;
		ResultSet rs = null;
		PreparedStatement st = null;
		String lms_flag = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext();
			 * //System.out.println("ctx:"+ctx); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * //System.out.println("ds:"+ds); conn = ds.getConnection();
			 * Infosys code changes ends here
			 */
			List tempList = new ArrayList();

			String sql = "select distinct template_id from sales_call.template_version where template_version_id IN (select template_version_id from sales_call.sce_fft where emplid = '"
					+ emplid
					+ "' and  product_cd = '"
					+ productCd
					+ "'and event_id = " + eventId + ")";
			// System.out.println("sql:"+sql);
			st = conn.prepareCall(sql);
			// System.out.println("st:"+st);
			rs = st.executeQuery(); // System.out.println("rs:"+rs);
			int template_id = -1;
			int current_version = -1;
			int template_version_id = -1;
			while (rs.next()) {
				System.out.println("rs.getString'template_id':"
						+ rs.getString("template_id"));
				template_id = new Integer(rs.getString("template_id"))
						.intValue();
			}
			System.out.println("template_id:" + template_id);
			sql = "select current_version from sales_call.template where template_id = "
					+ template_id;
			System.out.println("sql2:" + sql);
			st = conn.prepareCall(sql);
			rs = st.executeQuery();
			// System.out.println("rs2:"+rs);
			while (rs.next()) {
				current_version = new Integer(rs.getString("current_version"))
						.intValue();
			}
			System.out.println("current_version:" + current_version);
			sql = "select template_version_id from sales_call.template_version where version = "
					+ current_version + " and template_id = " + template_id;
			System.out.println("sql3:" + sql);
			st = conn.prepareCall(sql);
			rs = st.executeQuery();
			System.out.println("rs3:" + rs);
			while (rs.next()) {
				template_version_id = new Integer(
						rs.getString("template_version_id")).intValue();
			}
			System.out.println("template_version_id:" + template_version_id);
			sql = "select lms_flag from sales_call.evaluation_form_score where template_version_id = "
					+ template_version_id
					+ " and Score_legend = '"
					+ scoreValue + "'";
			System.out.println("sql4:" + sql);
			st = conn.prepareCall(sql);
			rs = st.executeQuery();
			// System.out.println("rs4:"+rs);
			while (rs.next()) {
				lms_flag = rs.getString("lms_flag");

			}
			System.out.println("lms_flag:" + lms_flag);
			if (lms_flag != null && lms_flag.equalsIgnoreCase("Y")) {
				lmsMapped = true;
			}
			System.out.println("lmsMapped:" + lmsMapped);
			System.out.println("isLMSMapped ends....:");
		} catch (Exception e) {
			System.out.println("Exception Message:" + e.getMessage());
			System.out.println("printStackTrace starts....:");
			e.printStackTrace();
			System.out.println("printStackTrace ends....:");
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

		return lmsMapped;
	}

	public static boolean isLMSMapped1(String emplid, String productCD,
			String eventId, String scoreValue) {
		System.out.println("isLMSMapped starts....:");
		boolean lmsMapped = false;
		ResultSet rs = null;
		PreparedStatement st = null;
		String lms_flag = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext();
			 * //System.out.println("ctx:"+ctx); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * //System.out.println("ds:"+ds); conn = ds.getConnection();
			 * //System.out.println("conn:"+conn); Infosys code changes ends
			 * here
			 */
			/*
			 * 2020 Q3: query updated from actvity name to activity code by
			 * muzees for activity name mismatch issue
			 */
			List tempList = new ArrayList();
			int template_version_id = -1;
			String sql = "";
			if (scoreValue == null || scoreValue.equals("")) {
				sql = "(SELECT max(template_version_id)from sales_call.template_version tv where template_id=(select distinct(evaluation_template_id)"
						+ "from sales_call.lms_course_mapping where activity_pk="
						+ productCD + ")and tv.Publish_flag='Y')";
			} else {
				sql = "(SELECT max(template_version_id)from sales_call.sce_fft where overall_rating='"
						+ scoreValue
						+ "' and event_id='"
						+ eventId
						+ "' and"
						+ " emplid='"
						+ emplid
						+ "' and PRODUCT_CD='"
						+ productCD + "')";
			}
			// sql="(SELECT max(template_version_id)from sales_call.template_version tv where template_id=(select distinct(evaluation_template_id)"+
			// "from sales_call.lms_course_mapping where lms_course_name='"+productName+"')and tv.Publish_flag='Y')";

			// String sql =
			// "select distinct template_id from sales_call.template_version where template_version_id IN (select template_version_id from sales_call.sce_fft where emplid = '"+emplid+"' and  product_cd = '"+productCd+"'and event_id = "+eventId+")";
			// System.out.println("sql:"+sql);
			st = conn.prepareCall(sql);
			// System.out.println("st:"+st);
			rs = st.executeQuery(); // System.out.println("rs:"+rs);
			int template_id = -1;
			int current_version = -1;
			while (rs.next()) {
				template_version_id = new Integer(
						rs.getString("max(template_version_id)")).intValue();
			}
			// while (rs.next())
			// {System.out.println("rs.getString'template_id':"+rs.getString("template_id"));
			// template_id = new
			// Integer(rs.getString("template_id")).intValue();
			// }
			// System.out.println("template_id:"+template_id);
			// sql =
			// "select current_version from sales_call.template where template_id = "+template_id;
			// System.out.println("sql2:"+sql);
			// st = conn.prepareCall(sql);
			// rs = st.executeQuery();
			// System.out.println("rs2:"+rs);
			// while (rs.next()) {
			// current_version = new
			// Integer(rs.getString("current_version")).intValue();
			// }
			// System.out.println("current_version:"+current_version);
			// sql =
			// "select template_version_id from sales_call.template_version where version = "+current_version+" and template_id = "+template_id;
			// System.out.println("sql3:"+sql);
			// st = conn.prepareCall(sql);
			// rs = st.executeQuery();
			// System.out.println("rs3:"+rs);

			System.out.println("template_version_id:" + template_version_id);
			sql = "select lms_flag from sales_call.evaluation_form_score where template_version_id = "
					+ template_version_id
					+ " and Score_legend = '"
					+ scoreValue + "'";
			System.out.println("sql4:" + sql);
			st = conn.prepareCall(sql);
			rs = st.executeQuery();
			// System.out.println("rs4:"+rs);
			while (rs.next()) {
				lms_flag = rs.getString("lms_flag");

			}
			System.out.println("lms_flag:" + lms_flag);
			if (lms_flag != null && lms_flag.equalsIgnoreCase("Y")) {
				lmsMapped = true;
			}
			System.out.println("lmsMapped:" + lmsMapped);
			System.out.println("isLMSMapped ends....:");
		} catch (Exception e) {
			System.out.println("Exception Message:" + e.getMessage());
			System.out.println("printStackTrace starts....:");
			e.printStackTrace();
			System.out.println("printStackTrace ends....:");
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

		return lmsMapped;
	}

	public static String getScoreLegend(String scoreValue) {
		String scoreLegend = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext();
			 * //System.out.println("ctx:"+ctx); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * //System.out.println("ds:"+ds); conn = ds.getConnection();
			 * //System.out.println("conn:"+conn); Infosys code changes ends
			 * here
			 */
			String sql = "select distinct score_legend from  sales_call.scoring_system where score_value = trim('"
					+ scoreValue + "')";
			// System.out.println("sql:"+sql);
			st = conn.prepareCall(sql);
			// System.out.println("st:"+st);
			rs = st.executeQuery(); // System.out.println("rs:"+rs);
			while (rs.next()) {
				System.out.println("rs.getString'template_id':"
						+ rs.getString("score_legend"));
				scoreLegend = rs.getString("score_legend");
			}
			// System.out.println("scoreLegend:"+scoreLegend);

		} catch (Exception e) {
			System.out.println("Exception Message:" + e.getMessage());
			System.out.println("printStackTrace starts....:");
			e.printStackTrace();
			System.out.println("printStackTrace ends....:");
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
		return scoreLegend;
	}

	public static String getNtId(String role, String emplId, String email) {
		ResultSet rs = null;
		PreparedStatement st = null;
		String ntId = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext(); Context ctx = new
			 * InitialContext(); //System.out.println("ctx:"+ctx); DataSource ds
			 * = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * //System.out.println("ds:"+ds); conn = ds.getConnection();
			 * //System.out.println("conn:"+conn);
			 */
			String sql = null;

			if (role != null && role.equals("employee")) {
				sql = "select distinct nt_id from mv_field_employee_rbu where emplid = '"
						+ emplId + "'";
			} else if (role != null && role.equals("user")) {
				// sql =
				// "select distinct nt_id from user_access where emplid = '"+emplId+"'";
				sql = "select distinct nt_id from user_access where  emplid = '"
						+ emplId + "' OR LOWER(email) = '" + email + "'";
				// select * from user_access where emplid = '' OR LOWER(email) =
				// 'vivian.armstrong@pfizer.com';
			}
			// System.out.println("sql:"+sql);
			st = conn.prepareCall(sql);
			// System.out.println("st:"+st);
			rs = st.executeQuery(); // System.out.println("rs:"+rs);
			while (rs.next()) {
				// System.out.println("rs.getString'NT_ID':"+rs.getString("NT_ID"));
				ntId = rs.getString("NT_ID");
			}
			// System.out.println("ntId:"+ntId);

		} catch (Exception e) {
			System.out.println("Exception Message:" + e.getMessage());
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
		return ntId;
	}

	public static boolean isCourseMapped(String productName) {
		ResultSet rs = null;
		PreparedStatement st = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			/*
			 * Context ctx = new InitialContext();
			 * //System.out.println("ctx:"+ctx); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * //System.out.println("ds:"+ds); conn = ds.getConnection();
			 * //System.out.println("conn:"+conn); Infosys code changes ends
			 * here
			 */
			String sql = null;

			sql = "select lms_course_name from sales_call.lms_course_mapping where lms_course_name='"
					+ productName + "'";
			st = conn.prepareCall(sql);
			// System.out.println("st:"+st);
			rs = st.executeQuery(); // System.out.println("rs:"+rs);

			int resultsCounter = 0;
			// rs.beforeFirst();
			// rs.last();
			// rs.afterLast();
			// int numberOfRows = rs.getRow();
			while (rs.next()) {
				resultsCounter++;
			}
			if (resultsCounter == 0) {
				return false;
			} else {
				return true;
			}
		}

		catch (Exception e) {
			System.out.println("Exception Message:" + e.getMessage());
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
		return false;
	}

	public static boolean isSCE(String productName) {
		ResultSet rs = null;
		PreparedStatement st = null;
		/*
		 * Infosys code changes starts here Connection conn = null;
		 */
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			String sql = null;
			/*
			 * Context ctx = new InitialContext();
			 * //System.out.println("ctx:"+ctx); DataSource ds =
			 * (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			 * //System.out.println("ds:"+ds); conn = ds.getConnection();
			 * //System.out.println("conn:"+conn); Infosys code changes ends
			 * here
			 */
			sql = "select lms_course_name from sales_call.lms_course_mapping where lms_course_name='"
					+ productName
					+ "'"
					+ " union select product_name from sales_call.sce_fft where product_name='"
					+ productName + "' and event_id not in (5,6,7)";
			st = conn.prepareCall(sql);
			// System.out.println("st:"+st);
			rs = st.executeQuery(); // System.out.println("rs:"+rs);

			int resultsCounter = 0;
			// rs.beforeFirst();
			// rs.last();
			// rs.afterLast();
			// int numberOfRows = rs.getRow();
			while (rs.next()) {
				resultsCounter++;
			}
			if (resultsCounter == 0) {
				return false;
			} else {
				return true;
			}
		}

		catch (Exception e) {
			System.out.println("Exception Message:" + e.getMessage());
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
		return false;
	}

	/* 2020 Q3: Start of muzees for multiple evaluation */
	public static boolean checkMultipleEvaluation(String emplid,
			String product_cd, String eventId) {
		ResultSet rs = null;
		PreparedStatement st = null;
		boolean mutltipleEvaluation = false;
		Connection conn = JdbcConnectionUtil.getJdbcConnection();
		try {
			String sql = null;
			sql = "select count(*) from TR.V_USP_SCE_REGISTERED where emplid='"
					+ emplid
					+ "' and activity_pk='"
					+ product_cd
					+ "' and REGISTRATION_DATE>(select SUBMITTED_DATE from sales_call.sce_fft where sce_id="
					+ "(select max(sce_id) from sales_call.sce_fft where emplid='"
					+ emplid + "' and product_cd='" + product_cd
					+ "' and EVENT_ID='" + eventId + "'))";
			st = conn.prepareCall(sql);
			rs = st.executeQuery();

			while (rs.next()) {
				int multipleCount = rs.getInt("count(*)");
				if (multipleCount > 0)
					mutltipleEvaluation = true;
			}
		}

		catch (Exception e) {
			System.out.println("Exception Message:" + e.getMessage());
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
		return mutltipleEvaluation;
	}// end
}
