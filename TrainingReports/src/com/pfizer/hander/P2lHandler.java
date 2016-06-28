package com.pfizer.hander;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import sun.security.krb5.internal.bd;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.db.ActivityDocsLinks;
import com.pfizer.db.Employee;
import com.pfizer.db.P2LTrainingTrackPhaseRelation;
import com.pfizer.db.P2lActivityAction;
import com.pfizer.db.P2lActivityStatus;
import com.pfizer.db.P2lEmployeeStatus;
import com.pfizer.db.P2lTrack;
import com.pfizer.db.P2lTrackPhase;
import com.pfizer.db.SubActivityBean;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.utils.ReadProperties;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.report.phasereports.CourseSearchForm;
import com.tgix.html.FormUtil;
import com.tgix.printing.LoggerHelper;
// Added for Major Enhancement 3.6 - F1
import com.tgix.Utils.Timer;
import com.tgix.Utils.Util;
// Added for Major Enhancement 3.6 - F4(employee grid user view)

// end of addition

public class P2lHandler {
	protected static final Log log = LogFactory.getLog(P2lHandler.class);

	public P2lHandler() {

	}

	public void insertPie(String name, String trackId) {
		String retString = null;
		String insertSql = "insert into p2l_track_phase "
				+ " (TRACK_PHASE_ID, PHASE_NUMBER, track_id) "
				+ " values (P2L_TRACK_PHASE_ID_SEQ.nextval,?,?) ";
		ResultSet rs = null;
		PreparedStatement statement = null;

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
			statement = conn.prepareStatement(insertSql);
			statement.setString(1, name);
			statement.setString(2, trackId);

			statement.executeUpdate();
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
	}

	public int insertTrack(String name) {
		List result = DBUtil.executeSql(
				"Select P2L_TRACK_ID_SEQ.nextval as nextid from dual",
				AppConst.APP_DATASOURCE);
		Map map = (Map) result.get(0);

		String retString = null;
		String insertSql = "insert into p2l_track "
				+ " (TRACK_ID, track_label, track_type,do_overall,do_complete ) "
				+ " values (?,?,'custom','N','N') ";
		ResultSet rs = null;
		PreparedStatement statement = null;

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
			statement = conn.prepareStatement(insertSql);
			if(map!=null)
				statement.setBigDecimal(1, (BigDecimal) map.get("NEXTID"));
			statement.setString(2, name);

			statement.executeUpdate();
			return ((BigDecimal) map.get("NEXTID")).intValue();
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

		return 0;
	}

	public List getActivityTree(String id) {

		/*
		 * Q42011 release activity drill down should contain activities whose
		 * status is active
		 */
		String sql = " select m.activityname, m.activity_pk,m.ACTIVITY_CODE,m.start_date,m.end_date, level "
				+ " from mv_usp_activity_hierarchy m  "
				+ " start with m.activity_pk = "
				+ id
				+ " and m.rel_type='Parent' and m.active = '1' "
				+ " connect by prior m.activity_pk = m.prntactfk and m.active = '1'";
		List result = executeSql2(sql);
		return result;
	}

	public List getActivityTreeByName(String name) {
		String sql = " select activityname, activity_pk,m.ACTIVITY_CODE, level "
				+ " from mv_usp_activity_hierarchy m "
				+ " start with upper(activityname) like '%"
				+ name
				+ "%' and rel_type='Parent' and activity_code is not null"
				+ " connect by prior activity_pk = prntactfk";
		List result = executeSql2(sql);
		log.info(sql);
		return result;
	}

	public List getActivityTreeByCode(String code) {
		String sql = " select activityname, activity_pk,m.ACTIVITY_CODE, level "
				+ " from mv_usp_activity_hierarchy m "
				+ " start with activity_code ='"
				+ code
				+ "' and rel_type='Parent' and activity_code is not null"
				+ " connect by prior activity_pk = prntactfk";
		List result = executeSql2(sql);
		log.info(sql);
		return result;
	}

	public void updateTrackPhase(String value, String field, String id) {
		String retString = null;
		String fieldstr = "";
		if ("ROOT_ACTIVITY_ID".equals(field)) {
			fieldstr = " ROOT_ACTIVITY_ID=? ";
		}
		if ("ALT_ACTIVITY_ID".equals(field)) {
			fieldstr = " ALT_ACTIVITY_ID=? ";
		}

		String insertSql = "update  p2l_track_phase set " + fieldstr
				+ "   where track_phase_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		log.info(insertSql + "\nid: " + id + "\nvalue:" + value);
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
			statement = conn.prepareStatement(insertSql);
			statement.setBigDecimal(1, new BigDecimal(id));
			statement.setBigDecimal(2, new BigDecimal(value));

			int num = statement.executeUpdate();
			// System.out.println("update:" + num);

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

	public void deleteTrackPhase(String id) {
		String sql = " delete from p2l_track_phase where track_phase_id = "
				+ id;
		System.out.println("update:"
				+ DBUtil.executeUpdate(sql, AppConst.APP_DATASOURCE));
	}

	public void updateTrackPhase(P2lTrackPhase phase) {
		String retString = null;
		String fieldstr = "";
		// System.out.println("Step1");
		String insertSql = "update  p2l_track_phase set "
				+ " PHASE_NUMBER = ?, " + " REPORT_APPROVAL_STATUS = ?, "
				+ " do_exempt = ?, " + " do_assigned = ?, "
				+ " sort_order = ? " + "   where track_phase_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		log.info(insertSql + "\nid: " + phase.getTrackPhaseId());
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
			statement = conn.prepareStatement(insertSql);
			statement.setString(1, phase.getPhaseNumber());
			statement.setString(2, phase.getApprovalStatus() ? "Yes" : "No");
			statement.setString(3, phase.getExempt() ? "Yes" : "No");
			statement.setString(4, phase.getAssigned() ? "Yes" : "No");
			BigDecimal db = new BigDecimal(0);
			if (!Util.isEmpty(phase.getSortorder())) {
				try {
					statement.setBigDecimal(5,
							new BigDecimal(phase.getSortorder()));
				} catch (Exception e) {
					statement.setBigDecimal(5, null);
				}
			} else {
				statement.setBigDecimal(5, null);

			}

			statement.setBigDecimal(6, new BigDecimal(phase.getTrackPhaseId()));
			// System.out.println("Step2");

			int num = statement.executeUpdate();
			// System.out.println("Step3");
			// System.out.println("update:" + num);

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

	public void updateTrack(P2lTrack track) {
		String retString = null;
		String insertSql = "update  p2l_track set " + "   track_label=?, "
				+ "   DO_COMPLETE=? " + "   where track_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
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
			statement = conn.prepareStatement(insertSql);
			statement.setString(1, track.getTrackLabel());
			statement.setString(2, track.getDoComplete() ? "Y" : "N");
			statement.setString(3, track.getTrackId());

			int num = statement.executeUpdate();
			// System.out.println("update:" + num);

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

	public boolean insertTrainingReports(P2lTrack track, String menuId) {

		String retString = null;
		String insertSql = "insert into  training_report  "
				+ "   (training_report_id, training_report_label,training_report_url,parent,active,allow_group, track_id,delete_flag) "
				+ "   values (TRAINING_REPORT_ID_SEQ.nextval,?,?,?,1,null,?,'N') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
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
			statement = conn.prepareStatement(insertSql);
			statement.setString(1, track.getTrackLabel());
			statement.setString(2, "/TrainingReports/p2l/begin?track="
					+ track.getTrackId());
			statement.setBigDecimal(3, new BigDecimal(menuId));
			statement.setString(4, track.getTrackId());

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

	public void updateTrainingReports(P2lTrack track) {
		String retString = null;
		String insertSql = "update  training_report set "
				+ "   training_report_label=? " + "   where track_id = ? ";
		ResultSet rs = null;
		PreparedStatement statement = null;
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
			statement = conn.prepareStatement(insertSql);
			statement.setString(1, track.getTrackLabel());
			statement.setString(2, track.getTrackId());

			int num = statement.executeUpdate();
			// System.out.println("update:" + num);

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

	public Map getSpecialCodes() {
		Map ret = new HashMap();

		String sql = "Select * from p2l_sce_special_codes";

		List result = executeSql2(sql);

		for (Iterator it = result.iterator(); it.hasNext();) {
			Map curr = (Map) it.next();
			//if(curr.get("SCE_EVENT_ID")!=null)
			ret.put((String) curr.get("COURSE_CODE"),((BigDecimal) curr.get("SCE_EVENT_ID")).toString());
		}
		return ret;
	}

	public List getAllCompleteTracks() {
		List ret = new ArrayList();

		String sql = "Select track_id as trackid, track_label as trackLabel from p2l_track where do_complete='Y' order by track_label";

		List result = executeSql2(sql);
		// only expect 1
		P2lTrack track = null;
		if (result != null && result.size() > 0) {

			for (Iterator it = result.iterator(); it.hasNext();) {
				boolean addTrack = false;
				Map tmp = (Map) it.next();
				track = new P2lTrack();
				FormUtil.loadObject(tmp, track, true);
				track = getTrack(track.getTrackId());
				List newPhases = new ArrayList();
				for (Iterator b = track.getPhases().iterator(); b.hasNext();) {
					P2lTrackPhase phase = (P2lTrackPhase) b.next();
					phase.setTrack(track);

					List act = getActivities(phase.getRootActivityId());

					phase.setActivities(act);
					if (act.size() > 0) {
						newPhases.add(phase);
						addTrack = true;
					}
				}
				track.setPhases(newPhases);
				if (addTrack) {
					// System.out.println("Asize:" + track.getTrackLabel());
					ret.add(track);
				}
			}

		}
		// System.out.println("Done");
		return ret;
	}

	public List getActivities(String activityId) {
		List ret = new ArrayList();
		String sql = "select distinct activityname from "
				+ " (select mah.* "
				+ " from mv_usp_activity_hierarchy mah "
				+ " start with activity_pk = "
				+ activityId
				+ " connect by prior activity_pk = prntactfk) sub1 "
				+ " where activityname like '%(Bucket)%' and ACTIVITY_CODE is not null order by activityname ";
		List result = executeSql2(sql);
		log.info(sql);
		return result;
	}

	public P2lTrack getTrack(String trackId) {
		String sql = "Select track_id as trackid, track_label as trackLabel, track_type as trackType, do_overall as doOverall, do_complete as doComplete from p2l_track where track_id='"
				+ trackId + "'";

		List result = executeSql2(sql);
		// only expect 1
		P2lTrack track = null;
		if (result != null && result.size() > 0) {
			track = new P2lTrack();
			FormUtil.loadObject((Map) result.get(0), track, true);
			sql = "select a.sort_order as sortorder, m2.code as coursecodealt, m2.activityname as activitynamealt,m.code as coursecode, m.activityname, a.track_id as trackid,a.do_prerequisite as prerequisite,a.report_approval_status as approvalstatus, a.phase_number as phasenumber, a.do_assigned as assigned, a.do_exempt as exempt,a.track_phase_id trackPhaseId , a.root_activity_id as rootactivityid, a.alt_activity_id as altactivityid "
					+ " from p2l_track_phase a, mv_usp_activity_master m,mv_usp_activity_master m2 "
					+ " where  a.alt_activity_id = m2.activity_pk(+) and a.ROOT_ACTIVITY_ID = m.activity_pk(+) and a.track_id='"
					+ trackId + "' order by sort_order, a.phase_number";
			result = executeSql2(sql);
			log.info(sql);
			if (result != null && result.size() > 0) {
				List phases = new ArrayList();
				for (Iterator it = result.iterator(); it.hasNext();) {
					P2lTrackPhase phase = new P2lTrackPhase();
					FormUtil.loadObject((Map) it.next(), phase, true);
					phase.setTrack(track);
					// log.debug(phase);
					phases.add(phase);
				}
				track.setPhases(phases);
			}
		}
		return track;
	}

	public P2lTrackPhase getTrackPhase(String activityId, String trackId) {
		
		  String sql = " select    " + " a.track_id as trackid, " +
		  " m.code as coursecode, " + " a.sort_order as sortorder, " +
		  " m.activityname, " + " m2.code as coursecodealt, " +
		  " m2.activityname as activitynamealt, " +
		  " a.do_assigned as assigned, a.do_exempt as exempt, " +
		  " a.phase_number as phasenumber, " +
		  " a.report_approval_status as approvalstatus, " +
		  " a.do_prerequisite as prerequisite, " +
		  " a.root_activity_id as rootactivityid, " +
		  " a.track_phase_id as trackPhaseId, " +
		  " a.alt_activity_id as altactivityid, " +
		  " b.track_label as trackLabel " +
		  " from p2l_track_phase a, p2l_track b, mv_usp_activity_master m,mv_usp_activity_master m2 "
		  +
		  " where a.alt_activity_id = m2.activity_pk(+) and a.ROOT_ACTIVITY_ID = m.activity_pk(+) and a.track_id = b.track_id and a.root_activity_id="
		  + activityId + " and a.track_id = '" + trackId + "'";
		 

		

		List result = executeSql2(sql);
		log.info(sql);
		P2lTrackPhase phase = null;
		if (result != null && result.size() > 0) {
			phase = new P2lTrackPhase();
			FormUtil.loadObject((Map) result.get(0), phase, true);
			System.out.println("checking" + phase.getActivityname());
		}
		return phase;
	}

	public P2lTrackPhase getTrackPhaseById(String Id) {
		String sql = " select    "
				+ " a.track_id as trackid, "
				+ " m2.code as coursecodealt, "
				+ " a.sort_order as sortorder, "
				+ " m2.activityname as activitynamealt, "
				+ " m.code as coursecode, "
				+ " m.activityname, "
				+ " a.do_assigned as assigned, a.do_exempt as exempt, "
				+ " a.phase_number as phasenumber, "
				+ " a.report_approval_status as approvalstatus, "
				+ " a.do_prerequisite as prerequisite, "
				+ " a.root_activity_id as rootactivityid, "
				+ " a.track_phase_id as trackPhaseId, "
				+ " a.alt_activity_id as altactivityid, "
				+ " b.track_label as trackLabel "
				+ " from p2l_track_phase a, p2l_track b, mv_usp_activity_master m, mv_usp_activity_master m2 "
				+ " where a.alt_activity_id = m2.activity_pk(+) and a.ROOT_ACTIVITY_ID = m.activity_pk(+) and a.track_id = b.track_id and a.track_phase_id="
				+ Id + "";

		List result = executeSql2(sql);
		log.info(sql);
		P2lTrackPhase phase = null;
		if (result != null && result.size() > 0) {
			phase = new P2lTrackPhase();
			FormUtil.loadObject((Map) result.get(0), phase, true);
		}
		return phase;
	}

	public String getActivityNameById(String activityId) {
		String sql = " select    activityname from mv_usp_activity_master where activity_pk="
				+ activityId;

		List result = executeSql2(sql);
		if (result != null && result.size() > 0) {
			Map map = (Map) result.get(0);
			return (String) map.get("ACTIVITYNAME");
		}
		return "";
	}

	public Employee[] getCompleteByNode(String node, UserFilter uFilter,
			String altNode, boolean isdetail) {
		String nodes = "";
		if (!Util.isEmpty(altNode)) {
			nodes = node + "," + altNode;
		} else {
			nodes = node;
		}
		String sql = " and e.guid IN "
				+ " (select distinct emp_no "
				+ "  from mv_usp_completed a "
				+ "  where (a.ACTIVITYFK in "
				+ "   (select activity_pk "
				+ "	from mv_usp_activity_hierarchy "
				+ "	start with activity_pk in ("
				+ nodes
				+ ")  "
				+ "	connect by prior activity_pk = prntactfk and level < 2))   and a.STATUS = 'Complete') ";

		return executeSql(sql, uFilter, isdetail);
	}

	public List getComplete(String node, UserFilter uFilter, String altNode,
			boolean isdetail) {
		List total = new ArrayList();
		String sql1 = "select emp_no, activityfk, status from mv_usp_completed c where c.ACTIVITYFK in (select m.ACTIVITY_PK from mv_usp_activity_master m where m.PRNTACTFK="
				+ node + ")";
		List nodes = executeSql2(sql1);
		if (nodes != null && nodes.size() > 0) {
			total.addAll(nodes);
		}
		sql1 = "select emp_no, activityfk, status from mv_usp_completed c where c.ACTIVITYFK ="
				+ altNode;
		nodes = executeSql2(sql1);
		if (nodes != null && nodes.size() > 0) {
			total.addAll(nodes);
		}
		return total;
	}

	public boolean checkCompleteAndRegistered(String node, String guid) {

		String sql = " select distinct emp_no "
				+ "  from V_USP_ACTIVITY_STATUS a "
				+ "  where (a.ACTIVITY_pk in "
				+ "   (select activity_pk "
				+ "	from mv_usp_activity_hierarchy "
				+ "	start with activity_pk = "
				+ node
				+ " "
				+ "	connect by prior activity_pk = prntactfk and rel_type <> 'Subscription' ))  "
				+ "  and a.emp_no = '" + guid + "'";

		List res = executeSql2(sql);
		log.info(sql);
		if (res.size() > 0)
			return true;

		sql = " select distinct emp_no " + "  from mv_usp_registered a "
				+ "  where (a.ACTIVITY_PK in " + "   (select activity_pk "
				+ "	from mv_usp_activity_hierarchy "
				+ "	start with activity_pk = " + node + " "
				+ "	connect by prior activity_pk = prntactfk ))  "
				+ "  and a.emp_no = '" + guid + "'";

		res = executeSql2(sql);
		if (res.size() > 0)
			return true;

		return false;
	}

	public Employee[] getRegistered(String node, UserFilter uFilter,
			boolean isdetail, String altNode) {
		String nodes = "";
		if (!Util.isEmpty(altNode)) {
			nodes = node + "," + altNode;
		} else {
			nodes = node;
		}
		String sql = " and e.guid IN " + " ( select distinct emp_no "
				+ "  from mv_usp_registered a "
				+ "  where a.status=0 and a.ACTIVITY_pk in "
				+ "   (select activity_pk "
				+ "	from mv_usp_activity_hierarchy "
				+ "	start with activity_pk in  (" + nodes + ")   "
				+ "	connect by prior activity_pk = prntactfk)) ";

		return executeSql(sql, uFilter, isdetail);

	}

	public Employee[] getWorldByNode(String node, UserFilter uFilter,
			String altNode, boolean isdetail, String emplid) {
		String nodes = "";
		if (!Util.isEmpty(altNode)) {
			nodes = node + "," + altNode;
		} else {
			nodes = node;
		}
		String sql = " and e.guid IN "
				+ " (select  emp_no "
				+ "           from (  select activity_pk, activityname, emp_no,'Registered' "
				+ "                       from mv_usp_registered "
				+ "   union "
				+ "                   select activity_pk, activityname, emp_no,'Assigned' "
				+ "                       from mv_usp_assigned  "
				+ "   union "
				+ "                   select ACTIVITYFK, activityname, emp_no,'Completed' "
				+ "                       from mv_usp_completed) a "
				+ "           where a.activity_pk in  "
				+ "               (select activity_pk  "
				+ "                   from mv_usp_activity_hierarchy "
				+ "                   start with activity_pk in ("
				+ nodes
				+ ") "
				+ "                   connect by prior activity_pk = prntactfk)) ";

		// List totalList = executeSql( sql );
		if (!Util.isEmpty(emplid)) {
			sql = " and e.emplid = '" + emplid + "' " + sql;
		}
		return executeSql(sql, uFilter, isdetail);
	}

	private String buildCriteria(UserFilter uFilter) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		StringBuffer criteria = new StringBuffer();
		criteria.append(" and e.emplid = e.emplid ");
		if (!"ALL".equalsIgnoreCase(form.getTeam())) {
			criteria.append(" AND e.new_team_cd='" + form.getTeam() + "' ");
		}

		if (!uFilter.isTsrOrAdmin()) {
			criteria.append(" and e.cluster_cd = '" + uFilter.getClusterCode()
					+ "' ");
		}

		if (form.getSelectionType() == TerritoryFilterForm.TYPE_ALL_FILTER) {
			// if (uFilter.isAdmin()) {
			// } else {
			// criteria.append(" and e.cluster_cd = '"+uFilter.getClusterCode()+"' ");
			// }
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_AREA_FILTER) {
			criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
			// if (uFilter.isAdmin()) {
			// } else {
			// criteria.append(" and e.cluster_cd = '"+uFilter.getClusterCode()+"' ");
			// }
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_REGION_FILTER) {
			criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			// if (uFilter.isAdmin()) {
			// } else {
			// criteria.append(" and e.cluster_cd = '"+uFilter.getClusterCode()+"' ");
			// }
		} else if (form.getSelectionType() == TerritoryFilterForm.TYPE_DISTRICT_FILTER) {
			criteria.append(" and e.area_cd = '" + form.getArea() + "' ");
			criteria.append(" and e.region_cd = '" + form.getRegion() + "' ");
			criteria.append(" and e.district_id = '" + form.getDistrict()
					+ "' ");
			// if (uFilter.isAdmin()) {
			// } else {
			// criteria.append(" and e.cluster_cd = '"+uFilter.getClusterCode()+"' ");
			// }
		}

		return criteria.toString();
	}

	// added for RBU
	/**
	 * Function overloading for generating the query condition for Report
	 * Generation
	 */
	private String buildCriteria(UserTerritory ut, UserFilter uFilter) {
		TerritoryFilterForm form = uFilter.getFilterForm();
		StringBuffer criteria = new StringBuffer();

		// Query condition to be appended for Geography Selection
		if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_ALL_SALESPOS_FILTER) {
			criteria.append(" ");
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL1_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel1()));
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL2_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel2()));
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL3_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel3()));
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL4_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel4()));
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL5_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel5()));
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL6_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel6()));
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL7_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel7()));
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL8_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel8()));
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL9_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel9()));
		} else if (form.getNewSelectionType() == TerritoryFilterForm.TYPE_LEVEL10_FILTER) {
			criteria.append(ut.getAllSalesPosition(form.getLevel10()));
		}

		// System.out.print("Sales Org selected"+form.getSalesOrg());
		// Query condition to be appended for Geography Selection
		criteria.append(ut.getAllSalesGroup(form.getSalesOrg()));
		/*
		 * if(form.getSalesorg() == null || form.getSalesOrg() == "ALL" ||
		 * form.getSalesOrg().equalsIgnoreCase("all")){ criteria.append(" "); }
		 * else { criteria.append(ut.getAllSalesGroup(form.getSalesOrg())); }
		 */
		// combined condition for Geography and Sales Organization
		return criteria.toString();
	}

	// ended for RBU
	public int getCompleteById() {
		return 0;
	}

	public int getRegisteredById() {
		return 0;
	}

	public int getAssingedById() {
		return 0;
	}

	/*
	 * public Collection getOveralStatus(P2lTrack track, UserFilter uFilter,
	 * boolean isDetail) { String nodes = track.getAllNodesDelimit();
	 * 
	 * List result = new ArrayList(); String sqlAlt =
	 * " e.last_name as lastName, " + " e.team_cd as teamCode, " +
	 * " e.cluster_cd as clusterCode, " + " e.email_address as email, " +
	 * " e.territory_role_cd as role, " + " e.district_desc as districtDesc, " +
	 * " e.preferred_name as preferredName, ";
	 * 
	 * 
	 * if ( !isDetail ) { sqlAlt = ""; } String sql =
	 * "select distinct emp_no,E.EMPLID, " + sqlAlt +
	 * " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
	 * + "from   (		select activity_pk, emp_no, status  " +
	 * "                   from V_USP_ACTIVITY_STATUS  where activity_pk in (" +
	 * nodes + ")) a, v_new_field_employee e " + " where a.emp_no = e.guid ";
	 * 
	 * //if ( onlyRms ) { // sql = sql + " and e.territory_role_cd = 'RM' "; //
	 * System.out.println("hello world"); //}
	 * 
	 * sql = sql + buildCriteria(uFilter); log.info(sql); Timer timer = new
	 * Timer(); List temp = executeSql2(sql); Map master = new HashMap();
	 * P2lEmployeeStatus pStatus; for (int i=0;i < temp.size(); i++) { Employee
	 * emp = new Employee(); Map map = (Map)temp.get(i); emp.setGuid(
	 * Util.toEmpty((String)map.get("EMP_NO")) ); emp.setEmplId(
	 * (String)map.get("EMPLID") ); emp.setEmail(
	 * Util.toEmpty((String)map.get("EMAIL")) );
	 * emp.setEmployeeStatus((String)map.get("EMPL_STATUS"));
	 * emp.setPreferredName((String)map.get("preferredName".toUpperCase()) );
	 * emp.setFirstName((String)map.get("preferredName".toUpperCase()) );
	 * emp.setLastName((String)map.get("lastName".toUpperCase()) );
	 * emp.setDistrictDesc((String)map.get("districtDesc".toUpperCase()));
	 * emp.setTeamCode((String)map.get("teamCode".toUpperCase()));
	 * emp.setClusterCode((String)map.get("clusterCode".toUpperCase()));
	 * emp.setRole((String)map.get("role".toUpperCase())); pStatus = new
	 * P2lEmployeeStatus(emp,(String)map.get("STATUS"),"Overall");
	 * pStatus.setStatus((String)map.get("STATUS"));
	 * 
	 * if ( master.get( emp.getGuid()) == null) {
	 * master.put(emp.getGuid(),pStatus); } else { P2lEmployeeStatus pStatusTemp
	 * = (P2lEmployeeStatus)master.get( emp.getGuid()); if
	 * (pStatusTemp.getStatus().equals("Complete")) {
	 * master.put(emp.getGuid(),pStatus); } if
	 * (pStatusTemp.getStatus().equals("Registered")) { continue; } if
	 * (pStatusTemp.getStatus().equals("Exempt")) { if
	 * ("Complete".equals(pStatus.getStatus()) ||
	 * "Assigned".equals(pStatus.getStatus()) ||
	 * "Registered".equals(pStatus.getStatus()) ) {
	 * pStatus.setStatus("Registered"); master.put(emp.getGuid(),pStatus); }
	 * 
	 * continue; } if (pStatusTemp.getStatus().equals("Assigned")) { if
	 * ("Complete".equals(pStatus.getStatus()) ||
	 * "Exempt".equals(pStatus.getStatus()) ||
	 * "Registered".equals(pStatus.getStatus()) ) {
	 * pStatus.setStatus("Registered"); master.put(emp.getGuid(),pStatus); }
	 * continue; }
	 * 
	 * } }
	 * 
	 * return master.values();
	 * 
	 * 
	 * }
	 */

	public Collection getOveralStatus(P2lTrack track, UserFilter uFilter,
			boolean isDetail) {
		String nodes = track.getAllNodesDelimit();

		List result = new ArrayList();
		String sqlAlt = " e.last_name as lastName, "
				+ " e.email_address as email, " + " e.role_cd as role, "
				+ " e.preferred_name as preferredName, ";

		if (!isDetail) {
			sqlAlt = "";
		}
		String sql = "select distinct emp_no,E.EMPLID, "
				+ sqlAlt
				+ " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				+ "from   (		select activity_pk, emp_no, status  "
				+ "                   from V_USP_ACTIVITY_STATUS  where activity_pk in ("
				+ nodes + ")) a, mv_field_employee_RBU e "
				+ " where a.emp_no = e.guid ";

		// if ( onlyRms ) {
		// sql = sql + " and e.territory_role_cd = 'RM' ";
		// System.out.println("hello world");
		// }

		sql = sql + buildCriteria(uFilter);
		log.info(sql);
		Timer timer = new Timer();
		List temp = executeSql2(sql);
		Map master = new HashMap();
		P2lEmployeeStatus pStatus;
		
		for (int i = 0; i < temp.size(); i++) {
			Employee emp = new Employee();
			Map map = (Map) temp.get(i);
			emp.setGuid(Util.toEmpty((String) map.get("EMP_NO")));
			emp.setEmplId((String) map.get("EMPLID"));
			emp.setEmail(Util.toEmpty((String) map.get("EMAIL")));
			emp.setEmployeeStatus((String) map.get("EMPL_STATUS"));
			emp.setPreferredName((String) map.get("preferredName".toUpperCase()));
			emp.setFirstName((String) map.get("preferredName".toUpperCase()));
			emp.setLastName((String) map.get("lastName".toUpperCase()));
			emp.setRole((String) map.get("role".toUpperCase()));
			pStatus = new P2lEmployeeStatus(emp, (String) map.get("STATUS"),
					"Overall");
			pStatus.setStatus((String) map.get("STATUS"));
			
			/* Changing 'Exempt' to Waived by Meenakshi */
			if (master.get(emp.getGuid()) == null) {
				master.put(emp.getGuid(), pStatus);
			} else {
				P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus) master
						.get(emp.getGuid());
				if (pStatusTemp.getStatus().equals("Complete")) {
					master.put(emp.getGuid(), pStatus);
				}
				if (pStatusTemp.getStatus().equals("Registered")) {
					continue;
				}
				// if (pStatusTemp.getStatus().equals("Exempt")) {
				if (pStatusTemp.getStatus().equals("Waived")) {
					if ("Complete".equals(pStatus.getStatus())
							|| "Assigned".equals(pStatus.getStatus())
							|| "Registered".equals(pStatus.getStatus())) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
					}

					continue;
				}
				if (pStatusTemp.getStatus().equals("Assigned")) {
					if ("Complete".equals(pStatus.getStatus())
							// || "Exempt".equals(pStatus.getStatus())
							|| "Waived".equals(pStatus.getStatus())
							|| "Registered".equals(pStatus.getStatus())) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
					}
					continue;
				}

			}
		}
		return master.values();
	}

	/* Method overloading for the bug fix */
	public Collection getOveralStatus(P2lTrack track, UserFilter uFilter,
			boolean isDetail, UserTerritory ut) {
		String nodes = track.getAllNodesDelimit();
		List result = new ArrayList();
		String sqlAlt = " e.last_name as lastName, "
				+ " e.email_address as email, " + " e.role_cd as role, "
				+ " e.preferred_name as preferredName, ";

		if (!isDetail) {
			sqlAlt = "";
		}
		String sql = "select distinct emp_no,E.EMPLID, "
				+ sqlAlt
				+ " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				+ "from   (		select activity_pk, emp_no, status  "
				+ "                   from V_USP_ACTIVITY_STATUS  where activity_pk in ("
				+ nodes + ")) a, mv_field_employee_RBU e "
				+ " where a.emp_no = e.guid ";

		sql = sql + buildCriteria(ut, uFilter);
		log.info(sql);
		// System.out.println("Final sql for testing\n"+sql);
		Timer timer = new Timer();
		List temp = executeSql2(sql);
		Map master = new HashMap();
		P2lEmployeeStatus pStatus;
		
		for (int i = 0; i < temp.size(); i++) {
			Employee emp = new Employee();
			Map map = (Map) temp.get(i);
			
			emp.setGuid(Util.toEmpty((String) map.get("EMP_NO")));
			emp.setEmplId((String) map.get("EMPLID"));
			emp.setEmail(Util.toEmpty((String) map.get("EMAIL")));
			emp.setEmployeeStatus((String) map.get("EMPL_STATUS"));
			emp.setPreferredName((String) map.get("preferredName".toUpperCase()));
			emp.setFirstName((String) map.get("preferredName".toUpperCase()));
			emp.setLastName((String) map.get("lastName".toUpperCase()));
			emp.setRole((String) map.get("role".toUpperCase()));
			pStatus = new P2lEmployeeStatus(emp, (String) map.get("STATUS"),
					"Overall");
			pStatus.setStatus((String) map.get("STATUS"));

			if (master.get(emp.getGuid()) == null) {
				master.put(emp.getGuid(), pStatus);
			} else {
				P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus) master
						.get(emp.getGuid());
				if (pStatusTemp.getStatus().equals("Complete")) {
					master.put(emp.getGuid(), pStatus);
				}
				if (pStatusTemp.getStatus().equals("Registered")) {
					continue;
				}
				// if (pStatusTemp.getStatus().equals("Exempt")) {
				if (pStatusTemp.getStatus().equals("Waived")) {
					if ("Complete".equals(pStatus.getStatus())
							|| "Assigned".equals(pStatus.getStatus())
							|| "Registered".equals(pStatus.getStatus())) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
					}
					continue;
				}
				if (pStatusTemp.getStatus().equals("Assigned")) {
					if ("Complete".equals(pStatus.getStatus())
							// || "Exempt".equals(pStatus.getStatus())
							|| "Waived".equals(pStatus.getStatus())
							|| "Registered".equals(pStatus.getStatus())) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
					}
					continue;
				}
			}
		}
		// System.out.println("Returing master values\n"+master.size());
		return master.values();
	}

	public Collection getPhaseStatus(P2lTrackPhase phase, UserFilter uFilter,
			boolean isDetail) {
		return getPhaseStatus(phase, uFilter, isDetail, "");
	}

	public Collection getPhaseStatus(P2lTrackPhase phase, UserFilter uFilter,
			boolean isDetail, String otherNodes) {
		ReadProperties props = new ReadProperties();
		String nodes = "";
		String completeNodes = "";
		boolean doFulltree = false;
		String emplid = uFilter.getEmployeeId();
		// System.out.println("Employee ID----------"+emplid);

		if (!Util.isEmpty(phase.getAlttActivityId())) {
			nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()
					+ otherNodes;
			completeNodes = phase.getRootActivityId() + ","
					+ phase.getAlttActivityId() + otherNodes;
		} else {
			nodes = phase.getRootActivityId() + otherNodes;
			completeNodes = phase.getRootActivityId() + otherNodes;
		}
		if (phase.getPrerequisite()) {

			List tmplist = getPrerequisite(phase.getRootActivityId(), phase);
			if (tmplist != null && tmplist.size() > 0) {
				for (Iterator itr = tmplist.iterator(); itr.hasNext();) {
					Map item = (Map) itr.next();
					BigDecimal tnode = (BigDecimal) item.get("preqactfk"
							.toUpperCase());
					// P2lTrackPhase tmpPhase = new P2lTrackPhase();
					//if(tnode!=null)
					nodes = nodes + "," + tnode.toString();
				}
			}
		}
		List result = new ArrayList();
		/*
		 * String sqlAlt = " e.last_name as lastName, " +
		 * " manager.last_name as mlastName, " +
		 * " manager.preferred_name as mpreferredName, " +
		 * " e.area_desc as areaDesc, " + " e.region_desc as regionDesc, " +
		 * " e.team_cd as teamCode, " + " e.cluster_cd as clusterCode, " +
		 * " e.email_address as email, " + " e.territory_role_cd as role, " +
		 * " e.district_desc as districtDesc, " +
		 * " e.preferred_name as preferredName, ";
		 */
		String sqlAlt = props.getValue("sqlAlt");
		String pendingSql = " select ACTIVITYFK, emp_no,'Pending' as status, completion_date as completedate  "
				+ " from mv_usp_pending  " + " union  ";

		if (!isDetail) {
			sqlAlt = "";
		}
		String sql = "select distinct emp_no,E.EMPLID, completedate, "
				+ " e.last_name as lastName,  e.last_name as mlastName, e.preferred_name as mpreferredName,e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, "
				+ " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				+ "from   (		select activity_pk, emp_no,'Registered' as status, completion_date as completedate  "
				+ "                   from mv_usp_registered  "
				+ "               union  "
				+ "               select activity_pk, emp_no,'Assigned' as status, completion_date as completedate  "
				+ "                   from mv_usp_assigned  "
				+ "               union  "
				+ "               select ACTIVITYFK, emp_no,'RegisteredC' as status, completion_date as completedate  "
				+ "                   from mv_usp_completed  "
				+ "               union  ";

		if (phase.getApprovalStatus()) {
			sql = sql + pendingSql;
		}
		String subScriptionClause = " and rel_type <> 'Subscription' ";
		if ("CPT".equals(phase.getTrack().getTrackType())) {
			subScriptionClause = " ";
		}

		/*
		 * sql = sql +
		 * "               select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
		 * +
		 * "                   from mv_usp_completed c where c.status='Exempt' and c.activityfk in ("
		 * + completeNodes +")   " + "               union  " +
		 * "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
		 * +
		 * "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
		 * + completeNodes +") ) a,  " +
		 * "        v_new_field_employee e, v_new_field_employee manager " +
		 * "where a.activity_pk in  " + "       (select activity_pk  " +
		 * "           from mv_usp_activity_hierarchy  " +
		 * "           start with activity_pk in (" + nodes + ")  " +
		 * "           connect by prior activity_pk = prntactfk " +
		 * subScriptionClause + ") " +
		 * "           and manager.emplid(+) = e.reports_to_emplid and e.GUID=a.emp_no "
		 * ;
		 */
		/*
		 * The following condition is for Admin users to see all the training
		 * records else see only those records of the employee having reports to
		 * as the logged in user's employee id
		 */
		if (uFilter.isAdmin() || uFilter.isTsrAdmin() || uFilter.isTsrOrAdmin()) {
			sql = sql
					+ "               select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate  "
					+ "                   from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ "               union  "
					+ "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
					+ "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+
					/* Added for RBU changes */
					"(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name, preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc,bu,sales_position_id_desc from mv_field_employee_rbu) e "
					+
					/* End of addition */
					" where a.activity_pk in  "
					+ "       (select activity_pk  "
					+ "           from mv_usp_activity_hierarchy  "
					+ "           start with activity_pk in (" + nodes + ")  "
					+ "           connect by prior activity_pk = prntactfk "
					+ subScriptionClause + ") "
					+ "           and a.emp_no=e.guid ";
		} else {
			sql = sql
					+ "               select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate  "
					+ "                   from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ "               union  "
					+ "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
					+ "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+
					//
					/* Added for RBU changes */
					"(SELECT (select distinct emplid "
					+ " from MV_FIELD_EMPLOYEE_RBU where emplid=x.reports_to_emplid) REPORTS_TO_EMPLID,"
					+ " emplid, GUID, empl_status, LEVEL org_level,last_name, preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc,bu,sales_position_id_desc FROM MV_FIELD_EMPLOYEE_RBU x "
					+ " CONNECT BY PRIOR emplid=x.reports_to_emplid START WITH reports_to_emplid= ("
					+ emplid + ")) e " +
					/* End of addition */
					" where a.activity_pk in  "
					+ "       (select activity_pk  "
					+ "           from mv_usp_activity_hierarchy  "
					+ "           start with activity_pk in (" + nodes + ")  "
					+ "           connect by prior activity_pk = prntactfk "
					+ subScriptionClause + ") "
					+ "           and a.emp_no=e.guid ";
		}

		// if ( onlyRms ) {
		// sql = sql + " and e.territory_role_cd = 'RM' ";
		// }

		sql = sql + buildCriteria(uFilter);
		// System.out.println("SQL---\n"+sql);
		Timer timer = new Timer();
		List temp = executeSql2(sql);
		Map master = new HashMap();
		P2lEmployeeStatus pStatus;
		boolean curriculumCompletionFlag = false;
		for (int i = 0; i < temp.size(); i++) {
			Employee emp = new Employee();
			Map map = (Map) temp.get(i);
			emp.setManagerFname(Util.toEmpty((String) map.get("mpreferredName"
					.toUpperCase())));
			emp.setManagerLname(Util.toEmpty((String) map.get("mlastName"
					.toUpperCase())));
			emp.setGuid(Util.toEmpty((String) map.get("EMP_NO")));
			emp.setEmplId((String) map.get("EMPLID"));
			emp.setEmail(Util.toEmpty((String) map.get("EMAIL")));
			emp.setEmployeeStatus((String) map.get("EMPL_STATUS"));
			emp.setPreferredName((String) map.get("preferredName".toUpperCase()));
			emp.setFirstName((String) map.get("preferredName".toUpperCase()));
			emp.setLastName((String) map.get("lastName".toUpperCase()));
			emp.setDistrictDesc((String) map.get("districtDesc".toUpperCase()));
			emp.setTeamCode((String) map.get("teamCode".toUpperCase()));
			emp.setClusterCode((String) map.get("clusterCode".toUpperCase()));
			emp.setRole((String) map.get("role".toUpperCase()));
			emp.setGeographyDesc((String) map.get("geoDesc".toUpperCase()));
			emp.setSalesOrgDesc((String) map.get("salesOrgDesc".toUpperCase()));
			emp.setSalesPostionDesc((String) map.get("salesPositionDesc"
					.toUpperCase()));
			emp.setBusinessUnit((String) map.get("bu".toUpperCase()));
			pStatus = new P2lEmployeeStatus(emp, (String) map.get("STATUS"),
					phase.getPhaseNumber());
			pStatus.setStatus((String) map.get("STATUS"));

			Object obj = map.get("completedate".toUpperCase());
			if (obj != null) {
				pStatus.setCompleteDate(new java.sql.Date(
						((java.util.Date) obj).getTime()));
				pStatus.setStatusDate(new java.sql.Date(((java.util.Date) obj)
						.getTime()));
			} else {
				pStatus.setCompleteDate(null);
			}

			if (master.get(emp.getGuid()) == null) {
				curriculumCompletionFlag = pStatus.getStatus()
						.equalsIgnoreCase("Complete")
						|| pStatus.getStatus().equalsIgnoreCase("Waived");
				// System.out.println("completion flag is "+curriculumCompletionFlag);
				if (pStatus.getStatus().equalsIgnoreCase("RegisteredC"))
					continue;
				master.put(emp.getGuid(), pStatus);
			} else {
				if (!curriculumCompletionFlag) {
					curriculumCompletionFlag = pStatus.getStatus()
							.equalsIgnoreCase("Complete")
							|| pStatus.getStatus().equalsIgnoreCase("Waived");
					// System.out.println("completion flag is "+curriculumCompletionFlag);
				}
				P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus) master
						.get(emp.getGuid());
				Date d1 = pStatusTemp.getStatusDate();
				Date d2 = pStatus.getStatusDate();
				if (pStatus.getStatus().equalsIgnoreCase("Assigned")) {
					if (!pStatusTemp.getStatus().equalsIgnoreCase("Assigned")
							|| pStatus.getStatus().equalsIgnoreCase(
									"RegisteredC"))
						continue;
					else {
						master.put(emp.getGuid(), pStatus);
					}
				} else if (pStatus.getStatus().equalsIgnoreCase("RegisteredC"))
					continue;
				else if (d1 == null || d2 == null)
					continue;
				else if (d2.after(d1) || d2.equals(d1)) {
					// else
					// if(pStatus.getStatus().equalsIgnoreCase("RegisteredC"))
					// continue;
					if (curriculumCompletionFlag) {
						if (pStatusTemp.getStatus().equalsIgnoreCase(
								"Cancelled")
								&& (pStatus.getStatus().equalsIgnoreCase(
										"Complete") || pStatus.getStatus()
										.equalsIgnoreCase("Waived"))) {
							// System.out.println("here2 for this user "+pStatus.getStatus());
							master.put(emp.getGuid(), pStatus);
						} else if ((pStatusTemp.getStatus().equalsIgnoreCase(
								"Complete") || pStatusTemp.getStatus()
								.equalsIgnoreCase("Waived"))
								&& pStatus.getStatus().equalsIgnoreCase(
										"Cancelled"))
							continue;
						else if (pStatus.getStatus().equalsIgnoreCase(
								"Registered")
								|| pStatus.getStatus().equalsIgnoreCase(
										"In Progress")) {
							master.put(emp.getGuid(), pStatus);
						} else {
							if (pStatus.getStatus().equalsIgnoreCase("No Show"))
								continue;
							master.put(emp.getGuid(), pStatus);
						}

					} else {
						master.put(emp.getGuid(), pStatus);
					}

				}

				/*
				 * if(pStatus.getStatusDate().after(pStatusTemp.getStatusDate()))
				 * { master.put(emp.getGuid(),pStatus);
				 * 
				 * }
				 * 
				 * if ( pStatusTemp.getStatus().equals("RegisteredC") ) { if (
				 * pStatus.getStatus().equals("Registered") ) {
				 * master.put(emp.getGuid(),pStatus); }else if (
				 * pStatus.getStatus().equals("Assigned") ) {
				 * pStatus.setStatus("Registered");
				 * master.put(emp.getGuid(),pStatus); }else if (
				 * pStatus.getStatus().equals("Complete") ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); } }else if (
				 * pStatusTemp.getStatus().equals("Registered") ) { //Start
				 * Added for Major Enhancement 3.6 - F1 - To include
				 * cancellation status if (
				 * pStatus.getStatus().equals("Complete") ||
				 * pStatus.getStatus().equals("Pending") ||
				 * pStatus.getStatus().equals("Cancelled") ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); } }else if (
				 * pStatusTemp.getStatus().equals("Cancelled") ) { if (
				 * pStatus.getStatus().equals("Complete") ||
				 * pStatus.getStatus().equals("Pending") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus);
				 * 
				 * } }else if ( pStatusTemp.getStatus().equals("Pending") ) { if
				 * ( pStatus.getStatus().equals("Complete") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); } }else if (
				 * pStatusTemp.getStatus().equals("Waived") ) { if
				 * (pStatus.getStatus().equals("Complete")) {
				 * master.put(emp.getGuid(),pStatus); } }else if (
				 * pStatusTemp.getStatus().equals("Assigned") ) { if (
				 * pStatus.getStatus().equals("Complete") ||
				 * pStatus.getStatus().equals("Registered") ||
				 * pStatus.getStatus().equals("Pending") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); }else if (
				 * pStatus.getStatus().equals("RegisteredC") ) {
				 * pStatus.setStatus("Registered");
				 * master.put(emp.getGuid(),pStatus); } }
				 */
			}
		}

		return master.values();
	}

	// added for RBU
	/**
	 * Function overloading for generating the report with Geographical
	 * parameters
	 */

	public Collection getPhaseStatus(UserTerritory ut, P2lTrackPhase phase,
			UserFilter uFilter, boolean isDetail) {
		return getPhaseStatus(ut, phase, uFilter, isDetail, "");
	}

	// Added for TRT Enhancement 3.6 (employee grid user view)
	public List getSelOptionalEmplFields() {
		// List result = new ArrayList();
		// System.out.println("inside getSelOptionalEmplFields");

		String sql = "SELECT SELECTED_FIELDS FROM EMPLOYEE_GRID_CONFIG";
		List selOptFields = executeSql2(sql);
		ArrayList fieldslist = new ArrayList();
		Iterator it = selOptFields.iterator();

		for (int i = 0; it.hasNext();) {
			Map selFields = (Map) it.next();
			String str = (String) selFields.get("SELECTED_FIELDS");
			if (str != null) {
				String[] arr = str.split(",");

				for (int j = 0; j < arr.length; j++) {
					fieldslist.add(arr[j]);
					// System.out.println("FieldList == "+fieldslist.get(j));
				}
			}
		}
		return fieldslist;

	}

	// End of addition
	public Collection getPhaseStatus(UserTerritory ut, P2lTrackPhase phase,
			UserFilter uFilter, boolean isDetail, String otherNodes) {
		ReadProperties props = new ReadProperties();
		String nodes = "";
		String completeNodes = "";

		boolean doFulltree = false;
		String emplid = uFilter.getEmployeeId();
		// System.out.println("Employee ID***----------"+emplid);
		// System.out.println("ISADMIN----------"+uFilter.isAdmin());
		// System.out.println("ISTSRADMIN----------"+uFilter.isTsrAdmin());
		// System.out.println("ISTSRORADMIN----------"+uFilter.isTsrOrAdmin());

		String salesquery = "select SALES_POSITION_ID from mv_field_employee_rbu where emplid = "
				+ emplid;
		if (uFilter.isSpecialRoleUser()) {
			salesquery = "SELECT sales_position_id FROM MV_FIELD_EMPLOYEE_RBU WHERE emplid = "
					+ uFilter.getEmployeeIdForSplRole();
			// System.out.println("spl role id is "+uFilter.getEmployeeIdForSplRole());
		}

		// System.out.println(salesquery);
		List squery = new ArrayList();
		squery = executeSql2(salesquery);
		// System.out.println("check one ok");

		HashMap sh = new HashMap();
		String salesPid = null;
		if (squery.size() != 0) {
			// Iterator it = squery.iterator();
			// if(squery.get(0)!=null){
			sh = (HashMap) squery.get(0);
			salesPid = (String) sh.get("SALES_POSITION_ID");
			// System.out.println(" you r here "+salesPid);

		}

		if (!Util.isEmpty(phase.getAlttActivityId())) {
			nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()
					+ otherNodes;
			completeNodes = phase.getRootActivityId() + ","
					+ phase.getAlttActivityId() + otherNodes;
		} else {
			nodes = phase.getRootActivityId() + otherNodes;
			completeNodes = phase.getRootActivityId() + otherNodes;
		}
		if (phase.getPrerequisite()) {

			List tmplist = getPrerequisite(phase.getRootActivityId(), phase);
			if (tmplist != null && tmplist.size() > 0) {
				for (Iterator itr = tmplist.iterator(); itr.hasNext();) {
					Map item = (Map) itr.next();
					BigDecimal tnode = (BigDecimal) item.get("preqactfk"
							.toUpperCase());
					// P2lTrackPhase tmpPhase = new P2lTrackPhase();
					//if(tnode!=null)
					nodes = nodes + "," + tnode.toString();
				}
			}
		}
		List result = new ArrayList();
		/* Modified for RBU changes */
		String sqlAlt = props.getValue("sqlAlt");
		// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
		// employee grid)

		// String pendingSql =
		// " select ACTIVITYFK, emp_no,'Pending' as status, null as completedate  "
		// +
		String pendingSql = " select ACTIVITYFK, emp_no,'Pending' as status, lstupd as completedate, 'NA' as score  "
				+ " from mv_usp_pending  " + " union  ";

		String sqlAllOptionalFields = " e.sex as gender, e.promotion_date as promoDate, f.email_address as memailaddress, e.sales_position_type_cd as source, e.hire_date as hireDate, e.state as state,e.home_state as home_state, e.source_org as SOURCE_ORG,e.sales_position_id as SALES_POSITION_ID, ";
		// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee
		// grid)

		// Start: added for HQ user requirement for TRT Phase 2 enhancement.
		String hqOptionalFields = "";
		// if (uFilter.isHqUser()) {
		hqOptionalFields = " ,field_active,is_hq_user,e.group_cd, reports_to_rel_1 ";
		// }
		if (!isDetail) {
			sqlAlt = "";
		}
		// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
		// employee grid)
		String sql = "select distinct emp_no,score,E.EMPLID, completedate, "
				+ sqlAllOptionalFields
				+ "  e.last_name as lastName, e.first_name as firstName, f.last_name as mlastName, f.preferred_name as mpreferredName, e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, e.nt_id as NT_ID , completedate as statusdate, score, "
				+ " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				+
				// Start: added for HQ user requirement for TRT Phase 2
				// enhancement.
				hqOptionalFields
				+
				// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
				// employee grid)

				/*
				 * Start: Commenting for TRT 3.6 enhancement - F 4.5 -(user view
				 * of employee grid) String sql =
				 * "select distinct emp_no,E.EMPLID, completedate, e.last_name as lastName, e.first_name as firstName, "
				 * +
				 * "f.last_name as mlastName, f.preferred_name as mpreferredName,e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, "
				 * +
				 * "e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				 * + End of commenting
				 */

				/*
				 * Commenting out for CSO enhancement
				 * "from (select activity_pk, emp_no,'Registered' as status, null as completedate  "
				 * + "                   from mv_usp_registered  " +
				 * "               union  " +
				 * "               select activity_pk, emp_no,'Assigned' as status, null as completedate  "
				 * + "                   from mv_usp_assigned  " +
				 * "               union  " +
				 * "               select ACTIVITYFK, emp_no,'RegisteredC' as status, null as completedate  "
				 * + "                   from mv_usp_completed  " +
				 * "               union  "; End of commenting
				 */
				/*
				 * Log: Modified by Meenakshi.M.B on 14-May-2010 Changing the
				 * condition for CSO requirements. Introducing the new MV to
				 * improve performance of the query
				 */
				" from (select activity_pk, emp_no,status, completedate,score from mv_activity_status"
				+ " union ";

		String orderByClause = "order by e.last_name";

		if (phase.getApprovalStatus()) {
			sql = sql + pendingSql;
		}
		String subScriptionClause = " and rel_type <> 'Subscription' ";
		if ("CPT".equals(phase.getTrack().getTrackType())) {
			subScriptionClause = " ";
		}
		/*
		 * The following condition is for Admin users to see all the training
		 * records else see only those records of the employee having reports to
		 * as the logged in user's employee id
		 */
		// Start: added for HQ user requirement for TRT Phase 2 enhancement.
		// System.out.println("uFilter.isHqUser() = "+uFilter.isHqUser());
		if (uFilter.isAdmin() || uFilter.isTsrAdmin() || uFilter.isTsrOrAdmin()
				|| uFilter.isHqUser())
		// if(uFilter.isAdmin() || uFilter.isTsrAdmin() ||
		// uFilter.isTsrOrAdmin())
		{
			// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			// sql = sql +
			// "               select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			// +
			sql = sql
					+ "               select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score"
					+ "                   from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ "               union  "
					+
					// "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
					// +
					"               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score"
					+ "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+
					/* Added for RBU changes */
					// "(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "+
					// Start: added for HQ user requirement for TRT Phase 2
					// enhancement.
					"(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, nt_id, sex, state,home_state, hire_date, promotion_date, sales_position_id_desc,sales_position_type_cd,field_active,is_hq_user, reports_to_rel_1,source_org,sales_position_id from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f "
					+
					// "(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, nt_id, sex, state, hire_date, promotion_date, sales_position_id_desc,sales_position_type_cd from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f "+
					/* End of addition */
					" where a.activity_pk in  "
					+ "       (select activity_pk  "
					+ "           from mv_usp_activity_hierarchy  "
					+ "           start with activity_pk in ("
					+ nodes
					+ ")  "
					+ "           connect by prior activity_pk = prntactfk "
					+ subScriptionClause
					+ ") "
					+ "           and a.emp_no=e.guid and e.reports_to_emplid = f.emplid(+) ";
			// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
		} else {
			/* Commenting out for CSO requirement changes */
			/*
			 * sql = sql +
			 * "               select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			 * +
			 * "                   from mv_usp_completed c where c.status='Exempt' and c.activityfk in ("
			 * + completeNodes +")   " + "               union  " +
			 * "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
			 * +
			 * "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
			 * + completeNodes +") ) a,  " + // /* Added for RBU changes
			 * "(SELECT (select distinct emplid "+
			 * " from MV_FIELD_EMPLOYEE_RBU where emplid=x.reports_to_emplid) REPORTS_TO_EMPLID,"
			 * +
			 * " emplid, GUID, empl_status, LEVEL org_level,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc FROM MV_FIELD_EMPLOYEE_RBU x "
			 * +
			 * " CONNECT BY PRIOR emplid=x.reports_to_emplid START WITH reports_to_emplid= ("
			 * + emplid+
			 * ")) e, (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "
			 * + /* End of addition " where a.activity_pk in  " +
			 * "       (select activity_pk  " +
			 * "           from mv_usp_activity_hierarchy  " +
			 * "           start with activity_pk in (" + nodes + ")  " +
			 * "           connect by prior activity_pk = prntactfk " +
			 * subScriptionClause + ") " +
			 * "           and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) "
			 * ;
			 */
			/* End of comment */
			/*
			 * Modified Log by Meenakshi.M.B: Changing the query for CSO
			 * enhancement
			 */
			// sql = sql +
			// " select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			// +
			sql = sql
					+ " select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score  "
					+ " from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ " union  "
					+ " select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score  "
					+ " from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+ " (select x.reports_to_emplid,x.emplid,guid,x.empl_status,y.org_level, x.last_name,x.first_name,x.preferred_name,x.sales_group,group_cd, "
					+ " x.email_address,x.role_cd,x.geo_desc,x.bu,x.sales_position_id_desc,x.nt_id, x.sex, x.state, x.home_state, x.hire_date, x.promotion_date,x.sales_position_type_cd , "
					+
					// Added extra fields for changes due to HQ users
					// requirement of TRT Phase 2 enhancement
					" field_active,is_hq_user, reports_to_rel_1,x.source_org,x.sales_position_id"
					+ " from mv_field_employee_rbu x, "
					+ " (select distinct sales_position_id,emplid,LEVEL as org_level from MV_FIELD_EMPLOYEE_RELATION connect by prior "
					+
					// " emplid=related_emplid start with related_emplid= ("+
					// emplid +
					// ")) y where x.emplid=y.emplid and x.sales_position_id=y.sales_position_id) e, "+
					" sales_position_id=related_sales_position_id start with sales_position_id= ("
					+ salesPid
					+ ")) y where x.emplid=y.emplid and x.sales_position_id = y.sales_position_id ) e, "
					+ " (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f where a.activity_pk in (select activity_pk "
					+ " from mv_usp_activity_hierarchy  "
					+ " start with activity_pk in ("
					+ nodes
					+ ")  "
					+ " connect by prior activity_pk = prntactfk "
					+ subScriptionClause
					+ ") "
					+ " and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
		}
		LoggerHelper
				.logSystemDebug("uFilter.isRefresh()" + uFilter.isRefresh());

		// System.out.println("\n uFilter.isRefresh()"+uFilter.isRefresh());
		// added for RBU
		if (uFilter.isRefresh() == true) {
			sql = sql + buildCriteria(ut, uFilter) + orderByClause;
		} else {
			sql = sql + orderByClause;
		}
		// ended for RBU
		// LoggerHelper.logSystemDebug("Final SQL---\n"+sql);
		LoggerHelper.logSystemDebug("phase chart sql is " + sql);
		// System.out.println("phase chart sql is"+sql);
		Timer timer = new Timer();
		List temp = executeSql2(sql);
		// Map master = new HashMap();
		/* Modified to fix the default sorting of Last name */
		Map master = new LinkedHashMap();
		P2lEmployeeStatus pStatus;
		boolean curriculumCompletionFlag = false;
		List sortedList = new ArrayList();
		Employee emp = null;
		for (int i = 0; i < temp.size(); i++) {
			emp = new Employee();
			String tempfname = "";
			Map map = (Map) temp.get(i);
			emp.setManagerFname(Util.toEmpty((String) map.get("mpreferredName"
					.toUpperCase())));
			emp.setManagerLname(Util.toEmpty((String) map.get("mlastName"
					.toUpperCase())));
			// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			emp.setManagerEmail(Util.toEmpty((String) map.get("MEMAILADDRESS"))); // for
																					// email
																					// of
																					// manager
			// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			emp.setRegionDesc(Util.toEmpty((String) map.get("regionDesc"
					.toUpperCase())));
			emp.setAreaDesc(Util.toEmpty((String) map.get("areaDesc"
					.toUpperCase())));
			emp.setGuid(Util.toEmpty((String) map.get("EMP_NO")));
			emp.setEmplId((String) map.get("EMPLID"));
			emp.setEmail(Util.toEmpty((String) map.get("EMAIL")));
			emp.setEmployeeStatus((String) map.get("EMPL_STATUS"));
			emp.setPreferredName((String) map.get("preferredName".toUpperCase()));
			emp.setFirstName((String) map.get("preferredName".toUpperCase()));
			emp.setSource((String) map.get("SOURCE"));
			emp.setSourceOrg((String) map.get("SOURCE_ORG"));
			emp.setSalesPositionId((String) map.get("SALES_POSITION_ID"));
			emp.setHomeState((String) map.get("HOME_STATE"));
			// System.out.println("+++++++++++emp.getSource()="+emp.getSource());
			/* Code added for bug fix for first name display of MDM users */
			tempfname = emp.getFirstName();
			if (tempfname == null || tempfname.equals("")) {
				emp.setFirstName((String) map.get("firstName".toUpperCase()));
			}
			/* End of addition */
			// Added for TRT Phase 2 - HQ Users

			if (("").equals(emp.getSource()) || emp.getSource() == null) { // Source
																			// is
																			// null
																			// for
																			// HQ
																			// users
				emp.setFirstName((String) map.get("firstName".toUpperCase()));
			}
			// End
			emp.setLastName((String) map.get("lastName".toUpperCase()));
			emp.setDistrictDesc((String) map.get("districtDesc".toUpperCase()));
			emp.setTeamCode((String) map.get("teamCode".toUpperCase()));
			emp.setClusterCode((String) map.get("clusterCode".toUpperCase()));
			emp.setRole((String) map.get("role".toUpperCase()));
			emp.setGeographyDesc((String) map.get("geoDesc".toUpperCase()));
			emp.setBusinessUnit((String) map.get("bu".toUpperCase()));
			emp.setSalesOrgDesc((String) map.get("salesOrgDesc".toUpperCase()));
			emp.setSalesPostionDesc((String) map.get("salesPositionDesc"
					.toUpperCase()));

			// Start: Modified for TRT 3.6 enhancement - F 4.5 ( Display of
			// employee grid)

			emp.setNtId((String) map.get("NT_ID"));
			emp.setState((String) map.get("STATE"));
			emp.setPromoDate((Date) map.get("PROMODATE"));
			emp.setGender((String) map.get("GENDER"));
			emp.setHireDate((Date) map.get("HIREDATE"));
			emp.setHomeState((String) map.get("HOME_STATE"));

			pStatus = new P2lEmployeeStatus(emp, (String) map.get("STATUS"),
					phase.getPhaseNumber());
			pStatus.setStatus((String) map.get("STATUS"));
			pStatus.setScore((String) map.get("SCORE"));
			Object obj = map.get("completedate".toUpperCase());
			if (obj != null) {
				pStatus.setStatusDate(new java.sql.Date(((java.util.Date) map
						.get("completedate".toUpperCase())).getTime()));
			}
			// pStatus.setStatusDate(new
			// java.sql.Date(((java.util.Date)map.get("completedate".toUpperCase())).getTime()));
			String score=null;
			//if(map.get("SCORE")!=null){
			 score= map.get("SCORE").toString();
			if (score.equals("NA")) {

				pStatus.setScore("");
			} else {
				pStatus.setScore(map.get("SCORE").toString());

			}
			//}
			// End: Modified for TRT 3.6 enhancement - F 4.5( Display of
			// employee grid)
			// Start : Added for Phase 2 HQ Users requirement.
			emp.setGroupCD((String) map.get("GROUP_CD"));
			emp.setHQManager(Util.toEmpty((String) map.get("reports_to_rel_1"
					.toUpperCase())));
			// End

			Object obj1 = map.get("completedate".toUpperCase());
			if (obj1 != null) {
				// pStatus.setCompleteDate(new
				// java.sql.Date(((java.sql.Timestamp)obj).getTime()));
				pStatus.setCompleteDate(new java.sql.Date(
						((java.util.Date) obj1).getTime()));
			} else {
				pStatus.setCompleteDate(null);
			}
			if (master.get(emp.getGuid()) == null) {
				curriculumCompletionFlag = pStatus.getStatus()
						.equalsIgnoreCase("Complete")
						|| pStatus.getStatus().equalsIgnoreCase("Waived");
				// System.out.println("completion flag is "+curriculumCompletionFlag);
				if (pStatus.getStatus().equalsIgnoreCase("RegisteredC"))
					continue;
				master.put(emp.getGuid(), pStatus);
				// sortedList.add(pStatus);
			} else {

				if (!curriculumCompletionFlag) {
					curriculumCompletionFlag = pStatus.getStatus()
							.equalsIgnoreCase("Complete")
							|| pStatus.getStatus().equalsIgnoreCase("Waived");
					// System.out.println("completion flag is "+curriculumCompletionFlag);
				}

				P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus) master
						.get(emp.getGuid());
				Date d1 = pStatusTemp.getStatusDate();
				Date d2 = pStatus.getStatusDate();

				if (pStatus.getStatus().equalsIgnoreCase("Assigned")) {
					if (!pStatusTemp.getStatus().equalsIgnoreCase("Assigned")
							|| pStatus.getStatus().equalsIgnoreCase(
									"RegisteredC"))
						continue;
					else {
						master.put(emp.getGuid(), pStatus);
					}
				} else if (pStatus.getStatus().equalsIgnoreCase("RegisteredC"))
					continue;
				else if (d1 == null || d2 == null)
					continue;
				else if (d2.after(d1) || d2.equals(d1)) {
					// else
					// if(pStatus.getStatus().equalsIgnoreCase("RegisteredC"))
					// continue;

					if (curriculumCompletionFlag) {
						// System.out.println("curriculum completion flag is "+curriculumCompletionFlag);
						if (pStatusTemp.getStatus().equalsIgnoreCase(
								"Cancelled")
								&& (pStatus.getStatus().equalsIgnoreCase(
										"Complete") || pStatus.getStatus()
										.equalsIgnoreCase("Waived"))) {
							master.put(emp.getGuid(), pStatus);
						} else if ((pStatusTemp.getStatus().equalsIgnoreCase(
								"Complete") || pStatusTemp.getStatus()
								.equalsIgnoreCase("Waived"))
								&& pStatus.getStatus().equalsIgnoreCase(
										"Cancelled"))
							continue;
						else if (pStatus.getStatus().equalsIgnoreCase(
								"Registered")
								|| pStatus.getStatus().equalsIgnoreCase(
										"In Progress")) {
							master.put(emp.getGuid(), pStatus);
						} else {
							if (pStatus.getStatus().equalsIgnoreCase("No Show"))
								continue;
							master.put(emp.getGuid(), pStatus);
						}

					} else {
						master.put(emp.getGuid(), pStatus);
					}
				}

				/*
				 * if ( pStatusTemp.getStatus().equals("RegisteredC") ) { if (
				 * pStatus.getStatus().equals("Registered") ) {
				 * master.put(emp.getGuid(),pStatus); } else if (
				 * pStatus.getStatus().equals("Assigned") ) {
				 * pStatus.setStatus("Registered");
				 * master.put(emp.getGuid(),pStatus); } else if (
				 * pStatus.getStatus().equals("Complete") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); } else if (
				 * pStatus.getStatus().equals("In Progress") ) { // Added for
				 * the fix -17Sep master.put(emp.getGuid(),pStatus); } else if (
				 * pStatus.getStatus().equals("Cancelled") ) {
				 * master.put(emp.getGuid(),pStatus); } //End of addition
				 * 
				 * }else if ( pStatusTemp.getStatus().equals("Registered") ||
				 * pStatus.getStatus().equals("In Progress")) { // Added 'In
				 * Progress' for the fix -17Sep// if (
				 * pStatus.getStatus().equals("Complete") ||
				 * pStatus.getStatus().equals("Pending") ||
				 * pStatus.getStatus().equals("Cancelled") //||
				 * pStatus.getStatus().equals("Exempt") ||
				 * pStatus.getStatus().equals("Waived") // Added for 'In
				 * Progress' status release ||
				 * pStatus.getStatus().equals("In Progress")){
				 * master.put(emp.getGuid(),pStatus); } }else if (
				 * pStatusTemp.getStatus().equals("Cancelled") ) { // Modified
				 * for the fix -17Sep //if (
				 * pStatus.getStatus().equals("Complete") if (
				 * pStatus.getStatus().equals("Assigned") ||
				 * pStatus.getStatus().equals("Pending") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); } }else if (
				 * pStatusTemp.getStatus().equals("Pending") ) { if (
				 * pStatus.getStatus().equals("Complete") //||
				 * pStatus.getStatus().equals("Exempt") ||
				 * pStatus.getStatus().equals("Waived") // Added for 'In
				 * Progress' status release ||
				 * pStatus.getStatus().equals("In Progress")){
				 * master.put(emp.getGuid(),pStatus); } }else if (
				 * pStatusTemp.getStatus().equals("Waived") ) { //if (
				 * pStatusTemp.getStatus().equals("Exempt") ) { if
				 * (pStatus.getStatus().equals("Complete")) {
				 * master.put(emp.getGuid(),pStatus); } }else if (
				 * pStatusTemp.getStatus().equals("Assigned") ) { if (
				 * pStatus.getStatus().equals("Complete") ||
				 * pStatus.getStatus().equals("Registered") ||
				 * pStatus.getStatus().equals("Pending") //||
				 * pStatus.getStatus().equals("Exempt") ||
				 * pStatus.getStatus().equals("Waived") // Added for 'In
				 * Progress' status release ||
				 * pStatus.getStatus().equals("In Progress")) {
				 * master.put(emp.getGuid(),pStatus); }else if (
				 * pStatus.getStatus().equals("RegisteredC") ) {
				 * pStatus.setStatus("Registered");
				 * master.put(emp.getGuid(),pStatus); } }
				 */

			}

		}
		/* This was the old code */
		return master.values();

		// return sortedList;
	}

	// ended for RBU
	public Collection getSubSetPhaseStatus(UserTerritory ut,
			P2lTrackPhase phase, UserFilter uFilter, boolean isDetail,
			String otherNodes) {

		ReadProperties props = new ReadProperties();
		String nodes = "";
		String completeNodes = "";

		boolean doFulltree = false;
		String emplid = uFilter.getEmployeeId();
		// System.out.println("Employee ID----------"+emplid);
		// System.out.println("ISADMIN----------"+uFilter.isAdmin());
		// System.out.println("ISTSRADMIN----------"+uFilter.isTsrAdmin());
		// System.out.println("ISTSRORADMIN----------"+uFilter.isTsrOrAdmin());

		if (!Util.isEmpty(phase.getAlttActivityId())) {
			nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()
					+ otherNodes;
			completeNodes = phase.getRootActivityId() + ","
					+ phase.getAlttActivityId() + otherNodes;
		} else {
			nodes = phase.getRootActivityId() + otherNodes;
			completeNodes = phase.getRootActivityId() + otherNodes;
		}
		if (phase.getPrerequisite()) {

			List tmplist = getPrerequisite(phase.getRootActivityId(), phase);
			if (tmplist != null && tmplist.size() > 0) {
				for (Iterator itr = tmplist.iterator(); itr.hasNext();) {
					Map item = (Map) itr.next();
					BigDecimal tnode = (BigDecimal) item.get("preqactfk"
							.toUpperCase());
					// P2lTrackPhase tmpPhase = new P2lTrackPhase();
					//if(tnode!=null)
					nodes = nodes + "," + tnode.toString();
				}
			}
		}
		List result = new ArrayList();
		/* Modified for RBU changes */
		String sqlAlt = props.getValue("sqlAlt");
		// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
		// employee grid)

		// String pendingSql =
		// " select ACTIVITYFK, emp_no,'Pending' as status, null as completedate  "
		// +
		String pendingSql = " select ACTIVITYFK, emp_no,'Pending' as status, lstupd as completedate, 'NA' as score  "
				+ " from mv_usp_pending  " + " union  ";

		String sqlAllOptionalFields = " e.sex as gender, e.promotion_date as promoDate, f.email_address as memailaddress, e.sales_position_type_cd as source, e.hire_date as hireDate, e.state as state, e.home_state as home_state, ";
		// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee
		// grid)
		// Start: added for HQ user requirement for TRT Phase 2 enhancement.
		String hqOptionalFields = "";
		// if (uFilter.isHqUser()) {
		hqOptionalFields = " ,field_active,is_hq_user,e.group_cd, reports_to_rel_1 ";
		// }

		if (!isDetail) {
			sqlAlt = "";
		}
		// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
		// employee grid)
		String sql = "select distinct emp_no,score,E.EMPLID, completedate, "
				+ sqlAllOptionalFields
				+ "  e.last_name as lastName, e.first_name as firstName, f.last_name as mlastName, f.preferred_name as mpreferredName, e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, e.nt_id as NT_ID , completedate as statusdate, score, "
				+ " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				+
				// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
				// employee grid)
				// Start: added for HQ user requirement for TRT Phase 2
				// enhancement.
				hqOptionalFields
				+
				// End
				/*
				 * Start: Commenting for TRT 3.6 enhancement - F 4.5 -(user view
				 * of employee grid) String sql =
				 * "select distinct emp_no,E.EMPLID, completedate, e.last_name as lastName, e.first_name as firstName, "
				 * +
				 * "f.last_name as mlastName, f.preferred_name as mpreferredName,e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, "
				 * +
				 * "e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				 * + End of commenting
				 */

				/*
				 * Commenting out for CSO enhancement
				 * "from (select activity_pk, emp_no,'Registered' as status, null as completedate  "
				 * + "                   from mv_usp_registered  " +
				 * "               union  " +
				 * "               select activity_pk, emp_no,'Assigned' as status, null as completedate  "
				 * + "                   from mv_usp_assigned  " +
				 * "               union  " +
				 * "               select ACTIVITYFK, emp_no,'RegisteredC' as status, null as completedate  "
				 * + "                   from mv_usp_completed  " +
				 * "               union  "; End of commenting
				 */
				/*
				 * Log: Modified by Meenakshi.M.B on 14-May-2010 Changing the
				 * condition for CSO requirements. Introducing the new MV to
				 * improve performance of the query
				 */
				" from (select activity_pk, emp_no,status, completedate,score from mv_activity_status"
				+ " union ";

		String orderByClause = "order by e.last_name";

		if (phase.getApprovalStatus()) {
			sql = sql + pendingSql;
		}
		String subScriptionClause = " and rel_type <> 'Subscription' ";
		if ("CPT".equals(phase.getTrack().getTrackType())) {
			subScriptionClause = " ";
		}
		/*
		 * The following condition is for Admin users to see all the training
		 * records else see only those records of the employee having reports to
		 * as the logged in user's employee id
		 */
		// Start: added for HQ user requirement for TRT Phase 2 enhancement.
		// if(uFilter.isAdmin() || uFilter.isTsrAdmin() ||
		// uFilter.isTsrOrAdmin())
		if (uFilter.isAdmin() || uFilter.isTsrAdmin() || uFilter.isTsrOrAdmin()
				|| uFilter.isHqUser()) {
			// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			// sql = sql +
			// "               select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			// +
			sql = sql
					+ "               select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score"
					+ "                   from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ "               union  "
					+
					// "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
					// +
					"               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score"
					+ "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+
					/* Added for RBU changes */
					// "(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "+
					// Start: added for HQ user requirement for TRT Phase 2
					// enhancement.
					// "(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, nt_id, sex, state, hire_date, promotion_date, sales_position_id_desc,sales_position_type_cd from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f "+
					"(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, nt_id, sex, state, home_state, hire_date, promotion_date, sales_position_id_desc,sales_position_type_cd,field_active,is_hq_user, reports_to_rel_1 from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f "
					+
					/* End of addition */
					" where a.activity_pk in  "
					+ "       (select activity_pk  "
					+ "           from mv_usp_activity_hierarchy  "
					+ "           start with activity_pk in ("
					+ nodes
					+ ")  "
					+ "           connect by prior activity_pk = prntactfk "
					+ subScriptionClause
					+ ") "
					+ "           and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
			// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
		} else {
			/* Commenting out for CSO requirement changes */
			/*
			 * sql = sql +
			 * "               select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			 * +
			 * "                   from mv_usp_completed c where c.status='Exempt' and c.activityfk in ("
			 * + completeNodes +")   " + "               union  " +
			 * "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
			 * +
			 * "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
			 * + completeNodes +") ) a,  " + // /* Added for RBU changes
			 * "(SELECT (select distinct emplid "+
			 * " from MV_FIELD_EMPLOYEE_RBU where emplid=x.reports_to_emplid) REPORTS_TO_EMPLID,"
			 * +
			 * " emplid, GUID, empl_status, LEVEL org_level,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc FROM MV_FIELD_EMPLOYEE_RBU x "
			 * +
			 * " CONNECT BY PRIOR emplid=x.reports_to_emplid START WITH reports_to_emplid= ("
			 * + emplid+
			 * ")) e, (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "
			 * + /* End of addition " where a.activity_pk in  " +
			 * "       (select activity_pk  " +
			 * "           from mv_usp_activity_hierarchy  " +
			 * "           start with activity_pk in (" + nodes + ")  " +
			 * "           connect by prior activity_pk = prntactfk " +
			 * subScriptionClause + ") " +
			 * "           and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) "
			 * ;
			 */
			/* End of comment */
			/*
			 * Modified Log by Meenakshi.M.B: Changing the query for CSO
			 * enhancement
			 */
			/*
			 * sql = sql +
			 * " select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			 * +
			 * " from mv_usp_completed c where c.status='Exempt' and c.activityfk in ("
			 * + completeNodes +")   " + " union  " +
			 * " select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
			 * +
			 * " from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
			 * + completeNodes +") ) a,  " +
			 * " (select x.reports_to_emplid,x.emplid,guid,x.empl_status,y.org_level, x.last_name,x.first_name,x.preferred_name,x.sales_group,group_cd, "
			 * +
			 * " x.email_address,x.role_cd,x.geo_desc,x.bu,x.sales_position_id_desc,x.nt_id, x.sex, x.state, x.hire_date, x.promotion_date from mv_field_employee_rbu x, "
			 * +
			 * " (select distinct sales_position_id,emplid,LEVEL as org_level from MV_FIELD_EMPLOYEE_RELATION connect by prior "
			 * + " emplid=related_emplid start with related_emplid= ("+ emplid +
			 * ")) y where x.emplid=y.emplid and x.sales_position_id=y.sales_position_id) e, "
			 * +
			 * " (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f where a.activity_pk in (select activity_pk "
			 * + " from mv_usp_activity_hierarchy  " +
			 * " start with activity_pk in (" + nodes + ")  " +
			 * " connect by prior activity_pk = prntactfk " + subScriptionClause
			 * + ") " +
			 * " and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
			 */
			sql = sql
					+ " select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score "
					+ " from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ " union  "
					+ " select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score "
					+ " from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+ " (select x.reports_to_emplid,x.emplid,guid,x.empl_status,y.org_level, x.last_name,x.first_name,x.preferred_name,x.sales_group,group_cd, "
					+
					// " x.email_address,x.role_cd,x.geo_desc,x.bu,x.sales_position_id_desc,x.nt_id, x.sex, x.state, x.hire_date, x.promotion_date,x.sales_position_type_cd from mv_field_employee_rbu x, "+
					" x.email_address,x.role_cd,x.geo_desc,x.bu,x.sales_position_id_desc,x.nt_id, x.sex, x.state, x.home_state, x.hire_date, x.promotion_date,x.sales_position_type_cd, "
					+
					// Added extra fields for changes due to HQ users
					// requirement of TRT Phase 2 enhancement
					" field_active,is_hq_user, reports_to_rel_1 "
					+ " from mv_field_employee_rbu x, "
					+ " (select distinct sales_position_id,emplid,LEVEL as org_level from MV_FIELD_EMPLOYEE_RELATION connect by prior "
					+ " emplid=related_emplid start with related_emplid= ("
					+ emplid
					+ ")) y where x.emplid=y.emplid and x.sales_position_id=y.sales_position_id) e, "
					+ " (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f where a.activity_pk in (select activity_pk "
					+ " from mv_usp_activity_hierarchy  "
					+ " start with activity_pk in ("
					+ nodes
					+ ")  "
					+ " connect by prior activity_pk = prntactfk "
					+ subScriptionClause
					+ ") "
					+ " and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
		}

		// System.out.println("\n uFilter.isRefresh()"+uFilter.isRefresh());
		// added for RBU
		if (uFilter.isRefresh() == true) {
			sql = sql + buildCriteria(ut, uFilter) + orderByClause;
		} else {
			sql = sql + orderByClause;
		}
		// ended for RBU
		// System.out.println("Final SQL---\n"+sql);
		Timer timer = new Timer();
		List temp = executeSql2(sql);
		// Map master = new HashMap();
		/* Modified to fix the default sorting of Last name */
		Map master = new LinkedHashMap();
		P2lEmployeeStatus pStatus;

		List sortedList = new ArrayList();

		for (int i = 0; i < temp.size(); i++) {
			Employee emp = new Employee();
			String tempfname = "";
			Map map = (Map) temp.get(i);
			emp.setManagerFname(Util.toEmpty((String) map.get("mpreferredName"
					.toUpperCase())));
			emp.setManagerLname(Util.toEmpty((String) map.get("mlastName"
					.toUpperCase())));
			// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			emp.setManagerEmail(Util.toEmpty((String) map.get("MEMAILADDRESS"))); // for
																					// email
																					// of
																					// manager
			// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			emp.setRegionDesc(Util.toEmpty((String) map.get("regionDesc"
					.toUpperCase())));
			emp.setAreaDesc(Util.toEmpty((String) map.get("areaDesc"
					.toUpperCase())));
			emp.setGuid(Util.toEmpty((String) map.get("EMP_NO")));
			emp.setEmplId((String) map.get("EMPLID"));
			emp.setEmail(Util.toEmpty((String) map.get("EMAIL")));
			emp.setEmployeeStatus((String) map.get("EMPL_STATUS"));
			emp.setPreferredName((String) map.get("preferredName".toUpperCase()));
			emp.setFirstName((String) map.get("preferredName".toUpperCase()));
			/* Code added for bug fix for first name display of MDM users */
			tempfname = emp.getFirstName();
			if (tempfname == null || tempfname.equals("")) {
				emp.setFirstName((String) map.get("firstName".toUpperCase()));
			}
			/* End of addition */
			emp.setLastName((String) map.get("lastName".toUpperCase()));
			emp.setDistrictDesc((String) map.get("districtDesc".toUpperCase()));
			emp.setTeamCode((String) map.get("teamCode".toUpperCase()));
			emp.setClusterCode((String) map.get("clusterCode".toUpperCase()));
			emp.setRole((String) map.get("role".toUpperCase()));
			emp.setGeographyDesc((String) map.get("geoDesc".toUpperCase()));
			emp.setBusinessUnit((String) map.get("bu".toUpperCase()));
			emp.setSalesOrgDesc((String) map.get("salesOrgDesc".toUpperCase()));
			emp.setSalesPostionDesc((String) map.get("salesPositionDesc"
					.toUpperCase()));

			// Start: Modified for TRT 3.6 enhancement - F 4.5 ( Display of
			// employee grid)

			emp.setNtId((String) map.get("NT_ID"));
			emp.setState((String) map.get("STATE"));
			emp.setPromoDate((Date) map.get("PROMODATE"));
			emp.setGender((String) map.get("GENDER"));
			emp.setHireDate((Date) map.get("HIREDATE"));
			emp.setSource((String) map.get("SOURCE"));
			emp.setHomeState((String) map.get("HOME_STATE"));

			pStatus = new P2lEmployeeStatus(emp, (String) map.get("STATUS"),
					phase.getPhaseNumber());
			pStatus.setStatus((String) map.get("STATUS"));
			pStatus.setScore((String) map.get("SCORE"));
			String score=null;
			//if(map.get("SCORE")!=null){
			score = map.get("SCORE").toString();
			if (score.equals("NA")) {

				pStatus.setScore("");
			} else {
				pStatus.setScore(map.get("SCORE").toString());

			}
			//}
			// End: Modified for TRT 3.6 enhancement - F 4.5( Display of
			// employee grid)
			// Start : Added for Phase 2 HQ Users requirement.
			emp.setGroupCD((String) map.get("GROUP_CD"));
			emp.setHQManager(Util.toEmpty((String) map.get("reports_to_rel_1"
					.toUpperCase())));
			// End

			Object obj = map.get("completedate".toUpperCase());
			if (obj != null) {
				// pStatus.setCompleteDate(new
				// java.sql.Date(((java.sql.Timestamp)obj).getTime()));
				pStatus.setCompleteDate(new java.sql.Date(
						((java.util.Date) obj).getTime()));
			} else {
				pStatus.setCompleteDate(null);
			}
			if (master.get(emp.getGuid()) == null) {
				master.put(emp.getGuid(), pStatus);
				// sortedList.add(pStatus);
			} else {
				P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus) master
						.get(emp.getGuid());
				if (pStatusTemp.getStatus().equals("RegisteredC")) {
					if (pStatus.getStatus().equals("Registered")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
					if (pStatus.getStatus().equals("Assigned")) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
					if (pStatus.getStatus().equals("Complete")
					// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Registered")) {
					// Start: Added for Major Enhancement 3.6 - F1

					if (pStatus.getStatus().equals("Complete")
							|| pStatus.getStatus().equals("Pending")
							|| pStatus.getStatus().equals("Cancelled")
							// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Cancelled")) {
					if (pStatus.getStatus().equals("Complete")
							|| pStatus.getStatus().equals("Pending")
							// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Pending")) {
					if (pStatus.getStatus().equals("Complete")
					// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						sortedList.add(pStatus);
					}
				}
				// if ( pStatusTemp.getStatus().equals("Exempt") ) {
				if (pStatusTemp.getStatus().equals("Waived")) {
					if (pStatus.getStatus().equals("Complete")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Assigned")) {
					if (pStatus.getStatus().equals("Complete")
							|| pStatus.getStatus().equals("Registered")
							|| pStatus.getStatus().equals("Pending")
							// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
					if (pStatus.getStatus().equals("RegisteredC")) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}

			}

		}
		/* This was the old code */
		return master.values();

		// return sortedList;
	}

	// ended for RBU

	public Employee[] search(String nodes, UserSession uSession,
			EmplSearchForm form) {

		String sql = " and e.guid IN "
				+ " (select  emp_no "
				+ "           from (  select activity_pk, activityname, emp_no,'Registered' "
				+ "                       from mv_usp_registered "
				+ "   union "
				+ "                   select activity_pk, activityname, emp_no,'Assigned' "
				+ "                       from mv_usp_assigned  "
				+ "   union "
				+ "                   select ACTIVITYFK, activityname, emp_no,'Completed' "
				+ "                       from mv_usp_completed) a "
				+ "           where a.activity_pk in  "
				+ "               (select activity_pk  "
				+ "                   from mv_usp_activity_hierarchy "
				+ "                   start with activity_pk in ("
				+ nodes
				+ ")"
				+ "                   connect by prior activity_pk = prntactfk)) ";

		if (!Util.isEmpty(form.getEmplid())) {
			sql = " and e.emplid = '" + form.getEmplid().trim() + "' " + sql;
		}
		if (!Util.isEmpty(form.getSalesposId())) {
			sql = " and e.SALES_POSITION_ID = '" + form.getSalesposId() + "' "
					+ sql;
		} // else {
			// Start: Modified for TRT 3.6 enhancement - F 4.1-(additional
			// search fields)
		if (!Util.isEmpty(form.getRole()) && !form.getRole().equals("All")) {
			sql = " and e.ROLE_CD = '" + form.getRole().trim() + "' " + sql;
		}
		if (!Util.isEmpty(form.getBu()) && !form.getBu().equals("All")) {
			sql = " and e.BU = '" + form.getBu().trim() + "' " + sql;
		}
		if (!Util.isEmpty(form.getFname()) || !Util.isEmpty(form.getLname())) {
			// End of modification
			if (Util.isEmpty(form.getFname()) && Util.isEmpty(form.getLname())) {
				return null;
			}
			String fsql = "";
			String lsql = "";
			if (!Util.isEmpty(form.getFname())) {
				fsql = " and (upper(e.FIRST_NAME) like '%"
						+ form.getFname().toUpperCase()
						+ "%' or upper(e.preferred_name) like '%"
						+ form.getFname().toUpperCase() + "%' )";
			}
			if (!Util.isEmpty(form.getLname())) {
				lsql = " and upper(e.LAST_NAME) like '%"
						+ form.getLname().toUpperCase() + "%' ";
			}
			sql = fsql + lsql + sql;
		}

		// sql = sql + buildCriteria(uSession.getUserFilter());
		// System.out.println("\nSQL SEARCH-----"+sql);
		return executeSql3(sql, uSession);
	}

	public List getPedScores(Map activityIds, String guid) {
		List result = new ArrayList();
		String ids = "";

		for (Iterator it = activityIds.keySet().iterator(); it.hasNext();) {
			if (Util.isEmpty(ids)) {
				ids = (String) it.next();
			} else {
				ids = ids + "," + (String) it.next();
			}
		}
		// System.out.println("IDS"+ids);
		String sql = " Select    * from sv_course_compl a where a.activity_pk in ("
				+ ids + ") " +
				// String sql =
				// " Select * from vt_course_compl a where a.activity_pk in (" +
				// ids + ") " +
				" and a.emp_no = '" + guid + "'";

		// System.out.println("P2l query:"+sql);
		result = executeSql2(sql);

		return result;
	}

public P2lActivityStatus getPhaseDetail(String employeeId , P2lTrackPhase phase ) {
        String nodes = "";
        String altNode = phase.getAlttActivityId();
        String activitypk = phase.getRootActivityId();
        if (!Util.isEmpty(altNode)) {
            if ( checkCompleteAndRegistered( activitypk, employeeId ) ) {
                //System.out.println("got it");
                nodes = activitypk ;
            } else {
                nodes = altNode;
            }
        } else {
            nodes = activitypk;
        }
        /* Change log: Meenakshi: Modified the query to add ip_check flag for 'In Progress' changes */        
        String sql = "select hi.activity_pk, hi.prntactfk, hi.mname, hi.mlevel, hi.rel_type, hi.ACTLABEL_NAME, hi.activityname, " +
                       //" select hi.*, " +
                        " action.action_check, " +
                        "   attemp.CURRENTATTEMPTIND, " +
                        " attemp.EStatus, " +
                        "   m.code, attemp.enddt, " +
                        "   r.reg_check, r.regdate," +
                        "   ip.ip_check, ip.ipdate," +
                        "   rq.assign_check,rq.asndate, " +
                        "   can.cancel_check, can.candate, " +
                        "   attemp.att_check as has_attempt, "+
                        "   r.reg_check as has_registered, " +
                        "   ip.ip_check as has_inprogress, "+
                        "   rq.assign_check as has_assigned, "+
                        "   can.cancel_check as has_cancelled, "+
                        "   com.com_check as has_completed, com.comdate,noshow.noshow_check as has_noshow, noshow.noshowdate,"+
                        "   attemp.score, " +
                        "   success, " +
                        "   decode(pend.activityfk,null,'no','yes') as pending_check,"+
                        "   CompletionStatus " +
                        " from  " +     
                        "   ( select rownum as myrow,activity_pk,prntactfk,lpad(' ', level*4,'-') as mname,  level as mlevel, m.REL_TYPE, m.ACTLABEL_NAME, activityname  " + 
                        "   from mv_USP_ACTIVITY_HIERARCHY m " +
                        "	start with activity_pk in  (" + nodes  + ") and rel_type='Parent' " +
                        "	connect by prior activity_pk = prntactfk  and level < 9 ) hi " + 
                        "   , mv_usp_activity_master m " +
                        "   , (select activityfk from mv_usp_pending where emp_no='" + employeeId + "') pend " +
                        /* Modifying the query to get 'Waived' status from mv_usp_completed by Meenakshi*/
                        "	, ( select distinct att.enddt, att.ACTIVITYFK,att.CURRENTATTEMPTIND , 'true' as att_check, att.score, decode(success,1,'Pass',0,'Fail') as success, decode(CompletionStatus,1,'Complete',0,'Not Complete') as CompletionStatus, mc.status as EStatus  from MV_USP_ATTEMPT att " + 
                    	"				   	 			   	, mv_usp_completed mc " + 
						"									where att.EMP_NO = '" + employeeId + "' and mc.EMP_NO = '" + employeeId + "' and att.activityfk=mc.activityfk) attemp " + 
                        "   , ( select max(reg.REGISTRATION_DATE) as regdate, reg.ACTIVITY_pK , 'true' as reg_check, score   from mv_usp_registered reg " +
                        "                                   where reg.EMP_NO = '" + employeeId + "' and reg.status=0 group by reg.ACTIVITY_pK,score) r " +
                        "   , ( select max(reg.LSTUPD) as ipdate ,reg.ACTIVITY_pK , 'true' as ip_check, score   from mv_usp_in_progress reg " +
                        "                                   where reg.EMP_NO = '" + employeeId + "' group by reg.ACTIVITY_pK,score) ip " +                        
                        "   , ( select distinct act.ACTIVITY_id , 'Complete' as action_check from p2l_activity_action act " + 
                        "                                   where act.empl_guid = '" + employeeId + "' ) action " +
                        "   , ( select max(com.COMPLETION_DATE) as comdate, COM.ACTIVITYfK , 'true' as com_check, score   from mv_usp_completed com " +
                        "                                   where com.EMP_NO = '" + employeeId + "'  group by COM.ACTIVITYfK,score) com " +
						"   , ( SELECT max(ma.ASSIGNMENT_DATE) asndate, ma.ACTIVITY_PK , 'true' AS assign_check" +
                        "                   FROM mv_usp_assigned ma " +  
						"									WHERE ma.emp_no = '" + employeeId + "' group by ma.ACTIVITY_PK)  rq "+ 
                        "   , ( SELECT max(can.CANCELLATION_DATE) as candate, can.ACTIVITY_PK , 'true' AS cancel_check" +
                        "                   FROM mv_usp_cancelled can " +  
						"									WHERE can.emp_no = '" + employeeId + "'  group by can.ACTIVITY_PK )  can "+
                        "  , ( SELECT noshow.no_show_date as noshowdate, noshow.ACTIVITY_PK , 'true' AS noshow_check "+
                        "                   FROM mv_usp_no_show noshow  WHERE noshow.emp_no = '"+employeeId+"'   )  noshow "+
                        "   where " + 
                        "	 hi.activity_pk = attemp.ACTIVITYFK(+) " +
                        "       and m.ACTIVITY_PK  = hi.activity_pk " + 
                        "       and hi.activity_pk = r.ACTIVITY_pK(+) " +
                        "       and hi.activity_pk = ip.ACTIVITY_PK(+) "+
                        "       and hi.activity_pk = com.ACTIVITYfK(+) " +
                        "       and hi.activity_pk = pend.ACTIVITYFK(+) " +
                        "       and hi.activity_pk = action.ACTIVITY_id(+) " +
                        "       and hi.activity_pk = rq.ACTIVITY_PK(+)" +
                        "       and hi.activity_pk = can.ACTIVITY_PK(+) " +
                        "       and hi.activity_pk = noshow.activity_pk(+) order by hi.myrow";
                        
        List result = executeSql2(sql); 
       // System.out.println("\nGetPhaseDetail-----------------"+sql);
        HashMap tempMap = new HashMap();
        P2lActivityStatus emp = null;
        Iterator it = result.iterator();
        P2lActivityStatus curr = new P2lActivityStatus((HashMap)it.next());
        tempMap.put(new Integer(curr.getActivityId()), curr);
        for (int i=0; it.hasNext();) {
            P2lActivityStatus next = new P2lActivityStatus((HashMap)it.next());
            //System.out.println("Activity ID-----------------"+next.getActivityId());
            if ( tempMap.get( new Integer(next.getActivityId()) ) == null ) {
                //System.out.println("Putting into map-----------------"+new Integer(next.getActivityId()));
                tempMap.put(new Integer(next.getActivityId()), next);
            }
            if ( next.getParentid() != 0 ) {
                P2lActivityStatus tmp = (P2lActivityStatus)tempMap.get(new Integer(next.getParentid()));
                if (tmp != null) {
                    //System.out.println("Activity ID--Outside if"+next.getActivityId());             
                        // Selvam 9-june-2008 : Some of the course in P2L registered in Fullfilment level, This produces duplicate
                    // entry in employee detail page. The following logic will avoid that duplicate.                                     
                        if (next.isFullfilment() == true && next.isRegistered()== true){
                            //System.out.println("Activity ID--Inside if"+next.getActivityId());
                            continue;
                        }
                    //System.out.println("Adding child----"+next.getActivityId());                                          
                    tmp.addChild( next );
                    
                }            
            }
        }
        if ( phase.getPrerequisite() && !nodes.equals(altNode))  {
           // System.out.println("Activity ID--Inside second if");
            List tmplist = getPrerequisite(activitypk,phase);
            if ( tmplist != null && tmplist.size() > 0 ) {
                for (Iterator itr = tmplist.iterator(); itr.hasNext();) {
                    Map item = (Map)itr.next();
                    BigDecimal tnode = (BigDecimal)item.get("preqactfk".toUpperCase());
                    P2lTrackPhase tmpPhase = new P2lTrackPhase();
                    tmpPhase.setRootActivityId(tnode.toString());
                    P2lActivityStatus tmp = getPhaseDetail(employeeId,tmpPhase);
                    tmp.setRaiselevel(1);
                    curr.addChild(tmp);
                }
            }            
        }
        //System.out.println("\n-------------Returning Curr-----------------"+curr.getActivityId());
        return curr;
    }
	// Start : Added for TRT Phase 2 - Requirement no. 6
	public P2lActivityStatus getSinglePhaseDetail(String employeeId,
			String activitypk) {
		String nodes = "";
		/*
		 * String altNode = phase.getAlttActivityId(); String activitypk =
		 * phase.getRootActivityId(); if (!Util.isEmpty(altNode)) { if (
		 * checkCompleteAndRegistered( activitypk, employeeId ) ) {
		 * System.out.println("got it"); nodes = activitypk ; } else { nodes =
		 * altNode; } } else {
		 */
		nodes = activitypk;
		// }

		String sql = "select hi.activity_pk, hi.prntactfk, hi.mname, hi.mlevel, hi.rel_type, hi.ACTLABEL_NAME, hi.activityname, "
				+
				// " select hi.*, " +
				" action.action_check, "
				+ "   attemp.CURRENTATTEMPTIND, "
				+ " attemp.EStatus, "
				+ "   m.code, attemp.enddt, "
				+ "   r.reg_check, r.regdate, "
				+ "   rq.assign_check, rq.asndate,"
				+ "   can.cancel_check, can.candate,"
				+ "   attemp.att_check as has_attempt, "
				+ "   r.reg_check as has_registered, "
				+ "   rq.assign_check as has_assigned, "
				+ "   can.cancel_check as has_cancelled, "
				+ "   com.com_check as has_completed, com.comdate, noshow.noshow_check as has_noshow, noshow.noshowdate,"
				+ "   attemp.score, "
				+ "   success, "
				+ "   decode(pend.activityfk,null,'no','yes') as pending_check,"
				+ "   CompletionStatus "
				+ " from  "
				+ "   ( select rownum as myrow,activity_pk,prntactfk,lpad(' ', level*4,'-') as mname,  level as mlevel, m.REL_TYPE, m.ACTLABEL_NAME, activityname  "
				+ "   from mv_USP_ACTIVITY_HIERARCHY m "
				+ "	start with activity_pk in  ("
				+ nodes
				+ ") and rel_type='Parent' "
				+ "	connect by prior activity_pk = prntactfk  and level < 9 ) hi "
				+ "   , mv_usp_activity_master m "
				+ "   , (select activityfk from mv_usp_pending where emp_no='"
				+ employeeId
				+ "') pend "
				+ "	, ( select distinct att.enddt, att.ACTIVITYFK,att.CURRENTATTEMPTIND , 'true' as att_check, score, decode(success,1,'Pass',0,'Fail') as success, decode(CompletionStatus,1,'Complete',0,'Not Complete') as CompletionStatus, decode(att.EXEMPTIND,1,'Exempt',0,'Complete') as EStatus  from MV_USP_ATTEMPT att "
				+ "									where att.EMP_NO = '"
				+ employeeId
				+ "' ) attemp "
				+ "   , ( select max(reg.REGISTRATION_DATE) as regdate,reg.ACTIVITY_pK , 'true' as reg_check, score   from mv_usp_registered reg "
				+ "                                   where reg.EMP_NO = '"
				+ employeeId
				+ "' group by reg.ACTIVITY_pK,score ) r "
				+ "   , ( select distinct act.ACTIVITY_id , 'Complete' as action_check from p2l_activity_action act "
				+ "                                   where act.empl_guid = '"
				+ employeeId
				+ "' ) action "
				+ "   , ( select max(com.COMPLETION_DATE) as comdate,COM.ACTIVITYfK , 'true' as com_check, score   from mv_usp_completed com "
				+ "                                   where com.EMP_NO = '"
				+ employeeId
				+ "' group by COM.ACTIVITYfK,score ) com "
				+ "   , ( SELECT max(ma.ASSIGNMENT_DATE) asndate,ma.ACTIVITY_PK , 'true' AS assign_check"
				+ "                   FROM mv_usp_assigned ma "
				+ "									WHERE ma.emp_no = '"
				+ employeeId
				+ "' group by ma.ACTIVITY_PK)  rq "
				+ "   , ( SELECT max(can.CANCELLATION_DATE) as candate,can.ACTIVITY_PK , 'true' AS cancel_check"
				+ "                   FROM mv_usp_cancelled can "
				+ "									WHERE can.emp_no = '"
				+ employeeId
				+ "' group by can.ACTIVITY_PK )  can "
				+ "  , ( SELECT noshow.no_show_date as noshowdate, noshow.ACTIVITY_PK , 'true' AS noshow_check "
				+ "                   FROM mv_usp_no_show noshow  WHERE noshow.emp_no = '"
				+ employeeId
				+ "'  )  noshow "
				+ "   where "
				+ "	 hi.activity_pk = attemp.ACTIVITYFK(+) "
				+ "       and m.ACTIVITY_PK  = hi.activity_pk "
				+ "       and hi.activity_pk = r.ACTIVITY_pK(+) "
				+ "       and hi.activity_pk = com.ACTIVITYfK(+) "
				+ "       and hi.activity_pk = pend.ACTIVITYFK(+) "
				+ "       and hi.activity_pk = action.ACTIVITY_id(+) "
				+ "       and hi.activity_pk = rq.ACTIVITY_PK(+)"
				+ "       and hi.activity_pk = can.ACTIVITY_PK(+) "
				+ "       and hi.activity_pk = noshow.activity_pk(+) order by hi.myrow ";

		List result = executeSql2(sql);
		// System.out.println("\nGetSinglePhaseDetail-----------------"+sql);
		HashMap tempMap = new HashMap();
		P2lActivityStatus emp = null;
		Iterator it = result.iterator();
		P2lActivityStatus curr = new P2lActivityStatus((HashMap) it.next());
		tempMap.put(new Integer(curr.getActivityId()), curr);
		for (int i = 0; it.hasNext();) {
			P2lActivityStatus next = new P2lActivityStatus((HashMap) it.next());
			// System.out.println("Activity ID-----------------"+next.getActivityId());
			if (tempMap.get(new Integer(next.getActivityId())) == null) {
				// System.out.println("Putting into map-----------------"+new
				// Integer(next.getActivityId()));
				tempMap.put(new Integer(next.getActivityId()), next);
			}
			if (next.getParentid() != 0) {
				P2lActivityStatus tmp = (P2lActivityStatus) tempMap
						.get(new Integer(next.getParentid()));
				if (tmp != null) {
					// System.out.println("Activity ID--Outside if"+next.getActivityId());
					// Selvam 9-june-2008 : Some of the course in P2L registered
					// in Fullfilment level, This produces duplicate
					// entry in employee detail page. The following logic will
					// avoid that duplicate.
					if (next.isFullfilment() == true
							&& next.isRegistered() == true) {
						// System.out.println("Activity ID--Inside if"+next.getActivityId());
						continue;
					}
					// System.out.println("Adding child----"+next.getActivityId());
					tmp.addChild(next);

				}
			}
		}
		return curr;
	}

	public List getTrainingDetail(String employeeId) {

		String sql = "SELECT DISTINCT com.activityfk,com.activityname,com.completion_date,com.status, score "
				+

				" FROM mv_usp_completed com, mv_p2l_tblemp emp "
				+ " WHERE emp.emp_no='"
				+ employeeId
				+ "' AND com.empfk = emp.emp_pk ";

		List result = executeSql2(sql);
		// System.out.println("\nGetPhaseDetail-----------------"+sql);

		// System.out.println("\n-------------Returning Curr-----------------"+curr.getActivityId());
		return result;
	}

	public List getPrerequisite(String activitypk, P2lTrackPhase phase) {

		String sql = "select distinct preqactfk from MV_P2L_TBL_TMX_ActPreq m where m.ACTIVITYFK in "
				+ " (select activity_pk "
				+ " from mv_usp_activity_hierarchy  "
				+ " start with activity_pk = "
				+ activitypk
				+ " connect by prior activity_pk = prntactfk)";
		List result = executeSql2(sql);
		// status.getKids().clear();
		return result;
	}

	public void getKids(Iterator it, P2lActivityStatus emp) {
		if (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			P2lActivityStatus tmp = new P2lActivityStatus(map);
			if (tmp.getLevel() == (emp.getLevel() + 1)) {
				emp.addChild(tmp);
				if (it.hasNext()) {
					getKids(it, tmp);
				}
			}
		}
	}

	public Employee[] executeSql(String sql, UserFilter uFilter,
			boolean isDetail) {

		EmployeeHandler eh = new EmployeeHandler();
		Employee[] result = eh.getEmployees(uFilter.getFilterForm(), uFilter,
				sql, isDetail);
		// List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
		if (result == null) {
			return null;
		}
		return result;
	}

	public Employee[] executeSql3(String sql, UserSession uSession) {

		EmployeeHandler eh = new EmployeeHandler();
		TerritoryFilterForm form = uSession.getNewTerritoryFilterForm();
		// form.setTeam("All");
		form.setSalesOrg("All");
		Employee[] result = eh.getEmployees(form, uSession.getUserFilter(),
				sql, true);
		// List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
		if (result == null) {
			return null;
		}
		// System.out.println("Returning Search Results"+result.length);
		return result;
	}

	public List executeSql2(String sql) {

		List result = DBUtil.executeSql(sql, AppConst.APP_DATASOURCE);
		System.out.println("excute sql2 dongle" + result.size());
		return result;
	}

	public void insertComplete(UserSession uSession, String activityId,
			Employee emp) {
		P2lActivityAction action = new P2lActivityAction();
		action.setActiontype("Complete");
		action.setActivity_id(activityId);
		if (uSession.isAdmin()) {
			action.setByEmplId(uSession.getOrignalUser().getId());
		} else {
			action.setByEmplId(uSession.getUser().getId());
		}
		action.setEmplid(emp.getEmplId());
		action.setGuid(emp.getGuid());
		action.setSubDate(new Date());

		insertaction(action);
	}

	public void deleteComplete(UserSession uSession, String activityId,
			Employee emp) {
		P2lActivityAction action = new P2lActivityAction();
		action.setActiontype("UnComplete");
		action.setActivity_id(activityId);
		if (uSession.isAdmin()) {
			action.setByEmplId(uSession.getOrignalUser().getId());
		} else {
			action.setByEmplId(uSession.getUser().getId());
		}
		action.setEmplid(emp.getEmplId());
		action.setGuid(emp.getGuid());
		action.setSubDate(new Date());

		deleteAction(action);
	}

	private void insertaction(P2lActivityAction action) {
		String retString = null;
		String insertSql = "insert into p2l_activity_action "
				+ " (action_id, empl_id, activity_id, action_type, by_empl_id, sub_date, empl_guid, status ) "
				+ " values (P2L_ACTIVITY_ACTION_SEQ.nextval,?,?,?,?,sysdate,?,'N') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
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
			statement = conn.prepareStatement(insertSql);
			statement.setString(1, action.getEmplid());
			statement.setString(2, action.getActivity_id());
			statement.setString(3, action.getActiontype());
			statement.setString(4, action.getByEmplId());
			statement.setString(5, action.getGuid());

			statement.executeUpdate();
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
	}

	private void deleteAction(P2lActivityAction action) {
		String retString = null;
		String insertSql = "update  p2l_activity_action set "
				+ "   empl_id=?, activity_id=?, action_type=?, by_empl_id=?, sub_date=sysdate, empl_guid=?, status='D'  "
				+ "   where activity_id=" + action.getActivity_id()
				+ " and empl_id='" + action.getEmplid() + "'";
		ResultSet rs = null;
		PreparedStatement statement = null;
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
			statement = conn.prepareStatement(insertSql);
			statement.setString(1, action.getEmplid());
			statement.setString(2, action.getActivity_id());
			statement.setString(3, action.getActiontype());
			statement.setString(4, action.getByEmplId());
			statement.setString(5, action.getGuid());

			int num = statement.executeUpdate();
			// System.out.println("update:" + num);
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
	}

	public List getEmployeeActivity(CourseSearchForm form) {
		String sqlQuery = " SELECT VNFE.LAST_NAME, VNFE.EMPLID, MUAHM.ACTIVITY_PK, MUAHM.COMPLETE_STATUS ,VNFE.FIRST_NAME, VNFE.ROLE_CD, VNFE.SALES_GROUP,MUAHM.ACTIVITYNAME,VNFE.EMAIL_ADDRESS, M.CODE "
				+ " FROM V_USP_BATCH_COMPLETE MUAHM, MV_FIELD_EMPLOYEE_RBU VNFE, MV_USP_ACTIVITY_MASTER M "
				+ " WHERE MUAHM.activityname =  '"
				+ form.getActivity()
				+ "' "
				+ " AND MUAHM.EMP_NO = VNFE.GUID "
				+ " AND m.code is not null "
				+ " AND M.ACTIVITY_PK = MUAHM.ACTIVITY_PK AND (MUAHM.COMPLETE_STATUS in ('N','C') or MUAHM.COMPLETE_STATUS is null) "
				+ " AND M.ACTIVITY_PK IN ( "
				+ "   select activity_pk "
				+ "    from mv_usp_activity_hierarchy  "
				+ "    start with activity_pk = "
				+ form.getPhase()
				+ "    connect by prior activity_pk = prntactfk)"
				+ " and vnfe.guid in ( select emp_no from v_usp_activity_status vs where vs.activity_pk in (select activity_pk from mv_usp_activity_hierarchy start with activity_pk = "
				+ form.getPhase()
				+ " connect by prior activity_pk = prntactfk and rel_type <> 'Subscription' ) )"
				+ "  order by  VNFE.LAST_NAME ";

		// System.out.println(sqlQuery);
		List result = executeSql2(sqlQuery);

		return result;
	}

	public TreeMap getActivityByPhase(String phaseID) {
		TreeMap output = new TreeMap();
		String sqlQuery = " SELECT ACTIVITY_CODE,ACTIVITYNAME FROM  "
				+ " (SELECT ACTIVITY_CODE,ACTIVITYNAME,ACTLABEL_NAME FROM MV_USP_ACTIVITY_HIERARCHY "
				+ " START WITH ACTIVITY_PK = " + phaseID
				+ " CONNECT BY PRIOR ACTIVITY_PK = PRNTACTFK) "
				+ " WHERE ACTLABEL_NAME = 'ILT Session' ORDER BY ACTIVITYNAME ";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
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
			pstmt = conn.prepareStatement(sqlQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				output.put(rs.getString("ACTIVITY_CODE"),
						rs.getString("ACTIVITYNAME"));
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			log.error(e, e);
		} finally {
		}
		if (pstmt != null) {
			try {
				pstmt.close();
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
		return output;
	}

	public Vector getAllTrainingPhaseTrack() {
		Vector output = new Vector();
		String sqlQuery = " SELECT PT.TRACK_ID,PT.TRACK_LABEL,PTP.PHASE_NUMBER,PTP.ROOT_ACTIVITY_ID "
				+ " FROM P2L_TRACK_PHASE PTP,P2L_TRACK PT "
				+ " WHERE PTP.TRACK_ID=PT.TRACK_ID and pt.track_type='phase'"
				+ " ORDER BY PT.TRACK_LABEL,PTP.PHASE_NUMBER ";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		P2LTrainingTrackPhaseRelation record = null;
		String tempKey = "";
		String trackID = "";
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
			pstmt = conn.prepareStatement(sqlQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				trackID = rs.getString("TRACK_ID");
				if (!tempKey.equals(trackID)) {
					record = new P2LTrainingTrackPhaseRelation();
					record.setTrackID(trackID);
					record.setTrackLabel(rs.getString("TRACK_LABEL"));
					record.getRootActivityID().addElement(
							rs.getString("ROOT_ACTIVITY_ID"));
					record.getPhaseNumber().addElement(
							rs.getString("PHASE_NUMBER"));
					output.addElement(record);
				} else {
					record.getRootActivityID().addElement(
							rs.getString("ROOT_ACTIVITY_ID"));
					record.getPhaseNumber().addElement(
							rs.getString("PHASE_NUMBER"));
				}
				tempKey = trackID;
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			log.error(e, e);
		} finally {

			if (pstmt != null) {
				try {
					pstmt.close();
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
		return output;
	}

	/* Added for Psychiatry CUE Training */

	public Collection getCuePhaseStatus(UserTerritory ut, P2lTrackPhase phase,
			UserFilter uFilter, boolean isDetail, String otherNodes) {
		ReadProperties props = new ReadProperties();
		String nodes = "";
		String completeNodes = "";
		boolean doFulltree = false;
		String emplid = uFilter.getEmployeeId();
		// System.out.println("Employee ID----------"+emplid);
		// System.out.println("Inside getCuePhaseStatus");
		// System.out.println("Root activity ID"+phase.getRootActivityId());
		String activityid = phase.getRootActivityId();

		if (!Util.isEmpty(phase.getAlttActivityId())) {
			nodes = phase.getRootActivityId() + "," + phase.getAlttActivityId()
					+ otherNodes;
			completeNodes = phase.getRootActivityId() + ","
					+ phase.getAlttActivityId() + otherNodes;
		} else {
			nodes = phase.getRootActivityId() + otherNodes;
			completeNodes = phase.getRootActivityId() + otherNodes;
		}
		if (phase.getPrerequisite()) {

			List tmplist = getPrerequisite(phase.getRootActivityId(), phase);
			if (tmplist != null && tmplist.size() > 0) {
				for (Iterator itr = tmplist.iterator(); itr.hasNext();) {
					Map item = (Map) itr.next();
					BigDecimal tnode = (BigDecimal) item.get("preqactfk"
							.toUpperCase());
					// P2lTrackPhase tmpPhase = new P2lTrackPhase();
					//if(tnode!=null)
					nodes = nodes + "," + tnode.toString();
				}
			}
		}
		List result = new ArrayList();
		/* Modified for RBU changes */
		String sqlAlt = props.getValue("sqlAlt");

		String pendingSql = " select ACTIVITYFK, emp_no,'Pending' as status, completion_date as completedate  "
				+ " from mv_usp_pending  " + " union  ";

		if (!isDetail) {
			sqlAlt = "";
		}
		String sql = "select distinct emp_no,E.EMPLID, completedate, e.last_name as lastName,  f.last_name as mlastName, "
				+ " f.preferred_name as mpreferredName,e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, "
				+ " e.email_address as email, e.role_cd as role, e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, "
				+ " status,overall_rating, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				+ " from  (select activity_pk, emp_no,'Registered' as status, completion_date as completedate,null as score,null as overall_rating  "
				+ " from mv_usp_registered  where activity_pk = "
				+ activityid
				+ " "
				+ " minus "
				+ " select "
				+ activityid
				+ " as activity_pk, emp_no,'Registered' as status, completion_date as completedate,null as score,null as overall_rating "
				+ " from "
				+ " ((SELECT "
				+ activityid
				+ " AS activitycode, a.product_name AS activityname,b.guid AS emp_no, a.status, a.submitted_date, a.emplid, "
				+ " NULL AS score, a.overall_rating AS overall_rating FROM sce_fft a, mv_field_employee_rbu b WHERE a.product_cd = '"
				+ activityid
				+ "' "
				+ " AND a.emplid = b.emplid AND a.status = 'SUBMITTED'"
				+ " )) " + "V_SCE_CUE_TRAINING " + " union  ";
		String orderByClause = "order by e.last_name";

		if (phase.getApprovalStatus()) {
			sql = sql + pendingSql;
		}
		String subScriptionClause = " ";

		/*
		 * The following condition is for Admin users to see all the training
		 * records else see only those records of the employee having reports to
		 * as the logged in user's employee id
		 */
		if (uFilter.isAdmin() || uFilter.isTsrAdmin() || uFilter.isTsrOrAdmin()) {
			sql = sql
					+ " select activitycode,emp_no,'Complete' as status,submitted_date as completedate,score,overall_rating from "
					+ " (SELECT ACTIVITYCODE,EMP_NO,STATUS,SUBMITTED_DATE,SCORE,OVERALL_RATING FROM "
					+ " (SELECT "
					+ activityid
					+ " AS activitycode, a.product_name AS activityname,b.guid AS emp_no, a.status, a.submitted_date, a.emplid, "
					+ " NULL AS score, a.overall_rating AS overall_rating FROM sce_fft a, mv_field_employee_rbu b WHERE a.product_cd = '"
					+ activityid
					+ "' "
					+ " AND a.emplid = b.emplid AND a.status = 'SUBMITTED'"
					+ " ) "
					+ " )) a,mv_field_employee_rbu e, "
					+
					/* Added for RBU changes */
					" (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "
					+
					/* End of addition */
					" where a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
		} else {
			sql = sql
					+ "  select activitycode,emp_no,'Complete' as status,submitted_date as completedate, score,overall_rating from "
					+ " (SELECT ACTIVITYCODE,EMP_NO,STATUS,SUBMITTED_DATE,SCORE,OVERALL_RATING FROM "
					+ "  (SELECT "
					+ activityid
					+ " AS activitycode, a.product_name AS activityname,b.guid AS emp_no, a.status, a.submitted_date, a.emplid, "
					+ " NULL AS score, a.overall_rating AS overall_rating FROM sce_fft a, mv_field_employee_rbu b WHERE a.product_cd = '"
					+ activityid
					+ "' "
					+ " AND a.emplid = b.emplid AND a.status = 'SUBMITTED' "
					+ " ) "
					+ " )) a,  "
					+
					/* Added for RBU changes */
					"(SELECT (select distinct emplid "
					+ " from MV_FIELD_EMPLOYEE_RBU where emplid=x.reports_to_emplid) REPORTS_TO_EMPLID,"
					+ " emplid, GUID, empl_status, LEVEL org_level,last_name, preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc FROM MV_FIELD_EMPLOYEE_RBU x "
					+ " CONNECT BY PRIOR emplid=x.reports_to_emplid START WITH reports_to_emplid= ("
					+ emplid
					+ ")) e, "
					+ " (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "
					+
					/* End of addition */
					" where a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
		}

		// System.out.println("\n uFilter.isRefresh()"+uFilter.isRefresh());
		// added for RBU
		if (uFilter.isRefresh() == true) {
			sql = sql + buildCriteria(ut, uFilter) + orderByClause;
		} else {
			sql = sql + orderByClause;
		}
		// ended for RBU
		// System.out.println("Final SQL getCuePhaseStatus---\n"+sql);
		Timer timer = new Timer();
		List temp = executeSql2(sql);
		// Map master = new HashMap();
		// Coverting HashMap to LinkedHashMap to retain the sorting
		/* Modified to fix the default sorting of Last name */
		Map master = new LinkedHashMap();
		List sortedList = new ArrayList();

		P2lEmployeeStatus pStatus;
		for (int i = 0; i < temp.size(); i++) {
			Employee emp = new Employee();
			Map map = (Map) temp.get(i);
			emp.setManagerFname(Util.toEmpty((String) map.get("mpreferredName"
					.toUpperCase())));
			emp.setManagerLname(Util.toEmpty((String) map.get("mlastName"
					.toUpperCase())));
			emp.setGuid(Util.toEmpty((String) map.get("EMP_NO")));
			emp.setEmplId((String) map.get("EMPLID"));
			emp.setEmail(Util.toEmpty((String) map.get("EMAIL")));
			emp.setEmployeeStatus((String) map.get("EMPL_STATUS"));
			emp.setPreferredName((String) map.get("preferredName".toUpperCase()));
			emp.setFirstName((String) map.get("preferredName".toUpperCase()));
			emp.setLastName((String) map.get("lastName".toUpperCase()));
			emp.setRole((String) map.get("role".toUpperCase()));
			emp.setGeographyDesc((String) map.get("geoDesc".toUpperCase()));
			emp.setBusinessUnit((String) map.get("bu".toUpperCase()));
			emp.setSalesOrgDesc((String) map.get("salesOrgDesc".toUpperCase()));
			emp.setSalesPostionDesc((String) map.get("salesPositionDesc"
					.toUpperCase()));
			pStatus = new P2lEmployeeStatus(emp, (String) map.get("STATUS"),
					phase.getPhaseNumber());
			pStatus.setStatus((String) map.get("STATUS"));
			pStatus.setScore((String) map.get("overall_rating".toUpperCase()));
			// System.out.println("SCORE"+pStatus.getScore());
			Object obj = map.get("completedate".toUpperCase());
			if (obj != null) {
				// pStatus.setCompleteDate(new
				// java.sql.Date(((java.sql.Timestamp)obj).getTime()));
				pStatus.setCompleteDate(new java.sql.Date(
						((java.util.Date) obj).getTime()));
			} else {
				pStatus.setCompleteDate(null);
			}

			if (master.get(emp.getGuid()) == null) {
				master.put(emp.getGuid(), pStatus);
				// sortedList.add(pStatus);
			} else {
				P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus) master
						.get(emp.getGuid());
				if (pStatusTemp.getStatus().equals("RegisteredC")) {
					if (pStatus.getStatus().equals("Registered")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
					if (pStatus.getStatus().equals("Assigned")) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
					if (pStatus.getStatus().equals("Complete")
					// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Registered")) {
					if (pStatus.getStatus().equals("Complete")
							|| pStatus.getStatus().equals("Pending")
							// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Pending")) {
					if (pStatus.getStatus().equals("Complete")
					// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						sortedList.add(pStatus);
					}
				}
				// if ( pStatusTemp.getStatus().equals("Exempt") ) {
				if (pStatusTemp.getStatus().equals("Waived")) {
					if (pStatus.getStatus().equals("Complete")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Assigned")) {
					if (pStatus.getStatus().equals("Complete")
							|| pStatus.getStatus().equals("Registered")
							|| pStatus.getStatus().equals("Pending")
							// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
					if (pStatus.getStatus().equals("RegisteredC")) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}

			}
		}

		// return sortedList;
		return master.values();
	}

	public P2lTrack getCueTrack() {
		String sql = "Select track_id as trackid, track_label as trackLabel, track_type as trackType, do_overall as doOverall, do_complete as doComplete from p2l_track where track_id=(Select track_id from p2l_track where track_label='Psychiatry CUE Training')";

		List result = executeSql2(sql);
		// only expect 1
		P2lTrack track = null;
		if (result != null && result.size() > 0) {
			track = new P2lTrack();
			FormUtil.loadObject((Map) result.get(0), track, true);
			sql = "select a.sort_order as sortorder, m2.code as coursecodealt, m2.activityname as activitynamealt,m.code as coursecode, m.activityname, a.track_id as trackid,a.do_prerequisite as prerequisite,a.report_approval_status as approvalstatus, a.phase_number as phasenumber, a.do_assigned as assigned, a.do_exempt as exempt,a.track_phase_id trackPhaseId , a.root_activity_id as rootactivityid, a.alt_activity_id as altactivityid "
					+ " from p2l_track_phase a, mv_usp_activity_master m,mv_usp_activity_master m2 "
					+ " where  a.alt_activity_id = m2.activity_pk(+) and a.ROOT_ACTIVITY_ID = m.activity_pk(+) and a.track_id=(Select track_id from p2l_track where track_label='Psychiatry CUE Training') order by sort_order, a.phase_number";
			result = executeSql2(sql);
			log.info(sql);
			if (result != null && result.size() > 0) {
				List phases = new ArrayList();
				for (Iterator it = result.iterator(); it.hasNext();) {
					P2lTrackPhase phase = new P2lTrackPhase();
					FormUtil.loadObject((Map) it.next(), phase, true);
					phase.setTrack(track);
					// log.debug(phase);
					phases.add(phase);
				}
				track.setPhases(phases);
			}
		}
		return track;
	}

	/* Method added to get links for activity docs */
	public List getLinksForActivities(String activityId) {
		ResultSet rs = null;
		Statement st = null;
		List result = new ArrayList();
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
			String sql = "select DISPLAY_NAME,URL from ACTIVITY_LINKS where ACTIVITY_ID = '"
					+ activityId + "'";
			// System.out.println("Query to get link names   " + sql);

			// System.out.println(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ActivityDocsLinks links = new ActivityDocsLinks();
				if (rs.getString("DISPLAY_NAME") != null) {
					String displayName = rs.getString("DISPLAY_NAME");
					links.setDisplayName(displayName);
				}
				if (rs.getString("URL") != null) {
					String url = rs.getString("URL");
					links.setUrl(url);
				}
				links.setActivityId(activityId);
				result.add(links);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return result;
	}

	// Added for Major Enhancement 3.6 - F1

	public SubActivityBean getActivityDetails(String activityPk) {
		String sql = "select ACTIVITYNAME, ROOTACTIVITYFK from V_USP_ACTIVITY_MASTER where "
				+ "ACTIVITY_PK = " + activityPk;

		List temp = executeSql2(sql);

		SubActivityBean b = null;
		if (temp.size() > 0) {
			Map m = (HashMap) temp.get(0);

			b = new SubActivityBean();
			b.setActivityName((String) m.get("ACTIVITYNAME"));
			//if(m.get("ROOTACTIVITYFK")!=null)
			b.setRootActivityID(m.get("ROOTACTIVITYFK").toString());

			b.setActivityPK(activityPk);
		}
		return b;
	}

	// Start: Added for Major Enhancement 3.6 - F1

	public Collection getActivityStatus(UserTerritory ut, SubActivityBean bean,
			UserFilter uFilter) {
		ReadProperties props = new ReadProperties();
		String nodes = bean.getActivityPK();
		String completeNodes = bean.getActivityPK();
		// System.out.println("inside get activity status");
		boolean doFulltree = false;
		String emplid = uFilter.getEmployeeId();
		List result = new ArrayList();
		/* Modified for RBU changes */
		String sqlAlt = props.getValue("sqlAlt");
		// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
		// employee grid)

		// String pendingSql =
		// " select ACTIVITYFK, emp_no,'Pending' as status, null as completedate  "
		// +
		String pendingSql = " select ACTIVITYFK, emp_no,'Pending' as status, lstupd as completedate, 'NA' as score  "
				+ " from mv_usp_pending  " + " union  ";

		String sqlAllOptionalFields = " e.sex as gender, e.promotion_date as promoDate, f.email_address as memailaddress, e.sales_position_type_cd as source, e.hire_date as hireDate, e.state as state, e.home_state as home_state, e.source_org as source_org,e.sales_position_id as SALES_POSITION_ID, ";
		// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee
		// grid)
		// Start: added for HQ user requirement for TRT Phase 2 enhancement.
		String hqOptionalFields = "";
		// if (uFilter.isHqUser()) {
		hqOptionalFields = " ,field_active,is_hq_user,e.group_cd, reports_to_rel_1 ";
		// }

		// if ( !isDetail ) {
		// sqlAlt = "";
		// }
		// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
		// employee grid)
		String sql = "select distinct emp_no,score,E.EMPLID, completedate, "
				+ sqlAllOptionalFields
				+ "  e.last_name as lastName, e.first_name as firstName, f.last_name as mlastName, f.preferred_name as mpreferredName, e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, e.nt_id as NT_ID , completedate as statusdate, score, "
				+ " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				+
				// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
				// employee grid)
				// Start: added for HQ user requirement for TRT Phase 2
				// enhancement.
				hqOptionalFields
				+
				// End
				/*
				 * Start: Commenting for TRT 3.6 enhancement - F 4.5 -(user view
				 * of employee grid) String sql =
				 * "select distinct emp_no,E.EMPLID, completedate, e.last_name as lastName, e.first_name as firstName, "
				 * +
				 * "f.last_name as mlastName, f.preferred_name as mpreferredName,e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, "
				 * +
				 * "e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				 * + End of commenting
				 */

				/*
				 * Commenting out for CSO enhancement
				 * "from (select activity_pk, emp_no,'Registered' as status, null as completedate  "
				 * + "                   from mv_usp_registered  " +
				 * "               union  " +
				 * "               select activity_pk, emp_no,'Assigned' as status, null as completedate  "
				 * + "                   from mv_usp_assigned  " +
				 * "               union  " +
				 * "               select ACTIVITYFK, emp_no,'RegisteredC' as status, null as completedate  "
				 * + "                   from mv_usp_completed  " +
				 * "               union  "; End of commenting
				 */
				/*
				 * Log: Modified by Meenakshi.M.B on 14-May-2010 Changing the
				 * condition for CSO requirements. Introducing the new MV to
				 * improve performance of the query
				 */
				" from (select activity_pk, emp_no,status, completedate,score from mv_activity_status"
				+ " union ";

		String orderByClause = "order by e.last_name";

		// if (phase.getApprovalStatus()) {
		sql = sql + pendingSql;
		// }
		String subScriptionClause = " and rel_type <> 'Subscription' ";
		// if ("CPT".equals(phase.getTrack().getTrackType())) {
		// subScriptionClause = " ";
		// }
		/*
		 * The following condition is for Admin users to see all the training
		 * records else see only those records of the employee having reports to
		 * as the logged in user's employee id
		 */
		// Start: added for HQ user requirement for TRT Phase 2 enhancement.
		// if(uFilter.isAdmin() || uFilter.isTsrAdmin() ||
		// uFilter.isTsrOrAdmin())
		if (uFilter.isAdmin() || uFilter.isTsrAdmin() || uFilter.isTsrOrAdmin()
				|| uFilter.isHqUser()) {
			// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			// sql = sql +
			// "               select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			// +
			sql = sql
					+ "               select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score"
					+ "                   from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ "               union  "
					+
					// "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
					// +
					"               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score"
					+ "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+
					/* Added for RBU changes */
					// "(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "+

					// "(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, nt_id, sex, state, hire_date, promotion_date, sales_position_id_desc,sales_position_type_cd from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f "+
					"(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, nt_id, sex, state, home_state, hire_date, promotion_date, sales_position_id_desc,sales_position_type_cd,field_active,is_hq_user, reports_to_rel_1,source_org,sales_position_id from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f "
					+
					/* End of addition */
					" where a.activity_pk in  "
					+ "       (select activity_pk  "
					+ "           from mv_usp_activity_hierarchy  "
					+ "           start with activity_pk in ("
					+ nodes
					+ ")  "
					+ "           connect by prior activity_pk = prntactfk "
					+ subScriptionClause
					+ ") "
					+ "           and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
			// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
		} else {
			/* Commenting out for CSO requirement changes */
			/*
			 * sql = sql +
			 * "               select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			 * +
			 * "                   from mv_usp_completed c where c.status='Exempt' and c.activityfk in ("
			 * + completeNodes +")   " + "               union  " +
			 * "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
			 * +
			 * "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
			 * + completeNodes +") ) a,  " + // /* Added for RBU changes
			 * "(SELECT (select distinct emplid "+
			 * " from MV_FIELD_EMPLOYEE_RBU where emplid=x.reports_to_emplid) REPORTS_TO_EMPLID,"
			 * +
			 * " emplid, GUID, empl_status, LEVEL org_level,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc FROM MV_FIELD_EMPLOYEE_RBU x "
			 * +
			 * " CONNECT BY PRIOR emplid=x.reports_to_emplid START WITH reports_to_emplid= ("
			 * + emplid+
			 * ")) e, (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "
			 * + /* End of addition " where a.activity_pk in  " +
			 * "       (select activity_pk  " +
			 * "           from mv_usp_activity_hierarchy  " +
			 * "           start with activity_pk in (" + nodes + ")  " +
			 * "           connect by prior activity_pk = prntactfk " +
			 * subScriptionClause + ") " +
			 * "           and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) "
			 * ;
			 */
			/* End of comment */
			/*
			 * Modified Log by Meenakshi.M.B: Changing the query for CSO
			 * enhancement
			 */
			sql = sql
					+ " select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score "
					+ " from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ " union  "
					+ " select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score "
					+ " from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+ " (select x.reports_to_emplid,x.emplid,guid,x.empl_status,y.org_level, x.last_name,x.first_name,x.preferred_name,x.sales_group,group_cd, "
					+
					// " x.email_address,x.role_cd,x.geo_desc,x.bu,x.sales_position_id_desc,x.nt_id, x.sex, x.state, x.hire_date, x.promotion_date,x.sales_position_type_cd from mv_field_employee_rbu x, "+
					" x.email_address,x.role_cd,x.geo_desc,x.bu,x.sales_position_id_desc,x.nt_id, x.sex, x.state, x.home_state, x.hire_date, x.promotion_date,x.sales_position_type_cd , "
					+
					// Added extra fields for changes due to HQ users
					// requirement of TRT Phase 2 enhancement
					" field_active,is_hq_user, reports_to_rel_1,x.source_org,x.sales_position_id "
					+ " from mv_field_employee_rbu x, "
					+ " (select distinct sales_position_id,emplid,LEVEL as org_level from MV_FIELD_EMPLOYEE_RELATION connect by prior "
					+ " emplid=related_emplid start with related_emplid= ("
					+ emplid
					+ ")) y where x.emplid=y.emplid and x.sales_position_id=y.sales_position_id) e, "
					+ " (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f where a.activity_pk in (select activity_pk "
					+ " from mv_usp_activity_hierarchy  "
					+ " start with activity_pk in ("
					+ nodes
					+ ")  "
					+ " connect by prior activity_pk = prntactfk "
					+ subScriptionClause
					+ ") "
					+ " and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
		}

		// System.out.println("\n uFilter.isRefresh()"+uFilter.isRefresh());
		// added for RBU
		if (uFilter.isRefresh() == true) {
			sql = sql + buildCriteria(ut, uFilter) + orderByClause;
		} else {
			sql = sql + orderByClause;
		}
		// ended for RBU
		// System.out.println("Final SQL---\n"+sql);
		Timer timer = new Timer();
		List temp = executeSql2(sql);
		Map master = new LinkedHashMap();
		P2lEmployeeStatus pStatus;
		boolean curriculumCompletionFlag = false;
		List sortedList = new ArrayList();

		for (int i = 0; i < temp.size(); i++) {
			Employee emp = new Employee();
			String tempfname = "";
			Map map = (Map) temp.get(i);
			emp.setManagerFname(Util.toEmpty((String) map.get("mpreferredName"
					.toUpperCase())));
			emp.setManagerLname(Util.toEmpty((String) map.get("mlastName"
					.toUpperCase())));
			// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			emp.setManagerEmail(Util.toEmpty((String) map.get("MEMAILADDRESS"))); // for
																					// email
																					// of
																					// manager
			// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			emp.setRegionDesc(Util.toEmpty((String) map.get("regionDesc"
					.toUpperCase())));
			emp.setAreaDesc(Util.toEmpty((String) map.get("areaDesc"
					.toUpperCase())));
			emp.setGuid(Util.toEmpty((String) map.get("EMP_NO")));
			emp.setEmplId((String) map.get("EMPLID"));
			emp.setEmail(Util.toEmpty((String) map.get("EMAIL")));
			emp.setEmployeeStatus((String) map.get("EMPL_STATUS"));
			emp.setPreferredName((String) map.get("preferredName".toUpperCase()));
			emp.setFirstName((String) map.get("firstName".toUpperCase()));
			/* Code added for bug fix for first name display of MDM users */
			tempfname = emp.getFirstName();
			if (tempfname == null || tempfname.equals("")) {
				emp.setFirstName((String) map.get("firstName".toUpperCase()));
			}
			/* End of addition */
			emp.setLastName((String) map.get("lastName".toUpperCase()));
			emp.setDistrictDesc((String) map.get("districtDesc".toUpperCase()));
			emp.setTeamCode((String) map.get("teamCode".toUpperCase()));
			emp.setClusterCode((String) map.get("clusterCode".toUpperCase()));
			emp.setRole((String) map.get("role".toUpperCase()));
			emp.setGeographyDesc((String) map.get("geoDesc".toUpperCase()));
			emp.setBusinessUnit((String) map.get("bu".toUpperCase()));
			emp.setSalesOrgDesc((String) map.get("salesOrgDesc".toUpperCase()));
			emp.setSalesPostionDesc((String) map.get("salesPositionDesc"
					.toUpperCase()));

			// Start: Modified for TRT 3.6 enhancement - F 4.5 ( Display of
			// employee grid)

			emp.setNtId((String) map.get("NT_ID"));
			emp.setState((String) map.get("STATE"));
			emp.setPromoDate((Date) map.get("PROMODATE"));
			emp.setGender((String) map.get("GENDER"));
			emp.setHireDate((Date) map.get("HIREDATE"));
			emp.setSource((String) map.get("SOURCE"));
			emp.setSourceOrg((String) map.get("SOURCE_ORG"));
			/* Q42011 SALES POSITION ID ADDED */
			emp.setSalesPositionId((String) map.get("SALES_POSITION_ID"));
			emp.setHomeState((String) map.get("HOME_STATE"));

			pStatus = new P2lEmployeeStatus(emp, (String) map.get("STATUS"),
					null);
			pStatus.setStatus((String) map.get("STATUS"));
			pStatus.setScore((String) map.get("SCORE"));
			String score=null;
			//if(map.get("SCORE")!=null){
			score = map.get("SCORE").toString();
			if (score.equals("NA")) {

				pStatus.setScore("");
			} else {
				pStatus.setScore(map.get("SCORE").toString());

			}
			//}
			// End: Modified for TRT 3.6 enhancement - F 4.5( Display of
			// employee grid)
			// Start : Added for Phase 2 HQ Users requirement.
			emp.setGroupCD((String) map.get("GROUP_CD"));
			emp.setHQManager(Util.toEmpty((String) map.get("reports_to_rel_1"
					.toUpperCase())));
			// End

			Object obj = map.get("completedate".toUpperCase());
			if (obj != null) {
				pStatus.setStatusDate(new java.sql.Date(((java.util.Date) map
						.get("completedate".toUpperCase())).getTime()));
				pStatus.setCompleteDate(new java.sql.Date(
						((java.util.Date) obj).getTime()));
			} else {
				pStatus.setCompleteDate(null);
			}
			if (master.get(emp.getGuid()) == null) {
				curriculumCompletionFlag = pStatus.getStatus()
						.equalsIgnoreCase("Complete")
						|| pStatus.getStatus().equalsIgnoreCase("Waived");
				if (pStatus.getStatus().equalsIgnoreCase("RegisteredC"))
					continue;
				master.put(emp.getGuid(), pStatus);
			} else {

				if (!curriculumCompletionFlag) {
					curriculumCompletionFlag = pStatus.getStatus()
							.equalsIgnoreCase("Complete")
							|| pStatus.getStatus().equalsIgnoreCase("Waived");
				}
				P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus) master
						.get(emp.getGuid());

				Date d1 = pStatusTemp.getStatusDate();
				Date d2 = pStatus.getStatusDate();
				if (pStatus.getStatus().equalsIgnoreCase("Assigned")) {
					if (!pStatusTemp.getStatus().equalsIgnoreCase("Assigned")
							|| pStatus.getStatus().equalsIgnoreCase(
									"RegisteredC"))
						continue;
					else {
						master.put(emp.getGuid(), pStatus);
					}
				} else if (pStatus.getStatus().equalsIgnoreCase("RegisteredC"))
					continue;
				else if (d1 == null || d2 == null)
					continue;
				else if (d2.after(d1) || d2.equals(d1)) {
					// else
					// if(pStatus.getStatus().equalsIgnoreCase("RegisteredC"))
					// continue;
					if (curriculumCompletionFlag) {
						if (pStatusTemp.getStatus().equalsIgnoreCase(
								"Cancelled")
								&& (pStatus.getStatus().equalsIgnoreCase(
										"Complete") || pStatus.getStatus()
										.equalsIgnoreCase("Waived"))) {
							master.put(emp.getGuid(), pStatus);
						} else if ((pStatusTemp.getStatus().equalsIgnoreCase(
								"Complete") || pStatusTemp.getStatus()
								.equalsIgnoreCase("Waived"))
								&& pStatus.getStatus().equalsIgnoreCase(
										"Cancelled"))
							continue;
						else if (pStatus.getStatus().equalsIgnoreCase(
								"Registered")
								|| pStatus.getStatus().equalsIgnoreCase(
										"In Progress")) {
							master.put(emp.getGuid(), pStatus);
						} else {
							if (pStatus.getStatus().equalsIgnoreCase("No Show"))
								continue;
							master.put(emp.getGuid(), pStatus);
						}

					} else {
						master.put(emp.getGuid(), pStatus);
					}
				}

				/*
				 * if(obj!=null){ // pStatus.setCompleteDate(new
				 * java.sql.Date(((java.sql.Timestamp)obj).getTime()));
				 * pStatus.setCompleteDate(new
				 * java.sql.Date(((java.util.Date)obj).getTime())); }else{
				 * pStatus.setCompleteDate(null); } if ( master.get(
				 * emp.getGuid()) == null) { master.put(emp.getGuid(),pStatus);
				 * sortedList.add(pStatus); } else { P2lEmployeeStatus
				 * pStatusTemp = (P2lEmployeeStatus)master.get( emp.getGuid());
				 * 
				 * 
				 * 
				 * /*if ( pStatusTemp.getStatus().equals("RegisteredC") ) { if (
				 * pStatus.getStatus().equals("Registered") ) {
				 * master.put(emp.getGuid(),pStatus); //sortedList.add(pStatus);
				 * } if ( pStatus.getStatus().equals("Assigned") ) {
				 * pStatus.setStatus("Registered");
				 * master.put(emp.getGuid(),pStatus); //sortedList.add(pStatus);
				 * } if ( pStatus.getStatus().equals("Complete") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); //sortedList.add(pStatus);
				 * } } if ( pStatusTemp.getStatus().equals("Registered") ) {
				 * //Start: Added for Major Enhancement 3.6 - F1
				 * 
				 * if ( pStatus.getStatus().equals("Complete") ||
				 * pStatus.getStatus().equals("Pending") ||
				 * pStatus.getStatus().equals("Cancelled") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); //
				 * sortedList.add(pStatus); } } if (
				 * pStatusTemp.getStatus().equals("Cancelled") ) { if (
				 * pStatus.getStatus().equals("Complete") ||
				 * pStatus.getStatus().equals("Pending") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); //
				 * sortedList.add(pStatus); } } if (
				 * pStatusTemp.getStatus().equals("Pending") ) { if (
				 * pStatus.getStatus().equals("Complete") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); sortedList.add(pStatus); }
				 * } //if ( pStatusTemp.getStatus().equals("Exempt") ) { if (
				 * pStatusTemp.getStatus().equals("Waived") ) { if
				 * (pStatus.getStatus().equals("Complete")) {
				 * master.put(emp.getGuid(),pStatus); //sortedList.add(pStatus);
				 * } } if ( pStatusTemp.getStatus().equals("Assigned") ) { if (
				 * pStatus.getStatus().equals("Complete") ||
				 * pStatus.getStatus().equals("Registered") ||
				 * pStatus.getStatus().equals("Pending") //||
				 * pStatus.getStatus().equals("Exempt")) { ||
				 * pStatus.getStatus().equals("Waived")) {
				 * master.put(emp.getGuid(),pStatus); sortedList.add(pStatus); }
				 * if ( pStatus.getStatus().equals("RegisteredC") ) {
				 * pStatus.setStatus("Registered");
				 * master.put(emp.getGuid(),pStatus); //sortedList.add(pStatus);
				 * } }
				 */

			}

		}
		/* This was the old code */
		return master.values();

		// return sortedList;
	}

	public Collection getSubSetActivityStatus(UserTerritory ut,
			SubActivityBean bean, UserFilter uFilter) {
		ReadProperties props = new ReadProperties();
		String nodes = bean.getActivityPK();
		String completeNodes = bean.getActivityPK();

		boolean doFulltree = false;
		String emplid = uFilter.getEmployeeId();
		List result = new ArrayList();
		/* Modified for RBU changes */
		String sqlAlt = props.getValue("sqlAlt");
		// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
		// employee grid)

		// String pendingSql =
		// " select ACTIVITYFK, emp_no,'Pending' as status, null as completedate  "
		// +
		String pendingSql = " select ACTIVITYFK, emp_no,'Pending' as status, lstupd as completedate, 'NA' as score  "
				+ " from mv_usp_pending  " + " union  ";

		String sqlAllOptionalFields = " e.sex as gender, e.promotion_date as promoDate, f.email_address as memailaddress, e.sales_position_type_cd as source, e.hire_date as hireDate, e.state as state, e.home_state as home_state, ";
		// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee
		// grid)

		// if ( !isDetail ) {
		// sqlAlt = "";
		// }
		// Start: added for HQ user requirement for TRT Phase 2 enhancement.
		String hqOptionalFields = "";
		// if (uFilter.isHqUser()) {
		hqOptionalFields = " ,field_active,is_hq_user,e.group_cd, reports_to_rel_1 ";
		// }
		// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
		// employee grid)
		String sql = "select distinct emp_no,score,E.EMPLID, completedate, "
				+ sqlAllOptionalFields
				+ "  e.last_name as lastName, e.first_name as firstName, f.last_name as mlastName, f.preferred_name as mpreferredName, e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, e.nt_id as NT_ID , completedate as statusdate, score, "
				+ " status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				+
				// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
				// employee grid)
				// Start: added for HQ user requirement for TRT Phase 2
				// enhancement.
				hqOptionalFields
				+
				// End
				/*
				 * Start: Commenting for TRT 3.6 enhancement - F 4.5 -(user view
				 * of employee grid) String sql =
				 * "select distinct emp_no,E.EMPLID, completedate, e.last_name as lastName, e.first_name as firstName, "
				 * +
				 * "f.last_name as mlastName, f.preferred_name as mpreferredName,e.sales_group as salesOrgDesc, e.bu as bu, e.geo_desc as geoDesc, e.email_address as email, e.role_cd as role, "
				 * +
				 * "e.preferred_name as preferredName, e.sales_position_id_desc as salesPositionDesc, status, DECODE(e.empl_status,'A','Active','L','On-Leave','P','On-Leave','T','Terminated') as empl_status "
				 * + End of commenting
				 */

				/*
				 * Commenting out for CSO enhancement
				 * "from (select activity_pk, emp_no,'Registered' as status, null as completedate  "
				 * + "                   from mv_usp_registered  " +
				 * "               union  " +
				 * "               select activity_pk, emp_no,'Assigned' as status, null as completedate  "
				 * + "                   from mv_usp_assigned  " +
				 * "               union  " +
				 * "               select ACTIVITYFK, emp_no,'RegisteredC' as status, null as completedate  "
				 * + "                   from mv_usp_completed  " +
				 * "               union  "; End of commenting
				 */
				/*
				 * Log: Modified by Meenakshi.M.B on 14-May-2010 Changing the
				 * condition for CSO requirements. Introducing the new MV to
				 * improve performance of the query
				 */
				" from (select activity_pk, emp_no,status, completedate,score from mv_activity_status"
				+ " union ";

		String orderByClause = "order by e.last_name";

		// if (phase.getApprovalStatus()) {
		sql = sql + pendingSql;
		// }
		String subScriptionClause = " and rel_type <> 'Subscription' ";
		// if ("CPT".equals(phase.getTrack().getTrackType())) {
		// subScriptionClause = " ";
		// }
		/*
		 * The following condition is for Admin users to see all the training
		 * records else see only those records of the employee having reports to
		 * as the logged in user's employee id
		 */
		// Start: added for HQ user requirement for TRT Phase 2 enhancement.
		// if(uFilter.isAdmin() || uFilter.isTsrAdmin() ||
		// uFilter.isTsrOrAdmin())
		if (uFilter.isAdmin() || uFilter.isTsrAdmin() || uFilter.isTsrOrAdmin()
				|| uFilter.isHqUser()) {
			// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			// sql = sql +
			// "               select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			// +
			sql = sql
					+ "               select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score"
					+ "                   from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ "               union  "
					+
					// "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
					// +
					"               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score"
					+ "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+
					/* Added for RBU changes */
					// "(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "+
					// Start: added for HQ user requirement for TRT Phase 2
					// enhancement.
					// "(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, nt_id, sex, state, hire_date, promotion_date, sales_position_id_desc,sales_position_type_cd from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f "+
					"(select distinct emplid,reports_to_emplid, GUID,empl_status,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, nt_id, sex, state, home_state, hire_date, promotion_date, sales_position_id_desc,sales_position_type_cd,field_active,is_hq_user, reports_to_rel_1 from mv_field_employee_rbu) e, (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f "
					+
					/* End of addition */
					" where a.activity_pk in  "
					+ "       (select activity_pk  "
					+ "           from mv_usp_activity_hierarchy  "
					+ "           start with activity_pk in ("
					+ nodes
					+ ")  "
					+ "           connect by prior activity_pk = prntactfk "
					+ subScriptionClause
					+ ") "
					+ "           and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
			// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
		} else {
			/* Commenting out for CSO requirement changes */
			/*
			 * sql = sql +
			 * "               select ACTIVITYFK, emp_no,'Exempt' as status, null as completedate  "
			 * +
			 * "                   from mv_usp_completed c where c.status='Exempt' and c.activityfk in ("
			 * + completeNodes +")   " + "               union  " +
			 * "               select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate  "
			 * +
			 * "                   from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
			 * + completeNodes +") ) a,  " + // /* Added for RBU changes
			 * "(SELECT (select distinct emplid "+
			 * " from MV_FIELD_EMPLOYEE_RBU where emplid=x.reports_to_emplid) REPORTS_TO_EMPLID,"
			 * +
			 * " emplid, GUID, empl_status, LEVEL org_level,last_name,first_name,preferred_name,sales_group,group_cd,email_address,role_cd,geo_desc, bu, sales_position_id_desc FROM MV_FIELD_EMPLOYEE_RBU x "
			 * +
			 * " CONNECT BY PRIOR emplid=x.reports_to_emplid START WITH reports_to_emplid= ("
			 * + emplid+
			 * ")) e, (select distinct emplid, last_name, preferred_name from mv_field_employee_rbu ) f "
			 * + /* End of addition " where a.activity_pk in  " +
			 * "       (select activity_pk  " +
			 * "           from mv_usp_activity_hierarchy  " +
			 * "           start with activity_pk in (" + nodes + ")  " +
			 * "           connect by prior activity_pk = prntactfk " +
			 * subScriptionClause + ") " +
			 * "           and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) "
			 * ;
			 */
			/* End of comment */
			/*
			 * Modified Log by Meenakshi.M.B: Changing the query for CSO
			 * enhancement
			 */
			sql = sql
					+ " select ACTIVITYFK, emp_no,'Waived' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score  "
					+ " from mv_usp_completed c where c.status='Waived' and c.activityfk in ("
					+ completeNodes
					+ ")   "
					+ " union  "
					+ " select ACTIVITYFK, emp_no,'Complete' as status, completion_date as completedate, decode(score,null,'NA','', 'NA',score) as score  "
					+ " from mv_usp_completed c where c.status='Complete' and c.activityfk in ("
					+ completeNodes
					+ ") ) a,  "
					+ " (select x.reports_to_emplid,x.emplid,guid,x.empl_status,y.org_level, x.last_name,x.first_name,x.preferred_name,x.sales_group,group_cd, "
					+
					// " x.email_address,x.role_cd,x.geo_desc,x.bu,x.sales_position_id_desc,x.nt_id, x.sex, x.state, x.hire_date, x.promotion_date,x.sales_position_type_cd from mv_field_employee_rbu x, "+
					" x.email_address,x.role_cd,x.geo_desc,x.bu,x.sales_position_id_desc,x.nt_id, x.sex, x.state, x.home_state, x.hire_date, x.promotion_date,x.sales_position_type_cd, "
					+
					// Added extra fields for changes due to HQ users
					// requirement of TRT Phase 2 enhancement
					" field_active,is_hq_user, reports_to_rel_1 "
					+ " from mv_field_employee_rbu x, "
					+ " (select distinct sales_position_id,emplid,LEVEL as org_level from MV_FIELD_EMPLOYEE_RELATION connect by prior "
					+ " emplid=related_emplid start with related_emplid= ("
					+ emplid
					+ ")) y where x.emplid=y.emplid and x.sales_position_id=y.sales_position_id) e, "
					+ " (select distinct emplid, last_name, preferred_name,email_address from mv_field_employee_rbu ) f where a.activity_pk in (select activity_pk "
					+ " from mv_usp_activity_hierarchy  "
					+ " start with activity_pk in ("
					+ nodes
					+ ")  "
					+ " connect by prior activity_pk = prntactfk "
					+ subScriptionClause
					+ ") "
					+ " and a.emp_no=e.guid and e.reports_to_emplid = f.emplid (+) ";
		}

		// System.out.println("\n uFilter.isRefresh()"+uFilter.isRefresh());
		// added for RBU
		if (uFilter.isRefresh() == true) {
			sql = sql + buildCriteria(ut, uFilter) + orderByClause;
		} else {
			sql = sql + orderByClause;
		}
		// ended for RBU
		// System.out.println("Final SQL---\n"+sql);
		Timer timer = new Timer();
		List temp = executeSql2(sql);
		// Map master = new HashMap();
		/* Modified to fix the default sorting of Last name */
		Map master = new LinkedHashMap();
		P2lEmployeeStatus pStatus;

		List sortedList = new ArrayList();

		for (int i = 0; i < temp.size(); i++) {
			Employee emp = new Employee();
			String tempfname = "";
			Map map = (Map) temp.get(i);
			emp.setManagerFname(Util.toEmpty((String) map.get("mpreferredName"
					.toUpperCase())));
			emp.setManagerLname(Util.toEmpty((String) map.get("mlastName"
					.toUpperCase())));
			// Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			emp.setManagerEmail(Util.toEmpty((String) map.get("MEMAILADDRESS"))); // for
																					// email
																					// of
																					// manager
			// End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of
			// employee grid)
			emp.setRegionDesc(Util.toEmpty((String) map.get("regionDesc"
					.toUpperCase())));
			emp.setAreaDesc(Util.toEmpty((String) map.get("areaDesc"
					.toUpperCase())));
			emp.setGuid(Util.toEmpty((String) map.get("EMP_NO")));
			emp.setEmplId((String) map.get("EMPLID"));
			emp.setEmail(Util.toEmpty((String) map.get("EMAIL")));
			emp.setEmployeeStatus((String) map.get("EMPL_STATUS"));
			emp.setPreferredName((String) map.get("preferredName".toUpperCase()));
			emp.setFirstName((String) map.get("preferredName".toUpperCase()));
			/* Code added for bug fix for first name display of MDM users */
			tempfname = emp.getFirstName();
			if (tempfname == null || tempfname.equals("")) {
				emp.setFirstName((String) map.get("firstName".toUpperCase()));
			}
			/* End of addition */
			emp.setLastName((String) map.get("lastName".toUpperCase()));
			emp.setDistrictDesc((String) map.get("districtDesc".toUpperCase()));
			emp.setTeamCode((String) map.get("teamCode".toUpperCase()));
			emp.setClusterCode((String) map.get("clusterCode".toUpperCase()));
			emp.setRole((String) map.get("role".toUpperCase()));
			emp.setGeographyDesc((String) map.get("geoDesc".toUpperCase()));
			emp.setBusinessUnit((String) map.get("bu".toUpperCase()));
			emp.setSalesOrgDesc((String) map.get("salesOrgDesc".toUpperCase()));
			emp.setSalesPostionDesc((String) map.get("salesPositionDesc"
					.toUpperCase()));

			// Start: Modified for TRT 3.6 enhancement - F 4.5 ( Display of
			// employee grid)

			emp.setNtId((String) map.get("NT_ID"));
			emp.setState((String) map.get("STATE"));
			emp.setPromoDate((Date) map.get("PROMODATE"));
			emp.setGender((String) map.get("GENDER"));
			emp.setHireDate((Date) map.get("HIREDATE"));
			emp.setSource((String) map.get("SOURCE"));
			emp.setHomeState((String) map.get("HOME_STATE"));

			pStatus = new P2lEmployeeStatus(emp, (String) map.get("STATUS"),
					null);
			pStatus.setStatus((String) map.get("STATUS"));
			pStatus.setScore((String) map.get("SCORE"));
			String score=null;
			//if(map.get("SCORE")!=null){
			score = map.get("SCORE").toString();
			if (score.equals("NA")) {

				pStatus.setScore("");
			} else {
				pStatus.setScore(map.get("SCORE").toString());

			}
			//}
			// End: Modified for TRT 3.6 enhancement - F 4.5( Display of
			// employee grid)
			// Start : Added for Phase 2 HQ Users requirement.
			emp.setGroupCD((String) map.get("GROUP_CD"));
			emp.setHQManager(Util.toEmpty((String) map.get("reports_to_rel_1"
					.toUpperCase())));
			// End

			Object obj = map.get("completedate".toUpperCase());
			if (obj != null) {
				// pStatus.setCompleteDate(new
				// java.sql.Date(((java.sql.Timestamp)obj).getTime()));
				pStatus.setCompleteDate(new java.sql.Date(
						((java.util.Date) obj).getTime()));
			} else {
				pStatus.setCompleteDate(null);
			}
			if (master.get(emp.getGuid()) == null) {
				master.put(emp.getGuid(), pStatus);
				sortedList.add(pStatus);
			} else {
				P2lEmployeeStatus pStatusTemp = (P2lEmployeeStatus) master
						.get(emp.getGuid());
				if (pStatusTemp.getStatus().equals("RegisteredC")) {
					if (pStatus.getStatus().equals("Registered")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
					if (pStatus.getStatus().equals("Assigned")) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
					if (pStatus.getStatus().equals("Complete")
					// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Registered")) {
					// Start: Added for Major Enhancement 3.6 - F1

					if (pStatus.getStatus().equals("Complete")
							|| pStatus.getStatus().equals("Pending")
							|| pStatus.getStatus().equals("Cancelled")
							// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Cancelled")) {
					if (pStatus.getStatus().equals("Complete")
							|| pStatus.getStatus().equals("Pending")
							// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Pending")) {
					if (pStatus.getStatus().equals("Complete")
					// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}
				// if ( pStatusTemp.getStatus().equals("Exempt") ) {
				if (pStatusTemp.getStatus().equals("Waived")) {
					if (pStatus.getStatus().equals("Complete")) {
						master.put(emp.getGuid(), pStatus);
						sortedList.add(pStatus);
					}
				}
				if (pStatusTemp.getStatus().equals("Assigned")) {
					if (pStatus.getStatus().equals("Complete")
							|| pStatus.getStatus().equals("Registered")
							|| pStatus.getStatus().equals("Pending")
							// || pStatus.getStatus().equals("Exempt")) {
							|| pStatus.getStatus().equals("Waived")) {
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
					if (pStatus.getStatus().equals("RegisteredC")) {
						pStatus.setStatus("Registered");
						master.put(emp.getGuid(), pStatus);
						// sortedList.add(pStatus);
					}
				}

			}

		}
		/* This was the old code */
		return master.values();

		// return sortedList;
	}

	public boolean checkGapTrack() {
		List result = DBUtil
				.executeSql(
						"SELECT * FROM TRAINING_REPORT WHERE TRACK_ID LIKE '%GAP%' AND ACTIVE=1",
						AppConst.APP_DATASOURCE);
		if (result != null && result.size() > 0) {
			// P2lTrackPhase phase = new P2lTrackPhase();
			// phase.setGapExists(true);
			return true;
		}
		return false;
	}

	public boolean checkGapTrackByMenuId(String menuId) {
		List result = DBUtil
				.executeSql(
						"SELECT * FROM TRAINING_REPORT WHERE TRACK_ID LIKE '%GAP%' AND TRAINING_REPORT_ID="
								+ menuId, AppConst.APP_DATASOURCE);
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}

	public String getActiveGapMenuId() {
		List result = DBUtil
				.executeSql(
						"SELECT TRAINING_REPORT_ID FROM TRAINING_REPORT WHERE TRACK_ID LIKE '%GAP%' AND ACTIVE=1",
						AppConst.APP_DATASOURCE);
		if (result != null && result.size() > 0) {
			// P2lTrackPhase phase = new P2lTrackPhase();
			// phase.setGapExists(true);
			Map map = new HashMap();

			map = (HashMap) result.get(0);
			//if(map.get("TRAINING_REPORT_ID")!=null){
			String str = map.get("TRAINING_REPORT_ID").toString();
			return str;
			//}
		}
		return " ";
	}

	public String insertGapTrack(String name) {

		List result = DBUtil.executeSql(
				"Select GAP_TRACK_ID_SEQ.nextval as nextGapId from dual",
				AppConst.APP_DATASOURCE);
		Map map = (Map) result.get(0);
		// System.out.println(" result.get(0) = "+result.get(0));
		String retString = null;

		String insertSql = " insert into GAP_ANALYSIS_TRACK "
				+ " (TRACK_ID,TRACK_LABEL,TRACK_TYPE) "
				+ " values(?,?,'GapAnalysis') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		String nextValue = null;

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
			statement = conn.prepareStatement(insertSql);
			nextValue = "GAP_" + (BigDecimal) map.get("NEXTGAPID");
			// System.out.println("nextValue GapTrackId = "+nextValue);

			// statement.setBigDecimal( 1, (BigDecimal)map.get("nextGapId") );
			statement.setString(1, nextValue);
			statement.setString(2, name);

			// System.out.println(statement);
			LoggerHelper.logSystemDebug("forecast Final SQL---\n" + insertSql);

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

	public boolean insertTrainingReportsForGap(P2lTrack track, String menuId) {

		String retString = null;
		String insertSql = "insert into  training_report  "
				+ "   (training_report_id, training_report_label,training_report_url,parent,active,allow_group, track_id,delete_flag) "
				+ "   values (TRAINING_REPORT_ID_SEQ.nextval,?,?,?,1,null,?,'N') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
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
			statement = conn.prepareStatement(insertSql);
			statement.setString(1, track.getTrackLabel());
			statement.setString(
					2,
					"/TrainingReports/gapAnalysis/begin?track="
							+ track.getTrackId());
			statement.setBigDecimal(3, new BigDecimal(menuId));
			statement.setString(4, track.getTrackId());

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

	// ////////END:MERGING///////////////////////////

}
