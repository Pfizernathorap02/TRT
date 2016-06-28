package com.pfizer.hander;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.db.BatchJob;
import com.pfizer.db.RBUEmailReminder;
import com.pfizer.db.RBUEmailReminderHistory;
import com.pfizer.db.TrAudit;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.Utils.Timer;

public class AuditHandler {
	protected static final Log log = LogFactory.getLog(AuditHandler.class);

	String insertSql = "insert into tr_website_audit "
			+ " (audit_id, user_id, action, page_name, filter_pie, filter_slice, filter_product, filter_area, filter_region, filter_district, action_date) "
			+ " values ( tr_website_audit_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ";

	/*
	 * String insertSql = "insert into tr_website_audit " +
	 * " (audit_id, user_id, action, page_name, filter_pie, filter_slice, filter_product, filter_area, filter_region, filter_district, action_date) "
	 * +
	 * " values ( tr_website_audit_seq.nextval, :userId, :action, :pageName, :pie, :slice, :product, :area, :region, :district, sysdate) "
	 * ;
	 */

	public void insertAuditByFilter(UserFilter filter, User user, String type) {
		TrAudit audit = new TrAudit(user, type);
		audit.setSlice(filter.getQuseryStrings().getSection());
		audit.setProduct(filter.getProduct());
		audit.setArea(filter.getFilterForm().getArea());
		audit.setRegion(filter.getFilterForm().getRegion());
		audit.setDistrict(filter.getFilterForm().getDistrict());
		audit.setPie(filter.getQuseryStrings().getType());
		insertAudit(audit);
	}

	public void insertAuditLogin(User user) {
		TrAudit audit = new TrAudit(user, TrAudit.ACTION_LOGIN);
		insertAudit(audit);
	}

	public void insertEmailAudit(String emplid) {

		TrAudit audit = new TrAudit();
		audit.setAction("email");
		audit.setUserId(emplid);
		log.info("inserting email audit:" + emplid);
		insertAudit(audit);
	}

	private void insertAudit(TrAudit audit) {
		String retString = null;
		System.out.print("Inside insertAudit");
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
			
			statement.setString(1, audit.getUserId());
			statement.setString(2, audit.getAction());
			statement.setString(3, audit.getPageName());
			statement.setString(4, audit.getPie());
			statement.setString(5, audit.getSlice());
			statement.setString(6, audit.getProduct());
			statement.setString(7, audit.getArea());
			statement.setString(8, audit.getRegion());
			statement.setString(9, audit.getDistrict());

			statement.executeUpdate();
			System.out.print(insertSql);
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

	public void updateBatchJob(BatchJob job) {
		String retString = null;

		ResultSet rs = null;
		PreparedStatement statement = null;

		String sql;
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
			sql = "update batch_job set status = ? , last_run = ?, comment_last_run = ? where name='NOT_COMPLETE_EMAIL'";
			statement = conn.prepareStatement(sql);

			statement.setString(1, job.getStatus());
			statement.setDate(2, new java.sql.Date(job.getLastRun().getTime()));
			statement.setString(3, job.getComment());

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

	public void updateRBUEmailReminderJob(String status) {
		String retString = null;

		ResultSet rs = null;
		Statement statement = null;

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
			statement = conn.createStatement();
			String sql = "update RBU_SUSTAINABILITY_JOB set JOB_STATUS = '"
					+ status + "'  where JOB_ID=1";
			statement.execute(sql);
			conn.commit();
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

	public void insertRBUEmailReminderHistory(RBUEmailReminderHistory job) {
		String retString = null;
		ResultSet rs = null;
		Statement statement = null;

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
			statement = conn.createStatement();
			String sql = "insert into RBU_SUSTAINABILITY_JOB_HISTORY(JOB_ID,MANAGER_EMPLID,RUN_DATE,REPORTS_EMPLID,HISTORY_ID)"
					+ " VALUES ("
					+ job.getJobId()
					+ ", '"
					+ job.getManagerEmplId()
					+ "', sysdate, '"
					+ job.getReportsEmplId()
					+ "', "
					+ job.getHistoryId()
					+ ") ";
			System.out.println("Query to insert ############### " + sql);
			statement.execute(sql);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occured " + e.getMessage());
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

	public long getEmailHistoryId() {
		long ret = 0;
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

			String sql = "select max(history_id) as id from RBU_SUSTAINABILITY_JOB_HISTORY";

			log.info(sql);
			rs = st.executeQuery(sql);
			if (rs.next()) {
				ret = rs.getLong("ID");
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

	public Date getLatestRunDate() {
		Date ret = null;
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

			String sql = "select max(run_date) as lastRun from RBU_SUSTAINABILITY_JOB_HISTORY";

			log.info(sql);
			rs = st.executeQuery(sql);
			if (rs.next()) {
				Timestamp t = rs.getTimestamp("lastRun".toUpperCase());
				long milliseconds = t.getTime() + (t.getNanos() / 1000000);
				ret = new java.util.Date(milliseconds);

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

	public BatchJob getEmailBatchJob() {
		BatchJob ret = null;
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

			String sql = "select" + " batch_id as batchId, " + " name, "
					+ " status, " + " last_run as lastRun, "
					+ " comment_last_run as run_comment " + "from"
					+ " batch_job " + " where "
					+ " name = 'NOT_COMPLETE_EMAIL' ";

			log.info(sql);
			ArrayList tempList = new ArrayList();

			rs = st.executeQuery(sql);
			if (rs.next()) {
				ret = new BatchJob();
				ret.setBatchId(rs.getLong("batchId".toUpperCase()));
				ret.setName(rs.getString("name".toUpperCase()));
				ret.setStatus(rs.getString("status".toUpperCase()));
				ret.setComment(rs.getString("run_comment".toUpperCase()));
				ret.setLastRun(rs.getDate("lastRun".toUpperCase()));
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

	public RBUEmailReminder getEmailBatchJobForRBU() {
		RBUEmailReminder ret = null;
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

			String sql = "select"
					+ " JOB_ID as jobId, "
					+ " name, "
					+ " job_status as status, "
					+ " run_frequency_days as days, START_DATE as startDate, EMAIL_SUBJECT as subject"
					+ " from " + " RBU_SUSTAINABILITY_JOB " + " where "
					+ " JOB_ID = 1 ";

			log.info(sql);
			System.out.println("Get the email job" + sql);
			rs = st.executeQuery(sql);
			if (rs.next()) {
				ret = new RBUEmailReminder();
				ret.setJobId(rs.getLong("jobId".toUpperCase()));
				ret.setName(rs.getString("name".toUpperCase()));
				ret.setStatus(rs.getString("status".toUpperCase()));
				ret.setDays(rs.getDouble("days".toUpperCase()));
				Timestamp t = rs.getTimestamp("startDate".toUpperCase());
				long milliseconds = t.getTime() + (t.getNanos() / 1000000);
				ret.setStartDate(new java.util.Date(milliseconds));
				ret.setSubject(rs.getString("subject").toUpperCase());
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
	 * This method gets the database url which will be used to determeine the
	 * environment
	 */
	/*
	 * public String getDatabaseURL() { String ret = ""; ResultSet rs = null;
	 * Statement st = null; Connection conn = null;
	 * 
	 * try { Context ctx = new InitialContext();
	 * 
	 * Timer timer = new Timer();
	 * 
	 * DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE); conn =
	 * ds.getConnection(); DatabaseMetaData meta = conn.getMetaData();
	 * System.out.println("URL here is >>>>>>>>>>> " + meta.getURL()); ret =
	 * meta.getURL(); //System.out.println(conn.get); } catch (Exception e) {
	 * log.error(e,e); } finally { if ( conn != null) { try { conn.close(); }
	 * catch ( Exception e2) { log.error(e2,e2); } } } return ret; }
	 */

	public HashMap getEnvParameters() {
		HashMap ret = new HashMap();
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
			String sql = "select ENVIRONMENT as ENV, HOST_NAME as hostName from RBU_SUSTAINABILITY_ENV_PARAMS";
			log.info(sql);
			rs = st.executeQuery(sql);
			if (rs.next()) {
				String env = rs.getString("ENV");
				String hostName = rs.getString("hostName");
				ret.put(env, hostName);
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
}
